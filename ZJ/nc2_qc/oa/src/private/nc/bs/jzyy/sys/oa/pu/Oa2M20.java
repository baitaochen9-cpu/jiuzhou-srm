package nc.bs.jzyy.sys.oa.pu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.pf.IPFBusiAction;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialConvertVO;
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
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Oa2M20 extends AbstracAdapter4Ext {
	
	//默认集团
	public static final String PK_GROUP="0001V110000000000FH0";
	
	//默认制单人
	public static final String DEF_USER_CODE="yf01";
	public static final String DEF_USER_ID="1001V8100000000BQ04T";
	
	//默认币种
	public static final String CCURRENCYID="1002Z0100000000001K1";
	
	@Override
	public JSONObject sys(Object billvo) throws BusinessException, DAOException {
		PraybillVO praybillVO = this.M20(billvo);
		JSONObject data = new JSONObject();
		String re_mess="生成请购单成功";
		if(null!=praybillVO){
			data.put("code", praybillVO.getHVO().getVbillcode());
			data.put("pk", praybillVO.getPrimaryKey());
			data.put("billstatus", praybillVO.getHVO().getFbillstatus());
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
	
	public PraybillVO M20(Object jsondata) throws BusinessException{
		//传入参数转换JSON对象
		JSONObject redataObject=(JSONObject)JSON.toJSON(jsondata);
		//JSONObject redataObject=reFullObject.getJSONObject("data");
		//表头信息
		JSONObject reObject=redataObject.getJSONObject("h");
		
		//转译后的表头信息
		Map<String,String> Hmap=new HashMap<String, String>();
		
		//当前日期
		UFDate dateNow=new UFDate();
		//组织信息
		if(StringUtils.isNotEmpty(reObject.getString("org"))){
			StockOrgVO orgVO=(StockOrgVO)this.querySuperVO(StockOrgVO.class, "code='"+reObject.getString("org")+"' and dr=0 and enablestate=2");
			if(null!=orgVO){
				Hmap.put("pk_org", orgVO.getPrimaryKey());
				Hmap.put("pk_org_v", orgVO.getPk_vid());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("org")+" NC中未查询到在用的相关组织信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("org 组织信息非空!");
		}
		
		//计划员信息
		if(StringUtils.isNotEmpty(reObject.getString("planpsn"))){
			PsndocVO psndocVO=(PsndocVO)this.querySuperVO(PsndocVO.class, "code='"+reObject.getString("planpsn")+"' and dr=0 and enablestate=2");
			if(null!=psndocVO){
				Hmap.put("pk_planpsn", psndocVO.getPrimaryKey());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("planpsn")+" NC中未查询到在用的人员信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("planpsn 计划员信息非空!");
		}
		
		MAP_DEPT=new HashMap<String, DeptVO>();
		//部门信息
		if(StringUtils.isNotEmpty(reObject.getString("plandept"))){
			/*DeptVO deptVO=(DeptVO)this.querySuperVO(DeptVO.class, "code='"+reObject.getString("plandept")+"' and dr=0 and enablestate=2 and pk_org='"+Hmap.get("pk_org")+"'");
			if(null!=deptVO){
				Hmap.put("pk_plandept", deptVO.getPrimaryKey());
				Hmap.put("pk_plandept_v", deptVO.getPk_vid());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("plandept")+" NC中未查询到在用的部门信息!");
			}*/
			
			DeptVO deptVO=null;
			if(MAP_DEPT.containsKey(reObject.getString("plandept"))){
				deptVO=MAP_DEPT.get(reObject.getString("plandept"));
			}else{
				//1. 根据OA编码查询对照关系
				BDContraDtlVO dtlVO=(BDContraDtlVO)this.querySuperVO(BDContraDtlVO.class, "EXSYSVAL='"+reObject.getString("plandept")+"'");
				if(null!=dtlVO){
					deptVO=(DeptVO)this.querySuperVO(DeptVO.class, "code='"+dtlVO.getBdcode()+"' and dr=0 and enablestate=2 and pk_org='"+Hmap.get("pk_org")+"'");
					if(null!=deptVO){
						MAP_DEPT.put(reObject.getString("pk_reqdept"), deptVO);
					}
				}else{
					ExceptionUtils.wrappBusinessException(reObject.getString("plandept")+" NC未匹配到对照关系信息!");
				}
			}
			if(null!=deptVO){
				Hmap.put("pk_plandept", deptVO.getPrimaryKey());
				Hmap.put("pk_plandept_v", deptVO.getPk_vid());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("plandept")+" NC中未查询到在用的部门信息!");
			}
			
		}else{
			ExceptionUtils.wrappBusinessException("plandept 请购部门信息非空!");
		}
		
		//请购类型
		if(StringUtils.isNotEmpty(reObject.getString("vtrantypecode"))){
			BilltypeVO billtypeVO=(BilltypeVO)this.querySuperVO(BilltypeVO.class, "pk_billtypecode='"+reObject.getString("vtrantypecode")+"'");
			if(null!=billtypeVO){
				Hmap.put("ctrantypeid", billtypeVO.getPrimaryKey());
				Hmap.put("vtrantypecode", billtypeVO.getPk_billtypecode());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("vtrantypecode")+" NC中未查询到在用的请购类型信息!");
			}
		}else{
			ExceptionUtils.wrappBusinessException("vtrantypecode 请购类型信息非空!");
		}
		/*项目*/
		if(StringUtils.isNotEmpty(reObject.getString("chprojectid"))){
			ProjectHeadVO projectHeadVO=(ProjectHeadVO)this.querySuperVO(ProjectHeadVO.class, "project_code='"+reObject.getString("chprojectid")+"' and dr=0 and enablestate=2 and pk_org='"+Hmap.get("pk_org")+"'");
			if(null!=projectHeadVO){
				Hmap.put("chprojectid", projectHeadVO.getPrimaryKey());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("chprojectid")+" NC中未查询到项目信息!");
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
				//设置创建人
				InvocationInfoProxy.getInstance().setUserId(userVO.getPrimaryKey());
			}else{
				ExceptionUtils.wrappBusinessException(reObject.getString("billmaker")+" 未查询到有效的制单用户信息!");
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
		//请购日期 当前日期
		if(StringUtils.isNotEmpty(reObject.getString("dbilldate"))){
			Hmap.put("dbilldate", reObject.getString("dbilldate"));
		}else{
			Hmap.put("dbilldate", dateNow.toString());
		}
		
		
		//备注
		if(StringUtils.isNotEmpty(reObject.getString("memo"))){
			Hmap.put("vmemo", reObject.getString("memo"));
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
			
			//物料信息
			MaterialVO materialVO=null;
			if(StringUtils.isNotEmpty(item.getString("material"))){
				materialVO=(MaterialVO)this.querySuperVO(MaterialVO.class, "code='"+item.getString("material")+"' and dr=0 and enablestate=2");
				if(null!=materialVO){
					//物料
					itemMap.put("pk_material", materialVO.getPrimaryKey());
					//主单位
					itemMap.put("cunitid", materialVO.getPk_measdoc());
					//辅单位查询
					MaterialConvertVO convertVO=(MaterialConvertVO) this.querySuperVO(MaterialConvertVO.class, "pk_material='"+materialVO.getPrimaryKey()+"' and dr=0");
					
					if(null!=convertVO){
						//辅单位
						itemMap.put("castunitid", convertVO.getPk_measdoc());
						//换算率
						itemMap.put("vchangerate", convertVO.getMeasrate());
					}else{
						itemMap.put("castunitid", materialVO.getPk_measdoc());
						itemMap.put("vchangerate", "1/1");
					}
				}else{
					ExceptionUtils.wrappBusinessException(item.getString("material")+" NC中未查询到在用的物料档案信息!");
				}
			}else{
				ExceptionUtils.wrappBusinessException("material 物料信息非空!");
			}
			
			//请购数量
			if(StringUtils.isNotEmpty(item.getString("nnum"))){
				itemMap.put("nnum", item.getString("nnum"));
			}else{
				ExceptionUtils.wrappBusinessException(materialVO.getCode()+" "+materialVO.getName()+ " nnum 请购数量非空!");
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
			/*
			 * 需求部门
			 * 2022-07-27  调整：
			 * OA传入OA部门编码，通过基础u档案对照 转换为NC部门档案
			 */
			if(StringUtils.isNotEmpty(item.getString("pk_reqdept"))){
				DeptVO deptVO=null;
				if(MAP_DEPT.containsKey(item.getString("pk_reqdept"))){
					deptVO=MAP_DEPT.get(item.getString("pk_reqdept"));
				}else{
					//1. 根据OA编码查询对照关系
					BDContraDtlVO dtlVO=(BDContraDtlVO)this.querySuperVO(BDContraDtlVO.class, "EXSYSVAL='"+item.getString("pk_reqdept")+"'");
					if(null!=dtlVO){
						deptVO=(DeptVO)this.querySuperVO(DeptVO.class, "code='"+dtlVO.getBdcode()+"' and dr=0 and enablestate=2 and pk_org='"+Hmap.get("pk_org")+"'");
						if(null!=deptVO){
							MAP_DEPT.put(item.getString("pk_reqdept"), deptVO);
						}
					}else{
						ExceptionUtils.wrappBusinessException(item.getString("pk_reqdept")+" NC未匹配到对照关系信息!");
					}
				}
				if(null!=deptVO){
					itemMap.put("pk_reqdept", deptVO.getPrimaryKey());
					itemMap.put("pk_reqdept_v", deptVO.getPk_vid());
				}else{
					ExceptionUtils.wrappBusinessException(item.getString("pk_reqdept")+" NC中未查询到在用的部门信息!");
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
			
			/*项目信息
			if(StringUtils.isNotEmpty(item.getString("cprojectid"))){
				ProjectHeadVO projectHeadVO=null;
				if(MAP_PRO.containsKey(item.getString("cprojectid"))){
					projectHeadVO=MAP_PRO.get("cprojectid");
				}else{
					projectHeadVO=(ProjectHeadVO)this.querySuperVO(ProjectHeadVO.class, "project_code='"+item.getString("cprojectid")+"' and dr=0 and enablestate=2 and pk_org='"+Hmap.get("pk_org")+"'");
				}
				if(null!=projectHeadVO){
					itemMap.put("cprojectid", projectHeadVO.getPrimaryKey());
				}else{
					ExceptionUtils.wrappBusinessException(item.getString("chprojectid")+" NC中未查询到项目信息!");
				}
			}*/
			
			//行备注
			if(StringUtils.isNotEmpty(item.getString("vbmemo"))){
				itemMap.put("vbmemo", item.getString("vbmemo"));
			}
			Bmaps.add(itemMap);
		}
		
		//组装请购单VO数据 
		PraybillVO praybillVO = this.MakeM20(Hmap, Bmaps);
		
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
	 * 组装请购单
	 * @return
	 */
	public  PraybillVO MakeM20(Map<String,String> h,List<Map<String,String>> bList){
		PraybillVO praybillVO=new PraybillVO();
		
		//表头VO
		PraybillHeaderVO hVO=new PraybillHeaderVO();
		hVO.setPk_group(PK_GROUP);
		hVO.setBdirecttransit(UFBoolean.FALSE);
		hVO.setBislatest(UFBoolean.TRUE);
		hVO.setBsctype(UFBoolean.FALSE);
		hVO.setCcurrencyid(CCURRENCYID);
		//单据日期
		hVO.setDbilldate(new UFDate(h.get("dbilldate")));
		hVO.setFbillstatus(0);
		//7=手工录入
		hVO.setFpraysource(7);
		hVO.setNversion(1);
		hVO.setPk_org(h.get("pk_org"));
		hVO.setPk_org_v(h.get("pk_org_v"));
		//部门信息
		hVO.setPk_plandept(h.get("pk_plandept"));
		hVO.setPk_plandept_v(h.get("pk_plandept_v"));
		//计划员
		hVO.setPk_planpsn(h.get("pk_planpsn"));
		hVO.setStatus(VOStatus.NEW);
		//单据类型
		hVO.setCtrantypeid(h.get("ctrantypeid"));
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
			bVo.setNnum(new UFDouble(map.get("nnum")));
			bVo.setVchangerate(map.get("vchangerate"));
			//计算辅数量
			UFDouble nastnum = HslParseUtil.hslDivUFDouble(bVo.getVchangerate(), bVo.getNnum());
			bVo.setNastnum(scale.adjustNumScale(nastnum,bVo.getCastunitid()));
			bVo.setVbmemo(map.get("vbmemo"));
			bVo.setNgenct(0);
			bVo.setNpriceauditbill(0);
			bVo.setNquotebill(0);
			bVo.setPk_group(hVO.getPk_group());
			bVo.setPk_material(map.get("pk_material"));
			bVo.setPk_srcmaterial(bVo.getPk_material());
			bVo.setPk_org(hVO.getPk_org());
			bVo.setPk_org_v(hVO.getPk_org_v());
			bVo.setPk_purchaseorg(hVO.getPk_org());
			bVo.setPk_purchaseorg_v(hVO.getPk_org_v());
			bVo.setPk_reqdept(hVO.getPk_plandept());
			bVo.setPk_reqdept_v(hVO.getPk_plandept_v());
			bVo.setPk_reqstoorg(hVO.getPk_org());
			bVo.setPk_reqstoorg_v(hVO.getPk_org_v());
			bVo.setPk_employee(map.get("pk_employee"));
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
			
			bVo.setBisarrange(UFBoolean.FALSE);
			
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
