package nc.bs.so.salepacklist.ace.bp.rule;

import java.util.ArrayList;
import java.util.List;

import nc.bs.ml.NCLangResOnserver;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.so.salepacklist.SalePackListHVO;
import nc.vo.trade.checkrule.VOChecker;

public class CheckValityRule implements IRule<AggSalePackListHVO> {
	public void process(AggSalePackListHVO[] invoices) {
		for (AggSalePackListHVO vo : invoices) {
			checkHeadValidity(vo.getParentVO());
			checkBodyValidity((SalePackListBVO[]) vo.getChildrenVO());
		}
	}

	private void checkBodyValidity(SalePackListBVO[] childrenVOs) {
		List<String> errField = new ArrayList<String>();
		for (SalePackListBVO bvo : childrenVOs) {
			if (bvo.getStatus() != 3) {

				if (VOChecker.isEmpty(bvo.getPalleno())) {
					errField.add("托盘号不能为空");
				}
				if (VOChecker.isEmpty(bvo.getSpec())) {
					errField.add("包装规格不能为空");
				}

				if (VOChecker.isEmpty(bvo.getSpec_t())) {
					errField.add("托盘规格不能为空");
				}
				if (VOChecker.isEmpty(bvo.getUnit())) {
					errField.add("计量单位不能为空");
				}
				if (VOChecker.isEmpty(bvo.getNpiece())) {
					errField.add("件数不能为空");
				}

				if (errField.size() > 0) {
					StringBuilder errMsg = new StringBuilder(NCLangResOnserver
							.getInstance().getStrByID("4006002_0",
									"04006002-0122", null,
									new String[] { bvo.getRowno() }));

					errMsg.append((String) errField.get(0));
					for (int i = 1; i < errField.size(); i++) {
						errMsg.append(
								NCLangResOnserver.getInstance().getStrByID(
										"4006002_0", "04006002-0123")).append(
								(String) errField.get(i));
					}
					ExceptionUtils.wrappBusinessException(errMsg.toString());
				}
			}
		}
	}

	private void checkHeadValidity(SalePackListHVO head) {
		List<String> errField = new ArrayList<String>();

		if (VOChecker.isEmpty(head.getPk_org())) {
			errField.add(NCLangResOnserver.getInstance().getStrByID(
					"4006002_0", "04006002-0114"));
		}
		if (VOChecker.isEmpty(head.getDbilldate())) {
			errField.add(NCLangResOnserver.getInstance().getStrByID(
					"4006002_0", "04006002-0124"));
		}
		if (VOChecker.isEmpty(head.getTranstypepk())) {
			errField.add(NCLangResOnserver.getInstance().getStrByID(
					"4006002_0", "04006002-0125"));
		}

		if (errField.size() > 0) {
			StringBuilder errMsg = new StringBuilder(NCLangResOnserver
					.getInstance().getStrByID("4006002_0", "04006002-0127"));
			errMsg.append((String) errField.get(0));
			for (int i = 1; i < errField.size(); i++) {
				errMsg.append(
						NCLangResOnserver.getInstance().getStrByID("4006002_0",
								"04006002-0123")).append(
						(String) errField.get(i));
			}
			ExceptionUtils.wrappBusinessException(errMsg.toString());
		}
	}

}
