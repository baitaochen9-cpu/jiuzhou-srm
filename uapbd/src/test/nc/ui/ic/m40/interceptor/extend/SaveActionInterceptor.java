/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */package nc.ui.ic.m40.interceptor.extend;

/*     */
/*     */import java.awt.event.ActionEvent;
/*     */
import java.util.Collection;
/*     */
import java.util.HashMap;
/*     */
import java.util.List;
/*     */
import java.util.Map;
/*     */
import javax.swing.Action;
/*     */
import nc.bs.uif2.BusinessExceptionAdapter;
/*     */
import nc.bs.uif2.validation.ValidationException;
/*     */
import nc.bs.uif2.validation.ValidationFailure;
/*     */
import nc.md.persist.framework.IMDPersistenceQueryService;
/*     */
import nc.md.persist.framework.MDPersistenceService;
/*     */
import nc.ui.ic.general.view.ICBizView;
/*     */
import nc.ui.medpub.utils.MaterialPropertyUtil;
/*     */
import nc.ui.ml.NCLangRes;
/*     */
import nc.ui.pub.bill.BillCardPanel;
/*     */
import nc.ui.uif2.ShowStatusBarMsgUtil;
/*     */
import nc.ui.uif2.actions.ActionInterceptor;
/*     */
import nc.ui.uif2.model.AbstractAppModel;
/*     */
import nc.vo.mmgp.util.MMStringUtil;
/*     */
import nc.vo.moq.agentcert.MedAgentVO_148;
/*     */
import nc.vo.pub.BusinessException;

/*     */
/*     */public class SaveActionInterceptor
/*     */implements ActionInterceptor
/*     */{
	/* 29 */private ICBizView billForm = null;
	/*     */private IMDPersistenceQueryService service;

	/*     */
	/*     */public boolean beforeDoAction(Action action, ActionEvent e)
	/*     */{
		/* 32 */return ((checkBatchandLotno()) && (checkSavePermit()));
		/*     */}

	/*     */
	/*     */private boolean checkBatchandLotno() {
		/* 36 */BillCardPanel panel = getBillForm().getBillCardPanel();
		/* 37 */panel.stopEditing();
		/* 38 */int count = panel.getRowCount();
		/* 39 */ValidationException exp = new ValidationException();
		/* 40 */Map LotnotorowNo = new HashMap();
		/* 41 */Map batchcodeToMap = new HashMap();
		/* 42 */for (int i = 0; i < count; ++i) {
			/* 43 */String batchcode = (String) panel.getBodyValueAt(i,
					"vbatchcode");
			/* 44 */String lotNo = (String) panel.getBodyValueAt(i,
					"vlotno_148");
			/* 45 */String rowNo = (String) panel.getBodyValueAt(i, "crowno");
			/* 46 */String cmaterialvid = (String) panel.getBodyValueAt(i,
					"cmaterialvid");
			/*     */
			/* 48 */for (int j = i; j < count; ++j) {
				/* 49 */if ((batchcode == null)
						|| (lotNo == null)
						|| (!(batchcode.equals((String) panel.getBodyValueAt(j,
								"vbatchcode"))))
						||
						/* 50 */(lotNo.equals((String) panel.getBodyValueAt(j,
								"vlotno_148")))
						|| (!(cmaterialvid.equals((String) panel
								.getBodyValueAt(j, "cmaterialvid")))))
					continue;
				/* 51 */exp.addValidationFailure(new ValidationFailure("第"
						+ rowNo + "行和第" + panel.getBodyValueAt(j, "crowno")
						+ "行一个【批次】只允许一个【产品批号】！\n"));
				/*     */}
			/*     */
			/*     */}
		/*     */
		/* 56 */if (exp.getFailureMessage().toArray().length > 0) {
			/* 57 */ShowStatusBarMsgUtil.showErrorMsg(
			/* 58 */NCLangRes.getInstance().getStrByID("uif2",
			/* 59 */"ExceptionHandlerWithDLG-000000"), exp.getFailureMessage()
					.toString(),
			/* 60 */this.billForm.getModel().getContext());
			/* 61 */return false;
			/*     */}
		/* 63 */return true;
		/*     */}

	/*     */
	/*     */private boolean checkSavePermit() {
		/* 67 */BillCardPanel panel = getBillForm().getBillCardPanel();
		/* 68 */panel.stopEditing();
		/* 69 */int count = panel.getRowCount();
		/* 70 */ValidationException exp = new ValidationException();
		/*     */try {
			/* 72 */for (int i = 0; i < count; ++i) {
				/* 73 */Object rowNo = panel.getBodyValueAt(i, "crowno");
				/* 74 */String pk_material = (String) panel.getBodyValueAt(i,
						"cmaterialvid");
				/* 75 */String pk_org = (String) panel.getBodyValueAt(i,
						"pk_org");
				/* 76 */boolean isLotNo = MaterialPropertyUtil.isLotNo(
						pk_material, null);
				/* 77 */boolean bvalidmanageFlag = MaterialPropertyUtil
						.bvalidmanageFlag(pk_material);
				/*     */
				/* 79 */boolean wholeManaFlag = MaterialPropertyUtil
						.wholeManaFalg(pk_material, pk_org);
				/* 80 */boolean qualityManFlag = MaterialPropertyUtil
						.qualityManFlag(pk_material, pk_org);
				/* 81 */Object vlotNo = panel.getBodyValueAt(i, "vlotno_148");
				/* 82 */Object dproducedate = panel.getBodyValueAt(i,
						"dproducedate_148");
				/* 83 */Object vinvaliddate = panel.getBodyValueAt(i,
						"vinvaliddate_148");
				/*     */
				/* 85 */String pk_agent_148 = (String) panel.getBodyValueAt(i,
						"pk_agent_148");
				/* 86 */if ((pk_agent_148 != null)
						&& (!(checkAgent(pk_agent_148)))) {
					/* 87 */exp.getFailures().clear();
					/* 88 */exp.addValidationFailure(
					/* 89 */new ValidationFailure("第" + rowNo
							+ "行代理人信息不存在，请重新选择！"));
					/*     */}
				/* 91 */if ((isLotNo)
						&& (MMStringUtil.isObjectStrEmpty(vlotNo))) {
					/* 92 */exp.getFailures().clear();
					/* 93 */exp
							.addValidationFailure(
							/* 94 */new ValidationFailure("第" + rowNo
									+ "行【产品批号】不能为空！"));
					/*     */}
				/*     */
				/* 97 */if ((isLotNo) && (bvalidmanageFlag)
						&& (((MMStringUtil.isObjectStrEmpty(dproducedate)) ||
						/* 98 */(MMStringUtil.isObjectStrEmpty(vinvaliddate))))) {
					/* 99 */exp.getFailures().clear();
					/* 100 */exp.addValidationFailure(
					/* 101 */new ValidationFailure("第" + rowNo
							+ "行物料启用了批号管理，效期管理，【生产日期】、【有效期至】不可为空！"));
					/*     */}
				/*     */
				/* 104 */if ((!(wholeManaFlag))
						|| (!(qualityManFlag))
						|| ((!(MMStringUtil.isObjectStrEmpty(dproducedate))) &&
						/* 105 */(!(MMStringUtil.isObjectStrEmpty(vinvaliddate)))))
					continue;
				/* 106 */exp.getFailures().clear();
				/* 107 */exp.addValidationFailure(
				/* 108 */new ValidationFailure("第" + rowNo
						+ "行物料启用了批次管理，保质期管理，【生产日期】、【有效期至】不可为空！"));
				/*     */}
			/*     */
			/* 112 */if (exp.getFailureMessage().toArray().length > 0) {
				/* 113 */ShowStatusBarMsgUtil.showErrorMsg(
				/* 114 */NCLangRes.getInstance().getStrByID("uif2",
				/* 115 */"ExceptionHandlerWithDLG-000000"), exp
						.getFailureMessage().toString(),
				/* 116 */this.billForm.getModel().getContext());
				/* 117 */return false;
				/*     */}
			/*     */} catch (Exception e) {
			/* 120 */throw new BusinessExceptionAdapter((BusinessException) e);
			/*     */}
		/* 122 */return true;
		/*     */}

	/*     */
	/*     */public void afterDoActionSuccessed(Action action, ActionEvent e)
	/*     */{
		/*     */}

	/*     */
	/*     */public boolean afterDoActionFailed(Action action, ActionEvent e,
			Throwable ex)
	/*     */{
		/* 133 */return true;
		/*     */}

	/*     */
	/*     */public ICBizView getBillForm() {
		/* 137 */return this.billForm;
		/*     */}

	/*     */
	/*     */public void setBillForm(ICBizView billForm) {
		/* 141 */this.billForm = billForm;
		/*     */}

	/*     */
	/*     */private boolean checkAgent(String pk_agent)
	/*     */throws BusinessException
	/*     */{
		/* 150 */boolean flag = true;
		/* 151 */if (pk_agent != null) {
			/* 152 */Collection res = getService().queryBillOfVOByCond(
					MedAgentVO_148.class,
					" pk_agent='" + pk_agent + "' and isnull(dr,0)=0 ", false);
			/* 153 */if (res.isEmpty()) {
				/* 154 */flag = false;
				/*     */}
			/*     */}
		/* 157 */return flag;
		/*     */}

	/*     */
	/*     */public IMDPersistenceQueryService getService() {
		/* 161 */if (this.service == null) {
			/* 162 */this.service = MDPersistenceService
					.lookupPersistenceQueryService();
			/*     */}
		/* 164 */return this.service;
		/*     */}
	/*     */
}