package nc.ui.pu.m23.action;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.cmp.utils.BillcodeGenerater;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.ui.jzqc.labelprint.action.CommonLabelPrintAction;
import nc.ui.pu.m23.print.dialog.SingletonLabelPrintDlg;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.ObjectUtils;
import nc.vo.pubapp.util.VORowNoUtils;

/**
 * 原料待验标签打印按钮
 * 
 * @author zhw
 * 
 */
public class SingletonLabelPrintAction extends CommonLabelPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1811961090975795908L;

	private String transtype;

	public void initPrintInfo() {
		setPreview(false);
		super.setCode("singLabel");
		super.setBtnName("原料待验标签打印");
		setActioncode("singLabel");
		setNodeKey("ot");
		setFuncode("H3010200400");
		setTranstype("JZ01-Cxx-05");
		setActionname(getBtnName());
		putValue("ShortDescription", getActionname());
		putValue("AcceleratorKey", null);
	}

	public void doAction(ActionEvent e) throws Exception {

		if (!isJzQCEnable()) {
			throw new BusinessException("九洲质量标签功能未启用，请检查库存组织参数设置！");
		}

		ArriveVO pvo = getArriveVO();
		ArriveVO vo = (ArriveVO) ObjectUtils.serializableClone(pvo);
		ArriveItemVO[] bvos = this.getBVOs(vo);
		if (bvos.length == 0 || bvos[0] == null) {
			ExceptionUtils
					.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("4004040_0",
									"04004040-0005")/*
													 * @res "请选中表体行"
													 */);
		}
		// 校验是否已存在标签档案
		ILabelprintMaintain maintain = (ILabelprintMaintain) NCLocator
				.getInstance().lookup(ILabelprintMaintain.class.getName());
		maintain.checkExistsAggLabelPrintHVO(vo.getHVO().getPk_org(),
				bvos[0].getPrimaryKey(), getTranstype());
		maintain.checkExistsPower(vo.getHVO().getPk_org(), getTranstype(),
				vo.getHVO(), getModel().getContext().getPk_loginUser());
		// 弹窗显示
		ArriveItemVO[] changebvos = createArriveItemVOs(bvos[0]);
		SingletonLabelPrintDlg trayDlg = new SingletonLabelPrintDlg(getModel()
				.getContext().getEntranceUI(), bvos[0], changebvos);
		int idok = trayDlg.showModal();

		if (UIDialog.ID_OK == idok) {
			ArriveItemVO[] srcvos = trayDlg.getBvos();
			VORowNoUtils.setVOsRowNoByRule(srcvos, "crowno");
			List<AggLabelPrintHVO> list = changeLabelPrintHVO(bvos[0], srcvos);
			// 保存标签数据
			Object o = savechangeAggLabelPrintHVO(list);
			// AggLabelPrintHVO[] hvos = getAggLabelPrintHVO();
			doPrint((AggLabelPrintHVO[]) o);
			ShowStatusBarMsgUtil.showStatusBarMsg("标签打印完成！", getModel()
					.getContext());
		}
	}

	protected Object savechangeAggLabelPrintHVO(List<AggLabelPrintHVO> list) {
		Object o = PfServiceScmUtil.processBatch("SAVEBASE", "JZ01",
				list.toArray(new AggLabelPrintHVO[list.size()]), null, null);
		return o;
	}

	protected List<AggLabelPrintHVO> changeLabelPrintHVO(ArriveItemVO totalvo,
			ArriveItemVO[] bvos) throws ValidationException, BusinessException {

		if (bvos.length == 0 || bvos[0] == null) {
			ExceptionUtils.wrappBusinessException("没有要打印的数据！");
		}
		List<AggLabelPrintHVO> list = new ArrayList<>();
		int size = bvos.length;
		BillcodeGenerater generater = new BillcodeGenerater();
		String produceno = generater.getBillCode("JZQC", totalvo.getPk_group(),
				"GLOBLE00000000000000", null, null);
		for (ArriveItemVO bvo : bvos) {
			AggLabelPrintHVO aggvo = new AggLabelPrintHVO();
			LabelPrintHVO hvo = new LabelPrintHVO();

			// 设置主组织默认值

			hvo.setAttributeValue("pk_group", bvo.getPk_group());
			hvo.setAttributeValue("pk_org", bvo.getPk_org());
			hvo.setAttributeValue("pkorg", bvo.getPk_org());
			// 设置单据状态、单据业务日期默认值
			hvo.setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
			hvo.setAttributeValue("billdate",
					new UFDate(System.currentTimeMillis()));
			hvo.setAttributeValue("billtype", "JZ01");
			hvo.setAttributeValue("transtype", getTranstype());
			hvo.setAttributeValue("transtypepk", PfServiceScmUtil
					.getTrantypeidByCode(new String[] { hvo.getTranstype() })
					.get(hvo.getTranstype()));

			hvo.setAttributeValue("creationtime",
					new UFDateTime(System.currentTimeMillis()));
			hvo.setAttributeValue("creator", getModel().getContext()
					.getPk_loginUser());
			hvo.setAttributeValue("maketime",
					new UFDateTime(System.currentTimeMillis()));
			hvo.setAttributeValue("billmaker", getModel().getContext()
					.getPk_loginUser());

			hvo.setAttributeValue("iprintcount", 0);// 打印次数
			hvo.setSrcbilltype("23");// 标签类型
			hvo.setBlabelstatus(UFBoolean.TRUE);// 标签状态
			hvo.setBprintstatus(UFBoolean.TRUE);// 可打印状态
			hvo.setSrcbillid(totalvo.getPk_arriveorder());// 到货单id
			hvo.setSrcbillrowid(bvo.getPk_arriveorder_b());// 到货单子表id
			hvo.setPk_material(bvo.getPk_material());// 物料主键
			hvo.setPk_srcmaterial(bvo.getPk_srcmaterial());// 物料版本主键
			hvo.setNum_b(bvo.getNastnum());// 重量
			hvo.setCastunitid(bvo.getCastunitid());// 包装单位
			hvo.setCunitid(bvo.getCunitid());// 计量单位
			hvo.setVbatchcode(bvo.getVbatchcode());// 批次号
			hvo.setPk_batchcode(bvo.getPk_batchcode());
			hvo.setBc_vvendbatchcode(bvo.getBc_vvendbatchcode());// 供应商批次号
			hvo.setAmount(totalvo.getNastnum());// 总包装数量
			hvo.setSerial_number(Integer.parseInt(bvo.getCrowno()) / 10);// 标签序号
			hvo.setSerial_total(size);
			hvo.setProduceno(produceno);
			hvo.setCouterpackspec(bvo.getVbdef2());// 包装规格
			hvo.setNum(bvo.getNnum());// 批次总数量
			hvo.setDproducedate(bvo.getDproducedate());// 生产日期
			hvo.setEnddate(bvo.getDinvaliddate());// 复测日期
			aggvo.setParentVO(hvo);
			list.add(aggvo);
		}

		return list;
	}

	private ArriveItemVO[] createArriveItemVOs(ArriveItemVO hvo)
			throws Exception {

		if (StringUtil.isEmpty(hvo.getVbdef8())) {
			throw new BusinessException("包装规格不能为空");
		}
		DefdocVO defvo = getDefdocVO(hvo.getVbdef8());

		if (defvo == null) {
			throw new BusinessException("包装规格不存在");
		}
		hvo.setCrowno(null);
		BigDecimal[] bigs = getNnpiece(hvo, defvo);

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
		sbvo.setVbdef2(sbvo.getVbdef8());
		sbvo.setNastnum(new UFDouble(npiece));
		return sbvo;
	}

	// 商 和 余数
	private BigDecimal[] getNnpiece(ArriveItemVO hvo, DefdocVO defvo)
			throws BusinessException {
		UFDouble ufdouble = hvo.getNastnum();
		if (ufdouble == null)
			throw new BusinessException("数量不能为空!");
		List<BigDecimal> list = new ArrayList<>();
		if ("VAR".equals(defvo.getCode())) {
			list.add(BigDecimal.ONE);
			list.add(BigDecimal.ZERO);
			list.add(BigDecimal.ONE);
		} else {
			BigDecimal bweight = getNweight(defvo);
			BigDecimal bgrosswt = ufdouble.toBigDecimal();
			BigDecimal[] bigs = bgrosswt.divideAndRemainder(bweight);
			for (BigDecimal big : bigs) {
				list.add(big);
			}
			list.add(bweight);
		}

		return list.toArray(new BigDecimal[list.size()]);
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

	/**
	 * 得到选中的bvo
	 * 
	 * @param vo
	 */
	protected ArriveItemVO[] getBVOs(ArriveVO vo) {
		int[] rows = null;
		// 卡片界面
		if (((ShowUpableBillForm) this.getForm()).isComponentVisible()) {
			BillCardPanel panel = this.getForm().getBillCardPanel();
			rows = panel.getBodyPanel().getTable().getSelectedRows();
		} else {
			// 列表界面
			BillListPanel panel = this.getList().getBillListPanel();
			rows = panel.getBodyTable().getSelectedRows();
		}

		ArriveItemVO[] bvotmps = (ArriveItemVO[]) vo.getChildrenVO();
		ArriveItemVO[] bvos = new ArriveItemVO[rows.length];
		for (int i = 0; i < rows.length; i++) {

			String pk_arriveorder_b = null;
			// 卡片界面
			if (((ShowUpableBillForm) this.getForm()).isComponentVisible()) {
				pk_arriveorder_b = (String) this.getForm().getBillCardPanel()
						.getBodyValueAt(rows[i], ArriveItemVO.PK_ARRIVEORDER_B);
			} else {// 列表界面
				pk_arriveorder_b = (String) this.getList().getBillListPanel()
						.getBodyBillModel()
						.getValueAt(rows[i], ArriveItemVO.PK_ARRIVEORDER_B);
			}

			for (int j = 0; j < bvotmps.length; j++) {
				if (bvotmps[j].getPk_arriveorder_b().equals(pk_arriveorder_b)) {
					bvos[i] = bvotmps[j];
					break;
				}
			}
		}
		return bvos;
	}

	protected ArriveVO getArriveVO() {
		ArriveVO aggVO = (ArriveVO) this.getForm().getValue();
		return aggVO;
	}

	protected boolean isJzQCEnable() {
		boolean yf635 = false;
		ArriveVO vo = getArriveVO();
		if (vo != null) {
			try {
				yf635 = SysinitAccessor
						.getInstance()
						.getParaBoolean(
								(String) vo.getParentVO().getAttributeValue(
										"pk_org"), "YF636").booleanValue();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return yf635;
	}

	@Override
	protected boolean isActionEnable() {
		if (this.getModel().getAppUiState() == AppUiState.EDIT
				|| this.getModel().getSelectedData() == null) {
			return true;
		}

		ArriveVO vo = getArriveVO();
		if (vo == null) {
			return false;
		}
		// 表体报检
		return this.isOneVOEnable(vo);
	}

	private boolean isOneVOEnable(ArriveVO vo) {
		if (!POEnumBillStatus.APPROVE.value().equals(
				vo.getHVO().getFbillstatus())) {
			return false;
		}
		int[] selectedRows = null;
		// 卡片界面
		if (((ShowUpableBillForm) this.getForm()).isComponentVisible()) {
			selectedRows = this.getForm().getBillCardPanel().getBodyPanel()
					.getTable().getSelectedRows();
		} else {
			// 列表界面
			selectedRows = this.getList().getBillListPanel().getBodyTable()
					.getSelectedRows();
		}
		if (selectedRows == null || selectedRows.length == 0) {
			return false;
		}
		ArriveItemVO[] orgItems = vo.getBVO();
		if (orgItems == null) {
			return false;
		}
		return true;
	}

	public String getTranstype() {
		return transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

}