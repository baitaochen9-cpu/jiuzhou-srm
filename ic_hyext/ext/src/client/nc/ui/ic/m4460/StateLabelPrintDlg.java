package nc.ui.ic.m4460;

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
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.ObjectUtils;
import nc.vo.trade.voutils.SafeCompute;

public class StateLabelPrintDlg extends UIDialog implements BillEditListener2,
		BillEditListener, ActionListener, BillCardBeforeEditListener {
	private static final long serialVersionUID = 1L;
	private UIButton btnCancle;
	private UIButton btnOk;

	private UIButton addline;
	private UIButton delline;
	private boolean isCloseByBtnOK;
	private UIPanel m_pnlButton;
	private BillCardPanel pnlCardpanel;
	private String billTempletID = "1001AA100000000BXWWP";
	private StateAdjustVO[] bvos;

	public StateLabelPrintDlg(Container parent, StateAdjustVO[] bvos) {
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
			int count = getBillCardPanel().getBillModel().getRowCount();
			if (count < 2) {
				handleException(new BusinessException("最后一行数据不能删除！"));
				return;
			}
			getBillCardPanel().delLine();
			calncanreplnum();
		}
	}

	public boolean beforeEdit(BillEditEvent e) {
		try {
			String key = e.getKey();
			if ("vdef2".equals(key)) {// 包装规格
				UIRefPane refpane = (UIRefPane) getBillCardPanel()
						.getBillModel().getItemByKey("vdef2").getComponent();
				StateAdjustVO hvo = (StateAdjustVO) getBillCardPanel()
						.getBillModel().getBodyValueRowVO(0,
								StateAdjustVO.class.getName());
				String pk_org = hvo.getPk_org();
				refpane.getRefModel().setPk_org(pk_org);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			handleException(e1);
			return true;
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
			pnlCardpanel.addEditListener(this);
			pnlCardpanel.setBillBeforeEditListenerHeadTail(this);
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

	private void initialize(StateAdjustVO[] bvos) {
		try {
			setSize(1000, 500);
			setTitle("状态标签打印");
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(getBillCardPanel(), "Center");

			getContentPane().add(getUIPanelButton(), "South");
			// 项目主键 nnum 数量
			// 项目主键 nusenum 已分配数量
			getBillCardPanel().getHeadItem("nnum").setValue(bvos[0].getNadjustassistnum());
			// getBillCardPanel().getHeadItem("nusenum").setValue(
			// bvos[0].getNnum());
			bvos[0].setNnum(bvos[0].getNadjustassistnum());
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

		boolean isCheckPass = true;
		int len = getBillCardPanel().getBillModel().getRowCount();

		for (int i = 0; i < len; i++) {
			Object o = getBillCardPanel().getBillModel().getValueAt(i, "vdef2");
			if (o == null) {
				handleException(new BusinessException("第" + (i + 1)
						+ "行包装规格不能为空！"));
				isCheckPass = false;
			}
		}
		if (!isCheckPass) {
			return;
		}

		UIRefPane refpane = (UIRefPane) getBillCardPanel().getHeadItem(
				"spec").getComponent();
		boolean ischecknum  =checkNum( refpane.getRefPK());
		
		if(ischecknum){
			String usenum = (String) getBillCardPanel().getHeadItem("nusenum")
					.getValueObject();
			UFDouble nusenum = new UFDouble(usenum);
			String num = (String) getBillCardPanel().getHeadItem("nnum")
					.getValueObject();
			UFDouble nnum = new UFDouble(num);
			if (nnum.compareTo(nusenum) != 0) {
				handleException(new BusinessException("标签已分配数量不等于数量！"));
				return;
			}
		}
		
		StateAdjustVO[] vos = (StateAdjustVO[]) pnlCardpanel.getBillModel()
				.getBodyValueVOs(StateAdjustVO.class.getName());
		setBvos(vos);
		isCloseByBtnOK = true;
		dispose();
	}

	public StateAdjustVO[] getBvos() {
		return bvos;
	}

	public void setBvos(StateAdjustVO[] bvos) {
		this.bvos = bvos;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// 项目主键 nnum 数量
		// 项目主键 nusenum 已分配数量
		try {
			String key = e.getKey();
			Object value = e.getValue();
			if (e.getPos() == BillItem.HEAD) {
				if ("spec".equals(key)) {// 规格
					StateAdjustVO[] bvos = calStateAdjustVOs(((String[]) value)[0]);
					getBillCardPanel().getBillModel().setBodyDataVO(bvos);
					getBillCardPanel().getBillModel().execLoadFormula();
					getBillCardPanel().getBillModel()
							.loadLoadRelationItemValue();
					calncanreplnum();
				}
			} else if (e.getPos() == BillItem.BODY) {
				if ("nnum".equals(key)) {// 数量
					calncanreplnum();
				} else if ("vdef2".equals(key)) {// 包装规格
					DefdocVO defvo = getDefdocVO((String) value);
					BigDecimal bweight = getNweight(defvo);
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(bweight), e.getRow(), "nnum");
					calncanreplnum();
				}
			}

		} catch (Exception e1) {
			handleException(e1);
		}
	}

	private void calncanreplnum() {
		// 项目主键 nnum 数量
		// 项目主键 nusenum 已分配数量
		int rowcount = getBillCardPanel().getBillModel().getRowCount();
		UFDouble total = UFDouble.ZERO_DBL;
		for (int i = 0; i < rowcount; i++) {
			UFDouble nastnum = (UFDouble) getBillCardPanel().getBillModel()
					.getValueAt(i, "nnum");
			total = SafeCompute.add(total, nastnum);
		}
		getBillCardPanel().getHeadItem("nusenum").setValue(total);// 已分配数量

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

	private StateAdjustVO[] calStateAdjustVOs(String spec) throws Exception {
		// 项目主键 nnum 数量
		// 项目主键 nusenum 已分配数量

		DefdocVO defvo = getDefdocVO(spec);

		if (defvo == null) {
			throw new BusinessException("包装规格不存在");
		}

		StateAdjustVO hvo = (StateAdjustVO) getBillCardPanel().getBillModel()
				.getBodyValueRowVO(0, StateAdjustVO.class.getName());
		BigDecimal[] bigs = getNnpiece(hvo, defvo);
		int size = bigs[0].intValue();
		List<StateAdjustVO> list = new ArrayList<>();

		int len = getBillCardPanel().getBillModel().getRowCount();

		for (int i = 0; i < len; i++) {
			Object o = getBillCardPanel().getBillModel().getValueAt(i, "vdef2");
			if (o != null) {
				StateAdjustVO bvo = (StateAdjustVO) getBillCardPanel()
						.getBillModel().getBodyValueRowVO(i,
								StateAdjustVO.class.getName());
				list.add(bvo);
			}
		}

		for (int i = 0; i < size; i++) {
			StateAdjustVO sbvo = getStateAdjustVO(spec, hvo, bigs[2]);
			list.add(sbvo);
		}

		if (bigs[1].compareTo(BigDecimal.ZERO) != 0) {
			StateAdjustVO bvo1 = getStateAdjustVO(spec, hvo, bigs[1]);
			list.add(bvo1);
		}

		return list.toArray(new StateAdjustVO[list.size()]);
	}

	private StateAdjustVO getStateAdjustVO(String spec, StateAdjustVO hvo,
			BigDecimal npiece) throws Exception {
		StateAdjustVO sbvo = (StateAdjustVO) ObjectUtils.serializableClone(hvo);
		sbvo.setVdef2(spec);
		sbvo.setNnum(new UFDouble(npiece));
		return sbvo;
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

	private BigDecimal getNweight(DefdocVO defvo) throws BusinessException {
		String def3 = defvo.getDef2();
		if (StringUtil.isEmpty(def3))
			throw new BusinessException("包装规格净重不能为空!");
		return new BigDecimal(def3);
	}

	// 商 和 余数
	private BigDecimal[] getNnpiece(StateAdjustVO hvo, DefdocVO defvo)
			throws BusinessException {
		UFDouble ufdouble = new UFDouble(getBillCardPanel().getHeadItem("nnum")
				.getValue());// 数量
		UFDouble nusenum = new UFDouble(getBillCardPanel().getHeadItem(
				"nusenum").getValue());// 已分配数量
		ufdouble = SafeCompute.sub(ufdouble, nusenum);
		BigDecimal bweight = getNweight(defvo);
		BigDecimal bgrosswt = ufdouble.toBigDecimal();
		BigDecimal[] bigs = bgrosswt.divideAndRemainder(bweight);

		List<BigDecimal> list = new ArrayList<>();
		for (BigDecimal big : bigs) {
			list.add(big);
		}
		list.add(bweight);

		return list.toArray(new BigDecimal[list.size()]);
	}

	@Override
	public boolean beforeEdit(BillItemEvent e) {
		try {
			String key = e.getItem().getKey();
			if ("spec".equals(key)) {// 包装规格
				UIRefPane refpane = (UIRefPane) getBillCardPanel().getHeadItem(
						"spec").getComponent();
				StateAdjustVO hvo = (StateAdjustVO) getBillCardPanel()
						.getBillModel().getBodyValueRowVO(0,
								StateAdjustVO.class.getName());
				String pk_org = hvo.getPk_org();
				refpane.getRefModel().setPk_org(pk_org);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			handleException(e1);
			return true;
		}
		return true;
	}
}