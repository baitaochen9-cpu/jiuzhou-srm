package nccloud.web.ct.saledaily.action;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleChangeVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.util.VORowNoUtils;
import nccloud.dto.ct.pub.transfer.TransferInfo;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.pubitf.ct.saledaily.service.ISaleDailyQueryForNCCloudService;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;

/**
 * @description 销售合同拉单
 * @author wangshrc
 * @date 2019年3月7日 下午2:24:33
 * @version ncc1.0
 */
public class SaleDailyTrans4310Action implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson json = JsonFactory.create();
		// 解析前台请求
		TransferInfo[] transferInfo = json.fromJson(read, TransferInfo[].class);
		ISaleDailyQueryForNCCloudService service = ServiceLocator.find(ISaleDailyQueryForNCCloudService.class);
		AggCtSaleVO[] result = null;
		try {
			result = service.transToSaleDaily(transferInfo);
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		if (null == result || result.length == 0) {
			return null;
		}
		for (AggCtSaleVO billVO : result) {
			// 设置表体变更历史页签
			this.setBodyChangeValue(billVO);
		}
		VORowNoUtils.setVOsRowNoByRule(result);
		// 处理为前台结构
		ExtBillCardOperator operator = new ExtBillCardOperator("400600200_card");
		ExtBillCard[] cards = new ExtBillCard[result.length];

		// 处理精度
		for (int i = 0; i < result.length; i++) {
			cards[i] = operator.toCard(result[i]);
			SaleDailyPrecisionUtil.dealPrecision(cards[i]);
		}
		return cards;
	}

	// 变更历史新增 原始版本数据
	private void setBodyChangeValue(AggCtSaleVO bill) {
		// 先清空变更表的数据
		bill.setCtSaleChangeVO(null);
		// 再set原始版本
		CtSaleChangeVO originChangeVO = new CtSaleChangeVO();
		originChangeVO.setPk_group(bill.getParentVO().getPk_group());
		originChangeVO.setPk_org(bill.getParentVO().getPk_group());
		originChangeVO.setPk_org_v(bill.getParentVO().getPk_org_v());
		originChangeVO.setVmemo(
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0005")/* @res "原始版本" */);
		originChangeVO.setVchangecode(UFDouble.ONE_DBL);
		bill.setCtSaleChangeVO(new CtSaleChangeVO[] { originChangeVO });
	}

}
