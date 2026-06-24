package nc.bs.jzyy.sys.oa.ic;

import java.util.HashMap;
import java.util.Map;

import nc.api.pubitf.IJsonParamMapping;

/**
 * ø‚¥Êµ•æ›≤È—Ø◊÷∂Œ”≥…‰∏∏¿‡
 * @Description: 
 *   
 * @author: ¡ıŒ∞
 * @date:   2019-5-24 …œŒÁ11:06:12   
 * @version NCC1909
 */
public class ICTranslateFieldMapping implements IJsonParamMapping{

  @Override
  public Map<String, Object> getDBaccessByField(Map<String, Object> filedsMap) {
    Map<String, Object> BDVOmap = new HashMap<String, Object>();
    ICBillFieldsEnum[] BDVOname = ICBillFieldsEnum.values();
    for(String field : filedsMap.keySet()){
      for(ICBillFieldsEnum name : BDVOname){
        if(field.equals(name.getFiledName())){
          BDVOmap.put(field, name.getSuperVO());
        }
      }
    }
    return BDVOmap;
  }

}
