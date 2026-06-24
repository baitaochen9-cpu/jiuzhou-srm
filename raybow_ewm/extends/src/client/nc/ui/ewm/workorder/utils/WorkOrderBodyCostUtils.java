/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package nc.ui.ewm.workorder.utils;
/*     */
/*     */import java.util.ArrayList;
/*     */
import java.util.List;
/*     */
import nc.ui.am.util.BillCardPanelUtils;
/*     */
import nc.ui.pub.bill.BillCardPanel;
/*     */
import nc.ui.pub.bill.BillModel;
/*     */
import nc.vo.am.common.BizContext;
/*     */
import nc.vo.am.common.util.ArrayUtils;
/*     */
import nc.vo.am.common.util.ExceptionUtils;
/*     */
import nc.vo.am.common.util.UFDoubleUtils;
/*     */
import nc.vo.am.manager.CurrencyRateManager;
/*     */
import nc.vo.pub.BusinessException;
/*     */
import nc.vo.pub.SuperVO;
/*     */
import nc.vo.pub.lang.UFDate;
/*     */
import nc.vo.pub.lang.UFDouble;
/*     */
/*     */public class WorkOrderBodyCostUtils
/*     */{
	/* 122 */public static final Object[][] TABLE_EVENT_CONST = {
			{
					"wo_plan_inv",
					new String[]{"money_org", "nassistnum", "nnum", "price_org",
					"vchangerate"}, new String[]{"money_org", "pl_mtr_mny_org"}},
			{"wo_plan_psn",new String[] {"man_hours", "money", "person_num", "rate"},
						new String[]{"money", "pl_lbr_mny_org"}},
			{"wo_plan_tool", new String[]{"man_hours", "money", "tools_num", "rate"},
							new String[]{"money", "pl_tol_mny_org"}},
			{"wo_planotherexes", new String[]{"exesmoney"},new String[] {"exesmoney", "pl_oth_mny_org"}},
			{
					"wo_actual_inv",
					new String[]{"money_org", "nassistnum", "nnum", "price_org",
							"vchangerate"}, new String[]{"money_org", "ac_mtr_mny_org"}},
			{"wo_actual_psn", new String[]{"man_hours", "money", "rate"},
								new String[]{"money", "ac_lbr_mny_org"}},
			{"wo_actual_tool", new String[]{"man_hours", "money", "tools_num", "rate"},
									new String[]{"money", "ac_tol_mny_org"}},
			{"wo_actualotherexes",new String[] {"exesmoney"},
										new String[]{"exesmoney", "ac_oth_mny_org"}}};
	/*     */
	/* 162 */public static final String[] PL_MNY_ORG = {"pl_mtr_mny_org",
			"pl_lbr_mny_org", "pl_oth_mny_org", "pl_tol_mny_org"};
	/*     */
	/* 164 */public static final String[] AC_MNY_ORG = {"ac_mtr_mny_org",
			"ac_lbr_mny_org", "ac_oth_mny_org", "def4","ac_tol_mny_org"};
	/*     */
	/*     */public static void headTotalCostBody(BillCardPanel billCardPanel)
	/*     */{
		/*  41 */for (int i = 0; i < TABLE_EVENT_CONST.length; ++i) {
			/*  42 */Object tableConst = TABLE_EVENT_CONST[i][0];
			/*  43 */String[] readAndWriteItems = (String[]) (String[]) TABLE_EVENT_CONST[i][2];
			/*  44 */setTabCostToHead(billCardPanel, (String) tableConst,
					readAndWriteItems[0], readAndWriteItems[1]);
			/*     */}
		/*     */}
	/*     */
	/*     */public static void setTabCostToHead(BillCardPanel card,
			String tabcode, String moneyKey, String headTotalMnyKey)
	/*     */{
		/*  57 */BillModel bodyModel = card.getBillModel(tabcode);
		/*  58 */if (bodyModel == null) {
			/*  59 */return;
			/*     */}
		/*  61 */int rowCount = bodyModel.getRowCount();
		/*  62 */if (rowCount > 0) {
			/*  63 */UFDouble[] moneys = new UFDouble[rowCount];
			/*  64 */for (int i = 0; i < rowCount; ++i) {
				/*  65 */if (tabcode.equals("wo_plan_inv")) {
					/*  66 */if (BillCardPanelUtils.getBodyValue(card,
							tabcode, "src_bill_type", i) != null) {
						/*  67 */moneys[i] = UFDouble.ZERO_DBL;
						/*     */} else {
						/*  69 */UFDouble nmoney = (UFDouble) BillCardPanelUtils
								.getBodyValue(card, tabcode, moneyKey, i);
						/*  70 */moneys[i] = nmoney;
						/*     */}
					/*     */} else {
					/*  73 */UFDouble nmoney = (UFDouble) BillCardPanelUtils
							.getBodyValue(card, tabcode, moneyKey, i);
					/*  74 */moneys[i] = nmoney;
					/*     */}
				/*     */}
			/*  77 */UFDouble totalMny = UFDoubleUtils.add(moneys);
			/*  78 */BillCardPanelUtils.setHeadItemValue(card,
					headTotalMnyKey, totalMny);
			/*  79 */setHeadTotalMoney(card);
			/*     */} else {
			/*  81 */BillCardPanelUtils.setHeadItemValue(card,
					headTotalMnyKey, UFDouble.ZERO_DBL);
			/*  82 */setHeadTotalMoney(card);
			/*     */}
		/*     */}
	/*     */
	/*     */public static void setHeadTotalMoney(BillCardPanel card)
	/*     */{
		/*  92 */List headPlanMoneys = setHeadTotalMoneyByMnyConst(card,
				PL_MNY_ORG);
		/*     */
		/*  94 */List headActualMoneys = setHeadTotalMoneyByMnyConst(card,
				AC_MNY_ORG);
		/*     */
		/*  96 */BillCardPanelUtils.setHeadItemValue(card, "pl_ttl_mny_org",
				UFDoubleUtils.add((UFDouble[]) headPlanMoneys
						.toArray(new UFDouble[0])));
		/*     */
		/*  98 */BillCardPanelUtils.setHeadItemValue(card, "ac_ttl_mny_org",
				UFDoubleUtils.add((UFDouble[]) headActualMoneys
						.toArray(new UFDouble[0])));
		/*     */
		/* 101 */BillCardPanelUtils.setHeadLocalMoney(card,
				(String[]) ArrayUtils.addElement(
						ArrayUtils.addElement(PL_MNY_ORG, AC_MNY_ORG),
						new String[]{"pl_ttl_mny_org", "ac_ttl_mny_org"}),
				BizContext.getInstance().getBizDate());
		/*     */}
	/*     */
	/*     */private static List<UFDouble> setHeadTotalMoneyByMnyConst(
			BillCardPanel card, String[] MNY_CONST)
	/*     */{
		/* 109 */List headTotalMoneys = new ArrayList();
		/*     */
		/* 111 */for (String pl_mny_org : MNY_CONST) {
			/* 112 */headTotalMoneys.add((UFDouble) BillCardPanelUtils
					.getHeadItemValue(card, pl_mny_org));
			/*     */}
		/*     */
		/* 115 */return headTotalMoneys;
		/*     */}
	/*     */
	/*     */@Deprecated
	/*     */public static void setCostByCurrType(SuperVO[] bodyVOs,
			String pk_currtype_src, String pk_org, String productField,
			String[] multiplierfirFields, String multiFieldSet, UFDate date)
	/*     */throws BusinessException
	/*     */{
		/* 188 */if (ArrayUtils.isEmpty(bodyVOs)) {
			/* 189 */return;
			/*     */}
		/* 191 */UFDouble[] mount = new UFDouble[bodyVOs.length];
		/* 192 */for (int i = 0; i < bodyVOs.length; ++i) {
			/* 193 */mount[i] = ((UFDouble) bodyVOs[i]
					.getAttributeValue(productField));
			/*     */}
		/* 195 */UFDouble[] mountNew = null;
		/*     */try {
			/* 197 */mountNew = CurrencyRateManager.getOrgAmountsByOpp(pk_org,
					pk_currtype_src, mount, date);
			/*     */}
		/*     */catch (BusinessException e) {
			/* 200 */ExceptionUtils.asBusinessException(e);
			/*     */}
		/* 202 */for (int i = 0; i < bodyVOs.length; ++i)
		/*     */{
			/* 204 */bodyVOs[i].setAttributeValue(productField, mountNew[i]);
			/* 205 */UFDouble num = UFDouble.ONE_DBL;
			/* 206 */for (int j = 0; j < multiplierfirFields.length; ++j) {
				/* 207 */Object oValue = bodyVOs[i]
						.getAttributeValue(multiplierfirFields[j]);
				/* 208 */UFDouble num1 = UFDouble.ZERO_DBL;
				/* 209 */if (oValue != null) {
					/* 210 */num1 = new UFDouble(oValue.toString());
					/*     */}
				/* 212 */num = UFDoubleUtils.multiply(num1, num);
				/*     */}
			/*     */
			/* 215 */UFDouble multiSetter = UFDoubleUtils
					.div(mountNew[i], num);
			/* 216 */bodyVOs[i].setAttributeValue(multiFieldSet, multiSetter);
			/*     */}
		/*     */}
	/*     */
}