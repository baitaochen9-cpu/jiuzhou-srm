package nc.bs.jzyy.sys.oa.saledaily.check;

import java.util.List;

import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.Calendars;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * 销售合同组装数据后的额一般逻辑校验
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:25:05   
 * @version NCC1909
 */
public class CheckCtSaleValidatorAfter extends CheckCtSaleValidator {

	@Override
	public String [] getBodyNotNullFields() {
	    String[] bodynames = {CtSaleBVO.FTAXTYPEFLAG, CTVatNameConst.CRECECOUNTRYID, 
	    		CTVatNameConst.FBUYSELLFLAG, CTVatNameConst.CTAXCODEID};
	    return bodynames;
	}
	
	@Override
	public void otherCheck(AbstractBill billVO, List<ValidationException> exceptions) {
		super.otherCheck(billVO, exceptions);
		if(billVO instanceof AggCtSaleVO){
			AggCtSaleVO ctPuVO = (AggCtSaleVO) billVO;
			CtSaleVO parentVO = ctPuVO.getParentVO();
			this.checkDate(parentVO);
		}
	}
	
	private void checkDate(CtSaleVO parentVO) {
		//计划生效日期
		UFDate valDate = parentVO.getValdate();
	    if (!ValueUtil.isEmpty(valDate)) {
	      // 签订日期
	      UFDate subDate = parentVO.getSubscribedate();
	      // 终止日期
	      UFDate invalDate = parentVO.getInvallidate();
	      if (!ValueUtil.isEmpty(subDate)) {
	        if (valDate.before(subDate) && !valDate.isSameDate(subDate,Calendars
					.getGMTDefault())) {
	          ExceptionUtils.wrappBusinessException(
	              nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0","04020003-0050")/*@res "计划生效日期不能在合同签订日期之前！"*/);
	        }
	      }
	      if (!ValueUtil.isEmpty(invalDate)) {
	        if (valDate.after(invalDate) || ValueUtil.equals(valDate, invalDate)) {
	          ExceptionUtils.wrappBusinessException(
	              nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0","04020003-0051")/*@res "计划生效日期应小于计划终止日期！"*/);
	        }
	      }
	    }
	    //计划终止日期
		UFDate invallidate = parentVO.getInvallidate();
		if (ValueUtil.isEmpty(valDate) || ValueUtil.isEmpty(invallidate)) {
	      return;
	    }
	    else if (invallidate.before(valDate) || ValueUtil.equals(invallidate, valDate)) {
	      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0","04020003-0048")/*@res "计划终止日期应大于计划生效日期！"*/);
	    }
	}
}
