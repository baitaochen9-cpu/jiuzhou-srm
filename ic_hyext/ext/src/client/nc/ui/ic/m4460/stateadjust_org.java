/*     */ package nc.ui.ic.m4460;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

/*     */ import nc.ui.ic.m4460.action.StateAdjustOnhandQueryAction;
/*     */ import nc.ui.ic.m4460.view.StateAdjustBillListView;
/*     */ import nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator;
import nc.ui.uif2.editor.UserdefitemContainerListPreparator;
/*     */ 
/*     */ public class stateadjust_org extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.vo.uif2.LoginContext getContext() { if (context.get("context") != null)
/*  14 */       return (nc.vo.uif2.LoginContext)context.get("context");
/*  15 */     nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
/*  16 */     context.put("context", bean);
/*  17 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  18 */     invokeInitializingBean(bean);
/*  19 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.editor.TemplateContainer getTemplateContainer() {
/*  23 */     if (context.get("templateContainer") != null)
/*  24 */       return (nc.ui.uif2.editor.TemplateContainer)context.get("templateContainer");
/*  25 */     nc.ui.uif2.editor.TemplateContainer bean = new nc.ui.uif2.editor.TemplateContainer();
/*  26 */     context.put("templateContainer", bean);
/*  27 */     bean.setContext(getContext());
/*  28 */     bean.load();
/*  29 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  30 */     invokeInitializingBean(bean);
/*  31 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.pub.env.ICUIContext getIcUIContext() {
/*  35 */     if (context.get("icUIContext") != null)
/*  36 */       return (nc.ui.ic.pub.env.ICUIContext)context.get("icUIContext");
/*  37 */     nc.ui.ic.pub.env.ICUIContext bean = new nc.ui.ic.pub.env.ICUIContext(getContext());context.put("icUIContext", bean);
/*  38 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  39 */     invokeInitializingBean(bean);
/*  40 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.vo.bd.meta.BDObjectAdpaterFactory getBoadatorfactory() {
/*  44 */     if (context.get("boadatorfactory") != null)
/*  45 */       return (nc.vo.bd.meta.BDObjectAdpaterFactory)context.get("boadatorfactory");
/*  46 */     nc.vo.bd.meta.BDObjectAdpaterFactory bean = new nc.vo.bd.meta.BDObjectAdpaterFactory();
/*  47 */     context.put("boadatorfactory", bean);
/*  48 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  49 */     invokeInitializingBean(bean);
/*  50 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.model.StateAdjustModelService getManageModelService() {
/*  54 */     if (context.get("manageModelService") != null)
/*  55 */       return (nc.ui.ic.m4460.model.StateAdjustModelService)context.get("manageModelService");
/*  56 */     nc.ui.ic.m4460.model.StateAdjustModelService bean = new nc.ui.ic.m4460.model.StateAdjustModelService();
/*  57 */     context.put("manageModelService", bean);
/*  58 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  59 */     invokeInitializingBean(bean);
/*  60 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.pub.model.ICBizModel getIcBizModel() {
/*  64 */     if (context.get("icBizModel") != null)
/*  65 */       return (nc.ui.ic.pub.model.ICBizModel)context.get("icBizModel");
/*  66 */     nc.ui.ic.pub.model.ICBizModel bean = new nc.ui.ic.pub.model.ICBizModel();
/*  67 */     context.put("icBizModel", bean);
/*  68 */     bean.setService(getManageModelService());
/*  69 */     bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
/*  70 */     bean.setIcUIContext(getIcUIContext());
/*  71 */     bean.setContext(getContext());
/*  72 */     bean.setBillType("4460");
/*  73 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  74 */     invokeInitializingBean(bean);
/*  75 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.model.StateAdjustModel getModel() {
/*  79 */     if (context.get("model") != null)
/*  80 */       return (nc.ui.ic.m4460.model.StateAdjustModel)context.get("model");
/*  81 */     nc.ui.ic.m4460.model.StateAdjustModel bean = new nc.ui.ic.m4460.model.StateAdjustModel();
/*  82 */     context.put("model", bean);
/*  83 */     bean.setService(getManageModelService());
/*  84 */     bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
/*  85 */     bean.setContext(getContext());
/*  86 */     bean.setBillType("4460");
/*  87 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  88 */     invokeInitializingBean(bean);
/*  89 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.pub.model.ICBizDataManager getModelDataManager() {
/*  93 */     if (context.get("modelDataManager") != null)
/*  94 */       return (nc.ui.ic.pub.model.ICBizDataManager)context.get("modelDataManager");
/*  95 */     nc.ui.ic.pub.model.ICBizDataManager bean = new nc.ui.ic.pub.model.ICBizDataManager();
/*  96 */     context.put("modelDataManager", bean);
/*  97 */     bean.setModel(getModel());
/*  98 */     bean.setService(getManageModelService());
/*  99 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 100 */     invokeInitializingBean(bean);
/* 101 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer() {
/* 105 */     if (context.get("userdefitemContainer") != null)
/* 106 */       return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
/* 107 */     nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
/* 108 */     context.put("userdefitemContainer", bean);
/* 109 */     bean.setContext(getContext());
/* 110 */     bean.setParams(getManagedList0());
/* 111 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 112 */     invokeInitializingBean(bean);
/* 113 */     return bean;
/*     */   }
/*     */   
/* 116 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getQueryParam_e433c());return list;
/*     */   }
/*     */   
/* 119 */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_e433c() { if (context.get("nc.ui.uif2.userdefitem.QueryParam#e433c") != null)
/* 120 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#e433c");
/* 121 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/* 122 */     context.put("nc.ui.uif2.userdefitem.QueryParam#e433c", bean);
/* 123 */     bean.setRulecode("materialassistant");
/* 124 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 125 */     invokeInitializingBean(bean);
/* 126 */     return bean;
/*     */   }
/*     */   
/*     */   public MarAsstPreparator getMarAsstPreparator() {
/* 130 */     if (context.get("marAsstPreparator") != null)
/* 131 */       return (MarAsstPreparator)context.get("marAsstPreparator");
/* 132 */     MarAsstPreparator bean = new MarAsstPreparator();
/* 133 */     context.put("marAsstPreparator", bean);
/* 134 */     bean.setModel(getModel());
/* 135 */     bean.setContainer(getUserdefitemContainer());
/* 136 */     bean.setPrefix("vfree");
/* 137 */     bean.setMaterialField("cmaterialvid");
/* 138 */     bean.setStoreStateField("cstateid");
/* 139 */     bean.setProjectField("cprojectid");
/* 140 */     bean.setSupplierField("cvendorid");
/* 141 */     bean.setProductorField("cproductorid");
/* 142 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 143 */     invokeInitializingBean(bean);
/* 144 */     return bean;
/*     */   }

public UserdefitemContainerListPreparator getDefItemPreparator() {
/* 130 */     if (context.get("defItemPreparator") != null)
/* 131 */       return (UserdefitemContainerListPreparator)context.get("defItemPreparator");
/* 132 */     UserdefitemContainerListPreparator bean = new UserdefitemContainerListPreparator();
/* 133 */     context.put("defItemPreparator", bean);
/* 135 */     bean.setContainer(getUserdefitemContainer1());
/* 136 */     bean.setParams(getManagedList110());
/* 142 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 143 */     invokeInitializingBean(bean);
/* 144 */     return bean;
/*     */   }
private List getManagedList110() { List list = new java.util.ArrayList();list.add(getQueryParam_e433c2());return list;
/*     */   }
private nc.ui.uif2.editor.UserdefQueryParam getQueryParam_e433c2() { if (context.get("nc.ui.uif2.userdefitem.QueryParam#e433c2") != null)
/* 120 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#e433c2");
/* 121 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 122 */     context.put("nc.ui.uif2.userdefitem.QueryParam#e433c2", bean);
/* 123 */     bean.setMdfullname("ic.StateAdjustVO");
			  bean.setPos(0);
bean.setPrefix("vdef");
/* 124 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 125 */     invokeInitializingBean(bean);
/* 126 */     return bean;
/*     */   }
public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer1() {
/* 105 */     if (context.get("userdefitemContainer1") != null)
/* 106 */       return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer1");
/* 107 */     nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
/* 108 */     context.put("userdefitemContainer1", bean);
/* 109 */     bean.setContext(getContext());
/* 110 */     bean.setParams(getManagedList10());
/* 111 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 112 */     invokeInitializingBean(bean);
/* 113 */     return bean;
/*     */   }
/*     */   
/* 116 */   private List getManagedList10() { List list = new java.util.ArrayList();list.add(getQueryParam_e433c1());return list;
/*     */   }
/*     */   
/* 119 */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_e433c1() { if (context.get("nc.ui.uif2.userdefitem.QueryParam#e433c1") != null)
/* 120 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#e433c1");
/* 121 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/* 122 */     context.put("nc.ui.uif2.userdefitem.QueryParam#e433c1", bean);
/* 123 */     bean.setMdfullname("ic.StateAdjustVO");
/* 124 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 125 */     invokeInitializingBean(bean);
/* 126 */     return bean;
/*     */   }

/*     */   
/*     */   public nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getUserdefAndMarAsstListPreparator() {
/* 148 */     if (context.get("userdefAndMarAsstListPreparator") != null)
/* 149 */       return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare)context.get("userdefAndMarAsstListPreparator");
/* 150 */     nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
/* 151 */     context.put("userdefAndMarAsstListPreparator", bean);
/* 152 */     bean.setBillListDataPrepares(getManagedList1());
/* 153 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 154 */     invokeInitializingBean(bean);
/* 155 */     return bean;
/*     */   }
/*     */   
/* 158 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getMarAsstPreparator());list.add(getDefItemPreparator());return list;
/*     */   }
/*     */   
/* 161 */   public nc.ui.uif2.editor.value.BillCardPanelMetaDataValueAdapter getComponentValueManager() { if (context.get("componentValueManager") != null)
/* 162 */       return (nc.ui.uif2.editor.value.BillCardPanelMetaDataValueAdapter)context.get("componentValueManager");
/* 163 */     nc.ui.uif2.editor.value.BillCardPanelMetaDataValueAdapter bean = new nc.ui.uif2.editor.value.BillCardPanelMetaDataValueAdapter();
/* 164 */     context.put("componentValueManager", bean);
/* 165 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 166 */     invokeInitializingBean(bean);
/* 167 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender getBillTemplateMender() {
/* 171 */     if (context.get("billTemplateMender") != null)
/* 172 */       return (nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender)context.get("billTemplateMender");
/* 173 */     nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender bean = new nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeBillTemplateMender(getContext());context.put("billTemplateMender", bean);
/* 174 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 175 */     invokeInitializingBean(bean);
/* 176 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getVadjustBillMediator() {
/* 180 */     if (context.get("vadjustBillMediator") != null)
/* 181 */       return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator)context.get("vadjustBillMediator");
/* 182 */     nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
/* 183 */     context.put("vadjustBillMediator", bean);
/* 184 */     bean.setModel(getModel());
/* 185 */     bean.setSrcBillIdField("cadjustbillid");
/* 186 */     bean.setSrcBillNOField("cadjustbillcode");
/* 187 */     bean.setSrcBillTypeField("cadjustbilltype");
/* 188 */     bean.setSrcBillTypeFieldPos(Integer.valueOf(0));
/* 189 */     bean.setSrcBillNOFieldPos(Integer.valueOf(0));
/* 190 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 191 */     invokeInitializingBean(bean);
/* 192 */     return bean;
/*     */   }
/*     */   
/*     */   public StateAdjustBillListView getList() {
/* 196 */     if (context.get("list") != null)
/* 197 */       return (StateAdjustBillListView)context.get("list");
/* 198 */     StateAdjustBillListView bean = new StateAdjustBillListView();
/* 199 */     context.put("list", bean);
/* 200 */     bean.setModel(getModel());
/* 201 */     bean.setMultiSelectionEnable(true);
/* 202 */     bean.setMultiSelectionMode(1);
/* 203 */     bean.setTemplateContainer(getTemplateContainer());
/* 204 */     bean.setUserdefitemListPreparator(getUserdefAndMarAsstListPreparator());
/* 205 */     bean.initUI();
/* 206 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 207 */     invokeInitializingBean(bean);
/* 208 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.TangramContainer getContainer() {
/* 212 */     if (context.get("container") != null)
/* 213 */       return (nc.ui.uif2.TangramContainer)context.get("container");
/* 214 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 215 */     context.put("container", bean);
/* 216 */     bean.setConstraints(getManagedList2());
/* 217 */     bean.setActions(getManagedList3());
/* 218 */     bean.setEditActions(getManagedList4());
/* 219 */     bean.setModel(getModel());
/* 220 */     bean.initUI();
/* 221 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 222 */     invokeInitializingBean(bean);
/* 223 */     return bean;
/*     */   }
/*     */   
/* 226 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getDown());return list;
/*     */   }
/*     */   
/* 229 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getDown() { if (context.get("down") != null)
/* 230 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("down");
/* 231 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 232 */     context.put("down", bean);
/* 233 */     bean.setNewComponent(getList());
/* 234 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 235 */     invokeInitializingBean(bean);
/* 236 */     return bean;
/*     */   }
/*     */   
/* 239 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getQueryAction());list.add(getRefreshAction());list.add(getSeparatorAction());list.add(getStateAdjustAction());list.add(getSeparatorAction());list.add(getHisQueryAction());list.add(getSeparatorAction());list.add(getPrintMngAction());list.add(getSeparatorAction());list.add(getElectronicsignatureAction());list.add(getSeparatorAction());list.add(getElectronicsignatureHisAction()); list.add(getSeparatorAction()); list.add(getLabelPrintAction()); list.add(getSeparatorAction());    return list; }
/*     */   
/* 241 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getStateAdjustSaveAction());list.add(getCancelAction());return list;
/*     */   }
/*     */   
/* 244 */   public nc.ui.pubapp.uif2app.view.TemplateContainer getBatchtemplateContainer() { if (context.get("batchtemplateContainer") != null)
/* 245 */       return (nc.ui.pubapp.uif2app.view.TemplateContainer)context.get("batchtemplateContainer");
/* 246 */     nc.ui.pubapp.uif2app.view.TemplateContainer bean = new nc.ui.pubapp.uif2app.view.TemplateContainer();
/* 247 */     context.put("batchtemplateContainer", bean);
/* 248 */     bean.setContext(getContext());
/* 249 */     bean.setFuncode("40010815");
/* 250 */     bean.setPk_group("@@@@");
/* 251 */     bean.setNodeKeies(getManagedList5());
/* 252 */     bean.setBillTemplateMender(getBillTemplateMender());
/* 253 */     bean.load();
/* 254 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 255 */     invokeInitializingBean(bean);
/* 256 */     return bean;
/*     */   }
/*     */   
/* 259 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add("batchscm");return list;
/*     */   }
/*     */   
/* 262 */   public nc.ui.scmf.ic.batchcode.ref.ScmBatchRef getScmBatchRef() { if (context.get("scmBatchRef") != null)
/* 263 */       return (nc.ui.scmf.ic.batchcode.ref.ScmBatchRef)context.get("scmBatchRef");
/* 264 */     nc.ui.scmf.ic.batchcode.ref.ScmBatchRef bean = new nc.ui.scmf.ic.batchcode.ref.ScmBatchRef();
/* 265 */     context.put("scmBatchRef", bean);
/* 266 */     bean.setRefDlg(getScmBatchDlg_14bab6a());
/* 267 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 268 */     invokeInitializingBean(bean);
/* 269 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.scmf.ic.batchcode.ref.ScmBatchDlg getScmBatchDlg_14bab6a() {
/* 273 */     if (context.get("nc.ui.scmf.ic.batchcode.ref.ScmBatchDlg#14bab6a") != null)
/* 274 */       return (nc.ui.scmf.ic.batchcode.ref.ScmBatchDlg)context.get("nc.ui.scmf.ic.batchcode.ref.ScmBatchDlg#14bab6a");
/* 275 */     nc.ui.scmf.ic.batchcode.ref.ScmBatchDlg bean = new nc.ui.scmf.ic.batchcode.ref.ScmBatchDlg(getScmBatchRef_created_60299b(), getBatchtemplateContainer());context.put("nc.ui.scmf.ic.batchcode.ref.ScmBatchDlg#14bab6a", bean);
/* 276 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 277 */     invokeInitializingBean(bean);
/* 278 */     return bean;
/*     */   }
/*     */   
/*     */   private java.awt.Container getScmBatchRef_created_60299b() {
/* 282 */     if (context.get("scmBatchRef.created#60299b") != null)
/* 283 */       return (java.awt.Container)context.get("scmBatchRef.created#60299b");
/* 284 */     java.awt.Container bean = getScmBatchRef().getParent();
/* 285 */     context.put("scmBatchRef.created#60299b", bean);
/* 286 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 287 */     invokeInitializingBean(bean);
/* 288 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.deal.StateadjustDLGWrapper getQryDLGInitializer() {
/* 292 */     if (context.get("qryDLGInitializer") != null)
/* 293 */       return (nc.ui.ic.m4460.deal.StateadjustDLGWrapper)context.get("qryDLGInitializer");
/* 294 */     nc.ui.ic.m4460.deal.StateadjustDLGWrapper bean = new nc.ui.ic.m4460.deal.StateadjustDLGWrapper();
/* 295 */     context.put("qryDLGInitializer", bean);
/* 296 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 297 */     invokeInitializingBean(bean);
/* 298 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.deal.StateadjustOnhandDLGWrapper getOnhandQryDLGInitializer() {
/* 302 */     if (context.get("onhandQryDLGInitializer") != null)
/* 303 */       return (nc.ui.ic.m4460.deal.StateadjustOnhandDLGWrapper)context.get("onhandQryDLGInitializer");
/* 304 */     nc.ui.ic.m4460.deal.StateadjustOnhandDLGWrapper bean = new nc.ui.ic.m4460.deal.StateadjustOnhandDLGWrapper();
/* 305 */     context.put("onhandQryDLGInitializer", bean);
/* 306 */     bean.setScmBatchRef(getScmBatchRef());
/* 307 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 308 */     invokeInitializingBean(bean);
/* 309 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.SeparatorAction getSeparatorAction() {
/* 313 */     if (context.get("separatorAction") != null)
/* 314 */       return (nc.funcnode.ui.action.SeparatorAction)context.get("separatorAction");
/* 315 */     nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
/* 316 */     context.put("separatorAction", bean);
/* 317 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 318 */     invokeInitializingBean(bean);
/* 319 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.action.StateAdjustRefreshAction getRefreshAction() {
/* 323 */     if (context.get("refreshAction") != null)
/* 324 */       return (nc.ui.ic.m4460.action.StateAdjustRefreshAction)context.get("refreshAction");
/* 325 */     nc.ui.ic.m4460.action.StateAdjustRefreshAction bean = new nc.ui.ic.m4460.action.StateAdjustRefreshAction();
/* 326 */     context.put("refreshAction", bean);
/* 327 */     bean.setModel(getModel());
/* 328 */     bean.setQueryAction(getQueryAction());
/* 329 */     bean.setDataManager(getModelDataManager());
/* 330 */     bean.setListView(getList());
/* 331 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 332 */     invokeInitializingBean(bean);
/* 333 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.action.StateAdjustAction getStateAdjustAction() {
/* 337 */     if (context.get("stateAdjustAction") != null)
/* 338 */       return (nc.ui.ic.m4460.action.StateAdjustAction)context.get("stateAdjustAction");
/* 339 */     nc.ui.ic.m4460.action.StateAdjustAction bean = new nc.ui.ic.m4460.action.StateAdjustAction();
/* 340 */     context.put("stateAdjustAction", bean);
/* 341 */     bean.setModel(getModel());
/* 342 */     bean.setList(getList());
/* 343 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 344 */     invokeInitializingBean(bean);
/* 345 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.special.action.SelectAllAction getSelectAllAction() {
/* 349 */     if (context.get("selectAllAction") != null)
/* 350 */       return (nc.ui.ic.special.action.SelectAllAction)context.get("selectAllAction");
/* 351 */     nc.ui.ic.special.action.SelectAllAction bean = new nc.ui.ic.special.action.SelectAllAction();
/* 352 */     context.put("selectAllAction", bean);
/* 353 */     bean.setModel(getModel());
/* 354 */     bean.setList(getList());
/* 355 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 356 */     invokeInitializingBean(bean);
/* 357 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.special.action.UnSelectAction getUnSelectAction() {
/* 361 */     if (context.get("unSelectAction") != null)
/* 362 */       return (nc.ui.ic.special.action.UnSelectAction)context.get("unSelectAction");
/* 363 */     nc.ui.ic.special.action.UnSelectAction bean = new nc.ui.ic.special.action.UnSelectAction();
/* 364 */     context.put("unSelectAction", bean);
/* 365 */     bean.setModel(getModel());
/* 366 */     bean.setList(getList());
/* 367 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 368 */     invokeInitializingBean(bean);
/* 369 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.action.StateAdjustSaveAction getStateAdjustSaveAction() {
/* 373 */     if (context.get("stateAdjustSaveAction") != null)
/* 374 */       return (nc.ui.ic.m4460.action.StateAdjustSaveAction)context.get("stateAdjustSaveAction");
/* 375 */     nc.ui.ic.m4460.action.StateAdjustSaveAction bean = new nc.ui.ic.m4460.action.StateAdjustSaveAction();
/* 376 */     context.put("stateAdjustSaveAction", bean);
/* 377 */     bean.setList(getList());
/* 378 */     bean.setQueryAction(getQueryAction());
/* 379 */     bean.setModel(getModel());
/* 380 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 381 */     invokeInitializingBean(bean);
/* 382 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.action.StateAdjustCancelAction getCancelAction() {
/* 386 */     if (context.get("cancelAction") != null)
/* 387 */       return (nc.ui.ic.m4460.action.StateAdjustCancelAction)context.get("cancelAction");
/* 388 */     nc.ui.ic.m4460.action.StateAdjustCancelAction bean = new nc.ui.ic.m4460.action.StateAdjustCancelAction();
/* 389 */     context.put("cancelAction", bean);
/* 390 */     bean.setModel(getModel());
/* 391 */     bean.setQueryAction(getQueryAction());
/* 392 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 393 */     invokeInitializingBean(bean);
/* 394 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.deal.StateadjustBillDLGWrapper getBillQryDLGInitializer() {
/* 398 */     if (context.get("billQryDLGInitializer") != null)
/* 399 */       return (nc.ui.ic.m4460.deal.StateadjustBillDLGWrapper)context.get("billQryDLGInitializer");
/* 400 */     nc.ui.ic.m4460.deal.StateadjustBillDLGWrapper bean = new nc.ui.ic.m4460.deal.StateadjustBillDLGWrapper();
/* 401 */     context.put("billQryDLGInitializer", bean);
/* 402 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 403 */     invokeInitializingBean(bean);
/* 404 */     return bean;
/*     */   }
/*     */   
/*     */   public StateAdjustOnhandQueryAction getQueryAction() {
/* 408 */     if (context.get("queryAction") != null)
/* 409 */       return (StateAdjustOnhandQueryAction)context.get("queryAction");
/* 410 */     StateAdjustOnhandQueryAction bean = new StateAdjustOnhandQueryAction();
/* 411 */     context.put("queryAction", bean);
/* 412 */     bean.setModel(getModel());
/* 413 */     bean.setDataManager(getModelDataManager());
/* 414 */     bean.setQryCondDLGInitializer(getOnhandQryDLGInitializer());
/* 415 */     bean.setList(getList());
/* 416 */     bean.setHasQueryArea(false);
/* 417 */     bean.setNodeKey("4008100901");
/* 418 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 419 */     invokeInitializingBean(bean);
/* 420 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.action.StateAdjustBillQueryAction getHisQueryAction() {
/* 424 */     if (context.get("hisQueryAction") != null)
/* 425 */       return (nc.ui.ic.m4460.action.StateAdjustBillQueryAction)context.get("hisQueryAction");
/* 426 */     nc.ui.ic.m4460.action.StateAdjustBillQueryAction bean = new nc.ui.ic.m4460.action.StateAdjustBillQueryAction();
/* 427 */     context.put("hisQueryAction", bean);
/* 428 */     bean.setModel(getModel());
/* 429 */     bean.setDataManager(getModelDataManager());
/* 430 */     bean.setHasQueryArea(false);
/* 431 */     bean.setQryCondDLGInitializer(getBillQryDLGInitializer());
/* 432 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 433 */     invokeInitializingBean(bean);
/* 434 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getPrintMngAction() {
/* 438 */     if (context.get("printMngAction") != null)
/* 439 */       return (nc.funcnode.ui.action.GroupAction)context.get("printMngAction");
/* 440 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 441 */     context.put("printMngAction", bean);
/* 442 */     bean.setCode("printMngAction");
/* 443 */     bean.setName("¥Ú”°");
/* 444 */     bean.setActions(getManagedList6());
/* 445 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 446 */     invokeInitializingBean(bean);
/* 447 */     return bean;
/*     */   }
/*     */   
			public ElectronicsignatureAction getElectronicsignatureAction() {
/* 308 */     if (context.get("tidaiping") != null)
/* 309 */       return (ElectronicsignatureAction)context.get("tidaiping");
/* 310 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 311 */     context.put("tidaiping", bean);
/* 312 */     bean.setModel(getModel());
			  bean.setEditor(getList());
/* 314 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 315 */     invokeInitializingBean(bean);
/* 316 */     return bean;
/*     */   }
/*     */   
			public ElectronicsignatureHisAction getElectronicsignatureHisAction() {
/* 308 */     if (context.get("tidaiping1") != null)
/* 309 */       return (ElectronicsignatureHisAction)context.get("tidaiping1");
/* 310 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 311 */     context.put("tidaiping1", bean);
/* 312 */     bean.setModel(getModel());
bean.setEditor(getList());
/* 314 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 315 */     invokeInitializingBean(bean);
/* 316 */     return bean;
/*     */   }	
			
			public StateLabelPrintAction getLabelPrintAction() {
				/* 308 */     if (context.get("labelPrint") != null)
				/* 309 */       return (StateLabelPrintAction)context.get("labelPrint");
				/* 310 */     StateLabelPrintAction bean = new StateLabelPrintAction();
				/* 311 */     context.put("labelPrint", bean);
				/* 312 */     bean.setModel(getModel());
				bean.setList(getList());
				/* 314 */     setBeanFacotryIfBeanFacatoryAware(bean);
				/* 315 */     invokeInitializingBean(bean);
				/* 316 */     return bean;
				/*     */   }
			public UnQualifiedStateLabelPrintAction getUnLabelPrintAction() {
				/* 308 */     if (context.get("unlabelPrint") != null)
				/* 309 */       return (UnQualifiedStateLabelPrintAction)context.get("unlabelPrint");
				/* 310 */     UnQualifiedStateLabelPrintAction bean = new UnQualifiedStateLabelPrintAction();
				/* 311 */     context.put("unlabelPrint", bean);
				/* 312 */     bean.setModel(getModel());
				bean.setList(getList());
				/* 314 */     setBeanFacotryIfBeanFacatoryAware(bean);
				/* 315 */     invokeInitializingBean(bean);
				/* 316 */     return bean;
				/*     */   }
/* 450 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getTemplatePrintAction());list.add(getTemplatePreviewAction());list.add(getOutputAction());return list;
/*     */   }
/*     */   
/* 453 */   public nc.ui.ic.m4460.deal.StateAdjustPrintProcessor getPrintProcessor() { if (context.get("printProcessor") != null)
/* 454 */       return (nc.ui.ic.m4460.deal.StateAdjustPrintProcessor)context.get("printProcessor");
/* 455 */     nc.ui.ic.m4460.deal.StateAdjustPrintProcessor bean = new nc.ui.ic.m4460.deal.StateAdjustPrintProcessor();
/* 456 */     context.put("printProcessor", bean);
/* 457 */     bean.setModel(getModel());
/* 458 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 459 */     invokeInitializingBean(bean);
/* 460 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.action.StateAdjustOutputAction getOutputAction() {
/* 464 */     if (context.get("outputAction") != null)
/* 465 */       return (nc.ui.ic.m4460.action.StateAdjustOutputAction)context.get("outputAction");
/* 466 */     nc.ui.ic.m4460.action.StateAdjustOutputAction bean = new nc.ui.ic.m4460.action.StateAdjustOutputAction();
/* 467 */     context.put("outputAction", bean);
/* 468 */     bean.setModel(getModel());
/* 469 */     bean.setParent(getList());
/* 470 */     bean.setListView(getList());
/* 471 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 472 */     invokeInitializingBean(bean);
/* 473 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.action.StateAdjustPreviewAction getTemplatePreviewAction() {
/* 477 */     if (context.get("templatePreviewAction") != null)
/* 478 */       return (nc.ui.ic.m4460.action.StateAdjustPreviewAction)context.get("templatePreviewAction");
/* 479 */     nc.ui.ic.m4460.action.StateAdjustPreviewAction bean = new nc.ui.ic.m4460.action.StateAdjustPreviewAction();
/* 480 */     context.put("templatePreviewAction", bean);
/* 481 */     bean.setPreview(true);
/* 482 */     bean.setModel(getModel());
/* 483 */     bean.setListView(getList());
/* 484 */     bean.setPrintProcessor(getPrintProcessor());
/* 485 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 486 */     invokeInitializingBean(bean);
/* 487 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.action.StateAdjustPrintAction getTemplatePrintAction() {
/* 491 */     if (context.get("templatePrintAction") != null)
/* 492 */       return (nc.ui.ic.m4460.action.StateAdjustPrintAction)context.get("templatePrintAction");
/* 493 */     nc.ui.ic.m4460.action.StateAdjustPrintAction bean = new nc.ui.ic.m4460.action.StateAdjustPrintAction();
/* 494 */     context.put("templatePrintAction", bean);
/* 495 */     bean.setPreview(false);
/* 496 */     bean.setModel(getModel());
/* 497 */     bean.setListView(getList());
/* 498 */     bean.setPrintProcessor(getPrintProcessor());
/* 499 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 500 */     invokeInitializingBean(bean);
/* 501 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.handler.AdjustnumHandler getNadjustnumHandler() {
/* 505 */     if (context.get("nadjustnumHandler") != null)
/* 506 */       return (nc.ui.ic.m4460.handler.AdjustnumHandler)context.get("nadjustnumHandler");
/* 507 */     nc.ui.ic.m4460.handler.AdjustnumHandler bean = new nc.ui.ic.m4460.handler.AdjustnumHandler();
/* 508 */     context.put("nadjustnumHandler", bean);
/* 509 */     bean.setModel(getModel());
/* 510 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 511 */     invokeInitializingBean(bean);
/* 512 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.handler.AdjustassistnumHandler getNadjustassistnumHandler() {
/* 516 */     if (context.get("nadjustassistnumHandler") != null)
/* 517 */       return (nc.ui.ic.m4460.handler.AdjustassistnumHandler)context.get("nadjustassistnumHandler");
/* 518 */     nc.ui.ic.m4460.handler.AdjustassistnumHandler bean = new nc.ui.ic.m4460.handler.AdjustassistnumHandler();
/* 519 */     context.put("nadjustassistnumHandler", bean);
/* 520 */     bean.setModel(getModel());
/* 521 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 522 */     invokeInitializingBean(bean);
/* 523 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.handler.CadjuststateidHandler getCadjuststateidHandler() {
/* 527 */     if (context.get("cadjuststateidHandler") != null)
/* 528 */       return (nc.ui.ic.m4460.handler.CadjuststateidHandler)context.get("cadjuststateidHandler");
/* 529 */     nc.ui.ic.m4460.handler.CadjuststateidHandler bean = new nc.ui.ic.m4460.handler.CadjuststateidHandler();
/* 530 */     context.put("cadjuststateidHandler", bean);
/* 531 */     bean.setModel(getModel());
/* 532 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 533 */     invokeInitializingBean(bean);
/* 534 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.m4460.handler.AdjustgrossnumHandler getNadjustgrossnumHandler() {
/* 538 */     if (context.get("nadjustgrossnumHandler") != null)
/* 539 */       return (nc.ui.ic.m4460.handler.AdjustgrossnumHandler)context.get("nadjustgrossnumHandler");
/* 540 */     nc.ui.ic.m4460.handler.AdjustgrossnumHandler bean = new nc.ui.ic.m4460.handler.AdjustgrossnumHandler();
/* 541 */     context.put("nadjustgrossnumHandler", bean);
/* 542 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 543 */     invokeInitializingBean(bean);
/* 544 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.pub.handler.list.ICListEditEventHandlerMap getParentListEditHandlerMap() {
/* 548 */     if (context.get("parentListEditHandlerMap") != null)
/* 549 */       return (nc.ui.ic.pub.handler.list.ICListEditEventHandlerMap)context.get("parentListEditHandlerMap");
/* 550 */     nc.ui.ic.pub.handler.list.ICListEditEventHandlerMap bean = new nc.ui.ic.pub.handler.list.ICListEditEventHandlerMap();
/* 551 */     context.put("parentListEditHandlerMap", bean);
/* 552 */     bean.setHandlerMap(getManagedMap0());
/* 553 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 554 */     invokeInitializingBean(bean);
/* 555 */     return bean;
/*     */   }
/*     */   
/* 558 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("nadjustnum", getNadjustnumHandler());map.put("nadjustassistnum", getNadjustassistnumHandler());map.put("cadjuststateid", getCadjuststateidHandler());map.put("nadjustgrossnum", getNadjustgrossnumHandler());return map;
/*     */   }
/*     */   
/* 561 */   public nc.ui.ic.pub.handler.list.ICListEditEventHandlerMediator getListHeadTailEditEventHandlerMediator() { if (context.get("listHeadTailEditEventHandlerMediator") != null)
/* 562 */       return (nc.ui.ic.pub.handler.list.ICListEditEventHandlerMediator)context.get("listHeadTailEditEventHandlerMediator");
/* 563 */     nc.ui.ic.pub.handler.list.ICListEditEventHandlerMediator bean = new nc.ui.ic.pub.handler.list.ICListEditEventHandlerMediator();
/* 564 */     context.put("listHeadTailEditEventHandlerMediator", bean);
/* 565 */     bean.setParentHandlerMap(getParentListEditHandlerMap());
/* 566 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 567 */     invokeInitializingBean(bean);
/* 568 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.model.AppEventHandlerMediator getAppEventHandlerMediator() {
/* 572 */     if (context.get("appEventHandlerMediator") != null)
/* 573 */       return (nc.ui.pubapp.uif2app.model.AppEventHandlerMediator)context.get("appEventHandlerMediator");
/* 574 */     nc.ui.pubapp.uif2app.model.AppEventHandlerMediator bean = new nc.ui.pubapp.uif2app.model.AppEventHandlerMediator();
/* 575 */     context.put("appEventHandlerMediator", bean);
/* 576 */     bean.setModel(getModel());
/* 577 */     bean.setHandlerMap(getManagedMap1());
/* 578 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 579 */     invokeInitializingBean(bean);
/* 580 */     return bean;
/*     */   }
/*     */   
/* 583 */   private Map getManagedMap1() { Map map = new java.util.HashMap();map.put("nc.ui.pubapp.uif2app.event.list.ListHeadBeforeEditEvent", getManagedList7());map.put("nc.ui.pubapp.uif2app.event.list.ListHeadAfterEditEvent", getManagedList8());return map; }
/*     */   
/* 585 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getListHeadTailEditEventHandlerMediator());return list; }
/*     */   
/* 587 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getListHeadTailEditEventHandlerMediator());return list;
/*     */   }
/*     */   
/* 590 */   public nc.ui.ic.m4460.LabelPrintInitDataListener getInitDataListener() { if (context.get("InitDataListener") != null)
/* 591 */       return (nc.ui.ic.m4460.LabelPrintInitDataListener)context.get("InitDataListener");
/* 592 */     nc.ui.ic.m4460.LabelPrintInitDataListener bean = new nc.ui.ic.m4460.LabelPrintInitDataListener();
/* 593 */     context.put("InitDataListener", bean);
/* 594 */     bean.setModel(getIcBizModel());
bean.setAdjustModel(getModel());
/* 595 */     bean.setVoClassName("nc.vo.ic.m4460.entity.StateAdjustVO");
/* 596 */     bean.setAutoShowUpComponent(getList());
/* 597 */     bean.setQueryAction(getHisQueryAction());
/* 598 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 599 */     invokeInitializingBean(bean);
/* 600 */     return bean;
/*     */   }
/*     */ }