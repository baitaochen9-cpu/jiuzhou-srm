package nc.vo.ewm.workorder.utils;
/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */
/*     */import nc.vo.am.common.util.ArrayUtils;
/*     */
import nc.vo.am.common.util.ExceptionUtils;
/*     */
import nc.vo.am.common.util.UFDoubleUtils;
/*     */
import nc.vo.am.manager.CurrencyRateManager;
/*     */
import nc.vo.ewm.workorder.AggWorkOrderVO;
/*     */
import nc.vo.ewm.workorder.WOActualInvVO;
/*     */
import nc.vo.ewm.workorder.WOActualOtherExesVO;
/*     */
import nc.vo.ewm.workorder.WOActualPsnVO;
/*     */
import nc.vo.ewm.workorder.WOActualToolVO;
/*     */
import nc.vo.ewm.workorder.WOPlanInVVO;
/*     */
import nc.vo.ewm.workorder.WOPlanOtherExesVO;
/*     */
import nc.vo.ewm.workorder.WOPlanPsnVO;
/*     */
import nc.vo.ewm.workorder.WOPlanToolVO;
/*     */
import nc.vo.ewm.workorder.WorkOrderHeadVO;
/*     */
import nc.vo.pub.BusinessException;
/*     */
import nc.vo.pub.SuperVO;
/*     */
import nc.vo.pub.lang.UFDate;
/*     */
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;
/*     */
/*     */public class ComputeCostUtil
/*     */{
	/*     */public static void computeWorkOrderCost(AggWorkOrderVO billVO,
			AggWorkOrderVO oldBillVO, UFDate date)
	/*     */throws BusinessException
	/*     */{
		/*  35 */WorkOrderHeadVO headVO = billVO.getParentVO();
		/*  36 */Class[] bodyClass = {WOPlanInVVO.class, WOPlanPsnVO.class,
				WOPlanToolVO.class, WOPlanOtherExesVO.class,
				WOActualInvVO.class, WOActualPsnVO.class, WOActualToolVO.class,
				WOActualOtherExesVO.class};
		/*     */
		/*  39 */String[] bodyMoneyFields = {"money_org", "money", "money",
				"exesmoney", "money_org", "money", "money", "exesmoney"};
		/*     */
		/*  42 */String[] headMoneyFields = {"pl_mtr_mny_org",
				"pl_lbr_mny_org", "pl_tol_mny_org", "pl_oth_mny_org",
				"ac_mtr_mny_org", "ac_lbr_mny_org", "ac_tol_mny_org",
				"ac_oth_mny_org"};
		/*     */
		/*  45 */cleanOriginCost(headVO, headMoneyFields);
		/*  46 */for (int i = 0; i < bodyClass.length; ++i) {
			/*  47 */setTabCostToHead(headVO,
					(SuperVO[]) (SuperVO[]) billVO.getChildren(bodyClass[i]),
					bodyMoneyFields[i], headMoneyFields[i]);
			/*     */}
		/*     */
		/*  50 */setHeadTotalMoney(headVO, oldBillVO, date);
		UFDouble udef4 = UFDouble.ZERO_DBL;
		if(headVO.getDef4() != null){
			udef4 = new UFDouble(headVO.getDef4());
		}
		headVO.setAc_mtr_mny_org(SafeCompute.sub(headVO.getAc_mtr_mny_org(),udef4));
		/*     */}
	/*     */
	/*     */private static void cleanOriginCost(WorkOrderHeadVO headVO,
			String[] headMoneyFields) {
		/*  54 */for (String headMoneyField : headMoneyFields)
			/*  55 */headVO.setAttributeValue(headMoneyField,
					Integer.valueOf(0));
		/*     */}
	/*     */
	/*     */private static void setTabCostToHead(WorkOrderHeadVO headVO,
			SuperVO[] bodyVOs, String moneyKey, String headTotalMnyKey)
	/*     */{
		/*  69 */if (ArrayUtils.isEmpty(bodyVOs)) {
			/*  70 */return;
			/*     */}
		/*  72 */int rowCount = bodyVOs.length;
		/*  73 */UFDouble[] moneys = new UFDouble[rowCount];
		/*  74 */if (rowCount > 0) {
			/*  75 */boolean isPlanInv = false;
			/*  76 */if (bodyVOs[0] instanceof WOPlanInVVO) {
				/*  77 */isPlanInv = true;
				/*     */}
			/*  79 */for (int i = 0; i < rowCount; ++i)
			/*     */{
				/*  82 */UFDouble nmoney = null;
				/*  83 */if (bodyVOs[i].getStatus() == 3) {
					/*  84 */nmoney = UFDouble.ZERO_DBL;
					/*     */}
				/*  86 */else if ((isPlanInv)
						&& (bodyVOs[i].getAttributeValue("src_bill_type") != null))
					/*  87 */nmoney = UFDouble.ZERO_DBL;
				/*     */else {
					/*  89 */nmoney = (UFDouble) bodyVOs[i]
							.getAttributeValue(moneyKey);
					/*     */}
				/*     */
				/*  92 */moneys[i] = nmoney;
				/*     */}
			/*  94 */UFDouble totalMny = UFDoubleUtils.add(moneys);
			/*  95 */headVO.setAttributeValue(headTotalMnyKey, totalMny);
			/*     */}
		/*     */}
	/*     */
	/*     */public static void setCostByCurrType(WorkOrderHeadVO headVO,
			UFDate date, boolean isPlan)
	/*     */throws BusinessException
	/*     */{
		/* 108 */String[] fieldsPlanGroup = {"pl_lbr_mny_group",
				"pl_mtr_mny_group", "pl_oth_mny_group", "pl_tol_mny_group",
				"pl_ttl_mny_group"};
		/*     */
		/* 110 */String[] fieldsPlanGlobal = {"pl_lbr_mny_global",
				"pl_mtr_mny_global", "pl_oth_mny_global", "pl_tol_mny_global",
				"pl_ttl_mny_global"};
		/*     */
		/* 113 */String[] fieldsPlanOrg = {"pl_lbr_mny_org", "pl_mtr_mny_org",
				"pl_oth_mny_org", "pl_tol_mny_org", "pl_ttl_mny_org"};
		/*     */
		/* 116 */String[] fieldsActualGroup = {"ac_lbr_mny_group",
				"ac_mtr_mny_group", "ac_oth_mny_group", "ac_tol_mny_group",
				"ac_ttl_mny_group"};
		/*     */
		/* 118 */String[] fieldsActualGlobal = {"ac_lbr_mny_global",
				"ac_mtr_mny_global", "ac_oth_mny_global", "ac_tol_mny_global",
				"ac_ttl_mny_global"};
		/*     */
		/* 121 */String[] fieldsActualOrg = {"ac_lbr_mny_org",
				"ac_mtr_mny_org", "ac_oth_mny_org", "ac_tol_mny_org",
				"ac_ttl_mny_org"};
		/*     */
		/* 123 */String[] fieldsOrg = null;
		/* 124 */String[] fieldsGroup = null;
		/* 125 */String[] fieldsGlobal = null;
		/* 126 */if (isPlan) {
			/* 127 */fieldsOrg = fieldsPlanOrg;
			/* 128 */fieldsGroup = fieldsPlanGroup;
			/* 129 */fieldsGlobal = fieldsPlanGlobal;
			/*     */} else {
			/* 131 */fieldsOrg = fieldsActualOrg;
			/* 132 */fieldsGroup = fieldsActualGroup;
			/* 133 */fieldsGlobal = fieldsActualGlobal;
			/*     */}
		/* 135 */UFDouble[] headOrgMnys = new UFDouble[fieldsOrg.length];
		/* 136 */for (int i = 0; i < fieldsOrg.length; ++i)
			/* 137 */headOrgMnys[i] = ((UFDouble) headVO
					.getAttributeValue(fieldsOrg[i]));
		/*     */try
		/*     */{
			/* 140 */UFDouble[] groupMny = CurrencyRateManager
					.getGroupAmountsByOpp(headVO.getPk_group(),
							headVO.getPk_currtype(), headOrgMnys, date);
			/*     */
			/* 142 */UFDouble[] globeMny = CurrencyRateManager
					.getGlobalAmountsByOpp(headVO.getPk_currtype(),
							headOrgMnys, date);
			/* 143 */for (int i = 0; i < fieldsOrg.length; ++i) {
				/* 144 */headVO.setAttributeValue(fieldsGroup[i], groupMny[i]);
				/* 145 */headVO
						.setAttributeValue(fieldsGlobal[i], globeMny[i]);
				/*     */}
			/*     */} catch (BusinessException e) {
			/* 148 */ExceptionUtils.asBusinessException(e);
			/*     */}
		/*     */}
	/*     */
	/*     */private static void setHeadTotalMoney(WorkOrderHeadVO headVO,
			AggWorkOrderVO oldBillVO, UFDate date)
	/*     */throws BusinessException
	/*     */{
		/* 160 */int len = 4;
		/* 161 */UFDouble[] headPlanMoenys = new UFDouble[len];
		/* 162 */headPlanMoenys[0] = headVO.getPl_mtr_mny_org();
		/* 163 */headPlanMoenys[1] = headVO.getPl_lbr_mny_org();
		/* 164 */headPlanMoenys[2] = headVO.getPl_oth_mny_org();
		/* 165 */headPlanMoenys[3] = headVO.getPl_tol_mny_org();
		/*     */
		/* 167 */UFDouble[] headActualMoenys = new UFDouble[len];
		/* 168 */headActualMoenys[0] = headVO.getAc_mtr_mny_org();
		/* 169 */headActualMoenys[1] = headVO.getAc_lbr_mny_org();
		/* 170 */headActualMoenys[2] = headVO.getAc_oth_mny_org();
		/* 171 */headActualMoenys[3] = headVO.getAc_tol_mny_org();
		/*     */
		/* 173 */UFDouble pltotalMnyOrg_head = headVO.getPl_ttl_mny_org();
		/*     */
		/* 175 */UFDouble pltotalMnyOrg_old = null;
		/* 176 */UFDouble acTotalMnyOrg_old = null;
		/* 177 */if (oldBillVO != null) {
			/* 178 */WorkOrderHeadVO oldHeadVO = oldBillVO.getParentVO();
			/* 179 */pltotalMnyOrg_old = oldHeadVO.getPl_ttl_mny_org();
			/* 180 */acTotalMnyOrg_old = oldHeadVO.getAc_ttl_mny_org();
			/*     */}
		/*     */
		/* 183 */UFDouble pltotalMnyOrg_new = UFDoubleUtils
				.add(headPlanMoenys);
		/*     */
		/* 185 */if ((!(UFDoubleUtils.isEqual(pltotalMnyOrg_head,
				pltotalMnyOrg_new)))
				|| (!(UFDoubleUtils.isEqual(pltotalMnyOrg_old,
						pltotalMnyOrg_new))))
		/*     */{
			/* 187 */headVO.setPl_ttl_mny_org(pltotalMnyOrg_new);
			/* 188 */setCostByCurrType(headVO, date, true);
			/*     */}
		/* 190 */UFDouble acTotalMnyOrg_head = headVO.getAc_ttl_mny_org();
		/* 191 */UFDouble actotalMnyOrg_new = UFDoubleUtils
				.add(headActualMoenys);
		/* 192 */if ((UFDoubleUtils.isEqual(acTotalMnyOrg_head,
				actotalMnyOrg_new))
				&& (UFDoubleUtils.isEqual(acTotalMnyOrg_old, actotalMnyOrg_new)))
			/*     */return;
		/* 194 */headVO.setAc_ttl_mny_org(actotalMnyOrg_new);
		/* 195 */setCostByCurrType(headVO, date, false);
		/*     */}
	/*     */
	/*     */public static void stdJobWOCurrTypeChange(SuperVO[] bodyVOs,
			String pk_currtype_src, String pk_org, String productField,
			String[] multiplierfirFields, String multiFieldSet, UFDate date)
	/*     */throws BusinessException
	/*     */{
		/* 218 */if (ArrayUtils.isEmpty(bodyVOs)) {
			/* 219 */return;
			/*     */}
		/* 221 */UFDouble[] mount = new UFDouble[bodyVOs.length];
		/* 222 */for (int i = 0; i < bodyVOs.length; ++i) {
			/* 223 */mount[i] = ((UFDouble) bodyVOs[i]
					.getAttributeValue(productField));
			/* 224 */if (mount[i] == null)
				/* 225 */mount[i] = UFDouble.ZERO_DBL;
			/*     */}
		/* 227 */UFDouble[] mountNew = null;
		/*     */try {
			/* 229 */mountNew = CurrencyRateManager.getOrgAmountsByOpp(pk_org,
					pk_currtype_src, mount, date);
			/*     */}
		/*     */catch (BusinessException e) {
			/* 232 */ExceptionUtils.asBusinessException(e);
			/*     */}
		/* 234 */for (int i = 0; i < bodyVOs.length; ++i)
		/*     */{
			/* 236 */bodyVOs[i].setAttributeValue(productField, mountNew[i]);
			/* 237 */UFDouble num = UFDouble.ONE_DBL;
			/* 238 */for (int j = 0; j < multiplierfirFields.length; ++j) {
				/* 239 */Object oValue = bodyVOs[i]
						.getAttributeValue(multiplierfirFields[j]);
				/* 240 */UFDouble num1 = UFDouble.ZERO_DBL;
				/* 241 */if (oValue != null) {
					/* 242 */num1 = new UFDouble(oValue.toString());
					/*     */}
				/* 244 */num = UFDoubleUtils.multiply(num1, num);
				/*     */}
			/*     */
			/* 247 */UFDouble multiSetter = UFDoubleUtils
					.div(mountNew[i], num);
			/* 248 */bodyVOs[i].setAttributeValue(multiFieldSet, multiSetter);
			/*     */}
		/*     */}
	/*     */
}