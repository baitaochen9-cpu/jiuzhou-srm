package nc.ui.so.salepacklist.action;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.cmp.utils.BillcodeGenerater;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.jzqc.labelprint.action.CommonLabelPrintAction;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.so.salepacklist.ace.view.dialog.ShipMarkLabelPrintDlg;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
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
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.trade.pub.BillStatus;

import org.apache.commons.lang.ArrayUtils;

/**
 * 成品发货标签打印按钮
 * 
 * @author zhw
 * 
 */
public class ShipMarkLabelPrintAction extends CommonLabelPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1811961090975795908L;

	private String transtype;

	public void initPrintInfo() {
		setPreview(false);
		setNodeKey("ot");
		setFuncode("H3010200407");
		setTranstype("JZ01-Cxx-40");
		super.setCode("shipMarkLabel");
		super.setBtnName("成品发货标签打印");
		setActioncode("shipMarkLabel");
		setActionname(getBtnName());
		putValue("ShortDescription", getActionname());
		putValue("AcceleratorKey", null);

	}

	public void doAction(ActionEvent e) throws Exception {

		if (!isJzQCEnable()) {
			throw new BusinessException("九洲质量标签功能未启用，请检查库存组织参数设置！");
		}

		AggSalePackListHVO pvo = (AggSalePackListHVO) this.model
				.getSelectedData();
		AggSalePackListHVO vo = (AggSalePackListHVO) ObjectUtils
				.serializableClone(pvo);
		SalePackListBVO[] bvos = this.getBVOs(vo);
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
		String[] billids = getBillids( bvos);
		maintain.checkExistsAggLabelPrintHVO(vo.getParentVO().getPk_org(),
				billids, getTranstype());
		maintain.checkExistsPower(vo.getParentVO().getPk_org(), getTranstype(),
				vo.getParentVO(), getModel().getContext().getPk_loginUser());
		// 弹窗显示
		SalePackListBVO[] changebvos = createSalePackListBVOs(bvos);

		SalePackListBVO[] changebvos1 = getAggLabelPrintHVOMap(changebvos);
		ShipMarkLabelPrintDlg trayDlg = new ShipMarkLabelPrintDlg(getModel()
				.getContext().getEntranceUI(), vo.getParentVO(), changebvos1);
		int idok = trayDlg.showModal();

		if (UIDialog.ID_OK == idok) {
			SalePackListBVO[] srcvos = trayDlg.getBvos();
			VORowNoUtils.setVOsRowNoByRule(srcvos, "rowno");
			List<AggLabelPrintHVO> list = changeLabelPrintHVO(vo, bvos[0],
					srcvos);
			// 保存标签数据
			Object o = savechangeAggLabelPrintHVO(list);
			AggLabelPrintHVO[] hvos = getAggLabelPrintHVOs((AggLabelPrintHVO[]) o);
			doPrint(hvos);
			ShowStatusBarMsgUtil.showStatusBarMsg("标签打印完成！", getModel()
					.getContext());
		}
	}

	private SalePackListBVO[] getAggLabelPrintHVOMap(SalePackListBVO[] bvos)
			throws BusinessException {

		LinkedHashMap<String, List<SalePackListBVO>> map = new LinkedHashMap<String, List<SalePackListBVO>>();
		for (SalePackListBVO vo : bvos) {

			String key = vo.getBatchcode() + "&" + vo.getSpec();
			List<SalePackListBVO> list = null;
			if (map.containsKey(key)) {
				list = map.get(key);
			} else {
				list = new ArrayList<>();
			}
			list.add(vo);
			map.put(key, list);
		}

		List<SalePackListBVO> alist = new ArrayList<>();
		for (List<SalePackListBVO> list : map.values()) {
			for (SalePackListBVO aggvo : list) {
				alist.add(aggvo);
			}
		}

		return alist.toArray(new SalePackListBVO[alist.size()]);
	}

	private AggLabelPrintHVO[] getAggLabelPrintHVOs(
			AggLabelPrintHVO[] changebvos) throws Exception {

		LinkedHashMap<String, List<AggLabelPrintHVO>> map = new LinkedHashMap<String, List<AggLabelPrintHVO>>();
		for (AggLabelPrintHVO aggvo : changebvos) {

			LabelPrintHVO vo = aggvo.getParentVO();
			String key = vo.getVbatchcode() + "&" + vo.getCouterpackspec();
			List<AggLabelPrintHVO> list = null;
			if (map.containsKey(key)) {
				list = map.get(key);
			} else {
				list = new ArrayList<>();
			}
			list.add(aggvo);
			map.put(key, list);
		}
		List<AggLabelPrintHVO> alist = new ArrayList<>();
		for (List<AggLabelPrintHVO> list : map.values()) {
			for (AggLabelPrintHVO aggvo : list) {
				alist.add(aggvo);
			}

			AggLabelPrintHVO sbvo = (AggLabelPrintHVO) ObjectUtils
					.serializableClone(list.get(list.size() - 1));
			alist.add(sbvo);
		}

		return alist.toArray(new AggLabelPrintHVO[alist.size()]);
	}

	protected Object savechangeAggLabelPrintHVO(List<AggLabelPrintHVO> list) {
		Object o = PfServiceScmUtil.processBatch("SAVEBASE", "JZ01",
				list.toArray(new AggLabelPrintHVO[list.size()]), null, null);
		return o;
	}

	protected List<AggLabelPrintHVO> changeLabelPrintHVO(AggSalePackListHVO vo,
			SalePackListBVO totalvo, SalePackListBVO[] bvos)
			throws ValidationException, BusinessException {

		if (bvos.length == 0 || bvos[0] == null) {
			ExceptionUtils.wrappBusinessException("没有要打印的数据！");
		}
		List<AggLabelPrintHVO> list = new ArrayList<>();
		int size = bvos.length;
		BillcodeGenerater generater = new BillcodeGenerater();
		String produceno = generater.getBillCode("JZQC", vo.getParentVO()
				.getPk_group(), "GLOBLE00000000000000", null, null);
		
		for (SalePackListBVO bvo : bvos) {
			AggLabelPrintHVO aggvo = new AggLabelPrintHVO();
			LabelPrintHVO hvo = new LabelPrintHVO();

			// 设置主组织默认值

			hvo.setAttributeValue("pk_group", vo.getParentVO().getPk_group());
			hvo.setAttributeValue("pk_org", vo.getParentVO().getPk_org());
			hvo.setAttributeValue("pkorg", vo.getParentVO().getPk_org());
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
			hvo.setSrcbilltype("4345");// 标签类型
			hvo.setBlabelstatus(UFBoolean.TRUE);// 标签状态
			hvo.setBprintstatus(UFBoolean.TRUE);// 可打印状态
			hvo.setSrcbillid(vo.getParentVO().getPk_salepacklist());// 包装清单id
			hvo.setSrcbillrowid(bvo.getPk_salepacklist_b());// 包装清单子表id
			hvo.setPk_material(vo.getParentVO().getPk_material());// 物料主键
			hvo.setPk_srcmaterial(vo.getParentVO().getPk_srcmaterial());// 物料版本主键
			hvo.setNum_b(bvo.getNweight());// vo.getParentVO()重量
			hvo.setCastunitid(bvo.getUnit());// 包装单位
			hvo.setCunitid(bvo.getUnit());// 计量单位
			BatchcodeVO batch = getBatchcodeVO(bvo.getPk_batchcode());
			if (batch != null) {
				hvo.setVbatchcode(batch.getVbatchcode());// 批次号
				hvo.setPk_batchcode(batch.getPk_batchcode());
				hvo.setBc_vvendbatchcode(batch.getVvendbatchcode());// 供应商批次号
				hvo.setDproducedate(batch.getDproducedate());// 生产日期
				hvo.setEnddate(batch.getDvalidate());// 复测日期
			}
			hvo.setDef2(vo.getParentVO().getDef2());
			hvo.setDef3(vo.getParentVO().getDef3());
			hvo.setDef4(vo.getParentVO().getDef4());
			// hvo.setBc_vvendbatchcode(bvo.get);// 供应商批次号
			hvo.setAmount(totalvo.getNweight());// 总包装数量
			hvo.setSerial_number(Integer.parseInt(bvo.getRowno()));// 标签序号
			hvo.setCouterpackspec(bvo.getSpec());// 包装规格
			hvo.setNum(bvo.getNweight());// 批次总数量
			// hvo.setDproducedate(bvo.getDproducedate());// 生产日期
			// hvo.setEnddate(bvo.getDinvaliddate());// 复测日期
			hvo.setSerial_number(Integer.parseInt(bvo.getRowno()) / 10);// 标签序号
			hvo.setSerial_total(size);
			hvo.setProduceno(produceno);
			aggvo.setParentVO(hvo);
			list.add(aggvo);
		}

		return list;
	}
	
	private String[] getBillids(SalePackListBVO[] hvos) throws Exception {

		List<String> list = new ArrayList<>();
		for (SalePackListBVO hvo : hvos) {
			if (!list.contains(hvo.getPrimaryKey())) {
				list.add(hvo.getPrimaryKey());
			}
		}
		return list.toArray(new String[list.size()]);
	}

	private SalePackListBVO[] createSalePackListBVOs(SalePackListBVO[] hvos)
			throws Exception {

		List<SalePackListBVO> list = new ArrayList<>();
		for (SalePackListBVO hvo : hvos) {
			SalePackListBVO[] bvos = createSalePackListBVOs(hvo);
			int size = bvos.length;
			for (int i = 0; i < size; i++) {
				list.add(bvos[i]);
			}

			// SalePackListBVO bvo1 = (SalePackListBVO) ObjectUtils
			// .serializableClone(bvos[size - 1]);
			// list.add(bvo1);
		}
		return list.toArray(new SalePackListBVO[list.size()]);
	}

	private SalePackListBVO[] createSalePackListBVOs(SalePackListBVO hvo)
			throws Exception {

		if (StringUtil.isEmpty(hvo.getSpec())) {
			throw new BusinessException("包装规格不能为空");
		}
		DefdocVO defvo = getDefdocVO(hvo.getSpec());

		if (defvo == null) {
			throw new BusinessException("包装规格不存在");
		}
		hvo.setRowno(null);
		BigDecimal[] bigs = getNnpiece(hvo, defvo);

		int size = bigs[0].intValue();
		List<SalePackListBVO> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			SalePackListBVO sbvo = getSalePackListBVO(hvo, bigs[2]);
			list.add(sbvo);
		}
		// SalePackListBVO sbvo = getSalePackListBVO(hvo, bigs[2]);
		// list.add(sbvo);
		if (bigs[1].compareTo(BigDecimal.ZERO) != 0) {
			SalePackListBVO bvo1 = getSalePackListBVO(hvo, bigs[1]);
			list.add(bvo1);
		}
		return list.toArray(new SalePackListBVO[list.size()]);
	}

	private SalePackListBVO getSalePackListBVO(SalePackListBVO hvo,
			BigDecimal npiece) throws Exception {
		SalePackListBVO sbvo = (SalePackListBVO) ObjectUtils
				.serializableClone(hvo);
		sbvo.setNweight(new UFDouble(npiece));
		return sbvo;
	}

	// 商 和 余数
	private BigDecimal[] getNnpiece(SalePackListBVO hvo, DefdocVO defvo)
			throws BusinessException {
		UFDouble ufdouble = hvo.getNweight();
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

	// 包装规格
	private MaterialStockVO[] getMaterialStockVO(String pk_org,
			String pk_material) throws UifException {
		MaterialStockVO[] bvo = (MaterialStockVO[]) HYPubBO_Client
				.queryByCondition(MaterialStockVO.class, " pk_org = '" + pk_org
						+ "' and pk_material ='" + pk_material
						+ "' and nvl(dr,0)=0");
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
	protected SalePackListBVO[] getBVOs(AggSalePackListHVO vo) {
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

		SalePackListBVO[] bvotmps = (SalePackListBVO[]) vo.getChildrenVO();
		// SalePackListBVO[] bvos = new SalePackListBVO[rows.length];
		// for (int i = 0; i < rows.length; i++) {
		//
		// String pk_arriveorder_b = null;
		// // 卡片界面
		// if (((ShowUpableBillForm) this.getForm()).isComponentVisible()) {
		// pk_arriveorder_b = (String) this.getForm().getBillCardPanel()
		// .getBodyValueAt(rows[i], "pk_salepacklist_b");
		// } else {// 列表界面
		// pk_arriveorder_b = (String) this.getList().getBillListPanel()
		// .getBodyBillModel()
		// .getValueAt(rows[i], "pk_salepacklist_b");
		// }
		//
		// for (int j = 0; j < bvotmps.length; j++) {
		// if (bvotmps[j].getPk_salepacklist_b().equals(pk_arriveorder_b)) {
		// bvos[i] = bvotmps[j];
		// break;
		// }
		// }
		// }
		return bvotmps;
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
			return this.isOneVOEnable((AggSalePackListHVO) this.getModel()
					.getSelectedData());
		}
		if (objs.length > 1) {
			return false;
		}
		// 表体报检
		return this.isOneVOEnable((AggSalePackListHVO) objs[0]);
	}

	private boolean isOneVOEnable(AggSalePackListHVO vo) {

		if (BillStatus.CHECKPASS != vo.getParentVO().getApprovestatus()
				.intValue()) {
			return false;
		}
		// int[] selectedRows = null;
		// // 卡片界面
		// if (((ShowUpableBillForm) this.getForm()).isComponentVisible()) {
		// selectedRows = this.getForm().getBillCardPanel().getBodyPanel()
		// .getTable().getSelectedRows();
		// } else {
		// // 列表界面
		// selectedRows = this.getList().getBillListPanel().getBodyTable()
		// .getSelectedRows();
		// }
		// if (selectedRows == null || selectedRows.length == 0) {
		// return false;
		// }
		SalePackListBVO[] orgItems = (SalePackListBVO[]) vo.getChildrenVO();
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