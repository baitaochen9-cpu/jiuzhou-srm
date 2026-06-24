package nc.bs.jzyy.sys.oa.saledaily.rule;

import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.StringUtils;

/**
 * 设置表体销售国相关数据
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:28:19   
 * @version NCC1909
 */
public class ChooseBodySaleCountry  extends CtFieldEvent{

	private AggCtSaleVO[] paramArrayOfE = null;
	public ChooseBodySaleCountry(AggCtSaleVO[] paramArrayOfE){
		this.paramArrayOfE = paramArrayOfE;
	}

	public void process() {
		for(AggCtSaleVO aggvo : paramArrayOfE){
			//根据物料设置信息
			this.setDateWithSaleCountry(aggvo);
		}
	}

	private void setDateWithSaleCountry(AggCtSaleVO aggvo) {
		CtSaleVO parentVO = aggvo.getParentVO();
		CtSaleBVO[] ctSaleBVO = aggvo.getCtSaleBVO();
		for(CtSaleBVO bvo : ctSaleBVO){
			String country = bvo.getCtaxcountryid();
		    if (StringUtils.isNotBlank(country)) {
		       this.setValue(parentVO, bvo);
		    }
		}
	}
	
	public void setValue(CtSaleVO parentVO, CtSaleBVO bvo) {
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

	    this.setTaxInfo(parentVO, bvo, sendcountry, rececountry, taxcountry,
	        buysell, triatrade);
	  }
	
	private void setTaxInfo(CtSaleVO parentVO, CtSaleBVO bvo,
		      String sendcountry, String rececountry, String taxcountry,
		      BuySellFlagEnum buysell, Boolean triatrade) {
		    String material = bvo.getPk_material();

		    String pk_customer = parentVO.getPk_customer();

		    UFDate date = parentVO.getSubscribedate();
		    Integer ninvctlstyle = parentVO.getNinvctlstyle();

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
	  }
	
	private void cleanTaxItemValue(CtSaleBVO bvo) {
		bvo.setAttributeValue(CTVatNameConst.CTAXCODEID, null);
    	bvo.setAttributeValue(CtAbstractBVO.FTAXTYPEFLAG, null);
        bvo.setAttributeValue(CtAbstractBVO.NTAXRATE, null);
	  }
	
	private boolean isNotStandard(String material, Integer ninvctlstyle) {
	    if (Ninvctlstyle.MATERIAL.value().equals(ninvctlstyle)
	        && StringUtils.isBlank(material)) {
	      return true;
	    }

	    return false;
	  }


}
