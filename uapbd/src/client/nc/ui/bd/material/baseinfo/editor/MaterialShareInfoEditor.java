package nc.ui.bd.material.baseinfo.editor;

import javax.swing.SwingUtilities;

import nc.itf.fi.pub.SysInit;
import nc.pub.billcode.vo.BillCodeContext;
import nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillForm;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.pub.NODE_TYPE;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uif2.LoginContext;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class MaterialShareInfoEditor extends BillForm {

  @Override
  public void initUI() {
    super.initUI();
    initOrgRefPane();
    setRequestFocus(false);
  }

  private void initOrgRefPane() {
    UIRefPane orgPane =
        (UIRefPane) getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).getComponent();
    orgPane.getRefModel().setUseDataPower(false);
    orgPane.getRefModel().setFilterPks(getFuncPermissionOrgIDs());
  }

  private String[] getFuncPermissionOrgIDs() {
    String[] pk_orgs = getModel().getContext().getFuncInfo().getFuncPermissionPkorgs();

    return pk_orgs == null ? new String[0] : pk_orgs;
  }

  @Override
  protected void onAdd() {
    super.onAdd();
    getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).setEnabled(
        isOrgNode() && isNotCreateVersion());
  }

  private boolean isOrgNode() {
    NODE_TYPE node_type = getModel().getContext().getNodeType();
    return node_type == NODE_TYPE.ORG_NODE;
  }

  private boolean isNotCreateVersion() {
    String createVersionPk = ((MaterialBaseInfoModel) getModel()).getCreateVersionPk();
    if (StringUtils.isBlank(createVersionPk)) {
      return true;
    }
    return false;
  }

  @Override
  protected void onEdit() {
    super.onEdit();
    getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).setEnabled(false);
  }

  @Override
  protected void setDefaultValue() {
    super.setDefaultValue();
    LoginContext context = getModel().getContext();
    getBillCardPanel().setHeadItem(MaterialVO.PK_ORG, context.getPk_org());
  }

  public void initCodeByBillCodeRule(BillCodeContext billCodeContext, String billcode) {
    if (billCodeContext != null) {
      BillItem codeItem = getBillCardPanel().getHeadItem(MaterialVO.CODE);
      codeItem.setEnabled(billCodeContext.isEditable());
      UFBoolean paraBoolean = UFBoolean.FALSE;
      try {
		paraBoolean = SysInit.getParaBoolean("0001V110000000000FH0", "BD306");/*yezhian 20260204  bug 00000033*/
	} catch (BusinessException e) {
		// 
		e.printStackTrace();
	}
      if (billCodeContext.isPrecode() ||  (null != billcode && paraBoolean.booleanValue()) /*yezhian 20260204 bug 00000033*/) {
        codeItem.setValue(billcode);
      }
      else {
        if (getModel().getUiState() == UIState.ADD ) {
          codeItem.setValue(null);
          codeItem.setEnabled(false);
        }
        else {
          codeItem.setValue(billcode);
        }

      }
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        String pk_org = (String) getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).getValueObject();
        if (StringUtils.isBlank(pk_org)) {
          getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).getComponent().requestFocus();
        }
        else if (getBillCardPanel().getHeadItem(MaterialVO.CODE).isEnabled()) {
          getBillCardPanel().getHeadItem(MaterialVO.CODE).getComponent().requestFocus();
        }
        else {
          getBillCardPanel().getHeadItem(MaterialVO.NAME).getComponent().requestFocus();
        }
      }
    });
  }

}
