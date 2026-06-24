package nc.ui.bd.material.pf.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.material.mdm.SendMdmItf;
import nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;

public class MaterilToGWAction extends NCAction{
	
	public MaterilToGWAction() {
		// TODO Auto-generated constructor stub
		setBtnName("GW");
		setCode("GW");
	}
	
	private MaterialBaseInfoModel model = null;  

	@Override
	public void doAction(ActionEvent e) throws Exception {

		SendMdmItf lookup = NCLocator.getInstance().lookup(SendMdmItf.class);
		
		

		Object[] selectedOperaDatas = this.model.getSelectedOperaDatas();
		if(selectedOperaDatas == null || selectedOperaDatas.length == 0){
			throw new BusinessException("请选中一条数据");
		}
		ArrayList<MaterialVO> list = new ArrayList<>();
		for (Object object : selectedOperaDatas) {
			list.add((MaterialVO)object);
		}
		
		MaterialVO[] array = list.toArray(new MaterialVO[list.size()]);
		lookup.materialSendGw(array);

		MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(), "推送关务", "推送成功");
	
		
	}

	public MaterialBaseInfoModel getModel() {
		return model;
	}

	public void setModel(MaterialBaseInfoModel model) {
		this.model = model;
	}
	@Override
	protected boolean isActionEnable() {
		// TODO Auto-generated method stub
		return  this.model.getUiState() == UIState.NOT_EDIT;
	}

}
