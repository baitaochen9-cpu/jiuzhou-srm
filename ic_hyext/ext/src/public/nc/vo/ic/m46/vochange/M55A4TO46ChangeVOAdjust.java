package nc.vo.ic.m46.vochange;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.general.util.GenBsUtil;
import nc.pubitf.ic.query.ILiabilityQuery;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.m46.deal.Bill55A4to46Process;
import nc.vo.ic.m46.entity.FinProdInHeadVO;
import nc.vo.ic.m46.entity.FinProdInVO;
import nc.vo.ic.pub.define.ICLiabilityQueryReturnVO;
import nc.vo.ic.pub.define.ICLiabilityQueryVOForCwarehouse;
import nc.vo.ic.pub.define.ICLiabilityQueryVOForCworkcenter;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.pf.ICDefaultChangeVOAdjust;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.tbb.SysRegConst;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang.ArrayUtils;

/**
 * 生产报告至产成品入库vo交换处理类
 * 
 * @since 6.0
 * @version 2011-6-17 下午01:30:25
 * @author wanghna
 */
public class M55A4TO46ChangeVOAdjust extends ICDefaultChangeVOAdjust {

  @Override
  public AggregatedValueObject[] batchAdjustAfterChange(
      AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs,
      ChangeVOAdjustContext adjustContext) throws BusinessException {
    this.processSourceType(destVOs);
    // 初始化前置转单处理器
    Bill55A4to46Process proc = new Bill55A4to46Process();
    GenBsUtil.initTransBillBaseProcess(proc);
    this.dealLiability(destVOs);
    
    // 处理“入库返修”字段
    this.dealBreworkflag(destVOs);
    
    return proc.processBillVOs((FinProdInVO[]) destVOs);
  }
  
  private void dealBreworkflag(AggregatedValueObject[] destVOs)
      throws BusinessException {
    ChangeVOToolFor46 tool = new ChangeVOToolFor46();
    tool.dealBreworkflag(destVOs);
  }

  public void dealLiability(AggregatedValueObject[] destVOs) {
    if (ArrayUtils.isEmpty(destVOs)) {
      return;
    }
    QueryParamContainer container = this.getQueryParamContainer(destVOs);
    
    Map<String, ICLiabilityQueryReturnVO> returnVOMapForCIOLia =
        this.getReturnVOMapForCIOLia(container);

    Map<String, ICLiabilityQueryReturnVO> returnVOMapForCLia =
        this.getReturnVOMapForCLia(container);
    
    for(AggregatedValueObject vo :destVOs){
      CircularlyAccessibleValueObject[] bodyvos = vo.getChildrenVO();
      if(ArrayUtils.isEmpty(bodyvos)){
        continue;
      }
      for(CircularlyAccessibleValueObject bodyvo:bodyvos){
        String pkOrg =
            (String) vo.getParentVO().getAttributeValue(
                ICPubMetaNameConst.PK_ORG);
        String cwarehouseid =
            (String) vo.getParentVO().getAttributeValue(
                ICPubMetaNameConst.CWAREHOUSEID);
        ICLiabilityQueryReturnVO cioLiaVO = returnVOMapForCIOLia.get(pkOrg+cwarehouseid);
        if (cioLiaVO != null) {
          bodyvo.setAttributeValue(MetaNameConst.CIOLIABILITYVID,
              cioLiaVO.getLiabilityvid());
          bodyvo.setAttributeValue(MetaNameConst.CIOLIABILITYOID,
              cioLiaVO.getLiabilityoid());
        }
        // 生产利润中心
        String cprocalbodyoid =
            (String) vo.getParentVO().getAttributeValue(
                FinProdInHeadVO.CPROCALBODYOID);
        String cdptid =
            (String) vo.getParentVO().getAttributeValue(
                ICPubMetaNameConst.CDPTID);
        String cworkcenterid =
            (String) bodyvo
                .getAttributeValue(MetaNameConst.CWORKCENTERID);
        ICLiabilityQueryReturnVO cLiaVO =
            returnVOMapForCLia.get(cprocalbodyoid + cdptid + cworkcenterid);
        bodyvo.setAttributeValue(MetaNameConst.CLIABILITYVID,
            cLiaVO.getLiabilityvid());
        bodyvo.setAttributeValue(MetaNameConst.CLIABILITYOID,
            cLiaVO.getLiabilityoid());       
      }
    }   
  }
  /**
   * 获取利润中心的返回结果MAP
   */
  private Map<String, ICLiabilityQueryReturnVO> getReturnVOMapForCLia(
      QueryParamContainer container) {
    Map<String, ICLiabilityQueryVOForCworkcenter> paramsMap =
        container.getParamsMapForCworkcenter();
    if(ValueCheckUtil.isNullORZeroLength(paramsMap)){
      return new HashMap<String, ICLiabilityQueryReturnVO>();
    }
    ICLiabilityQueryReturnVO[] returnVOs = new ICLiabilityQueryReturnVO[0];
    try {
      returnVOs =
          this.getService().queryCliabilityByCworkcenter(
              paramsMap.values().toArray(
                  new ICLiabilityQueryVOForCworkcenter[paramsMap.size()]));
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    if(ArrayUtils.isEmpty(returnVOs)){
      return new HashMap<String, ICLiabilityQueryReturnVO>();
    }
    return this.processRet(paramsMap.keySet(), returnVOs);
  }
  /**
   * 获取工厂利润中心的返回结果MAP
   */
  private Map<String, ICLiabilityQueryReturnVO> getReturnVOMapForCIOLia(
      QueryParamContainer container) {
    Map<String, ICLiabilityQueryVOForCwarehouse> paramsMap =
        container.getParamsMapForCwarehouse();
    if (ValueCheckUtil.isNullORZeroLength(paramsMap)) {
      return new HashMap<String, ICLiabilityQueryReturnVO>();
    }
    ICLiabilityQueryReturnVO[] returnVOs =
        new ICLiabilityQueryReturnVO[0];
    try {
      returnVOs =
          this.getService().queryCliabilityByCwarehouse(
              paramsMap.values().toArray(
                  new ICLiabilityQueryVOForCwarehouse[paramsMap.size()]));
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    if (ArrayUtils.isEmpty(returnVOs)) {
      return new HashMap<String, ICLiabilityQueryReturnVO>();
    }
    return this.processRet(paramsMap.keySet(), returnVOs);
  }
  
  private Map<String, ICLiabilityQueryReturnVO> processRet(Set<String> keySet,
      ICLiabilityQueryReturnVO[] returnVOs) {
    Map<String, ICLiabilityQueryReturnVO> returnMap =
        new HashMap<String, ICLiabilityQueryReturnVO>();
    int count = 0;
    for (String key : keySet) {
      //由于前面查的时候是用HashMap的values()方法去获取查询参数，并且返回的结果集是根据传进去参数排序
      //的，因此这里调用keySet()获取的key和返回的结果数组是一一对应的
      returnMap.put(key, returnVOs[count]);
      count++;
    }
    return returnMap;
  }
  
  /**
   * 将查询参数组装放入查询容器，并返回查询容器
   * 
   * @param destVOs
   * @return QueryParamContainer
   */
  private QueryParamContainer getQueryParamContainer(
      AggregatedValueObject[] destVOs) {
    QueryParamContainer container = new QueryParamContainer();
    for (AggregatedValueObject destvo : destVOs) {
      String pkOrg =
          (String) destvo.getParentVO().getAttributeValue(
              ICPubMetaNameConst.PK_ORG);
      String cwarehouseid =
          (String) destvo.getParentVO().getAttributeValue(
              ICPubMetaNameConst.CWAREHOUSEID);
      container.addToParamsMapForWarehouse(pkOrg, cwarehouseid);
      CircularlyAccessibleValueObject[] childrenVOs = destvo.getChildrenVO();
      if(ArrayUtils.isEmpty(childrenVOs)){
        continue;
      }
      for(CircularlyAccessibleValueObject childrenVO:childrenVOs){
        String cprocalbodyoid =
            (String) destvo.getParentVO().getAttributeValue(
                SysRegConst.CPROCALBODYOID);
        String cdptid =
            (String) destvo.getParentVO().getAttributeValue(
                ICPubMetaNameConst.CDPTID);
        String cworkcenterid =
            (String) childrenVO
                .getAttributeValue(MetaNameConst.CWORKCENTERID);
        container.addToParamsMapForWorkcenter(cprocalbodyoid, cdptid, cworkcenterid);
      }
    }
    return container;
  }
  
  
  private ILiabilityQuery getService() {
    return (ILiabilityQuery) NCLocator.getInstance().lookup(
        ILiabilityQuery.class.getName());
  }
  /**
   * 查询参数容器
   * 
   */
  private class QueryParamContainer {
    
    private Map<String,ICLiabilityQueryVOForCwarehouse> paramsMapForWarehouse;
    private Map<String,ICLiabilityQueryVOForCworkcenter> paramsMapForWorkcenter;
    
    public QueryParamContainer(){
      paramsMapForWarehouse = new HashMap<String,ICLiabilityQueryVOForCwarehouse>();
      paramsMapForWorkcenter = new HashMap<String,ICLiabilityQueryVOForCworkcenter>();
    }
    
    public void addToParamsMapForWarehouse(String pk_org,String cwarehouse){
      String key = pk_org + cwarehouse;
      if(paramsMapForWarehouse.containsKey(key)){
        return;
      }
      ICLiabilityQueryVOForCwarehouse param = new ICLiabilityQueryVOForCwarehouse();
      param.setPk_org(pk_org);
      param.setCwarehouseid(cwarehouse);
      this.paramsMapForWarehouse.put(key, param);
    }
    
    public void addToParamsMapForWorkcenter(String cprocalbodyoid,String cdptid,String cworkcenterid){
      String key = cprocalbodyoid + cdptid + cworkcenterid;
      if(paramsMapForWorkcenter.containsKey(key)){
        return;
      }
      ICLiabilityQueryVOForCworkcenter param = new ICLiabilityQueryVOForCworkcenter();
      param.setFacorg(cprocalbodyoid);
      param.setCdptid(cdptid);
      param.setCworkcenterid(cworkcenterid);
      this.paramsMapForWorkcenter.put(key, param);
    }
    
    public Map<String,ICLiabilityQueryVOForCwarehouse> getParamsMapForCwarehouse(){
      return this.paramsMapForWarehouse;
    }
    
    public Map<String,ICLiabilityQueryVOForCworkcenter> getParamsMapForCworkcenter(){
      return this.paramsMapForWorkcenter;
    }
    
    
  }

}
