package nc.ui.riasm.electronicsignature.model;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AppEventConst;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.ui.uif2.model.IAppModelService;
import nc.ui.uif2.model.NullModelService;
import nc.uif2.annoations.SupportEvents;
import nc.uif2.annoations.client.SupportUIStates;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.meta.IBDObjectAdapterFactory;


@SupportEvents({AppEventConst.SELECTION_CHANGED,AppEventConst.UISTATE_CHANGED,AppEventConst.MODEL_INITIALIZED,
				AppEventConst.DATA_INSERTED, AppEventConst.DATA_UPDATED, AppEventConst.DATA_DELETED})
				
@SupportUIStates({UIState.DISABLE, UIState.NOT_EDIT})
public  class ElectronicDataAppModel extends HierachicalDataAppModel implements AppEventConst{
	
	/**
	 * 在树上查找包含Id为target_id的BusinessObject的树节点。
	 * @param target_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode findNodeByBusinessObjectId(Object target_id) {
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getTree().getRoot();
		Enumeration e = root.preorderEnumeration();
		e.nextElement();
		while(e.hasMoreElements())
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
			IBDObject bo = getBusinessObjectAdapterFactory().createBDObject(node.getUserObject());
			if(bo.getId().equals(target_id))
			{
				return node;
			}
		} 
		return null;
	}


	public Object add(Object object)  throws Exception  {

		Object obj = getService().insert(object);
		return obj;

	}

	public Object update(Object object)  throws Exception{
		object = getService().update(object);
		return object;
	}


	public DefaultMutableTreeNode findNodeByBusinessObject(Object businessObject) {
		IBDObject bo = getBusinessObjectAdapterFactory().createBDObject(businessObject);
		
		if(bo==null)return null;
		
		DefaultMutableTreeNode node = findNodeByBusinessObjectId(bo.getId());
		return node;
	}	

	public void delete()  throws Exception{
		
		if(getSelectedData() != null)
		{
			getService().delete(getSelectedData());
			
		}
	}
}
