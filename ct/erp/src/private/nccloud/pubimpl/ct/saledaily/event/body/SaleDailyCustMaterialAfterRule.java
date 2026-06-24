package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.uapbd.ICustMaterialPubService;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.uapbd.custmaterial.CustMaterialVO;
import nccloud.dto.ct.saledaily.utils.UnitEditUtils;
import nccloud.dto.ct.saledaily.utils.VatEditUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 销售合同客户物料编辑后
 * @author wangshrc
 * @date 2019年2月15日 上午10:00:35
 * @version ncc1.0
 */
public class SaleDailyCustMaterialAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		int[] rows = new int[] { event.getRow() };
		// TODO 客户物料多选参照
		// RefMoreSelectedUtils utils =
		// new RefMoreSelectedUtils(event.getBillCardPanel());
		// int[] rows =
		// utils.refMoreSelected(event.getRow(), CtSaleBVO.CCUSTMATERIALID,
		// true);
		// 通过物料客户码带关联值（现在只带生产厂商，其他的通过模板元数据关联项带）
		this.setInfoByCustMar(billvo, rows);
		// 设置表体默认单位
		new UnitEditUtils(billvo, CTBillType.SaleDaily.getCode())
				.setEditable(rows);
		// vat
		new VatEditUtil().vat(billvo, rows);
		return billvo;
	}

	private void setInfoByCustMar(AggCtSaleVO billvo, int[] rows) {
		Set<String> custMarSet = new HashSet<String>();
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		for (int i : rows) {
			custMarSet.add((String) bvos[i].getCcustmaterialid());
		}
		if (custMarSet.size() > 0) {
			ICustMaterialPubService custmarsrv = NCLocator.getInstance()
					.lookup(ICustMaterialPubService.class);
			try {
				Map<String, CustMaterialVO> custMarMap = custmarsrv
						.queryVOsByID(custMarSet.toArray(new String[custMarSet
								.size()]));
				for (int i : rows) {
					String key = (String) bvos[i].getCcustmaterialid();
					CustMaterialVO custMarVO = custMarMap.get(key);
					// 生产厂商
					bvos[i].setCproductorid(custMarVO.getVfree4());
					// 自定义项1-10
					bvos[i].setVfree1(custMarVO.getVfree6());
					bvos[i].setVfree2(custMarVO.getVfree7());
					bvos[i].setVfree3(custMarVO.getVfree8());
					bvos[i].setVfree4(custMarVO.getVfree9());
					bvos[i].setVfree5(custMarVO.getVfree10());
					bvos[i].setVfree6(custMarVO.getVfree11());
					bvos[i].setVfree7(custMarVO.getVfree12());
					bvos[i].setVfree8(custMarVO.getVfree13());
					bvos[i].setVfree9(custMarVO.getVfree14());
					bvos[i].setVfree10(custMarVO.getVfree15());
				}

			} catch (BusinessException e) {

				ExceptionUtils.wrappException(e);

			}
		}
	}
}
