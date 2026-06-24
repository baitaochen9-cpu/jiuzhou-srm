package nccloud.pubimpl.ct.purdaily.service;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.ct.purdaily.IPuLinkCtPrice;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.business.enumeration.FpricePattern;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPubillViewVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nccloud.pubitf.ct.purdaily.service.ILinkCtPriceInfo;

/**
 * @description 联查价格明细
 * @author xiahui
 * @date 创建时间：2019-3-8 上午9:15:49
 * @version ncc1.0
 **/
public class LinkCtPriceInfoImpl implements ILinkCtPriceInfo {

	@Override
	public AggCtPriceVO getCtPriceAggVO(Map<String, Object> ctMap) throws BusinessException {
		AggCtPriceVO priceVO = this.getCtPriceVO(ctMap);
		if(null==priceVO) {
			return priceVO;
		}
		this.setPriceVO(priceVO, (String) ctMap.get(CtAbstractVO.CTRANTYPEID));
		return priceVO;
	}

	/**
	 * 设置合同价格信息VO
	 * 
	 * @param priceVO
	 * @param ctrantypeid
	 * @throws BusinessException
	 */
	private void setPriceVO(AggCtPriceVO priceVO, String ctrantypeid) throws BusinessException {
		priceVO.setIsFromPurdaily(UFBoolean.TRUE);
		BusinessSetVO businessVO = NCLocator.getInstance().lookup(IBusinessTypeService.class)
				.queryBusinessVO(ctrantypeid);
		// 如果为总价，则价格信息表的总价为合同含税单价，基价不显示
		if (null != businessVO) {
			if (FpricePattern.MULTIPRICE.value().equals(businessVO.getFpricePattern())) {
				priceVO.setIsMultiPriceType(UFBoolean.TRUE);
			}
			if (FpricePattern.ALLPRICE.value().equals(businessVO.getFpricePattern())) {
				for (CtPriceBodyVO body : priceVO.getChildrenVO()) {
					body.setTotalprice(body.getBaseprice());
					body.setBaseprice(null);
				}
			}
		}
	}

	/**
	 * 获取合同价格信息
	 * 
	 * @param parentVO 采购合同表头信息
	 * @param bbill    采购合同表体信息
	 * @return
	 * @throws BusinessException
	 */
	private AggCtPriceVO getCtPriceVO(Map<String, Object> ctMap) throws BusinessException {
		CtPubillViewVO viewVO = new CtPubillViewVO();

		viewVO.setPk_ct_pu((String) ctMap.get(CtPuBVO.PK_CT_PU));
		viewVO.setPk_org((String) ctMap.get(CtPuBVO.PK_ORG));
		viewVO.setNqtorigtaxprice((UFDouble) ctMap.get(CtPuBVO.NQTORIGTAXPRICE));
		viewVO.setPk_ct_price((String) ctMap.get(CtPuBVO.PK_CT_PRICE));

		return NCLocator.getInstance().lookup(IPuLinkCtPrice.class).getCtPriceVO(viewVO);
	}

}
