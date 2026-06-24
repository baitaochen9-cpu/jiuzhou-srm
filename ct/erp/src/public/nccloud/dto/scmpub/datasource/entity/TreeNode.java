package nccloud.dto.scmpub.datasource.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.md.model.context.MDNode;

/**
 * @description 
 * @author guozhq
 * @date 2019-3-1 下午1:57:04
 * @version ncc1.0
 */
public class TreeNode implements Serializable{
	
	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = -277000647391703653L;

	private MDNode node;
	
	private Boolean isLeaf = false;
	
	private Boolean isRoot;
	
  public TreeNode(MDNode node) {
  	this.node = node;
  }
  
  public List<TreeNode> getChildNodes(){
  	List<TreeNode> childTreeNodes = new ArrayList<TreeNode>();
  	List<MDNode> mdnodes = this.node.getChildNodes();
  	for(MDNode node : mdnodes){
      // 如果是计算属性，就不显示。否则后续取数据时会有问题
      if (node.getAttribute().isCalculation()) {
        continue;
      }
      TreeNode treeNode = new TreeNode(node);
      childTreeNodes.add(treeNode);
      if(node.getRelatedBean() == null){
      	treeNode.setIsLeaf(true);
      }else{
      	treeNode.setIsLeaf(false);
      }
  	}
  	return childTreeNodes;
  }

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public MDNode getNode() {
		return node;
	}

	public void setNode(MDNode node) {
		this.node = node;
	}

	public Boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}
	
}
