package nc.ui.ic.location.ref;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.m4n.ITransformMaitain;
import nc.ui.pub.beans.UIDialog;
import nc.vo.framework.rsa.Encode;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.ic.pub.define.IUiSizeDef;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

/**
 * <p>
 * <b>单品参照对话框：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author yangb
 * @time 2010-4-9 下午03:01:52
 */
public class LocationSplitInDlg extends UIDialog {

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == LocationSplitInDlg.this.getJButtonOK()) {
				LocationSplitInDlg.this.connEtoC1(e);
			}
			if (e.getSource() == LocationSplitInDlg.this.getJButtonCancel()) {
				LocationSplitInDlg.this.connEtoC2(e);
			}
		}
	}

	class KeyListAdapter extends java.awt.event.KeyAdapter {
		@Override
		public void keyPressed(java.awt.event.KeyEvent e) {
			if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
				LocationSplitInDlg.this.onOK();
			}
		}
	}

	private static final long serialVersionUID = 1L;

	private JButton ivjJButtonCancel = null;

	private JButton ivjJButtonOK = null;

	private nc.ui.pub.beans.UITextField ivjjpassword = null;

	private nc.ui.pub.beans.UITextField ivjTfUser = null;

	private nc.ui.pub.beans.UITextField ivjaction = null;

	private JPanel ivjUIDialogContentPane = null;

	private nc.ui.pub.beans.UILabel ivjUILabel1 = null;
	private nc.ui.pub.beans.UILabel ivjUILabel121 = null;

	private nc.ui.pub.beans.UILabel ivjUILabel11 = null;

	private Encode m_Encode;

	private String m_UserID;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private OnhandSNViewVO datavo;
	private String fenshu;
	private String shuliang;

	/**
	 * LocationSplitInDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public LocationSplitInDlg() {
		super();
		this.initialize();

	}

	/**
	 * LocationSplitInDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public LocationSplitInDlg(java.awt.Container parent, String title) {
		super(parent, title);
		this.initialize();

	}

	/**
	 * LocationSplitInDlg 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public LocationSplitInDlg(java.awt.Frame owner) {
		super(owner);
		this.initialize();

	}

	/**
	 * LocationSplitInDlg 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public LocationSplitInDlg(java.awt.Frame owner, String title) {
		super(owner, title);
		this.initialize();

	}

	public Encode getEncode() {
		if (this.m_Encode == null) {
			this.m_Encode = new Encode();
		}
		return this.m_Encode;
	}

	public String getUserID() {
		return this.m_UserID;
	}

	public void onCancel() {
		this.closeCancel();
		return;
	}

	public void onOK() {
		String xlh = this.getAction().getText();
		String zjh = this.getTfUser().getText();

		String sl = this.getjpassword().getText();

		if (StringUtil.isEmpty(sl)) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(this, "提示", "数量不能为空");
			return;
		}

		if (StringUtil.isEmpty(zjh)) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(this, "提示", "追加不能为空");
			return;
		}

		try {
			OnhandSNViewVO datavo = this.getDatavo();
			List<OnhandSNViewVO> list = getSplitVOs(datavo,zjh,sl);
			ITransformMaitain server = NCLocator.getInstance().lookup(
					ITransformMaitain.class);
			server.pushSaveIn(datavo,
					list.toArray(new OnhandSNViewVO[list.size()]));
		} catch (Exception e1) {
			e1.printStackTrace();

			nc.ui.pub.beans.MessageDialog.showHintDlg(this, "提示",
					"拆分失败" + e1.getMessage());
			return;
		}
		this.closeOK();
		return;
	}

	private List<OnhandSNViewVO> getSplitVOs(OnhandSNViewVO datavo,String zjh,String sl) {
		List<OnhandSNViewVO> list = new ArrayList<>();
		// 如果是分数 平均拆分后 余数新增一行
		UFDouble shuliang = new UFDouble(sl);
		
		UFDouble temp = new UFDouble(shuliang).setScale(2,
				UFDouble.ROUND_HALF_UP);
		OnhandSNViewVO vo = (OnhandSNViewVO) datavo.clone();
		vo.setNonhandnum(new UFDouble(temp));
		vo.setVsncode(datavo.getVsncode() + "-" + (zjh));
		list.add(vo);
		
		return list;
	}

	private void initialize() {
		try {
			this.setName("LocationSplitInDlg");
			this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			this.setSize(321, 320);
			this.setTitle("称量差异入库");
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
						- IUiSizeDef.BTN_X_CANCEL - 50, 265,
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
						- IUiSizeDef.BTN_X_OK - 70, 265, IUiSizeDef.BTN_WIDTH,
						IUiSizeDef.BTN_HEIGHT);
				this.ivjJButtonOK.addKeyListener(new KeyListAdapter());
			} catch (java.lang.Throwable e) {
				this.handleException(e);
			}
		}
		return this.ivjJButtonOK;
	}

	protected nc.ui.pub.beans.UITextField getjpassword() {
		if (this.ivjjpassword == null) {
			try {
				this.ivjjpassword = new nc.ui.pub.beans.UITextField();
				this.ivjjpassword.setName("jpassword");
				this.ivjjpassword.setBounds(130, 156, 140, 20);
				this.ivjjpassword.addKeyListener(new KeyListAdapter());
				this.ivjjpassword.setTextType("TextDbl");
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjjpassword;
	}

	public nc.ui.pub.beans.UITextField getTfUser() {
		if (this.ivjTfUser == null) {
			try {
				this.ivjTfUser = new nc.ui.pub.beans.UITextField();
				this.ivjTfUser.setName("TfUser");
				this.ivjTfUser
						.setPreferredSize(new java.awt.Dimension(100, 20));
				this.ivjTfUser.setBounds(130, 104, 140, 20);
				this.ivjTfUser.addKeyListener(new KeyListAdapter());
				this.ivjTfUser.setTextType("TextStr");
				this.ivjTfUser.setMaxLength(2);
				this.ivjTfUser.setValue("-1");
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjTfUser;
	}

	public nc.ui.pub.beans.UITextField getAction() {
		if (this.ivjaction == null) {
			try {
				this.ivjaction = new nc.ui.pub.beans.UITextField();
				this.ivjaction.setName("ivjaction");
				this.ivjaction
						.setPreferredSize(new java.awt.Dimension(100, 20));
				this.ivjaction.setBounds(130, 52, 140, 20);
				this.ivjaction.setTextType("TextStr");
				this.ivjaction.setEnabled(false);
				this.ivjaction.addKeyListener(new KeyListAdapter());
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
				this.getUIDialogContentPane().add(this.getUILabel11(),
						this.getUILabel11().getName());
				this.getUIDialogContentPane().add(this.getUILabel1(),
						this.getUILabel1().getName());
				this.getUIDialogContentPane().add(this.getAction(),
						this.getAction().getName());
				this.getUIDialogContentPane().add(this.getjpassword(),
						this.getjpassword().getName());
				this.getUIDialogContentPane().add(this.getTfUser(),
						this.getTfUser().getName());
			} catch (java.lang.Throwable e) {
				this.handleException(e);
			}
		}
		return this.ivjUIDialogContentPane;
	}

	protected nc.ui.pub.beans.UILabel getUILabel1() {
		if (this.ivjUILabel1 == null) {
			try {
				this.ivjUILabel1 = new nc.ui.pub.beans.UILabel();
				this.ivjUILabel1.setName("UILabel1");
				this.ivjUILabel1.setPreferredSize(new java.awt.Dimension(165,
						22));
				this.ivjUILabel1.setText("数量");
				this.ivjUILabel1.setBounds(61, 156, 90, 22);
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjUILabel1;
	}

	protected nc.ui.pub.beans.UILabel getUILabel121() {
		if (this.ivjUILabel121 == null) {
			try {
				this.ivjUILabel121 = new nc.ui.pub.beans.UILabel();
				this.ivjUILabel121.setName("UILabel121");
				this.ivjUILabel121.setPreferredSize(new java.awt.Dimension(165,
						22));
				this.ivjUILabel121.setText("原序列号");
				this.ivjUILabel121.setBounds(61, 52, 90, 22);
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjUILabel121;
	}

	protected nc.ui.pub.beans.UILabel getUILabel11() {
		if (this.ivjUILabel11 == null) {
			try {
				this.ivjUILabel11 = new nc.ui.pub.beans.UILabel();
				this.ivjUILabel11.setName("UILabel11");
				this.ivjUILabel11.setPreferredSize(new java.awt.Dimension(65,
						22));
				this.ivjUILabel11.setText("追加");
				this.ivjUILabel11.setBounds(61, 104, 52, 22);
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjUILabel11;
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

	public OnhandSNViewVO getDatavo() {
		return datavo;
	}

	public void setDatavo(OnhandSNViewVO datavo) {
		this.datavo = datavo;
	}

	public String getFenshu() {
		return fenshu;
	}

	public void setFenshu(String fenshu) {
		this.fenshu = fenshu;
	}

	public String getShuliang() {
		return shuliang;
	}

	public void setShuliang(String shuliang) {
		this.shuliang = shuliang;
	}

}
