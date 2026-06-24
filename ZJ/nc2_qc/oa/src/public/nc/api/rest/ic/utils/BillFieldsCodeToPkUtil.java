package nc.api.rest.ic.utils;

import java.util.List;
import java.util.Map;

import nc.api.pubitf.IJsonParamMapping;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.scmpub.util.translate.BillTranslator;
import nc.vo.scmpub.util.translate.MDTranslateParamProvider;
import nc.vo.scmpub.util.translate.TranslateUtils;

/**
 * 处理参数编码翻译
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-5-24 下午1:38:58   
 * @version NCC1909
 */
public class BillFieldsCodeToPkUtil {

  private static String PK_ORG = "pk_org";
  /**
   * 通过json参数及对应的档案对象，翻译json中参照字段编码对应的主键并返回到map中
   * @param fieldmap
   * @param BDVOmap
   * @return
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> translateCode(Map<String, Object> fieldmap, Map<String, Object> BDVOmap){
    String pkGroup = AppContext.getInstance().getPkGroup();
    String[] pkOrgs = null;
    String[] codes = null;
    //先翻译组织
    Object orgVO = BDVOmap.get(PK_ORG);
    if(orgVO != null){
      Object fieldCode = fieldmap.get(PK_ORG);
      if(fieldCode instanceof List && ((List<String>) fieldCode).size() > 0){
        codes = ((List<String>) fieldCode).toArray(new String[0]);
      }else if(fieldCode instanceof String){
        codes = new String[]{(String) fieldCode};
      }
      Map<String, String> orgCodeToId =
          TranslateUtils.trancelateCodeToID(orgVO, codes, pkGroup);
      if(orgCodeToId.values() != null){
        pkOrgs = new String[orgCodeToId.values().size()];
        int i = 0;
        for(String orgPk : orgCodeToId.values()){
          pkOrgs[i] = orgPk;
          i++;
        }
        fieldmap.put(PK_ORG, pkOrgs);
      }else{
        //组织翻译失败，其他字段不支持翻译
        return null;
      }
    }
    //翻译其他字段
    for(String key : fieldmap.keySet()){
      Object BDVO = BDVOmap.get(key);
      if(BDVO == null || PK_ORG.equals(key))
        continue;
      Object fieldCode = fieldmap.get(key);
      if(fieldCode instanceof List && ((List<String>) fieldCode).size() > 0){
        codes = ((List<String>) fieldCode).toArray(new String[0]);
      }else if(fieldCode instanceof String){
        codes = new String[]{(String) fieldCode};
      }
      for(String pk_org : pkOrgs){
        Map<String, String> codeToId =
            TranslateUtils.trancelateCodeToID(BDVO, codes, pk_org);
        if(codeToId.values() != null){
          fieldmap.put(key, codeToId.values().toArray(new String[codeToId.values().size()]));
        }
      }
    }
    return fieldmap;
  }
  
  
  /**
   * 元数据单据编码翻译，适用于由元数据的单据
   * @param bills
   */
  public static void doTranslateVOFields(IBill[] bills){
    MDTranslateParamProvider<IBill> provider = new MDTranslateParamProvider<>();
    BillTranslator billTanslator = new BillTranslator();
    billTanslator.translateCodeToId(bills, provider);
  }
  
  /**
   * 无元数据单据查询，需要对传入的参数跟档案进行映射，通过当担VO进行翻译
   * @param jsonParamMapping
   * @param paramMap
   */
  public static Map<String, Object> doTranslateFields(IJsonParamMapping jsonParamMapping, 
      Map<String, Object> paramMap){
    Map<String, Object> keyFromParam = jsonParamMapping.getDBaccessByField(paramMap);
    return BillFieldsCodeToPkUtil.translateCode(paramMap, keyFromParam);
  }
  
}
