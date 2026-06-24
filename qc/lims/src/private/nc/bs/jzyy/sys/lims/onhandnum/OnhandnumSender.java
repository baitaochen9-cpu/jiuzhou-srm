package nc.bs.jzyy.sys.lims.onhandnum;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.jzyy.sys.lims.AbstractSender4LIMS;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.UserVO;

import com.alibaba.fastjson.JSONObject;

/**
 * 1、ERP采购到货单调用LIMS报检接口
 * 
 * @author liyf
 * 
 */
public class OnhandnumSender extends AbstractSender4LIMS {

	public static final String functype = "nc_inspection";
	ArriveItemVO[] bvos = null;
	ArriveVO vo = null;
	

	@Override
	public Object afterSend(JSONObject response) throws Exception {
		String code = response.getString("code");
		if(!"200".equals(code)){
			String message = response.getString("msg");
			throw new BusinessException(message);
		}else{
			JSONObject data = response.getJSONObject("data");
			if(data != null && data.size()>0){
				String u_extrequestid = response.getString("u_extrequestid");
				String syncid = response.getString("syncid");
				for (ArriveItemVO bvo : bvos) {
					bvo.setNaccumchecknum(bvo.getNnum());
					bvo.setVbdef11(u_extrequestid);
					bvo.setVbdef12(syncid);
				}
				new VOUpdate().update(bvos,
						new String[] { "naccumchecknum", "vbdef11" , "vbdef12"});
			}else{
				throw new BusinessException("返回的data数据为空！");
			}
		}
		return response;
	}

	@Override
	public void init(Object obj, Map<String, Object> otherpms) throws Exception {
		bvos = (ArriveItemVO[]) otherpms.get("bvos");
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

		// List<JSONObject> list = new ArrayList<>();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("functype", functype);
		for (ArriveItemVO bvo : bvos) {
			MaterialVO matervo = mmap.get(bvo.getPk_material());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("u_syncid",  getSysFlowNo());// 同步号
			jsonObject.put("u_requesttype", getctrantype(matervo));// 业务类型
			jsonObject.put("u_sourcebillno", vo.getHVO().getVbillcode());// 来源单据号
			// jsonObject.put("bill_id", vo.getHVO().getPk_arriveorder());//
			// 来源单据ID
			jsonObject.put("u_sourcerequestid", bvo.getPk_arriveorder_b());// 来源单据明细ID
			jsonObject.put("u_extmaterialid", matervo.getCode());// 物料编码
			// jsonObject.put("material_name", matervo.getName());// 物料名称
			// jsonObject.put("materialspec", matervo.getMaterialspec());// 规格
			// jsonObject.put("materialtype", matervo.getMaterialtype());// 型号
			jsonObject.put("u_material_lot", bvo.getVbatchcode());// 批次号
			jsonObject.put("u_supplier_lotcode", bvo.getBc_vvendbatchcode());// 供应商批次号
			jsonObject.put("u_manufacturedt", bvo.getDproducedate().toString());// 生产日期
			jsonObject.put("u_validdt", bvo.getDinvaliddate().toString());// 失效/复测日期
			SupplierVO supplier = supmap.get(vo.getHVO().getPk_supplier());
			if (supplier != null) {
				jsonObject.put("u_extmanufacturerid", supplier.getCode());// 供应商编码
				// jsonObject.put("cvendor_name", supplier.getName());// 供应商名称
				jsonObject.put("u_manufacturer_level", supplier.getName2());// 供应商/生产商等级
			}
			// DefdocVO defvo = dmap.get(bvo.getCproductorid());
			// if (defvo != null) {
			// jsonObject.put("cproductor_name", defvo.getName());// 生产厂商
			// jsonObject.put("cproductor_code", defvo.getCode());// 生产厂商编码
			//
			// }

			// jsonObject.put("pk_onhanddim", "");// ERP库存维度

			MeasdocVO mesvo = memap.get(bvo.getCunitid());
			if (mesvo != null) {
				jsonObject.put("u_material_unit", mesvo.getName());// 主单位名称
			}
			jsonObject.put("u_material_qty", bvo.getNnum().toString());// 报检主数量

			// mesvo = memap.get(bvo.getCastunitid());
			// if (mesvo != null) {
			// jsonObject.put("castunit", mesvo.getName());// 单位
			// }
			jsonObject.put("u_container_cnt", bvo.getNastnum().toString());// 报检数量
			jsonObject.put("u_submitdt", new UFDateTime().toString());// 报检时间

			if (uservo != null) {
				jsonObject.put("u_submitercode", uservo.getUser_code());// 报检人编码
				jsonObject.put("u_submitername", uservo.getUser_name());// 报检人名称
			}

			if (deptvo != null) {
				jsonObject.put("u_deptcode", deptvo.getCode());// 报检部门编码
				jsonObject.put("u_deptname", deptvo.getName());// 报检部门名称
			}

			// jsonObject.put("productionLline", "");// 产线

			if (orgvo != null) {
				jsonObject.put("u_org_code", orgvo.getCode());// 所属公司编码
				jsonObject.put("u_org_name", orgvo.getName());// 所属公司
			}

			jsonObject.put("u_group_code", vo.getHVO().getPk_group());// 所属集团
			// jsonObject.put("def1", "");// 预留1
			// jsonObject.put("def2", "");// 预留2
			// jsonObject.put("def3", "");// 预留3
			// jsonObject.put("def4", "");// 预留4
			// jsonObject.put("def5", "");// 预留5

			// jsonObject.put("action", "0");// 动作

			// list.add(jsonObject);
			jsonObject1.put("data", jsonObject);
		}

		return jsonObject1.toJSONString();
	}

	private String getctrantype(MaterialVO matervo) throws UifException {

		if (matervo.getPk_marbasclass().startsWith("0502")) {// 罐区溶剂
			return "3";
		}
		return "1";
	}

}
