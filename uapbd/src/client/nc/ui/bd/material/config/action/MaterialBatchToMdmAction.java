package nc.ui.bd.material.config.action;

import java.awt.event.ActionEvent;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.material.mdm.SendMdmItf;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pubapp.AppContext;

public class MaterialBatchToMdmAction extends NCAction{
	
	protected AbstractAppModel model = null;
	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}

	public MaterialBatchToMdmAction() {
		setBtnName("批量升级（期初）");
		setCode("batchToMdm");
	}

	@SuppressWarnings("restriction")
	@Override
	public void doAction(ActionEvent e) throws Exception {
		/*
		 * 1.检查当前选择数据的组织
		 * 2.检查组织下的全部数据，然后做分业处理，过滤为物料分类必须有集团统一分类对照的数据
		 */
		MessageDialog.showHintDlg(null, "提示",
				"本操作前请核对主数据系统当前数据状态，只能做为上线期间初始操作使用。仅用于批量同步主数据主表内容做为原始数据！");
		String pk_org = model.getContext().getPk_org();
		MaterialVO selectedData = (MaterialVO) getModel().getSelectedData();
		if(null == selectedData){
			MessageDialog.showErrorDlg(null, "错误", "请最少选择一条数据！");
			return;
		}
		SendMdmItf lookup = NCLocator.getInstance().lookup(SendMdmItf.class);
		
		String pk_material = selectedData.getPk_material();
		 pk_org = getStockOrg(pk_material);
		//创建接口，从后台处理
		 boolean isOk = lookup.batchToMdm(pk_org);
		
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
	
	SendMdmItf lookup ;
	private SendMdmItf getSenMdmSevecs(){
		if(null == lookup ){
			lookup = NCLocator.getInstance().lookup(nc.itf.material.mdm.SendMdmItf.class);
		}
		return lookup;
	}
	

}
