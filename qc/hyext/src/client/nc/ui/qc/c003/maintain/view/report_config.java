/*      */ package nc.ui.qc.c003.maintain.view;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator;
/*      */ import nc.ui.uif2.I18nFB;
/*      */ 
/*      */ public class report_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*      */ {
/*   11 */   private Map<String, Object> context = new java.util.HashMap();
/*      */   
/*   13 */   public nc.ui.uif2.actions.ActionContributors getToftpanelActionContributors() { if (context.get("toftpanelActionContributors") != null)
/*   14 */       return (nc.ui.uif2.actions.ActionContributors)context.get("toftpanelActionContributors");
/*   15 */     nc.ui.uif2.actions.ActionContributors bean = new nc.ui.uif2.actions.ActionContributors();
/*   16 */     context.put("toftpanelActionContributors", bean);
/*   17 */     bean.setContributors(getManagedList0());
/*   18 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   19 */     invokeInitializingBean(bean);
/*   20 */     return bean;
/*      */   }
/*      */   
/*   23 */   private List getManagedList0() { List list = new ArrayList();list.add(getActionsOfList());list.add(getActionsOfCard());list.add(getActionsOfHistory());return list;
/*      */   }
/*      */   
/*   26 */   public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfList() { if (context.get("actionsOfList") != null)
/*   27 */       return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("actionsOfList");
/*   28 */     nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getListView());context.put("actionsOfList", bean);
/*   29 */     bean.setActions(getManagedList1());
/*   30 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   31 */     invokeInitializingBean(bean);
/*   32 */     return bean;
/*      */   }
/*      */   
/*   35 */   private List getManagedList1() { List list = new ArrayList();list.add(getAddUIAction());list.add(getUpdateUIAction());list.add(getDeleteUIAction());list.add(getSeparatorAction());list.add(getQueryUIAction());list.add(getRefreshUIAction());list.add(getSeparatorAction());list.add(getCommitMenuAction());list.add(getAuditMenuAction());list.add(getAccessoriesMenuAction());list.add(getSeparatorAction());list.add(getLinkQueryMenuAction());list.add(getSeparatorAction());list.add(getRelatingMenuAction());list.add(getSeparatorAction());list.add(getCheckInfoAction());list.add(getSeparatorAction());list.add(getPrintMenuAction());return list;
/*      */   }
/*      */   
/*   38 */   public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfCard() { if (context.get("actionsOfCard") != null)
/*   39 */       return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("actionsOfCard");
/*   40 */     nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBillFormEditor());context.put("actionsOfCard", bean);
/*   41 */     bean.setModel(getManageAppModel());
/*   42 */     bean.setActions(getManagedList2());
/*   43 */     bean.setEditActions(getManagedList3());
/*   44 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   45 */     invokeInitializingBean(bean);
/*   46 */     return bean;
/*      */   }
/*      */   
/*   49 */   private List getManagedList2() { List list = new ArrayList();list.add(getAddUIAction());list.add(getUpdateUIAction());list.add(getDeleteUIAction());list.add(getSeparatorAction());list.add(getQueryUIAction());list.add(getCardRefreshAction());list.add(getSeparatorAction());list.add(getCommitMenuAction());list.add(getAuditMenuAction());list.add(getAccessoriesMenuAction());list.add(getSeparatorAction());list.add(getLinkQueryMenuAction());list.add(getSeparatorAction());list.add(getRelatingMenuAction());list.add(getSeparatorAction());list.add(getCheckInfoAction());list.add(getSeparatorAction());list.add(getPrintMenuAction());return list; }
/*      */   
/*   51 */   private List getManagedList3() { List list = new ArrayList();list.add(getSaveUIAction());list.add(getSeparatorAction());list.add(getCancelAction());return list;
/*      */   }
/*      */   
/*   54 */   public nc.funcnode.ui.action.SeparatorAction getSeparatorAction() { if (context.get("separatorAction") != null)
/*   55 */       return (nc.funcnode.ui.action.SeparatorAction)context.get("separatorAction");
/*   56 */     nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
/*   57 */     context.put("separatorAction", bean);
/*   58 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   59 */     invokeInitializingBean(bean);
/*   60 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfHistory() {
/*   64 */     if (context.get("actionsOfHistory") != null)
/*   65 */       return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("actionsOfHistory");
/*   66 */     nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getHisVersionListView());context.put("actionsOfHistory", bean);
/*   67 */     bean.setActions(getManagedList4());
/*   68 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   69 */     invokeInitializingBean(bean);
/*   70 */     return bean;
/*      */   }
/*      */   
/*   73 */   private List getManagedList4() { List list = new ArrayList();list.add(getReturnAction());return list;
/*      */   }
/*      */   
/*   76 */   public nc.ui.pubapp.billref.dest.TransferViewProcessor getTransferViewProcessor() { if (context.get("transferViewProcessor") != null)
/*   77 */       return (nc.ui.pubapp.billref.dest.TransferViewProcessor)context.get("transferViewProcessor");
/*   78 */     nc.ui.pubapp.billref.dest.TransferViewProcessor bean = new nc.ui.pubapp.billref.dest.TransferViewProcessor();
/*   79 */     context.put("transferViewProcessor", bean);
/*   80 */     bean.setList(getListView());
/*   81 */     bean.setActionContainer(getActionsOfList());
/*   82 */     bean.setCardActionContainer(getActionsOfCard());
/*   83 */     bean.setSaveAction(getSaveUIAction());
/*   84 */     bean.setCommitAction(getSendUIAction());
/*   85 */     bean.setCancelAction(getCancelAction());
/*   86 */     bean.setQueryInfoToolbarPanel(getQueryInfo());
/*   87 */     bean.setBillForm(getBillFormEditor());
/*   88 */     bean.setTransferLogic(getDefaultBillDataLogic_1a08f12());
/*   89 */     bean.setQueryAreaShell(getQueryArea());
/*   90 */     bean.setListRowChangeProcessor(getSelectionChangedHandler());
/*   91 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   92 */     invokeInitializingBean(bean);
/*   93 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.billref.dest.DefaultBillDataLogic getDefaultBillDataLogic_1a08f12() {
/*   97 */     if (context.get("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#1a08f12") != null)
/*   98 */       return (nc.ui.pubapp.billref.dest.DefaultBillDataLogic)context.get("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#1a08f12");
/*   99 */     nc.ui.pubapp.billref.dest.DefaultBillDataLogic bean = new nc.ui.pubapp.billref.dest.DefaultBillDataLogic();
/*  100 */     context.put("nc.ui.pubapp.billref.dest.DefaultBillDataLogic#1a08f12", bean);
/*  101 */     bean.setBillForm(getBillFormEditor());
/*  102 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  103 */     invokeInitializingBean(bean);
/*  104 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.AddUIAction getAddUIAction() {
/*  108 */     if (context.get("addUIAction") != null)
/*  109 */       return (nc.ui.qc.c003.maintain.action.AddUIAction)context.get("addUIAction");
/*  110 */     nc.ui.qc.c003.maintain.action.AddUIAction bean = new nc.ui.qc.c003.maintain.action.AddUIAction();
/*  111 */     context.put("addUIAction", bean);
/*  112 */     bean.setModel(getManageAppModel());
/*  113 */     bean.setEditor(getBillFormEditor());
/*  114 */     bean.setTransferViewProcessor(getTransferViewProcessor());
/*  115 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  116 */     invokeInitializingBean(bean);
/*  117 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.UpdateUIAction getUpdateUIAction() {
/*  121 */     if (context.get("updateUIAction") != null)
/*  122 */       return (nc.ui.qc.c003.maintain.action.UpdateUIAction)context.get("updateUIAction");
/*  123 */     nc.ui.qc.c003.maintain.action.UpdateUIAction bean = new nc.ui.qc.c003.maintain.action.UpdateUIAction();
/*  124 */     context.put("updateUIAction", bean);
/*  125 */     bean.setModel(getManageAppModel());
/*  126 */     bean.setBillForm(getBillFormEditor());
/*  127 */     bean.setInterceptor(getShowUpComponentInterceptor_110b7a());
/*  128 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  129 */     invokeInitializingBean(bean);
/*  130 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getShowUpComponentInterceptor_110b7a() {
/*  134 */     if (context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#110b7a") != null)
/*  135 */       return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#110b7a");
/*  136 */     nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
/*  137 */     context.put("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor#110b7a", bean);
/*  138 */     bean.setShowUpComponent(getBillFormEditor());
/*  139 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  140 */     invokeInitializingBean(bean);
/*  141 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.DeleteUIAction getDeleteUIAction() {
/*  145 */     if (context.get("deleteUIAction") != null)
/*  146 */       return (nc.ui.qc.c003.maintain.action.DeleteUIAction)context.get("deleteUIAction");
/*  147 */     nc.ui.qc.c003.maintain.action.DeleteUIAction bean = new nc.ui.qc.c003.maintain.action.DeleteUIAction();
/*  148 */     context.put("deleteUIAction", bean);
/*  149 */     bean.setModel(getManageAppModel());
/*  150 */     bean.setActionName("DISCARD");
/*  151 */     bean.setBillType("C003");
/*  152 */     bean.setEditor(getBillFormEditor());
/*  153 */     bean.setValidationService(getPowerDeleteservice());
/*  154 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  155 */     invokeInitializingBean(bean);
/*  156 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.pub.power.PowerValidateService getPowerDeleteservice() {
/*  160 */     if (context.get("powerDeleteservice") != null)
/*  161 */       return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerDeleteservice");
/*  162 */     nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
/*  163 */     context.put("powerDeleteservice", bean);
/*  164 */     bean.setActionCode("delete");
/*  165 */     bean.setBillCodeFiledName("vbillcode");
/*  166 */     bean.setPermissionCode("C003");
/*  167 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  168 */     invokeInitializingBean(bean);
/*  169 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.SaveUIAction getSaveUIAction() {
/*  173 */     if (context.get("saveUIAction") != null)
/*  174 */       return (nc.ui.qc.c003.maintain.action.SaveUIAction)context.get("saveUIAction");
/*  175 */     nc.ui.qc.c003.maintain.action.SaveUIAction bean = new nc.ui.qc.c003.maintain.action.SaveUIAction();
/*  176 */     context.put("saveUIAction", bean);
/*  177 */     bean.setModel(getManageAppModel());
/*  178 */     bean.setEditor(getBillFormEditor());
/*  179 */     bean.setActionName("SAVEBASE");
/*  180 */     bean.setBillType("C003");
/*  181 */     bean.setValidationService(getPowervalidservice());
/*  182 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  183 */     invokeInitializingBean(bean);
/*  184 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.validation.CompositeValidation getValidateService() {
/*  188 */     if (context.get("validateService") != null)
/*  189 */       return (nc.ui.pubapp.uif2app.validation.CompositeValidation)context.get("validateService");
/*  190 */     nc.ui.pubapp.uif2app.validation.CompositeValidation bean = new nc.ui.pubapp.uif2app.validation.CompositeValidation();
/*  191 */     context.put("validateService", bean);
/*  192 */     bean.setValidators(getManagedList5());
/*  193 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  194 */     invokeInitializingBean(bean);
/*  195 */     return bean;
/*      */   }
/*      */   
/*  198 */   private List getManagedList5() { List list = new ArrayList();list.add(getPowervalidservice());return list;
/*      */   }
/*      */   
/*  201 */   public nc.ui.pubapp.pub.power.PowerSaveValidateService getPowervalidservice() { if (context.get("powervalidservice") != null)
/*  202 */       return (nc.ui.pubapp.pub.power.PowerSaveValidateService)context.get("powervalidservice");
/*  203 */     nc.ui.pubapp.pub.power.PowerSaveValidateService bean = new nc.ui.pubapp.pub.power.PowerSaveValidateService();
/*  204 */     context.put("powervalidservice", bean);
/*  205 */     bean.setEditActionCode("edit");
/*  206 */     bean.setBillCodeFiledName("vbillcode");
/*  207 */     bean.setPermissionCode("C003");
/*  208 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  209 */     invokeInitializingBean(bean);
/*  210 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.actions.CancelAction getCancelAction() {
/*  214 */     if (context.get("cancelAction") != null)
/*  215 */       return (nc.ui.pubapp.uif2app.actions.CancelAction)context.get("cancelAction");
/*  216 */     nc.ui.pubapp.uif2app.actions.CancelAction bean = new nc.ui.pubapp.uif2app.actions.CancelAction();
/*  217 */     context.put("cancelAction", bean);
/*  218 */     bean.setModel(getManageAppModel());
/*  219 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  220 */     invokeInitializingBean(bean);
/*  221 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.editor.QueryTemplateContainer getQueryTemplateContainer() {
/*  225 */     if (context.get("queryTemplateContainer") != null)
/*  226 */       return (nc.ui.uif2.editor.QueryTemplateContainer)context.get("queryTemplateContainer");
/*  227 */     nc.ui.uif2.editor.QueryTemplateContainer bean = new nc.ui.uif2.editor.QueryTemplateContainer();
/*  228 */     context.put("queryTemplateContainer", bean);
/*  229 */     bean.setContext(getContext());
/*  230 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  231 */     invokeInitializingBean(bean);
/*  232 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction getQueryUIAction() {
/*  236 */     if (context.get("queryUIAction") != null)
/*  237 */       return (nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction)context.get("queryUIAction");
/*  238 */     nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction();
/*  239 */     context.put("queryUIAction", bean);
/*  240 */     bean.setDataManager(getInitDataManager());
/*  241 */     bean.setModel(getManageAppModel());
/*  242 */     bean.setTemplateContainer(getQueryTemplateContainer());
/*  243 */     bean.setQryCondDLGInitializer(getReportQueryDlgInitializer_108b73f());
/*  244 */     bean.setShowUpComponent(getListView());
/*  245 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  246 */     invokeInitializingBean(bean);
/*  247 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.query.ReportQueryDlgInitializer getReportQueryDlgInitializer_108b73f() {
/*  251 */     if (context.get("nc.ui.qc.c003.query.ReportQueryDlgInitializer#108b73f") != null)
/*  252 */       return (nc.ui.qc.c003.query.ReportQueryDlgInitializer)context.get("nc.ui.qc.c003.query.ReportQueryDlgInitializer#108b73f");
/*  253 */     nc.ui.qc.c003.query.ReportQueryDlgInitializer bean = new nc.ui.qc.c003.query.ReportQueryDlgInitializer();
/*  254 */     context.put("nc.ui.qc.c003.query.ReportQueryDlgInitializer#108b73f", bean);
/*  255 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  256 */     invokeInitializingBean(bean);
/*  257 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.ListRefreshAction getRefreshUIAction() {
/*  261 */     if (context.get("refreshUIAction") != null)
/*  262 */       return (nc.ui.qc.c003.maintain.action.ListRefreshAction)context.get("refreshUIAction");
/*  263 */     nc.ui.qc.c003.maintain.action.ListRefreshAction bean = new nc.ui.qc.c003.maintain.action.ListRefreshAction();
/*  264 */     context.put("refreshUIAction", bean);
/*  265 */     bean.setDataManager(getInitDataManager());
/*  266 */     bean.setModel(getManageAppModel());
/*  267 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  268 */     invokeInitializingBean(bean);
/*  269 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.CardRefreshAction getCardRefreshAction() {
/*  273 */     if (context.get("cardRefreshAction") != null)
/*  274 */       return (nc.ui.qc.c003.maintain.action.CardRefreshAction)context.get("cardRefreshAction");
/*  275 */     nc.ui.qc.c003.maintain.action.CardRefreshAction bean = new nc.ui.qc.c003.maintain.action.CardRefreshAction();
/*  276 */     context.put("cardRefreshAction", bean);
/*  277 */     bean.setModel(getManageAppModel());
/*  278 */     bean.setEditor(getBillFormEditor());
/*  279 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  280 */     invokeInitializingBean(bean);
/*  281 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.SendUIAction getSendUIAction() {
/*  285 */     if (context.get("sendUIAction") != null)
/*  286 */       return (nc.ui.qc.c003.maintain.action.SendUIAction)context.get("sendUIAction");
/*  287 */     nc.ui.qc.c003.maintain.action.SendUIAction bean = new nc.ui.qc.c003.maintain.action.SendUIAction();
/*  288 */     context.put("sendUIAction", bean);
/*  289 */     bean.setModel(getManageAppModel());
/*  290 */     bean.setEditor(getBillFormEditor());
/*  291 */     bean.setBillType("C003");
/*  292 */     bean.setActionName("SAVE");
/*  293 */     bean.setFilledUpInFlow(false);
/*  294 */     bean.setValidationService(getCommitpowervalidservice());
/*  295 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  296 */     invokeInitializingBean(bean);
/*  297 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction getSaveCommitAction() {
/*  301 */     if (context.get("saveCommitAction") != null)
/*  302 */       return (nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction)context.get("saveCommitAction");
/*  303 */     nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction bean = new nc.ui.pubapp.uif2app.actions.pflow.SaveAndCommitScriptAction(getSaveUIAction(), getSendUIAction());context.put("saveCommitAction", bean);
/*  304 */     bean.setModel(getManageAppModel());
/*  305 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  306 */     invokeInitializingBean(bean);
/*  307 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction getApproveStatusQuery() {
/*  311 */     if (context.get("approveStatusQuery") != null)
/*  312 */       return (nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction)context.get("approveStatusQuery");
/*  313 */     nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction bean = new nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction();
/*  314 */     context.put("approveStatusQuery", bean);
/*  315 */     bean.setModel(getManageAppModel());
/*  316 */     bean.setBillType("C003");
/*  317 */     bean.setBtnName(getI18nFB_13691d5());
/*  318 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  319 */     invokeInitializingBean(bean);
/*  320 */     return bean;
/*      */   }
/*      */   
/*      */   private String getI18nFB_13691d5() {
/*  324 */     if (context.get("nc.ui.uif2.I18nFB#13691d5") != null)
/*  325 */       return (String)context.get("nc.ui.uif2.I18nFB#13691d5");
/*  326 */     I18nFB bean = new I18nFB();
/*  327 */     context.put("&nc.ui.uif2.I18nFB#13691d5", bean);bean.setResDir("c010003_0");
/*  328 */     bean.setResId("0c010003-0097");
/*  329 */     bean.setDefaultValue("查看审批意见");
/*  330 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  331 */     invokeInitializingBean(bean);
/*      */     try {
/*  333 */       Object product = bean.getObject();
/*  334 */       context.put("nc.ui.uif2.I18nFB#13691d5", product);
/*  335 */       return (String)product;
/*      */     } catch (Exception e) {
/*  337 */       throw new RuntimeException(e);
/*      */     } }
/*      */   
/*  340 */   public nc.ui.qc.c003.maintain.action.RevokeUIAction getRevokeUIAction() { if (context.get("revokeUIAction") != null)
/*  341 */       return (nc.ui.qc.c003.maintain.action.RevokeUIAction)context.get("revokeUIAction");
/*  342 */     nc.ui.qc.c003.maintain.action.RevokeUIAction bean = new nc.ui.qc.c003.maintain.action.RevokeUIAction();
/*  343 */     context.put("revokeUIAction", bean);
/*  344 */     bean.setModel(getManageAppModel());
/*  345 */     bean.setEditor(getBillFormEditor());
/*  346 */     bean.setBillType("C003");
/*  347 */     bean.setActionName("UNSAVE");
/*  348 */     bean.setFilledUpInFlow(false);
/*  349 */     bean.setValidationService(getUncommitpowervalidservice());
/*  350 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  351 */     invokeInitializingBean(bean);
/*  352 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.funcnode.ui.action.GroupAction getCommitMenuAction() {
/*  356 */     if (context.get("commitMenuAction") != null)
/*  357 */       return (nc.funcnode.ui.action.GroupAction)context.get("commitMenuAction");
/*  358 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/*  359 */     context.put("commitMenuAction", bean);
/*  360 */     bean.setCode("commitMenuAction");
/*  361 */     bean.setActions(getManagedList6());
/*  362 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  363 */     invokeInitializingBean(bean);
/*  364 */     return bean;
/*      */   }
/*      */   
/*  367 */   private List getManagedList6() { List list = new ArrayList();list.add(getSendUIAction());list.add(getRevokeUIAction());return list;
/*      */   }
/*      */   
/*  370 */   public nc.funcnode.ui.action.GroupAction getAuditMenuAction() { if (context.get("auditMenuAction") != null)
/*  371 */       return (nc.funcnode.ui.action.GroupAction)context.get("auditMenuAction");
/*  372 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/*  373 */     context.put("auditMenuAction", bean);
/*  374 */     bean.setCode("auditMenuAction");
/*  375 */     bean.setActions(getManagedList7());
/*  376 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  377 */     invokeInitializingBean(bean);
/*  378 */     return bean;
/*      */   }
/*      */   
/*  381 */   private List getManagedList7() { List list = new ArrayList();list.add(getApproveUIAction_1a061a1());list.add(getUnApproveUIAction_69a34c());list.add(getSeparatorAction());list.add(getApproveStatusQuery());return list;
/*      */   }
/*      */   
/*  384 */   private nc.ui.qc.c003.maintain.action.ApproveUIAction getApproveUIAction_1a061a1() { if (context.get("nc.ui.qc.c003.maintain.action.ApproveUIAction#1a061a1") != null)
/*  385 */       return (nc.ui.qc.c003.maintain.action.ApproveUIAction)context.get("nc.ui.qc.c003.maintain.action.ApproveUIAction#1a061a1");
/*  386 */     nc.ui.qc.c003.maintain.action.ApproveUIAction bean = new nc.ui.qc.c003.maintain.action.ApproveUIAction();
/*  387 */     context.put("nc.ui.qc.c003.maintain.action.ApproveUIAction#1a061a1", bean);
/*  388 */     bean.setModel(getManageAppModel());
/*  389 */     bean.setFilledUpInFlow(false);
/*  390 */     bean.setBillType("C003");
/*  391 */     bean.setEditor(getBillFormEditor());
/*  392 */     bean.setValidationService(getPowerApproveService());
/*  393 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  394 */     invokeInitializingBean(bean);
/*  395 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.action.UnApproveUIAction getUnApproveUIAction_69a34c() {
/*  399 */     if (context.get("nc.ui.qc.c003.maintain.action.UnApproveUIAction#69a34c") != null)
/*  400 */       return (nc.ui.qc.c003.maintain.action.UnApproveUIAction)context.get("nc.ui.qc.c003.maintain.action.UnApproveUIAction#69a34c");
/*  401 */     nc.ui.qc.c003.maintain.action.UnApproveUIAction bean = new nc.ui.qc.c003.maintain.action.UnApproveUIAction();
/*  402 */     context.put("nc.ui.qc.c003.maintain.action.UnApproveUIAction#69a34c", bean);
/*  403 */     bean.setModel(getManageAppModel());
/*  404 */     bean.setFilledUpInFlow(false);
/*  405 */     bean.setBillType("C003");
/*  406 */     bean.setEditor(getBillFormEditor());
/*  407 */     bean.setValidationService(getPowerUNApproveService());
/*  408 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  409 */     invokeInitializingBean(bean);
/*  410 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.pub.power.PowerValidateService getCommitpowervalidservice() {
/*  414 */     if (context.get("commitpowervalidservice") != null)
/*  415 */       return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("commitpowervalidservice");
/*  416 */     nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
/*  417 */     context.put("commitpowervalidservice", bean);
/*  418 */     bean.setActionCode("commit");
/*  419 */     bean.setBillCodeFiledName("vbillcode");
/*  420 */     bean.setPermissionCode("C003");
/*  421 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  422 */     invokeInitializingBean(bean);
/*  423 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.pub.power.PowerValidateService getUncommitpowervalidservice() {
/*  427 */     if (context.get("uncommitpowervalidservice") != null)
/*  428 */       return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("uncommitpowervalidservice");
/*  429 */     nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
/*  430 */     context.put("uncommitpowervalidservice", bean);
/*  431 */     bean.setActionCode("uncommit");
/*  432 */     bean.setBillCodeFiledName("vbillcode");
/*  433 */     bean.setPermissionCode("C003");
/*  434 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  435 */     invokeInitializingBean(bean);
/*  436 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.pub.power.PowerValidateService getPowerApproveService() {
/*  440 */     if (context.get("powerApproveService") != null)
/*  441 */       return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerApproveService");
/*  442 */     nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
/*  443 */     context.put("powerApproveService", bean);
/*  444 */     bean.setActionCode("approve");
/*  445 */     bean.setBillCodeFiledName("vbillcode");
/*  446 */     bean.setPermissionCode("C003");
/*  447 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  448 */     invokeInitializingBean(bean);
/*  449 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.pub.power.PowerValidateService getPowerUNApproveService() {
/*  453 */     if (context.get("powerUNApproveService") != null)
/*  454 */       return (nc.ui.pubapp.pub.power.PowerValidateService)context.get("powerUNApproveService");
/*  455 */     nc.ui.pubapp.pub.power.PowerValidateService bean = new nc.ui.pubapp.pub.power.PowerValidateService();
/*  456 */     context.put("powerUNApproveService", bean);
/*  457 */     bean.setActionCode("unapprove");
/*  458 */     bean.setBillCodeFiledName("vbillcode");
/*  459 */     bean.setPermissionCode("C003");
/*  460 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  461 */     invokeInitializingBean(bean);
/*  462 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.funcnode.ui.action.MenuAction getLinkQueryMenuAction() {
/*  466 */     if (context.get("linkQueryMenuAction") != null)
/*  467 */       return (nc.funcnode.ui.action.MenuAction)context.get("linkQueryMenuAction");
/*  468 */     nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
/*  469 */     context.put("linkQueryMenuAction", bean);
/*  470 */     bean.setCode("linkQueryMenuAction_view");
/*  471 */     bean.setName(getI18nFB_1600f50());
/*  472 */     bean.setActions(getManagedList8());
/*  473 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  474 */     invokeInitializingBean(bean);
/*  475 */     return bean;
/*      */   }
/*      */   
/*      */   private String getI18nFB_1600f50() {
/*  479 */     if (context.get("nc.ui.uif2.I18nFB#1600f50") != null)
/*  480 */       return (String)context.get("nc.ui.uif2.I18nFB#1600f50");
/*  481 */     I18nFB bean = new I18nFB();
/*  482 */     context.put("&nc.ui.uif2.I18nFB#1600f50", bean);bean.setResDir("c010001_0");
/*  483 */     bean.setResId("0c010001-0111");
/*  484 */     bean.setDefaultValue("联查");
/*  485 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  486 */     invokeInitializingBean(bean);
/*      */     try {
/*  488 */       Object product = bean.getObject();
/*  489 */       context.put("nc.ui.uif2.I18nFB#1600f50", product);
/*  490 */       return (String)product;
/*      */     } catch (Exception e) {
/*  492 */       throw new RuntimeException(e); } }
/*      */   
/*  494 */   private List getManagedList8() { List list = new ArrayList();list.add(getLinkBillUIAction());list.add(getSeparatorAction());list.add(getReportBillQryHistAction());list.add(getLinkMainMaterialAction());return list;
/*      */   }
/*      */   
/*  497 */   private nc.scmmm.ui.uif2.actions.SCMLinkQueryAction getLinkBillUIAction() { if (context.get("linkBillUIAction") != null)
/*  498 */       return (nc.scmmm.ui.uif2.actions.SCMLinkQueryAction)context.get("linkBillUIAction");
/*  499 */     nc.scmmm.ui.uif2.actions.SCMLinkQueryAction bean = new nc.scmmm.ui.uif2.actions.SCMLinkQueryAction();
/*  500 */     context.put("linkBillUIAction", bean);
/*  501 */     bean.setModel(getManageAppModel());
/*  502 */     bean.setBillType("C003");
/*  503 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  504 */     invokeInitializingBean(bean);
/*  505 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.action.ReportBillQryHistAction getReportBillQryHistAction() {
/*  509 */     if (context.get("reportBillQryHistAction") != null)
/*  510 */       return (nc.ui.qc.c003.maintain.action.ReportBillQryHistAction)context.get("reportBillQryHistAction");
/*  511 */     nc.ui.qc.c003.maintain.action.ReportBillQryHistAction bean = new nc.ui.qc.c003.maintain.action.ReportBillQryHistAction();
/*  512 */     context.put("reportBillQryHistAction", bean);
/*  513 */     bean.setModel(getManageAppModel());
/*  514 */     bean.setEditor(getBillFormEditor());
/*  515 */     bean.setListViewHis(getHisVersionListView());
/*  516 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  517 */     invokeInitializingBean(bean);
/*  518 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.action.LinkMainMaterialAction getLinkMainMaterialAction() {
/*  522 */     if (context.get("linkMainMaterialAction") != null)
/*  523 */       return (nc.ui.qc.c003.maintain.action.LinkMainMaterialAction)context.get("linkMainMaterialAction");
/*  524 */     nc.ui.qc.c003.maintain.action.LinkMainMaterialAction bean = new nc.ui.qc.c003.maintain.action.LinkMainMaterialAction();
/*  525 */     context.put("linkMainMaterialAction", bean);
/*  526 */     bean.setModel(getManageAppModel());
/*  527 */     bean.setForm(getBillFormEditor());
/*  528 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  529 */     invokeInitializingBean(bean);
/*  530 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.funcnode.ui.action.MenuAction getRelatingMenuAction() {
/*  534 */     if (context.get("relatingMenuAction") != null)
/*  535 */       return (nc.funcnode.ui.action.MenuAction)context.get("relatingMenuAction");
/*  536 */     nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
/*  537 */     context.put("relatingMenuAction", bean);
/*  538 */     bean.setCode("relatingMenuAction");
/*  539 */     bean.setName(getI18nFB_18d5619());
/*  540 */     bean.setActions(getManagedList9());
/*  541 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  542 */     invokeInitializingBean(bean);
/*  543 */     return bean;
/*      */   }
/*      */   
/*      */   private String getI18nFB_18d5619() {
/*  547 */     if (context.get("nc.ui.uif2.I18nFB#18d5619") != null)
/*  548 */       return (String)context.get("nc.ui.uif2.I18nFB#18d5619");
/*  549 */     I18nFB bean = new I18nFB();
/*  550 */     context.put("&nc.ui.uif2.I18nFB#18d5619", bean);bean.setResDir("c010001_0");
/*  551 */     bean.setResId("0c010001-0112");
/*  552 */     bean.setDefaultValue("关联功能");
/*  553 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  554 */     invokeInitializingBean(bean);
/*      */     try {
/*  556 */       Object product = bean.getObject();
/*  557 */       context.put("nc.ui.uif2.I18nFB#18d5619", product);
/*  558 */       return (String)product;
/*      */     } catch (Exception e) {
/*  560 */       throw new RuntimeException(e); } }
/*      */   
/*  562 */   private List getManagedList9() { List list = new ArrayList();list.add(getCreateRejectBillUIAction());list.add(getRecheckUIAction());return list;
/*      */   }
/*      */   
/*  565 */   private nc.ui.qc.c003.maintain.action.CreateRejectBillUIAction getCreateRejectBillUIAction() { if (context.get("createRejectBillUIAction") != null)
/*  566 */       return (nc.ui.qc.c003.maintain.action.CreateRejectBillUIAction)context.get("createRejectBillUIAction");
/*  567 */     nc.ui.qc.c003.maintain.action.CreateRejectBillUIAction bean = new nc.ui.qc.c003.maintain.action.CreateRejectBillUIAction();
/*  568 */     context.put("createRejectBillUIAction", bean);
/*  569 */     bean.setForm(getBillFormEditor());
/*  570 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  571 */     invokeInitializingBean(bean);
/*  572 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.action.RecheckUIAction getRecheckUIAction() {
/*  576 */     if (context.get("recheckUIAction") != null)
/*  577 */       return (nc.ui.qc.c003.maintain.action.RecheckUIAction)context.get("recheckUIAction");
/*  578 */     nc.ui.qc.c003.maintain.action.RecheckUIAction bean = new nc.ui.qc.c003.maintain.action.RecheckUIAction();
/*  579 */     context.put("recheckUIAction", bean);
/*  580 */     bean.setModel(getManageAppModel());
/*  581 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  582 */     invokeInitializingBean(bean);
/*  583 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.funcnode.ui.action.MenuAction getAccessoriesMenuAction() {
/*  587 */     if (context.get("accessoriesMenuAction") != null)
/*  588 */       return (nc.funcnode.ui.action.MenuAction)context.get("accessoriesMenuAction");
/*  589 */     nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
/*  590 */     context.put("accessoriesMenuAction", bean);
/*  591 */     bean.setCode("accessoriesMenuAction");
/*  592 */     bean.setName(getI18nFB_fa0d3());
/*  593 */     bean.setActions(getManagedList10());
/*  594 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  595 */     invokeInitializingBean(bean);
/*  596 */     return bean;
/*      */   }
/*      */   
/*      */   private String getI18nFB_fa0d3() {
/*  600 */     if (context.get("nc.ui.uif2.I18nFB#fa0d3") != null)
/*  601 */       return (String)context.get("nc.ui.uif2.I18nFB#fa0d3");
/*  602 */     I18nFB bean = new I18nFB();
/*  603 */     context.put("&nc.ui.uif2.I18nFB#fa0d3", bean);bean.setResDir("c010001_0");
/*  604 */     bean.setResId("0c010001-0113");
/*  605 */     bean.setDefaultValue("辅助功能");
/*  606 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  607 */     invokeInitializingBean(bean);
/*      */     try {
/*  609 */       Object product = bean.getObject();
/*  610 */       context.put("nc.ui.uif2.I18nFB#fa0d3", product);
/*  611 */       return (String)product;
/*      */     } catch (Exception e) {
/*  613 */       throw new RuntimeException(e); } }
/*      */   
/*  615 */   private List getManagedList10() { List list = new ArrayList();list.add(getAccessoriesAction());return list;
/*      */   }
/*      */   
/*  618 */   private nc.ui.pubapp.uif2app.actions.FileDocManageAction getAccessoriesAction() { if (context.get("accessoriesAction") != null)
/*  619 */       return (nc.ui.pubapp.uif2app.actions.FileDocManageAction)context.get("accessoriesAction");
/*  620 */     nc.ui.pubapp.uif2app.actions.FileDocManageAction bean = new nc.ui.pubapp.uif2app.actions.FileDocManageAction();
/*  621 */     context.put("accessoriesAction", bean);
/*  622 */     bean.setModel(getManageAppModel());
/*  623 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  624 */     invokeInitializingBean(bean);
/*  625 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.util.ReportPrintProcesser getPrintProcessor() {
/*  629 */     if (context.get("printProcessor") != null)
/*  630 */       return (nc.ui.qc.c003.maintain.util.ReportPrintProcesser)context.get("printProcessor");
/*  631 */     nc.ui.qc.c003.maintain.util.ReportPrintProcesser bean = new nc.ui.qc.c003.maintain.util.ReportPrintProcesser();
/*  632 */     context.put("printProcessor", bean);
/*  633 */     bean.setModel(getManageAppModel());
/*  634 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  635 */     invokeInitializingBean(bean);
/*  636 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.funcnode.ui.action.GroupAction getPrintMenuAction() {
/*  640 */     if (context.get("printMenuAction") != null)
/*  641 */       return (nc.funcnode.ui.action.GroupAction)context.get("printMenuAction");
/*  642 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/*  643 */     context.put("printMenuAction", bean);
/*  644 */     bean.setCode("printMenuAction");
/*  645 */     bean.setActions(getManagedList11());
/*  646 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  647 */     invokeInitializingBean(bean);
/*  648 */     return bean;
/*      */   }
/*      */   
/*  651 */   private List getManagedList11() { List list = new ArrayList();list.add(getPrintAction());list.add(getPreviewAction());list.add(getOutputAction());list.add(getSeparatorAction());list.add(getPrintCertBillAction());list.add(getSeparatorAction());list.add(getPrintCountQueryAction());return list;
/*      */   }
/*      */   
/*  654 */   public nc.ui.qc.c003.maintain.action.PrintReportBillAction getPrintAction() { if (context.get("printAction") != null)
/*  655 */       return (nc.ui.qc.c003.maintain.action.PrintReportBillAction)context.get("printAction");
/*  656 */     nc.ui.qc.c003.maintain.action.PrintReportBillAction bean = new nc.ui.qc.c003.maintain.action.PrintReportBillAction();
/*  657 */     context.put("printAction", bean);
/*  658 */     bean.setActionType("print");
/*  659 */     bean.setModel(getManageAppModel());
/*  660 */     bean.setBeforePrintDataProcess(getPrintProcessor());
/*  661 */     bean.setEditor(getBillFormEditor());
/*  662 */     bean.setListView(getListView());
/*  663 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  664 */     invokeInitializingBean(bean);
/*  665 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.PrintReportBillAction getPreviewAction() {
/*  669 */     if (context.get("previewAction") != null)
/*  670 */       return (nc.ui.qc.c003.maintain.action.PrintReportBillAction)context.get("previewAction");
/*  671 */     nc.ui.qc.c003.maintain.action.PrintReportBillAction bean = new nc.ui.qc.c003.maintain.action.PrintReportBillAction();
/*  672 */     context.put("previewAction", bean);
/*  673 */     bean.setActionType("preview");
/*  674 */     bean.setModel(getManageAppModel());
/*  675 */     bean.setBeforePrintDataProcess(getPrintProcessor());
/*  676 */     bean.setEditor(getBillFormEditor());
/*  677 */     bean.setListView(getListView());
/*  678 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  679 */     invokeInitializingBean(bean);
/*  680 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.PrintReportBillAction getOutputAction() {
/*  684 */     if (context.get("outputAction") != null)
/*  685 */       return (nc.ui.qc.c003.maintain.action.PrintReportBillAction)context.get("outputAction");
/*  686 */     nc.ui.qc.c003.maintain.action.PrintReportBillAction bean = new nc.ui.qc.c003.maintain.action.PrintReportBillAction();
/*  687 */     context.put("outputAction", bean);
/*  688 */     bean.setActionType("output");
/*  689 */     bean.setModel(getManageAppModel());
/*  690 */     bean.setBeforePrintDataProcess(getPrintProcessor());
/*  691 */     bean.setEditor(getBillFormEditor());
/*  692 */     bean.setListView(getListView());
/*  693 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  694 */     invokeInitializingBean(bean);
/*  695 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.PrintCertBillAction getPrintCertBillAction() {
/*  699 */     if (context.get("printCertBillAction") != null)
/*  700 */       return (nc.ui.qc.c003.maintain.action.PrintCertBillAction)context.get("printCertBillAction");
/*  701 */     nc.ui.qc.c003.maintain.action.PrintCertBillAction bean = new nc.ui.qc.c003.maintain.action.PrintCertBillAction();
/*  702 */     context.put("printCertBillAction", bean);
/*  703 */     bean.setBtnName(getI18nFB_7b518());
/*  704 */     bean.setModel(getManageAppModel());
/*  705 */     bean.setEditor(getBillFormEditor());
/*  706 */     bean.setCode("printCertBillAction");
/*  707 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  708 */     invokeInitializingBean(bean);
/*  709 */     return bean;
/*      */   }
/*      */   
/*      */   private String getI18nFB_7b518() {
/*  713 */     if (context.get("nc.ui.uif2.I18nFB#7b518") != null)
/*  714 */       return (String)context.get("nc.ui.uif2.I18nFB#7b518");
/*  715 */     I18nFB bean = new I18nFB();
/*  716 */     context.put("&nc.ui.uif2.I18nFB#7b518", bean);bean.setResDir("c010001_0");
/*  717 */     bean.setResId("0c010001-0115");
/*  718 */     bean.setDefaultValue("打印质证书");
/*  719 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  720 */     invokeInitializingBean(bean);
/*      */     try {
/*  722 */       Object product = bean.getObject();
/*  723 */       context.put("nc.ui.uif2.I18nFB#7b518", product);
/*  724 */       return (String)product;
/*      */     } catch (Exception e) {
/*  726 */       throw new RuntimeException(e);
/*      */     } }
/*      */   
/*  729 */   public nc.ui.scmpub.action.SCMPrintCountQueryAction getPrintCountQueryAction() { if (context.get("printCountQueryAction") != null)
/*  730 */       return (nc.ui.scmpub.action.SCMPrintCountQueryAction)context.get("printCountQueryAction");
/*  731 */     nc.ui.scmpub.action.SCMPrintCountQueryAction bean = new nc.ui.scmpub.action.SCMPrintCountQueryAction();
/*  732 */     context.put("printCountQueryAction", bean);
/*  733 */     bean.setModel(getManageAppModel());
/*  734 */     bean.setBilldateFieldName("dreportdate");
/*  735 */     bean.setBillType("C003");
/*  736 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  737 */     invokeInitializingBean(bean);
/*  738 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getPasteClearItem() {
/*  742 */     if (context.get("pasteClearItem") != null)
/*  743 */       return (ArrayList)context.get("pasteClearItem");
/*  744 */     ArrayList bean = new ArrayList(getManagedList12());context.put("pasteClearItem", bean);
/*  745 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  746 */     invokeInitializingBean(bean);
/*  747 */     return bean;
/*      */   }
/*      */   
/*  750 */   private List getManagedList12() { List list = new ArrayList();list.add("pk_reportbill_b");list.add("ts");return list;
/*      */   }
/*      */   
/*  753 */   public nc.ui.qc.c003.maintain.action.ReportBillReturnAction getReturnAction() { if (context.get("returnAction") != null)
/*  754 */       return (nc.ui.qc.c003.maintain.action.ReportBillReturnAction)context.get("returnAction");
/*  755 */     nc.ui.qc.c003.maintain.action.ReportBillReturnAction bean = new nc.ui.qc.c003.maintain.action.ReportBillReturnAction();
/*  756 */     context.put("returnAction", bean);
/*  757 */     bean.setListView(getListView());
/*  758 */     bean.setCode("returnAction");
/*  759 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  760 */     invokeInitializingBean(bean);
/*  761 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.ReportBodyDelLineAction getBodyDelLineAction() {
/*  765 */     if (context.get("bodyDelLineAction") != null)
/*  766 */       return (nc.ui.qc.c003.maintain.action.ReportBodyDelLineAction)context.get("bodyDelLineAction");
/*  767 */     nc.ui.qc.c003.maintain.action.ReportBodyDelLineAction bean = new nc.ui.qc.c003.maintain.action.ReportBodyDelLineAction();
/*  768 */     context.put("bodyDelLineAction", bean);
/*  769 */     bean.setModel(getManageAppModel());
/*  770 */     bean.setBillFormEditor(getBillFormEditor());
/*  771 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  772 */     invokeInitializingBean(bean);
/*  773 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.ReportBodyCopyLineAction getBodyCopyLineAction() {
/*  777 */     if (context.get("bodyCopyLineAction") != null)
/*  778 */       return (nc.ui.qc.c003.maintain.action.ReportBodyCopyLineAction)context.get("bodyCopyLineAction");
/*  779 */     nc.ui.qc.c003.maintain.action.ReportBodyCopyLineAction bean = new nc.ui.qc.c003.maintain.action.ReportBodyCopyLineAction();
/*  780 */     context.put("bodyCopyLineAction", bean);
/*  781 */     bean.setModel(getManageAppModel());
/*  782 */     bean.setBillFormEditor(getBillFormEditor());
/*  783 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  784 */     invokeInitializingBean(bean);
/*  785 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.ReportBodyPasteLineAction getBodyPasteLineAction() {
/*  789 */     if (context.get("bodyPasteLineAction") != null)
/*  790 */       return (nc.ui.qc.c003.maintain.action.ReportBodyPasteLineAction)context.get("bodyPasteLineAction");
/*  791 */     nc.ui.qc.c003.maintain.action.ReportBodyPasteLineAction bean = new nc.ui.qc.c003.maintain.action.ReportBodyPasteLineAction();
/*  792 */     context.put("bodyPasteLineAction", bean);
/*  793 */     bean.setClearItems(getPasteClearItem());
/*  794 */     bean.setModel(getManageAppModel());
/*  795 */     bean.setBillFormEditor(getBillFormEditor());
/*  796 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  797 */     invokeInitializingBean(bean);
/*  798 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.ReportBodyPasteToTailAction getBodyPasteToTailAction() {
/*  802 */     if (context.get("bodyPasteToTailAction") != null)
/*  803 */       return (nc.ui.qc.c003.maintain.action.ReportBodyPasteToTailAction)context.get("bodyPasteToTailAction");
/*  804 */     nc.ui.qc.c003.maintain.action.ReportBodyPasteToTailAction bean = new nc.ui.qc.c003.maintain.action.ReportBodyPasteToTailAction();
/*  805 */     context.put("bodyPasteToTailAction", bean);
/*  806 */     bean.setClearItems(getPasteClearItem());
/*  807 */     bean.setModel(getManageAppModel());
/*  808 */     bean.setBillFormEditor(getBillFormEditor());
/*  809 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  810 */     invokeInitializingBean(bean);
/*  811 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.action.ReportCheckInfoAction getCheckInfoAction() {
/*  815 */     if (context.get("checkInfoAction") != null)
/*  816 */       return (nc.ui.qc.c003.maintain.action.ReportCheckInfoAction)context.get("checkInfoAction");
/*  817 */     nc.ui.qc.c003.maintain.action.ReportCheckInfoAction bean = new nc.ui.qc.c003.maintain.action.ReportCheckInfoAction();
/*  818 */     context.put("checkInfoAction", bean);
/*  819 */     bean.setModel(getManageAppModel());
/*  820 */     bean.setEditor(getBillFormEditor());
/*  821 */     bean.setDataManager(getInitDataManager());
/*  822 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  823 */     invokeInitializingBean(bean);
/*  824 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler getSelectionChangedHandlerForHis() {
/*  828 */     if (context.get("selectionChangedHandlerForHis") != null)
/*  829 */       return (nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler)context.get("selectionChangedHandlerForHis");
/*  830 */     nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler bean = new nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler();
/*  831 */     context.put("selectionChangedHandlerForHis", bean);
/*  832 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  833 */     invokeInitializingBean(bean);
/*  834 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler getSelectionChangedHandler() {
/*  838 */     if (context.get("selectionChangedHandler") != null)
/*  839 */       return (nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler)context.get("selectionChangedHandler");
/*  840 */     nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler bean = new nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler();
/*  841 */     context.put("selectionChangedHandler", bean);
/*  842 */     bean.setShowUpableBillListView(getListView());
/*  843 */     bean.setShowUpableBillForm(getBillFormEditor());
/*  844 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  845 */     invokeInitializingBean(bean);
/*  846 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.handler.head.ReportBillOrgHandler getOrganization() {
/*  850 */     if (context.get("Organization") != null)
/*  851 */       return (nc.ui.qc.c003.maintain.handler.head.ReportBillOrgHandler)context.get("Organization");
/*  852 */     nc.ui.qc.c003.maintain.handler.head.ReportBillOrgHandler bean = new nc.ui.qc.c003.maintain.handler.head.ReportBillOrgHandler();
/*  853 */     context.put("Organization", bean);
/*  854 */     bean.setEditor(getBillFormEditor());
/*  855 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  856 */     invokeInitializingBean(bean);
/*  857 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.model.AppEventHandlerMediator getAppEventHandlerMediator() {
/*  861 */     if (context.get("appEventHandlerMediator") != null)
/*  862 */       return (nc.ui.pubapp.uif2app.model.AppEventHandlerMediator)context.get("appEventHandlerMediator");
/*  863 */     nc.ui.pubapp.uif2app.model.AppEventHandlerMediator bean = new nc.ui.pubapp.uif2app.model.AppEventHandlerMediator();
/*  864 */     context.put("appEventHandlerMediator", bean);
/*  865 */     bean.setModel(getManageAppModel());
/*  866 */     bean.setHandlerGroup(getManagedList13());
/*  867 */     bean.setHandlerMap(getManagedMap0());
/*  868 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  869 */     invokeInitializingBean(bean);
/*  870 */     return bean;
/*      */   }
/*      */   
/*  873 */   private List getManagedList13() { List list = new ArrayList();list.add(getEventHandlerGroup_1aa6e96());list.add(getEventHandlerGroup_1c2ebe());list.add(getEventHandlerGroup_1f6c1e0());list.add(getEventHandlerGroup_1338036());list.add(getEventHandlerGroup_269243());return list;
/*      */   }
/*      */   
/*  876 */   private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_1aa6e96() { if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1aa6e96") != null)
/*  877 */       return (nc.ui.pubapp.uif2app.event.EventHandlerGroup)context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1aa6e96");
/*  878 */     nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
/*  879 */     context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1aa6e96", bean);
/*  880 */     bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent");
/*  881 */     bean.setHandler(getBodyBeforeEditHandler_19f6cf5());
/*  882 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  883 */     invokeInitializingBean(bean);
/*  884 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.handler.body.BodyBeforeEditHandler getBodyBeforeEditHandler_19f6cf5() {
/*  888 */     if (context.get("nc.ui.qc.c003.maintain.handler.body.BodyBeforeEditHandler#19f6cf5") != null)
/*  889 */       return (nc.ui.qc.c003.maintain.handler.body.BodyBeforeEditHandler)context.get("nc.ui.qc.c003.maintain.handler.body.BodyBeforeEditHandler#19f6cf5");
/*  890 */     nc.ui.qc.c003.maintain.handler.body.BodyBeforeEditHandler bean = new nc.ui.qc.c003.maintain.handler.body.BodyBeforeEditHandler();
/*  891 */     context.put("nc.ui.qc.c003.maintain.handler.body.BodyBeforeEditHandler#19f6cf5", bean);
/*  892 */     bean.setContext(getContext());
/*  893 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  894 */     invokeInitializingBean(bean);
/*  895 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_1c2ebe() {
/*  899 */     if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1c2ebe") != null)
/*  900 */       return (nc.ui.pubapp.uif2app.event.EventHandlerGroup)context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1c2ebe");
/*  901 */     nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
/*  902 */     context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1c2ebe", bean);
/*  903 */     bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent");
/*  904 */     bean.setHandler(getBodyAfterEditHandler_423ab4());
/*  905 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  906 */     invokeInitializingBean(bean);
/*  907 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.handler.body.BodyAfterEditHandler getBodyAfterEditHandler_423ab4() {
/*  911 */     if (context.get("nc.ui.qc.c003.maintain.handler.body.BodyAfterEditHandler#423ab4") != null)
/*  912 */       return (nc.ui.qc.c003.maintain.handler.body.BodyAfterEditHandler)context.get("nc.ui.qc.c003.maintain.handler.body.BodyAfterEditHandler#423ab4");
/*  913 */     nc.ui.qc.c003.maintain.handler.body.BodyAfterEditHandler bean = new nc.ui.qc.c003.maintain.handler.body.BodyAfterEditHandler();
/*  914 */     context.put("nc.ui.qc.c003.maintain.handler.body.BodyAfterEditHandler#423ab4", bean);
/*  915 */     bean.setContext(getContext());
/*  916 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  917 */     invokeInitializingBean(bean);
/*  918 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_1f6c1e0() {
/*  922 */     if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1f6c1e0") != null)
/*  923 */       return (nc.ui.pubapp.uif2app.event.EventHandlerGroup)context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1f6c1e0");
/*  924 */     nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
/*  925 */     context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1f6c1e0", bean);
/*  926 */     bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent");
/*  927 */     bean.setHandler(getHeadTailBeforeEditHandler_40bc00());
/*  928 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  929 */     invokeInitializingBean(bean);
/*  930 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.handler.head.HeadTailBeforeEditHandler getHeadTailBeforeEditHandler_40bc00() {
/*  934 */     if (context.get("nc.ui.qc.c003.maintain.handler.head.HeadTailBeforeEditHandler#40bc00") != null)
/*  935 */       return (nc.ui.qc.c003.maintain.handler.head.HeadTailBeforeEditHandler)context.get("nc.ui.qc.c003.maintain.handler.head.HeadTailBeforeEditHandler#40bc00");
/*  936 */     nc.ui.qc.c003.maintain.handler.head.HeadTailBeforeEditHandler bean = new nc.ui.qc.c003.maintain.handler.head.HeadTailBeforeEditHandler();
/*  937 */     context.put("nc.ui.qc.c003.maintain.handler.head.HeadTailBeforeEditHandler#40bc00", bean);
/*  938 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  939 */     invokeInitializingBean(bean);
/*  940 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_1338036() {
/*  944 */     if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1338036") != null)
/*  945 */       return (nc.ui.pubapp.uif2app.event.EventHandlerGroup)context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1338036");
/*  946 */     nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
/*  947 */     context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1338036", bean);
/*  948 */     bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent");
/*  949 */     bean.setHandler(getHeadTailAfterEditHandler_132928d());
/*  950 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  951 */     invokeInitializingBean(bean);
/*  952 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.handler.head.HeadTailAfterEditHandler getHeadTailAfterEditHandler_132928d() {
/*  956 */     if (context.get("nc.ui.qc.c003.maintain.handler.head.HeadTailAfterEditHandler#132928d") != null)
/*  957 */       return (nc.ui.qc.c003.maintain.handler.head.HeadTailAfterEditHandler)context.get("nc.ui.qc.c003.maintain.handler.head.HeadTailAfterEditHandler#132928d");
/*  958 */     nc.ui.qc.c003.maintain.handler.head.HeadTailAfterEditHandler bean = new nc.ui.qc.c003.maintain.handler.head.HeadTailAfterEditHandler();
/*  959 */     context.put("nc.ui.qc.c003.maintain.handler.head.HeadTailAfterEditHandler#132928d", bean);
/*  960 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  961 */     invokeInitializingBean(bean);
/*  962 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_269243() {
/*  966 */     if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#269243") != null)
/*  967 */       return (nc.ui.pubapp.uif2app.event.EventHandlerGroup)context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#269243");
/*  968 */     nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
/*  969 */     context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#269243", bean);
/*  970 */     bean.setEvent("nc.ui.uif2.AppEvent");
/*  971 */     bean.setHandler(getSelectionChangedHandler());
/*  972 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  973 */     invokeInitializingBean(bean);
/*  974 */     return bean;
/*      */   }
/*      */   
/*  977 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("nc.ui.pubapp.uif2app.event.card.CardPanelLoadEvent", getManagedList14());map.put("nc.ui.pubapp.uif2app.event.OrgChangedEvent", getManagedList15());return map; }
/*      */   
/*  979 */   private List getManagedList14() { List list = new ArrayList();list.add(getScmCardLoadhandler());return list; }
/*      */   
/*  981 */   private List getManagedList15() { List list = new ArrayList();list.add(getOrganization());return list;
/*      */   }
/*      */   
/*  984 */   public nc.ui.scmpub.listener.ScmCardItemsFillableHandler getScmCardLoadhandler() { if (context.get("scmCardLoadhandler") != null)
/*  985 */       return (nc.ui.scmpub.listener.ScmCardItemsFillableHandler)context.get("scmCardLoadhandler");
/*  986 */     nc.ui.scmpub.listener.ScmCardItemsFillableHandler bean = new nc.ui.scmpub.listener.ScmCardItemsFillableHandler();
/*  987 */     context.put("scmCardLoadhandler", bean);
/*  988 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  989 */     invokeInitializingBean(bean);
/*  990 */     return bean;
/*      */   }
/*      */   
/*      */   public ReportDefaultFuncNodeInitDataListener getInitDataListener() {
/*  994 */     if (context.get("InitDataListener") != null)
/*  995 */       return (ReportDefaultFuncNodeInitDataListener)context.get("InitDataListener");
/*  996 */     ReportDefaultFuncNodeInitDataListener bean = new ReportDefaultFuncNodeInitDataListener();
/*  997 */     context.put("InitDataListener", bean);
/*  998 */     bean.setContext(getContext());
/*  999 */     bean.setModel(getManageAppModel());
/* 1000 */     bean.setQueryAction(getQueryUIAction());
/* 1001 */     bean.setVoClassName("nc.vo.qc.c003.entity.ReportVO");
/* 1002 */     bean.setAutoShowUpComponent(getBillFormEditor());
/* 1003 */     bean.setProcessorMap(getManagedMap1());
/* 1004 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1005 */     invokeInitializingBean(bean);
/* 1006 */     return bean;
/*      */   }
/*      */   
/* 1009 */   private Map getManagedMap1() { Map map = new java.util.HashMap();map.put("30", getInitDataProcessorForLink_89ca78());return map;
/*      */   }
/*      */   
/* 1012 */   private InitDataProcessorForLink getInitDataProcessorForLink_89ca78() { if (context.get("nc.ui.qc.c003.maintain.view.InitDataProcessorForLink#89ca78") != null)
/* 1013 */       return (InitDataProcessorForLink)context.get("nc.ui.qc.c003.maintain.view.InitDataProcessorForLink#89ca78");
/* 1014 */     InitDataProcessorForLink bean = new InitDataProcessorForLink();
/* 1015 */     context.put("nc.ui.qc.c003.maintain.view.InitDataProcessorForLink#89ca78", bean);
/* 1016 */     bean.setModel(getManageAppModel());
/* 1017 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1018 */     invokeInitializingBean(bean);
/* 1019 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.vo.uif2.LoginContext getContext() {
/* 1023 */     if (context.get("context") != null)
/* 1024 */       return (nc.vo.uif2.LoginContext)context.get("context");
/* 1025 */     nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
/* 1026 */     context.put("context", bean);
/* 1027 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1028 */     invokeInitializingBean(bean);
/* 1029 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory getBoadatorfactory() {
/* 1033 */     if (context.get("boadatorfactory") != null)
/* 1034 */       return (nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory)context.get("boadatorfactory");
/* 1035 */     nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory bean = new nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory();
/* 1036 */     context.put("boadatorfactory", bean);
/* 1037 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1038 */     invokeInitializingBean(bean);
/* 1039 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.query.ReportModelService getManageModelService() {
/* 1043 */     if (context.get("manageModelService") != null)
/* 1044 */       return (nc.ui.qc.c003.query.ReportModelService)context.get("manageModelService");
/* 1045 */     nc.ui.qc.c003.query.ReportModelService bean = new nc.ui.qc.c003.query.ReportModelService();
/* 1046 */     context.put("manageModelService", bean);
/* 1047 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1048 */     invokeInitializingBean(bean);
/* 1049 */     return bean;
/*      */   }
/*      */   
/*      */   public ReportBillManageModel getManageAppModel() {
/* 1053 */     if (context.get("manageAppModel") != null)
/* 1054 */       return (ReportBillManageModel)context.get("manageAppModel");
/* 1055 */     ReportBillManageModel bean = new ReportBillManageModel();
/* 1056 */     context.put("manageAppModel", bean);
/* 1057 */     bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
/* 1058 */     bean.setContext(getContext());
/* 1059 */     bean.setBillType("C003");
/* 1060 */     bean.setService(getManageModelService());
/* 1061 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1062 */     invokeInitializingBean(bean);
/* 1063 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.model.BaseBillModelDataManager getModelDataManager() {
/* 1067 */     if (context.get("modelDataManager") != null)
/* 1068 */       return (nc.ui.pubapp.uif2app.model.BaseBillModelDataManager)context.get("modelDataManager");
/* 1069 */     nc.ui.pubapp.uif2app.model.BaseBillModelDataManager bean = new nc.ui.pubapp.uif2app.model.BaseBillModelDataManager();
/* 1070 */     context.put("modelDataManager", bean);
/* 1071 */     bean.setModel(getManageAppModel());
/* 1072 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1073 */     invokeInitializingBean(bean);
/* 1074 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.scmpub.page.model.SCMBillPageModelDataManager getInitDataManager() {
/* 1078 */     if (context.get("initDataManager") != null)
/* 1079 */       return (nc.ui.scmpub.page.model.SCMBillPageModelDataManager)context.get("initDataManager");
/* 1080 */     nc.ui.scmpub.page.model.SCMBillPageModelDataManager bean = new nc.ui.scmpub.page.model.SCMBillPageModelDataManager();
/* 1081 */     context.put("initDataManager", bean);
/* 1082 */     bean.setModel(getManageAppModel());
/* 1083 */     bean.setService(getManageModelService());
/* 1084 */     bean.setPageQuery(getPageQuery());
/* 1085 */     bean.setPageDelegator(getPageDelegator());
/* 1086 */     bean.setPagePanel(getQueryInfo());
/* 1087 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1088 */     invokeInitializingBean(bean);
/* 1089 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.view.TemplateContainer getTemplateContainer() {
/* 1093 */     if (context.get("templateContainer") != null)
/* 1094 */       return (nc.ui.pubapp.uif2app.view.TemplateContainer)context.get("templateContainer");
/* 1095 */     nc.ui.pubapp.uif2app.view.TemplateContainer bean = new nc.ui.pubapp.uif2app.view.TemplateContainer();
/* 1096 */     context.put("templateContainer", bean);
/* 1097 */     bean.setContext(getContext());
/* 1098 */     bean.setBillTemplateMender(getTrantypeTempMender());
/* 1099 */     bean.load();
/* 1100 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1101 */     invokeInitializingBean(bean);
/* 1102 */     return bean;
/*      */   }
/*      */   
/*      */   public ReportBillListView getListView() {
/* 1106 */     if (context.get("listView") != null)
/* 1107 */       return (ReportBillListView)context.get("listView");
/* 1108 */     ReportBillListView bean = new ReportBillListView();
/* 1109 */     context.put("listView", bean);
/* 1110 */     bean.setModel(getManageAppModel());
/* 1111 */     bean.setMultiSelectionMode(0);
/* 1112 */     bean.setTemplateContainer(getTemplateContainer());
/* 1113 */     bean.setUserdefitemListPreparator(getUserdefAndMarAsstListPreparator());
/* 1114 */     bean.setPaginationBar(getPageBar());
/* 1115 */     bean.initUI();
/* 1116 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1117 */     invokeInitializingBean(bean);
/* 1118 */     return bean;
/*      */   }
/*      */   
/*      */   public ReportBillForm getBillFormEditor() {
/* 1122 */     if (context.get("billFormEditor") != null)
/* 1123 */       return (ReportBillForm)context.get("billFormEditor");
/* 1124 */     ReportBillForm bean = new ReportBillForm();
/* 1125 */     context.put("billFormEditor", bean);
/* 1126 */     bean.setModel(getManageAppModel());
/* 1127 */     bean.setTemplateContainer(getTemplateContainer());
/* 1128 */     bean.setTemplateNotNullValidate(true);
/* 1129 */     bean.setAutoAddLine(false);
/* 1130 */     bean.setUserdefitemPreparator(getUserdefAndMarAsstCardPreparator());
/* 1131 */     bean.setBlankChildrenFilter(getSingleFieldBlankChildrenFilter_4bc2ec());
/* 1132 */     bean.setBodyActionMap(getManagedMap2());
/* 1133 */     bean.initUI();
/* 1134 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1135 */     invokeInitializingBean(bean);
/* 1136 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter getSingleFieldBlankChildrenFilter_4bc2ec() {
/* 1140 */     if (context.get("nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter#4bc2ec") != null)
/* 1141 */       return (nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter)context.get("nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter#4bc2ec");
/* 1142 */     nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter bean = new nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter();
/* 1143 */     context.put("nc.ui.pubapp.uif2app.view.value.SingleFieldBlankChildrenFilter#4bc2ec", bean);
/* 1144 */     bean.setFieldName("nnum");
/* 1145 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1146 */     invokeInitializingBean(bean);
/* 1147 */     return bean;
/*      */   }
/*      */   
/* 1150 */   private Map getManagedMap2() { Map map = new java.util.HashMap();map.put("report_b", getManagedList16());map.put("tab_iteminfo", getManagedList17());map.put("tab_sampleinfo", getManagedList18());map.put("tab_checkkinfo", getManagedList19());return map; }
/*      */   
/* 1152 */   private List getManagedList16() { List list = new ArrayList();list.add(getBodyDelLineAction());list.add(getBodyCopyLineAction());list.add(getBodyPasteLineAction());list.add(getBodyPasteToTailAction());list.add(getActionsBar_ActionsBarSeparator_1ea53b4());list.add(getRearrangeRowNoBodyLineAction_11d786f());list.add(getActionsBar_ActionsBarSeparator_1cdf866());list.add(getDefaultBodyZoomAction_16f2dfc());return list;
/*      */   }
/*      */   
/* 1155 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1ea53b4() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1ea53b4") != null)
/* 1156 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1ea53b4");
/* 1157 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 1158 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1ea53b4", bean);
/* 1159 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1160 */     invokeInitializingBean(bean);
/* 1161 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getRearrangeRowNoBodyLineAction_11d786f() {
/* 1165 */     if (context.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#11d786f") != null)
/* 1166 */       return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction)context.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#11d786f");
/* 1167 */     nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
/* 1168 */     context.put("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#11d786f", bean);
/* 1169 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1170 */     invokeInitializingBean(bean);
/* 1171 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1cdf866() {
/* 1175 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1cdf866") != null)
/* 1176 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1cdf866");
/* 1177 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 1178 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1cdf866", bean);
/* 1179 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1180 */     invokeInitializingBean(bean);
/* 1181 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction getDefaultBodyZoomAction_16f2dfc() {
/* 1185 */     if (context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#16f2dfc") != null)
/* 1186 */       return (nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction)context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#16f2dfc");
/* 1187 */     nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction bean = new nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction();
/* 1188 */     context.put("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#16f2dfc", bean);
/* 1189 */     bean.setPos(1);
/* 1190 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1191 */     invokeInitializingBean(bean);
/* 1192 */     return bean;
/*      */   }
/*      */   
/* 1195 */   private List getManagedList17() { List list = new ArrayList();list.add(getActionsBar_ActionsBarSeparator_343699());list.add(getDefaultBodyZoomAction_1ec7b04());return list;
/*      */   }
/*      */   
/* 1198 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_343699() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#343699") != null)
/* 1199 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#343699");
/* 1200 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 1201 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#343699", bean);
/* 1202 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1203 */     invokeInitializingBean(bean);
/* 1204 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction getDefaultBodyZoomAction_1ec7b04() {
/* 1208 */     if (context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#1ec7b04") != null)
/* 1209 */       return (nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction)context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#1ec7b04");
/* 1210 */     nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction bean = new nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction();
/* 1211 */     context.put("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#1ec7b04", bean);
/* 1212 */     bean.setPos(1);
/* 1213 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1214 */     invokeInitializingBean(bean);
/* 1215 */     return bean;
/*      */   }
/*      */   
/* 1218 */   private List getManagedList18() { List list = new ArrayList();list.add(getActionsBar_ActionsBarSeparator_8051ee());list.add(getDefaultBodyZoomAction_83025d());return list;
/*      */   }
/*      */   
/* 1221 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_8051ee() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8051ee") != null)
/* 1222 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8051ee");
/* 1223 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 1224 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8051ee", bean);
/* 1225 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1226 */     invokeInitializingBean(bean);
/* 1227 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction getDefaultBodyZoomAction_83025d() {
/* 1231 */     if (context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#83025d") != null)
/* 1232 */       return (nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction)context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#83025d");
/* 1233 */     nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction bean = new nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction();
/* 1234 */     context.put("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#83025d", bean);
/* 1235 */     bean.setPos(1);
/* 1236 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1237 */     invokeInitializingBean(bean);
/* 1238 */     return bean;
/*      */   }
/*      */   
/* 1241 */   private List getManagedList19() { List list = new ArrayList();list.add(getActionsBar_ActionsBarSeparator_1106474());list.add(getDefaultBodyZoomAction_1330573());return list;
/*      */   }
/*      */   
/* 1244 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1106474() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1106474") != null)
/* 1245 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1106474");
/* 1246 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 1247 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1106474", bean);
/* 1248 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1249 */     invokeInitializingBean(bean);
/* 1250 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction getDefaultBodyZoomAction_1330573() {
/* 1254 */     if (context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#1330573") != null)
/* 1255 */       return (nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction)context.get("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#1330573");
/* 1256 */     nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction bean = new nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction();
/* 1257 */     context.put("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction#1330573", bean);
/* 1258 */     bean.setPos(1);
/* 1259 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1260 */     invokeInitializingBean(bean);
/* 1261 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator getMouseClickShowPanelMediator() {
/* 1265 */     if (context.get("mouseClickShowPanelMediator") != null)
/* 1266 */       return (nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator)context.get("mouseClickShowPanelMediator");
/* 1267 */     nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator bean = new nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator();
/* 1268 */     context.put("mouseClickShowPanelMediator", bean);
/* 1269 */     bean.setListView(getListView());
/* 1270 */     bean.setShowUpComponent(getBillFormEditor());
/* 1271 */     bean.setHyperLinkColumn("vbillcode");
/* 1272 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1273 */     invokeInitializingBean(bean);
/* 1274 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.common.validateservice.ClosingCheck getClosingListener() {
/* 1278 */     if (context.get("ClosingListener") != null)
/* 1279 */       return (nc.ui.pubapp.common.validateservice.ClosingCheck)context.get("ClosingListener");
/* 1280 */     nc.ui.pubapp.common.validateservice.ClosingCheck bean = new nc.ui.pubapp.common.validateservice.ClosingCheck();
/* 1281 */     context.put("ClosingListener", bean);
/* 1282 */     bean.setModel(getManageAppModel());
/* 1283 */     bean.setSaveAction(getSaveUIAction());
/* 1284 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1285 */     invokeInitializingBean(bean);
/* 1286 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell getQueryArea() {
/* 1290 */     if (context.get("queryArea") != null)
/* 1291 */       return (nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell)context.get("queryArea");
/* 1292 */     nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell bean = new nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell();
/* 1293 */     context.put("queryArea", bean);
/* 1294 */     bean.setQueryAreaCreator(getQueryUIAction());
/* 1295 */     bean.initUI();
/* 1296 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1297 */     invokeInitializingBean(bean);
/* 1298 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getQueryInfo() {
/* 1302 */     if (context.get("queryInfo") != null)
/* 1303 */       return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("queryInfo");
/* 1304 */     nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
/* 1305 */     context.put("queryInfo", bean);
/* 1306 */     bean.setModel(getManageAppModel());
/* 1307 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1308 */     invokeInitializingBean(bean);
/* 1309 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel getCardInfoPnl() {
/* 1313 */     if (context.get("cardInfoPnl") != null)
/* 1314 */       return (nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel)context.get("cardInfoPnl");
/* 1315 */     nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel bean = new nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel();
/* 1316 */     context.put("cardInfoPnl", bean);
/* 1317 */     bean.setTitleAction(getReturnaction());
/* 1318 */     bean.setModel(getManageAppModel());
/* 1319 */     bean.setRightExActions(getManagedList20());
/* 1320 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1321 */     invokeInitializingBean(bean);
/* 1322 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.actions.UEReturnAction getReturnaction() {
/* 1326 */     if (context.get("returnaction") != null)
/* 1327 */       return (nc.ui.pubapp.uif2app.actions.UEReturnAction)context.get("returnaction");
/* 1328 */     nc.ui.pubapp.uif2app.actions.UEReturnAction bean = new nc.ui.pubapp.uif2app.actions.UEReturnAction();
/* 1329 */     context.put("returnaction", bean);
/* 1330 */     bean.setGoComponent(getListView());
/* 1331 */     bean.setSaveAction(getSaveUIAction());
/* 1332 */     bean.setModel(getManageAppModel());
/* 1333 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1334 */     invokeInitializingBean(bean);
/* 1335 */     return bean;
/*      */   }
/*      */   
/* 1338 */   private List getManagedList20() { List list = new ArrayList();list.add(getActionsBar_ActionsBarSeparator_aacdf1());list.add(getHeadZoomAction());return list;
/*      */   }
/*      */   
/* 1341 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_aacdf1() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#aacdf1") != null)
/* 1342 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#aacdf1");
/* 1343 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 1344 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#aacdf1", bean);
/* 1345 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1346 */     invokeInitializingBean(bean);
/* 1347 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.actions.DefaultHeadZoomAction getHeadZoomAction() {
/* 1351 */     if (context.get("headZoomAction") != null)
/* 1352 */       return (nc.ui.pubapp.uif2app.actions.DefaultHeadZoomAction)context.get("headZoomAction");
/* 1353 */     nc.ui.pubapp.uif2app.actions.DefaultHeadZoomAction bean = new nc.ui.pubapp.uif2app.actions.DefaultHeadZoomAction();
/* 1354 */     context.put("headZoomAction", bean);
/* 1355 */     bean.setBillForm(getBillFormEditor());
/* 1356 */     bean.setModel(getManageAppModel());
/* 1357 */     bean.setPos(0);
/* 1358 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1359 */     invokeInitializingBean(bean);
/* 1360 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.TangramContainer getContainer() {
/* 1364 */     if (context.get("container") != null)
/* 1365 */       return (nc.ui.uif2.TangramContainer)context.get("container");
/* 1366 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 1367 */     context.put("container", bean);
/* 1368 */     bean.setModel(getManageAppModel());
/* 1369 */     bean.setTangramLayoutRoot(getTBNode_1b6bcd9());
/* 1370 */     bean.initUI();
/* 1371 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1372 */     invokeInitializingBean(bean);
/* 1373 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_1b6bcd9() {
/* 1377 */     if (context.get("nc.ui.uif2.tangramlayout.node.TBNode#1b6bcd9") != null)
/* 1378 */       return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#1b6bcd9");
/* 1379 */     nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
/* 1380 */     context.put("nc.ui.uif2.tangramlayout.node.TBNode#1b6bcd9", bean);
/* 1381 */     bean.setShowMode("CardLayout");
/* 1382 */     bean.setTabs(getManagedList21());
/* 1383 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1384 */     invokeInitializingBean(bean);
/* 1385 */     return bean;
/*      */   }
/*      */   
/* 1388 */   private List getManagedList21() { List list = new ArrayList();list.add(getTbNode());list.add(getHisverNode());return list;
/*      */   }
/*      */   
/* 1391 */   public nc.ui.uif2.tangramlayout.node.TBNode getTbNode() { if (context.get("tbNode") != null)
/* 1392 */       return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("tbNode");
/* 1393 */     nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
/* 1394 */     context.put("tbNode", bean);
/* 1395 */     bean.setShowMode("CardLayout");
/* 1396 */     bean.setTabs(getManagedList22());
/* 1397 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1398 */     invokeInitializingBean(bean);
/* 1399 */     return bean;
/*      */   }
/*      */   
/* 1402 */   private List getManagedList22() { List list = new ArrayList();list.add(getHSNode_cad202());list.add(getVSNode_2874c());return list;
/*      */   }
/*      */   
/* 1405 */   private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_cad202() { if (context.get("nc.ui.uif2.tangramlayout.node.HSNode#cad202") != null)
/* 1406 */       return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#cad202");
/* 1407 */     nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
/* 1408 */     context.put("nc.ui.uif2.tangramlayout.node.HSNode#cad202", bean);
/* 1409 */     bean.setLeft(getCNode_284f8a());
/* 1410 */     bean.setRight(getVSNode_80df66());
/* 1411 */     bean.setDividerLocation(0.22F);
/* 1412 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1413 */     invokeInitializingBean(bean);
/* 1414 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_284f8a() {
/* 1418 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#284f8a") != null)
/* 1419 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#284f8a");
/* 1420 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/* 1421 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#284f8a", bean);
/* 1422 */     bean.setComponent(getQueryArea());
/* 1423 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1424 */     invokeInitializingBean(bean);
/* 1425 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_80df66() {
/* 1429 */     if (context.get("nc.ui.uif2.tangramlayout.node.VSNode#80df66") != null)
/* 1430 */       return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#80df66");
/* 1431 */     nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
/* 1432 */     context.put("nc.ui.uif2.tangramlayout.node.VSNode#80df66", bean);
/* 1433 */     bean.setUp(getCNode_7e0be0());
/* 1434 */     bean.setDown(getCNode_b6b27f());
/* 1435 */     bean.setDividerLocation(25.0F);
/* 1436 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1437 */     invokeInitializingBean(bean);
/* 1438 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_7e0be0() {
/* 1442 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#7e0be0") != null)
/* 1443 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#7e0be0");
/* 1444 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/* 1445 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#7e0be0", bean);
/* 1446 */     bean.setComponent(getQueryInfo());
/* 1447 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1448 */     invokeInitializingBean(bean);
/* 1449 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_b6b27f() {
/* 1453 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#b6b27f") != null)
/* 1454 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#b6b27f");
/* 1455 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/* 1456 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#b6b27f", bean);
/* 1457 */     bean.setName(getI18nFB_13dddf3());
/* 1458 */     bean.setComponent(getListView());
/* 1459 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1460 */     invokeInitializingBean(bean);
/* 1461 */     return bean;
/*      */   }
/*      */   
/*      */   private String getI18nFB_13dddf3() {
/* 1465 */     if (context.get("nc.ui.uif2.I18nFB#13dddf3") != null)
/* 1466 */       return (String)context.get("nc.ui.uif2.I18nFB#13dddf3");
/* 1467 */     I18nFB bean = new I18nFB();
/* 1468 */     context.put("&nc.ui.uif2.I18nFB#13dddf3", bean);bean.setResDir("common");
/* 1469 */     bean.setResId("UC001-0000107");
/* 1470 */     bean.setDefaultValue("列表");
/* 1471 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1472 */     invokeInitializingBean(bean);
/*      */     try {
/* 1474 */       Object product = bean.getObject();
/* 1475 */       context.put("nc.ui.uif2.I18nFB#13dddf3", product);
/* 1476 */       return (String)product;
/*      */     } catch (Exception e) {
/* 1478 */       throw new RuntimeException(e);
/*      */     } }
/*      */   
/* 1481 */   private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_2874c() { if (context.get("nc.ui.uif2.tangramlayout.node.VSNode#2874c") != null)
/* 1482 */       return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#2874c");
/* 1483 */     nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
/* 1484 */     context.put("nc.ui.uif2.tangramlayout.node.VSNode#2874c", bean);
/* 1485 */     bean.setUp(getCNode_1a1b1ec());
/* 1486 */     bean.setDown(getCNode_137f2d());
/* 1487 */     bean.setDividerLocation(30.0F);
/* 1488 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1489 */     invokeInitializingBean(bean);
/* 1490 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_1a1b1ec() {
/* 1494 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#1a1b1ec") != null)
/* 1495 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1a1b1ec");
/* 1496 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/* 1497 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#1a1b1ec", bean);
/* 1498 */     bean.setComponent(getCardInfoPnl());
/* 1499 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1500 */     invokeInitializingBean(bean);
/* 1501 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_137f2d() {
/* 1505 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#137f2d") != null)
/* 1506 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#137f2d");
/* 1507 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/* 1508 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#137f2d", bean);
/* 1509 */     bean.setName(getI18nFB_a828df());
/* 1510 */     bean.setComponent(getBillFormEditor());
/* 1511 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1512 */     invokeInitializingBean(bean);
/* 1513 */     return bean;
/*      */   }
/*      */   
/*      */   private String getI18nFB_a828df() {
/* 1517 */     if (context.get("nc.ui.uif2.I18nFB#a828df") != null)
/* 1518 */       return (String)context.get("nc.ui.uif2.I18nFB#a828df");
/* 1519 */     I18nFB bean = new I18nFB();
/* 1520 */     context.put("&nc.ui.uif2.I18nFB#a828df", bean);bean.setResDir("common");
/* 1521 */     bean.setResId("UC001-0000106");
/* 1522 */     bean.setDefaultValue("卡片");
/* 1523 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1524 */     invokeInitializingBean(bean);
/*      */     try {
/* 1526 */       Object product = bean.getObject();
/* 1527 */       context.put("nc.ui.uif2.I18nFB#a828df", product);
/* 1528 */       return (String)product;
/*      */     } catch (Exception e) {
/* 1530 */       throw new RuntimeException(e);
/*      */     } }
/*      */   
/* 1533 */   public ReportBillListView getHisVersionListView() { if (context.get("hisVersionListView") != null)
/* 1534 */       return (ReportBillListView)context.get("hisVersionListView");
/* 1535 */     ReportBillListView bean = new ReportBillListView();
/* 1536 */     context.put("hisVersionListView", bean);
/* 1537 */     bean.setModel(getHisManageAppModel());
/* 1538 */     bean.setMultiSelectionMode(0);
/* 1539 */     bean.setTemplateContainer(getTemplateContainer());
/* 1540 */     bean.setUserdefitemListPreparator(getUserdefAndMarAsstListPreparator());
/* 1541 */     bean.setHandlerGroup(getManagedList23());
/* 1542 */     bean.initUI();
/* 1543 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1544 */     invokeInitializingBean(bean);
/* 1545 */     return bean;
/*      */   }
/*      */   
/* 1548 */   private List getManagedList23() { List list = new ArrayList();list.add(getEventHandlerGroup_173707());return list;
/*      */   }
/*      */   
/* 1551 */   private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_173707() { if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#173707") != null)
/* 1552 */       return (nc.ui.pubapp.uif2app.event.EventHandlerGroup)context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#173707");
/* 1553 */     nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
/* 1554 */     context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#173707", bean);
/* 1555 */     bean.setEvent("nc.ui.uif2.AppEvent");
/* 1556 */     bean.setHandler(getSelectionChangedHandler_a420());
/* 1557 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1558 */     invokeInitializingBean(bean);
/* 1559 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler getSelectionChangedHandler_a420() {
/* 1563 */     if (context.get("nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler#a420") != null)
/* 1564 */       return (nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler)context.get("nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler#a420");
/* 1565 */     nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler bean = new nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler();
/* 1566 */     context.put("nc.ui.qc.c003.maintain.handler.head.SelectionChangedHandler#a420", bean);
/* 1567 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1568 */     invokeInitializingBean(bean);
/* 1569 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.model.BillManageModel getHisManageAppModel() {
/* 1573 */     if (context.get("hisManageAppModel") != null)
/* 1574 */       return (nc.ui.pubapp.uif2app.model.BillManageModel)context.get("hisManageAppModel");
/* 1575 */     nc.ui.pubapp.uif2app.model.BillManageModel bean = new nc.ui.pubapp.uif2app.model.BillManageModel();
/* 1576 */     context.put("hisManageAppModel", bean);
/* 1577 */     bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
/* 1578 */     bean.setContext(getContext());
/* 1579 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1580 */     invokeInitializingBean(bean);
/* 1581 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.tangramlayout.node.TBNode getHisverNode() {
/* 1585 */     if (context.get("hisverNode") != null)
/* 1586 */       return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("hisverNode");
/* 1587 */     nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
/* 1588 */     context.put("hisverNode", bean);
/* 1589 */     bean.setShowMode("TabbedPane");
/* 1590 */     bean.setTabs(getManagedList24());
/* 1591 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1592 */     invokeInitializingBean(bean);
/* 1593 */     return bean;
/*      */   }
/*      */   
/* 1596 */   private List getManagedList24() { List list = new ArrayList();list.add(getCNode_c25357());return list;
/*      */   }
/*      */   
/* 1599 */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_c25357() { if (context.get("nc.ui.uif2.tangramlayout.node.CNode#c25357") != null)
/* 1600 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#c25357");
/* 1601 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/* 1602 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#c25357", bean);
/* 1603 */     bean.setName(getI18nFB_1ded3d());
/* 1604 */     bean.setComponent(getHisVersionListView());
/* 1605 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1606 */     invokeInitializingBean(bean);
/* 1607 */     return bean;
/*      */   }
/*      */   
/*      */   private String getI18nFB_1ded3d() {
/* 1611 */     if (context.get("nc.ui.uif2.I18nFB#1ded3d") != null)
/* 1612 */       return (String)context.get("nc.ui.uif2.I18nFB#1ded3d");
/* 1613 */     I18nFB bean = new I18nFB();
/* 1614 */     context.put("&nc.ui.uif2.I18nFB#1ded3d", bean);bean.setResDir("c06005");
/* 1615 */     bean.setResId("1C060050076");
/* 1616 */     bean.setDefaultValue("检验历史");
/* 1617 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1618 */     invokeInitializingBean(bean);
/*      */     try {
/* 1620 */       Object product = bean.getObject();
/* 1621 */       context.put("nc.ui.uif2.I18nFB#1ded3d", product);
/* 1622 */       return (String)product;
/*      */     } catch (Exception e) {
/* 1624 */       throw new RuntimeException(e);
/*      */     } }
/*      */   
/* 1627 */   public nc.ui.scmpub.listener.BillCodeEditMediator getBillCodeMediator() { if (context.get("billCodeMediator") != null)
/* 1628 */       return (nc.ui.scmpub.listener.BillCodeEditMediator)context.get("billCodeMediator");
/* 1629 */     nc.ui.scmpub.listener.BillCodeEditMediator bean = new nc.ui.scmpub.listener.BillCodeEditMediator();
/* 1630 */     context.put("billCodeMediator", bean);
/* 1631 */     bean.setBillCodeKey("vbillcode");
/* 1632 */     bean.setBillType("C003");
/* 1633 */     bean.setBillForm(getBillFormEditor());
/* 1634 */     bean.initUI();
/* 1635 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1636 */     invokeInitializingBean(bean);
/* 1637 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.maintain.util.QcReportRowNoMediator getRowNoMediator() {
/* 1641 */     if (context.get("rowNoMediator") != null)
/* 1642 */       return (nc.ui.qc.c003.maintain.util.QcReportRowNoMediator)context.get("rowNoMediator");
/* 1643 */     nc.ui.qc.c003.maintain.util.QcReportRowNoMediator bean = new nc.ui.qc.c003.maintain.util.QcReportRowNoMediator();
/* 1644 */     context.put("rowNoMediator", bean);
/* 1645 */     bean.setEditor(getBillFormEditor());
/* 1646 */     bean.setModel(getManageAppModel());
/* 1647 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1648 */     invokeInitializingBean(bean);
/* 1649 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getUserdefAndMarAsstCardPreparator() {
/* 1653 */     if (context.get("userdefAndMarAsstCardPreparator") != null)
/* 1654 */       return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare)context.get("userdefAndMarAsstCardPreparator");
/* 1655 */     nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
/* 1656 */     context.put("userdefAndMarAsstCardPreparator", bean);
/* 1657 */     bean.setBillDataPrepares(getManagedList25());
/* 1658 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1659 */     invokeInitializingBean(bean);
/* 1660 */     return bean;
/*      */   }
/*      */   
/* 1663 */   private List getManagedList25() { List list = new ArrayList();list.add(getUserdefitemPreparator());list.add(getMarAsstPreparator());return list;
/*      */   }
/*      */   
/* 1666 */   public nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getUserdefAndMarAsstListPreparator() { if (context.get("userdefAndMarAsstListPreparator") != null)
/* 1667 */       return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare)context.get("userdefAndMarAsstListPreparator");
/* 1668 */     nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
/* 1669 */     context.put("userdefAndMarAsstListPreparator", bean);
/* 1670 */     bean.setBillListDataPrepares(getManagedList26());
/* 1671 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1672 */     invokeInitializingBean(bean);
/* 1673 */     return bean;
/*      */   }
/*      */   
/* 1676 */   private List getManagedList26() { List list = new ArrayList();list.add(getUserdefitemlistPreparator());list.add(getMarAsstPreparator());return list;
/*      */   }
/*      */   
/* 1679 */   public nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator getMarAsstPreparator() { if (context.get("marAsstPreparator") != null)
/* 1680 */       return (nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator)context.get("marAsstPreparator");
/* 1681 */     nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator bean = new nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator();
/* 1682 */     context.put("marAsstPreparator", bean);
/* 1683 */     bean.setModel(getManageAppModel());
/* 1684 */     bean.setContainer(getUserdefitemContainer());
/* 1685 */     bean.setPrefix("vfree");
/* 1686 */     bean.setMaterialField("pk_material");
/* 1687 */     bean.setProjectField("cprojectid");
/* 1688 */     bean.setSupplierField("pk_supplier");
/* 1689 */     bean.setProductorField("cproductorid");
/* 1690 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1691 */     invokeInitializingBean(bean);
/* 1692 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer() {
/* 1696 */     if (context.get("userdefitemContainer") != null)
/* 1697 */       return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
/* 1698 */     nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
/* 1699 */     context.put("userdefitemContainer", bean);
/* 1700 */     bean.setContext(getContext());
/* 1701 */     bean.setParams(getManagedList27());
/* 1702 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1703 */     invokeInitializingBean(bean);
/* 1704 */     return bean;
/*      */   }
/*      */   
/* 1707 */   private List getManagedList27() { List list = new ArrayList();list.add(getQueryParam_1edb1aa());list.add(getQueryParam_2b38e4());list.add(getQueryParam_1a44172());return list;
/*      */   }
/*      */   
/* 1710 */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1edb1aa() { if (context.get("nc.ui.uif2.userdefitem.QueryParam#1edb1aa") != null)
/* 1711 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#1edb1aa");
/* 1712 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/* 1713 */     context.put("nc.ui.uif2.userdefitem.QueryParam#1edb1aa", bean);
/* 1714 */     bean.setMdfullname("qc.qc_reportbill");
/* 1715 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1716 */     invokeInitializingBean(bean);
/* 1717 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_2b38e4() {
/* 1721 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#2b38e4") != null)
/* 1722 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#2b38e4");
/* 1723 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/* 1724 */     context.put("nc.ui.uif2.userdefitem.QueryParam#2b38e4", bean);
/* 1725 */     bean.setMdfullname("qc.qc_reportbill_b");
/* 1726 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1727 */     invokeInitializingBean(bean);
/* 1728 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1a44172() {
/* 1732 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#1a44172") != null)
/* 1733 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#1a44172");
/* 1734 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/* 1735 */     context.put("nc.ui.uif2.userdefitem.QueryParam#1a44172", bean);
/* 1736 */     bean.setRulecode("materialassistant");
/* 1737 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1738 */     invokeInitializingBean(bean);
/* 1739 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemPreparator() {
/* 1743 */     if (context.get("userdefitemPreparator") != null)
/* 1744 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("userdefitemPreparator");
/* 1745 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 1746 */     context.put("userdefitemPreparator", bean);
/* 1747 */     bean.setContainer(getUserdefitemContainer());
/* 1748 */     bean.setParams(getManagedList28());
/* 1749 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1750 */     invokeInitializingBean(bean);
/* 1751 */     return bean;
/*      */   }
/*      */   
/* 1754 */   private List getManagedList28() { List list = new ArrayList();list.add(getUserdefQueryParam_c4c81c());list.add(getUserdefQueryParam_12de01b());return list;
/*      */   }
/*      */   
/* 1757 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_c4c81c() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#c4c81c") != null)
/* 1758 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#c4c81c");
/* 1759 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 1760 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#c4c81c", bean);
/* 1761 */     bean.setMdfullname("qc.qc_reportbill");
/* 1762 */     bean.setPos(0);
/* 1763 */     bean.setPrefix("vdef");
/* 1764 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1765 */     invokeInitializingBean(bean);
/* 1766 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_12de01b() {
/* 1770 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#12de01b") != null)
/* 1771 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#12de01b");
/* 1772 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 1773 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#12de01b", bean);
/* 1774 */     bean.setMdfullname("qc.qc_reportbill_b");
/* 1775 */     bean.setPos(1);
/* 1776 */     bean.setPrefix("vbdef");
/* 1777 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1778 */     invokeInitializingBean(bean);
/* 1779 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemlistPreparator() {
/* 1783 */     if (context.get("userdefitemlistPreparator") != null)
/* 1784 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("userdefitemlistPreparator");
/* 1785 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 1786 */     context.put("userdefitemlistPreparator", bean);
/* 1787 */     bean.setContainer(getUserdefitemContainer());
/* 1788 */     bean.setParams(getManagedList29());
/* 1789 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1790 */     invokeInitializingBean(bean);
/* 1791 */     return bean;
/*      */   }
/*      */   
/* 1794 */   private List getManagedList29() { List list = new ArrayList();list.add(getUserdefQueryParam_d4104b());list.add(getUserdefQueryParam_79d76f());return list;
/*      */   }
/*      */   
/* 1797 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_d4104b() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#d4104b") != null)
/* 1798 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#d4104b");
/* 1799 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 1800 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#d4104b", bean);
/* 1801 */     bean.setMdfullname("qc.qc_reportbill");
/* 1802 */     bean.setPos(0);
/* 1803 */     bean.setPrefix("vdef");
/* 1804 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1805 */     invokeInitializingBean(bean);
/* 1806 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_79d76f() {
/* 1810 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#79d76f") != null)
/* 1811 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#79d76f");
/* 1812 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 1813 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#79d76f", bean);
/* 1814 */     bean.setMdfullname("qc.qc_reportbill_b");
/* 1815 */     bean.setPos(1);
/* 1816 */     bean.setTabcode("report_b");
/* 1817 */     bean.setPrefix("vbdef");
/* 1818 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1819 */     invokeInitializingBean(bean);
/* 1820 */     return bean;
/*      */   }
/*      */   
/*      */   public LinkQueryHyperlinkMediator getApplyLinkQueryHyperlinkMediator() {
/* 1824 */     if (context.get("applyLinkQueryHyperlinkMediator") != null)
/* 1825 */       return (LinkQueryHyperlinkMediator)context.get("applyLinkQueryHyperlinkMediator");
/* 1826 */     LinkQueryHyperlinkMediator bean = new LinkQueryHyperlinkMediator();
/* 1827 */     context.put("applyLinkQueryHyperlinkMediator", bean);
/* 1828 */     bean.setModel(getManageAppModel());
/* 1829 */     bean.setSrcBillIdField("pk_applybill");
/* 1830 */     bean.setSrcBillNOField("vapplybillcode");
/* 1831 */     bean.setSrcBillType("C001");
/* 1832 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1833 */     invokeInitializingBean(bean);
/* 1834 */     return bean;
/*      */   }
/*      */   
/*      */   public LinkQueryHyperlinkMediator getHisApplyLinkQueryHyperlinkMediator() {
/* 1838 */     if (context.get("hisApplyLinkQueryHyperlinkMediator") != null)
/* 1839 */       return (LinkQueryHyperlinkMediator)context.get("hisApplyLinkQueryHyperlinkMediator");
/* 1840 */     LinkQueryHyperlinkMediator bean = new LinkQueryHyperlinkMediator();
/* 1841 */     context.put("hisApplyLinkQueryHyperlinkMediator", bean);
/* 1842 */     bean.setModel(getHisManageAppModel());
/* 1843 */     bean.setSrcBillIdField("pk_applybill");
/* 1844 */     bean.setSrcBillNOField("vapplybillcode");
/* 1845 */     bean.setSrcBillType("C001");
/* 1846 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1847 */     invokeInitializingBean(bean);
/* 1848 */     return bean;
/*      */   }
/*      */   
/*      */   public LinkQueryHyperlinkMediator getRejectLinkQueryHyperlinkMediator() {
/* 1852 */     if (context.get("rejectLinkQueryHyperlinkMediator") != null)
/* 1853 */       return (LinkQueryHyperlinkMediator)context.get("rejectLinkQueryHyperlinkMediator");
/* 1854 */     LinkQueryHyperlinkMediator bean = new LinkQueryHyperlinkMediator();
/* 1855 */     context.put("rejectLinkQueryHyperlinkMediator", bean);
/* 1856 */     bean.setModel(getManageAppModel());
/* 1857 */     bean.setSrcBillIdField("pk_rejectbill");
/* 1858 */     bean.setSrcBillNOField("vrejectcode");
/* 1859 */     bean.setSrcBillType("C004");
/* 1860 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1861 */     invokeInitializingBean(bean);
/* 1862 */     return bean;
/*      */   }
/*      */   
/*      */   public LinkQueryHyperlinkMediator getHisRejectLinkQueryHyperlinkMediator() {
/* 1866 */     if (context.get("hisRejectLinkQueryHyperlinkMediator") != null)
/* 1867 */       return (LinkQueryHyperlinkMediator)context.get("hisRejectLinkQueryHyperlinkMediator");
/* 1868 */     LinkQueryHyperlinkMediator bean = new LinkQueryHyperlinkMediator();
/* 1869 */     context.put("hisRejectLinkQueryHyperlinkMediator", bean);
/* 1870 */     bean.setModel(getHisManageAppModel());
/* 1871 */     bean.setSrcBillIdField("pk_rejectbill");
/* 1872 */     bean.setSrcBillNOField("vrejectcode");
/* 1873 */     bean.setSrcBillType("C004");
/* 1874 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1875 */     invokeInitializingBean(bean);
/* 1876 */     return bean;
/*      */   }
/*      */   
/*      */   public LinkQueryHyperlinkMediator getFirstlinkQueryHyperlinkMediator() {
/* 1880 */     if (context.get("firstlinkQueryHyperlinkMediator") != null)
/* 1881 */       return (LinkQueryHyperlinkMediator)context.get("firstlinkQueryHyperlinkMediator");
/* 1882 */     LinkQueryHyperlinkMediator bean = new LinkQueryHyperlinkMediator();
/* 1883 */     context.put("firstlinkQueryHyperlinkMediator", bean);
/* 1884 */     bean.setModel(getManageAppModel());
/* 1885 */     bean.setSrcBillIdField("cfirstid");
/* 1886 */     bean.setSrcBillNOField("vfirstcode");
/* 1887 */     bean.setSrcBillTypeField("cfirsttypecode");
/* 1888 */     bean.setSrcBillTypeFieldPos(Integer.valueOf(1));
/* 1889 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1890 */     invokeInitializingBean(bean);
/* 1891 */     return bean;
/*      */   }
/*      */   
/*      */   public LinkQueryHyperlinkMediator getHisFirstlinkQueryHyperlinkMediator() {
/* 1895 */     if (context.get("hisFirstlinkQueryHyperlinkMediator") != null)
/* 1896 */       return (LinkQueryHyperlinkMediator)context.get("hisFirstlinkQueryHyperlinkMediator");
/* 1897 */     LinkQueryHyperlinkMediator bean = new LinkQueryHyperlinkMediator();
/* 1898 */     context.put("hisFirstlinkQueryHyperlinkMediator", bean);
/* 1899 */     bean.setModel(getHisManageAppModel());
/* 1900 */     bean.setSrcBillIdField("cfirstid");
/* 1901 */     bean.setSrcBillNOField("vfirstcode");
/* 1902 */     bean.setSrcBillTypeField("cfirsttypecode");
/* 1903 */     bean.setSrcBillTypeFieldPos(Integer.valueOf(1));
/* 1904 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1905 */     invokeInitializingBean(bean);
/* 1906 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.query.ReportLazyItemLoader getBillLazilyLoader() {
/* 1910 */     if (context.get("billLazilyLoader") != null)
/* 1911 */       return (nc.ui.qc.c003.query.ReportLazyItemLoader)context.get("billLazilyLoader");
/* 1912 */     nc.ui.qc.c003.query.ReportLazyItemLoader bean = new nc.ui.qc.c003.query.ReportLazyItemLoader();
/* 1913 */     context.put("billLazilyLoader", bean);
/* 1914 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1915 */     invokeInitializingBean(bean);
/* 1916 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager getLasilyLodadMediator() {
/* 1920 */     if (context.get("LasilyLodadMediator") != null)
/* 1921 */       return (nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager)context.get("LasilyLodadMediator");
/* 1922 */     nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager bean = new nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager();
/* 1923 */     context.put("LasilyLodadMediator", bean);
/* 1924 */     bean.setModel(getManageAppModel());
/* 1925 */     bean.setLoader(getBillLazilyLoader());
/* 1926 */     bean.setLazilyLoadSupporter(getManagedList30());
/* 1927 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1928 */     invokeInitializingBean(bean);
/* 1929 */     return bean;
/*      */   }
/*      */   
/* 1932 */   private List getManagedList30() { List list = new ArrayList();list.add(getCardPanelLazilyLoad_12ed03f());list.add(getQcReportListPanelLazilyLoad_69f987());list.add(getActionLazilyLoad_17d439f());return list;
/*      */   }
/*      */   
/* 1935 */   private nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad getCardPanelLazilyLoad_12ed03f() { if (context.get("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#12ed03f") != null)
/* 1936 */       return (nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad)context.get("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#12ed03f");
/* 1937 */     nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad();
/* 1938 */     context.put("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#12ed03f", bean);
/* 1939 */     bean.setBillform(getBillFormEditor());
/* 1940 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1941 */     invokeInitializingBean(bean);
/* 1942 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.qc.c003.maintain.lazilyload.QcReportListPanelLazilyLoad getQcReportListPanelLazilyLoad_69f987() {
/* 1946 */     if (context.get("nc.ui.qc.c003.maintain.lazilyload.QcReportListPanelLazilyLoad#69f987") != null)
/* 1947 */       return (nc.ui.qc.c003.maintain.lazilyload.QcReportListPanelLazilyLoad)context.get("nc.ui.qc.c003.maintain.lazilyload.QcReportListPanelLazilyLoad#69f987");
/* 1948 */     nc.ui.qc.c003.maintain.lazilyload.QcReportListPanelLazilyLoad bean = new nc.ui.qc.c003.maintain.lazilyload.QcReportListPanelLazilyLoad();
/* 1949 */     context.put("nc.ui.qc.c003.maintain.lazilyload.QcReportListPanelLazilyLoad#69f987", bean);
/* 1950 */     bean.setListView(getListView());
/* 1951 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1952 */     invokeInitializingBean(bean);
/* 1953 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad getActionLazilyLoad_17d439f() {
/* 1957 */     if (context.get("nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad#17d439f") != null)
/* 1958 */       return (nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad)context.get("nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad#17d439f");
/* 1959 */     nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad();
/* 1960 */     context.put("nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad#17d439f", bean);
/* 1961 */     bean.setModel(getManageAppModel());
/* 1962 */     bean.setActionList(getManagedList31());
/* 1963 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1964 */     invokeInitializingBean(bean);
/* 1965 */     return bean;
/*      */   }
/*      */   
/* 1968 */   private List getManagedList31() { List list = new ArrayList();list.add(getPrintAction());list.add(getPreviewAction());list.add(getOutputAction());return list;
/*      */   }
/*      */   
/* 1971 */   public nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller getRemoteCallCombinatorCaller() { if (context.get("remoteCallCombinatorCaller") != null)
/* 1972 */       return (nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller)context.get("remoteCallCombinatorCaller");
/* 1973 */     nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller bean = new nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller();
/* 1974 */     context.put("remoteCallCombinatorCaller", bean);
/* 1975 */     bean.setRemoteCallers(getManagedList32());
/* 1976 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1977 */     invokeInitializingBean(bean);
/* 1978 */     return bean;
/*      */   }
/*      */   
/* 1981 */   private List getManagedList32() { List list = new ArrayList();list.add(getQueryTemplateContainer());list.add(getTemplateContainer());list.add(getUserdefitemContainer());return list;
/*      */   }
/*      */   
/* 1984 */   public nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender getTrantypeTempMender() { if (context.get("trantypeTempMender") != null)
/* 1985 */       return (nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender)context.get("trantypeTempMender");
/* 1986 */     nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender bean = new nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender(getContext());context.put("trantypeTempMender", bean);
/* 1987 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1988 */     invokeInitializingBean(bean);
/* 1989 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.pub.util.QCFractionFixMediator getFractionFixMediator() {
/* 1993 */     if (context.get("fractionFixMediator") != null)
/* 1994 */       return (nc.ui.qc.pub.util.QCFractionFixMediator)context.get("fractionFixMediator");
/* 1995 */     nc.ui.qc.pub.util.QCFractionFixMediator bean = new nc.ui.qc.pub.util.QCFractionFixMediator(getBillFormEditor(), getListView());context.put("fractionFixMediator", bean);
/* 1996 */     bean.initUI();
/* 1997 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1998 */     invokeInitializingBean(bean);
/* 1999 */     return bean;
/*      */   }
/*      */   
/*      */   public LinkQueryHyperlinkMediator getOperationLinkQueryHyperlinkMediator() {
/* 2003 */     if (context.get("operationLinkQueryHyperlinkMediator") != null)
/* 2004 */       return (LinkQueryHyperlinkMediator)context.get("operationLinkQueryHyperlinkMediator");
/* 2005 */     LinkQueryHyperlinkMediator bean = new LinkQueryHyperlinkMediator();
/* 2006 */     context.put("operationLinkQueryHyperlinkMediator", bean);
/* 2007 */     bean.setModel(getManageAppModel());
/* 2008 */     bean.setSrcBillIdField("pk_operationrep");
/* 2009 */     bean.setSrcBillNOField("operationrepcode");
/* 2010 */     bean.setSrcBillType("55D2-Cxx-01");
/* 2011 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2012 */     invokeInitializingBean(bean);
/* 2013 */     return bean;
/*      */   }
/*      */   
/*      */   public LinkQueryHyperlinkMediator getHisOperationLinkQueryHyperlinkMediator() {
/* 2017 */     if (context.get("hisOperationLinkQueryHyperlinkMediator") != null)
/* 2018 */       return (LinkQueryHyperlinkMediator)context.get("hisOperationLinkQueryHyperlinkMediator");
/* 2019 */     LinkQueryHyperlinkMediator bean = new LinkQueryHyperlinkMediator();
/* 2020 */     context.put("hisOperationLinkQueryHyperlinkMediator", bean);
/* 2021 */     bean.setModel(getHisManageAppModel());
/* 2022 */     bean.setSrcBillIdField("pk_operationrep");
/* 2023 */     bean.setSrcBillNOField("operationrepcode");
/* 2024 */     bean.setSrcBillType("55D2-Cxx-01");
/* 2025 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2026 */     invokeInitializingBean(bean);
/* 2027 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.components.pagination.PaginationBar getPageBar() {
/* 2031 */     if (context.get("pageBar") != null)
/* 2032 */       return (nc.ui.uif2.components.pagination.PaginationBar)context.get("pageBar");
/* 2033 */     nc.ui.uif2.components.pagination.PaginationBar bean = new nc.ui.uif2.components.pagination.PaginationBar();
/* 2034 */     context.put("pageBar", bean);
/* 2035 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2036 */     invokeInitializingBean(bean);
/* 2037 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.actions.pagination.BillModelPaginationDelegator getPageDelegator() {
/* 2041 */     if (context.get("pageDelegator") != null)
/* 2042 */       return (nc.ui.pubapp.uif2app.actions.pagination.BillModelPaginationDelegator)context.get("pageDelegator");
/* 2043 */     nc.ui.pubapp.uif2app.actions.pagination.BillModelPaginationDelegator bean = new nc.ui.pubapp.uif2app.actions.pagination.BillModelPaginationDelegator(getManageAppModel());context.put("pageDelegator", bean);
/* 2044 */     bean.setPaginationQuery(getPageQuery());
/* 2045 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2046 */     invokeInitializingBean(bean);
/* 2047 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.qc.c003.page.ReportBillPageService getPageQuery() {
/* 2051 */     if (context.get("pageQuery") != null)
/* 2052 */       return (nc.ui.qc.c003.page.ReportBillPageService)context.get("pageQuery");
/* 2053 */     nc.ui.qc.c003.page.ReportBillPageService bean = new nc.ui.qc.c003.page.ReportBillPageService();
/* 2054 */     context.put("pageQuery", bean);
/* 2055 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2056 */     invokeInitializingBean(bean);
/* 2057 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.scmpub.page.model.SCMBillPageMediator getPageMediator() {
/* 2061 */     if (context.get("pageMediator") != null)
/* 2062 */       return (nc.ui.scmpub.page.model.SCMBillPageMediator)context.get("pageMediator");
/* 2063 */     nc.ui.scmpub.page.model.SCMBillPageMediator bean = new nc.ui.scmpub.page.model.SCMBillPageMediator();
/* 2064 */     context.put("pageMediator", bean);
/* 2065 */     bean.setListView(getListView());
/* 2066 */     bean.setRecordInPage(10);
/* 2067 */     bean.setCachePages(10);
/* 2068 */     bean.setPageDelegator(getPageDelegator());
/* 2069 */     bean.init();
/* 2070 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2071 */     invokeInitializingBean(bean);
/* 2072 */     return bean;
/*      */   }
/*      */ }