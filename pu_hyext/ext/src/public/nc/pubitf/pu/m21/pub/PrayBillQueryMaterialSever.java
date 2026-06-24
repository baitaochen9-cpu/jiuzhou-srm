package nc.pubitf.pu.m21.pub;

import java.util.Map;

public interface PrayBillQueryMaterialSever {
	
  public Map<String,String> materialQuery (String[] codes ) throws Exception;
  
  public Map<String,String> unitQuery (Map codesMap ) throws Exception;
  
//  public Map<String,String> unitQuery (String[] codes ) throws Exception;

  public String orgQuery(String code) throws Exception;
}
