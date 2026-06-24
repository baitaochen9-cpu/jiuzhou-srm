package nccloud.web.ct.saledaily.utils;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.scmpub.util.CollectionUtils;
import nc.vo.scmpub.util.VOEntityUtil;
import nc.vo.scmpub.util.ValueCheckUtil;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.pubitf.scmpub.pub.batchopr.dto.SCMBatchResultDTO;
import nccloud.web.scmpub.pub.action.TempletQueryAction;
import nccloud.web.scmpub.pub.utils.SCMBatchOperatorResult;

/**
 * @description 转换成前台需要的数据结构 默认处理接口的返回值类型为AGGVO数组的场景，其他需要重载convertRetVO，自己处理
 * @author wangceb
 * @date 2018年8月20日 下午2:23:21
 * @version ncc1.0
 */
public class BatchOprResultConvertor {

	/**
	 * 
	 * 批量处理 转换成前台需要的数据结构
	 * 
	 * @param dto
	 * @return
	 *
	 */
	public SCMBatchOperatorResult convert(SCMBatchResultDTO dto, String pageCode) {
		SCMBatchOperatorResult ret = new SCMBatchOperatorResult();
		ret.setSucessNum(dto.getSucessNum());
		ret.setFailedNum(ValueCheckUtil.isNullORZeroLength(dto
				.getErrorMessages()) ? 0 : dto.getErrorMessages().length);
		Object[] sucessRetObj = dto.getSucessRetObj();
		ret.setSucessVOs(this.convertRetVO(sucessRetObj,pageCode));
		ret.setErrorMessages(dto.getErrorMessages());
		return ret;
	}

	protected Grid convertRetVO(Object[] sucessRetObj, String pageCode) {
		if (ValueCheckUtil.isNullORZeroLength(sucessRetObj)) {
			return null;
		}
		List<AggregatedValueObject> sucessVOs = new ArrayList<AggregatedValueObject>();

		for (Object retObj : sucessRetObj) {
			if (retObj == null) {
				continue;
			}
			sucessVOs.add(((AggregatedValueObject[]) retObj)[0]);
		}
		if (ValueCheckUtil.isNullORZeroLength(sucessVOs)) {
			return null;
		}
		String templetid = TempletQueryAction.getTempletIdByPageCode(pageCode);
		Grid grid = new GridOperator(templetid, pageCode).toGrid(VOEntityUtil
				.getHeadVOs(CollectionUtils.listToArray(sucessVOs)));
		return grid;
	}
}
