package nccloud.web.ct.purdaily.action;

import java.util.Map;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.pubitf.ct.purdaily.service.IPurdailyService;
import nccloud.web.ct.pub.action.ExtBaseQueryCardAction;
import nccloud.web.ct.purdaily.utils.PrecisionUtil;

/**
 * @description 采购合同维护卡片查询
 * @author xiahui
 * @date 创建时间：2019-1-15 上午9:54:44
 * @version ncc1.0
 **/
public class QueryCardAction extends ExtBaseQueryCardAction {

	private static final String SCENE_BZ = "bz"; // 报账平台

	@Override
	public IBill[] queryBill(String[] ids, Map<String, Object> userObj) throws BusinessException {
		String id = this.getProcessedIds(ids[0], userObj);
		IPurdailyMaintain service = ServiceLocator.find(IPurdailyMaintain.class);
		AggCtPuVO[] retVo = service.queryCtPuVoByIds(new String[] { id });
		return retVo == null ? null : retVo;
	}

	@Override
	public void processPrecision(ExtBillCard retCard) {
		// 精度处理
		PrecisionUtil.setExtCardPrecision(retCard);
	}

	/**
	 * 处理前端Ids,报账平台的历史版本查询，返回结果是最新的版本结果
	 * 
	 * @param id
	 * @param userObj
	 * @return
	 * @throws BusinessException
	 */
	private String getProcessedIds(String id, Map<String, Object> userObj) throws BusinessException {
		if (userObj != null && QueryCardAction.SCENE_BZ.equals((String) userObj.get("scene"))) {
			// 报账平台查询最新版本ids
			IPurdailyService service = ServiceLocator.find(IPurdailyService.class);
			return service.queryLatestId(id);
		}

		return id;
	}

}
