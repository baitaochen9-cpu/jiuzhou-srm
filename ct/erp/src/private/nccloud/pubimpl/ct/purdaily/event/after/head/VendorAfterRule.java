package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.query.vendor.VendorInfo;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.ct.pub.utils.CtTransBusitypesUtil;
import nccloud.dto.ct.pub.utils.ExchangeRateUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.ReCalculateByPriceUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;
import nccloud.dto.ct.pub.utils.VendorUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 供应商编辑后
 * @author xiahui
 * @date 创建时间：2019-1-24 上午10:57:46
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.editor.after.head.Vendor
 **/
public class VendorAfterRule implements IHeadAfterRule<AggCtPuVO> {
	private static final String PK_CT_PRICE_NAME = "pk_ct_price.vname";

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		VendorInfo cvendor = this.getCvendorInfo(util);
		// 清空合同价格信息表主键
		util.setBodyValue(0, CtPuBVO.PK_CT_PRICE, null);
		// 清空合同价格信息表主键
		util.setBodyValue(0, VendorAfterRule.PK_CT_PRICE_NAME, null);
		if (cvendor == null) {
			return billvo;
		}
		// 人员
		if (!ValueUtil.isEmpty(cvendor.getRespPerson())) {
			util.setHeadValue(CtAbstractVO.PERSONNELID, cvendor.getRespPerson());
		}
		// 部门
		if (!ValueUtil.isEmpty(cvendor.getRespDepartment())) {
			util.setHeadValue(CtAbstractVO.DEPID, cvendor.getRespDepartment());
			Map<String, String> vidMap = DeptPubService.getLastVIDSByDeptIDS(new String[] { cvendor.getRespDepartment() });
			util.setHeadValue(CtAbstractVO.DEPID_V, vidMap.get(cvendor.getRespDepartment()));
		}
		// 交货地点
		if (!ValueUtil.isEmpty(cvendor.getAddress())) {
			util.setHeadValue(CtAbstractVO.DELIADDR, cvendor.getAddress());
		}
		// 币种
		if (!ValueUtil.isEmpty(cvendor.getDefaultCurrency())) {
			String currencyid = (String) util.getHeadValue(CtAbstractVO.CORIGCURRENCYID);
			if (!cvendor.getDefaultCurrency().equals(currencyid)) {
				util.setHeadValue(CtAbstractVO.CORIGCURRENCYID, cvendor.getDefaultCurrency());
				// new PuCorigcurrencyid().afterEdit(event);
			}
		}
		// 收付款协议
		if (!ValueUtil.isEmpty(cvendor.getDefaultPaymentTerm())) {
			String payterm = (String) util.getHeadValue(CtAbstractVO.PK_PAYTERM);
			if (!cvendor.getDefaultPaymentTerm().equals(payterm)) {
				util.setHeadValue(CtAbstractVO.PK_PAYTERM, cvendor.getDefaultPaymentTerm());
				// new PayTerm().afterEdit(event);
			}
		}
		// 发货国
		if (!ValueUtil.isEmpty(cvendor.getPk_country())) {
			this.vat(util, cvendor);
		}
		// 折本汇率
		ExchangeRateUtil.changeSellExchangeRate(util);
		return billvo;
	}

	private VendorInfo getCvendorInfo(ExtBillUtil util) {
		String cvendorid = util.getHeadTailStringValue(CtPuVO.CVENDORID);
		if (cvendorid == null) {
			return null;
		}
		String pk_org = util.getHeadTailStringValue(CtAbstractVO.PK_ORG);
		return VendorUtil.queryCvendor(cvendorid, pk_org);
	}

	private void vat(ExtBillUtil util, VendorInfo cvendor) {
		HashMap<Integer, VATInfoQueryVO> querys = this.setCountryAndGetVatQuery(util, cvendor);

		if (querys.isEmpty()) {
			return;
		}
		this.setVatInfo(util, querys);
	}

	private HashMap<Integer, VATInfoQueryVO> setCountryAndGetVatQuery(ExtBillUtil util, VendorInfo cvendor) {
		int count = util.getBodyRowCount();
		HashMap<Integer, VATInfoQueryVO> querys = new HashMap<Integer, VATInfoQueryVO>();
		HashMap<String, String> taxcountryorg = new HashMap<String, String>();
		String cvendorid = util.getHeadTailStringValue(CtPuVO.CVENDORID);
		UFDate date = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);
		// 物料控制方式
		Object ninvctlstyle = CtTransBusitypesUtil.getNinvctlstyle(util);
		String sendcountry = cvendor.getPk_country();
		String rececountry = CTVatUtil.getPuRececountry(util.getHeadTailStringValue(CtAbstractVO.PK_ORG));
		for (int i = 0; i < count; i++) {

			util.setBodyValue(i, CTVatNameConst.CSENDCOUNTRYID, cvendor.getPk_country());

			util.setBodyValue(i, CTVatNameConst.CRECECOUNTRYID, rececountry);

			String pk_financeorg = util.getBodyStringValue(i, CtAbstractBVO.PK_FINANCEORG);
			String taxcountry = taxcountryorg.get(pk_financeorg);
			if (StringUtil.isEmptyTrimSpace(taxcountry)) {
				taxcountry = CTVatUtil.getTaxCountry(util.getBodyStringValue(i, CtAbstractBVO.PK_FINANCEORG));
				taxcountryorg.put(pk_financeorg, taxcountry);
			}

			util.setBodyValue(i, CTVatNameConst.CTAXCOUNTRYID, rececountry);

			BuySellFlagEnum buysell = CTVatUtil.getPuBuySellFlag(sendcountry, taxcountry);
			if (null != buysell) {
				util.setBodyValue(i, CTVatNameConst.FBUYSELLFLAG, buysell.value());
			}
			Boolean triatrade = CTVatUtil.getPuTriatradeFlag(rececountry, taxcountry, buysell);
			util.setBodyValue(i, CTVatNameConst.BTRIATRADEFLAG, triatrade);

			String material = util.getBodyStringValue(i, CtAbstractBVO.PK_MATERIAL);

			if (StringUtil.isEmptyTrimSpace(sendcountry) || StringUtil.isEmptyTrimSpace(rececountry)
					|| StringUtil.isEmptyTrimSpace(taxcountry) || StringUtil.isEmptyTrimSpace(cvendorid)
					|| ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value()) && StringUtil.isEmptyTrimSpace(material)) {
				continue;

			}

			VATInfoQueryVO query = new VATInfoQueryVO(taxcountry, buysell, UFBoolean.valueOf(triatrade.booleanValue()),
					sendcountry, rececountry, cvendorid, material, date);
			querys.put(Integer.valueOf(i), query);
		}
		return querys;
	}

	private void setVatInfo(ExtBillUtil util, HashMap<Integer, VATInfoQueryVO> querys) {
		Map<VATInfoQueryVO, VATInfoVO> vats = VATBDService.querySupplierVATInfoM(querys.values().toArray(
				new VATInfoQueryVO[querys.size()]));

		ArrayList<Integer> rows = new ArrayList<Integer>();

		for (Entry<Integer, VATInfoQueryVO> row : querys.entrySet()) {

			VATInfoVO info = vats.get(row.getValue());
			int i = row.getKey().intValue();
			if (null != info && !ValueUtil.equals(info.getCtaxcodeid(), util.getBodyValue(i, CTVatNameConst.CTAXCODEID))) {
				rows.add(row.getKey());

				util.setBodyValue(i, CTVatNameConst.CTAXCODEID, info.getCtaxcodeid());
				util.setBodyValue(i, CtAbstractBVO.FTAXTYPEFLAG, info.getFtaxtypeflag());
				util.setBodyValue(i, CTVatNameConst.NNOSUBTAXRATE, info.getNnosubtaxrate());
				util.setBodyValue(i, CtAbstractBVO.NTAXRATE, info.getNtaxrate());
			}

			if (null == info) {
				util.setBodyValue(i, CTVatNameConst.CTAXCODEID, null);
				util.setBodyValue(i, CtAbstractBVO.FTAXTYPEFLAG, null);
				util.setBodyValue(i, CTVatNameConst.NNOSUBTAXRATE, null);
				util.setBodyValue(i, CtAbstractBVO.NTAXRATE, null);
				new ReCalculateByPriceUtil().reCalculate(util, new int[] { i });
			}

		}
		if (rows.size() > 0) {
			int[] irows = new int[rows.size()];
			for (int i = 0, len = rows.size(); i < len; i++) {
				irows[i] = rows.get(i).intValue();
			}
			RelationCalculate calcul = new RelationCalculate();
			calcul.calculate(util, irows, CtAbstractBVO.NTAXRATE);
			calcul.calculate(util, irows, CTVatNameConst.NNOSUBTAXRATE);
		}
	}
}
