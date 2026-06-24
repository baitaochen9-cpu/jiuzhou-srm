package nc.bs.srm.ic.po;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.cmp.tools.StringUtil;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.pu.m21.IOrderClose;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.BusinessException;
import nccloud.api.jzsrm.AbstracProcessor4Ext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PoOrder_rows extends AbstracProcessor4Ext {

	@Override
	public JSONObject process(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		OrderVO[] order = null;
		try {
			JSONObject bill = jsonObject.getJSONObject("bill");
			String pk = bill.getJSONObject("head").getString("vbillcode");
			order = this.queryaggvo(pk);
			processBill(order, bill);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			System.out.println("-------------------------" + e.getMessage()
					+ e.getStackTrace()[0].toString());
			return getRsultJsonFailed("操作采购订单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order[0].getHVO().getVbillcode());
		data.put("pk", order[0].getHVO().getPk_order());
		// 将结果返回v
		return getRsultDataSuccess(data, "修改采购订单行状态成功");
	}

	private Object processBill(OrderVO[] order, JSONObject json)
			throws BusinessException {

		OrderVO[] svo = order;
		
		// 3.修改数据
		IOrderClose service = NCLocator.getInstance().lookup(IOrderClose.class);
		JSONArray bjsons = json.getJSONArray("body");
		// String status = bjsons.getString("status");
		OrderItemVO[] bvos = order[0].getBVO();
		int parseInt = 0;
		boolean open = true;
		boolean close = true;


		for (int i = 0; i < bjsons.size(); i++) {
			JSONObject jsonObject = bjsons.getJSONObject(i);
			String status = jsonObject.getString("closedFlag");
			if ("0".equals(status)) {
				close = false;
			}
			if ("1".equals(status)) {
				open = false;
			}
		}
		if (!close && !open)   {
			for (int i = 0; i < bjsons.size(); i++) {
				JSONObject jsonObject = bjsons.getJSONObject(i);

				String status = jsonObject.getString("closedFlag");

				String lineNum = jsonObject.getString("lineNum");
				if (StringUtil.isEmpty(lineNum)) {
					throw new BusinessException("srm行号不能为空");
				}
				for (OrderItemVO bvo : bvos) {
					if (lineNum.equals(bvo.getVbdef2())) {

						String crowno = bvo.getCrowno();
						char charAt = crowno.charAt(0);//String.valueOf(charAt)
						parseInt = Integer.parseInt(String.valueOf(charAt));
						if ("0".equals(status)
								&& "Y".equals(String.valueOf(bvo
										.getBstockclose()))) {
							svo[0].setBVO(new OrderItemVO[]{bvo});
							svo = service.open(svo, 1, false);
						} else if ("1".equals(status)
								&& "N".equals(String.valueOf(bvo
										.getBstockclose()))) {
							svo[0].setBVO(new OrderItemVO[]{bvo});
							svo = service.close(svo, 1, false);
						}
					}
				}
			}
		}else{
			if (close) {
				svo = service.close(order, 0, false);
			} else if (open) {
				svo = service.open(order, 0, false);
			}
		}
//		 IPFBusiAction service1 = (IPFBusiAction) NCLocator.getInstance()
//		 .lookup(IPFBusiAction.class);
//		 OrderVO[] returnVO1 = (OrderVO[]) service1.processBatch("REVISE",
//		 "21",
//		 order, null, null, null);
		return svo[0].getParentVO().getPrimaryKey();
	}

	// public
	// 查询采购订单
	public OrderVO[] queryaggvo(String vbillcode) throws DAOException {
		String sql = "select pk_order from po_order where bislatest  = 'Y' and vbillcode = '"
				+ vbillcode + "'";
		String pk = (String) getDao().executeQuery(sql, new ColumnProcessor());
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}

}
