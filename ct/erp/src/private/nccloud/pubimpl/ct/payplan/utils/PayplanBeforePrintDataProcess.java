package nccloud.pubimpl.ct.payplan.utils;

import nc.impl.scmf.payplan.rule.PayPlanScaleRule;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.scale.BillVOScaleProcessor;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.pubapp.scale.TotalValueVOScaleProcessor;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nccloud.pubitf.scmpub.pub.print.BaseMetaPrintService.IBeforePrintDataProcess;

/**
 * @description 打印前处理（精度处理）
 * @author xiahui
 * @date 创建时间：2019-3-8 下午3:15:45
 * @version ncc1.0
 * @ref nc.ui.scmf.payplan.action.PayPlanPrintProcesser
 **/
public class PayplanBeforePrintDataProcess implements IBeforePrintDataProcess {

	@Override
	public Object[] processData(Object[] datas) {
		AggregatedValueObject[] bills = new AggregatedValueObject[datas.length];
		for (int i = 0; i < datas.length; i++) {
			bills[i] = (AggregatedValueObject) datas[i];
		}

		String pk_group = AppContext.getInstance().getPkGroup();
		BillVOScaleProcessor scale = new BillVOScaleProcessor(pk_group, bills);
		TotalValueVOScaleProcessor totalScale = new TotalValueVOScaleProcessor(bills);

		new PayPlanScaleRule().setScale(scale);

		for (AggregatedValueObject aggPlanVO : bills) {
			CircularlyAccessibleValueObject[] bodyvos = aggPlanVO.getChildrenVO();
			for (CircularlyAccessibleValueObject body : bodyvos) {
				body.setAttributeValue(AbstractPayPlanVO.NRATE,
						ScaleUtils.adjustScale((UFDouble) body.getAttributeValue(AbstractPayPlanVO.NRATE), 2));
			}
		}

		return datas;
	}

}
