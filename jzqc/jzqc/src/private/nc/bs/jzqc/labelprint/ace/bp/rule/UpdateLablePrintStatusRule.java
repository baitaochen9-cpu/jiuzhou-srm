package nc.bs.jzqc.labelprint.ace.bp.rule;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.pub.lang.UFBoolean;

public class UpdateLablePrintStatusRule {
	public void process(AggLabelPrintHVO[] vos, UFBoolean ufboolean) {
		if (ArrayUtils.isEmpty(vos))
			return;
		List<LabelPrintHVO> list = new ArrayList<LabelPrintHVO>();
		for (AggLabelPrintHVO clientBill : vos) {
			LabelPrintHVO hvo = clientBill.getParentVO();
			hvo.setBprintstatus(ufboolean);
			list.add(hvo);
		}

		VOUpdate<LabelPrintHVO> update = new VOUpdate<LabelPrintHVO>();
		update.update(list.toArray(new LabelPrintHVO[list.size()]),
				new String[] { "bprintstatus" });
	}
}