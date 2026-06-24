package nc.bs.to.m5x.maintain.rule.insert;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.vo.bd.material.MaterialVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.scmpub.res.billtype.TOBillType;
import nc.vo.scmpub.util.VOFieldLengthChecker;
import nc.vo.to.m5x.entity.BillHeaderVO;
import nc.vo.to.m5x.entity.BillItemVO;
import nc.vo.to.m5x.entity.BillVO;
import nc.vo.to.m5x.enumeration.TransMode;
import nc.vo.to.m5xtrantype.entity.M5xTranTypeVO;
import nc.vo.to.m5xtrantype.enumeration.FModyType;

import nc.itf.scmpub.reference.uap.bd.customer.CustomerPubService;
import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.itf.scmpub.reference.uap.bd.supplier.SupplierPubService;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.scmpub.reference.uap.org.CostRegionPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.to.m5atrantype.IM5aTranTypeService;

import nc.pubitf.so.m30.to.pub.ISaleOrderForTO;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.uif2.validation.ValidationException;
import nc.cmp.bill.util.SysInit;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.to.m5xtrantype.M5xTranTypeImpl;

/**
 * @description
 *              单据检查规则 <li>直运调拨订单检查 <li>检查单据必输项非空 <li>检查日期 <li>检查存货是否已经分配 <li>
 *              检查结算规则 <li>
 *              检查物流组织符合物流委托关系 <li>外部供应商寄存检查。不支持赠品、不支持三方调拨 <li>检查自定义项（公共框架实现？）
 *              <li>数量、金额校验 <li>表体行号检查 <li>字段极限值检查 <li>检查财务组织间调拨和三方调拨客商<li>
 *              检查三方调拨调出方与出货方的财务组织
 * @scene
 *        调拨订单新增保存、修改保存、调拨订单修订修改保存
 * 
 * @since 6.1
 * @time 2010-1-14 下午04:39:02
 * @author 孙伟
 */
public class CheckBillRule implements IRule<BillVO> {

  /**
   * 方法功能描述：检查三方调拨调出方与出货方的财务组织是否相同，相同则不支持。 <b>参数说明</b>
   * <p>
   * 
   * @author lixlc
   * @time 2010-10-14 下午07:07:54
   */
  private void check5CFinanceOrg(BillVO vo) {
    BillHeaderVO head = vo.getParentVO();
    if (!head.getPk_org().equals(head.getCtoutstockorgid())
        && head.getCoutfinanceorgid().equals(head.getCtoutfinanceorgid())) {
      String msg =
          NCLangRes4VoTransl.getNCLangRes().getStrByID("4009011_0",
              "04009011-0082")/*
                               * @res "调出财务组织和出货财务组织相同，库存组织却不同，不支持此种业务!"
                               */;
      ExceptionUtils.wrappBusinessException(msg);
    }
  }

  /**
   * 检查折本汇率是否为0或空
   * 
   * @param vo
   */
  private void checkCurrChangeRate(BillVO vo) {
    BillHeaderVO header = vo.getParentVO();
    UFDouble rate = header.getNexchangerate();
    if (rate == null || MathTool.equals(rate, UFDouble.ZERO_DBL)) {
      String msg =
          NCLangResOnserver.getInstance().getStrByID("4009011_0",
              "04009011-0310")/* 折本汇率为0或空！不能保存！ */;
      ExceptionUtils.wrappBusinessException(msg);
    }
  }

  /**
   * 方法功能描述：检查调出方是否有内部客商，调入方是否有。
   * <p>
   * <b>参数说明</b>
   * 
   * @param vo
   *          <p>
   * @author lixlc
   * @time 2010-7-2 下午01:53:22
   */
  private void checkCustSup(BillVO vo) {
    Integer fModeFlag = vo.getParentVO().getFmodeflag();
    BillHeaderVO head = vo.getParentVO();
    String outFinanceOrgID = head.getCoutfinanceorgid();
    String inFinanceOrgID = head.getCinfinanceorgid();
    // 财务组织间调拨
    if (TransMode.TRANSMODE_5D.equalsValue(fModeFlag)) {
      this.querySupplier(new String[] {
        outFinanceOrgID
      });
      this.queryCustomer(new String[] {
        inFinanceOrgID
      });
    }
    // 三方调拨
    if (TransMode.TRANSMODE_5C.equalsValue(fModeFlag)) {
      String tOut = head.getCtoutfinanceorgid();
      this.querySupplier(new String[] {
        outFinanceOrgID, tOut
      });
      this.queryCustomer(new String[] {
        outFinanceOrgID, inFinanceOrgID
      });
    }
  }

  private void checkDate(BillVO bill) {
    BillItemVO[] items = bill.getChildrenVO();

    for (BillItemVO item : items) {
      if (item.getStatus() == VOStatus.DELETED) {
        continue;
      }

      // 计划发货日、计划到货日
      UFDate planarrivedate = item.getDplanarrivedate();
      if (null == planarrivedate) {
        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
            .getNCLangRes().getStrByID("4009011_0", "04009011-0083")/*
                                                                     * @res
                                                                     * "计划到货日不能为空!"
                                                                     */);
        return;
      }

      UFDate planoutdate = item.getDplanoutdate();
      if (null == planoutdate) {
        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
            .getNCLangRes().getStrByID("4009011_0", "04009011-0084")/*
                                                                     * @res
                                                                     * "计划发货日不能为空!"
                                                                     */);
        return;
      }
      UFDate dbilldate = item.getDbilldate();
      if (planoutdate.before(dbilldate)) {
        String message =
            NCLangResOnserver.getInstance().getStrByID("4009011_0",
                "04009011-0317", null, new String[] {
                  item.getCrowno()
                })/* 单据日期不能晚于计划发货日期!(第{0}行) */;
        ExceptionUtils.wrappBusinessException(message);
      }
      if (planarrivedate.before(planoutdate)) {
        String message =
            NCLangResOnserver.getInstance().getStrByID("4009011_0",
                "04009011-0298", null, new String[] {
                  item.getCrowno()
                })/* 计划到货日不能早于计划发货日期!(第{0}行) */;
        ExceptionUtils.wrappBusinessException(message);
      }
    }

  }

  private void checkDriect(BillVO vo) {
    String trantype = vo.getParentVO().getCtrantypeid();
    if (null == trantype) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4009011_0", "04009011-0085")/*
                                                                   * @res
                                                                   * "取不到交易类型"
                                                                   */);
    }
    // 根据交易类型获得调拨类型
    M5xTranTypeImpl impl = new M5xTranTypeImpl();
    M5xTranTypeVO tranTypeVO = null;
    try {
      tranTypeVO = impl.queryTranType(trantype);
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
    if (null == tranTypeVO) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4009011_0", "04009011-0085")/*
                                                                   * @res
                                                                   * "取不到交易类型"
                                                                   */);
      return;
    }
    Integer modetype = tranTypeVO.getFmodetype();

    BillItemVO[] items = vo.getChildrenVO();
    Map<String, UFBoolean> mapSODirect = this.getIsSODirect(items);
    Map<String, UFBoolean> map5ADirect = this.getIs5ADirect(items);
    for (BillItemVO item : items) {
      if (item.getStatus() == VOStatus.DELETED) {
        continue;
      }
      this.checkTransType(modetype, item, mapSODirect, map5ADirect);
      this.checkReceiveCust(modetype, item);
    }
  }

  /**
   * 方法功能描述：检查物料是否已分配到指定库存组织。
   * <p>
   * <b>参数说明</b>
   * 
   * @param vo
   *          <p>
   * @author lixlc
   * @time 2010-7-1 下午04:54:01
   */
  private void checkInvAlreadyAllot(BillVO vo) {
    Set<String> set = new HashSet<String>();   
    Map<String,String> mateirla_map = new HashMap<String,String>();//添加自定义项检查
    
    BillHeaderVO header = vo.getParentVO();
    for (BillItemVO item : vo.getChildrenVO()) {
      set.add(item.getCinventoryvid());
      
      mateirla_map.put(item.getCinventoryvid(), item.getVbdef1()); //添加自定义项检查
    }
    String[] pk_materials = new String[set.size()];
    set.toArray(pk_materials);
    
    String inStorckOrg = header.getCinstockorgid();
  /*********************************JZ_yezhian 20250805 调拨订单跳过分配检查*********************************************************/  
    /*启用业务参数控制，是否执行库存组织分配检查，这里要求物料都有主数据，
     */
    UFBoolean paraBoolean =  UFBoolean.FALSE; //默认保持标准产品逻辑
    try {
		 paraBoolean = SysInit.getParaBoolean(header.getPk_org(), "TO21");
	} catch (BusinessException e) {
				 ExceptionUtils.wrappException(e);
	}
    
    if (paraBoolean.booleanValue()) {
    	
    	for(String key : mateirla_map.keySet()){
    		if (null == mateirla_map.get(key) || "".equals(mateirla_map.get(key))) { //如果有空值，直接返回异常信息
    			ExceptionUtils.wrappBusinessException("启用非分配调拨时，检查调入方物料信息必须有值。（如果调入信息为空，请检查物料主数据是否已关联双方物料）");
			}
    	}
    	return ; //返回，跳过下方原产品校验
	} 
  //**********************************JZ_yezhian 20250805 end****************************************************  
    
    // 检查物料是否分配到调入库存组织
   
    if (!header.getBunilateralflag().booleanValue()) {
      MaterialPubService.checkMaterialVisiabilityInStorckOrg(inStorckOrg,
          pk_materials);
    }
    // 检查物料是否分配到调出库存组织
    String outStorckOrg = header.getPk_org();
    MaterialPubService.checkMaterialVisiabilityInStorckOrg(outStorckOrg,
        pk_materials);
    // 检查物料是否分配到出货库存组织
    String toutStorckOrg = header.getCtoutstockorgid();
    if (!outStorckOrg.equals(toutStorckOrg)) {
      MaterialPubService.checkMaterialVisiabilityInStorckOrg(toutStorckOrg,
          pk_materials);
    }
  }

  /**
   * 检查存货不可是劳务、折扣类存货
   * 
   * @param vo
   */
  private void checkInvAttribute(BillVO vo) {
    // 查询物料属性
    Set<String> pks = new HashSet<String>();
    for (BillItemVO item : vo.getChildrenVO()) {
      pks.add(item.getCinventoryid());
    }
    if (pks.size() == 0) {
      return;
    }
    String[] fields = new String[] {
      MaterialVO.PK_MATERIAL, MaterialVO.FEE, MaterialVO.DISCOUNTFLAG
    };
    Map<String, MaterialVO> map =
        MaterialPubService.queryMaterialBaseInfo(pks.toArray(new String[0]),
            fields);
    for (BillItemVO item : vo.getChildrenVO()) {
      MaterialVO mvo = map.get(item.getCinventoryid());
      UFBoolean fee = mvo.getFee();
      UFBoolean discount = mvo.getDiscountflag();
      if (null != fee && fee.booleanValue() || null != discount
          && discount.booleanValue()) {
        String msg =
            NCLangRes4VoTransl.getNCLangRes().getStrByID("4009011_0",
                "04009011-0108")/* @res "劳务和折扣类存货不能调拨！" */;
        ExceptionUtils.wrappBusinessException(msg);
      }
    }
  }

  private void checkLimitValue(BillVO vo) {
    VOFieldLengthChecker.checkVOFieldsLength(vo);
  }

  private void checkMustInput(BillVO bill) {
    BillHeaderVO header = bill.getParentVO();

    String vtrantypecode = header.getVtrantypecode();
    if (PubAppTool.isNull(vtrantypecode)) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4009011_0", "04009011-0086")/*
                                                    * @res "交易类型不可为空。"
                                                    */);
    }

    String cincbid = header.getCinstockorgid();
    if (PubAppTool.isNull(cincbid)) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4009011_0", "04009011-0087")/*
                                                    * @res "调入库存组织不可为空。"
                                                    */);
    }

    String coutcbid = header.getPk_org();
    if (PubAppTool.isNull(coutcbid)) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4009011_0", "04009011-0088")/*
                                                    * @res "调出库存组织不可为空。"
                                                    */);
    }

    String ctoutStorcOrg = header.getCtoutstockorgid();
    if (PubAppTool.isNull(ctoutStorcOrg)) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4009011_0", "04009011-0019")/*
                                                    * @res "出货库存组织不可为空。"
                                                    */);
    }
    String coutcurrtype = header.getCcurrencyid();
    if (PubAppTool.isNull(coutcurrtype)) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4009011_0", "04009011-0089")/*
                                                    * @res "币种不可为空。"
                                                    */);
    }

    UFDate dbilldate = header.getDbilldate();
    if (null == dbilldate) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4009011_0", "04009011-0090")/*
                                                    * @res "单据日期不可为空。"
                                                    */);
    }

    BillItemVO[] items = bill.getChildrenVO();
    for (BillItemVO item : items) {
      if (item.getStatus() == VOStatus.DELETED) {
        continue;
      }

      String cinventoryid = item.getCinventoryid();
      if (PubAppTool.isNull(cinventoryid)) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0091")/*
                                                      * @res "物料不可为空。"
                                                      */);
      }

      UFDouble nnum = item.getNnum();
      if (null == nnum) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0092")/*
                                                      * @res "主数量不可为空。"
                                                      */);
      }
      if (MathTool.compareTo(nnum, UFDouble.ZERO_DBL) == 0) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0093")/*
                                                      * @res "主数量不可为零。"
                                                      */);
      }

      /*  UFDouble nastnum = item.getNastnum();
        if (null == nastnum) {
          ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4009011_0", "04009011-0094")
                                                        * @res "数量不可为空。"
                                                        );
        }
        if (MathTool.compareTo(nastnum, UFDouble.ZERO_DBL) == 0) {
          ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4009011_0", "04009011-0095")
                                                        * @res "数量不可为零。"
                                                        );
        }*/

      String qtUnit = item.getCqtunitid();
      if (PubAppTool.isNull(qtUnit)) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0096")/*
                                                      * @res "报价计量单位不可为空。"
                                                      */);
      }
      String astUnit = item.getCastunitid();
      if (PubAppTool.isNull(astUnit)) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0097")/*
                                                      * @res "单位不可为空。"
                                                      */);
      }

      String vchangerate = item.getVchangerate();
      if (PubAppTool.isNull(vchangerate)) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0098")/*
                                                      * @res "换算率不可为空。"
                                                      */);
      }
      String vqtchangerate = item.getVqtunitrate();
      if (PubAppTool.isNull(vqtchangerate)) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0099")/*
                                                      * @res "报价计量单位换算率不可为空。"
                                                      */);
      }
    }
  }

  private void checkOuterConsign(BillVO vo) {
    BillHeaderVO header = vo.getParentVO();
    String trantype = header.getCtrantypeid();
    // 根据交易类型获得调拨类型
    M5xTranTypeImpl impl = new M5xTranTypeImpl();
    M5xTranTypeVO tranType = null;
    try {
      tranType = impl.queryTranType(trantype);
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }

    // if外部供应商寄
    if (tranType == null
        || !FModyType.JC_MODETYPE.equalsValue(tranType.getFmodetype())) {
      return;
    }

    Integer fmodeflag = header.getFmodeflag();
    if (TransMode.TRANSMODE_5C.value().equals(fmodeflag)) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4009011_0", "04009011-0100")/*
                                                    * @res "外部供应商寄存，不支持三方调拨。"
                                                    */);
    }

    BillItemVO[] items = vo.getChildrenVO();
    for (BillItemVO item : items) {
      if (item.getStatus() == VOStatus.DELETED) {
        continue;
      }

      UFBoolean biolargessflag = item.getBiolargessflag();
      if (biolargessflag != null && biolargessflag.booleanValue()) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0101")/*
                                                      * @res "外部供应商寄存，不支持赠品。"
                                                      */);
      }
    }
  }

  private void checkReceiveCust(Integer modetype, BillItemVO item) {
    if (FModyType.DIRECTTRAN_MODETYPE.equalsValue(modetype)) {
      // 直运调拨时检查收货客户
      String receiverid = item.getCreceivecustid();
      if (PubAppTool.isNull(receiverid)) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("4009011_0", "04009011-0102")/*
                                                      * @res "直运调拨，收货客户不可为空。"
                                                      */);
      }
    }
  }

  private void checkRowNo(BillVO vo) {
    try {
      VORowNoUtils.validateRowNo(vo.getChildrenVO(), BillItemVO.CROWNO);
    }
    catch (ValidationException e) {
      ExceptionUtils.wrappException(e);
    }
  }

  private void checkSettleRule(BillVO vo) {
    // 组织间和组织内调拨订单不匹配调拨关系
    Integer ctypecode = vo.getParentVO().getFmodeflag();
    if (ctypecode != null
        && (TransMode.TRANSMODE_5E.equalsValue(ctypecode) || TransMode.TRANSMODE_5I
            .equalsValue(ctypecode))) {
      return;
    }

    boolean bSFDB = TransMode.TRANSMODE_5C.equalsValue(ctypecode);

    BillItemVO[] voaItem = vo.getChildrenVO();
    Set<String> setFor5D = new HashSet<String>();
    Set<String> setFor5C = new HashSet<String>();
    int len = voaItem.length;
    for (int i = 0; i < len; i++) {
      if (voaItem[i].getStatus() == VOStatus.DELETED) {
        continue;
      }

      // 赠品行不匹配调拨关系，不检查
      UFBoolean iolargessflag = voaItem[i].getBiolargessflag();
      UFBoolean otlargessflag = voaItem[i].getBotlargessflag();
      // 如果调拨关系表体的主键和表头不一致，就抛错，因为一张订单只可以匹配到一行调拨关系表头
      if (null == iolargessflag || !iolargessflag.booleanValue()) {
        String pk_transrela = voaItem[i].getCiosettleruleid();
        if (PubAppTool.isNull(pk_transrela)) {
          ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl
              .getNCLangRes().getStrByID("4009011_0", "04009011-0103")/*
                                                                       * @res
                                                                       * "没有匹配到调入调出内部结算规则。"
                                                                       */);
        }
        setFor5D.add(pk_transrela);
      }

      if (bSFDB) {
        if (null == otlargessflag || !otlargessflag.booleanValue()) {
          String pk_transrela = voaItem[i].getCotsettleruleid();
          if (PubAppTool.isNull(pk_transrela)) {
            ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl
                .getNCLangRes().getStrByID("4009011_0", "04009011-0104")/*
                                                                         * @res
                                                                         * "没有匹配到调出出货内部结算规则。"
                                                                         */);
          }
          setFor5C.add(pk_transrela);
        }
      }
    }

    if (setFor5D.size() > 1 || setFor5C.size() > 1) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4009011_0", "04009011-0105")/*
                                                    * @res
                                                    * "表体行调拨关系不一致！请检查调拨关系定义，确定为所有存货定义了明细的调拨关系!"
                                                    */);
    }
  }

  /**
   * 校验财务组织间税码、扣税类别、税率必输
   * 
   * @param vo
   */
  private void checkTaxCode(BillVO vo) {
    BillHeaderVO head = vo.getParentVO();
    BillItemVO[] items = vo.getChildrenVO();
    Integer fModeFlag = head.getFmodeflag();
    if (!TransMode.TRANSMODE_5D.equalsValue(fModeFlag)) {
      return;
    }
    for (BillItemVO item : items) {
      if (PubAppTool.isNull(item.getCtaxcodeid())) {

        String msg =
            NCLangRes4VoTransl.getNCLangRes().getStrByID("4009011_0",
                "04009011-0332")/*
                                 * @res "财务组织间税码不能为空"
                                 */;
        ExceptionUtils.wrappBusinessException(msg);

      }
      if (item.getFtaxtypeflag() == null) {
        String msg =
            NCLangRes4VoTransl.getNCLangRes().getStrByID("4009011_0",
                "04009011-0333")/*
                                 * @res "财务组织间扣税类别不能为空"
                                 */;
        ExceptionUtils.wrappBusinessException(msg);
      }
      if (item.getNtaxrate() == null) {
        String msg =
            NCLangRes4VoTransl.getNCLangRes().getStrByID("4009011_0",
                "04009011-0334")/*
                                 * @res "财务组织间税率不能为空"
                                 */;
        ExceptionUtils.wrappBusinessException(msg);
      }

    }
  }

  private void checkTransType(Integer modetype, BillItemVO item,
      Map<String, UFBoolean> mapSODirect, Map<String, UFBoolean> map5ADirect) {
    boolean isDriect = false;
    // 只有来源直运销售订单的调拨订单，调拨方式才可为直运
    String srcBillType = item.getVsrctype();
    if (SOBillType.Order.getCode().equals(srcBillType)) {
      UFBoolean isSoDirect = mapSODirect.get(item.getVsrctrantype());
      isDriect = isSoDirect.booleanValue();
    }
    else if (TOBillType.TransIn.getCode().equals(srcBillType)) {
      UFBoolean isSoDirect = map5ADirect.get(item.getVsrctrantype());
      isDriect = isSoDirect.booleanValue();
    }

    if (isDriect) {
      if (!FModyType.DIRECTTRAN_MODETYPE.equalsValue(modetype)) {
        String message =
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009011_0",
                "04009011-0106")/* @res "来源直运的调拨订单，交易类型属性应为直运。" */;
        ExceptionUtils.wrappBusinessException(message);
      }
    }
    else {
      if (FModyType.DIRECTTRAN_MODETYPE.equalsValue(modetype)) {
        String message =
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009011_0",
                "04009011-0107")/* @res "非直运的调拨订单，交易类型属性不可为直运。" */;
        ExceptionUtils.wrappBusinessException(message);
      }
    }
  }

  private Map<String, UFBoolean> getIs5ADirect(BillItemVO[] items) {
    Set<String> set = new HashSet<String>();
    for (BillItemVO item : items) {
      String vsrctype = item.getVsrctype();
      if (TOBillType.TransIn.getCode().equals(vsrctype)) {
        set.add(item.getVsrctrantype());
      }
    }
    Map<String, UFBoolean> map = null;
    if (set.size() > 0) {
      // 调用调入申请判断调入申请是否直运
      IM5aTranTypeService service =
          NCLocator.getInstance().lookup(IM5aTranTypeService.class);
      try {
        map = service.queryIsM5ADriect(set.toArray(new String[0]));
      }
      catch (BusinessException e) {
        ExceptionUtils.wrappException(e);
      }
    }
    if (null == map) {
      map = new HashMap<String, UFBoolean>();
    }
    return map;
  }

  private Map<String, UFBoolean> getIsSODirect(BillItemVO[] items) {
    Set<String> set = new HashSet<String>();
    for (BillItemVO item : items) {
      String vsrctype = item.getVsrctype();
      if (SOBillType.Order.getCode().equals(vsrctype)) {
        set.add(item.getVsrctrantype());
      }
    }
    Map<String, UFBoolean> map = null;
    if (set.size() > 0) {
      // 调用销售接口判断销售订单是否直运
      ISaleOrderForTO service =
          NCLocator.getInstance().lookup(ISaleOrderForTO.class);
      try {
        map = service.queryIsDirectTO(set.toArray(new String[0]));
      }
      catch (BusinessException e) {
        ExceptionUtils.wrappException(e);
      }
    }
    if (null == map) {
      map = new HashMap<String, UFBoolean>();
    }
    return map;
  }

  @Override
  public void process(BillVO[] vos) {
    for (BillVO vo : vos) {
      this.checkDriect(vo);
      this.checkMustInput(vo);
      this.checkDate(vo);
      this.checkInvAlreadyAllot(vo);
      this.checkInvAttribute(vo);
      this.checkSettleRule(vo);

      this.checkOuterConsign(vo);

      this.checkCurrChangeRate(vo);

      this.checkRowNo(vo);
      this.checkLimitValue(vo);
      this.checkCustSup(vo);
      this.check5CFinanceOrg(vo);
      this.checkTaxCode(vo);
      this.checkNumPriceMny(vo);
      this.checkCostRegionFor5C(vo);
    }
  }

  /**
   * 方法功能描述：查询调出方是否有内部客户。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param inFinanceOrgID
   *          <p>
   * @author lixlc
   * @time 2010-7-2 下午02:53:03
   */
  private void queryCustomer(String[] inFinanceOrgID) {
    Map<String, String> cust =
        CustomerPubService.queryCusPkByOrgPk(inFinanceOrgID);
    Map<String, OrgVO> orgMap = this.getOrgMap(inFinanceOrgID);
    for (String id : inFinanceOrgID) {
      String custID = cust.get(id);
      if (null == custID) {
        String msg =
            NCLangResOnserver.getInstance().getStrByID("4009011_0",
                "04009011-0109", null, new String[] {
                  orgMap.get(id).getCode()
                });/*财务组织编码为{0}没有定义内部客户！*/

        ExceptionUtils.wrappBusinessException(msg);
      }
    }
  }

  /**
   * 方法功能描述：查询调出方是否有内部供应商。
   * <p>
   * <b>参数说明</b>
   * 
   * @param outFinanceOrgID
   *          <p>
   * @author lixlc
   * @time 2010-7-2 下午02:07:20
   */
  private void querySupplier(String[] outFinanceOrgID) {
    Map<String, String> supp =
        SupplierPubService.querySupPkByOrgPk(outFinanceOrgID);

    Map<String, OrgVO> orgMap = this.getOrgMap(outFinanceOrgID);

    for (String id : outFinanceOrgID) {
      if (null == supp || !supp.containsKey(id)) {
        String msg =
            NCLangResOnserver.getInstance().getStrByID("4009011_0",
                "04009011-0110", null, new String[] {
                  orgMap.get(id).getCode()
                });/*财务组织编码为{0}没有定义内部客商！*/

        ExceptionUtils.wrappBusinessException(msg);
      }
    }
  }

  private Map<String, OrgVO> getOrgMap(String[] orgpks) {

    OrgVO[] orgvos = OrgUnitPubService.getOrgsByPks(orgpks, new String[] {
      OrgVO.PK_ORG, OrgVO.CODE, OrgVO.NAME
    });

    Map<String, OrgVO> orgMap = new HashMap<String, OrgVO>();
    for (OrgVO vo : orgvos) {
      orgMap.put(vo.getPk_org(), vo);
    }
    return orgMap;
  }

  /**
   * 检查税率取值范围为0-100
   * 
   * @param billVO 调拨订单VO
   * 
   */
  private void checkNumPriceMny(BillVO billVO) {
    BillItemVO[] items = billVO.getChildrenVO();
    for (BillItemVO item : items) {
      UFDouble ntaxrate = item.getNtaxrate();
      if (MathTool.lessThan(ntaxrate, UFDouble.ZERO_DBL)
          || MathTool.lessThan(new UFDouble(100), ntaxrate)) {
        String msg =
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009011_0",
                "04009011-0335")/* @res "税率的取值范围必须是0-100" */;
        ExceptionUtils.wrappBusinessException(msg);
      }
    }
  }

  /**
   * 三方调拨时，如果成本域是库存组织+仓库层级，则调出仓库不能为空
   * 
   * @param billVO 调拨订单VO
   * 
   */
  private void checkCostRegionFor5C(BillVO billVO) {
    boolean isIAEnabled = SysInitGroupQuery.isIAEnabled();
    if (!isIAEnabled) {
      return;
    }
    boolean is5C =
        TransMode.TRANSMODE_5C.equalsValue(billVO.getParentVO().getFmodeflag());
    if (is5C) {
      BillItemVO[] items = billVO.getChildrenVO();
      StringBuffer buffer = new StringBuffer();
      for (BillItemVO item : items) {
        if (PubAppTool.isNull(item.getCoutstordocid())) {
          buffer.append(item.getCrowno());
          buffer.append(" ");
        }
      }
      if (buffer.length() == 0) {
        return;
      }
      else if (buffer.length() > 0) {
        String stockorgid = billVO.getParentVO().getPk_org();
        Map<String, String> mapstock =
            CostRegionPubService.getCostRegionMapByStockOrgIDS(new String[] {
              stockorgid
            });
        String CostRegion = mapstock.get(stockorgid);
        if (PubAppTool.isNull(CostRegion)) {
          String financeorgid = billVO.getParentVO().getCoutfinanceorgid();
          Map<String, String> mapfi =
              CostRegionPubService
                  .getCostRegionMapByFinanceOrgIDS(new String[] {
                    financeorgid
                  });
          CostRegion = mapfi.get(financeorgid);
        }

        if (PubAppTool.isNull(CostRegion)) {
          String msg =
              NCLangResOnserver.getInstance().getStrByID("4009011_0",
                  "04009011-0357", null, new String[] {
                    billVO.getParentVO().getVbillcode(), buffer.toString()
                  });/*单据号为{0}表体行号为{1}的调拨订单，三方调拨时，如果成本域是库存组织+仓库层级，调出仓库不允许为空！*/
          ExceptionUtils.wrappBusinessException(msg);
        }
      }
    }
    else {
      return;
    }
  }
}
