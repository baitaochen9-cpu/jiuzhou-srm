package nc.bs.mmpac.apply.task.rule;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpac.apply.task.consts.TaskALangConsts;
import nc.vo.mmpac.apply.task.entity.AggTaskAVO;
import nc.vo.mmpac.apply.task.entity.TaskABVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class CheckUpdateWGBGRule2 implements IRule<AggTaskAVO> {
	    @Override
	    public void process(AggTaskAVO[] vos) {
	        this.CheckUpdateWGBGRule(vos);
	    }

	    /**
	     * 
	     * 
	     * @param vos
	     */
	    private void CheckUpdateWGBGRule(AggTaskAVO[] vos) {
	        if (null == vos) {
	            return;
	        }
	        
	        for (AggTaskAVO vo : vos) {
	            
	            
	            String csrcid=vo.getParentVO().getCsrcid();
	            String pk_org=vo.getParentVO().getPk_org();
	            String vbillcode =vo.getParentVO().getVsrccode();
	            try {
					String kz=querySysinitName("MMPAVC01",pk_org);
				
		            if(vbillcode!=null&&!"".equals(vbillcode)){
//		            	if(!"Y".equals(kz)){
		            		
		            		String iswh=queryIsHave(vbillcode);
//		            		if("Y".equals(iswh)){
//		            			 ExceptionUtils.wrappBusinessException("已维护作业量不能重复维护");
//		            		}
		            		String updatesql=" update mm_wr set vdef2='N' where  vbillcode  ='"+vbillcode +"'  and nvl(dr,0)=0";
		            		BaseDAO basedao=new BaseDAO();
		            		basedao.executeUpdate(updatesql);
		            		
//		            	}
		            }
	            } catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 ExceptionUtils.wrappBusinessException(e.getMessage());
				}
	            
	          
	        }
	    }
	    
	    //查询是否维护过
		private String queryIsHave(String id) throws BusinessException {
			  String sql = "select vdef2  from mm_wr  where vbillcode ='"+id+"'  and nvl(dr,0)=0";
			  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
			  return obj == null ? "" : obj.toString();
			 }
	    
		  //查询参数代码
			private String querySysinitName(String initcode,String pk_org) throws BusinessException {
				  String sql = "select value from pub_sysinit  where initcode='"+initcode+"' and pk_org='"+pk_org+"' and nvl(dr,0)=0";
				  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
				  return obj == null ? "" : obj.toString();
				 }
	

}
