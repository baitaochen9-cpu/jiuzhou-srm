package nc.ui.bd.material.pf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class material_pf_config extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.bd.uitabextend.ExinfoLoader getExinfoloader(){
 if(context.get("exinfoloader")!=null)
 return (nc.ui.bd.uitabextend.ExinfoLoader)context.get("exinfoloader");
  nc.ui.bd.uitabextend.ExinfoLoader bean = new nc.ui.bd.uitabextend.ExinfoLoader();
  context.put("exinfoloader",bean);
  bean.setCurrent_md_ID("10140MPF");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.uitabextend.UITabExtManager getUiTabExtMnger(){
 if(context.get("uiTabExtMnger")!=null)
 return (nc.ui.bd.uitabextend.UITabExtManager)context.get("uiTabExtMnger");
  nc.ui.bd.uitabextend.UITabExtManager bean = new nc.ui.bd.uitabextend.UITabExtManager();
  context.put("uiTabExtMnger",bean);
  bean.setTargetComponent(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.uif2.LoginContext getContext(){
 if(context.get("context")!=null)
 return (nc.vo.uif2.LoginContext)context.get("context");
  nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
  context.put("context",bean);
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

private List getManagedList0(){  List list = new ArrayList();  list.add(getTemplateContainer());  list.add(getUserdefitemContainer());  list.add(getQueryTemplateContainer());  return list;}

public nc.ui.uif2.editor.TemplateContainer getTemplateContainer(){
 if(context.get("templateContainer")!=null)
 return (nc.ui.uif2.editor.TemplateContainer)context.get("templateContainer");
  nc.ui.uif2.editor.TemplateContainer bean = new nc.ui.uif2.editor.TemplateContainer();
  context.put("templateContainer",bean);
  bean.setContext(getContext());
  bean.setNodeKeies(getManagedList1());
  bean.load();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add("approve");  list.add("data");  list.add("asstframe");  list.add("assistant");  list.add("asstdefine");  return list;}

public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer(){
 if(context.get("userdefitemContainer")!=null)
 return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
  nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
  context.put("userdefitemContainer",bean);
  bean.setContext(getContext());
  bean.setParams(getManagedList2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getQueryParam_12d484f());  list.add(getQueryParam_1e66c60());  list.add(getQueryParam_d545dd());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_12d484f(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#12d484f")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#12d484f");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#12d484f",bean);
  bean.setMdfullname("uap.material");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1e66c60(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#1e66c60")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#1e66c60");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#1e66c60",bean);
  bean.setRulecode("materialassistant");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_d545dd(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#d545dd")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#d545dd");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#d545dd",bean);
  bean.setMdfullname("uap.material_pf");
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

public nc.ui.bd.material.pf.model.MaterialPfModelService getManageModelService(){
 if(context.get("ManageModelService")!=null)
 return (nc.ui.bd.material.pf.model.MaterialPfModelService)context.get("ManageModelService");
  nc.ui.bd.material.pf.model.MaterialPfModelService bean = new nc.ui.bd.material.pf.model.MaterialPfModelService();
  context.put("ManageModelService",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.GeneralBDObjectAdapterFactory getBoadatorfactory(){
 if(context.get("boadatorfactory")!=null)
 return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("boadatorfactory");
  nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
  context.put("boadatorfactory",bean);
  bean.setMode("MD");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.model.BillManageModel getApproveModel(){
 if(context.get("approveModel")!=null)
 return (nc.ui.uif2.model.BillManageModel)context.get("approveModel");
  nc.ui.uif2.model.BillManageModel bean = new nc.ui.uif2.model.BillManageModel();
  context.put("approveModel",bean);
  bean.setService(getManageModelService());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel getBaseinfoModel(){
 if(context.get("baseinfoModel")!=null)
 return (nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel)context.get("baseinfoModel");
  nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel bean = new nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel();
  context.put("baseinfoModel",bean);
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setService(getManageModelService());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.model.MaterialPFModelMediator getModelMediator(){
 if(context.get("modelMediator")!=null)
 return (nc.ui.bd.material.pf.model.MaterialPFModelMediator)context.get("modelMediator");
  nc.ui.bd.material.pf.model.MaterialPFModelMediator bean = new nc.ui.bd.material.pf.model.MaterialPFModelMediator();
  context.put("modelMediator",bean);
  bean.setBaseModel(getBaseinfoModel());
  bean.setPfModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.model.MaterialPfModelDataManager getModelDataManager(){
 if(context.get("modelDataManager")!=null)
 return (nc.ui.bd.material.pf.model.MaterialPfModelDataManager)context.get("modelDataManager");
  nc.ui.bd.material.pf.model.MaterialPfModelDataManager bean = new nc.ui.bd.material.pf.model.MaterialPfModelDataManager();
  context.put("modelDataManager",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.model.MaterialPfFuncNodeInitDataListener getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.bd.material.pf.model.MaterialPfFuncNodeInitDataListener)context.get("InitDataListener");
  nc.ui.bd.material.pf.model.MaterialPfFuncNodeInitDataListener bean = new nc.ui.bd.material.pf.model.MaterialPfFuncNodeInitDataListener();
  context.put("InitDataListener",bean);
  bean.setDataManager(getModelDataManager());
  bean.setModel(getApproveModel());
  bean.setModelMediator(getModelMediator());
  bean.setEditor(getApproveEditor());
  bean.setQueryAction(getQueryAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.ActionContributors getToftpanelActionContributors(){
 if(context.get("toftpanelActionContributors")!=null)
 return (nc.ui.uif2.actions.ActionContributors)context.get("toftpanelActionContributors");
  nc.ui.uif2.actions.ActionContributors bean = new nc.ui.uif2.actions.ActionContributors();
  context.put("toftpanelActionContributors",bean);
  bean.setContributors(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList3(){  List list = new ArrayList();  list.add(getListViewActions());  list.add(getCardPanelActions());  return list;}

public nc.ui.bd.material.pf.view.MaterialPfListView getListView(){
 if(context.get("listView")!=null)
 return (nc.ui.bd.material.pf.view.MaterialPfListView)context.get("listView");
  nc.ui.bd.material.pf.view.MaterialPfListView bean = new nc.ui.bd.material.pf.view.MaterialPfListView();
  context.put("listView",bean);
  bean.setModel(getApproveModel());
  bean.setMultiSelectionEnable(true);
  bean.setNodekey("approve");
  bean.setPos("head");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNorth(getListInfoPnl());
  bean.setUserdefitemListPreparator(getCompositeBillListDataPrepare_1ddc729());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getCompositeBillListDataPrepare_1ddc729(){
 if(context.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#1ddc729")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare)context.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#1ddc729");
  nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
  context.put("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#1ddc729",bean);
  bean.setBillListDataPrepares(getManagedList4());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList4(){  List list = new ArrayList();  list.add(getUserdefitemlistPreparator());  return list;}

public nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemlistPreparator(){
 if(context.get("userdefitemlistPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("userdefitemlistPreparator");
  nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
  context.put("userdefitemlistPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList5());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add(getUserdefQueryParam_13e88fe());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_13e88fe(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#13e88fe")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#13e88fe");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#13e88fe",bean);
  bean.setMdfullname("uap.material_pf");
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
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.attach.AttachCtrlAction getAccessoryShowAction(){
 if(context.get("accessoryShowAction")!=null)
 return (nc.ui.bd.attach.AttachCtrlAction)context.get("accessoryShowAction");
  nc.ui.bd.attach.AttachCtrlAction bean = new nc.ui.bd.attach.AttachCtrlAction();
  context.put("accessoryShowAction",bean);
  bean.setModel(getApproveModel());
  bean.setPrefix("uapbd/6b2c8309-930b-4989-8b0e-f2a80f35c62c");
  bean.setBilltype("10WL");
  bean.setResourceCode("materialpf");
  bean.setMdOperateCode("edit");
  bean.setCheckPermission(true);
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
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add(getAccessoryShowAction());  list.add(getFirstLineAction());  list.add(getPreLineAction());  list.add(getNextLineAction());  list.add(getLastLineAction());  return list;}

private nc.ui.uif2.actions.FirstLineAction getFirstLineAction(){
 if(context.get("firstLineAction")!=null)
 return (nc.ui.uif2.actions.FirstLineAction)context.get("firstLineAction");
  nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
  context.put("firstLineAction",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.PreLineAction getPreLineAction(){
 if(context.get("preLineAction")!=null)
 return (nc.ui.uif2.actions.PreLineAction)context.get("preLineAction");
  nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
  context.put("preLineAction",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.NextLineAction getNextLineAction(){
 if(context.get("nextLineAction")!=null)
 return (nc.ui.uif2.actions.NextLineAction)context.get("nextLineAction");
  nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
  context.put("nextLineAction",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.LastLineAction getLastLineAction(){
 if(context.get("lastLineAction")!=null)
 return (nc.ui.uif2.actions.LastLineAction)context.get("lastLineAction");
  nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
  context.put("lastLineAction",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.ShowMeUpAction getReturnaction(){
 if(context.get("returnaction")!=null)
 return (nc.ui.uif2.actions.ShowMeUpAction)context.get("returnaction");
  nc.ui.uif2.actions.ShowMeUpAction bean = new nc.ui.uif2.actions.ShowMeUpAction();
  context.put("returnaction",bean);
  bean.setGoComponent(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.view.MaterialPfApproveEditor getApproveEditor(){
 if(context.get("approveEditor")!=null)
 return (nc.ui.bd.material.pf.view.MaterialPfApproveEditor)context.get("approveEditor");
  nc.ui.bd.material.pf.view.MaterialPfApproveEditor bean = new nc.ui.bd.material.pf.view.MaterialPfApproveEditor();
  context.put("approveEditor",bean);
  bean.setModel(getApproveModel());
  bean.setNodekey("approve");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setClosingListener(getClosingListener());
  bean.setDataEditor(getDataEditor());
  bean.setUserdefitemPreparator(getCompositeBillDataPrepare_93fe2f());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getCompositeBillDataPrepare_93fe2f(){
 if(context.get("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#93fe2f")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare)context.get("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#93fe2f");
  nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
  context.put("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#93fe2f",bean);
  bean.setBillDataPrepares(getManagedList7());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList7(){  List list = new ArrayList();  list.add(getUserdefitemPreparator());  return list;}

public nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemPreparator(){
 if(context.get("userdefitemPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("userdefitemPreparator");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("userdefitemPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getUserdefQueryParam_14dfc1());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_14dfc1(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#14dfc1")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#14dfc1");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#14dfc1",bean);
  bean.setMdfullname("uap.material_pf");
  bean.setPos(0);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.view.MaterialPfDataEditor getDataEditor(){
 if(context.get("dataEditor")!=null)
 return (nc.ui.bd.material.pf.view.MaterialPfDataEditor)context.get("dataEditor");
  nc.ui.bd.material.pf.view.MaterialPfDataEditor bean = new nc.ui.bd.material.pf.view.MaterialPfDataEditor();
  context.put("dataEditor",bean);
  bean.setModel(getBaseinfoModel());
  bean.setApproveModel(getApproveModel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("data");
  bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_6e2c42());
  bean.setBodyActionMap(getManagedMap0());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_6e2c42(){
 if(context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#6e2c42")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#6e2c42");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#6e2c42",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList9());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList9(){  List list = new ArrayList();  list.add(getCardUserdefitemQueryParam());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getCardUserdefitemQueryParam(){
 if(context.get("cardUserdefitemQueryParam")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("cardUserdefitemQueryParam");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("cardUserdefitemQueryParam",bean);
  bean.setMdfullname("uap.material");
  bean.setPos(0);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("materialconvert",getManagedList10());  map.put("materialtaxtype",getManagedList11());  map.put("materialassistant",getManagedList12());  return map;}

private List getManagedList10(){  List list = new ArrayList();  list.add(getAddLineAction_15f62b());  list.add(getInsertLineAction_1d4873f());  list.add(getDelLineAction_1fb7588());  list.add(getCopyLineAction_1e15ecb());  list.add(getPasteLineAction_11672cd());  return list;}

private nc.ui.uif2.actions.AddLineAction getAddLineAction_15f62b(){
 if(context.get("nc.ui.uif2.actions.AddLineAction#15f62b")!=null)
 return (nc.ui.uif2.actions.AddLineAction)context.get("nc.ui.uif2.actions.AddLineAction#15f62b");
  nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
  context.put("nc.ui.uif2.actions.AddLineAction#15f62b",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.InsertLineAction getInsertLineAction_1d4873f(){
 if(context.get("nc.ui.uif2.actions.InsertLineAction#1d4873f")!=null)
 return (nc.ui.uif2.actions.InsertLineAction)context.get("nc.ui.uif2.actions.InsertLineAction#1d4873f");
  nc.ui.uif2.actions.InsertLineAction bean = new nc.ui.uif2.actions.InsertLineAction();
  context.put("nc.ui.uif2.actions.InsertLineAction#1d4873f",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.DelLineAction getDelLineAction_1fb7588(){
 if(context.get("nc.ui.uif2.actions.DelLineAction#1fb7588")!=null)
 return (nc.ui.uif2.actions.DelLineAction)context.get("nc.ui.uif2.actions.DelLineAction#1fb7588");
  nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
  context.put("nc.ui.uif2.actions.DelLineAction#1fb7588",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.CopyLineAction getCopyLineAction_1e15ecb(){
 if(context.get("nc.ui.uif2.actions.CopyLineAction#1e15ecb")!=null)
 return (nc.ui.uif2.actions.CopyLineAction)context.get("nc.ui.uif2.actions.CopyLineAction#1e15ecb");
  nc.ui.uif2.actions.CopyLineAction bean = new nc.ui.uif2.actions.CopyLineAction();
  context.put("nc.ui.uif2.actions.CopyLineAction#1e15ecb",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.PasteLineAction getPasteLineAction_11672cd(){
 if(context.get("nc.ui.uif2.actions.PasteLineAction#11672cd")!=null)
 return (nc.ui.uif2.actions.PasteLineAction)context.get("nc.ui.uif2.actions.PasteLineAction#11672cd");
  nc.ui.uif2.actions.PasteLineAction bean = new nc.ui.uif2.actions.PasteLineAction();
  context.put("nc.ui.uif2.actions.PasteLineAction#11672cd",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList11(){  List list = new ArrayList();  list.add(getAddLineAction_cb1b58());  list.add(getInsertLineAction_706321());  list.add(getDelLineAction_7c1c2a());  list.add(getCopyLineAction_1d234a());  list.add(getPasteLineAction_f809cd());  return list;}

private nc.ui.uif2.actions.AddLineAction getAddLineAction_cb1b58(){
 if(context.get("nc.ui.uif2.actions.AddLineAction#cb1b58")!=null)
 return (nc.ui.uif2.actions.AddLineAction)context.get("nc.ui.uif2.actions.AddLineAction#cb1b58");
  nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
  context.put("nc.ui.uif2.actions.AddLineAction#cb1b58",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.InsertLineAction getInsertLineAction_706321(){
 if(context.get("nc.ui.uif2.actions.InsertLineAction#706321")!=null)
 return (nc.ui.uif2.actions.InsertLineAction)context.get("nc.ui.uif2.actions.InsertLineAction#706321");
  nc.ui.uif2.actions.InsertLineAction bean = new nc.ui.uif2.actions.InsertLineAction();
  context.put("nc.ui.uif2.actions.InsertLineAction#706321",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.DelLineAction getDelLineAction_7c1c2a(){
 if(context.get("nc.ui.uif2.actions.DelLineAction#7c1c2a")!=null)
 return (nc.ui.uif2.actions.DelLineAction)context.get("nc.ui.uif2.actions.DelLineAction#7c1c2a");
  nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
  context.put("nc.ui.uif2.actions.DelLineAction#7c1c2a",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.CopyLineAction getCopyLineAction_1d234a(){
 if(context.get("nc.ui.uif2.actions.CopyLineAction#1d234a")!=null)
 return (nc.ui.uif2.actions.CopyLineAction)context.get("nc.ui.uif2.actions.CopyLineAction#1d234a");
  nc.ui.uif2.actions.CopyLineAction bean = new nc.ui.uif2.actions.CopyLineAction();
  context.put("nc.ui.uif2.actions.CopyLineAction#1d234a",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.PasteLineAction getPasteLineAction_f809cd(){
 if(context.get("nc.ui.uif2.actions.PasteLineAction#f809cd")!=null)
 return (nc.ui.uif2.actions.PasteLineAction)context.get("nc.ui.uif2.actions.PasteLineAction#f809cd");
  nc.ui.uif2.actions.PasteLineAction bean = new nc.ui.uif2.actions.PasteLineAction();
  context.put("nc.ui.uif2.actions.PasteLineAction#f809cd",bean);
  bean.setModel(getBaseinfoModel());
  bean.setCardpanel(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList12(){  List list = new ArrayList();  list.add(getMarAsstDefineAction_4b9e56());  return list;}

private nc.ui.bd.material.assistant.action.MarAsstDefineAction getMarAsstDefineAction_4b9e56(){
 if(context.get("nc.ui.bd.material.assistant.action.MarAsstDefineAction#4b9e56")!=null)
 return (nc.ui.bd.material.assistant.action.MarAsstDefineAction)context.get("nc.ui.bd.material.assistant.action.MarAsstDefineAction#4b9e56");
  nc.ui.bd.material.assistant.action.MarAsstDefineAction bean = new nc.ui.bd.material.assistant.action.MarAsstDefineAction();
  context.put("nc.ui.bd.material.assistant.action.MarAsstDefineAction#4b9e56",bean);
  bean.setModel(getBaseinfoModel());
  bean.setDialogMediator(getAsstDlgMediator());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator getAsstDlgMediator(){
 if(context.get("asstDlgMediator")!=null)
 return (nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator)context.get("asstDlgMediator");
  nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator bean = new nc.ui.bd.pub.view.BDEditorOkCancelDialogMediator();
  context.put("asstDlgMediator",bean);
  bean.setName(getI18nFB_883bf3());
  bean.setModel(getMarAsstModel());
  bean.setEditor(getMarAsstEditor());
  bean.setSaveAction(getMarAsstDefineSaveAction_18911f1());
  bean.setCancelAction(getCancelAction_1f92493());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_883bf3(){
 if(context.get("nc.ui.uif2.I18nFB#883bf3")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#883bf3");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#883bf3",bean);  bean.setResDir("10140mag");
  bean.setDefaultValue("辅助属性定义");
  bean.setResId("010140mag0191");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#883bf3",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.bd.material.assistant.action.MarAsstDefineSaveAction getMarAsstDefineSaveAction_18911f1(){
 if(context.get("nc.ui.bd.material.assistant.action.MarAsstDefineSaveAction#18911f1")!=null)
 return (nc.ui.bd.material.assistant.action.MarAsstDefineSaveAction)context.get("nc.ui.bd.material.assistant.action.MarAsstDefineSaveAction#18911f1");
  nc.ui.bd.material.assistant.action.MarAsstDefineSaveAction bean = new nc.ui.bd.material.assistant.action.MarAsstDefineSaveAction();
  context.put("nc.ui.bd.material.assistant.action.MarAsstDefineSaveAction#18911f1",bean);
  bean.setModel(getMarAsstModel());
  bean.setBaseInfoEditor(getDataEditor());
  bean.setMarAsstPanel(getMarAsstPanel());
  bean.setEditor(getMarAsstEditor());
  bean.setFrameEditor(getBillForm_fb9f14());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.BillForm getBillForm_fb9f14(){
 if(context.get("nc.ui.uif2.editor.BillForm#fb9f14")!=null)
 return (nc.ui.uif2.editor.BillForm)context.get("nc.ui.uif2.editor.BillForm#fb9f14");
  nc.ui.uif2.editor.BillForm bean = new nc.ui.uif2.editor.BillForm();
  context.put("nc.ui.uif2.editor.BillForm#fb9f14",bean);
  bean.setModel(getBillManageModel_17de2e());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("asstframe");
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.model.BillManageModel getBillManageModel_17de2e(){
 if(context.get("nc.ui.uif2.model.BillManageModel#17de2e")!=null)
 return (nc.ui.uif2.model.BillManageModel)context.get("nc.ui.uif2.model.BillManageModel#17de2e");
  nc.ui.uif2.model.BillManageModel bean = new nc.ui.uif2.model.BillManageModel();
  context.put("nc.ui.uif2.model.BillManageModel#17de2e",bean);
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.CancelAction getCancelAction_1f92493(){
 if(context.get("nc.ui.uif2.actions.CancelAction#1f92493")!=null)
 return (nc.ui.uif2.actions.CancelAction)context.get("nc.ui.uif2.actions.CancelAction#1f92493");
  nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
  context.put("nc.ui.uif2.actions.CancelAction#1f92493",bean);
  bean.setModel(getMarAsstModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.assistant.view.MarAsstQuickDefineEditor getMarAsstEditor(){
 if(context.get("marAsstEditor")!=null)
 return (nc.ui.bd.material.assistant.view.MarAsstQuickDefineEditor)context.get("marAsstEditor");
  nc.ui.bd.material.assistant.view.MarAsstQuickDefineEditor bean = new nc.ui.bd.material.assistant.view.MarAsstQuickDefineEditor();
  context.put("marAsstEditor",bean);
  bean.setModel(getMarAsstModel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("asstdefine");
  bean.setUserDefItemContainer(getUserdefitemContainer());
  bean.setMarAsstPanel(getMarAsstPanel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel getMarAsstModel(){
 if(context.get("marAsstModel")!=null)
 return (nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel)context.get("marAsstModel");
  nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel bean = new nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel();
  context.put("marAsstModel",bean);
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.assistant.view.MarAsstPanel getMarAsstPanel(){
 if(context.get("marAsstPanel")!=null)
 return (nc.ui.bd.material.assistant.view.MarAsstPanel)context.get("marAsstPanel");
  nc.ui.bd.material.assistant.view.MarAsstPanel bean = new nc.ui.bd.material.assistant.view.MarAsstPanel();
  context.put("marAsstPanel",bean);
  bean.setModel(getBaseinfoModel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("assistant");
  bean.setUserDefItemContainer(getUserdefitemContainer());
  bean.setRequestFocus(false);
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getListViewActions(){
 if(context.get("listViewActions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("listViewActions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getListView());  context.put("listViewActions",bean);
  bean.setActions(getManagedList13());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList13(){  List list = new ArrayList();  list.add(getAddAction());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getRefreshAction());  list.add(getSeparatorAction());  list.add(getSeparatorAction());  list.add(getCommitGroupAction());  list.add(getApproveGroupAciton());  list.add(getSeparatorAction());  list.add(getPrintActionGroup());  list.add(getLinkMdmAction());  return list;}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getCardPanelActions(){
 if(context.get("cardPanelActions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("cardPanelActions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getDataEditor());  context.put("cardPanelActions",bean);
  bean.setActions(getManagedList14());
  bean.setEditActions(getManagedList15());
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList14(){  List list = new ArrayList();  list.add(getAddAction());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getRefreshSingleAction());  list.add(getSeparatorAction());  list.add(getSeparatorAction());  list.add(getCommitGroupAction());  list.add(getApproveGroupAciton());  list.add(getSeparatorAction());  list.add(getCardPrintActionGroup());  list.add(getAccessoryAction());  list.add(getLinkMdmAction());  return list;}

private List getManagedList15(){  List list = new ArrayList();  list.add(getSaveAction());  list.add(getSeparatorAction());  list.add(getCancelAction());  return list;}

public nc.funcnode.ui.action.SeparatorAction getSeparatorAction(){
 if(context.get("separatorAction")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("separatorAction");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("separatorAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.action.LinkMdmAction getLinkMdmAction(){
 if(context.get("linkMdmAction")!=null)
 return (nc.ui.bd.material.pf.action.LinkMdmAction)context.get("linkMdmAction");
  nc.ui.bd.material.pf.action.LinkMdmAction bean = new nc.ui.bd.material.pf.action.LinkMdmAction();
  context.put("linkMdmAction",bean);
  bean.setModel(getApproveModel());
  bean.setRefreshAction(getRefreshAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.SwitchAction getSwitchMediator(){
 if(context.get("switchMediator")!=null)
 return (nc.ui.uif2.actions.SwitchAction)context.get("switchMediator");
  nc.ui.uif2.actions.SwitchAction bean = new nc.ui.uif2.actions.SwitchAction();
  context.put("switchMediator",bean);
  bean.setContext(getContext());
  bean.setComponent1(getListView());
  bean.setComponent2(getApproveEditor());
  bean.init();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.action.MaterialPfAddAction getAddAction(){
 if(context.get("addAction")!=null)
 return (nc.ui.bd.material.pf.action.MaterialPfAddAction)context.get("addAction");
  nc.ui.bd.material.pf.action.MaterialPfAddAction bean = new nc.ui.bd.material.pf.action.MaterialPfAddAction();
  context.put("addAction",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.pfactions.PfEditAction getEditAction(){
 if(context.get("editAction")!=null)
 return (nc.ui.bd.pub.pfactions.PfEditAction)context.get("editAction");
  nc.ui.bd.pub.pfactions.PfEditAction bean = new nc.ui.bd.pub.pfactions.PfEditAction();
  context.put("editAction",bean);
  bean.setModel(getApproveModel());
  bean.setBilltype("10WL");
  bean.setResourceCode("materialpf");
  bean.setMdOperateCode("edit");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.pfactions.PfDeleteAction getDeleteAction(){
 if(context.get("deleteAction")!=null)
 return (nc.ui.bd.pub.pfactions.PfDeleteAction)context.get("deleteAction");
  nc.ui.bd.pub.pfactions.PfDeleteAction bean = new nc.ui.bd.pub.pfactions.PfDeleteAction();
  context.put("deleteAction",bean);
  bean.setModel(getApproveModel());
  bean.setResourceCode("materialpf");
  bean.setMdOperateCode("delete");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.action.MaterialPfSaveAction getSaveAction(){
 if(context.get("saveAction")!=null)
 return (nc.ui.bd.material.pf.action.MaterialPfSaveAction)context.get("saveAction");
  nc.ui.bd.material.pf.action.MaterialPfSaveAction bean = new nc.ui.bd.material.pf.action.MaterialPfSaveAction();
  context.put("saveAction",bean);
  bean.setModel(getApproveModel());
  bean.setEditor(getApproveEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.action.MaterialPfCancelAction getCancelAction(){
 if(context.get("cancelAction")!=null)
 return (nc.ui.bd.material.pf.action.MaterialPfCancelAction)context.get("cancelAction");
  nc.ui.bd.material.pf.action.MaterialPfCancelAction bean = new nc.ui.bd.material.pf.action.MaterialPfCancelAction();
  context.put("cancelAction",bean);
  bean.setModel(getApproveModel());
  bean.setEditor(getApproveEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getQueryActionGroup(){
 if(context.get("queryActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("queryActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("queryActionGroup",bean);
  bean.setCode("Query");
  bean.setActions(getQueryActionList_16d1825());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.QueryActionList getQueryActionList_16d1825(){
 if(context.get("nc.ui.uif2.actions.QueryActionList#16d1825")!=null)
 return (nc.ui.uif2.actions.QueryActionList)context.get("nc.ui.uif2.actions.QueryActionList#16d1825");
  nc.ui.uif2.actions.QueryActionList bean = new nc.ui.uif2.actions.QueryActionList();
  context.put("nc.ui.uif2.actions.QueryActionList#16d1825",bean);
  bean.setQueryAction(getQueryAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.QueryAction getQueryAction(){
 if(context.get("queryAction")!=null)
 return (nc.ui.uif2.actions.QueryAction)context.get("queryAction");
  nc.ui.uif2.actions.QueryAction bean = new nc.ui.uif2.actions.QueryAction();
  context.put("queryAction",bean);
  bean.setModel(getApproveModel());
  bean.setDataManager(getModelDataManager());
  bean.setTemplateContainer(getQueryTemplateContainer());
  bean.setQueryDelegator(getBUIncluedGlobleAndGroupQueryDelegator_14c5a2f());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.bd.pub.query.BUIncluedGlobleAndGroupQueryDelegator getBUIncluedGlobleAndGroupQueryDelegator_14c5a2f(){
 if(context.get("nc.ui.bd.pub.query.BUIncluedGlobleAndGroupQueryDelegator#14c5a2f")!=null)
 return (nc.ui.bd.pub.query.BUIncluedGlobleAndGroupQueryDelegator)context.get("nc.ui.bd.pub.query.BUIncluedGlobleAndGroupQueryDelegator#14c5a2f");
  nc.ui.bd.pub.query.BUIncluedGlobleAndGroupQueryDelegator bean = new nc.ui.bd.pub.query.BUIncluedGlobleAndGroupQueryDelegator();
  context.put("nc.ui.bd.pub.query.BUIncluedGlobleAndGroupQueryDelegator#14c5a2f",bean);
  bean.setContext(getContext());
  bean.setIncluedGlobleAndGroupBUFields(getManagedList16());
  bean.setTemplateContainer(getQueryTemplateContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList16(){  List list = new ArrayList();  list.add("pk_targetorg");  return list;}

public nc.ui.uif2.actions.RefreshAction getRefreshAction(){
 if(context.get("refreshAction")!=null)
 return (nc.ui.uif2.actions.RefreshAction)context.get("refreshAction");
  nc.ui.uif2.actions.RefreshAction bean = new nc.ui.uif2.actions.RefreshAction();
  context.put("refreshAction",bean);
  bean.setModel(getApproveModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.RefreshSingleAction getRefreshSingleAction(){
 if(context.get("refreshSingleAction")!=null)
 return (nc.ui.uif2.actions.RefreshSingleAction)context.get("refreshSingleAction");
  nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
  context.put("refreshSingleAction",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getCommitGroupAction(){
 if(context.get("commitGroupAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("commitGroupAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("commitGroupAction",bean);
  bean.setCode("Commit");
  bean.setActions(getManagedList17());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList17(){  List list = new ArrayList();  list.add(getCommitAction());  list.add(getRecallAction());  return list;}

public nc.ui.bd.material.pf.action.MaterialPfCommitAction getCommitAction(){
 if(context.get("commitAction")!=null)
 return (nc.ui.bd.material.pf.action.MaterialPfCommitAction)context.get("commitAction");
  nc.ui.bd.material.pf.action.MaterialPfCommitAction bean = new nc.ui.bd.material.pf.action.MaterialPfCommitAction();
  context.put("commitAction",bean);
  bean.setModel(getApproveModel());
  bean.setBilltype("10WL");
  bean.setResourceCode("materialpf");
  bean.setMdOperateCode("commit");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.action.MaterialPfRecallAction getRecallAction(){
 if(context.get("recallAction")!=null)
 return (nc.ui.bd.material.pf.action.MaterialPfRecallAction)context.get("recallAction");
  nc.ui.bd.material.pf.action.MaterialPfRecallAction bean = new nc.ui.bd.material.pf.action.MaterialPfRecallAction();
  context.put("recallAction",bean);
  bean.setModel(getApproveModel());
  bean.setBillType("10WL");
  bean.setResourceCode("materialpf");
  bean.setMdOperateCode("recall");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getApproveGroupAciton(){
 if(context.get("approveGroupAciton")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("approveGroupAciton");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("approveGroupAciton",bean);
  bean.setCode("approve");
  bean.setActions(getManagedList18());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList18(){  List list = new ArrayList();  list.add(getApproveAction());  list.add(getAbstainAction());  list.add(getApproveInfoAction());  return list;}

public nc.ui.bd.material.pf.action.MaterialPfApproveAction getApproveAction(){
 if(context.get("approveAction")!=null)
 return (nc.ui.bd.material.pf.action.MaterialPfApproveAction)context.get("approveAction");
  nc.ui.bd.material.pf.action.MaterialPfApproveAction bean = new nc.ui.bd.material.pf.action.MaterialPfApproveAction();
  context.put("approveAction",bean);
  bean.setModel(getApproveModel());
  bean.setBilltype("10WL");
  bean.setResourceCode("materialpf");
  bean.setMdOperateCode("approve");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.pfactions.ApproveInfoAction getApproveInfoAction(){
 if(context.get("approveInfoAction")!=null)
 return (nc.ui.bd.pub.pfactions.ApproveInfoAction)context.get("approveInfoAction");
  nc.ui.bd.pub.pfactions.ApproveInfoAction bean = new nc.ui.bd.pub.pfactions.ApproveInfoAction();
  context.put("approveInfoAction",bean);
  bean.setModel(getApproveModel());
  bean.setBilltype("10WL");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.action.MaterialPfUnApproveAction getAbstainAction(){
 if(context.get("abstainAction")!=null)
 return (nc.ui.bd.material.pf.action.MaterialPfUnApproveAction)context.get("abstainAction");
  nc.ui.bd.material.pf.action.MaterialPfUnApproveAction bean = new nc.ui.bd.material.pf.action.MaterialPfUnApproveAction();
  context.put("abstainAction",bean);
  bean.setModel(getApproveModel());
  bean.setBilltype("10WL");
  bean.setResourceCode("materialpf");
  bean.setMdOperateCode("unapprove");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintActionGroup(){
 if(context.get("printActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printActionGroup",bean);
  bean.setCode("PrintMenu");
  bean.setActions(getManagedList19());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList19(){  List list = new ArrayList();  list.add(getTempletprintaction());  list.add(getTempletprintpreviewaction());  list.add(getOutputAction());  return list;}

public nc.ui.bd.pub.actions.AccessoryAction getAccessoryAction(){
 if(context.get("accessoryAction")!=null)
 return (nc.ui.bd.pub.actions.AccessoryAction)context.get("accessoryAction");
  nc.ui.bd.pub.actions.AccessoryAction bean = new nc.ui.bd.pub.actions.AccessoryAction();
  context.put("accessoryAction",bean);
  bean.setWindowListener(getAccessoryShowAction());
  bean.setModel(getApproveModel());
  bean.setMetaDataID("6b2c8309-930b-4989-8b0e-f2a80f35c62c");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.TemplatePreviewAction getTempletprintpreviewaction(){
 if(context.get("templetprintpreviewaction")!=null)
 return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("templetprintpreviewaction");
  nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
  context.put("templetprintpreviewaction",bean);
  bean.setNodeKey("materialpflist");
  bean.setModel(getApproveModel());
  bean.setPrintDlgParentConatiner(getApproveEditor());
  bean.setDatasource(getDatasource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.TemplatePrintAction getTempletprintaction(){
 if(context.get("templetprintaction")!=null)
 return (nc.ui.uif2.actions.TemplatePrintAction)context.get("templetprintaction");
  nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
  context.put("templetprintaction",bean);
  bean.setNodeKey("materialpflist");
  bean.setModel(getApproveModel());
  bean.setPrintDlgParentConatiner(getApproveEditor());
  bean.setDatasource(getDatasource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.OutputAction getOutputAction(){
 if(context.get("outputAction")!=null)
 return (nc.ui.uif2.actions.OutputAction)context.get("outputAction");
  nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
  context.put("outputAction",bean);
  bean.setNodeKey("materialpflist");
  bean.setModel(getApproveModel());
  bean.setPrintDlgParentConatiner(getApproveEditor());
  bean.setDatasource(getDatasource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getDatasource(){
 if(context.get("datasource")!=null)
 return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("datasource");
  nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
  context.put("datasource",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getCardPrintActionGroup(){
 if(context.get("cardPrintActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("cardPrintActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("cardPrintActionGroup",bean);
  bean.setCode("PrintMenu");
  bean.setActions(getManagedList20());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList20(){  List list = new ArrayList();  list.add(getCardTempletprintaction());  list.add(getCardTempletprintpreviewaction());  list.add(getCardOutputAction());  return list;}

public nc.ui.uif2.actions.TemplatePreviewAction getCardTempletprintpreviewaction(){
 if(context.get("cardTempletprintpreviewaction")!=null)
 return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("cardTempletprintpreviewaction");
  nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
  context.put("cardTempletprintpreviewaction",bean);
  bean.setNodeKey("materialpfcard");
  bean.setModel(getApproveModel());
  bean.setPrintDlgParentConatiner(getApproveEditor());
  bean.setDatasource(getCardDatasource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.TemplatePrintAction getCardTempletprintaction(){
 if(context.get("cardTempletprintaction")!=null)
 return (nc.ui.uif2.actions.TemplatePrintAction)context.get("cardTempletprintaction");
  nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
  context.put("cardTempletprintaction",bean);
  bean.setNodeKey("materialpfcard");
  bean.setModel(getApproveModel());
  bean.setPrintDlgParentConatiner(getApproveEditor());
  bean.setDatasource(getCardDatasource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.OutputAction getCardOutputAction(){
 if(context.get("cardOutputAction")!=null)
 return (nc.ui.uif2.actions.OutputAction)context.get("cardOutputAction");
  nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
  context.put("cardOutputAction",bean);
  bean.setNodeKey("materialpfcard");
  bean.setModel(getApproveModel());
  bean.setPrintDlgParentConatiner(getApproveEditor());
  bean.setDatasource(getCardDatasource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.material.pf.model.MaterialPFDataSource getCardDatasource(){
 if(context.get("cardDatasource")!=null)
 return (nc.ui.bd.material.pf.model.MaterialPFDataSource)context.get("cardDatasource");
  nc.ui.bd.material.pf.model.MaterialPFDataSource bean = new nc.ui.bd.material.pf.model.MaterialPFDataSource();
  context.put("cardDatasource",bean);
  bean.setModel(getApproveModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayoutRoot(getTBNode_86532b());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_86532b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#86532b")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#86532b");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#86532b",bean);
  bean.setTabs(getManagedList21());
  bean.setShowMode("CardLayout");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList21(){  List list = new ArrayList();  list.add(getHSNode_a06b1b());  list.add(getVSNode_f45342());  return list;}

private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_a06b1b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.HSNode#a06b1b")!=null)
 return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#a06b1b");
  nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
  context.put("nc.ui.uif2.tangramlayout.node.HSNode#a06b1b",bean);
  bean.setLeft(getCNode_1b4d9de());
  bean.setRight(getCNode_4b8f1b());
  bean.setDividerLocation(0.2f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1b4d9de(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1b4d9de")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1b4d9de");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1b4d9de",bean);
  bean.setComponent(getQueryAreaShell());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_4b8f1b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#4b8f1b")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#4b8f1b");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#4b8f1b",bean);
  bean.setComponent(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_f45342(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#f45342")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#f45342");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#f45342",bean);
  bean.setUp(getCNode_1d7d6a8());
  bean.setDown(getVSNode_190cad4());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1d7d6a8(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1d7d6a8")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1d7d6a8");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1d7d6a8",bean);
  bean.setComponent(getCardInfoPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_190cad4(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#190cad4")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#190cad4");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#190cad4",bean);
  bean.setUp(getCNode_14dc614());
  bean.setDown(getTBNode_5ce834());
  bean.setDividerLocation(0.2f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_14dc614(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#14dc614")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#14dc614");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#14dc614",bean);
  bean.setComponent(getApproveEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_5ce834(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#5ce834")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#5ce834");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#5ce834",bean);
  bean.setTabs(getManagedList22());
  bean.setTabbedPaneFetcher(getTabbedPaneFetcher());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList22(){  List list = new ArrayList();  list.add(getCNode_1669f2f());  return list;}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1669f2f(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1669f2f")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1669f2f");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1669f2f",bean);
  bean.setName(getI18nFB_1078008());
  bean.setComponent(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1078008(){
 if(context.get("nc.ui.uif2.I18nFB#1078008")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1078008");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1078008",bean);  bean.setResDir("10140mpf");
  bean.setDefaultValue("物料信息");
  bean.setResId("010140mpf0002");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1078008",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.pub.DefaultTabbedPaneEditableControl getTabbedPaneFetcher(){
 if(context.get("tabbedPaneFetcher")!=null)
 return (nc.ui.bd.pub.DefaultTabbedPaneEditableControl)context.get("tabbedPaneFetcher");
  nc.ui.bd.pub.DefaultTabbedPaneEditableControl bean = new nc.ui.bd.pub.DefaultTabbedPaneEditableControl();
  context.put("tabbedPaneFetcher",bean);
  bean.setModel(getBaseinfoModel());
  bean.setTargetComponent(getDataEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.QueryAreaShell getQueryAreaShell(){
 if(context.get("queryAreaShell")!=null)
 return (nc.ui.uif2.actions.QueryAreaShell)context.get("queryAreaShell");
  nc.ui.uif2.actions.QueryAreaShell bean = new nc.ui.uif2.actions.QueryAreaShell();
  context.put("queryAreaShell",bean);
  bean.setQueryArea(getBdqueryActionMediator_created_be5a7b());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.queryarea.QueryArea getBdqueryActionMediator_created_be5a7b(){
 if(context.get("bdqueryActionMediator.created#be5a7b")!=null)
 return (nc.ui.queryarea.QueryArea)context.get("bdqueryActionMediator.created#be5a7b");
  nc.ui.queryarea.QueryArea bean = getBdqueryActionMediator().createQueryArea();
  context.put("bdqueryActionMediator.created#be5a7b",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.FunNodeClosingHandler getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.uif2.FunNodeClosingHandler)context.get("ClosingListener");
  nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
  context.put("ClosingListener",bean);
  bean.setModel(getApproveModel());
  bean.setSaveaction(getSaveAction());
  bean.setCancelaction(getCancelAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.tools.BDPubQueryActionMediator getBdqueryActionMediator(){
 if(context.get("bdqueryActionMediator")!=null)
 return (nc.ui.bd.pub.tools.BDPubQueryActionMediator)context.get("bdqueryActionMediator");
  nc.ui.bd.pub.tools.BDPubQueryActionMediator bean = new nc.ui.bd.pub.tools.BDPubQueryActionMediator();
  context.put("bdqueryActionMediator",bean);
  bean.setQueryAction(getQueryAction());
  bean.setOrgFieldCode(getManagedList23());
  bean.process();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList23(){  List list = new ArrayList();  list.add("pk_org");  list.add("pk_targetorg");  return list;}

}
