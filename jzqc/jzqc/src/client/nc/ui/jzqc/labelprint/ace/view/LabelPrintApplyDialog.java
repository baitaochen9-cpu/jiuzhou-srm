package nc.ui.jzqc.labelprint.ace.view;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIDialog;
import nc.vo.ic.pub.define.IUiSizeDef;

public class LabelPrintApplyDialog extends UIDialog {

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == LabelPrintApplyDialog.this.getJButtonOK()) {
				LabelPrintApplyDialog.this.connEtoC1(e);
			}
			if (e.getSource() == LabelPrintApplyDialog.this.getJButtonCancel()) {
				LabelPrintApplyDialog.this.connEtoC2(e);
			}
		}
	}

	class KeyListAdapter extends java.awt.event.KeyAdapter {
		@Override
		public void keyPressed(java.awt.event.KeyEvent e) {
			if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
				LabelPrintApplyDialog.this.onOK();

			}

		}
	}

	private static final long serialVersionUID = 1L;

	private JButton ivjJButtonCancel = null;

	private JButton ivjJButtonOK = null;

	private nc.ui.pub.beans.UITextArea ivjaction = null;

	private JPanel ivjUIDialogContentPane = null;
	private nc.ui.pub.beans.UILabel ivjUILabel121 = null;

	private String m_Reason;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	/**
	 * AccreditLoginDialog 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public LabelPrintApplyDialog(java.awt.Container owner) {
		super(owner);
		this.initialize();

	}

	public void onCancel() {
		this.setM_Reason(null);
		this.closeCancel();
		return;
	}

	public void onOK() {

		String sUserID = this.getAction().getText();
		if (sUserID == null || "".equals(sUserID)) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(this, "提示", "请输入原因!");
			return;
		}
		this.setM_Reason(sUserID);
		this.closeOK();
		return;
	}

	private void initialize() {
		try {
			this.setName("AccreditLoginDialog");
			this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			this.setSize(321, 250);
			this.setTitle("标签打印申请原因");
			this.setContentPane(this.getUIDialogContentPane());
			this.initConnections();

		} catch (java.lang.Throwable e) {
			this.handleException(e);
		}
	}

	protected void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			if (arg1 == null) {
				return;
			}
			this.onOK();
		} catch (java.lang.Throwable e) {
			this.handleException(e);
		}
	}

	protected void connEtoC2(java.awt.event.ActionEvent arg1) {
		try {
			if (arg1 == null) {
				return;
			}
			this.onCancel();
		} catch (java.lang.Throwable e) {
			this.handleException(e);
		}
	}

	protected javax.swing.JButton getJButtonCancel() {
		if (this.ivjJButtonCancel == null) {
			try {
				this.ivjJButtonCancel = new javax.swing.JButton();
				this.ivjJButtonCancel.setName("JButtonCancel");
				this.ivjJButtonCancel
						.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("common", "UC001-0000008")/* @res "取消" */);
				this.ivjJButtonCancel.setBounds(this.getWidth()
						- IUiSizeDef.BTN_X_CANCEL - 50, 205,
						IUiSizeDef.BTN_WIDTH, IUiSizeDef.BTN_HEIGHT);
			} catch (java.lang.Throwable e) {
				this.handleException(e);
			}
		}
		return this.ivjJButtonCancel;
	}

	protected javax.swing.JButton getJButtonOK() {
		if (this.ivjJButtonOK == null) {
			try {
				this.ivjJButtonOK = new javax.swing.JButton();
				this.ivjJButtonOK.setName("JButtonOK");
				this.ivjJButtonOK.setText(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4008001_0", "04008001-0114")/*
																 * @res "确认"
																 */);
				this.ivjJButtonOK.setBounds(this.getWidth()
						- IUiSizeDef.BTN_X_OK - 70, 205, IUiSizeDef.BTN_WIDTH,
						IUiSizeDef.BTN_HEIGHT);
				this.ivjJButtonOK.addKeyListener(new KeyListAdapter());
			} catch (java.lang.Throwable e) {
				this.handleException(e);
			}
		}
		return this.ivjJButtonOK;
	}

	public nc.ui.pub.beans.UITextArea getAction() {
		if (this.ivjaction == null) {
			try {
				this.ivjaction = new nc.ui.pub.beans.UITextArea(5,5);
				this.ivjaction.setName("ivjaction");
				this.ivjaction
						.setPreferredSize(new java.awt.Dimension(200, 120));
				this.ivjaction.setBounds(61, 52, 200, 120);
				this.ivjaction.addKeyListener(new KeyListAdapter());
				this.ivjaction.setEditable(true);
				this.ivjaction.setEnabled(true);
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjaction;
	}

	protected javax.swing.JPanel getUIDialogContentPane() {
		if (this.ivjUIDialogContentPane == null) {
			try {
				this.ivjUIDialogContentPane = new javax.swing.JPanel();
				this.ivjUIDialogContentPane.setName("UIDialogContentPane");
				this.ivjUIDialogContentPane.setLayout(null);
				this.getUIDialogContentPane().add(this.getJButtonOK(),
						this.getJButtonOK().getName());
				this.getUIDialogContentPane().add(this.getJButtonCancel(),
						this.getJButtonCancel().getName());
				this.getUIDialogContentPane().add(this.getUILabel121(),
						this.getUILabel121().getName());
				this.getUIDialogContentPane().add(this.getAction(),
						this.getAction().getName());
			} catch (java.lang.Throwable e) {
				this.handleException(e);
			}
		}
		return this.ivjUIDialogContentPane;
	}

	protected nc.ui.pub.beans.UILabel getUILabel121() {
		if (this.ivjUILabel121 == null) {
			try {
				this.ivjUILabel121 = new nc.ui.pub.beans.UILabel();
				this.ivjUILabel121.setName("UILabel121");
				this.ivjUILabel121.setPreferredSize(new java.awt.Dimension(65,
						22));
				this.ivjUILabel121.setText("申请原因");
				this.ivjUILabel121.setBounds(61, 32, 52, 22);
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjUILabel121;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
		// nc.vo.scm.pub.SCMEnv.out(exception);
	}

	@Override
	protected void hotKeyPressed(javax.swing.KeyStroke hotKey,
			java.awt.event.KeyEvent e) {

		int modifiers = hotKey.getModifiers();
		if (modifiers == 0) {
			switch (hotKey.getKeyCode()) {
			case java.awt.event.KeyEvent.VK_SPACE:
				this.onOK();
				break;
			case java.awt.event.KeyEvent.VK_ESCAPE:
				this.onCancel();
				break;

			}
		}

	}

	protected void initConnections() throws java.lang.Exception {
		this.getJButtonOK().addActionListener(this.ivjEventHandler);
		this.getJButtonCancel().addActionListener(this.ivjEventHandler);
	}

	public String getM_Reason() {
		return m_Reason;
	}

	public void setM_Reason(String m_Reason) {
		this.m_Reason = m_Reason;
	}
}