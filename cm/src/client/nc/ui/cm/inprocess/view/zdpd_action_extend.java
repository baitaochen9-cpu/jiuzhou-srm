package nc.ui.cm.inprocess.view;

import java.util.HashMap;
import java.util.Map;

import nc.ui.uif2.components.IAutoShowUpComponent;
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
  bean.setTarget((javax.swing.Action)findBeanInUIF2BeanFactory("mmPullDataAction"));
  bean.setPos("after");
  bean.setAction(getZdpdAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.cm.inprocess.action.InprocessZdpdDataAction getZdpdAction(){
 if(context.get("zdpdAction")!=null)
 return (nc.ui.cm.inprocess.action.InprocessZdpdDataAction)context.get("zdpdAction");
  nc.ui.cm.inprocess.action.InprocessZdpdDataAction bean = new nc.ui.cm.inprocess.action.InprocessZdpdDataAction();
  context.put("zdpdAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.pubapp.uif2app.view.BillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setGoComponent((IAutoShowUpComponent)findBeanInUIF2BeanFactory("listView"));
 setBeanFacotryIfBeanFacatoryAware(bean);
 invokeInitializingBean(bean);
 return bean;
}
}
