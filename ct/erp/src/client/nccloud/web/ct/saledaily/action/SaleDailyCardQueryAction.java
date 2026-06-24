package nccloud.web.ct.saledaily.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.entity.CtAbstractExecVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleExecVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scmpub.util.CollectionUtils;
import nccloud.dto.ct.saledaily.entity.SaleDailyQueryInfo;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;

/**
 * @description 销售合同卡片查询
 * @author wangshrc
 * @date 2019年1月16日 上午9:47:15
 * @version ncc1.0
 */
public class SaleDailyCardQueryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		SaleDailyQueryInfo info = json.fromJson(str, SaleDailyQueryInfo.class);
		ExtBillCardOperator operator = new ExtBillCardOperator("400600200_card");
		AggCtSaleVO[] aggvos = null;
		ISaledailyMaintainApp service = ServiceLocator.find(ISaledailyMaintainApp.class);
		try {
			aggvos = service.queryMZ3App(new String[] { info.getPk() });
			if (aggvos != null && aggvos.length > 0) {
				// 执行情况按ts排序
				CtSaleExecVO[] execVos = (CtSaleExecVO[]) vosKeySort(aggvos[0].getCtSaleExecVO(), CtAbstractExecVO.TS);
				aggvos[0].setCtSaleExecVO(execVos);
				// 设置head上的物料控制方式
				getNinvctlstyle(aggvos);
				operator.setTransFlag(true);
				ExtBillCard card = operator.toCard(aggvos[0]);
				SaleDailyPrecisionUtil.dealPrecision(card);
				Map<Object, Object> map = new HashMap<Object, Object>();
				map.put("extCard", card);
				IBusinessTypeService busiService = ServiceLocator.find(IBusinessTypeService.class);
				String ctrantypeid = aggvos[0].getParentVO().getCtrantypeid();
				boolean showPayTerm = false;

				BusinessSetVO businessvo = busiService.queryBusinessVO(ctrantypeid);
				if (businessvo == null || businessvo.getBshowpayterm() == null
						|| UFBoolean.TRUE.booleanValue() != businessvo.getBshowpayterm().booleanValue()) {
					showPayTerm = false;
				} else {
					showPayTerm = true;
				}
				map.put("showPayTerm", showPayTerm);
				return map;
			}else {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0006")/*@res "该数据已被删除，请重新查询"*/);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 方法功能描述：设置交易类型控制方式
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param aggvos
	 * @time 2019-6-20 上午11:16:53
	 */
	private void getNinvctlstyle(AggCtSaleVO[] aggvos) {
		for (int i = 0; i < aggvos.length; i++) {
			String ctrantypeid = aggvos[i].getParentVO().getCtrantypeid();
			try {
				IBusinessTypeService iBusiness = ServiceLocator.find(IBusinessTypeService.class);
				Integer intValue = iBusiness.queryMaterial(ctrantypeid);
				aggvos[i].getParentVO().setNinvctlstyle(intValue);
			} catch (BusinessException e) {
				ExceptionUtils.wrapException(e);
			}
		}
	}

	/**
	 * 执行过程根据字段排序
	 * 
	 * @param vos      aggVOs
	 * @param orderKey 执行过程要排序的字段
	 * @return
	 *
	 */
	public static CircularlyAccessibleValueObject[] vosKeySort(CircularlyAccessibleValueObject[] vos,
			final String orderKey) {
		List<CircularlyAccessibleValueObject> volist = new ArrayList<CircularlyAccessibleValueObject>();
		CollectionUtils.addArrayToList(volist, vos);
		Collections.sort(volist, new Comparator<CircularlyAccessibleValueObject>() {

			@Override
			public int compare(CircularlyAccessibleValueObject a, CircularlyAccessibleValueObject b) {
				UFDateTime aStr = (UFDateTime) a.getAttributeValue(orderKey);
				UFDateTime bStr = (UFDateTime) b.getAttributeValue(orderKey);
				return aStr.compareTo(bStr);
			}
		});
		return CollectionUtils.listToArray(volist);
	}

}
