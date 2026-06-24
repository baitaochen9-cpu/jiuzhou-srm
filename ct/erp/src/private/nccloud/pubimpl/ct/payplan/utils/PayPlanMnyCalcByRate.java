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

/**
 * @description 采购合同付款计划联动（根据比率联动金额）
 * @author xiahui
 * @date 创建时间：2019-3-12 上午10:58:33
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.rule.PayPlanMnyCalcByRate
 **/
public class PayPlanMnyCalcByRate {
	public static final UFDouble UF100 = new UFDouble(100);

	private UFDouble accMny = UFDouble.ZERO_DBL;

	private UFDouble accOrigMny = UFDouble.ZERO_DBL;

	private UFDouble accRate = UFDouble.ZERO_DBL;

	private String ccurrencyid;

	private String corigcurrencyid;

	private UFDouble ntotallocalmny = UFDouble.ZERO_DBL;

	private UFDouble ntotalorigmny = UFDouble.ZERO_DBL;

	private ViewVOUtil util;

	@SuppressWarnings("rawtypes")
	private Map userObj;

	@SuppressWarnings({ "rawtypes" })
	public PayPlanMnyCalcByRate(ViewVOUtil util, Map userObj) {
		this.util = util;
		this.userObj = userObj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void calcMnyByRate(UFDouble nrate) {
		this.init(nrate);
		int orgDigit = new ScaleObjectFactory.CurrtypeScaleObject(2, 4).getDigit(this.corigcurrencyid);
		int digit = new ScaleObjectFactory.CurrtypeScaleObject(2, 4).getDigit(this.ccurrencyid);
		UFDouble norigmny = null;
		UFDouble nmny = null;

		if (null == this.ntotalorigmny || UFDouble.ZERO_DBL.equals(this.ntotalorigmny) || null == this.ntotallocalmny) {
			return;
		}
		if (MathTool.compareTo(this.accRate, PayPlanMnyCalcByRate.UF100) == 0) {
			norigmny = MathTool.sub(this.ntotalorigmny, this.accOrigMny);
			nmny = MathTool.sub(this.ntotallocalmny, this.accMny);
		} else {
			norigmny = this.ntotalorigmny.multiply(nrate.div(PayPlanMnyCalcByRate.UF100, UFDouble.DEFAULT_POWER), orgDigit);
			nmny = this.ntotallocalmny.multiply(nrate.div(PayPlanMnyCalcByRate.UF100, UFDouble.DEFAULT_POWER), digit);
		}

		util.setViewValue(AbstractPayPlanVO.NORIGMNY, norigmny);
		util.setViewValue(AbstractPayPlanVO.NMNY, nmny);
		for (Map data : (List<Map>) userObj.get("rows")) {

			UFDouble rate = PayPlanCalcUtil.getUFDouble(data, AbstractPayPlanVO.NRATE);

			if (rate != null) {
				PayPlanCalcUtil.setValue(data, AbstractPayPlanVO.NORIGMNY,
						this.ntotalorigmny.multiply(rate.div(PayPlanMnyCalcByRate.UF100, UFDouble.DEFAULT_POWER), orgDigit));
				PayPlanCalcUtil.setValue(data, AbstractPayPlanVO.NMNY,
						this.ntotallocalmny.multiply(rate.div(PayPlanMnyCalcByRate.UF100, UFDouble.DEFAULT_POWER), digit));
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void init(UFDouble nrate) {
		for (Map data : (List<Map>) userObj.get("rows")) {
			UFDouble irate = PayPlanCalcUtil.getUFDouble(data, AbstractPayPlanVO.NRATE);
			this.accRate = MathTool.add(this.accRate, irate);
			UFDouble inorigmny = PayPlanCalcUtil.getUFDouble(data, AbstractPayPlanVO.NORIGMNY);
			this.accOrigMny = MathTool.add(this.accOrigMny, inorigmny);
			UFDouble inmny = PayPlanCalcUtil.getUFDouble(data, AbstractPayPlanVO.NMNY);
			this.accMny = MathTool.add(this.accMny, inmny);
		}
		// 加上操作行
		this.accRate = MathTool.add(this.accRate, nrate);
		
		String pk_ct_pu = util.getStringValue(PayPlanVO.PK_CT_PU);
		try {
			IPurdailyMaintain iservice = NCLocator.getInstance().lookup(IPurdailyMaintain.class);
			AggCtPuVO[] aggvo = iservice.queryCtPuVoByIds(new String[] { pk_ct_pu });
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
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

	}
}
