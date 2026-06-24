package nc.bs.jzyy.sys.oa.out.praybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.FileUtil;
import nc.bs.jzyy.sys.FileVO;
import nc.itf.material.mdm.SendMdmItf;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

public class PrayBillFoOA_propertyUtil {

	/**
	 * value 里放取取方式，
	 * list[ 第一元素 类型 1 直接取字段值，	 类型2 取档案内容 ;  类型3 附件 （获取时单独处理）; 类型4 枚举 （在本类做初始）
	 * 第二元素 实体字段; 
	 * 第三元素 表名 ;
	 * 第四元素 值位置
	 * 第 五元素 查询条件 ]
	 */
	public static final Map<String, List<Object>> headMap = new HashMap<String, List<Object>>() {
		{
//			put("qgdh",  new ArrayList(){{add(1);  add(PraybillHeaderVO.VBILLCODE);}}); 
			//申请人
			put("sqr",new ArrayList(){{add(2);  add(PraybillHeaderVO.BILLMAKER); add("sm_user");  add("user_code");  add("cuserid"); }});
			//申请时间
			put("sqsj",new ArrayList(){{add(1);  add(PraybillHeaderVO.DMAKEDATE); }});
			//ERP单号
			put("erpcode",new ArrayList(){{add(1);  add(PraybillHeaderVO.VBILLCODE); }});
			//请购组织
			put("sqgs",new ArrayList(){{add(2);  add(PraybillHeaderVO.PK_ORG);add("org_stockorg");  add("code");  add("pk_stockorg"); }});
			//库存组织	field10844	orgcode
			put("orgcode", new ArrayList(){{add(2);  add(PraybillHeaderVO.PK_ORG);add("org_orgs");  add("code");  add("pk_org"); }});
			//采购组织	field19118	cgzz
			put("cgzz", new ArrayList(){{add(2);  add(PraybillItemVO.PK_PURCHASEORG);add("org_purchaseorg");  add("code");  add("pk_purchaseorg"); }});
			//请购类型	field10839	qglx
			put("qglx", new ArrayList(){{add(2);  add(PraybillHeaderVO.CTRANTYPEID);add("bd_billtype");  add("  pk_billtypecode ");  add(" pk_billtypeid "); }});
			//请购类型名称	field11470	qglxmc
			put("qglxmc", new ArrayList(){{add(2);  add(PraybillHeaderVO.CTRANTYPEID);add("bd_billtype");  add("  billtypename  ");  add(" pk_billtypeid "); }});
//			请购类型子选项	field11377	qglxzxx
			put("qglxzxx", new ArrayList(){{add(2);  add(PraybillHeaderVO.VDEF17);add("bd_defdoc");  add("code");  add(" pk_defdoc "); }});
//			费用部门	field10843	jhbm
			put("jhbm", new ArrayList(){{add(2);  add(PraybillHeaderVO.VDEF18);add("bd_defdoc");  add("code");  add("pk_defdoc"); }});
//			项目代码	field19121	xmdm
			put("xmdm", new ArrayList(){{add(2);  add(PraybillHeaderVO.CHPROJECTID);add("bd_project");  add("project_code");  add("pk_project"); }});
//			项目名称	field19122	xmmc
			put("xmmc", new ArrayList(){{add(2);  add(PraybillHeaderVO.CHPROJECTID);add("bd_project");  add(" project_name ");  add("pk_project"); }});
//			计划金额	field11378	ygje
//			put("ygje", new ArrayList(){{add(2);  add(PraybillHeaderVO.CHPROJECTID);add("bd_project");  add(" project_name ");  add("pk_project"); }});
			put("ygje", new ArrayList(){{add(1);  add(PraybillHeaderVO.NTOTALTAXMNY); }});
			//			说明	field11379	sm
			put("sm",new ArrayList(){{add(1);  add(PraybillHeaderVO.VMEMO); }});
//			相关附件	field19149	xgfj*******
			put("xgfj",new ArrayList(){{add(3);  add(PraybillHeaderVO.PK_PRAYBILL); }});
//			来源单据ID	field19149	lydjid
			put("lydjid",new ArrayList(){{add(1);  add(PraybillHeaderVO.PK_PRAYBILL); }});
//			单据日期	field19173	djrq
			put("djrq",new ArrayList(){{add(1);  add(PraybillHeaderVO.DBILLDATE); }});
//	
		}
	};
	public static final String[] OA_Itiem_H = new String[]{"qgdh","sqr","sqsj","erpcode","sqgs","orgcode","cgzz","qglx","qglxmc", "qglxzxx","ygje",
		"jhbm","xmdm","xmmc", "sm",/*"xgfj",*/"lydjid","djrq"};
	
	public static final String[] ERP_Itiem_H = new String[]{PraybillHeaderVO.BILLMAKER,PraybillHeaderVO.DMAKEDATE,PraybillHeaderVO.VBILLCODE,PraybillHeaderVO.PK_ORG
		,PraybillHeaderVO.CTRANTYPEID,PraybillHeaderVO.VDEF17,PraybillHeaderVO.VDEF18,PraybillHeaderVO.CHPROJECTID,PraybillHeaderVO.VMEMO
		,PraybillHeaderVO.PK_PRAYBILL,PraybillHeaderVO.DBILLDATE};
	
	public static final String[] ERP_Itiem_B = new String[]{PraybillItemVO.PK_REQSTOORG,PraybillItemVO.PK_PURCHASEORG,PraybillItemVO.PK_MATERIAL,PraybillItemVO.CPRODUCTORID,PraybillItemVO. CASTUNITID
		,PraybillItemVO. NASTNUM,PraybillItemVO. NTAXPRICE,PraybillItemVO. NTAXMNY,PraybillItemVO. PK_REQSTOR,PraybillItemVO. DSUGGESTDATE
		,PraybillItemVO. DREQDATE,PraybillItemVO. VBDEF7,PraybillItemVO. VBMEMO,PraybillItemVO.PK_SUGGESTSUPPLIER ,PraybillItemVO.PK_EMPLOYEE 
		,PraybillItemVO.CORDERTRANTYPECODE ,PraybillItemVO.CPROJECTTASKID ,PraybillItemVO. VBDEF19,PraybillItemVO. PK_PRAYBILL_B};
	
	
	public static final Map<String, List<Object>> bodyMap = new HashMap<String, List<Object>>(){{
		put("wlbm",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL); add("bd_material");  add(" code ");  add("pk_material"); }});
//		主数据编码	field19117	zsjbm
		put("zsjbm",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL); add("bd_material");  add(" def7 ");  add("pk_material"); }});
//		物料名称	field10811	wlmc
		put("wlmc",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL); add("bd_material");  add(" name ");  add("pk_material"); }});
//		助记码	field19123	zjm
		put("zjm",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL); add("bd_material");  add("materialmnecode");  add("pk_material"); }});
//		型号	field10812	xh
		put("xh",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL); add("bd_material");  add("materialtype");  add("pk_material"); }});
//		规格	field10813	gg
		put("gg",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL); add("bd_material");  add("materialspec");  add("pk_material"); }});
//		易制毒	field19124	yzd
		put("yzd",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL );add("bd_material");  add(" def20 ");  add("pk_material"); }});
//		易制爆	dield19125	yzb
		put("yzb",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL );add("bd_material");  add(" def18 ");  add("pk_material"); }});
//		材质	field19172	cz
		put("cz",new ArrayList(){{add(2);  add(PraybillItemVO.PK_MATERIAL );add("bd_material");  add(" def19 ");  add(" pk_material  "); }});
//		生产厂商	field10820	sccs
		put("sccs",new ArrayList(){{add(2);  add(PraybillItemVO.CPRODUCTORID); add("bd_defdoc");  add("name");  add("pk_defdoc"); }});
//		计量单位	field10814	jldw
		put("jldw",new ArrayList(){{add(2);  add(PraybillItemVO. CASTUNITID); add("bd_measdoc");  add("name");  add("pk_measdoc "); }});
//		计量单位编码  Field3
		put("Field3",new ArrayList(){{add(2);  add(PraybillItemVO. CASTUNITID); add("bd_measdoc");  add("code");  add("pk_measdoc "); }});
//		需求数量	field11381	sl2
		put("sl2",new ArrayList(){{add(1);  add(PraybillItemVO. NASTNUM); }});
//		计划单价	field19132	jhdj
		put("jhdj",new ArrayList(){{add(1);  add(PraybillItemVO. NTAXPRICE); }});
//		计划金额	field19116	jhje
		put("jhje",new ArrayList(){{add(1);  add(PraybillItemVO. NTAXMNY); }});
//		收货仓库	field10822	xqck
		put("xqck",new ArrayList(){{add(2);  add(PraybillItemVO. PK_REQSTOR);add("bd_stordoc"); add("code ");add("pk_stordoc"); }});
//		建议订货日期		jhdhrq
		put("dsuggestdate",new ArrayList(){{add(1);  add(PraybillItemVO. DSUGGESTDATE);  }});
//		需求日期	field11371	jhdhrq ----
		put("jhdhrq",new ArrayList(){{add(1);  add(PraybillItemVO. DREQDATE); }});
//		计划收料日期	field14098	Field16
		put("Field16",new ArrayList(){{add(1);  add(PraybillItemVO. VBDEF17); }});
//		备注	field10816	bz
		put("bz",new ArrayList(){{add(1);  add(PraybillItemVO. VBMEMO ); }});
//		建议供应商	field19169	jygys
		put("jygys",new ArrayList(){{add(2);  add(PraybillItemVO.PK_SUGGESTSUPPLIER); add("bd_supplier");  add("name");  add("pk_supplier "); }});

//		采购员	field11475	cgy
		put("cgy",new ArrayList(){{add(2);  add(PraybillItemVO.PK_EMPLOYEE );add("bd_psndoc");  add("  code  ");  add(" pk_psndoc"); }});
//		订单类型编码	field19170	ddlxbm
		put("ddlxbm",new ArrayList(){{add(2);  add(PraybillItemVO.CORDERTRANTYPECODE );add("bd_billtype");  add(" pk_billtypecode ");  add(" pk_billtypeid "); }});
//		项目任务	field19171	xmrw
		put("xmrw",new ArrayList(){{add(2);  add(PraybillItemVO.CPROJECTTASKID );add("pm_wbs");  add(" wbs_name ");  add(" pk_wbs  "); }});

//		维修工单号	field10817	wxgdh
		put("wxgdh",new ArrayList(){{add(1);  add(PraybillItemVO. VBDEF19); }});
//		来源单据明细ID	field19148	lydjmxid
		put("lydjmxid",new ArrayList(){{add(1);  add(PraybillItemVO. PK_PRAYBILL_B); }});
		
	}};
	
	public static  String[] OA_Itiem_B = new String[]{"wlbm","zsjbm","wlmc","zjm","xh","gg","sccs","yzd","yzb","jldw","sl2","jhdj","jhje","xqck","dsuggestdate",
		"jhdhrq","Field16","bz","jygys","cgy","ddlxbm","xmrw","cz","wxgdh","lydjmxid","Field3"};
	
	public static final String[] isNum =  new String[]{ "jhje","jhdj","sl2","ygje"};

	public static Object getMapValue(List<Object> list , ISuperVO vo) throws DAOException{
		if(null == list || list.size() == 0){
			return null;
		}
		
		Integer type = (Integer) list.get(0);
		String itmen = (String) list.get(1);
		
		
		if(1 == type){
			Object attributeValue = vo.getAttributeValue(itmen);
			if(null != attributeValue){
				if(attributeValue instanceof UFDate  || attributeValue instanceof UFDateTime){  //如果是时间类型，就取前日期
					String[] time = attributeValue.toString().split(" ");
					attributeValue = time[0];// 申请日期
				}
			}
			
			return "~".equals(attributeValue) ? "" : attributeValue;
		}else 
			if(2 == type){
			String tablename = (String) list.get(2);
			String field = (String) list.get(3);
			String keyField = (String) list.get(4);
			String key =  (String) vo.getAttributeValue(itmen);
			String queryDocNameByID = getServer().queryDocNameByID( (String) vo.getAttributeValue(itmen),
					tablename, keyField, field);
			return "~".equals(queryDocNameByID) ? "" : queryDocNameByID; 
		}else
			if(3 == type){ //附件处理
				FileVO[] ss = FileUtil.queryFiles((String)vo.getAttributeValue(itmen), true);
				if (ss.length > 0) {
//					Map detail = new HashMap();
					List array = new ArrayList();
					for (FileVO filevo : ss) {
						Map files = new HashMap();
						String base64 = filevo.getBase64Str();
						String name = filevo.getName();
						files.put("fileName", name);
						files.put("filePath", "base64:" + base64);
						array.add(files);
					}
					return array;
				}
		}
		return null;
		
	}
	
	public static SendMdmItf getServer(){
		return NCLocator.getInstance().lookup(SendMdmItf.class);
	}
	
	
}
