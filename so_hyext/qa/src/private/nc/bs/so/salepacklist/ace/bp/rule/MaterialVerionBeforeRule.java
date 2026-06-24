/*
 * @(#)SupplyVerionBeforeRule.java  2011-2-22 下午03:17:46
 *
 * Copyright Ufsoft. All rights reserved.
 */

package nc.bs.so.salepacklist.ace.bp.rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.logging.Log;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.CollectionUtils;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.am.common.util.StringUtils;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;
import nc.vo.so.salepacklist.AggSalePackListHVO;

/**
 * <p>
 * <b>补充版本信息业务规则</b>
 * </p>
 */
public class MaterialVerionBeforeRule implements IRule<AggSalePackListHVO> {

	@Override
	public void process(AggSalePackListHVO[] vos) {
		if (ArrayUtils.isEmpty(vos))
			return;

		// 物料版本带出物料字段
		Set<String> pk_material_vs = new HashSet<String>();
		for (AggSalePackListHVO vo : vos) {
			String vid = (String) vo.getParentVO().getPk_material();

			if (!StringUtils.isEmpty(vid)) {
				pk_material_vs.add(vid);
			}
		}

		if (CollectionUtils.isNotEmpty(pk_material_vs)) {
			Map<String, MaterialVO> materialVos = new HashMap<String, MaterialVO>();

			IMaterialPubService materialService = AMProxy
					.lookup(IMaterialPubService.class);

			String[] fields = new String[] { MaterialVO.PK_MATERIAL, // 物料版本id
					MaterialVO.PK_SOURCE, MaterialVO.CODE // 物料编码
			};
			try {
				materialVos = materialService.queryMaterialBaseInfoByPks(
						pk_material_vs.toArray(new String[0]), fields);
			} catch (BusinessException ex) {
				Log.getInstance(this.getClass()).error(ex);
				ExceptionUtils.asBusinessRuntimeException(ex);
			}

			for (AggSalePackListHVO vo : vos) {
				String vid = vo.getParentVO().getPk_material();
				String oid = vo.getParentVO().getPk_srcmaterial();
				if (StringUtils.isNotEmpty(vid) && StringUtils.isEmpty(oid)) {
					if (materialVos.get(vid) != null) {
						vo.getParentVO().setPk_srcmaterial(
								materialVos.get(vid).getPk_source());
					}
				}
			}
		}
	}
}
