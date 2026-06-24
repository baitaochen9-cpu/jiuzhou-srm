package nccloud.web.ct.oba.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.io.WebFile;
import nccloud.framework.core.io.WebFileDisposition;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.pubitf.platform.attachment.IAttachmentService;
import nccloud.pubitf.platform.attachment.IFileStorageConst;

/**
 * @description 结构化附件下载
 * @author xiahui
 * @date 创建时间：2019-3-5 下午1:32:53
 * @version ncc1.0
 **/
public class OBADownloadAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		try {
			Map<String, String[]> params = request.readParameters();
			// 校验isdoc
			String[] isdoc = params.get("isdoc");
			if (isdoc == null || isdoc.length == 0) {
				isdoc = new String[] { "z" };
			}
			String doc = isdoc[0];
			// 校验附件名
			String[] names = params.get("name");
			if (names == null || names.length == 0) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501002_0",
						"01501002-0283")/* @res 附件名称不能为空！！ */);
			}
			String fileName = names[0];
			// 校验pk_doc
			String[] pk_docs = params.get("pk_doc");
			if (pk_docs == null || pk_docs.length == 0) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501002_0",
						"01501002-0284")/* @res pk_doc不能为空！！ */);
			}
			String pk_doc = pk_docs[0];
			// 读取文件流
			IAttachmentService service = ServiceLocator.find(IAttachmentService.class);
			InputStream in = service.download(doc, pk_doc, IFileStorageConst.Bucket);
			return new WebFile(fileName, in);
		}catch (Exception e1) {
			Throwable e = ExceptionUtils.unmarsh(e1);
	    String mess = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501003_0","01501003-0121")/*@res 业务异常：*/ + e.getMessage();
	    if (e.getMessage() == null || "".equals(e.getMessage())) {
	      StringWriter sw = new StringWriter();
	      e.printStackTrace(new PrintWriter(sw, true));
	      mess = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501003_0","01501003-0122")/*@res 系统异常：*/ + sw.toString();
	    }
	    InputStream in = new ByteArrayInputStream(mess.getBytes());
	    WebFile file = new WebFile("print_error.html", in);
	    file.setDisposition(WebFileDisposition.Inline);
	    return file;
		}
	}
}
