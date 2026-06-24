package nccloud.pubimpl.ct.payplan.event.after.body;

import java.util.Map;

import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nccloud.dto.ct.pub.utils.ViewVOUtil;
import nccloud.dto.scmpub.pub.context.TableAfterEditEvent;
import nccloud.dto.scmpub.pub.event.rule.ITableAfterRule;

/**
 * @description 账期到期日
 * @author xiahui
 * @date 创建时间：2019-3-8 下午4:48:41
 * @version ncc1.0
 **/
public class DenddateBodyAfterEditRule implements ITableAfterRule<PayPlanViewVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public PayPlanViewVO[] afterEdit(PayPlanViewVO[] vo, TableAfterEditEvent event, Map userObj) {
		ViewVOUtil util = new ViewVOUtil(vo[event.getRow()]);

		UFDate denddate = util.getUFDateValue(AbstractPayPlanVO.DENDDATE);
		if (null == denddate) {
			util.setViewValue(AbstractPayPlanVO.IITERMDAYS, null);
			return vo;
		}
		UFDate dbegindate = util.getUFDateValue(AbstractPayPlanVO.DBEGINDATE);
		Integer days = util.getIntegerValue(AbstractPayPlanVO.IITERMDAYS);
		if (null == dbegindate && null == days) {
			return vo;
		}
		if (dbegindate != null) {
			int iitermdays = denddate.getDaysAfter(dbegindate);
			util.setViewValue(AbstractPayPlanVO.IITERMDAYS, Integer.valueOf(iitermdays));
		} else if (days != null) {
			UFDate begin = denddate.getDateBefore(days.intValue());
			util.setViewValue(AbstractPayPlanVO.DBEGINDATE, begin);
		}

		return vo;
	}

}
