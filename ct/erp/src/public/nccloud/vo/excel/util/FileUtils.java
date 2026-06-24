package nccloud.vo.excel.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

import org.w3c.dom.Node;

import nc.bs.logging.Logger;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.logging.Debug;

/**
 * Created by IntelliJ IDEA. User: cch Date: 2004-11-16 Time: 13:41:30 文件操作基本类
 */
public class FileUtils {

	/**
	 * 将字节流写入文件
	 * 
	 * @param filedata
	 * @param filename
	 * @return
	 */
	public static File writeBytesToFile(byte[] filedata, String filename) throws IOException {
		File file = new File(filename);
		File pFile = file.getParentFile();
		if (!pFile.exists())
			pFile.mkdirs();
		Debug.debug(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000521",
				null, new String[] { file.getAbsolutePath() })/*
															 * @res "文件保存路径为：" + file .getAbsolutePath()
															 */);
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
			outStream.write(filedata);
			outStream.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(outStream);
		}

		Debug.debug(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000522",
				null, new String[] { file.getAbsolutePath() })/*
															 * @res "文件保存成功，已保存到：" + file .getAbsolutePath()
															 */);
		return file;
	}

	/**
	 * 从文件中得到字节流
	 * 
	 * @param newfile
	 * @return
	 */
	public static ByteArrayOutputStream getByteStreamFromFile(File newfile) throws Exception {
		byte[] buffer = new byte[1000];
		ByteArrayOutputStream out = null;
		InputStream stream = null;
		try {
			stream = new FileInputStream(newfile);
			out = new ByteArrayOutputStream();
			int index = 0;
			while ((index = stream.read(buffer, 0, 1000)) > 0) {
				out.write(buffer, 0, index);
			}
			out.flush();
		} catch (IOException e) {
		} finally {
			IOUtils.closeQuietly(stream);
			IOUtils.closeQuietly(out);
		}
		return out;
	}

	/**
	 * 将doc写入XML文件
	 * 
	 * @param newdoc
	 * @return
	 */
	public static File writeDocToXMLFile(Node newdoc, String filename) throws IOException {
		File file = new File(filename);
		File pFile = file.getParentFile();
		if (!pFile.exists())
			pFile.mkdirs();
		Debug.debug(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0197")/* @res "文件保存路径为：" */
				+ file.getAbsolutePath());
		// 设置多语言环境
		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		PrintWriter writer = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			writer = new PrintWriter(outputStreamWriter);
			XMLUtil.printDOMTree(writer, newdoc, 0, "UTF-8");
			// XmlUtils.printDOMTree(null, newdoc, 2, "");
			writer.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(outputStreamWriter);
			IOUtils.closeQuietly(writer);
		}

		Debug.debug(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0198")/* @res "文件保存成功，已保存到：" */
				+ file.getAbsolutePath());
		return file;
	}

	/**
	 * 取得文件扩展名
	 * 
	 * @param f
	 * @return
	 */
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * 读取一个Excel单元格中的内容。
	 * 
	 * @param cell
	 * @return 以字符串形式返回。
	 */
	public static String getCellValue(HSSFCell cell) {
		String str;

		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
		case 0:
			str = String.valueOf(cell.getNumericCellValue());

			double dnum = Double.parseDouble(str);
			// long lnum = ( long ) dnum;
			str = String.valueOf(dnum);

			// FIXME:: 取出来的老是浮点数,硬编码取出小数点0,有了合适的办法再改
			if (str.endsWith(".0")) {
				str = str.substring(0, str.indexOf(".0"));
			}

			break;

		case 1:
			str = String.valueOf(cell.getRichStringCellValue());

			break;

		case 3:
			str = "";

			break;

		case 4:
			str = String.valueOf(cell.getBooleanCellValue());

			break;

		case 2:
			str = cell.getCellFormula();

			break;

		default:
			Logger.error("Not a supported cell type");
			str = null;

			break;
		}

		return str;
	}

	/**
	 * 搬移文件到指定目录。
	 * 
	 * @param srcFile
	 *            源文件
	 * @param dstDir
	 *            目标目录
	 * @return 若成功true，否则false。
	 */
	public static boolean moveFile(File srcFile, File dstDir) {
		if (srcFile == null || srcFile.isFile() == false) {
			return false;
		}
		if (dstDir == null || dstDir.isDirectory() == false) {
			return false;
		}

		return srcFile.renameTo(new File(dstDir, srcFile.getName()));

	}

	/**
	 * Node ---> byte
	 * 
	 * @param doc
	 * @return
	 */
	public static byte[] getDocBinaryByteData(Node node) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(stream));
		XMLUtil.printDOMTree(writer, node, 0);
		writer.close();
		return stream.toByteArray();
	}

	public static byte[] getDocBinaryByteData(Node node, String charsetName) throws UnsupportedEncodingException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(stream, charsetName));
		XMLUtil.printDOMTree(writer, node, 0, charsetName);
		writer.close();
		return stream.toByteArray();
	}
}