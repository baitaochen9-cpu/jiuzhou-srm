package nc.ui.qc.supplierqualitystatus.action;

import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.qc.supplierqualitystatus.model.BodyManageTableModel;
import nc.ui.uif2.actions.batch.BatchAddLineWithDefValueAction;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.uif2.LoginContext;

/**
 * 按钮增行操作
 * 
 * @author lkp
 * 
 */
public class BodyAddLineAction extends BatchAddLineWithDefValueAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9074669956715385595L;

	@Override
	protected boolean isActionEnable() {

		Object obj = ((BodyManageTableModel) getModel()).getFuncModel()
				.getSelectedData();
		if (obj == null)
			return false;
		SupStockVO vo = (SupStockVO) obj;
		if (obj == null || StringUtil.isEmptyWithTrim(vo.getPk_supstock()))
			return false;
		return super.isActionEnable();
	}

	/**
	 * 设置所增行的默认值
	 */
	protected void setDefaultValue(Object obj) {

		Object userObj = ((BodyManageTableModel) getModel()).getFuncModel()
				.getSelectedData();
		SupStockVO pvo = (SupStockVO) userObj;
		LoginContext context = ((BodyManageTableModel) getModel()).getContext();
		String pk_group = context.getPk_group();
		String pk_org = context.getPk_org();
		SupplierqualityHVO vo = (SupplierqualityHVO) obj;
		// 设置主组织默认值
		vo.setAttributeValue("pk_group", pk_group);
		vo.setAttributeValue("pk_org", pk_org);
		vo.setAttributeValue("pkorg", pk_org);
		// 设置单据状态、单据业务日期默认值
		vo.setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
		vo.setAttributeValue("billdate", new UFDate(System.currentTimeMillis()));
		vo.setAttributeValue("billtype", "C055");
		vo.setAttributeValue("transtype", "C055");
		vo.setAttributeValue("transtypepk", PfServiceScmUtil
				.getTrantypeidByCode(new String[] { "C055" }).get("C055"));
		vo.setAttributeValue("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		vo.setAttributeValue("creator", context.getPk_loginUser());
		vo.setAttributeValue("maketime",
				new UFDateTime(System.currentTimeMillis()));
		vo.setAttributeValue("billmaker", context.getPk_loginUser());

		vo.setAttributeValue("pk_supplier", pvo.getPk_supplier());

	}

}
