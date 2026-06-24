/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/

package nc.itf.cm.inprocess;

import nc.pub.smart.data.DataSet;
import nc.vo.cm.equivrate.entity.EquivrateAggVO;
import nc.vo.cm.inprocess.entity.InprocessAggVO;
import nc.vo.cm.inprostuff.entity.InproStuffAggVO;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;
public  interface IInprocessBackFlushService2
{
	
	
	/**
	 * 在产材料计算数据集
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet proMaterialsStuff(  IContext context)throws  BusinessException;

	
	/**
	 * 在产订单报表数据集
	 * @param param
	 * @return
	 * @throws BusinessException
	 */
	public DataSet proWrOrders(  IContext context)throws  BusinessException;

	
	
	/**
	 * 自动盘点
	 * @param useid
	 * @param data
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
  public abstract InprocessAggVO[] backFlushProduct(InprocessAggVO bill)
    throws  Exception;
  
  /**
   * 自动约当
   * @param pk_org
   * @param period
 * @return 
   * @throws BusinessException
   * @throws Exception
   */
  public abstract EquivrateAggVO[] autoEquivrate(String pk_org, String period)
		    throws Exception;
  
  
  
/**
 * 自动材料计算
 * @param pk_org
* @return 
* @param period
 * @throws BusinessException
 * @throws Exception
 */
public abstract InproStuffAggVO[] autoInprostuff(String pk_org, String period)
		    throws  Exception;
 
}
