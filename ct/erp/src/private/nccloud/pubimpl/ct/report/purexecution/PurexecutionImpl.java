package nccloud.pubimpl.ct.report.purexecution;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.pu.m21.IOrderQuery;
import nc.pub.report.util.ReportDrillBillUtil;
import nc.pubitf.sc.m61.pub.ISCOrderQuery;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderViewVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.sc.m61.entity.SCOrderItemVO;
import nc.vo.sc.m61.entity.SCOrderViewVO;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.pubitf.ct.report.service.IPurexecution;
import nccloud.pubitf.platform.workbench.IAppPage;

import com.ufida.report.anareport.base.FreeReportDrillParam;

/**
 * 采购合同执行情况联查实现类
 * 
 * @description TODO
 * @author zhengxinm
 * @date 2019-2-14 下午2:11:50
 * @version ncc1.0
 */
public class PurexecutionImpl implements IPurexecution {

	@Override
	public Map<String, Object> getUrlBuyConditions(QueryTreeFormatVO conditionTree,
			Map serializedObjMap) throws BusinessException {
		String opents = (String) serializedObjMap.get("opents");
		// 调用平台的构建param方法
		ReportDrillBillUtil utils = new ReportDrillBillUtil();
		FreeReportDrillParam drillParam = utils.drillBill(serializedObjMap,
				conditionTree, serializedObjMap, opents);
		String ctbillcode = (String) drillParam.getTraceDatas()[0]
				.getValue("CTBILLCODE");
		String pk_material = (String) drillParam.getTraceDatas()[0]
				.getValue("PK_MATERIAL");
		String pk_org = (String) drillParam.getTraceDatas()[0]
				.getValue("PK_ORG_V");
		String bsc = (String) drillParam.getTraceDatas()[0]
				.getValue("BSC");
		boolean isbsc = bsc !=null && "Y".equals(bsc);
		Map<String , Object> result = new HashMap<String , Object>();
		result.put("bsc", isbsc);
		if(isbsc) {
			VOQuery<SCOrderItemVO> orderBVOQuery = new VOQuery<SCOrderItemVO>(
					SCOrderItemVO.class);
			String condition = "and sc_order_b.pk_material = '" + pk_material
					+ "' and sc_order_b.ctvbillcode = '" + ctbillcode + "'";
			SCOrderItemVO[] scOrderBVOs = orderBVOQuery.query(condition, null);
			String[] bids = new String[scOrderBVOs.length];
			for (int i = 0; i < scOrderBVOs.length; i++) {
				bids[i] = scOrderBVOs[i].getPk_order_b();
			}
			if(bids.length > 0) {
				ISCOrderQuery service = NCLocator.getInstance().lookup(
						ISCOrderQuery.class);
				Map<String, SCOrderViewVO> viewvos = service.querySCOrderView(bids, new String[] {});
				StringBuilder billcodes = new StringBuilder();
				for (int i = 0;i<bids.length;i++) {
					billcodes.append(viewvos.get(bids[i]).getVbillcode());
					if (i != bids.length -1 ) {
						billcodes.append(",");
					}
				}
				result.put("billcodes", billcodes.toString());
				return result;
			}
		}
		IAppPage service = NCLocator.getInstance().lookup(IAppPage.class);
		String url = service.queryPageUrl("400413232", "400413232_card");
		url = url.replace("/nccloud/resources", "");
		url = url + "?ctbillcode=" + ctbillcode;
		if(pk_material != null) {
			url = url + "&pk_material=" + pk_material;
		}
		if(pk_org != null) {
			url = url + "&pk_org=" + pk_org;
		}
		result.put("url", url);
		return result;
	}

	@Override
	public Map<String, Object> getPurExecutionDetail(String pk_org, String ctbillcode,
			String pk_material) throws BusinessException {
		AggCtPuVO[] ctPuAggVO = getAggCtPuVo(pk_org, ctbillcode, pk_material);
		
		String ccontractrowid = ctPuAggVO[0].getCtPuBVO()[0].getPk_ct_pu_b();
		// 根据物料 合同id 以及合同明细id 查询采购订单表体数据
		VOQuery<OrderItemVO> orderBVOQuery = new VOQuery<OrderItemVO>(
				OrderItemVO.class);
		String condition = "and po_order_b.pk_material = '" + pk_material
				+ "' and po_order_b.ccontractid = '" + ctPuAggVO[0].getParent().getPrimaryKey()
				+ "' and po_order_b.ccontractrowid ='" + ccontractrowid + "'";
		OrderItemVO[] poOrderBVOs = orderBVOQuery.query(condition, null);
		String[] bids = new String[poOrderBVOs.length];

		for (int i = 0; i < poOrderBVOs.length; i++) {
			bids[i] = poOrderBVOs[i].getPk_order_b();
		}
		IOrderQuery service = NCLocator.getInstance().lookup(
				IOrderQuery.class);
		Map<String, OrderViewVO> resultMap = service.queryOrderViewsByIds(bids);
		OrderViewVO[] orderViewVos = new OrderViewVO[bids.length];
		for (int i = 0;i<bids.length;i++) {
			orderViewVos[i] = resultMap.get(bids[i]);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("ctPuAggVO", ctPuAggVO[0]);
		result.put("orderViewVos", orderViewVos);
		return result;
	}
	@Override
	public AggCtPuVO[] getAggCtPuVo(String pk_org, String ctbillcode,
			String pk_material){
		AggCtPuVO[] ctPuAggVO = null;
		BillQuery<AggCtPuVO> query = new BillQuery<AggCtPuVO>(AggCtPuVO.class);
		SqlBuilder sql = new SqlBuilder();
		//增加组织过滤条件，只拿单据号可能会有重复的， add by yangls7
		sql.append("select ct_pu.PK_CT_PU from ct_pu where ct_pu.dr = 0 and  ct_pu.vbillcode = '"
				+ ctbillcode + "' and ct_pu.pk_org_v = '" + pk_org + "'");
		DataAccessUtils dao = new DataAccessUtils();
		IRowSet rowset = dao.query(sql.toString());
		String pk_ct_pu = "";
		if (rowset.next()) {
			pk_ct_pu = rowset.getString(0);
		}
		if (!"".equals(pk_ct_pu)) {
			ctPuAggVO = query.query(new String[] { pk_ct_pu });
			if (pk_material == null)
				return ctPuAggVO;
		}
		// 根据物料主键过滤掉非当前物料的合同子表的物料信心
		CtPuBVO[] ctPuBVO = ctPuAggVO[0].getCtPuBVO();
		CtPuBVO[] currentCtPuBVO = new CtPuBVO[1];
		for (CtPuBVO bvo : ctPuBVO) {
			if (bvo.getPk_material() != null && bvo.getPk_material().equals(pk_material)) {
				currentCtPuBVO[0] = bvo;
				break;
			}
		}
		ctPuAggVO[0].setCtPuBVO(currentCtPuBVO);
		return ctPuAggVO;
		
	}
}
