package nc.ui.bd.material.pf.action;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.AbstractFunclet;
import nc.itf.material.mdm.SendMdmItf;
import nc.ui.bd.material.pf.action.dlg.LinkMdmDialog;
import nc.ui.bd.material.pf.model.MaterialPfModelDataManager;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.pf.AggMaterialPfVO;
import nc.vo.bd.material.pf.MaterialPfVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;

import com.ufsoft.report.dialog.MessageDialog;

@SuppressWarnings("restriction")
public class LinkMdmAction extends NCAction implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected AbstractAppModel model = null;
	private nc.ui.uif2.actions.RefreshAction refreshAction = null;
	MaterialVO material = null;
	

	AggMaterialPfVO materialPfVO = null; 
	



	public LinkMdmAction() {
		setBtnName("MDM");
		setCode("MDMLK");
	}	
	
	@Override
	public void doAction(ActionEvent actionEvent) throws Exception {
		
		
		if(null == getMaterial()){

			nc.ui.pub.beans.MessageDialog.showHintDlg(null, "提示", "请选择一行数据！");
		}
		
		AbstractFunclet funclet = (AbstractFunclet) getModel().getContext()
				.getEntranceUI();
		
		LinkMdmDialog dlg = new LinkMdmDialog(funclet, getMaterial(), getPf_parentVO(),getPk_org());
		
		dlg.addWindowListener(this);
		dlg.show();
		
	
	}

	String PK_ORG = "所属组织";
	String STOCKORG ="分配组织";
	SendMdmItf lookup ;
	/**
	 * 通过所选物料
	 * @return 所属组织
	 * @throws BusinessException 
	 */
	private String getPk_org() throws BusinessException {
		String pk_group = getMaterial().getPk_group();//物料集团信息
		String pk_org2 = getMaterial().getPk_org(); //物料所属组织
		String stockOrg2 = this.getStockOrg(getMaterial().getPk_material()); //分配库存组织
		if (pk_group == pk_org2 ){ 
			// 物料所属公司 == 集团情况，//说明物料在集团档案下，检查分配的库存组织如果不是集团
			if(null == stockOrg2 || "~".equals(stockOrg2)){
				return pk_org2;	//该物料未被分配或被多次分配，集团档案下无法直接匹配所属公司，将被直接挂下集团下物料
			}else{
				pk_org2 = stockOrg2;
			}
		
			
		}
		
		String paraString = nc.itf.fi.pub.SysInit.getParaString(pk_org2, "MDMBD01"); //到这里的，说明物料一定不在集团下，而是在业务单元，就必须检查参数
		
		if( PK_ORG.equals(paraString) ){  //如果
			return getMaterial().getPk_org();
		}else if( null == paraString  
				||  STOCKORG.equals(paraString)  && !"10140MPF".equals(getModel().getContext().getFuncInfo().getFuncode())){ //分配组织无法通过申请单进行处理，这种情况直接用物料下的组织
			
			return this.getStockOrg(getMaterial().getPk_material());
		} else {
			return getMaterial().getPk_org();
		}
//		return null;
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
	
	private SendMdmItf getSenMdmSevecs(){
		if(null == lookup ){
			lookup = NCLocator.getInstance().lookup(nc.itf.material.mdm.SendMdmItf.class);
		}
		return lookup;
	}
	
	private boolean isPfFund(){
		if( "10140MPF".equals(getModel().getContext().getFuncInfo().getFuncode())){
			return true;
		}	
		return false;
	}
	
	
	@Override
	protected boolean isActionEnable() {
//		super.isActionEnable();
		
		if (getModel().getSelectedData() == null)
		return false;
		if(this.getModel().getSelectedData() instanceof AggMaterialPfVO){
			materialPfVO = (AggMaterialPfVO) this.getModel().getSelectedData();
			if(null == getMaterialPfVO() ){
				return false;
			}
			MaterialPfVO parentVO = (MaterialPfVO)materialPfVO.getParentVO();
			material =( MaterialVO) parentVO.getMaterial();
			
		}else{
			material = (MaterialVO) this.getModel().getSelectedData();
		}
//		
//		if(null == getMaterial() ){
//			return false;
//		}
//		String def7 = material.getDef7();
//		if(null != def7 ){
//			return false;
//		}
		return true;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
//			getRefreshAction().getDataManager().refresh();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
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
		this.model.addAppEventListener(this);
	}
	
	public MaterialVO getMaterial() {
		if(null == material){
			if("10140MPF".equals(getModel().getContext().getFuncInfo().getFuncode())){
				material= (MaterialVO) ((MaterialPfVO)getMaterialPfVO().getParentVO()).getMaterial();
			}else{
				material = (MaterialVO) this.getModel().getSelectedData();
			}
			
		}
		return material ;
	}

	public void setMaterial(MaterialVO material) {
		this.material = material;
	}
	
	/**
	 * 物料申请单表头信息获取，此方法主要为了防止空指针
	 * @return
	 */
	private MaterialPfVO getPf_parentVO() {
		
		if(null != this.getMaterialPfVO()){
			return (MaterialPfVO)getMaterialPfVO().getParentVO();
		}
		
		return null;

	}

	public AggMaterialPfVO getMaterialPfVO() {
		if ( "10140MPF".equals(getModel().getContext().getFuncInfo().getFuncode()) && null == materialPfVO){//10140MPF
			materialPfVO = (AggMaterialPfVO) this.getModel().getSelectedData();
		}
		return materialPfVO;
	}

	public void setMaterialPfVO(AggMaterialPfVO materialPfVO) {
		this.materialPfVO = materialPfVO;
	}



}