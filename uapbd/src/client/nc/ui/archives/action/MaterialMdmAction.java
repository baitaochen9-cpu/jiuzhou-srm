package nc.ui.archives.action;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import nc.bs.ncc.mdm.common.Operations;
import nc.bs.ncc.mdm.common.SynService;
import nc.bs.ncc.mdm.masterdata.IMasterDataTranslateProcess;
import nc.bs.ncc.mdm.masterdata.IMsaterDataDisposeProcess;
import nc.bs.ncc.mdm.util.MasterDataDisposeProcessImpl;
import nc.bs.ncc.mdm.util.MasterDataTranslateProcessImpl;
import nc.bs.ncc.mdm.vo.MdmMaterialVO;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;
import net.sf.json.JSONObject;
import nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel;

/** 
 *uapbd模块
 */
public class MaterialMdmAction extends NCAction{

	private static final long serialVersionUID = 1684142632048938718L;
	private MaterialBaseInfoModel model;
	IMasterDataTranslateProcess masterDataTranslateProcess = MasterDataTranslateProcessImpl.getInstance();
	IMsaterDataDisposeProcess masterDataDisposeProcess = MasterDataDisposeProcessImpl.getInstance();

	public MaterialMdmAction() {
		this.setBtnName("同步主数据");
		this.setCode("materialMdmAction");
	}
	
	@Override
	public void doAction(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub
		Object[] objs =  model.getSelectedOperaDatas();	 
		if(objs!=null&&objs.length>0){
			try {	
				MaterialVO[] materialVOs = new MaterialVO[objs.length];
				for(int i=0;i<objs.length;i++){
					materialVOs[i]=(MaterialVO)objs[i];
				}	
				JSONObject obo = masterDataTranslateProcess.translate(Arrays.asList(materialVOs),MdmMaterialVO.class, Operations.UPDATE);
				obo.put("masterData", masterDataDisposeProcess.materialDispose(obo));
				SynService.messageSynToMasterData(obo, Operations.UPDATE);	
				ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant.getSaveSuccessInfo(), getModel().getContext());
			} catch (Exception e) {
				throw new BusinessException("同步主数据【物料】出错:" + e.getMessage(), e);
			}
		}
		
	}

	public MaterialBaseInfoModel getModel() {
		return model;
	}

	public void setModel(MaterialBaseInfoModel model) {
		this.model = model;
	}

}
