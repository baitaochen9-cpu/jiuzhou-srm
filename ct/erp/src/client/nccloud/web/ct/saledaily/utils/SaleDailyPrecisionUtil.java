package nccloud.web.ct.saledaily.utils;

import nc.vo.ct.ap.entity.CtApBVO;
import nc.vo.ct.ap.entity.CtApVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractExpVO;
import nc.vo.ct.entity.CtAbstractPayTermVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nccloud.framework.web.convert.precision.PositionType;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.scmpub.pub.utils.scale.ExtBillcardTotalValueScaleProcessor;
import nccloud.web.scmpub.pub.utils.scale.GridTotalValueScaleProcessor;
import nccloud.web.scmpub.pub.utils.scale.SCMExtBillPrecisionOperator;
import nccloud.web.scmpub.pub.utils.scale.SCMGridPrecisionOperator;

/**
 * @description 销售合同精度处理
 * @author wangshrc
 * @date 2019年1月15日 下午1:31:28
 * @version ncc1.0
 */
public class SaleDailyPrecisionUtil {
	public static void dealPrecision(Grid grid) {
		if (grid != null) {
			SCMGridPrecisionOperator operator = new SCMGridPrecisionOperator(grid);
			operator.addCurrencyMnyPrecision(headorgmnykeys, CtSaleVO.CORIGCURRENCYID);
			operator.addCurrencyMnyPrecision(headmnykeys, CtSaleVO.CCURRENCYID);
			operator.addExchangeRatePrecision(headExchangeRate, CtSaleVO.PK_ORG, CtSaleVO.CORIGCURRENCYID,
					CtSaleVO.CCURRENCYID);
			operator.addGroupExchgRatePrecision(headGroupExchangeRate, CtSaleVO.CORIGCURRENCYID, CtSaleVO.CCURRENCYID);
			operator.addGlobalExchgRatePrecision(headGlobalExchangeRate, CtSaleVO.CORIGCURRENCYID,
					CtSaleVO.CCURRENCYID);
			operator.processPrecision();
			GridTotalValueScaleProcessor process = new GridTotalValueScaleProcessor(grid);
			process.setHeadTailKeys(NUMHEAD);
		}

	}

	public static void dealPrecision(ExtBillCard card) {
		if (card != null) {
			SCMExtBillPrecisionOperator operator = new SCMExtBillPrecisionOperator(card);
			operator.addCurrencyMnyPrecision(headorgmnykeys, PositionType.Head, CtSaleVO.CORIGCURRENCYID,
					PositionType.Head, FORM_AREAID);
			operator.addCurrencyMnyPrecision(headmnykeys, PositionType.Head, CtSaleVO.CCURRENCYID, PositionType.Head,
					FORM_AREAID);
			operator.addorgExchangeRatePrecision(headExchangeRate, PositionType.Head, CtSaleVO.PK_ORG,
					PositionType.Head, CtSaleVO.CORIGCURRENCYID, PositionType.Head, CtSaleVO.CCURRENCYID,
					PositionType.Head, FORM_AREAID);
			operator.addGroupExchgRatePrecision(headGroupExchangeRate, PositionType.Head, CtSaleVO.CORIGCURRENCYID,
					PositionType.Head, CtSaleVO.CCURRENCYID, PositionType.Head, FORM_AREAID);
			operator.addGlobalExchgRatePrecision(headGlobalExchangeRate, PositionType.Head, CtSaleVO.CORIGCURRENCYID,
					PositionType.Head, CtSaleVO.CCURRENCYID, PositionType.Head, FORM_AREAID);
			operator.addGlobalStdMnyPrecision(globalmnykeys, PositionType.Body, BASE_AREAID);
			operator.addGroupStdMnyPrecision(groupmnykeys, PositionType.Body, BASE_AREAID);
			operator.addChangeRatePrecision(changeRates, PositionType.Body, BASE_AREAID);
			operator.addNumPrecision(quoteNumkeys, PositionType.Body, CtAbstractBVO.CQTUNITID, PositionType.Body,
					BASE_AREAID);
			operator.addNumPrecision(assistNumkeys, PositionType.Body, CtAbstractBVO.CASTUNITID, PositionType.Body,
					BASE_AREAID);
			operator.addNumPrecision(numkeys, PositionType.Body, CtAbstractBVO.CUNITID, PositionType.Body, BASE_AREAID);
			operator.addCurrencyPricePrecision(origpricekeys, PositionType.Body, CtAbstractVO.CORIGCURRENCYID,
					PositionType.Head, BASE_AREAID);
			operator.addCurrencyPricePrecision(pricekeys, PositionType.Body, CtAbstractVO.CCURRENCYID,
					PositionType.Head, BASE_AREAID);
			operator.addCurrencyMnyPrecision(mnykeys, PositionType.Body, CtAbstractVO.CCURRENCYID, PositionType.Head,
					BASE_AREAID);
			operator.addCurrencyMnyPrecision(orgmnykeys, PositionType.Body, CtAbstractVO.CORIGCURRENCYID,
					PositionType.Head, BASE_AREAID);
			operator.addTaxratePrecision(taxRatekeys, PositionType.Body, BASE_AREAID);
			operator.addCurrencyMnyPrecision(paytermmny, PositionType.Body, CtSaleVO.CORIGCURRENCYID, PositionType.Head,
					PAYTERM_AREAID);
			operator.addCurrencyMnyPrecision(ngroupplanrecvmny, PositionType.Body, CtSaleVO.CCURRENCYID,
					PositionType.Head, PAYTERM_AREAID);
			operator.addCurrencyMnyPrecision(nglobalpanrecymny, PositionType.Body, CtSaleVO.CCURRENCYID,
					PositionType.Head, PAYTERM_AREAID);
			operator.addCurrencyMnyPrecision(nlocalplanmny, PositionType.Body, CtSaleVO.CCURRENCYID, PositionType.Head,
					PAYTERM_AREAID);
			operator.addCurrencyMnyPrecision(exporgmnykeys, PositionType.Body, CtSaleVO.CORIGCURRENCYID,
					PositionType.Head, EXP_AREAID);
			operator.processPrecision();
			ExtBillcardTotalValueScaleProcessor processor = new ExtBillcardTotalValueScaleProcessor(card);
			processor.setHeadTailKeys(NUMHEAD);
		}
	}

	public static void dealHeadPrecision(ExtBillCard card) {
		if(card!=null) {			
			SCMExtBillPrecisionOperator operator = new SCMExtBillPrecisionOperator(card);
			operator.addCurrencyMnyPrecision(headorgmnykeys, PositionType.Head, CtSaleVO.CORIGCURRENCYID, PositionType.Head,
					FORM_AREAID);
			operator.addCurrencyMnyPrecision(headmnykeys, PositionType.Head, CtSaleVO.CCURRENCYID, PositionType.Head,
					FORM_AREAID);
			operator.addorgExchangeRatePrecision(headExchangeRate, PositionType.Head, CtSaleVO.PK_ORG, PositionType.Head,
					CtSaleVO.CORIGCURRENCYID, PositionType.Head, CtSaleVO.CCURRENCYID, PositionType.Head, FORM_AREAID);
			operator.addGroupExchgRatePrecision(headGroupExchangeRate, PositionType.Head, CtSaleVO.CORIGCURRENCYID,
					PositionType.Head, CtSaleVO.CCURRENCYID, PositionType.Head, FORM_AREAID);
			operator.addGlobalExchgRatePrecision(headGlobalExchangeRate, PositionType.Head, CtSaleVO.CORIGCURRENCYID,
					PositionType.Head, CtSaleVO.CCURRENCYID, PositionType.Head, FORM_AREAID);
			operator.processPrecision();
			ExtBillcardTotalValueScaleProcessor processor = new ExtBillcardTotalValueScaleProcessor(card);
			processor.setHeadTailKeys(NUMHEAD);
		}
	}

	// 折本汇率
	private static String[] headExchangeRate = new String[] { CtSaleVO.NEXCHANGERATE };
	// 集团本币汇率
	private static String[] headGroupExchangeRate = new String[] { CtSaleVO.NGROUPEXCHGRATE };
	// 全局汇率
	private static String[] headGlobalExchangeRate = new String[] { CtSaleVO.NGLOBALEXCHGRATE };
	// 合计字段
	private static String[] NUMHEAD = new String[] { CtSaleVO.NTOTALASTNUM };
	private static String[] globalmnykeys = new String[] { CtAbstractBVO.NGLOBALMNY, CtAbstractBVO.NGLOBALTAXMNY };
	// 集团本位币金额
	private static String[] groupmnykeys = new String[] { CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY };
	// 换算率
	private static String[] changeRates = new String[] { CtAbstractBVO.VCHANGERATE, CtAbstractBVO.VQTUNITRATE };
	// 原币金额
	private static String[] orgmnykeys = new String[] { CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGTAXMNY,
			CtAbstractBVO.NORITOTALGPMNY, CtAbstractBVO.NORDSUM, CtApBVO.NORICOPEGPMNY };
	private static String[] exporgmnykeys = new String[] { CtAbstractExpVO.VEXPSUM };

	// 表头原币金额
	private static String[] headorgmnykeys = new String[] { CtAbstractVO.NORIPREPAYLIMITMNY,
			CtAbstractVO.NORIGPSHAMOUNT, CtApVO.NORIGCOPAMOUNT, CtApVO.NORIPREPAYMNY, CtAbstractVO.NTOTALORIGMNY };
	// 表头本币金额
	private static String[] headmnykeys = new String[] { CtAbstractVO.NPREPAYLIMITMNY, CtAbstractVO.NTOTALGPAMOUNT,
			CtAbstractVO.NTOTALTAXMNY, CtApVO.NTOTALCOPAMOUNT, CtApVO.NPREPAYMNY };
	// 本币金额
	private static String[] mnykeys = new String[] { CtAbstractBVO.NMNY, CtAbstractBVO.NTAXMNY, CtAbstractBVO.NTAX,
			CtAbstractBVO.NTOTALGPMNY, CtApBVO.NCOPEGPMNY, CTVatNameConst.NNOSUBTAX, CTVatNameConst.NCALTAXMNY,
			CTVatNameConst.NCALCOSTMNY };
	// 报价单位数量
	private static String[] quoteNumkeys = new String[] { CtAbstractBVO.NQTUNITNUM };
	// 业务单位数量
	private static String[] assistNumkeys = new String[] { CtAbstractBVO.NASTNUM };
	// 主数量
	private static String[] numkeys = new String[] { CtAbstractBVO.NNUM, CtAbstractBVO.NORDNUM, CtPuBVO.NSCHEDULERNUM };
	// 价格
	// 原币
	private static String[] origpricekeys = new String[] { CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE,
			CtAbstractBVO.NORIGPRICE, CtAbstractBVO.NORIGTAXPRICE, };
	// 本币
	private static String[] pricekeys = new String[] { CtAbstractBVO.NQTPRICE, CtAbstractBVO.NQTTAXPRICE,
			CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NGPRICE };
	// 税率
	private static String[] taxRatekeys = new String[] { CtAbstractBVO.NTAXRATE, CTVatNameConst.NNOSUBTAXRATE };

	/**
	 * 销售收款协议特有字段
	 */
	// 原币计划收款金额
	private static String[] paytermmny = new String[] { CtAbstractPayTermVO.NPLANRECMNY, CtAbstractPayTermVO.NCTRECVMNY,
			CtAbstractPayTermVO.NREALRECYMNY };
	// 本币计划收款金额
	private static String[] nlocalplanmny = new String[] { CtAbstractPayTermVO.NLOCALPLANMNY };
	// 集团本币计划收款金额
	private static String[] ngroupplanrecvmny = new String[] { CtAbstractPayTermVO.NGROUPPLANRECVMNY };
	// 全局本币收款金额
	private static String[] nglobalpanrecymny = new String[] { CtAbstractPayTermVO.NGLOBALPANRECYMNY };

	private static String FORM_AREAID = "head";
	private static String BASE_AREAID = "saledaily_base";
	// private static String TERM_AREAID = "saledaily_term";
	// private static String EXEC_AREAID = "saledaily_exec";
	private static String EXP_AREAID = "saledaily_exp";
	// private static String MEMORA_AREAID = "saledaily_memora";
	// private static String CHANGE_AREAID = "saledaily_change";
	private static String PAYTERM_AREAID = "saledaily_payterm";
}
