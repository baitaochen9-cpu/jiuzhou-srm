package nc.ui.bd.defdoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class defdoc_producter extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.bd.defdoc.DefdocLoginContext getContext(){
 if(context.get("context")!=null)
 return (nc.ui.bd.defdoc.DefdocLoginContext)context.get("context");
  nc.ui.bd.defdoc.DefdocLoginContext bean = new nc.ui.bd.defdoc.DefdocLoginContext();
  context.put("context",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.DefdocObjectAdpaterFactory getBoadatorfactory(){
 if(context.get("boadatorfactory")!=null)
 return (nc.ui.bd.defdoc.DefdocObjectAdpaterFactory)context.get("boadatorfactory");
  nc.ui.bd.defdoc.DefdocObjectAdpaterFactory bean = new nc.ui.bd.defdoc.DefdocObjectAdpaterFactory();
  context.put("boadatorfactory",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.DefdocAppService getAppModelService(){
 if(context.get("appModelService")!=null)
 return (nc.ui.bd.defdoc.DefdocAppService)context.get("appModelService");
  nc.ui.bd.defdoc.DefdocAppService bean = new nc.ui.bd.defdoc.DefdocAppService();
  context.put("appModelService",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.ValidateFormulasActionInterceptor getTreeCardFormulasInterceptor(){
 if(context.get("treeCardFormulasInterceptor")!=null)
 return (nc.ui.bd.pub.actions.ValidateFormulasActionInterceptor)context.get("treeCardFormulasInterceptor");
  nc.ui.bd.pub.actions.ValidateFormulasActionInterceptor bean = new nc.ui.bd.pub.actions.ValidateFormulasActionInterceptor();
  context.put("treeCardFormulasInterceptor",bean);
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.ValidateFormulasActionInterceptor getListFormulasInterceptor(){
 if(context.get("listFormulasInterceptor")!=null)
 return (nc.ui.bd.pub.actions.ValidateFormulasActionInterceptor)context.get("listFormulasInterceptor");
  nc.ui.bd.pub.actions.ValidateFormulasActionInterceptor bean = new nc.ui.bd.pub.actions.ValidateFormulasActionInterceptor();
  context.put("listFormulasInterceptor",bean);
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.DefdocClosingListener getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.bd.defdoc.DefdocClosingListener)context.get("ClosingListener");
  nc.ui.bd.defdoc.DefdocClosingListener bean = new nc.ui.bd.defdoc.DefdocClosingListener();
  context.put("ClosingListener",bean);
  bean.setContext(getContext());
  bean.setBatchbillModel(getBatchBillTableModel());
  bean.setTreecardModel(getHAppModel());
  bean.setBatchbillSaveAction(getListSaveAction());
  bean.setBatchbillCancelAction(getListCancelAction());
  bean.setTreecardSaveAction(getTreecardSaveAction());
  bean.setTreecardCancelAction(getTreecardCancelAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.model.BatchBillTableModel getBatchBillTableModel(){
 if(context.get("batchBillTableModel")!=null)
 return (nc.ui.uif2.model.BatchBillTableModel)context.get("batchBillTableModel");
  nc.ui.uif2.model.BatchBillTableModel bean = new nc.ui.uif2.model.BatchBillTableModel();
  context.put("batchBillTableModel",bean);
  bean.setService(getAppModelService());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.value.BillCardPanelBodyVOValueAdapter getComponentValueManager(){
 if(context.get("componentValueManager")!=null)
 return (nc.ui.uif2.editor.value.BillCardPanelBodyVOValueAdapter)context.get("componentValueManager");
  nc.ui.uif2.editor.value.BillCardPanelBodyVOValueAdapter bean = new nc.ui.uif2.editor.value.BillCardPanelBodyVOValueAdapter();
  context.put("componentValueManager",bean);
  bean.setBodyVOName("nc.vo.bd.defdoc.DefdocVO");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.editor.DefdocBillListEditor getList(){
 if(context.get("list")!=null)
 return (nc.ui.bd.defdoc.editor.DefdocBillListEditor)context.get("list");
  nc.ui.bd.defdoc.editor.DefdocBillListEditor bean = new nc.ui.bd.defdoc.editor.DefdocBillListEditor();
  context.put("list",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setNodekey("list");
  bean.setVoClassName("nc.vo.bd.defdoc.DefdocVO");
  bean.setBillModelCellEditableController(getBillModelCellEditableController());
  bean.setMdUpdOperateCode("edit");
  bean.setBodyMultiSelectEnable(true);
  bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_18b7d36());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_18b7d36(){
 if(context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#18b7d36")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#18b7d36");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#18b7d36",bean);
  bean.setContainer(getUserdefitemContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.BatchTableCellEditableController getBillModelCellEditableController(){
 if(context.get("billModelCellEditableController")!=null)
 return (nc.ui.bd.pub.BatchTableCellEditableController)context.get("billModelCellEditableController");
  nc.ui.bd.pub.BatchTableCellEditableController bean = new nc.ui.bd.pub.BatchTableCellEditableController();
  context.put("billModelCellEditableController",bean);
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.SeparatorAction getSeperatorAction(){
 if(context.get("seperatorAction")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("seperatorAction");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("seperatorAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocBatchAddAction getListAddAction(){
 if(context.get("listAddAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocBatchAddAction)context.get("listAddAction");
  nc.ui.bd.defdoc.action.DefdocBatchAddAction bean = new nc.ui.bd.defdoc.action.DefdocBatchAddAction();
  context.put("listAddAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setEditor(getList());
  bean.setVoClassName("nc.vo.bd.defdoc.DefdocVO");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.batch.BatchEditAction getListEditAction(){
 if(context.get("listEditAction")!=null)
 return (nc.ui.uif2.actions.batch.BatchEditAction)context.get("listEditAction");
  nc.ui.uif2.actions.batch.BatchEditAction bean = new nc.ui.uif2.actions.batch.BatchEditAction();
  context.put("listEditAction",bean);
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocBatchDelAction getListDelAction(){
 if(context.get("listDelAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocBatchDelAction)context.get("listDelAction");
  nc.ui.bd.defdoc.action.DefdocBatchDelAction bean = new nc.ui.bd.defdoc.action.DefdocBatchDelAction();
  context.put("listDelAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setMdOperateCode("delete");
  bean.setBatchBillTable(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.batch.BatchSaveAction getListSaveAction(){
 if(context.get("listSaveAction")!=null)
 return (nc.ui.uif2.actions.batch.BatchSaveAction)context.get("listSaveAction");
  nc.ui.uif2.actions.batch.BatchSaveAction bean = new nc.ui.uif2.actions.batch.BatchSaveAction();
  context.put("listSaveAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setEditor(getList());
  bean.setValidationService(getNotNullValidatorService());
  bean.setInterceptor(getListFormulasInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.model.DefaultBatchValidationService getNotNullValidatorService(){
 if(context.get("notNullValidatorService")!=null)
 return (nc.ui.uif2.model.DefaultBatchValidationService)context.get("notNullValidatorService");
  nc.ui.uif2.model.DefaultBatchValidationService bean = new nc.ui.uif2.model.DefaultBatchValidationService();
  context.put("notNullValidatorService",bean);
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocBatchCancelAction getListCancelAction(){
 if(context.get("listCancelAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocBatchCancelAction)context.get("listCancelAction");
  nc.ui.bd.defdoc.action.DefdocBatchCancelAction bean = new nc.ui.bd.defdoc.action.DefdocBatchCancelAction();
  context.put("listCancelAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocBatchDisableAction getListDisableAction(){
 if(context.get("listDisableAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocBatchDisableAction)context.get("listDisableAction");
  nc.ui.bd.defdoc.action.DefdocBatchDisableAction bean = new nc.ui.bd.defdoc.action.DefdocBatchDisableAction();
  context.put("listDisableAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setMdOperateCode("disable");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocBatchEnableAction getListEnableAction(){
 if(context.get("listEnableAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocBatchEnableAction)context.get("listEnableAction");
  nc.ui.bd.defdoc.action.DefdocBatchEnableAction bean = new nc.ui.bd.defdoc.action.DefdocBatchEnableAction();
  context.put("listEnableAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setMdOperateCode("enable");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getListEnableGroupAction(){
 if(context.get("listEnableGroupAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("listEnableGroupAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("listEnableGroupAction",bean);
  bean.setCode("listEnable");
  bean.setName(getI18nFB_c97fb0());
  bean.setActions(getManagedList0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_c97fb0(){
 if(context.get("nc.ui.uif2.I18nFB#c97fb0")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#c97fb0");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#c97fb0",bean);  bean.setResDir("common");
  bean.setDefaultValue("启用");
  bean.setResId("UC001-0000111");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#c97fb0",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList0(){  List list = new ArrayList();  list.add(getListEnableAction());  list.add(getListDisableAction());  return list;}

public nc.ui.bd.defdoc.DefdocHierarchicalDataFactory getHierarchicalDataFactory(){
 if(context.get("hierarchicalDataFactory")!=null)
 return (nc.ui.bd.defdoc.DefdocHierarchicalDataFactory)context.get("hierarchicalDataFactory");
  nc.ui.bd.defdoc.DefdocHierarchicalDataFactory bean = new nc.ui.bd.defdoc.DefdocHierarchicalDataFactory();
  context.put("hierarchicalDataFactory",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.DefdocTreeCreateStrategy getTreeCreateStrategy(){
 if(context.get("treeCreateStrategy")!=null)
 return (nc.ui.bd.defdoc.DefdocTreeCreateStrategy)context.get("treeCreateStrategy");
  nc.ui.bd.defdoc.DefdocTreeCreateStrategy bean = new nc.ui.bd.defdoc.DefdocTreeCreateStrategy();
  context.put("treeCreateStrategy",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.DefdocTreecardAppModel getHAppModel(){
 if(context.get("HAppModel")!=null)
 return (nc.ui.bd.defdoc.DefdocTreecardAppModel)context.get("HAppModel");
  nc.ui.bd.defdoc.DefdocTreecardAppModel bean = new nc.ui.bd.defdoc.DefdocTreecardAppModel();
  context.put("HAppModel",bean);
  bean.setService(getAppModelService());
  bean.setTreeCreateStrategy(getTreeCreateStrategy());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
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
  bean.setNodeKeies(getManagedList1());
  bean.load();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add("treecard");  return list;}

public nc.ui.bd.defdoc.DefdocUserDefItemContainer getUserdefitemContainer(){
 if(context.get("userdefitemContainer")!=null)
 return (nc.ui.bd.defdoc.DefdocUserDefItemContainer)context.get("userdefitemContainer");
  nc.ui.bd.defdoc.DefdocUserDefItemContainer bean = new nc.ui.bd.defdoc.DefdocUserDefItemContainer();
  context.put("userdefitemContainer",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pub.beans.tree.BDObjectFilterByText getTreeFilter(){
 if(context.get("treeFilter")!=null)
 return (nc.ui.pub.beans.tree.BDObjectFilterByText)context.get("treeFilter");
  nc.ui.pub.beans.tree.BDObjectFilterByText bean = new nc.ui.pub.beans.tree.BDObjectFilterByText();
  context.put("treeFilter",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.BDFilterTreeCellRenderer getBdTreeCellRenderer(){
 if(context.get("bdTreeCellRenderer")!=null)
 return (nc.ui.bd.pub.BDFilterTreeCellRenderer)context.get("bdTreeCellRenderer");
  nc.ui.bd.pub.BDFilterTreeCellRenderer bean = new nc.ui.bd.pub.BDFilterTreeCellRenderer();
  context.put("bdTreeCellRenderer",bean);
  bean.setContext(getContext());
  bean.setFilter(getTreeFilter());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.TreePanel getTreePanel(){
 if(context.get("treePanel")!=null)
 return (nc.ui.uif2.components.TreePanel)context.get("treePanel");
  nc.ui.uif2.components.TreePanel bean = new nc.ui.uif2.components.TreePanel();
  context.put("treePanel",bean);
  bean.setModel(getHAppModel());
  bean.setFilterByText(getTreeFilter());
  bean.setSearchMode("filter");
  bean.setTreeCellRenderer(getBdTreeCellRenderer());
  bean.init();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.value.BillCardPanelHeadVOValueAdapter getBillcardHeadValueManager(){
 if(context.get("billcardHeadValueManager")!=null)
 return (nc.ui.uif2.editor.value.BillCardPanelHeadVOValueAdapter)context.get("billcardHeadValueManager");
  nc.ui.uif2.editor.value.BillCardPanelHeadVOValueAdapter bean = new nc.ui.uif2.editor.value.BillCardPanelHeadVOValueAdapter();
  context.put("billcardHeadValueManager",bean);
  bean.setHeadVOName("nc.vo.bd.defdoc.DefdocVO");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.editor.DefdocTreecardEditor getBillFormEditor(){
 if(context.get("billFormEditor")!=null)
 return (nc.ui.bd.defdoc.editor.DefdocTreecardEditor)context.get("billFormEditor");
  nc.ui.bd.defdoc.editor.DefdocTreecardEditor bean = new nc.ui.bd.defdoc.editor.DefdocTreecardEditor();
  context.put("billFormEditor",bean);
  bean.setModel(getHAppModel());
  bean.setNodekey("treecard");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_de295a());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_de295a(){
 if(context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#de295a")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#de295a");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#de295a",bean);
  bean.setContainer(getUserdefitemContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.AddAction getTreecardAddAction(){
 if(context.get("treecardAddAction")!=null)
 return (nc.ui.uif2.actions.AddAction)context.get("treecardAddAction");
  nc.ui.uif2.actions.AddAction bean = new nc.ui.uif2.actions.AddAction();
  context.put("treecardAddAction",bean);
  bean.setModel(getHAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocTreecardEditAction getTreecardEditAction(){
 if(context.get("treecardEditAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocTreecardEditAction)context.get("treecardEditAction");
  nc.ui.bd.defdoc.action.DefdocTreecardEditAction bean = new nc.ui.bd.defdoc.action.DefdocTreecardEditAction();
  context.put("treecardEditAction",bean);
  bean.setModel(getHAppModel());
  bean.setMdOperateCode("edit");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocTreecardDeleteAction getTreecardDelAction(){
 if(context.get("treecardDelAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocTreecardDeleteAction)context.get("treecardDelAction");
  nc.ui.bd.defdoc.action.DefdocTreecardDeleteAction bean = new nc.ui.bd.defdoc.action.DefdocTreecardDeleteAction();
  context.put("treecardDelAction",bean);
  bean.setModel(getHAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setMdOperateCode("delete");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.SaveAction getTreecardSaveAction(){
 if(context.get("treecardSaveAction")!=null)
 return (nc.ui.uif2.actions.SaveAction)context.get("treecardSaveAction");
  nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
  context.put("treecardSaveAction",bean);
  bean.setModel(getHAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setValidationService(getSaveValidateService());
  bean.setInterceptor(getTreeCardFormulasInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.bs.uif2.validation.DefaultValidationService getSaveValidateService(){
 if(context.get("saveValidateService")!=null)
 return (nc.bs.uif2.validation.DefaultValidationService)context.get("saveValidateService");
  nc.bs.uif2.validation.DefaultValidationService bean = new nc.bs.uif2.validation.DefaultValidationService();
  context.put("saveValidateService",bean);
  bean.setValidators(getManagedList2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getNullValueValidator_7e03ee());  return list;}

private nc.bs.uif2.validation.NullValueValidator getNullValueValidator_7e03ee(){
 if(context.get("nc.bs.uif2.validation.NullValueValidator#7e03ee")!=null)
 return (nc.bs.uif2.validation.NullValueValidator)context.get("nc.bs.uif2.validation.NullValueValidator#7e03ee");
  nc.bs.uif2.validation.NullValueValidator bean = new nc.bs.uif2.validation.NullValueValidator(getMDAttributeNameGetter_16cde6a(),getManagedList3());  context.put("nc.bs.uif2.validation.NullValueValidator#7e03ee",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.bs.uif2.validation.MDAttributeNameGetter getMDAttributeNameGetter_16cde6a(){
 if(context.get("nc.bs.uif2.validation.MDAttributeNameGetter#16cde6a")!=null)
 return (nc.bs.uif2.validation.MDAttributeNameGetter)context.get("nc.bs.uif2.validation.MDAttributeNameGetter#16cde6a");
  nc.bs.uif2.validation.MDAttributeNameGetter bean = new nc.bs.uif2.validation.MDAttributeNameGetter("nc.vo.bd.defdoc.DefdocVO");  context.put("nc.bs.uif2.validation.MDAttributeNameGetter#16cde6a",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList3(){  List list = new ArrayList();  list.add("code");  list.add("name");  return list;}

public nc.ui.uif2.actions.SaveAddAction getTreecardSaveAddAction(){
 if(context.get("treecardSaveAddAction")!=null)
 return (nc.ui.uif2.actions.SaveAddAction)context.get("treecardSaveAddAction");
  nc.ui.uif2.actions.SaveAddAction bean = new nc.ui.uif2.actions.SaveAddAction();
  context.put("treecardSaveAddAction",bean);
  bean.setModel(getHAppModel());
  bean.setEditor(getBillFormEditor());
  bean.setAddAction(getTreecardAddAction());
  bean.setValidationService(getSaveValidateService());
  bean.setInterceptor(getTreeCardFormulasInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocTreecardCancelAction getTreecardCancelAction(){
 if(context.get("treecardCancelAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocTreecardCancelAction)context.get("treecardCancelAction");
  nc.ui.bd.defdoc.action.DefdocTreecardCancelAction bean = new nc.ui.bd.defdoc.action.DefdocTreecardCancelAction();
  context.put("treecardCancelAction",bean);
  bean.setModel(getHAppModel());
  bean.setEditor(getBillFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocTreecardDisableAction getTreecardDisableAction(){
 if(context.get("treecardDisableAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocTreecardDisableAction)context.get("treecardDisableAction");
  nc.ui.bd.defdoc.action.DefdocTreecardDisableAction bean = new nc.ui.bd.defdoc.action.DefdocTreecardDisableAction();
  context.put("treecardDisableAction",bean);
  bean.setModel(getHAppModel());
  bean.setMdOperateCode("disable");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocTreecardEnableAction getTreecardEnableAction(){
 if(context.get("treecardEnableAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocTreecardEnableAction)context.get("treecardEnableAction");
  nc.ui.bd.defdoc.action.DefdocTreecardEnableAction bean = new nc.ui.bd.defdoc.action.DefdocTreecardEnableAction();
  context.put("treecardEnableAction",bean);
  bean.setModel(getHAppModel());
  bean.setMdOperateCode("enable");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.TemplatePreviewAction getTreecardTemplatePrintPreviewAction(){
 if(context.get("treecardTemplatePrintPreviewAction")!=null)
 return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("treecardTemplatePrintPreviewAction");
  nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
  context.put("treecardTemplatePrintPreviewAction",bean);
  bean.setModel(getHAppModel());
  bean.setPrintDlgParentConatiner(getBillFormEditor());
  bean.setNodeKey("defdoc_tree");
  bean.setDatasource(getDatasource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.TemplatePrintAction getTreecardTemplatePrintAction(){
 if(context.get("treecardTemplatePrintAction")!=null)
 return (nc.ui.uif2.actions.TemplatePrintAction)context.get("treecardTemplatePrintAction");
  nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
  context.put("treecardTemplatePrintAction",bean);
  bean.setModel(getHAppModel());
  bean.setPrintDlgParentConatiner(getBillFormEditor());
  bean.setNodeKey("defdoc_tree");
  bean.setDatasource(getDatasource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.OutputAction getTreecardOutputAction(){
 if(context.get("treecardOutputAction")!=null)
 return (nc.ui.uif2.actions.OutputAction)context.get("treecardOutputAction");
  nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
  context.put("treecardOutputAction",bean);
  bean.setModel(getHAppModel());
  bean.setPrintDlgParentConatiner(getBillFormEditor());
  bean.setNodeKey("defdoc_tree");
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
  bean.setModel(getHAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getTreecardEnableGroupAction(){
 if(context.get("treecardEnableGroupAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("treecardEnableGroupAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("treecardEnableGroupAction",bean);
  bean.setCode("treeEnable");
  bean.setName(getI18nFB_134161a());
  bean.setActions(getManagedList4());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_134161a(){
 if(context.get("nc.ui.uif2.I18nFB#134161a")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#134161a");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#134161a",bean);  bean.setResDir("common");
  bean.setDefaultValue("启用");
  bean.setResId("UC001-0000111");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#134161a",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList4(){  List list = new ArrayList();  list.add(getTreecardEnableAction());  list.add(getTreecardDisableAction());  return list;}

public nc.funcnode.ui.action.GroupAction getTreecardPrintMenuAction(){
 if(context.get("treecardPrintMenuAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("treecardPrintMenuAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("treecardPrintMenuAction",bean);
  bean.setCode("treeprint");
  bean.setName("打印");
  bean.setActions(getManagedList5());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add(getTreecardTemplatePrintAction());  list.add(getTreecardTemplatePrintPreviewAction());  list.add(getTreecardOutputAction());  return list;}

public nc.ui.bd.defdoc.DefdocFuncNodeInitDataListener getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.bd.defdoc.DefdocFuncNodeInitDataListener)context.get("InitDataListener");
  nc.ui.bd.defdoc.DefdocFuncNodeInitDataListener bean = new nc.ui.bd.defdoc.DefdocFuncNodeInitDataListener();
  context.put("InitDataListener",bean);
  bean.setDatamanager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.DefdocSingleNodeDataManager getModelDataManager(){
 if(context.get("modelDataManager")!=null)
 return (nc.ui.bd.defdoc.DefdocSingleNodeDataManager)context.get("modelDataManager");
  nc.ui.bd.defdoc.DefdocSingleNodeDataManager bean = new nc.ui.bd.defdoc.DefdocSingleNodeDataManager();
  context.put("modelDataManager",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setContext(getContext());
  bean.setService(getAppModelService());
  bean.setPaginationModel(getPaginationModel());
  bean.setDelegator(getPaginationDelegator());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.BillBatchPaginationDelegator getPaginationDelegator(){
 if(context.get("paginationDelegator")!=null)
 return (nc.ui.bd.pub.BillBatchPaginationDelegator)context.get("paginationDelegator");
  nc.ui.bd.pub.BillBatchPaginationDelegator bean = new nc.ui.bd.pub.BillBatchPaginationDelegator();
  context.put("paginationDelegator",bean);
  bean.setPaginationModel(getPaginationModel());
  bean.setBillModel(getBatchBillTableModel());
  bean.setEditor(getList());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.pagination.PaginationModel getPaginationModel(){
 if(context.get("paginationModel")!=null)
 return (nc.ui.uif2.components.pagination.PaginationModel)context.get("paginationModel");
  nc.ui.uif2.components.pagination.PaginationModel bean = new nc.ui.uif2.components.pagination.PaginationModel();
  context.put("paginationModel",bean);
  bean.setPaginationQueryService(getAppModelService());
  bean.setPageSize(50);
  bean.init();
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

public nc.ui.bd.pub.BDAutoHideOrgPanel getOrgPanel(){
 if(context.get("orgPanel")!=null)
 return (nc.ui.bd.pub.BDAutoHideOrgPanel)context.get("orgPanel");
  nc.ui.bd.pub.BDAutoHideOrgPanel bean = new nc.ui.bd.pub.BDAutoHideOrgPanel();
  context.put("orgPanel",bean);
  bean.setLabelName(getI18nFB_972fa3());
  bean.setModel(getBatchBillTableModel());
  bean.setDataManager(getModelDataManager());
  bean.setPk_orgtype("CORPORGTYPE000000000");
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_972fa3(){
 if(context.get("nc.ui.uif2.I18nFB#972fa3")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#972fa3");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#972fa3",bean);  bean.setResDir("common");
  bean.setDefaultValue("业务单元");
  bean.setResId("2UC000-000011");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#972fa3",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.defdoc.action.DefdocBatchQueryAction getListQueryAction(){
 if(context.get("listQueryAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocBatchQueryAction)context.get("listQueryAction");
  nc.ui.bd.defdoc.action.DefdocBatchQueryAction bean = new nc.ui.bd.defdoc.action.DefdocBatchQueryAction();
  context.put("listQueryAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.tools.BDQueryActionMediator getBdqueryActionMediator(){
 if(context.get("bdqueryActionMediator")!=null)
 return (nc.ui.bd.pub.tools.BDQueryActionMediator)context.get("bdqueryActionMediator");
  nc.ui.bd.pub.tools.BDQueryActionMediator bean = new nc.ui.bd.pub.tools.BDQueryActionMediator();
  context.put("bdqueryActionMediator",bean);
  bean.setQueryAction(getListQueryAction());
  bean.setOrgFieldCode(getManagedList6());
  bean.process();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add("pk_org");  return list;}

public nc.ui.uif2.actions.RefreshAction getListRefreshAction(){
 if(context.get("listRefreshAction")!=null)
 return (nc.ui.uif2.actions.RefreshAction)context.get("listRefreshAction");
  nc.ui.uif2.actions.RefreshAction bean = new nc.ui.uif2.actions.RefreshAction();
  context.put("listRefreshAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.ShowDisableDataAction getListShowSealAction(){
 if(context.get("listShowSealAction")!=null)
 return (nc.ui.uif2.actions.ShowDisableDataAction)context.get("listShowSealAction");
  nc.ui.uif2.actions.ShowDisableDataAction bean = new nc.ui.uif2.actions.ShowDisableDataAction();
  context.put("listShowSealAction",bean);
  bean.setModel(getHAppModel());
  bean.setDataManager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getListFilterMenuAction(){
 if(context.get("listFilterMenuAction")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("listFilterMenuAction");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("listFilterMenuAction",bean);
  bean.setCode("filter");
  bean.setName(getI18nFB_1b876a2());
  bean.setActions(getManagedList7());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1b876a2(){
 if(context.get("nc.ui.uif2.I18nFB#1b876a2")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1b876a2");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1b876a2",bean);  bean.setResDir("common");
  bean.setDefaultValue("过滤");
  bean.setResId("UCH069");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1b876a2",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList7(){  List list = new ArrayList();  list.add(getListShowSealAction());  return list;}

public nc.ui.bd.defdoc.action.DefdocBatchPaginationPreviewAction getListPrintPreviewAction(){
 if(context.get("listPrintPreviewAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocBatchPaginationPreviewAction)context.get("listPrintPreviewAction");
  nc.ui.bd.defdoc.action.DefdocBatchPaginationPreviewAction bean = new nc.ui.bd.defdoc.action.DefdocBatchPaginationPreviewAction();
  context.put("listPrintPreviewAction",bean);
  bean.setPrintAction(getListPrintAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.action.DefdocBatchPaginationPrintAction getListPrintAction(){
 if(context.get("listPrintAction")!=null)
 return (nc.ui.bd.defdoc.action.DefdocBatchPaginationPrintAction)context.get("listPrintAction");
  nc.ui.bd.defdoc.action.DefdocBatchPaginationPrintAction bean = new nc.ui.bd.defdoc.action.DefdocBatchPaginationPrintAction();
  context.put("listPrintAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setPrintDlgParentConatiner(getList());
  bean.setNodeKey("defdoc_list");
  bean.setPaginationModel(getPaginationModel());
  bean.setPrintFactory(getPrintFactory());
  bean.setDatamanager(getModelDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDPaginationOutputAction getListOutputAction(){
 if(context.get("listOutputAction")!=null)
 return (nc.ui.bd.pub.actions.print.BDPaginationOutputAction)context.get("listOutputAction");
  nc.ui.bd.pub.actions.print.BDPaginationOutputAction bean = new nc.ui.bd.pub.actions.print.BDPaginationOutputAction();
  context.put("listOutputAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setPrintDlgParentConatiner(getList());
  bean.setNodeKey("defdoc_list");
  bean.setPaginationModel(getPaginationModel());
  bean.setPrintFactory(getPrintFactory());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.defdoc.DefdocPagePrintFactory getPrintFactory(){
 if(context.get("printFactory")!=null)
 return (nc.ui.bd.defdoc.DefdocPagePrintFactory)context.get("printFactory");
  nc.ui.bd.defdoc.DefdocPagePrintFactory bean = new nc.ui.bd.defdoc.DefdocPagePrintFactory();
  context.put("printFactory",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getListPrintMenuAction(){
 if(context.get("listPrintMenuAction")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("listPrintMenuAction");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("listPrintMenuAction",bean);
  bean.setCode("listprint");
  bean.setName(getI18nFB_174a970());
  bean.setActions(getManagedList8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_174a970(){
 if(context.get("nc.ui.uif2.I18nFB#174a970")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#174a970");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#174a970",bean);  bean.setResDir("common");
  bean.setDefaultValue("打印");
  bean.setResId("UC001-0000007");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#174a970",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList8(){  List list = new ArrayList();  list.add(getListPrintAction());  list.add(getListPrintPreviewAction());  list.add(getListOutputAction());  return list;}

public nc.ui.bd.defdoc.editor.DefdocSplitPanel getListContainerWithPaginationBar(){
 if(context.get("listContainerWithPaginationBar")!=null)
 return (nc.ui.bd.defdoc.editor.DefdocSplitPanel)context.get("listContainerWithPaginationBar");
  nc.ui.bd.defdoc.editor.DefdocSplitPanel bean = new nc.ui.bd.defdoc.editor.DefdocSplitPanel();
  context.put("listContainerWithPaginationBar",bean);
  bean.setComponent1(getList());
  bean.setComponent2(getPaginationBar());
  bean.setDivideLocation(30);
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayoutRoot(getVSNode_5741cd());
  bean.setActions(getManagedList9());
  bean.setEditActions(getManagedList10());
  bean.setModel(getBatchBillTableModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_5741cd(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#5741cd")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#5741cd");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#5741cd",bean);
  bean.setUp(getCNode_2021d7());
  bean.setDown(getCNode_18dcc2b());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_2021d7(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#2021d7")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#2021d7");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#2021d7",bean);
  bean.setComponent(getOrgPanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_18dcc2b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#18dcc2b")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#18dcc2b");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#18dcc2b",bean);
  bean.setComponent(getListContainerWithPaginationBar());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList9(){  List list = new ArrayList();  list.add(getListAddAction());  list.add(getListEditAction());  list.add(getListDelAction());  list.add(getSeperatorAction());  list.add(getListFilterMenuAction());  list.add(getListQueryAction());  list.add(getListRefreshAction());  list.add(getSeperatorAction());  list.add(getListEnableGroupAction());  list.add(getSeperatorAction());  list.add(getListPrintMenuAction());  list.add(getSeperatorAction());  list.add(getMdmAction());  return list;}

private List getManagedList10(){  List list = new ArrayList();  list.add(getListAddAction());  list.add(getListDelAction());  list.add(getSeperatorAction());  list.add(getListSaveAction());  list.add(getSeperatorAction());  list.add(getListCancelAction());  list.add(getSeperatorAction());  return list;}

public nc.ui.archives.action.ProducterMdmAction getMdmAction(){
 if(context.get("mdmAction")!=null)
 return (nc.ui.archives.action.ProducterMdmAction)context.get("mdmAction");
  nc.ui.archives.action.ProducterMdmAction bean = new nc.ui.archives.action.ProducterMdmAction();
  context.put("mdmAction",bean);
  bean.setModel(getBatchBillTableModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
