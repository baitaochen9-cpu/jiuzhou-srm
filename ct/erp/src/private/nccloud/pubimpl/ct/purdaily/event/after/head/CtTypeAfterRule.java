package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.Map;

import nc.itf.ct.reference.TypeServiceUtil;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CtTransBusitypes;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.dto.to.pub.constance.MsgFlag;

/**
 * @description 交易类型 编辑后事件
 * @author xiahui
 * @date 创建时间：2019-2-14 上午9:55:17
 * @version ncc1.0
 **/
public class CtTypeAfterRule implements IHeadAfterRule<AggCtPuVO> {

	public static final String MARBASCLASSNAME = "pk_marbasclass.name"; // 物料分类名称
	public static final String MATERIALNAME = "pk_material.name"; // 物料名称
	public static final String MATERIALSPEC = "pk_material.materialspec"; // 物料规格
	public static final String MATERIALTYPE = "pk_material.materialtype"; // 物料型号

	@SuppressWarnings("unchecked")
	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		String ctrantypeid = util.getHeadTailStringValue(CtAbstractVO.CTRANTYPEID); // 合同类型
		if (ValueUtil.isEmpty(ctrantypeid)) {
			util.setHeadValue(CtAbstractVO.CTRANTYPEID, event.getOldValue());
			String code = TypeServiceUtil.getTrantypecodeByid((String) event.getOldValue());
			util.setHeadValue(CtAbstractVO.VTRANTYPECODE, code);
			userobject
					.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0043")); // 交易类型不能为空
			return billvo;
		}

		BusinessSetVO businessvo = CtTransBusitypes.getBusinessSetVO(ctrantypeid); // 交易类型VO
		Integer ninvctlstyle = businessvo.getNinvctlstyle();
		util.setHeadValue(CtAbstractVO.NINVCTLSTYLE, ninvctlstyle);
		// 如果是采购合同则设置交易类型属性“总括订单”
		if (CTBillType.PurDaily.getCode().equals(util.getHeadValue(CtAbstractVO.CBILLTYPECODE))) {
			util.setHeadValue(CtPuVO.BBRACKETORDER, businessvo.getBbracketOrder());
		}

		if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value())) {
			this.checkMaterial(util);
		} else if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MARBASCLASS.value())) {
			this.checkMarbasclass(util);
		} else if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.WITHOUT.value())) {
			this.checkWithOut(util);
		}

		return billvo;
	}

	/**
	 * 如果选择了无。将物料分类和物料都设置为空
	 * 
	 * @param util
	 */
	private void checkWithOut(ExtBillUtil util) {
		String[] keys = { CtAbstractBVO.PK_MATERIAL, CtTypeAfterRule.MATERIALNAME, CtAbstractBVO.PK_MARBASCLASS,
				CtTypeAfterRule.MARBASCLASSNAME, CtAbstractBVO.NASTNUM, CtAbstractBVO.CASTUNITID, CtAbstractBVO.NNUM,
				CtAbstractBVO.NQTUNITNUM, CtAbstractBVO.CUNITID, CtAbstractBVO.CQTUNITID };
		util.clearBodyValue(keys);
		// 设置换算率
		this.setVchangeRate(util);

	}

	private void checkMarbasclass(ExtBillUtil util) {
		String[] keys = { CtAbstractBVO.PK_SRCMATERIAL, CtAbstractBVO.PK_MATERIAL, CtAbstractBVO.NASTNUM,
				CtAbstractBVO.CASTUNITID, CtAbstractBVO.NNUM, CtAbstractBVO.NQTUNITNUM, CtAbstractBVO.CUNITID,
				CtAbstractBVO.CQTUNITID, CtTypeAfterRule.MATERIALNAME, CtTypeAfterRule.MATERIALSPEC,
				CtTypeAfterRule.MATERIALTYPE };
		util.clearBodyValue(keys);
		// 设置换算率
		this.setVchangeRate(util);
	}

	/**
	 * 换算率默认设置为1
	 * 
	 * @param util
	 */
	private void setVchangeRate(ExtBillUtil util) {
		String nchangerate = "1.0000/1.0000";
		util.setBodyValue(CtAbstractBVO.VCHANGERATE, nchangerate);
		util.setBodyValue(CtAbstractBVO.VQTUNITRATE, nchangerate);
	}

	/**
	 * 如果选择了物料，则判断模板设置的物料编码是否可见，物料分类编码、名称是否必输。
	 * 
	 * @param util
	 */
	private void checkMaterial(ExtBillUtil util) {
		util.clearBodyValue(new String[] { CtAbstractBVO.PK_MARBASCLASS, CtTypeAfterRule.MARBASCLASSNAME });
	}

}
