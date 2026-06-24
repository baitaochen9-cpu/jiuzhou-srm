package nc.ui.bd.material.pf.action;

import java.awt.event.ActionEvent;

import nc.itf.material.mdm.SendMdmImp;
import nc.ui.erm.costshare.common.ArrayClassConvertUtil;
import nc.ui.gl.rule.UIState;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.bd.material.MaterialVO;
/**
 * 20251104
 * 用于更新通过列表批量更新主数据系统
 * @author zhian.ye
 *
 */
public class UpdateMdmAction extends NCAction {
	protected AbstractAppModel model = null;
	private nc.ui.uif2.actions.RefreshAction refreshAction = null;

	MaterialVO[] material = null;
	/*
	 * (non-Javadoc)
	 * @see nc.ui.uif2.NCAction#doAction(java.awt.event.ActionEvent)
	 */
	
	public UpdateMdmAction() {
		// TODO Auto-generated constructor stub
		setBtnName("更新MDM信息");
		setCode("UPDATEMDM");
	}
	@SuppressWarnings("restriction")
	@Override
	public void doAction(ActionEvent e) throws Exception {
//		this.getModel().getUiState();
		Object[] selectedDatas = null;
		if(this.getModel() instanceof BillManageModel){
			 selectedDatas = ((BillManageModel)this.getModel()).getSelectedOperaDatas();
			
		}else {
			return;
		}
		if(null == selectedDatas || selectedDatas.length == 0){
			return;
		}
		MaterialVO[] convert = ArrayClassConvertUtil.convert(selectedDatas, MaterialVO.class);
		//调用接口到后台处理
		SendMdmImp smi = new SendMdmImp();
		smi.updaterMdmMaterial(convert);
		
	}

	public nc.ui.uif2.actions.RefreshAction getRefreshAction() {
		return refreshAction;
	}

	public void setRefreshAction(nc.ui.uif2.actions.RefreshAction refreshAction) {
		this.refreshAction = refreshAction;
	}


	 public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
//		this.model.addAppEventListener(this);
	}
	@SuppressWarnings("restriction")
	@Override
	protected boolean isActionEnable() {
		;
//		if (UIState.NOT_EDIT == this.getModel().getUiState()) {
			return true;
//		}
//		return false;
}
}