package nc.ui.pu.m23.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class arrive_config extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getFormInterceptor(){
 if(context.get("formInterceptor")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("formInterceptor");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("formInterceptor",bean);
  bean.setShowUpComponent(getBillFormEditor());
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

private List getManagedList0(){  List list = new ArrayList();  list.add(getActionsOfList());  list.add(getActionsOfCard());  return list;}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfList(){
 if(context.get("actionsOfList")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("actionsOfList");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getListView());  context.put("actionsOfList",bean);
  bean.setActions(getManagedList1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add(getAddMenuAction());  list.add(getBackMenuAction());  list.add(getUpdateUIAction());  list.add(getDeleteUIAction());  list.add(getSeparatorAction());  list.add(getQueryUIAction());  list.add(getRefreshUIAction());  list.add(getSeparatorAction());  list.add(getSendApproveMenuAction());  list.add(getAuditMenuAction());  list.add(getAssistFuncMenuAction());  list.add(getSeparatorAction());  list.add(getLinkQueryMenuAction_view());  list.add(getSeparatorAction());  list.add(getRelateFuncMenuAction());  list.add(getSeparatorAction());  list.add(getPrintMenuActionList());  return list;}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfCard(){
 if(context.get("actionsOfCard")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("actionsOfCard");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBillFormEditor());  context.put("actionsOfCard",bean);
  bean.setModel(getManageAppModel());
  bean.setActions(getManagedList2());
  bean.setEditActions(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getAddMenuAction());  list.add(getBackMenuAction());  list.add(getUpdateUIAction());  list.add(getDeleteUIAction());  list.add(getSeparatorAction());  list.add(getQueryUIAction());  list.add(getCardRefreshAction());  list.add(getSeparatorAction());  list.add(getSendApproveMenuAction());  list.add(getAuditMenuAction());  list.add(getAssistFuncMenuAction());  list.add(getSeparatorAction());  list.add(getLinkQueryMenuAction_view());  list.add(getSeparatorAction());  list.add(getRelateFuncMenuAction());  list.add(getSeparatorAction());  list.add(getPrintMenuActionCard());   return list;}

private List getManagedList3(){  List list = new ArrayList();  list.add(getSaveUIAction());  list.add(getSaveCommitAction());  list.add(getSeparatorAction());  list.add(getCancelAction());  list.add(getSeparatorAction());  list.add(getLinkQueryMenuAction_edit());  return list;}

public nc.ui.pu.m23.billref.dest.ArriveTransferListProcessor getArriveTransferListProcessor(){
 if(context.get("arriveTransferListProcessor")!=null)
 return (nc.ui.pu.m23.billref.dest.ArriveTransferListProcessor)context.get("arriveTransferListProcessor");
  nc.ui.pu.m23.billref.dest.ArriveTransferListProcessor bean = new nc.ui.pu.m23.billref.dest.ArriveTransferListProcessor();
  context.put("arriveTransferListProcessor",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.billref.dest.TransferViewProcessor getTransferViewProcessor(){
 if(context.get("transferViewProcessor")!=null)
 return (nc.ui.pubapp.billref.dest.TransferViewProcessor)context.get("transferViewProcessor");
  nc.ui.pubapp.billref.dest.TransferViewProcessor bean = new nc.ui.pubapp.billref.dest.TransferViewProcessor();
  context.put("transferViewProcessor",bean);
  bean.setList(getListView());
  bean.setActionContainer(getActionsOfList());
  bean.setCardActionContainer(getActionsOfCard());
  bean.setSaveAction(getSaveUIAction());
  bean.setCommitAction(getSendUIAction());
  bean.setCancelAction(getCancelAction());
  bean.setBillForm(getBillFormEditor());
  bean.setQueryAreaShell(getQueryArea());
  bean.setListProcessor(getArriveTransferListProcessor());
  bean.setQueryInfoToolbarPanel(getQueryInfo());
  bean.setTransferLogic(getDefaultBillDataLogic_15e13b8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.billref.dest.DefaultBillDataLogic getDefaultBillDataLogic_15e13b8(){
 if(context.get("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#15e13b8")!=null)
 return (nc.ui.pubapp.billref.dest.DefaultBillDataLogic)context.get("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#15e13b8");
  nc.ui.pubapp.billref.dest.DefaultBillDataLogic bean = new nc.ui.pubapp.billref.dest.DefaultBillDataLogic();
  context.put("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#15e13b8",bean);
  bean.setBillForm(getBillFormEditor());
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

public nc.ui.pubapp.uif2app.actions.AddMenuAction getAddMenuAction(){
 if(context.get("addMenuAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.AddMenuAction)context.get("addMenuAction");
  nc.ui.pubapp.uif2app.actions.AddMenuAction bean = new nc.ui.pubapp.uif2app.actions.AddMenuAction();
  context.put("addMenuAction",bean);
  bean.setBillType("23");
  bean.setTooltip(getI18nFB_160a625());
  bean.setActions(getManagedList4());
  bean.setModel(getManageAppModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_160a625(){
 if(context.get("nc.ui.uif2.I18nFB#160a625")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#160a625");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#160a625",bean);  bean.setResDir("common");
  bean.setResId("04004000-0001");
  bean.setDefaultValue("新增业务数据(Ctrl+N)");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#160a625",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList4(){  List list = new ArrayList();  list.add(getAddFrom21UIAction());  list.add(getAddFrom61UIAction());  return list;}

private nc.ui.pu.m23.action.maintain.AddFrom21UIAction getAddFrom21UIAction(){
 if(context.get("addFrom21UIAction")!=null)
 return (nc.ui.pu.m23.action.maintain.AddFrom21UIAction)context.get("addFrom21UIAction");
  nc.ui.pu.m23.action.maintain.AddFrom21UIAction bean = new nc.ui.pu.m23.action.maintain.AddFrom21UIAction();
  context.put("addFrom21UIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setTransferViewProcessor(getTransferViewProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.maintain.AddFrom61UIAction getAddFrom61UIAction(){
 if(context.get("addFrom61UIAction")!=null)
 return (nc.ui.pu.m23.action.maintain.AddFrom61UIAction)context.get("addFrom61UIAction");
  nc.ui.pu.m23.action.maintain.AddFrom61UIAction bean = new nc.ui.pu.m23.action.maintain.AddFrom61UIAction();
  context.put("addFrom61UIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setTransferViewProcessor(getTransferViewProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getBackMenuAction(){
 if(context.get("backMenuAction")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("backMenuAction");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("backMenuAction",bean);
  bean.setCode("backMenuAction");
  bean.setName(getI18nFB_3bdb55());
  bean.setTooltip(getI18nFB_1bcc751());
  bean.setActions(getManagedList5());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_3bdb55(){
 if(context.get("nc.ui.uif2.I18nFB#3bdb55")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#3bdb55");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#3bdb55",bean);  bean.setResDir("common");
  bean.setResId("14004000-0006");
  bean.setDefaultValue("退货");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#3bdb55",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private java.lang.String getI18nFB_1bcc751(){
 if(context.get("nc.ui.uif2.I18nFB#1bcc751")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1bcc751");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1bcc751",bean);  bean.setResDir("common");
  bean.setResId("04004000-0002");
  bean.setDefaultValue("退货(Ctrl+1)");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1bcc751",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList5(){  List list = new ArrayList();  list.add(getBackFrom21UIAction());  list.add(getBackFrom61UIAction());  list.add(getBackFrom23UIAction());  return list;}

private nc.ui.pu.m23.action.maintain.BackFrom21UIAction getBackFrom21UIAction(){
 if(context.get("backFrom21UIAction")!=null)
 return (nc.ui.pu.m23.action.maintain.BackFrom21UIAction)context.get("backFrom21UIAction");
  nc.ui.pu.m23.action.maintain.BackFrom21UIAction bean = new nc.ui.pu.m23.action.maintain.BackFrom21UIAction();
  context.put("backFrom21UIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setTransferViewProcessor(getTransferViewProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.maintain.BackFrom61UIAction getBackFrom61UIAction(){
 if(context.get("backFrom61UIAction")!=null)
 return (nc.ui.pu.m23.action.maintain.BackFrom61UIAction)context.get("backFrom61UIAction");
  nc.ui.pu.m23.action.maintain.BackFrom61UIAction bean = new nc.ui.pu.m23.action.maintain.BackFrom61UIAction();
  context.put("backFrom61UIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setTransferViewProcessor(getTransferViewProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.maintain.BackFrom23UIAction getBackFrom23UIAction(){
 if(context.get("backFrom23UIAction")!=null)
 return (nc.ui.pu.m23.action.maintain.BackFrom23UIAction)context.get("backFrom23UIAction");
  nc.ui.pu.m23.action.maintain.BackFrom23UIAction bean = new nc.ui.pu.m23.action.maintain.BackFrom23UIAction();
  context.put("backFrom23UIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setCopyActionProcessor(getCopyActionProcessor());
  bean.setInterceptor(getFormInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.processor.BackFromArriveCopyProcessor getCopyActionProcessor(){
 if(context.get("copyActionProcessor")!=null)
 return (nc.ui.pu.m23.action.processor.BackFromArriveCopyProcessor)context.get("copyActionProcessor");
  nc.ui.pu.m23.action.processor.BackFromArriveCopyProcessor bean = new nc.ui.pu.m23.action.processor.BackFromArriveCopyProcessor();
  context.put("copyActionProcessor",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.maintain.UpdateUIAction getUpdateUIAction(){
 if(context.get("updateUIAction")!=null)
 return (nc.ui.pu.m23.action.maintain.UpdateUIAction)context.get("updateUIAction");
  nc.ui.pu.m23.action.maintain.UpdateUIAction bean = new nc.ui.pu.m23.action.maintain.UpdateUIAction();
  context.put("updateUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBillForm(getBillFormEditor());
  bean.setInterceptor(getShowUpComponentInterceptor_c1af53());
  bean.setPowercheck(true);
  bean.setPermissioncode("23");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getShowUpComponentInterceptor_c1af53(){
 if(context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#c1af53")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#c1af53");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#c1af53",bean);
  bean.setShowUpComponent(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.maintain.DeleteUIAction getDeleteUIAction(){
 if(context.get("deleteUIAction")!=null)
 return (nc.ui.pu.m23.action.maintain.DeleteUIAction)context.get("deleteUIAction");
  nc.ui.pu.m23.action.maintain.DeleteUIAction bean = new nc.ui.pu.m23.action.maintain.DeleteUIAction();
  context.put("deleteUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setActionName("DISCARD");
  bean.setBillType("23");
  bean.setEditor(getBillFormEditor());
  bean.setValidationService(getPowerDeleteservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getPowerDeleteservice(){
 if(context.get("powerDeleteservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerDeleteservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("powerDeleteservice",bean);
  bean.setActionCode("delete");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("23");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.maintain.SaveUIAction getSaveUIAction(){
 if(context.get("saveUIAction")!=null)
 return (nc.ui.pu.m23.action.maintain.SaveUIAction)context.get("saveUIAction");
  nc.ui.pu.m23.action.maintain.SaveUIAction bean = new nc.ui.pu.m23.action.maintain.SaveUIAction();
  context.put("saveUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setActionName("SAVEBASE");
  bean.setBillType("23");
  bean.setValidationService(getValidateService());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.validation.CompositeValidation getValidateService(){
 if(context.get("validateService")!=null)
 return (nc.ui.pubapp.uif2app.validation.CompositeValidation)context.get("validateService");
  nc.ui.pubapp.uif2app.validation.CompositeValidation bean = new nc.ui.pubapp.uif2app.validation.CompositeValidation();
  context.put("validateService",bean);
  bean.setValidators(getManagedList6());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add(getPowervalidservice());  return list;}

public nc.ui.pubapp.pub.power.PowerSaveValidateService getPowervalidservice(){
 if(context.get("powervalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerSaveValidateService)context.get("powervalidservice");
  nc.ui.pubapp.pub.power.PowerSaveValidateService bean = new nc.ui.pubapp.pub.power.PowerSaveValidateService();
  context.put("powervalidservice",bean);
  bean.setEditActionCode("edit");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("23");
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

public nc.ui.pu.m23.query.ArriveQueryDLGInitializer getArriveQryDLGInitializer(){
 if(context.get("arriveQryDLGInitializer")!=null)
 return (nc.ui.pu.m23.query.ArriveQueryDLGInitializer)context.get("arriveQryDLGInitializer");
  nc.ui.pu.m23.query.ArriveQueryDLGInitializer bean = new nc.ui.pu.m23.query.ArriveQueryDLGInitializer();
  context.put("arriveQryDLGInitializer",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction getQueryUIAction(){
 if(context.get("queryUIAction")!=null)
 return (nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction)context.get("queryUIAction");
  nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction();
  context.put("queryUIAction",bean);
  bean.setDataManager(getModelDataManager());
  bean.setQryCondDLGInitializer(getArriveQryDLGInitializer());
  bean.setModel(getManageAppModel());
  bean.setShowUpComponent(getListView());
  bean.setTemplateContainer(getQueryTemplateContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction getRefreshUIAction(){
 if(context.get("refreshUIAction")!=null)
 return (nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction)context.get("refreshUIAction");
  nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction();
  context.put("refreshUIAction",bean);
  bean.setDataManager(getModelDataManager());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.maintain.ArriveRefreshCardAction getCardRefreshAction(){
 if(context.get("cardRefreshAction")!=null)
 return (nc.ui.pu.m23.action.maintain.ArriveRefreshCardAction)context.get("cardRefreshAction");
  nc.ui.pu.m23.action.maintain.ArriveRefreshCardAction bean = new nc.ui.pu.m23.action.maintain.ArriveRefreshCardAction();
  context.put("cardRefreshAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction getSaveCommitAction(){
 if(context.get("saveCommitAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction)context.get("saveCommitAction");
  nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction bean = new nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction(getSaveUIAction(),getSendUIAction());  context.put("saveCommitAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBtnName(getI18nFB_1dd7bc3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1dd7bc3(){
 if(context.get("nc.ui.uif2.I18nFB#1dd7bc3")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1dd7bc3");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1dd7bc3",bean);  bean.setResDir("common");
  bean.setResId("2SCMPUB-000027");
  bean.setDefaultValue("保存提交");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1dd7bc3",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.pu.m23.action.approve.SendUIAction getSendUIAction(){
 if(context.get("sendUIAction")!=null)
 return (nc.ui.pu.m23.action.approve.SendUIAction)context.get("sendUIAction");
  nc.ui.pu.m23.action.approve.SendUIAction bean = new nc.ui.pu.m23.action.approve.SendUIAction();
  context.put("sendUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setBillType("23");
  bean.setPreActionNames(getManagedList7());
  bean.setActionName("SAVE");
  bean.setFilledUpInFlow(true);
  bean.setValidationService(getSendpowervalidservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList7(){  List list = new ArrayList();  list.add("SAVEBASE");  return list;}

public nc.ui.pu.pub.action.UnSaveScriptAction getRevokeUIAction(){
 if(context.get("revokeUIAction")!=null)
 return (nc.ui.pu.pub.action.UnSaveScriptAction)context.get("revokeUIAction");
  nc.ui.pu.pub.action.UnSaveScriptAction bean = new nc.ui.pu.pub.action.UnSaveScriptAction();
  context.put("revokeUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setActionName("UNSAVEBILL");
  bean.setBillType("23");
  bean.setFilledUpInFlow(true);
  bean.setValidationService(getUnsendpowervalidservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getSendpowervalidservice(){
 if(context.get("sendpowervalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("sendpowervalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("sendpowervalidservice",bean);
  bean.setActionCode("commit");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("23");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getUnsendpowervalidservice(){
 if(context.get("unsendpowervalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("unsendpowervalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("unsendpowervalidservice",bean);
  bean.setActionCode("uncommit");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("23");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getSendApproveMenuAction(){
 if(context.get("sendApproveMenuAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("sendApproveMenuAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("sendApproveMenuAction",bean);
  bean.setCode("sendApproveMenuAction");
  bean.setActions(getManagedList8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getSendUIAction());  list.add(getRevokeUIAction());  return list;}

public nc.funcnode.ui.action.GroupAction getAuditMenuAction(){
 if(context.get("auditMenuAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("auditMenuAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("auditMenuAction",bean);
  bean.setCode("auditMenuAction");
  bean.setActions(getManagedList9());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList9(){  List list = new ArrayList();  list.add(getApproveUIAction_1e87581());  list.add(getUnApproveUIAction_7a8f16());  list.add(getSeparatorAction());  list.add(getLinkAuditStatusUIAction());  return list;}

private nc.ui.pu.m23.action.approve.ApproveUIAction getApproveUIAction_1e87581(){
 if(context.get("nc.ui.pu.m23.action.approve.ApproveUIAction#1e87581")!=null)
 return (nc.ui.pu.m23.action.approve.ApproveUIAction)context.get("nc.ui.pu.m23.action.approve.ApproveUIAction#1e87581");
  nc.ui.pu.m23.action.approve.ApproveUIAction bean = new nc.ui.pu.m23.action.approve.ApproveUIAction();
  context.put("nc.ui.pu.m23.action.approve.ApproveUIAction#1e87581",bean);
  bean.setModel(getManageAppModel());
  bean.setFilledUpInFlow(true);
  bean.setBillType("23");
  bean.setEditor(getBillFormEditor());
  bean.setValidationService(getApprovepowervalidservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.approve.UnApproveUIAction getUnApproveUIAction_7a8f16(){
 if(context.get("nc.ui.pu.m23.action.approve.UnApproveUIAction#7a8f16")!=null)
 return (nc.ui.pu.m23.action.approve.UnApproveUIAction)context.get("nc.ui.pu.m23.action.approve.UnApproveUIAction#7a8f16");
  nc.ui.pu.m23.action.approve.UnApproveUIAction bean = new nc.ui.pu.m23.action.approve.UnApproveUIAction();
  context.put("nc.ui.pu.m23.action.approve.UnApproveUIAction#7a8f16",bean);
  bean.setModel(getManageAppModel());
  bean.setFilledUpInFlow(true);
  bean.setBillType("23");
  bean.setEditor(getBillFormEditor());
  bean.setValidationService(getUnapprovepowervalidservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getApprovepowervalidservice(){
 if(context.get("approvepowervalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("approvepowervalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("approvepowervalidservice",bean);
  bean.setActionCode("approve");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("23");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getUnapprovepowervalidservice(){
 if(context.get("unapprovepowervalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("unapprovepowervalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("unapprovepowervalidservice",bean);
  bean.setActionCode("unapprove");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("23");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.FileDocManageAction getAccessoriesAction(){
 if(context.get("accessoriesAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.FileDocManageAction)context.get("accessoriesAction");
  nc.ui.pubapp.uif2app.actions.FileDocManageAction bean = new nc.ui.pubapp.uif2app.actions.FileDocManageAction();
  context.put("accessoriesAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getAssistFuncMenuAction(){
 if(context.get("assistFuncMenuAction")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("assistFuncMenuAction");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("assistFuncMenuAction",bean);
  bean.setCode("assistFuncMenuAction");
  bean.setName(getI18nFB_47f2b9());
  bean.setTooltip(getI18nFB_1908b53());
  bean.setActions(getManagedList10());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_47f2b9(){
 if(context.get("nc.ui.uif2.I18nFB#47f2b9")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#47f2b9");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#47f2b9",bean);  bean.setResDir("common");
  bean.setResId("14004000-0007");
  bean.setDefaultValue("辅助功能");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#47f2b9",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private java.lang.String getI18nFB_1908b53(){
 if(context.get("nc.ui.uif2.I18nFB#1908b53")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1908b53");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1908b53",bean);  bean.setResDir("common");
  bean.setResId("04004000-0003");
  bean.setDefaultValue("辅助功能（Ctrl+Alt+N）");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1908b53",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList10(){  List list = new ArrayList();  list.add(getQuickArrUIAction());  list.add(getFileMgrUIAction());  return list;}

private nc.ui.pu.m23.action.QuickArrUIAction getQuickArrUIAction(){
 if(context.get("quickArrUIAction")!=null)
 return (nc.ui.pu.m23.action.QuickArrUIAction)context.get("quickArrUIAction");
  nc.ui.pu.m23.action.QuickArrUIAction bean = new nc.ui.pu.m23.action.QuickArrUIAction();
  context.put("quickArrUIAction",bean);
  bean.setBillForm(getBillFormEditor());
  bean.setTransferViewProcessor(getTransferViewProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.FileDocManageAction getFileMgrUIAction(){
 if(context.get("FileMgrUIAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.FileDocManageAction)context.get("FileMgrUIAction");
  nc.ui.pubapp.uif2app.actions.FileDocManageAction bean = new nc.ui.pubapp.uif2app.actions.FileDocManageAction();
  context.put("FileMgrUIAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.pub.action.PULinkQueryAction getLinkBillUIAction(){
 if(context.get("linkBillUIAction")!=null)
 return (nc.ui.pu.pub.action.PULinkQueryAction)context.get("linkBillUIAction");
  nc.ui.pu.pub.action.PULinkQueryAction bean = new nc.ui.pu.pub.action.PULinkQueryAction();
  context.put("linkBillUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBillType("23");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.LinkSetPartUIAction getLinkSetPartUIAction(){
 if(context.get("linkSetPartUIAction")!=null)
 return (nc.ui.pu.m23.action.LinkSetPartUIAction)context.get("linkSetPartUIAction");
  nc.ui.pu.m23.action.LinkSetPartUIAction bean = new nc.ui.pu.m23.action.LinkSetPartUIAction();
  context.put("linkSetPartUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBillForm(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.LinkATPUIAction getLinkATPUIAction(){
 if(context.get("linkATPUIAction")!=null)
 return (nc.ui.pu.m23.action.LinkATPUIAction)context.get("linkATPUIAction");
  nc.ui.pu.m23.action.LinkATPUIAction bean = new nc.ui.pu.m23.action.LinkATPUIAction();
  context.put("linkATPUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBillForm(getBillFormEditor());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction getLinkAuditStatusUIAction(){
 if(context.get("linkAuditStatusUIAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction)context.get("linkAuditStatusUIAction");
  nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction bean = new nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction();
  context.put("linkAuditStatusUIAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getLinkQueryMenuAction_view(){
 if(context.get("linkQueryMenuAction_view")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("linkQueryMenuAction_view");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("linkQueryMenuAction_view",bean);
  bean.setCode("linkQueryMenuAction_view");
  bean.setName(getI18nFB_ab0655());
  bean.setTooltip(getI18nFB_2cdcbf());
  bean.setActions(getManagedList11());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_ab0655(){
 if(context.get("nc.ui.uif2.I18nFB#ab0655")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#ab0655");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#ab0655",bean);  bean.setResDir("common");
  bean.setResId("14004000-0008");
  bean.setDefaultValue("联查");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#ab0655",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private java.lang.String getI18nFB_2cdcbf(){
 if(context.get("nc.ui.uif2.I18nFB#2cdcbf")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#2cdcbf");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#2cdcbf",bean);  bean.setResDir("common");
  bean.setResId("04004000-0004");
  bean.setDefaultValue("联查（Ctrl+Alt+Q）");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#2cdcbf",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList11(){  List list = new ArrayList();  list.add(getLinkBillUIAction());  list.add(getSeparatorAction_1509120());  list.add(getLinkSetPartUIAction());  list.add(getLinkATPUIAction());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1509120(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#1509120")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#1509120");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#1509120",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getLinkQueryMenuAction_edit(){
 if(context.get("linkQueryMenuAction_edit")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("linkQueryMenuAction_edit");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("linkQueryMenuAction_edit",bean);
  bean.setCode("linkQueryMenuAction_edit");
  bean.setName(getI18nFB_67e424());
  bean.setTooltip(getI18nFB_b321c0());
  bean.setActions(getManagedList12());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_67e424(){
 if(context.get("nc.ui.uif2.I18nFB#67e424")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#67e424");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#67e424",bean);  bean.setResDir("common");
  bean.setResId("14004000-0008");
  bean.setDefaultValue("联查");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#67e424",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private java.lang.String getI18nFB_b321c0(){
 if(context.get("nc.ui.uif2.I18nFB#b321c0")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#b321c0");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#b321c0",bean);  bean.setResDir("common");
  bean.setResId("04004000-0004");
  bean.setDefaultValue("联查（Ctrl+Alt+Q）");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#b321c0",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList12(){  List list = new ArrayList();  list.add(getLinkSetPartUIAction());  list.add(getLinkATPUIAction());  return list;}

public nc.funcnode.ui.action.MenuAction getRelateFuncMenuAction(){
 if(context.get("relateFuncMenuAction")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("relateFuncMenuAction");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("relateFuncMenuAction",bean);
  bean.setCode("relateFuncMenuAction");
  bean.setName(getI18nFB_6cd60c());
  bean.setTooltip(getI18nFB_158bd6e());
  bean.setActions(getManagedList13());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_6cd60c(){
 if(context.get("nc.ui.uif2.I18nFB#6cd60c")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#6cd60c");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#6cd60c",bean);  bean.setResDir("common");
  bean.setResId("14004000-0009");
  bean.setDefaultValue("关联功能");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#6cd60c",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private java.lang.String getI18nFB_158bd6e(){
 if(context.get("nc.ui.uif2.I18nFB#158bd6e")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#158bd6e");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#158bd6e",bean);  bean.setResDir("common");
  bean.setResId("14004000-0009");
  bean.setDefaultValue("关联功能");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#158bd6e",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList13(){  List list = new ArrayList();  list.add(getUnQCUIAction());  list.add(getQCUIAction_fcc005());  list.add(getUrgentLetGoUIAction_1a2c965());  list.add(getCrtFACardUIAction_5407c0());  list.add(getDelFACardUIAction_c8e916());  list.add(getMaterialAssignAction_3ee14d());  list.add(getTransAssetAction_39ae5d());  list.add(getDelTransAssetAction_e65ccf());  return list;}

private nc.ui.pu.m23.action.UnQCUIAction getUnQCUIAction(){
 if(context.get("UnQCUIAction")!=null)
 return (nc.ui.pu.m23.action.UnQCUIAction)context.get("UnQCUIAction");
  nc.ui.pu.m23.action.UnQCUIAction bean = new nc.ui.pu.m23.action.UnQCUIAction();
  context.put("UnQCUIAction",bean);
  bean.setModel(getManageAppModel());
  bean.setForm(getBillFormEditor());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.QCUIAction getQCUIAction_fcc005(){
 if(context.get("nc.ui.pu.m23.action.QCUIAction#fcc005")!=null)
 return (nc.ui.pu.m23.action.QCUIAction)context.get("nc.ui.pu.m23.action.QCUIAction#fcc005");
  nc.ui.pu.m23.action.QCUIAction bean = new nc.ui.pu.m23.action.QCUIAction();
  context.put("nc.ui.pu.m23.action.QCUIAction#fcc005",bean);
  bean.setModel(getManageAppModel());
  bean.setForm(getBillFormEditor());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.UrgentLetGoUIAction getUrgentLetGoUIAction_1a2c965(){
 if(context.get("nc.ui.pu.m23.action.UrgentLetGoUIAction#1a2c965")!=null)
 return (nc.ui.pu.m23.action.UrgentLetGoUIAction)context.get("nc.ui.pu.m23.action.UrgentLetGoUIAction#1a2c965");
  nc.ui.pu.m23.action.UrgentLetGoUIAction bean = new nc.ui.pu.m23.action.UrgentLetGoUIAction();
  context.put("nc.ui.pu.m23.action.UrgentLetGoUIAction#1a2c965",bean);
  bean.setForm(getBillFormEditor());
  bean.setList(getListView());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.CrtFACardUIAction getCrtFACardUIAction_5407c0(){
 if(context.get("nc.ui.pu.m23.action.CrtFACardUIAction#5407c0")!=null)
 return (nc.ui.pu.m23.action.CrtFACardUIAction)context.get("nc.ui.pu.m23.action.CrtFACardUIAction#5407c0");
  nc.ui.pu.m23.action.CrtFACardUIAction bean = new nc.ui.pu.m23.action.CrtFACardUIAction();
  context.put("nc.ui.pu.m23.action.CrtFACardUIAction#5407c0",bean);
  bean.setBillForm(getBillFormEditor());
  bean.setModel(getManageAppModel());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.DelFACardUIAction getDelFACardUIAction_c8e916(){
 if(context.get("nc.ui.pu.m23.action.DelFACardUIAction#c8e916")!=null)
 return (nc.ui.pu.m23.action.DelFACardUIAction)context.get("nc.ui.pu.m23.action.DelFACardUIAction#c8e916");
  nc.ui.pu.m23.action.DelFACardUIAction bean = new nc.ui.pu.m23.action.DelFACardUIAction();
  context.put("nc.ui.pu.m23.action.DelFACardUIAction#c8e916",bean);
  bean.setBillForm(getBillFormEditor());
  bean.setModel(getManageAppModel());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.MaterialAssignAction getMaterialAssignAction_3ee14d(){
 if(context.get("nc.ui.pu.m23.action.MaterialAssignAction#3ee14d")!=null)
 return (nc.ui.pu.m23.action.MaterialAssignAction)context.get("nc.ui.pu.m23.action.MaterialAssignAction#3ee14d");
  nc.ui.pu.m23.action.MaterialAssignAction bean = new nc.ui.pu.m23.action.MaterialAssignAction();
  context.put("nc.ui.pu.m23.action.MaterialAssignAction#3ee14d",bean);
  bean.setBillForm(getBillFormEditor());
  bean.setModel(getManageAppModel());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.TransAssetAction getTransAssetAction_39ae5d(){
 if(context.get("nc.ui.pu.m23.action.TransAssetAction#39ae5d")!=null)
 return (nc.ui.pu.m23.action.TransAssetAction)context.get("nc.ui.pu.m23.action.TransAssetAction#39ae5d");
  nc.ui.pu.m23.action.TransAssetAction bean = new nc.ui.pu.m23.action.TransAssetAction();
  context.put("nc.ui.pu.m23.action.TransAssetAction#39ae5d",bean);
  bean.setBillForm(getBillFormEditor());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.DelTransAssetAction getDelTransAssetAction_e65ccf(){
 if(context.get("nc.ui.pu.m23.action.DelTransAssetAction#e65ccf")!=null)
 return (nc.ui.pu.m23.action.DelTransAssetAction)context.get("nc.ui.pu.m23.action.DelTransAssetAction#e65ccf");
  nc.ui.pu.m23.action.DelTransAssetAction bean = new nc.ui.pu.m23.action.DelTransAssetAction();
  context.put("nc.ui.pu.m23.action.DelTransAssetAction#e65ccf",bean);
  bean.setBillForm(getBillFormEditor());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.processor.ArrivePrintProcessor getPrintProcessor(){
 if(context.get("printProcessor")!=null)
 return (nc.ui.pu.m23.action.processor.ArrivePrintProcessor)context.get("printProcessor");
  nc.ui.pu.m23.action.processor.ArrivePrintProcessor bean = new nc.ui.pu.m23.action.processor.ArrivePrintProcessor();
  context.put("printProcessor",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction getPreviewAction(){
 if(context.get("previewAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction)context.get("previewAction");
  nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction bean = new nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction();
  context.put("previewAction",bean);
  bean.setPreview(true);
  bean.setNodeKey("4004080002");
  bean.setModel(getManageAppModel());
  bean.setBeforePrintDataProcess(getPrintProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction getPrintAction(){
 if(context.get("printAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction)context.get("printAction");
  nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction bean = new nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction();
  context.put("printAction",bean);
  bean.setPreview(false);
  bean.setNodeKey("4004080002");
  bean.setModel(getManageAppModel());
  bean.setBeforePrintDataProcess(getPrintProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.OutputAction getOutputAction(){
 if(context.get("outputAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.OutputAction)context.get("outputAction");
  nc.ui.pubapp.uif2app.actions.OutputAction bean = new nc.ui.pubapp.uif2app.actions.OutputAction();
  context.put("outputAction",bean);
  bean.setNodeKey("4004080002");
  bean.setModel(getManageAppModel());
  bean.setParent(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.BarcodeAction getDirectPrintBarcodeAction(){
 if(context.get("directPrintBarcodeAction")!=null)
 return (nc.ui.pu.m23.action.BarcodeAction)context.get("directPrintBarcodeAction");
  nc.ui.pu.m23.action.BarcodeAction bean = new nc.ui.pu.m23.action.BarcodeAction();
  context.put("directPrintBarcodeAction",bean);
  bean.setForm(getBillFormEditor());
  bean.setDirect(true);
  bean.setModel(getManageAppModel());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.BarcodeAction getPrintBarcodeAction(){
 if(context.get("printBarcodeAction")!=null)
 return (nc.ui.pu.m23.action.BarcodeAction)context.get("printBarcodeAction");
  nc.ui.pu.m23.action.BarcodeAction bean = new nc.ui.pu.m23.action.BarcodeAction();
  context.put("printBarcodeAction",bean);
  bean.setForm(getBillFormEditor());
  bean.setDirect(false);
  bean.setModel(getManageAppModel());
  bean.setList(getListView());
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
  bean.setSplitAttributes(getManagedList14());
  bean.setPrintAction(getPrintAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList14(){  List list = new ArrayList();  list.add("pk_receivestore");  list.add("pk_material.pk_marbasclass");  return list;}

public nc.ui.pu.m23.action.CombinePrintUIAction getCombinePriceAction(){
 if(context.get("combinePriceAction")!=null)
 return (nc.ui.pu.m23.action.CombinePrintUIAction)context.get("combinePriceAction");
  nc.ui.pu.m23.action.CombinePrintUIAction bean = new nc.ui.pu.m23.action.CombinePrintUIAction();
  context.put("combinePriceAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBillForm(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmpub.action.SCMPrintCountQueryAction getPrintCountQueryAction(){
 if(context.get("printCountQueryAction")!=null)
 return (nc.ui.scmpub.action.SCMPrintCountQueryAction)context.get("printCountQueryAction");
  nc.ui.scmpub.action.SCMPrintCountQueryAction bean = new nc.ui.scmpub.action.SCMPrintCountQueryAction();
  context.put("printCountQueryAction",bean);
  bean.setModel(getManageAppModel());
  bean.setBilldateFieldName("dbilldate");
  bean.setBillType("23");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintMenuActionList(){
 if(context.get("printMenuActionList")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printMenuActionList");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printMenuActionList",bean);
  bean.setCode("printMenuAction");
  bean.setActions(getManagedList15());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList15(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreviewAction());  list.add(getOutputAction());  list.add(getSplitPrintAction());  list.add(getPrintCountQueryAction());  list.add(getSeparatorAction_1d726ce());  list.add(getCombinePriceAction());  return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1d726ce(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#1d726ce")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#1d726ce");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#1d726ce",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintMenuActionCard(){
 if(context.get("printMenuActionCard")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printMenuActionCard");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printMenuActionCard",bean);
  bean.setCode("printMenuAction");
  bean.setActions(getManagedList16());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList16(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreviewAction());  list.add(getOutputAction());  list.add(getSplitPrintAction());  list.add(getPrintCountQueryAction());  list.add(getSeparatorAction_34bd13());  list.add(getCombinePriceAction());  list.add(getDirectPrintBarcodeAction());  list.add(getPrintBarcodeAction());return list;}

private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_34bd13(){
 if(context.get("nc.funcnode.ui.action.SeparatorAction#34bd13")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#34bd13");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nc.funcnode.ui.action.SeparatorAction#34bd13",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pu.m23.action.TrayLabelPrintAction getTrayLabelPrintAction(){
	 if(context.get("metaDataBasedPrintActiona")!=null)
	 return (nc.ui.pu.m23.action.TrayLabelPrintAction)context.get("metaDataBasedPrintActiona");
	  nc.ui.pu.m23.action.TrayLabelPrintAction bean = new nc.ui.pu.m23.action.TrayLabelPrintAction();
	  context.put("metaDataBasedPrintActiona",bean);
	  bean.setModel(getManageAppModel());
	  bean.setForm(getBillFormEditor());
	  bean.setList(getListView());
	setBeanFacotryIfBeanFacatoryAware(bean);
	invokeInitializingBean(bean);
	return bean;
	}

private nc.ui.pu.m23.action.SingletonLabelPrintAction getSingletonLabelPrintAction(){
	 if(context.get("metaDataBasedPrintActionb")!=null)
	 return (nc.ui.pu.m23.action.SingletonLabelPrintAction)context.get("metaDataBasedPrintActionb");
	  nc.ui.pu.m23.action.SingletonLabelPrintAction bean = new nc.ui.pu.m23.action.SingletonLabelPrintAction();
	  context.put("metaDataBasedPrintActionb",bean);
	  bean.setModel(getManageAppModel());
	  bean.setForm(getBillFormEditor());
	  bean.setList(getListView());
	setBeanFacotryIfBeanFacatoryAware(bean);
	invokeInitializingBean(bean);
	return bean;
	}

public nc.ui.pubapp.uif2app.actions.BodyDelLineAction getBodyDelLineAction(){
 if(context.get("bodyDelLineAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.BodyDelLineAction)context.get("bodyDelLineAction");
  nc.ui.pubapp.uif2app.actions.BodyDelLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyDelLineAction();
  context.put("bodyDelLineAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.BodyCopyLineAction getBodyCopyLineAction(){
 if(context.get("bodyCopyLineAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.BodyCopyLineAction)context.get("bodyCopyLineAction");
  nc.ui.pubapp.uif2app.actions.BodyCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyCopyLineAction();
  context.put("bodyCopyLineAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.BodyPasteLineAction getBodyPasteLineAction(){
 if(context.get("bodyPasteLineAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.BodyPasteLineAction)context.get("bodyPasteLineAction");
  nc.ui.pubapp.uif2app.actions.BodyPasteLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteLineAction();
  context.put("bodyPasteLineAction",bean);
  bean.setClearItems(getManagedList17());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList17(){  List list = new ArrayList();  list.add("crowno");  list.add("pk_arriveorder_b");  return list;}

public nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction getBodyPasteToTailAction(){
 if(context.get("bodyPasteToTailAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction)context.get("bodyPasteToTailAction");
  nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction();
  context.put("bodyPasteToTailAction",bean);
  bean.setClearItems(getManagedList18());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList18(){  List list = new ArrayList();  list.add("crowno");  list.add("pk_arriveorder_b");  return list;}

public nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getSortRowNoBodyAction(){
 if(context.get("sortRowNoBodyAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction)context.get("sortRowNoBodyAction");
  nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
  context.put("sortRowNoBodyAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.action.body.CardEditBodyAction getCardEditBodyAction(){
 if(context.get("cardEditBodyAction")!=null)
 return (nc.ui.pu.m23.action.body.CardEditBodyAction)context.get("cardEditBodyAction");
  nc.ui.pu.m23.action.body.CardEditBodyAction bean = new nc.ui.pu.m23.action.body.CardEditBodyAction();
  context.put("cardEditBodyAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator getCardPanelOrgSetterForAllRefMediator_0(){
 if(context.get("nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator#0")!=null)
 return (nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator)context.get("nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator#0");
  nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator bean = new nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator(getBillFormEditor());  context.put("nc.ui.pubapp.uif2app.view.CardPanelOrgSetterForAllRefMediator#0",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.editor.list.HeadRowChangeHandler getList_head_rowchangehandler(){
 if(context.get("list_head_rowchangehandler")!=null)
 return (nc.ui.pu.m23.editor.list.HeadRowChangeHandler)context.get("list_head_rowchangehandler");
  nc.ui.pu.m23.editor.list.HeadRowChangeHandler bean = new nc.ui.pu.m23.editor.list.HeadRowChangeHandler();
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
  bean.setModel(getManageAppModel());
  bean.setHandlerMap(getManagedMap0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("nc.ui.pubapp.uif2app.event.OrgChangedEvent",getManagedList19());  map.put("nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent",getManagedList20());  map.put("nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent",getManagedList21());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent",getManagedList22());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent",getManagedList23());  map.put("nc.ui.pubapp.uif2app.event.list.ListHeadRowChangedEvent",getManagedList24());  return map;}

private List getManagedList19(){  List list = new ArrayList();  list.add(getOrgChangedEventHandler_652f0a());  return list;}

private nc.ui.pu.m23.editor.org.OrgChangedEventHandler getOrgChangedEventHandler_652f0a(){
 if(context.get("nc.ui.pu.m23.editor.org.OrgChangedEventHandler#652f0a")!=null)
 return (nc.ui.pu.m23.editor.org.OrgChangedEventHandler)context.get("nc.ui.pu.m23.editor.org.OrgChangedEventHandler#652f0a");
  nc.ui.pu.m23.editor.org.OrgChangedEventHandler bean = new nc.ui.pu.m23.editor.org.OrgChangedEventHandler();
  context.put("nc.ui.pu.m23.editor.org.OrgChangedEventHandler#652f0a",bean);
  bean.setCardForm(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList20(){  List list = new ArrayList();  list.add(getCardHeadTailBeforeEditEventHandler_f363c8());  return list;}

private nc.ui.pu.m23.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler getCardHeadTailBeforeEditEventHandler_f363c8(){
 if(context.get("nc.ui.pu.m23.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler#f363c8")!=null)
 return (nc.ui.pu.m23.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler)context.get("nc.ui.pu.m23.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler#f363c8");
  nc.ui.pu.m23.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler bean = new nc.ui.pu.m23.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler();
  context.put("nc.ui.pu.m23.editor.card.beforeedit.CardHeadTailBeforeEditEventHandler#f363c8",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList21(){  List list = new ArrayList();  list.add(getCardHeadTailAfterEditEventHandler_11c1966());  return list;}

private nc.ui.pu.m23.editor.card.afteredit.CardHeadTailAfterEditEventHandler getCardHeadTailAfterEditEventHandler_11c1966(){
 if(context.get("nc.ui.pu.m23.editor.card.afteredit.CardHeadTailAfterEditEventHandler#11c1966")!=null)
 return (nc.ui.pu.m23.editor.card.afteredit.CardHeadTailAfterEditEventHandler)context.get("nc.ui.pu.m23.editor.card.afteredit.CardHeadTailAfterEditEventHandler#11c1966");
  nc.ui.pu.m23.editor.card.afteredit.CardHeadTailAfterEditEventHandler bean = new nc.ui.pu.m23.editor.card.afteredit.CardHeadTailAfterEditEventHandler();
  context.put("nc.ui.pu.m23.editor.card.afteredit.CardHeadTailAfterEditEventHandler#11c1966",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList22(){  List list = new ArrayList();  list.add(getCardBodyBeforeEditEventHandler_1c7b6ae());  return list;}

private nc.ui.pu.m23.editor.card.beforeedit.CardBodyBeforeEditEventHandler getCardBodyBeforeEditEventHandler_1c7b6ae(){
 if(context.get("nc.ui.pu.m23.editor.card.beforeedit.CardBodyBeforeEditEventHandler#1c7b6ae")!=null)
 return (nc.ui.pu.m23.editor.card.beforeedit.CardBodyBeforeEditEventHandler)context.get("nc.ui.pu.m23.editor.card.beforeedit.CardBodyBeforeEditEventHandler#1c7b6ae");
  nc.ui.pu.m23.editor.card.beforeedit.CardBodyBeforeEditEventHandler bean = new nc.ui.pu.m23.editor.card.beforeedit.CardBodyBeforeEditEventHandler();
  context.put("nc.ui.pu.m23.editor.card.beforeedit.CardBodyBeforeEditEventHandler#1c7b6ae",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList23(){  List list = new ArrayList();  list.add(getCardBodyAfterEditEventHandler_aa93dd());  return list;}

private nc.ui.pu.m23.editor.card.afteredit.CardBodyAfterEditEventHandler getCardBodyAfterEditEventHandler_aa93dd(){
 if(context.get("nc.ui.pu.m23.editor.card.afteredit.CardBodyAfterEditEventHandler#aa93dd")!=null)
 return (nc.ui.pu.m23.editor.card.afteredit.CardBodyAfterEditEventHandler)context.get("nc.ui.pu.m23.editor.card.afteredit.CardBodyAfterEditEventHandler#aa93dd");
  nc.ui.pu.m23.editor.card.afteredit.CardBodyAfterEditEventHandler bean = new nc.ui.pu.m23.editor.card.afteredit.CardBodyAfterEditEventHandler();
  context.put("nc.ui.pu.m23.editor.card.afteredit.CardBodyAfterEditEventHandler#aa93dd",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList24(){  List list = new ArrayList();  list.add(getList_head_rowchangehandler());  return list;}

public nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener)context.get("InitDataListener");
  nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener bean = new nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener();
  context.put("InitDataListener",bean);
  bean.setContext(getContext());
  bean.setModel(getManageAppModel());
  bean.setVoClassName("nc.vo.pu.m23.entity.ArriveVO");
  bean.setAutoShowUpComponent(getBillFormEditor());
  bean.setQueryAction(getQueryUIAction());
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

public nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory getBoadatorfactory(){
 if(context.get("boadatorfactory")!=null)
 return (nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory)context.get("boadatorfactory");
  nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory bean = new nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory();
  context.put("boadatorfactory",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.model.ArriveModelService getManageModelService(){
 if(context.get("manageModelService")!=null)
 return (nc.ui.pu.m23.model.ArriveModelService)context.get("manageModelService");
  nc.ui.pu.m23.model.ArriveModelService bean = new nc.ui.pu.m23.model.ArriveModelService();
  context.put("manageModelService",bean);
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
  bean.setBillType("23");
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
  bean.setPageQuery(getPageQuery());
  bean.setPageDelegator(getPageDelegator());
  bean.setPagePanel(getQueryInfo());
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

public nc.ui.pu.m23.model.ArrivePageModelService getPageQuery(){
 if(context.get("pageQuery")!=null)
 return (nc.ui.pu.m23.model.ArrivePageModelService)context.get("pageQuery");
  nc.ui.pu.m23.model.ArrivePageModelService bean = new nc.ui.pu.m23.model.ArrivePageModelService();
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

public nc.ui.pu.pub.view.PUBillListView getListView(){
 if(context.get("listView")!=null)
 return (nc.ui.pu.pub.view.PUBillListView)context.get("listView");
  nc.ui.pu.pub.view.PUBillListView bean = new nc.ui.pu.pub.view.PUBillListView();
  context.put("listView",bean);
  bean.setModel(getManageAppModel());
  bean.setMultiSelectionMode(0);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setPaginationBar(getPageBar());
  bean.setUserdefitemListPreparator(getUserdefAndMarAsstListPreparator());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getVsourcecodeMediator(){
 if(context.get("vsourcecodeMediator")!=null)
 return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator)context.get("vsourcecodeMediator");
  nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
  context.put("vsourcecodeMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setSrcBillIdField("csourceid");
  bean.setSrcBillNOField("vsourcecode");
  bean.setSrcBillTypeField("csourcetypecode");
  bean.setSrcBillTypeFieldPos(1);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getVfirstcodeMediator(){
 if(context.get("vfirstcodeMediator")!=null)
 return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator)context.get("vfirstcodeMediator");
  nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
  context.put("vfirstcodeMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setSrcBillIdField("cfirstid");
  bean.setSrcBillNOField("vfirstcode");
  bean.setSrcBillTypeField("cfirsttypecode");
  bean.setSrcBillTypeFieldPos(1);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m23.view.ArriveCardForm getBillFormEditor(){
 if(context.get("billFormEditor")!=null)
 return (nc.ui.pu.m23.view.ArriveCardForm)context.get("billFormEditor");
  nc.ui.pu.m23.view.ArriveCardForm bean = new nc.ui.pu.m23.view.ArriveCardForm();
  context.put("billFormEditor",bean);
  bean.setModel(getManageAppModel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setUserdefitemPreparator(getUserdefAndMarAsstCardPreparator());
  bean.setTemplateNotNullValidate(true);
  bean.setAutoAddLine(false);
  bean.setBlankChildrenFilter(getSingleFieldBlankChildrenFilter_2b0aa0());
  bean.setBodyLineActions(getManagedList25());
  bean.setClearHyperlink(getManagedList26());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter getSingleFieldBlankChildrenFilter_2b0aa0(){
 if(context.get("nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter#2b0aa0")!=null)
 return (nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter)context.get("nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter#2b0aa0");
  nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter bean = new nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter();
  context.put("nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter#2b0aa0",bean);
  bean.setFieldName("pk_material");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList25(){  List list = new ArrayList();  list.add(getBodyDelLineAction());  list.add(getBodyCopyLineAction());  list.add(getBodyPasteLineAction());  list.add(getBodyPasteToTailAction());  list.add(getActionsBar_ActionsBarSeparator_1c6c8ee());  list.add(getCardEditBodyAction());  list.add(getSortRowNoBodyAction());  list.add(getActionsBar_ActionsBarSeparator_1b1b67c());  list.add(getDefaultBodyZoomAction_439d4d());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1c6c8ee(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1c6c8ee")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1c6c8ee");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1c6c8ee",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1b1b67c(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b1b67c")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b1b67c");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b1b67c",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction getDefaultBodyZoomAction_439d4d(){
 if(context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#439d4d")!=null)
 return (nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction)context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#439d4d");
  nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction bean = new nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction();
  context.put("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#439d4d",bean);
  bean.setPos(1);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList26(){  List list = new ArrayList();  list.add("vbillcode");  return list;}

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

public nc.ui.pubapp.common.validateservice.ClosingCheck getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.pubapp.common.validateservice.ClosingCheck)context.get("ClosingListener");
  nc.ui.pubapp.common.validateservice.ClosingCheck bean = new nc.ui.pubapp.common.validateservice.ClosingCheck();
  context.put("ClosingListener",bean);
  bean.setModel(getManageAppModel());
  bean.setSaveAction(getSaveUIAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.model.BillBodySortMediator getBillBodySortMediator(){
 if(context.get("billBodySortMediator")!=null)
 return (nc.ui.pubapp.uif2app.model.BillBodySortMediator)context.get("billBodySortMediator");
  nc.ui.pubapp.uif2app.model.BillBodySortMediator bean = new nc.ui.pubapp.uif2app.model.BillBodySortMediator(getManageAppModel(),getBillFormEditor(),getListView());  context.put("billBodySortMediator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell getQueryArea(){
 if(context.get("queryArea")!=null)
 return (nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell)context.get("queryArea");
  nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell bean = new nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell();
  context.put("queryArea",bean);
  bean.setQueryAreaCreator(getQueryUIAction());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getQueryInfo(){
 if(context.get("queryInfo")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("queryInfo");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("queryInfo",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel getCardInfoPnl(){
 if(context.get("cardInfoPnl")!=null)
 return (nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel)context.get("cardInfoPnl");
  nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel bean = new nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel();
  context.put("cardInfoPnl",bean);
  bean.setTitleAction(getReturnaction());
  bean.setRightExActions(getManagedList27());
  bean.setModel(getManageAppModel());
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
  bean.setSaveAction(getSaveUIAction());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList27(){  List list = new ArrayList();  list.add(getActionsBar_ActionsBarSeparator_10c3ac7());  list.add(getHeadZoomAction());  return list;}

private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_10c3ac7(){
 if(context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10c3ac7")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10c3ac7");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10c3ac7",bean);
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

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setModel(getManageAppModel());
  bean.setTangramLayoutRoot(getTBNode_15f7462());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_15f7462(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#15f7462")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#15f7462");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#15f7462",bean);
  bean.setShowMode("CardLayout");
  bean.setTabs(getManagedList28());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList28(){  List list = new ArrayList();  list.add(getHSNode_5300fd());  list.add(getVSNode_188b205());  return list;}

private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_5300fd(){
 if(context.get("nc.ui.uif2.tangramlayout.node.HSNode#5300fd")!=null)
 return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#5300fd");
  nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
  context.put("nc.ui.uif2.tangramlayout.node.HSNode#5300fd",bean);
  bean.setLeft(getCNode_1e6fb0d());
  bean.setRight(getVSNode_1a4fae1());
  bean.setDividerLocation(0.22f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1e6fb0d(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1e6fb0d")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1e6fb0d");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1e6fb0d",bean);
  bean.setComponent(getQueryArea());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_1a4fae1(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#1a4fae1")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#1a4fae1");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#1a4fae1",bean);
  bean.setUp(getCNode_16c147b());
  bean.setDown(getCNode_b55fce());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_16c147b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#16c147b")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#16c147b");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#16c147b",bean);
  bean.setComponent(getQueryInfo());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_b55fce(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#b55fce")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#b55fce");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#b55fce",bean);
  bean.setName(getI18nFB_b4b096());
  bean.setComponent(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_b4b096(){
 if(context.get("nc.ui.uif2.I18nFB#b4b096")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#b4b096");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#b4b096",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000107");
  bean.setDefaultValue("列表");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#b4b096",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_188b205(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#188b205")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#188b205");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#188b205",bean);
  bean.setUp(getCNode_165f092());
  bean.setDown(getCNode_147b81d());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_165f092(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#165f092")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#165f092");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#165f092",bean);
  bean.setComponent(getCardInfoPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_147b81d(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#147b81d")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#147b81d");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#147b81d",bean);
  bean.setName(getI18nFB_195081d());
  bean.setComponent(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_195081d(){
 if(context.get("nc.ui.uif2.I18nFB#195081d")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#195081d");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#195081d",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000106");
  bean.setDefaultValue("卡片");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#195081d",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.scmpub.listener.BillCodeEditMediator getBillCodeMediator(){
 if(context.get("billCodeMediator")!=null)
 return (nc.ui.scmpub.listener.BillCodeEditMediator)context.get("billCodeMediator");
  nc.ui.scmpub.listener.BillCodeEditMediator bean = new nc.ui.scmpub.listener.BillCodeEditMediator();
  context.put("billCodeMediator",bean);
  bean.setBillCodeKey("vbillcode");
  bean.setBillType("23");
  bean.setBillForm(getBillFormEditor());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.FractionFixMediator getFractionFixMediator(){
 if(context.get("fractionFixMediator")!=null)
 return (nc.ui.pubapp.uif2app.view.FractionFixMediator)context.get("fractionFixMediator");
  nc.ui.pubapp.uif2app.view.FractionFixMediator bean = new nc.ui.pubapp.uif2app.view.FractionFixMediator(getBillFormEditor());  context.put("fractionFixMediator",bean);
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.RowNoMediator getRowNoMediator(){
 if(context.get("rowNoMediator")!=null)
 return (nc.ui.pubapp.uif2app.view.RowNoMediator)context.get("rowNoMediator");
  nc.ui.pubapp.uif2app.view.RowNoMediator bean = new nc.ui.pubapp.uif2app.view.RowNoMediator();
  context.put("rowNoMediator",bean);
  bean.setEditor(getBillFormEditor());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getUserdefAndMarAsstCardPreparator(){
 if(context.get("userdefAndMarAsstCardPreparator")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare)context.get("userdefAndMarAsstCardPreparator");
  nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
  context.put("userdefAndMarAsstCardPreparator",bean);
  bean.setBillDataPrepares(getManagedList29());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList29(){  List list = new ArrayList();  list.add(getUserdefitemPreparator());  list.add(getMarAsstPreparator());  list.add(getBcuserdefitemPreparator());  return list;}

public nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getUserdefAndMarAsstListPreparator(){
 if(context.get("userdefAndMarAsstListPreparator")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare)context.get("userdefAndMarAsstListPreparator");
  nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
  context.put("userdefAndMarAsstListPreparator",bean);
  bean.setBillListDataPrepares(getManagedList30());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList30(){  List list = new ArrayList();  list.add(getUserdefitemlistPreparator());  list.add(getMarAsstPreparator());  list.add(getBcuserdefitemlistPreparator());  return list;}

public nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemPreparator(){
 if(context.get("userdefitemPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("userdefitemPreparator");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("userdefitemPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList31());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList31(){  List list = new ArrayList();  list.add(getUserdefQueryParam_1d1e42d());  list.add(getUserdefQueryParam_db4a5d());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1d1e42d(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#1d1e42d")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1d1e42d");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#1d1e42d",bean);
  bean.setMdfullname("pu.po_arriveorder");
  bean.setPos(0);
  bean.setPrefix("vdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_db4a5d(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#db4a5d")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#db4a5d");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#db4a5d",bean);
  bean.setMdfullname("pu.po_arriveorder_b");
  bean.setPos(1);
  bean.setPrefix("vbdef");
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
  bean.setParams(getManagedList32());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList32(){  List list = new ArrayList();  list.add(getUserdefQueryParam_ef5365());  list.add(getUserdefQueryParam_4c49ea());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_ef5365(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#ef5365")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#ef5365");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#ef5365",bean);
  bean.setMdfullname("pu.po_arriveorder");
  bean.setPos(0);
  bean.setPrefix("vdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_4c49ea(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#4c49ea")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#4c49ea");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#4c49ea",bean);
  bean.setMdfullname("pu.po_arriveorder_b");
  bean.setPos(1);
  bean.setTabcode("body");
  bean.setPrefix("vbdef");
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
  bean.setMaterialField("pk_material");
  bean.setProjectField("cprojectid");
  bean.setProductorField("cproductorid");
  bean.setCustomerField("casscustid");
  bean.setSignatureField("cffileid");
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
  bean.setParams(getManagedList33());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList33(){  List list = new ArrayList();  list.add(getQueryParam_88b651());  list.add(getQueryParam_17994a1());  list.add(getQueryParam_10cea46());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_88b651(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#88b651")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#88b651");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#88b651",bean);
  bean.setMdfullname("pu.po_arriveorder");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_17994a1(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#17994a1")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#17994a1");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#17994a1",bean);
  bean.setMdfullname("pu.po_arriveorder_b");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_10cea46(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#10cea46")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#10cea46");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#10cea46",bean);
  bean.setRulecode("materialassistant");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UserdefitemContainerListPreparator getBcuserdefitemlistPreparator(){
 if(context.get("bcuserdefitemlistPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("bcuserdefitemlistPreparator");
  nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
  context.put("bcuserdefitemlistPreparator",bean);
  bean.setContainer(getBcuserdefitemContainer());
  bean.setParams(getManagedList34());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList34(){  List list = new ArrayList();  list.add(getUserdefQueryParam_e8a997());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_e8a997(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#e8a997")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#e8a997");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#e8a997",bean);
  bean.setRulecode("SCM_BATCHCODE");
  bean.setPos(1);
  bean.setPrefix("bc_vdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UserdefitemContainerPreparator getBcuserdefitemPreparator(){
 if(context.get("bcuserdefitemPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("bcuserdefitemPreparator");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("bcuserdefitemPreparator",bean);
  bean.setContainer(getBcuserdefitemContainer());
  bean.setParams(getManagedList35());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList35(){  List list = new ArrayList();  list.add(getUserdefQueryParam_d5fd12());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_d5fd12(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#d5fd12")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#d5fd12");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#d5fd12",bean);
  bean.setRulecode("SCM_BATCHCODE");
  bean.setPos(1);
  bean.setPrefix("bc_vdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.userdefitem.UserDefItemContainer getBcuserdefitemContainer(){
 if(context.get("bcuserdefitemContainer")!=null)
 return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("bcuserdefitemContainer");
  nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
  context.put("bcuserdefitemContainer",bean);
  bean.setContext(getContext());
  bean.setParams(getManagedList36());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList36(){  List list = new ArrayList();  list.add(getQueryParam_d9db6f());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_d9db6f(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#d9db6f")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#d9db6f");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#d9db6f",bean);
  bean.setRulecode("SCM_BATCHCODE");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ic.general.view.BatchcodeMediator getBatchcodeMediator(){
 if(context.get("batchcodeMediator")!=null)
 return (nc.ui.ic.general.view.BatchcodeMediator)context.get("batchcodeMediator");
  nc.ui.ic.general.view.BatchcodeMediator bean = new nc.ui.ic.general.view.BatchcodeMediator();
  context.put("batchcodeMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setBillEditor(getBillFormEditor());
  bean.setPk_calbodyKey("pk_org");
  bean.setCmaterialvidKey("pk_material");
  bean.setBatchDefPrefix("bc_vdef");
  bean.setBatchBillBodyKeys(getManagedList37());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList37(){  List list = new ArrayList();  list.add("bc_vvendbatchcode");  list.add("bc_cqualitylevelid");  return list;}

public nc.ui.pu.m23.query.ArriveLazyItemLoader getBillLazilyLoader(){
 if(context.get("billLazilyLoader")!=null)
 return (nc.ui.pu.m23.query.ArriveLazyItemLoader)context.get("billLazilyLoader");
  nc.ui.pu.m23.query.ArriveLazyItemLoader bean = new nc.ui.pu.m23.query.ArriveLazyItemLoader();
  context.put("billLazilyLoader",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad getLazyActions(){
 if(context.get("lazyActions")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad)context.get("lazyActions");
  nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad();
  context.put("lazyActions",bean);
  bean.setModel(getManageAppModel());
  bean.setActionList(getManagedList38());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList38(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreviewAction());  list.add(getOutputAction());  list.add(getSplitPrintAction());  return list;}

public nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager getLasilyLodadMediator(){
 if(context.get("LasilyLodadMediator")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager)context.get("LasilyLodadMediator");
  nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager bean = new nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager();
  context.put("LasilyLodadMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setLoader(getBillLazilyLoader());
  bean.setLazilyLoadSupporter(getManagedList39());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList39(){  List list = new ArrayList();  list.add(getLazyActions());  list.add(getCardPanelLazilyLoad_d57cda());  list.add(getListPanelLazilyLoad_1f7ed76());  return list;}

private nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad getCardPanelLazilyLoad_d57cda(){
 if(context.get("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#d57cda")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad)context.get("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#d57cda");
  nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad();
  context.put("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#d57cda",bean);
  bean.setBillform(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad getListPanelLazilyLoad_1f7ed76(){
 if(context.get("nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#1f7ed76")!=null)
 return (nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad)context.get("nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#1f7ed76");
  nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad();
  context.put("nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#1f7ed76",bean);
  bean.setListView(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller getRemoteCallCombinatorCaller(){
 if(context.get("remoteCallCombinatorCaller")!=null)
 return (nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller)context.get("remoteCallCombinatorCaller");
  nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller bean = new nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller();
  context.put("remoteCallCombinatorCaller",bean);
  bean.setRemoteCallers(getManagedList40());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList40(){  List list = new ArrayList();  list.add(getQueryTemplateContainer());  list.add(getTemplateContainer());  list.add(getUserdefitemContainer());  return list;}

}
