package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.Map;

import nc.itf.ct.reference.TypeServiceUtil;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CtTransBusitypes;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scmf.pub.keyvalue.IKeyValue;
import nc.vo.scmf.pub.keyvalue.VOKeyValue;
import nccloud.commons.lang.StringUtils;
import nccloud.dto.ct.saledaily.utils.SalePayTermUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同交易类型编辑后
 * @author wangshrc
 * @date 2019年2月14日 上午9:20:50
 * @version ncc1.0
 */
public class SaleDailyVtrantypeAfterRule implements IHeadAfterRule<AggCtSaleVO> {
	// 物料分类名称
	public static final String MARBASCLASSNAME = "pk_marbasclass.name";

	// 物料名称
	public static final String MATERIALNAME = "pk_material.name";

	// 物料规格
	public static final String MATERIALSPEC = "pk_material.materialspec";

	// 物料型号
	public static final String MATERIALTYPE = "pk_material.materialtype";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo, BillCardHeadEditEvent event, Map userobject) {
		CtSaleVO hvo = billvo.getParentVO();
		String ctrantypeid = hvo.getCtrantypeid();
		if (ValueUtil.isEmpty(ctrantypeid)) {
			hvo.setCtrantypeid((String) event.getOldValue());
			String code = TypeServiceUtil.getTrantypecodeByid((String) event.getOldValue());
			hvo.setVtrantypecode(code);
			userobject.put("errMsg", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
					"04020003-0043")/*
									 * @res "交易类型不能为空"
									 */);
			return billvo;
		}
		// 交易类型VO
		BusinessSetVO businessvo = CtTransBusitypes.getBusinessSetVO(ctrantypeid);
		boolean showPayTerm = false;
		if (businessvo == null || businessvo.getBshowpayterm() == null
				|| UFBoolean.TRUE.booleanValue() != businessvo.getBshowpayterm().booleanValue()) {
			showPayTerm = false;
		}else {
			// 页签表头初始化数据
			SalePayTermUtil.setValue(businessvo, billvo);
			showPayTerm = true;
		}
		// 设置页签是否显示
		userobject.put("showPayTerm", showPayTerm);
		Integer ninvctlstyle = businessvo.getNinvctlstyle();
		hvo.setNinvctlstyle(ninvctlstyle);
		// 根据物料控制方式，确定是否执行清空表体物料或物料分类相关信息
		this.clearMaterialOrMarClass(billvo, ninvctlstyle);
		return billvo;
	}

	private AggCtSaleVO clearMaterialOrMarClass(AggCtSaleVO billvo, Integer ninvctlstyle) {
		IKeyValue keyValue = new VOKeyValue<AggCtSaleVO>(billvo);
		for (int i = 0; i < keyValue.getBodyCount(); i++) {
			// 清空物料销售分类、基本分类
			String[] keys = getClearItem(ninvctlstyle);
			for (int j = 0; j < keys.length; j++) {
				keyValue.setBodyValue(i, keys[j], null);
			}
		}
		return billvo;
	}

	private String[] getClearItem(Integer ninvctlstyle) {
		if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value())) {
			String[] keys = { MARBASCLASSNAME, CtAbstractBVO.PK_MARBASCLASS };
			return keys;
		} else if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MARBASCLASS.value())) {
			String[] keys = { CtAbstractBVO.PK_SRCMATERIAL, CtAbstractBVO.PK_MATERIAL, CtAbstractBVO.NASTNUM,
					CtAbstractBVO.CASTUNITID, CtAbstractBVO.NNUM, CtAbstractBVO.NQTUNITNUM, CtAbstractBVO.CUNITID,
					CtAbstractBVO.CQTUNITID, MATERIALNAME, MATERIALSPEC, MATERIALTYPE };
			return keys;
		} else if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.WITHOUT.value())) {
			String[] keys = { CtAbstractBVO.PK_SRCMATERIAL, CtAbstractBVO.PK_MATERIAL, CtAbstractBVO.NASTNUM,
					CtAbstractBVO.CASTUNITID, CtAbstractBVO.NNUM, CtAbstractBVO.NQTUNITNUM, CtAbstractBVO.CUNITID,
					CtAbstractBVO.CQTUNITID, MATERIALNAME, MATERIALSPEC, MATERIALTYPE, MARBASCLASSNAME,
					CtAbstractBVO.PK_MARBASCLASS };
			return keys;
		}
		return null;
	}

}