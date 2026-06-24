package nc.bs.jzyy.sys.oa.ic.m45;

import java.util.Map;

import nc.bs.jzyy.sys.oa.ic.ICTranslateFieldMapping;

/**
 * 处理采购入库单查询的字段映射
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-5-24 上午11:05:43   
 * @version NCC1909
 */
public class M45FieldMapping extends ICTranslateFieldMapping {

  @Override
  public Map<String, Object> getDBaccessByField(Map<String, Object> paramMap) {
    Map<String, Object> keyFromParam = super.getDBaccessByField(paramMap);
    M45FieldsEnum[] values = M45FieldsEnum.values();
    for(M45FieldsEnum em : values){
      String name = em.getFiledName();
      if(paramMap.get(name) != null){
        keyFromParam.put(name, em.getSuperVO());
      }
    }
    
    return keyFromParam;
  }
  
}
