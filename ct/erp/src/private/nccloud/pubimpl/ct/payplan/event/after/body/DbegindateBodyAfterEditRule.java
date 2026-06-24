package nccloud.pubimpl.ct.payplan.event.after.body;

import java.util.Map;

import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nccloud.dto.ct.pub.utils.ViewVOUtil;
import nccloud.dto.scmpub.pub.context.TableAfterEditEvent;
import nccloud.dto.scmpub.pub.event.rule.ITableAfterRule;

/**
 * @description 起算日期
 * @author xiahui
 * @date 创建时间：2019-3-8 下午4:48:56
 * @version ncc1.0
 **/
public class DbegindateBodyAfterEditRule implements ITableAfterRule<PayPlanViewVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public PayPlanViewVO[] afterEdit(PayPlanViewVO[] vo, TableAfterEditEvent event, Map userObj) {
		ViewVOUtil util = new ViewVOUtil(vo[event.getRow()]);

		Object dbegindate = event.getChangedRows()[0].getNewvalue();
		if (null == dbegindate) {
			util.setViewValue(AbstractPayPlanVO.IITERMDAYS, null);
			util.setViewValue(AbstractPayPlanVO.DENDDATE, null);
			return vo;
		}

		Integer iitermdays = util.getIntegerValue(AbstractPayPlanVO.IITERMDAYS);
		if (null == iitermdays) {
			return vo;
		}

		UFDate denddate = new UFDate(dbegindate.toString()).getDateAfter(iitermdays.intValue());
		util.setViewValue(AbstractPayPlanVO.DENDDATE, denddate);

		return vo;
	}

}
