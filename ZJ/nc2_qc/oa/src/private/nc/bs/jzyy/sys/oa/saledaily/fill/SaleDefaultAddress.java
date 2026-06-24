package nc.bs.jzyy.sys.oa.saledaily.fill;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.addrdoc.AddrdocPubService;
import nc.itf.scmpub.reference.uap.bd.customer.CustomerPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;

/**
 * 本类主要完成以下功能：销售合同默认收货地址
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-27 下午6:22:54   
 * @version NCC1909
 */
public class SaleDefaultAddress {

  /**
   * 方法功能描述：设置表体默认收货地址、收货地区、收货地点
   * <p>
   * <b>参数说明</b>
   * 
   * @param aggvo
   *          <p>
   * @since 6.3
   * @author mafeic
   * @time 2013-7-13 下午05:11:08
   */
  public void setBodyDefaultAddress(AggCtSaleVO aggvo) {
	  CtSaleVO parentVO = aggvo.getParentVO();
    String[] defaultadds = this.getDefaultAddress(parentVO);
    // 表头只有一个客户，所以只会返回一个默认地址
    if (null != defaultadds && defaultadds.length == 1) {
      // 取默认收货地区
      String[] defaultareapks =
          CustomerPubService.getAreaPksByConsignAddress(defaultadds);
      // 取默认收货地点
      Map<String, String> defaultaddoc =
          AddrdocPubService.getAddressDocPksByConsignAddress(defaultadds);
      for(CtSaleBVO bvo : aggvo.getCtSaleBVO()){
    	  // 设置默认收货地址
    	  bvo.setAttributeValue(CtAbstractBVO.PK_RECEIVEADDRESS, defaultadds[0]);
    	  // 设置默认收货地区
    	  if (null != defaultareapks && defaultareapks.length == 1) {
    		  bvo.setAttributeValue(CtAbstractBVO.CDEVAREAID, defaultareapks[0]);
    	  }
    	  // 设置默认收货地点
    	  if (null != defaultaddoc && !defaultaddoc.isEmpty()
    			  && defaultaddoc.size() == 1) {
    		  bvo.setAttributeValue(CtAbstractBVO.CDEVADDRID, defaultaddoc.get(defaultadds[0]));
    	  }
      }

    }
  }


  /**
   * 方法功能描述：根据客户取默认收货地址
   * <p>
   * <b>参数说明</b>
   * 
   * @param edit
   * @return <p>
   * @since 6.3
   * @author mafeic
   * @time 2013-7-13 下午03:35:28
   */
  private String[] getDefaultAddress(CtSaleVO parentVO) {
    // 客户pk
    String[] cvendorids = new String[] {
    		parentVO.getPk_customer()
    };
    // 主组织pk
    String pk_org = parentVO.getPk_org();

    // 取客户默认收货地址
    String[] defaultadds =
        CustomerPubService.getDefaultAddresses(cvendorids, pk_org);

    return defaultadds;
  }
}
