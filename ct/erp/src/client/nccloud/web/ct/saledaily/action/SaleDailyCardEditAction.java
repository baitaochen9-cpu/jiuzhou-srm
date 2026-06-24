package nccloud.web.ct.saledaily.action;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.so.pub.enumeration.BillStatus;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.web.scmpub.pub.action.DataPermissionAction;
import nccloud.web.scmpub.pub.utils.SCMEditCheckUtils;

/**
 * @description 销售合同修改校验权限
 * @author wangshrc
 * @date 2019年1月17日 上午9:58:20
 * @version ncc1.0
 */
public class SaleDailyCardEditAction extends DataPermissionAction implements
		ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		ExtBillCardOperator operator = new ExtBillCardOperator("400600200_card");
		AggCtSaleVO vo = (AggCtSaleVO) operator.toBill(request);
		CtSaleVO hvo = vo.getParentVO();
		String pk = hvo.getPk_ct_sale();
		String vtrantypecode = hvo.getVtrantypecode();
		Integer fstatusflag = hvo.getFstatusflag();
		if (BillStatus.I_AUDITING == fstatusflag.intValue())
			SCMEditCheckUtils.checkEdit(pk, vtrantypecode);
		this.checkPermission(new AggCtSaleVO[] { vo });
		return null;
	}

	@Override
	public String getPermissioncode() {
		return CTBillType.SaleDaily.getCode();
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.EDIT.getActioncode();
	}

	@Override
	public String getBillCodeField() {
		return CtSaleVO.VBILLCODE;
	}

}
