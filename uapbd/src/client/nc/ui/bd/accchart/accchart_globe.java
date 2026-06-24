package nc.ui.bd.accchart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class accchart_globe extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.bd.account.model.AccQueryManageModel getAccquerymodel(){
 if(context.get("accquerymodel")!=null)
 return (nc.ui.bd.account.model.AccQueryManageModel)context.get("accquerymodel");
  nc.ui.bd.account.model.AccQueryManageModel bean = new nc.ui.bd.account.model.AccQueryManageModel();
  context.put("accquerymodel",bean);
  bean.setContext(getContext());
  bean.setAccChartEnv(getAccChartEnv());
  bean.setService(getAccountappmodelServicer());
  bean.setBusinessObjectAdapterFactory(getOadaptorfactory());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.model.QueryAccountModelDataManager getAccQueryDataManager(){
 if(context.get("accQueryDataManager")!=null)
 return (nc.ui.bd.account.model.QueryAccountModelDataManager)context.get("accQueryDataManager");
  nc.ui.bd.account.model.QueryAccountModelDataManager bean = new nc.ui.bd.account.model.QueryAccountModelDataManager();
  context.put("accQueryDataManager",bean);
  bean.setAccQueryModel(getAccquerymodel());
  bean.setAccChartEnv(getAccChartEnv());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccchartVersionInterceptor getQuery_accchartVersionInteceptor(){
 if(context.get("query_accchartVersionInteceptor")!=null)
 return (nc.ui.bd.account.actions.AccchartVersionInterceptor)context.get("query_accchartVersionInteceptor");
  nc.ui.bd.account.actions.AccchartVersionInterceptor bean = new nc.ui.bd.account.actions.AccchartVersionInterceptor();
  context.put("query_accchartVersionInteceptor",bean);
  bean.setContext(getContext());
  bean.setAccChartEnv(getAccChartEnv());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.SelectAccchartInterceptor getQuery_selectAccchartInteceptor(){
 if(context.get("query_selectAccchartInteceptor")!=null)
 return (nc.ui.bd.account.actions.SelectAccchartInterceptor)context.get("query_selectAccchartInteceptor");
  nc.ui.bd.account.actions.SelectAccchartInterceptor bean = new nc.ui.bd.account.actions.SelectAccchartInterceptor();
  context.put("query_selectAccchartInteceptor",bean);
  bean.setAppModel(getCharthappmodel());
  bean.setAccChartEnv(getAccChartEnv());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccEditAction getQuery_accedit_action(){
 if(context.get("query_accedit_action")!=null)
 return (nc.ui.bd.account.actions.AccEditAction)context.get("query_accedit_action");
  nc.ui.bd.account.actions.AccEditAction bean = new nc.ui.bd.account.actions.AccEditAction();
  context.put("query_accedit_action",bean);
  bean.setModel(getAccquerymodel());
  bean.setInterceptor(getQuery_accchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccDelAction getQuery_accdelete_action(){
 if(context.get("query_accdelete_action")!=null)
 return (nc.ui.bd.account.actions.AccDelAction)context.get("query_accdelete_action");
  nc.ui.bd.account.actions.AccDelAction bean = new nc.ui.bd.account.actions.AccDelAction();
  context.put("query_accdelete_action",bean);
  bean.setModel(getAccquerymodel());
  bean.setInterceptor(getQuery_accchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccRefreshSingleAction getQuery_accsinglerefresh_action(){
 if(context.get("query_accsinglerefresh_action")!=null)
 return (nc.ui.bd.account.actions.AccRefreshSingleAction)context.get("query_accsinglerefresh_action");
  nc.ui.bd.account.actions.AccRefreshSingleAction bean = new nc.ui.bd.account.actions.AccRefreshSingleAction();
  context.put("query_accsinglerefresh_action",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccQueryRefreshAction getQuery_refresh_action(){
 if(context.get("query_refresh_action")!=null)
 return (nc.ui.bd.account.actions.AccQueryRefreshAction)context.get("query_refresh_action");
  nc.ui.bd.account.actions.AccQueryRefreshAction bean = new nc.ui.bd.account.actions.AccQueryRefreshAction();
  context.put("query_refresh_action",bean);
  bean.setModel(getAccquerymodel());
  bean.setDataManager(getAccQueryDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccQueryReturnAction getQuery_return_action(){
 if(context.get("query_return_action")!=null)
 return (nc.ui.bd.account.actions.AccQueryReturnAction)context.get("query_return_action");
  nc.ui.bd.account.actions.AccQueryReturnAction bean = new nc.ui.bd.account.actions.AccQueryReturnAction();
  context.put("query_return_action",bean);
  bean.setAccListViewer(getAcclistview());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getQuery_exportinout_actions(){
 if(context.get("query_exportinout_actions")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("query_exportinout_actions");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("query_exportinout_actions",bean);
  bean.setCode("exportinout");
  bean.setName(getI18nFB_172b0c4());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_172b0c4(){
 if(context.get("nc.ui.uif2.I18nFB#172b0c4")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#172b0c4");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#172b0c4",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("导入导出");
  bean.setResId("010140accb0317");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#172b0c4",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.funcnode.ui.action.GroupAction getQuery_print_actions(){
 if(context.get("query_print_actions")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("query_print_actions");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("query_print_actions",bean);
  bean.setCode("print");
  bean.setName(getI18nFB_b32e3f());
  bean.setActions(getManagedList0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_b32e3f(){
 if(context.get("nc.ui.uif2.I18nFB#b32e3f")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#b32e3f");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#b32e3f",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("打印");
  bean.setResId("010140accb0320");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#b32e3f",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList0(){  List list = new ArrayList();  list.add(getQuery_template_print_action());  list.add(getQuery_template_preview_action());  list.add(getQuery_template_output_action());  return list;}

public nc.ui.uif2.actions.PrintPreviewAction getQuery_periview_action(){
 if(context.get("query_periview_action")!=null)
 return (nc.ui.uif2.actions.PrintPreviewAction)context.get("query_periview_action");
  nc.ui.uif2.actions.PrintPreviewAction bean = new nc.ui.uif2.actions.PrintPreviewAction();
  context.put("query_periview_action",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.PrintAction getQuery_print_action(){
 if(context.get("query_print_action")!=null)
 return (nc.ui.uif2.actions.PrintAction)context.get("query_print_action");
  nc.ui.uif2.actions.PrintAction bean = new nc.ui.uif2.actions.PrintAction();
  context.put("query_print_action",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDTemplatePreviewAction getQuery_template_preview_action(){
 if(context.get("query_template_preview_action")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePreviewAction)context.get("query_template_preview_action");
  nc.ui.bd.pub.actions.print.BDTemplatePreviewAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePreviewAction();
  context.put("query_template_preview_action",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodeKey("listtemp");
  bean.setDatasource(getQuerylistDataSource());
  bean.setPrintDlgParentConatiner(getAccquerylistviewer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.TemplatePrintAction getQuery_template_print_action(){
 if(context.get("query_template_print_action")!=null)
 return (nc.ui.uif2.actions.TemplatePrintAction)context.get("query_template_print_action");
  nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
  context.put("query_template_print_action",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodeKey("listtemp");
  bean.setDatasource(getQuerylistDataSource());
  bean.setPrintDlgParentConatiner(getAccquerylistviewer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.OutputAction getQuery_template_output_action(){
 if(context.get("query_template_output_action")!=null)
 return (nc.ui.uif2.actions.OutputAction)context.get("query_template_output_action");
  nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
  context.put("query_template_output_action",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodeKey("listtemp");
  bean.setDatasource(getQuerylistDataSource());
  bean.setPrintDlgParentConatiner(getAccquerylistviewer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getQuerylistDataSource(){
 if(context.get("querylistDataSource")!=null)
 return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("querylistDataSource");
  nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
  context.put("querylistDataSource",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.ShowDisableDataAction getQuery_filteraction(){
 if(context.get("query_filteraction")!=null)
 return (nc.ui.uif2.actions.ShowDisableDataAction)context.get("query_filteraction");
  nc.ui.uif2.actions.ShowDisableDataAction bean = new nc.ui.uif2.actions.ShowDisableDataAction();
  context.put("query_filteraction",bean);
  bean.setModel(getAccquerymodel());
  bean.setDataManager(getAccQueryDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getQuery_accfilteraction(){
 if(context.get("query_accfilteraction")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("query_accfilteraction");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("query_accfilteraction",bean);
  bean.setCode("accfilter");
  bean.setName(getI18nFB_770cca());
  bean.setActions(getManagedList1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_770cca(){
 if(context.get("nc.ui.uif2.I18nFB#770cca")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#770cca");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#770cca",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("过滤");
  bean.setResId("010140accb0315");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#770cca",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList1(){  List list = new ArrayList();  list.add(getQuery_filteraction());  return list;}

public nc.ui.bd.account.actions.AccountSaveAction getQuery_accsaveaction(){
 if(context.get("query_accsaveaction")!=null)
 return (nc.ui.bd.account.actions.AccountSaveAction)context.get("query_accsaveaction");
  nc.ui.bd.account.actions.AccountSaveAction bean = new nc.ui.bd.account.actions.AccountSaveAction();
  context.put("query_accsaveaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setEditor(getQuery_acceditor());
  bean.setValidationService(getValidator());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.CancelAction getQuery_acccancelaction(){
 if(context.get("query_acccancelaction")!=null)
 return (nc.ui.uif2.actions.CancelAction)context.get("query_acccancelaction");
  nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
  context.put("query_acccancelaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setEditor(getQuery_acceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.IncurflagSetAction getQuery_insurorientaction(){
 if(context.get("query_insurorientaction")!=null)
 return (nc.ui.bd.account.actions.IncurflagSetAction)context.get("query_insurorientaction");
  nc.ui.bd.account.actions.IncurflagSetAction bean = new nc.ui.bd.account.actions.IncurflagSetAction();
  context.put("query_insurorientaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setInterceptor(getQuery_accchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.BalanFlagAction getQuery_balanorientaction(){
 if(context.get("query_balanorientaction")!=null)
 return (nc.ui.bd.account.actions.BalanFlagAction)context.get("query_balanorientaction");
  nc.ui.bd.account.actions.BalanFlagAction bean = new nc.ui.bd.account.actions.BalanFlagAction();
  context.put("query_balanorientaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setInterceptor(getQuery_accchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.OutflagSetAction getQuery_outflagaction(){
 if(context.get("query_outflagaction")!=null)
 return (nc.ui.bd.account.actions.OutflagSetAction)context.get("query_outflagaction");
  nc.ui.bd.account.actions.OutflagSetAction bean = new nc.ui.bd.account.actions.OutflagSetAction();
  context.put("query_outflagaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setInterceptor(getQuery_accchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AllowCloseAccountAction getQuery_allowcloseaccaction(){
 if(context.get("query_allowcloseaccaction")!=null)
 return (nc.ui.bd.account.actions.AllowCloseAccountAction)context.get("query_allowcloseaccaction");
  nc.ui.bd.account.actions.AllowCloseAccountAction bean = new nc.ui.bd.account.actions.AllowCloseAccountAction();
  context.put("query_allowcloseaccaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setAccChartEnv(getAccChartEnv());
  bean.setInterceptor(getQuery_accchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccountBatchEnableAction getQuery_accenablection(){
 if(context.get("query_accenablection")!=null)
 return (nc.ui.bd.account.actions.AccountBatchEnableAction)context.get("query_accenablection");
  nc.ui.bd.account.actions.AccountBatchEnableAction bean = new nc.ui.bd.account.actions.AccountBatchEnableAction();
  context.put("query_accenablection",bean);
  bean.setModel(getAccquerymodel());
  bean.setInterceptor(getQuery_accchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccountBatchDisableAction getQuery_accstopaction(){
 if(context.get("query_accstopaction")!=null)
 return (nc.ui.bd.account.actions.AccountBatchDisableAction)context.get("query_accstopaction");
  nc.ui.bd.account.actions.AccountBatchDisableAction bean = new nc.ui.bd.account.actions.AccountBatchDisableAction();
  context.put("query_accstopaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setInterceptor(getQuery_accchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getQuery_accenablegroup(){
 if(context.get("query_accenablegroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("query_accenablegroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("query_accenablegroup",bean);
  bean.setActions(getManagedList2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getQuery_accenablection());  list.add(getQuery_accstopaction());  return list;}

public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getQuery_cardDataSource(){
 if(context.get("query_cardDataSource")!=null)
 return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("query_cardDataSource");
  nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
  context.put("query_cardDataSource",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDTemplatePreviewAction getQuery_templatepreview(){
 if(context.get("query_templatepreview")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePreviewAction)context.get("query_templatepreview");
  nc.ui.bd.pub.actions.print.BDTemplatePreviewAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePreviewAction();
  context.put("query_templatepreview",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodeKey("cardtemp");
  bean.setDatasource(getQuery_cardDataSource());
  bean.setPrintDlgParentConatiner(getQuery_acceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDTemplatePrintAction getQuery_templateprint(){
 if(context.get("query_templateprint")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePrintAction)context.get("query_templateprint");
  nc.ui.bd.pub.actions.print.BDTemplatePrintAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePrintAction();
  context.put("query_templateprint",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodeKey("cardtemp");
  bean.setDatasource(getQuery_cardDataSource());
  bean.setPrintDlgParentConatiner(getQuery_acceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.OutputAction getQuery_outputAction(){
 if(context.get("query_outputAction")!=null)
 return (nc.ui.uif2.actions.OutputAction)context.get("query_outputAction");
  nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
  context.put("query_outputAction",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodeKey("cardtemp");
  bean.setDatasource(getQuery_cardDataSource());
  bean.setPrintDlgParentConatiner(getQuery_acceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getQuery_printgroup(){
 if(context.get("query_printgroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("query_printgroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("query_printgroup",bean);
  bean.setCode("print");
  bean.setName(getI18nFB_1e8c7f7());
  bean.setActions(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1e8c7f7(){
 if(context.get("nc.ui.uif2.I18nFB#1e8c7f7")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1e8c7f7");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1e8c7f7",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("打印");
  bean.setResId("010140accb0320");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1e8c7f7",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList3(){  List list = new ArrayList();  list.add(getQuery_templatepreview());  list.add(getQuery_templateprint());  list.add(getQuery_outputAction());  return list;}

public nc.ui.bd.account.actions.AccAssAddLineAction getQuery_assaddlineaction(){
 if(context.get("query_assaddlineaction")!=null)
 return (nc.ui.bd.account.actions.AccAssAddLineAction)context.get("query_assaddlineaction");
  nc.ui.bd.account.actions.AccAssAddLineAction bean = new nc.ui.bd.account.actions.AccAssAddLineAction();
  context.put("query_assaddlineaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setCardpanel(getQuery_accasspanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccAssDelLineAction getQuery_assdellineaction(){
 if(context.get("query_assdellineaction")!=null)
 return (nc.ui.bd.account.actions.AccAssDelLineAction)context.get("query_assdellineaction");
  nc.ui.bd.account.actions.AccAssDelLineAction bean = new nc.ui.bd.account.actions.AccAssDelLineAction();
  context.put("query_assdellineaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setCardpanel(getQuery_accasspanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AssUpAction getQuery_assupaction(){
 if(context.get("query_assupaction")!=null)
 return (nc.ui.bd.account.actions.AssUpAction)context.get("query_assupaction");
  nc.ui.bd.account.actions.AssUpAction bean = new nc.ui.bd.account.actions.AssUpAction();
  context.put("query_assupaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setBillCard(getQuery_accasspanel());
  bean.setExceptionHandler(getExhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AssDownAction getQuery_assdownaction(){
 if(context.get("query_assdownaction")!=null)
 return (nc.ui.bd.account.actions.AssDownAction)context.get("query_assdownaction");
  nc.ui.bd.account.actions.AssDownAction bean = new nc.ui.bd.account.actions.AssDownAction();
  context.put("query_assdownaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setBillCard(getQuery_accasspanel());
  bean.setExceptionHandler(getExhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccCtrlMdleAddAction getQuery_ctrlmdleaddaction(){
 if(context.get("query_ctrlmdleaddaction")!=null)
 return (nc.ui.bd.account.actions.AccCtrlMdleAddAction)context.get("query_ctrlmdleaddaction");
  nc.ui.bd.account.actions.AccCtrlMdleAddAction bean = new nc.ui.bd.account.actions.AccCtrlMdleAddAction();
  context.put("query_ctrlmdleaddaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setCardpanel(getQuery_accctrlmdlpanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccCtrlMdleDelAction getQuery_ctrlmdledelaction(){
 if(context.get("query_ctrlmdledelaction")!=null)
 return (nc.ui.bd.account.actions.AccCtrlMdleDelAction)context.get("query_ctrlmdledelaction");
  nc.ui.bd.account.actions.AccCtrlMdleDelAction bean = new nc.ui.bd.account.actions.AccCtrlMdleDelAction();
  context.put("query_ctrlmdledelaction",bean);
  bean.setModel(getAccquerymodel());
  bean.setCardpanel(getQuery_accctrlmdlpanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getQryinfopnl(){
 if(context.get("qryinfopnl")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("qryinfopnl");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("qryinfopnl",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getAccqueryactions(){
 if(context.get("accqueryactions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("accqueryactions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getAccquerylistviewer());  context.put("accqueryactions",bean);
  bean.setActions(getManagedList4());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList4(){  List list = new ArrayList();  list.add(getQuery_accedit_action());  list.add(getQuery_accdelete_action());  list.add(getNullAction());  list.add(getAccqryaction());  list.add(getQuery_refresh_action());  list.add(getQuery_accfilteraction());  list.add(getNullAction());  list.add(getQuery_accenablegroup());  list.add(getNullAction());  list.add(getQuery_exportinout_actions());  list.add(getQuery_print_actions());  list.add(getNullAction());  list.add(getQryReturnAction());  return list;}

public nc.ui.uif2.actions.ShowMeUpAction getQryReturnAction(){
 if(context.get("qryReturnAction")!=null)
 return (nc.ui.uif2.actions.ShowMeUpAction)context.get("qryReturnAction");
  nc.ui.uif2.actions.ShowMeUpAction bean = new nc.ui.uif2.actions.ShowMeUpAction();
  context.put("qryReturnAction",bean);
  bean.setGoComponent(getAcclistview());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.view.AccQueryListViewer getAccquerylistviewer(){
 if(context.get("accquerylistviewer")!=null)
 return (nc.ui.bd.account.view.AccQueryListViewer)context.get("accquerylistviewer");
  nc.ui.bd.account.view.AccQueryListViewer bean = new nc.ui.bd.account.view.AccQueryListViewer();
  context.put("accquerylistviewer",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodekey("account");
  bean.setMultiSelectionEnable(true);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setUserdefitemListPreparator(getUserdefitemListPreparator());
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
  bean.setParams(getManagedList16());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add(getListUserdefitemQueryParam());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getListUserdefitemQueryParam(){
 if(context.get("listUserdefitemQueryParam")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("listUserdefitemQueryParam");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("listUserdefitemQueryParam",bean);
  bean.setMdfullname("uap.accasoa");
  bean.setPos(0);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getQueryCardInfoPnl(){
 if(context.get("queryCardInfoPnl")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("queryCardInfoPnl");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("queryCardInfoPnl",bean);
  bean.setModel(getAccquerymodel());
  bean.setActions(getManagedList6());
  bean.setTitleAction(getShowMeUpAction_19547a2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add(getFirstLineAction_143ee59());  list.add(getPreLineAction_d52518());  list.add(getNextLineAction_1765d8c());  list.add(getLastLineAction_1f7a58a());  return list;}

private nc.ui.uif2.actions.FirstLineAction getFirstLineAction_143ee59(){
 if(context.get("nc.ui.uif2.actions.FirstLineAction#143ee59")!=null)
 return (nc.ui.uif2.actions.FirstLineAction)context.get("nc.ui.uif2.actions.FirstLineAction#143ee59");
  nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
  context.put("nc.ui.uif2.actions.FirstLineAction#143ee59",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.PreLineAction getPreLineAction_d52518(){
 if(context.get("nc.ui.uif2.actions.PreLineAction#d52518")!=null)
 return (nc.ui.uif2.actions.PreLineAction)context.get("nc.ui.uif2.actions.PreLineAction#d52518");
  nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
  context.put("nc.ui.uif2.actions.PreLineAction#d52518",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.NextLineAction getNextLineAction_1765d8c(){
 if(context.get("nc.ui.uif2.actions.NextLineAction#1765d8c")!=null)
 return (nc.ui.uif2.actions.NextLineAction)context.get("nc.ui.uif2.actions.NextLineAction#1765d8c");
  nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
  context.put("nc.ui.uif2.actions.NextLineAction#1765d8c",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.LastLineAction getLastLineAction_1f7a58a(){
 if(context.get("nc.ui.uif2.actions.LastLineAction#1f7a58a")!=null)
 return (nc.ui.uif2.actions.LastLineAction)context.get("nc.ui.uif2.actions.LastLineAction#1f7a58a");
  nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
  context.put("nc.ui.uif2.actions.LastLineAction#1f7a58a",bean);
  bean.setModel(getAccquerymodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.ShowMeUpAction getShowMeUpAction_19547a2(){
 if(context.get("nc.ui.uif2.actions.ShowMeUpAction#19547a2")!=null)
 return (nc.ui.uif2.actions.ShowMeUpAction)context.get("nc.ui.uif2.actions.ShowMeUpAction#19547a2");
  nc.ui.uif2.actions.ShowMeUpAction bean = new nc.ui.uif2.actions.ShowMeUpAction();
  context.put("nc.ui.uif2.actions.ShowMeUpAction#19547a2",bean);
  bean.setGoComponent(getAccquerylistviewer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.view.AccountEditor getQuery_acceditor(){
 if(context.get("query_acceditor")!=null)
 return (nc.ui.bd.account.view.AccountEditor)context.get("query_acceditor");
  nc.ui.bd.account.view.AccountEditor bean = new nc.ui.bd.account.view.AccountEditor();
  context.put("query_acceditor",bean);
  bean.setModel(getAccquerymodel());
  bean.setAccChartEnv(getAccChartEnv());
  bean.setNodekey("account");
  bean.setAssListViewer(getQuery_accasspanel());
  bean.setCtrlMdlListViewer(getQuery_accctrlmdlpanel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setClosingListener(getClosingListener());
  bean.setExceptionHandler(getExhandler());
  bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_191e3a5());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_191e3a5(){
 if(context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#191e3a5")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#191e3a5");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#191e3a5",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList7());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList7(){  List list = new ArrayList();  list.add(getAccountCardUserdefitemQueryParam());  list.add(getAccsubCardUserdefitemQueryParam());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getAccountCardUserdefitemQueryParam(){
 if(context.get("accountCardUserdefitemQueryParam")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("accountCardUserdefitemQueryParam");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("accountCardUserdefitemQueryParam",bean);
  bean.setMdfullname("uap.accasoa");
  bean.setPos(0);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getAccsubCardUserdefitemQueryParam(){
 if(context.get("accsubCardUserdefitemQueryParam")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("accsubCardUserdefitemQueryParam");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("accsubCardUserdefitemQueryParam",bean);
  bean.setMdfullname("uap.accasoa");
  bean.setPos(1);
  bean.setPrefix("def");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.view.AccAssListViewer getQuery_accasspanel(){
 if(context.get("query_accasspanel")!=null)
 return (nc.ui.bd.account.view.AccAssListViewer)context.get("query_accasspanel");
  nc.ui.bd.account.view.AccAssListViewer bean = new nc.ui.bd.account.view.AccAssListViewer();
  context.put("query_accasspanel",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodekey("accass");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setActions(getManagedList8());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getQuery_assaddlineaction());  list.add(getQuery_assdellineaction());  list.add(getQuery_assupaction());  list.add(getQuery_assdownaction());  return list;}

public nc.ui.bd.account.view.AccCtrlMdlListViewer getQuery_accctrlmdlpanel(){
 if(context.get("query_accctrlmdlpanel")!=null)
 return (nc.ui.bd.account.view.AccCtrlMdlListViewer)context.get("query_accctrlmdlpanel");
  nc.ui.bd.account.view.AccCtrlMdlListViewer bean = new nc.ui.bd.account.view.AccCtrlMdlListViewer();
  context.put("query_accctrlmdlpanel",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodekey("accctrlmdl");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setActions(getManagedList9());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList9(){  List list = new ArrayList();  list.add(getQuery_ctrlmdleaddaction());  list.add(getQuery_ctrlmdledelaction());  return list;}

public nc.ui.uif2.editor.BillForm getQuery_auditinfopanel(){
 if(context.get("query_auditinfopanel")!=null)
 return (nc.ui.uif2.editor.BillForm)context.get("query_auditinfopanel");
  nc.ui.uif2.editor.BillForm bean = new nc.ui.uif2.editor.BillForm();
  context.put("query_auditinfopanel",bean);
  bean.setModel(getAccquerymodel());
  bean.setNodekey("auditinfo");
  bean.setTemplateContainer(getTemplateContainer());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getQuerycardactions(){
 if(context.get("querycardactions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("querycardactions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getQuery_acceditor());  context.put("querycardactions",bean);
  bean.setModel(getAccquerymodel());
  bean.setEditActions(getManagedList10());
  bean.setActions(getManagedList11());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList10(){  List list = new ArrayList();  list.add(getQuery_accsaveaction());  list.add(getNullAction());  list.add(getQuery_acccancelaction());  return list;}

private List getManagedList11(){  List list = new ArrayList();  list.add(getQuery_accedit_action());  list.add(getQuery_accdelete_action());  list.add(getNullAction());  list.add(getAccqryaction());  list.add(getQuery_accsinglerefresh_action());  list.add(getQuery_accfilteraction());  list.add(getNullAction());  list.add(getQuery_accenablegroup());  list.add(getNullAction());  list.add(getQuery_printgroup());  return list;}

public nc.vo.uif2.LoginContext getContext(){
 if(context.get("context")!=null)
 return (nc.vo.uif2.LoginContext)context.get("context");
  nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
  context.put("context",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.model.CurrentAccChartEnv getAccChartEnv(){
 if(context.get("accChartEnv")!=null)
 return (nc.ui.bd.account.model.CurrentAccChartEnv)context.get("accChartEnv");
  nc.ui.bd.account.model.CurrentAccChartEnv bean = new nc.ui.bd.account.model.CurrentAccChartEnv();
  context.put("accChartEnv",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.model.AccChartModelServicer getService(){
 if(context.get("service")!=null)
 return (nc.ui.bd.accchart.model.AccChartModelServicer)context.get("service");
  nc.ui.bd.accchart.model.AccChartModelServicer bean = new nc.ui.bd.accchart.model.AccChartModelServicer();
  context.put("service",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.model.AccountModelServicer getAccountappmodelServicer(){
 if(context.get("accountappmodelServicer")!=null)
 return (nc.ui.bd.account.model.AccountModelServicer)context.get("accountappmodelServicer");
  nc.ui.bd.account.model.AccountModelServicer bean = new nc.ui.bd.account.model.AccountModelServicer();
  context.put("accountappmodelServicer",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccDistActionsManager getDistactionsmediator(){
 if(context.get("distactionsmediator")!=null)
 return (nc.ui.bd.account.actions.AccDistActionsManager)context.get("distactionsmediator");
  nc.ui.bd.account.actions.AccDistActionsManager bean = new nc.ui.bd.account.actions.AccDistActionsManager();
  context.put("distactionsmediator",bean);
  bean.setContext(getContext());
  bean.setModel(getAccountappmodel());
  bean.setDownAction(getBDDistResourceDownAction());
  bean.setReclaimAction(getBDDistTokenReclaimAction());
  bean.setUploadAction(getBDDistResourceUploadAction());
  bean.setStatusAction(getDistResourceStatusAction());
  bean.setDistMenuAction(getDistactionmenu());
  bean.setActionContainer(getListactions());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.BDObjectAdpaterFactory getOadaptorfactory(){
 if(context.get("oadaptorfactory")!=null)
 return (nc.vo.bd.meta.BDObjectAdpaterFactory)context.get("oadaptorfactory");
  nc.vo.bd.meta.BDObjectAdpaterFactory bean = new nc.vo.bd.meta.BDObjectAdpaterFactory();
  context.put("oadaptorfactory",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.model.AccChartCreatTreeStrategy getTreeCreateStrategy(){
 if(context.get("treeCreateStrategy")!=null)
 return (nc.ui.bd.accchart.model.AccChartCreatTreeStrategy)context.get("treeCreateStrategy");
  nc.ui.bd.accchart.model.AccChartCreatTreeStrategy bean = new nc.ui.bd.accchart.model.AccChartCreatTreeStrategy();
  context.put("treeCreateStrategy",bean);
  bean.setFactory(getOadaptorfactory());
  bean.setRootName(getI18nFB_17f440b());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_17f440b(){
 if(context.get("nc.ui.uif2.I18nFB#17f440b")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#17f440b");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#17f440b",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("科目表");
  bean.setResId("010140accb0026");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#17f440b",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.account.model.AccBodyDataRepeatValidator getValidator(){
 if(context.get("validator")!=null)
 return (nc.ui.bd.account.model.AccBodyDataRepeatValidator)context.get("validator");
  nc.ui.bd.account.model.AccBodyDataRepeatValidator bean = new nc.ui.bd.account.model.AccBodyDataRepeatValidator();
  context.put("validator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.model.AccChartAppModel getCharthappmodel(){
 if(context.get("charthappmodel")!=null)
 return (nc.ui.bd.accchart.model.AccChartAppModel)context.get("charthappmodel");
  nc.ui.bd.accchart.model.AccChartAppModel bean = new nc.ui.bd.accchart.model.AccChartAppModel();
  context.put("charthappmodel",bean);
  bean.setBusinessObjectAdapterFactory(getOadaptorfactory());
  bean.setTreeCreateStrategy(getTreeCreateStrategy());
  bean.setContext(getContext());
  bean.setService(getService());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.model.AccountAppModel getAccountappmodel(){
 if(context.get("accountappmodel")!=null)
 return (nc.ui.bd.account.model.AccountAppModel)context.get("accountappmodel");
  nc.ui.bd.account.model.AccountAppModel bean = new nc.ui.bd.account.model.AccountAppModel();
  context.put("accountappmodel",bean);
  bean.setBusinessObjectAdapterFactory(getOadaptorfactory());
  bean.setContext(getContext());
  bean.setAccChartEnv(getAccChartEnv());
  bean.setService(getAccountappmodelServicer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.model.AccChartDataManager getModelDataManager(){
 if(context.get("modelDataManager")!=null)
 return (nc.ui.bd.accchart.model.AccChartDataManager)context.get("modelDataManager");
  nc.ui.bd.accchart.model.AccChartDataManager bean = new nc.ui.bd.accchart.model.AccChartDataManager();
  context.put("modelDataManager",bean);
  bean.setModel(getCharthappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.model.AccountModelDataManager getAccDataManager(){
 if(context.get("accDataManager")!=null)
 return (nc.ui.bd.account.model.AccountModelDataManager)context.get("accDataManager");
  nc.ui.bd.account.model.AccountModelDataManager bean = new nc.ui.bd.account.model.AccountModelDataManager();
  context.put("accDataManager",bean);
  bean.setModel(getAccountappmodel());
  bean.setAccQueryModel(getAccquerymodel());
  bean.setAccChartEnv(getAccChartEnv());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.FunNodeClosingHandler getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.uif2.FunNodeClosingHandler)context.get("ClosingListener");
  nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
  context.put("ClosingListener",bean);
  bean.setModel(getAccountappmodel());
  bean.setSaveaction(getAccsaveaction());
  bean.setCancelaction(getAcccancelaction());
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
  bean.setNodeKeies(getManagedList12());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList12(){  List list = new ArrayList();  list.add("account");  list.add("accass");  list.add("accctrlmdl");  list.add("auditinfo");  list.add("accchart");  list.add("notnullattr");  list.add("insertmidacc");  list.add("qaccass");  list.add("qassacc");  return list;}

public nc.ui.uif2.editor.QueryTemplateContainer getQueryTemplateContianer(){
 if(context.get("queryTemplateContianer")!=null)
 return (nc.ui.uif2.editor.QueryTemplateContainer)context.get("queryTemplateContianer");
  nc.ui.uif2.editor.QueryTemplateContainer bean = new nc.ui.uif2.editor.QueryTemplateContainer();
  context.put("queryTemplateContianer",bean);
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
  bean.setRemoteCallers(getManagedList13());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList13(){  List list = new ArrayList();  list.add(getTemplateContainer());  list.add(getQueryTemplateContianer());  list.add(getUserdefitemContainer());  return list;}

public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer(){
 if(context.get("userdefitemContainer")!=null)
 return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
  nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
  context.put("userdefitemContainer",bean);
  bean.setContext(getContext());
  bean.setParams(getManagedList14());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList14(){  List list = new ArrayList();  list.add(getQueryParam_4c4375());  list.add(getQueryParam_e0e8bd());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_4c4375(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#4c4375")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#4c4375");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#4c4375",bean);
  bean.setMdfullname("uap.accasoa");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_e0e8bd(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#e0e8bd")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#e0e8bd");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#e0e8bd",bean);
  bean.setMdfullname("uap.accasoa");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getCardInfoPnl(){
 if(context.get("cardInfoPnl")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("cardInfoPnl");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("cardInfoPnl",bean);
  bean.setActions(getManagedList15());
  bean.setTitleAction(getShowMeUpAction_1d01058());
  bean.setModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList15(){  List list = new ArrayList();  list.add(getFirstLineAction());  list.add(getPreLineAction());  list.add(getNextLineAction());  list.add(getLastLineAction());  return list;}

private nc.ui.bd.account.actions.AccFirstLineAction getFirstLineAction(){
 if(context.get("firstLineAction")!=null)
 return (nc.ui.bd.account.actions.AccFirstLineAction)context.get("firstLineAction");
  nc.ui.bd.account.actions.AccFirstLineAction bean = new nc.ui.bd.account.actions.AccFirstLineAction();
  context.put("firstLineAction",bean);
  bean.setAppModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.bd.account.actions.AccPreLineAction getPreLineAction(){
 if(context.get("preLineAction")!=null)
 return (nc.ui.bd.account.actions.AccPreLineAction)context.get("preLineAction");
  nc.ui.bd.account.actions.AccPreLineAction bean = new nc.ui.bd.account.actions.AccPreLineAction();
  context.put("preLineAction",bean);
  bean.setAppModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.bd.account.actions.AccNextLineAction getNextLineAction(){
 if(context.get("nextLineAction")!=null)
 return (nc.ui.bd.account.actions.AccNextLineAction)context.get("nextLineAction");
  nc.ui.bd.account.actions.AccNextLineAction bean = new nc.ui.bd.account.actions.AccNextLineAction();
  context.put("nextLineAction",bean);
  bean.setAppModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.bd.account.actions.AccLastLineAction getLastLineAction(){
 if(context.get("lastLineAction")!=null)
 return (nc.ui.bd.account.actions.AccLastLineAction)context.get("lastLineAction");
  nc.ui.bd.account.actions.AccLastLineAction bean = new nc.ui.bd.account.actions.AccLastLineAction();
  context.put("lastLineAction",bean);
  bean.setAppModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.actions.ShowMeUpAction getShowMeUpAction_1d01058(){
 if(context.get("nc.ui.uif2.actions.ShowMeUpAction#1d01058")!=null)
 return (nc.ui.uif2.actions.ShowMeUpAction)context.get("nc.ui.uif2.actions.ShowMeUpAction#1d01058");
  nc.ui.uif2.actions.ShowMeUpAction bean = new nc.ui.uif2.actions.ShowMeUpAction();
  context.put("nc.ui.uif2.actions.ShowMeUpAction#1d01058",bean);
  bean.setGoComponent(getAcclistview());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.view.AccChartTopPane getToppane(){
 if(context.get("toppane")!=null)
 return (nc.ui.bd.accchart.view.AccChartTopPane)context.get("toppane");
  nc.ui.bd.accchart.view.AccChartTopPane bean = new nc.ui.bd.accchart.view.AccChartTopPane();
  context.put("toppane",bean);
  bean.setChartAppModel(getCharthappmodel());
  bean.setAccountAppModel(getAccountappmodel());
  bean.setAccDataManager(getAccDataManager());
  bean.setRefreshAction(getChartrefreshaction());
  bean.setAccChartDataManager(getModelDataManager());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.view.AccountListViewer getAcclistview(){
 if(context.get("acclistview")!=null)
 return (nc.ui.bd.account.view.AccountListViewer)context.get("acclistview");
  nc.ui.bd.account.view.AccountListViewer bean = new nc.ui.bd.account.view.AccountListViewer();
  context.put("acclistview",bean);
  bean.setModel(getAccountappmodel());
  bean.setNodekey("account");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setUserdefitemListPreparator(getUserdefitemListPreparator());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList16(){  List list = new ArrayList();  list.add(getListUserdefitemQueryParam());  return list;}

public nc.ui.bd.account.view.AccountEditor getAcceditor(){
 if(context.get("acceditor")!=null)
 return (nc.ui.bd.account.view.AccountEditor)context.get("acceditor");
  nc.ui.bd.account.view.AccountEditor bean = new nc.ui.bd.account.view.AccountEditor();
  context.put("acceditor",bean);
  bean.setModel(getAccountappmodel());
  bean.setAccChartEnv(getAccChartEnv());
  bean.setNodekey("account");
  bean.setAssListViewer(getAccasspanel());
  bean.setCtrlMdlListViewer(getAccctrlmdlpanel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setClosingListener(getClosingListener());
  bean.setExceptionHandler(getExhandler());
  bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_e10a8a());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_e10a8a(){
 if(context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#e10a8a")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#e10a8a");
  nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
  context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#e10a8a",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList17());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList17(){  List list = new ArrayList();  list.add(getAccountCardUserdefitemQueryParam());  list.add(getAccsubCardUserdefitemQueryParam());  return list;}

public nc.ui.bd.account.view.AccAssListViewer getAccasspanel(){
 if(context.get("accasspanel")!=null)
 return (nc.ui.bd.account.view.AccAssListViewer)context.get("accasspanel");
  nc.ui.bd.account.view.AccAssListViewer bean = new nc.ui.bd.account.view.AccAssListViewer();
  context.put("accasspanel",bean);
  bean.setModel(getAccountappmodel());
  bean.setNodekey("accass");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setActions(getManagedList18());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList18(){  List list = new ArrayList();  list.add(getAssaddlineaction());  list.add(getAssdellineaction());  list.add(getAssupaction());  list.add(getAssdownaction());  return list;}

public nc.ui.bd.account.view.AccCtrlMdlListViewer getAccctrlmdlpanel(){
 if(context.get("accctrlmdlpanel")!=null)
 return (nc.ui.bd.account.view.AccCtrlMdlListViewer)context.get("accctrlmdlpanel");
  nc.ui.bd.account.view.AccCtrlMdlListViewer bean = new nc.ui.bd.account.view.AccCtrlMdlListViewer();
  context.put("accctrlmdlpanel",bean);
  bean.setModel(getAccountappmodel());
  bean.setNodekey("accctrlmdl");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setActions(getManagedList19());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList19(){  List list = new ArrayList();  list.add(getCtrlmdleaddaction());  list.add(getCtrlmdledelaction());  return list;}

public nc.ui.uif2.editor.BillForm getAuditinfopanel(){
 if(context.get("auditinfopanel")!=null)
 return (nc.ui.uif2.editor.BillForm)context.get("auditinfopanel");
  nc.ui.uif2.editor.BillForm bean = new nc.ui.uif2.editor.BillForm();
  context.put("auditinfopanel",bean);
  bean.setModel(getAccountappmodel());
  bean.setNodekey("auditinfo");
  bean.setTemplateContainer(getTemplateContainer());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.view.InsertMidAccEditor getInsertmidacceditor(){
 if(context.get("insertmidacceditor")!=null)
 return (nc.ui.bd.account.view.InsertMidAccEditor)context.get("insertmidacceditor");
  nc.ui.bd.account.view.InsertMidAccEditor bean = new nc.ui.bd.account.view.InsertMidAccEditor();
  context.put("insertmidacceditor",bean);
  bean.setNodekey("insertmidacc");
  bean.setTemplateContainer(getTemplateContainer());
  bean.setModel(getCharthappmodel());
  bean.setExceptionHandler(getExhandler());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.view.QuickSetAccAssHeadEditor getQassnortheditor(){
 if(context.get("qassnortheditor")!=null)
 return (nc.ui.bd.account.view.QuickSetAccAssHeadEditor)context.get("qassnortheditor");
  nc.ui.bd.account.view.QuickSetAccAssHeadEditor bean = new nc.ui.bd.account.view.QuickSetAccAssHeadEditor();
  context.put("qassnortheditor",bean);
  bean.setModel(getAccountappmodel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("qaccass");
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.view.QuickSetAccAssBodyEditor getQasscentereditor(){
 if(context.get("qasscentereditor")!=null)
 return (nc.ui.bd.account.view.QuickSetAccAssBodyEditor)context.get("qasscentereditor");
  nc.ui.bd.account.view.QuickSetAccAssBodyEditor bean = new nc.ui.bd.account.view.QuickSetAccAssBodyEditor();
  context.put("qasscentereditor",bean);
  bean.setModel(getAccountappmodel());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("qassacc");
  bean.setBodyActionMap(getManagedMap0());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("account",getManagedList20());  return map;}

private List getManagedList20(){  List list = new ArrayList();  list.add(getAccallselect());  list.add(getAccallnotselect());  return list;}

public nc.ui.bd.account.view.QuickSetAssEditor getQaccasseditor(){
 if(context.get("qaccasseditor")!=null)
 return (nc.ui.bd.account.view.QuickSetAssEditor)context.get("qaccasseditor");
  nc.ui.bd.account.view.QuickSetAssEditor bean = new nc.ui.bd.account.view.QuickSetAssEditor();
  context.put("qaccasseditor",bean);
  bean.setNorthEditor(getQassnortheditor());
  bean.setCenterEditor(getQasscentereditor());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.view.AccchartNewVersionEditor getNewversioneditor(){
 if(context.get("newversioneditor")!=null)
 return (nc.ui.bd.accchart.view.AccchartNewVersionEditor)context.get("newversioneditor");
  nc.ui.bd.accchart.view.AccchartNewVersionEditor bean = new nc.ui.bd.accchart.view.AccchartNewVersionEditor();
  context.put("newversioneditor",bean);
  bean.setModel(getCharthappmodel());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccountAddAction getAccaddaction(){
 if(context.get("accaddaction")!=null)
 return (nc.ui.bd.account.actions.AccountAddAction)context.get("accaddaction");
  nc.ui.bd.account.actions.AccountAddAction bean = new nc.ui.bd.account.actions.AccountAddAction();
  context.put("accaddaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setAccChartEnv(getAccChartEnv());
  bean.setInterceptor(getSelectAccchartInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccEditAction getAcceditaction(){
 if(context.get("acceditaction")!=null)
 return (nc.ui.bd.account.actions.AccEditAction)context.get("acceditaction");
  nc.ui.bd.account.actions.AccEditAction bean = new nc.ui.bd.account.actions.AccEditAction();
  context.put("acceditaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccDelAction getAccdelaction(){
 if(context.get("accdelaction")!=null)
 return (nc.ui.bd.account.actions.AccDelAction)context.get("accdelaction");
  nc.ui.bd.account.actions.AccDelAction bean = new nc.ui.bd.account.actions.AccDelAction();
  context.put("accdelaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.RefreshAction getAccrefreshaction(){
 if(context.get("accrefreshaction")!=null)
 return (nc.ui.uif2.actions.RefreshAction)context.get("accrefreshaction");
  nc.ui.uif2.actions.RefreshAction bean = new nc.ui.uif2.actions.RefreshAction();
  context.put("accrefreshaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setDataManager(getAccDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccRefreshSingleAction getAccsinglerefresh(){
 if(context.get("accsinglerefresh")!=null)
 return (nc.ui.bd.account.actions.AccRefreshSingleAction)context.get("accsinglerefresh");
  nc.ui.bd.account.actions.AccRefreshSingleAction bean = new nc.ui.bd.account.actions.AccRefreshSingleAction();
  context.put("accsinglerefresh",bean);
  bean.setModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.InsertMidClassAction getInsertmidaction(){
 if(context.get("insertmidaction")!=null)
 return (nc.ui.bd.account.actions.InsertMidClassAction)context.get("insertmidaction");
  nc.ui.bd.account.actions.InsertMidClassAction bean = new nc.ui.bd.account.actions.InsertMidClassAction();
  context.put("insertmidaction",bean);
  bean.setManageModel(getAccountappmodel());
  bean.setEditor(getInsertmidacceditor());
  bean.setDlgTitle(getI18nFB_dc8cd8());
  bean.setModel(getCharthappmodel());
  bean.setSaveAction(getInsertmidlevaction());
  bean.setExceptionHandler(getExhandler());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_dc8cd8(){
 if(context.get("nc.ui.uif2.I18nFB#dc8cd8")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#dc8cd8");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#dc8cd8",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("插入中间级科目");
  bean.setResId("010140accb0312");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#dc8cd8",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.account.actions.AccCtrlRuleAction getAccctrlruleaction(){
 if(context.get("accctrlruleaction")!=null)
 return (nc.ui.bd.account.actions.AccCtrlRuleAction)context.get("accctrlruleaction");
  nc.ui.bd.account.actions.AccCtrlRuleAction bean = new nc.ui.bd.account.actions.AccCtrlRuleAction();
  context.put("accctrlruleaction",bean);
  bean.setModel(getCharthappmodel());
  bean.setManageModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccountSaveAction getAccsaveaction(){
 if(context.get("accsaveaction")!=null)
 return (nc.ui.bd.account.actions.AccountSaveAction)context.get("accsaveaction");
  nc.ui.bd.account.actions.AccountSaveAction bean = new nc.ui.bd.account.actions.AccountSaveAction();
  context.put("accsaveaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setEditor(getAcceditor());
  bean.setValidationService(getValidator());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.CancelAction getAcccancelaction(){
 if(context.get("acccancelaction")!=null)
 return (nc.ui.uif2.actions.CancelAction)context.get("acccancelaction");
  nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
  context.put("acccancelaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setEditor(getAcceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccountSaveAddAction getAccsavenewaddaction(){
 if(context.get("accsavenewaddaction")!=null)
 return (nc.ui.bd.account.actions.AccountSaveAddAction)context.get("accsavenewaddaction");
  nc.ui.bd.account.actions.AccountSaveAddAction bean = new nc.ui.bd.account.actions.AccountSaveAddAction();
  context.put("accsavenewaddaction",bean);
  bean.setAddAction(getAccaddaction());
  bean.setModel(getAccountappmodel());
  bean.setEditor(getAcceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.SeparatorAction getNullAction(){
 if(context.get("nullAction")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("nullAction");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("nullAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccCopyAddAction getAcccopyaction(){
 if(context.get("acccopyaction")!=null)
 return (nc.ui.bd.account.actions.AccCopyAddAction)context.get("acccopyaction");
  nc.ui.bd.account.actions.AccCopyAddAction bean = new nc.ui.bd.account.actions.AccCopyAddAction();
  context.put("acccopyaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setEditor(getAcceditor());
  bean.setAccAssList(getAccasspanel());
  bean.setCtrlMdlList(getAccctrlmdlpanel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getAcceditactiongroup(){
 if(context.get("acceditactiongroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("acceditactiongroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("acceditactiongroup",bean);
  bean.setActions(getManagedList21());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList21(){  List list = new ArrayList();  list.add(getAcceditaction());  list.add(getBatchUpdateWizardAction());  return list;}

public nc.ui.bd.account.actions.AccountMapAction getAccmapaction(){
 if(context.get("accmapaction")!=null)
 return (nc.ui.bd.account.actions.AccountMapAction)context.get("accmapaction");
  nc.ui.bd.account.actions.AccountMapAction bean = new nc.ui.bd.account.actions.AccountMapAction();
  context.put("accmapaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setChartmodel(getCharthappmodel());
  bean.setFunnode("10140ACCMAP");
  bean.setRefreshAction(getChartrefreshaction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accbatchupdate.action.AccountBatchUpdateAction getBatchUpdateWizardAction(){
 if(context.get("batchUpdateWizardAction")!=null)
 return (nc.ui.bd.accbatchupdate.action.AccountBatchUpdateAction)context.get("batchUpdateWizardAction");
  nc.ui.bd.accbatchupdate.action.AccountBatchUpdateAction bean = new nc.ui.bd.accbatchupdate.action.AccountBatchUpdateAction();
  context.put("batchUpdateWizardAction",bean);
  bean.setDataManager(getAccDataManager());
  bean.setRefreshaction(getAccrefreshaction());
  bean.setModel(getAccountappmodel());
  bean.setTopPane(getToppane());
  bean.setAccChartEnv(getAccChartEnv());
  bean.setMdId("23a89307-5992-460e-95dd-c628c85f7f95");
  bean.setQryTempNodeKey("");
  bean.setBillTempNodekey("batchupdateacc");
  bean.setBillTemplatePkField("pk_accasoa");
  bean.setQryService(getAcountBatchQueryService_1a2bf80());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.bd.accbatchupdate.model.AcountBatchQueryService getAcountBatchQueryService_1a2bf80(){
 if(context.get("nc.ui.bd.accbatchupdate.model.AcountBatchQueryService#1a2bf80")!=null)
 return (nc.ui.bd.accbatchupdate.model.AcountBatchQueryService)context.get("nc.ui.bd.accbatchupdate.model.AcountBatchQueryService#1a2bf80");
  nc.ui.bd.accbatchupdate.model.AcountBatchQueryService bean = new nc.ui.bd.accbatchupdate.model.AcountBatchQueryService();
  context.put("nc.ui.bd.accbatchupdate.model.AcountBatchQueryService#1a2bf80",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getAccenablegroup(){
 if(context.get("accenablegroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("accenablegroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("accenablegroup",bean);
  bean.setActions(getManagedList22());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList22(){  List list = new ArrayList();  list.add(getAccenablection());  list.add(getAccstopaction());  return list;}

public nc.ui.bd.account.actions.AccountEnableAction getAccenablection(){
 if(context.get("accenablection")!=null)
 return (nc.ui.bd.account.actions.AccountEnableAction)context.get("accenablection");
  nc.ui.bd.account.actions.AccountEnableAction bean = new nc.ui.bd.account.actions.AccountEnableAction();
  context.put("accenablection",bean);
  bean.setModel(getAccountappmodel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccountDisableAction getAccstopaction(){
 if(context.get("accstopaction")!=null)
 return (nc.ui.bd.account.actions.AccountDisableAction)context.get("accstopaction");
  nc.ui.bd.account.actions.AccountDisableAction bean = new nc.ui.bd.account.actions.AccountDisableAction();
  context.put("accstopaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintgroup(){
 if(context.get("printgroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printgroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printgroup",bean);
  bean.setCode("print");
  bean.setName(getI18nFB_1ca9f9a());
  bean.setActions(getManagedList23());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1ca9f9a(){
 if(context.get("nc.ui.uif2.I18nFB#1ca9f9a")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1ca9f9a");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1ca9f9a",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("打印");
  bean.setResId("010140accb0320");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1ca9f9a",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList23(){  List list = new ArrayList();  list.add(getTemplateprint());  list.add(getTemplatepreview());  list.add(getOutputAction());  return list;}

public nc.funcnode.ui.action.GroupAction getListprintgroup(){
 if(context.get("listprintgroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("listprintgroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("listprintgroup",bean);
  bean.setCode("print");
  bean.setName(getI18nFB_1b46169());
  bean.setActions(getManagedList24());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1b46169(){
 if(context.get("nc.ui.uif2.I18nFB#1b46169")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1b46169");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1b46169",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("打印");
  bean.setResId("010140accb0320");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1b46169",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList24(){  List list = new ArrayList();  list.add(getListtemplateprint());  list.add(getListtemplatepreview());  list.add(getListoutputAction());  return list;}

public nc.funcnode.ui.action.MenuAction getDistactionmenu(){
 if(context.get("distactionmenu")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("distactionmenu");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("distactionmenu",bean);
  bean.setCode("dist");
  bean.setName(getI18nFB_1675c59());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1675c59(){
 if(context.get("nc.ui.uif2.I18nFB#1675c59")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1675c59");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1675c59",bean);  bean.setResDir("common");
  bean.setDefaultValue("分布式");
  bean.setResId("UC001-0000119");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1675c59",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.account.actions.AccDistTokenReclaimAction getBDDistTokenReclaimAction(){
 if(context.get("BDDistTokenReclaimAction")!=null)
 return (nc.ui.bd.account.actions.AccDistTokenReclaimAction)context.get("BDDistTokenReclaimAction");
  nc.ui.bd.account.actions.AccDistTokenReclaimAction bean = new nc.ui.bd.account.actions.AccDistTokenReclaimAction();
  context.put("BDDistTokenReclaimAction",bean);
  bean.setTokencode("accttoken");
  bean.setModel(getAccountappmodel());
  bean.setChartModel(getCharthappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccDistResourceUploadAction getBDDistResourceUploadAction(){
 if(context.get("BDDistResourceUploadAction")!=null)
 return (nc.ui.bd.account.actions.AccDistResourceUploadAction)context.get("BDDistResourceUploadAction");
  nc.ui.bd.account.actions.AccDistResourceUploadAction bean = new nc.ui.bd.account.actions.AccDistResourceUploadAction();
  context.put("BDDistResourceUploadAction",bean);
  bean.setTokencode("accttoken");
  bean.setModel(getAccountappmodel());
  bean.setChartModel(getCharthappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccDistResourceStatusAction getDistResourceStatusAction(){
 if(context.get("distResourceStatusAction")!=null)
 return (nc.ui.bd.account.actions.AccDistResourceStatusAction)context.get("distResourceStatusAction");
  nc.ui.bd.account.actions.AccDistResourceStatusAction bean = new nc.ui.bd.account.actions.AccDistResourceStatusAction();
  context.put("distResourceStatusAction",bean);
  bean.setTokencode("accttoken");
  bean.setModel(getAccountappmodel());
  bean.setChartModel(getCharthappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccDistResourceDownAction getBDDistResourceDownAction(){
 if(context.get("BDDistResourceDownAction")!=null)
 return (nc.ui.bd.account.actions.AccDistResourceDownAction)context.get("BDDistResourceDownAction");
  nc.ui.bd.account.actions.AccDistResourceDownAction bean = new nc.ui.bd.account.actions.AccDistResourceDownAction();
  context.put("BDDistResourceDownAction",bean);
  bean.setTokencode("accttoken");
  bean.setModel(getAccountappmodel());
  bean.setChartModel(getCharthappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.OutputAction getOutputAction(){
 if(context.get("outputAction")!=null)
 return (nc.ui.uif2.actions.OutputAction)context.get("outputAction");
  nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
  context.put("outputAction",bean);
  bean.setModel(getAccountappmodel());
  bean.setDatasource(getCardDataSource());
  bean.setNodeKey("cardtemp");
  bean.setPrintDlgParentConatiner(getAcceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.OutputAction getListoutputAction(){
 if(context.get("listoutputAction")!=null)
 return (nc.ui.uif2.actions.OutputAction)context.get("listoutputAction");
  nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
  context.put("listoutputAction",bean);
  bean.setModel(getAccountappmodel());
  bean.setDatasource(getListDataSource());
  bean.setNodeKey("listtemp");
  bean.setPrintDlgParentConatiner(getAcclistview());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDMultiPanelDirectPrintAction getPrintaction(){
 if(context.get("printaction")!=null)
 return (nc.ui.bd.pub.actions.print.BDMultiPanelDirectPrintAction)context.get("printaction");
  nc.ui.bd.pub.actions.print.BDMultiPanelDirectPrintAction bean = new nc.ui.bd.pub.actions.print.BDMultiPanelDirectPrintAction();
  context.put("printaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setHeadEditors(getManagedList25());
  bean.setBodyEditors(getManagedList26());
  bean.setTailEditor(getAuditinfopanel());
  bean.setTitle(getI18nFB_e01761());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList25(){  List list = new ArrayList();  list.add(getAcceditor());  return list;}

private List getManagedList26(){  List list = new ArrayList();  list.add(getAccasspanel());  list.add(getAccctrlmdlpanel());  return list;}

private java.lang.String getI18nFB_e01761(){
 if(context.get("nc.ui.uif2.I18nFB#e01761")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#e01761");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#e01761",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("会计科目");
  bean.setResId("010140accb0321");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#e01761",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.pub.actions.print.BDMultiPanelDirectPrintAction getPreviewaction(){
 if(context.get("previewaction")!=null)
 return (nc.ui.bd.pub.actions.print.BDMultiPanelDirectPrintAction)context.get("previewaction");
  nc.ui.bd.pub.actions.print.BDMultiPanelDirectPrintAction bean = new nc.ui.bd.pub.actions.print.BDMultiPanelDirectPrintAction();
  context.put("previewaction",bean);
  bean.setDirectPrint(false);
  bean.setModel(getAccountappmodel());
  bean.setHeadEditors(getManagedList27());
  bean.setBodyEditors(getManagedList28());
  bean.setTailEditor(getAuditinfopanel());
  bean.setTitle(getI18nFB_1ee7e1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList27(){  List list = new ArrayList();  list.add(getAcceditor());  return list;}

private List getManagedList28(){  List list = new ArrayList();  list.add(getAccasspanel());  list.add(getAccctrlmdlpanel());  return list;}

private java.lang.String getI18nFB_1ee7e1(){
 if(context.get("nc.ui.uif2.I18nFB#1ee7e1")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1ee7e1");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1ee7e1",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("会计科目");
  bean.setResId("010140accb0321");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1ee7e1",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.pub.actions.print.BDTemplatePreviewAction getTemplatepreview(){
 if(context.get("templatepreview")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePreviewAction)context.get("templatepreview");
  nc.ui.bd.pub.actions.print.BDTemplatePreviewAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePreviewAction();
  context.put("templatepreview",bean);
  bean.setModel(getAccountappmodel());
  bean.setNodeKey("cardtemp");
  bean.setPrintDlgParentConatiner(getAcceditor());
  bean.setDatasource(getCardDataSource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDTemplatePrintAction getTemplateprint(){
 if(context.get("templateprint")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePrintAction)context.get("templateprint");
  nc.ui.bd.pub.actions.print.BDTemplatePrintAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePrintAction();
  context.put("templateprint",bean);
  bean.setModel(getAccountappmodel());
  bean.setNodeKey("cardtemp");
  bean.setPrintDlgParentConatiner(getAcceditor());
  bean.setDatasource(getCardDataSource());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDListPanelDirectPrintAction getListprintaction(){
 if(context.get("listprintaction")!=null)
 return (nc.ui.bd.pub.actions.print.BDListPanelDirectPrintAction)context.get("listprintaction");
  nc.ui.bd.pub.actions.print.BDListPanelDirectPrintAction bean = new nc.ui.bd.pub.actions.print.BDListPanelDirectPrintAction();
  context.put("listprintaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setListView(getAcclistview());
  bean.setTitle(getI18nFB_1098bd2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1098bd2(){
 if(context.get("nc.ui.uif2.I18nFB#1098bd2")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1098bd2");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1098bd2",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("会计科目");
  bean.setResId("010140accb0321");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1098bd2",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.pub.actions.print.BDListPanelDirectPrintAction getListpreviewaction(){
 if(context.get("listpreviewaction")!=null)
 return (nc.ui.bd.pub.actions.print.BDListPanelDirectPrintAction)context.get("listpreviewaction");
  nc.ui.bd.pub.actions.print.BDListPanelDirectPrintAction bean = new nc.ui.bd.pub.actions.print.BDListPanelDirectPrintAction();
  context.put("listpreviewaction",bean);
  bean.setDirectPrint(false);
  bean.setModel(getAccountappmodel());
  bean.setListView(getAcclistview());
  bean.setTitle(getI18nFB_1ba8886());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1ba8886(){
 if(context.get("nc.ui.uif2.I18nFB#1ba8886")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1ba8886");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1ba8886",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("会计科目");
  bean.setResId("010140accb0321");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1ba8886",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.pub.actions.print.BDTemplatePreviewAction getListtemplatepreview(){
 if(context.get("listtemplatepreview")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePreviewAction)context.get("listtemplatepreview");
  nc.ui.bd.pub.actions.print.BDTemplatePreviewAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePreviewAction();
  context.put("listtemplatepreview",bean);
  bean.setModel(getAccountappmodel());
  bean.setNodeKey("listtemp");
  bean.setDatasource(getListDataSource());
  bean.setPrintDlgParentConatiner(getAcclistview());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.BDTemplatePrintAction getListtemplateprint(){
 if(context.get("listtemplateprint")!=null)
 return (nc.ui.bd.pub.actions.print.BDTemplatePrintAction)context.get("listtemplateprint");
  nc.ui.bd.pub.actions.print.BDTemplatePrintAction bean = new nc.ui.bd.pub.actions.print.BDTemplatePrintAction();
  context.put("listtemplateprint",bean);
  bean.setModel(getAccountappmodel());
  bean.setNodeKey("listtemp");
  bean.setDatasource(getListDataSource());
  bean.setPrintDlgParentConatiner(getAcclistview());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getListDataSource(){
 if(context.get("listDataSource")!=null)
 return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("listDataSource");
  nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
  context.put("listDataSource",bean);
  bean.setModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getCardDataSource(){
 if(context.get("cardDataSource")!=null)
 return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("cardDataSource");
  nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
  context.put("cardDataSource",bean);
  bean.setModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.SelectAccchartInterceptor getSelectAccchartInteceptor(){
 if(context.get("selectAccchartInteceptor")!=null)
 return (nc.ui.bd.account.actions.SelectAccchartInterceptor)context.get("selectAccchartInteceptor");
  nc.ui.bd.account.actions.SelectAccchartInterceptor bean = new nc.ui.bd.account.actions.SelectAccchartInterceptor();
  context.put("selectAccchartInteceptor",bean);
  bean.setAppModel(getCharthappmodel());
  bean.setAccChartEnv(getAccChartEnv());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccchartVersionInterceptor getAccchartVersionInteceptor(){
 if(context.get("accchartVersionInteceptor")!=null)
 return (nc.ui.bd.account.actions.AccchartVersionInterceptor)context.get("accchartVersionInteceptor");
  nc.ui.bd.account.actions.AccchartVersionInterceptor bean = new nc.ui.bd.account.actions.AccchartVersionInterceptor();
  context.put("accchartVersionInteceptor",bean);
  bean.setContext(getContext());
  bean.setAccChartEnv(getAccChartEnv());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.QuickSetAssAction getQuicksetassaction(){
 if(context.get("quicksetassaction")!=null)
 return (nc.ui.bd.account.actions.QuickSetAssAction)context.get("quicksetassaction");
  nc.ui.bd.account.actions.QuickSetAssAction bean = new nc.ui.bd.account.actions.QuickSetAssAction();
  context.put("quicksetassaction",bean);
  bean.setManageModel(getAccountappmodel());
  bean.setEditor(getQaccasseditor());
  bean.setDlgTitle(getI18nFB_159229c());
  bean.setModel(getCharthappmodel());
  bean.setSaveAction(getQuicksetasssaveaction());
  bean.setCancelAction(getQuicksetasscancelaction());
  bean.setExceptionHandler(getExhandler());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_159229c(){
 if(context.get("nc.ui.uif2.I18nFB#159229c")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#159229c");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#159229c",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("快速设置辅助核算");
  bean.setResId("010140accb0082");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#159229c",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.account.actions.IncurflagSetAction getInsurorientaction(){
 if(context.get("insurorientaction")!=null)
 return (nc.ui.bd.account.actions.IncurflagSetAction)context.get("insurorientaction");
  nc.ui.bd.account.actions.IncurflagSetAction bean = new nc.ui.bd.account.actions.IncurflagSetAction();
  context.put("insurorientaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.BalanFlagAction getBalanorientaction(){
 if(context.get("balanorientaction")!=null)
 return (nc.ui.bd.account.actions.BalanFlagAction)context.get("balanorientaction");
  nc.ui.bd.account.actions.BalanFlagAction bean = new nc.ui.bd.account.actions.BalanFlagAction();
  context.put("balanorientaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.OutflagSetAction getOutflagaction(){
 if(context.get("outflagaction")!=null)
 return (nc.ui.bd.account.actions.OutflagSetAction)context.get("outflagaction");
  nc.ui.bd.account.actions.OutflagSetAction bean = new nc.ui.bd.account.actions.OutflagSetAction();
  context.put("outflagaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getAccaddactiongroup(){
 if(context.get("accaddactiongroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("accaddactiongroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("accaddactiongroup",bean);
  bean.setActions(getManagedList29());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList29(){  List list = new ArrayList();  list.add(getAccaddaction());  list.add(getInsertmidaction());  return list;}

public nc.funcnode.ui.action.MenuAction getBusiactiongroup(){
 if(context.get("busiactiongroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("busiactiongroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("busiactiongroup",bean);
  bean.setCode("busiaction");
  bean.setName(getBusinessbtnMultilanguage());
  bean.setActions(getManagedList30());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getBusinessbtnMultilanguage(){
 if(context.get("businessbtnMultilanguage")!=null)
 return (java.lang.String)context.get("businessbtnMultilanguage");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&businessbtnMultilanguage",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("辅助功能");
  bean.setResId("010140accb0313");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("businessbtnMultilanguage",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList30(){  List list = new ArrayList();  list.add(getQuicksetassaction());  return list;}

public nc.funcnode.ui.action.GroupAction getChartactiongroup(){
 if(context.get("chartactiongroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("chartactiongroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("chartactiongroup",bean);
  bean.setActions(getManagedList31());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList31(){  List list = new ArrayList();  list.add(getChartmaintenanceaction());  list.add(getChartaddaction());  list.add(getCharteditaction());  list.add(getAccchartdelaction());  list.add(getChartnewversionaction());  list.add(getAccmapaction());  list.add(getChartdelversionaction());  return list;}

public nc.funcnode.ui.action.MenuAction getAccmanagesetgroup(){
 if(context.get("accmanagesetgroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("accmanagesetgroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("accmanagesetgroup",bean);
  bean.setCode("accmanageaction");
  bean.setName(getI18nFB_ef42eb());
  bean.setActions(getManagedList32());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_ef42eb(){
 if(context.get("nc.ui.uif2.I18nFB#ef42eb")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#ef42eb");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#ef42eb",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("科目管控设置");
  bean.setResId("010140accb0314");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#ef42eb",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList32(){  List list = new ArrayList();  list.add(getAccctrlruleaction());  list.add(getChartobligateaction());  return list;}

public nc.ui.bd.account.actions.AccQueryAction getAccqryaction(){
 if(context.get("accqryaction")!=null)
 return (nc.ui.bd.account.actions.AccQueryAction)context.get("accqryaction");
  nc.ui.bd.account.actions.AccQueryAction bean = new nc.ui.bd.account.actions.AccQueryAction();
  context.put("accqryaction",bean);
  bean.setModel(getCharthappmodel());
  bean.setAccQueryListViewer(getAccquerylistviewer());
  bean.setDataManager(getAccQueryDataManager());
  bean.setQueryDelegator(getAccountQueryDelegator_129368());
  bean.setTemplateContainer(getQueryTemplateContianer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.bd.account.actions.AccountQueryDelegator getAccountQueryDelegator_129368(){
 if(context.get("nc.ui.bd.account.actions.AccountQueryDelegator#129368")!=null)
 return (nc.ui.bd.account.actions.AccountQueryDelegator)context.get("nc.ui.bd.account.actions.AccountQueryDelegator#129368");
  nc.ui.bd.account.actions.AccountQueryDelegator bean = new nc.ui.bd.account.actions.AccountQueryDelegator();
  context.put("nc.ui.bd.account.actions.AccountQueryDelegator#129368",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AllowCloseAccountAction getAllowcloseaccaction(){
 if(context.get("allowcloseaccaction")!=null)
 return (nc.ui.bd.account.actions.AllowCloseAccountAction)context.get("allowcloseaccaction");
  nc.ui.bd.account.actions.AllowCloseAccountAction bean = new nc.ui.bd.account.actions.AllowCloseAccountAction();
  context.put("allowcloseaccaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setAccChartEnv(getAccChartEnv());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getAccfilteraction(){
 if(context.get("accfilteraction")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("accfilteraction");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("accfilteraction",bean);
  bean.setCode("accfilter");
  bean.setName(getI18nFB_18b980f());
  bean.setActions(getManagedList33());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_18b980f(){
 if(context.get("nc.ui.uif2.I18nFB#18b980f")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#18b980f");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#18b980f",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("过滤");
  bean.setResId("010140accb0315");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#18b980f",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList33(){  List list = new ArrayList();  list.add(getFilteraction());  return list;}

public nc.ui.uif2.actions.ShowDisableDataAction getFilteraction(){
 if(context.get("filteraction")!=null)
 return (nc.ui.uif2.actions.ShowDisableDataAction)context.get("filteraction");
  nc.ui.uif2.actions.ShowDisableDataAction bean = new nc.ui.uif2.actions.ShowDisableDataAction();
  context.put("filteraction",bean);
  bean.setDataManager(getAccDataManager());
  bean.setModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.actions.AccChartMaitenanceAction getChartmaintenanceaction(){
 if(context.get("chartmaintenanceaction")!=null)
 return (nc.ui.bd.accchart.actions.AccChartMaitenanceAction)context.get("chartmaintenanceaction");
  nc.ui.bd.accchart.actions.AccChartMaitenanceAction bean = new nc.ui.bd.accchart.actions.AccChartMaitenanceAction();
  context.put("chartmaintenanceaction",bean);
  bean.setChartAppModel(getCharthappmodel());
  bean.setTopPane(getToppane());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.actions.AccChartAddAction getChartaddaction(){
 if(context.get("chartaddaction")!=null)
 return (nc.ui.bd.accchart.actions.AccChartAddAction)context.get("chartaddaction");
  nc.ui.bd.accchart.actions.AccChartAddAction bean = new nc.ui.bd.accchart.actions.AccChartAddAction();
  context.put("chartaddaction",bean);
  bean.setChartAppModel(getCharthappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.actions.AccChartEditAction getCharteditaction(){
 if(context.get("charteditaction")!=null)
 return (nc.ui.bd.accchart.actions.AccChartEditAction)context.get("charteditaction");
  nc.ui.bd.accchart.actions.AccChartEditAction bean = new nc.ui.bd.accchart.actions.AccChartEditAction();
  context.put("charteditaction",bean);
  bean.setChartAppModel(getCharthappmodel());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.actions.AccChartDelVersionAction getChartdelversionaction(){
 if(context.get("chartdelversionaction")!=null)
 return (nc.ui.bd.accchart.actions.AccChartDelVersionAction)context.get("chartdelversionaction");
  nc.ui.bd.accchart.actions.AccChartDelVersionAction bean = new nc.ui.bd.accchart.actions.AccChartDelVersionAction();
  context.put("chartdelversionaction",bean);
  bean.setModel(getCharthappmodel());
  bean.setExceptionHandler(getExhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.actions.AccChartNewVersionAction getChartnewversionaction(){
 if(context.get("chartnewversionaction")!=null)
 return (nc.ui.bd.accchart.actions.AccChartNewVersionAction)context.get("chartnewversionaction");
  nc.ui.bd.accchart.actions.AccChartNewVersionAction bean = new nc.ui.bd.accchart.actions.AccChartNewVersionAction();
  context.put("chartnewversionaction",bean);
  bean.setDimen(getDimension_1ac34de());
  bean.setEditor(getNewversioneditor());
  bean.setDlgTitle(getI18nFB_c2f345());
  bean.setModel(getCharthappmodel());
  bean.setSaveAction(getVersionsaveaction());
  bean.setExceptionHandler(getExhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.awt.Dimension getDimension_1ac34de(){
 if(context.get("java.awt.Dimension#1ac34de")!=null)
 return (java.awt.Dimension)context.get("java.awt.Dimension#1ac34de");
  java.awt.Dimension bean = new java.awt.Dimension(400,150);  context.put("java.awt.Dimension#1ac34de",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_c2f345(){
 if(context.get("nc.ui.uif2.I18nFB#c2f345")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#c2f345");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#c2f345",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("科目表创建新版本");
  bean.setResId("010140accb0316");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#c2f345",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.bd.accchart.actions.AccChartObligateAction getChartobligateaction(){
 if(context.get("chartobligateaction")!=null)
 return (nc.ui.bd.accchart.actions.AccChartObligateAction)context.get("chartobligateaction");
  nc.ui.bd.accchart.actions.AccChartObligateAction bean = new nc.ui.bd.accchart.actions.AccChartObligateAction();
  context.put("chartobligateaction",bean);
  bean.setChartAppModel(getCharthappmodel());
  bean.setAccModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.actions.AccChartRefreshAction getChartrefreshaction(){
 if(context.get("chartrefreshaction")!=null)
 return (nc.ui.bd.accchart.actions.AccChartRefreshAction)context.get("chartrefreshaction");
  nc.ui.bd.accchart.actions.AccChartRefreshAction bean = new nc.ui.bd.accchart.actions.AccChartRefreshAction();
  context.put("chartrefreshaction",bean);
  bean.setManager(getModelDataManager());
  bean.setModel(getCharthappmodel());
  bean.setExceptionHandler(getExhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.actions.AccChartDelAction getAccchartdelaction(){
 if(context.get("accchartdelaction")!=null)
 return (nc.ui.bd.accchart.actions.AccChartDelAction)context.get("accchartdelaction");
  nc.ui.bd.accchart.actions.AccChartDelAction bean = new nc.ui.bd.accchart.actions.AccChartDelAction();
  context.put("accchartdelaction",bean);
  bean.setModel(getCharthappmodel());
  bean.setRefreshAction(getChartrefreshaction());
  bean.setExceptionHandler(getExhandler());
  bean.setInterceptor(getAccchartVersionInteceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.DefaultExceptionHanler getExhandler(){
 if(context.get("exhandler")!=null)
 return (nc.ui.uif2.DefaultExceptionHanler)context.get("exhandler");
  nc.ui.uif2.DefaultExceptionHanler bean = new nc.ui.uif2.DefaultExceptionHanler();
  context.put("exhandler",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.accchart.actions.ChartNewVersionSaveAction getVersionsaveaction(){
 if(context.get("versionsaveaction")!=null)
 return (nc.ui.bd.accchart.actions.ChartNewVersionSaveAction)context.get("versionsaveaction");
  nc.ui.bd.accchart.actions.ChartNewVersionSaveAction bean = new nc.ui.bd.accchart.actions.ChartNewVersionSaveAction();
  context.put("versionsaveaction",bean);
  bean.setEditor(getNewversioneditor());
  bean.setModel(getCharthappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccAssAddLineAction getAssaddlineaction(){
 if(context.get("assaddlineaction")!=null)
 return (nc.ui.bd.account.actions.AccAssAddLineAction)context.get("assaddlineaction");
  nc.ui.bd.account.actions.AccAssAddLineAction bean = new nc.ui.bd.account.actions.AccAssAddLineAction();
  context.put("assaddlineaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setCardpanel(getAccasspanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccAssDelLineAction getAssdellineaction(){
 if(context.get("assdellineaction")!=null)
 return (nc.ui.bd.account.actions.AccAssDelLineAction)context.get("assdellineaction");
  nc.ui.bd.account.actions.AccAssDelLineAction bean = new nc.ui.bd.account.actions.AccAssDelLineAction();
  context.put("assdellineaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setCardpanel(getAccasspanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AssUpAction getAssupaction(){
 if(context.get("assupaction")!=null)
 return (nc.ui.bd.account.actions.AssUpAction)context.get("assupaction");
  nc.ui.bd.account.actions.AssUpAction bean = new nc.ui.bd.account.actions.AssUpAction();
  context.put("assupaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setBillCard(getAccasspanel());
  bean.setExceptionHandler(getExhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AssDownAction getAssdownaction(){
 if(context.get("assdownaction")!=null)
 return (nc.ui.bd.account.actions.AssDownAction)context.get("assdownaction");
  nc.ui.bd.account.actions.AssDownAction bean = new nc.ui.bd.account.actions.AssDownAction();
  context.put("assdownaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setBillCard(getAccasspanel());
  bean.setExceptionHandler(getExhandler());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccCtrlMdleAddAction getCtrlmdleaddaction(){
 if(context.get("ctrlmdleaddaction")!=null)
 return (nc.ui.bd.account.actions.AccCtrlMdleAddAction)context.get("ctrlmdleaddaction");
  nc.ui.bd.account.actions.AccCtrlMdleAddAction bean = new nc.ui.bd.account.actions.AccCtrlMdleAddAction();
  context.put("ctrlmdleaddaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setCardpanel(getAccctrlmdlpanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.AccCtrlMdleDelAction getCtrlmdledelaction(){
 if(context.get("ctrlmdledelaction")!=null)
 return (nc.ui.bd.account.actions.AccCtrlMdleDelAction)context.get("ctrlmdledelaction");
  nc.ui.bd.account.actions.AccCtrlMdleDelAction bean = new nc.ui.bd.account.actions.AccCtrlMdleDelAction();
  context.put("ctrlmdledelaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setCardpanel(getAccctrlmdlpanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.QuickAssSetAllSelectAction getAccallselect(){
 if(context.get("accallselect")!=null)
 return (nc.ui.bd.account.actions.QuickAssSetAllSelectAction)context.get("accallselect");
  nc.ui.bd.account.actions.QuickAssSetAllSelectAction bean = new nc.ui.bd.account.actions.QuickAssSetAllSelectAction();
  context.put("accallselect",bean);
  bean.setEditor(getQasscentereditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.QuickAssSetAllNotSelAction getAccallnotselect(){
 if(context.get("accallnotselect")!=null)
 return (nc.ui.bd.account.actions.QuickAssSetAllNotSelAction)context.get("accallnotselect");
  nc.ui.bd.account.actions.QuickAssSetAllNotSelAction bean = new nc.ui.bd.account.actions.QuickAssSetAllNotSelAction();
  context.put("accallnotselect",bean);
  bean.setEditor(getQasscentereditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.QuickSetAssSaveAction getQuicksetasssaveaction(){
 if(context.get("quicksetasssaveaction")!=null)
 return (nc.ui.bd.account.actions.QuickSetAssSaveAction)context.get("quicksetasssaveaction");
  nc.ui.bd.account.actions.QuickSetAssSaveAction bean = new nc.ui.bd.account.actions.QuickSetAssSaveAction();
  context.put("quicksetasssaveaction",bean);
  bean.setNorthEditor(getQassnortheditor());
  bean.setCenterEditor(getQasscentereditor());
  bean.setModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.QuickSetAssCancelAction getQuicksetasscancelaction(){
 if(context.get("quicksetasscancelaction")!=null)
 return (nc.ui.bd.account.actions.QuickSetAssCancelAction)context.get("quicksetasscancelaction");
  nc.ui.bd.account.actions.QuickSetAssCancelAction bean = new nc.ui.bd.account.actions.QuickSetAssCancelAction();
  context.put("quicksetasscancelaction",bean);
  bean.setNorthEditor(getQassnortheditor());
  bean.setCenterEditor(getQasscentereditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.bd.account.actions.InsertMidLevSaveAction getInsertmidlevaction(){
 if(context.get("insertmidlevaction")!=null)
 return (nc.ui.bd.account.actions.InsertMidLevSaveAction)context.get("insertmidlevaction");
  nc.ui.bd.account.actions.InsertMidLevSaveAction bean = new nc.ui.bd.account.actions.InsertMidLevSaveAction();
  context.put("insertmidlevaction",bean);
  bean.setModel(getAccountappmodel());
  bean.setInsertEditor(getInsertmidacceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getListactions(){
 if(context.get("listactions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("listactions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getAcclistview());  context.put("listactions",bean);
  bean.setActions(getManagedList34());
  bean.setModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList34(){  List list = new ArrayList();  list.add(getChartactiongroup());  list.add(getAccmanagesetgroup());  list.add(getNullAction());  list.add(getAccaddactiongroup());  list.add(getAcceditactiongroup());  list.add(getAccdelaction());  list.add(getAcccopyaction());  list.add(getNullAction());  list.add(getAccqryaction());  list.add(getAccrefreshaction());  list.add(getAccfilteraction());  list.add(getNullAction());  list.add(getBusiactiongroup());  list.add(getAccenablegroup());  list.add(getNullAction());  list.add(getListprintgroup());  return list;}

public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getCardactions(){
 if(context.get("cardactions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("cardactions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getAcceditor());  context.put("cardactions",bean);
  bean.setEditActions(getManagedList35());
  bean.setActions(getManagedList36());
  bean.setModel(getAccountappmodel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList35(){  List list = new ArrayList();  list.add(getAccsaveaction());  list.add(getAccsavenewaddaction());  list.add(getNullAction());  list.add(getAcccancelaction());  return list;}

private List getManagedList36(){  List list = new ArrayList();  list.add(getAccaddactiongroup());  list.add(getAcceditactiongroup());  list.add(getAccdelaction());  list.add(getAcccopyaction());  list.add(getNullAction());  list.add(getAccqryaction());  list.add(getAccsinglerefresh());  list.add(getAccfilteraction());  list.add(getNullAction());  list.add(getBusiactiongroup());  list.add(getAccenablegroup());  list.add(getNullAction());  list.add(getPrintgroup());  return list;}

public nc.ui.uif2.actions.ActionContributors getToftpanelActionContributors(){
 if(context.get("toftpanelActionContributors")!=null)
 return (nc.ui.uif2.actions.ActionContributors)context.get("toftpanelActionContributors");
  nc.ui.uif2.actions.ActionContributors bean = new nc.ui.uif2.actions.ActionContributors();
  context.put("toftpanelActionContributors",bean);
  bean.setContributors(getManagedList37());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList37(){  List list = new ArrayList();  list.add(getListactions());  list.add(getCardactions());  list.add(getQuerycardactions());  list.add(getAccqueryactions());  return list;}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayoutRoot(getTBNode_1899af7());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_1899af7(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#1899af7")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#1899af7");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#1899af7",bean);
  bean.setName("querytb");
  bean.setShowMode("CardLayout");
  bean.setTabs(getManagedList38());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList38(){  List list = new ArrayList();  list.add(getTBNode_c1d401());  list.add(getTBNode_1dbec96());  return list;}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_c1d401(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#c1d401")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#c1d401");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#c1d401",bean);
  bean.setTabs(getManagedList39());
  bean.setShowMode("CardLayout");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList39(){  List list = new ArrayList();  list.add(getVSNode_18e8a6b());  list.add(getVSNode_16d0670());  return list;}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_18e8a6b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#18e8a6b")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#18e8a6b");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#18e8a6b",bean);
  bean.setUp(getCNode_f16175());
  bean.setDown(getCNode_1358868());
  bean.setDividerLocation(60f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_f16175(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#f16175")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#f16175");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#f16175",bean);
  bean.setComponent(getToppane());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1358868(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1358868")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1358868");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1358868",bean);
  bean.setComponent(getAcclistview());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_16d0670(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#16d0670")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#16d0670");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#16d0670",bean);
  bean.setUp(getCNode_1218b16());
  bean.setDown(getVSNode_124eda1());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1218b16(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1218b16")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1218b16");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1218b16",bean);
  bean.setComponent(getCardInfoPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_124eda1(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#124eda1")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#124eda1");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#124eda1",bean);
  bean.setUp(getVSNode_8e82d4());
  bean.setDown(getCNode_2f442d());
  bean.setDividerLocation(0.9f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_8e82d4(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#8e82d4")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#8e82d4");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#8e82d4",bean);
  bean.setUp(getCNode_132928e());
  bean.setDown(getHSNode_27cb36());
  bean.setDividerLocation(0.5f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_132928e(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#132928e")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#132928e");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#132928e",bean);
  bean.setComponent(getAcceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_27cb36(){
 if(context.get("nc.ui.uif2.tangramlayout.node.HSNode#27cb36")!=null)
 return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#27cb36");
  nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
  context.put("nc.ui.uif2.tangramlayout.node.HSNode#27cb36",bean);
  bean.setLeft(getTBNode_e2de25());
  bean.setRight(getTBNode_dca644());
  bean.setDividerLocation(0.75f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_e2de25(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#e2de25")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#e2de25");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#e2de25",bean);
  bean.setTabs(getManagedList40());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList40(){  List list = new ArrayList();  list.add(getCNode_1ab4398());  return list;}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1ab4398(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1ab4398")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1ab4398");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1ab4398",bean);
  bean.setName(getI18nFB_133e3b7());
  bean.setComponent(getAccasspanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_133e3b7(){
 if(context.get("nc.ui.uif2.I18nFB#133e3b7")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#133e3b7");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#133e3b7",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("辅助核算");
  bean.setResId("010140accb0318");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#133e3b7",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_dca644(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#dca644")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#dca644");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#dca644",bean);
  bean.setTabs(getManagedList41());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList41(){  List list = new ArrayList();  list.add(getCNode_ff8a7b());  return list;}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_ff8a7b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#ff8a7b")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#ff8a7b");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#ff8a7b",bean);
  bean.setName(getI18nFB_668e2a());
  bean.setComponent(getAccctrlmdlpanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_668e2a(){
 if(context.get("nc.ui.uif2.I18nFB#668e2a")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#668e2a");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#668e2a",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("受控模块");
  bean.setResId("010140accb0319");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#668e2a",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_2f442d(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#2f442d")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#2f442d");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#2f442d",bean);
  bean.setComponent(getAuditinfopanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_1dbec96(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#1dbec96")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#1dbec96");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#1dbec96",bean);
  bean.setShowMode("CardLayout");
  bean.setTabs(getManagedList42());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList42(){  List list = new ArrayList();  list.add(getVSNode_124ae87());  list.add(getVSNode_c60a82());  return list;}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_124ae87(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#124ae87")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#124ae87");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#124ae87",bean);
  bean.setUp(getCNode_145c675());
  bean.setDown(getCNode_1e86880());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_145c675(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#145c675")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#145c675");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#145c675",bean);
  bean.setComponent(getQryinfopnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1e86880(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1e86880")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1e86880");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1e86880",bean);
  bean.setComponent(getAccquerylistviewer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_c60a82(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#c60a82")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#c60a82");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#c60a82",bean);
  bean.setUp(getCNode_13e50d7());
  bean.setDown(getVSNode_1dae841());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_13e50d7(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#13e50d7")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#13e50d7");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#13e50d7",bean);
  bean.setComponent(getQueryCardInfoPnl());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_1dae841(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#1dae841")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#1dae841");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#1dae841",bean);
  bean.setUp(getVSNode_14e0efd());
  bean.setDown(getCNode_1ba4450());
  bean.setDividerLocation(0.9f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_14e0efd(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#14e0efd")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#14e0efd");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#14e0efd",bean);
  bean.setUp(getCNode_2d5d1f());
  bean.setDown(getHSNode_f119f());
  bean.setDividerLocation(0.5f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_2d5d1f(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#2d5d1f")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#2d5d1f");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#2d5d1f",bean);
  bean.setComponent(getQuery_acceditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_f119f(){
 if(context.get("nc.ui.uif2.tangramlayout.node.HSNode#f119f")!=null)
 return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#f119f");
  nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
  context.put("nc.ui.uif2.tangramlayout.node.HSNode#f119f",bean);
  bean.setLeft(getTBNode_d9cbd9());
  bean.setRight(getTBNode_86391a());
  bean.setDividerLocation(0.75f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_d9cbd9(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#d9cbd9")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#d9cbd9");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#d9cbd9",bean);
  bean.setTabs(getManagedList43());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList43(){  List list = new ArrayList();  list.add(getCNode_5e53f4());  return list;}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_5e53f4(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#5e53f4")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#5e53f4");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#5e53f4",bean);
  bean.setName(getI18nFB_3ccf8d());
  bean.setComponent(getQuery_accasspanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_3ccf8d(){
 if(context.get("nc.ui.uif2.I18nFB#3ccf8d")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#3ccf8d");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#3ccf8d",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("辅助核算");
  bean.setResId("010140accb0318");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#3ccf8d",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_86391a(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#86391a")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#86391a");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#86391a",bean);
  bean.setTabs(getManagedList44());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList44(){  List list = new ArrayList();  list.add(getCNode_c6e777());  return list;}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_c6e777(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#c6e777")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#c6e777");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#c6e777",bean);
  bean.setName(getI18nFB_d65291());
  bean.setComponent(getQuery_accctrlmdlpanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_d65291(){
 if(context.get("nc.ui.uif2.I18nFB#d65291")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#d65291");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#d65291",bean);  bean.setResDir("10140accb");
  bean.setDefaultValue("受控模块");
  bean.setResId("010140accb0319");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#d65291",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1ba4450(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1ba4450")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1ba4450");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1ba4450",bean);
  bean.setComponent(getQuery_auditinfopanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
