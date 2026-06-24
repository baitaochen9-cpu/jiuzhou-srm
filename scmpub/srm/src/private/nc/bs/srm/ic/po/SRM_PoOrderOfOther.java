package nc.bs.srm.ic.po;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfUtilTools;
import nc.cmp.bill.util.SysInit;
import nc.cmp.tools.StringUtil;
import nc.impl.obm.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.material.mdm.SendMdmItf;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scmpub.util.VOEntityUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.db2.jcc.am.o;

/**
 * 1.如果有请购单VO，则根据请购单关联采购订单 2.如果不传请购单，则自制采购订单 根据请购单生成采购订单
 * 
 * @author HP
 * 
 */
public class SRM_PoOrderOfOther extends PoOrderPublic {

	@Override
	public JSONObject process(Object billvo) throws Exception {
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		OrderVO[] order = null;
		try {
			JSONObject bill = jsonObject.getJSONObject("bill");
			order = processBill(bill);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			throw new BusinessException("操作采购订单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order[0].getHVO().getVbillcode());
		data.put("pk", order[0].getHVO().getPk_order());
		// 将结果返回v
		return getRsultDataSuccess(data, "生成采购订单成功");
	}

	public OrderVO[] processBill(JSONObject reqJson) throws BusinessException {

		OrderVO prebill = chg2BillVo(reqJson); //单据转换
		fillup(prebill); //数据补全
		sysFillup(prebill);
		OrderVO[] savedBills = insertBill(new OrderVO[] { prebill }); //持久化
		OrderVO[] apprveddBills = approveBill(savedBills); //流程处理
	
		return qryOrignBills(apprveddBills);
	}

//	/**
//	 *  修复数据，收货仓库
//	 *  原系统数据补全时，全自动依据需求仓库修正收货仓库，无法满足九洲本部及外沙岩头采购场景，这里收货库存组织就没时间组织级参数来控制
//	 * @param prebill
//	 * @throws BusinessException 
//	 */
//	private void sysFillup(OrderVO prebill) throws BusinessException {
//		OrderHeaderVO hvo = prebill.getHVO();
//		OrderItemVO[] bvo = prebill.getBVO();
//		String pk_org = hvo.getPk_org();//采购组织，即表头pk_org
//		String body_reqcorp =  null;
//		UFBoolean para = SysInit.getParaBoolean(pk_org, "JZ_SRM002");
//		SendMdmItf lookup = NCLocator.getInstance().lookup(SendMdmItf.class);
//		for(OrderItemVO body :  bvo){//能进到这里的，确保表头表体有值 
//			String pk_reqstoorg = body.getPk_reqstoorg();//需求库存组织
//			if(StringUtil.isNotEmpty(pk_reqstoorg)){			
//				
//				UFBoolean paraBoolean = SysInit.getParaBoolean(pk_reqstoorg, "JZ_SRM001");
//				if (null != paraBoolean && paraBoolean == UFBoolean.TRUE) {
//					body.setPk_arrvstoorg(body.getPk_org());
//					body.setPk_arrvstoorg_v(body.getPk_org_v());
//				}
//				
//			
//				body_reqcorp = body.getPk_reqcorp();//需求公司
//				String pk_material = body.getPk_material();
//				if(null != para && UFBoolean.TRUE == para){
//					//修订表体物料，优先选择本公司物料，如果没有，说明物料为分配到本公司的，在本公司没有创建物料档案，再取需求公司物料，以避免跨公司物料异常情况发生。
//					String materialChangeByOrg = lookup.materialChangeByOrg(pk_material, pk_org); //目标公司物料
//					pk_material = (null==materialChangeByOrg || "".equals(materialChangeByOrg)) ? pk_material : materialChangeByOrg;
//					body.setPk_material(pk_material);
//					body.setPk_srcmaterial(pk_material);
//					
//				}
//			}
//		}
//		
//	}

	protected OrderVO[] insertBill(OrderVO[] bills) throws BusinessException {
	
		IPFBusiAction service1 = (IPFBusiAction) NCLocator.getInstance()
				.lookup(IPFBusiAction.class);
		OrderVO[] orderVOs = (OrderVO[]) service1.processBatch("SAVEBASE", "21",
				bills, null, null, null);
		return orderVOs;
	}

 

	protected OrderVO[] approveBill(OrderVO[] billvos) throws BusinessException {
		// 重新查询，防止并发
		OrderVO[] orderVOs = qryOrignBills(billvos);

		HashMap map1 = new HashMap();
		map1.put("notechecked", "notechecked");

		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
				.getInstance().lookup(IplatFormEntry.class.getName());
		Object retObj = iIplatFormEntry.processAction("APPROVE"
				+ orderVOs[0].getHVO().getCreator(), orderVOs[0].getHVO()
				.getVtrantypecode(), null, orderVOs[0], null, map1);
		

		
		return orderVOs;
	}

	public OrderVO chg2BillVo(JSONObject bill) throws DAOException,
			BusinessException {
//		JSONObject jsonObject = bill.getJSONObject("bill");
		JSONArray jsonArray = bill.getJSONArray("body");
		String pre_vbillcode = jsonArray.getJSONObject(0).getString("pre_vbillcode");
		OrderVO order = new OrderVO();
		OrderHeaderVO hvo = new OrderHeaderVO();
		if(StringUtil.isNotEmpty(pre_vbillcode)){
			PraybillVO[] queyrArriveVO = queyrArriveVO(pre_vbillcode);
			OrderVO[] orders = null;

			//转单
			if (queyrArriveVO != null && queyrArriveVO.length > 0) {
				orders = (OrderVO[])PfUtilTools.runChangeDataAry("20", "21", queyrArriveVO);
			}
			order = orders[0];
		}
		
		hvo = chg2Hvo(bill,  hvo);
		//  并发控制
		isLock(hvo.getVtrantypecode(), hvo.getVdef1(), hvo.getPk_org());
		order.setParent(hvo);
		List<OrderItemVO> list = chg2Bodys(order, bill);
		order.setChildrenVO(list.toArray(new OrderItemVO[list.size()]));
		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(order);
		return order;
	}

	private void checkData(OrderVO resvo) throws BusinessException {
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}
	}



	public List<OrderItemVO> chg2Bodys(OrderVO gvo, JSONObject preBillJson)
			throws BusinessException {
		if (preBillJson == null || preBillJson.size() == 0) {
			throw new BusinessException("传入表体数据不能为空");
		}
		List<OrderItemVO> list = new ArrayList<OrderItemVO>();

		OrderHeaderVO hvo = gvo.getHVO();
		OrderItemVO[] bvos = gvo.getBVO();
		JSONArray childArray = preBillJson.getJSONArray("body"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		String pre_vbillcode = childArray.getJSONObject(0).getString(
				"pre_vbillcode");
		//组装成map集合
		for (int i = 0; i < childArray.size(); i++) {
			Map childVomap = (Map) childArray.get(i);
			//如果是请购单生成
			if(StringUtil.isNotEmpty(pre_vbillcode) 
					&& StringUtil.isEmpty((String)childVomap.get("pre_pk_detail"))
					&& ((String)childVomap.get("pre_pk_detail")).length() > 10){
				for (OrderItemVO item : bvos) {
					if(item.getCsourcebid().equals(childVomap.get("pre_pk_detail"))){
						item = chg2Body(childVomap, hvo, item);
						list.add(item);
					}
				}
			}else{
				//如果是不根据请购单生成
				//如果请购单生成并且表体请购单主键不匹配请购单
				OrderItemVO bvo = new OrderItemVO();
				bvo = chg2Body(childVomap, hvo, bvo);
				list.add(bvo);
			}
		}
		if(list == null || list.size() == 0){
			throw new BusinessException("未匹配到表体数据 请检查请购单");
		}
		return list;

	}
	//根据来源单据主键查找转库单
		protected PraybillVO[] queyrArriveVO(String vbillcode) throws BusinessException {
			VOQuery<PraybillHeaderVO> query = new VOQuery<PraybillHeaderVO>(PraybillHeaderVO.class);
			PraybillHeaderVO[] hvos = query.query(" and vbillcode='" + vbillcode  + "'", null);
			PraybillVO[] arrive = null;
			if (hvos == null || hvos.length == 0) {
				throw new BusinessException("未找到对应的转库单，请检查来源单据号是否正确");
			} else {
				BillQuery<PraybillVO> billQry = new BillQuery<PraybillVO>(PraybillVO.class);
				arrive = billQry.query(VOEntityUtil.getVOsNotRepeatValue(hvos,"pk_praybill"));
		
			}
			return arrive;
		}

}
