package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractExpVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuExpVO;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nccloud.dto.ct.pub.utils.ExchangeRateUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.dto.to.pub.constance.MsgFlag;

import nccloud.commons.lang.StringUtils;

/**
 * @description 币种 编辑后事件
 * @author xiahui
 * @date 创建时间：2019-2-14 上午10:37:03
 * @version ncc1.0
 **/
public class PuCorigcurrencyidAfterRule implements IHeadAfterRule<AggCtPuVO> {

	private static final String PK_CT_PRICE_NAME = "pk_ct_price.vname";

	// 清空单价、金额字段
	private String[] bfields = { CtAbstractBVO.NGLOBALMNY, CtAbstractBVO.NGLOBALTAXMNY, CtAbstractBVO.NGPRICE,
			CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY, CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NMNY,
			CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGPRICE, CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NMNY,
			CtAbstractBVO.NORIGMNY, CtAbstractBVO.NGPRICE, CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY,
			CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NMNY, CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGPRICE,
			CtAbstractBVO.NORIGTAXMNY, CtAbstractBVO.NORIGTAXPRICE, CtAbstractBVO.NQTORIGPRICE,
			CtAbstractBVO.NQTORIGTAXPRICE, CtAbstractBVO.NQTPRICE, CtAbstractBVO.NQTTAXPRICE, CtAbstractBVO.NTAX,
			CtAbstractBVO.NTAXMNY, CTVatNameConst.NCALTAXMNY, CTVatNameConst.NCALCOSTMNY };

	@SuppressWarnings("unchecked")
	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		String newValue = ValueUtils.getString(event.getNewValue());
		if (StringUtils.isEmpty(newValue)) {
			userobject
					.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0041")); // 币种未定义，请先定义币种。
			return billvo;
		}
		// 1. 币种改变汇率 2.汇率变动处理
		ExchangeRateUtil.changeSellExchangeRate(util);

		// 清空合同价格信息表主键
		util.setBodyValue(CtPuBVO.PK_CT_PRICE, null);
		util.setBodyValue(PuCorigcurrencyidAfterRule.PK_CT_PRICE_NAME, null);
		// 636 币种改变清空表体单价、金额
		util.clearBodyValue(bfields);
		util.clearBodyValue(new String[] { CtAbstractExpVO.VEXPSUM }, CtPuExpVO.class);

		util.setHeadValue(CtAbstractVO.NTOTALORIGMNY, null);
		util.setHeadValue(CtAbstractVO.NTOTALTAXMNY, null);

		return billvo;
	}

}
