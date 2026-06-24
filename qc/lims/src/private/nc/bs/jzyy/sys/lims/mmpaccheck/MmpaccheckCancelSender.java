package nc.bs.jzyy.sys.lims.mmpaccheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP流程生产订单调用LIMS报检撤销接口
 * 
 * @author liyf
 * 
 */
public class MmpaccheckCancelSender extends AbstractSender4LIMS {
	public static final String functype_delete = "nc_inspection_delete";
	PMOItemVO[] bvos = null;
	PMOAggVO vo = null;
	List<PMOItemVO> reList = new ArrayList<PMOItemVO>();

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
				for (PMOItemVO bvo : bvos) {
					JSONObject matcheRepBody = null;
					for(int i=0;i<data.size();i++){
						JSONObject respbody = data.getJSONObject(i);
						if(bvo.getVdef11().equalsIgnoreCase(respbody.getString("u_extrequestid"))){
							matcheRepBody = respbody;
						}
					}
					if(matcheRepBody == null ){
						throw new BusinessException(bvo.getVrowno() +"行,未匹配到回执,请联系信息运维排查.");
					}
					String u_extrequestid = matcheRepBody.getString("u_extrequestid");//LIMS检验请求ID
					bvo.setVdef11(null);
					bvo.setVdef12(syncid);
					
				}
				new VOUpdate().update(bvos,new String[] { "vdef11", "vdef12"});
			} else {
				throw new BusinessException("回执异常:返回的output数据为空:" + response);
			}
		}
		return response;
	
	}

	@Override
	public void init(Object obj, Map<String, Object> otherpms) throws Exception {
		vo = (PMOAggVO)obj;
		bvos = (PMOItemVO[]) vo.getChildrenVO();

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
		String ctrantype ="9";
		sysFlowNO ="MO"+ctrantype+"-"+sysFlowNO;
		jsonObject.put("u_syncid",sysFlowNO);// 同步号
		jsonObject.put("u_cancelleddt", new UFDateTime().toString());//ERP中撤回时间
		jsonObject.put("u_cancelledby", uservo.getUser_code());//撤回人编码
		jsonObject.put("u_cancelledbyname", uservo.getUser_name());//撤回人名称
		StringBuffer u_extrequestid = new StringBuffer();
		PMOAggVO newvo = (PMOAggVO)  obj;
		PMOItemVO[] bvos = vo.getChildrenVO();
		for (int i=0;i<bvos.length;i++) {
			 String vbdef11 = bvos[i].getVdef11();//LIMS检验请求ID
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
