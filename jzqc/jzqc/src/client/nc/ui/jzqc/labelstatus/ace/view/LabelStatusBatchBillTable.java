package nc.ui.jzqc.labelstatus.ace.view;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.view.ShowUpableBatchBillTable;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jzqc.labelstatus.LabelStatusVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class LabelStatusBatchBillTable extends ShowUpableBatchBillTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -886255764518913919L;

	@Override
	public boolean beforeEdit(BillEditEvent e) {

		int row = e.getRow();
		String key = e.getKey();
		BillItem item = getBillCardPanel().getBodyItem(key);
		UIRefPane batchref = (UIRefPane) item.getComponent();
		if ("labeltype".equals(key)) {// 标签类型
			batchref.getRefModel()
					.addWherePart("  and parentbilltype ='JZ01' ");
		} else if ("storestatus".equals(key)) {// 库存状态
			String pk_org = (String) getBillCardPanel().getBillModel()
					.getValueAt(row, "pk_org");
			if (StringUtil.isEmpty(pk_org)) {
				ExceptionUtils.wrappBusinessException("组织不能为空");
				return false;
			}
			LabelStatusVO vo = (LabelStatusVO) getBillCardPanel()
					.getBillModel().getBodyValueRowVO(row,
							LabelStatusVO.class.getName());
			batchref.getRefModel().addWherePart(
					"  and pk_org ='" + vo.getPk_org() + "' ");
		}
		return true;

	}

	/**
	 * 实现编辑后的逻辑
	 * 
	 * @param e
	 */
	protected void doAfterEdit(BillEditEvent e) {
		int row = e.getRow();
		String key = e.getKey();
		if ("pk_org".equals(key)) {// 组织
			getBillCardPanel().getBillModel().setValueAt(null, row,
					"storestatus");
		}
	}

}
