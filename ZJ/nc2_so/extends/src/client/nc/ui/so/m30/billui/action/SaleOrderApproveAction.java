/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.ui.so.m30.billui.action;
/*    */
/*    */import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.pubapp.pub.exception.IResumeException;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction;
import nc.ui.scmpub.util.ResumeExceptionUIProcessUtils;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.pub.enumeration.BillStatus;
import nccloud.base.exception.BusinessException;
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */public class SaleOrderApproveAction extends ApproveScriptAction
/*    */{
	IProcessService check = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
	/*    */protected boolean isActionEnable()
	/*    */{
		/* 29 */Object[] seldatas = this.model.getSelectedOperaDatas();
		/*    */boolean outSystem = false;
		/* 31 */if ((this.model.getAppUiState() == AppUiState.NOT_EDIT)
				&& (null != seldatas) && (seldatas.length > 1))
		/*    */{
			SaleOrderVO aggVO = (SaleOrderVO) getModel().getSelectedData();
			try {
				outSystem = check.is2oa(aggVO.getParentVO().getPk_org());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new BusinessException("检查同步oa参数出错");

			}
			if (outSystem) {
				return false;
			}
			/* 33 */return true;
			/*    */}
		/* 35 */Object selectedData = this.model.getSelectedData();
		/* 36 */Integer status = null;
		/* 37 */if ((null != selectedData)
				&& (selectedData instanceof SaleOrderVO)) {
			/* 38 */SaleOrderVO selorder = (SaleOrderVO) selectedData;
			/* 39 */status = selorder.getParentVO().getFstatusflag();
			try {
				outSystem = check.is2oa(selorder.getParentVO().getPk_org());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new BusinessException("检查同步oa参数出错");

			}
			if (outSystem) {
				return false;
			}
			/*    */}
		/* 41 */boolean isEnable = (this.model.getAppUiState() == AppUiState.NOT_EDIT)
				&& (selectedData != null)
				&& (((BillStatus.FREE.equalsValue(status)) || (BillStatus.AUDITING
						.equalsValue(status))));
		/*    */SaleOrderVO aggVO = (SaleOrderVO) getModel().getSelectedData();
		
		/* 47 */return isEnable;
		/*    */}
	/*    */
	/*    */protected boolean isResume(IResumeException resumeInfo)
	/*    */{
		/* 52 */return ResumeExceptionUIProcessUtils.isResume(resumeInfo,
				getFlowContext());
		/*    */}
	/*    */
}