/**
 * $文件说明$
 * 
 * @author zhaoyha
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-5-11 上午10:55:58
 */
package nc.bs.pu.m25.maintain.rule.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.pubitf.pu.m21.pu.m25.IOrderWriteBackFor25;
import nc.pubitf.pu.m21.pu.m25.IOrderWriteBackParaFor25;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.m25.entity.InvoiceHeaderVO;
import nc.vo.pu.m25.entity.InvoiceItemVO;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pu.m25.enumeration.InvoiceRowType;
import nc.vo.pu.m25.enumeration.InvoiceUserAnswerType;
import nc.vo.pu.m25.env.InvoiceUIToBSEnv;
import nc.vo.pu.pub.util.AggVOUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.res.billtype.POBillType;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @description
 * 发票参照订单生成时检查是否超订单单价容差控制
 * @scene
 * 保存的BP
 * @param
 * env 采购发票操作时前台到后台的环境信息，一般随平台动作的userObj向外传 
 *
 * @since 6.3
 * @version 2014-10-22 下午3:36:02
 * @author zhangshqb
 */
public class OrderPriceCheckRule implements ICompareRule<InvoiceVO> {

  private InvoiceUIToBSEnv env;

  /**
   * OrderPriceCheckRule 的构造子
   * 
   * @param env
   */
  public OrderPriceCheckRule(InvoiceUIToBSEnv env) {
    this.setEnv(env);
  }

  /**
   * @return env
   */
  public InvoiceUIToBSEnv getEnv() {
    return this.env;
  }

  @Override
  public void process(InvoiceVO[] vos, InvoiceVO[] originVOs) {
    Map<String, InvoiceItemVO> orgMap = new HashMap<String, InvoiceItemVO>();
    if (!ArrayUtils.isEmpty(originVOs)) {
      orgMap = AggVOUtil.createItemMap(originVOs);
    }
    List<IOrderWriteBackParaFor25> wbVoList =
        new ArrayList<IOrderWriteBackParaFor25>();
    for (InvoiceVO vo : vos) {
      InvoiceHeaderVO head = vo.getParentVO();
      for (InvoiceItemVO item : vo.getChildrenVO()) {
        String firstordertype = item.getCfirsttypecode();
        String srcordertype = item.getCsourcetypecode();
        if (!POBillType.Order.getCode().equals(firstordertype)
            && !POBillType.Order.getCode().equals(srcordertype)) {
          continue;
        }
        // 对于费用行不检查容差控制(仅对费用,不含折扣和货物)
        if (item.getFrowtype() != null
            && item.getFrowtype().intValue() == InvoiceRowType.FEE_ROW) {
          continue;
        }

        InvoiceItemVO oldItem = orgMap.get(item.getPk_order_b());
        UFDouble oldPrice = null != oldItem ? oldItem.getNprice() : null;
        UFDouble newPrice = item.getNprice();
        if (!StringUtil.isEmptyWithTrim(item.getPk_order_b())
            && MathTool.compareTo(newPrice, oldPrice) > 0) {
          wbVoList.add(this.createPOWBVO(item, head));
        }
      }
    }
    if (0 == wbVoList.size()) {
      return;
    }
    IOrderWriteBackParaFor25[] wbVos =
        wbVoList.toArray(new IOrderWriteBackParaFor25[wbVoList.size()]);
    try {
      // 调用订单的服务
      NCLocator.getInstance().lookup(IOrderWriteBackFor25.class)
          .invoicePriceChk(wbVos);
    }
    catch (BusinessException e) {
      // 日志异常
      ExceptionUtils.wrappException(e);
    }
  }

  /**
   * @param env
   *          要设置的 env
   */
  public void setEnv(InvoiceUIToBSEnv env) {
    this.env = env;
  }

  private IOrderWriteBackParaFor25 createPOWBVO(final InvoiceItemVO item,
      final InvoiceHeaderVO head) {
    return new IOrderWriteBackParaFor25() {

      @Override
      public String getBBID() {
        return null;
      }

      @Override
      public String getBID() {
        return item.getPk_order_b();
      }

      @Override
      public UFDouble getDiffMny() {
        return null;
      }

      @Override
      public UFDouble getDiffNum() {
        return null;
      }

      @Override
      public String getHID() {
        return item.getPk_order();
      }

      @Override
      public UFDouble getPrice() {
//        return item.getNprice();
    	  //2020 09 18 yezhian update fo raybow
    	  return item.getNorigprice(); 
    	//2020 09 18 yezhian update fo raybow  end
      }

      @Override
      public boolean isClose() {
        return false;
      }

      @Override
      public boolean isDiscard() {
        return false;
      }

      @Override
      public boolean isFee() {
        return head.getBfee().booleanValue();
      }

      @Override
      public boolean isNumUserConfirm() {
        return false;
      }

      @Override
      public boolean isPriceUserConfirm() {
        return InvoiceUserAnswerType.YES == OrderPriceCheckRule.this.getEnv()
            .getOverPOPrice();
      }

      @Override
      public boolean isReturn() {
        return false;
      }

    };
  }

}
