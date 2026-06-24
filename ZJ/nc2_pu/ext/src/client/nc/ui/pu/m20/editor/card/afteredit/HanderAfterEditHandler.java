package nc.ui.pu.m20.editor.card.afteredit;

import java.util.Map;
import nc.ui.pu.m20.editor.card.afteredit.header.Bdirecttransit;
import nc.ui.pu.m20.editor.card.afteredit.header.BillDate;
import nc.ui.pu.m20.editor.card.afteredit.header.Bsctype;
import nc.ui.pu.m20.editor.card.afteredit.header.DefProcess;
import nc.ui.pu.m20.editor.card.afteredit.header.Project;
import nc.ui.pu.pub.editor.card.handler.AbstractCardHeadTailAfterEditEventHandler;
import nc.ui.pu.pub.editor.card.listener.ICardHeadTailAfterEditEventListener;

public class HanderAfterEditHandler
		extends
			AbstractCardHeadTailAfterEditEventHandler {
	public void registerEventListener(
			Map<String, ICardHeadTailAfterEditEventListener> listenerMap) {
		listenerMap.put("bsctype", new Bsctype());

		listenerMap.put("bdirecttransit", new Bdirecttransit());

		listenerMap.put("chprojectid", new Project());
        //vdef17 请购单子选项
		listenerMap.put("ctrantypeid", new DefProcess());
		listenerMap.put("dbilldate", new BillDate());
	}
}