package nc.bs.srm.ic.po.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IAttachManageConst;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.bs.srm.pub.SenderQuerys;
import nc.cmp.tools.StringUtil;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ms.tb.outline.util.Base64Utils;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.filesystem.NCFileNode;
import nc.vo.pub.filesystem.NCFileVO;
import nccloud.api.jzsrm.AbstracProcessor4Ext;
import sun.misc.BASE64Decoder;
import uap.pub.fs.client.FileStorageClient;
import uap.pub.fs.domain.basic.FileHeader;
import uap.pub.fs.domain.ext.IFileStorageExt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 采购订单新增附件
 * 1、推送字段，订单号，归档号，文件流
 * 2、支持传多个文件流，每次传都是NC全量接收。例如：一个订单，第一次推送2个文件流，第二次推送一个
 * 处理逻辑是，先删除两个，再新增一个。
 * 
 * @author htf
 * 
 */
public class SRM_Poorder_fileadd extends AbstracProcessor4Ext {

	private SenderQuerys sqy = new SenderQuerys();
	/**
	 * 附件操作类
	 */
	private IFileSystemService service = (IFileSystemService) NCLocator.getInstance().lookup(IFileSystemService.class);

	private String pk_order;
	@Override
	public JSONObject process(Object billvo) throws Exception {
		JSONObject reqJson = (JSONObject) billvo;
		JSONObject json = reqJson.getJSONObject("bill");
		String poorder_code = json.getString("displayPoNum");
		String onFile_code = json.getString("archiveCode");
	
		JSONArray pdfs = json.getJSONArray("archiveAttachmentEIlist");
		
		/*
		 * 附件新增整体逻辑
		 * 1.根据订单号查到订单，查看订单是否存在，查看附件是否存在
		 * 2.不存在订单，给出提示报错
		 * 3.存在附件，则先删后增；不存在附件，则直接新增即可
		 * 
		 */
		if (StringUtil.isEmpty(poorder_code)) {
			throw new BusinessException("采购订单号poorder_code：不能为空");
		}
		VOQuery<OrderHeaderVO> query = new VOQuery<OrderHeaderVO>(OrderHeaderVO.class);
		OrderHeaderVO[] hvos = query.query(" and vbillcode='" + poorder_code + "' and BISLATEST = 'Y'", null);

		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("" + poorder_code + "采购订单在NC不存在");
		} 
		try{
			pk_order = hvos[0].getPrimaryKey();
			String sql = "select storepath from bap_fs_body where headid = (select guid from bap_fs_header where path in " +
					"(select pk_doc from sm_pub_filesystem where filepath = '"+pk_order+"' or filepath like '%"+pk_order+"%'))";
			List<Object[]> storePataArr = (List<Object[]>)getDao().executeQuery(sql, new ArrayListProcessor());
			//如果查到pdf删除
			if(storePataArr != null || storePataArr.size() > 0){
				service.deleteNCFileNode(pk_order);
			}
	
			BASE64Decoder decpder = new BASE64Decoder();
			for (int i = 0; i < pdfs.size(); i++) {
				JSONObject pdfJson = pdfs.getJSONObject(i);
				String fileName = pdfJson.getString("fileName");
				if(StringUtil.isEmpty(fileName)){
					throw new BusinessException("文件名称不能为空");
				}
				String pdf = pdfJson.getString("archiveAttachmentEI");
				if(StringUtil.isEmpty(pdf)){
					throw new BusinessException("文件流不能为空");
				}
				byte[] decodeBuffer = decpder.decodeBuffer(pdf);
				ByteArrayInputStream inputStream = new ByteArrayInputStream(decodeBuffer);
				uploadFile(pk_order,fileName,inputStream);
				if(StringUtil.isNotEmpty(onFile_code)){
					String upSql = "update po_order set vdef5 = '"+onFile_code+"' where pk_order = '"+pk_order+"' ";
					getDao().executeUpdate(upSql);				}
				}
		}catch(Exception e){
			throw new BusinessException("操作采购订单出错:" + e.getMessage());
		}

		JSONObject rs = new JSONObject();
		rs.put("erp_code", poorder_code);
		return this.getRsultDataSuccess(rs, "采购订单上传附件成功");
	}
	
	/**
	 * 
	 * @param billpk 单据主键
	 * @param fileName 文件名称
	 * @param stream stream流
	 * @return
	 * @throws BusinessException
	 * @throws FileNotFoundException
	 */
		@SuppressWarnings("unused")
		public NCFileVO uploadFile(String billpk, String fileName,InputStream stream) throws BusinessException, FileNotFoundException {
			FileHeader fileHeader = FileStorageClient.getInstance().uploadFile(IAttachManageConst.RIAMoudleID, fileName, stream, false, new IFileStorageExt[0]);
			NCFileVO filevo = buildNCFileVO(fileHeader);
			NCFileNode node = service.createCloudFileNode(billpk +"/"+fileName, InvocationInfoProxy.getInstance().getUserId(), filevo);
			return filevo;
		}
		
		private NCFileVO buildNCFileVO(FileHeader fileHeader) {
			String pk_doc = fileHeader.getPath();
			String name = fileHeader.getName();
			long size1 = fileHeader.getFileSize().longValue();
			NCFileVO filevo = new NCFileVO();
			filevo.setPath(name);
			filevo.setCreator(InvocationInfoProxy.getInstance().getUserId());
			filevo.setIsdoc("z");
			filevo.setFileLen(size1);
			filevo.setPk_doc(pk_doc);
			return filevo;
		}
}
