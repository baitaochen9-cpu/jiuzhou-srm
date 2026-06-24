package nc.ui.bd.cust.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class Customer_Config_Org extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.vo.uif2.LoginContext getContext(){
 if(context.get("context")!=null)
 return (nc.vo.uif2.LoginContext)context.get("context");
  nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
  context.put("context",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.uitabextend.ExinfoLoader getExinfoloader(){
 if(context.get("exinfoloader")!=null)
 return (nc.ui.bd.uitabextend.ExinfoLoader)context.get("exinfoloader");
  nc.ui.bd.uitabextend.ExinfoLoader bean = new nc.ui.bd.uitabextend.ExinfoLoader();
  context.put("exinfoloader",bean);
  bean.setCurrent_md_ID("e4f48eaf-5567-4383-a370-a59cb3e8a451");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.uitabextend.UITabExtManager getUiTabExtMnger(){
 if(context.get("uiTabExtMnger")!=null)
 return (nc.ui.bd.uitabextend.UITabExtManager)context.get("uiTabExtMnger");
  nc.ui.bd.uitabextend.UITabExtManager bean = new nc.ui.bd.uitabextend.UITabExtManager();
  context.put("uiTabExtMnger",bean);
  bean.setTargetComponent(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.ActionContributors getToftpanelActionContributors(){
 if(context.get("toftpanelActionContributors")!=null)
 return (nc.ui.uif2.actions.ActionContributors)context.get("toftpanelActionContributors");
  nc.ui.uif2.actions.ActionContributors bean = new nc.ui.uif2.actions.ActionContributors();
  context.put("toftpanelActionContributors",bean);
  bean.setContributors(getManagedList0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList0(){  List list = new ArrayList();  list.add(getBaseinfoListViewActions());  list.add(getBaseinfoEditorActions());  return list;}

public nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller getRemoteCallCombinatorCaller(){
 if(context.get("remoteCallCombinatorCaller")!=null)
 return (nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller)context.get("remoteCallCombinatorCaller");
  nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller bean = new nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller();
  context.put("remoteCallCombinatorCaller",bean);
  bean.setRemoteCallers(getManagedList1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add(getTemplateContainer());  list.add(getUserdefitemContainer());  list.add(getQueryTemplateContainer());  return list;}

public nc.ui.uif2.editor.TemplateContainer getTemplateContainer(){
 if(context.get("templateContainer")!=null)
 return (nc.ui.uif2.editor.TemplateContainer)context.get("templateContainer");
  nc.ui.uif2.editor.TemplateContainer bean = new nc.ui.uif2.editor.TemplateContainer();
  context.put("templateContainer",bean);
  bean.setContext(getContext());
  bean.setNodeKeies(getManagedList2());
  bean.load();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add("");  list.add("share");  list.add("createSupplier");  list.add("associateSup");  return list;}

public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer(){
 if(context.get("userdefitemContainer")!=null)
 return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
  nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
  context.put("userdefitemContainer",bean);
  bean.setContext(getContext());
  bean.setParams(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList3(){  List list = new ArrayList();  list.add(getCustomerUserdefitemParam());  list.add(getCustlinkmanUserdefitemParam());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getCustomerUserdefitemParam(){
 if(context.get("customerUserdefitemParam")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("customerUserdefitemParam");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("customerUserdefitemParam",bean);
  bean.setMdfullname("uap.customer");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getCustlinkmanUserdefitemParam(){
 if(context.get("custlinkmanUserdefitemParam")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("custlinkmanUserdefitemParam");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("custlinkmanUserdefitemParam",bean);
  bean.setMdfullname("uap.custlinkman");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.QueryTemplateContainer getQueryTemplateContainer(){
 if(context.get("queryTemplateContainer")!=null)
 return (nc.ui.uif2.editor.QueryTemplateContainer)context.get("queryTemplateContainer");
  nc.ui.uif2.editor.QueryTemplateContainer bean = new nc.ui.uif2.editor.QueryTemplateContainer();
  context.put("queryTemplateContainer",bean);
  bean.setContext(getContext());
  bean.setNodeKey("");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.service.CustBaseInfoModelService getService(){
 if(context.get("service")!=null)
 return (nc.ui.bd.cust.baseinfo.service.CustBaseInfoModelService)context.get("service");
  nc.ui.bd.cust.baseinfo.service.CustBaseInfoModelService bean = new nc.ui.bd.cust.baseinfo.service.CustBaseInfoModelService();
  context.put("service",bean);
  bean.setContext(getContext());
  bean.setExtendContext(getBaseExtendContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.BDObjectAdpaterFactory getBoadapterfacotry(){
 if(context.get("boadapterfacotry")!=null)
 return (nc.vo.bd.meta.BDObjectAdpaterFactory)context.get("boadapterfacotry");
  nc.vo.bd.meta.BDObjectAdpaterFactory bean = new nc.vo.bd.meta.BDObjectAdpaterFactory();
  context.put("boadapterfacotry",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.model.CustBaseInfoModel getBaseinfoModel(){
 if(context.get("baseinfoModel")!=null)
 return (nc.ui.bd.cust.baseinfo.model.CustBaseInfoModel)context.get("baseinfoModel");
  nc.ui.bd.cust.baseinfo.model.CustBaseInfoModel bean = new nc.ui.bd.cust.baseinfo.model.CustBaseInfoModel();
  context.put("baseinfoModel",bean);
  bean.setBusinessObjectAdapterFactory(getBoadapterfacotry());
  bean.setService(getService());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.model.CustListModelDataManager getModelDataManager(){
 if(context.get("modelDataManager")!=null)
 return (nc.ui.bd.cust.baseinfo.model.CustListModelDataManager)context.get("modelDataManager");
  nc.ui.bd.cust.baseinfo.model.CustListModelDataManager bean = new nc.ui.bd.cust.baseinfo.model.CustListModelDataManager();
  context.put("modelDataManager",bean);
  bean.setListModel(getBaseinfoModel());
  bean.setPaginationModel(getPaginationModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.model.CustFuncNodeInitDataListener getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.bd.cust.baseinfo.model.CustFuncNodeInitDataListener)context.get("InitDataListener");
  nc.ui.bd.cust.baseinfo.model.CustFuncNodeInitDataListener bean = new nc.ui.bd.cust.baseinfo.model.CustFuncNodeInitDataListener();
  context.put("InitDataListener",bean);
  bean.setDataManager(getModelDataManager());
  bean.setQueryAction(getQueryAction());
  bean.setEditor(getBaseinfoListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.pagination.PaginationModel getPaginationModel(){
 if(context.get("paginationModel")!=null)
 return (nc.ui.uif2.components.pagination.PaginationModel)context.get("paginationModel");
  nc.ui.uif2.components.pagination.PaginationModel bean = new nc.ui.uif2.components.pagination.PaginationModel();
  context.put("paginationModel",bean);
  bean.init();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.editor.CustBaseInfoListView getBaseinfoListView(){
 if(context.get("baseinfoListView")!=null)
 return (nc.ui.bd.cust.baseinfo.editor.CustBaseInfoListView)context.get("baseinfoListView");
  nc.ui.bd.cust.baseinfo.editor.CustBaseInfoListView bean = new nc.ui.bd.cust.baseinfo.editor.CustBaseInfoListView();
  context.put("baseinfoListView",bean);
  bean.setModel(getBaseinfoModel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("");
  bean.setPos("head");
  bean.setMultiSelectionEnable(true);
  bean.setUserdefitemListPreparator(getUserdefitemListPreparator());
  bean.setSouth(getPaginationBar());
  bean.setNorth(getListInfoPnl());
  bean.setBcsAction(getBatchcreateSupplierAction());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemListPreparator(){
 if(context.get("userdefitemListPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("userdefitemListPreparator");
  nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
  context.put("userdefitemListPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList4());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList4(){  List list = new ArrayList();  list.add(getListUserdefitemQueryParam());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getListUserdefitemQueryParam(){
 if(context.get("listUserdefitemQueryParam")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("listUserdefitemQueryParam");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("listUserdefitemQueryParam",bean);
  bean.setMdfullname("uap.customer");
  bean.setPos(0);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getListInfoPnl(){
 if(context.get("listInfoPnl")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("listInfoPnl");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("listInfoPnl",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pub.datatemplet.action.DataTempletAction getDataTempletAction(){
 if(context.get("dataTempletAction")!=null)
 return (nc.ui.pub.datatemplet.action.DataTempletAction)context.get("dataTempletAction");
  nc.ui.pub.datatemplet.action.DataTempletAction bean = new nc.ui.pub.datatemplet.action.DataTempletAction();
  context.put("dataTempletAction",bean);
  bean.setTemplet(getDataTemplet());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pub.datatemplet.DataTemplet getDataTemplet(){
 if(context.get("dataTemplet")!=null)
 return (nc.ui.pub.datatemplet.DataTemplet)context.get("dataTemplet");
  nc.ui.pub.datatemplet.DataTemplet bean = new nc.ui.pub.datatemplet.DataTemplet();
  context.put("dataTemplet",bean);
  bean.setContext(getContext());
  bean.setConvert(getDataTempletConvert());
  bean.setValidation(getDataTempletValidation());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pub.datatemplet.DataTempletConvert getDataTempletConvert(){
 if(context.get("dataTempletConvert")!=null)
 return (nc.ui.pub.datatemplet.DataTempletConvert)context.get("dataTempletConvert");
  nc.ui.pub.datatemplet.DataTempletConvert bean = new nc.ui.pub.datatemplet.DataTempletConvert();
  context.put("dataTempletConvert",bean);
  bean.setEditor(getBaseinfoEditor());
  bean.setSetValueStrategy("one_to_many");
  bean.setBeforeImportHandlers(getManagedList5());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add(getBeforeImportHandler());  return list;}

public nc.ui.bd.pub.datatemplet.SharePanelBeforeImport getBeforeImportHandler(){
 if(context.get("beforeImportHandler")!=null)
 return (nc.ui.bd.pub.datatemplet.SharePanelBeforeImport)context.get("beforeImportHandler");
  nc.ui.bd.pub.datatemplet.SharePanelBeforeImport bean = new nc.ui.bd.pub.datatemplet.SharePanelBeforeImport();
  context.put("beforeImportHandler",bean);
  bean.setEditor(getBaseinfoEditor());
  bean.setImportModel(getBaseinfoModel());
  bean.setContext(getContext());
  bean.setSharePanel(getSharepanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pub.datatemplet.DataTempletValidation getDataTempletValidation(){
 if(context.get("dataTempletValidation")!=null)
 return (nc.ui.pub.datatemplet.DataTempletValidation)context.get("dataTempletValidation");
  nc.ui.pub.datatemplet.DataTempletValidation bean = new nc.ui.pub.datatemplet.DataTempletValidation();
  context.put("dataTempletValidation",bean);
  bean.setEditor(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.pagination.PaginationBar getPaginationBar(){
 if(context.get("paginationBar")!=null)
 return (nc.ui.uif2.components.pagination.PaginationBar)context.get("paginationBar");
  nc.ui.uif2.components.pagination.PaginationBar bean = new nc.ui.uif2.components.pagination.PaginationBar();
  context.put("paginationBar",bean);
  bean.setPaginationModel(getPaginationModel());
  bean.setContext(getContext());
  bean.registeCallbak();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.attach.AttachAction getAccessoryShowAction(){
 if(context.get("accessoryShowAction")!=null)
 return (nc.ui.bd.attach.AttachAction)context.get("accessoryShowAction");
  nc.ui.bd.attach.AttachAction bean = new nc.ui.bd.attach.AttachAction();
  context.put("accessoryShowAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setPrefix("uapbd/e4f48eaf-5567-4383-a370-a59cb3e8a451");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getCardInfoPnl(){
 if(context.get("cardInfoPnl")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("cardInfoPnl");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("cardInfoPnl",bean);
  bean.setActions(getManagedList6());
  bean.setTitleAction(getReturnaction());
  bean.setModel(getBaseinfoModel());
  bean.setDataTemplateAction(getDataTempletAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add(getAccessoryShowAction());  list.add(getActionsBarSeparator());  list.add(getFirstLineAction());  list.add(getPreLineAction());  list.add(getNextLineAction());  list.add(getLastLineAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBarSeparator(){
 if(context.get("actionsBarSeparator")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("actionsBarSeparator");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("actionsBarSeparator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.FirstLineAction getFirstLineAction(){
 if(context.get("firstLineAction")!=null)
 return (nc.ui.uif2.actions.FirstLineAction)context.get("firstLineAction");
  nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
  context.put("firstLineAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setExceptionHandler(getExceptionhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.PreLineAction getPreLineAction(){
 if(context.get("preLineAction")!=null)
 return (nc.ui.uif2.actions.PreLineAction)context.get("preLineAction");
  nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
  context.put("preLineAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setExceptionHandler(getExceptionhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.NextLineAction getNextLineAction(){
 if(context.get("nextLineAction")!=null)
 return (nc.ui.uif2.actions.NextLineAction)context.get("nextLineAction");
  nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
  context.put("nextLineAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setExceptionHandler(getExceptionhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.LastLineAction getLastLineAction(){
 if(context.get("lastLineAction")!=null)
 return (nc.ui.uif2.actions.LastLineAction)context.get("lastLineAction");
  nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
  context.put("lastLineAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setExceptionHandler(getExceptionhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.bd.attach.UpdateAccAction getReturnaction(){
 if(context.get("returnaction")!=null)
 return (nc.ui.bd.attach.UpdateAccAction)context.get("returnaction");
  nc.ui.bd.attach.UpdateAccAction bean = new nc.ui.bd.attach.UpdateAccAction();
  context.put("returnaction",bean);
  bean.setGoComponent(getBaseinfoListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.editor.CustShareInfoEditor getSharepanel(){
 if(context.get("sharepanel")!=null)
 return (nc.ui.bd.cust.baseinfo.editor.CustShareInfoEditor)context.get("sharepanel");
  nc.ui.bd.cust.baseinfo.editor.CustShareInfoEditor bean = new nc.ui.bd.cust.baseinfo.editor.CustShareInfoEditor();
  context.put("sharepanel",bean);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("share");
  bean.setModel(getBaseinfoModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.editor.CustBaseInfoEditor getBaseinfoEditor(){
 if(context.get("baseinfoEditor")!=null)
 return (nc.ui.bd.cust.baseinfo.editor.CustBaseInfoEditor)context.get("baseinfoEditor");
  nc.ui.bd.cust.baseinfo.editor.CustBaseInfoEditor bean = new nc.ui.bd.cust.baseinfo.editor.CustBaseInfoEditor();
  context.put("baseinfoEditor",bean);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setModel(getBaseinfoModel());
  bean.setNodekey("");
  bean.setShareInfoEditor(getSharepanel());
  bean.setExceptionHandler(getExceptionhandler());
  bean.setBodyActionMap(getManagedMap0());
  bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_1c2a47d());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("custcontacts",getManagedList7());  map.put("custtaxtypes",getManagedList8());  map.put("custvat",getManagedList9());  return map;}

private List getManagedList7(){  List list = new ArrayList();  list.add(getCustContactsAddLineAction_cad5fc());  list.add(getInsertLineAction_a6c923());  list.add(getDelLineAction_1df485a());  return list;}

private nc.ui.bd.cust.baseinfo.action.CustContactsAddLineAction getCustContactsAddLineAction_cad5fc(){
 if(context.get("nc.ui.bd.cust.baseinfo.action.CustContactsAddLineAction#cad5fc")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustContactsAddLineAction)context.get("nc.ui.bd.cust.baseinfo.action.CustContactsAddLineAction#cad5fc");
  nc.ui.bd.cust.baseinfo.action.CustContactsAddLineAction bean = new nc.ui.bd.cust.baseinfo.action.CustContactsAddLineAction();
  context.put("nc.ui.bd.cust.baseinfo.action.CustContactsAddLineAction#cad5fc",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.InsertLineAction getInsertLineAction_a6c923(){
 if(context.get("nc.ui.uif2.actions.InsertLineAction#a6c923")!=null)
 return (nc.ui.uif2.actions.InsertLineAction)context.get("nc.ui.uif2.actions.InsertLineAction#a6c923");
  nc.ui.uif2.actions.InsertLineAction bean = new nc.ui.uif2.actions.InsertLineAction();
  context.put("nc.ui.uif2.actions.InsertLineAction#a6c923",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.DelLineAction getDelLineAction_1df485a(){
 if(context.get("nc.ui.uif2.actions.DelLineAction#1df485a")!=null)
 return (nc.ui.uif2.actions.DelLineAction)context.get("nc.ui.uif2.actions.DelLineAction#1df485a");
  nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
  context.put("nc.ui.uif2.actions.DelLineAction#1df485a",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getAddLineAction_73299());  list.add(getInsertLineAction_8b0d16());  list.add(getDelLineAction_1084388());  return list;}

private nc.ui.uif2.actions.AddLineAction getAddLineAction_73299(){
 if(context.get("nc.ui.uif2.actions.AddLineAction#73299")!=null)
 return (nc.ui.uif2.actions.AddLineAction)context.get("nc.ui.uif2.actions.AddLineAction#73299");
  nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
  context.put("nc.ui.uif2.actions.AddLineAction#73299",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.InsertLineAction getInsertLineAction_8b0d16(){
 if(context.get("nc.ui.uif2.actions.InsertLineAction#8b0d16")!=null)
 return (nc.ui.uif2.actions.InsertLineAction)context.get("nc.ui.uif2.actions.InsertLineAction#8b0d16");
  nc.ui.uif2.actions.InsertLineAction bean = new nc.ui.uif2.actions.InsertLineAction();
  context.put("nc.ui.uif2.actions.InsertLineAction#8b0d16",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.DelLineAction getDelLineAction_1084388(){
 if(context.get("nc.ui.uif2.actions.DelLineAction#1084388")!=null)
 return (nc.ui.uif2.actions.DelLineAction)context.get("nc.ui.uif2.actions.DelLineAction#1084388");
  nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
  context.put("nc.ui.uif2.actions.DelLineAction#1084388",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList9(){  List list = new ArrayList();  list.add(getAddLineAction_189c04e());  list.add(getInsertLineAction_b06386());  list.add(getDelLineAction_55f6de());  return list;}

private nc.ui.uif2.actions.AddLineAction getAddLineAction_189c04e(){
 if(context.get("nc.ui.uif2.actions.AddLineAction#189c04e")!=null)
 return (nc.ui.uif2.actions.AddLineAction)context.get("nc.ui.uif2.actions.AddLineAction#189c04e");
  nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
  context.put("nc.ui.uif2.actions.AddLineAction#189c04e",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.InsertLineAction getInsertLineAction_b06386(){
 if(context.get("nc.ui.uif2.actions.InsertLineAction#b06386")!=null)
 return (nc.ui.uif2.actions.InsertLineAction)context.get("nc.ui.uif2.actions.InsertLineAction#b06386");
  nc.ui.uif2.actions.InsertLineAction bean = new nc.ui.uif2.actions.InsertLineAction();
  context.put("nc.ui.uif2.actions.InsertLineAction#b06386",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.DelLineAction getDelLineAction_55f6de(){
 if(context.get("nc.ui.uif2.actions.DelLineAction#55f6de")!=null)
 return (nc.ui.uif2.actions.DelLineAction)context.get("nc.ui.uif2.actions.DelLineAction#55f6de");
  nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
  context.put("nc.ui.uif2.actions.DelLineAction#55f6de",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_1c2a47d(){
 if(context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#1c2a47d")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#1c2a47d");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#1c2a47d",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList10());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList10(){  List list = new ArrayList();  list.add(getCardUserdefitemQueryParam());  list.add(getBodyUserdefitemQueryParam());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getCardUserdefitemQueryParam(){
 if(context.get("cardUserdefitemQueryParam")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("cardUserdefitemQueryParam");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("cardUserdefitemQueryParam",bean);
  bean.setMdfullname("uap.customer");
  bean.setPos(0);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getBodyUserdefitemQueryParam(){
 if(context.get("bodyUserdefitemQueryParam")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("bodyUserdefitemQueryParam");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("bodyUserdefitemQueryParam",bean);
  bean.setMdfullname("uap.custlinkman");
  bean.setPos(1);
  bean.setPrefix("def");
  bean.setTabcode("custcontacts");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.DefaultExceptionHanler getExceptionhandler(){
 if(context.get("exceptionhandler")!=null)
 return (nc.ui.uif2.DefaultExceptionHanler)context.get("exceptionhandler");
  nc.ui.uif2.DefaultExceptionHanler bean = new nc.ui.uif2.DefaultExceptionHanler();
  context.put("exceptionhandler",bean);
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

public nc.ui.uif2.actions.SwitchAction getSwitchBaseMediator(){
 if(context.get("switchBaseMediator")!=null)
 return (nc.ui.uif2.actions.SwitchAction)context.get("switchBaseMediator");
  nc.ui.uif2.actions.SwitchAction bean = new nc.ui.uif2.actions.SwitchAction();
  context.put("switchBaseMediator",bean);
  bean.setContext(getContext());
  bean.setComponent1(getBaseinfoListView());
  bean.setComponent2(getBaseinfoEditor());
  bean.init();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustBaseInfoAddAction getAddAction(){
 if(context.get("addAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseInfoAddAction)context.get("addAction");
  nc.ui.bd.cust.baseinfo.action.CustBaseInfoAddAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseInfoAddAction();
  context.put("addAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setDataTempletAction(getDataTempletAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustEditAction getEditAction(){
 if(context.get("editAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustEditAction)context.get("editAction");
  nc.ui.bd.cust.baseinfo.action.CustEditAction bean = new nc.ui.bd.cust.baseinfo.action.CustEditAction();
  context.put("editAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setResourceCode("customer");
  bean.setMdOperateCode("edit");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.BDAnsyDelAction getDeleteAction(){
 if(context.get("deleteAction")!=null)
 return (nc.ui.bd.pub.actions.BDAnsyDelAction)context.get("deleteAction");
  nc.ui.bd.pub.actions.BDAnsyDelAction bean = new nc.ui.bd.pub.actions.BDAnsyDelAction();
  context.put("deleteAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setResourceCode("customer");
  bean.setMdOperateCode("delete");
  bean.setService(getService());
  bean.setRefreshaction(getRefreshAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.assign.multiorg.MultiOrgAssignVisitAction getOrgvisitAction(){
 if(context.get("orgvisitAction")!=null)
 return (nc.ui.bd.pub.assign.multiorg.MultiOrgAssignVisitAction)context.get("orgvisitAction");
  nc.ui.bd.pub.assign.multiorg.MultiOrgAssignVisitAction bean = new nc.ui.bd.pub.assign.multiorg.MultiOrgAssignVisitAction();
  context.put("orgvisitAction",bean);
  bean.setCode("custorgvisitaction");
  bean.setModel(getBaseinfoModel());
  bean.setFunnode("10140CORG");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustBaseCopyAddAction getCopyAddAction(){
 if(context.get("copyAddAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseCopyAddAction)context.get("copyAddAction");
  nc.ui.bd.cust.baseinfo.action.CustBaseCopyAddAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseCopyAddAction();
  context.put("copyAddAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setEditor(getBaseinfoEditor());
  bean.setShareEditor(getSharepanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getBatchUpdateGroupAction(){
 if(context.get("batchUpdateGroupAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("batchUpdateGroupAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("batchUpdateGroupAction",bean);
  bean.setCode("BatchUpdateGroup");
  bean.setActions(getManagedList11());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList11(){  List list = new ArrayList();  list.add(getBatchUpdateAction());  list.add(getBatchUpdateWizardAction());  return list;}

public nc.ui.bd.pub.actions.BDBatchUpdateWizardAction getBatchUpdateWizardAction(){
 if(context.get("batchUpdateWizardAction")!=null)
 return (nc.ui.bd.pub.actions.BDBatchUpdateWizardAction)context.get("batchUpdateWizardAction");
  nc.ui.bd.pub.actions.BDBatchUpdateWizardAction bean = new nc.ui.bd.pub.actions.BDBatchUpdateWizardAction();
  context.put("batchUpdateWizardAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setMdId("e4f48eaf-5567-4383-a370-a59cb3e8a451");
  bean.setQryTempNodeKey("");
  bean.setBillTempNodekey("assign");
  bean.setBillTemplatePkField("pk_customer");
  bean.setQryService(getService());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustBatchUpdateAction getBatchUpdateAction(){
 if(context.get("batchUpdateAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBatchUpdateAction)context.get("batchUpdateAction");
  nc.ui.bd.cust.baseinfo.action.CustBatchUpdateAction bean = new nc.ui.bd.cust.baseinfo.action.CustBatchUpdateAction();
  context.put("batchUpdateAction",bean);
  bean.setWizardAction(getBatchUpdateWizardAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.QueryAction getQueryAction(){
 if(context.get("queryAction")!=null)
 return (nc.ui.uif2.actions.QueryAction)context.get("queryAction");
  nc.ui.uif2.actions.QueryAction bean = new nc.ui.uif2.actions.QueryAction();
  context.put("queryAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setDataManager(getModelDataManager());
  bean.setQueryDelegator(getBusinessUnitOnlyQueryDelegator_b6bb6c());
  bean.setTemplateContainer(getQueryTemplateContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.bd.pub.query.BusinessUnitOnlyQueryDelegator getBusinessUnitOnlyQueryDelegator_b6bb6c(){
 if(context.get("nc.ui.bd.pub.query.BusinessUnitOnlyQueryDelegator#b6bb6c")!=null)
 return (nc.ui.bd.pub.query.BusinessUnitOnlyQueryDelegator)context.get("nc.ui.bd.pub.query.BusinessUnitOnlyQueryDelegator#b6bb6c");
  nc.ui.bd.pub.query.BusinessUnitOnlyQueryDelegator bean = new nc.ui.bd.pub.query.BusinessUnitOnlyQueryDelegator();
  context.put("nc.ui.bd.pub.query.BusinessUnitOnlyQueryDelegator#b6bb6c",bean);
  bean.setContext(getContext());
  bean.setReplaceRefModelFields(getManagedList12());
  bean.setTemplateContainer(getQueryTemplateContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList12(){  List list = new ArrayList();  list.add("pk_org_assign");  return list;}

public nc.funcnode.ui.action.MenuAction getFilterAction(){
 if(context.get("filterAction")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("filterAction");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("filterAction",bean);
  bean.setCode("ShowDisable");
  bean.setName(getI18nFB_154d354());
  bean.setActions(getManagedList13());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_154d354(){
 if(context.get("nc.ui.uif2.I18nFB#154d354")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#154d354");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#154d354",bean);  bean.setResDir("common");
  bean.setDefaultValue("过滤");
  bean.setResId("UCH069");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#154d354",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList13(){  List list = new ArrayList();  list.add(getShowDisableAction());  return list;}

public nc.ui.uif2.actions.ShowDisableDataAction getShowDisableAction(){
 if(context.get("showDisableAction")!=null)
 return (nc.ui.uif2.actions.ShowDisableDataAction)context.get("showDisableAction");
  nc.ui.uif2.actions.ShowDisableDataAction bean = new nc.ui.uif2.actions.ShowDisableDataAction();
  context.put("showDisableAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.RefreshAction getRefreshAction(){
 if(context.get("refreshAction")!=null)
 return (nc.ui.uif2.actions.RefreshAction)context.get("refreshAction");
  nc.ui.uif2.actions.RefreshAction bean = new nc.ui.uif2.actions.RefreshAction();
  context.put("refreshAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustBaseInfoRefreshSingleAction getRefreshSingleAction(){
 if(context.get("refreshSingleAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseInfoRefreshSingleAction)context.get("refreshSingleAction");
  nc.ui.bd.cust.baseinfo.action.CustBaseInfoRefreshSingleAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseInfoRefreshSingleAction();
  context.put("refreshSingleAction",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustomerSaveAction getSaveAction(){
 if(context.get("saveAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustomerSaveAction)context.get("saveAction");
  nc.ui.bd.cust.baseinfo.action.CustomerSaveAction bean = new nc.ui.bd.cust.baseinfo.action.CustomerSaveAction();
  context.put("saveAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setEditor(getBaseinfoEditor());
  bean.setInterceptor(getCompositeActionInterceptor());
  bean.setValidationService(getValidationService());
  bean.setRefreshAction(getRefreshSingleAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.SaveAddAction getSaveAddAction(){
 if(context.get("saveAddAction")!=null)
 return (nc.ui.uif2.actions.SaveAddAction)context.get("saveAddAction");
  nc.ui.uif2.actions.SaveAddAction bean = new nc.ui.uif2.actions.SaveAddAction();
  context.put("saveAddAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setEditor(getBaseinfoEditor());
  bean.setAddAction(getAddAction());
  bean.setInterceptor(getCompositeActionInterceptor());
  bean.setValidationService(getValidationService());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.CompositeActionInterceptor getCompositeActionInterceptor(){
 if(context.get("compositeActionInterceptor")!=null)
 return (nc.ui.bd.pub.actions.CompositeActionInterceptor)context.get("compositeActionInterceptor");
  nc.ui.bd.pub.actions.CompositeActionInterceptor bean = new nc.ui.bd.pub.actions.CompositeActionInterceptor();
  context.put("compositeActionInterceptor",bean);
  bean.setInterceptors(getManagedList14());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList14(){  List list = new ArrayList();  list.add(getManageModeActionInterceptor());  list.add(getBdSaveActionInterceptor());  return list;}

public nc.ui.bd.pub.actions.BDSaveActionInterceptor getBdSaveActionInterceptor(){
 if(context.get("bdSaveActionInterceptor")!=null)
 return (nc.ui.bd.pub.actions.BDSaveActionInterceptor)context.get("bdSaveActionInterceptor");
  nc.ui.bd.pub.actions.BDSaveActionInterceptor bean = new nc.ui.bd.pub.actions.BDSaveActionInterceptor();
  context.put("bdSaveActionInterceptor",bean);
  bean.setEditor(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.ManageModeActionInterceptor getManageModeActionInterceptor(){
 if(context.get("manageModeActionInterceptor")!=null)
 return (nc.ui.bd.pub.actions.ManageModeActionInterceptor)context.get("manageModeActionInterceptor");
  nc.ui.bd.pub.actions.ManageModeActionInterceptor bean = new nc.ui.bd.pub.actions.ManageModeActionInterceptor();
  context.put("manageModeActionInterceptor",bean);
  bean.setModel(getBaseinfoModel());
  bean.setEditor(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.bs.uif2.validation.DefaultValidationService getValidationService(){
 if(context.get("validationService")!=null)
 return (nc.bs.uif2.validation.DefaultValidationService)context.get("validationService");
  nc.bs.uif2.validation.DefaultValidationService bean = new nc.bs.uif2.validation.DefaultValidationService();
  context.put("validationService",bean);
  bean.setValidators(getManagedList15());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList15(){  List list = new ArrayList();  list.add(getFreeCustValidator_1fe68e6());  return list;}

private nc.bs.bd.cust.baseinfo.validator.FreeCustValidator getFreeCustValidator_1fe68e6(){
 if(context.get("nc.bs.bd.cust.baseinfo.validator.FreeCustValidator#1fe68e6")!=null)
 return (nc.bs.bd.cust.baseinfo.validator.FreeCustValidator)context.get("nc.bs.bd.cust.baseinfo.validator.FreeCustValidator#1fe68e6");
  nc.bs.bd.cust.baseinfo.validator.FreeCustValidator bean = new nc.bs.bd.cust.baseinfo.validator.FreeCustValidator();
  context.put("nc.bs.bd.cust.baseinfo.validator.FreeCustValidator#1fe68e6",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustCancelAction getCancelAction(){
 if(context.get("cancelAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustCancelAction)context.get("cancelAction");
  nc.ui.bd.cust.baseinfo.action.CustCancelAction bean = new nc.ui.bd.cust.baseinfo.action.CustCancelAction();
  context.put("cancelAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setEditor(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getAssignActionGroup(){
 if(context.get("assignActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("assignActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("assignActionGroup",bean);
  bean.setCode("AssignGroup");
  bean.setActions(getManagedList16());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList16(){  List list = new ArrayList();  list.add(getAssignAction());  list.add(getCancelAssignAction());  list.add(getAssignWizardAction());  list.add(getAssignStatusAction());  return list;}

public nc.ui.bd.cust.baseinfo.action.CustAssignAction getAssignAction(){
 if(context.get("assignAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustAssignAction)context.get("assignAction");
  nc.ui.bd.cust.baseinfo.action.CustAssignAction bean = new nc.ui.bd.cust.baseinfo.action.CustAssignAction();
  context.put("assignAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setAssignContext(getAssignContext());
  bean.setInterceptor(getManageModeActionInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustCancelAssignAction getCancelAssignAction(){
 if(context.get("CancelAssignAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustCancelAssignAction)context.get("CancelAssignAction");
  nc.ui.bd.cust.baseinfo.action.CustCancelAssignAction bean = new nc.ui.bd.cust.baseinfo.action.CustCancelAssignAction();
  context.put("CancelAssignAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setAssignContext(getAssignContext());
  bean.setInterceptor(getManageModeActionInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustAssignWizardAction getAssignWizardAction(){
 if(context.get("AssignWizardAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustAssignWizardAction)context.get("AssignWizardAction");
  nc.ui.bd.cust.baseinfo.action.CustAssignWizardAction bean = new nc.ui.bd.cust.baseinfo.action.CustAssignWizardAction();
  context.put("AssignWizardAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setAssignContext(getAssignContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.BDAssignStatusAction getAssignStatusAction(){
 if(context.get("AssignStatusAction")!=null)
 return (nc.ui.bd.pub.actions.BDAssignStatusAction)context.get("AssignStatusAction");
  nc.ui.bd.pub.actions.BDAssignStatusAction bean = new nc.ui.bd.pub.actions.BDAssignStatusAction();
  context.put("AssignStatusAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setQueryService(getAssignService());
  bean.setFunnode("10140CASTAT");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.assign.AssignContext getAssignContext(){
 if(context.get("assignContext")!=null)
 return (nc.ui.bd.pub.assign.AssignContext)context.get("assignContext");
  nc.ui.bd.pub.assign.AssignContext bean = new nc.ui.bd.pub.assign.AssignContext();
  context.put("assignContext",bean);
  bean.setAssignService(getAssignService());
  bean.setBillTempNodekey("assign");
  bean.setBillTemplatePkItemkey("pk_customer");
  bean.setLogincontext(getContext());
  bean.setOrgTypeIDs(getManagedList17());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList17(){  List list = new ArrayList();  list.add("BUSINESSUNIT00000000");  list.add("CREDITCTLREGION00000");  return list;}

public nc.ui.bd.cust.baseinfo.service.CustAssignService getAssignService(){
 if(context.get("assignService")!=null)
 return (nc.ui.bd.cust.baseinfo.service.CustAssignService)context.get("assignService");
  nc.ui.bd.cust.baseinfo.service.CustAssignService bean = new nc.ui.bd.cust.baseinfo.service.CustAssignService();
  context.put("assignService",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.BankaccAction getBankaccAction(){
 if(context.get("bankaccAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.BankaccAction)context.get("bankaccAction");
  nc.ui.bd.cust.baseinfo.action.BankaccAction bean = new nc.ui.bd.cust.baseinfo.action.BankaccAction();
  context.put("bankaccAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setFunnode("10140CBA");
  bean.setRefreshSingleAction(getRefreshSingleAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustAddressAction getCustaddressAction(){
 if(context.get("custaddressAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustAddressAction)context.get("custaddressAction");
  nc.ui.bd.cust.baseinfo.action.CustAddressAction bean = new nc.ui.bd.cust.baseinfo.action.CustAddressAction();
  context.put("custaddressAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setFunnode("10140CAD");
  bean.setRefreshSingleAction(getRefreshSingleAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CreateSupplierAction getCreateSupplierAction(){
 if(context.get("createSupplierAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CreateSupplierAction)context.get("createSupplierAction");
  nc.ui.bd.cust.baseinfo.action.CreateSupplierAction bean = new nc.ui.bd.cust.baseinfo.action.CreateSupplierAction();
  context.put("createSupplierAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCreateSupModel(getCreateViewModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.editor.CreateSupplierEditor getCreateSupView(){
 if(context.get("createSupView")!=null)
 return (nc.ui.bd.cust.baseinfo.editor.CreateSupplierEditor)context.get("createSupView");
  nc.ui.bd.cust.baseinfo.editor.CreateSupplierEditor bean = new nc.ui.bd.cust.baseinfo.editor.CreateSupplierEditor();
  context.put("createSupView",bean);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("createSupplier");
  bean.setModel(getCreateViewModel());
  bean.setBaseModel(getBaseinfoModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.model.BillManageModel getCreateViewModel(){
 if(context.get("createViewModel")!=null)
 return (nc.ui.uif2.model.BillManageModel)context.get("createViewModel");
  nc.ui.uif2.model.BillManageModel bean = new nc.ui.uif2.model.BillManageModel();
  context.put("createViewModel",bean);
  bean.setBusinessObjectAdapterFactory(getBoadapterfacotry());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CreateSupplierSaveAction getCreateSupSaveAction(){
 if(context.get("createSupSaveAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CreateSupplierSaveAction)context.get("createSupSaveAction");
  nc.ui.bd.cust.baseinfo.action.CreateSupplierSaveAction bean = new nc.ui.bd.cust.baseinfo.action.CreateSupplierSaveAction();
  context.put("createSupSaveAction",bean);
  bean.setModel(getCreateViewModel());
  bean.setEditor(getCreateSupView());
  bean.setBaseModel(getBaseinfoModel());
  bean.setValidationService(getDefaultValidationService_ef250());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.bs.uif2.validation.DefaultValidationService getDefaultValidationService_ef250(){
 if(context.get("nc.bs.uif2.validation.DefaultValidationService#ef250")!=null)
 return (nc.bs.uif2.validation.DefaultValidationService)context.get("nc.bs.uif2.validation.DefaultValidationService#ef250");
  nc.bs.uif2.validation.DefaultValidationService bean = new nc.bs.uif2.validation.DefaultValidationService();
  context.put("nc.bs.uif2.validation.DefaultValidationService#ef250",bean);
  bean.setValidators(getManagedList18());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList18(){  List list = new ArrayList();  list.add(getSupplierNotNullValidator_198e993());  return list;}

private nc.bs.bd.supplier.baseinfo.validator.SupplierNotNullValidator getSupplierNotNullValidator_198e993(){
 if(context.get("nc.bs.bd.supplier.baseinfo.validator.SupplierNotNullValidator#198e993")!=null)
 return (nc.bs.bd.supplier.baseinfo.validator.SupplierNotNullValidator)context.get("nc.bs.bd.supplier.baseinfo.validator.SupplierNotNullValidator#198e993");
  nc.bs.bd.supplier.baseinfo.validator.SupplierNotNullValidator bean = new nc.bs.bd.supplier.baseinfo.validator.SupplierNotNullValidator();
  context.put("nc.bs.bd.supplier.baseinfo.validator.SupplierNotNullValidator#198e993",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CreateSupplierCancelAction getCreateSupCancelAction(){
 if(context.get("createSupCancelAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CreateSupplierCancelAction)context.get("createSupCancelAction");
  nc.ui.bd.cust.baseinfo.action.CreateSupplierCancelAction bean = new nc.ui.bd.cust.baseinfo.action.CreateSupplierCancelAction();
  context.put("createSupCancelAction",bean);
  bean.setModel(getCreateViewModel());
  bean.setEditor(getCreateSupView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator getCreateSupDlgBaseMediator(){
 if(context.get("createSupDlgBaseMediator")!=null)
 return (nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator)context.get("createSupDlgBaseMediator");
  nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator bean = new nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator();
  context.put("createSupDlgBaseMediator",bean);
  bean.setName(getI18nFB_1468411());
  bean.setModel(getCreateViewModel());
  bean.setEditor(getCreateSupView());
  bean.setSaveAction(getCreateSupSaveAction());
  bean.setCancelAction(getCreateSupCancelAction());
  bean.setWidth(350);
  bean.setHeight(400);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1468411(){
 if(context.get("nc.ui.uif2.I18nFB#1468411")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1468411");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1468411",bean);  bean.setResDir("10140cub");
  bean.setDefaultValue("生成供应商");
  bean.setResId("010140cub0006");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1468411",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.cust.baseinfo.action.AssociateSupplierAction getAssociateSupAction(){
 if(context.get("associateSupAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.AssociateSupplierAction)context.get("associateSupAction");
  nc.ui.bd.cust.baseinfo.action.AssociateSupplierAction bean = new nc.ui.bd.cust.baseinfo.action.AssociateSupplierAction();
  context.put("associateSupAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setAssociateModel(getAssociateSupModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.model.BillManageModel getAssociateSupModel(){
 if(context.get("associateSupModel")!=null)
 return (nc.ui.uif2.model.BillManageModel)context.get("associateSupModel");
  nc.ui.uif2.model.BillManageModel bean = new nc.ui.uif2.model.BillManageModel();
  context.put("associateSupModel",bean);
  bean.setBusinessObjectAdapterFactory(getBoadapterfacotry());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.editor.AssociateSupplierEditor getAssociateSupEditor(){
 if(context.get("associateSupEditor")!=null)
 return (nc.ui.bd.cust.baseinfo.editor.AssociateSupplierEditor)context.get("associateSupEditor");
  nc.ui.bd.cust.baseinfo.editor.AssociateSupplierEditor bean = new nc.ui.bd.cust.baseinfo.editor.AssociateSupplierEditor();
  context.put("associateSupEditor",bean);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("associateSup");
  bean.setModel(getAssociateSupModel());
  bean.setBaseModel(getBaseinfoModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator getAssociateSupDlgBaseMediator(){
 if(context.get("associateSupDlgBaseMediator")!=null)
 return (nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator)context.get("associateSupDlgBaseMediator");
  nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator bean = new nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator();
  context.put("associateSupDlgBaseMediator",bean);
  bean.setName(getI18nFB_88ab02());
  bean.setModel(getAssociateSupModel());
  bean.setEditor(getAssociateSupEditor());
  bean.setSaveAction(getAssociateSupplierSaveAction_93261e());
  bean.setCancelAction(getCancelAction_abb656());
  bean.setWidth(350);
  bean.setHeight(400);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_88ab02(){
 if(context.get("nc.ui.uif2.I18nFB#88ab02")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#88ab02");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#88ab02",bean);  bean.setResDir("10140cub");
  bean.setDefaultValue("关联供应商");
  bean.setResId("010140cub0000");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#88ab02",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.bd.cust.baseinfo.action.AssociateSupplierSaveAction getAssociateSupplierSaveAction_93261e(){
 if(context.get("nc.ui.bd.cust.baseinfo.action.AssociateSupplierSaveAction#93261e")!=null)
 return (nc.ui.bd.cust.baseinfo.action.AssociateSupplierSaveAction)context.get("nc.ui.bd.cust.baseinfo.action.AssociateSupplierSaveAction#93261e");
  nc.ui.bd.cust.baseinfo.action.AssociateSupplierSaveAction bean = new nc.ui.bd.cust.baseinfo.action.AssociateSupplierSaveAction();
  context.put("nc.ui.bd.cust.baseinfo.action.AssociateSupplierSaveAction#93261e",bean);
  bean.setModel(getAssociateSupModel());
  bean.setEditor(getAssociateSupEditor());
  bean.setBaseModel(getBaseinfoModel());
  bean.setBaseEditor(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.CancelAction getCancelAction_abb656(){
 if(context.get("nc.ui.uif2.actions.CancelAction#abb656")!=null)
 return (nc.ui.uif2.actions.CancelAction)context.get("nc.ui.uif2.actions.CancelAction#abb656");
  nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
  context.put("nc.ui.uif2.actions.CancelAction#abb656",bean);
  bean.setModel(getAssociateSupModel());
  bean.setEditor(getAssociateSupEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.query.OrgBrowseAction getOrgBrowseAction(){
 if(context.get("orgBrowseAction")!=null)
 return (nc.ui.bd.pub.query.OrgBrowseAction)context.get("orgBrowseAction");
  nc.ui.bd.pub.query.OrgBrowseAction bean = new nc.ui.bd.pub.query.OrgBrowseAction();
  context.put("orgBrowseAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setFunnode("10140CUQ");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustAssociatePfAction getAssociatePfCustAction(){
 if(context.get("associatePfCustAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustAssociatePfAction)context.get("associatePfCustAction");
  nc.ui.bd.cust.baseinfo.action.CustAssociatePfAction bean = new nc.ui.bd.cust.baseinfo.action.CustAssociatePfAction();
  context.put("associatePfCustAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setApproveModelName("approveModel");
  bean.setApproveXmlFilePath("nc/ui/bd/cust/config/Customer_Associate_pf.xml");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.AccessoryAction getAccessoryAction(){
 if(context.get("accessoryAction")!=null)
 return (nc.ui.bd.pub.actions.AccessoryAction)context.get("accessoryAction");
  nc.ui.bd.pub.actions.AccessoryAction bean = new nc.ui.bd.pub.actions.AccessoryAction();
  context.put("accessoryAction",bean);
  bean.setWindowListener(getAccessoryShowAction());
  bean.setModel(getBaseinfoModel());
  bean.setMetaDataID("e4f48eaf-5567-4383-a370-a59cb3e8a451");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintActionGroup(){
 if(context.get("printActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printActionGroup",bean);
  bean.setCode("PrintGroup");
  bean.setActions(getManagedList19());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList19(){  List list = new ArrayList();  list.add(getTempletPrintAction());  list.add(getTempletPreviewAction());  list.add(getOutPutAction());  return list;}

public nc.ui.uif2.actions.TemplatePreviewAction getTempletPreviewAction(){
 if(context.get("templetPreviewAction")!=null)
 return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("templetPreviewAction");
  nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
  context.put("templetPreviewAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setNodeKey("basecard");
  bean.setDatasource(getSingelDatasource());
  bean.setPrintDlgParentConatiner(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.TemplatePrintAction getTempletPrintAction(){
 if(context.get("templetPrintAction")!=null)
 return (nc.ui.uif2.actions.TemplatePrintAction)context.get("templetPrintAction");
  nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
  context.put("templetPrintAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setNodeKey("basecard");
  bean.setDatasource(getSingelDatasource());
  bean.setPrintDlgParentConatiner(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.OutputAction getOutPutAction(){
 if(context.get("outPutAction")!=null)
 return (nc.ui.uif2.actions.OutputAction)context.get("outPutAction");
  nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
  context.put("outPutAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setNodeKey("basecard");
  bean.setDatasource(getSingelDatasource());
  bean.setPrintDlgParentConatiner(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getSingelDatasource(){
 if(context.get("singelDatasource")!=null)
 return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("singelDatasource");
  nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
  context.put("singelDatasource",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getListPrintActionGroup(){
 if(context.get("listPrintActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("listPrintActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("listPrintActionGroup",bean);
  bean.setCode("PrintGroup");
  bean.setActions(getManagedList20());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList20(){  List list = new ArrayList();  list.add(getListTempletprintAction());  list.add(getListTempletPreviewAction());  list.add(getListOutputAction());  return list;}

public nc.ui.bd.pub.actions.print.BDTemplatePaginationPreviewAction getListTempletPreviewAction(){
 if(context.get("listTempletPreviewAction")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePaginationPreviewAction)context.get("listTempletPreviewAction");
  nc.ui.bd.pub.actions.print.BDTemplatePaginationPreviewAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePaginationPreviewAction();
  context.put("listTempletPreviewAction",bean);
  bean.setPrintAction(getListTempletprintAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDTemplatePaginationPrintAction getListTempletprintAction(){
 if(context.get("listTempletprintAction")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePaginationPrintAction)context.get("listTempletprintAction");
  nc.ui.bd.pub.actions.print.BDTemplatePaginationPrintAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePaginationPrintAction();
  context.put("listTempletprintAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setNodeKey("baselist");
  bean.setPaginationModel(getPaginationModel());
  bean.setPrintFactory(getPrintFactory());
  bean.setPrintDlgParentConatiner(getBaseinfoListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDPaginationOutputAction getListOutputAction(){
 if(context.get("listOutputAction")!=null)
 return (nc.ui.bd.pub.actions.print.BDPaginationOutputAction)context.get("listOutputAction");
  nc.ui.bd.pub.actions.print.BDPaginationOutputAction bean = new nc.ui.bd.pub.actions.print.BDPaginationOutputAction();
  context.put("listOutputAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setNodeKey("baselist");
  bean.setPaginationModel(getPaginationModel());
  bean.setPrintFactory(getPrintFactory());
  bean.setPrintDlgParentConatiner(getBaseinfoListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.DefaultTemplatePagePrintFactory getPrintFactory(){
 if(context.get("printFactory")!=null)
 return (nc.ui.bd.pub.actions.print.DefaultTemplatePagePrintFactory)context.get("printFactory");
  nc.ui.bd.pub.actions.print.DefaultTemplatePagePrintFactory bean = new nc.ui.bd.pub.actions.print.DefaultTemplatePagePrintFactory();
  context.put("printFactory",bean);
  bean.setMdId("e4f48eaf-5567-4383-a370-a59cb3e8a451");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getAssistantFuncMenu_card(){
 if(context.get("assistantFuncMenu_card")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("assistantFuncMenu_card");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("assistantFuncMenu_card",bean);
  bean.setName(getI18nFB_14fd5c7());
  bean.setCode("AssistantFuncMenu");
  bean.setActions(getManagedList21());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_14fd5c7(){
 if(context.get("nc.ui.uif2.I18nFB#14fd5c7")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#14fd5c7");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#14fd5c7",bean);  bean.setResDir("common");
  bean.setDefaultValue("辅助功能");
  bean.setResId("UC001-0000137");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#14fd5c7",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList21(){  List list = new ArrayList();  list.add(getCreateSupplierAction());  list.add(getAssociateSupAction());  list.add(getAccessoryAction());  return list;}

public nc.funcnode.ui.action.MenuAction getAssistantFuncMenu(){
 if(context.get("assistantFuncMenu")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("assistantFuncMenu");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("assistantFuncMenu",bean);
  bean.setName(getI18nFB_5c3d1d());
  bean.setCode("AssistantFuncMenu");
  bean.setActions(getManagedList22());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_5c3d1d(){
 if(context.get("nc.ui.uif2.I18nFB#5c3d1d")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#5c3d1d");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#5c3d1d",bean);  bean.setResDir("common");
  bean.setDefaultValue("辅助功能");
  bean.setResId("UC001-0000137");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#5c3d1d",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList22(){  List list = new ArrayList();  list.add(getCreateSupplierAction());  list.add(getAssociateSupAction());  list.add(getBatchcreateSupplierAction());  list.add(getAccessoryAction());  return list;}

public nc.funcnode.ui.action.GroupAction getCard_enableGroupAction(){
 if(context.get("card_enableGroupAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("card_enableGroupAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("card_enableGroupAction",bean);
  bean.setCode("EnableGroup");
  bean.setActions(getManagedList23());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList23(){  List list = new ArrayList();  list.add(getCard_enableAction());  list.add(getCard_disableAction());  return list;}

public nc.ui.bd.cust.baseinfo.action.CustBaseInfoDisableAction getCard_disableAction(){
 if(context.get("card_disableAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseInfoDisableAction)context.get("card_disableAction");
  nc.ui.bd.cust.baseinfo.action.CustBaseInfoDisableAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseInfoDisableAction();
  context.put("card_disableAction",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustBaseInfoEnableAction getCard_enableAction(){
 if(context.get("card_enableAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseInfoEnableAction)context.get("card_enableAction");
  nc.ui.bd.cust.baseinfo.action.CustBaseInfoEnableAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseInfoEnableAction();
  context.put("card_enableAction",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getEnableGroupAction(){
 if(context.get("enableGroupAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("enableGroupAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("enableGroupAction",bean);
  bean.setCode("EnableGroup");
  bean.setActions(getManagedList24());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList24(){  List list = new ArrayList();  list.add(getBaseinfoEnableAction());  list.add(getBaseinfoDisableAction());  return list;}

public nc.ui.bd.cust.baseinfo.action.CustBaseInfoBatchDisableAction getBaseinfoDisableAction(){
 if(context.get("baseinfoDisableAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseInfoBatchDisableAction)context.get("baseinfoDisableAction");
  nc.ui.bd.cust.baseinfo.action.CustBaseInfoBatchDisableAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseInfoBatchDisableAction();
  context.put("baseinfoDisableAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setResourceCode("customer");
  bean.setMdOperateCode("disable");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustBaseInfoBatchEnableAction getBaseinfoEnableAction(){
 if(context.get("baseinfoEnableAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseInfoBatchEnableAction)context.get("baseinfoEnableAction");
  nc.ui.bd.cust.baseinfo.action.CustBaseInfoBatchEnableAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseInfoBatchEnableAction();
  context.put("baseinfoEnableAction",bean);
  bean.setModel(getBaseinfoModel());
  bean.setResourceCode("customer");
  bean.setMdOperateCode("enable");
  bean.setValidationService(getEnablevalidationService());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.validator.CustDeleteValidator getEnablevalidationService(){
 if(context.get("enablevalidationService")!=null)
 return (nc.ui.bd.cust.baseinfo.validator.CustDeleteValidator)context.get("enablevalidationService");
  nc.ui.bd.cust.baseinfo.validator.CustDeleteValidator bean = new nc.ui.bd.cust.baseinfo.validator.CustDeleteValidator();
  context.put("enablevalidationService",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getCard_freezegroupaction(){
 if(context.get("card_freezegroupaction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("card_freezegroupaction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("card_freezegroupaction",bean);
  bean.setCode("FreezeGroup");
  bean.setActions(getManagedList25());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList25(){  List list = new ArrayList();  list.add(getCard_freezeaction());  list.add(getCard_unfreezeaction());  return list;}

public nc.ui.bd.cust.baseinfo.action.CustBaseInfoFreezeAction getCard_freezeaction(){
 if(context.get("card_freezeaction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseInfoFreezeAction)context.get("card_freezeaction");
  nc.ui.bd.cust.baseinfo.action.CustBaseInfoFreezeAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseInfoFreezeAction();
  context.put("card_freezeaction",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustBaseInfoUnFreezeAction getCard_unfreezeaction(){
 if(context.get("card_unfreezeaction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBaseInfoUnFreezeAction)context.get("card_unfreezeaction");
  nc.ui.bd.cust.baseinfo.action.CustBaseInfoUnFreezeAction bean = new nc.ui.bd.cust.baseinfo.action.CustBaseInfoUnFreezeAction();
  context.put("card_unfreezeaction",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getFreezegroupaction(){
 if(context.get("freezegroupaction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("freezegroupaction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("freezegroupaction",bean);
  bean.setCode("FreezeGroup");
  bean.setActions(getManagedList26());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList26(){  List list = new ArrayList();  list.add(getList_freezeaction());  list.add(getList_unfreezeaction());  return list;}

public nc.ui.bd.cust.baseinfo.action.CustBatchFreezeAction getList_freezeaction(){
 if(context.get("list_freezeaction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBatchFreezeAction)context.get("list_freezeaction");
  nc.ui.bd.cust.baseinfo.action.CustBatchFreezeAction bean = new nc.ui.bd.cust.baseinfo.action.CustBatchFreezeAction();
  context.put("list_freezeaction",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.cust.baseinfo.action.CustBatchUnFreezeAction getList_unfreezeaction(){
 if(context.get("list_unfreezeaction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.CustBatchUnFreezeAction)context.get("list_unfreezeaction");
  nc.ui.bd.cust.baseinfo.action.CustBatchUnFreezeAction bean = new nc.ui.bd.cust.baseinfo.action.CustBatchUnFreezeAction();
  context.put("list_unfreezeaction",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayoutRoot(getTBNode_1421f7d());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_1421f7d(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#1421f7d")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#1421f7d");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#1421f7d",bean);
  bean.setTabs(getManagedList27());
  bean.setShowMode("CardLayout");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList27(){  List list = new ArrayList();  list.add(getHSNode_135a682());  list.add(getVSNode_1d5c42b());  return list;}

private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_135a682(){
 if(context.get("nc.ui.uif2.tangramlayout.node.HSNode#135a682")!=null)
 return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#135a682");
  nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
  context.put("nc.ui.uif2.tangramlayout.node.HSNode#135a682",bean);
  bean.setLeft(getCNode_1b06529());
  bean.setRight(getCNode_1c17a13());
  bean.setDividerLocation(0.2f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1b06529(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1b06529")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1b06529");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1b06529",bean);
  bean.setComponent(getQueryAreaShell());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1c17a13(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1c17a13")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1c17a13");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1c17a13",bean);
  bean.setComponent(getBaseinfoListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_1d5c42b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#1d5c42b")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#1d5c42b");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#1d5c42b",bean);
  bean.setUp(getCNode_146acb5());
  bean.setDown(getVSNode_850049());
  bean.setShowMode("NoDivider");
  bean.setDividerLocation(36f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_146acb5(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#146acb5")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#146acb5");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#146acb5",bean);
  bean.setComponent(getCardInfoPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_850049(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#850049")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#850049");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#850049",bean);
  bean.setUp(getCNode_f4c194());
  bean.setDown(getId());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_f4c194(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#f4c194")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#f4c194");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#f4c194",bean);
  bean.setComponent(getSharepanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getId(){
 if(context.get("id")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("id");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("id",bean);
  bean.setTabs(getManagedList28());
  bean.setTabbedPaneFetcher(getTabbedPaneFetcher());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList28(){  List list = new ArrayList();  list.add(getCNode_12a8cd9());  return list;}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_12a8cd9(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#12a8cd9")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#12a8cd9");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#12a8cd9",bean);
  bean.setName(getI18nFB_d1071f());
  bean.setComponent(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_d1071f(){
 if(context.get("nc.ui.uif2.I18nFB#d1071f")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#d1071f");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#d1071f",bean);  bean.setResDir("10140cub");
  bean.setDefaultValue("基本信息");
  bean.setResId("010140cub0037");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#d1071f",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.pub.DefaultTabbedPaneEditableControl getTabbedPaneFetcher(){
 if(context.get("tabbedPaneFetcher")!=null)
 return (nc.ui.bd.pub.DefaultTabbedPaneEditableControl)context.get("tabbedPaneFetcher");
  nc.ui.bd.pub.DefaultTabbedPaneEditableControl bean = new nc.ui.bd.pub.DefaultTabbedPaneEditableControl();
  context.put("tabbedPaneFetcher",bean);
  bean.setModel(getBaseinfoModel());
  bean.setTargetComponent(getBaseinfoEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.QueryAreaShell getQueryAreaShell(){
 if(context.get("queryAreaShell")!=null)
 return (nc.ui.uif2.actions.QueryAreaShell)context.get("queryAreaShell");
  nc.ui.uif2.actions.QueryAreaShell bean = new nc.ui.uif2.actions.QueryAreaShell();
  context.put("queryAreaShell",bean);
  bean.setQueryArea(getBdqueryActionBaseMediator_created_215aa7());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.queryarea.QueryArea getBdqueryActionBaseMediator_created_215aa7(){
 if(context.get("bdqueryActionBaseMediator.created#215aa7")!=null)
 return (nc.ui.queryarea.QueryArea)context.get("bdqueryActionBaseMediator.created#215aa7");
  nc.ui.queryarea.QueryArea bean = getBdqueryActionBaseMediator().createQueryArea();
  context.put("bdqueryActionBaseMediator.created#215aa7",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getBaseinfoListViewActions(){
 if(context.get("baseinfoListViewActions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("baseinfoListViewActions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBaseinfoListView());  context.put("baseinfoListViewActions",bean);
  bean.setActions(getManagedList29());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList29(){  List list = new ArrayList();  list.add(getAddAction());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getCopyAddAction());  list.add(getBatchUpdateGroupAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getRefreshAction());  list.add(getFilterAction());  list.add(getSeparatorAction());  list.add(getAssignActionGroup());  list.add(getBankaccAction());  list.add(getCustaddressAction());  list.add(getFreezegroupaction());  list.add(getEnableGroupAction());  list.add(getAssistantFuncMenu());  list.add(getSeparatorAction());  list.add(getOrgBrowseAction());  list.add(getAssociatePfCustAction());  list.add(getSeparatorAction());  list.add(getListPrintActionGroup());  list.add(getSeparatorAction());  list.add(getMdmAction());  return list;}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getBaseinfoEditorActions(){
 if(context.get("baseinfoEditorActions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("baseinfoEditorActions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBaseinfoEditor());  context.put("baseinfoEditorActions",bean);
  bean.setActions(getManagedList30());
  bean.setEditActions(getManagedList31());
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList30(){  List list = new ArrayList();  list.add(getAddAction());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getCopyAddAction());  list.add(getBatchUpdateGroupAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getRefreshSingleAction());  list.add(getSeparatorAction());  list.add(getAssignActionGroup());  list.add(getBankaccAction());  list.add(getCustaddressAction());  list.add(getCard_freezegroupaction());  list.add(getCard_enableGroupAction());  list.add(getAssistantFuncMenu_card());  list.add(getSeparatorAction());  list.add(getOrgBrowseAction());  list.add(getAssociatePfCustAction());  list.add(getSeparatorAction());  list.add(getPrintActionGroup());  list.add(getSeparatorAction());  list.add(getMdmAction());  return list;}

private List getManagedList31(){  List list = new ArrayList();  list.add(getSaveAction());  list.add(getSaveAddAction());  list.add(getSeparatorAction());  list.add(getCancelAction());  return list;}

public nc.ui.uif2.FunNodeClosingHandler getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.uif2.FunNodeClosingHandler)context.get("ClosingListener");
  nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
  context.put("ClosingListener",bean);
  bean.setModel(getBaseinfoModel());
  bean.setSaveaction(getSaveAction());
  bean.setCancelaction(getCancelAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.extend.ExtendContext getBaseExtendContext(){
 if(context.get("baseExtendContext")!=null)
 return (nc.ui.bd.pub.extend.ExtendContext)context.get("baseExtendContext");
  nc.ui.bd.pub.extend.ExtendContext bean = new nc.ui.bd.pub.extend.ExtendContext();
  context.put("baseExtendContext",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.extend.ExtendOrgBaseUIProcessMediator getBaseExtendUIPrcBaseMediator(){
 if(context.get("baseExtendUIPrcBaseMediator")!=null)
 return (nc.ui.bd.pub.extend.ExtendOrgBaseUIProcessMediator)context.get("baseExtendUIPrcBaseMediator");
  nc.ui.bd.pub.extend.ExtendOrgBaseUIProcessMediator bean = new nc.ui.bd.pub.extend.ExtendOrgBaseUIProcessMediator();
  context.put("baseExtendUIPrcBaseMediator",bean);
  bean.setBaseEditor(getBaseinfoEditor());
  bean.setExtendContext(getBaseExtendContext());
  bean.setBaseTabName(getI18nFB_1c69651());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1c69651(){
 if(context.get("nc.ui.uif2.I18nFB#1c69651")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1c69651");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1c69651",bean);  bean.setResDir("10140cub");
  bean.setDefaultValue("基本信息");
  bean.setResId("010140cub0037");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1c69651",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.pub.tools.BDPubQueryActionMediator getBdqueryActionBaseMediator(){
 if(context.get("bdqueryActionBaseMediator")!=null)
 return (nc.ui.bd.pub.tools.BDPubQueryActionMediator)context.get("bdqueryActionBaseMediator");
  nc.ui.bd.pub.tools.BDPubQueryActionMediator bean = new nc.ui.bd.pub.tools.BDPubQueryActionMediator();
  context.put("bdqueryActionBaseMediator",bean);
  bean.setQueryAction(getQueryAction());
  bean.setOrgFieldCode(getManagedList32());
  bean.process();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList32(){  List list = new ArrayList();  list.add("pk_org_assign");  return list;}

public nc.ui.bd.cust.baseinfo.action.BatchCreateSupAction getBatchcreateSupplierAction(){
 if(context.get("batchcreateSupplierAction")!=null)
 return (nc.ui.bd.cust.baseinfo.action.BatchCreateSupAction)context.get("batchcreateSupplierAction");
  nc.ui.bd.cust.baseinfo.action.BatchCreateSupAction bean = new nc.ui.bd.cust.baseinfo.action.BatchCreateSupAction();
  context.put("batchcreateSupplierAction",bean);
  bean.setCustBaseModel(getBaseinfoModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.archives.action.CustomerMdmAction getMdmAction(){
 if(context.get("mdmAction")!=null)
 return (nc.ui.archives.action.CustomerMdmAction)context.get("mdmAction");
  nc.ui.archives.action.CustomerMdmAction bean = new nc.ui.archives.action.CustomerMdmAction();
  context.put("mdmAction",bean);
  bean.setModel(getBaseinfoModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
