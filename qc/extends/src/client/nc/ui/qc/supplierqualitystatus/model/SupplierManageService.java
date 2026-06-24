package nc.ui.qc.supplierqualitystatus.model;

import nc.bs.framework.common.NCLocator;
import nc.itf.qc.ISupplierqualitystatusMaintain;
import nc.ui.uif2.model.IBatchAppModelService;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.uif2.LoginContext;


/**
 * 参数管理的服务类
 * 
 * @author lkp
 *
 */
public class SupplierManageService implements IBatchAppModelService {
	
	private ISupplierqualitystatusMaintain service = null;

	private ISupplierqualitystatusMaintain getService()
	{
		if(service == null)
			service = NCLocator.getInstance().lookup(ISupplierqualitystatusMaintain.class);
		return service;
	}
 
	public BatchOperateVO batchSave(BatchOperateVO batchVO) throws Exception {
		
		return getService().batchSaveVO(batchVO);
	} 

	public Object[] queryByDataVisibilitySetting(LoginContext context) throws Exception {
		return null;
	}
 
}
