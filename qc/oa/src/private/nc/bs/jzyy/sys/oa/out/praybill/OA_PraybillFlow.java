package nc.bs.jzyy.sys.oa.out.praybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.jzyy.sys.oa.out.SenderQuerys;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OA_PraybillFlow extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object bill) throws BusinessException {
		JSONObject jsonObject = (JSONObject) bill;
		PraybillVO[] orders ;
		try {
			String approver = jsonObject.getString("approver");
			if (getString_TrimZeroLenAsNull(approver) != null) {
				approver = getUserId(approver);
			}
			
			
			String pk_org = jsonObject.getString("org_code");
			if (getString_TrimZeroLenAsNull(pk_org) == null) {
				throw new BusinessException("组织不能为空");
			}
			pk_org = getStockorg(pk_org);

			if (getString_TrimZeroLenAsNull(pk_org) == null) {
				throw new BusinessException("组织在nc中不存在");
			}
			String vbillcode = jsonObject.getString("erpcode");
			if (getString_TrimZeroLenAsNull(vbillcode) == null) {
				throw new BusinessException("单据号不能为空");
			}
			String action = jsonObject.getString("action");// 0 fanshen 1 审核
			PraybillVO[] pricebills;
			if (action.equals("0")) {
				JSONArray array = jsonObject.getJSONArray("tableRecords");
				if(array.size()>0){
                    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < array.size(); i++) {
						Map<String, Object> result = new HashMap<String, Object>();
						Map<String, Object> map = (Map) array.get(i);
						String pk = map.get("pk_praybill_b").toString();
						if(pk == null){
							throw new BusinessException("第"+i+"条表体主键为空");
						}
						String employee = new SenderQuerys().getCemployerpk(map.get(
								"pk_employee").toString());
//						if(employee == null){
//							throw new BusinessException("采购员未查询到对应主键，请检查！表体主键为"+pk);
//						}
						if(employee != null){
							result.put("pk", pk);
							result.put("pk_employee", employee);
						}
						data.add(result);
					}
					updateEmployee(data);
				}
				orders = queyrVo(pk_org, vbillcode);
				pricebills = approveBill(orders,approver);
			} else {
				orders = queyrVo(pk_org, vbillcode);
				pricebills = unapproveBill(orders,approver);
			}
			return getRsultJsonSuccess("生成成功", vbillcode);
		} catch (Exception e) {		
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("出错:" + e.getMessage());
		}
	}

	/**
	 * 更新采购员
	 * 
	 * @param pk
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public void updateEmployee(List<Map<String, Object>> data) throws DAOException {
		for(Map<String, Object> result : data){
			String sql = "update po_praybill_b set pk_employee = '" + result.get("pk_employee")
					+ "' where pk_praybill_b = '" + result.get("pk") + "' ";
			getDao().executeUpdate(sql);
		}
	}

	protected PraybillVO[] queyrVo(String pk_org, String vbillcode)
			throws BusinessException {

		VOQuery<PraybillHeaderVO> query = new VOQuery<PraybillHeaderVO>(
				PraybillHeaderVO.class);
		PraybillHeaderVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and vbillcode='" + vbillcode + "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的单据,单据编号" + vbillcode);

		}

		String pk = hvos[0].getPk_praybill();

		PraybillVO[] bills = queryaggvo(pk);
		return bills;
	}

	protected PraybillVO[] queryaggvo(String pk) {
		BillQuery<PraybillVO> billquery = new BillQuery<PraybillVO>(
				PraybillVO.class);
		PraybillVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}

	protected PraybillVO[] approveBill(PraybillVO[] orders,String approver)
			throws BusinessException {
		if (StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(
					orders[0].getHVO().getBillmaker());
		}else{
			InvocationInfoProxy.getInstance().setUserId(approver);
		}
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		PraybillVO[] object = (PraybillVO[]) baction.processBatch("APPROVE",
				"20", orders, null, null, null);
		return object;
	}

	protected PraybillVO[] unapproveBill(PraybillVO[] bills,String approver)
			throws BusinessException {
		if (StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(
					bills[0].getHVO().getBillmaker());
		}else{
			InvocationInfoProxy.getInstance().setUserId(approver);
		}
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		PraybillVO[] object = (PraybillVO[]) baction.processBatch("UNSAVEBILL",
				"20", bills, null, null, null);
		return object;
	}

}
