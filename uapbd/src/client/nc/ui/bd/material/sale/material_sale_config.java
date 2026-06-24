/*     */ package nc.ui.bd.material.sale;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.OutputAction;
/*     */ import nc.ui.uif2.editor.UserdefQueryParam;
/*     */ 
/*     */ public class material_sale_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_sale() { if (context.get("material_sale") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_sale");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_sale", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialSaleListView());
/*  19 */     bean.setClosingListener(getMaterialSaleClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialSaleEditAction());list.add(getMaterialSaleDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialSaleRefreshAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialSaleListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.sale.model.MaterialSaleAppModelService getMaterialSaleAppModelsService() { if (context.get("materialSaleAppModelsService") != null)
/*  29 */       return (nc.ui.bd.material.sale.model.MaterialSaleAppModelService)context.get("materialSaleAppModelsService");
/*  30 */     nc.ui.bd.material.sale.model.MaterialSaleAppModelService bean = new nc.ui.bd.material.sale.model.MaterialSaleAppModelService();
/*  31 */     context.put("materialSaleAppModelsService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialSaleAppModel() {
/*  38 */     if (context.get("materialSaleAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialSaleAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialSaleAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialSaleAppModelsService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_11c02e7());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_11c02e7() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#11c02e7") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#11c02e7");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#11c02e7", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.sale.model.MaterialSaleAppModelDataManager getMaterialSaleModelDataManager() {
/*  62 */     if (context.get("materialSaleModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.sale.model.MaterialSaleAppModelDataManager)context.get("materialSaleModelDataManager");
/*  64 */     nc.ui.bd.material.sale.model.MaterialSaleAppModelDataManager bean = new nc.ui.bd.material.sale.model.MaterialSaleAppModelDataManager();
/*  65 */     context.put("materialSaleModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialSaleAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialSaleMediator() {
/*  73 */     if (context.get("materialSaleMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialSaleMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialSaleMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialSaleModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialSaleAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialSaleListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.sale.view.MaterialSaleListView getMaterialSaleListView() {
/*  87 */     if (context.get("materialSaleListView") != null)
/*  88 */       return (nc.ui.bd.material.sale.view.MaterialSaleListView)context.get("materialSaleListView");
/*  89 */     nc.ui.bd.material.sale.view.MaterialSaleListView bean = new nc.ui.bd.material.sale.view.MaterialSaleListView();
/*  90 */     context.put("materialSaleListView", bean);
/*  91 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/*  92 */     bean.setModel(getMaterialSaleAppModel());
/*  93 */     bean.setNodekey("sale");
/*  94 */     bean.setName(getI18nFB_2c5b4());
/*  95 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_149fac6());
/*  96 */     bean.initUI();
/*  97 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  98 */     invokeInitializingBean(bean);
/*  99 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_2c5b4() {
/* 103 */     if (context.get("nc.ui.uif2.I18nFB#2c5b4") != null)
/* 104 */       return (String)context.get("nc.ui.uif2.I18nFB#2c5b4");
/* 105 */     I18nFB bean = new I18nFB();
/* 106 */     context.put("&nc.ui.uif2.I18nFB#2c5b4", bean);bean.setResDir("10140mag");
/* 107 */     bean.setDefaultValue("销售信息");
/* 108 */     bean.setResId("110140mag0021");
/* 109 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 110 */     invokeInitializingBean(bean);
/*     */     try {
/* 112 */       Object product = bean.getObject();
/* 113 */       context.put("nc.ui.uif2.I18nFB#2c5b4", product);
/* 114 */       return (String)product;
/*     */     } catch (Exception e) {
/* 116 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 119 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_149fac6() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#149fac6") != null)
/* 120 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#149fac6");
/* 121 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 122 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#149fac6", bean);
/* 123 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 124 */     bean.setParams(getManagedList1());
/* 125 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 126 */     invokeInitializingBean(bean);
/* 127 */     return bean;
/*     */   }
/*     */   
/* 130 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_1e0be());list.add(getListUserBodydefitemQueryParam());return list;
/*     */   }
/*     */   
/* 133 */   private UserdefQueryParam getUserdefQueryParam_1e0be() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1e0be") != null)
/* 134 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1e0be");
/* 135 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 136 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1e0be", bean);
/* 137 */     bean.setMdfullname("uap.materialsale");
/* 138 */     bean.setPos(0);
/* 139 */     bean.setPrefix("def");
/* 140 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 141 */     invokeInitializingBean(bean);
/* 142 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getListUserBodydefitemQueryParam() {
/* 146 */     if (context.get("listUserBodydefitemQueryParam") != null)
/* 147 */       return (UserdefQueryParam)context.get("listUserBodydefitemQueryParam");
/* 148 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 149 */     context.put("listUserBodydefitemQueryParam", bean);
/* 150 */     bean.setMdfullname("uap.materialbindle");
/* 151 */     bean.setPos(1);
/* 152 */     bean.setPrefix("def");
/* 153 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 154 */     invokeInitializingBean(bean);
/* 155 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.sale.view.MaterialSaleEditor getMaterialSaleEditor() {
/* 159 */     if (context.get("materialSaleEditor") != null)
/* 160 */       return (nc.ui.bd.material.sale.view.MaterialSaleEditor)context.get("materialSaleEditor");
/* 161 */     nc.ui.bd.material.sale.view.MaterialSaleEditor bean = new nc.ui.bd.material.sale.view.MaterialSaleEditor();
/* 162 */     context.put("materialSaleEditor", bean);
/* 163 */     bean.setModel(getMaterialSaleAppModel());
/* 164 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 165 */     bean.setNodekey("sale");
/* 166 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_c66abb());
/* 167 */     bean.setBodyActionMap(getManagedMap0());
/* 168 */     bean.setActions(getManagedList4());
/* 169 */     bean.initUI();
/* 170 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 171 */     invokeInitializingBean(bean);
/* 172 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_c66abb() {
/* 176 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#c66abb") != null)
/* 177 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#c66abb");
/* 178 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 179 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#c66abb", bean);
/* 180 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 181 */     bean.setParams(getManagedList2());
/* 182 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 183 */     invokeInitializingBean(bean);
/* 184 */     return bean;
/*     */   }
/*     */   
/* 187 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_1eefb92());list.add(getCardUserBodydefitemQueryParam());return list;
/*     */   }
/*     */   
/* 190 */   private UserdefQueryParam getUserdefQueryParam_1eefb92() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1eefb92") != null)
/* 191 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1eefb92");
/* 192 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 193 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1eefb92", bean);
/* 194 */     bean.setMdfullname("uap.materialsale");
/* 195 */     bean.setPos(0);
/* 196 */     bean.setPrefix("def");
/* 197 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 198 */     invokeInitializingBean(bean);
/* 199 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getCardUserBodydefitemQueryParam() {
/* 203 */     if (context.get("cardUserBodydefitemQueryParam") != null)
/* 204 */       return (UserdefQueryParam)context.get("cardUserBodydefitemQueryParam");
/* 205 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 206 */     context.put("cardUserBodydefitemQueryParam", bean);
/* 207 */     bean.setMdfullname("uap.materialbindle");
/* 208 */     bean.setPos(1);
/* 209 */     bean.setPrefix("def");
/* 210 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 211 */     invokeInitializingBean(bean);
/* 212 */     return bean;
/*     */   }
/*     */   
/* 215 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("materialbindle", getManagedList3());return map; }
/*     */   
/* 217 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getAddLineAction_8929bf());list.add(getDelLineAction_113ce5c());return list;
/*     */   }
/*     */   
/* 220 */   private nc.ui.uif2.actions.AddLineAction getAddLineAction_8929bf() { if (context.get("nc.ui.uif2.actions.AddLineAction#8929bf") != null)
/* 221 */       return (nc.ui.uif2.actions.AddLineAction)context.get("nc.ui.uif2.actions.AddLineAction#8929bf");
/* 222 */     nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
/* 223 */     context.put("nc.ui.uif2.actions.AddLineAction#8929bf", bean);
/* 224 */     bean.setModel(getMaterialSaleAppModel());
/* 225 */     bean.setCardpanel(getMaterialSaleEditor());
/* 226 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 227 */     invokeInitializingBean(bean);
/* 228 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.actions.DelLineAction getDelLineAction_113ce5c() {
/* 232 */     if (context.get("nc.ui.uif2.actions.DelLineAction#113ce5c") != null)
/* 233 */       return (nc.ui.uif2.actions.DelLineAction)context.get("nc.ui.uif2.actions.DelLineAction#113ce5c");
/* 234 */     nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
/* 235 */     context.put("nc.ui.uif2.actions.DelLineAction#113ce5c", bean);
/* 236 */     bean.setModel(getMaterialSaleAppModel());
/* 237 */     bean.setCardpanel(getMaterialSaleEditor());
/* 238 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 239 */     invokeInitializingBean(bean);
/* 240 */     return bean;
/*     */   }
/*     */   
/* 243 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getMaterialSaleFirstLineAction());list.add(getMaterialSalePreLineAction());list.add(getMaterialSaleNextLineAction());list.add(getMaterialSaleLastLineAction());return list;
/*     */   }
/*     */   
/* 246 */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialSaleDialogMediator() { if (context.get("materialSaleDialogMediator") != null)
/* 247 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialSaleDialogMediator");
/* 248 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 249 */     context.put("materialSaleDialogMediator", bean);
/* 250 */     bean.setModel(getMaterialSaleAppModel());
/* 251 */     bean.setDialogContainer(getMaterialSaleinfoDialogContainer());
/* 252 */     bean.setName(getI18nFB_1513717());
/* 253 */     bean.setClosingListener(getMaterialSaleClosingListener());
/* 254 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 255 */     invokeInitializingBean(bean);
/* 256 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_1513717() {
/* 260 */     if (context.get("nc.ui.uif2.I18nFB#1513717") != null)
/* 261 */       return (String)context.get("nc.ui.uif2.I18nFB#1513717");
/* 262 */     I18nFB bean = new I18nFB();
/* 263 */     context.put("&nc.ui.uif2.I18nFB#1513717", bean);bean.setResDir("10140mag");
/* 264 */     bean.setDefaultValue("销售信息");
/* 265 */     bean.setResId("110140mag0021");
/* 266 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 267 */     invokeInitializingBean(bean);
/*     */     try {
/* 269 */       Object product = bean.getObject();
/* 270 */       context.put("nc.ui.uif2.I18nFB#1513717", product);
/* 271 */       return (String)product;
/*     */     } catch (Exception e) {
/* 273 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 276 */   public nc.ui.uif2.TangramContainer getMaterialSaleinfoDialogContainer() { if (context.get("materialSaleinfoDialogContainer") != null)
/* 277 */       return (nc.ui.uif2.TangramContainer)context.get("materialSaleinfoDialogContainer");
/* 278 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 279 */     context.put("materialSaleinfoDialogContainer", bean);
/* 280 */     bean.setConstraints(getManagedList5());
/* 281 */     bean.setActions(getManagedList6());
/* 282 */     bean.setEditActions(getManagedList7());
/* 283 */     bean.setModel(getMaterialSaleAppModel());
/* 284 */     bean.initUI();
/* 285 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 286 */     invokeInitializingBean(bean);
/* 287 */     return bean;
/*     */   }
/*     */   
/* 290 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_2d75df());return list;
/*     */   }
/*     */   
/* 293 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_2d75df() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#2d75df") != null)
/* 294 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#2d75df");
/* 295 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 296 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#2d75df", bean);
/* 297 */     bean.setNewComponent(getMaterialSaleEditor());
/* 298 */     bean.setNewComponentName(getI18nFB_f7e259());
/* 299 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 300 */     invokeInitializingBean(bean);
/* 301 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_f7e259() {
/* 305 */     if (context.get("nc.ui.uif2.I18nFB#f7e259") != null)
/* 306 */       return (String)context.get("nc.ui.uif2.I18nFB#f7e259");
/* 307 */     I18nFB bean = new I18nFB();
/* 308 */     context.put("&nc.ui.uif2.I18nFB#f7e259", bean);bean.setResDir("10140mag");
/* 309 */     bean.setDefaultValue("销售信息");
/* 310 */     bean.setResId("110140mag0021");
/* 311 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 312 */     invokeInitializingBean(bean);
/*     */     try {
/* 314 */       Object product = bean.getObject();
/* 315 */       context.put("nc.ui.uif2.I18nFB#f7e259", product);
/* 316 */       return (String)product;
/*     */     } catch (Exception e) {
/* 318 */       throw new RuntimeException(e); } }
/*     */   
/* 320 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialSaleEditAction());list.add(getMaterialSaleDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialSaleRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialSalePrintActionGroup());return list; }
/*     */   
/* 322 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialSaleSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialSaleCancelAction());return list;
/*     */   }
/*     */   
/* 325 */   public nc.ui.uif2.actions.EditAction getMaterialSaleEditAction() { if (context.get("materialSaleEditAction") != null)
/* 326 */       return (nc.ui.uif2.actions.EditAction)context.get("materialSaleEditAction");
/* 327 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 328 */     context.put("materialSaleEditAction", bean);
/* 329 */     bean.setCode("SaEdit");
/* 330 */     bean.setModel(getMaterialSaleAppModel());
/* 331 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 332 */     invokeInitializingBean(bean);
/* 333 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialSaleDeleteAction() {
/* 337 */     if (context.get("materialSaleDeleteAction") != null)
/* 338 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialSaleDeleteAction");
/* 339 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 340 */     context.put("materialSaleDeleteAction", bean);
/* 341 */     bean.setCode("SaDelete");
/* 342 */     bean.setModel(getMaterialSaleAppModel());
/* 343 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 344 */     invokeInitializingBean(bean);
/* 345 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.SaveAction getMaterialSaleSaveAction() {
/* 349 */     if (context.get("materialSaleSaveAction") != null)
/* 350 */       return (nc.ui.uif2.actions.SaveAction)context.get("materialSaleSaveAction");
/* 351 */     nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
/* 352 */     context.put("materialSaleSaveAction", bean);
/* 353 */     bean.setCode("SaSave");
/* 354 */     bean.setModel(getMaterialSaleAppModel());
/* 355 */     bean.setEditor(getMaterialSaleEditor());
/* 356 */     bean.setValidationService(getMaterialSaleValidationService());
/* 357 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 358 */     invokeInitializingBean(bean);
/* 359 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.bs.uif2.validation.DefaultValidationService getMaterialSaleValidationService() {
/* 363 */     if (context.get("materialSaleValidationService") != null)
/* 364 */       return (nc.bs.uif2.validation.DefaultValidationService)context.get("materialSaleValidationService");
/* 365 */     nc.bs.uif2.validation.DefaultValidationService bean = new nc.bs.uif2.validation.DefaultValidationService();
/* 366 */     context.put("materialSaleValidationService", bean);
/* 367 */     bean.setValidators(getManagedList8());
/* 368 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 369 */     invokeInitializingBean(bean);
/* 370 */     return bean;
/*     */   }
/*     */   
/* 373 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialSaleSaveValidatorForClient_9591ec());return list;
/*     */   }
/*     */   
/* 376 */   private nc.ui.bd.material.sale.validator.MaterialSaleSaveValidatorForClient getMaterialSaleSaveValidatorForClient_9591ec() { if (context.get("nc.ui.bd.material.sale.validator.MaterialSaleSaveValidatorForClient#9591ec") != null)
/* 377 */       return (nc.ui.bd.material.sale.validator.MaterialSaleSaveValidatorForClient)context.get("nc.ui.bd.material.sale.validator.MaterialSaleSaveValidatorForClient#9591ec");
/* 378 */     nc.ui.bd.material.sale.validator.MaterialSaleSaveValidatorForClient bean = new nc.ui.bd.material.sale.validator.MaterialSaleSaveValidatorForClient();
/* 379 */     context.put("nc.ui.bd.material.sale.validator.MaterialSaleSaveValidatorForClient#9591ec", bean);
/* 380 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 381 */     invokeInitializingBean(bean);
/* 382 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialSaleCancelAction() {
/* 386 */     if (context.get("materialSaleCancelAction") != null)
/* 387 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialSaleCancelAction");
/* 388 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 389 */     context.put("materialSaleCancelAction", bean);
/* 390 */     bean.setCode("SaCancel");
/* 391 */     bean.setModel(getMaterialSaleAppModel());
/* 392 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 393 */     invokeInitializingBean(bean);
/* 394 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialSaleRefreshSingleAction() {
/* 398 */     if (context.get("materialSaleRefreshSingleAction") != null)
/* 399 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialSaleRefreshSingleAction");
/* 400 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 401 */     context.put("materialSaleRefreshSingleAction", bean);
/* 402 */     bean.setCode("SaRefresh");
/* 403 */     bean.setModel(getMaterialSaleAppModel());
/* 404 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 405 */     invokeInitializingBean(bean);
/* 406 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialSaleRefreshAction() {
/* 410 */     if (context.get("materialSaleRefreshAction") != null)
/* 411 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialSaleRefreshAction");
/* 412 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 413 */     context.put("materialSaleRefreshAction", bean);
/* 414 */     bean.setCode("SaRefresh");
/* 415 */     bean.setModel(getMaterialSaleAppModel());
/* 416 */     bean.setManager(getMaterialSaleModelDataManager());
/* 417 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 418 */     invokeInitializingBean(bean);
/* 419 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialSaleListPrintActionGroup() {
/* 423 */     if (context.get("materialSaleListPrintActionGroup") != null)
/* 424 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialSaleListPrintActionGroup");
/* 425 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 426 */     context.put("materialSaleListPrintActionGroup", bean);
/* 427 */     bean.setCode("SaPrintMenu");
/* 428 */     bean.setActions(getManagedList9());
/* 429 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 430 */     invokeInitializingBean(bean);
/* 431 */     return bean;
/*     */   }
/*     */   
/* 434 */   private List getManagedList9() { List list = new java.util.ArrayList();list.add(getMaterialSaleListTempletPrintAction());list.add(getMaterialSaleListTempletPreviewAction());list.add(getMaterialSaleListOutputAction());return list;
/*     */   }
/*     */   
/* 437 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialSaleListTempletPreviewAction() { if (context.get("materialSaleListTempletPreviewAction") != null)
/* 438 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialSaleListTempletPreviewAction");
/* 439 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 440 */     context.put("materialSaleListTempletPreviewAction", bean);
/* 441 */     bean.setCode("SaTempPreview");
/* 442 */     bean.setModel(getMaterialSaleAppModel());
/* 443 */     bean.setNodeKey("salelist");
/* 444 */     bean.setPrintDlgParentConatiner(getMaterialSaleListView());
/* 445 */     bean.setDatasource(getMaterialSaleListardDataSource());
/* 446 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 447 */     invokeInitializingBean(bean);
/* 448 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialSaleListTempletPrintAction() {
/* 452 */     if (context.get("materialSaleListTempletPrintAction") != null)
/* 453 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialSaleListTempletPrintAction");
/* 454 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 455 */     context.put("materialSaleListTempletPrintAction", bean);
/* 456 */     bean.setCode("SaTempPrint");
/* 457 */     bean.setModel(getMaterialSaleAppModel());
/* 458 */     bean.setNodeKey("salelist");
/* 459 */     bean.setPrintDlgParentConatiner(getMaterialSaleListView());
/* 460 */     bean.setDatasource(getMaterialSaleListardDataSource());
/* 461 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 462 */     invokeInitializingBean(bean);
/* 463 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialSaleListOutputAction() {
/* 467 */     if (context.get("materialSaleListOutputAction") != null)
/* 468 */       return (OutputAction)context.get("materialSaleListOutputAction");
/* 469 */     OutputAction bean = new OutputAction();
/* 470 */     context.put("materialSaleListOutputAction", bean);
/* 471 */     bean.setCode("SaOutput");
/* 472 */     bean.setModel(getMaterialSaleAppModel());
/* 473 */     bean.setNodeKey("salelist");
/* 474 */     bean.setPrintDlgParentConatiner(getMaterialSaleListView());
/* 475 */     bean.setDatasource(getMaterialSaleListardDataSource());
/* 476 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 477 */     invokeInitializingBean(bean);
/* 478 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialSaleListardDataSource() {
/* 482 */     if (context.get("materialSaleListardDataSource") != null)
/* 483 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialSaleListardDataSource");
/* 484 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 485 */     context.put("materialSaleListardDataSource", bean);
/* 486 */     bean.setModel(getMaterialSaleAppModel());
/* 487 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 488 */     invokeInitializingBean(bean);
/* 489 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialSalePrintActionGroup() {
/* 493 */     if (context.get("materialSalePrintActionGroup") != null)
/* 494 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialSalePrintActionGroup");
/* 495 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 496 */     context.put("materialSalePrintActionGroup", bean);
/* 497 */     bean.setCode("SaPrintMenu");
/* 498 */     bean.setActions(getManagedList10());
/* 499 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 500 */     invokeInitializingBean(bean);
/* 501 */     return bean;
/*     */   }
/*     */   
/* 504 */   private List getManagedList10() { List list = new java.util.ArrayList();list.add(getMaterialSaleTempletPrintAction());list.add(getMaterialSaleTempletPreviewAction());list.add(getMaterialSaleOutputAction());return list;
/*     */   }
/*     */   
/* 507 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialSaleTempletPreviewAction() { if (context.get("materialSaleTempletPreviewAction") != null)
/* 508 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialSaleTempletPreviewAction");
/* 509 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 510 */     context.put("materialSaleTempletPreviewAction", bean);
/* 511 */     bean.setCode("SaTempPreview");
/* 512 */     bean.setModel(getMaterialSaleAppModel());
/* 513 */     bean.setNodeKey("salecard");
/* 514 */     bean.setPrintDlgParentConatiner(getMaterialSaleEditor());
/* 515 */     bean.setDatasource(getMaterialSaleCardDataSource());
/* 516 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 517 */     invokeInitializingBean(bean);
/* 518 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialSaleTempletPrintAction() {
/* 522 */     if (context.get("materialSaleTempletPrintAction") != null)
/* 523 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialSaleTempletPrintAction");
/* 524 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 525 */     context.put("materialSaleTempletPrintAction", bean);
/* 526 */     bean.setCode("SaTempPrint");
/* 527 */     bean.setModel(getMaterialSaleAppModel());
/* 528 */     bean.setNodeKey("salecard");
/* 529 */     bean.setPrintDlgParentConatiner(getMaterialSaleEditor());
/* 530 */     bean.setDatasource(getMaterialSaleCardDataSource());
/* 531 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 532 */     invokeInitializingBean(bean);
/* 533 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialSaleOutputAction() {
/* 537 */     if (context.get("materialSaleOutputAction") != null)
/* 538 */       return (OutputAction)context.get("materialSaleOutputAction");
/* 539 */     OutputAction bean = new OutputAction();
/* 540 */     context.put("materialSaleOutputAction", bean);
/* 541 */     bean.setCode("SaOutput");
/* 542 */     bean.setModel(getMaterialSaleAppModel());
/* 543 */     bean.setNodeKey("salecard");
/* 544 */     bean.setPrintDlgParentConatiner(getMaterialSaleEditor());
/* 545 */     bean.setDatasource(getMaterialSaleCardDataSource());
/* 546 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 547 */     invokeInitializingBean(bean);
/* 548 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialSaleCardDataSource() {
/* 552 */     if (context.get("materialSaleCardDataSource") != null)
/* 553 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialSaleCardDataSource");
/* 554 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 555 */     context.put("materialSaleCardDataSource", bean);
/* 556 */     bean.setModel(getMaterialSaleAppModel());
/* 557 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 558 */     invokeInitializingBean(bean);
/* 559 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMaterialSaleFirstLineAction() {
/* 563 */     if (context.get("materialSaleFirstLineAction") != null)
/* 564 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materialSaleFirstLineAction");
/* 565 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 566 */     context.put("materialSaleFirstLineAction", bean);
/* 567 */     bean.setModel(getMaterialSaleAppModel());
/* 568 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 569 */     invokeInitializingBean(bean);
/* 570 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialSaleNextLineAction() {
/* 574 */     if (context.get("materialSaleNextLineAction") != null)
/* 575 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialSaleNextLineAction");
/* 576 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 577 */     context.put("materialSaleNextLineAction", bean);
/* 578 */     bean.setModel(getMaterialSaleAppModel());
/* 579 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 580 */     invokeInitializingBean(bean);
/* 581 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialSalePreLineAction() {
/* 585 */     if (context.get("materialSalePreLineAction") != null)
/* 586 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialSalePreLineAction");
/* 587 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 588 */     context.put("materialSalePreLineAction", bean);
/* 589 */     bean.setModel(getMaterialSaleAppModel());
/* 590 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 591 */     invokeInitializingBean(bean);
/* 592 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialSaleLastLineAction() {
/* 596 */     if (context.get("materialSaleLastLineAction") != null)
/* 597 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialSaleLastLineAction");
/* 598 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 599 */     context.put("materialSaleLastLineAction", bean);
/* 600 */     bean.setModel(getMaterialSaleAppModel());
/* 601 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 602 */     invokeInitializingBean(bean);
/* 603 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialSaleClosingListener() {
/* 607 */     if (context.get("materialSaleClosingListener") != null)
/* 608 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialSaleClosingListener");
/* 609 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 610 */     context.put("materialSaleClosingListener", bean);
/* 611 */     bean.setModel(getMaterialSaleAppModel());
/* 612 */     bean.setSaveaction(getMaterialSaleSaveAction());
/* 613 */     bean.setCancelaction(getMaterialSaleCancelAction());
/* 614 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 615 */     invokeInitializingBean(bean);
/* 616 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialSaleAppModel());
/* 641 */     bean.setEditor(getMaterialSaleEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialSaleAppModel());
/* 641 */     bean.setEditor(getMaterialSaleEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }
