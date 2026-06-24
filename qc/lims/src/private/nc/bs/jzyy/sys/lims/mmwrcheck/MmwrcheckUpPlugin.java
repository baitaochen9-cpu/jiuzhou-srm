package nc.bs.jzyy.sys.lims.mmwrcheck;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.bs.jzyy.sys.lims.logger.LimsLogger;
import nc.itf.jzyy.sys.lims.LimsLogVO;
import nc.jdbc.framework.exception.DbException;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringEscapeUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 1、ERP生产报告调用LIMS回写接口
 * @author yunfeng.li
 *
 */
public class MmwrcheckUpPlugin {
	
	boolean islogg=false;
	LimsLogVO logvo =null;
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
	//判断是否同步
	public Object sys(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		AggWrVO newvo = (AggWrVO) obj;
		//执行同步
		 process(billltypecode, newvo, otherpms);
		 return newvo;
	}
	private Object process(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		AbstractSender4LIMS sender = new MmwrcheckUpSender();
		String pk_log=null;
		if(otherpms == null )
			otherpms = new HashMap<String, Object>();
		try {
			if(islogg){
				addNewLog(obj);
				pk_log = logvo.getPk_log();
				otherpms.put("logvo", logvo);
			}			
			JSONObject resp = (JSONObject)sender.process(billltypecode, obj, otherpms);			
			//更新日志
			if(islogg){
				String endTime= getLogger().getCurTime();
				String sql = " update "+LimsLogVO.table_name+" set  isSuccess='Y',errorinfor='', resTime='" + endTime+ "' where pk_log='" + logvo.getPk_log() + "'";
				getLogger().addlog(sql);
			}
			return resp;
			
		} catch(Exception e){
			//更新日志
			if(islogg){
				//有些日志返回的带sql语句,一些特殊字符,需要转义处理下
				String errormsg = StringEscapeUtils.escapeSql(e.getMessage());
			
				try {
					getLogger().addlog(" update "+LimsLogVO.table_name+" set  isSuccess='N',errorinfor='"+errormsg+"' where pk_log='"+pk_log+"'");
				}  catch (Exception e1) {
					e1.printStackTrace();
					throw new BusinessException("同步LIMS失败:"+e1.getMessage());
				} 
			}
			
			throw new BusinessException("同步LIMS失败:"+e.getMessage());
		}	
	
		
	}
	private void addNewLog(Object obj) throws BusinessException, DbException, SQLException {
		AggWrVO  billvo = (AggWrVO ) obj;
		//新增日志
		String busitype = "流程生产订单调用LIMS报检接口";
		String curTime = getLogger().getCurTime();
	     logvo = new LimsLogVO();
		logvo.setTaget(busitype);
		logvo.setPk_bill(billvo.getParentVO().getPrimaryKey());// 主键
		logvo.setVbillno(billvo.getParentVO().getVbillcode());// 单据号
		logvo.setSendDate(getLogger().getCurDate().toString());
		logvo.setSendTime(curTime);// 时间
		logvo.setBusitype(busitype);
//		logvo.setVdef1();
		getLogger().insertLog(logvo);
	
		
	}
	

}
