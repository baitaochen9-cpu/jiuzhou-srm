package nc.bs.jzyy.sys.oa.saledaily.fill;

import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang.StringUtils;


/**
 * 销售合同处理国家信息
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-27 下午6:21:48   
 * @version NCC1909
 */
public class SaleCountry {

  private String countrykey;

  public SaleCountry(String countryKey) {
    this.setCountrykey(countryKey);
  }

  public void setValue(AggCtSaleVO aggvo, CtSaleBVO bvo) {
    String sendcountry = bvo.getCsendcountryid();
    String rececountry = bvo.getCrececountryid();
    String taxcountry = bvo.getCtaxcountryid();

    BuySellFlagEnum buysell =
        CTVatUtil.getSaleBuySellFlag(rececountry, taxcountry);
    if (null != buysell) {
      bvo.setAttributeValue(CTVatNameConst.FBUYSELLFLAG, buysell.value());
    }
    Boolean triatrade =
        CTVatUtil.getSaleTriatradeFlag(sendcountry, taxcountry, buysell);
    bvo.setAttributeValue(CTVatNameConst.BTRIATRADEFLAG, triatrade);

    this.setTaxInfo(aggvo, bvo, sendcountry, rececountry, taxcountry,
        buysell, triatrade);
  }

  private void cleanTaxItemValue(CtSaleBVO bvo) {
	  bvo.setAttributeValue(CTVatNameConst.CTAXCODEID, null);
	  bvo.setAttributeValue(CtAbstractBVO.FTAXTYPEFLAG, null);
	  bvo.setAttributeValue(CTVatNameConst.CTAXCODEID, null);
	  bvo.setAttributeValue(CtAbstractBVO.NTAXRATE, null);
  }

  private boolean isNotStandard(String material, Integer ninvctlstyle) {
    if (Ninvctlstyle.MATERIAL.value().equals(ninvctlstyle)
        && StringUtils.isBlank(material)) {
      return true;
    }

    return false;
  }

  private void setTaxInfo(AggCtSaleVO aggvo, CtSaleBVO bvo, 
      String sendcountry, String rececountry, String taxcountry,
      BuySellFlagEnum buysell, Boolean triatrade) {
    String material = bvo.getPk_material();
    
    String pk_customer = aggvo.getParentVO().getPk_customer();
    
    UFDate date = aggvo.getParentVO().getSubscribedate();
    Integer ninvctlstyle = (Integer) getNinvctlstyle(aggvo.getParentVO());

    if (StringUtil.isEmptyTrimSpace(sendcountry)
        || StringUtil.isEmptyTrimSpace(rececountry)
        || StringUtil.isEmptyTrimSpace(taxcountry)
        || StringUtil.isEmptyTrimSpace(pk_customer)
        || this.isNotStandard(material, ninvctlstyle)) {
      this.cleanTaxItemValue(bvo);
      return;

    }
    VATInfoQueryVO query =
        new VATInfoQueryVO(taxcountry, buysell, UFBoolean.valueOf(triatrade
            .booleanValue()), sendcountry, rececountry, pk_customer, material,
            date);
    VATInfoVO[] info = VATBDService.queryCustVATInfo(new VATInfoQueryVO[] {
      query
    });

    if (null != info && info.length > 0 && info[0] != null) {
    	bvo.setAttributeValue(CTVatNameConst.CTAXCODEID, info[0].getCtaxcodeid());
    	bvo.setAttributeValue(CtAbstractBVO.FTAXTYPEFLAG, info[0].getFtaxtypeflag());
    	bvo.setAttributeValue(CtAbstractBVO.NTAXRATE, info[0].getNtaxrate());
    }
    else {
      this.cleanTaxItemValue(bvo);
    }
  }
  
  /**
	 * 物料控制方式
	 * @param parentVO
	 * @return
	 */
	private Object getNinvctlstyle(CtSaleVO parentVO) {
	    // 物料控制方式
	    Object ninvctlstyle = parentVO.getNinvctlstyle();

	    if (null != ninvctlstyle) {
	      return ninvctlstyle;
	    }
	    // 合同类型
	    String ctrantypeid = parentVO.getCtrantypeid();
	    if (StringUtil.isEmptyTrimSpace(ctrantypeid)) {
	      try {
	        IBusinessTypeService iBusiness =
	            (IBusinessTypeService) NCLocator.getInstance().lookup(
	                IBusinessTypeService.class.getName());
	        ninvctlstyle = iBusiness.queryMaterial(ctrantypeid);
	        // 物料控制方式
	        parentVO.setAttributeValue(CtAbstractVO.NINVCTLSTYLE, ninvctlstyle);
	      }
	      catch (Exception e1) {
	        ExceptionUtils.wrappException(e1);
	      }
	    }
	    return ninvctlstyle;
	  }

public String getCountrykey() {
	return countrykey;
}

public void setCountrykey(String countrykey) {
	this.countrykey = countrykey;
}

}
