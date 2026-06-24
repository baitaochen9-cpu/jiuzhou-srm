package nc.ui.mmpac.pmo.pac0002.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.para.SysInitQuery;
import nc.ui.mmpac.pmo.pac0002.view.PMOBillForm;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOHeadVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillCombinServer;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillToServer;
/**
 * LIMS报检
 * @author yunfeng.li
 *
 */
public class QCUIAction extends NCAction {
	private static final long serialVersionUID = 6L;
	private PMOBillForm form;
	private BillListView list;
	private BillManageModel model;

	public QCUIAction() {
		SCMActionInitializer.initializeAction(this, "Verify");
	}
	
	private IProcessService iuap;
	private IProcessService getProcessService(){
		if(null==iuap){
			iuap = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
		}
		return iuap;
	}
	public void doAction(ActionEvent e) throws Exception {
		PMOAggVO vo = (PMOAggVO) this.model.getSelectedData();
		String pk_org = vo.getParentVO().getPk_org();

		PMOItemVO[] bvos = (PMOItemVO[]) vo.getChildrenVO();
		if ((bvos.length == 0) || (bvos[0] == null)) {
			ExceptionUtils.wrappBusinessException("请选择需要报检的订单");
		}
		 /**
		   * 2021-10-25判断是生成NC的单子还是同步LIMS
		   */
	     boolean outSystem = this.getProcessService().isOutSystem(pk_org);
	     if(outSystem){
			pushToLims(vo);
		}
	}
	public PMOAggVO pushToLims(PMOAggVO vo) throws BusinessException{
		String pk_org = vo.getParentVO().getPk_org();
		PMOItemVO[] bvos = (PMOItemVO[]) vo.getChildrenVO();
		// 如果是外系统质检
		List<PMOItemVO> reList = new ArrayList<PMOItemVO>();
		for (PMOItemVO itemVO : bvos) {
			if (chekQC(pk_org, itemVO.getCmaterialid())) {
				throw new BusinessException("免检物料,不需要报检！");
			}
			if (itemVO.getVdef11() != null&&!"~".equals(itemVO.getVdef11())) {
				throw new BusinessException("已报检,不需要重复报检！");
			}
			reList.add(itemVO);
		
		}
		
		PMOAggVO newvo = (PMOAggVO) vo.clone();
 		newvo.setChildrenVO(bvos);
 		PMOAggVO[] newvos = {newvo};
 		PMOAggVO[] orgivos = {vo};
 		ClientBillToServer tool = new ClientBillToServer();
 		PMOAggVO[] lightVOs = (PMOAggVO[]) tool.construct(newvos,newvos);
 		/*
 		 * edit by xuchong 2022年9月8日
 		 * 根据组织判断Lims 区分调用
 		 * */
 		
 		//2. 检查配置参数 2022年12月11日
		UFBoolean paraBoolean =SysInitQuery.getParaBoolean(pk_org, "YF_LIMS04");
		if(!paraBoolean.booleanValue()){
			ExceptionUtils.wrappBusinessException("组织参数中【YF_LIMS04】未开启此功能！请检查系统设置！");
		}
 		String fun_type="LIMS_MM_CHECK";
 		/*
 		 * 泰华Lims只支持单条报检
 		 * */
 		if("0001V110000000012E56".equalsIgnoreCase(pk_org)&& lightVOs[0].getChildrenVO().length!=1){
 			ExceptionUtils.wrappBusinessException("请通过IPC报检");
 		}else{
 	 		ISysDispatcher outerService=(ISysDispatcher) NCLocator.getInstance().lookup(ISysDispatcher.class.getName());
 			Map<String, Object> param = new HashMap<String, Object>();
 			PMOAggVO returnvo = (PMOAggVO) outerService.dispatch(lightVOs[0],fun_type, param);
 			new ClientBillCombinServer().combine(orgivos, new PMOAggVO[]{returnvo});
 	 		this.model.directlyUpdate(orgivos); 
 			ShowStatusBarMsgUtil.showStatusBarMsg("LIMS报检成功",getModel().getContext());
 		}
 		return vo;
	}

	// 检查物料是否免检
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

	public PMOBillForm getForm() {
		return this.form;
	}

	public BillManageModel getModel() {
		return this.model;
	}

	public void setForm(PMOBillForm form) {
		this.form = form;
	}

	public void setList(BillListView list) {
		this.list = list;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	private PMOItemVO[] getBVOs1(PMOAggVO vo) {
		int[] rows = null;

		if (this.form.isComponentVisible()) {
			BillCardPanel panel = this.form.getBillCardPanel();
			rows = panel.getBodyPanel().getTable().getSelectedRows();
		} else {
			BillListPanel panel = this.list.getBillListPanel();
			rows = panel.getBodyTable().getSelectedRows();
		}

		PMOItemVO[] bvotmps = (PMOItemVO[]) vo.getChildrenVO();
		PMOItemVO[] bvos = new PMOItemVO[rows.length];

		for (int i = 0; i < rows.length; ++i) {
			String pk_arriveorder_b = null;

			if (this.form.isComponentVisible()) {
				pk_arriveorder_b = (String) this.form.getBillCardPanel()
						.getBodyValueAt(rows[i], "cmoid");
			} else {
				pk_arriveorder_b = (String) this.list.getBillListPanel()
						.getBodyBillModel().getValueAt(rows[i], "cmoid");
			}

			for (int j = 0; j < bvotmps.length; ++j) {
				if (bvotmps[j].getCmoid().equals(pk_arriveorder_b)) {
					bvos[i] = bvotmps[j];
					break;
				}
			}
		}
		return bvos;
	}

	private boolean isOneVOEnable(PMOAggVO vo) {
		if (!(vo.getParentVO().getFbillstatus().intValue() == 1)) {
			return false;
		}
		int[] selectedRows = null;

		if (this.form.isComponentVisible()) {
			selectedRows = this.form.getBillCardPanel().getBodyPanel()
					.getTable().getSelectedRows();
		} else {
			selectedRows = this.list.getBillListPanel().getBodyTable()
					.getSelectedRows();
		}

		if ((selectedRows == null) || (selectedRows.length == 0)) {
			return false;
		}
		PMOItemVO[] orgItems = vo.getChildrenVO();

		return (orgItems != null);
	}

	protected boolean isActionEnable() {
		if ((getModel().getAppUiState() == AppUiState.EDIT)
				|| (getModel().getSelectedData() == null)) {
			return false;
		}

		Object obj = getModel().getSelectedData();
		if (obj == null) {
			return false;
		}
//		if ((this.model.getSelectedData() != null)
//				&& (ArrayUtils.isEmpty(objs))) {
//			return isOneVOEnable((PMOAggVO) this.model.getSelectedData());
//		}
		PMOAggVO agg = (PMOAggVO) obj;
		PMOHeadVO headVO = agg.getParentVO();
		//fbillstatus  订单状态  fbillstatus int  流程生产订单状态   0=自由，1=审批，2=提交，3=审批中，4=审批不通过， 
		if(headVO.getFbillstatus() != 1){
			return false;
		}
		try {
			if (!this.getProcessService().isOutSystem(agg.getParentVO().getPk_org())) {// 如果是外系统质检
				return false;
			}
			/*
		     * 如果组织为28 撤掉按钮灰色不可用 2023年3月16日
		     */
			 //如果是药物科技则走药物科技的报检
	    	if("0001V110000000012E56".equalsIgnoreCase(agg.getParentVO().getPk_org())){
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}