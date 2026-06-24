package nccloud.web.ct.purdaily.utils;

import nc.vo.ct.ap.entity.CtApBVO;
import nc.vo.ct.ap.entity.CtApVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractExpVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nccloud.framework.web.convert.precision.PositionType;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.scmpub.pub.utils.scale.ExtBillcardTotalValueScaleProcessor;
import nccloud.web.scmpub.pub.utils.scale.GridTotalValueScaleProcessor;
import nccloud.web.scmpub.pub.utils.scale.SCMExtBillPrecisionOperator;
import nccloud.web.scmpub.pub.utils.scale.SCMGridPrecisionOperator;

/**
 * @description 采购合同维护，精度处理
 * @author xiahui
 * @date 创建时间：2019-3-7 上午9:10:24
 * @version ncc1.0
 **/
public class PrecisionUtil {

	/**
	 * 列表精度处理
	 * 
	 * @param grid
	 */
	public static void setGridPrecision(Grid grid) {
		SCMGridPrecisionOperator scale = new SCMGridPrecisionOperator(grid);
		GridTotalValueScaleProcessor totalscale = new GridTotalValueScaleProcessor(grid);

		// 表头本币金额精度
		scale.addCurrencyMnyPrecision(headmnykeys, CtAbstractVO.CCURRENCYID);
		// 表头原币金额精度
		scale.addCurrencyMnyPrecision(headorgmnykeys, CtAbstractVO.CORIGCURRENCYID);
		// 折本汇率精度
		scale.addExchangeRatePrecision(exchangeratekeys, CtAbstractVO.PK_ORG, CtAbstractVO.CORIGCURRENCYID,
				CtAbstractVO.CCURRENCYID);
		// 集团本币汇率精度
		scale.addGroupExchgRatePrecision(groupratekeys, CtAbstractVO.CORIGCURRENCYID, CtAbstractVO.CCURRENCYID);
		// 全局本币汇率精度
		scale.addGlobalExchgRatePrecision(globalratekeys, CtAbstractVO.CORIGCURRENCYID, CtAbstractVO.CCURRENCYID);
		// 合计信息精度控制器(整单数量整单金额)
		totalscale.setHeadTailKeys(totalkeys);

		scale.processPrecision();
	}

	/**
	 * 设置卡片精度
	 * 
	 * @param card
	 */
	public static void setExtCardPrecision(ExtBillCard card) {
		SCMExtBillPrecisionOperator scale = new SCMExtBillPrecisionOperator(card);
		ExtBillcardTotalValueScaleProcessor totalscale = new ExtBillcardTotalValueScaleProcessor(card);

		// 表头本币金额精度
		scale.addCurrencyMnyPrecision(headmnykeys, PositionType.Head, CtAbstractVO.CCURRENCYID, PositionType.Head,
				cardFormId);
		// 表头原币金额精度
		scale.addCurrencyMnyPrecision(headorgmnykeys, PositionType.Head, CtAbstractVO.CORIGCURRENCYID,
				PositionType.Head, cardFormId);
		// 折本汇率精度
		scale.addorgExchangeRatePrecision(exchangeratekeys, PositionType.Head, CtAbstractVO.PK_ORG, PositionType.Head,
				CtAbstractVO.CORIGCURRENCYID, PositionType.Head, CtAbstractVO.CCURRENCYID, PositionType.Head,
				cardFormId);
		// 集团本位币汇率
		scale.addGroupExchgRatePrecision(groupratekeys, PositionType.Head, CtAbstractVO.CORIGCURRENCYID,
				PositionType.Head, CtAbstractVO.CCURRENCYID, PositionType.Head, cardFormId);
		// 全局本位币汇率
		scale.addGlobalExchgRatePrecision(globalratekeys, PositionType.Head, CtAbstractVO.CORIGCURRENCYID,
				PositionType.Head, CtAbstractVO.CCURRENCYID, PositionType.Head, cardFormId);

		// 全局本位币金额精度
		scale.addGlobalStdMnyPrecision(globalmnykeys, PositionType.Body, cardTableId);
		// 集团本位币金额精度
		scale.addGroupStdMnyPrecision(groupmnykeys, PositionType.Body, cardTableId);
		// 换算率精度
		scale.addChangeRatePrecision(changeRates, PositionType.Body, cardTableId);
		// 报价单位数量精度
		scale.addNumPrecision(quoteNumkeys, PositionType.Body, CtAbstractBVO.CQTUNITID, PositionType.Body, cardTableId);
		// 业务单位数量精度
		scale.addNumPrecision(assistNumkeys, PositionType.Body, CtAbstractBVO.CASTUNITID, PositionType.Body,
				cardTableId);
		// 主单位数量精度
		scale.addNumPrecision(numkeys, PositionType.Body, CtAbstractBVO.CUNITID, PositionType.Body, cardTableId);
		// 原币单价精度
		scale.addCurrencyPricePrecision(origpricekeys, PositionType.Body, CtAbstractVO.CORIGCURRENCYID,
				PositionType.Head, cardTableId);
		// 本币单价精度
		scale.addCurrencyPricePrecision(pricekeys, PositionType.Body, CtAbstractVO.CCURRENCYID, PositionType.Head,
				cardTableId);
		// 表体本币金额精度
		scale.addCurrencyMnyPrecision(mnykeys, PositionType.Body, CtAbstractVO.CCURRENCYID, PositionType.Head,
				cardTableId);
		// 表体原币金额精度
		scale.addCurrencyMnyPrecision(orgmnykeys, PositionType.Body, CtAbstractVO.CORIGCURRENCYID, PositionType.Head,
				cardTableId);
		scale.addCurrencyMnyPrecision(exporgmnykeys, PositionType.Body, CtAbstractVO.CORIGCURRENCYID, PositionType.Head,
				cardFeeId);
		// 税率精度
		scale.addTaxratePrecision(taxRatekeys, PositionType.Body, cardTableId);
		// 合计信息精度控制器(整单数量整单金额)
		totalscale.setHeadTailKeys(totalkeys);

		scale.processPrecision();
	}

	/** 字段 **/
	// 折本汇率
	private static String[] exchangeratekeys = new String[] { CtAbstractVO.NEXCHANGERATE };
	// 集团本币汇率
	private static String[] groupratekeys = new String[] { CtAbstractVO.NGROUPEXCHGRATE };
	// 全局本币汇率
	private static String[] globalratekeys = new String[] { CtAbstractVO.NGLOBALEXCHGRATE };
	// 合计信息
	private static String[] totalkeys = new String[] { CtAbstractVO.NTOTALASTNUM };

	// 全局本位币金额
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

	/** 页面AREAID **/
	private static String cardFormId = "head"; // 卡片表头区
	private static String cardTableId = "body"; // 卡片表体区
//	private static String cardTermId = "contractterm"; // 合同条款区
//	private static String cardPayId = "payagree"; // 付款协议
	private static String cardFeeId = "contractfee"; // 合同费用
//	private static String cardMemoraId = "contractmemora"; // 合同大事记
	// private static String cardChangeId = "changehistory";// 变更历史
	// private static String cardExecutId = "executeprocess"; // 执行过程
}
