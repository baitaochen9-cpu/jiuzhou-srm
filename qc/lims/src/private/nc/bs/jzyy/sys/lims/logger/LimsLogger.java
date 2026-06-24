package nc.bs.jzyy.sys.lims.logger;

import java.sql.SQLException;
import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.jzyy.sys.lims.LimsLogVO;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

/**
 * 数据库日志记录,取消文件日志
 * 方便业务追溯
 * @ClassName:     
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 云峰网络 411072655
 * @date:   2020-3-5 下午09:11:12   
 *     
 * @Copyright: 2020 www.yunfeng-net.com Inc. All rights reserved. 
 * 山东云峰网络科技有限公司
 */
public class LimsLogger {
	
	
	public UFDate getCurDate(){
		return new UFDate(System.currentTimeMillis());
	} 
	public String getCurTime(){
		return new UFDateTime(System.currentTimeMillis()).toString();
	}
	private ISysDispatcher pross =null;
	
	
	String billtypecode="LIMS_SYS_LOGGER";
	
	private ISysDispatcher getPross() {
		if(pross == null){
			pross = NCLocator.getInstance().lookup(ISysDispatcher.class);
		}
		return pross;
	}
	/**
	 * 主要为了档案的时间戳字段更新,且不需要更新ts的场景
	 * manager.setAddTimeStamp(false);
	 * @param sql
	 * @throws BusinessException
	 * @throws DbException
	 * @throws SQLException
	 */
	public void executeUpdateNoTS(String sql) throws BusinessException, DbException, SQLException {
		PersistenceManager manager = null;
		try {
			String ds = InvocationInfoProxy.getInstance().getUserDataSource();
			manager = PersistenceManager.getInstance(ds);
			manager.setAddTimeStamp(false);
			JdbcSession session = manager.getJdbcSession();
			session.executeUpdate(sql);
		} finally {
			// TODO: handle exception
			if (manager != null)
				manager.release();
		}
	}
	
	/**
	 * 更新日志表,单独的事务中进行处理
	 * 否则直接设置manager.setAddTimeStamp(false); 在was环境中会报错
	 * 更新类型的参数
	 * @param sql
	 * @param parameter
	 * @throws BusinessException
	 * @throws DbException
	 * @throws SQLException
	 */
	public void addlog(String sql) throws BusinessException, DbException, SQLException {
		// TODO Auto-generated method stub
		getPross().dispatch_RequiresNew(sql,billtypecode, null);
	}

	/**
	 * 更新类型的参数
	 * @param sql
	 * @param parameter
	 * @throws BusinessException
	 * @throws DbException
	 * @throws SQLException
	 */
	public void addlog(String sql,SQLParameter parameter) throws BusinessException, DbException, SQLException {
		// TODO Auto-generated method stub
		HashMap  otherpms = new HashMap<String, Object>();
		otherpms.put("parameter", parameter);
		getPross().dispatch_RequiresNew(sql,billtypecode, otherpms);
	}
	/**
	 * 新增一条日志
	 * @param logvo
	 * @throws BusinessException
	 * @throws DbException
	 * @throws SQLException
	 */
	public void insertLog(LimsLogVO logvo) throws BusinessException, DbException, SQLException {
		// TODO Auto-generated method stub
		getPross().dispatch_RequiresNew(logvo, billtypecode, null);
	}

}
