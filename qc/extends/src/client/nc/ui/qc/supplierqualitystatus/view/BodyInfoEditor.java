package nc.ui.qc.supplierqualitystatus.view;

import javax.swing.JComponent;

import org.apache.commons.lang3.StringUtils;

import nc.ui.bd.ref.model.SupplierGradeSysRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.qc.supplierqualitystatus.billref.SupplierGradeRefModel;
import nc.ui.so.pub.keyvalue.CardKeyValue;
import nc.ui.uap.funcreg.view.FuncBatchEditor;
import nc.vo.so.pub.keyvalue.IKeyValue;

/**
 * 按钮信息编辑器
 * 
 * @author lkp
 *
 */
public class BodyInfoEditor extends FuncBatchEditor{


	private static final long serialVersionUID = -1001640337913459434L;
	
//	private Map<String, Integer> indexMap = null; 
	
	@Override 
	protected void onEdit() {

		super.onEdit();
		
//		UFBoolean parentIsButtonPower = getFuncIsButtonPower();
//		int rowcount = getBillCardPanel().getBillTable().getRowCount();
//		for(int i = 0; i < rowcount; i++)
//		{
//			//如果节点或页签启用了按钮权限，则按钮的“是否启用按钮权限”项可编辑
//			if(parentIsButtonPower.booleanValue())
//			{
//				getBillCardPanel().getBillModel().setCellEditable(i, "isbuttonpower", true);
//			}else
//			{ //如果父节点没有启用按钮全县，则所有的按钮都不启用按钮权限，即都默认有权限。
//				getBillCardPanel().getBillModel().setCellEditable(i, "isbuttonpower", false);
//			}
//		}
		
	}
	
	@Override
	protected void afterLineInsert(int index) {

//		UFBoolean parentIsButtonPower = getFuncIsButtonPower();
//		if(!parentIsButtonPower.booleanValue())
//		{
//			//如果父对象没有启用按钮全县控制，则新增的按钮设置为不启用。
//			getBillCardPanel().getBillModel().setCellEditable(index, "isbuttonpower", false);
//		}else
//		{ 
//			//如果父对象启用了按钮权限，则新增的按钮设置为启用
//			getBillCardPanel().getBillModel().setCellEditable(index, "isbuttonpower", true);
//		}
	}
	
//	private UFBoolean getFuncIsButtonPower()
//	{
//		FuncRegManageAppModel funcModel = ((BtnManageTableModel)getModel()).getFuncModel();
//		Object userObj = funcModel.getSelectedData();
//		if(userObj == null)
//			return new UFBoolean(false);
//		UFBoolean parentIsButtonPower = new UFBoolean(false);
//		if(userObj instanceof FuncRegisterVO)
//		{
//			parentIsButtonPower = ((FuncRegisterVO)userObj).getIsbuttonpower();
//		}else if(userObj instanceof PageVO)
//		{
//			parentIsButtonPower = ((PageVO)userObj).getIsbuttonpower();
//		}
//		
//		return parentIsButtonPower;
//	} 
	
//	
//	protected int getIndexByColumName(String key)
//	{
//		if(indexMap == null)
//		{
//			indexMap = new HashMap<String, Integer>();
//			BillItem[] items = getBillCardPanel().getBillModel().getBodyItems();
//			if(items != null && items.length > 0)
//				for(int i = 0;i < items.length; i++)
//					indexMap.put(items[i].getKey(), i);
//		}
//		return indexMap.get(key);
//	}

	public boolean beforeEdit(BillEditEvent e) {
		
		BillCardPanel panel = getBillCardPanel();
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		int row = e.getRow();
		if("pk_suppliergrade".equals(key)){//生产商等级体系
			String def1 = keyValue.getBodyStringValue(row,"pk_org");//组织
			UIRefPane refpanel = (UIRefPane) panel.getBillModel().getItemByKey(key).getComponent();
			SupplierGradeSysRefModel gatherref = (SupplierGradeSysRefModel)refpanel.getRefModel();
			String sqlwhere = "";
			if(def1!=null){
				gatherref.setAddEnvWherePart(false);
				sqlwhere = sqlwhere + " and pk_org='" + def1 + "' ";
			}
			gatherref.addWherePart(sqlwhere);
		}else if("pk_grade_info".equals(key)){//物料等级
			String def1 = keyValue.getBodyStringValue(row,"pk_suppliergrade");//生产商等级体系
			UIRefPane refpanel = (UIRefPane) panel.getBillModel().getItemByKey(key).getComponent();
			SupplierGradeRefModel gatherref = (SupplierGradeRefModel)refpanel.getRefModel();
			String sqlwhere = "";
			if(def1!=null){
				sqlwhere = sqlwhere + " and pk_suppliergrade='" + def1 + "' ";
			}else{
				throw new IllegalArgumentException("生产商等级体系不能为空");
			}
			gatherref.addWherePart(sqlwhere);
		}else if("quality_ref".equals(key)){
			String org = keyValue.getBodyStringValue(row,"pk_org");//组织
			UIRefPane refpanel = (UIRefPane) panel.getBillModel().getItemByKey(key).getComponent();
			refpanel.setPk_org(org);
			refpanel.setMultiSelectedEnabled(true);
			panel.getBillModel().setRowState(row, 2);
		}
		return true;
	}
	
	protected void doAfterEdit(BillEditEvent e) {

		BillCardPanel panel = getBillCardPanel();
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		int row = e.getRow();
		int rowState = panel.getBillModel().getRowState(row);
		if("pk_suppliergrade".equals(key)){//生产商等级体系
			panel.getBillModel().setValueAt(null, row, "pk_grade_info");
		}else if("quality_ref".equals(key)){
			UIRefPane refpanel = (UIRefPane) panel.getBillModel().getItemByKey(key).getComponent();
			String[] refNames = refpanel.getRefNames();
			panel.getBillModel().setRowState(row, 2);
			panel.getBillModel().setValueAt(StringUtils.join(refNames,"&"), row, "def1");
		}
	}
}
