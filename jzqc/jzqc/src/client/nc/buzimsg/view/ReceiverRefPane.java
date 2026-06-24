/*     */ package nc.buzimsg.view;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/*     */ import nc.buzimsg.action.BuzimsgSelfDefRefpaneEvent;
/*     */ import nc.buzimsg.util.BuzimsgUtil;
/*     */ import nc.buzimsg.vo.MsgresRcvVO;
import nc.vo.jcom.lang.StringUtil;

import org.apache.commons.lang.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReceiverRefPane
/*     */   extends AbstractBuzimsgSelfDefRefpane
/*     */ {
/*     */   private static final long serialVersionUID = 5798066423025196214L;
/*  18 */   private int parentRowIndex = -1;
/*     */   
/*     */   private String msgres_code;
/*     */   
/*     */   private MsgresRcvVO[] initdata;
/*     */   private MsgresRcvVO[] currSelected;
/*     */   private MsgresRcvVO[] oldSelected;
/*     */   
/*     */   public ReceiverRefPane(MsgresRcvVO[] initdata, String msgres_code)
/*     */   {
/*  28 */     this(-1, initdata, msgres_code);
/*     */   }
/*     */   
/*     */   public ReceiverRefPane(int parentRowIndex, MsgresRcvVO[] initdata, String msgres_code)
/*     */   {
/*  33 */     this.initdata = initdata;
/*  34 */     this.parentRowIndex = parentRowIndex;
/*  35 */     this.msgres_code = msgres_code;
/*  36 */     currSelected = initdata;
/*  37 */     oldSelected = initdata;
/*  38 */     init();
/*     */   }
/*     */   
/*     */   private void init()
/*     */   {
/*  43 */     resettingButtonListener();
/*  44 */     disableTextEditting();
/*     */   }
/*     */   
/*     */   private void resettingButtonListener()
/*     */   {
/*  49 */     ActionListener[] ls = getUIButton().getActionListeners();
/*     */     
/*  51 */     if (ls != null)
/*     */     {
/*  53 */       for (ActionListener listener : ls) {
/*  54 */         getUIButton().removeActionListener(listener);
/*     */       }
/*     */     }
/*  57 */     getUIButton().addActionListener(new RefPaneActionListener());
/*     */   }
/*     */   
/*     */   private void disableTextEditting()
/*     */   {
/*  62 */     getUITextField().setEditable(false);
/*     */   }
/*     */   
/*     */ 
/*     */   private void setValues(MsgresRcvVO[] vos)
/*     */   {
/*  68 */     resetValue(vos);
/*     */   }
/*     */   
/*     */   private void resetValue(MsgresRcvVO[] vos)
/*     */   {
/*  73 */     setText(BuzimsgUtil.getReceiverColumnDispText(vos));
/*  74 */     currSelected = vos;
/*  75 */     fireEvent(new BuzimsgSelfDefRefpaneEvent(this, parentRowIndex, oldSelected, currSelected, 1));
/*  76 */     oldSelected = vos;
/*     */   }
/*     */   
/*     */   private class RefPaneActionListener implements ActionListener
/*     */   {
/*     */     private RefPaneActionListener() {}
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/*  84 */       ReceiverConfigurationDlg dlg = new ReceiverConfigurationDlg(ReceiverRefPane.this, initdata, msgres_code);
/*  85 */       dlg.setSize(new Dimension(600, 450));
/*  86 */       dlg.setResizable(true);
/*     */       
/*  88 */       int result = dlg.showModal();
/*  89 */       if (result == 1)
/*     */       {
/*  91 */         MsgresRcvVO[] selected = dlg.getReceivers4Return();
/*  92 */         ReceiverRefPane.this.setValues( msgresRcvVOConvertor(selected));
/*     */       }
/*     */     }
/*     */   }

private MsgresRcvVO[] msgresRcvVOConvertor(MsgresRcvVO[] vos)
/*     */   {
/* 570 */     if (ArrayUtils.isEmpty(vos)) {
/* 571 */       return null;
/*     */     }
/* 573 */     List<MsgresRcvVO> list = new ArrayList();
/* 574 */     for (MsgresRcvVO receiverVO : vos)
/*     */     {
/* 576 */       if(!StringUtil.isEmpty(receiverVO.getReceiverpk()))
/* 595 */             list.add(receiverVO);
/*     */           }
/* 600 */     return (MsgresRcvVO[])list.toArray(new MsgresRcvVO[0]);
/*     */   }
/*     */   
/*     */   public MsgresRcvVO[] getCurrSelected()
/*     */   {
/*  99 */     return currSelected;
/*     */   }
/*     */   
/*     */   public int getParentRowIndex()
/*     */   {
/* 104 */     return parentRowIndex;
/*     */   }
/*     */ }
