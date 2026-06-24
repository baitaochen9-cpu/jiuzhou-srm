package nc.ui.qc.supplierqualitystatus.view;

import nc.ui.mmgp.uif2.model.MMGPTreeMangeModelDataManager;
import nc.ui.mmgp.uif2.view.MMGPShowUpableBillListView;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractTreeManageQueryAndRefreshMrg;
import nc.ui.uif2.model.HierachicalDataAppModel;

public class SupplierTreeBillListView extends MMGPShowUpableBillListView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8273586184027442334L;
	
	private AbstractTreeManageQueryAndRefreshMrg treeAndListViewRefresh;

	private HierachicalDataAppModel treeAppModel;

	private MMGPTreeMangeModelDataManager datamanager;

	public HierachicalDataAppModel getTreeAppModel() {
		return treeAppModel;
	}

	public void setTreeAppModel(HierachicalDataAppModel treeAppModel) {
		this.treeAppModel = treeAppModel;
	}

	public MMGPTreeMangeModelDataManager getDatamanager() {
		return datamanager;
	}

	public void setDatamanager(MMGPTreeMangeModelDataManager datamanager) {
		this.datamanager = datamanager;
	}

	public AbstractTreeManageQueryAndRefreshMrg getTreeAndListViewRefresh() {
		return treeAndListViewRefresh;
	}

	public void setTreeAndListViewRefresh(
			AbstractTreeManageQueryAndRefreshMrg treeAndListViewRefresh) {
		this.treeAndListViewRefresh = treeAndListViewRefresh;
	}
	@Override
	public void bodyRowChange(BillEditEvent e) {
//		if(getModel().getUiState() == UIState.NOT_EDIT){
			super.bodyRowChange(e);
//		}
	}
}
