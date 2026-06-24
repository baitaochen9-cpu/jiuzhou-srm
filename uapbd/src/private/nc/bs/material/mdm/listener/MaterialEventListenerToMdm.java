package nc.bs.material.mdm.listener;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ncc.mdm.util.VOCollectUtil;
import nc.itf.material.mdm.SendMdmItf;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class MaterialEventListenerToMdm implements IBusinessListener {
	SendMdmItf lookup ;

	@Override
	public void doAction(IBusinessEvent event) throws BusinessException {
		// TODO Auto-generated method stub
		MaterialVO[] materialVOs = VOCollectUtil.process((BDCommonEvent) event,MaterialVO.class);
		if(null == materialVOs || materialVOs.length == 0 ) return;
		for(MaterialVO mateiralvo : materialVOs){
			if(null == mateiralvo.getDef7() || "".equals(mateiralvo.getDef7())){
				continue ;
			}
			String pk_org = getStockOrg(mateiralvo.getPk_material());
			getSenMdmSevecs().insetMdmMaterialAssist(materialVOs,pk_org);
		}
		
	
		
	}
	
	private SendMdmItf getSenMdmSevecs(){
		if(null == lookup ){
			lookup = NCLocator.getInstance().lookup(nc.itf.material.mdm.SendMdmItf.class);
		}
		return lookup;
	}

	/**
	 * 获取分配库存组织
	 * @return 库存组织ID
	 * @throws DAOException 
	 */
	private String getStockOrg(String pk_material) throws DAOException {
		
		
			String Pk_org = getSenMdmSevecs().queryDocNameByID(pk_material, 
					" bd_materialstock ",
					"pk_material ", "pk_org");
	
		return Pk_org;

	}
}
