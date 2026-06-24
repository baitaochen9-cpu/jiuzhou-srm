package nc.itf.ct.saledaily;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.RecvPlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public interface ISaledailyMaintain {

  /**
   * 方法功能描述：删除销售合同
   * <p>
   * 使用场景：
   * <ul>
   * <li>
   * </ul>
   * <b>参数说明</b>
   * 
   * @param bills
   *          销售合同
   * @throws BusinessException
   *           <p>
   * @since 6.1
   * @author gaogr
   * @time 2012-5-2 下午04:45:03
   */
  void deleteSaledaily(AggCtSaleVO[] bills) throws BusinessException;

  // /**
  // * 方法功能描述：销售合同查询
  // * <p>
  // * 使用场景：
  // * <ul>
  // * <li>
  // * </ul>
  // * <b>参数说明</b>
  // *
  // * @param queryScheme
  // * 查询方案
  // * @return
  // * @throws BusinessException
  // * <p>
  // * @since 6.1
  // * @author gaogr
  // * @time 2012-5-2 下午04:45:52
  // */
  // AggCtSaleVO[] maintainQuery(IQueryScheme queryScheme)
  // throws BusinessException;

  /**
   * 方法功能描述：生成收款单VO
   * <p>
   * 使用场景：销售合同推收款单
   * <ul>
   * <li>
   * </ul>
   * <b>参数说明</b>
   * 
   * @param srcVos
   *          销售合同
   * @return
   * @throws BusinessException
   *           <p>
   * @since 6.1
   * @author gaogr
   * @time 2012-5-2 下午04:42:43
   */
  AggregatedValueObject[] makePaybill(AggCtSaleVO[] srcVos)
      throws BusinessException;
  
  /**
   * 方法功能描述：生成收款单VO
   * <p>
   * 使用场景：销售合同推收款单
   * <ul>
   * <li>
   * </ul>
   * <b>参数说明</b>
   * 
   * @param srcVos
   *          销售合同
   * @return
   * @throws BusinessException
   *           <p>
   * @since 6.1
   * @author gaogr
   * @time 2012-5-2 下午04:42:43
   */
  AggregatedValueObject[] makePaybillByCtID(String[] id_tss)
      throws BusinessException;

  /**
   * 方法功能描述：根据表头主键查询vo。
   * 
   * @author wangfengd
   * @time 2010-7-14 下午02:15:31
   */
  AggCtSaleVO[] queryCtApVoByIds(String[] ids) throws BusinessException;

  /**
   * 根据合同查询收款计划
   */
  RecvPlanVO[] queryRecPlanVO(String pk, String vbillcode)
      throws BusinessException;

  /**
   * 方法功能描述：保存销售合同
   * <p>
   * 使用场景：新增保存和修改保存
   * <ul>
   * <li>
   * </ul>
   * <b>参数说明</b>
   * 
   * @param bill
   *          销售合同
   * @return
   * @throws BusinessException
   *           <p>
   * @since 6.1
   * @author gaogr
   * @time 2012-5-2 下午04:46:12
   */
  AggCtSaleVO[] save(AggCtSaleVO[] bill, AggCtSaleVO[] originBills)
      throws BusinessException;

}
