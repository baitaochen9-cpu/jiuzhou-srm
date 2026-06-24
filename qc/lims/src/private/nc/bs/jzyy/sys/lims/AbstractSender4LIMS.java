package nc.bs.jzyy.sys.lims;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.lims.logger.LimsLogger;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.lims.LimsLogVO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.sm.UserVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;


public abstract class AbstractSender4LIMS{
	
//	static Logger logger1 = Logger.getLogger(AbstractSender4LIMS.class);
	
    Object param = null;//业务组建拼装传来的参数
    Map<String,Object>  otherParam = null;
	private BaseDAO dao = null;
//	private static final String WEBURL ="http://10.8.9.149:8080/api/operation";
	private static final String appkey ="iYax2jY9XCMofvRVg5BpA6BkAZ4Qcs3tCN39W3HHjB2gL+TCWoT8Jt0GvTRUJLQazhhYnQn6CiYLJRYYnqHsDobIf0UETMRhUKAnTpA32ImelFbbc5PvbvrNfBMGZw==";
	private static final String extsyscode ="NC";

    protected BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}    
    
    LimsLogger logger = new LimsLogger();
    
    public LimsLogger getLogger() {
    	if(logger == null ){
    		logger = new LimsLogger();
    	}
		return logger;
	}
	String getStringNullAssEmpoty(Object value){
		if (value == null) {
			return  "";
		}
		return value.toString().trim();
	}
	public  Object process(String posttype, Object obj,
			Map<String,Object> otherpms) throws Exception{
		this.param = obj;
		this.otherParam = otherpms;
		
		init(obj,otherpms);
		
		String sendJson = null;
		String response = null;
		 //记录日志
		LimsLogVO logvo = (LimsLogVO) otherpms.get("logvo");
		//组装同步的报文 
		sendJson = getSendJson(obj,otherpms);
		SQLParameter parameter = new SQLParameter();
		if(logvo!=null){
			parameter.addClobParam(sendJson);
			getLogger().addlog(" update "+LimsLogVO.table_name+" set sendJson=? where pk_log='"+logvo.getPk_log()+"'", parameter);
		}
		//执行发送接口
		response = (String)send(sendJson);
		if(logvo!=null){
			parameter.clearParams();
			parameter.addClobParam(response);
			getLogger().addlog(" update "+LimsLogVO.table_name+" set resJson=? where pk_log='"+logvo.getPk_log()+"'",parameter);
		}
	
		if(response == null ||  StringUtils.isEmpty(response.toString())){
			 throw new BusinessException("回执为空:"+response);
		}
		JSONObject resJsonObj = JSONObject.parseObject(response.toString());
		Object rs = afterSend(resJsonObj);
	    return rs;
	}
	
	public String getBillPk(){
		return (String) this.otherParam.get("billpk");
	}
	
	
	

	public String getSysFlowNo() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}
	
	public abstract void init(Object obj,
			Map<String,Object> otherpms) throws Exception;
	
	protected abstract String getSendJson(Object obj,
			Map<String,Object> otherpms) throws Exception;
	protected abstract Object send(String sendJson) throws Exception;
	
	protected abstract Object afterSend( JSONObject response) throws Exception;
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	public Map<String, Object> getOtherParam() {
		return otherParam;
	}
	public void setOtherParam(Map<String, Object> otherParam) {
		this.otherParam = otherParam;
	}
	
    private IProcessService processService;

	private IProcessService getProcessService() {
        if (processService == null) {
            processService = NCLocator.getInstance().lookup(IProcessService.class);
        }
        return processService;
    }
	
	
	/**
	 * 字符串判断空值
	 * @param value
	 * @return
	 */
	public String getNullAsEmpty(Object value) {
		if(value == null) {
			return "";
		}
		return value.toString().trim();
	}
	
	/**
	 * 数值判断空值
	 * @param value
	 * @return
	 */
	public double getNullAsZero(UFDouble value) {
		if(value == null) {
			return 0.00;
		}
		return value.doubleValue();
	}
	

	public Map<String, DefdocVO> getDefdocVOMap(Set<String> pk_marterial_vs)
			throws BusinessException {

		Map<String, DefdocVO> mmap = new HashMap<String, DefdocVO>();
		if (!CollectionUtils.isEmpty(pk_marterial_vs)) {
			String sql = " select l.pk_defdoc,l.code,l.name from  bd_defdoc l ";
			sql = sql + " where nvl(l.dr,0)= 0  ";
			String newWherePart = SQLUtil
					.buildSqlForIn("pk_defdoc", pk_marterial_vs
							.toArray(new String[pk_marterial_vs.size()]));
			sql = sql + " and " + newWherePart;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<DefdocVO> list = (List<DefdocVO>) bs.executeQuery(sql,
					new BeanListProcessor(DefdocVO.class));
			if (!CollectionUtils.isEmpty(list)) {

				for (DefdocVO vo1 : list) {
					mmap.put(vo1.getPk_defdoc(), vo1);
				}
			}
		}
		return mmap;
	}
	
	/**
	 * 查询物料库存信息
	 * @param pk_org 库存组织
	 * @param pk_material
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, Object> getMaterialStor(String pk_org,String pk_material) throws BusinessException {
		//  stockbycheck  根据检验结果入库  
//		qualitymanflag  保质期管理  qualitymanflag char(1)  UFBoolean      
//		qualitynum  保质期  qualitynum smallint(4)  Integer      
//	    qualityunit  保质期单位   0=年，1=月，2=日，

		String sql = " select stockbycheck,qualitymanflag ,  qualitynum ,qualityunit     from bd_materialstock where pk_material='"
				+ pk_material + "' and   pk_org ='" + pk_org + "' and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		HashMap<String, Object> invInfor = (HashMap<String, Object>) bs.executeQuery(sql, new MapProcessor());
		return invInfor;
	}

	public Map<String, MeasdocVO> getMeasdocVOMap(Set<String> pk_marterial_vs)
			throws BusinessException {

		Map<String, MeasdocVO> mmap = new HashMap<String, MeasdocVO>();
		if (!CollectionUtils.isEmpty(pk_marterial_vs)) {
			String sql = " select l.pk_measdoc,l.code,l.name from  bd_measdoc l ";
			sql = sql + " where nvl(l.dr,0)= 0  ";
			String newWherePart = SQLUtil
					.buildSqlForIn("pk_measdoc", pk_marterial_vs
							.toArray(new String[pk_marterial_vs.size()]));
			sql = sql + " and " + newWherePart;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<MeasdocVO> list = (List<MeasdocVO>) bs.executeQuery(sql,
					new BeanListProcessor(MeasdocVO.class));
			if (!CollectionUtils.isEmpty(list)) {

				for (MeasdocVO vo1 : list) {
					mmap.put(vo1.getPk_measdoc(), vo1);
				}
			}
		}
		return mmap;
	}

	public Map<String, OnhandDimVO> getOnhandDimVOMap(
			Set<String> pk_marterial_vs) throws BusinessException {

		Map<String, OnhandDimVO> mmap = new HashMap<String, OnhandDimVO>();
		if (!CollectionUtils.isEmpty(pk_marterial_vs)) {
			String sql = " select *  from  ic_onhanddim l ";
			sql = sql + " where nvl(l.dr,0)= 0  ";
			String newWherePart = SQLUtil
					.buildSqlForIn("pk_onhanddim", pk_marterial_vs
							.toArray(new String[pk_marterial_vs.size()]));
			sql = sql + " and " + newWherePart;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<OnhandDimVO> list = (List<OnhandDimVO>) bs.executeQuery(sql,
					new BeanListProcessor(OnhandDimVO.class));
			if (!CollectionUtils.isEmpty(list)) {

				for (OnhandDimVO vo1 : list) {
					mmap.put(vo1.getPk_onhanddim(), vo1);
				}
			}
		}
		return mmap;
	}
	
	public Map<String, BatchcodeVO> getBatchcodeVOMap(
			Set<String> pk_marterial_vs) throws BusinessException {

		Map<String, BatchcodeVO> mmap = new HashMap<String, BatchcodeVO>();
		if (!CollectionUtils.isEmpty(pk_marterial_vs)) {
			String sql = " select *  from  scm_batchcode l ";
			sql = sql + " where nvl(l.dr,0)= 0  ";
			String newWherePart = SQLUtil
					.buildSqlForIn("pk_batchcode", pk_marterial_vs
							.toArray(new String[pk_marterial_vs.size()]));
			sql = sql + " and " + newWherePart;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<BatchcodeVO> list = (List<BatchcodeVO>) bs.executeQuery(sql,
					new BeanListProcessor(BatchcodeVO.class));
			if (!CollectionUtils.isEmpty(list)) {

				for (BatchcodeVO vo1 : list) {
					mmap.put(vo1.getPk_batchcode(), vo1);
				}
			}
		}
		return mmap;
	}

	public Map<String, MaterialVO> getMaterialVOMap(Set<String> pk_marterial_vs)
			throws BusinessException {

		Map<String, MaterialVO> mmap = new HashMap<String, MaterialVO>();
		if (!CollectionUtils.isEmpty(pk_marterial_vs)) {
			String sql = " select l.pk_material,l.pk_source,l.code,l.name,l.materialspec,l.materialtype,s.code pk_marbasclass  from  bd_material l ";
			sql = sql
					+ " join bd_marbasclass s on  l.pk_marbasclass = s.pk_marbasclass ";
			sql = sql + " where nvl(l.dr,0)= 0 and nvl(s.dr,0)= 0   ";
			String newWherePart = SQLUtil
					.buildSqlForIn("pk_material", pk_marterial_vs
							.toArray(new String[pk_marterial_vs.size()]));
			sql = sql + " and " + newWherePart;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<MaterialVO> list = (List<MaterialVO>) bs.executeQuery(sql,
					new BeanListProcessor(MaterialVO.class));
			if (!CollectionUtils.isEmpty(list)) {

				for (MaterialVO vo1 : list) {
					mmap.put(vo1.getPk_material(), vo1);
				}
			}
		}
		return mmap;
	}
	
	public String getgrade(String gradeinfo) throws BusinessException {
		if(gradeinfo == null){
			throw new BusinessException("未知的供应商等级:" + gradeinfo);
		}
		gradeinfo=gradeinfo.trim();
		if ("批准".equals(gradeinfo)) {
			return "2";
		} else if ("认证".equals(gradeinfo)) {
			return "3";
		} else if ("维持".equals(gradeinfo)) {
			return "4";
		} else if ("不批准".equals(gradeinfo)) {
			return "1";
		} else {
			throw new BusinessException("未知的供应商等级:" + gradeinfo);
		}
	}

	public Map<String, SupplierVO> getSupplierVO(Set<String> pk_marterial_vs)
			throws BusinessException {

		Map<String, SupplierVO> mmap = new HashMap<String, SupplierVO>();
		if (!CollectionUtils.isEmpty(pk_marterial_vs)) {
			String sql = " select l.pk_supplier,l.code,l.name,e.suppliergrade name2  from  bd_supplier l ";
			sql = sql
					+ " join bd_supstock s on  l.pk_supplier = s.pk_supplier ";
			sql = sql
					+ " join bd_supplier_grade e on  s.supgrade = e.pk_grade_info ";
			sql = sql + " where nvl(l.dr,0)= 0 and nvl(s.dr,0)= 0   ";

			String newWherePart = SQLUtil
					.buildSqlForIn("l.pk_supplier", pk_marterial_vs
							.toArray(new String[pk_marterial_vs.size()]));
			sql = sql + " and " + newWherePart;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<SupplierVO> list = (List<SupplierVO>) bs.executeQuery(sql,
					new BeanListProcessor(SupplierVO.class));
			if (!CollectionUtils.isEmpty(list)) {
				for (SupplierVO vo1 : list) {
					mmap.put(vo1.getPk_supplier(), vo1);
				}
			}
		}

		return mmap;
	}
	
	public Map<String, SupplierqualityHVO> getGradeInfo(Set<String> pk_marterial_vs,String pk_group,String pk_org)
			throws BusinessException {

		Map<String, SupplierqualityHVO> mmap = new HashMap<String, SupplierqualityHVO>();
		if (!CollectionUtils.isEmpty(pk_marterial_vs)) {
			String sql = " select y.pk_material,y.pk_srcmaterial,y.pk_vendor,e.suppliergrade pk_grade_info from qc_supplierquality y ";
			sql = sql
					+ " join bd_supplier_grade e on y.pk_grade_info = e.pk_grade_info ";
			sql = sql + " where nvl(e.dr,0)= 0 and nvl(y.dr,0)= 0   ";

			String newWherePart = SQLUtil
					.buildSqlForIn("y.pk_supplier", pk_marterial_vs
							.toArray(new String[pk_marterial_vs.size()]));
			sql = sql + " and   y.pk_group = '"+pk_group+"'  and y.pk_org= '"+pk_org+"'";
			
			sql = sql + " and " + newWherePart;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<SupplierqualityHVO> list = (List<SupplierqualityHVO>) bs.executeQuery(sql,
					new BeanListProcessor(SupplierqualityHVO.class));
			if (!CollectionUtils.isEmpty(list)) {
				for (SupplierqualityHVO vo1 : list) {
					String key = vo1.getPk_material()+"&"+vo1.getPk_vendor();
					mmap.put(key, vo1);
				}
			}
		}

		return mmap;
	}
	
	public UserVO getUserVO(String cuserid) throws BusinessException {

		UserVO vo = null;
		if (!StringUtil.isEmpty(cuserid)) {
			String sql = " select user_code ,user_name from  sm_user l ";
			sql = sql + " where nvl(l.dr,0)= 0   ";

			sql = sql + " and cuserid ='" + cuserid + "'";
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<UserVO> list = (List<UserVO>) bs.executeQuery(sql,
					new BeanListProcessor(UserVO.class));
			if (!CollectionUtils.isEmpty(list)) {
				vo = list.get(0);
			}
		}
		return vo;
	}
	
	public OrgVO getOrgVO(String pk_org) throws BusinessException {

		OrgVO vo = null;
		if (!StringUtil.isEmpty(pk_org)) {
			String sql = " select code ,name from  org_orgs l ";
			sql = sql + " where nvl(l.dr,0)= 0   ";

			sql = sql + " and pk_org ='" + pk_org + "'";
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<OrgVO> list = (List<OrgVO>) bs.executeQuery(sql,
					new BeanListProcessor(OrgVO.class));
			if (!CollectionUtils.isEmpty(list)) {
				vo = list.get(0);
			}
		}
		return vo;
	}

	public Map<String, OrgVO> getOrgVOs(Set<String> pk_orgs) throws BusinessException {

		Map<String, OrgVO> mmap = new HashMap<String, OrgVO>();
		if (!CollectionUtils.isEmpty(pk_orgs)) {
			String sql = " select pk_org, code ,name from  org_orgs l ";
			sql = sql + " where nvl(l.dr,0)= 0   ";

			String newWherePart = SQLUtil
					.buildSqlForIn("l.pk_org", pk_orgs
							.toArray(new String[pk_orgs.size()]));
			sql = sql + " and " + newWherePart;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<OrgVO> list = (List<OrgVO>) bs.executeQuery(sql,
					new BeanListProcessor(OrgVO.class));
			if (!CollectionUtils.isEmpty(list)) {
				for (OrgVO vo1 : list) {
					mmap.put(vo1.getPk_org(), vo1);
				}
			}
		}
		return mmap;
	}
	
	// 检查物料是否免检
	public boolean chekQC(String pk_org, String material)
			throws BusinessException {
		String sql = " select chkfreeflag    from bd_materialstock where pk_material='"
				+ material + "' and   pk_org ='" + pk_org + "' and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs
				.executeQuery(sql, new MapProcessor());

		if (hashMap2 != null && hashMap2.size() > 0) {
			UFBoolean b = UFBoolean.valueOf(hashMap2.get("chkfreeflag")
					.toString());
			return b.booleanValue();
		}
		return false;

	}
	

	public  String invoke(String jsonobj) throws Exception {
		String message =  doPost(jsonobj,null);
		return message;
	}
	
	

	private   String doPost(String jsonObj,String url) throws Exception {
//		url="http://127.0.0.1:8080/api/operation";
		if(StringUtil.isEmpty(url)){
			 url =  getProcessService().getSysLIMSIp();
		}
		
		HttpHeaders headers = new HttpHeaders();

		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		headers.add("X-Auth-Token", UUID.randomUUID().toString());
		headers.add("appkey",appkey);
		headers.add("extsyscode", extsyscode);
		
		HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj, headers);
		RestTemplate template = new RestTemplate();
		String result = template.postForObject(url, formEntity, String.class);
//		String result ="{'code':'200','output':{'status':'success','syncid':'dfasf','data':[{'erpitemid':'1001V0100000000889WR','u_extrequestid':'afdaf'}]}}";
		return result;
	}
	
}
