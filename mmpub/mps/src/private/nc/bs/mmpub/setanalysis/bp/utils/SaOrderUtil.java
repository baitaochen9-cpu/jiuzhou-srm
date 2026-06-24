package nc.bs.mmpub.setanalysis.bp.utils;

import nc.bs.mmpub.dpub.consts.SqlKeyConstants;
import nc.vo.mmpub.setanalysis.entity.SaDemandVO;
import nc.vo.mmpub.setanalysis.entity.SaMatchVO;
import nc.vo.mmpub.setanalysis.entity.SaMaterialVO;
import nc.vo.mmpub.setanalysis.entity.SaSupplyVO;

/**
 * 排序公共类
 * 
 * @since 6.3
 * @version 2012-8-8 下午04:51:17
 * @author zhaohyc
 */
public class SaOrderUtil {
    public final static String ASC = " asc ";

    public final static String DESC = " desc ";

    public final static String ORDERBY = " order by ";

    /**
     * 取得供给排序SQL
     * 
     * @return
     */
    public static String getSupplyDetailOrder() {
        StringBuilder sql = new StringBuilder();
        sql.append(SaOrderUtil.ORDERBY);
        sql.append(SaSupplyVO.PK_SUPPLYORG).append(SaOrderUtil.ASC).append(",");
        sql.append(SaSupplyVO.FSUPPLYTYPE).append(SaOrderUtil.ASC).append(",");
        sql.append(SaSupplyVO.CMATERIALID).append(SaOrderUtil.ASC).append(",");
        sql.append(SaSupplyVO.CMATERIALVID).append(SaOrderUtil.ASC);
        return sql.toString();
    }

    /**
     * 需求表的排序OrderBy 不支持别名 顺序（升）创建日期（降）需求日期（升）未匹配数量（降）库存组织（升）
     */
    public static String getDemandOrder() {
        StringBuilder sb = new StringBuilder();
        sb.append(SaDemandVO.SORTCODE).append(SqlKeyConstants.ASC).append(",");
        sb.append(SaDemandVO.CREATIONTIME).append(SqlKeyConstants.DESC).append(",");
        sb.append(SaDemandVO.DEMANDTIME).append(SqlKeyConstants.ASC).append(",");
        sb.append(SaDemandVO.NREMAINNUM).append(SqlKeyConstants.DESC).append(",");
        sb.append(SaDemandVO.PK_DEMANDORG).append(SqlKeyConstants.ASC);
        return sb.toString();

    }

    /**
     * 供应表的排序 分类标示（升）供给日期（升）供给类型（升）数量（降）库存组织（升）
     * 
     * @return
     */
    public static String getSupplyOrder() {
        StringBuilder sb = new StringBuilder();
        sb.append(SaSupplyVO.CGROUPKEY).append(SqlKeyConstants.ASC).append(",");
        sb.append(SaSupplyVO.DSUPPLYDATE).append(SqlKeyConstants.ASC).append(",");
        //liyf 中山瑞华项目,增加按照库存状态,首次入库日期的排序
        sb.append("vdef1,SUBSTR(vdef2,0,10) asc, SUBSTR(vdef3,0,10) asc ,");
        sb.append(SaSupplyVO.FSUPPLYTYPE).append(SqlKeyConstants.ASC).append(",");
        sb.append(SaSupplyVO.NREMAINNUM).append(SqlKeyConstants.DESC).append(",");
        sb.append(SaSupplyVO.VFIRSTBID).append(SqlKeyConstants.ASC).append(",");
        sb.append(SaSupplyVO.PK_SUPPLY).append(SqlKeyConstants.ASC);// 固定供给顺序
        return sb.toString();
    }

    /**
     * 匹配明细排序
     * 
     * @return
     */
    public static String getMatchOrder() {
        StringBuilder sb = new StringBuilder();
        sb.append(SaMatchVO.ANALYSISORDER).append(SqlKeyConstants.ASC);
        return sb.toString();
    }

    /**
     * 物料排序
     * 
     * @return
     */
    public static String getMaterialOrder() {
        StringBuilder sb = new StringBuilder();
        sb.append(SaMaterialVO.FMATERIALTYPE).append(SqlKeyConstants.ASC).append(",");
        sb.append(SaMaterialVO.CMATERIALID).append(SqlKeyConstants.ASC);
        return sb.toString();
    }
}
