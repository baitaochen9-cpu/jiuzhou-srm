package nccloud.pubimpl.ic.pub.event.listener;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.ic.general.businessevent.ICGeneralCommonEvent;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillHeadVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.ICBillType;

public class MedICPubBeforeCheckBatchandLotnoEvent
  implements IBusinessListener
{
  public void doAction(IBusinessEvent event)
    throws BusinessException
  {
    ICGeneralCommonEvent.ICGeneralCommonUserObj obj = (ICGeneralCommonEvent.ICGeneralCommonUserObj)event.getUserObject();
    ICBillVO[] newBillVOs = (ICBillVO[])obj.getNewObjects();
    ICBillVO[] oldBillVOs = (ICBillVO[])obj.getOldObjects();

    if ("1001".equals(event.getEventType())) {
      if ((oldBillVOs == null) || (oldBillVOs.length == 0)) {
        return;
      }
      checkBatchandLotno(oldBillVOs);
    }

    if ("1003".equals(event.getEventType())) {
      if ((newBillVOs == null) || (newBillVOs.length == 0)) {
        return;
      }
      checkBatchandLotno(newBillVOs);
    }
  }

  public void checkBatchandLotno(ICBillVO[] aggvos) {
    ValidationException exp = new ValidationException();
    for (ICBillVO aggvo : aggvos) {
      String billTypeCode = aggvo.getHead().getBillType().getCode();
      
      if(!isCheckEnable( aggvo.getHead())){
    	  continue;
      }
      ICBillBodyVO[] items = aggvo.getBodys();
      for (int i = 0; i < items.length; i++) {
        ICBillBodyVO item = items[i];

        if ((ICBillType.DiscardIn.getCode().equals(billTypeCode)) && ("55A4".equals(item.getCsourcetype()))) {
          continue;
        }
        String rowNo = item.getCrowno();
        String batchcode = item.getVbatchcode();
        String cmaterialvid = item.getCmaterialvid();
        for (int j = i; j < items.length; j++) {
          ICBillBodyVO item2 = items[j];
          if ((batchcode == null) || 
            (rowNo.equals(item2.getCrowno())) || 
            (!batchcode.equals(item2.getVbatchcode())) || 
            (!cmaterialvid.equals(item2.getCmaterialvid()))) continue;
          exp.addValidationFailure(
            new ValidationFailure("第" + 
            rowNo + "行和第" + item2.getCrowno() + 
            "行【批次】不能重复！\n"));
        }
      }
    }

    if (exp.getFailureMessage().toArray().length > 0)
      ExceptionUtils.wrappBusinessException(exp.getFailureMessage()
        .toString());
  }
  
  protected boolean isCheckEnable(ICBillHeadVO selectedData) {
		boolean yf635 = false;

		if (selectedData != null) {
			try {
				yf635 = SysinitAccessor
						.getInstance()
						.getParaBoolean( selectedData.getPk_org(),
								"YF639").booleanValue();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return yf635;
	}
}