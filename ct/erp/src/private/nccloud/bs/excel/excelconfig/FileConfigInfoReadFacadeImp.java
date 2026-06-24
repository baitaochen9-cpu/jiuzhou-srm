/*
 * Created on 2006-4-11
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package nccloud.bs.excel.excelconfig;

import java.io.File;
import java.util.List;
import nc.bs.logging.Logger;
import nc.vo.jcom.io.fileparse.IntellijFileReader;
import nc.vo.jcom.io.fileparse.IntellijFileReaderPool;
import nc.vo.pfxx.exception.FileConfigException;
import nccloud.bs.excel.util.PfxxServerSideUtils;
import nccloud.nc.ui.trade.excelimport.vo.ISendResult;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Field;
import nccloud.vo.excel.scheme.Record;
import nc.vo.jcom.io.fileparse.FileParseException;

/**
 * 后台配置文件读取接口类。(文件读取目前不区分帐套)
 * 
 * @author ljian
 */
public class FileConfigInfoReadFacadeImp
{





	/**
	 * 后台获取scheme文档。
	 * 
	 * @param billType
	 * @param exsystemcode
	 * @return
	 * @throws FileConfigException
	 */
//	public static BillDefination getStandardSchemeBillDefination(String billType, String exsystemcode, int aamlevel) throws FileConfigException
//	{
//		return getSchemeDefinationFromXML(billType, aamlevel, exsystemcode, true);
//	}
//
//	public static BillDefination getSchemeDefinationFromXML(String billType, String exsystemcode, int aamlevel) throws FileConfigException
//	{
//		return getSchemeDefinationFromXML(billType, aamlevel, exsystemcode, false);
//	}

	/**
	 * 后台获取scheme文档。
	 * 根据单据类型，自动查找合适的单据定义
	 * @param billType
	 * @param exsystemcode
	 * @return
	 * @throws FileConfigException
	 */
//	public static BillDefination getStandardSchemeBillDefination(String billType, String exsystemcode) throws FileConfigException
//	{
//		return getSchemeDefinationFromXML(billType, null, exsystemcode, true);
//	}

	/**
	 * 读取sheme定义。根据单据类型，自动查找合适的单据定义
	 * 
	 * @param billType
	 * @param exsystemcode
	 * @return
	 * @throws FileConfigException
	 */
	public static BillDefination getSchemeDefinationFromXML(String moduleName,String billType) throws FileConfigException
	{
		return getSchemeDefinationFromXML(moduleName,billType, false);
	}

	private static BillDefination getSchemeDefinationFromXML(String modulename,String billType,boolean extend) throws FileConfigException
	{
		List<String> fileNames = PfxxServerSideUtils.getSchemeFilename(modulename,billType,extend);
		File file = null;
		for (String fileName : fileNames)
		{
			file = new File(fileName);
			if (file.exists())
			{
				break;
			}
		}
		if (file == null || !file.exists())
		{
			Logger.error("can't find BillDefination file........");
			throw new FileConfigException(billType, ISendResult.ERR_CONFIG_SCHEME_NONE, nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("pfxx",
					"UPPpfxx-000273")/* @res "从后台读取校验文件发生异常!" */);
		}
		try
		{
			// 智能读取,自动缓存,文件修改后自动更新
			IntellijFileReader filereader = IntellijFileReaderPool.getIntellijFileReader(new SchemeFileParserFactory().getFileParser(file));
			//此处对multilangtype属性加入到每个field里面。
			List<Record> records = ((BillDefination) filereader.getResult()).getRecords();
			String multilangtype = ((BillDefination) filereader.getResult()).getMultiLangType();
			for(Record record : records){
				List<Field> fields = record.getFields();
				for(Field field : fields){
					field.setMultiLangType(multilangtype);
				}
		    }
			return (BillDefination) filereader.getResult();
		}
		catch (FileParseException e)
		{
			Logger.error(e.getMessage(), e);
			throw new FileConfigException(file.getAbsolutePath(), ISendResult.ERR_CONFIG_SCHEME_NONE, nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"pfxx", "UPPpfxx-000273")/* @res "从后台读取校验文件发生异常!" */);
		}
	}


	public static String getSchemeDefinationFileName(String moduleName,String billType) throws FileConfigException
	{
		List<String> fileNames = PfxxServerSideUtils.getSchemeFilename(moduleName,billType,false);
		File file = null;
		for (String fileName : fileNames)
		{
			file = new File(fileName);
			if (file.exists())
			{
				break;
			}
		}
		if (file == null || !file.exists())
		{
			Logger.error("can't find BillDefination file........");
			throw new FileConfigException(billType, ISendResult.ERR_CONFIG_SCHEME_NONE, nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("pfxx",
					"UPPpfxx-000273")/* @res "从后台读取校验文件发生异常!" */);
		}
		return file.getAbsolutePath();
	}
}
