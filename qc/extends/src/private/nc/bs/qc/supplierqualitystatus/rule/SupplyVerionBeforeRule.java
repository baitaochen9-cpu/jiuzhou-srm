/*
 * @(#)SupplyVerionBeforeRule.java  2011-2-22 下午03:17:46
 *
 * Copyright Ufsoft. All rights reserved.
 */

package nc.bs.qc.supplierqualitystatus.rule;

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
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <p>
 * <b>补充版本信息业务规则</b>
 * </p>
 */
public abstract class SupplyVerionBeforeRule implements IRule {

	@Override
	public void process(Object[] vos) {
		if (ArrayUtils.isEmpty(vos))
			return;

		// 物料版本带出物料字段
		Set<String> pk_material_vs = new HashSet<String>();

		for (Object vo : vos) {
			IBill bill = (IBill) vo;
			String vid = (String) bill.getParent().getAttributeValue(
					getVidField());

			if (!StringUtils.isEmpty(vid)) {
				pk_material_vs.add(vid);
			}
			Class<? extends ISuperVO>[] clazz = getBodyClass();
			if (clazz == null || clazz.length == 0)
				continue;
			for (int i = 0; i < clazz.length; i++) {
				ISuperVO[] tbody = bill.getChildren(clazz[i]);

				if (tbody == null || tbody.length == 0)
					continue;
				for (ISuperVO body : tbody) {
					vid = (String) body.getAttributeValue(getVidField());
					if (StringUtils.isEmpty(vid)) {
						pk_material_vs.add(vid);
					}
				}
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

			for (Object vo : vos) {
				IBill bill = (IBill) vo;
				String vid = (String) bill.getParent().getAttributeValue(
						getVidField());
				String oid = (String) bill.getParent().getAttributeValue(
						getOidField());
				if (StringUtils.isNotEmpty(vid) && StringUtils.isEmpty(oid)) {
					if (materialVos.get(vid) != null) {
						bill.getParent().setAttributeValue(getOidField(),
								materialVos.get(vid).getPk_source());
					}
				}

				Class<? extends ISuperVO>[] clazz = getBodyClass();
				if (clazz == null || clazz.length == 0)
					continue;
				for (int i = 0; i < clazz.length; i++) {
					ISuperVO[] tbody = bill.getChildren(clazz[i]);

					if (tbody == null || tbody.length == 0)
						continue;
					for (ISuperVO body : tbody) {
						vid = (String) body.getAttributeValue(
								getVidField());
						oid = (String)body.getAttributeValue(
								getOidField());
						if (StringUtils.isNotEmpty(vid)
								&& StringUtils.isEmpty(oid)) {
							if (materialVos.get(vid) != null) {
								body.setAttributeValue(
										getOidField(),
										materialVos.get(vid).getPk_source());
							}
						}
					}
				}
			}

		}
	}

	public abstract String getOidField();

	public abstract String getVidField();

	public abstract Class<? extends ISuperVO>[] getBodyClass();
}
