/*     */ package nc.ui.bd.material.fi;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.TemplatePreviewAction;
/*     */ import nc.ui.uif2.actions.TemplatePrintAction;
/*     */ 
/*     */ public class material_fi_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_fi() { if (context.get("material_fi") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_fi");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_fi", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialFiListView());
/*  19 */     bean.setClosingListener(getMaterialFiClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialFiEditAction());list.add(getMaterialFiDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialFiRefreshAction());
list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialFiListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.fi.model.MaterialFiAppModelService getMaterialFiAppModelService() { if (context.get("materialFiAppModelService") != null)
/*  29 */       return (nc.ui.bd.material.fi.model.MaterialFiAppModelService)context.get("materialFiAppModelService");
/*  30 */     nc.ui.bd.material.fi.model.MaterialFiAppModelService bean = new nc.ui.bd.material.fi.model.MaterialFiAppModelService();
/*  31 */     context.put("materialFiAppModelService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialFiAppModel() {
/*  38 */     if (context.get("materialFiAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialFiAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialFiAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialFiAppModelService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_15f03b());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_15f03b() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#15f03b") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#15f03b");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#15f03b", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.fi.model.MaterialFiAppModelDataManager getMaterialFiModelDataManager() {
/*  62 */     if (context.get("materialFiModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.fi.model.MaterialFiAppModelDataManager)context.get("materialFiModelDataManager");
/*  64 */     nc.ui.bd.material.fi.model.MaterialFiAppModelDataManager bean = new nc.ui.bd.material.fi.model.MaterialFiAppModelDataManager();
/*  65 */     context.put("materialFiModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialFiAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialFiMediator() {
/*  73 */     if (context.get("materialFiMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialFiMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialFiMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialFiModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialFiAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialFiListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.fi.view.MaterialFiListView getMaterialFiListView() {
/*  87 */     if (context.get("materialFiListView") != null)
/*  88 */       return (nc.ui.bd.material.fi.view.MaterialFiListView)context.get("materialFiListView");
/*  89 */     nc.ui.bd.material.fi.view.MaterialFiListView bean = new nc.ui.bd.material.fi.view.MaterialFiListView();
/*  90 */     context.put("materialFiListView", bean);
/*  91 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/*  92 */     bean.setModel(getMaterialFiAppModel());
/*  93 */     bean.setNodekey("fi");
/*  94 */     bean.setName(getI18nFB_4ece41());
/*  95 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_8e3549());
/*  96 */     bean.initUI();
/*  97 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  98 */     invokeInitializingBean(bean);
/*  99 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_4ece41() {
/* 103 */     if (context.get("nc.ui.uif2.I18nFB#4ece41") != null)
/* 104 */       return (String)context.get("nc.ui.uif2.I18nFB#4ece41");
/* 105 */     I18nFB bean = new I18nFB();
/* 106 */     context.put("&nc.ui.uif2.I18nFB#4ece41", bean);bean.setResDir("10140mag");
/* 107 */     bean.setDefaultValue("财务信息");
/* 108 */     bean.setResId("110140mag0011");
/* 109 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 110 */     invokeInitializingBean(bean);
/*     */     try {
/* 112 */       Object product = bean.getObject();
/* 113 */       context.put("nc.ui.uif2.I18nFB#4ece41", product);
/* 114 */       return (String)product;
/*     */     } catch (Exception e) {
/* 116 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 119 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_8e3549() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#8e3549") != null)
/* 120 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#8e3549");
/* 121 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 122 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#8e3549", bean);
/* 123 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 124 */     bean.setParams(getManagedList1());
/* 125 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 126 */     invokeInitializingBean(bean);
/* 127 */     return bean;
/*     */   }
/*     */   
/* 130 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_1c72213());return list;
/*     */   }
/*     */   
/* 133 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1c72213() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1c72213") != null)
/* 134 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1c72213");
/* 135 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 136 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1c72213", bean);
/* 137 */     bean.setMdfullname("uap.materialfi");
/* 138 */     bean.setPos(0);
/* 139 */     bean.setPrefix("def");
/* 140 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 141 */     invokeInitializingBean(bean);
/* 142 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.fi.view.MaterialFiEditor getMaterialFiEditor() {
/* 146 */     if (context.get("materialFiEditor") != null)
/* 147 */       return (nc.ui.bd.material.fi.view.MaterialFiEditor)context.get("materialFiEditor");
/* 148 */     nc.ui.bd.material.fi.view.MaterialFiEditor bean = new nc.ui.bd.material.fi.view.MaterialFiEditor();
/* 149 */     context.put("materialFiEditor", bean);
/* 150 */     bean.setModel(getMaterialFiAppModel());
/* 151 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 152 */     bean.setNodekey("fi");
/* 153 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_10a9b63());
/* 154 */     bean.setActions(getManagedList3());
/* 155 */     bean.initUI();
/* 156 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 157 */     invokeInitializingBean(bean);
/* 158 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_10a9b63() {
/* 162 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#10a9b63") != null)
/* 163 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#10a9b63");
/* 164 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 165 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#10a9b63", bean);
/* 166 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 167 */     bean.setParams(getManagedList2());
/* 168 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 169 */     invokeInitializingBean(bean);
/* 170 */     return bean;
/*     */   }
/*     */   
/* 173 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_4c72e3());return list;
/*     */   }
/*     */   
/* 176 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_4c72e3() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#4c72e3") != null)
/* 177 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#4c72e3");
/* 178 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 179 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#4c72e3", bean);
/* 180 */     bean.setMdfullname("uap.materialfi");
/* 181 */     bean.setPos(0);
/* 182 */     bean.setPrefix("def");
/* 183 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 184 */     invokeInitializingBean(bean);
/* 185 */     return bean;
/*     */   }
/*     */   
/* 188 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getMaterialFiFirstLineAction());list.add(getMaterialFiPreLineAction());list.add(getMaterialFiNextLineAction());list.add(getMaterialFiLastLineAction());return list;
/*     */   }
/*     */   
/* 191 */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialFiDialogMediator() { if (context.get("materialFiDialogMediator") != null)
/* 192 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialFiDialogMediator");
/* 193 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 194 */     context.put("materialFiDialogMediator", bean);
/* 195 */     bean.setModel(getMaterialFiAppModel());
/* 196 */     bean.setDialogContainer(getMaterialFiDialogContainer());
/* 197 */     bean.setName(getI18nFB_15fb989());
/* 198 */     bean.setClosingListener(getMaterialFiClosingListener());
/* 199 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 200 */     invokeInitializingBean(bean);
/* 201 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_15fb989() {
/* 205 */     if (context.get("nc.ui.uif2.I18nFB#15fb989") != null)
/* 206 */       return (String)context.get("nc.ui.uif2.I18nFB#15fb989");
/* 207 */     I18nFB bean = new I18nFB();
/* 208 */     context.put("&nc.ui.uif2.I18nFB#15fb989", bean);bean.setResDir("10140mag");
/* 209 */     bean.setDefaultValue("财务信息");
/* 210 */     bean.setResId("110140mag0011");
/* 211 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 212 */     invokeInitializingBean(bean);
/*     */     try {
/* 214 */       Object product = bean.getObject();
/* 215 */       context.put("nc.ui.uif2.I18nFB#15fb989", product);
/* 216 */       return (String)product;
/*     */     } catch (Exception e) {
/* 218 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 221 */   public nc.ui.uif2.TangramContainer getMaterialFiDialogContainer() { if (context.get("materialFiDialogContainer") != null)
/* 222 */       return (nc.ui.uif2.TangramContainer)context.get("materialFiDialogContainer");
/* 223 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 224 */     context.put("materialFiDialogContainer", bean);
/* 225 */     bean.setConstraints(getManagedList4());
/* 226 */     bean.setActions(getManagedList5());
/* 227 */     bean.setEditActions(getManagedList6());
/* 228 */     bean.setModel(getMaterialFiAppModel());
/* 229 */     bean.initUI();
/* 230 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 231 */     invokeInitializingBean(bean);
/* 232 */     return bean;
/*     */   }
/*     */   
/* 235 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_108692());return list;
/*     */   }
/*     */   
/* 238 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_108692() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#108692") != null)
/* 239 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#108692");
/* 240 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 241 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#108692", bean);
/* 242 */     bean.setNewComponent(getMaterialFiEditor());
/* 243 */     bean.setNewComponentName(getI18nFB_18de27d());
/* 244 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 245 */     invokeInitializingBean(bean);
/* 246 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_18de27d() {
/* 250 */     if (context.get("nc.ui.uif2.I18nFB#18de27d") != null)
/* 251 */       return (String)context.get("nc.ui.uif2.I18nFB#18de27d");
/* 252 */     I18nFB bean = new I18nFB();
/* 253 */     context.put("&nc.ui.uif2.I18nFB#18de27d", bean);bean.setResDir("10140mag");
/* 254 */     bean.setDefaultValue("财务信息");
/* 255 */     bean.setResId("110140mag0011");
/* 256 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 257 */     invokeInitializingBean(bean);
/*     */     try {
/* 259 */       Object product = bean.getObject();
/* 260 */       context.put("nc.ui.uif2.I18nFB#18de27d", product);
/* 261 */       return (String)product;
/*     */     } catch (Exception e) {
/* 263 */       throw new RuntimeException(e); } }
/*     */   
/* 265 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getMaterialFiEditAction());list.add(getMaterialFiDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialFiRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialFiPrintActionGroup());return list; }
/*     */   
/* 267 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialFiSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialFiCancelAction());return list;
/*     */   }
/*     */   
/* 270 */   public nc.ui.uif2.actions.EditAction getMaterialFiEditAction() { if (context.get("materialFiEditAction") != null)
/* 271 */       return (nc.ui.uif2.actions.EditAction)context.get("materialFiEditAction");
/* 272 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 273 */     context.put("materialFiEditAction", bean);
/* 274 */     bean.setCode("FiEdit");
/* 275 */     bean.setModel(getMaterialFiAppModel());
/* 276 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 277 */     invokeInitializingBean(bean);
/* 278 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialFiDeleteAction() {
/* 282 */     if (context.get("materialFiDeleteAction") != null)
/* 283 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialFiDeleteAction");
/* 284 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 285 */     context.put("materialFiDeleteAction", bean);
/* 286 */     bean.setCode("FiDelete");
/* 287 */     bean.setModel(getMaterialFiAppModel());
/* 288 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 289 */     invokeInitializingBean(bean);
/* 290 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.SaveAction getMaterialFiSaveAction() {
/* 294 */     if (context.get("materialFiSaveAction") != null)
/* 295 */       return (nc.ui.uif2.actions.SaveAction)context.get("materialFiSaveAction");
/* 296 */     nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
/* 297 */     context.put("materialFiSaveAction", bean);
/* 298 */     bean.setCode("FiSave");
/* 299 */     bean.setModel(getMaterialFiAppModel());
/* 300 */     bean.setEditor(getMaterialFiEditor());
/* 301 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 302 */     invokeInitializingBean(bean);
/* 303 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialFiCancelAction() {
/* 307 */     if (context.get("materialFiCancelAction") != null)
/* 308 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialFiCancelAction");
/* 309 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 310 */     context.put("materialFiCancelAction", bean);
/* 311 */     bean.setCode("FiCancel");
/* 312 */     bean.setModel(getMaterialFiAppModel());
/* 313 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 314 */     invokeInitializingBean(bean);
/* 315 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialFiRefreshAction() {
/* 319 */     if (context.get("materialFiRefreshAction") != null)
/* 320 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialFiRefreshAction");
/* 321 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 322 */     context.put("materialFiRefreshAction", bean);
/* 323 */     bean.setCode("FiRefresh");
/* 324 */     bean.setModel(getMaterialFiAppModel());
/* 325 */     bean.setManager(getMaterialFiModelDataManager());
/* 326 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 327 */     invokeInitializingBean(bean);
/* 328 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialFiRefreshSingleAction() {
/* 332 */     if (context.get("materialFiRefreshSingleAction") != null)
/* 333 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialFiRefreshSingleAction");
/* 334 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 335 */     context.put("materialFiRefreshSingleAction", bean);
/* 336 */     bean.setCode("FiRefresh");
/* 337 */     bean.setModel(getMaterialFiAppModel());
/* 338 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 339 */     invokeInitializingBean(bean);
/* 340 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialFiListPrintActionGroup() {
/* 344 */     if (context.get("materialFiListPrintActionGroup") != null)
/* 345 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialFiListPrintActionGroup");
/* 346 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 347 */     context.put("materialFiListPrintActionGroup", bean);
/* 348 */     bean.setActions(getManagedList7());
/* 349 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 350 */     invokeInitializingBean(bean);
/* 351 */     return bean;
/*     */   }
/*     */   
/* 354 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialFiListTempletPrintAction());list.add(getMaterialFiListTempletPreviewAction());list.add(getMaterialFiListOutputAction());return list;
/*     */   }
/*     */   
/* 357 */   public TemplatePreviewAction getMaterialFiListTempletPreviewAction() { if (context.get("materialFiListTempletPreviewAction") != null)
/* 358 */       return (TemplatePreviewAction)context.get("materialFiListTempletPreviewAction");
/* 359 */     TemplatePreviewAction bean = new TemplatePreviewAction();
/* 360 */     context.put("materialFiListTempletPreviewAction", bean);
/* 361 */     bean.setCode("FiTempPreview");
/* 362 */     bean.setModel(getMaterialFiAppModel());
/* 363 */     bean.setNodeKey("filist");
/* 364 */     bean.setPrintDlgParentConatiner(getMaterialFiListView());
/* 365 */     bean.setDatasource(getMaterialFiListardDataSource());
/* 366 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 367 */     invokeInitializingBean(bean);
/* 368 */     return bean;
/*     */   }
/*     */   
/*     */   public TemplatePrintAction getMaterialFiListTempletPrintAction() {
/* 372 */     if (context.get("materialFiListTempletPrintAction") != null)
/* 373 */       return (TemplatePrintAction)context.get("materialFiListTempletPrintAction");
/* 374 */     TemplatePrintAction bean = new TemplatePrintAction();
/* 375 */     context.put("materialFiListTempletPrintAction", bean);
/* 376 */     bean.setCode("FiTempPrint");
/* 377 */     bean.setModel(getMaterialFiAppModel());
/* 378 */     bean.setNodeKey("filist");
/* 379 */     bean.setPrintDlgParentConatiner(getMaterialFiListView());
/* 380 */     bean.setDatasource(getMaterialFiListardDataSource());
/* 381 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 382 */     invokeInitializingBean(bean);
/* 383 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.OutputAction getMaterialFiListOutputAction() {
/* 387 */     if (context.get("materialFiListOutputAction") != null)
/* 388 */       return (nc.ui.uif2.actions.OutputAction)context.get("materialFiListOutputAction");
/* 389 */     nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
/* 390 */     context.put("materialFiListOutputAction", bean);
/* 391 */     bean.setCode("FiOutput");
/* 392 */     bean.setModel(getMaterialFiAppModel());
/* 393 */     bean.setNodeKey("filist");
/* 394 */     bean.setPrintDlgParentConatiner(getMaterialFiListView());
/* 395 */     bean.setDatasource(getMaterialFiListardDataSource());
/* 396 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 397 */     invokeInitializingBean(bean);
/* 398 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialFiListardDataSource() {
/* 402 */     if (context.get("materialFiListardDataSource") != null)
/* 403 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialFiListardDataSource");
/* 404 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 405 */     context.put("materialFiListardDataSource", bean);
/* 406 */     bean.setModel(getMaterialFiAppModel());
/* 407 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 408 */     invokeInitializingBean(bean);
/* 409 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialFiPrintActionGroup() {
/* 413 */     if (context.get("materialFiPrintActionGroup") != null)
/* 414 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialFiPrintActionGroup");
/* 415 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 416 */     context.put("materialFiPrintActionGroup", bean);
/* 417 */     bean.setCode("FiPrintMenu");
/* 418 */     bean.setActions(getManagedList8());
/* 419 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 420 */     invokeInitializingBean(bean);
/* 421 */     return bean;
/*     */   }
/*     */   
/* 424 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialFiTempletPrintAction());list.add(getMaterialFiTempletPreviewAction());list.add(getMaterialFiOutputAction());return list;
/*     */   }
/*     */   
/* 427 */   public TemplatePreviewAction getMaterialFiTempletPreviewAction() { if (context.get("materialFiTempletPreviewAction") != null)
/* 428 */       return (TemplatePreviewAction)context.get("materialFiTempletPreviewAction");
/* 429 */     TemplatePreviewAction bean = new TemplatePreviewAction();
/* 430 */     context.put("materialFiTempletPreviewAction", bean);
/* 431 */     bean.setCode("FiTempPreview");
/* 432 */     bean.setModel(getMaterialFiAppModel());
/* 433 */     bean.setNodeKey("ficard");
/* 434 */     bean.setPrintDlgParentConatiner(getMaterialFiEditor());
/* 435 */     bean.setDatasource(getMaterialFiCardDataSource());
/* 436 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 437 */     invokeInitializingBean(bean);
/* 438 */     return bean;
/*     */   }
/*     */   
/*     */   public TemplatePrintAction getMaterialFiTempletPrintAction() {
/* 442 */     if (context.get("materialFiTempletPrintAction") != null)
/* 443 */       return (TemplatePrintAction)context.get("materialFiTempletPrintAction");
/* 444 */     TemplatePrintAction bean = new TemplatePrintAction();
/* 445 */     context.put("materialFiTempletPrintAction", bean);
/* 446 */     bean.setCode("FiTempPrint");
/* 447 */     bean.setModel(getMaterialFiAppModel());
/* 448 */     bean.setNodeKey("ficard");
/* 449 */     bean.setPrintDlgParentConatiner(getMaterialFiEditor());
/* 450 */     bean.setDatasource(getMaterialFiCardDataSource());
/* 451 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 452 */     invokeInitializingBean(bean);
/* 453 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.OutputAction getMaterialFiOutputAction() {
/* 457 */     if (context.get("materialFiOutputAction") != null)
/* 458 */       return (nc.ui.uif2.actions.OutputAction)context.get("materialFiOutputAction");
/* 459 */     nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
/* 460 */     context.put("materialFiOutputAction", bean);
/* 461 */     bean.setCode("FiOutput");
/* 462 */     bean.setModel(getMaterialFiAppModel());
/* 463 */     bean.setNodeKey("ficard");
/* 464 */     bean.setPrintDlgParentConatiner(getMaterialFiEditor());
/* 465 */     bean.setDatasource(getMaterialFiCardDataSource());
/* 466 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 467 */     invokeInitializingBean(bean);
/* 468 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialFiCardDataSource() {
/* 472 */     if (context.get("materialFiCardDataSource") != null)
/* 473 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialFiCardDataSource");
/* 474 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 475 */     context.put("materialFiCardDataSource", bean);
/* 476 */     bean.setModel(getMaterialFiAppModel());
/* 477 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 478 */     invokeInitializingBean(bean);
/* 479 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMaterialFiFirstLineAction() {
/* 483 */     if (context.get("materialFiFirstLineAction") != null)
/* 484 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materialFiFirstLineAction");
/* 485 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 486 */     context.put("materialFiFirstLineAction", bean);
/* 487 */     bean.setModel(getMaterialFiAppModel());
/* 488 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 489 */     invokeInitializingBean(bean);
/* 490 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialFiNextLineAction() {
/* 494 */     if (context.get("materialFiNextLineAction") != null)
/* 495 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialFiNextLineAction");
/* 496 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 497 */     context.put("materialFiNextLineAction", bean);
/* 498 */     bean.setModel(getMaterialFiAppModel());
/* 499 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 500 */     invokeInitializingBean(bean);
/* 501 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialFiPreLineAction() {
/* 505 */     if (context.get("materialFiPreLineAction") != null)
/* 506 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialFiPreLineAction");
/* 507 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 508 */     context.put("materialFiPreLineAction", bean);
/* 509 */     bean.setModel(getMaterialFiAppModel());
/* 510 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 511 */     invokeInitializingBean(bean);
/* 512 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialFiLastLineAction() {
/* 516 */     if (context.get("materialFiLastLineAction") != null)
/* 517 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialFiLastLineAction");
/* 518 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 519 */     context.put("materialFiLastLineAction", bean);
/* 520 */     bean.setModel(getMaterialFiAppModel());
/* 521 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 522 */     invokeInitializingBean(bean);
/* 523 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialFiClosingListener() {
/* 527 */     if (context.get("materialFiClosingListener") != null)
/* 528 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialFiClosingListener");
/* 529 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 530 */     context.put("materialFiClosingListener", bean);
/* 531 */     bean.setModel(getMaterialFiAppModel());
/* 532 */     bean.setSaveaction(getMaterialFiSaveAction());
/* 533 */     bean.setCancelaction(getMaterialFiCancelAction());
/* 534 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 535 */     invokeInitializingBean(bean);
/* 536 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialFiAppModel());
/* 641 */     bean.setEditor(getMaterialFiEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialFiAppModel());
/* 641 */     bean.setEditor(getMaterialFiEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }
