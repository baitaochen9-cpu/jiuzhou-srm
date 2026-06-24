package nc.ui.archives.action;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import nc.bs.ncc.mdm.common.Operations;
import nc.bs.ncc.mdm.common.SynService;
import nc.bs.ncc.mdm.masterdata.IMasterDataTranslateProcess;
import nc.bs.ncc.mdm.masterdata.IMsaterDataDisposeProcess;
import nc.bs.ncc.mdm.util.MasterDataDisposeProcessImpl;
import nc.bs.ncc.mdm.util.MasterDataTranslateProcessImpl;
import nc.bs.ncc.mdm.vo.MdmSupplierVO;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.pub.BusinessException;
import net.sf.json.JSONObject;
import nc.ui.bd.supplier.baseinfo.model.SupplierBaseInfoModel;

/**
 * 供应商同步主数据按钮类,uapbd
 */
public class SupplierMdmAction extends NCAction {
	
	private static final long serialVersionUID = 1931580164111322318L;
	private SupplierBaseInfoModel model;
	IMasterDataTranslateProcess masterDataTranslateProcess = MasterDataTranslateProcessImpl.getInstance();
	IMsaterDataDisposeProcess masterDataDisposeProcess = MasterDataDisposeProcessImpl.getInstance();
	
	public SupplierMdmAction() {
		this.setBtnName("同步主数据");
		this.setCode("supplierMdmAction");
	}

	@Override
	public void doAction(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub		
		Object[] objs =  model.getSelectedOperaDatas();	 
		if(objs!=null&&objs.length>0){
			try {
				SupplierVO[] supVOs = new SupplierVO[objs.length];
				for(int i=0;i<objs.length;i++){
					supVOs[i] = (SupplierVO) objs[i];
				}
				JSONObject obo = masterDataTranslateProcess.translate(Arrays.asList(supVOs),MdmSupplierVO.class, Operations.UPDATE);
				masterDataDisposeProcess.supplierDispose(supVOs, obo,masterDataTranslateProcess, Operations.UPDATE);
				SynService.messageSynToMasterData(obo, Operations.UPDATE);		
				ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant.getSaveSuccessInfo(), getModel().getContext());
			} catch (Exception e) {
				throw new BusinessException("同步主数据【供应商】出错:" + e.getMessage(), e);
			}	
		}
	}

	public SupplierBaseInfoModel getModel() {
		return model;
	}

	public void setModel(SupplierBaseInfoModel model) {
		this.model = model;
	}
	
}
