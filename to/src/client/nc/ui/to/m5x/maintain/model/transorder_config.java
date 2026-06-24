package nc.ui.to.m5x.maintain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class transorder_config extends AbstractJavaBeanDefinition{
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

public nc.ui.to.m5x.maintain.model.TransOrderService getManageModelService(){
 if(context.get("ManageModelService")!=null)
 return (nc.ui.to.m5x.maintain.model.TransOrderService)context.get("ManageModelService");
  nc.ui.to.m5x.maintain.model.TransOrderService bean = new nc.ui.to.m5x.maintain.model.TransOrderService();
  context.put("ManageModelService",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory getBoadatorfactory(){
 if(context.get("boadatorfactory")!=null)
 return (nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory)context.get("boadatorfactory");
  nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory bean = new nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory();
  context.put("boadatorfactory",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.model.BillManageModel getManageAppModel(){
 if(context.get("manageAppModel")!=null)
 return (nc.ui.pubapp.uif2app.model.BillManageModel)context.get("manageAppModel");
  nc.ui.pubapp.uif2app.model.BillManageModel bean = new nc.ui.pubapp.uif2app.model.BillManageModel();
  context.put("manageAppModel",bean);
  bean.setService(getManageModelService());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
  bean.setBillType("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmpub.page.model.SCMBillPageModelDataManager getModelDataManager(){
 if(context.get("modelDataManager")!=null)
 return (nc.ui.scmpub.page.model.SCMBillPageModelDataManager)context.get("modelDataManager");
  nc.ui.scmpub.page.model.SCMBillPageModelDataManager bean = new nc.ui.scmpub.page.model.SCMBillPageModelDataManager();
  context.put("modelDataManager",bean);
  bean.setModel(getManageAppModel());
  bean.setService(getManageModelService());
  bean.setPageQuery(getPageQuery());
  bean.setPageDelegator(getPageDelegator());
  bean.setPagePanel(getListToolbarPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.lazilyload.DefaultBillLazilyLoader getBillLazilyLoader(){
 if(context.get("billLazilyLoader")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.DefaultBillLazilyLoader)context.get("billLazilyLoader");
  nc.ui.pubapp.uif2app.lazilyload.DefaultBillLazilyLoader bean = new nc.ui.pubapp.uif2app.lazilyload.DefaultBillLazilyLoader();
  context.put("billLazilyLoader",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager getLasilyLodadMediator(){
 if(context.get("lasilyLodadMediator")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager)context.get("lasilyLodadMediator");
  nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager bean = new nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager();
  context.put("lasilyLodadMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setLoader(getBillLazilyLoader());
  bean.setLazilyLoadSupporter(getManagedList0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList0(){  List list = new ArrayList();  list.add(getCardPanelLazilyLoad_137009b());  list.add(getListPanelLazilyLoad_8d01f());  list.add(getActionLazilyLoad_97ce53());  return list;}

private nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad getCardPanelLazilyLoad_137009b(){
 if(context.get("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#137009b")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad)context.get("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#137009b");
  nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad();
  context.put("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#137009b",bean);
  bean.setBillform(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad getListPanelLazilyLoad_8d01f(){
 if(context.get("nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#8d01f")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad)context.get("nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#8d01f");
  nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad();
  context.put("nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#8d01f",bean);
  bean.setListView(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad getActionLazilyLoad_97ce53(){
 if(context.get("nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad#97ce53")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad)context.get("nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad#97ce53");
  nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad();
  context.put("nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad#97ce53",bean);
  bean.setModel(getManageAppModel());
  bean.setActionList(getManagedList1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreviewAction());  list.add(getSplitPrintAction());  list.add(getSpShowHiddenAction());  return list;}

public nc.ui.uif2.components.progress.TPAProgressUtil getTpaprogressutil(){
 if(context.get("tpaprogressutil")!=null)
 return (nc.ui.uif2.components.progress.TPAProgressUtil)context.get("tpaprogressutil");
  nc.ui.uif2.components.progress.TPAProgressUtil bean = new nc.ui.uif2.components.progress.TPAProgressUtil();
  context.put("tpaprogressutil",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender getTrantypeTempMender(){
 if(context.get("trantypeTempMender")!=null)
 return (nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender)context.get("trantypeTempMender");
  nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender bean = new nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender(getContext());  context.put("trantypeTempMender",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.TemplateContainer getTemplateContainer(){
 if(context.get("templateContainer")!=null)
 return (nc.ui.pubapp.uif2app.view.TemplateContainer)context.get("templateContainer");
  nc.ui.pubapp.uif2app.view.TemplateContainer bean = new nc.ui.pubapp.uif2app.view.TemplateContainer();
  context.put("templateContainer",bean);
  bean.setContext(getContext());
  bean.setBillTemplateMender(getTrantypeTempMender());
  bean.load();
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
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.PfAddInfoLoader getPfAddInfoLoader(){
 if(context.get("pfAddInfoLoader")!=null)
 return (nc.ui.pubapp.uif2app.actions.PfAddInfoLoader)context.get("pfAddInfoLoader");
  nc.ui.pubapp.uif2app.actions.PfAddInfoLoader bean = new nc.ui.pubapp.uif2app.actions.PfAddInfoLoader();
  context.put("pfAddInfoLoader",bean);
  bean.setBillType("5X");
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller getRemoteCallCombinatorCaller(){
 if(context.get("remoteCallCombinatorCaller")!=null)
 return (nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller)context.get("remoteCallCombinatorCaller");
  nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller bean = new nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller();
  context.put("remoteCallCombinatorCaller",bean);
  bean.setRemoteCallers(getManagedList2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getQueryTemplateContainer());  list.add(getTemplateContainer());  list.add(getUserdefitemContainer());  list.add(getPfAddInfoLoader());  list.add(getOhandcardtemplateContainer());  return list;}

public nc.ui.to.m5x.revise.view.TransOrderBillListView getListView(){
 if(context.get("listView")!=null)
 return (nc.ui.to.m5x.revise.view.TransOrderBillListView)context.get("listView");
  nc.ui.to.m5x.revise.view.TransOrderBillListView bean = new nc.ui.to.m5x.revise.view.TransOrderBillListView();
  context.put("listView",bean);
  bean.setModel(getManageAppModel());
  bean.setMultiSelectionMode(0);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setPaginationBar(getPageBar());
  bean.setUserdefitemListPreparator(getCompositeBillListDataPrepare_6ab2a1());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getCompositeBillListDataPrepare_6ab2a1(){
 if(context.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#6ab2a1")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare)context.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#6ab2a1");
  nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
  context.put("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#6ab2a1",bean);
  bean.setBillListDataPrepares(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList3(){  List list = new ArrayList();  list.add(getUserdefitemlistPreparator());  list.add(getMarAsstPreparator());  return list;}

public nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener)context.get("InitDataListener");
  nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener bean = new nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener();
  context.put("InitDataListener",bean);
  bean.setModel(getManageAppModel());
  bean.setQueryAction(getQueryAction());
  bean.setVoClassName("nc.vo.to.m5x.entity.BillVO");
  bean.setAutoShowUpComponent(getBillFormEditor());
  bean.setProcessorMap(getManagedMap0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("19",getM3ZInitDataProcessor_f57072());  map.put("20",getMmpacInitDataProcessor_1a973d7());  return map;}

private nc.ui.to.m5x.billref.m3z.M3ZInitDataProcessor getM3ZInitDataProcessor_f57072(){
 if(context.get("nc.ui.to.m5x.billref.m3z.M3ZInitDataProcessor#f57072")!=null)
 return (nc.ui.to.m5x.billref.m3z.M3ZInitDataProcessor)context.get("nc.ui.to.m5x.billref.m3z.M3ZInitDataProcessor#f57072");
  nc.ui.to.m5x.billref.m3z.M3ZInitDataProcessor bean = new nc.ui.to.m5x.billref.m3z.M3ZInitDataProcessor();
  context.put("nc.ui.to.m5x.billref.m3z.M3ZInitDataProcessor#f57072",bean);
  bean.setTransferProcessor(getTransferProcessor());
  bean.setBillForm(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.to.m5x.billref.mmpac.MmpacInitDataProcessor getMmpacInitDataProcessor_1a973d7(){
 if(context.get("nc.ui.to.m5x.billref.mmpac.MmpacInitDataProcessor#1a973d7")!=null)
 return (nc.ui.to.m5x.billref.mmpac.MmpacInitDataProcessor)context.get("nc.ui.to.m5x.billref.mmpac.MmpacInitDataProcessor#1a973d7");
  nc.ui.to.m5x.billref.mmpac.MmpacInitDataProcessor bean = new nc.ui.to.m5x.billref.mmpac.MmpacInitDataProcessor();
  context.put("nc.ui.to.m5x.billref.mmpac.MmpacInitDataProcessor#1a973d7",bean);
  bean.setTransferProcessor(getTransferProcessor());
  bean.setBillForm(getBillFormEditor());
  bean.setDelegator(getPageDelegator());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.BillOrgPanel getBillOrgPanel(){
 if(context.get("billOrgPanel")!=null)
 return (nc.ui.pubapp.uif2app.view.BillOrgPanel)context.get("billOrgPanel");
  nc.ui.pubapp.uif2app.view.BillOrgPanel bean = new nc.ui.pubapp.uif2app.view.BillOrgPanel();
  context.put("billOrgPanel",bean);
  bean.setLabelName(getI18nFB_1f42b28());
  bean.setModel(getManageAppModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1f42b28(){
 if(context.get("nc.ui.uif2.I18nFB#1f42b28")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1f42b28");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1f42b28",bean);  bean.setResDir("common");
  bean.setResId("UC000-0003722");
  bean.setDefaultValue("调出库存组织");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1f42b28",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.to.m5x.maintain.view.TransOrderEditor getBillFormEditor(){
 if(context.get("billFormEditor")!=null)
 return (nc.ui.to.m5x.maintain.view.TransOrderEditor)context.get("billFormEditor");
  nc.ui.to.m5x.maintain.view.TransOrderEditor bean = new nc.ui.to.m5x.maintain.view.TransOrderEditor();
  context.put("billFormEditor",bean);
  bean.setBillOrgPanel(getBillOrgPanel());
  bean.setModel(getManageAppModel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setTemplateNotNullValidate(true);
  bean.setAutoAddLine(true);
  bean.setBlankChildrenFilter(getMultiFieldsBlankChildrenFilter_13d6bca());
  bean.setBodyLineActions(getManagedList5());
  bean.setUserdefitemPreparator(getCompositeBillDataPrepare_9fe972());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter getMultiFieldsBlankChildrenFilter_13d6bca(){
 if(context.get("nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter#13d6bca")!=null)
 return (nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter)context.get("nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter#13d6bca");
  nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter bean = new nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter();
  context.put("nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter#13d6bca",bean);
  bean.setFilterMap(getManagedMap1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap1(){  Map map = new HashMap();  map.put("base",getManagedList4());  return map;}

private List getManagedList4(){  List list = new ArrayList();  list.add("cinventoryid");  return list;}

private List getManagedList5(){  List list = new ArrayList();  list.add(getAddLineAction_16fdc09());  list.add(getInsertLineAction_d6dfea());  list.add(getBodyDelLineAction_106bcb4());  list.add(getBodyCopyLineAction_14ef4ed());  list.add(getBodyPasteLineAction_b22fc6());  list.add(getBodyPasteToTailAction_5e79c0());  list.add(getActionsBar_ActionsBarSeparator_8b65e2());  list.add(getRearrangeRowNoBodyLineAction_1cdfeaa());  list.add(getActionsBar_ActionsBarSeparator_10a4de0());  list.add(getDefaultBodyZoomAction_17eb8f1());  return list;}

private nc.ui.to.m5x.maintain.action.AddLineAction getAddLineAction_16fdc09(){
 if(context.get("nc.ui.to.m5x.maintain.action.AddLineAction#16fdc09")!=null)
 return (nc.ui.to.m5x.maintain.action.AddLineAction)context.get("nc.ui.to.m5x.maintain.action.AddLineAction#16fdc09");
  nc.ui.to.m5x.maintain.action.AddLineAction bean = new nc.ui.to.m5x.maintain.action.AddLineAction();
  context.put("nc.ui.to.m5x.maintain.action.AddLineAction#16fdc09",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.to.m5x.maintain.action.InsertLineAction getInsertLineAction_d6dfea(){
 if(context.get("nc.ui.to.m5x.maintain.action.InsertLineAction#d6dfea")!=null)
 return (nc.ui.to.m5x.maintain.action.InsertLineAction)context.get("nc.ui.to.m5x.maintain.action.InsertLineAction#d6dfea");
  nc.ui.to.m5x.maintain.action.InsertLineAction bean = new nc.ui.to.m5x.maintain.action.InsertLineAction();
  context.put("nc.ui.to.m5x.maintain.action.InsertLineAction#d6dfea",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.BodyDelLineAction getBodyDelLineAction_106bcb4(){
 if(context.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#106bcb4")!=null)
 return (nc.ui.pubapp.uif2app.actions.BodyDelLineAction)context.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#106bcb4");
  nc.ui.pubapp.uif2app.actions.BodyDelLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyDelLineAction();
  context.put("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#106bcb4",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.BodyCopyLineAction getBodyCopyLineAction_14ef4ed(){
 if(context.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#14ef4ed")!=null)
 return (nc.ui.pubapp.uif2app.actions.BodyCopyLineAction)context.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#14ef4ed");
  nc.ui.pubapp.uif2app.actions.BodyCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyCopyLineAction();
  context.put("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#14ef4ed",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.BodyPasteLineAction getBodyPasteLineAction_b22fc6(){
 if(context.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#b22fc6")!=null)
 return (nc.ui.pubapp.uif2app.actions.BodyPasteLineAction)context.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#b22fc6");
  nc.ui.pubapp.uif2app.actions.BodyPasteLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteLineAction();
  context.put("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#b22fc6",bean);
  bean.setClearItems(getManagedList6());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add("cbill_bid");  list.add("cbillid");  list.add("ts");  list.add("vbrevisereason");  list.add("corigbill_bid");  list.add("noutnum");  list.add("ninnum");  list.add("nsendnum");  list.add("nreturnnum");  list.add("nshouldoutnum");  list.add("nwaylossnum");  list.add("niosettlemny");  list.add("niosettlenum");  list.add("notsettlemny");  list.add("notsettlenum");  list.add("ntoappleynum");  list.add("ntoordernum");  list.add("npoapplynum");  list.add("npoordernum");  list.add("nscordernum");  list.add("nmmordernum");  list.add("nplonum");  return list;}

private nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction getBodyPasteToTailAction_5e79c0(){
 if(context.get("nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction#5e79c0")!=null)
 return (nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction)context.get("nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction#5e79c0");
  nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction();
  context.put("nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction#5e79c0",bean);
  bean.setClearItems(getManagedList7());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList7(){  List list = new ArrayList();  list.add("cbill_bid");  list.add("cbillid");  list.add("ts");  list.add("vbrevisereason");  list.add("corigbill_bid");  list.add("noutnum");  list.add("ninnum");  list.add("nsendnum");  list.add("nreturnnum");  list.add("nshouldoutnum");  list.add("nwaylossnum");  list.add("niosettlemny");  list.add("niosettlenum");  list.add("notsettlemny");  list.add("notsettlenum");  list.add("ntoappleynum");  list.add("ntoordernum");  list.add("npoapplynum");  list.add("npoordernum");  list.add("nscordernum");  list.add("nmmordernum");  list.add("nplonum");  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_8b65e2(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8b65e2")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8b65e2");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8b65e2",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getRearrangeRowNoBodyLineAction_1cdfeaa(){
 if(context.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#1cdfeaa")!=null)
 return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction)context.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#1cdfeaa");
  nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
  context.put("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#1cdfeaa",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_10a4de0(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10a4de0")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10a4de0");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10a4de0",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction getDefaultBodyZoomAction_17eb8f1(){
 if(context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#17eb8f1")!=null)
 return (nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction)context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#17eb8f1");
  nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction bean = new nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction();
  context.put("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#17eb8f1",bean);
  bean.setPos(1);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getCompositeBillDataPrepare_9fe972(){
 if(context.get("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#9fe972")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare)context.get("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#9fe972");
  nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
  context.put("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#9fe972",bean);
  bean.setBillDataPrepares(getManagedList8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getUserdefitemPreparator());  list.add(getMarAsstPreparator());  return list;}

public nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemPreparator(){
 if(context.get("userdefitemPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("userdefitemPreparator");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("userdefitemPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList9());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList9(){  List list = new ArrayList();  list.add(getUserdefQueryParam_974d20());  list.add(getUserdefQueryParam_14888cc());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_974d20(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#974d20")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#974d20");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#974d20",bean);
  bean.setMdfullname("to.to_bill");
  bean.setPos(0);
  bean.setPrefix("vdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_14888cc(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#14888cc")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#14888cc");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#14888cc",bean);
  bean.setMdfullname("to.to_bill_b");
  bean.setPos(1);
  bean.setPrefix("vbdef");
  bean.setTabcode("base");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemlistPreparator(){
 if(context.get("userdefitemlistPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("userdefitemlistPreparator");
  nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
  context.put("userdefitemlistPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList10());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList10(){  List list = new ArrayList();  list.add(getUserdefQueryParam_10d1819());  list.add(getUserdefQueryParam_a913e6());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_10d1819(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#10d1819")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#10d1819");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#10d1819",bean);
  bean.setMdfullname("to.to_bill");
  bean.setPos(0);
  bean.setPrefix("vdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_a913e6(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#a913e6")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#a913e6");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#a913e6",bean);
  bean.setMdfullname("to.to_bill_b");
  bean.setPos(1);
  bean.setPrefix("vbdef");
  bean.setTabcode("base");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator getMarAsstPreparator(){
 if(context.get("marAsstPreparator")!=null)
 return (nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator)context.get("marAsstPreparator");
  nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator bean = new nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator();
  context.put("marAsstPreparator",bean);
  bean.setModel(getManageAppModel());
  bean.setContainer(getUserdefitemContainer());
  bean.setPrefix("vfree");
  bean.setMaterialField("cinventoryid");
  bean.setProjectField("cprojectid");
  bean.setSupplierField("cvendorid");
  bean.setProductorField("cproductorid");
  bean.setCustomerField("casscustid");
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
  bean.setParams(getManagedList11());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList11(){  List list = new ArrayList();  list.add(getQueryParam_5970c3());  list.add(getQueryParam_1fa4ace());  list.add(getQueryParam_1453404());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_5970c3(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#5970c3")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#5970c3");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#5970c3",bean);
  bean.setMdfullname("to.to_bill");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1fa4ace(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#1fa4ace")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#1fa4ace");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#1fa4ace",bean);
  bean.setMdfullname("to.to_bill_b");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1453404(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#1453404")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#1453404");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#1453404",bean);
  bean.setRulecode("materialassistant");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getSrclinkQueryHyperlinkMediator(){
 if(context.get("srclinkQueryHyperlinkMediator")!=null)
 return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator)context.get("srclinkQueryHyperlinkMediator");
  nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
  context.put("srclinkQueryHyperlinkMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setSrcBillIdField("csrcid");
  bean.setSrcBillNOField("vsrccode");
  bean.setSrcBillTypeField("vsrctype");
  bean.setSrcBillTypeFieldPos(1);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getFirstlinkQueryHyperlinkMediator(){
 if(context.get("firstlinkQueryHyperlinkMediator")!=null)
 return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator)context.get("firstlinkQueryHyperlinkMediator");
  nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
  context.put("firstlinkQueryHyperlinkMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setSrcBillIdField("cfirstid");
  bean.setSrcBillNOField("vfirstcode");
  bean.setSrcBillTypeField("vfirsttype");
  bean.setSrcBillTypeFieldPos(1);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ic.onhand.pane.QueryOnHandInfoPanel getOhandcard(){
 if(context.get("ohandcard")!=null)
 return (nc.ui.ic.onhand.pane.QueryOnHandInfoPanel)context.get("ohandcard");
  nc.ui.ic.onhand.pane.QueryOnHandInfoPanel bean = new nc.ui.ic.onhand.pane.QueryOnHandInfoPanel();
  context.put("ohandcard",bean);
  bean.setOnhandPanelSrc(getBillFormEditor());
  bean.setUserdefitemPreparator(getUserdefAndMarAsstCardPreparator());
  bean.setTemplateContainer(getOhandcardtemplateContainer());
  bean.setLogincontext(getContext());
  bean.initialize();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.TemplateContainer getOhandcardtemplateContainer(){
 if(context.get("ohandcardtemplateContainer")!=null)
 return (nc.ui.pubapp.uif2app.view.TemplateContainer)context.get("ohandcardtemplateContainer");
  nc.ui.pubapp.uif2app.view.TemplateContainer bean = new nc.ui.pubapp.uif2app.view.TemplateContainer();
  context.put("ohandcardtemplateContainer",bean);
  bean.setContext(getContext());
  bean.setFuncode("400808082");
  bean.setBillTemplateMender(getTrantypeTempMender());
  bean.load();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getUserdefAndMarAsstCardPreparator(){
 if(context.get("userdefAndMarAsstCardPreparator")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare)context.get("userdefAndMarAsstCardPreparator");
  nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
  context.put("userdefAndMarAsstCardPreparator",bean);
  bean.setBillDataPrepares(getManagedList12());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList12(){  List list = new ArrayList();  list.add(getUserdefitemPreparator());  list.add(getMarAsstPreparator());  return list;}

public nc.ui.to.m5x.maintain.action.M5XQryCondDlgInitializer getM5xQryCondDLGInitializer(){
 if(context.get("m5xQryCondDLGInitializer")!=null)
 return (nc.ui.to.m5x.maintain.action.M5XQryCondDlgInitializer)context.get("m5xQryCondDLGInitializer");
  nc.ui.to.m5x.maintain.action.M5XQryCondDlgInitializer bean = new nc.ui.to.m5x.maintain.action.M5XQryCondDlgInitializer();
  context.put("m5xQryCondDLGInitializer",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator getMouseClickShowPanelMediator(){
 if(context.get("mouseClickShowPanelMediator")!=null)
 return (nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator)context.get("mouseClickShowPanelMediator");
  nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator bean = new nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator();
  context.put("mouseClickShowPanelMediator",bean);
  bean.setListView(getListView());
  bean.setShowUpComponent(getBillFormEditor());
  bean.setHyperLinkColumn("vbillcode");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator getOrgSetterForAllRefMediator(){
 if(context.get("orgSetterForAllRefMediator")!=null)
 return (nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator)context.get("orgSetterForAllRefMediator");
  nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator bean = new nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator(getBillFormEditor());  context.put("orgSetterForAllRefMediator",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.RowNoMediator getRowNoMediator(){
 if(context.get("rowNoMediator")!=null)
 return (nc.ui.pubapp.uif2app.view.RowNoMediator)context.get("rowNoMediator");
  nc.ui.pubapp.uif2app.view.RowNoMediator bean = new nc.ui.pubapp.uif2app.view.RowNoMediator();
  context.put("rowNoMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmpub.listener.BillCodeEditMediator getBillCodeMediator(){
 if(context.get("billCodeMediator")!=null)
 return (nc.ui.scmpub.listener.BillCodeEditMediator)context.get("billCodeMediator");
  nc.ui.scmpub.listener.BillCodeEditMediator bean = new nc.ui.scmpub.listener.BillCodeEditMediator();
  context.put("billCodeMediator",bean);
  bean.setBillCodeKey("vbillcode");
  bean.setBillType("5X");
  bean.setBillForm(getBillFormEditor());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.FractionFixMediator getFractionFixMediator(){
 if(context.get("fractionFixMediator")!=null)
 return (nc.ui.pubapp.uif2app.view.FractionFixMediator)context.get("fractionFixMediator");
  nc.ui.pubapp.uif2app.view.FractionFixMediator bean = new nc.ui.pubapp.uif2app.view.FractionFixMediator(getManagedList13(),getManagedList14());  context.put("fractionFixMediator",bean);
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList13(){  List list = new ArrayList();  list.add(getBillFormEditor());  return list;}

private List getManagedList14(){  List list = new ArrayList();  list.add(getListView());  return list;}

public nc.ui.pubapp.uif2app.model.BillBodySortMediator getBillBodySortMediator(){
 if(context.get("billBodySortMediator")!=null)
 return (nc.ui.pubapp.uif2app.model.BillBodySortMediator)context.get("billBodySortMediator");
  nc.ui.pubapp.uif2app.model.BillBodySortMediator bean = new nc.ui.pubapp.uif2app.model.BillBodySortMediator(getManageAppModel(),getBillFormEditor(),getListView());  context.put("billBodySortMediator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.model.AppEventHandlerMediator getAppEventHandlerMediator(){
 if(context.get("appEventHandlerMediator")!=null)
 return (nc.ui.pubapp.uif2app.model.AppEventHandlerMediator)context.get("appEventHandlerMediator");
  nc.ui.pubapp.uif2app.model.AppEventHandlerMediator bean = new nc.ui.pubapp.uif2app.model.AppEventHandlerMediator();
  context.put("appEventHandlerMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setHandlerMap(getManagedMap2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap2(){  Map map = new HashMap();  map.put("nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent",getManagedList15());  map.put("nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent",getManagedList16());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent",getManagedList17());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent",getManagedList18());  map.put("nc.ui.pubapp.uif2app.event.OrgChangedEvent",getManagedList19());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent",getManagedList20());  map.put("nc.ui.pubapp.uif2app.event.card.CardPanelLoadEvent",getManagedList21());  return map;}

private List getManagedList15(){  List list = new ArrayList();  list.add(getHeadBeforeEditHandler_5a06c0());  return list;}

private nc.ui.to.m5x.maintain.editor.headevent.HeadBeforeEditHandler getHeadBeforeEditHandler_5a06c0(){
 if(context.get("nc.ui.to.m5x.maintain.editor.headevent.HeadBeforeEditHandler#5a06c0")!=null)
 return (nc.ui.to.m5x.maintain.editor.headevent.HeadBeforeEditHandler)context.get("nc.ui.to.m5x.maintain.editor.headevent.HeadBeforeEditHandler#5a06c0");
  nc.ui.to.m5x.maintain.editor.headevent.HeadBeforeEditHandler bean = new nc.ui.to.m5x.maintain.editor.headevent.HeadBeforeEditHandler();
  context.put("nc.ui.to.m5x.maintain.editor.headevent.HeadBeforeEditHandler#5a06c0",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList16(){  List list = new ArrayList();  list.add(getHeadAfterEditHandler_71400c());  return list;}

private nc.ui.to.m5x.maintain.editor.headevent.HeadAfterEditHandler getHeadAfterEditHandler_71400c(){
 if(context.get("nc.ui.to.m5x.maintain.editor.headevent.HeadAfterEditHandler#71400c")!=null)
 return (nc.ui.to.m5x.maintain.editor.headevent.HeadAfterEditHandler)context.get("nc.ui.to.m5x.maintain.editor.headevent.HeadAfterEditHandler#71400c");
  nc.ui.to.m5x.maintain.editor.headevent.HeadAfterEditHandler bean = new nc.ui.to.m5x.maintain.editor.headevent.HeadAfterEditHandler();
  context.put("nc.ui.to.m5x.maintain.editor.headevent.HeadAfterEditHandler#71400c",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList17(){  List list = new ArrayList();  list.add(getBodyBeforeEditHandler_124085f());  return list;}

private nc.ui.to.m5x.maintain.editor.bodyevent.BodyBeforeEditHandler getBodyBeforeEditHandler_124085f(){
 if(context.get("nc.ui.to.m5x.maintain.editor.bodyevent.BodyBeforeEditHandler#124085f")!=null)
 return (nc.ui.to.m5x.maintain.editor.bodyevent.BodyBeforeEditHandler)context.get("nc.ui.to.m5x.maintain.editor.bodyevent.BodyBeforeEditHandler#124085f");
  nc.ui.to.m5x.maintain.editor.bodyevent.BodyBeforeEditHandler bean = new nc.ui.to.m5x.maintain.editor.bodyevent.BodyBeforeEditHandler();
  context.put("nc.ui.to.m5x.maintain.editor.bodyevent.BodyBeforeEditHandler#124085f",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList18(){  List list = new ArrayList();  list.add(getBodyAfterEditHandler_a19fd0());  return list;}

private nc.ui.to.m5x.maintain.editor.bodyevent.BodyAfterEditHandler getBodyAfterEditHandler_a19fd0(){
 if(context.get("nc.ui.to.m5x.maintain.editor.bodyevent.BodyAfterEditHandler#a19fd0")!=null)
 return (nc.ui.to.m5x.maintain.editor.bodyevent.BodyAfterEditHandler)context.get("nc.ui.to.m5x.maintain.editor.bodyevent.BodyAfterEditHandler#a19fd0");
  nc.ui.to.m5x.maintain.editor.bodyevent.BodyAfterEditHandler bean = new nc.ui.to.m5x.maintain.editor.bodyevent.BodyAfterEditHandler();
  context.put("nc.ui.to.m5x.maintain.editor.bodyevent.BodyAfterEditHandler#a19fd0",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList19(){  List list = new ArrayList();  list.add(getOrgEditHandler_1034a75());  return list;}

private nc.ui.to.m5x.maintain.editor.orgevent.OrgEditHandler getOrgEditHandler_1034a75(){
 if(context.get("nc.ui.to.m5x.maintain.editor.orgevent.OrgEditHandler#1034a75")!=null)
 return (nc.ui.to.m5x.maintain.editor.orgevent.OrgEditHandler)context.get("nc.ui.to.m5x.maintain.editor.orgevent.OrgEditHandler#1034a75");
  nc.ui.to.m5x.maintain.editor.orgevent.OrgEditHandler bean = new nc.ui.to.m5x.maintain.editor.orgevent.OrgEditHandler(getBillFormEditor(),getListView(),getContext());  context.put("nc.ui.to.m5x.maintain.editor.orgevent.OrgEditHandler#1034a75",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList20(){  List list = new ArrayList();  list.add(getOperateLineHandler_10d48de());  return list;}

private nc.ui.to.m5x.maintain.action.OperateLineHandler getOperateLineHandler_10d48de(){
 if(context.get("nc.ui.to.m5x.maintain.action.OperateLineHandler#10d48de")!=null)
 return (nc.ui.to.m5x.maintain.action.OperateLineHandler)context.get("nc.ui.to.m5x.maintain.action.OperateLineHandler#10d48de");
  nc.ui.to.m5x.maintain.action.OperateLineHandler bean = new nc.ui.to.m5x.maintain.action.OperateLineHandler();
  context.put("nc.ui.to.m5x.maintain.action.OperateLineHandler#10d48de",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList21(){  List list = new ArrayList();  list.add(getScmCardLoadhandler());  return list;}

public nc.ui.scmpub.listener.ScmCardItemsFillableHandler getScmCardLoadhandler(){
 if(context.get("scmCardLoadhandler")!=null)
 return (nc.ui.scmpub.listener.ScmCardItemsFillableHandler)context.get("scmCardLoadhandler");
  nc.ui.scmpub.listener.ScmCardItemsFillableHandler bean = new nc.ui.scmpub.listener.ScmCardItemsFillableHandler();
  context.put("scmCardLoadhandler",bean);
  bean.setEnabledItems(getManagedMap3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap3(){  Map map = new HashMap();  map.put("base",getManagedList22());  return map;}

private List getManagedList22(){  List list = new ArrayList();  list.add("coutstordocid");  list.add("cinstordocid");  list.add("ctoutstordocid");  list.add("cinpsnid");  list.add("cindeptvid");  list.add("cprojectid");  list.add("cvendorid");  list.add("cproductorid");  list.add("casscustid");  list.add("cconsighvendorid");  list.add("creceiveaddrid");  list.add("creceiveareaid");  list.add("creceivesiteid");  list.add("csendtypeid");  list.add("dplanoutdate");  list.add("dplanarrivedate");  list.add("drequiredate");  list.add("vbnote");  list.add("vbrevisereason");  list.add("vbdef1");  list.add("vbdef2");  list.add("vbdef3");  list.add("vbdef4");  list.add("vbdef5");  list.add("vbdef6");  list.add("vbdef7");  list.add("vbdef8");  list.add("vbdef9");  list.add("vbdef10");  list.add("vbdef11");  list.add("vbdef12");  list.add("vbdef13");  list.add("vbdef14");  list.add("vbdef15");  list.add("vbdef16");  list.add("vbdef17");  list.add("vbdef18");  list.add("vbdef19");  list.add("vbdef20");  return list;}

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

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayoutRoot(getTBNode_1622fd5());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_1622fd5(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#1622fd5")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#1622fd5");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#1622fd5",bean);
  bean.setShowMode("CardLayout");
  bean.setTabs(getManagedList23());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList23(){  List list = new ArrayList();  list.add(getHSNode_8101b3());  list.add(getVSNode_1107c91());  return list;}

private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_8101b3(){
 if(context.get("nc.ui.uif2.tangramlayout.node.HSNode#8101b3")!=null)
 return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#8101b3");
  nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
  context.put("nc.ui.uif2.tangramlayout.node.HSNode#8101b3",bean);
  bean.setLeft(getCNode_12b0e57());
  bean.setRight(getVSNode_160aeff());
  bean.setDividerLocation(0.22f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_12b0e57(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#12b0e57")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#12b0e57");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#12b0e57",bean);
  bean.setComponent(getQueryArea());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_160aeff(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#160aeff")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#160aeff");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#160aeff",bean);
  bean.setUp(getCNode_1ba83ca());
  bean.setDown(getCNode_1e55d8c());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1ba83ca(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1ba83ca")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1ba83ca");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1ba83ca",bean);
  bean.setComponent(getListToolbarPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1e55d8c(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1e55d8c")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1e55d8c");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1e55d8c",bean);
  bean.setName(getI18nFB_2273df());
  bean.setComponent(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_2273df(){
 if(context.get("nc.ui.uif2.I18nFB#2273df")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#2273df");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#2273df",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000107");
  bean.setDefaultValue("列表");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#2273df",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_1107c91(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#1107c91")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#1107c91");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#1107c91",bean);
  bean.setUp(getCNode_196973c());
  bean.setDown(getVSNode_adc78());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_196973c(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#196973c")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#196973c");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#196973c",bean);
  bean.setComponent(getCardToolbarPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_adc78(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#adc78")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#adc78");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#adc78",bean);
  bean.setName(getI18nFB_1a2e701());
  bean.setUp(getCNode_5f7821());
  bean.setDown(getCNode_1c7c3d9());
  bean.setDividerLocation(0.8f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1a2e701(){
 if(context.get("nc.ui.uif2.I18nFB#1a2e701")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1a2e701");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1a2e701",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000106");
  bean.setDefaultValue("卡片");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1a2e701",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_5f7821(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#5f7821")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#5f7821");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#5f7821",bean);
  bean.setComponent(getBillFormEditor());
  bean.setName(getI18nFB_1ec24bb());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1ec24bb(){
 if(context.get("nc.ui.uif2.I18nFB#1ec24bb")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1ec24bb");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1ec24bb",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000106");
  bean.setDefaultValue("卡片");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1ec24bb",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1c7c3d9(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1c7c3d9")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1c7c3d9");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1c7c3d9",bean);
  bean.setComponent(getOhandcard());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getListToolbarPnl(){
 if(context.get("listToolbarPnl")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("listToolbarPnl");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("listToolbarPnl",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel getCardToolbarPnl(){
 if(context.get("cardToolbarPnl")!=null)
 return (nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel)context.get("cardToolbarPnl");
  nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel bean = new nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel();
  context.put("cardToolbarPnl",bean);
  bean.setTitleAction(getReturnaction());
  bean.setModel(getManageAppModel());
  bean.setRightExActions(getManagedList24());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.UEReturnAction getReturnaction(){
 if(context.get("returnaction")!=null)
 return (nc.ui.pubapp.uif2app.actions.UEReturnAction)context.get("returnaction");
  nc.ui.pubapp.uif2app.actions.UEReturnAction bean = new nc.ui.pubapp.uif2app.actions.UEReturnAction();
  context.put("returnaction",bean);
  bean.setGoComponent(getListView());
  bean.setSaveAction(getSaveAction());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList24(){  List list = new ArrayList();  list.add(getActionsBar_ActionsBarSeparator_13256fe());  list.add(getHeadZoomAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_13256fe(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#13256fe")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#13256fe");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#13256fe",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.DefaultHeadZoomAction getHeadZoomAction(){
 if(context.get("headZoomAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.DefaultHeadZoomAction)context.get("headZoomAction");
  nc.ui.pubapp.uif2app.actions.DefaultHeadZoomAction bean = new nc.ui.pubapp.uif2app.actions.DefaultHeadZoomAction();
  context.put("headZoomAction",bean);
  bean.setBillForm(getBillFormEditor());
  bean.setModel(getManageAppModel());
  bean.setPos(0);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.ActionContributors getToftpanelActionContributors(){
 if(context.get("toftpanelActionContributors")!=null)
 return (nc.ui.uif2.actions.ActionContributors)context.get("toftpanelActionContributors");
  nc.ui.uif2.actions.ActionContributors bean = new nc.ui.uif2.actions.ActionContributors();
  context.put("toftpanelActionContributors",bean);
  bean.setContributors(getManagedList25());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList25(){  List list = new ArrayList();  list.add(getActionsOfList());  list.add(getActionsOfCard());  return list;}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfList(){
 if(context.get("actionsOfList")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("actionsOfList");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getListView());  context.put("actionsOfList",bean);
  bean.setActions(getManagedList26());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList26(){  List list = new ArrayList();  list.add(getAddMenu());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getCopyAction());  list.add(getSeparatorAction_1ed6443());  list.add(getQueryAction());  list.add(getListRefreshAction());  list.add(getSeparatorAction_5196c7());  list.add(getSendApproveActionGroup());  list.add(getApproveActionGroup());  list.add(getAssitfuncActionGroup());  list.add(getSeparatorAction_820e10());  list.add(getLinkQueryActionGroup());  list.add(getSeparatorAction_69596b());  list.add(getPrintActionGroup());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1ed6443(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#1ed6443")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#1ed6443");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#1ed6443",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_5196c7(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#5196c7")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#5196c7");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#5196c7",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_820e10(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#820e10")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#820e10");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#820e10",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_69596b(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#69596b")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#69596b");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#69596b",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfCard(){
 if(context.get("actionsOfCard")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("actionsOfCard");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBillFormEditor());  context.put("actionsOfCard",bean);
  bean.setActions(getManagedList27());
  bean.setEditActions(getManagedList28());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList27(){  List list = new ArrayList();  list.add(getAddMenu());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getCopyAction());  list.add(getSeparatorAction_6db246());  list.add(getQueryAction());  list.add(getCardRefreshAction());  list.add(getSeparatorAction_1d34c5a());  list.add(getSendApproveActionGroup());  list.add(getApproveActionGroup());  list.add(getAssitfuncActionGroup());  list.add(getSeparatorAction_139ea46());  list.add(getLinkQueryActionGroup());  list.add(getSeparatorAction_1439e65());  list.add(getPrintActionGroup());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_6db246(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#6db246")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#6db246");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#6db246",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1d34c5a(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#1d34c5a")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#1d34c5a");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#1d34c5a",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_139ea46(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#139ea46")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#139ea46");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#139ea46",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1439e65(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#1439e65")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#1439e65");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#1439e65",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList28(){  List list = new ArrayList();  list.add(getSaveAction());  list.add(getSendApproveForEditAction());  list.add(getSeparatorAction_541642());  list.add(getCancelAction());  list.add(getSeparatorAction_814a4c());  list.add(getEditAssitFuncActionGroup());  list.add(getSeparatorAction_350269());  list.add(getEditlinkQueryActionGroup());  list.add(getSeparatorAction_315b67());  list.add(getInquaryAction());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_541642(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#541642")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#541642");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#541642",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_814a4c(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#814a4c")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#814a4c");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#814a4c",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_350269(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#350269")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#350269");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#350269",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_315b67(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#315b67")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#315b67");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#315b67",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.TransOrderAddAction getAddManualAction(){
 if(context.get("addManualAction")!=null)
 return (nc.ui.to.m5x.maintain.action.TransOrderAddAction)context.get("addManualAction");
  nc.ui.to.m5x.maintain.action.TransOrderAddAction bean = new nc.ui.to.m5x.maintain.action.TransOrderAddAction();
  context.put("addManualAction",bean);
  bean.setSourceBillType("MANUAL");
  bean.setModel(getManageAppModel());
  bean.setInterceptor(getCompositeActionInterceptor_9d9c9a());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor getCompositeActionInterceptor_9d9c9a(){
 if(context.get("nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor#9d9c9a")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor)context.get("nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor#9d9c9a");
  nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor();
  context.put("nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor#9d9c9a",bean);
  bean.setInterceptors(getManagedList29());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList29(){  List list = new ArrayList();  list.add(getShowUpComponentInterceptor_67cc6b());  return list;}

private nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getShowUpComponentInterceptor_67cc6b(){
 if(context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#67cc6b")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#67cc6b");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#67cc6b",bean);
  bean.setShowUpComponent(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.SetDefValueForDest getTransferLogic(){
 if(context.get("transferLogic")!=null)
 return (nc.ui.to.m5x.maintain.action.SetDefValueForDest)context.get("transferLogic");
  nc.ui.to.m5x.maintain.action.SetDefValueForDest bean = new nc.ui.to.m5x.maintain.action.SetDefValueForDest();
  context.put("transferLogic",bean);
  bean.setBillForm(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.billref.dest.TransferViewProcessor getTransferProcessor(){
 if(context.get("transferProcessor")!=null)
 return (nc.ui.pubapp.billref.dest.TransferViewProcessor)context.get("transferProcessor");
  nc.ui.pubapp.billref.dest.TransferViewProcessor bean = new nc.ui.pubapp.billref.dest.TransferViewProcessor();
  context.put("transferProcessor",bean);
  bean.setList(getListView());
  bean.setActionContainer(getActionsOfList());
  bean.setCardActionContainer(getActionsOfCard());
  bean.setSaveAction(getSaveAction());
  bean.setCommitAction(getSendApproveAction());
  bean.setCancelAction(getCancelAction());
  bean.setBillForm(getBillFormEditor());
  bean.setTransferLogic(getTransferLogic());
  bean.setQueryInfoToolbarPanel(getListToolbarPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.TransOrderAddFromSourceAction getAddFrom5AAction(){
 if(context.get("addFrom5AAction")!=null)
 return (nc.ui.to.m5x.maintain.action.TransOrderAddFromSourceAction)context.get("addFrom5AAction");
  nc.ui.to.m5x.maintain.action.TransOrderAddFromSourceAction bean = new nc.ui.to.m5x.maintain.action.TransOrderAddFromSourceAction();
  context.put("addFrom5AAction",bean);
  bean.setSourceBillType("5A");
  bean.setSourceBillName(getI18nFB_1945330());
  bean.setFlowBillType(false);
  bean.setModel(getManageAppModel());
  bean.setProcessor(getTransferProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1945330(){
 if(context.get("nc.ui.uif2.I18nFB#1945330")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1945330");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1945330",bean);  bean.setResDir("4009011_0");
  bean.setResId("04009011-0308");
  bean.setDefaultValue("调入申请单");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1945330",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.pubapp.uif2app.actions.AddMenuAction getAddMenu(){
 if(context.get("addMenu")!=null)
 return (nc.ui.pubapp.uif2app.actions.AddMenuAction)context.get("addMenu");
  nc.ui.pubapp.uif2app.actions.AddMenuAction bean = new nc.ui.pubapp.uif2app.actions.AddMenuAction();
  context.put("addMenu",bean);
  bean.setBillType("5X");
  bean.setTooltip(getI18nFB_e19b13());
  bean.setActions(getManagedList30());
  bean.setPfAddInfoLoader(getPfAddInfoLoader());
  bean.setModel(getManageAppModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_e19b13(){
 if(context.get("nc.ui.uif2.I18nFB#e19b13")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#e19b13");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#e19b13",bean);  bean.setResDir("4009011_0");
  bean.setResId("04009011-0370");
  bean.setDefaultValue("新增业务数据(Ctrl+N)");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#e19b13",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList30(){  List list = new ArrayList();  list.add(getAddManualAction());  list.add(getSeparatorAction_18c395e());  list.add(getAddFrom5AAction());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_18c395e(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#18c395e")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#18c395e");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#18c395e",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getSendApproveActionGroup(){
 if(context.get("sendApproveActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("sendApproveActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("sendApproveActionGroup",bean);
  bean.setCode("sendApprove");
  bean.setName(getI18nFB_10a7632());
  bean.setActions(getManagedList31());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_10a7632(){
 if(context.get("nc.ui.uif2.I18nFB#10a7632")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#10a7632");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#10a7632",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000029");
  bean.setDefaultValue("提交");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#10a7632",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList31(){  List list = new ArrayList();  list.add(getSendApproveAction());  list.add(getCallBackAction());  return list;}

public nc.ui.to.m5x.maintain.action.TransOrderSendApproveAction getSendApproveAction(){
 if(context.get("sendApproveAction")!=null)
 return (nc.ui.to.m5x.maintain.action.TransOrderSendApproveAction)context.get("sendApproveAction");
  nc.ui.to.m5x.maintain.action.TransOrderSendApproveAction bean = new nc.ui.to.m5x.maintain.action.TransOrderSendApproveAction();
  context.put("sendApproveAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFilledUpInFlow(true);
  bean.setEditor(getBillFormEditor());
  bean.setSave(getSaveAction());
  bean.setActionName("SAVE");
  bean.setBillType("5X");
  bean.setValidationService(getPowerSavevalidservice());
  bean.setTpaProgressUtil(getTpaprogressutil());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction getSendApproveForEditAction(){
 if(context.get("sendApproveForEditAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction)context.get("sendApproveForEditAction");
  nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction bean = new nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction(getSaveAction(),getSendApproveAction());  context.put("sendApproveForEditAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getPowerSavevalidservice(){
 if(context.get("powerSavevalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerSavevalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("powerSavevalidservice",bean);
  bean.setActionCode("commit");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.CallBackAction getCallBackAction(){
 if(context.get("callBackAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.CallBackAction)context.get("callBackAction");
  nc.ui.to.m5x.maintain.action.assitfunc.CallBackAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.CallBackAction();
  context.put("callBackAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFilledUpInFlow(true);
  bean.setEditor(getBillFormEditor());
  bean.setActionName("UNSAVE");
  bean.setBillType("5X");
  bean.setValidationService(getPowerunSavevalidservice());
  bean.setTpaProgressUtil(getTpaprogressutil());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getPowerunSavevalidservice(){
 if(context.get("powerunSavevalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerunSavevalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("powerunSavevalidservice",bean);
  bean.setActionCode("uncommit");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.TransOrderApproveAction getApproveAction(){
 if(context.get("approveAction")!=null)
 return (nc.ui.to.m5x.maintain.action.TransOrderApproveAction)context.get("approveAction");
  nc.ui.to.m5x.maintain.action.TransOrderApproveAction bean = new nc.ui.to.m5x.maintain.action.TransOrderApproveAction();
  context.put("approveAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFilledUpInFlow(true);
  bean.setActionName("APPROVE");
  bean.setEditor(getBillFormEditor());
  bean.setBillType("5X");
  bean.setValidationService(getPowerapprovevalidservice());
  bean.setTpaProgressUtil(getTpaprogressutil());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getPowerapprovevalidservice(){
 if(context.get("powerapprovevalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerapprovevalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("powerapprovevalidservice",bean);
  bean.setActionCode("approve");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.TransOrderUnApproveAction getUnApproveAction(){
 if(context.get("unApproveAction")!=null)
 return (nc.ui.to.m5x.maintain.action.TransOrderUnApproveAction)context.get("unApproveAction");
  nc.ui.to.m5x.maintain.action.TransOrderUnApproveAction bean = new nc.ui.to.m5x.maintain.action.TransOrderUnApproveAction();
  context.put("unApproveAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFilledUpInFlow(true);
  bean.setActionName("UNAPPROVE");
  bean.setEditor(getBillFormEditor());
  bean.setBillType("5X");
  bean.setValidationService(getPowerunapprovevalidservice());
  bean.setTpaProgressUtil(getTpaprogressutil());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getPowerunapprovevalidservice(){
 if(context.get("powerunapprovevalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerunapprovevalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("powerunapprovevalidservice",bean);
  bean.setActionCode("unapprove");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getApproveActionGroup(){
 if(context.get("approveActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("approveActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("approveActionGroup",bean);
  bean.setCode("assitfunc");
  bean.setName(getI18nFB_1d6027e());
  bean.setActions(getManagedList32());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1d6027e(){
 if(context.get("nc.ui.uif2.I18nFB#1d6027e")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1d6027e");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1d6027e",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000027");
  bean.setDefaultValue("审核");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1d6027e",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList32(){  List list = new ArrayList();  list.add(getApproveAction());  list.add(getUnApproveAction());  list.add(getSeparatorAction_13db5b4());  list.add(getApproveStateAction());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_13db5b4(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#13db5b4")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#13db5b4");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#13db5b4",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction getQueryAction(){
 if(context.get("queryAction")!=null)
 return (nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction)context.get("queryAction");
  nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction();
  context.put("queryAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getModelDataManager());
  bean.setQryCondDLGInitializer(getM5xQryCondDLGInitializer());
  bean.setTemplateContainer(getQueryTemplateContainer());
  bean.setShowUpComponent(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction getListRefreshAction(){
 if(context.get("listRefreshAction")!=null)
 return (nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction)context.get("listRefreshAction");
  nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction();
  context.put("listRefreshAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.RefreshSingleAction getCardRefreshAction(){
 if(context.get("cardRefreshAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.RefreshSingleAction)context.get("cardRefreshAction");
  nc.ui.pubapp.uif2app.actions.RefreshSingleAction bean = new nc.ui.pubapp.uif2app.actions.RefreshSingleAction();
  context.put("cardRefreshAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.TransOrderEditAction getEditAction(){
 if(context.get("editAction")!=null)
 return (nc.ui.to.m5x.maintain.action.TransOrderEditAction)context.get("editAction");
  nc.ui.to.m5x.maintain.action.TransOrderEditAction bean = new nc.ui.to.m5x.maintain.action.TransOrderEditAction();
  context.put("editAction",bean);
  bean.setModel(getManageAppModel());
  bean.setInterceptor(getShowUpComponentInterceptor_645bd5());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getShowUpComponentInterceptor_645bd5(){
 if(context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#645bd5")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#645bd5");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#645bd5",bean);
  bean.setShowUpComponent(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.TransOrderDeleteAction getDeleteAction(){
 if(context.get("deleteAction")!=null)
 return (nc.ui.to.m5x.maintain.action.TransOrderDeleteAction)context.get("deleteAction");
  nc.ui.to.m5x.maintain.action.TransOrderDeleteAction bean = new nc.ui.to.m5x.maintain.action.TransOrderDeleteAction();
  context.put("deleteAction",bean);
  bean.setModel(getManageAppModel());
  bean.setActionName("DELETE");
  bean.setBillType("5X");
  bean.setValidationService(getPowerdeletevalidservice());
  bean.setTpaProgressUtil(getTpaprogressutil());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getPowerdeletevalidservice(){
 if(context.get("powerdeletevalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerdeletevalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("powerdeletevalidservice",bean);
  bean.setActionCode("delete");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.BillCopyAction getCopyAction(){
 if(context.get("copyAction")!=null)
 return (nc.ui.to.m5x.maintain.action.BillCopyAction)context.get("copyAction");
  nc.ui.to.m5x.maintain.action.BillCopyAction bean = new nc.ui.to.m5x.maintain.action.BillCopyAction();
  context.put("copyAction",bean);
  bean.setInterceptor(getCompositeActionInterceptor_1561ec7());
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setCopyActionProcessor(getCopyActionProcessor_fed4f2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor getCompositeActionInterceptor_1561ec7(){
 if(context.get("nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor#1561ec7")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor)context.get("nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor#1561ec7");
  nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor();
  context.put("nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor#1561ec7",bean);
  bean.setInterceptors(getManagedList33());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList33(){  List list = new ArrayList();  list.add(getShowUpComponentInterceptor_c90c89());  return list;}

private nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getShowUpComponentInterceptor_c90c89(){
 if(context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#c90c89")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#c90c89");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#c90c89",bean);
  bean.setShowUpComponent(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.to.m5x.maintain.action.CopyActionProcessor getCopyActionProcessor_fed4f2(){
 if(context.get("nc.ui.to.m5x.maintain.action.CopyActionProcessor#fed4f2")!=null)
 return (nc.ui.to.m5x.maintain.action.CopyActionProcessor)context.get("nc.ui.to.m5x.maintain.action.CopyActionProcessor#fed4f2");
  nc.ui.to.m5x.maintain.action.CopyActionProcessor bean = new nc.ui.to.m5x.maintain.action.CopyActionProcessor();
  context.put("nc.ui.to.m5x.maintain.action.CopyActionProcessor#fed4f2",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.TransOrderSaveAction getSaveAction(){
 if(context.get("saveAction")!=null)
 return (nc.ui.to.m5x.maintain.action.TransOrderSaveAction)context.get("saveAction");
  nc.ui.to.m5x.maintain.action.TransOrderSaveAction bean = new nc.ui.to.m5x.maintain.action.TransOrderSaveAction();
  context.put("saveAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setActionName("WRITE");
  bean.setBillType("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.CancelAction getCancelAction(){
 if(context.get("cancelAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.CancelAction)context.get("cancelAction");
  nc.ui.pubapp.uif2app.actions.CancelAction bean = new nc.ui.pubapp.uif2app.actions.CancelAction();
  context.put("cancelAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getAssitfuncActionGroup(){
 if(context.get("assitfuncActionGroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("assitfuncActionGroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("assitfuncActionGroup",bean);
  bean.setCode("assitfunc");
  bean.setName(getI18nFB_4e835b());
  bean.setActions(getManagedList34());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_4e835b(){
 if(context.get("nc.ui.uif2.I18nFB#4e835b")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#4e835b");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#4e835b",bean);  bean.setResDir("4009002_0");
  bean.setResId("04009002-0168");
  bean.setDefaultValue("辅助功能");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#4e835b",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList34(){  List list = new ArrayList();  list.add(getFillInvAction());  list.add(getSendInvAction());  list.add(getSeparatorAction_9203f0());  list.add(getTransInfoAction());  list.add(getObligateQueryAction());  list.add(getObligateAction());  list.add(getSeparatorAction_40b525());  list.add(getFreezeAction());  list.add(getThawAction());  list.add(getBillOpenAction());  list.add(getBillCloseAction());  list.add(getSeparatorAction_34a7dc());  list.add(getReturnAction());  list.add(getSeparatorAction_f869f4());  list.add(getDocMngAction());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_9203f0(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#9203f0")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#9203f0");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#9203f0",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_40b525(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#40b525")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#40b525");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#40b525",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_34a7dc(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#34a7dc")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#34a7dc");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#34a7dc",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_f869f4(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#f869f4")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#f869f4");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#f869f4",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.FillInvAction getFillInvAction(){
 if(context.get("fillInvAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.FillInvAction)context.get("fillInvAction");
  nc.ui.to.m5x.maintain.action.assitfunc.FillInvAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.FillInvAction();
  context.put("fillInvAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.SendInvAction getSendInvAction(){
 if(context.get("sendInvAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.SendInvAction)context.get("sendInvAction");
  nc.ui.to.m5x.maintain.action.assitfunc.SendInvAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.SendInvAction();
  context.put("sendInvAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.TransInfoAction getTransInfoAction(){
 if(context.get("transInfoAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.TransInfoAction)context.get("transInfoAction");
  nc.ui.to.m5x.maintain.action.assitfunc.TransInfoAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.TransInfoAction();
  context.put("transInfoAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.ObligateQueryAction getObligateQueryAction(){
 if(context.get("obligateQueryAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.ObligateQueryAction)context.get("obligateQueryAction");
  nc.ui.to.m5x.maintain.action.assitfunc.ObligateQueryAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.ObligateQueryAction();
  context.put("obligateQueryAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.ObligateAction getObligateAction(){
 if(context.get("obligateAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.ObligateAction)context.get("obligateAction");
  nc.ui.to.m5x.maintain.action.assitfunc.ObligateAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.ObligateAction();
  context.put("obligateAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setRefreshAction(getCardRefreshAction());
  bean.setListview(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.FreezeAction getFreezeAction(){
 if(context.get("freezeAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.FreezeAction)context.get("freezeAction");
  nc.ui.to.m5x.maintain.action.assitfunc.FreezeAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.FreezeAction();
  context.put("freezeAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setActionName("FREEZE");
  bean.setBillType("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.ThawAction getThawAction(){
 if(context.get("thawAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.ThawAction)context.get("thawAction");
  nc.ui.to.m5x.maintain.action.assitfunc.ThawAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.ThawAction();
  context.put("thawAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setActionName("UNFREEZE");
  bean.setBillType("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.BillOpenAction getBillOpenAction(){
 if(context.get("billOpenAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.BillOpenAction)context.get("billOpenAction");
  nc.ui.to.m5x.maintain.action.assitfunc.BillOpenAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.BillOpenAction();
  context.put("billOpenAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setActionName("OPENBILL");
  bean.setBillType("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.BillCloseAction getBillCloseAction(){
 if(context.get("billCloseAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.BillCloseAction)context.get("billCloseAction");
  nc.ui.to.m5x.maintain.action.assitfunc.BillCloseAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.BillCloseAction();
  context.put("billCloseAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setActionName("CLOSEBILL");
  bean.setBillType("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.assitfunc.ReturnAction getReturnAction(){
 if(context.get("returnAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.ReturnAction)context.get("returnAction");
  nc.ui.to.m5x.maintain.action.assitfunc.ReturnAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.ReturnAction();
  context.put("returnAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setInterceptor(getShowUpComponentInterceptor_f44f31());
  bean.setCopyActionProcessor(getReturnActionProcessor_17b320c());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getShowUpComponentInterceptor_f44f31(){
 if(context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#f44f31")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#f44f31");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#f44f31",bean);
  bean.setShowUpComponent(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.to.m5x.maintain.action.assitfunc.ReturnActionProcessor getReturnActionProcessor_17b320c(){
 if(context.get("nc.ui.to.m5x.maintain.action.assitfunc.ReturnActionProcessor#17b320c")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.ReturnActionProcessor)context.get("nc.ui.to.m5x.maintain.action.assitfunc.ReturnActionProcessor#17b320c");
  nc.ui.to.m5x.maintain.action.assitfunc.ReturnActionProcessor bean = new nc.ui.to.m5x.maintain.action.assitfunc.ReturnActionProcessor();
  context.put("nc.ui.to.m5x.maintain.action.assitfunc.ReturnActionProcessor#17b320c",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.FileDocManageAction getDocMngAction(){
 if(context.get("docMngAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.FileDocManageAction)context.get("docMngAction");
  nc.ui.pubapp.uif2app.actions.FileDocManageAction bean = new nc.ui.pubapp.uif2app.actions.FileDocManageAction();
  context.put("docMngAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getEditAssitFuncActionGroup(){
 if(context.get("editAssitFuncActionGroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("editAssitFuncActionGroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("editAssitFuncActionGroup",bean);
  bean.setCode("assitfunc");
  bean.setName(getI18nFB_112c71a());
  bean.setActions(getManagedList35());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_112c71a(){
 if(context.get("nc.ui.uif2.I18nFB#112c71a")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#112c71a");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#112c71a",bean);  bean.setResDir("4009002_0");
  bean.setResId("04009002-0168");
  bean.setDefaultValue("辅助功能");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#112c71a",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList35(){  List list = new ArrayList();  list.add(getRef5AAddLineAction());  list.add(getAskPriceAction());  return list;}

public nc.ui.to.m5x.maintain.action.assitfunc.Ref5AAddLineAction getRef5AAddLineAction(){
 if(context.get("ref5AAddLineAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.Ref5AAddLineAction)context.get("ref5AAddLineAction");
  nc.ui.to.m5x.maintain.action.assitfunc.Ref5AAddLineAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.Ref5AAddLineAction();
  context.put("ref5AAddLineAction",bean);
  bean.setEditor(getBillFormEditor());
  bean.setSourceBillType("5A");
  bean.setSourceBillName(getI18nFB_1cda89f());
  bean.setFlowBillType(false);
  bean.setModel(getManageAppModel());
  bean.setProcessor(getTransferProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1cda89f(){
 if(context.get("nc.ui.uif2.I18nFB#1cda89f")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1cda89f");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1cda89f",bean);  bean.setResDir("4009011_0");
  bean.setResId("04009011-0308");
  bean.setDefaultValue("调入申请单");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1cda89f",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.to.m5x.maintain.action.assitfunc.AskPriceAction getAskPriceAction(){
 if(context.get("askPriceAction")!=null)
 return (nc.ui.to.m5x.maintain.action.assitfunc.AskPriceAction)context.get("askPriceAction");
  nc.ui.to.m5x.maintain.action.assitfunc.AskPriceAction bean = new nc.ui.to.m5x.maintain.action.assitfunc.AskPriceAction();
  context.put("askPriceAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getLinkQueryActionGroup(){
 if(context.get("linkQueryActionGroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("linkQueryActionGroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("linkQueryActionGroup",bean);
  bean.setCode("linkQuery");
  bean.setName(getI18nFB_f967da());
  bean.setActions(getManagedList36());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_f967da(){
 if(context.get("nc.ui.uif2.I18nFB#f967da")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#f967da");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#f967da",bean);  bean.setResDir("4009002_0");
  bean.setResId("04009002-0167");
  bean.setDefaultValue("联查");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#f967da",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList36(){  List list = new ArrayList();  list.add(getLinkQueryAction());  list.add(getSpShowHiddenAction());  return list;}

public nc.funcnode.ui.action.MenuAction getEditlinkQueryActionGroup(){
 if(context.get("editlinkQueryActionGroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("editlinkQueryActionGroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("editlinkQueryActionGroup",bean);
  bean.setCode("editlinkQuery");
  bean.setName(getI18nFB_1144cb());
  bean.setActions(getManagedList37());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1144cb(){
 if(context.get("nc.ui.uif2.I18nFB#1144cb")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1144cb");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1144cb",bean);  bean.setResDir("4009002_0");
  bean.setResId("04009002-0167");
  bean.setDefaultValue("联查");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1144cb",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList37(){  List list = new ArrayList();  list.add(getSpShowHiddenAction());  return list;}

public nc.scmmm.ui.uif2.actions.SCMLinkQueryAction getLinkQueryAction(){
 if(context.get("linkQueryAction")!=null)
 return (nc.scmmm.ui.uif2.actions.SCMLinkQueryAction)context.get("linkQueryAction");
  nc.scmmm.ui.uif2.actions.SCMLinkQueryAction bean = new nc.scmmm.ui.uif2.actions.SCMLinkQueryAction();
  context.put("linkQueryAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBillType("5X");
  bean.setOpenMode(1);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.pub.action.QueryOnhandAction getSpShowHiddenAction(){
 if(context.get("spShowHiddenAction")!=null)
 return (nc.ui.to.pub.action.QueryOnhandAction)context.get("spShowHiddenAction");
  nc.ui.to.pub.action.QueryOnhandAction bean = new nc.ui.to.pub.action.QueryOnhandAction();
  context.put("spShowHiddenAction",bean);
  bean.setEditorModel(getManageAppModel());
  bean.setBillForm(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction getApproveStateAction(){
 if(context.get("approveStateAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction)context.get("approveStateAction");
  nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction bean = new nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction();
  context.put("approveStateAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintActionGroup(){
 if(context.get("printActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printActionGroup",bean);
  bean.setCode("printgroup");
  bean.setName(getI18nFB_1bb8d4f());
  bean.setActions(getManagedList38());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1bb8d4f(){
 if(context.get("nc.ui.uif2.I18nFB#1bb8d4f")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1bb8d4f");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1bb8d4f",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000007");
  bean.setDefaultValue("打印");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1bb8d4f",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList38(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreviewAction());  list.add(getOutputAction());  list.add(getSeparatorAction_3a7cff());  list.add(getSplitPrintAction());  list.add(getCombineAction());  list.add(getPrintAccountQueryAction());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_3a7cff(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#3a7cff")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#3a7cff");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#3a7cff",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmpub.action.SCMPrintCountQueryAction getPrintAccountQueryAction(){
 if(context.get("printAccountQueryAction")!=null)
 return (nc.ui.scmpub.action.SCMPrintCountQueryAction)context.get("printAccountQueryAction");
  nc.ui.scmpub.action.SCMPrintCountQueryAction bean = new nc.ui.scmpub.action.SCMPrintCountQueryAction();
  context.put("printAccountQueryAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBilldateFieldName("dbilldate");
  bean.setBillType("5X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.print.CombineAction getCombineAction(){
 if(context.get("combineAction")!=null)
 return (nc.ui.to.m5x.maintain.action.print.CombineAction)context.get("combineAction");
  nc.ui.to.m5x.maintain.action.print.CombineAction bean = new nc.ui.to.m5x.maintain.action.print.CombineAction();
  context.put("combineAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.print.BillPrintAction getPreviewAction(){
 if(context.get("previewAction")!=null)
 return (nc.ui.to.m5x.maintain.action.print.BillPrintAction)context.get("previewAction");
  nc.ui.to.m5x.maintain.action.print.BillPrintAction bean = new nc.ui.to.m5x.maintain.action.print.BillPrintAction();
  context.put("previewAction",bean);
  bean.setPreview(true);
  bean.setModel(getManageAppModel());
  bean.setBeforePrintDataProcess(getPrintProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.print.BillPrintAction getPrintAction(){
 if(context.get("printAction")!=null)
 return (nc.ui.to.m5x.maintain.action.print.BillPrintAction)context.get("printAction");
  nc.ui.to.m5x.maintain.action.print.BillPrintAction bean = new nc.ui.to.m5x.maintain.action.print.BillPrintAction();
  context.put("printAction",bean);
  bean.setPreview(false);
  bean.setModel(getManageAppModel());
  bean.setBeforePrintDataProcess(getPrintProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.pub.TransorderPrintProcessor getPrintProcessor(){
 if(context.get("printProcessor")!=null)
 return (nc.ui.to.m5x.pub.TransorderPrintProcessor)context.get("printProcessor");
  nc.ui.to.m5x.pub.TransorderPrintProcessor bean = new nc.ui.to.m5x.pub.TransorderPrintProcessor();
  context.put("printProcessor",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.action.print.SplitPrintProcessor getPrintdataSplitProcessor(){
 if(context.get("printdataSplitProcessor")!=null)
 return (nc.ui.to.m5x.maintain.action.print.SplitPrintProcessor)context.get("printdataSplitProcessor");
  nc.ui.to.m5x.maintain.action.print.SplitPrintProcessor bean = new nc.ui.to.m5x.maintain.action.print.SplitPrintProcessor();
  context.put("printdataSplitProcessor",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.action.SplitPrintAction getSplitPrintAction(){
 if(context.get("splitPrintAction")!=null)
 return (nc.ui.pubapp.action.SplitPrintAction)context.get("splitPrintAction");
  nc.ui.pubapp.action.SplitPrintAction bean = new nc.ui.pubapp.action.SplitPrintAction();
  context.put("splitPrintAction",bean);
  bean.setModel(getManageAppModel());
  bean.setSplitAttributes(getManagedList39());
  bean.setPrintAction(getPreviewAction());
  bean.setPrintdataSplitProcessor(getPrintdataSplitProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList39(){  List list = new ArrayList();  list.add("coutstordocid");  return list;}

public nc.ui.pubapp.uif2app.actions.OutputAction getOutputAction(){
 if(context.get("outputAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.OutputAction)context.get("outputAction");
  nc.ui.pubapp.uif2app.actions.OutputAction bean = new nc.ui.pubapp.uif2app.actions.OutputAction();
  context.put("outputAction",bean);
  bean.setModel(getManageAppModel());
  bean.setParent(getBillFormEditor());
  bean.setBeforePrintDataProcess(getPrintProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.action.InquaryAction getInquaryAction(){
 if(context.get("inquaryAction")!=null)
 return (nc.ui.to.m5x.action.InquaryAction)context.get("inquaryAction");
  nc.ui.to.m5x.action.InquaryAction bean = new nc.ui.to.m5x.action.InquaryAction();
  context.put("inquaryAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.common.validateservice.ClosingCheck getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.pubapp.common.validateservice.ClosingCheck)context.get("ClosingListener");
  nc.ui.pubapp.common.validateservice.ClosingCheck bean = new nc.ui.pubapp.common.validateservice.ClosingCheck();
  context.put("ClosingListener",bean);
  bean.setModel(getManageAppModel());
  bean.setSaveAction(getSaveAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.pagination.PaginationBar getPageBar(){
 if(context.get("pageBar")!=null)
 return (nc.ui.uif2.components.pagination.PaginationBar)context.get("pageBar");
  nc.ui.uif2.components.pagination.PaginationBar bean = new nc.ui.uif2.components.pagination.PaginationBar();
  context.put("pageBar",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.pagination.BillModelPaginationDelegator getPageDelegator(){
 if(context.get("pageDelegator")!=null)
 return (nc.ui.pubapp.uif2app.actions.pagination.BillModelPaginationDelegator)context.get("pageDelegator");
  nc.ui.pubapp.uif2app.actions.pagination.BillModelPaginationDelegator bean = new nc.ui.pubapp.uif2app.actions.pagination.BillModelPaginationDelegator(getManageAppModel());  context.put("pageDelegator",bean);
  bean.setPaginationQuery(getPageQuery());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.to.m5x.maintain.model.TransOrderPageService getPageQuery(){
 if(context.get("pageQuery")!=null)
 return (nc.ui.to.m5x.maintain.model.TransOrderPageService)context.get("pageQuery");
  nc.ui.to.m5x.maintain.model.TransOrderPageService bean = new nc.ui.to.m5x.maintain.model.TransOrderPageService();
  context.put("pageQuery",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmpub.page.model.SCMBillPageMediator getPageMediator(){
 if(context.get("pageMediator")!=null)
 return (nc.ui.scmpub.page.model.SCMBillPageMediator)context.get("pageMediator");
  nc.ui.scmpub.page.model.SCMBillPageMediator bean = new nc.ui.scmpub.page.model.SCMBillPageMediator();
  context.put("pageMediator",bean);
  bean.setListView(getListView());
  bean.setRecordInPage(10);
  bean.setCachePages(10);
  bean.setPageDelegator(getPageDelegator());
  bean.init();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmpub.listener.crossrule.CrossRuleMediator getCrossRuleMediator(){
 if(context.get("crossRuleMediator")!=null)
 return (nc.ui.scmpub.listener.crossrule.CrossRuleMediator)context.get("crossRuleMediator");
  nc.ui.scmpub.listener.crossrule.CrossRuleMediator bean = new nc.ui.scmpub.listener.crossrule.CrossRuleMediator();
  context.put("crossRuleMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setBillType("5X");
  bean.init();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
