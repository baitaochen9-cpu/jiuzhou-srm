package nc.bs.ic.m45.update.rule;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.qc.ISupplierqualitystatusMaintain;
import nc.vo.am.common.util.StringUtils;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInHeadVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/**
 * 校验供应商和生产商之间状态
 * @author yunfeng.li
 *
 */
public class PurchInQualiStatuskRule implements IRule<PurchaseInVO> {
	public void process(PurchaseInVO[] vos) {

		try {

			for (PurchaseInVO purchaseInVO : vos) {
				// 维持状态 不允许采购
				ISupplierqualitystatusMaintain maitian = (ISupplierqualitystatusMaintain) NCLocator
						.getInstance().lookup(
								ISupplierqualitystatusMaintain.class);
				// 物料
				Map<String, String> pk_material_vs = new HashMap<>();
				PurchaseInHeadVO headvo = purchaseInVO.getHead();
				PurchaseInBodyVO[] items = purchaseInVO.getBodys();
				if (headvo == null || items == null || items.length == 0)
					continue;

				for (PurchaseInBodyVO vo : items) {
					String vid = vo.getCmaterialvid();

					if (headvo == null || items == null || items.length == 0)
						continue;
					if(VOStatus.DELETED == vo.getStatus())
						continue;
					if (!StringUtils.isEmpty(vid)) {
						pk_material_vs.put(vid, vo.getCproductorid());
					}
				}
				maitian.checkSupplierStatus(pk_material_vs,
						headvo.getPk_group(), headvo.getPk_org(),
						headvo.getCvendorid(), "入库", "维持");
				maitian.checkSupplierStatus(pk_material_vs,
						headvo.getPk_group(), headvo.getPk_org(),
						headvo.getCvendorid(), "入库", "待批准");
				maitian.checkSupplierStatus(pk_material_vs,
						headvo.getPk_group(), headvo.getPk_org(),
						headvo.getCvendorid(), "入库", "不批准");
			}
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrappException(e);
		}
	}

}
