package nccloud.bs.excel.pub;

import java.io.ByteArrayOutputStream;
import java.io.File;


import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.pfxx.exception.FileConfigException;
import nc.vo.pfxx.exception.PfxxException;
import nc.vo.pub.BusinessException;
import nccloud.bs.excel.excelconfig.FileConfigInfoReadFacadeImp;
import nccloud.bs.excel.scheme.ISchemeCreator;
import nccloud.bs.excel.scheme.MetaDataSchemeCreator;
import nccloud.bs.excel.scheme.SchemeCreator;
import nccloud.itf.uap.excel.IExcelFileService;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.util.FileUtils;

public class ExcelFileServiceImpl implements IExcelFileService
{




	/**
	 * 根据单据类型，自动查找合适的单据定义
	 * @see nc.itf.uap.pfxx.IPFxxFileService#getStandardSchemeBillDefination(java.lang.String, java.lang.String)
	 */
//	@Override
//	public BillDefination getStandardSchemeBillDefination(String billtype, String exsystemcode) throws BusinessException
//	{
//		return FileConfigInfoReadFacadeImp.getStandardSchemeBillDefination(billtype, exsystemcode);
//	}

	/**
	 * 根据单据类型，自动查找合适的单据定义
	 * @see nc.itf.uap.pfxx.IPFxxFileService#getSchemeDefination(java.lang.String, java.lang.String)
	 */
	@Override
	public BillDefination getSchemeDefination(String moduleName,String billtype) throws BusinessException
	{
		return FileConfigInfoReadFacadeImp.getSchemeDefinationFromXML(moduleName,billtype);
	}

//	@Override
//	public BillDefination getStandardSchemeBillDefination(String billtype, String exsystemcode, int aamlevel) throws BusinessException
//	{
//		return FileConfigInfoReadFacadeImp.getStandardSchemeBillDefination(billtype, exsystemcode, aamlevel);
//	}

//	@Override
//	public BillDefination getSchemeDefination(String billtype, String exsystemcode, int aamlevel) throws BusinessException
//	{
//		return FileConfigInfoReadFacadeImp.getSchemeDefinationFromXML(billtype, exsystemcode,aamlevel);
//	}



	@Override
	public BillDefination createBillDefinationByVO(String baseClassName) throws BusinessException
	{
		try
		{
			ISchemeCreator schemeCreator = new SchemeCreator(baseClassName);
			return schemeCreator.generate();
		}
		catch (Exception e)
		{
			String errorinfo = NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000003")/*
																															 * @res "从文件取流数据发生异常"
																															 */;
			throw new PfxxException(errorinfo, e);
		}
	}

	@Override
	public BillDefination createBillDefinationByMetaData(String billMetaDataId) throws BusinessException
	{
		try
		{
			ISchemeCreator schemeCreator = new MetaDataSchemeCreator(billMetaDataId);
			return schemeCreator.generate();
		}
		catch (Exception e)
		{
			String errorinfo = NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000003")/*
																															 * @res "从文件取流数据发生异常"
																															 */;
			throw new PfxxException(errorinfo, e);
		}
	}

//	@Override
//	public void saveSchemeFile(BillDefination billDefination, String billtype, String exsystemcode, int aamlevel) throws BusinessException
//	{
//		FileConfigInfoWriteFacade.saveSchemaInfo(billDefination, billtype, exsystemcode, aamlevel);
//	}



	@Override
	public byte[] getServerFile(String relativePath) throws BusinessException
	{
		try
		{
			if (StringUtil.isEmpty(relativePath))
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0098")/*@res "文件路径为空!无法取得服务端的文件!"*/);
			if (!relativePath.startsWith("/"))
				relativePath = "/" + relativePath;
			String filename = RuntimeEnv.getInstance().getNCHome() + relativePath;
			File file = new File(filename);
			if (file.exists())
			{
				ByteArrayOutputStream out = null;
				try
				{
					out = FileUtils.getByteStreamFromFile(file);
				}
				catch (Exception e)
				{
					throw new PfxxException(NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000003")/*
																																		 * @res "从文件取流数据发生异常"
																																		 */);
				}
				return out.toByteArray();
			}
			throw new PfxxException(NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-000206")/*
																										 * @res
																										 * "文件不存在，文件路径："
																										 */
					+ filename);
		}
		catch (PfxxException e)
		{
			throw new PfxxException(NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-000207")/*
																										 * @res
																										 * "从后台读取文件发生错误："
																										 */
					+ e.getMessage());
		}
	}


	
//	public boolean  saveMultiLang(String[][] data , String billtype, String partpath ){
//		boolean success = true;
//		String filepath = PfxxConstants.CONFIG_PATH + "resources/" +partpath + "/" +billtype;
//		for(int i = 0; i < data.length; i++){
//		nc.bs.pfxx.makemultilang.PfxxMultiLangUtil.makeMultiLang(filepath, data[i][1], data[i][2], data[i][3], data[i][4], data[i][5]);
//		}
//		return success;
//	}
	@Override
	public String getBilldefineFileName(String moduleName,String billType){
		try {
			return FileConfigInfoReadFacadeImp.getSchemeDefinationFileName(moduleName,billType);
		} catch (FileConfigException e) {
			Logger.error("Can't find billdefination file.");
		}
		return null;
	}
}