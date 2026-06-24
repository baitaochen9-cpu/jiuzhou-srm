package nc.bs.jzyy.sys.lims.mmpaccheck;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.bs.jzyy.sys.lims.logger.LimsLogger;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.jzyy.sys.lims.LimsLogVO;
import nc.jdbc.framework.exception.DbException;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 1、ERP流程生产订单调用LIMS报检接口
 * @author yunfeng.li
 *
 */
public class MmpaccheckUpPlugin {
	
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
		PMOAggVO bill = (PMOAggVO)obj;
		//前台可能没选择行,重新查询
		BillQuery<PMOAggVO> qry = new BillQuery<PMOAggVO>(PMOAggVO.class);
		PMOAggVO orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		//
		PMOAggVO newvo = (PMOAggVO)  orgiVO.clone();
		PMOItemVO[] bvos = (PMOItemVO[]) newvo.getChildrenVO();
		for (PMOItemVO bvo : bvos) {
			String vbdef11 = bvo.getVdef11();//LIMS报检单号
			if("~".equalsIgnoreCase(vbdef11) || StringUtils.isEmpty(vbdef11)){
				
			}else{
				throw new BusinessException("同步LIMS失败:行:"+bvo.getVrowno()+"已报检,不允许重复报检");
			}
		}
		
		
		//执行同步
		 process(billltypecode, orgiVO, otherpms);
		 //返回前台刷新
		 orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		 return orgiVO;
	}
	private Object process(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		AbstractSender4LIMS sender = new MmpaccheckUpSender();
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
		PMOAggVO  billvo = (PMOAggVO ) obj;
		//新增日志
		String busitype = "流程生产订单调用LIMS报检接口";
		String curTime = getLogger().getCurTime();
	     logvo = new LimsLogVO();
		logvo.setTaget(busitype);
		logvo.setPk_bill(billvo.getChildrenVO()[0].getCmoid());// 主键
		logvo.setVbillno(billvo.getParentVO().getVbillcode());// 单据号
		logvo.setSendDate(getLogger().getCurDate().toString());
		logvo.setSendTime(curTime);// 时间
		logvo.setBusitype(busitype);
//		logvo.setVdef1();
		getLogger().insertLog(logvo);
	
		
	}
	

}
