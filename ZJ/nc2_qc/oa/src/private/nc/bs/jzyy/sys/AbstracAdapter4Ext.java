package nc.bs.jzyy.sys;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.trade.business.HYPubBO;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.login.bs.INCUserQueryService;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.accessor.IBDData;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName:
 * @author: 云峰网络 411072655
 * 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */
public abstract class AbstracAdapter4Ext {

	public BaseDAO dao;

	public static final String SUCCESSCODE = "200";
	public static final String ERRCODE = "-1";

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public MapListProcessor maplistProcessor = new MapListProcessor();

	/**
	 * 同步处理
	 * 
	 * @param billvo
	 * @return
	 * @throws BusinessException
	 */
	public JSONObject process(Object billvo) throws BusinessException,
			DAOException {
		String isSsy = befroreSys(billvo);
		if (!"00".equalsIgnoreCase(isSsy)) {
			return null;
		}
		return sys(billvo);
	};

	/**
	 * 同步前校验如果等于00,则代表可以同步,否则不需要同步
	 * 
	 * @param billvo
	 * @return
	 * @throws BusinessException
	 */
	public String befroreSys(Object billvo) throws BusinessException {
		return "00";
	};

	public abstract JSONObject sys(Object billvo) throws BusinessException,
			DAOException;

	/**
	 * null按照空字符传递
	 * 
	 * @param value
	 * @return
	 */
	public String getStringNullAssEmpoty(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString().trim();
	}

	/**
	 * 返回结果集处理
	 * 
	 * @param requestId
	 * @param status
	 * @param masg
	 * @param executeQuery
	 * @return
	 */
	public JSONObject getRsultDataSuccess(List<Map<String, Object>> respData) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", "获取数据" + respData.size() + "条!");
		String datas = JSON.toJSONString(respData);
		JSONArray datasjson = JSONArray.parseArray(datas);
		jsobj.put("list", datasjson);
		return jsobj;
	}

	/**
	 * 返回结果集处理
	 * 
	 * @param requestId
	 * @param status
	 * @param masg
	 * @param executeQuery
	 * @return
	 */
	public JSONObject getRsultDataSuccess(List<Map<String, Object>> respData,
			String msg) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", msg);
		String datas = JSON.toJSONString(respData);
		JSONArray datasjson = JSONArray.parseArray(datas);
		jsobj.put("list", datasjson);
		return jsobj;
	}

	/**
	 * 返回结果集处理
	 * 
	 * @param requestId
	 * @param status
	 * @param masg
	 * @param executeQuery
	 * @return
	 */
	public JSONObject getRsultDataSuccess(JSONObject data, String msg) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", msg);
		JSONArray datasjson = new JSONArray();
		datasjson.add(data);
		jsobj.put("list", datasjson);
		return jsobj;
	}

	public static JSONObject getRsultJsonFailed(String masg) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", ERRCODE);
		jsobj.put("success", Boolean.FALSE);// Boolean.TRUE/FALSE
		jsobj.put("message", masg);
		return jsobj;
	}

	public static JSONObject getRsultJsonFailed(Exception e) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", ERRCODE);
		jsobj.put("success", Boolean.FALSE);// Boolean.TRUE/FALSE
		jsobj.put("message", e.getMessage());
		String errorStack = StackPrint.getInstance().printStack(unmarsh(e));
		jsobj.put("errorstack", errorStack);
		return jsobj;
	}

	/**
	 * 解析最底层的异常
	 * 
	 * @param ex
	 * @return
	 */
	public static Throwable unmarsh(Throwable ex) {
		Throwable cause = ex.getCause();
		if (cause != null) {
			cause = unmarsh(cause);
		} else {
			cause = ex;
		}
		return cause;
	}

	public static JSONObject getRsultJsonFailed(String masg, String code) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", code);
		jsobj.put("success", Boolean.FALSE);// Boolean.TRUE/FALSE
		jsobj.put("message", masg);
		return jsobj;
	}

	public static JSONObject getRsultJsonSuccess(String message, String code) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", message);
		return jsobj;
	}

	public JSONObject getRsultJsonSuccess(String message) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", message);
		return jsobj;
	}

	public String getOrg(String code) throws UifException {
		String pk_org = getColValue2("org_orgs", "pk_org", "code", code,
				"islastversion", "Y");
		return pk_org;
	}

	public String getStockorg(String code) throws UifException {
		String pk_org = getColValue2("org_stockorg", "pk_stockorg", "code",
				code, "islastversion", "Y");
		return pk_org;
	}

	public String getSalesorg(String code) throws UifException {
		String pk_org = getColValue2("org_salesorg", "pk_salesorg", "code",
				code, "islastversion", "Y");
		return pk_org;
	}

	public String getGroup(String code) throws UifException {
		String pk_org = getColValue("org_group", "pk_group", "code", code);
		return pk_org;
	}

	public String getUserId(String code) throws UifException {
		String pk_org = getColValue("sm_user", "cuserid", "user_code", code);
		return pk_org;
	}

	public String getColValue(String tableName, String fieldname,
			String setField, String setValue) throws UifException {
		String strwhere = " nvl(dr,0) = 0  and " + setField + " = '" + setValue
				+ "'";
		return getString_TrimZeroLenAsNull(findColValue(tableName, fieldname,
				strwhere));
	}

	public String getColValue2(String tableName, String fieldname,
			String setField1, String setValue1, String setField2,
			String setValue2) throws UifException {
		String strwhere = " nvl(dr,0) = 0  and " + setField1 + " = '"
				+ setValue1 + "' and " + setField2 + " = '" + setValue2 + "'";
		return getString_TrimZeroLenAsNull(findColValue(tableName, fieldname,
				strwhere));
	}

	public Object findColValue(String tablename, String fieldname,
			String strwhere) throws UifException {
		return new HYPubBO().findColValue(tablename, fieldname, strwhere);
	}

	public String getStrFsql(String strsql) {
		IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			List list = (List) qryBS.executeQuery(strsql, null,
					new ArrayListProcessor());
			if ((list != null) && (list.size() > 0)) {
				String s = ((Object[]) list.get(0))[0].toString();
				return s;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public String getFieldOtherPk(String pk_org, String name, String metaDataID) {

		IGeneralAccessor genAcc = GeneralAccessorFactory
				.getAccessor(metaDataID);
		String docPk = null;

		IBDData bdData = genAcc.getDocByNameWithMainLang(pk_org, name);

		if (bdData != null && bdData.getPk() != null)
			docPk = bdData.getPk();
		return docPk;
	}

	public String getString_TrimZeroLenAsNull(Object value) {
		if ((value == null) || (value.toString().trim().length() == 0)) {
			return null;
		}
		return value.toString().trim();
	}

	public UFDouble getUfDouble_OfNull(Object value) {
		if ((value == null) || (value.toString().trim().length() == 0)) {
			return null;
		}
		return new UFDouble(value.toString().trim());
	}

	public static UFDouble getUFDouble_NullAsZero(Object value) {
		if ((value == null) || (value.toString().trim().equals("")))
			return UFDouble.ZERO_DBL;
		if ((value instanceof UFDouble))
			return (UFDouble) value;
		if ((value instanceof BigDecimal)) {
			return new UFDouble((BigDecimal) value);
		}
		return new UFDouble(value.toString().trim());
	}

	public static UFDate getUFDate(Object value) {
		if ((value == null) || (value.toString().trim().equals("")))
			return null;
		if ((value instanceof UFDate)) {
			return (UFDate) value;
		}
		return getUFDate(value.toString());
	}

	public static UFDate getUFDate(String dbilldate) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return new UFDate(ft.parse(dbilldate));
		} catch (ParseException e) {
			Logger.error(e.getMessage(), e);
		}
		return new UFDate(dbilldate);

	}

	/**
	 * 获取组织币种
	 * 
	 * @param pk_org
	 * @return
	 */
	public String getOrgCurr(String pk_org) {
		Map<String, String> orgCurrMap = null;
		orgCurrMap = OrgUnitPubService
				.queryOrgCurrByPk(new String[] { pk_org });
		if (orgCurrMap != null)
			return orgCurrMap.get(pk_org);
		return null;
	}

	public UserVO qryUserVO(String userCode) throws BusinessException {
		userCode = userCode.replace("sap", "erp").replace("SAP", "erp");
		String dataSource = InvocationInfoProxy.getInstance()
				.getUserDataSource();
		UserVO userVO = NCLocator.getInstance()
				.lookup(INCUserQueryService.class)
				.findUserVO(dataSource, userCode);
		return userVO;
	}

	public UserVO qryUserVOByName(String userName) throws BusinessException {
		IUserManageQuery query = NCLocator.getInstance().lookup(
				IUserManageQuery.class);
		UserVO[] users = query.queryUserByName(userName);
		if (users == null || users.length == 0)
			return null;
		return users[0];
	}

	public UserVO getUser(String userPK) throws BusinessException {
		IUserManageQuery query = NCLocator.getInstance().lookup(
				IUserManageQuery.class);
		UserVO user = query.getUser(userPK);
		return user;
	}
}
