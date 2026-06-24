package nc.bs.ic.onhand.alert;

import nc.bs.ic.pub.alert.AlertConst;
import nc.bs.ic.pub.alert.AlertConst.QualityAlertType;
import nc.bs.pub.pa.IPreAlertPlugin;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


/**
 * 保质期临近预警
 * @since 6.0
 * @version 2011-2-21 下午04:03:36
 * @author chennn
 */

public class QualityDayNearAlert extends QualityDayAlert implements IPreAlertPlugin{

  @Override
  public PreAlertObject executeTask(PreAlertContext context)
  throws BusinessException {
    return this.alert(context);
  }

  @Override
  protected QualityDayView getQualityView(PreAlertContext context) {
	  /*
	   * 20230216  zhian.ye 增加阀值参数，
	   */

	  Integer alertType = new Integer( (String)context.getKeyMap().get("alertType"));
	  
    Object ratioObj = context.getKeyMap().get(AlertConst.RATIO);
    UFDouble ratio = new UFDouble();
    if (ratioObj != null) {
      String ratioStr = ratioObj.toString();
      ratio = new UFDouble(ratioStr);
    }
    UFDate date = context.getLoginDate();
    QualityDayView view = null  ;
    
    // 20230216 zhian.ye 判断阀值分别执行不同构造
    if(alertType == 1){
    	return new QualityDayView(QualityAlertType.Near, date, ratio);
    	
    }else if(alertType == 2) {
    Integer days = new Integer(ratioObj.toString());
    	return new QualityDayView(QualityAlertType.Near, date, days);
    }
    return view;
  }



}
