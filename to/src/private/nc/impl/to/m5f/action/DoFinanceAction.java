package nc.impl.to.m5f.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.bill.SplitBill;
import nc.vo.pubapp.calculator.CalculatorUtil;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pattern.model.tool.BillIndex;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.to.m5f.entity.STranFinInAggVO;
import nc.vo.to.m5f.entity.STranFinInHeadVO;
import nc.vo.to.m5f.entity.STranFinInItemVO;
import nc.vo.to.m5f.entity.STranFinOutAggVO;
import nc.vo.to.m5f.entity.STranFinOutHeadVO;
import nc.vo.to.m5f.entity.STranFinOutItemVO;
import nc.vo.to.m5f.entity.STransFinParaVO;
import nc.vo.to.m5f.entity.SettleListHeaderVO;
import nc.vo.to.m5f.entity.SettleListVO;

import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;

import nc.bs.framework.common.NCLocator;
import nc.bs.to.m5f.util.STQuryUtil;
import nc.cmp.tools.StringUtil;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.tool.VOConcurrentTool;
import nc.impl.pubapp.pattern.pub.LockOperator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.to.m5f.action.dofinance.STranFinContext;
import nc.impl.to.m5f.action.dofinance.SettlePushARAPProcessor;
import nc.impl.to.m5f.action.dofinance.SettlePushIAProcssor;
import nc.impl.to.m5f.action.dofinance.SettlePushInData;
import nc.impl.to.m5f.action.dofinance.SettlePushOutData;
import nc.impl.to.m5f.action.rule.dofinance.CheckEnableFinanceRule;
import nc.impl.to.m5f.action.rule.dofinance.ReWritePreSettleInRule;
import nc.impl.to.m5f.action.rule.dofinance.UpdateTransFiFlagRule;
import nc.impl.to.m5f.plugin.ActionPlugInPoint;
import nc.jdbc.framework.processor.MapProcessor;
import nc.medpub.util.SysInitParamUtil;

/**
 * 
 * 
 * @description
 *              转财务操作
 * 
 * @since 6.0
 * @version 2009-12-3 上午9:45:41
 * @author liyu1
 * 
 */

public class DoFinanceAction {

  public SettleListVO[] doFinance(SettleListVO[] vos) {

    SettleListVO[] bills = this.getOriginBills(vos);

    // 补充结算清单子子数据到VO
    STQuryUtil util = new STQuryUtil();
    util.fillLineVOForBill(bills);

    AroundProcesser<SettleListVO> processer =
        new AroundProcesser<SettleListVO>(ActionPlugInPoint.DoFinanceAction);
    TimeLog.logStart();
    this.addBeforeRule(processer);
    this.addAfterRule(processer);
    TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4009003_0", "04009003-0149")/*@res "添加转财务前后处理规则"*/); /* -=notranslate=- */

    TimeLog.logStart();
    processer.before(bills);
    TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4009003_0", "04009003-0150")/*@res "调用转财务前执行业务规则"*/); /* -=notranslate=- */

    TimeLog.logStart();

    STranFinContext context = new STranFinContext(bills);

    SettlePushOutData outdeal = new SettlePushOutData();
    SettlePushInData indeal = new SettlePushInData();

    /**
     * 设置转财务调出信息
     */
    STranFinOutAggVO[] outaggs = outdeal.getPushOutData(context);

    /**
     * 设置转财务调入信息
     */
    STranFinInAggVO[] inaggs = indeal.getPushInData(context);
    
//-----------------------内部结算传财务时，物料转换 jiuzhou  2025/12/02  begin
//    SysInitParamUtil.getParaBoolean(pk_org, initCode)
    for(STranFinInAggVO aggvo : inaggs){
    	String pk_org = aggvo.getHeaderVO().getPk_org();
    	UFBoolean to_22 = UFBoolean.FALSE; 
    	try {
    		to_22 = SysInitParamUtil.getParaBoolean(pk_org, "TO22");
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (to_22 == UFBoolean.FALSE){
    		continue ; 
    	}
    	
    	STranFinInItemVO[] children = (STranFinInItemVO[]) aggvo.getChildren(STranFinInItemVO.class);
    	for(STranFinInItemVO body : children){
    		String def1 = (String) body.getAttributeValue("vbdef1");
    		String cinventoryid = (String) body.getAttributeValue("cinventoryid");
    		if(null == def1 || StringUtil.isEmpty(def1)){
    			String destorg = body.getPk_org();
    			try {
					Map<String,String> material = this.getDestMaterial(cinventoryid, destorg);
					body.setCinventoryvid(material.get("pk_material"));
		    		body.setCinventoryid(material.get("pk_material"));
		    		continue;
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					
				}
    		}
    		body.setCinventoryvid(def1);
    		body.setCinventoryid(def1);
 
    	}
    }
  //-----------------------内部结算传财务时，物料转换 jiuzhou  2025/12/02   end
    
    
    TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4009003_0", "04009003-0151")/*@res "构造结算参数"*/); /* -=notranslate=- */

    TimeLog.logStart();
    this.pushARAPProc(context.getStvos(), outaggs, inaggs);
    TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4009003_0", "04009003-0152")/*@res "组织传应收应付的数据并传收付处理"*/); /* -=notranslate=- */

    TimeLog.logStart();
    this.pushIAProc(context.getStvos(), outaggs, inaggs);
    TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4009003_0", "04009003-0153")/*@res " 组织传存货核算的数据并传存货"*/); /* -=notranslate=- */

    TimeLog.logStart();
    processer.after(context.getStvos());
    TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4009003_0", "04009003-0154")/*@res "调用转财务后执行业务规则"*/); /* -=notranslate=- */

    TimeLog.logStart();
    // SettleListVO[] rets = getLightVO(bills);
    TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4009003_0", "04009003-0155")/*@res "组织返回值,返回轻量级VO"*/); /* -=notranslate=- */

    return bills;
  }
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
  
  /**
	 * 利用主数据编码实现物料ID转换
	 * 
	 * @param cmaterialoid
	 *            原物料ID
	 * @param destorg
	 *            目标库存组织
	 * @return 目标库存组织物料ID
	 * @throws BusinessException
	 */
	private Map<String,String> getDestMaterial(String cmaterialoid, String destorg)
			throws BusinessException {
		String sql = "select destmaterial.pk_material , destmaterial.pk_measdoc   "
				+ "from bd_material srcmaterial "
				+ "inner  join bd_material destmaterial on srcmaterial.def7 = destmaterial.def7 "
				+ " and nvl(srcmaterial.def7,'~') <>'~' and nvl(destmaterial.def7,'~')<>'~' "
				+ "where nvl(srcmaterial.dr,0)=0 and nvl(destmaterial.dr,0)=0  "
				+ " and srcmaterial.pk_material ='" + cmaterialoid + "'  "
				+ "  and destmaterial.pk_org = '" + destorg + "'";

		Map<String,String> destmaterial_pk = (Map<String, String>) iuap.executeQuery(sql, new MapProcessor());
		if (null == destmaterial_pk) {

			return null;
		}
		return destmaterial_pk;
	}

  private void addAfterRule(AroundProcesser<SettleListVO> processer) {
    // 更新结算清单转财务标识为已转财务
    IRule<SettleListVO> rule = new UpdateTransFiFlagRule();
    processer.addAfterRule(rule);

    // 更新待结算入累计转财务数量
    rule = new ReWritePreSettleInRule();
    processer.addAfterRule(rule);
  }

  private void addBeforeRule(AroundProcesser<SettleListVO> processer) {
    // 更新调入组织参考成本
    // IRule<SettleListVO> rule = new UpdateInCbRefPriceRule();
    // processer.addBeforeRule(rule);

    IRule<SettleListVO> rule = new CheckEnableFinanceRule();
    processer.addBeforeRule(rule);
  }

  private void checkTS(SettleListHeaderVO vo, BillIndex index) {
    String key = vo.getPrimaryKey();

    SettleListHeaderVO originVO =
        (SettleListHeaderVO) index.get(vo.getMetaData(), key);

    SettleListHeaderVO[] vos = new SettleListHeaderVO[] {
      vo
    };
    SettleListHeaderVO[] originVOs = new SettleListHeaderVO[] {
      originVO
    };

    VOConcurrentTool bo = new VOConcurrentTool();
    bo.checkTS(vos, originVOs);
  }

  private STranFinOutAggVO[] dealApVO(STranFinOutAggVO[] invos,
      Map<String, String> invoiceMap) {
    SplitBill<STranFinOutAggVO> split = new SplitBill<STranFinOutAggVO>();
    split.appendKey(STranFinOutItemVO.CPROFITCENTERID);

    STranFinOutAggVO[] aggvos = split.split(invos);
    for (STranFinOutAggVO vo : aggvos) {
      STranFinOutHeadVO head = vo.getHeaderVO();
      String vinvoicecode = invoiceMap.get(head.getCbillid());
      head.setVinvoicecode(vinvoicecode);
    }
    return aggvos;
  }

  private STranFinInAggVO[] dealArVO(STranFinInAggVO[] invos,
      Map<String, String> invoiceMap) {
    SplitBill<STranFinInAggVO> split = new SplitBill<STranFinInAggVO>();
    split.appendKey(STranFinInItemVO.CPROFITCENTERID);

    STranFinInAggVO[] aggvos = split.split(invos);
    for (STranFinInAggVO vo : aggvos) {
      STranFinInHeadVO head = vo.getHeaderVO();
      String vinvoicecode = invoiceMap.get(head.getCbillid());
      head.setVinvoicecode(vinvoicecode);
    }
    return aggvos;
  }

  private STranFinInAggVO[] dealInVO(STranFinInAggVO[] invos) {
    SplitBill<STranFinInAggVO> split = new SplitBill<STranFinInAggVO>();
    split.appendKey(STranFinInItemVO.CSRCID);
    split.appendKey(STranFinInItemVO.CBIZTYPEID);
    STranFinInAggVO[] aggvos = split.split(invos);
    // ScaleUtils scale = ScaleUtils.getScaleUtilAtBS();
    for (STranFinInAggVO aggvo : aggvos) {
      STranFinInItemVO[] aggItemvos = aggvo.getItemVOs();
      for (STranFinInItemVO aggItemvo : aggItemvos) {
        UFDouble subtax = aggItemvo.getNnosubtax();
        ScaleUtils scale = new ScaleUtils(aggItemvo.getPkGroup());
        aggItemvo.setNnosubtax(subtax);
        if (!MathTool.isZero(subtax)) {
          UFDouble mny = aggItemvo.getNmny();
          mny = MathTool.add(aggItemvo.getNmny(), subtax);
          UFDouble nprice = CalculatorUtil.div(mny, aggItemvo.getNnumber());
          // 传存货精度处理
          aggItemvo.setNprice(scale.adjustPriceScale(nprice));
          aggItemvo.setNmny(mny);
        }
        else {
          aggItemvo.setNprice(scale.adjustPriceScale(aggItemvo.getNprice()));
        }
      }
    }

    return aggvos;
  }

  /**
   * 按照待结算出对应的出库单将数据进行分单
   * 
   * @param outvos
   */
  private STranFinOutAggVO[] dealOutVO(STranFinOutAggVO[] outvos) {

    SplitBill<STranFinOutAggVO> split = new SplitBill<STranFinOutAggVO>();
    split.appendKey(STranFinOutItemVO.CSRCID);
    split.appendKey(STranFinOutItemVO.CBIZTYPEID);
    STranFinOutAggVO[] aggvos = split.split(outvos);
    return aggvos;

  }

  private SettleListVO[] getOriginBills(SettleListVO[] bills) {
    List<String> list = new ArrayList<String>();
    for (SettleListVO vo : bills) {
      list.add(vo.getParentVO().getCbillid());
    }
    LockOperator lock = new LockOperator();
    String message =
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009003_0",
            "04009003-0330")/*@res "出现并发，请稍候再试"*/;
    lock.lock(list.toArray(new String[list.size()]), message);
    BillQuery<SettleListVO> query =
        new BillQuery<SettleListVO>(SettleListVO.class);
    SettleListVO[] vos = query.query(list.toArray(new String[list.size()]));

    BillIndex index = new BillIndex(vos);
    for (SettleListVO vo : bills) {
      this.checkTS(vo.getParentVO(), index);
    }

    return vos;

  }

  private void pushARAPProc(SettleListVO[] bills, STranFinOutAggVO[] outaggs,
      STranFinInAggVO[] inaggs) {
    STransFinParaVO paravo = new STransFinParaVO();
    paravo.setStvos(bills);
    // 处理发票号，根据结算清单表头发票号填充传应收应付发票号
    Map<String, String> invoiceMap = this.dealInvoiceCode(bills);
    STranFinOutAggVO[] outvos = new STranFinOutAggVO[0];
    if (outaggs != null && outaggs.length > 0) {
      outvos = this.dealApVO(outaggs, invoiceMap);
    }
    STranFinInAggVO[] invos = new STranFinInAggVO[0];
    if (inaggs != null && inaggs.length > 0) {
      invos = this.dealArVO(inaggs, invoiceMap);
    }
    paravo.setSfinvos(invos);
    paravo.setSfoutvos(outvos);

    // 传应收、应付处理
    SettlePushARAPProcessor proc = new SettlePushARAPProcessor();
    proc.pushARAPProc(paravo);
  }

  // 建立结算清单主表ID与对应发票号的关系，为之后传应收应付的填充做准备
  private Map<String, String> dealInvoiceCode(SettleListVO[] bills) {
    Map<String, String> map = new HashMap<String, String>();
    for (SettleListVO bill : bills) {
      SettleListHeaderVO head = bill.getParentVO();
      map.put(head.getCbillid(), head.getVinvoicecode());
    }
    return map;
  }

  private void pushIAProc(SettleListVO[] bills, STranFinOutAggVO[] outaggs,
      STranFinInAggVO[] inaggs) {

    STranFinOutAggVO[] outvos = new STranFinOutAggVO[0];
    if (outaggs != null && outaggs.length > 0) {
      outvos = this.dealOutVO(outaggs);
    }

    STranFinInAggVO[] invos = new STranFinInAggVO[0];
    if (inaggs != null && inaggs.length > 0) {
      invos = this.dealInVO(inaggs);
    }

    STransFinParaVO paravo = new STransFinParaVO();
    paravo.setStvos(bills);
    paravo.setSfinvos(invos);
    paravo.setSfoutvos(outvos);

    if (!SysInitGroupQuery.isIAEnabled()) {
      return;
    }

    // 传存货调拨出，存货调拨入
    SettlePushIAProcssor proc = new SettlePushIAProcssor();
    proc.pushIAProc(paravo);
  }

}
