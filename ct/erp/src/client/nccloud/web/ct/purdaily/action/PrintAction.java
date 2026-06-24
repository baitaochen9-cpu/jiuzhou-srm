package nccloud.web.ct.purdaily.action;

import nc.vo.pubapp.res.NCModule;
import nccloud.framework.core.io.WebFileType;
import nccloud.web.platform.print.AbstractPrintAction;

/** 
 * @description 打印
 * @author xiahui
 * @date 创建时间：2019-2-13 下午5:49:13 
 * @version ncc1.0 
 **/
public class PrintAction extends AbstractPrintAction{

	@Override
	public String getPrintServiceModule() {
		return NCModule.CT.getName();
	}

	@Override
	public String getPrintServiceName() {
		return "nccloud.pubimpl.ct.purdaily.operator.PurdailyPrintOperator";
	}
	
	@Override
	public WebFileType getWebFileType() {
	  return WebFileType.Pdf;
	}

}
