package nccloud.pubimpl.ct.payplan.event.after.body;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nccloud.dto.ct.pub.utils.ViewVOUtil;
import nccloud.dto.scmpub.pub.context.TableAfterEditEvent;
import nccloud.dto.scmpub.pub.event.rule.ITableAfterRule;
import nccloud.dto.to.pub.constance.MsgFlag;
import nccloud.pubimpl.ct.payplan.utils.PayPlanRateCalcByMny;

/**
 * @description 原币金额
 * @author xiahui
 * @date 创建时间：2019-3-8 下午4:49:15
 * @version ncc1.0
 **/
public class NorigmnyBodyAfterEditRule implements ITableAfterRule<PayPlanViewVO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PayPlanViewVO[] afterEdit(PayPlanViewVO[] vo, TableAfterEditEvent event, Map userObj) {
		ViewVOUtil util = new ViewVOUtil(vo[event.getRow()]);

		UFDouble norigmny = ValueUtils.getUFDouble(event.getChangedRows()[0].getNewvalue());
		UFDouble oldNorigmny = ValueUtils.getUFDouble(event.getChangedRows()[0].getOldvalue());
		
		UFDouble ntotalorigmny = util.getUFDoubleValue(CtAbstractVO.NTOTALORIGMNY);
		if(MathTool.compareTo(ntotalorigmny, UFDouble.ZERO_DBL) == 0) {
			userObj.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0324")); // 合同{0}金额为零，不允许修改，请更改合同金额后操作！
			util.setViewValue(AbstractPayPlanVO.NORIGMNY, oldNorigmny);
			return vo;
		}
		
		if (null == norigmny) {
			userObj.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0037")); // 金额不能为空
			util.setViewValue(AbstractPayPlanVO.NORIGMNY, oldNorigmny);
			return vo;
		}
		if (MathTool.compareTo(norigmny, UFDouble.ZERO_DBL) <= 0) {
			userObj.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0038")); // 金额必须大于0
			util.setViewValue(AbstractPayPlanVO.NORIGMNY, oldNorigmny);
			return vo;
		}
		// 631新增-付款计划的金额不可小于累计付款金额、累计付款申请金额
		UFDouble naccumpayapporgmny = util.getUFDoubleValue(AbstractPayPlanVO.NACCUMPAYAPPORGMNY);

		if (MathTool.compareTo(norigmny, naccumpayapporgmny) < 0) {
			userObj.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0334")); // 金额不可以小于累计付款申请金额
			util.setViewValue(AbstractPayPlanVO.NORIGMNY, oldNorigmny);
			return vo;
		}
		UFDouble naccumpayorgmny = util.getUFDoubleValue(AbstractPayPlanVO.NACCUMPAYORGMNY);
		if (MathTool.compareTo(norigmny, naccumpayorgmny) < 0) {
			userObj.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0335")); // 金额不可以小于累计付款金额
			util.setViewValue(AbstractPayPlanVO.NORIGMNY, oldNorigmny);
			return vo;
		}
		
		// 比率联动
		new PayPlanRateCalcByMny(util,  userObj).calcMnyByRate(norigmny);

		return vo;
	}
}
