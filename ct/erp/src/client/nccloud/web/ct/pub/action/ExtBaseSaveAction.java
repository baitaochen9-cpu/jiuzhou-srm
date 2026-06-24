package nccloud.web.ct.pub.action;

import nc.vo.pub.BusinessException;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.util.GridCompareUtils;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.web.ct.purdaily.utils.PrecisionUtil;
import nccloud.web.scmpub.pub.operator.SCMExtBillCardOperator;

/**
 * @description 一主多子基础保存
 * @author xiahui
 * @date 2018-8-8 下午3:39:40
 * @version ncc1.0
 */
public abstract class ExtBaseSaveAction<T> extends BaseAction {

	@Override
	public Object excute(IRequest request) {
		try {
			SCMExtBillCardOperator operator = new SCMExtBillCardOperator();
			T bill = operator.toBill(request);
			// 获取原始Card
			ExtBillCard orignCard = operator.getOriginalCard();
			// 执行具体逻辑
			T ret = this.excute(bill);
			// 转换
			ExtBillCard retCard = operator.toNoTransCard(ret);
			// 执行精度处理
			this.afterProcess(retCard);
			// 差异比较(解决网络传输过程中，压缩解压缩效率问题)
			ExtBillCard finalBillCard = GridCompareUtils.compareExtBillCardGrid(orignCard, retCard);
			operator.translate(finalBillCard);
			return finalBillCard;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 
	 * 覆写该方法执行具体逻辑
	 * 
	 * @param bill
	 * @return
	 * @throws BusinessException
	 * 
	 */
	public abstract T excute(T bill);

	/**
	 * 
	 * 精度处理方式
	 * 
	 * @param retCard
	 * 
	 */
	protected void afterProcess(ExtBillCard retCard) {
	}
}
