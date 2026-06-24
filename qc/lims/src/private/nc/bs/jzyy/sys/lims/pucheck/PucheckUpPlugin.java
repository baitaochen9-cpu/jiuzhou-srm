package nc.bs.jzyy.sys.lims.pucheck;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.bs.jzyy.sys.lims.logger.LimsLogger;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.jzyy.sys.lims.LimsLogVO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP采购到货单调用LIMS报检接口
 * 
 * @author yunfeng.li
 * 
 */
public class PucheckUpPlugin {

	boolean islogg = false;
	LimsLogVO logvo = null;
	LimsLogger logger;

	public LimsLogger getLogger() {
		if (logger == null) {
			logger = new LimsLogger();
		}
		return logger;
	}

	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	// 判断是否同步
	public Object sys(String billltypecode, Object obj,
			Map<String, Object> otherpms) throws BusinessException {
		ArriveVO bill = (ArriveVO)  obj;
		//前台可能没选择行,重新查询
		BillQuery<ArriveVO> qry = new BillQuery<ArriveVO>(ArriveVO.class);
		ArriveVO orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		//
		ArriveVO newvo = (ArriveVO)  orgiVO.clone();
		//检查免检数据
		newvo = checkMianJian(newvo,otherpms);
		//如果没有需要同步的,直接返回
		if(newvo == null){
			return  bill;
		}
		ArriveItemVO[] bvos = (ArriveItemVO[]) newvo.getBVO();
		for (ArriveItemVO bvo : bvos) {
			String vbdef11 = bvo.getVbdef11();//LIMS报检单号
			if("~".equalsIgnoreCase(vbdef11) || StringUtils.isEmpty(vbdef11)){
				continue;
			}else{
				throw new BusinessException("同步LIMS失败:行:"+bvo.getCrowno()+"已报检,不允许重复报检");
			}
		}

		// 执行同步
		 process(billltypecode, newvo, otherpms);
		 //返回前台刷新
		 orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		 return orgiVO;

	}
	
	private ArriveVO checkMianJian(ArriveVO vo,Map<String, Object> otherpms) throws BusinessException{
		String pk_org = vo.getHVO().getPk_org();
	   	 ArriveItemVO[] bvos = vo.getBVO();
	     //如果是外系统质检
	  	 List<ArriveItemVO> reList = new ArrayList<ArriveItemVO>();
	  	boolean  shoudong = "手动报检".equalsIgnoreCase((String)otherpms.get("opetype"));
	  		for (ArriveItemVO itemVO : bvos) {
	  			if(itemVO == null){
	  				continue;
	  			}
	  			String material = itemVO.getPk_material();
	  			UFDouble naccumstorenum = itemVO.getNaccumstorenum();
	  			if (naccumstorenum != null
	  					&& naccumstorenum.compareTo(UFDouble.ZERO_DBL) > 0) {
	  				if(shoudong){
			  			throw new BusinessException("已入库,不需要报检.请检查后重新选择数据！");
	  				}else{
		  				continue;
	  				}	  			
	  			}
	  			UFDouble naccumchecknum = itemVO.getNaccumchecknum();
	  			if (naccumchecknum  !=null && naccumchecknum.compareTo(UFDouble.ZERO_DBL) > 0){
	  				if(shoudong){
			  			throw new BusinessException("已报检,不需要报检.请检查后重新选择数据！");
	  				}else{
		  				continue;
	  				}
	  			}
	  		
	  			if (chekQC(pk_org, material)) {
	  				continue;
	  			}
  				reList.add(itemVO);
	  		}
	  		if (reList == null || reList.size() == 0) {
	  			if("手动报检".equalsIgnoreCase((String)otherpms.get("opetype"))){
		  			throw new BusinessException("全部是免检物料,不需要报检.请检查后重新选择数据！");
				}else{
					return null;
				}
		  		
	  		}
	  		
	  		vo.setBVO(reList.toArray(new  ArriveItemVO[0] ));
	  		return vo;
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

	private Object process(String billltypecode, Object obj,
			Map<String, Object> otherpms) throws BusinessException {
		AbstractSender4LIMS sender = new PucheckUpSender();
		String pk_log = null;
		if(otherpms == null )
			otherpms = new HashMap<String, Object>();
		try {
			if (islogg) {
				addNewLog(obj);
				pk_log = logvo.getPk_log();
				otherpms.put("logvo", logvo);
			}

			JSONObject resp = (JSONObject) sender.process(billltypecode, obj,
					otherpms);

			// 更新日志
			if (islogg) {
				String endTime = getLogger().getCurTime();
				String sql = " update " + LimsLogVO.table_name
						+ " set  isSuccess='Y',errorinfor='', resTime='"
						+ endTime + "' where pk_log='" + logvo.getPk_log()
						+ "'";
				getLogger().addlog(sql);
			}
			return resp;

		} catch (Exception e) {
			// 更新日志
			if (islogg) {
				// 有些日志返回的带sql语句,一些特殊字符,需要转义处理下
				String errormsg = StringEscapeUtils.escapeSql(e.getMessage());

				try {
					getLogger().addlog(
							" update " + LimsLogVO.table_name
									+ " set  isSuccess='N',errorinfor='"
									+ errormsg + "' where pk_log='" + pk_log
									+ "'");
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BusinessException("同步LIMS失败:" + e1.getMessage());
				}
			}

			throw new BusinessException("同步LIMS失败:" + e.getMessage());
		}

	}

	private void addNewLog(Object obj) throws BusinessException, DbException,
			SQLException {
		ArriveVO billvo = (ArriveVO) obj;
		// 新增日志
		String busitype = "采购到货单调用LIMS报检接口";
		String curTime = getLogger().getCurTime();
		logvo = new LimsLogVO();
		logvo.setTaget(busitype);
		logvo.setPk_bill(billvo.getBVO()[0].getPrimaryKey());// 主键
		logvo.setVbillno(billvo.getHVO().getVbillcode());// 单据号
		logvo.setSendDate(getLogger().getCurDate().toString());
		logvo.setSendTime(curTime);// 时间
		logvo.setBusitype(busitype);
		// logvo.setVdef1();
		getLogger().insertLog(logvo);

	}

}
