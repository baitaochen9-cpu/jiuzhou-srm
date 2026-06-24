package nc.ui.pu.m21.action.payplan;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.pubapp.uif2app.model.BatchBillTableModel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BatchBillTable;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.axis.utils.StringUtils;

public class PayPlan2OAAction extends NCAction {

	private BatchBillTable editor;
	private BatchBillTableModel model;
	public PayPlan2OAAction() {
		super.setCode("oaAction");
		super.setBtnName("付款申请");
	}
	
	
	private static final String H_VIEW="v_nc2oa_payplan_h";
	private static final String B_VIEW="v_nc2oa_payplan_b";
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object selectedData = this.getModel().getSelectedData();
		if(null==selectedData){
			return;
		}
		PayPlanViewVO planViewVO=(PayPlanViewVO)selectedData;
		if(StringUtils.isEmpty(planViewVO.getPk_order())){
			return;
		}
		//4. 调用同步
		ISysDispatcher iIplatFormEntry = (ISysDispatcher)NCLocator.getInstance().lookup(ISysDispatcher.class.getName());
		 try {
			 Object dispatch = iIplatFormEntry.dispatch(planViewVO, "OA_PAYPLAN", null);
			 if(null!=dispatch){
				 ShowStatusBarMsgUtil.showStatusBarMsg("提交OA成功:"+dispatch,this.getModel().getContext());
			 }
		} catch (BusinessException exception) {
			// TODO Auto-generated catch block
			ExceptionUtils.wrappBusinessException(exception.getMessage());
		}
	}
	
	
	/**
	 * 转换为OA数据格式
	 * @param headMap
	 * @param listMaps
	 */
	private String  Convert2OA_Value(HashMap<String,Object> headMap,List<HashMap<String,Object>> listMaps){
		/* 主字段// 字段信息
		Set<Entry<String, Object>> H_keySet = headMap.entrySet();
		WorkflowRequestTableField[] mainField = new WorkflowRequestTableField[H_keySet.size()]; 
		int h_index=0;
		for (Entry<String, Object> hashMap : H_keySet) {
			
			mainField[h_index] = new WorkflowRequestTableField();
			mainField[h_index].setFieldName(hashMap.getKey());// 列名
			mainField[h_index].setFieldValue(null!=hashMap.getValue()?hashMap.getValue().toString():""); // VALUE
			mainField[h_index].setView(true);
			mainField[h_index].setEdit(true);
			
			h_index++;
		}
		// 主字段只有一行数据
		WorkflowRequestTableRecord[] mainRecord = new WorkflowRequestTableRecord[1];
		mainRecord[0] = new WorkflowRequestTableRecord();
		mainRecord[0].setWorkflowRequestTableFields(mainField);
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
		wmi.setRequestRecords(mainRecord);
		
		// 明细字段 // 明细表1
		WorkflowDetailTableInfo wdti[] = new WorkflowDetailTableInfo[1];
		// 数据行数
		WorkflowRequestTableRecord[] detailRecord = new WorkflowRequestTableRecord[listMaps.size()];
		//行索引
		int row_index=0;
		for (HashMap<String, Object> bodyMap : listMaps) {
			
			// 每行字段数量
			WorkflowRequestTableField[] detailField = new WorkflowRequestTableField[listMaps.get(0).entrySet().size()];
			int b_index=0;
			for(Entry<String, Object> hashMap :bodyMap.entrySet()){
				detailField[b_index]=new WorkflowRequestTableField();
				detailField[b_index].setFieldName(hashMap.getKey());// 列名
				detailField[b_index].setFieldValue(null!=hashMap.getValue()?hashMap.getValue().toString():""); // value
				detailField[b_index].setView(true);
				detailField[b_index].setEdit(true);
				b_index++;
			}
			detailRecord[row_index] = new WorkflowRequestTableRecord();
			detailRecord[row_index].setWorkflowRequestTableFields(detailField);
			row_index++;
		}
		wdti[0] = new WorkflowDetailTableInfo();
		wdti[0].setWorkflowRequestTableRecords(detailRecord);
		
		WorkflowBaseInfo wbi = new WorkflowBaseInfo();
		wbi.setWorkflowId("10541");// workflowid 5 代表内部留言

		WorkflowRequestInfo wri = new WorkflowRequestInfo();// 流程基本信息
		wri.setCreatorId("48");// 创建人id
		wri.setRequestLevel("2");// 0 正常，1重要，2紧急
		wri.setRequestName("lw-webservicexml " + System.currentTimeMillis());// 流程标题
		wri.setWorkflowBaseInfo(wbi);
		wri.setWorkflowMainTableInfo(wmi);// 添加主表字段数据
		wri.setWorkflowDetailTableInfos(wdti);// 添加明细表字段数据

		XmlUtil xmlUtil = XmlUtil.getInstance();
		String requestXml = xmlUtil.objToXml(wri);
		System.out.println(requestXml);
		Log.getInstance("同步OA").error(requestXml);
		return requestXml;*/
		return "";
	}
	
	
	/**
	 * 查询数据
	 * @param sql
	 * @param resultSetProcessor
	 * @return
	 */
	private Object QueryData(String v_name,String pk_order,ResultSetProcessor resultSetProcessor ){
		String query_sql="SELECT * FROM "+v_name+" WHERE pk_order='"+pk_order+"'";
		try {
			return this.getQueryBS().executeQuery(query_sql,resultSetProcessor);
		} catch (BusinessException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return null;
	}
	
	
	private IUAPQueryBS iuapQueryBS;
	private IUAPQueryBS getQueryBS(){
		if(null==iuapQueryBS){
			iuapQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		}
		return iuapQueryBS;
	}
	
	
	public BatchBillTable getEditor() {
		return editor;
	}
	public void setEditor(BatchBillTable editor) {
		this.editor = editor;
	}
	public BatchBillTableModel getModel() {
		return model;
	}
	public void setModel(BatchBillTableModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}
	
	protected boolean isActionEnable()
	{
		if (getModel().getUiState() != UIState.NOT_EDIT) {
			return false;
		}
		//如果未选中 或者 选中多行则不允许可用
		if (getModel().getRows().isEmpty() ||
				(null!=this.getModel().getSelectedOperaDatas() && this.getModel().getSelectedOperaDatas().length>1)) {
			return false;
		}
		PayPlanViewVO view =null;
		if(null!=this.getModel().getSelectedData()){
			view= (PayPlanViewVO) getModel().getSelectedData();
		}else{
			view= (PayPlanViewVO) getModel().getRow(0);
		}
		/*判断是否有累计付款金额 如果有 则不允许重复推送*/
		if(null!=view.getNaccumpaymny()){
			return false;
		}
		return (POEnumBillStatus.APPROVE.value().equals(view
				.getForderstatus()));
	}
}
