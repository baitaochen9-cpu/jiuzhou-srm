/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.bs.mmpps.plo.rule;
import org.apache.commons.lang3.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.util.mmf.framework.base.MMNumberUtil;
import nc.vo.bd.material.pu.MaterialPuVO;
import nc.vo.mmpps.mps0202.AggregatedPoVO;
import nc.vo.mmpps.mps0202.PoVO;
import nc.vo.mmpps.plo.util.PloNumUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 设置下达、差能分流时本次下达数量
 * 
 * @since 6.5
 * @version 2014-1-16 下午01:50:54
 * @author liuzhya
 */
public class PloReleaseSetNumRule implements IRule<AggregatedPoVO> {

	

    @Override
    public void process(AggregatedPoVO[] aggs) {
        PoVO[] povos = AggregatedPoVO.constructPoVOs(aggs);
        String calcType = calcType(povos[0].getPk_org());
        for (int j = 0; j < povos.length; j++) {
            // 计算默认下达主数量
            UFDouble thisnum = PloNumUtil.calculation(povos[j]);
            if (MMNumberUtil.isLsZero(thisnum)) {
                thisnum = UFDouble.ZERO_DBL;
            }
          
        
            // 辅数量
            UFDouble nassNum = PloNumUtil.calculationAss(povos[j]);
            if (MMNumberUtil.isLsZero(nassNum)) {
                nassNum = UFDouble.ZERO_DBL;
            }
            
            
            //2023-11-15 liyf 设置最小起订量
              if("最小起订/最小包装".equalsIgnoreCase(calcType)){
              	thisnum = caleMinnum(povos[j], thisnum);
              	nassNum = caleMinnum(povos[j], nassNum);

              }
            povos[j].setNaccthisnum(thisnum);

            povos[j].setNassaccthisnum(nassNum);
            
            
         
            
        }
    }
    
    private UFDouble caleMinnum(PoVO pvo,UFDouble thisnum){

 	   String cmaterialvid = pvo.getCmaterialvid();
        String condition=" and pk_material='"+cmaterialvid+"' and pk_org='"+pvo.getPk_org()+"'";
        VOQuery<MaterialPuVO > bq = new   VOQuery<MaterialPuVO >(MaterialPuVO.class);

        MaterialPuVO[] vos = bq.query(condition, null);
        if(vos!=null && vos.length >0){
        	MaterialPuVO vo = vos[0];
        	UFDouble minNum = UFDouble.ZERO_DBL;
        	UFDouble minPackNum = UFDouble.ONE_DBL;
        	String def2 = vo.getDef2();//最小起订量
        	if(StringUtils.isNotEmpty(def2)){
        		minNum = new UFDouble(def2);
        	}
        	
        	String def3 = vo.getDef3();//最小包装量
        	if(StringUtils.isNotEmpty(def3)){
        		minPackNum = new UFDouble(def3);
        	}
        	//如果本次下达数量小于最小起订量，则更新数量为最小起订量
        	if(thisnum.compareTo(minNum) <0){
        		thisnum = minNum;
        	}
            	//如果本次下达数量大于最小起订量，则更新数量为 向上取整[数量/最小包装量]*最小包装量
         	if(thisnum.compareTo(minNum) >0){
        		UFDouble packnum = thisnum.div(minPackNum).setScale(0, UFDouble.ROUND_UP);
        		thisnum = packnum.multiply(minPackNum);
            }
            
        }
        return thisnum;
     
    }
    
    /**
	 * 
	 * 组织参数，判断
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	public  String calcType(String pk_org)  {
		String sql="select DISTINCT value  from  pub_sysinit where initcode='YFMM001' and pk_org = '"+pk_org+"' ";
		
		String caltype="系统默认";
		try {
			caltype = (String)new BaseDAO().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ExceptionUtils.wrappException(new BusinessException(e
					.getMessage()));
		}
	
		return caltype;
	}

}
