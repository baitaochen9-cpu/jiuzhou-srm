/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.ui.ic.m4460.handler;
/*    */
/*    */import nc.ui.ic.pub.handler.list.ICListHeadTailEditEventHandler;
import nc.ui.ic.pub.model.ICBizEditorModel;
import nc.ui.ic.special.handler.FilterCstateidRefUtils;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.event.list.ListHeadBeforeEditEvent;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.ui.ic.m4460.view.StateAdjustBillListView;
/*    */
/*    */
/*    */
/*    */public class CadjuststateidHandler
		extends
			ICListHeadTailEditEventHandler
/*    */{
	/*    */private BillManageModel model;
	/*    */
	/*    */public CadjuststateidHandler()
	/*    */{
		/* 16 */this.model = null;
		/*    */}
	/*    */
	/*    */public void beforeListHeadTailEdit(ListHeadBeforeEditEvent event) {
		/* 20 */super.beforeListHeadTailEdit(event);
		
		StateAdjustVO vo = (StateAdjustVO) getModel()
				.getSelectedData();
		  String pk_org =vo.getPk_org();
	    	      
	    if (!NCBaseTypeUtils.isNull(pk_org)) {
	    	nc.ui.pub.bill.BillItem  obj=(nc.ui.pub.bill.BillItem)event.getSource();
	    	UIRefPane ref=(UIRefPane)obj.getComponent();
//	    	 new FilterCstateidRefUtils( model.get getEditorModel().getCardPanelWrapper().getHeadRefPane(event.getKey()))
//	 				.filterItemRefByOrg(pk_org);
//	    	UIPanel panel=new UIPanel("¿â´æ×´Ì¬"); 
//	    	event.getBillListPanel().getParentListPanel().get
//	    	getEditor().getBillListPanel().getBodyUIPanel().get
//	    	ICBizEditorModel editorModel=getEditorModel();
	    	/* 21 *///
//	    	 this.model.gets
//		    event.setReturnValue(Boolean.TRUE);
//	    	 BillListPanel pal= event.;
//			panel.setBackground(null);
//	    	UIRefPane ref=new UIRefPane("¿â´æ×´Ì¬");
	    	 new FilterCstateidRefUtils(ref).filterItemRefByOrg(pk_org);
//	    	String[] refNames = qualityMarketDialog.getQualityMarketRefPane().getRefNames();

	    }
	    event.setReturnValue(Boolean.TRUE);
		/* 21 *///
//	    event.setReturnValue(Boolean.TRUE);
		/*    */}
	
	
	protected StateAdjustVO getSelectedVO() {
		/* 51 */return ((StateAdjustVO) getModel().getSelectedData());
		/*    */}
	/*    */
	/*    */public BillManageModel getModel() {
		/* 25 */return this.model;
		/*    */}
	/*    */
	/*    */public void setModel(BillManageModel model) {
		/* 29 */this.model = model;
		/*    */}
	

	
	/*    */
}