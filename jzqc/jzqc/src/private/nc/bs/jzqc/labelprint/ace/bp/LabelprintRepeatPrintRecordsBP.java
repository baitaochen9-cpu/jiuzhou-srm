package nc.bs.jzqc.labelprint.ace.bp;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.jzqc.labelprintapply.LabelprintapplyHVO;

/**
 * 标准单据重打申请记录的BP
 */
public class LabelprintRepeatPrintRecordsBP {

	/**
	 * 重打申请记录动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggLabelprintapplyHVO[] repeatPrintRecords(
			AggLabelPrintHVO clientFullVOs) {
		VOQuery<LabelprintapplyHVO> query = new VOQuery<LabelprintapplyHVO>(
				LabelprintapplyHVO.class);
		String condition = " and pk_org = '"
				+ clientFullVOs.getParentVO().getPk_org()
				+ "' and srcbillid = '"
				+ clientFullVOs.getParentVO().getPk_labelprint() + "'";
		LabelprintapplyHVO[] hvos = query.query(condition, null);
		List<AggLabelprintapplyHVO> list = new ArrayList<>();
		if (hvos != null && hvos.length > 0) {
			for (LabelprintapplyHVO hvo : hvos) {
				AggLabelprintapplyHVO aggvo = new AggLabelprintapplyHVO();
				aggvo.setParentVO(hvo);
				list.add(aggvo);
			}
		}
		return list.toArray(new AggLabelprintapplyHVO[list.size()]);
	}

}
