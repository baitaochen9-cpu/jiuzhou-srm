package nc.ui.fa.simulatedep.action;

import java.awt.event.ActionEvent;

import nc.pub.fa.common.consts.FunCodeConst;
import nc.ui.am.action.info.ActionInfoInitalizer;
import nc.ui.am.action.info.IAMActionCode;
import nc.ui.am.action.support.AMNCAction;
import nc.ui.am.base.manager.BaseQueryDialogManager;
import nc.ui.am.base.manager.ModelDelegator;
import nc.ui.fa.simulatedep.model.SimDepAppModel;
import nc.ui.fa.simulatedep.model.SimDepModelServer;
import nc.ui.fa.simulatedep.view.NormalConditionPanel;
import nc.ui.fa.simulatedep.view.SimDepBillForm;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.vo.am.common.BizContext;
import nc.vo.fa.simulatedep.AggSimulateDepVO;
import nc.vo.fa.simulatedep.SimDepConditionVO;
import nc.vo.fa.simulatedep.SimulateDepHVO;
import nc.vo.fa.simulatedep.SimulateDepUIStateConst;
import nc.vo.uif2.LoginContext;

/**
 * 模拟折旧
 * @author lilc
 *
 */
public class SimulateDepAction extends AMNCAction {


	private SimDepBillForm billForm;

	private BaseQueryDialogManager queryDialogManager = null;

	private ModelDelegator modelDelegator = null;

	public SimulateDepAction() {
		ActionInfoInitalizer.initializeAction(this, IAMActionCode.simulantDep);
	}

	public void doAction(ActionEvent e) throws Exception {
		getSimDepAppModel().setUIState(SimulateDepUIStateConst.TOTALSTATUS);
		// 尝试加载查询模板数据。
		getQueryDialogManager().loadAndInitTempletWithNormalPanel("@@@@", FunCodeConst.SIM_DEP, getModel().getContext().getPk_loginUser(), null, "simdep",
				new NormalConditionPanel(billForm.getModel().getContext()), null);
		if (QueryConditionDLG.ID_OK != getQueryDialogManager().getQueryDialog().showModal()) {
			return;
		}
		SimDepConditionVO simDepConditionVO = (SimDepConditionVO) getQueryDialogManager().getQueryDialog().getNormalPanel().getNormalQueryObject();

		LoginContext logContext = getBillForm().getModel().getContext();
		simDepConditionVO.setPk_group(logContext.getPk_group());
//		String billcode = AMProxy.lookup(IBillCodeManageService.class).getPreBillCode(BillTypeConst.SIMULATEDEP, logContext.getPk_group(), simDepConditionVO.getPk_org());
//		simDepConditionVO.setBill_code(billcode);
		simDepConditionVO.setBill_date(BizContext.getInstance().getBizDate());
		simDepConditionVO.setOperator(getModel().getContext().getPk_loginUser());
		simDepConditionVO.setFilterCardSQL(getQueryDialogManager().getQueryDialog().getWhereSQL());
		AggSimulateDepVO simdepVO = getService().simulateDep(simDepConditionVO);
		getBillForm().showMeUp();
		SimulateDepHVO hvo = (SimulateDepHVO) simdepVO.getParentVO();
		// 处理账簿列是否现实
		getBillForm().processAccbookColumnsByAccbookInfo(hvo);
		// 处理账簿列的名称
		getBillForm().processAccbookColumnsName(hvo);
		// 设置精度
		String pk_accbook = hvo.getPk_accbook1();
		getBillForm().setPrecision(pk_accbook);

		getBillForm().getReportClass().setHeadDataVO(hvo);
		getBillForm().setTotalBodyVOs(simdepVO.getChildrenVO());
		getBillForm().showTotal();
		getBillForm().getReportClass().setBodyDataVO(simdepVO.getChildrenVO());
		getModel().directlyAdd(simdepVO);
		getModel().toDataStatus();
		getBillForm().showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("depreciation_0","02012050-0070")/*@res "模拟折旧完成！"*/);
	}
	
	private SimDepAppModel getSimDepAppModel() {
		return (SimDepAppModel) getBillForm().getModel();
	}

	public SimDepBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(SimDepBillForm billForm) {
		this.billForm = billForm;
	}

	public BaseQueryDialogManager getQueryDialogManager() {
		return queryDialogManager;
	}

	public void setQueryDialogManager(BaseQueryDialogManager queryDialogManager) {
		this.queryDialogManager = queryDialogManager;
	}

	public ModelDelegator getModelDelegator() {
		return modelDelegator;
	}

	public void setModelDelegator(ModelDelegator modelDelegator) {
		this.modelDelegator = modelDelegator;
	}

	private SimDepModelServer getService() {
		return (SimDepModelServer) getModel().getService();
	}
}