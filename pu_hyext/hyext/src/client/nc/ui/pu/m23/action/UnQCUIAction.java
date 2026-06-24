package nc.ui.pu.m23.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.qc.c001.page.IApplyMaintainApp;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.pu.m23.pubquery.IArrivePubQuery;
import nc.ui.pu.m23.view.ArriveCardForm;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.editor.BillListView;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillCombinServer;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillToServer;
import nc.vo.qc.c001.entity.ApplyVO;
import nc.vo.qc.pub.util.QCSysParamUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>到货单 检验 按钮处理类Action
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author hanbin
 * @time 2010-1-12 下午02:15:12
 */
public class UnQCUIAction extends NCAction {

  private static final long serialVersionUID = 7105286499110737794L;

  private ArriveCardForm form;

  private BillListView list;

  private BillManageModel model;

  public UnQCUIAction() {
	  
    super.setBtnName("撤销报检");
     super.setCode("unQCUIAction");
  }

  @Override
  public void doAction(ActionEvent e) throws Exception {
    ArriveVO vo = (ArriveVO) this.model.getSelectedData();
    String pk_org = vo.getHVO().getPk_org();
    ArriveItemVO[] bvos = this.getBVOs(vo);
    if (bvos.length == 0 || bvos[0] == null) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4004040_0", "04004040-0005")/*
                                                                   * @res
                                                                   * "请选中表体行"
                                                                   */);
    } 
	//2021-10-25判断是生成NC的逻辑还是同步LIMS
    IProcessService iuap = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
     boolean outSystem = iuap.isOutSystem(pk_org);
     if(outSystem){
    	 //如果是药物科技则走药物科技的报检
    	 if("0001V110000000012E56".equalsIgnoreCase(pk_org)){
	    	 pushToLims28(vo);
		}else{
	    	 pushToLims(vo);

		}
    	 return ;
     }
    // 先判断质检模块是否启用,再判断对应库存组织是否质检启用
    if (!SysInitGroupQuery.isQCEnabled()
        || UFBoolean.FALSE.equals(ValueUtils.getUFBoolean(QCSysParamUtil
            .getINI01(pk_org)))) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4004040_0", "04004040-0030")/*
                                                                   * @res
                                                                   * "质检模块未启用,无法报检!"
                                                                   */);
    }
    
    for (ArriveItemVO itemVO : bvos) {
      UFDouble naccumchecknum = itemVO.getNaccumchecknum();
      if (naccumchecknum != null) {
        if (naccumchecknum.compareTo(itemVO.getNnum()) == 0) {
          UFDouble naccumstorenum = itemVO.getNaccumstorenum();
          if (naccumstorenum != null
              && naccumstorenum.compareTo(UFDouble.ZERO_DBL) > 0) {
        	  ExceptionUtils.wrappBusinessException(
            		"已经完全入库，不能撤销");
            return;
          }
        }
      }
    }
    // 得到质检模块的提示信息
    canclebj(vo);
    List<String>list=new ArrayList<String>();
    list.add(vo.getHVO().getPk_arriveorder());
    IArrivePubQuery arrivePubQuery =
            NCLocator.getInstance().lookup(IArrivePubQuery.class);
        ArriveVO[] arriveVos =
            arrivePubQuery.queryAggVOByHids(list
                .toArray(new String[list.size()]));
    this.model.directlyUpdate(arriveVos);
  }

 /**
  * 药物科技取消LIMS报检 
  * @param vo
 * @throws BusinessException 
  */
private void pushToLims28(ArriveVO vo) throws BusinessException{
	ArriveItemVO[] bvos = getBVOs(vo);
	ArriveVO newvo = (ArriveVO) vo.clone();
	newvo.setBVO(bvos);
	ArriveVO[] newvos = {newvo};
	
	ArriveVO[] orgivos = {vo};
	
	String fun_type="TH_LIMS_PU_CHECK_CANCEL";	
	ClientBillToServer tool = new ClientBillToServer();
	ArriveVO[] lightVOs = (ArriveVO[]) tool.construct(newvos,newvos);
	ISysDispatcherThLims outerService = (ISysDispatcherThLims) NCLocator.getInstance().lookup(ISysDispatcherThLims.class.getName());
	 Map<String, Object> param = new HashMap<String,Object>();
	 param.put("opetype", "手动报检");
	//1. 校验多条
	if(lightVOs.length!=1){
		ShowStatusBarMsgUtil.showErrorMsg("提醒", "泰华Lims 需逐条撤销报检!", this.getModel().getContext());
		return;
	}	
	//判断是否已经报检
	boolean ischeck=false;
	for (ArriveItemVO itemVO : bvos) {
		if(StringUtils.isNotEmpty(itemVO.getVbdef11()) || StringUtils.isNotEmpty(itemVO.getVbdef12())){
			ischeck=true;
			break;
		}
	}
	if(ischeck){
		int showYesNoDlg = MessageDialog.showYesNoDlg(this.getForm(), "提示", "单据已执行报检动作，请通知质量人员做LIMS对应数据处理!");
		if(showYesNoDlg!=4)
			return;
	}
	
	ArriveVO returnvo = (ArriveVO) outerService.dispatch(lightVOs[0],fun_type,param);
	new ClientBillCombinServer().combine(orgivos, new ArriveVO[]{returnvo});
	this.model.directlyUpdate(orgivos); 
	ShowStatusBarMsgUtil.showStatusBarMsg("LIMS撤销报检成功",getModel().getContext());
	return ;

	
}  
  
private void pushToLims(ArriveVO vo) throws BusinessException {

	ArriveItemVO[] bvos = getBVOs(vo);
	ArriveVO newvo = (ArriveVO) vo.clone();
	newvo.setBVO(bvos);
	ArriveVO[] newvos = {newvo};
	ArriveVO[] orgivos = {vo};
	String fun_type="LIMS_PU_CHECK_CANCEL";	
	ClientBillToServer tool = new ClientBillToServer();
	ArriveVO[] lightVOs = (ArriveVO[]) tool.construct(newvos,newvos);
	ISysDispatcher outerService = (ISysDispatcher) NCLocator.getInstance().lookup(ISysDispatcher.class.getName());
	 Map<String, Object> param = new HashMap<String,Object>();
	 param.put("opetype", "手动报检");
	ArriveVO returnvo = (ArriveVO) outerService.dispatch(lightVOs[0],fun_type,param);
	new ClientBillCombinServer().combine(orgivos, new ArriveVO[]{returnvo});
	this.model.directlyUpdate(orgivos); 
	ShowStatusBarMsgUtil.showStatusBarMsg("LIMS撤销报检成功",getModel().getContext());
	return ;
}

private IProcessService iuap;
private IProcessService getProcessService(){
	if(null==iuap){
		iuap = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
	}
	return iuap;
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
  
  public  void canclebj(ArriveVO vos) throws BusinessException{
		 
	  String sql=
    		  "select b.csourceid,b.pk_applybill" + "\n" +
    				  "        from qc_applybill_s b" + "\n" + 
    				  "       where b.pk_applybill in (select a.pk_applybill" + "\n" + 
    				  "                                  from qc_applybill_s a" + "\n" + 
    				  "                                 where a.csourceid = '"+vos.getHVO().getPk_arriveorder()+"'" + "\n" + 
    				  "                                   and a.dr = 0)" + "\n" + 
    				  "         and b.dr = 0" + "\n" + 
    				  "       group by b.csourceid,b.pk_applybill";
  	IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
  	 List<Map<String,String>> list = (List) qryBS.executeQuery(sql, null,
				new MapListProcessor());
  	 if(list==null||list.size()==0){
  		 return;
  	 }else{
  		 if(list.size()>1){
  			throw new BusinessException("报检单含有其他到货单，不能反审,请手工删除");
  		 }	
  		 String pk_applybill=list.get(0).get("pk_applybill").toString();
  		 
  		IApplyMaintainApp qryBS2 = (IApplyMaintainApp) NCLocator.getInstance().lookup(
  				IApplyMaintainApp.class.getName());
  		 

  		 
  		 
  		ApplyVO[] aggvo2=qryBS2.queryMC001App(new String[] {pk_applybill});
  		if( aggvo2[0].getHVO().getModifiedtime()!=null&&aggvo2[0].getHVO().getModifiedtime().compareTo(aggvo2[0].getHVO().getCreationtime())>0){
				throw new BusinessException("报检单已修改，不允许删除");
			}
  		IPFBusiAction service = NCLocator.getInstance().lookup(IPFBusiAction.class);
  		ApplyVO[] aggvo22 = (ApplyVO[]) service.processBatch("DISCARD", "C001",aggvo2, null, null, null);	
  		
  	
  	 }
     
  
  
}

  public ArriveCardForm getForm() {
    return this.form;
  }

  public BillManageModel getModel() {
    return this.model;
  }

  public void setForm(ArriveCardForm form) {
    this.form = form;
  }

  /**
   * @param list
   *          要设置的 list
   */
  public void setList(BillListView list) {
    this.list = list;
  }

  public void setModel(BillManageModel model) {
    this.model = model;
    model.addAppEventListener(this);
  }

  /**
   * 得到选中的bvo
   * 
   * @param vo
   */
  private ArriveItemVO[] getBVOs(ArriveVO vo) {
    int[] rows = null;
    // 卡片界面
    if (((ShowUpableBillForm) this.form).isComponentVisible()) {
      BillCardPanel panel = this.form.getBillCardPanel();
      rows = panel.getBodyPanel().getTable().getSelectedRows();
    }
    else {
      // 列表界面
      BillListPanel panel = this.list.getBillListPanel();
      rows = panel.getBodyTable().getSelectedRows();
    }

    ArriveItemVO[] bvotmps = (ArriveItemVO[]) vo.getChildrenVO();
    ArriveItemVO[] bvos = new ArriveItemVO[rows.length];
    // Map<String, MaterialStockVO> bidMrlMap =
    // ArrivePublicUtil.queryMaterialStockInfo(bvotmps);
    for (int i = 0; i < rows.length; i++) {
      // UFDouble naccumstorenum = null;
      // UFDouble naccumletgoinnum = null;
      // UFDouble nelignum = null;
      // UFDouble nnotelignum = null;
      String pk_arriveorder_b = null;
      // String crowno = null;
      // 卡片界面
      if (((ShowUpableBillForm) this.form).isComponentVisible()) {
        pk_arriveorder_b =
            (String) this.form.getBillCardPanel().getBodyValueAt(rows[i],
                ArriveItemVO.PK_ARRIVEORDER_B);
        // naccumstorenum =
        // (UFDouble) this.form.getBillCardPanel().getBodyValueAt(rows[i],
        // ArriveItemVO.NACCUMSTORENUM);
        // naccumletgoinnum =
        // (UFDouble) this.form.getBillCardPanel().getBodyValueAt(rows[i],
        // ArriveItemVO.NACCUMLETGOINNUM);
        // crowno =
        // (String) this.form.getBillCardPanel().getBodyValueAt(rows[i],
        // ArriveItemVO.CROWNO);
        // nelignum =
        // (UFDouble) this.form.getBillCardPanel().getBodyValueAt(rows[i],
        // ArriveItemVO.NELIGNUM);
        // nnotelignum =
        // (UFDouble) this.form.getBillCardPanel().getBodyValueAt(rows[i],
        // ArriveItemVO.NNOTELIGNUM);

      }
      else {// 列表界面
        pk_arriveorder_b =
            (String) this.list.getBillListPanel().getBodyBillModel()
                .getValueAt(rows[i], ArriveItemVO.PK_ARRIVEORDER_B);
        // naccumstorenum =
        // (UFDouble) this.list.getBillListPanel().getBodyBillModel()
        // .getValueAt(rows[i], ArriveItemVO.NACCUMSTORENUM);
        // naccumletgoinnum =
        // (UFDouble) this.list.getBillListPanel().getBodyBillModel()
        // .getValueAt(rows[i], ArriveItemVO.NACCUMLETGOINNUM);
        // crowno =
        // (String) this.list.getBillListPanel().getBodyBillModel()
        // .getValueAt(rows[i], ArriveItemVO.CROWNO);
        // nelignum =
        // (UFDouble) this.list.getBillListPanel().getBodyBillModel()
        // .getValueAt(rows[i], ArriveItemVO.NELIGNUM);
        // nnotelignum =
        // (UFDouble) this.list.getBillListPanel().getBodyBillModel()
        // .getValueAt(rows[i], ArriveItemVO.NNOTELIGNUM);
      }
      // MaterialStockVO materialvo = bidMrlMap.get(pk_arriveorder_b);
      // boolean bstockbycheck =
      // ValueUtils.getBoolean(materialvo.getStockbycheck());
      // boolean chkfreeflag =
      // ValueUtils.getBoolean(materialvo.getChkfreeflag());
      // 按质检结果入库,非免检物料入库需要校验检验
      // if (bstockbycheck && !chkfreeflag) {
      // // 合格+不合格>0 质检报告已生成,累计入库-累计紧急放行入库>0,说明质检报告已入库,需限制检验
      // // 如果是紧急放行入库,则不用限制
      // if (MathTool.compareTo(MathTool.add(nelignum, nnotelignum),
      // UFDouble.ZERO_DBL) > 0
      // && MathTool.compareTo(
      // MathTool.sub(naccumstorenum, naccumletgoinnum),
      // UFDouble.ZERO_DBL) > 0) {
      // ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
      // .getNCLangRes().getStrByID("4004040_0", "04004040-0035", null,
      // new String[] {
      // crowno
      // })/* @res "第{0}行已根据质检报告结果入库，不允许检验！" */);
      // }
      // }
      for (int j = 0; j < bvotmps.length; j++) {
        if (bvotmps[j].getPk_arriveorder_b().equals(pk_arriveorder_b)) {
          bvos[i] = bvotmps[j];
          break;
        }
      }
    }
    return bvos;
  }

  private boolean isOneVOEnable(ArriveVO vo) {
    if (!POEnumBillStatus.APPROVE.value().equals(vo.getHVO().getFbillstatus())) {
      return false;
    }
    int[] selectedRows = null;
    // 卡片界面
    if (((ShowUpableBillForm) this.form).isComponentVisible()) {
      selectedRows =
          this.form.getBillCardPanel().getBodyPanel().getTable()
              .getSelectedRows();
    }
    else {
      // 列表界面
      selectedRows =
          this.list.getBillListPanel().getBodyTable().getSelectedRows();
    }
    if (selectedRows == null || selectedRows.length == 0) {
      return false;
    }
    ArriveItemVO[] orgItems = vo.getBVO();
    if (orgItems == null) {
      return false;
    }
    return true;
  }

  @Override
  protected boolean isActionEnable() {
    if (this.getModel().getAppUiState() == AppUiState.EDIT
        || this.getModel().getSelectedData() == null) {
      return false;
    }

    Object[] objs = this.getModel().getSelectedOperaDatas();

    if (this.model.getSelectedData() != null && ArrayUtils.isEmpty(objs)) {
      return this.isOneVOEnable((ArriveVO) this.model.getSelectedData());
    }
    if (objs.length > 1) {
      return false;
    }
    //表体报检
    return this.isOneVOEnable((ArriveVO) objs[0]);
  }
}
