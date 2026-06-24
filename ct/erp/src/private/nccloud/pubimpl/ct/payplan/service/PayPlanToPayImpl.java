package nccloud.pubimpl.ct.payplan.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.arap.util.IArapBillTypeCons;
import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.ct.purdaily.entity.AggPayPlanVO;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.pubitf.ct.payplan.service.IPayPlanToPay;

import nccloud.commons.lang.StringUtils;

/**
 * 
 * @Description 付款计划推付款单实现
 * @author zhangshqb
 * @since 2018-6-24
 * @version V1.0
 * 
 */
public class PayPlanToPayImpl implements IPayPlanToPay {

	@Override
	public AggregatedValueObject[] getPayAggVO(String[] ids) throws BusinessException {
		ViewQuery<PayPlanViewVO> query = new ViewQuery<PayPlanViewVO>(PayPlanViewVO.class);
		PayPlanViewVO[] rawViewVOs = query.query(ids);
		// 去掉已经付完款的数据
		PayPlanViewVO[] viewVOs = removeCompletedVOs(rawViewVOs);
		// 处理付款单中的金额
		moneyProcess(viewVOs);
		AggPayPlanVO[] vos = PayPlanViewVO.getAggPayPlanVO(viewVOs);
		AggregatedValueObject[] destVOs = PfServiceScmUtil.exeVOChangeByBillItfDef(CTBillType.PurDaily.getCode(),
				IArapBillTypeCons.D3, vos);
		AggregatedValueObject[] payvos = this.convertor(destVOs);
		refreshChildVO2HeadVO(payvos);
		return payvos;
	}

	private AggregatedValueObject[] convertor(AggregatedValueObject[] destVOs) {
		AggPayBillVO vo = new AggPayBillVO();

		PayBillVO headVO = ((AggPayBillVO) destVOs[0]).getHeadVO();
		UFDate busidate = AppContext.getInstance().getBusiDate();
		headVO.setBilldate(busidate);
		vo.setParentVO(headVO);

		List<PayBillItemVO> list = new ArrayList<PayBillItemVO>();
		for (AggregatedValueObject destVO : destVOs) {
			PayBillItemVO[] itemVOs = ((AggPayBillVO) destVO).getBodyVOs();
			list.addAll(Arrays.asList(itemVOs));
		}
		PayBillItemVO[] allItemVOs = list.toArray(new PayBillItemVO[list.size()]);

		for (PayBillItemVO itemVO : allItemVOs) {
			itemVO.setBilldate(busidate);
		}
		vo.setChildrenVO(allItemVOs);
		return new AggregatedValueObject[] { vo };
	}

	/**
	 * 处理付款单中的金额，应该是 原币金额-累计付款金额|本币金额-累计付款本币金额
	 * 
	 * @param viewVOs
	 * @author zhaoypm
	 * @since ncc1.0 2018-09-18
	 */
	private void moneyProcess(PayPlanViewVO[] viewVOs) {
		for (PayPlanViewVO vo : viewVOs) {
			UFDouble ncanpayorgmny = MathTool.sub(vo.getNorigmny(), vo.getNaccumpayorgmny());
			vo.setNorigmny(ncanpayorgmny);
			vo.setNmny(MathTool.sub(vo.getNmny(), vo.getNaccumpaymny()));
		}
	}

	/**
	 * 过滤掉已经付完款的付款计划
	 * 
	 * @param viewVOs
	 * @author zhaoypm
	 * @since ncc1.0 2018-09-20
	 */
	private PayPlanViewVO[] removeCompletedVOs(PayPlanViewVO[] viewVOs) {
		List<PayPlanViewVO> viewList = new ArrayList<PayPlanViewVO>();
		for (PayPlanViewVO payPlanViewVO : viewVOs) {
			UFDouble norigmny = payPlanViewVO.getNorigmny();// 原币金额（页面上的金额字段）
			UFDouble naccumpayorgmny = payPlanViewVO.getNaccumpayorgmny();// 累计付款金额
			UFDouble naccumpayapporgmny = payPlanViewVO.getNaccumpayapporgmny();// 累计付款申请金额
			if (isNullorZero(MathTool.sub(norigmny, naccumpayorgmny))
					|| isNullorZero(MathTool.sub(norigmny, naccumpayapporgmny))) {
				continue;
			}
			viewList.add(payPlanViewVO);
		}
		return viewList.toArray(new PayPlanViewVO[] {});

	}

	/**
	 * 设置表体第一行的一些field数值， 到表头显示
	 * 
	 * @param aggVOs
	 */
	private void refreshChildVO2HeadVO(AggregatedValueObject... aggVOs) {
		if (null == aggVOs || aggVOs.length == 0)
			return;
		for (int i = 0; i < aggVOs.length; i++) {
			AggregatedValueObject aggVO = aggVOs[i];
			if (aggVO == null) {
				continue;
			}
			CircularlyAccessibleValueObject parentVO = aggVO.getParentVO();
			CircularlyAccessibleValueObject[] childrenVO = aggVO.getChildrenVO();
			CircularlyAccessibleValueObject childVO = null != childrenVO && childrenVO.length > 0 ? childrenVO[0] : null;
			if (childVO != null) {
				for (Entry<String, String> entry : getRefMap().entrySet()) {
					String headKey = entry.getKey();
					String bodyKey = entry.getValue();
					Object atrrValue = childVO.getAttributeValue(bodyKey);
					if (atrrValue == null || StringUtils.isEmpty(atrrValue.toString())) {
						continue;
					} else {
						parentVO.setAttributeValue(headKey, atrrValue);
					}

				}
			}
			if (childrenVO != null) {
				UFDouble money = UFDouble.ZERO_DBL;
				UFDouble local_money = UFDouble.ZERO_DBL;
				for (CircularlyAccessibleValueObject vo : childrenVO) {
					UFDouble money_de = (vo.getAttributeValue(IBillFieldGet.MONEY_DE) == null ? UFDouble.ZERO_DBL : (UFDouble) vo
							.getAttributeValue(IBillFieldGet.MONEY_DE));
					UFDouble money_cr = (vo.getAttributeValue(IBillFieldGet.MONEY_CR) == null ? UFDouble.ZERO_DBL : (UFDouble) vo
							.getAttributeValue(IBillFieldGet.MONEY_CR));
					money = money.add(money_de.add(money_cr));
					UFDouble local_money_de = (vo.getAttributeValue(IBillFieldGet.LOCAL_MONEY_DE) == null ? UFDouble.ZERO_DBL
							: (UFDouble) vo.getAttributeValue(IBillFieldGet.LOCAL_MONEY_DE));
					UFDouble local_money_cr = (vo.getAttributeValue(IBillFieldGet.LOCAL_MONEY_CR) == null ? UFDouble.ZERO_DBL
							: (UFDouble) vo.getAttributeValue(IBillFieldGet.LOCAL_MONEY_CR));
					local_money = local_money.add(local_money_de.add(local_money_cr));
				}
				parentVO.setAttributeValue(IBillFieldGet.MONEY, money);
				parentVO.setAttributeValue(IBillFieldGet.LOCAL_MONEY, local_money);
			}
		}
	}

	/**
	 * 获取标题需要显示到表头字段的字段名映射关系
	 * 
	 * @return
	 */
	private Map<String, String> getRefMap() {
		Map<String, String> refMap = new HashMap<>();
		refMap.put("supplier", "supplier");// 供应商
		return refMap;
	}

	private boolean isNullorZero(UFDouble ufDouble) {
		return UFDouble.ZERO_DBL.equals(getNotNullDouble(ufDouble));
	}

	private UFDouble getNotNullDouble(UFDouble ufDouble) {
		if (ufDouble == null) {
			return UFDouble.ZERO_DBL;
		}
		return ufDouble;
	}

}
