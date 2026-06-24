package nc.ui.ic.m4z.action;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.m4z.IFreezeCheck;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.medpub.util.SysInitParamUtil;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.ui.ic.m4z.model.FreezeThawDataManager;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.AppUiContext;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.BillListView;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.ic.m4z.entity.FreezeThawVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.param.ICSysParam;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

import org.apache.commons.collections.CollectionUtils;
/**
 * 库存报检
 * @author yunfeng.li
 *
 */
@SuppressWarnings("all")
 public class CheckAction extends NCAction
   {
     private static final long serialVersionUID = 3326735260844537610L;
     private FreezeThawDataManager dataManager;
     private BillListView list;
     private BillManageModel model;
   
     public CheckAction()
     {
    	SCMActionInitializer.initializeAction(this, "Verify");

     }
      
     public void doAction(ActionEvent e)
       throws Exception
       {    	 
	     stopEdit();
	     onBeforeICCheck();
	     FreezeThawVO[] vos = (FreezeThawVO[])(FreezeThawVO[])this.list.getBillListPanel().getHeadBillModel().getBodySelectedVOs(FreezeThawVO.class.getName());
	 	
	     if (ValueCheckUtil.isNullORZeroLength(vos)) {
	       return;
	     }  
	     /*库存报检”整批报检“必填
		 * @auth hw 20210302
		 * @param "iwholeline_148"
		 * */
	    UFBoolean paraBoolean = UFBoolean.FALSE;
		try {
			paraBoolean = SysInitParamUtil.getParaBoolean(vos[0].getPk_org(), "IC130");
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
		throw new Exception("参数查询异常，请检查数据库链接！");
		}
		if (paraBoolean != UFBoolean.FALSE) {

			if(vos[0].getAttributeValue("iwholeline_148")!=UFBoolean.TRUE){
				throw new Exception("请勾选整行报检！");
			}
		}

	   //如果是外系LIMS统质检
	    String pk_org = vos[0].getPk_org();
		IProcessService iuap = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
		 if(iuap.isOutSystem( pk_org)){
			 if("0001V110000000012E56".equalsIgnoreCase(pk_org)){//如果是泰华LIMS
				 pushToLims28(vos);
				 
			 }else{
				 pushToLims(vos);
				 getDataManager().refreshFreezeOnhand();
				 ShowStatusBarMsgUtil.showStatusBarMsg("LIMS报检成功",getModel().getContext()); 
			 }
			
			 
		 }else{
			 if ((!(SysInitGroupQuery.isQCEnabled())) || (!(getIni01(pk_org)))) {
			    	ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008005_0", "04008005-0013"));
			    }
		        try
		        {
		    	   IFreezeCheck freezeCheck = (IFreezeCheck)NCLocator.getInstance().lookup(IFreezeCheck.class);
		    	   freezeCheck.check(vos);
		        } catch (BusinessException ex) {
		    	   ExceptionUtils.wrappException(ex);
			    }
			    getDataManager().refreshFreezeOnhand();
		 }
	 }
   
     private void pushToLims28(FreezeThawVO[] vos) throws BusinessException {
		// TODO Auto-generated method stub
  	    String pk_org = vos[0].getPk_org();
    	  try
	        {
	    	   IFreezeCheck freezeCheck = (IFreezeCheck)NCLocator.getInstance().lookup(IFreezeCheck.class);
	    	   freezeCheck.check(vos);
	        } catch (BusinessException ex) {
	    	   ExceptionUtils.wrappException(ex);
		    }
    	   FreezeThawVO[] reQueryOnhandVOs = vos;
    	   /**
       	 * 该查询会校验冻结数量 报检数量
       	 * 如果是泰华28检验 则不该如下处理2022年11月22日
       	 */
//    	   reQueryOnhandVOs=reQueryOnhandVOs(vos);
    	   if (ValueCheckUtil.isNullORZeroLength(reQueryOnhandVOs)) {
   			throw new BusinessException("库存状态发生变化,请刷新页面重新操作.");
   		}
   		//查询库存维度信息，获取到存货信息 
   		Set<String> pk_onhanddims = new HashSet<String>();
   		for (FreezeThawVO bvo : vos) {
   			if (!StringUtil.isEmpty(bvo.getPk_onhanddim())) {
   				pk_onhanddims.add(bvo.getPk_onhanddim());
   			}
   		}
   		Map<String, OnhandDimVO> ohmap = getOnhandDimVOMap(pk_onhanddims);
   		for (int i=0;i<vos.length;i++) {
   			int row =i+1;
   			FreezeThawVO itemVO = vos[i];
   			OnhandDimVO onhandvo = ohmap.get(itemVO.getPk_onhanddim());
   			if (chekQC(pk_org, onhandvo.getCmaterialoid())) {
   				throw new BusinessException("第"+row+"行是免检物料,不需要报检");
   			}
   			if(itemVO.getCcorrespondcode() != null && !"~".equals(itemVO.getCcorrespondcode())){
   				throw new BusinessException("第"+row+"行已报检,不能重复报检");
   			}
   		}
   	
   		String fun_type="TH_LIMS_IC_CHECK";
   		ISysDispatcherThLims outerService=(ISysDispatcherThLims) NCLocator.getInstance().lookup(ISysDispatcherThLims.class.getName());
   		/*
    		 * 泰华Lims只支持单条报检
    		 * 
    		if(fun_type.startsWith("TH_LIMS") && vos.length!=1){
    			ShowStatusBarMsgUtil.showErrorMsg("提醒", "泰华Lims 需逐条报检,不能多选!", this.getModel().getContext());
    			return;
    		}*/
   		Map<String, Object> param = new HashMap<String,Object>();
   		outerService.dispatch(vos,fun_type,  param);
   	    ShowStatusBarMsgUtil.showStatusBarMsg("报检完成!", this.getModel().getContext());

	}

	private void pushToLims(FreezeThawVO[] vos) throws BusinessException {
		// TODO Auto-generated method stub
 	    String pk_org = vos[0].getPk_org();
    	FreezeThawVO[] reQueryOnhandVOs = reQueryOnhandVOs(vos);
		if (ValueCheckUtil.isNullORZeroLength(reQueryOnhandVOs)) {
			throw new BusinessException("库存状态发生变化,请刷新页面重新操作.");

		}
		//查询库存维度信息，获取到存货信息 
		Set<String> pk_onhanddims = new HashSet<String>();
		for (FreezeThawVO bvo : vos) {
			if (!StringUtil.isEmpty(bvo.getPk_onhanddim())) {
				pk_onhanddims.add(bvo.getPk_onhanddim());
			}
		}
		Map<String, OnhandDimVO> ohmap = getOnhandDimVOMap(pk_onhanddims);
		for (int i=0;i<vos.length;i++) {
			int row =i+1;
			FreezeThawVO itemVO = vos[i];
			OnhandDimVO onhandvo = ohmap.get(itemVO.getPk_onhanddim());
			if (chekQC(pk_org, onhandvo.getCmaterialoid())) {
				throw new BusinessException("第"+row+"行是免检物料,不需要报检");

			}
			if(itemVO.getCcorrespondcode() != null && !"~".equals(itemVO.getCcorrespondcode())){
				throw new BusinessException("第"+row+"行已报检,不能重复报检");
			}
		}
		
		
		ISysDispatcher outerService = (ISysDispatcher) NCLocator.getInstance().lookup(ISysDispatcher.class.getName());
		Map<String, Object> param = new HashMap<String,Object>();
		outerService.dispatch(vos,"LIMS_IC_CHECK",  param);
	}


	public FreezeThawDataManager getDataManager()
     {
     return this.dataManager;
     }
   
     public BillListView getList() {
    	 return this.list;
     }
   
     public BillManageModel getModel() {
    	 return this.model;
     }
   
     public void setDataManager(FreezeThawDataManager dataManager) {
/*  96 */     this.dataManager = dataManager;
     }
   
     public void setList(BillListView list) {
/* 100 */     this.list = list;
     }
   
     public void setModel(BillManageModel model) {
/* 104 */     this.model = model;
/* 105 */     model.addAppEventListener(this);
     }
   
     private boolean getIni01(String pk_org) {
/* 109 */     String pk_group = AppUiContext.getInstance().getPkGroup();
/* 110 */     ICSysParam param = new ICSysParam(pk_group);
/* 111 */     return ValueUtils.getBoolean(param.getINI01(pk_org));
     }
   
   

//检查物料是否免检
	public boolean chekQC(String pk_org, String material)
			throws BusinessException {
		String sql = " select chkfreeflag    from bd_materialstock where pk_material='"
				+ material + "' and   pk_org ='" + pk_org + "' and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs
				.executeQuery(sql, new MapProcessor());

		if (hashMap2 != null && hashMap2.size() > 0) {
			UFBoolean b = UFBoolean.valueOf(hashMap2.get("chkfreeflag")
					.toString());
			return b.booleanValue();
		}
		return false;

	}
	
public Map<String, OnhandDimVO> getOnhandDimVOMap(
		Set<String> pk_marterial_vs) throws BusinessException {

	Map<String, OnhandDimVO> mmap = new HashMap<String, OnhandDimVO>();
	if (!CollectionUtils.isEmpty(pk_marterial_vs)) {
		String sql = " select *  from  ic_onhanddim l ";
		sql = sql + " where nvl(l.dr,0)= 0  ";
		String newWherePart = SQLUtil
				.buildSqlForIn("pk_onhanddim", pk_marterial_vs
						.toArray(new String[pk_marterial_vs.size()]));
		sql = sql + " and " + newWherePart;
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		List<OnhandDimVO> list = (List<OnhandDimVO>) bs.executeQuery(sql,
				new BeanListProcessor(OnhandDimVO.class));
		if (!CollectionUtils.isEmpty(list)) {

			for (OnhandDimVO vo1 : list) {
				mmap.put(vo1.getPk_onhanddim(), vo1);
			}
		}
	}
	return mmap;
}
private FreezeThawVO[] reQueryOnhandVOs(FreezeThawVO[] vos)
		throws BusinessException {
	String[] pk_onhanddims =
	VOEntityUtil.getVOsNotRepeatValue(vos, "pk_onhanddim");
	IOnhandQry onhandQry = (IOnhandQry) NCLocator.getInstance()
			.lookup(IOnhandQry.class);
	OnhandQryCond cond = new OnhandQryCond();
	cond.addAllSelectFields();
	cond.addSelectFields(new String[] { "pk_onhanddim" });
	SqlBuilder builder = new SqlBuilder();
	builder.append("hand.pk_onhanddim", pk_onhanddims);
	cond.setWhere(builder.toString());
	OnhandVO[] onhandVOs = onhandQry.queryOnhand(cond);
	if (ValueCheckUtil.isNullORZeroLength(onhandVOs)) {
		return null;
	}

	Map onhandDimAndFreezeVOMap =
	new HashMap();
	for (int i = 0; i < pk_onhanddims.length; ++i) {
		onhandDimAndFreezeVOMap.put(pk_onhanddims[i], vos[i]);
	}

	FreezeThawVO[] freezeVO = new FreezeThawVO[onhandVOs.length];
	UFDouble num = null;
	for (int i = 0; i < onhandVOs.length; ++i) {
		freezeVO[i] = new FreezeThawVO();

		freezeVO[i].setPk_group(onhandVOs[i].getPk_group());
		freezeVO[i].setPk_org(onhandVOs[i].getPk_org());

		freezeVO[i]
				.setPk_onhanddim(onhandVOs[i].getPk_onhanddim());

		num =
		NCBaseTypeUtils.sub(onhandVOs[i].getNonhandnum(),
				new UFDouble[] { onhandVOs[i]
				.getNlocknum() });
		freezeVO[i].setNnum(num);
		freezeVO[i].setNfrznum(num);
		if ((num == null) || (num.doubleValue() == 0.0D)) {
			ExceptionUtils.wrappBusinessException("报检主数量不可为空。");
		}
		num =
		NCBaseTypeUtils.sub(onhandVOs[i].getNonhandastnum(),
				new UFDouble[] { onhandVOs[i]
				/* 166 */.getNlockastnum() });
		freezeVO[i].setNassistnum(num);
		freezeVO[i].setNfrzastnum(num);
		num =
		NCBaseTypeUtils.sub(onhandVOs[i].getNgrossnum(),
				new UFDouble[] { onhandVOs[i]
				.getNlockgrossnum() });
		freezeVO[i].setNgrossnum(num);
	    freezeVO[i].setNfrzgrsnum(num);

		freezeVO[i].setNnum(vos[i].getNnum());
		freezeVO[i].setNfrznum(vos[i].getNfrznum());
		freezeVO[i].setNassistnum(vos[i].getNassistnum());
		freezeVO[i].setNfrzastnum(vos[i].getNfrzastnum());
		freezeVO[i].setNgrossnum(vos[i].getNgrossnum());
		freezeVO[i].setNfrzgrsnum(vos[i].getNfrzgrsnum());

		if ((((vos[i].getNfrznum() == null) || (vos[i]
				.getNfrznum().doubleValue() == 0.0D)))
				&& (((vos[i].getNfrzastnum() == null) || (vos[i]
						.getNfrzastnum().doubleValue() == 0.0D)))) {
			freezeVO[i].setNfrznum(vos[i].getNnum());
			freezeVO[i].setNfrzastnum(vos[i].getNassistnum());
		}

		/* 191 */FreezeThawVO clientVO =
		/* 192 */(FreezeThawVO) onhandDimAndFreezeVOMap.get(onhandVOs[i]
				.getPk_onhanddim());
		/* 193 */freezeVO[i].setVchangerate(clientVO.getVchangerate());
		/* 194 */freezeVO[i].setCunitid(clientVO.getCunitid());
		/* 195 */freezeVO[i].setCastunitid(clientVO.getCastunitid());
	}
	/* 197 */return freezeVO;
}
   
   
   
   
   
   
   
   
     private void stopEdit()
     {
     String[] stopItems = { "nfrznum", "nfrzastnum", "nfrzgrsnum" };
	     BillListPanel panel = getList().getBillListPanel();
	     BillItem bi = null;
	     for (String str : stopItems) {
		     bi = panel.getHeadItem(str);
		     if (null != bi)
		       bi.getItemEditor().stopEditing();
	     }
     }

     protected boolean isActionEnable()
     {
    	 Object[] objs = this.model.getSelectedOperaDatas();
    	 if (ValueCheckUtil.isNullORZeroLength(objs)) {
    		 return false;
       }
    	 return getDataManager().isFreezeAble();
     }
   
     protected void onBeforeICCheck()
       throws BusinessException
     {
    	 String VBATCHCODE = "pk_onhanddim.vbatchcode";
    	 Integer[] rows = getModel().getSelectedOperaRows();
    	 if (ValueCheckUtil.isNullORZeroLength(rows)) {
    		 return;
    	 }
	     String vbatchcode = null;
	     for (int i = 0; i < rows.length; ++i) {
	    	 vbatchcode = ValueUtils.getString(this.list.getBillListPanel().getHeadBillModel().getValueAt(rows[i].intValue(), "pk_onhanddim.vbatchcode"));
	   
	    	 if (NCBaseTypeUtils.isNull(vbatchcode))
	    		 throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008005_0", "04008005-0000"));
	      }
	  }
   }
