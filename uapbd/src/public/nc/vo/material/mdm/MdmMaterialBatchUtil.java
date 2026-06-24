package nc.vo.material.mdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import nc.vo.bd.material.MaterialVO;

public class MdmMaterialBatchUtil {



	private Map<String,Map<String,String>> mdmfomap ;
	
	Map<String,List<MaterialVO>> rest_h = new HashMap<String,List<MaterialVO>>();//最终返回<orgID，Material_list>
	
	Map<String,List<MaterialVO>> rest_b = new HashMap<String,List<MaterialVO>>();//最终返回<orgID，Material_list>
	
	public Map<String, List<MaterialVO>> getRest_h() {
		return rest_h;
	}



	public void setRest_h(Map<String, List<MaterialVO>> rest_h) {
		this.rest_h = rest_h;
	}



	public Map<String, List<MaterialVO>> getRest_b() {
		return rest_b;
	}



	public void setRest_b(Map<String, List<MaterialVO>> rest_b) {
		this.rest_b = rest_b;
	}



	public MdmMaterialBatchUtil(List<Map<String, String>> queryMdmPrimary) {
		
		if (null == queryMdmPrimary || queryMdmPrimary.size() == 0) {
			return;
		}
		this.mdmfomap = new HashMap<String,Map<String,String>>();
		
		for (Map<String, String> map : queryMdmPrimary) {
			mdmfomap.put(map.get("mdm_code"), map);// 取数据编码

		}
	}



	/**
	 * 
	 * @param map
	 *            数据清单
	 * @return 原始物料列表
	 */
	public  void getOriginalMaterial(
			Map<String, List<MaterialVO>> map) {
		if(null  == map || map.size() == 0){
			return ;
		}
		
		
		for(String key : map.keySet()){
			List<MaterialVO> nc_materials =  map.get(key);
			Map<String, String> mdm_h = mdmfomap.get(key);
			String fromsystemid = mdm_h.get("fromsystemid"); //主数据主表内的原始物料ID
			
			for(MaterialVO materal : nc_materials){
				String pk_material = materal.getPk_material();//nc物料ID
				String pk_org = materal.getPk_org();//组织
				if(pk_material.equals(fromsystemid)){ //如果物料ID等于主数据原始物料ID，说明是主表的原始数据需要进行同步
					List<MaterialVO> list = rest_h.get(pk_org);
					if(null == list){
						list = new ArrayList<MaterialVO>();
					}
					list.add(materal);
					rest_h.put(pk_org, list);
				}{
					List<MaterialVO> list = rest_b.get(pk_org);
					if(null == list){
						list = new ArrayList<MaterialVO>();
					}
					list.add(materal);
					rest_b.put(pk_org, list);
				}
				
			}
		}
		
	
	}

	
	public Map<String,Map<String,String>> getMdmfomap() {
		return mdmfomap;
	}

	public void setMdmfomap(Map<String,Map<String,String>> mdmfomap) {
		this.mdmfomap = mdmfomap;
	}


	
}
