package nc.ui.ewm.workorder.dlg;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import nc.ui.am.common.dlg.AbstractDialog;
import nc.ui.bd.ref.IRefConst;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UILabelLayout;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.logging.Debug;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

public class WoDateDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	private UIPanel centerPanel = null;

	private UILabel newWoStatusLabel = null;

	private UIRefPane woStatusRefPanel = null;

	private String woStatusStr = null;

	private UILabel memoLabel = null;

	private UIRefPane memoLabelField = null;

	private String memoStr = null;

	public static final String RET_PK_WO_STATUS = "Pk_wo_status";

	public static final String RET_MEMO = "Memo";
	public static final String RET_STATUS_TYPE = "Status_type";
	private Map<String, String> m_res = null;

	public WoDateDialog(Container parent, Integer[] statusTypes,
			String currStatusid, LoginContext context) {
		super(parent);
		initLabelStr();
		initUI();
	}

	protected UIPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new UIPanel();

			centerPanel.setLayout(new UILabelLayout(2, 2, -1, C_HGAP, CON_VGAP,
					CON_VGAP, CON_HGAP, -1, C_HGAP));

			centerPanel.add(getNewWoStatusLabel());
			centerPanel.add(getWOStatusRefPanel());
			centerPanel.add(getMemoLabel());
			centerPanel.add(getMemoLabelField());
		}
		return centerPanel;
	}

	private void handleException(Throwable e) {
		Debug.error(e.getMessage(), e);
		MessageDialog.showErrorDlg(null, NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("workorder_0", "04560003-0132"), NCLangRes4VoTransl
				.getNCLangRes().getStrByID("workorder_0", "04560003-0133"));
	}

	private void initLabelStr() {
		woStatusStr = "计划开始时间";

		memoStr = "计划结束时间";
	}

	private int getLabelWidth() {
		int status_px = computeStringWidth(woStatusStr);
		int memo_px = computeStringWidth(memoStr);

		int max = Math.max(status_px, memo_px);

		return Math.min(max, MAX_LABEL_W);
	}

	protected Dimension getLabelSize() {
		return new Dimension(getLabelWidth(), C_HEI);
	}

	public UILabel getNewWoStatusLabel() {
		if (newWoStatusLabel == null) {
			newWoStatusLabel = new UILabel();
			newWoStatusLabel.setName("newWoStatusLabel");
			newWoStatusLabel.setText(woStatusStr);
			newWoStatusLabel.setPreferredSize(getLabelSize());
			newWoStatusLabel.setHorizontalAlignment(4);
		}
		return newWoStatusLabel;
	}

	public UILabel getMemoLabel() {
		if (memoLabel == null) {
			memoLabel = new UILabel();
			memoLabel.setName("memoLabel");
			memoLabel.setText(memoStr);
			memoLabel.setPreferredSize(getLabelSize());
			memoLabel.setHorizontalAlignment(4);
		}
		return memoLabel;
	}

	private UIRefPane getWOStatusRefPanel() {
		if (woStatusRefPanel == null) {
			woStatusRefPanel = new UIRefPane(IRefConst.REFNODENAME_DATETIME);
			woStatusRefPanel.setText(new UFDate().toString());
			woStatusRefPanel.setPreferredSize(new Dimension(PANEL_W, C_HEI));
		}
		return woStatusRefPanel;
	}

	public UIRefPane getMemoLabelField() {
		if (memoLabelField == null) {
			memoLabelField = new UIRefPane(IRefConst.REFNODENAME_DATETIME);
			memoLabelField.setText(new UFDate().toString());
			memoLabelField.setPreferredSize(new Dimension(PANEL_W, C_HEI * 2));
		}

		return memoLabelField;
	}

	protected void pressOK() {

		m_res = new HashMap();
		m_res.put("begin", getWOStatusRefPanel().getText());
		m_res.put("end", getMemoLabelField().getText());
	}

	protected boolean validateData() {
		String begin = getWOStatusRefPanel().getText();
		String end = getMemoLabelField().getText();
		if(begin != null && end != null){
			if(begin.compareTo(end)>0){
				MessageDialog.showErrorDlg(null,"提示", "计划开始时间不能大于计划结束时间");
				return false;
			}
		}
		
		return true;
	}

	protected void initShowMustInputHint() {
		getWOStatusRefPanel().getUITextField().setShowMustInputHint(true);
	}

	public Map<String, String> getRetMap() {
		return m_res;
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if ((e.getID() == 200) || (e.getID() == 205)) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					getOkBtn().requestFocus();
				}
			});
		}
	}

	protected int getCenterPanelHeight() {
		return CON_HGAP + C_HEI + C_HGAP + C_HEI + C_HEI + CON_HGAP;
	}

	protected int getCenterPanelWidth() {
		return CON_VGAP + getLabelWidth() + C_VGAP + PANEL_W + CON_VGAP;
	}

	protected String getDialogTitle() {
		return "批量修改";
	}
}
