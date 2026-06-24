package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 合同条款
 * @author wangshrc
 * @date 2019年2月15日 上午11:29:44
 * @version ncc1.0
 */
public class SaleDailyTermCodeAfterRule implements
IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		//多选处理
		int[] rows = new HandleSaleDailySelected().handleSelected(
				billvo, event, userobject);
		//默认值
		for(int i = 0;i<rows.length;i++) {
			CtSaleVO hvo= billvo.getParentVO();
			// 组织
			String pk_org = hvo.getPk_org();
			String pk_org_v = hvo.getPk_org_v();
			// 集团
			String pk_group = hvo.getPk_group();

			if (!ValueUtil.isEmpty(pk_org)) {
				billvo.getCtSaleTermVO()[i].setPk_org(pk_org);
			}
			if (!ValueUtil.isEmpty(pk_org_v)) {
				billvo.getCtSaleTermVO()[i].setPk_org_v(pk_org_v);
			}
			if (!ValueUtil.isEmpty(pk_group)) {
				billvo.getCtSaleTermVO()[i].setPk_group(pk_group);
			}
		}
		
		return billvo;
	}

	

}
