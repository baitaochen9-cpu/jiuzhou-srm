package nc.itf.to.m5x;

import java.util.List;
import java.util.Map;

import nc.ui.pub.bill.BillCardPanel;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public interface IFindPricePubService {



  public 	Map<Integer, UFDouble> doFindPrice(Map<Integer, Map<String, String>> map);

  /**
   * 取SO模块的单价
   * @param map 
   * @return 行，单价
 * @throws BusinessException 
   */
public Map<Integer, UFDouble> doFindPrice_so(
		Map<Integer, Map<String, String>> map) throws BusinessException;

/**
 * 通过物料id 查询物料基本分类
 * @param id
 * @return
 * @throws BusinessException 
 */
public Map queryDbByCond(String id) throws BusinessException;

/**
 * 取参考成本
 * @param map_3
 * @return
 * @throws BusinessException 
 */
public Map<? extends Integer, ? extends UFDouble> doFindPrice_ck(
		Map<Integer, Map<String, String>> map_3) throws BusinessException;

}
