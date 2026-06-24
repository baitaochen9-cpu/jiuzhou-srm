package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.payment.PaymentService;
import nc.vo.bd.payment.PaymentChVO;
import nc.vo.bd.payment.PaymentVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.pub.SuperVO;
import nc.vo.scmpub.util.ArrayUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

import nccloud.commons.lang.StringUtils;

/**
 * @description 付款协议 编辑后事件
 * @author xiahui
 * @date 创建时间：2019-2-14 上午10:52:27
 * @version ncc1.0
 **/
public class PayTermAfterRule implements IHeadAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		// 清空付款协议页签行，按新选择的付款协议进行增行处理
		util.deleteBodyValue(CtPaymentVO.class);
		String pk_payterm = (String) util.getHeadValue(CtAbstractVO.PK_PAYTERM);
		if (StringUtils.isEmpty(pk_payterm)) {
			return billvo;
		}

		// 查询表头付款协议对应表体
		SuperVO[] vos = PaymentService.queryPaymentByIds(new String[] { pk_payterm }, PaymentVO.class);
		if (ArrayUtil.isEmpty(vos)) {
			return billvo;
		}
		PaymentVO pvo = (PaymentVO) vos[0];
		PaymentChVO[] pchvos = pvo.getPaymentch();

		// 表头组织、集团信息
		String pk_group = util.getHeadTailStringValue(CtAbstractVO.PK_GROUP);
		String pk_org = util.getHeadTailStringValue(CtAbstractVO.PK_ORG);
		String pk_org_v = util.getHeadTailStringValue(CtAbstractVO.PK_ORG_V);

		String[][] fields = { { CtPaymentVO.SHOWORDER, PaymentChVO.SHOWORDER }, /**
		 * 
		 * 
		 * 
		 * 付款期
		 */
		{ CtPaymentVO.ACCRATE, PaymentChVO.ACCRATE }/** 付款比例 */
		, { CtPaymentVO.PREPAYMENT, PaymentChVO.PREPAYMENT }/** 预付款 */
		, { CtPaymentVO.PK_PAYPERIOD, PaymentChVO.PK_PAYPERIOD }/** 起效日期 */
		, { CtPaymentVO.EFFECTDATEADDDATE, PaymentChVO.EFFECTDATEADDDATE }/**
		 * 
		 * 
		 * 起效日期延迟天数
		 */
		, { CtPaymentVO.PAYMENTDAY, PaymentChVO.PAYMENTDAY }/** 账期天数 */
		, { CtPaymentVO.CHECKDATA, PaymentChVO.CHECKDATA }/** 固定结账日 */
		, { CtPaymentVO.EFFECTMONTH, PaymentChVO.EFFECTMONTH }/** 生效月 */
		, { CtPaymentVO.EFFECTADDMONTH, PaymentChVO.EFFECTADDMONTH }/** 附加月 */
		, { CtPaymentVO.PK_BALATYPE, PaymentChVO.PK_BALATYPE }/** 结算方式 */
		, { CtPaymentVO.ISDEPOSIT, PaymentChVO.ISDEPOSIT }/** 质保金 */
		, { CtPaymentVO.PK_RATE, PaymentChVO.PK_RATE }, { CtPaymentVO.OUTACCOUNTDATE, PaymentChVO.ACCOUNTDAY }

		};

		CtPaymentVO[] ctPaymentVOs = new CtPaymentVO[pchvos.length];
		util.setBody(CtPaymentVO.class, ctPaymentVOs);
		for (int row = 0; row < pchvos.length; row++) {
			ctPaymentVOs[row] = new CtPaymentVO();

			for (String[] field : fields) {
				util.setBodyValue(row, field[0], pchvos[row].getAttributeValue(field[1]), CtPaymentVO.class);
			}
			util.setBodyValue(row, CtAbstractVO.PK_GROUP, pk_group, CtPaymentVO.class);
			util.setBodyValue(row, CtAbstractVO.PK_ORG, pk_org, CtPaymentVO.class);
			util.setBodyValue(row, CtAbstractVO.PK_ORG_V, pk_org_v, CtPaymentVO.class);
		}
		return billvo;
	}

}
