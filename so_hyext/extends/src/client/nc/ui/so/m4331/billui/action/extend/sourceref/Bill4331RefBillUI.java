package nc.ui.so.m4331.billui.action.extend.sourceref;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.event.EventListenerList;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pubapp.billref.src.DefaultBillReferQuery;
import nc.ui.pubapp.billref.src.IRefPanelInit;
import nc.ui.pubapp.billref.src.RefBillModel;
import nc.ui.pubapp.billref.src.RefContext;
import nc.ui.pubapp.billref.src.ShowState;
import nc.ui.pubapp.billref.src.action.QueryAction;
import nc.ui.pubapp.billref.src.value.BillListValueGetter;
import nc.ui.pubapp.billref.src.value.DefaultValueGetter;
import nc.ui.pubapp.billref.src.view.ActionPanel;
import nc.ui.pubapp.billref.src.view.AggVOToViewVOTransfer;
import nc.ui.pubapp.billref.src.view.BillFiltMaster;
import nc.ui.pubapp.billref.src.view.ExtendedPanelContainer;
import nc.ui.pubapp.billref.src.view.RefBillUI;
import nc.ui.pubapp.billref.src.view.RefListPanel;
import nc.ui.pubapp.billref.src.view.RefSingleTableListPanel;
import nc.ui.pubapp.billref.src.view.RefSingleTableListView;
import nc.ui.pubapp.billref.src.view.RefTabUIContainer;
import nc.ui.pubapp.billref.src.view.RefView;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.DefaultExceptionHanler;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.scale.ISetValueListener;
import nc.vo.uif2.AppStatusMemento;
import nc.vo.uif2.FileAppStatusSerializer;

import org.apache.commons.lang.ArrayUtils;

public class Bill4331RefBillUI extends RefBillUI implements AppEventListener {
	UIPanel filterConditionPanel;
	private BillFiltMaster billFilter;
	private BillListValueGetter billListValueGetter;
	private UIButton btConditionClear;
	private UIButton btConditionSetter;
	private UIPanel centerPanel;
	private UIPanel ivjSwitchPanel;
	/* 67 */private EventListenerList listeners = new EventListenerList();

	private ActionPanel mainCmdPanel;

	private ActionPanel menuPanel;

	private QueryAction queryAction;

	private RefBillModel refBillModel;

	private RefContext refContext;

	private RefView refListView;

	private RefSingleTableListView refSingleListView;

	private RefTabUIContainer refTabUIContainer;

	private UIPanel southPanel;

	private ActionPanel tabActionPanel;

	private UILabel userInfoLabel;

	private UIPanel userInfoPanel;

	public void addAppEventListener(AppEventListener l) {
		/* 101 */listeners.remove(AppEventListener.class, l);
		/* 102 */listeners.add(AppEventListener.class, l);
	}

	public void deleteCondtions() {
		/* 106 */getDefaltConditionPanel().removeAll();
		/* 107 */FlowLayout f = new FlowLayout();
		/* 108 */f.setAlignment(0);
		/* 109 */getDefaltConditionPanel().setLayout(f);
		/* 110 */getDefaltConditionPanel().add(getBtConditionSetter());
		/* 111 */if (getRefTabUIContainer().isLeftBillShow()) {
			/* 112 */getRefListView().addNorthPanel(getDefaltConditionPanel());
		} else {
			/* 114 */getRefSingleListView().addNorthPanel(
					getDefaltConditionPanel());
		}

		/* 118 */updateUI();
	}

	public void fireEvent(AppEvent event) {
		/* 122 */AppEventListener[] ls = (AppEventListener[]) listeners
				.getListeners(AppEventListener.class);

		/* 124 */for (AppEventListener listener : ls) {
			/* 125 */listener.handleEvent(event);
		}
	}

	public BillFiltMaster getBillFilter() {
		/* 133 */return billFilter;
	}

	public RefListPanel getBillListPanel() {
		/* 137 */return getRefListView().getRefListPanel();
	}

	public BillListValueGetter getBillListValueGetter() {
		/* 141 */if (billListValueGetter == null) {
			/* 142 */billListValueGetter = new DefaultValueGetter();
		}
		/* 144 */return billListValueGetter;
	}

	public UIButton getBtConditionClear() {
		/* 148 */if (btConditionClear == null) {
			/* 149 */btConditionClear = new UIButton(NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0039"));

			/* 154 */btConditionClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					/* 157 */getBillFilter().clearCondion();
				}
			});
		}

		/* 164 */return btConditionClear;
	}

	public UIButton getBtConditionSetter() {
		/* 168 */if (btConditionSetter == null) {
			/* 169 */btConditionSetter = new UIButton(NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0040"));

			/* 174 */btConditionSetter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					/* 178 */getBillFilter().showChooseDialog();
				}
			});
		}

		/* 184 */return btConditionSetter;
	}

	public UIPanel getCenterPanel() {
		/* 188 */if (centerPanel == null) {
			/* 189 */centerPanel = new UIPanel();
			/* 190 */centerPanel.setLayout(new BorderLayout());
			/* 191 */centerPanel.add(getSwitchPanel(), "Center");

			/* 195 */centerPanel.add(getSouthPanel(), "South");
		}
		/* 197 */return centerPanel;
	}

	public UIPanel getDefaltConditionPanel() {
		/* 201 */if (filterConditionPanel == null) {
			/* 202 */filterConditionPanel = new UIPanel();
			/* 203 */FlowLayout f = new FlowLayout();
			/* 204 */f.setAlignment(0);
			/* 205 */filterConditionPanel.setLayout(f);
			/* 206 */filterConditionPanel.setBorder(BorderFactory
					.createEtchedBorder());

			/* 208 */filterConditionPanel.add(getBtConditionSetter());
		}
		/* 210 */return filterConditionPanel;
	}

	public int getHeadSelRow() {
		/* 214 */int selrow = -1;
		/* 215 */if (getRefTabUIContainer().isLeftBillShow()) {
			/* 216 */if (getBillListPanel().getHeadBillModel().getRowCount() <= 0) {
				/* 217 */return selrow;
			}
			/* 219 */selrow = getBillListPanel().getHeadTable()
					.getSelectedRow();
		} else {
			/* 221 */if (getSrcBillViewPanel().getHeadBillModel().getRowCount() <= 0) {
				/* 222 */return selrow;
			}
			/* 224 */selrow = getSrcBillViewPanel().getHeadTable()
					.getSelectedRow();
		}
		/* 226 */return selrow;
	}

	public Object getHeadValue(int row, String key) {
		/* 230 */if (getRefTabUIContainer().isLeftTabShow()) {
			/* 231 */return getBillListPanel().getHeadBillModel().getValueAt(
					row, key);
		}

		/* 234 */return getSrcBillViewPanel().getHeadBillModel().getValueAt(
				row, key);
	}

	public ActionPanel getMainCmdPanel() {
		/* 239 */return mainCmdPanel;
	}

	public ActionPanel getMenuPanel() {
		/* 243 */return menuPanel;
	}

	public QueryAction getQueryAction() {
		/* 247 */if (queryAction.getExceptionHandler() == null) {
			/* 248 */queryAction
					.setExceptionHandler(new DefaultExceptionHanler());
		}
		/* 250 */return queryAction;
	}

	public RefBillModel getRefBillModel() {
		/* 254 */return refBillModel;
	}

	public RefContext getRefContext() {
		/* 258 */return refContext;
	}

	public RefView getRefListView() {
		/* 262 */return refListView;
	}

	public RefSingleTableListView getRefSingleListView() {
		/* 266 */return refSingleListView;
	}

	public RefTabUIContainer getRefTabUIContainer() {
		/* 285 */return refTabUIContainer;
	}

	public RefSingleTableListPanel getSelBillViewPanel() {
		/* 289 */return getRefSingleListView().getSelSingleTableListPanel();
	}

	public AggregatedValueObject[] getSelectVOs() {

		Map<String, RefBillModel.BillSelectedStatus> map = getRefBillModel()
				.getRowSelectManager().getBillStatusMap();

		List<AggregatedValueObject> list = new ArrayList<>();

		for (Entry<String, RefBillModel.BillSelectedStatus> entry : map
				.entrySet()) {
			String key = entry.getKey();
			RefBillModel.BillSelectedStatus value = entry.getValue();
			if (value.isBillSelected()) {
				list.add(getRefBillModel().getBillVO(key));
			}
		}
		return list.toArray(new AggregatedValueObject[list.size()]);
	}

	public UIPanel getSouthPanel() {
		/* 308 */if (southPanel == null) {
			/* 309 */southPanel = new UIPanel();
			/* 310 */southPanel.setLayout(new BorderLayout());
			/* 311 */if (getRefContext().getRefInfo().getExtendedPanel() != null) {
				/* 312 */ExtendedPanelContainer extendedPanelContainer = new ExtendedPanelContainer(
						getRefContext().getRefInfo().getExtendedPanel());

				/* 314 */southPanel.add(extendedPanelContainer, "Center");

				/* 316 */southPanel.add(getUserInfoPanel(), "South");
			} else {
				/* 319 */southPanel.add(getUserInfoPanel(), "Center");
			}
		}

		/* 324 */return southPanel;
	}

	public RefSingleTableListPanel getSrcBillViewPanel() {
		/* 335 */return getRefSingleListView().getRefSingleTableListPanel();
	}

	public ActionPanel getTabActionPanel() {
		/* 339 */return tabActionPanel;
	}

	public UILabel getUserInfoLabel() {
		/* 343 */if (userInfoLabel == null) {
			/* 344 */userInfoLabel = new UILabel();
			/* 345 */userInfoLabel.setSize(userInfoLabel.getWidth(),
					getHeight());
		}

		/* 349 */return userInfoLabel;
	}

	public UIPanel getUserInfoPanel() {
		/* 353 */if (userInfoPanel == null) {
			/* 354 */userInfoPanel = new UIPanel();
			/* 355 */FlowLayout f = new FlowLayout();
			/* 356 */f.setAlignment(0);
			/* 357 */userInfoPanel.setLayout(f);
			/* 358 */userInfoPanel.add(getUserInfoLabel());
		}
		/* 360 */return userInfoPanel;
	}

	public void handleEvent(AppEvent event) {
		/* 365 */if ("Data_Refresh" == event.getType()) {
			/* 366 */setModelVOsToUI();
			/* 367 */} else if ("Selected_data_update" == event.getType()) {
			/* 368 */if (getRefTabUIContainer().isRightBillShow()) {
				/* 369 */refreshMasterSelectPanel();
			} else {
				/* 371 */refreshSingleSelectPanel();
			}
			/* 373 */} else if ("Selected_body_data_update" == event.getType()) {
			/* 375 */refreshMasterBodySelectPanel();
		}
	}

	public void initPanel(nc.ui.pub.bill.BillListPanel masterPanel,
			nc.ui.pub.bill.BillListPanel singlePanel) {
		/* 380 */IRefPanelInit refPanelInit = getRefContext().getRefPanelInit();
		/* 381 */if (refPanelInit != null) {
			/* 382 */refPanelInit.refMasterPanelInit(masterPanel);
			/* 383 */refPanelInit.refSinglePanelInit(singlePanel);
		}
	}

	public void loadHeadData() {
		/* 389 */getQueryAction().setInitQuery(true);
		try {
			DefaultBillReferQuery referQuery = (DefaultBillReferQuery) getRefContext()
					.getRefDialog().getQueyDlg();
			referQuery.getBillSrcVar().setWhereStr(
					getRefContext().getRefDialog().getBillSourceVar()
							.getWhereStr());
			/* 391 */getQueryAction().actionPerformed(
					new ActionEvent(this, 0, "query"));
		} catch (Exception e) {
			/* 394 */ExceptionUtils.wrappException(e);
		}
	}

	public void refreshMasterBodySelectPanel() {
		/* 399 */if (getRefListView().getSelectedListPanel()
				.getChildListPanel().getTable().getRowCount() == 1) {
			/* 401 */getRefListView().refreshSelectedPanelData();
			/* 402 */getRefListView().getSelectedListPanel()
					.getBillRowManager().synchronizedAllRowStatus();
		} else {
			/* 405 */int row = getRefListView().getSelectedListPanel()
					.getChildListPanel().getTable().getSelectedRow();

			/* 407 */if (row == -1) {
				/* 408 */return;
			}
			/* 410 */getRefListView().getSelectedListPanel()
					.getChildListPanel().delLine(new int[] { row });
		}

		/* 413 */getRefListView().getSelectedListPanel().updateUI();
	}

	public void refreshMasterSelectPanel() {
		/* 417 */getRefListView().refreshSelectedPanelData();
		/* 418 */getRefListView().getSelectedListPanel().getBillRowManager()
				.synchronizedAllRowStatus();

		/* 420 */getRefListView().getSelectedListPanel().updateUI();
	}

	public void refreshSingleSelectPanel() {
		/* 424 */getRefSingleListView().refreshSelectedPanelData();
		/* 425 */getRefSingleListView().getSelSingleTableListPanel()
				.getBillViewRowManager().synchronizedAllRowStatus();

		/* 427 */getRefSingleListView().getSelSingleTableListPanel().updateUI();
	}

	public void removeAppEventListener(AppEventListener l) {
		/* 431 */listeners.remove(AppEventListener.class, l);
	}

	public void setBillListValueGetter(BillListValueGetter billListValueGetter) {
		/* 435 */this.billListValueGetter = billListValueGetter;
	}

	public void setMainCmdPanel(ActionPanel mainCmdPanel) {
		/* 439 */this.mainCmdPanel = mainCmdPanel;
	}

	public void setMenuPanel(ActionPanel menuPanel) {
		/* 443 */this.menuPanel = menuPanel;
	}

	public void setModelVOsToUI() {
		/* 447 */if (getRefTabUIContainer().isLeftBillShow()) {
			/* 448 */setVOToRefBillList();
		} else {
			/* 450 */setVOToRefBillView();
		}
	}

	public void setQueryAction(QueryAction queryAction) {
		/* 455 */this.queryAction = queryAction;
	}

	public void setRefBillModel(RefBillModel refBillModel) {
		/* 459 */this.refBillModel = refBillModel;
		/* 460 */this.refBillModel.addAppEventListener(this);
		/* 461 */this.refBillModel.setRefInfo(getRefContext().getRefInfo());
	}

	public void setRefContext(RefContext refContext) {
		/* 465 */this.refContext = refContext;
	}

	public void setRefListView(RefView refListView) {
		/* 469 */this.refListView = refListView;
	}

	public void setRefSingleListView(RefSingleTableListView refSingleListView) {
		/* 473 */this.refSingleListView = refSingleListView;
	}

	public void setShowState(ShowState showstate) {
		/* 477 */showTo(showstate);
	}

	public void setTabActionPanel(ActionPanel tabActionPanel) {
		/* 481 */this.tabActionPanel = tabActionPanel;
	}

	public void showUserInfo() {
		/* 492 */int billCount = 0;
		/* 493 */int rowCount = 0;
		/* 494 */if (getRefBillModel().getRowSelectManager() != null) {
			/* 495 */billCount = getRefBillModel().getRowSelectManager()
					.getSelectBillCount();
			/* 496 */rowCount = getRefBillModel().getRowSelectManager()
					.getSelectBillRowCount();
		}

		/* 500 */String info = "";
		/* 501 */if (rowCount == 0) {
			/* 503 */info = NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"pubapp_0", "0pubapp-0385", null,
					new String[] { "" + billCount });

		} else {

			/* 512 */info = NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"pubapp_0", "0pubapp-0249", null,
					new String[] { "" + billCount, "" + rowCount });
		}

		/* 521 */getUserInfoLabel().setText(info);
		/* 522 */getUserInfoPanel().validate();
	}

	public void switchShow() {
		/* 528 */showTo(getRefTabUIContainer().isBillShow() ? ShowState.BillView
				: ShowState.Bill);
	}

	public void updateFilterConditionPanel(UILabel[] labels,
			UITextField[] textfields) {
		/* 535 */if ((labels == null) || (textfields == null)
				|| (labels.length != textfields.length)) {
			/* 537 */return;
		}
		/* 539 */getDefaltConditionPanel().removeAll();
		/* 540 */getDefaltConditionPanel().setLayout(new BorderLayout());
		/* 541 */getDefaltConditionPanel().add(
				getDynamicConditionPanel(labels, textfields), "Center");

		/* 544 */getDefaltConditionPanel().add(getSetClearBtPanel(), "East");

		/* 553 */if (getRefTabUIContainer().isLeftBillShow()) {
			/* 554 */getRefListView().addNorthPanel(getDefaltConditionPanel());
		} else {
			/* 556 */getRefSingleListView().addNorthPanel(
					getDefaltConditionPanel());
		}

		/* 560 */updateUI();
	}

	protected void fireInitEvent() {
		/* 565 */fireEvent(new AppEvent("ref_billlistui_init", this,
				getBillListPanel()));

		/* 567 */fireEvent(new AppEvent("ref_billviewui_init", this,
				getSrcBillViewPanel()));

		/* 569 */fireEvent(new AppEvent("ref_billviewselui_init", this,
				getSelBillViewPanel()));
	}

	protected UIPanel getSwitchPanel() {
		/* 579 */if (ivjSwitchPanel == null) {
			/* 580 */ivjSwitchPanel = new UIPanel();
			/* 581 */ivjSwitchPanel.setLayout(new BorderLayout());
		}

		/* 584 */return ivjSwitchPanel;
	}

	protected void initUI() {
		/* 589 */setLayout(new BorderLayout());

		/* 594 */getRefContext().setRefBill(this);

		/* 596 */initPanel(getBillListPanel(), getRefSingleListView()
				.getRefSingleTableListPanel());

		/* 601 */refTabUIContainer = new RefTabUIContainer(getRefContext(),
				ShowState.Bill);

		/* 603 */billFilter = new BillFiltMaster(getRefContext());
		/* 604 */getSwitchPanel().add(refTabUIContainer, "Center");

		/* 606 */switchState(ShowState.Bill);

		/* 619 */add(getCenterPanel(), "Center");

		/* 621 */add(getMainCmdPanel(), "South");
		getRefListView().getRefListPanel().setEnabled(true);
		/* 622 */if (getRefContext().getRefInfo().getRefPanelInit() != null) {
			/* 623 */getRefContext().getRefInfo().getRefPanelInit()
					.refMasterPanelInit(getRefListView().getRefListPanel());

			/* 628 */getRefContext()
					.getRefInfo()
					.getRefPanelInit()
					.refMasterPanelInit(getRefListView().getSelectedListPanel());

			/* 631 */getRefContext()
					.getRefInfo()
					.getRefPanelInit()
					.refSinglePanelInit(
							getRefSingleListView().getRefSingleTableListPanel());

			/* 635 */getRefContext()
					.getRefInfo()
					.getRefPanelInit()
					.refSinglePanelInit(
							getRefSingleListView().getSelSingleTableListPanel());
		}
	}

	protected void setVOToRefBillList() {
		/* 645 */AggregatedValueObject[] vos = getRefBillModel()
				.getAllBillVOs();

		/* 648 */getRefListView().getRefListPanel().getHeadBillModel()
				.clearBodyData();
		/* 649 */getRefListView().getRefListPanel().getBodyBillModel()
				.clearBodyData();

		/* 651 */if ((vos != null) && (vos.length > 0)) {
			/* 653 */if ((getBillListPanel() instanceof nc.ui.pubapp.bill.BillListPanel)) {
				/* 654 */Set<ISetValueListener> listenerSet = getBillListPanel()
						.getListenerSet();

				/* 656 */for (ISetValueListener l1 : listenerSet) {
					/* 657 */l1.setValue(vos);
				}
			}

			/* 661 */getBillListPanel().getBillListValueSetter()
					.setHeaderDatas(getRefListView().getRefListPanel(), vos);

			/* 663 */getBillListPanel().setSelectRow(-1);
			/* 664 */getBillListPanel().updateUI();
		}

		/* 675 */getBillListPanel().getRefContext().getRefBill().showUserInfo();
	}

	protected void setVOToRefBillView() {
		/* 679 */if (getRefBillModel() == null) {
			/* 680 */return;
		}
		/* 682 */AggregatedValueObject[] vos = getRefBillModel()
				.getAllBillVOs();

		/* 685 */if (ArrayUtils.isEmpty(vos)) {
			/* 686 */getRefSingleListView().getRefSingleTableListPanel()
					.getHeadBillModel().clearBodyData();

			/* 688 */getRefSingleListView().getRefSingleTableListPanel()
					.getBodyBillModel().clearBodyData();
		} else {
			/* 691 */CircularlyAccessibleValueObject[] viewVOs = AggVOToViewVOTransfer
					.transfer(vos, getRefContext().getRefInfo().getViewVO()
							.getClass());

			/* 694 */getSrcBillViewPanel()
					.getBillViewValueSetter()
					.setHeaderDatas(
							getRefSingleListView().getRefSingleTableListPanel(),
							viewVOs);
		}
	}

	private UIPanel getDynamicConditionPanel(UILabel[] labels,
			UITextField[] textfields) {
		/* 704 */UIPanel conditionPanel = new UIPanel();
		/* 705 */conditionPanel.setLayout(new BorderLayout());
		/* 706 */if (labels.length <= 3) {
			/* 707 */conditionPanel.add(
					getOneLayerConditionPanel(labels, textfields), "Center");
		} else {
			/* 710 */conditionPanel.add(
					getOneLayerConditionPanel((UILabel[]) ArrayUtils.subarray(
							labels, 0, 3), (UITextField[]) ArrayUtils.subarray(
							textfields, 0, 3)), "Center");

			/* 714 */conditionPanel.add(
					getOneLayerConditionPanel((UILabel[]) ArrayUtils.subarray(
							labels, 3, labels.length),
							(UITextField[]) ArrayUtils.subarray(textfields, 3,
									textfields.length)), "South");
		}

		/* 719 */return conditionPanel;
	}

	private UIPanel getOneLayerConditionPanel(UILabel[] labels,
			UITextField[] textfields) {
		/* 724 */UIPanel oneLayerConditionPanel = new UIPanel();
		/* 725 */FlowLayout f = new FlowLayout();
		/* 726 */f.setAlignment(0);
		/* 727 */oneLayerConditionPanel.setLayout(f);
		/* 728 */for (int i = 0; i < labels.length; i++) {
			/* 729 */oneLayerConditionPanel.add(labels[i]);
			/* 730 */oneLayerConditionPanel.add(textfields[i]);
		}
		/* 732 */return oneLayerConditionPanel;
	}

	private UIPanel getSetBtPanel() {
		/* 749 */UIPanel btPanel = new UIPanel();
		/* 750 */btPanel.setLayout(new FlowLayout());
		/* 751 */btPanel.add(getBtConditionSetter());
		/* 752 */return btPanel;
	}

	private UIPanel getSetClearBtPanel() {
		/* 758 */UIPanel btPanel = new UIPanel();
		/* 759 */FlowLayout f = new FlowLayout();
		/* 760 */f.setAlignment(1);
		/* 761 */btPanel.setLayout(f);
		/* 762 */btPanel.add(getBtConditionClear());
		/* 763 */btPanel.add(getBtConditionSetter());

		/* 765 */return btPanel;
	}

	private void saveShowStatus(String status) {
		/* 769 */AppStatusMemento memento = null;
		try {
			/* 771 */memento = new FileAppStatusSerializer()
					.loadAppStatusMemento(getRefContext().getRefInfo()
							.getBillSrcVar().getFunNode(), getRefContext()
							.getRefInfo().getBillSrcVar().getUserId());

		} catch (Exception e) {

			/* 777 */Logger.error(e);
		}
		/* 779 */if (memento == null) {
			/* 780 */memento = new AppStatusMemento();
			/* 781 */memento.setUserId(getRefContext().getRefInfo()
					.getBillSrcVar().getUserId());

			/* 783 */memento.setFunid(getRefContext().getRefInfo()
					.getBillSrcVar().getFunNode());

			/* 785 */Map<Object, Object> saveMap = new HashMap();
			/* 786 */saveMap.put(getRefContext().getShowStatusString(), status);
			/* 787 */memento.setAppStatusMap(saveMap);
		}
		/* 789 */else if (memento.getAppStatusMap() != null) {
			/* 790 */memento.getAppStatusMap().put(
					getRefContext().getShowStatusString(), status);
		} else {
			/* 793 */Map<Object, Object> saveMap = new HashMap();
			/* 794 */saveMap.put(getRefContext().getShowStatusString(), status);
			/* 795 */memento.setAppStatusMap(saveMap);
		}

		try {
			/* 800 */new FileAppStatusSerializer()
					.storeAppStatusMemento(memento);
		} catch (Exception e) {
			/* 802 */Logger.error(e);
		}
	}

	private void showTo(ShowState showstate) {
		/* 808 */if (getRefTabUIContainer().getShowState() == showstate) {
			/* 809 */return;
		}
		/* 811 */switchState(showstate);
	}

	private void switchState(ShowState showstate) {
		/* 815 */getRefTabUIContainer().resetTabUI(showstate);
		/* 816 */if (showstate == ShowState.Bill) {
			/* 818 */if (!getRefListView().isSelectedListPanelShow()) {
				/* 819 */setVOToRefBillList();
			} else {
				/* 821 */getRefListView().refreshSelectedPanelData();
			}
			/* 824 */getRefContext().getRefBill().getBillFilter()
					.masterBillFilt();
			/* 825 */getRefListView().synchronizedAllRowStatus();
			/* 826 */saveShowStatus(showstate.value);
		} else {
			/* 830 */if (!getRefSingleListView().isSelectedListPanelShow()) {
				/* 831 */setVOToRefBillView();
			} else {
				/* 833 */getRefSingleListView().refreshSelectedPanelData();
			}
			/* 837 */getRefContext().getRefBill().getBillFilter()
					.viewBillFilt();
			/* 838 */getRefSingleListView().synchronizedAllRowStatus();
			/* 839 */saveShowStatus(showstate.value);
		}
	}

	public void saveBillCardUISet() {
		/* 846 */AppStatusMemento memento = null;
		try {
			/* 848 */memento = new FileAppStatusSerializer()
					.loadAppStatusMemento(getRefContext().getRefInfo()
							.getBillSrcVar().getFunNode(), getRefContext()
							.getRefInfo().getBillSrcVar().getUserId());

		} catch (Exception e) {

			/* 854 */Logger.error(e);
		}

		/* 857 */String statusID = getRefContext().getBillCardUISetString();
		/* 858 */Object statusObject = getRefListView().getRefListPanel()
				.getBillCardUISet();

		/* 860 */if (memento == null) {
			/* 861 */memento = new AppStatusMemento();
			/* 862 */memento.setUserId(getRefContext().getRefInfo()
					.getBillSrcVar().getUserId());

			/* 864 */memento.setFunid(getRefContext().getRefInfo()
					.getBillSrcVar().getFunNode());

			/* 866 */Map<Object, Object> saveMap = new HashMap();
			/* 867 */saveMap.put(statusID, statusObject);
			/* 868 */memento.setAppStatusMap(saveMap);
		}
		/* 870 */else if (memento.getAppStatusMap() != null) {
			/* 871 */memento.getAppStatusMap().put(statusID, statusObject);
		} else {
			/* 873 */Map<Object, Object> saveMap = new HashMap();
			/* 874 */saveMap.put(statusID, statusObject);
			/* 875 */memento.setAppStatusMap(saveMap);
		}

		try {
			/* 880 */new FileAppStatusSerializer()
					.storeAppStatusMemento(memento);
		} catch (Exception e) {
			/* 882 */Logger.error(e);
		}
	}
}
