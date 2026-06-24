package nc.bs.jzyy.sys.thlims.mmpacipcheck;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.LimsIpcVO;
import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.bs.logging.Log;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.sm.UserVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP流程生产订单调用LIMS报检接口
 * 
 * @author liyf
 * 
 */
public class IpcheckUpSender extends AbstractSender4ThLims {

	public static final String functype = "nc_th_ipc_inspection";
	PMOItemVO[] bvos = null;
	PMOAggVO vo = null;

	@Override
	public void init() throws Exception {
		this.vo=(PMOAggVO)this.getParam();
		this.bvos=this.vo.getChildrenVO();
		limsLogVO=new THLimsLogVO();
	}
	@Override
	public Object afterSend(JSONObject response) throws Exception {
		String succeed = response.getString("succeed");
		if (!"Y".equals(succeed)) {
			Log.getInstance("泰华Lims流程生产订单IPC请验回传").error(response.toJSONString());
			throw new BusinessException(response.getString("message"));
		} else {
			/*String syncid = response.getString("ID");
			for (PMOItemVO bvo : bvos) {
				//LIMS检验请求ID
				bvo.setVdef11(response.getString("Lims_billCode"));
				//ERP同步号
				bvo.setVdef12(syncid);
			}
			new VOUpdate().update(bvos,new String[] { "vdef11", "vdef12"});*/
			
			//保存报检
			this.saveIPC();
		}
		return response;
	}
	
	
	/*
	 * 记录报检记录
	 * */
	private LimsIpcVO ipcVO;
	
	private void saveIPC(){
		if(null==ipcVO){
			return;
		}
		try {
			this.getPross().dispatch_RequiresNew(ipcVO, "LIMS_SYS_LOGGER", null);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException("保存报检记录异常!");
		}
	}
	
	private ISysDispatcherThLims pross;
	private ISysDispatcherThLims getPross() {
		if(pross == null){
			pross = NCLocator.getInstance().lookup(ISysDispatcherThLims.class);
		}
		return pross;
	}
	
	

	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}

	@Override
	protected String getSendJson() throws Exception {
		PMOAggVO newvo = (PMOAggVO) vo.clone();
		newvo.setChildrenVO(bvos);

		String jsonObj = changeTOJson(newvo,this.getOtherParam());
		Log.getInstance("检验Lims").error("检验Lims："+jsonObj);
		return jsonObj;
	}

	private String changeTOJson(PMOAggVO vo,Map<String, Object> otherpms) throws BusinessException {
		
		//ipcVO
		ipcVO=new LimsIpcVO();
		ipcVO.setStatus(VOStatus.NEW);
		
		PMOItemVO[] bvos = vo.getChildrenVO();
		Set<String> pk_marterial_vs = new HashSet<String>();
		Set<String> pk_defdocs = new HashSet<String>();
		Set<String> pk_measdoc = new HashSet<String>();
		Set<String> cvendorids = new HashSet<String>();
		Set<String> pk_depts = new HashSet<String>();
		for (PMOItemVO bvo : bvos) {
			if (!StringUtil.isEmpty(bvo.getCmaterialid())) {
				pk_marterial_vs.add(bvo.getCmaterialid());
			}

			if (!StringUtil.isEmpty(bvo.getCproductorid())) {
				pk_defdocs.add(bvo.getCproductorid());
			}

			if (!StringUtil.isEmpty(bvo.getCunitid())) {
				pk_measdoc.add(bvo.getCunitid());
			}

			if (!StringUtil.isEmpty(bvo.getCastunitid())) {
				pk_measdoc.add(bvo.getCastunitid());
			}
			
			if (!StringUtil.isEmpty(bvo.getCvendorid())) {
				cvendorids.add(bvo.getCvendorid());
			}
			
			if (!StringUtil.isEmpty(bvo.getCdeptid())) {
				pk_depts.add(bvo.getCdeptid());
			}
		}

		Map<String, MaterialVO> mmap = getMaterialVOMap(pk_marterial_vs);
//		Map<String, DefdocVO> dmap = getDefdocVOMap(pk_defdocs);
		Map<String, MeasdocVO> memap = getMeasdocVOMap(pk_measdoc);
		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());
		OrgVO orgvo = getOrgVO(vo.getParentVO().getPk_org());
		Map<String, OrgVO> deptmap = getOrgVOs(pk_depts);
		
		PMOItemVO bvo1 = bvos[0];
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("functype", functype);
		JSONObject hjsonObject = new JSONObject();
		
		HashMap<String, Object> materialStor = getMaterialStor(vo.getParentVO().getPk_org(), bvos[0].getCmaterialid());
		
		/**
		 * 1=采购报检
		   2=生产完工报检
		   3=库存报检（复验）
		   4=中控检验
		   5=清洁批报检
		 */
		int ctrantype =4;//getctrantype(materialStor);
		//sysFlowNO ="MO"+ctrantype+"-"+sysFlowNO;
		//String sysFlowNO = getSysFlowNo();
//		UFDateTime ufDateTime=new UFDateTime();
		UFDateTime ufDateTime= AppContext.getInstance().getServerTime();

		String sysFlowNO="IPC28_"+vo.getParentVO().getVbillcode()+"_"+ufDateTime.toString(TimeZone.getTimeZone("GMT+08:00"),new SimpleDateFormat("yyyyMMddhhmmss"));
		hjsonObject.put("ID",sysFlowNO);// 同步号
		ipcVO.setID(sysFlowNO);
		hjsonObject.put("ctrantype", ctrantype);// 业务类型
		hjsonObject.put("ts", ufDateTime.toString());// 报检时间
		ipcVO.setQc_time(ufDateTime.toString());
		if (uservo != null) {
			hjsonObject.put("usercode", uservo.getUser_code());// 报检人编码
			hjsonObject.put("username", uservo.getUser_name());// 报检人名称
			ipcVO.setQc_psn(uservo.getUser_name());

		}
		OrgVO deptvo = deptmap.get(bvo1.getCdeptid());
		if (deptvo != null) {
			hjsonObject.put("applydept_code", deptvo.getCode());// 报检部门编码
			hjsonObject.put("applydept_name", deptvo.getName());// 报检部门名称
		}

		hjsonObject.put("productionLline", "");// 产线

		if (orgvo != null) {
			hjsonObject.put("org_code", orgvo.getCode());// 所属公司编码
			hjsonObject.put("org_name", orgvo.getName());// 所属公司
		
		}

		hjsonObject.put("u_group_code", "G");// 所属集团

		//Lims 支持单行报检 只取首行
		PMOItemVO bvo=bvos[0];
		ipcVO.setQc_point(otherpms.get("bjd").toString());//报检点
		//查询报检次数
		ipcVO.setQc_times(1);//默认1
//		按照报检点查询报检次数,记录IPC报检次数
		String str ="CPMOHID='"+bvo.getCpmohid()+"' and CMOID='"+bvo.getCmoid()+"' and QC_POINT='"+ipcVO.getQc_point()+"' and dr=0";
		LimsIpcVO[] limsIpcVOs=(LimsIpcVO[])this.getPubBO().queryByCondition(LimsIpcVO.class, str);
		if(null!=limsIpcVOs && limsIpcVOs.length>0){
			ipcVO.setQc_times(limsIpcVOs.length+1);
		}
		//按照批次查询报检次数,用来同步LIMS
		int  batch_qctimes = 1;
		 str ="CPMOHID='"+bvo.getCpmohid()+"' and CMOID='"+bvo.getCmoid()+"' and dr=0";
		 limsIpcVOs=(LimsIpcVO[])this.getPubBO().queryByCondition(LimsIpcVO.class, str);
		if(null!=limsIpcVOs && limsIpcVOs.length>0){
			batch_qctimes =limsIpcVOs.length+1;
		}
		
		hjsonObject.put("vbillcode", vo.getParentVO().getVbillcode());// 来源单据号
		hjsonObject.put("bill_id", vo.getPrimaryKey());// 来源单据ID
		hjsonObject.put("billitem_id", bvo.getPrimaryKey());// 来源单据明细ID
		
		
		
		ipcVO.setCpmohid(vo.getPrimaryKey());
		ipcVO.setCmoid(bvo.getPrimaryKey());
		
		MaterialVO matervo = mmap.get(bvo.getCmaterialid());

		hjsonObject.put("material_code", matervo.getCode());// 物料编码
		hjsonObject.put("material_name", matervo.getName());// 物料名称
		hjsonObject.put("materialspec", matervo.getMaterialspec());// 规格
		hjsonObject.put("materialtype", matervo.getMaterialtype());// 型号
		
		ipcVO.setCmaterialid(matervo.getPrimaryKey());
		
		hjsonObject.put("vbatchcode", bvo.getVbatchcode());// 批次号
		hjsonObject.put("vvendbatchcode", "");// 供应商批次号

		UFDateTime u_finisheddt = new UFDateTime();
		String u_manufacturedt = u_finisheddt.toString();
		if(StringUtils.isEmpty(u_manufacturedt)||"~".equalsIgnoreCase(u_manufacturedt)){
			throw new BusinessException("请维护报告表头生产日期(vdef10)");
		}
		hjsonObject.put("dproducedate",u_manufacturedt);
		//复测日期
		UFDate u_expirydt = getU_expirydt(u_manufacturedt,materialStor);
		hjsonObject.put("dvalidate",u_expirydt.toString());
		
		hjsonObject.put("cvendor_code", "");
		hjsonObject.put("cvendor_name", "");
		hjsonObject.put("cproductor_code", "");
		hjsonObject.put("cproductor_name", "");
		hjsonObject.put("supgrade","");
		
		MeasdocVO mesvo = memap.get(bvo.getCunitid());
		if (mesvo != null) {
			hjsonObject.put("cunit_name", mesvo.getName());// 主单位名称
		}
		hjsonObject.put("num", bvo.getNnum().toDouble());// 报检主数量

		 mesvo = memap.get(bvo.getCastunitid());
		 if (mesvo != null) {
			 hjsonObject.put("castunit", mesvo.getName());// 单位
		 }
		if(bvo.getNastnum() !=null){
			hjsonObject.put("nastnum", bvo.getNastnum().toDouble());// 容器数量

		}
		 hjsonObject.put("def1",otherpms.get("bjd").toString());// 预留1===报检点
		 hjsonObject.put("def2", batch_qctimes);// 预留2===报检次数 默认1
		//查询报检次数
		 /*LimsIpcVO[] ipcVOs=(LimsIpcVO[])this.getPubBO().queryByCondition(LimsIpcVO.class, "cpmohid='"+vo.getPrimaryKey()+"' and cmoid='"+bvo.getPrimaryKey()+"' and dr=0");
		 if(null!=ipcVOs && ipcVOs.length>0){
			 hjsonObject.put("def2", ipcVOs.length);
		 }*/

		 
		 hjsonObject.put("def3", "");// 预留3
		 hjsonObject.put("def4", "");// 预留4
		 hjsonObject.put("def5", "");// 预留5
		 
		 //日志记录
		 limsLogVO.setSys_id(sysFlowNO);
		 limsLogVO.setCtrantype(ctrantype+"");
		 limsLogVO.setQctime(ufDateTime.toString());
		 limsLogVO.setTs(ufDateTime.toString());
		 limsLogVO.setQcpoint(ipcVO.getQc_point());
		 limsLogVO.setCuserid(AppContext.getInstance().getPkUser());
		 limsLogVO.setUsercode(uservo.getUser_code());
		 limsLogVO.setUsername(uservo.getUser_name());
		 limsLogVO.setPk_org(vo.getParentVO().getPk_org());
		 limsLogVO.setPk_org_v(vo.getParentVO().getPk_org_v());
		 limsLogVO.setQccount(ipcVO.getQc_times()+"");
		 limsLogVO.setVbillid(bvo.getPrimaryKey());
		 limsLogVO.setVbill_bid(bvo.getPrimaryKey());
		 limsLogVO.setVbillcode(vo.getParentVO().getVbillcode());
			
		//第三层
		Map<String,Object> data  = getHeadData();
        data.put("billCode",vo.getParentVO().getVbillcode());
        data.put("targetrule","LIMS_CHECK_"+ctrantype);
        data.put("data",hjsonObject); 
        
        //组装
	  	String reJson = JSON.toJSONString(data);
	  	Log.getInstance("limsIPC").error(reJson);
	  	return  reJson;
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
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(u_expirydt.toDate());
		if(0 ==qualityunit){
			calendar.add(Calendar.YEAR, qualitynum);
		}
		if(1 ==qualityunit){
			calendar.add(Calendar.MONTH, qualitynum);
		}
		if(2 ==qualityunit){
			calendar.add(Calendar.DATE, qualitynum);
		}
		calendar.add(Calendar.DATE, -1);
		u_expirydt =  new UFDate(calendar.getTime());
		return u_expirydt;
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
