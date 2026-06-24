package nc.ui.pu.m23.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.pu.m23.qc.IArriveForQC;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.qc.c001.pu.ReturnObjectFor23;
import nc.ui.pu.m23.view.ArriveCardForm;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.editor.BillListView;
import nc.vo.ml.NCLangRes4VoTransl;
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
import nc.vo.qc.pub.enumeration.StrictLevelEnum;
import nc.vo.qc.pub.util.QCSysParamUtil;

import org.apache.commons.lang.ArrayUtils;
/**
 * 报检按钮
 * @author yunfeng.li
 *
 */
@SuppressWarnings("restriction")
public class QCUIAction extends NCAction
{
	private static final long serialVersionUID = 7105286499110737794L;
	private ArriveCardForm form;
	private BillListView list;
	private BillManageModel model;
	
	public QCUIAction()
	{
		SCMActionInitializer.initializeAction(this, "Verify");
	}
	
	private IProcessService iuap;
	private IProcessService getProcessService(){
		if(null==iuap){
			iuap = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
		}
		return iuap;
	}
	
	
	public void doAction(ActionEvent e) throws Exception
	{
		 ArriveVO vo = (ArriveVO) this.model.getSelectedData();
		 String pk_org = vo.getHVO().getPk_org();
		  /**
		   * 2021-10-25判断是生成NC的单子还是同步LIMS
		   */
	     boolean outSystem = this.getProcessService().isOutSystem(pk_org);
	     
	     if(outSystem){
	    	 //如果是药物科技则走药物科技的报检
	    	 if("0001V110000000012E56".equalsIgnoreCase(pk_org)){
		    	 pushToLims28(vo);
			}else{
		    	 pushToLims(vo);

			}
	    	 return ;
	     }else{
	    	ArriveItemVO[] bvos = getBVOs(vo);
	 		if ((bvos.length == 0) || (bvos[0] == null)) {
	 		ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl
	 					.getNCLangRes().getStrByID("4004040_0", "04004040-0005"));
	 			}
	 		 
	 		
	 		if ((!(SysInitGroupQuery.isQCEnabled()))
	 				|| (UFBoolean.FALSE.equals(ValueUtils
	 						.getUFBoolean(QCSysParamUtil.getINI01(pk_org)))))
	 		{
	 			ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl
	 					.getNCLangRes().getStrByID("4004040_0", "04004040-0030"));
	 			}
	 		
	 		for (ArriveItemVO itemVO : bvos) {
	 			UFDouble naccumchecknum = itemVO.getNaccumchecknum();
	 			if ((naccumchecknum == null) ||
	 			(naccumchecknum.compareTo(itemVO.getNnum()) != 0))
	 				continue;
	 			UFDouble naccumstorenum = itemVO.getNaccumstorenum();
	 			if ((naccumstorenum == null)
	 					|| (naccumstorenum.compareTo(UFDouble.ZERO_DBL) <= 0))
	 				continue;
	 			ShowStatusBarMsgUtil.showStatusBarMsg(
	 					NCLangRes4VoTransl.getNCLangRes().getStrByID("4004040_0",
	 							"04004040-0214", null,
	 							new String[]{itemVO.getCrowno()}),
	 					this.model.getContext());
	 			
	 			return;
	 			}
	 		
	 		ArriveVO newvo = (ArriveVO) vo.clone();
	 		newvo.setBVO(bvos);
	 		ArriveVO[] newvos = {newvo};
	 		
	 		ArriveVO[] orgivos = {vo};
	 		
	 		ClientBillToServer tool = new ClientBillToServer();
	 		ArriveVO[] lightVOs = (ArriveVO[]) tool.construct(newvos,newvos);
	 		
	 		Object[] objects = ((IArriveForQC) NCLocator.getInstance().lookup(IArriveForQC.class)).qualityCheck(lightVOs, false);
	 		
	 		ArriveVO[] returnVos = (ArriveVO[]) (ArriveVO[]) objects[0];
	 		ReturnObjectFor23 rof = (ReturnObjectFor23) objects[1];
	 		ShowStatusBarMsgUtil.showStatusBarMsg(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004040_0", "04004040-0034"),
	 				getModel().getContext());
	 		
	 		 if (rof != null) {
	 			Map strictMap = rof.getCsourcebid_strictlevel();
	 			for (ArriveVO hvo : newvos) {
	 				ArriveItemVO[] bvs = hvo.getBVO();
	 				for (ArriveItemVO bvo : bvs) {
	 				String bid = bvo.getPk_arriveorder_b();
	 					UFDouble naccumstorenum = bvo.getNaccumstorenum();
	 					if (naccumstorenum != null)
	 						;
	 					if ((naccumstorenum
	 							.compareTo((bvo.getNnum() == null)
	 									? UFDouble.ZERO_DBL
	 									: bvo.getNnum()) >= 0)
	 							&&
	 							(!(strictMap.containsKey(bid))))
	 						continue;
	 					if (StrictLevelEnum.FREE.value().equals(
	 							strictMap.get(bid))) {
	 						ShowStatusBarMsgUtil.showStatusBarMsg(
	 								NCLangRes4VoTransl.getNCLangRes().getStrByID(
	 										"4004040_0", "04004040-0032", null,
	 										new String[]{bvo.getCrowno()}),
	 								this.model.getContext());
	 						}
	 					else if (StrictLevelEnum.PAUSE.value().equals(strictMap.get(bid))) {
	 						ShowStatusBarMsgUtil.showStatusBarMsg(
	 								NCLangRes4VoTransl.getNCLangRes().getStrByID(
	 										"4004040_0", "04004040-0033", null,
	 										new String[]{bvo.getCrowno()}),
	 								this.model.getContext());
	 						}
	 					
	 					}
	 				}
	 			}
	 		new ClientBillCombinServer().combine(orgivos, returnVos);
	 		this.model.directlyUpdate(orgivos); 
	     }	
	}
	
	/**
	 * 药物科技28LIMS报检
	 * @param vo
	 * @throws BusinessException 
	 */
	private void  pushToLims28(ArriveVO vo) throws BusinessException{

    	ArriveItemVO[] bvos = getBVOs(vo);
		ArriveVO newvo = (ArriveVO) vo.clone();
 		newvo.setBVO(bvos);
 		ArriveVO[] newvos = {newvo};
 		ArriveVO[] orgivos = {vo};
 		ClientBillToServer tool = new ClientBillToServer();
 		ArriveVO[] lightVOs = (ArriveVO[]) tool.construct(newvos,newvos);
		/*
		 * edit by xuchong 2022年9月8日
		 * 根据组织判断Lims 区分调用
		 * */
 		String fun_type="TH_LIMS_PU_CHECK";
 		
 		/*
 		 * 泰华Lims只支持单条报检
 		 * 
 		if(fun_type.startsWith("TH_LIMS") && lightVOs[0].getBVO().length!=1){
 			ShowStatusBarMsgUtil.showErrorMsg("提醒", "泰华Lims 需逐条报检,不能多选!", this.getModel().getContext());
 			return;
 		}*/
 		ISysDispatcherThLims outerService=(ISysDispatcherThLims) NCLocator.getInstance().lookup(ISysDispatcherThLims.class.getName());
		Map<String, Object> param = new HashMap<String,Object>();
		param.put("opetype", "手动报检");
		ArriveVO returnvo = (ArriveVO) outerService.dispatch(lightVOs[0],fun_type,param);
		//更新界面数据
		new ClientBillCombinServer().combine(orgivos, new ArriveVO[]{returnvo});
 		this.model.directlyUpdate(orgivos); 
	 	
		ShowStatusBarMsgUtil.showStatusBarMsg("LIMS报检成功",getModel().getContext());
		 
		return ;
 
	
	}
	
	private void pushToLims(ArriveVO vo) throws BusinessException {
    	ArriveItemVO[] bvos = getBVOs(vo);
		ArriveVO newvo = (ArriveVO) vo.clone();
 		newvo.setBVO(bvos);
 		ArriveVO[] newvos = {newvo};
 		ArriveVO[] orgivos = {vo};
 		ClientBillToServer tool = new ClientBillToServer();
 		ArriveVO[] lightVOs = (ArriveVO[]) tool.construct(newvos,newvos);
		/*
		 * edit by xuchong 2022年9月8日
		 * 根据组织判断Lims 区分调用
		 * */
 		String fun_type="LIMS_PU_CHECK";
 		ISysDispatcher outerService=(ISysDispatcher) NCLocator.getInstance().lookup(ISysDispatcher.class.getName());
		Map<String, Object> param = new HashMap<String,Object>();
		param.put("opetype", "手动报检");
		ArriveVO returnvo = (ArriveVO) outerService.dispatch(lightVOs[0],fun_type,param);
		
		new ClientBillCombinServer().combine(orgivos, new ArriveVO[]{returnvo});
 		this.model.directlyUpdate(orgivos); 
	 	
		ShowStatusBarMsgUtil.showStatusBarMsg("LIMS报检成功",getModel().getContext());
		 
		return ;
 
	}

	// 检查物料是否免检
	public boolean chekQC(String pk_org, String material)
			throws BusinessException {
		String sql = " select chkfreeflag  from bd_materialstock where pk_material='"
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
	public ArriveCardForm getForm() {
		/* 166 */return this.form;
		}
	
	public BillManageModel getModel() {
		/* 170 */return this.model;
		}
	
	public void setForm(ArriveCardForm form) {
		/* 174 */this.form = form;
		}
	
	public void setList(BillListView list)
	{
		/* 182 */this.list = list;
		}
	
	public void setModel(BillManageModel model) {
		/* 186 */this.model = model;
		/* 187 */model.addAppEventListener(this);
		}
	
	private ArriveItemVO[] getBVOs(ArriveVO vo)
	{
		/* 196 */int[] rows = null;
		
		/* 198 */if (this.form.isComponentVisible()) {
			/* 199 */BillCardPanel panel = this.form.getBillCardPanel();
			/* 200 */rows = panel.getBodyPanel().getTable().getSelectedRows();
			}
		else
		{
			/* 204 */BillListPanel panel = this.list.getBillListPanel();
			/* 205 */rows = panel.getBodyTable().getSelectedRows();
			}
		
		/* 208 */ArriveItemVO[] bvotmps = (ArriveItemVO[]) (ArriveItemVO[]) vo
				.getChildrenVO();
		/* 209 */ArriveItemVO[] bvos = new ArriveItemVO[rows.length];
		
		/* 212 */for (int i = 0; i < rows.length; ++i)
		{
			/* 217 */String pk_arriveorder_b = null;
			
			/* 220 */if (this.form.isComponentVisible()) {
				/* 221 */pk_arriveorder_b = (String) this.form
						.getBillCardPanel().getBodyValueAt(rows[i],
								"pk_arriveorder_b");
				}
			else
			{
				pk_arriveorder_b = (String) this.list
						.getBillListPanel().getBodyBillModel()
						.getValueAt(rows[i], "pk_arriveorder_b");
				}
			
			for (int j = 0; j < bvotmps.length; ++j) {
				if (bvotmps[j].getPk_arriveorder_b().equals(
						pk_arriveorder_b)) {
					bvos[i] = bvotmps[j];
					break;
					}
				}
			}
		return bvos;
		}
	
	private boolean isOneVOEnable(ArriveVO vo) {
		if (!(POEnumBillStatus.APPROVE.value().equals(vo.getHVO()
				.getFbillstatus()))) {
			return false;
		}
		int[] selectedRows = null;
		
		if (this.form.isComponentVisible()) {
			selectedRows = this.form.getBillCardPanel().getBodyPanel().getTable().getSelectedRows();
		}
		else
		{
			selectedRows = this.list.getBillListPanel().getBodyTable().getSelectedRows();
		}
		
		if ((selectedRows == null) || (selectedRows.length == 0)) {
			return false;
		}
		ArriveItemVO[] orgItems = vo.getBVO();
		if(orgItems == null){
			return false;
		}
		
		return (orgItems != null);
	}
	
	protected boolean isActionEnable()
	{
		if ((getModel().getAppUiState() == AppUiState.EDIT)
				|| (getModel().getSelectedData() == null))
		{
			return false;
			}
		
		Object[] objs = getModel().getSelectedOperaDatas();
		
		if ((this.model.getSelectedData() != null)
				&& (ArrayUtils.isEmpty(objs))) {
		return isOneVOEnable((ArriveVO) this.model
					.getSelectedData());
			}
		if (objs.length > 1) {
			return false;
		}
	    //表体报检
	    return this.isOneVOEnable((ArriveVO) objs[0]);
	}
	

}