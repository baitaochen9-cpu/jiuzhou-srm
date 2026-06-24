package nc.bs.jzyy.sys.oa.saledaily.fill;

import nc.api.rest.ct.utils.CTRelationCalculateUtil;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseBodyCustMaterialEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseBodyFinanceorgEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseBodyMaterialEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseBodySaleCountry;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseBodyTaxCode;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseHeadCustomerEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseHeadPersionEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChoosePayTermEvent;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.scmpub.res.billtype.CTBillType;

/**
 * 销售合同修改、变更填充其他字段数据
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:29:43   
 * @version NCC1909
 */
public class SaledailyUpdateDefValue {

public AggCtSaleVO[] setDefultValue(AggCtSaleVO[] aggVOs){
		
		new ChooseHeadCustomerEvent(aggVOs).process();
		
		CtSaleVO[] hvos = new CtSaleVO[aggVOs.length];
		for(int i=0; i<aggVOs.length; i++){
			hvos[i] = aggVOs[i].getParentVO();
		}
		new ChooseHeadPersionEvent(hvos).process();
		
		new ChoosePayTermEvent(aggVOs).process();
		
		new ChooseBodyMaterialEvent(aggVOs).process();
			
		new ChooseBodyCustMaterialEvent(aggVOs).process();
		
		new ChooseBodyFinanceorgEvent(aggVOs).process();
		
		new ChooseBodySaleCountry(aggVOs).process();
		
		for(AggCtSaleVO aggvo : aggVOs){
			new ChooseBodyTaxCode(aggvo.getParentVO(), 
					aggvo.getCtSaleBVO(), 
					CTBillType.SaleDaily.getCode()).process();
		}
		
		//计算数量、单价、金额
		for(AggCtSaleVO aggvo : aggVOs){
			new CTRelationCalculateUtil().calculate(aggvo.getParentVO(), aggvo.getCtSaleBVO());
		}
		
		return aggVOs;
	}

	
}
