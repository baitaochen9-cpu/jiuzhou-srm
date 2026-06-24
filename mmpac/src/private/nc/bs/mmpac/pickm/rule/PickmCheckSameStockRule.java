package nc.bs.mmpac.pickm.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.mmpac.pickm.adapter.TransTypeServiceAdapter;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpac.pickm.consts.PickmLangConsts;
import nc.vo.mmpac.pickm.entity.AggPickmVO;
import nc.vo.mmpac.pickm.entity.PickmHeadVO;
import nc.vo.mmpac.pickm.entity.PickmItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 委外类型备料计划发料仓库不能为空，校验供应仓库和发料仓库不能相同的RULE
 * 
 * @since 6.3
 * @version 2012-5-17 上午11:26:43
 * @author maoleia
 */
public class PickmCheckSameStockRule implements IRule<AggPickmVO> {

    @Override
    public void process(AggPickmVO[] aggPickmVOs) {

        if (MMValueCheck.isEmpty(aggPickmVOs)) {
            return;
        }
        List<String> transIDs = new ArrayList<String>();
        for (AggPickmVO aggVO : aggPickmVOs) {

            if (MMValueCheck.isEmpty(aggVO)) {
                continue;
            }
            transIDs.add(aggVO.getParentVO().getVbusitypeid());
        }
        Map<String, Boolean> isWWMap = new HashMap<String, Boolean>();
        if (MMValueCheck.isNotEmpty(transIDs)) {
            isWWMap = TransTypeServiceAdapter.isWWPickmTransType(transIDs.toArray(new String[0]));
        }

        List<ValidationFailure> failureList = new ArrayList<ValidationFailure>();

        for (AggPickmVO aggVO : aggPickmVOs) {

            if (MMValueCheck.isEmpty(aggVO)) {
                continue;
            }

            PickmHeadVO headVO = aggVO.getParentVO();
            if (MMValueCheck.isEmpty(headVO)) {
                continue;
            }

            PickmItemVO[] itemVOs = (PickmItemVO[]) aggVO.getChildrenVO();
            if (MMValueCheck.isEmpty(itemVOs)) {
                continue;
            }
            Boolean isWW = Boolean.FALSE;
            if (MMValueCheck.isNotEmpty(isWWMap) && MMValueCheck.isNotEmpty(headVO.getVbusitypeid())
                    && isWWMap.containsKey(headVO.getVbusitypeid())) {
                isWW = isWWMap.get(headVO.getVbusitypeid());
            }
            for (PickmItemVO itemVO : itemVOs) {

                if (MMValueCheck.isEmpty(itemVO)) {
                    continue;
                }
                if (isWW && MMValueCheck.isEmpty(itemVO.getCoutstockid())) {
                    failureList.add(new ValidationFailure(PickmLangConsts.getHIT_CKCODENOTNULL1(new String[] {
                        headVO.getVbillcode(), itemVO.getVrowno()
                    })));
                    continue;
                }
                
//                查询部门维护的仓库信息
                String dept=aggVO.getParentVO().getCdeptid();
              	String sql=" select def2  from org_dept where dr=0 and pk_dept='"+dept+"'";
      			IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
      	      String pk_org = itemVO.getPk_org();
//              String pk_org_v = ValueUtils.getString(utils.getBodyValue(iRow, this.materialParam.getPk_org_v(), tablecode));
//              String pk_group = ValueUtils.getString(utils.getBodyValue(iRow, this.materialParam.getPk_group(), tablecode));
              String cmaterialvid =itemVO.getCbmaterialid();
//                      ValueUtils.getString(utils.getBodyValue(iRow, this.materialParam.getCbmaterialvid(), tablecode));
      			try {
      				Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
      				if(map!=null){
      					String def2= map.get("def2")==null?"":map.get("def2").toString() ;
      					if(def2!=null&&!def2.equals("")&&!def2.equals("~")){
//      						dpacParam.setc
      						
      						itemVO.setCoutstockid(def2);
      					}else{
      						String sql2=
      								"SELECT  c.pk_stordoc\n" +
      										"FROM bd_materialwarh C\n" + 
      										"WHERE C.PK_MATERIALSTOCK IN (\n" + 
      										"select    A.PK_MATERIALSTOCK  from bd_materialstock   A where pk_material='"+cmaterialvid+"' " +
      												"and pk_org ='"+pk_org+"' and dr=0\n" + 
      										" )";

      						Map map2 = (Map)iuap.executeQuery(sql2, new MapProcessor());
      						if(map2!=null){
      							String pk_stordoc= map2.get("pk_stordoc")==null?"":map2.get("pk_stordoc").toString() ;
      							itemVO.setCoutstockid(pk_stordoc);
      						}
      							
      							
      					}
      					
      				}else{
      						String sql2=
      								"SELECT  c.pk_stordoc\n" +
      										"FROM bd_materialwarh C\n" + 
      										"WHERE C.PK_MATERIALSTOCK IN (\n" + 
      										"select    A.PK_MATERIALSTOCK  from bd_materialstock   A where pk_material='"+cmaterialvid+"' " +
      												"and pk_org ='"+pk_org+"' and dr=0\n" + 
      										" )";

      						Map map2 = (Map)iuap.executeQuery(sql2, new MapProcessor());
      						if(map2!=null){
      							String pk_stordoc= map2.get("pk_stordoc")==null?"":map2.get("pk_stordoc").toString() ;
      							itemVO.setCoutstockid(pk_stordoc);
      						}
      							
      							
      					}
      				
      			} catch (BusinessException e2) {
      				// TODO Auto-generated catch block
//      				e.printStackTrace();
      				ExceptionUtils.wrappException(e2);
      			}
                
                // 获得出库仓库ID
                String deliveryStockID =
                        MMValueCheck.isEmpty(itemVO.getCoutstockid()) ? new String() : itemVO.getCoutstockid();
                // 获得供应仓库ID
                String suplyStockID =
                        MMValueCheck.isEmpty(itemVO.getCinstockid()) ? new String() : itemVO.getCinstockid();

                // 若两仓库相同，记录出错行号
                if (!MMValueCheck.isEmpty(deliveryStockID) && deliveryStockID.equals(suplyStockID)) {
                    failureList.add(new ValidationFailure(PickmLangConsts.getHIT_SAMESTOCK(new String[] {
                        headVO.getVbillcode(), itemVO.getVrowno()
                    })));
                }
            }

        }

        if (failureList.size() > 0) {
            ExceptionUtils.wrappBusinessException(new ValidationException(failureList).getMessage());
        }
    }

}
