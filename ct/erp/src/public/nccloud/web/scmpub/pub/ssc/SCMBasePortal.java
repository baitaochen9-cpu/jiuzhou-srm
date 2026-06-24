package nccloud.web.scmpub.pub.ssc;

import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.sscrp.rpbill.QryConditionVO;
import nc.vo.sscrp.rpbill.RPbillPrintInfoVO;
import nccloud.pubitf.sscrp.rpbill.BasePortal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @description
 *
 * @scene
 * 
 * @param
 * 
 *
 * @since NCC 1909
 * @version 2019年3月21日上午9:43:30
 * @author wangceb
 */
public abstract class SCMBasePortal extends BasePortal {
	
	public static final String COPY = "copy";

	@SuppressWarnings("static-access")
	@Override
	public JSONObject getURL4MakeBill(QryConditionVO param) {
		JSONObject paraData = new JSONObject();
		String url = this.getMakeBillAppUrl(param, paraData);
		if (PubAppTool.isNull(url)) {
			return null;
		}
		paraData.put("appcode", param.getAppcode());
		paraData.put("pagecode", param.getPagecode());
		this.setParaData(paraData,param);
		JSONObject result = new JSONObject();
		result.put(PARAM_URL, url);
		result.put(PARAM_DATA, paraData);
		return result;

	}

	protected abstract String getMakeBillAppUrl(QryConditionVO param, JSONObject paraData);

	@SuppressWarnings("static-access")
	@Override
	public JSONObject getURL4Show(QryConditionVO param) {
		JSONObject paraData = new JSONObject();
		String url = this.getShowDetailUrl(param, paraData);
		if (PubAppTool.isNull(url)) {
			return null;
		}
		paraData.put("appcode", param.getAppcode());
		paraData.put("pagecode", this.getPagecodeForShow(param));
		paraData.put("id", param.getBillid());
		paraData.put("status", "browse");
		JSONObject result = new JSONObject();
		result.put(PARAM_URL, url);
		result.put(PARAM_DATA, paraData);
		return result;

	}
	protected void setParaData(JSONObject paraData, QryConditionVO param) {
	}
	protected String getPagecodeForShow(QryConditionVO param) {
		return param.getPagecode();
	}
	
	protected String getNodeKey(QryConditionVO param) {
		return null;
	}
	
	@Override
	public JSONObject getURL4Copy(QryConditionVO param) {
		JSONObject paraData = new JSONObject();
		String url = this.getCopyAppUrl(param, paraData);
		if (PubAppTool.isNull(url)) {
			throw new BusinessRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1056001_0","01056001-0038")/*@res "当前单据不支持复制"*/);
		}
		paraData.put("appcode", param.getAppcode());
		paraData.put("pagecode", param.getPagecode());
		JSONObject result = new JSONObject();
		result.put(PARAM_URL, url);
		result.put(PARAM_DATA, paraData);
		return result;
	}
	
	protected String getCopyAppUrl(QryConditionVO param, JSONObject paraData) {
		return null;
	};

	protected abstract String getShowDetailUrl(QryConditionVO param, JSONObject paraData);
	
	@Override
	public RPbillPrintInfoVO getPrintInfo(QryConditionVO param) {
		JSONObject paraData = new JSONObject();
		String url = this.getPrintUrl(param, paraData);
		if (url == null) {
			throw new BusinessRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1056001_0","01056001-0039")/*@res "当前单据不支持打印"*/);
		}
		RPbillPrintInfoVO printInfo = new RPbillPrintInfoVO();
		printInfo.setFiletype("pdf");
		printInfo.setUrl(url);
		
		
		paraData.put("nodekey", this.getNodeKey(param));
		paraData.put("filename", param.getPagecode());
		paraData.put("appcode", param.getAppcode());
		paraData.put("funcode", param.getAppcode());
		paraData.put("pagecode", this.getPageCodeForPrint(param));
		
		JSONArray oids = new JSONArray();
		oids.add(param.getBillid());
		
		paraData.put("oids", oids);
		paraData.put("outputType", "print");
		paraData.put("printTemplateID", null);
		
		printInfo.setParadata(paraData);
		return printInfo;
	}

	protected String getPrintUrl(QryConditionVO param, JSONObject paraData) {
		return null;
	};
	
	protected String getPageCodeForPrint(QryConditionVO param) {
		return param.getPagecode();
	}
	
	@Override
	public String[] getExceptActionCodes() {
		// TODO Auto-generated method stub
		return super.getExceptActionCodes();
	}
	

}
