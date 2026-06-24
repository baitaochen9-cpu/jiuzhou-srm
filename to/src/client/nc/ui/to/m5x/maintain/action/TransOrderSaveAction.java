/*zOS88nlpyIMXjP1tMO0jODwjXGyWzJjcBqHCPKEfHtZWHo59C1X8282FgwawT9Lo*/
package nc.ui.to.m5x.maintain.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.vo.ic.pub.exp.ReserveCheckException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.scmpub.exp.AtpNotEnoughException;
import nc.vo.scmpub.res.BusinessCheck;
import nc.vo.scmpub.res.billtype.TOBillType;
import nc.vo.to.m5x.entity.BillItemVO;
import nc.vo.to.m5x.entity.BillVO;
import nc.vo.to.m5x.enumeration.BillStatus;
import nc.vo.to.m5x.pub.M5XVOBusiRuleUtil;
import nc.vo.trade.checkrule.VOChecker;

import nc.itf.material.mdm.SendMdmItf;
import nc.itf.pubapp.pub.exception.IResumeException;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.AbstractFunclet;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.billref.push.BillPushConst;
import nc.ui.pubapp.billref.push.RewriteInfo;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.SaveScriptAction;
import nc.ui.scmpub.util.ResumeExceptionUIProcessUtils;
import nc.ui.uif2.UIState;

public class TransOrderSaveAction extends SaveScriptAction {

  private static final long serialVersionUID = 1422077764571906889L;

  public boolean checkReserve(ReserveCheckException e) {
    int iresults =
        MessageDialog.showYesNoCancelDlg(
            WorkbenchEnvironment.getInstance().getWorkbench().getParent(),
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
                "04008001-0054")/*@res "警告"*/,
            e.getMessage()
                + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                    "4008001_0", "04008001-0055")/*@res "是否删除预留记录。"*/);
    if (UIDialog.ID_YES == iresults) {
      PfUserObject userObj = this.getFlowContext().getUserObj();
      userObj = userObj == null ? new PfUserObject() : userObj;
      userObj.getBusinessCheckMap().put("ReserveCheck", true);
      this.getFlowContext().setUserObj(userObj);
      return true;
    }
    return false;
  }

  @Override
  public void doAction(ActionEvent e) throws Exception {
    if (this.getModel().getUiState() == UIState.EDIT) {
      Object obj = this.editor.getValue();
      int index = this.getModel().findBusinessData(obj);
      if (index == -1) {
        ExceptionUtils
            .wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
                .getStrByID("4009011_0", "04009011-0040")/*@res "修改保存时，获取前台差异VO出错。"*/);
      }
      // BillVO oldVO = (BillVO) this.getModel().getData().get(index);
      BillVO newVO = (BillVO) obj;
      // 保存不通过状态重置为自由态
      this.reSetBillStatusForNoPass(newVO);
    }
    // add by kongzhk start 2025-09-01 单价校验
    BillVO bill = (BillVO) this.editor.getValue();
//    String cinstockorgvid = bill.getParentVO().getCinstockorgvid();//调入库存级组织
    BillItemVO[] items = bill.getChildrenVO();
	for(BillItemVO item : items){
		UFDouble price = item.getNorignetprice();
		if(price == null || UFDouble.ZERO_DBL.compareTo(price) == 0){
			int res = MessageDialog.showOkCancelDlg(null, "单价提示", "行号为【" + item.getCrowno() + "】的明细行单价为空，是否继续保存。");
			if(res !=1){
				return;
			}
		}
//		String vbdef1 = item.getVbdef1();
//		if(vbdef1 == null || vbdef1.isEmpty() ){
////			int res = MessageDialog.showErrorDlg(null, "数据转换", "行号为【" + item.getCrowno() + "】的明细行未转换成目标组织数据，请检查物料是否进行主数据关联，是否继续保存。");
//			throw new BusinessException("行号为【" + item.getCrowno() + "】的明细行未转换成目标组织数据，请检查物料是否进行主数据关联，是否继续保存。");
//		}
	}
    //end
    super.doAction(e);
    this.doAfterAction();
  }

  @Override
  protected boolean isResume(IResumeException resumeInfo) {
    // 调用wangweir框架统一处理
    return ResumeExceptionUIProcessUtils.isResume(resumeInfo,
        this.getFlowContext());
    // // ATP检查
    // return this.processCheck(resumeInfo);
  }
  
  SendMdmItf itf = NCLocator.getInstance().lookup(SendMdmItf.class);
  @Override
  protected Object[] processBefore(Object[] vos) {
    // 必要的完整性检查
    for (Object vo : vos) {
      BillVO ordervo = (BillVO) vo;
      BillItemVO[] bvos = ordervo.getChildrenVO();
      if (bvos == null || bvos.length == 0) {
        ExceptionUtils
            .wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
                .getStrByID("4009011_0", "04009011-0041")/*@res "表体数据为空，不允许保存。"*/);
      }
      String dest_org = ordervo.getParentVO().getCinstockorgid();//调入库存级组织
      List<String> rows = new ArrayList<String>();
      for(BillItemVO body  : bvos){
    	  String cinventoryvid = body.getCinventoryvid();//调出物料
    	  //检查调入公司物料
    	  String vbdef1 = body.getVbdef1();//自定义项 1= 调入公司物料ID
    	  if(null == vbdef1 || vbdef1.isEmpty()){
    		  //尝试通过调入公司ID和主数据进行匹配
    		  try {
				vbdef1 =  itf.materialChangeByOrg(cinventoryvid, dest_org);
				if(null == vbdef1 || vbdef1.isEmpty()){
					rows.add(body.getCrowno());
					}else{
						 body.setVbdef1(vbdef1);
					}
			} catch (BusinessException e) {
					ExceptionUtils.wrappBusinessException("主数据匹配异常，请检查物料ID:"+cinventoryvid);
			}
    	  }
    	  if(null != rows && rows.size() > 0){
				ExceptionUtils.wrappBusinessException("物料【"+rows.toString()
							+"】无法匹配到对方公司物料，请检查双方主数据关联!");
			
    	  }
      }
      
      
      if (VOChecker.isEmpty(this.getModel().getContext().getPk_org())) {
        ExceptionUtils
            .wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
                .getStrByID("4009011_0", "04009011-0042")/*@res "主组织为空，不允许保存。"*/);
      }
      this.fillBizData(ordervo, this.getModel().getUiState());
    }
    // 补充业务流程
    PfServiceScmUtil.setBusiType((BillVO[]) vos,
        TOBillType.TransOrder.getCode());
    return vos;
  }

  private void doAfterAction() {
    if (this.getModel().getUiState() == UIState.EDIT) {
      BillVO bill = (BillVO) this.editor.getValue();
      this.rewriteM4331UINum(bill);
    }
  }

  private void fillBizData(BillVO value, UIState state) {
    if (state == AppUiState.ADD.getUiState()) {
      M5XVOBusiRuleUtil.fillOperator(new BillVO[] {
        value
      });
    }
    M5XVOBusiRuleUtil.fillTakeOutIfNull(new BillVO[] {
      value
    });
    M5XVOBusiRuleUtil.calculateHeadTotalNum(value);
  }

  @SuppressWarnings("boxing")
  private boolean processCheck(IResumeException resumeInfo) {
    boolean isResume = true;
    int back = 0;
    // 可用量检查***
    if (BusinessCheck.ATPCheck.getCheckCode().equals(
        resumeInfo.getBusiExceptionType())) {
      back =
          MessageDialog
              .showYesNoDlg(
                  WorkbenchEnvironment.getInstance().getWorkbench().getParent(),
                  NCLangRes.getInstance().getStrByID("4009011_0",
                      "04009011-0293")/*调拨订单可用量检查*/,
                  ((AtpNotEnoughException) resumeInfo).getMessage());

      // 可用量不足继续
      if (UIDialog.ID_YES == back) {
        isResume = true;
        PfUserObject userObj = this.getFlowContext().getUserObj();
        userObj = userObj == null ? new PfUserObject() : userObj;
        userObj.getBusinessCheckMap().put(
            BusinessCheck.ATPCheck.getCheckCode(), !isResume);
        this.getFlowContext().setUserObj(userObj);
      }
      else {
        isResume = false;
      }
    }
    // 预留检查***
    else if (resumeInfo instanceof ReserveCheckException) {
      isResume = this.checkReserve((ReserveCheckException) resumeInfo);
    }
    return isResume;
  }

  private void reSetBillStatusForNoPass(BillVO vo) {
    Integer fstatusflag = vo.getParentVO().getFstatusflag();
    if (BillStatus.NOPASS.value().equals(fstatusflag)) {
      vo.getParentVO().setFstatusflag((Integer) BillStatus.FREE.value());
      vo.getParentVO().setApprover(null);
      vo.getParentVO().setTaudittime(null);

      BillItemVO[] items = vo.getChildrenVO();
      if (items != null) {
        for (BillItemVO item : items) {
          item.setFrowstatuflag((Integer) BillStatus.FREE.value());
        }
      }

      this.editor.setValue(vo);
    }
  }

  /**
   * 调拨订单保存时回写来源界面
   * 
   * @param bill
   */
  private void rewriteM4331UINum(BillVO bill) {
    BillItemVO[] items = bill.getChildrenVO();
    int len = items.length;
    RewriteInfo[] rewriteInfos = new RewriteInfo[len];
    for (int i = 0; i < len; i++) {
      rewriteInfos[i] = new RewriteInfo();
      rewriteInfos[i].setRewriteNum(items[i].getNnum());
      rewriteInfos[i].setSourceHeadId(items[i].getCsrcid());
      rewriteInfos[i].setSourceRowId(items[i].getCsrcbid());
      rewriteInfos[i].setSourceType(items[i].getVsrctype());
    }

    ((AbstractFunclet) this.getModel().getContext().getEntranceUI())
        .fireFuncletLinkEvent(rewriteInfos, BillPushConst.BILL_PUSH);
  }

}

/*zOS88nlpyIMXjP1tMO0jODwjXGyWzJjcBqHCPKEfHtZWHo59C1X8282FgwawT9Lo*/