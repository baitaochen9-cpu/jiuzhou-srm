package nccloud.pubimpl.ct.purdaily.utils;

import nc.ui.pub.bill.IBillItem;
import nc.vo.ct.ap.entity.CtApBVO;
import nc.vo.ct.ap.entity.CtApVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.scale.BillScaleProcessor;
import nc.vo.pubapp.scale.BillVOScaleProcessor;
import nc.vo.pubapp.scale.FieldInfo;
import nc.vo.pubapp.scale.PosEnum;
import nc.vo.pubapp.scale.TotalValueVOScaleProcessor;
import nccloud.pubitf.scmpub.pub.print.BaseMetaPrintService.IBeforePrintDataProcess;

/**
 * @description 打印前处理（精度处理）
 * @author xiahui
 * @date 创建时间：2019-2-13 下午6:03:18
 * @version ncc1.0
 **/
public class PurdailyBeforePrintDataProcess implements IBeforePrintDataProcess {

	@Override
	public Object[] processData(Object[] datas) {
		AggregatedValueObject[] bills = new AggregatedValueObject[datas.length];
		for (int i = 0; i < datas.length; i++) {
			bills[i] = (AggregatedValueObject) datas[i];
		}

		String pk_group = AppContext.getInstance().getPkGroup();
		BillVOScaleProcessor scale = new BillVOScaleProcessor(pk_group, bills);
		TotalValueVOScaleProcessor totalScale = new TotalValueVOScaleProcessor(bills);

		// 全局本位币金额精度
		scale.setGlobalLocMnyCtlInfo(globalmnykeys, PosEnum.body, null);
		// 集团本位币金额精度
		scale.setGroupLocMnyCtlInfo(groupmnykeys, PosEnum.body, null);
		// 税率
		scale.setTaxRateCtlInfo(ntaxratekeys, PosEnum.body, null);
		// 换算率
		scale.setHslCtlInfo(changeRates, PosEnum.body, null);
		// 报价单位数量精度
		scale.setNumCtlInfo(quoteNumkeys, PosEnum.body, null, CtAbstractBVO.CQTUNITID, PosEnum.body, null);
		// 业务单位数量精度
		scale.setNumCtlInfo(assistNumkeys, PosEnum.body, null, CtAbstractBVO.CASTUNITID, PosEnum.body, null);
		// 主单位数量精度
		scale.setNumCtlInfo(numkeys, PosEnum.body, null, CtAbstractBVO.CUNITID, PosEnum.body, null);
		// 单价精度
		// 原币单价精度
		scale.setPriceCtlInfo(origpricekeys, PosEnum.body, null, CtAbstractVO.CORIGCURRENCYID, PosEnum.head, null);
		// 本币单价精度
		scale.setPriceCtlInfo(pricekeys, PosEnum.body, null, CtAbstractVO.CCURRENCYID, PosEnum.head, null);
		// scale.setPriceCtlInfo(this.pricekeys, PosEnum.body, null);
		// 本币金额精度
		scale.setMnyCtlInfo(mnykeys, PosEnum.body, null, CtAbstractVO.CCURRENCYID, PosEnum.head, null);
		// 原币金额精度
		scale.setMnyCtlInfo(orgmnykeys, PosEnum.body, null, CtAbstractVO.CORIGCURRENCYID, PosEnum.head, null);
		// 表头原币金额精度
		scale.setMnyCtlInfo(headorgmnykeys, PosEnum.head, null, CtAbstractVO.CORIGCURRENCYID, PosEnum.head, null);
		// 表头本币金额精度
		scale.setMnyCtlInfo(headmnykeys, PosEnum.head, null, CtAbstractVO.CCURRENCYID, PosEnum.head, null);
		// 汇率精度处理
		this.setOrgExchange(scale);
		this.setGroupExchange(scale);
		this.setGlobalExchaneg(scale);

		// 合计信息精度控制器(整单数量整单金额)
		totalScale.setHeadTailKeys(new String[] { CtAbstractVO.NTOTALASTNUM });
		scale.process();

		return datas;
	}

	private void setGlobalExchaneg(BillScaleProcessor scale) {
		FieldInfo rate = new FieldInfo(CtAbstractVO.NGLOBALEXCHGRATE, IBillItem.HEAD, null);
		FieldInfo orgOrigCurr = new FieldInfo(CtAbstractVO.CORIGCURRENCYID, IBillItem.HEAD, null);
		FieldInfo orgLocCurr = new FieldInfo(CtAbstractVO.CCURRENCYID, IBillItem.HEAD, null);
		scale.setGlobalExchangeCtlInfo(rate, orgOrigCurr, orgLocCurr);
	}

	private void setGroupExchange(BillScaleProcessor scale) {
		FieldInfo rate = new FieldInfo(CtAbstractVO.NGROUPEXCHGRATE, IBillItem.HEAD, null);
		FieldInfo orgOrigCurr = new FieldInfo(CtAbstractVO.CORIGCURRENCYID, IBillItem.HEAD, null);
		FieldInfo orgLocCurr = new FieldInfo(CtAbstractVO.CCURRENCYID, IBillItem.HEAD, null);
		scale.setGroupExchangeCtlInfo(rate, orgOrigCurr, orgLocCurr);
	}

	private void setOrgExchange(BillScaleProcessor scale) {
		FieldInfo rate = new FieldInfo(CtAbstractVO.NEXCHANGERATE, IBillItem.HEAD, null);
		FieldInfo srcCurr = new FieldInfo(CtAbstractVO.CORIGCURRENCYID, IBillItem.HEAD, null);
		FieldInfo destCurr = new FieldInfo(CtAbstractVO.CCURRENCYID, IBillItem.HEAD, null);
		FieldInfo org = new FieldInfo(CtAbstractVO.PK_ORG, IBillItem.HEAD, null);
		scale.setOrgExchangeCtlInfo(rate, srcCurr, destCurr, org);
	}

	// 全局本位币金额
	private static String[] globalmnykeys = new String[] { CtAbstractBVO.NGLOBALMNY, CtAbstractBVO.NGLOBALTAXMNY };
	// 集团本位币金额
	private static String[] groupmnykeys = new String[] { CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY };
	// 数量
	private static String[] assistNumkeys = new String[] { CtAbstractBVO.NASTNUM };
	// 换算率
	private static String[] changeRates = new String[] { CtAbstractBVO.VCHANGERATE, CtAbstractBVO.VQTUNITRATE };
	// 表头本币金额
	private static String[] headmnykeys = new String[] { CtAbstractVO.NPREPAYLIMITMNY, CtAbstractVO.NTOTALGPAMOUNT,
			CtAbstractVO.NTOTALTAXMNY, CtApVO.NTOTALCOPAMOUNT, CtApVO.NPREPAYMNY };
	// 表头原币金额
	private static String[] headorgmnykeys = new String[] { CtAbstractVO.NORIPREPAYLIMITMNY, CtAbstractVO.NORIGPSHAMOUNT,
			CtApVO.NORIGCOPAMOUNT, CtApVO.NORIPREPAYMNY, CtAbstractVO.NTOTALORIGMNY };
	// 本币金额
	private static String[] mnykeys = new String[] { CtAbstractBVO.NMNY, CtAbstractBVO.NTAXMNY, CtAbstractBVO.NTAX,
			CtAbstractBVO.NTOTALGPMNY, CTVatNameConst.NNOSUBTAX, CTVatNameConst.NCALTAXMNY, CTVatNameConst.NCALCOSTMNY };
	// 税率
	private static String[] ntaxratekeys = new String[] { CtAbstractBVO.NTAXRATE, CTVatNameConst.NNOSUBTAXRATE };
	// 主数量
	private static String[] numkeys = new String[] { CtAbstractBVO.NNUM, CtAbstractBVO.NORDNUM };
	// 原币金额
	private static String[] orgmnykeys = new String[] { CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGTAXMNY,
			CtAbstractBVO.NORDSUM, CtAbstractBVO.NORITOTALGPMNY, CtApBVO.NORICOPEGPMNY };
	// 单价
	// 原币
	private static String[] origpricekeys = new String[] { CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE,
			CtAbstractBVO.NORIGPRICE, CtAbstractBVO.NORIGTAXPRICE, };
	// 本币
	private static String[] pricekeys = new String[] { CtAbstractBVO.NQTPRICE, CtAbstractBVO.NQTTAXPRICE,
			CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NGPRICE };
	// 业务单位数量
	private static String[] quoteNumkeys = new String[] { CtAbstractBVO.NQTUNITNUM };

}
