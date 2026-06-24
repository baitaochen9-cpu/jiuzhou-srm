package nc.ui.ic.location.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.m4n.ITransformMaitain;
import nc.ui.ic.location.ref.LocationRefDlg;
import nc.ui.ic.location.ref.LocationSplitDlg;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.uif2app.event.AppUiStateChangeEvent;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.NCAction;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

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
 * @since 6.0nc.bs.ic.general.plugins.GeneralDefdocPlugin 133995
 *        CC2020050100000100 07
 * @author songhy
 * @time 2010-8-27 上午08:59:39
 */
public class SplitAction extends NCAction implements IAppEventHandler<AppEvent> {
	private static final long serialVersionUID = 1L;
	private LocationRefDlg locationDialog;
	private LocationSplitDlg refDlg;

	public SplitAction(LocationRefDlg locationDialog) {
		super();
		this.setBtnName("拆分");
		this.setCode("split");
		this.putValue("ShortDescription", "拆分");
		this.locationDialog = locationDialog;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		this.locationDialog.setSelectedVOsCache();
		OnhandSNViewVO[] datavos = this.locationDialog.getSelectedVOs();

		if (datavos == null || datavos.length == 0) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(locationDialog, "提示",
					"请选择数据后在拆分");
			return;
		}

		if (datavos.length > 1) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(locationDialog, "提示",
					"请仅选择一条数据后在拆分");
			return;
		}
		OnhandSNViewVO data = datavos[0];
		if(StringUtil.isEmpty(data.getVbatchcode())){
			nc.ui.pub.beans.MessageDialog.showHintDlg(locationDialog, "提示",
					"批次号不能为空");
			return;
		}
		this.getLocationSplitDlg().setDatavo(data);
		if (this.getLocationSplitDlg().showModal() == UIDialog.ID_OK) {
			locationDialog.loadData();
		}
	}

	

	private LocationSplitDlg getLocationSplitDlg() {
		if (this.refDlg == null) {
			this.refDlg = new LocationSplitDlg();
		}
		return this.refDlg;
	}

	@Override
	public void handleAppEvent(AppEvent e) {
		if (e instanceof AppUiStateChangeEvent || e instanceof OrgChangedEvent) {
			super.handleEvent(e);
		}
	}

}