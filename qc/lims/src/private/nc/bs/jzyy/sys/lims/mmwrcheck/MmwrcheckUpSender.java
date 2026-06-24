package nc.bs.jzyy.sys.lims.mmwrcheck;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.medpub.JXQUtil;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP生产报告调用LIMS回写接口
 * 
 * @author liyf
 * 
 */
public class MmwrcheckUpSender extends AbstractSender4LIMS {

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
					if(syncid.startsWith("WR5")){
//						bbstockbycheck  依据检验结果入库 
						bvo.setBbstockbycheck(UFBoolean.TRUE);
//						nbsldchecknum  已报检主数量, 
						bvo.setNbsldchecknum(bvo.getNbwrnum());
//						nbsldcheckastnum  已报检数量 ,
						bvo.setNbsldcheckastnum(bvo.getNbwrastnum());
					}
					
					bvo.setVbdef12(syncid);
					
				}
				new VOUpdate().update(bvos,new String[] {"vbdef11","vbdef12","bbstockbycheck","nbsldchecknum","nbsldcheckastnum"});
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

		JSONObject billJson = new JSONObject();
		billJson.put("functype", functype);
		//实际只有一行，且LIMS报文也是单行的结构，所以此处暂时保留循环,假如支持多行，直接修改报文结构
		HashMap<String, Object> materialStor = getMaterialStor(vo.getParentVO().getPk_org(), bvos[0].getCbmaterialid());
		String ctrantype =getctrantype(materialStor);

		MmwrSendTool tool = new MmwrSendTool();
		for (WrItemVO bvo : bvos) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("action", "finish");// 动作
			String sysFlowNo = getSysFlowNo();
			sysFlowNo ="WR"+ctrantype+"-"+sysFlowNo;
			jsonObject.put("u_syncid", sysFlowNo);// 同步号
			// 订单LIMS检验请求ID
			String u_extrequestid = tool.getSourceLimsID(bvo);
			jsonObject.put("u_extrequestid",u_extrequestid);
			//同时完工单也记录下 订单LIMS检验请求ID，方便后续afterSend对比
			bvo.setVbdef11(u_extrequestid);
			MeasdocVO mesvo = memap.get(bvo.getCbunitid());
			if (mesvo != null) {
				jsonObject.put("u_finishedbatchunit", mesvo.getName());//  实际产出单位名称
			}
			jsonObject.put("u_finishedbatchsize", bvo.getNbwrnum().doubleValue());// 实际产出数量
			UFDateTime u_finisheddt = new UFDateTime();
			jsonObject.put("u_finisheddt", u_finisheddt.toString());// ERP中报告时间
			if (uservo != null) {
				jsonObject.put("u_finishedby",uservo.getUser_code());// 报告人编码
				jsonObject.put("u_finishedbyname",uservo.getUser_name());// 报告人名称
			}
//			生产日期	u_manufacturedt	String	16	Y	YYYY-MM-DD hh:mm			如ERP发生变化，需要传给LIMS
			String u_manufacturedt = vo.getParentVO().getVdef10();
			if(StringUtils.isEmpty(u_manufacturedt)||"~".equalsIgnoreCase(u_manufacturedt)){
				throw new BusinessException("请维护报告表头生产日期(vdef10)");
			}
			jsonObject.put("u_manufacturedt",u_manufacturedt);
			//复测日期
			UFDate u_expirydt = getU_expirydt(u_manufacturedt,materialStor);
			jsonObject.put("u_expirydt",u_expirydt.toString());

			billJson.put("data", jsonObject);
		}

		return billJson.toJSONString();
	}
	private UFDate getU_expirydt(String u_manufacturedt,
			HashMap<String, Object> materialStor) {
		// TODO Auto-generated method stub
		UFDate u_expirydt = new UFDate(u_manufacturedt);
		if(materialStor == null||materialStor.size() == 0 ){
			return u_expirydt;
		}
//		qualitymanflag  保质期管理  qualitymanflag char(1)  UFBoolean      
//		qualitynum  保质期  qualitynum smallint(4)  Integer      
//	    qualityunit  保质期单位   0=年，1=月，2=日，
		String qualitymanflag= (String) materialStor.get("qualitymanflag");
		if(qualitymanflag == null){
			return u_expirydt;
		}
		UFBoolean b_qualitymanflag = UFBoolean.valueOf(qualitymanflag);
		if(!b_qualitymanflag.booleanValue()){
			return u_expirydt;
		}
		//保质期单位 
		Integer qualityunit= (Integer) materialStor.get("qualityunit");
		
//		保质期  
		Integer qualitynum = (Integer) materialStor.get("qualitynum");
		
		/*
		 * 通过MED生产日期计算失效日期
		 * prama1:生产日期
		 * prama2:日期计算格式
		 * prama3:周期单位
		 * prama4:周期值
		 */
		String validDateByProduceDate = "";
		try {
			
		
		
//		Calendar calendar = new GregorianCalendar();
//		calendar.setTime(u_expirydt.toDate());
		if(0 ==qualityunit){
//			calendar.add(Calendar.YEAR, qualitynum);
			validDateByProduceDate = JXQUtil.getValidDateByProduceDate(u_manufacturedt, "YYYY-MM", Calendar.YEAR, qualitynum);
		}
		if(1 ==qualityunit){
//			calendar.add(Calendar.MONTH, qualitynum);
			validDateByProduceDate = JXQUtil.getValidDateByProduceDate(u_manufacturedt, "YYYY-MM", Calendar.MONTH, qualitynum);
		}
		if(2 ==qualityunit){
//			calendar.add(Calendar.DATE, qualitynum);
			validDateByProduceDate = JXQUtil.getValidDateByProduceDate(u_manufacturedt, "YYYY-MM-DD", Calendar.DATE, qualitynum);
		}
		validDateByProduceDate = JXQUtil.getDValidDateByValidDate(validDateByProduceDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new BusinessException("日期计算异常，请联系管理员！"+this.getClass().getName());
		}
//		calendar.add(Calendar.DATE, -1);
//		u_expirydt =  new UFDate(calendar.getTime());
		return new UFDate(validDateByProduceDate);
	}

	private String getctrantype(HashMap<String, Object> materialStor) throws BusinessException {
		//  stockbycheck  根据检验结果入库  
		if(materialStor == null||materialStor.size() == 0 ){
			return "4";
		}
		String chkfreeflag= (String) materialStor.get("stockbycheck");
		if(chkfreeflag == null){
			return "4";
		}
		UFBoolean b = UFBoolean.valueOf(chkfreeflag);
//		/ 罐区溶剂--
		if(b.booleanValue()){
			return "5";
		}
		
		return "4";
	}
	
}
