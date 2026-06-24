package nccloud.pubimpl.ct.payplan.event.after.body;

import java.util.Map;

import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nccloud.dto.ct.pub.utils.ViewVOUtil;
import nccloud.dto.scmpub.pub.context.TableAfterEditEvent;
import nccloud.dto.scmpub.pub.event.rule.ITableAfterRule;
import nccloud.dto.to.pub.constance.MsgFlag;
import nccloud.pubimpl.ct.payplan.utils.PayPlanMnyCalcByRate;

/**
 * @description 比率
 * @author xiahui
 * @date 创建时间：2019-3-8 下午4:49:25
 * @version ncc1.0
 **/
public class NrateBodyAfterEditRule implements ITableAfterRule<PayPlanViewVO> {

	@SuppressWarnings("unchecked")
	@Override
	public PayPlanViewVO[] afterEdit(PayPlanViewVO[] vo, TableAfterEditEvent event,
			@SuppressWarnings("rawtypes") Map userObj) {
		ViewVOUtil util = new ViewVOUtil(vo[event.getRow()]);

		UFDouble nrate = ValueUtils.getUFDouble(event.getChangedRows()[0].getNewvalue());
		UFDouble oldNrate = ValueUtils.getUFDouble(event.getChangedRows()[0].getOldvalue());
		if (null == nrate) {
			userObj.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0039")); // 比率不能为空
			util.setViewValue(AbstractPayPlanVO.NRATE, oldNrate);
			return vo;
		}
		if (MathTool.compareTo(nrate, UFDouble.ZERO_DBL) < 0) {
			userObj.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0040")); // 比率必须大于0
			util.setViewValue(AbstractPayPlanVO.NRATE, oldNrate);
			return vo;
		}

		// 联动金额
		new PayPlanMnyCalcByRate(util, userObj).calcMnyByRate(nrate);

		return vo;
	}

}
