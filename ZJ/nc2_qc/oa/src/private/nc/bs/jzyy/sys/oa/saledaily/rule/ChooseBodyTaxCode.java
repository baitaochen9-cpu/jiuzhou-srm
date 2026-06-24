package nc.bs.jzyy.sys.oa.saledaily.rule;

import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoByTaxcodeQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.pub.lang.UFDate;
import nc.vo.scmpub.res.billtype.CTBillType;

/**
 * 表体税码指定后处理税率等信息（不联动计算）
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:18:48   
 * @version NCC1909
 */
public class ChooseBodyTaxCode  extends CtFieldEvent  {

	private CtAbstractVO hvo;
	  
	private CtAbstractBVO[] bvos;
	
	private String billtype;
	
	public ChooseBodyTaxCode(CtAbstractVO hvo, CtAbstractBVO[] bvos, String billtype){
		this.hvo = hvo;
		this.bvos = bvos;
		this.billtype = billtype;
	}
	
	@Override
	public void process() {
		
		this.setDataByTaxCode();
		
	}

	private void setDataByTaxCode() {
		UFDate date = hvo.getSubscribedate();
		for(CtAbstractBVO bvo : bvos){
			String taxcode = bvo.getCtaxcodeid();
			if(taxcode == null){
				continue;
			}
			VATInfoByTaxcodeQueryVO query = new VATInfoByTaxcodeQueryVO(taxcode, date);
			VATInfoVO[] info = VATBDService.queryVATInfo(new VATInfoByTaxcodeQueryVO[] {
					query
			});
			bvo.setAttributeValue(CtAbstractBVO.FTAXTYPEFLAG, info[0].getFtaxtypeflag());
			bvo.setAttributeValue(CtAbstractBVO.NTAXRATE, info[0].getNtaxrate());
			if (CTBillType.PurDaily.getCode().equals(this.billtype)
					|| CTBillType.OtherPur.getCode().equals(this.billtype)) {
				bvo.setAttributeValue(CTVatNameConst.NNOSUBTAXRATE, info[0].getNnosubtaxrate());
			}
		}
	}
	

}
