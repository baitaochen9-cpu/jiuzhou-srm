package nc.pubimpl.pu.m21.pu.m25;

import java.util.HashMap;
import java.util.Map;

import nc.bs.pu.m21.writeback.pu.OrderWriteBackFor25BP;
import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.pubitf.pu.m21.ic.m45.IOrderWriteBackPara;
import nc.pubitf.pu.m21.pu.m25.IOrderWriteBackFor25;
import nc.pubitf.pu.m21.pu.m25.IOrderWriteBackParaFor25;
import nc.vo.pu.m21.entity.OrderViewVO;
import nc.vo.pu.m21.exception.AskPriceException;
import nc.vo.pu.m21.pub.OrderVOUtil;
import nc.vo.pu.pub.constant.PUParaValue.ctrltype;
import nc.vo.pu.pub.util.PUSysParamUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;

import org.apache.commons.lang.ArrayUtils;

public class OrderWriteBackFor25Impl implements IOrderWriteBackFor25 {

  /**
   * 父类方法重写
   *
   * @see nc.pubitf.pu.m21.pu.m25.IOrderWriteBackFor25#invoicePriceChk(nc.pubitf.pu.m21.pu.m25.IOrderWriteBackParaFor25[])
   */
  @Override
  public void invoicePriceChk(IOrderWriteBackParaFor25[] wbVos)
      throws BusinessException {

    try {
      String[] bids = OrderVOUtil.getInsance().getBIDs(wbVos);
      OrderViewVO[] views =
          new ViewQuery<OrderViewVO>(OrderViewVO.class).query(bids);

      if (ArrayUtils.isEmpty(views)) {
        return;
      }

      Map<String, ctrltype> po04Map = this.getPO04Map(views);
      Map<String, UFDouble> po05Map = this.getPO05Map(views);

      Map<String, IOrderWriteBackPara> wbMap =
          OrderVOUtil.getInsance().createWBMap(wbVos);
      for (OrderViewVO view : views) {
        ctrltype PO04 = po04Map.get(view.getPk_org());

        if (ctrltype.not_control.equals(PO04)) {
          continue;
        }

        IOrderWriteBackParaFor25 wbVo =
            (IOrderWriteBackParaFor25) wbMap.get(view.getPk_order_b());
        // 删除则不进行单价容差检查
        if (wbVo.isDiscard()) {
          continue;
        }
        UFDouble PO05 = po05Map.get(view.getPk_org());

        // 主本币无税 价
        /**  2020 09 17 yezhian update 
         */
        UFDouble orderPrice = //view.getNnetprice();
        			view.getNorignetprice();
        //***************************  end ***************
        UFDouble invoicePrice = wbVo.getPrice();
        UFDouble tolerancePrice = orderPrice.multiply(PO05.add(100)).div(100);
        boolean isUserComfirm = wbVo.isPriceUserConfirm();
        if (MathTool.compareTo(invoicePrice, tolerancePrice) > 0) {
          if (PO04.equals(ctrltype.not_save)) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004030_0","04004030-0174")/*@res "存在发票本币无税单价超出订单本币无税净价容差控制的行，请检查！"*/);
          }
          else if (PO04.equals(ctrltype.ask) && !isUserComfirm) {
            throw new AskPriceException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004030_0","04004030-0175")/*@res "存在发票本币无税单价超出订单本币无税净价容差控制的行，是否继续？"*/);
          }
        }
      }
    }
    catch (Exception e) {
      // 日志异常
      ExceptionUtils.marsh(e);
    }
  }

  @Override
  public void writeBackFor25(IOrderWriteBackParaFor25[] wbVos)
      throws BusinessException {

    try {
      new OrderWriteBackFor25BP().writeback(wbVos);
    }
    catch (Exception e) {
      ExceptionUtils.marsh(e);
    }
  }

  /**
   * 方法功能描述：获得PO04参数的Map
   * <p>
   * <b>参数说明</b>
   *
   * @param views
   * @return Map<String,String> key:pkOrg value:PO04
   *         <p>
   * @since 6.0
   * @author wuxla
   * @time 2010-5-16 上午08:47:59
   */
  private Map<String, ctrltype> getPO04Map(OrderViewVO[] views) {
    Map<String, ctrltype> map = new HashMap<String, ctrltype>();
    for (OrderViewVO view : views) {
      String pkOrg = view.getPk_org();
      if (map.containsKey(pkOrg)) {
        continue;
      }
      ctrltype PO04 = PUSysParamUtil.getPO04(view.getPk_org());
      map.put(pkOrg, PO04);
    }
    return map;
  }

  /**
   * 方法功能描述：获得PO05参数的Map
   * <p>
   * <b>参数说明</b>
   *
   * @param views
   * @return map<String,UFDouble> key:pkOrg value:PO05
   *         <p>
   * @since 6.0
   * @author wuxla
   * @time 2010-5-16 上午08:47:01
   */
  private Map<String, UFDouble> getPO05Map(OrderViewVO[] views) {
    Map<String, UFDouble> map = new HashMap<String, UFDouble>();
    for (OrderViewVO view : views) {
      String pkOrg = view.getPk_org();
      if (map.containsKey(pkOrg)) {
        continue;
      }

      UFDouble PO05 = PUSysParamUtil.getPO05(view.getPk_org());
      map.put(pkOrg, PO05);
    }

    return map;
  }
}