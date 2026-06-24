package nc.bs.jzyy.sys.oa.saledaily.rule;

import java.util.Map;

import nc.bs.jzyy.sys.oa.saledaily.fill.SaleCountry;
import nc.bs.jzyy.sys.oa.saledaily.fill.SaleCustMaterialInfo;
import nc.bs.jzyy.sys.oa.saledaily.fill.SaleDefaultAddress;
import nc.bs.jzyy.sys.oa.saledaily.fill.SalePayTerm;
import nc.itf.scmpub.reference.uap.bd.customer.CustomerPubService;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.vo.bd.cust.saleinfo.CustsaleVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ArrayUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

import com.google.gdata.util.common.base.StringUtil;

/**
 * 表头客户设置相关数据（不联动计算）
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:28:41   
 * @version NCC1909
 */
public class ChooseHeadCustomerEvent extends CtFieldEvent{

	private AggCtSaleVO[] aggVOs = null;
	
	public ChooseHeadCustomerEvent(AggCtSaleVO[] aggVOs){
		this.aggVOs = aggVOs;
	}

	public void process() {
		for(AggCtSaleVO aggvo : aggVOs){
			//根据客户设置信息
			this.setDateWithCustomer(aggvo);
			
			//根据表头设置税率相关信息
			this.setRateInfo(aggvo);
		}
		
	}
	
	/**
	 * 根据客户设置默认信息
	 * @param aggvo
	 */
	private void setDateWithCustomer(AggCtSaleVO aggvo) {
		CtSaleVO parentVO = aggvo.getParentVO();
		String pk_org = parentVO.getPk_org();
		String pk_customer = parentVO.getPk_customer();

		CustsaleVO cvendor = this.getCustomerInfo(pk_customer, pk_org);
	    if (cvendor == null) {
	    	aggvo.setCtSalePayTermVO(null);
	    }
	    else {
	      // 部门
	      if (!ValueUtil.isEmpty(cvendor.getRespdept())) {
	        String deptid = cvendor.getRespdept();
	        parentVO.setAttributeValue(CtAbstractVO.DEPID, deptid);
	        Map<String, String> vidMap =
	            DeptPubService.getLastVIDSByDeptIDS(new String[] {
	              deptid
	            });
	        parentVO.setAttributeValue(CtAbstractVO.DEPID_V, vidMap.get(deptid));
	      }
	      // 人员
	      if (!ValueUtil.isEmpty(cvendor.getRespperson())) {
	    	  parentVO.setAttributeValue(CtAbstractVO.PERSONNELID, cvendor.getRespperson());
	      }
	      // 交货地点
	      String defaultAddress = CustomerPubService.getDefaultAddress(pk_customer, pk_org);
	      if (!ValueUtil.isEmpty(defaultAddress)) {
	    	  parentVO.setAttributeValue(CtAbstractVO.DELIADDR, defaultAddress);
	      }
	      // 币种
	      if (!ValueUtil.isEmpty(cvendor.getCurrencydefault())) {
	        if (!cvendor.getCurrencydefault().equals(parentVO.getCorigcurrencyid())) {
	        	parentVO.setAttributeValue(CtAbstractVO.CORIGCURRENCYID, cvendor.getCurrencydefault());
//	        	new CBuyOrigcurrency().afterEdit(event);
	        }
	      }
	      // 收款协议
	      if (!ValueUtil.isEmpty(cvendor.getPaytermdefault())) {
	        if (!cvendor.getPaytermdefault().equals(parentVO.getPk_payterm())) {
	        	parentVO.setAttributeValue(CtAbstractVO.PK_PAYTERM, cvendor.getPaytermdefault());
	          new SalePayTerm().setPayTermInfo(aggvo);
	        }
	      }
	    }

	    // 客户改变，收货国家也相应变动
	    if (!ValueUtil.isEmpty(pk_customer)) {
	      SaleDefaultAddress defadd = new SaleDefaultAddress();
	      defadd.setBodyDefaultAddress(aggvo);

	      // 国家改变才进行后续处理
	      String crececountryid = CTVatUtil.getSaleRececountry(pk_customer);
	      this.changeCountry(aggvo, crececountryid);
	    }
	    // 设置客户物料信息
	    new SaleCustMaterialInfo().setCustMaterialInfo(aggvo);
        
	}	
	
	private CustsaleVO getCustomerInfo(String cvendorid, String pk_org) {
	    if (cvendorid == null) {
	      return null;
	    }
	    String[] pkArray = new String[] {
	      cvendorid
	    };
	    String[] feilds = new String[] {
	      CustsaleVO.RESPDEPT,// 专管部门
	      CustsaleVO.RESPPERSON,// 专管业务员
	      CustsaleVO.CURRENCYDEFAULT,// 默认交易币种
	      CustsaleVO.PAYTERMDEFAULT,// 默认首付款协议
	    };
	    CustsaleVO[] custSaleVO =
	        CustomerPubService.getCustSaleVO(pkArray, pk_org, feilds);

	    if (ArrayUtil.isEmpty(custSaleVO)) {
	      return null;
	    }
	    return custSaleVO != null ? custSaleVO[0] : null;
	}
	
	private void changeCountry(AggCtSaleVO aggvo, String crececountryid) {
		SaleCountry contry = new SaleCountry(CTVatNameConst.CRECECOUNTRYID);
		for(CtSaleBVO bvo : aggvo.getCtSaleBVO()){
			bvo.setAttributeValue(CTVatNameConst.CRECECOUNTRYID, crececountryid);
			contry.setValue(aggvo, bvo);
		}
	}
	
	private void setRateInfo(AggCtSaleVO aggvo) {
		  String pk_customer = aggvo.getParentVO().getPk_customer();
		  Integer ninvctlstyle = aggvo.getParentVO().getNinvctlstyle();
		  UFDate date = aggvo.getParentVO().getSubscribedate();
		  for(CtSaleBVO bvo : aggvo.getCtSaleBVO()){
			  String taxcountry = bvo.getCtaxcountryid();
			  String sendcountry = bvo.getCsendcountryid();
			  String rececountry = bvo.getCrececountryid();
			  if (null != ninvctlstyle && !StringUtil.isEmptyOrWhitespace(pk_customer)
					  && !StringUtil.isEmptyOrWhitespace(taxcountry)
					  && !StringUtil.isEmptyOrWhitespace(sendcountry)
					  && !StringUtil.isEmptyOrWhitespace(rececountry)) {
				  BuySellFlagEnum buysell =
				          CTVatUtil.getSaleBuySellFlag(rececountry, taxcountry);
				  Boolean triatrade =
				          CTVatUtil.getSaleTriatradeFlag(sendcountry, taxcountry, buysell);
				  VATInfoQueryVO query =
						  new VATInfoQueryVO(taxcountry, buysell, UFBoolean.valueOf(triatrade
								  .booleanValue()), sendcountry, rececountry, pk_customer, null,
								  date);
				  VATInfoVO[] info = VATBDService.queryCustVATInfo(new VATInfoQueryVO[] {
						  query
				  });
				  
				  if (null != info && info.length > 0 && null != info[0]) {
					  bvo.setAttributeValue(CTVatNameConst.CTAXCODEID, info[0].getCtaxcodeid());
					  bvo.setAttributeValue(CtAbstractBVO.FTAXTYPEFLAG, info[0].getFtaxtypeflag());
					  bvo.setAttributeValue(CTVatNameConst.NNOSUBTAXRATE, info[0].getNnosubtaxrate());
					  bvo.setAttributeValue(CtAbstractBVO.NTAXRATE, info[0].getNtaxrate());
				  }
			  }
		  }
			
	}

}
