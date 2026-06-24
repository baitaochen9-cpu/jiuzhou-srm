package nc.bs.jzyy.sys.oa.out.praybill;

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
		try {
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

			PraybillVO[] orders = queyrVo(pk_org, vbillcode);
			String action = jsonObject.getString("action");// 0 fanshen 1 审核
			PraybillVO[] pricebills;
			if (action.equals("0")) {
				pricebills = approveBill(orders);

			} else {
				pricebills = unapproveBill(orders);
			}
            JSONArray array = jsonObject.getJSONArray("tableRecords");
            for(int i = 0;i < array.size();i++){
            	Map<String, Object> map = (Map)array.get(i);
            	String pk = map.get("pk_praybill_b").toString();
            	String employee = new SenderQuerys().getCemployerpk(map.get("employee").toString());
            	updateEmployee(pk,employee);
            }
			return getRsultJsonSuccess("生成成功", pricebills[0].getHVO()
					.getVbillcode());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("出错:" + e.getMessage());
		}
	}

	/**
	 * 更新采购员
	 * @param pk
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public void updateEmployee(String pk,String employee) throws DAOException {
		String sql = "update po_praybill_b set employee = '"+ employee + "' where pk_praybill_b = '"+pk+"' ";
		getDao().executeUpdate(sql);
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

	protected PraybillVO[] approveBill(PraybillVO[] orders)
			throws BusinessException {
		String approver = InvocationInfoProxy.getInstance().getUserId();
		if (!StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(
					orders[0].getHVO().getBillmaker());
		}
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		PraybillVO[] object = (PraybillVO[]) baction.processBatch("APPROVE",
				"20", orders, null, null, null);
		return object;
	}

	protected PraybillVO[] unapproveBill(PraybillVO[] bills)
			throws BusinessException {
		String approver = InvocationInfoProxy.getInstance().getUserId();
		if (!StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(
					bills[0].getHVO().getBillmaker());
		}
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		PraybillVO[] object = (PraybillVO[]) baction.processBatch("UNSAVEBILL",
				"20", bills, null, null, null);
		return object;
	}

}
