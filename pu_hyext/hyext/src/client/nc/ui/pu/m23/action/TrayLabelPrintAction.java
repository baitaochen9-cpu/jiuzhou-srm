package nc.ui.pu.m23.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.cmp.utils.BillcodeGenerater;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.pu.m23.print.dialog.TrayLabelPrintDlg;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
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
import nc.vo.trade.voutils.SafeCompute;

/**
 * 托盘标签打印按钮
 * 
 * @author zhw
 * 
 */
public class TrayLabelPrintAction extends SingletonLabelPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1811961090975795908L;

	public void initPrintInfo() {
		setPreview(false);
		super.setCode("trayLabel");
		super.setBtnName("托盘标签打印");
		setActioncode("trayLabel");
		setNodeKey("ot");
		setFuncode("H3010200401");
		setTranstype("JZ01-Cxx-10");
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
		ArriveItemVO totalvo = getArriveItemVO(bvos);
		// 校验是否已存在标签档案
		ILabelprintMaintain maintain = (ILabelprintMaintain) NCLocator
				.getInstance().lookup(ILabelprintMaintain.class.getName());
		 maintain.checkExistsAggLabelPrintHVO(vo.getHVO().getPk_org(),
				 totalvo.getVbdef8(), getTranstype());
		maintain.checkExistsPower(vo.getHVO().getPk_org(), getTranstype(),
				vo.getHVO(), getModel().getContext().getPk_loginUser());
		// 弹窗显示
//		totalvo.setVbdef2(totalvo.getVbdef8());

		ArriveItemVO hvo = setNum(totalvo);
//		hvo.setVbdef2(totalvo.getVbdef8());
		TrayLabelPrintDlg trayDlg = new TrayLabelPrintDlg(getModel()
				.getContext().getEntranceUI(), new ArriveItemVO[] { hvo });
		int idok = trayDlg.showModal();

		if (UIDialog.ID_OK == idok) {
			ArriveItemVO[] srcvos = trayDlg.getBvos();
			VORowNoUtils.setVOsRowNoByRule(srcvos, "crowno");
			List<AggLabelPrintHVO> list = changeLabelPrintHVO(hvo, srcvos);
			// 保存标签数据
			Object o = savechangeAggLabelPrintHVO(list);
			doPrint((AggLabelPrintHVO[]) o);
			ShowStatusBarMsgUtil.showStatusBarMsg("标签打印完成！", getModel()
					.getContext());
		}
	}

	private ArriveItemVO getArriveItemVO(ArriveItemVO[] vos) throws BusinessException {

		Map<String, ArriveItemVO> map = new HashMap<String, ArriveItemVO>();
		for (ArriveItemVO vo : vos) {

			String key = vo.getVbatchcode() + "&" + vo.getPk_material();
			if (map.containsKey(key)) {
				ArriveItemVO vo1 = map.get(key);
				vo.setNastnum(SafeCompute.add(vo.getNastnum(), vo1.getNastnum()));
				vo.setNnum(SafeCompute.add(vo.getNnum(), vo1.getNnum()));
				vo.setVbdef9((!StringUtil.isEmpty(vo1.getVbdef9()) ? vo1
						.getVbdef9() : "") + "," + vo.getPk_arriveorder());// //
																			// 表头id
				vo.setVbdef8((!StringUtil.isEmpty(vo1.getVbdef8()) ? vo1
						.getVbdef8() : "") + "," + vo.getPk_arriveorder_b());// //
																				// 表体id
			} else {
				vo.setVbdef9(vo.getPk_arriveorder());
				vo.setVbdef8(vo.getPk_arriveorder_b());
			}
			map.put(key, vo);
		}

		if (map.size() > 1) {
			throw new BusinessException("请选同一批次同一存货记录进行标签打印！");
		}
		return map.values().toArray(new ArriveItemVO[map.size()])[0];
	}

	private ArriveItemVO setNum(ArriveItemVO hvo) throws BusinessException {
		UFDouble ufdouble = hvo.getNastnum();
		if (ufdouble == null)
			throw new BusinessException("数量不能为空!");

//		hvo = (ArriveItemVO) HYPubBO_Client.queryByPrimaryKey(
//				ArriveItemVO.class, hvo.getPrimaryKey());
//		String vbdef16 = hvo.getVbdef16();
		UFDouble def16 = UFDouble.ZERO_DBL;

//		if (!(StringUtil.isEmpty(vbdef16) && "~".equals(vbdef16))) {
//			def16 = new UFDouble(vbdef16);
//		}
		ufdouble = SafeCompute.sub(ufdouble, def16);

		if (ufdouble.compareTo(UFDouble.ZERO_DBL) <= 0) {
			throw new BusinessException("没有可以标签打印的数量!");
		}
		hvo.setNastnum(ufdouble);
		return hvo;
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
			hvo.setSrcbillid(totalvo.getVbdef9()); // 到货单id
			hvo.setSrcbillrowid(totalvo.getVbdef8());// 到货单子表id
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
}