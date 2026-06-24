package nc.bs.jzyy.sys.thlims.pucheck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.ThLimsCheckRule;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.bs.logging.Log;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.sm.UserVO;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP采购到货单调用LIMS报检接口
 * @author liyf
 */
public class PucheckUpSender extends AbstractSender4ThLims {
	
	//中间平台参数名
	public static final String functype = "nc_th_inspection";
	ArriveItemVO[] bvos = null;
	ArriveVO vo = null;
	
	/*
	 * {					
  		"ID":"**99***",
		"succeed":"N",
		"Lims_billCode":"*******",
		"message":"检测目的不能为空"
	}
	 * */
	public Object afterSend(JSONObject response) throws Exception {
		String succeed = response.getString("succeed");
		limsLogVO.setResp(succeed);
		if (!"Y".equals(succeed)) {
			Log.getInstance("泰华Lims到货单请验回传").error(response.toJSONString());
			throw new BusinessException(response.getString("message"));
		} else {
			/*
			 * 3. 因为是合并报检 所以要根据物料+批次号 重新查询表体信息
			 * */
			String syncid = response.getString("ID");
			List<ArriveItemVO> updateList=new ArrayList<ArriveItemVO>();
			StringBuffer q_sql=new StringBuffer("pk_arriveorder='"+vo.getPrimaryKey()+"' and dr=0 and pk_material=");
			for (ArriveItemVO bvo : bvos) {
				q_sql=q_sql.append("'"+bvo.getPk_material()+"' ");
				if(StringUtils.isNotEmpty(bvo.getPk_batchcode())){
					q_sql=q_sql.append(" and pk_batchcode='"+bvo.getPk_batchcode()+"'");
				}
				ArriveItemVO[] arriveItemVOs=(ArriveItemVO[])this.getPubBO().queryByCondition(ArriveItemVO.class, q_sql.toString());
				if(null!=arriveItemVOs && arriveItemVOs.length>0){
					for (ArriveItemVO arriveItemVO : arriveItemVOs) {
						
						//LIMS检验请求ID
						arriveItemVO.setVbdef11(response.getString("Lims_billcode"));
						//ERP同步号
						arriveItemVO.setVbdef12(syncid);
						
						updateList.add(arriveItemVO);
					}
				}
				/**
				 * 1. 更新批次状态 在检验
				 * 
				 * 2. 批次号档案增加字段【报检次数 vdef4】字段默认为空，取值向LIMS请验时NVL(报检次数，0)；执行LIMS接口调用后，打次号档案报检次数加1
				 */
				String batch_sql="update scm_batchcode set Binqc='Y' where pk_batchcode='"+bvo.getPk_batchcode()+"'";
				if(StringUtils.isNotEmpty(bvo.getPk_batchcode())){
					batch_sql="update scm_batchcode set Binqc='Y',vdef4=to_number(decode(vdef4,'~',0,null,0,vdef4))+1  where pk_batchcode='"+bvo.getPk_batchcode()+"'";
				}
				getDao().executeUpdate(batch_sql);
			}
			ArriveItemVO[] updateArry = updateList.toArray(new ArriveItemVO[updateList.size()]);
			//1. 更新同步标识
			new VOUpdate().update(updateArry, new String[] {"vbdef11", "vbdef12" });
			ArriveVO newvo = (ArriveVO) vo.clone();
			newvo.setBVO(updateArry);
			//3. 回写报检数量
			WriteBackArriveCheckNumRule rule2 = new WriteBackArriveCheckNumRule(true);
			rule2.process(new ArriveVO[] { newvo });
		}
		return response;
	}


	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}

	@Override
	public void init() throws Exception {
		this.vo=(ArriveVO)this.getParam();
		this.bvos=this.vo.getBVO();
		limsLogVO=new THLimsLogVO();
	}

	@Override
	protected String getSendJson() throws Exception {
		ArriveVO newvo = (ArriveVO) vo.clone();
		newvo.setBVO(bvos);
		String jsonObj = changeTOJson(newvo);
		Log.getInstance("检验Lims").error("检验Lims："+jsonObj);
		return jsonObj;
	}
	
	/*
	 * 组装传入参数
	 * */
	private String changeTOJson(ArriveVO vo) throws BusinessException {
		ArriveItemVO[] bvos = vo.getBVO();
		Set<String> pk_marterial_vs = new HashSet<String>();
		Set<String> pk_defdocs = new HashSet<String>();
		Set<String> pk_measdoc = new HashSet<String>();
		for (ArriveItemVO bvo : bvos) {
			if (!StringUtil.isEmpty(bvo.getPk_srcmaterial())) {
				pk_marterial_vs.add(bvo.getPk_srcmaterial());
			}

			if (!StringUtil.isEmpty(bvo.getCproductorid())) {
				pk_defdocs.add(bvo.getCproductorid());
			}

			if (!StringUtil.isEmpty(bvo.getCunitid())) {
				pk_measdoc.add(bvo.getCunitid());
			}

			if (!StringUtil.isEmpty(bvo.getCastunitid())) {
				pk_measdoc.add(bvo.getCastunitid());
			}
		}
		
		Map<String, MaterialVO> mmap = getMaterialVOMap(pk_marterial_vs);

		Map<String, DefdocVO> dmap = getDefdocVOMap(pk_defdocs);

		Map<String, MeasdocVO> memap = getMeasdocVOMap(pk_measdoc);

		Set<String> cvendorids = new HashSet<String>();
		cvendorids.add(vo.getHVO().getPk_supplier());
		//Map<String, SupplierVO> supmap = getSupplierVO(cvendorids);

		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());

		OrgVO orgvo = getOrgVO(vo.getHVO().getPk_org());

		OrgVO deptvo = getOrgVO(vo.getHVO().getPk_dept());

		Map<String, SupplierqualityHVO> gmap = getGradeInfo(cvendorids, vo.getHVO().getPk_group(), vo.getHVO().getPk_org());

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("functype", functype);
		JSONObject hjsonObject = new JSONObject();
		/**
		 * 1=采购报检
		   2=生产完工报检
		   3=库存报检（复验）
		   4=中控检验
		   5=清洁批报检
		 */
		int ctrantype =1;
		UFDateTime ufDateTime=new UFDateTime();
		//String sysFlowNO = getSysFlowNo();
		String sysFlowNO="CG28_"+vo.getHVO().getVbillcode()+"_"+ufDateTime.toString(TimeZone.getTimeZone("GMT+08:00"),new SimpleDateFormat("yyyyMMddhhmmss"));
		hjsonObject.put("ID",sysFlowNO);// 同步号
		limsLogVO.setSys_id(sysFlowNO);
		hjsonObject.put("ctrantype", ctrantype);// 业务类型
		limsLogVO.setCtrantype(ctrantype+"");
		hjsonObject.put("ts", ufDateTime.toString());// 报检时间
		limsLogVO.setQctime(ufDateTime.toString());
		limsLogVO.setTs(ufDateTime.toString());
		if (uservo != null) {
			hjsonObject.put("usercode", uservo.getUser_code());// 报检人编码
			hjsonObject.put("username", uservo.getUser_name());// 报检人名称
			
			limsLogVO.setCuserid(AppContext.getInstance().getPkUser());
			limsLogVO.setUsercode(uservo.getUser_code());
			limsLogVO.setUsername(uservo.getUser_name());
		}
		if (deptvo != null) {
			hjsonObject.put("applydept_code", deptvo.getCode());// 报检部门编码
			hjsonObject.put("applydept_name", deptvo.getName());// 报检部门名称
		}
		hjsonObject.put("productionLline", "");// 产线
		if (orgvo != null) {
			hjsonObject.put("org_code", orgvo.getCode());// 所属公司编码
			hjsonObject.put("org_name", orgvo.getName());// 所属公司
			
			limsLogVO.setPk_org(vo.getHVO().getPk_org());
			limsLogVO.setPk_org_v(vo.getHVO().getPk_org_v());
		}
		hjsonObject.put("group", "G");// 所属集团
		
		//Lims 支持单行报检 只取首行
		ArriveItemVO bvo=bvos[0];

		hjsonObject.put("vbillcode", vo.getHVO().getVbillcode());// 来源单据号
		hjsonObject.put("bill_id", bvo.getPk_arriveorder());// 来源单据ID
		hjsonObject.put("billitem_id", bvo.getPk_arriveorder_b());// 来源单据明细ID
		
		limsLogVO.setVbillid(vo.getPrimaryKey());
		limsLogVO.setVbill_bid(bvo.getPrimaryKey());
		limsLogVO.setVbillcode(vo.getHVO().getVbillcode());
		
		MaterialVO matervo = mmap.get(bvo.getPk_srcmaterial());

		hjsonObject.put("material_code", matervo.getCode());// 物料编码
		hjsonObject.put("material_name", matervo.getName());// 物料名称
		hjsonObject.put("materialspec", matervo.getMaterialspec());// 规格
		hjsonObject.put("materialtype", matervo.getMaterialtype());// 型号
		
		hjsonObject.put("vbatchcode", bvo.getVbatchcode());// 批次号
		hjsonObject.put("vvendbatchcode", bvo.getBc_vvendbatchcode());// 供应商批次号
		
		if (bvo.getDproducedate() != null){
			hjsonObject.put("dproducedate", bvo.getDproducedate().toString());// 生产日期
		}

		if (bvo.getDinvaliddate() != null){
			hjsonObject.put("dvalidate", bvo.getDinvaliddate().toString());// 失效/复测日期
		}
		//供应商
		if(null!=vo.getHVO().getPk_supplier()){
			SupplierVO supplierVO = (SupplierVO) this.getPubBO().queryByPrimaryKey(SupplierVO.class, vo.getHVO().getPk_supplier());
			if(null!=supplierVO){
				hjsonObject.put("cvendor_code", supplierVO.getCode());
				hjsonObject.put("cvendor_name", supplierVO.getName());
			}
		}
		
		//生产商
		hjsonObject.put("cproductor_code", "");
		hjsonObject.put("cproductor_name", "");
		hjsonObject.put("supgrade","N/A");
		DefdocVO defvo = dmap.get(bvo.getCproductorid());
		if (defvo != null) {
			// 生产厂商编码
			hjsonObject.put("cproductor_code", defvo.getCode());
			hjsonObject.put("cproductor_name", defvo.getName());
			String key = bvo.getPk_material() + "&" + bvo.getCproductorid();
			SupplierqualityHVO suppliervo = gmap.get(key);
//			生产商等级	u_manufacturer_level 1=不批准2=批准3=认证4=维持仅用于类型1，2，3
			if (null!=suppliervo){
				hjsonObject.put("supgrade",suppliervo.getPk_grade_info());// 供应商/生产商等级
				hjsonObject.put("supgradename",suppliervo.getName());// 供应商/生产商等级
			}
		}
		
		MeasdocVO mesvo = memap.get(bvo.getCunitid());
		if (null!=mesvo) {
			hjsonObject.put("cunit_name", mesvo.getName());// 主单位名称
		}
		hjsonObject.put("num", bvo.getNnum().toDouble());// 报检主数量
		
		//辅数量===默认为件
		 hjsonObject.put("castunit","件");// 单位
		 /*mesvo = memap.get(bvo.getCastunitid());
		 if (mesvo != null) {
			 hjsonObject.put("castunit", mesvo.getName());// 单位
		 }*/
		 //容器数量==表体def18
		 //容器数量==表体def8 所对应自顶档案的def2 2022年12月30日
		 //如果包装规格是VAR 则默认传1 2023年1月9日
		 hjsonObject.put("nastnum",1);
		//包装数量
		hjsonObject.put("nastnum",1);
		ThLimsCheckRule limsCheckRule = new ThLimsCheckRule();
		UFDouble npacknum = limsCheckRule.isVar(bvo.getVfree2(), bvo.getNastnum());
		hjsonObject.put("nastnum",npacknum.toDouble()); 
	
		 //原料等级==物料档案def12 
		 hjsonObject.put("yldj", "");
		 MaterialVO materialVO=(MaterialVO)this.getPubBO().queryByPrimaryKey(MaterialVO.class, matervo.getPk_material());
		 if(null!=materialVO && StringUtils.isNotEmpty(materialVO.getDef12())){
			 DefdocVO defdocVO=(DefdocVO) this.getPubBO().queryByPrimaryKey(DefdocVO.class, materialVO.getDef12());
			 if(null!=defdocVO){
				 hjsonObject.put("yldj",defdocVO.getName());
			 }
		 }
		 //查询报检次数
		 BatchcodeVO[] batchcodeVOs=(BatchcodeVO[])this.getPubBO().queryByCondition(BatchcodeVO.class, "vbatchcode='"+bvo.getVbatchcode()+"' and cmaterialoid='"+bvo.getPk_material()+"'");
		 hjsonObject.put("jycs", 0);//检验次数
		 hjsonObject.put("sffj", "N");//是否复检
		 if(null!=batchcodeVOs && batchcodeVOs.length>0){
			 if(null!=batchcodeVOs[0].getVdef4()){
				 Integer times = Integer.getInteger(batchcodeVOs[0].getVdef4());
				 if(null!=times && times>0){
					 hjsonObject.put("jycs", times);//检验次数
					 hjsonObject.put("sffj", "Y");//是否复检
				 }
			 }
		 }
		 //供应商批次及数量 2023年3月17日
		 hjsonObject.put("vvendbatchcode", bvo.getBc_vvendbatchcode());
		 hjsonObject.put("vvendbatchnum", bvo.getBc_vdef1());
		 limsLogVO.setQccount(hjsonObject.getString("jycs"));
		 hjsonObject.put("def1", "");// 预留1
		 hjsonObject.put("def2", "");// 预留2
		 hjsonObject.put("def3", "");// 预留3
		 hjsonObject.put("def4", "");// 预留4
		 hjsonObject.put("def5", "");// 预留5
		 
		
		//第三层
		Map<String,Object> data  = getHeadData();
        data.put("billCode",vo.getHVO().getVbillcode());
        data.put("targetrule","LIMS_CHECK_"+ctrantype);
        data.put("data",hjsonObject); 
        
        //组装
	  	String reJson = JSON.toJSONString(data);
	  	return  reJson;
	}
	


}
