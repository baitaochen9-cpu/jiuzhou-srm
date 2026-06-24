/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.ui.pu.m23.editor.card.beforeedit.body;
/*    */
/*    */
import nc.ui.pu.m23.utils.ArriveClientUtil;
/*    */
import nc.ui.pu.pub.editor.card.listener.ICardBodyBeforeEditEventListener;
import nc.ui.pub.beans.UIRefPane;
/*    */
import nc.ui.pub.bill.BillCardPanel;
/*    */
import nc.ui.pub.bill.BillItem;
/*    */
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
/*    */
import nc.ui.scmpub.ref.FilterProjectRefUtils;
/*    */
/*    */public class Bc_cstateid
/*    */implements ICardBodyBeforeEditEventListener
/*    */{
	/*    */public void beforeEdit(CardBodyBeforeEditEvent e)
	/*    */{
		/* 23 */BillCardPanel card = e.getBillCardPanel();
		/*    */
		/* 25 */String stockOrg = ArriveClientUtil.getStringHeaderValue(card,
				"pk_org");
		
	
//   	 new FilterCstateidRefUtils( model.get getEditorModel().getCardPanelWrapper().getHeadRefPane(event.getKey()))
//				.filterItemRefByOrg(pk_org);
//   	UIPanel panel=new UIPanel("¿â´æ×´Ì¬"); 
//   	event.getBillListPanel().getParentListPanel().get
//   	getEditor().getBillListPanel().getBodyUIPanel().get
//   	ICBizEditorModel editorModel=getEditorModel();
   	/* 21 *///
//   	 this.model.gets
//	    event.setReturnValue(Boolean.TRUE);
//   	 BillListPanel pal= event.;
//		panel.setBackground(null);
//   	UIRefPane ref=new UIRefPane("¿â´æ×´Ì¬");
   	 new PuFilterCstateidRefUtils(((UIRefPane)card.getBodyItem("bc_cstateid").getComponent())).filterItemRefByOrg(stockOrg);
		
		
		
		/*    */
		/* 32 */e.setReturnValue(Boolean.TRUE);
		/*    */}
	/*    */
}