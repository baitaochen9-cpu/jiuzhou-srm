/*
 * Created on 2005-4-4
 */
package nccloud.bs.excel.excelconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import nc.bs.ml.NCLangResOnserver;
import nc.vo.jcom.io.fileparse.FileParseException;
import nc.vo.jcom.io.fileparse.FileParser;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Field;
import nccloud.vo.excel.scheme.Record;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * @author cch
 * @modify zhaoxub 2011-02-23 <br>
 *         提取常量到PfxxConstants
 */
class SchemeFileParser extends FileParser
{
	public SchemeFileParser(File file)
	{
		super(file);
	}

	/**
	 * 返回scheme文档
	 */
	@Override
	public Object getResult() throws FileParseException
	{
		BillDefination billDefination = null;
		XStream xstream = new XStream(new StaxDriver());
		Annotations.configureAliases(xstream, BillDefination.class, Record.class, Field.class);
		try
		{
			billDefination = (BillDefination) xstream.fromXML(new FileInputStream(file));
		}
		catch (FileNotFoundException e)
		{
			throw new FileParseException(NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-000254")
			/*
			 * @res "读取插件注册文件时出现异常，请检查文件是否存在，文件:"
			 */
			+ file.getPath());
		}
		return billDefination;
	}

	/**
	 * 返回scheme定义
	 */
	@Override
	public Object getResultMore() throws FileParseException
	{
		return null;
	}

	@Override
	public String getParserID()
	{
		return "Scheme";
	}
}