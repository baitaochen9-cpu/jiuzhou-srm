package nc.bs.jzyy.sys.oa.saleorder;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.uap.pf.IPFBusiAction;
import nc.pubitf.so.m30.api.ISaleOrderQueryAPI;
import nc.vo.ic.org.OrgVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;

import com.alibaba.fastjson.JSONObject;

public class SaleOrderDelete extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// TODO Auto-generated method stub
		JSONObject jsonObject = (JSONObject) billvo;
		String vbillcode = null;
		try {
			JSONObject bill = jsonObject.getJSONObject("bill");

			vbillcode = bill.getString("vbillcode");
			if (getString_TrimZeroLenAsNull(vbillcode) == null) {
				throw new BusinessException("单据号不能为空");
			}

			String pk_org = bill.getString("org_code");
			if (getString_TrimZeroLenAsNull(pk_org) == null) {
				throw new BusinessException("销售组织不能为空");
			}

			pk_org = getSalesorg(pk_org);

			if (getString_TrimZeroLenAsNull(pk_org) == null) {
				throw new BusinessException("销售组织在nc中不存在");
			}

			ICBSContext context = new ICBSContext();
			OrgVO orgvo = context.getOrgInfo().getOrgVO(pk_org);
			if (StringUtil.isEmpty(InvocationInfoProxy.getInstance()
					.getGroupId())) {
				InvocationInfoProxy.getInstance().setGroupId(
						orgvo.getPk_group());
			}

			SaleOrderVO[] orders = queyrSaleBillByOrderBid(pk_org, vbillcode);
			deleteSaleOrder(orders);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("删除销售订单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", vbillcode);
		// 将结果返回
		return getRsultDataSuccess(data, "删除销售订单成功");
	}

	private void deleteSaleOrder(SaleOrderVO[] orders) throws BusinessException {

		if (orders == null || orders.length == 0)
			return;

		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);

		Object[] myrtn = baction.processBatch("UNAPPROVE", "30", orders, null,
				null, null);
		// myrtn = baction.processBatch("UNSAVE", "30", (SaleOrderVO[]) myrtn,
		// null, null, null);
		myrtn = baction.processBatch("DELETE", "30", (SaleOrderVO[]) myrtn,
				null, null, null);
	}

	/**
	 * 根据销售订单子表主键查询销售订单
	 * 
	 * @param pk_order_b
	 * @return
	 * @throws BusinessException
	 */
	private SaleOrderVO[] queyrSaleBillByOrderBid(String pk_org,
			String vbillcode) throws BusinessException {

		VOQuery<SaleOrderHVO> query = new VOQuery<SaleOrderHVO>(
				SaleOrderHVO.class);
		SaleOrderHVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and vbillcode='" + vbillcode + "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的销售订单,单据编号" + vbillcode);

		}

		ISaleOrderQueryAPI qryApi = NCLocator.getInstance().lookup(
				ISaleOrderQueryAPI.class);
		SaleOrderVO[] bills = qryApi.queryVOByIDs(new String[] { hvos[0]
				.getCsaleorderid() });

		return bills;
	}
}
