package nccloud.dto.ct.pub.utils;

import nc.impl.pubapp.env.BSContext;
import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.bd.currency.CurrencyRate;
import nc.itf.scmpub.reference.uap.para.SysParaInitQuery;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.scmpub.res.para.NCPara;

/**
 * @description 汇率改变编辑后事件
 * @author xiahui
 * @date 创建时间：2019-1-28 上午9:02:20
 * @version ncc1.0
 **/
public class ExchangeRateUtil {

	/**
	 * 设置默认集团和全局汇率
	 * 
	 * @param billvo
	 */
	public static void setDefaultGroupGlobalRate(ExtBillUtil util) {
		String billtype = util.getHeadTailStringValue(CtAbstractVO.CBILLTYPECODE);

		if (CTBillType.OtherPur.getCode().equals(billtype) || CTBillType.PurDaily.getCode().equals(billtype)) {
			// 设置 集团本币汇率
			ExchangeRateUtil.setGroupSellExChangerate(util);
			// 设置 全局本币汇率
			ExchangeRateUtil.setGlobalSellExChangerate(util);
		}

		// if (CTBillType.OtherSale.getCode().equals(billtype)
		// || CTBillType.SaleDaily.getCode().equals(billtype)) {
		// // 设置 集团本币汇率
		// ExchangeRateUtil.setGroupBuyExChangerate(util);
		// // 设置 全局本币汇率
		// ExchangeRateUtil.setGlobalBuyExChangerate(util);
		// }
	}

	/**
	 * 1 改变折本汇率 2 改变集团本币汇率（基于参数） 3 改变全局本币汇率（基于参数） 4 改变汇率后的处理
	 * 
	 * @param util
	 */
	public static void changeSellExchangeRate(ExtBillUtil util) {
		String oldExchangeRate = util.getHeadTailStringValue(CtAbstractVO.NEXCHANGERATE);
		// 1.设置表头 折本汇率
		ExchangeRateUtil.setSellExchangerate(util);
		// 2 设置 集团本币汇率 加联动计算
		ExchangeRateUtil.setGroupSellExChangerate(util);
		// 3 设置 全局本币汇率 加联动计算
		ExchangeRateUtil.setGlobalSellExChangerate(util);
		// 4 汇率变化后处理
		ExchangeRateUtil.calculateBodyRows(util, oldExchangeRate);
	}

	public static void calculateBodyRows(ExtBillUtil util, String oldRate) {
		String newRate = util.getHeadTailStringValue(CtAbstractVO.NEXCHANGERATE);
		if (newRate != null && !newRate.equals(oldRate)) {
			int[] rows = util.getRows(util.getBodyRowCount());
			ExchangeRateUtil.calculateBodyRows(rows, util);
		}
	}

	public static void calculateBodyRows(int[] rows, ExtBillUtil util) {
		if (rows == null) {
			return;
		}
		// 汇率变化后重新计算行
		new RelationCalculate().calculate(util, rows, CtAbstractVO.NEXCHANGERATE);
	}

	public static void setSellExchangerate(ExtBillUtil util) {
		String origcurrency = util.getHeadTailStringValue(CtAbstractVO.CORIGCURRENCYID);
		String currency = util.getHeadTailStringValue(CtAbstractVO.CCURRENCYID);
		UFDate billDate = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);

		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency) || StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}

		String pk_org = util.getHeadTailStringValue(CtAbstractVO.PK_ORG);
		String pk_exratescheme = CurrencyRateUtilHelper.getInstance().getExrateschemeByOrgID(pk_org);
		UFDouble exchangeRate = null;
		exchangeRate = CurrencyRate.getCurrencySellRate(pk_exratescheme, origcurrency, currency, billDate);
		if (exchangeRate == null) {
			return;
		}
		// 暂时将精度设为8，周一讨论 yinyxa 2011-06-11
		util.setBodyValue(0, CtAbstractVO.NEXCHANGERATE, exchangeRate);
	}

	/**
	 * 设置集团本币汇率
	 * 
	 * @param util
	 */
	private static void setGroupSellExChangerate(ExtBillUtil util) {
		// 原币
		String origcurrency = util.getHeadTailStringValue(CtAbstractVO.CORIGCURRENCYID);
		// 本币
		String currency = util.getHeadTailStringValue(CtAbstractVO.CCURRENCYID);
		// 日期
		UFDate billDate = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);
		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency) || StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}
		// 集团本币汇率
		UFDouble groupChangeRate = null;
		BSContext ctx = new BSContext();
		// 如果不是 "基于原币折算" "基于组织本位币计算" 那么就是不启用，
		// groupChangeRate = UFDouble.ZERO_DBL
		String nc001 = SysParaInitQuery.getParaString(ctx.getGroupID(), "NC001");
		if (NCPara.NC001_CALCULATEBYORIGCURRTYPE.getName().equals(nc001)) {
			groupChangeRate = CurrencyRate.getGroupLocalCurrencySellRate(origcurrency, billDate);
		} else if (NCPara.NC001_CALCULATEBYCURRTYPE.getName().equals(nc001)) {
			groupChangeRate = CurrencyRate.getGroupLocalCurrencySellRate(currency, billDate);
		}
		if (groupChangeRate == null) {
			return;
		}
		util.setHeadValue(CtAbstractVO.NGROUPEXCHGRATE, groupChangeRate);
	}

	/**
	 * 设置 全局本币汇率
	 * 
	 * @param util
	 */
	private static void setGlobalSellExChangerate(ExtBillUtil util) {
		// 原币
		String origcurrency = util.getHeadTailStringValue(CtAbstractVO.CORIGCURRENCYID);
		// 本币
		String currency = util.getHeadTailStringValue(CtAbstractVO.CCURRENCYID);
		// 日期
		UFDate billDate = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);
		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency) || StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}
		// 集团本币汇率
		UFDouble globalChangeRate = null;
		// 如果不是 "基于原币计算" "基于组织本位币计算" 那么就是不启用，
		// groupChangeRate = UFDouble.ZERO_DBL
		String nc002 = SysParaInitQuery.getParaString(IOrgConst.GLOBEORG, "NC002");
		if (NCPara.NC002_CALCULATEBYORIGCURRTYPE.getName().equals(nc002)) {
			globalChangeRate = CurrencyRate.getGlobalLocalCurrencySellRate(origcurrency, billDate);
		} else if (NCPara.NC002_CALCULATEBYCURRTYPE.getName().equals(nc002)) {
			globalChangeRate = CurrencyRate.getGlobalLocalCurrencySellRate(currency, billDate);
		}
		if (globalChangeRate == null) {
			return;
		}
		util.setHeadValue(CtAbstractVO.NGLOBALEXCHGRATE, globalChangeRate);
	}
}
