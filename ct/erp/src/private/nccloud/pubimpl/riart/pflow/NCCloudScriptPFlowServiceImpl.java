package nccloud.pubimpl.riart.pflow;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.bs.pub.pf.CloudPfUtil;
import nccloud.pubitf.pub.pflow.CloudPFlowParamUtil;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;
import nccloud.pubitf.riart.pflow.ICloudScriptPFlowService;


public class NCCloudScriptPFlowServiceImpl  implements ICloudScriptPFlowService{
	
	
  public Object[] exeScriptPFlow_RequiresNew(CloudPFlowContext context)   throws BusinessException {
	    return this.exeScriptPFlow(context);
	  }

	  @SuppressWarnings("rawtypes")
	  private Object[] procFlow(CloudPFlowContext context) {
	    Object[] retObjs = null;
	    if(context.geteParam()==null ){
	    	Map<Object, Object> eparam = new HashMap<Object,Object>();
	    	eparam.put(CloudPFlowParamUtil.IS_COULD_ENTRY, true);
	    	context.seteParam(eparam);
	    }
	    
	    if(context.geteParam()!=null && !context.geteParam().containsKey(CloudPFlowParamUtil.IS_COULD_ENTRY)){
	    	context.geteParam().put(CloudPFlowParamUtil.IS_COULD_ENTRY, true);
	    }
	    
	    try {
	      if (context.getBillVos() != null && context.getBillVos().length > 1) {
	        retObjs =
	            CloudPfUtil.runBatch(
	                context.getActionName(),
	                context.getTrantype() == null ? context.getBillType() : context
	                    .getTrantype(),
	                context.getBillVos(),
	                context.getBatchUserObj(),
	                null,
	                context.geteParam() == null ? null : (HashMap) context
	                    .geteParam());
	      }
	      else if (context.getBillVos() != null && context.getBillVos().length == 1) {
	        Object retObj =
	            CloudPfUtil.runAction(
	                context.getActionName(),
	                context.getTrantype() == null ? context.getBillType() : context
	                    .getTrantype(), context.getBillVos()[0], context
	                    .getUserObj() == null ? null : context.getUserObj()[0].getUserObject(),
	                null, null, context.geteParam() == null ? null
	                    : (HashMap) context.geteParam());

	        if (retObj instanceof Object[]) {
	          retObjs = (Object[]) retObj;
	        }
	        else {
	          retObjs = new Object[] {
	            retObj
	          };
	        }
	      }
	      else {
	        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501002_0","01501002-0323")/*@res 单据数据为空！*/);
	        return null;
	      }
	    }
	    catch (Exception e) {
	      ExceptionUtils.wrappException(e);
	    }
	    return retObjs;
	  }


	public Object[] exeScriptPFlow(CloudPFlowContext context)
			throws BusinessException {
		Object[] ret = this.procFlow(context);
	    return ret;
	}

	@Override
	public Object[] exeScriptPFlow_CommitNoFlowBatch(CloudPFlowContext context)
			throws BusinessException {
	    Object[] retObjs = null;
	    try {
	      if (context.getBillVos() != null && context.getBillVos().length >= 1) {
	        retObjs =
	            CloudPfUtil.runScriptPFlow_CommitNoFlowBatch(
	                context.getActionName(),
	                context.getTrantype() == null ? context.getBillType() : context
	                    .getTrantype(),
	                context.getBillVos(),
	                context.getBatchUserObj(),
	                null,
	                context.geteParam() == null ? null : (HashMap) context
	                    .geteParam());
	      }  else {
	        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501002_0","01501002-0323")/*@res 单据数据为空！*/);
	        return null;
	      }
	    }
	    catch (Exception e) {
	      ExceptionUtils.wrappException(e);
	    }
	    return retObjs;
	  }
	
	
	@Override
	public Object[] exeScriptPFlow_UnSaveNoFlowBatch(CloudPFlowContext context)
			throws BusinessException {
	    Object[] retObjs = null;
	    try {
	      if (context.getBillVos() != null && context.getBillVos().length >= 1) {
	        retObjs =
	            CloudPfUtil.runScriptPFlow_UnSaveNoFlowBatch(
	                context.getActionName(),
	                context.getTrantype() == null ? context.getBillType() : context
	                    .getTrantype(),
	                context.getBillVos(),
	                context.getBatchUserObj(),
	                null,
	                context.geteParam() == null ? null : (HashMap) context
	                    .geteParam());
	      }  else {
	        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501002_0","01501002-0323")/*@res 单据数据为空！*/);
	        return null;
	      }
	    }
	    catch (Exception e) {
	      ExceptionUtils.wrappException(e);
	    }
	    return retObjs;
	  }





}
