package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.scmf.pub.keyvalue.IKeyValue;
import nc.vo.scmf.pub.keyvalue.VOKeyValue;
import nccloud.dto.ct.saledaily.utils.CustMaterialInfoUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 客户物料码
 * @author wangshrc
 * @date 2019年2月15日 上午10:13:16
 * @version ncc1.0
 */
public class SaleDailyCustMarAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		IKeyValue keyValue = new VOKeyValue<AggCtSaleVO>(billvo);
		new CustMaterialInfoUtil().setCustMaterialInfo(keyValue,
				new int[] { event.getRow() });
		return billvo;
	}

}
