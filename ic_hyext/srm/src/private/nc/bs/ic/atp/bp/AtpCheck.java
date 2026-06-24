package nc.bs.ic.atp.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.atp.pub.ATPLockCtrl;
import nc.bs.ic.atp.pub.ATPMultLangRes;
import nc.bs.ic.pub.db.ICDBVisitor;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.pubapp.env.BSContext;
import nc.itf.bd.userdefitem.IUserdefitemQryService;
import nc.itf.fi.pub.SysInit;
import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.md.data.access.NCObject;
import nc.pubitf.ic.query.CommonQuery;
import nc.vo.bd.accessor.IBDData;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.ic.atp.entity.AtpVO;
import nc.vo.ic.atp.pub.AtpVOUtil;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.material.define.InvCalBodyVO;
import nc.vo.ic.param.CheckTypeEnum;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.warehouse.WhVO;
import nc.vo.ml.MultiLangUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.JavaType;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.AssertUtils;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.scmpub.exp.AtpNotEnoughException;
import nc.vo.scmpub.util.TimeUtils;
/**
 * <p>
 * <b>可用量检查</b>
 * <p>
 *
 * @version v60
 * @since v60
 * @author liuzy
 * @time 2010-6-11 上午11:07:31
 */
public class AtpCheck {

  public final static String AtpCheckFlag = "ATPCheck";
  
  public final static String AtpNotCheckOrgs = "AtpNotCheckOrgs";

  /** 物料辅助属性自定义项规则编码 */
  private static final String MARASSISTANTRULECODE = "materialassistant";

  private AtpVO[] checkvos;

  private ICBSContext context = new ICBSContext();

  // 检查方式:key---库存组织oid
  private Map<String, CheckTypeEnum> mapCheckType =
      new HashMap<String, CheckTypeEnum>();

  // 展望天数:key---库存组织oid
  private Map<String, Integer> mapIC022 = new HashMap<String, Integer>();

  // 物料库存页签 key--pk_org+pk_materailvid
  private Map<String, InvCalBodyVO> mapInvCalVO =
      new HashMap<String, InvCalBodyVO>();

  // 是否劳务或折扣类物料 key--pk_materailvid
  private Map<String, UFBoolean> mapLaborProp =
      new HashMap<String, UFBoolean>();

  // 仓库是否影响可用量 key--cwarehouseid
  private Map<String, UFBoolean> mapWhProp = new HashMap<String, UFBoolean>();

  // 物料启用的自由辅助属性
  private Map<String, UserdefitemVO> mapVfree =
      new HashMap<String, UserdefitemVO>();
  
  /**
   * AtpCheck 的构造子
   */
  public AtpCheck() {
    super();
  }

  /**
   *
   */
  public static void abandonATPCheck() {
    new ICBSContext().setSession(AtpCheck.AtpCheckFlag, UFBoolean.FALSE);
  }
  
  /**
   * 此方法谨慎使用
   * 设置哪些组织不需要做可用量检查
   * 场景1：内部交易单边交易场景，调入组织不需要做检查
   */
  public static void abandonATPCheckByOrgs(Set<String> pk_orgs) {
    new ICBSContext().setSession(AtpCheck.AtpNotCheckOrgs, pk_orgs);
  }

  private Set<String> getabandonATPCheckOrgs() {
    return (Set<String>) new ICBSContext().getSession(AtpCheck.AtpNotCheckOrgs);
  }

  private AtpVO[] filterByOrgs(AtpVO[] atpvos) {
    Set<String> abandonCheckOrgs = this.getabandonATPCheckOrgs();
    if (ValueCheckUtil.isNullORZeroLength(abandonCheckOrgs)) {
      return atpvos;
    }

    List<AtpVO> relist = new ArrayList<AtpVO>();
    for (AtpVO atpVO : atpvos) {
      if (!abandonCheckOrgs.contains(atpVO.getPk_org())) {
        relist.add(atpVO);
      }
    }
    return CollectionUtils.listToArray(relist);
  }

  /**
   *
   */
  public void checkAtp(AtpVO[] atpvos) throws BusinessException {
    this.checkvos = this.filterByOrgs(atpvos);
    this.processAtpVO();
    if (ValueCheckUtil.isNullORZeroLength(this.checkvos)) {
      return;
    }
    new ATPLockCtrl().lockAtpDimAtDB(this.checkvos);
    // Map<String,AtpVO> mapatpvo = queryAtpVO();
    // Map<String,OnhandVO> maphandvo = queryOnhand();
    // processResults(mapatpvo,maphandvo);
    AtpDetailQuery atpquery = new AtpDetailQuery();
    this.checkvos = atpquery.queryAtp(this.checkvos, this.mapInvCalVO);
    this.checkResults();
  }

  /**
   *
   */
  private void checkResults() throws BusinessException {
    if (ValueCheckUtil.isNullORZeroLength(this.checkvos)) {
      return;
    }
    List<AtpVO> lerrvo = new ArrayList<AtpVO>();
    for (AtpVO atpvo : this.checkvos) {
      // atpvo.setNatpnum(AtpVOUtil.calcAtpByScheme(atpvo));
      if (NCBaseTypeUtils.isLtZero(atpvo.getNatpnum())) {
        lerrvo.add(atpvo);
      }
    }
    if (lerrvo.size() <= 0) {
      return;
    }
    // 用户是否忽略该检查
    nc.vo.ic.pub.pf.ICPFParameter param = new ICBSContext().getICPFParameter();
    
    // 兼容scmpub交互异常框架
    if (param == null
        && BSContext.getInstance().getSession(AtpCheckFlag) != null) {
      UFBoolean checkresult =
          (UFBoolean) BSContext.getInstance().getSession(AtpCheckFlag);
      if (!checkresult.booleanValue()) {
        return;
      }
    }
    
    if (param != null && param.isBAtpCheckFlag()) {
      return;
    }
    this.initMapVfree();
    this.processHint(lerrvo.toArray(new AtpVO[lerrvo.size()]));
    this.processErro(lerrvo.toArray(new AtpVO[lerrvo.size()]));
  }


  private void processHint(AtpVO[] atpvos) throws BusinessException{

    StringBuilder str = new StringBuilder();
    Set<String> setMsg = new HashSet<String>();
    for(AtpVO vo:atpvos){
      if (!this.getCheckType(vo).equals(CheckTypeEnum.Hint)) {
        continue;
      }
      String msg = this.getCheckMsg(vo);
      if (setMsg.contains(msg)) continue;
      setMsg.add(msg);
      str.append(msg);
    }
    if(StringUtil.isSEmptyOrNull(str.toString())){
      return;
    }
    StringBuilder sf =
            new StringBuilder(NCLangResOnserver.getInstance().getStrByID(
                "4008021_0", "04008021-0020")/* 下列物料可用量不足: */);
          sf.append("\n");
          sf.append(str);
          sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0",
            "04008021-0026")/* 是否继续？ */);
    throw new AtpNotEnoughException(sf.toString());

  }

  /**
   * 获取物料最新版本id,暂时这样处理,最好适配到基础访问层
   */
  private void fillLastMaterailVid() {
    Map<String, Object> mapvid =
        new ICDBVisitor().getValue(new MaterialVO().getTableName(),
            MaterialVO.PK_MATERIAL, JavaType.String, MaterialVO.PK_SOURCE,
            VOEntityUtil.getVOsValues(this.checkvos, AtpVO.CMATERIALOID,
                String.class), MaterialVO.LATEST + "='Y' ");
    AssertUtils.assertValue(mapvid != null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008021_0","04008021-0007")/*@res "获取物料最新版本错"*/);
    if (mapvid == null) {
      return;
    }
    String vid = null;
    for (AtpVO atpvo : this.checkvos) {
      vid = (String) mapvid.get(atpvo.getCmaterialoid());
      AssertUtils.assertValue(vid != null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008021_0","04008021-0007")/*@res "获取物料最新版本错"*/);
      atpvo.setCmaterialvid(vid);
    }
  }

  /**
   * 方法功能描述：获取提示信息
   * <p>
   * <b>参数说明</b>
   *
   * @param atpvo
   * @return <p>
   * @since 6.0
   * @author liuzy
   * @time 2010-8-5 下午08:13:32
   */
  private String getCheckMsg(AtpVO atpvo) {
    StringBuilder sf = new StringBuilder();
//    NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0020")/*下列物料可用量不足:*/
    sf.append("\n");
    if (atpvo.getPk_org() != null) {
    	sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0021", null, new String[]{CommonQuery.getInstance().getOrgDocbyPks(new String[]{atpvo.getPk_org()})[0].getName().toString()})/*库存组织{0},*/);
    }
    if (atpvo.getCwarehouseid() != null) {
      sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0022", null, new String[]{CommonQuery.getInstance().getWarehouseDocbyPks(new String[]{atpvo.getCwarehouseid()})[0].getName().toString()})/*仓库{0},*/);
    }
    MaterialVO materialVO = getMaterialVOByPK(atpvo.getCmaterialvid());
    sf.append(" ");
    sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0023", null, new String[]{getMatieralInfoMess(materialVO)})/*物料：{0}*/);
    if (atpvo.getVbatchcode() != null) {
      sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0024", null, new String[]{atpvo.getVbatchcode()})/*-批次 {0}-*/);
    }
    sf.append("[" + atpvo.getDplandate() + "]");
    // 可用量精度处理
    UFDouble natpnum = ScaleUtils.getScaleUtilAtBS().adjustNumScale(atpvo.getNatpnum(), materialVO.getPk_measdoc());
    sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0025", null, new String[]{natpnum.toString()})/*可用量：{0}*/);
    sf.append("\n");
//    sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0026")/*是否继续？*/);
    return sf.toString();
  }
  
  private String getMatieralInfoMess(MaterialVO materialVO) {
    String codeTab =
        NCLangResOnserver.getInstance()
            .getStrByID("4008021_0", "04008021-0045")/* 编码: */;
    String specTab =
        NCLangResOnserver.getInstance()
            .getStrByID("4008021_0", "04008021-0046")/* 规格: */;
    String typeTab =
        NCLangResOnserver.getInstance()
            .getStrByID("4008021_0", "04008021-0047")/* 型号: */;

    StringBuilder mess = new StringBuilder();
    mess.append(materialVO.getName());
    mess.append("[");
    mess.append(codeTab);
    mess.append(materialVO.getCode());
    mess.append(",");
    if (materialVO.getMaterialspec() != null) {
      mess.append(" ");
      mess.append(specTab);
      mess.append(materialVO.getMaterialspec());
      mess.append(",");
    }
    if (materialVO.getMaterialtype() != null) {
      mess.append(" ");
      mess.append(typeTab);
      mess.append(materialVO.getMaterialtype());
      mess.append(",");
    }
    mess.deleteCharAt(mess.length() - 1);
    mess.append("]");
    return mess.toString();
  }
  
  private MaterialVO getMaterialVOByPK(String cinv_vid) {
    if (cinv_vid == null) {
      return null;
    }

    MaterialVO[] materialVOs =
        MaterialPubService.queryMaterialBaseInfoByPks(new String[] {
          cinv_vid
        }, new String[] {
          MaterialVO.NAME, MaterialVO.CODE, MaterialVO.MATERIALSPEC,
          MaterialVO.MATERIALTYPE
        });

    if (materialVOs == null || materialVOs.length <= 0) {
      return null;
    }

    return materialVOs[0];
  }

  /*
   * 获得展望量检查方式
   */
  private CheckTypeEnum getCheckType(AtpVO vo) {
    if (!this.mapCheckType.containsKey(vo.getPk_org())) {
      CheckTypeEnum checkTypeEnum = null;
      try {
        checkTypeEnum = this.context.getICSysParam().getIC090(vo.getPk_org());
        if (checkTypeEnum == null) {
          checkTypeEnum = CheckTypeEnum.Check;
        }
        this.mapCheckType.put(vo.getPk_org(), checkTypeEnum);
      }
      catch (Exception e) {
        ExceptionUtils.wrappException(e);
      }
    }
    return this.mapCheckType.get(vo.getPk_org());
  }

  /**
   *
   */
  private void initMapInvCalVO() throws BusinessException {
    InvCalBodyVO[] invcalvos =
        this.context.getInvInfo()
            .getInvCalBodyVO(
                VOEntityUtil.getVOsValues(this.checkvos, AtpVO.PK_ORG,
                    String.class),
                VOEntityUtil.getVOsValues(this.checkvos, AtpVO.CMATERIALVID,
                    String.class));
    if (ValueCheckUtil.isNullORZeroLength(invcalvos)) {
      throw new BusinessException(ATPMultLangRes.getInvCalAllocErr());
    }
    for (InvCalBodyVO invcalvo : invcalvos) {
      if (invcalvo == null) {
        throw new BusinessException(ATPMultLangRes.getInvCalAllocErr());
      }
      this.mapInvCalVO.put(invcalvo.getPk_org() + invcalvo.getPk_material(),
          invcalvo);
    }
  }

  /**
   *
   */
  private void initMapLaborProp() {
    InvBasVO[] basvos =
        this.context.getInvInfo().getInvBasVO(
            VOEntityUtil.getVOsValues(this.checkvos, AtpVO.CMATERIALVID,
                String.class));
    for (InvBasVO basvo : basvos) {
      if (basvo.getFee() != null && basvo.getFee().booleanValue()) {
        this.mapLaborProp.put(basvo.getPk_material(), UFBoolean.TRUE);
        continue;
      }
      if (basvo.getDiscountflag() != null
          && basvo.getDiscountflag().booleanValue()) {
        this.mapLaborProp.put(basvo.getPk_material(), UFBoolean.TRUE);
        continue;
      }
      this.mapLaborProp.put(basvo.getPk_material(), UFBoolean.FALSE);
    }
  }

  /**
   *
   */
  private boolean isWarehouseCheck(AtpVO vo) {
    if (vo.getCwarehouseid() == null) {
      return true;
    }
    if (!this.mapWhProp.containsKey(vo.getCwarehouseid())) {
      UFBoolean bret = UFBoolean.FALSE;
      try {
        WhVO wvo =
            this.context.getWarehouseInfo().getWhVO(vo.getCwarehouseid());
        if (wvo != null) {
          bret =
              wvo.getIsatpaffected() == null ? UFBoolean.FALSE : wvo
                  .getIsatpaffected();
        }
        this.mapWhProp.put(vo.getCwarehouseid(), bret);
      }
      catch (Exception e) {
        ExceptionUtils.wrappException(e);
      }
    }
    return this.mapWhProp.get(vo.getCwarehouseid()).booleanValue();
  }
  
   /**
    * 设置不影响可用量的预计入、出单据数量为空
    * @param vo
    * @throws BusinessException
    */
  private void procNocalnumNull(AtpVO vo) throws BusinessException {
    InvCalBodyVO invcalvo = this.mapInvCalVO.get(vo.getPk_org()
        + vo.getCmaterialvid());
    if (invcalvo == null) {
      throw new BusinessException(ATPMultLangRes.getInvCalAllocErr());
    }
    vo.setCalcScheme(invcalvo);
    // 流程生产订单数量
    vo.setNmonum(!vo.getBcalcnmonum().booleanValue() ? null : vo
        .getNmonum());
    // 离散生产订单数量
    vo.setNdmonum(!vo.getBcalcndmonum().booleanValue() ? null : vo
        .getNdmonum());
    // 计划订单数量
    vo.setNmponum(!vo.getBcalcnmponum().booleanValue() ? null : vo
        .getNmponum());
    // 请购单数量
    vo.setNonrequirenum(!vo.getBcalcnonrequirenum().booleanValue() ? null
        : vo.getNonrequirenum());
    // 采购订单数量
    vo.setNonponum(!vo.getBcalcnonponum().booleanValue() ? null : vo
        .getNonponum());
    // 到货待检数量
    vo.setNonreceivenum(!vo.getBcalcnonreceivenum().booleanValue() ? null
        : vo.getNonreceivenum());
    // 调拨订单预计入及库存转库预计入
    vo.setNtraninnum(!vo.getBcalcntraninnum().booleanValue() ? null : vo
        .getNtraninnum());
    // 工单数量
    vo.setNonamnum(!vo.getBcalcnonamnum().booleanValue() ? null : vo
        .getNonamnum());
    // 发货数量
    vo.setNonreceiptnum(!vo.getBcalcnonreceiptnum().booleanValue() ? null
        : vo.getNonreceiptnum());
    // 销售订单数量
    vo.setNonsonum(!vo.getBcalcnonsonum().booleanValue() ? null : vo
        .getNonsonum());
    // 调拨申请
    vo.setNontranspraynum(!vo.getBcalcnontranspraynum().booleanValue() ? null
        : vo.getNontranspraynum());
    // 委外订单数量
    vo.setNonwwnum(!vo.getBcalcnonwwnum().booleanValue() ? null : vo
        .getNonwwnum());
    // 备料计划数量
    vo.setNpickmnum(!vo.getBcalcnpickmnum().booleanValue() ? null : vo
        .getNpickmnum());
    // 调拨订单预计出
    vo.setNtranoutnum(!vo.getBcalcntranoutnum().booleanValue() ? null : vo
        .getNtranoutnum());
    // 借入量
    vo.setNborrownum(!vo.getBcalcnborrownum().booleanValue() ? null : vo
        .getNborrownum());
    // 冻结量
    vo.setNfreezenum(!vo.getBcalcnfreezenum().booleanValue() ? null : vo
        .getNfreezenum());
    // 出库申请影响可用量
    vo.setNsapplynum(!vo.getBcalnsapplynum().booleanValue() ? null : vo
        .getNsapplynum());
    // 费用申请影响可用量
    vo.setNexpsapplynum(!vo.getBcalcnexpsapplynum().booleanValue() ? null
        : vo.getNexpsapplynum());
  }

  /**
   *
   */
  private void processAtpVO() throws BusinessException {
    UFBoolean checkFlag =
        (UFBoolean) this.context.getSession(AtpCheck.AtpCheckFlag);
    if (checkFlag != null && !checkFlag.booleanValue()) {
      this.checkvos = null;
      return;
    }
    InvCalBodyVO invcalvo = null;
    List<AtpVO> latp = new ArrayList<AtpVO>();
    boolean binitinv = false;
    UFBoolean blabor = null;
    
    String isTO18 = "不跳过";
    String isTO19 = "不跳过";
    

    
    for (AtpVO vo : this.checkvos) {
      if (!binitinv) {
        this.fillLastMaterailVid();
    
        isTO18 = SysInit.getParaString(vo.getPk_org(), "TO18");
        isTO19 = SysInit.getParaString(vo.getPk_org(), "TO19");
        if(("不跳过".equals(isTO18) || "不跳过".equals(isTO19))){
        	this.initMapLaborProp();
        	this.initMapInvCalVO();
        }
          binitinv = true;
      }
      if("不跳过".equals(isTO18) || "不跳过".equals(isTO19)){
    	  this.procNocalnumNull(vo);
      }
      // 如果对可用量贡献大于等于0 不检查
      if (VOEntityUtil.isGEZero(AtpVOUtil.calcAtp(vo))) {
        continue;
      }
      if (this.getCheckType(vo) == CheckTypeEnum.NotCheck) {
        continue;
      }
      if (!this.isWarehouseCheck(vo)) {
        vo.setCwarehouseid(null);
        continue;
      }
//      if (!binitinv) {
//        this.fillLastMaterailVid();
//        this.initMapLaborProp();
//        this.initMapInvCalVO();
//        binitinv = true;
//      }
      blabor = this.mapLaborProp.get(vo.getCmaterialvid());
      if (blabor != null && blabor.booleanValue()) {
        continue;
      }
      invcalvo = this.mapInvCalVO.get(vo.getPk_org() + vo.getCmaterialvid());
      if (invcalvo == null) {
        throw new BusinessException(ATPMultLangRes.getInvCalAllocErr());
      }
      if (invcalvo.getIsautoatpcheck() == null
          || !invcalvo.getIsautoatpcheck().booleanValue()) {
        continue;
      }
      if (vo.getDplandate() == null) {
        vo.setDplandate(TimeUtils.getEndDate(this.context.getServerDateTime()
            .getDate()));
      }
      vo.setScheme(invcalvo);
      latp.add(vo);
    }
    if (ValueCheckUtil.isNullORZeroLength(latp)) {
      this.checkvos = null;
    }
    else {
      this.checkvos = latp.toArray(new AtpVO[latp.size()]);
    }
  }
  

  /**
   *
   */
  private void processErro(AtpVO[] errvos) throws BusinessException {
//    String[] invvids = new String[errvos.length];
//    String[] wareids = new String[errvos.length];
//    for (int i = 0; i < errvos.length; i++) {
//      invvids[i] = errvos[i].getCmaterialvid();
//      wareids[i] = errvos[i].getCwarehouseid();
//    }
//    IBDData[] docinv = CommonQuery.getInstance().getMaterialDocbyPks(invvids);
//    IBDData[] docware = CommonQuery.getInstance().getWarehouseDocbyPks(wareids);
    StringBuilder sf = new StringBuilder(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008021_0","04008021-0008")/*@res "下列物料可用量不足:"*/);
    sf.append(":\n");
    Set<String> setMsg = new HashSet<String>();
    for (int i = 0; i < errvos.length; i++) {
        String msg = this.getCheckMsg(errvos[i]);
        if (setMsg.contains(msg)) continue;
        setMsg.add(msg);
        sf.append(msg);
//      sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0021", null, new String[]{})/*库存组织,*/);
//      if (errvos[i].getCwarehouseid() != null) {
//        sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0022", null, new String[]{docware[i].getName().toString()})/*仓库{0},*/);
//      }
//      sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0023", null, new String[]{docinv[i].getName().toString()})/*物料：{0}*/);
//      if (errvos[i].getVbatchcode() != null) {
//        sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0024", null, new String[]{errvos[i].getVbatchcode()})/*-批次 {0}-*/);
//      }
//      sf.append("[" + errvos[i].getDplandate() + "]");
//      sf.append(NCLangResOnserver.getInstance().getStrByID("4008021_0", "04008021-0025", null, new String[]{errvos[i].getNatpnum().toString()})/*可用量：{0}*/);
//      sf.append("\n");
    }
    throw new BusinessException(sf.toString());
  }

  /*
   * 得到展望期
   */
  protected UFDate getForeDate(UFDate plandate, String pk_org) {
    if (plandate == null) {
      return plandate;
    }
    Integer days = null;
    UFDate retdate = plandate;
    if (!this.mapIC022.containsKey(pk_org)) {
      try {
        days = this.context.getICSysParam().getIC022(pk_org);
        this.mapIC022.put(pk_org, days);
      }
      catch (Exception e) {
        ExceptionUtils.wrappException(e);
      }
    }
    days = this.mapIC022.get(pk_org);
    if (days != null) {
      retdate = TimeUtils.getEndDate(plandate.getDateAfter(days.intValue()));
    }
    return retdate;
  }

  // 处理自由辅助属性
  private String processVfreeErr(AtpVO errvo) {
    StringBuilder sf = new StringBuilder();
    if (errvo.getVfree1() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree1");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree1());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree1());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree2() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree2");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree2());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree2());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree3() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree3");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree3());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree3());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree4() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree4");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree4());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree4());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree5() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree5");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree5());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree5());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree6() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree6");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree6());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree6());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree7() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree7");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree7());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree7());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree8() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree8");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree8());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree8());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree9() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree9");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree9());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree9());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    if (errvo.getVfree10() != null) {
      UserdefitemVO defvo = this.mapVfree.get("vfree10");
      IBDData docdef =
          CommonQuery.getInstance().getDocByPk(defvo.getClassid(),
              errvo.getVfree10());
      sf.append(", " + this.getShowName(defvo) + ":");
      if (null == docdef) {
        sf.append(errvo.getVfree10());
      }
      else {
        sf.append(docdef.getName().toString());
      }
    }
    return sf.toString();
  }

  private void initMapVfree() {
    UserdefitemVO[] defs = this.queryMarAsstDefitem();
    if (ValueCheckUtil.isNullORZeroLength(defs))
      return;
    for (UserdefitemVO vo : defs) {
      this.mapVfree.put("vfree" + (vo.getPropindex().intValue() - 5), vo);
    }
  }
  /**
   * 根据环境语言返回相应语言的显示名称
   * 
   * @return 显示名称
   */
  private String getShowName(UserdefitemVO defVo) {
    NCObject ncObject = NCObject.newInstance(defVo);
    UserdefitemVO defVoNew = (UserdefitemVO) ncObject.getContainmentObject();
    int langIndex = MultiLangUtil.getCurrentLangSeq();
    String showName = "";
    switch (langIndex) {
      case 1:
        showName = defVoNew.getShowname();
        break;
      case 2:
        showName = defVoNew.getShowname2();
        break;
      case 3:
        showName = defVoNew.getShowname3();
        break;
      case 4:
        showName = defVoNew.getShowname4();
        break;
      case 5:
        showName = defVoNew.getShowname5();
        break;
      case 6:
        showName = defVoNew.getShowname6();
        break;
    }
    return showName;
  }
  @SuppressWarnings("null")
  private UserdefitemVO[] queryMarAsstDefitem() {
    UserdefitemVO[] defs = null;
    // 获取自定义项
    IUserdefitemQryService service =
        NCLocator.getInstance().lookup(IUserdefitemQryService.class);
    try {
      defs =
          service.queryUserdefitemVOsByUserdefruleCode(
              AtpCheck.MARASSISTANTRULECODE, AppContext.getInstance()
                  .getPkGroup());
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    List<UserdefitemVO> ldefs = new ArrayList<UserdefitemVO>();
    if (ValueCheckUtil.isNullORZeroLength(defs))
      return defs;
    for (UserdefitemVO vo : defs) {
      if (vo.getPropindex().intValue() > 5) {
        ldefs.add(vo);
      }
    }
    return ldefs.toArray(new UserdefitemVO[ldefs.size()]);

  }
}