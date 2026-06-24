package nc.bs.jzyy.sys.thlims.mmwrcheck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.ThLimsCheckRule;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.bs.logging.Log;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP生产报告调用LIMS回写接口
 * 
 * @author liyf
 * 
 */
public class MmwrcheckUpSender extends AbstractSender4ThLims {

	public static final String functype = "nc_th_inspection_write";
	WrItemVO[] bvos = null;
	AggWrVO vo = null;

	@Override
	public void init() throws Exception {
		this.vo=(AggWrVO)this.getParam();
		this.bvos=this.vo.getChildrenVO();
		limsLogVO=new THLimsLogVO();
	}
	public Object afterSend(JSONObject response) throws Exception {
		String succeed = response.getString("succeed");
		if (!"Y".equals(succeed)) {
			Log.getInstance("泰华Lims生产报告请验回传").error(response.toJSONString());
			throw new BusinessException(response.getString("message"));
		} else {
			String syncid = response.getString("ID");
			/*
			 * 3. 因为是合并报检 所以要根据物料+批次号 重新查询表体信息
			 * */
			List<WrItemVO> updateList=new ArrayList<WrItemVO>();
			StringBuffer q_sql=new StringBuffer(" pk_wr='"+vo.getPrimaryKey()+"' and dr=0");
			WrItemVO bvo = bvos[0];
			//更新表体状态
			q_sql.append(" and cbmaterialid='"+bvo.getCbmaterialid()+"' ");
			q_sql.append(" and vbbatchcode  ='"+bvo.getVbbatchcode()+"'");

			WrItemVO[] itemVOs=(WrItemVO[])this.getPubBO().queryByCondition(WrItemVO.class,q_sql.toString());
			if(null!=itemVOs && itemVOs.length>0){
				for (WrItemVO itemVO : itemVOs) {
					//
//					 bbchkflag  检验标识  
					//bbstockbycheck  依据检验结果入库 
					itemVO.setBbstockbycheck(UFBoolean.TRUE);
					//nbsldchecknum  已报检主数量, 
					itemVO.setNbsldchecknum(itemVO.getNbwrnum());
					//nbsldcheckastnum  已报检数量 ,
					itemVO.setNbsldcheckastnum(itemVO.getNbwrastnum());
					itemVO.setVbdef11(response.getString("Lims_billcode"));
					itemVO.setVbdef12(syncid);
					itemVO.setStatus(VOStatus.UPDATED);
					updateList.add(itemVO);
				}
				//更新同步标识
				WrItemVO[] updateArry = updateList.toArray(new WrItemVO[updateList.size()]);
				new VOUpdate().update(updateArry,new String[] {"vbdef11","vbdef12","bbstockbycheck","nbsldchecknum","nbsldcheckastnum"});
				vo.setChildrenVO(updateArry);
			}
			
		 // 生产批次更新报检
			String batch_sql="update pd_pb set vdef4=to_number(decode(vdef4,'~',0,null,0,vdef4))+1 where  cmaterialid='"+bvo.getCbmaterialid()+"' and vprodbatchcode='"+bvo.getVbbatchcode()+"' ";
			/**
			 * 1. 更新批次状态 在检验
			 * 
			 * 2. 批次号档案增加字段【报检次数 vdef4】字段默认为空，取值向LIMS请验时NVL(报检次数，0)；执行LIMS接口调用后，批次号档案报检次数加1
			 */
			getDao().executeUpdate(batch_sql);
			//如果库存批次也已经有值,则更新一下库存批次
			if(StringUtils.isNotEmpty(bvo.getVbinbatchid())){
				batch_sql="update scm_batchcode set Binqc='Y',vdef4=to_number(decode(vdef4,'~',0,null,0,vdef4))+1  where  cmaterialvid ='"+bvo.getCbmaterialid()+"' and  pk_batchcode='"+bvo.getVbinbatchid()+"'";
				getDao().executeUpdate(batch_sql);
			}
		
		}
		return response;
	}

	
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}
	

	@Override
	public String getSendJson()
			throws Exception {
		AggWrVO newvo = (AggWrVO) vo;
		String jsonObj = changeTOJson(newvo);
		Log.getInstance("泰华检验Lims").info("报文："+jsonObj);
		return jsonObj;
	}
	
	private String changeTOJson(AggWrVO vo) throws BusinessException {
		Set<String> pk_measdoc = new HashSet<String>();
		
		Set<String> pk_marterial_vs = new HashSet<String>();
		for (WrItemVO bvo : bvos) {
			if (!StringUtil.isEmpty(bvo.getCbunitid())) {
				pk_measdoc.add(bvo.getCbunitid());
			}
			
			if (!StringUtil.isEmpty(bvo.getCbmaterialid())) {
				pk_marterial_vs.add(bvo.getCbmaterialid());
			}
		}

		Map<String, MeasdocVO> memap = getMeasdocVOMap(pk_measdoc);
		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());
		// List<JSONObject> list = new ArrayList<>();

		JSONObject billJson = new JSONObject();
		billJson.put("functype", functype);
		//实际只有一行，且LIMS报文也是单行的结构，所以此处暂时保留循环,假如支持多行，直接修改报文结构
		HashMap<String, Object> materialStor = getMaterialStor(vo.getParentVO().getPk_org(), bvos[0].getCbmaterialid());
		
		//物料信息
		Map<String, MaterialVO> mmap = getMaterialVOMap(pk_marterial_vs);
		/**
		 * 1=采购报检
		   2=生产完工报检
		   3=库存报检（复验）
		   4=中控检验
		   5=清洁批报检
		 */
		
		
		//报检类型
		UFDateTime ufDateTime=new UFDateTime();
		int ctrantype =2;
		//报检ID
		String sysFlowNO="SC28_";
		if("55A2-Cxx-28-04".equals(vo.getChildrenVO()[0].getVbsrctranstype())){
			//清洁报检
			ctrantype=5;
			sysFlowNO="QJ28_";
		}
		sysFlowNO=sysFlowNO.concat(vo.getParentVO().getVbillcode()+"_"+ufDateTime.toString(TimeZone.getTimeZone("GMT+08:00"),new SimpleDateFormat("yyyyMMddhhmmss")));
		billJson.put("ctrantype", ctrantype);// 业务类型
		billJson.put("ts", ufDateTime.toString());// 报检时间
		if (null!=uservo) {
			billJson.put("usercode", uservo.getUser_code());// 报检人编码
			billJson.put("username", uservo.getUser_name());// 报检人名称
		
		}
		//部门信息
		OrgVO deptvo = getOrgVO(bvos[0].getCbdeptid());
		if (null!=deptvo) {
			billJson.put("applydept_code", deptvo.getCode());// 报检部门编码
			billJson.put("applydept_name", deptvo.getName());// 报检部门名称
		}
		billJson.put("productionLline", "");// 产线
		//组织信息
		OrgVO orgvo = getOrgVO(vo.getParentVO().getPk_org());
		if (null!=orgvo) {
			billJson.put("org_code", orgvo.getCode());// 所属公司编码
			billJson.put("org_name", orgvo.getName());// 所属公司
			
		}
		billJson.put("group", "G");// 所属集团

		ThLimsCheckRule limsCheckRule = new ThLimsCheckRule();

		for (WrItemVO bvo : bvos) {
			billJson.put("ID", sysFlowNO);// 同步号
			
			billJson.put("vbillcode", vo.getParentVO().getVbillcode());// 来源单据号
			billJson.put("bill_id", vo.getParentVO().getPrimaryKey());// 来源单据ID
			billJson.put("billitem_id", bvo.getPrimaryKey());// 来源单据明细ID
			
			
			MaterialVO matervo = mmap.get(bvo.getCbmaterialid());

			billJson.put("material_code", matervo.getCode());// 物料编码
			billJson.put("material_name", matervo.getName());// 物料名称
			billJson.put("materialspec", matervo.getMaterialspec());// 规格
			billJson.put("materialtype", matervo.getMaterialtype());// 型号
			
			billJson.put("vbatchcode", bvo.getVbinbatchcode());//生产  getVbbatchcode  入库批次号getVbinbatchcode()
//			billJson.put("vvendbatchcode", "");// 供应商批次号
			
			//UFDateTime u_finisheddt = new UFDateTime();
			//泰华 dbilldate  干燥日期
			String u_manufacturedt = vo.getParentVO().getDbilldate().toString();
			/*
			 * 生产日期	u_manufacturedt	String	16	Y	YYYY-MM-DD hh:mm
			if(StringUtils.isEmpty(u_manufacturedt)||"~".equalsIgnoreCase(u_manufacturedt)){
				throw new BusinessException("请维护报告表头生产日期(vdef10)");
			}
			*billJson.put("dproducedate",u_manufacturedt);
			*/
			//dproducedate==表头dbilldate
			billJson.put("dproducedate",vo.getParentVO().getDbilldate().toString());
			
			//复测日期
			UFDate u_expirydt = getU_expirydt(u_manufacturedt,materialStor);
			billJson.put("dvalidate",u_expirydt.toString());
			
			billJson.put("cvendor_code", "");
			billJson.put("cvendor_name", "");
			billJson.put("cproductor_code", "");
			billJson.put("cproductor_name", "");
			billJson.put("supgrade","");
			
			
			MeasdocVO mesvo = memap.get(bvo.getCbunitid());
			if (null!=mesvo) {
				billJson.put("cunit_name", mesvo.getName());//  实际产出单位名称
			}
			billJson.put("num", bvo.getNbwrnum().doubleValue());// 实际产出数量
			
			//辅数量==默认为件
			billJson.put("castunit","件");
			/*mesvo = memap.get(bvo.getCbastunitid());
			 if (null!=mesvo) {
				 billJson.put("castunit", mesvo.getName());// 单位
			 }*/
			// 包装数量==def7
			//包装数量==表体def3 所对应自顶档案的def2 2022年12月30日
			billJson.put("nastnum",1);
			UFDouble npacknum = limsCheckRule.isVar(bvo.getVbfree2(), bvo.getNbwrastnum());
			 billJson.put("nastnum",npacknum.toDouble()); 
			 
			 billJson.put("sfwdx", "");//是否需要稳定性检验
			 if(StringUtils.isNotEmpty(bvo.getVbprodbatdef1())){
				 billJson.put("sfwdx", bvo.getVbprodbatdef1());
			 }
			 billJson.put("sfwsw", "");//是否需要微生物检验
			 if(StringUtils.isNotEmpty(bvo.getVbprodbatdef2())){
				 billJson.put("sfwsw", bvo.getVbprodbatdef2());
			 }
			 billJson.put("sfyzp", "");//是否验证批
			 if(StringUtils.isNotEmpty(bvo.getVbprodbatdef3())){
				 billJson.put("sfyzp",bvo.getVbprodbatdef3());
			 }
			 billJson.put("yzfnh", "");//验证方案号
			 if(StringUtils.isNotEmpty(bvo.getVbdef14())){
				 billJson.put("yzfnh", bvo.getVbdef14());
			 }
			 billJson.put("gzrq", vo.getParentVO().getDbilldate().toString());//干燥日期
			
			 //来源产品
			 billJson.put("def1", "");// 预留1
			 //billJson.put("Lycp", "");
			 if(StringUtils.isNotEmpty(bvo.getVbdef15())){
				 MaterialVO materialVO=(MaterialVO)this.getPubBO().queryByPrimaryKey(MaterialVO.class, bvo.getVbdef15());
				 if(null!=materialVO){
					 billJson.put("def1", materialVO.getName());
				 }
			 }
			 //来源产品批次
			 billJson.put("def2", "");// 预留2
			 //billJson.put("Lycppc", "");
			 if(StringUtils.isNotEmpty(bvo.getVbdef16())){
				 billJson.put("def2", bvo.getVbdef16());
			 }
			 billJson.put("def3", "");// 预留3
			 billJson.put("def4", "");// 预留4
			 billJson.put("def5", "");// 预留5
			 
			limsLogVO.setSys_id(sysFlowNO);
			limsLogVO.setCtrantype(ctrantype+"");
			limsLogVO.setQctime(ufDateTime.toString());
			limsLogVO.setTs(ufDateTime.toString());
			limsLogVO.setCuserid(AppContext.getInstance().getPkUser());
			limsLogVO.setUsercode(uservo.getUser_code());
			limsLogVO.setUsername(uservo.getUser_name());
			limsLogVO.setPk_org(vo.getParentVO().getPk_org());
			limsLogVO.setPk_org_v(vo.getParentVO().getPk_org_v());
			limsLogVO.setVbillid(bvo.getPrimaryKey());
			limsLogVO.setVbill_bid(bvo.getPrimaryKey());
			limsLogVO.setVbillcode(vo.getParentVO().getVbillcode());
			limsLogVO.setQccount("0");
		}
		//第三层
        Map<String,Object> data  = getHeadData();
        data.put("billCode",vo.getParentVO().getVbillcode());
        data.put("targetrule","LIMS_CHECK_"+ctrantype);
        data.put("data",billJson); 
        //组装
	  	String reJson = JSON.toJSONString(data);

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


}
