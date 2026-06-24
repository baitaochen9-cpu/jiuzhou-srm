package nc.itf.scmpub.reference.uap.group;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.res.NCModule;

import nc.pubitf.initgroup.InitGroupQuery;

import nc.bs.framework.common.InvocationInfoProxy;

/**
 * 判断模块是否启用
 * 
 * @since 6.0
 * @version 2011-6-18 上午11:39:23
 * @author 钟鸣
 */
public class SysInitGroupQuery {

  private static final String SCM_BC_CODE = "4017";

  private static final String SCM_CMP_CODE = "3607";

  private static final String UAP_BARCODE = "1057";

  /**
   * SN模块编码
   */
  private static final String SCM_SN_CODE = "4016";

  /**
   * 判断项目合同管理模块是否启用
   * 
   * @return boolean
   */
  public static boolean isPCMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(), "4820");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：营销费用
   * 
   * @return boolean
   */
  public static boolean isMeEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(), "4038");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断资产信息管理模块是否启用
   * 
   * @return
   */
  public static boolean isAIMEnabled() {
    try {
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), NCModule.AIM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断资产租赁管理模块是否启用
   * 
   * @return
   */
  public static boolean isALMEnabled() {
    try {
      // TODO 常量待定义
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), /* NCModule.ALM.getCode() */"4530");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 资产管理
   * 
   * @return boolean
   */
  public static boolean isAMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.AM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 财务模块：应付管理
   * 
   * @return boolean
   */
  public static boolean isAPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.AP.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 财务模块：应收管理
   * 
   * @return boolean
   */
  public static boolean isAREnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.AR.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断资产使用管理模块是否启用
   * 
   * @return
   */
  public static boolean isAUMEnabled() {
    try {
      // TODO 常量待定义
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), /* NCModule.AUM.getCode() */"4520");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：条码管理
   * 
   * @return boolean
   */
  public static boolean isBCEnabled() {
    try {
      // TODO NCModule中还没有条码这个模块这里先写死了
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          SysInitGroupQuery.SCM_BC_CODE);
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * UAP模块：条码管理
   * 
   * @return boolean
   */
  public static boolean isUAPBarCodeEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          SysInitGroupQuery.UAP_BARCODE);
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：序列号管理
   * 
   * @return true if sn is enable
   */
  public static boolean isSNEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          SysInitGroupQuery.SCM_SN_CODE);
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：销售返利
   * 
   * @return boolean
   */
  public static boolean isBRMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.BRM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 战略管理模块：生产成本管理
   * 
   * @return boolean
   */
  public static boolean isCMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.CM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 资金管理模块：现金管理
   * 
   * @return boolean
   */
  public static boolean isCMPEnabled() {
    try {
      // TODO 常量待定义
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          SysInitGroupQuery.SCM_CMP_CODE);
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：销售信用
   * 
   * @return boolean
   */
  public static boolean isCREDITEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.CREDIT.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：合同管理
   * 
   * @return boolean
   */
  public static boolean isCTEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.CT.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：运输管理
   * 
   * @return boolean
   */
  public static boolean isDMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.DM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断 维护管理模块是否启用
   * 
   * @return
   */
  public static boolean isEMMEnabled() {
    try {
      // TODO 常量待定义
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), /* NCModule.EMM.getCode() */"4550");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断某个模块在当前集团是否启用
   * 
   * @param module
   *          NC模块
   * @return
   */
  public static boolean isEnable(NCModule module) {
    boolean flag = false;
    String pk_group = SysInitGroupQuery.getOrgPk();
    try {
      flag = InitGroupQuery.isEnabled(pk_group, module.getCode());
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    return flag;
  }

  /**
   * 转口贸易
   * 
   * @return boolean
   */
  public static boolean isENTEnabled() {
    try {
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), "4131");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断运行管理模块是否启用
   * 
   * @return
   */
  public static boolean isEOMEnabled() {
    try {
      // TODO 常量待定义
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), /* NCModule.EOM.getCode() */"4540");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 出口管理(自营出口)
   * 
   * @return boolean
   */
  public static boolean isETEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(), "4121");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 出口管理(代理出口)
   * 
   * @return boolean
   */
  public static boolean isETPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(), "4126");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断维修管理模块是否启用
   * 
   * @return
   */
  public static boolean isEWMEnabled() {
    try {
      // TODO 常量待定义
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), /* NCModule.EWM.getCode() */"4560");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：固定资产
   * 
   * @return boolean
   */
  public static boolean isFAEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.FA.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 企业建模：会计平台
   * 
   * @return boolean
   */
  public static boolean isFIPEnabled() {
    return SysInitGroupQuery.isEnable(NCModule.FIP);
  }

  /**
   * 财务总帐是否在本集团已经启用
   * 
   * @return 总帐模块启用返回真
   */
  public static boolean isGLEnabled() {
    return SysInitGroupQuery.isEnable(NCModule.GL);
  }

  /**
   * 财务模块：存货核算
   * 
   * @return boolean
   */
  public static boolean isIAEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.IA.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 利润中心存货核算
   * 
   * @return boolean
   */
  public static boolean isPCIAEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PCIA.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 利润中心产品成本
   * 
   * @return boolean
   */
  public static boolean isPCCMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(), "3821");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 利润中心内部交易
   * 
   * @return boolean
   */
  public static boolean isPCTOEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PCTO.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：库存管理
   * 
   * @return boolean
   */
  public static boolean isICEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.IC.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：库存计划
   * 
   * @return boolean
   */
  public static boolean isINVPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.INVP.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 进口管理(自营进口)
   * 
   * @return boolean
   */
  public static boolean isITEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(), "4111");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 进口管理(代理进口)
   * 
   * @return boolean
   */
  public static boolean isITPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(), "4116");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 离散生产任务管理模块
   * 
   * @return boolean
   */
  public static boolean isMMDPACEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.MMDPAC.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 需求管理模块
   * 
   * @return boolean
   */
  public static boolean isMMDPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.MMDP.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 生产制造模块
   * 
   * @return boolean
   */
  public static boolean isMMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.MM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 生产任务管理模块
   * 
   * @return boolean
   */
  public static boolean isMMPACEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.MMPAC.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 流程生产任务管理模块
   * 
   * @return boolean
   */
  public static boolean isMMPPACEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.MMPPAC.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：采购计划
   * 
   * @return boolean
   */
  public static boolean isMPPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.MPP.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 订单中心
   * 
   * @return boolean
   */
  public static boolean isOPCEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.OPC.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 项目管理模块：项目综合管理
   * 
   * @return boolean
   */
  public static boolean isPIMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PIM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：采购管理
   * 
   * @return boolean
   */
  public static boolean isPOEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PO.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：采购价格
   * 
   * @return boolean
   */
  public static boolean isPPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PURP.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：销售价保
   * 
   * @return boolean
   */
  public static boolean isPPMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PPM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：销售价格
   * 
   * @return boolean
   */
  public static boolean isPRICEEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PRICE.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 付款排程
   * 
   * @return boolean
   */
  public static boolean isPSEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PS.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /********************** 战略管理 ********************/

  /**
   * 供应链模块：销售计划
   * 
   * @return boolean
   */
  public static boolean isPSPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.PSP.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /********************** 财务 ********************/

  /**
   * 质量管理
   * 
   * @return boolean
   */
  public static boolean isQCEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.QC.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断周转材租入管理模块是否启用
   * 
   * @return
   */
  public static boolean isRLMEnabled() {
    try {
      // TODO 常量待定义
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), /* NCModule.RLM.getCode() */"4585");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 判断周转材租出管理模块是否启用
   * 
   * @return
   */
  public static boolean isROMEnabled() {
    try {
      // TODO 常量待定义
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), /* NCModule.ROM.getCode() */"4583");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /********************** 资金管理 ********************/

  /**
   * 判断易耗品管理模块是否启用
   * 
   * @return
   */
  public static boolean isRUMEnabled() {
    try {
      return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
          .getGroupId(), NCModule.RUM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /********************** 资产管理 ********************/

  /**
   * 供应链模块：委外加工
   * 
   * @return boolean
   */
  public static boolean isSCEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.SC.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /********************** 生产制造 ********************/

  /**
   * 供应链模块
   * 
   * @return boolean
   */
  public static boolean isSCMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.SCM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：供应链基础设置
   * 
   * @return boolean
   */
  public static boolean isSCMFEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.SCMF.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：供应链管理报表
   * 
   * @return boolean
   */
  public static boolean isSCMMREnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.SCMMR.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：销售管理
   * 
   * @return boolean
   */
  public static boolean isSOEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.SO.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：销售返利
   * 
   * @return boolean
   */
  public static boolean isSREnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(), "4036");// NCModule.SR.getCode()
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 资金管理模块：内部帐户管理
   * 
   * @return boolean
   */
  public static boolean isTAMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.TAM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 资金管理
   * 
   * @return boolean
   */
  public static boolean isTMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.TM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：内部交易
   * 
   * @return boolean
   */
  public static boolean isTOEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.TO.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：U8零售接口
   * 
   * @return boolean
   */
  public static boolean isU8RMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.U8RM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  /**
   * 供应链模块：供应商管理
   * 
   * @return boolean
   */
  public static boolean isVRMEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          NCModule.VRM.getCode());
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }
  
  // add by wangceb 
  /**
   * 财务共享-报账平台
   * 
   * @return boolean
   */
  public static boolean isSSCRPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          "1056");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }
  
  /**
   * 财务共享-作业平台
   * 
   * @return boolean
   */
  public static boolean isSSCTPEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          "7010");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }
  

  /**
   * 财务共享-发票管理
   * 
   * @return boolean
   */
  public static boolean isSSCINVEnabled() {
    try {
      return InitGroupQuery.isEnabled(SysInitGroupQuery.getOrgPk(),
          "1058");
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
    return false;
  }

  private static String getOrgPk() {
    return InvocationInfoProxy.getInstance().getGroupId();
  }

}
