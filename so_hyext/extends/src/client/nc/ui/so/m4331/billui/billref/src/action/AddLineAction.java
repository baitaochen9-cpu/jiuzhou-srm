package nc.ui.so.m4331.billui.billref.src.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.billref.src.action.SuperAction;
import nc.vo.pub.BusinessException;
import nc.vo.so.salepacklist.SalePackListBVO;

public class AddLineAction extends SuperAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddLineAction() {
		setBtnName("增行");
		putValue("ShortDescription", "增行");
	}

	public void doAction(ActionEvent e) throws Exception {
		String[] tableCodes = getRefContext().getRefBill().getBillListPanel()
				.getBillListData().getBodyTableCodes();
		// int row[] = getRefContext().getRefBill().getBillListPanel()
		// .getBodyScrollPane(tableCodes[0]).getTable().getSelectedRows();
		// if(row == null || row.length==0){
		// throw new BusinessException("请先选中行进行新增");
		// }
		// getRefContext().getRefBill().getBillListPanel()
		// .getBodyScrollPane(tableCodes[0]).copyLine();
		// getRefContext().getRefBill().getBillListPanel()
		// .getBodyScrollPane(tableCodes[0]).pasteLineToTail();
		// getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
		getRefContext().getRefBill().getBillListPanel()
				.getBodyScrollPane(tableCodes[0]).addLine();

		int selrow = getRefContext().getRefBill().getBillListPanel()
				.getHeadTable().getSelectedRow();

		if (selrow < 0) {
			throw new BusinessException("请先选中表头行进行新增");
		}

		String headPK = (String) getRefContext().getRefBill()
				.getBillListPanel().getHeadBillModel()
				.getValueAt(selrow, "pk_salepacklist");

		String name = (String) getRefContext().getRefBill().getBillListPanel()
				.getHeadBillModel().getValueAt(selrow, "name");

		String def5 = (String) getRefContext().getRefBill().getBillListPanel()
				.getHeadBillModel().getValueAt(selrow, "def5");
		int rowcount = getRefContext().getRefBill().getBillListPanel()
				.getBodyBillModel().getRowCount();
		getRefContext().getRefBill().getBillListPanel().getBodyBillModel()
				.setValueAt(headPK, rowcount - 1, "pk_salepacklist");
		getRefContext().getRefBill().getBillListPanel().getBodyBillModel()
				.setValueAt(name, rowcount - 1, "batchcode");
		getRefContext().getRefBill().getBillListPanel().getBodyBillModel()
				.setValueAt(def5, rowcount - 1, "unit");

		getRefContext()
				.getRefBill()
				.getRefBillModel()
				.updateBodyRowVOs(
						headPK,
						getRefContext()
								.getRefBill()
								.getBillListPanel()
								.getBodyBillModel()
								.getBodyValueVOs(
										SalePackListBVO.class.getName()));
		getRefContext().getRefBill().getBillListPanel().getBodyBillModel()
				.execLoadFormula();
		getRefContext().getRefBill().getBillListPanel().getBodyBillModel().loadLoadRelationItemValue();
	}

	protected boolean isActionEnable() {
		return true;
	}
}