package nc.ui.qc.supplierqualitystatus.view;

import nc.ui.uif2.components.ITabbedPaneAwareComponent;
import nc.ui.uif2.components.ITabbedPaneAwareComponentListener;
import nc.ui.uif2.components.TabbedPaneAwareCompnonetDelegate;
import nc.ui.uif2.components.TreePanel;
import nc.ui.uif2.model.HierachicalDataAppModel;


/**
 * 功能节点管理的树形结构面板
 * 
 * @author lkp
 *
 */
public class SupplierTreePanel extends TreePanel implements ITabbedPaneAwareComponent{

	private static final long serialVersionUID = 3946737813835132661L;
	
	private TabbedPaneAwareCompnonetDelegate tabbedPaneAwareCompnonetDelegate = new TabbedPaneAwareCompnonetDelegate();
	
	public void setModel(HierachicalDataAppModel model) {
	
		super.setModel(model);
		getTree().setSelectionRow(0);
	}
  
	public void addTabbedPaneAwareComponentListener(
			ITabbedPaneAwareComponentListener l) {
		tabbedPaneAwareCompnonetDelegate.addTabbedPaneAwareComponentListener(l);
	}

	public boolean canBeHidden() {
		return tabbedPaneAwareCompnonetDelegate.canBeHidden();
	}

	public boolean isComponentVisible() {
		return tabbedPaneAwareCompnonetDelegate.isComponentVisible();
	}

	public void setComponentVisible(boolean componentVisible) {
		tabbedPaneAwareCompnonetDelegate.setComponentVisible(componentVisible);
	} 
	
}
