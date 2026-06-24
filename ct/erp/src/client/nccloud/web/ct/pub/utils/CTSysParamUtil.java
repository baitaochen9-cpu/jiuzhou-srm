package nccloud.web.ct.pub.utils;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.AssertUtils;
import nc.vo.scmf.pub.SCMParas;
import nc.vo.scmf.pub.util.SCMSysParamUtil;
import nccloud.dto.ic.pub.enumeration.NCModule;
import nccloud.dto.scmpub.pub.group.ISysInitGroupQuery;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.baseapp.param.ISysinitAccessor;

/**
 * @description NCCloud client端业务参数工具类
 * @author xiahui
 * @date 创建时间：2019-3-6 下午5:00:27
 * @version ncc1.0
 **/
public class CTSysParamUtil {

	private static final String SCM_CMP_CODE = "3607";

	/**
	 * 财务模块：应付管理
	 * 
	 * @return
	 */
	public static boolean isAPEnabled() {
		return CTSysParamUtil.isEnabled(NCModule.AP.getCode());
	}

	/**
	 * 现金管理
	 * 
	 * @return
	 */
	public static boolean isCMPEnabled() {
		return CTSysParamUtil.isEnabled(CTSysParamUtil.SCM_CMP_CODE);
	}

	/**
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public static boolean getPO88(String pk_org) {
		AssertUtils.assertValue(pk_org != null,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0", "04001001-0231")); // 财务组织为空
		boolean bpayapp = false;
		try {
			String value = CTSysParamUtil.getParaString(pk_org, SCMParas.PO88.name());
			if (value != null && SCMSysParamUtil.YES.equals(value)) {
				bpayapp = true;
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return bpayapp;
	}

	/**
	 * 取某一组织下String型参数值
	 * 
	 * @param pk_org
	 * @param initCode
	 *          参数编码
	 * @return
	 * @throws BusinessException
	 */
	public static String getParaString(String pk_org, String initCode) throws BusinessException {
		return ServiceLocator.find(ISysinitAccessor.class).getParaString(pk_org, initCode);
	}

	/**
	 * 模块是否启用
	 * 
	 * @param moduleCode
	 * @return
	 */
	public static boolean isEnabled(String moduleCode) {
		try {
			Map<String, Boolean> remap = ServiceLocator.find(ISysInitGroupQuery.class).isModuleEnable(
					new String[] { moduleCode });
			return remap.get(moduleCode);

		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return false;
	}
}
