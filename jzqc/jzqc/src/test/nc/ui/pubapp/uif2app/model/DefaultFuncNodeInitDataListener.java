/*     */ package nc.ui.pubapp.uif2app.model;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import nc.bs.logging.Logger;
/*     */ import nc.desktop.quickstart.IQSBillService;
/*     */ import nc.desktop.quickstart.QSContext;
/*     */ import nc.funcnode.ui.FuncletInitData;
/*     */ import nc.ui.ml.NCLangRes;
/*     */ import nc.ui.pub.beans.MessageDialog;
/*     */ import nc.ui.pub.linkoperate.ILinkApproveData;
/*     */ import nc.ui.pub.linkoperate.ILinkMaintainData;
/*     */ import nc.ui.pub.linkoperate.ILinkQueryData;
/*     */ import nc.ui.pubapp.billgraph.RowLinkQueryUtil;
/*     */ import nc.ui.pubapp.uif2app.PubExceptionHanler;
/*     */ import nc.ui.pubapp.uif2app.model.pagination.PaginationModelDataManager;
/*     */ import nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction;
/*     */ import nc.ui.uif2.IExceptionHandler;
/*     */ import nc.ui.uif2.IFuncNodeInitDataListener;
/*     */ import nc.ui.uif2.UIState;
/*     */ import nc.ui.uif2.components.IAutoShowUpComponent;
/*     */ import nc.ui.uif2.editor.BillForm;
/*     */ import nc.ui.uif2.model.AbstractUIAppModel;
/*     */ import nc.vo.ml.AbstractNCLangRes;
/*     */ import nc.vo.ml.NCLangRes4VoTransl;
/*     */ import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/*     */ import nc.vo.querytemplate.queryscheme.SimpleQuerySchemeVO;
/*     */ import nc.vo.trade.billsource.LightBillVO;
/*     */ import nc.vo.uif2.LoginContext;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultFuncNodeInitDataListener
/*     */   implements IFuncNodeInitDataListener
/*     */ {
/*     */   private boolean multiLinkQueryEnable;
/*     */   private PaginationModelDataManager pmodelDataManager;
/*     */   private IQSBillService qsservice;
/*     */   private IAutoShowUpComponent autoShowUpComponent;
/*     */   private IAutoShowUpComponent autoShowUpListComponent;
/*     */   private LoginContext context;
/*     */   private IExceptionHandler exceptionHandler;
/*     */   private AbstractUIAppModel model;
/*     */   private Map<Integer, IInitDataProcessor> processorMap;
/*     */   private DefaultQueryAction queryAction;
/*     */   private IQueryService service;
/*     */   private String voClassName;
/*     */   
/*     */   public PaginationModelDataManager getPmodelDataManager()
/*     */   {
/*  62 */     return pmodelDataManager;
/*     */   }
/*     */   
/*     */   public void setPmodelDataManager(PaginationModelDataManager pmodelDataManager)
/*     */   {
/*  67 */     this.pmodelDataManager = pmodelDataManager;
/*     */   }
/*     */   
/*     */   public boolean isMultiLinkQueryEnable() {
/*  71 */     return multiLinkQueryEnable;
/*     */   }
/*     */   
/*     */   public void setMultiLinkQueryEnable(boolean multiLinkQueryEnable) {
/*  75 */     this.multiLinkQueryEnable = multiLinkQueryEnable;
/*     */   }
/*     */   
/*     */   public DefaultFuncNodeInitDataListener()
/*     */   {
/*  57 */     multiLinkQueryEnable = false;
/*     */     
/*  59 */     pmodelDataManager = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */     qsservice = null;
/*     */   }
/*     */   
/*  81 */   public IQSBillService getQsservice() { return qsservice; }
/*     */   
/*     */   public void setQsservice(IQSBillService qsservice)
/*     */   {
/*  85 */     this.qsservice = qsservice;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class DefaultInitDataProcessor
/*     */     implements DefaultFuncNodeInitDataListener.IInitDataProcessor
/*     */   {
/*     */     public DefaultInitDataProcessor() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void process(FuncletInitData data)
/*     */     {
/* 122 */       if ((data.getInitData() instanceof SimpleQuerySchemeVO)) {
/* 123 */         doInitDataByQueryScheme(data);
/*     */         
/* 125 */         return;
/*     */       }
/*     */       
/* 128 */       if ((data.getInitData() instanceof QSContext)) {
/* 129 */         DefaultFuncNodeInitDataListener.this.doQSOpenInit(data.getInitData());
/* 130 */         return;
/*     */       }
/*     */       
/* 133 */       switch (data.getInitType())
/*     */       {
/*     */       case 0: 
/* 136 */         doLinkAdd(data);
/* 137 */         return;
/*     */       case 1: 
/* 139 */         doLinkApprove((ILinkApproveData)data.getInitData());
/* 140 */         break;
/*     */       case 2: 
/* 142 */         doLinkMaintain((ILinkMaintainData)data.getInitData());
/* 143 */         break;
/*     */       case 3: 
/* 145 */         if (isMultiLinkQueryEnable()) {
/* 146 */           doLinkQuerys(data.getInitData());
/*     */         } else
/* 148 */           doLinkQuery((ILinkQueryData)data.getInitData());
/* 149 */         break;
/*     */       case -100: 
/* 151 */         doBussinessLog((String)data.getInitData());
/* 152 */         break;
/*     */       }
/*     */       
/*     */       
/* 156 */       if (null != getAutoShowUpComponent())
/*     */       {
/* 158 */         IAutoShowUpComponent autoShowUpComponent = null;
/*     */         
/*     */ 
/* 161 */         Object inData = data.getInitData();
/* 162 */         if ((inData.getClass().isArray()) && (isMultiLinkQueryEnable()) && (null != getAutoShowUpListComponent()))
/*     */         {
/*     */ 
/* 165 */           autoShowUpComponent = getAutoShowUpListComponent();
/*     */         } else {
/* 167 */           autoShowUpComponent = getAutoShowUpComponent();
/*     */         }
/* 169 */         autoShowUpComponent.showMeUp();
/*     */         
/* 171 */         if (data.getInitType() == 3) {
/* 172 */           highLightSelectedRow(data, autoShowUpComponent);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void highLightSelectedRow(FuncletInitData data, IAutoShowUpComponent autoShowUpComponent)
/*     */     {
/* 179 */       BillForm billform = null;
/* 180 */       if ((autoShowUpComponent instanceof BillForm)) {
/* 181 */         billform = (BillForm)autoShowUpComponent;
/*     */       }
/*     */       
/* 184 */       Object inData = data.getInitData();
/* 185 */       int[] rows = null;
/* 186 */       Object obj = null;
/* 187 */       if (inData.getClass().isArray()) {
/* 188 */         ILinkQueryData[] linkQueryDatas = (ILinkQueryData[])inData;
/* 189 */         obj = linkQueryDatas[0].getUserObject();
/*     */       } else {
/* 191 */         obj = ((ILinkQueryData)inData).getUserObject();
/*     */       }
/*     */       
/* 194 */       LightBillVO vo = null;
/* 195 */       if ((obj instanceof LightBillVO)) {
/* 196 */         vo = (LightBillVO)obj;
/* 197 */         if (vo.getRowSet() != null) {
/* 198 */           Set<String> rowNoSet = vo.getRowSet();
/* 199 */           for (String row : rowNoSet) {
/* 200 */             int rowValue = RowLinkQueryUtil.getRowNoByKey(row, billform);
/* 201 */             if (rowValue != -1)
/* 202 */               rows = ArrayUtils.add(rows, rowValue);
/*     */           }
/* 204 */           if ((rows == null) || (billform == null)) {}
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     protected void doBussinessLog(String billPk)
/*     */     {
/* 212 */       Object obj = getService().queryByPk(billPk);
/*     */       
/* 214 */       getModel().initModel(obj);
/*     */     }
/*     */     
/*     */     protected void doLinkAdd(FuncletInitData data) {
/* 218 */       getModel().initModel(null);
/*     */     }
/*     */     
/*     */     protected void doLinkApprove(ILinkApproveData approveData) {
/* 222 */       if (getPmodelDataManager() != null) {
/* 223 */         getPmodelDataManager().initModelByPks(new String[] { approveData.getBillID() });
/*     */       }
/*     */       else {
/* 226 */         Object obj = getService().queryByPk(approveData.getBillID());
/*     */         
/* 228 */         getModel().initModel(obj);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void doLinkMaintain(ILinkMaintainData initData) {
/* 233 */       if (getPmodelDataManager() != null) {
/* 234 */         getPmodelDataManager().initModelByPks(new String[] { initData.getBillID() });
/*     */       }
/*     */       else {
/* 237 */         Object obj = getService().queryByPk(initData.getBillID());
/*     */         
/* 239 */         getModel().initModel(obj);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void doLinkQuerys(Object iLinkQueryData) {
/* 244 */       ILinkQueryData[] datas = null;
/* 245 */       if (iLinkQueryData.getClass().isArray()) {
/* 246 */         datas = (ILinkQueryData[])iLinkQueryData;
/*     */       } else {
/* 248 */         datas = new ILinkQueryData[] { (ILinkQueryData)iLinkQueryData };
/*     */       }
/*     */       
/* 251 */       if ((!(getService() instanceof DefaultFuncNodeInitDataListener.IQueryServiceWithFuncCodeExt)) && (!(getService() instanceof DefaultFuncNodeInitDataListener.IQueryServiceExt)))
/*     */       {
/* 253 */         throw new IllegalStateException(NCLangRes.getInstance().getStrByID("pubapp_0", "DefaultFuncNodeInitDataListener-0000"));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 260 */       String[] pks = new String[datas.length];
/* 261 */       for (int i = 0; i < pks.length; i++)
/* 262 */         pks[i] = datas[i].getBillID();
/* 263 */       Object[] objs = null;
/* 264 */       if ((getService() instanceof DefaultFuncNodeInitDataListener.IQueryServiceWithFuncCodeExt)) {
/* 265 */         objs = ((DefaultFuncNodeInitDataListener.IQueryServiceWithFuncCodeExt)getService()).queryByPksWithFunCode(pks, getContext().getNodeCode());
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 270 */         objs = ((DefaultFuncNodeInitDataListener.IQueryServiceExt)getService()).queryByPk(pks); }
/* 271 */       if (objs == null) {
/* 272 */         MessageDialog.showErrorDlg(getModel().getContext().getEntranceUI(), null, NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0337"));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 282 */       if (getPmodelDataManager() != null) {
/* 283 */         getPmodelDataManager().initModelByPks(objs == null ? new String[0] : pks);
/*     */       }
/*     */       else {
/* 286 */         getModel().initModel(objs);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void doLinkQuery(ILinkQueryData iLinkQueryData) {
/* 291 */       Object obj = null;
/* 292 */       if (((getService() instanceof DefaultFuncNodeInitDataListener.IQueryServiceWithFunCode)) && (getContext() != null))
/*     */       {
/* 294 */         DefaultFuncNodeInitDataListener.IQueryServiceWithFunCode serv = (DefaultFuncNodeInitDataListener.IQueryServiceWithFunCode)getService();
/*     */         
/* 296 */         obj = serv.queryByPkWithFunCode(iLinkQueryData.getBillID(), getContext().getNodeCode());
/*     */       }
/*     */       else
/*     */       {
/* 300 */         obj = getService().queryByPk(iLinkQueryData.getBillID());
/*     */       }
/*     */       
/* 303 */       if (null == obj) {
/* 304 */         MessageDialog.showErrorDlg(getModel().getContext().getEntranceUI(), null, NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0337"));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 315 */       if (getPmodelDataManager() != null) {
/* 316 */         getPmodelDataManager().initModelByPks(obj != null ? new String[] { iLinkQueryData.getBillID() } : new String[0]);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 321 */         getModel().initModel(obj);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IAutoShowUpComponent getAutoShowUpComponent()
/*     */   {
/* 345 */     return autoShowUpComponent;
/*     */   }
/*     */   
/*     */   public IAutoShowUpComponent getAutoShowUpListComponent() {
/* 349 */     return autoShowUpListComponent;
/*     */   }
/*     */   
/*     */   public LoginContext getContext() {
/* 353 */     if ((context == null) && 
/* 354 */       (getModel() != null)) {
/* 355 */       return getModel().getContext();
/*     */     }
/*     */     
/* 358 */     return context;
/*     */   }
/*     */   
/*     */   public AbstractUIAppModel getModel() {
/* 362 */     return model;
/*     */   }
/*     */   
/*     */   public Map<Integer, IInitDataProcessor> getProcessorMap() {
/* 366 */     return processorMap;
/*     */   }
/*     */   
/*     */   public DefaultQueryAction getQueryAction() {
/* 370 */     return queryAction;
/*     */   }
/*     */   
/*     */   public IQueryService getService() {
/* 374 */     if (null == service) {
/* 375 */       service = new SimpleQueryByPk(this);
/*     */     }
/* 377 */     return service;
/*     */   }
/*     */   
/*     */   public String getVoClassName() {
/* 381 */     return voClassName;
/*     */   }
/*     */   
/*     */ 
/*     */   public void initData(FuncletInitData data)
/*     */   {
/* 387 */     if ((UIState.EDIT.equals(getModel().getUiState())) || (UIState.ADD.equals(getModel().getUiState())))
/*     */     {
/* 389 */       return;
/*     */     }
/*     */     
/* 392 */     if (null == data) {
/* 393 */       getModel().initModel(null);
/* 394 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 402 */     IInitDataProcessor processor = null;
/* 403 */     if (null != getProcessorMap()) {
/* 404 */       processor = (IInitDataProcessor)getProcessorMap().get(Integer.valueOf(data.getInitType()));
/*     */     }
/*     */     
/* 407 */     if (null == processor) {
/* 408 */       processor = getDefaultProcessor();
/*     */     }
/*     */     try {
/* 411 */       processor.process(data);
/*     */     } catch (Exception e) {
/* 413 */       getExceptionHandler(true).handlerExeption(e);
/*     */       
/* 415 */       ExceptionUtils.wrappException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAutoShowUpComponent(IAutoShowUpComponent autoShowUpComponent) {
/* 420 */     this.autoShowUpComponent = autoShowUpComponent;
/*     */   }
/*     */   
/*     */   public void setAutoShowUpListComponent(IAutoShowUpComponent autoShowUpListComponent) {
/* 424 */     this.autoShowUpListComponent = autoShowUpListComponent;
/*     */   }
/*     */   
/*     */   public void setContext(LoginContext context) {
/* 428 */     this.context = context;
/*     */   }
/*     */   
/*     */   public void setModel(AbstractUIAppModel model) {
/* 432 */     this.model = model;
/*     */   }
/*     */   
/*     */   public void setProcessorMap(Map<String, IInitDataProcessor> processorStringMap)
/*     */   {
/* 437 */     Set<String> keys = processorStringMap.keySet();
/* 438 */     Map<Integer, IInitDataProcessor> procMap = new HashMap();
/* 439 */     for (String strKey : keys) {
/* 440 */       IInitDataProcessor processor = (IInitDataProcessor)processorStringMap.get(strKey);
/*     */       
/* 442 */       Integer key = Integer.valueOf(strKey);
/* 443 */       procMap.put(key, processor);
/*     */     }
/*     */     
/* 446 */     processorMap = procMap;
/*     */   }
/*     */   
/*     */   public void setQueryAction(DefaultQueryAction queryAction) {
/* 450 */     this.queryAction = queryAction;
/*     */   }
/*     */   
/*     */   public void setService(IQueryService service) {
/* 454 */     this.service = service;
/*     */   }
/*     */   
/*     */   public void setVoClassName(String voClassName) {
/* 458 */     this.voClassName = voClassName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void doInitDataByQueryScheme(FuncletInitData data)
/*     */   {
/* 465 */     SimpleQuerySchemeVO vo = (SimpleQuerySchemeVO)data.getInitData();
/* 466 */     if (getQueryAction() == null) {
/* 467 */       throw new IllegalArgumentException(NCLangRes.getInstance().getStrByID("uif2", "DefaultFuncNodeInitDataListener-000000"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 474 */     getQueryAction().doSimpleSchemeQuery(vo);
/*     */   }
/*     */   
/*     */   private void doQSOpenInit(Object initData) {
/* 478 */     QSContext qscontext = (QSContext)initData;
/* 479 */     String billID = qscontext.getBillid();
/* 480 */     String billType = qscontext.getBilltype();
/*     */     
/*     */ 
/* 483 */     if (getQsservice() != null) {
/*     */       try {
/* 485 */         Object obj = getQsservice().queryBill(qscontext);
/* 486 */         getModel().initModel(obj);
/*     */       } catch (Exception e) {
/* 488 */         Logger.error(e.getMessage(), e);
/*     */       }
/* 490 */     } else if (qscontext.getQstype().equalsIgnoreCase("fd"))
/*     */     {
/*     */ 
/* 493 */       if ((billID != null) && (billType != null))
/*     */         try {
/* 495 */           if (getPmodelDataManager() != null) {
/* 496 */             getPmodelDataManager().initModelByPks(new String[] { billID });
/*     */           }
/*     */           else {
/* 499 */             Object obj = getService().queryByPk(billID);
/* 500 */             getModel().initModel(obj);
/*     */           }
/*     */         } catch (Exception e) {
/* 503 */           Logger.error(e.getMessage(), e);
/*     */         }
/*     */     }
/*     */   }
/*     */   
/*     */   private IInitDataProcessor getDefaultProcessor() {
/* 509 */     return new DefaultInitDataProcessor();
/*     */   }
/*     */   
/*     */   private IExceptionHandler getExceptionHandler(boolean showErrorDlg) {
/* 513 */     if (exceptionHandler == null) {
/* 514 */       exceptionHandler = new PubExceptionHanler(context, showErrorDlg);
/*     */     }
/*     */     
/* 517 */     return exceptionHandler;
/*     */   }
/*     */   
/*     */   public static abstract interface IQueryServiceExt
/*     */     extends DefaultFuncNodeInitDataListener.IQueryService
/*     */   {
/*     */     public abstract Object[] queryByPk(String[] paramArrayOfString);
/*     */   }
/*     */   
/*     */   public static abstract interface IQueryServiceWithFuncCodeExt
/*     */     extends DefaultFuncNodeInitDataListener.IQueryServiceWithFunCode
/*     */   {
/*     */     public abstract Object[] queryByPksWithFunCode(String[] paramArrayOfString, String paramString);
/*     */   }
/*     */   
/*     */   public static abstract interface IQueryServiceWithFunCode
/*     */     extends DefaultFuncNodeInitDataListener.IQueryService
/*     */   {
/*     */     public abstract Object queryByPkWithFunCode(String paramString1, String paramString2);
/*     */   }
/*     */   
/*     */   public static abstract interface IQueryService
/*     */   {
/*     */     public abstract Object queryByPk(String paramString);
/*     */   }
/*     */   
/*     */   public static abstract interface IInitDataProcessor
/*     */   {
/*     */     public abstract void process(FuncletInitData paramFuncletInitData);
/*     */   }
/*     */ }