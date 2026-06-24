package nccloud.api.jzsrm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.srm.sys.FiledUpTimeAndUser;
import nc.bs.trade.business.HYPubBO;
import nc.bs.trade.comstatus.BillCommit;
import nc.bs.uap.lock.PKLock;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.pub.fa.common.util.StringUtils;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.accessor.IBDData;
import nc.vo.bd.material.MaterialConvertVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.borland.dx.sql.metadata.MetaDataException;
//import nccloud.api.rest.utils.StackPrint;

public abstract class AbstracProcessor4Ext {

	public static final String SUCCESSCODE = "200";
	public static final String ERRCODE = "-1";
	public static final String REPEATERRCODE = "1";

	public BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
    /**
                * 设置登录信息
     * @param userCode
     * @throws BusinessException
     */
	public void setUserInfo(String userCode) throws BusinessException {
		UserVO uservo = getfillUpRule().qryUserVO(userCode);
		InvocationInfoProxy.getInstance().setUserCode(userCode);
		InvocationInfoProxy.getInstance().setUserId(uservo.getCuserid());
		InvocationInfoProxy.getInstance().setBizDateTime(System.currentTimeMillis()); 
	}
	
	/**
	 * 设置单据提交
	 * 
	 * @param userCode
	 * @throws BusinessException
	 */
	public void setBillCommit(AggregatedValueObject billVo) throws BusinessException {
		BillCommit comt = new BillCommit();
		comt.commitBill(billVo);
	}
	
	public FiledUpTimeAndUser fillUpRule;

	public FiledUpTimeAndUser getfillUpRule() {
		if (fillUpRule == null) {
			fillUpRule = new FiledUpTimeAndUser();
		}
		return fillUpRule;
	}

	public abstract JSONObject process(Object billvo) throws Exception;

	public MapListProcessor maplistProcessor = new MapListProcessor();

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
	 * 判断Object是否为空
	 * @param object
	 * @return
	 */
	public static boolean isBlankOrEmpty(Object object) {
		if (null == object) {
			return true;
		}
		if (object.toString().equals("")) {
			return true;
		}
		return false;
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
		jsobj.put("total",respData.size());
		String datas = JSON.toJSONString(respData);
		JSONArray datasjson = JSONArray.parseArray(datas);
		jsobj.put("data", datasjson);
		
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
	public JSONObject getRsultDataSuccess(List<Map<String, Object>> respData, String msg) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);
		jsobj.put("message", msg);
		if (respData != null) {
			JSONObject rspinfo = new JSONObject();
			String datas = JSON.toJSONString(respData);
			JSONArray datasjson = JSONArray.parseArray(datas);
			rspinfo.put("list", datasjson);
			jsobj.put("rspinfo", rspinfo);

		}

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
//	public JSONObject getRsultData1Success(List<Map<String, Object>> respData, String msg) {
//		JSONObject jsobj = new JSONObject();
//		jsobj.put("code", SUCCESSCODE);
//		jsobj.put("success", Boolean.TRUE);
//		jsobj.put("message", msg);
//		if (respData != null) {
//			JSONObject rspinfo = new JSONObject();
//			String datas = JSON.toJSONString(respData);
//			JSONArray datasjson = JSONArray.parseArray(datas);
//			rspinfo.put("list", datasjson);
//			jsobj.put("data", rspinfo);
//
//		}
//
//		return jsobj;
//	}

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
		if (StringUtils.isEmpty(msg)) {
			jsobj.put("message", "处理成功.");
		} else {
			jsobj.put("message", msg);
		}
		if (data != null) {
			jsobj.put("rspinfo", data);
		}

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
	public JSONObject getRsultDataSuccess() {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", "处理成功.");
		return jsobj;
	}

	public static JSONObject getRsultJsonFailed(String masg) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", ERRCODE);
		jsobj.put("success", Boolean.FALSE);// Boolean.TRUE/FALSE
		jsobj.put("message", masg);
		ExceptionUtils.wrappBusinessException(masg);
		return jsobj;
	}

//	public static JSONObject getRsultJsonFailed(Exception e) {
//		JSONObject jsobj = new JSONObject();
//		jsobj.put("code", ERRCODE);
//		jsobj.put("success", Boolean.FALSE);// Boolean.TRUE/FALSE
//		jsobj.put("message", e.getMessage());
//		String errorStack = StackPrint.getInstance().printStack(unmarsh(e));
//		jsobj.put("errorstack", errorStack);
//		return jsobj;
//	}

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
	
	public static JSONObject getRepeatResultFailed(String masg) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", REPEATERRCODE);
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
		jsobj.put("code", ERRCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", message);
		return jsobj;
	}

	public String getOrg(String code) throws UifException, DAOException {
		String pk_org = getColValue2("org_orgs", "pk_org", "code", code, "islastversion", "Y");
		return pk_org;
	}

	public String getSalesorg(String code) throws UifException {
		String pk_org = getColValue2("org_salesorg", "pk_salesorg", "code", code, "islastversion", "Y");
		return pk_org;
	}

	public String getColValue(String tableName, String fieldname, String setField, String setValue)
			throws UifException {
		String strwhere = " nvl(dr,0) = 0  and " + setField + " = '" + setValue + "'";
		return getString_TrimZeroLenAsNull(findColValue(tableName, fieldname, strwhere));
	}

	public String getColValue2(String tableName, String fieldname, String setField1, String setValue1, String setField2,
			String setValue2) throws UifException {
		String strwhere = " nvl(dr,0) = 0  and " + setField1 + " = '" + setValue1 + "' and " + setField2 + " = '"
				+ setValue2 + "'";
		return getString_TrimZeroLenAsNull(findColValue(tableName, fieldname, strwhere));
	}

	public Object findColValue(String tablename, String fieldname, String strwhere) throws UifException {
		return new HYPubBO().findColValue(tablename, fieldname, strwhere);
	}

	public String getStrFsql(String strsql) {
		IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			List list = (List) qryBS.executeQuery(strsql, null, new ArrayListProcessor());
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

		IGeneralAccessor genAcc = GeneralAccessorFactory.getAccessor(metaDataID);
		String docPk = null;

		IBDData bdData = genAcc.getDocByNameWithMainLang(pk_org, name);

		if (bdData != null && bdData.getPk() != null)
			docPk = bdData.getPk();
		return docPk;
	}

	public String getString_TrimZeroLenAsNull(String[] value) {
		if (value == null || value.length == 0 || getString_TrimZeroLenAsNull(value[0]) == null) {
			return null;
		}
		return value.toString().trim();
	}

	public String getString_TrimZeroLenAsNull(Object value) {
		if ((value == null) || (value.toString().trim().length() == 0)) {
			return null;
		}
		return value.toString().trim();
	}

	public String getStrings_TrimZeroLenAsNull(Object value) {
		if ((value == null) || (value.toString().trim().length() == 0)) {
			return null;
		}
		if (value instanceof String[]) {
			String[] values = (String[]) value;
			return getString_TrimZeroLenAsNull(values[0]);
		}
		return null;
	}

	public String getStringValue(Map<String, Object> valueMap, String key) {
		Object value = valueMap.get(key);

		if (value instanceof String[]) {
			String[] values = (String[]) value;
			if (values == null || values.length == 0)
				return null;
			return values[0];
		}
		return null;
	}

	public UFDouble getUfDouble_OfNull(Object value) {
		if ((value == null) || (value.toString().trim().length() == 0)) {
			return null;
		}
		return new UFDouble(value.toString().trim());
	}

	public UFDouble getUFDouble_NullAsZero(Object value) {
		if ((value == null) || (value.toString().trim().equals("")))
			return UFDouble.ZERO_DBL;
		if ((value instanceof UFDouble))
			return (UFDouble) value;
		if ((value instanceof BigDecimal)) {
			return new UFDouble((BigDecimal) value);
		}
		return new UFDouble(value.toString().trim());
	}


	

	
	/**
	 * 获取存货的计量信息
	 * 
	 * @param cmaterialid
	 * @return
	 * @throws BusinessException
	 * @throws MetaDataException
	 */
	public MaterialConvertVO getInvMeasVO(String cmaterialid) throws BusinessException {
		// TODO Auto-generated method stub
		VOQuery<MaterialConvertVO> qry = new VOQuery<MaterialConvertVO>(MaterialConvertVO.class);
		MaterialConvertVO[] convertVOs = qry.query(" and pk_material='" + cmaterialid + "'", null);

		// 冻结的物料，冻结的客户，冻结的供应商，单据不允许导入 --因为单据改动的地方太多，所以放到这个公共查询入口
		IMDPersistenceQueryService mdQueryService = MDPersistenceService.lookupPersistenceQueryService();
		Object[] objs;
		try {
			objs = mdQueryService.queryBillOfVOByPKsWithOrder(MaterialVO.class, new String[] { cmaterialid }, false);
			MaterialVO hvo = (MaterialVO) objs[0];
			Integer enablestate = hvo.getEnablestate();
			if (2 != enablestate) {
				throw new BusinessException("物料" + hvo.getCode() + "未启用,不能导入");
			}
//			String frozenflag = hvo.getDef5();
//			if(frozenflag== null) {
//				frozenflag ="N"; 
//			}
//			if("Y".equalsIgnoreCase(frozenflag)) {
//				throw new BusinessException("物料"+hvo.getCode()+"已冻结,不能导入");
//
//			}
			if (convertVOs != null && convertVOs.length > 0) {
				return convertVOs[0];
			}
		} catch (MetaDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("查询物料信息异常" + e.getMessage());

		}

		return null;
	}
	
	/**
	 * 获取存货的计量信息
	 * 
	 * @param cmaterialid
	 * @return
	 * @throws BusinessException
	 * @throws MetaDataException
	 */
	public MaterialConvertVO getInvMeasVO2(String cmaterialid) throws BusinessException {
		// TODO Auto-generated method stub
		VOQuery<MaterialConvertVO> qry = new VOQuery<MaterialConvertVO>(MaterialConvertVO.class);
		MaterialConvertVO[] convertVOs = qry.query(" and pk_material='" + cmaterialid + "'", null);

		// 冻结的物料，冻结的客户，冻结的供应商，单据不允许导入 --因为单据改动的地方太多，所以放到这个公共查询入口
		IMDPersistenceQueryService mdQueryService = MDPersistenceService.lookupPersistenceQueryService();
		Object[] objs;
		try {
			objs = mdQueryService.queryBillOfVOByPKsWithOrder(MaterialVO.class, new String[] { cmaterialid }, false);
			MaterialVO hvo = (MaterialVO) objs[0];
			Integer enablestate = hvo.getEnablestate();
			if (2 != enablestate) {
				throw new BusinessException("物料" + hvo.getCode() + "未启用,不能导入");
			}
//			String frozenflag = hvo.getDef5();
//			if(frozenflag== null) {
//				frozenflag ="N"; 
//			}
//			if("Y".equalsIgnoreCase(frozenflag)) {
//				throw new BusinessException("物料"+hvo.getCode()+"已冻结,不能导入");
//
//			}
			if (convertVOs != null && convertVOs.length > 0) {
				return convertVOs[0];
			}
		} catch (MetaDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("查询物料信息异常" + e.getMessage());

		}

		return null;
	}


	

	/**
	 * 获取存货的计量信息
	 * 
	 * @param cmaterialid
	 * @return
	 */
	public MaterialConvertVO getInvMeasVO1(String cmaterialid) {
		// TODO Auto-generated method stub
		VOQuery<MaterialConvertVO> qry = new VOQuery<MaterialConvertVO>(MaterialConvertVO.class);
		MaterialConvertVO[] convertVOs = qry.query(" and pk_material='" + cmaterialid + "'", null);
		if (convertVOs != null) {
			return convertVOs[0];
		}
		return null;
	}

	/**
	 * 获取存货的计量信息
	 * 
	 * @param cmaterialid
	 * @return
	 */
	public MaterialConvertVO[] getInvMeasVOs(String cmaterialid) {
		// TODO Auto-generated method stub
		VOQuery<MaterialConvertVO> qry = new VOQuery<MaterialConvertVO>(MaterialConvertVO.class);
		MaterialConvertVO[] convertVOs = qry.query(" and pk_material='" + cmaterialid + "'", null);
		if (convertVOs != null) {
			return convertVOs;
		}
		return null;
	}

	public boolean isLock(String vtrantypecode, String vbillcode, String pk_org) throws BusinessException {
		if (PKLock.getInstance().addDynamicLock(vtrantypecode + vbillcode + pk_org)) {
		} else {
			throw new BusinessException("生成单据号" + vbillcode + "的单据，系统繁忙,请稍后再试!");
		}
		return true;
	}

	

}
