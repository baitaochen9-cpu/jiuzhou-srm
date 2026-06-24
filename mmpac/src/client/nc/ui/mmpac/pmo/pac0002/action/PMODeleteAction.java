package nc.ui.mmpac.pmo.pac0002.action;


import com.ibm.db2.jcc.am.i;

import nc.bs.framework.common.NCLocator;
import nc.cmp.bill.util.SysInit;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.mmpac.pmo.pac0002.IPMOMaintainService;
import nc.itf.pubapp.pub.exception.IResumeException;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.pub.task.ISingleBillService;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.vo.mmpac.pmo.pac0002.constant.PMOConst;
import nc.vo.mmpac.pmo.pac0002.constant.PMOConstLang;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.mmpac.pmo.pac0002.enumeration.PMOFBillstatusEnum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.exp.AtpNotEnoughException;
import nc.vo.scmpub.res.BusinessCheck;

public class PMODeleteAction extends nc.ui.pubapp.uif2app.actions.DeleteAction {

    private static final long serialVersionUID = -3827961430468974005L;

    @Override
    protected boolean isActionEnable() {
        Object[] objects = ((nc.ui.pubapp.uif2app.model.BillManageModel) this.getModel()).getSelectedOperaDatas();
        if (MMArrayUtil.isEmpty(objects)) {
            return false;
        }
        if (objects.length > 1) {
        	
            return true;
        }
        PMOAggVO aggvo = (PMOAggVO) objects[0];
			if (PMOFBillstatusEnum.FREE.equalsValue(aggvo.getParentVO().getFbillstatus())) {
			    return true;
			}
	
        return false;
    }

    @Override
    public ISingleBillService<Object> getSingleBillService() {
        ISingleBillService<Object> service = new ISingleBillService<Object>() {
            @Override
            public Object operateBill(Object bill) throws Exception {
                PMOAggVO agg = (PMOAggVO) bill;
                IPMOMaintainService deleteservice = NCLocator.getInstance().lookup(IPMOMaintainService.class);
                try {	
                	

                    deleteservice.delete(new PMOAggVO[] {
                        agg
                    });
                }
                catch (Exception ex) {
                    Throwable exp = ExceptionUtils.unmarsh(ex);
                    if (exp instanceof IResumeException) {
                        if (BusinessCheck.ATPCheck.getCheckCode().equals(
                                ((IResumeException) exp).getBusiExceptionType())) {
                            int back =
                                    MessageDialog.showYesNoDlg(WorkbenchEnvironment.getInstance().getWorkbench()
                                            .getParent(), PMOConstLang.getDLG_ATP_TITLE(),
                                            ((AtpNotEnoughException) exp).getMessage());
                            if (UIDialog.ID_YES == back) {
                                agg.getParentVO().setFatpcheck(PMOConst.ATPCHECK_OK);
                                deleteservice.delete(new PMOAggVO[] {
                                    agg
                                });
                            }
                            else {
                                ExceptionUtils.wrappBusinessException(PMOConstLang.getMSG_ATP_NOT_ENOUGH());
                            }
                        }
                    }
                    else {
                        ExceptionUtils.wrappException(ex);
                    }
                }
                return bill;
            }
        };
        return service;
    }
    	
   }
    
    

