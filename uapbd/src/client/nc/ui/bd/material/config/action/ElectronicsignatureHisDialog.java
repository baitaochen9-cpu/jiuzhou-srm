package nc.ui.bd.material.config.action;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import nc.bs.busilog.vo.BusinessLogESVO;
import nc.bs.busilog.vo.BusinessLogVO;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.pub.BusinessException;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;

/**
 * 
 * @ClassName: ElectronicsignatureDialog
 * @Description:
 * @author liyf
 * @date 2
 * @version 1.0
 */
public class ElectronicsignatureHisDialog extends UIDialog implements
		BillEditListener, BillEditListener2 {

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
	private BillListPanel billPanel;
	private BusiLogDetailPanel bean;
	/** 删行按钮 */
	private UIButton btnDelLine;

	public ElectronicsignatureHisDialog(BillCardPanel parent,
			AbstractAppModel model) {
		super(parent);
		this.parent = parent;
		this.model = model;
		this.initialize();
	}

	public void initData(ElectronicsignatureHisVO[] bodyVOs) {
		// 设置拆分界面表头数据
		getBillPanel().getHeadBillModel().setBodyDataVO(bodyVOs);
		getBillPanel().getHeadBillModel().execLoadFormula();
	}

	private void initialize() {
		this.setName("mpsSplitDialog");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("电子签名记录");
		int w = 710;
		int h = 500;
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

	private BillListPanel getBillPanel() {
		// TODO Auto-generated method stub
		if (this.billPanel == null) {
			this.billPanel = new BillListPanel();
			this.billPanel.setName("mpsSplitPanel");
			// SYSTEM 采购结算单表头(补录) 1001A110000000C8SCPW
			this.billPanel.loadTemplet("1001AA100000000D6SM1");
			this.billPanel.addEditListener(this);
			bean = new BusiLogDetailPanel();
			this.billPanel.getBodyUIPanel().add(bean);
			this.billPanel.getBodyUIPanel().setVisible(true);
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
			// this.btnUIPanel.add(this.getBtnAddLine(), this.getBtnAddLine()
			// .getName());
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
		int row = e.getRow();
		String pk_hises = (String) getBillPanel().getHeadBillModel()
				.getValueAt(row, "id");
		setDetail(pk_hises);
	}

	private void setDetail(String pk_hises) {
		try {
			String strWhere = " nvl(dr,0) = 0 and pk_hises = '" + pk_hises
					+ "' ";
			BusinessLogESVO[] vos = (BusinessLogESVO[]) HYPubBO_Client
					.queryByCondition(BusinessLogESVO.class, strWhere);

			if (vos != null && vos.length > 0) {
				BusinessLogVO vo = new BusinessLogVO();
				vo.setLogmsg(vos[0].getLogmsg());
				vo.setTypepk_busiobj(vos[0].getTypepk_busiobj());
				bean.setDetail(vo);
			}else{
				bean.clearDetail();
			}

		} catch (BusinessException e) {
			Logger.error("解析xml出错");
			bean.clearDetail();
		}
	}

	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == ElectronicsignatureHisDialog.this.getBtnOK()) {
				// 确定按钮执行事件
				ElectronicsignatureHisDialog.this.doOKAction();
			}

			if (e.getSource() == ElectronicsignatureHisDialog.this
					.getBtnDelLine()) {
				ElectronicsignatureHisDialog.this.closeCancel();
			}
		}
	}

	private void doAddLineAction() {

	}

	private void doOKAction() {

		this.closeOK();
	}

}
