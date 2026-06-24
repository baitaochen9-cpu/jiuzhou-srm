package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.Map;

import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.util.CtTransBusitypes;
import nccloud.commons.lang.StringUtils;
import nccloud.dto.ct.saledaily.utils.SalePayTermUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同付款协议编辑后
 * @author wangshrc
 * @date 2019年2月14日 上午10:41:48
 * @version ncc1.0
 */
public class SaleDailyPayTermAfterRule implements IHeadAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "rawtypes" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		String ctrantypeid = billvo.getParentVO().getCtrantypeid();
		if(StringUtils.isNotBlank(ctrantypeid)) {
			// 获取交易类型
			BusinessSetVO businessvo = CtTransBusitypes
					.getBusinessSetVO(ctrantypeid);
			// 页签表头初始化数据
			SalePayTermUtil.setValue(businessvo, billvo);
		}
		return billvo;
	}

}
