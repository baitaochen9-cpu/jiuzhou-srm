package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.ct.pub.utils.CtTransBusitypesUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.ReCalculateByPriceUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 国家编辑后处理
 * @author xiahui
 * @date 创建时间：2019-1-22 上午11:46:35
 * @version ncc1.0
 **/
public class CTPuCountryAfterRule implements IBodyAfterRule<AggCtPuVO> {

	/**
	 * 国家字段
	 */
	private String countrykey;

	public CTPuCountryAfterRule(String countryKey) {
		this.countrykey = countryKey;
	}

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		String country = util.getBodyStringValue(row, this.countrykey);
		if (StringUtil.isEmptyTrimSpace(country)) {
			return billvo;
		}

		String sendcountry = util.getBodyStringValue(row, CTVatNameConst.CSENDCOUNTRYID);
		String rececountry = util.getBodyStringValue(row, CTVatNameConst.CRECECOUNTRYID);
		String taxcountry = util.getBodyStringValue(row, CTVatNameConst.CTAXCOUNTRYID);

		BuySellFlagEnum buysell = CTVatUtil.getPuBuySellFlag(sendcountry, taxcountry);
		if (null != buysell) {
			String oldvalue = util.getHeadTailStringValue(CTVatNameConst.FBUYSELLFLAG);
			if (!ValueUtil.equals(oldvalue, buysell.value())) {
				util.setBodyValue(row, CTVatNameConst.FBUYSELLFLAG, buysell.value());
				new ReCalculateByPriceUtil().reCalculate(util, new int[] { row });
			}
		}

		Boolean triatrade = CTVatUtil.getPuTriatradeFlag(rececountry, taxcountry, buysell);
		util.setBodyValue(row, CTVatNameConst.BTRIATRADEFLAG, triatrade);

		this.setTaxInfo(util, row, sendcountry, rececountry, taxcountry, buysell, triatrade);

		return billvo;
	}

	private void relationCal(ExtBillUtil util, int row) {
		RelationCalculate calcul = new RelationCalculate();
		calcul.calculate(util, new int[] { row }, CtAbstractBVO.NTAXRATE);
		calcul.calculate(util, new int[] { row }, CTVatNameConst.NNOSUBTAXRATE);

	}

	private void setTaxInfo(ExtBillUtil util, int row, String sendcountry, String rececountry, String taxcountry,
			BuySellFlagEnum buysell, Boolean triatrade) {
		String material = util.getBodyStringValue(row, CtAbstractBVO.PK_MATERIAL);

		String cvendorid = util.getHeadTailStringValue(CtPuVO.CVENDORID);
		// 物料控制方式
		Object ninvctlstyle = CtTransBusitypesUtil.getNinvctlstyle(util);
		if (StringUtil.isEmptyTrimSpace(sendcountry) || StringUtil.isEmptyTrimSpace(rececountry)
				|| StringUtil.isEmptyTrimSpace(taxcountry) || StringUtil.isEmptyTrimSpace(cvendorid)
				|| ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value()) && StringUtil.isEmptyTrimSpace(material)) {
			return;

		}
		UFDate date = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);

		VATInfoQueryVO query = new VATInfoQueryVO(taxcountry, buysell, UFBoolean.valueOf(triatrade.booleanValue()),
				sendcountry, rececountry, cvendorid, material, date);
		VATInfoVO[] info = VATBDService.querySupplierVATInfo(new VATInfoQueryVO[] { query });

		if (null != info && info.length > 0 && null != info[0]) {
			// 税码改变才重新设置值
			if (!ValueUtil.equals(info[0].getCtaxcodeid(), util.getBodyValue(row, CTVatNameConst.CTAXCODEID))) {
				util.setBodyValue(row, CTVatNameConst.CTAXCODEID, info[0].getCtaxcodeid());
				util.setBodyValue(row, CtAbstractBVO.FTAXTYPEFLAG, info[0].getFtaxtypeflag());
				util.setBodyValue(row, CTVatNameConst.NNOSUBTAXRATE, info[0].getNnosubtaxrate());
				util.setBodyValue(row, CtAbstractBVO.NTAXRATE, info[0].getNtaxrate());
				this.relationCal(util, row);
			}

		} else {
			util.setBodyValue(row, CTVatNameConst.CTAXCODEID, null);
			util.setBodyValue(row, CtAbstractBVO.FTAXTYPEFLAG, null);
			util.setBodyValue(row, CTVatNameConst.NNOSUBTAXRATE, null);
			util.setBodyValue(row, CtAbstractBVO.NTAXRATE, null);
			new ReCalculateByPriceUtil().reCalculate(util, new int[] { row });
		}

	}
}
