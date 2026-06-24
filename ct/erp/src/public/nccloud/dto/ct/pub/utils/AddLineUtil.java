package nccloud.dto.ct.pub.utils;

import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * @description 表体新增行工具
 * @author xiahui
 * @date 创建时间：2019-1-29 上午10:48:53
 * @version ncc1.0
 **/
public class AddLineUtil {

	private static final String PK_CT_PRICE_NAME = "pk_ct_price.vname";

	/**
	 * 设置vat字段默认值
	 * 
	 * @param util
	 * @param rows
	 * @param pk_org 采购组织
	 */
	public static void setVatDefaultValue(ExtBillUtil util, int[] rows, String pk_org) {

		Integer ninvctlstyle = (Integer) CtTransBusitypesUtil.getNinvctlstyle(util);
		String sendcountry = CTVatUtil.getPuSendCountry(util.getHeadTailStringValue(CtPuVO.CVENDORID));
		String rececountry = CTVatUtil.getPuRececountry(pk_org);
		String cvendorid = util.getHeadTailStringValue(CtPuVO.CVENDORID);
		UFDate date = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);

		// 根据财务组织取得报税国, 由于之前设财务组织默认值的时候表体财务组织 = 表头采购组织，故而此处直接取表头采购组织
		String taxcountry = CTVatUtil.getTaxCountry(pk_org);
		BuySellFlagEnum buysell = CTVatUtil.getPuBuySellFlag(sendcountry, taxcountry);
		Boolean triatrade = CTVatUtil.getPuTriatradeFlag(rececountry, taxcountry, buysell);

		VATInfoVO[] info = null;
		if (null != ninvctlstyle && !StringUtil.isEmptyTrimSpace(cvendorid) && !StringUtil.isEmptyTrimSpace(taxcountry)
				&& !StringUtil.isEmptyTrimSpace(sendcountry) && !StringUtil.isEmptyTrimSpace(rececountry)) {
			VATInfoQueryVO query = new VATInfoQueryVO(taxcountry, buysell, UFBoolean.valueOf(triatrade.booleanValue()),
					sendcountry, rececountry, cvendorid, null, date);
			info = VATBDService.querySupplierVATInfo(new VATInfoQueryVO[] { query });
		}

		for (int row : rows) {
			util.setBodyValue(row, CTVatNameConst.CSENDCOUNTRYID, sendcountry);
			util.setBodyValue(row, CTVatNameConst.CRECECOUNTRYID, rececountry);

			// 表体财务组织与表头主组织相同时，
			util.setBodyValue(row, CTVatNameConst.CTAXCOUNTRYID, taxcountry);
			if (null != buysell) {
				util.setBodyValue(row, CTVatNameConst.FBUYSELLFLAG, buysell.value());
			}
			util.setBodyValue(row, CTVatNameConst.BTRIATRADEFLAG, triatrade);

			if (null != info && info.length > 0 && null != info[0]) {
				util.setBodyValue(row, CTVatNameConst.CTAXCODEID, info[0].getCtaxcodeid());
				util.setBodyValue(row, CtAbstractBVO.FTAXTYPEFLAG, info[0].getFtaxtypeflag());
				util.setBodyValue(row, CTVatNameConst.NNOSUBTAXRATE, info[0].getNnosubtaxrate());
				util.setBodyValue(row, CtAbstractBVO.NTAXRATE, info[0].getNtaxrate());
			}

		}
	}

	/**
	 * 设置采购合同基本页签默认值
	 * 
	 * @param util
	 * @param rows
	 * @param puorg
	 */
	public static void setCtPuBVODefaultValue(ExtBillUtil util, int[] rows, String pk_org) {
		String pk_org_v = util.getHeadTailStringValue(CtPuVO.PK_ORG_V);
		String pk_group = util.getHeadTailStringValue(CtPuVO.PK_GROUP);
		OrgVO orgVO = OrgUnitPubService.getOrg(pk_org);
		String taxcountry = CTVatUtil.getTaxCountry(orgVO.getPk_corp());

		for (int row : rows) {
			// 清空合同价格信息表主键
			util.setBodyValue(row, CtPuBVO.PK_CT_PRICE, null);
			// 清空合同价格信息表名称
			util.setBodyValue(row, AddLineUtil.PK_CT_PRICE_NAME, null);

			// 设置表体默认值
			util.setBodyValue(row, CtPuBVO.PK_ORG, pk_org);
			util.setBodyValue(row, CtPuBVO.PK_FINANCEORG, pk_org);

			util.setBodyValue(row, CtAbstractBVO.PK_ORG_V, pk_org_v);
			util.setBodyValue(row, CtPuBVO.PK_FINANCEORG_V, pk_org_v);

			util.setBodyValue(row, CtPuBVO.PK_GROUP, pk_group);

			util.setBodyValue(0, CTVatNameConst.CTAXCOUNTRYID, taxcountry);
		}

		AddLineUtil.setVatDefaultValue(util, rows, pk_org);
	}
}
