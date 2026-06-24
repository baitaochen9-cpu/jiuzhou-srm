package nc.api.pubitf;

import java.util.Map;

/**
 * 单据查询 json字段跟单据属性映射接口
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-5-24 上午9:33:06   
 * @version NCC1909
 */
public interface IJsonParamMapping {

  /**
   * 通过json中的参数名，获取到档案VO对象
   * 根据字段映射获取档案VO
   * @param filedsMap  VOfield, mapField
   * @return VOfield,SuperVO
   */
  Map<String, Object> getDBaccessByField(Map<String, Object> filedsMap);
}
