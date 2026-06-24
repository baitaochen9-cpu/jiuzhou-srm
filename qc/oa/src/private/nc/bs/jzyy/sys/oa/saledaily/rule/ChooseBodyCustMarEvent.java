package nc.bs.jzyy.sys.oa.saledaily.rule;

import nc.bs.jzyy.sys.oa.saledaily.fill.SaleCustMaterialInfo;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;

/**
 * 根据表体客户物料设置信息
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:25:55   
 * @version NCC1909
 */
public class ChooseBodyCustMarEvent extends CtFieldEvent{

	private AggCtSaleVO[] aggVOs = null;
	
	public ChooseBodyCustMarEvent(AggCtSaleVO[] aggVOs){
		this.aggVOs = aggVOs;
	}

	
	@Override
	public void process() {
		for(AggCtSaleVO aggvo : aggVOs){
			new SaleCustMaterialInfo().setCustMaterialInfo(aggvo);
		}
		
	}

}
