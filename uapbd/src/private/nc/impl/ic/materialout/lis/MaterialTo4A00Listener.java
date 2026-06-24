package nc.impl.ic.materialout.lis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.general.businessevent.ICGeneralCommonEvent;
import nc.itf.fi.pub.SysInit;
import nc.itf.ic.m4d.IMaterialOutMaintain;
import nc.vo.bc.pub.util.SysInitGroupQuery;
import nc.vo.ic.general.util.AssetRelatedUtil;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutHeadVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.ic.material.query.InvInfoQuery;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nccloud.dto.ic.onhand.utils.InvInfoUIQuery;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

public class MaterialTo4A00Listener  implements IBusinessListener{

	@Override
	public void doAction(IBusinessEvent event) throws BusinessException {
		 if (event instanceof ICGeneralCommonEvent) {
			  ICGeneralCommonEvent icEvent = (ICGeneralCommonEvent)event;
			 MaterialOutVO[] outBillVos1 = (MaterialOutVO[])(MaterialOutVO[])icEvent.getOldObjs();
			 //增加组织参数，防止对其他组织产生影响*******************************************/
			 String destorg = (String) outBillVos1[0].getParent().getAttributeValue("pk_org");
			 UFBoolean paraBoolean = UFBoolean.FALSE;
			 paraBoolean = SysInit.getParaBoolean(destorg, "ICTOEWM001");
			 if(paraBoolean == null || paraBoolean == UFBoolean.FALSE){
				 return;
			 }
			 /*******************************************/
			 if (!(SysInitGroupQuery.isAIMEnabled())) {
				 return;
			 }
			 MaterialOutVO[] outBillVos = new MaterialOutVO[outBillVos1.length];
			 System.arraycopy(outBillVos1, 0, outBillVos, 0, outBillVos1.length);
			for (MaterialOutVO materialOutVO : outBillVos) {
				InvInfoQuery query = InvInfoUIQuery.getInstance().getInvInfoQuery();
				Map canGenEquipFlags = AssetRelatedUtil.getCanGenEquipFlagMap(Arrays.asList(materialOutVO.getBodys()), query, false);
				List<MaterialOutBodyVO> canGenEquipBodys = new ArrayList<>();
				for (MaterialOutBodyVO sel : materialOutVO.getBodys()) {
					if (((Boolean) canGenEquipFlags.get(sel.getCgeneralbid())).booleanValue()) {
						canGenEquipBodys.add(sel);
					}
				}
				if(canGenEquipBodys.size()==0){
					return;
				}
				materialOutVO.setChildrenVO(canGenEquipBodys.toArray(new MaterialOutBodyVO[canGenEquipBodys.size()]));
			}
			MaterialOutVO[] generateEquipCard = NCLocator.getInstance().lookup(IMaterialOutMaintain.class).generateEquipCard(outBillVos);
			Map<String,MaterialOutHeadVO> id_vo = new HashMap<>();
			Map<String,MaterialOutBodyVO> bid_bvo = new HashMap<>();
			for (MaterialOutVO materialOutVO : outBillVos1) {
				id_vo.put(materialOutVO.getPrimaryKey(), materialOutVO.getHead());
				for (MaterialOutBodyVO sel : materialOutVO.getBodys()) {
					bid_bvo.put(sel.getCgeneralbid(), sel);
				}
			}
			for (MaterialOutVO materialOutVO : generateEquipCard) {
				id_vo.get(materialOutVO.getPrimaryKey()).setTs(materialOutVO.getHead().getTs());
				for (MaterialOutBodyVO sel : materialOutVO.getBodys()) {
					bid_bvo.get(sel.getCgeneralbid()).setTs(sel.getTs());
				}
			}
			 
			 
		 }
		
	}

}
