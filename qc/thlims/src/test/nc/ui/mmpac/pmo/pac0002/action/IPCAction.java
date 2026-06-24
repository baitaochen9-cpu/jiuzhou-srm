package nc.ui.mmpac.pmo.pac0002.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.pubapp.pub.smart.IBillQueryService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.para.SysInitQuery;
import nc.ui.mmpac.pmo.pac0002.view.PMOBillForm;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOHeadVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillTempletHeadVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillCombinServer;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillToServer;
import org.apache.commons.lang.ArrayUtils;
/**
 * LIMS报检
 * @author yunfeng.li
 *
 */
public class IPCAction extends NCAction {
	private static final long serialVersionUID = 6L;
	private PMOBillForm form;
	private BillListView list;
	private BillManageModel model;

	public IPCAction() {
		//SCMActionInitializer.initializeAction(this, "Verify");
		super.setBtnName("IPC检验");
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
		}else if(bvos.length!=1){
			/*
	 		 * 泰华Lims只支持单条报检
	 		 * */
			ShowStatusBarMsgUtil.showErrorMsg("提醒", "泰华Lims 需逐条报检,不能多选!", this.getModel().getContext());
 			return;
		}

		//1. 校验物料免检 & 单据行状态 是投放
		List<PMOItemVO> reList = new ArrayList<PMOItemVO>();
		for (PMOItemVO itemVO : bvos) {
			/*if (chekQC(pk_org, itemVO.getCmaterialid())) {
				throw new BusinessException("免检物料,不需要报检！");
			}
			if(itemVO.getFitemstatus()!=1){
				throw new BusinessException("表体状态非 投放状态!");
			}*/
			/*if (itemVO.getVdef11() != null&&!"~".equals(itemVO.getVdef11())) {
				throw new BusinessException("已报检,不需要重复报检！");
			}*/
			reList.add(itemVO);
		}
		vo.setChildrenVO(reList.toArray(new PMOItemVO[reList.size()]));
		//2. 检查配置参数
		UFBoolean paraBoolean =SysInitQuery.getParaBoolean(pk_org, "YFQCIPC");
		if(paraBoolean.booleanValue()){
			//根据模板名称查询PK
			String pk_templetid=this.getPkTempletid(pk_org);
			if(org.apache.commons.lang3.StringUtils.isEmpty(pk_templetid)){
				throw new BusinessException("未查询到LIMS_IPC 模板信息");
			}
			IPC_Dialog ipc_Dialog=new IPC_Dialog(this.getForm(), "IPC检验", vo, pk_templetid);
			ipc_Dialog.showModal();
		}else{
			ShowStatusBarMsgUtil.showErrorMsg("提示", "组织参数中【YFQCIPC】未开启此功能！请检查系统设置！",this.getModel().getContext());
			return;
		}
		
		 /**
		   * 2021-10-25判断是生成NC的单子还是同步LIMS
	     boolean outSystem = this.getProcessService().isOutSystem(pk_org);
	     if(outSystem){
			pushToLims(vo);
		}
		  */
	}
	
	/*
	 * 查询模板信息
	 * */
	private String getPkTempletid(String pk_org) throws BusinessException{
		String q_sql="SELECT PK_BILLTEMPLET FROM pub_billtemplet WHERE PK_ORG='"+pk_org+"' AND BILL_TEMPLETNAME='LIMS_IPC'";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

		HashMap<String, Object> hashMap = (HashMap<String,Object>) bs.executeQuery(q_sql, new MapProcessor());
		if(null!=hashMap && hashMap.size()>0){
			return hashMap.get("pk_billtemplet").toString();
		}
		return "";
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
 		String fun_type="TH_LIMS_MM_CHECK";
 		ISysDispatcherThLims outerService=(ISysDispatcherThLims) NCLocator.getInstance().lookup(ISysDispatcherThLims.class.getName());
		Map<String, Object> param = new HashMap<String, Object>();
		PMOAggVO returnvo = (PMOAggVO) outerService.dispatch(lightVOs[0],fun_type, param);

		new ClientBillCombinServer().combine(orgivos, new PMOAggVO[]{returnvo});
 		this.model.directlyUpdate(orgivos); 
 		
		ShowStatusBarMsgUtil.showStatusBarMsg("LIMS报检成功",getModel().getContext());
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
		if (obj != null) {
			return isOneVOEnable((PMOAggVO) this.model.getSelectedData());
		}
		PMOAggVO agg = (PMOAggVO) obj;
		PMOHeadVO headVO = agg.getParentVO();
		//fbillstatus  订单状态  fbillstatus int  流程生产订单状态   0=自由，1=审批，2=提交，3=审批中，4=审批不通过， 
		if(headVO.getFbillstatus() != 1){
			return false;
		}
		//判断是否外系统质检
		try {
			if (!this.getProcessService().isOutSystem(agg.getParentVO().getPk_org())) {// 如果是外系统质检
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}