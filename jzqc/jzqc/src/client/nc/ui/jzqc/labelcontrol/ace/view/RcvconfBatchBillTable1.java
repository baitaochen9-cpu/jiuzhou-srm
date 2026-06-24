package nc.ui.jzqc.labelcontrol.ace.view;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.bs.logging.Logger;
import nc.buzimsg.action.BuzimsgSelfDefRefPaneListener;
import nc.buzimsg.action.BuzimsgSelfDefRefpaneEvent;
import nc.buzimsg.action.ReceiveRuleInitDataContext;
import nc.buzimsg.util.BuzimsgUtil;
import nc.buzimsg.view.RcvconfBatchBillTable;
import nc.buzimsg.view.ReceiverRefPane;
import nc.buzimsg.view.ReceivingRuleRefPane;
import nc.buzimsg.vo.MsgresRcvConfVO;
import nc.buzimsg.vo.MsgresRcvVO;
import nc.buzimsg.vo.MsgresRegVO;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.md.model.IAttribute;
import nc.md.model.IBusinessEntity;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.UIStateChangeEvent;
import nc.ui.uif2.components.AutoShowUpEventSource;
import nc.ui.uif2.components.IAutoShowUpComponent;
import nc.ui.uif2.components.IAutoShowUpEventListener;
import nc.ui.uif2.components.ITabbedPaneAwareComponent;
import nc.ui.uif2.components.ITabbedPaneAwareComponentListener;
import nc.ui.uif2.components.TabbedPaneAwareCompnonetDelegate;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillTabVO;

import org.apache.commons.lang.ArrayUtils;

public class RcvconfBatchBillTable1 extends RcvconfBatchBillTable implements
		ITabbedPaneAwareComponent, BuzimsgSelfDefRefPaneListener,
		IAutoShowUpComponent {
	private static final long serialVersionUID = 2778677690258550409L;
	/* 66 */public static Map<Integer, MsgresRcvVO[]> RECEIVER_DATA_MAP = new HashMap();

	/* 68 */public static Map<Integer, Object> RCVRULE_DATA_MAP = new HashMap();

	/* 70 */public static Map<Integer, Object> RCVRULE_DATA_DISP_MAP = new HashMap();

	/* 72 */public static Map<Integer, Object> ORG_DATA_MAP = new HashMap();

	/* 74 */public static Map<Integer, String> RECEIVERS_MAP = new HashMap();

	private HierachicalDataAppModel treeModel;

	/* 78 */private TabbedPaneAwareCompnonetDelegate tabbedPaneAwareCompnonetDelegate = null;
	RcvTableCellRenderer renderer;
	private AutoShowUpEventSource autoShowUpComponent = new AutoShowUpEventSource(
			this);

	public void initUI() {
		/* 83 */super.initUI();
		/* 84 */addTableCellRenderer();
		/* 85 */initMouseMotionListener();
	}

	public RcvconfBatchBillTable1() {
		/* 91 */tabbedPaneAwareCompnonetDelegate = new TabbedPaneAwareCompnonetDelegate();
	}

	public void addTabbedPaneAwareComponentListener(
			ITabbedPaneAwareComponentListener l) {
		/* 97 */tabbedPaneAwareCompnonetDelegate
				.addTabbedPaneAwareComponentListener(l);
	}

	public boolean canBeHidden() {
		/* 103 */if (getModel().getUiState() == UIState.EDIT) {
			/* 104 */return false;
		}
		/* 106 */return true;
	}

	public boolean isComponentVisible() {
		/* 112 */return tabbedPaneAwareCompnonetDelegate.isComponentVisible();
	}

	public void setComponentVisible(boolean componentVisible) {
		/* 118 */tabbedPaneAwareCompnonetDelegate
				.setComponentVisible(componentVisible);
	}

	public boolean beforeEdit(BillEditEvent e) {
		/* 125 */boolean canEdit = permissionCheck();
		/* 126 */if (!canEdit) {
			/* 128 */showErrorMsg();
			/* 129 */return false;
		}

		/* 133 */clearErrorMsg();

		/* 137 */if (e.getKey().equals("receivers")) {
			/* 138 */injectReceiverColumnRefPaneCellEditor(e.getRow());
		}

		/* 145 */if (e.getKey().equals("pk_billtypeid")) {
			/* 147 */BillItem billItem = getBillCardPanel().getBodyItem(
					"pk_billtypeid");
			/* 148 */UIRefPane refPane = (UIRefPane) billItem.getComponent();
			/* 149 */filterTransactionRef(refPane);
		}

		/* 153 */if (e.getKey().equals("pk_org")) {
			/* 155 */BillItem billItem = getBillCardPanel().getBodyItem(
					"pk_org");
			/* 156 */UIRefPane refPane = (UIRefPane) billItem.getComponent();
			/* 157 */replaceRefModelJudgeByUserType(refPane);
			/* 158 */refPane.setMultiSelectedEnabled(true);
			/* 159 */refPane.getRefUIConfig().setIncludeSubShow(true);
			/* 160 */filterOrgRef(refPane);
		}

		/* 163 */return true;
	}

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		/* 168 */this.treeModel = treeModel;
	}

	public void handleEvent(AppEvent event) {
		doHandleModelEvent(event);
		if ((event instanceof UIStateChangeEvent))
		/*     */{
			/* 178 */UIStateChangeEvent stateChangeEvent = (UIStateChangeEvent) event;
			/*     */
			/*     */
			/* 181 */setSortabliltyOfBodyTable(stateChangeEvent);
			/*     */
			/* 183 */clearMapByUIState(stateChangeEvent);
			/* 184 */backupDataMapByUIState(stateChangeEvent);
			/*     */}
	}

	protected void onModelInit() {
		super.onModelInit();
		displayReceiverColumnValue();
		/*      */}

	protected void doAfterEdit(BillEditEvent e) {

		/* 196 */if (e.getKey().equals("receivers")) {
			/* 197 */storingReceiverData(e);
		}

		/* 200 */if (e.getKey().equals("receivingrule_disp")) {
		}

		/* 204 */if (e.getKey().equals("pk_org")) {
			/* 205 */handleOrgRefMulSelection(e);
		}
	}

	private void storingReceiverData(BillEditEvent e) {
		/* 211 */TableColumn receiverColumn = getReceiverColumn();
		/* 212 */ReceiverRefPane refPane = (ReceiverRefPane) receiverColumn
				.getCellEditor().getTableCellEditorComponent(getBodyTable(),
						e.getValue(), true, e.getRow(), 2);

		/* 214 */Integer integer = Integer.valueOf(refPane.getParentRowIndex());
		/* 215 */MsgresRcvVO[] vos = refPane.getCurrSelected();

		/* 217 */setReceiverColumnDispValue(vos, e.getRow());

		/* 219 */RECEIVER_DATA_MAP.put(integer, vos);
	}

	private void storingReceivingRuleData(BillEditEvent e) {
		/* 225 */TableColumn receiverColumn = getRcvRuleColumn();
		/* 226 */ReceivingRuleRefPane refPane = (ReceivingRuleRefPane) receiverColumn
				.getCellEditor().getTableCellEditorComponent(getBodyTable(),
						e.getValue(), true, e.getRow(), 2);

		/* 228 */Integer integer = Integer.valueOf(refPane.getParentRowIndex());
		/* 229 */Object ruleReslut = refPane.getSelected().getQueryResult();
		/* 230 */RCVRULE_DATA_MAP.put(integer, ruleReslut);
	}

	private void handleOrgRefMulSelection(BillEditEvent e) {
		/* 238 */BillItem billItem = getBillCardPanel().getBodyItem("pk_org");
		/* 239 */UIRefPane refPane = (UIRefPane) billItem.getComponent();

		/* 241 */String[] pks = refPane.getRefPKs();
		/* 242 */if (!ArrayUtils.isEmpty(pks)) {
			/* 244 */MsgresRcvConfVO sel = (MsgresRcvConfVO) getModel()
					.getSelectedData();

			try {
				/* 248 */getModel().delLine(e.getRow());
			} catch (Exception e1) {
				/* 252 */Logger.error(e1.getMessage(), e1);
			}

			/* 255 */MsgresRcvConfVO[] vos = new MsgresRcvConfVO[pks.length];
			/* 256 */String msgrescode = "labelcontrol";
			/* 257 */for (int i = 0; i < pks.length; i++) {
				/* 259 */String pk_org = pks[i];

				/* 261 */if (pk_org.equals(sel.getPk_org())) {
					/* 263 */vos[i] = sel;
				} else {
					/* 267 */vos[i] = new MsgresRcvConfVO();
					/* 268 */vos[i].setMsgres_code(msgrescode);
					/* 269 */vos[i].setPk_org(pks[i]);
					/* 270 */vos[i].setReceivers(deepClone(sel.getReceivers()));
					vos[i].setPk_billtypecode("JZ01-Cxx-25");
					vos[i].setPk_billtypeid("1001AA100000000BWXGT");
				}

				/* 273 */vos[i].setPk_group(WorkbenchEnvironment.getInstance()
						.getGroupVO().getPk_group());
			}

			/* 277 */if (isContained(vos, sel.getPk_org())) {
				/* 278 */vos = sorting(vos, sel.getPk_org());
			}
			/* 280 */getModel().addLines(vos);

			/* 283 */displayReceiverColumnValue();
		}
	}

	private MsgresRcvVO[] deepClone(MsgresRcvVO[] rcvs) {
		/* 289 */if (ArrayUtils.isEmpty(rcvs)) {
			/* 290 */return null;
		}
		/* 292 */MsgresRcvVO[] cloned = new MsgresRcvVO[rcvs.length];
		/* 293 */for (int i = 0; i < rcvs.length; i++) {
			/* 295 */MsgresRcvVO msgresRcvVO = rcvs[i];
			/* 296 */MsgresRcvVO rcv = new MsgresRcvVO();
			/* 297 */rcv.setReceivertype(msgresRcvVO.getReceivertype());
			/* 298 */rcv.setReceiverpk(msgresRcvVO.getReceiverpk());
			/* 299 */cloned[i] = rcv;
		}

		/* 302 */return cloned;
	}

	protected Object getEditingLineVO(int rowIndex) {
		/* 309 */Object obj = null;
		/* 310 */Map<String, Object> map;
		IBusinessEntity entity;
		String key;
		Iterator<String> keyIter;
		if (getBillCardPanel().getBillData().isMeataDataTemplate()) {
			/* 312 */map = getBillCardPanel().getBillModel()
					.getBodyRowValueByMetaData(rowIndex);
			/* 313 */handleMap(map);

			/* 315 */BillTabVO[] tabvos = getBillCardPanel().getBillData()
					.getBillTabVOs(1);

			/* 317 */entity = tabvos[0].getBillMetaDataBusinessEntity();
			/* 318 */obj = nc.md.data.access.DASFacade
					.newInstanceWithKeyValues(entity, map)
					.getContainmentObject();

			/* 321 */key = null;
			/* 322 */for (keyIter = map.keySet().iterator(); keyIter.hasNext();) {
				/* 324 */key = (String) keyIter.next();
				/* 325 */IAttribute attribute = entity.getAttributeByPath(key);
				/* 326 */boolean isMultiText = (attribute != null)
						&& (attribute.getDataType().getTypeType() == 58);

				/* 328 */if ((map.get(key) != null)
						&& (BeanHelper.getProperty(obj, key) == null)
						&& ((attribute == null) || (!isMultiText))) {
					/* 329 */BeanHelper.setProperty(obj, key, map.get(key));
				}
			}
		}
		/* 333 */if (obj != null) {
			/* 335 */MsgresRcvConfVO edittingVO = (MsgresRcvConfVO) obj;
			/* 336 */addRcvAndRuleData(edittingVO, rowIndex);
		}
		/* 338 */return obj;
	}

	private void handleMap(Map<String, Object> map) {
		/* 343 */String key = "receivers";
		/* 344 */map.put(key, null);
	}

	private MsgresRcvConfVO[] sorting(MsgresRcvConfVO[] vos, String pk_org) {
		/* 349 */List<MsgresRcvConfVO> sortedList = new ArrayList();
		/* 350 */sortedList.add(0, null);

		/* 352 */for (MsgresRcvConfVO msgresRcvConfVO : vos) {
			/* 354 */if (pk_org.equals(msgresRcvConfVO.getPk_org())) {
				/* 355 */sortedList.set(0, msgresRcvConfVO);
			} else {
				/* 357 */sortedList.add(msgresRcvConfVO);
			}
		}
		/* 360 */if (sortedList.get(0) == null) {
			/* 361 */return (MsgresRcvConfVO[]) sortedList.subList(1,
					sortedList.size()).toArray(new MsgresRcvConfVO[0]);
		}
		/* 363 */return (MsgresRcvConfVO[]) sortedList
				.toArray(new MsgresRcvConfVO[0]);
	}

	private boolean isContained(MsgresRcvConfVO[] vos, String pk_org) {
		/* 369 */if (pk_org == null) {
			/* 370 */return false;
		}
		/* 372 */for (MsgresRcvConfVO msgresRcvConfVO : vos) {
			/* 374 */if (pk_org.equals(msgresRcvConfVO.getPk_org())) {
				/* 375 */return true;
			}
		}
		/* 378 */return false;
	}

	private void injectReceiverColumnRefPaneCellEditor(int rowIndex) {
		/* 383 */TableColumn tempColumn = getReceiverColumn();
		/* 384 */ReceiverRefPane refPane = new ReceiverRefPane(rowIndex,
				getRcvVOUnderCurrSelectedParent(), "labelcontrol");

		/* 386 */refPane.addBuzimsgSelfRefPaneEventListener(this);
		/* 387 */tempColumn.setCellEditor(new BillCellEditor(refPane));
	}

	private boolean permissionCheck() {
		/* 400 */MsgresRcvConfVO confVO = (MsgresRcvConfVO) getModel()
				.getSelectedData();
		/* 401 */int status = confVO.getStatus();

		/* 404 */if (0 == status) {
			try {
				/* 408 */return BuzimsgUtil
						.isLoginUserCanOperate(new MsgresRcvConfVO[] { confVO });
			} catch (BusinessException e) {
				/* 412 */Logger.error(
						"Attempts to do permission check failed!", e);
				/* 413 */return false;
			}
		}

		/* 417 */return true;
	}

	private void showErrorMsg() {
		/* 422 */ShowStatusBarMsgUtil.showErrorMsg(
				NCLangRes.getInstance().getStrByID("sfapp",
						"RcvconfBatchBillTable-0000"),
				NCLangRes.getInstance().getStrByID("msgtemp",
						"RcvconfBatchBillTable-0001"), getModel().getContext());
	}

	private void clearErrorMsg() {
		/* 427 */ShowStatusBarMsgUtil.showStatusBarMsg("", getModel()
				.getContext());
	}

	private TableColumn getReceiverColumn() {
		/* 432 */return getBodyTable().getColumnModel().getColumn(2);
	}

	private TableColumn getRcvRuleColumn() {
		/* 437 */return getBodyTable().getColumnModel().getColumn(2);
	}

	private UITable getBodyTable() {
		/* 442 */return getBillCardPanel().getBillTable();
	}

	private class RcvTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 2909162131155913785L;

		private RcvTableCellRenderer() {
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			/* 453 */JLabel lb = (JLabel) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			/* 455 */lb.setHorizontalAlignment(0);

			/* 457 */String text = "";

			/* 459 */switch (column) {
			case 2:
				/* 466 */lb.setToolTipText(null);
				/* 467 */text = (String) RcvconfBatchBillTable1.RECEIVERS_MAP
						.get(Integer.valueOf(row)) + "";
			}

			/* 474 */lb.setText(text);

			/* 476 */return lb;
		}
	}

	private TableCellRenderer getTableCellRenderer() {
		/* 484 */if (renderer == null) {
			/* 485 */renderer = new RcvTableCellRenderer();
		}
		/* 487 */return renderer;
	}

	private void addTableCellRenderer() {
		getBillCardPanel().getBodyPanel().getShowCol("receivingrule_disp")
				.setCellRenderer(null);
		/* 493 */getBillCardPanel().getBodyPanel().getShowCol("receivers")
				.setCellRenderer(getTableCellRenderer());
		/* 494 */getBillCardPanel().getBodyPanel().getShowCol("pk_billtypeid")
				.setCellRenderer(new TableColumnCellRenderer());
		/* 495 */getBillCardPanel().getBodyPanel().getShowCol("pk_org")
				.setCellRenderer(new TableColumnCellRenderer());
	}

	private void addRcvAndRuleData(MsgresRcvConfVO edittingVO, int rowIndex) {
		/* 500 */if (RECEIVER_DATA_MAP.containsKey(Integer.valueOf(rowIndex))) {
			/* 502 */MsgresRcvVO[] subs = (MsgresRcvVO[]) RECEIVER_DATA_MAP
					.get(Integer.valueOf(rowIndex));
			/* 503 */edittingVO.setReceivers(subs);
		}

		/* 506 */if (RCVRULE_DATA_MAP.containsKey(Integer.valueOf(rowIndex))) {
			/* 508 */Object ruleResult = RCVRULE_DATA_MAP.get(Integer
					.valueOf(rowIndex));
			/* 509 */edittingVO.setReceivingrule(ruleResult);
		}

		/* 512 */if (RCVRULE_DATA_DISP_MAP.containsKey(Integer
				.valueOf(rowIndex))) {
			/* 514 */Object receivingrule_disp = RCVRULE_DATA_DISP_MAP
					.get(Integer.valueOf(rowIndex));
			/* 515 */edittingVO.setReceivingrule_disp(receivingrule_disp);
		}
	}

	private MsgresRcvVO[] getRcvVOUnderCurrSelectedParent() {
		/* 521 */MsgresRcvConfVO parent = (MsgresRcvConfVO) getModel()
				.getSelectedData();

		/* 523 */if (parent == null) {
			/* 524 */return null;
		}
		/* 526 */return parent.getReceivers();
	}

	private MsgresRcvVO[] getRcvVOByRowIndex(int rowIndex) {
		/* 531 */MsgresRcvConfVO parent = (MsgresRcvConfVO) getModel().getRow(
				rowIndex);

		/* 533 */if (parent == null) {
			/* 534 */return null;
		}
		/* 536 */return parent.getReceivers();
	}

	private boolean JudgeIsSame(int rowIndex) {
		/* 545 */MsgresRcvConfVO parent = (MsgresRcvConfVO) getModel().getRow(
				rowIndex);

		/* 547 */if (parent == null)
			/* 548 */return false;
		/* 549 */String pk_org = parent.getPk_org();
		/* 550 */String pk_group = parent.getPk_group();
		/* 551 */if ((pk_org == null) || (pk_group == null))
			/* 552 */return false;
		/* 553 */return pk_org.equals(pk_group);
	}

	private ReceiveRuleInitDataContext createReceiveRuleInitDataContext() {
		/* 557 */MsgresRegVO regVO = (MsgresRegVO) treeModel.getSelectedData();
		/* 558 */Object ruleResult = ((MsgresRcvConfVO) getModel()
				.getSelectedData()).getReceivingrule();

		/* 560 */ReceiveRuleInitDataContext context = new ReceiveRuleInitDataContext(
				regVO.getMetaid(), ruleResult);

		/* 563 */return context;
	}

	private void clearMapByUIState(UIStateChangeEvent stateChangeEvent) {
		/* 568 */if ((stateChangeEvent.getNewState() != UIState.ADD)
				&& (stateChangeEvent.getNewState() != UIState.EDIT)) {
			/* 569 */clearMap();
		}
	}

	private void backupDataMapByUIState(UIStateChangeEvent stateChangeEvent) {
		/* 574 */if ((stateChangeEvent.getNewState() == UIState.ADD)
				|| (stateChangeEvent.getNewState() == UIState.EDIT)) {
			/* 575 */backupDataMap();
		}
	}

	private void backupDataMap() {
		/* 580 */int rowCount = getModel().getRowCount();

		/* 582 */if (rowCount < 0) {
			/* 583 */return;
		}
		/* 585 */for (int i = 0; i < rowCount; i++) {
			/* 587 */MsgresRcvVO[] receivers = ((MsgresRcvConfVO) getModel()
					.getRow(i)).getReceivers();
			/* 588 */RECEIVER_DATA_MAP.put(Integer.valueOf(i), receivers);
		}
	}

	private void setSortabliltyOfBodyTable(UIStateChangeEvent stateChangeEvent) {
		/* 595 */if (stateChangeEvent.getNewState() == UIState.EDIT) {
			/* 597 */getBodyTable().removeSortListener();
		} else {
			/* 601 */clearMap();
			/* 602 */getBodyTable().addSortListener();
		}
	}

	private void filterTransactionRef(UIRefPane refPane) {
		/* 608 */refPane.setWhereString(BuzimsgUtil
				.getTransactionWherePart(getBilltypecode()));
	}

	private void replaceRefModelJudgeByUserType(UIRefPane refPane) {
		/* 614 */if (WorkbenchEnvironment.getInstance().getLoginUser()
				.getUser_type().intValue() == 0) {
			/* 615 */return;
		}
		/* 617 */refPane.setRefNodeName("业务单元");
	}

	private void filterOrgRef(UIRefPane refPane) {
		/* 622 */String[] filtered = BuzimsgUtil.getUserPermissionPkorgs();
		/* 623 */refPane.getRefModel().setFilterPks(filtered);
	}

	private String getBilltypecode() {

		return "JZ01";
	}

	private MsgresRegVO getSelectedMsgresRegVO() {
		/* 638 */Object obj = treeModel.getSelectedData();

		/* 640 */if ((obj != null) && ((obj instanceof MsgresRegVO))) {
			/* 641 */return (MsgresRegVO) obj;
		}
		/* 643 */return null;
	}

	public static void clearMap() {
		/* 648 */RECEIVER_DATA_MAP.clear();
		/* 649 */RCVRULE_DATA_MAP.clear();
		/* 650 */RCVRULE_DATA_DISP_MAP.clear();
	}

	/* 654 */private Popup popup = null;

	private class RcvRuleMouseMotionListener implements MouseListener,
			MouseMotionListener {
		private RcvRuleMouseMotionListener() {
		}

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			/* 667 */JTable table = (JTable) e.getSource();
			/* 668 */int col = table.columnAtPoint(e.getPoint());
			/* 669 */int row = table.rowAtPoint(e.getPoint());

			/* 671 */if ((col != -1) && (row != -1) && (col == 2)) {
				/* 673 */hidePopUpMenu();
				/* 674 */MsgresRcvConfVO confVO = (MsgresRcvConfVO) getModel()
						.getRow(row);

				/* 676 */if (isValidValue(confVO)) {
					/* 678 */UIPanel container = nc.buzimsg.util.RcvRuleTipsContainerFactory
							.getTipContainer(0, new ReceiveRuleInitDataContext(
									null, confVO.getReceivingrule()));

					/* 680 */if (container == null) {
						/* 681 */container = new UIPanel();
					}
					/* 683 */popup = PopupFactory.getSharedInstance().getPopup(
							table, container, e.getXOnScreen() + 5,
							e.getYOnScreen() + 5);
					/* 684 */popup.show();

				} else {
					/* 689 */hidePopUpMenu();
				}
			} else {
				/* 694 */hidePopUpMenu();
			}
		}

		private boolean isValidValue(MsgresRcvConfVO obj) {
			/* 701 */return BuzimsgUtil.isRealRule(obj);
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
			/* 731 */hidePopUpMenu();
		}
	}

	public void hidePopUpMenu() {
		/* 739 */if (popup != null) {
			/* 740 */popup.hide();
		}
	}

	private void initMouseMotionListener() {
		/* 745 */RcvRuleMouseMotionListener mouseListener = new RcvRuleMouseMotionListener();
		/* 746 */getBodyTable().addMouseMotionListener(mouseListener);
		/* 747 */getBodyTable().addMouseListener(mouseListener);
	}

	public void displayReceiverColumnValue() {
		/* 752 */int rowCount = getModel().getRowCount();
		/* 753 */if (rowCount <= 0) {
			/* 754 */return;
		}
		/* 756 */for (int i = 0; i < rowCount; i++) {
			/* 757 */setReceiverColumnDispValue(getRcvVOByRowIndex(i), i);
		}
	}

	private void setReceiverColumnDispValue(MsgresRcvVO[] vos, int row) {
		/* 765 */getBillCardPanel().getBillModel().setValueAt(
				BuzimsgUtil.getReceiverColumnDispText(vos), row, "receivers");
		/* 766 */RECEIVERS_MAP.put(Integer.valueOf(row),
				BuzimsgUtil.getReceiverColumnDispText(vos));
	}

	public void handleSelfRefpaneSelectionChanged(
			BuzimsgSelfDefRefpaneEvent event) {
		/* 791 */if (event.getEventType() == 1) {
			/* 793 */MsgresRcvVO[] newValue = (MsgresRcvVO[]) event
					.getNewValue();
			/* 794 */MsgresRcvVO[] oldValue = (MsgresRcvVO[]) event
					.getOldValue();
			/* 795 */int rowIndex = event.getParentRowIndex();

			/* 797 */BillEditListener editListener = getBillCardPanel()
					.getBodyPanel().getEditListener();
			/* 798 */editListener.afterEdit(new BillEditEvent(
					event.getSource(), oldValue, newValue, "receivers",
					rowIndex, 1));
		}

		/* 802 */if (event.getEventType() == 0) {
			/* 804 */ReceiveRuleInitDataContext newValue = (ReceiveRuleInitDataContext) event
					.getNewValue();
			/* 805 */ReceiveRuleInitDataContext oldValue = (ReceiveRuleInitDataContext) event
					.getOldValue();
			/* 806 */int rowIndex = event.getParentRowIndex();

			/* 808 */BillEditListener editListener = getBillCardPanel()
					.getBodyPanel().getEditListener();
			/* 809 */editListener.afterEdit(new BillEditEvent(
					event.getSource(), oldValue, newValue,
					"receivingrule_disp", rowIndex, 1));
		}
	}

	class TableColumnCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1932644063381647314L;

		TableColumnCellRenderer() {
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			/* 824 */if (column == 0) {
				/* 825 */MsgresRcvConfVO parent = (MsgresRcvConfVO) getModel()
						.getRow(row);
				/* 826 */if (parent == null)
					/* 827 */return this;
				/* 828 */String pk_group = parent.getPk_group();
				/* 829 */String pk_org = parent.getPk_org();
				/* 830 */if (pk_group.equals(pk_org)) {
					/* 831 */setValue(NCLangRes.getInstance().getStrByID(
							"msgbusitype", "ReceiverOrgModel-000000"));
				} else
					/* 833 */setValue(value);
				/* 834 */} else if (column == 1) {
				/* 836 */if ((value == null) || ("".equals(value))) {
					/* 837 */setValue(NCLangRes.getInstance().getStrByID(
							"msgbusitype", "ReceiverOrgModel-000001"));
				} else
					/* 839 */setValue(value);
			}
			/* 841 */return this;
		}
	}

	@Override
	public void setAutoShowUpEventListener(
			IAutoShowUpEventListener paramIAutoShowUpEventListener) {
		tabbedPaneAwareCompnonetDelegate.setComponentVisible(true);
		autoShowUpComponent
				.setAutoShowUpEventListener(paramIAutoShowUpEventListener);
	}

	@Override
	public void showMeUp() {
		autoShowUpComponent.showMeUp();
	}
}