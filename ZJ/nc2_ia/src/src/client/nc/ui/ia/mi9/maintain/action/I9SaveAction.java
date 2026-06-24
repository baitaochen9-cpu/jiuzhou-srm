package nc.ui.ia.mi9.maintain.action;

import java.awt.event.ActionEvent;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.ui.ia.bill.base.maintain.action.BaseSaveAction;
import nc.ui.ia.mi9.maintain.model.I9ModelService;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.vo.ia.mi9.entity.I9BillVO;
import nc.vo.ia.mi9.entity.I9ItemVO;
import nc.vo.ia.util.CustomCarriedForwardOrder;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class I9SaveAction extends BaseSaveAction {

	private static final long serialVersionUID = -8097685560335172590L;

	private I9ModelService service;

	public void setService(I9ModelService service) {
		this.service = service;
	}

	@Override
	public I9BillVO update(Object object) {
		I9BillVO retBill = null;
		try {
			retBill = (I9BillVO) this.service.update(object);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return retBill;
	}

	/**
	 * 20210325 zhian.ye 重写：Raybow 循环料自定义结转顺序
	 */
	@SuppressWarnings("restriction")
	@Override
	public void doAction(ActionEvent e) {
		/*
		 */
		// ****yezhian 重新定义业务日期
		// 2021-03-16***************************************/
		I9BillVO value = (I9BillVO) getEditor().getValue();
		String pk_org = value.getParentVO().getPk_org();
		I9ItemVO[] childrenVO = value.getChildrenVO();
			CustomCarriedForwardOrder sevse = NCLocator.getInstance().lookup(
					nc.vo.ia.util.CustomCarriedForwardOrder.class);
			for (I9ItemVO body : childrenVO) {
				String cinventoryvid =  body.getCinventoryvid();/* 物料 */ // dbizdate 
				 //检查物料是否是循环料
				 if(sevse.isSelectMaterial(cinventoryvid , pk_org) == UFBoolean.TRUE){
				 String billtype = "I9";
				 UFDate bizdate = body.getDbizdate();
				
				 UFDate bizData = sevse.getBizData(pk_org, bizdate, billtype);
				 body.setDbizdate(bizData);
				 }
			}
			I9BillVO bill = new I9BillVO();
			bill.setParent(value.getParentVO());
			bill.setChildrenVO(childrenVO);
			getEditor().setValue(bill);
			// /*************************************/
		

		super.doAction(e);
	}
}
