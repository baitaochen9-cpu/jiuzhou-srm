package nc.bs.jzyy.sys.lims.iccheck;

import java.util.Map;

import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.ic.m4z.entity.FreezeThawVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP库存检验冻结单调用LIMS报检撤销接口
 * 
 * @author liyf
 * 
 */
public class IccheckCancelSender extends AbstractSender4LIMS {
	public static final String functype_delete = "nc_inspection_delete";
	FreezeThawVO[] bvos = null;

	@Override
	public Object afterSend(JSONObject response) throws Exception {
		String code = response.getString("code");
		if(!"200".equals(code)){
			throw new BusinessException("回执异常:"+response.toJSONString());
		}else{
			JSONObject data = response.getJSONObject("output");
			if(data != null && data.size()>0){
				for (FreezeThawVO bvo : bvos) {
					bvo.setCcorrespondhid(null);
				}
				new VOUpdate().update(bvos,	new String[] { "ccorrespondhid"});
			}else{
				throw new BusinessException("回执异常:返回的output数据为空:"+response);
			}
		}
		return response;
	}

	@Override
	public void init(Object obj, Map<String, Object> otherpms) throws Exception {
		bvos = (FreezeThawVO[]) otherpms.get("bvos");
	}

	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}

	public String getSendJson(Object obj, Map<String, Object> otherpms)
			throws Exception {
		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("functype", functype_delete);
		for (FreezeThawVO bvo : bvos) {
			JSONObject jsonObject = new JSONObject();
			jsonObject1.put("action", "cancel");//动作
			jsonObject1.put("u_syncid", new UFDateTime().toString());//同步号
			jsonObject1.put("u_extrequestid", bvo.getCcorrespondhid());//LIMS检验请求ID
			jsonObject1.put("u_cancelleddt", new UFDateTime().toString());//ERP中撤回时间
			if(uservo != null){
				jsonObject1.put("u_cancelledby", uservo.getUser_code());//撤回人编码
				jsonObject1.put("u_cancelledbyname", uservo.getUser_name());//撤回人名称
			}
			jsonObject1.put("data", jsonObject);
		}
		return jsonObject1.toJSONString();
	}

}
