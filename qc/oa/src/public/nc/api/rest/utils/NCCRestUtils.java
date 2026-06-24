package nc.api.rest.utils;

import java.io.UnsupportedEncodingException;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.login.bs.INCUserQueryService;
import nc.pubitf.org.IGroupPubService;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.json.GsonUtils;
import nc.vo.sm.UserVO;

import org.json.JSONString;

import uap.ws.rest.util.UAPRSConstance;

import com.google.gson.Gson;

/**
 * 返回值处理工具类
 * 
 * @author lizhmf
 * @date 2019年7月16日上午9:26:39
 */
public class NCCRestUtils {

	public static final JSONString emptyJSONString = new JSONString() {
		public String toJSONString() {
			return "";
		}
	};

	/**
	 * 创建NC系统的GsonBuilder
	 * 
	 * @return Gson
	 */
	public static Gson buildNCGson() {
		return GsonUtils.buildNCGson4Rest();
	}


	/**
	 * 使用Gson转换对象
	 * 
	 * @param object
	 * @return
	 */
	public static JSONString toJSONString(final Object object) {
		return new JSONString() {

			@Override
			public String toJSONString() {
				String json = buildNCGson().toJson(object);
				try {
					String jsonCode = new String(json.getBytes(UAPRSConstance.CHARSET), UAPRSConstance.CHARSET);
					return jsonCode;
				} catch (UnsupportedEncodingException e) {
					ExceptionUtils.wrappException(e);
				}
				return null;
			}
		};
	}

	/**
	 * 对象转String，JSONString 会改变大小写
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSON(final Object object) {
		Gson gson = new Gson();
		String json = gson.toJson(object);
		try {
			json = new String(json.getBytes(UAPRSConstance.CHARSET), UAPRSConstance.CHARSET);
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.wrappException(e);
		}
		return json;
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return buildNCGson().fromJson(json, clazz);
	}

	/**
	 * 转换为JSONString，处理了编码
	 * 
	 * @param value
	 * @return
	 */
	public static JSONString toJSONStringNoSerializer(final Object object) {
		return new JSONString() {
			public String toJSONString() {
				Gson gson = new Gson();
				String str = gson.toJson(object);
				return str;
			}
		};
	}

	/**
	 * 初始化InvocationInfo
	 */
	public static void initInvocationInfo() {
		
		InvocationInfoProxy.getInstance().setTimeZone(nc.vo.pub.lang.ICalendar.BASE_TIMEZONE.toString());

		String dataSource = InvocationInfoProxy.getInstance().getUserDataSource();
		String userCode = InvocationInfoProxy.getInstance().getUserCode();
		try {
			UserVO userVO = NCLocator.getInstance().lookup(INCUserQueryService.class).findUserVO(dataSource, userCode);
			if (userVO != null) {
				InvocationInfoProxy.getInstance().setGroupId(userVO.getPk_group());
				String groupNo = NCLocator.getInstance().lookup(IGroupPubService.class).getGroupNoByPK(userVO.getPk_group());
				InvocationInfoProxy.getInstance().setGroupNumber(groupNo);
				InvocationInfoProxy.getInstance().setUserId(userVO.getPrimaryKey());
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
	}
}
