/*     */ package nc.ui.bd.material.pfc;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.OutputAction;
/*     */ import nc.ui.uif2.editor.UserdefQueryParam;
/*     */ 
/*     */ public class material_pfc_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_pfc() { if (context.get("material_pfc") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_pfc");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_pfc", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialPfcListView());
/*  19 */     bean.setClosingListener(getMaterialPfcClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialPfcEditAction());list.add(getMaterialPfcDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPfcRefreshAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPfcListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.pfc.model.MaterialPfcAppModelService getMaterialPfcAppModelsService() { if (context.get("materialPfcAppModelsService") != null)
/*  29 */       return (nc.ui.bd.material.pfc.model.MaterialPfcAppModelService)context.get("materialPfcAppModelsService");
/*  30 */     nc.ui.bd.material.pfc.model.MaterialPfcAppModelService bean = new nc.ui.bd.material.pfc.model.MaterialPfcAppModelService();
/*  31 */     context.put("materialPfcAppModelsService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialPfcAppModel() {
/*  38 */     if (context.get("materialPfcAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialPfcAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialPfcAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialPfcAppModelsService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_156e219());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_156e219() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#156e219") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#156e219");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#156e219", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pfc.model.MaterialPfcAppModelDataManager getMaterialPfcModelDataManager() {
/*  62 */     if (context.get("materialPfcModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.pfc.model.MaterialPfcAppModelDataManager)context.get("materialPfcModelDataManager");
/*  64 */     nc.ui.bd.material.pfc.model.MaterialPfcAppModelDataManager bean = new nc.ui.bd.material.pfc.model.MaterialPfcAppModelDataManager();
/*  65 */     context.put("materialPfcModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialPfcAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialPfcMediator() {
/*  73 */     if (context.get("materialPfcMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialPfcMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialPfcMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialPfcModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialPfcAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialPfcListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.assistant.MarAsstPubShowUtil getMaterialPfcMarAsstMediator() {
/*  87 */     if (context.get("materialPfcMarAsstMediator") != null)
/*  88 */       return (nc.ui.bd.material.assistant.MarAsstPubShowUtil)context.get("materialPfcMarAsstMediator");
/*  89 */     nc.ui.bd.material.assistant.MarAsstPubShowUtil bean = new nc.ui.bd.material.assistant.MarAsstPubShowUtil();
/*  90 */     context.put("materialPfcMarAsstMediator", bean);
/*  91 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/*  92 */     bean.setListEditor(getMaterialPfcListView());
/*  93 */     bean.setCardEditor(getMaterialPfcEditor());
/*  94 */     bean.setAsstPrecode("marasst");
/*  95 */     bean.setPos("body");
/*  96 */     bean.setModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  97 */     bean.init();
/*  98 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  99 */     invokeInitializingBean(bean);
/* 100 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pfc.view.MaterialPfcListView getMaterialPfcListView() {
/* 104 */     if (context.get("materialPfcListView") != null)
/* 105 */       return (nc.ui.bd.material.pfc.view.MaterialPfcListView)context.get("materialPfcListView");
/* 106 */     nc.ui.bd.material.pfc.view.MaterialPfcListView bean = new nc.ui.bd.material.pfc.view.MaterialPfcListView();
/* 107 */     context.put("materialPfcListView", bean);
/* 108 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 109 */     bean.setModel(getMaterialPfcAppModel());
/* 110 */     bean.setNodekey("materialpfc");
/* 111 */     bean.setName(getI18nFB_115f09d());
/* 112 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_1b5699b());
/* 113 */     bean.initUI();
/* 114 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 115 */     invokeInitializingBean(bean);
/* 116 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_115f09d() {
/* 120 */     if (context.get("nc.ui.uif2.I18nFB#115f09d") != null)
/* 121 */       return (String)context.get("nc.ui.uif2.I18nFB#115f09d");
/* 122 */     I18nFB bean = new I18nFB();
/* 123 */     context.put("&nc.ui.uif2.I18nFB#115f09d", bean);bean.setResDir("10140mag");
/* 124 */     bean.setDefaultValue("利润中心信息");
/* 125 */     bean.setResId("010140mag0350");
/* 126 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 127 */     invokeInitializingBean(bean);
/*     */     try {
/* 129 */       Object product = bean.getObject();
/* 130 */       context.put("nc.ui.uif2.I18nFB#115f09d", product);
/* 131 */       return (String)product;
/*     */     } catch (Exception e) {
/* 133 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 136 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_1b5699b() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#1b5699b") != null)
/* 137 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#1b5699b");
/* 138 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 139 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#1b5699b", bean);
/* 140 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 141 */     bean.setParams(getManagedList1());
/* 142 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 143 */     invokeInitializingBean(bean);
/* 144 */     return bean;
/*     */   }
/*     */   
/* 147 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_167f3c6());list.add(getListBodyUserdefItemQueryParam());return list;
/*     */   }
/*     */   
/* 150 */   private UserdefQueryParam getUserdefQueryParam_167f3c6() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#167f3c6") != null)
/* 151 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#167f3c6");
/* 152 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 153 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#167f3c6", bean);
/* 154 */     bean.setMdfullname("uapbd.materialpfc");
/* 155 */     bean.setPos(0);
/* 156 */     bean.setPrefix("def");
/* 157 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 158 */     invokeInitializingBean(bean);
/* 159 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getListBodyUserdefItemQueryParam() {
/* 163 */     if (context.get("listBodyUserdefItemQueryParam") != null)
/* 164 */       return (UserdefQueryParam)context.get("listBodyUserdefItemQueryParam");
/* 165 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 166 */     context.put("listBodyUserdefItemQueryParam", bean);
/* 167 */     bean.setMdfullname("uapbd.materialpfcsub");
/* 168 */     bean.setPos(1);
/* 169 */     bean.setPrefix("def");
/* 170 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 171 */     invokeInitializingBean(bean);
/* 172 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pfc.view.MaterialPfcEditor getMaterialPfcEditor() {
/* 176 */     if (context.get("materialPfcEditor") != null)
/* 177 */       return (nc.ui.bd.material.pfc.view.MaterialPfcEditor)context.get("materialPfcEditor");
/* 178 */     nc.ui.bd.material.pfc.view.MaterialPfcEditor bean = new nc.ui.bd.material.pfc.view.MaterialPfcEditor();
/* 179 */     context.put("materialPfcEditor", bean);
/* 180 */     bean.setModel(getMaterialPfcAppModel());
/* 181 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 182 */     bean.setNodekey("materialpfc");
/* 183 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_d101ce());
/* 184 */     bean.setBodyActionMap(getManagedMap0());
/* 185 */     bean.setActions(getManagedList4());
/* 186 */     bean.initUI();
/* 187 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 188 */     invokeInitializingBean(bean);
/* 189 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_d101ce() {
/* 193 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#d101ce") != null)
/* 194 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#d101ce");
/* 195 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 196 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#d101ce", bean);
/* 197 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 198 */     bean.setParams(getManagedList2());
/* 199 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 200 */     invokeInitializingBean(bean);
/* 201 */     return bean;
/*     */   }
/*     */   
/* 204 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_1286c19());list.add(getCardBodyUserdefItemQueryParam());return list;
/*     */   }
/*     */   
/* 207 */   private UserdefQueryParam getUserdefQueryParam_1286c19() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1286c19") != null)
/* 208 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1286c19");
/* 209 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 210 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1286c19", bean);
/* 211 */     bean.setMdfullname("uapbd.materialpfc");
/* 212 */     bean.setPos(0);
/* 213 */     bean.setPrefix("def");
/* 214 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 215 */     invokeInitializingBean(bean);
/* 216 */     return bean;
/*     */   }
/*     */   
/*     */   private UserdefQueryParam getCardBodyUserdefItemQueryParam() {
/* 220 */     if (context.get("cardBodyUserdefItemQueryParam") != null)
/* 221 */       return (UserdefQueryParam)context.get("cardBodyUserdefItemQueryParam");
/* 222 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 223 */     context.put("cardBodyUserdefItemQueryParam", bean);
/* 224 */     bean.setMdfullname("uapbd.materialpfcsub");
/* 225 */     bean.setPos(1);
/* 226 */     bean.setPrefix("def");
/* 227 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 228 */     invokeInitializingBean(bean);
/* 229 */     return bean;
/*     */   }
/*     */   
/* 232 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("materialpfcsub", getManagedList3());return map; }
/*     */   
/* 234 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getMaterialPfcsubAddLineAction());list.add(getMaterialPfcsubDelLineAction());return list; }
/*     */   
/* 236 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getMaterialPfcFirstLineAction());list.add(getMaterialPfcPreLineAction());list.add(getMaterialPfcNextLineAction());list.add(getMaterialPfcLastLineAction());return list;
/*     */   }
/*     */   
/* 239 */   public nc.ui.uif2.actions.AddLineAction getMaterialPfcsubAddLineAction() { if (context.get("materialPfcsubAddLineAction") != null)
/* 240 */       return (nc.ui.uif2.actions.AddLineAction)context.get("materialPfcsubAddLineAction");
/* 241 */     nc.ui.uif2.actions.AddLineAction bean = new nc.ui.uif2.actions.AddLineAction();
/* 242 */     context.put("materialPfcsubAddLineAction", bean);
/* 243 */     bean.setModel(getMaterialPfcAppModel());
/* 244 */     bean.setCardpanel(getMaterialPfcEditor());
/* 245 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 246 */     invokeInitializingBean(bean);
/* 247 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DelLineAction getMaterialPfcsubDelLineAction() {
/* 251 */     if (context.get("materialPfcsubDelLineAction") != null)
/* 252 */       return (nc.ui.uif2.actions.DelLineAction)context.get("materialPfcsubDelLineAction");
/* 253 */     nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
/* 254 */     context.put("materialPfcsubDelLineAction", bean);
/* 255 */     bean.setModel(getMaterialPfcAppModel());
/* 256 */     bean.setCardpanel(getMaterialPfcEditor());
/* 257 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 258 */     invokeInitializingBean(bean);
/* 259 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialPfcDialogMediator() {
/* 263 */     if (context.get("materialPfcDialogMediator") != null)
/* 264 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialPfcDialogMediator");
/* 265 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 266 */     context.put("materialPfcDialogMediator", bean);
/* 267 */     bean.setModel(getMaterialPfcAppModel());
/* 268 */     bean.setDialogContainer(getMaterialPfcDialogContainer());
/* 269 */     bean.setName(getI18nFB_1cd9365());
/* 270 */     bean.setClosingListener(getMaterialPfcClosingListener());
/* 271 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 272 */     invokeInitializingBean(bean);
/* 273 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_1cd9365() {
/* 277 */     if (context.get("nc.ui.uif2.I18nFB#1cd9365") != null)
/* 278 */       return (String)context.get("nc.ui.uif2.I18nFB#1cd9365");
/* 279 */     I18nFB bean = new I18nFB();
/* 280 */     context.put("&nc.ui.uif2.I18nFB#1cd9365", bean);bean.setResDir("10140mag");
/* 281 */     bean.setDefaultValue("利润中心信息");
/* 282 */     bean.setResId("010140mag0350");
/* 283 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 284 */     invokeInitializingBean(bean);
/*     */     try {
/* 286 */       Object product = bean.getObject();
/* 287 */       context.put("nc.ui.uif2.I18nFB#1cd9365", product);
/* 288 */       return (String)product;
/*     */     } catch (Exception e) {
/* 290 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 293 */   public nc.ui.uif2.TangramContainer getMaterialPfcDialogContainer() { if (context.get("materialPfcDialogContainer") != null)
/* 294 */       return (nc.ui.uif2.TangramContainer)context.get("materialPfcDialogContainer");
/* 295 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 296 */     context.put("materialPfcDialogContainer", bean);
/* 297 */     bean.setConstraints(getManagedList5());
/* 298 */     bean.setActions(getManagedList6());
/* 299 */     bean.setEditActions(getManagedList7());
/* 300 */     bean.setModel(getMaterialPfcAppModel());
/* 301 */     bean.initUI();
/* 302 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 303 */     invokeInitializingBean(bean);
/* 304 */     return bean;
/*     */   }
/*     */   
/* 307 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_2df01());return list;
/*     */   }
/*     */   
/* 310 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_2df01() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#2df01") != null)
/* 311 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#2df01");
/* 312 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 313 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#2df01", bean);
/* 314 */     bean.setNewComponent(getMaterialPfcEditor());
/* 315 */     bean.setNewComponentName(getI18nFB_187da25());
/* 316 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 317 */     invokeInitializingBean(bean);
/* 318 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_187da25() {
/* 322 */     if (context.get("nc.ui.uif2.I18nFB#187da25") != null)
/* 323 */       return (String)context.get("nc.ui.uif2.I18nFB#187da25");
/* 324 */     I18nFB bean = new I18nFB();
/* 325 */     context.put("&nc.ui.uif2.I18nFB#187da25", bean);bean.setResDir("10140mag");
/* 326 */     bean.setDefaultValue("利润中心信息");
/* 327 */     bean.setResId("010140mag0350");
/* 328 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 329 */     invokeInitializingBean(bean);
/*     */     try {
/* 331 */       Object product = bean.getObject();
/* 332 */       context.put("nc.ui.uif2.I18nFB#187da25", product);
/* 333 */       return (String)product;
/*     */     } catch (Exception e) {
/* 335 */       throw new RuntimeException(e); } }
/*     */   
/* 337 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialPfcEditAction());list.add(getMaterialPfcDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPfcRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPfcPrintActionGroup());return list; }
/*     */   
/* 339 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialPfcSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPfcCancelAction());return list;
/*     */   }
/*     */   
/* 342 */   public nc.ui.uif2.actions.EditAction getMaterialPfcEditAction() { if (context.get("materialPfcEditAction") != null)
/* 343 */       return (nc.ui.uif2.actions.EditAction)context.get("materialPfcEditAction");
/* 344 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 345 */     context.put("materialPfcEditAction", bean);
/* 346 */     bean.setCode("PfcEdit");
/* 347 */     bean.setModel(getMaterialPfcAppModel());
/* 348 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 349 */     invokeInitializingBean(bean);
/* 350 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialPfcDeleteAction() {
/* 354 */     if (context.get("materialPfcDeleteAction") != null)
/* 355 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialPfcDeleteAction");
/* 356 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 357 */     context.put("materialPfcDeleteAction", bean);
/* 358 */     bean.setCode("PfcDelete");
/* 359 */     bean.setModel(getMaterialPfcAppModel());
/* 360 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 361 */     invokeInitializingBean(bean);
/* 362 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pfc.action.MaterialPfcSaveAction getMaterialPfcSaveAction() {
/* 366 */     if (context.get("materialPfcSaveAction") != null)
/* 367 */       return (nc.ui.bd.material.pfc.action.MaterialPfcSaveAction)context.get("materialPfcSaveAction");
/* 368 */     nc.ui.bd.material.pfc.action.MaterialPfcSaveAction bean = new nc.ui.bd.material.pfc.action.MaterialPfcSaveAction();
/* 369 */     context.put("materialPfcSaveAction", bean);
/* 370 */     bean.setCode("PfcSave");
/* 371 */     bean.setModel(getMaterialPfcAppModel());
/* 372 */     bean.setEditor(getMaterialPfcEditor());
/* 373 */     bean.setPfcEditor(getMaterialPfcEditor());
/* 374 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 375 */     invokeInitializingBean(bean);
/* 376 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialPfcCancelAction() {
/* 380 */     if (context.get("materialPfcCancelAction") != null)
/* 381 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialPfcCancelAction");
/* 382 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 383 */     context.put("materialPfcCancelAction", bean);
/* 384 */     bean.setCode("PfcCancel");
/* 385 */     bean.setModel(getMaterialPfcAppModel());
/* 386 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 387 */     invokeInitializingBean(bean);
/* 388 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialPfcClosingListener() {
/* 392 */     if (context.get("materialPfcClosingListener") != null)
/* 393 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialPfcClosingListener");
/* 394 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 395 */     context.put("materialPfcClosingListener", bean);
/* 396 */     bean.setModel(getMaterialPfcAppModel());
/* 397 */     bean.setSaveaction(getMaterialPfcSaveAction());
/* 398 */     bean.setCancelaction(getMaterialPfcCancelAction());
/* 399 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 400 */     invokeInitializingBean(bean);
/* 401 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialPfcRefreshSingleAction() {
/* 405 */     if (context.get("materialPfcRefreshSingleAction") != null)
/* 406 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialPfcRefreshSingleAction");
/* 407 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 408 */     context.put("materialPfcRefreshSingleAction", bean);
/* 409 */     bean.setCode("PfcRefresh");
/* 410 */     bean.setModel(getMaterialPfcAppModel());
/* 411 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 412 */     invokeInitializingBean(bean);
/* 413 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialPfcRefreshAction() {
/* 417 */     if (context.get("materialPfcRefreshAction") != null)
/* 418 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialPfcRefreshAction");
/* 419 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 420 */     context.put("materialPfcRefreshAction", bean);
/* 421 */     bean.setCode("PfcRefresh");
/* 422 */     bean.setModel(getMaterialPfcAppModel());
/* 423 */     bean.setManager(getMaterialPfcModelDataManager());
/* 424 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 425 */     invokeInitializingBean(bean);
/* 426 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialPfcListPrintActionGroup() {
/* 430 */     if (context.get("materialPfcListPrintActionGroup") != null)
/* 431 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialPfcListPrintActionGroup");
/* 432 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 433 */     context.put("materialPfcListPrintActionGroup", bean);
/* 434 */     bean.setCode("PfcPrintMenu");
/* 435 */     bean.setActions(getManagedList8());
/* 436 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 437 */     invokeInitializingBean(bean);
/* 438 */     return bean;
/*     */   }
/*     */   
/* 441 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialPfcListTempletPrintAction());list.add(getMaterialPfcListTempletPreviewAction());list.add(getMaterialPfcListOutputAction());return list;
/*     */   }
/*     */   
/* 444 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialPfcListTempletPreviewAction() { if (context.get("materialPfcListTempletPreviewAction") != null)
/* 445 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialPfcListTempletPreviewAction");
/* 446 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 447 */     context.put("materialPfcListTempletPreviewAction", bean);
/* 448 */     bean.setCode("PfcTempPreview");
/* 449 */     bean.setModel(getMaterialPfcAppModel());
/* 450 */     bean.setNodeKey("pfclist");
/* 451 */     bean.setPrintDlgParentConatiner(getMaterialPfcListView());
/* 452 */     bean.setDatasource(getMaterialPfcListardDataSource());
/* 453 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 454 */     invokeInitializingBean(bean);
/* 455 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialPfcListTempletPrintAction() {
/* 459 */     if (context.get("materialPfcListTempletPrintAction") != null)
/* 460 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialPfcListTempletPrintAction");
/* 461 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 462 */     context.put("materialPfcListTempletPrintAction", bean);
/* 463 */     bean.setCode("PfcTempPrint");
/* 464 */     bean.setModel(getMaterialPfcAppModel());
/* 465 */     bean.setNodeKey("pfclist");
/* 466 */     bean.setPrintDlgParentConatiner(getMaterialPfcListView());
/* 467 */     bean.setDatasource(getMaterialPfcListardDataSource());
/* 468 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 469 */     invokeInitializingBean(bean);
/* 470 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialPfcListOutputAction() {
/* 474 */     if (context.get("materialPfcListOutputAction") != null)
/* 475 */       return (OutputAction)context.get("materialPfcListOutputAction");
/* 476 */     OutputAction bean = new OutputAction();
/* 477 */     context.put("materialPfcListOutputAction", bean);
/* 478 */     bean.setCode("PfcOutput");
/* 479 */     bean.setModel(getMaterialPfcAppModel());
/* 480 */     bean.setNodeKey("pfclist");
/* 481 */     bean.setPrintDlgParentConatiner(getMaterialPfcListView());
/* 482 */     bean.setDatasource(getMaterialPfcListardDataSource());
/* 483 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 484 */     invokeInitializingBean(bean);
/* 485 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialPfcListardDataSource() {
/* 489 */     if (context.get("materialPfcListardDataSource") != null)
/* 490 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialPfcListardDataSource");
/* 491 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 492 */     context.put("materialPfcListardDataSource", bean);
/* 493 */     bean.setModel(getMaterialPfcAppModel());
/* 494 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 495 */     invokeInitializingBean(bean);
/* 496 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialPfcPrintActionGroup() {
/* 500 */     if (context.get("materialPfcPrintActionGroup") != null)
/* 501 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialPfcPrintActionGroup");
/* 502 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 503 */     context.put("materialPfcPrintActionGroup", bean);
/* 504 */     bean.setCode("PfcPrintMenu");
/* 505 */     bean.setActions(getManagedList9());
/* 506 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 507 */     invokeInitializingBean(bean);
/* 508 */     return bean;
/*     */   }
/*     */   
/* 511 */   private List getManagedList9() { List list = new java.util.ArrayList();list.add(getMaterialPfcTempletPrintAction());list.add(getMaterialPfcTempletPreviewAction());list.add(getMaterialPfcOutputAction());return list;
/*     */   }
/*     */   
/* 514 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialPfcTempletPreviewAction() { if (context.get("materialPfcTempletPreviewAction") != null)
/* 515 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialPfcTempletPreviewAction");
/* 516 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 517 */     context.put("materialPfcTempletPreviewAction", bean);
/* 518 */     bean.setCode("PfcTempPreview");
/* 519 */     bean.setModel(getMaterialPfcAppModel());
/* 520 */     bean.setNodeKey("pfccard");
/* 521 */     bean.setPrintDlgParentConatiner(getMaterialPfcEditor());
/* 522 */     bean.setDatasource(getMaterialPfcCardDataSource());
/* 523 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 524 */     invokeInitializingBean(bean);
/* 525 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.TemplatePrintAction getMaterialPfcTempletPrintAction() {
/* 529 */     if (context.get("materialPfcTempletPrintAction") != null)
/* 530 */       return (nc.ui.uif2.actions.TemplatePrintAction)context.get("materialPfcTempletPrintAction");
/* 531 */     nc.ui.uif2.actions.TemplatePrintAction bean = new nc.ui.uif2.actions.TemplatePrintAction();
/* 532 */     context.put("materialPfcTempletPrintAction", bean);
/* 533 */     bean.setCode("PfcTempPrint");
/* 534 */     bean.setModel(getMaterialPfcAppModel());
/* 535 */     bean.setNodeKey("pfccard");
/* 536 */     bean.setPrintDlgParentConatiner(getMaterialPfcEditor());
/* 537 */     bean.setDatasource(getMaterialPfcCardDataSource());
/* 538 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 539 */     invokeInitializingBean(bean);
/* 540 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialPfcOutputAction() {
/* 544 */     if (context.get("materialPfcOutputAction") != null)
/* 545 */       return (OutputAction)context.get("materialPfcOutputAction");
/* 546 */     OutputAction bean = new OutputAction();
/* 547 */     context.put("materialPfcOutputAction", bean);
/* 548 */     bean.setCode("PfcOutput");
/* 549 */     bean.setModel(getMaterialPfcAppModel());
/* 550 */     bean.setNodeKey("pfccard");
/* 551 */     bean.setPrintDlgParentConatiner(getMaterialPfcEditor());
/* 552 */     bean.setDatasource(getMaterialPfcCardDataSource());
/* 553 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 554 */     invokeInitializingBean(bean);
/* 555 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialPfcCardDataSource() {
/* 559 */     if (context.get("materialPfcCardDataSource") != null)
/* 560 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialPfcCardDataSource");
/* 561 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 562 */     context.put("materialPfcCardDataSource", bean);
/* 563 */     bean.setModel(getMaterialPfcAppModel());
/* 564 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 565 */     invokeInitializingBean(bean);
/* 566 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMaterialPfcFirstLineAction() {
/* 570 */     if (context.get("materialPfcFirstLineAction") != null)
/* 571 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materialPfcFirstLineAction");
/* 572 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 573 */     context.put("materialPfcFirstLineAction", bean);
/* 574 */     bean.setModel(getMaterialPfcAppModel());
/* 575 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 576 */     invokeInitializingBean(bean);
/* 577 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialPfcNextLineAction() {
/* 581 */     if (context.get("materialPfcNextLineAction") != null)
/* 582 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialPfcNextLineAction");
/* 583 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 584 */     context.put("materialPfcNextLineAction", bean);
/* 585 */     bean.setModel(getMaterialPfcAppModel());
/* 586 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 587 */     invokeInitializingBean(bean);
/* 588 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialPfcPreLineAction() {
/* 592 */     if (context.get("materialPfcPreLineAction") != null)
/* 593 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialPfcPreLineAction");
/* 594 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 595 */     context.put("materialPfcPreLineAction", bean);
/* 596 */     bean.setModel(getMaterialPfcAppModel());
/* 597 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 598 */     invokeInitializingBean(bean);
/* 599 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialPfcLastLineAction() {
/* 603 */     if (context.get("materialPfcLastLineAction") != null)
/* 604 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialPfcLastLineAction");
/* 605 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 606 */     context.put("materialPfcLastLineAction", bean);
/* 607 */     bean.setModel(getMaterialPfcAppModel());
/* 608 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 609 */     invokeInitializingBean(bean);
/* 610 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialPfcAppModel());
/* 641 */     bean.setEditor(getMaterialPfcEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialPfcAppModel());
/* 641 */     bean.setEditor(getMaterialPfcEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }