package nc.ui.emm.pmbase.action;

import nc.ui.am.action.taskself.DeleteAction;
import nc.ui.am.action.taskself.IBillOperateService;
import nc.ui.emm.pmbase.model.PMDeleteService;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.pub.AggregatedValueObject;

/**
 * <p>
 * Ô¤·ÀÐÔÎ¬»¤É¾³ý°´Å¥:
 * 
 * </p>
 * 
 * @author cuikai
 * @version 6.0
 */
@SuppressWarnings("serial")
public class PMDeleteAction extends DeleteAction {

	@Override
	public IBillOperateService getBillOperateService() {
		return new PMDeleteService();

	}
	
	@SuppressWarnings("restriction")
	@Override
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
