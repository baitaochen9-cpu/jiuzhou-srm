package nccloud.dto.ct.pub.utils;

import java.util.HashSet;

import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.BusinessType;
import nc.vo.ct.uitl.ObjectUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.data.IRelationForItems;
import nc.vo.pubapp.calculator.data.VODataSetForCal;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.scmpub.util.StringUtil;

/**
 * @description
 * @author xiahui
 * @date 创建时间：2019-1-22 上午9:28:59
 * @version ncc1.0
 **/
public class CtVODataSet extends VODataSetForCal {
	// 物料控制方式
	private Integer materialControl;

	private ExtBillUtil util;

	private HashSet<String> keys = new HashSet<String>();

	public CtVODataSet(ExtBillUtil util, int row, IRelationForItems item, Integer materialControl) {
		super(util.getBody(row), item);
		this.util = util;
		this.initKeys();
		if (this.materialControl == null) {
			String ctranstypeid = util.getHeadTailStringValue(CtAbstractVO.CTRANTYPEID);
			if (StringUtil.isEmptyTrimSpace(ctranstypeid)) {
				return;
			}
			this.materialControl = materialControl;
		}
	}

	@Override
	public UFDate getBillDate() {
		Object value = util.getHeadValue(CtAbstractVO.SUBSCRIBEDATE);
		return ValueUtils.getUFDate(value);
	}

	/**
	 * 因为数量精度根据单位取，但是合同交易类型中，有控制物料分类的交易类型， 对于这种交易类型，无法取出单位，无法进行计算， 父类方法重写
	 * 
	 */
	@Override
	public String getCastunitid() {
		// 物料控制方式不为空，并且 不为 物料时
		if (!ObjectUtil.isEmpty(this.materialControl) && this.materialControl.intValue() != BusinessType.MATERIAL) {
			return "sameunit";
		}
		return super.getCastunitid();
	}

	@Override
	public String getCcurrencyid() {
		return util.getHeadTailStringValue(CtAbstractVO.CCURRENCYID);
	}

	/** 获得原币币种 */
	@Override
	public String getCorigcurrencyid() {
		return util.getHeadTailStringValue(CtAbstractVO.CORIGCURRENCYID);
	}

	@Override
	public String getCqtunitid() {
		if (!ObjectUtil.isEmpty(this.materialControl) && this.materialControl.intValue() != BusinessType.MATERIAL) {
			return "sameunit";
		}
		return super.getCqtunitid();
	}

	@Override
	public String getCunitid() {
		if (!ObjectUtil.isEmpty(this.materialControl) && this.materialControl.intValue() != BusinessType.MATERIAL) {
			return "sameunit";
		}
		return super.getCunitid();
	}

	@Override
	public UFDouble getNexchangerate() {
		Object nexchangerate = util.getHeadValue(CtAbstractVO.NEXCHANGERATE);
		return ValueUtils.getUFDouble(nexchangerate);
	}

	@Override
	public UFDouble getNglobalexchgrate() {
		Object nchangerate = util.getHeadValue(CtAbstractVO.NGLOBALEXCHGRATE);
		return ValueUtils.getUFDouble(nchangerate);
	}

	@Override
	public UFDouble getNgroupexchgrate() {
		Object nchangerate = util.getHeadValue(CtAbstractVO.NGROUPEXCHGRATE);
		return ValueUtils.getUFDouble(nchangerate);
	}

	@Override
	public boolean hasItemKey(String key) {
		if (this.keys.contains(key)) {
			return true;
		}
		return super.hasItemKey(key);
	}

	private void initKeys() {
		// 发票日期
		this.keys.add(CtAbstractVO.SUBSCRIBEDATE);
		// 汇率
		this.keys.add(CtAbstractVO.NEXCHANGERATE);
		this.keys.add(CtAbstractVO.NGLOBALEXCHGRATE);
		this.keys.add(CtAbstractVO.NGROUPEXCHGRATE);
	}
}
