package nc.bs.cm.allocate.execute.dirver.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMMapUtil;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.util.CostCenterObjectTempTableUtil;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.pub.MapSet;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 库存材料出库单所有主单位是KG的材料数量之和
 */
public class KGIcMaterilOutHandler {
	
	
	
	

    /**
     * 获取指定材料子项实际消耗数量动因值
     * 
     * @param pkGroup
     *            集团
     * @param pkOrg
     *            工厂
     * @param period
     *            会计期间
     * @param preperiod
     *            上一期间\期初期间
     * @param centerobjectMapSet
     *            MapSet<成本中心ID，成本对象ID>
     * @param materialvid
     *            指定材料vID
     * @return Map<成本中心ID+成本对象ID, 指定材料子项实际消耗数量>
     * @throws BusinessException
     */
    public Map<String, UFDouble> aquireAssignStuffActualNumber(String pkGroup, String pkOrg, String period,
            String preperiod, MapSet<String, String> centerobjectMapSet, String materialvid) throws BusinessException {
        
        if (null == centerobjectMapSet || centerobjectMapSet.size() == 0) {
            return null;
        }
        // 
//        UFDate[] beginEndDate = BDAdapter.getBeginAndEndDateByPeriod(pkOrg, period);
        Map<String, String> costRegion = BDAdapter.queryFinanceLayerCostRegion(pkOrg);
        if(costRegion == null){
        	return null;
        }
        String pk_cost = costRegion.get(pkOrg);
        
        // 插入条件临时表
        String tempTableName = new CostCenterObjectTempTableUtil().getTempTable(centerobjectMapSet).getRealTableName();
        SqlBuilder sql = new SqlBuilder();
        sql.append(" select d.ccostcenterid ,cm_costobject.ccostobjectid, ");
        sql.append(" sum(d.nnum) as nnum ");
        sql.append(" from ia_detailledger d inner join ia_i6bill_b c on d.cbill_bid = c.cbill_bid ");
        sql.append(" inner join cm_costobject on d.ccostobjid  =cm_costobject.cmaterialid   ");//主产品成本对象
        
        sql.append(" inner join "+tempTableName);
        sql.append(" on "+tempTableName+"."+CostCenterObjectTempTableUtil.CCOSTCENTERID +"=d.ccostcenterid");
        sql.append(" and "+tempTableName+"."+CostCenterObjectTempTableUtil.CCOSTOBJECTID +"=cm_costobject.ccostobjectid");

        sql.append(" where d.dr = 0 ");
        //
     
        //主单位等于KG,注意当前环境，有一个全局的有一个集团的，业务单据是用的全局的  pk_group='~'
        sql.append(" and d.cunitid in ( select  pk_measdoc  from  bd_measdoc where name in('KG','kg') ");
//      sql.append(" and pk_group",pkGroup);
        sql.append(" )");
        sql.append(" and d.cbilltypecode", "I6");
        sql.append(" and d.pk_group",pkGroup);
        sql.append(" and d.pk_org",pk_cost);
        sql.append(" and d.caccountperiod",period);
        sql.append(" and d.ccostobjid<>'~'");//没有产成品的领料不需要计算成本

//        sql.append(" and d.dbizdate", ">=", para.getBeginDate().toString());
//        sql.append(" and d.dbizdate", "<=", para.getEndDate().toString());
        sql.append(" group by d.ccostcenterid ,cm_costobject.ccostobjectid ");

        DataAccessUtils utils = new DataAccessUtils();
        IRowSet rowSet = utils.query(sql.toString());
        if (rowSet == null || rowSet.size() == 0) {
            return null;
        }

        Map<String, UFDouble> resultMap = new HashMap<String, UFDouble>();
        while (rowSet.next()) {
            resultMap.put(rowSet.getString(0) + rowSet.getString(1), rowSet.getUFDouble(2));
        }
        return resultMap;
    }

    /**
     * 获取指定材料子项实际消耗数量动因值(按成本中心取数)
     * 
     * @param pkGroup
     *            集团
     * @param pkOrg
     *            工厂
     * @param period
     *            会计期间
     * @param preperiod
     *            上一期间\期初期间
     * @param centerSet
     *            Set<成本中心ID>
     * @param materialvid
     *            指定材料vID
     * @return Map<成本中心ID, 指定材料子项实际消耗数量>
     * @throws BusinessException
     */
    public Map<String, UFDouble> aquireAssignStuffActualNumberForCenter(String pkGroup, String pkOrg, String period,
            String preperiod, Set<String> centerSet, String materialvid) throws BusinessException {
        if (null == centerSet || centerSet.size() == 0) {
            return null;
        }
        
        Map<String, String> costRegion = BDAdapter.queryFinanceLayerCostRegion(pkOrg);
        if(costRegion == null){
        	return null;
        }
        String pk_cost = costRegion.get(pkOrg);
        
        // 
        // 插入条件临时表
        SqlBuilder sql = new SqlBuilder();
        sql.append(" select d.ccostcenterid, ");
        sql.append(" sum(d.nnum) as nnum ");
        sql.append(" from ia_detailledger d inner join ia_i6bill_b c on d.cbill_bid = c.cbill_bid ");
//        sql.append(" inner join resa_ccdepts on ic_material_h.cdptid=resa_ccdepts.pk_dept ");//部门对照成本中心
//        sql.append(" inner join cm_costobject on d.ccostobjid  =cm_costobject.cmaterialvid  ");//主产品成本对象
        sql.append(" where d.dr = 0 ");
        //
     
        //主单位等于KG,注意当前环境，有一个全局的有一个集团的，业务单据是用的全局的  pk_group='~'
        sql.append(" and d.cunitid in ( select  pk_measdoc  from  bd_measdoc where name in('KG','kg') ");
//      sql.append(" and pk_group",pkGroup);
        sql.append(" )");
        sql.append(" and d.cbilltypecode", "I6");
        sql.append(" and d.pk_group",pkGroup);
        sql.append(" and d.pk_org",pk_cost);
        sql.append(" and d.caccountperiod",period);        
        sql.append(" and d.ccostobjid<>'~'");//没有产成品的领料不需要计算成本
        sql.append(" and d.ccostcenterid", centerSet.toArray(new String[centerSet.size()]));

//        sql.append(" and d.dbizdate", ">=", para.getBeginDate().toString());
//        sql.append(" and d.dbizdate", "<=", para.getEndDate().toString());
        sql.append(" group by d.ccostcenterid  ");

        DataAccessUtils utils = new DataAccessUtils();
        IRowSet rowSet = utils.query(sql.toString());
        if (rowSet == null || rowSet.size() == 0) {
            return null;
        }

        Map<String, UFDouble> resultMap = new HashMap<String, UFDouble>();
        while (rowSet.next()) {
            resultMap.put(rowSet.getString(0), rowSet.getUFDouble(1));
        }
        return resultMap;
    }

   

    private String getMaterialId(String vId) {
        if (vId == null) {
            return null;
        }
        Map<String, String> materialidmap = BDAdapter.convertMaterialvid2Oid(new String[] {
            vId
        });
        if (CMMapUtil.isEmpty(materialidmap)) {
            return null;
        }
        return materialidmap.get(vId);
    }
}
