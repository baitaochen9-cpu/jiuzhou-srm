package nc.ui.cm.inprostuff.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class zdpd_action_extend extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.pubapp.plugin.action.InsertActionInfo getInsertActionInfo_0(){
 if(context.get("nc.ui.pubapp.plugin.action.InsertActionInfo#0")!=null)
 return (nc.ui.pubapp.plugin.action.InsertActionInfo)context.get("nc.ui.pubapp.plugin.action.InsertActionInfo#0");
  nc.ui.pubapp.plugin.action.InsertActionInfo bean = new nc.ui.pubapp.plugin.action.InsertActionInfo();
  context.put("nc.ui.pubapp.plugin.action.InsertActionInfo#0",bean);
  bean.setActionContainer((nc.ui.uif2.actions.IActionContainer)findBeanInUIF2BeanFactory("actionsOfCard"));
  bean.setActionType("edit");
  bean.setTarget((javax.swing.Action)findBeanInUIF2BeanFactory("getPriceAction"));
  bean.setPos("after");
  bean.setAction(getZdydxsAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.cm.inprostuff.action.AutoinprostuffAction getZdydxsAction(){
 if(context.get("zdydxsAction")!=null)
 return (nc.ui.cm.inprostuff.action.AutoinprostuffAction)context.get("zdydxsAction");
  nc.ui.cm.inprostuff.action.AutoinprostuffAction bean = new nc.ui.cm.inprostuff.action.AutoinprostuffAction();
  context.put("zdydxsAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.pubapp.uif2app.view.BillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setGoComponent((nc.ui.uif2.components.IAutoShowUpComponent)findBeanInUIF2BeanFactory("listView"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
