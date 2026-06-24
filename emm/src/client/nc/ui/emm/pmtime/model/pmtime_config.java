/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.emm.pmtime.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class pmtime_config extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.emm.pmbase.view.PMBillCardView getBillForm(){
 if(context.get("billForm")!=null)
 return (nc.ui.emm.pmbase.view.PMBillCardView)context.get("billForm");
  nc.ui.emm.pmbase.view.PMBillCardView bean = new nc.ui.emm.pmbase.view.PMBillCardView();
  context.put("billForm",bean);
  bean.setModel(getModel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setBodyActionMap(getBodyActionsMap());
  bean.setHeadActions(getHeadTabActions());
  bean.setDefaultBodyActions(getDefaultBodyActions());
  bean.setModelDataManager(getModelDataManager());
  bean.setUserdefitemPreparator(getBillDataPreparator());
  bean.setClosingListener(getClosingListener());
  bean.setOrgPanel(getOrgPanel());
  bean.setClcStrategys(getCardListenerConnectStrategys());
  bean.setScaleProcessor(getBillCardDigitProcessor());
  bean.setCeStrategys(getCardComponentExtStrategys());
  bean.setComponentValueManager(getPMBillCardPanelValueAdapter_4cf1ad());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.emm.pmbase.model.PMBillCardPanelValueAdapter getPMBillCardPanelValueAdapter_4cf1ad(){
 if(context.get("nc.ui.emm.pmbase.model.PMBillCardPanelValueAdapter#4cf1ad")!=null)
 return (nc.ui.emm.pmbase.model.PMBillCardPanelValueAdapter)context.get("nc.ui.emm.pmbase.model.PMBillCardPanelValueAdapter#4cf1ad");
  nc.ui.emm.pmbase.model.PMBillCardPanelValueAdapter bean = new nc.ui.emm.pmbase.model.PMBillCardPanelValueAdapter();
  context.put("nc.ui.emm.pmbase.model.PMBillCardPanelValueAdapter#4cf1ad",bean);
  bean.setTableCodes(getTableCodes());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.view.PMBillListView getBillListView(){
 if(context.get("billListView")!=null)
 return (nc.ui.emm.pmbase.view.PMBillListView)context.get("billListView");
  nc.ui.emm.pmbase.view.PMBillListView bean = new nc.ui.emm.pmbase.view.PMBillListView();
  context.put("billListView",bean);
  bean.setModel(getModel());
  bean.setMultiSelectionEnable(false);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setModelDataManager(getModelDataManager());
  bean.setUserdefitemListPreparator(getDefItemListPreparator());
  bean.setClcStrategys(getListListenerConnectStrategys());
  bean.setScaleProcessor(getBillListDigitProcessor());
  bean.setPaginationBar(getPaginationBar());
  bean.setBillListPanelValueSetter(getPMBillListPanelValueAdapter_18ebd04());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.emm.pmbase.model.PMBillListPanelValueAdapter getPMBillListPanelValueAdapter_18ebd04(){
 if(context.get("nc.ui.emm.pmbase.model.PMBillListPanelValueAdapter#18ebd04")!=null)
 return (nc.ui.emm.pmbase.model.PMBillListPanelValueAdapter)context.get("nc.ui.emm.pmbase.model.PMBillListPanelValueAdapter#18ebd04");
  nc.ui.emm.pmbase.model.PMBillListPanelValueAdapter bean = new nc.ui.emm.pmbase.model.PMBillListPanelValueAdapter();
  context.put("nc.ui.emm.pmbase.model.PMBillListPanelValueAdapter#18ebd04",bean);
  bean.setTableCodes(getTableCodes());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.uif2.LoginContext getContext(){
 if(context.get("context")!=null)
 return (nc.vo.uif2.LoginContext)context.get("context");
  nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
  context.put("context",bean);
  bean.setNodeType(nc.vo.bd.pub.NODE_TYPE.ORG_NODE);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller getRemoteCallCombinatorCaller(){
 if(context.get("remoteCallCombinatorCaller")!=null)
 return (nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller)context.get("remoteCallCombinatorCaller");
  nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller bean = new nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller();
  context.put("remoteCallCombinatorCaller",bean);
  bean.setRemoteCallers(getManagedList0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList0(){  List list = new ArrayList();  list.add(getTemplateContainer());  list.add(getUserdefitemContainer());  list.add(getQueryTemplateContainer());  list.add(getPfAddInfoRemoteCaller());  return list;}

public nc.ui.uif2.editor.QueryTemplateContainer getQueryTemplateContainer(){
 if(context.get("queryTemplateContainer")!=null)
 return (nc.ui.uif2.editor.QueryTemplateContainer)context.get("queryTemplateContainer");
  nc.ui.uif2.editor.QueryTemplateContainer bean = new nc.ui.uif2.editor.QueryTemplateContainer();
  context.put("queryTemplateContainer",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.remotecall.PfAddInfoRemoteCallCombinator getPfAddInfoRemoteCaller(){
 if(context.get("pfAddInfoRemoteCaller")!=null)
 return (nc.ui.am.remotecall.PfAddInfoRemoteCallCombinator)context.get("pfAddInfoRemoteCaller");
  nc.ui.am.remotecall.PfAddInfoRemoteCallCombinator bean = new nc.ui.am.remotecall.PfAddInfoRemoteCallCombinator();
  context.put("pfAddInfoRemoteCaller",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayout(getTangramLayout_f58404());
  bean.setTangramLayoutRoot(getTBNode_a8a9ac());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.TangramLayout getTangramLayout_f58404(){
 if(context.get("nc.ui.uif2.tangramlayout.TangramLayout#f58404")!=null)
 return (nc.ui.uif2.tangramlayout.TangramLayout)context.get("nc.ui.uif2.tangramlayout.TangramLayout#f58404");
  nc.ui.uif2.tangramlayout.TangramLayout bean = new nc.ui.uif2.tangramlayout.TangramLayout();
  context.put("nc.ui.uif2.tangramlayout.TangramLayout#f58404",bean);
  bean.setContainerComponentStrategy(getAMContainerComponentStrategy_b3143c());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.layout.AMContainerComponentStrategy getAMContainerComponentStrategy_b3143c(){
 if(context.get("nc.ui.am.layout.AMContainerComponentStrategy#b3143c")!=null)
 return (nc.ui.am.layout.AMContainerComponentStrategy)context.get("nc.ui.am.layout.AMContainerComponentStrategy#b3143c");
  nc.ui.am.layout.AMContainerComponentStrategy bean = new nc.ui.am.layout.AMContainerComponentStrategy();
  context.put("nc.ui.am.layout.AMContainerComponentStrategy#b3143c",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_a8a9ac(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#a8a9ac")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#a8a9ac");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#a8a9ac",bean);
  bean.setShowMode("CardLayout");
  bean.setTabs(getManagedList1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add(getHSNode_1744142());  list.add(getVSNode_2ef44c());  return list;}

private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_1744142(){
 if(context.get("nc.ui.uif2.tangramlayout.node.HSNode#1744142")!=null)
 return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#1744142");
  nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
  context.put("nc.ui.uif2.tangramlayout.node.HSNode#1744142",bean);
  bean.setLeft(getCNode_ae2527());
  bean.setRight(getVSNode_1921c1f());
  bean.setDividerLocation(210f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_ae2527(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#ae2527")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#ae2527");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#ae2527",bean);
  bean.setComponent(getQueryArea());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_1921c1f(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#1921c1f")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#1921c1f");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#1921c1f",bean);
  bean.setUp(getCNode_1092d05());
  bean.setDown(getBillListViewNode_16ad81c());
  bean.setDividerLocation(25f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1092d05(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1092d05")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1092d05");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1092d05",bean);
  bean.setComponent(getQueryInfo());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.editor.uf2.BillListViewNode getBillListViewNode_16ad81c(){
 if(context.get("nc.ui.am.editor.uf2.BillListViewNode#16ad81c")!=null)
 return (nc.ui.am.editor.uf2.BillListViewNode)context.get("nc.ui.am.editor.uf2.BillListViewNode#16ad81c");
  nc.ui.am.editor.uf2.BillListViewNode bean = new nc.ui.am.editor.uf2.BillListViewNode();
  context.put("nc.ui.am.editor.uf2.BillListViewNode#16ad81c",bean);
  bean.setComponent(getBillListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_2ef44c(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#2ef44c")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#2ef44c");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#2ef44c",bean);
  bean.setUp(getCNode_16883fc());
  bean.setDown(getBillFormNode_b154b0());
  bean.setDividerLocation(25f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_16883fc(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#16883fc")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#16883fc");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#16883fc",bean);
  bean.setComponent(getCardPnlInfo());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.editor.uf2.BillFormNode getBillFormNode_b154b0(){
 if(context.get("nc.ui.am.editor.uf2.BillFormNode#b154b0")!=null)
 return (nc.ui.am.editor.uf2.BillFormNode)context.get("nc.ui.am.editor.uf2.BillFormNode#b154b0");
  nc.ui.am.editor.uf2.BillFormNode bean = new nc.ui.am.editor.uf2.BillFormNode();
  context.put("nc.ui.am.editor.uf2.BillFormNode#b154b0",bean);
  bean.setComponent(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getQueryInfo(){
 if(context.get("queryInfo")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("queryInfo");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("queryInfo",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell getQueryArea(){
 if(context.get("queryArea")!=null)
 return (nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell)context.get("queryArea");
  nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell bean = new nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell();
  context.put("queryArea",bean);
  bean.setQueryAreaCreator(getQueryAction());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getListPnlInfo(){
 if(context.get("listPnlInfo")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("listPnlInfo");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("listPnlInfo",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.view.AMUECardLayoutToolbarPanel getCardPnlInfo(){
 if(context.get("cardPnlInfo")!=null)
 return (nc.ui.am.view.AMUECardLayoutToolbarPanel)context.get("cardPnlInfo");
  nc.ui.am.view.AMUECardLayoutToolbarPanel bean = new nc.ui.am.view.AMUECardLayoutToolbarPanel();
  context.put("cardPnlInfo",bean);
  bean.setActions(getManagedList2());
  bean.setTitleAction(getReturnaction());
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getAttachmentAction());  list.add(getActionsBar_ActionsBarSeparator_11b60ab());  list.add(getFirstLineAction());  list.add(getPreLineAction());  list.add(getNextLineAction());  list.add(getLastLineAction());  list.add(getActionsBar_ActionsBarSeparator_679f05());  list.add(getHeadViewMaxAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_11b60ab(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#11b60ab")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#11b60ab");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#11b60ab",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_679f05(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#679f05")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#679f05");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#679f05",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.action.support.ReturnToListAction getReturnaction(){
 if(context.get("returnaction")!=null)
 return (nc.ui.am.action.support.ReturnToListAction)context.get("returnaction");
  nc.ui.am.action.support.ReturnToListAction bean = new nc.ui.am.action.support.ReturnToListAction();
  context.put("returnaction",bean);
  bean.setGoComponent(getBillListView());
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.orgpanel.OrgPanel getOrgPanel(){
 if(context.get("orgPanel")!=null)
 return (nc.ui.am.orgpanel.OrgPanel)context.get("orgPanel");
  nc.ui.am.orgpanel.OrgPanel bean = new nc.ui.am.orgpanel.OrgPanel();
  context.put("orgPanel",bean);
  bean.setModel(getModel());
  bean.setBRefVersion(false);
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.orgpanel.OrgChangeEventMediator getOrgChangeEventMediator(){
 if(context.get("orgChangeEventMediator")!=null)
 return (nc.ui.am.orgpanel.OrgChangeEventMediator)context.get("orgChangeEventMediator");
  nc.ui.am.orgpanel.OrgChangeEventMediator bean = new nc.ui.am.orgpanel.OrgChangeEventMediator();
  context.put("orgChangeEventMediator",bean);
  bean.setBillForm(getBillForm());
  bean.setModel(getModel());
  bean.setOrgChangeHandlers(getOrgChangeHandlers());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getOrgChangeHandlers(){
 if(context.get("orgChangeHandlers")!=null)
 return (java.util.ArrayList)context.get("orgChangeHandlers");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList3());  context.put("orgChangeHandlers",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList3(){  List list = new ArrayList();  list.add(getSetOrgToTemplateHandler_f92d94());  list.add(getBillCodeHandler_11f669d());  list.add(getChgTOAddNewHandler_89db54());  list.add(getBillFormEnableHandler_9e7d9d());  return list;}

private nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler getSetOrgToTemplateHandler_f92d94(){
 if(context.get("nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler#f92d94")!=null)
 return (nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler)context.get("nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler#f92d94");
  nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler bean = new nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler();
  context.put("nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler#f92d94",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.orgpanel.handler.BillCodeHandler getBillCodeHandler_11f669d(){
 if(context.get("nc.ui.am.orgpanel.handler.BillCodeHandler#11f669d")!=null)
 return (nc.ui.am.orgpanel.handler.BillCodeHandler)context.get("nc.ui.am.orgpanel.handler.BillCodeHandler#11f669d");
  nc.ui.am.orgpanel.handler.BillCodeHandler bean = new nc.ui.am.orgpanel.handler.BillCodeHandler();
  context.put("nc.ui.am.orgpanel.handler.BillCodeHandler#11f669d",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.orgpanel.handler.ChgTOAddNewHandler getChgTOAddNewHandler_89db54(){
 if(context.get("nc.ui.am.orgpanel.handler.ChgTOAddNewHandler#89db54")!=null)
 return (nc.ui.am.orgpanel.handler.ChgTOAddNewHandler)context.get("nc.ui.am.orgpanel.handler.ChgTOAddNewHandler#89db54");
  nc.ui.am.orgpanel.handler.ChgTOAddNewHandler bean = new nc.ui.am.orgpanel.handler.ChgTOAddNewHandler();
  context.put("nc.ui.am.orgpanel.handler.ChgTOAddNewHandler#89db54",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.orgpanel.handler.BillFormEnableHandler getBillFormEnableHandler_9e7d9d(){
 if(context.get("nc.ui.am.orgpanel.handler.BillFormEnableHandler#9e7d9d")!=null)
 return (nc.ui.am.orgpanel.handler.BillFormEnableHandler)context.get("nc.ui.am.orgpanel.handler.BillFormEnableHandler#9e7d9d");
  nc.ui.am.orgpanel.handler.BillFormEnableHandler bean = new nc.ui.am.orgpanel.handler.BillFormEnableHandler();
  context.put("nc.ui.am.orgpanel.handler.BillFormEnableHandler#9e7d9d",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.contributors.AMActionContributors getToftpanelActionContributors(){
 if(context.get("toftpanelActionContributors")!=null)
 return (nc.ui.am.action.contributors.AMActionContributors)context.get("toftpanelActionContributors");
  nc.ui.am.action.contributors.AMActionContributors bean = new nc.ui.am.action.contributors.AMActionContributors();
  context.put("toftpanelActionContributors",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
  bean.setContributors(getManagedList4());
  bean.setPfAddInfoRemoteCaller(getPfAddInfoRemoteCaller());
  bean.setBillTypeToActionCodeLst(getBillTypeToActionCodeLst());
  bean.initActions();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList4(){  List list = new ArrayList();  list.add(getCardActionsContainer());  list.add(getListActionsContainer());  return list;}

public java.util.ArrayList getBillTypeToActionCodeLst(){
 if(context.get("billTypeToActionCodeLst")!=null)
 return (java.util.ArrayList)context.get("billTypeToActionCodeLst");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList5());  context.put("billTypeToActionCodeLst",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add(getCommonBillTypeToActionCode_148ef8f());  return list;}

private nc.ui.am.action.contributors.CommonBillTypeToActionCode getCommonBillTypeToActionCode_148ef8f(){
 if(context.get("nc.ui.am.action.contributors.CommonBillTypeToActionCode#148ef8f")!=null)
 return (nc.ui.am.action.contributors.CommonBillTypeToActionCode)context.get("nc.ui.am.action.contributors.CommonBillTypeToActionCode#148ef8f");
  nc.ui.am.action.contributors.CommonBillTypeToActionCode bean = new nc.ui.am.action.contributors.CommonBillTypeToActionCode();
  context.put("nc.ui.am.action.contributors.CommonBillTypeToActionCode#148ef8f",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getListActionsContainer(){
 if(context.get("listActionsContainer")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("listActionsContainer");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBillListView());  context.put("listActionsContainer",bean);
  bean.setActions(getListViewActions());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getCardActionsContainer(){
 if(context.get("cardActionsContainer")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("cardActionsContainer");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBillForm());  context.put("cardActionsContainer",bean);
  bean.setActions(getCardViewActions());
  bean.setEditActions(getCardEditActions());
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getCardComponentExtStrategys(){
 if(context.get("cardComponentExtStrategys")!=null)
 return (java.util.ArrayList)context.get("cardComponentExtStrategys");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList6());  context.put("cardComponentExtStrategys",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add(getTransitypePanelExtStrategy_167c93c());  return list;}

private nc.ui.am.editor.ext.card.TransitypePanelExtStrategy getTransitypePanelExtStrategy_167c93c(){
 if(context.get("nc.ui.am.editor.ext.card.TransitypePanelExtStrategy#167c93c")!=null)
 return (nc.ui.am.editor.ext.card.TransitypePanelExtStrategy)context.get("nc.ui.am.editor.ext.card.TransitypePanelExtStrategy#167c93c");
  nc.ui.am.editor.ext.card.TransitypePanelExtStrategy bean = new nc.ui.am.editor.ext.card.TransitypePanelExtStrategy();
  context.put("nc.ui.am.editor.ext.card.TransitypePanelExtStrategy#167c93c",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getCardListenerConnectStrategys(){
 if(context.get("cardListenerConnectStrategys")!=null)
 return (java.util.ArrayList)context.get("cardListenerConnectStrategys");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList7());  context.put("cardListenerConnectStrategys",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList7(){  List list = new ArrayList();  list.add(getCardEditEventHandlerConnectStrategy());  list.add(getCardTabChangeEventHandlerConnectStrategy());  list.add(getKeyActionsListenerConnectStrategy());  list.add(getCardHyperlinkEventHandlerConnectStrategy());  return list;}

public java.util.ArrayList getListListenerConnectStrategys(){
 if(context.get("listListenerConnectStrategys")!=null)
 return (java.util.ArrayList)context.get("listListenerConnectStrategys");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList8());  context.put("listListenerConnectStrategys",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getListTabChangeEventHandlerConnectStrategy());  list.add(getListRowChangeEventHandlerConnectStrategy());  list.add(getListHyperlinkEventHandlerConnectStrategy());  return list;}

public nc.ui.am.editor.event.card.CardEditEventHandlerConnectStrategy getCardEditEventHandlerConnectStrategy(){
 if(context.get("cardEditEventHandlerConnectStrategy")!=null)
 return (nc.ui.am.editor.event.card.CardEditEventHandlerConnectStrategy)context.get("cardEditEventHandlerConnectStrategy");
  nc.ui.am.editor.event.card.CardEditEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.card.CardEditEventHandlerConnectStrategy();
  context.put("cardEditEventHandlerConnectStrategy",bean);
  bean.setBillForm(getBillForm());
  bean.setCardEditEventHandler(getCardEditEventHandler());
  bean.setCardEditEventHandlersExt(getCardEditEventHandlersExt());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.editor.event.card.CardTabChangeEventHandlerConnectStrategy getCardTabChangeEventHandlerConnectStrategy(){
 if(context.get("cardTabChangeEventHandlerConnectStrategy")!=null)
 return (nc.ui.am.editor.event.card.CardTabChangeEventHandlerConnectStrategy)context.get("cardTabChangeEventHandlerConnectStrategy");
  nc.ui.am.editor.event.card.CardTabChangeEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.card.CardTabChangeEventHandlerConnectStrategy();
  context.put("cardTabChangeEventHandlerConnectStrategy",bean);
  bean.setBillForm(getBillForm());
  bean.setTabChangeEventHandlers(getCardTabChangeEventHandlers());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.editor.event.card.KeyActionsListenerConnectStrategy getKeyActionsListenerConnectStrategy(){
 if(context.get("keyActionsListenerConnectStrategy")!=null)
 return (nc.ui.am.editor.event.card.KeyActionsListenerConnectStrategy)context.get("keyActionsListenerConnectStrategy");
  nc.ui.am.editor.event.card.KeyActionsListenerConnectStrategy bean = new nc.ui.am.editor.event.card.KeyActionsListenerConnectStrategy();
  context.put("keyActionsListenerConnectStrategy",bean);
  bean.setBillForm(getBillForm());
  bean.setKeyListeners(getKeyEventHandlers());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.editor.event.list.ListTabChangeEventHandlerConnectStrategy getListTabChangeEventHandlerConnectStrategy(){
 if(context.get("listTabChangeEventHandlerConnectStrategy")!=null)
 return (nc.ui.am.editor.event.list.ListTabChangeEventHandlerConnectStrategy)context.get("listTabChangeEventHandlerConnectStrategy");
  nc.ui.am.editor.event.list.ListTabChangeEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.list.ListTabChangeEventHandlerConnectStrategy();
  context.put("listTabChangeEventHandlerConnectStrategy",bean);
  bean.setBillListView(getBillListView());
  bean.setTabChangeEventHandlers(getListTabChangeEventHandlers());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.editor.event.list.ListRowChangeEventHanlderConnectStrategy getListRowChangeEventHandlerConnectStrategy(){
 if(context.get("listRowChangeEventHandlerConnectStrategy")!=null)
 return (nc.ui.am.editor.event.list.ListRowChangeEventHanlderConnectStrategy)context.get("listRowChangeEventHandlerConnectStrategy");
  nc.ui.am.editor.event.list.ListRowChangeEventHanlderConnectStrategy bean = new nc.ui.am.editor.event.list.ListRowChangeEventHanlderConnectStrategy();
  context.put("listRowChangeEventHandlerConnectStrategy",bean);
  bean.setBillListView(getBillListView());
  bean.setListRowChangeEventHandlers(getListRowChangeEventHandlers());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getCardTabChangeEventHandlers(){
 if(context.get("cardTabChangeEventHandlers")!=null)
 return (java.util.ArrayList)context.get("cardTabChangeEventHandlers");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList9());  context.put("cardTabChangeEventHandlers",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList9(){  List list = new ArrayList();  list.add(getDefaultCardTabChangeEventHandler_cc8e25());  return list;}

private nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler getDefaultCardTabChangeEventHandler_cc8e25(){
 if(context.get("nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler#cc8e25")!=null)
 return (nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler)context.get("nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler#cc8e25");
  nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler bean = new nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler();
  context.put("nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler#cc8e25",bean);
  bean.setModelDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getListTabChangeEventHandlers(){
 if(context.get("listTabChangeEventHandlers")!=null)
 return (java.util.ArrayList)context.get("listTabChangeEventHandlers");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList10());  context.put("listTabChangeEventHandlers",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList10(){  List list = new ArrayList();  list.add(getDefaultListTabChangeEventHandler_1882eef());  return list;}

private nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler getDefaultListTabChangeEventHandler_1882eef(){
 if(context.get("nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler#1882eef")!=null)
 return (nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler)context.get("nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler#1882eef");
  nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler bean = new nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler();
  context.put("nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler#1882eef",bean);
  bean.setModelDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getListRowChangeEventHandlers(){
 if(context.get("listRowChangeEventHandlers")!=null)
 return (java.util.ArrayList)context.get("listRowChangeEventHandlers");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList11());  context.put("listRowChangeEventHandlers",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList11(){  List list = new ArrayList();  list.add(getDefaultListRowChangeEventHandler_1c150c5());  return list;}

private nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler getDefaultListRowChangeEventHandler_1c150c5(){
 if(context.get("nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler#1c150c5")!=null)
 return (nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler)context.get("nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler#1c150c5");
  nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler bean = new nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler();
  context.put("nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler#1c150c5",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getKeyEventHandlers(){
 if(context.get("keyEventHandlers")!=null)
 return (java.util.ArrayList)context.get("keyEventHandlers");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList12());  context.put("keyEventHandlers",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList12(){  List list = new ArrayList();  list.add(getKeysAction4AddLineEventHandler_9df603());  return list;}

private nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler getKeysAction4AddLineEventHandler_9df603(){
 if(context.get("nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler#9df603")!=null)
 return (nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler)context.get("nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler#9df603");
  nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler bean = new nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler();
  context.put("nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler#9df603",bean);
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmtime.view.PMTimeCardEditHandler getCardEditEventHandler(){
 if(context.get("cardEditEventHandler")!=null)
 return (nc.ui.emm.pmtime.view.PMTimeCardEditHandler)context.get("cardEditEventHandler");
  nc.ui.emm.pmtime.view.PMTimeCardEditHandler bean = new nc.ui.emm.pmtime.view.PMTimeCardEditHandler();
  context.put("cardEditEventHandler",bean);
  bean.setInitRef(getPMTimeInitRef());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getCardEditEventHandlersExt(){
 if(context.get("cardEditEventHandlersExt")!=null)
 return (java.util.ArrayList)context.get("cardEditEventHandlersExt");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList13());  context.put("cardEditEventHandlersExt",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList13(){  List list = new ArrayList();  list.add(getUserDefItemCardEditEventHandler_824ea9());  return list;}

private nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler getUserDefItemCardEditEventHandler_824ea9(){
 if(context.get("nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler#824ea9")!=null)
 return (nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler)context.get("nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler#824ea9");
  nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler bean = new nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler();
  context.put("nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler#824ea9",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.editor.event.card.CardHyperlinkEventHandlerConnectStrategy getCardHyperlinkEventHandlerConnectStrategy(){
 if(context.get("cardHyperlinkEventHandlerConnectStrategy")!=null)
 return (nc.ui.am.editor.event.card.CardHyperlinkEventHandlerConnectStrategy)context.get("cardHyperlinkEventHandlerConnectStrategy");
  nc.ui.am.editor.event.card.CardHyperlinkEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.card.CardHyperlinkEventHandlerConnectStrategy();
  context.put("cardHyperlinkEventHandlerConnectStrategy",bean);
  bean.setHyperlinkEventHandlers(getCardHyperlinkEventHandlers());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getCardHyperlinkEventHandlers(){
 if(context.get("cardHyperlinkEventHandlers")!=null)
 return (java.util.ArrayList)context.get("cardHyperlinkEventHandlers");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList14());  context.put("cardHyperlinkEventHandlers",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList14(){  List list = new ArrayList();  list.add(getQueryAboutHyperlinkEventHandler());  return list;}

public nc.ui.am.editor.event.list.ListHyperlinkEventHandlerConnectStrategy getListHyperlinkEventHandlerConnectStrategy(){
 if(context.get("listHyperlinkEventHandlerConnectStrategy")!=null)
 return (nc.ui.am.editor.event.list.ListHyperlinkEventHandlerConnectStrategy)context.get("listHyperlinkEventHandlerConnectStrategy");
  nc.ui.am.editor.event.list.ListHyperlinkEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.list.ListHyperlinkEventHandlerConnectStrategy();
  context.put("listHyperlinkEventHandlerConnectStrategy",bean);
  bean.setHyperlinkEventHandlers(getListHyperlinkEventHandlers());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getListHyperlinkEventHandlers(){
 if(context.get("listHyperlinkEventHandlers")!=null)
 return (java.util.ArrayList)context.get("listHyperlinkEventHandlers");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList15());  context.put("listHyperlinkEventHandlers",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList15(){  List list = new ArrayList();  list.add(getQueryAboutHyperlinkEventHandler());  return list;}

public nc.ui.am.editor.event.handlers.support.QueryAboutHyperlinkEventHandler getQueryAboutHyperlinkEventHandler(){
 if(context.get("queryAboutHyperlinkEventHandler")!=null)
 return (nc.ui.am.editor.event.handlers.support.QueryAboutHyperlinkEventHandler)context.get("queryAboutHyperlinkEventHandler");
  nc.ui.am.editor.event.handlers.support.QueryAboutHyperlinkEventHandler bean = new nc.ui.am.editor.event.handlers.support.QueryAboutHyperlinkEventHandler();
  context.put("queryAboutHyperlinkEventHandler",bean);
  bean.setFuncNode2MD(getManagedList16());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList16(){  List list = new ArrayList();  list.add(getCommonFuncNodeToMD_fa43c3());  list.add(getEAMFuncNodeToMD_76f43b());  return list;}

private nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD getCommonFuncNodeToMD_fa43c3(){
 if(context.get("nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD#fa43c3")!=null)
 return (nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD)context.get("nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD#fa43c3");
  nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD bean = new nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD();
  context.put("nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD#fa43c3",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD getEAMFuncNodeToMD_76f43b(){
 if(context.get("nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD#76f43b")!=null)
 return (nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD)context.get("nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD#76f43b");
  nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD bean = new nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD();
  context.put("nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD#76f43b",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.TemplateContainer getTemplateContainer(){
 if(context.get("templateContainer")!=null)
 return (nc.ui.uif2.editor.TemplateContainer)context.get("templateContainer");
  nc.ui.uif2.editor.TemplateContainer bean = new nc.ui.uif2.editor.TemplateContainer();
  context.put("templateContainer",bean);
  bean.setContext(getContext());
  bean.load();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getBillDataPreparator(){
 if(context.get("billDataPreparator")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare)context.get("billDataPreparator");
  nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
  context.put("billDataPreparator",bean);
  bean.setBillDataPrepares(getManagedList17());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList17(){  List list = new ArrayList();  list.add(getHyperlinkBillDataPreparator());  list.add(getDefItemCardPreparator());  return list;}

public nc.ui.am.editor.prepare.HyperlinkPreparator getHyperlinkBillDataPreparator(){
 if(context.get("hyperlinkBillDataPreparator")!=null)
 return (nc.ui.am.editor.prepare.HyperlinkPreparator)context.get("hyperlinkBillDataPreparator");
  nc.ui.am.editor.prepare.HyperlinkPreparator bean = new nc.ui.am.editor.prepare.HyperlinkPreparator();
  context.put("hyperlinkBillDataPreparator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer(){
 if(context.get("userdefitemContainer")!=null)
 return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
  nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
  context.put("userdefitemContainer",bean);
  bean.setContext(getContext());
  bean.setParams(getManagedList18());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList18(){  List list = new ArrayList();  list.add(getQueryParam_11d2776());  list.add(getQueryParam_109e1af());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_11d2776(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#11d2776")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#11d2776");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#11d2776",bean);
  bean.setMdfullname("emm.PMHeadVO");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_109e1af(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#109e1af")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#109e1af");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#109e1af",bean);
  bean.setMdfullname("emm.PMWorkObjVO");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UserdefitemContainerPreparator getDefItemCardPreparator(){
 if(context.get("defItemCardPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("defItemCardPreparator");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("defItemCardPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList19());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList19(){  List list = new ArrayList();  list.add(getUserdefQueryParam_1bfaccb());  list.add(getUserdefQueryParam_e978a3());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1bfaccb(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#1bfaccb")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1bfaccb");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#1bfaccb",bean);
  bean.setMdfullname("emm.PMHeadVO");
  bean.setPos(0);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_e978a3(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#e978a3")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#e978a3");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#e978a3",bean);
  bean.setMdfullname("emm.PMWorkObjVO");
  bean.setPos(1);
  bean.setTabcode("pm_workobj");
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UserdefitemContainerListPreparator getDefItemListPreparator(){
 if(context.get("defItemListPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("defItemListPreparator");
  nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
  context.put("defItemListPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList20());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList20(){  List list = new ArrayList();  list.add(getUserdefQueryParam_1ef42ef());  list.add(getUserdefQueryParam_1137dce());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1ef42ef(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#1ef42ef")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1ef42ef");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#1ef42ef",bean);
  bean.setMdfullname("emm.PMHeadVO");
  bean.setPos(0);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1137dce(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#1137dce")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1137dce");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#1137dce",bean);
  bean.setMdfullname("emm.PMWorkObjVO");
  bean.setPos(1);
  bean.setTabcode("pm_workobj");
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.pagination.PaginationBar getPaginationBar(){
 if(context.get("paginationBar")!=null)
 return (nc.ui.uif2.components.pagination.PaginationBar)context.get("paginationBar");
  nc.ui.uif2.components.pagination.PaginationBar bean = new nc.ui.uif2.components.pagination.PaginationBar(2);  context.put("paginationBar",bean);
  bean.setPaginationModel(getPaginationModel());
  bean.setContext(getContext());
  bean.registeCallbak();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.pagination.PaginationModel getPaginationModel(){
 if(context.get("paginationModel")!=null)
 return (nc.ui.uif2.components.pagination.PaginationModel)context.get("paginationModel");
  nc.ui.uif2.components.pagination.PaginationModel bean = new nc.ui.uif2.components.pagination.PaginationModel();
  context.put("paginationModel",bean);
  bean.setPaginationQueryService(getModelService());
  bean.init();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.model.BillManageModel getModel(){
 if(context.get("model")!=null)
 return (nc.ui.am.model.BillManageModel)context.get("model");
  nc.ui.am.model.BillManageModel bean = new nc.ui.am.model.BillManageModel(getContext());  context.put("model",bean);
  bean.setService(getModelService());
  bean.setBusinessObjectAdapterFactory(getVoBdAdapterFactory());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.model.PMDataModelDelegator getModelDataManager(){
 if(context.get("modelDataManager")!=null)
 return (nc.ui.emm.pmbase.model.PMDataModelDelegator)context.get("modelDataManager");
  nc.ui.emm.pmbase.model.PMDataModelDelegator bean = new nc.ui.emm.pmbase.model.PMDataModelDelegator();
  context.put("modelDataManager",bean);
  bean.setModel(getModel());
  bean.setQueryDialogManager(getQueryDialogManager());
  bean.setPaginationModel(getPaginationModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.view.PMQueryDlgManager getQueryDialogManager(){
 if(context.get("queryDialogManager")!=null)
 return (nc.ui.emm.pmbase.view.PMQueryDlgManager)context.get("queryDialogManager");
  nc.ui.emm.pmbase.view.PMQueryDlgManager bean = new nc.ui.emm.pmbase.view.PMQueryDlgManager();
  context.put("queryDialogManager",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.base.manager.FuncNodeInitDataAdapter getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.am.base.manager.FuncNodeInitDataAdapter)context.get("InitDataListener");
  nc.ui.am.base.manager.FuncNodeInitDataAdapter bean = new nc.ui.am.base.manager.FuncNodeInitDataAdapter();
  context.put("InitDataListener",bean);
  bean.setModelDataManager(getModelDataManager());
  bean.setQueryAction(getQueryAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.FunNodeClosingHandler getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.uif2.FunNodeClosingHandler)context.get("ClosingListener");
  nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
  context.put("ClosingListener",bean);
  bean.setModel(getModel());
  bean.setSaveaction(getSaveAction());
  bean.setCancelaction(getCancelAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.VOBDObjectFactory getVoBdAdapterFactory(){
 if(context.get("voBdAdapterFactory")!=null)
 return (nc.vo.bd.meta.VOBDObjectFactory)context.get("voBdAdapterFactory");
  nc.vo.bd.meta.VOBDObjectFactory bean = new nc.vo.bd.meta.VOBDObjectFactory();
  context.put("voBdAdapterFactory",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.am.scale.process.ScaleProcessCenter getBillCardDigitProcessor(){
 if(context.get("billCardDigitProcessor")!=null)
 return (nc.vo.am.scale.process.ScaleProcessCenter)context.get("billCardDigitProcessor");
  nc.vo.am.scale.process.ScaleProcessCenter bean = new nc.vo.am.scale.process.ScaleProcessCenter();
  context.put("billCardDigitProcessor",bean);
  bean.setScaleObjectData(getScaleObjectData());
  bean.setScaleProcessor(getCardScaleSet());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.scale.process.ScaleProcessorCardPanel getCardScaleSet(){
 if(context.get("cardScaleSet")!=null)
 return (nc.ui.am.scale.process.ScaleProcessorCardPanel)context.get("cardScaleSet");
  nc.ui.am.scale.process.ScaleProcessorCardPanel bean = new nc.ui.am.scale.process.ScaleProcessorCardPanel();
  context.put("cardScaleSet",bean);
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.am.scale.process.ScaleProcessCenter getBillListDigitProcessor(){
 if(context.get("billListDigitProcessor")!=null)
 return (nc.vo.am.scale.process.ScaleProcessCenter)context.get("billListDigitProcessor");
  nc.vo.am.scale.process.ScaleProcessCenter bean = new nc.vo.am.scale.process.ScaleProcessCenter();
  context.put("billListDigitProcessor",bean);
  bean.setScaleObjectData(getScaleObjectData());
  bean.setScaleProcessor(getListScaleSet());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.scale.process.ScaleProcessListPanel getListScaleSet(){
 if(context.get("listScaleSet")!=null)
 return (nc.ui.am.scale.process.ScaleProcessListPanel)context.get("listScaleSet");
  nc.ui.am.scale.process.ScaleProcessListPanel bean = new nc.ui.am.scale.process.ScaleProcessListPanel();
  context.put("listScaleSet",bean);
  bean.setListView(getBillListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.am.scale.process.ScaleObjectData getScaleObjectData(){
 if(context.get("scaleObjectData")!=null)
 return (nc.vo.am.scale.process.ScaleObjectData)context.get("scaleObjectData");
  nc.vo.am.scale.process.ScaleObjectData bean = new nc.vo.am.scale.process.ScaleObjectData();
  context.put("scaleObjectData",bean);
  bean.setDigitMatcher(getDigitMatcher());
  bean.setScaleObjLoader(getSetScaleFields());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getDigitMatcher(){
 if(context.get("digitMatcher")!=null)
 return (java.util.ArrayList)context.get("digitMatcher");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList21());  context.put("digitMatcher",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList21(){  List list = new ArrayList();  list.add(getScaleTypeVO_19c623e());  return list;}

private nc.vo.am.scale.process.ScaleTypeVO getScaleTypeVO_19c623e(){
 if(context.get("nc.vo.am.scale.process.ScaleTypeVO#19c623e")!=null)
 return (nc.vo.am.scale.process.ScaleTypeVO)context.get("nc.vo.am.scale.process.ScaleTypeVO#19c623e");
  nc.vo.am.scale.process.ScaleTypeVO bean = new nc.vo.am.scale.process.ScaleTypeVO();
  context.put("nc.vo.am.scale.process.ScaleTypeVO#19c623e",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.scale.process.DefaultSetScaleFields getSetScaleFields(){
 if(context.get("setScaleFields")!=null)
 return (nc.ui.am.scale.process.DefaultSetScaleFields)context.get("setScaleFields");
  nc.ui.am.scale.process.DefaultSetScaleFields bean = new nc.ui.am.scale.process.DefaultSetScaleFields();
  context.put("setScaleFields",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.code.BillCodeMediator getBillCodeMediator(){
 if(context.get("billCodeMediator")!=null)
 return (nc.ui.am.code.BillCodeMediator)context.get("billCodeMediator");
  nc.ui.am.code.BillCodeMediator bean = new nc.ui.am.code.BillCodeMediator();
  context.put("billCodeMediator",bean);
  bean.setBillCodeKey("bill_code");
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getTableCodes(){
 if(context.get("tableCodes")!=null)
 return (java.util.ArrayList)context.get("tableCodes");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList22());  context.put("tableCodes",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList22(){  List list = new ArrayList();  list.add("ewm_wo");  return list;}

public java.util.HashMap getBodyActionsMap(){
 if(context.get("bodyActionsMap")!=null)
 return (java.util.HashMap)context.get("bodyActionsMap");
  java.util.HashMap bean = new java.util.HashMap(getManagedMap0());  context.put("bodyActionsMap",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("pm_stdjob",getManagedList23());  map.put("ewm_wo",getManagedList24());  map.put("pm_result",getManagedList25());  map.put("pm_workobj",getManagedList26());  return map;}

private List getManagedList23(){  List list = new ArrayList();  list.add(getAddLineAction());  list.add(getInsertLineAction());  list.add(getDeleteLineAction());  list.add(getTableEditLineAction());  list.add(getActionsBar_ActionsBarSeparator_1b60c5());  list.add(getBodyViewMaxAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1b60c5(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b60c5")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b60c5");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b60c5",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList24(){  List list = new ArrayList();  list.add(getActionsBar_ActionsBarSeparator_16e28a4());  list.add(getBodyViewMaxAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_16e28a4(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#16e28a4")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#16e28a4");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#16e28a4",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList25(){  List list = new ArrayList();  list.add(getAddLineAction());  list.add(getInsertLineAction());  list.add(getDeleteLineAction());  list.add(getTableEditLineAction());  list.add(getActionsBar_ActionsBarSeparator_12e6023());  list.add(getBodyViewMaxAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_12e6023(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#12e6023")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#12e6023");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#12e6023",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList26(){  List list = new ArrayList();  list.add(getWorkObjAddLineAction());  list.add(getWorkObjInsertLineAction());  list.add(getDeleteLineAction());  list.add(getTableEditLineAction());  list.add(getActionsBar_ActionsBarSeparator_1a9aef4());  list.add(getBodyViewMaxAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1a9aef4(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1a9aef4")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1a9aef4");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1a9aef4",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.model.PMModelService getModelService(){
 if(context.get("modelService")!=null)
 return (nc.ui.emm.pmbase.model.PMModelService)context.get("modelService");
  nc.ui.emm.pmbase.model.PMModelService bean = new nc.ui.emm.pmbase.model.PMModelService();
  context.put("modelService",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.SeparatorAction getSeparatorAction(){
 if(context.get("separatorAction")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("separatorAction");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("separatorAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMAddAction getAddAction(){
 if(context.get("addAction")!=null)
 return (nc.ui.am.action.support.AMAddAction)context.get("addAction");
  nc.ui.am.action.support.AMAddAction bean = new nc.ui.am.action.support.AMAddAction();
  context.put("addAction",bean);
  bean.setBillForm(getBillForm());
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMEditAction getEditAction(){
 if(context.get("editAction")!=null)
 return (nc.ui.emm.pmbase.action.PMEditAction)context.get("editAction");
  nc.ui.emm.pmbase.action.PMEditAction bean = new nc.ui.emm.pmbase.action.PMEditAction();
  context.put("editAction",bean);
  bean.setBillForm(getBillForm());
  bean.setModel(getModel());
  bean.setValidators(getManagedList27());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList27(){  List list = new ArrayList();  list.add(getUIPermissionValidator_11f6ae());  return list;}

private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_11f6ae(){
 if(context.get("nc.ui.am.validator.UIPermissionValidator#11f6ae")!=null)
 return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#11f6ae");
  nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
  context.put("nc.ui.am.validator.UIPermissionValidator#11f6ae",bean);
  bean.setContext(getContext());
  bean.setMeta_code("Edit");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMBatchAlterAction getBatchAlterAction(){
 if(context.get("batchAlterAction")!=null)
 return (nc.ui.am.action.support.AMBatchAlterAction)context.get("batchAlterAction");
  nc.ui.am.action.support.AMBatchAlterAction bean = new nc.ui.am.action.support.AMBatchAlterAction();
  context.put("batchAlterAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
  bean.setFilter(getFilter());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.common.DefaultBatchAlterFilter getFilter(){
 if(context.get("filter")!=null)
 return (nc.ui.am.common.DefaultBatchAlterFilter)context.get("filter");
  nc.ui.am.common.DefaultBatchAlterFilter bean = new nc.ui.am.common.DefaultBatchAlterFilter();
  context.put("filter",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMAddLineAction getAddLineAction(){
 if(context.get("addLineAction")!=null)
 return (nc.ui.am.action.support.AMAddLineAction)context.get("addLineAction");
  nc.ui.am.action.support.AMAddLineAction bean = new nc.ui.am.action.support.AMAddLineAction();
  context.put("addLineAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMDeleteLineAction getDeleteLineAction(){
 if(context.get("deleteLineAction")!=null)
 return (nc.ui.am.action.support.AMDeleteLineAction)context.get("deleteLineAction");
  nc.ui.am.action.support.AMDeleteLineAction bean = new nc.ui.am.action.support.AMDeleteLineAction();
  context.put("deleteLineAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMInsertLineAction getInsertLineAction(){
 if(context.get("insertLineAction")!=null)
 return (nc.ui.am.action.support.AMInsertLineAction)context.get("insertLineAction");
  nc.ui.am.action.support.AMInsertLineAction bean = new nc.ui.am.action.support.AMInsertLineAction();
  context.put("insertLineAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMTableEditLineAction getTableEditLineAction(){
 if(context.get("tableEditLineAction")!=null)
 return (nc.ui.am.action.support.AMTableEditLineAction)context.get("tableEditLineAction");
  nc.ui.am.action.support.AMTableEditLineAction bean = new nc.ui.am.action.support.AMTableEditLineAction();
  context.put("tableEditLineAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMCancelAction getCancelAction(){
 if(context.get("cancelAction")!=null)
 return (nc.ui.emm.pmbase.action.PMCancelAction)context.get("cancelAction");
  nc.ui.emm.pmbase.action.PMCancelAction bean = new nc.ui.emm.pmbase.action.PMCancelAction();
  context.put("cancelAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
  bean.setCodeField("bill_code");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMSaveAction getSaveAction(){
 if(context.get("saveAction")!=null)
 return (nc.ui.emm.pmbase.action.PMSaveAction)context.get("saveAction");
  nc.ui.emm.pmbase.action.PMSaveAction bean = new nc.ui.emm.pmbase.action.PMSaveAction();
  context.put("saveAction",bean);
  bean.setBillForm(getBillForm());
  bean.setModel(getModel());
  bean.setValidators(getManagedList28());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList28(){  List list = new ArrayList();  list.add(getDataNotValidator());  list.add(getCheckEquipValidator());  list.add(getCheckPeriodsValidator());  list.add(getCheckFreValidator());  list.add(getCheckWorkObjValidator());  return list;}

public nc.ui.am.action.support.AMSaveAddAction getSaveAddAction(){
 if(context.get("saveAddAction")!=null)
 return (nc.ui.am.action.support.AMSaveAddAction)context.get("saveAddAction");
  nc.ui.am.action.support.AMSaveAddAction bean = new nc.ui.am.action.support.AMSaveAddAction();
  context.put("saveAddAction",bean);
  bean.setModel(getModel());
  bean.setSaveAction(getSaveAction());
  bean.setAddAction(getAddAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMSaveCommitAction getSaveCommitAction(){
 if(context.get("saveCommitAction")!=null)
 return (nc.ui.am.action.support.AMSaveCommitAction)context.get("saveCommitAction");
  nc.ui.am.action.support.AMSaveCommitAction bean = new nc.ui.am.action.support.AMSaveCommitAction();
  context.put("saveCommitAction",bean);
  bean.setBillForm(getBillForm());
  bean.setModel(getModel());
  bean.setSaveAction(getSaveAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMDeleteAction getDeleteAction(){
 if(context.get("deleteAction")!=null)
 return (nc.ui.emm.pmbase.action.PMDeleteAction)context.get("deleteAction");
  nc.ui.emm.pmbase.action.PMDeleteAction bean = new nc.ui.emm.pmbase.action.PMDeleteAction();
  context.put("deleteAction",bean);
  bean.setModel(getModel());
  bean.setValidators(getManagedList29());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList29(){  List list = new ArrayList();  list.add(getUIPermissionValidator_15a4ee2());  return list;}

private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_15a4ee2(){
 if(context.get("nc.ui.am.validator.UIPermissionValidator#15a4ee2")!=null)
 return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#15a4ee2");
  nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
  context.put("nc.ui.am.validator.UIPermissionValidator#15a4ee2",bean);
  bean.setContext(getContext());
  bean.setMeta_code("Delete");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMBillViewMaxAction getHeadViewMaxAction(){
 if(context.get("headViewMaxAction")!=null)
 return (nc.ui.am.action.support.AMBillViewMaxAction)context.get("headViewMaxAction");
  nc.ui.am.action.support.AMBillViewMaxAction bean = new nc.ui.am.action.support.AMBillViewMaxAction();
  context.put("headViewMaxAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
  bean.setPos(0);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMBillViewMaxAction getBodyViewMaxAction(){
 if(context.get("bodyViewMaxAction")!=null)
 return (nc.ui.am.action.support.AMBillViewMaxAction)context.get("bodyViewMaxAction");
  nc.ui.am.action.support.AMBillViewMaxAction bean = new nc.ui.am.action.support.AMBillViewMaxAction();
  context.put("bodyViewMaxAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
  bean.setPos(1);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMSearchAction getQueryAction(){
 if(context.get("queryAction")!=null)
 return (nc.ui.am.action.support.AMSearchAction)context.get("queryAction");
  nc.ui.am.action.support.AMSearchAction bean = new nc.ui.am.action.support.AMSearchAction();
  context.put("queryAction",bean);
  bean.setModel(getModel());
  bean.setDataManager(getModelDataManager());
  bean.setTemplateContainer(getQueryTemplateContainer());
  bean.setQueryDelegator(getAMDefaultQueryDelegator_13a7a94());
  bean.setOrgType(getMainorgType());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.action.query.AMDefaultQueryDelegator getAMDefaultQueryDelegator_13a7a94(){
 if(context.get("nc.ui.am.action.query.AMDefaultQueryDelegator#13a7a94")!=null)
 return (nc.ui.am.action.query.AMDefaultQueryDelegator)context.get("nc.ui.am.action.query.AMDefaultQueryDelegator#13a7a94");
  nc.ui.am.action.query.AMDefaultQueryDelegator bean = new nc.ui.am.action.query.AMDefaultQueryDelegator();
  context.put("nc.ui.am.action.query.AMDefaultQueryDelegator#13a7a94",bean);
  bean.setContext(getContext());
  bean.setBaseQueryManager(getQueryDialogManager());
  bean.setAutoShowUpComponent(getBillListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.lang.String getMainorgType(){
 if(context.get("mainorgType")!=null)
 return (java.lang.String)context.get("mainorgType");
  nc.ui.am.config.VarString bean = new nc.ui.am.config.VarString();
    context.put("&mainorgType",bean);  bean.setValueStr("");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("mainorgType",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.am.action.support.AMRefreshAllAction getRefreshAllAction(){
 if(context.get("refreshAllAction")!=null)
 return (nc.ui.am.action.support.AMRefreshAllAction)context.get("refreshAllAction");
  nc.ui.am.action.support.AMRefreshAllAction bean = new nc.ui.am.action.support.AMRefreshAllAction();
  context.put("refreshAllAction",bean);
  bean.setModel(getModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMRefreshAction getRefreshAction(){
 if(context.get("refreshAction")!=null)
 return (nc.ui.emm.pmbase.action.PMRefreshAction)context.get("refreshAction");
  nc.ui.emm.pmbase.action.PMRefreshAction bean = new nc.ui.emm.pmbase.action.PMRefreshAction();
  context.put("refreshAction",bean);
  bean.setModel(getModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getApproveActionGroup(){
 if(context.get("approveActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("approveActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("approveActionGroup",bean);
  bean.setActions(getManagedList30());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList30(){  List list = new ArrayList();  list.add(getApproveAction());  list.add(getUnApproveAction());  list.add(getSeparatorAction());  list.add(getQueryApproveflowAction());  return list;}

public nc.funcnode.ui.action.GroupAction getCommitGroupAction(){
 if(context.get("commitGroupAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("commitGroupAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("commitGroupAction",bean);
  bean.setActions(getManagedList31());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList31(){  List list = new ArrayList();  list.add(getCommitAction());  list.add(getUnCommitAction());  return list;}

public nc.ui.am.action.support.AMCommitAction getCommitAction(){
 if(context.get("commitAction")!=null)
 return (nc.ui.am.action.support.AMCommitAction)context.get("commitAction");
  nc.ui.am.action.support.AMCommitAction bean = new nc.ui.am.action.support.AMCommitAction();
  context.put("commitAction",bean);
  bean.setBillForm(getBillForm());
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMUnCommitAction getUnCommitAction(){
 if(context.get("unCommitAction")!=null)
 return (nc.ui.am.action.support.AMUnCommitAction)context.get("unCommitAction");
  nc.ui.am.action.support.AMUnCommitAction bean = new nc.ui.am.action.support.AMUnCommitAction();
  context.put("unCommitAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMApproveAction getApproveAction(){
 if(context.get("approveAction")!=null)
 return (nc.ui.am.action.support.AMApproveAction)context.get("approveAction");
  nc.ui.am.action.support.AMApproveAction bean = new nc.ui.am.action.support.AMApproveAction();
  context.put("approveAction",bean);
  bean.setBillForm(getBillForm());
  bean.setModel(getModel());
  bean.setValidators(getManagedList32());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList32(){  List list = new ArrayList();  list.add(getApproveBillDateValidator_459eaa());  list.add(getUIPermissionValidator_b3e47b());  return list;}

private nc.ui.am.validator.ApproveBillDateValidator getApproveBillDateValidator_459eaa(){
 if(context.get("nc.ui.am.validator.ApproveBillDateValidator#459eaa")!=null)
 return (nc.ui.am.validator.ApproveBillDateValidator)context.get("nc.ui.am.validator.ApproveBillDateValidator#459eaa");
  nc.ui.am.validator.ApproveBillDateValidator bean = new nc.ui.am.validator.ApproveBillDateValidator();
  context.put("nc.ui.am.validator.ApproveBillDateValidator#459eaa",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_b3e47b(){
 if(context.get("nc.ui.am.validator.UIPermissionValidator#b3e47b")!=null)
 return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#b3e47b");
  nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
  context.put("nc.ui.am.validator.UIPermissionValidator#b3e47b",bean);
  bean.setContext(getContext());
  bean.setMeta_code("AMApprove");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMUnApproveAction getUnApproveAction(){
 if(context.get("unApproveAction")!=null)
 return (nc.ui.am.action.support.AMUnApproveAction)context.get("unApproveAction");
  nc.ui.am.action.support.AMUnApproveAction bean = new nc.ui.am.action.support.AMUnApproveAction();
  context.put("unApproveAction",bean);
  bean.setModel(getModel());
  bean.setValidators(getManagedList33());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList33(){  List list = new ArrayList();  list.add(getUIPermissionValidator_5d7df7());  return list;}

private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_5d7df7(){
 if(context.get("nc.ui.am.validator.UIPermissionValidator#5d7df7")!=null)
 return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#5d7df7");
  nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
  context.put("nc.ui.am.validator.UIPermissionValidator#5d7df7",bean);
  bean.setContext(getContext());
  bean.setMeta_code("AMUnApprove");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.container.AssMenuAction getAssMenuAction(){
 if(context.get("assMenuAction")!=null)
 return (nc.ui.am.action.container.AssMenuAction)context.get("assMenuAction");
  nc.ui.am.action.container.AssMenuAction bean = new nc.ui.am.action.container.AssMenuAction();
  context.put("assMenuAction",bean);
  bean.setActions(getManagedList34());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList34(){  List list = new ArrayList();  list.add(getAttachmentAction());  return list;}

public nc.ui.am.action.support.AMAttachmentAction getAttachmentAction(){
 if(context.get("attachmentAction")!=null)
 return (nc.ui.am.action.support.AMAttachmentAction)context.get("attachmentAction");
  nc.ui.am.action.support.AMAttachmentAction bean = new nc.ui.am.action.support.AMAttachmentAction();
  context.put("attachmentAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.FirstLineAction getFirstLineAction(){
 if(context.get("firstLineAction")!=null)
 return (nc.ui.uif2.actions.FirstLineAction)context.get("firstLineAction");
  nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
  context.put("firstLineAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.PreLineAction getPreLineAction(){
 if(context.get("preLineAction")!=null)
 return (nc.ui.uif2.actions.PreLineAction)context.get("preLineAction");
  nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
  context.put("preLineAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.NextLineAction getNextLineAction(){
 if(context.get("nextLineAction")!=null)
 return (nc.ui.uif2.actions.NextLineAction)context.get("nextLineAction");
  nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
  context.put("nextLineAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.LastLineAction getLastLineAction(){
 if(context.get("lastLineAction")!=null)
 return (nc.ui.uif2.actions.LastLineAction)context.get("lastLineAction");
  nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
  context.put("lastLineAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintMenuAction(){
 if(context.get("printMenuAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printMenuAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printMenuAction",bean);
  bean.setActions(getManagedList35());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList35(){  List list = new ArrayList();  list.add(getPrintTemplateAction());  list.add(getTempPrinviewAction());  list.add(getOutputAction());  return list;}

public nc.ui.am.action.support.AMPrintTempPrinviewAction getTempPrinviewAction(){
 if(context.get("tempPrinviewAction")!=null)
 return (nc.ui.am.action.support.AMPrintTempPrinviewAction)context.get("tempPrinviewAction");
  nc.ui.am.action.support.AMPrintTempPrinviewAction bean = new nc.ui.am.action.support.AMPrintTempPrinviewAction();
  context.put("tempPrinviewAction",bean);
  bean.setModel(getModel());
  bean.setScaleProcessor(getBillVODigitProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMPrintTemplateAction getPrintTemplateAction(){
 if(context.get("printTemplateAction")!=null)
 return (nc.ui.am.action.support.AMPrintTemplateAction)context.get("printTemplateAction");
  nc.ui.am.action.support.AMPrintTemplateAction bean = new nc.ui.am.action.support.AMPrintTemplateAction();
  context.put("printTemplateAction",bean);
  bean.setModel(getModel());
  bean.setScaleProcessor(getBillVODigitProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMOutputAction getOutputAction(){
 if(context.get("outputAction")!=null)
 return (nc.ui.am.action.support.AMOutputAction)context.get("outputAction");
  nc.ui.am.action.support.AMOutputAction bean = new nc.ui.am.action.support.AMOutputAction();
  context.put("outputAction",bean);
  bean.setModel(getModel());
  bean.setScaleProcessor(getBillVODigitProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.am.scale.process.PrintScaleProcessCenter getBillVODigitProcessor(){
 if(context.get("billVODigitProcessor")!=null)
 return (nc.vo.am.scale.process.PrintScaleProcessCenter)context.get("billVODigitProcessor");
  nc.vo.am.scale.process.PrintScaleProcessCenter bean = new nc.vo.am.scale.process.PrintScaleProcessCenter();
  context.put("billVODigitProcessor",bean);
  bean.setScaleObjectData(getScaleObjectData());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.container.QueryAboutMenuAction getQueryAboutMenuAction(){
 if(context.get("queryAboutMenuAction")!=null)
 return (nc.ui.am.action.container.QueryAboutMenuAction)context.get("queryAboutMenuAction");
  nc.ui.am.action.container.QueryAboutMenuAction bean = new nc.ui.am.action.container.QueryAboutMenuAction();
  context.put("queryAboutMenuAction",bean);
  bean.setActions(getManagedList36());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList36(){  List list = new ArrayList();  return list;}

public nc.ui.am.action.support.AMQueryAboutEquipAction getQueryAboutEquipAction(){
 if(context.get("queryAboutEquipAction")!=null)
 return (nc.ui.am.action.support.AMQueryAboutEquipAction)context.get("queryAboutEquipAction");
  nc.ui.am.action.support.AMQueryAboutEquipAction bean = new nc.ui.am.action.support.AMQueryAboutEquipAction();
  context.put("queryAboutEquipAction",bean);
  bean.setModel(getModel());
  bean.setBillList(getBillListView());
  bean.setBillForm(getBillForm());
  bean.setQueryAboutHead(true);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMQueryApproveflowAction getQueryApproveflowAction(){
 if(context.get("queryApproveflowAction")!=null)
 return (nc.ui.am.action.support.AMQueryApproveflowAction)context.get("queryApproveflowAction");
  nc.ui.am.action.support.AMQueryApproveflowAction bean = new nc.ui.am.action.support.AMQueryApproveflowAction();
  context.put("queryApproveflowAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMQueryAboutVoucherAction getQueryAboutVoucherAction(){
 if(context.get("queryAboutVoucherAction")!=null)
 return (nc.ui.am.action.support.AMQueryAboutVoucherAction)context.get("queryAboutVoucherAction");
  nc.ui.am.action.support.AMQueryAboutVoucherAction bean = new nc.ui.am.action.support.AMQueryAboutVoucherAction();
  context.put("queryAboutVoucherAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMQueryAboutBillAction getQueryAboutBusiness(){
 if(context.get("queryAboutBusiness")!=null)
 return (nc.ui.am.action.support.AMQueryAboutBillAction)context.get("queryAboutBusiness");
  nc.ui.am.action.support.AMQueryAboutBillAction bean = new nc.ui.am.action.support.AMQueryAboutBillAction();
  context.put("queryAboutBusiness",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getHeadTabActions(){
 if(context.get("headTabActions")!=null)
 return (java.util.ArrayList)context.get("headTabActions");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList37());  context.put("headTabActions",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList37(){  List list = new ArrayList();  list.add(getAttachmentAction());  list.add(getActionsBar_ActionsBarSeparator_1f9083e());  list.add(getFirstLineAction());  list.add(getPreLineAction());  list.add(getNextLineAction());  list.add(getLastLineAction());  list.add(getActionsBar_ActionsBarSeparator_8c58e7());  list.add(getHeadViewMaxAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1f9083e(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1f9083e")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1f9083e");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1f9083e",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_8c58e7(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8c58e7")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8c58e7");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8c58e7",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getDefaultBodyActions(){
 if(context.get("defaultBodyActions")!=null)
 return (java.util.ArrayList)context.get("defaultBodyActions");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList38());  context.put("defaultBodyActions",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList38(){  List list = new ArrayList();  list.add(getAddLineAction());  list.add(getInsertLineAction());  list.add(getDeleteLineAction());  list.add(getTableEditLineAction());  list.add(getBatchAlterAction());  list.add(getActionsBar_ActionsBarSeparator_20b03b());  list.add(getBodyViewMaxAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_20b03b(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#20b03b")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#20b03b");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#20b03b",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getSealActionGroup(){
 if(context.get("sealActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("sealActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("sealActionGroup",bean);
  bean.setActions(getManagedList39());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList39(){  List list = new ArrayList();  list.add(getUnSealAction());  list.add(getSealAction());  return list;}

public nc.ui.am.common.action.SealAction getSealAction(){
 if(context.get("sealAction")!=null)
 return (nc.ui.am.common.action.SealAction)context.get("sealAction");
  nc.ui.am.common.action.SealAction bean = new nc.ui.am.common.action.SealAction();
  context.put("sealAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
  bean.setBillOperateService(getBatchOperateService());
  bean.setValidators(getManagedList40());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList40(){  List list = new ArrayList();  list.add(getSealEnableValidator_1f11298());  return list;}

private nc.ui.eampub.validator.SealEnableValidator getSealEnableValidator_1f11298(){
 if(context.get("nc.ui.eampub.validator.SealEnableValidator#1f11298")!=null)
 return (nc.ui.eampub.validator.SealEnableValidator)context.get("nc.ui.eampub.validator.SealEnableValidator#1f11298");
  nc.ui.eampub.validator.SealEnableValidator bean = new nc.ui.eampub.validator.SealEnableValidator();
  context.put("nc.ui.eampub.validator.SealEnableValidator#1f11298",bean);
  bean.setSeal(true);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.common.action.UnSealAction getUnSealAction(){
 if(context.get("unSealAction")!=null)
 return (nc.ui.am.common.action.UnSealAction)context.get("unSealAction");
  nc.ui.am.common.action.UnSealAction bean = new nc.ui.am.common.action.UnSealAction();
  context.put("unSealAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
  bean.setBillOperateService(getBatchOperateService());
  bean.setValidators(getManagedList41());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList41(){  List list = new ArrayList();  list.add(getSealEnableValidator_1fbd0b8());  return list;}

private nc.ui.eampub.validator.SealEnableValidator getSealEnableValidator_1fbd0b8(){
 if(context.get("nc.ui.eampub.validator.SealEnableValidator#1fbd0b8")!=null)
 return (nc.ui.eampub.validator.SealEnableValidator)context.get("nc.ui.eampub.validator.SealEnableValidator#1fbd0b8");
  nc.ui.eampub.validator.SealEnableValidator bean = new nc.ui.eampub.validator.SealEnableValidator();
  context.put("nc.ui.eampub.validator.SealEnableValidator#1fbd0b8",bean);
  bean.setSeal(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.model.PMOperateService getBatchOperateService(){
 if(context.get("batchOperateService")!=null)
 return (nc.ui.emm.pmbase.model.PMOperateService)context.get("batchOperateService");
  nc.ui.emm.pmbase.model.PMOperateService bean = new nc.ui.emm.pmbase.model.PMOperateService();
  context.put("batchOperateService",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMChgCountAction getChgCountAction(){
 if(context.get("chgCountAction")!=null)
 return (nc.ui.emm.pmbase.action.PMChgCountAction)context.get("chgCountAction");
  nc.ui.emm.pmbase.action.PMChgCountAction bean = new nc.ui.emm.pmbase.action.PMChgCountAction();
  context.put("chgCountAction",bean);
  bean.setModel(getModel());
  bean.setValidators(getManagedList42());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList42(){  List list = new ArrayList();  list.add(getUIPermissionValidator_143f79d());  return list;}

private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_143f79d(){
 if(context.get("nc.ui.am.validator.UIPermissionValidator#143f79d")!=null)
 return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#143f79d");
  nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
  context.put("nc.ui.am.validator.UIPermissionValidator#143f79d",bean);
  bean.setContext(getContext());
  bean.setMeta_code("ChgCount");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMAttachmentAction getFileAction(){
 if(context.get("fileAction")!=null)
 return (nc.ui.emm.pmbase.action.PMAttachmentAction)context.get("fileAction");
  nc.ui.emm.pmbase.action.PMAttachmentAction bean = new nc.ui.emm.pmbase.action.PMAttachmentAction();
  context.put("fileAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.container.QueryAboutMenuAction getQueryAboutActionGroup(){
 if(context.get("queryAboutActionGroup")!=null)
 return (nc.ui.am.action.container.QueryAboutMenuAction)context.get("queryAboutActionGroup");
  nc.ui.am.action.container.QueryAboutMenuAction bean = new nc.ui.am.action.container.QueryAboutMenuAction();
  context.put("queryAboutActionGroup",bean);
  bean.setActions(getManagedList43());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList43(){  List list = new ArrayList();  list.add(getQueryAboutEquipAction());  list.add(getQueryAboutLocationAction());  list.add(getQueryAboutMeasurePointAction());  list.add(getQueryAboutStdjobAction());  list.add(getQueryAboutInRoadAction());  list.add(getSeparatorAction());  list.add(getQueryAboutWoAction());  return list;}

public nc.ui.am.action.container.QueryAboutMenuAction getQueryAboutEditActionGroup(){
 if(context.get("queryAboutEditActionGroup")!=null)
 return (nc.ui.am.action.container.QueryAboutMenuAction)context.get("queryAboutEditActionGroup");
  nc.ui.am.action.container.QueryAboutMenuAction bean = new nc.ui.am.action.container.QueryAboutMenuAction();
  context.put("queryAboutEditActionGroup",bean);
  bean.setActions(getManagedList44());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList44(){  List list = new ArrayList();  list.add(getQueryAboutEquipAction());  list.add(getQueryAboutLocationAction());  list.add(getQueryAboutStdjobAction());  list.add(getQueryAboutMeasurePointAction());  return list;}

public nc.ui.am.action.container.QueryAboutMenuAction getQueryAboutActionGroupTime(){
 if(context.get("queryAboutActionGroupTime")!=null)
 return (nc.ui.am.action.container.QueryAboutMenuAction)context.get("queryAboutActionGroupTime");
  nc.ui.am.action.container.QueryAboutMenuAction bean = new nc.ui.am.action.container.QueryAboutMenuAction();
  context.put("queryAboutActionGroupTime",bean);
  bean.setActions(getManagedList45());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList45(){  List list = new ArrayList();  list.add(getQueryAboutEquipAction());  list.add(getQueryAboutLocationAction());  list.add(getQueryAboutStdjobAction());  list.add(getQueryAboutInRoadAction());  list.add(getSeparatorAction());  list.add(getQueryAboutWoAction());  return list;}

public nc.ui.am.action.container.QueryAboutMenuAction getQueryAboutEditActionGroupTime(){
 if(context.get("queryAboutEditActionGroupTime")!=null)
 return (nc.ui.am.action.container.QueryAboutMenuAction)context.get("queryAboutEditActionGroupTime");
  nc.ui.am.action.container.QueryAboutMenuAction bean = new nc.ui.am.action.container.QueryAboutMenuAction();
  context.put("queryAboutEditActionGroupTime",bean);
  bean.setActions(getManagedList46());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList46(){  List list = new ArrayList();  list.add(getQueryAboutEquipAction());  list.add(getQueryAboutLocationAction());  list.add(getQueryAboutStdjobAction());  return list;}

public nc.ui.am.common.action.QueryAboutLocationAction getQueryAboutLocationAction(){
 if(context.get("queryAboutLocationAction")!=null)
 return (nc.ui.am.common.action.QueryAboutLocationAction)context.get("queryAboutLocationAction");
  nc.ui.am.common.action.QueryAboutLocationAction bean = new nc.ui.am.common.action.QueryAboutLocationAction();
  context.put("queryAboutLocationAction",bean);
  bean.setModel(getModel());
  bean.setBillList(getBillListView());
  bean.setBillForm(getBillForm());
  bean.setQueryAboutHead(true);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.common.action.QueryAboutStdJobAction getQueryAboutStdjobAction(){
 if(context.get("queryAboutStdjobAction")!=null)
 return (nc.ui.am.common.action.QueryAboutStdJobAction)context.get("queryAboutStdjobAction");
  nc.ui.am.common.action.QueryAboutStdJobAction bean = new nc.ui.am.common.action.QueryAboutStdJobAction();
  context.put("queryAboutStdjobAction",bean);
  bean.setTabCode("pm_stdjob");
  bean.setModel(getModel());
  bean.setBillList(getBillListView());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.support.AMQueryAboutAction getQueryAboutInRoadAction(){
 if(context.get("queryAboutInRoadAction")!=null)
 return (nc.ui.am.action.support.AMQueryAboutAction)context.get("queryAboutInRoadAction");
  nc.ui.am.action.support.AMQueryAboutAction bean = new nc.ui.am.action.support.AMQueryAboutAction("QueryAboutInRoad");  context.put("queryAboutInRoadAction",bean);
  bean.setModel(getModel());
  bean.setPrimayKeyField("pk_inspection_road");
  bean.setFunnode("4510006008");
  bean.setBillList(getBillListView());
  bean.setBillForm(getBillForm());
  bean.setQueryAboutHead(true);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMQueryAboutWoAction getQueryAboutWoAction(){
 if(context.get("queryAboutWoAction")!=null)
 return (nc.ui.emm.pmbase.action.PMQueryAboutWoAction)context.get("queryAboutWoAction");
  nc.ui.emm.pmbase.action.PMQueryAboutWoAction bean = new nc.ui.emm.pmbase.action.PMQueryAboutWoAction();
  context.put("queryAboutWoAction",bean);
  bean.setPrimayKeyField("pk_wo");
  bean.setModel(getModel());
  bean.setBillList(getBillListView());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMQueryAboutMeasurePointAction getQueryAboutMeasurePointAction(){
 if(context.get("queryAboutMeasurePointAction")!=null)
 return (nc.ui.emm.pmbase.action.PMQueryAboutMeasurePointAction)context.get("queryAboutMeasurePointAction");
  nc.ui.emm.pmbase.action.PMQueryAboutMeasurePointAction bean = new nc.ui.emm.pmbase.action.PMQueryAboutMeasurePointAction();
  context.put("queryAboutMeasurePointAction",bean);
  bean.setPrimayKeyField("pk_measure_point");
  bean.setModel(getModel());
  bean.setBillList(getBillListView());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMWorkObjAddLineAction getWorkObjAddLineAction(){
 if(context.get("workObjAddLineAction")!=null)
 return (nc.ui.emm.pmbase.action.PMWorkObjAddLineAction)context.get("workObjAddLineAction");
  nc.ui.emm.pmbase.action.PMWorkObjAddLineAction bean = new nc.ui.emm.pmbase.action.PMWorkObjAddLineAction();
  context.put("workObjAddLineAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMWorkObjInsertLineAction getWorkObjInsertLineAction(){
 if(context.get("workObjInsertLineAction")!=null)
 return (nc.ui.emm.pmbase.action.PMWorkObjInsertLineAction)context.get("workObjInsertLineAction");
  nc.ui.emm.pmbase.action.PMWorkObjInsertLineAction bean = new nc.ui.emm.pmbase.action.PMWorkObjInsertLineAction();
  context.put("workObjInsertLineAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.MakeWorkOrderAction getMakeWorkOrderAction(){
 if(context.get("makeWorkOrderAction")!=null)
 return (nc.ui.emm.pmbase.action.MakeWorkOrderAction)context.get("makeWorkOrderAction");
  nc.ui.emm.pmbase.action.MakeWorkOrderAction bean = new nc.ui.emm.pmbase.action.MakeWorkOrderAction();
  context.put("makeWorkOrderAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.action.PMCopyAction getCopyAction(){
 if(context.get("copyAction")!=null)
 return (nc.ui.emm.pmbase.action.PMCopyAction)context.get("copyAction");
  nc.ui.emm.pmbase.action.PMCopyAction bean = new nc.ui.emm.pmbase.action.PMCopyAction();
  context.put("copyAction",bean);
  bean.setModel(getModel());
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.validator.DataNotNullValidator getDataNotValidator(){
 if(context.get("dataNotValidator")!=null)
 return (nc.ui.am.validator.DataNotNullValidator)context.get("dataNotValidator");
  nc.ui.am.validator.DataNotNullValidator bean = new nc.ui.am.validator.DataNotNullValidator();
  context.put("dataNotValidator",bean);
  bean.setBillForm(getBillForm());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.emm.premaintain.validator.CheckEquipValidator getCheckEquipValidator(){
 if(context.get("checkEquipValidator")!=null)
 return (nc.vo.emm.premaintain.validator.CheckEquipValidator)context.get("checkEquipValidator");
  nc.vo.emm.premaintain.validator.CheckEquipValidator bean = new nc.vo.emm.premaintain.validator.CheckEquipValidator();
  context.put("checkEquipValidator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.emm.premaintain.validator.CheckPeriodsValidator getCheckPeriodsValidator(){
 if(context.get("checkPeriodsValidator")!=null)
 return (nc.vo.emm.premaintain.validator.CheckPeriodsValidator)context.get("checkPeriodsValidator");
  nc.vo.emm.premaintain.validator.CheckPeriodsValidator bean = new nc.vo.emm.premaintain.validator.CheckPeriodsValidator();
  context.put("checkPeriodsValidator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.emm.premaintain.validator.CheckFreValidator getCheckFreValidator(){
 if(context.get("checkFreValidator")!=null)
 return (nc.vo.emm.premaintain.validator.CheckFreValidator)context.get("checkFreValidator");
  nc.vo.emm.premaintain.validator.CheckFreValidator bean = new nc.vo.emm.premaintain.validator.CheckFreValidator();
  context.put("checkFreValidator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.emm.premaintain.validator.CheckWorkObjValidator getCheckWorkObjValidator(){
 if(context.get("checkWorkObjValidator")!=null)
 return (nc.vo.emm.premaintain.validator.CheckWorkObjValidator)context.get("checkWorkObjValidator");
  nc.vo.emm.premaintain.validator.CheckWorkObjValidator bean = new nc.vo.emm.premaintain.validator.CheckWorkObjValidator();
  context.put("checkWorkObjValidator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.emm.premaintain.validator.CheckAheadValidator getCheckAheadValidator(){
 if(context.get("checkAheadValidator")!=null)
 return (nc.vo.emm.premaintain.validator.CheckAheadValidator)context.get("checkAheadValidator");
  nc.vo.emm.premaintain.validator.CheckAheadValidator bean = new nc.vo.emm.premaintain.validator.CheckAheadValidator();
  context.put("checkAheadValidator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.emm.premaintain.validator.CheckMeasPointValidator getCheckMeasPointValidator(){
 if(context.get("checkMeasPointValidator")!=null)
 return (nc.vo.emm.premaintain.validator.CheckMeasPointValidator)context.get("checkMeasPointValidator");
  nc.vo.emm.premaintain.validator.CheckMeasPointValidator bean = new nc.vo.emm.premaintain.validator.CheckMeasPointValidator();
  context.put("checkMeasPointValidator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.emm.premaintain.validator.CheckMakeBillValidator getCheckIsWoMakeValidator(){
 if(context.get("checkIsWoMakeValidator")!=null)
 return (nc.vo.emm.premaintain.validator.CheckMakeBillValidator)context.get("checkIsWoMakeValidator");
  nc.vo.emm.premaintain.validator.CheckMakeBillValidator bean = new nc.vo.emm.premaintain.validator.CheckMakeBillValidator();
  context.put("checkIsWoMakeValidator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.action.container.AssMenuAction getAssistantFunActionGroup(){
 if(context.get("assistantFunActionGroup")!=null)
 return (nc.ui.am.action.container.AssMenuAction)context.get("assistantFunActionGroup");
  nc.ui.am.action.container.AssMenuAction bean = new nc.ui.am.action.container.AssMenuAction();
  context.put("assistantFunActionGroup",bean);
  bean.setActions(getManagedList47());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList47(){  List list = new ArrayList();  list.add(getChgNextDateAction());  list.add(getChgCountAction());  list.add(getQueryPlanAction());  list.add(getSeparatorAction());  list.add(getFileAction());  return list;}

public nc.ui.emm.pmbase.action.ChgNextDateAction getChgNextDateAction(){
 if(context.get("chgNextDateAction")!=null)
 return (nc.ui.emm.pmbase.action.ChgNextDateAction)context.get("chgNextDateAction");
  nc.ui.emm.pmbase.action.ChgNextDateAction bean = new nc.ui.emm.pmbase.action.ChgNextDateAction();
  context.put("chgNextDateAction",bean);
  bean.setModel(getModel());
  bean.setValidators(getManagedList48());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList48(){  List list = new ArrayList();  list.add(getUIPermissionValidator_d94bce());  return list;}

private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_d94bce(){
 if(context.get("nc.ui.am.validator.UIPermissionValidator#d94bce")!=null)
 return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#d94bce");
  nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
  context.put("nc.ui.am.validator.UIPermissionValidator#d94bce",bean);
  bean.setContext(getContext());
  bean.setMeta_code("ChgNextDat");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmtime.action.QueryPlanAction getQueryPlanAction(){
 if(context.get("queryPlanAction")!=null)
 return (nc.ui.emm.pmtime.action.QueryPlanAction)context.get("queryPlanAction");
  nc.ui.emm.pmtime.action.QueryPlanAction bean = new nc.ui.emm.pmtime.action.QueryPlanAction();
  context.put("queryPlanAction",bean);
  bean.setModel(getModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.init.EMInitRef getPMTimeInitRef(){
 if(context.get("PMTimeInitRef")!=null)
 return (nc.ui.am.ref.init.EMInitRef)context.get("PMTimeInitRef");
  nc.ui.am.ref.init.EMInitRef bean = new nc.ui.am.ref.init.EMInitRef();
  context.put("PMTimeInitRef",bean);
  bean.setRefInfos(getManagedList49());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList49(){  List list = new ArrayList();  list.add(getEquipWithUsedOrgRef());  list.add(getEquipWithUsedOrgRef4Body());  list.add(getLocationRef());  list.add(getLocationRef4Body());  list.add(getWorkTypeRef());  list.add(getWorkStatusRef());  list.add(getPriorityRef());  list.add(getExecutorRef());  list.add(getDirectorRef());  list.add(getPsnGroupRef());  list.add(getSpecialtyRef());  list.add(getWoDeptRef());  list.add(getProjectRef());  list.add(getInspectionRoadRef());  list.add(getStdJobRef4Body());  return list;}

public nc.ui.am.ref.init.EMInitRef getPMResultInitRef(){
 if(context.get("PMResultInitRef")!=null)
 return (nc.ui.am.ref.init.EMInitRef)context.get("PMResultInitRef");
  nc.ui.am.ref.init.EMInitRef bean = new nc.ui.am.ref.init.EMInitRef();
  context.put("PMResultInitRef",bean);
  bean.setRefInfos(getManagedList50());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList50(){  List list = new ArrayList();  list.add(getEquipWithUsedOrgRef());  list.add(getEquipWithUsedOrgRef4Body());  list.add(getLocationRef());  list.add(getLocationRef4Body());  list.add(getWorkTypeRef());  list.add(getWorkStatusRef());  list.add(getPriorityRef());  list.add(getExecutorRef());  list.add(getDirectorRef());  list.add(getPsnGroupRef());  list.add(getSpecialtyRef());  list.add(getWoDeptRef());  list.add(getInspectionRoadRef());  list.add(getProjectRef());  list.add(getStdJobRef4Body());  list.add(getMeasPointRef4Body());  return list;}

public nc.ui.am.ref.init.EMInitRef getPMConditionInitRef(){
 if(context.get("PMConditionInitRef")!=null)
 return (nc.ui.am.ref.init.EMInitRef)context.get("PMConditionInitRef");
  nc.ui.am.ref.init.EMInitRef bean = new nc.ui.am.ref.init.EMInitRef();
  context.put("PMConditionInitRef",bean);
  bean.setRefInfos(getManagedList51());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList51(){  List list = new ArrayList();  list.add(getEquipWithUsedOrgRef());  list.add(getEquipWithUsedOrgRef4Body());  list.add(getLocationRef());  list.add(getLocationRef4Body());  list.add(getWorkTypeRef());  list.add(getWorkStatusRef());  list.add(getPriorityRef());  list.add(getExecutorRef());  list.add(getDirectorRef());  list.add(getPsnGroupRef());  list.add(getSpecialtyRef());  list.add(getWoDeptRef());  list.add(getInspectionRoadRef());  list.add(getProjectRef());  list.add(getStdJobRef4Body());  list.add(getMeasPointRef4Body());  return list;}

public nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo getEquipWithUsedOrgRef(){
 if(context.get("equipWithUsedOrgRef")!=null)
 return (nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo)context.get("equipWithUsedOrgRef");
  nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo bean = new nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo();
  context.put("equipWithUsedOrgRef",bean);
  bean.setFiledKey("pk_equip");
  bean.setPosition(0);
  bean.setAssetOrg(false);
  bean.setDefaultWhereSql("");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo getEquipWithUsedOrgRef4Body(){
 if(context.get("equipWithUsedOrgRef4Body")!=null)
 return (nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo)context.get("equipWithUsedOrgRef4Body");
  nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo bean = new nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo();
  context.put("equipWithUsedOrgRef4Body",bean);
  bean.setFiledKey("pk_equip");
  bean.setTableCode("pm_workobj");
  bean.setMultiSelected(true);
  bean.setAssetOrg(false);
  bean.setDefaultWhereSql("");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.LocationInitRefInfo getLocationRef4Body(){
 if(context.get("locationRef4Body")!=null)
 return (nc.ui.am.ref.refinfos.LocationInitRefInfo)context.get("locationRef4Body");
  nc.ui.am.ref.refinfos.LocationInitRefInfo bean = new nc.ui.am.ref.refinfos.LocationInitRefInfo();
  context.put("locationRef4Body",bean);
  bean.setFiledKey("pk_location");
  bean.setTableCode("pm_workobj");
  bean.setMultiSelected(true);
  bean.setAssetOrg(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.LocationInitRefInfo getLocationRef(){
 if(context.get("locationRef")!=null)
 return (nc.ui.am.ref.refinfos.LocationInitRefInfo)context.get("locationRef");
  nc.ui.am.ref.refinfos.LocationInitRefInfo bean = new nc.ui.am.ref.refinfos.LocationInitRefInfo();
  context.put("locationRef",bean);
  bean.setFiledKey("pk_location");
  bean.setPosition(0);
  bean.setAssetOrg(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.utils.PMWorkTypeRefInfo getWorkTypeRef(){
 if(context.get("workTypeRef")!=null)
 return (nc.ui.emm.pmbase.utils.PMWorkTypeRefInfo)context.get("workTypeRef");
  nc.ui.emm.pmbase.utils.PMWorkTypeRefInfo bean = new nc.ui.emm.pmbase.utils.PMWorkTypeRefInfo();
  context.put("workTypeRef",bean);
  bean.setFiledKey("pk_worktype");
  bean.setPosition(0);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.utils.PMInitWorkStatusRefInfo getWorkStatusRef(){
 if(context.get("workStatusRef")!=null)
 return (nc.ui.emm.pmbase.utils.PMInitWorkStatusRefInfo)context.get("workStatusRef");
  nc.ui.emm.pmbase.utils.PMInitWorkStatusRefInfo bean = new nc.ui.emm.pmbase.utils.PMInitWorkStatusRefInfo();
  context.put("workStatusRef",bean);
  bean.setFiledKey("pk_wo_status");
  bean.setPosition(0);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.InitRefInfo getPriorityRef(){
 if(context.get("priorityRef")!=null)
 return (nc.ui.am.ref.refinfos.InitRefInfo)context.get("priorityRef");
  nc.ui.am.ref.refinfos.InitRefInfo bean = new nc.ui.am.ref.refinfos.InitRefInfo();
  context.put("priorityRef",bean);
  bean.setFiledKey("pk_priority");
  bean.setPosition(0);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.PsnInitRefInfo getExecutorRef(){
 if(context.get("executorRef")!=null)
 return (nc.ui.am.ref.refinfos.PsnInitRefInfo)context.get("executorRef");
  nc.ui.am.ref.refinfos.PsnInitRefInfo bean = new nc.ui.am.ref.refinfos.PsnInitRefInfo();
  context.put("executorRef",bean);
  bean.setFiledKey("pk_executor");
  bean.setPosition(0);
  bean.setDefaultWhereSql("");
  bean.setBusifuncode("as");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.PsnInitRefInfo getDirectorRef(){
 if(context.get("directorRef")!=null)
 return (nc.ui.am.ref.refinfos.PsnInitRefInfo)context.get("directorRef");
  nc.ui.am.ref.refinfos.PsnInitRefInfo bean = new nc.ui.am.ref.refinfos.PsnInitRefInfo();
  context.put("directorRef",bean);
  bean.setFiledKey("pk_director");
  bean.setPosition(0);
  bean.setDefaultWhereSql("");
  bean.setBusifuncode("as");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.InitRefInfo getPsnGroupRef(){
 if(context.get("psnGroupRef")!=null)
 return (nc.ui.am.ref.refinfos.InitRefInfo)context.get("psnGroupRef");
  nc.ui.am.ref.refinfos.InitRefInfo bean = new nc.ui.am.ref.refinfos.InitRefInfo();
  context.put("psnGroupRef",bean);
  bean.setFiledKey("pk_psn_group");
  bean.setPosition(0);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.InitRefInfo getSpecialtyRef(){
 if(context.get("specialtyRef")!=null)
 return (nc.ui.am.ref.refinfos.InitRefInfo)context.get("specialtyRef");
  nc.ui.am.ref.refinfos.InitRefInfo bean = new nc.ui.am.ref.refinfos.InitRefInfo();
  context.put("specialtyRef",bean);
  bean.setFiledKey("pk_specialty");
  bean.setPosition(0);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.DeptInitRefInfo getWoDeptRef(){
 if(context.get("woDeptRef")!=null)
 return (nc.ui.am.ref.refinfos.DeptInitRefInfo)context.get("woDeptRef");
  nc.ui.am.ref.refinfos.DeptInitRefInfo bean = new nc.ui.am.ref.refinfos.DeptInitRefInfo();
  context.put("woDeptRef",bean);
  bean.setFiledKey("pk_wo_dept");
  bean.setPosition(0);
  bean.setDefaultWhereSql("");
  bean.setBusifuncode("as");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.InitRefInfo getProjectRef(){
 if(context.get("projectRef")!=null)
 return (nc.ui.am.ref.refinfos.InitRefInfo)context.get("projectRef");
  nc.ui.am.ref.refinfos.InitRefInfo bean = new nc.ui.am.ref.refinfos.InitRefInfo();
  context.put("projectRef",bean);
  bean.setFiledKey("pk_project");
  bean.setPosition(0);
  bean.setDefaultWhereSql("");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.InitRefInfo getInspectionRoadRef(){
 if(context.get("inspectionRoadRef")!=null)
 return (nc.ui.am.ref.refinfos.InitRefInfo)context.get("inspectionRoadRef");
  nc.ui.am.ref.refinfos.InitRefInfo bean = new nc.ui.am.ref.refinfos.InitRefInfo();
  context.put("inspectionRoadRef",bean);
  bean.setFiledKey("pk_inspection_road");
  bean.setPosition(0);
  bean.setDefaultWhereSql("");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.emm.pmbase.utils.PMMeasPointRefInfo getMeasPointRef4Body(){
 if(context.get("measPointRef4Body")!=null)
 return (nc.ui.emm.pmbase.utils.PMMeasPointRefInfo)context.get("measPointRef4Body");
  nc.ui.emm.pmbase.utils.PMMeasPointRefInfo bean = new nc.ui.emm.pmbase.utils.PMMeasPointRefInfo();
  context.put("measPointRef4Body",bean);
  bean.setFiledKey("pk_measure_point_b");
  bean.setTableCode("pm_result");
  bean.setMultiSelected(true);
  bean.setDefaultWhereSql("");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.am.ref.refinfos.InitRefInfo getStdJobRef4Body(){
 if(context.get("stdJobRef4Body")!=null)
 return (nc.ui.am.ref.refinfos.InitRefInfo)context.get("stdJobRef4Body");
  nc.ui.am.ref.refinfos.InitRefInfo bean = new nc.ui.am.ref.refinfos.InitRefInfo();
  context.put("stdJobRef4Body",bean);
  bean.setFiledKey("pk_std_job");
  bean.setTableCode("pm_stdjob");
  bean.setMultiSelected(true);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.ArrayList getCardViewActions(){
 if(context.get("cardViewActions")!=null)
 return (java.util.ArrayList)context.get("cardViewActions");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList52());  context.put("cardViewActions",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList52(){  List list = new ArrayList();  list.add(getAddAction());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getCopyAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getRefreshAction());  list.add(getSeparatorAction());  list.add(getMakeWorkOrderAction());  list.add(getSealActionGroup());  list.add(getAssistantFunActionGroup());  list.add(getSeparatorAction());  list.add(getQueryAboutActionGroupTime());  list.add(getSeparatorAction());  list.add(getPrintMenuAction());  return list;}

public java.util.ArrayList getCardEditActions(){
 if(context.get("cardEditActions")!=null)
 return (java.util.ArrayList)context.get("cardEditActions");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList53());  context.put("cardEditActions",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList53(){  List list = new ArrayList();  list.add(getSaveAction());  list.add(getSaveAddAction());  list.add(getSeparatorAction());  list.add(getCancelAction());  list.add(getSeparatorAction());  list.add(getQueryAboutEditActionGroupTime());  return list;}

public java.util.ArrayList getListViewActions(){
 if(context.get("listViewActions")!=null)
 return (java.util.ArrayList)context.get("listViewActions");
  java.util.ArrayList bean = new java.util.ArrayList(getManagedList54());  context.put("listViewActions",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList54(){  List list = new ArrayList();  list.add(getAddAction());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getCopyAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getRefreshAllAction());  list.add(getSeparatorAction());  list.add(getMakeWorkOrderAction());  list.add(getSealActionGroup());  list.add(getAssistantFunActionGroup());  list.add(getSeparatorAction());  list.add(getQueryAboutActionGroupTime());  list.add(getSeparatorAction());  list.add(getPrintMenuAction());  return list;}

}
