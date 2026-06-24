package nc.ui.pu.m21.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class payplan_config extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener)context.get("InitDataListener");
  nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener bean = new nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener();
  context.put("InitDataListener",bean);
  bean.setContext(getContext());
  bean.setModel(getBatchBillTableModel());
  bean.setQueryAction(getQueryAction());
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

public nc.ui.pu.m21.service.OrderPayPlanModelService getBatchModelService(){
 if(context.get("batchModelService")!=null)
 return (nc.ui.pu.m21.service.OrderPayPlanModelService)context.get("batchModelService");
  nc.ui.pu.m21.service.OrderPayPlanModelService bean = new nc.ui.pu.m21.service.OrderPayPlanModelService();
  context.put("batchModelService",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.value.CAVO2BDObjectAdapterFactory getBoadatorfactory(){
 if(context.get("boadatorfactory")!=null)
 return (nc.ui.pubapp.uif2app.view.value.CAVO2BDObjectAdapterFactory)context.get("boadatorfactory");
  nc.ui.pubapp.uif2app.view.value.CAVO2BDObjectAdapterFactory bean = new nc.ui.pubapp.uif2app.view.value.CAVO2BDObjectAdapterFactory();
  context.put("boadatorfactory",bean);
  bean.setId_field("pk_order_payplan");
  bean.setCode_field("vbillcode");
  bean.setName_field("vbillcode");
  bean.setPid_field("pk_order");
  bean.setPk_org_field("pk_org");
  bean.setPk_group_field("pk_group");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.model.DefaultBatchValidationService getBatchValidateSerice(){
 if(context.get("batchValidateSerice")!=null)
 return (nc.ui.uif2.model.DefaultBatchValidationService)context.get("batchValidateSerice");
  nc.ui.uif2.model.DefaultBatchValidationService bean = new nc.ui.uif2.model.DefaultBatchValidationService();
  context.put("batchValidateSerice",bean);
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.model.PayPlanModel getBatchBillTableModel(){
 if(context.get("batchBillTableModel")!=null)
 return (nc.ui.scmf.payplan.model.PayPlanModel)context.get("batchBillTableModel");
  nc.ui.scmf.payplan.model.PayPlanModel bean = new nc.ui.scmf.payplan.model.PayPlanModel();
  context.put("batchBillTableModel",bean);
  bean.setContext(getContext());
  bean.setService(getBatchModelService());
  bean.setEditor(getList());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setValidationService(getBatchValidateSerice());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.model.ModelDataManager getModelDataManager(){
 if(context.get("modelDataManager")!=null)
 return (nc.ui.pubapp.uif2app.query2.model.ModelDataManager)context.get("modelDataManager");
  nc.ui.pubapp.uif2app.query2.model.ModelDataManager bean = new nc.ui.pubapp.uif2app.query2.model.ModelDataManager();
  context.put("modelDataManager",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setService(getBatchModelService());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.FunNodeClosingHandler getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.uif2.FunNodeClosingHandler)context.get("ClosingListener");
  nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
  context.put("ClosingListener",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setSaveaction(getSaveAction());
  bean.setCancelaction(getCancelAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.view.PayPlanOrgPanel getOrgPanel(){
 if(context.get("orgPanel")!=null)
 return (nc.ui.scmf.payplan.view.PayPlanOrgPanel)context.get("orgPanel");
  nc.ui.scmf.payplan.view.PayPlanOrgPanel bean = new nc.ui.scmf.payplan.view.PayPlanOrgPanel();
  context.put("orgPanel",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setDataManager(getModelDataManager());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.view.PayPlanVOValueAdapter getComponentValueManager(){
 if(context.get("componentValueManager")!=null)
 return (nc.ui.scmf.payplan.view.PayPlanVOValueAdapter)context.get("componentValueManager");
  nc.ui.scmf.payplan.view.PayPlanVOValueAdapter bean = new nc.ui.scmf.payplan.view.PayPlanVOValueAdapter();
  context.put("componentValueManager",bean);
  bean.setBodyVOName("nc.vo.pu.m21.entity.PayPlanViewVO");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.view.PayPlanBillForm getList(){
 if(context.get("list")!=null)
 return (nc.ui.scmf.payplan.view.PayPlanBillForm)context.get("list");
  nc.ui.scmf.payplan.view.PayPlanBillForm bean = new nc.ui.scmf.payplan.view.PayPlanBillForm();
  context.put("list",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setComponentValueManager(getComponentValueManager());
  bean.setVoClassName("nc.vo.pu.m21.entity.PayPlanViewVO");
  bean.setIsBodyAutoAddLine(false);
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.editor.PayPlanOrgChgEventHandler getPurhchaseOrganization(){
 if(context.get("purhchaseOrganization")!=null)
 return (nc.ui.scmf.payplan.editor.PayPlanOrgChgEventHandler)context.get("purhchaseOrganization");
  nc.ui.scmf.payplan.editor.PayPlanOrgChgEventHandler bean = new nc.ui.scmf.payplan.editor.PayPlanOrgChgEventHandler();
  context.put("purhchaseOrganization",bean);
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m21.editor.payplan.card.beforeedit.PayplanCardBodyBeforeEditEventHandler getCardBodyBeforeEdit(){
 if(context.get("cardBodyBeforeEdit")!=null)
 return (nc.ui.pu.m21.editor.payplan.card.beforeedit.PayplanCardBodyBeforeEditEventHandler)context.get("cardBodyBeforeEdit");
  nc.ui.pu.m21.editor.payplan.card.beforeedit.PayplanCardBodyBeforeEditEventHandler bean = new nc.ui.pu.m21.editor.payplan.card.beforeedit.PayplanCardBodyBeforeEditEventHandler();
  context.put("cardBodyBeforeEdit",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m21.editor.payplan.card.afteredit.PayPlanCardBodyAfterEditEventHandler getCardBodyAfterEdit(){
 if(context.get("cardBodyAfterEdit")!=null)
 return (nc.ui.pu.m21.editor.payplan.card.afteredit.PayPlanCardBodyAfterEditEventHandler)context.get("cardBodyAfterEdit");
  nc.ui.pu.m21.editor.payplan.card.afteredit.PayPlanCardBodyAfterEditEventHandler bean = new nc.ui.pu.m21.editor.payplan.card.afteredit.PayPlanCardBodyAfterEditEventHandler();
  context.put("cardBodyAfterEdit",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.model.AppEventHandlerMediator getAppEventHandlerMediator(){
 if(context.get("appEventHandlerMediator")!=null)
 return (nc.ui.pubapp.uif2app.model.AppEventHandlerMediator)context.get("appEventHandlerMediator");
  nc.ui.pubapp.uif2app.model.AppEventHandlerMediator bean = new nc.ui.pubapp.uif2app.model.AppEventHandlerMediator();
  context.put("appEventHandlerMediator",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setHandlerMap(getManagedMap0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("nc.ui.pubapp.uif2app.event.OrgChangedEvent",getManagedList0());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent",getManagedList1());  map.put("nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent",getManagedList2());  return map;}

private List getManagedList0(){  List list = new ArrayList();  list.add(getPurhchaseOrganization());  return list;}

private List getManagedList1(){  List list = new ArrayList();  list.add(getCardBodyBeforeEdit());  return list;}

private List getManagedList2(){  List list = new ArrayList();  list.add(getCardBodyAfterEdit());  return list;}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayoutRoot(getVSNode_ad0ac9());
  bean.setActions(getManagedList3());
  bean.setEditActions(getManagedList4());
  bean.setModel(getBatchBillTableModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_ad0ac9(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#ad0ac9")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#ad0ac9");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#ad0ac9",bean);
  bean.setDown(getCNode_f88bcd());
  bean.setUp(getCNode_1a255c5());
  bean.setDividerLocation(31f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_f88bcd(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#f88bcd")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#f88bcd");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#f88bcd",bean);
  bean.setComponent(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1a255c5(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1a255c5")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1a255c5");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1a255c5",bean);
  bean.setComponent(getOrgPanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}
//增加同步OA按钮
private List getManagedList3(){  List list = new ArrayList();  list.add(getEditAction());  list.add(getSeparatorAction());  list.add(getQueryAction());  list.add(getRefreshAction());  list.add(getSeparatorAction());  list.add(getPayAction());  list.add(getPayreqAction());  list.add(getSeparatorAction());  list.add(getLinkQueryAction());  list.add(getSeparatorAction());  list.add(getPrintMenuAction());  list.add(getSeparatorAction()); list.add(getOaAction()); return list;}

private List getManagedList4(){  List list = new ArrayList();  list.add(getSaveAction());  list.add(getSeparatorAction());  list.add(getCancelAction());  list.add(getSeparatorAction());  list.add(getAddAction());  list.add(getInsAction());  list.add(getDelAction());  list.add(getCopyAction());  list.add(getPasteAction());  return list;}

public nc.ui.pu.m21.view.PayPlanDLGInitializer getOrderQryDLGInitializer(){
 if(context.get("orderQryDLGInitializer")!=null)
 return (nc.ui.pu.m21.view.PayPlanDLGInitializer)context.get("orderQryDLGInitializer");
  nc.ui.pu.m21.view.PayPlanDLGInitializer bean = new nc.ui.pu.m21.view.PayPlanDLGInitializer();
  context.put("orderQryDLGInitializer",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.action.PayPlanQueryAction getQueryAction(){
 if(context.get("queryAction")!=null)
 return (nc.ui.scmf.payplan.action.PayPlanQueryAction)context.get("queryAction");
  nc.ui.scmf.payplan.action.PayPlanQueryAction bean = new nc.ui.scmf.payplan.action.PayPlanQueryAction();
  context.put("queryAction",bean);
  bean.setDataManager(getModelDataManager());
  bean.setQryCondDLGInitializer(getOrderQryDLGInitializer());
  bean.setModel(getBatchBillTableModel());
  bean.setOrgPanel(getOrgPanel());
  bean.setHasQueryArea(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction getRefreshAction(){
 if(context.get("refreshAction")!=null)
 return (nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction)context.get("refreshAction");
  nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction();
  context.put("refreshAction",bean);
  bean.setDataManager(getModelDataManager());
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.action.PayPlanAddLineAction getAddAction(){
 if(context.get("addAction")!=null)
 return (nc.ui.scmf.payplan.action.PayPlanAddLineAction)context.get("addAction");
  nc.ui.scmf.payplan.action.PayPlanAddLineAction bean = new nc.ui.scmf.payplan.action.PayPlanAddLineAction();
  context.put("addAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setVoClassName("nc.vo.pu.m21.entity.PayPlanViewVO");
  bean.setCopyItems(getManagedList5());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add("vbillcode");  list.add("dbilldate");  list.add("pk_supplier");  list.add("corigcurrencyid");  list.add("ccurrencyid");  list.add("nexchangerate");  list.add("pk_financeorg");  list.add("pk_order");  list.add("pk_financeorg_v");  list.add("hts");  list.add("forderstatus");  list.add("ntotalorigmny");  list.add("pk_group");  return list;}

public nc.ui.scmf.payplan.action.PayPlanInsertLineAction getInsAction(){
 if(context.get("insAction")!=null)
 return (nc.ui.scmf.payplan.action.PayPlanInsertLineAction)context.get("insAction");
  nc.ui.scmf.payplan.action.PayPlanInsertLineAction bean = new nc.ui.scmf.payplan.action.PayPlanInsertLineAction();
  context.put("insAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setVoClassName("nc.vo.pu.m21.entity.PayPlanViewVO");
  bean.setCopyItems(getManagedList6());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add("vbillcode");  list.add("dbilldate");  list.add("pk_supplier");  list.add("corigcurrencyid");  list.add("ccurrencyid");  list.add("nexchangerate");  list.add("pk_financeorg");  list.add("pk_order");  list.add("pk_financeorg_v");  list.add("hts");  list.add("forderstatus");  list.add("ntotalorigmny");  list.add("pk_group");  return list;}

public nc.ui.pu.m21.action.payplan.PayPlanEditAction getEditAction(){
 if(context.get("editAction")!=null)
 return (nc.ui.pu.m21.action.payplan.PayPlanEditAction)context.get("editAction");
  nc.ui.pu.m21.action.payplan.PayPlanEditAction bean = new nc.ui.pu.m21.action.payplan.PayPlanEditAction();
  context.put("editAction",bean);
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m21.action.payplan.PayPlanToPayAction getPayAction(){
 if(context.get("payAction")!=null)
 return (nc.ui.pu.m21.action.payplan.PayPlanToPayAction)context.get("payAction");
  nc.ui.pu.m21.action.payplan.PayPlanToPayAction bean = new nc.ui.pu.m21.action.payplan.PayPlanToPayAction();
  context.put("payAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m21.action.payplan.PayPlan2OAAction getOaAction(){
	 if(context.get("oaAction")!=null)
	 return (nc.ui.pu.m21.action.payplan.PayPlan2OAAction)context.get("oaAction");
	  nc.ui.pu.m21.action.payplan.PayPlan2OAAction bean = new nc.ui.pu.m21.action.payplan.PayPlan2OAAction();
	  context.put("oaAction",bean);
	  bean.setModel(getBatchBillTableModel());
	  bean.setEditor(getList());
	setBeanFacotryIfBeanFacatoryAware(bean);
	invokeInitializingBean(bean);
	return bean;
	}

public nc.ui.pu.m21.action.payplan.PayPlanToPayReqAction getPayreqAction(){
 if(context.get("payreqAction")!=null)
 return (nc.ui.pu.m21.action.payplan.PayPlanToPayReqAction)context.get("payreqAction");
  nc.ui.pu.m21.action.payplan.PayPlanToPayReqAction bean = new nc.ui.pu.m21.action.payplan.PayPlanToPayReqAction();
  context.put("payreqAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.action.PayPlanDelLineAction getDelAction(){
 if(context.get("delAction")!=null)
 return (nc.ui.scmf.payplan.action.PayPlanDelLineAction)context.get("delAction");
  nc.ui.scmf.payplan.action.PayPlanDelLineAction bean = new nc.ui.scmf.payplan.action.PayPlanDelLineAction();
  context.put("delAction",bean);
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.batch.BatchCopyLineAction getCopyAction(){
 if(context.get("copyAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.batch.BatchCopyLineAction)context.get("copyAction");
  nc.ui.pubapp.uif2app.actions.batch.BatchCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.batch.BatchCopyLineAction();
  context.put("copyAction",bean);
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.scmf.payplan.action.PayPlanPasteLineAction getPasteAction(){
 if(context.get("pasteAction")!=null)
 return (nc.ui.scmf.payplan.action.PayPlanPasteLineAction)context.get("pasteAction");
  nc.ui.scmf.payplan.action.PayPlanPasteLineAction bean = new nc.ui.scmf.payplan.action.PayPlanPasteLineAction();
  context.put("pasteAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setClearItems(getManagedList7());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList7(){  List list = new ArrayList();  list.add("naccumpayorgmny");  list.add("naccumpaymny");  list.add("naccumpayapporgmny");  list.add("naccumpayappmny");  list.add("pk_order_payplan");  return list;}

public nc.ui.pubapp.uif2app.actions.batch.BatchSaveAction getSaveAction(){
 if(context.get("saveAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.batch.BatchSaveAction)context.get("saveAction");
  nc.ui.pubapp.uif2app.actions.batch.BatchSaveAction bean = new nc.ui.pubapp.uif2app.actions.batch.BatchSaveAction();
  context.put("saveAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.actions.batch.BatchCancelAction getCancelAction(){
 if(context.get("cancelAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.batch.BatchCancelAction)context.get("cancelAction");
  nc.ui.pubapp.uif2app.actions.batch.BatchCancelAction bean = new nc.ui.pubapp.uif2app.actions.batch.BatchCancelAction();
  context.put("cancelAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m21.action.payplan.OrderPayPlanLinkQueryAction getLinkQueryAction(){
 if(context.get("linkQueryAction")!=null)
 return (nc.ui.pu.m21.action.payplan.OrderPayPlanLinkQueryAction)context.get("linkQueryAction");
  nc.ui.pu.m21.action.payplan.OrderPayPlanLinkQueryAction bean = new nc.ui.pu.m21.action.payplan.OrderPayPlanLinkQueryAction();
  context.put("linkQueryAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setBillType("21");
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

public nc.ui.scmf.payplan.action.PayPlanPrintProcesser getPrintProcessor(){
 if(context.get("printProcessor")!=null)
 return (nc.ui.scmf.payplan.action.PayPlanPrintProcesser)context.get("printProcessor");
  nc.ui.scmf.payplan.action.PayPlanPrintProcesser bean = new nc.ui.scmf.payplan.action.PayPlanPrintProcesser();
  context.put("printProcessor",bean);
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m21.action.payplan.OrderPayPlanPrintAction getPreviewAction(){
 if(context.get("previewAction")!=null)
 return (nc.ui.pu.m21.action.payplan.OrderPayPlanPrintAction)context.get("previewAction");
  nc.ui.pu.m21.action.payplan.OrderPayPlanPrintAction bean = new nc.ui.pu.m21.action.payplan.OrderPayPlanPrintAction();
  context.put("previewAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setPreview(true);
  bean.setBeforePrintDataProcess(getPrintProcessor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pu.m21.action.payplan.OrderPayPlanPrintAction getPrintAction(){
 if(context.get("printAction")!=null)
 return (nc.ui.pu.m21.action.payplan.OrderPayPlanPrintAction)context.get("printAction");
  nc.ui.pu.m21.action.payplan.OrderPayPlanPrintAction bean = new nc.ui.pu.m21.action.payplan.OrderPayPlanPrintAction();
  context.put("printAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setPreview(false);
  bean.setBeforePrintDataProcess(getPrintProcessor());
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
  bean.setActions(getManagedList8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreviewAction());  return list;}

}
