package nc.bs.jzyy.sys.oa.saledaily.fill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.payterm.PaytermService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.ui.ct.util.SalePayTermUtil;
import nc.vo.bd.income.IncomeChVO;
import nc.vo.bd.income.IncomeVO;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.entity.CtAbstractPayTermVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CtTransBusitypes;
import nc.vo.pub.lang.UFBoolean;

/**
 * 销售合同 设置收款协议
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-26 下午4:01:59   
 * @version NCC1909
 */
public class SalePayTerm {

	public void setPayTermInfo(AggCtSaleVO aggvo){
		CtSaleVO parentVO = aggvo.getParentVO();
		String ctrantypeid = parentVO.getCtrantypeid();
	    // 获取交易类型
	    BusinessSetVO businessvo = CtTransBusitypes.getBusinessSetVO(ctrantypeid);
	    // 设置页签是否显示
	    if (SalePayTermUtil.isShowTab(businessvo)) {
	        // 页签表头初始化数据
	        this.setPatTermValue(businessvo, aggvo);
	    }
	    else {
	        aggvo.setCtSalePayTermVO(null);
	    }
	}

	private void setPatTermValue(BusinessSetVO businessvo, AggCtSaleVO aggvo) {
		CtSaleVO parentVO = aggvo.getParentVO();
		if (businessvo != null
	         && businessvo.getBshowpayterm() != null
	         && UFBoolean.TRUE.booleanValue() == businessvo.getBshowpayterm().booleanValue()) {
			IncomeChVO[] incomeChVOs = this.getIncomeChVO(parentVO.getPk_payterm());
	        if (incomeChVOs != null) {
	          String pk_org = parentVO.getPk_org();
	          String pk_group = parentVO.getPk_group();
	          String pk_org_v = null;
	          if (!ValueUtil.isEmpty(pk_org)) {
	            pk_org_v = OrgUnitPubService.getOrgVid(pk_org);
	          }
	        
	          List<CtSalePayTermVO> termVOList = new ArrayList<CtSalePayTermVO>();
	          for (IncomeChVO incomeChVO : incomeChVOs) {
	        	CtSalePayTermVO termVO = new CtSalePayTermVO();
	            String[] attrName = incomeChVO.getAttributeNames();
	            for (int b = 0; b < attrName.length; b++) {
	              if (this.check(attrName[b])) {
	                Object obj = incomeChVO.getAttributeValue(attrName[b]);
	                termVO.setAttributeValue(attrName[b], obj);
	              }
	            }
	            termVO.setAttributeValue(CtAbstractPayTermVO.PK_ORG, pk_org);
	            termVO.setAttributeValue(CtAbstractPayTermVO.PK_ORG_V, pk_org_v);
	            termVO.setAttributeValue(CtAbstractPayTermVO.PK_GROUP, pk_group);
	            termVOList.add(termVO);
	          }
	        aggvo.setCtSalePayTermVO(termVOList.toArray(new CtSalePayTermVO[termVOList.size()]));
	      }
	    }
		
	}
	
	private IncomeChVO[] getIncomeChVO(String pk_income) {
	    if (pk_income != null) {
	      Map<String, IncomeVO> map = PaytermService.queryIncomeByPk(new String[] {
	        pk_income
	      });
	      IncomeChVO[] incomeChVOs = map.get(pk_income).getPaymentch();
	      return incomeChVOs;
	    }
	    return null;
	  }
	
	private boolean check(String name) {
	    String items[] =
	        new String[] {
	          CtAbstractPayTermVO.SHOWORDER, CtAbstractPayTermVO.ACCRATE,
	          CtAbstractPayTermVO.PREPAYMENT, CtAbstractPayTermVO.PK_INCOMEPERIOD,
	          CtAbstractPayTermVO.EFFECTDATEADDDATE,
	          CtAbstractPayTermVO.PAYMENTDAY, CtAbstractPayTermVO.CHECKDATA,
	          CtAbstractPayTermVO.EFFECTMONTH, CtAbstractPayTermVO.EFFECTADDMONTH,
	          CtAbstractPayTermVO.PK_BALATYPE, CtAbstractPayTermVO.ISDEPOSIT,
	          CtAbstractPayTermVO.PK_RATE, CtAbstractPayTermVO.ACCOUNTDAY
	        };

	    for (String item : items) {
	      if (item.equals(name)) {
	        return true;
	      }
	    }
	    return false;
	  }
}
