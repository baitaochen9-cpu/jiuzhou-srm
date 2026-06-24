package nc.ui.qc.supplierqualitystatus.ace.view;

import nc.ui.mmgp.uif2.view.MMGPOrgPanel;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.ui.uif2.model.IAppModelDataManager;

import org.apache.commons.lang.StringUtils;

public class MMGPTreeManageOrgPanel extends MMGPOrgPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5023118093712418983L;

	private AbstractUIAppModel treeModel;
	private IAppModelDataManager treeDataManager;

	public void setTreeModel(AbstractUIAppModel model) {
		treeModel = model;
	}

	public AbstractUIAppModel getTreeModel() {
		return treeModel;
	}

	public IAppModelDataManager getTreeDataManager() {
		return treeDataManager;
	}

	public void setTreeDataManager(IAppModelDataManager treeDataManager) {
		this.treeDataManager = treeDataManager;
	}

	@Override
	public void setPkOrg(String newPkOrg) {
		String oldPk = this.getRefPane().getRefPK();
		super.setPkOrg(newPkOrg);
		this.getTreeModel().getContext().setPk_org(newPkOrg);

		// 采购发票节点，查询出发票数据后，给orgRefPane上已设置了值，再新增自制单据时，因参照新旧值相同不会派发组织改变事件，
		// 在此修改为模型为新增态时,如orgRefPane的新旧pk非空时也派发组织改变事件
		if ((this.isTreeModelOrgChanged(oldPk, newPkOrg))
		// ||
		// (this.getModel().getUiState() == UIState.ADD &&
		// !StringUtil.isEmpty(oldPk) && !StringUtil.isEmpty(newNewPkOrg))
		) {
			this.fireTreeModelChangedEvent(oldPk, newPkOrg);
		}
	}
	@Override
	protected void handleOrgChangedEvent(OrgChangedEvent e) {
		super.handleOrgChangedEvent(e);
		String pk_org = this.getRefPane().getRefPK();
		this.getTreeModel().getContext().setPk_org(pk_org);
		if (pk_org != null) {
			this.getTreeDataManager().initModel();
		}
	}

	protected void fireTreeModelChangedEvent(String oldPkOrg, String newPkOrg) {
		// String oldPkOrg = this.getModel().getContext().getPk_org();
		this.getTreeModel().getContext().setPk_org(newPkOrg);
		OrgChangedEvent orgChangedEvent = new OrgChangedEvent(oldPkOrg,
				newPkOrg);
		this.getTreeModel().fireEvent(orgChangedEvent);
	}

	protected boolean isTreeModelOrgChanged(String oldPkOrg, String newPkOrg) {
		// String oldPkOrg = this.getModel().getContext().getPk_org();
		return !StringUtils.equals(oldPkOrg, newPkOrg);
	}

}
