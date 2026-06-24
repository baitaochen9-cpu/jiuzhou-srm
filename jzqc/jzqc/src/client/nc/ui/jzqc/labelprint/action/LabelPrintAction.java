package nc.ui.jzqc.labelprint.action;

import java.awt.event.ActionEvent;

import nc.ui.pub.print.PrintEntry;
import nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pub.BusinessException;

/**
 * 打印按钮
 * 
 * @author zhw
 * 
 */
public class LabelPrintAction extends MetaDataBasedPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1811961090975795908L;

	public LabelPrintAction() {
		super();
	}

	public void doAction(ActionEvent e) throws Exception {

		Object o = getModel().getSelectedData();
		if (o == null) {
			throw new BusinessException("请先选中数据！");
		} else {
			AggLabelPrintHVO hvo = (AggLabelPrintHVO) o;
			if (hvo.getParentVO() != null
					&& hvo.getParentVO().getBprintstatus() != null
					&& !hvo.getParentVO().getBprintstatus().booleanValue()) {
				throw new BusinessException("标签可打印状态为否不能打印！");
			}

			if (hvo.getParentVO() != null
					&& hvo.getParentVO().getBlabelstatus() != null
					&& !hvo.getParentVO().getBlabelstatus().booleanValue()) {
				throw new BusinessException("标签失效不能打印！");
			}
		}
		setPreview(true);
		super.doAction(e);
	}

	private nc.ui.pub.print.PrintEntry printEntry;

	@Override
	protected PrintEntry getPrintEntry() {
		// if (null == this.printEntry) {
		this.printEntry = super.getPrintEntry();
		setPrintListener(new LabelPrintRecordListener(
				getModel()));
		this.printEntry.setPrintListener(getPrintListener());
		// }
		return this.printEntry;
	}
}