package nccloud.web.ct.report.purexecution.action;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.ct.report.service.IPurexecution;


public class LinkSearchAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		  String jsonstr = request.read();
		    if (jsonstr == null || jsonstr.trim().length() == 0) {
		      String message = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2014014_0","02014014-0122")/*@res "当前参数为空"*/;
		      ExceptionUtils.wrapBusinessException(message);
		    }
		    IJson gson = JsonFactory.create();
		    QueryTreeFormatVO queryTreeVO = gson.fromJson(jsonstr, QueryTreeFormatVO.class);
		    Map<String, Object> jsonMap = gson.fromJson(jsonstr, Map.class);
		    IPurexecution service = ServiceLocator.find(IPurexecution.class);
		    Map<String, Object> url = null;
		    try {
		    	url = service.getUrlBuyConditions(queryTreeVO, jsonMap);
				} catch (BusinessException e) {
					 ExceptionUtils.wrapException(e);
				}
				return url;
			}
	}
	
