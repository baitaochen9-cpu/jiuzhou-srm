package nc.bs.jzyy.sys.lims.mmwrcheck;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP生产报告调用LIMS回写撤销接口
 * 
 * @author liyf
 * 
 */
public class MmwrcheckCancelSender extends AbstractSender4LIMS {

	public static final String functype = "nc_inspection_write";
	WrItemVO[] bvos = null;
	AggWrVO vo = null;

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
				String syncid = output.getString("syncid");
				for (WrItemVO bvo : bvos) {
					JSONObject matcheRepBody = null;
					if(!bvo.getVbdef11().equalsIgnoreCase(output.getString("u_extrequestid"))){
						if(matcheRepBody == null ){
							throw new BusinessException("未匹配到回执,请联系信息运维排查.");
						}
					}
					bvo.setVbdef11(null);
					bvo.setVbdef12(syncid);
					
				}
				new VOUpdate().update(bvos,new String[] {"vbdef11","vbdef12"});
			} else {
				throw new BusinessException("回执异常:返回的output数据为空:" + response);
			}
		}
		return response;
	
	}

	@Override
	public void init(Object obj, Map<String, Object> otherpms) throws Exception {
		bvos = (WrItemVO[]) otherpms.get("bvos");
		vo = (AggWrVO)obj;
	}

	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}

	public String getSendJson(Object obj, Map<String, Object> otherpms)
			throws Exception {
		AggWrVO newvo = (AggWrVO) vo;
		String jsonObj = changeTOJson(newvo);
		return jsonObj;
	}
	private String changeTOJson(AggWrVO vo) throws BusinessException {
		Set<String> pk_measdoc = new HashSet<String>();
		for (WrItemVO bvo : bvos) {
			if (!StringUtil.isEmpty(bvo.getCbunitid())) {
				pk_measdoc.add(bvo.getCbunitid());
			}
		}

		Map<String, MeasdocVO> memap = getMeasdocVOMap(pk_measdoc);
		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());
		// List<JSONObject> list = new ArrayList<>();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("functype", functype);
		//实际只有一行，且LIMS报文也是单行的结构，所以此处暂时保留循环,假如支持多行，直接修改报文结构
		MmwrSendTool tool = new MmwrSendTool();
		for (WrItemVO bvo : bvos) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("action", "finish");// 动作
			String sysFlowNo = getSysFlowNo();
			sysFlowNo ="WR2-"+sysFlowNo;
			jsonObject.put("u_syncid", sysFlowNo);// 同步号
			jsonObject.put("u_extrequestid", tool.getSourceLimsID(bvo));// 订单LIMS检验请求ID
			MeasdocVO mesvo = memap.get(bvo.getCbunitid());
			if (mesvo != null) {
				jsonObject.put("u_finishedbatchunit", mesvo.getName());//  实际产出单位名称
			}
			jsonObject.put("u_finishedbatchsize", bvo.getNbwrnum().multiply(-1).doubleValue());// 实际产出数量
			UFDateTime u_finisheddt = new UFDateTime();
			jsonObject.put("u_finisheddt", u_finisheddt.toString() );// ERP中报告时间
			if (uservo != null) {
				jsonObject.put("u_finishedby",uservo.getUser_code());// 报告人编码
				jsonObject.put("u_finishedbyname",uservo.getUser_name());// 报告人名称
			}
//			生产日期	u_manufacturedt	String	16	Y	YYYY-MM-DD hh:mm			如ERP发生变化，需要传给LIMS
			String u_manufacturedt = vo.getParentVO().getVdef10();
			if(StringUtils.isEmpty(u_manufacturedt)||"~".equalsIgnoreCase(u_manufacturedt)){
				throw new BusinessException("请维护报告表头生产日期(vdef10)");
			}
			jsonObject1.put("data", jsonObject);
		}

		return jsonObject1.toJSONString();
	}
}
