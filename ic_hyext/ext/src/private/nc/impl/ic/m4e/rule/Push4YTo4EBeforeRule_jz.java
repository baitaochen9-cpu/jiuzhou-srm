package nc.impl.ic.m4e.rule;

import nc.bs.bd.update63xto636.ClosecheckUpdate.MapListProcessor;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ic.general.util.GenBsUtil;
import nc.impl.pubapp.pattern.rule.IFilterRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.m4e.deal.IC4YTo4EProcess;
import nc.vo.ic.m4e.entity.TransInBodyVO;
import nc.vo.ic.m4e.entity.TransInVO;
import nc.vo.pub.ISuperVO;

/**
 * <p>
 * <b>本类主要完成以下功能：推式保存前的处理
 * 包括设置交易类型、行号
 * </b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author lirr
 * @time 2010-5-18 下午07:45:12
 */
public class Push4YTo4EBeforeRule_jz implements IFilterRule<TransInVO> {

  
 BaseDAO dao = null ;

  /**
   * 父类方法重写
   */
  @Override
  public TransInVO[] process(TransInVO[] vos) {
	  /*
	   * 1、检查数据 是否为空，
	   * 2、检查来源表体数据的来源单信息后找来源单据的物料号是否与当前单据物料号一至，如果不一至，则清除批次号ID
	   */
	  if(null == vos || vos.length == 0){
		  return vos;
	  }
	  for(TransInVO bill : vos ){
		  TransInBodyVO[] children = (TransInBodyVO[]) bill.getChildren(TransInBodyVO.class);
		  if(null == children || children.length ==0){
			  continue;
		  }
		  
		  for (TransInBodyVO body :  children){
			  String csourcebillbid = body.getCsourcebillbid();//来源单据表体ID
			  String sql = "select cmaterialoid  from ic_transout_b where cgeneralbid ='"+csourcebillbid+"'";//出库单物料
			  String executeQuery  = "";
			  try {
				 executeQuery = (String) this.getDao().executeQuery(sql,new ColumnProcessor() );
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  if (!executeQuery.equals(csourcebillbid)){
				  //这里增加目标物料取批次号信息，不能直接设置为空值 ，需要考虑同一批次多次调入的问题。
				  body.getVbatchcode();
				  String sql2 = " select pk_batchcode  from scm_batchcode "+
                  " where cmaterialoid ='"+body.getCmaterialoid()+"' " +
                  		" and scm_batchcode.vbatchcode ='"+ body.getVbatchcode()+"'  and nvl(dr,0)=0";
				  Object pk_batchcode = null;
				  try {
					  pk_batchcode = this.getDao().executeQuery(sql2, new ColumnProcessor());
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  if(null == pk_batchcode ){ //批次号档案未找到对应批次，应新增
					  body.setPk_batchcode(null);
					  
				  }else {
					  body.setPk_batchcode((String)pk_batchcode);
				}
			  }
		  }
	  }
    return vos;
  }

  private BaseDAO getDao() {
	  if (null == dao){
		  dao = new BaseDAO();
	  }
	return dao;
}
}
