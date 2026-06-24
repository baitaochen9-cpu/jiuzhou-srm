package nc.ui.jzqc.labelprint.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.bd.printcheck.IPrintLog;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.ui.bd.print.printlog.Printlistenner;
import nc.ui.pub.print.IMetaDataDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction;
import nc.ui.pubapp.uif2app.model.BatchBillTableModel;
import nc.ui.pubapp.uif2app.model.HierachicalDataAppModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.editor.BillListView;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.trade.checkrule.VOChecker;
import nc.vo.uif2.LoginContext;

import org.apache.commons.lang.ArrayUtils;

/**
 * 通用标签打印按钮
 * 
 * @author zhw
 * 
 */
public class CommonLabelPrintAction extends MetaDataBasedPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1811961090975795908L;

	private ShowUpableBillForm form;

	private BillListView list;

	private PrintEntry printEntry;

	private String funcode = "H3010200";

	public CommonLabelPrintAction() {
		initPrintInfo();
	}

	public void initPrintInfo() {
		super.setCode("commLabel");
		super.setBtnName("通用打印标签");
		setActioncode("commLabel");
		setActionname("通用打印标签");
		setNodeKey("ot");
		setFuncode("H3010200");
		setPreview(false);
	}

	public void doAction(ActionEvent e) throws Exception {

		Object o = getModel().getSelectedData();
		if (o == null) {
			throw new BusinessException("请先选中数据！");
		} else {
			if (!isJzQCEnable()) {
				throw new BusinessException("九洲质量标签功能未启用，请检查库存组织参数设置！");
			}
			AggLabelPrintHVO[] hvos = getAggLabelPrintHVO();

			doPrint(hvos);
		}

	}

	private AggLabelPrintHVO[] getAggLabelPrintHVO() {
		Object o = getModel().getSelectedData();
		AggLabelPrintHVO hvo = (AggLabelPrintHVO) o;
		return new AggLabelPrintHVO[] { hvo };
	}

	protected BatchcodeVO getBatchcodeVO(String pk_batchcode)
			throws BusinessException {
		IBatchcodePubService service = NCLocator.getInstance().lookup(
				IBatchcodePubService.class);
		BatchcodeVO[] codes = service
				.queryBatchcodesByPks(new String[] { pk_batchcode });
		if (codes == null || codes.length == 0)
			return null;
		return codes[0];
	}
	
	protected BatchcodeVO getBatchcodeVO(String cmateiralvid, String batchcode)
			throws BusinessException {
		IBatchcodePubService service = NCLocator.getInstance().lookup(
				IBatchcodePubService.class);
		BatchcodeVO[] codes = service
				.queryBatchVOs(new String[]{cmateiralvid}, new String[]{batchcode});
		if (codes == null || codes.length == 0)
			return null;
		return codes[0];
	}

	protected PrintEntry getPrintEntry() {
		if (getParent() == null) {

			setParent(getModel().getContext().getEntranceUI());
		}
		printEntry = new PrintEntry(getParent(), null);
		LoginContext ctx = getModel().getContext();
		printEntry.setTemplateID(ctx.getPk_group(), getFuncode(),
				ctx.getPk_loginUser(), null, getNodeKey());

		if (getPrintListener() != null) {
			printEntry.setPrintListener(getPrintListener());
		}
		return printEntry;
	}

	protected void doPrint(Object[] datas) throws Exception {
		if (getPrintEntry().selectTemplate() != 1) {
			return;
		}
		List<IMetaDataDataSource> list = new ArrayList<IMetaDataDataSource>();
		checkDataPermission(datas);
		IMetaDataDataSource[] defaultDataSource = getDefaultMetaDataSource(datas);
		if (!VOChecker.isEmpty(defaultDataSource)) {
			for (IMetaDataDataSource dataSourceItem : defaultDataSource) {
				printEntry.setDataSource(dataSourceItem);
				printEntry.setAdjustable(isAdjustable());
				list.add(dataSourceItem);
			}
		} else {
			return;
		}

		setDefaultPrintListener((IMetaDataDataSource[]) list
				.toArray(new IMetaDataDataSource[0]));
		setPreview(true);
		if (isPreview()) {
			printEntry.preview();
		} else {
			printEntry.print();
		}
	}

	private IMetaDataDataSource[] getDefaultMetaDataSource(Object[] datas) {
		IMetaDataDataSource[] defaultDataSource = null;

		boolean isMultiTask = !(getModel() instanceof HierachicalDataAppModel);
		isMultiTask = !(getModel() instanceof BatchBillTableModel);

		if (!VOChecker.isEmpty(datas)) {
			if (isMultiTask) {
				defaultDataSource = new MetaDataSource[datas.length];
				for (int i = 0; i < defaultDataSource.length; i++) {
					defaultDataSource[i] = new MetaDataSource(
							new Object[] { datas[i] });
				}
			} else {
				defaultDataSource = new MetaDataSource[] { new MetaDataSource(
						datas) };
			}
		}
		return defaultDataSource;
	}

	protected void setDefaultPrintListener(IMetaDataDataSource[] list)
			throws BusinessException {
		if (ArrayUtils.isEmpty(list)) {
			return;
		}

		if ((getPrintListener() == null) && (isPrintLimit("H3010200"))) {
			getDefaultPrintListener();
		}
		if ((getPrintListener() != null)
				&& ((getPrintListener() instanceof Printlistenner))) {
			((Printlistenner) getPrintListener()).setDatasource(list);
			((Printlistenner) getPrintListener()).setTemplatid(null);
			((Printlistenner) getPrintListener()).setFuncode("H3010200");
		}
	}

	private boolean isPrintLimit(String funnode) throws BusinessException {
		return ((IPrintLog) NCLocator.getInstance().lookup(IPrintLog.class))
				.isAddPrintListenerByTemplatid(funnode);
	}

	private void getDefaultPrintListener() {
		setPrintListener(new LabelPrintRecordListener(getModel()));
		printEntry.setPrintListener(getPrintListener());
	}

	protected boolean isJzQCEnable() {
		boolean yf635 = false;
		AbstractBill selectedData = (AbstractBill) getModel().getSelectedData();
		if (selectedData != null) {
			try {
				yf635 = SysinitAccessor
						.getInstance()
						.getParaBoolean(
								(String) selectedData.getParentVO()
										.getAttributeValue("pk_org"), "YF636")
						.booleanValue();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return yf635;
	}

	public ShowUpableBillForm getForm() {
		return form;
	}

	public void setForm(ShowUpableBillForm form) {
		this.form = form;
	}

	public BillListView getList() {
		return list;
	}

	public void setList(BillListView list) {
		this.list = list;
	}

	public String getFuncode() {
		return funcode;
	}

	public void setFuncode(String funcode) {
		this.funcode = funcode;
	}

}