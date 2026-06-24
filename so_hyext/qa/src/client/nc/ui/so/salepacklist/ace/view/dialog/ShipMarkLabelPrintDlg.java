package nc.ui.so.salepacklist.ace.view.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.so.salepacklist.SalePackListHVO;
import nc.vo.trade.voutils.SafeCompute;

public class ShipMarkLabelPrintDlg extends UIDialog implements
		BillEditListener2, ActionListener {
	private static final long serialVersionUID = 1L;
	private UIButton btnCancle;
	private UIButton btnOk;

	private UIButton addline;
	private UIButton delline;
	private boolean isCheckPass;
	private boolean isCloseByBtnOK;
	private UIPanel m_pnlButton;
	private BillCardPanel pnlCardpanel;
	private String billTempletID = "1001AA100000000BXP57";
	private SalePackListBVO[] bvos;
	UFDouble nnum = UFDouble.ZERO_DBL;

	public ShipMarkLabelPrintDlg(Container parent, SalePackListHVO hvo,
			SalePackListBVO[] bvos) {
		super(parent);
		initialize(hvo, bvos);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOk) {
			super.setResult(1);
			onSplitOK();
		} else if (e.getSource() == btnCancle) {
			super.setResult(2);
			dispose();
		} else if (e.getSource() == addline) {
			getBillCardPanel().copyLine();
			getBillCardPanel().pasteLineToTail();
		} else if (e.getSource() == delline) {
			getBillCardPanel().delLine();
		}
	}

	public boolean beforeEdit(BillEditEvent e) {
		pnlCardpanel.stopEditing();

		if ((e.getPos() == 1) && ("values".equals(e.getKey()))) {

		}

		return true;
	}

	public boolean isCloseByBtnOK() {
		return isCloseByBtnOK;
	}

	private BillCardPanel getBillCardPanel() {
		if (pnlCardpanel == null) {
			pnlCardpanel = new BillCardPanel();
			pnlCardpanel.loadTemplet(billTempletID);
			pnlCardpanel.setSize(getContentPane().size());
			pnlCardpanel.getBodyPanel().getTable().setSelectionMode(0);
			pnlCardpanel.addBodyEditListener2(this);
			pnlCardpanel.setBodyMenuShow(false);
		}
		return pnlCardpanel;
	}

	private UIPanel getUIPanelButton() {
		if (m_pnlButton == null) {
			btnOk = new UIButton("保存");
			btnOk.setSize(60, 22);
			btnOk.addActionListener(this);
			btnCancle = new UIButton(NCLangRes.getInstance().getStrByID(
					"pubapp_0", "0pubapp-0020"));
			btnCancle.setSize(btnOk.getSize());
			btnCancle.addActionListener(this);

			addline = new UIButton("增行");
			addline.setSize(60, 22);
			addline.addActionListener(this);

			delline = new UIButton("删行");
			delline.setSize(60, 22);
			delline.addActionListener(this);
			m_pnlButton = new UIPanel();
			m_pnlButton.setLayout(new FlowLayout(1));
			m_pnlButton.add(addline);
			m_pnlButton.add(delline);
			m_pnlButton.add(btnOk);
			m_pnlButton.add(btnCancle);

		}
		return m_pnlButton;
	}

	private void handleException(Exception exception) {
		try {
			ExceptionUtils.wrappException(exception);
		} catch (Exception e) {
			Logger.warn(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}
	}

	private void initialize(SalePackListHVO hvo, SalePackListBVO[] bvos) {
		try {
			setSize(1100, 500);
			setTitle("成品发货标签打印");
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(getBillCardPanel(), "Center");

			getContentPane().add(getUIPanelButton(), "South");

			getBillCardPanel().getBillModel().setBodyDataVO(bvos);
			int len = getBillCardPanel().getBillModel().getRowCount();
			nnum = hvo.getNweight();
			for (int i = 0; i < len; i++) {
				getBillCardPanel().getBillModel().setValueAt(
						hvo.getPk_material(), i, "pk_material");
				getBillCardPanel().getBillModel().setValueAt(hvo.getDef2(), i,
						"def2");
				getBillCardPanel().getBillModel().setValueAt(hvo.getDef3(), i,
						"def3");
				getBillCardPanel().getBillModel().setValueAt(hvo.getDef4(), i,
						"def4");
			}

			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().getBillModel().loadLoadRelationItemValue();
		} catch (Exception ivjExc) {
			handleException(ivjExc);
		}
	}

	private void onSplitOK() {
		// if (!isCheckPass) {
		// isCloseByBtnOK = false;
		// return;
		// }
		try {
			SalePackListBVO[] vos = (SalePackListBVO[]) pnlCardpanel
					.getBillModel().getBodyValueVOs(
							SalePackListBVO.class.getName());
			boolean ischecknum = checkNum(vos[0].getSpec());

			if (ischecknum) {
				UFDouble nusenum = UFDouble.ZERO_DBL;
				for (SalePackListBVO vo : vos) {
					nusenum = SafeCompute.add(nusenum, vo.getNweight());
				}

				if (nusenum.compareTo(nnum) != 0) {
					throw new BusinessException("分配净重不等于总净重！");
				}
			}

			setBvos(vos);
			isCloseByBtnOK = true;
			dispose();
		} catch (Exception e1) {
			e1.printStackTrace();
			handleException(e1);
		}
	}

	private boolean checkNum(String spec) {
		try {
			DefdocVO defvo = getDefdocVO(spec);
			if (defvo != null && "Y".equalsIgnoreCase(defvo.getDef3())) {
				return true;
			}
		} catch (Exception ivjExc) {
			handleException(ivjExc);
		}
		return false;
	}

	// 包装规格
	private DefdocVO getDefdocVO(String value) throws UifException {
		DefdocVO bvo = (DefdocVO) HYPubBO_Client.queryByPrimaryKey(
				DefdocVO.class, value);
		return bvo;
	}

	public SalePackListBVO[] getBvos() {
		return bvos;
	}

	public void setBvos(SalePackListBVO[] bvos) {
		this.bvos = bvos;
	}

}