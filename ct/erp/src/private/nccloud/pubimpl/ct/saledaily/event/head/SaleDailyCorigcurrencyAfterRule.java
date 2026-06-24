package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleExpVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.saledaily.utils.ExchangeRateUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

import nccloud.commons.lang.StringUtils;

/**
 * @description 销售合同币种编辑后
 * @author wangshrc
 * @date 2019年2月14日 上午9:41:18
 * @version ncc1.0
 */
public class SaleDailyCorigcurrencyAfterRule implements
		IHeadAfterRule<AggCtSaleVO> {
	// 清空单价、金额字段
	private String[] bfields = { CtAbstractBVO.NGLOBALMNY,
			CtAbstractBVO.NGLOBALTAXMNY, CtAbstractBVO.NGPRICE,
			CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY,
			CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NMNY,
			CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGPRICE,
			CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NMNY,
			CtAbstractBVO.NORIGMNY, CtAbstractBVO.NGPRICE,
			CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY,
			CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NMNY,
			CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGPRICE,
			CtAbstractBVO.NORIGTAXMNY, CtAbstractBVO.NORIGTAXPRICE,
			CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE,
			CtAbstractBVO.NQTPRICE, CtAbstractBVO.NQTTAXPRICE,
			CtAbstractBVO.NTAX, CtAbstractBVO.NTAXMNY,
			CTVatNameConst.NCALTAXMNY, };

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		CtSaleVO hvo = billvo.getParentVO();
		String newValue = (String) event.getNewValue();
		if (StringUtils.isEmpty(newValue)) {
			userobject.put("errMsg", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("4020003_0", "04020003-0041")/*
															 * @res
															 * "币种未定义，请先定义币种。"
															 */);
			return billvo;
		}
		// 1. 币种改变汇率 2.汇率变动处理
		ExchangeRateUtil.changeBuyExchangeRate(billvo);

		// 636 币种改变清空表体单价、金额
		if (CTBillType.SaleDaily.getCode().equals(hvo.getCbilltypecode())) {
			CtSaleBVO[] bvos = billvo.getCtSaleBVO();
			CtSaleExpVO[] expVos = billvo.getCtSaleExpVO();
			for (int i = 0; i < bvos.length; i++) {
				for (int j = 0; j < this.bfields.length; j++) {
					bvos[i].setAttributeValue(bfields[j], null);
				}
			}
			for (int i = 0; i < expVos.length; i++) {
				expVos[i].setVexpsum(null);
			}

		}

		// 清空合同表头价税合计
		hvo.setNtotalorigmny(null);
		return billvo;
	}

}
