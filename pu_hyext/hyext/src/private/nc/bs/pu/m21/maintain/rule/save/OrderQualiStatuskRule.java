package nc.bs.pu.m21.maintain.rule.save;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.qc.ISupplierqualitystatusMaintain;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.vo.am.common.util.StringUtils;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
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
public class OrderQualiStatuskRule implements IRule<OrderVO> {
	public void process(OrderVO[] vos) {

		try {

			for (OrderVO ordervo : vos) {
				// 维持状态 不允许采购
				ISupplierqualitystatusMaintain maitian = (ISupplierqualitystatusMaintain) NCLocator
						.getInstance().lookup(
								ISupplierqualitystatusMaintain.class);
				// 物料
				OrderHeaderVO headvo = ordervo.getHVO();
				OrderItemVO[] items = ordervo.getBVO();
				if (headvo == null || items == null || items.length == 0)
					continue;
				Map<String, String> pk_material_vs = new HashMap<>();
				for (OrderItemVO vo : items) {
					String vid = vo.getPk_material();
					if (headvo == null || items == null || items.length == 0)
						continue;
					if(VOStatus.DELETED == vo.getStatus())
						continue;
					if (!StringUtils.isEmpty(vid)) {
						pk_material_vs.put(vid, vo.getCproductorid());
					}
				}
				String controlModel = getParam(items[0].getPk_reqstoorg(),"YF621-EX01");
					
				if("表头采购组织".equals(controlModel)){
					maitian.checkSupplierStatus(pk_material_vs,
							headvo.getPk_group(), headvo.getPk_org(),
							headvo.getPk_supplier(), "采购", "维持");
				}else if("表体需求库存组织".equals(controlModel)){
					maitian.checkSupplierStatus(pk_material_vs,
							headvo.getPk_group(), items[0].getPk_reqstoorg(),
							headvo.getPk_supplier(), "采购", "维持");
				}else{
					throw new BusinessException("YF621-EX01参数值不存在，请联系管理员");
				}
//				maitian.checkSupplierStatus(pk_material_vs,
//						headvo.getPk_group(), headvo.getPk_org(),
//						headvo.getPk_supplier(), "采购", "不批准");
			}
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrappException(e);
		}
	}
	
	private String getParam(String pk_org, String paramCode) {
		String param = "";
		if (!StringUtil.isEmpty(paramCode)) {
			try {
				param = SysinitAccessor.getInstance()
						.getParaString(pk_org, paramCode).toString();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return param;
	}

}
