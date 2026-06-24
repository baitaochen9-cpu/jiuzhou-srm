package nccloud.dto.ct.pub.utils;

import nccloud.dto.ct.pub.entity.OperateType;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * @description 并发错误提示
 * @author qiaobb
 * @date 2019-1-4 下午4:21:13
 * @version ncc1.0
 */
public class OperateExceptionUtils {

	/**
	 * 
	 * 检查并发
	 * 
	 * @param objs
	 * @param type
	 * 
	 */
	public static void checkVo(Object[] objs, OperateType type) {
		if (objs == null || objs.length < 1) {
			if (OperateType.Refresh.equals(type)) {
				ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009012_0",
						"04009012-0019")/*
														 * @ res "当前单据已被删除，请返回列表！"
														 */);
			} else {
				ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009012_0",
						"04009012-0003")/*
														 * @ res "当前单据已被删除，请刷新重试！"
														 */);
			}
		}
	}

}
