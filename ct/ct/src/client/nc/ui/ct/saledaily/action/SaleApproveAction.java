/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.ui.ct.saledaily.action;
/*    */
/*    */import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.IProcessService;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction;
import nc.vo.ct.rule.ActionStateRule;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.base.exception.BusinessException;
/*    */
/*    */
/*    */
/*    */
/*    */public class SaleApproveAction extends ApproveScriptAction
/*    */{
	IProcessService check = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
	/*    */private static final long serialVersionUID = 3790165024661531170L;
	/*    */
	/*    */public void doAction(ActionEvent e)
	/*    */throws Exception
	/*    */{
		/* 40 */super.doAction(e);
		/*    */}
	/*    */
	/*    */protected boolean isActionEnable()
	/*    */{boolean outSystem = false;
		/* 45 */if (getModel().getSelectedOperaDatas() == null) {
			/* 46 */return false;
			/*    */}
		AggCtSaleVO aggVO = (AggCtSaleVO) getModel().getSelectedData();
		try {
			outSystem = check.is2oa(aggVO.getParentVO().getPk_org());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException("检查同步oa参数出错");

		}
		if (outSystem) {
			return false;
		}
		/* 48 */if (getModel().getSelectedOperaDatas().length > 1) {
			/* 49 */return true;
			/*    */}
		/*    */
		/* 52 */ActionStateRule rule = new ActionStateRule();
		/* 53 */return ((rule.isHaveFree(getModel().getSelectedData())) || (rule
				.isHaveApproving(getModel().getSelectedData())));
		/*    */}
	/*    */
}