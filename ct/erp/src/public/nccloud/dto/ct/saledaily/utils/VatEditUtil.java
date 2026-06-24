package nccloud.dto.ct.saledaily.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.rule.SaleRelationCalculate;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.ct.util.RelationCalculateUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * @description VAT编辑
 * @author wangshrc
 * @date 2019年2月16日 下午3:16:03
 * @version ncc1.0
 */
public class VatEditUtil {

	public void vat(AggCtSaleVO billvo, int[] rows) {
		String pk_customer = billvo.getParentVO().getPk_customer();
		HashMap<Integer, VATInfoQueryVO> querys = this.getVatQuery(rows,
				billvo, pk_customer);

		this.setVatInfo(billvo, querys);

	}

	private HashMap<Integer, VATInfoQueryVO> getVatQuery(int[] rows,
			AggCtSaleVO billvo, String pk_customer) {
		CtSaleVO hvo = billvo.getParentVO();
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		UFDate date = hvo.getSubscribedate();
		// 物料控制方式
		Object ninvctlstyle = this.getNinvctlstyle(billvo);
		String rececountry = CTVatUtil.getSaleRececountry(pk_customer);
		String sendcountry = CTVatUtil.getSaleSendcountry(hvo.getPk_org());
		HashMap<Integer, VATInfoQueryVO> querys = new HashMap<Integer, VATInfoQueryVO>();
		HashMap<String, String> taxcountryorg = new HashMap<String, String>();
		for (int i : rows) {
			bvos[i].setCrececountryid(rececountry);
			bvos[i].setCsendcountryid(sendcountry);
			String pk_financeorg = bvos[i].getPk_financeorg();
			String taxcountry = taxcountryorg.get(pk_financeorg);
			if (StringUtil.isEmptyTrimSpace(taxcountry)) {
				taxcountry = CTVatUtil
						.getTaxCountry(bvos[i].getPk_financeorg());
				taxcountryorg.put(pk_financeorg, taxcountry);
			}
			bvos[i].setCtaxcountryid(taxcountry);
			BuySellFlagEnum buysell = CTVatUtil.getSaleBuySellFlag(rececountry,
					taxcountry);
			if (null != buysell) {
				bvos[i].setFbuysellflag(buysell.value());
			}
			Boolean triatrade = CTVatUtil.getSaleTriatradeFlag(sendcountry,
					taxcountry, buysell);
			bvos[i].setBtriatradeflag(UFBoolean.valueOf(triatrade));
			String material = bvos[i].getPk_material();
			if (StringUtil.isEmptyTrimSpace(sendcountry)
					|| StringUtil.isEmptyTrimSpace(rececountry)
					|| StringUtil.isEmptyTrimSpace(taxcountry)
					|| StringUtil.isEmptyTrimSpace(pk_customer)
					|| ValueUtil.equals(ninvctlstyle,
							Ninvctlstyle.MATERIAL.value())
					&& StringUtil.isEmptyTrimSpace(material)) {
				continue;
			}
			VATInfoQueryVO query = new VATInfoQueryVO(taxcountry, buysell,
					UFBoolean.valueOf(triatrade.booleanValue()), sendcountry,
					rececountry, pk_customer, material, date);
			querys.put(Integer.valueOf(i), query);
		}
		return querys;
	}

	private void setVatInfo(AggCtSaleVO billvo,
			HashMap<Integer, VATInfoQueryVO> querys) {
		Map<VATInfoQueryVO, VATInfoVO> vats = VATBDService
				.queryCustVATInfoM(querys.values().toArray(
						new VATInfoQueryVO[querys.size()]));
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		for (Entry<Integer, VATInfoQueryVO> row : querys.entrySet()) {
			VATInfoVO info = vats.get(row.getValue());
			int i = row.getKey().intValue();
			if (null != info) {
				// 税码改变才重新设置值
				if (!ValueUtil.equals(info.getCtaxcodeid(),
						bvos[i].getCtaxcodeid())) {
					if (RelationCalculateFilter.isNeedRelationRow(billvo, i)) {
						// 有数量才联动计算
						rows.add(row.getKey());
					}
					bvos[i].setCtaxcodeid(info.getCtaxcodeid());
					bvos[i].setFtaxtypeflag(info.getFtaxtypeflag());
					bvos[i].setNtaxrate(info.getNtaxrate());
				}
			} else {
				bvos[i].setCtaxcodeid(null);
				bvos[i].setFtaxtypeflag(null);
				bvos[i].setNtaxrate(null);
				if (RelationCalculateFilter.isNeedRelationRow(billvo, i))
					this.recal(billvo, new int[] { i });
			}

		}
		int[] irows = new int[rows.size()];
		if (rows.size() > 0) {
			for (int i = 0, len = rows.size(); i < len; i++) {
				irows[i] = rows.get(i).intValue();
			}
		}
		new SaleRelationCalculate().calculate(billvo, CtAbstractBVO.NTAXRATE);
	}

	private void recal(AggCtSaleVO billvo, int[] rows) {
		boolean isTaxPrior = RelationCalculateUtil.isTaxPrior(billvo
				.getParentVO().getPk_group());
		SaleRelationCalculate cal = new SaleRelationCalculate();
		if (isTaxPrior) {
			cal.calculate(billvo, CtAbstractBVO.NQTORIGTAXPRICE);
		} else {
			cal.calculate(billvo, CtAbstractBVO.NQTORIGPRICE);
		}
	}

	public Object getNinvctlstyle(AggCtSaleVO billvo) {
		CtSaleVO hvo = billvo.getParentVO();
		// 物料控制方式
		Integer ninvctlstyle = hvo.getNinvctlstyle();

		if (null != ninvctlstyle) {
			return ninvctlstyle;
		}
		// 合同类型
		String ctrantypeid = hvo.getCtrantypeid();
		if (!StringUtil.isEmptyTrimSpace(ctrantypeid)) {
			try {
				IBusinessTypeService iBusiness = (IBusinessTypeService) NCLocator
						.getInstance().lookup(
								IBusinessTypeService.class.getName());
				ninvctlstyle = iBusiness.queryMaterial(ctrantypeid);
				// 物料控制方式
				hvo.setNinvctlstyle(ninvctlstyle);
			} catch (Exception e1) {
				ExceptionUtils.wrappException(e1);
			}
		}

		return ninvctlstyle;
	}

}
