/*    */ package nc.ui.ewm.workorder.event;
/*    */ 
/*    */ import nc.ui.am.editor.AMBillForm;
/*    */ import nc.ui.am.scaleevent.DefaultKeyFields;
/*    */ import nc.ui.am.scaleevent.EditEventHandler;
/*    */ import nc.ui.pub.bill.BillCardPanel;
/*    */ import nc.ui.pub.bill.BillEditEvent;
/*    */ import nc.ui.pub.bill.BillModel;
/*    */ import nc.vo.am.manager.CurrencyManager;
/*    */ import nc.vo.am.scale.query.ScaleUtils;
/*    */ import nc.vo.pub.lang.UFDouble;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WoActualPsnEditHandler
/*    */   extends EditEventHandler
/*    */ {
/*    */   private static final boolean PRICE_SCALE = true;
/*    */   private static final boolean SUM_SCALE = false;
/* 24 */   private static final UFDouble NULL_VALUE = UFDouble.ZERO_DBL;
/*    */   
/*    */   public void handleBodyAfterEditEvent(AMBillForm billForm, BillEditEvent e)
/*    */     throws Exception
/*    */   {
/* 29 */     if ((getKeyFields().getTableCode() != null) && (e.getTableCode().equals(getKeyFields().getTableCode())))
/*    */     {
/* 31 */       BillCardPanel billCard = getEditCardPanel(billForm);
/* 32 */       String pkOrg = getPk_Org(billForm);
/* 33 */       String pkCurrencyType = CurrencyManager.getLocalCurrencyPK(pkOrg);
/* 34 */       BillModel billModel = billCard.getBillModel();
/* 35 */       String editKey = e.getKey();
/*    */       
/* 37 */       String hourKey = getKeyFields().getMan_hours();
/* 38 */       String rateKey = getKeyFields().getRate();
/* 39 */       String moneyKey = getKeyFields().getMoney();
/* 40 */       int row = e.getRow();
/* 41 */       if (editKey.equals(rateKey)) {
/* 42 */         UFDouble rate = (UFDouble)e.getValue();
/* 43 */         UFDouble hour = (UFDouble)billCard.getBodyValueAt(row, hourKey);
/*    */         
/* 45 */         if ((hour != null) && (rate != null)) {
/* 46 */           UFDouble tempMoney = hour.multiply(rate);
/* 47 */           UFDouble money = ScaleUtils.setScaleByCurrType(tempMoney, pkCurrencyType, false);
/*    */           
/* 49 */           setValue(billModel, money, row, moneyKey);
/*    */         } else {
/* 51 */           setValue(billModel, NULL_VALUE, row, moneyKey);
/*    */         }
					calPsnMny(billForm,moneyKey);
/* 53 */       } else if (editKey.equals(moneyKey)) {
/* 54 */         UFDouble money = (UFDouble)e.getValue();
/* 55 */         UFDouble hour = (UFDouble)billCard.getBodyValueAt(row, hourKey);
/*    */         
/* 57 */         boolean isNotEmptyMoney = (money != null) && (!UFDouble.ZERO_DBL.equals(money));
/*    */         
/* 59 */         boolean isNotEmptyHour = (hour != null) && (!UFDouble.ZERO_DBL.equals(hour));
/*    */         
/*    */ 
/* 62 */         UFDouble rate = null;
/* 63 */         if ((isNotEmptyMoney) && (isNotEmptyHour)) {
/* 64 */           UFDouble tempRate = money.div(hour);
/* 65 */           rate = ScaleUtils.setScaleByCurrType(tempRate, pkCurrencyType, true);
/*    */         }
/*    */         else {
/* 68 */           rate = ScaleUtils.setScaleByCurrType(NULL_VALUE, pkCurrencyType, true);
/*    */         }
/*    */         
/* 71 */         setValue(billModel, rate, row, rateKey);
					calPsnMny(billForm,moneyKey);
/* 72 */       } else if (editKey.equals(hourKey)) {
/* 73 */         UFDouble hour = (UFDouble)e.getValue();
/* 74 */         UFDouble rate = (UFDouble)billCard.getBodyValueAt(row, rateKey);
/*    */         
/* 76 */         boolean isNotEmptyHour = (hour != null) && (!UFDouble.ZERO_DBL.equals(hour));
/*    */         
/* 78 */         boolean isNotEmptyRate = (rate != null) && (!UFDouble.ZERO_DBL.equals(rate));
/*    */         
/* 80 */         if ((isNotEmptyHour) && (isNotEmptyRate)) {
/* 81 */           UFDouble tempMoney = hour.multiply(rate);
/* 82 */           UFDouble money = ScaleUtils.setScaleByCurrType(tempMoney, pkCurrencyType, false);
/*    */           
/* 84 */           setValue(billModel, money, row, moneyKey);
/*    */         } else {
/* 86 */           UFDouble money = ScaleUtils.setScaleByCurrType(NULL_VALUE, pkCurrencyType, false);
/*    */           
/* 88 */           setValue(billModel, money, row, moneyKey);
/*    */         }
					calPsnMny(billForm,moneyKey);
/*    */       }
/*    */     }
/*    */   }
			private void calPsnMny(AMBillForm billForm,String moneyKey){
				BillCardPanel billCard = getEditCardPanel(billForm);
				BillModel billModel = billCard.getBillModel();
				int rowcount = billModel.getRowCount();
				UFDouble money = UFDouble.ZERO_DBL;
				UFDouble sum = UFDouble.ZERO_DBL;
				for(int i = 0 ; i<rowcount ; i++){
					Object o =billModel.getValueAt(i, moneyKey);
					if(o == null){
						money = UFDouble.ZERO_DBL;
					}else{
						money = (UFDouble)o;
					}
					sum = sum.add(money);
				}
				//
				billCard.getHeadItem("ac_lbr_mny_org").setValue(sum);//实际人工成本
			}
/*    */ }
