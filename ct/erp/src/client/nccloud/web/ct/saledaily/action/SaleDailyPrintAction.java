package nccloud.web.ct.saledaily.action;

import nc.vo.pubapp.res.NCModule;
import nccloud.framework.core.io.WebFileType;
import nccloud.web.platform.print.AbstractPrintAction;

/**
 * @description 销售合同打印
 * @author wangshrc
 * @date 2019年2月22日 下午3:52:58
 * @version ncc1.0
 */
public class SaleDailyPrintAction extends AbstractPrintAction {
	@Override
	public String getPrintServiceModule() {
		return NCModule.CT.getName();
	}

	@Override
	public String getPrintServiceName() {
		return "nccloud.pubimpl.ct.saledaily.operator.SaleDailyPrintOperator";
	}

	@Override
	public WebFileType getWebFileType() {
		return WebFileType.Pdf;
	}

}
