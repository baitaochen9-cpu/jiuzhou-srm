package nc.pubimpl.ic.onhand.mmpps.mpm;

import nc.pubitf.ic.onhand.mmpps.mpm.IQryOnhandMapForCalc;
import nc.pubitf.ic.onhand.mmpps.mpm.OnhandCalcSupplyMapVO;
import nc.pubitf.mmpub.sdmanage.ISupplyResult;
import nc.vo.bd.stordoc.StordocVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
import nc.vo.ic.pub.sql.SqlUtil;
import nc.vo.ic.storestate.EInvUsability;
import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 * IC-运算供给查询存量字段
 * 
 * @since 6.0
 * @version 2012-11-5 下午01:51:43
 * @author guofj
 * 
 * @modify zhaofeid 2013-08-16
 */

public class QryOnhandMapForCalcImpl implements IQryOnhandMapForCalc {

  @Override
  public OnhandCalcSupplyMapVO getCalcSupplyMapVO() {
    OnhandCalcSupplyMapVO mapvo = new OnhandCalcSupplyMapVO();
    mapvo.setFrom(this.getFromPart());
    mapvo.setWhere(this.getWherePart());
    return mapvo;
  }

  @Override
  public ISupplyResult getSupplyResult() {
    return this.getCalcSupplyMapVO();   
  }

  /**
   * 获得查询的from部分，包括连接
   * 
   * @param tempTable
   * @return
   */
  private String getFromPart() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(this.getMainFromPart());
    sql.append(this.getStordocInner());
    sql.append(this.getStoreStateOuter());
    
    sql.append(this.getBatchcodeOuter());
    return sql.toString();
  }

  /**
   * yezhian 23/09/05
   * 增加批次号关联
   * @return
   */
  private String getBatchcodeOuter() {
	  SqlBuilder sql = new SqlBuilder();
	  sql.append( " left join scm_batchcode  scm_batchcode ");
	  sql.append(" on  scm_batchcode.pk_batchcode = ");
	  sql.append(OnhandDimVO.TABLENAME + ".");
	  sql.append(OnhandDimVO.PK_BATCHCODE );
	return sql.toString() ;
}

/**
   * 现存量维度+现存量查询关联
   * 
   * @return
   */
  private String getMainFromPart() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(OnhandDimVO.TABLENAME);
    sql.append(" ");
    sql.append(OnhandDimVO.TABLENAME);
    sql.append(" inner join ");
    sql.append(OnhandNumVO.TABLENAME);
    sql.append(" ");
    sql.append(OnhandNumVO.TABLENAME);
    sql.append(" on ");
    sql.append(OnhandDimVO.TABLENAME + ".");
    sql.append(OnhandDimVO.PK_ONHANDDIM);
    sql.append("=" + OnhandNumVO.TABLENAME + ".");
    sql.append(OnhandNumVO.PK_ONHANDDIM);
    return sql.toString();
  }

  /**
   * 与仓库的inner连接
   * 
   * @param tempTable
   * @return
   */
  private String getStordocInner() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" inner join ");
    sql.append(StordocVO.getDefaultTableName());
    sql.append(" ");
    sql.append(StordocVO.getDefaultTableName());
    sql.append(" on ");
    sql.append(OnhandDimVO.TABLENAME + "." + OnhandDimVO.CWAREHOUSEID);
    sql.append("=");
    sql.append(StordocVO.getDefaultTableName());
    sql.append(".");
    sql.append(StordocVO.PK_STORDOC);
    return sql.toString();
  }

  /**
   * 与库存状态的outer连接
   * 
   * @param tempTable
   * @return
   */
  private String getStoreStateOuter() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" left outer join ic_storestate ic_storestate ");
    sql.append(" on ");
    sql.append(OnhandDimVO.TABLENAME + "." + OnhandDimVO.CSTATEID);
    sql.append("=");
    sql.append("ic_storestate.");
    sql.append(StoreStateVO.PK_STORESTATE);
    sql.append(" and ");
    sql.append(" ic_storestate.dr=0 ");
    return sql.toString();
  }

  private String getWherePart() {
    SqlBuilder sql = new SqlBuilder();
    // 结存数量-预留主数量大于零
    sql.append(" (");
    sql.append(SqlUtil.getFun_COALESCE(OnhandNumVO.TABLENAME + "."
        + OnhandNumVO.NONHANDNUM, "0.0"));
    sql.append(" > 0) ");
    // 库存状态为空或可用
    sql.append(" and (");
    sql.append(SqlUtil.getFun_ISNULL_For_String(OnhandDimVO.TABLENAME,
        OnhandDimVO.CSTATEID));
    sql.append(" or ic_storestate.");
    sql.append(StoreStateVO.IUSABILITY);
    sql.append("='");
    sql.append(EInvUsability.USABLE.value());
    sql.append("')");

    // 仓库 计划可用
    sql.append(" and ");
    sql.append(StordocVO.getDefaultTableName());
    sql.append(".");
    sql.append(StordocVO.MRPFLAG);
    sql.append("=");
    sql.append("'Y'");

    // 库存组织范围--组织过滤不需要了，调用方自己拼
    // sql.append(SqlIn.formInSQL(OnhandDimVO.TABLENAME + "." +
    // OnhandDimVO.PK_ORG, pk_org));

    return sql.toString();
  }
}
