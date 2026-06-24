package nc.bs.jzyy.sys.oa.out;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * NCC同步到中间平台
 * 
 * @author HP
 * 
 */
public abstract class AbstractSender4OA {
	Object param = null;// 业务组建拼装传来的参数
	Map<String, Object> otherParam = null;
	private BaseDAO dao = null;

	protected BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	// 获取同步地址
	public String getSysOAIp() throws Exception {
		// TODO Auto-generated method stub
		String sql = "select name  from  bd_defdoc where code='OAURL' 	and pk_defdoclist=(select pk_defdoclist  from bd_defdoclist where code='JZYY_PZQD')";

		return (String) getDao().executeQuery(sql, new ColumnProcessor());
	}

	// 获取同步地址
	public String getSysOA() throws Exception {
		// TODO Auto-generated method stub
		String sql = "select name  from  bd_defdoc where code='NEWOA' 	and pk_defdoclist=(select pk_defdoclist  from bd_defdoclist where code='JZYY_PZQD')";

		return (String) getDao().executeQuery(sql, new ColumnProcessor());
	}

	// 获取申请公司编码
	public String getSysCode(String pk) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select code  from  bd_defdoc where pk_defdoc= '"
				+ pk
				+ "' and pk_defdoclist=(select pk_defdoclist  from bd_defdoclist where code='JZYY_SQGS')";

		return (String) getDao().executeQuery(sql, new ColumnProcessor());
	}

	// 获取请购单子类型
	public String getSysType(String pk) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select code  from  bd_defdoc where pk_defdoc= '"
				+ pk
				+ "' and pk_defdoclist=(select pk_defdoclist  from bd_defdoclist where code='JZYY_QGZLX')";

		return (String) getDao().executeQuery(sql, new ColumnProcessor());
	}

	protected String getStringNullAssEmpoty(Object value) {
		if (value == null) {
			return " ";
		}
		return value.toString().trim();
	}

	public Object process(String posttype, Object obj,
			Map<String, Object> otherpms) throws Exception {
		this.param = obj;
		this.otherParam = otherpms;

		init();

		String sendJson = null;
		String response = null;
		sendJson = getSendJson();
		response = (String) send(sendJson);

		if (response == null || StringUtils.isEmpty(response.toString())) {
			throw new BusinessException("回执为空:" + response);
		}
		JSONObject resJsonObj = JSONObject.parseObject(response.toString());
		Object rs = afterSend(resJsonObj);
		return resJsonObj;
	}

	public abstract void init() throws Exception;

	protected abstract String getSendJson() throws Exception;

	protected abstract Object send(String sendJson) throws Exception;

	protected abstract Object afterSend(Object response) throws Exception;

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

	String[] host = null;

	/**
	 * 字符串判断空值
	 * 
	 * @param value
	 * @return
	 */
	public String getNullAsEmpty(Object value) {
		if (value == null) {
			return " ";
		}
		return value.toString().trim();
	}

	/**
	 * 数值判断空值
	 * 
	 * @param value
	 * @return
	 */
	public double getNullAsZero(UFDouble value) {
		if (value == null) {
			return 0.00;
		}
		return value.doubleValue();
	}

}
