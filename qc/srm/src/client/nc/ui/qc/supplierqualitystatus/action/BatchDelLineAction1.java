/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.qc.supplierqualitystatus.action;
import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.ui.uif2.actions.batch.BatchDelLineAction;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nccloud.api.srm.ISysDispatcher;

public class BatchDelLineAction1 extends BatchDelLineAction {
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Logger.debug("Entering " + super.getClass().toString()
				+ ".actionPerformed");
		boolean ischeck = checkElectronicSignature(this.getModel().getContext(), "删除",this.getModel());
		
		Object  o = getModel().getSelectedData();
		SupplierqualityHVO[] vo2 = new SupplierqualityHVO[1];
		if(o != null){
			if(o instanceof  SupplierqualityHVO){
				SupplierqualityHVO vo1 = (SupplierqualityHVO)o;
				vo2[0] = vo1;
			}
		}
		if (!ischeck)
			return;
		super.doAction(e);
		// 生产厂商质量状态推送SRM  vo2
		ISysDispatcher sys = NCLocator.getInstance().lookup(ISysDispatcher.class);
		for (SupplierqualityHVO vo : vo2) {
			String pk_supplierquality2 = vo.getPk_supplierquality();
			if(pk_supplierquality2 != null){
				try {
					// 生产厂商质量状态推srm
					sys.dispatch(vo, "srm_qc_quanlitydel", null);
				} catch (BusinessException e1) {
					ExceptionUtils.wrappException(e1);
				}
			}
		}
	}
	
}
