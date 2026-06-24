package nc.bs.jzqc.labelprint.ace.bp.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pfxx.util.ArrayUtils;

public class UpdateSerialNORule implements IRule<AggLabelPrintHVO> {
	@Override
	public void process(AggLabelPrintHVO[] vos) {
		if (ArrayUtils.isEmpty(vos))
			return;
		if (vos == null || vos.length == 0)
			return;

		for (AggLabelPrintHVO vo : vos) {
			vo.getParentVO().setVserial_number(
					getBuWei(vo.getParentVO().getSerial_number()));
			vo.getParentVO().setVserial_total(
					getBuWei(vo.getParentVO().getSerial_total()));
		}

	}

	private String getBuWei(Integer num) {

		if(num == null){
			return "0001";
		}
		if (num < 10) {
			return "000" + num;
		} else if (num < 100) {
			return "00" + num;
		} else if (num < 1000) {
			return "0" + num;
		} else {
			return "" + num;
		}
	}

}