package nccloud.dto.ct.saledaily.utils;

import nc.impl.pubapp.env.BSContext;
import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.bd.currency.CurrencyRate;
import nc.itf.scmpub.reference.uap.para.SysParaInitQuery;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.scmf.pub.keyvalue.IKeyValue;
import nc.vo.scmf.pub.keyvalue.VOKeyValue;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.scmpub.res.para.NCPara;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;

/**
 * @description 汇率改变编辑后事件
 * @author wangshrc
 * @date 2019年2月13日 下午4:11:16
 * @version ncc1.0
 */
public class ExchangeRateUtil {

	public static void calculateBodyRows(AbstractBill bill, String oldRate) {
		IKeyValue keyValue = new VOKeyValue<AbstractBill>(bill);
		String newRate = keyValue
				.getHeadStringValue(CtAbstractVO.NEXCHANGERATE);
		if (newRate != null && !newRate.equals(oldRate)) {
			int[] rows = new int[keyValue.getBodyCount()];
			for (int i = 0; i < rows.length; i++) {
				rows[i] = i;
			}
			ExchangeRateUtil.calculateBodyRows(rows, bill);
		}
	}

	public static void calculateBodyRows(int[] rows, AbstractBill bill) {
		if (rows == null) {
			return;
		}
		ExtBillUtil util = new ExtBillUtil(bill);
		// 汇率变化后重新计算行
		new RelationCalculate().calculate(util, rows,
				CtAbstractVO.NEXCHANGERATE);
	}

	/**
	 * 方法功能描述： 1 改变折本汇率 2 改变集团本币汇率（基于参数） 3 改变全局本币汇率（基于参数） 4 改变汇率后的处理
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param util
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-4-5 下午01:31:38
	 */
	public static void changeBuyExchangeRate(AbstractBill bill) {
		IKeyValue keyValue = new VOKeyValue<AbstractBill>(bill);
		String oldExchangeRate = keyValue
				.getHeadStringValue(CtAbstractVO.NEXCHANGERATE);
		// 1.设置表头 折本汇率
		ExchangeRateUtil.setBuyExchangerate(keyValue);
		// 2 设置 集团本币汇率 加联动计算
		ExchangeRateUtil.setGroupBuyExChangerate(keyValue);
		// 3 设置 全局本币汇率 加联动计算
		ExchangeRateUtil.setGlobalBuyExChangerate(keyValue);
		// 4 汇率变化后处理
		ExchangeRateUtil.calculateBodyRows(bill, oldExchangeRate);
	}

	/**
	 * 方法功能描述： 1 改变折本汇率 2 改变集团本币汇率（基于参数） 3 改变全局本币汇率（基于参数） 4 改变汇率后的处理
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param util
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-4-5 下午01:31:38
	 */
	public static void changeSellExchangeRate(AbstractBill bill) {
		IKeyValue keyValue = new VOKeyValue<AbstractBill>(bill);
		String oldExchangeRate = keyValue
				.getHeadStringValue(CtAbstractVO.NEXCHANGERATE);
		// 1.设置表头 折本汇率
		ExchangeRateUtil.setSellExchangerate(keyValue);
		// 2 设置 集团本币汇率 加联动计算
		ExchangeRateUtil.setGroupSellExChangerate(keyValue);
		// 3 设置 全局本币汇率 加联动计算
		ExchangeRateUtil.setGlobalSellExChangerate(keyValue);
		// 4 汇率变化后处理
		ExchangeRateUtil.calculateBodyRows(bill, oldExchangeRate);
	}

	/**
	 * 方法功能描述：设置表头汇率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param util
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-3-31 下午04:44:24
	 */
	public static void setBuyExchangerate(IKeyValue keyValue) {
		String origcurrency = keyValue
				.getHeadStringValue(CtAbstractVO.CORIGCURRENCYID);
		String currency = keyValue.getHeadStringValue(CtAbstractVO.CCURRENCYID);
		UFDate billDate = keyValue
				.getHeadUFDateValue(CtAbstractVO.SUBSCRIBEDATE);

		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency)
				|| StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}

		String pk_org = keyValue.getHeadStringValue(CtAbstractVO.PK_ORG);
		UFDouble exchangeRate = null;
		exchangeRate = CurrencyRate.getCurrencyBuyRateByOrg(pk_org,
				origcurrency, currency, billDate);
		if (exchangeRate == null) {
			return;
		}
		keyValue.setHeadValue(CtAbstractVO.NEXCHANGERATE, exchangeRate);

	}

	/**
	 * 方法功能描述：设置默认集团和全局汇率
	 * <p>
	 * 使用场景：组织编辑后设置
	 * <ul>
	 * <li>
	 * </ul>
	 * <b>参数说明</b>
	 * 
	 * @param util
	 *            <p>
	 * @since 6.1
	 * @author gaogr
	 * @time 2012-3-20 上午11:02:38
	 */
	public static void setDefaultGroupGlobalRate(IKeyValue keyValue) {
		String billtype = keyValue
				.getHeadStringValue(CtAbstractVO.CBILLTYPECODE);

		if (CTBillType.OtherPur.getCode().equals(billtype)
				|| CTBillType.PurDaily.getCode().equals(billtype)) {
			// 设置 集团本币汇率
			ExchangeRateUtil.setGroupSellExChangerate(keyValue);
			// 设置 全局本币汇率
			ExchangeRateUtil.setGlobalSellExChangerate(keyValue);
		}

		if (CTBillType.OtherSale.getCode().equals(billtype)
				|| CTBillType.SaleDaily.getCode().equals(billtype)) {
			// 设置 集团本币汇率
			ExchangeRateUtil.setGroupBuyExChangerate(keyValue);
			// 设置 全局本币汇率
			ExchangeRateUtil.setGlobalBuyExChangerate(keyValue);
		}
	}

	public static void setSellExchangerate(IKeyValue keyValue) {
		String origcurrency = keyValue
				.getHeadStringValue(CtAbstractVO.CORIGCURRENCYID);
		String currency = keyValue.getHeadStringValue(CtAbstractVO.CCURRENCYID);
		UFDate billDate = keyValue
				.getHeadUFDateValue(CtAbstractVO.SUBSCRIBEDATE);

		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency)
				|| StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}

		String pk_org = keyValue.getHeadStringValue(CtAbstractVO.PK_ORG);
		String pk_exratescheme = CurrencyRateUtilHelper.getInstance()
				.getExrateschemeByOrgID(pk_org);
		UFDouble exchangeRate = null;
		exchangeRate = CurrencyRate.getCurrencySellRate(pk_exratescheme,
				origcurrency, currency, billDate);
		if (exchangeRate == null) {
			return;
		}
		// 暂时将精度设为8，周一讨论 yinyxa 2011-06-11
		keyValue.setHeadValue(CtAbstractVO.NEXCHANGERATE, exchangeRate);
	}

	/**
	 * 方法功能描述：3 设置 全局本币汇率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param util
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-7-22 下午07:43:50
	 */
	private static void setGlobalBuyExChangerate(IKeyValue keyValue) {
		// 原币
		String origcurrency = keyValue
				.getHeadStringValue(CtAbstractVO.CORIGCURRENCYID);
		// 本币
		String currency = keyValue.getHeadStringValue(CtAbstractVO.CCURRENCYID);
		// 日期
		UFDate billDate = keyValue
				.getHeadUFDateValue(CtAbstractVO.SUBSCRIBEDATE);
		// 集团本币汇率
		UFDouble globalChangeRate = null;
		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency)
				|| StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}

		// 如果不是 "基于原币计算" "基于组织本位币计算" 那么就是不启用，
		// groupChangeRate = UFDouble.ZERO_DBL
		String nc002 = SysParaInitQuery.getParaString(IOrgConst.GLOBEORG,
				"NC002");
		if (NCPara.NC002_CALCULATEBYORIGCURRTYPE.getName().equals(nc002)) {
			globalChangeRate = CurrencyRate.getGlobalLocalCurrencyBuyRate(
					origcurrency, billDate);
		} else if (NCPara.NC002_CALCULATEBYCURRTYPE.getName().equals(nc002)) {
			globalChangeRate = CurrencyRate.getGlobalLocalCurrencyBuyRate(
					currency, billDate);
		}
		if (globalChangeRate == null) {
			return;
		}
		keyValue.setHeadValue(CtAbstractVO.NGLOBALEXCHGRATE, globalChangeRate);
	}

	/**
	 * 方法功能描述：3 设置 全局本币汇率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param util
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-7-22 下午07:43:50
	 */
	private static void setGlobalSellExChangerate(IKeyValue keyValue) {
		// 原币
		String origcurrency = keyValue
				.getHeadStringValue(CtAbstractVO.CORIGCURRENCYID);
		// 本币
		String currency = keyValue.getHeadStringValue(CtAbstractVO.CCURRENCYID);
		// 日期
		UFDate billDate = keyValue
				.getHeadUFDateValue(CtAbstractVO.SUBSCRIBEDATE);
		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency)
				|| StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}
		// 集团本币汇率
		UFDouble globalChangeRate = null;
		// 如果不是 "基于原币计算" "基于组织本位币计算" 那么就是不启用，
		// groupChangeRate = UFDouble.ZERO_DBL
		String nc002 = SysParaInitQuery.getParaString(IOrgConst.GLOBEORG,
				"NC002");
		if (NCPara.NC002_CALCULATEBYORIGCURRTYPE.getName().equals(nc002)) {
			globalChangeRate = CurrencyRate.getGlobalLocalCurrencySellRate(
					origcurrency, billDate);
		} else if (NCPara.NC002_CALCULATEBYCURRTYPE.getName().equals(nc002)) {
			globalChangeRate = CurrencyRate.getGlobalLocalCurrencySellRate(
					currency, billDate);
		}
		if (globalChangeRate == null) {
			return;
		}
		keyValue.setHeadValue(CtAbstractVO.NGLOBALEXCHGRATE, globalChangeRate);
	}

	/**
	 * 方法功能描述：设置集团本币汇率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param util
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-7-22 下午07:33:27
	 */
	private static void setGroupBuyExChangerate(IKeyValue keyValue) {

		// 原币
		String origcurrency = keyValue
				.getHeadStringValue(CtAbstractVO.CORIGCURRENCYID);
		// 本币
		String currency = keyValue.getHeadStringValue(CtAbstractVO.CCURRENCYID);
		// 日期
		UFDate billDate = keyValue
				.getHeadUFDateValue(CtAbstractVO.SUBSCRIBEDATE);

		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency)
				|| StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}
		// 集团本币汇率
		UFDouble groupChangeRate = null;
		BSContext ctx = new BSContext();
		// 如果不是 "基于原币计算" "基于组织本位币计算" 那么就是不启用，
		// groupChangeRate = UFDouble.ZERO_DBL
		String nc001 = SysParaInitQuery
				.getParaString(ctx.getGroupID(), "NC001");
		if (NCPara.NC001_CALCULATEBYORIGCURRTYPE.getName().equals(nc001)) {
			groupChangeRate = CurrencyRate.getGroupLocalCurrencyBuyRate(
					origcurrency, billDate);
		} else if (NCPara.NC001_CALCULATEBYCURRTYPE.getName().equals(nc001)) {
			groupChangeRate = CurrencyRate.getGroupLocalCurrencyBuyRate(
					currency, billDate);
		}
		if (groupChangeRate == null) {
			return;
		}
		keyValue.setHeadValue(CtAbstractVO.NGROUPEXCHGRATE, groupChangeRate);
	}

	/**
	 * 方法功能描述：设置集团本币汇率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param util
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-7-22 下午07:33:27
	 */
	private static void setGroupSellExChangerate(IKeyValue keyValue) {
		// 原币
		String origcurrency = keyValue
				.getHeadStringValue(CtAbstractVO.CORIGCURRENCYID);
		// 本币
		String currency = keyValue.getHeadStringValue(CtAbstractVO.CCURRENCYID);
		// 日期
		UFDate billDate = keyValue
				.getHeadUFDateValue(CtAbstractVO.SUBSCRIBEDATE);
		// 没有币种或者日期不再后续处理
		if (StringUtil.isEmptyTrimSpace(origcurrency)
				|| StringUtil.isEmptyTrimSpace(currency) || billDate == null) {
			return;
		}
		// 集团本币汇率
		UFDouble groupChangeRate = null;
		BSContext ctx = new BSContext();
		// 如果不是 "基于原币折算" "基于组织本位币计算" 那么就是不启用，
		// groupChangeRate = UFDouble.ZERO_DBL
		String nc001 = SysParaInitQuery
				.getParaString(ctx.getGroupID(), "NC001");
		if (NCPara.NC001_CALCULATEBYORIGCURRTYPE.getName().equals(nc001)) {
			groupChangeRate = CurrencyRate.getGroupLocalCurrencySellRate(
					origcurrency, billDate);
		} else if (NCPara.NC001_CALCULATEBYCURRTYPE.getName().equals(nc001)) {
			groupChangeRate = CurrencyRate.getGroupLocalCurrencySellRate(
					currency, billDate);
		}
		if (groupChangeRate == null) {
			return;
		}
		keyValue.setHeadValue(CtAbstractVO.NGROUPEXCHGRATE, groupChangeRate);
	}

}
