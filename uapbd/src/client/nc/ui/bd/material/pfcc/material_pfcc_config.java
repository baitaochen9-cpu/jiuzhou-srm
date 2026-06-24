/*     */ package nc.ui.bd.material.pfcc;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.OutputAction;
/*     */ import nc.ui.uif2.actions.TemplatePrintAction;
/*     */ 
/*     */ public class material_pfcc_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_profitcost() { if (context.get("material_profitcost") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_profitcost");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_profitcost", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialProfitCostListView());
/*  19 */     bean.setClosingListener(getMaterialProfitCostClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialProfitCostEditAction());list.add(getMaterialProfitCostDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProfitCostRefreshAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProfitCostListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.pfcc.model.MaterialProfitCostAppModelService getMaterialProfitCostAppModelService() { if (context.get("materialProfitCostAppModelService") != null)
/*  29 */       return (nc.ui.bd.material.pfcc.model.MaterialProfitCostAppModelService)context.get("materialProfitCostAppModelService");
/*  30 */     nc.ui.bd.material.pfcc.model.MaterialProfitCostAppModelService bean = new nc.ui.bd.material.pfcc.model.MaterialProfitCostAppModelService();
/*  31 */     context.put("materialProfitCostAppModelService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialProfitCostAppModel() {
/*  38 */     if (context.get("materialProfitCostAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialProfitCostAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialProfitCostAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialProfitCostAppModelService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_1660032());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_1660032() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1660032") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1660032");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1660032", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pfcc.model.MaterialProfitCostAppModelDataManager getMaterialProfitCostModelDataManager() {
/*  62 */     if (context.get("materialProfitCostModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.pfcc.model.MaterialProfitCostAppModelDataManager)context.get("materialProfitCostModelDataManager");
/*  64 */     nc.ui.bd.material.pfcc.model.MaterialProfitCostAppModelDataManager bean = new nc.ui.bd.material.pfcc.model.MaterialProfitCostAppModelDataManager();
/*  65 */     context.put("materialProfitCostModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialProfitCostAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialProfitCostMediator() {
/*  73 */     if (context.get("materialProfitCostMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialProfitCostMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialProfitCostMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialProfitCostModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialProfitCostAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialProfitCostListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.assistant.MarAsstPubShowUtil getMaterialPfccMarAsstMediator() {
/*  87 */     if (context.get("materialPfccMarAsstMediator") != null)
/*  88 */       return (nc.ui.bd.material.assistant.MarAsstPubShowUtil)context.get("materialPfccMarAsstMediator");
/*  89 */     nc.ui.bd.material.assistant.MarAsstPubShowUtil bean = new nc.ui.bd.material.assistant.MarAsstPubShowUtil();
/*  90 */     context.put("materialPfccMarAsstMediator", bean);
/*  91 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/*  92 */     bean.setListEditor(getMaterialProfitCostListView());
/*  93 */     bean.setCardEditor(getMaterialProfitCostEditor());
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
/*     */   public nc.ui.bd.material.pfcc.view.MaterialProfitCostListView getMaterialProfitCostListView() {
/* 105 */     if (context.get("materialProfitCostListView") != null)
/* 106 */       return (nc.ui.bd.material.pfcc.view.MaterialProfitCostListView)context.get("materialProfitCostListView");
/* 107 */     nc.ui.bd.material.pfcc.view.MaterialProfitCostListView bean = new nc.ui.bd.material.pfcc.view.MaterialProfitCostListView();
/* 108 */     context.put("materialProfitCostListView", bean);
/* 109 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 110 */     bean.setModel(getMaterialProfitCostAppModel());
/* 111 */     bean.setNodekey("profitcost");
/* 112 */     bean.setName(getI18nFB_196f429());
/* 113 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_c01aac());
/* 114 */     bean.initUI();
/* 115 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 116 */     invokeInitializingBean(bean);
/* 117 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_196f429() {
/* 121 */     if (context.get("nc.ui.uif2.I18nFB#196f429") != null)
/* 122 */       return (String)context.get("nc.ui.uif2.I18nFB#196f429");
/* 123 */     I18nFB bean = new I18nFB();
/* 124 */     context.put("&nc.ui.uif2.I18nFB#196f429", bean);bean.setResDir("10140mag");
/* 125 */     bean.setDefaultValue("利润中心成本");
/* 126 */     bean.setResId("010140mag0359");
/* 127 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 128 */     invokeInitializingBean(bean);
/*     */     try {
/* 130 */       Object product = bean.getObject();
/* 131 */       context.put("nc.ui.uif2.I18nFB#196f429", product);
/* 132 */       return (String)product;
/*     */     } catch (Exception e) {
/* 134 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 137 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_c01aac() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#c01aac") != null)
/* 138 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#c01aac");
/* 139 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 140 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#c01aac", bean);
/* 141 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 142 */     bean.setParams(getManagedList1());
/* 143 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 144 */     invokeInitializingBean(bean);
/* 145 */     return bean;
/*     */   }
/*     */   
/* 148 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_177b7bd());return list;
/*     */   }
/*     */   
/* 151 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_177b7bd() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#177b7bd") != null)
/* 152 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#177b7bd");
/* 153 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 154 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#177b7bd", bean);
/* 155 */     bean.setMdfullname("uapbd.materialpfcc");
/* 156 */     bean.setPos(0);
/* 157 */     bean.setPrefix("def");
/* 158 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 159 */     invokeInitializingBean(bean);
/* 160 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pfcc.view.MaterialProfitCostEditor getMaterialProfitCostEditor() {
/* 164 */     if (context.get("materialProfitCostEditor") != null)
/* 165 */       return (nc.ui.bd.material.pfcc.view.MaterialProfitCostEditor)context.get("materialProfitCostEditor");
/* 166 */     nc.ui.bd.material.pfcc.view.MaterialProfitCostEditor bean = new nc.ui.bd.material.pfcc.view.MaterialProfitCostEditor();
/* 167 */     context.put("materialProfitCostEditor", bean);
/* 168 */     bean.setModel(getMaterialProfitCostAppModel());
/* 169 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 170 */     bean.setNodekey("profitcost");
/* 171 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_596e4d());
/* 172 */     bean.setBodyActionMap(getManagedMap0());
/* 173 */     bean.setActions(getManagedList4());
/* 174 */     bean.initUI();
/* 175 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 176 */     invokeInitializingBean(bean);
/* 177 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_596e4d() {
/* 181 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#596e4d") != null)
/* 182 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#596e4d");
/* 183 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 184 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#596e4d", bean);
/* 185 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 186 */     bean.setParams(getManagedList2());
/* 187 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 188 */     invokeInitializingBean(bean);
/* 189 */     return bean;
/*     */   }
/*     */   
/* 192 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_1718be());return list;
/*     */   }
/*     */   
/* 195 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1718be() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1718be") != null)
/* 196 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1718be");
/* 197 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 198 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1718be", bean);
/* 199 */     bean.setMdfullname("uapbd.materialpfcc");
/* 200 */     bean.setPos(0);
/* 201 */     bean.setPrefix("def");
/* 202 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 203 */     invokeInitializingBean(bean);
/* 204 */     return bean;
/*     */   }
/*     */   
/* 207 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("profitcostlist", getManagedList3());return map; }
/*     */   
/* 209 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getMaterialPfccsubAddLineAction());list.add(getMaterialPfccsubDelLineAction());return list; }
/*     */   
/* 211 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getMaterialProfitCostFirstLineAction());list.add(getMaterialProfitCostPreLineAction());list.add(getMaterialProfitCostNextLineAction());list.add(getMaterialProfitCostLastLineAction());return list;
/*     */   }
/*     */   
/* 214 */   public nc.ui.bd.material.pfcc.action.MaterialPFCCAddLineAction getMaterialPfccsubAddLineAction() { if (context.get("materialPfccsubAddLineAction") != null)
/* 215 */       return (nc.ui.bd.material.pfcc.action.MaterialPFCCAddLineAction)context.get("materialPfccsubAddLineAction");
/* 216 */     nc.ui.bd.material.pfcc.action.MaterialPFCCAddLineAction bean = new nc.ui.bd.material.pfcc.action.MaterialPFCCAddLineAction();
/* 217 */     context.put("materialPfccsubAddLineAction", bean);
/* 218 */     bean.setModel(getMaterialProfitCostAppModel());
/* 219 */     bean.setCardpanel(getMaterialProfitCostEditor());
/* 220 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 221 */     invokeInitializingBean(bean);
/* 222 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DelLineAction getMaterialPfccsubDelLineAction() {
/* 226 */     if (context.get("materialPfccsubDelLineAction") != null)
/* 227 */       return (nc.ui.uif2.actions.DelLineAction)context.get("materialPfccsubDelLineAction");
/* 228 */     nc.ui.uif2.actions.DelLineAction bean = new nc.ui.uif2.actions.DelLineAction();
/* 229 */     context.put("materialPfccsubDelLineAction", bean);
/* 230 */     bean.setModel(getMaterialProfitCostAppModel());
/* 231 */     bean.setCardpanel(getMaterialProfitCostEditor());
/* 232 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 233 */     invokeInitializingBean(bean);
/* 234 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialProfitCostDialogMediator() {
/* 238 */     if (context.get("materialProfitCostDialogMediator") != null)
/* 239 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialProfitCostDialogMediator");
/* 240 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 241 */     context.put("materialProfitCostDialogMediator", bean);
/* 242 */     bean.setModel(getMaterialProfitCostAppModel());
/* 243 */     bean.setDialogContainer(getMaterialProfitCostDialogContainer());
/* 244 */     bean.setName(getI18nFB_1588baa());
/* 245 */     bean.setClosingListener(getMaterialProfitCostClosingListener());
/* 246 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 247 */     invokeInitializingBean(bean);
/* 248 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_1588baa() {
/* 252 */     if (context.get("nc.ui.uif2.I18nFB#1588baa") != null)
/* 253 */       return (String)context.get("nc.ui.uif2.I18nFB#1588baa");
/* 254 */     I18nFB bean = new I18nFB();
/* 255 */     context.put("&nc.ui.uif2.I18nFB#1588baa", bean);bean.setResDir("10140mag");
/* 256 */     bean.setDefaultValue("利润中心成本");
/* 257 */     bean.setResId("010140mag0359");
/* 258 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 259 */     invokeInitializingBean(bean);
/*     */     try {
/* 261 */       Object product = bean.getObject();
/* 262 */       context.put("nc.ui.uif2.I18nFB#1588baa", product);
/* 263 */       return (String)product;
/*     */     } catch (Exception e) {
/* 265 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 268 */   public nc.ui.uif2.TangramContainer getMaterialProfitCostDialogContainer() { if (context.get("materialProfitCostDialogContainer") != null)
/* 269 */       return (nc.ui.uif2.TangramContainer)context.get("materialProfitCostDialogContainer");
/* 270 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 271 */     context.put("materialProfitCostDialogContainer", bean);
/* 272 */     bean.setConstraints(getManagedList5());
/* 273 */     bean.setActions(getManagedList6());
/* 274 */     bean.setEditActions(getManagedList7());
/* 275 */     bean.setModel(getMaterialProfitCostAppModel());
/* 276 */     bean.initUI();
/* 277 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 278 */     invokeInitializingBean(bean);
/* 279 */     return bean;
/*     */   }
/*     */   
/* 282 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_558155());return list;
/*     */   }
/*     */   
/* 285 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_558155() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#558155") != null)
/* 286 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#558155");
/* 287 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 288 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#558155", bean);
/* 289 */     bean.setNewComponent(getMaterialProfitCostEditor());
/* 290 */     bean.setNewComponentName(getI18nFB_398529());
/* 291 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 292 */     invokeInitializingBean(bean);
/* 293 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_398529() {
/* 297 */     if (context.get("nc.ui.uif2.I18nFB#398529") != null)
/* 298 */       return (String)context.get("nc.ui.uif2.I18nFB#398529");
/* 299 */     I18nFB bean = new I18nFB();
/* 300 */     context.put("&nc.ui.uif2.I18nFB#398529", bean);bean.setResDir("10140mag");
/* 301 */     bean.setDefaultValue("利润中心成本");
/* 302 */     bean.setResId("010140mag0359");
/* 303 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 304 */     invokeInitializingBean(bean);
/*     */     try {
/* 306 */       Object product = bean.getObject();
/* 307 */       context.put("nc.ui.uif2.I18nFB#398529", product);
/* 308 */       return (String)product;
/*     */     } catch (Exception e) {
/* 310 */       throw new RuntimeException(e); } }
/*     */   
/* 312 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialProfitCostEditAction());list.add(getMaterialProfitCostDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProfitCostRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProfitCostPrintActionGroup());return list; }
/*     */   
/* 314 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialProfitCostSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialProfitCostCancelAction());return list;
/*     */   }
/*     */   
/* 317 */   public nc.ui.uif2.actions.EditAction getMaterialProfitCostEditAction() { if (context.get("materialProfitCostEditAction") != null)
/* 318 */       return (nc.ui.uif2.actions.EditAction)context.get("materialProfitCostEditAction");
/* 319 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 320 */     context.put("materialProfitCostEditAction", bean);
/* 321 */     bean.setCode("pfccEdit");
/* 322 */     bean.setModel(getMaterialProfitCostAppModel());
/* 323 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 324 */     invokeInitializingBean(bean);
/* 325 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialProfitCostDeleteAction() {
/* 329 */     if (context.get("materialProfitCostDeleteAction") != null)
/* 330 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialProfitCostDeleteAction");
/* 331 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 332 */     context.put("materialProfitCostDeleteAction", bean);
/* 333 */     bean.setCode("pfccDelete");
/* 334 */     bean.setModel(getMaterialProfitCostAppModel());
/* 335 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 336 */     invokeInitializingBean(bean);
/* 337 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.SaveAction getMaterialProfitCostSaveAction() {
/* 341 */     if (context.get("materialProfitCostSaveAction") != null)
/* 342 */       return (nc.ui.uif2.actions.SaveAction)context.get("materialProfitCostSaveAction");
/* 343 */     nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
/* 344 */     context.put("materialProfitCostSaveAction", bean);
/* 345 */     bean.setCode("pfccSave");
/* 346 */     bean.setModel(getMaterialProfitCostAppModel());
/* 347 */     bean.setEditor(getMaterialProfitCostEditor());
/* 348 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 349 */     invokeInitializingBean(bean);
/* 350 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialProfitCostCancelAction() {
/* 354 */     if (context.get("materialProfitCostCancelAction") != null)
/* 355 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialProfitCostCancelAction");
/* 356 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 357 */     context.put("materialProfitCostCancelAction", bean);
/* 358 */     bean.setCode("pfccCancel");
/* 359 */     bean.setModel(getMaterialProfitCostAppModel());
/* 360 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 361 */     invokeInitializingBean(bean);
/* 362 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialProfitCostRefreshSingleAction() {
/* 366 */     if (context.get("materialProfitCostRefreshSingleAction") != null)
/* 367 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialProfitCostRefreshSingleAction");
/* 368 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 369 */     context.put("materialProfitCostRefreshSingleAction", bean);
/* 370 */     bean.setCode("pfccRefresh");
/* 371 */     bean.setModel(getMaterialProfitCostAppModel());
/* 372 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 373 */     invokeInitializingBean(bean);
/* 374 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialProfitCostRefreshAction() {
/* 378 */     if (context.get("materialProfitCostRefreshAction") != null)
/* 379 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialProfitCostRefreshAction");
/* 380 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 381 */     context.put("materialProfitCostRefreshAction", bean);
/* 382 */     bean.setCode("pfccRefresh");
/* 383 */     bean.setModel(getMaterialProfitCostAppModel());
/* 384 */     bean.setManager(getMaterialProfitCostModelDataManager());
/* 385 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 386 */     invokeInitializingBean(bean);
/* 387 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialProfitCostListPrintActionGroup() {
/* 391 */     if (context.get("materialProfitCostListPrintActionGroup") != null)
/* 392 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialProfitCostListPrintActionGroup");
/* 393 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 394 */     context.put("materialProfitCostListPrintActionGroup", bean);
/* 395 */     bean.setCode("ProfitCostPrintMenu");
/* 396 */     bean.setActions(getManagedList8());
/* 397 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 398 */     invokeInitializingBean(bean);
/* 399 */     return bean;
/*     */   }
/*     */   
/* 402 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialProfitCostListTempletPrintAction());list.add(getMaterialProfitCostListTempletPreviewAction());list.add(getMaterialProfitCostListOutputAction());return list;
/*     */   }
/*     */   
/* 405 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialProfitCostListTempletPreviewAction() { if (context.get("materialProfitCostListTempletPreviewAction") != null)
/* 406 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialProfitCostListTempletPreviewAction");
/* 407 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 408 */     context.put("materialProfitCostListTempletPreviewAction", bean);
/* 409 */     bean.setCode("pfccTempPreview");
/* 410 */     bean.setModel(getMaterialProfitCostAppModel());
/* 411 */     bean.setNodeKey("pfcclist");
/* 412 */     bean.setPrintDlgParentConatiner(getMaterialProfitCostListView());
/* 413 */     bean.setDatasource(getMaterialProfitCostListardDataSource());
/* 414 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 415 */     invokeInitializingBean(bean);
/* 416 */     return bean;
/*     */   }
/*     */   
/*     */   public TemplatePrintAction getMaterialProfitCostListTempletPrintAction() {
/* 420 */     if (context.get("materialProfitCostListTempletPrintAction") != null)
/* 421 */       return (TemplatePrintAction)context.get("materialProfitCostListTempletPrintAction");
/* 422 */     TemplatePrintAction bean = new TemplatePrintAction();
/* 423 */     context.put("materialProfitCostListTempletPrintAction", bean);
/* 424 */     bean.setCode("pfccTempPrint");
/* 425 */     bean.setModel(getMaterialProfitCostAppModel());
/* 426 */     bean.setNodeKey("pfcclist");
/* 427 */     bean.setPrintDlgParentConatiner(getMaterialProfitCostListView());
/* 428 */     bean.setDatasource(getMaterialProfitCostListardDataSource());
/* 429 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 430 */     invokeInitializingBean(bean);
/* 431 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialProfitCostListOutputAction() {
/* 435 */     if (context.get("materialProfitCostListOutputAction") != null)
/* 436 */       return (OutputAction)context.get("materialProfitCostListOutputAction");
/* 437 */     OutputAction bean = new OutputAction();
/* 438 */     context.put("materialProfitCostListOutputAction", bean);
/* 439 */     bean.setCode("pfccOutput");
/* 440 */     bean.setModel(getMaterialProfitCostAppModel());
/* 441 */     bean.setNodeKey("pfcclist");
/* 442 */     bean.setPrintDlgParentConatiner(getMaterialProfitCostListView());
/* 443 */     bean.setDatasource(getMaterialProfitCostListardDataSource());
/* 444 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 445 */     invokeInitializingBean(bean);
/* 446 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialProfitCostListardDataSource() {
/* 450 */     if (context.get("materialProfitCostListardDataSource") != null)
/* 451 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialProfitCostListardDataSource");
/* 452 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 453 */     context.put("materialProfitCostListardDataSource", bean);
/* 454 */     bean.setModel(getMaterialProfitCostAppModel());
/* 455 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 456 */     invokeInitializingBean(bean);
/* 457 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialProfitCostPrintActionGroup() {
/* 461 */     if (context.get("materialProfitCostPrintActionGroup") != null)
/* 462 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialProfitCostPrintActionGroup");
/* 463 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 464 */     context.put("materialProfitCostPrintActionGroup", bean);
/* 465 */     bean.setCode("pfccPrintMenu");
/* 466 */     bean.setActions(getManagedList9());
/* 467 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 468 */     invokeInitializingBean(bean);
/* 469 */     return bean;
/*     */   }
/*     */   
/* 472 */   private List getManagedList9() { List list = new java.util.ArrayList();list.add(getMaterialProfitCostTempletPrintAction());list.add(getMaterialProfitCostTempletPreviewAction());list.add(getMaterialProfitCostOutputAction());return list;
/*     */   }
/*     */   
/* 475 */   public nc.ui.uif2.actions.TemplatePreviewAction getMaterialProfitCostTempletPreviewAction() { if (context.get("materialProfitCostTempletPreviewAction") != null)
/* 476 */       return (nc.ui.uif2.actions.TemplatePreviewAction)context.get("materialProfitCostTempletPreviewAction");
/* 477 */     nc.ui.uif2.actions.TemplatePreviewAction bean = new nc.ui.uif2.actions.TemplatePreviewAction();
/* 478 */     context.put("materialProfitCostTempletPreviewAction", bean);
/* 479 */     bean.setCode("pfccTempPreview");
/* 480 */     bean.setModel(getMaterialProfitCostAppModel());
/* 481 */     bean.setNodeKey("pfcccard");
/* 482 */     bean.setPrintDlgParentConatiner(getMaterialProfitCostEditor());
/* 483 */     bean.setDatasource(getMaterialProfitCostCardDataSource());
/* 484 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 485 */     invokeInitializingBean(bean);
/* 486 */     return bean;
/*     */   }
/*     */   
/*     */   public TemplatePrintAction getMaterialProfitCostTempletPrintAction() {
/* 490 */     if (context.get("materialProfitCostTempletPrintAction") != null)
/* 491 */       return (TemplatePrintAction)context.get("materialProfitCostTempletPrintAction");
/* 492 */     TemplatePrintAction bean = new TemplatePrintAction();
/* 493 */     context.put("materialProfitCostTempletPrintAction", bean);
/* 494 */     bean.setCode("pfccTempPrint");
/* 495 */     bean.setModel(getMaterialProfitCostAppModel());
/* 496 */     bean.setNodeKey("pfcccard");
/* 497 */     bean.setPrintDlgParentConatiner(getMaterialProfitCostEditor());
/* 498 */     bean.setDatasource(getMaterialProfitCostCardDataSource());
/* 499 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 500 */     invokeInitializingBean(bean);
/* 501 */     return bean;
/*     */   }
/*     */   
/*     */   public OutputAction getMaterialProfitCostOutputAction() {
/* 505 */     if (context.get("materialProfitCostOutputAction") != null)
/* 506 */       return (OutputAction)context.get("materialProfitCostOutputAction");
/* 507 */     OutputAction bean = new OutputAction();
/* 508 */     context.put("materialProfitCostOutputAction", bean);
/* 509 */     bean.setCode("pfccOutput");
/* 510 */     bean.setModel(getMaterialProfitCostAppModel());
/* 511 */     bean.setNodeKey("pfcccard");
/* 512 */     bean.setPrintDlgParentConatiner(getMaterialProfitCostEditor());
/* 513 */     bean.setDatasource(getMaterialProfitCostCardDataSource());
/* 514 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 515 */     invokeInitializingBean(bean);
/* 516 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialProfitCostCardDataSource() {
/* 520 */     if (context.get("materialProfitCostCardDataSource") != null)
/* 521 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialProfitCostCardDataSource");
/* 522 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 523 */     context.put("materialProfitCostCardDataSource", bean);
/* 524 */     bean.setModel(getMaterialProfitCostAppModel());
/* 525 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 526 */     invokeInitializingBean(bean);
/* 527 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMaterialProfitCostFirstLineAction() {
/* 531 */     if (context.get("materialProfitCostFirstLineAction") != null)
/* 532 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materialProfitCostFirstLineAction");
/* 533 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 534 */     context.put("materialProfitCostFirstLineAction", bean);
/* 535 */     bean.setModel(getMaterialProfitCostAppModel());
/* 536 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 537 */     invokeInitializingBean(bean);
/* 538 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialProfitCostNextLineAction() {
/* 542 */     if (context.get("materialProfitCostNextLineAction") != null)
/* 543 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialProfitCostNextLineAction");
/* 544 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 545 */     context.put("materialProfitCostNextLineAction", bean);
/* 546 */     bean.setModel(getMaterialProfitCostAppModel());
/* 547 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 548 */     invokeInitializingBean(bean);
/* 549 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialProfitCostPreLineAction() {
/* 553 */     if (context.get("materialProfitCostPreLineAction") != null)
/* 554 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialProfitCostPreLineAction");
/* 555 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 556 */     context.put("materialProfitCostPreLineAction", bean);
/* 557 */     bean.setModel(getMaterialProfitCostAppModel());
/* 558 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 559 */     invokeInitializingBean(bean);
/* 560 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialProfitCostLastLineAction() {
/* 564 */     if (context.get("materialProfitCostLastLineAction") != null)
/* 565 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialProfitCostLastLineAction");
/* 566 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 567 */     context.put("materialProfitCostLastLineAction", bean);
/* 568 */     bean.setModel(getMaterialProfitCostAppModel());
/* 569 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 570 */     invokeInitializingBean(bean);
/* 571 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialProfitCostClosingListener() {
/* 575 */     if (context.get("materialProfitCostClosingListener") != null)
/* 576 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialProfitCostClosingListener");
/* 577 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 578 */     context.put("materialProfitCostClosingListener", bean);
/* 579 */     bean.setModel(getMaterialProfitCostAppModel());
/* 580 */     bean.setSaveaction(getMaterialProfitCostSaveAction());
/* 581 */     bean.setCancelaction(getMaterialProfitCostCancelAction());
/* 582 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 583 */     invokeInitializingBean(bean);
/* 584 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialProfitCostAppModel());
/* 641 */     bean.setEditor(getMaterialProfitCostEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialProfitCostAppModel());
/* 641 */     bean.setEditor(getMaterialProfitCostEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }