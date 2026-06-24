package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;
import nccloud.dto.scmpub.pub.utils.SCMMultSelectedUtil;

/**
 * @description 合同条款编辑后
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:39:56
 * @version ncc1.0
 **/
public class TermAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {

		new SCMMultSelectedUtil().handleMultSelected(billvo, event, userobject);

		return billvo;
	}

}
