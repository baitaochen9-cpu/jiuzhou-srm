package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.Map;

import nc.vo.ct.rule.SaleRelationCalculate;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.UFDouble;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同表头汇率编辑后
 * @author wangshrc
 * @date 2019年2月14日 上午9:29:45
 * @version ncc1.0
 */
public class SaleDailyChangeRateAfterRule implements
		IHeadAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		String rateItem = (String) event.getNewValue();
		if (ValueUtil.isEmpty(rateItem)) {
			return billvo;
		}
		UFDouble oldRate = null;
		UFDouble obj = null;
		if(event.getOldValue()!=null) {
			 oldRate =new UFDouble(event.getOldValue().toString());
		} 
		if(event.getNewValue()!=null) {
			 obj = new UFDouble(event.getNewValue().toString()) ;
		}
		if (ValueUtil.isEmpty(obj) || UFDouble.ZERO_DBL.compareTo(obj) > 0
				|| ValueUtil.equals(obj, UFDouble.ZERO_DBL)) {
			if (!ValueUtil.isEmpty(oldRate)) {
				if (CtSaleVO.NEXCHANGERATE.equals(event.getChangeKey()))
					billvo.getParentVO().setNexchangerate(oldRate);
				if (CtSaleVO.NGLOBALEXCHGRATE.equals(event.getChangeKey()))
					billvo.getParentVO().setNglobalexchgrate(oldRate);
				if (CtSaleVO.NGROUPEXCHGRATE.equals(event.getChangeKey()))
					billvo.getParentVO().setNgroupexchgrate(oldRate);
			}
			userobject.put("errMsg", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("4020003_0", "04020003-0042"));
			return billvo;
		}
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		int[] lines = new int[bvos.length];
		for (int i = 0; i < lines.length; i++) {
			lines[i] = 1;
		}
		// 联动计算表体单价金额字段
		SaleRelationCalculate cal = new SaleRelationCalculate();
		cal.calculate(billvo, event.getChangeKey());
		return billvo;
	}

}
