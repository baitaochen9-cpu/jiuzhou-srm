package nc.ui.pp.m28.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.IProcessService;
import nc.ui.pp.m28.action.util.StateUtil;
import nc.ui.pp.m28.editor.rule.PAuditFillCalcPreferRule;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.pub.power.PowerCheckUtils;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.scmpub.res.billtype.PPBillType;
import nccloud.base.exception.BusinessException;

import org.apache.commons.lang.ArrayUtils;

public class PriceApproveAction extends ApproveScriptAction {
	private static final long serialVersionUID = 4320445779266309601L;

	public void doAction(ActionEvent e) throws Exception {
		
		
		PAuditFillCalcPreferRule fillCalcPreRule = new PAuditFillCalcPreferRule();
		BillCardPanel cardpanel = ((ShowUpableBillForm) this.editor)
				.getBillCardPanel();

		PriceAuditVO[] savebeforevos = fillCalcPreRule
				.convSelPriceAuditVOs(this.model);

		super.doAction(e);
		for (PriceAuditVO beforevo : savebeforevos)
			fillCalcPreRule.process(cardpanel, beforevo);
	     
	}

	private void dataPowerCheck() {
		PriceAuditVO aggVo = (PriceAuditVO) getModel().getSelectedData();
		PowerCheckUtils.checkHasPermission(new PriceAuditVO[]{aggVo},
				PPBillType.PriceAudit.getCode(), "approve", "vbillcode");
	}

	protected boolean isActionEnable() {
		IProcessService check = (IProcessService)NCLocator.getInstance().lookup(IProcessService.class.getName());
		  //判断
	     boolean outSystem = false;
			BillManageModel prModel = getModel();
			Object[] seldatas = prModel.getSelectedOperaDatas();
			if (ArrayUtils.isEmpty(seldatas)) {
				Object seldata = prModel.getSelectedData();
				if (null == seldata) {
					return false;
				}
			} else if (seldatas.length > 1) {
				return true;
			}
			PriceAuditVO aggVO = (PriceAuditVO) prModel.getSelectedData();
			try {
				outSystem = check.is2oa(aggVO.getParentVO().getPk_org());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new BusinessException("检查同步oa参数出错");

			}
			 if(outSystem){
		    	 return false;
		     }
			return ((StateUtil.isFree(aggVO)) || (StateUtil.isApproving(aggVO)) || (StateUtil
					.isCommit(aggVO)));
		     
	}
}