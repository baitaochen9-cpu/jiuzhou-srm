package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

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
import nc.vo.ct.util.CTVatUtil;
import nc.vo.ct.util.RelationCalculateUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

import nccloud.commons.lang.StringUtils;

/**
 * @description 销售合同国家编辑后
 * @author wangshrc
 * @date 2019年2月15日 上午9:06:36
 * @version ncc1.0
 */
public class SaleDailyCountryAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		int lineNum = event.getRow();
		String country = billvo.getCtSaleBVO()[lineNum].getAttributeValue(
				event.getChangeKey()).toString();
		if (StringUtils.isNotBlank(country)) {
			this.setValue(billvo, lineNum);
		}
		// TODO
		// event.getBillCardPanel().getBillModel().loadLoadRelationItemValue(lineNum);
		return billvo;
	}

	public void setValue(AggCtSaleVO billvo, int lineNum) {
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		String sendcountry = bvos[lineNum].getCsendcountryid();
		String rececountry = bvos[lineNum].getCrececountryid();
		String taxcountry = bvos[lineNum].getCtaxcountryid();

		BuySellFlagEnum buysell = CTVatUtil.getSaleBuySellFlag(rececountry,
				taxcountry);
		if (null != buysell) {
			Integer oldbuysell = bvos[lineNum].getFbuysellflag();
			bvos[lineNum].setFbuysellflag(buysell.value());
			if (!buysell.value().equals(oldbuysell)) {
				this.recal(billvo, lineNum);
			}

		}
		Boolean triatrade = CTVatUtil.getSaleTriatradeFlag(sendcountry,
				taxcountry, buysell);
		bvos[lineNum].setBtriatradeflag(UFBoolean.valueOf(triatrade));

		this.setTaxInfo(billvo, lineNum, sendcountry, rececountry, taxcountry,
				buysell, triatrade);
	}

	private void cleanTaxItemValue(AggCtSaleVO billvo, int lineNum) {
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		bvos[lineNum].setCtaxcodeid(null);
		bvos[lineNum].setFtaxtypeflag(null);
		bvos[lineNum].setNtaxrate(null);
		this.recal(billvo, lineNum);
	}

	private boolean isNotStandard(String material, Integer ninvctlstyle) {
		if (Ninvctlstyle.MATERIAL.value().equals(ninvctlstyle)
				&& StringUtils.isBlank(material)) {
			return true;
		}

		return false;
	}

	private void setTaxInfo(AggCtSaleVO billvo, int lineNum,
			String sendcountry, String rececountry, String taxcountry,
			BuySellFlagEnum buysell, Boolean triatrade) {
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		CtSaleVO hvo = billvo.getParentVO();
		String material = bvos[lineNum].getPk_material();
		String pk_customer = hvo.getPk_customer();
		UFDate date = hvo.getSubscribedate();
		Integer ninvctlstyle = getNinvctlstyle(billvo);

		if (StringUtil.isEmptyTrimSpace(sendcountry)
				|| StringUtil.isEmptyTrimSpace(rececountry)
				|| StringUtil.isEmptyTrimSpace(taxcountry)
				|| StringUtil.isEmptyTrimSpace(pk_customer)
				|| this.isNotStandard(material, ninvctlstyle)) {
			this.cleanTaxItemValue(billvo, lineNum);
			return;

		}
		VATInfoQueryVO query = new VATInfoQueryVO(taxcountry, buysell,
				UFBoolean.valueOf(triatrade.booleanValue()), sendcountry,
				rececountry, pk_customer, material, date);
		VATInfoVO[] info = VATBDService
				.queryCustVATInfo(new VATInfoQueryVO[] { query });

		if (null != info && info.length > 0 && info[0] != null) {
			bvos[lineNum].setCtaxcodeid(info[0].getCtaxcodeid());
			bvos[lineNum].setFtaxtypeflag(info[0].getFtaxtypeflag());
			bvos[lineNum].setNtaxrate(info[0].getNtaxrate());
			SaleRelationCalculate cal = new SaleRelationCalculate();
			cal.calculate(billvo, CtAbstractBVO.NTAXRATE);
		} else {
			this.cleanTaxItemValue(billvo, lineNum);
		}
	}

	private Integer getNinvctlstyle(AggCtSaleVO billvo) {
		CtSaleVO hvo = billvo.getParentVO();
		// 物料控制方式
		Integer ninvctlstyle = hvo.getNinvctlstyle();

		if (null != ninvctlstyle) {
			return ninvctlstyle;
		}
		// 合同类型
		String ctrantypeid = hvo.getCtrantypeid();
		if (StringUtil.isEmptyTrimSpace(ctrantypeid)) {
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

	private void recal(AggCtSaleVO billvo, int line) {
		boolean isTaxPrior = RelationCalculateUtil.isTaxPrior(billvo
				.getParentVO().getPk_group());
		SaleRelationCalculate cal = new SaleRelationCalculate();
		if (isTaxPrior) {
			cal.calculate(billvo, CtAbstractBVO.NQTORIGTAXPRICE);
		} else {
			cal.calculate(billvo, CtAbstractBVO.NQTORIGPRICE);
		}
	}
}
