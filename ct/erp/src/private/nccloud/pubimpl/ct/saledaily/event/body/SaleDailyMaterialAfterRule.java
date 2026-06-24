package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.pub.keyvalue.IKeyValue;
import nc.vo.scmf.pub.keyvalue.VOKeyValue;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.saledaily.utils.CtSaleDefaultAddressUtil;
import nccloud.dto.ct.saledaily.utils.CustMaterialInfoUtil;
import nccloud.dto.ct.saledaily.utils.UnitEditUtils;
import nccloud.dto.ct.saledaily.utils.VatEditUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;
import nccloud.dto.scmpub.pub.utils.SCMMultSelectedUtil;
import nccloud.pubitf.ct.saledaily.service.ISaleDailyQueryForNCCloudService;

/**
 * @description 销售合同物料编辑后
 * @author wangshrc
 * @date 2019年2月14日 下午1:59:39
 * @version ncc1.0
 */
public class SaleDailyMaterialAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo, BillCardBodyEditEvent event, Map userobject) {
		IKeyValue keyValue = new VOKeyValue<AggCtSaleVO>(billvo);
		// 处理物料多选
		int[] rows = new SCMMultSelectedUtil().handleMultSelected(billvo, event, userobject);
		// 1. 带出物料相关信息
		MaterialRelationValueRule marRela = new MaterialRelationValueRule(
				keyValue);
		marRela.setBodyDefValue(rows);
		ISaleDailyQueryForNCCloudService service = NCLocator.getInstance()
				.lookup(ISaleDailyQueryForNCCloudService.class);
		try {
			service.setAddLineInfo(billvo);
			new UnitEditUtils(billvo, CTBillType.SaleDaily.getCode()).setEditable(rows);
			
			CtSaleDefaultAddressUtil.setBodyDefaultAddress(keyValue);
			new VatEditUtil().vat(billvo, rows);
			new CustMaterialInfoUtil().setCustMaterialInfo(keyValue, rows);
			return billvo;
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}

		return null;
	}

}
