package nc.bs.srm.ic.po;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.cmp.tools.StringUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PoOrder_update extends PoOrderPublic {

	@Override
	public JSONObject process(Object billvo) throws BusinessException {
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		OrderVO[] order = null;
		try {
			JSONObject reqJson = jsonObject.getJSONObject("bill");
			JSONObject hjson = reqJson.getJSONObject("head");
			String vbillcode = hjson.getString("vbillcode");
			String pk_org = hjson.getString("pk_org");

			order = queryVOByCode(vbillcode, pk_org);
			// 判断是否需要删除单据
			JSONArray reqBoyds = reqJson.getJSONArray("body");
			boolean isDelBill = true;
			for (int i = 0; i < reqBoyds.size(); i++) {
				String integer = reqBoyds.getJSONObject(i).getString(
						"cancelledFlag");
				if ("0".equals(integer)) {
					isDelBill = false;
				}
			}
			if (isDelBill == true) {
				del(order[0]);
				return getRsultDataSuccess((JSONObject) null, "修改采购订单成功:采购订单已删除");

			}
			 processBill(order[0], reqJson);
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			throw new BusinessException("操作采购订单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order[0].getHVO().getVbillcode());
		data.put("pk", order[0].getHVO().getPk_order());
		// 将结果返回v
		return getRsultDataSuccess(data, "修改采购订单成功");
	}

	private OrderVO processBill(OrderVO order, JSONObject json)
			throws BusinessException {
		JSONArray bjsons = json.getJSONArray("body");
		// 3.修改数据
		ch2Bill(order, json);
		//补全数据
		fillup(order);
		//补全单据状态
		upStatues(order,bjsons);
		sysFillup(order);
		revise(order);
		
		return order;
	}

	private void ch2Bill(OrderVO order, JSONObject json)
			throws BusinessException {
		OrderHeaderVO hvo = order.getHVO();
		OrderItemVO[] bvos = order.getBVO();
		OrderHeaderVO billVO = chg2Hvo(json, hvo);
		OrderItemVO[] billDetailVO = getBillDetailVO(json,hvo, bvos);
		order.setHVO(billVO);
		order.setBVO(billDetailVO);
	}



	// public
	protected OrderVO revise(OrderVO billvo) throws BusinessException {

		IPFBusiAction service1 = (IPFBusiAction) NCLocator.getInstance()
				.lookup(IPFBusiAction.class);
		OrderVO[] returnVO1 = (OrderVO[]) service1.processBatch("REVISE", "21",
				new OrderVO[] { billvo }, null, null, null);

		OrderVO[] aggvoss = this.queryVOByPk(returnVO1[0].getParent()
				.getPrimaryKey());

		return aggvoss[0];
	}

	

	private OrderItemVO[] getBillDetailVO(JSONObject bill,OrderHeaderVO hvo ,OrderItemVO[] vos)
			throws BusinessException {
		JSONArray req_bodys = bill.getJSONArray("body"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		ArrayList<String> arr = new ArrayList<>();
		ArrayList<String> erp_lineNums = new ArrayList<>();// 记录NC已经保存的SRM行号

		// 创建一个数组里面包含所有vo的表体行
		for (int i = 0; i < req_bodys.size(); i++) {
			String lineNum = req_bodys.getJSONObject(i).getString("lineNum");
			arr.add(lineNum);
		}
		
		List<OrderItemVO> bodys = new ArrayList<OrderItemVO>();
		for(OrderItemVO vo:vos){
//			bodys.add(vo);
			erp_lineNums.add(vo.getVbdef2());
		}

		/**
		 * 如果报文的行号等于表体 的行号 并且取消标识为0就用表体的vo来组装， 如果创建的数组里面不包含报文里面的表体行
		 * 并且取消标识为0就走新增表体 如果这两个都不满足就走删除表体行
		 */

		for (int i = 0; i < req_bodys.size(); i++) {
			Map req_body = (Map) req_bodys.get(i);
			String lineNum = (String) req_body.get("lineNum");
			String cancelledFlag = (String) req_body.get("cancelledFlag");
			// 判断是否新增
			if (!erp_lineNums.contains(lineNum)&&"0".equals(cancelledFlag)) {
				OrderItemVO matchedItem = new  OrderItemVO();
				
				OrderItemVO vo = (OrderItemVO)matchedItem.clone();
//				vo.setPk_order_b(null);
//				vo.setCrowno(null);
				vo = chg2Body(req_body,hvo, vo);
				vo.setStatus(VOStatus.NEW);
				bodys.add(vo);
				erp_lineNums.add(lineNum);

			} else {
				OrderItemVO matchedItem = null;
				for (OrderItemVO vo : vos) {
					String erp_srm_lineNum = vo.getVbdef2();
					if (lineNum.equals(erp_srm_lineNum)) {
//						matchedItem = (OrderItemVO)vo.clone();
//						bodys.add(matchedItem);
						if(!cancelledFlag.equals(1)){
							matchedItem = chg2Body(req_body,hvo, vo);
							matchedItem.setStatus(VOStatus.UPDATED);
							bodys.add(matchedItem);
						}else{
							matchedItem = new OrderItemVO();
							bodys.add(matchedItem);
						}
						

					}
				}

			}

		}
		return bodys.toArray(new OrderItemVO[bodys.size()]);

	}
	private void upStatues(OrderVO order ,JSONArray bjsons){
		order.getHVO().setStatus(VOStatus.UPDATED);
		for(OrderItemVO bvo : order.getBVO()){
			for (int i = 0; i < bjsons.size(); i++) {
				JSONObject jsonObject = bjsons.getJSONObject(i);
				String lineNum = jsonObject.getString("lineNum");
				String cancelledFlag = jsonObject.getString("cancelledFlag");

				if(lineNum.equals(bvo.getVbdef2())){
					if(cancelledFlag.equals("1")){
						bvo.setStatus(VOStatus.DELETED);
					}else if("0".equals(cancelledFlag)&& StringUtil.isEmpty(bvo.getPk_order_b())){
						bvo.setStatus(VOStatus.NEW);
					}else{
						bvo.setStatus(VOStatus.UPDATED);
					}
				}
			}
		}
	}
}
