package nc.ui.uif2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;

/**
 * 
 * @ClassName: ElectronicsignatureDialog
 * @Description: 
 * @author liyf
 * @date 2
 * @version 1.0
 */
public class ElectronicsignatureDialog extends UIDialog implements BillEditListener,
		BillEditListener2 {

	private static final long serialVersionUID = 5064747027574608058L;
	private BillCardPanel parent;
	private AbstractAppModel model;
	/** UI JPanel */
	private JPanel uiContentPane;

	/** 确定按钮 */
	private UIButton btnOK;
	/** 按钮响应事件 */
	private ActionHandler actionHandler = new ActionHandler();
	/** 按钮模板 */
	private UIPanel btnUIPanel;
	private BillCardPanel billPanel;
	/** 删行按钮 */
	private UIButton btnDelLine;
	

	public ElectronicsignatureDialog(BillCardPanel parent,AbstractAppModel model) {
		super(parent);
		this.parent = parent;
		this.model=model;
		this.initialize();
	}
	

	public void initData(ElectronicsignatureHisVO vo ,ElectronicsignatureHisVO vo1) {
		// 设置拆分界面表头数据
		if(vo != null){
			getBillPanel().setHeadItem("billmaker", vo.getBillmaker());
			getBillPanel().setHeadItem("maketime",vo.getMaketime());
			getBillPanel().setHeadItem("def1", vo.getVmemo());
		}
		
		if(vo1 != null){
			getBillPanel().setHeadItem("approver", vo1.getApprover());
			getBillPanel().setHeadItem("approvedate", vo1.getApprovedate());
			getBillPanel().setHeadItem("def2", vo1.getVmemo());
		}
		
		getBillPanel().execHeadLoadFormulas();
	}

	private void initialize() {
		this.setName("mpsSplitDialog");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("电子签名");
		int w = 700;
		int h = 200;
		this.setBounds(500, 400, w, h);
		this.setResizable(true);
		// 设置模板
		this.setContentPane(this.getUIContentPane());
		// 添加监听
		this.addActionListener();

	}

	private void addActionListener() {
		// TODO Auto-generated method stub
		this.getBtnOK().addActionListener(this.actionHandler);
		this.getBtnDelLine().addActionListener(this.actionHandler);

	}

	private Container getUIContentPane() {
		if (null == this.uiContentPane) {
			this.uiContentPane = new JPanel();
			this.uiContentPane.setName("UIDialogContentPane");
			this.uiContentPane.setLayout(new BorderLayout());
			this.getUIContentPane().add(this.getBillPanel(), "Center");
			this.getUIContentPane().add(this.getBtnUIPanel(), "South");
		}
		return this.uiContentPane;
	}

	private BillCardPanel getBillPanel() {
		// TODO Auto-generated method stub
		if (this.billPanel == null) {
			this.billPanel = new BillCardPanel();
			this.billPanel.setName("mpsSplitPanel");
//			SYSTEM	采购结算单表头(补录)	1001A110000000C8SCPW
			this.billPanel.loadTemplet("1001ZZ100000000591K6");
			this.billPanel.addEditListener(this);
			this.billPanel.addBillEditListenerHeadTail(this);
			this.billPanel.getBillTable().setSelectionMode(
					javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.billPanel.setTatolRowShow(true);
			this.billPanel.setBodyMenuShow(false);
		}
		return this.billPanel;
	}

	/**
	 * 按钮pannelgetter
	 * 
	 * @return 按钮pannel
	 */
	public UIPanel getBtnUIPanel() {
		if (this.btnUIPanel == null) {
			this.btnUIPanel = new nc.ui.pub.beans.UIPanel();
			this.btnUIPanel.setName("BtnUIPanel");
//			this.btnUIPanel.add(this.getBtnAddLine(), this.getBtnAddLine()
//					.getName());
			this.btnUIPanel.add(this.getBtnOK(), this.getBtnOK().getName());
			this.btnUIPanel.add(this.getBtnDelLine(), this.getBtnDelLine()
					.getName());
			
		}
		return this.btnUIPanel;
	}
	
	private UIButton getBtnDelLine() {
		if (null == this.btnDelLine) {
			this.btnDelLine = new UIButton();
			this.btnDelLine.setName("btnDelLine");
			this.btnDelLine.setText("取消");
		}
		return this.btnDelLine;
	}

	private UIButton getBtnOK() {
		if (null == this.btnOK) {
			this.btnOK = new UIButton();
			this.btnOK.setName("btnOK");
			this.btnOK.setText("确定");
		}
		return this.btnOK;
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return true;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		
	}

	@Override
	public void bodyRowChange(BillEditEvent e) {

	}

	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == ElectronicsignatureDialog.this.getBtnOK()) {
				// 确定按钮执行事件
				ElectronicsignatureDialog.this.doOKAction();
			}
			
			if (e.getSource() == ElectronicsignatureDialog.this.getBtnDelLine()) {
				ElectronicsignatureDialog.this.closeCancel();
			}
		}
	}

	private void doAddLineAction() {
		
	}

	private void doOKAction() {
		
		this.closeOK();
	}

}
