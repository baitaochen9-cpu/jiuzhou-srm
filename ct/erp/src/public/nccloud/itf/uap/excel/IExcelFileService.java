package nccloud.itf.uap.excel;

import nc.vo.pub.BusinessException;
import nccloud.vo.excel.scheme.BillDefination;

/**
 * 信息交换平台文件服务，单点，集群环境下从主服务器读写
 * 
 * @author cch (cch@ufida.com.cn) 2006-4-18-12:41:17
 * @modify zhaoxub 2010-05-20<br>
 *         删除<code>updateShcemeFileFromV35ToV50(String billtype,String[] exCodesOfU8)</code><br>
 * @modify zhaoxub 2011-01-21<br>
 *         1)删除<code>getBillConfigInfo(String account, String billtype)</code><br>
 *         2)删除<code>createSchemeDocument(String account, String billtype, String exsystemcode)</code><br>
 *         3)删除<code>getBillConfigInfoFromXML()</code><br>
 *         4)删除<code>getRegisterPluginInfo(String account, String billtype, String process)</code>参数process<br>
 *         5)删除<code>deletePlugin(String billtype, String process, String modulename)</code>参数process<br>
 *         6)增加<code>createSchemeDocumentByMetaData(String billtype, String exsystemcode, String billMetaDataId)</code><br>
 * @modify zhaoxub 2011-02-23<br>
 *         1)修改
 *         <code>saveSchemeFile(byte[] filedata, String account, String billtype, String exsystemcode, boolean bSaveAs)</code>
 *         删除参数account<br>
 *         2)删除<code>getServerPropFile()</code><br>
 * @modify zhaoxub 2011-03-29<br>
 *         增加 <code>getStandardSchemeBillDefination()</code>
 * @modify zhaoxub 2011-04-01<br>
 *         1)删除 <code>getSchemeFile(String billtype, String exsystemcode)</code><br>
 *         2)删除<code>saveSchemeFile(byte[] filedata, String billtype, String exsystemcode, boolean bSaveAs)</code><br>
 *         3)删除<code>deletePlugin(String billtype, String modulename)</code><br>
 * @modify zhaoxub 2011-04-28<br>
 *         1)删除 <code>public Document getStandardSchemeFile(String billtype, String exsystemcode)</code><br>
 *         2)删除 <code>public boolean isSchemeFileExist(String billtype, String exsystemcode)</code><br>
 *         3)删除 <code>public boolean isNeedRecordInputStream()</code><br>
 *         4)删除 <code>public void setNeedRecordInputStream(boolean bvalue)</code><br>
 *         4)删除 <code>Hashtable<String, PostConfigVO> getPostConfigInfosFromXML()</code><br>
 * @modify zhaoxub 2011-04-28<br>
 *         1)删除 <code>public Map<String, XternalDocInfoVO> getXternalDocInfoFromXML()</code><br>
 *         2)删除 <code>public Map<String, XternalDocInfoVO> getHsXternalDocInfo()</code><br>
 * @version V60
 */
public interface IExcelFileService {







	/**
	 * 取与指定单据类型和外系统对应的XML校验文件。 如果exsystemcode为空,那么取NC默认校验文件。
	 * 
	 * @param billtype
	 *            单据类型
	 * @param exsystemcode
	 *            外系统编码
	 * 
	 * @return BillDefination 校验文件BillDefination对象。如果不存在，抛出异常。
	 * @throws BusinessException
	 */
//	public BillDefination getStandardSchemeBillDefination(String billtype, String exsystemcode)
//			throws BusinessException;

	/**
	 * 获取校验文件定义。如果exsystemcode为空，那么取默认校验文件定义。
	 * 
	 * @param billtype
	 *            单据类型
	 * @param modeleName
	 *            模块名称
	 * @return BillDefination
	 */
	public BillDefination getSchemeDefination(String moduleName,String billtype) throws BusinessException;
	/**
	 * 取与指定单据类型和外系统对应的XML校验文件。 如果exsystemcode为空,那么取NC默认校验文件。
	 * 
	 * @param billtype
	 *            单据类型
	 * @param exsystemcode
	 *            外系统编码
	 * 
	 * @return BillDefination 校验文件BillDefination对象。如果不存在，抛出异常。
	 * @throws BusinessException
	 */
//	public BillDefination getStandardSchemeBillDefination(String billtype, String exsystemcode,int aamlevel)
//			throws BusinessException;

	/**
	 * 获取校验文件定义。如果exsystemcode为空，那么取默认校验文件定义。
	 * 
	 * @param billtype
	 *            单据类型
	 * @param exsystemcode
	 *            外系统编码
	 * @return BillDefination
	 */
//	public BillDefination getSchemeDefination(String billtype, String exsystemcode,int aamlevel) throws BusinessException;







	/**
	 * 根据单据注册的元数据信息自动创建校验文件。
	 * 
	 * @param baseClassName
	 *            VO类名称
	 * 
	 * @return BillDefination 校验文件对象。
	 * @throws BusinessException
	 */
	public BillDefination createBillDefinationByVO(String baseClassName) throws BusinessException;

	/**
	 * 根据单据注册的元数据信息自动创建校验文件。
	 * 
	 * @param billMetaDataId
	 *            元数据ID
	 * 
	 * @return BillDefination 校验文件对象。
	 * @throws BusinessException
	 */
	public BillDefination createBillDefinationByMetaData(String billMetaDataId) throws BusinessException;

//	/**
//	 * 保存单据对象形式的校验文件。
//	 * 
//	 * @param billDefination
//	 * @param billtype
//	 * @param exsystemcode
//	 * @throws BusinessException
//	 */
//	public void saveSchemeFile(BillDefination billDefination, String billtype, String exsystemcode,int aamlevel)
//			throws BusinessException;



	/**
	 * 取得服务端的文件
	 * 
	 * @param relativePath
	 *            相对于NCHOME的文件路径，如"pfxx/billdefine/aa.xml"
	 * @return byte[]
	 * @throws BusinessException
	 */
	public byte[] getServerFile(String relativePath) throws BusinessException;





	/*
	 * 返回校验文件文件名
	 * @param billType 单据类型。
	 * @param aamlevel 应用资产层级。
	 * @param exsystemcode 外部系统code。
	 * @param std 是不是standard.
	 * @return String BilldefineFileName.
	 */
	public String getBilldefineFileName(String moduleName,String billType);
	
 
}
