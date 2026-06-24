package nccloud.web.ct.report.purexecution.action;

import java.util.HashMap;
import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pu.m21.entity.OrderViewVO;
import nc.vo.pub.BusinessException;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.billcard.BillCardOperator;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.pubitf.ct.report.service.IPurexecution;
import nccloud.web.ct.report.purexecution.utils.PurExecutionPrecisionUtils;
import nccloud.web.ic.pub.utils.PageUtils;
/**
 * 
 * @description 采购合同执行情况联查订单明细
 * @author zhengxinm 
 * @date 2019-2-17 上午8:58:00 
 * @version ncc1.0
 */
public class PurExecutionDetailAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		// TODO Auto-generated method stub
		String jsonstr = request.read();
		if (jsonstr == null || jsonstr.trim().length() == 0) {
			String message = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2014014_0", "02014014-0122")/* @res "当前参数为空" */;
			ExceptionUtils.wrapBusinessException(message);
		}
		IJson gson = JsonFactory.create();
		Map<String, String> jsonMap = gson.fromJson(jsonstr, Map.class);
		
		String ctbillcode = jsonMap.get("ctbillcode");//合同编号
		String pk_material = jsonMap.get("pk_material");//订单物料
		String pk_org = jsonMap.get("pk_org");//组织
		Map<String,Object> results = null;
		IPurexecution service = ServiceLocator.find(IPurexecution.class);
		try {
			results = service.getPurExecutionDetail(pk_org, ctbillcode, pk_material);
			//获取采购合同模板主键
			String templetid2 = PageUtils.getPageTemplet("400400604", "400400604_card").getOid();
			AggCtPuVO ctPuAggVO = (AggCtPuVO) results.get("ctPuAggVO");
			OrderViewVO[] orderViewVos = (OrderViewVO[]) results.get("orderViewVos");
			GridOperator go = new GridOperator("400413232_card");
			go.setBodycode("body");
			Grid grid = null;
			if (orderViewVos != null && orderViewVos.length > 0) {
				grid = go.toGrid(orderViewVos);
			}
			BillCardOperator operator2 = new BillCardOperator(templetid2,"400400604_card");
			BillCard ctPuAggVOBillCard = operator2.toCard(ctPuAggVO);
			Map<String,Object> billCardMap = new HashMap<String, Object>();
			if(grid != null && grid.getModel().getRows().length > 0) {
				PurExecutionPrecisionUtils.dealGridPrecision(grid);
			}
			PurExecutionPrecisionUtils.dealHeadPrecision(ctPuAggVOBillCard);
			billCardMap.put("ctPuAggVOBillCard", ctPuAggVOBillCard);
			billCardMap.put("orderViewVos", grid);
			return billCardMap;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}
}
