package nccloud.web.ct.price.action;

import nc.vo.pubapp.res.NCModule;
import nccloud.framework.core.io.WebFileType;
import nccloud.web.platform.print.AbstractPrintAction;

public class CtPricePrintAction extends AbstractPrintAction {

	@Override
	public String getPrintServiceModule() {
		return NCModule.CT.getName();
	}

	@Override
	public String getPrintServiceName() {
		return "nccloud.pubimpl.ct.price.CtPricePrintOperator";
	}

	@Override
	public WebFileType getWebFileType() {
		return WebFileType.Pdf;
	}
}
