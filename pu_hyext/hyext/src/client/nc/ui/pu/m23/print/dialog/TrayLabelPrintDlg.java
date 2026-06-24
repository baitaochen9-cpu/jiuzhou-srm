package nc.ui.pu.m23.print.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.ObjectUtils;
import nc.vo.trade.voutils.SafeCompute;

public class TrayLabelPrintDlg extends UIDialog implements BillEditListener2,
		BillEditListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private UIButton btnCancle;
	private UIButton btnOk;

	private UIButton addline;
	private UIButton delline;
	private boolean isCheckPass;
	private boolean isCloseByBtnOK;
	private UIPanel m_pnlButton;
	private BillCardPanel pnlCardpanel;
	private String billTempletID = "1001AA100000000BXAOC";
	private ArriveItemVO[] bvos;

	public TrayLabelPrintDlg(Container parent, ArriveItemVO[] bvos) {
		super(parent);
		initialize(bvos);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOk) {
			super.setResult(1);
			onSplitOK();
		} else if (e.getSource() == btnCancle) {
			super.setResult(2);
			dispose();
		} else if (e.getSource() == addline) {
			getBillCardPanel().getBodyPanel().getTableModel()
					.copyLine(new int[] { 0 });
			getBillCardPanel().pasteLineToTail();
			calncanreplnum();
		} else if (e.getSource() == delline) {
			getBillCardPanel().delLine();
			calncanreplnum();
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
			pnlCardpanel.setDividerProportion(0.2);
			pnlCardpanel.getBodyPanel().getTable().setSelectionMode(0);
			pnlCardpanel.addBodyEditListener2(this);
			pnlCardpanel.addEditListener(this);
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

	private void initialize(ArriveItemVO[] bvos) {
		try {
			setSize(1000, 500);
			setTitle("托盘标签打印");
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(getBillCardPanel(), "Center");

			getContentPane().add(getUIPanelButton(), "South");
			// ncanarrivenum 单托数量
			// ncanreplnum 已分配数量
			UFDouble nastnum = bvos[0].getNastnum();
			bvos[0].setNcanarrivenum(UFDouble.ZERO_DBL);
			bvos[0].setNcanreplnum(UFDouble.ZERO_DBL);
			getBillCardPanel().getBillData().setHeaderValueVO(bvos[0]);
			bvos[0].setNastnum(UFDouble.ZERO_DBL);
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
			String def1 = getBillCardPanel().getHeadItem("ncanreplnum")
					.getValue();// 已分配数量
			String nastnum = getBillCardPanel().getHeadItem("nastnum")
					.getValue();// 主数量

			UFDouble nusenum = new UFDouble(def1);
			UFDouble nnum = new UFDouble(nastnum);
			if (nusenum.compareTo(nnum) != 0) {
				 throw new BusinessException("已分配数量不等于主数量！");
			}
			ArriveItemVO[] vos = (ArriveItemVO[]) pnlCardpanel.getBillModel()
					.getBodyValueVOs(ArriveItemVO.class.getName());
			setBvos(vos);
			isCloseByBtnOK = true;
			dispose();
		} catch (Exception e1) {
			e1.printStackTrace();
			handleException(e1);
		}

	}

	public ArriveItemVO[] getBvos() {
		return bvos;
	}

	public void setBvos(ArriveItemVO[] bvos) {
		this.bvos = bvos;
	}

	@Override
	public void afterEdit(BillEditEvent e) {

		// ncanarrivenum 单托数量
		// ncanreplnum 已分配数量
		// nastnum 数量
		try {
			String key = e.getKey();
			Object value = e.getValue();
			if (e.getPos() == BillItem.HEAD) {
				if ("ncanarrivenum".equals(key)) {// 单托数量
					ArriveItemVO hvo = (ArriveItemVO) getBillCardPanel()
							.getBillData().getHeaderValueVO(
									ArriveItemVO.class.getName());
					ArriveItemVO[] bvos;
					UFDouble ufdouble = hvo.getNastnum();
					if (ufdouble == null)
						throw new BusinessException("数量不能为空!");
					List<ArriveItemVO> list = new ArrayList<>();
					ArriveItemVO[] bvvos = (ArriveItemVO[]) getBillCardPanel()
							.getBillModel().getBodyValueVOs(
									ArriveItemVO.class.getName());

					for (ArriveItemVO bvo : bvvos) {
						if (bvo.getNastnum() != null
								&& bvo.getNastnum()
										.compareTo(UFDouble.ZERO_DBL) > 0) {
							list.add(bvo);
						}

					}
					String canreplnum = getBillCardPanel().getHeadItem(
							"ncanreplnum").getValue();
					String astnum = getBillCardPanel().getHeadItem("nastnum")
							.getValue();

					UFDouble ncanreplnum = new UFDouble(canreplnum);
					UFDouble nastnum = new UFDouble(astnum);
					UFDouble ntempnum = SafeCompute.sub(nastnum, ncanreplnum);
					if (ntempnum.compareTo(UFDouble.ZERO_DBL) > 0) {
						bvos = calArriveItemVOs(hvo, ntempnum, (UFDouble) value);
						for (ArriveItemVO bvo : bvos) {
							list.add(bvo);
						}
					}
					getBillCardPanel().getBillModel().setBodyDataVO(
							list.toArray(new ArriveItemVO[list.size()]));
					getBillCardPanel().getBillModel().execLoadFormula();
					getBillCardPanel().getBillModel()
							.loadLoadRelationItemValue();
					calncanreplnum();
				}
			} else if (e.getPos() == BillItem.BODY) {
				if ("nastnum".equals(key)) {// 数量
					calncanreplnum();
				}
			}

		} catch (Exception e1) {
			handleException(e1);
		}

	}

	private void calncanreplnum() {
		int rowcount = getBillCardPanel().getBillModel().getRowCount();
		UFDouble total = UFDouble.ZERO_DBL;
		for (int i = 0; i < rowcount; i++) {
			UFDouble nastnum = (UFDouble) getBillCardPanel().getBillModel()
					.getValueAt(i, "nastnum");
			total = SafeCompute.add(total, nastnum);
		}
		getBillCardPanel().getHeadItem("ncanreplnum").setValue(total);// 已分配数量

	}

	private void handleException(Exception exception) {
		try {
			ExceptionUtils.wrappException(exception);
		} catch (Exception e) {
			Logger.warn(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}
	}

	@Override
	public void bodyRowChange(BillEditEvent e) {

	}

	private ArriveItemVO[] calArriveItemVOs(ArriveItemVO hvo,
			UFDouble ntempnum, UFDouble ncanarrivenum) throws Exception {

		hvo.setCrowno(null);
		BigDecimal[] bigs = getNnpiece(ntempnum, ncanarrivenum);

		int size = bigs[0].intValue();
		List<ArriveItemVO> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			ArriveItemVO sbvo = getArriveItemVO(hvo, bigs[2]);
			list.add(sbvo);
		}

		if (bigs[1].compareTo(BigDecimal.ZERO) != 0) {
			ArriveItemVO bvo1 = getArriveItemVO(hvo, bigs[1]);
			list.add(bvo1);
		}

		return list.toArray(new ArriveItemVO[list.size()]);
	}

	private ArriveItemVO getArriveItemVO(ArriveItemVO hvo, BigDecimal npiece)
			throws Exception {
		ArriveItemVO sbvo = (ArriveItemVO) ObjectUtils.serializableClone(hvo);
//		sbvo.setVbdef2(sbvo.getVbdef8());
		sbvo.setNastnum(new UFDouble(npiece));
		return sbvo;
	}

	// 商 和 余数
	private BigDecimal[] getNnpiece(UFDouble ntempnum, UFDouble ncanarrivenum)
			throws BusinessException {

		BigDecimal bweight = new BigDecimal(ncanarrivenum.doubleValue());
		BigDecimal bgrosswt = ntempnum.toBigDecimal();
		BigDecimal[] bigs = bgrosswt.divideAndRemainder(bweight);

		List<BigDecimal> list = new ArrayList<>();
		for (BigDecimal big : bigs) {
			list.add(big);
		}
		list.add(bweight);

		return list.toArray(new BigDecimal[list.size()]);
	}
}