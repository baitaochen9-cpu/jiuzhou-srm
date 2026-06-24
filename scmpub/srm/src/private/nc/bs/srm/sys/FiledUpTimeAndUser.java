package nc.bs.srm.sys;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.login.bs.INCUserQueryService;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.sm.UserVO;

public class FiledUpTimeAndUser {

	public UserVO qryUserVO(String userCode) throws BusinessException {
		String dataSource = InvocationInfoProxy.getInstance().getUserDataSource();
		UserVO userVO = NCLocator.getInstance().lookup(INCUserQueryService.class).findUserVO(dataSource, userCode);
		return userVO;
	}

	public UserVO qryUserVOByName(String userName) throws BusinessException {
		IUserManageQuery query = NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserVO[] users = query.queryUserByName(userName);
		if (users == null || users.length == 0)
			return null;
		return users[0];
	}

	public UserVO getUser(String userPK) throws BusinessException {
		IUserManageQuery query = NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserVO user = query.getUser(userPK);
		return user;
	}

	/**
	 * 补全时分秒
	 * 
	 * @param curate
	 * @return
	 * @throws ParseException
	 */
	public Date filedUpTime(String curate) throws ParseException {
		if (curate.endsWith("00:00:00")) {
			curate = curate.substring(0, 10);
		}
		curate = new UFDate(curate).toString();
		if (curate.length() > 10) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.parse(curate);// 构造开始日期
		} else {

		}
		return randomDate(curate);
	}

	/**
	 * 补全时分秒
	 * 
	 * @param curate
	 * @return
	 * @throws ParseException
	 */
	public Date filedUpTime1(String curate) throws ParseException {
		if (curate.endsWith("00:00:00")) {
			curate = curate.substring(0, 10);
		}
//		curate =new UFDate().toString();
		if (curate.length() > 10) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.parse(curate);// 构造开始日期
		}
		return randomDate(curate);
	}

	/**
	 * 
	 * 根据前面的时分秒 ，匹配成后续的字段的时分秒
	 * 
	 * @return
	 */
	public UFDate matcheTims(String time1, String time2) {
		String str = time2.substring(0, 10) + time1.substring(11);
		return new UFDate(str);

	}

	/**
	 * 
	 * 根据前面的时分秒 ，匹配成后续的字段的时分秒
	 * 
	 * @return
	 */
	public UFDate matcheTims(UFDate time1, UFDate time2) {
		String str = time2.toString().substring(0, 10) + time1.toString().substring(10);
		return new UFDateTime(str).getDate();
	}

	/**
	 * 在正常上下班时间内 生成随机时间
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Date randomDate(String curate) throws ParseException {
		String beginTime = curate + " 08:30:00";
		String endTime = curate + " 18:30:00";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = format.parse(beginTime);// 构造开始日期
		Date end = format.parse(endTime);// 构造结束日期
		if (start.getTime() >= end.getTime()) {
			return start;
		}

		long date = random(start.getTime(), end.getTime());

		return new Date(date);

	}

	public long random(long begin, long end) {
		long rtn = begin + (long) (Math.random() * (end - begin));
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) {
			return random(begin, end);
		}
		return rtn;
	}

	/**
	 * 根据当前时间随机增加一个
	 * 
	 * @return
	 */

	public Date randomTime(Date dBegin, String dateType, int m, int n) throws Exception {

//	  随机产生[M，N)
		int step = (int) (m + Math.random() * (n - m));
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		if ("M".equalsIgnoreCase(dateType)) {
			calBegin.add(Calendar.MONTH, step);
		}
		if ("D".equalsIgnoreCase(dateType)) {
			calBegin.add(Calendar.DAY_OF_YEAR, step);
		}
		if ("H".equalsIgnoreCase(dateType)) {
			calBegin.add(Calendar.HOUR, step);
		}
		if ("M1".equalsIgnoreCase(dateType)) {
			calBegin.add(Calendar.MINUTE, step);
		}
		if ("S".equalsIgnoreCase(dateType)) {
			calBegin.add(Calendar.SECOND, step);
		}
		return calBegin.getTime();
	}

	/**
	 * 切割時間段
	 *
	 * @param dateType 交易類型 M/D/H/N -->每月/每天/每小時/每分鐘
	 * @param start    yyyy-MM-dd HH:mm:ss
	 * @param end      yyyy-MM-dd HH:mm:ss
	 * @return
	 */

	public List<String> findDates(String dateType, Date dBegin, Date dEnd, int step) throws Exception {
		List<String> listDate = new ArrayList<String>();
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		listDate.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calBegin.getTime()));
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dEnd);
		while (calEnd.after(calBegin)) {
			if ("M".equalsIgnoreCase(dateType)) {
				calBegin.add(Calendar.MONTH, step);
			}
			if ("D".equalsIgnoreCase(dateType)) {
				calBegin.add(Calendar.DAY_OF_YEAR, step);
			}
			if ("H".equalsIgnoreCase(dateType)) {
				calBegin.add(Calendar.HOUR, step);
			}
			if ("M1".equalsIgnoreCase(dateType)) {
				calBegin.add(Calendar.MINUTE, step);
			}
			if ("S".equalsIgnoreCase(dateType)) {
				calBegin.add(Calendar.SECOND, step);
			}

			if (calEnd.after(calBegin))
				listDate.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calBegin.getTime()));
			else
				listDate.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calEnd.getTime()));
		}
		return listDate;
	}

	/**
	 * 主要为了档案的时间戳字段更新,且不需要更新ts的场景 manager.setAddTimeStamp(false);
	 * 
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

}
