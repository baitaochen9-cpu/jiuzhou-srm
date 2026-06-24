package nc.bs.mmpub.setanalysis.bp.supply;

import nc.util.mmpub.dpub.db.SqlBuilder;
import nc.vo.mmpub.setanalysis.entity.SaSupplyVO;

/**
 * 获取插入语句
 * 
 * @since 6.3
 * @version 2012-8-11 下午12:52:55
 * @author zhr
 */
public class SaSupplyUtil {
    /**
     * 制造领域供给获取范围
     * 
     * @return
     */
    // zhaolik修改bug NCdp204525293
    public static String getMMSupplyInsertSql() {
        SqlBuilder sql = new SqlBuilder();
        sql.insertinto(SaSupplyVO.TABLENAME);
        sql.l();
        sql.append(SaSupplyVO.PK_SUPPLY + ",");// 1 主键
        sql.append(SaSupplyVO.PK_BR + ",");// 2 物料范围主键
        sql.append(SaSupplyVO.PK_GROUP + ",");// 3 集团
        sql.append(SaSupplyVO.PK_ORG + ",");// 4 组织(供给单据的原始主组织)
        sql.append(SaSupplyVO.PK_ORG_V + ",");// 5 组织版本
        sql.append(SaSupplyVO.FSUPPLYTYPE + ",");// 6 供给类型
        sql.append(SaSupplyVO.VSUPPLYID + ",");// 7 供给表头主键
        sql.append(SaSupplyVO.VSUPPLYBID + ",");// 8 供给表体主键
        sql.append(SaSupplyVO.VSUPPLYROWNO + ",");// 9 供给单据行号
        sql.append(SaSupplyVO.PK_SUPPLYORG + ",");// 10 供给组织(生产领用委托关系的对应的组织或自身)
        sql.append(SaSupplyVO.PK_SUPPLYORG_V + ",");// 11 供给组织版本
        sql.append(SaSupplyVO.CMATERIALID + ",");// 12 物料主键
        sql.append(SaSupplyVO.CMATERIALVID + ",");// 13 物料版本主键
        sql.append(SaSupplyVO.VSUPPLYTYPECODE + ",");// 14 供给单据类型
        sql.append(SaSupplyVO.VSUPPLYCODE + ",");// 15 供给单据号
        sql.append(SaSupplyVO.DSUPPLYDATE + ",");// 16 供给日期
        sql.append(SaSupplyVO.DSUPPLYDBEFOREADPTER + ",");// 1601 供给日期
        sql.append(SaSupplyVO.NNUM + ",");// 17 供给数量(原始单据供给数量刨除预留)
        sql.append(SaSupplyVO.NRESERVATIONNUM + ",");// 18 预留数量
        sql.append(SaSupplyVO.NREMAINNUM + ",");// 1801可供给数量(每次被匹配都要减少,剩余的供给)
        sql.append(SaSupplyVO.CVENDORID + ",");// 19 供应商
        sql.append(SaSupplyVO.CPRODUCTORID + ",");// 20 生产厂商
        sql.append(SaSupplyVO.CPROJECTID + ",");// 21 项目
        sql.append(SaSupplyVO.CCUSTOMERID + ",");// 22 客户
        sql.append(SaSupplyVO.CFFILEID + ",");// 2201 特征码
        sql.append(SaSupplyVO.VFREE1 + ",");// 23 自由项1
        sql.append(SaSupplyVO.VFREE2 + ",");// 自由项2
        sql.append(SaSupplyVO.VFREE3 + ",");// 自由项3
        sql.append(SaSupplyVO.VFREE4 + ",");// 自由项4
        sql.append(SaSupplyVO.VFREE5 + ",");// 自由项5
        sql.append(SaSupplyVO.VFREE6 + ",");// 自由项6
        sql.append(SaSupplyVO.VFREE7 + ",");// 自由项7
        sql.append(SaSupplyVO.VFREE8 + ",");// 自由项8
        sql.append(SaSupplyVO.VFREE9 + ",");// 自由项9
        sql.append(SaSupplyVO.VFREE10 + ",");// 自由项10
        sql.append(SaSupplyVO.CGROUPKEY + ",");// 25 分组key
        sql.append(SaSupplyVO.LOWLEVELCODE + ",");// 26 物料低阶码
        sql.append(SaSupplyVO.DR + ",");// 27 DR
        sql.append(SaSupplyVO.CREATOR + ",");// 28 创建人
        sql.append(SaSupplyVO.CREATIONTIME + ",");// 29 创建时间
        sql.append(SaSupplyVO.COMPUTECODE + ",");// 30 分析号
        sql.append(SaSupplyVO.CBOMVERSIONID + ",");// 31 生产BOM版本ID
        sql.append(SaSupplyVO.CPACKBOMID + ",");// 3101 包装BOM版本ID
        sql.append(SaSupplyVO.NNOTIMPLEMENTDNUM + ",");// 32未执行量(未刨除预留的未执行量)
        sql.append(SaSupplyVO.NIMPLEMENTDNUM + ",");// 3201执行量(制造单据=计划产出数量-未执行量,ESLE 单据数量-未执行量)
        sql.append(SaSupplyVO.NBILLNUM + ",");// 33 单据数量
        sql.append(SaSupplyVO.FBILLSTATUS + ",");// 34 单据状态
        sql.append(SaSupplyVO.CMEASUREID + ",");// 35 主单位
        sql.append(SaSupplyVO.VFIRSTBID + ",");// 36 源头单据子表ID
        sql.append(SaSupplyVO.VFIRSTCODE + ",");// 37 源头单据号
        sql.append(SaSupplyVO.VFIRSTID + ",");// 38 源头单据ID
        sql.append(SaSupplyVO.VFIRSTROWNO + ",");// 39 源头单据行号
        sql.append(SaSupplyVO.VFIRSTTYPE + ",");// 40 源头单据类型
        sql.append(SaSupplyVO.VSRCBID + ",");// 41 来源单据子表ID
        sql.append(SaSupplyVO.VSRCCODE + ",");// 42 来源单据号
        sql.append(SaSupplyVO.VSRCID + ",");// 43 来源单据ID
        sql.append(SaSupplyVO.VSRCROWNO + ",");// 44 来源单据行号
        sql.append(SaSupplyVO.VSRCTYPE + ",");// 45 来源单据类型
        sql.append(SaSupplyVO.WASTERRATE + ",");// 46 废品系数
        sql.append(SaSupplyVO.DREWINDTIME + ",");// 4701 收料/完工日期
        sql.append(SaSupplyVO.DBILLDATE + ",");// 47 下单/开工日期
        sql.append(SaSupplyVO.PK_PRODUCTFACOTRY + ",");// 4801 生产工厂
        sql.append(SaSupplyVO.CCUSTMATERIALID);// 48 客户物料码
        sql.r();
        return sql.toString();
    }

    /**
     * 供应链领域供给获取范围
     * 
     * @return
     */
    public static String getSCMSupplyInsertSql() {
        SqlBuilder sql = new SqlBuilder();
        sql.insertinto(SaSupplyVO.TABLENAME);
        sql.l();
        sql.append(SaSupplyVO.PK_SUPPLY + ",");// 1 主键
        sql.append(SaSupplyVO.PK_BR + ",");// 2 物料范围主键
        sql.append(SaSupplyVO.PK_GROUP + ",");// 3 集团
        sql.append(SaSupplyVO.PK_ORG + ",");// 4 组织(供给单据的原始主组织)
        sql.append(SaSupplyVO.PK_ORG_V + ",");// 5 组织版本
        sql.append(SaSupplyVO.FSUPPLYTYPE + ",");// 6 供给类型
        sql.append(SaSupplyVO.VSUPPLYID + ",");// 7 供给表头主键
        sql.append(SaSupplyVO.VSUPPLYBID + ",");// 8 供给表体主键
        sql.append(SaSupplyVO.VSUPPLYROWNO + ",");// 9 供给单据行号
        sql.append(SaSupplyVO.PK_SUPPLYORG + ",");// 10 供给组织(生产领用委托关系的对应的组织或自身)
        sql.append(SaSupplyVO.PK_SUPPLYORG_V + ",");// 11 供给组织版本
        sql.append(SaSupplyVO.CMATERIALID + ",");// 12 物料主键
        sql.append(SaSupplyVO.CMATERIALVID + ",");// 13 物料版本主键
        sql.append(SaSupplyVO.VSUPPLYTYPECODE + ",");// 14 供给单据类型
        sql.append(SaSupplyVO.VSUPPLYCODE + ",");// 15 供给单据号
        sql.append(SaSupplyVO.DSUPPLYDATE + ",");// 16 供给日期
        sql.append(SaSupplyVO.DSUPPLYDBEFOREADPTER + ",");// 1601 供给日期
        sql.append(SaSupplyVO.NNUM + ",");// 17 供给数量(原始单据供给数量刨除预留)
        sql.append(SaSupplyVO.NRESERVATIONNUM + ",");// 18 预留数量
        sql.append(SaSupplyVO.NREMAINNUM + ",");// 1801可供给数量(每次被匹配都要减少,剩余的供给)
        sql.append(SaSupplyVO.CVENDORID + ",");// 19 供应商
        sql.append(SaSupplyVO.CPRODUCTORID + ",");// 20 生产厂商
        sql.append(SaSupplyVO.CPROJECTID + ",");// 21 项目
        sql.append(SaSupplyVO.CCUSTOMERID + ",");// 22 客户
        sql.append(SaSupplyVO.CFFILEID + ",");// 2201特征码
        sql.append(SaSupplyVO.VFREE1 + ",");// 23 自由项1
        sql.append(SaSupplyVO.VFREE2 + ",");// 自由项2
        sql.append(SaSupplyVO.VFREE3 + ",");// 自由项3
        sql.append(SaSupplyVO.VFREE4 + ",");// 自由项4
        sql.append(SaSupplyVO.VFREE5 + ",");// 自由项5
        sql.append(SaSupplyVO.VFREE6 + ",");// 自由项6
        sql.append(SaSupplyVO.VFREE7 + ",");// 自由项7
        sql.append(SaSupplyVO.VFREE8 + ",");// 自由项8
        sql.append(SaSupplyVO.VFREE9 + ",");// 自由项9
        sql.append(SaSupplyVO.VFREE10 + ",");// 自由项10
        sql.append(SaSupplyVO.CGROUPKEY + ",");// 25 分组key
        sql.append(SaSupplyVO.LOWLEVELCODE + ",");// 26 物料低阶码
        sql.append(SaSupplyVO.DR + ",");// 27 DR
        sql.append(SaSupplyVO.CREATOR + ",");// 28 创建人
        sql.append(SaSupplyVO.CREATIONTIME + ",");// 29 创建时间
        sql.append(SaSupplyVO.COMPUTECODE + ",");// 30 分析号
        sql.append(SaSupplyVO.NNOTIMPLEMENTDNUM + ",");// 32未执行量(未刨除预留的未执行量)
        sql.append(SaSupplyVO.NIMPLEMENTDNUM + ",");// 3201执行量(制造单据=计划产出数量-未执行量,ESLE 单据数量-未执行量)
        sql.append(SaSupplyVO.NBILLNUM + ",");// 33 单据数量
        sql.append(SaSupplyVO.FBILLSTATUS + ",");// 34 单据状态
        sql.append(SaSupplyVO.CMEASUREID + ",");// 35 主单位
        sql.append(SaSupplyVO.WASTERRATE + ",");// 351 废品系数
        sql.append(SaSupplyVO.VFIRSTBID + ",");// 36 源头单据子表ID
        sql.append(SaSupplyVO.VFIRSTCODE + ",");// 37 源头单据号
        sql.append(SaSupplyVO.VFIRSTID + ",");// 38 源头单据ID
        sql.append(SaSupplyVO.VFIRSTROWNO + ",");// 39 源头单据行号
        sql.append(SaSupplyVO.VFIRSTTYPE + ",");// 40 源头单据类型
        sql.append(SaSupplyVO.VSRCBID + ",");// 41 来源单据子表ID
        sql.append(SaSupplyVO.VSRCCODE + ",");// 42 来源单据号
        sql.append(SaSupplyVO.VSRCID + ",");// 43 来源单据ID
        sql.append(SaSupplyVO.VSRCROWNO + ",");// 44 来源单据行号
        sql.append(SaSupplyVO.VSRCTYPE);// 45 来源单据类型
        sql.r();
        return sql.toString();
    }

    /**
     * 库存组织供给获取范围
     * 
     * @return
     */
    public static String getOHSupplyInsertSql() {
        SqlBuilder sql = new SqlBuilder();
        sql.insertinto(SaSupplyVO.TABLENAME);
        sql.l();
        sql.append(SaSupplyVO.PK_SUPPLY + ",");// 1 主键
        sql.append(SaSupplyVO.PK_BR + ",");// 2 物料范围主键
        sql.append(SaSupplyVO.PK_GROUP + ",");// 3 集团
        sql.append(SaSupplyVO.PK_ORG + ",");// 4 组织(供给单据的原始主组织)
        sql.append(SaSupplyVO.PK_ORG_V + ",");// 5 组织版本
        sql.append(SaSupplyVO.FSUPPLYTYPE + ",");// 6 供给类型
        sql.append(SaSupplyVO.VSUPPLYID + ",");// 7 供给表头主键
        sql.append(SaSupplyVO.VSUPPLYBID + ",");// 8 供给表体主键
        sql.append(SaSupplyVO.VSUPPLYROWNO + ",");// 9 供给单据行号
        sql.append(SaSupplyVO.PK_SUPPLYORG + ",");// 10 供给组织(生产领用委托关系的对应的组织或自身)
        sql.append(SaSupplyVO.PK_SUPPLYORG_V + ",");// 11 供给组织版本
        sql.append(SaSupplyVO.CMATERIALID + ",");// 12 物料主键
        sql.append(SaSupplyVO.CMATERIALVID + ",");// 13 物料版本主键
        sql.append(SaSupplyVO.VSUPPLYTYPECODE + ",");// 14 供给单据类型
        sql.append(SaSupplyVO.VSUPPLYCODE + ",");// 15 供给单据号
        sql.append(SaSupplyVO.DSUPPLYDATE + ",");// 16 供给日期
        sql.append(SaSupplyVO.DSUPPLYDBEFOREADPTER + ",");// 1601 供给日期
        sql.append(SaSupplyVO.NNUM + ",");// 17 供给数量(原始单据供给数量刨除预留)
        sql.append(SaSupplyVO.NRESERVATIONNUM + ",");// 18 预留数量
        sql.append(SaSupplyVO.NREMAINNUM + ",");// 1801可供给数量(每次被匹配都要减少,剩余的供给)
        sql.append(SaSupplyVO.CVENDORID + ",");// 19 供应商
        sql.append(SaSupplyVO.CPRODUCTORID + ",");// 20 生产厂商
        sql.append(SaSupplyVO.CPROJECTID + ",");// 21 项目
        sql.append(SaSupplyVO.CCUSTOMERID + ",");// 22 客户
        sql.append(SaSupplyVO.CFFILEID + ",");// 2201 特征码
        sql.append(SaSupplyVO.VFREE1 + ",");// 23 自由项1
        sql.append(SaSupplyVO.VFREE2 + ",");// 自由项2
        sql.append(SaSupplyVO.VFREE3 + ",");// 自由项3
        sql.append(SaSupplyVO.VFREE4 + ",");// 自由项4
        sql.append(SaSupplyVO.VFREE5 + ",");// 自由项5
        sql.append(SaSupplyVO.VFREE6 + ",");// 自由项6
        sql.append(SaSupplyVO.VFREE7 + ",");// 自由项7
        sql.append(SaSupplyVO.VFREE8 + ",");// 自由项8
        sql.append(SaSupplyVO.VFREE9 + ",");// 自由项9
        sql.append(SaSupplyVO.VFREE10 + ",");// 自由项10
        sql.append(SaSupplyVO.VSTORDOC + ",");// 仓库 24
        sql.append(SaSupplyVO.CGROUPKEY + ",");// 25 分组key
        sql.append(SaSupplyVO.LOWLEVELCODE + ",");// 26 物料低阶码
        sql.append(SaSupplyVO.DR + ",");// 27 DR
        sql.append(SaSupplyVO.CREATOR + ",");// 28 创建人
        sql.append(SaSupplyVO.CREATIONTIME + ",");// 29 创建时间
        sql.append(SaSupplyVO.COMPUTECODE + ",");// 30 分析号
        sql.append(SaSupplyVO.NNOTIMPLEMENTDNUM + ",");// 32未执行量(未刨除预留的未执行量)
        sql.append(SaSupplyVO.NIMPLEMENTDNUM + ",");// 3201执行量(制造单据=计划产出数量-未执行量,ESLE 单据数量-未执行量)
        sql.append(SaSupplyVO.NBILLNUM + ",");// 33 单据数量
        sql.append(SaSupplyVO.FBILLSTATUS + ",");// 34 单据状态
        sql.append(SaSupplyVO.CMEASUREID + ",");// 35 主单位
        sql.append(SaSupplyVO.WASTERRATE + ",");// 351 废品系数
        sql.append(SaSupplyVO.VFIRSTBID + ",");// 36 源头单据子表ID
        sql.append(SaSupplyVO.VFIRSTCODE + ",");// 37 源头单据号
        sql.append(SaSupplyVO.VFIRSTID + ",");// 38 源头单据ID
        sql.append(SaSupplyVO.VFIRSTROWNO + ",");// 39 源头单据行号
        sql.append(SaSupplyVO.VFIRSTTYPE + ",");// 40 源头单据类型
        sql.append(SaSupplyVO.VSRCBID + ",");// 41 来源单据子表ID
        sql.append(SaSupplyVO.VSRCCODE + ",");// 42 来源单据号
        sql.append(SaSupplyVO.VSRCID + ",");// 43 来源单据ID
        sql.append(SaSupplyVO.VSRCROWNO + ",");// 44 来源单据行号
        sql.append(SaSupplyVO.VSRCTYPE+ ",");// 45 来源单据类型
        
        
        //liyf 202312-18 库存状态,首次入库日期和失效日期
        sql.append(SaSupplyVO.VDEF1+ ",");// 库存状态
        sql.append(SaSupplyVO.VDEF2+ ",");// 首次入库日期
        sql.append(SaSupplyVO.VDEF3);// 失效日期

        //liyf 202312-18 库存状态,首次入库日期和失效日期
        sql.r();
        return sql.toString();
    }

    /**
     * 取得其他供给SQL
     * 
     * @param materialids
     * @param materialKey
     * @param pk_key
     * @return
     */
    public static String getOtherWhereSql(String[] materialids, String materialKey, String pk_key, String computeCode) {
        if (null == materialids || materialids.length == 0) {
            return "";
        }
        SqlBuilder sql = new SqlBuilder();
        String alieName = "T10";
        sql.and();
        sql.append(materialKey, materialids);
        sql.and();
        sql.append(pk_key);
        sql.notin();
        sql.l();
        sql.select();
        sql.append(alieName + "." + SaSupplyVO.PK_SUPPLY);
        sql.from(SaSupplyVO.TABLENAME);
        sql.append(" " + alieName);
        sql.where();
        sql.append(alieName + "." + SaSupplyVO.DR + "=0 ");
        sql.and();
        sql.append(alieName + "." + SaSupplyVO.COMPUTECODE + "='" + computeCode + "'");
        sql.r();
        return sql.toString();
    }

    /**
     * 取得其他供给SQL
     * add by malid
     * 
     * @param materialids
     * @param materialKey
     * @param pk_key
     * @return
     */
    public static String getOtherWhereSqlForOnHand(String pk_key, String computeCode) {
        SqlBuilder sql = new SqlBuilder();
        String alieName = "T10";
        sql.append("having  max( " + pk_key + ")");// modefied by malid NCdp204652430
        sql.notin();
        sql.l();
        sql.select();
        sql.append(alieName + "." + SaSupplyVO.PK_SUPPLY);
        sql.from(SaSupplyVO.TABLENAME);
        sql.append(" " + alieName);
        sql.where();
        sql.append(alieName + "." + SaSupplyVO.DR + "=0 ");
        sql.and();
        sql.append(alieName + "." + SaSupplyVO.COMPUTECODE + "='" + computeCode + "'");
        sql.r();
        return sql.toString();
    }

    /**
     * @param wrnommexenum 完工未执行
     * @param reservationnum 预留
     * @param planInputrmnum 计划投入未执行数量
     * @param wasteRate 废品率
     * @return
     */
    public static String getSupplyNum(String wrnommexenum, String reservationnum, String planInputrmnum,
            String wasteRate) {

        SqlBuilder sql = new SqlBuilder();
        sql.append("(");
        sql.caseWhen();
        sql.append(wrnommexenum + " <" + "  isnull(" + reservationnum + ",0) ");// 完工未执行-预留<0
        sql.then();
        sql.append("(" + planInputrmnum + ")*(1-isnull(" + wasteRate + ",0))" + " - isnull(" + reservationnum
                + ",0) + " + wrnommexenum);// 供给数量=(计划投入-完工数量)*(1-废品率)-（预留-完工未执行）
        sql.append(" else ");
        sql.append("(" + planInputrmnum + ")*(1-isnull(" + wasteRate + ",0))");// 供给数量=计划投入-完工数量
        sql.append(" end " + ")");

        return sql.toString();
    }
}
