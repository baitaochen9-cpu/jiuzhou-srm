package nc.bs.jzqc.labelprint.ace.bp;

import java.util.ArrayList;
import java.util.List;

import nc.bs.ic.pub.env.ICBSContext;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.jzqc.labelprintapply.LabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pub.pf.IPfRetCheckInfo;

/**
 * 标准单据重打申请的BP
 */
public class LabelprintRepeatPrintApplyBP {

	/**
	 * 重打申请动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 * @throws BusinessException
	 */
	ICBSContext context = new ICBSContext();

	public AggLabelPrintHVO[] repeatPrintApply(AggLabelPrintHVO[] clientBills)
			throws BusinessException {
		
		String reason = clientBills[0].getParentVO().getVnote();
		BillTransferTool<AggLabelPrintHVO> tool = new BillTransferTool(
				clientBills);
		AggLabelPrintHVO[] originBills = tool.getOriginBills();
		clientBills = tool.getClientFullInfoBill();
		List<AggLabelprintapplyHVO> list = new ArrayList<AggLabelprintapplyHVO>();
		for (AggLabelPrintHVO clientBill : originBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			check(clientBill);
			AggLabelprintapplyHVO aggvo = changeAggLabelprintapplyHVO(clientBill,reason);
			list.add(aggvo);
		}
		savechangeAggLabelprintapplyHVO(list);
		return clientBills;
	}

	private void check(AggLabelPrintHVO clientBill) throws BusinessException {
		if (clientBill.getParentVO() != null
				&& clientBill.getParentVO().getBprintstatus() != null
				&& clientBill.getParentVO().getBprintstatus().booleanValue()) {
			throw new BusinessException("标签可打印状态为是不能重复打印申请！");
		}

		if (clientBill.getParentVO() != null
				&& clientBill.getParentVO().getBlabelstatus() != null
				&& !clientBill.getParentVO().getBlabelstatus().booleanValue()) {
			throw new BusinessException("标签已失效不能重复打印申请！");
		}

		// 存在未审批通过的重复打印申请

		VOQuery<LabelprintapplyHVO> query = new VOQuery<LabelprintapplyHVO>(
				LabelprintapplyHVO.class);

		LabelprintapplyHVO[] hvos = query.query(" and srcbillid ='"
				+ clientBill.getParentVO().getPk_labelprint() + "'", null);

		if (hvos == null || hvos.length == 0)
			return;

		for (LabelprintapplyHVO hvo : hvos) {
			if (IPfRetCheckInfo.PASSING != hvo.getApprovestatus().intValue()) {
				throw new BusinessException("存在未审批通过的重复打印申请！");
			}
		}
	}

	private AggLabelprintapplyHVO changeAggLabelprintapplyHVO(
			AggLabelPrintHVO clientBill,String reason) {
		AggLabelprintapplyHVO aggvo = new AggLabelprintapplyHVO();
		LabelprintapplyHVO hvo = new LabelprintapplyHVO();
		LabelPrintHVO phvo = clientBill.getParentVO();

		// 设置主组织默认值

		hvo.setAttributeValue("pk_group", phvo.getPk_group());
		hvo.setAttributeValue("pk_org", phvo.getPk_org());
		hvo.setAttributeValue("pkorg", phvo.getPk_org());
		// 设置单据状态、单据业务日期默认值
		hvo.setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
		hvo.setAttributeValue("billdate",
				new UFDate(System.currentTimeMillis()));
		hvo.setAttributeValue("billtype", "JZ11");
		hvo.setAttributeValue("transtype", "JZ11");
		hvo.setAttributeValue("transtypepk", PfServiceScmUtil
				.getTrantypeidByCode(new String[] { "JZ11" }).get("JZ11"));

		hvo.setAttributeValue("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		hvo.setAttributeValue("creator", context.getUserID());
		hvo.setAttributeValue("maketime",
				new UFDateTime(System.currentTimeMillis()));
		hvo.setAttributeValue("billmaker", context.getUserID());

		hvo.setAttributeValue("iprintcount", phvo.getIprintcount());
		hvo.setAttributeValue("proposer", context.getUserID());
		hvo.setCoop_bill_code(phvo.getBillno());
		hvo.setCoop_bill_type(phvo.getSrcbilltype());
		hvo.setSrcbillid(phvo.getPk_labelprint());
		hvo.setSrcbilltype(phvo.getBilltype());
		hvo.setPk_material(phvo.getPk_material());
		hvo.setPk_srcmaterial(phvo.getPk_srcmaterial());
		hvo.setNum_b(phvo.getNum_b());
		hvo.setCastunitid(phvo.getCastunitid());
		hvo.setExexute_desc(reason);
		aggvo.setParentVO(hvo);
		return aggvo;
	}

	private void savechangeAggLabelprintapplyHVO(
			List<AggLabelprintapplyHVO> list) {

		PfServiceScmUtil.processBatch("SAVEBASE", "JZ11",
				list.toArray(new AggLabelprintapplyHVO[list.size()]), null,
				null);
	}

}
