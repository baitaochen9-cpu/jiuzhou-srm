package nc.ui.pu.m422x.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class storereq_editor extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.pu.m422x.editor.org.OrgChangedEventHandler getPurhchaseOrganization(){
 if(context.get("purhchaseOrganization")!=null)
 return (nc.ui.pu.m422x.editor.org.OrgChangedEventHandler)context.get("purhchaseOrganization");
  nc.ui.pu.m422x.editor.org.OrgChangedEventHandler bean = new nc.ui.pu.m422x.editor.org.OrgChangedEventHandler();
  context.put("purhchaseOrganization",bean);
  bean.setCardForm((nc.ui.pubapp.uif2app.view.ShowUpableBillForm)findBeanInUIF2BeanFactory("billFormEditor"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler getCardHeadBeforeEdit(){
 if(context.get("cardHeadBeforeEdit")!=null)
 return (nc.ui.pu.m422x.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler)context.get("cardHeadBeforeEdit");
  nc.ui.pu.m422x.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler bean = new nc.ui.pu.m422x.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler();
  context.put("cardHeadBeforeEdit",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.editor.card.beforeedit.CardBodyBeforeEditEventHandler getCardBodyBeforeEdit(){
 if(context.get("cardBodyBeforeEdit")!=null)
 return (nc.ui.pu.m422x.editor.card.beforeedit.CardBodyBeforeEditEventHandler)context.get("cardBodyBeforeEdit");
  nc.ui.pu.m422x.editor.card.beforeedit.CardBodyBeforeEditEventHandler bean = new nc.ui.pu.m422x.editor.card.beforeedit.CardBodyBeforeEditEventHandler();
  context.put("cardBodyBeforeEdit",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.editor.card.afteredit.CardHeadTailAfterEditEventHandler getCardHeadAfterEdit(){
 if(context.get("cardHeadAfterEdit")!=null)
 return (nc.ui.pu.m422x.editor.card.afteredit.CardHeadTailAfterEditEventHandler)context.get("cardHeadAfterEdit");
  nc.ui.pu.m422x.editor.card.afteredit.CardHeadTailAfterEditEventHandler bean = new nc.ui.pu.m422x.editor.card.afteredit.CardHeadTailAfterEditEventHandler();
  context.put("cardHeadAfterEdit",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.editor.card.afteredit.CardBodyAfterEditEventHandler getCardBodyAfterEdit(){
 if(context.get("cardBodyAfterEdit")!=null)
 return (nc.ui.pu.m422x.editor.card.afteredit.CardBodyAfterEditEventHandler)context.get("cardBodyAfterEdit");
  nc.ui.pu.m422x.editor.card.afteredit.CardBodyAfterEditEventHandler bean = new nc.ui.pu.m422x.editor.card.afteredit.CardBodyAfterEditEventHandler();
  context.put("cardBodyAfterEdit",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.editor.card.afteredit.RelationCalculate getRelationCalculate(){
 if(context.get("relationCalculate")!=null)
 return (nc.ui.pu.m422x.editor.card.afteredit.RelationCalculate)context.get("relationCalculate");
  nc.ui.pu.m422x.editor.card.afteredit.RelationCalculate bean = new nc.ui.pu.m422x.editor.card.afteredit.RelationCalculate();
  context.put("relationCalculate",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.editor.list.HeadRowChangeHandler getList_head_rowchangehandler(){
 if(context.get("list_head_rowchangehandler")!=null)
 return (nc.ui.pu.m422x.editor.list.HeadRowChangeHandler)context.get("list_head_rowchangehandler");
  nc.ui.pu.m422x.editor.list.HeadRowChangeHandler bean = new nc.ui.pu.m422x.editor.list.HeadRowChangeHandler();
  context.put("list_head_rowchangehandler",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.model.AppEventHandlerMediator getAppEventHandlerMediator(){
 if(context.get("appEventHandlerMediator")!=null)
 return (nc.ui.pubapp.uif2app.model.AppEventHandlerMediator)context.get("appEventHandlerMediator");
  nc.ui.pubapp.uif2app.model.AppEventHandlerMediator bean = new nc.ui.pubapp.uif2app.model.AppEventHandlerMediator();
  context.put("appEventHandlerMediator",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.IAppModelEx)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setHandlerMap(getManagedMap0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("nc.ui.pubapp.uif2app.event.OrgChangedEvent",getManagedList0());  map.put("nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent",getManagedList1());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent",getManagedList2());  map.put("nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent",getManagedList3());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent",getManagedList4());  map.put("nc.ui.pubapp.uif2app.event.list.ListHeadRowChangedEvent",getManagedList5());  return map;}

private List getManagedList0(){  List list = new ArrayList();  list.add(getPurhchaseOrganization());  return list;}

private List getManagedList1(){  List list = new ArrayList();  list.add(getCardHeadBeforeEdit());  return list;}

private List getManagedList2(){  List list = new ArrayList();  list.add(getCardBodyBeforeEdit());  return list;}

private List getManagedList3(){  List list = new ArrayList();  list.add(getCardHeadAfterEdit());  return list;}

private List getManagedList4(){  List list = new ArrayList();  list.add(getCardBodyAfterEdit());  list.add(getRelationCalculate());  return list;}

private List getManagedList5(){  List list = new ArrayList();  list.add(getList_head_rowchangehandler());  return list;}

}
