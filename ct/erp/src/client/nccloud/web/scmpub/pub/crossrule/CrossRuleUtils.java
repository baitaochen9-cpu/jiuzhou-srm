package nccloud.web.scmpub.pub.crossrule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.scmpub.util.CollectionUtils;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.web.ui.config.Area;
import nccloud.framework.web.ui.config.Item;
import nccloud.framework.web.ui.config.PageTemplet;
import nccloud.framework.web.ui.conver.MetaPathFinder;
import nccloud.framework.web.ui.meta.ComponentType;
import nccloud.framework.web.ui.model.GridModel;
import nccloud.framework.web.ui.model.row.Cell;
import nccloud.framework.web.ui.model.row.Row;
import nccloud.framework.web.ui.pattern.form.Form;
import nccloud.framework.web.ui.pattern.grid.Grid;

public class CrossRuleUtils {

	/**
	 * 获取参照类型或下拉枚举类型数据的值
	 * 
	 * @param templet
	 * @param head
	 * @param body
	 * @return 对应字段元数据id和值的映射
	 */
	public static Map<String, String[]> queryValueMap(PageTemplet templet, Form head, Grid body) {
		Map<String, String[]> valueMap = new HashMap<String, String[]>();
		Map<String, List<Item>> meta2ItemMap = new HashMap<String, List<Item>>();
		Map<String, String> meta2AreacodeMap = new HashMap<String, String>();
		Set<String> areacodes = new HashSet<String>();
		areacodes.add(head.getModel().getAreacode());
		// 表体为空时，不取表体区域的字段
		if (body != null) {
			areacodes.add(body.getModel().getAreacode());
		}
		for (Area area : templet.getAllAreas()) {
			String areacode = area.getCode();
			if (areacodes.contains(areacode)) {
				// modify by xiahui 当表头和表体具有相同字段的时候，解决页面属性重复错误
				String key = area.getVometa() + "##" + area.getClazz();
				if (!meta2ItemMap.containsKey(key)) {
					meta2ItemMap.put(key, new ArrayList<Item>());
				}
				CollectionUtils.addArrayToList(meta2ItemMap.get(key), area.getItems());
				meta2AreacodeMap.put(key, areacode);
			}
		}
		Set<String> propertyKeySet = new HashSet<String>();
		for (String key : meta2ItemMap.keySet()) {
			String[] str = key.split("##");
			String meta = str[0];
			if (meta2ItemMap.get(key) != null) {
				GridModel model = null;
				if (head.getModel().getAreacode().equals(meta2AreacodeMap.get(key))) {
					model = head.getModel();
				} else if (body.getModel().getAreacode().equals(meta2AreacodeMap.get(key))) {
					model = body.getModel();
				}
				if (model == null) {
					continue;
				}
				for (Item item : meta2ItemMap.get(key)) {
					if (item.getItemtype().equals(ComponentType.Refer) || item.getItemtype().equals(ComponentType.Select)) {
						String code = item.getCode();
						String metaid = new MetaPathFinder().findMetaID(meta, code, str[1]);
						String value = getValue(model, code);
						if (value != null && !value.trim().equals("") && metaid != null) {
							if (propertyKeySet.isEmpty() || !propertyKeySet.contains(metaid))
								propertyKeySet.add(metaid);
							else
								ExceptionUtils.wrapBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("10140cr",
										"110140cr-0007")/*
																		 * 界面属性信息重复！
																		 */);
							valueMap.put(metaid, new String[] { value });
						}
					}
				}
			}
		}
		return valueMap;
	}

	/**
	 * 获取模板上确定字段的元数据id
	 * 
	 * @param templet
	 * @param code
	 * @return
	 */
	public static String getMetaID(PageTemplet templet, String currarea, String code) {
		for (Area area : templet.getAllAreas()) {
			if (area.getCode().equals(currarea)) {
				for (Item item : area.getItems()) {
					if (item.getCode().equals(code)) {
						return new MetaPathFinder().findMetaID(area.getVometa(), code, area.getClazz());
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取确定字段的值
	 * 
	 * @param model
	 * @param code
	 * @return
	 */
	public static String getValue(GridModel model, String code) {
		Row[] rows = model.getRows();
		if (rows != null && rows.length > 0) {
			Cell cell = rows[0].getCell(code);
			if (cell != null) {
				return (String) cell.getValue();
			}
		}
		return null;
	}
}
