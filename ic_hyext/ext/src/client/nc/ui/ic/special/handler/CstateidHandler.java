package nc.ui.ic.special.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nc.ui.ic.material.query.InvInfoUIQuery;
import nc.ui.ic.pub.handler.card.ICCardEditEventHandler;
import nc.ui.ic.pub.util.CardPanelWrapper;
import nc.ui.ic.pub.util.UIBusiCalculator;
import nc.ui.ic.special.model.ICSpeBizEditorModel;
import nc.ui.ic.special.view.ICSpeCardBodyEntity;
import nc.ui.ic.storestate.ref.StoreStateRefModel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.view.util.RefMoreSelectedUtils;
import nc.ui.scmpub.ref.FilterMaterialRefUtils;
import nc.vo.ic.batchcode.ICBatchFields;
import nc.vo.ic.material.deal.UnitAndHslProc;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.special.define.ICSpecialBillEntity;
import nc.vo.ic.special.define.ICSpecialBodyEntity;
import nc.vo.ic.special.define.SpecialMetaNameConst;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>调整单物料编辑事件处理
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author liuzy
 * @time 2010-6-6 下午06:30:19
 */
public class CstateidHandler extends ICCardEditEventHandler {

 

  @Override
  public void beforeCardBodyEdit(CardBodyBeforeEditEvent event) {
    super.beforeCardBodyEdit(event);
    this.dealFilterByStorc(event.getKey());
  }

  /**
   * 物料是否按仓库过滤
   * 
   * @param key
   */
  protected void dealFilterByStorc(String key) {
    // 是否按仓库过滤(参数IC050)
    CardPanelWrapper wrapper = this.getEditorModel().getCardPanelWrapper();
    boolean isFilterByStoc = false;
    String pk_org =
      ValueUtils.getString(wrapper.getHeadItem(ICPubMetaNameConst.PK_ORG)
          .getValueObject());

    BillItem bi = wrapper.getHeadItem(ICPubMetaNameConst.CWAREHOUSEID);
   
    if (!NCBaseTypeUtils.isNull(pk_org)) {
//   	StoreStateRefModel model=new StoreStateRefModel("公司库存状态");
//  	model.setWherePart("pk_org='"+pk_org+"'", true);
//    	
    	 new FilterCstateidRefUtils(wrapper.getBodyRefPane(key)).filterItemRefByOrg(pk_org);
//        .filterRefByStorc(pk_stoc);
//    	if(event.getKey()!=null&&"cstateid".equals(event.getKey())){
//			 CardPanelWrapper wrapper = getEditorModel().getCardPanelWrapper();
//			 /* 126 */     boolean isFilterByStoc = false;
//			 /* 127 */     String pk_org =wrapper.getHeadItem("pk_org").getValueObject().toString();
//			StoreStateRefModel  sss=new StoreStateRefModel("name");
//			sss.setWherePart("pk_org='"+pk_org+"'");
//		}
    }
  }

  
}
