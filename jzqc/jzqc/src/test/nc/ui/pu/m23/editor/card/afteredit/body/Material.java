/*     */ package nc.ui.pu.m23.editor.card.afteredit.body;
/*     */ 
/*     */ import java.util.Map;
/*     */ import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
/*     */ import nc.ui.pu.m23.editor.card.utils.ChangeRateUtil;
/*     */ import nc.ui.pu.m23.utils.ArriveClientUtil;
/*     */ import nc.ui.pu.pub.editor.card.listener.ICardBodyAfterEditEventListener;
/*     */ import nc.ui.pub.bill.BillCardPanel;
/*     */ import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
/*     */ import nc.vo.bd.material.stock.MaterialStockVO;
/*     */ import nc.vo.pu.m23.rule.transfer.CalcValidDay;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ public class Material
/*     */   implements ICardBodyAfterEditEventListener
/*     */ {
/*     */   public void afterEdit(CardBodyAfterEditEvent e)
/*     */   {
/*  35 */     BillCardPanel card = e.getBillCardPanel();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  40 */     String mrl = ArriveClientUtil.getStringCellValue(card, e.getRow(), "pk_material");
/*     */     
/*     */ String cunitid = ArriveClientUtil.getStringCellValue(card, e.getRow(), "cunitid");
/*  43 */     String org = ArriveClientUtil.getStringHeaderValue(card, "pk_org");
/*     */     
/*  45 */     if (StringUtils.isNotEmpty(mrl))
/*     */     {
/*  47 */       ChangeRateUtil.setChgRateAndFixedFlag(card, e.getRow());
/*     */       
/*     */ 
/*  50 */       setDefaultValidDay(e, card, mrl, org);
/*     */     }
/*     */   }
/*     */   
/*     */   private void clearRelateItemValues(BillCardPanel card, int rowNo)
/*     */   {
/*  56 */     String[] itemKeyArray = new String[16];
/*     */     
/*  58 */     itemKeyArray[0] = "nnum";
/*  59 */     itemKeyArray[1] = "nastnum";
/*     */     
/*  61 */     itemKeyArray[2] = "nmny";
/*  62 */     itemKeyArray[3] = "ntaxmny";
/*     */     
/*  64 */     itemKeyArray[4] = "nprice";
/*  65 */     itemKeyArray[5] = "ntaxprice";
/*     */     
/*  67 */     itemKeyArray[6] = "nelignum";
/*  68 */     itemKeyArray[7] = "nnotelignum";
/*     */     
/*  70 */     itemKeyArray[8] = "npresentastnum";
/*  71 */     itemKeyArray[9] = "npresentnum";
/*     */     
/*  73 */     itemKeyArray[10] = "pk_batchcode";
/*     */     
/*  75 */     itemKeyArray[11] = "dproducedate";
/*  76 */     itemKeyArray[12] = "ivalidday";
/*  77 */     itemKeyArray[13] = "dinvaliddate";
/*     */     
/*  79 */     itemKeyArray[14] = "bfixedrate";
/*  80 */     itemKeyArray[15] = "vchangerate";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  85 */     ArriveClientUtil.clearBodyCellValues(card, rowNo, itemKeyArray);
/*     */     
/*  87 */     ArriveClientUtil.clearFreeCellValues(card, rowNo);
/*     */   }
/*     */   
/*     */   private void setDefaultValidDay(CardBodyAfterEditEvent e, BillCardPanel card, String mrl, String org)
/*     */   {
/*  92 */     CalcValidDay util = new CalcValidDay();
/*  93 */     String[] fields = new String[3];
/*  94 */     fields[0] = "qualitymanflag";
/*  95 */     fields[1] = "qualityunit";
/*  96 */     fields[2] = "qualitynum";
/*  97 */     Map<String, MaterialStockVO> map = null;
/*  98 */     String[] mrls = new String[1];
/*  99 */     mrls[0] = mrl;
/* 100 */     map = MaterialPubService.queryMaterialStockInfo(mrls, org, fields);
/*     */     
/* 102 */     if ((map != null) && (map.get(mrl) != null)) {
/* 103 */       Integer validDay = util.calcValidDay((MaterialStockVO)map.get(mrl));
/* 104 */       card.setBodyValueAt(validDay, e.getRow(), "ivalidday");
/*     */     }
/*     */   }
/*     */ }
