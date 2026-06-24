/*     */ package nc.ui.bd.material.prod;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.OutputAction;
/*     */ import nc.ui.uif2.actions.TemplatePrintAction;
/*     */ 
/*     */ public class material_prod_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_prod() { if (context.get("material_prod") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_prod");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_prod", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialProdListView());
/*  19 */     bean.setClosingListener(getMaterialProdClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialProdEditAction());list.add(getMaterialProdDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProdRefreshAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProdListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.prod.model.MaterialProdAppModelService getMaterialProdAppModelsService() { if (context.get("materialProdAppModelsService") != null)
/*  29 */       return (nc.ui.bd.material.prod.model.MaterialProdAppModelService)context.get("materialProdAppModelsService");
/*  30 */     nc.ui.bd.material.prod.model.MaterialProdAppModelService bean = new nc.ui.bd.material.prod.model.MaterialProdAppModelService();
/*  31 */     context.put("materialProdAppModelsService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialProdAppModel() {
/*  38 */     if (context.get("materialProdAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialProdAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialProdAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialProdAppModelsService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_c17ddb());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_c17ddb() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#c17ddb") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#c17ddb");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#c17ddb", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.prod.model.MaterialProdAppModelDataManager getMaterialProdModelDataManager() {
/*  62 */     if (context.get("materialProdModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.prod.model.MaterialProdAppModelDataManager)context.get("materialProdModelDataManager");
/*  64 */     nc.ui.bd.material.prod.model.MaterialProdAppModelDataManager bean = new nc.ui.bd.material.prod.model.MaterialProdAppModelDataManager();
/*  65 */     context.put("materialProdModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialProdAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialProdMediator() {
/*  73 */     if (context.get("materialProdMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialProdMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialProdMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialProdModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialProdAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialProdListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.assistant.MarAsstPubShowUtil getMaterialProdMarAsstMediator() {
/*  87 */     if (context.get("materialProdMarAsstMediator") != null)
/*  88 */       return (nc.ui.bd.material.assistant.MarAsstPubShowUtil)context.get("materialProdMarAsstMediator");
/*  89 */     nc.ui.bd.material.assistant.MarAsstPubShowUtil bean = new nc.ui.bd.material.assistant.MarAsstPubShowUtil();
/*  90 */     context.put("materialProdMarAsstMediator", bean);
/*  91 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/*  92 */     bean.setListEditor(getMaterialProdListView());
/*  93 */     bean.setCardEditor(getMaterialProdEditor());
/*  94 */     bean.setAsstPrecode("costvalutasst");
/*  95 */     bean.setPos("head");
/*  96 */     bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  97 */     bean.init();
/*  98 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  99 */     invokeInitializingBean(bean);
/* 100 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.prod.view.MaterialProdListView getMaterialProdListView() {
/* 104 */     if (context.get("materialProdListView") != null)
/* 105 */       return (nc.ui.bd.material.prod.view.MaterialProdListView)context.get("materialProdListView");
/* 106 */     nc.ui.bd.material.prod.view.MaterialProdListView bean = new nc.ui.bd.material.prod.view.MaterialProdListView();
/* 107 */     context.put("materialProdListView", bean);
/* 108 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 109 */     bean.setModel(getMaterialProdAppModel());
/* 110 */     bean.setNodekey("prod");
/* 111 */     bean.setName(getI18nFB_12dc9c4());
/* 112 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_eb6cb2());
/* 113 */     bean.initUI();
/* 114 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 115 */     invokeInitializingBean(bean);
/* 116 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_12dc9c4() {
/* 120 */     if (context.get("nc.ui.uif2.I18nFB#12dc9c4") != null)
/* 121 */       return (String)context.get("nc.ui.uif2.I18nFB#12dc9c4");
/* 122 */     I18nFB bean = new I18nFB();
/* 123 */     context.put("&nc.ui.uif2.I18nFB#12dc9c4", bean);bean.setResDir("10140mag");
/* 124 */     bean.setDefaultValue("生产信息");
/* 125 */     bean.setResId("110140mag0015");
/* 126 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 127 */     invokeInitializingBean(bean);
/*     */     try {
/* 129 */       Object product = bean.getObject();
/* 130 */       context.put("nc.ui.uif2.I18nFB#12dc9c4", product);
/* 131 */       return (String)product;
/*     */     } catch (Exception e) {
/* 133 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 136 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_eb6cb2() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#eb6cb2") != null)
/* 137 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#eb6cb2");
/* 138 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 139 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#eb6cb2", bean);
/* 140 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 141 */     bean.setParams(getManagedList1());
/* 142 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 143 */     invokeInitializingBean(bean);
/* 144 */     return bean;
/*     */   }
/*     */   
/* 147 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_1197f29());return list;
/*     */   }
/*     */   
/* 150 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1197f29() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1197f29") != null)
/* 151 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1197f29");
/* 152 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 153 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1197f29", bean);
/* 154 */     bean.setMdfullname("uap.materialprod");
/* 155 */     bean.setPos(0);
/* 156 */     bean.setPrefix("def");
/* 157 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 158 */     invokeInitializingBean(bean);
/* 159 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.prod.view.MaterialProdEditor getMaterialProdEditor() {
/* 163 */     if (context.get("materialProdEditor") != null)
/* 164 */       return (nc.ui.bd.material.prod.view.MaterialProdEditor)context.get("materialProdEditor");
/* 165 */     nc.ui.bd.material.prod.view.MaterialProdEditor bean = new nc.ui.bd.material.prod.view.MaterialProdEditor();
/* 166 */     context.put("materialProdEditor", bean);
/* 167 */     bean.setModel(getMaterialProdAppModel());
/* 168 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 169 */     bean.setNodekey("prod");
/* 170 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_1db1592());
/* 171 */     bean.setActions(getManagedList3());
/* 172 */     bean.initUI();
/* 173 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 174 */     invokeInitializingBean(bean);
/* 175 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_1db1592() {
/* 179 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#1db1592") != null)
/* 180 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#1db1592");
/* 181 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 182 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#1db1592", bean);
/* 183 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 184 */     bean.setParams(getManagedList2());
/* 185 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 186 */     invokeInitializingBean(bean);
/* 187 */     return bean;
/*     */   }
/*     */   
/* 190 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_2d3c70());return list;
/*     */   }
/*     */   
/* 193 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_2d3c70() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#2d3c70") != null)
/* 194 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#2d3c70");
/* 195 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 196 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#2d3c70", bean);
/* 197 */     bean.setMdfullname("uap.materialprod");
/* 198 */     bean.setPos(0);
/* 199 */     bean.setPrefix("def");
/* 200 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 201 */     invokeInitializingBean(bean);
/* 202 */     return bean;
/*     */   }
/*     */   
/* 205 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getMaterialProdFirstLineAction());list.add(getMaterialProdPreLineAction());list.add(getMaterialProdNextLineAction());list.add(getMaterialProdLastLineAction());return list;
/*     */   }
/*     */   
/* 208 */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialProdDialogMediator() { if (context.get("materialProdDialogMediator") != null)
/* 209 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialProdDialogMediator");
/* 210 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 211 */     context.put("materialProdDialogMediator", bean);
/* 212 */     bean.setModel(getMaterialProdAppModel());
/* 213 */     bean.setDialogContainer(getMaterialProdDialogContainer());
/* 214 */     bean.setName(getI18nFB_65535b());
/* 215 */     bean.setClosingListener(getMaterialProdClosingListener());
/* 216 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 217 */     invokeInitializingBean(bean);
/* 218 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_65535b() {
/* 222 */     if (context.get("nc.ui.uif2.I18nFB#65535b") != null)
/* 223 */       return (String)context.get("nc.ui.uif2.I18nFB#65535b");
/* 224 */     I18nFB bean = new I18nFB();
/* 225 */     context.put("&nc.ui.uif2.I18nFB#65535b", bean);bean.setResDir("10140mag");
/* 226 */     bean.setDefaultValue("生产信息");
/* 227 */     bean.setResId("110140mag0015");
/* 228 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 229 */     invokeInitializingBean(bean);
/*     */     try {
/* 231 */       Object product = bean.getObject();
/* 232 */       context.put("nc.ui.uif2.I18nFB#65535b", product);
/* 233 */       return (String)product;
/*     */     } catch (Exception e) {
/* 235 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 238 */   public nc.ui.uif2.TangramContainer getMaterialProdDialogContainer() { if (context.get("materialProdDialogContainer") != null)
/* 239 */       return (nc.ui.uif2.TangramContainer)context.get("materialProdDialogContainer");
/* 240 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 241 */     context.put("materialProdDialogContainer", bean);
/* 242 */     bean.setConstraints(getManagedList4());
/* 243 */     bean.setActions(getManagedList5());
/* 244 */     bean.setEditActions(getManagedList6());
/* 245 */     bean.setModel(getMaterialProdAppModel());
/* 246 */     bean.initUI();
/* 247 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 248 */     invokeInitializingBean(bean);
/* 249 */     return bean;
/*     */   }
/*     */   
/* 252 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_1f98032());return list;
/*     */   }
/*     */   
/* 255 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_1f98032() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#1f98032") != null)
/* 256 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#1f98032");
/* 257 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 258 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#1f98032", bean);
/* 259 */     bean.setNewComponent(getMaterialProdEditor());
/* 260 */     bean.setNewComponentName(getI18nFB_4381b8());
/* 261 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 262 */     invokeInitializingBean(bean);
/* 263 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_4381b8() {
/* 267 */     if (context.get("nc.ui.uif2.I18nFB#4381b8") != null)
/* 268 */       return (String)context.get("nc.ui.uif2.I18nFB#4381b8");
/* 269 */     I18nFB bean = new I18nFB();
/* 270 */     context.put("&nc.ui.uif2.I18nFB#4381b8", bean);bean.setResDir("10140mag");
/* 271 */     bean.setDefaultValue("生产信息");
/* 272 */     bean.setResId("110140mag0015");
/* 273 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 274 */     invokeInitializingBean(bean);
/*     */     try {
/* 276 */       Object product = bean.getObject();
/* 277 */       context.put("nc.ui.uif2.I18nFB#4381b8", product);
/* 278 */       return (String)product;
/*     */     } catch (Exception e) {
/* 280 */       throw new RuntimeException(e); } }
/*     */   
/* 282 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getMaterialProdEditAction());list.add(getMaterialProdDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProdRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProdPrintActionGroup());return list; }
/*     */   
/* 284 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialProdSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProdCancelAction());return list;
/*     */   }
/*     */   
/* 287 */   public nc.ui.uif2.actions.EditAction getMaterialProdEditAction() { if (context.get("materialProdEditAction") != null)
/* 288 */       return (nc.ui.uif2.actions.EditAction)context.get("materialProdEditAction");
/* 289 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 290 */     context.put("materialProdEditAction", bean);
/* 291 */     bean.setCode("PrEdit");
/* 292 */     bean.setModel(getMaterialProdAppModel());
/* 293 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 294 */     invokeInitializingBean(bean);
/* 295 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialProdDeleteAction() {
/* 299 */     if (context.get("materialProdDeleteAction") != null)
/* 300 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialProdDeleteAction");
/* 301 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 302 */     context.put("materialProdDeleteAction", bean);
/* 303 */     bean.setCode("PrDelete");
/* 304 */     bean.setModel(getMaterialProdAppModel());
/* 305 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 306 */     invokeInitializingBean(bean);
/* 307 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.SaveAction getMaterialProdSaveAction() {
/* 311 */     if (context.get("materialProdSaveAction") != null)
/* 312 */       return (nc.ui.uif2.actions.SaveAction)context.get("materialProdSaveAction");
/* 313 */     nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
/* 314 */     context.put("materialProdSaveAction", bean);
/* 315 */     bean.setCode("PrSave");
/* 316 */     bean.setModel(getMaterialProdAppModel());
/* 317 */     bean.setEditor(getMaterialProdEditor());
/* 318 */     bean.setValidationService(getMaterialProdValidationService());
/* 319 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 320 */     invokeInitializingBean(bean);
/* 321 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.bs.uif2.validation.DefaultValidationService getMaterialProdValidationService() {
/* 325 */     if (context.get("materialProdValidationService") != null)
/* 326 */       return (nc.bs.uif2.validation.DefaultValidationService)context.get("materialProdValidationService");
/* 327 */     nc.bs.uif2.validation.DefaultValidationService bean = new nc.bs.uif2.validation.DefaultValidationService();
/* 328 */     context.put("materialProdValidationService", bean);
/* 329 */     bean.setValidators(getManagedList7());
/* 330 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 331 */     invokeInitializingBean(bean);
/* 332 */     return bean;
/*     */   }
/*     */   
/* 335 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialProdSaveValidator_14f9441());return list;
/*     */   }
/*     */   
/* 338 */   private nc.bs.bd.material.prod.validator.MaterialProdSaveValidator getMaterialProdSaveValidator_14f9441() { if (context.get("nc.bs.bd.material.prod.validator.MaterialProdSaveValidator#14f9441") != null)
/* 339 */       return (nc.bs.bd.material.prod.validator.MaterialProdSaveValidator)context.get("nc.bs.bd.material.prod.validator.MaterialProdSaveValidator#14f9441");
/* 340 */     nc.bs.bd.material.prod.validator.MaterialProdSaveValidator bean = new nc.bs.bd.material.prod.validator.MaterialProdSaveValidator();
/* 341 */     context.put("nc.bs.bd.material.prod.validator.MaterialProdSaveValidator#14f9441", bean);
/* 342 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 343 */     invokeInitializingBean(bean);
/* 344 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialProdCancelAction() {
/* 348 */     if (context.get("materialProdCancelAction") != null)
/* 349 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialProdCancelAction");
/* 350 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 351 */     context.put("materialProdCancelAction", bean);
/* 352 */     bean.setCode("PrCancel");
/* 353 */     bean.setModel(getMaterialProdAppModel());
/* 354 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 355 */     invokeInitializingBean(bean);
/* 356 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialProdRefreshSingleAction() {
/* 360 */     if (context.get("materialProdRefreshSingleAction") != null)
/* 361 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialProdRefreshSingleAction");
/* 362 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 363 */     context.put("materialProdRefreshSingleAction", bean);
/* 364 */     bean.setCode("PrRefresh");
/* 365 */     bean.setModel(getMaterialProdAppModel());
/* 366 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 367 */     invokeInitializingBean(bean);
/* 368 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialProdRefreshAction() {
/* 372 */     if (context.get("materialProdRefreshAction") != null)
/* 373 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialProdRefreshAction");
/* 374 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 375 */     context.put("materialProdRefreshAction", bean);
/* 376 */     bean.setCode("PrRefresh");
/* 377 */     bean.setModel(getMaterialProdAppModel());
/* 378 */     bean.setManager(getMaterialProdModelDataManager());
/* 379 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 380 */     invokeInitializingBean(bean);
/* 381 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialProdListPrintActionGroup() {
/* 385 */     if (context.get("materialProdListPrintActionGroup") != null)
/* 386 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialProdListPrintActionGroup");
/* 387 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 388 */     context.put("materialProdListPrintActionGroup", bean);
/* 389 */     bean.setCode("PrPrintMenu");
/* 390 */     bean.setActions(getManagedList8());
/* 391 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 392 */     invokeInitializingBean(bean);
/* 393 */     return bean;
/*     */   }
/*     */   
/* 396 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialProdListTempletPrintAction());list.add(getMaterialProdListTempletPreviewAction());list.add(getMaterialProdListOutputAction());return list;
/*     */   }
/*     */   
/* 399 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialProdListTempletPreviewAction() { if (context.get("materialProdListTempletPreviewAction") != null)
/* 400 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialProdListTempletPreviewAction");
/* 401 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 402 */     context.put("materialProdListTempletPreviewAction", bean);
/* 403 */     bean.setCode("PrTempPreview");
/* 404 */     bean.setModel(getMaterialProdAppModel());
/* 405 */     bean.setNodeKey("prodlist");
/* 406 */     bean.setPrintDlgParentConatiner(getMaterialProdListView());
/* 407 */     bean.setDatasource(getMaterialProdListardDataSource());
/* 408 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 409 */     invokeInitializingBean(bean);
/* 410 */     return bean;
/*     */   }
/*     */   
/*     */   public TemplatePrintAction getMaterialProdListTempletPrintAction() {
/* 414 */     if (context.get("materialProdListTempletPrintAction") != null)
/* 415 */       return (TemplatePrintAction)context.get("materialProdListTempletPrintAction");
/* 416 */     TemplatePrintAction bean = new TemplatePrintAction();
/* 417 */     context.put("materialProdListTempletPrintAction", bean);
/* 418 */     bean.setCode("PrTempPrint");
/* 419 */     bean.setModel(getMaterialProdAppModel());
/* 420 */     bean.setNodeKey("prodlist");
/* 421 */     bean.setPrintDlgParentConatiner(getMaterialProdListView());
/* 422 */     bean.setDatasource(getMaterialProdListardDataSource());
/* 423 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 424 */     invokeInitializingBean(bean);
/* 425 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialProdListOutputAction() {
/* 429 */     if (context.get("materialProdListOutputAction") != null)
/* 430 */       return (OutputAction)context.get("materialProdListOutputAction");
/* 431 */     OutputAction bean = new OutputAction();
/* 432 */     context.put("materialProdListOutputAction", bean);
/* 433 */     bean.setCode("PrOutput");
/* 434 */     bean.setModel(getMaterialProdAppModel());
/* 435 */     bean.setNodeKey("prodlist");
/* 436 */     bean.setPrintDlgParentConatiner(getMaterialProdListView());
/* 437 */     bean.setDatasource(getMaterialProdListardDataSource());
/* 438 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 439 */     invokeInitializingBean(bean);
/* 440 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialProdListardDataSource() {
/* 444 */     if (context.get("materialProdListardDataSource") != null)
/* 445 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialProdListardDataSource");
/* 446 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 447 */     context.put("materialProdListardDataSource", bean);
/* 448 */     bean.setModel(getMaterialProdAppModel());
/* 449 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 450 */     invokeInitializingBean(bean);
/* 451 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialProdPrintActionGroup() {
/* 455 */     if (context.get("materialProdPrintActionGroup") != null)
/* 456 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialProdPrintActionGroup");
/* 457 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 458 */     context.put("materialProdPrintActionGroup", bean);
/* 459 */     bean.setCode("PrPrintMenu");
/* 460 */     bean.setActions(getManagedList9());
/* 461 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 462 */     invokeInitializingBean(bean);
/* 463 */     return bean;
/*     */   }
/*     */   
/* 466 */   private List getManagedList9() { List list = new java.util.ArrayList();list.add(getMaterialProdTempletPrintAction());list.add(getMaterialProdTempletPreviewAction());list.add(getMaterialProdOutputAction());return list;
/*     */   }
/*     */   
/* 469 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialProdTempletPreviewAction() { if (context.get("materialProdTempletPreviewAction") != null)
/* 470 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialProdTempletPreviewAction");
/* 471 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 472 */     context.put("materialProdTempletPreviewAction", bean);
/* 473 */     bean.setCode("PrTempPreview");
/* 474 */     bean.setModel(getMaterialProdAppModel());
/* 475 */     bean.setNodeKey("prodcard");
/* 476 */     bean.setPrintDlgParentConatiner(getMaterialProdEditor());
/* 477 */     bean.setDatasource(getMaterialProdCardDataSource());
/* 478 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 479 */     invokeInitializingBean(bean);
/* 480 */     return bean;
/*     */   }
/*     */   
/*     */   public TemplatePrintAction getMaterialProdTempletPrintAction() {
/* 484 */     if (context.get("materialProdTempletPrintAction") != null)
/* 485 */       return (TemplatePrintAction)context.get("materialProdTempletPrintAction");
/* 486 */     TemplatePrintAction bean = new TemplatePrintAction();
/* 487 */     context.put("materialProdTempletPrintAction", bean);
/* 488 */     bean.setCode("PrTempPrint");
/* 489 */     bean.setModel(getMaterialProdAppModel());
/* 490 */     bean.setNodeKey("prodcard");
/* 491 */     bean.setPrintDlgParentConatiner(getMaterialProdEditor());
/* 492 */     bean.setDatasource(getMaterialProdCardDataSource());
/* 493 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 494 */     invokeInitializingBean(bean);
/* 495 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialProdOutputAction() {
/* 499 */     if (context.get("materialProdOutputAction") != null)
/* 500 */       return (OutputAction)context.get("materialProdOutputAction");
/* 501 */     OutputAction bean = new OutputAction();
/* 502 */     context.put("materialProdOutputAction", bean);
/* 503 */     bean.setCode("PrOutput");
/* 504 */     bean.setModel(getMaterialProdAppModel());
/* 505 */     bean.setNodeKey("prodcard");
/* 506 */     bean.setPrintDlgParentConatiner(getMaterialProdEditor());
/* 507 */     bean.setDatasource(getMaterialProdCardDataSource());
/* 508 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 509 */     invokeInitializingBean(bean);
/* 510 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialProdCardDataSource() {
/* 514 */     if (context.get("materialProdCardDataSource") != null)
/* 515 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialProdCardDataSource");
/* 516 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 517 */     context.put("materialProdCardDataSource", bean);
/* 518 */     bean.setModel(getMaterialProdAppModel());
/* 519 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 520 */     invokeInitializingBean(bean);
/* 521 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMaterialProdFirstLineAction() {
/* 525 */     if (context.get("materialProdFirstLineAction") != null)
/* 526 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materialProdFirstLineAction");
/* 527 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 528 */     context.put("materialProdFirstLineAction", bean);
/* 529 */     bean.setModel(getMaterialProdAppModel());
/* 530 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 531 */     invokeInitializingBean(bean);
/* 532 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialProdNextLineAction() {
/* 536 */     if (context.get("materialProdNextLineAction") != null)
/* 537 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialProdNextLineAction");
/* 538 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 539 */     context.put("materialProdNextLineAction", bean);
/* 540 */     bean.setModel(getMaterialProdAppModel());
/* 541 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 542 */     invokeInitializingBean(bean);
/* 543 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialProdPreLineAction() {
/* 547 */     if (context.get("materialProdPreLineAction") != null)
/* 548 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialProdPreLineAction");
/* 549 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 550 */     context.put("materialProdPreLineAction", bean);
/* 551 */     bean.setModel(getMaterialProdAppModel());
/* 552 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 553 */     invokeInitializingBean(bean);
/* 554 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialProdLastLineAction() {
/* 558 */     if (context.get("materialProdLastLineAction") != null)
/* 559 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialProdLastLineAction");
/* 560 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 561 */     context.put("materialProdLastLineAction", bean);
/* 562 */     bean.setModel(getMaterialProdAppModel());
/* 563 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 564 */     invokeInitializingBean(bean);
/* 565 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialProdClosingListener() {
/* 569 */     if (context.get("materialProdClosingListener") != null)
/* 570 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialProdClosingListener");
/* 571 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 572 */     context.put("materialProdClosingListener", bean);
/* 573 */     bean.setModel(getMaterialProdAppModel());
/* 574 */     bean.setSaveaction(getMaterialProdSaveAction());
/* 575 */     bean.setCancelaction(getMaterialProdCancelAction());
/* 576 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 577 */     invokeInitializingBean(bean);
/* 578 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialProdAppModel());
/* 641 */     bean.setEditor(getMaterialProdEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialProdAppModel());
/* 641 */     bean.setEditor(getMaterialProdEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }