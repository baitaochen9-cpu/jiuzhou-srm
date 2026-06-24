package nc.ui.md.signog.treetotree;

import java.awt.Component;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.md.innerservice.IMetaDataManagerService;
import nc.md.model.MetaDataException;
import nc.md.model.type.IType;
import nc.md.util.MDUtil;
import nc.md.vo.MDFilterConfigEntityVO;
import nc.ui.md.tree.entityop.MDElementTreeNode;
import nc.ui.md.tree.entityop.MDEntityTreeNodeInfo;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UITree;
import nc.vo.jcom.lang.StringUtil;

public class MDSignLogTree extends UITree {

	SignLogTreeTypeEnum treeType = null;
	private String domainCode = null;
	// 用来初始化 当前树所有 主实体id -> 属性path
	Map<String, List<String>> initAttrMap = new HashMap<String, List<String>>();
	// 用来初始化 当前树所有 实体id（包含主子） -> 业务活动name
	Map<String, List<String>> initBusiOPMap = new HashMap<String, List<String>>();
	// 当前树所有 主实体id -> 属性path
	Map<String, List<String>> alwaysShowAttrMap = new HashMap<String, List<String>>();
	// 当前树所有 实体id（包含主子） -> 业务活动name
	Map<String, List<String>> alwaysShowBusiOPMap = new HashMap<String, List<String>>();

	public static final String ATTR_EMPTY_NODE_NAME = "ATTR_EMPTY_NODE_NAME";
	public static final String BUSIOP_EMPTY_NODE_NAME = "BUSIOP_EMPTY_NODE_NAME";

	public MDSignLogTree(SignLogTreeTypeEnum type,
			List<MDEntityTreeNodeInfo> pathList,
			List<MDEntityTreeNodeInfo> alwaysShowList) {
		this.treeType = type;
		if (pathList != null && pathList.size() > 0) {
			initSelectedData(initAttrMap, initBusiOPMap, pathList);
		}
		if (alwaysShowList != null && alwaysShowList.size() > 0) {
			initSelectedData(initAttrMap, initBusiOPMap, alwaysShowList);
			initSelectedData(alwaysShowAttrMap, alwaysShowBusiOPMap,
					alwaysShowList);
		}
		init();
	}

	public MDSignLogTree(SignLogTreeTypeEnum type,
			List<MDEntityTreeNodeInfo> pathList,
			List<MDEntityTreeNodeInfo> alwaysShowList,String domainCode) {
		this.treeType = type;
		this.domainCode = domainCode;
		if (pathList != null && pathList.size() > 0) {
			initSelectedData(initAttrMap, initBusiOPMap, pathList);
		}
		if (alwaysShowList != null && alwaysShowList.size() > 0) {
			initSelectedData(initAttrMap, initBusiOPMap, alwaysShowList);
			initSelectedData(alwaysShowAttrMap, alwaysShowBusiOPMap,
					alwaysShowList);
		}
		init();
	}

	private void initSelectedData(Map<String, List<String>> initAttrMap,
			Map<String, List<String>> initBusiOPMap,
			List<MDEntityTreeNodeInfo> pathList) {

		if (pathList != null && pathList.size() > 0) {
			for (MDEntityTreeNodeInfo nodeInfo : pathList) {
				String beanID = nodeInfo.getBeanID();
				List<String> attrPathList = nodeInfo.getAttrPathList();
				List<String> busiOPPathList = nodeInfo.getBusiOPPathList();
				// attr
				if (attrPathList != null && attrPathList.size() > 0) {
					if (!initAttrMap.containsKey(beanID)) {
						initAttrMap.put(beanID, new ArrayList<String>());
					}
					initAttrMap.get(beanID).addAll(attrPathList);
				}
				// busiop
				if (busiOPPathList != null && busiOPPathList.size() > 0) {
					if (!initBusiOPMap.containsKey(beanID)) {
						initBusiOPMap.put(beanID, new ArrayList<String>());
					}
					initBusiOPMap.get(beanID).addAll(busiOPPathList);
				}

			}
		}
	}

	private void init() {
		// 加载实体,属性懒加载
		initData();
		// 点击节点打开子节点内容
		addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
			/**
			 * 连同子实体一起加载
			 */
			public void treeWillExpand(TreeExpansionEvent event)
					throws ExpandVetoException {
				MDElementTreeNode entityTreeNode = (MDElementTreeNode) event
						.getPath().getLastPathComponent();
				if (entityTreeNode.hasLoad() || !entityTreeNode.isEntity()) {
					return;
				}
				// 只展开 实体节点
				expandEntityTreeNode(entityTreeNode);
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event)
					throws ExpandVetoException {
				return;
			}
		});
		setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean sel, boolean expanded, boolean leaf,
					int row, boolean hasFocus) {
				Component comp = super.getTreeCellRendererComponent(tree,
						value, sel, expanded, leaf, row, hasFocus);
				MDElementTreeNode curNode = (MDElementTreeNode) value;
				if (curNode.isShowAlways()) {
					comp.setEnabled(false);// 必选项置灰
				}
				return comp;
			}
		});
		updateUI();
	}

	/**
	 * 初始化树时，加载到实体节点，考虑过滤
	 */
	private void initData() {
		MDElementTreeNode rootNode = new MDElementTreeNode(
				MDElementTreeNode.TYPE_ROOT, null, null, "", null, null);
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode, true);
		setModel(treeModel);
		Map<String, MDElementTreeNode> moduleMap = new HashMap<String, MDElementTreeNode>();
		// 排序
		Map<String, List<MDElementTreeNode>> moduleListMap = new HashMap<String, List<MDElementTreeNode>>();
		// [module/id/name/displayName]
		List<MDFilterConfigEntityVO> entityList = null;
		try {
			entityList = NCLocator.getInstance()
					.lookup(IMetaDataManagerService.class)
					.getAllMainEntityInfoWithOP(domainCode);
		} catch (MetaDataException e) {
			Logger.error("查询元数据 业务操作实体树 错误", e);
			return;
		}
		for (MDFilterConfigEntityVO entityInfo : entityList) {
			String moduleName = entityInfo.getOwnModule();
			String entityID = entityInfo.getEntityID();
			String entityName = entityInfo.getEntityCode();
			if (!(treeType == SignLogTreeTypeEnum.FULL_TREE)
					&& !initAttrMap.containsKey(entityID)
					&& !initBusiOPMap.containsKey(entityID)) {
				continue;
			}
			String resModule = entityInfo.getCompResModule();
			if (resModule == null || "".equals(resModule)) {
				Logger.error("该节点的resModule为空");
			}
			if (!moduleMap.containsKey(moduleName)) {
				String dispName = entityInfo.getOwnModuleDispName();
				if (null == dispName || dispName.trim().isEmpty()) {
					dispName = MDUtil.translateDisplayName(
							entityInfo.getDev_moduleresmodule(),
							entityInfo.getDev_moduleresid(),
							entityInfo.getDev_moduledispname());
				}
				MDElementTreeNode moduleNode = new MDElementTreeNode(
						MDElementTreeNode.TYPE_MODULE, moduleName, moduleName,
						dispName);
				moduleMap.put(moduleName, moduleNode);
				rootNode.add(moduleNode);
			}
			if (!moduleListMap.containsKey(moduleName)) {
				moduleListMap.put(moduleName,
						new ArrayList<MDElementTreeNode>());
			}
			MDElementTreeNode curEntityNode = new MDElementTreeNode(
					MDElementTreeNode.TYPE_ENTITY, entityID, entityName,
					entityInfo.getEntityDispName(), resModule,
					entityInfo.getEntityResID());
			moduleListMap.get(moduleName).add(curEntityNode);
		}

		for (String moduleName : moduleListMap.keySet()) {
			List<MDElementTreeNode> entitySortList = moduleListMap
					.get(moduleName);
			Collections.sort(entitySortList,
					new Comparator<MDElementTreeNode>() {
						public int compare(MDElementTreeNode o1,
								MDElementTreeNode o2) {
							return Collator.getInstance(Locale.CHINA).compare(
									o1.toString(), o2.toString());
						}
					});
			for (MDElementTreeNode curEntityNode : entitySortList) {
				moduleMap.get(moduleName).add(curEntityNode);
			}
		}

	}

	public void expandEntityTreeNode(MDElementTreeNode entityTreeNode) {
		if (entityTreeNode.hasLoad() || !entityTreeNode.isEntity()) {
			return;
		}
		String entityID = entityTreeNode.getId();
		String resModule = entityTreeNode.getResModule();
		if (resModule == null || "".equals(resModule)) {
			Logger.error("该节点的resModule为空");
		}
		// try {
		// IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(entityID);
		// bean.getAttributes();
		// } catch (MetaDataException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// 加载
		List<Object> result = null;
		try {
			result = NCLocator.getInstance()
					.lookup(IMetaDataManagerService.class)
					.getAllMainEntityInfoWithOPByEntityID(entityID);
		} catch (MetaDataException e) {
			Logger.error("查询元数据 业务操作实体树子节点错误", e);
			return;
		}
		// [id/name/displayName]
		List<Object[]> attrResult = (List<Object[]>) result.get(0);
		List<Object[]> busiOPResult = (List<Object[]>) result.get(1);
		List<String> filterPathList = (List<String>) result.get(2);
		/**
		 * 以下方便查看问题
		 */
		if (attrResult == null || attrResult.isEmpty()) {
			Logger.error("属性为空");
		}
		if (busiOPResult == null || busiOPResult.isEmpty()) {
			Logger.error("业务操作为空");
		}
		if (filterPathList == null || filterPathList.isEmpty()) {
			Logger.error("适配路径为空");
		}
		Set<String> filterPathSet = new HashSet<String>();// 离散过滤配置节点 配置的信息
		if (filterPathList != null && filterPathList.size() > 0) {
			for (String path : filterPathList) {
				if (!StringUtil.isEmptyWithTrim(path) && path.contains(".")) {
					filterPathSet.add(path.substring(path.indexOf(".") + 1));
				}
			}
		}

		// filterPathSet.addAll((List<String>) filterPathList);

		MDElementTreeNode attrEmptyNode = new MDElementTreeNode(
				MDElementTreeNode.TYPE_EMPTY, null, ATTR_EMPTY_NODE_NAME, "<"
						+ NCLangRes.getInstance().getStrByID("ncmdui",
								"uapncmdui-000023")/* 属性 */+ ">", null, null);
		MDElementTreeNode busiOPEmptyNode = new MDElementTreeNode(
				MDElementTreeNode.TYPE_EMPTY, null, BUSIOP_EMPTY_NODE_NAME, "<"
						+ NCLangRes.getInstance().getStrByID("ncmdui",
								"uapncmdui-000024")/* 操作 */+ ">", null, null);
		entityTreeNode.add(attrEmptyNode);
		entityTreeNode.add(busiOPEmptyNode);
		// 1）加载属性
		for (Object[] attr : attrResult) {
			String id = (String) attr[0];
			String name = (String) attr[1];
			String displayName = (String) attr[2];
			String resid = (String) attr[3];
			if (resid == null || "".equals(resid.trim())) {
				Logger.error("多语资源id为空");
			}
			String datatype = (String) attr[4];
			Integer datatypestyle = (Integer) attr[5];

			MDElementTreeNode curAttrNode = null;
			if (IType.STYLE_ARRAY == datatypestyle) {// 暂时认为是子实体
				boolean needExpand = false;
				List<String> toLoadSubPath = null;
				if (treeType == SignLogTreeTypeEnum.FULL_TREE) {
					needExpand = true;
				} else {// 非全树，需要过滤
					toLoadSubPath = new ArrayList<String>();
					List<String> attrPathList = initAttrMap.get(entityID);
					if (attrPathList != null && attrPathList.size() > 0) {
						for (String attrPath : attrPathList) {
							if (attrPath.startsWith(name + ".")) {// 包含子实体的路径
								toLoadSubPath.add(attrPath);
							}
						}
					}
					if (toLoadSubPath.size() > 0) {// 有子实体路径，需要加载子实体节点并展开
						needExpand = true;
					}
				}
				if (needExpand) {
					String temp = entityTreeNode.getResModule();
					if (temp == null || "".equals(temp)) {
						Logger.error("该属性的resmodule为空");
					}
					curAttrNode = new MDElementTreeNode(
							MDElementTreeNode.TYPE_SUB_ATTR, id, name,
							displayName, entityTreeNode.getResModule(), resid);
					curAttrNode.setDatatype(datatype);
					curAttrNode.setFullPath(name);
					entityTreeNode.getChildByName(ATTR_EMPTY_NODE_NAME).add(
							curAttrNode);
					extendsSubEntity(entityID, entityTreeNode.getResModule(),
							curAttrNode, toLoadSubPath, filterPathSet);
				}
			} else {
				if (!(treeType == SignLogTreeTypeEnum.FULL_TREE)) {// 非全树，需要过滤
					if (!initAttrMap.containsKey(entityID)
							|| !initAttrMap.get(entityID).contains(name)) {
						continue;
					}
				}
				if (filterPathSet.size() > 0 && !filterPathSet.contains(name)) {// 离散过滤
					continue;
				}
				curAttrNode = new MDElementTreeNode(
						MDElementTreeNode.TYPE_ATTR, id, name, displayName,
						entityTreeNode.getResModule(), resid);
				curAttrNode.setFullPath(name);
				curAttrNode.setDatatype(datatype);
				curAttrNode.setAllowsChildren(false);
				entityTreeNode.getChildByName(ATTR_EMPTY_NODE_NAME).add(
						curAttrNode);
				if (alwaysShowAttrMap.containsKey(entityID)
						&& alwaysShowAttrMap.get(entityID).contains(name)) {
					// 必选项
					curAttrNode.setShowAlways(true);
				}
			}
		}
		// 2）主实体加载操作
		loadBusiOpNode(entityTreeNode, busiOPResult);
		entityTreeNode.setHasLoad(true);
	}

	/**
	 * 加载子实体 toLoadSubPath 为null或size==0 则不过滤，加载全部属性
	 * 
	 * @param toLoadPath全路径
	 */
	private void extendsSubEntity(String rootEntityID, String resModule,
			MDElementTreeNode curNode, List<String> toLoadPath,
			Set<String> filterPathSet) {
		String datatypeID = curNode.getDatatype();
		List<Object> result = null;
		try {
			result = NCLocator.getInstance()
					.lookup(IMetaDataManagerService.class)
					.getAllMainEntityInfoWithOPByEntityID(datatypeID);
		} catch (MetaDataException e) {
			Logger.error("查询元数据 业务操作实体树 子节点错误", e);
			return;
		}
		// [id/name/displayName]
		List<Object[]> attrResult = (List<Object[]>) result.get(0);
		for (Object[] attr : attrResult) {
			String id = (String) attr[0];
			String name = (String) attr[1];
			String displayName = (String) attr[2];
			String resid = (String) attr[3];
			String datatype = (String) attr[4];
			Integer datatypestyle = (Integer) attr[5];
			String fullPath = curNode.getFullPath() + "." + name;
			if (filterPathSet.size() > 0 && !filterPathSet.contains(fullPath)) {
				continue;
			}
			if (IType.STYLE_ARRAY == datatypestyle) {// 暂时认为是子实体
				boolean needExpand = false;
				List<String> toLoadSubPath = null;
				if (treeType == SignLogTreeTypeEnum.FULL_TREE) {
					needExpand = true;
				} else {// 非全树，需要过滤
					toLoadSubPath = new ArrayList<String>();
					List<String> attrPathList = initAttrMap.get(rootEntityID);
					for (String attrPath : attrPathList) {
						if (attrPath.startsWith(curNode.getFullPath() + ".")) {// 包含子实体的路径
							toLoadSubPath.add(attrPath);
						}
					}
					if (toLoadSubPath.size() > 0) {// 有子实体路径，需要加载子实体节点并展开
						needExpand = true;
					}
				}
				if (needExpand) {
					MDElementTreeNode curAttrNode = new MDElementTreeNode(
							MDElementTreeNode.TYPE_SUB_ATTR, id, name,
							displayName, resModule, resid);
					curAttrNode.setDatatype(datatype);
					curAttrNode.setFullPath(curNode.getFullPath() + "." + name);
					curNode.add(curAttrNode);
					extendsSubEntity(rootEntityID, resModule, curAttrNode,
							toLoadSubPath, filterPathSet);
				}
				// List<String> toLoadSubPath = null;
			} else {// 普通属性
				if (toLoadPath == null || toLoadPath.size() == 0
						|| toLoadPath.contains(fullPath)) {// 添加属性
					MDElementTreeNode curAttrNode = new MDElementTreeNode(
							MDElementTreeNode.TYPE_ATTR, id, name, displayName,
							resModule, resid);
					curAttrNode.setFullPath(fullPath);
					curAttrNode.setDatatype(datatype);
					curAttrNode.setAllowsChildren(false);
					curNode.add(curAttrNode);
					if (alwaysShowAttrMap.containsKey(rootEntityID)
							&& alwaysShowAttrMap.get(rootEntityID).contains(
									fullPath)) {
						// 必选项
						curAttrNode.setShowAlways(true);
					}
				}
			}
		}
	}

	/**
	 * @deprecated 实体节点 懒加载 属性+busiOP
	 * 
	 * @param entityTreeNode
	 */
	public void expandTreeNode(MDElementTreeNode entityTreeNode) {
		if (entityTreeNode.hasLoad()
				|| (!entityTreeNode.isEntity() && !entityTreeNode.isSubAttr())) {
			return;
		}
		String entityID = entityTreeNode.isEntity() ? entityTreeNode.getId()
				: entityTreeNode.getDatatype();
		MDElementTreeNode rootEntityTreeNode = entityTreeNode;// 主实体
		// 获得 本节点的全路径 和 子实体
		String fullPath = "";
		if (!rootEntityTreeNode.isEntity()) {// 子实体
			fullPath = rootEntityTreeNode.getName();
			while (!((MDElementTreeNode) rootEntityTreeNode.getParent())
					.isEntity()) {
				rootEntityTreeNode = (MDElementTreeNode) rootEntityTreeNode
						.getParent();
				if (!rootEntityTreeNode.isEmpty()) {
					fullPath = rootEntityTreeNode.getName() + "." + fullPath;
				}
			}
			rootEntityTreeNode = (MDElementTreeNode) rootEntityTreeNode
					.getParent();
		}

		// 加载
		List<Object> result = null;
		try {
			result = NCLocator.getInstance()
					.lookup(IMetaDataManagerService.class)
					.getAllMainEntityInfoWithOPByEntityID(entityID);
		} catch (MetaDataException e) {
			Logger.error("查询元数据 业务操作实体树 子节点错误", e);
			return;
		}
		// [id/name/displayName]
		List<Object[]> attrResult = (List<Object[]>) result.get(0);
		List<Object[]> busiOPResult = (List<Object[]>) result.get(1);

		if (entityTreeNode.isEntity()) {
			MDElementTreeNode attrEmptyNode = new MDElementTreeNode(
					MDElementTreeNode.TYPE_EMPTY, null, ATTR_EMPTY_NODE_NAME,
					"<attribute>", null, null);
			MDElementTreeNode busiOPEmptyNode = new MDElementTreeNode(
					MDElementTreeNode.TYPE_EMPTY, null, BUSIOP_EMPTY_NODE_NAME,
					"<busiOperation>", null, null);
			entityTreeNode.add(attrEmptyNode);
			entityTreeNode.add(busiOPEmptyNode);
		}

		// 加载属性 initAttrMap
		for (Object[] attr : attrResult) {
			String id = (String) attr[0];
			String name = (String) attr[1];
			String displayName = (String) attr[2];
			String resid = (String) attr[3];
			String datatype = (String) attr[4];
			Integer datatypestyle = (Integer) attr[5];

			MDElementTreeNode curAttrNode = null;
			if (IType.STYLE_ARRAY == datatypestyle) {// 暂时认为是子实体
				curAttrNode = new MDElementTreeNode(
						MDElementTreeNode.TYPE_SUB_ATTR, id, name, displayName,
						rootEntityTreeNode.getResModule(), resid);
				curAttrNode.setDatatype(datatype);
			} else {
				String attrFullPath = StringUtil.isEmptyWithTrim(fullPath) ? name
						: fullPath + "." + name;
				if (!(treeType == SignLogTreeTypeEnum.FULL_TREE)) {// 非全树，需要过滤
					if (!initAttrMap.containsKey(rootEntityTreeNode.getId())
							|| !initAttrMap.get(rootEntityTreeNode.getId())
									.contains(attrFullPath)) {
						continue;
					}
				}
				curAttrNode = new MDElementTreeNode(
						MDElementTreeNode.TYPE_ATTR, id, name, displayName,
						rootEntityTreeNode.getResModule(), resid);
				curAttrNode.setFullPath(attrFullPath);
				curAttrNode.setDatatype(datatype);
				curAttrNode.setAllowsChildren(false);
			}
			if (entityTreeNode.isEntity()) {
				entityTreeNode.getChildByName(ATTR_EMPTY_NODE_NAME).add(
						curAttrNode);
			} else {
				entityTreeNode.add(curAttrNode);
			}

			if (curAttrNode.isSubAttr()) {
				expandTreeNode(curAttrNode);
			}
		}

		// 主实体加载操作
		if (entityTreeNode.isEntity()) {// 子实体不加载操作
			loadBusiOpNode(entityTreeNode, busiOPResult);
		}
		entityTreeNode.setHasLoad(true);
	}

	private void loadBusiOpNode(MDElementTreeNode entityTreeNode,
			List<Object[]> busiOPResult) {
		String entityID = entityTreeNode.getId();
		for (Object[] busiOP : busiOPResult) {
			String id = (String) busiOP[0];
			String name = (String) busiOP[1];
			String displayName = (String) busiOP[2];
			String resid = (String) busiOP[3];
			String resModule = (String) busiOP[4];
			if (resModule == null || "".equals(resModule.trim())) {
				Logger.error("resModule is null");
			}
			if (!(treeType == SignLogTreeTypeEnum.FULL_TREE)) {// 非全树，需要过滤
				if (!initBusiOPMap.containsKey(entityID)
						|| !initBusiOPMap.get(entityID).contains(name)) {
					continue;
				}
			}
			Logger.error("resModule:" + resModule + "     resid:" + resid);
			MDElementTreeNode curBusiOPNode = new MDElementTreeNode(
					MDElementTreeNode.TYPE_BUSIOP, id, name, displayName,
					resModule, resid);
			curBusiOPNode.setFullPath(name);
			curBusiOPNode.setAllowsChildren(false);
			if (alwaysShowBusiOPMap.containsKey(entityID)
					&& alwaysShowBusiOPMap.get(entityID).contains(name)) {
				// 必选项
				curBusiOPNode.setShowAlways(true);
			}
			entityTreeNode.getChildByName(BUSIOP_EMPTY_NODE_NAME).add(
					curBusiOPNode);
		}
	}

	/**
	 * 获得当前所有 树节点的信息
	 * 
	 * @return
	 */
	public List<MDEntityTreeNodeInfo> getcurMDEntityTreeNodeInfo(TreeModel model) {
		List<MDEntityTreeNodeInfo> resultList = new ArrayList<MDEntityTreeNodeInfo>();
		MDElementTreeNode root = (MDElementTreeNode) model.getRoot();
		int amoduleCount = root.getChildCount();
		for (int i = 0; i < amoduleCount; i++) {
			MDElementTreeNode moduleNode = (MDElementTreeNode) root
					.getChildAt(i);
			int entityCount = moduleNode.getChildCount();
			for (int j = 0; j < entityCount; j++) {
				MDElementTreeNode classNode = (MDElementTreeNode) moduleNode
						.getChildAt(j);
				expandEntityTreeNode(classNode);// 展开实体节点
				// MDEntityTreeNodeInfo beanInfo = new MDEntityTreeNodeInfo();
				// beanInfo.setBeanID(classNode.getId());
				// List<String> busiOPPathList = new ArrayList<String>();
				// List<String> busiOPIDList = new ArrayList<String>();
				// List<String> attrPathList = new ArrayList<String>();
				// beanInfo.setBusiOPPathList(busiOPPathList);
				// beanInfo.setAttrPathList(attrPathList);
				// beanInfo.setBusiOPIDList(busiOPIDList);
				// resultList.add(beanInfo);
				// // attr
				// MDElementTreeNode attrEmptyNode = classNode
				// .getChildByName(ATTR_EMPTY_NODE_NAME);
				// int attrNum = attrEmptyNode.getChildCount();
				// for (int m = 0; m < attrNum; m++) {
				// MDElementTreeNode attrNode = (MDElementTreeNode)
				// attrEmptyNode
				// .getChildAt(m);
				// addAttrPath(attrNode, "", attrPathList);
				// }
				//
				// // busiop
				// MDElementTreeNode busiOPEmptyNode = classNode
				// .getChildByName(BUSIOP_EMPTY_NODE_NAME);
				// if (busiOPEmptyNode != null) {
				// int busiopNum = busiOPEmptyNode.getChildCount();
				// if (busiopNum > 0) {
				// for (int k = 0; k < busiopNum; k++) {
				// MDElementTreeNode busiOPNode = (MDElementTreeNode)
				// busiOPEmptyNode
				// .getChildAt(k);
				// busiOPPathList.add(busiOPNode.getName());
				// busiOPIDList.add(busiOPNode.getId());
				// }
				// }
				// }
				resultList.add(createMDEntityTreeNodeInfo(classNode));
			}
		}
		return resultList;
	}

	/**
	 * 根据MDElementTreeNode类生成MDEntityTreeNodeInfo
	 * 
	 * @param classNode
	 * @return
	 */
	public MDEntityTreeNodeInfo createMDEntityTreeNodeInfo(
			MDElementTreeNode classNode) {
		expandEntityTreeNode(classNode);// 展开实体节点
		MDEntityTreeNodeInfo beanInfo = new MDEntityTreeNodeInfo();
		beanInfo.setBeanID(classNode.getId());
		List<String> busiOPPathList = new ArrayList<String>();
		List<String> busiOPIDList = new ArrayList<String>();
		List<String> attrPathList = new ArrayList<String>();
		beanInfo.setBusiOPPathList(busiOPPathList);
		beanInfo.setAttrPathList(attrPathList);
		beanInfo.setBusiOPIDList(busiOPIDList);
		// attr
		MDElementTreeNode attrEmptyNode = classNode
				.getChildByName(ATTR_EMPTY_NODE_NAME);
		int attrNum = attrEmptyNode.getChildCount();
		for (int m = 0; m < attrNum; m++) {
			MDElementTreeNode attrNode = (MDElementTreeNode) attrEmptyNode
					.getChildAt(m);
			addAttrPath(attrNode, "", attrPathList);
		}

		// busiop
		MDElementTreeNode busiOPEmptyNode = classNode
				.getChildByName(BUSIOP_EMPTY_NODE_NAME);
		if (busiOPEmptyNode != null) {
			int busiopNum = busiOPEmptyNode.getChildCount();
			if (busiopNum > 0) {
				for (int k = 0; k < busiopNum; k++) {
					MDElementTreeNode busiOPNode = (MDElementTreeNode) busiOPEmptyNode
							.getChildAt(k);
					busiOPPathList.add(busiOPNode.getName());
					busiOPIDList.add(busiOPNode.getId());
				}
			}
		}
		return beanInfo;
	}

	private void addAttrPath(MDElementTreeNode attrNode, String parentPath,
			List<String> attrPathList) {
		String curPath = StringUtil.isEmptyWithTrim(parentPath) ? attrNode
				.getName() : parentPath + "." + attrNode.getName();
		if (attrNode.isAttr()) {
			attrPathList.add(curPath);
		} else if (attrNode.isSubAttr()) {
			int childAttrCount = attrNode.getChildCount();
			for (int i = 0; i < childAttrCount; i++) {
				addAttrPath((MDElementTreeNode) attrNode.getChildAt(i),
						curPath, attrPathList);
			}
		}
	}
}
