package nc.ui.so.m4331.billui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.pubitf.para.SysInitQuery;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.so.m4331.billui.model.DeliveryManageModel;
import nc.ui.so.m4331.billui.view.DeliveryEditor;
import nc.ui.so.m4331.billui.view.DeliveryListView;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryVO;

import org.apache.commons.lang3.StringUtils;

/**
 * 发货单 COA报检
 */
public class ThLimsCoaAction extends NCAction {

	private static final long serialVersionUID = 1L;
	private DeliveryEditor editor;
	private DeliveryListView listView;
	private DeliveryManageModel model; 
	
	public ThLimsCoaAction() {
		super.setCode("LimsCoa");
		super.setBtnName("外部COA");
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		
		DeliveryVO seldeliveryVO=(DeliveryVO)this.getModel().getSelectedData();
		if(null==seldeliveryVO){
			return;
		}
		/*
		 * 检查参数开启参数
		 * */
		UFBoolean paraBoolean =SysInitQuery.getParaBoolean(seldeliveryVO.getParentVO().getPk_org(), "YFQCOA");
		if(!paraBoolean.booleanValue()){
			ShowStatusBarMsgUtil.showErrorMsg("提示", "该组织下未开启 YFQCOA 参数!", this.getModel().getContext());
			return;
		}
		/*
		 * 1. 选中行 按照行报检
		 * 2. 未选中行 则按整单报检
		 * 3. 如果同步的行已经 同步Lims  则进行提醒 确认是否重复发送
		 * */
		List<DeliveryBVO> selRow = this.getSelRow();
		if(null!=selRow  && selRow.size()>0){
			seldeliveryVO.setChildrenVO(selRow.toArray(new DeliveryBVO[selRow.size()]));
		}
		StringBuffer isCheckMes=new StringBuffer();
		for (DeliveryBVO bvo : seldeliveryVO.getChildrenVO()) {
			for (DeliveryBVO deliveryBVO : selRow) {
				if(bvo.getPrimaryKey().equals(deliveryBVO.getPrimaryKey())){
					if(StringUtils.isNotEmpty(bvo.getVbdef7())&& "Y".equals(bvo.getVbdef7())){
						isCheckMes=isCheckMes.append(bvo.getCrowno()+",");
					}
				}
			}
		}
		if(isCheckMes.length()>0){
			String question="以下行："+isCheckMes+" 已经发送COA,是否重复发送?";
			int showYesNoDlg=MessageDialog.showYesNoDlg(this.getEditor(), "提示", question);
			if(showYesNoDlg!=4){
				return;
			}
		}
		//调用lims coa
		this.Push2Lims(seldeliveryVO);
	}
	
	
	private void  Push2Lims(DeliveryVO selVo) throws BusinessException{
		String fun_type="TH_LIMS_COA";
		ISysDispatcherThLims outerService=(ISysDispatcherThLims) NCLocator.getInstance().lookup(ISysDispatcherThLims.class.getName());
		Map<String, Object> param = new HashMap<String,Object>();
		DeliveryVO returnvo = (DeliveryVO) outerService.dispatch(selVo, fun_type, param);
		this.model.directlyUpdate(new DeliveryVO[]{returnvo}); 
		ShowStatusBarMsgUtil.showStatusBarMsg("LIMS 外部COA同步成功",getModel().getContext());
	}
	
	public DeliveryEditor getEditor() {
		return editor;
	}
	public void setEditor(DeliveryEditor editor) {
		this.editor = editor;
	}
	public DeliveryManageModel getModel() {
		return model;
	}
	public void setModel(DeliveryManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}
	
	public DeliveryListView getListView() {
		return listView;
	}
	public void setListView(DeliveryListView listView) {
		this.listView = listView;
	}

	@Override
	protected boolean isActionEnable() {
		if(null==this.getModel().getSelectedOperaDatas()|| this.getModel().getSelectedOperaDatas().length>1){
			return false;
		}
		/*int[] selectedRows =null;
		if(this.getEditor().isShowing()){
			selectedRows= this.getEditor().getBillCardPanel().getBodyPanel().getTable().getSelectedRows();
		}else if(this.getListView().isShowing()){
			selectedRows=this.getListView().getBillListPanel().getBodyTable().getSelectedRows();
		}
		if(selectedRows==null || selectedRows.length==0 || selectedRows.length>1){
			return false;
		}
		if(null!=selectedRows && selectedRows.length>0){
			return true;
		}*/
		return true;
	}
	
	
	/**
	 * 获取选中的表体行数据
	 */
	private List<DeliveryBVO> getSelRow(){
		DeliveryVO deliveryVO=null;
		String[] sel_cdeliverybids=null;
		if(this.getEditor().isShowing()){
			int[] sel_row= this.getEditor().getBillCardPanel().getBodyPanel().getTable().getSelectedRows();
			sel_cdeliverybids=new String[sel_row.length];
			for(int i=0;i<sel_row.length;i++){
				sel_cdeliverybids[i]=this.getEditor().getBillCardPanel().getBodyValueAt(sel_row[i], "cdeliverybid").toString();
			}
			
			deliveryVO=(DeliveryVO)this.getEditor().getValue();
		}/*else if(this.getListView().isShowing()){
			sel_row=this.getListView().getBillListPanel().getBodyTable().getSelectedRow();
			deliveryVO=(DeliveryVO)this.getModel().getSelectedData();
			cdeliverybid=this.getListView().getBillListPanel().getBodyBillModel().getValueAt(sel_row, "cdeliverybid").toString();
		}*/
		List<DeliveryBVO> selBvos=new ArrayList<DeliveryBVO>();
		if(null!=sel_cdeliverybids && sel_cdeliverybids.length>0 && null!=deliveryVO){
			for(DeliveryBVO bvo:deliveryVO.getChildrenVO()){
				for (String cdeliverybid : sel_cdeliverybids) {
					if(cdeliverybid.equals(bvo.getPrimaryKey())){
						selBvos.add(bvo);
					}
				}
			}
		}else{
			for(DeliveryBVO bvo:deliveryVO.getChildrenVO()){
				selBvos.add(bvo);
			}
		}
		return selBvos;
	}
	
}
