package nc.ui.ewm.workorder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class PluginBeanConfigFilePath_print extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.ewm.workorder.action.PrintApplyAction getPrintApplyAction(){
 if(context.get("PrintApplyAction")!=null)
 return (nc.ui.ewm.workorder.action.PrintApplyAction)context.get("PrintApplyAction");
  nc.ui.ewm.workorder.action.PrintApplyAction bean = new nc.ui.ewm.workorder.action.PrintApplyAction();
  context.put("PrintApplyAction",bean);
  bean.setModel((nc.ui.am.model.BillManageModel)findBeanInUIF2BeanFactory("model"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
