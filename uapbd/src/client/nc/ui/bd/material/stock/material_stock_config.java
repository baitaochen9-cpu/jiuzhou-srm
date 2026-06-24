/*     */ package nc.ui.bd.material.stock;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.OutputAction;
/*     */ import nc.ui.uif2.editor.UserdefQueryParam;
/*     */ 
/*     */ public class material_stock_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_stock() { if (context.get("material_stock") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_stock");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_stock", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialStockListView());
/*  19 */     bean.setClosingListener(getMaterialStockClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialStockEditAction());list.add(getMaterialStockDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialStockRefreshAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialStockListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.stock.model.MaterialStockAppModelService getMaterialStockAppModelService() { if (context.get("materialStockAppModelService") != null)
/*  29 */       return (nc.ui.bd.material.stock.model.MaterialStockAppModelService)context.get("materialStockAppModelService");
/*  30 */     nc.ui.bd.material.stock.model.MaterialStockAppModelService bean = new nc.ui.bd.material.stock.model.MaterialStockAppModelService();
/*  31 */     context.put("materialStockAppModelService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialStockAppModel() {
/*  38 */     if (context.get("materialStockAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialStockAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialStockAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialStockAppModelService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_783d8f());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_783d8f() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#783d8f") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#783d8f");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#783d8f", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.stock.model.MaterialStockAppModelDataManager getMaterialStockModelDataManager() {
/*  62 */     if (context.get("materialStockModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.stock.model.MaterialStockAppModelDataManager)context.get("materialStockModelDataManager");
/*  64 */     nc.ui.bd.material.stock.model.MaterialStockAppModelDataManager bean = new nc.ui.bd.material.stock.model.MaterialStockAppModelDataManager();
/*  65 */     context.put("materialStockModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialStockAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialStockMediator() {
/*  73 */     if (context.get("materialStockMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialStockMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialStockMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialStockModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialStockAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialStockListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.stock.view.MaterialStockMarAsstShowUtil getMaterialStockMarAsstMediator() {
/*  87 */     if (context.get("materialStockMarAsstMediator") != null)
/*  88 */       return (nc.ui.bd.material.stock.view.MaterialStockMarAsstShowUtil)context.get("materialStockMarAsstMediator");
/*  89 */     nc.ui.bd.material.stock.view.MaterialStockMarAsstShowUtil bean = new nc.ui.bd.material.stock.view.MaterialStockMarAsstShowUtil();
/*  90 */     context.put("materialStockMarAsstMediator", bean);
/*  91 */     bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  92 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/*  93 */     bean.setListEditor(getMaterialStockListView());
/*  94 */     bean.setCardEditor(getMaterialStockEditor());
/*  95 */     bean.init();
/*  96 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  97 */     invokeInitializingBean(bean);
/*  98 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.stock.view.MaterialStockListView getMaterialStockListView() {
/* 102 */     if (context.get("materialStockListView") != null)
/* 103 */       return (nc.ui.bd.material.stock.view.MaterialStockListView)context.get("materialStockListView");
/* 104 */     nc.ui.bd.material.stock.view.MaterialStockListView bean = new nc.ui.bd.material.stock.view.MaterialStockListView();
/* 105 */     context.put("materialStockListView", bean);
/* 106 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 107 */     bean.setModel(getMaterialStockAppModel());
/* 108 */     bean.setNodekey("stock");
/* 109 */     bean.setName(getI18nFB_117786());
/* 110 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_dc1cec());
/* 111 */     bean.initUI();
/* 112 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 113 */     invokeInitializingBean(bean);
/* 114 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_117786() {
/* 118 */     if (context.get("nc.ui.uif2.I18nFB#117786") != null)
/* 119 */       return (String)context.get("nc.ui.uif2.I18nFB#117786");
/* 120 */     I18nFB bean = new I18nFB();
/* 121 */     context.put("&nc.ui.uif2.I18nFB#117786", bean);bean.setResDir("10140mag");
/* 122 */     bean.setDefaultValue("库存信息");
/* 123 */     bean.setResId("110140mag0028");
/* 124 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 125 */     invokeInitializingBean(bean);
/*     */     try {
/* 127 */       Object product = bean.getObject();
/* 128 */       context.put("nc.ui.uif2.I18nFB#117786", product);
/* 129 */       return (String)product;
/*     */     } catch (Exception e) {
/* 131 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 134 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_dc1cec() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#dc1cec") != null)
/* 135 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#dc1cec");
/* 136 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 137 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#dc1cec", bean);
/* 138 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 139 */     bean.setParams(getManagedList1());
/* 140 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 141 */     invokeInitializingBean(bean);
/* 142 */     return bean;
/*     */   }
/*     */   
/* 145 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_be2a8f());list.add(getListUserBodydefitemQueryParam());return list;
/*     */   }
/*     */   
/* 148 */   private UserdefQueryParam getUserdefQueryParam_be2a8f() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#be2a8f") != null)
/* 149 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#be2a8f");
/* 150 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 151 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#be2a8f", bean);
/* 152 */     bean.setMdfullname("uap.materialstock");
/* 153 */     bean.setPos(0);
/* 154 */     bean.setPrefix("def");
/* 155 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 156 */     invokeInitializingBean(bean);
/* 157 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getListUserBodydefitemQueryParam() {
/* 161 */     if (context.get("listUserBodydefitemQueryParam") != null)
/* 162 */       return (UserdefQueryParam)context.get("listUserBodydefitemQueryParam");
/* 163 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 164 */     context.put("listUserBodydefitemQueryParam", bean);
/* 165 */     bean.setMdfullname("uap.materialwarh");
/* 166 */     bean.setPos(1);
/* 167 */     bean.setPrefix("def");
/* 168 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 169 */     invokeInitializingBean(bean);
/* 170 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.stock.view.MaterialStockEditor getMaterialStockEditor() {
/* 174 */     if (context.get("materialStockEditor") != null)
/* 175 */       return (nc.ui.bd.material.stock.view.MaterialStockEditor)context.get("materialStockEditor");
/* 176 */     nc.ui.bd.material.stock.view.MaterialStockEditor bean = new nc.ui.bd.material.stock.view.MaterialStockEditor();
/* 177 */     context.put("materialStockEditor", bean);
/* 178 */     bean.setModel(getMaterialStockAppModel());
/* 179 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 180 */     bean.setNodekey("stock");
/* 181 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_1af8b05());
/* 182 */     bean.setBodyActionMap(getManagedMap0());
/* 183 */     bean.setActions(getManagedList4());
/* 184 */     bean.initUI();
/* 185 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 186 */     invokeInitializingBean(bean);
/* 187 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_1af8b05() {
/* 191 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#1af8b05") != null)
/* 192 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#1af8b05");
/* 193 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 194 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#1af8b05", bean);
/* 195 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 196 */     bean.setParams(getManagedList2());
/* 197 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 198 */     invokeInitializingBean(bean);
/* 199 */     return bean;
/*     */   }
/*     */   
/* 202 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_eef36d());list.add(getCardUserBodydefitem());return list;
/*     */   }
/*     */   
/* 205 */   private UserdefQueryParam getUserdefQueryParam_eef36d() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#eef36d") != null)
/* 206 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#eef36d");
/* 207 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 208 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#eef36d", bean);
/* 209 */     bean.setMdfullname("uap.materialstock");
/* 210 */     bean.setPos(0);
/* 211 */     bean.setPrefix("def");
/* 212 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 213 */     invokeInitializingBean(bean);
/* 214 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getCardUserBodydefitem() {
/* 218 */     if (context.get("cardUserBodydefitem") != null)
/* 219 */       return (UserdefQueryParam)context.get("cardUserBodydefitem");
/* 220 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 221 */     context.put("cardUserBodydefitem", bean);
/* 222 */     bean.setMdfullname("uap.materialwarh");
/* 223 */     bean.setPos(1);
/* 224 */     bean.setPrefix("def");
/* 225 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 226 */     invokeInitializingBean(bean);
/* 227 */     return bean;
/*     */   }
/*     */   
/* 230 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("materialwarh", getManagedList3());return map; }
/*     */   
/* 232 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getMaterialStockAddLineAction());list.add(getMaterialStockDelLineAction());return list; }
/*     */   
/* 234 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getMateriaStockFirstLineAction());list.add(getMaterialStockPreLineAction());list.add(getMaterialStockNextLineAction());list.add(getMaterialStockLastLineAction());return list;
/*     */   }
/*     */   
/* 237 */   public nc.ui.uif2.actions.AddLineAction getMaterialStockAddLineAction() { if (context.get("materialStockAddLineAction") != null)
/* 238 */       return (nc.ui.uif2.actions.AddLineAction)context.get("materialStockAddLineAction");
/* 239 */     nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
/* 240 */     context.put("materialStockAddLineAction", bean);
/* 241 */     bean.setModel(getMaterialStockAppModel());
/* 242 */     bean.setCardpanel(getMaterialStockEditor());
/* 243 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 244 */     invokeInitializingBean(bean);
/* 245 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DelLineAction getMaterialStockDelLineAction() {
/* 249 */     if (context.get("materialStockDelLineAction") != null)
/* 250 */       return (nc.ui.uif2.actions.DelLineAction)context.get("materialStockDelLineAction");
/* 251 */     nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
/* 252 */     context.put("materialStockDelLineAction", bean);
/* 253 */     bean.setModel(getMaterialStockAppModel());
/* 254 */     bean.setCardpanel(getMaterialStockEditor());
/* 255 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 256 */     invokeInitializingBean(bean);
/* 257 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialStockDialogMediator() {
/* 261 */     if (context.get("materialStockDialogMediator") != null)
/* 262 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialStockDialogMediator");
/* 263 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 264 */     context.put("materialStockDialogMediator", bean);
/* 265 */     bean.setModel(getMaterialStockAppModel());
/* 266 */     bean.setDialogContainer(getMaterialStockDialogContainer());
/* 267 */     bean.setName(getI18nFB_1dc68ed());
/* 268 */     bean.setClosingListener(getMaterialStockClosingListener());
/* 269 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 270 */     invokeInitializingBean(bean);
/* 271 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_1dc68ed() {
/* 275 */     if (context.get("nc.ui.uif2.I18nFB#1dc68ed") != null)
/* 276 */       return (String)context.get("nc.ui.uif2.I18nFB#1dc68ed");
/* 277 */     I18nFB bean = new I18nFB();
/* 278 */     context.put("&nc.ui.uif2.I18nFB#1dc68ed", bean);bean.setResDir("10140mag");
/* 279 */     bean.setDefaultValue("库存信息");
/* 280 */     bean.setResId("110140mag0028");
/* 281 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 282 */     invokeInitializingBean(bean);
/*     */     try {
/* 284 */       Object product = bean.getObject();
/* 285 */       context.put("nc.ui.uif2.I18nFB#1dc68ed", product);
/* 286 */       return (String)product;
/*     */     } catch (Exception e) {
/* 288 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 291 */   public nc.ui.uif2.TangramContainer getMaterialStockDialogContainer() { if (context.get("materialStockDialogContainer") != null)
/* 292 */       return (nc.ui.uif2.TangramContainer)context.get("materialStockDialogContainer");
/* 293 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 294 */     context.put("materialStockDialogContainer", bean);
/* 295 */     bean.setConstraints(getManagedList5());
/* 296 */     bean.setActions(getManagedList6());
/* 297 */     bean.setEditActions(getManagedList7());
/* 298 */     bean.setModel(getMaterialStockAppModel());
/* 299 */     bean.initUI();
/* 300 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 301 */     invokeInitializingBean(bean);
/* 302 */     return bean;
/*     */   }
/*     */   
/* 305 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_127d56c());return list;
/*     */   }
/*     */   
/* 308 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_127d56c() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#127d56c") != null)
/* 309 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#127d56c");
/* 310 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 311 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#127d56c", bean);
/* 312 */     bean.setNewComponent(getMaterialStockEditor());
/* 313 */     bean.setNewComponentName(getI18nFB_16ca29c());
/* 314 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 315 */     invokeInitializingBean(bean);
/* 316 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_16ca29c() {
/* 320 */     if (context.get("nc.ui.uif2.I18nFB#16ca29c") != null)
/* 321 */       return (String)context.get("nc.ui.uif2.I18nFB#16ca29c");
/* 322 */     I18nFB bean = new I18nFB();
/* 323 */     context.put("&nc.ui.uif2.I18nFB#16ca29c", bean);bean.setResDir("10140mag");
/* 324 */     bean.setDefaultValue("库存信息");
/* 325 */     bean.setResId("110140mag0028");
/* 326 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 327 */     invokeInitializingBean(bean);
/*     */     try {
/* 329 */       Object product = bean.getObject();
/* 330 */       context.put("nc.ui.uif2.I18nFB#16ca29c", product);
/* 331 */       return (String)product;
/*     */     } catch (Exception e) {
/* 333 */       throw new RuntimeException(e); } }
/*     */   
/* 335 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialStockEditAction());list.add(getMaterialStockDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialStockRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialStockPrintActionGroup());return list; }
/*     */   
/* 337 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialStockSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialStockCancelAction());return list;
/*     */   }
/*     */   
/* 340 */   public nc.ui.uif2.actions.EditAction getMaterialStockEditAction() { if (context.get("materialStockEditAction") != null)
/* 341 */       return (nc.ui.uif2.actions.EditAction)context.get("materialStockEditAction");
/* 342 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 343 */     context.put("materialStockEditAction", bean);
/* 344 */     bean.setCode("StEdit");
/* 345 */     bean.setModel(getMaterialStockAppModel());
/* 346 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 347 */     invokeInitializingBean(bean);
/* 348 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialStockDeleteAction() {
/* 352 */     if (context.get("materialStockDeleteAction") != null)
/* 353 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialStockDeleteAction");
/* 354 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 355 */     context.put("materialStockDeleteAction", bean);
/* 356 */     bean.setCode("StDelete");
/* 357 */     bean.setModel(getMaterialStockAppModel());
/* 358 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 359 */     invokeInitializingBean(bean);
/* 360 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.SaveAction getMaterialStockSaveAction() {
/* 364 */     if (context.get("materialStockSaveAction") != null)
/* 365 */       return (nc.ui.uif2.actions.SaveAction)context.get("materialStockSaveAction");
/* 366 */     nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
/* 367 */     context.put("materialStockSaveAction", bean);
/* 368 */     bean.setCode("StSave");
/* 369 */     bean.setModel(getMaterialStockAppModel());
/* 370 */     bean.setEditor(getMaterialStockEditor());
/* 371 */     bean.setValidationService(getMaterialStockValidationService());
/* 372 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 373 */     invokeInitializingBean(bean);
/* 374 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.bs.uif2.validation.DefaultValidationService getMaterialStockValidationService() {
/* 378 */     if (context.get("materialStockValidationService") != null)
/* 379 */       return (nc.bs.uif2.validation.DefaultValidationService)context.get("materialStockValidationService");
/* 380 */     nc.bs.uif2.validation.DefaultValidationService bean = new nc.bs.uif2.validation.DefaultValidationService();
/* 381 */     context.put("materialStockValidationService", bean);
/* 382 */     bean.setValidators(getManagedList8());
/* 383 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 384 */     invokeInitializingBean(bean);
/* 385 */     return bean;
/*     */   }
/*     */   
/* 388 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialStockSaveValidator_2ce49e());return list;
/*     */   }
/*     */   
/* 391 */   private nc.bs.bd.material.stock.validator.MaterialStockSaveValidator getMaterialStockSaveValidator_2ce49e() { if (context.get("nc.bs.bd.material.stock.validator.MaterialStockSaveValidator#2ce49e") != null)
/* 392 */       return (nc.bs.bd.material.stock.validator.MaterialStockSaveValidator)context.get("nc.bs.bd.material.stock.validator.MaterialStockSaveValidator#2ce49e");
/* 393 */     nc.bs.bd.material.stock.validator.MaterialStockSaveValidator bean = new nc.bs.bd.material.stock.validator.MaterialStockSaveValidator();
/* 394 */     context.put("nc.bs.bd.material.stock.validator.MaterialStockSaveValidator#2ce49e", bean);
/* 395 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 396 */     invokeInitializingBean(bean);
/* 397 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialStockCancelAction() {
/* 401 */     if (context.get("materialStockCancelAction") != null)
/* 402 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialStockCancelAction");
/* 403 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 404 */     context.put("materialStockCancelAction", bean);
/* 405 */     bean.setCode("StCancel");
/* 406 */     bean.setModel(getMaterialStockAppModel());
/* 407 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 408 */     invokeInitializingBean(bean);
/* 409 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialStockRefreshSingleAction() {
/* 413 */     if (context.get("materialStockRefreshSingleAction") != null)
/* 414 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialStockRefreshSingleAction");
/* 415 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 416 */     context.put("materialStockRefreshSingleAction", bean);
/* 417 */     bean.setCode("StRefresh");
/* 418 */     bean.setModel(getMaterialStockAppModel());
/* 419 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 420 */     invokeInitializingBean(bean);
/* 421 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialStockRefreshAction() {
/* 425 */     if (context.get("materialStockRefreshAction") != null)
/* 426 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialStockRefreshAction");
/* 427 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 428 */     context.put("materialStockRefreshAction", bean);
/* 429 */     bean.setCode("StRefresh");
/* 430 */     bean.setModel(getMaterialStockAppModel());
/* 431 */     bean.setManager(getMaterialStockModelDataManager());
/* 432 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 433 */     invokeInitializingBean(bean);
/* 434 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialStockListPrintActionGroup() {
/* 438 */     if (context.get("materialStockListPrintActionGroup") != null)
/* 439 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialStockListPrintActionGroup");
/* 440 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 441 */     context.put("materialStockListPrintActionGroup", bean);
/* 442 */     bean.setCode("StPrintMenu");
/* 443 */     bean.setActions(getManagedList9());
/* 444 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 445 */     invokeInitializingBean(bean);
/* 446 */     return bean;
/*     */   }
/*     */   
/* 449 */   private List getManagedList9() { List list = new java.util.ArrayList();list.add(getMaterialStockListTempletPrintAction());list.add(getMaterialStockListTempletPreviewAction());list.add(getMaterialStockListOutputAction());return list;
/*     */   }
/*     */   
/* 452 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialStockListTempletPreviewAction() { if (context.get("materialStockListTempletPreviewAction") != null)
/* 453 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialStockListTempletPreviewAction");
/* 454 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 455 */     context.put("materialStockListTempletPreviewAction", bean);
/* 456 */     bean.setCode("StTempPreview");
/* 457 */     bean.setModel(getMaterialStockAppModel());
/* 458 */     bean.setNodeKey("stocklist");
/* 459 */     bean.setPrintDlgParentConatiner(getMaterialStockListView());
/* 460 */     bean.setDatasource(getMaterialStockListardDataSource());
/* 461 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 462 */     invokeInitializingBean(bean);
/* 463 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialStockListTempletPrintAction() {
/* 467 */     if (context.get("materialStockListTempletPrintAction") != null)
/* 468 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialStockListTempletPrintAction");
/* 469 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 470 */     context.put("materialStockListTempletPrintAction", bean);
/* 471 */     bean.setCode("StTempPrint");
/* 472 */     bean.setModel(getMaterialStockAppModel());
/* 473 */     bean.setNodeKey("stocklist");
/* 474 */     bean.setPrintDlgParentConatiner(getMaterialStockListView());
/* 475 */     bean.setDatasource(getMaterialStockListardDataSource());
/* 476 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 477 */     invokeInitializingBean(bean);
/* 478 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialStockListOutputAction() {
/* 482 */     if (context.get("materialStockListOutputAction") != null)
/* 483 */       return (OutputAction)context.get("materialStockListOutputAction");
/* 484 */     OutputAction bean = new OutputAction();
/* 485 */     context.put("materialStockListOutputAction", bean);
/* 486 */     bean.setCode("StOutput");
/* 487 */     bean.setModel(getMaterialStockAppModel());
/* 488 */     bean.setNodeKey("stocklist");
/* 489 */     bean.setPrintDlgParentConatiner(getMaterialStockListView());
/* 490 */     bean.setDatasource(getMaterialStockListardDataSource());
/* 491 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 492 */     invokeInitializingBean(bean);
/* 493 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialStockListardDataSource() {
/* 497 */     if (context.get("materialStockListardDataSource") != null)
/* 498 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialStockListardDataSource");
/* 499 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 500 */     context.put("materialStockListardDataSource", bean);
/* 501 */     bean.setModel(getMaterialStockAppModel());
/* 502 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 503 */     invokeInitializingBean(bean);
/* 504 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialStockPrintActionGroup() {
/* 508 */     if (context.get("materialStockPrintActionGroup") != null)
/* 509 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialStockPrintActionGroup");
/* 510 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 511 */     context.put("materialStockPrintActionGroup", bean);
/* 512 */     bean.setCode("StPrintMenu");
/* 513 */     bean.setActions(getManagedList10());
/* 514 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 515 */     invokeInitializingBean(bean);
/* 516 */     return bean;
/*     */   }
/*     */   
/* 519 */   private List getManagedList10() { List list = new java.util.ArrayList();list.add(getMaterialStockTempletPrintAction());list.add(getMaterialStockTempletPreviewAction());list.add(getMaterialStockOutputAction());return list;
/*     */   }
/*     */   
/* 522 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialStockTempletPreviewAction() { if (context.get("materialStockTempletPreviewAction") != null)
/* 523 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialStockTempletPreviewAction");
/* 524 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 525 */     context.put("materialStockTempletPreviewAction", bean);
/* 526 */     bean.setCode("StTempPreview");
/* 527 */     bean.setModel(getMaterialStockAppModel());
/* 528 */     bean.setNodeKey("stockcard");
/* 529 */     bean.setPrintDlgParentConatiner(getMaterialStockEditor());
/* 530 */     bean.setDatasource(getMaterialStockCardDataSource());
/* 531 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 532 */     invokeInitializingBean(bean);
/* 533 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialStockTempletPrintAction() {
/* 537 */     if (context.get("materialStockTempletPrintAction") != null)
/* 538 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialStockTempletPrintAction");
/* 539 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 540 */     context.put("materialStockTempletPrintAction", bean);
/* 541 */     bean.setCode("StTempPrint");
/* 542 */     bean.setModel(getMaterialStockAppModel());
/* 543 */     bean.setNodeKey("stockcard");
/* 544 */     bean.setPrintDlgParentConatiner(getMaterialStockEditor());
/* 545 */     bean.setDatasource(getMaterialStockCardDataSource());
/* 546 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 547 */     invokeInitializingBean(bean);
/* 548 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialStockOutputAction() {
/* 552 */     if (context.get("materialStockOutputAction") != null)
/* 553 */       return (OutputAction)context.get("materialStockOutputAction");
/* 554 */     OutputAction bean = new OutputAction();
/* 555 */     context.put("materialStockOutputAction", bean);
/* 556 */     bean.setCode("StOutput");
/* 557 */     bean.setModel(getMaterialStockAppModel());
/* 558 */     bean.setNodeKey("stockcard");
/* 559 */     bean.setPrintDlgParentConatiner(getMaterialStockEditor());
/* 560 */     bean.setDatasource(getMaterialStockCardDataSource());
/* 561 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 562 */     invokeInitializingBean(bean);
/* 563 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialStockCardDataSource() {
/* 567 */     if (context.get("materialStockCardDataSource") != null)
/* 568 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialStockCardDataSource");
/* 569 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 570 */     context.put("materialStockCardDataSource", bean);
/* 571 */     bean.setModel(getMaterialStockAppModel());
/* 572 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 573 */     invokeInitializingBean(bean);
/* 574 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMateriaStockFirstLineAction() {
/* 578 */     if (context.get("materiaStockFirstLineAction") != null)
/* 579 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materiaStockFirstLineAction");
/* 580 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 581 */     context.put("materiaStockFirstLineAction", bean);
/* 582 */     bean.setModel(getMaterialStockAppModel());
/* 583 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 584 */     invokeInitializingBean(bean);
/* 585 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialStockNextLineAction() {
/* 589 */     if (context.get("materialStockNextLineAction") != null)
/* 590 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialStockNextLineAction");
/* 591 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 592 */     context.put("materialStockNextLineAction", bean);
/* 593 */     bean.setModel(getMaterialStockAppModel());
/* 594 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 595 */     invokeInitializingBean(bean);
/* 596 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialStockPreLineAction() {
/* 600 */     if (context.get("materialStockPreLineAction") != null)
/* 601 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialStockPreLineAction");
/* 602 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 603 */     context.put("materialStockPreLineAction", bean);
/* 604 */     bean.setModel(getMaterialStockAppModel());
/* 605 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 606 */     invokeInitializingBean(bean);
/* 607 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialStockLastLineAction() {
/* 611 */     if (context.get("materialStockLastLineAction") != null)
/* 612 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialStockLastLineAction");
/* 613 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 614 */     context.put("materialStockLastLineAction", bean);
/* 615 */     bean.setModel(getMaterialStockAppModel());
/* 616 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 617 */     invokeInitializingBean(bean);
/* 618 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialStockClosingListener() {
/* 622 */     if (context.get("materialStockClosingListener") != null)
/* 623 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialStockClosingListener");
/* 624 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 625 */     context.put("materialStockClosingListener", bean);
/* 626 */     bean.setModel(getMaterialStockAppModel());
/* 627 */     bean.setSaveaction(getMaterialStockSaveAction());
/* 628 */     bean.setCancelaction(getMaterialStockCancelAction());
/* 629 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 630 */     invokeInitializingBean(bean);
/* 631 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialStockAppModel());
/* 641 */     bean.setEditor(getMaterialStockEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialStockAppModel());
/* 641 */     bean.setEditor(getMaterialStockEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }