package nccloud.pubimpl.ct.payplan.event;

import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractTableAfterHandler;
import nccloud.dto.scmpub.pub.event.rule.ITableAfterRule;
import nccloud.pubimpl.ct.payplan.event.after.body.DbegindateBodyAfterEditRule;
import nccloud.pubimpl.ct.payplan.event.after.body.DenddateBodyAfterEditRule;
import nccloud.pubimpl.ct.payplan.event.after.body.IitermdaysBodyAfterEditRule;
import nccloud.pubimpl.ct.payplan.event.after.body.NorigmnyBodyAfterEditRule;
import nccloud.pubimpl.ct.payplan.event.after.body.NrateBodyAfterEditRule;

/**
 * @description 表体编辑后事件
 * @author xiahui
 * @date 创建时间：2019-3-8 下午2:55:18
 * @version ncc1.0
 **/
public class BodyAfterEventHandler extends AbstractTableAfterHandler {
	
	@Override
	protected ITableAfterRule<PayPlanViewVO> getAfterRule(String changeKey) {
		ITableAfterRule<PayPlanViewVO> rule = null;
		
		if(AbstractPayPlanVO.DENDDATE.equals(changeKey)) {
			rule = new DenddateBodyAfterEditRule();
		}else if(AbstractPayPlanVO.DBEGINDATE.equals(changeKey)) {
			rule = new DbegindateBodyAfterEditRule();
		}else if(AbstractPayPlanVO.IITERMDAYS.equals(changeKey)) {
			rule = new IitermdaysBodyAfterEditRule();
		}else if(AbstractPayPlanVO.NORIGMNY.equals(changeKey)) {
			rule = new NorigmnyBodyAfterEditRule();
		}else if(AbstractPayPlanVO.NRATE.equals(changeKey)) {
			rule = new NrateBodyAfterEditRule();
		}

		return rule;
	}
}
