package nc.ui.uif2;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.riasm.IElectronicsignatureMaintain;
import nc.sfbase.client.CheckDevice;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.framework.rsa.Encode;
import nc.vo.ic.pub.define.IUiSizeDef;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.operatelog.OperateLogVO;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.FuncSubInfo;
import nc.vo.uap.rbac.util.RbacUserPwdUtil;
import nc.vo.uif2.LoginContext;

/**
 * <p>
 * <b>条码权限检查对话框：</b>
 * <ul>
 * 权限检查不通过时，可能通过此对话框用有权限用户进行授权。
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author liujq
 * @time 2010-4-7 上午11:52:49
 */
public class AccreditLoginDialog extends UIDialog {

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == AccreditLoginDialog.this.getJButtonOK()) {
				AccreditLoginDialog.this.connEtoC1(e);
			}
			if (e.getSource() == AccreditLoginDialog.this.getJButtonCancel()) {
				AccreditLoginDialog.this.connEtoC2(e);
			}
		}
	}

	class KeyListAdapter extends java.awt.event.KeyAdapter {
		@Override
		public void keyPressed(java.awt.event.KeyEvent e) {
			if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
				AccreditLoginDialog.this.onOK();

			}

		}
	}

	private static final long serialVersionUID = 1L;

	private JButton ivjJButtonCancel = null;

	private JButton ivjJButtonOK = null;

	private javax.swing.JPasswordField ivjjpassword = null;

	private nc.ui.pub.beans.UITextField ivjTfUser = null;

	private nc.ui.pub.beans.UITextField ivjaction = null;

	private nc.ui.pub.beans.UITextField ivjvmemo = null;

	private JPanel ivjUIDialogContentPane = null;

	private nc.ui.pub.beans.UILabel ivjUILabel1 = null;
	private nc.ui.pub.beans.UILabel ivjUILabel121 = null;
	private nc.ui.pub.beans.UILabel ivjUILabel122 = null;

	private nc.ui.pub.beans.UILabel ivjUILabel11 = null;

	private Encode m_Encode;

	private String m_UserID;
	private String pk;
	private String billid;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	private LoginContext context;
	private String cmd;
	private boolean ismust =false;

	/**
	 * AccreditLoginDialog 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public AccreditLoginDialog(java.awt.Container parent, LoginContext context,
			String cmd, String billid) {
		super(parent);
		this.initialize();
		this.context = context;
		this.cmd = cmd;
		this.billid = billid;

	}
	public AccreditLoginDialog(java.awt.Container parent, LoginContext context,
			String cmd, String billid,boolean ismust) {
		super(parent);
		this.initialize();
		this.context = context;
		this.cmd = cmd;
		this.billid = billid;
		this.ismust = ismust;
	}

	/**
	 * AccreditLoginDialog 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public AccreditLoginDialog(java.awt.Container parent, String title) {
		super(parent, title);
		this.initialize();

	}

	/**
	 * AccreditLoginDialog 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public AccreditLoginDialog(java.awt.Frame owner) {
		super(owner);
		this.initialize();

	}

	/**
	 * AccreditLoginDialog 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public AccreditLoginDialog(java.awt.Frame owner, String title) {
		super(owner, title);
		this.initialize();

	}

	public static nc.vo.sm.UserVO findUser(String usercode, String pwd)
			throws Exception {
		IElectronicsignatureMaintain service = NCLocator.getInstance().lookup(
				IElectronicsignatureMaintain.class);
		UserVO user = service.findUserByCode(usercode);
		if (user != null) {
			if (RbacUserPwdUtil.checkUserPassword(user, pwd)) {// 身份合法
				return user;

			} else {// 密码错误，身份不合法.
				return null;
			}
		} else { // 说明用户名称错误
			return null;
		}
	}

	public void clearPassWord() {
		this.getjpassword().setText(null);
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
		this.setUserID(null);
		this.closeCancel();
		return;
	}

	public void onOK() {

		String sUserID = this.getTfUser().getText();
		String sPwd = String.valueOf(this.getjpassword().getPassword());
		if (sUserID == null) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(
					this,
					"提示",
					 "请输入用户名!");
			return;
		}
		try {
			// sPwd = this.getEncode().encode(sPwd);
			nc.vo.sm.UserVO uservo = AccreditLoginDialog
					.findUser(sUserID, sPwd);
			if (uservo == null || uservo.getPrimaryKey() == null) {
				nc.ui.pub.beans.MessageDialog.showHintDlg(this,	"提示",	"用户密码不正确!");
				return;
			}
			this.setUserID(uservo.getPrimaryKey());
			
				if(ismust){
					String memo = this.getVmemo().getText();
					if(StringUtil.isEmpty(memo)){
						nc.ui.pub.beans.MessageDialog.showHintDlg(this,	"提示",	"备注必输!");
						return;
					}
				}
		} catch (Exception e) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(
					this,	"提示","检验用户名密码出现错误："+e.toString());
			return;
		}
		try {
			insertButtonLog(this.context, this.cmd);
			insertElectronicsignatureHisVO(this.context, this.cmd);
		} catch (Exception e) {
			nc.ui.pub.beans.MessageDialog.showHintDlg(
					this,	"提示","生成签名日志错误"+e.toString());
			return;
		}
		
		this.closeOK();
		return;
	}

	private void insertButtonLog(LoginContext context, String cmd) throws UifException {

		WorkbenchEnvironment env = WorkbenchEnvironment.getInstance();
		long time = System.currentTimeMillis();
		OperateLogVO logVO = new OperateLogVO();
		// 设备，入口
		logVO.setDevice(CheckDevice.check() + "");
		logVO.setLogintype(CheckDevice.checkLoginType() + "");
		logVO.setType(OperateLogVO.ENTER_NODEBUTTON);
		logVO.setButtonname(cmd);

		FuncSubInfo frVO = context.getFuncInfo();
		logVO.setPk_funcnode(frVO.getFun_id());
		logVO.setFunccode(frVO.getFuncode());
		logVO.setFuncname(frVO.getEntityName());
		logVO.setIp(env.getSession().getClientHostIP());
		logVO.setLogdate(new UFDate(time));
		logVO.setLogtime(new UFTime(time));
		if (env.getGroupVO() != null) {
			logVO.setPk_group(env.getGroupVO().getPrimaryKey());
		}
		logVO.setPk_user(env.getLoginUser().getPrimaryKey());
		logVO.setUser_name(env.getLoginUser().getUser_name());
		logVO.setUsertype(env.getLoginUser().getUser_type());
		logVO.setDetail(getVmemo().getText());
		// / insert log
		// ButtonLogCacheManage.getInstance().addLog(logVO);
		 HYPubBO_Client.insert(logVO);
//		IOperatelogService service = NCLocator.getInstance().lookup(
//				IOperatelogService.class);
//		service.insertVO(logVO);
	
	}

	private void insertElectronicsignatureHisVO(LoginContext context, String cmd) throws UifException {
		WorkbenchEnvironment env = WorkbenchEnvironment.getInstance();
		long time = System.currentTimeMillis();
		ElectronicsignatureHisVO logVO = new ElectronicsignatureHisVO();
		FuncSubInfo frVO = context.getFuncInfo();
		logVO.setVbtnname(cmd);
		logVO.setCfuncode(frVO.getFuncode());
		logVO.setTranstype("EHIS");
		logVO.setTranstypepk("EHIS");
		if (env.getGroupVO() != null) {
			logVO.setPk_group(env.getGroupVO().getPrimaryKey());
		}
		logVO.setPk_org(context.getPk_org());
		logVO.setVmemo(getVmemo().getText());
		if (cmd.contains("保存")) {
			logVO.setBillmaker(env.getLoginUser().getPrimaryKey());
			logVO.setMaketime(new UFDateTime(time));
		} else {
			logVO.setApprover(env.getLoginUser().getPrimaryKey());
			logVO.setApprovedate(new UFDateTime(time));
			logVO.setMaketime(new UFDateTime(time));
		}
		
		if (StringUtil.isEmpty(this.billid)) {
			logVO.setBillid("~");
		} else {
			logVO.setBillid("~");
//			logVO.setBillid(this.billid);
		}
		logVO.setTs(new UFDateTime(time));
		String npks = HYPubBO_Client.insert(logVO);
		this.setPk(npks);
	
	}

	public void setEncode(Encode newEncode) {
		this.m_Encode = newEncode;
	}

	public void setUserID(String newUserID) {
		this.m_UserID = newUserID;
	}

	private void initialize() {
		try {
			this.setName("AccreditLoginDialog");
			this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			this.setSize(321, 320);
			this.setTitle("电子签名");
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

	protected javax.swing.JPasswordField getjpassword() {
		if (this.ivjjpassword == null) {
			try {
				this.ivjjpassword = new javax.swing.JPasswordField();
				this.ivjjpassword.setName("jpassword");
				this.ivjjpassword.setBounds(133, 156, 100, 20);
				this.ivjjpassword.addKeyListener(new KeyListAdapter());
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
				this.ivjTfUser.setBounds(133, 104, 100, 20);
				this.ivjTfUser.setEditable(false);
				this.ivjTfUser.setEnabled(false);
				this.ivjTfUser.addKeyListener(new KeyListAdapter());
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjTfUser;
	}

	public nc.ui.pub.beans.UITextField getVmemo() {
		if (this.ivjvmemo == null) {
			try {
				this.ivjvmemo = new nc.ui.pub.beans.UITextField();
				this.ivjvmemo.setName("ivjvmemo");
				this.ivjvmemo.setPreferredSize(new java.awt.Dimension(100, 20));
				this.ivjvmemo.setBounds(133, 208, 100, 20);
				this.ivjvmemo.addKeyListener(new KeyListAdapter());
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjvmemo;
	}

	public nc.ui.pub.beans.UITextField getAction() {
		if (this.ivjaction == null) {
			try {
				this.ivjaction = new nc.ui.pub.beans.UITextField();
				this.ivjaction.setName("ivjaction");
				this.ivjaction
						.setPreferredSize(new java.awt.Dimension(100, 20));
				this.ivjaction.setBounds(133, 52, 100, 20);
				this.ivjaction.addKeyListener(new KeyListAdapter());
				this.ivjaction.setEditable(false);
				this.ivjaction.setEnabled(false);
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
				this.getUIDialogContentPane().add(this.getUILabel122(),
						this.getUILabel122().getName());
				this.getUIDialogContentPane().add(this.getAction(),
						this.getAction().getName());
				this.getUIDialogContentPane().add(this.getjpassword(),
						this.getjpassword().getName());
				this.getUIDialogContentPane().add(this.getTfUser(),
						this.getTfUser().getName());
				this.getUIDialogContentPane().add(this.getVmemo(),
						this.getVmemo().getName());
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
				this.ivjUILabel1
						.setPreferredSize(new java.awt.Dimension(65, 22));
				this.ivjUILabel1.setText(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4008001_0", "04008001-0115")/*
																 * @res "用户密码"
																 */);
				this.ivjUILabel1.setBounds(61, 156, 52, 22);
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjUILabel1;
	}

	protected nc.ui.pub.beans.UILabel getUILabel122() {
		if (this.ivjUILabel122 == null) {
			try {
				this.ivjUILabel122 = new nc.ui.pub.beans.UILabel();
				this.ivjUILabel122.setName("UILabel122");
				this.ivjUILabel122.setPreferredSize(new java.awt.Dimension(65,
						22));
				this.ivjUILabel122.setText("备注");
				this.ivjUILabel122.setBounds(61, 208, 52, 22);
			} catch (java.lang.Throwable ivjExc) {
				this.handleException(ivjExc);
			}
		}
		return this.ivjUILabel122;
	}

	protected nc.ui.pub.beans.UILabel getUILabel121() {
		if (this.ivjUILabel121 == null) {
			try {
				this.ivjUILabel121 = new nc.ui.pub.beans.UILabel();
				this.ivjUILabel121.setName("UILabel121");
				this.ivjUILabel121.setPreferredSize(new java.awt.Dimension(65,
						22));
				this.ivjUILabel121.setText("操作");
				this.ivjUILabel121.setBounds(61, 52, 52, 22);
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
				this.ivjUILabel11.setText(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4008001_0", "04008001-0116")/*
																 * @res "授权用户"
																 */);
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

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

}