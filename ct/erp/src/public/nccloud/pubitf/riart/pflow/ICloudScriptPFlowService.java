package nccloud.pubitf.riart.pflow;

import nc.vo.pub.BusinessException;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;

/**
 * 动作脚本执行服务
 * 
 * @since 2018-4-19 下午1:56:34
 * @version 1.0.0
 * @author 于晓龙
 */

public interface ICloudScriptPFlowService {

  /**
   * 执行动作脚本
   * 
   * @param context 上下问题信息
   * @return Object[]
   * @throws BusinessException
   */
  public Object[] exeScriptPFlow(CloudPFlowContext context)
      throws BusinessException;

  /**
   * 执行动作脚本
   * 
   * @param context 上下问题信息
   * @return Object[]
   * @throws BusinessException
   */
  public Object[] exeScriptPFlow_RequiresNew(CloudPFlowContext context)
      throws BusinessException;
  
  /**
   * 批量提交没有审批流的情况
   * 
   * @param context 上下问题信息
   * @return Object[]
   * @throws BusinessException
   */
  public Object[] exeScriptPFlow_CommitNoFlowBatch(CloudPFlowContext context) throws BusinessException;
  
  /**
   * 批量收回没有审批流的情况
   * 
   * @param context 上下问题信息
   * @return Object[]
   * @throws BusinessException
   */
  public Object[] exeScriptPFlow_UnSaveNoFlowBatch(CloudPFlowContext context) throws BusinessException;
  
  

  



  
}
