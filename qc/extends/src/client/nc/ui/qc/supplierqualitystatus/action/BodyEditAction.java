package nc.ui.qc.supplierqualitystatus.action;

import nc.ui.qc.supplierqualitystatus.model.BodyManageTableModel;
import nc.ui.uif2.actions.batch.BatchEditAction;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.jcom.lang.StringUtil;

/**
 * 按钮编辑的动作
 * 
 * @author lkp
 * 
 */
public class BodyEditAction extends BatchEditAction {

	private static final long serialVersionUID = -2886050746728516317L;

	@Override
	protected boolean isActionEnable() {
		Object obj = ((BodyManageTableModel) getModel()).getFuncModel()
				.getSelectedData();
		if (obj == null )
			return false;
		SupStockVO vo = (SupStockVO) obj;
		if (obj == null || StringUtil.isEmptyWithTrim(vo.getPk_supstock()))
			return false;
		return super.isActionEnable();
	}
}
