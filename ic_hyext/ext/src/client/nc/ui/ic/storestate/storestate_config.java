/*     */ package nc.ui.ic.storestate;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

/*     */ import nc.funcnode.ui.action.GroupAction;
/*     */ import nc.funcnode.ui.action.SeparatorAction;
import nc.ui.ic.storestate.action.ElectronicsignatureAction;
import nc.ui.ic.storestate.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ 
/*     */ public class storestate_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.vo.uif2.LoginContext getContext() { if (context.get("context") != null)
/*  14 */       return (nc.vo.uif2.LoginContext)context.get("context");
/*  15 */     nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
/*  16 */     context.put("context", bean);
/*  17 */     bean.setNodeType(nc.vo.bd.pub.NODE_TYPE.GROUP_NODE);
/*  18 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  19 */     invokeInitializingBean(bean);
/*  20 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.model.DefaultBatchValidationService getValidateSerice() {
/*  24 */     if (context.get("validateSerice") != null)
/*  25 */       return (nc.ui.uif2.model.DefaultBatchValidationService)context.get("validateSerice");
/*  26 */     nc.ui.uif2.model.DefaultBatchValidationService bean = new nc.ui.uif2.model.DefaultBatchValidationService();
/*  27 */     context.put("validateSerice", bean);
/*  28 */     bean.setEditor(getEditor());
/*  29 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  30 */     invokeInitializingBean(bean);
/*  31 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.pub.smart.SmartBatchAppModelService getService() {
/*  35 */     if (context.get("service") != null)
/*  36 */       return (nc.ui.pubapp.pub.smart.SmartBatchAppModelService)context.get("service");
/*  37 */     nc.ui.pubapp.pub.smart.SmartBatchAppModelService bean = new nc.ui.pubapp.pub.smart.SmartBatchAppModelService();
/*  38 */     context.put("service", bean);
/*  39 */     bean.setServiceItf("nc.itf.ic.storestate.IStoreStateService");
/*  40 */     bean.setVoClass("nc.vo.ic.storestate.StoreStateVO");
/*  41 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  42 */     invokeInitializingBean(bean);
/*  43 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.vo.bd.meta.BDObjectAdpaterFactory getBoadatorfactory() {
/*  47 */     if (context.get("boadatorfactory") != null)
/*  48 */       return (nc.vo.bd.meta.BDObjectAdpaterFactory)context.get("boadatorfactory");
/*  49 */     nc.vo.bd.meta.BDObjectAdpaterFactory bean = new nc.vo.bd.meta.BDObjectAdpaterFactory();
/*  50 */     context.put("boadatorfactory", bean);
/*  51 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  52 */     invokeInitializingBean(bean);
/*  53 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.editor.TemplateContainer getTemplateContainer() {
/*  57 */     if (context.get("templateContainer") != null)
/*  58 */       return (nc.ui.uif2.editor.TemplateContainer)context.get("templateContainer");
/*  59 */     nc.ui.uif2.editor.TemplateContainer bean = new nc.ui.uif2.editor.TemplateContainer();
/*  60 */     context.put("templateContainer", bean);
/*  61 */     bean.setContext(getContext());
/*  62 */     bean.load();
/*  63 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  64 */     invokeInitializingBean(bean);
/*  65 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.model.BatchBillTableModel getAppmodel() {
/*  69 */     if (context.get("appmodel") != null)
/*  70 */       return (nc.ui.pubapp.uif2app.model.BatchBillTableModel)context.get("appmodel");
/*  71 */     nc.ui.pubapp.uif2app.model.BatchBillTableModel bean = new nc.ui.pubapp.uif2app.model.BatchBillTableModel();
/*  72 */     context.put("appmodel", bean);
/*  73 */     bean.setService(getService());
/*  74 */     bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
/*  75 */     bean.setContext(getContext());
/*  76 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  77 */     invokeInitializingBean(bean);
/*  78 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.common.validateservice.ClosingCheck getClosingListener() {
/*  82 */     if (context.get("ClosingListener") != null)
/*  83 */       return (nc.ui.pubapp.common.validateservice.ClosingCheck)context.get("ClosingListener");
/*  84 */     nc.ui.pubapp.common.validateservice.ClosingCheck bean = new nc.ui.pubapp.common.validateservice.ClosingCheck();
/*  85 */     context.put("ClosingListener", bean);
/*  86 */     bean.setModel(getAppmodel());
/*  87 */     bean.setSaveAction(getSaveAction());
/*  88 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  89 */     invokeInitializingBean(bean);
/*  90 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.storestate.model.StoreStateModelDataManager getModelDataManager() {
/*  94 */     if (context.get("modelDataManager") != null)
/*  95 */       return (nc.ui.ic.storestate.model.StoreStateModelDataManager)context.get("modelDataManager");
/*  96 */     nc.ui.ic.storestate.model.StoreStateModelDataManager bean = new nc.ui.ic.storestate.model.StoreStateModelDataManager();
/*  97 */     context.put("modelDataManager", bean);
/*  98 */     bean.setModel(getAppmodel());
/*  99 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 100 */     invokeInitializingBean(bean);
/* 101 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.view.BatchBillTable getEditor() {
/* 105 */     if (context.get("editor") != null)
/* 106 */       return (nc.ui.pubapp.uif2app.view.BatchBillTable)context.get("editor");
/* 107 */     nc.ui.pubapp.uif2app.view.BatchBillTable bean = new nc.ui.pubapp.uif2app.view.BatchBillTable();
/* 108 */     context.put("editor", bean);
/* 109 */     bean.setModel(getAppmodel());
/* 110 */     bean.setVoClassName("nc.vo.ic.storestate.StoreStateVO");
/* 111 */     bean.setComponentValueManager(getBillCardPanelMetaDataValueAdapter_85d291());
/* 112 */     bean.initUI();
/* 113 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 114 */     invokeInitializingBean(bean);
/* 115 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.pubapp.uif2app.view.value.BillCardPanelMetaDataValueAdapter getBillCardPanelMetaDataValueAdapter_85d291() {
/* 119 */     if (context.get("nc.ui.pubapp.uif2app.view.value.BillCardPanelMetaDataValueAdapter#85d291") != null)
/* 120 */       return (nc.ui.pubapp.uif2app.view.value.BillCardPanelMetaDataValueAdapter)context.get("nc.ui.pubapp.uif2app.view.value.BillCardPanelMetaDataValueAdapter#85d291");
/* 121 */     nc.ui.pubapp.uif2app.view.value.BillCardPanelMetaDataValueAdapter bean = new nc.ui.pubapp.uif2app.view.value.BillCardPanelMetaDataValueAdapter();
/* 122 */     context.put("nc.ui.pubapp.uif2app.view.value.BillCardPanelMetaDataValueAdapter#85d291", bean);
/* 123 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 124 */     invokeInitializingBean(bean);
/* 125 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.storestate.action.StorestateAddAction getAddAction() {
/* 129 */     if (context.get("addAction") != null)
/* 130 */       return (nc.ui.ic.storestate.action.StorestateAddAction)context.get("addAction");
/* 131 */     nc.ui.ic.storestate.action.StorestateAddAction bean = new nc.ui.ic.storestate.action.StorestateAddAction();
/* 132 */     context.put("addAction", bean);
/* 133 */     bean.setModel(getAppmodel());
/* 134 */     bean.setVoClassName("nc.vo.ic.storestate.StoreStateVO");
/* 135 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 136 */     invokeInitializingBean(bean);
/* 137 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.actions.batch.BatchEditAction getEditAction() {
/* 141 */     if (context.get("editAction") != null)
/* 142 */       return (nc.ui.pubapp.uif2app.actions.batch.BatchEditAction)context.get("editAction");
/* 143 */     nc.ui.pubapp.uif2app.actions.batch.BatchEditAction bean = new nc.ui.pubapp.uif2app.actions.batch.BatchEditAction();
/* 144 */     context.put("editAction", bean);
/* 145 */     bean.setModel(getAppmodel());
/* 146 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 147 */     invokeInitializingBean(bean);
/* 148 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.storestate.action.StorestateDeleteAction getDeleteAction() {
/* 152 */     if (context.get("deleteAction") != null)
/* 153 */       return (nc.ui.ic.storestate.action.StorestateDeleteAction)context.get("deleteAction");
/* 154 */     nc.ui.ic.storestate.action.StorestateDeleteAction bean = new nc.ui.ic.storestate.action.StorestateDeleteAction();
/* 155 */     context.put("deleteAction", bean);
/* 156 */     bean.setModel(getAppmodel());
/* 157 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 158 */     invokeInitializingBean(bean);
/* 159 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.storestate.action.StorestateSaveAction getSaveAction() {
/* 163 */     if (context.get("saveAction") != null)
/* 164 */       return (nc.ui.ic.storestate.action.StorestateSaveAction)context.get("saveAction");
/* 165 */     nc.ui.ic.storestate.action.StorestateSaveAction bean = new nc.ui.ic.storestate.action.StorestateSaveAction();
/* 166 */     context.put("saveAction", bean);
/* 167 */     bean.setModel(getAppmodel());
/* 168 */     bean.setEditor(getEditor());
/* 169 */     bean.setValidationService(getValidateSerice());
/* 170 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 171 */     invokeInitializingBean(bean);
/* 172 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.actions.batch.BatchCancelAction getCancelAction() {
/* 176 */     if (context.get("cancelAction") != null)
/* 177 */       return (nc.ui.pubapp.uif2app.actions.batch.BatchCancelAction)context.get("cancelAction");
/* 178 */     nc.ui.pubapp.uif2app.actions.batch.BatchCancelAction bean = new nc.ui.pubapp.uif2app.actions.batch.BatchCancelAction();
/* 179 */     context.put("cancelAction", bean);
/* 180 */     bean.setModel(getAppmodel());
/* 181 */     bean.setEditor(getEditor());
/* 182 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 183 */     invokeInitializingBean(bean);
/* 184 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.actions.batch.BatchRefreshAction getRefreshAction() {
/* 188 */     if (context.get("refreshAction") != null)
/* 189 */       return (nc.ui.pubapp.uif2app.actions.batch.BatchRefreshAction)context.get("refreshAction");
/* 190 */     nc.ui.pubapp.uif2app.actions.batch.BatchRefreshAction bean = new nc.ui.pubapp.uif2app.actions.batch.BatchRefreshAction();
/* 191 */     context.put("refreshAction", bean);
/* 192 */     bean.setModel(getAppmodel());
/* 193 */     bean.setModelManager(getModelDataManager());
/* 194 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 195 */     invokeInitializingBean(bean);
/* 196 */     return bean;
/*     */   }
/*     */   
/*     */   public GroupAction getEnableactiongroup() {
/* 200 */     if (context.get("enableactiongroup") != null)
/* 201 */       return (GroupAction)context.get("enableactiongroup");
/* 202 */     GroupAction bean = new GroupAction();
/* 203 */     context.put("enableactiongroup", bean);
/* 204 */     bean.setCode("seal");
/* 205 */     bean.setName(getI18nFB_1c3622c());
/* 206 */     bean.setActions(getManagedList0());
/* 207 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 208 */     invokeInitializingBean(bean);
/* 209 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_1c3622c() {
/* 213 */     if (context.get("nc.ui.uif2.I18nFB#1c3622c") != null)
/* 214 */       return (String)context.get("nc.ui.uif2.I18nFB#1c3622c");
/* 215 */     I18nFB bean = new I18nFB();
/* 216 */     context.put("&nc.ui.uif2.I18nFB#1c3622c", bean);bean.setResDir("4008001_0");
/* 217 */     bean.setResId("04008001-0740");
/* 218 */     bean.setDefaultValue("∑‚¥Ê");
/* 219 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 220 */     invokeInitializingBean(bean);
/*     */     try {
/* 222 */       Object product = bean.getObject();
/* 223 */       context.put("nc.ui.uif2.I18nFB#1c3622c", product);
/* 224 */       return (String)product;
/*     */     } catch (Exception e) {
/* 226 */       throw new RuntimeException(e); } }
/*     */   
/* 228 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getDisableaction());list.add(getEnableaction());return list;
/*     */   }
/*     */   
/* 231 */   public nc.ui.ic.storestate.action.StorestateDisableAction getDisableaction() { if (context.get("disableaction") != null)
/* 232 */       return (nc.ui.ic.storestate.action.StorestateDisableAction)context.get("disableaction");
/* 233 */     nc.ui.ic.storestate.action.StorestateDisableAction bean = new nc.ui.ic.storestate.action.StorestateDisableAction();
/* 234 */     context.put("disableaction", bean);
/* 235 */     bean.setModel(getAppmodel());
/* 236 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 237 */     invokeInitializingBean(bean);
/* 238 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.ic.storestate.action.StorestateEnableAction getEnableaction() {
/* 242 */     if (context.get("enableaction") != null)
/* 243 */       return (nc.ui.ic.storestate.action.StorestateEnableAction)context.get("enableaction");
/* 244 */     nc.ui.ic.storestate.action.StorestateEnableAction bean = new nc.ui.ic.storestate.action.StorestateEnableAction();
/* 245 */     context.put("enableaction", bean);
/* 246 */     bean.setModel(getAppmodel());
/* 247 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 248 */     invokeInitializingBean(bean);
/* 249 */     return bean;
/*     */   }
/*     */   
/*     */   public GroupAction getPrintActionGroup() {
/* 253 */     if (context.get("printActionGroup") != null)
/* 254 */       return (GroupAction)context.get("printActionGroup");
/* 255 */     GroupAction bean = new GroupAction();
/* 256 */     context.put("printActionGroup", bean);
/* 257 */     bean.setCode("PrintMenu");
/* 258 */     bean.setName(getI18nFB_1347466());
/* 259 */     bean.setActions(getManagedList1());
/* 260 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 261 */     invokeInitializingBean(bean);
/* 262 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_1347466() {
/* 266 */     if (context.get("nc.ui.uif2.I18nFB#1347466") != null)
/* 267 */       return (String)context.get("nc.ui.uif2.I18nFB#1347466");
/* 268 */     I18nFB bean = new I18nFB();
/* 269 */     context.put("&nc.ui.uif2.I18nFB#1347466", bean);bean.setResDir("4008001_0");
/* 270 */     bean.setResId("04008001-0738");
/* 271 */     bean.setDefaultValue("¥Ú”°");
/* 272 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 273 */     invokeInitializingBean(bean);
/*     */     try {
/* 275 */       Object product = bean.getObject();
/* 276 */       context.put("nc.ui.uif2.I18nFB#1347466", product);
/* 277 */       return (String)product;
/*     */     } catch (Exception e) {
/* 279 */       throw new RuntimeException(e); } }
/*     */   
/* 281 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getPrintAction());list.add(getPreviewAction());list.add(getOutputAction());return list;
/*     */   }
/*     */   
/* 284 */   public nc.ui.scmf.pub.ic.action.GeneralMDPrintAction getPrintAction() { if (context.get("printAction") != null)
/* 285 */       return (nc.ui.scmf.pub.ic.action.GeneralMDPrintAction)context.get("printAction");
/* 286 */     nc.ui.scmf.pub.ic.action.GeneralMDPrintAction bean = new nc.ui.scmf.pub.ic.action.GeneralMDPrintAction();
/* 287 */     context.put("printAction", bean);
/* 288 */     bean.setPreview(false);
/* 289 */     bean.setModel(getAppmodel());
/* 290 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 291 */     invokeInitializingBean(bean);
/* 292 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.scmf.pub.ic.action.GeneralMDPreviewAction getPreviewAction() {
/* 296 */     if (context.get("previewAction") != null)
/* 297 */       return (nc.ui.scmf.pub.ic.action.GeneralMDPreviewAction)context.get("previewAction");
/* 298 */     nc.ui.scmf.pub.ic.action.GeneralMDPreviewAction bean = new nc.ui.scmf.pub.ic.action.GeneralMDPreviewAction();
/* 299 */     context.put("previewAction", bean);
/* 300 */     bean.setPreview(true);
/* 301 */     bean.setModel(getAppmodel());
/* 302 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 303 */     invokeInitializingBean(bean);
/* 304 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.pubapp.uif2app.actions.OutputAction getOutputAction() {
/* 308 */     if (context.get("outputAction") != null)
/* 309 */       return (nc.ui.pubapp.uif2app.actions.OutputAction)context.get("outputAction");
/* 310 */     nc.ui.pubapp.uif2app.actions.OutputAction bean = new nc.ui.pubapp.uif2app.actions.OutputAction();
/* 311 */     context.put("outputAction", bean);
/* 312 */     bean.setModel(getAppmodel());
/* 313 */     bean.setParent(getEditor());
/* 314 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 315 */     invokeInitializingBean(bean);
/* 316 */     return bean;
/*     */   }
			public ElectronicsignatureAction getElectronicsignatureAction() {
/* 308 */     if (context.get("electronicsignatureAction") != null)
/* 309 */       return (ElectronicsignatureAction)context.get("electronicsignatureAction");
/* 310 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 311 */     context.put("electronicsignatureAction", bean);
/* 312 */     bean.setModel(getAppmodel());
/* 313 */     bean.setEditor(getEditor());
/* 314 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 315 */     invokeInitializingBean(bean);
/* 316 */     return bean;
/*     */   }
/*     */   
			public ElectronicsignatureHisAction getElectronicsignatureHisAction() {
/* 308 */     if (context.get("electronicsignatureHisAction") != null)
/* 309 */       return (ElectronicsignatureHisAction)context.get("electronicsignatureHisAction");
/* 310 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 311 */     context.put("electronicsignatureHisAction", bean);
/* 312 */     bean.setModel(getAppmodel());
/* 313 */     bean.setEditor(getEditor());
/* 314 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 315 */     invokeInitializingBean(bean);
/* 316 */     return bean;
/*     */   }			
/*     */   public nc.ui.uif2.TangramContainer getContainer() {
/* 320 */     if (context.get("container") != null)
/* 321 */       return (nc.ui.uif2.TangramContainer)context.get("container");
/* 322 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 323 */     context.put("container", bean);
/* 324 */     bean.setTangramLayoutRoot(getCNode_c5f808());
/* 325 */     bean.setActions(getManagedList2());
/* 326 */     bean.setEditActions(getManagedList3());
/* 327 */     bean.setModel(getAppmodel());
/* 328 */     bean.initUI();
/* 329 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 330 */     invokeInitializingBean(bean);
/* 331 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_c5f808() {
/* 335 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#c5f808") != null)
/* 336 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#c5f808");
/* 337 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/* 338 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#c5f808", bean);
/* 339 */     bean.setComponent(getEditor());
/* 340 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 341 */     invokeInitializingBean(bean);
/* 342 */     return bean;
/*     */   }
/*     */   
/* 345 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getAddAction());list.add(getEditAction());list.add(getDeleteAction());list.add(getSeparatorAction_11f1223());list.add(getRefreshAction());list.add(getSeparatorAction_dfd835());list.add(getEnableactiongroup());list.add(getSeparatorAction_1eb883a());list.add(getPrintActionGroup());list.add(getSeparatorAction_1eb883a());list.add(getElectronicsignatureAction());list.add(getSeparatorAction_1eb883a());list.add(getElectronicsignatureAction());return list;
/*     */   }
/*     */   
/* 348 */   private SeparatorAction getSeparatorAction_11f1223() { if (context.get("nc.funcnode.ui.action.SeparatorAction#11f1223") != null)
/* 349 */       return (SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#11f1223");
/* 350 */     SeparatorAction bean = new SeparatorAction();
/* 351 */     context.put("nc.funcnode.ui.action.SeparatorAction#11f1223", bean);
/* 352 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 353 */     invokeInitializingBean(bean);
/* 354 */     return bean;
/*     */   }
/*     */   
/*     */   private SeparatorAction getSeparatorAction_dfd835() {
/* 358 */     if (context.get("nc.funcnode.ui.action.SeparatorAction#dfd835") != null)
/* 359 */       return (SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#dfd835");
/* 360 */     SeparatorAction bean = new SeparatorAction();
/* 361 */     context.put("nc.funcnode.ui.action.SeparatorAction#dfd835", bean);
/* 362 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 363 */     invokeInitializingBean(bean);
/* 364 */     return bean;
/*     */   }
/*     */   
/*     */   private SeparatorAction getSeparatorAction_1eb883a() {
/* 368 */     if (context.get("nc.funcnode.ui.action.SeparatorAction#1eb883a") != null)
/* 369 */       return (SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#1eb883a");
/* 370 */     SeparatorAction bean = new SeparatorAction();
/* 371 */     context.put("nc.funcnode.ui.action.SeparatorAction#1eb883a", bean);
/* 372 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 373 */     invokeInitializingBean(bean);
/* 374 */     return bean;
/*     */   }
/*     */   
/* 377 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getAddAction());list.add(getDeleteAction());list.add(getSeparatorAction_3cc4ee());list.add(getSaveAction());list.add(getCancelAction());return list;
/*     */   }
/*     */   
/* 380 */   private SeparatorAction getSeparatorAction_3cc4ee() { if (context.get("nc.funcnode.ui.action.SeparatorAction#3cc4ee") != null)
/* 381 */       return (SeparatorAction)context.get("nc.funcnode.ui.action.SeparatorAction#3cc4ee");
/* 382 */     SeparatorAction bean = new SeparatorAction();
/* 383 */     context.put("nc.funcnode.ui.action.SeparatorAction#3cc4ee", bean);
/* 384 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 385 */     invokeInitializingBean(bean);
/* 386 */     return bean;
/*     */   }
/*     */ }