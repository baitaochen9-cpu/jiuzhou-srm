package nc.impl.ct.saledaily;

import java.util.HashMap;
import java.util.Map;

import nc.bs.ct.saledaily.query.SaleQueryBP;
import nc.bs.framework.common.NCLocator;
import nc.bs.scmpub.page.BillPageLazyQuery;
import nc.impl.pubapp.pattern.data.bill.EfficientBillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.pubitf.so.m30.api.ISaleOrderQueryAPI;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.page.PageQueryVO;
import nc.vo.scmpub.util.StringUtil;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderViewVO;

public class SaledailyMaintainAppImpl implements ISaledailyMaintainApp {

	@Override
	public PageQueryVO queryMZ3App(IQueryScheme queryScheme) throws BusinessException {
		PageQueryVO page = null;
		try {
			page = new SaleQueryBP().query(queryScheme);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return page;

	}

	@Override
	public AggCtSaleVO[] queryMZ3App(String[] ids) throws BusinessException {
		BillPageLazyQuery<AggCtSaleVO> query = new BillPageLazyQuery<AggCtSaleVO>(AggCtSaleVO.class);
		AggCtSaleVO[] bills = null;
		try {
			bills = query.queryPageBills(ids);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return bills;
	}

	@Override
	public Map<String, Object> queryViewVo(String pk, String bid, String rowno, String material, String marbasclass)
			throws BusinessException {
		// 根据物料 合同id 以及合同明细id 查询采购订单表体数据
		VOQuery<SaleOrderBVO> orderBVOQuery = new VOQuery<SaleOrderBVO>(SaleOrderBVO.class);
		AggCtSaleVO ctSaleVo = queryMZ3App(new String[] { pk })[0];
		CtSaleBVO[] ctSaleBvos = ctSaleVo.getCtSaleBVO();
		// 根据物料主键获取当前物料的合同子表的数据
		CtSaleBVO[] currentCtSaleBVO = new CtSaleBVO[1];
		String ccontractrowid = null;
		String condition = null;
		if (!StringUtil.isEmptyTrimSpace(bid) && !bid.equals("null")) {
			for (CtSaleBVO bvo : ctSaleBvos) {
				if (bid.equals(bvo.getPk_ct_sale_b())) {
					currentCtSaleBVO[0] = bvo;
					break;
				}
			}
			ccontractrowid = bid;
			condition = "and so_saleorder_b.cctmanagebid = '" + ccontractrowid + "' and so_saleorder_b.cctmanageid = '"
					+ pk + "'";
		} else
//			if (!StringUtil.isEmptyTrimSpace(material)) {
//			for (CtSaleBVO bvo : ctSaleBvos) {
//				if (material.equals(bvo.getPk_material())) {
//					currentCtSaleBVO[0] = bvo;
//					break;
//				}
//			}
//			ccontractrowid = currentCtSaleBVO[0].getPk_ct_sale_b();
//			condition = "and so_saleorder_b.cmaterialvid = '" + material + "' and so_saleorder_b.cctmanageid = '" + pk
//					+ "' and so_saleorder_b.cctmanagebid ='" + ccontractrowid + "'";
//		} else 
		if (!StringUtil.isEmptyTrimSpace(rowno) && !rowno.equals("null")) {
			for (CtSaleBVO bvo : ctSaleBvos) {
				if (rowno.equals(bvo.getCrowno())) {
					currentCtSaleBVO[0] = bvo;
					break;
				}
			}
			condition = "and so_saleorder_b.vfirstrowno  = '" + rowno + "' and so_saleorder_b.cctmanageid = '" + pk
					+ "'";
		}
		ctSaleVo.setCtSaleBVO(currentCtSaleBVO);
		SaleOrderBVO[] soOrderBVOs = orderBVOQuery.query(condition, null);
		String[] bids = new String[soOrderBVOs.length];

		for (int i = 0; i < soOrderBVOs.length; i++) {
			bids[i] = soOrderBVOs[i].getCsaleorderbid();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("ctSaleAggVO", ctSaleVo);
		if (bids.length > 0) {
			ISaleOrderQueryAPI service = NCLocator.getInstance().lookup(ISaleOrderQueryAPI.class);
			SaleOrderViewVO[] viewVos = service.queryViewVOByBIDs(bids);
			result.put("saleOrderViewVOs", viewVos);
		}
		return result;
	}

	@Override
	public AggCtSaleVO queryMZ3AppByVbillcode(String billid) throws BusinessException {
		// 根据物料 合同id 以及合同明细id 查询采购订单表体数据
		try {
			SqlBuilder sql = new SqlBuilder();
			sql.append(" from ct_sale ");
			sql.append(" where ");
			sql.append(CtSaleVO.PK_CT_SALE, billid);
//			sql.append(" and ");
//			sql.append(CtSaleVO.BSHOWLATEST, UFBoolean.TRUE);
			AggCtSaleVO[] vos = new EfficientBillQuery<AggCtSaleVO>(AggCtSaleVO.class).query(sql.toString());
			String vbillcode =  vos[0].getParentVO().getVbillcode();
			//变更后合同 生效后主键变为原始版本，此处通过报账传递的pk查询vbillcode再查出最新版本有效数据
			SqlBuilder sql2 = new SqlBuilder();
			sql2.append(" from ct_sale ");
			sql2.append(" where ");
			sql2.append(CtSaleVO.VBILLCODE, vbillcode);
			sql2.append(" and "); 
			sql2.append(CtSaleVO.BSHOWLATEST, UFBoolean.TRUE);
			sql2.append(" and ");
			sql2.append(CtSaleVO.DR, 0);
			return new EfficientBillQuery<AggCtSaleVO>(AggCtSaleVO.class).query(sql2.toString())[0];
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
			return null;
		}

	}
}
