package nc.ui.to.m5x.maintain.editor.bodyevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.to.m5x.entity.BillHeaderVO;
import nc.vo.to.m5x.entity.BillItemVO;
import nc.vo.to.m5x.entity.BillVO;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.itf.to.m5x.IM5XRemoteCallPack;
import nc.itf.to.m5x.IQueryPCByStockOrgAndStock;

import nc.bs.framework.common.NCLocator;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.view.util.RefMoreSelectedUtils;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.scmpub.ref.FilterMaterialRefUtils;
import nc.ui.to.m5x.pub.ClearInvInfoUtil;
import nc.ui.to.m5x.pub.EditHandlerUtil;
import nc.ui.to.m5x.pub.TrafficOrgUtil;
import nc.ui.uif2.ShowStatusBarMsgUtil;

/**
 * 物料编码编辑事件
 * 
 */
public class InventoryEditHandler {

  public void afterEdit(CardBodyAfterEditEvent e) {
	  try {
		
	
    BillCardPanel cardPanel = e.getBillCardPanel();
    // 清空物料相关信息。
    ClearInvInfoUtil clearInvInfoUtil = new ClearInvInfoUtil(cardPanel);
    clearInvInfoUtil.clearInvInfo(new int[] {
      e.getRow()
    });
    if (null == e.getValue()) {
      return;
    }

    // 物料批选处理，新增物料行，处理新增行行号。
    RefMoreSelectedUtils utils = new RefMoreSelectedUtils(e.getBillCardPanel());
    int[] rows = utils.refMoreSelected(e.getRow(), e.getKey(), true);
    EditHandlerUtil editUtil = new EditHandlerUtil(cardPanel);
    editUtil.setWhInfo(rows, true);
    // 补充单位、换算率等信息(已走前台缓存)
    CardPanelValueUtils util = new CardPanelValueUtils(cardPanel);

    BillVO origBillVO = util.getBillValueVO(BillVO.class);
    BillVO selBillRowVO = this.getEditRowBillVO(rows, origBillVO);

    // 合并调用匹配结算规则、询价
    IM5XRemoteCallPack call =
        NCLocator.getInstance().lookup(IM5XRemoteCallPack.class);
    BillVO retBillVO = util.getBillValueVO(BillVO.class);
    List<BillItemVO> itemVOlist = new ArrayList<BillItemVO>();
    for (BillItemVO itemVO : retBillVO.getChildrenVO()) {
      if (itemVO.getCinventoryid() != null) {
        itemVOlist.add(itemVO);
      }
    }
    retBillVO.setChildrenVO(itemVOlist.toArray(new BillItemVO[0]));

    try {
      retBillVO = call.invAfterEditHandler(retBillVO);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
    String message = retBillVO.getMapMessage().get("HINTNOPRICE");
    // 合并结算规则及价格到前台VO
    this.combineResult(origBillVO, retBillVO, rows, cardPanel);
    // 重新设置界面VO
    // cardPanel.setBillValueVO(origBillVO);
    if (message != null && message.trim().length() > 0) {
      MessageDialog
          .showHintDlg(
              cardPanel,
              NCLangRes.getInstance().getStrByID("4009011_0", "04009011-0038")/* 询价提示 */,
              message);
    }
	  } catch (Exception e2) {
		  ExceptionUtils.wrappBusinessException(e2.toString());
		}
  }

  public void beforeEdit(CardBodyBeforeEditEvent e) {
    BillCardPanel panel = e.getBillCardPanel();
    CardPanelValueUtils util = new CardPanelValueUtils(panel);
    // 调入库存组织为空，不可编辑
    String outStock = util.getHeadTailStringValue(BillHeaderVO.CINSTOCKORGID);
    if (null == outStock || outStock.length() < 1) {
      ShowStatusBarMsgUtil.showStatusBarMsg(
          NCLangRes.getInstance().getStrByID("4009011_0", "04009011-0051")/*
                                                                          * 请先选择调入库存组织
                                                                          * 。
                                                                          */,
          e.getContext());
      e.setReturnValue(Boolean.valueOf(false));
      return;
    }
    String vtrantypeid = util.getHeadTailStringValue(BillHeaderVO.CTRANTYPEID);
    if (PubAppTool.isNull(vtrantypeid)) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4009011_0", "04009011-0049")/*
                                                                  * @res
                                                                  * "请先录入订单类型。"
                                                                  */);
    }
    // 设置可以多选
    UIRefPane refPane =
        (UIRefPane) e.getBillCardPanel().getBodyItem(BillItemVO.CINVENTORYVID)
            .getComponent();
    refPane.setMultiSelectedEnabled(true);

    // 编辑物料存在上游单据，物料不可编辑
    String srcbid =
        ValueUtils.getString(util.getBodyValue(e.getRow(), BillItemVO.CSRCBID,
            null));
    if (!PubAppTool.isNull(srcbid)) {
      ShowStatusBarMsgUtil.showStatusBarMsg(
          NCLangRes.getInstance().getStrByID("4009011_0", "04009011-0052")/*
                                                                          * 物料存在上游单据
                                                                          * ，
                                                                          * 物料不可编辑
                                                                          * 。
                                                                          */,
          e.getContext());
      e.setReturnValue(Boolean.valueOf(false));
      return;
    }

    // 按调出库存组织设置物料的数据权限
    FilterMaterialRefUtils refUtil = new FilterMaterialRefUtils(refPane);
    refUtil.filterItemRefByOrg(e.getContext().getPk_org());

    // 折扣类存货
    refUtil.filterRefByDiscountflag(UFBoolean.FALSE);
    // 过滤掉劳务
    refUtil.filterRefByFeeflag(UFBoolean.FALSE);
  }

  public void updateNumPriceMny(BillVO origBillVO, BillVO retBillVO,
      int[] selectedrows, BillCardPanel cardPanel) {
    String[] updateFields =
        new String[] {
          BillItemVO.NNETPRICE, BillItemVO.NTAXNETPRICE, BillItemVO.NMNY,
          BillItemVO.NTAXMNY, BillItemVO.NTAX, BillItemVO.NORIGTAXNETPRICE,
          BillItemVO.NORIGNETPRICE, BillItemVO.NORIGMNY,
          BillItemVO.NORIGTAXMNY, BillItemVO.NGLOBALMNY,
          BillItemVO.NGLOBALTAXMNY, BillItemVO.NGROUPMNY,
          BillItemVO.NGROUPTAXMNY, BillItemVO.NASKQTORIGPRICE,
          BillItemVO.NASKQTORIGTAXPRC, BillItemVO.NNUM, BillItemVO.NASTNUM,
          BillItemVO.NQTUNITNUM, BillItemVO.NQTNETPRICE,
          BillItemVO.NQTTAXNETPRICE, BillItemVO.NQTORIGNETPRICE,
          BillItemVO.NQTORIGTAXNETPRC
        };
    BillItemVO[] origItems = origBillVO.getChildrenVO();
    BillItemVO[] retItems = retBillVO.getChildrenVO();
    Map<String, BillItemVO> origMap1 = new HashMap<String, BillItemVO>();
    Map<String, BillItemVO> retMap = new HashMap<String, BillItemVO>();
    for (int i = 0; i < origItems.length; i++) {
      origMap1.put(origItems[i].getCrowno(), origItems[i]);
    }
    for (int i = 0; i < retItems.length; i++) {
      retMap.put(retItems[i].getCrowno(), retItems[i]);
    }
    int len = selectedrows.length;
    cardPanel.setTatolRowShow(false);
    for (int i = 0; i < len; i++) {
      BillItemVO origItem = origItems[selectedrows[i]];
      BillItemVO retItem = retMap.get(origItem.getCrowno());
      for (String key : updateFields) {
        origItem.setAttributeValue(key, retItem.getAttributeValue(key));
        cardPanel.setBodyValueAt(retItem.getAttributeValue(key),
            selectedrows[i], key);
        new TrafficOrgUtil(cardPanel).setBodyDefaultTrafficorg(selectedrows[i]);
      }
    }

    cardPanel.getBillModel().loadLoadRelationItemValue(selectedrows);
    cardPanel.setTatolRowShow(true);
  }

  private void combineHead(BillHeaderVO origHeadVO, BillHeaderVO retHeadVO,
      BillCardPanel cardPanel) {
    String[] names =
        new String[] {
          BillHeaderVO.CONWAYOWNERORGID, BillHeaderVO.FIOONWAYOWNERFLAG,
          BillHeaderVO.FOTONWAYOWNERFLAG
        };
    for (String key : names) {
      origHeadVO.setAttributeValue(key, retHeadVO.getAttributeValue(key));
      cardPanel.setHeadItem(key, retHeadVO.getAttributeValue(key));
    }

  }

  private void combineResult(BillVO origBillVO, BillVO retBillVO, int[] rows,
      BillCardPanel cardPanel) {
    this.combineHead(origBillVO.getParentVO(), retBillVO.getParentVO(),
        cardPanel);

    // 合并结算规则上带过来的信息
    BillItemVO[] origBillItems = origBillVO.getChildrenVO();
    BillItemVO[] retBillItems = retBillVO.getChildrenVO();
    Map<String, BillItemVO> origMap1 = new HashMap<String, BillItemVO>();
    Map<String, BillItemVO> retMap = new HashMap<String, BillItemVO>();
    for (int i = 0; i < origBillItems.length; i++) {
      origMap1.put(origBillItems[i].getCrowno(), origBillItems[i]);
    }
    for (int i = 0; i < retBillItems.length; i++) {
      retMap.put(retBillItems[i].getCrowno(), retBillItems[i]);
    }
    int len = rows.length;
    for (int i = 0; i < len; i++) {
      BillItemVO origBillItem = origBillItems[rows[i]];
      BillItemVO retBillItem = retMap.get(origBillItem.getCrowno());
      this.conbineSettleRuleInfo(origBillItem, retBillItem, rows[i], cardPanel);
    }
    // 合并后台返回的价格
    this.updateNumPriceMny(origBillVO, retBillVO, rows, cardPanel);
  }

  private void conbineSettleRuleInfo(BillItemVO origItem, BillItemVO retItem,
      int row, BillCardPanel cardPanel) {
    String[] fields =
        new String[] {
          BillItemVO.CIOSETTLERULEID, BillItemVO.CIOSETTLERULE_BID,
          BillItemVO.COTSETTLERULEID, BillItemVO.COTSETTLERULE_BID,

          BillItemVO.NADDPRICERATE, BillItemVO.CINPCID, BillItemVO.CINPCVID,
          BillItemVO.COUTPCID, BillItemVO.COUTPCVID, BillItemVO.CASTUNITID,
          BillItemVO.CQTUNITID, BillItemVO.VCHANGERATE, BillItemVO.VQTUNITRATE,
          BillItemVO.NTAXRATE, BillItemVO.FTAXTYPEFLAG, BillItemVO.CTAXCODEID,
          BillItemVO.CUNITID, BillItemVO.COUTSTORDOCID,
          BillItemVO.CINSTORDOCID, BillItemVO.CTOUTSTORDOCID
        };

    for (String key : fields) {
      origItem.setAttributeValue(key, retItem.getAttributeValue(key));
      cardPanel.setBodyValueAt(retItem.getAttributeValue(key), row, key);
    }
  }

  private BillVO getEditRowBillVO(int[] rows, BillVO origBillVO) {
    BillItemVO[] items = origBillVO.getChildrenVO();
    List<BillItemVO> lstItems = new ArrayList<BillItemVO>();
    for (int row : rows) {
      lstItems.add(items[row]);
    }
    BillVO ret = new BillVO();
    ret.setParentVO(origBillVO.getParentVO());
    ret.setChildrenVO(lstItems.toArray(new BillItemVO[0]));
    return ret;
  }

  /**
   * 设置物料相关信息 包括：业务计量单位、报价计量单位、换算率、报价计量单位换算率、是否报价计量单位固定换算率、是否固定换算率
   * 
   * @param rows
   * @param util
   */
  private void setInvInfo(int[] rows, CardPanelValueUtils util) {
    String soUnit = null;
    String[] pk_materials = new String[rows.length];
    for (int i = 0; i < rows.length; i++) {
      pk_materials[i] =
          util.getBodyStringValue(rows[i], BillItemVO.CINVENTORYVID);
    }
    // 批查销售单位(前台有缓存)
    Map<String, String> map =
        MaterialPubService.querySaleMeasdocIDByPks(pk_materials);
    for (int i = 0; i < rows.length; i++) {
      String unit = util.getBodyStringValue(rows[i], BillItemVO.CUNITID);
      // 取物料的销售默认单位，如没有取主单位
      String pk_material =
          util.getBodyStringValue(rows[i], BillItemVO.CINVENTORYVID);
      if (null != map && 0 != map.size()
          && !PubAppTool.isNull(map.get(pk_material))) {
        soUnit = map.get(pk_material);
      }
      else {
        soUnit = unit;
      }
      util.setBodyValue(soUnit, rows[i], BillItemVO.CASTUNITID);
      util.setBodyValue(unit, rows[i], BillItemVO.CQTUNITID);

      String rate =
          MaterialPubService.queryMainMeasRateByMaterialAndMeasdoc(pk_material,
              unit, soUnit);
      util.setBodyValue(rate, rows[i], BillItemVO.VCHANGERATE);

      // 物料编辑公式可配报价单位，所以要重新计算报价换算率
      String cqtunitid = util.getBodyStringValue(rows[i], BillItemVO.CQTUNITID);
      rate =
          MaterialPubService.queryMainMeasRateByMaterialAndMeasdoc(pk_material,
              unit, cqtunitid);
      // util.setBodyValue(HslParseUtil.HSL_ONE, rows[i],
      // BillItemVO.VQTUNITRATE);
      util.setBodyValue(rate, rows[i], BillItemVO.VQTUNITRATE);

    }
  }

  // 设置收发货利润中心字段
  private void setPcId(int[] rows, CardPanelValueUtils util) {
    String ctoutstockorgid =
        util.getHeadTailStringValue(BillHeaderVO.CTOUTSTOCKORGID);
    String cinstorckorgid =
        util.getHeadTailStringValue(BillHeaderVO.CINSTOCKORGID);
    Set<String> set = new HashSet<String>();
    set.add(ctoutstockorgid);
    set.add(cinstorckorgid);
    IQueryPCByStockOrgAndStock queryPC =
        NCLocator.getInstance().lookup(IQueryPCByStockOrgAndStock.class);
    MapList<String, String> maplist = new MapList<String, String>();
    try {
      maplist = queryPC.queryPCByStockOrg(set);
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
    List<String> outlist = maplist.get(ctoutstockorgid);
    List<String> inlist = maplist.get(cinstorckorgid);
    for (int i = 0; i < rows.length; i++) {
      if (outlist != null && outlist.size() > 0) {
        util.setBodyValue(outlist.get(0), rows[i], BillItemVO.COUTPCID);
        util.setBodyValue(outlist.get(1), rows[i], BillItemVO.COUTPCVID);
      }
      if (inlist != null && inlist.size() > 0) {
        util.setBodyValue(inlist.get(0), rows[i], BillItemVO.CINPCID);
        util.setBodyValue(inlist.get(1), rows[i], BillItemVO.CINPCVID);
      }
    }
  }
}
