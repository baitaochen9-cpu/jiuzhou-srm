/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */package nc.ui.pub.beans;
/*     */
/*     */import java.awt.BorderLayout;
/*     */
import java.awt.Color;
/*     */
import java.awt.Component;
/*     */
import java.awt.Container;
/*     */
import java.awt.Dialog;
/*     */
import java.awt.Dimension;
/*     */
import java.awt.Frame;
/*     */
import java.awt.KeyboardFocusManager;
/*     */
import java.awt.LayoutManager;
/*     */
import java.awt.Toolkit;
/*     */
import java.awt.Window;
/*     */
import java.awt.event.ActionEvent;
/*     */
import java.awt.event.KeyEvent;
/*     */
import java.awt.event.KeyListener;
/*     */
import java.awt.event.WindowEvent;
/*     */
import java.awt.event.WindowListener;
/*     */
import java.util.HashSet;
/*     */
import javax.swing.AbstractAction;
/*     */
import javax.swing.ActionMap;
/*     */
import javax.swing.InputMap;
/*     */
import javax.swing.JComponent;
/*     */
import javax.swing.JDialog;
/*     */
import javax.swing.JOptionPane;
/*     */
import javax.swing.JPanel;
/*     */
import javax.swing.JRootPane;
/*     */
import javax.swing.KeyStroke;
/*     */
import javax.swing.SwingUtilities;
/*     */
import javax.swing.event.EventListenerList;
/*     */
import nc.bs.logging.Logger;
/*     */
import nc.ui.plaf.basic.UIDialogRootPaneUI;
/*     */
import nc.ui.pub.beans.bill.IBillCardPanelKeyListener;
/*     */
import nc.ui.pub.beans.util.MiscUtils;
/*     */
import nc.uitheme.ui.ThemeResourceCenter;
/*     */
/*     */public class UIDialog extends JDialog
/*     */implements WindowListener, IResetableDialog, KeyListener
/*     */{
	/*     */protected transient EventListenerList m_listenerList;
	/*  46 */private static Color dlgContentBGColor = ThemeResourceCenter
			.getInstance().getColor("themeres/dialog/dialogResConf",
					"dialogContentPane.backgroundColor");
	/*     */private JPanel ivjJDialogContentPane;
	/*     */public static final int ID_OK = 1;
	/*     */public static final int ID_CANCEL = 2;
	/*     */public static final int ID_YES = 4;
	/*     */public static final int ID_NO = 8;
	/*     */private boolean isResetable;
	/*     */private int m_nResult;
	/*  69 */protected static HashSet m_allSingleHotKeys = null;
	/*     */private transient Component comp;
	/*     */
	/*     */
	/*     */public UIDialog()
	/*     */{
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 105 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Container parent)
	/*     */{
		/* 117 */this(getTopWindow(parent));
		/*     */}
	/*     */
	/*     */public UIDialog(Container parent, boolean reset)
	/*     */{
		/* 123 */this(getTopWindow(parent), reset);
		/*     */}
	/*     */
	/*     */public UIDialog(Container parent, String title)
	/*     */{
		/* 137 */this(getTopWindow(parent), title);
		/*     */}
	/*     */
	/*     */public UIDialog(Container parent, String title, boolean reset)
	/*     */{
		/* 142 */this(getTopWindow(parent), title, reset);
		/*     */}
	/*     */
	/*     */public UIDialog(Frame owner)
	/*     */{
		/* 153 */super(owner);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 154 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Frame owner, boolean reset)
	/*     */{
		/* 159 */super(owner);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 160 */this.isResetable = reset;
		/* 161 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Frame owner, String title)
	/*     */{
		/* 173 */super(owner, title);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 174 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Frame owner, String title, boolean reset)
	/*     */{
		/* 179 */super(owner, title);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 180 */this.isResetable = reset;
		/* 181 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Dialog owner, String title) {
		/* 185 */super(owner, title);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 186 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Dialog owner, String title, boolean reset) {
		/* 190 */super(owner, title);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 191 */this.isResetable = reset;
		/* 192 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Dialog owner) {
		/* 196 */super(owner);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 197 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Dialog owner, boolean reset) {
		/* 201 */super(owner);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 202 */this.isResetable = reset;
		/* 203 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Window owner, String title) {
		/* 207 */super(owner, title);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 208 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Window owner, String title, boolean reset) {
		/* 212 */super(owner, title);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 213 */this.isResetable = reset;
		/* 214 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Window owner) {
		/* 218 */super(owner);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 219 */initialize();
		/*     */}
	/*     */
	/*     */public UIDialog(Window owner, boolean reset)
	/*     */{
		/* 224 */super(owner);
		/*     */
		/*  45 */this.m_listenerList = new EventListenerList();
		/*     */
		/*  52 */this.ivjJDialogContentPane = null;
		/*     */
		/*  63 */this.isResetable = false;
		/*     */
		/*  65 */this.m_nResult = 0;
		/*     */
		/* 579 */this.comp = null;
		/*     */
		/* 225 */this.isResetable = reset;
		/* 226 */initialize();
		/*     */}
	/*     */
	/*     */private static Window getTopWindow(Container parentContainer) {
		/* 230 */Container parent = parentContainer;
		/*     */
		/* 232 */while ((parent != null) && (!(parent instanceof Dialog))
				&& (!(parent instanceof Frame))) {
			/* 233 */parent = parent.getParent();
			/*     */}
		/* 235 */if (parent == null) {
			/* 236 */parent = JOptionPane
					.getFrameForComponent(parentContainer);
			/*     */}
		/* 238 */return ((Window) parent);
		/*     */}
	/*     */
	/*     */public void addUIDialogListener(UIDialogListener newListener)
	/*     */{
		/* 248 */this.m_listenerList.add(UIDialogListener.class, newListener);
		/*     */}
	/*     */
	/*     */protected void close()
	/*     */{
		/* 256 */if (!(isShowing()))
			/* 257 */return;
		/* 258 */setVisible(false);
		/* 259 */if ((isModal()) && (getDefaultCloseOperation() == 2))
			/* 260 */destroy();
		/*     */}
	/*     */
	/*     */public void closeCancel()
	/*     */{
		/* 268 */setResult(2);
		/* 269 */close();
		/* 270 */fireUIDialogClosed(new UIDialogEvent(this, 202));
		/*     */}
	/*     */
	/*     */public void closeOK()
	/*     */{
		/* 278 */setResult(1);
		/* 279 */close();
		/* 280 */fireUIDialogClosed(new UIDialogEvent(this, 204));
		/*     */}
	/*     */
	/*     */public void destroy()
	/*     */{
		/* 288 */dispose();
		/*     */}
	/*     */
	/*     */protected void fireUIDialogClosed(UIDialogEvent event)
	/*     */{
		/* 303 */Object[] listeners = this.m_listenerList.getListenerList();
		/* 304 */for (int i = listeners.length - 2; i >= 0; i -= 2)
			/* 305 */if (listeners[i] == UIDialogListener.class)
				/* 306 */((UIDialogListener) listeners[(i + 1)])
						.dialogClosed(event);
		/*     */}
	/*     */
	/*     */public boolean isResetable()
	/*     */{
		/* 315 */return this.isResetable;
		/*     */}
	/*     */
	/*     */public void setReset(boolean ReSet) {
		/* 319 */this.isResetable = ReSet;
		/* 320 */setUIDialogUndecorationStyle(this);
		/*     */}
	/*     */
	/*     */private JPanel getJDialogContentPane()
	/*     */{
		/* 353 */if (this.ivjJDialogContentPane == null) {
			/* 354 */this.ivjJDialogContentPane = new JPanel();
			/* 355 */this.ivjJDialogContentPane.setName("JDialogContentPane");
			/* 356 */this.ivjJDialogContentPane.setLayout(new BorderLayout());
			/*     */}
		/* 358 */return this.ivjJDialogContentPane;
		/*     */}
	/*     */
	/*     */public int getResult()
	/*     */{
		/* 367 */return this.m_nResult;
		/*     */}
	/*     */
	/*     */public String getTitle()
	/*     */{
		/* 377 */return super.getTitle();
		/*     */}
	/*     */
	/*     */protected void hotKeyPressed(KeyStroke hotKey, KeyEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */private void initConnections()
	/*     */{
		/* 420 */addWindowListener(this);
		/* 421 */addKeyListener(this);
		/*     */}
	/*     */
	/*     */private void initialize()
	/*     */{
		/* 431 */setName("UIDialog");
		/* 432 */setDefaultCloseOperation(2);
		/* 433 */setSize(400, 240);
		/* 434 */setModal(true);
		/* 435 */setContentPane(getJDialogContentPane());
		/* 436 */initConnections();
		/* 437 */setResizable(false);
		/*     */
		/* 439 */setUIDialogUndecorationStyle(this);
		/*     */}
	/*     */
	/*     */public static void setUIDialogUndecorationStyle(JDialog dlg)
	/*     */{
		/* 449 */dlg.setUndecorated(true);
		/* 450 */dlg.getRootPane().setWindowDecorationStyle(2);
		/* 451 */UIDialogRootPaneUI uiRootUI = new UIDialogRootPaneUI();
		/* 452 */if (dlg instanceof IResetableDialog)
		/*     */{
			/* 454 */uiRootUI.setReset(((IResetableDialog) dlg).isResetable());
			/*     */}
		/*     */
		/* 461 */dlg.getRootPane().setUI(uiRootUI);
		/*     */}
	/*     */
	/*     */protected boolean isSingleHotKey(int keyCode)
	/*     */{
		/* 474 */return m_allSingleHotKeys.contains(Integer.valueOf(keyCode));
		/*     */}
	/*     */
	/*     */public void keyPressed(KeyEvent e) {
		/* 478 */int keyCode = e.getKeyCode();
		/* 479 */int modifiers = e.getModifiers();
		/* 480 */if (keyCode == 27)
			/* 481 */closeCancel();
		/* 482 */if ((keyCode == 16) || (keyCode == 17) || (keyCode == 18))
			/* 483 */return;
		/* 484 */if ((modifiers > 1) || (isSingleHotKey(keyCode))) {
			/* 485 */hotKeyPressed(KeyStroke.getKeyStroke(keyCode, modifiers),
					e);
			/*     */}
		/*     */
		/* 488 */processBillHotKeyEvent(e);
		/*     */}
	/*     */
	/*     */public void keyReleased(KeyEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */public void keyTyped(KeyEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */public void removeUIDialogListener(UIDialogListener newListener)
	/*     */{
		/* 506 */this.m_listenerList.remove(UIDialogListener.class,
				newListener);
		/*     */}
	/*     */
	/*     */protected void reportException(Exception e)
	/*     */{
		/* 518 */Logger.debug(e);
		/*     */}
	/*     */
	/*     */
	/*     */public void setParent(Container parent)
	/*     */{
		/*     */}
	/*     */
	/*     */protected void setResult(int n)
	/*     */{
		/* 540 */this.m_nResult = n;
		/*     */}
	/*     */
	/*     */public void setTitle(String s)
	/*     */{
		/* 552 */super.setTitle(s);
		/*     */}
	/*     */
	/*     */public int showModal()
	/*     */{
		/* 564 */setModal(true);
		/*     */
		/* 566 */if (!(isShowing()))
		/*     */{
			/* 568 */Dimension screan = Toolkit.getDefaultToolkit()
					.getScreenSize();
			/* 569 */Dimension dlgsize = getSize();
			/* 570 */setLocation((screan.width - dlgsize.width) / 2,
					(screan.height - dlgsize.height) / 2);
			/*     */
			/* 574 */show();
			/*     */}
		/* 576 */return getResult();
		/*     */}
	/*     */
	/*     */public void show()
	/*     */{
		/* 585 */this.comp = KeyboardFocusManager
				.getCurrentKeyboardFocusManager().getFocusOwner();
		/*     */
		/* 587 */super.show();
		/*     */}
	/*     */
	/*     */public void hide()
	/*     */{
		/* 593 */super.hide();
		/* 594 */Runnable run = new Runnable() {
			/*     */public void run() {
				/* 596 */if (UIDialog.this.comp != null)
				/*     */{
					/* 602 */UIDialog.this.comp.requestFocusInWindow();
					/* 603 */UIDialog.this.comp.requestFocus();
					/*     */}
				/*     */
				/*     */}
			/*     */
		};
		/* 609 */SwingUtilities.invokeLater(run);
		/*     */}
	/*     */
	/*     */private void processBillHotKeyEvent(KeyEvent e)
	/*     */{
		/* 616 */Component bcp = MiscUtils.findChildByClass(this,
				IBillCardPanelKeyListener.class);
		/*     */
		/* 618 */if (bcp instanceof IBillCardPanelKeyListener)
			/* 619 */((IBillCardPanelKeyListener) bcp).processShortKeyEvent(e);
		/*     */}
	/*     */
	/*     */protected void processWindowEvent(WindowEvent e)
	/*     */{
		/* 646 */super.processWindowEvent(e);
		/*     */
		/* 648 */int defaultCloseOperation = getDefaultCloseOperation();
		/* 649 */if (e.getID() == 201) {
			/* 650 */if (defaultCloseOperation != 0)
				/* 651 */closeCancel();
			/*     */}
		/*     */else
		/*     */{
			/* 655 */if (((e.getID() != 200) && (e.getID() != 205)) ||
			/* 662 */(!(isJre7u25()))) {
				/*     */return;
				/*     */}
			/*     */
			/* 666 */Thread t = new Thread()
			/*     */{
				/*     */public void run() {
					/* 669 */while (!(UIDialog.this.isShowing()))
						/*     */try {
							/* 671 */Thread.sleep(50L);
							/*     */}
						/*     */catch (InterruptedException e) {
							/*     */}
					/* 675 */UIDialog.this.requestFocusInWindow();
					/* 676 */UIDialog.this.requestFocus();
					/*     */}
				/*     */
			};
			/* 682 */t.start();
			/*     */
			/* 685 */Thread t1 = new Thread()
			/*     */{
				/*     */public void run() {
					/* 688 */while ((UIDialog.this.isShowing())
							&& (!(UIDialog.this.hasFocus())))
						/*     */try {
							/* 690 */Thread.sleep(50L);
							/*     */}
						/*     */catch (InterruptedException e) {
							/*     */}
					/* 694 */if (UIDialog.this.getComponentCount() > 0)
						/* 695 */UIDialog.this.getComponent(
								UIDialog.this.getComponentCount() - 1)
								.transferFocus();
					/*     */}
				/*     */
			};
			/* 700 */t.start();
			/*     */}
		/*     */}
	/*     */
	/*     */private boolean isJre7()
	/*     */{
		/* 711 */return System.getProperty("java.version").startsWith("1.7");
		/*     */}
	/*     */
	/*     */private boolean isJre7u25() {
		/* 715 */return System.getProperty("java.version").equalsIgnoreCase(
				"1.7.0_25");
		/*     */}
	/*     */
	/*     */public void windowOpened(WindowEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */public void windowClosing(WindowEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */public void windowClosed(WindowEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */public void windowIconified(WindowEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */public void windowDeiconified(WindowEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */public void windowActivated(WindowEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */public void windowDeactivated(WindowEvent e)
	/*     */{
		/*     */}
	/*     */
	/*     */protected JRootPane createRootPane()
	/*     */{
		/* 785 */JRootPane rp = new JRootPane()
		/*     */{
			/*     */protected boolean processKeyBinding(KeyStroke ks,
					KeyEvent e, int condition, boolean pressed) {
				/* 788 */boolean b = super.processKeyBinding(ks, e, condition,
						pressed);
				/* 789 */if (pressed) {
					/* 790 */UIDialog.this.keyPressed(e);
					/*     */}
				/* 792 */return b;
				/*     */}
			/*     */
			/*     */public void setContentPane(Container content)
			/*     */{
				/* 797 */if (content instanceof JComponent) {
					/* 798 */((JComponent) content).setOpaque(true);
					/*     */}
				/* 800 */content.setBackground(UIDialog.dlgContentBGColor);
				/* 801 */JPanel panel = new JPanel(new BorderLayout())
				/*     */{
					/*     */public void setOpaque(boolean isOpaque)
					/*     */{
						/* 805 */super.setOpaque(true);
						/*     */}
					/*     */
				};
				/* 809 */panel.setOpaque(true);
				/* 810 */panel.setBackground(UIDialog.dlgContentBGColor);
				/* 811 */panel.add(content, "Center");
				/* 812 */super.setContentPane(panel);
				/*     */}
			/*     */
		};
		/* 816 */rp.getInputMap(1).put(KeyStroke.getKeyStroke(27, 0, false),
				"CLOSEDIALOG");
		/*     */
		/* 819 */rp.getActionMap().put("CLOSEDIALOG",
				new ShortCutKeyAction(27));
		/*     */
		/* 821 */return rp;
		/*     */}
	/*     */
	/*     */public void setSize(int width, int height)
	/*     */{
		/* 826 */if ((getRootPane() != null)
				&& (getRootPane().getUI() instanceof UIDialogRootPaneUI))
		/*     */{
			/* 828 */UIDialogRootPaneUI ui = (UIDialogRootPaneUI) getRootPane()
					.getUI();
			/* 829 */JComponent titlePane = ui.getTitlePane();
			/* 830 */if (titlePane != null) {
				/* 831 */height += titlePane.getPreferredSize().height + 4;
				/* 832 */width += 6;
				/*     */}
			/*     */}
		/*     */
		/* 836 */super.setSize(width, height);
		/*     */}
	/*     */
	/*     */public void setSizeNoChange(int width, int height)
	/*     */{
		/* 841 */super.setSize(width, height);
		/*     */}
	/*     */
	/*     */public void setLocation(int x, int y)
	/*     */{
		/* 846 */if ((getRootPane() != null)
				&& (getRootPane().getUI() instanceof UIDialogRootPaneUI))
		/*     */{
			/* 848 */UIDialogRootPaneUI ui = (UIDialogRootPaneUI) getRootPane()
					.getUI();
			/* 849 */JComponent titlePane = ui.getTitlePane();
			/* 850 */if (titlePane != null) {
				/* 851 */y -= titlePane.getPreferredSize().height;
				/*     */}
			/*     */}
		/*     */
		/* 855 */super.setLocation(x, y);
		/*     */}
	/*     */
	/*     */public void setLocationNoChange(int x, int y)
	/*     */{
		/* 860 */super.setLocation(x, y);
		/*     */}
	/*     */
	/*     */static
	/*     */{
		/*  73 */m_allSingleHotKeys = new HashSet();
		/*  74 */m_allSingleHotKeys.add(Integer.valueOf(27));
		/*  75 */m_allSingleHotKeys.add(Integer.valueOf(33));
		/*  76 */m_allSingleHotKeys.add(Integer.valueOf(34));
		/*  77 */m_allSingleHotKeys.add(Integer.valueOf(35));
		/*  78 */m_allSingleHotKeys.add(Integer.valueOf(36));
		/*  79 */m_allSingleHotKeys.add(Integer.valueOf(37));
		/*  80 */m_allSingleHotKeys.add(Integer.valueOf(38));
		/*  81 */m_allSingleHotKeys.add(Integer.valueOf(39));
		/*  82 */m_allSingleHotKeys.add(Integer.valueOf(40));
		/*  83 */m_allSingleHotKeys.add(Integer.valueOf(127));
		/*  84 */m_allSingleHotKeys.add(Integer.valueOf(113));
		/*  85 */m_allSingleHotKeys.add(Integer.valueOf(114));
		/*  86 */m_allSingleHotKeys.add(Integer.valueOf(115));
		/*  87 */m_allSingleHotKeys.add(Integer.valueOf(116));
		/*  88 */m_allSingleHotKeys.add(Integer.valueOf(116));
		/*  89 */m_allSingleHotKeys.add(Integer.valueOf(118));
		/*  90 */m_allSingleHotKeys.add(Integer.valueOf(119));
		/*  91 */m_allSingleHotKeys.add(Integer.valueOf(120));
		/*  92 */m_allSingleHotKeys.add(Integer.valueOf(121));
		/*  93 */m_allSingleHotKeys.add(Integer.valueOf(122));
		/*  94 */m_allSingleHotKeys.add(Integer.valueOf(123));
		/*  95 */m_allSingleHotKeys.add(Integer.valueOf(10));
		/*     */}
	/*     */
	/*     */class ShortCutKeyAction extends AbstractAction
	/*     */{
		/* 325 */int keycode = -1;
		/*     */static final int VK_ESCAPE = 27;
		/*     */static final String KEY_CLOSE_DIALOG = "CLOSEDIALOG";
		/*     */
		/*     */public ShortCutKeyAction(int paramInt)
		/*     */{
			/* 332 */this.keycode = paramInt;
			/*     */}
		/*     */
		/*     */public void actionPerformed(ActionEvent e) {
			/* 336 */switch (this.keycode)
			/*     */{
			/*     */case 27 :
					/* 338 */UIDialog.this.closeCancel();
					/* 339 */return;
					/*     */}
			/*     */}
		/*     */
	}
	/*     */
}