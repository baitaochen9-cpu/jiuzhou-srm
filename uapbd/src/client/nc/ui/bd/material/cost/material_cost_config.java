/*     */ package nc.ui.bd.material.cost;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.OutputAction;
/*     */ import nc.ui.uif2.editor.UserdefQueryParam;
/*     */ 
/*     */ public class material_cost_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_cost() { if (context.get("material_cost") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_cost");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_cost", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialCostListView());
/*  19 */     bean.setClosingListener(getMaterialCostClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialCostEditAction());list.add(getMaterialCostDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCostRefreshAction());
list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCostListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.cost.model.MaterialCostAppModelService getMaterialCostAppModelService() { if (context.get("materialCostAppModelService") != null)
/*  29 */       return (nc.ui.bd.material.cost.model.MaterialCostAppModelService)context.get("materialCostAppModelService");
/*  30 */     nc.ui.bd.material.cost.model.MaterialCostAppModelService bean = new nc.ui.bd.material.cost.model.MaterialCostAppModelService();
/*  31 */     context.put("materialCostAppModelService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialCostAppModel() {
/*  38 */     if (context.get("materialCostAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialCostAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialCostAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialCostAppModelService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_f07fe());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_f07fe() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#f07fe") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#f07fe");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#f07fe", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.cost.model.MaterialCostAppModelDataManager getMaterialCostModelDataManager() {
/*  62 */     if (context.get("materialCostModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.cost.model.MaterialCostAppModelDataManager)context.get("materialCostModelDataManager");
/*  64 */     nc.ui.bd.material.cost.model.MaterialCostAppModelDataManager bean = new nc.ui.bd.material.cost.model.MaterialCostAppModelDataManager();
/*  65 */     context.put("materialCostModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialCostAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialCostMediator() {
/*  73 */     if (context.get("materialCostMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialCostMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialCostMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialCostModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialCostAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialCostListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.assistant.MarAsstPubShowUtil getMaterialCostMarAsstMediator() {
/*  87 */     if (context.get("materialCostMarAsstMediator") != null)
/*  88 */       return (nc.ui.bd.material.assistant.MarAsstPubShowUtil)context.get("materialCostMarAsstMediator");
/*  89 */     nc.ui.bd.material.assistant.MarAsstPubShowUtil bean = new nc.ui.bd.material.assistant.MarAsstPubShowUtil();
/*  90 */     context.put("materialCostMarAsstMediator", bean);
/*  91 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/*  92 */     bean.setListEditor(getMaterialCostListView());
/*  93 */     bean.setCardEditor(getMaterialCostEditor());
/*  94 */     bean.setAsstPrecode("marasst");
/*  95 */     bean.setPos("body");
/*  96 */     bean.setShowAcc(true);
/*  97 */     bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  98 */     bean.init();
/*  99 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 100 */     invokeInitializingBean(bean);
/* 101 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.cost.view.MaterialCostListView getMaterialCostListView() {
/* 105 */     if (context.get("materialCostListView") != null)
/* 106 */       return (nc.ui.bd.material.cost.view.MaterialCostListView)context.get("materialCostListView");
/* 107 */     nc.ui.bd.material.cost.view.MaterialCostListView bean = new nc.ui.bd.material.cost.view.MaterialCostListView();
/* 108 */     context.put("materialCostListView", bean);
/* 109 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 110 */     bean.setModel(getMaterialCostAppModel());
/* 111 */     bean.setNodekey("cost");
/* 112 */     bean.setName(getI18nFB_d5cc51());
/* 113 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_17bcbfc());
/* 114 */     bean.initUI();
/* 115 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 116 */     invokeInitializingBean(bean);
/* 117 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_d5cc51() {
/* 121 */     if (context.get("nc.ui.uif2.I18nFB#d5cc51") != null)
/* 122 */       return (String)context.get("nc.ui.uif2.I18nFB#d5cc51");
/* 123 */     I18nFB bean = new I18nFB();
/* 124 */     context.put("&nc.ui.uif2.I18nFB#d5cc51", bean);bean.setResDir("10140mag");
/* 125 */     bean.setDefaultValue("成本信息");
/* 126 */     bean.setResId("110140mag0025");
/* 127 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 128 */     invokeInitializingBean(bean);
/*     */     try {
/* 130 */       Object product = bean.getObject();
/* 131 */       context.put("nc.ui.uif2.I18nFB#d5cc51", product);
/* 132 */       return (String)product;
/*     */     } catch (Exception e) {
/* 134 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 137 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_17bcbfc() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#17bcbfc") != null)
/* 138 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#17bcbfc");
/* 139 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 140 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#17bcbfc", bean);
/* 141 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 142 */     bean.setParams(getManagedList1());
/* 143 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 144 */     invokeInitializingBean(bean);
/* 145 */     return bean;
/*     */   }
/*     */   
/* 148 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_590974());list.add(getListBodyUserdefitemQueryParam());return list;
/*     */   }
/*     */   
/* 151 */   private UserdefQueryParam getUserdefQueryParam_590974() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#590974") != null)
/* 152 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#590974");
/* 153 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 154 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#590974", bean);
/* 155 */     bean.setMdfullname("uap.materialcost");
/* 156 */     bean.setPos(0);
/* 157 */     bean.setPrefix("def");
/* 158 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 159 */     invokeInitializingBean(bean);
/* 160 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getListBodyUserdefitemQueryParam() {
/* 164 */     if (context.get("listBodyUserdefitemQueryParam") != null)
/* 165 */       return (UserdefQueryParam)context.get("listBodyUserdefitemQueryParam");
/* 166 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 167 */     context.put("listBodyUserdefitemQueryParam", bean);
/* 168 */     bean.setMdfullname("uap.materialcostmode");
/* 169 */     bean.setPos(1);
/* 170 */     bean.setPrefix("def");
/* 171 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 172 */     invokeInitializingBean(bean);
/* 173 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.cost.view.MaterialCostEditor getMaterialCostEditor() {
/* 177 */     if (context.get("materialCostEditor") != null)
/* 178 */       return (nc.ui.bd.material.cost.view.MaterialCostEditor)context.get("materialCostEditor");
/* 179 */     nc.ui.bd.material.cost.view.MaterialCostEditor bean = new nc.ui.bd.material.cost.view.MaterialCostEditor();
/* 180 */     context.put("materialCostEditor", bean);
/* 181 */     bean.setModel(getMaterialCostAppModel());
/* 182 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 183 */     bean.setNodekey("cost");
/* 184 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_808104());
/* 185 */     bean.setBodyActionMap(getManagedMap0());
/* 186 */     bean.setActions(getManagedList4());
/* 187 */     bean.initUI();
/* 188 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 189 */     invokeInitializingBean(bean);
/* 190 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_808104() {
/* 194 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#808104") != null)
/* 195 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#808104");
/* 196 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 197 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#808104", bean);
/* 198 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 199 */     bean.setParams(getManagedList2());
/* 200 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 201 */     invokeInitializingBean(bean);
/* 202 */     return bean;
/*     */   }
/*     */   
/* 205 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_8d2ced());list.add(getCardBodyUserdefitemQueryParam());return list;
/*     */   }
/*     */   
/* 208 */   private UserdefQueryParam getUserdefQueryParam_8d2ced() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#8d2ced") != null)
/* 209 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#8d2ced");
/* 210 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 211 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#8d2ced", bean);
/* 212 */     bean.setMdfullname("uap.materialcost");
/* 213 */     bean.setPos(0);
/* 214 */     bean.setPrefix("def");
/* 215 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 216 */     invokeInitializingBean(bean);
/* 217 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getCardBodyUserdefitemQueryParam() {
/* 221 */     if (context.get("cardBodyUserdefitemQueryParam") != null)
/* 222 */       return (UserdefQueryParam)context.get("cardBodyUserdefitemQueryParam");
/* 223 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 224 */     context.put("cardBodyUserdefitemQueryParam", bean);
/* 225 */     bean.setMdfullname("uap.materialcostmode");
/* 226 */     bean.setPos(1);
/* 227 */     bean.setPrefix("def");
/* 228 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 229 */     invokeInitializingBean(bean);
/* 230 */     return bean;
/*     */   }
/*     */   
/* 233 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("materialcostmode", getManagedList3());return map; }
/*     */   
/* 235 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getAddLineAction_1dafc49());list.add(getDelLineAction_1e94cc2());return list;
/*     */   }
/*     */   
/* 238 */   private nc.ui.uif2.actions.AddLineAction getAddLineAction_1dafc49() { if (context.get("nc.ui.uif2.actions.AddLineAction#1dafc49") != null)
/* 239 */       return (nc.ui.uif2.actions.AddLineAction)context.get("nc.ui.uif2.actions.AddLineAction#1dafc49");
/* 240 */     nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
/* 241 */     context.put("nc.ui.uif2.actions.AddLineAction#1dafc49", bean);
/* 242 */     bean.setModel(getMaterialCostAppModel());
/* 243 */     bean.setCardpanel(getMaterialCostEditor());
/* 244 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 245 */     invokeInitializingBean(bean);
/* 246 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.actions.DelLineAction getDelLineAction_1e94cc2() {
/* 250 */     if (context.get("nc.ui.uif2.actions.DelLineAction#1e94cc2") != null)
/* 251 */       return (nc.ui.uif2.actions.DelLineAction)context.get("nc.ui.uif2.actions.DelLineAction#1e94cc2");
/* 252 */     nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
/* 253 */     context.put("nc.ui.uif2.actions.DelLineAction#1e94cc2", bean);
/* 254 */     bean.setModel(getMaterialCostAppModel());
/* 255 */     bean.setCardpanel(getMaterialCostEditor());
/* 256 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 257 */     invokeInitializingBean(bean);
/* 258 */     return bean;
/*     */   }
/*     */   
/* 261 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getMaterialCostFirstLineAction());list.add(getMaterialCostPreLineAction());list.add(getMaterialCostNextLineAction());list.add(getMaterialCostLastLineAction());return list;
/*     */   }
/*     */   
/* 264 */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialCostDialogMediator() { if (context.get("materialCostDialogMediator") != null)
/* 265 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialCostDialogMediator");
/* 266 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 267 */     context.put("materialCostDialogMediator", bean);
/* 268 */     bean.setModel(getMaterialCostAppModel());
/* 269 */     bean.setDialogContainer(getMaterialCostDialogContainer());
/* 270 */     bean.setName(getI18nFB_1ae7f97());
/* 271 */     bean.setClosingListener(getMaterialCostClosingListener());
/* 272 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 273 */     invokeInitializingBean(bean);
/* 274 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_1ae7f97() {
/* 278 */     if (context.get("nc.ui.uif2.I18nFB#1ae7f97") != null)
/* 279 */       return (String)context.get("nc.ui.uif2.I18nFB#1ae7f97");
/* 280 */     I18nFB bean = new I18nFB();
/* 281 */     context.put("&nc.ui.uif2.I18nFB#1ae7f97", bean);bean.setResDir("10140mag");
/* 282 */     bean.setDefaultValue("成本信息");
/* 283 */     bean.setResId("110140mag0025");
/* 284 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 285 */     invokeInitializingBean(bean);
/*     */     try {
/* 287 */       Object product = bean.getObject();
/* 288 */       context.put("nc.ui.uif2.I18nFB#1ae7f97", product);
/* 289 */       return (String)product;
/*     */     } catch (Exception e) {
/* 291 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 294 */   public nc.ui.uif2.TangramContainer getMaterialCostDialogContainer() { if (context.get("materialCostDialogContainer") != null)
/* 295 */       return (nc.ui.uif2.TangramContainer)context.get("materialCostDialogContainer");
/* 296 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 297 */     context.put("materialCostDialogContainer", bean);
/* 298 */     bean.setConstraints(getManagedList5());
/* 299 */     bean.setActions(getManagedList6());
/* 300 */     bean.setEditActions(getManagedList7());
/* 301 */     bean.setModel(getMaterialCostAppModel());
/* 302 */     bean.initUI();
/* 303 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 304 */     invokeInitializingBean(bean);
/* 305 */     return bean;
/*     */   }
/*     */   
/* 308 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_69cadf());return list;
/*     */   }
/*     */   
/* 311 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_69cadf() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#69cadf") != null)
/* 312 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#69cadf");
/* 313 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 314 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#69cadf", bean);
/* 315 */     bean.setNewComponent(getMaterialCostEditor());
/* 316 */     bean.setNewComponentName(getI18nFB_13ad0ea());
/* 317 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 318 */     invokeInitializingBean(bean);
/* 319 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_13ad0ea() {
/* 323 */     if (context.get("nc.ui.uif2.I18nFB#13ad0ea") != null)
/* 324 */       return (String)context.get("nc.ui.uif2.I18nFB#13ad0ea");
/* 325 */     I18nFB bean = new I18nFB();
/* 326 */     context.put("&nc.ui.uif2.I18nFB#13ad0ea", bean);bean.setResDir("10140mag");
/* 327 */     bean.setDefaultValue("成本信息");
/* 328 */     bean.setResId("110140mag0025");
/* 329 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 330 */     invokeInitializingBean(bean);
/*     */     try {
/* 332 */       Object product = bean.getObject();
/* 333 */       context.put("nc.ui.uif2.I18nFB#13ad0ea", product);
/* 334 */       return (String)product;
/*     */     } catch (Exception e) {
/* 336 */       throw new RuntimeException(e); } }
/*     */   
/* 338 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialCostEditAction());list.add(getMaterialCostDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCostRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCostPrintActionGroup());return list; }
/*     */   
/* 340 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialCostSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCostCancelAction());return list;
/*     */   }
/*     */   
/* 343 */   public nc.ui.uif2.actions.EditAction getMaterialCostEditAction() { if (context.get("materialCostEditAction") != null)
/* 344 */       return (nc.ui.uif2.actions.EditAction)context.get("materialCostEditAction");
/* 345 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 346 */     context.put("materialCostEditAction", bean);
/* 347 */     bean.setCode("CoEdit");
/* 348 */     bean.setModel(getMaterialCostAppModel());
/* 349 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 350 */     invokeInitializingBean(bean);
/* 351 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialCostDeleteAction() {
/* 355 */     if (context.get("materialCostDeleteAction") != null)
/* 356 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialCostDeleteAction");
/* 357 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 358 */     context.put("materialCostDeleteAction", bean);
/* 359 */     bean.setCode("CoDelete");
/* 360 */     bean.setModel(getMaterialCostAppModel());
/* 361 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 362 */     invokeInitializingBean(bean);
/* 363 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.SaveAction getMaterialCostSaveAction() {
/* 367 */     if (context.get("materialCostSaveAction") != null)
/* 368 */       return (nc.ui.uif2.actions.SaveAction)context.get("materialCostSaveAction");
/* 369 */     nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
/* 370 */     context.put("materialCostSaveAction", bean);
/* 371 */     bean.setCode("CoSave");
/* 372 */     bean.setModel(getMaterialCostAppModel());
/* 373 */     bean.setEditor(getMaterialCostEditor());
/* 374 */     bean.setValidationService(getMaterialCostValidationService());
/* 375 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 376 */     invokeInitializingBean(bean);
/* 377 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.bs.uif2.validation.DefaultValidationService getMaterialCostValidationService() {
/* 381 */     if (context.get("materialCostValidationService") != null)
/* 382 */       return (nc.bs.uif2.validation.DefaultValidationService)context.get("materialCostValidationService");
/* 383 */     nc.bs.uif2.validation.DefaultValidationService bean = new nc.bs.uif2.validation.DefaultValidationService();
/* 384 */     context.put("materialCostValidationService", bean);
/* 385 */     bean.setValidators(getManagedList8());
/* 386 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 387 */     invokeInitializingBean(bean);
/* 388 */     return bean;
/*     */   }
/*     */   
/* 391 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialCostSaveValidator_186167c());list.add(getCostModeUniqueValidator_2e30e7());return list;
/*     */   }
/*     */   
/* 394 */   private nc.bs.bd.material.cost.validator.MaterialCostSaveValidator getMaterialCostSaveValidator_186167c() { if (context.get("nc.bs.bd.material.cost.validator.MaterialCostSaveValidator#186167c") != null)
/* 395 */       return (nc.bs.bd.material.cost.validator.MaterialCostSaveValidator)context.get("nc.bs.bd.material.cost.validator.MaterialCostSaveValidator#186167c");
/* 396 */     nc.bs.bd.material.cost.validator.MaterialCostSaveValidator bean = new nc.bs.bd.material.cost.validator.MaterialCostSaveValidator();
/* 397 */     context.put("nc.bs.bd.material.cost.validator.MaterialCostSaveValidator#186167c", bean);
/* 398 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 399 */     invokeInitializingBean(bean);
/* 400 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.bd.material.cost.validator.CostModeUniqueValidator getCostModeUniqueValidator_2e30e7() {
/* 404 */     if (context.get("nc.ui.bd.material.cost.validator.CostModeUniqueValidator#2e30e7") != null)
/* 405 */       return (nc.ui.bd.material.cost.validator.CostModeUniqueValidator)context.get("nc.ui.bd.material.cost.validator.CostModeUniqueValidator#2e30e7");
/* 406 */     nc.ui.bd.material.cost.validator.CostModeUniqueValidator bean = new nc.ui.bd.material.cost.validator.CostModeUniqueValidator();
/* 407 */     context.put("nc.ui.bd.material.cost.validator.CostModeUniqueValidator#2e30e7", bean);
/* 408 */     bean.setModel(getMaterialCostAppModel());
/* 409 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 410 */     invokeInitializingBean(bean);
/* 411 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialCostCancelAction() {
/* 415 */     if (context.get("materialCostCancelAction") != null)
/* 416 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialCostCancelAction");
/* 417 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 418 */     context.put("materialCostCancelAction", bean);
/* 419 */     bean.setCode("CoCancel");
/* 420 */     bean.setModel(getMaterialCostAppModel());
/* 421 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 422 */     invokeInitializingBean(bean);
/* 423 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialCostRefreshSingleAction() {
/* 427 */     if (context.get("materialCostRefreshSingleAction") != null)
/* 428 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialCostRefreshSingleAction");
/* 429 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 430 */     context.put("materialCostRefreshSingleAction", bean);
/* 431 */     bean.setCode("CoRefresh");
/* 432 */     bean.setModel(getMaterialCostAppModel());
/* 433 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 434 */     invokeInitializingBean(bean);
/* 435 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialCostRefreshAction() {
/* 439 */     if (context.get("materialCostRefreshAction") != null)
/* 440 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialCostRefreshAction");
/* 441 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 442 */     context.put("materialCostRefreshAction", bean);
/* 443 */     bean.setCode("CoRefresh");
/* 444 */     bean.setModel(getMaterialCostAppModel());
/* 445 */     bean.setManager(getMaterialCostModelDataManager());
/* 446 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 447 */     invokeInitializingBean(bean);
/* 448 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialCostListPrintActionGroup() {
/* 452 */     if (context.get("materialCostListPrintActionGroup") != null)
/* 453 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialCostListPrintActionGroup");
/* 454 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 455 */     context.put("materialCostListPrintActionGroup", bean);
/* 456 */     bean.setCode("CoPrintMenu");
/* 457 */     bean.setActions(getManagedList9());
/* 458 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 459 */     invokeInitializingBean(bean);
/* 460 */     return bean;
/*     */   }
/*     */   
/* 463 */   private List getManagedList9() { List list = new java.util.ArrayList();list.add(getMaterialCostListTempletPrintAction());list.add(getMaterialCostListTempletPreviewAction());list.add(getMaterialCostListOutputAction());return list;
/*     */   }
/*     */   
/* 466 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialCostListTempletPreviewAction() { if (context.get("materialCostListTempletPreviewAction") != null)
/* 467 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialCostListTempletPreviewAction");
/* 468 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 469 */     context.put("materialCostListTempletPreviewAction", bean);
/* 470 */     bean.setCode("CoTempPreview");
/* 471 */     bean.setModel(getMaterialCostAppModel());
/* 472 */     bean.setNodeKey("costlist");
/* 473 */     bean.setPrintDlgParentConatiner(getMaterialCostListView());
/* 474 */     bean.setDatasource(getMaterialCostListardDataSource());
/* 475 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 476 */     invokeInitializingBean(bean);
/* 477 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialCostListTempletPrintAction() {
/* 481 */     if (context.get("materialCostListTempletPrintAction") != null)
/* 482 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialCostListTempletPrintAction");
/* 483 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 484 */     context.put("materialCostListTempletPrintAction", bean);
/* 485 */     bean.setCode("CoTempPrint");
/* 486 */     bean.setModel(getMaterialCostAppModel());
/* 487 */     bean.setNodeKey("costlist");
/* 488 */     bean.setPrintDlgParentConatiner(getMaterialCostListView());
/* 489 */     bean.setDatasource(getMaterialCostListardDataSource());
/* 490 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 491 */     invokeInitializingBean(bean);
/* 492 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialCostListOutputAction() {
/* 496 */     if (context.get("materialCostListOutputAction") != null)
/* 497 */       return (OutputAction)context.get("materialCostListOutputAction");
/* 498 */     OutputAction bean = new OutputAction();
/* 499 */     context.put("materialCostListOutputAction", bean);
/* 500 */     bean.setCode("CoOutput");
/* 501 */     bean.setModel(getMaterialCostAppModel());
/* 502 */     bean.setNodeKey("costlist");
/* 503 */     bean.setPrintDlgParentConatiner(getMaterialCostListView());
/* 504 */     bean.setDatasource(getMaterialCostListardDataSource());
/* 505 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 506 */     invokeInitializingBean(bean);
/* 507 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialCostListardDataSource() {
/* 511 */     if (context.get("materialCostListardDataSource") != null)
/* 512 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialCostListardDataSource");
/* 513 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 514 */     context.put("materialCostListardDataSource", bean);
/* 515 */     bean.setModel(getMaterialCostAppModel());
/* 516 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 517 */     invokeInitializingBean(bean);
/* 518 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialCostPrintActionGroup() {
/* 522 */     if (context.get("materialCostPrintActionGroup") != null)
/* 523 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialCostPrintActionGroup");
/* 524 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 525 */     context.put("materialCostPrintActionGroup", bean);
/* 526 */     bean.setCode("CoPrintMenu");
/* 527 */     bean.setActions(getManagedList10());
/* 528 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 529 */     invokeInitializingBean(bean);
/* 530 */     return bean;
/*     */   }
/*     */   
/* 533 */   private List getManagedList10() { List list = new java.util.ArrayList();list.add(getMaterialCostTempletPrintAction());list.add(getMaterialCostTempletPreviewAction());list.add(getMaterialCostOutputAction());return list;
/*     */   }
/*     */   
/* 536 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialCostTempletPreviewAction() { if (context.get("materialCostTempletPreviewAction") != null)
/* 537 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialCostTempletPreviewAction");
/* 538 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 539 */     context.put("materialCostTempletPreviewAction", bean);
/* 540 */     bean.setCode("CoTempPreview");
/* 541 */     bean.setModel(getMaterialCostAppModel());
/* 542 */     bean.setNodeKey("costcard");
/* 543 */     bean.setPrintDlgParentConatiner(getMaterialCostEditor());
/* 544 */     bean.setDatasource(getMaterialCostCardDataSource());
/* 545 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 546 */     invokeInitializingBean(bean);
/* 547 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialCostTempletPrintAction() {
/* 551 */     if (context.get("materialCostTempletPrintAction") != null)
/* 552 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialCostTempletPrintAction");
/* 553 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 554 */     context.put("materialCostTempletPrintAction", bean);
/* 555 */     bean.setCode("CoTempPrint");
/* 556 */     bean.setModel(getMaterialCostAppModel());
/* 557 */     bean.setNodeKey("costcard");
/* 558 */     bean.setPrintDlgParentConatiner(getMaterialCostEditor());
/* 559 */     bean.setDatasource(getMaterialCostCardDataSource());
/* 560 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 561 */     invokeInitializingBean(bean);
/* 562 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialCostOutputAction() {
/* 566 */     if (context.get("materialCostOutputAction") != null)
/* 567 */       return (OutputAction)context.get("materialCostOutputAction");
/* 568 */     OutputAction bean = new OutputAction();
/* 569 */     context.put("materialCostOutputAction", bean);
/* 570 */     bean.setCode("CoOutput");
/* 571 */     bean.setModel(getMaterialCostAppModel());
/* 572 */     bean.setNodeKey("costcard");
/* 573 */     bean.setPrintDlgParentConatiner(getMaterialCostEditor());
/* 574 */     bean.setDatasource(getMaterialCostCardDataSource());
/* 575 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 576 */     invokeInitializingBean(bean);
/* 577 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialCostCardDataSource() {
/* 581 */     if (context.get("materialCostCardDataSource") != null)
/* 582 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialCostCardDataSource");
/* 583 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 584 */     context.put("materialCostCardDataSource", bean);
/* 585 */     bean.setModel(getMaterialCostAppModel());
/* 586 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 587 */     invokeInitializingBean(bean);
/* 588 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMaterialCostFirstLineAction() {
/* 592 */     if (context.get("materialCostFirstLineAction") != null)
/* 593 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materialCostFirstLineAction");
/* 594 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 595 */     context.put("materialCostFirstLineAction", bean);
/* 596 */     bean.setModel(getMaterialCostAppModel());
/* 597 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 598 */     invokeInitializingBean(bean);
/* 599 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialCostNextLineAction() {
/* 603 */     if (context.get("materialCostNextLineAction") != null)
/* 604 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialCostNextLineAction");
/* 605 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 606 */     context.put("materialCostNextLineAction", bean);
/* 607 */     bean.setModel(getMaterialCostAppModel());
/* 608 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 609 */     invokeInitializingBean(bean);
/* 610 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialCostPreLineAction() {
/* 614 */     if (context.get("materialCostPreLineAction") != null)
/* 615 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialCostPreLineAction");
/* 616 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 617 */     context.put("materialCostPreLineAction", bean);
/* 618 */     bean.setModel(getMaterialCostAppModel());
/* 619 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 620 */     invokeInitializingBean(bean);
/* 621 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialCostLastLineAction() {
/* 625 */     if (context.get("materialCostLastLineAction") != null)
/* 626 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialCostLastLineAction");
/* 627 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 628 */     context.put("materialCostLastLineAction", bean);
/* 629 */     bean.setModel(getMaterialCostAppModel());
/* 630 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 631 */     invokeInitializingBean(bean);
/* 632 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialCostClosingListener() {
/* 636 */     if (context.get("materialCostClosingListener") != null)
/* 637 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialCostClosingListener");
/* 638 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 639 */     context.put("materialCostClosingListener", bean);
/* 640 */     bean.setModel(getMaterialCostAppModel());
/* 641 */     bean.setSaveaction(getMaterialCostSaveAction());
/* 642 */     bean.setCancelaction(getMaterialCostCancelAction());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }


/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialCostAppModel());
/* 641 */     bean.setEditor(getMaterialCostEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialCostAppModel());
/* 641 */     bean.setEditor(getMaterialCostEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }
