package nc.bs.jzyy.sys.lims.pucheck;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
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
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP采购到货单调用LIMS报检接口
 * 
 * @author liyf
 * 
 */
public class PucheckUpSender extends AbstractSender4LIMS {

	public static final String functype = "nc_inspection";
	ArriveItemVO[] bvos = null;
	ArriveVO vo = null;

	@Override
	public Object afterSend(JSONObject response) throws Exception {
		String code = response.getString("code");
		if (!"200".equals(code)) {
			throw new BusinessException(response.toJSONString());
		} else {
			JSONObject output = response.getJSONObject("output");
			if (output != null && output.size() > 0) {
				String status = output.getString("status");
				if(!"success".equalsIgnoreCase(status)){
					String msg = output.getString("msg");
					throw new BusinessException( msg);
				}
				JSONArray data = JSONArray.parseArray(output.getString("data"));
				String syncid = output.getString("syncid");
				for (ArriveItemVO bvo : bvos) {
					JSONObject matcheRepBody = null;
					for(int i=0;i<data.size();i++){
						JSONObject respbody = data.getJSONObject(i);
						if(bvo.getPk_arriveorder_b().equalsIgnoreCase(respbody.getString("erpitemid"))){
							matcheRepBody = respbody;
						}
					}
					if(matcheRepBody == null ){
						throw new BusinessException(bvo.getCrowno() +"行,未匹配到回执,请联系信息运维排查.");
					}
					String u_extrequestid = matcheRepBody.getString("u_extrequestid");//LIMS检验请求ID
					bvo.setVbdef11(u_extrequestid);
//					bvo.setNaccumchecknum(bvo.getNnum());////累计报检主数量
					bvo.setVbdef12(syncid);
					//更新批次状态 在检验
					String sql ="update scm_batchcode set Binqc='Y' where pk_batchcode='"+bvo.getPk_batchcode()+"'";
					getDao().executeUpdate(sql);
					
				}
				new VOUpdate().update(bvos, new String[] {"vbdef11", "vbdef12" });

				ArriveVO newvo = (ArriveVO) vo.clone();
				newvo.setBVO(bvos);

				WriteBackArriveCheckNumRule rule2 = new WriteBackArriveCheckNumRule(
						true);
				rule2.process(new ArriveVO[] { newvo });
			} else {
				throw new BusinessException("回执异常:返回的output数据为空:" + response);
			}
		}
		return response;
	}

	@Override
	public void init(Object obj, Map<String, Object> otherpms) throws Exception {
		ArriveVO newvo = (ArriveVO) obj;
		bvos = (ArriveItemVO[]) newvo.getBVO();
		vo = (ArriveVO) obj;
	}

	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}

	public String getSendJson(Object obj, Map<String, Object> otherpms)
			throws Exception {

		ArriveVO newvo = (ArriveVO) vo.clone();
		newvo.setBVO(bvos);

		String jsonObj = changeTOJson(newvo);
		return jsonObj;
	}

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
		Map<String, SupplierVO> supmap = getSupplierVO(cvendorids);

		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());

		OrgVO orgvo = getOrgVO(vo.getHVO().getPk_org());

		OrgVO deptvo = getOrgVO(vo.getHVO().getPk_dept());

		Map<String, SupplierqualityHVO> gmap = getGradeInfo(cvendorids, vo
				.getHVO().getPk_group(), vo.getHVO().getPk_org());

		// List<JSONObject> list = new ArrayList<>();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("functype", functype);
		
		JSONObject hjsonObject = new JSONObject();
		String sysFlowNO = getSysFlowNo();
		String ctrantype = getctrantype(vo.getHVO().getPk_org(),bvos[0].getPk_material());
		sysFlowNO ="PU"+ctrantype+"-"+sysFlowNO;
		hjsonObject.put("u_syncid",sysFlowNO);// 同步号
		hjsonObject.put("u_requesttype", ctrantype);// 业务类型
		hjsonObject.put("u_submitdt", new UFDateTime().toString());// 报检时间
		if (uservo != null) {
			hjsonObject.put("u_submitercode", uservo.getUser_code());// 报检人编码
			hjsonObject.put("u_submitername", uservo.getUser_name());// 报检人名称
		}

		if (deptvo != null) {
			hjsonObject.put("u_deptcode", deptvo.getCode());// 报检部门编码
			hjsonObject.put("u_deptname", deptvo.getName());// 报检部门名称
		}

		// jsonObject.put("productionLline", "");// 产线

		if (orgvo != null) {
			hjsonObject.put("u_org_code", orgvo.getCode());// 所属公司编码
			hjsonObject.put("u_org_name", orgvo.getName());// 所属公司
		}

		hjsonObject.put("u_group_code", "G");// 所属集团

		JSONArray array = new JSONArray();
		for (ArriveItemVO bvo : bvos) {
			MaterialVO matervo = mmap.get(bvo.getPk_material());

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("u_sourcebillno", vo.getHVO().getVbillcode());// 来源单据号
			// 来源单据ID
			jsonObject.put("u_sourcerequestid", bvo.getPk_arriveorder_b());// 来源单据明细ID
			jsonObject.put("u_extmaterialid", matervo.getCode());// 物料编码
			// jsonObject.put("material_name", matervo.getName());// 物料名称
			// jsonObject.put("materialspec", matervo.getMaterialspec());// 规格
			// jsonObject.put("materialtype", matervo.getMaterialtype());// 型号
			jsonObject.put("u_material_lot", bvo.getVbatchcode());// 批次号
			jsonObject.put("u_supplier_lotcode", bvo.getBc_vvendbatchcode());// 供应商批次号
			if (bvo.getDproducedate() != null)
				jsonObject.put("u_manufacturedt", bvo.getDproducedate()
						.toString());// 生产日期

			if (bvo.getDinvaliddate() != null)
				jsonObject.put("u_validdt", bvo.getDinvaliddate().toString());// 失效/复测日期
			 DefdocVO defvo = dmap.get(bvo.getCproductorid());
			if (defvo != null) {
//				   生产厂商编码 u_extmanufacturerid
				jsonObject.put("u_extmanufacturerid", defvo.getCode());
				String key = bvo.getPk_material() + "&" + bvo.getCproductorid();
				SupplierqualityHVO suppliervo = gmap.get(key);
//				生产商等级	u_manufacturer_level 1=不批准2=批准3=认证4=维持仅用于类型1，2，3
				if (suppliervo != null)
					jsonObject.put("u_manufacturer_level",getgrade(suppliervo.getPk_grade_info()));// 供应商/生产商等级
			}
			MeasdocVO mesvo = memap.get(bvo.getCunitid());
			if (mesvo != null) {
				jsonObject.put("u_material_unit", mesvo.getName());// 主单位名称
			}
			jsonObject.put("u_material_qty", bvo.getNnum().toString());// 报检主数量

			// mesvo = memap.get(bvo.getCastunitid());
			// if (mesvo != null) {
			// jsonObject.put("castunit", mesvo.getName());// 单位
			// }
//			jsonObject.put("u_container_cnt", bvo.getNastnum().toString());// 容器数量
			
			/*
			 * 非罐区物料取包装数量，罐区物料直接 报1
			 * */
			if(ctrantype.equals("3")){
//			--罐区物料
				jsonObject.put("u_container_cnt", "1");// 包装数量
			} else {
//			--非罐区物料
				jsonObject.put("u_container_cnt", bvo.getVbdef18().toString());// 包装数量
			}


			// jsonObject.put("def1", "");// 预留1
			// jsonObject.put("def2", "");// 预留2
			// jsonObject.put("def3", "");// 预留3
			// jsonObject.put("def4", "");// 预留4
			// jsonObject.put("def5", "");// 预留5
			// list.add(jsonObject);
			array.add(jsonObject);
		}
		String details = array.toJSONString().replace("[", "").replace("]", "");
		hjsonObject.put("details", details);

		jsonObject1.put("data", hjsonObject);
		return jsonObject1.toJSONString();
	}

	private String getctrantype(String pk_org,String pk_material) throws BusinessException {
		//  stockbycheck  根据检验结果入库  
		String sql = " select stockbycheck    from bd_materialstock where pk_material='"
				+ pk_material + "' and   pk_org ='" + pk_org + "' and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs.executeQuery(sql, new MapProcessor());
		String chkfreeflag= (String) hashMap2.get("stockbycheck");
		if(chkfreeflag == null){
			return "1";
		}
		UFBoolean b = UFBoolean.valueOf(chkfreeflag);
//		/ 罐区溶剂--
		if(b.booleanValue()){
			return "3";
		}
		
		return "1";
	}



}
