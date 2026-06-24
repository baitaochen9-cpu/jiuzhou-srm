package nc.bs.jzyy.sys.oa.pu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.util.BillQueryUtils;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.uap.ws.log.NCLogger;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.stordoc.StordocVO;
import nc.vo.org.DeptVO;
import nc.vo.org.StockOrgVO;
import nc.vo.pfxx.bdconstra.BDContraDtlVO;
import nc.vo.pmpub.project.ProjectHeadVO;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.HslParseUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.sm.UserVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Oa2M20 extends AbstracAdapter4Ext {
	
	//默认集团
	public static final String PK_GROUP="0001V110000000000FH0";
	
	//默认币种
	public static final String CCURRENCYID="1002Z0100000000001K1";
	
	@Override
	public JSONObject sys(Object billvo) throws BusinessException, DAOException {
		Map materialMap = new HashMap<>();
		PraybillVO praybillVO = this.M20(billvo,materialMap);
		JSONObject data = new JSONObject();
		String re_mess="生成请购单成功";
		if(null!=praybillVO){
			Map lydjmxidMap = new HashMap<>();
			data.put("code", praybillVO.getHVO().getVbillcode());
			data.put("pk", praybillVO.getPrimaryKey());
			data.put("billstatus", praybillVO.getHVO().getFbillstatus());
			data.put("lydjid",praybillVO.getHVO().getPk_praybill());
			for (PraybillItemVO item : praybillVO.getBVO()) {
				if(null == materialMap.get(item.getCrowno())) continue;
				String oaHhCode=(String) materialMap.get(item.getCrowno());
				lydjmxidMap.put(oaHhCode, item.getPk_praybill_b());
			}
			data.put("lydjmx",lydjmxidMap);
		}else{
			re_mess="生成请购单异常";
		}
		// 将结果返回
		return getRsultDataSuccess(data,re_mess);
	}
	/*
	 * 传入JSON
{
	"functype": "OA_M20",
	"h": {
		"org": "23",
		"planpsn": "yf01",
		"plandept": "08",
		"vtrantypecode": "20-01",
		"dbilldate": "2022-06-20",
		"vdef19": "OA单号",
		"chprojectid": "PM202006290010",
		"memo": "备注2022-06-20"
	},
	"b": [
		{
			"material": "201-V107",
			"nnum": "10",
			"cordertrantypecode": "21-Cxx-23-02",
			"pk_reqdept": "0301",
			"pk_reqstor": "03",
			"vbmemo": "行备注2022-06-20",
			"vbdef19": "OA00001"
		}
	]
}
	 * */
	/**
	 * 请购单
	 * @param jsondata
	 * @return
	 * @throws BusinessException
	 */
	//部门
	private Map<String,DeptVO> MAP_DEPT;
	//仓库
	private Map<String,StordocVO> MAP_STORDOC;
	//项目信息
	private Map<String,ProjectHeadVO> MAP_PRO;
	//生产厂商
	private Map<String,DefdocVO> MAP_PRODUCT;
	
	public PraybillVO M20(Object jsondata,Map materialMap) throws BusinessException{
		//传入参数转换JSON对象
		JSONObject redataObject=(JSONObject)JSON.toJSON(jsondata);
		//JSONObject redataObject=reFullObject.getJSONObject("data");
		//表头信息
		JSONObject reObject=redataObject.getJSONObject("h");
		
		//转译后的表头信息
		Map<String,String> Hmap=new HashMap<String, String>();
		
		//当前日期
		UFDate dateNow=new UFDate();
		//组织信息（库存、请购、采购、费用部门）250613修改添加
		String pk_org = null == reObject.getString("org") ? reObject.getString("pk_org") :	reObject.getString("org");
		if (null ==  pk_org) ExceptionUtils.wrappBusinessException("org 库存组织信息非空!");
		if(StringUtils.isNotEmpty(pk_org)){
			StockOrgVO orgVO=(StockOrgVO)this.querySuperVO(StockOrgVO.class, "code='"+pk_org+"' and dr=0 and enablestate=2");
			if(null!=orgVO){
				Hmap.put("pk_org", orgVO.getPrimaryKey());
				Hmap.put("pk_org_v", orgVO.getPk_vid());
			}else{
				ExceptionUtils.wrappBusinessException(pk_org+" NC中未查询到在用的相关库存组织信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("org 库存组织信息非空!");
		}
		if(StringUtils.isNotEmpty(reObject.getString("vbdef18"))){
			StockOrgVO reqstoorgVO=(StockOrgVO)this.querySuperVO(StockOrgVO.class, "code='"+reObject.getString("vbdef18")+"' and dr=0 and enablestate=2");
			if(null!=reqstoorgVO){
				Hmap.put("vbdef18", reqstoorgVO.getPrimaryKey());
				Hmap.put("vbdef18_v", reqstoorgVO.getPk_vid());
			}else{
//				ExceptionUtils.wrappBusinessException(reObject.getString("vbdef18")+" NC中未查询到在用的相关请购组织信息!");
				Logger.error(reObject.getString("vbdef18")+" NC中未查询到在用的相关请购组织信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("org 请购组织信息非空!");
		}
		if(StringUtils.isNotEmpty(reObject.getString("pk_purchaseorg"))){
			StockOrgVO purchaseorgVO=(StockOrgVO)this.querySuperVO(StockOrgVO.class, "code='"+reObject.getString("pk_purchaseorg")+"' and dr=0 and enablestate=2");
			if(null!=purchaseorgVO){
				Hmap.put("pk_purchaseorg", purchaseorgVO.getPrimaryKey());
				Hmap.put("pk_purchaseorg_v", purchaseorgVO.getPk_vid());
			}else{
				Logger.error(reObject.getString("pk_purchaseorg")+" NC中未查询到在用的相关采购组织信息!");
//				ExceptionUtils.wrappBusinessException(reObject.getString("pk_purchaseorg")+" NC中未查询到在用的相关采购组织信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("org 采购组织信息非空!");
		}
		if(StringUtils.isNotEmpty(reObject.getString("vdef18"))){
			DefdocVO orgVO=(DefdocVO)this.querySuperVO(DefdocVO.class, "code='"+reObject.getString("vdef18")+"' and dr=0 and enablestate=2 and  pk_defdoclist ='1001V110000000169CBQ'");
			if(null!=orgVO){
				Hmap.put("vdef18", orgVO.getPrimaryKey());
//				Hmap.put("vdef18_v", orgVO.getPk_vid());
			}else{
				Logger.error(reObject.getString("vbdef18")+" NC中未查询到在用的相关请购组织信息!");
//				ExceptionUtils.wrappBusinessException(reObject.getString("vdef18")+" NC中未查询到在用的相关费用部门组织信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("org 费用部门信息非空!");
		}
		//计划员信息
		if(StringUtils.isNotEmpty(reObject.getString("billmaker"))){
			PsndocVO psndocVO=(PsndocVO)this.querySuperVO(PsndocVO.class, "code='"+reObject.getString("billmaker")+"' and dr=0 and enablestate=2");
			if(null!=psndocVO){
				Hmap.put("pk_billmaker", psndocVO.getPrimaryKey());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("billmaker")+" NC中未查询到在用的人员信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("billmaker 计划员信息非空!");
		}
		
		MAP_DEPT=new HashMap<String, DeptVO>();
		
		//请购类型 250613修改参数名
		if(StringUtils.isNotEmpty(reObject.getString("ctrantypeid"))){
			BilltypeVO billtypeVO=(BilltypeVO)this.querySuperVO(BilltypeVO.class, "pk_billtypecode='"+reObject.getString("ctrantypeid")+"'");
			if(null!=billtypeVO){
				Hmap.put("ctrantypeid", billtypeVO.getPrimaryKey());
				Hmap.put("vtrantypecode", billtypeVO.getPk_billtypecode());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("ctrantypeid")+" NC中未查询到在用的请购类型信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("ctrantypeid 请购类型信息非空!");
		}
		//请购子类型 250613修改添加
		if(StringUtils.isNotEmpty(reObject.getString("vdef17"))){
			DefdocVO billtypeVO=(DefdocVO)this.querySuperVO(DefdocVO.class, "code='"+reObject.getString("vdef17")+"' and pk_defdoclist ='1001V1100000000PQKO6'");
			if(null!=billtypeVO){
				Hmap.put("vdef17", billtypeVO.getPk_defdoc());
			}else{
//				ExceptionUtils.wrappBusinessException(reObject.getString("vdef17")+" NC中未查询到在用的请购类型子选项信息!");
				Logger.error(reObject.getString("vdef17")+" NC中未查询到在用的请购类型子选项信息!");
			}
		}else{
//			ExceptionUtils.wrappBusinessException("vdef17 请购类型子选项信息非空!");
			Logger.error(reObject.getString("vdef17")+" NC请购类型子选项信息非空!");
		}
		/*项目*/
		if(StringUtils.isNotEmpty(reObject.getString("chprojectid"))){
//			ProjectHeadVO projectHeadVO=(ProjectHeadVO)this.querySuperVO(ProjectHeadVO.class, "project_code='"+reObject.getString("chprojectid")+"' and dr=0 and enablestate=2 and pk_org='"+Hmap.get("pk_org")+"'");
			
			String 	projectkey = this.allotProject(reObject.getString("chprojectid"), Hmap.get("pk_org"));

//			if(null!=projectHeadVO){
			if(null!=projectkey){
//				Hmap.put("chprojectid", projectHeadVO.getPrimaryKey());
				Hmap.put("chprojectid", projectkey);
			}else{
//				ExceptionUtils.wrappBusinessException(reObject.getString("chprojectid")+" NC中未查询到项目信息!");
				Hmap.put("vbdef3", reObject.getString(reObject.getString("chprojectid")+" NC中未查询到项目信息!"));
			}
		}
		//OA单号 vdef19  2022年7月25日 增加
		if(reObject.containsKey("vdef19") && StringUtils.isNotEmpty(reObject.getString("vdef19"))){
			Hmap.put("vdef19", reObject.getString("vdef19"));
		}else{
			ExceptionUtils.wrappBusinessException("vdef19 OA请求号信息非空!");
		}
		
		//制单人
		if(reObject.containsKey("billmaker") && StringUtils.isNotEmpty(reObject.getString("billmaker"))){
			UserVO userVO=(UserVO)this.querySuperVO(UserVO.class, "user_code='"+reObject.getString("billmaker")+"' and enablestate=2");
			if(null!=userVO){
				Hmap.put("billmaker", userVO.getPrimaryKey());
			}else{
				Hmap.put("billmaker", "NC_USER0000000000000");
//				ExceptionUtils.wrappBusinessException(reObject.getString("billmaker")+" 未查询到有效的制单用户信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("billmaker 制单人信息非空!");
		}
		//审批人 
		UserVO approUserVO=null;
		if(reObject.containsKey("approver") && StringUtils.isNotEmpty(reObject.getString("approver"))){
			approUserVO=(UserVO)this.querySuperVO(UserVO.class, "user_code='"+reObject.getString("approver")+"' and enablestate=2");
			if(null==approUserVO){
				ExceptionUtils.wrappBusinessException(reObject.getString("approver")+" 未查询到有效的审批用户信息!");
			}else{
				Hmap.put("approver", approUserVO.getPrimaryKey());
			}
		}else{
			ExceptionUtils.wrappBusinessException("approver 审批人信息非空!");
		}
		//请购日期 当前日期 250613修改参数名
		if(StringUtils.isNotEmpty(reObject.getString("dmakedate"))){
			Hmap.put("dmakedate", reObject.getString("dmakedate"));
		}else{
			Hmap.put("dmakedate", dateNow.toString());
		}
		
		
		//备注 250613修改参数名
		if(StringUtils.isNotEmpty(reObject.getString("sm"))){
			Hmap.put("vmemo", reObject.getString("sm"));
		}
		
		//表体信息
		JSONArray Bjsons = redataObject.getJSONArray("b");
		if(null==Bjsons || Bjsons.size()==0){
			ExceptionUtils.wrappBusinessException("请购单表体信息非空!");
		}
		MAP_STORDOC=new HashMap<String, StordocVO>();
		MAP_PRO=new HashMap<String, ProjectHeadVO>();
		MAP_PRODUCT=new HashMap<String, DefdocVO>();
		//转译后的表体信息
		List<Map<String,String>> Bmaps=new ArrayList<Map<String,String>>();
		
		for(int i=0;i<Bjsons.size();i++){
			//传入JSON
			JSONObject item = Bjsons.getJSONObject(i);
			
			//转译后的JSON
			Map<String,String> itemMap=new HashMap<String, String>();
			
			//物料信息250613修改参数名,附上机构代码查询物料，无结果再去掉机构条件
			MaterialVO materialVO=null;
			if(StringUtils.isNotEmpty(item.getString("pk_material"))){
				materialVO=(MaterialVO)this.querySuperVO(MaterialVO.class,
						"code='"+item.getString("pk_material")+"' and dr=0 and enablestate=2 and pk_org = (select pk_org from org_orgs where orgtype7 = 'Y' and code ='"+Hmap.get("pk_org")+"')");
				if(null==materialVO){
					materialVO=(MaterialVO)this.querySuperVO(MaterialVO.class, "code='"+item.getString("pk_material")+"' and dr=0 and enablestate=2");
				}
				if(null!=materialVO){
//					materialMap.put(materialVO.getPrimaryKey(), item.getString("pk_material"));
					//物料
					itemMap.put("pk_material", materialVO.getPrimaryKey());
					//主单位
					itemMap.put("cunitid", materialVO.getPk_measdoc());
					itemMap.put("castunitid", materialVO.getPk_measdoc());
					itemMap.put("vchangerate", "1/1");
					
					/*辅单位查询 2022年12月22日修改为主辅计量一致
					MaterialConvertVO convertVO=(MaterialConvertVO) this.querySuperVO(MaterialConvertVO.class, "pk_material='"+materialVO.getPrimaryKey()+"' and dr=0");
					
					if(null!=convertVO){
						//辅单位
						itemMap.put("castunitid", convertVO.getPk_measdoc());
						//换算率
						itemMap.put("vchangerate", convertVO.getMeasrate());
					}else{
						itemMap.put("castunitid", materialVO.getPk_measdoc());
						itemMap.put("vchangerate", "1/1");
					}*/
				}else{
					ExceptionUtils.wrappBusinessException(item.getString("pk_material")+" NC中未查询到在用的物料档案信息!");
				}
			}else{
				ExceptionUtils.wrappBusinessException("material 物料信息非空!");
			}
			
			//请购数量250613修改参数名
			if(StringUtils.isNotEmpty(item.getString("nastnum"))){
				itemMap.put("nastnum", item.getString("nastnum"));
			}else{
				ExceptionUtils.wrappBusinessException(materialVO.getCode()+" "+materialVO.getName()+ " nastnum 请购数量非空!");
			}
			//需求日期
			if(StringUtils.isNotEmpty(item.getString("dreqdate"))){
				itemMap.put("dreqdate", item.getString("dreqdate"));
			}else{
				itemMap.put("dreqdate", dateNow.toString());
			}
			//建议订货日期
			if(StringUtils.isNotEmpty(item.getString("dsuggestdate"))){
				itemMap.put("dsuggestdate", item.getString("dsuggestdate"));
			}else{
				itemMap.put("dsuggestdate", dateNow.toString());
			}
			
			//业务员
			
			if(StringUtils.isNotEmpty(item.getString("pk_employee"))){
				PsndocVO psndocVO=(PsndocVO)this.querySuperVO(PsndocVO.class, "code='"+item.getString("pk_employee")+"' and dr=0 and enablestate=2");
				if(null!=psndocVO){
					itemMap.put("pk_employee", psndocVO.getPk_psndoc());
				}else{
					ExceptionUtils.wrappBusinessException(item.getString("pk_employee")+" 未查询到有效的业务员信息!");
				}
			}

            //仓库
			
			//订单类型 默认：国内采购
			if(StringUtils.isNotEmpty(item.getString("cordertrantypecode"))){
				BilltypeVO billtypeVO=(BilltypeVO)this.querySuperVO(BilltypeVO.class, "pk_billtypecode='"+item.getString("cordertrantypecode")+"'");
				if(null!=billtypeVO){
					itemMap.put("cordertrantypecode", billtypeVO.getPrimaryKey());
				}else{
					//默认:普通采购
					itemMap.put("cordertrantypecode", "0001V110000000002QRI");
				}
			}
			//需求仓库
			if(StringUtils.isNotEmpty(item.getString("pk_reqstor"))){
				StordocVO stordocVO=null;
				if(MAP_STORDOC.containsKey(item.getString("pk_reqstor"))){
					stordocVO=MAP_STORDOC.get(item.getString("pk_reqstor"));
				}else{
					stordocVO=(StordocVO)this.querySuperVO(StordocVO.class, "code='"+item.getString("pk_reqstor")+"' and dr=0 and enablestate=2 and pk_org='"+Hmap.get("pk_org")+"'");
					if(null!=stordocVO){
						MAP_STORDOC.put(item.getString("pk_reqstor"), stordocVO);
					}
				}
				if(null!=stordocVO){
					itemMap.put("pk_reqstor", stordocVO.getPrimaryKey());
				}else{
					ExceptionUtils.wrappBusinessException(item.getString("pk_reqstor")+" NC中未查询到在用的仓库信息!");
				}
			}
			//维修工单号
			if(StringUtils.isNotEmpty(item.getString("vbdef19"))){
				itemMap.put("vbdef19", item.getString("vbdef19"));
			}
			
			//生产厂商 2022-07-27 新增字段
			if(StringUtils.isNotEmpty(item.getString("cproductorid"))){
				DefdocVO defdocVO=null;
				if(MAP_PRODUCT.containsKey(item.getString("cproductorid"))){
					defdocVO=MAP_PRODUCT.get("cproductorid");
				}else{
					defdocVO=(DefdocVO)this.querySuperVO(DefdocVO.class, "code='"+item.getString("cproductorid")+"' and dr=0 and enablestate=2 and PK_DEFDOCLIST='1002ZZ1000000000066Q'");
					if(null!=defdocVO){
						MAP_PRODUCT.put(item.getString("cproductorid"), defdocVO);
					}
				}
				if(null!=defdocVO){
					itemMap.put("cproductorid", defdocVO.getPrimaryKey());
				}else{
					ExceptionUtils.wrappBusinessException(item.getString("cproductorid")+" NC中未查询到生产厂商信息!");
				}
			}
			
			
			//行备注
			if(StringUtils.isNotEmpty(item.getString("vbmemo"))){
				itemMap.put("vbmemo", item.getString("vbmemo"));
			}
			
			//2022年12月16日 增加请购需求日期dreqdate
			if(item.containsKey("dreqdate") && StringUtils.isNotEmpty(item.getString("dreqdate"))){
				itemMap.put("dreqdate", item.getString("dreqdate"));
			}
			
			//250613 增加计划收料日期vbdef7
			if(item.containsKey("vbdef17") && StringUtils.isNotEmpty(item.getString("vbdef17"))){
				itemMap.put("vbdef17", item.getString("vbdef17"));
			}
			
			//250613 增加计划单价ntaxprice
			if(StringUtils.isNotEmpty(item.getString("ntaxprice"))){
				itemMap.put("ntaxprice", item.getString("ntaxprice"));
			}
			//250613 增加计划金额ntaxmny
			if(StringUtils.isNotEmpty(item.getString("ntaxmny"))){
				itemMap.put("ntaxmny", item.getString("ntaxmny"));
			}
			//250613 增加项目任务cprojecttaskid
			if(StringUtils.isNotEmpty(item.getString("cprojecttaskid"))){
				itemMap.put("cprojecttaskid", item.getString("cprojecttaskid"));
			}
			//250613 增加建议供应商pk_suggestsupplier
			if(StringUtils.isNotEmpty(item.getString("pk_suggestsupplier"))){
				itemMap.put("pk_suggestsupplier", item.getString("pk_suggestsupplier"));
			}
			//20250715 增加行号对应OA明细重复物料时单据明细ID匹配问题
			if(StringUtils.isNotEmpty(item.getString("hh"))){
				itemMap.put("hh", item.getString("hh"));
			}
			Bmaps.add(itemMap);
		}
		
		//组装请购单VO数据 
		PraybillVO praybillVO = this.MakeM20(Hmap, Bmaps,materialMap);
		
		//推单保存
		PraybillVO[] saveVos=(PraybillVO[])this.PushBillAction("SAVEBASE", "20", new PraybillVO[]{praybillVO});
		if(null!=saveVos && saveVos.length>0){
			//设定默认审批人
			if(null!=approUserVO){
				InvocationInfoProxy.getInstance().setUserId(approUserVO.getPrimaryKey());
				InvocationInfoProxy.getInstance().setUserCode(approUserVO.getUser_code());
			}
			//自动审批
			PraybillVO[] approveVos = (PraybillVO[])this.PushBillAction("APPROVE", "20", saveVos);
			if(null!=approveVos && approveVos.length>0){
				return approveVos[0];
			}else{
				return saveVos[0];
			}
		}else{
			ExceptionUtils.wrappBusinessException("生成NC请购单异常!");
		}
		return saveVos[0];
	}
	
	/**
	 * 获取项目ID
	 * @param string
	 * @return
	 */
	private String allotProject(String string,String pk_org) {
		//考虑被分配的项目  直接通过baseDAO 实际取数逻辑处理
		StringBuffer sqlbuf = new StringBuffer();
		sqlbuf.append("SELECT DISTINCT bd_project.project_code, ");
		sqlbuf.append(" bd_project.project_name, ");
		sqlbuf.append(" bd_project.project_sh_name, ");
		sqlbuf.append( "bd_project.pk_project, ");
		sqlbuf.append( " bd_project.ordermethod, ");
		sqlbuf.append( " bd_project.planmodel, ");
		sqlbuf.append( " bd_project.planpriority, ");
		sqlbuf.append( " bd_project.pk_workcalendar, ");
		sqlbuf.append( " bd_project.pk_group, ");
		sqlbuf.append( " bd_project.pk_org, ");
		sqlbuf.append( " b.PK_PARTI_ORG , '"+pk_org+"' as org ");
		sqlbuf.append( " FROM bd_project bd_project ");
		sqlbuf.append( " LEFT OUTER JOIN bd_project_b b ");
		sqlbuf.append( " ON bd_project.PK_PROJECT  = b.PK_PROJECT ");
		sqlbuf.append( " WHERE ( bd_project.dr     = 0 ) ");
		sqlbuf.append( " AND ( enablestate         = 2 ) ");
		sqlbuf.append( " AND ( deletestate        IS NULL OR deletestate           <> 1 ) ");
		sqlbuf.append( " and (bd_project.pk_org ='0001V110000000000FH0' or bd_project.pk_org  = '"+pk_org+"' or b.PK_PARTI_ORG  = '"+pk_org+"') ");
		sqlbuf.append( " AND ( b.dr   = 0 ) ");
		sqlbuf.append( " and bd_project.project_code ='"+string+"' ");
		sqlbuf.append( " ORDER BY project_code ");
		
		BaseDAO dao = new BaseDAO();
		try {
			dao.executeQuery(sqlbuf.toString(), new ResultSetProcessor() {
				
				@Override
				public Object handleResultSet(ResultSet arg0) throws SQLException {
					List<String> project_org = new ArrayList<String>();
					List<String> project_rarti = new ArrayList<String>();
					List<String>  project_group = new ArrayList<String>();
					while (arg0.next()) {
						String pk_org_db = arg0.getString("pk_org"); //所属公司
						String pk_parti_org = arg0.getString("pk_parti_org");//受分配公司
						String org = arg0.getString("org");//查询公司
						if(org.equals(pk_org_db)){
							//属于本公司的项目，考虑到本公司项目不会存在项目号一致情况，可直接返回结果
//							project_org.add(arg0.getString("pk_project"));  //
							return arg0.getString("pk_project");
							
						}else if("0001V110000000000FH0".equals(pk_org_db) ){
							//集团项目
							project_group.add(arg0.getString("pk_project"));
							if(project_group.size() > 1){
								throw new SQLException("集团项目号有多个"+arg0.getString("project_code"));
							}
						}else if(org.equals(pk_parti_org)){
							//其它分配来的项目
							project_rarti.add(arg0.getString("pk_project"));
						}
					}
//					if (null != project_org && project_org.size()==1 ){
//						return project_org.get(0);
//					} else
					if (null != project_group && project_group.size() == 1){
						return project_group.get(0);
					}else if(null != project_rarti && project_rarti.size()>0){
						return project_rarti.get(0);
					}
					return null;
				}
			}) ;
		} catch (DAOException e) {
			
			ExceptionUtils.wrappBusinessException("查询项目时出现异常:"+e.getMessage());
		}
		return null;
	}

	/**
	 * 组装请购单
	 * @return
	 */
	public  PraybillVO MakeM20(Map<String,String> h,List<Map<String,String>> bList,Map materialMap){
		PraybillVO praybillVO=new PraybillVO();
		
		//表头VO
		PraybillHeaderVO hVO=new PraybillHeaderVO();
		hVO.setPk_group(PK_GROUP);
		hVO.setBdirecttransit(UFBoolean.FALSE);
		hVO.setBislatest(UFBoolean.TRUE);
		hVO.setBsctype(UFBoolean.FALSE);
		hVO.setCcurrencyid(CCURRENCYID);
		//单据日期
		hVO.setDbilldate(new UFDate(h.get("dmakedate")));
		hVO.setFbillstatus(0);
		//7=手工录入
		hVO.setFpraysource(7);
		hVO.setNversion(1);
		hVO.setPk_org(h.get("pk_org"));
		hVO.setPk_org_v(h.get("pk_org_v"));
		//部门信息
//		hVO.setPk_plandept(h.get("pk_plandept"));
//		hVO.setPk_plandept_v(h.get("pk_plandept_v"));
		//计划员
		hVO.setPk_planpsn(h.get("pk_billmaker"));
		hVO.setStatus(VOStatus.NEW);
		//单据类型
		hVO.setCtrantypeid(h.get("ctrantypeid"));
		hVO.setVdef17(h.get("vdef17"));
		hVO.setVtrantypecode(h.get("vtrantypecode"));
		//vdef19 oa单号2022-07-25
		if(h.containsKey("vdef19")){
			hVO.setVdef19(h.get("vdef19"));
		}
		//备注
		if(h.containsKey("vmemo")){
			hVO.setVmemo(h.get("vmemo"));
		}
		//制单人
		hVO.setBillmaker(h.get("billmaker"));
		//审批人
		hVO.setApprover(h.get("approver"));
		hVO.setVdef18(h.get("vdef18"));
		//表体VOs
		List<PraybillItemVO> listItemVOs=new ArrayList<PraybillItemVO>();
		ScaleUtils scale = new ScaleUtils(hVO.getPk_group());
		//行号
		int rowno=10;
		for (Map<String, String> map : bList) {
			PraybillItemVO bVo=new PraybillItemVO();
			bVo.setStatus(VOStatus.NEW);
			bVo.setBcanpurchaseorgedit(UFBoolean.TRUE);
			bVo.setBisgensaorder(UFBoolean.FALSE);
			bVo.setBpublishtoec(UFBoolean.FALSE);
			bVo.setBrowclose(UFBoolean.FALSE);
			bVo.setCastunitid(map.get("castunitid"));
			bVo.setCordertrantypecode(map.get("cordertrantypecode"));
			bVo.setCrowno(rowno+"");
			//20250715 增加行号对应OA明细重复物料时单据明细ID匹配问题
			materialMap.put(bVo.getCrowno(), map.get("hh"));
			bVo.setCunitid(map.get("cunitid"));
			bVo.setDbilldate(hVO.getDbilldate());
			if(map.containsKey("dreqdate")){
				bVo.setDreqdate(new UFDate(map.get("dreqdate")));
			}else{
				bVo.setDreqdate(hVO.getDbilldate());
			}
			if(map.containsKey("dsuggestdate")){
				bVo.setDsuggestdate(new UFDate(map.get("dsuggestdate")));
			}else{
				bVo.setDsuggestdate(hVO.getDbilldate());
			}
			bVo.setNaccumulatenum(UFDouble.ZERO_DBL);
			bVo.setNnum(new UFDouble(map.get("nastnum")));
			//250613 增加字段
//			bVo.setNastnum(new UFDouble(map.get("nastnum")));
			bVo.setVbdef17(map.get("vbdef17"));
			bVo.setNtaxprice(new UFDouble(map.get("ntaxprice")));
			bVo.setVchangerate(map.get("vchangerate"));
//			bVo.setPk_suggestsupplier(map.get("pk_suggestsupplier"));
			bVo.setCprojecttaskid(map.get("cprojecttaskid"));
			//计算辅数量
			UFDouble nastnum = HslParseUtil.hslDivUFDouble(bVo.getVchangerate(), bVo.getNnum());
			bVo.setNastnum(scale.adjustNumScale(nastnum,bVo.getCastunitid()));
			bVo.setNtaxmny(new UFDouble(map.get("ntaxmny")));
			bVo.setNgenct(0);
			bVo.setNpriceauditbill(0);
			bVo.setNquotebill(0);
			bVo.setPk_group(hVO.getPk_group());
			bVo.setPk_material(map.get("pk_material"));
			bVo.setPk_srcmaterial(bVo.getPk_material());
			bVo.setPk_org(hVO.getPk_org());
			bVo.setPk_org_v(hVO.getPk_org_v());
			bVo.setPk_purchaseorg(h.get("pk_purchaseorg"));
			bVo.setPk_purchaseorg_v(h.get("pk_purchaseorg_v"));
//			bVo.setPk_reqdept(hVO.getPk_plandept());
//			bVo.setPk_reqdept_v(hVO.getPk_plandept_v());
			bVo.setPk_reqstoorg(h.get("vbdef18"));
			bVo.setPk_reqstoorg_v(h.get("vbdef18_v"));
			bVo.setVbdef3(h.get("vbdef3"));
			bVo.setPk_reqdept(map.get("pk_reqdept"));
			bVo.setPk_reqdept_v(map.get("pk_reqdept_v"));
			bVo.setPk_reqstor(map.get("pk_reqstor"));
			//项目信息
			if(h.containsKey("chprojectid")){
				bVo.setCprojectid(h.get("chprojectid"));
			}
			//维修工单号
			if(map.containsKey("vbdef19")){
				bVo.setVbdef19(map.get("vbdef19"));
			}
			//生产厂商 2022-07-27 新增
			if(map.containsKey("cproductorid")){
				bVo.setCproductorid(map.get("cproductorid"));
			}
			//请购需求日期2022年12月16日
			if(map.containsKey("dreqdate")){
				bVo.setDreqdate(new UFDate(map.get("dreqdate")));
			}
			bVo.setBisarrange(UFBoolean.FALSE);
			
			//行备注2022年12月21日
			if(map.containsKey("vbmemo")){
				bVo.setVbmemo(map.get("vbmemo"));
			}
			// 2023-03-23 业务员
			//维修工单号
			if(map.containsKey("pk_employee")){
				bVo.setPk_employee(map.get("pk_employee"));
			}
			rowno+=10;
			
			listItemVOs.add(bVo);
		}
		
		praybillVO.setHVO(hVO);
		praybillVO.setBVO(listItemVOs.toArray(new PraybillItemVO[listItemVOs.size()]));
		
		return praybillVO;
	} 
	
	
	/**
	 * @param action
	 * @param billtype
	 * @param srcBills
	 * @return
	 * @throws BusinessException
	 * 推单动作 
	 * @throws nc.vo.pub.BusinessException 
	 */
	private Object[] PushBillAction(String action,String billtype,AbstractBill[] srcBills) throws BusinessException{
		//进行推单操作
		IPFBusiAction busiAction=(IPFBusiAction)NCLocator.getInstance().lookup(IPFBusiAction.class);
		return busiAction.processBatch(action, billtype, srcBills, null, null,null);
	}
	
	/**
	 * 根据查询条件查询档案信息
	 * @param srcClass
	 * @param where
	 * @return
	 */
	private HYPubBO pubBO;
	private SuperVO querySuperVO(Class srcClass,String where){
		if(null==pubBO){
			pubBO=new HYPubBO();
		}
		try {
			SuperVO[] superVOs=(SuperVO[])pubBO.queryByCondition(srcClass, where);
			if(null!=superVOs && superVOs.length>0){
				return superVOs[0];
			}
		} catch (UifException e) {
			e.printStackTrace();
		}
		return null;
	}
}
