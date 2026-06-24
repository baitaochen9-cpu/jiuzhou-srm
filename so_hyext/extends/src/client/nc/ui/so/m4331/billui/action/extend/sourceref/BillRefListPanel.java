package nc.ui.so.m4331.billui.action.extend.sourceref;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pubapp.billref.src.view.RefListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.so.salepacklist.SalePackListHVO;
import nc.vo.trade.voutils.SafeCompute;

/**
 * @author yangb(kongxd)
 * 
 */
@SuppressWarnings("serial")
public class BillRefListPanel extends RefListPanel {

	public BillRefListPanel() {
		super();

	}

	@Override
	protected void initUI() {
		// TODO 自动生成的方法存根
		super.initUI();
		addHeadEditListener(editListener);
		addBodyEditListener(editListener1);
		String[] codes = getRefContext().getRefBill().getBillListPanel()
				.getBillListData().getBodyTableCodes();
		getBodyScrollPane(codes[0]).addEditListener2(editListener2);
		getParentListPanel().addEditListener2(editListener2);
		setChildMultiSelect(false);
	}

	private HeadEditListener editListener = new HeadEditListener();

	public class HeadEditListener implements nc.ui.pub.bill.BillEditListener {
		public HeadEditListener() {
		}

		public void afterEdit(BillEditEvent e) {
			int row = e.getRow();
			String key = e.getKey();
			if ("vdef2".equals(key)) {// 运输储存条件
				UIRefPane refpane = (UIRefPane) getHeadBillModel()
						.getItemByKey("vdef2").getComponent();
				String spec = refpane.getRefPK();
				getHeadBillModel().setValueAt(spec, row, "def2");
			} else if ("vdef3".equals(key)) {// 备注信息
				UIRefPane refpane = (UIRefPane) getHeadBillModel()
						.getItemByKey("vdef3").getComponent();
				String spec = refpane.getRefPK();
				getHeadBillModel().setValueAt(spec, row, "def3");
			} else if ("vdef4".equals(key)) {//质量标准
				UIRefPane refpane = (UIRefPane) getHeadBillModel()
						.getItemByKey("vdef4").getComponent();
				String spec = refpane.getRefPK();
				getHeadBillModel().setValueAt(spec, row, "def5");
			}
			String headPK = getBillRowManager().getSelectedHeadPk();
			setBodyVOS(headPK, (String) e.getValue(), row);
		}

		public void bodyRowChange(BillEditEvent e) {

		}
	}

	private void setBodyVOS(String headPK, String value, int row) {

		AggSalePackListHVO aggvo = (AggSalePackListHVO) getRefContext()
				.getRefBill().getRefBillModel().getBillVO(headPK);

		SalePackListHVO hvo = (SalePackListHVO) getHeadBillModel()
				.getBodyValueRowVO(row, SalePackListHVO.class.getName());
		aggvo.setParentVO(hvo);
	}

	private SalePackListBVO[] createSalePackListBVOs(String value, int row,
			SalePackListHVO hvo) {

		if (StringUtil.isEmpty(hvo.getSpec())) {
			handleException(new BusinessException("包装规格不能为空"));
			return null;
		}
		DefdocVO defvo = getDefdocVO(hvo.getSpec());

		if (defvo == null) {
			defvo = new DefdocVO();
			// handleException(new BusinessException("包装规格不存在"));
			// return null;
		}
		BigDecimal[] bigs = getNnpiece(hvo, defvo);

		List<SalePackListBVO> list = new ArrayList<>();
		SalePackListBVO sbvo = getSalePackListBVO(hvo, defvo, bigs[0], 0);
		list.add(sbvo);
		SalePackListBVO bvo1 = getSalePackListBVO(hvo, defvo, bigs[1], 1);
		list.add(bvo1);
		return list.toArray(new SalePackListBVO[list.size()]);
	}

	private SalePackListBVO getSalePackListBVO(SalePackListHVO hvo,
			DefdocVO defvo, BigDecimal npiece, int i) {
		SalePackListBVO sbvo = new SalePackListBVO();
		sbvo.setNpiece(npiece.intValue());
		sbvo.setPk_salepacklist(hvo.getPk_salepacklist());
		sbvo.setPk_salepacklist_b(sbvo.getPk_salepacklist() + i);
		return sbvo;
	}

	// 商 和 余数
	private BigDecimal[] getNnpiece(SalePackListHVO hvo, DefdocVO defvo) {
		UFDouble ufdouble = hvo.getNgrosswt();
		if (ufdouble == null) {
			handleException(new BusinessException("数量不能为空"));
			return null;
		}
		BigDecimal bweight = getNweight(defvo);
		BigDecimal bgrosswt = ufdouble.toBigDecimal();
		BigDecimal[] bigs = bgrosswt.divideAndRemainder(bweight);

		return bigs;
	}

	// 包装规格
	private DefdocVO getDefdocVO(String value) {
		try {
			DefdocVO bvo = (DefdocVO) HYPubBO_Client.queryByPrimaryKey(
					DefdocVO.class, value);
			return bvo;
		} catch (Exception e1) {
			e1.printStackTrace();
			handleException(e1);
		}
		return new DefdocVO();
	}

	private BigDecimal getNweight(DefdocVO defvo) {
		String def3 = defvo.getDef2();
		try {
			if (StringUtil.isEmpty(def3))
				throw new BusinessException("包装规格净重不能为空!");
		} catch (Exception e1) {
			e1.printStackTrace();
			handleException(e1);
		}
		return new BigDecimal(def3);
	}

	private BodyEditListener editListener1 = new BodyEditListener();

	public class BodyEditListener implements nc.ui.pub.bill.BillEditListener {
		public BodyEditListener() {
		}

		public void afterEdit(BillEditEvent e) {
			try {
				int row = e.getRow();
				String key = e.getKey();
				if ("spec_t".equals(key)) {// 托盘规格 计算托盘重量 托盘尺寸
					// 项目主键 def1 托盘重量
					// 项目主键 def2 托盘尺寸
					String value = (String) e.getValue();
					if (StringUtil.isEmpty(value)) {
						getBodyBillModel().setValueAt(null, row, "def1");
						getBodyBillModel().setValueAt(null, row, "def2");

					} else {
						DefdocVO defvo = getDefdocVO(value);
						if (defvo == null) {
							handleException(new BusinessException("托盘规格不存在"));
						}
						getBodyBillModel().setValueAt(defvo.getDef4(), row,
								"def1");
						String def2 = defvo.getDef1() + " " + defvo.getDef2()
								+ " " + defvo.getDef3();
						getBodyBillModel().setValueAt(def2, row, "def2");
					}

				} else if ("npiece".equals(key)) {// 包装件数
					calSpecData(row);
					calJianshu();
				} else if ("spec".equals(key)) {// 包装规格
					UIRefPane refpane = (UIRefPane) getBodyBillModel()
							.getItemByKey("spec").getComponent();
					String spec = refpane.getRefPK();
					getBodyBillModel().setValueAt(spec, row, "pk_defdoec");
					calSpecData(row);
					calJianshu();
				}
				String headPK = getBillRowManager().getSelectedHeadPk();
				getRefContext()
						.getRefBill()
						.getRefBillModel()
						.updateBodyRowVOs(
								headPK,
								getRefContext()
										.getRefBill()
										.getBillListPanel()
										.getBodyBillModel()
										.getBodyValueVOs(
												SalePackListBVO.class.getName()));
				// String headPK = getBillRowManager().getSelectedHeadPk();
				// String bodyPK = getBillRowManager().getBodyPKByRow(row);
				// CircularlyAccessibleValueObject bodyVO = getBodyVO(row);
				// getRefContext().getRefBill().getRefBillModel()
				// .updateBodyRowVO(headPK, bodyPK, bodyVO);
			} catch (Exception e1) {
				e1.printStackTrace();
				handleException(e1);
			}
		}

		public void bodyRowChange(BillEditEvent e) {

		}
	}

	private void calJianshu() {
		int rowcount = getBodyBillModel().getRowCount();

		UFDouble tnpiece = UFDouble.ZERO_DBL;

		for (int i = 0; i < rowcount; i++) {
			UFDouble npiece = (UFDouble) getBodyBillModel().getValueAt(i,
					"nweight");
			tnpiece = SafeCompute.add(tnpiece, npiece);
		}
		int row = getHeadTable().getSelectedRow();
		getHeadBillModel().setValueAt(tnpiece, row, "def1");
		String headPK = getBillRowManager().getSelectedHeadPk();
		AggregatedValueObject aggvo = getRefContext().getRefBill()
				.getRefBillModel().getBillVO(headPK);
		aggvo.getParentVO().setAttributeValue("def1", tnpiece);

	}

	private void calSpecData(int row) {
		// 项目主键 nweight 净重
		// 项目主键 ntarenum皮重
		// 项目主键 ngrosswt 毛重 毛重=净重+皮重

		String spec = (String) getBodyBillModel().getValueAt(row, "pk_defdoec");
		Integer inpiece = (Integer) getBodyBillModel()
				.getValueAt(row, "npiece");
		UFDouble npiece = UFDouble.ZERO_DBL;
		if (inpiece != null) {
			npiece = new UFDouble(inpiece.doubleValue());
		}

		if (StringUtil.isEmpty(spec) || npiece == null) {
			getBodyBillModel().setValueAt(null, row, "nweight");
			getBodyBillModel().setValueAt(null, row, "ntarenum");
			getBodyBillModel().setValueAt(null, row, "ngrosswt");
		} else {
			DefdocVO defvo = getDefdocVO(spec);
			if (defvo == null) {
				handleException(new BusinessException("包装规格不存在"));
			}
			UFDouble ntarenum = SafeCompute.multiply(
					new UFDouble(defvo.getDef1()), npiece);
			UFDouble nweight = SafeCompute.multiply(
					new UFDouble(defvo.getDef2()), npiece);
			UFDouble ngrosswt = SafeCompute.add(ntarenum, nweight);
			getBodyBillModel().setValueAt(nweight, row, "nweight");
			getBodyBillModel().setValueAt(ntarenum, row, "ntarenum");
			getBodyBillModel().setValueAt(ngrosswt, row, "ngrosswt");
		}

	}

	private BodyEditListener2 editListener2 = new BodyEditListener2();

	public class BodyEditListener2 implements nc.ui.pub.bill.BillEditListener2 {
		public BodyEditListener2() {
		}

		public boolean beforeEdit(BillEditEvent e) {
			try {
				String key = e.getKey();
				int pos = e.getPos();
				int row = getHeadTable().getSelectedRow();
				SalePackListHVO hvo = (SalePackListHVO) getHeadBillModel()
						.getBodyValueRowVO(row, SalePackListHVO.class.getName());
				String pk_org = hvo.getPk_org();
					if ("vdef2".equals(key)) {// 运输储存条件
						UIRefPane refpane = (UIRefPane) getHeadBillModel()
								.getItemByKey("vdef2").getComponent();
						refpane.getRefModel().setPk_org(pk_org);
					}
					//新增编辑前组织控制，用于档案合并补丁
					else if("def2".equals(key)) {// 运输储存条件
						UIRefPane refpane = (UIRefPane) getHeadBillModel()
								.getItemByKey("def2").getComponent();
						refpane.getRefModel().setPk_org(pk_org);
					}else if ("vdef3".equals(key)) {// 备注信息
						UIRefPane refpane = (UIRefPane) getHeadBillModel()
								.getItemByKey("vdef3").getComponent();
						refpane.getRefModel().setPk_org(pk_org);
					}else if ("vdef4".equals(key)) {// 质量标准
						UIRefPane refpane = (UIRefPane) getHeadBillModel()
								.getItemByKey("vdef4").getComponent();
						refpane.getRefModel().setPk_org(pk_org);
					}
					else if ("spec".equals(key)) {// 包装规格
						UIRefPane refpane = (UIRefPane) getBodyBillModel()
								.getItemByKey("spec").getComponent();

						refpane.getRefModel().setPk_org(pk_org);
					}else if ("spec_t".equals(key)) {// 托盘规格
						UIRefPane refpane = (UIRefPane) getBodyBillModel()
								.getItemByKey("spec_t").getComponent();

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

	private void handleException(Exception exception) {
		try {
			ExceptionUtils.wrappException(exception);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}
	}

	boolean isSelectedPanel;
	private BillRowManager billRowManager;

	public class BillRowManager extends RefListPanel.BillRowManager {
		protected synchronized void bodyRowChange(int iNewRow) {

		}
	}

	public BillRowManager getBillRowManager() {
		if (billRowManager == null) {
			billRowManager = new BillRowManager();
		}
		return billRowManager;
	}
}
