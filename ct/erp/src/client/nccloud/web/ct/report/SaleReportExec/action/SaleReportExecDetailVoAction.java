package nccloud.web.ct.report.SaleReportExec.action;

import java.util.HashMap;
import java.util.Map;

import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.entity.SaleOrderViewVO;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;
import nccloud.web.ic.pub.utils.PageUtils;
import nccloud.web.so.pub.scale.SaleOrderViewScaleProcess;

/**
 * 通过条件查询销售合同执行明细
 * 
 * @author cuijun
 * @date 2019-1-23 下午8:36:21
 * @version ncc1.0
 */
public class SaleReportExecDetailVoAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String jsonstr = request.read();
		IJson json = JsonFactory.create();
		QueryTreeFormatVO queryInfo = json.fromJson(jsonstr, QueryTreeFormatVO.class);

		if (jsonstr == null || jsonstr.trim().length() == 0) {
			String message = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2014014_0",
					"02014014-0122")/* @res "当前参数为空" */;
			ExceptionUtils.wrapBusinessException(message);
		}
		// 查询条件获取
		String pk = queryInfo.getUserdefObj().get("id").toString();
		String material = queryInfo.getUserdefObj().get("material").toString();
		String marbasclass = queryInfo.getUserdefObj().get("marbasclass").toString();
		String bid = queryInfo.getUserdefObj().get("bid").toString();
		String rowno = queryInfo.getUserdefObj().get("rowno").toString();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			ISaledailyMaintainApp service = ServiceLocator.find(ISaledailyMaintainApp.class);
			// 根据物料 合同id 以及合同明细id 查询明细数据
			Map<String, Object> viewMap = service.queryViewVo(pk, bid, rowno,material, marbasclass);

			SaleOrderViewVO[] saleOrderViewVOs = (SaleOrderViewVO[]) viewMap.get("saleOrderViewVOs");
			AggCtSaleVO ctSaleAggVO = (AggCtSaleVO) viewMap.get("ctSaleAggVO");

			GridOperator go = new GridOperator("400612014_page");
			go.setBodycode("body");
			if (saleOrderViewVOs != null && saleOrderViewVOs.length > 0) {
				Grid grid = go.toGrid(saleOrderViewVOs);
				SaleOrderViewScaleProcess.process(grid);
				resultMap.put("saleOrderViewVOs", grid);
			}
			// 获取预置销售合同模板主键
			String templetid = PageUtils.getPageTemplet("400600200", "400600200_card").getOid();
			ExtBillCardOperator operator = new ExtBillCardOperator(templetid, "400600200_card");
			ExtBillCard ctSaleAggVOBillCard = operator.toCard(ctSaleAggVO);
			SaleDailyPrecisionUtil.dealPrecision(ctSaleAggVOBillCard);
			resultMap.put("ctSaleAggVOBillCard", ctSaleAggVOBillCard);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return resultMap;
	}
}