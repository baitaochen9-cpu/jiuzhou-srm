package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.ValueUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

import nccloud.commons.lang.StringUtils;

/**
 * @description 如果单位，与辅单位相同，那么换算率，数量同步
 * @author xiahui
 * @date 创建时间：2019-1-22 上午11:30:07
 * @version ncc1.0
 **/
public class ChangeRateAfterRule implements IBodyAfterRule<AggCtPuVO> {

	private String key;
	private String orderkey;

	public ChangeRateAfterRule(String key) {
		this.key = key;
		this.orderkey = this.getKeyValue();
	}

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		// 当单位与报价单位相同 并且 换算率不相同时，设置换算率也要相等。
		String castUnitID = util.getBodyStringValue(row, CtAbstractBVO.CASTUNITID);
		String cqtUnitID = util.getBodyStringValue(row, CtAbstractBVO.CQTUNITID);

		Object keyValue = util.getBodyValue(row, this.key);

		// 换算率发动联动计算
		new RelationCalculate().calculate(util, new int[] { row }, this.key);

		if (StringUtils.equals(castUnitID, cqtUnitID)) {
			// 当key为换算率
			if (StringUtils.equals(this.key, CtAbstractBVO.VCHANGERATE)) {
				Object nastNum = util.getBodyValue(row, CtAbstractBVO.NASTNUM);
				util.setBodyValue(row, CtAbstractBVO.NQTUNITNUM, nastNum);
			} else {
				// 当key 为报价换算率
				Object nastNum = util.getBodyValue(row, CtAbstractBVO.NQTUNITNUM);
				util.setBodyValue(row, CtAbstractBVO.NASTNUM, nastNum);
			}

			util.setBodyValue(row, this.orderkey, keyValue);
		}

		return billvo;
	}

	private String getKeyValue() {
		if (ValueUtil.equals(this.key, CtAbstractBVO.VCHANGERATE)) {
			return CtAbstractBVO.VQTUNITRATE;
		}
		return CtAbstractBVO.VCHANGERATE;
	}

}
