package nc.bs.jzyy.sys.oa.saledaily.rule;

import nc.bs.jzyy.sys.oa.saledaily.fill.SalePayTerm;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;

/**
 * 表头收款协议相关数据处理
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:29:11   
 * @version NCC1909
 */
public class ChoosePayTermEvent extends CtFieldEvent {

	
	private AggCtSaleVO[] aggVOs = null;
	
	public ChoosePayTermEvent(AggCtSaleVO[] aggVOs){
		this.aggVOs = aggVOs;
	}
	@Override
	public void process() {
		for(AggCtSaleVO aggvo : aggVOs){
			String pk_payterm = aggvo.getParentVO().getPk_payterm();
			if(pk_payterm == null){
				continue;
			}
			//收付款协议设置信息
			this.setPayTermInfo(aggvo);
		}
	}
	
	/**
	 * 表头有付款协议要设置付款表体
	 * @param aggvo
	 */
	private void setPayTermInfo(AggCtSaleVO aggvo) {
		SalePayTerm payTerm = new SalePayTerm();
		payTerm.setPayTermInfo(aggvo);
	}


}
