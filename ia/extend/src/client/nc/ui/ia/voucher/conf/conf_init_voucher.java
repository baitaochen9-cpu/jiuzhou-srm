package nc.ui.ia.voucher.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class conf_init_voucher extends AbstractJavaBeanDefinition{
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

public nc.ui.ia.voucher.model.ModelService getModelService(){
 if(context.get("modelService")!=null)
 return (nc.ui.ia.voucher.model.ModelService)context.get("modelService");
  nc.ui.ia.voucher.model.ModelService bean = new nc.ui.ia.voucher.model.ModelService();
  context.put("modelService",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.BDObjectAdpaterFactory getBoadatorfactory(){
 if(context.get("boadatorfactory")!=null)
 return (nc.vo.bd.meta.BDObjectAdpaterFactory)context.get("boadatorfactory");
  nc.vo.bd.meta.BDObjectAdpaterFactory bean = new nc.vo.bd.meta.BDObjectAdpaterFactory();
  context.put("boadatorfactory",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.model.DefaultBatchValidationService getBatchValidateSerice(){
 if(context.get("batchValidateSerice")!=null)
 return (nc.ui.uif2.model.DefaultBatchValidationService)context.get("batchValidateSerice");
  nc.ui.uif2.model.DefaultBatchValidationService bean = new nc.ui.uif2.model.DefaultBatchValidationService();
  context.put("batchValidateSerice",bean);
  bean.setEditor(getQueryResultUI());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.voucher.model.BatchTableModel getBatchBillTableModel(){
 if(context.get("batchBillTableModel")!=null)
 return (nc.ui.ia.voucher.model.BatchTableModel)context.get("batchBillTableModel");
  nc.ui.ia.voucher.model.BatchTableModel bean = new nc.ui.ia.voucher.model.BatchTableModel();
  context.put("batchBillTableModel",bean);
  bean.setContext(getContext());
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
  bean.setService(getModelService());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.bill.base.maintain.view.IABodyVOValueAdapter getComponentValueManager(){
 if(context.get("componentValueManager")!=null)
 return (nc.ui.ia.bill.base.maintain.view.IABodyVOValueAdapter)context.get("componentValueManager");
  nc.ui.ia.bill.base.maintain.view.IABodyVOValueAdapter bean = new nc.ui.ia.bill.base.maintain.view.IABodyVOValueAdapter();
  context.put("componentValueManager",bean);
  bean.setBodyVOName("nc.vo.ia.detailledger.view.ia.AuditDisplayVO");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.voucher.view.VoucherUI getQueryResultUI(){
 if(context.get("queryResultUI")!=null)
 return (nc.ui.ia.voucher.view.VoucherUI)context.get("queryResultUI");
  nc.ui.ia.voucher.view.VoucherUI bean = new nc.ui.ia.voucher.view.VoucherUI();
  context.put("queryResultUI",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setComponentValueManager(getComponentValueManager());
  bean.setVoClassName("nc.vo.ia.detailledger.view.ia.AuditDisplayVO");
  bean.setIsBodyAutoAddLine(false);
  bean.setBodyMultiSelectEnable(true);
  bean.setUserdefitemPreparator(getUserdefitemPreparatorForIA());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getUserdefitemPreparatorForIA(){
 if(context.get("userdefitemPreparatorForIA")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare)context.get("userdefitemPreparatorForIA");
  nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
  context.put("userdefitemPreparatorForIA",bean);
  bean.setBillDataPrepares(getManagedList0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList0(){  List list = new ArrayList();  list.add(getUserdefitemPreparator());  list.add(getMarAsstPreparator());  return list;}

public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer(){
 if(context.get("userdefitemContainer")!=null)
 return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
  nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
  context.put("userdefitemContainer",bean);
  bean.setContext(getContext());
  bean.setParams(getManagedList1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add(getQueryParam_194172f());  list.add(getQueryParam_17e3673());  list.add(getQueryParam_128d374());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_194172f(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#194172f")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#194172f");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#194172f",bean);
  bean.setMdfullname(getHeadpath());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_17e3673(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#17e3673")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#17e3673");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#17e3673",bean);
  bean.setMdfullname(getItempath());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_128d374(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#128d374")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#128d374");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#128d374",bean);
  bean.setRulecode("materialassistant");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator getMarAsstPreparator(){
 if(context.get("marAsstPreparator")!=null)
 return (nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator)context.get("marAsstPreparator");
  nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator bean = new nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator();
  context.put("marAsstPreparator",bean);
  bean.setModel(getBatchBillTableModel());
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

public nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemPreparator(){
 if(context.get("userdefitemPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("userdefitemPreparator");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("userdefitemPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getUserdefQueryParam_1529eb6());  list.add(getUserdefQueryParam_131d6ff());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1529eb6(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#1529eb6")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1529eb6");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#1529eb6",bean);
  bean.setMdfullname(getHeadpath());
  bean.setPos(0);
  bean.setPrefix("vdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_131d6ff(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#131d6ff")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#131d6ff");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#131d6ff",bean);
  bean.setMdfullname(getItempath());
  bean.setPos(1);
  bean.setPrefix("vbdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.lang.String getHeadpath(){
 if(context.get("headpath")!=null)
 return (java.lang.String)context.get("headpath");
  java.lang.String bean = new java.lang.String("ia.ia_i4bill");  context.put("headpath",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.lang.String getItempath(){
 if(context.get("itempath")!=null)
 return (java.lang.String)context.get("itempath");
  java.lang.String bean = new java.lang.String("ia.ia_i4bill_b");  context.put("itempath",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.voucher.action.QueryAction getQueryAction(){
 if(context.get("queryAction")!=null)
 return (nc.ui.ia.voucher.action.QueryAction)context.get("queryAction");
  nc.ui.ia.voucher.action.QueryAction bean = new nc.ui.ia.voucher.action.QueryAction();
  context.put("queryAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setDataManager(getModelDataManager());
  bean.setBillTable(getQueryResultUI());
  bean.setQryCondDLGInitializer(getQryCondDLGInitializer());
  bean.setShowUpComponent(getQueryResultUI());
  bean.setTemplateContainer(getQueryTemplateContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.voucher.action.CreateHzPzAction getCreateHzPz(){
 if(context.get("CreateHzPz")!=null)
 return (nc.ui.ia.voucher.action.CreateHzPzAction)context.get("CreateHzPz");
  nc.ui.ia.voucher.action.CreateHzPzAction bean = new nc.ui.ia.voucher.action.CreateHzPzAction();
  context.put("CreateHzPz",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setService(getModelService());
  bean.setBillTable(getQueryResultUI());
  bean.setCode("begin");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.voucher.view.VoucherQryDLGInitializer getQryCondDLGInitializer(){
 if(context.get("qryCondDLGInitializer")!=null)
 return (nc.ui.ia.voucher.view.VoucherQryDLGInitializer)context.get("qryCondDLGInitializer");
  nc.ui.ia.voucher.view.VoucherQryDLGInitializer bean = new nc.ui.ia.voucher.view.VoucherQryDLGInitializer();
  context.put("qryCondDLGInitializer",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.voucher.action.CreateAction getCreateRtAction(){
 if(context.get("createRtAction")!=null)
 return (nc.ui.ia.voucher.action.CreateAction)context.get("createRtAction");
  nc.ui.ia.voucher.action.CreateAction bean = new nc.ui.ia.voucher.action.CreateAction();
  context.put("createRtAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setService(getModelService());
  bean.setBillTable(getQueryResultUI());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.voucher.action.DeleteAction getCancelRtAction(){
 if(context.get("cancelRtAction")!=null)
 return (nc.ui.ia.voucher.action.DeleteAction)context.get("cancelRtAction");
  nc.ui.ia.voucher.action.DeleteAction bean = new nc.ui.ia.voucher.action.DeleteAction();
  context.put("cancelRtAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setService(getModelService());
  bean.setBillTable(getQueryResultUI());
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

public nc.ui.ia.voucher.action.PrintAction getPrintAction(){
 if(context.get("printAction")!=null)
 return (nc.ui.ia.voucher.action.PrintAction)context.get("printAction");
  nc.ui.ia.voucher.action.PrintAction bean = new nc.ui.ia.voucher.action.PrintAction();
  context.put("printAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setQueryUI(getQueryResultUI());
  bean.setBeforePrintDataProcess(getPrintDataProcess());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.voucher.action.PrintAction getPreViewAction(){
 if(context.get("preViewAction")!=null)
 return (nc.ui.ia.voucher.action.PrintAction)context.get("preViewAction");
  nc.ui.ia.voucher.action.PrintAction bean = new nc.ui.ia.voucher.action.PrintAction();
  context.put("preViewAction",bean);
  bean.setQueryUI(getQueryResultUI());
  bean.setModel(getBatchBillTableModel());
  bean.setPreview(true);
  bean.setBeforePrintDataProcess(getPrintDataProcess());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintGroup(){
 if(context.get("printGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printGroup",bean);
  bean.setCode("printGroup");
  bean.setActions(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList3(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreViewAction());  list.add(getOutputAction());  list.add(getSeparatorAction());  return list;}

public nc.ui.pubapp.uif2app.actions.OutputAction getOutputAction(){
 if(context.get("outputAction")!=null)
 return (nc.ui.pubapp.uif2app.actions.OutputAction)context.get("outputAction");
  nc.ui.pubapp.uif2app.actions.OutputAction bean = new nc.ui.pubapp.uif2app.actions.OutputAction();
  context.put("outputAction",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setParent(getQueryResultUI());
  bean.setBeforePrintDataProcess(getPrintDataProcess());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.ia.pub.view.BillBeforePrintDataProcess getPrintDataProcess(){
 if(context.get("printDataProcess")!=null)
 return (nc.ui.ia.pub.view.BillBeforePrintDataProcess)context.get("printDataProcess");
  nc.ui.ia.pub.view.BillBeforePrintDataProcess bean = new nc.ui.ia.pub.view.BillBeforePrintDataProcess();
  context.put("printDataProcess",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setTable(getQueryResultUI());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayoutRoot(getCNode_1c86bd9());
  bean.setActions(getManagedList4());
  bean.setModel(getBatchBillTableModel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1c86bd9(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1c86bd9")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1c86bd9");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1c86bd9",bean);
  bean.setComponent(getQueryResultUI());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList4(){  List list = new ArrayList();  list.add(getQueryAction());  list.add(getSeparatorAction());  list.add(getCreateRtAction());  list.add(getCancelRtAction());  list.add(getSeparatorAction());  list.add(getPrintGroup());  list.add(getSeparatorAction());  list.add(getCreateHzPz());  return list;}

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

public nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller getRemoteCallCombinatorCaller(){
 if(context.get("remoteCallCombinatorCaller")!=null)
 return (nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller)context.get("remoteCallCombinatorCaller");
  nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller bean = new nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller();
  context.put("remoteCallCombinatorCaller",bean);
  bean.setRemoteCallers(getManagedList5());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add(getQueryTemplateContainer());  list.add(getTemplateContainer());  return list;}

public nc.ui.ia.voucher.view.FIPSrcQueryInitDataProcessor getFipSrcQueryInitDataProcessor(){
 if(context.get("fipSrcQueryInitDataProcessor")!=null)
 return (nc.ui.ia.voucher.view.FIPSrcQueryInitDataProcessor)context.get("fipSrcQueryInitDataProcessor");
  nc.ui.ia.voucher.view.FIPSrcQueryInitDataProcessor bean = new nc.ui.ia.voucher.view.FIPSrcQueryInitDataProcessor();
  context.put("fipSrcQueryInitDataProcessor",bean);
  bean.setListener(getInitDataListener());
  bean.setDisplayUI(getQueryResultUI());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener)context.get("InitDataListener");
  nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener bean = new nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener();
  context.put("InitDataListener",bean);
  bean.setModel(getBatchBillTableModel());
  bean.setQueryAction(getQueryAction());
  bean.setProcessorMap(getManagedMap0());
  bean.setVoClassName("nc.vo.ia.detailledger.view.ia.AuditDisplayVO");
  bean.setAutoShowUpComponent(getQueryResultUI());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("3",getFipSrcQueryInitDataProcessor());  return map;}

}
