package nccloud.dto.scmpub.datasource.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nc.pub.oba.OBABaseUtil;
import nc.uap.oba.word.merger.ITemplateEntity;
import nc.uap.oba.word.merger.model.node.DataNode;
import nc.uap.oba.word.merger.model.node.NodeList;
import nc.uap.oba.word.merger.util.DocumentHelper;
import nc.vo.oba.OBAMDQueryVO;
import nc.vo.oba.OBAStaticServer;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.scmpub.util.ValueCheckUtil;

/**
 * @description
 * @author xiahui
 * @date 创建时间：2019-3-5 上午11:29:30
 * @version ncc1.0
 **/
public class CreateDataByBillVOUtil {
	public static String createResult(ITemplateEntity te, AbstractBill billVO) throws Exception {
		Document doc = OBABaseUtil.getInstance().newDocumentBuilder();
		String beanname = OBABaseUtil.getInstance().getBean(billVO).getName();
		String beantablename = OBABaseUtil.getInstance().getBean(billVO).getTable().getName();
		Element country = doc.createElement(beantablename);
		doc.appendChild(country);

		// oba 2.0方式取nodes
		NodeList<DataNode> tabDataNodes = te.getWTModel().getDataSourceCollection().getDataSource(beantablename)
				.getChildNodes().item(0).getChildNodes();
		List<OBAMDQueryVO> qryList = CreateDataByBillVOUtil.getQryCondtionVOs(billVO, beanname, tabDataNodes);
		Map<String, Map<String, Object[]>> mdMap = OBAStaticServer.getService().createResultByBillVO(qryList);
		for (DataNode tabDataNode : tabDataNodes) {
			NodeList<DataNode> nodes = tabDataNode.getChildNodes();
			// 主页签
			if (beantablename.equals(tabDataNode.getName())) {
				CreateDataByBillVOUtil.createMainTable(billVO, doc, country, tabDataNode, nodes, mdMap);
			} else {
				CircularlyAccessibleValueObject[] childVOs = billVO.getTableVO(tabDataNode.getName());
				if (ValueCheckUtil.isNullORZeroLength(childVOs)) {
					continue;
				}
				CreateDataByBillVOUtil.createTabTable(doc, country, tabDataNode, nodes, childVOs, mdMap);
			}
		}
		return DocumentHelper.document2String(doc);
	}

	private static void createMainTable(AbstractBill billVO, Document doc, Element country, DataNode tabDataNode,
			NodeList<DataNode> nodes, Map<String, Map<String, Object[]>> mdMap) {
		Map<String, Object[]> mainvaluesMap = mdMap.get(billVO.getParent().getPrimaryKey());

		Element tabEle = doc.createElement(tabDataNode.getName());
		country.appendChild(tabEle);

		for (DataNode dataNode : nodes) {
			Element attrEle = doc.createElement(dataNode.getName());
			if (ValueCheckUtil.isNullORZeroLength(mainvaluesMap.get(dataNode.getName()))) {
				continue;
			}
			String value = mainvaluesMap.get(dataNode.getName())[0] == null ? null : mainvaluesMap.get(dataNode.getName())[0]
					.toString();
			attrEle.setTextContent(value);
			tabEle.appendChild(attrEle);
		}
	}

	private static void createTabTable(Document doc, Element country, DataNode tabDataNode, NodeList<DataNode> nodes,
			CircularlyAccessibleValueObject[] childVOs, Map<String, Map<String, Object[]>> mdMap) throws BusinessException {
		for (CircularlyAccessibleValueObject childVO : childVOs) {
			Map<String, Object[]> tabvaluesMap = mdMap.get(childVO.getPrimaryKey());
			Element tabEle = doc.createElement(tabDataNode.getName());
			country.appendChild(tabEle);
			for (DataNode dataNode : nodes) {
				Element attrEle = doc.createElement(dataNode.getName());
				if (ValueCheckUtil.isNullORZeroLength(tabvaluesMap.get(dataNode.getName()))) {
					continue;
				}
				String value = tabvaluesMap.get(dataNode.getName())[0] == null ? null : tabvaluesMap.get(dataNode.getName())[0]
						.toString();
				attrEle.setTextContent(value);
				tabEle.appendChild(attrEle);
			}
		}
	}

	private static List<OBAMDQueryVO> getQryCondtionVOs(AbstractBill billVO, String beanname, NodeList<DataNode> tabDataNodes)
			throws BusinessException {
		List<OBAMDQueryVO> qryList = new ArrayList<OBAMDQueryVO>();
		for (DataNode tabDataNode : tabDataNodes) {
			NodeList<DataNode> nodes = tabDataNode.getChildNodes();
			String[] paths = new String[nodes.getCount()];
			for (int i = 0; i < nodes.getCount(); i++) {
				paths[i] = nodes.item(i).getName();
			}
			// 主页签
			if (beanname.equals(tabDataNode.getName())) {
				OBAMDQueryVO obaVO = new OBAMDQueryVO();
				obaVO.setBean(OBABaseUtil.getInstance().getBean(billVO));
				obaVO.setIds(new String[] { billVO.getParent().getPrimaryKey() });
				obaVO.setPaths(paths);
				qryList.add(obaVO);
			} else {
				CircularlyAccessibleValueObject[] childVOs = billVO.getTableVO(tabDataNode.getName());
				if (ValueCheckUtil.isNullORZeroLength(childVOs)) {
					continue;
				}
				for (CircularlyAccessibleValueObject childVO : childVOs) {
					OBAMDQueryVO obaVO = new OBAMDQueryVO();
					obaVO.setBean(OBABaseUtil.getInstance().getBean(childVO));
					obaVO.setIds(new String[] { childVO.getPrimaryKey() });
					obaVO.setPaths(paths);
					qryList.add(obaVO);
				}
			}
		}
		return qryList;
	}
}
