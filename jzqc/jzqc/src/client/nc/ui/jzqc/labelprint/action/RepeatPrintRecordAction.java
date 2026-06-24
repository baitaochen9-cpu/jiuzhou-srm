package nc.ui.jzqc.labelprint.action;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletLinkEvent;
import nc.funcnode.ui.FuncletLinkListener;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.funcreg.FuncRegisterVO;

import org.apache.commons.lang.ArrayUtils;

public class RepeatPrintRecordAction extends NCAction {
	private static final long serialVersionUID = 1L;
	private AbstractAppModel model;

	public RepeatPrintRecordAction() {
		String str = "标签重打记录";
		this.setBtnName(str);
		this.setCode("repeatprintreord");
	}

	public void doAction(ActionEvent e) throws Exception {

		Object o = getModel().getSelectedData();
		if (o == null) {
			throw new BusinessException("请先选中数据！");
		} else {
			AggLabelPrintHVO hvo = (AggLabelPrintHVO) o;
			ILabelprintMaintain operator = NCLocator.getInstance().lookup(
					ILabelprintMaintain.class);
			AggLabelprintapplyHVO[] bills = operator.repeatPrintRecords(hvo);
			ToftPanelAdaptor adaptor = (ToftPanelAdaptor) getModel()
					.getContext().getEntranceUI();
			FuncletInitData data = new FuncletInitData();

			if (ArrayUtils.isEmpty(bills)) {
				MessageDialog.showWarningDlg(adaptor, "提示", "没有重复打印记录");
				return;
			}
			data.setInitData(bills);
			IFuncRegisterQueryService qry = (IFuncRegisterQueryService) NCLocator
					.getInstance().lookup(IFuncRegisterQueryService.class);

			FuncRegisterVO function = qry.queryFunctionByCode("H3010300");
			FuncletLinkListener linkListener = new FuncletLinkListener() {
				public void dealLinkEvent(FuncletLinkEvent event) {
					if (event.getID() == 0) {
					}

				}

			};
			Dimension newSize = new Dimension(Toolkit.getDefaultToolkit()
					.getScreenSize().width * 8 / 10, Toolkit
					.getDefaultToolkit().getScreenSize().height * 8 / 10);

			FuncletWindowLauncher.openFuncNodeForceModalDialog(adaptor, function, data,
					linkListener, true, newSize);
		}
	}

	protected boolean isActionEnable() {
		Object obj = getModel().getSelectedData();
		if (obj == null)
			return false;
		return model.getUiState() == UIState.NOT_EDIT;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

}
