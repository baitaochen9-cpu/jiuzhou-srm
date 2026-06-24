package nc.ui.ic.location.action;

import java.awt.event.ActionEvent;

import nc.ui.ic.location.ref.LocationRefDlg;
import nc.ui.ic.location.ref.LocationSplitInDlg;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.uif2app.event.AppUiStateChangeEvent;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.NCAction;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;

/**
 * <p>
 * <b>拆分按钮：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author songhy
 * @time 2010-8-27 上午08:59:39
 */
public class WeightDifInAction extends NCAction implements
		IAppEventHandler<AppEvent> {
	private static final long serialVersionUID = 1L;

	private LocationRefDlg locationDialog;
	private LocationSplitInDlg refDlg;

	public WeightDifInAction(LocationRefDlg locationDialog) {
		super();
		this.setBtnName("入库");
		this.setCode("DIFFIN");
		this.putValue("ShortDescription", "称量差异入库");
		this.locationDialog = locationDialog;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		this.locationDialog.setSelectedVOsCache();
		OnhandSNViewVO[] datavos = this.locationDialog.getSelectedVOs();

		if (datavos == null || datavos.length == 0) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(locationDialog, "提示",
					"请选择数据后在入库");
			return;
		}

		if (datavos.length > 1) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(locationDialog, "提示",
					"请仅选择一条数据后在入库");
			return;
		}
		
		LocationSplitInDlg refDlg = new LocationSplitInDlg();
		refDlg.setDatavo(datavos[0]);
		refDlg.getAction().setValue(datavos[0].getVsncode());
		if (refDlg.showModal() == UIDialog.ID_OK) {
			locationDialog.loadData();
		} else {

		}
	}

	@Override
	public void handleAppEvent(AppEvent e) {
		if (e instanceof AppUiStateChangeEvent || e instanceof OrgChangedEvent) {
			super.handleEvent(e);
		}
	}

}