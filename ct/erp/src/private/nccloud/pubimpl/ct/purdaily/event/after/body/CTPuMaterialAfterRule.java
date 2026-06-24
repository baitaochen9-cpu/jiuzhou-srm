package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.itf.ct.price.IQueryForPurdaily;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.pub.utils.AddLineUtil;
import nccloud.dto.ct.pub.utils.CtTransBusitypesUtil;
import nccloud.dto.ct.pub.utils.EditableByUnit;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.ReCalculateByPriceUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;
import nccloud.dto.ct.pub.utils.RelationCalculateFilter;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;
import nccloud.dto.scmpub.pub.utils.SCMMultSelectedUtil;

import nccloud.commons.lang.ArrayUtils;
import nccloud.commons.lang.StringUtils;

/**
 * @description 物料编辑后处理
 * @author xiahui
 * @date 创建时间：2019-1-21 下午2:24:57
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.editor.after.body.CTPuMaterial
 **/
public class CTPuMaterialAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event,
			@SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		// 获取物料多选编辑行
		int[] rows = new SCMMultSelectedUtil().handleMultSelected(billvo, event, userobject);
		// 设置表体默认单位 换算率
		new EditableByUnit(util, CTBillType.PurDaily.getCode()).setEditable(rows);

		String pk_org = util.getHeadTailStringValue(CtAbstractVO.PK_ORG);

		// 设置表体默认值
		AddLineUtil.setCtPuBVODefaultValue(util, rows, pk_org);

		this.vat(util, rows);

		// 根据物料,供应商询合同价格信息表价格,一条数据默认设置,多条数据为空
		this.ctPrice(util, rows);

		return billvo;
	}

	private void ctPrice(ExtBillUtil util, int[] rows) {
		if ((ValueUtils.getBoolean(util.getHeadValue(CtPuVO.BBRACKETORDER)))) {
			return;
		}
		List<String[]> bodyCondition = new ArrayList<String[]>();
		String pk_org = util.getHeadTailStringValue(CtAbstractVO.PK_ORG);
		String cvendorid = util.getHeadTailStringValue(CtPuVO.CVENDORID);
		String corigcurrencyid = util.getHeadTailStringValue(CtAbstractVO.CORIGCURRENCYID);
		if (!StringUtils.isEmpty(pk_org) && !StringUtils.isEmpty(cvendorid) && !StringUtils.isEmpty(corigcurrencyid)) {
			for (int row : rows) {
				String pk_srcmaterial = util.getBodyStringValue(row, CtAbstractBVO.PK_SRCMATERIAL);
				String cqtunitid = util.getBodyStringValue(row, CtAbstractBVO.CQTUNITID);
				if (!StringUtils.isEmpty(pk_srcmaterial) && !StringUtils.isEmpty(cqtunitid)) {
					bodyCondition.add(new String[] { String.valueOf(row), pk_srcmaterial, cqtunitid });
				}
			}

			if (bodyCondition.size() > 0) {
				try {
					IQueryForPurdaily iservice = NCLocator.getInstance().lookup(IQueryForPurdaily.class);
					String[][] ctPriceInfos = null;
					ctPriceInfos = iservice.queryCtPriceByOrgSupplierCurrencyMaterialUnit(pk_org, cvendorid,
							corigcurrencyid, bodyCondition.toArray(new String[bodyCondition.size()][]));
					if (!ArrayUtils.isEmpty(ctPriceInfos)) {
						for (String[] info : ctPriceInfos) {
							util.setBodyValue(Integer.parseInt(info[0]), CtPuBVO.PK_CT_PRICE, info[1]);
						}
					}
				} catch (Exception e1) {
					ExceptionUtils.wrappException(e1);
				}
			}
		}
	}

	private void vat(ExtBillUtil util, int[] rows) {
		String cvendorid = util.getHeadTailStringValue(CtPuVO.CVENDORID);
		if (StringUtil.isEmptyTrimSpace(cvendorid)) {
			return;
		}
		HashMap<Integer, VATInfoQueryVO> querys = this.getVatQuery(rows, util, cvendorid);

		this.setVatInfo(util, querys);
	}

	private HashMap<Integer, VATInfoQueryVO> getVatQuery(int[] rows, ExtBillUtil util, String cvendorid) {
		UFDate date = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);

		// 物料控制方式
		Object ninvctlstyle = CtTransBusitypesUtil.getNinvctlstyle(util);

		String sendcountry = CTVatUtil.getPuSendCountry(cvendorid);
		String rececountry = CTVatUtil.getPuRececountry(util.getHeadTailStringValue(CtAbstractVO.PK_ORG));

		HashMap<Integer, VATInfoQueryVO> querys = new HashMap<Integer, VATInfoQueryVO>();
		HashMap<String, String> taxcountryorg = new HashMap<String, String>();
		for (int i : rows) {
			util.setBodyValue(i, CTVatNameConst.CSENDCOUNTRYID, sendcountry);
			util.setBodyValue(i, CTVatNameConst.CRECECOUNTRYID, rececountry);
			String pk_financeorg = util.getBodyStringValue(i, CtAbstractBVO.PK_FINANCEORG);
			String taxcountry = taxcountryorg.get(pk_financeorg);
			if (StringUtil.isEmptyTrimSpace(taxcountry)) {
				taxcountry = CTVatUtil.getTaxCountry(util.getBodyStringValue(i, CtAbstractBVO.PK_FINANCEORG));
				taxcountryorg.put(pk_financeorg, taxcountry);
			}

			util.setBodyValue(i, CTVatNameConst.CTAXCOUNTRYID, taxcountry);
			BuySellFlagEnum buysell = CTVatUtil.getPuBuySellFlag(sendcountry, taxcountry);
			if (null != buysell) {
				util.setBodyValue(i, CTVatNameConst.FBUYSELLFLAG, buysell.value());
			}

			Boolean triatrade = CTVatUtil.getPuTriatradeFlag(rececountry, taxcountry, buysell);
			util.setBodyValue(i, CTVatNameConst.BTRIATRADEFLAG, triatrade);

			String material = util.getBodyStringValue(i, CtAbstractBVO.PK_MATERIAL);

			if (null == buysell || StringUtil.isEmptyTrimSpace(sendcountry) || StringUtil.isEmptyTrimSpace(rececountry)
					|| StringUtil.isEmptyTrimSpace(taxcountry) || StringUtil.isEmptyTrimSpace(cvendorid)
					|| ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value())
							&& StringUtil.isEmptyTrimSpace(material)) {
				continue;

			}

			VATInfoQueryVO query = new VATInfoQueryVO(taxcountry, BuySellFlagEnum.valueOf(buysell.value()),
					UFBoolean.valueOf(triatrade.booleanValue()), sendcountry, rececountry, cvendorid, material, date);
			querys.put(Integer.valueOf(i), query);
		}
		return querys;
	}

	private void setVatInfo(ExtBillUtil util, HashMap<Integer, VATInfoQueryVO> querys) {
		Map<VATInfoQueryVO, VATInfoVO> vats = VATBDService
				.querySupplierVATInfoM(querys.values().toArray(new VATInfoQueryVO[querys.size()]));

		ArrayList<Integer> rows = new ArrayList<Integer>();

		for (Entry<Integer, VATInfoQueryVO> row : querys.entrySet()) {

			VATInfoVO info = vats.get(row.getValue());
			int i = row.getKey().intValue();
			if (null != info) {
				// 税码改变才重新设置值
				if (!ValueUtil.equals(info.getCtaxcodeid(), util.getBodyValue(i, CTVatNameConst.CTAXCODEID))) {
					if (RelationCalculateFilter.isNeedRelationRow(util, i)) {
						// 有数量才联动计算
						rows.add(row.getKey());
					}

					util.setBodyValue(i, CTVatNameConst.CTAXCODEID, info.getCtaxcodeid());
					util.setBodyValue(i, CtAbstractBVO.FTAXTYPEFLAG, info.getFtaxtypeflag());
					util.setBodyValue(i, CTVatNameConst.NNOSUBTAXRATE, info.getNnosubtaxrate());
					util.setBodyValue(i, CtAbstractBVO.NTAXRATE, info.getNtaxrate());
				}
			} else {
				util.setBodyValue(i, CTVatNameConst.CTAXCODEID, null);
				util.setBodyValue(i, CtAbstractBVO.FTAXTYPEFLAG, null);
				util.setBodyValue(i, CTVatNameConst.NNOSUBTAXRATE, null);
				util.setBodyValue(i, CtAbstractBVO.NTAXRATE, null);
				if (RelationCalculateFilter.isNeedRelationRow(util, i)) {
					new ReCalculateByPriceUtil().reCalculate(util, new int[] { i });
				}
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
