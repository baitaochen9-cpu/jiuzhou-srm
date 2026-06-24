package nc.ui.mmpac.wr.event;

import nc.ui.mmf.framework.handler.MMBaseHandler;
import nc.ui.mmf.framework.handler.MMEventHandler;
import nc.ui.mmpac.wr.handler.head.WrCdeptHandler;
import nc.ui.mmpac.wr.handler.head.WrCshiftidHandler;
import nc.ui.mmpac.wr.handler.head.WrCteamidHandler;
import nc.ui.mmpac.wr.handler.head.WrCwkidHandler;
import nc.ui.mmpac.wr.handler.head.WrCworkmanHandler;
import nc.ui.mmpac.wr.handler.head.WrDbillDateHandler;
import nc.ui.mmpac.wr.handler.head.WrTranstypeHandler;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.mmpac.wr.entity.WrVO;

/** 生产报告表头编辑后事件
 * 
 * @since 6.5
 * @version 2012-7-20
 * @author wangweiab */
public class WrCardHeadTailAfterEditHandler extends MMEventHandler implements
        IAppEventHandler<CardHeadTailAfterEditEvent> {

    @Override
    public void handleAppEvent(CardHeadTailAfterEditEvent e) {
        MMBaseHandler handler = this.getHandler(e.getKey());
        if (handler != null) {
            handler.afterEdit(e);
        }
        e.getBillCardPanel().execHeadTailLoadFormulas();
    }

    @Override
    public void initMap() {
        // 添加表头交易类型
        this.putHandler(WrVO.VTRANTYPEID, WrTranstypeHandler.class);
        // 添加表头生产部门
        this.putHandler(WrVO.CDEPTVID, WrCdeptHandler.class);
        // 添加表头班次
        this.putHandler(WrVO.CSHIFTID, WrCshiftidHandler.class);
        // 添加表头班组
        this.putHandler(WrVO.CTEAMID, WrCteamidHandler.class);
        // 添加表头工作中心
        this.putHandler(WrVO.CWKID, WrCwkidHandler.class);
        // 添加表头作业员
        this.putHandler(WrVO.CWORKMANID, WrCworkmanHandler.class);
        // 添加表头报产日期
        this.putHandler(WrVO.DBILLDATE, WrDbillDateHandler.class);
    }
}
