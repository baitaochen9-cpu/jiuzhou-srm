package nccloud.web.platform.attachment.tool;

import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.pub.BusinessException;
import nc.vo.pub.para.SysInitVO;
import nccloud.framework.service.ServiceLocator;

public class AttachmentUploadSystemParamUtils {

	public static String readSysParam(String initCode, String pkOrg) {
		String res = null;
		if (initCode == null || pkOrg == null) {
			res = null;
		}
		ISysInitQry service = ServiceLocator.find(ISysInitQry.class);
		String whereStr = "initcode='" + initCode + "' and pk_org = '" + pkOrg + "' and dr = 0";
		try {
			SysInitVO[] sysInitVOs = service.getSysInitVOs(whereStr);
			if (sysInitVOs != null && sysInitVOs.length > 0 && sysInitVOs[0] != null
					&& sysInitVOs[0].getValue() != null) {
				res = sysInitVOs[0].getValue();
			}
		} catch (BusinessException e) {
			return res;
		}
		return res;
	}

}
