package nc.ui.md.signog.treetotree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.bs.logging.Logger;
import nc.ui.md.busilog.treetotree.VerticalFlowLayout;
import nc.ui.md.tree.entityop.MDElementTreeNode;
import nc.ui.md.tree.entityop.MDEntityTreeNodeInfo;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.tree.FilterTreeModel;
import nc.ui.pub.beans.tree.IFilterByText;
import nc.ui.pub.beans.tree.TreeFilterHandler;
import nc.ui.uif2.components.FilterTreePanel;
import nc.uitheme.ui.ThemeResourceCenter;
import nc.vo.jcom.lang.StringUtil;

/**
 * 业务日志 元数据配置树 panel
 * 
 * @author dingxm
 */
public class SignLogMDTreeToTreePanel extends UIPanel {
	private static final long serialVersionUID = -3209661823467145863L;
	// 左右移动按钮
	private UIPanel movePane = null;
	private UIButton moveBtn1 = null;// >
	private UIButton moveBtn2 = null;// >>
	private UIButton moveBtn3 = null;// <
	private UIButton moveBtn4 = null;// <<
	private Color splitColor = ThemeResourceCenter.getInstance().getColor("themeres/ui/toolbaricons/uif2Control.theme.xml", "splitColor");
	// 左树
	private MDSignLogTree leftTree = null;
	// 右树
	private UIPanel rightPanel = null;
	private MDSignLogTree rightTree = null;
	UIScrollPane rightScrollPane = null;

	List<MDEntityTreeNodeInfo> selectedPathList = null;
	List<MDEntityTreeNodeInfo> alwaysShowNodeList = null;
	private String domainCode = null;
	private class MyFilterByText  implements IFilterByText {
		@Override
		public boolean accept(DefaultMutableTreeNode node, String filterText) {
			if (StringUtil.isEmptyWithTrim(filterText)) {
				return true;
			}
			String dispName = ((MDElementTreeNode) node).getDisplayName();
			if (StringUtil.isEmptyWithTrim(dispName)) {
				return true;
			}
			return ((dispName).indexOf(filterText.trim()) != -1);
		}

		@Override
		public DefaultMutableTreeNode cloneMatchedNode(
				DefaultMutableTreeNode matchedNode) {
			MDElementTreeNode node = ((MDElementTreeNode) matchedNode)
					.clone();
			return node;
		}
		
	}
	public SignLogMDTreeToTreePanel(List<MDEntityTreeNodeInfo> selectedPathList,
			List<MDEntityTreeNodeInfo> alwaysShowNodeList) {
		super();
		this.selectedPathList = selectedPathList;
		this.alwaysShowNodeList = alwaysShowNodeList;
		initUI();
	}
	public SignLogMDTreeToTreePanel(List<MDEntityTreeNodeInfo> selectedPathList,
			List<MDEntityTreeNodeInfo> alwaysShowNodeList,String code) {
		super();
		this.selectedPathList = selectedPathList;
		this.alwaysShowNodeList = alwaysShowNodeList;
		this.domainCode = code;
		initUI();
	}

	private void initUI() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constrains = new GridBagConstraints();
		constrains.fill = GridBagConstraints.BOTH;
		constrains.anchor = GridBagConstraints.CENTER;
		constrains.gridx = 0;
		constrains.gridy = 0;
		constrains.gridwidth = 1;
		constrains.gridheight = 1;
		constrains.weightx = 0.4;
		constrains.weighty = 1;
		setLayout(layout);
		FilterTreePanel functreepanel = new FilterTreePanel(
				(MDSignLogTree) getLeftTree());
		MyFilterByText myFilter = new MyFilterByText();
		functreepanel.setFilterByText(myFilter);
		//start只做界面修改,待选外框
		UIPanel leftPanel = new UIPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));		
		String leftTitle = NCLangRes.getInstance().getStrByID("_Template", "0_template0162")/* "待选" */;		
		UILabel label = new UILabel(leftTitle);
		leftPanel.add(label);
		functreepanel.setBorder(new LineBorder(splitColor));
		leftPanel.add(functreepanel);
		//end 只做界面修改
		add(leftPanel, constrains);

		constrains.weightx = 0;
		constrains.gridx = 1;
		constrains.weightx = 0;
		constrains.weighty = 1;
		constrains.fill = GridBagConstraints.NONE;
		add(getMovePane(), constrains);
		constrains.weightx = 0.4;
		constrains.weighty = 1;
		constrains.gridx = 2;
		constrains.fill = GridBagConstraints.BOTH;
		
		
		add(getRightTreePanel(), constrains);
	}

	private UIPanel getMovePane() {
		if (movePane == null) {
			movePane = new UIPanel();
			VerticalFlowLayout vf = new VerticalFlowLayout();
			vf.setAlignment(VerticalFlowLayout.MIDDLE);
			vf.setHorizontalFill(true);
			vf.setVgap(15);
			movePane.setLayout(vf);
			movePane.add(getMoveBtn1());
			movePane.add(getMoveBtn2());
			movePane.add(getMoveBtn3());
			movePane.add(getMoveBtn4());
			movePane.setPreferredSize(new Dimension(95, 200));
			movePane.setPreferredSize(new Dimension(100, 180));

			movePane.setOpaque(false);
		}
		return movePane;
	}

	private Component getRightTreePanel() {

		if (rightPanel == null) {
			rightPanel = new UIPanel();
			rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
			String rightTitle = NCLangRes.getInstance().getStrByID("_Template",
					"0_template0163")/* "已选" */;
			UILabel label = new UILabel(rightTitle);
			rightPanel.add(label);
			//修改界面(要求加个过滤框，为了好看，没意义)
			FilterTreePanel righttreefilterpan = new FilterTreePanel((MDSignLogTree)getInitRightTree());
			righttreefilterpan.setBorder(new LineBorder(splitColor));
			MyFilterByText myFilter = new MyFilterByText();
			righttreefilterpan.setFilterByText(myFilter);
			rightScrollPane = new UIScrollPane(righttreefilterpan);
			
			
			rightScrollPane.setPreferredSize(new Dimension(150, 180));
			rightPanel.add(rightScrollPane);

			rightPanel.setOpaque(false);
		}
		return rightPanel;
	}

	private Component getInitRightTree() {
		if (rightTree == null) {
			rightTree = new MDSignLogTree(SignLogTreeTypeEnum.FILTE_RTREE,
					selectedPathList, alwaysShowNodeList,domainCode);
		}
		return rightTree;
	}

	private Component getLeftTree() {
		if (leftTree == null) {
			leftTree = new MDSignLogTree(SignLogTreeTypeEnum.FULL_TREE, null,
					null,domainCode);
		}
		return leftTree;
	}

	private UIButton getMoveBtn1() {
		if (moveBtn1 == null) {
			moveBtn1 = new UIButton(">");
			moveBtn1.setSize(30, 20);
			moveBtn1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					TreePath[] paths = leftTree.getSelectionPaths();
					if (paths != null && paths.length > 0) {
						for (TreePath path : paths) {
							if (path == null || path.getPathCount() <= 1) {
								continue;
							}
							copyPathTreeToTree(path);
						}
						rightTree.updateUI();
//						updataSelectedList(rightTree,selectedPathList);
					}
				}

				private void copyPathTreeToTree(TreePath path) {
					// 左树的treePath
					Object[] pathNode = path.getPath();

					// 复制该节点到右边树
					MDElementTreeNode curRightNode = (MDElementTreeNode) rightTree
							.getModel().getRoot();
					MDElementTreeNode curLeftNode = (MDElementTreeNode) pathNode[0];
					for (int i = 1; i < pathNode.length; i++) {
						curLeftNode = (MDElementTreeNode) pathNode[i];
						if (curLeftNode.isEntity()) {
							leftTree.expandEntityTreeNode(curLeftNode);
						}

						MDElementTreeNode rightNode = curRightNode
								.getChildByName(curLeftNode.getName());
						// boolean rightIsNew = false;// 右边是否新增节点
						if (rightNode == null) {
							rightNode = (MDElementTreeNode) curLeftNode.clone();
							rightNode.setHasLoad(false);
							rightNode.removeAllChildren();
							curRightNode.add(rightNode);
						}
						if (rightNode.isEntity()) {
							rightTree.expandEntityTreeNode(rightNode);
						}
						curRightNode = rightNode;
					}
					// 复制所有子节点到右边树
					copyChildTreeToTree(curLeftNode, curRightNode);
				}

				private void copyChildTreeToTree(MDElementTreeNode curLeftNode,
						MDElementTreeNode curRightNode) {
					if (curLeftNode.isAttr()) {
						return;
					} else {
						if (curLeftNode.isEntity()) {
							leftTree.expandEntityTreeNode(curLeftNode);
							rightTree.expandEntityTreeNode(curRightNode);
						}
						int childCount = curLeftNode.getChildCount();
						if (childCount > 0) {
							for (int i = 0; i < childCount; i++) {
								MDElementTreeNode leftChildNode = (MDElementTreeNode) curLeftNode
										.getChildAt(i);
								MDElementTreeNode rightChildNode = curRightNode
										.getChildByName(leftChildNode.getName());
								if (rightChildNode == null) {
									rightChildNode = (MDElementTreeNode) leftChildNode
											.clone();
									rightChildNode.setHasLoad(false);
									rightChildNode.removeAllChildren();
									curRightNode.add(rightChildNode);
								}
								copyChildTreeToTree(leftChildNode,
										rightChildNode);
							}
						}
					}
				}
			});
		}
		return moveBtn1;
	}

	private UIButton getMoveBtn2() {
		if (moveBtn2 == null) {
			moveBtn2 = new UIButton(">>");
			moveBtn2.setSize(30, 20);
			moveBtn2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					rightTree = new MDSignLogTree(
							SignLogTreeTypeEnum.FULL_TREE, null,
							alwaysShowNodeList,domainCode);
					//界面 
					FilterTreePanel righttreefilterpan = new FilterTreePanel((MDSignLogTree)getInitRightTree());
					righttreefilterpan.setBorder(new LineBorder(splitColor));
					MyFilterByText myFilter = new MyFilterByText();
					righttreefilterpan.setFilterByText(myFilter);
					rightScrollPane.setViewportView(righttreefilterpan);
					rightTree.updateUI();
//					updataSelectedList(rightTree,selectedPathList);
					updateUI();
				}
			});
		}
		return moveBtn2;
	}

	private UIButton getMoveBtn3() {
		if (moveBtn3 == null) {
			moveBtn3 = new UIButton("<");
			moveBtn3.setSize(30, 20);
			moveBtn3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					TreePath[] paths = rightTree.getSelectionPaths();
					if (paths == null || paths.length == 0) {
						return;
					}
					boolean hasAlwaysShow = false;
					for (TreePath path : paths) {
						MDElementTreeNode node = (MDElementTreeNode) path
								.getLastPathComponent();
						if (node.isEntity()) {
							rightTree.expandEntityTreeNode(node);
						}

						List<String> toContainPath = new ArrayList<String>();
						addAlwaysShowPath(node, toContainPath, "");
						if (toContainPath.size() > 0) {
							hasAlwaysShow = true;
							if (!node.isAttr()) {// 属性节点无需处理
								MDElementTreeNode newNode = (MDElementTreeNode) node
										.clone();
								newNode.removeAllChildren();
								MDElementTreeNode curLeftNode = node;
								MDElementTreeNode curRightNode = newNode;
								for (String curPath : toContainPath) {
									String[] nodeNames = curPath.split("\\.");
									for (int i = 1; i < nodeNames.length; i++) {
										String nodeName = nodeNames[i];
										curLeftNode = curLeftNode
												.getChildByName(nodeName);
										if (curRightNode
												.getChildByName(nodeName) == null) {
											curRightNode
													.add((MDElementTreeNode) curLeftNode
															.clone());
											curRightNode = curRightNode
													.getChildByName(nodeName);
										}
									}
								}
								MDElementTreeNode parentNode = (MDElementTreeNode) node
										.getParent();
								node.removeFromParent();
								parentNode.add(newNode);
							}
						} else {
							node.removeFromTree();
						}
					}
//					updataSelectedList(rightTree,selectedPathList);
					rightTree.updateUI();
					if (hasAlwaysShow) {
						MessageDialog.showErrorDlg(
								getParent(),
								"",
								NCLangRes.getInstance().getStrByID("ncmdui",
										"uapncmdui-000025")/* 必选项无法移除 */);
					}
				}
			});
		}
		return moveBtn3;
	}

	private void addAlwaysShowPath(MDElementTreeNode node,
			List<String> toContainPath, String parentPath) {
		String fullPath = StringUtil.isEmptyWithTrim(parentPath) ? node
				.getName() : parentPath + "." + node.getName();
		if (node.isAttr() || node.isBusiOP()) {
			if (node.isShowAlways()) {
				toContainPath.add(fullPath);
			}
		} else {
			int childCount = node.getChildCount();
			if (childCount > 0) {
				for (int i = 0; i < childCount; i++) {
					addAlwaysShowPath((MDElementTreeNode) node.getChildAt(i),
							toContainPath, fullPath);
				}
			}
		}
	}

	private UIButton getMoveBtn4() {
		if (moveBtn4 == null) {
			moveBtn4 = new UIButton("<<");
			moveBtn4.setSize(30, 20);
			moveBtn4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (alwaysShowNodeList == null
							|| alwaysShowNodeList.size() == 0) {
						alwaysShowNodeList = new ArrayList<MDEntityTreeNodeInfo>();
					}
					rightTree = new MDSignLogTree(
							SignLogTreeTypeEnum.FILTE_RTREE, null,
							alwaysShowNodeList,domainCode);

					//界面修改
					FilterTreePanel righttreefilterpan = new FilterTreePanel((MDSignLogTree)getInitRightTree());
					righttreefilterpan.setBorder(new LineBorder(splitColor));
					MyFilterByText myFilter = new MyFilterByText();
					righttreefilterpan.setFilterByText(myFilter);
					rightScrollPane.setViewportView(righttreefilterpan);
					selectedPathList.clear();
					rightTree.updateUI();
					updateUI();

				}
			});
		}
		return moveBtn4;
	}

	public List<MDEntityTreeNodeInfo> getSelectedMDEntityTreeNodeInfo() {
		Component com = rightScrollPane.getViewport().getView();
		if(com instanceof FilterTreePanel){
			FilterTreePanel filterPanel = (FilterTreePanel) com;
			Class<?> filterClass = filterPanel.getClass();
			Field filterHandler = null;
			try {
				filterHandler = filterClass.getDeclaredField("filterHandler");
				filterHandler.setAccessible(true);
				Object o = filterHandler.get(filterPanel);
				TreeFilterHandler handler = null;
				if(o instanceof TreeFilterHandler&&o!=null){
					handler = (TreeFilterHandler) o;
				}else{
					return null;
				}
				FilterTreeModel model = handler.getFilterTreeModel();
				if(model!=null){
					DefaultTreeModel treeModel = model.getOriginalModel();
					if(treeModel!=null){
						return rightTree.getcurMDEntityTreeNodeInfo(treeModel);
					}
				}
			} catch (SecurityException e) {
				Logger.error(e.getMessage(),e);
			} catch (NoSuchFieldException e) {
				Logger.error(e.getMessage(),e);
			} catch (IllegalArgumentException e) {
				Logger.error(e.getMessage(),e);
			} catch (IllegalAccessException e) {
				Logger.error(e.getMessage(),e);
			}
		}
		return rightTree.getcurMDEntityTreeNodeInfo(rightTree.getModel());
	}

	public void setBtnEnabled(boolean isEnabled) {
		getMoveBtn1().setEnabled(isEnabled);
		getMoveBtn2().setEnabled(isEnabled);
		getMoveBtn3().setEnabled(isEnabled);
		getMoveBtn4().setEnabled(isEnabled);
	}

//	private void updataSelectedList(MDSignLogTree rightTree,List<MDEntityTreeNodeInfo> selectedList){
//		List<MDEntityTreeNodeInfo> resultList = new ArrayList<MDEntityTreeNodeInfo>();
//		MDElementTreeNode root = (MDElementTreeNode) rightTree.getModel().getRoot();
//		int amoduleCount = root.getChildCount();
//		for (int i = 0; i < amoduleCount; i++) {
//			MDElementTreeNode moduleNode = (MDElementTreeNode) root
//					.getChildAt(i);
//			int entityCount = moduleNode.getChildCount();
//			for (int j = 0; j < entityCount; j++) {
//				MDElementTreeNode classNode = (MDElementTreeNode) moduleNode
//						.getChildAt(j);
//				resultList.add(rightTree.createMDEntityTreeNodeInfo(classNode));
//			}
//			if(selectedList!=null){
//				selectedList.clear();
//				selectedList.addAll(resultList);
//			}else{
//				selectedList = new ArrayList<MDEntityTreeNodeInfo>();
//				selectedList.addAll(resultList);
//			}
//		}
//	}
	// /**
	// * 刷新右边树
	// */
	// public void refreshRightTree() {
	// rightScrollPane.remove(rightTree);
	// rightTree = new MDSignLogTree(SignLogTreeTypeEnum.FILTE_RTREE,
	// selectedPathList, alwaysShowNodeList);
	// rightScrollPane.setViewportView(rightTree);
	// repaint();
	// updateUI();
	// }
}
