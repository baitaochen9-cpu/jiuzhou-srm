package nc.ui.archives.action;

import java.awt.event.ActionEvent;
import nc.bs.ncc.mdm.common.Operations;
import nc.bs.ncc.mdm.masterdata.IMasterDataTranslateProcess;
import nc.bs.ncc.mdm.masterdata.IMsaterDataDisposeProcess;
import nc.bs.ncc.mdm.util.MasterDataDisposeProcessImpl;
import nc.bs.ncc.mdm.util.MasterDataTranslateProcessImpl;
import nc.ui.bd.account.model.AccountAppModel;
import nc.ui.bd.ref.model.AccSystemRefModel;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.bd.account.AccountVO;
import nc.vo.pub.BusinessException;

/**
 *uapbd
 */

public class AccchartMdmAction extends NCAction{

	private static final long serialVersionUID = 4252771758544251412L;
	private AccountAppModel model;
	IMasterDataTranslateProcess masterDataTranslateProcess = MasterDataTranslateProcessImpl.getInstance();
	IMsaterDataDisposeProcess masterDataDisposeProcess = MasterDataDisposeProcessImpl.getInstance();

	public AccchartMdmAction() {
		this.setBtnName("同步主数据");
		this.setCode("accchartMdmAction");
	}

	@Override
	public void doAction(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub
		Object[] objs =  model.getSelectedDatas();
		
		if(objs!=null&&objs.length>0){
			try {
				AccountVO[] accoountVOs = new AccountVO[objs.length];
				for(int i=0;i<objs.length;i++){
					accoountVOs[i]=(AccountVO)objs[i];
				}
				masterDataDisposeProcess.accchartDispose(accoountVOs, Operations.UPDATE);
				ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant.getSaveSuccessInfo(), getModel().getContext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new BusinessException("同步主数据基础数据【会计科目】出错:" + e.getMessage());
			}		
		}
	
	}
	
	public AccountAppModel getModel() {
		return model;
	}


	public void setModel(AccountAppModel model) {
		this.model = model;
	}

}
