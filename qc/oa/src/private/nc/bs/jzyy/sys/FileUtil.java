
package nc.bs.jzyy.sys;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.jzyy.sys.FileVO;
import nc.vo.pub.filesystem.FileTypeConst;
import nc.vo.pub.filesystem.NCFileNode;
import nc.vo.pub.filesystem.NCFileVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.itf.uap.pf.NCCWorkFlowService;
import nccloud.pubitf.platform.attachment.IAttachmentService;
import nccloud.pubitf.platform.attachment.IFileStorageConst;
import nccloud.pubitf.platform.attachment.WebFileParaVO;
import uap.pub.fs.client.FileStorageClient;
import uap.pub.fs.domain.basic.FileHeader;
/**
 * 文件附件接口
 * 
 * @author Administrator
 *
 */
public class FileUtil {

	/**
	 * 查询合同附件
	 * 
	 * @param srcPk        合同主键
	 * @param downLoadFlag 是否下载
	 * @return
	 */
	public static FileVO[] queryFiles(String srcPk) {
		return queryFiles(srcPk, false);
	}
	public static FileVO[] queryFiles(String srcPk, boolean downLoadFlag) {
		try {
			WebFileParaVO webFileParaVO = new WebFileParaVO();
			webFileParaVO.setBillId(srcPk);
			webFileParaVO.setFullPath(srcPk);

			NCFileVO[] ncfiles = null;
			String newBucket = getStorePath(webFileParaVO);
			if (IFileStorageConst.Riamsgattachfiles.equals(newBucket)) {
				NCCWorkFlowService wfService = NCLocator.getInstance().lookup(NCCWorkFlowService.class);
				ncfiles = wfService.getAttacthByPkCheckFlow(webFileParaVO.getBillId());
			} else {
				String userId = InvocationInfoProxy.getInstance().getUserId();
				IAttachmentService ncservice = NCLocator.getInstance().lookup(IAttachmentService.class);
				ncfiles =  ncservice.queryNCFilesByNodePath(webFileParaVO.getFullPath(), userId);
			}
			WebFileParaVO[] retWebFileParaVO = convert(ncfiles, webFileParaVO.getBillId(), IFileStorageConst.Bucket);
			IAttachmentService ncservice = NCLocator.getInstance().lookup(IAttachmentService.class);

			ArrayList<FileVO> fileList = new ArrayList<>();
			for (WebFileParaVO item : retWebFileParaVO) {
				FileVO fileVO = new FileVO();
				BeanUtils.copyProperties(fileVO, item); // 将item 转为 fileVO
				if (downLoadFlag) {
					InputStream in = ncservice.download(item.getIsdoc(), item.getPk_doc(), getStorePath(item));
					fileVO.setName(item.getName());
					fileVO.setInputStream(in);
					fileVO.setBase64Str(Base64Utils.getBase64FromInputStream(in));
				}
				fileList.add(fileVO);
			}
			return fileList.toArray(new FileVO[0]);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e);
			ExceptionUtils.wrappException(e);
		}
		return null;
	}

	/**
	 * 查询附件并且下载
	 * 
	 * @param pk
	 * @return
	 */
	public static FileVO[] queryFilesAndDownload(String pk) {
		return queryFiles(pk, true);
	}

	private static String getStorePath(WebFileParaVO para) {
		String storepath = IFileStorageConst.Bucket;
		if (para.getStorepath() != null && !"".equals(para.getStorepath())) {
			storepath = para.getStorepath();
		}

		return storepath;
	}

	/**
	 * 转换数据格式
	 * 
	 * @param files
	 * @param billID
	 * @param newBucket
	 * @return
	 */
	public static WebFileParaVO[] convert(NCFileVO[] files, String billID, String newBucket) {
		List<WebFileParaVO> list = new ArrayList<WebFileParaVO>();
		String clientIP = InvocationInfoProxy.getInstance().getClientHost();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				NCFileVO file = files[i];
				if (!file.isFolder()) {
					WebFileParaVO paras = new WebFileParaVO();
					paras.setBillId(billID);
					paras.setPk(file.getPk());
					paras.setFullPath(file.getFullPath());
					paras.setIsdoc(file.getIsdoc());
					paras.setName(file.getName());
					paras.setPk_doc(file.getPk_doc());
					paras.setCreater(file.getCreator());
					paras.setFileSize(file.getFileLen());
					if (FileTypeConst.IMAGE.equalsIgnoreCase(file.getFiletype())
							&& "z".equalsIgnoreCase(file.getIsdoc())) {
						String url = FileStorageClient.getInstance().getViewURL(newBucket, file.getPk_doc(), clientIP);
						String suffix = file.getFullPath().substring(file.getFullPath().lastIndexOf(".") + 1);
						paras.setPreviewUrl(url);
						paras.setSuffix(suffix);
					}
					list.add(paras);
				}
			}
		}
		return (WebFileParaVO[]) list.toArray(new WebFileParaVO[0]);
	}

	/**
	 * 附件上传
	 * 
	 * @param pk       单据主键
	 * @param fileData 文件内容Base64字符串
	 * @param fileName 文件名称
	 */
	public static FileHeader uploadFiles( String fileData, String fileName) {
		InputStream inputStreamFromBase64 = Base64Utils.getInputStreamFromBase64(fileData);
		return uploadFiles(inputStreamFromBase64, fileName);
	}

	/**
	 * 附件上传
	 * 
	 * @param pk       单据主键
	 * @param fileData InputStream输入流
	 * @param fileName 文件名称
	 */
	public static FileHeader uploadFiles(InputStream fileData, String fileName) {
		IAttachmentService ncservice = NCLocator.getInstance().lookup(IAttachmentService.class);
		FileHeader header = ncservice.upload(fileName, fileData, false, 0, IFileStorageConst.Bucket);
		return header;
	}

	/**
	 * 保存文件
	 * 
	 * @param pk     主键
	 * @param header FileHeader对象
	 * @return
	 */
//	private static NCFileNode saveDBInfo(String pk, FileHeader header) {
//		IAttachmentService ncservice = NCLocator.getInstance().lookup(IAttachmentService.class);
//		NCFileVO attach = new NCFileVO();
//		attach.setPath(header.getName());
//		attach.setCreator(AppContext.getInstance().getPkUser());
//		attach.setFileLen(header.getFileSize().longValue());
//		attach.setPk_doc(header.getPath());
//		attach.setIsdoc("z");
//		return ncservice.saveAttachDBInfo("uapbd/"+header.getPath()+"/"+pk, attach);
//	}
	public static NCFileNode saveDBInfo(String fullPath, FileHeader header) {
		IAttachmentService ncservice = NCLocator.getInstance().lookup(IAttachmentService.class);
		NCFileVO attach = new NCFileVO();
		attach.setPath(header.getName());
		attach.setCreator(AppContext.getInstance().getPkUser());
		attach.setFileLen(header.getFileSize());
		attach.setPk_doc(header.getPath());
		attach.setIsdoc("z");
		NCFileNode node = ncservice.saveAttachDBInfo(fullPath, attach);
		return node;
	}

}
