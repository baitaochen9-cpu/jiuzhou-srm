package nccloud.web.ct.saledaily.ssc;

import com.alibaba.fastjson.JSONObject;

import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.sscrp.rpbill.QryConditionVO;
import nccloud.dto.pu.pub.IDataBaseTools;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.SessionContext;
import nccloud.web.scmpub.pub.ssc.SCMBasePortal;

public class SaleDailyPortal extends SCMBasePortal {

	@Override
	protected String getMakeBillAppUrl(QryConditionVO param, JSONObject paraData) {
		String transtypecode = param.getTranstypecode();
		IDataBaseTools tools = ServiceLocator.find(IDataBaseTools.class);
		IRowSet rowSet = tools
				.query("select BILLTYPENAME,PK_BILLTYPEID,PK_BILLTYPECODE from bd_billtype where PK_BILLTYPECODE = '"
						+ transtypecode + "'  and pk_group= '"
						+ SessionContext.getInstance().getClientInfo().getPk_group() + "'");
		String pk_billtypename = null;
		String pk_billtypeid = null;
		String pk_billtypecode = null;
		while (rowSet.next()) {
			pk_billtypename = rowSet.getString(0);
			pk_billtypeid = rowSet.getString(1);
			pk_billtypecode = rowSet.getString(2);
		}
		paraData.put("status", "add");
		paraData.put("transtypecode", pk_billtypecode);
		paraData.put("billtypeid", pk_billtypeid);
		paraData.put("billtypename", pk_billtypename);
		return "/nccloud/resources/ct/ct/saledaily/main/index.html#/card";
	}

	@Override
	protected String getShowDetailUrl(QryConditionVO param, JSONObject paraData) {
		String id = param.getBillid();
		//获取最新版本合同
		ISaledailyMaintainApp service = ServiceLocator.find(ISaledailyMaintainApp.class);
		try {
			AggCtSaleVO aggVo = service.queryMZ3AppByVbillcode(id);
			id = aggVo.getPrimaryKey();
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		param.setBillid(id);
		return "/nccloud/resources/ct/ct/saledaily/main/index.html#/card?";
	}

	@Override
	protected String getCopyAppUrl(QryConditionVO param, JSONObject paraData) {
		String id = param.getBillid();
		//获取最新版本合同
		ISaledailyMaintainApp service = ServiceLocator.find(ISaledailyMaintainApp.class);
		try {
			AggCtSaleVO aggVo = service.queryMZ3AppByVbillcode(id);
			id = aggVo.getPrimaryKey();
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		param.setBillid(id);
		paraData.put("status", "add");
		paraData.put("srcid", param.getBillid());
		paraData.put("type", "copy");
		return "/nccloud/resources/ct/ct/saledaily/main/index.html#/card";
	}

	@Override
	protected String getPrintUrl(QryConditionVO param, JSONObject paraData) {
		return "/nccloud/ct/saledaily/print.do";
	}

	@Override
	protected String getPageCodeForPrint(QryConditionVO param) {
		return "400600200_card";
	}

	@Override
	public String[] getExceptActionCodes() {
		return null;
	}

}
