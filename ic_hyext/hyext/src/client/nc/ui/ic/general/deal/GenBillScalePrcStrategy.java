package nc.ui.ic.general.deal;

import nc.ui.ic.pub.deal.IUIBillScalePrcStrategy;
import nc.ui.pub.bill.IBillItem;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.scale.BillScaleProcessor;
import nc.vo.pubapp.scale.FieldInfo;
import nc.vo.pubapp.scale.PosEnum;
import nc.vo.pubapp.scale.TotalValueScale;

/**
 * <p>
 * <b>精度处理策略：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author liuzy
 * @time 2010-5-31 下午09:10:18
 */
public class GenBillScalePrcStrategy implements IUIBillScalePrcStrategy {

  // 是否所有显示项都在表头上：对于视图的显示，要么都在表头或表体，如转单的单表界面。
  private UFBoolean isAllHead = null;

  /**
   * 
   */
  @Override
  public String[] getAssNumKey() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getCostMnyKey() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getCostPriceKey() {
    return null;
  }

  @Override
  public String[] getDiscountKey() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getGlobalLocMny() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getGroupLocMny() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getHslKey() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getMnyKey() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getNumKey() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getOrgMnyKey() {
    return null;
  }

  /**
   * 
   */
  @Override
  public String[] getPriceKey() {
    return null;
  }

  /**
   * @return isAllHead
   */
  public UFBoolean isAllHead() {
    return this.isAllHead;
  }

  /**
   * <li>这里仅处理基本的精度信息
   * <li>对于销售采购税率单价及金额需要子类调用设置价格精度的方法
   */
  @Override
  public void scaleProcess(BillScaleProcessor scale, String curTableCode,
      TotalValueScale total) {
    // 不适用于打印场景，基于VO的精度处理，仅支持数值类型字段精度处理，不支持换算率精度处理
    // 换算率精度
    // add by chenlla 打印模板精度处理
    scale.setHslCtlInfo(CollectionUtils.combineArrs(new String[] {
      ICPubMetaNameConst.VCHANGERATE
    }, this.getHslKey()), this.changePos(PosEnum.body), curTableCode);

    // 成本单价精度
    scale.setCostPriceCtlInfo(CollectionUtils.combineArrs(new String[] {
      ICPubMetaNameConst.NCOSTPRICE, ICPubMetaNameConst.NPLANNEDPRICE
    }, this.getCostPriceKey()), this.changePos(PosEnum.body), curTableCode);
    // 主数量
    scale.setNumCtlInfo(this.getMainNumKeys(), this.changePos(PosEnum.body),
        curTableCode, ICPubMetaNameConst.CUNITID, this.changePos(PosEnum.body),
        null);
    // 业务数量
    scale.setNumCtlInfo(this.getNumKeys(), this.changePos(PosEnum.body),
        curTableCode, ICPubMetaNameConst.CASTUNITID,
        this.changePos(PosEnum.body), null);
    // 库存组织成本金额精度控制，找库存组织对应财务组织的本币精度
    scale.setStockOrgCostMnyCtlInfo(
        CollectionUtils.combineArrs(this.getCostMnyKey(), new String[] {
          ICPubMetaNameConst.NCOSTMNY, ICPubMetaNameConst.NPLANNEDMNY
        }), this.changePos(PosEnum.body), null, ICPubMetaNameConst.PK_ORG,
        this.changePos(PosEnum.head), null);
    // 设置件数精度
    scale.setUnitCtlInfo(this.getNpieceKeys(), this.changePos(PosEnum.body),
        null, ICPubMetaNameConst.CMATERIALVID, this.changePos(PosEnum.body),
        null);
    // 设置件数精度
    scale.setVolumnCtlInfo(this.getVolumnKeys(), this.changePos(PosEnum.body),
        null);
    // 设置重量精度
    scale.setWeightCtlInfo(this.getWeightKeys(), this.changePos(PosEnum.body),
        null);
    // 处理精度，主要界面的BillItem的精度及添加监听
    scale.process();
    // 合计信息精度控制器
    total.setHeadTailKeys(new String[] {
      ICPubMetaNameConst.NTOTALNUM, MetaNameConst.NTOTALPIECE,
      MetaNameConst.NTOTALVOLUME, MetaNameConst.NTOTALWEIGHT
    });
  }

  /**
   * @param isAllHead
   *          要设置的 isAllHead
   */
  public void setAllHead(UFBoolean isAllHead) {
    this.isAllHead = isAllHead;
  }

  /**
   * 
   */
  protected int changePos(int pos) {
    if (this.isAllHead == null) {
      return pos;
    }
    return this.isAllHead.booleanValue() ? IBillItem.HEAD : IBillItem.BODY;
  }

  /**
   * 
   */
  protected PosEnum changePos(PosEnum pos) {
    if (this.isAllHead == null) {
      return pos;
    }
    return this.isAllHead.booleanValue() ? PosEnum.head : PosEnum.body;
  }

  /**
   * 方法功能描述：需要设置主数量精度的字段，因为借入单没有shouldnum单独抽出来，其他单据无需复写
   * <p>
   * <b>参数说明</b>
   * 
   * @return
   *         <p>
   * @since 6.0
   * @author lirr
   * @time 2010-6-22 下午02:15:20
   */
  protected String[] getMainNumKeys() {
    return CollectionUtils.combineArrs(new String[] {
      MetaNameConst.NBARCODENUM, MetaNameConst.NCORRESPONDGRSNUM,
      MetaNameConst.NCORRESPONDNUM, ICPubMetaNameConst.NCOUNTNUM,
      ICPubMetaNameConst.NTARENUM, ICPubMetaNameConst.NNUM,
      ICPubMetaNameConst.NSHOULDNUM, ICPubMetaNameConst.NGROSSNUM,
      ICPubMetaNameConst.NACCUMVMINUM
    }, this.getNumKey());
  }

  /**
   * 
   */
  protected String[] getNpieceKeys() {
    return new String[] {
      ICPubMetaNameConst.NPIECE,
    };
  }

  /**
   * 方法功能描述：需要设置数量精度的字段，因为借入单没有shouldnum单独抽出来，其他单据无需复写
   * <p>
   * <b>参数说明</b>
   * 
   * @return
   *         <p>
   * @since 6.0
   * @author lirr
   * @time 2010-6-22 下午02:15:20
   */
  protected String[] getNumKeys() {
    return CollectionUtils.combineArrs(new String[] {
      MetaNameConst.NCORRESPONDASTNUM, MetaNameConst.NCORRESPONDASTNUM,
      ICPubMetaNameConst.NASSISTNUM, ICPubMetaNameConst.NSHOULDASSISTNUM
    }, this.getAssNumKey());
  }

  /**
   * 
   */
  protected String[] getTaxRateKeys() {
    return new String[] {
      MetaNameConst.NTAXRATE
    };
  }

  /**
   * 
   */
  protected String[] getVolumnKeys() {
    return new String[] {
      ICPubMetaNameConst.NVOLUME
    };
  }

  /**
   * 
   */
  protected String[] getWeightKeys() {
    return new String[] {
      ICPubMetaNameConst.NWEIGHT
    };
  }

  protected void setDiscountCtlInfo(BillScaleProcessor scale) {

    // 设置折扣精度
    scale.setSaleDiscountCtlInfo(this.getDiscountKey(),
        this.changePos(PosEnum.body), null);
  }

  /**
   * 
   */
  protected void setGlobalExchangeCtlInfo(BillScaleProcessor scale) {
    FieldInfo rate =
        new FieldInfo(MetaNameConst.NGLOBALEXCHGRATE,
            this.changePos(IBillItem.BODY), null);
    FieldInfo ccurrency =
        new FieldInfo(MetaNameConst.CCURRENCYID,
            this.changePos(IBillItem.BODY), null);
    FieldInfo oriccurrency =
        new FieldInfo(MetaNameConst.CORIGCURRENCYID,
            this.changePos(IBillItem.BODY), null);
    scale.setGlobalExchangeCtlInfo(rate, oriccurrency, ccurrency);
  }

  /**
   * 全局本位币金额精度
   */
  protected void setGlobalLocMnyCtlInfo(BillScaleProcessor scale) {
    // 全局本位币金额精度
    String[] fields = new String[] {
      MetaNameConst.NGLOBALMNY, MetaNameConst.NGLOBALTAXMNY,
    };
    scale.setGlobalLocMnyCtlInfo(
        CollectionUtils.combineArrs(fields, this.getGlobalLocMny()),
        this.changePos(PosEnum.body), null);
  }

  /**
   * 
   */
  protected void setGroupExchangeCtlInfo(BillScaleProcessor scale) {
    FieldInfo rate =
        new FieldInfo(MetaNameConst.NGROUPEXCHGRATE,
            this.changePos(IBillItem.BODY), null);
    FieldInfo ccurrency =
        new FieldInfo(MetaNameConst.CCURRENCYID,
            this.changePos(IBillItem.BODY), null);
    FieldInfo oriccurrency =
        new FieldInfo(MetaNameConst.CORIGCURRENCYID,
            this.changePos(IBillItem.BODY), null);
    scale.setGroupExchangeCtlInfo(rate, oriccurrency, ccurrency);
  }

  /**
   * 集团本位币金额精度
   */
  protected void setGroupLocMnyCtlInfo(BillScaleProcessor scale) {
    // 集团本位币金额精度
    String[] fields = new String[] {
      MetaNameConst.NGROUPMNY, MetaNameConst.NGROUPTAXMNY,
    };
    scale.setGroupLocMnyCtlInfo(
        CollectionUtils.combineArrs(fields, this.getGroupLocMny()),
        this.changePos(PosEnum.body), null);
  }

  /**
   * 本币金额精度
   */
  protected void setMnyCtlInfo(BillScaleProcessor scale) {
    // 本币金额精度
    String[] fields = new String[] {
      MetaNameConst.NMNY, MetaNameConst.NTAX, MetaNameConst.NTAXMNY,
      ICPubMetaNameConst.NCALTAXMNY
    };
    scale.setMnyCtlInfo(CollectionUtils.combineArrs(fields, this.getMnyKey()),
        this.changePos(PosEnum.body), null, MetaNameConst.CCURRENCYID,
        this.changePos(PosEnum.body), null);
  }

  /**
   * 
   */
  protected void setOrgExchangeCtlInfo(BillScaleProcessor scale) {
    FieldInfo rate =
        new FieldInfo(MetaNameConst.NCHANGESTDRATE,
            this.changePos(IBillItem.BODY), null);
    FieldInfo ccurrency =
        new FieldInfo(MetaNameConst.CCURRENCYID,
            this.changePos(IBillItem.BODY), null);
    FieldInfo oriccurrency =
        new FieldInfo(MetaNameConst.CORIGCURRENCYID,
            this.changePos(IBillItem.BODY), null);
    FieldInfo org =
        new FieldInfo(MetaNameConst.CFANACEORGOID,
            this.changePos(IBillItem.BODY), null);
    scale.setOrgExchangeCtlInfo(rate, oriccurrency, ccurrency, org);
  }

  /**
   * 原币金额精度
   */
  protected void setOrgMnyCtlInfo(BillScaleProcessor scale) {
    // 原币金额精度
    String[] fields = new String[] {
      MetaNameConst.NORIGMNY, MetaNameConst.NORIGTAX, MetaNameConst.NORIGTAXMNY
    };
    scale.setMnyCtlInfo(
        CollectionUtils.combineArrs(fields, this.getOrgMnyKey()),
        this.changePos(PosEnum.body), null, MetaNameConst.CORIGCURRENCYID,
        this.changePos(PosEnum.body), null);
  }

  /**
   * 单价精度
   */
  protected void setPriceCtlInfo(BillScaleProcessor scale) {
    // 单价精度
    String[] fields =
        new String[] {
          MetaNameConst.NORIGPRICE, MetaNameConst.NORIGTAXPRICE,
          MetaNameConst.NORIGNETPRICE, MetaNameConst.NORIGTAXNETPRICE,

          MetaNameConst.NPRICE, MetaNameConst.NTAXPRICE,
          MetaNameConst.NNETPRICE, MetaNameConst.NTAXNETPRICE,

          MetaNameConst.NQTORIGPRICE, MetaNameConst.NQTORIGTAXPRICE,
          MetaNameConst.NQTORIGNETPRICE, MetaNameConst.NQTORIGTAXNETPRICE,

          MetaNameConst.NQTPRICE, MetaNameConst.NQTTAXPRICE,
          MetaNameConst.NQTNETPRICE, MetaNameConst.NQTTAXNETPRICE,
        };
    scale.setPriceCtlInfo(
        CollectionUtils.combineArrs(fields, this.getPriceKey()),
        this.changePos(PosEnum.body), null,MetaNameConst.CCURRENCYID,this.changePos(PosEnum.body), null);
  }

  /**
   * 报价单位数量及报价换算率
   */
  protected void setQtCtlInfo(BillScaleProcessor scale) {
    // 报价单位数量
    String[] fields = new String[] {
      MetaNameConst.NQTUNITNUM,
    };
    scale.setNumCtlInfo(fields, this.changePos(PosEnum.body), null,
        MetaNameConst.CQTUNITID, this.changePos(PosEnum.body), null);
    fields = new String[] {
      MetaNameConst.VQTUNITRATE,
    };
    // 报价换算率精度
    scale.setHslCtlInfo(fields, this.changePos(PosEnum.body), null);
  }

  /**
   * 设置税率精度
   */
  protected void setTaxRateCtlInfo(BillScaleProcessor scale) {
    // 设置税率精度
    scale.setTaxRateCtlInfo(this.getTaxRateKeys(),
        this.changePos(PosEnum.body), null);
  }

}
