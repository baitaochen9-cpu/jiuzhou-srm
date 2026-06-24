/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package nc.ui.pu.m422x.editor.card.afteredit;

import java.util.Map;
import nc.ui.pu.m422x.editor.card.afteredit.body.Appdept;
import nc.ui.pu.m422x.editor.card.afteredit.body.AssistUnit;
import nc.ui.pu.m422x.editor.card.afteredit.body.BatchCode;
import nc.ui.pu.m422x.editor.card.afteredit.body.Material;
import nc.ui.pu.m422x.editor.card.afteredit.body.Reqdate;
import nc.ui.pu.pub.editor.card.afteredit.CProject;
import nc.ui.pu.pub.editor.card.handler.AbstractCardBodyAfterEditEventHandler;
import nc.ui.pu.pub.editor.card.listener.AbstractRelationCalculateListener;
import nc.ui.pu.pub.editor.card.listener.ICardBodyAfterEditEventListener;
import nc.vo.pu.pub.enumeration.PuAttrNameEnum;

public class CardBodyAfterEditEventHandler
		extends
			AbstractCardBodyAfterEditEventHandler {
	public AbstractRelationCalculateListener getCalculateListener() {
		return null;
	}

	public void registerEventListener(
			Map<String, ICardBodyAfterEditEventListener> listenerMap) {
		listenerMap.put("pk_material", new Material());

		listenerMap.put("pk_appdept_v", new Appdept());

		listenerMap.put("castunitid", new AssistUnit());

		listenerMap.put("dreqdate", new Reqdate());

		listenerMap.put(PuAttrNameEnum.cprojectid.name(), new CProject());

		listenerMap.put("vbatchcode", new BatchCode());
	}
}