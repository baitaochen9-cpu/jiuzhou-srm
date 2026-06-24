package nc.bs.jzyy.sys.thlims.iccheck;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.ThLimsCheckRule;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.ic.m4z.entity.FreezeThawVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 1、ERP库存检验冻结单调用LIMS报检接口
 * @author yunfeng.li
 *
 */
public class IccheckUpPlugin {
	
	boolean islogg=false;
	


	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	//判断是否同步
	public Object sys(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		
		FreezeThawVO[] bvos = (FreezeThawVO[]) obj;
		Set<String> pk_onhanddims = new HashSet<String>();
		for (FreezeThawVO bvo : bvos) {
			/*String vbdef11 = bvo.getCcorrespondcode();//LIMS报检单号
			if("~".equalsIgnoreCase(vbdef11) || StringUtils.isEmpty(vbdef11)){
				
			}else{
				throw new BusinessException("已报检未返回结果,不允许重复报检");
			}*/
			//2023年2月12日 查询对应的批次 在检 状态 如果是在检 则返回 已报检未返回结果,不允许重复报检
			//1. 查询现存量维度
			String q_sql="select  ic.pk_onhanddim,scm.cmaterialoid,scm.pk_batchcode,scm.vbatchcode,scm.binqc  from  ic_onhanddim ic,scm_batchcode scm where ic.pk_onhanddim=? and ic.pk_batchcode=scm.pk_batchcode";
			SQLParameter parameter=new SQLParameter();
			parameter.addParam(bvo.getPk_onhanddim());
			
			HashMap<String, Object> hashMap = (HashMap<String, Object>) this.getDao().executeQuery(q_sql, parameter, new MapProcessor());
			if(null!=hashMap && null!=hashMap.get("binqc") && "Y".equals(hashMap.get("binqc").toString())){
				throw new BusinessException("该批次记录已报检未返回结果,不允许重复报检!");
			}
			if (!StringUtil.isEmpty(bvo.getPk_onhanddim())) {
				pk_onhanddims.add(bvo.getPk_onhanddim());
			}
		}
		Map<String, OnhandDimVO> dimVOS = getOnhandDimVOMap(pk_onhanddims);
		otherpms.put("dimVOS", dimVOS);
		ThLimsCheckRule checkRule = new ThLimsCheckRule();
		for (OnhandDimVO bvo : dimVOS.values()) {
			String pk_material = bvo.getCmaterialvid();
			String pk_org = bvo.getPk_org();
			
			checkRule.isQcCtr(pk_org, pk_material);//检查是否免检，免检会直接报错
			checkRule.isNextMaterCtr(pk_material);//检查是否直接下一步物料
		}
		//执行同步
		 process(billltypecode, obj, otherpms);
		return bvos;
	}
	
	private Map<String, OnhandDimVO> getOnhandDimVOMap(
			Set<String> pk_onhanddims) throws BusinessException {

		Map<String, OnhandDimVO> mmap = new HashMap<String, OnhandDimVO>();
		if (!CollectionUtils.isEmpty(pk_onhanddims)) {
			String sql = " select *  from  ic_onhanddim l ";
			sql = sql + " where nvl(l.dr,0)= 0  ";
			String newWherePart = SQLUtil
					.buildSqlForIn("pk_onhanddim", pk_onhanddims
							.toArray(new String[pk_onhanddims.size()]));
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
	
	
	private Object process(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		AbstractSender4ThLims sender = new IccheckUpSender();
		if(otherpms == null ){
			otherpms = new HashMap<String, Object>();
		}
		JSONObject resp=null;
		try {
			resp = (JSONObject) sender.process(billltypecode, obj, otherpms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		THLimsLogVO limsLogVO = sender.getLimsLogVO();
		if(null!=limsLogVO){
			this.addNewLog(limsLogVO);
		}
		return resp;
	}
	
	private ISysDispatcherThLims pross;
	private ISysDispatcherThLims getPross() {
		if(pross == null){
			pross = NCLocator.getInstance().lookup(ISysDispatcherThLims.class);
		}
		return pross;
	}
	
	private void addNewLog(SuperVO obj) {
		 try {
			this.getPross().dispatch_RequiresNew(obj, "LIMS_SYS_LOGGER", null);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

}
