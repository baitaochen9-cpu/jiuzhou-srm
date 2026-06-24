package nc.vo.material.mdm;

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.material.mdm.SendMdmItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;

/**
 * 用于物料同步主数据的窗口字段列表
 * 
 * @author zhian.ye 20240424
 * 
 *         如果要表窗口字段进行添加，必须行在这里加好，再到模板初始化进行调整，注意编码的映射，否则业务处理无法处理
 */
public class SendMdmPropetys {

	public static final String MDMCLASS = "mdmclass";
	public static final String MDMCODE = "mdmcode";
	/**
	 * ERP头上的字段属性物料基本属性
	 * 添加字段与模板保持同步
	 */
	public static final String[] headItems = { "code"/* 物料编码 */, "name"/* 物料名称 */,
			"pk_measdoc"/* 计量单位 */,"materialtype"/* 型号 */, 
			"materialspec"/* 规格 */,"pk_material"/* 物料ID */, 
			"pk_marbasclass"/* 物料分类ID */,"vmanufacturer_148"/* 生产厂家 */,
			"materialmnecode"/*助记码*/ ,"memo"/*备注*/,"def19"/*材质*/
			,"def14"/*集团统一分类*/};
	
	/**
	 * 非空校验组，用于用户交互处理时
	 */
	public static final Map<String, String> handNotNullList = new HashMap<String, String>() {
		{
			put("materialtype", "型号");
			put("materialspec", "规格");
			put("mdmclass", "集团物料分类");

		}
	};
	/**
	 * 对话窗口表体的字段属性，源于主数据字段
	 * 添加字段与模板保持同步
	 */
	public static final String[] bodyItems = {"groupMarbascode" ,
			"groupMarbascode_@code","groupMarbascode_name"/* 集团统一分类 */, 
			"mdm_code"/* 主数据编码 */,"invname"/* 物料名称 */, "spec"/* 规格 */, 
			"model"/* 型号 */, "brand"/* 生产厂家 */,"variant_name"/* 异名 */, 
			"english_name"/* 英文名称 */,"materialmnecode"/*助记码*/ ,"cz"/*材质*/,
			"postunit"/*计量单位编码*/,"postunit_name"/*计量单位名称*/,"pk_material"/*物料原始编码*/
			,"fromsystemid"/*物料主键*/
	};
	
	/**
	 * 主数据组织物料字段清单
	 */
	public static final String[] mdmorgmaterial = { "id", "brand",
			"enablestate#name", "fromsystem", "fromsystemid", "groupclasscode",
			"groupclassname", "invclassid", "invclassidcode", "invclassidname",
			"invname", "model", "orgcode", "orgname", "pk_material", "pk_org",
			"reserve1", "reserve10", "reserve2", "reserve3", "reserve4",
			"reserve5", "reserve6", "reserve7", "reserve8", "reserve9", "spec",
			"unitcode", "unitname", "updatetime", "vproductor", "cz",
			"yzd#name", "yzb#name" };

	/**
	 * 物料组织属性
	 */
	public static Map<String, String> orgmaterialMap = null;

	

	public void setGetOrgmaterialMap(Map<String, String> getOrgmaterialMap) {
		this.orgmaterialMap = getOrgmaterialMap;
	} 
	
	public static Map<String,List<String>> getCondsByVO(MaterialVO material) {
		/*
		 * 组装查询条件
		 */
 		Map<String,List<String>> conds = new HashMap<String,List<String>>();
		String name2 = material.getName();// 物料名称
		String materialmnecode = material.getMaterialmnecode();
		String materialtype = material.getMaterialtype();//型号
		String materialspec = material.getMaterialspec();//规格
		
		List<String> list = new ArrayList<String>();
		list.add("and"); 
		list.add("like");
		list.add("'%"+name2+"%'");
		conds.put("invname", list);  //名称一致的
		list=new ArrayList<String>();
		list.add("and");
		list.add("like");
		list.add("'%"+name2+"%'");
		conds.put("splicename", list); //标识一至的
		list=new ArrayList<String>();
		list.add("and");
		list.add("like");
		list.add("'%"+materialmnecode+"%'"); //助记码一至的
		conds.put("materialmnecode", list);
		list = new ArrayList<String>();
		list.add("and");
		list.add("like");
		list.add("'%"+materialtype+"%'"); //型号
		conds.put("model", list);

		return conds;
	}
	
	private static Object[] sqlExecute(String tableName,String columnName,String whereColumnName,String whereValue) {
        try {
            Object[] obj_result = null;
            IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
            StringBuilder sql = new StringBuilder();
            sql.append("select "  + columnName + " from " + tableName + " where " + whereColumnName + " = '" + whereValue + "';");
            obj_result = (Object[])query.executeQuery(sql.toString(),new ArrayProcessor());
            return obj_result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
        
    }

	
	/**
	 * 维度匹配查询
	 * @return 调用接获取主数据主表格式化数据
	 */
	public static Map<String,List<String>> getCondsByVO(int mode, MaterialVO material,
			Boolean Mterialcode,Boolean Materialname,Boolean Materialtype,Boolean Materialspec,
			Boolean Materialmnecode,Boolean Def19,Boolean Brand,
			Boolean GroupMarbascode_name,String GroupMarbascode,HashMap<String, String> hashmap
			) {

		Map<String,List<String>> conds = new HashMap<String,List<String>>();
		
		String name2;
		String materialtype;
		String materialspec;
		String materialmnecode;
		String cz;
		String brand;
		Object[] groupMarbascode_names;
		String groupMarbascode_name;
		List<String> list;
		
		switch (mode){
		case 1 ://维度匹配中，将所有的查询维度分别合并输出
			name2 = material.getName();// 物料名称
			materialtype = material.getMaterialtype();//型号
			materialspec = material.getMaterialspec();//规格
			materialmnecode = material.getMaterialmnecode();//助记码
			cz = material.getDef19();//材质
			brand = (String)material.getAttributeValue("vdef1_148");//生产厂家		
			groupMarbascode_names = sqlExecute( "bd_defdoc","name", "pk_defdoc", GroupMarbascode);//集团统一分类
			groupMarbascode_name = groupMarbascode_names[0].toString();
					
			list = new ArrayList<String>();

			
			if (true == Materialname && null != name2) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + name2 + "%'");
				conds.put("invname", list); // 物料名称
			}
			if (true == Materialtype && null != materialtype) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + materialtype + "%'");
				conds.put("model", list); // 型号
			}
			if (true == Materialspec && null != materialspec) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + materialspec + "%'");
				conds.put("spec", list); // 规格
			}
			if (true == Materialmnecode && null != materialmnecode) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + materialmnecode + "%'");
				conds.put("materialmnecode", list); // 助记码
			}
			if (true == Def19 && null != cz) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + cz + "%'");
				conds.put("cz", list); // 材质
			}
			if (true == Brand && null != brand) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + brand + "%'");
				conds.put("brand", list); // 生产厂家
			}
			if (true == GroupMarbascode_name && null != groupMarbascode_name) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + groupMarbascode_name + "%'");
				conds.put("groupMarbascode#name", list); // 集团统一分类
			}
			break;
		case 2 ://模糊搜索中，将所有的模塑搜索项合并显示
			List<String> list1 = new ArrayList<String>();

			if (true == Mterialcode && null != hashmap.get("code")) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + hashmap.get("code") + "%'");
				conds.put("pk_material", list); // 物料编码
			}
			if (true == Materialname && null != hashmap.get("name")) {
				list1 = new ArrayList<String>();
				list1.add("and");
				list1.add("like");
				list1.add("'%" + hashmap.get("name") + "%'");
				conds.put("invname", list1); // 物料名称
			}
			if (true == Materialtype && null != hashmap.get("model")) {
				list1 = new ArrayList<String>();
				list1.add("and");
				list1.add("like");
				list1.add("'%" + hashmap.get("model") + "%'");
				conds.put("model", list1); // 型号
			}
			if (true == Materialspec && null != hashmap.get("spec")) {
				list1 = new ArrayList<String>();
				list1.add("and");
				list1.add("like");
				list1.add("'%" + hashmap.get("spec") + "%'");
				conds.put("spec", list1); // 规格
			}
			if (true == Materialmnecode
					&& null != hashmap.get("materialmnecode")) {
				list1 = new ArrayList<String>();
				list1.add("and");
				list1.add("like");
				list1.add("'%" + hashmap.get("materialmnecode") + "%'");
				conds.put("materialmnecode", list1); // 助记码
			}
			if (true == GroupMarbascode_name && null != hashmap.get("mdm_code")) {
				list1 = new ArrayList<String>();
				list1.add("and");
				list1.add("like");
				list1.add("'%" + hashmap.get("mdm_code") + "%'");
				conds.put("mdm_code", list1); // 主数据编码
			}
			break;
		case 3://模糊搜索中，将所有的条件合并交集显示
			name2 = material.getName();// 物料名称
			materialtype = material.getMaterialtype();//型号
			materialspec = material.getMaterialspec();//规格
			materialmnecode = material.getMaterialmnecode();//助记码
			cz = material.getDef19();//材质
			brand = (String)material.getAttributeValue("vdef1_148");//生产厂家		
			groupMarbascode_names = sqlExecute( "bd_defdoc","name", "pk_defdoc", GroupMarbascode);//集团统一分类
			groupMarbascode_name = groupMarbascode_names[0].toString();
					
			list = new ArrayList<String>();
			
			StringBuilder sqlQuery;
			if (true == Materialname && null != name2) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + name2 + "%'");
				conds.put("invname", list); // 物料名称
//				sqlQuery.append("and like")
			}
			if (true == Materialtype && null != materialtype) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + materialtype + "%'");
				conds.put("model", list); // 型号
			}
			if (true == Materialspec && null != materialspec) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + materialspec + "%'");
				conds.put("spec", list); // 规格
			}
			if (true == Materialmnecode && null != materialmnecode) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + materialmnecode + "%'");
				conds.put("materialmnecode", list); // 助记码
			}
			if (true == Def19 && null != cz) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + cz + "%'");
				conds.put("cz", list); // 材质
			}
			if (true == Brand && null != brand) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + brand + "%'");
				conds.put("brand", list); // 生产厂家
			}
			if (true == GroupMarbascode_name && null != groupMarbascode_name) {
				list = new ArrayList<String>();
				list.add("and");
				list.add("like");
				list.add("'%" + groupMarbascode_name + "%'");
				conds.put("groupMarbascode#name", list); // 集团统一分类
			}
			break;
		default:
			break;
		}
		return conds;
	}

}
