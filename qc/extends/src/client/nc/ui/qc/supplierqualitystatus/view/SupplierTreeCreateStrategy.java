package nc.ui.qc.supplierqualitystatus.view;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.ml.NCLangRes;
import nc.vo.bd.access.tree.AbastractTreeCreateStrategy;
import nc.vo.bd.supplier.supplierclass.SupplierclassVO;
import nc.vo.jcom.tree.INodeFilter;


/**
 * 功能节点
 * 页签
 * 按钮 树的构造策略
 * 
 * @author lkp
 * @modify by jl on 2008年12月16日 加上按钮
 *
 */
public class SupplierTreeCreateStrategy extends AbastractTreeCreateStrategy {
	
	public SupplierTreeCreateStrategy()
	{
	}

	public boolean isCodeTree() {
		return false;
	}
	
	public DefaultMutableTreeNode createTreeNode(Object obj) {
		return new DefaultMutableTreeNode(obj);
	}

	public Object getNodeId(Object obj) {
		if(obj instanceof SupplierclassVO)
			return ((SupplierclassVO)obj).getPk_supplierclass();
		return null; 
	}

	public Object getParentNodeId(Object obj) {
		if(obj instanceof SupplierclassVO)
			return ((SupplierclassVO)obj).getParent_id();
		return null;
	}


	public Object getCodeValue(Object obj) {
		return null;
	}


	public String getCodeRule() {
		return null;
	}


	public DefaultMutableTreeNode getRootNode() {
		return new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID("nctools", "FuncTreeCreateStrategy-000000")/*功能节点*/);
	}

	public String getCircularRule() {
		return null;
	}

	public DefaultMutableTreeNode createDefaultTreeNodeForLoseNode(Object codeValue) {
		return null;
	}

	public DefaultMutableTreeNode getOtherTreeNode() {
		return null;
	}
	
	public int getInsertType() {
		return WHEN_LOSE_PARENT_INSERT_TO_ROOT;
	}

	public INodeFilter getNodeFileter() {
		return null;
	}
	
}
