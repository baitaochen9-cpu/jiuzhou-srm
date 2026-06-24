package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoByTaxcodeQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 税码编辑后处理
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:26:18
 * @version ncc1.0
 **/
public class CtTaxcodeAfterRule implements IBodyAfterRule<AggCtPuVO> {

	private String cttype;

	public CtTaxcodeAfterRule(String cttype) {
		this.cttype = cttype;
	}

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();
		String taxcode = (String) event.getChangrows()[0].getNewvalue();
		if (StringUtil.isEmptyTrimSpace(taxcode)) {
			return billvo;
		}

		UFDate date = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);

		VATInfoByTaxcodeQueryVO query = new VATInfoByTaxcodeQueryVO(taxcode, date);
		VATInfoVO[] info = VATBDService.queryVATInfo(new VATInfoByTaxcodeQueryVO[] { query });
		if (null != info && info.length > 0 && null != info[0]) {
			util.setBodyValue(row, CtAbstractBVO.FTAXTYPEFLAG, info[0].getFtaxtypeflag());
			util.setBodyValue(row, CtAbstractBVO.NTAXRATE, info[0].getNtaxrate());
			RelationCalculate calcul = new RelationCalculate();
			calcul.calculate(util, new int[] { row }, CtAbstractBVO.NTAXRATE);
			if (CTBillType.PurDaily.getCode().equals(this.cttype) || CTBillType.OtherPur.getCode().equals(this.cttype)) {
				util.setBodyValue(row, CTVatNameConst.NNOSUBTAXRATE, info[0].getNnosubtaxrate());
				calcul.calculate(util, new int[] { row }, CTVatNameConst.NNOSUBTAXRATE);
			}

		} else {
			util.setBodyValue(row, CTVatNameConst.CTAXCODEID, null);
			util.setBodyValue(row, CtAbstractBVO.FTAXTYPEFLAG, null);
			util.setBodyValue(row, CtAbstractBVO.NTAXRATE, null);
		}

		return billvo;
	}

}
