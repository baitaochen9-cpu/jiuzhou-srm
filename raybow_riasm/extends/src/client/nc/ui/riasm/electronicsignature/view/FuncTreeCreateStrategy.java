package nc.ui.riasm.electronicsignature.view;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.ml.NCLangRes;
import nc.vo.bd.access.tree.AbastractTreeCreateStrategy;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jcom.tree.INodeFilter;
import nc.vo.riasm.electronicsignature.ElectronicSignatureVO;
import nc.vo.sm.funcreg.BusiActiveVO;
import nc.vo.sm.funcreg.ButtonRegVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.funcreg.ModuleVO;
import nc.vo.sm.funcreg.PageVO;


/**
 * 功能节点
 * 页签
 * 按钮 树的构造策略
 * 
 * @author lkp
 * @modify by jl on 2008年12月16日 加上按钮
 *
 */
public class FuncTreeCreateStrategy extends AbastractTreeCreateStrategy {
	
	public FuncTreeCreateStrategy()
	{
	}

	public boolean isCodeTree() {
		return false;
	}
	
	public DefaultMutableTreeNode createTreeNode(Object obj) {
		return new DefaultMutableTreeNode(obj);
	}

	public Object getNodeId(Object obj) {
		
		if(obj instanceof FuncRegisterVO)
			return ((FuncRegisterVO)obj).getCfunid();
		else if(obj instanceof PageVO)
			return ((PageVO)obj).getPk_page();
		else if(obj instanceof ButtonRegVO)
			return ((ButtonRegVO)obj).getPk_btn();
		else if(obj instanceof BusiActiveVO)
			return ((BusiActiveVO)obj).getPk_busiactive();
		else if(obj instanceof ModuleVO)
			return ((ModuleVO)obj).getModuleid();
		else if(obj instanceof ElectronicSignatureVO)
			return ((ElectronicSignatureVO)obj).getBillid();
		return null; 
	}

	public Object getParentNodeId(Object obj) {
		
		if(obj instanceof PageVO)
			return ((PageVO)obj).getParentid();
		else if(obj instanceof FuncRegisterVO)
		{
			FuncRegisterVO reg = (FuncRegisterVO)obj;
			if(StringUtil.isEmptyWithTrim(reg.getParent_id()))
				return reg.getOwn_module();
			else
				return reg.getParent_id();
		}else if(obj instanceof ButtonRegVO){
			return ((ButtonRegVO)obj).getParent_id();
		}else if(obj instanceof BusiActiveVO)
			return ((BusiActiveVO)obj).getParent_id();
		else if(obj instanceof ModuleVO) 
			return ((ModuleVO)obj).getParentcode();
		else if(obj instanceof ElectronicSignatureVO){
			ElectronicSignatureVO reg = (ElectronicSignatureVO)obj;
			if(StringUtil.isEmptyWithTrim(reg.getSrcbilltype()))
				return reg.getSrcbilltype();
			else
				return reg.getSrcbilltype();
		}
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
