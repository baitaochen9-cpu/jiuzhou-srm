/*     */ package nc.ui.bd.material.plan;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.OutputAction;
/*     */ import nc.ui.uif2.editor.UserdefQueryParam;
/*     */ 
/*     */ public class material_plan_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_plan() { if (context.get("material_plan") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_plan");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_plan", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialPlanListView());
/*  19 */     bean.setClosingListener(getMaterialPlanClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialPlanEditAction());list.add(getMaterialPlanDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPlanRefreshAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPlanListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.plan.model.MaterialPlanAppModelService getMaterialPlanAppModelsService() { if (context.get("materialPlanAppModelsService") != null)
/*  29 */       return (nc.ui.bd.material.plan.model.MaterialPlanAppModelService)context.get("materialPlanAppModelsService");
/*  30 */     nc.ui.bd.material.plan.model.MaterialPlanAppModelService bean = new nc.ui.bd.material.plan.model.MaterialPlanAppModelService();
/*  31 */     context.put("materialPlanAppModelsService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialPlanAppModel() {
/*  38 */     if (context.get("materialPlanAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialPlanAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialPlanAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialPlanAppModelsService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_1a446de());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_1a446de() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1a446de") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1a446de");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1a446de", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.plan.model.MaterialPlanAppModelDataManager getMaterialPlanModelDataManager() {
/*  62 */     if (context.get("materialPlanModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.plan.model.MaterialPlanAppModelDataManager)context.get("materialPlanModelDataManager");
/*  64 */     nc.ui.bd.material.plan.model.MaterialPlanAppModelDataManager bean = new nc.ui.bd.material.plan.model.MaterialPlanAppModelDataManager();
/*  65 */     context.put("materialPlanModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialPlanAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialPlanMediator() {
/*  73 */     if (context.get("materialPlanMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialPlanMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialPlanMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialPlanModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialPlanAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialPlanListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.assistant.MarAsstPubShowUtil getMaterialPlanMarAsstMediator() {
/*  87 */     if (context.get("materialPlanMarAsstMediator") != null)
/*  88 */       return (nc.ui.bd.material.assistant.MarAsstPubShowUtil)context.get("materialPlanMarAsstMediator");
/*  89 */     nc.ui.bd.material.assistant.MarAsstPubShowUtil bean = new nc.ui.bd.material.assistant.MarAsstPubShowUtil();
/*  90 */     context.put("materialPlanMarAsstMediator", bean);
/*  91 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/*  92 */     bean.setListEditor(getMaterialPlanListView());
/*  93 */     bean.setCardEditor(getMaterialPlanEditor());
/*  94 */     bean.setAsstPrecode("marasst");
/*  95 */     bean.setPos("head");
/*  96 */     bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  97 */     bean.init();
/*  98 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  99 */     invokeInitializingBean(bean);
/* 100 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.plan.view.MaterialPlanListView getMaterialPlanListView() {
/* 104 */     if (context.get("materialPlanListView") != null)
/* 105 */       return (nc.ui.bd.material.plan.view.MaterialPlanListView)context.get("materialPlanListView");
/* 106 */     nc.ui.bd.material.plan.view.MaterialPlanListView bean = new nc.ui.bd.material.plan.view.MaterialPlanListView();
/* 107 */     context.put("materialPlanListView", bean);
/* 108 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 109 */     bean.setModel(getMaterialPlanAppModel());
/* 110 */     bean.setNodekey("plan");
/* 111 */     bean.setName(getI18nFB_28a5f8());
/* 112 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_1ef3ad2());
/* 113 */     bean.initUI();
/* 114 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 115 */     invokeInitializingBean(bean);
/* 116 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_28a5f8() {
/* 120 */     if (context.get("nc.ui.uif2.I18nFB#28a5f8") != null)
/* 121 */       return (String)context.get("nc.ui.uif2.I18nFB#28a5f8");
/* 122 */     I18nFB bean = new I18nFB();
/* 123 */     context.put("&nc.ui.uif2.I18nFB#28a5f8", bean);bean.setResDir("10140mag");
/* 124 */     bean.setDefaultValue("计划信息");
/* 125 */     bean.setResId("110140mag0013");
/* 126 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 127 */     invokeInitializingBean(bean);
/*     */     try {
/* 129 */       Object product = bean.getObject();
/* 130 */       context.put("nc.ui.uif2.I18nFB#28a5f8", product);
/* 131 */       return (String)product;
/*     */     } catch (Exception e) {
/* 133 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 136 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_1ef3ad2() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#1ef3ad2") != null)
/* 137 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#1ef3ad2");
/* 138 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 139 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#1ef3ad2", bean);
/* 140 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 141 */     bean.setParams(getManagedList1());
/* 142 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 143 */     invokeInitializingBean(bean);
/* 144 */     return bean;
/*     */   }
/*     */   
/* 147 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_a8764e());list.add(getListUserBodydefitemQueryParam());return list;
/*     */   }
/*     */   
/* 150 */   private UserdefQueryParam getUserdefQueryParam_a8764e() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#a8764e") != null)
/* 151 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#a8764e");
/* 152 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 153 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#a8764e", bean);
/* 154 */     bean.setMdfullname("uap.materialplan");
/* 155 */     bean.setPos(0);
/* 156 */     bean.setPrefix("def");
/* 157 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 158 */     invokeInitializingBean(bean);
/* 159 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getListUserBodydefitemQueryParam() {
/* 163 */     if (context.get("listUserBodydefitemQueryParam") != null)
/* 164 */       return (UserdefQueryParam)context.get("listUserBodydefitemQueryParam");
/* 165 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 166 */     context.put("listUserBodydefitemQueryParam", bean);
/* 167 */     bean.setMdfullname("uap.materialrepl");
/* 168 */     bean.setPos(1);
/* 169 */     bean.setPrefix("def");
/* 170 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 171 */     invokeInitializingBean(bean);
/* 172 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.plan.view.MaterialPlanEditor getMaterialPlanEditor() {
/* 176 */     if (context.get("materialPlanEditor") != null)
/* 177 */       return (nc.ui.bd.material.plan.view.MaterialPlanEditor)context.get("materialPlanEditor");
/* 178 */     nc.ui.bd.material.plan.view.MaterialPlanEditor bean = new nc.ui.bd.material.plan.view.MaterialPlanEditor();
/* 179 */     context.put("materialPlanEditor", bean);
/* 180 */     bean.setModel(getMaterialPlanAppModel());
/* 181 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 182 */     bean.setNodekey("plan");
/* 183 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_3bea08());
/* 184 */     bean.setBodyActionMap(getManagedMap0());
/* 185 */     bean.setActions(getManagedList4());
/* 186 */     bean.initUI();
/* 187 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 188 */     invokeInitializingBean(bean);
/* 189 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_3bea08() {
/* 193 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#3bea08") != null)
/* 194 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#3bea08");
/* 195 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 196 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#3bea08", bean);
/* 197 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 198 */     bean.setParams(getManagedList2());
/* 199 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 200 */     invokeInitializingBean(bean);
/* 201 */     return bean;
/*     */   }
/*     */   
/* 204 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_febd44());list.add(getCardBodyUserdefitemQueryParam());return list;
/*     */   }
/*     */   
/* 207 */   private UserdefQueryParam getUserdefQueryParam_febd44() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#febd44") != null)
/* 208 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#febd44");
/* 209 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 210 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#febd44", bean);
/* 211 */     bean.setMdfullname("uap.materialplan");
/* 212 */     bean.setPos(0);
/* 213 */     bean.setPrefix("def");
/* 214 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 215 */     invokeInitializingBean(bean);
/* 216 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getCardBodyUserdefitemQueryParam() {
/* 220 */     if (context.get("cardBodyUserdefitemQueryParam") != null)
/* 221 */       return (UserdefQueryParam)context.get("cardBodyUserdefitemQueryParam");
/* 222 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 223 */     context.put("cardBodyUserdefitemQueryParam", bean);
/* 224 */     bean.setMdfullname("uap.materialrepl");
/* 225 */     bean.setPos(1);
/* 226 */     bean.setPrefix("def");
/* 227 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 228 */     invokeInitializingBean(bean);
/* 229 */     return bean;
/*     */   }
/*     */   
/* 232 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("materialrepl", getManagedList3());return map; }
/*     */   
/* 234 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getMaterialStockAddLineAction());list.add(getMaterialStockDelLineAction());return list; }
/*     */   
/* 236 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getMaterialPlanFirstLineAction());list.add(getMaterialPlanPreLineAction());list.add(getMaterialPlanNextLineAction());list.add(getMaterialPlanLastLineAction());return list;
/*     */   }
/*     */   
/* 239 */   public nc.ui.uif2.actions.AddLineAction getMaterialStockAddLineAction() { if (context.get("materialStockAddLineAction") != null)
/* 240 */       return (nc.ui.uif2.actions.AddLineAction)context.get("materialStockAddLineAction");
/* 241 */     nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
/* 242 */     context.put("materialStockAddLineAction", bean);
/* 243 */     bean.setModel(getMaterialPlanAppModel());
/* 244 */     bean.setCardpanel(getMaterialPlanEditor());
/* 245 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 246 */     invokeInitializingBean(bean);
/* 247 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DelLineAction getMaterialStockDelLineAction() {
/* 251 */     if (context.get("materialStockDelLineAction") != null)
/* 252 */       return (nc.ui.uif2.actions.DelLineAction)context.get("materialStockDelLineAction");
/* 253 */     nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
/* 254 */     context.put("materialStockDelLineAction", bean);
/* 255 */     bean.setModel(getMaterialPlanAppModel());
/* 256 */     bean.setCardpanel(getMaterialPlanEditor());
/* 257 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 258 */     invokeInitializingBean(bean);
/* 259 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialPlanDialogMediator() {
/* 263 */     if (context.get("materialPlanDialogMediator") != null)
/* 264 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialPlanDialogMediator");
/* 265 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 266 */     context.put("materialPlanDialogMediator", bean);
/* 267 */     bean.setModel(getMaterialPlanAppModel());
/* 268 */     bean.setDialogContainer(getMaterialPlanDialogContainer());
/* 269 */     bean.setName(getI18nFB_181b877());
/* 270 */     bean.setClosingListener(getMaterialPlanClosingListener());
/* 271 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 272 */     invokeInitializingBean(bean);
/* 273 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_181b877() {
/* 277 */     if (context.get("nc.ui.uif2.I18nFB#181b877") != null)
/* 278 */       return (String)context.get("nc.ui.uif2.I18nFB#181b877");
/* 279 */     I18nFB bean = new I18nFB();
/* 280 */     context.put("&nc.ui.uif2.I18nFB#181b877", bean);bean.setResDir("10140mag");
/* 281 */     bean.setDefaultValue("计划信息");
/* 282 */     bean.setResId("110140mag0013");
/* 283 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 284 */     invokeInitializingBean(bean);
/*     */     try {
/* 286 */       Object product = bean.getObject();
/* 287 */       context.put("nc.ui.uif2.I18nFB#181b877", product);
/* 288 */       return (String)product;
/*     */     } catch (Exception e) {
/* 290 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 293 */   public nc.ui.uif2.TangramContainer getMaterialPlanDialogContainer() { if (context.get("materialPlanDialogContainer") != null)
/* 294 */       return (nc.ui.uif2.TangramContainer)context.get("materialPlanDialogContainer");
/* 295 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 296 */     context.put("materialPlanDialogContainer", bean);
/* 297 */     bean.setConstraints(getManagedList5());
/* 298 */     bean.setActions(getManagedList6());
/* 299 */     bean.setEditActions(getManagedList7());
/* 300 */     bean.setModel(getMaterialPlanAppModel());
/* 301 */     bean.initUI();
/* 302 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 303 */     invokeInitializingBean(bean);
/* 304 */     return bean;
/*     */   }
/*     */   
/* 307 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_17a5fc8());return list;
/*     */   }
/*     */   
/* 310 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_17a5fc8() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#17a5fc8") != null)
/* 311 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#17a5fc8");
/* 312 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 313 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#17a5fc8", bean);
/* 314 */     bean.setNewComponent(getMaterialPlanEditor());
/* 315 */     bean.setNewComponentName(getI18nFB_175bfaa());
/* 316 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 317 */     invokeInitializingBean(bean);
/* 318 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_175bfaa() {
/* 322 */     if (context.get("nc.ui.uif2.I18nFB#175bfaa") != null)
/* 323 */       return (String)context.get("nc.ui.uif2.I18nFB#175bfaa");
/* 324 */     I18nFB bean = new I18nFB();
/* 325 */     context.put("&nc.ui.uif2.I18nFB#175bfaa", bean);bean.setResDir("10140mag");
/* 326 */     bean.setDefaultValue("计划信息");
/* 327 */     bean.setResId("110140mag0013");
/* 328 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 329 */     invokeInitializingBean(bean);
/*     */     try {
/* 331 */       Object product = bean.getObject();
/* 332 */       context.put("nc.ui.uif2.I18nFB#175bfaa", product);
/* 333 */       return (String)product;
/*     */     } catch (Exception e) {
/* 335 */       throw new RuntimeException(e); } }
/*     */   
/* 337 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialPlanEditAction());list.add(getMaterialPlanDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPlanRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPlanPrintActionGroup());return list; }
/*     */   
/* 339 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialPlanSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPlanCancelAction());return list;
/*     */   }
/*     */   
/* 342 */   public nc.ui.uif2.actions.EditAction getMaterialPlanEditAction() { if (context.get("materialPlanEditAction") != null)
/* 343 */       return (nc.ui.uif2.actions.EditAction)context.get("materialPlanEditAction");
/* 344 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 345 */     context.put("materialPlanEditAction", bean);
/* 346 */     bean.setCode("PlEdit");
/* 347 */     bean.setModel(getMaterialPlanAppModel());
/* 348 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 349 */     invokeInitializingBean(bean);
/* 350 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialPlanDeleteAction() {
/* 354 */     if (context.get("materialPlanDeleteAction") != null)
/* 355 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialPlanDeleteAction");
/* 356 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 357 */     context.put("materialPlanDeleteAction", bean);
/* 358 */     bean.setCode("PlDelete");
/* 359 */     bean.setModel(getMaterialPlanAppModel());
/* 360 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 361 */     invokeInitializingBean(bean);
/* 362 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.SaveAction getMaterialPlanSaveAction() {
/* 366 */     if (context.get("materialPlanSaveAction") != null)
/* 367 */       return (nc.ui.uif2.actions.SaveAction)context.get("materialPlanSaveAction");
/* 368 */     nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
/* 369 */     context.put("materialPlanSaveAction", bean);
/* 370 */     bean.setCode("PlSave");
/* 371 */     bean.setModel(getMaterialPlanAppModel());
/* 372 */     bean.setEditor(getMaterialPlanEditor());
/* 373 */     bean.setValidationService(getMaterialPlanValidationService());
/* 374 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 375 */     invokeInitializingBean(bean);
/* 376 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.bs.uif2.validation.DefaultValidationService getMaterialPlanValidationService() {
/* 380 */     if (context.get("materialPlanValidationService") != null)
/* 381 */       return (nc.bs.uif2.validation.DefaultValidationService)context.get("materialPlanValidationService");
/* 382 */     nc.bs.uif2.validation.DefaultValidationService bean = new nc.bs.uif2.validation.DefaultValidationService();
/* 383 */     context.put("materialPlanValidationService", bean);
/* 384 */     bean.setValidators(getManagedList8());
/* 385 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 386 */     invokeInitializingBean(bean);
/* 387 */     return bean;
/*     */   }
/*     */   
/* 390 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialPlanSaveValidator_126650d());return list;
/*     */   }
/*     */   
/* 393 */   private nc.bs.bd.material.plan.validator.MaterialPlanSaveValidator getMaterialPlanSaveValidator_126650d() { if (context.get("nc.bs.bd.material.plan.validator.MaterialPlanSaveValidator#126650d") != null)
/* 394 */       return (nc.bs.bd.material.plan.validator.MaterialPlanSaveValidator)context.get("nc.bs.bd.material.plan.validator.MaterialPlanSaveValidator#126650d");
/* 395 */     nc.bs.bd.material.plan.validator.MaterialPlanSaveValidator bean = new nc.bs.bd.material.plan.validator.MaterialPlanSaveValidator();
/* 396 */     context.put("nc.bs.bd.material.plan.validator.MaterialPlanSaveValidator#126650d", bean);
/* 397 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 398 */     invokeInitializingBean(bean);
/* 399 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialPlanCancelAction() {
/* 403 */     if (context.get("materialPlanCancelAction") != null)
/* 404 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialPlanCancelAction");
/* 405 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 406 */     context.put("materialPlanCancelAction", bean);
/* 407 */     bean.setCode("PlCancel");
/* 408 */     bean.setModel(getMaterialPlanAppModel());
/* 409 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 410 */     invokeInitializingBean(bean);
/* 411 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialPlanRefreshSingleAction() {
/* 415 */     if (context.get("materialPlanRefreshSingleAction") != null)
/* 416 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialPlanRefreshSingleAction");
/* 417 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 418 */     context.put("materialPlanRefreshSingleAction", bean);
/* 419 */     bean.setCode("PlRefresh");
/* 420 */     bean.setModel(getMaterialPlanAppModel());
/* 421 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 422 */     invokeInitializingBean(bean);
/* 423 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialPlanRefreshAction() {
/* 427 */     if (context.get("materialPlanRefreshAction") != null)
/* 428 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialPlanRefreshAction");
/* 429 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 430 */     context.put("materialPlanRefreshAction", bean);
/* 431 */     bean.setCode("PlRefresh");
/* 432 */     bean.setModel(getMaterialPlanAppModel());
/* 433 */     bean.setManager(getMaterialPlanModelDataManager());
/* 434 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 435 */     invokeInitializingBean(bean);
/* 436 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialPlanListPrintActionGroup() {
/* 440 */     if (context.get("materialPlanListPrintActionGroup") != null)
/* 441 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialPlanListPrintActionGroup");
/* 442 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 443 */     context.put("materialPlanListPrintActionGroup", bean);
/* 444 */     bean.setCode("PlPrintMenu");
/* 445 */     bean.setActions(getManagedList9());
/* 446 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 447 */     invokeInitializingBean(bean);
/* 448 */     return bean;
/*     */   }
/*     */   
/* 451 */   private List getManagedList9() { List list = new java.util.ArrayList();list.add(getMaterialPlanListTempletPrintAction());list.add(getMaterialPlanListTempletPreviewAction());list.add(getMaterialPlanListOutputAction());return list;
/*     */   }
/*     */   
/* 454 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialPlanListTempletPreviewAction() { if (context.get("materialPlanListTempletPreviewAction") != null)
/* 455 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialPlanListTempletPreviewAction");
/* 456 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 457 */     context.put("materialPlanListTempletPreviewAction", bean);
/* 458 */     bean.setCode("PlTempPreview");
/* 459 */     bean.setModel(getMaterialPlanAppModel());
/* 460 */     bean.setNodeKey("planlist");
/* 461 */     bean.setPrintDlgParentConatiner(getMaterialPlanListView());
/* 462 */     bean.setDatasource(getMaterialPlanListardDataSource());
/* 463 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 464 */     invokeInitializingBean(bean);
/* 465 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialPlanListTempletPrintAction() {
/* 469 */     if (context.get("materialPlanListTempletPrintAction") != null)
/* 470 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialPlanListTempletPrintAction");
/* 471 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 472 */     context.put("materialPlanListTempletPrintAction", bean);
/* 473 */     bean.setCode("PlTempPrint");
/* 474 */     bean.setModel(getMaterialPlanAppModel());
/* 475 */     bean.setNodeKey("planlist");
/* 476 */     bean.setPrintDlgParentConatiner(getMaterialPlanListView());
/* 477 */     bean.setDatasource(getMaterialPlanListardDataSource());
/* 478 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 479 */     invokeInitializingBean(bean);
/* 480 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialPlanListOutputAction() {
/* 484 */     if (context.get("materialPlanListOutputAction") != null)
/* 485 */       return (OutputAction)context.get("materialPlanListOutputAction");
/* 486 */     OutputAction bean = new OutputAction();
/* 487 */     context.put("materialPlanListOutputAction", bean);
/* 488 */     bean.setCode("PlOutput");
/* 489 */     bean.setModel(getMaterialPlanAppModel());
/* 490 */     bean.setNodeKey("planlist");
/* 491 */     bean.setPrintDlgParentConatiner(getMaterialPlanListView());
/* 492 */     bean.setDatasource(getMaterialPlanListardDataSource());
/* 493 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 494 */     invokeInitializingBean(bean);
/* 495 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialPlanListardDataSource() {
/* 499 */     if (context.get("materialPlanListardDataSource") != null)
/* 500 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialPlanListardDataSource");
/* 501 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 502 */     context.put("materialPlanListardDataSource", bean);
/* 503 */     bean.setModel(getMaterialPlanAppModel());
/* 504 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 505 */     invokeInitializingBean(bean);
/* 506 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialPlanPrintActionGroup() {
/* 510 */     if (context.get("materialPlanPrintActionGroup") != null)
/* 511 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialPlanPrintActionGroup");
/* 512 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 513 */     context.put("materialPlanPrintActionGroup", bean);
/* 514 */     bean.setCode("PlPrintMenu");
/* 515 */     bean.setActions(getManagedList10());
/* 516 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 517 */     invokeInitializingBean(bean);
/* 518 */     return bean;
/*     */   }
/*     */   
/* 521 */   private List getManagedList10() { List list = new java.util.ArrayList();list.add(getMaterialPlanTempletPrintAction());list.add(getMaterialPlanTempletPreviewAction());list.add(getMaterialPlanOutputAction());return list;
/*     */   }
/*     */   
/* 524 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialPlanTempletPreviewAction() { if (context.get("materialPlanTempletPreviewAction") != null)
/* 525 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialPlanTempletPreviewAction");
/* 526 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 527 */     context.put("materialPlanTempletPreviewAction", bean);
/* 528 */     bean.setCode("PlTempPreview");
/* 529 */     bean.setModel(getMaterialPlanAppModel());
/* 530 */     bean.setNodeKey("plancard");
/* 531 */     bean.setPrintDlgParentConatiner(getMaterialPlanEditor());
/* 532 */     bean.setDatasource(getMaterialPlanCardDataSource());
/* 533 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 534 */     invokeInitializingBean(bean);
/* 535 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialPlanTempletPrintAction() {
/* 539 */     if (context.get("materialPlanTempletPrintAction") != null)
/* 540 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialPlanTempletPrintAction");
/* 541 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 542 */     context.put("materialPlanTempletPrintAction", bean);
/* 543 */     bean.setCode("PlTempPrint");
/* 544 */     bean.setModel(getMaterialPlanAppModel());
/* 545 */     bean.setNodeKey("plancard");
/* 546 */     bean.setPrintDlgParentConatiner(getMaterialPlanEditor());
/* 547 */     bean.setDatasource(getMaterialPlanCardDataSource());
/* 548 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 549 */     invokeInitializingBean(bean);
/* 550 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialPlanOutputAction() {
/* 554 */     if (context.get("materialPlanOutputAction") != null)
/* 555 */       return (OutputAction)context.get("materialPlanOutputAction");
/* 556 */     OutputAction bean = new OutputAction();
/* 557 */     context.put("materialPlanOutputAction", bean);
/* 558 */     bean.setCode("PlOutput");
/* 559 */     bean.setModel(getMaterialPlanAppModel());
/* 560 */     bean.setNodeKey("plancard");
/* 561 */     bean.setPrintDlgParentConatiner(getMaterialPlanEditor());
/* 562 */     bean.setDatasource(getMaterialPlanCardDataSource());
/* 563 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 564 */     invokeInitializingBean(bean);
/* 565 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialPlanCardDataSource() {
/* 569 */     if (context.get("materialPlanCardDataSource") != null)
/* 570 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialPlanCardDataSource");
/* 571 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 572 */     context.put("materialPlanCardDataSource", bean);
/* 573 */     bean.setModel(getMaterialPlanAppModel());
/* 574 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 575 */     invokeInitializingBean(bean);
/* 576 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMaterialPlanFirstLineAction() {
/* 580 */     if (context.get("materialPlanFirstLineAction") != null)
/* 581 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materialPlanFirstLineAction");
/* 582 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 583 */     context.put("materialPlanFirstLineAction", bean);
/* 584 */     bean.setModel(getMaterialPlanAppModel());
/* 585 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 586 */     invokeInitializingBean(bean);
/* 587 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialPlanNextLineAction() {
/* 591 */     if (context.get("materialPlanNextLineAction") != null)
/* 592 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialPlanNextLineAction");
/* 593 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 594 */     context.put("materialPlanNextLineAction", bean);
/* 595 */     bean.setModel(getMaterialPlanAppModel());
/* 596 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 597 */     invokeInitializingBean(bean);
/* 598 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialPlanPreLineAction() {
/* 602 */     if (context.get("materialPlanPreLineAction") != null)
/* 603 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialPlanPreLineAction");
/* 604 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 605 */     context.put("materialPlanPreLineAction", bean);
/* 606 */     bean.setModel(getMaterialPlanAppModel());
/* 607 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 608 */     invokeInitializingBean(bean);
/* 609 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialPlanLastLineAction() {
/* 613 */     if (context.get("materialPlanLastLineAction") != null)
/* 614 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialPlanLastLineAction");
/* 615 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 616 */     context.put("materialPlanLastLineAction", bean);
/* 617 */     bean.setModel(getMaterialPlanAppModel());
/* 618 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 619 */     invokeInitializingBean(bean);
/* 620 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialPlanClosingListener() {
/* 624 */     if (context.get("materialPlanClosingListener") != null)
/* 625 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialPlanClosingListener");
/* 626 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 627 */     context.put("materialPlanClosingListener", bean);
/* 628 */     bean.setModel(getMaterialPlanAppModel());
/* 629 */     bean.setSaveaction(getMaterialPlanSaveAction());
/* 630 */     bean.setCancelaction(getMaterialPlanCancelAction());
/* 631 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 632 */     invokeInitializingBean(bean);
/* 633 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialPlanAppModel());
/* 641 */     bean.setEditor(getMaterialPlanEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialPlanAppModel());
/* 641 */     bean.setEditor(getMaterialPlanEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }