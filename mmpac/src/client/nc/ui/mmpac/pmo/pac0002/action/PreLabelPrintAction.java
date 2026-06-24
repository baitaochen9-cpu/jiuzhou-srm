package nc.ui.mmpac.pmo.pac0002.action;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.cmp.utils.BillcodeGenerater;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.jzqc.labelprint.action.CommonLabelPrintAction;
import nc.ui.mmpac.pmo.pac0002.pint.dialog.PreLabelPrintDlg;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.org.OrgVO;
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
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

import org.apache.commons.lang.ArrayUtils;

/**
 * 预标签打印按钮
 * 
 * @author zhw
 * 
 */
public class PreLabelPrintAction extends CommonLabelPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1811961090975795908L;

	private String transtype;

	public void initPrintInfo() {
		setPreview(false);
		super.setCode("preLabel");
		super.setBtnName("生产标签打印");
		setActioncode("preLabel");
		setNodeKey("ot");
		setFuncode("H3010200402");
		setTranstype("JZ01-Cxx-15");
		setActionname(getBtnName());
		putValue("ShortDescription", getActionname());
		putValue("AcceleratorKey", null);
	}

	public void doAction(ActionEvent e) throws Exception {

		if (!isJzQCEnable()) {
			throw new BusinessException("九洲质量标签功能未启用，请检查库存组织参数设置！");
		}

		PMOAggVO pvo = (PMOAggVO) this.model.getSelectedData();
		PMOAggVO vo = (PMOAggVO) ObjectUtils.serializableClone(pvo);
		PMOItemVO[] bvos = this.getBVOs(vo);
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
		maintain.checkExistsAggLabelPrintHVO(vo.getParentVO().getPk_org(),
				bvos[0].getPrimaryKey(), getTranstype());
		maintain.checkExistsPower(vo.getParentVO().getPk_org(), getTranstype(),
				vo.getParentVO(), getModel().getContext().getPk_loginUser());
		// 弹窗显示
		PMOItemVO[] changebvos = createPMOItemVOs(bvos[0]);
		PreLabelPrintDlg trayDlg = new PreLabelPrintDlg(getModel().getContext()
				.getEntranceUI(), changebvos);
		int idok = trayDlg.showModal();

		if (UIDialog.ID_OK == idok) {
			PMOItemVO[] srcvos = trayDlg.getBvos();
			VORowNoUtils.setVOsRowNoByRule(srcvos, "vrowno");
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

	protected List<AggLabelPrintHVO> changeLabelPrintHVO(PMOItemVO totalvo,
			PMOItemVO[] bvos) throws ValidationException, BusinessException {

		if (bvos.length == 0 || bvos[0] == null) {
			ExceptionUtils.wrappBusinessException("没有要打印的数据！");
		}
		List<AggLabelPrintHVO> list = new ArrayList<>();
		int size = bvos.length;
		BillcodeGenerater generater = new BillcodeGenerater();
		String produceno = generater.getBillCode("JZQC", totalvo.getPk_group(),
				"GLOBLE00000000000000", null, null);
		BatchcodeVO batch = getBatchcodeVO(totalvo.getCmaterialvid(),
				totalvo.getVbatchcode());
		for (PMOItemVO bvo : bvos) {
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
			hvo.setSrcbilltype("55A2");// 标签类型
			hvo.setBlabelstatus(UFBoolean.TRUE);// 标签状态
			hvo.setBprintstatus(UFBoolean.TRUE);// 可打印状态
			hvo.setSrcbillid(totalvo.getCpmohid());// 流程生产单id
			hvo.setSrcbillrowid(bvo.getCmoid());// 流程生产单子表id
			hvo.setPk_material(bvo.getCmaterialvid());// 物料主键
			hvo.setPk_srcmaterial(bvo.getCmaterialid());// 物料版本主键
			hvo.setNum_b(bvo.getNmmastnum());// 重量
			hvo.setCastunitid(bvo.getCastunitid());// 包装单位
			hvo.setCunitid(bvo.getCunitid());// 计量单位
			if (batch != null) {
				hvo.setVbatchcode(batch.getVbatchcode());// 批次号
				hvo.setPk_batchcode(batch.getPk_batchcode());
				hvo.setBc_vvendbatchcode(batch.getVvendbatchcode());// 供应商批次号
				hvo.setDproducedate(batch.getDproducedate());// 生产日期
				hvo.setEnddate(batch.getDvalidate());// 复测日期
			}
			hvo.setAmount(totalvo.getNmmastnum());// 总包装数量
			hvo.setSerial_number(Integer.parseInt(bvo.getVrowno()));// 标签序号
			hvo.setCouterpackspec(bvo.getVdef2());// 包装规格
			hvo.setNum(bvo.getNmmnum());// 批次总数量
			hvo.setDef5(bvo.getVbatchcode());
			// hvo.setDproducedate(bvo.getDproducedate());// 生产日期
			// hvo.setEnddate(bvo.getDinvaliddate());// 复测日期
			hvo.setSerial_number(Integer.parseInt(bvo.getVrowno()) / 10);// 标签序号
			hvo.setSerial_total(size);
			hvo.setProduceno(produceno);
			aggvo.setParentVO(hvo);
			list.add(aggvo);
		}

		return list;
	}

	private PMOItemVO[] createPMOItemVOs(PMOItemVO hvo) throws Exception {

		if (StringUtil.isEmpty(hvo.getVdef3())) {
			throw new BusinessException("包装规格不能为空");
		}
		DefdocVO defvo = getDefdocVO(hvo.getVdef3());

		if (defvo == null) {
			throw new BusinessException("包装规格不存在");
		}
		hvo.setVrowno(null);
		BigDecimal[] bigs = getNnpiece(hvo, defvo);

		int size = bigs[0].intValue();
		List<PMOItemVO> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			PMOItemVO sbvo = getPMOItemVO(hvo, bigs[2]);
			list.add(sbvo);
		}

		if (bigs[1].compareTo(BigDecimal.ZERO) != 0) {
			PMOItemVO bvo1 = getPMOItemVO(hvo, bigs[1]);
			list.add(bvo1);
		}

		return list.toArray(new PMOItemVO[list.size()]);
	}

	private PMOItemVO getPMOItemVO(PMOItemVO hvo, BigDecimal npiece)
			throws Exception {
		PMOItemVO sbvo = (PMOItemVO) ObjectUtils.serializableClone(hvo);
		sbvo.setNmmastnum(new UFDouble(npiece));
		sbvo.setNmmastnum(null);
		return sbvo;
	}

	// 商 和 余数
	private BigDecimal[] getNnpiece(PMOItemVO hvo, DefdocVO defvo)
			throws BusinessException {
		UFDouble ufdouble = hvo.getNmmastnum();
		if (ufdouble == null)
			throw new BusinessException("数量不能为空!");
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
	protected PMOItemVO[] getBVOs(PMOAggVO vo) {
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

		PMOItemVO[] bvotmps = (PMOItemVO[]) vo.getChildrenVO();
		PMOItemVO[] bvos = new PMOItemVO[rows.length];
		for (int i = 0; i < rows.length; i++) {

			String pk_arriveorder_b = null;
			// 卡片界面
			if (((ShowUpableBillForm) this.getForm()).isComponentVisible()) {
				pk_arriveorder_b = (String) this.getForm().getBillCardPanel()
						.getBodyValueAt(rows[i], PMOItemVO.CMOID);
			} else {// 列表界面
				pk_arriveorder_b = (String) this.getList().getBillListPanel()
						.getBodyBillModel()
						.getValueAt(rows[i], PMOItemVO.CMOID);
			}

			for (int j = 0; j < bvotmps.length; j++) {
				if (bvotmps[j].getCmoid().equals(pk_arriveorder_b)) {
					bvos[i] = bvotmps[j];
					break;
				}
			}
		}
		return bvos;
	}

	@Override
	protected boolean isActionEnable() {
		if (this.getModel().getAppUiState() == AppUiState.EDIT
				|| this.getModel().getSelectedData() == null) {
			return true;
		}

		Object[] objs = ((BillManageModel) this.getModel())
				.getSelectedOperaDatas();

		if (this.getModel().getSelectedData() != null
				&& ArrayUtils.isEmpty(objs)) {
			return this.isOneVOEnable((PMOAggVO) this.getModel()
					.getSelectedData());
		}
		if (objs.length > 1) {
			return false;
		}
		// 表体报检
		return this.isOneVOEnable((PMOAggVO) objs[0]);
	}

	private boolean isOneVOEnable(PMOAggVO vo) {

		OrgVO org = getOrgVO(vo.getParentVO().getPk_org());
		if (org == null || StringUtil.isEmpty(org.getPk_org())) {
			return false;
		}

		if (!"28".equals(org.getCode())) {
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
		PMOItemVO[] orgItems = vo.getChildrenVO();
		if (orgItems == null) {
			return false;
		}

		if (1 != orgItems[0].getFitemstatus().intValue())// 不是投放状态
			return false;
		return true;
	}

	private OrgVO getOrgVO(String pk_org) {
		List<OrgVO> list = getModel().getContext().getOrgvos();
		for (OrgVO org : list) {
			if (org.getPk_org().equals(pk_org))
				return org;
		}
		return null;
	}

	public String getTranstype() {
		return transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

}