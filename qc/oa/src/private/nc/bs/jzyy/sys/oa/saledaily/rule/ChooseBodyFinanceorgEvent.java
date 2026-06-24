package nc.bs.jzyy.sys.oa.saledaily.rule;


import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;

import org.apache.commons.lang.StringUtils;

/**
 * 表体财务组织相关数据处理
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:27:36   
 * @version NCC1909
 */
public class ChooseBodyFinanceorgEvent extends CtFieldEvent {

	private AggCtSaleVO[] paramArrayOfE = null;
	
	public ChooseBodyFinanceorgEvent(AggCtSaleVO[] paramArrayOfE){
		this.paramArrayOfE = paramArrayOfE;
	}

	public void process() {
		for(AggCtSaleVO aggvo : paramArrayOfE){
			//根据表体财务组织设置信息
			this.setDateByFinanceorg(aggvo.getCtSaleBVO());
		}
		new ChooseBodySaleCountry(paramArrayOfE).process();
	}

	private void setDateByFinanceorg(CtSaleBVO[] ctSaleBVOs) {
		for(CtSaleBVO bvo : ctSaleBVOs){
			String ctaxcountryid = bvo.getPk_financeorg();
			if (StringUtils.isBlank(ctaxcountryid)) {
				return;
			}
			if (bvo.getCtaxcountryid() != null
					&& bvo.getCtaxcountryid().equals(ctaxcountryid)) {
				return;
			}
			bvo.setAttributeValue(CTVatNameConst.CTAXCOUNTRYID, ctaxcountryid);
		}
	}

}
