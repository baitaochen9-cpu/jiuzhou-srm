 package nc.ui.mmpac.wr.event.extend;
 
 import nc.ui.pubapp.uif2app.event.IAppEventHandler;
 import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
 
 public class WrCardHeadAfterEditEvent
   implements IAppEventHandler<CardHeadTailAfterEditEvent>
 {
   public void handleAppEvent(CardHeadTailAfterEditEvent e)
   {
     if (("dbilldate".equals(e.getKey()))){
    	 new DbilldateAfterEditEvent().afterEdit(e);
     }
     if("vdef10".equals(e.getKey())){
    	 new Vdef10AfterEditEvent().afterEdit(e);
     }
   }
 }
