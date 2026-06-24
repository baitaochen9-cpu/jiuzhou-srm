package nc.bs.jzyy.sys.oa.saledaily.rule;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.oa.saledaily.fill.DealBodyVat;
import nc.bs.jzyy.sys.oa.saledaily.fill.DealByBodyUnit;
import nc.cmp.utils.StringUtil;
import nc.pubitf.uapbd.ICustMaterialPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.uapbd.custmaterial.CustMaterialVO;

/**
 * 表体客户物料码
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:27:22   
 * @version NCC1909
 */
public class ChooseBodyCustMaterialEvent  extends CtFieldEvent {
	
	private AggCtSaleVO[] paramArrayOfE = null;
	
	public ChooseBodyCustMaterialEvent(AggCtSaleVO[] paramArrayOfE){
		this.paramArrayOfE = paramArrayOfE;
	}
	
	@Override
	public void process() {
		for(AggCtSaleVO aggvo : paramArrayOfE){
			// 通过物料客户码带关联值（现在只带生产厂商）
			this.setInfoByCustMar(aggvo.getCtSaleBVO());
			// 设置表体默认单位
			new DealByBodyUnit(aggvo.getParentVO(), aggvo.getCtSaleBVO(), CTBillType.SaleDaily.getCode())
			.setEditable();
			// vat
			new DealBodyVat().vat(aggvo.getParentVO(), aggvo.getCtSaleBVO());
		}
		
	}
	
	private void setInfoByCustMar(CtSaleBVO[] bvos) {
	    Set<String> custMarSet = new HashSet<>();
	    for (CtSaleBVO bvo : bvos) {
	    	if(StringUtil.isEmpty(bvo.getCcustmaterialid())){
	    		continue;
	    	}
	      custMarSet.add(bvo.getCcustmaterialid());
	    }
	    if (custMarSet.size() > 0) {
	      ICustMaterialPubService custmarsrv =
	          NCLocator.getInstance().lookup(ICustMaterialPubService.class);
	      try {
	        Map<String, CustMaterialVO> custMarMap =
	            custmarsrv.queryVOsByID(custMarSet.toArray(new String[custMarSet
	                .size()]));
	        for (CtSaleBVO bvo : bvos) {
	          String key = bvo.getCcustmaterialid();
	          CustMaterialVO custMarVO = custMarMap.get(key);
	          // 生产厂商
	          bvo.setAttributeValue(CtAbstractBVO.CPRODUCTORID, custMarVO.getVfree4());
	          // 自定义项1-10
	          bvo.setAttributeValue(CtAbstractBVO.VFREE1, custMarVO.getVfree6());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE2, custMarVO.getVfree7());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE3, custMarVO.getVfree8());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE4, custMarVO.getVfree9());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE5, custMarVO.getVfree10());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE6, custMarVO.getVfree11());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE7, custMarVO.getVfree12());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE8, custMarVO.getVfree13());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE9, custMarVO.getVfree14());
	          bvo.setAttributeValue(CtAbstractBVO.VFREE10, custMarVO.getVfree15());
	        }

	      }
	      catch (BusinessException e) {

	        ExceptionUtils.wrappException(e);

	      }
	    }

	  }

}
