package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.uitl.StringUtil;
import nccloud.dto.ct.saledaily.utils.UnitAndChangeRateUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 单位编辑后
 * @author wangshrc
 * @date 2019年2月14日 下午2:45:00
 * @version ncc1.0
 */
public class SaleDailyCastUnitAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		if (event.getChangrows().length <= 0)
			return billvo;
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		String astUnit = bvos[event.getRow()].getCastunitid();
		if (StringUtil.isEmptyTrimSpace(astUnit)) {
			return billvo;
		}

		// 设置报价单位 从报价单位换算率发起联动计算
		bvos[event.getRow()].setCqtunitid(astUnit);
		UnitAndChangeRateUtil.setCqtChangeRate(billvo,
				new int[] { event.getRow() }); // 从换算率发起联动计算
		UnitAndChangeRateUtil.setChangeRate(billvo,
				new int[] { event.getRow() });
		return billvo;
	}

}
