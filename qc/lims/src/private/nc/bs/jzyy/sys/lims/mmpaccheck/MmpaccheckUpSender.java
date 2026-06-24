package nc.bs.jzyy.sys.lims.mmpaccheck;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP流程生产订单调用LIMS报检接口
 * 
 * @author liyf
 * 
 */
public class MmpaccheckUpSender extends AbstractSender4LIMS {

	public static final String functype = "nc_inspection";
	PMOItemVO[] bvos = null;
	PMOAggVO vo = null;

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
				for (PMOItemVO bvo : bvos) {
					JSONObject matcheRepBody = null;
					for(int i=0;i<data.size();i++){
						JSONObject respbody = data.getJSONObject(i);
						if(bvo.getCmoid().equalsIgnoreCase(respbody.getString("erpitemid"))){
							matcheRepBody = respbody;
						}
					}
					if(matcheRepBody == null ){
						throw new BusinessException(bvo.getVrowno() +"行,未匹配到回执,请联系信息运维排查.");
					}
					String u_extrequestid = matcheRepBody.getString("u_extrequestid");//LIMS检验请求ID
					bvo.setVdef11(u_extrequestid);
					bvo.setVdef12(syncid);
					
				}
				new VOUpdate().update(bvos,new String[] { "vdef11", "vdef12"});
			} else {
				throw new BusinessException("回执异常:返回的output数据为空:" + response);
			}
		}
		return response;
	
	}

	@Override
	public void init(Object obj, Map<String, Object> otherpms) throws Exception {
		 vo = (PMOAggVO)obj;
		 bvos = (PMOItemVO[]) vo.getChildrenVO();
	}

	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}

	public String getSendJson(Object obj, Map<String, Object> otherpms)
			throws Exception {
		PMOAggVO newvo = (PMOAggVO) vo.clone();
		newvo.setChildrenVO(bvos);

		String jsonObj = changeTOJson(newvo);
		return jsonObj;
	}
	private String changeTOJson(PMOAggVO vo) throws BusinessException {
		PMOItemVO[] bvos = vo.getChildrenVO();
		Set<String> pk_marterial_vs = new HashSet<String>();
		Set<String> pk_defdocs = new HashSet<String>();
		Set<String> pk_measdoc = new HashSet<String>();
		Set<String> cvendorids = new HashSet<String>();
		Set<String> pk_depts = new HashSet<String>();
		for (PMOItemVO bvo : bvos) {
			if (!StringUtil.isEmpty(bvo.getCmaterialid())) {
				pk_marterial_vs.add(bvo.getCmaterialid());
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
			
			if (!StringUtil.isEmpty(bvo.getCvendorid())) {
				cvendorids.add(bvo.getCvendorid());
			}
			
			if (!StringUtil.isEmpty(bvo.getCdeptid())) {
				pk_depts.add(bvo.getCdeptid());
			}
		}

		Map<String, MaterialVO> mmap = getMaterialVOMap(pk_marterial_vs);
//		Map<String, DefdocVO> dmap = getDefdocVOMap(pk_defdocs);
		Map<String, MeasdocVO> memap = getMeasdocVOMap(pk_measdoc);
		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());
		OrgVO orgvo = getOrgVO(vo.getParentVO().getPk_org());
		Map<String, OrgVO> deptmap = getOrgVOs(pk_depts);
		
		PMOItemVO bvo1 = bvos[0];
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("functype", functype);
		JSONObject hjsonObject = new JSONObject();
		String sysFlowNO = getSysFlowNo();
		HashMap<String, Object> materialStor = getMaterialStor(vo.getParentVO().getPk_org(), bvos[0].getCmaterialid());
		String ctrantype =getctrantype(materialStor);
		sysFlowNO ="MO"+ctrantype+"-"+sysFlowNO;
		hjsonObject.put("u_syncid",sysFlowNO);// 同步号
		hjsonObject.put("u_requesttype", ctrantype);// 业务类型
		
		hjsonObject.put("u_submitdt", new UFDateTime().toString());// 报检时间
//		业务类型	u_requesttype	String	20	Y	1=采购检验				2=仓库复检				3=槽车检验				4=生产检验
		if (uservo != null) {
			hjsonObject.put("u_submitercode", uservo.getUser_code());// 报检人编码
			hjsonObject.put("u_submitername", uservo.getUser_name());// 报检人名称
		}
		OrgVO deptvo = deptmap.get(bvo1.getCdeptid());
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
		for (PMOItemVO bvo : bvos) {
			MaterialVO matervo = mmap.get(bvo.getCmaterialid());

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("u_sourcebillno",  vo.getParentVO().getVbillcode());// 来源单据号
			// 来源单据ID
			jsonObject.put("u_sourcerequestid", bvo.getCmoid());// 来源单据明细ID
			jsonObject.put("u_extmaterialid", matervo.getCode());// 物料编码
			// jsonObject.put("material_name", matervo.getName());// 物料名称
			// jsonObject.put("materialspec", matervo.getMaterialspec());// 规格
			// jsonObject.put("materialtype", matervo.getMaterialtype());// 型号
			jsonObject.put("u_material_lot", bvo.getVbatchcode());// 批次号
//			jsonObject.put("u_supplier_lotcode", bvo.getBc_vvendbatchcode());// 供应商批次号
//			if (bvo.getDproducedate() != null)jsonObject.put("u_manufacturedt", bvo.getDproducedate());// 生产日期
//			if (bvo.getDinvaliddate() != null)
//				jsonObject.put("u_validdt", bvo.getDinvaliddate().toString());// 失效/复测日期

//			 DefdocVO defvo = dmap.get(bvo.getCproductorid());
//				if (defvo != null) {
////					   生产厂商编码 u_extmanufacturerid
//					jsonObject.put("u_extmanufacturerid", defvo.getCode());
//					String key = bvo.getPk_material() + "&" + bvo.getCproductorid();
//					SupplierqualityHVO suppliervo = gmap.get(key);
////					生产商等级	u_manufacturer_level 1=不批准2=批准3=认证4=维持仅用于类型1，2，3
//					if (suppliervo != null)
//						jsonObject.put("u_manufacturer_level",getgrade(suppliervo.getPk_grade_info()));// 供应商/生产商等级
//				}

			MeasdocVO mesvo = memap.get(bvo.getCunitid());
			if (mesvo != null) {
				jsonObject.put("u_material_unit", mesvo.getName());// 主单位名称
			}
			jsonObject.put("u_material_qty", bvo.getNnum().toString());// 报检主数量

			// mesvo = memap.get(bvo.getCastunitid());
			// if (mesvo != null) {
			// jsonObject.put("castunit", mesvo.getName());// 单位
			// }
			if(bvo.getNastnum() !=null){
				jsonObject.put("u_container_cnt", bvo.getNastnum().toString());// 容器数量

			}
			// jsonObject.put("def1", "");// 预留1
			// jsonObject.put("def2", "");// 预留2
			// jsonObject.put("def3", "");// 预留3
			// jsonObject.put("def4", "");// 预留4
			// jsonObject.put("def5", "");// 预留5
			array.add(jsonObject);
		}
		String details = array.toJSONString().replace("[", "").replace("]", "");
		hjsonObject.put("details", details);

		jsonObject1.put("data", hjsonObject);
		return jsonObject1.toJSONString();
		
	}
	

	private String getctrantype(HashMap<String, Object> materialStor) throws BusinessException {
		//  stockbycheck  根据检验结果入库  
		if(materialStor == null||materialStor.size() == 0 ){
			return "4";
		}
		String chkfreeflag= (String) materialStor.get("stockbycheck");
		if(chkfreeflag == null){
			return "4";
		}
		UFBoolean b = UFBoolean.valueOf(chkfreeflag);
//		/ 罐区溶剂--
		if(b.booleanValue()){
			return "5";
		}
		
		return "4";
	}
}
