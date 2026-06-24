package nc.bs.jzyy.sys.oa.saledaily.fill;


import nc.api.rest.ct.utils.CTRelationCalculateUtil;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseBodyMaterialEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseHeadCustomerEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseHeadOrgEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChooseHeadPersionEvent;
import nc.bs.jzyy.sys.oa.saledaily.rule.ChoosePayTermEvent;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;

/**
 * 
 * @Description: 销售合同新增根据必输项填充其他字段数据
 *   
 * @author: 刘伟
 * @date:   2019-4-25 上午10:21:53   
 * @version NCC1909
 */
public class SaledailySaveDefValue {
	
	
	public AggCtSaleVO[] setDefultValue(AggCtSaleVO[] aggVOs){
		
		new ChooseHeadOrgEvent(aggVOs).process();
		
		new ChooseHeadCustomerEvent(aggVOs).process();
		
		CtSaleVO[] hvos = new CtSaleVO[aggVOs.length];
		for(int i=0; i<aggVOs.length; i++){
			hvos[i] = aggVOs[i].getParentVO();
		}
		new ChooseHeadPersionEvent(hvos).process();
		
		new ChoosePayTermEvent(aggVOs).process();
		
		new ChooseBodyMaterialEvent(aggVOs).process();
			
		//计算数量、单价、金额
		for(AggCtSaleVO aggvo : aggVOs){
			new CTRelationCalculateUtil().calculate(aggvo.getParentVO(), aggvo.getCtSaleBVO());
		}
		
		return aggVOs;
	}


}
