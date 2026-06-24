package nccloud.pubimpl.ct.price.service;

import nc.vo.pub.BusinessException;
import nccloud.dto.ct.price.entity.CtPriceDynamicColumn;
import nccloud.pubitf.ct.price.service.ICtPriceDynamicColumnService;
import nccloud.vo.price.util.PriceTemplateProcessorForNCC;

public class CtPriceDynamicColumnServiceImpl implements ICtPriceDynamicColumnService {

	@Override
	public CtPriceDynamicColumn[] getDynamicColumn(String pk_ct_price, String pk_priceTemplate)
			throws BusinessException {
		PriceTemplateProcessorForNCC processor = new PriceTemplateProcessorForNCC();
		CtPriceDynamicColumn[] columns = processor.getPriceItemsByPKPriceTemplate(pk_ct_price, pk_priceTemplate);
		return columns;
	}

}
