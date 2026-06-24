package nccloud.web.ct.oba.action;

import java.util.Map;

import nc.bs.pub.filesystem.FileSystemUtil;
import nc.md.innerservice.IMetaDataQueryService;
import nc.md.model.IBean;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.filesystem.NCFileVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.platform.attachment.IAttachmentService;
import nccloud.pubitf.platform.attachment.IFileStorageConst;
import nccloud.pubitf.platform.attachment.WebFileParaVO;
import nccloud.web.platform.attachment.action.WebFileParaConvertor;

/**
 * @description 结构化附件查询
 * @author xiahui
 * @date 创建时间：2019-3-5 下午1:31:21
 * @version ncc1.0
 **/
public class OBAQueryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		Map<String, String> param = json.fromJson(str, Map.class);
		String billId = param.get("billId");
		String billType = param.get("billType");

		try {
			// 根据fullClassName查询相应的bean
			IBean bean = ServiceLocator.find(IMetaDataQueryService.class).getBeanByFullClassName(AggCtPuVO.class.getName());
			// 获取目录
			String rootPath = billType + "/" + bean.getID();
			String rootDirStr = FileSystemUtil.validatePathString(rootPath);
			// 获取文件
			NCFileVO[] ncfiles = ServiceLocator.find(IAttachmentService.class).queryNCFilesByNodePath(rootDirStr,
					SessionContext.getInstance().getClientInfo().getUserid());
			WebFileParaConvertor convertor = new WebFileParaConvertor();
			WebFileParaVO[] ret = convertor.convert(ncfiles, billId, IFileStorageConst.Bucket);
			return ret;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}
}
