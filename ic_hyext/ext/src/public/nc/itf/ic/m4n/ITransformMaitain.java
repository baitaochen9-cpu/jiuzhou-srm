package nc.itf.ic.m4n;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ic.m4a.entity.GeneralInVO;
import nc.vo.ic.m4i.entity.GeneralOutVO;
import nc.vo.ic.m4n.entity.TransformVO;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.pub.BusinessException;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-5-28 上午11:13:25
 */
public interface ITransformMaitain {

  /**
   * 方法功能描述：
   * <p>
   * 删除单据 <b>参数说明</b>
   * 
   * @param bills
   * @throws BusinessException
   *           <p>
   * @since 6.0
   * @author chennn
   * @time 2010-5-28 上午11:17:01
   */
  public void delete(TransformVO[] bills) throws BusinessException;

  /**
   * 方法功能描述：
   * <p>
   * 新增单据 <b>参数说明</b>
   * 
   * @param bills
   * @return
   * @throws BusinessException
   *           <p>
   * @since 6.0
   * @author chennn
   * @time 2010-5-28 上午11:16:26
   */
  public TransformVO[] insert(TransformVO[] bills) throws BusinessException;

  public TransformVO[] query(String swhere) throws BusinessException;
  
  public TransformVO[] query(IQueryScheme queryScheme) throws BusinessException;

  /**
   * 方法功能描述：
   * <p>
   * 更新单据 <b>参数说明</b>
   * 
   * @param bills
   * @param originbills
   * @return
   * @throws BusinessException
   *           <p>
   * @since 6.0
   * @author chennn
   * @time 2010-5-28 上午11:16:49
   */
  public TransformVO[] update(TransformVO[] bills,TransformVO[] originbills) throws BusinessException;
  
  TransformVO pushSave(  OnhandSNViewVO datavo,OnhandSNViewVO[] datavos) throws BusinessException;
  
  GeneralInVO pushSaveIn(  OnhandSNViewVO datavo,OnhandSNViewVO[] datavos) throws BusinessException;
  GeneralOutVO pushSaveOut(  OnhandSNViewVO datavo,OnhandSNViewVO[] datavos) throws BusinessException;
}
