package nccloud.bs.excel.scheme;

import nc.vo.pub.BusinessException;
import nccloud.vo.excel.scheme.BillDefination;

/**
 * Created by IntelliJ IDEA.
 * User: cch
 * Date: 2004-11-30
 * Time: 11:20:06
 * To change this template use File | Settings | File Templates.
 */
public interface ISchemeCreator {

	/**
	 * 产生校验文件
	 * 
	 * @return
	 */
	public BillDefination generate() throws BusinessException;

//	public File generateSchemeFile() throws BusinessException;
//
//	public File generateSchemeFile(String filename) throws BusinessException;
}
