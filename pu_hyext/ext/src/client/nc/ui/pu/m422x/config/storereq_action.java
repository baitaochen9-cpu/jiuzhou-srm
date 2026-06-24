package nc.ui.pu.m422x.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class storereq_action extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
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
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer((nc.ui.uif2.components.ITabbedPaneAwareComponent)findBeanInUIF2BeanFactory("listView"));  context.put("actionsOfList",bean);
  bean.setActions(getManagedList1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add(getAddMenu());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getCopyAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getRefreshAction());  list.add(getSeparatorAction());  list.add(getSendApproveMenuAction());  list.add(getAuditMenuAction());  list.add(getAssistMenuAction());  list.add(getSeparatorAction());  list.add(getLinkBillMenuAction());  list.add(getSeparatorAction());  list.add(getSeparatorAction());  list.add(getPrintMenuAction());  return list;}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfCard(){
 if(context.get("actionsOfCard")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("actionsOfCard");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer((nc.ui.uif2.components.ITabbedPaneAwareComponent)findBeanInUIF2BeanFactory("billFormEditor"));  context.put("actionsOfCard",bean);
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setActions(getManagedList2());
  bean.setEditActions(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getAddMenu());  list.add(getEditAction());  list.add(getDeleteAction());  list.add(getCopyAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getCardRefreshAction());  list.add(getSeparatorAction());  list.add(getSendApproveMenuAction());  list.add(getAuditMenuAction());  list.add(getAssistMenuAction());  list.add(getSeparatorAction());  list.add(getLinkBillMenuAction());  list.add(getSeparatorAction());  list.add(getSeparatorAction());  list.add(getPrintMenuAction());  return list;}

private List getManagedList3(){  List list = new ArrayList();  list.add(getSaveAction());  list.add(getSaveApproveAction());  list.add(getSeparatorAction());  list.add(getCancelAction());  list.add(getSeparatorAction());  list.add(getLinkBillMenuActionOnEdit());  return list;}

public nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getFormInterceptor(){
 if(context.get("formInterceptor")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("formInterceptor");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("formInterceptor",bean);
  bean.setShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent)findBeanInUIF2BeanFactory("billFormEditor"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getListInterceptor(){
 if(context.get("listInterceptor")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("listInterceptor");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("listInterceptor",bean);
  bean.setShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent)findBeanInUIF2BeanFactory("listView"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.billref.dest.TransferBillViewProcessor getTransferBillViewProcessor(){
 if(context.get("transferBillViewProcessor")!=null)
 return (nc.ui.pubapp.billref.dest.TransferBillViewProcessor)context.get("transferBillViewProcessor");
  nc.ui.pubapp.billref.dest.TransferBillViewProcessor bean = new nc.ui.pubapp.billref.dest.TransferBillViewProcessor();
  context.put("transferBillViewProcessor",bean);
  bean.setList((nc.ui.pubapp.uif2app.view.ShowUpableBillListView)findBeanInUIF2BeanFactory("listView"));
  bean.setActionContainer((nc.ui.uif2.actions.IActionContributor)findBeanInUIF2BeanFactory("container"));
  bean.setBillForm((nc.ui.pubapp.uif2app.view.PubShowUpableBillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setTransferLogic(getDefaultBillDataLogic_1b1c1fa());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.billref.dest.DefaultBillDataLogic getDefaultBillDataLogic_1b1c1fa(){
 if(context.get("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#1b1c1fa")!=null)
 return (nc.ui.pubapp.billref.dest.DefaultBillDataLogic)context.get("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#1b1c1fa");
  nc.ui.pubapp.billref.dest.DefaultBillDataLogic bean = new nc.ui.pubapp.billref.dest.DefaultBillDataLogic();
  context.put("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#1b1c1fa",bean);
  bean.setBillForm((nc.ui.pubapp.uif2app.view.PubShowUpableBillForm)findBeanInUIF2BeanFactory("billFormEditor"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.processor.CopyActionProcessor getCopyActionProcessor(){
 if(context.get("copyActionProcessor")!=null)
 return (nc.ui.pu.m422x.action.processor.CopyActionProcessor)context.get("copyActionProcessor");
  nc.ui.pu.m422x.action.processor.CopyActionProcessor bean = new nc.ui.pu.m422x.action.processor.CopyActionProcessor();
  context.put("copyActionProcessor",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.AddMenuAction getAddMenu(){
 if(context.get("addMenu")!=null)
 return (nc.ui.pubapp.uif2app.actions.AddMenuAction)context.get("addMenu");
  nc.ui.pubapp.uif2app.actions.AddMenuAction bean = new nc.ui.pubapp.uif2app.actions.AddMenuAction();
  context.put("addMenu",bean);
  bean.setBillType("422X");
  bean.setTooltip(getI18nFB_a1a91d());
  bean.setActions(getManagedList4());
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setPfAddInfoLoader((nc.ui.pubapp.uif2app.actions.PfAddInfoLoader)findBeanInUIF2BeanFactory("pfAddInfoLoader"));
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_a1a91d(){
 if(context.get("nc.ui.uif2.I18nFB#a1a91d")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#a1a91d");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#a1a91d",bean);  bean.setResDir("common");
  bean.setResId("04004000-0001");
  bean.setDefaultValue("新增业务数据(Ctrl+N)");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#a1a91d",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList4(){  List list = new ArrayList();  list.add(getAddManualAction());  list.add(getSeparatorAction());  list.add(getAddFrom4D14Action());  return list;}

public nc.funcnode.ui.action.SeparatorAction getSeparatorAction(){
 if(context.get("separatorAction")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("separatorAction");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("separatorAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.AddManualAction getAddManualAction(){
 if(context.get("addManualAction")!=null)
 return (nc.ui.pu.m422x.action.AddManualAction)context.get("addManualAction");
  nc.ui.pu.m422x.action.AddManualAction bean = new nc.ui.pu.m422x.action.AddManualAction();
  context.put("addManualAction",bean);
  bean.setBtnName(getI18nFB_165eabd());
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.uif2.editor.IBillCardPanelEditor)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setInterceptor(getFormInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_165eabd(){
 if(context.get("nc.ui.uif2.I18nFB#165eabd")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#165eabd");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#165eabd",bean);  bean.setResDir("common");
  bean.setResId("14004000-0000");
  bean.setDefaultValue("自制");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#165eabd",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.pu.m422x.action.AddFrom4D14Action getAddFrom4D14Action(){
 if(context.get("addFrom4D14Action")!=null)
 return (nc.ui.pu.m422x.action.AddFrom4D14Action)context.get("addFrom4D14Action");
  nc.ui.pu.m422x.action.AddFrom4D14Action bean = new nc.ui.pu.m422x.action.AddFrom4D14Action();
  context.put("addFrom4D14Action",bean);
  bean.setSourceBillType("4D14");
  bean.setSourceBillName(getI18nFB_162f6c());
  bean.setFlowBillType(false);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.uif2.editor.IBillCardPanelEditor)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setTransferViewProcessor(getTransferViewProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_162f6c(){
 if(context.get("nc.ui.uif2.I18nFB#162f6c")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#162f6c");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#162f6c",bean);  bean.setResDir("common");
  bean.setResId("14004000-0101");
  bean.setDefaultValue("物资及服务需求单");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#162f6c",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.pu.m422x.view.StoreReqQueryDLGInitializer getQryDLGInitializer(){
 if(context.get("qryDLGInitializer")!=null)
 return (nc.ui.pu.m422x.view.StoreReqQueryDLGInitializer)context.get("qryDLGInitializer");
  nc.ui.pu.m422x.view.StoreReqQueryDLGInitializer bean = new nc.ui.pu.m422x.view.StoreReqQueryDLGInitializer();
  context.put("qryDLGInitializer",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction getQueryAction(){
 if(context.get("queryAction")!=null)
 return (nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction)context.get("queryAction");
  nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction();
  context.put("queryAction",bean);
  bean.setDataManager((nc.ui.pubapp.uif2app.query2.model.IModelDataManager)findBeanInUIF2BeanFactory("modelDataManager"));
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setQryCondDLGInitializer(getQryDLGInitializer());
  bean.setShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent)findBeanInUIF2BeanFactory("listView"));
  bean.setTemplateContainer((nc.ui.uif2.editor.QueryTemplateContainer)findBeanInUIF2BeanFactory("queryTemplateContainer"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction getRefreshAction(){
 if(context.get("refreshAction")!=null)
 return (nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction)context.get("refreshAction");
  nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction();
  context.put("refreshAction",bean);
  bean.setDataManager((nc.ui.pubapp.uif2app.query2.model.IModelDataManager)findBeanInUIF2BeanFactory("modelDataManager"));
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.RefreshSingleAction getCardRefreshAction(){
 if(context.get("cardRefreshAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.RefreshSingleAction)context.get("cardRefreshAction");
  nc.ui.pubapp.uif2app.actions.RefreshSingleAction bean = new nc.ui.pubapp.uif2app.actions.RefreshSingleAction();
  context.put("cardRefreshAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.StoreReqAppEditAction getEditAction(){
 if(context.get("editAction")!=null)
 return (nc.ui.pu.m422x.action.StoreReqAppEditAction)context.get("editAction");
  nc.ui.pu.m422x.action.StoreReqAppEditAction bean = new nc.ui.pu.m422x.action.StoreReqAppEditAction();
  context.put("editAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.pubapp.uif2app.view.BillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setInterceptor(getFormInterceptor());
  bean.setPowercheck(true);
  bean.setPermissioncode("422X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.pub.power.PowerValidateService getDeletepowervalidservice(){
 if(context.get("deletepowervalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("deletepowervalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("deletepowervalidservice",bean);
  bean.setActionCode("delete");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("422X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.DeleteAction getDeleteAction(){
 if(context.get("deleteAction")!=null)
 return (nc.ui.pu.m422x.action.DeleteAction)context.get("deleteAction");
  nc.ui.pu.m422x.action.DeleteAction bean = new nc.ui.pu.m422x.action.DeleteAction();
  context.put("deleteAction",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.BillManageModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setActionName("DISCARD");
  bean.setBillType("422X");
  bean.setEditor((nc.ui.uif2.editor.IEditor)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setValidationService(getDeletepowervalidservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.CopyAction getCopyAction(){
 if(context.get("copyAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.CopyAction)context.get("copyAction");
  nc.ui.pubapp.uif2app.actions.CopyAction bean = new nc.ui.pubapp.uif2app.actions.CopyAction();
  context.put("copyAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.pubapp.uif2app.view.BillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setCopyActionProcessor(getCopyActionProcessor());
  bean.setInterceptor(getFormInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.SaveAction getSaveAction(){
 if(context.get("saveAction")!=null)
 return (nc.ui.pu.m422x.action.SaveAction)context.get("saveAction");
  nc.ui.pu.m422x.action.SaveAction bean = new nc.ui.pu.m422x.action.SaveAction();
  context.put("saveAction",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.BillManageModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.uif2.editor.IEditor)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setActionName("SAVEBASE");
  bean.setBillType("422X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.CancelAction getCancelAction(){
 if(context.get("cancelAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.CancelAction)context.get("cancelAction");
  nc.ui.pubapp.uif2app.actions.CancelAction bean = new nc.ui.pubapp.uif2app.actions.CancelAction();
  context.put("cancelAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.SendApproveAction getSendApproveAction(){
 if(context.get("sendApproveAction")!=null)
 return (nc.ui.pu.m422x.action.SendApproveAction)context.get("sendApproveAction");
  nc.ui.pu.m422x.action.SendApproveAction bean = new nc.ui.pu.m422x.action.SendApproveAction();
  context.put("sendApproveAction",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.BillManageModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.uif2.editor.IEditor)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setActionName("SAVE");
  bean.setBillType("422X");
  bean.setPreActionNames(getManagedList5());
  bean.setFilledUpInFlow(true);
  bean.setValidationService(getSendpowervalidservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add("SAVEBASE");  return list;}

public nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction getSaveApproveAction(){
 if(context.get("saveApproveAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction)context.get("saveApproveAction");
  nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction bean = new nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction(getSaveAction(),getSendApproveAction());  context.put("saveApproveAction",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.BillManageModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setBtnName(getI18nFB_83710a());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_83710a(){
 if(context.get("nc.ui.uif2.I18nFB#83710a")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#83710a");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#83710a",bean);  bean.setResDir("common");
  bean.setResId("2SCMPUB-000027");
  bean.setDefaultValue("保存提交");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#83710a",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.pu.pub.action.UnSaveScriptAction getRevokeAction(){
 if(context.get("revokeAction")!=null)
 return (nc.ui.pu.pub.action.UnSaveScriptAction)context.get("revokeAction");
  nc.ui.pu.pub.action.UnSaveScriptAction bean = new nc.ui.pu.pub.action.UnSaveScriptAction();
  context.put("revokeAction",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.BillManageModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.uif2.editor.IEditor)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setActionName("UNSAVEBILL");
  bean.setBillType("422X");
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
  bean.setPermissionCode("422X");
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
  bean.setPermissionCode("422X");
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
  bean.setActions(getManagedList6());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add(getSendApproveAction());  list.add(getRevokeAction());  return list;}

public nc.ui.pubapp.pub.power.PowerValidateService getApprovepowervalidservice(){
 if(context.get("approvepowervalidservice")!=null)
 return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("approvepowervalidservice");
  nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
  context.put("approvepowervalidservice",bean);
  bean.setActionCode("approve");
  bean.setBillCodeFiledName("vbillcode");
  bean.setPermissionCode("422X");
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
  bean.setPermissionCode("422X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.StoreReqAppApproveAction getApproveAction(){
 if(context.get("approveAction")!=null)
 return (nc.ui.pu.m422x.action.StoreReqAppApproveAction)context.get("approveAction");
  nc.ui.pu.m422x.action.StoreReqAppApproveAction bean = new nc.ui.pu.m422x.action.StoreReqAppApproveAction();
  context.put("approveAction",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.BillManageModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setActionName("APPROVE");
  bean.setFilledUpInFlow(false);
  bean.setBillType("422X");
  bean.setEditor((nc.ui.uif2.editor.IEditor)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setValidationService(getApprovepowervalidservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.StoreReqAppUnApproveAction getUnApproveAction(){
 if(context.get("unApproveAction")!=null)
 return (nc.ui.pu.m422x.action.StoreReqAppUnApproveAction)context.get("unApproveAction");
  nc.ui.pu.m422x.action.StoreReqAppUnApproveAction bean = new nc.ui.pu.m422x.action.StoreReqAppUnApproveAction();
  context.put("unApproveAction",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.BillManageModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setActionName("UNAPPROVE");
  bean.setFilledUpInFlow(false);
  bean.setBillType("422X");
  bean.setEditor((nc.ui.uif2.editor.IEditor)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setValidationService(getUnapprovepowervalidservice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getAuditMenuAction(){
 if(context.get("auditMenuAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("auditMenuAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("auditMenuAction",bean);
  bean.setCode("auditMenuAction");
  bean.setActions(getManagedList7());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList7(){  List list = new ArrayList();  list.add(getApproveAction());  list.add(getUnApproveAction());  list.add(getSeparatorAction());  list.add(getApprovingStatusAction());  return list;}

public nc.ui.pu.m422x.action.CloseAction getCloseAction(){
 if(context.get("closeAction")!=null)
 return (nc.ui.pu.m422x.action.CloseAction)context.get("closeAction");
  nc.ui.pu.m422x.action.CloseAction bean = new nc.ui.pu.m422x.action.CloseAction();
  context.put("closeAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.OpenAction getOpenAction(){
 if(context.get("openAction")!=null)
 return (nc.ui.pu.m422x.action.OpenAction)context.get("openAction");
  nc.ui.pu.m422x.action.OpenAction bean = new nc.ui.pu.m422x.action.OpenAction();
  context.put("openAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.RowCloseAction getRowCloseAction(){
 if(context.get("rowCloseAction")!=null)
 return (nc.ui.pu.m422x.action.RowCloseAction)context.get("rowCloseAction");
  nc.ui.pu.m422x.action.RowCloseAction bean = new nc.ui.pu.m422x.action.RowCloseAction();
  context.put("rowCloseAction",bean);
  bean.setBillForm((nc.ui.pubapp.uif2app.view.BillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setList((nc.ui.uif2.editor.BillListView)findBeanInUIF2BeanFactory("listView"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.RowOpenAction getRowOpenAction(){
 if(context.get("rowOpenAction")!=null)
 return (nc.ui.pu.m422x.action.RowOpenAction)context.get("rowOpenAction");
  nc.ui.pu.m422x.action.RowOpenAction bean = new nc.ui.pu.m422x.action.RowOpenAction();
  context.put("rowOpenAction",bean);
  bean.setBillForm((nc.ui.pubapp.uif2app.view.BillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setList((nc.ui.uif2.editor.BillListView)findBeanInUIF2BeanFactory("listView"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.FileDocManageAction getFileAction(){
 if(context.get("fileAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.FileDocManageAction)context.get("fileAction");
  nc.ui.pubapp.uif2app.actions.FileDocManageAction bean = new nc.ui.pubapp.uif2app.actions.FileDocManageAction();
  context.put("fileAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.AssistAction getAssistMenuAction(){
 if(context.get("assistMenuAction")!=null)
 return (nc.ui.pu.m422x.action.AssistAction)context.get("assistMenuAction");
  nc.ui.pu.m422x.action.AssistAction bean = new nc.ui.pu.m422x.action.AssistAction();
  context.put("assistMenuAction",bean);
  bean.setActions(getManagedList8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getCloseAction());  list.add(getOpenAction());  list.add(getRowCloseAction());  list.add(getRowOpenAction());  list.add(getSeparatorAction());  list.add(getFileAction());  return list;}

public nc.ui.pu.pub.action.PULinkQueryAction getLinkBillAction(){
 if(context.get("linkBillAction")!=null)
 return (nc.ui.pu.pub.action.PULinkQueryAction)context.get("linkBillAction");
  nc.ui.pu.pub.action.PULinkQueryAction bean = new nc.ui.pu.pub.action.PULinkQueryAction();
  context.put("linkBillAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setBillType("422X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.pub.action.QueryOnhandAction getATPQueryAction(){
 if(context.get("ATPQueryAction")!=null)
 return (nc.ui.pu.pub.action.QueryOnhandAction)context.get("ATPQueryAction");
  nc.ui.pu.pub.action.QueryOnhandAction bean = new nc.ui.pu.pub.action.QueryOnhandAction();
  context.put("ATPQueryAction",bean);
  bean.setModel((nc.ui.pubapp.uif2app.model.BillManageModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setForm((nc.ui.pu.pub.view.PUBillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setList((nc.ui.pubapp.uif2app.view.BillListView)findBeanInUIF2BeanFactory("listView"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction getApprovingStatusAction(){
 if(context.get("approvingStatusAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction)context.get("approvingStatusAction");
  nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction bean = new nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction();
  context.put("approvingStatusAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setBillType("422X");
  bean.setBtnName(getI18nFB_1725312());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1725312(){
 if(context.get("nc.ui.uif2.I18nFB#1725312")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1725312");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1725312",bean);  bean.setResDir("4001002_0");
  bean.setResId("04001002-0579");
  bean.setDefaultValue("查看审批意见");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1725312",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.pu.m422x.action.LinkBillMenuAction getLinkBillMenuAction(){
 if(context.get("linkBillMenuAction")!=null)
 return (nc.ui.pu.m422x.action.LinkBillMenuAction)context.get("linkBillMenuAction");
  nc.ui.pu.m422x.action.LinkBillMenuAction bean = new nc.ui.pu.m422x.action.LinkBillMenuAction();
  context.put("linkBillMenuAction",bean);
  bean.setActions(getManagedList9());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList9(){  List list = new ArrayList();  list.add(getLinkBillAction());  list.add(getATPQueryAction());  return list;}

public nc.ui.pu.m422x.action.LinkBillMenuAction getLinkBillMenuActionOnEdit(){
 if(context.get("linkBillMenuActionOnEdit")!=null)
 return (nc.ui.pu.m422x.action.LinkBillMenuAction)context.get("linkBillMenuActionOnEdit");
  nc.ui.pu.m422x.action.LinkBillMenuAction bean = new nc.ui.pu.m422x.action.LinkBillMenuAction();
  context.put("linkBillMenuActionOnEdit",bean);
  bean.setActions(getManagedList10());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList10(){  List list = new ArrayList();  list.add(getATPQueryAction());  return list;}

public nc.ui.pu.m422x.action.processor.StoreReqPrintProcessor getPrintProcessor(){
 if(context.get("printProcessor")!=null)
 return (nc.ui.pu.m422x.action.processor.StoreReqPrintProcessor)context.get("printProcessor");
  nc.ui.pu.m422x.action.processor.StoreReqPrintProcessor bean = new nc.ui.pu.m422x.action.processor.StoreReqPrintProcessor();
  context.put("printProcessor",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
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
  bean.setNodeKey("4004000002");
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
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
  bean.setNodeKey("4004000002");
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
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
  bean.setNodeKey("4004000002");
  bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setParent((java.awt.Container)findBeanInUIF2BeanFactory("billFormEditor"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m422x.action.CombineAction getCombineAction(){
 if(context.get("combineAction")!=null)
 return (nc.ui.pu.m422x.action.CombineAction)context.get("combineAction");
  nc.ui.pu.m422x.action.CombineAction bean = new nc.ui.pu.m422x.action.CombineAction();
  context.put("combineAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setEditor((nc.ui.pubapp.uif2app.view.ShowUpableBillForm)findBeanInUIF2BeanFactory("billFormEditor"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmpub.action.SCMPrintCountQueryAction getPrintCountQueryAction(){
 if(context.get("printCountQueryAction")!=null)
 return (nc.ui.scmpub.action.SCMPrintCountQueryAction)context.get("printCountQueryAction");
  nc.ui.scmpub.action.SCMPrintCountQueryAction bean = new nc.ui.scmpub.action.SCMPrintCountQueryAction();
  context.put("printCountQueryAction",bean);
  bean.setModel((nc.ui.uif2.model.AbstractAppModel)findBeanInUIF2BeanFactory("manageAppModel"));
  bean.setBilldateFieldName("dbilldate");
  bean.setBillType("422X");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintMenuAction(){
 if(context.get("printMenuAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printMenuAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printMenuAction",bean);
  bean.setCode("printMenuAction");
  bean.setActions(getManagedList11());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList11(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreviewAction());  list.add(getOutputAction());  list.add(getPrintCountQueryAction());  list.add(getSeparatorAction());  list.add(getCombineAction());  return list;}

public nc.ui.pu.m422x.billref.dest.StoreReqTransferListProcessor getStoreReqTransferListProcessor(){
 if(context.get("storeReqTransferListProcessor")!=null)
 return (nc.ui.pu.m422x.billref.dest.StoreReqTransferListProcessor)context.get("storeReqTransferListProcessor");
  nc.ui.pu.m422x.billref.dest.StoreReqTransferListProcessor bean = new nc.ui.pu.m422x.billref.dest.StoreReqTransferListProcessor();
  context.put("storeReqTransferListProcessor",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.billref.dest.TransferViewProcessor getTransferViewProcessor(){
 if(context.get("transferViewProcessor")!=null)
 return (nc.ui.pubapp.billref.dest.TransferViewProcessor)context.get("transferViewProcessor");
  nc.ui.pubapp.billref.dest.TransferViewProcessor bean = new nc.ui.pubapp.billref.dest.TransferViewProcessor();
  context.put("transferViewProcessor",bean);
  bean.setList((nc.ui.pubapp.uif2app.view.ShowUpableBillListView)findBeanInUIF2BeanFactory("listView"));
  bean.setActionContainer(getActionsOfList());
  bean.setCardActionContainer(getActionsOfCard());
  bean.setSaveAction(getSaveAction());
  bean.setCommitAction(getSendApproveAction());
  bean.setCancelAction(getCancelAction());
  bean.setBillForm((nc.ui.pubapp.uif2app.view.PubShowUpableBillForm)findBeanInUIF2BeanFactory("billFormEditor"));
  bean.setQueryAreaShell((nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell)findBeanInUIF2BeanFactory("queryArea"));
  bean.setListProcessor(getStoreReqTransferListProcessor());
  bean.setQueryInfoToolbarPanel((nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)findBeanInUIF2BeanFactory("queryInfo"));
  bean.setTransferLogic(getDefaultBillDataLogic_abcaaa());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.billref.dest.DefaultBillDataLogic getDefaultBillDataLogic_abcaaa(){
 if(context.get("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#abcaaa")!=null)
 return (nc.ui.pubapp.billref.dest.DefaultBillDataLogic)context.get("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#abcaaa");
  nc.ui.pubapp.billref.dest.DefaultBillDataLogic bean = new nc.ui.pubapp.billref.dest.DefaultBillDataLogic();
  context.put("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#abcaaa",bean);
  bean.setBillForm((nc.ui.pubapp.uif2app.view.PubShowUpableBillForm)findBeanInUIF2BeanFactory("billFormEditor"));
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
