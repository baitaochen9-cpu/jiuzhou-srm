package nccloud.pubimpl.ct.payplan.utils;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.PayPlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.scale.ScaleObjectFactory;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nccloud.dto.ct.pub.utils.ViewVOUtil;
import nccloud.dto.to.pub.constance.MsgFlag;

/**
 * @description 采购合同付款计划联动（根据金额联动比率）
 * @author xiahui
 * @date 创建时间：2019-3-11 下午4:59:17
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.rule.PayPlanRateCalcByMny
 **/
public class PayPlanRateCalcByMny {
	public static final UFDouble UF100 = new UFDouble(100);

	private UFDouble accMny = UFDouble.ZERO_DBL;

	private UFDouble accOrigMny = UFDouble.ZERO_DBL;

	private UFDouble accRate = UFDouble.ZERO_DBL;

	private String ccurrencyid;

	private String corigcurrencyid;

	private UFDouble ntempMny = UFDouble.ZERO_DBL;

	private UFDouble ntotallocalmny = UFDouble.ZERO_DBL;

	private UFDouble ntotalorigmny = UFDouble.ZERO_DBL;

	private ViewVOUtil util;

	@SuppressWarnings("rawtypes")
	private Map userObj;

	@SuppressWarnings({ "rawtypes" })
	public PayPlanRateCalcByMny(ViewVOUtil util, Map userObj) {
		this.util = util;
		this.userObj = userObj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void calcMnyByRate(UFDouble norigmny) {
		this.init(norigmny);
		int orgDigit = new ScaleObjectFactory.CurrtypeScaleObject(2, 4).getDigit(this.corigcurrencyid);
		UFDouble nrate = null;
		UFDouble nmny = null;

		if (null == norigmny || MathTool.compareTo(norigmny, UFDouble.ZERO_DBL) <= 0) {
			return;
		}
		if (0 == MathTool.compareTo(this.ntotalorigmny, this.accOrigMny)) {
			nrate = MathTool.sub(PayPlanRateCalcByMny.UF100, this.accRate);
			nmny = MathTool.sub(this.ntotallocalmny, this.accMny);
		} else {
			if (0 == MathTool.compareTo(this.ntotalorigmny, UFDouble.ZERO_DBL)) {
				userObj.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0335")); // 合同{0}金额为零，不允许修改，请更改合同金额后操作！
				return;
			}
			nrate = norigmny.div(this.ntotalorigmny, UFDouble.DEFAULT_POWER).multiply(PayPlanRateCalcByMny.UF100, orgDigit);
			nmny = this.ntempMny;
		}

		util.setViewValue(AbstractPayPlanVO.NRATE, nrate);
		util.setViewValue(AbstractPayPlanVO.NMNY, nmny);

		for (Map data : (List<Map>) userObj.get("rows")) {
			UFDouble origmny = PayPlanCalcUtil.getUFDouble(data, AbstractPayPlanVO.NORIGMNY);
			UFDouble rate = origmny.div(this.ntotalorigmny, UFDouble.DEFAULT_POWER).multiply(PayPlanRateCalcByMny.UF100,
					orgDigit);
			PayPlanCalcUtil.setValue(data, AbstractPayPlanVO.NRATE, rate);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void init(UFDouble norigmny) {
		for (Map data : (List<Map>) userObj.get("rows")) {
			UFDouble irate = PayPlanCalcUtil.getUFDouble(data, AbstractPayPlanVO.NRATE);
			this.accRate = MathTool.add(this.accRate, irate);
			UFDouble inorigmny = PayPlanCalcUtil.getUFDouble(data, AbstractPayPlanVO.NORIGMNY);
			this.accOrigMny = MathTool.add(this.accOrigMny, inorigmny);
			UFDouble inmny = PayPlanCalcUtil.getUFDouble(data, AbstractPayPlanVO.NMNY);
			this.accMny = MathTool.add(this.accMny, inmny);
		}
		// 加上操作行
		this.accOrigMny = MathTool.add(this.accOrigMny, norigmny);

		String pk_order = util.getStringValue(PayPlanVO.PK_CT_PU);
		try {
			IPurdailyMaintain iservice = NCLocator.getInstance().lookup(IPurdailyMaintain.class);
			AggCtPuVO[] aggvo = iservice.queryCtPuVoByIds(new String[] { pk_order });
			this.ntotalorigmny = aggvo[0].getParentVO().getNtotalorigmny();
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		
		this.corigcurrencyid = util.getStringValue(AbstractPayPlanVO.CORIGCURRENCYID);
		this.ccurrencyid = util.getStringValue(AbstractPayPlanVO.CCURRENCYID);
		UFDouble nexchangerate = util.getUFDoubleValue(AbstractPayPlanVO.NEXCHANGERATE);
		String pk_fiorg = util.getStringValue(AbstractPayPlanVO.PK_FINANCEORG);
		CurrencyRateUtil rateUtil = CurrencyRateUtil.getInstanceByOrg(pk_fiorg);
		try {
			this.ntotallocalmny = MathTool.nvl(rateUtil.getAmountByOpp(this.corigcurrencyid, this.ccurrencyid,
					this.ntotalorigmny, nexchangerate, new UFDate()));
			this.ntempMny = rateUtil.getAmountByOpp(this.corigcurrencyid, this.ccurrencyid, norigmny, nexchangerate,
					new UFDate());
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
	};
}
