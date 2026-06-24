package nc.ui.qc.supplierqualitystatus.action;

import java.awt.event.ActionEvent;

import nc.bs.logging.Logger;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pu.supqualistatus.SupplierqualityHistoryVO;

public class BatchSaveAction1 extends nc.ui.uif2.actions.batch.BatchSaveAction {
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Logger.debug("Entering " + super.getClass().toString()
				+ ".actionPerformed");
		boolean ischeck = checkElectronicSignature(this.getModel().getContext(), "保存",this.getModel());
		if (!ischeck)
			return;
		
		//获取电子签名，并暂存在表头
		String signVnote = getSignVnote();
		//2023-10-18 liyf 更新电子签名的签名信息到 历史信息def11
		Object bfObjc = getModel().getSelectedData();

		super.doAction(e);
		Object  o = getModel().getSelectedData();
		String billid = null;
		String pk_org = null;

		if(o != null){
			if(o instanceof  SupplierqualityHVO){
				SupplierqualityHVO vo1 = (SupplierqualityHVO)o;
				billid  = vo1.getPk_supplier();
				pk_org = vo1.getPk_org();
			}
		}
		//更新电子签名
		updatebillId("保存",billid);
	
		//实际业务场景
		if(bfObjc!=null && bfObjc  instanceof  SupplierqualityHVO ){
			SupplierqualityHVO vo1 = (SupplierqualityHVO)bfObjc;
			String pk_supplierquality = vo1.getPk_supplierquality();
			String pk_material = vo1.getPk_material();
			String pk_supplier = vo1.getPk_supplier();
			String strWhere =" isnull(dr,0) = 0  and def10=(select max(def10) from  qc_supplierhistory where  pk_org='"+pk_org+"' and pk_supplier='"+pk_supplier+"')"+" and pk_org='"+pk_org+"' and pk_supplier='"+pk_supplier+"'" ;
			SupplierqualityHistoryVO[] vos = (SupplierqualityHistoryVO[]) HYPubBO_Client.queryByCondition(SupplierqualityHistoryVO.class, strWhere);
			if(vos!=null && vos.length >0){
				for(SupplierqualityHistoryVO vo:vos){
					vo.setDef11(signVnote);
				}
				HYPubBO_Client.updateAry(vos);
			}

		}
	

	}

}
