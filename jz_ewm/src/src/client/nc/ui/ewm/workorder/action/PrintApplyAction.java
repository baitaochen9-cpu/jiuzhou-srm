package nc.ui.ewm.workorder.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import nc.bd.ewm.print.IProcessService;
import nc.bd.ewm.print.PrintAdd;
import nc.bs.framework.common.NCLocator;
import nc.ui.am.action.taskbatch.DataProcess;
import nc.ui.am.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.RefreshSingleAction;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.ewm.workorder.AggWorkOrderVO;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("restriction")
public class PrintApplyAction extends NCAction {
	private static final long serialVersionUID = 1L;
	IProcessService ip = NCLocator.getInstance().lookup(
			IProcessService.class);
	private BillForm editor;
	private AbstractAppModel model;
	public void doAction(ActionEvent e) throws Exception {
		AggWorkOrderVO billVOs = (AggWorkOrderVO) getModel().getSelectedData();
		String def = billVOs.getParentVO().getDef9();
		// 判断可重复打印参数
	    if (StringUtils.contains(def, "N")||StringUtils.isEmpty(def) ) {
			int res = JOptionPane.showConfirmDialog(null, "是否提交重打申请,是否继续?", "提示",
					JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.YES_OPTION) {
				// 检查是否已存在申请
				List rs = ip.printApplyQuery(billVOs.getPrimaryKey());
				if (rs.size() != 0) {
					showErrorMessage("提示", "已存在未审批打印申请单，单据号" + rs);
					//刷新
					RefreshSingleAction refreshaction = new RefreshSingleAction();
					refreshaction.setModel(this.getModel());
					ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
					refreshaction.doAction(e1);		
				} else {
					PrintAdd mantain = NCLocator.getInstance().lookup(
							PrintAdd.class);
					String billno = mantain.PushPrintAdd(billVOs);
					showErrorMessage("提示", "已生成打印申请单，单据号" + billno);
					//刷新
					RefreshSingleAction refreshaction = new RefreshSingleAction();
					refreshaction.setModel(this.getModel());
					ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
					refreshaction.doAction(e1);
					
				}
			}

		}else{
			showErrorMessage("提示", "当前可重复打印，不需要提交重打申请！");
		}
	  //刷新
		RefreshSingleAction refreshaction = new RefreshSingleAction();
		refreshaction.setModel(this.getModel());
		ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
		refreshaction.doAction(e1);	
	}
	
    protected boolean isActionEnable() {
		
		BillManageModel billManageModel = ((BillManageModel) getModel());
		if (billManageModel == null)
			return false;
		AggWorkOrderVO[] billVOs = (AggWorkOrderVO[]) DataProcess
				.getSelectedViewData(billManageModel);

		if (ArrayUtils.isEmpty(billVOs)) {
			return  false;
		}
		//控制重打按钮置灰
		try {
			return ip.isPrint(billVOs[0].getParentVO().getPk_org());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		};
		if(isCheckStatusType(billVOs)){
			return false;
		}
		return (getModel().getUiState() == UIState.NOT_EDIT)
				&& (getModel().getSelectedData() != null);
	}
	
	private boolean isCheckStatusType(AggWorkOrderVO[] billVOs) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);//批准
		list.add(2);//進行中
		list.add(3);//完成
		list.add(4);//已報告
		for (int i = 0; i < billVOs.length; i++) {
			Integer statusType2 = billVOs[i].getParentVO().getWo_statustype();
			if (!list.contains(statusType2)) {
				return true;
			}
		}
		return false;
	}


	public void showErrorMessage(String title, String err) {
		ShowStatusBarMsgUtil.showErrorMsg(title, err, getModel().getContext());
	}
	public PrintApplyAction() {
		super.setBtnName("重打申請");
		super.setCode("PrintApplyAction");
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}


	public void setCode(PrintApplyAction printApplyAction) {
		// TODO Auto-generated method stub
		super.setCode("PrintApplyAction");
	}
}
