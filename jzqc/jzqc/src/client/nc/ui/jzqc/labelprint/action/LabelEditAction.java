package nc.ui.jzqc.labelprint.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.EditAction;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pub.BusinessException;

public class LabelEditAction extends EditAction {
	private static final long serialVersionUID = 1L;

	public void doAction(ActionEvent e) throws Exception {

		Object o = getModel().getSelectedData();
		if (o == null) {
			throw new BusinessException("请先选中数据！");
		} else {
			AggLabelPrintHVO hvo = (AggLabelPrintHVO) o;
			if (hvo.getParentVO() != null
					&& hvo.getParentVO().getIprintcount() != null
					&& hvo.getParentVO().getIprintcount().intValue() > 0) {
				throw new BusinessException("打印次数大于零的数据不能修改！");
			}
		}
		super.doAction(e);
	}
}
