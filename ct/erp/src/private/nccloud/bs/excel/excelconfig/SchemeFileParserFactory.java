/*
 * Created on 2006-4-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nccloud.bs.excel.excelconfig;

import java.io.File;

import nc.bs.ml.NCLangResOnserver;
import nc.vo.jcom.io.fileparse.FileParseException;
import nc.vo.jcom.io.fileparse.FileParserFactory;
import nc.vo.jcom.io.fileparse.IFileParser;

/**
 * 校验文件解析器工厂，暂时只处理文件形式。
 * 
 * @author ljian
 */
public class SchemeFileParserFactory extends FileParserFactory
{
	@Override
	protected IFileParser createFileParser(File file)
	{
		return new SchemeFileParser(file);
	}

	@Override
	protected IFileParser createDirectoryParser(File file) throws FileParseException
	{
		throw new FileParseException(NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-V50039")/*"校验文件不支持解析目录！"*/);
	}
}
