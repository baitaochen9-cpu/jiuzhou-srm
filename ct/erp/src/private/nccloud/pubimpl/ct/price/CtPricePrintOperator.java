package nccloud.pubimpl.ct.price;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.IMetaDataDataSource;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.scale.BillVOScaleProcessor;
import nc.vo.pubapp.scale.PosEnum;
import nc.vo.scmpub.util.ArrayUtil;
import nc.vo.scmpub.util.StringUtil;
import nccloud.dto.ct.price.constants.CtPriceConstants;
import nccloud.dto.ct.price.entity.CtPriceDynamicColumn;
import nccloud.pubitf.scmpub.pub.print.BaseMetaPrintService;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;
import nccloud.vo.price.util.PriceTemplateProcessorForNCC;

/**
 * 
 * @description 价格信息表动态列打印
 * @author zhaoypm
 * @time 2019年6月26日 下午6:50:14
 * @since ncc1.0
 */
public class CtPricePrintOperator extends BaseMetaPrintService {

	@Override
	public Object[] getDatas(String[] ids) {
		ISCMPubQueryService service = NCLocator.getInstance().lookup(ISCMPubQueryService.class);
		try {
			AggCtPriceVO[] aggVOs = service.billquery(AggCtPriceVO.class, ids);
			this.processScale(aggVOs);
			return aggVOs;
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return null;
	}

	@Override
	protected IDataSource[] getDataSource(Object[] datas) {
		IDataSource[] ds = new IDataSource[datas.length];
		for (int i = 0; i < datas.length; i++) {
			ds[i] = new CtPriceMetaDataSource(new Object[] { datas[i] });
		}
		return ds;
	}

	private void processScale(AggCtPriceVO[] aggVOs) {
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		BillVOScaleProcessor processor = new BillVOScaleProcessor(pk_group, aggVOs);
		processor.setPriceCtlInfo(CtPriceConstants.pricekeys, PosEnum.body, null, CtPriceHeaderVO.CORIGCURRENCYID,
				PosEnum.head, null);
		processor.process();
	}

	private class CtPriceMetaDataSource implements IMetaDataDataSource {

		private static final long serialVersionUID = 2293130810750729772L;

		private Object[] printData;
		/**
		 * 存放所有价格项变量名称的数组
		 */
		private String[] priceItemNames = null;

		/**
		 * 结构是价格项名称->attrcode 如：服务费->npriceItem1
		 * 
		 */
		private Map<String, String> priceItemsNameAttrcodeMap = new HashMap<>();
		/**
		 * 值变量->值 如：priceItemValue1-[2.00]> ||
		 * priceItemValue7-[10.00,12.00,19.00]>
		 */
		private Map<String, List<String>> valuesMap = null;
		/**
		 * 动态列索引（数字后缀）
		 */
		private static final int INDEX_DYNAMICCOLUMN = 4;
		/**
		 * 每个变量所对应的值数组（list）前缀
		 */
		private static final String BUCKET_NAME_PRIFIX = "priceItemValue";

		public CtPriceMetaDataSource(Object[] printData) {
			this.printData = printData;
		}

		@Override
		public String[] getItemValuesByExpress(String itemExpress) {
			if (!StringUtil.isNullStringOrNull(itemExpress) && itemExpress.startsWith("priceItemName")) {
				return this.getPriceItemNames(itemExpress);
			} else if (!StringUtil.isNullStringOrNull(itemExpress) && itemExpress.startsWith("priceItemValue")) {
				return this.getPriceItemValues(itemExpress);
			}
			return null;
		}

		@Override
		public boolean isNumber(String itemExpress) {
			return false;
		}

		@Override
		public String[] getDependentItemExpressByExpress(String itemExpress) {
			if (!StringUtil.isNullStringOrNull(itemExpress) && itemExpress.startsWith("priceItem")) {
				int namePosition = Integer.parseInt(itemExpress.substring(itemExpress.length() - 1));
				return new String[] { "priceItemName" + namePosition };
			}
			return null;
		}

		@Override
		public String[] getAllDataItemExpress() {
			return null;
		}

		@Override
		public String[] getAllDataItemNames() {
			return null;
		}

		@Override
		public String getModuleName() {
			return null;
		}

		@Override
		public Object[] getMDObjects() {
			// 处理前方法
			if (getProcessor() != null) {
				return getProcessor().processData(printData);
			}
			return printData;
		}

		private String[] getPriceItemNames(String itemExpress) {
			if (ArrayUtil.isEmpty(this.priceItemNames)) {
				// 初始化存放价格项名称的数据 priceItemNames
				this.initNames();
			}
			// 根据itemExpress决定返回值
			int namePosition = Integer.parseInt(itemExpress.substring(itemExpress.length() - 1));
			if (namePosition > this.priceItemNames.length) {
				// 价格项不足以撑满预留单元格，如果不存在直接返回null
				return null;
			}
			if (namePosition < INDEX_DYNAMICCOLUMN) {
				// 非动态列
				return new String[] { this.priceItemNames[namePosition - 1] };
			} else if (namePosition == INDEX_DYNAMICCOLUMN) {
				// 动态列，返回剩余所有的元素值
				return Arrays.copyOfRange(this.priceItemNames, namePosition - 1, this.priceItemNames.length);
			}
			// 没有值的返回null;
			return null;
		}

		private String[] getPriceItemValues(String itemExpress) {
			if (null == this.valuesMap) {
				this.initValuesMap();
			}
			List<String> list = this.valuesMap.get(itemExpress);
			if (null == list) {
				return null;
			}
			return list.toArray(new String[] {});
		}

		/**
		 * 初始化值map
		 */
		private void initValuesMap() {
			AggCtPriceVO vo = (AggCtPriceVO) this.printData[0];
			CtPriceBodyVO[] children = vo.getChildrenVO();
			// 1.对表体行循环
			for (int childIndex = 0; childIndex < children.length; childIndex++) {
				// 2. 取出每行上，每个价格项的值
				for (int j = 1; j <= this.priceItemNames.length; j++) {
					// 当前索引
					int currIndex = childIndex * this.priceItemNames.length + j;
					// 指定当前索引的值应该存在那个数组（list）中
					List<String> bucket = this.getBucket(childIndex, currIndex);
					String priceItemName = this.priceItemNames[j - 1];
					CtPriceBodyVO body = children[childIndex];
					String priceItemValue = body.getAttributeValue(priceItemsNameAttrcodeMap.get(priceItemName))
							.toString();
					bucket.add(priceItemValue);
				}
			}
		}

		/**
		 * 判断当前单元格的索引是否属于动态列
		 * 
		 * @param childIndex
		 *            标题行索引，[0,n)
		 * @param currIndex
		 * @return
		 */
		private boolean isBelongToDynamicColumn(int childIndex, int currIndex) {
			// 当前行的动态列最小索引
			int left = childIndex * this.priceItemNames.length + CtPriceMetaDataSource.INDEX_DYNAMICCOLUMN;
			// 当前行的动态列最大索引
			int right = (childIndex + 1) * this.priceItemNames.length;
			return currIndex >= left && currIndex <= right;
		}

		/**
		 * 计算当前索引应该存在哪个数组（list）中
		 * 
		 * @param childIndex
		 * @param currIndex
		 * @return
		 */
		private List<String> getBucket(int childIndex, int currIndex) {
			if (null == this.valuesMap) {
				this.valuesMap = new HashMap<>();
			}
			String bucketName = null;
			if (!this.isBelongToDynamicColumn(childIndex, currIndex)) {
				// 不是动态列
				int sufix = currIndex % this.priceItemNames.length == 0 ? this.priceItemNames.length
						: currIndex % this.priceItemNames.length;
				bucketName = CtPriceMetaDataSource.BUCKET_NAME_PRIFIX + sufix;
			} else {
				bucketName = CtPriceMetaDataSource.BUCKET_NAME_PRIFIX + INDEX_DYNAMICCOLUMN;
			}
			List<String> bucket = valuesMap.get(bucketName);
			if (null == valuesMap.get(bucketName)) {
				bucket = new ArrayList<>();
				valuesMap.put(bucketName, bucket);
			}
			return bucket;

		}

		/**
		 * 初始化价格项变量名称
		 */
		private void initNames() {
			AggCtPriceVO ctPrice = (AggCtPriceVO) this.printData[0];
			CtPriceHeaderVO parentVO = ctPrice.getParentVO();
			String pk_pricetemplet = parentVO.getPk_pricetemplet();
			String pk_ct_price = parentVO.getPk_ct_price();
			CtPriceDynamicColumn[] items = null;
			try {
				items = new PriceTemplateProcessorForNCC().getPriceItemsByPKPriceTemplate(pk_ct_price, pk_pricetemplet);
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
			if (!ArrayUtil.isEmpty(items)) {
				List<String> nameList = new ArrayList<>();
				for (int i = 0; i < items.length; i++) {
					CtPriceDynamicColumn item = items[i];
					String name = item.getLabel();
					nameList.add(name);
					priceItemsNameAttrcodeMap.put(name, item.getAttrCode());
				}
				this.priceItemNames = nameList.toArray(new String[] {});
			} else {
				this.priceItemNames = new String[] {};
			}
		}
	}
}
