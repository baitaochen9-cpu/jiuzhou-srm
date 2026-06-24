package nc.bs.jzqc.labelprint.ace.bp;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * 标准单据失效的BP
 */
public class LabelprintInvalidBP {

	/**
	 * 失效动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 * @throws BusinessException
	 */
	public AggLabelPrintHVO[] printInvalid(AggLabelPrintHVO[] clientBills)
			throws BusinessException {
		BillTransferTool<AggLabelPrintHVO> tool = new BillTransferTool(
				clientBills);
		AggLabelPrintHVO[] originBills = tool.getOriginBills();
		clientBills = tool.getClientFullInfoBill();
		List<LabelPrintHVO> list = new ArrayList<LabelPrintHVO>();
		for (AggLabelPrintHVO clientBill : originBills) {
			LabelPrintHVO hvo = clientBill.getParentVO();
			if (hvo != null && hvo.getIprintcount() != null
					&& hvo.getIprintcount().intValue() == 0) {
				throw new BusinessException("打印次数等于零的数据不能失效！");
			}

			if (hvo != null && hvo.getBlabelstatus() != null
					&& !hvo.getBlabelstatus().booleanValue()) {
				throw new BusinessException("标签已失效不能再次失效！");
			}

			hvo.setBlabelstatus(UFBoolean.FALSE);
			list.add(hvo);
		}

		VOUpdate<LabelPrintHVO> update = new VOUpdate<LabelPrintHVO>();
		update.update(list.toArray(new LabelPrintHVO[list.size()]),
				new String[] { "blabelstatus" });
		return clientBills;
	}

}
