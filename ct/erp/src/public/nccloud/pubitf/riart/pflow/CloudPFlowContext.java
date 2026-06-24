package nccloud.pubitf.riart.pflow;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pubapp.pflow.PfUserObject;

/**
 * 
 * @since 2018-4-2 下午1:45:32
 * @version 1.0.0
 * @author 于晓龙
 */

public class CloudPFlowContext {

  private String actionName;

  private String billType;

  private AggregatedValueObject[] billVos;

  private Map<Object, Object> eParam;

  private Map<Integer, String> exceptionResults =
      new HashMap<Integer, String>();

  private boolean isBatch;

  private String mdOperateCode; // 元数据操作编码

  private String operateCode; // 资源对象操作编码，以上两者注入其一，都不注入，则不进行数据权限控制。

  private String resourceCode; // 业务实体资源编码

  private String trantype;

  private String userId = "";

  private PfUserObject[] userObj;
  
  private Object[] batchUserObj;
  
  public Object[] getBatchUserObj() {
	return batchUserObj;
}

public void setBatchUserObj(Object[] batchUserObj) {
	this.batchUserObj = batchUserObj;
}


//  public Map<String, Object> getFlowparam() {
//	return flowparam;
//  }
//
//  public void setFlowparam(Map<String, Object> flowparam) {
//	this.flowparam = flowparam;
//	}
//
//private Map<String, Object> flowparam;

  /**
   * 合并异常信息
   * 
   * @param newExceptionResults
   */
  public void combinExceptionResults(Map<Integer, String> newExceptionResults) {
    Set<Entry<Integer, String>> entrySet = newExceptionResults.entrySet();
    for (Entry<Integer, String> entry : entrySet) {
      if (!this.alreadyContainResult(entry.getKey())) {
        this.exceptionResults.put(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * @return the actionName
   */
  public String getActionName() {
    return this.actionName;
  }

  /**
   * @return the billType
   */
  public String getBillType() {
    return this.billType;
  }

  /**
   * @return the billVos
   */
  public AggregatedValueObject[] getBillVos() {
    return this.billVos;
  }

  /**
   * @return the eParam
   */
  public Map<Object, Object> geteParam() {
    return this.eParam;
  }

  /**
   * @return the exceptionResults
   */
  public Map<Integer, String> getExceptionResults() {
    return this.exceptionResults;
  }

  /**
   * @return the mdOperateCode
   */
  public String getMdOperateCode() {
    return this.mdOperateCode;
  }

  /**
   * @return the operateCode
   */
  public String getOperateCode() {
    return this.operateCode;
  }

  /**
   * @return the resourceCode
   */
  public String getResourceCode() {
    return this.resourceCode;
  }

  /**
   * @return the trantype
   */
  public String getTrantype() {
    return this.trantype;
  }

  /**
   * @return the userId
   */
  public String getUserId() {
    return this.userId;
  }

  /**
   * @return the userObj
   */
  public PfUserObject[] getUserObj() {
    return this.userObj;
  }

  /**
   * @return the isBatch
   */
  public boolean isBatch() {
    return this.isBatch;
  }

  /**
   * @param actionName the actionName to set
   */
  public void setActionName(String actionName) {
    this.actionName = actionName;
  }

  /**
   * @param isBatch the isBatch to set
   */
  public void setBatch(boolean isBatch) {
    this.isBatch = isBatch;
  }

  /**
   * @param billType the billType to set
   */
  public void setBillType(String billType) {
    this.billType = billType;
  }

  /**
   * @param billVos the billVos to set
   */
  public void setBillVos(AggregatedValueObject[] billVos) {
    this.billVos = billVos;
  }

  /**
   * @param eParam the eParam to set
   */
  public void seteParam(Map<Object, Object> eParam) {
    this.eParam = eParam;
  }

  /**
   * @param exceptionResults the exceptionResults to set
   */
  public void setExceptionResults(Map<Integer, String> exceptionResults) {
    this.exceptionResults = exceptionResults;
  }

  /**
   * @param mdOperateCode the mdOperateCode to set
   */
  public void setMdOperateCode(String mdOperateCode) {
    this.mdOperateCode = mdOperateCode;
  }

  /**
   * @param operateCode the operateCode to set
   */
  public void setOperateCode(String operateCode) {
    this.operateCode = operateCode;
  }

  /**
   * @param resourceCode the resourceCode to set
   */
  public void setResourceCode(String resourceCode) {
    this.resourceCode = resourceCode;
  }

  /**
   * @param trantype the trantype to set
   */
  public void setTrantype(String trantype) {
    this.trantype = trantype;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * @param userObj the userObj to set
   */
  public void setUserObj(PfUserObject[] userObj) {
    this.userObj = userObj;
  }

  private boolean alreadyContainResult(Integer index) {
    return this.exceptionResults.containsKey(index)
        && this.exceptionResults.get(index) != null;
  }

}
