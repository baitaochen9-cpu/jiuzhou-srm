package nccloud.bs.excel.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nc.vo.jcom.lang.StringUtil;
import nccloud.vo.excel.pub.ExcelConstants;

/**
 */
public class PfxxServerSideUtils
{
	/**
	 * 不同外部系统的scheme文件名构造 此方法只能在服务器端调用
	 * 根据单据类型查出单据处理定义，此处不需要层次
	 * @param billtype
	 * @param exsystemcode
	 * @return
	 */
	public static List<String> getSchemeFilename(String moduleName, String billtype, boolean extend)
	{
		return getSchemeFilenames(moduleName,billtype);
	}

	private static List<String> getSchemeFilenames(String modulename, String billtype) 
	{
		// TODO bs
		List<String> list = new ArrayList<String>();

		list.add(getSchemeFilename(modulename, billtype));
		return list;
	}

	//	public static String getSchemeFilename(String billtype, int aamlevel, String exsystemcode)
	//	{
	//		return getSchemeFilename(billtype, aamlevel, exsystemcode, false);
	//	}
	/**
	 * 不同外部系统的scheme文件名构造 此方法只能在服务器端调用
	 * 唯一确定一个
	 * @param billtype
	 * @param exsystemcode
	 * @return
	 */
//	public static List<String> getSchemeFilename(String billtype, Integer aamlevel, String exsystemcode, boolean standard)
//	{
		// 
//		BusinessProcessorDefination bpDefination = null;
//		try
//		{
//			if (aamlevel != null)
//			{
//				bpDefination = PfxxUtils.lookUpPFxxFileService().getBusinessProcessorDefination(null, billtype, aamlevel);
//			}
//			else
//			{
//				bpDefination = PfxxUtils.lookUpPFxxFileService().getBusinessProcessorDefination(null, billtype);
//			}
//		}
//		catch (BusinessException e)
//		{
//			Logger.error(e.getMessage(), e);
//		}
//		if (bpDefination == null)
//		{
//			throw new PfxxRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0102")/*@res "没有取得对应的单据定义，请检查是否存在单据类型："*/
//					+ billtype + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0103")/*@res "的定义文件。文件目录：NCHOME\\pfxx\\businessprocessor"*/);
//		}
//		return getSchemeFilename(modulename, billtype);
//	}
	
	/**
	 * 指定层次优先
	 * @param billtype
	 * @param aamlevel
	 * @param exsystemcode
	 * @param standard
	 * @return
	 */
//	public static  String  getSchemeFilenameLevelFirst(String billtype, Integer aamlevel, String exsystemcode, boolean standard)
//	{
//		// 
//		BusinessProcessorDefination bpDefination = null;
//		try
//		{
//			if (aamlevel != null)
//			{
//				bpDefination = PfxxUtils.lookUpPFxxFileService().getBusinessProcessorDefination(null, billtype, aamlevel);
//			}
//			//
//			if (bpDefination==null)
//			{
//				bpDefination = PfxxUtils.lookUpPFxxFileService().getBusinessProcessorDefination(null, billtype);
//			}
//		}
//		catch (BusinessException e)
//		{
//			Logger.error(e.getMessage(), e);
//		}
//		if (bpDefination == null)
//		{
//			throw new PfxxRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0102")/*@res "没有取得对应的单据定义，请检查是否存在单据类型："*/
//					+ billtype + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0103")/*@res "的定义文件。文件目录：NCHOME\\pfxx\\businessprocessor"*/);
//		}
//		return getSchemeFilename(standard,bpDefination.getModuleName(),aamlevel, billtype, exsystemcode);
//	}
	public static String getBusinessProcessorKey(String billtype, int aamlevel)
	{
		return MessageFormat.format("{1}_{0}", billtype, aamlevel);
	}

	//	/**
	//	 * 取非标准billdefine，实际交换规则文件
	//	 *
	//	 * @author
	//	 * @param billtype
	//	 * @param exsystemcode
	//	 * @return
	//	 */
	//	public static List<String> getSchemeFilename(String billtype, String exsystemcode)
	//	{
	//		return getSchemeFilename(billtype, exsystemcode, false);
	//	}
	private static String getSchemeFilename(String moduleName, String billtype)
	{
		StringBuffer sb = new StringBuffer();
			sb.append(ExcelConstants.BILLDEFINE_PATHS);
		if (!StringUtil.isEmptyWithTrim(moduleName))
		{
			sb.append(moduleName).append('/');
		}
		sb.append(billtype).append(".xml");
		return sb.toString();
	}
}