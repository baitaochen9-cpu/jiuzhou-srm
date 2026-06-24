package nc.bs.pu.m23.maintain.rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.qc.ISupplierqualitystatusMaintain;
import nc.vo.am.common.util.StringUtils;
import nc.vo.pu.m23.entity.ArriveHeaderVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 
 * 厂商物料质量等级为：“待批准、不批准、已批准、认证、维持”；
 * 如果物料的生产厂商物料质量等级为“不批准”和“维持”则不允许采购；
 * 生产厂商物料质量等级为“待批准”允许采购、不允许到货及收货，生产厂商物料质量等级为“已批准和认证”状态不受限制）
 * @author yunfeng.li
 *
 */
public class ArriveQualiStatuskRule implements IRule<ArriveVO> {
	public void process(ArriveVO[] vos) {

		try {

			for (ArriveVO arriveVO : vos) {
				// 维持状态 不允许采购
				ISupplierqualitystatusMaintain maitian = (ISupplierqualitystatusMaintain) NCLocator
						.getInstance().lookup(
								ISupplierqualitystatusMaintain.class);
				// 物料
				ArriveHeaderVO headvo = arriveVO.getHVO();
				ArriveItemVO[] items = arriveVO.getBVO();
				if (headvo == null || items == null || items.length == 0)
					continue;
				Map<String, String> pk_material_vs = new HashMap<>();
				for (ArriveItemVO vo : items) {
					String vid = vo.getPk_material();
					if(VOStatus.DELETED == vo.getStatus())
						continue;
					if (!StringUtils.isEmpty(vid)) {
						pk_material_vs.put(vid, vo. getCproductorid() );
					}
				}
//				maitian.checkSupplierStatus(pk_material_vs,
//						headvo.getPk_group(), headvo.getPk_org(),
//						headvo.getPk_supplier(), "收货", "维持");
//				maitian.checkSupplierStatus(pk_material_vs,
//						headvo.getPk_group(), headvo.getPk_org(),
//						headvo.getPk_supplier(), "收货", "不批准");
				maitian.checkSupplierStatus(pk_material_vs,
						headvo.getPk_group(), headvo.getPk_org(),
						headvo.getPk_supplier(), "收货", "待批准");
			}
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrappException(e);
		}
	}

}
