package nc.ui.archives.action;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import nc.bs.framework.common.NCLocator;
import nc.bs.ncc.mdm.common.Operations;
import nc.bs.ncc.mdm.common.SynService;
import nc.bs.ncc.mdm.masterdata.IMasterDataTranslateProcess;
import nc.bs.ncc.mdm.util.MasterDataTranslateProcessImpl;
import nc.bs.ncc.mdm.vo.MdmProductVO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 *riamm 
 */
public class ProducterMdmAction extends NCAction{

	private static final long serialVersionUID = -1098367402646395104L;
	private BatchBillTableModel model;
	IMasterDataTranslateProcess masterDataTranslateProcess = MasterDataTranslateProcessImpl.getInstance();
	
	public ProducterMdmAction() {
		this.setBtnName("同步主数据");
		this.setCode("producterMdmAction");
	}
	
	@Override
	public void doAction(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub
		Object[] objs =  model.getSelectedOperaDatas();	 
		
		if(objs!=null&&objs.length>0){
			try{
				DefdocVO[] producterVOs = new DefdocVO[objs.length];
				for(int i=0;i<objs.length;i++){
					producterVOs[i]=(DefdocVO)objs[i];
				}	
				JSONObject obo = masterDataTranslateProcess.translate(Arrays.asList(producterVOs), MdmProductVO.class,Operations.UPDATE);
				JSONObject result = SynService.messageSynToMasterData(obo,Operations.UPDATE);
				JSONArray resultArray = result.getJSONArray("data");

				for (Object i : resultArray) {
					JSONObject obj = JSONObject.fromObject(i);				
					String sql = "select * from bd_defdoc where pk_defdoc = '"+obj.getString("id")+"'";
					DefdocVO product = (DefdocVO) NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new BeanProcessor(DefdocVO.class));
					product.setDef4(obj.getString("mdm_code"));
					NCLocator.getInstance().lookup(IVOPersistence.class).updateVO(product);
				}
				ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant.getSaveSuccessInfo(), getModel().getContext());
			}catch(Exception e){
				throw new BusinessException("同步主数据客商档案【生产商】出错:" + e.getMessage());
			}
		}
		
	}

	public BatchBillTableModel getModel() {
		return model;
	}

	public void setModel(BatchBillTableModel batchBillTableModel) {
		this.model = batchBillTableModel;
	}
}
