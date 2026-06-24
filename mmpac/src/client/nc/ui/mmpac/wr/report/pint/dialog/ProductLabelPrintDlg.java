package nc.ui.mmpac.wr.report.pint.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrQualityVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

public class ProductLabelPrintDlg extends UIDialog implements
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
	private String billTempletID = "1001AA100000000BXIII";
	private WrItemVO[] bvos;
	UFDouble nnum = UFDouble.ZERO_DBL;
	private Map<String, UFDouble> map = new HashMap<String, UFDouble>();

	public ProductLabelPrintDlg(Container parent, WrItemVO bvo, WrItemVO[] bvos) {
		super(parent);
		initialize(bvo, bvos);
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

	private void initialize(WrItemVO bvo, WrItemVO[] bvos) {
		try {
			setSize(1000, 500);
			setTitle("产品内标签打印");
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(getBillCardPanel(), "Center");

			getContentPane().add(getUIPanelButton(), "South");

			for (WrItemVO bvo1 : bvos) {
				WrQualityVO[] quavos = bvo1.getQualityvos();
				if (quavos != null && quavos.length > 0) {
					nnum = quavos[0].getNginnum();
					if (!map.containsKey(bvo1.getPk_wr_product())) {
						map.put(bvo1.getPk_wr_product(), quavos[0].getNginnum());
					}
				}
			}

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
			WrItemVO[] vos = (WrItemVO[]) pnlCardpanel.getBillModel()
					.getBodyValueVOs(WrItemVO.class.getName());
			Map<String, UFDouble> nusenummap = new HashMap<String, UFDouble>();
			Map<String, WrItemVO> vommap = new HashMap<String, WrItemVO>();
			UFDouble nusenum = UFDouble.ZERO_DBL;
			for (WrItemVO vo : vos) {
				if (!nusenummap.containsKey(vo.getPk_wr_product())) {
					nusenummap.put(vo.getPk_wr_product(), vo.getNbwrastnum());
				} else {
					nusenum = SafeCompute.add(
							nusenummap.get(vo.getPk_wr_product()),
							vo.getNbwrastnum());
					nusenummap.put(vo.getPk_wr_product(), nusenum);
				}
				if (!vommap.containsKey(vo.getPk_wr_product())) {
					vommap.put(vo.getPk_wr_product(), vo);
				}
			}

			for (String key : vommap.keySet()) {
				WrItemVO vo = vommap.get(key);
				if (vo == null) {
					continue;
				}
				boolean ischecknum = checkNum(vo.getVbdef20());
				if (ischecknum) {
					nusenum = nusenummap.get(key);
					nnum = map.get(key);
					if (nusenum.compareTo(nnum) != 0) {
						throw new BusinessException("分配数量不等于入库数量！");
					}
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
		return true;
	}

	// 包装规格
	private DefdocVO getDefdocVO(String value) throws UifException {
		DefdocVO bvo = (DefdocVO) HYPubBO_Client.queryByPrimaryKey(
				DefdocVO.class, value);
		return bvo;
	}

	public WrItemVO[] getBvos() {
		return bvos;
	}

	public void setBvos(WrItemVO[] bvos) {
		this.bvos = bvos;
	}

}