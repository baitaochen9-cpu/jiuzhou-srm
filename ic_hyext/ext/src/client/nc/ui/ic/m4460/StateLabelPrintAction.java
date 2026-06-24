package nc.ui.ic.m4460;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.cmp.utils.BillcodeGenerater;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.ui.jzqc.labelprint.action.CommonLabelPrintAction;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.jzqc.labelstatus.LabelStatusVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.ObjectUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.funcreg.ParamRegVO;
import nc.vo.trade.voutils.SafeCompute;

import org.apache.commons.lang.ArrayUtils;

/**
 * 状态标签打印按钮
 * 
 * @author zhw
 * 
 */
public class StateLabelPrintAction extends CommonLabelPrintAction {
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
		super.setCode("stateMarkLabel");
		super.setBtnName("状态标签打印");
		setActioncode("stateMarkLabel");
		setActionname(getBtnName());
		putValue("ShortDescription", getActionname());
		putValue("AcceleratorKey", null);

	}

	public void doAction(ActionEvent e) throws Exception {

		StateAdjustVO[] vos = (StateAdjustVO[]) this.getList()
				.getBillListPanel().getHeadBillModel()
				.getBodySelectedVOs(StateAdjustVO.class.getName());
		if (ValueCheckUtil.isNullORZeroLength(vos)) {
			MessageDialog.showErrorDlg(null, "提示", "请选择要打印的库存状态记录");
			return;
		}
		if (!StringUtil.isEmpty(vos[0].getPk_org())) {
			throw new BusinessException("请到查询库存状态调整记录界面数据！");
		}

		Map<String, OnhandDimVO> map = getOnhandDimVO(vos);
		StateAdjustVO pvo = getStateAdjustVO(vos, map);

		OnhandDimVO dimvo = map.get(pvo.getPk_onhanddim_adj());
		if (dimvo == null) {
			throw new BusinessException("现存量维度不存在");
		}

		StoreStateVO statevo = getStateVO(dimvo.getCstateid());
		if (statevo == null) {
			throw new BusinessException("库存状态不存在");
		}

		checkState(statevo);
		setPrintTranstype(statevo, vos[0].getPk_org());
		StateAdjustVO selectedData = (StateAdjustVO) ObjectUtils
				.serializableClone(pvo);
		if (!isJzQCEnable(selectedData)) {
			throw new BusinessException("九洲质量标签功能未启用，请检查库存组织参数设置！");
		}

		// 校验是否已存在标签档案
		ILabelprintMaintain maintain = (ILabelprintMaintain) NCLocator
				.getInstance().lookup(ILabelprintMaintain.class.getName());
		String sss = selectedData.getVdef8();
		if (StringUtil.isEmpty(sss)) {
			throw new BusinessException("请到查询库存状态调整记录界面数据！");
		}
		maintain.checkExistsAggLabelPrintHVO(selectedData.getPk_org(), sss,
				getTranstype());
		maintain.checkExistsPower(selectedData.getPk_org(), getTranstype(),
				selectedData, getModel().getContext().getPk_loginUser());
		// 弹窗显示
		// StateAdjustVO[] changebvos = createStateAdjustVOs(selectedData);
		StateLabelPrintDlg trayDlg = new StateLabelPrintDlg(getModel()
				.getContext().getEntranceUI(),
				new StateAdjustVO[] { selectedData });
		int idok = trayDlg.showModal();

		if (UIDialog.ID_OK == idok) {
			StateAdjustVO[] srcvos = trayDlg.getBvos();
			VORowNoUtils.setVOsRowNoByRule(srcvos, "cadjustrowno");
			List<AggLabelPrintHVO> list = changeLabelPrintHVO(selectedData,
					srcvos);
			// 保存标签数据
			Object o = savechangeAggLabelPrintHVO(list);
			// AggLabelPrintHVO[] hvos = getAggLabelPrintHVO();
			doPrint((AggLabelPrintHVO[]) o);
			ShowStatusBarMsgUtil.showStatusBarMsg("标签打印完成！", getModel()
					.getContext());
		}
	}

	private StateAdjustVO getStateAdjustVO(StateAdjustVO[] vos,
			Map<String, OnhandDimVO> hmap) throws BusinessException {

		Map<String, StateAdjustVO> map = new HashMap<String, StateAdjustVO>();
		for (StateAdjustVO vo : vos) {
			OnhandDimVO hvo = hmap.get(vo.getPk_onhanddim_adj());
			if (hvo == null)
				throw new BusinessException("物料现存量维度不存在，请检查！");

			String key = hvo.getVbatchcode() + "&" + hvo.getCmaterialoid()
					+ "&" + hvo.getCstateid();
			vo.setPk_org(hvo.getPk_org());
			if (!"Y".equals(vo.getAttributeValue("vdef1"))) {
				continue;
			}
			if (map.containsKey(key)) {
				StateAdjustVO vo1 = map.get(key);
				vo.setNadjustnum(SafeCompute.add(vo.getNadjustnum(),
						vo1.getNadjustnum()));
				vo.setNadjustassistnum(SafeCompute.add(
						vo.getNadjustassistnum(), vo1.getNadjustassistnum()));
				vo.setNassistnum(SafeCompute.add(vo.getNassistnum(),
						vo1.getNassistnum()));
				vo.setNnum(SafeCompute.add(vo.getNnum(), vo1.getNnum()));
				vo.setVdef9((!StringUtil.isEmpty(vo1.getVdef9()) ? vo1
						.getVdef9() : "") + "," + vo.getPk_onhanddim_adj());// //
																			// 现存量维度id
				vo.setVdef8((!StringUtil.isEmpty(vo1.getVdef8()) ? vo1
						.getVdef8() : "") + "," + vo.getCstateadjustid());// //
																			// 调整记录id
			} else {
				vo.setVdef9(vo.getPk_onhanddim_adj());
				vo.setVdef8(vo.getCstateadjustid());
			}
			map.put(key, vo);
		}

		if (map.size() > 1) {
			throw new BusinessException("请勾选同一批次同一存货同一状态的记录进行标签打印！");
		}
		return map.values().toArray(new StateAdjustVO[map.size()])[0];
	}

	protected Object savechangeAggLabelPrintHVO(List<AggLabelPrintHVO> list) {
		Object o = PfServiceScmUtil.processBatch("SAVEBASE", "JZ01",
				list.toArray(new AggLabelPrintHVO[list.size()]), null, null);
		return o;
	}

	protected List<AggLabelPrintHVO> changeLabelPrintHVO(StateAdjustVO totalvo,
			StateAdjustVO[] bvos) throws BusinessException {

		if (bvos.length == 0 || bvos[0] == null) {
			ExceptionUtils.wrappBusinessException("没有要打印的数据！");
		}

		OnhandDimVO dimvo = (OnhandDimVO) HYPubBO_Client.queryByPrimaryKey(
				OnhandDimVO.class, bvos[0].getPk_onhanddim_adj());
		if (dimvo == null) {
			throw new BusinessException("现存量维度不存在");
		}

		BatchcodeVO batch = getBatchcodeVO(dimvo.getPk_batchcode());

		StoreStateVO statevo = getStateVO(dimvo.getCstateid());
		if (statevo == null) {
			throw new BusinessException("库存状态不存在");
		}

		checkState(statevo);
		setPrintTranstype(statevo, bvos[0].getPk_org());

		List<AggLabelPrintHVO> list = new ArrayList<>();
		int size = bvos.length;
		BillcodeGenerater generater = new BillcodeGenerater();
		String produceno = generater.getBillCode("JZQC", totalvo.getPk_group(),
				"GLOBLE00000000000000", null, null);
		for (StateAdjustVO bvo : bvos) {
			AggLabelPrintHVO aggvo = new AggLabelPrintHVO();
			LabelPrintHVO hvo = new LabelPrintHVO();

			// 设置主组织默认值
			hvo.setAttributeValue("pk_group", totalvo.getPk_group());
			hvo.setAttributeValue("pk_org", totalvo.getPk_org());
			hvo.setAttributeValue("pkorg", totalvo.getPk_org());
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
			hvo.setSrcbilltype("4460");// 标签类型
			hvo.setBlabelstatus(UFBoolean.TRUE);// 标签状态
			hvo.setBprintstatus(UFBoolean.TRUE);// 可打印状态
			hvo.setSrcbillid(totalvo.getVdef9()); // 现存量维度id
			hvo.setSrcbillrowid(totalvo.getVdef8());// 调整记录id

			hvo.setPk_material(dimvo.getCmaterialoid());// 物料主键
			hvo.setPk_srcmaterial(dimvo.getCmaterialvid());//
			// 物料版本主键
			hvo.setNum_b(bvo.getNnum());// 重量
			hvo.setCastunitid(bvo.getCastunitid());// 包装单位
			hvo.setCunitid(bvo.getCunitid());// 计量单位

			hvo.setAmount(totalvo.getNadjustassistnum());// 总包装数量
			hvo.setCouterpackspec((String) bvo.getAttributeValue("vdef2"));// 包装规格
			hvo.setNum(bvo.getNadjustassistnum());// 批次总数量

			// nassistnum
			if (batch != null) {
				hvo.setVbatchcode(batch.getVbatchcode());// 批次号
				hvo.setPk_batchcode(batch.getPk_batchcode());
				hvo.setBc_vvendbatchcode(batch.getVvendbatchcode());// 供应商批次号
				hvo.setDproducedate(batch.getDproducedate());// 生产日期
				hvo.setEnddate(batch.getDvalidate());// 复测日期
			}
			hvo.setSerial_number(Integer.parseInt(bvo.getCadjustrowno()) / 10);// 标签序号
			hvo.setSerial_total(size);
			hvo.setProduceno(produceno);
			aggvo.setParentVO(hvo);
			list.add(aggvo);
		}

		return list;
	}

	protected void checkState(StoreStateVO statevo) throws BusinessException {
//		if ("buhege".equals(statevo.getVcode())) {
//			throw new BusinessException("调整状态为不合格状态，请选拒绝标签打印");
//		}
	}

	private StateAdjustVO[] createStateAdjustVOs(StateAdjustVO hvo)
			throws Exception {

		OnhandDimVO dimvo = (OnhandDimVO) HYPubBO_Client.queryByPrimaryKey(
				OnhandDimVO.class, hvo.getPk_onhanddim_adj());

		if (dimvo == null) {
			throw new BusinessException("现存量维度不存在");
		}

		MaterialStockVO[] stocks = getMaterialStockVO(hvo.getPk_org(), dimvo);

		if (stocks == null || stocks.length == 0) {
			throw new BusinessException("物料的库存信息不存在");
		}

		if (StringUtil.isEmpty(stocks[0].getDef2())) {
			throw new BusinessException("包装规格不能为空");
		}
		DefdocVO defvo = getDefdocVO(stocks[0].getDef2());

		if (defvo == null) {
			throw new BusinessException("包装规格不存在");
		}

		StoreStateVO statevo = getStateVO(dimvo.getCstateid());
		if (statevo == null) {
			throw new BusinessException("库存状态不存在");
		}
		checkState(statevo);

		hvo.setVdef9(stocks[0].getDef2());// 包装规格
		hvo.setVdef10(dimvo.getPk_batchcode());// 批次号主键
		hvo.setCadjustrowno(null);
		BigDecimal[] bigs = getNnpiece(hvo, defvo);

		int size = bigs[0].intValue();
		List<StateAdjustVO> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			StateAdjustVO sbvo = getStateAdjustVO(hvo, bigs[2]);
			list.add(sbvo);
		}

		if (bigs[1].compareTo(BigDecimal.ZERO) != 0) {
			StateAdjustVO bvo1 = getStateAdjustVO(hvo, bigs[1]);
			list.add(bvo1);
		}

		return list.toArray(new StateAdjustVO[list.size()]);
	}

	private StateAdjustVO getStateAdjustVO(StateAdjustVO hvo, BigDecimal npiece)
			throws Exception {
		StateAdjustVO sbvo = (StateAdjustVO) ObjectUtils.serializableClone(hvo);
		sbvo.setNassistnum(new UFDouble(npiece));
		return sbvo;
	}

	// 商 和 余数
	private BigDecimal[] getNnpiece(StateAdjustVO hvo, DefdocVO defvo)
			throws BusinessException {
		UFDouble ufdouble = hvo.getNassistnum();
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

	// 库存状态
	private StoreStateVO getStateVO(String value) throws UifException {
		StoreStateVO bvo = (StoreStateVO) HYPubBO_Client.queryByPrimaryKey(
				StoreStateVO.class, value);
		return bvo;
	}

	// 包装规格
	private MaterialStockVO[] getMaterialStockVO(String pk_org,
			OnhandDimVO dimvo) throws BusinessException {

		MaterialStockVO[] bvo = (MaterialStockVO[]) HYPubBO_Client
				.queryByCondition(MaterialStockVO.class, " pk_org = '" + pk_org
						+ "' and pk_material ='" + dimvo.getCmaterialoid()
						+ "' and nvl(dr,0)=0");
		return bvo;
	}

	private BigDecimal getNweight(DefdocVO defvo) throws BusinessException {
		String def3 = defvo.getDef2();
		if (StringUtil.isEmpty(def3))
			throw new BusinessException("包装规格净重不能为空！");
		return new BigDecimal(def3);
	}

	private Map<String, OnhandDimVO> getOnhandDimVO(StateAdjustVO[] resultVOs)
			throws BusinessException {
		String[] pk_onhanddims = (String[]) VOEntityUtil.getVOsValues(
				resultVOs, "pk_onhanddim_adj", String.class);
		IOnhandQry onhandquery = NCLocator.getInstance().lookup(
				IOnhandQry.class);
		OnhandDimVO[] onhandDims = onhandquery.queryOnhandDim(pk_onhanddims);
		Map<String, OnhandDimVO> map = new HashMap();
		for (OnhandDimVO vo : onhandDims) {
			map.put(vo.getPk_onhanddim(), vo);
		}

		return map;
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
			return this.isOneVOEnable((StateAdjustVO) this.getModel()
					.getSelectedData());
		}
		// 表体报检
		return this.isOneVOEnable((StateAdjustVO) objs[0]);
	}

	private boolean isOneVOEnable(StateAdjustVO vo) {

		// if (BillStatus.CHECKPASS != vo.getParentVO().getApprovestatus()
		// .intValue()) {
		// return false;
		// }
		if (!StringUtil.isEmpty(vo.getPk_org())) {
			return false;
		}

		if (!"Y".equals(vo.getAttributeValue("vdef1"))) {
			return false;
		}
		return true;
	}

	protected boolean isJzQCEnable(StateAdjustVO selectedData) {
		boolean yf635 = false;

		if (selectedData != null) {
			try {
				yf635 = SysinitAccessor
						.getInstance()
						.getParaBoolean(
								(String) selectedData
										.getAttributeValue("pk_org"),
								"YF636").booleanValue();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return yf635;
	}

	private void setPrintTranstype(StoreStateVO statevo, String pk_org)
			throws BusinessException {

		String strWhere = " nvl(dr,0) = 0 and pk_org = '" + pk_org
				+ "' and  storestatus ='" + statevo.getPk_storestate() + "'";
		LabelStatusVO[] vos = (LabelStatusVO[]) HYPubBO_Client
				.queryByCondition(LabelStatusVO.class, strWhere);

		if (vos == null || vos.length == 0)
			throw new BusinessException("库存状态未设置对应的标签类型！");

		String labeltype = vos[0].getLabeltype();// 状态标签

		strWhere = " nvl(dr,0) = 0 and  paramvalue ='" + labeltype + "'";
		ParamRegVO[] vos1 = (ParamRegVO[]) HYPubBO_Client.queryByCondition(
				ParamRegVO.class, strWhere);

		if (vos1 == null || vos1.length == 0)
			throw new BusinessException("参数注册未设置！");

		strWhere = " nvl(dr,0) = 0 and  cfunid ='" + vos1[0].getParentid()
				+ "'";
		FuncRegisterVO vo = (FuncRegisterVO) HYPubBO_Client
				.queryByPrimaryKey(FuncRegisterVO.class, vos1[0].getParentid());

		if (vo == null)
			throw new BusinessException("功能节点注册未设置！");
		BilltypeVO type = (BilltypeVO) HYPubBO_Client.queryByPrimaryKey(
				BilltypeVO.class, labeltype);

		if (type == null) {
			throw new BusinessException("单据类型未设置！");
		}
		setFuncode(vo.getFuncode());
		setTranstype(type.getPk_billtypecode());
		
		// if ("hege".equals(statevo.getVcode())) {// 标签档案-合格标签
		//
		// } else if ("guoqi".equals(statevo.getVcode())) {// 标签档案-过期标签
		// setFuncode("H3010200405");
		// setTranstype("JZ01-Cxx-30");
		// } else if ("buhege".equals(statevo.getVcode())) {// 标签档案-不合格标签
		// setFuncode("H3010200408");
		// setTranstype("JZ01-Cxx-45");
		// } else if ("daijian".equals(statevo.getVcode())) {// 标签档案-预标签
		// setFuncode("H3010200402");
		// setTranstype("JZ01-Cxx-15");
		// } else {
		// throw new BusinessException("不支持的标签类型！");
		// }
	}

	public String getTranstype() {
		return transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

}