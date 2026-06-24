package nccloud.pubimpl.ct.price.event.after.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.ct.price.ICtPriceForGH;
import nc.itf.ct.reference.ec.EcRegionQueryService;
import nc.pubitf.initgroup.InitGroupQuery;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.commons.lang.StringUtils;
/**
 * 
 * @description 设置区域信息的工具类，物料和物料分类编辑后都会用到 
 * @author zhaoypm
 * @time 2019-4-8 下午4:43:30
 * @since ncc1.0
 */
public class RegionUtil {
	/**
	 * 这是适用区域信息
	 * 
	 * @param billvo
	 */
	public void setRegionInfo(AggCtPriceVO billvo) {
		CtPriceHeaderVO head = billvo.getParentVO();
		String pk_org = head.getPk_org();
		String pk_srcmaterial = head.getPk_material();
		String pk_marbasclass = head.getPk_marbasclass();
		if (StringUtils.isEmpty(pk_marbasclass)
				&& StringUtils.isEmpty(pk_srcmaterial)) {
			// 如果物料分类与物料同时为空，清除区域信息
			this.clearRegion(billvo);
			return;
		}
		if (StringUtils.isEmpty(pk_marbasclass)) {
			pk_marbasclass = this.queryMarbasclassByMaterial(pk_srcmaterial);
		}
		// 区域信息查询map Map<物料分类pk+主组织, 适用采购组织数组>
		Map<String, String[]> marclassOrgMap = this.getQueryParam(billvo,
				pk_org + pk_marbasclass);
		// 查询区域信息：Map<物料分类pk+主组织+采购组织pk, 区域name>
		if (marclassOrgMap.isEmpty()) {
			// 如果所含组织为空，清除区域信息
			this.clearRegion(billvo);
			return;
		}
		if (this.isEbpurEnabled()) {
			try {
				Map<String, String> regionMap = EcRegionQueryService
						.queryRegionInfo(marclassOrgMap);
				this.setRegionInfo(billvo, regionMap, pk_org + pk_marbasclass);
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
		}
	}

	/**
	 * 清空表体行所有的适用区域字段
	 * 
	 * @param billvo
	 */
	private void clearRegion(AggCtPriceVO billvo) {
		CtPriceBodyVO[] body = billvo.getChildrenVO();
		for (CtPriceBodyVO row : body) {
			row.setOrgRegion(null);
		}

	}

	private String queryMarbasclassByMaterial(String pk_material) {
		ICtPriceForGH service = NCLocator.getInstance().lookup(ICtPriceForGH.class);
		try {
			Map<String, String> resultMap = service
					.queryMarbasclassByMaterial(new String[] { pk_material });
			return resultMap.get(pk_material);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
			return null;
		}
	}

	private Map<String, String[]> getQueryParam(AggCtPriceVO billvo,
			String pk_marbasclass) {
		Map<String, String[]> marclassOrgMap = new HashMap<String, String[]>();
		Set<String> puorgs = new HashSet<String>();
		CtPriceBodyVO[] body = billvo.getChildrenVO();
		for (CtPriceBodyVO row : body) {
			String pk_puorg = row.getPk_puorg();
			if (StringUtils.isEmpty(pk_puorg)) {
				continue;
			}
			puorgs.add(pk_puorg);
		}
		if (puorgs.isEmpty()) {
			return marclassOrgMap;
		}
		marclassOrgMap.put(pk_marbasclass, puorgs.toArray(new String[0]));
		return marclassOrgMap;
	}

	private boolean isEbpurEnabled() {
		try {
			return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
					.getGroupId(), "EC20");
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrappException(e);
		}
		return false;
	}
	
	/**
	 * 设置适用区域信息
	 * @param billvo
	 * @param resultMap
	 * @param pk_marbasclass
	 */
	private void setRegionInfo(AggCtPriceVO billvo,
			Map<String, String> resultMap, String pk_marbasclass) {
		CtPriceBodyVO[] body = billvo.getChildrenVO();
		if (resultMap == null || resultMap.isEmpty()) {
			for (CtPriceBodyVO row : body) {
				row.setOrgRegion(null);
			}
			return;
		}
		for (CtPriceBodyVO row : body) {
			String key = this.getKey(row, pk_marbasclass);
			String region = resultMap.get(key);
			row.setOrgRegion(region);
		}
	}
	private String getKey(CtPriceBodyVO row, String pk_marbasclass) {
		String pk_puorg = row.getPk_puorg();
		return pk_marbasclass + pk_puorg;
	}
}
