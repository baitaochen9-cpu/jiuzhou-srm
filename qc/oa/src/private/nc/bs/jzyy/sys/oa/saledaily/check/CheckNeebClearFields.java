package nc.bs.jzyy.sys.oa.saledaily.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.pub.CTVatNameConst;

/**
 * 变更、修改时是否要清空单价金额信息重算
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-27 下午6:24:19   
 * @version NCC1909
 */
public class CheckNeebClearFields {

	  // 清空单价、金额字段
	  private static String[] bNumfields = {
	    CtAbstractBVO.NASTNUM, CtAbstractBVO.NNUM, CtAbstractBVO.NQTUNITNUM, 
	  };
	  
	  private static String[] bPricefields = {
	    CtAbstractBVO.NGPRICE, CtAbstractBVO.NGTAXPRICE, 
	    CtAbstractBVO.NORIGPRICE, CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NGPRICE,
	    CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NORIGPRICE,
	    CtAbstractBVO.NORIGTAXPRICE, CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE,
	    CtAbstractBVO.NQTPRICE, CtAbstractBVO.NQTTAXPRICE, 
	  };
	  
	  private static String[] bMnyfields = {
	    CtAbstractBVO.NGLOBALMNY, CtAbstractBVO.NGLOBALTAXMNY,
	    CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY,
	    CtAbstractBVO.NMNY, CtAbstractBVO.NORIGMNY, CtAbstractBVO.NMNY,
	    CtAbstractBVO.NORIGMNY, CtAbstractBVO.NGROUPMNY,
	    CtAbstractBVO.NGROUPTAXMNY, CtAbstractBVO.NMNY, CtAbstractBVO.NORIGMNY, 
	    CtAbstractBVO.NORIGTAXMNY, CtAbstractBVO.NTAXMNY, CTVatNameConst.NCALTAXMNY,
	    CTVatNameConst.NCALCOSTMNY,CtAbstractBVO.NTAX,
	  };
	  
	  
	public static void checkFields(CtAbstractBVO bvo, Set<String> bodyKeySet){
		List<String> nums = new ArrayList<>(Arrays.asList(CtAbstractBVO.NASTNUM, CtAbstractBVO.NNUM));
		List<String> prices = new ArrayList<>(Arrays.asList(CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE));
		List<String> mnys = new ArrayList<>(Arrays.asList(CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGTAXMNY));
		int numIndex = 0,priceIndex = 0,mnyIndex = 0;
		for(String key : bodyKeySet){ 
			if(nums.contains(key) && numIndex == 0){
				clear(bvo, bNumfields);
				numIndex++;
			}else if(prices.contains(key) && priceIndex == 0){
				clear(bvo, bPricefields);
				priceIndex++;
			}else if(mnys.contains(key) && mnyIndex == 0){
				clear(bvo, bMnyfields);
				mnyIndex++;
			}
		}
		if((numIndex + priceIndex + mnyIndex) > 1){
			clear(bvo, bNumfields);
			clear(bvo, bPricefields);
			clear(bvo, bMnyfields);
		}
		
	}


	private static void clear(CtAbstractBVO bvo, String[] bPricefields) {
		for(String field : bPricefields){
			bvo.setAttributeValue(field, null);
		}
		
	}
}
