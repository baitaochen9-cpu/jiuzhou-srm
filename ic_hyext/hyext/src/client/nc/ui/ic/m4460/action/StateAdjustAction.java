package nc.ui.ic.m4460.action;

import java.awt.event.ActionEvent;

import nc.ui.ic.m4460.utils.StateAdjustUIUtils;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.actions.EditAction;
import nc.ui.pubapp.uif2app.view.BillListView;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.uif2.UIState;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmpub.res.SCMActionCode;

/**
 * 库存状态调整单的“状态调整”动作
 * 
 * @since 6.0
 * @version 2011-3-6 上午09:48:47
 * @author wanghna
 */
public class StateAdjustAction extends EditAction {

  private static final long serialVersionUID = 510372490113110846L;

  // private IQueryAndRefreshManager dataManager;

  private BillListView list;

  private static final String[] items = new String[] {
    StateAdjustVO.NLOCKNUM, StateAdjustVO.NLOCKASSISTNUM, StateAdjustVO.NNUM,
    StateAdjustVO.NASSISTNUM, StateAdjustVO.NGROSSNUM, StateAdjustVO.NRSNUM
  };

  public StateAdjustAction() {
    super();
    SCMActionInitializer.initializeAction(this,
        SCMActionCode.IC_STATEADJUSTACTION);
  }

  @Override
  public void doAction(ActionEvent e) throws Exception {
    StateAdjustVO[] vos =
        (StateAdjustVO[]) this.getList().getBillListPanel().getHeadBillModel()
            .getBodySelectedVOs(StateAdjustVO.class.getName());
    if (ValueCheckUtil.isNullORZeroLength(vos)) {
      MessageDialog.showErrorDlg(
          null,
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008009_0",
              "04008009-0000")/* @res "提示" */, nc.vo.ml.NCLangRes4VoTransl
              .getNCLangRes().getStrByID("4008009_0", "04008009-0001")/*
                                                                       * @res
                                                                       * "请选择要库存状态调整的记录"
                                                                       */);
      return;
    }

    //序列号管理和存在预留的维度行不允许手输调整数量
    for (StateAdjustVO vo : vos) { 
      if(!StateAdjustUIUtils.isSnManager(vo)&&NCBaseTypeUtils.isLEZero(vo.getNrsnum())){
        continue;
      }
      
      if(!StringUtil.isSEmptyOrNull(vo.getVsncode())&&NCBaseTypeUtils.isGtZero(vo.getNrsnum())){
        MessageDialog.showErrorDlg(
            null,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                "4008009_0", "04008009-0000")/* @res "提示" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                "4008009_0", "04008009-0050")/* @res "请选择要库存状态调整的记录" */);
        return;
      }
      vo.setNadjustnum(vo.getNnum());
      vo.setNadjustassistnum(vo.getNassistnum());
    }

    this.getModel().initModel(vos);
    this.getList().getBillListPanel().setMultiSelect(false);
    this.getList().getBillListPanel().getHeadBillModel().setEnabled(true);
    this.getModel().setUiState(UIState.EDIT);
    this.setItemsUnEditable();
  }

  private void setItemsUnEditable() {
    if (ValueCheckUtil.isNullORZeroLength(StateAdjustAction.items)) {
      return;
    }
    BillListPanel list = this.getList().getBillListPanel();
    BillItem billitem = null;
    for (String item : StateAdjustAction.items) {
      billitem = list.getHeadItem(item);
      if (null != billitem) {
        billitem.setEdit(false);
      }
    }
  }

  // public IQueryAndRefreshManager getDataManager() {
  // return this.dataManager;
  // }

  public BillListView getList() {
    return this.list;
  }

  // public void setDataManager(IQueryAndRefreshManager dataManager) {
  // this.dataManager = dataManager;
  // }

  public void setList(BillListView list) {
    this.list = list;
  }

  @Override
  protected boolean isActionEnable() {
    /*StateAdjustVO[] vos =
        (StateAdjustVO[]) this.getList().getBillListPanel().getHeadBillModel()
            .getBodySelectedVOs(StateAdjustVO.class.getName());
    // Object[] objs = this.getList().getModel().getSelectedOperaDatas();
    if (ValueCheckUtil.isNullORZeroLength(vos))
      return false;
    if (!StringUtil.isSEmptyOrNull(vos[0].getVbillcode()))
      return false;*/
    return super.isActionEnable();
  }

}
