package nc.ui.bd.defdoc.action;

import java.awt.event.ActionEvent;

import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.framework.common.NCLocator;
import nc.bs.ncc.mdm.util.VOCollectUtil;
import nc.itf.bd.defdoc.IDefdocQryService;
import nc.itf.material.mdm.SendMdmItf;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.bd.pub.actions.ManageModeActionInterceptor;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.ui.uif2.actions.CompositeActionInterceptor;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.uap.rbac.query.util.QueryUtils;

public class DefdocTreecardBatchSynchronize extends NCAction{
	

	public DefdocTreecardBatchSynchronize()
	{

		setBtnName("批量同步");
		setCode("batchSyn");
		
		}

	
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub
		
		IDefdocQryService query = NCLocator.getInstance().lookup(IDefdocQryService.class);
		DefdocVO[] defdocs = query.queryDefdocVOsByDoclistPk("1001V1100000000W68CQ", "GLOBLE00000000000000", "group");
		
		
		SendMdmItf sever = NCLocator.getInstance().lookup(SendMdmItf.class);
		sever.insetMdmMaterialClass(defdocs);
	}
	
}
