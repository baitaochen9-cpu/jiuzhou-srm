package nc.bs.jzyy.sys.oa.saledaily.fill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nc.api.rest.ct.utils.NinvctlsTypeUtil;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * 表体有关税的处理  【222】
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:15:50   
 * @version NCC1909
 */
public class DealBodyVat {
	
	/**
	 * 处理VAT
	 * @param hvo
	 * @param bvos
	 */
	public void vat(CtAbstractVO hvo, CtAbstractBVO[] bvos) {
	    String pk_customer = null;
	    if(hvo instanceof CtSaleVO){
	    	CtSaleVO saleHvo = (CtSaleVO) hvo;
	    	pk_customer = saleHvo.getPk_customer();
	    }
	    HashMap<Integer, VATInfoQueryVO> querys =
	        this.getVatQuery(hvo, bvos, pk_customer);

	    this.setVatInfo(bvos, querys);

	  }
	
	private HashMap<Integer, VATInfoQueryVO> getVatQuery(CtAbstractVO hvo, CtAbstractBVO[] bvos, String pk_customer) {
	    UFDate date = hvo.getSubscribedate();
	    // 物料控制方式
	    Object ninvctlstyle = NinvctlsTypeUtil.getNinvctlstyle(hvo);
	    String rececountry = CTVatUtil.getSaleRececountry(pk_customer);
	    String sendcountry = hvo.getPk_org();
	    HashMap<Integer, VATInfoQueryVO> querys = new HashMap<Integer, VATInfoQueryVO>();
	    HashMap<String, String> taxcountryorg = new HashMap<String, String>();
	    for (CtAbstractBVO bvo : bvos) {
	    	bvo.setAttributeValue(CTVatNameConst.CRECECOUNTRYID, rececountry);
	    	bvo.setAttributeValue(CTVatNameConst.CSENDCOUNTRYID, sendcountry);
	        String pk_financeorg = bvo.getPk_financeorg();
	        String taxcountry = taxcountryorg.get(pk_financeorg);
	        if (StringUtil.isEmptyTrimSpace(taxcountry)) {
	          taxcountry =
	            CTVatUtil.getTaxCountry(bvo.getPk_financeorg());
	          taxcountryorg.put(pk_financeorg, taxcountry);
	        }
	        bvo.setAttributeValue(CTVatNameConst.CTAXCOUNTRYID, taxcountry);
	        BuySellFlagEnum buysell =
	            CTVatUtil.getSaleBuySellFlag(rececountry, taxcountry);
	        if (null != buysell) {
	        	bvo.setAttributeValue(CTVatNameConst.FBUYSELLFLAG, buysell.value());
	        }
	        Boolean triatrade =
	            CTVatUtil.getSaleTriatradeFlag(sendcountry, taxcountry, buysell);
	        bvo.setAttributeValue(CTVatNameConst.BTRIATRADEFLAG, triatrade);
	        String material = bvo.getPk_material();
	        if (StringUtil.isEmptyTrimSpace(sendcountry)
	          || StringUtil.isEmptyTrimSpace(rececountry)
	          || StringUtil.isEmptyTrimSpace(taxcountry)
	          || StringUtil.isEmptyTrimSpace(pk_customer)
	          || ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value())
	          && StringUtil.isEmptyTrimSpace(material)) {
	          continue;
	        }
	        VATInfoQueryVO query =
	          new VATInfoQueryVO(taxcountry, buysell, UFBoolean.valueOf(triatrade
	              .booleanValue()), sendcountry, rececountry, pk_customer,
	              material, date);
	        querys.put(Integer.valueOf(bvo.getCrowno()), query);
	    }
	    return querys;
	  }

	private void setVatInfo(CtAbstractBVO[] bvos, HashMap<Integer, VATInfoQueryVO> querys) {
		
		Map<VATInfoQueryVO, VATInfoVO> vats =
		        VATBDService.querySupplierVATInfoM(querys.values().toArray(new VATInfoQueryVO[querys.size()]));
		    ArrayList<Integer> rows = new ArrayList<Integer>();
		    for (Entry<Integer, VATInfoQueryVO> row : querys.entrySet()) {
		      VATInfoVO info = vats.get(row.getValue());
		      CtAbstractBVO bvo = null;
		      for (CtAbstractBVO item : bvos) {
		    	  if(Integer.valueOf(item.getCrowno()) == row.getKey().intValue()){
		    		  bvo = item;
		    		  break;
		    	  }
		      }
		      if (null != info
		          && !ValueUtil.equals(info.getCtaxcodeid(), bvo.getCtaxcodeid())) {
		        rows.add(row.getKey());
		        bvo.setAttributeValue(CTVatNameConst.CTAXCODEID, info.getCtaxcodeid());
		        bvo.setAttributeValue(CtAbstractBVO.FTAXTYPEFLAG, info.getFtaxtypeflag());
		        bvo.setAttributeValue(CTVatNameConst.NNOSUBTAXRATE, info.getNnosubtaxrate());
		        bvo.setAttributeValue(CtAbstractBVO.NTAXRATE, info.getNtaxrate());
		      }
		    }
	  }

}
