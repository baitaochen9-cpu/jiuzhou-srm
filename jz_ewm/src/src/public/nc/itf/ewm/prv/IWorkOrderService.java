package nc.itf.ewm.prv;

import java.util.List;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WOHisVO;
import nc.vo.ewm.workorder.WOPlanInVVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.uif2.LoginContext;

public abstract interface IWorkOrderService
{
  public abstract void deleteWorkOrders(AggWorkOrderVO[] paramArrayOfAggWorkOrderVO)
    throws BusinessException;
  
  public abstract Object insertWorkOrder(AggregatedValueObject paramAggregatedValueObject)
    throws BusinessException;
  
  public abstract Object updateWorkOrder(AggWorkOrderVO paramAggWorkOrderVO)
    throws BusinessException;
  
  public abstract Object updateWorkOrderStatus(AggWorkOrderVO paramAggWorkOrderVO, WOHisVO paramWOHisVO, boolean paramBoolean)
    throws BusinessException;
  
  public abstract void checkBodyVOExisted(AggWorkOrderVO[] paramArrayOfAggWorkOrderVO)
    throws BusinessException;
  
  public abstract List<String> queryTransType(String paramString);
  
  public abstract Object woCapital(AggWorkOrderVO paramAggWorkOrderVO, Object paramObject)
    throws BusinessException;
  
  public abstract Object woCapitalCancel(AggWorkOrderVO paramAggWorkOrderVO)
    throws BusinessException;
  
  public abstract WOPlanInVVO[] filterRemainable(WOPlanInVVO[] paramArrayOfWOPlanInVVO)
    throws BusinessException;
  
  public abstract Object[] getDefaultWoStatus(String paramString, LoginContext paramLoginContext)
    throws BusinessException;
  
  public abstract void WOLockValidate(AggWorkOrderVO paramAggWorkOrderVO)
    throws BusinessException;
  
  public abstract Object[] queryObjectByPks(String[] paramArrayOfString)
    throws BusinessException;
  
  public abstract AggWorkOrderVO makeUpCopyVOInfo(AggWorkOrderVO paramAggWorkOrderVO)
    throws BusinessException;
  
   AggWorkOrderVO updateWorkOrderHeadVO(AggWorkOrderVO[] paramAggWorkOrderVOs)
		    throws BusinessException;
}

