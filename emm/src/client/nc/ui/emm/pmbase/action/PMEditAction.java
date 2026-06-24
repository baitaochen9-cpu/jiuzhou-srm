package nc.ui.emm.pmbase.action;

import java.awt.event.ActionEvent;

import nc.ui.am.action.support.AMEditAction;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.emm.premaintain.PMHeadVO;
import nc.vo.emm.premaintain.utils.PMCommonUtils;
import nc.vo.pub.AggregatedValueObject;

/**
 * <p>
 * 预防性维护修改按钮:
 * 		如果已经生成工单，不能修改表头的设备和位置的对象。
 * </p>
 *
 * @author cuikai
 * @version 6.0
 */
@SuppressWarnings("serial")
public class PMEditAction extends AMEditAction {

	public void doAction(ActionEvent e) throws Exception {
		// 获得界面上所有数据，做校验用
		Object obj = getModel().getSelectedData();
		// 判断是否有修改的数据
		if (obj == null) {
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pmbase_0","04550002-0004")/*@res "请选择要修改的数据"*/);
			return;
		}
		// 修改前的校验
		validate(obj);
		// 设置状态
		toStatus();

		// 预防性维护生成下游单据,不能修改设备和位置
		if (PMCommonUtils.checkMakeDownriverBill((PMBillVO)obj)) {
			getBillForm().getBillCardPanel().getHeadItem(PMHeadVO.PK_EQUIP).setEnabled(false);
			getBillForm().getBillCardPanel().getHeadItem(PMHeadVO.PK_LOCATION).setEnabled(false);
		} else {
			getBillForm().getBillCardPanel().getHeadItem(PMHeadVO.PK_EQUIP).setEnabled(true);
			getBillForm().getBillCardPanel().getHeadItem(PMHeadVO.PK_LOCATION).setEnabled(true);
		}

		postProcess();
	}
	
	protected boolean isActionEnable() {
		// TODO Auto-generated method stub
		AggregatedValueObject selectedData = (AggregatedValueObject) this.getModel().getSelectedData();
		if(null == selectedData){
			return false;
		}
		if(selectedData instanceof PMBillVO){
			PMBillVO bill = (PMBillVO) selectedData;
			int status = bill.getParentVO().getEnablestate();
			if (2 == status) {
				return false;
			}
		}
		return super.isActionEnable();
	}

}