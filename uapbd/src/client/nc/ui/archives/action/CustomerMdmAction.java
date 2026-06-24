package nc.ui.archives.action;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import nc.bs.ncc.mdm.common.Operations;
import nc.bs.ncc.mdm.common.SynService;
import nc.bs.ncc.mdm.masterdata.IMasterDataTranslateProcess;
import nc.bs.ncc.mdm.masterdata.IMsaterDataDisposeProcess;
import nc.bs.ncc.mdm.util.MasterDataDisposeProcessImpl;
import nc.bs.ncc.mdm.util.MasterDataTranslateProcessImpl;
import nc.bs.ncc.mdm.vo.MdmCustomerVO;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.pub.BusinessException;
import net.sf.json.JSONObject;
import nc.ui.bd.cust.baseinfo.model.CustBaseInfoModel;
/**
 *uapbd 
 */
public class CustomerMdmAction extends NCAction {

	private static final long serialVersionUID = 1044146992274172126L;
	private CustBaseInfoModel model;
	IMasterDataTranslateProcess masterDataTranslateProcess = MasterDataTranslateProcessImpl.getInstance();
	IMsaterDataDisposeProcess masterDataDisposeProcess = MasterDataDisposeProcessImpl.getInstance();
	
	public CustomerMdmAction() {
		this.setBtnName("同步主数据");
		this.setCode("customerMdmAction");
	}
	
	@Override
	public void doAction(ActionEvent event) throws Exception {
		Object[] objs = model.getSelectedOperaDatas();	 
		if(objs!=null&&objs.length>0){
			try {
				CustomerVO[] custVOs = new CustomerVO[objs.length];
				for(int i=0;i<objs.length;i++){
					custVOs[i]=(CustomerVO) objs[i];
				}			
				JSONObject obo = masterDataTranslateProcess.translate(Arrays.asList(custVOs),MdmCustomerVO.class, Operations.UPDATE);
				masterDataDisposeProcess.customerDispose(custVOs, obo,masterDataTranslateProcess, Operations.UPDATE);
				SynService.messageSynToMasterData(obo, Operations.UPDATE);
				ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant.getSaveSuccessInfo(), getModel().getContext());
			} catch (Exception e) {
				throw new BusinessException("同步主数据【客户】出错:" + e.getMessage(), e);
			}
		}	
	}

	public CustBaseInfoModel getModel() {
		return model;
	}

	public void setModel(CustBaseInfoModel model) {
		this.model = model;
	}
}
