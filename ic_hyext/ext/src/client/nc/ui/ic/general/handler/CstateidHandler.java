package nc.ui.ic.general.handler;

import nc.ui.ic.material.query.InvInfoUIQuery;
import nc.ui.ic.pub.handler.card.ICCardEditEventHandler;
import nc.ui.ic.special.handler.FilterCstateidRefUtils;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.lang.RuleRes;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.data.ValueUtils;

/**
 * ¿â´æ×´Ì¬±à¼­¿ØÖÆ
 * 
 * @since 6.0
 * @version 2010-12-21 ÏÂÎç02:42:35
 * @author chenlla
 */
public class CstateidHandler extends ICCardEditEventHandler {

  @Override
  public void beforeCardBodyEdit(CardBodyBeforeEditEvent event) {
    int row = event.getRow();
    String cmaterialvid = this.getEditorModel().getCardPanelWrapper().getBodyValueAt_String(row,
        ICPubMetaNameConst.CMATERIALVID);
    if(StringUtil.isSEmptyOrNull(cmaterialvid)){
      event.setReturnValue(Boolean.FALSE);
      return;
    }
    InvBasVO invBasVO = InvInfoUIQuery.getInstance().getInvInfoQuery().getInvBasVO(cmaterialvid);
    UFBoolean isStateManage = invBasVO.getFix1();
    if (!ValueCheckUtil.isTrue(isStateManage)) {
      event.setReturnValue(Boolean.FALSE);
      this.getEditorModel().getContext().showStatusBarMessage(
          RuleRes.getMaterialStateErr());
      return;
    }
    
    String pk_org =
    	      ValueUtils.getString(this.getEditorModel().getCardPanelWrapper().getHeadItem(ICPubMetaNameConst.PK_ORG)
    	          .getValueObject());
    if (!NCBaseTypeUtils.isNull(pk_org)) {

    	 new FilterCstateidRefUtils(this.getEditorModel().getCardPanelWrapper().getBodyRefPane(event.getKey())).filterItemRefByOrg(pk_org);

    }
    event.setReturnValue(Boolean.TRUE);
  }

}
