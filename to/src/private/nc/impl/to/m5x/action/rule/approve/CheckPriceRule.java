package nc.impl.to.m5x.action.rule.approve;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.to.m5x.entity.BillItemVO;
import nc.vo.to.m5x.entity.BillVO;

public class CheckPriceRule implements IRule<BillVO>{

	@Override
	public void process(BillVO[] bills) {
		if(bills == null || bills.length < 1){
			return;
		}
		for(BillVO bill : bills){
			BillItemVO[] items = bill.getChildrenVO();
			for(BillItemVO item : items){
				UFDouble price = item.getNorignetprice();
				if(price == null || UFDouble.ZERO_DBL.compareTo(price) == 0){
					ExceptionUtils.wrappBusinessException("行号为【" + item.getCrowno() + "】的明细行单价为空，不允许审核。");
				}
			}
		}
	}

}
