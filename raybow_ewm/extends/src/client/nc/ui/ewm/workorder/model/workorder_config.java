/*      */ package nc.ui.ewm.workorder.model;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import nc.ui.am.ref.refinfos.InitRefInfo;
/*      */ import nc.ui.uif2.editor.UserdefQueryParam;
/*      */ 
/*      */ public class workorder_config extends nc.ui.uif2.factory.AbstractJavaBeanDefinition
/*      */ {
/*   11 */   private Map<String, Object> context = new java.util.HashMap();
/*      */   
/*   13 */   public nc.ui.ewm.workorder.view.WorkOrderBillForm getBillForm() { if (context.get("billForm") != null)
/*   14 */       return (nc.ui.ewm.workorder.view.WorkOrderBillForm)context.get("billForm");
/*   15 */     nc.ui.ewm.workorder.view.WorkOrderBillForm bean = new nc.ui.ewm.workorder.view.WorkOrderBillForm();
/*   16 */     context.put("billForm", bean);
/*   17 */     bean.setModel(getModel());
/*   18 */     bean.setTemplateContainer(getTemplateContainer());
/*   19 */     bean.setBodyActionMap(getBodyActionsMap());
/*   20 */     bean.setHeadActions(getHeadTabActions());
/*   21 */     bean.setDefaultBodyActions(getDefaultBodyActions());
/*   22 */     bean.setModelDataManager(getModelDataManager());
/*   23 */     bean.setUserdefitemPreparator(getBillDataPreparator());
/*   24 */     bean.setClosingListener(getClosingListener());
/*   25 */     bean.setOrgPanel(getOrgPanel());
/*   26 */     bean.setClcStrategys(getCardListenerConnectStrategys());
/*   27 */     bean.setScaleProcessor(getBillCardDigitProcessor());
/*   28 */     bean.setCeStrategys(getCardComponentExtStrategys());
/*   29 */     bean.initUI();
/*   30 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   31 */     invokeInitializingBean(bean);
/*   32 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.view.WorkOrderListView getBillListView() {
/*   36 */     if (context.get("billListView") != null)
/*   37 */       return (nc.ui.ewm.workorder.view.WorkOrderListView)context.get("billListView");
/*   38 */     nc.ui.ewm.workorder.view.WorkOrderListView bean = new nc.ui.ewm.workorder.view.WorkOrderListView();
/*   39 */     context.put("billListView", bean);
/*   40 */     bean.setModel(getModel());
/*   41 */     bean.setMultiSelectionEnable(false);
/*   42 */     bean.setTemplateContainer(getTemplateContainer());
/*   43 */     bean.setModelDataManager(getModelDataManager());
/*   44 */     bean.setUserdefitemListPreparator(getDefItemListPreparator());
/*   45 */     bean.setClcStrategys(getListListenerConnectStrategys());
/*   46 */     bean.setScaleProcessor(getBillListDigitProcessor());
/*   47 */     bean.setPaginationBar(getPaginationBar());
/*   48 */     bean.initUI();
/*   49 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   50 */     invokeInitializingBean(bean);
/*   51 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.vo.uif2.LoginContext getContext() {
/*   55 */     if (context.get("context") != null)
/*   56 */       return (nc.vo.uif2.LoginContext)context.get("context");
/*   57 */     nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
/*   58 */     context.put("context", bean);
/*   59 */     bean.setNodeType(nc.vo.bd.pub.NODE_TYPE.ORG_NODE);
/*   60 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   61 */     invokeInitializingBean(bean);
/*   62 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller getRemoteCallCombinatorCaller() {
/*   66 */     if (context.get("remoteCallCombinatorCaller") != null)
/*   67 */       return (nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller)context.get("remoteCallCombinatorCaller");
/*   68 */     nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller bean = new nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller();
/*   69 */     context.put("remoteCallCombinatorCaller", bean);
/*   70 */     bean.setRemoteCallers(getManagedList0());
/*   71 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   72 */     invokeInitializingBean(bean);
/*   73 */     return bean;
/*      */   }
/*      */   
/*   76 */   private List getManagedList0() { List list = new ArrayList();list.add(getTemplateContainer());list.add(getUserdefitemContainer());list.add(getQueryTemplateContainer());list.add(getPfAddInfoRemoteCaller());return list;
/*      */   }
/*      */   
/*   79 */   public nc.ui.uif2.editor.QueryTemplateContainer getQueryTemplateContainer() { if (context.get("queryTemplateContainer") != null)
/*   80 */       return (nc.ui.uif2.editor.QueryTemplateContainer)context.get("queryTemplateContainer");
/*   81 */     nc.ui.uif2.editor.QueryTemplateContainer bean = new nc.ui.uif2.editor.QueryTemplateContainer();
/*   82 */     context.put("queryTemplateContainer", bean);
/*   83 */     bean.setContext(getContext());
/*   84 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   85 */     invokeInitializingBean(bean);
/*   86 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.remotecall.PfAddInfoRemoteCallCombinator getPfAddInfoRemoteCaller() {
/*   90 */     if (context.get("pfAddInfoRemoteCaller") != null)
/*   91 */       return (nc.ui.am.remotecall.PfAddInfoRemoteCallCombinator)context.get("pfAddInfoRemoteCaller");
/*   92 */     nc.ui.am.remotecall.PfAddInfoRemoteCallCombinator bean = new nc.ui.am.remotecall.PfAddInfoRemoteCallCombinator();
/*   93 */     context.put("pfAddInfoRemoteCaller", bean);
/*   94 */     bean.setModel(getModel());
/*   95 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*   96 */     invokeInitializingBean(bean);
/*   97 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.TangramContainer getContainer() {
/*  101 */     if (context.get("container") != null)
/*  102 */       return (nc.ui.uif2.TangramContainer)context.get("container");
/*  103 */     nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
/*  104 */     context.put("container", bean);
/*  105 */     bean.setTangramLayout(getTangramLayout_e4936a());
/*  106 */     bean.setTangramLayoutRoot(getTBNode_1ae803d());
/*  107 */     bean.initUI();
/*  108 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  109 */     invokeInitializingBean(bean);
/*  110 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.TangramLayout getTangramLayout_e4936a() {
/*  114 */     if (context.get("nc.ui.uif2.tangramlayout.TangramLayout#e4936a") != null)
/*  115 */       return (nc.ui.uif2.tangramlayout.TangramLayout)context.get("nc.ui.uif2.tangramlayout.TangramLayout#e4936a");
/*  116 */     nc.ui.uif2.tangramlayout.TangramLayout bean = new nc.ui.uif2.tangramlayout.TangramLayout();
/*  117 */     context.put("nc.ui.uif2.tangramlayout.TangramLayout#e4936a", bean);
/*  118 */     bean.setContainerComponentStrategy(getAMContainerComponentStrategy_167b234());
/*  119 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  120 */     invokeInitializingBean(bean);
/*  121 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.layout.AMContainerComponentStrategy getAMContainerComponentStrategy_167b234() {
/*  125 */     if (context.get("nc.ui.am.layout.AMContainerComponentStrategy#167b234") != null)
/*  126 */       return (nc.ui.am.layout.AMContainerComponentStrategy)context.get("nc.ui.am.layout.AMContainerComponentStrategy#167b234");
/*  127 */     nc.ui.am.layout.AMContainerComponentStrategy bean = new nc.ui.am.layout.AMContainerComponentStrategy();
/*  128 */     context.put("nc.ui.am.layout.AMContainerComponentStrategy#167b234", bean);
/*  129 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  130 */     invokeInitializingBean(bean);
/*  131 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_1ae803d() {
/*  135 */     if (context.get("nc.ui.uif2.tangramlayout.node.TBNode#1ae803d") != null)
/*  136 */       return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#1ae803d");
/*  137 */     nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
/*  138 */     context.put("nc.ui.uif2.tangramlayout.node.TBNode#1ae803d", bean);
/*  139 */     bean.setShowMode("CardLayout");
/*  140 */     bean.setTabs(getManagedList1());
/*  141 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  142 */     invokeInitializingBean(bean);
/*  143 */     return bean;
/*      */   }
/*      */   
/*  146 */   private List getManagedList1() { List list = new ArrayList();list.add(getHSNode_16d796c());list.add(getVSNode_ed84df());return list;
/*      */   }
/*      */   
/*  149 */   private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_16d796c() { if (context.get("nc.ui.uif2.tangramlayout.node.HSNode#16d796c") != null)
/*  150 */       return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#16d796c");
/*  151 */     nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
/*  152 */     context.put("nc.ui.uif2.tangramlayout.node.HSNode#16d796c", bean);
/*  153 */     bean.setLeft(getCNode_8a46df());
/*  154 */     bean.setRight(getVSNode_1b69ace());
/*  155 */     bean.setDividerLocation(210.0F);
/*  156 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  157 */     invokeInitializingBean(bean);
/*  158 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_8a46df() {
/*  162 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#8a46df") != null)
/*  163 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#8a46df");
/*  164 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/*  165 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#8a46df", bean);
/*  166 */     bean.setComponent(getQueryArea());
/*  167 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  168 */     invokeInitializingBean(bean);
/*  169 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_1b69ace() {
/*  173 */     if (context.get("nc.ui.uif2.tangramlayout.node.VSNode#1b69ace") != null)
/*  174 */       return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#1b69ace");
/*  175 */     nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
/*  176 */     context.put("nc.ui.uif2.tangramlayout.node.VSNode#1b69ace", bean);
/*  177 */     bean.setUp(getCNode_32e0e7());
/*  178 */     bean.setDown(getBillListViewNode_4b83bf());
/*  179 */     bean.setDividerLocation(25.0F);
/*  180 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  181 */     invokeInitializingBean(bean);
/*  182 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_32e0e7() {
/*  186 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#32e0e7") != null)
/*  187 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#32e0e7");
/*  188 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/*  189 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#32e0e7", bean);
/*  190 */     bean.setComponent(getQueryInfo());
/*  191 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  192 */     invokeInitializingBean(bean);
/*  193 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.editor.uf2.BillListViewNode getBillListViewNode_4b83bf() {
/*  197 */     if (context.get("nc.ui.am.editor.uf2.BillListViewNode#4b83bf") != null)
/*  198 */       return (nc.ui.am.editor.uf2.BillListViewNode)context.get("nc.ui.am.editor.uf2.BillListViewNode#4b83bf");
/*  199 */     nc.ui.am.editor.uf2.BillListViewNode bean = new nc.ui.am.editor.uf2.BillListViewNode();
/*  200 */     context.put("nc.ui.am.editor.uf2.BillListViewNode#4b83bf", bean);
/*  201 */     bean.setComponent(getBillListView());
/*  202 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  203 */     invokeInitializingBean(bean);
/*  204 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_ed84df() {
/*  208 */     if (context.get("nc.ui.uif2.tangramlayout.node.VSNode#ed84df") != null)
/*  209 */       return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#ed84df");
/*  210 */     nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
/*  211 */     context.put("nc.ui.uif2.tangramlayout.node.VSNode#ed84df", bean);
/*  212 */     bean.setUp(getCNode_3ebace());
/*  213 */     bean.setDown(getBillFormNode_5a2fa3());
/*  214 */     bean.setDividerLocation(25.0F);
/*  215 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  216 */     invokeInitializingBean(bean);
/*  217 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.tangramlayout.node.CNode getCNode_3ebace() {
/*  221 */     if (context.get("nc.ui.uif2.tangramlayout.node.CNode#3ebace") != null)
/*  222 */       return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#3ebace");
/*  223 */     nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
/*  224 */     context.put("nc.ui.uif2.tangramlayout.node.CNode#3ebace", bean);
/*  225 */     bean.setComponent(getCardPnlInfo());
/*  226 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  227 */     invokeInitializingBean(bean);
/*  228 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.editor.uf2.BillFormNode getBillFormNode_5a2fa3() {
/*  232 */     if (context.get("nc.ui.am.editor.uf2.BillFormNode#5a2fa3") != null)
/*  233 */       return (nc.ui.am.editor.uf2.BillFormNode)context.get("nc.ui.am.editor.uf2.BillFormNode#5a2fa3");
/*  234 */     nc.ui.am.editor.uf2.BillFormNode bean = new nc.ui.am.editor.uf2.BillFormNode();
/*  235 */     context.put("nc.ui.am.editor.uf2.BillFormNode#5a2fa3", bean);
/*  236 */     bean.setComponent(getBillForm());
/*  237 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  238 */     invokeInitializingBean(bean);
/*  239 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getQueryInfo() {
/*  243 */     if (context.get("queryInfo") != null)
/*  244 */       return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("queryInfo");
/*  245 */     nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
/*  246 */     context.put("queryInfo", bean);
/*  247 */     bean.setModel(getModel());
/*  248 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  249 */     invokeInitializingBean(bean);
/*  250 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell getQueryArea() {
/*  254 */     if (context.get("queryArea") != null)
/*  255 */       return (nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell)context.get("queryArea");
/*  256 */     nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell bean = new nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell();
/*  257 */     context.put("queryArea", bean);
/*  258 */     bean.setQueryAreaCreator(getQueryAction());
/*  259 */     bean.initUI();
/*  260 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  261 */     invokeInitializingBean(bean);
/*  262 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getListPnlInfo() {
/*  266 */     if (context.get("listPnlInfo") != null)
/*  267 */       return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("listPnlInfo");
/*  268 */     nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
/*  269 */     context.put("listPnlInfo", bean);
/*  270 */     bean.setModel(getModel());
/*  271 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  272 */     invokeInitializingBean(bean);
/*  273 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.view.WOCardLayoutToolbarPanel getCardPnlInfo() {
/*  277 */     if (context.get("cardPnlInfo") != null)
/*  278 */       return (nc.ui.ewm.workorder.view.WOCardLayoutToolbarPanel)context.get("cardPnlInfo");
/*  279 */     nc.ui.ewm.workorder.view.WOCardLayoutToolbarPanel bean = new nc.ui.ewm.workorder.view.WOCardLayoutToolbarPanel();
/*  280 */     context.put("cardPnlInfo", bean);
/*  281 */     bean.setActions(getManagedList2());
/*  282 */     bean.setTitleAction(getReturnaction());
/*  283 */     bean.setModel(getModel());
/*  284 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  285 */     invokeInitializingBean(bean);
/*  286 */     return bean;
/*      */   }
/*      */   
/*  289 */   private List getManagedList2() { List list = new ArrayList();list.add(getAttachmentActionCard());list.add(getActionsBar_ActionsBarSeparator_d3e64c());list.add(getFirstLineAction());list.add(getPreLineAction());list.add(getNextLineAction());list.add(getLastLineAction());list.add(getActionsBar_ActionsBarSeparator_1b4c79a());list.add(getHeadViewMaxAction());return list;
/*      */   }
/*      */   
/*  292 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_d3e64c() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#d3e64c") != null)
/*  293 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#d3e64c");
/*  294 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/*  295 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#d3e64c", bean);
/*  296 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  297 */     invokeInitializingBean(bean);
/*  298 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1b4c79a() {
/*  302 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b4c79a") != null)
/*  303 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b4c79a");
/*  304 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/*  305 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1b4c79a", bean);
/*  306 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  307 */     invokeInitializingBean(bean);
/*  308 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.action.support.ReturnToListAction getReturnaction() {
/*  312 */     if (context.get("returnaction") != null)
/*  313 */       return (nc.ui.am.action.support.ReturnToListAction)context.get("returnaction");
/*  314 */     nc.ui.am.action.support.ReturnToListAction bean = new nc.ui.am.action.support.ReturnToListAction();
/*  315 */     context.put("returnaction", bean);
/*  316 */     bean.setGoComponent(getBillListView());
/*  317 */     bean.setModel(getModel());
/*  318 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  319 */     invokeInitializingBean(bean);
/*  320 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.orgpanel.OrgPanel getOrgPanel() {
/*  324 */     if (context.get("orgPanel") != null)
/*  325 */       return (nc.ui.am.orgpanel.OrgPanel)context.get("orgPanel");
/*  326 */     nc.ui.am.orgpanel.OrgPanel bean = new nc.ui.am.orgpanel.OrgPanel();
/*  327 */     context.put("orgPanel", bean);
/*  328 */     bean.setModel(getModel());
/*  329 */     bean.initUI();
/*  330 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  331 */     invokeInitializingBean(bean);
/*  332 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.orgpanel.OrgChangeEventMediator getOrgChangeEventMediator() {
/*  336 */     if (context.get("orgChangeEventMediator") != null)
/*  337 */       return (nc.ui.am.orgpanel.OrgChangeEventMediator)context.get("orgChangeEventMediator");
/*  338 */     nc.ui.am.orgpanel.OrgChangeEventMediator bean = new nc.ui.am.orgpanel.OrgChangeEventMediator();
/*  339 */     context.put("orgChangeEventMediator", bean);
/*  340 */     bean.setBillForm(getBillForm());
/*  341 */     bean.setModel(getModel());
/*  342 */     bean.setOrgChangeHandlers(getOrgChangeHandlers());
/*  343 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  344 */     invokeInitializingBean(bean);
/*  345 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getOrgChangeHandlers() {
/*  349 */     if (context.get("orgChangeHandlers") != null)
/*  350 */       return (ArrayList)context.get("orgChangeHandlers");
/*  351 */     ArrayList bean = new ArrayList(getManagedList3());context.put("orgChangeHandlers", bean);
/*  352 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  353 */     invokeInitializingBean(bean);
/*  354 */     return bean;
/*      */   }
/*      */   
/*  357 */   private List getManagedList3() { List list = new ArrayList();list.add(getSetOrgToTemplateHandler_88fc94());list.add(getBillCodeHandler_17e79a5());list.add(getChgTOAddNewHandler_6471a4());list.add(getBillFormEnableHandler_60e1e1());list.add(getWorkOrderOrgChgHandler_8dba92());return list;
/*      */   }
/*      */   
/*  360 */   private nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler getSetOrgToTemplateHandler_88fc94() { if (context.get("nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler#88fc94") != null)
/*  361 */       return (nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler)context.get("nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler#88fc94");
/*  362 */     nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler bean = new nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler();
/*  363 */     context.put("nc.ui.am.orgpanel.handler.SetOrgToTemplateHandler#88fc94", bean);
/*  364 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  365 */     invokeInitializingBean(bean);
/*  366 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.orgpanel.handler.BillCodeHandler getBillCodeHandler_17e79a5() {
/*  370 */     if (context.get("nc.ui.am.orgpanel.handler.BillCodeHandler#17e79a5") != null)
/*  371 */       return (nc.ui.am.orgpanel.handler.BillCodeHandler)context.get("nc.ui.am.orgpanel.handler.BillCodeHandler#17e79a5");
/*  372 */     nc.ui.am.orgpanel.handler.BillCodeHandler bean = new nc.ui.am.orgpanel.handler.BillCodeHandler();
/*  373 */     context.put("nc.ui.am.orgpanel.handler.BillCodeHandler#17e79a5", bean);
/*  374 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  375 */     invokeInitializingBean(bean);
/*  376 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.orgpanel.handler.ChgTOAddNewHandler getChgTOAddNewHandler_6471a4() {
/*  380 */     if (context.get("nc.ui.am.orgpanel.handler.ChgTOAddNewHandler#6471a4") != null)
/*  381 */       return (nc.ui.am.orgpanel.handler.ChgTOAddNewHandler)context.get("nc.ui.am.orgpanel.handler.ChgTOAddNewHandler#6471a4");
/*  382 */     nc.ui.am.orgpanel.handler.ChgTOAddNewHandler bean = new nc.ui.am.orgpanel.handler.ChgTOAddNewHandler();
/*  383 */     context.put("nc.ui.am.orgpanel.handler.ChgTOAddNewHandler#6471a4", bean);
/*  384 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  385 */     invokeInitializingBean(bean);
/*  386 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.orgpanel.handler.BillFormEnableHandler getBillFormEnableHandler_60e1e1() {
/*  390 */     if (context.get("nc.ui.am.orgpanel.handler.BillFormEnableHandler#60e1e1") != null)
/*  391 */       return (nc.ui.am.orgpanel.handler.BillFormEnableHandler)context.get("nc.ui.am.orgpanel.handler.BillFormEnableHandler#60e1e1");
/*  392 */     nc.ui.am.orgpanel.handler.BillFormEnableHandler bean = new nc.ui.am.orgpanel.handler.BillFormEnableHandler();
/*  393 */     context.put("nc.ui.am.orgpanel.handler.BillFormEnableHandler#60e1e1", bean);
/*  394 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  395 */     invokeInitializingBean(bean);
/*  396 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.event.WorkOrderOrgChgHandler getWorkOrderOrgChgHandler_8dba92() {
/*  400 */     if (context.get("nc.ui.ewm.workorder.event.WorkOrderOrgChgHandler#8dba92") != null)
/*  401 */       return (nc.ui.ewm.workorder.event.WorkOrderOrgChgHandler)context.get("nc.ui.ewm.workorder.event.WorkOrderOrgChgHandler#8dba92");
/*  402 */     nc.ui.ewm.workorder.event.WorkOrderOrgChgHandler bean = new nc.ui.ewm.workorder.event.WorkOrderOrgChgHandler();
/*  403 */     context.put("nc.ui.ewm.workorder.event.WorkOrderOrgChgHandler#8dba92", bean);
/*  404 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  405 */     invokeInitializingBean(bean);
/*  406 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.contributors.AMActionContributors getToftpanelActionContributors() {
/*  410 */     if (context.get("toftpanelActionContributors") != null)
/*  411 */       return (nc.ui.am.action.contributors.AMActionContributors)context.get("toftpanelActionContributors");
/*  412 */     nc.ui.am.action.contributors.AMActionContributors bean = new nc.ui.am.action.contributors.AMActionContributors();
/*  413 */     context.put("toftpanelActionContributors", bean);
/*  414 */     bean.setModel(getModel());
/*  415 */     bean.setBillForm(getBillForm());
/*  416 */     bean.setContributors(getManagedList4());
/*  417 */     bean.setPfAddInfoRemoteCaller(getPfAddInfoRemoteCaller());
/*  418 */     bean.setBillTypeToActionCodeLst(getBillTypeToActionCodeLst());
/*  419 */     bean.initActions();
/*  420 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  421 */     invokeInitializingBean(bean);
/*  422 */     return bean;
/*      */   }
/*      */   
/*  425 */   private List getManagedList4() { List list = new ArrayList();list.add(getCardActionsContainer());list.add(getListActionsContainer());return list;
/*      */   }
/*      */   
/*  428 */   public ArrayList getBillTypeToActionCodeLst() { if (context.get("billTypeToActionCodeLst") != null)
/*  429 */       return (ArrayList)context.get("billTypeToActionCodeLst");
/*  430 */     ArrayList bean = new ArrayList(getManagedList5());context.put("billTypeToActionCodeLst", bean);
/*  431 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  432 */     invokeInitializingBean(bean);
/*  433 */     return bean;
/*      */   }
/*      */   
/*  436 */   private List getManagedList5() { List list = new ArrayList();list.add(getCommonBillTypeToActionCode_154da59());return list;
/*      */   }
/*      */   
/*  439 */   private nc.ui.am.action.contributors.CommonBillTypeToActionCode getCommonBillTypeToActionCode_154da59() { if (context.get("nc.ui.am.action.contributors.CommonBillTypeToActionCode#154da59") != null)
/*  440 */       return (nc.ui.am.action.contributors.CommonBillTypeToActionCode)context.get("nc.ui.am.action.contributors.CommonBillTypeToActionCode#154da59");
/*  441 */     nc.ui.am.action.contributors.CommonBillTypeToActionCode bean = new nc.ui.am.action.contributors.CommonBillTypeToActionCode();
/*  442 */     context.put("nc.ui.am.action.contributors.CommonBillTypeToActionCode#154da59", bean);
/*  443 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  444 */     invokeInitializingBean(bean);
/*  445 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getListActionsContainer() {
/*  449 */     if (context.get("listActionsContainer") != null)
/*  450 */       return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("listActionsContainer");
/*  451 */     nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBillListView());context.put("listActionsContainer", bean);
/*  452 */     bean.setActions(getListViewActions());
/*  453 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  454 */     invokeInitializingBean(bean);
/*  455 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getCardActionsContainer() {
/*  459 */     if (context.get("cardActionsContainer") != null)
/*  460 */       return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("cardActionsContainer");
/*  461 */     nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getBillForm());context.put("cardActionsContainer", bean);
/*  462 */     bean.setActions(getCardViewActions());
/*  463 */     bean.setEditActions(getCardEditActions());
/*  464 */     bean.setModel(getModel());
/*  465 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  466 */     invokeInitializingBean(bean);
/*  467 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getCardComponentExtStrategys() {
/*  471 */     if (context.get("cardComponentExtStrategys") != null)
/*  472 */       return (ArrayList)context.get("cardComponentExtStrategys");
/*  473 */     ArrayList bean = new ArrayList(getManagedList6());context.put("cardComponentExtStrategys", bean);
/*  474 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  475 */     invokeInitializingBean(bean);
/*  476 */     return bean;
/*      */   }
/*      */   
/*  479 */   private List getManagedList6() { List list = new ArrayList();list.add(getTransitypePanelExtStrategy_1cb0cd1());return list;
/*      */   }
/*      */   
/*  482 */   private nc.ui.am.editor.ext.card.TransitypePanelExtStrategy getTransitypePanelExtStrategy_1cb0cd1() { if (context.get("nc.ui.am.editor.ext.card.TransitypePanelExtStrategy#1cb0cd1") != null)
/*  483 */       return (nc.ui.am.editor.ext.card.TransitypePanelExtStrategy)context.get("nc.ui.am.editor.ext.card.TransitypePanelExtStrategy#1cb0cd1");
/*  484 */     nc.ui.am.editor.ext.card.TransitypePanelExtStrategy bean = new nc.ui.am.editor.ext.card.TransitypePanelExtStrategy();
/*  485 */     context.put("nc.ui.am.editor.ext.card.TransitypePanelExtStrategy#1cb0cd1", bean);
/*  486 */     bean.setModel(getModel());
/*  487 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  488 */     invokeInitializingBean(bean);
/*  489 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getCardListenerConnectStrategys() {
/*  493 */     if (context.get("cardListenerConnectStrategys") != null)
/*  494 */       return (ArrayList)context.get("cardListenerConnectStrategys");
/*  495 */     ArrayList bean = new ArrayList(getManagedList7());context.put("cardListenerConnectStrategys", bean);
/*  496 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  497 */     invokeInitializingBean(bean);
/*  498 */     return bean;
/*      */   }
/*      */   
/*  501 */   private List getManagedList7() { List list = new ArrayList();list.add(getCardEditEventHandlerConnectStrategy());list.add(getCardTabChangeEventHandlerConnectStrategy());list.add(getKeyActionsListenerConnectStrategy());list.add(getCardHyperlinkEventHandlerConnectStrategy());return list;
/*      */   }
/*      */   
/*  504 */   public ArrayList getListListenerConnectStrategys() { if (context.get("listListenerConnectStrategys") != null)
/*  505 */       return (ArrayList)context.get("listListenerConnectStrategys");
/*  506 */     ArrayList bean = new ArrayList(getManagedList8());context.put("listListenerConnectStrategys", bean);
/*  507 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  508 */     invokeInitializingBean(bean);
/*  509 */     return bean;
/*      */   }
/*      */   
/*  512 */   private List getManagedList8() { List list = new ArrayList();list.add(getListTabChangeEventHandlerConnectStrategy());list.add(getListRowChangeEventHandlerConnectStrategy());list.add(getListHyperlinkEventHandlerConnectStrategy());return list;
/*      */   }
/*      */   
/*  515 */   public nc.ui.am.editor.event.card.CardEditEventHandlerConnectStrategy getCardEditEventHandlerConnectStrategy() { if (context.get("cardEditEventHandlerConnectStrategy") != null)
/*  516 */       return (nc.ui.am.editor.event.card.CardEditEventHandlerConnectStrategy)context.get("cardEditEventHandlerConnectStrategy");
/*  517 */     nc.ui.am.editor.event.card.CardEditEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.card.CardEditEventHandlerConnectStrategy();
/*  518 */     context.put("cardEditEventHandlerConnectStrategy", bean);
/*  519 */     bean.setBillForm(getBillForm());
/*  520 */     bean.setCardEditEventHandler(getCardEditEventHandler());
/*  521 */     bean.setCardEditEventHandlersExt(getCardEditEventHandlersExt());
/*  522 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  523 */     invokeInitializingBean(bean);
/*  524 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.editor.event.card.CardTabChangeEventHandlerConnectStrategy getCardTabChangeEventHandlerConnectStrategy() {
/*  528 */     if (context.get("cardTabChangeEventHandlerConnectStrategy") != null)
/*  529 */       return (nc.ui.am.editor.event.card.CardTabChangeEventHandlerConnectStrategy)context.get("cardTabChangeEventHandlerConnectStrategy");
/*  530 */     nc.ui.am.editor.event.card.CardTabChangeEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.card.CardTabChangeEventHandlerConnectStrategy();
/*  531 */     context.put("cardTabChangeEventHandlerConnectStrategy", bean);
/*  532 */     bean.setBillForm(getBillForm());
/*  533 */     bean.setTabChangeEventHandlers(getCardTabChangeEventHandlers());
/*  534 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  535 */     invokeInitializingBean(bean);
/*  536 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.editor.event.card.KeyActionsListenerConnectStrategy getKeyActionsListenerConnectStrategy() {
/*  540 */     if (context.get("keyActionsListenerConnectStrategy") != null)
/*  541 */       return (nc.ui.am.editor.event.card.KeyActionsListenerConnectStrategy)context.get("keyActionsListenerConnectStrategy");
/*  542 */     nc.ui.am.editor.event.card.KeyActionsListenerConnectStrategy bean = new nc.ui.am.editor.event.card.KeyActionsListenerConnectStrategy();
/*  543 */     context.put("keyActionsListenerConnectStrategy", bean);
/*  544 */     bean.setBillForm(getBillForm());
/*  545 */     bean.setKeyListeners(getKeyEventHandlers());
/*  546 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  547 */     invokeInitializingBean(bean);
/*  548 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.editor.event.list.ListTabChangeEventHandlerConnectStrategy getListTabChangeEventHandlerConnectStrategy() {
/*  552 */     if (context.get("listTabChangeEventHandlerConnectStrategy") != null)
/*  553 */       return (nc.ui.am.editor.event.list.ListTabChangeEventHandlerConnectStrategy)context.get("listTabChangeEventHandlerConnectStrategy");
/*  554 */     nc.ui.am.editor.event.list.ListTabChangeEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.list.ListTabChangeEventHandlerConnectStrategy();
/*  555 */     context.put("listTabChangeEventHandlerConnectStrategy", bean);
/*  556 */     bean.setBillListView(getBillListView());
/*  557 */     bean.setTabChangeEventHandlers(getListTabChangeEventHandlers());
/*  558 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  559 */     invokeInitializingBean(bean);
/*  560 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.editor.event.list.ListRowChangeEventHanlderConnectStrategy getListRowChangeEventHandlerConnectStrategy() {
/*  564 */     if (context.get("listRowChangeEventHandlerConnectStrategy") != null)
/*  565 */       return (nc.ui.am.editor.event.list.ListRowChangeEventHanlderConnectStrategy)context.get("listRowChangeEventHandlerConnectStrategy");
/*  566 */     nc.ui.am.editor.event.list.ListRowChangeEventHanlderConnectStrategy bean = new nc.ui.am.editor.event.list.ListRowChangeEventHanlderConnectStrategy();
/*  567 */     context.put("listRowChangeEventHandlerConnectStrategy", bean);
/*  568 */     bean.setBillListView(getBillListView());
/*  569 */     bean.setListRowChangeEventHandlers(getListRowChangeEventHandlers());
/*  570 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  571 */     invokeInitializingBean(bean);
/*  572 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getCardTabChangeEventHandlers() {
/*  576 */     if (context.get("cardTabChangeEventHandlers") != null)
/*  577 */       return (ArrayList)context.get("cardTabChangeEventHandlers");
/*  578 */     ArrayList bean = new ArrayList(getManagedList9());context.put("cardTabChangeEventHandlers", bean);
/*  579 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  580 */     invokeInitializingBean(bean);
/*  581 */     return bean;
/*      */   }
/*      */   
/*  584 */   private List getManagedList9() { List list = new ArrayList();list.add(getDefaultCardTabChangeEventHandler_ee9103());list.add(getWorkOrderCardTabChangeEventHandler_7293d4());return list;
/*      */   }
/*      */   
/*  587 */   private nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler getDefaultCardTabChangeEventHandler_ee9103() { if (context.get("nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler#ee9103") != null)
/*  588 */       return (nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler)context.get("nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler#ee9103");
/*  589 */     nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler bean = new nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler();
/*  590 */     context.put("nc.ui.am.editor.event.card.DefaultCardTabChangeEventHandler#ee9103", bean);
/*  591 */     bean.setModelDataManager(getModelDataManager());
/*  592 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  593 */     invokeInitializingBean(bean);
/*  594 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.event.WorkOrderCardTabChangeEventHandler getWorkOrderCardTabChangeEventHandler_7293d4() {
/*  598 */     if (context.get("nc.ui.ewm.workorder.event.WorkOrderCardTabChangeEventHandler#7293d4") != null)
/*  599 */       return (nc.ui.ewm.workorder.event.WorkOrderCardTabChangeEventHandler)context.get("nc.ui.ewm.workorder.event.WorkOrderCardTabChangeEventHandler#7293d4");
/*  600 */     nc.ui.ewm.workorder.event.WorkOrderCardTabChangeEventHandler bean = new nc.ui.ewm.workorder.event.WorkOrderCardTabChangeEventHandler();
/*  601 */     context.put("nc.ui.ewm.workorder.event.WorkOrderCardTabChangeEventHandler#7293d4", bean);
/*  602 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  603 */     invokeInitializingBean(bean);
/*  604 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getListTabChangeEventHandlers() {
/*  608 */     if (context.get("listTabChangeEventHandlers") != null)
/*  609 */       return (ArrayList)context.get("listTabChangeEventHandlers");
/*  610 */     ArrayList bean = new ArrayList(getManagedList10());context.put("listTabChangeEventHandlers", bean);
/*  611 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  612 */     invokeInitializingBean(bean);
/*  613 */     return bean;
/*      */   }
/*      */   
/*  616 */   private List getManagedList10() { List list = new ArrayList();list.add(getDefaultListTabChangeEventHandler_ca0a6a());return list;
/*      */   }
/*      */   
/*  619 */   private nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler getDefaultListTabChangeEventHandler_ca0a6a() { if (context.get("nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler#ca0a6a") != null)
/*  620 */       return (nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler)context.get("nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler#ca0a6a");
/*  621 */     nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler bean = new nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler();
/*  622 */     context.put("nc.ui.am.editor.event.list.DefaultListTabChangeEventHandler#ca0a6a", bean);
/*  623 */     bean.setModelDataManager(getModelDataManager());
/*  624 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  625 */     invokeInitializingBean(bean);
/*  626 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getListRowChangeEventHandlers() {
/*  630 */     if (context.get("listRowChangeEventHandlers") != null)
/*  631 */       return (ArrayList)context.get("listRowChangeEventHandlers");
/*  632 */     ArrayList bean = new ArrayList(getManagedList11());context.put("listRowChangeEventHandlers", bean);
/*  633 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  634 */     invokeInitializingBean(bean);
/*  635 */     return bean;
/*      */   }
/*      */   
/*  638 */   private List getManagedList11() { List list = new ArrayList();list.add(getDefaultListRowChangeEventHandler_71d039());return list;
/*      */   }
/*      */   
/*  641 */   private nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler getDefaultListRowChangeEventHandler_71d039() { if (context.get("nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler#71d039") != null)
/*  642 */       return (nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler)context.get("nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler#71d039");
/*  643 */     nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler bean = new nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler();
/*  644 */     context.put("nc.ui.am.editor.event.list.DefaultListRowChangeEventHandler#71d039", bean);
/*  645 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  646 */     invokeInitializingBean(bean);
/*  647 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getKeyEventHandlers() {
/*  651 */     if (context.get("keyEventHandlers") != null)
/*  652 */       return (ArrayList)context.get("keyEventHandlers");
/*  653 */     ArrayList bean = new ArrayList(getManagedList12());context.put("keyEventHandlers", bean);
/*  654 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  655 */     invokeInitializingBean(bean);
/*  656 */     return bean;
/*      */   }
/*      */   
/*  659 */   private List getManagedList12() { List list = new ArrayList();list.add(getKeysAction4AddLineEventHandler_1d6d2f());return list;
/*      */   }
/*      */   
/*  662 */   private nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler getKeysAction4AddLineEventHandler_1d6d2f() { if (context.get("nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler#1d6d2f") != null)
/*  663 */       return (nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler)context.get("nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler#1d6d2f");
/*  664 */     nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler bean = new nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler();
/*  665 */     context.put("nc.ui.am.editor.event.card.handlers.KeysAction4AddLineEventHandler#1d6d2f", bean);
/*  666 */     bean.setBillForm(getBillForm());
/*  667 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  668 */     invokeInitializingBean(bean);
/*  669 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.WorkOrderCommonEditHandler getCardEditEventHandler() {
/*  673 */     if (context.get("cardEditEventHandler") != null)
/*  674 */       return (nc.ui.ewm.workorder.event.WorkOrderCommonEditHandler)context.get("cardEditEventHandler");
/*  675 */     nc.ui.ewm.workorder.event.WorkOrderCommonEditHandler bean = new nc.ui.ewm.workorder.event.WorkOrderCommonEditHandler();
/*  676 */     context.put("cardEditEventHandler", bean);
/*  677 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  678 */     invokeInitializingBean(bean);
/*  679 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getCardEditEventHandlersExt() {
/*  683 */     if (context.get("cardEditEventHandlersExt") != null)
/*  684 */       return (ArrayList)context.get("cardEditEventHandlersExt");
/*  685 */     ArrayList bean = new ArrayList(getManagedList13());context.put("cardEditEventHandlersExt", bean);
/*  686 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  687 */     invokeInitializingBean(bean);
/*  688 */     return bean;
/*      */   }
/*      */   
/*  691 */   private List getManagedList13() { List list = new ArrayList();list.add(getMaterialEditActual());list.add(getMaterialEditPlan());list.add(getPlanToolEditEventHandler());list.add(getActualToolEditEventHandler());list.add(getPlanPsnEditEventHandler());list.add(getActualPsnEditEventHandler());list.add(getMaterialEditWOPart());list.add(getWorkOrderEditHandler());list.add(getUserDefItemCardEditEventHandler_1c8c8ab());return list;
/*      */   }
/*      */   
/*  694 */   private nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler getUserDefItemCardEditEventHandler_1c8c8ab() { if (context.get("nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler#1c8c8ab") != null)
/*  695 */       return (nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler)context.get("nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler#1c8c8ab");
/*  696 */     nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler bean = new nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler();
/*  697 */     context.put("nc.ui.am.editor.event.card.UserDefItemCardEditEventHandler#1c8c8ab", bean);
/*  698 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  699 */     invokeInitializingBean(bean);
/*  700 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.editor.event.card.CardHyperlinkEventHandlerConnectStrategy getCardHyperlinkEventHandlerConnectStrategy() {
/*  704 */     if (context.get("cardHyperlinkEventHandlerConnectStrategy") != null)
/*  705 */       return (nc.ui.am.editor.event.card.CardHyperlinkEventHandlerConnectStrategy)context.get("cardHyperlinkEventHandlerConnectStrategy");
/*  706 */     nc.ui.am.editor.event.card.CardHyperlinkEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.card.CardHyperlinkEventHandlerConnectStrategy();
/*  707 */     context.put("cardHyperlinkEventHandlerConnectStrategy", bean);
/*  708 */     bean.setHyperlinkEventHandlers(getCardHyperlinkEventHandlers());
/*  709 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  710 */     invokeInitializingBean(bean);
/*  711 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getCardHyperlinkEventHandlers() {
/*  715 */     if (context.get("cardHyperlinkEventHandlers") != null)
/*  716 */       return (ArrayList)context.get("cardHyperlinkEventHandlers");
/*  717 */     ArrayList bean = new ArrayList(getManagedList14());context.put("cardHyperlinkEventHandlers", bean);
/*  718 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  719 */     invokeInitializingBean(bean);
/*  720 */     return bean;
/*      */   }
/*      */   
/*  723 */   private List getManagedList14() { List list = new ArrayList();list.add(getQueryAboutHyperlinkEventHandler());return list;
/*      */   }
/*      */   
/*  726 */   public nc.ui.am.editor.event.list.ListHyperlinkEventHandlerConnectStrategy getListHyperlinkEventHandlerConnectStrategy() { if (context.get("listHyperlinkEventHandlerConnectStrategy") != null)
/*  727 */       return (nc.ui.am.editor.event.list.ListHyperlinkEventHandlerConnectStrategy)context.get("listHyperlinkEventHandlerConnectStrategy");
/*  728 */     nc.ui.am.editor.event.list.ListHyperlinkEventHandlerConnectStrategy bean = new nc.ui.am.editor.event.list.ListHyperlinkEventHandlerConnectStrategy();
/*  729 */     context.put("listHyperlinkEventHandlerConnectStrategy", bean);
/*  730 */     bean.setHyperlinkEventHandlers(getListHyperlinkEventHandlers());
/*  731 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  732 */     invokeInitializingBean(bean);
/*  733 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getListHyperlinkEventHandlers() {
/*  737 */     if (context.get("listHyperlinkEventHandlers") != null)
/*  738 */       return (ArrayList)context.get("listHyperlinkEventHandlers");
/*  739 */     ArrayList bean = new ArrayList(getManagedList15());context.put("listHyperlinkEventHandlers", bean);
/*  740 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  741 */     invokeInitializingBean(bean);
/*  742 */     return bean;
/*      */   }
/*      */   
/*  745 */   private List getManagedList15() { List list = new ArrayList();list.add(getQueryAboutHyperlinkEventHandler());return list;
/*      */   }
/*      */   
/*  748 */   public nc.ui.am.editor.event.handlers.support.QueryAboutHyperlinkEventHandler getQueryAboutHyperlinkEventHandler() { if (context.get("queryAboutHyperlinkEventHandler") != null)
/*  749 */       return (nc.ui.am.editor.event.handlers.support.QueryAboutHyperlinkEventHandler)context.get("queryAboutHyperlinkEventHandler");
/*  750 */     nc.ui.am.editor.event.handlers.support.QueryAboutHyperlinkEventHandler bean = new nc.ui.am.editor.event.handlers.support.QueryAboutHyperlinkEventHandler();
/*  751 */     context.put("queryAboutHyperlinkEventHandler", bean);
/*  752 */     bean.setFuncNode2MD(getManagedList16());
/*  753 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  754 */     invokeInitializingBean(bean);
/*  755 */     return bean;
/*      */   }
/*      */   
/*  758 */   private List getManagedList16() { List list = new ArrayList();list.add(getCommonFuncNodeToMD_cae4d3());list.add(getEAMFuncNodeToMD_74e662());return list;
/*      */   }
/*      */   
/*  761 */   private nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD getCommonFuncNodeToMD_cae4d3() { if (context.get("nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD#cae4d3") != null)
/*  762 */       return (nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD)context.get("nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD#cae4d3");
/*  763 */     nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD bean = new nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD();
/*  764 */     context.put("nc.ui.am.editor.event.handlers.support.CommonFuncNodeToMD#cae4d3", bean);
/*  765 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  766 */     invokeInitializingBean(bean);
/*  767 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD getEAMFuncNodeToMD_74e662() {
/*  771 */     if (context.get("nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD#74e662") != null)
/*  772 */       return (nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD)context.get("nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD#74e662");
/*  773 */     nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD bean = new nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD();
/*  774 */     context.put("nc.ui.am.editor.event.handlers.support.EAMFuncNodeToMD#74e662", bean);
/*  775 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  776 */     invokeInitializingBean(bean);
/*  777 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.editor.TemplateContainer getTemplateContainer() {
/*  781 */     if (context.get("templateContainer") != null)
/*  782 */       return (nc.ui.uif2.editor.TemplateContainer)context.get("templateContainer");
/*  783 */     nc.ui.uif2.editor.TemplateContainer bean = new nc.ui.uif2.editor.TemplateContainer();
/*  784 */     context.put("templateContainer", bean);
/*  785 */     bean.setContext(getContext());
/*  786 */     bean.load();
/*  787 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  788 */     invokeInitializingBean(bean);
/*  789 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getBillDataPreparator() {
/*  793 */     if (context.get("billDataPreparator") != null)
/*  794 */       return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare)context.get("billDataPreparator");
/*  795 */     nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
/*  796 */     context.put("billDataPreparator", bean);
/*  797 */     bean.setBillDataPrepares(getManagedList17());
/*  798 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  799 */     invokeInitializingBean(bean);
/*  800 */     return bean;
/*      */   }
/*      */   
/*  803 */   private List getManagedList17() { List list = new ArrayList();list.add(getHyperlinkBillDataPreparator());list.add(getDefItemCardPreparator());return list;
/*      */   }
/*      */   
/*  806 */   public nc.ui.am.editor.prepare.HyperlinkPreparator getHyperlinkBillDataPreparator() { if (context.get("hyperlinkBillDataPreparator") != null)
/*  807 */       return (nc.ui.am.editor.prepare.HyperlinkPreparator)context.get("hyperlinkBillDataPreparator");
/*  808 */     nc.ui.am.editor.prepare.HyperlinkPreparator bean = new nc.ui.am.editor.prepare.HyperlinkPreparator();
/*  809 */     context.put("hyperlinkBillDataPreparator", bean);
/*  810 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  811 */     invokeInitializingBean(bean);
/*  812 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer() {
/*  816 */     if (context.get("userdefitemContainer") != null)
/*  817 */       return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
/*  818 */     nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
/*  819 */     context.put("userdefitemContainer", bean);
/*  820 */     bean.setContext(getContext());
/*  821 */     bean.setParams(getManagedList18());
/*  822 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  823 */     invokeInitializingBean(bean);
/*  824 */     return bean;
/*      */   }
/*      */   
/*  827 */   private List getManagedList18() { List list = new ArrayList();list.add(getQueryParam_3dbda0());list.add(getQueryParam_16c61dc());list.add(getQueryParam_8cbf8e());list.add(getQueryParam_741140());list.add(getQueryParam_188829());list.add(getQueryParam_93701c());list.add(getQueryParam_1a569a5());list.add(getQueryParam_baddd1());list.add(getQueryParam_38c8d1());list.add(getQueryParam_823eb());list.add(getQueryParam_6e1f78());list.add(getQueryParam_1d435de());list.add(getQueryParam_11cb506());list.add(getQueryParam_1b09d7c());list.add(getQueryParam_3339c8());return list;
/*      */   }
/*      */   
/*  830 */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_3dbda0() { if (context.get("nc.ui.uif2.userdefitem.QueryParam#3dbda0") != null)
/*  831 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#3dbda0");
/*  832 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  833 */     context.put("nc.ui.uif2.userdefitem.QueryParam#3dbda0", bean);
/*  834 */     bean.setMdfullname("ewm.WorkOrder");
/*  835 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  836 */     invokeInitializingBean(bean);
/*  837 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_16c61dc() {
/*  841 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#16c61dc") != null)
/*  842 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#16c61dc");
/*  843 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  844 */     context.put("nc.ui.uif2.userdefitem.QueryParam#16c61dc", bean);
/*  845 */     bean.setMdfullname("ewm.wo_task");
/*  846 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  847 */     invokeInitializingBean(bean);
/*  848 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_8cbf8e() {
/*  852 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#8cbf8e") != null)
/*  853 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#8cbf8e");
/*  854 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  855 */     context.put("nc.ui.uif2.userdefitem.QueryParam#8cbf8e", bean);
/*  856 */     bean.setMdfullname("ewm.wo_plan_psn");
/*  857 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  858 */     invokeInitializingBean(bean);
/*  859 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_741140() {
/*  863 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#741140") != null)
/*  864 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#741140");
/*  865 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  866 */     context.put("nc.ui.uif2.userdefitem.QueryParam#741140", bean);
/*  867 */     bean.setMdfullname("ewm.WOActualPsnVO");
/*  868 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  869 */     invokeInitializingBean(bean);
/*  870 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_188829() {
/*  874 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#188829") != null)
/*  875 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#188829");
/*  876 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  877 */     context.put("nc.ui.uif2.userdefitem.QueryParam#188829", bean);
/*  878 */     bean.setMdfullname("ewm.wo_plan_inv");
/*  879 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  880 */     invokeInitializingBean(bean);
/*  881 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_93701c() {
/*  885 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#93701c") != null)
/*  886 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#93701c");
/*  887 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  888 */     context.put("nc.ui.uif2.userdefitem.QueryParam#93701c", bean);
/*  889 */     bean.setMdfullname("ewm.WOActualInvVO");
/*  890 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  891 */     invokeInitializingBean(bean);
/*  892 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1a569a5() {
/*  896 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#1a569a5") != null)
/*  897 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#1a569a5");
/*  898 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  899 */     context.put("nc.ui.uif2.userdefitem.QueryParam#1a569a5", bean);
/*  900 */     bean.setMdfullname("ewm.WOPlanToolVO");
/*  901 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  902 */     invokeInitializingBean(bean);
/*  903 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_baddd1() {
/*  907 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#baddd1") != null)
/*  908 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#baddd1");
/*  909 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  910 */     context.put("nc.ui.uif2.userdefitem.QueryParam#baddd1", bean);
/*  911 */     bean.setMdfullname("ewm.WOActualToolVO");
/*  912 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  913 */     invokeInitializingBean(bean);
/*  914 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_38c8d1() {
/*  918 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#38c8d1") != null)
/*  919 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#38c8d1");
/*  920 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  921 */     context.put("nc.ui.uif2.userdefitem.QueryParam#38c8d1", bean);
/*  922 */     bean.setMdfullname("ewm.wo_plan_exes");
/*  923 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  924 */     invokeInitializingBean(bean);
/*  925 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_823eb() {
/*  929 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#823eb") != null)
/*  930 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#823eb");
/*  931 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  932 */     context.put("nc.ui.uif2.userdefitem.QueryParam#823eb", bean);
/*  933 */     bean.setMdfullname("ewm.wo_actual_exes");
/*  934 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  935 */     invokeInitializingBean(bean);
/*  936 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_6e1f78() {
/*  940 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#6e1f78") != null)
/*  941 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#6e1f78");
/*  942 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  943 */     context.put("nc.ui.uif2.userdefitem.QueryParam#6e1f78", bean);
/*  944 */     bean.setMdfullname("ewm.WOPartVO");
/*  945 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  946 */     invokeInitializingBean(bean);
/*  947 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1d435de() {
/*  951 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#1d435de") != null)
/*  952 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#1d435de");
/*  953 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  954 */     context.put("nc.ui.uif2.userdefitem.QueryParam#1d435de", bean);
/*  955 */     bean.setMdfullname("ewm.WOHisVO");
/*  956 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  957 */     invokeInitializingBean(bean);
/*  958 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_11cb506() {
/*  962 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#11cb506") != null)
/*  963 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#11cb506");
/*  964 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  965 */     context.put("nc.ui.uif2.userdefitem.QueryParam#11cb506", bean);
/*  966 */     bean.setMdfullname("ewm.WOLogVO");
/*  967 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  968 */     invokeInitializingBean(bean);
/*  969 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1b09d7c() {
/*  973 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#1b09d7c") != null)
/*  974 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#1b09d7c");
/*  975 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  976 */     context.put("nc.ui.uif2.userdefitem.QueryParam#1b09d7c", bean);
/*  977 */     bean.setMdfullname("ewm.wo_taskobj");
/*  978 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  979 */     invokeInitializingBean(bean);
/*  980 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.uif2.userdefitem.QueryParam getQueryParam_3339c8() {
/*  984 */     if (context.get("nc.ui.uif2.userdefitem.QueryParam#3339c8") != null)
/*  985 */       return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#3339c8");
/*  986 */     nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
/*  987 */     context.put("nc.ui.uif2.userdefitem.QueryParam#3339c8", bean);
/*  988 */     bean.setMdfullname("ewm.wo_precaution");
/*  989 */     setBeanFacotryIfBeanFacatoryAware(bean);
/*  990 */     invokeInitializingBean(bean);
/*  991 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.editor.UserdefitemContainerPreparator getDefItemCardPreparator() {
/*  995 */     if (context.get("defItemCardPreparator") != null)
/*  996 */       return (nc.ui.uif2.editor.UserdefitemContainerPreparator)context.get("defItemCardPreparator");
/*  997 */     nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
/*  998 */     context.put("defItemCardPreparator", bean);
/*  999 */     bean.setContainer(getUserdefitemContainer());
/* 1000 */     bean.setParams(getManagedList19());
/* 1001 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1002 */     invokeInitializingBean(bean);
/* 1003 */     return bean;
/*      */   }
/*      */   
/* 1006 */   private List getManagedList19() { List list = new ArrayList();list.add(getUserdefQueryParam_c8bc7b());list.add(getUserdefQueryParam_ce36d2());list.add(getUserdefQueryParam_7123c4());list.add(getUserdefQueryParam_1e32c93());list.add(getUserdefQueryParam_1838ed7());list.add(getUserdefQueryParam_1705a1());list.add(getUserdefQueryParam_172b200());list.add(getUserdefQueryParam_1044ef4());list.add(getUserdefQueryParam_1dba7f5());list.add(getUserdefQueryParam_1efde5e());list.add(getUserdefQueryParam_f02dea());list.add(getUserdefQueryParam_56654f());list.add(getUserdefQueryParam_154374());list.add(getUserdefQueryParam_1ffb557());list.add(getUserdefQueryParam_da763e());return list;
/*      */   }
/*      */   
/* 1009 */   private UserdefQueryParam getUserdefQueryParam_c8bc7b() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#c8bc7b") != null)
/* 1010 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#c8bc7b");
/* 1011 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1012 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#c8bc7b", bean);
/* 1013 */     bean.setMdfullname("ewm.WorkOrder");
/* 1014 */     bean.setPos(0);
/* 1015 */     bean.setPrefix("def");
/* 1016 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1017 */     invokeInitializingBean(bean);
/* 1018 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_ce36d2() {
/* 1022 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#ce36d2") != null)
/* 1023 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#ce36d2");
/* 1024 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1025 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#ce36d2", bean);
/* 1026 */     bean.setMdfullname("ewm.wo_task");
/* 1027 */     bean.setPos(1);
/* 1028 */     bean.setTabcode("wo_task");
/* 1029 */     bean.setPrefix("def");
/* 1030 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1031 */     invokeInitializingBean(bean);
/* 1032 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_7123c4() {
/* 1036 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#7123c4") != null)
/* 1037 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#7123c4");
/* 1038 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1039 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#7123c4", bean);
/* 1040 */     bean.setMdfullname("ewm.wo_plan_psn");
/* 1041 */     bean.setPos(1);
/* 1042 */     bean.setTabcode("wo_plan_psn");
/* 1043 */     bean.setPrefix("def");
/* 1044 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1045 */     invokeInitializingBean(bean);
/* 1046 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1e32c93() {
/* 1050 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1e32c93") != null)
/* 1051 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1e32c93");
/* 1052 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1053 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1e32c93", bean);
/* 1054 */     bean.setMdfullname("ewm.WOActualPsnVO");
/* 1055 */     bean.setPos(1);
/* 1056 */     bean.setTabcode("wo_actual_psn");
/* 1057 */     bean.setPrefix("def");
/* 1058 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1059 */     invokeInitializingBean(bean);
/* 1060 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1838ed7() {
/* 1064 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1838ed7") != null)
/* 1065 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1838ed7");
/* 1066 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1067 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1838ed7", bean);
/* 1068 */     bean.setMdfullname("ewm.wo_plan_inv");
/* 1069 */     bean.setPos(1);
/* 1070 */     bean.setTabcode("wo_plan_inv");
/* 1071 */     bean.setPrefix("def");
/* 1072 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1073 */     invokeInitializingBean(bean);
/* 1074 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1705a1() {
/* 1078 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1705a1") != null)
/* 1079 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1705a1");
/* 1080 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1081 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1705a1", bean);
/* 1082 */     bean.setMdfullname("ewm.WOActualInvVO");
/* 1083 */     bean.setPos(1);
/* 1084 */     bean.setTabcode("wo_actual_inv");
/* 1085 */     bean.setPrefix("def");
/* 1086 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1087 */     invokeInitializingBean(bean);
/* 1088 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_172b200() {
/* 1092 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#172b200") != null)
/* 1093 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#172b200");
/* 1094 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1095 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#172b200", bean);
/* 1096 */     bean.setMdfullname("ewm.WOPlanToolVO");
/* 1097 */     bean.setPos(1);
/* 1098 */     bean.setTabcode("wo_plan_tool");
/* 1099 */     bean.setPrefix("def");
/* 1100 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1101 */     invokeInitializingBean(bean);
/* 1102 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1044ef4() {
/* 1106 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1044ef4") != null)
/* 1107 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1044ef4");
/* 1108 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1109 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1044ef4", bean);
/* 1110 */     bean.setMdfullname("ewm.WOActualToolVO");
/* 1111 */     bean.setPos(1);
/* 1112 */     bean.setTabcode("wo_actual_tool");
/* 1113 */     bean.setPrefix("def");
/* 1114 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1115 */     invokeInitializingBean(bean);
/* 1116 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1dba7f5() {
/* 1120 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1dba7f5") != null)
/* 1121 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1dba7f5");
/* 1122 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1123 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1dba7f5", bean);
/* 1124 */     bean.setMdfullname("ewm.wo_plan_exes");
/* 1125 */     bean.setPos(1);
/* 1126 */     bean.setTabcode("wo_planotherexes");
/* 1127 */     bean.setPrefix("def");
/* 1128 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1129 */     invokeInitializingBean(bean);
/* 1130 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1efde5e() {
/* 1134 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1efde5e") != null)
/* 1135 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1efde5e");
/* 1136 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1137 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1efde5e", bean);
/* 1138 */     bean.setMdfullname("ewm.wo_actual_exes");
/* 1139 */     bean.setPos(1);
/* 1140 */     bean.setTabcode("wo_actualotherexes");
/* 1141 */     bean.setPrefix("def");
/* 1142 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1143 */     invokeInitializingBean(bean);
/* 1144 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_f02dea() {
/* 1148 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#f02dea") != null)
/* 1149 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#f02dea");
/* 1150 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1151 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#f02dea", bean);
/* 1152 */     bean.setMdfullname("ewm.WOPartVO");
/* 1153 */     bean.setPos(1);
/* 1154 */     bean.setTabcode("wo_part");
/* 1155 */     bean.setPrefix("def");
/* 1156 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1157 */     invokeInitializingBean(bean);
/* 1158 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_56654f() {
/* 1162 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#56654f") != null)
/* 1163 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#56654f");
/* 1164 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1165 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#56654f", bean);
/* 1166 */     bean.setMdfullname("ewm.WOHisVO");
/* 1167 */     bean.setPos(1);
/* 1168 */     bean.setTabcode("wo_his");
/* 1169 */     bean.setPrefix("def");
/* 1170 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1171 */     invokeInitializingBean(bean);
/* 1172 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_154374() {
/* 1176 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#154374") != null)
/* 1177 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#154374");
/* 1178 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1179 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#154374", bean);
/* 1180 */     bean.setMdfullname("ewm.WOLogVO");
/* 1181 */     bean.setPos(1);
/* 1182 */     bean.setTabcode("wo_log");
/* 1183 */     bean.setPrefix("def");
/* 1184 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1185 */     invokeInitializingBean(bean);
/* 1186 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1ffb557() {
/* 1190 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1ffb557") != null)
/* 1191 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1ffb557");
/* 1192 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1193 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1ffb557", bean);
/* 1194 */     bean.setMdfullname("ewm.wo_taskobj");
/* 1195 */     bean.setPos(1);
/* 1196 */     bean.setTabcode("wo_taskobj");
/* 1197 */     bean.setPrefix("def");
/* 1198 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1199 */     invokeInitializingBean(bean);
/* 1200 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_da763e() {
/* 1204 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#da763e") != null)
/* 1205 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#da763e");
/* 1206 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1207 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#da763e", bean);
/* 1208 */     bean.setMdfullname("ewm.wo_precaution");
/* 1209 */     bean.setPos(1);
/* 1210 */     bean.setTabcode("wo_precaution");
/* 1211 */     bean.setPrefix("def");
/* 1212 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1213 */     invokeInitializingBean(bean);
/* 1214 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.editor.UserdefitemContainerListPreparator getDefItemListPreparator() {
/* 1218 */     if (context.get("defItemListPreparator") != null)
/* 1219 */       return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("defItemListPreparator");
/* 1220 */     nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
/* 1221 */     context.put("defItemListPreparator", bean);
/* 1222 */     bean.setContainer(getUserdefitemContainer());
/* 1223 */     bean.setParams(getManagedList20());
/* 1224 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1225 */     invokeInitializingBean(bean);
/* 1226 */     return bean;
/*      */   }
/*      */   
/* 1229 */   private List getManagedList20() { List list = new ArrayList();list.add(getUserdefQueryParam_1169a65());list.add(getUserdefQueryParam_f28fb5());list.add(getUserdefQueryParam_b8e5cc());list.add(getUserdefQueryParam_f6df88());list.add(getUserdefQueryParam_1cd75e9());list.add(getUserdefQueryParam_1f81906());list.add(getUserdefQueryParam_132e11a());list.add(getUserdefQueryParam_158a7eb());list.add(getUserdefQueryParam_1783b1e());list.add(getUserdefQueryParam_7164aa());list.add(getUserdefQueryParam_880035());list.add(getUserdefQueryParam_c5c916());list.add(getUserdefQueryParam_114c3bf());list.add(getUserdefQueryParam_3f4de0());list.add(getUserdefQueryParam_11ae47());return list;
/*      */   }
/*      */   
/* 1232 */   private UserdefQueryParam getUserdefQueryParam_1169a65() { if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1169a65") != null)
/* 1233 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1169a65");
/* 1234 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1235 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1169a65", bean);
/* 1236 */     bean.setMdfullname("ewm.WorkOrder");
/* 1237 */     bean.setPos(0);
/* 1238 */     bean.setPrefix("def");
/* 1239 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1240 */     invokeInitializingBean(bean);
/* 1241 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_f28fb5() {
/* 1245 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#f28fb5") != null)
/* 1246 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#f28fb5");
/* 1247 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1248 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#f28fb5", bean);
/* 1249 */     bean.setMdfullname("ewm.wo_task");
/* 1250 */     bean.setPos(1);
/* 1251 */     bean.setTabcode("wo_task");
/* 1252 */     bean.setPrefix("def");
/* 1253 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1254 */     invokeInitializingBean(bean);
/* 1255 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_b8e5cc() {
/* 1259 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#b8e5cc") != null)
/* 1260 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#b8e5cc");
/* 1261 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1262 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#b8e5cc", bean);
/* 1263 */     bean.setMdfullname("ewm.wo_plan_psn");
/* 1264 */     bean.setPos(1);
/* 1265 */     bean.setTabcode("wo_plan_psn");
/* 1266 */     bean.setPrefix("def");
/* 1267 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1268 */     invokeInitializingBean(bean);
/* 1269 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_f6df88() {
/* 1273 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#f6df88") != null)
/* 1274 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#f6df88");
/* 1275 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1276 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#f6df88", bean);
/* 1277 */     bean.setMdfullname("ewm.WOActualPsnVO");
/* 1278 */     bean.setPos(1);
/* 1279 */     bean.setTabcode("wo_actual_psn");
/* 1280 */     bean.setPrefix("def");
/* 1281 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1282 */     invokeInitializingBean(bean);
/* 1283 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1cd75e9() {
/* 1287 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1cd75e9") != null)
/* 1288 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1cd75e9");
/* 1289 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1290 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1cd75e9", bean);
/* 1291 */     bean.setMdfullname("ewm.wo_plan_inv");
/* 1292 */     bean.setPos(1);
/* 1293 */     bean.setTabcode("wo_plan_inv");
/* 1294 */     bean.setPrefix("def");
/* 1295 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1296 */     invokeInitializingBean(bean);
/* 1297 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1f81906() {
/* 1301 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1f81906") != null)
/* 1302 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1f81906");
/* 1303 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1304 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1f81906", bean);
/* 1305 */     bean.setMdfullname("ewm.WOActualInvVO");
/* 1306 */     bean.setPos(1);
/* 1307 */     bean.setTabcode("wo_actual_inv");
/* 1308 */     bean.setPrefix("def");
/* 1309 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1310 */     invokeInitializingBean(bean);
/* 1311 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_132e11a() {
/* 1315 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#132e11a") != null)
/* 1316 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#132e11a");
/* 1317 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1318 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#132e11a", bean);
/* 1319 */     bean.setMdfullname("ewm.WOPlanToolVO");
/* 1320 */     bean.setPos(1);
/* 1321 */     bean.setTabcode("wo_plan_tool");
/* 1322 */     bean.setPrefix("def");
/* 1323 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1324 */     invokeInitializingBean(bean);
/* 1325 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_158a7eb() {
/* 1329 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#158a7eb") != null)
/* 1330 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#158a7eb");
/* 1331 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1332 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#158a7eb", bean);
/* 1333 */     bean.setMdfullname("ewm.WOActualToolVO");
/* 1334 */     bean.setPos(1);
/* 1335 */     bean.setTabcode("wo_actual_tool");
/* 1336 */     bean.setPrefix("def");
/* 1337 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1338 */     invokeInitializingBean(bean);
/* 1339 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_1783b1e() {
/* 1343 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1783b1e") != null)
/* 1344 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#1783b1e");
/* 1345 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1346 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#1783b1e", bean);
/* 1347 */     bean.setMdfullname("ewm.wo_plan_exes");
/* 1348 */     bean.setPos(1);
/* 1349 */     bean.setTabcode("wo_planotherexes");
/* 1350 */     bean.setPrefix("def");
/* 1351 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1352 */     invokeInitializingBean(bean);
/* 1353 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_7164aa() {
/* 1357 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#7164aa") != null)
/* 1358 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#7164aa");
/* 1359 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1360 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#7164aa", bean);
/* 1361 */     bean.setMdfullname("ewm.wo_actual_exes");
/* 1362 */     bean.setPos(1);
/* 1363 */     bean.setTabcode("wo_actualotherexes");
/* 1364 */     bean.setPrefix("def");
/* 1365 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1366 */     invokeInitializingBean(bean);
/* 1367 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_880035() {
/* 1371 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#880035") != null)
/* 1372 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#880035");
/* 1373 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1374 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#880035", bean);
/* 1375 */     bean.setMdfullname("ewm.WOPartVO");
/* 1376 */     bean.setPos(1);
/* 1377 */     bean.setTabcode("wo_part");
/* 1378 */     bean.setPrefix("def");
/* 1379 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1380 */     invokeInitializingBean(bean);
/* 1381 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_c5c916() {
/* 1385 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#c5c916") != null)
/* 1386 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#c5c916");
/* 1387 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1388 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#c5c916", bean);
/* 1389 */     bean.setMdfullname("ewm.WOHisVO");
/* 1390 */     bean.setPos(1);
/* 1391 */     bean.setTabcode("wo_his");
/* 1392 */     bean.setPrefix("def");
/* 1393 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1394 */     invokeInitializingBean(bean);
/* 1395 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_114c3bf() {
/* 1399 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#114c3bf") != null)
/* 1400 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#114c3bf");
/* 1401 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1402 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#114c3bf", bean);
/* 1403 */     bean.setMdfullname("ewm.WOLogVO");
/* 1404 */     bean.setPos(1);
/* 1405 */     bean.setTabcode("wo_log");
/* 1406 */     bean.setPrefix("def");
/* 1407 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1408 */     invokeInitializingBean(bean);
/* 1409 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_3f4de0() {
/* 1413 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#3f4de0") != null)
/* 1414 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#3f4de0");
/* 1415 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1416 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#3f4de0", bean);
/* 1417 */     bean.setMdfullname("ewm.wo_taskobj");
/* 1418 */     bean.setPos(1);
/* 1419 */     bean.setTabcode("wo_taskobj");
/* 1420 */     bean.setPrefix("def");
/* 1421 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1422 */     invokeInitializingBean(bean);
/* 1423 */     return bean;
/*      */   }
/*      */   
/*      */   private UserdefQueryParam getUserdefQueryParam_11ae47() {
/* 1427 */     if (context.get("nc.ui.uif2.editor.UserdefQueryParam#11ae47") != null)
/* 1428 */       return (UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#11ae47");
/* 1429 */     UserdefQueryParam bean = new UserdefQueryParam();
/* 1430 */     context.put("nc.ui.uif2.editor.UserdefQueryParam#11ae47", bean);
/* 1431 */     bean.setMdfullname("ewm.wo_precaution");
/* 1432 */     bean.setPos(1);
/* 1433 */     bean.setTabcode("wo_precaution");
/* 1434 */     bean.setPrefix("def");
/* 1435 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1436 */     invokeInitializingBean(bean);
/* 1437 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.components.pagination.PaginationBar getPaginationBar() {
/* 1441 */     if (context.get("paginationBar") != null)
/* 1442 */       return (nc.ui.uif2.components.pagination.PaginationBar)context.get("paginationBar");
/* 1443 */     nc.ui.uif2.components.pagination.PaginationBar bean = new nc.ui.uif2.components.pagination.PaginationBar(2);context.put("paginationBar", bean);
/* 1444 */     bean.setPaginationModel(getPaginationModel());
/* 1445 */     bean.setContext(getContext());
/* 1446 */     bean.registeCallbak();
/* 1447 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1448 */     invokeInitializingBean(bean);
/* 1449 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.components.pagination.PaginationModel getPaginationModel() {
/* 1453 */     if (context.get("paginationModel") != null)
/* 1454 */       return (nc.ui.uif2.components.pagination.PaginationModel)context.get("paginationModel");
/* 1455 */     nc.ui.uif2.components.pagination.PaginationModel bean = new nc.ui.uif2.components.pagination.PaginationModel();
/* 1456 */     context.put("paginationModel", bean);
/* 1457 */     bean.setPaginationQueryService(getModelService());
/* 1458 */     bean.init();
/* 1459 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1460 */     invokeInitializingBean(bean);
/* 1461 */     return bean;
/*      */   }
/*      */   
/*      */   public WorkOrderManageModel getModel() {
/* 1465 */     if (context.get("model") != null)
/* 1466 */       return (WorkOrderManageModel)context.get("model");
/* 1467 */     WorkOrderManageModel bean = new WorkOrderManageModel(getContext());context.put("model", bean);
/* 1468 */     bean.setService(getModelService());
/* 1469 */     bean.setBusinessObjectAdapterFactory(getVoBdAdapterFactory());
/* 1470 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1471 */     invokeInitializingBean(bean);
/* 1472 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.base.manager.DataModelDelegator getModelDataManager() {
/* 1476 */     if (context.get("modelDataManager") != null)
/* 1477 */       return (nc.ui.am.base.manager.DataModelDelegator)context.get("modelDataManager");
/* 1478 */     nc.ui.am.base.manager.DataModelDelegator bean = new nc.ui.am.base.manager.DataModelDelegator();
/* 1479 */     context.put("modelDataManager", bean);
/* 1480 */     bean.setModel(getModel());
/* 1481 */     bean.setQueryDialogManager(getQueryDialogManager());
/* 1482 */     bean.setPaginationModel(getPaginationModel());
/* 1483 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1484 */     invokeInitializingBean(bean);
/* 1485 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.query.WOQueryDialogManager getQueryDialogManager() {
/* 1489 */     if (context.get("queryDialogManager") != null)
/* 1490 */       return (nc.ui.ewm.workorder.query.WOQueryDialogManager)context.get("queryDialogManager");
/* 1491 */     nc.ui.ewm.workorder.query.WOQueryDialogManager bean = new nc.ui.ewm.workorder.query.WOQueryDialogManager();
/* 1492 */     context.put("queryDialogManager", bean);
/* 1493 */     bean.setOrgType("MAINTAINORGTYPE00000");
/* 1494 */     bean.setModel(getModel());
/* 1495 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1496 */     invokeInitializingBean(bean);
/* 1497 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.view.WOFuncNodeInitDataAdapter getInitDataListener() {
/* 1501 */     if (context.get("InitDataListener") != null)
/* 1502 */       return (nc.ui.ewm.workorder.view.WOFuncNodeInitDataAdapter)context.get("InitDataListener");
/* 1503 */     nc.ui.ewm.workorder.view.WOFuncNodeInitDataAdapter bean = new nc.ui.ewm.workorder.view.WOFuncNodeInitDataAdapter();
/* 1504 */     context.put("InitDataListener", bean);
/* 1505 */     bean.setModelDataManager(getModelDataManager());
/* 1506 */     bean.setQueryAction(getQueryAction());
/* 1507 */     bean.setAddAction(getAddAction());
/* 1508 */     bean.setBillForm(getBillForm());
/* 1509 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1510 */     invokeInitializingBean(bean);
/* 1511 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.FunNodeClosingHandler getClosingListener() {
/* 1515 */     if (context.get("ClosingListener") != null)
/* 1516 */       return (nc.ui.uif2.FunNodeClosingHandler)context.get("ClosingListener");
/* 1517 */     nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
/* 1518 */     context.put("ClosingListener", bean);
/* 1519 */     bean.setModel(getModel());
/* 1520 */     bean.setSaveaction(getSaveAction());
/* 1521 */     bean.setCancelaction(getCancelAction());
/* 1522 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1523 */     invokeInitializingBean(bean);
/* 1524 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.vo.bd.meta.VOBDObjectFactory getVoBdAdapterFactory() {
/* 1528 */     if (context.get("voBdAdapterFactory") != null)
/* 1529 */       return (nc.vo.bd.meta.VOBDObjectFactory)context.get("voBdAdapterFactory");
/* 1530 */     nc.vo.bd.meta.VOBDObjectFactory bean = new nc.vo.bd.meta.VOBDObjectFactory();
/* 1531 */     context.put("voBdAdapterFactory", bean);
/* 1532 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1533 */     invokeInitializingBean(bean);
/* 1534 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.vo.am.scale.process.ScaleProcessCenter getBillCardDigitProcessor() {
/* 1538 */     if (context.get("billCardDigitProcessor") != null)
/* 1539 */       return (nc.vo.am.scale.process.ScaleProcessCenter)context.get("billCardDigitProcessor");
/* 1540 */     nc.vo.am.scale.process.ScaleProcessCenter bean = new nc.vo.am.scale.process.ScaleProcessCenter();
/* 1541 */     context.put("billCardDigitProcessor", bean);
/* 1542 */     bean.setScaleObjectData(getScaleObjectData());
/* 1543 */     bean.setScaleProcessor(getCardScaleSet());
/* 1544 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1545 */     invokeInitializingBean(bean);
/* 1546 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scale.process.ScaleProcessorCardPanel getCardScaleSet() {
/* 1550 */     if (context.get("cardScaleSet") != null)
/* 1551 */       return (nc.ui.am.scale.process.ScaleProcessorCardPanel)context.get("cardScaleSet");
/* 1552 */     nc.ui.am.scale.process.ScaleProcessorCardPanel bean = new nc.ui.am.scale.process.ScaleProcessorCardPanel();
/* 1553 */     context.put("cardScaleSet", bean);
/* 1554 */     bean.setBillForm(getBillForm());
/* 1555 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1556 */     invokeInitializingBean(bean);
/* 1557 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.vo.am.scale.process.ScaleProcessCenter getBillListDigitProcessor() {
/* 1561 */     if (context.get("billListDigitProcessor") != null)
/* 1562 */       return (nc.vo.am.scale.process.ScaleProcessCenter)context.get("billListDigitProcessor");
/* 1563 */     nc.vo.am.scale.process.ScaleProcessCenter bean = new nc.vo.am.scale.process.ScaleProcessCenter();
/* 1564 */     context.put("billListDigitProcessor", bean);
/* 1565 */     bean.setScaleObjectData(getScaleObjectData());
/* 1566 */     bean.setScaleProcessor(getListScaleSet());
/* 1567 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1568 */     invokeInitializingBean(bean);
/* 1569 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scale.process.ScaleProcessListPanel getListScaleSet() {
/* 1573 */     if (context.get("listScaleSet") != null)
/* 1574 */       return (nc.ui.am.scale.process.ScaleProcessListPanel)context.get("listScaleSet");
/* 1575 */     nc.ui.am.scale.process.ScaleProcessListPanel bean = new nc.ui.am.scale.process.ScaleProcessListPanel();
/* 1576 */     context.put("listScaleSet", bean);
/* 1577 */     bean.setListView(getBillListView());
/* 1578 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1579 */     invokeInitializingBean(bean);
/* 1580 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.vo.am.scale.process.ScaleObjectData getScaleObjectData() {
/* 1584 */     if (context.get("scaleObjectData") != null)
/* 1585 */       return (nc.vo.am.scale.process.ScaleObjectData)context.get("scaleObjectData");
/* 1586 */     nc.vo.am.scale.process.ScaleObjectData bean = new nc.vo.am.scale.process.ScaleObjectData();
/* 1587 */     context.put("scaleObjectData", bean);
/* 1588 */     bean.setDigitMatcher(getDigitMatcher());
/* 1589 */     bean.setScaleObjLoader(getSetScaleFields());
/* 1590 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1591 */     invokeInitializingBean(bean);
/* 1592 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getDigitMatcher() {
/* 1596 */     if (context.get("digitMatcher") != null)
/* 1597 */       return (ArrayList)context.get("digitMatcher");
/* 1598 */     ArrayList bean = new ArrayList(getManagedList21());context.put("digitMatcher", bean);
/* 1599 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1600 */     invokeInitializingBean(bean);
/* 1601 */     return bean;
/*      */   }
/*      */   
/* 1604 */   private List getManagedList21() { List list = new ArrayList();list.add(getScaleTypeVO_c16cca());return list;
/*      */   }
/*      */   
/* 1607 */   private nc.vo.am.scale.process.ScaleTypeVO getScaleTypeVO_c16cca() { if (context.get("nc.vo.am.scale.process.ScaleTypeVO#c16cca") != null)
/* 1608 */       return (nc.vo.am.scale.process.ScaleTypeVO)context.get("nc.vo.am.scale.process.ScaleTypeVO#c16cca");
/* 1609 */     nc.vo.am.scale.process.ScaleTypeVO bean = new nc.vo.am.scale.process.ScaleTypeVO();
/* 1610 */     context.put("nc.vo.am.scale.process.ScaleTypeVO#c16cca", bean);
/* 1611 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1612 */     invokeInitializingBean(bean);
/* 1613 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.view.WOSetScaleFields getSetScaleFields() {
/* 1617 */     if (context.get("setScaleFields") != null)
/* 1618 */       return (nc.ui.ewm.workorder.view.WOSetScaleFields)context.get("setScaleFields");
/* 1619 */     nc.ui.ewm.workorder.view.WOSetScaleFields bean = new nc.ui.ewm.workorder.view.WOSetScaleFields();
/* 1620 */     context.put("setScaleFields", bean);
/* 1621 */     bean.setBillForm(getBillForm());
/* 1622 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1623 */     invokeInitializingBean(bean);
/* 1624 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.code.BillCodeMediator getBillCodeMediator() {
/* 1628 */     if (context.get("billCodeMediator") != null)
/* 1629 */       return (nc.ui.am.code.BillCodeMediator)context.get("billCodeMediator");
/* 1630 */     nc.ui.am.code.BillCodeMediator bean = new nc.ui.am.code.BillCodeMediator();
/* 1631 */     context.put("billCodeMediator", bean);
/* 1632 */     bean.setBillCodeKey("bill_code");
/* 1633 */     bean.setModel(getModel());
/* 1634 */     bean.setBillForm(getBillForm());
/* 1635 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1636 */     invokeInitializingBean(bean);
/* 1637 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBillType() {
/* 1641 */     if (context.get("billType") != null)
/* 1642 */       return (String)context.get("billType");
/* 1643 */     String bean = new String("4B36");context.put("billType", bean);
/* 1644 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1645 */     invokeInitializingBean(bean);
/* 1646 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabTaskObj() {
/* 1650 */     if (context.get("bodytabTaskObj") != null)
/* 1651 */       return (String)context.get("bodytabTaskObj");
/* 1652 */     String bean = new String("wo_taskobj");context.put("bodytabTaskObj", bean);
/* 1653 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1654 */     invokeInitializingBean(bean);
/* 1655 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabPlanInv() {
/* 1659 */     if (context.get("bodytabPlanInv") != null)
/* 1660 */       return (String)context.get("bodytabPlanInv");
/* 1661 */     String bean = new String("wo_plan_inv");context.put("bodytabPlanInv", bean);
/* 1662 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1663 */     invokeInitializingBean(bean);
/* 1664 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabActualInv() {
/* 1668 */     if (context.get("bodytabActualInv") != null)
/* 1669 */       return (String)context.get("bodytabActualInv");
/* 1670 */     String bean = new String("wo_actual_inv");context.put("bodytabActualInv", bean);
/* 1671 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1672 */     invokeInitializingBean(bean);
/* 1673 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabPrecaution() {
/* 1677 */     if (context.get("bodytabPrecaution") != null)
/* 1678 */       return (String)context.get("bodytabPrecaution");
/* 1679 */     String bean = new String("wo_precaution");context.put("bodytabPrecaution", bean);
/* 1680 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1681 */     invokeInitializingBean(bean);
/* 1682 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabPermit() {
/* 1686 */     if (context.get("bodytabPermit") != null)
/* 1687 */       return (String)context.get("bodytabPermit");
/* 1688 */     String bean = new String("wo_permit");context.put("bodytabPermit", bean);
/* 1689 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1690 */     invokeInitializingBean(bean);
/* 1691 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabPlanPsn() {
/* 1695 */     if (context.get("bodytabPlanPsn") != null)
/* 1696 */       return (String)context.get("bodytabPlanPsn");
/* 1697 */     String bean = new String("wo_plan_psn");context.put("bodytabPlanPsn", bean);
/* 1698 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1699 */     invokeInitializingBean(bean);
/* 1700 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabActualPsn() {
/* 1704 */     if (context.get("bodytabActualPsn") != null)
/* 1705 */       return (String)context.get("bodytabActualPsn");
/* 1706 */     String bean = new String("wo_actual_psn");context.put("bodytabActualPsn", bean);
/* 1707 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1708 */     invokeInitializingBean(bean);
/* 1709 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabPlanTool() {
/* 1713 */     if (context.get("bodytabPlanTool") != null)
/* 1714 */       return (String)context.get("bodytabPlanTool");
/* 1715 */     String bean = new String("wo_plan_tool");context.put("bodytabPlanTool", bean);
/* 1716 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1717 */     invokeInitializingBean(bean);
/* 1718 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabActualTool() {
/* 1722 */     if (context.get("bodytabActualTool") != null)
/* 1723 */       return (String)context.get("bodytabActualTool");
/* 1724 */     String bean = new String("wo_actual_tool");context.put("bodytabActualTool", bean);
/* 1725 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1726 */     invokeInitializingBean(bean);
/* 1727 */     return bean;
/*      */   }
/*      */   
/*      */   public String getBodytabStructure() {
/* 1731 */     if (context.get("bodytabStructure") != null)
/* 1732 */       return (String)context.get("bodytabStructure");
/* 1733 */     String bean = new String("wo_structure");context.put("bodytabStructure", bean);
/* 1734 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1735 */     invokeInitializingBean(bean);
/* 1736 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.funcnode.ui.action.SeparatorAction getSeparatorAction() {
/* 1740 */     if (context.get("separatorAction") != null)
/* 1741 */       return (nc.funcnode.ui.action.SeparatorAction)context.get("separatorAction");
/* 1742 */     nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
/* 1743 */     context.put("separatorAction", bean);
/* 1744 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1745 */     invokeInitializingBean(bean);
/* 1746 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.AddAction getAddAction() {
/* 1750 */     if (context.get("addAction") != null)
/* 1751 */       return (nc.ui.ewm.workorder.action.AddAction)context.get("addAction");
/* 1752 */     nc.ui.ewm.workorder.action.AddAction bean = new nc.ui.ewm.workorder.action.AddAction();
/* 1753 */     context.put("addAction", bean);
/* 1754 */     bean.setBillForm(getBillForm());
/* 1755 */     bean.setModel(getModel());
/* 1756 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1757 */     invokeInitializingBean(bean);
/* 1758 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.EditAction getEditAction() {
/* 1762 */     if (context.get("editAction") != null)
/* 1763 */       return (nc.ui.ewm.workorder.action.EditAction)context.get("editAction");
/* 1764 */     nc.ui.ewm.workorder.action.EditAction bean = new nc.ui.ewm.workorder.action.EditAction();
/* 1765 */     context.put("editAction", bean);
/* 1766 */     bean.setBillForm(getBillForm());
/* 1767 */     bean.setModel(getModel());
/* 1768 */     bean.setValidators(getManagedList22());
/* 1769 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1770 */     invokeInitializingBean(bean);
/* 1771 */     return bean;
/*      */   }
/*      */   
/* 1774 */   private List getManagedList22() { List list = new ArrayList();list.add(getUIPermissionValidator_17aa46c());list.add(getEditValidator_c0e073());return list;
/*      */   }
/*      */   
/* 1777 */   private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_17aa46c() { if (context.get("nc.ui.am.validator.UIPermissionValidator#17aa46c") != null)
/* 1778 */       return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#17aa46c");
/* 1779 */     nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
/* 1780 */     context.put("nc.ui.am.validator.UIPermissionValidator#17aa46c", bean);
/* 1781 */     bean.setContext(getContext());
/* 1782 */     bean.setMeta_code("Edit");
/* 1783 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1784 */     invokeInitializingBean(bean);
/* 1785 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.validator.EditValidator getEditValidator_c0e073() {
/* 1789 */     if (context.get("nc.ui.ewm.workorder.validator.EditValidator#c0e073") != null)
/* 1790 */       return (nc.ui.ewm.workorder.validator.EditValidator)context.get("nc.ui.ewm.workorder.validator.EditValidator#c0e073");
/* 1791 */     nc.ui.ewm.workorder.validator.EditValidator bean = new nc.ui.ewm.workorder.validator.EditValidator();
/* 1792 */     context.put("nc.ui.ewm.workorder.validator.EditValidator#c0e073", bean);
/* 1793 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1794 */     invokeInitializingBean(bean);
/* 1795 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMBatchAlterAction getBatchAlterAction() {
/* 1799 */     if (context.get("batchAlterAction") != null)
/* 1800 */       return (nc.ui.am.action.support.AMBatchAlterAction)context.get("batchAlterAction");
/* 1801 */     nc.ui.am.action.support.AMBatchAlterAction bean = new nc.ui.am.action.support.AMBatchAlterAction();
/* 1802 */     context.put("batchAlterAction", bean);
/* 1803 */     bean.setModel(getModel());
/* 1804 */     bean.setBillForm(getBillForm());
/* 1805 */     bean.setFilter(getFilter());
/* 1806 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1807 */     invokeInitializingBean(bean);
/* 1808 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.common.DefaultBatchAlterFilter getFilter() {
/* 1812 */     if (context.get("filter") != null)
/* 1813 */       return (nc.ui.am.common.DefaultBatchAlterFilter)context.get("filter");
/* 1814 */     nc.ui.am.common.DefaultBatchAlterFilter bean = new nc.ui.am.common.DefaultBatchAlterFilter();
/* 1815 */     context.put("filter", bean);
/* 1816 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1817 */     invokeInitializingBean(bean);
/* 1818 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMAddLineAction getAddLineAction() {
/* 1822 */     if (context.get("addLineAction") != null)
/* 1823 */       return (nc.ui.am.action.support.AMAddLineAction)context.get("addLineAction");
/* 1824 */     nc.ui.am.action.support.AMAddLineAction bean = new nc.ui.am.action.support.AMAddLineAction();
/* 1825 */     context.put("addLineAction", bean);
/* 1826 */     bean.setModel(getModel());
/* 1827 */     bean.setBillForm(getBillForm());
/* 1828 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1829 */     invokeInitializingBean(bean);
/* 1830 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMDeleteLineAction getDeleteLineAction() {
/* 1834 */     if (context.get("deleteLineAction") != null)
/* 1835 */       return (nc.ui.am.action.support.AMDeleteLineAction)context.get("deleteLineAction");
/* 1836 */     nc.ui.am.action.support.AMDeleteLineAction bean = new nc.ui.am.action.support.AMDeleteLineAction();
/* 1837 */     context.put("deleteLineAction", bean);
/* 1838 */     bean.setModel(getModel());
/* 1839 */     bean.setBillForm(getBillForm());
/* 1840 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1841 */     invokeInitializingBean(bean);
/* 1842 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMInsertLineAction getInsertLineAction() {
/* 1846 */     if (context.get("insertLineAction") != null)
/* 1847 */       return (nc.ui.am.action.support.AMInsertLineAction)context.get("insertLineAction");
/* 1848 */     nc.ui.am.action.support.AMInsertLineAction bean = new nc.ui.am.action.support.AMInsertLineAction();
/* 1849 */     context.put("insertLineAction", bean);
/* 1850 */     bean.setModel(getModel());
/* 1851 */     bean.setBillForm(getBillForm());
/* 1852 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1853 */     invokeInitializingBean(bean);
/* 1854 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.LineTableEditAction getTableEditLineAction() {
/* 1858 */     if (context.get("tableEditLineAction") != null)
/* 1859 */       return (nc.ui.ewm.workorder.tabaction.LineTableEditAction)context.get("tableEditLineAction");
/* 1860 */     nc.ui.ewm.workorder.tabaction.LineTableEditAction bean = new nc.ui.ewm.workorder.tabaction.LineTableEditAction();
/* 1861 */     context.put("tableEditLineAction", bean);
/* 1862 */     bean.setModel(getModel());
/* 1863 */     bean.setBillForm(getBillForm());
/* 1864 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1865 */     invokeInitializingBean(bean);
/* 1866 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMCancelAction getCancelAction() {
/* 1870 */     if (context.get("cancelAction") != null)
/* 1871 */       return (nc.ui.am.action.support.AMCancelAction)context.get("cancelAction");
/* 1872 */     nc.ui.am.action.support.AMCancelAction bean = new nc.ui.am.action.support.AMCancelAction();
/* 1873 */     context.put("cancelAction", bean);
/* 1874 */     bean.setModel(getModel());
/* 1875 */     bean.setBillForm(getBillForm());
/* 1876 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1877 */     invokeInitializingBean(bean);
/* 1878 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.SaveAction getSaveAction() {
/* 1882 */     if (context.get("saveAction") != null)
/* 1883 */       return (nc.ui.ewm.workorder.action.SaveAction)context.get("saveAction");
/* 1884 */     nc.ui.ewm.workorder.action.SaveAction bean = new nc.ui.ewm.workorder.action.SaveAction();
/* 1885 */     context.put("saveAction", bean);
/* 1886 */     bean.setModel(getModel());
/* 1887 */     bean.setBillForm(getBillForm());
/* 1888 */     bean.setValidators(getManagedList23());
/* 1889 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1890 */     invokeInitializingBean(bean);
/* 1891 */     return bean;
/*      */   }
/*      */   
/* 1894 */   private List getManagedList23() { List list = new ArrayList();list.add(getDataNotNullValidator_38a35d());list.add(getSaveValidator_6d35f6());return list;
/*      */   }
/*      */   
/* 1897 */   private nc.ui.am.validator.DataNotNullValidator getDataNotNullValidator_38a35d() { if (context.get("nc.ui.am.validator.DataNotNullValidator#38a35d") != null)
/* 1898 */       return (nc.ui.am.validator.DataNotNullValidator)context.get("nc.ui.am.validator.DataNotNullValidator#38a35d");
/* 1899 */     nc.ui.am.validator.DataNotNullValidator bean = new nc.ui.am.validator.DataNotNullValidator();
/* 1900 */     context.put("nc.ui.am.validator.DataNotNullValidator#38a35d", bean);
/* 1901 */     bean.setBillForm(getBillForm());
/* 1902 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1903 */     invokeInitializingBean(bean);
/* 1904 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.validator.SaveValidator getSaveValidator_6d35f6() {
/* 1908 */     if (context.get("nc.ui.ewm.workorder.validator.SaveValidator#6d35f6") != null)
/* 1909 */       return (nc.ui.ewm.workorder.validator.SaveValidator)context.get("nc.ui.ewm.workorder.validator.SaveValidator#6d35f6");
/* 1910 */     nc.ui.ewm.workorder.validator.SaveValidator bean = new nc.ui.ewm.workorder.validator.SaveValidator();
/* 1911 */     context.put("nc.ui.ewm.workorder.validator.SaveValidator#6d35f6", bean);
/* 1912 */     bean.setBillForm(getBillForm());
/* 1913 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1914 */     invokeInitializingBean(bean);
/* 1915 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMSaveAddAction getSaveAddAction() {
/* 1919 */     if (context.get("saveAddAction") != null)
/* 1920 */       return (nc.ui.am.action.support.AMSaveAddAction)context.get("saveAddAction");
/* 1921 */     nc.ui.am.action.support.AMSaveAddAction bean = new nc.ui.am.action.support.AMSaveAddAction();
/* 1922 */     context.put("saveAddAction", bean);
/* 1923 */     bean.setSaveAction(getSaveAction());
/* 1924 */     bean.setAddAction(getAddAction());
/* 1925 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1926 */     invokeInitializingBean(bean);
/* 1927 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMSaveCommitAction getSaveCommitAction() {
/* 1931 */     if (context.get("saveCommitAction") != null)
/* 1932 */       return (nc.ui.am.action.support.AMSaveCommitAction)context.get("saveCommitAction");
/* 1933 */     nc.ui.am.action.support.AMSaveCommitAction bean = new nc.ui.am.action.support.AMSaveCommitAction();
/* 1934 */     context.put("saveCommitAction", bean);
/* 1935 */     bean.setBillForm(getBillForm());
/* 1936 */     bean.setModel(getModel());
/* 1937 */     bean.setSaveAction(getSaveAction());
/* 1938 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1939 */     invokeInitializingBean(bean);
/* 1940 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.DeleteAction getDeleteAction() {
/* 1944 */     if (context.get("deleteAction") != null)
/* 1945 */       return (nc.ui.ewm.workorder.action.DeleteAction)context.get("deleteAction");
/* 1946 */     nc.ui.ewm.workorder.action.DeleteAction bean = new nc.ui.ewm.workorder.action.DeleteAction();
/* 1947 */     context.put("deleteAction", bean);
/* 1948 */     bean.setBillForm(getBillForm());
/* 1949 */     bean.setModel(getModel());
/* 1950 */     bean.setValidators(getManagedList24());
/* 1951 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1952 */     invokeInitializingBean(bean);
/* 1953 */     return bean;
/*      */   }
/*      */   
/* 1956 */   private List getManagedList24() { List list = new ArrayList();list.add(getUIPermissionValidator_cfbc27());list.add(getDeleteValidator_49cbd6());list.add(getOrgPermissionValidator());return list;
/*      */   }
/*      */   
/* 1959 */   private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_cfbc27() { if (context.get("nc.ui.am.validator.UIPermissionValidator#cfbc27") != null)
/* 1960 */       return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#cfbc27");
/* 1961 */     nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
/* 1962 */     context.put("nc.ui.am.validator.UIPermissionValidator#cfbc27", bean);
/* 1963 */     bean.setContext(getContext());
/* 1964 */     bean.setMeta_code("Delete");
/* 1965 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1966 */     invokeInitializingBean(bean);
/* 1967 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.validator.DeleteValidator getDeleteValidator_49cbd6() {
/* 1971 */     if (context.get("nc.ui.ewm.workorder.validator.DeleteValidator#49cbd6") != null)
/* 1972 */       return (nc.ui.ewm.workorder.validator.DeleteValidator)context.get("nc.ui.ewm.workorder.validator.DeleteValidator#49cbd6");
/* 1973 */     nc.ui.ewm.workorder.validator.DeleteValidator bean = new nc.ui.ewm.workorder.validator.DeleteValidator();
/* 1974 */     context.put("nc.ui.ewm.workorder.validator.DeleteValidator#49cbd6", bean);
/* 1975 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1976 */     invokeInitializingBean(bean);
/* 1977 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMBillViewMaxAction getHeadViewMaxAction() {
/* 1981 */     if (context.get("headViewMaxAction") != null)
/* 1982 */       return (nc.ui.am.action.support.AMBillViewMaxAction)context.get("headViewMaxAction");
/* 1983 */     nc.ui.am.action.support.AMBillViewMaxAction bean = new nc.ui.am.action.support.AMBillViewMaxAction();
/* 1984 */     context.put("headViewMaxAction", bean);
/* 1985 */     bean.setModel(getModel());
/* 1986 */     bean.setBillForm(getBillForm());
/* 1987 */     bean.setPos(0);
/* 1988 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 1989 */     invokeInitializingBean(bean);
/* 1990 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMBillViewMaxAction getBodyViewMaxAction() {
/* 1994 */     if (context.get("bodyViewMaxAction") != null)
/* 1995 */       return (nc.ui.am.action.support.AMBillViewMaxAction)context.get("bodyViewMaxAction");
/* 1996 */     nc.ui.am.action.support.AMBillViewMaxAction bean = new nc.ui.am.action.support.AMBillViewMaxAction();
/* 1997 */     context.put("bodyViewMaxAction", bean);
/* 1998 */     bean.setModel(getModel());
/* 1999 */     bean.setBillForm(getBillForm());
/* 2000 */     bean.setPos(1);
/* 2001 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2002 */     invokeInitializingBean(bean);
/* 2003 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.WorkOrderQueryAction getQueryAction() {
/* 2007 */     if (context.get("queryAction") != null)
/* 2008 */       return (nc.ui.ewm.workorder.action.WorkOrderQueryAction)context.get("queryAction");
/* 2009 */     nc.ui.ewm.workorder.action.WorkOrderQueryAction bean = new nc.ui.ewm.workorder.action.WorkOrderQueryAction();
/* 2010 */     context.put("queryAction", bean);
/* 2011 */     bean.setModel(getModel());
/* 2012 */     bean.setDataManager(getModelDataManager());
/* 2013 */     bean.setTemplateContainer(getQueryTemplateContainer());
/* 2014 */     bean.setQueryDelegator(getAMDefaultQueryDelegator_e57ce9());
/* 2015 */     bean.setOrgType(getMainorgType());
/* 2016 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2017 */     invokeInitializingBean(bean);
/* 2018 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.action.query.AMDefaultQueryDelegator getAMDefaultQueryDelegator_e57ce9() {
/* 2022 */     if (context.get("nc.ui.am.action.query.AMDefaultQueryDelegator#e57ce9") != null)
/* 2023 */       return (nc.ui.am.action.query.AMDefaultQueryDelegator)context.get("nc.ui.am.action.query.AMDefaultQueryDelegator#e57ce9");
/* 2024 */     nc.ui.am.action.query.AMDefaultQueryDelegator bean = new nc.ui.am.action.query.AMDefaultQueryDelegator();
/* 2025 */     context.put("nc.ui.am.action.query.AMDefaultQueryDelegator#e57ce9", bean);
/* 2026 */     bean.setContext(getContext());
/* 2027 */     bean.setBaseQueryManager(getQueryDialogManager());
/* 2028 */     bean.setAutoShowUpComponent(getBillListView());
/* 2029 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2030 */     invokeInitializingBean(bean);
/* 2031 */     return bean;
/*      */   }
/*      */   
/*      */   public String getMainorgType() {
/* 2035 */     if (context.get("mainorgType") != null)
/* 2036 */       return (String)context.get("mainorgType");
/* 2037 */     nc.ui.am.config.VarString bean = new nc.ui.am.config.VarString();
/* 2038 */     context.put("&mainorgType", bean);bean.setValueStr("");
/* 2039 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2040 */     invokeInitializingBean(bean);
/*      */     try {
/* 2042 */       Object product = bean.getObject();
/* 2043 */       context.put("mainorgType", product);
/* 2044 */       return (String)product;
/*      */     } catch (Exception e) {
/* 2046 */       throw new RuntimeException(e);
/*      */     } }
/*      */   
/* 2049 */   public nc.ui.am.action.support.AMRefreshAllAction getRefreshAllAction() { if (context.get("refreshAllAction") != null)
/* 2050 */       return (nc.ui.am.action.support.AMRefreshAllAction)context.get("refreshAllAction");
/* 2051 */     nc.ui.am.action.support.AMRefreshAllAction bean = new nc.ui.am.action.support.AMRefreshAllAction();
/* 2052 */     context.put("refreshAllAction", bean);
/* 2053 */     bean.setModel(getModel());
/* 2054 */     bean.setDataManager(getModelDataManager());
/* 2055 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2056 */     invokeInitializingBean(bean);
/* 2057 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMRefreshAction getRefreshAction() {
/* 2061 */     if (context.get("refreshAction") != null)
/* 2062 */       return (nc.ui.am.action.support.AMRefreshAction)context.get("refreshAction");
/* 2063 */     nc.ui.am.action.support.AMRefreshAction bean = new nc.ui.am.action.support.AMRefreshAction();
/* 2064 */     context.put("refreshAction", bean);
/* 2065 */     bean.setModel(getModel());
/* 2066 */     bean.setDataManager(getModelDataManager());
/* 2067 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2068 */     invokeInitializingBean(bean);
/* 2069 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.funcnode.ui.action.GroupAction getApproveActionGroup() {
/* 2073 */     if (context.get("approveActionGroup") != null)
/* 2074 */       return (nc.funcnode.ui.action.GroupAction)context.get("approveActionGroup");
/* 2075 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 2076 */     context.put("approveActionGroup", bean);
/* 2077 */     bean.setActions(getManagedList25());
/* 2078 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2079 */     invokeInitializingBean(bean);
/* 2080 */     return bean;
/*      */   }
/*      */   
/* 2083 */   private List getManagedList25() { List list = new ArrayList();list.add(getApproveAction());list.add(getUnApproveAction());list.add(getSeparatorAction());list.add(getQueryApproveflowAction());return list;
/*      */   }
/*      */   
/* 2086 */   public nc.funcnode.ui.action.GroupAction getCommitGroupAction() { if (context.get("commitGroupAction") != null)
/* 2087 */       return (nc.funcnode.ui.action.GroupAction)context.get("commitGroupAction");
/* 2088 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 2089 */     context.put("commitGroupAction", bean);
/* 2090 */     bean.setActions(getManagedList26());
/* 2091 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2092 */     invokeInitializingBean(bean);
/* 2093 */     return bean;
/*      */   }
/*      */   
/* 2096 */   private List getManagedList26() { List list = new ArrayList();list.add(getWfstartAction());list.add(getWfRecallAction());return list;
/*      */   }
/*      */   
/* 2099 */   public nc.ui.am.action.support.AMCommitAction getCommitAction() { if (context.get("commitAction") != null)
/* 2100 */       return (nc.ui.am.action.support.AMCommitAction)context.get("commitAction");
/* 2101 */     nc.ui.am.action.support.AMCommitAction bean = new nc.ui.am.action.support.AMCommitAction();
/* 2102 */     context.put("commitAction", bean);
/* 2103 */     bean.setBillForm(getBillForm());
/* 2104 */     bean.setModel(getModel());
/* 2105 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2106 */     invokeInitializingBean(bean);
/* 2107 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMUnCommitAction getUnCommitAction() {
/* 2111 */     if (context.get("unCommitAction") != null)
/* 2112 */       return (nc.ui.am.action.support.AMUnCommitAction)context.get("unCommitAction");
/* 2113 */     nc.ui.am.action.support.AMUnCommitAction bean = new nc.ui.am.action.support.AMUnCommitAction();
/* 2114 */     context.put("unCommitAction", bean);
/* 2115 */     bean.setModel(getModel());
/* 2116 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2117 */     invokeInitializingBean(bean);
/* 2118 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMApproveAction getApproveAction() {
/* 2122 */     if (context.get("approveAction") != null)
/* 2123 */       return (nc.ui.am.action.support.AMApproveAction)context.get("approveAction");
/* 2124 */     nc.ui.am.action.support.AMApproveAction bean = new nc.ui.am.action.support.AMApproveAction();
/* 2125 */     context.put("approveAction", bean);
/* 2126 */     bean.setBillForm(getBillForm());
/* 2127 */     bean.setModel(getModel());
/* 2128 */     bean.setValidators(getManagedList27());
/* 2129 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2130 */     invokeInitializingBean(bean);
/* 2131 */     return bean;
/*      */   }
/*      */   
/* 2134 */   private List getManagedList27() { List list = new ArrayList();list.add(getApproveBillDateValidator_1b364ef());list.add(getUIPermissionValidator_a7b6f8());return list;
/*      */   }
/*      */   
/* 2137 */   private nc.ui.am.validator.ApproveBillDateValidator getApproveBillDateValidator_1b364ef() { if (context.get("nc.ui.am.validator.ApproveBillDateValidator#1b364ef") != null)
/* 2138 */       return (nc.ui.am.validator.ApproveBillDateValidator)context.get("nc.ui.am.validator.ApproveBillDateValidator#1b364ef");
/* 2139 */     nc.ui.am.validator.ApproveBillDateValidator bean = new nc.ui.am.validator.ApproveBillDateValidator();
/* 2140 */     context.put("nc.ui.am.validator.ApproveBillDateValidator#1b364ef", bean);
/* 2141 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2142 */     invokeInitializingBean(bean);
/* 2143 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_a7b6f8() {
/* 2147 */     if (context.get("nc.ui.am.validator.UIPermissionValidator#a7b6f8") != null)
/* 2148 */       return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#a7b6f8");
/* 2149 */     nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
/* 2150 */     context.put("nc.ui.am.validator.UIPermissionValidator#a7b6f8", bean);
/* 2151 */     bean.setContext(getContext());
/* 2152 */     bean.setMeta_code("AMApprove");
/* 2153 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2154 */     invokeInitializingBean(bean);
/* 2155 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMUnApproveAction getUnApproveAction() {
/* 2159 */     if (context.get("unApproveAction") != null)
/* 2160 */       return (nc.ui.am.action.support.AMUnApproveAction)context.get("unApproveAction");
/* 2161 */     nc.ui.am.action.support.AMUnApproveAction bean = new nc.ui.am.action.support.AMUnApproveAction();
/* 2162 */     context.put("unApproveAction", bean);
/* 2163 */     bean.setModel(getModel());
/* 2164 */     bean.setValidators(getManagedList28());
/* 2165 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2166 */     invokeInitializingBean(bean);
/* 2167 */     return bean;
/*      */   }
/*      */   
/* 2170 */   private List getManagedList28() { List list = new ArrayList();list.add(getUIPermissionValidator_dd6aaf());return list;
/*      */   }
/*      */   
/* 2173 */   private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_dd6aaf() { if (context.get("nc.ui.am.validator.UIPermissionValidator#dd6aaf") != null)
/* 2174 */       return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#dd6aaf");
/* 2175 */     nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
/* 2176 */     context.put("nc.ui.am.validator.UIPermissionValidator#dd6aaf", bean);
/* 2177 */     bean.setContext(getContext());
/* 2178 */     bean.setMeta_code("AMUnApprove");
/* 2179 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2180 */     invokeInitializingBean(bean);
/* 2181 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.container.AssMenuAction getAssMenuAction() {
/* 2185 */     if (context.get("assMenuAction") != null)
/* 2186 */       return (nc.ui.am.action.container.AssMenuAction)context.get("assMenuAction");
/* 2187 */     nc.ui.am.action.container.AssMenuAction bean = new nc.ui.am.action.container.AssMenuAction();
/* 2188 */     context.put("assMenuAction", bean);
/* 2189 */     bean.setActions(getManagedList29());
/* 2190 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2191 */     invokeInitializingBean(bean);
/* 2192 */     return bean;
/*      */   }
/*      */   
/* 2195 */   private List getManagedList29() { List list = new ArrayList();list.add(getGenPrayBill());list.add(getStockoutaction());list.add(getStockreturnaction());list.add(getInvLockAction());list.add(getInvUnLockAction());list.add(getSeparatorAction());list.add(getLinkStockOutBillAction());list.add(getSeparatorAction());list.add(getCapitalaction());list.add(getCapitalCancelAction());list.add(getSeparatorAction());list.add(getAttachmentAction());return list;
/*      */   }
/*      */   
/* 2198 */   public nc.ui.ewm.workorder.action.FileAction getAttachmentAction() { if (context.get("attachmentAction") != null)
/* 2199 */       return (nc.ui.ewm.workorder.action.FileAction)context.get("attachmentAction");
/* 2200 */     nc.ui.ewm.workorder.action.FileAction bean = new nc.ui.ewm.workorder.action.FileAction();
/* 2201 */     context.put("attachmentAction", bean);
/* 2202 */     bean.setModel(getModel());
/* 2203 */     bean.setBillList(getBillListView());
/* 2204 */     bean.setBillForm(getBillForm());
/* 2205 */     bean.setAttchAction(getAttachmentActionCard());
/* 2206 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2207 */     invokeInitializingBean(bean);
/* 2208 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.actions.FirstLineAction getFirstLineAction() {
/* 2212 */     if (context.get("firstLineAction") != null)
/* 2213 */       return (nc.ui.uif2.actions.FirstLineAction)context.get("firstLineAction");
/* 2214 */     nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
/* 2215 */     context.put("firstLineAction", bean);
/* 2216 */     bean.setModel(getModel());
/* 2217 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2218 */     invokeInitializingBean(bean);
/* 2219 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.actions.PreLineAction getPreLineAction() {
/* 2223 */     if (context.get("preLineAction") != null)
/* 2224 */       return (nc.ui.uif2.actions.PreLineAction)context.get("preLineAction");
/* 2225 */     nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
/* 2226 */     context.put("preLineAction", bean);
/* 2227 */     bean.setModel(getModel());
/* 2228 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2229 */     invokeInitializingBean(bean);
/* 2230 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.actions.NextLineAction getNextLineAction() {
/* 2234 */     if (context.get("nextLineAction") != null)
/* 2235 */       return (nc.ui.uif2.actions.NextLineAction)context.get("nextLineAction");
/* 2236 */     nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
/* 2237 */     context.put("nextLineAction", bean);
/* 2238 */     bean.setModel(getModel());
/* 2239 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2240 */     invokeInitializingBean(bean);
/* 2241 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.uif2.actions.LastLineAction getLastLineAction() {
/* 2245 */     if (context.get("lastLineAction") != null)
/* 2246 */       return (nc.ui.uif2.actions.LastLineAction)context.get("lastLineAction");
/* 2247 */     nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
/* 2248 */     context.put("lastLineAction", bean);
/* 2249 */     bean.setModel(getModel());
/* 2250 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2251 */     invokeInitializingBean(bean);
/* 2252 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.funcnode.ui.action.GroupAction getPrintMenuAction() {
/* 2256 */     if (context.get("printMenuAction") != null)
/* 2257 */       return (nc.funcnode.ui.action.GroupAction)context.get("printMenuAction");
/* 2258 */     nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
/* 2259 */     context.put("printMenuAction", bean);
/* 2260 */     bean.setCode("print");
/* 2261 */     bean.setActions(getManagedList30());
/* 2262 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2263 */     invokeInitializingBean(bean);
/* 2264 */     return bean;
/*      */   }
/*      */   
/* 2267 */   private List getManagedList30() { List list = new ArrayList();list.add(getPrintTemplateAction());list.add(getTempPrinviewAction());list.add(getOutputAction());list.add(getSeparatorAction());list.add(getPrintWoPlanInvAction());list.add(getPrintWoTaskAction());list.add(getPrintWoReportAction());list.add(getPrintWoCostItemAction());return list;
/*      */   }
/*      */   
/* 2270 */   public nc.ui.ewm.workorder.action.AMPrintTempPrinviewAction1 getTempPrinviewAction() { if (context.get("tempPrinviewAction") != null)
/* 2271 */       return (nc.ui.ewm.workorder.action.AMPrintTempPrinviewAction1)context.get("tempPrinviewAction");
/* 2272 */     nc.ui.ewm.workorder.action.AMPrintTempPrinviewAction1 bean = new nc.ui.ewm.workorder.action.AMPrintTempPrinviewAction1();
/* 2273 */     context.put("tempPrinviewAction", bean);
/* 2274 */     bean.setModel(getModel());
/* 2275 */     bean.setNodeKey("wo_main");
/* 2276 */     bean.setScaleProcessor(getBillVODigitProcessor());
/* 2277 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2278 */     invokeInitializingBean(bean);
/* 2279 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.AMPrintTemplateAction1 getPrintTemplateAction() {
/* 2283 */     if (context.get("printTemplateAction") != null)
/* 2284 */       return (nc.ui.ewm.workorder.action.AMPrintTemplateAction1)context.get("printTemplateAction");
/* 2285 */    nc.ui.ewm.workorder.action.AMPrintTemplateAction1 bean = new nc.ui.ewm.workorder.action.AMPrintTemplateAction1();
/* 2286 */     context.put("printTemplateAction", bean);
/* 2287 */     bean.setModel(getModel());
/* 2288 */     bean.setNodeKey("wo_main");
/* 2289 */     bean.setScaleProcessor(getBillVODigitProcessor());
/* 2290 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2291 */     invokeInitializingBean(bean);
/* 2292 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.AMOutputAction1 getOutputAction() {
/* 2296 */     if (context.get("outputAction") != null)
/* 2297 */       return (nc.ui.ewm.workorder.action.AMOutputAction1)context.get("outputAction");
/* 2298 */    nc.ui.ewm.workorder.action.AMOutputAction1 bean = new nc.ui.ewm.workorder.action.AMOutputAction1();
/* 2299 */     context.put("outputAction", bean);
/* 2300 */     bean.setModel(getModel());
/* 2301 */     bean.setScaleProcessor(getBillVODigitProcessor());
/* 2302 */     bean.setNodeKey("wo_main");
/* 2303 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2304 */     invokeInitializingBean(bean);
/* 2305 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.vo.am.scale.process.PrintScaleProcessCenter getBillVODigitProcessor() {
/* 2309 */     if (context.get("billVODigitProcessor") != null)
/* 2310 */       return (nc.vo.am.scale.process.PrintScaleProcessCenter)context.get("billVODigitProcessor");
/* 2311 */     nc.vo.am.scale.process.PrintScaleProcessCenter bean = new nc.vo.am.scale.process.PrintScaleProcessCenter();
/* 2312 */     context.put("billVODigitProcessor", bean);
/* 2313 */     bean.setScaleObjectData(getScaleObjectData());
/* 2314 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2315 */     invokeInitializingBean(bean);
/* 2316 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.container.QueryAboutMenuAction getQueryAboutMenuAction() {
/* 2320 */     if (context.get("queryAboutMenuAction") != null)
/* 2321 */       return (nc.ui.am.action.container.QueryAboutMenuAction)context.get("queryAboutMenuAction");
/* 2322 */     nc.ui.am.action.container.QueryAboutMenuAction bean = new nc.ui.am.action.container.QueryAboutMenuAction();
/* 2323 */     context.put("queryAboutMenuAction", bean);
/* 2324 */     bean.setActions(getManagedList31());
/* 2325 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2326 */     invokeInitializingBean(bean);
/* 2327 */     return bean;
/*      */   }
/*      */   
/* 2330 */   private List getManagedList31() { List list = new ArrayList();list.add(getQueryAboutEquipAction());list.add(getQueryAboutLocationAction());list.add(getLinkQryStdJobAction());list.add(getSeparatorAction());list.add(getLinkQryWorkFlowAction());list.add(getLinkQryStatusHisAction());list.add(getSeparatorAction());list.add(getLinkQryResAction());list.add(getLinkQryInvAtion());list.add(getSeparatorAction());list.add(getLinkQryParentAction());list.add(getLinkQryChildAction());list.add(getLinkQryOriginAction());list.add(getLinkQryLatterAction());list.add(getLinkQryBillAction());list.add(getSeparatorAction());list.add(getQryAboutVoucherAction());return list;
/*      */   }
/*      */   
/* 2333 */   public nc.ui.am.action.support.AMQueryAboutEquipAction getQueryAboutEquipAction() { if (context.get("queryAboutEquipAction") != null)
/* 2334 */       return (nc.ui.am.action.support.AMQueryAboutEquipAction)context.get("queryAboutEquipAction");
/* 2335 */     nc.ui.am.action.support.AMQueryAboutEquipAction bean = new nc.ui.am.action.support.AMQueryAboutEquipAction();
/* 2336 */     context.put("queryAboutEquipAction", bean);
/* 2337 */     bean.setModel(getModel());
/* 2338 */     bean.setBillList(getBillListView());
/* 2339 */     bean.setBillForm(getBillForm());
/* 2340 */     bean.setQueryAboutHead(true);
/* 2341 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2342 */     invokeInitializingBean(bean);
/* 2343 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMQueryApproveflowAction getQueryApproveflowAction() {
/* 2347 */     if (context.get("queryApproveflowAction") != null)
/* 2348 */       return (nc.ui.am.action.support.AMQueryApproveflowAction)context.get("queryApproveflowAction");
/* 2349 */     nc.ui.am.action.support.AMQueryApproveflowAction bean = new nc.ui.am.action.support.AMQueryApproveflowAction();
/* 2350 */     context.put("queryApproveflowAction", bean);
/* 2351 */     bean.setModel(getModel());
/* 2352 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2353 */     invokeInitializingBean(bean);
/* 2354 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMQueryAboutVoucherAction getQueryAboutVoucherAction() {
/* 2358 */     if (context.get("queryAboutVoucherAction") != null)
/* 2359 */       return (nc.ui.am.action.support.AMQueryAboutVoucherAction)context.get("queryAboutVoucherAction");
/* 2360 */     nc.ui.am.action.support.AMQueryAboutVoucherAction bean = new nc.ui.am.action.support.AMQueryAboutVoucherAction();
/* 2361 */     context.put("queryAboutVoucherAction", bean);
/* 2362 */     bean.setModel(getModel());
/* 2363 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2364 */     invokeInitializingBean(bean);
/* 2365 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMQueryAboutBillAction getQueryAboutBusiness() {
/* 2369 */     if (context.get("queryAboutBusiness") != null)
/* 2370 */       return (nc.ui.am.action.support.AMQueryAboutBillAction)context.get("queryAboutBusiness");
/* 2371 */     nc.ui.am.action.support.AMQueryAboutBillAction bean = new nc.ui.am.action.support.AMQueryAboutBillAction();
/* 2372 */     context.put("queryAboutBusiness", bean);
/* 2373 */     bean.setModel(getModel());
/* 2374 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2375 */     invokeInitializingBean(bean);
/* 2376 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getHeadTabActions() {
/* 2380 */     if (context.get("headTabActions") != null)
/* 2381 */       return (ArrayList)context.get("headTabActions");
/* 2382 */     ArrayList bean = new ArrayList(getManagedList32());context.put("headTabActions", bean);
/* 2383 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2384 */     invokeInitializingBean(bean);
/* 2385 */     return bean;
/*      */   }
/*      */   
/* 2388 */   private List getManagedList32() { List list = new ArrayList();list.add(getAttachmentAction());list.add(getActionsBar_ActionsBarSeparator_87234d());list.add(getFirstLineAction());list.add(getPreLineAction());list.add(getNextLineAction());list.add(getLastLineAction());list.add(getActionsBar_ActionsBarSeparator_1e9586());list.add(getHeadViewMaxAction());return list;
/*      */   }
/*      */   
/* 2391 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_87234d() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#87234d") != null)
/* 2392 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#87234d");
/* 2393 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 2394 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#87234d", bean);
/* 2395 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2396 */     invokeInitializingBean(bean);
/* 2397 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1e9586() {
/* 2401 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1e9586") != null)
/* 2402 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1e9586");
/* 2403 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 2404 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1e9586", bean);
/* 2405 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2406 */     invokeInitializingBean(bean);
/* 2407 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getDefaultBodyActions() {
/* 2411 */     if (context.get("defaultBodyActions") != null)
/* 2412 */       return (ArrayList)context.get("defaultBodyActions");
/* 2413 */     ArrayList bean = new ArrayList(getManagedList33());context.put("defaultBodyActions", bean);
/* 2414 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2415 */     invokeInitializingBean(bean);
/* 2416 */     return bean;
/*      */   }
/*      */   
/* 2419 */   private List getManagedList33() { List list = new ArrayList();list.add(getAddLineAction());list.add(getInsertLineAction());list.add(getDeleteLineAction());list.add(getBatchAlterAction());list.add(getActionsBar_ActionsBarSeparator_11be85b());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 2422 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_11be85b() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#11be85b") != null)
/* 2423 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#11be85b");
/* 2424 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 2425 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#11be85b", bean);
/* 2426 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2427 */     invokeInitializingBean(bean);
/* 2428 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.LineAddAction getLineAddAction() {
/* 2432 */     if (context.get("lineAddAction") != null)
/* 2433 */       return (nc.ui.ewm.workorder.tabaction.LineAddAction)context.get("lineAddAction");
/* 2434 */     nc.ui.ewm.workorder.tabaction.LineAddAction bean = new nc.ui.ewm.workorder.tabaction.LineAddAction();
/* 2435 */     context.put("lineAddAction", bean);
/* 2436 */     bean.setModel(getModel());
/* 2437 */     bean.setBillForm(getBillForm());
/* 2438 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2439 */     invokeInitializingBean(bean);
/* 2440 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.LineDeleteAction getLineDeleteAction() {
/* 2444 */     if (context.get("lineDeleteAction") != null)
/* 2445 */       return (nc.ui.ewm.workorder.tabaction.LineDeleteAction)context.get("lineDeleteAction");
/* 2446 */     nc.ui.ewm.workorder.tabaction.LineDeleteAction bean = new nc.ui.ewm.workorder.tabaction.LineDeleteAction();
/* 2447 */     context.put("lineDeleteAction", bean);
/* 2448 */     bean.setModel(getModel());
/* 2449 */     bean.setBillForm(getBillForm());
/* 2450 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2451 */     invokeInitializingBean(bean);
/* 2452 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.LineInsertAction getLineInsertAction() {
/* 2456 */     if (context.get("lineInsertAction") != null)
/* 2457 */       return (nc.ui.ewm.workorder.tabaction.LineInsertAction)context.get("lineInsertAction");
/* 2458 */     nc.ui.ewm.workorder.tabaction.LineInsertAction bean = new nc.ui.ewm.workorder.tabaction.LineInsertAction();
/* 2459 */     context.put("lineInsertAction", bean);
/* 2460 */     bean.setModel(getModel());
/* 2461 */     bean.setBillForm(getBillForm());
/* 2462 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2463 */     invokeInitializingBean(bean);
/* 2464 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.CopyPlanInvAction getCopyPlanInvAction() {
/* 2468 */     if (context.get("copyPlanInvAction") != null)
/* 2469 */       return (nc.ui.ewm.workorder.tabaction.CopyPlanInvAction)context.get("copyPlanInvAction");
/* 2470 */     nc.ui.ewm.workorder.tabaction.CopyPlanInvAction bean = new nc.ui.ewm.workorder.tabaction.CopyPlanInvAction();
/* 2471 */     context.put("copyPlanInvAction", bean);
/* 2472 */     bean.setModel(getModel());
/* 2473 */     bean.setBillForm(getBillForm());
/* 2474 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2475 */     invokeInitializingBean(bean);
/* 2476 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.CopyPlanPsnAction getCopyPlanPsnAction() {
/* 2480 */     if (context.get("copyPlanPsnAction") != null)
/* 2481 */       return (nc.ui.ewm.workorder.tabaction.CopyPlanPsnAction)context.get("copyPlanPsnAction");
/* 2482 */     nc.ui.ewm.workorder.tabaction.CopyPlanPsnAction bean = new nc.ui.ewm.workorder.tabaction.CopyPlanPsnAction();
/* 2483 */     context.put("copyPlanPsnAction", bean);
/* 2484 */     bean.setModel(getModel());
/* 2485 */     bean.setBillForm(getBillForm());
/* 2486 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2487 */     invokeInitializingBean(bean);
/* 2488 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.CopyPlanToolAction getCopyPlanToolAction() {
/* 2492 */     if (context.get("copyPlanToolAction") != null)
/* 2493 */       return (nc.ui.ewm.workorder.tabaction.CopyPlanToolAction)context.get("copyPlanToolAction");
/* 2494 */     nc.ui.ewm.workorder.tabaction.CopyPlanToolAction bean = new nc.ui.ewm.workorder.tabaction.CopyPlanToolAction();
/* 2495 */     context.put("copyPlanToolAction", bean);
/* 2496 */     bean.setModel(getModel());
/* 2497 */     bean.setBillForm(getBillForm());
/* 2498 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2499 */     invokeInitializingBean(bean);
/* 2500 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.CopyPlanOtherExesAction getCopyPlanOtherExesAction() {
/* 2504 */     if (context.get("copyPlanOtherExesAction") != null)
/* 2505 */       return (nc.ui.ewm.workorder.tabaction.CopyPlanOtherExesAction)context.get("copyPlanOtherExesAction");
/* 2506 */     nc.ui.ewm.workorder.tabaction.CopyPlanOtherExesAction bean = new nc.ui.ewm.workorder.tabaction.CopyPlanOtherExesAction();
/* 2507 */     context.put("copyPlanOtherExesAction", bean);
/* 2508 */     bean.setModel(getModel());
/* 2509 */     bean.setBillForm(getBillForm());
/* 2510 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2511 */     invokeInitializingBean(bean);
/* 2512 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.ChoosePartAction getChoosePartAction() {
/* 2516 */     if (context.get("choosePartAction") != null)
/* 2517 */       return (nc.ui.ewm.workorder.tabaction.ChoosePartAction)context.get("choosePartAction");
/* 2518 */     nc.ui.ewm.workorder.tabaction.ChoosePartAction bean = new nc.ui.ewm.workorder.tabaction.ChoosePartAction();
/* 2519 */     context.put("choosePartAction", bean);
/* 2520 */     bean.setModel(getModel());
/* 2521 */     bean.setBillForm(getBillForm());
/* 2522 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2523 */     invokeInitializingBean(bean);
/* 2524 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.BatchAlterAction getBatchAlterActionTask() {
/* 2528 */     if (context.get("batchAlterActionTask") != null)
/* 2529 */       return (nc.ui.ewm.workorder.tabaction.BatchAlterAction)context.get("batchAlterActionTask");
/* 2530 */     nc.ui.ewm.workorder.tabaction.BatchAlterAction bean = new nc.ui.ewm.workorder.tabaction.BatchAlterAction();
/* 2531 */     context.put("batchAlterActionTask", bean);
/* 2532 */     bean.setModel(getModel());
/* 2533 */     bean.setBillForm(getBillForm());
/* 2534 */     bean.setFilter(getTaskFilter());
/* 2535 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2536 */     invokeInitializingBean(bean);
/* 2537 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.TaskFilter getTaskFilter() {
/* 2541 */     if (context.get("taskFilter") != null)
/* 2542 */       return (nc.ui.ewm.workorder.tabaction.TaskFilter)context.get("taskFilter");
/* 2543 */     nc.ui.ewm.workorder.tabaction.TaskFilter bean = new nc.ui.ewm.workorder.tabaction.TaskFilter();
/* 2544 */     context.put("taskFilter", bean);
/* 2545 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2546 */     invokeInitializingBean(bean);
/* 2547 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.BatchAlterAction getBatchAlterPlanInv() {
/* 2551 */     if (context.get("batchAlterPlanInv") != null)
/* 2552 */       return (nc.ui.ewm.workorder.tabaction.BatchAlterAction)context.get("batchAlterPlanInv");
/* 2553 */     nc.ui.ewm.workorder.tabaction.BatchAlterAction bean = new nc.ui.ewm.workorder.tabaction.BatchAlterAction();
/* 2554 */     context.put("batchAlterPlanInv", bean);
/* 2555 */     bean.setModel(getModel());
/* 2556 */     bean.setBillForm(getBillForm());
/* 2557 */     bean.setFilter(getPlanInvFilter());
/* 2558 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2559 */     invokeInitializingBean(bean);
/* 2560 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.PlanInvFilter getPlanInvFilter() {
/* 2564 */     if (context.get("planInvFilter") != null)
/* 2565 */       return (nc.ui.ewm.workorder.tabaction.PlanInvFilter)context.get("planInvFilter");
/* 2566 */     nc.ui.ewm.workorder.tabaction.PlanInvFilter bean = new nc.ui.ewm.workorder.tabaction.PlanInvFilter();
/* 2567 */     context.put("planInvFilter", bean);
/* 2568 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2569 */     invokeInitializingBean(bean);
/* 2570 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.BatchAlterAction getBatchAlterPlanPsn() {
/* 2574 */     if (context.get("batchAlterPlanPsn") != null)
/* 2575 */       return (nc.ui.ewm.workorder.tabaction.BatchAlterAction)context.get("batchAlterPlanPsn");
/* 2576 */     nc.ui.ewm.workorder.tabaction.BatchAlterAction bean = new nc.ui.ewm.workorder.tabaction.BatchAlterAction();
/* 2577 */     context.put("batchAlterPlanPsn", bean);
/* 2578 */     bean.setModel(getModel());
/* 2579 */     bean.setBillForm(getBillForm());
/* 2580 */     bean.setFilter(getPlanPsnFilter());
/* 2581 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2582 */     invokeInitializingBean(bean);
/* 2583 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.PlanPsnFilter getPlanPsnFilter() {
/* 2587 */     if (context.get("planPsnFilter") != null)
/* 2588 */       return (nc.ui.ewm.workorder.tabaction.PlanPsnFilter)context.get("planPsnFilter");
/* 2589 */     nc.ui.ewm.workorder.tabaction.PlanPsnFilter bean = new nc.ui.ewm.workorder.tabaction.PlanPsnFilter();
/* 2590 */     context.put("planPsnFilter", bean);
/* 2591 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2592 */     invokeInitializingBean(bean);
/* 2593 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.BatchAlterAction getBatchAlterPlanTool() {
/* 2597 */     if (context.get("batchAlterPlanTool") != null)
/* 2598 */       return (nc.ui.ewm.workorder.tabaction.BatchAlterAction)context.get("batchAlterPlanTool");
/* 2599 */     nc.ui.ewm.workorder.tabaction.BatchAlterAction bean = new nc.ui.ewm.workorder.tabaction.BatchAlterAction();
/* 2600 */     context.put("batchAlterPlanTool", bean);
/* 2601 */     bean.setModel(getModel());
/* 2602 */     bean.setBillForm(getBillForm());
/* 2603 */     bean.setFilter(getPlanToolFilter());
/* 2604 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2605 */     invokeInitializingBean(bean);
/* 2606 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.PlanToolFilter getPlanToolFilter() {
/* 2610 */     if (context.get("planToolFilter") != null)
/* 2611 */       return (nc.ui.ewm.workorder.tabaction.PlanToolFilter)context.get("planToolFilter");
/* 2612 */     nc.ui.ewm.workorder.tabaction.PlanToolFilter bean = new nc.ui.ewm.workorder.tabaction.PlanToolFilter();
/* 2613 */     context.put("planToolFilter", bean);
/* 2614 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2615 */     invokeInitializingBean(bean);
/* 2616 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.BatchAlterAction getBatchAlterActualInv() {
/* 2620 */     if (context.get("batchAlterActualInv") != null)
/* 2621 */       return (nc.ui.ewm.workorder.tabaction.BatchAlterAction)context.get("batchAlterActualInv");
/* 2622 */     nc.ui.ewm.workorder.tabaction.BatchAlterAction bean = new nc.ui.ewm.workorder.tabaction.BatchAlterAction();
/* 2623 */     context.put("batchAlterActualInv", bean);
/* 2624 */     bean.setModel(getModel());
/* 2625 */     bean.setBillForm(getBillForm());
/* 2626 */     bean.setFilter(getActualInvFilter());
/* 2627 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2628 */     invokeInitializingBean(bean);
/* 2629 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.ActualInvFilter getActualInvFilter() {
/* 2633 */     if (context.get("actualInvFilter") != null)
/* 2634 */       return (nc.ui.ewm.workorder.tabaction.ActualInvFilter)context.get("actualInvFilter");
/* 2635 */     nc.ui.ewm.workorder.tabaction.ActualInvFilter bean = new nc.ui.ewm.workorder.tabaction.ActualInvFilter();
/* 2636 */     context.put("actualInvFilter", bean);
/* 2637 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2638 */     invokeInitializingBean(bean);
/* 2639 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.BatchAlterAction getBatchAlterActualPsn() {
/* 2643 */     if (context.get("batchAlterActualPsn") != null)
/* 2644 */       return (nc.ui.ewm.workorder.tabaction.BatchAlterAction)context.get("batchAlterActualPsn");
/* 2645 */     nc.ui.ewm.workorder.tabaction.BatchAlterAction bean = new nc.ui.ewm.workorder.tabaction.BatchAlterAction();
/* 2646 */     context.put("batchAlterActualPsn", bean);
/* 2647 */     bean.setModel(getModel());
/* 2648 */     bean.setBillForm(getBillForm());
/* 2649 */     bean.setFilter(getActualPsnFilter());
/* 2650 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2651 */     invokeInitializingBean(bean);
/* 2652 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.ActualPsnFilter getActualPsnFilter() {
/* 2656 */     if (context.get("actualPsnFilter") != null)
/* 2657 */       return (nc.ui.ewm.workorder.tabaction.ActualPsnFilter)context.get("actualPsnFilter");
/* 2658 */     nc.ui.ewm.workorder.tabaction.ActualPsnFilter bean = new nc.ui.ewm.workorder.tabaction.ActualPsnFilter();
/* 2659 */     context.put("actualPsnFilter", bean);
/* 2660 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2661 */     invokeInitializingBean(bean);
/* 2662 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.BatchAlterAction getBatchAlterActualTool() {
/* 2666 */     if (context.get("batchAlterActualTool") != null)
/* 2667 */       return (nc.ui.ewm.workorder.tabaction.BatchAlterAction)context.get("batchAlterActualTool");
/* 2668 */     nc.ui.ewm.workorder.tabaction.BatchAlterAction bean = new nc.ui.ewm.workorder.tabaction.BatchAlterAction();
/* 2669 */     context.put("batchAlterActualTool", bean);
/* 2670 */     bean.setModel(getModel());
/* 2671 */     bean.setBillForm(getBillForm());
/* 2672 */     bean.setFilter(getActualToolFilter());
/* 2673 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2674 */     invokeInitializingBean(bean);
/* 2675 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.ActualToolFilter getActualToolFilter() {
/* 2679 */     if (context.get("actualToolFilter") != null)
/* 2680 */       return (nc.ui.ewm.workorder.tabaction.ActualToolFilter)context.get("actualToolFilter");
/* 2681 */     nc.ui.ewm.workorder.tabaction.ActualToolFilter bean = new nc.ui.ewm.workorder.tabaction.ActualToolFilter();
/* 2682 */     context.put("actualToolFilter", bean);
/* 2683 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2684 */     invokeInitializingBean(bean);
/* 2685 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.BatchAlterAction getBatchAlterPart() {
/* 2689 */     if (context.get("batchAlterPart") != null)
/* 2690 */       return (nc.ui.ewm.workorder.tabaction.BatchAlterAction)context.get("batchAlterPart");
/* 2691 */     nc.ui.ewm.workorder.tabaction.BatchAlterAction bean = new nc.ui.ewm.workorder.tabaction.BatchAlterAction();
/* 2692 */     context.put("batchAlterPart", bean);
/* 2693 */     bean.setModel(getModel());
/* 2694 */     bean.setBillForm(getBillForm());
/* 2695 */     bean.setFilter(getPartFilter());
/* 2696 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2697 */     invokeInitializingBean(bean);
/* 2698 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.tabaction.PartFilter getPartFilter() {
/* 2702 */     if (context.get("partFilter") != null)
/* 2703 */       return (nc.ui.ewm.workorder.tabaction.PartFilter)context.get("partFilter");
/* 2704 */     nc.ui.ewm.workorder.tabaction.PartFilter bean = new nc.ui.ewm.workorder.tabaction.PartFilter();
/* 2705 */     context.put("partFilter", bean);
/* 2706 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2707 */     invokeInitializingBean(bean);
/* 2708 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.WFStartAction getWfstartAction() {
/* 2712 */     if (context.get("wfstartAction") != null)
/* 2713 */       return (nc.ui.ewm.workorder.action.WFStartAction)context.get("wfstartAction");
/* 2714 */     nc.ui.ewm.workorder.action.WFStartAction bean = new nc.ui.ewm.workorder.action.WFStartAction();
/* 2715 */     context.put("wfstartAction", bean);
/* 2716 */     bean.setModel(getModel());
/* 2717 */     bean.setValidators(getManagedList34());
/* 2718 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2719 */     invokeInitializingBean(bean);
/* 2720 */     return bean;
/*      */   }
/*      */   
/* 2723 */   private List getManagedList34() { List list = new ArrayList();list.add(getCommitValidator_1f1be36());list.add(getIsExistWorkflowValidator_1fed888());return list;
/*      */   }
/*      */   
/* 2726 */   private nc.ui.ewm.workorder.validator.CommitValidator getCommitValidator_1f1be36() { if (context.get("nc.ui.ewm.workorder.validator.CommitValidator#1f1be36") != null)
/* 2727 */       return (nc.ui.ewm.workorder.validator.CommitValidator)context.get("nc.ui.ewm.workorder.validator.CommitValidator#1f1be36");
/* 2728 */     nc.ui.ewm.workorder.validator.CommitValidator bean = new nc.ui.ewm.workorder.validator.CommitValidator();
/* 2729 */     context.put("nc.ui.ewm.workorder.validator.CommitValidator#1f1be36", bean);
/* 2730 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2731 */     invokeInitializingBean(bean);
/* 2732 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.validator.IsExistWorkflowValidator getIsExistWorkflowValidator_1fed888() {
/* 2736 */     if (context.get("nc.ui.ewm.workorder.validator.IsExistWorkflowValidator#1fed888") != null)
/* 2737 */       return (nc.ui.ewm.workorder.validator.IsExistWorkflowValidator)context.get("nc.ui.ewm.workorder.validator.IsExistWorkflowValidator#1fed888");
/* 2738 */     nc.ui.ewm.workorder.validator.IsExistWorkflowValidator bean = new nc.ui.ewm.workorder.validator.IsExistWorkflowValidator();
/* 2739 */     context.put("nc.ui.ewm.workorder.validator.IsExistWorkflowValidator#1fed888", bean);
/* 2740 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2741 */     invokeInitializingBean(bean);
/* 2742 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.WFReCallAction getWfRecallAction() {
/* 2746 */     if (context.get("wfRecallAction") != null)
/* 2747 */       return (nc.ui.ewm.workorder.action.WFReCallAction)context.get("wfRecallAction");
/* 2748 */     nc.ui.ewm.workorder.action.WFReCallAction bean = new nc.ui.ewm.workorder.action.WFReCallAction();
/* 2749 */     context.put("wfRecallAction", bean);
/* 2750 */     bean.setModel(getModel());
/* 2751 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2752 */     invokeInitializingBean(bean);
/* 2753 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.WFDriveAction getWfdriveAction() {
/* 2757 */     if (context.get("wfdriveAction") != null)
/* 2758 */       return (nc.ui.ewm.workorder.action.WFDriveAction)context.get("wfdriveAction");
/* 2759 */     nc.ui.ewm.workorder.action.WFDriveAction bean = new nc.ui.ewm.workorder.action.WFDriveAction();
/* 2760 */     context.put("wfdriveAction", bean);
/* 2761 */     bean.setModel(getModel());
/* 2762 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2763 */     invokeInitializingBean(bean);
/* 2764 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.WFRollBackAction getWfrollbackAction() {
/* 2768 */     if (context.get("wfrollbackAction") != null)
/* 2769 */       return (nc.ui.ewm.workorder.action.WFRollBackAction)context.get("wfrollbackAction");
/* 2770 */     nc.ui.ewm.workorder.action.WFRollBackAction bean = new nc.ui.ewm.workorder.action.WFRollBackAction();
/* 2771 */     context.put("wfrollbackAction", bean);
/* 2772 */     bean.setModel(getModel());
/* 2773 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2774 */     invokeInitializingBean(bean);
/* 2775 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.CopyAction getCopyAction() {
/* 2779 */     if (context.get("copyAction") != null)
/* 2780 */       return (nc.ui.ewm.workorder.action.CopyAction)context.get("copyAction");
/* 2781 */     nc.ui.ewm.workorder.action.CopyAction bean = new nc.ui.ewm.workorder.action.CopyAction();
/* 2782 */     context.put("copyAction", bean);
/* 2783 */     bean.setModel(getModel());
/* 2784 */     bean.setBillForm(getBillForm());
/* 2785 */     bean.setValidators(getManagedList35());
/* 2786 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2787 */     invokeInitializingBean(bean);
/* 2788 */     return bean;
/*      */   }
/*      */   
/* 2791 */   private List getManagedList35() { List list = new ArrayList();list.add(getOrgPermissionValidator());return list;
/*      */   }
/*      */   
/* 2794 */   public nc.ui.ewm.workorder.action.CapitalAction getCapitalaction() { if (context.get("capitalaction") != null)
/* 2795 */       return (nc.ui.ewm.workorder.action.CapitalAction)context.get("capitalaction");
/* 2796 */     nc.ui.ewm.workorder.action.CapitalAction bean = new nc.ui.ewm.workorder.action.CapitalAction();
/* 2797 */     context.put("capitalaction", bean);
/* 2798 */     bean.setModel(getModel());
/* 2799 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2800 */     invokeInitializingBean(bean);
/* 2801 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.CapitalCancelAction getCapitalCancelAction() {
/* 2805 */     if (context.get("capitalCancelAction") != null)
/* 2806 */       return (nc.ui.ewm.workorder.action.CapitalCancelAction)context.get("capitalCancelAction");
/* 2807 */     nc.ui.ewm.workorder.action.CapitalCancelAction bean = new nc.ui.ewm.workorder.action.CapitalCancelAction();
/* 2808 */     context.put("capitalCancelAction", bean);
/* 2809 */     bean.setModel(getModel());
/* 2810 */     bean.setValidators(getManagedList36());
/* 2811 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2812 */     invokeInitializingBean(bean);
/* 2813 */     return bean;
/*      */   }
/*      */   
/* 2816 */   private List getManagedList36() { List list = new ArrayList();list.add(getCapitalCancelValidator_39f0b9());return list;
/*      */   }
/*      */   
/* 2819 */   private nc.ui.ewm.workorder.validator.CapitalCancelValidator getCapitalCancelValidator_39f0b9() { if (context.get("nc.ui.ewm.workorder.validator.CapitalCancelValidator#39f0b9") != null)
/* 2820 */       return (nc.ui.ewm.workorder.validator.CapitalCancelValidator)context.get("nc.ui.ewm.workorder.validator.CapitalCancelValidator#39f0b9");
/* 2821 */     nc.ui.ewm.workorder.validator.CapitalCancelValidator bean = new nc.ui.ewm.workorder.validator.CapitalCancelValidator();
/* 2822 */     context.put("nc.ui.ewm.workorder.validator.CapitalCancelValidator#39f0b9", bean);
/* 2823 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2824 */     invokeInitializingBean(bean);
/* 2825 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.AlterStatusAction getAlterstatusaction() {
/* 2829 */     if (context.get("alterstatusaction") != null)
/* 2830 */       return (nc.ui.ewm.workorder.action.AlterStatusAction)context.get("alterstatusaction");
/* 2831 */     nc.ui.ewm.workorder.action.AlterStatusAction bean = new nc.ui.ewm.workorder.action.AlterStatusAction();
/* 2832 */     context.put("alterstatusaction", bean);
/* 2833 */     bean.setModel(getModel());
/* 2834 */     bean.setBillForm(getBillForm());
/* 2835 */     bean.setValidators(getManagedList37());
/* 2836 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2837 */     invokeInitializingBean(bean);
/* 2838 */     return bean;
/*      */   }

/*      */   public nc.ui.ewm.workorder.action.AlterDateAction getAlterDateAction() {
/* 2829 */     if (context.get("alterdateaction") != null)
/* 2830 */       return (nc.ui.ewm.workorder.action.AlterDateAction)context.get("alterdateaction");
/* 2831 */     nc.ui.ewm.workorder.action.AlterDateAction bean = new nc.ui.ewm.workorder.action.AlterDateAction();
/* 2832 */     context.put("alterdateaction", bean);
/* 2833 */     bean.setModel(getModel());
/* 2834 */     bean.setBillForm(getBillForm());
/* 2835 */     bean.setValidators(getManagedList37());
			   bean.setDataManager(getModelDataManager());
/* 2836 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2837 */     invokeInitializingBean(bean);
/* 2838 */     return bean;
/*      */   }
/*      */   
/* 2841 */   private List getManagedList37() { List list = new ArrayList();list.add(getUIPermissionValidator_1eb0d15());list.add(getAlterStatusValidator_a7f95a());return list;
/*      */   }
/*      */   
/* 2844 */   private nc.ui.am.validator.UIPermissionValidator getUIPermissionValidator_1eb0d15() { if (context.get("nc.ui.am.validator.UIPermissionValidator#1eb0d15") != null)
/* 2845 */       return (nc.ui.am.validator.UIPermissionValidator)context.get("nc.ui.am.validator.UIPermissionValidator#1eb0d15");
/* 2846 */     nc.ui.am.validator.UIPermissionValidator bean = new nc.ui.am.validator.UIPermissionValidator();
/* 2847 */     context.put("nc.ui.am.validator.UIPermissionValidator#1eb0d15", bean);
/* 2848 */     bean.setContext(getContext());
/* 2849 */     bean.setMeta_code("AlterStatus");
/* 2850 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2851 */     invokeInitializingBean(bean);
/* 2852 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.validator.AlterStatusValidator getAlterStatusValidator_a7f95a() {
/* 2856 */     if (context.get("nc.ui.ewm.workorder.validator.AlterStatusValidator#a7f95a") != null)
/* 2857 */       return (nc.ui.ewm.workorder.validator.AlterStatusValidator)context.get("nc.ui.ewm.workorder.validator.AlterStatusValidator#a7f95a");
/* 2858 */     nc.ui.ewm.workorder.validator.AlterStatusValidator bean = new nc.ui.ewm.workorder.validator.AlterStatusValidator();
/* 2859 */     context.put("nc.ui.ewm.workorder.validator.AlterStatusValidator#a7f95a", bean);
/* 2860 */     bean.setContext(getContext());
/* 2861 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2862 */     invokeInitializingBean(bean);
/* 2863 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenPrayBillAction getGenPrayBill() {
/* 2867 */     if (context.get("genPrayBill") != null)
/* 2868 */       return (nc.ui.ewm.workorder.action.GenPrayBillAction)context.get("genPrayBill");
/* 2869 */     nc.ui.ewm.workorder.action.GenPrayBillAction bean = new nc.ui.ewm.workorder.action.GenPrayBillAction();
/* 2870 */     context.put("genPrayBill", bean);
/* 2871 */     bean.setModel(getModel());
/* 2872 */     bean.setBillList(getBillListView());
/* 2873 */     bean.setBillForm(getBillForm());
/* 2874 */     bean.setValidators(getManagedList38());
/* 2875 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2876 */     invokeInitializingBean(bean);
/* 2877 */     return bean;
/*      */   }
/*      */   
/* 2880 */   private List getManagedList38() { List list = new ArrayList();list.add(getGenPrayBillValidator_1e3b235());return list;
/*      */   }
/*      */   
/* 2883 */   private nc.ui.ewm.workorder.validator.GenPrayBillValidator getGenPrayBillValidator_1e3b235() { if (context.get("nc.ui.ewm.workorder.validator.GenPrayBillValidator#1e3b235") != null)
/* 2884 */       return (nc.ui.ewm.workorder.validator.GenPrayBillValidator)context.get("nc.ui.ewm.workorder.validator.GenPrayBillValidator#1e3b235");
/* 2885 */     nc.ui.ewm.workorder.validator.GenPrayBillValidator bean = new nc.ui.ewm.workorder.validator.GenPrayBillValidator();
/* 2886 */     context.put("nc.ui.ewm.workorder.validator.GenPrayBillValidator#1e3b235", bean);
/* 2887 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2888 */     invokeInitializingBean(bean);
/* 2889 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenStockReturnAction getStockreturnaction() {
/* 2893 */     if (context.get("stockreturnaction") != null)
/* 2894 */       return (nc.ui.ewm.workorder.action.GenStockReturnAction)context.get("stockreturnaction");
/* 2895 */     nc.ui.ewm.workorder.action.GenStockReturnAction bean = new nc.ui.ewm.workorder.action.GenStockReturnAction();
/* 2896 */     context.put("stockreturnaction", bean);
/* 2897 */     bean.setModel(getModel());
/* 2898 */     bean.setBillList(getBillListView());
/* 2899 */     bean.setBillForm(getBillForm());
/* 2900 */     bean.setValidators(getManagedList39());
/* 2901 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2902 */     invokeInitializingBean(bean);
/* 2903 */     return bean;
/*      */   }
/*      */   
/* 2906 */   private List getManagedList39() { List list = new ArrayList();list.add(getGenICMaterialReturnValidator_1c8db79());return list;
/*      */   }
/*      */   
/* 2909 */   private nc.ui.ewm.workorder.validator.GenICMaterialReturnValidator getGenICMaterialReturnValidator_1c8db79() { if (context.get("nc.ui.ewm.workorder.validator.GenICMaterialReturnValidator#1c8db79") != null)
/* 2910 */       return (nc.ui.ewm.workorder.validator.GenICMaterialReturnValidator)context.get("nc.ui.ewm.workorder.validator.GenICMaterialReturnValidator#1c8db79");
/* 2911 */     nc.ui.ewm.workorder.validator.GenICMaterialReturnValidator bean = new nc.ui.ewm.workorder.validator.GenICMaterialReturnValidator();
/* 2912 */     context.put("nc.ui.ewm.workorder.validator.GenICMaterialReturnValidator#1c8db79", bean);
/* 2913 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2914 */     invokeInitializingBean(bean);
/* 2915 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenStockOutAction getStockoutaction() {
/* 2919 */     if (context.get("stockoutaction") != null)
/* 2920 */       return (nc.ui.ewm.workorder.action.GenStockOutAction)context.get("stockoutaction");
/* 2921 */     nc.ui.ewm.workorder.action.GenStockOutAction bean = new nc.ui.ewm.workorder.action.GenStockOutAction();
/* 2922 */     context.put("stockoutaction", bean);
/* 2923 */     bean.setModel(getModel());
/* 2924 */     bean.setBillList(getBillListView());
/* 2925 */     bean.setBillForm(getBillForm());
/* 2926 */     bean.setDataManager(getModelDataManager());
/* 2927 */     bean.setValidators(getManagedList40());
/* 2928 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2929 */     invokeInitializingBean(bean);
/* 2930 */     return bean;
/*      */   }
/*      */   
/* 2933 */   private List getManagedList40() { List list = new ArrayList();list.add(getGenICMaterialOutValidator_1c0e6df());return list;
/*      */   }
/*      */   
/* 2936 */   private nc.ui.ewm.workorder.validator.GenICMaterialOutValidator getGenICMaterialOutValidator_1c0e6df() { if (context.get("nc.ui.ewm.workorder.validator.GenICMaterialOutValidator#1c0e6df") != null)
/* 2937 */       return (nc.ui.ewm.workorder.validator.GenICMaterialOutValidator)context.get("nc.ui.ewm.workorder.validator.GenICMaterialOutValidator#1c0e6df");
/* 2938 */     nc.ui.ewm.workorder.validator.GenICMaterialOutValidator bean = new nc.ui.ewm.workorder.validator.GenICMaterialOutValidator();
/* 2939 */     context.put("nc.ui.ewm.workorder.validator.GenICMaterialOutValidator#1c0e6df", bean);
/* 2940 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2941 */     invokeInitializingBean(bean);
/* 2942 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.common.action.QueryAboutLocationAction getQueryAboutLocationAction() {
/* 2946 */     if (context.get("queryAboutLocationAction") != null)
/* 2947 */       return (nc.ui.am.common.action.QueryAboutLocationAction)context.get("queryAboutLocationAction");
/* 2948 */     nc.ui.am.common.action.QueryAboutLocationAction bean = new nc.ui.am.common.action.QueryAboutLocationAction();
/* 2949 */     context.put("queryAboutLocationAction", bean);
/* 2950 */     bean.setModel(getModel());
/* 2951 */     bean.setBillList(getBillListView());
/* 2952 */     bean.setBillForm(getBillForm());
/* 2953 */     bean.setQueryAboutHead(true);
/* 2954 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2955 */     invokeInitializingBean(bean);
/* 2956 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.common.action.QueryAboutStdJobAction getLinkQryStdJobAction() {
/* 2960 */     if (context.get("linkQryStdJobAction") != null)
/* 2961 */       return (nc.ui.am.common.action.QueryAboutStdJobAction)context.get("linkQryStdJobAction");
/* 2962 */     nc.ui.am.common.action.QueryAboutStdJobAction bean = new nc.ui.am.common.action.QueryAboutStdJobAction();
/* 2963 */     context.put("linkQryStdJobAction", bean);
/* 2964 */     bean.setModel(getModel());
/* 2965 */     bean.setBillList(getBillListView());
/* 2966 */     bean.setBillForm(getBillForm());
/* 2967 */     bean.setQueryAboutHead(true);
/* 2968 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2969 */     invokeInitializingBean(bean);
/* 2970 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.QryAboutStatusHisAction getLinkQryStatusHisAction() {
/* 2974 */     if (context.get("linkQryStatusHisAction") != null)
/* 2975 */       return (nc.ui.ewm.workorder.action.QryAboutStatusHisAction)context.get("linkQryStatusHisAction");
/* 2976 */     nc.ui.ewm.workorder.action.QryAboutStatusHisAction bean = new nc.ui.ewm.workorder.action.QryAboutStatusHisAction();
/* 2977 */     context.put("linkQryStatusHisAction", bean);
/* 2978 */     bean.setModel(getModel());
/* 2979 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2980 */     invokeInitializingBean(bean);
/* 2981 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.QryAboutParentAction getLinkQryParentAction() {
/* 2985 */     if (context.get("linkQryParentAction") != null)
/* 2986 */       return (nc.ui.ewm.workorder.action.QryAboutParentAction)context.get("linkQryParentAction");
/* 2987 */     nc.ui.ewm.workorder.action.QryAboutParentAction bean = new nc.ui.ewm.workorder.action.QryAboutParentAction();
/* 2988 */     context.put("linkQryParentAction", bean);
/* 2989 */     bean.setModel(getModel());
/* 2990 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 2991 */     invokeInitializingBean(bean);
/* 2992 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.QryAboutChildAction getLinkQryChildAction() {
/* 2996 */     if (context.get("linkQryChildAction") != null)
/* 2997 */       return (nc.ui.ewm.workorder.action.QryAboutChildAction)context.get("linkQryChildAction");
/* 2998 */     nc.ui.ewm.workorder.action.QryAboutChildAction bean = new nc.ui.ewm.workorder.action.QryAboutChildAction();
/* 2999 */     context.put("linkQryChildAction", bean);
/* 3000 */     bean.setModel(getModel());
/* 3001 */     bean.setBillForm(getBillForm());
/* 3002 */     bean.setBillList(getBillListView());
/* 3003 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3004 */     invokeInitializingBean(bean);
/* 3005 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.QryAboutOriginAction getLinkQryOriginAction() {
/* 3009 */     if (context.get("linkQryOriginAction") != null)
/* 3010 */       return (nc.ui.ewm.workorder.action.QryAboutOriginAction)context.get("linkQryOriginAction");
/* 3011 */     nc.ui.ewm.workorder.action.QryAboutOriginAction bean = new nc.ui.ewm.workorder.action.QryAboutOriginAction();
/* 3012 */     context.put("linkQryOriginAction", bean);
/* 3013 */     bean.setModel(getModel());
/* 3014 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3015 */     invokeInitializingBean(bean);
/* 3016 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.QryAboutLatterAction getLinkQryLatterAction() {
/* 3020 */     if (context.get("linkQryLatterAction") != null)
/* 3021 */       return (nc.ui.ewm.workorder.action.QryAboutLatterAction)context.get("linkQryLatterAction");
/* 3022 */     nc.ui.ewm.workorder.action.QryAboutLatterAction bean = new nc.ui.ewm.workorder.action.QryAboutLatterAction();
/* 3023 */     context.put("linkQryLatterAction", bean);
/* 3024 */     bean.setModel(getModel());
/* 3025 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3026 */     invokeInitializingBean(bean);
/* 3027 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMQueryAboutBillAction getLinkQryBillAction() {
/* 3031 */     if (context.get("linkQryBillAction") != null)
/* 3032 */       return (nc.ui.am.action.support.AMQueryAboutBillAction)context.get("linkQryBillAction");
/* 3033 */     nc.ui.am.action.support.AMQueryAboutBillAction bean = new nc.ui.am.action.support.AMQueryAboutBillAction();
/* 3034 */     context.put("linkQryBillAction", bean);
/* 3035 */     bean.setModel(getModel());
/* 3036 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3037 */     invokeInitializingBean(bean);
/* 3038 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.InvReserveQueryAction getLinkQryResAction() {
/* 3042 */     if (context.get("linkQryResAction") != null)
/* 3043 */       return (nc.ui.ewm.workorder.action.InvReserveQueryAction)context.get("linkQryResAction");
/* 3044 */     nc.ui.ewm.workorder.action.InvReserveQueryAction bean = new nc.ui.ewm.workorder.action.InvReserveQueryAction();
/* 3045 */     context.put("linkQryResAction", bean);
/* 3046 */     bean.setModel(getModel());
/* 3047 */     bean.setBillForm(getBillForm());
/* 3048 */     bean.setBillList(getBillListView());
/* 3049 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3050 */     invokeInitializingBean(bean);
/* 3051 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.WorkOrderQueryAboutStocksAction getLinkQryInvAtion() {
/* 3055 */     if (context.get("linkQryInvAtion") != null)
/* 3056 */       return (nc.ui.ewm.workorder.action.WorkOrderQueryAboutStocksAction)context.get("linkQryInvAtion");
/* 3057 */     nc.ui.ewm.workorder.action.WorkOrderQueryAboutStocksAction bean = new nc.ui.ewm.workorder.action.WorkOrderQueryAboutStocksAction();
/* 3058 */     context.put("linkQryInvAtion", bean);
/* 3059 */     bean.setBillForm(getBillForm());
/* 3060 */     bean.setModel(getModel());
/* 3061 */     bean.setMaterialField("pk_material");
/* 3062 */     bean.setMaterialVField("pk_material_v_ID");
/* 3063 */     bean.setStockOrgField("pk_stockorg_ID");
/* 3064 */     bean.setStockOrgVField("pk_stockorg_v_ID");
/* 3065 */     bean.setPkUnitField("castunitid_ID");
/* 3066 */     bean.setStorDocField("pk_stordoc_ID");
/* 3067 */     bean.setChangeRateField("vchangerate");
/* 3068 */     bean.setTabCode("wo_plan_inv");
/* 3069 */     bean.init();
/* 3070 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3071 */     invokeInitializingBean(bean);
/* 3072 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.QryAboutWorkFlowAction getLinkQryWorkFlowAction() {
/* 3076 */     if (context.get("linkQryWorkFlowAction") != null)
/* 3077 */       return (nc.ui.ewm.workorder.action.QryAboutWorkFlowAction)context.get("linkQryWorkFlowAction");
/* 3078 */     nc.ui.ewm.workorder.action.QryAboutWorkFlowAction bean = new nc.ui.ewm.workorder.action.QryAboutWorkFlowAction();
/* 3079 */     context.put("linkQryWorkFlowAction", bean);
/* 3080 */     bean.setModel(getModel());
/* 3081 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3082 */     invokeInitializingBean(bean);
/* 3083 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenStdJobAction getGenStdJobAction() {
/* 3087 */     if (context.get("genStdJobAction") != null)
/* 3088 */       return (nc.ui.ewm.workorder.action.GenStdJobAction)context.get("genStdJobAction");
/* 3089 */     nc.ui.ewm.workorder.action.GenStdJobAction bean = new nc.ui.ewm.workorder.action.GenStdJobAction();
/* 3090 */     context.put("genStdJobAction", bean);
/* 3091 */     bean.setModel(getModel());
/* 3092 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3093 */     invokeInitializingBean(bean);
/* 3094 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenLatterAction getGenLatterAction() {
/* 3098 */     if (context.get("genLatterAction") != null)
/* 3099 */       return (nc.ui.ewm.workorder.action.GenLatterAction)context.get("genLatterAction");
/* 3100 */     nc.ui.ewm.workorder.action.GenLatterAction bean = new nc.ui.ewm.workorder.action.GenLatterAction();
/* 3101 */     context.put("genLatterAction", bean);
/* 3102 */     bean.setModel(getModel());
/* 3103 */     bean.setBillForm(getBillForm());
/* 3104 */     bean.setValidators(getManagedList41());
/* 3105 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3106 */     invokeInitializingBean(bean);
/* 3107 */     return bean;
/*      */   }
/*      */   
/* 3110 */   private List getManagedList41() { List list = new ArrayList();list.add(getGenLatterValidator_17577e5());return list;
/*      */   }
/*      */   
/* 3113 */   private nc.ui.ewm.workorder.validator.GenLatterValidator getGenLatterValidator_17577e5() { if (context.get("nc.ui.ewm.workorder.validator.GenLatterValidator#17577e5") != null)
/* 3114 */       return (nc.ui.ewm.workorder.validator.GenLatterValidator)context.get("nc.ui.ewm.workorder.validator.GenLatterValidator#17577e5");
/* 3115 */     nc.ui.ewm.workorder.validator.GenLatterValidator bean = new nc.ui.ewm.workorder.validator.GenLatterValidator();
/* 3116 */     context.put("nc.ui.ewm.workorder.validator.GenLatterValidator#17577e5", bean);
/* 3117 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3118 */     invokeInitializingBean(bean);
/* 3119 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenFailureDocAction getGenFailureDocAction() {
/* 3123 */     if (context.get("genFailureDocAction") != null)
/* 3124 */       return (nc.ui.ewm.workorder.action.GenFailureDocAction)context.get("genFailureDocAction");
/* 3125 */     nc.ui.ewm.workorder.action.GenFailureDocAction bean = new nc.ui.ewm.workorder.action.GenFailureDocAction();
/* 3126 */     context.put("genFailureDocAction", bean);
/* 3127 */     bean.setModel(getModel());
/* 3128 */     bean.setValidators(getManagedList42());
/* 3129 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3130 */     invokeInitializingBean(bean);
/* 3131 */     return bean;
/*      */   }
/*      */   
/* 3134 */   private List getManagedList42() { List list = new ArrayList();list.add(getGenFailureBillValidator_1126871());return list;
/*      */   }
/*      */   
/* 3137 */   private nc.ui.ewm.workorder.validator.GenFailureBillValidator getGenFailureBillValidator_1126871() { if (context.get("nc.ui.ewm.workorder.validator.GenFailureBillValidator#1126871") != null)
/* 3138 */       return (nc.ui.ewm.workorder.validator.GenFailureBillValidator)context.get("nc.ui.ewm.workorder.validator.GenFailureBillValidator#1126871");
/* 3139 */     nc.ui.ewm.workorder.validator.GenFailureBillValidator bean = new nc.ui.ewm.workorder.validator.GenFailureBillValidator();
/* 3140 */     context.put("nc.ui.ewm.workorder.validator.GenFailureBillValidator#1126871", bean);
/* 3141 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3142 */     invokeInitializingBean(bean);
/* 3143 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenIsolationAction getGenIsolationAction() {
/* 3147 */     if (context.get("genIsolationAction") != null)
/* 3148 */       return (nc.ui.ewm.workorder.action.GenIsolationAction)context.get("genIsolationAction");
/* 3149 */     nc.ui.ewm.workorder.action.GenIsolationAction bean = new nc.ui.ewm.workorder.action.GenIsolationAction();
/* 3150 */     context.put("genIsolationAction", bean);
/* 3151 */     bean.setModel(getModel());
/* 3152 */     bean.setBillList(getBillListView());
/* 3153 */     bean.setBillForm(getBillForm());
/* 3154 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3155 */     invokeInitializingBean(bean);
/* 3156 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenPermitAction getGenPermitAction() {
/* 3160 */     if (context.get("genPermitAction") != null)
/* 3161 */       return (nc.ui.ewm.workorder.action.GenPermitAction)context.get("genPermitAction");
/* 3162 */     nc.ui.ewm.workorder.action.GenPermitAction bean = new nc.ui.ewm.workorder.action.GenPermitAction();
/* 3163 */     context.put("genPermitAction", bean);
/* 3164 */     bean.setModel(getModel());
/* 3165 */     bean.setDataManager(getModelDataManager());
/* 3166 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3167 */     invokeInitializingBean(bean);
/* 3168 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenOutAPayAction getGenOutAPayAction() {
/* 3172 */     if (context.get("genOutAPayAction") != null)
/* 3173 */       return (nc.ui.ewm.workorder.action.GenOutAPayAction)context.get("genOutAPayAction");
/* 3174 */     nc.ui.ewm.workorder.action.GenOutAPayAction bean = new nc.ui.ewm.workorder.action.GenOutAPayAction();
/* 3175 */     context.put("genOutAPayAction", bean);
/* 3176 */     bean.setModel(getModel());
/* 3177 */     bean.setValidators(getManagedList43());
/* 3178 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3179 */     invokeInitializingBean(bean);
/* 3180 */     return bean;
/*      */   }
/*      */   
/* 3183 */   private List getManagedList43() { List list = new ArrayList();list.add(getGenOutAPayValidator_1870baf());return list;
/*      */   }
/*      */   
/* 3186 */   private nc.ui.ewm.workorder.validator.GenOutAPayValidator getGenOutAPayValidator_1870baf() { if (context.get("nc.ui.ewm.workorder.validator.GenOutAPayValidator#1870baf") != null)
/* 3187 */       return (nc.ui.ewm.workorder.validator.GenOutAPayValidator)context.get("nc.ui.ewm.workorder.validator.GenOutAPayValidator#1870baf");
/* 3188 */     nc.ui.ewm.workorder.validator.GenOutAPayValidator bean = new nc.ui.ewm.workorder.validator.GenOutAPayValidator();
/* 3189 */     context.put("nc.ui.ewm.workorder.validator.GenOutAPayValidator#1870baf", bean);
/* 3190 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3191 */     invokeInitializingBean(bean);
/* 3192 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.GenARAPAction getGenARAPAction() {
/* 3196 */     if (context.get("genARAPAction") != null)
/* 3197 */       return (nc.ui.ewm.workorder.action.GenARAPAction)context.get("genARAPAction");
/* 3198 */     nc.ui.ewm.workorder.action.GenARAPAction bean = new nc.ui.ewm.workorder.action.GenARAPAction();
/* 3199 */     context.put("genARAPAction", bean);
/* 3200 */     bean.setModel(getModel());
/* 3201 */     bean.setValidators(getManagedList44());
/* 3202 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3203 */     invokeInitializingBean(bean);
/* 3204 */     return bean;
/*      */   }
/*      */   
/* 3207 */   private List getManagedList44() { List list = new ArrayList();list.add(getGenARAPValidator_1101f5b());return list;
/*      */   }
/*      */   
/* 3210 */   private nc.ui.ewm.workorder.validator.GenARAPValidator getGenARAPValidator_1101f5b() { if (context.get("nc.ui.ewm.workorder.validator.GenARAPValidator#1101f5b") != null)
/* 3211 */       return (nc.ui.ewm.workorder.validator.GenARAPValidator)context.get("nc.ui.ewm.workorder.validator.GenARAPValidator#1101f5b");
/* 3212 */     nc.ui.ewm.workorder.validator.GenARAPValidator bean = new nc.ui.ewm.workorder.validator.GenARAPValidator();
/* 3213 */     context.put("nc.ui.ewm.workorder.validator.GenARAPValidator#1101f5b", bean);
/* 3214 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3215 */     invokeInitializingBean(bean);
/* 3216 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.InvLockAction getInvLockAction() {
/* 3220 */     if (context.get("invLockAction") != null)
/* 3221 */       return (nc.ui.ewm.workorder.action.InvLockAction)context.get("invLockAction");
/* 3222 */     nc.ui.ewm.workorder.action.InvLockAction bean = new nc.ui.ewm.workorder.action.InvLockAction();
/* 3223 */     context.put("invLockAction", bean);
/* 3224 */     bean.setModel(getModel());
/* 3225 */     bean.setBillForm(getBillForm());
/* 3226 */     bean.setBillList(getBillListView());
/* 3227 */     bean.setValidators(getManagedList45());
/* 3228 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3229 */     invokeInitializingBean(bean);
/* 3230 */     return bean;
/*      */   }
/*      */   
/* 3233 */   private List getManagedList45() { List list = new ArrayList();list.add(getInvLockValidator_6bb6e5());return list;
/*      */   }
/*      */   
/* 3236 */   private nc.ui.ewm.workorder.validator.InvLockValidator getInvLockValidator_6bb6e5() { if (context.get("nc.ui.ewm.workorder.validator.InvLockValidator#6bb6e5") != null)
/* 3237 */       return (nc.ui.ewm.workorder.validator.InvLockValidator)context.get("nc.ui.ewm.workorder.validator.InvLockValidator#6bb6e5");
/* 3238 */     nc.ui.ewm.workorder.validator.InvLockValidator bean = new nc.ui.ewm.workorder.validator.InvLockValidator();
/* 3239 */     context.put("nc.ui.ewm.workorder.validator.InvLockValidator#6bb6e5", bean);
/* 3240 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3241 */     invokeInitializingBean(bean);
/* 3242 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.InvUnLockAction getInvUnLockAction() {
/* 3246 */     if (context.get("invUnLockAction") != null)
/* 3247 */       return (nc.ui.ewm.workorder.action.InvUnLockAction)context.get("invUnLockAction");
/* 3248 */     nc.ui.ewm.workorder.action.InvUnLockAction bean = new nc.ui.ewm.workorder.action.InvUnLockAction();
/* 3249 */     context.put("invUnLockAction", bean);
/* 3250 */     bean.setModel(getModel());
/* 3251 */     bean.setBillForm(getBillForm());
/* 3252 */     bean.setBillList(getBillListView());
/* 3253 */     bean.setValidators(getManagedList46());
/* 3254 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3255 */     invokeInitializingBean(bean);
/* 3256 */     return bean;
/*      */   }
/*      */   
/* 3259 */   private List getManagedList46() { List list = new ArrayList();list.add(getInvUnLockValidator_1b478b4());return list;
/*      */   }
/*      */   
/* 3262 */   private nc.ui.ewm.workorder.validator.InvUnLockValidator getInvUnLockValidator_1b478b4() { if (context.get("nc.ui.ewm.workorder.validator.InvUnLockValidator#1b478b4") != null)
/* 3263 */       return (nc.ui.ewm.workorder.validator.InvUnLockValidator)context.get("nc.ui.ewm.workorder.validator.InvUnLockValidator#1b478b4");
/* 3264 */     nc.ui.ewm.workorder.validator.InvUnLockValidator bean = new nc.ui.ewm.workorder.validator.InvUnLockValidator();
/* 3265 */     context.put("nc.ui.ewm.workorder.validator.InvUnLockValidator#1b478b4", bean);
/* 3266 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3267 */     invokeInitializingBean(bean);
/* 3268 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.QryAboutVoucherAction getQryAboutVoucherAction() {
/* 3272 */     if (context.get("qryAboutVoucherAction") != null)
/* 3273 */       return (nc.ui.ewm.workorder.action.QryAboutVoucherAction)context.get("qryAboutVoucherAction");
/* 3274 */     nc.ui.ewm.workorder.action.QryAboutVoucherAction bean = new nc.ui.ewm.workorder.action.QryAboutVoucherAction();
/* 3275 */     context.put("qryAboutVoucherAction", bean);
/* 3276 */     bean.setModel(getModel());
/* 3277 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3278 */     invokeInitializingBean(bean);
/* 3279 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.LinkStockOutBillAction getLinkStockOutBillAction() {
/* 3283 */     if (context.get("linkStockOutBillAction") != null)
/* 3284 */       return (nc.ui.ewm.workorder.action.LinkStockOutBillAction)context.get("linkStockOutBillAction");
/* 3285 */     nc.ui.ewm.workorder.action.LinkStockOutBillAction bean = new nc.ui.ewm.workorder.action.LinkStockOutBillAction();
/* 3286 */     context.put("linkStockOutBillAction", bean);
/* 3287 */     bean.setModel(getModel());
/* 3288 */     bean.setBillForm(getBillForm());
/* 3289 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3290 */     invokeInitializingBean(bean);
/* 3291 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.action.support.AMBizProcessMenuAction getWorkflowMenuAction() {
/* 3295 */     if (context.get("workflowMenuAction") != null)
/* 3296 */       return (nc.ui.am.action.support.AMBizProcessMenuAction)context.get("workflowMenuAction");
/* 3297 */     nc.ui.am.action.support.AMBizProcessMenuAction bean = new nc.ui.am.action.support.AMBizProcessMenuAction();
/* 3298 */     context.put("workflowMenuAction", bean);
/* 3299 */     bean.setProcessActionCode("SignalWorkFlow");
/* 3300 */     bean.setActions(getManagedList47());
/* 3301 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3302 */     invokeInitializingBean(bean);
/* 3303 */     return bean;
/*      */   }
/*      */   
/* 3306 */   private List getManagedList47() { List list = new ArrayList();list.add(getWfdriveAction());list.add(getWfrollbackAction());return list;
/*      */   }
/*      */   
/* 3309 */   public nc.ui.am.action.container.QueryAboutMenuAction getQueryAboutMenuActionEdit() { if (context.get("queryAboutMenuActionEdit") != null)
/* 3310 */       return (nc.ui.am.action.container.QueryAboutMenuAction)context.get("queryAboutMenuActionEdit");
/* 3311 */     nc.ui.am.action.container.QueryAboutMenuAction bean = new nc.ui.am.action.container.QueryAboutMenuAction();
/* 3312 */     context.put("queryAboutMenuActionEdit", bean);
/* 3313 */     bean.setActions(getManagedList48());
/* 3314 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3315 */     invokeInitializingBean(bean);
/* 3316 */     return bean;
/*      */   }
/*      */   
/* 3319 */   private List getManagedList48() { List list = new ArrayList();list.add(getQueryAboutEquipAction());list.add(getQueryAboutLocationAction());list.add(getLinkQryStdJobAction());list.add(getSeparatorAction());list.add(getLinkQryInvAtion());return list;
/*      */   }
/*      */   
/* 3322 */   public nc.ui.ewm.workorder.action.RelativeFuncMenuAction getRelaFuncAction() { if (context.get("relaFuncAction") != null)
/* 3323 */       return (nc.ui.ewm.workorder.action.RelativeFuncMenuAction)context.get("relaFuncAction");
/* 3324 */     nc.ui.ewm.workorder.action.RelativeFuncMenuAction bean = new nc.ui.ewm.workorder.action.RelativeFuncMenuAction();
/* 3325 */     context.put("relaFuncAction", bean);
/* 3326 */     bean.setActions(getManagedList49());
/* 3327 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3328 */     invokeInitializingBean(bean);
/* 3329 */     return bean;
/*      */   }
/*      */   
/* 3332 */   private List getManagedList49() { List list = new ArrayList();list.add(getGenPermitAction());list.add(getGenStdJobAction());list.add(getGenLatterAction());list.add(getGenFailureDocAction());list.add(getGenIsolationAction());list.add(getGenOutAPayAction());list.add(getGenARAPAction());return list;
/*      */   }
/*      */   
/* 3335 */   public nc.ui.ewm.workorder.action.PrintWoPlanInvAction1 getPrintWoPlanInvAction() { if (context.get("printWoPlanInvAction") != null)
/* 3336 */       return (nc.ui.ewm.workorder.action.PrintWoPlanInvAction1)context.get("printWoPlanInvAction");
/* 3337 */     nc.ui.ewm.workorder.action.PrintWoPlanInvAction1 bean = new nc.ui.ewm.workorder.action.PrintWoPlanInvAction1();
/* 3338 */     context.put("printWoPlanInvAction", bean);
/* 3339 */     bean.setModel(getModel());
/* 3340 */     bean.setScaleProcessor(getBillVODigitProcessor());
/* 3341 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3342 */     invokeInitializingBean(bean);
/* 3343 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.PrintWoTaskAction1 getPrintWoTaskAction() {
/* 3347 */     if (context.get("printWoTaskAction") != null)
/* 3348 */       return (nc.ui.ewm.workorder.action.PrintWoTaskAction1)context.get("printWoTaskAction");
/* 3349 */     nc.ui.ewm.workorder.action.PrintWoTaskAction1 bean = new nc.ui.ewm.workorder.action.PrintWoTaskAction1();
/* 3350 */     context.put("printWoTaskAction", bean);
/* 3351 */     bean.setModel(getModel());
/* 3352 */     bean.setScaleProcessor(getBillVODigitProcessor());
/* 3353 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3354 */     invokeInitializingBean(bean);
/* 3355 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.PrintWoReportAction1 getPrintWoReportAction() {
/* 3359 */     if (context.get("printWoReportAction") != null)
/* 3360 */       return (nc.ui.ewm.workorder.action.PrintWoReportAction1)context.get("printWoReportAction");
/* 3361 */     nc.ui.ewm.workorder.action.PrintWoReportAction1 bean = new nc.ui.ewm.workorder.action.PrintWoReportAction1();
/* 3362 */     context.put("printWoReportAction", bean);
/* 3363 */     bean.setModel(getModel());
/* 3364 */     bean.setScaleProcessor(getBillVODigitProcessor());
/* 3365 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3366 */     invokeInitializingBean(bean);
/* 3367 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.action.PrintWoCostItemAction1 getPrintWoCostItemAction() {
/* 3371 */     if (context.get("printWoCostItemAction") != null)
/* 3372 */       return (nc.ui.ewm.workorder.action.PrintWoCostItemAction1)context.get("printWoCostItemAction");
/* 3373 */     nc.ui.ewm.workorder.action.PrintWoCostItemAction1 bean = new nc.ui.ewm.workorder.action.PrintWoCostItemAction1();
/* 3374 */     context.put("printWoCostItemAction", bean);
/* 3375 */     bean.setModel(getModel());
/* 3376 */     bean.setScaleProcessor(getBillVODigitProcessor());
/* 3377 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3378 */     invokeInitializingBean(bean);
/* 3379 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.pub.am.common.validator.OrgPermissionValidator getOrgPermissionValidator() {
/* 3383 */     if (context.get("orgPermissionValidator") != null)
/* 3384 */       return (nc.pub.am.common.validator.OrgPermissionValidator)context.get("orgPermissionValidator");
/* 3385 */     nc.pub.am.common.validator.OrgPermissionValidator bean = new nc.pub.am.common.validator.OrgPermissionValidator();
/* 3386 */     context.put("orgPermissionValidator", bean);
/* 3387 */     bean.setContext(getContext());
/* 3388 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3389 */     invokeInitializingBean(bean);
/* 3390 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.bd.attach.AttachAction getAttachmentActionCard() {
/* 3394 */     if (context.get("attachmentActionCard") != null)
/* 3395 */       return (nc.ui.bd.attach.AttachAction)context.get("attachmentActionCard");
/* 3396 */     nc.ui.bd.attach.AttachAction bean = new nc.ui.bd.attach.AttachAction();
/* 3397 */     context.put("attachmentActionCard", bean);
/* 3398 */     bean.setModel(getModel());
/* 3399 */     bean.setPrefix("ewm/wo");
/* 3400 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3401 */     invokeInitializingBean(bean);
/* 3402 */     return bean;
/*      */   }
/*      */   
/*      */   public ArrayList getCardViewActions() {
/* 3406 */     if (context.get("cardViewActions") != null)
/* 3407 */       return (ArrayList)context.get("cardViewActions");
/* 3408 */     ArrayList bean = new ArrayList(getManagedList50());context.put("cardViewActions", bean);
/* 3409 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3410 */     invokeInitializingBean(bean);
/* 3411 */     return bean;
/*      */   }
/*      */   
/* 3414 */   private List getManagedList50() { List list = new ArrayList();list.add(getAddAction());list.add(getEditAction());list.add(getDeleteAction());list.add(getCopyAction());list.add(getSeparatorAction());list.add(getQueryAction());list.add(getRefreshAction());list.add(getSeparatorAction());list.add(getAlterstatusaction());list.add(getCommitGroupAction());list.add(getWorkflowMenuAction());list.add(getAssMenuAction());list.add(getSeparatorAction());list.add(getQueryAboutMenuAction());list.add(getSeparatorAction());list.add(getRelaFuncAction());list.add(getSeparatorAction());list.add(getPrintMenuAction());list.add(getSeparatorAction());list.add(getAlterDateAction());return list;
/*      */   }
/*      */   
/* 3417 */   public ArrayList getCardEditActions() { if (context.get("cardEditActions") != null)
/* 3418 */       return (ArrayList)context.get("cardEditActions");
/* 3419 */     ArrayList bean = new ArrayList(getManagedList51());context.put("cardEditActions", bean);
/* 3420 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3421 */     invokeInitializingBean(bean);
/* 3422 */     return bean;
/*      */   }
/*      */   
/* 3425 */   private List getManagedList51() { List list = new ArrayList();list.add(getSaveAction());list.add(getSeparatorAction());list.add(getCancelAction());list.add(getSeparatorAction());list.add(getLinkStockOutBillAction());list.add(getSeparatorAction());list.add(getQueryAboutMenuActionEdit());return list;
/*      */   }
/*      */   
/* 3428 */   public ArrayList getListViewActions() { if (context.get("listViewActions") != null)
/* 3429 */       return (ArrayList)context.get("listViewActions");
/* 3430 */     ArrayList bean = new ArrayList(getManagedList52());context.put("listViewActions", bean);
/* 3431 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3432 */     invokeInitializingBean(bean);
/* 3433 */     return bean;
/*      */   }
/*      */   
/* 3436 */   private List getManagedList52() { List list = new ArrayList();list.add(getAddAction());list.add(getEditAction());list.add(getDeleteAction());list.add(getCopyAction());list.add(getSeparatorAction());list.add(getQueryAction());list.add(getRefreshAllAction());list.add(getSeparatorAction());list.add(getAlterstatusaction());list.add(getCommitGroupAction());list.add(getWorkflowMenuAction());list.add(getAssMenuAction());list.add(getSeparatorAction());list.add(getQueryAboutMenuAction());list.add(getSeparatorAction());list.add(getRelaFuncAction());list.add(getSeparatorAction());list.add(getPrintMenuAction());list.add(getSeparatorAction());list.add(getAlterDateAction());return list;
/*      */   }
/*      */   
/* 3439 */   public java.util.HashMap getBodyActionsMap() { if (context.get("bodyActionsMap") != null)
/* 3440 */       return (java.util.HashMap)context.get("bodyActionsMap");
/* 3441 */     java.util.HashMap bean = new java.util.HashMap(getManagedMap0());context.put("bodyActionsMap", bean);
/* 3442 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3443 */     invokeInitializingBean(bean);
/* 3444 */     return bean;
/*      */   }
/*      */   
/* 3447 */   private Map getManagedMap0() { Map map = new java.util.HashMap();map.put("wo_taskobj", getManagedList53());map.put("wo_task", getManagedList54());map.put("wo_plan_inv", getManagedList55());map.put("wo_plan_psn", getManagedList56());map.put("wo_plan_tool", getManagedList57());map.put("wo_planotherexes", getManagedList58());map.put("wo_precaution", getManagedList59());map.put("wo_permit", getManagedList60());map.put("wo_actual_inv", getManagedList61());map.put("wo_actual_psn", getManagedList62());map.put("wo_actual_tool", getManagedList63());map.put("wo_actualotherexes", getManagedList64());map.put("wo_part", getManagedList65());map.put("wo_log", getManagedList66());map.put("wo_structure", getManagedList67());return map; }
/*      */   
/* 3449 */   private List getManagedList53() { List list = new ArrayList();list.add(getLineAddAction_133c165());list.add(getLineInsertAction());list.add(getLineDeleteAction_ddf475());list.add(getTableEditLineAction());list.add(getActionsBar_ActionsBarSeparator_1dc476a());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3452 */   private nc.ui.ewm.workorder.tabaction.LineAddAction getLineAddAction_133c165() { if (context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#133c165") != null)
/* 3453 */       return (nc.ui.ewm.workorder.tabaction.LineAddAction)context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#133c165");
/* 3454 */     nc.ui.ewm.workorder.tabaction.LineAddAction bean = new nc.ui.ewm.workorder.tabaction.LineAddAction();
/* 3455 */     context.put("nc.ui.ewm.workorder.tabaction.LineAddAction#133c165", bean);
/* 3456 */     bean.setModel(getModel());
/* 3457 */     bean.setBillForm(getBillForm());
/* 3458 */     bean.setTabcode("wo_taskobj");
/* 3459 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3460 */     invokeInitializingBean(bean);
/* 3461 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.tabaction.LineDeleteAction getLineDeleteAction_ddf475() {
/* 3465 */     if (context.get("nc.ui.ewm.workorder.tabaction.LineDeleteAction#ddf475") != null)
/* 3466 */       return (nc.ui.ewm.workorder.tabaction.LineDeleteAction)context.get("nc.ui.ewm.workorder.tabaction.LineDeleteAction#ddf475");
/* 3467 */     nc.ui.ewm.workorder.tabaction.LineDeleteAction bean = new nc.ui.ewm.workorder.tabaction.LineDeleteAction();
/* 3468 */     context.put("nc.ui.ewm.workorder.tabaction.LineDeleteAction#ddf475", bean);
/* 3469 */     bean.setModel(getModel());
/* 3470 */     bean.setBillForm(getBillForm());
/* 3471 */     bean.setTabcode("wo_taskobj");
/* 3472 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3473 */     invokeInitializingBean(bean);
/* 3474 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1dc476a() {
/* 3478 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1dc476a") != null)
/* 3479 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1dc476a");
/* 3480 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3481 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1dc476a", bean);
/* 3482 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3483 */     invokeInitializingBean(bean);
/* 3484 */     return bean;
/*      */   }
/*      */   
/* 3487 */   private List getManagedList54() { List list = new ArrayList();list.add(getLineAddAction_d48534());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getBatchAlterActionTask());list.add(getActionsBar_ActionsBarSeparator_754889());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3490 */   private nc.ui.ewm.workorder.tabaction.LineAddAction getLineAddAction_d48534() { if (context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#d48534") != null)
/* 3491 */       return (nc.ui.ewm.workorder.tabaction.LineAddAction)context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#d48534");
/* 3492 */     nc.ui.ewm.workorder.tabaction.LineAddAction bean = new nc.ui.ewm.workorder.tabaction.LineAddAction();
/* 3493 */     context.put("nc.ui.ewm.workorder.tabaction.LineAddAction#d48534", bean);
/* 3494 */     bean.setModel(getModel());
/* 3495 */     bean.setBillForm(getBillForm());
/* 3496 */     bean.setTabcode("wo_task");
/* 3497 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3498 */     invokeInitializingBean(bean);
/* 3499 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_754889() {
/* 3503 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#754889") != null)
/* 3504 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#754889");
/* 3505 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3506 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#754889", bean);
/* 3507 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3508 */     invokeInitializingBean(bean);
/* 3509 */     return bean;
/*      */   }
/*      */   
/* 3512 */   private List getManagedList55() { List list = new ArrayList();list.add(getLineAddAction_1ed1ca8());list.add(getLineInsertAction());list.add(getLineDeleteAction_1f4722c());list.add(getTableEditLineAction());list.add(getBatchAlterPlanInv());list.add(getChoosePartAction());list.add(getActionsBar_ActionsBarSeparator_173e43a());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3515 */   private nc.ui.ewm.workorder.tabaction.LineAddAction getLineAddAction_1ed1ca8() { if (context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#1ed1ca8") != null)
/* 3516 */       return (nc.ui.ewm.workorder.tabaction.LineAddAction)context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#1ed1ca8");
/* 3517 */     nc.ui.ewm.workorder.tabaction.LineAddAction bean = new nc.ui.ewm.workorder.tabaction.LineAddAction();
/* 3518 */     context.put("nc.ui.ewm.workorder.tabaction.LineAddAction#1ed1ca8", bean);
/* 3519 */     bean.setModel(getModel());
/* 3520 */     bean.setBillForm(getBillForm());
/* 3521 */     bean.setTabcode("wo_plan_inv");
/* 3522 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3523 */     invokeInitializingBean(bean);
/* 3524 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.tabaction.LineDeleteAction getLineDeleteAction_1f4722c() {
/* 3528 */     if (context.get("nc.ui.ewm.workorder.tabaction.LineDeleteAction#1f4722c") != null)
/* 3529 */       return (nc.ui.ewm.workorder.tabaction.LineDeleteAction)context.get("nc.ui.ewm.workorder.tabaction.LineDeleteAction#1f4722c");
/* 3530 */     nc.ui.ewm.workorder.tabaction.LineDeleteAction bean = new nc.ui.ewm.workorder.tabaction.LineDeleteAction();
/* 3531 */     context.put("nc.ui.ewm.workorder.tabaction.LineDeleteAction#1f4722c", bean);
/* 3532 */     bean.setModel(getModel());
/* 3533 */     bean.setBillForm(getBillForm());
/* 3534 */     bean.setTabcode("wo_plan_inv");
/* 3535 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3536 */     invokeInitializingBean(bean);
/* 3537 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_173e43a() {
/* 3541 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#173e43a") != null)
/* 3542 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#173e43a");
/* 3543 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3544 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#173e43a", bean);
/* 3545 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3546 */     invokeInitializingBean(bean);
/* 3547 */     return bean;
/*      */   }
/*      */   
/* 3550 */   private List getManagedList56() { List list = new ArrayList();list.add(getLineAddAction());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getBatchAlterPlanPsn());list.add(getActionsBar_ActionsBarSeparator_18cc59d());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3553 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_18cc59d() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#18cc59d") != null)
/* 3554 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#18cc59d");
/* 3555 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3556 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#18cc59d", bean);
/* 3557 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3558 */     invokeInitializingBean(bean);
/* 3559 */     return bean;
/*      */   }
/*      */   
/* 3562 */   private List getManagedList57() { List list = new ArrayList();list.add(getLineAddAction());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getBatchAlterPlanTool());list.add(getActionsBar_ActionsBarSeparator_101ce64());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3565 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_101ce64() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#101ce64") != null)
/* 3566 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#101ce64");
/* 3567 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3568 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#101ce64", bean);
/* 3569 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3570 */     invokeInitializingBean(bean);
/* 3571 */     return bean;
/*      */   }
/*      */   
/* 3574 */   private List getManagedList58() { List list = new ArrayList();list.add(getLineAddAction());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getActionsBar_ActionsBarSeparator_1953a04());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3577 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1953a04() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1953a04") != null)
/* 3578 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1953a04");
/* 3579 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3580 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1953a04", bean);
/* 3581 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3582 */     invokeInitializingBean(bean);
/* 3583 */     return bean;
/*      */   }
/*      */   
/* 3586 */   private List getManagedList59() { List list = new ArrayList();list.add(getLineAddAction());list.add(getLineInsertAction());list.add(getLineDeleteAction_13e28c());list.add(getTableEditLineAction());list.add(getActionsBar_ActionsBarSeparator_17e8fd6());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3589 */   private nc.ui.ewm.workorder.tabaction.LineDeleteAction getLineDeleteAction_13e28c() { if (context.get("nc.ui.ewm.workorder.tabaction.LineDeleteAction#13e28c") != null)
/* 3590 */       return (nc.ui.ewm.workorder.tabaction.LineDeleteAction)context.get("nc.ui.ewm.workorder.tabaction.LineDeleteAction#13e28c");
/* 3591 */     nc.ui.ewm.workorder.tabaction.LineDeleteAction bean = new nc.ui.ewm.workorder.tabaction.LineDeleteAction();
/* 3592 */     context.put("nc.ui.ewm.workorder.tabaction.LineDeleteAction#13e28c", bean);
/* 3593 */     bean.setModel(getModel());
/* 3594 */     bean.setBillForm(getBillForm());
/* 3595 */     bean.setTabcode("wo_precaution");
/* 3596 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3597 */     invokeInitializingBean(bean);
/* 3598 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_17e8fd6() {
/* 3602 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#17e8fd6") != null)
/* 3603 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#17e8fd6");
/* 3604 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3605 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#17e8fd6", bean);
/* 3606 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3607 */     invokeInitializingBean(bean);
/* 3608 */     return bean;
/*      */   }
/*      */   
/* 3611 */   private List getManagedList60() { List list = new ArrayList();list.add(getLineAddAction());list.add(getLineDeleteAction());list.add(getActionsBar_ActionsBarSeparator_1567e4());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3614 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1567e4() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1567e4") != null)
/* 3615 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1567e4");
/* 3616 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3617 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1567e4", bean);
/* 3618 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3619 */     invokeInitializingBean(bean);
/* 3620 */     return bean;
/*      */   }
/*      */   
/* 3623 */   private List getManagedList61() { List list = new ArrayList();list.add(getLineAddAction_157b50f());list.add(getLineInsertAction());list.add(getLineDeleteAction_1301895());list.add(getTableEditLineAction());list.add(getBatchAlterActualInv());list.add(getCopyPlanInvAction());list.add(getActionsBar_ActionsBarSeparator_9dfe9b());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3626 */   private nc.ui.ewm.workorder.tabaction.LineAddAction getLineAddAction_157b50f() { if (context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#157b50f") != null)
/* 3627 */       return (nc.ui.ewm.workorder.tabaction.LineAddAction)context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#157b50f");
/* 3628 */     nc.ui.ewm.workorder.tabaction.LineAddAction bean = new nc.ui.ewm.workorder.tabaction.LineAddAction();
/* 3629 */     context.put("nc.ui.ewm.workorder.tabaction.LineAddAction#157b50f", bean);
/* 3630 */     bean.setModel(getModel());
/* 3631 */     bean.setBillForm(getBillForm());
/* 3632 */     bean.setTabcode("wo_actual_inv");
/* 3633 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3634 */     invokeInitializingBean(bean);
/* 3635 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.tabaction.LineDeleteAction getLineDeleteAction_1301895() {
/* 3639 */     if (context.get("nc.ui.ewm.workorder.tabaction.LineDeleteAction#1301895") != null)
/* 3640 */       return (nc.ui.ewm.workorder.tabaction.LineDeleteAction)context.get("nc.ui.ewm.workorder.tabaction.LineDeleteAction#1301895");
/* 3641 */     nc.ui.ewm.workorder.tabaction.LineDeleteAction bean = new nc.ui.ewm.workorder.tabaction.LineDeleteAction();
/* 3642 */     context.put("nc.ui.ewm.workorder.tabaction.LineDeleteAction#1301895", bean);
/* 3643 */     bean.setModel(getModel());
/* 3644 */     bean.setBillForm(getBillForm());
/* 3645 */     bean.setTabcode("wo_actual_inv");
/* 3646 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3647 */     invokeInitializingBean(bean);
/* 3648 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_9dfe9b() {
/* 3652 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#9dfe9b") != null)
/* 3653 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#9dfe9b");
/* 3654 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3655 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#9dfe9b", bean);
/* 3656 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3657 */     invokeInitializingBean(bean);
/* 3658 */     return bean;
/*      */   }
/*      */   
/* 3661 */   private List getManagedList62() { List list = new ArrayList();list.add(getLineAddAction_157b50f1());list.add(getLineInsertAction_13018951());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getBatchAlterActualPsn());list.add(getCopyPlanPsnAction());list.add(getActionsBar_ActionsBarSeparator_b6aad9());list.add(getBodyViewMaxAction());return list;
/*      */   }
		
		private nc.ui.ewm.workorder.tabaction.LineAddAction getLineAddAction_157b50f1() { if (context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#157b50f1") != null)
			/* 3627 */       return (nc.ui.ewm.workorder.tabaction.LineAddAction)context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#157b50f1");
			/* 3628 */     nc.ui.ewm.workorder.tabaction.LineAddAction bean = new nc.ui.ewm.workorder.tabaction.LineAddAction();
			/* 3629 */     context.put("nc.ui.ewm.workorder.tabaction.LineAddAction#157b50f1", bean);
			/* 3630 */     bean.setModel(getModel());
			/* 3631 */     bean.setBillForm(getBillForm());
			/* 3632 */     bean.setTabcode("wo_actual_psn");
			/* 3633 */     setBeanFacotryIfBeanFacatoryAware(bean);
			/* 3634 */     invokeInitializingBean(bean);
			/* 3635 */     return bean;
			/*      */   }
			/*      */   
			/*      */   private nc.ui.ewm.workorder.tabaction.LineInsertAction getLineInsertAction_13018951() {
			/* 3639 */     if (context.get("nc.ui.ewm.workorder.tabaction.LineInsertAction#13018951") != null)
			/* 3640 */       return (nc.ui.ewm.workorder.tabaction.LineInsertAction)context.get("nc.ui.ewm.workorder.tabaction.LineInsertAction#13018951");
			/* 3641 */     nc.ui.ewm.workorder.tabaction.LineInsertAction bean = new nc.ui.ewm.workorder.tabaction.LineInsertAction();
			/* 3642 */     context.put("nc.ui.ewm.workorder.tabaction.LineInsertAction#13018951", bean);
			/* 3643 */     bean.setModel(getModel());
			/* 3644 */     bean.setBillForm(getBillForm());
			/* 3645 */     bean.setTabcode("wo_actual_psn");
			/* 3646 */     setBeanFacotryIfBeanFacatoryAware(bean);
			/* 3647 */     invokeInitializingBean(bean);
			/* 3648 */     return bean;
			/*      */   }
/*      */   
/* 3664 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_b6aad9() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#b6aad9") != null)
/* 3665 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#b6aad9");
/* 3666 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3667 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#b6aad9", bean);
/* 3668 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3669 */     invokeInitializingBean(bean);
/* 3670 */     return bean;
/*      */   }
/*      */   
/* 3673 */   private List getManagedList63() { List list = new ArrayList();list.add(getLineAddAction());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getBatchAlterActualTool());list.add(getCopyPlanToolAction());list.add(getActionsBar_ActionsBarSeparator_8ab0d4());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3676 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_8ab0d4() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8ab0d4") != null)
/* 3677 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8ab0d4");
/* 3678 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3679 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#8ab0d4", bean);
/* 3680 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3681 */     invokeInitializingBean(bean);
/* 3682 */     return bean;
/*      */   }
/*      */   
/* 3685 */   private List getManagedList64() { List list = new ArrayList();list.add(getLineAddAction());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getCopyPlanOtherExesAction());list.add(getActionsBar_ActionsBarSeparator_15f2f99());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3688 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_15f2f99() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#15f2f99") != null)
/* 3689 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#15f2f99");
/* 3690 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3691 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#15f2f99", bean);
/* 3692 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3693 */     invokeInitializingBean(bean);
/* 3694 */     return bean;
/*      */   }
/*      */   
/* 3697 */   private List getManagedList65() { List list = new ArrayList();list.add(getLineAddAction_2e16b9());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getBatchAlterPart());list.add(getChoosePartAction());list.add(getActionsBar_ActionsBarSeparator_1d5fe0a());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3700 */   private nc.ui.ewm.workorder.tabaction.LineAddAction getLineAddAction_2e16b9() { if (context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#2e16b9") != null)
/* 3701 */       return (nc.ui.ewm.workorder.tabaction.LineAddAction)context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#2e16b9");
/* 3702 */     nc.ui.ewm.workorder.tabaction.LineAddAction bean = new nc.ui.ewm.workorder.tabaction.LineAddAction();
/* 3703 */     context.put("nc.ui.ewm.workorder.tabaction.LineAddAction#2e16b9", bean);
/* 3704 */     bean.setModel(getModel());
/* 3705 */     bean.setBillForm(getBillForm());
/* 3706 */     bean.setTabcode("wo_part");
/* 3707 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3708 */     invokeInitializingBean(bean);
/* 3709 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1d5fe0a() {
/* 3713 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1d5fe0a") != null)
/* 3714 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1d5fe0a");
/* 3715 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3716 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1d5fe0a", bean);
/* 3717 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3718 */     invokeInitializingBean(bean);
/* 3719 */     return bean;
/*      */   }
/*      */   
/* 3722 */   private List getManagedList66() { List list = new ArrayList();list.add(getLineAddAction_196343());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getActionsBar_ActionsBarSeparator_c40255());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3725 */   private nc.ui.ewm.workorder.tabaction.LineAddAction getLineAddAction_196343() { if (context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#196343") != null)
/* 3726 */       return (nc.ui.ewm.workorder.tabaction.LineAddAction)context.get("nc.ui.ewm.workorder.tabaction.LineAddAction#196343");
/* 3727 */     nc.ui.ewm.workorder.tabaction.LineAddAction bean = new nc.ui.ewm.workorder.tabaction.LineAddAction();
/* 3728 */     context.put("nc.ui.ewm.workorder.tabaction.LineAddAction#196343", bean);
/* 3729 */     bean.setModel(getModel());
/* 3730 */     bean.setBillForm(getBillForm());
/* 3731 */     bean.setTabcode("wo_log");
/* 3732 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3733 */     invokeInitializingBean(bean);
/* 3734 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_c40255() {
/* 3738 */     if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#c40255") != null)
/* 3739 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#c40255");
/* 3740 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3741 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#c40255", bean);
/* 3742 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3743 */     invokeInitializingBean(bean);
/* 3744 */     return bean;
/*      */   }
/*      */   
/* 3747 */   private List getManagedList67() { List list = new ArrayList();list.add(getLineAddAction());list.add(getLineInsertAction());list.add(getLineDeleteAction());list.add(getTableEditLineAction());list.add(getActionsBar_ActionsBarSeparator_752726());list.add(getBodyViewMaxAction());return list;
/*      */   }
/*      */   
/* 3750 */   private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_752726() { if (context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#752726") != null)
/* 3751 */       return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#752726");
/* 3752 */     nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
/* 3753 */     context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#752726", bean);
/* 3754 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3755 */     invokeInitializingBean(bean);
/* 3756 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.WoMaterialEditHandler getMaterialEditPlan() {
/* 3760 */     if (context.get("MaterialEditPlan") != null)
/* 3761 */       return (nc.ui.ewm.workorder.event.WoMaterialEditHandler)context.get("MaterialEditPlan");
/* 3762 */     nc.ui.ewm.workorder.event.WoMaterialEditHandler bean = new nc.ui.ewm.workorder.event.WoMaterialEditHandler();
/* 3763 */     context.put("MaterialEditPlan", bean);
/* 3764 */     bean.setMaterialKeyFields(getMaterialKeyFieldsPlan());
/* 3765 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3766 */     invokeInitializingBean(bean);
/* 3767 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scaleevent.EditEventHandler getPlanToolEditEventHandler() {
/* 3771 */     if (context.get("PlanToolEditEventHandler") != null)
/* 3772 */       return (nc.ui.am.scaleevent.EditEventHandler)context.get("PlanToolEditEventHandler");
/* 3773 */     nc.ui.am.scaleevent.EditEventHandler bean = new nc.ui.am.scaleevent.EditEventHandler();
/* 3774 */     context.put("PlanToolEditEventHandler", bean);
/* 3775 */     bean.setKeyFields(getPlanToolKeyFields());
/* 3776 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3777 */     invokeInitializingBean(bean);
/* 3778 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scaleevent.DefaultKeyFields getPlanToolKeyFields() {
/* 3782 */     if (context.get("PlanToolKeyFields") != null)
/* 3783 */       return (nc.ui.am.scaleevent.DefaultKeyFields)context.get("PlanToolKeyFields");
/* 3784 */     nc.ui.am.scaleevent.DefaultKeyFields bean = new nc.ui.am.scaleevent.DefaultKeyFields();
/* 3785 */     context.put("PlanToolKeyFields", bean);
/* 3786 */     bean.setTableCode("wo_plan_tool");
/* 3787 */     bean.setNum("tools_num");
/* 3788 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3789 */     invokeInitializingBean(bean);
/* 3790 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scaleevent.EditEventHandler getActualToolEditEventHandler() {
/* 3794 */     if (context.get("ActualToolEditEventHandler") != null)
/* 3795 */       return (nc.ui.am.scaleevent.EditEventHandler)context.get("ActualToolEditEventHandler");
/* 3796 */     nc.ui.am.scaleevent.EditEventHandler bean = new nc.ui.am.scaleevent.EditEventHandler();
/* 3797 */     context.put("ActualToolEditEventHandler", bean);
/* 3798 */     bean.setKeyFields(getActualToolKeyFields());
/* 3799 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3800 */     invokeInitializingBean(bean);
/* 3801 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scaleevent.DefaultKeyFields getActualToolKeyFields() {
/* 3805 */     if (context.get("ActualToolKeyFields") != null)
/* 3806 */       return (nc.ui.am.scaleevent.DefaultKeyFields)context.get("ActualToolKeyFields");
/* 3807 */     nc.ui.am.scaleevent.DefaultKeyFields bean = new nc.ui.am.scaleevent.DefaultKeyFields();
/* 3808 */     context.put("ActualToolKeyFields", bean);
/* 3809 */     bean.setTableCode("wo_actual_tool");
/* 3810 */     bean.setNum("tools_num");
/* 3811 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3812 */     invokeInitializingBean(bean);
/* 3813 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scaleevent.EditEventHandler getPlanPsnEditEventHandler() {
/* 3817 */     if (context.get("PlanPsnEditEventHandler") != null)
/* 3818 */       return (nc.ui.am.scaleevent.EditEventHandler)context.get("PlanPsnEditEventHandler");
/* 3819 */     nc.ui.am.scaleevent.EditEventHandler bean = new nc.ui.am.scaleevent.EditEventHandler();
/* 3820 */     context.put("PlanPsnEditEventHandler", bean);
/* 3821 */     bean.setKeyFields(getPlanPsnKeyFields());
/* 3822 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3823 */     invokeInitializingBean(bean);
/* 3824 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scaleevent.DefaultKeyFields getPlanPsnKeyFields() {
/* 3828 */     if (context.get("PlanPsnKeyFields") != null)
/* 3829 */       return (nc.ui.am.scaleevent.DefaultKeyFields)context.get("PlanPsnKeyFields");
/* 3830 */     nc.ui.am.scaleevent.DefaultKeyFields bean = new nc.ui.am.scaleevent.DefaultKeyFields();
/* 3831 */     context.put("PlanPsnKeyFields", bean);
/* 3832 */     bean.setTableCode("wo_plan_psn");
/* 3833 */     bean.setNum("person_num");
/* 3834 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3835 */     invokeInitializingBean(bean);
/* 3836 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.WoActualPsnEditHandler getActualPsnEditEventHandler() {
/* 3840 */     if (context.get("ActualPsnEditEventHandler") != null)
/* 3841 */       return (nc.ui.ewm.workorder.event.WoActualPsnEditHandler)context.get("ActualPsnEditEventHandler");
/* 3842 */     nc.ui.ewm.workorder.event.WoActualPsnEditHandler bean = new nc.ui.ewm.workorder.event.WoActualPsnEditHandler();
/* 3843 */     context.put("ActualPsnEditEventHandler", bean);
/* 3844 */     bean.setKeyFields(getActualPsnKeyFields());
/* 3845 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3846 */     invokeInitializingBean(bean);
/* 3847 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.scaleevent.DefaultKeyFields getActualPsnKeyFields() {
/* 3851 */     if (context.get("ActualPsnKeyFields") != null)
/* 3852 */       return (nc.ui.am.scaleevent.DefaultKeyFields)context.get("ActualPsnKeyFields");
/* 3853 */     nc.ui.am.scaleevent.DefaultKeyFields bean = new nc.ui.am.scaleevent.DefaultKeyFields();
/* 3854 */     context.put("ActualPsnKeyFields", bean);
/* 3855 */     bean.setTableCode("wo_actual_psn");
/* 3856 */     bean.setNum("person_num");
/* 3857 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3858 */     invokeInitializingBean(bean);
/* 3859 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.material.MaterialEditHandler getMaterialEditActual() {
/* 3863 */     if (context.get("MaterialEditActual") != null)
/* 3864 */       return (nc.ui.am.material.MaterialEditHandler)context.get("MaterialEditActual");
/* 3865 */     nc.ui.am.material.MaterialEditHandler bean = new nc.ui.am.material.MaterialEditHandler();
/* 3866 */     context.put("MaterialEditActual", bean);
/* 3867 */     bean.setMaterialKeyFields(getMaterialKeyFieldsActual());
/* 3868 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3869 */     invokeInitializingBean(bean);
/* 3870 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.material.MaterialEditHandler getMaterialEditWOPart() {
/* 3874 */     if (context.get("MaterialEditWOPart") != null)
/* 3875 */       return (nc.ui.am.material.MaterialEditHandler)context.get("MaterialEditWOPart");
/* 3876 */     nc.ui.am.material.MaterialEditHandler bean = new nc.ui.am.material.MaterialEditHandler();
/* 3877 */     context.put("MaterialEditWOPart", bean);
/* 3878 */     bean.setMaterialKeyFields(getMaterialKeyFieldsWOPart());
/* 3879 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3880 */     invokeInitializingBean(bean);
/* 3881 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.material.DefaultMaterialKeyFields getMaterialKeyFieldsPlan() {
/* 3885 */     if (context.get("materialKeyFieldsPlan") != null)
/* 3886 */       return (nc.ui.am.material.DefaultMaterialKeyFields)context.get("materialKeyFieldsPlan");
/* 3887 */     nc.ui.am.material.DefaultMaterialKeyFields bean = new nc.ui.am.material.DefaultMaterialKeyFields();
/* 3888 */     context.put("materialKeyFieldsPlan", bean);
/* 3889 */     bean.setMaterialBodyTabCode("wo_plan_inv");
/* 3890 */     bean.setPriceField("price_org");
/* 3891 */     bean.setTotalField("money_org");
/* 3892 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3893 */     invokeInitializingBean(bean);
/* 3894 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.material.DefaultMaterialKeyFields getMaterialKeyFieldsActual() {
/* 3898 */     if (context.get("materialKeyFieldsActual") != null)
/* 3899 */       return (nc.ui.am.material.DefaultMaterialKeyFields)context.get("materialKeyFieldsActual");
/* 3900 */     nc.ui.am.material.DefaultMaterialKeyFields bean = new nc.ui.am.material.DefaultMaterialKeyFields();
/* 3901 */     context.put("materialKeyFieldsActual", bean);
/* 3902 */     bean.setMaterialBodyTabCode("wo_actual_inv");
/* 3903 */     bean.setPriceField("price_org");
/* 3904 */     bean.setTotalField("money_org");
/* 3905 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3906 */     invokeInitializingBean(bean);
/* 3907 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.material.DefaultMaterialKeyFields getMaterialKeyFieldsWOPart() {
/* 3911 */     if (context.get("materialKeyFieldsWOPart") != null)
/* 3912 */       return (nc.ui.am.material.DefaultMaterialKeyFields)context.get("materialKeyFieldsWOPart");
/* 3913 */     nc.ui.am.material.DefaultMaterialKeyFields bean = new nc.ui.am.material.DefaultMaterialKeyFields();
/* 3914 */     context.put("materialKeyFieldsWOPart", bean);
/* 3915 */     bean.setMaterialBodyTabCode("wo_part");
/* 3916 */     bean.setPriceField("price_org");
/* 3917 */     bean.setTotalField("money_org");
/* 3918 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3919 */     invokeInitializingBean(bean);
/* 3920 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.WorkOrderEditHandler getWorkOrderEditHandler() {
/* 3924 */     if (context.get("WorkOrderEditHandler") != null)
/* 3925 */       return (nc.ui.ewm.workorder.event.WorkOrderEditHandler)context.get("WorkOrderEditHandler");
/* 3926 */     nc.ui.ewm.workorder.event.WorkOrderEditHandler bean = new nc.ui.ewm.workorder.event.WorkOrderEditHandler();
/* 3927 */     context.put("WorkOrderEditHandler", bean);
/* 3928 */     bean.setBothEditEvent(getManagedList68());
/* 3929 */     bean.setHeadEditEvent(getHeadEditEventHandler());
/* 3930 */     bean.setStatedEditEvent(getManagedList69());
/* 3931 */     bean.setTabEditEvent(getManagedList70());
/* 3932 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3933 */     invokeInitializingBean(bean);
/* 3934 */     return bean;
/*      */   }
/*      */   
/* 3937 */   private List getManagedList68() { List list = new ArrayList();list.add(getWithRefInitCardEditEventHandler_88686f());return list;
/*      */   }
/*      */   
/* 3940 */   private nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler getWithRefInitCardEditEventHandler_88686f() { if (context.get("nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler#88686f") != null)
/* 3941 */       return (nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler)context.get("nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler#88686f");
/* 3942 */     nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler bean = new nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler();
/* 3943 */     context.put("nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler#88686f", bean);
/* 3944 */     bean.setInitRef(getWorkOrderInitRef());
/* 3945 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3946 */     invokeInitializingBean(bean);
/* 3947 */     return bean;
/*      */   }
/*      */   
/* 3950 */   private List getManagedList69() { List list = new ArrayList();list.add(getStatedEditEventHandler());return list; }
/*      */   
/* 3952 */   private List getManagedList70() { List list = new ArrayList();list.add(getTaskObjTabEditHandler());list.add(getTaskTabEditHandler());list.add(getInvTabEditHandler());list.add(getPsnTabEditHandler());list.add(getPrecautionTabEditHandler());list.add(getPermitTabEditHandler());return list;
/*      */   }
/*      */   
/* 3955 */   public nc.ui.ewm.workorder.event.pub.DefaultExCardEditEventHandler getHeadEditEventHandler() { if (context.get("headEditEventHandler") != null)
/* 3956 */       return (nc.ui.ewm.workorder.event.pub.DefaultExCardEditEventHandler)context.get("headEditEventHandler");
/* 3957 */     nc.ui.ewm.workorder.event.pub.DefaultExCardEditEventHandler bean = new nc.ui.ewm.workorder.event.pub.DefaultExCardEditEventHandler();
/* 3958 */     context.put("headEditEventHandler", bean);
/* 3959 */     bean.setSimpleItemEditEvents(getManagedList71());
/* 3960 */     bean.setItemEditEvents(getManagedList72());
/* 3961 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3962 */     invokeInitializingBean(bean);
/* 3963 */     return bean;
/*      */   }
/*      */   
/* 3966 */   private List getManagedList71() { List list = new ArrayList();list.add(getHeadEditHandler_180611f());return list;
/*      */   }
/*      */   
/* 3969 */   private nc.ui.ewm.workorder.event.headitem.HeadEditHandler getHeadEditHandler_180611f() { if (context.get("nc.ui.ewm.workorder.event.headitem.HeadEditHandler#180611f") != null)
/* 3970 */       return (nc.ui.ewm.workorder.event.headitem.HeadEditHandler)context.get("nc.ui.ewm.workorder.event.headitem.HeadEditHandler#180611f");
/* 3971 */     nc.ui.ewm.workorder.event.headitem.HeadEditHandler bean = new nc.ui.ewm.workorder.event.headitem.HeadEditHandler();
/* 3972 */     context.put("nc.ui.ewm.workorder.event.headitem.HeadEditHandler#180611f", bean);
/* 3973 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3974 */     invokeInitializingBean(bean);
/* 3975 */     return bean;
/*      */   }
/*      */   
/* 3978 */   private List getManagedList72() { List list = new ArrayList();list.add(getItemEditEvent4ConsignFlag_1684f82());list.add(getItemEditEvent4RepairPlan_13bf611());list.add(getItemEditEvent4StdJob_197fdb4());list.add(getItemEditEvent4SafetyJob_1914293());return list;
/*      */   }
/*      */   
/* 3981 */   private nc.ui.ewm.workorder.event.headitem.ItemEditEvent4ConsignFlag getItemEditEvent4ConsignFlag_1684f82() { if (context.get("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4ConsignFlag#1684f82") != null)
/* 3982 */       return (nc.ui.ewm.workorder.event.headitem.ItemEditEvent4ConsignFlag)context.get("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4ConsignFlag#1684f82");
/* 3983 */     nc.ui.ewm.workorder.event.headitem.ItemEditEvent4ConsignFlag bean = new nc.ui.ewm.workorder.event.headitem.ItemEditEvent4ConsignFlag();
/* 3984 */     context.put("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4ConsignFlag#1684f82", bean);
/* 3985 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3986 */     invokeInitializingBean(bean);
/* 3987 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.event.headitem.ItemEditEvent4RepairPlan getItemEditEvent4RepairPlan_13bf611() {
/* 3991 */     if (context.get("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4RepairPlan#13bf611") != null)
/* 3992 */       return (nc.ui.ewm.workorder.event.headitem.ItemEditEvent4RepairPlan)context.get("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4RepairPlan#13bf611");
/* 3993 */     nc.ui.ewm.workorder.event.headitem.ItemEditEvent4RepairPlan bean = new nc.ui.ewm.workorder.event.headitem.ItemEditEvent4RepairPlan();
/* 3994 */     context.put("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4RepairPlan#13bf611", bean);
/* 3995 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 3996 */     invokeInitializingBean(bean);
/* 3997 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.event.headitem.ItemEditEvent4StdJob getItemEditEvent4StdJob_197fdb4() {
/* 4001 */     if (context.get("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4StdJob#197fdb4") != null)
/* 4002 */       return (nc.ui.ewm.workorder.event.headitem.ItemEditEvent4StdJob)context.get("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4StdJob#197fdb4");
/* 4003 */     nc.ui.ewm.workorder.event.headitem.ItemEditEvent4StdJob bean = new nc.ui.ewm.workorder.event.headitem.ItemEditEvent4StdJob();
/* 4004 */     context.put("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4StdJob#197fdb4", bean);
/* 4005 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4006 */     invokeInitializingBean(bean);
/* 4007 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.event.headitem.ItemEditEvent4SafetyJob getItemEditEvent4SafetyJob_1914293() {
/* 4011 */     if (context.get("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4SafetyJob#1914293") != null)
/* 4012 */       return (nc.ui.ewm.workorder.event.headitem.ItemEditEvent4SafetyJob)context.get("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4SafetyJob#1914293");
/* 4013 */     nc.ui.ewm.workorder.event.headitem.ItemEditEvent4SafetyJob bean = new nc.ui.ewm.workorder.event.headitem.ItemEditEvent4SafetyJob();
/* 4014 */     context.put("nc.ui.ewm.workorder.event.headitem.ItemEditEvent4SafetyJob#1914293", bean);
/* 4015 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4016 */     invokeInitializingBean(bean);
/* 4017 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.pub.DefaultExCardEditEventHandler getStatedEditEventHandler() {
/* 4021 */     if (context.get("statedEditEventHandler") != null)
/* 4022 */       return (nc.ui.ewm.workorder.event.pub.DefaultExCardEditEventHandler)context.get("statedEditEventHandler");
/* 4023 */     nc.ui.ewm.workorder.event.pub.DefaultExCardEditEventHandler bean = new nc.ui.ewm.workorder.event.pub.DefaultExCardEditEventHandler();
/* 4024 */     context.put("statedEditEventHandler", bean);
/* 4025 */     bean.setSimpleItemEditEvents(getManagedList73());
/* 4026 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4027 */     invokeInitializingBean(bean);
/* 4028 */     return bean;
/*      */   }
/*      */   
/* 4031 */   private List getManagedList73() { List list = new ArrayList();list.add(getBodyCostEditHandler_1a9ccfb());list.add(getBodySequenceEditHandler_d0946a());return list;
/*      */   }
/*      */   
/* 4034 */   private nc.ui.ewm.workorder.event.bodyitem.BodyCostEditHandler getBodyCostEditHandler_1a9ccfb() { if (context.get("nc.ui.ewm.workorder.event.bodyitem.BodyCostEditHandler#1a9ccfb") != null)
/* 4035 */       return (nc.ui.ewm.workorder.event.bodyitem.BodyCostEditHandler)context.get("nc.ui.ewm.workorder.event.bodyitem.BodyCostEditHandler#1a9ccfb");
/* 4036 */     nc.ui.ewm.workorder.event.bodyitem.BodyCostEditHandler bean = new nc.ui.ewm.workorder.event.bodyitem.BodyCostEditHandler();
/* 4037 */     context.put("nc.ui.ewm.workorder.event.bodyitem.BodyCostEditHandler#1a9ccfb", bean);
/* 4038 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4039 */     invokeInitializingBean(bean);
/* 4040 */     return bean;
/*      */   }
/*      */   
/*      */   private nc.ui.ewm.workorder.event.bodyitem.BodySequenceEditHandler getBodySequenceEditHandler_d0946a() {
/* 4044 */     if (context.get("nc.ui.ewm.workorder.event.bodyitem.BodySequenceEditHandler#d0946a") != null)
/* 4045 */       return (nc.ui.ewm.workorder.event.bodyitem.BodySequenceEditHandler)context.get("nc.ui.ewm.workorder.event.bodyitem.BodySequenceEditHandler#d0946a");
/* 4046 */     nc.ui.ewm.workorder.event.bodyitem.BodySequenceEditHandler bean = new nc.ui.ewm.workorder.event.bodyitem.BodySequenceEditHandler();
/* 4047 */     context.put("nc.ui.ewm.workorder.event.bodyitem.BodySequenceEditHandler#d0946a", bean);
/* 4048 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4049 */     invokeInitializingBean(bean);
/* 4050 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler getInvTabEditHandler() {
/* 4054 */     if (context.get("invTabEditHandler") != null)
/* 4055 */       return (nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler)context.get("invTabEditHandler");
/* 4056 */     nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler bean = new nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler();
/* 4057 */     context.put("invTabEditHandler", bean);
/* 4058 */     bean.setTableCodes("wo_actual_inv;wo_plan_inv");
/* 4059 */     bean.setSimpleItemEditEvents(getManagedList74());
/* 4060 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4061 */     invokeInitializingBean(bean);
/* 4062 */     return bean;
/*      */   }
/*      */   
/* 4065 */   private List getManagedList74() { List list = new ArrayList();list.add(getBodyInvTabEditHandler_1bff609());return list;
/*      */   }
/*      */   
/* 4068 */   private nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyInvTabEditHandler getBodyInvTabEditHandler_1bff609() { if (context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyInvTabEditHandler#1bff609") != null)
/* 4069 */       return (nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyInvTabEditHandler)context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyInvTabEditHandler#1bff609");
/* 4070 */     nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyInvTabEditHandler bean = new nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyInvTabEditHandler();
/* 4071 */     context.put("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyInvTabEditHandler#1bff609", bean);
/* 4072 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4073 */     invokeInitializingBean(bean);
/* 4074 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler getPsnTabEditHandler() {
/* 4078 */     if (context.get("psnTabEditHandler") != null)
/* 4079 */       return (nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler)context.get("psnTabEditHandler");
/* 4080 */     nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler bean = new nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler();
/* 4081 */     context.put("psnTabEditHandler", bean);
/* 4082 */     bean.setTableCodes("wo_actual_psn;wo_plan_psn");
/* 4083 */     bean.setSimpleItemEditEvents(getManagedList75());
/* 4084 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4085 */     invokeInitializingBean(bean);
/* 4086 */     return bean;
/*      */   }
/*      */   
/* 4089 */   private List getManagedList75() { List list = new ArrayList();list.add(getBodyPsnTabEditHandler_1b1f7ee());return list;
/*      */   }
/*      */   
/* 4092 */   private nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPsnTabEditHandler getBodyPsnTabEditHandler_1b1f7ee() { if (context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPsnTabEditHandler#1b1f7ee") != null)
/* 4093 */       return (nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPsnTabEditHandler)context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPsnTabEditHandler#1b1f7ee");
/* 4094 */     nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPsnTabEditHandler bean = new nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPsnTabEditHandler();
/* 4095 */     context.put("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPsnTabEditHandler#1b1f7ee", bean);
/* 4096 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4097 */     invokeInitializingBean(bean);
/* 4098 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler getTaskObjTabEditHandler() {
/* 4102 */     if (context.get("taskObjTabEditHandler") != null)
/* 4103 */       return (nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler)context.get("taskObjTabEditHandler");
/* 4104 */     nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler bean = new nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler();
/* 4105 */     context.put("taskObjTabEditHandler", bean);
/* 4106 */     bean.setTableCodes("wo_taskobj");
/* 4107 */     bean.setSimpleItemEditEvents(getManagedList76());
/* 4108 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4109 */     invokeInitializingBean(bean);
/* 4110 */     return bean;
/*      */   }
/*      */   
/* 4113 */   private List getManagedList76() { List list = new ArrayList();list.add(getBodyTaskObjTabEditHandler_10c3669());return list;
/*      */   }
/*      */   
/* 4116 */   private nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskObjTabEditHandler getBodyTaskObjTabEditHandler_10c3669() { if (context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskObjTabEditHandler#10c3669") != null)
/* 4117 */       return (nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskObjTabEditHandler)context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskObjTabEditHandler#10c3669");
/* 4118 */     nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskObjTabEditHandler bean = new nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskObjTabEditHandler();
/* 4119 */     context.put("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskObjTabEditHandler#10c3669", bean);
/* 4120 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4121 */     invokeInitializingBean(bean);
/* 4122 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler getTaskTabEditHandler() {
/* 4126 */     if (context.get("taskTabEditHandler") != null)
/* 4127 */       return (nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler)context.get("taskTabEditHandler");
/* 4128 */     nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler bean = new nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler();
/* 4129 */     context.put("taskTabEditHandler", bean);
/* 4130 */     bean.setTableCodes("wo_task");
/* 4131 */     bean.setSimpleItemEditEvents(getManagedList77());
/* 4132 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4133 */     invokeInitializingBean(bean);
/* 4134 */     return bean;
/*      */   }
/*      */   
/* 4137 */   private List getManagedList77() { List list = new ArrayList();list.add(getBodyTaskTabEditHandler_c83a7f());return list;
/*      */   }
/*      */   
/* 4140 */   private nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskTabEditHandler getBodyTaskTabEditHandler_c83a7f() { if (context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskTabEditHandler#c83a7f") != null)
/* 4141 */       return (nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskTabEditHandler)context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskTabEditHandler#c83a7f");
/* 4142 */     nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskTabEditHandler bean = new nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskTabEditHandler();
/* 4143 */     context.put("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyTaskTabEditHandler#c83a7f", bean);
/* 4144 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4145 */     invokeInitializingBean(bean);
/* 4146 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler getPrecautionTabEditHandler() {
/* 4150 */     if (context.get("precautionTabEditHandler") != null)
/* 4151 */       return (nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler)context.get("precautionTabEditHandler");
/* 4152 */     nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler bean = new nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler();
/* 4153 */     context.put("precautionTabEditHandler", bean);
/* 4154 */     bean.setTableCodes("wo_precaution");
/* 4155 */     bean.setSimpleItemEditEvents(getManagedList78());
/* 4156 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4157 */     invokeInitializingBean(bean);
/* 4158 */     return bean;
/*      */   }
/*      */   
/* 4161 */   private List getManagedList78() { List list = new ArrayList();list.add(getBodyPrecautionEditHandler_1788b20());return list;
/*      */   }
/*      */   
/* 4164 */   private nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPrecautionEditHandler getBodyPrecautionEditHandler_1788b20() { if (context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPrecautionEditHandler#1788b20") != null)
/* 4165 */       return (nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPrecautionEditHandler)context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPrecautionEditHandler#1788b20");
/* 4166 */     nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPrecautionEditHandler bean = new nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPrecautionEditHandler();
/* 4167 */     context.put("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPrecautionEditHandler#1788b20", bean);
/* 4168 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4169 */     invokeInitializingBean(bean);
/* 4170 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler getPermitTabEditHandler() {
/* 4174 */     if (context.get("permitTabEditHandler") != null)
/* 4175 */       return (nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler)context.get("permitTabEditHandler");
/* 4176 */     nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler bean = new nc.ui.ewm.workorder.event.pub.DefaultCardTabEditEventHandler();
/* 4177 */     context.put("permitTabEditHandler", bean);
/* 4178 */     bean.setTableCodes("wo_permit");
/* 4179 */     bean.setSimpleItemEditEvents(getManagedList79());
/* 4180 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4181 */     invokeInitializingBean(bean);
/* 4182 */     return bean;
/*      */   }
/*      */   
/* 4185 */   private List getManagedList79() { List list = new ArrayList();list.add(getBodyPermitEditHandler_f5fd4c());return list;
/*      */   }
/*      */   
/* 4188 */   private nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPermitEditHandler getBodyPermitEditHandler_f5fd4c() { if (context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPermitEditHandler#f5fd4c") != null)
/* 4189 */       return (nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPermitEditHandler)context.get("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPermitEditHandler#f5fd4c");
/* 4190 */     nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPermitEditHandler bean = new nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPermitEditHandler();
/* 4191 */     context.put("nc.ui.ewm.workorder.event.bodyitem.simpleevent.BodyPermitEditHandler#f5fd4c", bean);
/* 4192 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4193 */     invokeInitializingBean(bean);
/* 4194 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.init.EMInitRef getWorkOrderInitRef() {
/* 4198 */     if (context.get("WorkOrderInitRef") != null)
/* 4199 */       return (nc.ui.am.ref.init.EMInitRef)context.get("WorkOrderInitRef");
/* 4200 */     nc.ui.am.ref.init.EMInitRef bean = new nc.ui.am.ref.init.EMInitRef();
/* 4201 */     context.put("WorkOrderInitRef", bean);
/* 4202 */     bean.setRefInfos(getManagedList80());
/* 4203 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4204 */     invokeInitializingBean(bean);
/* 4205 */     return bean;
/*      */   }
/*      */   
/* 4208 */   private List getManagedList80() { List list = new ArrayList();list.add(getH001Equip());list.add(getH002Location());list.add(getH003StdJob());list.add(getH004WorkType());list.add(getH005WorkStatus());list.add(getH006Priority());list.add(getH007Executor());list.add(getH008Director());list.add(getH009PsnGroup());list.add(getH010Specialty());list.add(getH011WODept());list.add(getH011WODept_v());list.add(getH012Project());list.add(getH013WorkCenter());list.add(getH014Reportedby());list.add(getH015Parentwo());list.add(getH016Provider());list.add(getH017RepairPlan());list.add(getH018MTCon());list.add(getH019SafetyJob());list.add(getH020InspectionRoad());list.add(getH021typeRef());list.add(getH022symptomRef());list.add(getH023reasonRef());list.add(getH024serviceRef());list.add(getBPLPsn001JobType());list.add(getBPLPsn002Psn());list.add(getBPLInV001Mtrid());list.add(getBACPsn001JobType());list.add(getBACPsn002Psn());list.add(getBPLTool001Tool());list.add(getBACTool001Tool());list.add(getBTskObj001Equip());list.add(getBTskObj002Location());list.add(getBStruc001PkWO());list.add(getBACInV001Mtrid());list.add(getBPrecaution001PkPrecation());list.add(getBPrecaution002PkIsolateTemp());list.add(getBPermit003PkPermit());return list;
/*      */   }
/*      */   
/* 4211 */   public nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo getH001Equip() { if (context.get("h001Equip") != null)
/* 4212 */       return (nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo)context.get("h001Equip");
/* 4213 */     nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo bean = new nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo();
/* 4214 */     context.put("h001Equip", bean);
/* 4215 */     bean.setFiledKey("pk_equip");
/* 4216 */     bean.setPosition(0);
/* 4217 */     bean.setAssetOrg(false);
/* 4218 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4219 */     invokeInitializingBean(bean);
/* 4220 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.LocationInitRefInfo getH002Location() {
/* 4224 */     if (context.get("h002Location") != null)
/* 4225 */       return (nc.ui.am.ref.refinfos.LocationInitRefInfo)context.get("h002Location");
/* 4226 */     nc.ui.am.ref.refinfos.LocationInitRefInfo bean = new nc.ui.am.ref.refinfos.LocationInitRefInfo();
/* 4227 */     context.put("h002Location", bean);
/* 4228 */     bean.setFiledKey("pk_location");
/* 4229 */     bean.setPosition(0);
/* 4230 */     bean.setAssetOrg(false);
/* 4231 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4232 */     invokeInitializingBean(bean);
/* 4233 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH003StdJob() {
/* 4237 */     if (context.get("h003StdJob") != null)
/* 4238 */       return (InitRefInfo)context.get("h003StdJob");
/* 4239 */     InitRefInfo bean = new InitRefInfo();
/* 4240 */     context.put("h003StdJob", bean);
/* 4241 */     bean.setFiledKey("pk_std_job");
/* 4242 */     bean.setPosition(0);
/* 4243 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4244 */     invokeInitializingBean(bean);
/* 4245 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH004WorkType() {
/* 4249 */     if (context.get("h004WorkType") != null)
/* 4250 */       return (InitRefInfo)context.get("h004WorkType");
/* 4251 */     InitRefInfo bean = new InitRefInfo();
/* 4252 */     context.put("h004WorkType", bean);
/* 4253 */     bean.setFiledKey("pk_worktype");
/* 4254 */     bean.setPosition(0);
/* 4255 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4256 */     invokeInitializingBean(bean);
/* 4257 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH005WorkStatus() {
/* 4261 */     if (context.get("h005WorkStatus") != null)
/* 4262 */       return (InitRefInfo)context.get("h005WorkStatus");
/* 4263 */     InitRefInfo bean = new InitRefInfo();
/* 4264 */     context.put("h005WorkStatus", bean);
/* 4265 */     bean.setFiledKey("pk_wo_status");
/* 4266 */     bean.setPosition(0);
/* 4267 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4268 */     invokeInitializingBean(bean);
/* 4269 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH006Priority() {
/* 4273 */     if (context.get("h006Priority") != null)
/* 4274 */       return (InitRefInfo)context.get("h006Priority");
/* 4275 */     InitRefInfo bean = new InitRefInfo();
/* 4276 */     context.put("h006Priority", bean);
/* 4277 */     bean.setFiledKey("pk_priority");
/* 4278 */     bean.setPosition(0);
/* 4279 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4280 */     invokeInitializingBean(bean);
/* 4281 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.PsnInitRefInfo getH007Executor() {
/* 4285 */     if (context.get("h007Executor") != null)
/* 4286 */       return (nc.ui.am.ref.refinfos.PsnInitRefInfo)context.get("h007Executor");
/* 4287 */     nc.ui.am.ref.refinfos.PsnInitRefInfo bean = new nc.ui.am.ref.refinfos.PsnInitRefInfo();
/* 4288 */     context.put("h007Executor", bean);
/* 4289 */     bean.setFiledKey("pk_executor");
/* 4290 */     bean.setPosition(0);
/* 4291 */     bean.setBusifuncode("as");
/* 4292 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4293 */     invokeInitializingBean(bean);
/* 4294 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.PsnInitRefInfo getH008Director() {
/* 4298 */     if (context.get("h008Director") != null)
/* 4299 */       return (nc.ui.am.ref.refinfos.PsnInitRefInfo)context.get("h008Director");
/* 4300 */     nc.ui.am.ref.refinfos.PsnInitRefInfo bean = new nc.ui.am.ref.refinfos.PsnInitRefInfo();
/* 4301 */     context.put("h008Director", bean);
/* 4302 */     bean.setFiledKey("pk_director");
/* 4303 */     bean.setPosition(0);
/* 4304 */     bean.setBusifuncode("as");
/* 4305 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4306 */     invokeInitializingBean(bean);
/* 4307 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH009PsnGroup() {
/* 4311 */     if (context.get("h009PsnGroup") != null)
/* 4312 */       return (InitRefInfo)context.get("h009PsnGroup");
/* 4313 */     InitRefInfo bean = new InitRefInfo();
/* 4314 */     context.put("h009PsnGroup", bean);
/* 4315 */     bean.setFiledKey("pk_psn_group");
/* 4316 */     bean.setPosition(0);
/* 4317 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4318 */     invokeInitializingBean(bean);
/* 4319 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH010Specialty() {
/* 4323 */     if (context.get("h010Specialty") != null)
/* 4324 */       return (InitRefInfo)context.get("h010Specialty");
/* 4325 */     InitRefInfo bean = new InitRefInfo();
/* 4326 */     context.put("h010Specialty", bean);
/* 4327 */     bean.setFiledKey("pk_specialty");
/* 4328 */     bean.setPosition(0);
/* 4329 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4330 */     invokeInitializingBean(bean);
/* 4331 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.DeptInitRefInfo getH011WODept() {
/* 4335 */     if (context.get("h011WODept") != null)
/* 4336 */       return (nc.ui.am.ref.refinfos.DeptInitRefInfo)context.get("h011WODept");
/* 4337 */     nc.ui.am.ref.refinfos.DeptInitRefInfo bean = new nc.ui.am.ref.refinfos.DeptInitRefInfo();
/* 4338 */     context.put("h011WODept", bean);
/* 4339 */     bean.setFiledKey("pk_wo_dept");
/* 4340 */     bean.setBusifuncode("as");
/* 4341 */     bean.setPosition(0);
/* 4342 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4343 */     invokeInitializingBean(bean);
/* 4344 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.DeptVersionInitRefInfo getH011WODept_v() {
/* 4348 */     if (context.get("h011WODept_v") != null)
/* 4349 */       return (nc.ui.am.ref.refinfos.DeptVersionInitRefInfo)context.get("h011WODept_v");
/* 4350 */     nc.ui.am.ref.refinfos.DeptVersionInitRefInfo bean = new nc.ui.am.ref.refinfos.DeptVersionInitRefInfo();
/* 4351 */     context.put("h011WODept_v", bean);
/* 4352 */     bean.setBusifuncode("as");
/* 4353 */     bean.setFiledKey("pk_wo_dept_v");
/* 4354 */     bean.setPosition(0);
/* 4355 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4356 */     invokeInitializingBean(bean);
/* 4357 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH012Project() {
/* 4361 */     if (context.get("h012Project") != null)
/* 4362 */       return (InitRefInfo)context.get("h012Project");
/* 4363 */     InitRefInfo bean = new InitRefInfo();
/* 4364 */     context.put("h012Project", bean);
/* 4365 */     bean.setFiledKey("pk_project");
/* 4366 */     bean.setPosition(0);
/* 4367 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4368 */     invokeInitializingBean(bean);
/* 4369 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH013WorkCenter() {
/* 4373 */     if (context.get("h013WorkCenter") != null)
/* 4374 */       return (InitRefInfo)context.get("h013WorkCenter");
/* 4375 */     InitRefInfo bean = new InitRefInfo();
/* 4376 */     context.put("h013WorkCenter", bean);
/* 4377 */     bean.setFiledKey("pk_workcenter");
/* 4378 */     bean.setPosition(0);
/* 4379 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4380 */     invokeInitializingBean(bean);
/* 4381 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.PsnInitRefInfo getH014Reportedby() {
/* 4385 */     if (context.get("h014Reportedby") != null)
/* 4386 */       return (nc.ui.am.ref.refinfos.PsnInitRefInfo)context.get("h014Reportedby");
/* 4387 */     nc.ui.am.ref.refinfos.PsnInitRefInfo bean = new nc.ui.am.ref.refinfos.PsnInitRefInfo();
/* 4388 */     context.put("h014Reportedby", bean);
/* 4389 */     bean.setFiledKey("pk_reportedby");
/* 4390 */     bean.setPosition(0);
/* 4391 */     bean.setBusifuncode("as");
/* 4392 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4393 */     invokeInitializingBean(bean);
/* 4394 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.ref.refinfos.ParentWORefInfo getH015Parentwo() {
/* 4398 */     if (context.get("h015Parentwo") != null)
/* 4399 */       return (nc.ui.ewm.workorder.ref.refinfos.ParentWORefInfo)context.get("h015Parentwo");
/* 4400 */     nc.ui.ewm.workorder.ref.refinfos.ParentWORefInfo bean = new nc.ui.ewm.workorder.ref.refinfos.ParentWORefInfo();
/* 4401 */     context.put("h015Parentwo", bean);
/* 4402 */     bean.setFiledKey("pk_parent_wo");
/* 4403 */     bean.setPosition(0);
/* 4404 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4405 */     invokeInitializingBean(bean);
/* 4406 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH016Provider() {
/* 4410 */     if (context.get("h016Provider") != null)
/* 4411 */       return (InitRefInfo)context.get("h016Provider");
/* 4412 */     InitRefInfo bean = new InitRefInfo();
/* 4413 */     context.put("h016Provider", bean);
/* 4414 */     bean.setFiledKey("pk_provider");
/* 4415 */     bean.setPosition(0);
/* 4416 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4417 */     invokeInitializingBean(bean);
/* 4418 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.ref.refinfos.RepairPlanRefInfo getH017RepairPlan() {
/* 4422 */     if (context.get("h017RepairPlan") != null)
/* 4423 */       return (nc.ui.ewm.workorder.ref.refinfos.RepairPlanRefInfo)context.get("h017RepairPlan");
/* 4424 */     nc.ui.ewm.workorder.ref.refinfos.RepairPlanRefInfo bean = new nc.ui.ewm.workorder.ref.refinfos.RepairPlanRefInfo();
/* 4425 */     context.put("h017RepairPlan", bean);
/* 4426 */     bean.setFiledKey("pk_repair_plan_b");
/* 4427 */     bean.setPosition(0);
/* 4428 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4429 */     invokeInitializingBean(bean);
/* 4430 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH018MTCon() {
/* 4434 */     if (context.get("h018MTCon") != null)
/* 4435 */       return (InitRefInfo)context.get("h018MTCon");
/* 4436 */     InitRefInfo bean = new InitRefInfo();
/* 4437 */     context.put("h018MTCon", bean);
/* 4438 */     bean.setFiledKey("pk_mtcon");
/* 4439 */     bean.setPosition(0);
/* 4440 */     bean.setDefaultWhereSql("bill_status = 9 ");
/* 4441 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4442 */     invokeInitializingBean(bean);
/* 4443 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH019SafetyJob() {
/* 4447 */     if (context.get("h019SafetyJob") != null)
/* 4448 */       return (InitRefInfo)context.get("h019SafetyJob");
/* 4449 */     InitRefInfo bean = new InitRefInfo();
/* 4450 */     context.put("h019SafetyJob", bean);
/* 4451 */     bean.setFiledKey("pk_safety_job");
/* 4452 */     bean.setPosition(0);
/* 4453 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4454 */     invokeInitializingBean(bean);
/* 4455 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getH020InspectionRoad() {
/* 4459 */     if (context.get("h020InspectionRoad") != null)
/* 4460 */       return (InitRefInfo)context.get("h020InspectionRoad");
/* 4461 */     InitRefInfo bean = new InitRefInfo();
/* 4462 */     context.put("h020InspectionRoad", bean);
/* 4463 */     bean.setFiledKey("pk_inspection_road");
/* 4464 */     bean.setPosition(0);
/* 4465 */     bean.setDefaultWhereSql("");
/* 4466 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4467 */     invokeInitializingBean(bean);
/* 4468 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.FailureDocInitRefInfo getH021typeRef() {
/* 4472 */     if (context.get("h021typeRef") != null)
/* 4473 */       return (nc.ui.am.ref.refinfos.FailureDocInitRefInfo)context.get("h021typeRef");
/* 4474 */     nc.ui.am.ref.refinfos.FailureDocInitRefInfo bean = new nc.ui.am.ref.refinfos.FailureDocInitRefInfo();
/* 4475 */     context.put("h021typeRef", bean);
/* 4476 */     bean.setPosition(0);
/* 4477 */     bean.setFiledKey("pk_failure_type");
/* 4478 */     bean.setAssetOrg(false);
/* 4479 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4480 */     invokeInitializingBean(bean);
/* 4481 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.FailureDocInitRefInfo getH022symptomRef() {
/* 4485 */     if (context.get("h022symptomRef") != null)
/* 4486 */       return (nc.ui.am.ref.refinfos.FailureDocInitRefInfo)context.get("h022symptomRef");
/* 4487 */     nc.ui.am.ref.refinfos.FailureDocInitRefInfo bean = new nc.ui.am.ref.refinfos.FailureDocInitRefInfo();
/* 4488 */     context.put("h022symptomRef", bean);
/* 4489 */     bean.setPosition(0);
/* 4490 */     bean.setFiledKey("pk_failure_symptom");
/* 4491 */     bean.setAssetOrg(false);
/* 4492 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4493 */     invokeInitializingBean(bean);
/* 4494 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.FailureDocInitRefInfo getH023reasonRef() {
/* 4498 */     if (context.get("h023reasonRef") != null)
/* 4499 */       return (nc.ui.am.ref.refinfos.FailureDocInitRefInfo)context.get("h023reasonRef");
/* 4500 */     nc.ui.am.ref.refinfos.FailureDocInitRefInfo bean = new nc.ui.am.ref.refinfos.FailureDocInitRefInfo();
/* 4501 */     context.put("h023reasonRef", bean);
/* 4502 */     bean.setPosition(0);
/* 4503 */     bean.setFiledKey("failure_reason_name");
/* 4504 */     bean.setMultiSelected(true);
/* 4505 */     bean.setAssetOrg(false);
/* 4506 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4507 */     invokeInitializingBean(bean);
/* 4508 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.FailureDocInitRefInfo getH024serviceRef() {
/* 4512 */     if (context.get("h024serviceRef") != null)
/* 4513 */       return (nc.ui.am.ref.refinfos.FailureDocInitRefInfo)context.get("h024serviceRef");
/* 4514 */     nc.ui.am.ref.refinfos.FailureDocInitRefInfo bean = new nc.ui.am.ref.refinfos.FailureDocInitRefInfo();
/* 4515 */     context.put("h024serviceRef", bean);
/* 4516 */     bean.setPosition(0);
/* 4517 */     bean.setFiledKey("pk_service_step");
/* 4518 */     bean.setAssetOrg(false);
/* 4519 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4520 */     invokeInitializingBean(bean);
/* 4521 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBPLPsn001JobType() {
/* 4525 */     if (context.get("bPLPsn001JobType") != null)
/* 4526 */       return (InitRefInfo)context.get("bPLPsn001JobType");
/* 4527 */     InitRefInfo bean = new InitRefInfo();
/* 4528 */     context.put("bPLPsn001JobType", bean);
/* 4529 */     bean.setFiledKey("pk_job_type");
/* 4530 */     bean.setTableCode(getBodytabPlanPsn());
/* 4531 */     bean.setMultiSelected(false);
/* 4532 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4533 */     invokeInitializingBean(bean);
/* 4534 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.PsnInitRefInfo getBPLPsn002Psn() {
/* 4538 */     if (context.get("bPLPsn002Psn") != null)
/* 4539 */       return (nc.ui.am.ref.refinfos.PsnInitRefInfo)context.get("bPLPsn002Psn");
/* 4540 */     nc.ui.am.ref.refinfos.PsnInitRefInfo bean = new nc.ui.am.ref.refinfos.PsnInitRefInfo();
/* 4541 */     context.put("bPLPsn002Psn", bean);
/* 4542 */     bean.setFiledKey("pk_psndoc");
/* 4543 */     bean.setTableCode(getBodytabPlanPsn());
/* 4544 */     bean.setMultiSelected(true);
/* 4545 */     bean.setBusifuncode("as");
/* 4546 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4547 */     invokeInitializingBean(bean);
/* 4548 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBPLTool001Tool() {
/* 4552 */     if (context.get("bPLTool001Tool") != null)
/* 4553 */       return (InitRefInfo)context.get("bPLTool001Tool");
/* 4554 */     InitRefInfo bean = new InitRefInfo();
/* 4555 */     context.put("bPLTool001Tool", bean);
/* 4556 */     bean.setFiledKey("pk_tool");
/* 4557 */     bean.setTableCode(getBodytabPlanTool());
/* 4558 */     bean.setMultiSelected(true);
/* 4559 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4560 */     invokeInitializingBean(bean);
/* 4561 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBACPsn001JobType() {
/* 4565 */     if (context.get("bACPsn001JobType") != null)
/* 4566 */       return (InitRefInfo)context.get("bACPsn001JobType");
/* 4567 */     InitRefInfo bean = new InitRefInfo();
/* 4568 */     context.put("bACPsn001JobType", bean);
/* 4569 */     bean.setFiledKey("pk_job_type");
/* 4570 */     bean.setTableCode(getBodytabActualPsn());
/* 4571 */     bean.setMultiSelected(false);
/* 4572 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4573 */     invokeInitializingBean(bean);
/* 4574 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.PsnInitRefInfo getBACPsn002Psn() {
/* 4578 */     if (context.get("bACPsn002Psn") != null)
/* 4579 */       return (nc.ui.am.ref.refinfos.PsnInitRefInfo)context.get("bACPsn002Psn");
/* 4580 */     nc.ui.am.ref.refinfos.PsnInitRefInfo bean = new nc.ui.am.ref.refinfos.PsnInitRefInfo();
/* 4581 */     context.put("bACPsn002Psn", bean);
/* 4582 */     bean.setFiledKey("pk_psndoc");
/* 4583 */     bean.setTableCode(getBodytabActualPsn());
/* 4584 */     bean.setMultiSelected(true);
/* 4585 */     bean.setBusifuncode("as");
/* 4586 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4587 */     invokeInitializingBean(bean);
/* 4588 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBACTool001Tool() {
/* 4592 */     if (context.get("bACTool001Tool") != null)
/* 4593 */       return (InitRefInfo)context.get("bACTool001Tool");
/* 4594 */     InitRefInfo bean = new InitRefInfo();
/* 4595 */     context.put("bACTool001Tool", bean);
/* 4596 */     bean.setFiledKey("pk_tool");
/* 4597 */     bean.setTableCode(getBodytabActualTool());
/* 4598 */     bean.setMultiSelected(true);
/* 4599 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4600 */     invokeInitializingBean(bean);
/* 4601 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo getBTskObj001Equip() {
/* 4605 */     if (context.get("bTskObj001Equip") != null)
/* 4606 */       return (nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo)context.get("bTskObj001Equip");
/* 4607 */     nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo bean = new nc.ui.am.ref.refinfos.EquipInOwnOrgByRuleRefInfo();
/* 4608 */     context.put("bTskObj001Equip", bean);
/* 4609 */     bean.setFiledKey("pk_equip");
/* 4610 */     bean.setTableCode(getBodytabTaskObj());
/* 4611 */     bean.setMultiSelected(true);
/* 4612 */     bean.setAssetOrg(false);
/* 4613 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4614 */     invokeInitializingBean(bean);
/* 4615 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.am.ref.refinfos.LocationInitRefInfo getBTskObj002Location() {
/* 4619 */     if (context.get("bTskObj002Location") != null)
/* 4620 */       return (nc.ui.am.ref.refinfos.LocationInitRefInfo)context.get("bTskObj002Location");
/* 4621 */     nc.ui.am.ref.refinfos.LocationInitRefInfo bean = new nc.ui.am.ref.refinfos.LocationInitRefInfo();
/* 4622 */     context.put("bTskObj002Location", bean);
/* 4623 */     bean.setFiledKey("pk_location");
/* 4624 */     bean.setTableCode(getBodytabTaskObj());
/* 4625 */     bean.setMultiSelected(true);
/* 4626 */     bean.setAssetOrg(false);
/* 4627 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4628 */     invokeInitializingBean(bean);
/* 4629 */     return bean;
/*      */   }
/*      */   
/*      */   public nc.ui.ewm.workorder.ref.refinfos.ChildWORefInfo getBStruc001PkWO() {
/* 4633 */     if (context.get("bStruc001PkWO") != null)
/* 4634 */       return (nc.ui.ewm.workorder.ref.refinfos.ChildWORefInfo)context.get("bStruc001PkWO");
/* 4635 */     nc.ui.ewm.workorder.ref.refinfos.ChildWORefInfo bean = new nc.ui.ewm.workorder.ref.refinfos.ChildWORefInfo();
/* 4636 */     context.put("bStruc001PkWO", bean);
/* 4637 */     bean.setFiledKey("pk_wo");
/* 4638 */     bean.setTableCode(getBodytabStructure());
/* 4639 */     bean.setMultiSelected(true);
/* 4640 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4641 */     invokeInitializingBean(bean);
/* 4642 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBPLInV001Mtrid() {
/* 4646 */     if (context.get("bPLInV001Mtrid") != null)
/* 4647 */       return (InitRefInfo)context.get("bPLInV001Mtrid");
/* 4648 */     InitRefInfo bean = new InitRefInfo();
/* 4649 */     context.put("bPLInV001Mtrid", bean);
/* 4650 */     bean.setFiledKey("pk_material_v");
/* 4651 */     bean.setTableCode(getBodytabPlanInv());
/* 4652 */     bean.setMultiSelected(true);
/* 4653 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4654 */     invokeInitializingBean(bean);
/* 4655 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBACInV001Mtrid() {
/* 4659 */     if (context.get("bACInV001Mtrid") != null)
/* 4660 */       return (InitRefInfo)context.get("bACInV001Mtrid");
/* 4661 */     InitRefInfo bean = new InitRefInfo();
/* 4662 */     context.put("bACInV001Mtrid", bean);
/* 4663 */     bean.setFiledKey("pk_material_v");
/* 4664 */     bean.setTableCode(getBodytabActualInv());
/* 4665 */     bean.setMultiSelected(true);
/* 4666 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4667 */     invokeInitializingBean(bean);
/* 4668 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBPrecaution001PkPrecation() {
/* 4672 */     if (context.get("bPrecaution001PkPrecation") != null)
/* 4673 */       return (InitRefInfo)context.get("bPrecaution001PkPrecation");
/* 4674 */     InitRefInfo bean = new InitRefInfo();
/* 4675 */     context.put("bPrecaution001PkPrecation", bean);
/* 4676 */     bean.setFiledKey("pk_precaution");
/* 4677 */     bean.setTableCode(getBodytabPrecaution());
/* 4678 */     bean.setMultiSelected(true);
/* 4679 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4680 */     invokeInitializingBean(bean);
/* 4681 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBPrecaution002PkIsolateTemp() {
/* 4685 */     if (context.get("bPrecaution002PkIsolateTemp") != null)
/* 4686 */       return (InitRefInfo)context.get("bPrecaution002PkIsolateTemp");
/* 4687 */     InitRefInfo bean = new InitRefInfo();
/* 4688 */     context.put("bPrecaution002PkIsolateTemp", bean);
/* 4689 */     bean.setFiledKey("pk_isolate_temp");
/* 4690 */     bean.setTableCode(getBodytabPrecaution());
/* 4691 */     bean.setMultiSelected(false);
/* 4692 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4693 */     invokeInitializingBean(bean);
/* 4694 */     return bean;
/*      */   }
/*      */   
/*      */   public InitRefInfo getBPermit003PkPermit() {
/* 4698 */     if (context.get("bPermit003PkPermit") != null)
/* 4699 */       return (InitRefInfo)context.get("bPermit003PkPermit");
/* 4700 */     InitRefInfo bean = new InitRefInfo();
/* 4701 */     context.put("bPermit003PkPermit", bean);
/* 4702 */     bean.setFiledKey("pk_permit");
/* 4703 */     bean.setTableCode(getBodytabPermit());
/* 4704 */     bean.setMultiSelected(true);
/* 4705 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4706 */     invokeInitializingBean(bean);
/* 4707 */     return bean;
/*      */   }
/*      */   
/*      */   public WorkOrderAppModelService getModelService() {
/* 4711 */     if (context.get("modelService") != null)
/* 4712 */       return (WorkOrderAppModelService)context.get("modelService");
/* 4713 */     WorkOrderAppModelService bean = new WorkOrderAppModelService();
/* 4714 */     context.put("modelService", bean);
/* 4715 */     bean.setContext(getContext());
/* 4716 */     setBeanFacotryIfBeanFacatoryAware(bean);
/* 4717 */     invokeInitializingBean(bean);
/* 4718 */     return bean;
/*      */   }
/*      */ }