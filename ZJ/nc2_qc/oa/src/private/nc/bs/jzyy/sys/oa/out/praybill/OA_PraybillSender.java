package nc.bs.jzyy.sys.oa.out.praybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.FileUtil;
import nc.bs.jzyy.sys.FileVO;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.bs.jzyy.sys.oa.out.ApiProxy;
import nc.bs.jzyy.sys.oa.out.SenderQuerys;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pp.m28.entity.PriceAuditHeaderVO;
import nc.vo.pp.m28.entity.PriceAuditItemVO;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;

import com.alibaba.fastjson.JSON;

public class OA_PraybillSender extends AbstractSender4OA {

	@Override
	public Object afterSend(Object response) throws Exception {
		// TODO Auto-generated method stub
		return response;
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object send(String sendJson) throws Exception {
		String result = "";
		String url = getSysOA();
		result = ApiProxy.httpPost(url, sendJson);
		return result;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getSendJson() throws Exception {
		PraybillVO bill = (PraybillVO) getParam();
		PraybillHeaderVO head = bill.getHVO();
		PraybillItemVO[] bodys = bill.getBVO();
		SenderQuerys query = new SenderQuerys();
		// 报文
		// 表头

		List<Map<String, Object>> mainData = new ArrayList<Map<String, Object>>();
		String[] headkey = new String[] { "sqr", "sqsj", "jhy", "zsl", "zsl2",
				"erpcode", "qglx", "qglxzxx","qglxmc","jhbm", "orgcode","sm","ygje","ck","sqgs"};
		for (String key : headkey) {
			Map<String, Object> detail = new HashMap<String, Object>();
			detail.put("fieldName", key);
			Object value = null;

			if ("sqr".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(query.getBillmakercode(head
						.getBillmaker()));// 申请人
			}
			if ("sqsj".equalsIgnoreCase(key)) {
				String timeFormat2 = head.getDmakedate().toString();
				if (timeFormat2 != null) {
					String[] time = timeFormat2.split(" ");
					value = time[0];// 申请日期
				} else {
					value = " ";
				}
			}
			if ("jhy".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(query.getCemployercode(head
						.getPk_planpsn()));// 计划员
			}
			if ("ygje".equalsIgnoreCase(key)) {
				value = "0";//预估金额
			}
			if ("zsl".equalsIgnoreCase(key)) {
				value = getNullAsZero(head.getNtotalastnum());// 总数量
			}
			if ("zsl2".equalsIgnoreCase(key)) {
				value = getNullAsZero(head.getNtotalastnum());// 总数量2
			}
			if ("erpcode".equalsIgnoreCase(key)) {
				value = head.getVbillcode();// 单据号
			}
			if ("qglx".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(query.getBilltypeCode(head.getCtrantypeid()));// 请购类型编码
			}
			if ("qglxzxx".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(getSysType(head.getVdef17()));// 请购类型子选项
			}
			if ("sqgs".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(getSysCode(head.getVdef18()));// 申请公司
			}
			if ("qglxmc".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(query.getBilltype(head.getCtrantypeid()));// 请购类型名称
			}
			if ("jhbm".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(query.getDeptEx(head.getPk_plandept()));// 计划部门
			}
			if ("orgcode".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(query.getStockorgCode(head.getPk_org()));// 库存组织
			}
			if ("ck".equalsIgnoreCase(key)) {
				value = " ";// 仓库
			}
			if ("sm".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(head.getVmemo());// 说明
			}
			detail.put("fieldValue", value);
			mainData.add(detail);
		}
		// 附件
		FileVO[] ss = FileUtil.queryFiles(head.getPk_praybill(), true);
		if (ss.length > 0) {
			Map detail = new HashMap();
			List array = new ArrayList();
			for (FileVO vo : ss) {
				Map files = new HashMap();
				String base64 = vo.getBase64Str();
				String name = vo.getName();
				files.put("fileName", name);
				files.put("filePath", "base64:" + base64);
				array.add(files);
			}
			detail.put("fieldName", "xgfj");
			detail.put("fieldValue", array);
			mainData.add(detail);
		}
		// -------------------------------------------------------------------------------------------------------------------------------------------

		// 表体
		List<Map<String, Object>> detailData = new ArrayList<Map<String, Object>>();
		Map<String, Object> first = new HashMap<String, Object>();
		List<Map<String, Object>> workflowRequestTableRecords = new ArrayList<Map<String, Object>>();
		for (PraybillItemVO body : bodys) {
			// 表体第二层
			Map<String, Object> second = new HashMap<String, Object>();
			List<Map<String, Object>> workflowRequestTableRecords2 = new ArrayList<Map<String, Object>>();
			String[] bodykey = new String[] { "wlmc",
					"wlbm", "xh", "gg","jldw", "sl", "sl2","bz", "jhdhrq","pk_praybill_b"};
			for (String key : bodykey) {
				Map<String, Object> secondDetail = new HashMap<String, Object>();
				secondDetail.put("fieldName", key);
				Object value = null;
				Map<String, Object> ma = query.getMaterial(body
						.getPk_material());// 物料编码名称规格型号
				if ("wlbm".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("code"));
				}
				if ("wlmc".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("name"));
				}
				if ("gg".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("materialspec"));// 规格
				}
				if ("xh".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("materialtype"));// 型号
				}
				if ("jldw".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(query.getCastunitid(body
							.getCunitid()));// 主单位
				}
				if ("sl".equalsIgnoreCase(key)||"sl2".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNnum());// 主数量
				}
				if ("bz".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(body.getVbmemo());// 备注
				}
				if ("pk_praybill_b".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(body.getPk_praybill_b());// 主键
				}
				secondDetail.put("fieldValue", value);
				workflowRequestTableRecords2.add(secondDetail);
			}

			// 组装单据
			second.put("recordOrder", "0");
			second.put("workflowRequestTableFields",
					workflowRequestTableRecords2);
			workflowRequestTableRecords.add(second);

		}
		first.put("tableDBName", "formtable_main_279_dt1");
		first.put("workflowRequestTableRecords", workflowRequestTableRecords);
		detailData.add(first);

		// 第二层
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("requestName", "请购单");
		request.put("workflowId","396");
		request.put("detailData", detailData);
		request.put("mainData", mainData);

		// 第三层
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("srccode", "NC");
		data.put("billCode", head.getVbillcode());
		data.put("billmaker",
				getNullAsEmpty(query.getBillmakercode(head.getBillmaker())));
		data.put("srcappkey", "460b9b77f172429d8b508a762b4015de");
		data.put("targetcode", "newoa");
		data.put("targetrule", "praybillform");
		data.put("data", request);

		// 组装
		String reJson = JSON.toJSONString(data);
		return reJson;
	}
}
