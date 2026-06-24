package nccloud.web.ct.oba.action;

import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.platform.attachment.IAttachmentService;
import nccloud.pubitf.platform.attachment.IFileStorageConst;
import nccloud.pubitf.platform.attachment.WebFileParaVO;

/**
 * @description 结构化附件删除
 * @author xiahui
 * @date 创建时间：2019-3-5 下午1:33:08
 * @version ncc1.0
 **/
public class OBADeleteAction implements ICommonAction {
	private IAttachmentService ncservice = ServiceLocator.find(IAttachmentService.class);

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		WebFileParaVO para = json.fromJson(str, WebFileParaVO.class);
		// 表记录删除
		this.removeInfoFromDB(para.getFullPath());
		// 上传的URL数据，sm_pub_filesystem.pk_doc是网址
		if (!para.getPk_doc().startsWith("http://")) {
			// 文件服务器删除
			this.ncservice.remove(para.getPk_doc(), para.getFullPath(), this.getStorePath(para));
		}
		return null;
	}

	private String getStorePath(WebFileParaVO para) {
		String storepath = IFileStorageConst.Bucket;
		if (para.getStorepath() != null && !"".equals(para.getStorepath())) {
			storepath = para.getStorepath();
		}
		return storepath;
	}

	private void removeInfoFromDB(String fullPath) {
		this.ncservice.deleteAttachDBInfo(new String[] { fullPath });
	}
}
