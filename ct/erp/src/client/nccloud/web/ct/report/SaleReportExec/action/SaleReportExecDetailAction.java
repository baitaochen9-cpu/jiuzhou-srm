package nccloud.web.ct.report.SaleReportExec.action;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.ct.report.service.IDrillSaleReportExec;
/**
 * 销售合同执行查询明细联查
 * @author cuijun
 * @date 2019-1-23 下午1:02:35
 * @version ncc1.0
 */
public class SaleReportExecDetailAction implements ICommonAction {

	@SuppressWarnings("rawtypes")
	@Override
	public Object doAction(IRequest request) {
	  String jsonstr = request.read();
    if (jsonstr == null || jsonstr.trim().length() == 0) {
      String message = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0005")/*@res "当前参数为空"*/;
      ExceptionUtils.wrapBusinessException(message);
    }
    IJson gson = JsonFactory.create();
    QueryTreeFormatVO queryTreeVO = gson.fromJson(jsonstr, QueryTreeFormatVO.class);
    Map<String, Object> jsonMap = gson.fromJson(jsonstr, Map.class);
    IDrillSaleReportExec service = ServiceLocator.find(IDrillSaleReportExec.class);
    String url = null;
    try {
    	url = service.getUrlBuyConditions(queryTreeVO, jsonMap);
		} catch (BusinessException e) {
			 ExceptionUtils.wrapException(e);
		}
		return url;
	}

}