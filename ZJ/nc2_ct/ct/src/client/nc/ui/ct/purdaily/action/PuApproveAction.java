package nc.ui.ct.purdaily.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.IProcessService;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.rule.ActionStateRule;
import nccloud.base.exception.BusinessException;

public class PuApproveAction extends ApproveScriptAction {
	private static final long serialVersionUID = 3790165024661531170L;

	public void doAction(ActionEvent e) throws Exception {
		super.doAction(e);
	}

	protected boolean isActionEnable() {
		IProcessService check = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
		  //判断
		boolean outSystem = false;

		boolean isEnable = (getModel().getAppUiState() == AppUiState.NOT_EDIT)
				&& (null != getModel().getSelectedData());

		if (isEnable) {
			
			if ((getModel().getSelectedOperaDatas() == null)
					|| (getModel().getSelectedOperaDatas().length == 1)) {
				AggCtPuVO aggVO = (AggCtPuVO) getModel().getSelectedData();
				try {
					outSystem = check.is2oa(aggVO.getParentVO().getPk_org());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new BusinessException("检查同步oa参数出错");

				}
				if (outSystem) {
					return false;
				}
				ActionStateRule rule = new ActionStateRule();
				return ((rule.isHaveFree(getModel().getSelectedData())) || (rule
						.isHaveApproving(getModel().getSelectedData())));
			}

			if (getModel().getSelectedOperaDatas().length > 1) {
				return true;
			}
			
		}
		return isEnable;

	}
}