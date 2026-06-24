package nc.bs.jzyy.sys.thlims.iccheck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.ThLimsCheckRule;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.bs.logging.Log;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.ic.m4z.entity.FreezeThawVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.DeptVO;
import nc.vo.org.OrgVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.sm.UserVO;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP库存检验冻结单调用LIMS报检接口
 * 
 * @author liyf
 * 
 */
public class IccheckUpSender extends AbstractSender4ThLims {

	public static final String functype = "nc_th_inspection";
	FreezeThawVO[] vos = null;
	//接口日志
	protected THLimsLogVO limsLogVO;


	
	@Override
	public void init() throws Exception {
		vos = (FreezeThawVO[]) this.getParam();
		limsLogVO=new THLimsLogVO();
	}
	
	@Override
	public Object afterSend(JSONObject response) throws Exception {
		String succeed = response.getString("succeed");
		if (!"Y".equals(succeed)) {
			Log.getInstance("泰华Lims库存请验回传").error(response.toJSONString());
			throw new BusinessException(response.getString("message"));
		} else {
			String syncid = response.getString("ID");
			for (FreezeThawVO bvo : vos) {
				//LIMS检验请求ID
				//String lims_billCode = response.getString("ID");
				bvo.setCcorrespondcode(syncid);
				int rs = this.getDao().executeUpdate("update ic_invfreeze set ccorrespondcode='"+syncid+"',ccorrespondrowno='Y' where pk_group='"+bvo.getPk_group()+"' and pk_org='"+bvo.getPk_org()+"' and pk_onhanddim='"+bvo.getPk_onhanddim()+"' and ccorrespondcode is null and ccorrespondrowno is null");
				
			}			
			/**
			 * 1. 更新批次状态 在检验
			 * 
			 * 2. 批次号档案增加字段【报检次数 vdef4】字段默认为空，取值向LIMS请验时NVL(报检次数，0)；执行LIMS接口调用后，打次号档案报检次数加1
			 */
			List<String> pk_batchcodes = new ArrayList<>();
			for(String pk_onhanddim :ohmap.keySet()){
				OnhandDimVO onhandDimVO = ohmap.get(pk_onhanddim);
				if(!pk_batchcodes.contains(onhandDimVO.getPk_batchcode())){
					pk_batchcodes.add(onhandDimVO.getPk_batchcode());
				}
				
				
			}
			for(String pk_batchcode:pk_batchcodes){
				String batch_sql="update scm_batchcode set Binqc='Y',vdef4=to_number(decode(vdef4,'~',0,null,0,vdef4))+1  where pk_batchcode='"+pk_batchcode+"'";
				getDao().executeUpdate(batch_sql);
			}
		}
		return response;
	}


	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}
	
	@Override
	public String getSendJson()
			throws Exception {
		String pk_org = vos[0].getPk_org();
		String jsonObj = changeTOJson(pk_org,vos);
		Log.getInstance("库存检验Lims").error("库存检验Lims："+jsonObj);
		return jsonObj;
	}
	private Map<String, OnhandDimVO> ohmap;
	private String changeTOJson(String pk_org, FreezeThawVO[] reQueryOnhandVOs)
			throws BusinessException {
		Set<String> pk_marterial_vs = new HashSet<String>();
		Set<String> pk_measdoc = new HashSet<String>();
		Set<String> pk_defdocs = new HashSet<String>();
		Set<String> cvendorids = new HashSet<String>();
		Set<String> pk_batchcodes = new HashSet<String>();
		for (FreezeThawVO bvo : reQueryOnhandVOs) {
		
			if (!StringUtil.isEmpty(bvo.getCunitid())) {
				pk_measdoc.add(bvo.getCunitid());
			}
			if (!StringUtil.isEmpty(bvo.getCastunitid())) {
				pk_measdoc.add(bvo.getCastunitid());
			}
		}
		ohmap = (Map<String, OnhandDimVO>) getOtherParam().get("dimVOS");
		for (OnhandDimVO bvo : ohmap.values()) {
			if (!StringUtil.isEmpty(bvo.getCmaterialoid())) {
				pk_marterial_vs.add(bvo.getCmaterialoid());
			}
			//生产厂商
			if (!StringUtil.isEmpty(bvo.getCproductorid())) {
				pk_defdocs.add(bvo.getCproductorid());
			}
			
			if (!StringUtil.isEmpty(bvo.getPk_batchcode())) {
				pk_batchcodes.add(bvo.getPk_batchcode());
			}
		}

	
		Map<String, MaterialVO> mmap = getMaterialVOMap(pk_marterial_vs);
		Map<String, DefdocVO> dmap = getDefdocVOMap(pk_defdocs);
		Map<String, MeasdocVO> memap = getMeasdocVOMap(pk_measdoc);
		

		//从批次档案vdef14获取供应商
		Map<String, BatchcodeVO> batmap = getBatchcodeVOMap(pk_batchcodes);
		//2025-07-16 一次报检一个物料的一个批次
		if(batmap.size() >1){
			throw new BusinessException("不支持一次报检多批次");

		}
		for(BatchcodeVO batvo:batmap.values()){
		
			//供应商
			if (!StringUtil.isEmpty(batvo.getVdef14())) {
				cvendorids.add(batvo.getVdef14());
			}else{
				MaterialVO materialVO = mmap.get(batvo.getCmaterialoid());
				String marbasclasscode = materialVO.getPk_marbasclass();
				if(marbasclasscode.startsWith("2801")|| marbasclasscode.startsWith("2804")){
					throw new BusinessException(materialVO.getCode() +" "+batvo.getVbatchcode()+"未维护供应商");
				}
			}
		}
		Map<String, SupplierVO> supmap = getSupplierVO(cvendorids);
		//获取用户信息
		UserVO uservo = this.getUserInfo(AppContext.getInstance().getPkUser());
		if(null==uservo){
			throw new BusinessException("当前用户未关联到人员档案信息!");
		}
		OrgVO orgvo = getOrgVO(pk_org);
		//生产商物料状态
		Map<String, SupplierqualityHVO> gmap=null;
		if(null!=cvendorids && cvendorids.size()>0){
			gmap= getGradeInfo(cvendorids, reQueryOnhandVOs[0].getPk_group(), pk_org);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("functype", functype);
		JSONObject hjsonObject = new JSONObject();
		/**
		 * 1=采购报检
		   2=生产完工报检
		   3=库存报检（复验）
		   4=中控检验
		   5=清洁批报检
		 */
		int ctrantype =3;
		UFDateTime ufDateTime=new UFDateTime();
		//String sysFlowNO = getSysFlowNo();
		hjsonObject.put("ctrantype", ctrantype);// 业务类型
		hjsonObject.put("ts", ufDateTime.toString());// 报检时间
	
		
		if (uservo != null) {
			hjsonObject.put("usercode", uservo.getUser_code());// 报检人编码
			hjsonObject.put("username", uservo.getUser_name());// 报检人名称
		}
		
		hjsonObject.put("applydept_code", "");// 报检部门编码
		hjsonObject.put("applydept_name", "");// 报检部门名称
		if(StringUtils.isNotEmpty(uservo.getPk_psndoc())){
			//查询工作信息
			PsnjobVO[] psnjobVOs=(PsnjobVO[])this.getPubBO().queryByCondition(PsnjobVO.class, "PK_PSNDOC='"+uservo.getPk_psndoc()+"' and ISMAINJOB='Y' and ENDDUTYDATE is null and dr=0 and PK_ORG='"+pk_org+"'");
			if(null!=psnjobVOs && psnjobVOs.length>0 && StringUtils.isNotEmpty(psnjobVOs[0].getPk_dept())){
				//查询部门信息
				DeptVO deptVO=(DeptVO)this.getPubBO().queryByPrimaryKey(DeptVO.class, psnjobVOs[0].getPk_dept());
				if(null!=deptVO){
					hjsonObject.put("applydept_code", deptVO.getCode());// 报检部门编码
					hjsonObject.put("applydept_name", deptVO.getName());// 报检部门名称
				}else{
					throw new BusinessException("查询报检部门异常:请维护当前操作人任职部门");

				}
			}else{
				throw new BusinessException("查询报检部门异常:未查询到当前操作人在"+orgvo.getName()+"工作信息");
			}
		}
		
		hjsonObject.put("productionLline", "");// 产线
		
		hjsonObject.put("org_code", orgvo.getCode());// 所属公司编码
		hjsonObject.put("org_name", orgvo.getName());// 所属公司
		
		hjsonObject.put("group", "G");// 所属集团
		//同一个物料相同批次进行汇总
		Map<String,ArrayList<FreezeThawVO>> groupVOs = new HashMap<String,ArrayList<FreezeThawVO>>();
		for (FreezeThawVO bvo : reQueryOnhandVOs) {
			OnhandDimVO onhandvo = ohmap.get(bvo.getPk_onhanddim());
			MaterialVO matervo = mmap.get(onhandvo.getCmaterialoid());
			String key = matervo.getCode()+"&"+onhandvo.getVbatchcode();
			if(groupVOs.containsKey(key)){
				groupVOs.get(key).add(bvo);
			}else{
				ArrayList<FreezeThawVO> details = new ArrayList<FreezeThawVO>();
				details.add(bvo);
				groupVOs.put(key, details);
			}
		}
		ThLimsCheckRule limsCheckRule = new ThLimsCheckRule();
		for(String key:groupVOs.keySet()){
			//JSONObject jsonObject = new JSONObject();
			hjsonObject.put("vbillcode",key);// 来源单据号
			// 来源单据ID
			String u_sourcerequestid ="";
			ArrayList<FreezeThawVO> details = groupVOs.get(key);
			UFDouble nnum = UFDouble.ZERO_DBL;
			UFDouble nastnum = UFDouble.ZERO_DBL;
			for(int i=0;i<details.size();i++){
				FreezeThawVO bvo = details.get(i);
				u_sourcerequestid= u_sourcerequestid+bvo.getPk_onhanddim();
				if(i<details.size()-1){
					u_sourcerequestid=u_sourcerequestid+"&";
				}
				nnum= nnum.add(bvo.getNnum());
				/*
				 * 同物料 同批次下 会有多个包装规格的情况 此处在合并前 就计算辅数量  2023年4月5日
				 * */
				if(StringUtils.isNotEmpty(bvo.getVfree2())){
					UFDouble temp_nastnum=limsCheckRule.dealPackNum(bvo.getVfree2(), bvo.getNnum());
					bvo.setNassistnum(temp_nastnum);
				}
				nastnum=nastnum.add(bvo.getNassistnum());
			}
			hjsonObject.put("bill_id", u_sourcerequestid);// 来源单据明细ID
			hjsonObject.put("billitem_id", u_sourcerequestid);// 来源单据明细ID
			FreezeThawVO bvo = details.get(0);
			OnhandDimVO onhandvo = ohmap.get(bvo.getPk_onhanddim());
			MaterialVO matervo = mmap.get(onhandvo.getCmaterialoid());
			hjsonObject.put("material_code", matervo.getCode());// 物料编码
			hjsonObject.put("material_name", matervo.getName());// 物料名称
			hjsonObject.put("materialspec", matervo.getMaterialspec());// 规格
			hjsonObject.put("materialtype", matervo.getMaterialtype());// 型号
			
			String sysFlowNO="KC28_"+reQueryOnhandVOs[0].getPrimaryKey()+"_"+ufDateTime.toString(TimeZone.getTimeZone("GMT+08:00"),new SimpleDateFormat("yyyyMMddhhmmss"));

			hjsonObject.put("vvendbatchcode", "");// 供应商批次号
			hjsonObject.put("jycs",0);//检验次数
			hjsonObject.put("sffj","否");//是否复检
			BatchcodeVO batvo = batmap.get(onhandvo.getPk_batchcode());
			if (batvo != null) {
				sysFlowNO="KC28_"+batvo.getVbatchcode()+"_"+ufDateTime.toString(TimeZone.getTimeZone("GMT+08:00"),new SimpleDateFormat("yyyyMMddhhmmss"));
				hjsonObject.put("vbatchcode", batvo.getVbatchcode());// 批次号
				hjsonObject.put("dproducedate", batvo.getDproducedate().toString());// 生产日期
				hjsonObject.put("dvalidate", batvo.getDvalidate().toString());// 失效/复测日期
				hjsonObject.put("vvendbatchcode", batvo.getVvendbatchcode());// 供应商批次号
				hjsonObject.put("vvendbatchnum", nnum.toDouble());// 供应商批次数量

				if(null!=batvo.getVdef4() && StringUtils.isNotEmpty(batvo.getVdef4())){
					hjsonObject.put("jycs",Integer.parseInt(batvo.getVdef4()));//检验次数
				}
				if(null!=batvo.getVdef4() && StringUtils.isNotEmpty(batvo.getVdef4())){
					if(Integer.parseInt(batvo.getVdef4())>0){
						hjsonObject.put("sffj","是");//是否复检
					}
				}
			}
			
			hjsonObject.put("ID",sysFlowNO);// 同步号
		
			
			//生产商
			hjsonObject.put("cproductor_code", "");
			hjsonObject.put("cproductor_name", "");
			hjsonObject.put("supgrade","N/A");
			DefdocVO cproductor = dmap.get(onhandvo.getCproductorid());
			if (cproductor != null) {
				hjsonObject.put("cproductor_code", cproductor.getCode());// 生产商编码
				hjsonObject.put("cproductor_name", cproductor.getName());//生产商名称
				String key11 = onhandvo.getCmaterialoid() + "&" + onhandvo.getCproductorid();
				SupplierqualityHVO suppliervo = gmap.get(key11);
//				生产商等级	u_manufacturer_level 1=不批准2=批准3=认证4=维持仅用于类型1，2，3
				if (null!=suppliervo){
					hjsonObject.put("supgrade",suppliervo.getPk_grade_info());// 供应商/生产商等级
					hjsonObject.put("supgradename",suppliervo.getName());// 供应商/生产商等级
				}
			}		

			MeasdocVO mesvo = memap.get(bvo.getCunitid());
			if (mesvo != null) {
				hjsonObject.put("cunit_name", mesvo.getName());// 主单位名称
			}
			hjsonObject.put("num", nnum.toDouble());// 报检主数量
			
			//辅数量==默认为件
			hjsonObject.put("castunit","件");
			/*mesvo = memap.get(bvo.getCastunitid());
			 if (mesvo != null) {
				 hjsonObject.put("castunit", mesvo.getName());// 单位
			 }*/
			// 容器数量===num/vfree7 两位小数向上取整
			//包装数量
			hjsonObject.put("nastnum",1);
			UFDouble npacknum = limsCheckRule.isVar(bvo.getVfree2(), nastnum);
			hjsonObject.put("nastnum",npacknum.toDouble()); 
			/*hjsonObject.put("nastnum",0);
			if(StringUtils.isNotEmpty(onhandvo.getVfree2())){
				nastnum=nnum.div(new UFDouble(onhandvo.getVfree2())).setScale(0,UFDouble.ROUND_UP);
				hjsonObject.put("nastnum",nastnum.toDouble());
			}*/
			
			//原料等级==物料档案def9  2023年2月4日新增
			//def9 改 def10 2023年2月17日
			 hjsonObject.put("yldj", "");
			 MaterialVO materialVO=(MaterialVO)this.getPubBO().queryByPrimaryKey(MaterialVO.class, matervo.getPk_material());
			 if(null!=materialVO && StringUtils.isNotEmpty(materialVO.getDef12())){
				 DefdocVO defdocVO=(DefdocVO) this.getPubBO().queryByPrimaryKey(DefdocVO.class, materialVO.getDef12());
				 if(null!=defdocVO){
					 hjsonObject.put("yldj",defdocVO.getName());
				 }
			 }
			
			 hjsonObject.put("def1", "");// 预留1
			 hjsonObject.put("def2", "");// 预留2
			 hjsonObject.put("def3", "");// 预留3
			 hjsonObject.put("def4", "");// 预留4
			 hjsonObject.put("def5", "");// 预留5
			 
				limsLogVO.setCtrantype(ctrantype+"");
				limsLogVO.setQctime(ufDateTime.toString());
				limsLogVO.setTs(ufDateTime.toString());
				limsLogVO.setSys_id(sysFlowNO);
		}
		//第三层
		Map<String,Object> data  = getHeadData();
        data.put("billCode",reQueryOnhandVOs[0].getPrimaryKey());
        data.put("targetrule","LIMS_CHECK_"+ctrantype);
        data.put("data",hjsonObject); 
        
        //组装
	  	String reJson = JSON.toJSONString(data);
	  	
	
		
	  	return  reJson;
	}



	
	public UserVO getUserInfo(String cuserid) throws BusinessException {
		UserVO vo = null;
		if (!StringUtil.isEmpty(cuserid)) {
			String sql = " select user_code ,user_name,pk_psndoc from  sm_user l ";
			sql = sql + " where nvl(l.dr,0)= 0   ";

			sql = sql + " and cuserid ='" + cuserid + "'";
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			
			HashMap<String, Object> userMap = (HashMap<String, Object>) bs.executeQuery(sql, new MapProcessor());
			if(null!=userMap){
				vo=new UserVO();
				vo.setUser_code(userMap.get("user_code").toString());
				vo.setUser_name(userMap.get("user_name").toString());
				if(null!=userMap.get("pk_psndoc")){
					vo.setPk_base_doc(userMap.get("pk_psndoc").toString());
				}
			}
		}
		return vo;
	}
}
