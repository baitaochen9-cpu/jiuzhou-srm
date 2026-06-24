package nc.ui.qc.supplierqualitystatus.view;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.pub.beans.tree.IFilterByText;
import nc.vo.bd.supplier.supplierclass.SupplierclassVO;

public class SupplierRegFilterByText implements IFilterByText {

	@Override
	public DefaultMutableTreeNode cloneMatchedNode(
			DefaultMutableTreeNode matchedNode) {
		return new DefaultMutableTreeNode(matchedNode.getUserObject());
	}

	@Override
	public boolean accept(DefaultMutableTreeNode node, String filterText) {
		if (node == null || node.getUserObject() == null)
			return false;

		Object userObj = node.getUserObject();

		String code = "";
		String name = "";
		// IBDObject bdobject = new
		// BDObjectAdpaterFactory().createBDObject(userObj);
		// if (bdobject != null) {
		// code = null2Empty(bdobject.getCode());
		// name = null2Empty(bdobject.getName());
		//
		// //简体中文语种直接判断name字段，其它语种需要根据resid查询对应语种多语资源再做判断
		// if
		// (Language.SIMPLE_CHINESE_CODE.equals(WorkbenchEnvironment.getInstance().getCurrLanguage().getCode()))
		// return (code.indexOf(filterText) != -1 || name.indexOf(filterText) !=
		// -1);
		// }

		if (userObj instanceof SupplierclassVO) {
			SupplierclassVO funcvo = (SupplierclassVO) userObj;
			code = funcvo.getCode();
			name = funcvo.getName();

		}

		return (null2Empty(code).indexOf(filterText) != -1 || null2Empty(name)
				.indexOf(filterText) != -1);
	}

	private String null2Empty(Object o) {
		return o == null ? "" : o + "";
	}

}