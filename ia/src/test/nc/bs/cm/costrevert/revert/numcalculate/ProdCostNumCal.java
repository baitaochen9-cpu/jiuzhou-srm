package nc.bs.cm.costrevert.revert.numcalculate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.cm.costrevert.VOChangeUtil;
import nc.bs.cm.costrevert.query.ProdCostDBQuery;
import nc.vo.cm.costrevert.AggCtRevertVO;
import nc.vo.cm.costrevert.CtRevertHeadVO;
import nc.vo.cm.costrevert.CtRevertItemVO;
import nc.vo.cm.costrevert.CtRevertParamVO;
import nc.vo.pub.BusinessException;

/**
 * 获取本期完工成本数据
 * <p>
 * 主要用于末级产品或成本分类还原，对于非末级直接从还原表中取就行了[因为非末级都已经还原过了]
 * </p>
 *
 * @author licli
 * @modify syz
 */
public class ProdCostNumCal {

    /**
     * 获取各成本对象的本期完工成本数据，并按成本中心+成本分类+产品进行汇总；
     *
     * @param paramVO
     *            CtRevertParamVO 参数vo
     * @return 本期完工成本数据,key=成本中心+成本分类+产品,value=AggCtRevertVO
     * @throws BusinessException
     */
    public static Map<String, AggCtRevertVO> calNum(CtRevertParamVO paramVO, List<String> costClassOrMats)
            throws BusinessException {
        // if (CMCollectionUtil.isEmpty(paramVO.getRelation().keySet())) {
        // return new HashMap<String, AggCtRevertVO>();
        // }
        Map<String, AggCtRevertVO> prodCostMap = ProdCostNumCal.getProdCostVORelation(paramVO, costClassOrMats);
        VOChangeUtil changeUtil = new VOChangeUtil();
        return changeUtil.getRevertMap4ChangMap(prodCostMap, paramVO);
    }

    /**
     * 根据产品，成本分类，得到完工成本数据及关系
     *
     * @param paramVO
     *            参数vo
     * @return K= 完工成本id V = 完工成本聚合vo
     * @throws BusinessException
     *             BusinessException
     */
    private static Map<String, AggCtRevertVO> getProdCostVORelation(CtRevertParamVO paramVO,
            List<String> costClassOrMats) throws BusinessException {
        Map<String, AggCtRevertVO> aggProdCostMap = new HashMap<String, AggCtRevertVO>();
        ProdCostDBQuery db = new ProdCostDBQuery();
        Map<String, CtRevertHeadVO> headMap = db.getProdCostHeadVO(paramVO, costClassOrMats);
        Map<String, List<CtRevertItemVO>> itemMap = db.getProdCostItemVO(paramVO, costClassOrMats);
        // 3 组合聚合vo
        for (Entry<String, CtRevertHeadVO> enter : headMap.entrySet()) {
            // if (itemMap.containsKey(enter.getKey())) {// 只有表头没有表体的数据，代表副产品的完工成本
            AggCtRevertVO aggvo = new AggCtRevertVO();
            aggvo.setParentVO(enter.getValue());
            if (itemMap.get(enter.getKey()) != null) {
                aggvo.setChildrenVO(itemMap.get(enter.getKey()).toArray(new CtRevertItemVO[0]));
            }
            else {
                aggvo.setChildrenVO(new CtRevertItemVO[0]);
            }
            aggProdCostMap.put(enter.getKey(), aggvo);
            // }
        }
        return aggProdCostMap;
    }
}
