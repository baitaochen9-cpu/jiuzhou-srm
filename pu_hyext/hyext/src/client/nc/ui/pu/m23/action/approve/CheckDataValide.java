package nc.ui.pu.m23.action.approve;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pu.m23.entity.ArriveHeaderVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class CheckDataValide {
	public void check(ArriveVO aggVO ) {
		try{
			 ArriveItemVO[] ivo=aggVO.getBVO();
			 ArriveHeaderVO hvo=aggVO.getHVO();
			 UFDate  hdate=hvo.getDbilldate().asEnd();
			 for(int i=0;i<ivo.length;i++){
				   UFDate dproducedate=ivo[i].getDproducedate();//生产日期
				   if(dproducedate==null){
					   String sql="select qualitymanflag  from bd_materialstock where pk_org='"+hvo.getPk_org()+"' and pk_material='"+ivo[i].getPk_material()+"'";			
						String qualitymanflag = (String) NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
						if("Y".equalsIgnoreCase(qualitymanflag)){
							   throw new BusinessException("生产日期不能为空");
						}					 
	
				   }else{
					   dproducedate= dproducedate.asEnd();
					   if(dproducedate.after(hdate)){
						   throw new BusinessException("生产日期不能晚于到货日期");
					   }
				   
				   }
			 }
		
		}catch(Exception e){
			   ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	
		 
	}

}
