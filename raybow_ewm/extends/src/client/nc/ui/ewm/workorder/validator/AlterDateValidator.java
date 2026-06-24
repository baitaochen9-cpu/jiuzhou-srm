 package nc.ui.ewm.workorder.validator;
 
 import nc.bs.logging.Logger;
import nc.bs.uif2.validation.ValidationFailure;
import nc.bs.uif2.validation.Validator;
import nc.vo.am.common.util.ArrayConstructor;
import nc.vo.am.pub.uap.PFUtils;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WorkOrderHeadVO;
import nc.vo.ewm.workorder.utils.TransiTypeUtils;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uif2.LoginContext;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
 
 
 
 
 
 public class AlterDateValidator
   implements Validator
 {
   private static final long serialVersionUID = 1L;
   public LoginContext context = null;
   
 
   public ValidationFailure validate(Object obj)
   {
     ValidationFailure validationFailure = null;
    StringBuffer errorMessage = new StringBuffer();
     AggregatedValueObject[] billVOs = (AggregatedValueObject[])ArrayConstructor.getArray(obj);
     
 
     for (int i = 0; i < billVOs.length; i++)
     {
       AggWorkOrderVO billVO = (AggWorkOrderVO)billVOs[i];
      WorkOrderHeadVO headVO = billVO.getParentVO();
      Integer iStatusType = headVO.getWo_statustype();
       
     if (UFBoolean.TRUE.equals(headVO.getStatus_follow())) {
        errorMessage.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0502") + "\n");
     } else if (iStatusType.equals(Integer.valueOf(-1))) {
        errorMessage.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0167") + "\n");
       }
       else
       {
         String transi_type = headVO.getTransi_type();
         if (transi_type == null) {
           transi_type = headVO.getBill_type();
         }
         boolean isStartWorkFlow = false;
         try {
           isStartWorkFlow = PFUtils.isStartWorkFlow(transi_type, headVO.getPk_wo(), WorkflowTypeEnum.Workflow.getIntValue());
         }
         catch (BusinessException e) {
           Logger.error(e.getMessage(), e);
           validationFailure = new ValidationFailure();
           validationFailure.setMessage(e.getMessage());
           return validationFailure;
         }
         if (isStartWorkFlow) {
          errorMessage.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0168") + "\n");
         }
         else
         {
           boolean isAfterward = TransiTypeUtils.isAfterward(transi_type);
           if (((!isAfterward) && (iStatusType.equals(Integer.valueOf(0)))) || ((isAfterward) && (iStatusType.equals(Integer.valueOf(2)))))
           {
             if (PFUtils.isExistWorkflowDefinition(transi_type, headVO.getPk_org(), headVO.getBillmaker(), WorkflowTypeEnum.Workflow.getIntValue()))
             {
               errorMessage.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0169") + "\n");
             }
           }
         }
       }
     }
     
 
 
    if (errorMessage.length() > 0) {
       validationFailure = new ValidationFailure();
      validationFailure.setMessage(errorMessage.toString());
     }
     return validationFailure;
   }
   
   public LoginContext getContext() {
     if (context == null) {
       context = new LoginContext();
     }
     return context;
   }
   
   public void setContext(LoginContext context) {
     this.context = context;
   }
 }