package nccloud.web.ct.oba.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.lightapp.framework.web.action.attachment.AttachmentVO;
import nc.lightapp.framework.web.action.attachment.IUploadAction;
import nc.md.innerservice.IMetaDataQueryService;
import nc.md.model.IBean;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.filesystem.NCFileNode;
import nc.vo.pub.filesystem.NCFileVO;
import nc.vo.pubapp.AppContext;
import nccloud.framework.core.config.uploadwhitelist.UploadWhiteListDefine;
import nccloud.framework.core.config.uploadwhitelist.UploadWhiteListDefineConfig;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.io.WebFile;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.platform.attachment.IAttachmentService;
import nccloud.pubitf.platform.attachment.IFileStorageConst;
import nccloud.pubitf.platform.attachment.WebFileParaVO;
import nccloud.web.platform.attachment.action.WebFileParaConvertor;
import nccloud.web.platform.attachment.tool.AttachmentUploadSystemParamUtils;
import uap.pub.fs.domain.basic.FileHeader;

/**
 * @description 结构化附件上传
 * @author xiahui
 * @date 创建时间：2019-3-5 下午1:32:42
 * @version ncc1.0
 **/
public class OBAUploadAction implements IUploadAction {

	private IAttachmentService service = ServiceLocator.find(IAttachmentService.class);

	@Override
	public Object doAction(AttachmentVO paras) {
		Map<String, String[]> params = paras.getParameters();
		try {
			// 根据fullClassName查询相应的bean
			IBean bean = ServiceLocator.find(IMetaDataQueryService.class)
					.getBeanByFullClassName(AggCtPuVO.class.getName());
			// 获取目录
			String fullPath = bean.getID();
			// 校验文件
			WebFile[] files = paras.getFiles();
			this.checkPara(files);
			// 上传
			String billId = params.get("billId")[0];
			String billType = params.get("billType")[0];
			fullPath = billType + "/" + fullPath;
			List<String> filePaths = new ArrayList<String>();
			for (WebFile file : files) {
				FileHeader header = this.service.upload(file.getFileName(), file.getInputStream(), false, 0,
						IFileStorageConst.Bucket);
				this.saveDBInfo(fullPath, header);
				filePaths.add(fullPath + "/" + header.getName());
			}
			// 返回上传的附件列表
			NCFileVO[] ncfiles = this.service.queryNCFilesByFullPaths(filePaths.toArray(new String[0]));
			WebFileParaConvertor convertor = new WebFileParaConvertor();
			WebFileParaVO[] ret = convertor.convertAfterUpload(ncfiles, billId, IFileStorageConst.Bucket);
			return ret;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	private void checkPara(WebFile[] files) {
		long limit = this.service.getFileLimit();
		for (WebFile webfile : files) {
			if (webfile.getFileSize() > limit) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501002_0",
						"01501002-0282")/* @res 上传文件不能超过上限 */);
			}
			
//			UploadWhiteListDefineConfig config = new UploadWhiteListDefineConfig();
//			UploadWhiteListDefine[] defines = config.read(config.getFile());
//			String suffixes = "";
//			if (defines != null && defines.length > 0) {
//				UploadWhiteListDefine define = defines[0];
//				if (define != null) {
//					suffixes = define.getWhite();
//				}
//			}
		  // 校验文件格式
			//上传文件白名单从配置文件读取改为从系统参数读取
			String suffixes ="rar|zip|png|bmp|gif|jpg|pdf|html|htm|xml|doc|docx|xls|xlsx|ppt|ppts|csv|txt";


      String initCode = "AttachType";
      String pkOrg = "GLOBLE00000000000000";
      String paraString=AttachmentUploadSystemParamUtils.readSysParam(initCode,pkOrg);
      if(!nccloud.commons.lang.StringUtils.isEmpty(paraString))
    	  suffixes=suffixes+"|"+paraString;
      
			String filename = webfile.getFileName();
			// 文件名可能包含多个. modified by rockzhu 20180904
			String suffix = "";
			if (filename != null && filename.contains(".")) {
				suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			}
			if ("".equals(suffix)) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501003_0",
						"01501003-0113")/* @res 上传文件名不合法！文件名： */
						+ filename);
			}
			if (!suffixes.contains(suffix)) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501003_0",
						"01501003-0114")/* @res 上传文件不满足格式限制！文件名： */
						+ filename);
			}
		}
	}

	private NCFileNode saveDBInfo(String fullPath, FileHeader header) {
		NCFileVO attach = new NCFileVO();
		attach.setPath(header.getName());
		attach.setCreator(AppContext.getInstance().getPkUser());
		attach.setFileLen(header.getFileSize().longValue());
		attach.setPk_doc(header.getPath());
		attach.setIsdoc("z");
		NCFileNode node = this.service.saveAttachDBInfo(fullPath, attach);
		return node;
	}
}
