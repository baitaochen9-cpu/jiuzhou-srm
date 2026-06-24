package nc.bs.jzyy.sys.lims.iccheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.ic.m4z.entity.FreezeThawVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP库存检验冻结单调用LIMS报检接口
 * 
 * @author liyf
 * 
 */
public class IccheckUpSender extends AbstractSender4LIMS {

	public static final String functype = "nc_inspection";
	FreezeThawVO[] vos = null;

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
				BaseDAO dao = new BaseDAO();
				for (FreezeThawVO bvo : vos) {
					JSONObject matcheRepBody = null;
					for(int i=0;i<data.size();i++){
						JSONObject respbody = data.getJSONObject(i);
						String erpitemid = respbody.getString("erpitemid");
						if(erpitemid.contains(bvo.getPk_onhanddim())){
							matcheRepBody = respbody;
						}
					}
					if(matcheRepBody == null ){
						throw new BusinessException("行,未匹配到回执,请联系信息运维排查.");
					}
					String u_extrequestid = matcheRepBody.getString("u_extrequestid");//LIMS检验请求ID
//					bvo.setCcorrespondrowno(ccorrespondrowno);
					bvo.setCcorrespondcode(u_extrequestid);
					int rs = dao.executeUpdate("update ic_invfreeze set  Ccorrespondcode='"+u_extrequestid+"' where pk_group='"+bvo.getPk_group()+"' and pk_org='"+bvo.getPk_org()+"' and  Pk_onhanddim='"+bvo.getPk_onhanddim()+"'");
					if(rs<0){
						
					}
				}
			
			} else {
				throw new BusinessException("回执异常:返回的output数据为空:" + response);
			}
		}
		return response;
	}

	@Override
	public void init(Object obj, Map<String, Object> otherpms) throws Exception {
		vos = (FreezeThawVO[]) obj;
	}

	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}

	public String getSendJson(Object obj, Map<String, Object> otherpms)
			throws Exception {
		String pk_org = vos[0].getPk_org();
		String jsonObj = changeTOJson(pk_org,vos);
		return jsonObj;
	}

	private String changeTOJson(String pk_org, FreezeThawVO[] reQueryOnhandVOs)
			throws BusinessException {
		Set<String> pk_marterial_vs = new HashSet<String>();
		Set<String> pk_onhanddims = new HashSet<String>();
		Set<String> pk_measdoc = new HashSet<String>();
		Set<String> pk_defdocs = new HashSet<String>();
		Set<String> cvendorids = new HashSet<String>();
		Set<String> pk_batchcodes = new HashSet<String>();
		for (FreezeThawVO bvo : reQueryOnhandVOs) {
			if (!StringUtil.isEmpty(bvo.getPk_onhanddim())) {
				pk_onhanddims.add(bvo.getPk_onhanddim());
			}

			if (!StringUtil.isEmpty(bvo.getCunitid())) {
				pk_measdoc.add(bvo.getCunitid());
			}

			if (!StringUtil.isEmpty(bvo.getCastunitid())) {
				pk_measdoc.add(bvo.getCastunitid());
			}
		}
		Map<String, OnhandDimVO> ohmap = getOnhandDimVOMap(pk_onhanddims);
		for (OnhandDimVO bvo : ohmap.values()) {
			if (!StringUtil.isEmpty(bvo.getCmaterialoid())) {
				pk_marterial_vs.add(bvo.getCmaterialoid());
			}
			if (!StringUtil.isEmpty(bvo.getCproductorid())) {
				pk_defdocs.add(bvo.getCproductorid());
			}
			if (!StringUtil.isEmpty(bvo.getCvendorid())) {
				cvendorids.add(bvo.getCvendorid());
			}
			if (!StringUtil.isEmpty(bvo.getPk_batchcode())) {
				pk_batchcodes.add(bvo.getPk_batchcode());
			}
		}

		Map<String, BatchcodeVO> batmap = getBatchcodeVOMap(pk_batchcodes);
		Map<String, MaterialVO> mmap = getMaterialVOMap(pk_marterial_vs);
		Map<String, DefdocVO> dmap = getDefdocVOMap(pk_defdocs);
		Map<String, MeasdocVO> memap = getMeasdocVOMap(pk_measdoc);
		Map<String, SupplierVO> supmap = getSupplierVO(cvendorids);
		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());
		OrgVO orgvo = getOrgVO(pk_org);
		//生产商物料状态
		Map<String, SupplierqualityHVO> gmap = getGradeInfo(cvendorids, reQueryOnhandVOs[0].getPk_group(), pk_org);

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("functype", functype);
		JSONObject hjsonObject = new JSONObject();
		hjsonObject.put("u_syncid", getSysFlowNo());// 同步号
		hjsonObject.put("u_requesttype", "2");// 业务类型
		hjsonObject.put("u_submitdt", new UFDateTime().toString());// 报检时间
		if (uservo != null) {
			hjsonObject.put("u_submitercode", uservo.getUser_code());// 报检人编码
			hjsonObject.put("u_submitername", uservo.getUser_name());// 报检人名称
		}
//		hjsonObject.put("u_deptcode", deptvo.getCode());// 报检部门编码
//		hjsonObject.put("u_deptname", deptvo.getName());// 报检部门名称
		
		hjsonObject.put("u_org_code", orgvo.getCode());// 所属公司编码
		hjsonObject.put("u_org_name", orgvo.getName());// 所属公司

		hjsonObject.put("u_group_code", "G");// 所属集团
		//同一个物料相同批次进行汇总
		Map<String,ArrayList<FreezeThawVO>> groupVOs = new HashMap<String,ArrayList<FreezeThawVO>>();
		for (FreezeThawVO bvo : reQueryOnhandVOs) {
			OnhandDimVO onhandvo = ohmap.get(bvo.getPk_onhanddim());
			MaterialVO matervo = mmap.get(onhandvo.getCmaterialoid());
			String key = matervo.getCode()+"&"+onhandvo.getVbatchcode();
			if(groupVOs.containsKey(key)){
				groupVOs.get(key).add(bvo);
			}else{
				ArrayList<FreezeThawVO> details = new ArrayList<FreezeThawVO>();
				details.add(bvo);
				groupVOs.put(key, details);
			}
		}
		JSONArray array = new JSONArray();
		for(String key:groupVOs.keySet()){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("u_sourcebillno",key);// 来源单据号
			// 来源单据ID
			String u_sourcerequestid ="";
			ArrayList<FreezeThawVO> details = groupVOs.get(key);
			UFDouble nnum = UFDouble.ZERO_DBL;
			for(int i=0;i<details.size();i++){
				FreezeThawVO bvo = details.get(i);
				u_sourcerequestid= u_sourcerequestid+bvo.getPk_onhanddim();
				if(i<details.size()-1){
					u_sourcerequestid=u_sourcerequestid+"&";
				}
				nnum= nnum.add(bvo.getNnum());
			}
			jsonObject.put("u_sourcerequestid", u_sourcerequestid);// 来源单据明细ID
			FreezeThawVO bvo = details.get(0);
			OnhandDimVO onhandvo = ohmap.get(bvo.getPk_onhanddim());
			MaterialVO matervo = mmap.get(onhandvo.getCmaterialoid());
			jsonObject.put("u_extmaterialid", matervo.getCode());// 物料编码
			// jsonObject.put("material_name", matervo.getName());// 物料名称
			// jsonObject.put("materialspec", matervo.getMaterialspec());// 规格
			// jsonObject.put("materialtype", matervo.getMaterialtype());// 型号
			jsonObject.put("u_material_lot", onhandvo.getVbatchcode());// 批次号
//			jsonObject.put("u_supplier_lotcode",);// 供应商批次号
			BatchcodeVO batvo = batmap.get(onhandvo.getPk_batchcode());
			if (batvo != null) {
				jsonObject.put("u_manufacturedt", batvo.getDproducedate()
						.toString());// 生产日期
				jsonObject.put("u_validdt", batvo.getDvalidate().toString());// 失效/复测日期
			}

			DefdocVO cproductor = dmap.get(onhandvo.getCproductorid());
			if (cproductor != null) {
				jsonObject.put("u_extmanufacturerid", cproductor.getCode());// 生产商编码
				// jsonObject.put("cvendor_name", supplier.getName());//生产商名称
				String key11 = onhandvo.getCmaterialoid() + "&" + onhandvo.getCproductorid();
				SupplierqualityHVO suppliervo = gmap.get(key11);
				if (suppliervo != null)
					jsonObject.put("u_manufacturer_level",
							getgrade(suppliervo.getPk_grade_info()));// 供应商/生产商等级
			}		

			MeasdocVO mesvo = memap.get(bvo.getCunitid());
			if (mesvo != null) {
				jsonObject.put("u_material_unit", mesvo.getName());// 主单位名称
			}
			jsonObject.put("u_material_qty", nnum.toString());// 报检主数量

//			jsonObject.put("u_container_cnt", bvo.getNnum().toString());//容器数量
			jsonObject.put("u_submitdt", new UFDateTime().toString());// 报检时间

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
}
