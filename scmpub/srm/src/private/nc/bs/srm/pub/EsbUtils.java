package nc.bs.srm.pub;

import junit.framework.Assert;
import nc.bs.dao.DAOException;
import nc.uif.pub.exception.UifException;

public class EsbUtils {
	//是否启用Esb
	private static  boolean isEnableSrm() throws DAOException {
		ParamCheck check = new ParamCheck();
		return check.IsToSrm(null);
	}
	//组装请求地址
	public static String getPostUrl(String oriUrl) throws DAOException, UifException {
		Assert.assertNotNull("参数不能为空", oriUrl);
		SenderQuerys convert = new SenderQuerys();
		StringBuffer postUrl = new StringBuffer(oriUrl);
		
		return (String) convert.getMnecodeByCode("JZYY_PZQD", postUrl.toString(), "");// 助记码维护的地址
	}

}
