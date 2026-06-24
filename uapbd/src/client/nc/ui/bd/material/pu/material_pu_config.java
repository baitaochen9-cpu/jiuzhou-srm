/*     */ package nc.ui.bd.material.pu;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.ui.bd.material.config.action.ElectronicsignatureAction;
import nc.ui.bd.material.config.action.ElectronicsignatureHisAction;
/*     */ import nc.ui.uif2.I18nFB;
/*     */ import nc.ui.uif2.actions.TemplatePreviewAction;
/*     */ import nc.ui.uif2.actions.TemplatePrintAction;
/*     */ 
/*     */ public class material_pu_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*     */ {
/*  11 */   private Map<String, Object> context = new java.util.HashMap();
/*     */   
/*  13 */   public nc.ui.bd.uitabextend.DefaultUIExtComponent getMaterial_pu() { if (context.get("material_pu") != null)
/*  14 */       return (nc.ui.bd.uitabextend.DefaultUIExtComponent)context.get("material_pu");
/*  15 */     nc.ui.bd.uitabextend.DefaultUIExtComponent bean = new nc.ui.bd.uitabextend.DefaultUIExtComponent();
/*  16 */     context.put("material_pu", bean);
/*  17 */     bean.setActions(getManagedList0());
/*  18 */     bean.setExComponent(getMaterialPuListView());
/*  19 */     bean.setClosingListener(getMaterialPuClosingListener());
/*  20 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  21 */     invokeInitializingBean(bean);
/*  22 */     return bean;
/*     */   }
/*     */   
/*  25 */   private List getManagedList0() { List list = new java.util.ArrayList();list.add(getMaterialPuEditAction());list.add(getMaterialPuDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPuRefreshAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPuListPrintActionGroup());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialCosttidaiping1());return list;
/*     */   }
/*     */   
/*  28 */   public nc.ui.bd.material.pu.model.MaterialPuAppModelService getMaterialPuAppModelService() { if (context.get("materialPuAppModelService") != null)
/*  29 */       return (nc.ui.bd.material.pu.model.MaterialPuAppModelService)context.get("materialPuAppModelService");
/*  30 */     nc.ui.bd.material.pu.model.MaterialPuAppModelService bean = new nc.ui.bd.material.pu.model.MaterialPuAppModelService();
/*  31 */     context.put("materialPuAppModelService", bean);
/*  32 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  33 */     invokeInitializingBean(bean);
/*  34 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel getMaterialPuAppModel() {
/*  38 */     if (context.get("materialPuAppModel") != null)
/*  39 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel)context.get("materialPuAppModel");
/*  40 */     nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel bean = new nc.ui.bd.pub.orginfo.model.OrgInfoBillManageModel();
/*  41 */     context.put("materialPuAppModel", bean);
/*  42 */     bean.setContext((nc.vo.uif2.LoginContext)findBeanInUIF2BeanFactory("context"));
/*  43 */     bean.setService(getMaterialPuAppModelService());
/*  44 */     bean.setBusinessObjectAdapterFactory(getGeneralBDObjectAdapterFactory_1719a9f());
/*  45 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  46 */     invokeInitializingBean(bean);
/*  47 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.vo.bd.meta.GeneralBDObjectAdapterFactory getGeneralBDObjectAdapterFactory_1719a9f() {
/*  51 */     if (context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1719a9f") != null)
/*  52 */       return (nc.vo.bd.meta.GeneralBDObjectAdapterFactory)context.get("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1719a9f");
/*  53 */     nc.vo.bd.meta.GeneralBDObjectAdapterFactory bean = new nc.vo.bd.meta.GeneralBDObjectAdapterFactory();
/*  54 */     context.put("nc.vo.bd.meta.GeneralBDObjectAdapterFactory#1719a9f", bean);
/*  55 */     bean.setMode("VO");
/*  56 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  57 */     invokeInitializingBean(bean);
/*  58 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pu.model.MaterialPuAppModelDataManager getMaterialPuModelDataManager() {
/*  62 */     if (context.get("materialPuModelDataManager") != null)
/*  63 */       return (nc.ui.bd.material.pu.model.MaterialPuAppModelDataManager)context.get("materialPuModelDataManager");
/*  64 */     nc.ui.bd.material.pu.model.MaterialPuAppModelDataManager bean = new nc.ui.bd.material.pu.model.MaterialPuAppModelDataManager();
/*  65 */     context.put("materialPuModelDataManager", bean);
/*  66 */     bean.setModel(getMaterialPuAppModel());
/*  67 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  68 */     invokeInitializingBean(bean);
/*  69 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.orginfo.model.OrgInfoMediator getMaterialPuMediator() {
/*  73 */     if (context.get("materialPuMediator") != null)
/*  74 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoMediator)context.get("materialPuMediator");
/*  75 */     nc.ui.bd.pub.orginfo.model.OrgInfoMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoMediator();
/*  76 */     context.put("materialPuMediator", bean);
/*  77 */     bean.setBaseModel((nc.ui.uif2.model.AbstractUIAppModel)findBeanInUIF2BeanFactory("baseinfoModel"));
/*  78 */     bean.setModelDataManager(getMaterialPuModelDataManager());
/*  79 */     bean.setOrgInfoModel(getMaterialPuAppModel());
/*  80 */     bean.setOrgInfoPanel(getMaterialPuListView());
/*  81 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  82 */     invokeInitializingBean(bean);
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pu.view.MaterialPuListView getMaterialPuListView() {
/*  87 */     if (context.get("materialPuListView") != null)
/*  88 */       return (nc.ui.bd.material.pu.view.MaterialPuListView)context.get("materialPuListView");
/*  89 */     nc.ui.bd.material.pu.view.MaterialPuListView bean = new nc.ui.bd.material.pu.view.MaterialPuListView();
/*  90 */     context.put("materialPuListView", bean);
/*  91 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/*  92 */     bean.setModel(getMaterialPuAppModel());
/*  93 */     bean.setNodekey("pu");
/*  94 */     bean.setName(getI18nFB_15e7e16());
/*  95 */     bean.setUserdefitemListPreparator(getUserdefitemContainerListPreparator_123f942());
/*  96 */     bean.initUI();
/*  97 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  98 */     invokeInitializingBean(bean);
/*  99 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_15e7e16() {
/* 103 */     if (context.get("nc.ui.uif2.I18nFB#15e7e16") != null)
/* 104 */       return (String)context.get("nc.ui.uif2.I18nFB#15e7e16");
/* 105 */     I18nFB bean = new I18nFB();
/* 106 */     context.put("&nc.ui.uif2.I18nFB#15e7e16", bean);bean.setResDir("10140mag");
/* 107 */     bean.setDefaultValue("采购信息");
/* 108 */     bean.setResId("110140mag0018");
/* 109 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 110 */     invokeInitializingBean(bean);
/*     */     try {
/* 112 */       Object product = bean.getObject();
/* 113 */       context.put("nc.ui.uif2.I18nFB#15e7e16", product);
/* 114 */       return (String)product;
/*     */     } catch (Exception e) {
/* 116 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 119 */   private nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemContainerListPreparator_123f942() { if (context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#123f942") != null)
/* 120 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerListPreparator#123f942");
/* 121 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 122 */     context.put("nc.ui.uif2.editor.UserdefitemContainerListPreparator#123f942", bean);
/* 123 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 124 */     bean.setParams(getManagedList1());
/* 125 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 126 */     invokeInitializingBean(bean);
/* 127 */     return bean;
/*     */   }
/*     */   
/* 130 */   private List getManagedList1() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_a815a0());return list;
/*     */   }
/*     */   
/* 133 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_a815a0() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#a815a0") != null)
/* 134 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#a815a0");
/* 135 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 136 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#a815a0", bean);
/* 137 */     bean.setMdfullname("uap.materialpu");
/* 138 */     bean.setPos(0);
/* 139 */     bean.setPrefix("def");
/* 140 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 141 */     invokeInitializingBean(bean);
/* 142 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.material.pu.view.MaterialPuEditor getMaterialPuEditor() {
/* 146 */     if (context.get("materialPuEditor") != null)
/* 147 */       return (nc.ui.bd.material.pu.view.MaterialPuEditor)context.get("materialPuEditor");
/* 148 */     nc.ui.bd.material.pu.view.MaterialPuEditor bean = new nc.ui.bd.material.pu.view.MaterialPuEditor();
/* 149 */     context.put("materialPuEditor", bean);
/* 150 */     bean.setModel(getMaterialPuAppModel());
/* 151 */     bean.setTemplateContainer((nc.ui.uif2.editor.TemplateContainer)findBeanInUIF2BeanFactory("templateContainer"));
/* 152 */     bean.setNodekey("pu");
/* 153 */     bean.setUserdefitemPreparator(getUserdefitemContainerPreparator_18d0816());
/* 154 */     bean.setActions(getManagedList3());
/* 155 */     bean.initUI();
/* 156 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 157 */     invokeInitializingBean(bean);
/* 158 */     return bean;
/*     */   }
/*     */   
/*     */   private nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemContainerPreparator_18d0816() {
/* 162 */     if (context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#18d0816") != null)
/* 163 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("nc.ui.uif2.editor.UserdefitemContainerPreparator#18d0816");
/* 164 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/* 165 */     context.put("nc.ui.uif2.editor.UserdefitemContainerPreparator#18d0816", bean);
/* 166 */     bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer)findBeanInUIF2BeanFactory("userdefitemContainer"));
/* 167 */     bean.setParams(getManagedList2());
/* 168 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 169 */     invokeInitializingBean(bean);
/* 170 */     return bean;
/*     */   }
/*     */   
/* 173 */   private List getManagedList2() { List list = new java.util.ArrayList();list.add(getUserdefQueryParam_c18ee7());return list;
/*     */   }
/*     */   
/* 176 */   private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_c18ee7() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#c18ee7") != null)
/* 177 */       return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#c18ee7");
/* 178 */     nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
/* 179 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#c18ee7", bean);
/* 180 */     bean.setMdfullname("uap.materialpu");
/* 181 */     bean.setPos(0);
/* 182 */     bean.setPrefix("def");
/* 183 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 184 */     invokeInitializingBean(bean);
/* 185 */     return bean;
/*     */   }
/*     */   
/* 188 */   private List getManagedList3() { List list = new java.util.ArrayList();list.add(getMaterialPuFirstLineAction());list.add(getMaterialPuPreLineAction());list.add(getMaterialPuNextLineAction());list.add(getMaterialPuLastLineAction());return list;
/*     */   }
/*     */   
/* 191 */   public nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator getMaterialPuDialogMediator() { if (context.get("materialPuDialogMediator") != null)
/* 192 */       return (nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator)context.get("materialPuDialogMediator");
/* 193 */     nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator bean = new nc.ui.bd.pub.orginfo.model.OrgInfoCardDialogMediator();
/* 194 */     context.put("materialPuDialogMediator", bean);
/* 195 */     bean.setModel(getMaterialPuAppModel());
/* 196 */     bean.setDialogContainer(getMaterialPuDialogContainer());
/* 197 */     bean.setName(getI18nFB_ddf32());
/* 198 */     bean.setClosingListener(getMaterialPuClosingListener());
/* 199 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 200 */     invokeInitializingBean(bean);
/* 201 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_ddf32() {
/* 205 */     if (context.get("nc.ui.uif2.I18nFB#ddf32") != null)
/* 206 */       return (String)context.get("nc.ui.uif2.I18nFB#ddf32");
/* 207 */     I18nFB bean = new I18nFB();
/* 208 */     context.put("&nc.ui.uif2.I18nFB#ddf32", bean);bean.setResDir("10140mag");
/* 209 */     bean.setDefaultValue("采购信息");
/* 210 */     bean.setResId("110140mag0018");
/* 211 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 212 */     invokeInitializingBean(bean);
/*     */     try {
/* 214 */       Object product = bean.getObject();
/* 215 */       context.put("nc.ui.uif2.I18nFB#ddf32", product);
/* 216 */       return (String)product;
/*     */     } catch (Exception e) {
/* 218 */       throw new RuntimeException(e);
/*     */     } }
/*     */   
/* 221 */   public nc.ui.uif2.TangramContainer getMaterialPuDialogContainer() { if (context.get("materialPuDialogContainer") != null)
/* 222 */       return (nc.ui.uif2.TangramContainer)context.get("materialPuDialogContainer");
/* 223 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/* 224 */     context.put("materialPuDialogContainer", bean);
/* 225 */     bean.setConstraints(getManagedList4());
/* 226 */     bean.setActions(getManagedList5());
/* 227 */     bean.setEditActions(getManagedList6());
/* 228 */     bean.setModel(getMaterialPuAppModel());
/* 229 */     bean.initUI();
/* 230 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 231 */     invokeInitializingBean(bean);
/* 232 */     return bean;
/*     */   }
/*     */   
/* 235 */   private List getManagedList4() { List list = new java.util.ArrayList();list.add(getTangramLayoutConstraint_1e65fd7());return list;
/*     */   }
/*     */   
/* 238 */   private nc.ui.uif2.tangramlayout.TangramLayoutConstraint getTangramLayoutConstraint_1e65fd7() { if (context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#1e65fd7") != null)
/* 239 */       return (nc.ui.uif2.tangramlayout.TangramLayoutConstraint)context.get("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#1e65fd7");
/* 240 */     nc.ui.uif2.tangramlayout.TangramLayoutConstraint bean = new nc.ui.uif2.tangramlayout.TangramLayoutConstraint();
/* 241 */     context.put("nc.ui.uif2.tangramlayout.TangramLayoutConstraint#1e65fd7", bean);
/* 242 */     bean.setNewComponent(getMaterialPuEditor());
/* 243 */     bean.setNewComponentName(getI18nFB_b88410());
/* 244 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 245 */     invokeInitializingBean(bean);
/* 246 */     return bean;
/*     */   }
/*     */   
/*     */   private String getI18nFB_b88410() {
/* 250 */     if (context.get("nc.ui.uif2.I18nFB#b88410") != null)
/* 251 */       return (String)context.get("nc.ui.uif2.I18nFB#b88410");
/* 252 */     I18nFB bean = new I18nFB();
/* 253 */     context.put("&nc.ui.uif2.I18nFB#b88410", bean);bean.setResDir("10140mag");
/* 254 */     bean.setDefaultValue("采购信息");
/* 255 */     bean.setResId("110140mag0018");
/* 256 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 257 */     invokeInitializingBean(bean);
/*     */     try {
/* 259 */       Object product = bean.getObject();
/* 260 */       context.put("nc.ui.uif2.I18nFB#b88410", product);
/* 261 */       return (String)product;
/*     */     } catch (Exception e) {
/* 263 */       throw new RuntimeException(e); } }
/*     */   
/* 265 */   private List getManagedList5() { List list = new java.util.ArrayList();list.add(getMaterialPuEditAction());list.add(getMaterialPuDeleteAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPuRefreshSingleAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPuPrintActionGroup());return list; }
/*     */   
/* 267 */   private List getManagedList6() { List list = new java.util.ArrayList();list.add(getMaterialPuSaveAction());list.add((javax.swing.Action)findBeanInUIF2BeanFactory("separatorAction"));list.add(getMaterialPuCancelAction());return list;
/*     */   }
/*     */   
/* 270 */   public nc.ui.uif2.actions.EditAction getMaterialPuEditAction() { if (context.get("materialPuEditAction") != null)
/* 271 */       return (nc.ui.uif2.actions.EditAction)context.get("materialPuEditAction");
/* 272 */     nc.ui.uif2.actions.EditAction bean = new nc.ui.uif2.actions.EditAction();
/* 273 */     context.put("materialPuEditAction", bean);
/* 274 */     bean.setCode("PuEdit");
/* 275 */     bean.setModel(getMaterialPuAppModel());
/* 276 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 277 */     invokeInitializingBean(bean);
/* 278 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.DeleteAction getMaterialPuDeleteAction() {
/* 282 */     if (context.get("materialPuDeleteAction") != null)
/* 283 */       return (nc.ui.uif2.actions.DeleteAction)context.get("materialPuDeleteAction");
/* 284 */     nc.ui.uif2.actions.DeleteAction bean = new nc.ui.uif2.actions.DeleteAction();
/* 285 */     context.put("materialPuDeleteAction", bean);
/* 286 */     bean.setCode("PuDelete");
/* 287 */     bean.setModel(getMaterialPuAppModel());
/* 288 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 289 */     invokeInitializingBean(bean);
/* 290 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.SaveAction getMaterialPuSaveAction() {
/* 294 */     if (context.get("materialPuSaveAction") != null)
/* 295 */       return (nc.ui.uif2.actions.SaveAction)context.get("materialPuSaveAction");
/* 296 */     nc.ui.uif2.actions.SaveAction bean = new nc.ui.uif2.actions.SaveAction();
/* 297 */     context.put("materialPuSaveAction", bean);
/* 298 */     bean.setCode("PuSave");
/* 299 */     bean.setModel(getMaterialPuAppModel());
/* 300 */     bean.setEditor(getMaterialPuEditor());
/* 301 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 302 */     invokeInitializingBean(bean);
/* 303 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.CancelAction getMaterialPuCancelAction() {
/* 307 */     if (context.get("materialPuCancelAction") != null)
/* 308 */       return (nc.ui.uif2.actions.CancelAction)context.get("materialPuCancelAction");
/* 309 */     nc.ui.uif2.actions.CancelAction bean = new nc.ui.uif2.actions.CancelAction();
/* 310 */     context.put("materialPuCancelAction", bean);
/* 311 */     bean.setCode("PuCancel");
/* 312 */     bean.setModel(getMaterialPuAppModel());
/* 313 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 314 */     invokeInitializingBean(bean);
/* 315 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshSingleAction getMaterialPuRefreshSingleAction() {
/* 319 */     if (context.get("materialPuRefreshSingleAction") != null)
/* 320 */       return (nc.ui.uif2.actions.RefreshSingleAction)context.get("materialPuRefreshSingleAction");
/* 321 */     nc.ui.uif2.actions.RefreshSingleAction bean = new nc.ui.uif2.actions.RefreshSingleAction();
/* 322 */     context.put("materialPuRefreshSingleAction", bean);
/* 323 */     bean.setCode("PuRefresh");
/* 324 */     bean.setModel(getMaterialPuAppModel());
/* 325 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 326 */     invokeInitializingBean(bean);
/* 327 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.RefreshAllAction getMaterialPuRefreshAction() {
/* 331 */     if (context.get("materialPuRefreshAction") != null)
/* 332 */       return (nc.ui.uif2.actions.RefreshAllAction)context.get("materialPuRefreshAction");
/* 333 */     nc.ui.uif2.actions.RefreshAllAction bean = new nc.ui.uif2.actions.RefreshAllAction();
/* 334 */     context.put("materialPuRefreshAction", bean);
/* 335 */     bean.setCode("PuRefresh");
/* 336 */     bean.setModel(getMaterialPuAppModel());
/* 337 */     bean.setManager(getMaterialPuModelDataManager());
/* 338 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 339 */     invokeInitializingBean(bean);
/* 340 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialPuListPrintActionGroup() {
/* 344 */     if (context.get("materialPuListPrintActionGroup") != null)
/* 345 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialPuListPrintActionGroup");
/* 346 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 347 */     context.put("materialPuListPrintActionGroup", bean);
/* 348 */     bean.setCode("PuPrintMenu");
/* 349 */     bean.setActions(getManagedList7());
/* 350 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 351 */     invokeInitializingBean(bean);
/* 352 */     return bean;
/*     */   }
/*     */   
/* 355 */   private List getManagedList7() { List list = new java.util.ArrayList();list.add(getMaterialPuListTempletPrintAction());list.add(getMaterialPuListTempletPreviewAction());list.add(getMaterialPuListOutputAction());return list;
/*     */   }
/*     */   
/* 358 */   public TemplatePreviewAction getMaterialPuListTempletPreviewAction() { if (context.get("materialPuListTempletPreviewAction") != null)
/* 359 */       return (TemplatePreviewAction)context.get("materialPuListTempletPreviewAction");
/* 360 */     TemplatePreviewAction bean = new TemplatePreviewAction();
/* 361 */     context.put("materialPuListTempletPreviewAction", bean);
/* 362 */     bean.setCode("PuTempPreview");
/* 363 */     bean.setModel(getMaterialPuAppModel());
/* 364 */     bean.setNodeKey("pulist");
/* 365 */     bean.setPrintDlgParentConatiner(getMaterialPuListView());
/* 366 */     bean.setDatasource(getMaterialPuListardDataSource());
/* 367 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 368 */     invokeInitializingBean(bean);
/* 369 */     return bean;
/*     */   }
/*     */   
/*     */   public TemplatePrintAction getMaterialPuListTempletPrintAction() {
/* 373 */     if (context.get("materialPuListTempletPrintAction") != null)
/* 374 */       return (TemplatePrintAction)context.get("materialPuListTempletPrintAction");
/* 375 */     TemplatePrintAction bean = new TemplatePrintAction();
/* 376 */     context.put("materialPuListTempletPrintAction", bean);
/* 377 */     bean.setCode("PuTempPrint");
/* 378 */     bean.setModel(getMaterialPuAppModel());
/* 379 */     bean.setNodeKey("pulist");
/* 380 */     bean.setPrintDlgParentConatiner(getMaterialPuListView());
/* 381 */     bean.setDatasource(getMaterialPuListardDataSource());
/* 382 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 383 */     invokeInitializingBean(bean);
/* 384 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.OutputAction getMaterialPuListOutputAction() {
/* 388 */     if (context.get("materialPuListOutputAction") != null)
/* 389 */       return (nc.ui.uif2.actions.OutputAction)context.get("materialPuListOutputAction");
/* 390 */     nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
/* 391 */     context.put("materialPuListOutputAction", bean);
/* 392 */     bean.setCode("PuOutput");
/* 393 */     bean.setModel(getMaterialPuAppModel());
/* 394 */     bean.setNodeKey("pulist");
/* 395 */     bean.setPrintDlgParentConatiner(getMaterialPuListView());
/* 396 */     bean.setDatasource(getMaterialPuListardDataSource());
/* 397 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 398 */     invokeInitializingBean(bean);
/* 399 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataAllDatasSource getMaterialPuListardDataSource() {
/* 403 */     if (context.get("materialPuListardDataSource") != null)
/* 404 */       return (nc.ui.bd.pub.actions.print.MetaDataAllDatasSource)context.get("materialPuListardDataSource");
/* 405 */     nc.ui.bd.pub.actions.print.MetaDataAllDatasSource bean = new nc.ui.bd.pub.actions.print.MetaDataAllDatasSource();
/* 406 */     context.put("materialPuListardDataSource", bean);
/* 407 */     bean.setModel(getMaterialPuAppModel());
/* 408 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 409 */     invokeInitializingBean(bean);
/* 410 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.funcnode.ui.action.GroupAction getMaterialPuPrintActionGroup() {
/* 414 */     if (context.get("materialPuPrintActionGroup") != null)
/* 415 */       return (nc.funcnode.ui.action.GroupAction)context.get("materialPuPrintActionGroup");
/* 416 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 417 */     context.put("materialPuPrintActionGroup", bean);
/* 418 */     bean.setCode("PuPrintMenu");
/* 419 */     bean.setActions(getManagedList8());
/* 420 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 421 */     invokeInitializingBean(bean);
/* 422 */     return bean;
/*     */   }
/*     */   
/* 425 */   private List getManagedList8() { List list = new java.util.ArrayList();list.add(getMaterialPuTempletPrintAction());list.add(getMaterialPuTempletPreviewAction());list.add(getMaterialPuOutputAction());return list;
/*     */   }
/*     */   
/* 428 */   public TemplatePreviewAction getMaterialPuTempletPreviewAction() { if (context.get("materialPuTempletPreviewAction") != null)
/* 429 */       return (TemplatePreviewAction)context.get("materialPuTempletPreviewAction");
/* 430 */     TemplatePreviewAction bean = new TemplatePreviewAction();
/* 431 */     context.put("materialPuTempletPreviewAction", bean);
/* 432 */     bean.setCode("PuTempPreview");
/* 433 */     bean.setModel(getMaterialPuAppModel());
/* 434 */     bean.setNodeKey("pucard");
/* 435 */     bean.setPrintDlgParentConatiner(getMaterialPuEditor());
/* 436 */     bean.setDatasource(getMaterialPuCardDataSource());
/* 437 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 438 */     invokeInitializingBean(bean);
/* 439 */     return bean;
/*     */   }
/*     */   
/*     */   public TemplatePrintAction getMaterialPuTempletPrintAction() {
/* 443 */     if (context.get("materialPuTempletPrintAction") != null)
/* 444 */       return (TemplatePrintAction)context.get("materialPuTempletPrintAction");
/* 445 */     TemplatePrintAction bean = new TemplatePrintAction();
/* 446 */     context.put("materialPuTempletPrintAction", bean);
/* 447 */     bean.setCode("PuTempPrint");
/* 448 */     bean.setModel(getMaterialPuAppModel());
/* 449 */     bean.setNodeKey("pucard");
/* 450 */     bean.setPrintDlgParentConatiner(getMaterialPuEditor());
/* 451 */     bean.setDatasource(getMaterialPuCardDataSource());
/* 452 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 453 */     invokeInitializingBean(bean);
/* 454 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.OutputAction getMaterialPuOutputAction() {
/* 458 */     if (context.get("materialPuOutputAction") != null)
/* 459 */       return (nc.ui.uif2.actions.OutputAction)context.get("materialPuOutputAction");
/* 460 */     nc.ui.uif2.actions.OutputAction bean = new nc.ui.uif2.actions.OutputAction();
/* 461 */     context.put("materialPuOutputAction", bean);
/* 462 */     bean.setCode("PuOutput");
/* 463 */     bean.setModel(getMaterialPuAppModel());
/* 464 */     bean.setNodeKey("pucard");
/* 465 */     bean.setPrintDlgParentConatiner(getMaterialPuEditor());
/* 466 */     bean.setDatasource(getMaterialPuCardDataSource());
/* 467 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 468 */     invokeInitializingBean(bean);
/* 469 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource getMaterialPuCardDataSource() {
/* 473 */     if (context.get("materialPuCardDataSource") != null)
/* 474 */       return (nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource)context.get("materialPuCardDataSource");
/* 475 */     nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource bean = new nc.ui.bd.pub.actions.print.MetaDataSingleSelectDataSource();
/* 476 */     context.put("materialPuCardDataSource", bean);
/* 477 */     bean.setModel(getMaterialPuAppModel());
/* 478 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 479 */     invokeInitializingBean(bean);
/* 480 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.FirstLineAction getMaterialPuFirstLineAction() {
/* 484 */     if (context.get("materialPuFirstLineAction") != null)
/* 485 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("materialPuFirstLineAction");
/* 486 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 487 */     context.put("materialPuFirstLineAction", bean);
/* 488 */     bean.setModel(getMaterialPuAppModel());
/* 489 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 490 */     invokeInitializingBean(bean);
/* 491 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.NextLineAction getMaterialPuNextLineAction() {
/* 495 */     if (context.get("materialPuNextLineAction") != null)
/* 496 */       return (nc.ui.uif2.actions.NextLineAction)context.get("materialPuNextLineAction");
/* 497 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 498 */     context.put("materialPuNextLineAction", bean);
/* 499 */     bean.setModel(getMaterialPuAppModel());
/* 500 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 501 */     invokeInitializingBean(bean);
/* 502 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.PreLineAction getMaterialPuPreLineAction() {
/* 506 */     if (context.get("materialPuPreLineAction") != null)
/* 507 */       return (nc.ui.uif2.actions.PreLineAction)context.get("materialPuPreLineAction");
/* 508 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 509 */     context.put("materialPuPreLineAction", bean);
/* 510 */     bean.setModel(getMaterialPuAppModel());
/* 511 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 512 */     invokeInitializingBean(bean);
/* 513 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.actions.LastLineAction getMaterialPuLastLineAction() {
/* 517 */     if (context.get("materialPuLastLineAction") != null)
/* 518 */       return (nc.ui.uif2.actions.LastLineAction)context.get("materialPuLastLineAction");
/* 519 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 520 */     context.put("materialPuLastLineAction", bean);
/* 521 */     bean.setModel(getMaterialPuAppModel());
/* 522 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 523 */     invokeInitializingBean(bean);
/* 524 */     return bean;
/*     */   }
/*     */   
/*     */   public nc.ui.uif2.FunNodeClosingHandler getMaterialPuClosingListener() {
/* 528 */     if (context.get("materialPuClosingListener") != null)
/* 529 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("materialPuClosingListener");
/* 530 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 531 */     context.put("materialPuClosingListener", bean);
/* 532 */     bean.setModel(getMaterialPuAppModel());
/* 533 */     bean.setSaveaction(getMaterialPuSaveAction());
/* 534 */     bean.setCancelaction(getMaterialPuCancelAction());
/* 535 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 536 */     invokeInitializingBean(bean);
/* 537 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureAction getMaterialCosttidaiping() {
/* 636 */     if (context.get("materialCosttidaiping") != null)
/* 637 */       return (ElectronicsignatureAction)context.get("materialCosttidaiping");
/* 638 */     ElectronicsignatureAction bean = new ElectronicsignatureAction();
/* 639 */     context.put("materialCosttidaiping", bean);
/* 640 */     bean.setModel(getMaterialPuAppModel());
/* 641 */     bean.setEditor(getMaterialPuEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */   public ElectronicsignatureHisAction getMaterialCosttidaiping1() {
/* 636 */     if (context.get("materialCosttidaiping1") != null)
/* 637 */       return (ElectronicsignatureHisAction)context.get("materialCosttidaiping1");
/* 638 */     ElectronicsignatureHisAction bean = new ElectronicsignatureHisAction();
/* 639 */     context.put("materialCosttidaiping1", bean);
/* 640 */     bean.setModel(getMaterialPuAppModel());
/* 641 */     bean.setEditor(getMaterialPuEditor());
/* 643 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 644 */     invokeInitializingBean(bean);
/* 645 */     return bean;
/*     */   }
/*     */ }