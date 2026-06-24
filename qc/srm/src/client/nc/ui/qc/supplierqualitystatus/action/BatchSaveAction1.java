/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.qc.supplierqualitystatus.action;
import java.awt.event.ActionEvent;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.api.srm.ISysDispatcher;

public class BatchSaveAction1 extends nc.ui.uif2.actions.batch.BatchSaveAction {
	@SuppressWarnings("restriction")
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Logger.debug("Entering " + super.getClass().toString()
				+ ".actionPerformed");
		boolean ischeck = checkElectronicSignature(this.getModel().getContext(), "保存",this.getModel());
		if (!ischeck)
			return;
		
		BatchOperateVO voAll = getModel().getCurrentSaveObject();
		Object[] addObjs = voAll.getAddObjs();
		Object[] updObjs = voAll.getUpdObjs();
		Object[] delObjs = voAll.getDelObjs();
		int i = voAll.getAddIndexs().length+voAll.getUpdIndexs().length;
		super.doAction(e);
		Object  o = getModel().getSelectedData();
		String billid = null;
		
		if(o != null){
			if(o instanceof  SupplierqualityHVO){
				SupplierqualityHVO vo1 = (SupplierqualityHVO)o;
				billid  = vo1.getPk_supplier();
			}
		}
		
		// 生产厂商质量状态推送SRM  vo2
		SupplierqualityHVO[] vo2 = new SupplierqualityHVO[i];
		SupplierqualityHVO[] vodel = new SupplierqualityHVO[voAll.getDelIndexs().length];
		int index = 0;
		int indexdel = 0;
		for(Object obj:addObjs){
			if(obj instanceof AggSupplierqualityHVO){
				AggSupplierqualityHVO head = (AggSupplierqualityHVO)obj;
				vo2[index++] = head.getParentVO();
			}
		}
		for(Object obj:updObjs){
			if(obj instanceof AggSupplierqualityHVO){
				AggSupplierqualityHVO head = (AggSupplierqualityHVO)obj;
				vo2[index++] = head.getParentVO();
			}
		}
		
		for(Object obj:delObjs){
			if(obj instanceof SupplierqualityHVO){
				SupplierqualityHVO head = (SupplierqualityHVO)obj;
				vodel[indexdel++] = head;
			}
		}
		
		// 如果增删改都没有操作，默认是将现有的推过去 
		List<Object> rows = getModel().getRows();
		if(vodel.length == 0 && vo2.length == 0){
			vo2 = new SupplierqualityHVO[rows.size()];
			for(Object obj:rows){
				if(obj instanceof SupplierqualityHVO){
					vo2[index++] = (SupplierqualityHVO)obj;
				}
			}
		}
		ISysDispatcher sys = NCLocator.getInstance().lookup(ISysDispatcher.class);
		// 删除
		for (SupplierqualityHVO vo : vodel) {
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
		// 新增和修改
		for (SupplierqualityHVO vo : vo2) {
			if(vo != null){
				try {
					// 生产厂商质量状态推srm
					sys.dispatch(vo, "srm_qc_quanlity", null);
				} catch (BusinessException e1) {
					ExceptionUtils.wrappException(e1);
				}
			}
		}		updatebillId("保存",billid);
		
	}

}
