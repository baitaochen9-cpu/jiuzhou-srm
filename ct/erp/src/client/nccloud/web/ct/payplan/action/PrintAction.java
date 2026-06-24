package nccloud.web.ct.payplan.action;

import nc.vo.pubapp.res.NCModule;
import nccloud.framework.core.io.WebFileType;
import nccloud.web.platform.print.AbstractPrintAction;

/** 
 * @description 
 * @author xiahui
 * @date 创建时间：2019-3-5 下午7:15:41 
 * @version ncc1.0 
 **/
public class PrintAction  extends AbstractPrintAction{

	@Override
	public String getPrintServiceModule() {
		return NCModule.CT.getName();
	}

	@Override
	public String getPrintServiceName() {
		return "nccloud.pubimpl.ct.payplan.operator.PayplanPrintOperator";
	}
	
	@Override
	public WebFileType getWebFileType() {
	  return WebFileType.Pdf;
	}
}
