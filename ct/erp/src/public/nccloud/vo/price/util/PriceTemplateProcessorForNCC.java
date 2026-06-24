package nccloud.vo.price.util;

import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.pubitf.ct.pricetemplet.IPubPriceTemplet;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.ct.pricetemplet.entity.PriceTempletItemVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.scale.BillVOScaleProcessor;
import nc.vo.pubapp.scale.PosEnum;
import nc.vo.scmpub.util.ArrayUtil;
import nccloud.dto.ct.price.constants.CtPriceConstants;
import nccloud.dto.ct.price.entity.CtPriceDynamicColumn;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;

/**
 * 
 * @description 主要提供了根据价格模板主键查询价格项的方法
 * @author zhaoypm
 * @time 2019-4-9 上午9:51:34
 * @since ncc1.0
 */
public class PriceTemplateProcessorForNCC {
	public static final String[] npriceitems = new String[] { CtPriceBodyVO.NPRICEITEM1, CtPriceBodyVO.NPRICEITEM2,
			CtPriceBodyVO.NPRICEITEM3, CtPriceBodyVO.NPRICEITEM4, CtPriceBodyVO.NPRICEITEM5, CtPriceBodyVO.NPRICEITEM6,
			CtPriceBodyVO.NPRICEITEM7, CtPriceBodyVO.NPRICEITEM8, CtPriceBodyVO.NPRICEITEM9, CtPriceBodyVO.NPRICEITEM10,
			CtPriceBodyVO.NPRICEITEM11, CtPriceBodyVO.NPRICEITEM12, CtPriceBodyVO.NPRICEITEM13,
			CtPriceBodyVO.NPRICEITEM14, CtPriceBodyVO.NPRICEITEM15, CtPriceBodyVO.NPRICEITEM16,
			CtPriceBodyVO.NPRICEITEM17, CtPriceBodyVO.NPRICEITEM18, CtPriceBodyVO.NPRICEITEM19,
			CtPriceBodyVO.NPRICEITEM20, CtPriceBodyVO.NPRICEITEM21, CtPriceBodyVO.NPRICEITEM22,
			CtPriceBodyVO.NPRICEITEM23, CtPriceBodyVO.NPRICEITEM24, CtPriceBodyVO.NPRICEITEM25,
			CtPriceBodyVO.NPRICEITEM26, CtPriceBodyVO.NPRICEITEM27, CtPriceBodyVO.NPRICEITEM28,
			CtPriceBodyVO.NPRICEITEM29, CtPriceBodyVO.NPRICEITEM30 };

	/**
	 * 通过价格模板主键获取价格项
	 * 
	 * @param pk_priceTemplate
	 * @return
	 * @throws BusinessException
	 */
	public CtPriceDynamicColumn[] getPriceItemsByPKPriceTemplate(String pk_ct_price, String pk_priceTemplate)
			throws BusinessException {
		IPubPriceTemplet service = NCLocator.getInstance().lookup(IPubPriceTemplet.class);
		// pk_template => priceTemplateItemVO[]
		Map<String, PriceTempletItemVO[]> priceTemplateMap = service
				.priceItemQryByIDs(new String[] { pk_priceTemplate });
		PriceTempletItemVO[] priceTempletItemVOs = priceTemplateMap.get(pk_priceTemplate);
		// IROWNO(行标号)排序
		PriceTempletItemVO[] bodyRows = sortPriceTempItems(priceTempletItemVOs);
		String nameX = getCpriceItemName();

		CtPriceDynamicColumn[] items = covertChildren2Items(bodyRows, nameX);
		setValues(items, pk_ct_price);
		return items;
	}

	private CtPriceDynamicColumn[] setValues(CtPriceDynamicColumn[] items, String pk_ct_price) {
		ISCMPubQueryService service = NCLocator.getInstance().lookup(ISCMPubQueryService.class);
		AggCtPriceVO[] vos = null;
		try {
			vos = service.billquery(AggCtPriceVO.class, new String[] { pk_ct_price });
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		if (!ArrayUtil.isEmpty(vos)) {
			this.processScale(vos);
			AggCtPriceVO vo = vos[0];
			CtPriceBodyVO[] children = vo.getChildrenVO();
			for (CtPriceDynamicColumn column : items) {
				String attrcode = column.getAttrCode();
				for (CtPriceBodyVO bodyVO : children) {
					String value = bodyVO.getAttributeValue(attrcode).toString();
					column.setValue(value);
				}
			}

		}
		return items;
	}

	private void processScale(AggCtPriceVO[] aggVOs) {
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		BillVOScaleProcessor processor = new BillVOScaleProcessor(pk_group, aggVOs);
		processor.setPriceCtlInfo(CtPriceConstants.pricekeys, PosEnum.body, null, CtPriceHeaderVO.CORIGCURRENCYID,
				PosEnum.head, null);
		processor.process();
	}

	/**
	 * 按照IROWNO(行标号)排序
	 * 
	 * @param items
	 * @return
	 */
	public PriceTempletItemVO[] sortPriceTempItems(PriceTempletItemVO[] items) {
		PriceTempletItemVO temp = null;
		int min = -1;
		for (int i = 0; i < items.length; i++) {
			temp = items[i];
			for (int j = i + 1; j < items.length - 1; j++) {
				if (items[j].getIrowno().intValue() < temp.getIrowno().intValue()) {
					temp = items[j];
					min = j;
				}
			}
			if (min > -1) {
				items[min] = items[i];
				items[i] = temp;
			}
		}
		return items;
	}

	/**
	 * 清空价格项
	 * 
	 * @param items
	 */
	public void clearPriceitemsValue(PriceTempletItemVO[] items) {
		for (PriceTempletItemVO item : items) {
			for (String npriceitem : PriceTemplateProcessorForNCC.npriceitems) {
				item.setAttributeValue(npriceitem, null);
			}
		}
	}

	/**
	 * 获取价格项编码
	 * 
	 * @return
	 */
	private String getCpriceItemName() {
		MultiLangText text = new MultiLangText();
		int lang = text.getCurrLangIndex() + 1;
		String field = PriceTempletItemVO.CPRICEITEMNAME;
		if (lang > 1) {
			field = field + lang;
		}
		return field;
	}

	/**
	 * 获取价格项多语名称
	 * 
	 * @param item
	 *            子表行数据vo
	 * @param nameX
	 *            多语名称
	 * @return
	 */
	private String getCpriceItemNameValue(PriceTempletItemVO item, String nameX) {
		Object value = item.getAttributeValue(nameX);
		// 如果当前语种没有值，就取主语种的
		if (ValueUtil.isEmpty(value)) {
			value = item.getAttributeValue(PriceTempletItemVO.CPRICEITEMNAME);
		}
		// 中文建库 验证英文环境 抛空引用 所以在这加上判断 by diaoxy for NCdp204655405
		if (null == value) {
			value = "";
		}
		return value.toString();
	}

	/**
	 * 将表体数据转换成动态列数组（保证顺序）
	 * 
	 * @param bodyRows
	 */
	private CtPriceDynamicColumn[] covertChildren2Items(PriceTempletItemVO[] bodyRows, String nameX) {
		CtPriceDynamicColumn[] items = new CtPriceDynamicColumn[bodyRows.length];
		for (int i = 0; i < bodyRows.length; i++) {
			PriceTempletItemVO itemVO = bodyRows[i];
			String nameValue = getCpriceItemNameValue(itemVO, nameX);
			CtPriceDynamicColumn column = new CtPriceDynamicColumn();
			column.setAttrCode(PriceTemplateProcessorForNCC.npriceitems[i]);
			column.setLabel(nameValue);
			items[i] = column;
		}
		return items;
	}
}
