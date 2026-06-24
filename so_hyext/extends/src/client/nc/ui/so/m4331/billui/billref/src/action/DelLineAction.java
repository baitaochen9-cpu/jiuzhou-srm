package nc.ui.so.m4331.billui.billref.src.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.billref.src.action.SuperAction;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.trade.voutils.SafeCompute;

public class DelLineAction extends SuperAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DelLineAction() {
		setBtnName("删行");
		putValue("ShortDescription", "删行");
	}

	public void doAction(ActionEvent e) throws Exception {
		String[] tableCodes = getRefContext().getRefBill().getBillListPanel()
				.getBillListData().getBodyTableCodes();
		int row[] = getRefContext().getRefBill().getBillListPanel()
				.getBodyScrollPane(tableCodes[0]).getTable().getSelectedRows();
		if (row == null || row.length == 0) {
			throw new BusinessException("请先选中行进行删除");
		}
		getRefContext().getRefBill().getBillListPanel()
				.getBodyScrollPane(tableCodes[0]).delLine();
		int selrow = getRefContext().getRefBill().getBillListPanel()
				.getHeadTable().getSelectedRow();

		if (selrow < 0) {
			throw new BusinessException("请先选中表头行进行新增");
		}

		String headPK = (String) getRefContext().getRefBill()
				.getBillListPanel().getHeadBillModel()
				.getValueAt(selrow, "pk_salepacklist");
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
		calJianshu();
	}
	

	private void calJianshu() {
		int rowcount = getRefContext().getRefBill().getBillListPanel().getBodyBillModel().getRowCount();

		UFDouble tnpiece = UFDouble.ZERO_DBL;

		for (int i = 0; i < rowcount; i++) {
			UFDouble npiece = (UFDouble) getRefContext().getRefBill().getBillListPanel().getBodyBillModel().getValueAt(i,
					"nweight");
			tnpiece = SafeCompute.add(tnpiece, npiece);
		}
		int row = getRefContext().getRefBill().getBillListPanel().getHeadTable().getSelectedRow();
		getRefContext().getRefBill().getBillListPanel().getHeadBillModel().setValueAt(tnpiece, row, "def1");
		String headPK = getRefContext().getRefBill().getBillListPanel().getBillRowManager().getSelectedHeadPk();
		AggregatedValueObject aggvo = getRefContext().getRefBill()
				.getRefBillModel().getBillVO(headPK);
		aggvo.getParentVO().setAttributeValue("def1", tnpiece);

	}

	protected boolean isActionEnable() {
		return true;
	}
}