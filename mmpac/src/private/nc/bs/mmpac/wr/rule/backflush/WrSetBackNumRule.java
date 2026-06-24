package nc.bs.mmpac.wr.rule.backflush;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 审核更新订单计划数量
 * @author yunfeng.li
 *
 */
public class WrSetBackNumRule implements IRule<AggWrVO> {

    @Override
    public void process(AggWrVO[] vos) {

        if (MMValueCheck.isEmpty(vos)) {
            return;
        }
        WrItemVO[] items = (WrItemVO[]) vos[0].getChildren(WrItemVO.class);
        if (MMValueCheck.isEmpty(items)) {
            return;
        }
//        WrPickVO[] pickVOs = items[0].getPickvos();
//        if (MMValueCheck.isEmpty(pickVOs)) {
//            return;
//        }
        
        String sql=
        		"   select  c.cpoid\n" +
        				"   from mm_wr_product a left join  mm_mo b on a.cbfirstmobid=b.cmoid and b.dr=0\n" + 
        				"   left join mm_plo c on b.vsrcid=c.cpoid and c.dr=0\n" + 
        				"where a.dr=0\n" + 
        				"and a.pk_wr_product in ( '#'";

        

     

        for (AggWrVO aggWrVO : vos) {

            if (MMValueCheck.isEmpty(aggWrVO.getChildren(WrItemVO.class))) {
                continue;
            }

            for (WrItemVO wrItemVO : (WrItemVO[]) aggWrVO.getChildren(WrItemVO.class)) {

                sql=sql+",'"+wrItemVO.getPk_wr_product()+"'";
                
            }
        }
        try {
        	IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	List<Map<String,Object>> list = (List<Map<String,Object>> )iuap.executeQuery(sql+")", new MapListProcessor());
        	Set<String>set= new HashSet<String>();
        	if(list==null||list.size()==0){
        		return;
        	}
        	for(int i=0;i<list.size();i++){
        		if(list.get(i).get("cpoid")==null||"".equals(list.get(i).get("cpoid"))){
        			return;
        		}
        		String cpoid=list.get(i).get("cpoid").toString();
        		set.add(cpoid);
        		
        	}
        	
        	for(String key:set){
        		String updatesql=
        				"update mm_plo\n" +
        						"   set vdef3 =\n" + 
        						"       (select sum(a.nbwrnum) as nbwrastnum\n" + 
        						"          from mm_wr_product a\n" + 
        						"          left join mm_wr d on a.pk_wr=d.pk_wr\n" + 
        						"          left join mm_mo b\n" + 
        						"            on a.cbfirstmobid = b.cmoid\n" + 
        						"           and b.dr = 0\n" + 
        						"          left join mm_plo c\n" + 
        						"            on b.vsrcid = c.cpoid\n" + 
        						"           and c.dr = 0\n" + 
        						"         where a.dr = 0 and d.fbillstatus =2  \n" + 
        						"        and a.fbproducttype='1'   and c.cpoid = '"+key+"'"+
        						"           )\n" + 
        						" where cpoid = '"+key+"'";

        				
//        			
//        						"update  mm_plo set vdef1 =(  select sum(a.nbwrastnum) as nbwrastnum\n" + 
//        						"   from mm_wr_product a left join  mm_mo b on a.cbfirstmobid=b.cmoid and b.dr=0\n" + 
//        						"   left join mm_plo c on b.vsrcid=c.cpoid and c.dr=0\n" + 
//        						"where a.dr=0\n" + 
//        						"and c.cpoid='"+key+"') where cpoid='"+key+"'";
        		
        		
        		BaseDAO basedao=new BaseDAO();
        		basedao.executeUpdate(updatesql);

        	}
      

   
           
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
    }
}
