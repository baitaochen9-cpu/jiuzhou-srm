package nc.ui.qc.supplierqualitystatus.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.billtemplate.IBillTemplateQry;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItemUISet;
import nc.vo.arap.pub.VOUtils;
import nc.vo.pu.supqualistatus.SupplierqualityHistoryVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.VOUtil;

public class ShowHistoryDialog extends UIDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel jpanel = null;
	protected BillCardPanel billCardPanel = null;
	private UIPanel uiPanel = null;
	private UIButton buttonOk = null;
	private UIButton buttonCancel = null;
	private final String templateID = "1001ZZ1000000003X493";
	private String nodeKey;

	public ShowHistoryDialog(Container parent, String nodeKey) {
		super(parent);
		setDefaultCloseOperation(2);
		setSize(1170, 550);
		setResizable(true);
		this.nodeKey = nodeKey;

		setTitle("变更历史");
		setContentPane(getUIDialogContentPane());
	}

	protected JPanel getUIDialogContentPane() {
		if (jpanel == null) {
			jpanel = new JPanel();
			jpanel.setLayout(new BorderLayout());

			getUIDialogContentPane().add(getBillCardPanel(), "Center");

			addListenerEvent();
			jpanel.add(getPanlCmd(), "South");
		}
		return jpanel;
	}

	public BillCardPanel getBillCardPanel() {
		if (billCardPanel == null) {
			try {
				billCardPanel = new BillCardPanel();
				IBillTemplateQry billTemplate = (IBillTemplateQry) NCLocator
						.getInstance().lookup(IBillTemplateQry.class);
				BillTempletVO tvs = billTemplate
						.findListTempletData(templateID);
				BillData billdate = new BillData(tvs);
				billCardPanel.setBillData(billdate);
				billCardPanel.setBodyMultiSelect(false);
				billCardPanel.getBillModel().setEnabled(false);

			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
		}
		return billCardPanel;
	}

	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
	}

	private UIPanel getPanlCmd() {
		if (uiPanel == null) {
			uiPanel = new UIPanel();
			uiPanel.setPreferredSize(new Dimension(0, 40));
			uiPanel.setLayout(new FlowLayout());
			uiPanel.add(getbtnOk(), getbtnOk().getName());
			uiPanel.add(getbtnCancel(), getbtnCancel().getName());
		}
		return uiPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getbtnOk()) {
			try {
				onOk();
			} catch (BusinessException e1) {
				ExceptionUtils.wrappException(e1);
			}
		} else if (e.getSource() == getbtnCancel()) {
			closeCancel();
		}
	}

	private UIButton getbtnOk() {
		if (buttonOk == null) {
			buttonOk = new UIButton();
			buttonOk.setName("btnOk");
			buttonOk.setText("确定");
		}
		return buttonOk;
	}

	private UIButton getbtnCancel() {
		if (buttonCancel == null) {
			buttonCancel = new UIButton();
			buttonCancel.setName("btnCancel");
			buttonCancel.setText("取消");
		}
		return buttonCancel;
	}

	public void onOk() throws BusinessException {
		closeOK();
	}

	public void loadData(SupplierqualityHistoryVO[] bodyvos) {
		
		String[] formulas = new String[]{"def1->getColValue(bd_supplier,code,pk_supplier,pk_supplier)",
				"def2->getColValue(bd_material,code,pk_material,pk_material)",
				"def3->getColValue(bd_defdoc,code,pk_defdoc,pk_vendor)"};
		SuperVOUtil.execFormulaWithVOs(bodyvos, formulas);
		VOUtil.sort((CircularlyAccessibleValueObject[])bodyvos, new String[]{"def1","def2","def3"},new int[]{VOUtil.ASC,VOUtil.ASC,VOUtil.ASC} );
		
		getBillCardPanel().getBillData().setBodyValueVO(bodyvos);

		// 项目主键 pk_supplier.code
		// 项目主键 pk_supplier
		// 项目主键 pk_material
		// 项目主键 pk_material.name
		// 项目主键 pk_vendor.code
		// 项目主键 pk_vendor
		// 项目主键 pk_vendor.shortname

		String[] itemkeys = { "pk_supplier.code", "pk_supplier", "pk_material",
				"pk_material.name", "pk_vendor", "pk_vendor.shortname" };

		if ("inv".equals(nodeKey)) {
			itemkeys = new String[] { "pk_material", "pk_material.name","pk_material.materialspec","pk_material.materialtype" };
		} else if ("supplier".equals(nodeKey)) {
			itemkeys = new String[] { "pk_vendor", "pk_vendor.shortname","pk_vendor.code" };
		} else if ("data".equals(nodeKey)) {
			itemkeys = new String[] { "pk_supplier.code", "pk_supplier" };
		} else {
			itemkeys = new String[0];
		}

		for (String str : itemkeys) {
			getBillCardPanel().hideBodyTableCol(str);

		}
		// getBillCardPanel().getBillModel().setSortColumn(new
		// String[]{"pk_supplier.code"});
		// getBillCardPanel().getBillModel().updateValue();
		getBillCardPanel().getBillModel().loadLoadRelationItemValue();
		getBillCardPanel().getBillModel().execLoadFormula();

	}
}
