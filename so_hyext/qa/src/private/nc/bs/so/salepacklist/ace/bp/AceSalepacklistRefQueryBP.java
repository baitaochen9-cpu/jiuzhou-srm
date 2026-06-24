package nc.bs.so.salepacklist.ace.bp;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.so.salepacklist.SalePackListHVO;

/**
 * 标准单据查询的BP
 */
public class AceSalepacklistRefQueryBP {

	public AggSalePackListHVO[] queryAllBillDatas(String where)
			throws BusinessException {
		VOQuery<DeliveryBVO> query = new VOQuery(DeliveryBVO.class);
		DeliveryBVO[] bvos = (DeliveryBVO[]) query.queryWithWhereKeyWord(
				" where so_delivery_b." + where
						+ " and nvl(so_delivery_b.vbdef19, 'N') in ('N','~') and nvl(so_delivery_b.vbdef7, 'N') in ('N','~')",
				null);

		if (bvos == null || bvos.length == 0)
			throw new BusinessException("该发货单不需要生成销售包装清单");
		AggSalePackListHVO[] bills = changeAggSalePackListHVO(bvos);

		if (bills == null || bills.length == 0)
			throw new BusinessException("该发货单不需要生成销售包装清单");
		return bills;
	}

	private AggSalePackListHVO[] changeAggSalePackListHVO(DeliveryBVO[] bvos)
			throws BusinessException {

		List<AggSalePackListHVO> alist = new ArrayList<>();
		for (DeliveryBVO bvo : bvos) {
			if (!StringUtil.isEmpty(bvo.getVbdef18())
					&& "Y".equals(bvo.getVbdef18())) {
				continue;
			}
			AggSalePackListHVO aggvo = new AggSalePackListHVO();
			SalePackListHVO hvo = getHeadVO(bvo);
			SalePackListBVO body = getBody(bvo);
			aggvo.setParentVO(hvo);
			// aggvo.setChildrenVO(new SalePackListBVO[] { body });
			alist.add(aggvo);
		}

		return alist.toArray(new AggSalePackListHVO[alist.size()]);
	}

	private SalePackListHVO getHeadVO(DeliveryBVO bvo) throws BusinessException {
		SalePackListHVO hvo = new SalePackListHVO();
		// 设置来源信息
		hvo.setSrcbillid(bvo.getCdeliveryid());// 来源id
		hvo.setSrcbilltype("4331");// 来源类型
		hvo.setCode(bvo.getCrowno());// 行号

		// 设置转换信息
		hvo.setPk_srcmaterial(bvo.getCmaterialid());// 物料id
		hvo.setPk_material(bvo.getCmaterialid());// 物料id
		hvo.setNgrosswt(bvo.getNnum());// 数量
		hvo.setPk_salepacklist(bvo.getCdeliverybid());// 来源行id
		hvo.setName(bvo.getVbatchcode());// 批次号
		hvo.setDef4(bvo.getPk_batchcode());// 批次号id
		hvo.setDef5(bvo.getCastunitid());// 计量单位id
		hvo.setPk_group(bvo.getPk_group());
		hvo.setPk_org(bvo.getPk_org());
		return hvo;
	}

	private SalePackListBVO getBody(DeliveryBVO bvo) throws BusinessException {
		SalePackListBVO body = new SalePackListBVO();
		return body;
	}

}
