package nc.bs.mmpac.wr.rule.prodin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.util.mmf.busi.service.MaterialPubService;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrQualityVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.MapList;

public class WrProdInSetWarehouseRule implements IRule<AggWrVO> {

    @Override
    public void process(AggWrVO[] vos) {

        // 判空
        if (MMValueCheck.isEmpty(vos)) {
            return;
        }

        // 查询主仓库
        Map<String, MaterialStockVO> stockMap = this.queryStockMap(vos);

        if (MMValueCheck.isEmpty(stockMap)) {
            return;
        }

        // 设置主仓库
        for (AggWrVO aggVO : vos) {

            for (WrItemVO wrItemVO : (WrItemVO[]) aggVO.getChildren(WrItemVO.class)) {

                for (WrQualityVO wrQualityVO : wrItemVO.getQualityvos()) {

                    MaterialStockVO materialStockVO = stockMap.get(wrQualityVO.getCgmaterialvid());

                    if (MMValueCheck.isEmpty(materialStockVO) || MMValueCheck.isEmpty(materialStockVO.getPk_stordoc())) {
                        continue;
                    }

                    wrQualityVO.setCgwarehouseid(materialStockVO.getPk_stordoc());
                    
                    
                    
                        
                    	///* 25 */       setter.setBodyValue(null, e.getRow(), "cgmposid");
                    					String cgwarehouseid=wrQualityVO.getCgwarehouseid();
                    					String pk_material=wrQualityVO.getCgmaterialid();
                    					String pk_org=wrQualityVO.getPk_org();
                    					String sql=" select  def1   from bd_materialwarh where pk_stordoc ='"+cgwarehouseid+"' and pk_materialstock in (" +
                    							"select   pk_materialstock  from bd_materialstock where pk_material='"+pk_material+"' and pk_org ='"+pk_org+"')";
                    					IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                    			    	
                    	    			
										try {
											Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
											if(map!=null){
	                    	    				String def1= map.get("def1")==null?"":map.get("def1").toString() ;
	                    	    				wrQualityVO.setCgmposid(def1);
	                    	    			}
											
	                    					
//											e.getBillCardPanel().setBodyValueAt(def1, e.getRow(), "cgmposid");
										} catch (BusinessException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
                    	    			
                    
                }
            }
        }
    }

    /**
     * 查询物料库存信息的默认主仓库
     * 
     * @param vos
     * @return
     */
    private Map<String, MaterialStockVO> queryStockMap(AggWrVO[] vos) {

        MapList<String, String> srockMaterialvidMapList = new MapList<String, String>();

        for (AggWrVO aggVO : vos) {

            for (WrItemVO wrItemVO : (WrItemVO[]) aggVO.getChildren(WrItemVO.class)) {

                for (WrQualityVO wrQualityVO : wrItemVO.getQualityvos()) {

                    srockMaterialvidMapList.put(wrQualityVO.getCgdepositorgid(), wrQualityVO.getCgmaterialvid());
                }
            }
        }

        Map<String, List<String>> stockMaterialvidMap = srockMaterialvidMapList.toMap();
        Map<String, MaterialStockVO> resultMap = new HashMap<String, MaterialStockVO>();
        Map<String, MaterialStockVO> stockMap = null;

        String[] materialStockVOFields = {
            MaterialStockVO.PK_STORDOC
        };

        for (String pk_org : stockMaterialvidMap.keySet()) {

            List<String> materialvidList = stockMaterialvidMap.get(pk_org);

            stockMap =
                    MaterialPubService.queryMaterialStockInfoByPks(materialvidList.toArray(new String[0]), pk_org,
                            materialStockVOFields);

            if (!MMValueCheck.isEmpty(stockMap)) {
                resultMap.putAll(stockMap);
            }
        }

        return resultMap;
    }
}
