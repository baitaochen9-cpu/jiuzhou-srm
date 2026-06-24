/*     */ package nc.ui.ewm.workorder.action;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
import java.util.ArrayList;
/*     */ import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/*     */ import java.util.Map;

import nc.bs.trade.business.HYPubBO;
/*     */ import nc.itf.uap.pf.busiflow.PfButtonClickContext;
/*     */ import nc.pubitf.org.IStockOrgPubService;
import nc.pubitf.uapbd.IMaterialPubService;
/*     */ import nc.ui.am.action.info.ActionInfoInitalizer;
/*     */ import nc.ui.am.action.support.AMNCAction;
/*     */ import nc.ui.am.editor.AMBillForm;
/*     */ import nc.ui.am.status.StatusUtils;
import nc.ui.am.util.BillCardPanelUtils;
/*     */ import nc.ui.am.util.RownoClientUtils;
/*     */ import nc.ui.pub.bill.BillCardPanel;
/*     */ import nc.ui.pub.bill.BillModel;
/*     */ import nc.ui.pub.pf.PfUtilClient;
/*     */ import nc.ui.uif2.UIState;
/*     */ import nc.vo.am.common.AbstractAggBill;
import nc.vo.am.common.BizContext;
/*     */ import nc.vo.am.common.util.ArrayConstructor;
import nc.vo.am.common.util.ArrayUtils;
/*     */ import nc.vo.am.common.util.BaseVOUtils;
import nc.vo.am.common.util.UFDoubleUtils;
/*     */ import nc.vo.am.manager.CurrencyManager;
/*     */ import nc.vo.am.proxy.AMProxy;
/*     */ import nc.vo.am.pub.uap.ModuleInfoQuery;
import nc.vo.bd.material.MaterialVO;
/*     */ import nc.vo.ewm.workorder.AggWorkOrderVO;
/*     */ import nc.vo.ewm.workorder.WOActualInvVO;
/*     */ import nc.vo.ewm.workorder.utils.ActualInvFrom4DProcessUtil;
/*     */ import nc.vo.ewm.workorder.utils.TransiTypeUtils;
/*     */ import nc.vo.ic.general.define.ICBillBodyVO;
/*     */ import nc.vo.ic.general.define.ICBillVO;
/*     */ import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
/*     */ import nc.vo.ic.m4d.entity.MaterialOutVO;
/*     */ import nc.vo.ic.pub.util.VOEntityUtil;
/*     */ import nc.vo.logging.Debug;
/*     */ import nc.vo.ml.NCLangRes4VoTransl;
/*     */ import nc.vo.pub.AggregatedValueObject;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import nc.vo.pub.bill.BillTabVO;
/*     */ import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
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
/*     */ public class LinkStockOutBillAction
/*     */   extends AMNCAction
/*     */ {
/*     */   private static final long serialVersionUID = -8110691657621378958L;
/*     */   private AMBillForm billForm;
/*     */   
/*     */   public LinkStockOutBillAction()
/*     */   {
/*  62 */     ActionInfoInitalizer.initializeAction(this, "LinkStockOut");
/*     */   }
/*     */   
/*     */   public void doAction(ActionEvent e) throws Exception
/*     */   {
/*  67 */     if (!ModuleInfoQuery.isICEnabled()) {
/*  68 */       throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0319"));
/*     */     }
/*     */     
/*  71 */     PfUtilClient.childButtonClickedNew(createPfButtonClickContext());
/*  72 */     if (PfUtilClient.isCloseOK()) {
/*  73 */       AggregatedValueObject[] stockBillVOs = PfUtilClient.getRetOldVos();
/*  74 */       if ((stockBillVOs != null) && (stockBillVOs.length > 0)) {
/*  75 */         setCurrencyToStockOutBill((MaterialOutVO[])ArrayConstructor.getArray(stockBillVOs));
/*     */         
/*  77 */         AggWorkOrderVO[] retBills = (AggWorkOrderVO[])ArrayConstructor.getArray(PfUtilClient.getRetVos());
/*  78 */         if (retBills == null)
/*  79 */           return;
/*  80 */         WOActualInvVO[] invVOs = (WOActualInvVO[])BaseVOUtils.getBodyVOs(retBills, WOActualInvVO.class);
/*  81 */         if ((invVOs == null) || (invVOs.length == 0)) {
/*  82 */           return;
/*     */         }
/*  84 */         setLinkFlag(invVOs);
/*     */         
/*  86 */         if (getModel().containOperateStatus(StatusUtils.view)) {
/*  87 */           getModel().toOperateStatus(StatusUtils.edit);
/*     */         }
/*     */         
/*  90 */         AggWorkOrderVO woBillVO = (AggWorkOrderVO)getValueFromEditor();
/*  91 */         woBillVO.setChildren(WOActualInvVO.class, invVOs);
/*  92 */         ActualInvFrom4DProcessUtil.processFullHeadWOBillAfterChgFrom4D(new AggWorkOrderVO[] { woBillVO });
/*     */         
/*  94 */         setInvToView(invVOs);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private MaterialOutVO[] setCurrencyToStockOutBill(MaterialOutVO[] billvos)
/*     */     throws BusinessException
/*     */   {
/* 103 */     String[] orgs = VOEntityUtil.getVOsNotRepeatValue(VOEntityUtil.getHeadVOs(billvos), "pk_org");
/* 104 */     IStockOrgPubService stockOrgService = (IStockOrgPubService)AMProxy.lookup(IStockOrgPubService.class);
/* 105 */     Map<String, String> finaorgMap = stockOrgService.queryFinanceOrgIDsByStockOrgIDs(orgs);
/* 106 */     for (ICBillVO vo : billvos) {
/* 107 */       String pk_org = vo.getHead().getPk_org();
/* 108 */       String currencyId = CurrencyManager.getLocalCurrencyPK((String)finaorgMap.get(pk_org));
/* 109 */       ICBillBodyVO[] bodys = vo.getBodys();
/* 110 */       for (ICBillBodyVO icBillBodyVO : bodys) {
/* 111 */         ((MaterialOutBodyVO)icBillBodyVO).setCcurrencyid(currencyId);
/*     */       }
/*     */     }
/* 114 */     return billvos;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object getValueFromEditor()
/*     */   {
/* 123 */     getBillForm().getBillCardPanel().stopEditing();
/*     */     
/* 125 */     Object obj = getBillForm().getBillCardPanel().getBillData().getBillObjectByMetaData();
/* 126 */     AbstractAggBill billVo = (AbstractAggBill)obj;
/* 127 */     if (UIState.ADD.equals(getBillForm().getModel().getUiState()))
/*     */     {
/* 129 */       billVo.getParent().setAttributeValue("pk_wo", "1");
/*     */       
/* 131 */       billVo.getParent().setAttributeValue("pk_group", getModel().getContext().getPk_group());
/* 132 */       billVo.getParent().setAttributeValue("bill_type", getModel().getBillType());
/*     */     }
/*     */     
/*     */ 
/* 136 */     return obj;
/*     */   }
/*     */   
/*     */   private void setLinkFlag(WOActualInvVO[] invVOs)
/*     */   {
/* 141 */     for (WOActualInvVO invVO : invVOs)
/*     */     {
/* 143 */       String vchangeRateFieldvalue = invVO.getVchangerate();
/*     */       
/* 145 */       invVO.setVchangerate(vchangeRateFieldvalue);
/*     */       
/* 147 */       invVO.setLink_flag(UFBoolean.TRUE);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setInvToView(WOActualInvVO[] invVOs)
/*     */   {
/* 158 */     if (getModel().containOperateStatus(StatusUtils.view)) {
/* 159 */       getModel().toOperateStatus(StatusUtils.edit);
/*     */     }
/*     */     
/* 162 */     BillCardPanel billCardPanel = getBillForm().getBillCardPanel();
/* 163 */     BillTabVO tabVO = billCardPanel.getBillData().getTabVO(1, "wo_actual_inv");
/* 164 */     int tabIndex = billCardPanel.getBodyTabbedPane().getIndexofTableCode(tabVO);
/* 165 */     billCardPanel.getBodyTabbedPane().setSelectedIndex(tabIndex);
/*     */     
/* 167 */     BillModel billModel = billCardPanel.getBillModel("wo_actual_inv");
/* 168 */     int originRowCount = billModel.getRowCount();
/* 169 */     billModel.addLine(invVOs.length);
/* 170 */     billModel.setBodyRowObjectByMetaData(invVOs, originRowCount);
/* 171 */     RownoClientUtils.whenAddLine(billCardPanel, "rowno", invVOs.length);
			  try {
				calMny(invVOs, billCardPanel);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
/*     */   }
           private void calMny(WOActualInvVO[] invVOs,BillCardPanel billCardPanel) throws BusinessException{
 			  //触发金额编辑后事件
        	   List<String> list = new ArrayList<>();
        	   for (WOActualInvVO vo : invVOs) {
        	   	list.add(vo.getPk_material());
        	   }
       		IMaterialPubService materialService = AMProxy
       				.lookup(IMaterialPubService.class);
       		String[] fields = new String[] { MaterialVO.PK_MATERIAL,MaterialVO.PK_MARBASCLASS,
       				MaterialVO.PK_SOURCE, MaterialVO.CODE };
       		Map<String, MaterialVO> voMap = materialService
       				.queryMaterialBaseInfoByPks(
       						list.toArray(new String[list.size()]), fields);
       		
       		HYPubBO bo = new HYPubBO();
        	   // 服务类存货取材料出库单的单价
        	   // 材料类型的存货取最新成本价
        	   List<UFDouble>  fuwu = new ArrayList<UFDouble>();
        	   List<UFDouble>  cailiao = new ArrayList<UFDouble>();
        	   for (WOActualInvVO vo : invVOs) {
        	   	MaterialVO mvo = voMap.get(vo.getPk_material());
        	   	if (mvo == null)
    				throw new BusinessException("物料信息出错，请检查！");
    			String str = (String) bo.findColValue(
    					"bd_marbasclass",
    					"code ",
    					" nvl(dr,0) = 0 and pk_marbasclass = '"
    							+ mvo.getPk_marbasclass() + "'");
        	   	if (str.startsWith("09")) {// 服務類
        	   		fuwu.add(vo.getMoney());
        	   	} else {
        	   		cailiao.add(vo.getMoney());
        	   	}
        	   }
        	   UFDouble totalMny = UFDoubleUtils.add(fuwu.toArray(new UFDouble[fuwu.size()]));
        	   BillCardPanelUtils.setHeadItemValue(billCardPanel, "def4", totalMny);
        	   UFDouble totalMny1 = UFDoubleUtils.add(cailiao.toArray(new UFDouble[cailiao.size()]));
        	   BillCardPanelUtils.setHeadItemValue(billCardPanel, "ac_mtr_mny_org", totalMny1);
        	   setHeadTotalMoney(billCardPanel);
        	   			  
           }
			public static final String[] AC_MNY_ORG = { "ac_mtr_mny_org", "ac_lbr_mny_org", "ac_oth_mny_org","def4", "ac_tol_mny_org" };
			public static final String[] AC_MNY_ORG1 = { "ac_mtr_mny_org", "ac_lbr_mny_org", "ac_oth_mny_org", "ac_tol_mny_org" };
			public static void setHeadTotalMoney(BillCardPanel card)
/*     */   {
/*     */     
/*  94 */     List<UFDouble> headActualMoneys = setHeadTotalMoneyByMnyConst(card, AC_MNY_ORG);
/*     */     
/*     */     
/*  98 */     BillCardPanelUtils.setHeadItemValue(card, "ac_ttl_mny_org", UFDoubleUtils.add((UFDouble[])headActualMoneys.toArray(new UFDouble[0])));
/*     */     
/*     */ 
/* 101 */     BillCardPanelUtils.setHeadLocalMoney(card, (String[])ArrayUtils.addElement(ArrayUtils.addElement( AC_MNY_ORG1), new String[] { "ac_ttl_mny_org" }), BizContext.getInstance().getBizDate());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static List<UFDouble> setHeadTotalMoneyByMnyConst(BillCardPanel card, String[] MNY_CONST)
/*     */   {
/* 109 */     List<UFDouble> headTotalMoneys = new ArrayList();
/*     */     
/* 111 */     for (String pl_mny_org : MNY_CONST) {
/* 112 */       headTotalMoneys.add((UFDouble)BillCardPanelUtils.getHeadItemValue(card, pl_mny_org));
/*     */     }
/*     */     
/* 115 */     return headTotalMoneys;
/*     */   }
/*     */   
/*     */   private PfButtonClickContext createPfButtonClickContext() {
/* 175 */     PfButtonClickContext context = new PfButtonClickContext();
/* 176 */     context.setSrcBillType("4D");
/* 177 */     context.setPk_group(getModel().getContext().getPk_group());
/* 178 */     context.setUserId(getModel().getContext().getPk_loginUser());
/* 179 */     String curBillOrTransitype = getModel().getTransType();
/* 180 */     if (curBillOrTransitype == null) {
/* 181 */       curBillOrTransitype = getModel().getBillType();
/*     */     }
/* 183 */     context.setCurrBilltype(curBillOrTransitype);
/* 184 */     context.setParent(getModel().getContext().getEntranceUI());
/* 185 */     context.setClassifyMode(2);
/* 186 */     context.setTransTypes(Arrays.asList(new String[] { getModel().getTransType() }));
/* 187 */     String[] stockOrgs = getStockOrgsByMaintainOrg(getModel().getContext().getPk_org());
/* 188 */     context.setUserObj(stockOrgs);
/* 189 */     return context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] getStockOrgsByMaintainOrg(String pk_org)
/*     */   {
/* 199 */     String[] stockOrgs = null;
/*     */     try {
/* 201 */       stockOrgs = ((IStockOrgPubService)AMProxy.lookup(IStockOrgPubService.class)).getStockOrgIDSByMaintainOrgID(pk_org);
/*     */     } catch (BusinessException e) {
/* 203 */       Debug.error(e);
/* 204 */       showErrorMessage(e.getMessage());
/*     */     }
/* 206 */     return stockOrgs;
/*     */   }
/*     */   
/*     */   protected boolean isActionEnable()
/*     */   {
/* 211 */     boolean enable = true;
/* 212 */     if (getModel().containOperateStatus(StatusUtils.add))
/*     */     {
/* 214 */       String transType = getModel().getTransType();
/* 215 */       if (TransiTypeUtils.isAfterward(transType)) {
/* 216 */         enable = true;
/*     */       } else {
/* 218 */         enable = false;
/*     */       }
/* 220 */     } else if ((getModel().containOperateStatus(StatusUtils.edit)) || (getModel().containOperateStatus(StatusUtils.view)))
/*     */     {
/*     */ 
/* 223 */       AggWorkOrderVO billVO = (AggWorkOrderVO)getModel().getSelectedData();
/* 224 */       if (billVO != null) {
/* 225 */         Integer woStatusType = billVO.getParentVO().getWo_statustype();
/* 226 */         if (woStatusType == null) {
/* 227 */           return false;
/*     */         }
/* 229 */         if ((1 == woStatusType.intValue()) || (2 == woStatusType.intValue()))
/*     */         {
/* 231 */           enable = true;
/*     */         } else {
/* 233 */           enable = false;
/*     */         }
/*     */       } else {
/* 236 */         enable = false;
/*     */       }
/*     */     }
/* 239 */     return enable;
/*     */   }
/*     */   
/*     */   public AMBillForm getBillForm() {
/* 243 */     return billForm;
/*     */   }
/*     */   
/*     */   public void setBillForm(AMBillForm billForm) {
/* 247 */     this.billForm = billForm;
/*     */   }
/*     */ }
