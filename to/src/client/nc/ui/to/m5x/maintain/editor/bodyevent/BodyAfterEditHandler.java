package nc.ui.to.m5x.maintain.editor.bodyevent;

import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.scmpub.res.billtype.TOBillType;
import nc.vo.to.m5x.entity.BillHeaderVO;
import nc.vo.to.m5x.entity.BillItemVO;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.to.m5x.maintain.editor.headevent.IOOnWayOwnerEditHandler;
import nc.ui.to.m5x.pub.OnWayOnwerUtil;
import nc.ui.to.pub.editor.ProfitCenterEditHandler;
import nc.ui.to.pub.editor.PubBatchEditHandler;

public class BodyAfterEditHandler implements
    IAppEventHandler<CardBodyAfterEditEvent> {

  @Override
  public void handleAppEvent(CardBodyAfterEditEvent e) {
    String key = e.getKey();
    if (key.equals(BillItemVO.CINDEPTVID)) {
      InDeptEditHandler handler = new InDeptEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.COUTSTORDOCID)) {
      OutStordocEditHandler handler = new OutStordocEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CTOUTSTORDOCID)) {
      TakeOutStordocEditHandler handler = new TakeOutStordocEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CINVENTORYVID)) {
    	InventoryEditHandler handler = new InventoryEditHandler();
    	handler.afterEdit(e);
    	//-----------yezhian 20250814 增加主数据转码 begin
    	ChangeMaterialByMdm hand =  new ChangeMaterialByMdm(); //CINVENTORYVID
    	hand.afterEdit(e);
    	//-----------yezhian 20250814 增加主数据转码 end
    	
      
    }
    else if (key.equals(BillItemVO.CASTUNITID)) {
      AstUnitEditHandler handler = new AstUnitEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NTAX)) {
      NTaxEditHandler handler = new NTaxEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.VBATCHCODE)) {
      PubBatchEditHandler handler =
          new PubBatchEditHandler(TOBillType.TransOrder.getCode());
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.VCHANGERATE)) {
      ChangeRateEditHandler handler = new ChangeRateEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NNUM)) {
      NumberEditHandler handler = new NumberEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NASTNUM)) {
      AssistNumEditHandler handler = new AssistNumEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NTAXRATE)
        || key.equals(BillItemVO.FTAXTYPEFLAG)) {
      TaxRateEditHandler handler = new TaxRateEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NORIGTAXNETPRICE)) {
      OrigTaxPriceEditHandler handler = new OrigTaxPriceEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NORIGNETPRICE)) {
      OrigPriceEditHandler handler = new OrigPriceEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CQTUNITID)) {
      QtUnitEditHandler handler = new QtUnitEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NQTUNITNUM)) {
      QtNumEditHandler handler = new QtNumEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.VQTUNITRATE)) {
      QtChangeRateEditHandler handler = new QtChangeRateEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NQTORIGNETPRICE)) {
      QtOrigPriceEditHandler handler = new QtOrigPriceEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NQTORIGTAXNETPRC)) {
      QtOrigTaxPriceEditHandler handler = new QtOrigTaxPriceEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CINSTORDOCID)) {
      InStordocEditHandler handler = new InStordocEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NORIGMNY)) {
      OrigMnyEditHandler handler = new OrigMnyEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NORIGTAXMNY)) {
      OrigTaxMnyEditHandler handler = new OrigTaxMnyEditHandler();
      handler.afterEdit(e);
    }

    else if (key.equals(BillItemVO.CSENDTYPEID)) {
      SendTypeEditHandler handler = new SendTypeEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CTAXCODEID)) {
      CTaxCodeEditHandler handler = new CTaxCodeEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.NCALTAXMNY)) {
      NcalTaxMnyEditHandler handler = new NcalTaxMnyEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CRECEIVEADDRID)) {
      ReceiveAddrEditHandler handler = new ReceiveAddrEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CORIGCOUNTRYID)) {
      OrigCountryEditHandler handler = new OrigCountryEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CDESTICOUNTRYID)) {
      DestiCountryEditHandler handler = new DestiCountryEditHandler();
      handler.afterEdit(e);
    }
    else if (key.startsWith("vfree")) {
      FreeItemEditHandler handler = new FreeItemEditHandler();
      handler.afterEdit(e);
    }
    else if (key.equals(BillItemVO.CINPCVID)) {
      String[] cstorcids = new String[] {
        BillItemVO.CINSTORDOCID
      };
      ProfitCenterEditHandler handrule =
          new ProfitCenterEditHandler(e, cstorcids, BillItemVO.CINPCVID);
      handrule.afterEdit(e.getRow());
    }
    else if (key.equals(BillItemVO.COUTPCVID)) {
      BillCardPanel panel = e.getBillCardPanel();
      CardPanelValueUtils util = new CardPanelValueUtils(panel);
      String takeoutstockorg =
          ValueUtils.getString(util
              .getHeadTailValue(BillHeaderVO.CTOUTSTOCKORGID));
      String pk_org =
          ValueUtils.getString(util.getHeadTailValue(BillHeaderVO.PK_ORG));
      String[] cstorcids;
      // 三方调拨,只有出货仓库根据出货利润中心过滤
      if (!pk_org.equals(takeoutstockorg)) {
        cstorcids = new String[] {
          BillItemVO.CTOUTSTORDOCID
        };
      }
      // 财务组织间调拨
      else {
        cstorcids = new String[] {
          BillItemVO.COUTSTORDOCID, BillItemVO.CTOUTSTORDOCID
        };
      }
      ProfitCenterEditHandler handrule =
          new ProfitCenterEditHandler(e, cstorcids, BillItemVO.COUTPCVID);
      handrule.afterEdit(e.getRow());
    }
    /**
     * 增加调拨订单表体点击赠品的编辑后事件。
     */
    else if (key.equals(BillItemVO.BIOLARGESSFLAG)) {
      BiolargessflagEH handler = new BiolargessflagEH();
      handler.afterEdit(e);
    }
    if (e.getAfterEditEventState() == CardBodyAfterEditEvent.NOTBATCHCOPY
        || e.getAfterEditEventState() == CardBodyAfterEditEvent.BATCHCOPYEND) {
      // 调入调出在途归属是否可编辑的控制, 因UAP不支持表头下拉框编辑事件，故在此控制之
      IOOnWayOwnerEditHandler ioOnWayOwner = new IOOnWayOwnerEditHandler();
      ioOnWayOwner.beforeEdit(e.getBillCardPanel());

      OnWayOnwerUtil onWayOnwer = new OnWayOnwerUtil(e.getBillCardPanel());
      onWayOnwer.calculateOnWayOnwer();
    }
  }

}
