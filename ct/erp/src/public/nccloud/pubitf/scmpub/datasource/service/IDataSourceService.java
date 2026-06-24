package nccloud.pubitf.scmpub.datasource.service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nccloud.dto.scmpub.datasource.entity.TreeNode;

/**
 * @description 数据源配置接口
 * @author guozhq
 * @date 2019-3-1 下午1:55:26
 * @version ncc1.0
 */
public interface IDataSourceService {

	/**
	 * 
	 * TODO
	 * 
	 * @param classfullName
	 * @param metaid
	 * @return
	 * @throws BusinessException
	 * 
	 */
	public TreeNode[] queryOBAMetaTree(String billType, String metaid, String metaFullName) throws BusinessException;

	public File generateXML(Map<String, String> nodes, String billType, String fileName) throws BusinessException;

	public void generatorDoc(InputStream in, OutputStream out, String billType, String[] billIds);
}
