/*LrQMk/CqhOuglVbPU1U6VUbrrcY2Ak4ECTIqvT4zA2M=*/
package nc.impl.to.m5x.action;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.scmpub.util.SCMBusinessLogUtil;
import nc.bs.to.m5x.plugin.ActionPlugInPoint;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.to.m5x.action.rule.approve.ApproveReserveRule;
import nc.impl.to.m5x.action.rule.approve.CheckEnableApproveRule;
import nc.impl.to.m5x.action.rule.approve.CheckPriceRule;
import nc.impl.to.m5x.action.rule.approve.FilterOrderByStatusRule;
import nc.impl.to.m5x.action.rule.approve.SendMsgOderToDeliveryRule;
import nc.impl.to.m5x.action.rule.approve.UpdatePflowVORule;
import nc.impl.to.m5x.action.rule.approve.UpdateRowStatusRule;
import nc.itf.to.m5x.ITransOrderMaintain;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.billtype.TOBillType;
import nc.vo.to.m5x.entity.BillHeaderVO;
import nc.vo.to.m5x.entity.BillVO;
import nc.vo.to.m5x.enumeration.BillStatus;
import nc.vo.to.m5x.pub.M5XVOBusiRuleUtil;
import nc.vo.to.pub.BusinessActionCodeConst;
import nccloud.vo.scmpub.utils.power.DataPermissionUtil;

/**
 * 调拨订单审批动作
 * 
 * @author 孙伟
 * @time 2009-11-26 下午04:20:59
 * @since 6.0
 */
public class ApproveAction {

  /**
   * 方法功能描述：调拨订单审批动作入口方法。
   * <p>
   * <b>参数说明</b>
   * 
   * @param script
   *          调拨订单审批脚本
   * @return
   */
  public BillVO[] approve(AbstractCompiler2 script) {
    BillVO[] ret = null;
    try {
      Object[] inCurObject = script.getPfParameterVO().m_preValueVos;
      if (inCurObject == null) {
        return new BillVO[0];
      }
      BillVO[] inCurVOs = new BillVO[inCurObject.length];
      int len = inCurObject.length;
      for (int i = 0; i < len; i++) {
        inCurVOs[i] = (BillVO) inCurObject[i];
      }
      // add By guozhq NCCloud适配数据权限-维护权限、特殊数据权限
      if(script.getPfParameterVO().isCloudEntry){
      	DataPermissionUtil.checkPermission(inCurVOs, TOBillType.TransOrder.getCode(), 
      			PowerActionEnum.APPROVE.getActioncode(), BillHeaderVO.VBILLCODE);
      }
      
      // TODO 雷军的后台补全VO提供后删除
      // BillTransferTool<BillVO> transferTool = new
      // BillTransferTool<BillVO>(inCurVOs);
      // inCurVOs = transferTool.getClientFullInfoBill();

      AroundProcesser<BillVO> processer =
          new AroundProcesser<BillVO>(ActionPlugInPoint.ApproveAction);
      this.addBeforeRule(processer);
      this.addAfterRule(processer, null != script ? script.getPfParameterVO()
          : null);

      TimeLog.logStart();
      processer.before(inCurVOs);
      TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
          "4009011_0", "04009011-0201")/* @res "调用审批流前执行业务规则" */); /*
                                                                              * -=notranslate
                                                                              * =-
                                                                              */

      /************* 该组件为批动作工作流处理开始...不能进行修改 *********************/
      script.procFlowBacth(script.getPfParameterVO());
      /************** 返回结果 *************************************************/

      TimeLog.logStart();
      processer.after(inCurVOs);
      TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
          "4009011_0", "04009011-0202")/* @res "调用审批流后执行业务规则" */); /*
                                                                              * -=notranslate
                                                                              * =-
                                                                              */

      // TODO 将可生成下游单据的VO设置到流程平台中
      /*
       * //将可生成下游单据的VO设置到流程平台中 script.setVos(pushVOs);
       * script.m_tmpVo.m_splitValueVos=pushVOs;
       * //由于单据数组长度可能改变，所以userObject也需要长度一直，否则有可能数组业界 Object[] userAry = new
       * Object[pushVOs.length]; if(userAry.length>0){ userAry[0] =
       * script.getUserObjAry()[0]; } script.m_tmpVo.m_userObjs = userAry;
       */
      // 查询最新VO返回
      ret = this.queryNewVO(inCurVOs);

      for (BillVO bill : ret) {
        // 计算调拨方式:5C,5D,5E,5I 在途归属财务组织;前台显示用
        M5XVOBusiRuleUtil.calculateOnWayOnwer(bill);
      }

      TimeLog.logStart();
      this.writeLog(BusinessActionCodeConst.APPROVE, inCurVOs);

    }
    catch (Exception ex) {
      ExceptionUtils.wrappException(ex);
    }
    return ret;
  }

  // ncm_changjr3_通版_当多级审批流驱动业务流时会出现当第一个人审批时就触发了推单的业务流
  // 主要修改增加两个审批流后执行业务规则FilterOrderByStatusRule和UpdatePflowVORule
  private void addAfterRule(AroundProcesser<BillVO> processer,
      PfParameterVO pfParameterVO) {
    // TODO Auto-generated method stub
    // 更新行状态

    processer.addAfterRule(new UpdateRowStatusRule());

    // 预留审批规则

    processer.addAfterRule(new ApproveReserveRule());

    processer.addAfterRule(new FilterOrderByStatusRule(BillStatus.AUDIT
        .toIntValue()));
    processer.addAfterFinalRule(new UpdatePflowVORule<BillVO>(pfParameterVO));
    
    //NCC调拨订单生成发货单发消息处理
    processer.addAfterRule(new SendMsgOderToDeliveryRule());

  }

  private void addBeforeRule(AroundProcesser<BillVO> processer) {
    // TODO Auto-generated method stub
    // 检查单据是否可以审批
    IRule<BillVO> rule = new CheckEnableApproveRule();
    processer.addBeforeRule(rule);
    // add by kongzhk start 2025-09-01 检查单据单价是否有值
    processer.addBeforeRule(new CheckPriceRule());
    // end
  }

  // ends_changjr3
  private BillVO[] queryNewVO(BillVO[] inCurVOs) {
    int len = inCurVOs.length;
    String[] ids = new String[len];
    for (int i = 0; i < len; i++) {
      ids[i] = inCurVOs[i].getPrimaryKey();
    }
    BillQuery<BillVO> query = new BillQuery<BillVO>(BillVO.class);
    BillVO[] bills = query.query(ids);
    return bills;
  }

  private void writeLog(String opercode, BillVO[] bills) {
    String classname = ITransOrderMaintain.class.getName();
    SCMBusinessLogUtil<BillVO> logutil =
        new SCMBusinessLogUtil<BillVO>(bills, null, classname, opercode);
    logutil.process();

  }

}

/*LrQMk/CqhOuglVbPU1U6VUbrrcY2Ak4ECTIqvT4zA2M=*/