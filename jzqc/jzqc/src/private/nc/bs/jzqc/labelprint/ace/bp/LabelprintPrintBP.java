package nc.bs.jzqc.labelprint.ace.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.database.IDQueryBuilder;
import nc.vo.bd.printcheck.PrintResultVO;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uapbd.printnumber.PrintNumberVO;

/**
 * 标准单据打印次数的BP
 */
public class LabelprintPrintBP {

	/**
	 * 更新打印次数动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, PrintResultVO> getPrintResultVO(
			AggLabelPrintHVO[] clientBills) throws BusinessException {

		List<String> list = new ArrayList<String>();
		for (AggLabelPrintHVO clientBill : clientBills) {
			list.add(clientBill.getParentVO().getPrimaryKey());
		}

		StringBuffer sql = new StringBuffer();
		IDQueryBuilder iq = new IDQueryBuilder();
		sql.append(iq.buildSQL("billid", list.toArray(new String[list.size()])));
		VOQuery<PrintNumberVO> query = new VOQuery<PrintNumberVO>(
				PrintNumberVO.class);

		PrintNumberVO[] voLogs = query.query(" and " + sql.toString(), null);

		Map<String, PrintResultVO> map = getCheckResult(voLogs);
		return map;
	}

	private Map<String, PrintResultVO> getCheckResult(PrintNumberVO[] voLogs) {
		Map<String, PrintResultVO> billid_result_map = new HashMap<String, PrintResultVO>();
		if (voLogs == null || voLogs.length == 0)
			return billid_result_map;
		for (PrintNumberVO vo : voLogs) {
			PrintResultVO printResultvo = new PrintResultVO();
			printResultvo.setPk_printnumber(vo.getPk_printnumber());
			printResultvo.setPk_org(vo.getPk_org());
			printResultvo.setPk_billtype(vo.getPk_billtype());
			printResultvo.setBillid(vo.getBillid());
			printResultvo.setBillcode(vo.getBillcode());
			printResultvo.setPrintcount(vo.getPrintnumber());
			printResultvo.setPrintlimit(vo.getPrintlimit());
			printResultvo.setBilldata(vo.getBilldata());
			printResultvo.setAlternum(vo.getAlternum());
			billid_result_map.put(vo.getBillid(), printResultvo);
		}
		return billid_result_map;
	}

	public AggLabelPrintHVO[] updatePrintResultVO(AggLabelPrintHVO[] clientBills)
			throws BusinessException {

		List<LabelPrintHVO> list = new ArrayList<LabelPrintHVO>();
		for (AggLabelPrintHVO clientBill : clientBills) {
			LabelPrintHVO hvo = clientBill.getParentVO();
			hvo.setBprintstatus(UFBoolean.FALSE);
			list.add(hvo);
		}

		VOUpdate<LabelPrintHVO> update = new VOUpdate<LabelPrintHVO>();
		update.update(list.toArray(new LabelPrintHVO[list.size()]),
				new String[] { "bprintstatus", "iprintcount" });
		return clientBills;
	}
}
