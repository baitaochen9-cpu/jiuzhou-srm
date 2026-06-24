package nc.ui.ic.location.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.m4n.ITransformMaitain;
import nc.ui.ic.location.ref.LocationRefDlg;
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
public class WeightDiffOutAction extends NCAction implements
		IAppEventHandler<AppEvent> {
	private static final long serialVersionUID = 1L;

	private LocationRefDlg locationDialog;

	public WeightDiffOutAction(LocationRefDlg locationDialog) {
		super();
		this.setBtnName("出库");
		this.setCode("DIFFOUT");
		this.putValue("ShortDescription", "称量差异出库");
		this.locationDialog = locationDialog;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		this.locationDialog.setSelectedVOsCache();
		OnhandSNViewVO[] datavos = this.locationDialog.getSelectedVOs();

		if (datavos == null || datavos.length == 0) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(locationDialog, "提示",
					"请选择数据后在出库");
			return;
		}
		try {
			ITransformMaitain server = NCLocator.getInstance().lookup(
					ITransformMaitain.class);
			server.pushSaveOut(datavos[0], datavos);
			locationDialog.loadData();
		} catch (Exception e1) {
			e1.printStackTrace();
			nc.ui.pub.beans.MessageDialog.showHintDlg(locationDialog, "提示",
					"出库失败" + e1.getMessage());
			return;
		}

	}

	@Override
	public void handleAppEvent(AppEvent e) {
		if (e instanceof AppUiStateChangeEvent || e instanceof OrgChangedEvent) {
			super.handleEvent(e);
		}
	}

}