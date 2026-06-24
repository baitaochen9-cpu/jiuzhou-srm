package nccloud.pubimpl.scmpub.datasource.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.context.MDNode;
import nc.md.persist.framework.MDPersistenceService;
import nc.pub.oba.OBABaseUtil;
import nc.uap.oba.word.merger.AbstractDocumentMerger;
import nc.uap.oba.word.merger.ITemplateEntity;
import nc.uap.oba.word.merger.impl.DocumentMerger;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.oba.OBAStaticInfo;
import nccloud.commons.lang.StringUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nccloud.dto.scmpub.datasource.entity.TreeNode;
import nccloud.dto.scmpub.datasource.utils.CreateDataByBillVOUtil;
import nccloud.pubitf.scmpub.datasource.service.IDataSourceService;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @description 数据源配置服务类实现
 * @author guozhq
 * @date 2019-3-1 下午2:09:33
 * @version ncc1.0
 */
public class DataSourceServiceImpl implements IDataSourceService {

	@Override
	public TreeNode[] queryOBAMetaTree(String billType, String metaid, String metaFullName) throws BusinessException {
		List<TreeNode> result = new ArrayList<TreeNode>();
		if (metaid == null) {
			// 根节点
			IBean bean = null;
			if (StringUtils.isNotBlank(billType)) {
				bean = PfMetadataTools.queryMetaOfBilltype(billType);
			} else if (StringUtils.isNotBlank(metaFullName)) {
				bean = MDBaseQueryFacade.getInstance().getBusinessEntityByFullName(metaFullName);
			}

			MDNode rootNode = new MDNode(bean, null);
			TreeNode node = new TreeNode(rootNode);
			node.setIsRoot(true);
			result.add(node);
		} else {
			IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(metaid);
			MDNode rootNode = new MDNode(bean, null);
			TreeNode node = new TreeNode(rootNode);
			List<TreeNode> childs = node.getChildNodes();
			result.addAll(childs);
		}
		return result.toArray(new TreeNode[result.size()]);
	}

	@Override
	public File generateXML(Map<String, String> checkNodes, String billType, String fileName) throws BusinessException {
		// 获取根节点及其属性的TreeNode
		IBean bean = PfMetadataTools.queryMetaOfBilltype(billType);
		MDNode rootNode = new MDNode(bean, null);
		TreeNode node = new TreeNode(rootNode);
		List<TreeNode> childs = node.getChildNodes();

		String beancode = rootNode.getOwnerBean().getName();
		String beadname = rootNode.getOwnerBean().getDisplayName();
		Document doc = OBABaseUtil.getInstance().newDocumentBuilder();
		// 实体页签
		Element country = doc.createElement(beancode);
		country.setAttribute(OBAStaticInfo.UAPDISPLAYNAME, beadname);
		doc.appendChild(country);

		Map<String, String> tabcodeMap = new LinkedHashMap<String, String>();// 页签code,name
		Map<String, String> mainnodeMap = new LinkedHashMap<>();// 主页签属性node
		Map<String, Map<String, String>> allnodeMap = new LinkedHashMap<>();// 全部非页签属性

		for (Map.Entry<String, String> entry : checkNodes.entrySet()) {
			String[] paths = entry.getKey().split("\\.");

			TreeNode treeNode = null;
			if (paths != null && paths.length == 1) {
				treeNode = node;
			} else {
				treeNode = this.findOBATreeNodeByName(childs, paths[1]);
			}
			if (isTabTreeNode(treeNode)) {
				// 子页签
				if(paths.length > 2) {//如果是子页签的根节点不处理
					String key = this.getTreeNodeKey(paths, 2);
					String name = treeNode.getNode().getAttribute().getName();
					tabcodeMap.put(name, treeNode.getNode().getAttribute().getDisplayName());
					if (allnodeMap.containsKey(name)) {
						Map<String, String> nodeMap = allnodeMap.get(name);
						nodeMap.put(key, entry.getValue());
					} else {
						Map<String, String> nodeMap = new LinkedHashMap<>();
						nodeMap.put(key, entry.getValue());
						allnodeMap.put(name, nodeMap);
					}
				}
			} else {
				// 主页签
				String key = this.getTreeNodeKey(paths, 1);
				mainnodeMap.put(key, entry.getValue());
			}
		}

		if (mainnodeMap.size() > 0) {
			this.mainNodeBuilding(rootNode, doc, country, mainnodeMap);
		}
		if (allnodeMap.size() > 0) {
			this.tabNodeBuilding(doc, country, tabcodeMap, allnodeMap);
		}

		return exportConfigDataByXml(fileName, doc);
	}

	@SuppressWarnings("finally")
	@Override
	public void generatorDoc(InputStream in, OutputStream out, String billType, String[] billIds) {
		try {
			IBean bean = PfMetadataTools.queryMetaOfBilltype(billType);
			Object bill = MDPersistenceService.lookupPersistenceQueryService()
					.queryBillOfNCObjectByPK(Class.forName(bean.getFullClassName()), billIds[0]).getContainmentObject();
			
			String dataxml = null;
			
			// 创建合并引擎功能类
			AbstractDocumentMerger merger = new DocumentMerger();
			// 根据路径加载模板，同时生成单体模板模型
			ITemplateEntity te = merger.loadTemplate(in);
			te.setOutputStream(out);

			if (OBABaseUtil.getInstance().getBean(bill) != null) {// 实体
				if (bill instanceof AbstractBill) {// 主子
					dataxml = CreateDataByBillVOUtil.createResult(te, (AbstractBill) bill);
				}
			}
			te.getDataRegistry().registByXmlData(OBABaseUtil.getInstance().getBean(bill).getName(), dataxml);																													
			merger.merging(te);			
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		} finally {
			//抛异常的时候下载一个空文件
			return;
		}
	}

	/**
	 * 根据全路径，获取主页签和子页签对应的Key
	 * 
	 * @param paths
	 * @param fromIndex
	 * @return
	 */
	private String getTreeNodeKey(String[] paths, int fromIndex) {
		if (paths != null && paths.length == 1) {
			return paths[0];
		}

		StringBuffer sb = new StringBuffer();
		for (int i = fromIndex; i < paths.length; i++) {
			sb.append(paths[i]);
			sb.append(".");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private File exportConfigDataByXml(String fileName, Document doc) {
		try {
			File file = new File(fileName);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			FileOutputStream out = new FileOutputStream(file);
			StreamResult xmlResult = new StreamResult(out);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new DOMSource(doc), xmlResult);
			out.flush();
			out.close();

			return file;
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return null;
	}

	private void tabNodeBuilding(Document doc, Element country, Map<String, String> tabcodeMap,
			Map<String, Map<String, String>> allnodeMap) {
		for (String tabcode : tabcodeMap.keySet()) {
			Map<String, String> tabMap = allnodeMap.get(tabcode);
			if (tabMap == null || tabMap.size() == 0) {
				continue;
			}
			Element tabEle = doc.createElement(tabcode);
			tabEle.setAttribute(OBAStaticInfo.UAPDISPLAYNAME, tabcodeMap.get(tabcode));
			country.appendChild(tabEle);
			for (Map.Entry<String, String> entry : tabMap.entrySet()) {
				Element mdEle = doc.createElement(entry.getKey());
				mdEle.setAttribute(OBAStaticInfo.UAPDISPLAYNAME, entry.getValue());
				tabEle.appendChild(mdEle);
			}
		}
	}

	private void mainNodeBuilding(MDNode rootNode, Document doc, Element country, Map<String, String> mainnodeList) {
		Element mainEle = doc.createElement(rootNode.getRelatedBean().getName());
		mainEle.setAttribute(OBAStaticInfo.UAPDISPLAYNAME, rootNode.getRelatedBean().getDisplayName());
		country.appendChild(mainEle);
		for (Map.Entry<String, String> entry : mainnodeList.entrySet()) {
			Element attrEle = doc.createElement(entry.getKey());
			attrEle.setAttribute(OBAStaticInfo.UAPDISPLAYNAME, entry.getValue());
			mainEle.appendChild(attrEle);
		}
	}

	/**
	 * 判断是否是页签属性
	 * 
	 * @param treeNode
	 * @return
	 */
	private boolean isTabTreeNode(TreeNode OBATreeNode) {
		if (OBATreeNode == null || OBATreeNode.getNode().getAttribute() == null) {
			return false;
		}
		if (OBATreeNode.getNode().getAttribute().getAccessStrategy().toString()
				.indexOf(OBAStaticInfo.BODYOFAGGVOACCESSOR) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 通过指定refName获取对应的OBATreeNode
	 * 
	 * @param childs
	 * @param name
	 * @return
	 */
	private TreeNode findOBATreeNodeByName(List<TreeNode> childs, String name) {
		for (TreeNode treeNode : childs) {
			if (treeNode.getNode().getAttribute().getName().equals(name)) {
				return treeNode;
			}
		}
		return null;
	}

}
