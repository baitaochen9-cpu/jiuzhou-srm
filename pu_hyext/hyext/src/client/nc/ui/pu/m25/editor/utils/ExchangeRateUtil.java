/**
 * $文件说明$
 * 
 * @author tianft
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-3-23 下午03:34:09
 */
package nc.ui.pu.m25.editor.utils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.itf.scmpub.reference.uap.bd.currency.CurrencyRate;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.ui.pu.pub.editor.CardEditorHelper;
import nc.ui.pub.bill.BillCardPanel;
import nc.vo.bc.pub.util.SysParaInitQuery;
import nc.vo.pu.m25.entity.InvoiceHeaderVO;
import nc.vo.pu.pub.util.PubSysParamUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmpub.res.para.NCPara;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>汇率的工具类,适用于发票
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author tianft
 * @time 2010-3-23 下午03:34:09
 */
public class ExchangeRateUtil {

  public static void calculateBodyRows(CardEditorHelper util) {
    int[] rows = util.getRows(util.getItemCount());
    ExchangeRateUtil.calculateBodyRows(rows, util.getEditor());
  }

  public static void calculateBodyRows(CardEditorHelper util, String oldRate) {
    String newRate = util.getHeadStringValue(InvoiceHeaderVO.NEXCHANGERATE);
    if (newRate != null && !newRate.equals(oldRate)) {
      int[] rows = util.getRows(util.getItemCount());
      ExchangeRateUtil.calculateBodyRows(rows, util.getEditor());
    }
  }

  public static void calculateBodyRows(int[] rows, BillCardPanel billCardPanel) {
    if (rows == null) {
      return;
    }
    // 汇率变化后重新计算行
    new RelationCalculate().calculate(billCardPanel, rows,
        InvoiceHeaderVO.NEXCHANGERATE);
  }

  /**
   * 方法功能描述：改变汇率及改变汇率后的处理
   * <p>
   * <b>参数说明</b>
   * 
   * @param util <p>
   * @since 6.0
   * @author tianft
   * @time 2010-4-5 下午01:31:38
   */
  public static void changeExchangeRate(CardEditorHelper util) {
    String oldExchangeRate =
        util.getHeadStringValue(InvoiceHeaderVO.NEXCHANGERATE);
    // 1.设置表头汇率
    ExchangeRateUtil.setExchangerate(util);
    // 2.汇率变化后处理
    ExchangeRateUtil.calculateBodyRows(util, oldExchangeRate);
  }
  

  /**
   * 方法功能描述：设置表头汇率
   * <p>
   * <b>参数说明</b>
   * 
   * @param util <p>
   * @since 6.0
   * @author tianft
   * @time 2010-3-31 下午04:44:24
   */
  public static void setExchangerate(CardEditorHelper util) {
    ExchangeRateUtil.setExchangerate(util, null);
  }

  /**
   * 方法功能描述：根据传进来的日期设置表头汇率
   * <p>
   * <b>参数说明</b>
   * 
   * @param util <p>
   * @since 6.0
   * @author tianft
   * @time 2010-3-31 下午04:44:24
   */
  public static void setExchangerate(CardEditorHelper util, UFDate billdate) {
    String origcurrency =
        util.getHeadStringValue(InvoiceHeaderVO.CORIGCURRENCYID);
    String currency = util.getHeadStringValue(InvoiceHeaderVO.CCURRENCYID);
    UFDate billDate = billdate;
    
    
    /*****20210809 zhian.ye JZIT 调整以到票日期进行汇率计算
     * 增加参数进行处理，参数关闭时执行系统 原逻辑。
     * *************************************/
    if(billDate == null){
      billDate = util.getHeadUFDateValue(InvoiceHeaderVO.DBILLDATE);//darrivedate 
      //***************add_begin
      UFBoolean paraBoolean = SysParaInitQuery.getParaBoolean(util.getHeadStringValue(InvoiceHeaderVO.PK_ORG), "PUYE001");
      if(paraBoolean == UFBoolean.TRUE){
    	  billDate = util.getHeadUFDateValue(InvoiceHeaderVO.DARRIVEDATE);
      }
      //**************end
    }
    /****************************************************************/
    
    
    
    // 组织本币
    String pk_org = util.getHeadStringValue(InvoiceHeaderVO.PK_ORG);
    String orgCurrency =
        CurrencyRateUtilHelper.getInstance().getLocalCurrtypeByOrgID(pk_org);

    // 没有币种或者日期不再后续处理
    if (StringUtils.isEmpty(origcurrency) || StringUtils.isEmpty(currency)
        || billDate == null) {
      return;
    }

    // 集团本位币汇率,根据NC001参数获取汇率 modify by wangljc at 2011-6-20 18:06:27
    UFDouble ngroupexchgrate = UFDouble.ZERO_DBL;
    String nc001 =
        PubSysParamUtil
            .getNC001(InvocationInfoProxy.getInstance().getGroupId());
    if (NCPara.NC001_CALCULATEBYORIGCURRTYPE.getName().equals(nc001)) {
      ngroupexchgrate =
          CurrencyRate.getGroupLocalCurrencySellRate(origcurrency, billDate);
    }
    else if (NCPara.NC001_CALCULATEBYCURRTYPE.getName().equals(nc001)) {
      ngroupexchgrate =
          CurrencyRate.getGroupLocalCurrencySellRate(orgCurrency, billDate);
    }

    // 全局本位币汇率,根据NC002参数获取汇率 modify by wangljc at 2011-6-20 18:06:27
    UFDouble nglobalexchgrate = UFDouble.ZERO_DBL;
    String nc002 = PubSysParamUtil.getNC002();
    if (NCPara.NC001_CALCULATEBYORIGCURRTYPE.getName().equals(nc002)) {
      nglobalexchgrate =
          CurrencyRate.getGlobalLocalCurrencySellRate(origcurrency, billDate);
    }
    else if (NCPara.NC001_CALCULATEBYCURRTYPE.getName().equals(nc002)) {
      nglobalexchgrate =
          CurrencyRate.getGlobalLocalCurrencySellRate(orgCurrency, billDate);
    }

    if (ngroupexchgrate != null) {
      util.getEditor().getHeadItem(InvoiceHeaderVO.NGROUPEXCHGRATE)
          .setValue(ngroupexchgrate);
    }
    if (nglobalexchgrate != null) {
      util.getEditor().getHeadItem(InvoiceHeaderVO.NGLOBALEXCHGRATE)
          .setValue(nglobalexchgrate);
    }

    UFDouble exchangeRate = null;

    exchangeRate =
        CurrencyRate.getCurrencySellRateByOrg(pk_org, origcurrency, currency,
            billDate);
    if (exchangeRate == null) {
      return;
    }
    util.getEditor().getHeadItem(InvoiceHeaderVO.NEXCHANGERATE)
        .setValue(exchangeRate);

  }

}
