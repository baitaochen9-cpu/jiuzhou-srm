package nc.itf.ct.purdaily;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.scmpub.page.PageQueryVO;

/**
 * 单据维护应用接口 <br>
 * <b>主要功能：</b>
 * <ol>
 * <li>单据查询</li>
 * </ol>
 * 
 * @since 6.36
 * @version 2015-04-07 15:25:46
 */
public interface IPurdailyMaintainApp {
  /**
   * 单据查询
   * 
   * @param scheme
   *          UI端组织的查询方案
   * @return 按照单据号进行排序的单据分页。懒加载形式，只有第一页的第一张单据 才有表体数据。没有查询到数据时返回零长度的数组
   * @throws BusinessException
   */
  PageQueryVO queryMZ2App(IQueryScheme scheme) throws BusinessException;

  /**
   * 单据查询
   * 
   * @param ids
   *          单据主键数组
   * @return 懒加载形式，只有第一张单据才有表体数据。
   * @throws BusinessException
   */
  AggCtPuVO[] queryMZ2App(String[] ids) throws BusinessException;
  
  /**
	 * 根据单据号查询最新版本采购合同
	 * 
	 * @param vbillcode 单据号
	 * @return 
	 * @throws BusinessException
	 */
  AggCtPuVO queryMZ3AppByVbillcode(String vbillcode) throws BusinessException;
}
