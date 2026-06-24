package nc.bs.mmpps.calc.bp.fetch.supply.feature;

import java.util.List;
import java.util.Map;

import nc.bs.mmpps.calc.bp.fetch.base.DBGetterConst;
import nc.bs.mmpps.calc.bp.fetch.base.FetchSQL;
import nc.bs.mmpps.calc.bp.fetch.feature.TransTypeSQLDecorate;
import nc.bs.mmpps.calc.bp.fetch.util.OnHandMapUtil;
import nc.bs.mmpps.calc.bp.temptable.MaterialMarasstframeTempTable;
import nc.bs.mmpps.calc.bp.temptable.PPSTempTable;
import nc.util.mmf.framework.db.MMSqlBuilder;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.entity.calculate.MaterialVO;
import nc.vo.mmpps.calc.entity.calculate.SupplyVO;
import nc.vo.pd.mpobjqryscheme.param.BillTransTypeVO;

public class OnHandExtSQLDecorate extends TransTypeSQLDecorate {

	private CalculateContext context;

	private String temptableName;

	@Override
	public String buildFromSql() {

		MaterialMarasstframeTempTable temptable = PPSTempTable.getInstance(
				MaterialMarasstframeTempTable.class, this.context);
		temptableName = temptable.getRealTableName();

		MMSqlBuilder sql = new MMSqlBuilder();
		sql.leftjoin();
		sql.append(temptableName);
		sql.on();
		sql.append(DBGetterConst.MATERIAL_ALIAS + "."
				+ MaterialVO.PK_MARASSTFRAME + " = " + temptableName + "."
				+ MaterialVO.PK_MARASSTFRAME);
		return sql.toString();
	}

	@Override
	public Map<String, String> buildTabColMap() {

		return newTablColMap(tabColMap);
	}

	@Override
	public String buildGroupBy() {
		return this.getInnerGroupBySql();
	}

	public Map<String, String> newTablColMap(Map<String, String> oldTabColMap) {
		return OnHandMapUtil.convert(oldTabColMap, this.context);
	}

	public OnHandExtSQLDecorate(FetchSQL dbGetter,
			Map<String, String> tabColMap, CalculateContext context,
			String transTypeColumn, String billTypeCode) {
		super(dbGetter, context, transTypeColumn, billTypeCode, false);
		this.tabColMap = tabColMap;
		this.context = context;

	}

	private Map<String, String> tabColMap;

	private String getInnerGroupBySql() {
		return new StringBuilder()
				.append(this.tabColMap.get(SupplyVO.PK_SUPPLYORG) + ",")
				.append(this.tabColMap.get(SupplyVO.CPPSMATERIALID) + ",")
				.append(this.tabColMap.get(SupplyVO.CMATERIALID) + ",")
				.append(DBGetterConst.MATERIAL_ALIAS + "."
						+ MaterialVO.CMEASDOC + ",")
				.append(this.tabColMap.get(SupplyVO.CVENDORID) + ",")
				.append(this.tabColMap.get(SupplyVO.CPRODUCTORID) + ",")
				.append(this.tabColMap.get(SupplyVO.CPROJECTID) + ",")
				.append(this.tabColMap.get(SupplyVO.CCUSTOMERID) + ",")
				.append(this.tabColMap.get(SupplyVO.CFFILEID) + ",")
				.append(this.tabColMap.get(SupplyVO.VBATCHCODE) + ",")
				.append(this.tabColMap.get(SupplyVO.CWAREHOUSEID) + ",")
				// .append(this.tabColMap.get(SupplyVO.PK_BATCHCODE) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE1) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE2) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE3) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE4) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE5) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE6) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE7) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE8) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE9) + ",")
				.append(this.tabColMap.get(SupplyVO.VFREE10))
				//20230907 yezhian 增加库存单据状态分组，主要为解决库存状态问题
				.append(", " +this.tabColMap.get(SupplyVO.FBILLSTATUS)).toString()
				
				;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String buildJYLXFilterSql() {
		Map<String, List<BillTransTypeVO>> billTransTypeVOs = (Map<String, List<BillTransTypeVO>>) this.context
				.getCacheValue(this.cacheKey);
		String retSql = "";
		if (billTransTypeVOs != null && billTransTypeVOs.size() > 0) {
			if(this.billType == null){
				this.billType = "inv2";
			}
			List<BillTransTypeVO> list = billTransTypeVOs.get(this.billType);
			if (list != null && list.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (BillTransTypeVO obj : list) {
					sb.append("'" + obj.getVonhandstatues() + "',");
				}
				if (sb.length() > 0) {
					sb.substring(0, sb.length() - 1);
					retSql = " (M.MARSTORESTATE = 'Y' and " + this.transTypeColumn
							+ " in (" + sb.substring(0, sb.length() - 1)
							+ ") or  M.MARSTORESTATE = 'N') ";
				}
			}
		}
		return retSql;
	}
}
