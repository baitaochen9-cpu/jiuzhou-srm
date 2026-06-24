package nc.bs.jzyy.sys.lims.pucheck;

import java.util.Map;

import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP采购到货单调用LIMS报检撤销接口
 * 
 * @author liyf
 * 
 */
public class PucheckCancelSender extends AbstractSender4LIMS {
	public static final String functype_delete = "nc_inspection_delete";
	ArriveItemVO[] bvos = null;
	ArriveVO vo = null;
	@Override
	public Object afterSend(JSONObject response) throws Exception {
		String code = response.getString("code");
		if (!"200".equals(code)) {
			throw new BusinessException(response.toJSONString());
		} else {
			JSONObject output = response.getJSONObject("output");
			if (output != null && output.size() > 0) {
				String status = output.getString("status");
				if(!"success".equalsIgnoreCase(status)){
					String msg = output.getString("msg");
					throw new BusinessException( msg);
				}
				JSONArray data = JSONArray.parseArray(output.getString("data"));
				String syncid = output.getString("syncid");
				for (ArriveItemVO bvo : bvos) {
					JSONObject matcheRepBody = null;
					for(int i=0;i<data.size();i++){
						JSONObject respbody = data.getJSONObject(i);
						if(bvo.getVbdef11().equalsIgnoreCase(respbody.getString("u_extrequestid"))){
							matcheRepBody = respbody;
						}
					}
					if(matcheRepBody == null ){
						throw new BusinessException(bvo.getCrowno() +"行,未匹配到回执,请联系信息运维排查.");
					}
					String u_extrequestid = matcheRepBody.getString("u_extrequestid");
					bvo.setVbdef11(null);//
//					bvo.setNaccumchecknum(null);//累计报检主数量
					bvo.setVbdef12(syncid);
					
				}
				new VOUpdate().update(bvos, new String[] {"vbdef11", "vbdef12" });

				ArriveVO newvo = (ArriveVO) vo.clone();
				newvo.setBVO(bvos);

				ChkReApplyAndDelChkDetailRule rule1 = new ChkReApplyAndDelChkDetailRule();
				rule1.process(new ArriveVO[] { newvo });
				WriteBackArriveCheckNumRule rule2 = new WriteBackArriveCheckNumRule(
						false);
				rule2.process(new ArriveVO[] { newvo });
			} else {
				throw new BusinessException("回执异常:返回的output数据为空:" + response);
			}
		}
		return response;
	}

	@Override
	public void init(Object obj, Map<String, Object> otherpms) throws Exception {
		ArriveVO newvo = (ArriveVO) obj;
		bvos = (ArriveItemVO[]) newvo.getBVO();
		vo = (ArriveVO) obj;
	
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
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("action", "cancel");//动作
		String sysFlowNO = getSysFlowNo();
		sysFlowNO ="PU9"+"-"+sysFlowNO;
		jsonObject.put("u_syncid",sysFlowNO);// 同步号
		jsonObject.put("u_cancelleddt", new UFDateTime().toString());//ERP中撤回时间
		jsonObject.put("u_cancelledby", uservo.getUser_code());//撤回人编码
		jsonObject.put("u_cancelledbyname", uservo.getUser_name());//撤回人名称
		StringBuffer u_extrequestid = new StringBuffer();
		ArriveVO newvo = (ArriveVO)  obj;
		ArriveItemVO[] bvos = newvo.getBVO();
		for (int i=0;i<bvos.length;i++) {
			 String vbdef11 = bvos[i].getVbdef11();//LIMS检验请求ID
			 if(i==bvos.length-1){
				 u_extrequestid.append(vbdef11);
			 }else{
				 u_extrequestid.append(vbdef11+";");
			 }
			 
		}
		jsonObject.put("u_extrequestid", u_extrequestid.toString());
		jsonObject1.put("data", jsonObject);
		return jsonObject1.toJSONString();
	}


}
