package nc.ui.pu.m23.print.dialog;

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
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

public class SingletonLabelPrintDlg extends UIDialog implements
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
	private String billTempletID = "1001AA100000000BX85Z";
	private ArriveItemVO[] bvos;
	UFDouble nnum = UFDouble.ZERO_DBL;
	public SingletonLabelPrintDlg(Container parent, ArriveItemVO bvo,
			ArriveItemVO[] bvos) {
		super(parent);
		initialize(bvo,bvos);
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

	private void initialize(ArriveItemVO bvo,ArriveItemVO[] bvos) {
		try {
			setSize(1000, 500);
			setTitle("原料待验标签打印");
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(getBillCardPanel(), "Center");

			getContentPane().add(getUIPanelButton(), "South");
			nnum = bvo.getNastnum();
			getBillCardPanel().getBillModel().setBodyDataVO(bvos);
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
			// String def1 = getBillCardPanel().getHeadItem("ncanreplnum")
			// .getValue();// 已分配数量
			// String nastnum = getBillCardPanel().getHeadItem("nastnum")
			// .getValue();// 主数量
			//
			// UFDouble nusenum = new UFDouble(def1);
			// UFDouble nnum = new UFDouble(nastnum);
			// if (nusenum.compareTo(nnum) != 0) {
			// throw new BusinessException("已分配数量不等于主数量！");
			// }
			ArriveItemVO[] vos = (ArriveItemVO[]) pnlCardpanel.getBillModel()
					.getBodyValueVOs(ArriveItemVO.class.getName());
			
			boolean ischecknum  =checkNum( vos[0].getVbdef8());
			
			if(ischecknum){
				UFDouble nusenum = UFDouble.ZERO_DBL;
				for (ArriveItemVO vo : vos) {
					nusenum = SafeCompute.add(nusenum, vo.getNastnum());
				}

				if (nusenum.compareTo(nnum) != 0) {
					throw new BusinessException("分配数量不等于主数量！");
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

	public ArriveItemVO[] getBvos() {
		return bvos;
	}

	public void setBvos(ArriveItemVO[] bvos) {
		this.bvos = bvos;
	}

}