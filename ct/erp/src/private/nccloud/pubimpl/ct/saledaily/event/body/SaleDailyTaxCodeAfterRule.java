package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoByTaxcodeQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.rule.SaleRelationCalculate;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 税码编辑后
 * @author wangshrc
 * @date 2019年2月15日 上午9:39:35
 * @version ncc1.0
 */
public class SaleDailyTaxCodeAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		String taxcode = (String) event.getChangrows()[0].getNewvalue();
		if (StringUtil.isEmptyTrimSpace(taxcode)) {
			return billvo;
		}
		CtSaleVO hvo = billvo.getParentVO();
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		UFDate date = hvo.getSubscribedate();
		VATInfoByTaxcodeQueryVO query = new VATInfoByTaxcodeQueryVO(taxcode,
				date);
		VATInfoVO[] info = VATBDService
				.queryVATInfo(new VATInfoByTaxcodeQueryVO[] { query });
		int lineNum = event.getRow();
		if (null != info && info.length > 0 && null != info[0]) {
			bvos[lineNum].setFtaxtypeflag(info[0].getFtaxtypeflag());
			bvos[lineNum].setNtaxrate(info[0].getNtaxrate());
			new SaleRelationCalculate().calculate(billvo,
					CtAbstractBVO.NTAXRATE);
		} else {
			bvos[lineNum].setCtaxcodeid(null);
			bvos[lineNum].setFtaxtypeflag(null);
			bvos[lineNum].setNtaxrate(null);
		}
		return billvo;
	}

}
