package nc.bs.mmpps.calc.bp.workcalendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.mmpps.calc.bp.db.DBUtil;
import nc.bs.mmpps.calc.bp.temptable.DemandTempTable;
import nc.bs.mmpps.calc.bp.temptable.PPSTempTable;
import nc.bs.mmpps.calc.bp.temptable.SupplyTempTable;
import nc.impl.pubapp.pattern.database.TempTable;
import nc.jdbc.framework.SQLParameter;
import nc.util.mmf.busi.consts.BillTypeConst;
import nc.util.mmf.framework.db.MMSqlBuilder;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.entity.calculate.DemandVO;
import nc.vo.mmpps.calc.entity.calculate.PDemandVO;
import nc.vo.mmpps.calc.entity.calculate.SupplyVO;
import nc.vo.mmpps.calc.entity.schema.SchemaVO;
import nc.vo.mmpps.calc.enumeration.DemandType;
import nc.vo.pub.JavaType;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * 匹配日期调整
 * 
 * @since 6.5
 * @version 2012-8-6 上午09:15:53
 * @author guojunf
 */
public class MatchDateAdjustManager {

	private CalculateContext context;

	// 供应的非工作日对应工作日映射
	Map<UFDate, UFDate> dateMapping4Supply;

	// 需求的非工作日对应工作日映射
	Map<UFDate, UFDate> dateMapping4Demand;

	private String MatchWorkDateFieldName = "matchworkdate";

	// 供应日期临时表
	private String supplydateTempTable;

	// 需求日期临时表
	public String demanddateTempTable;

	private DBUtil dbUtil;

	private String demandTable;

	private String supplyTable;

	public MatchDateAdjustManager(CalculateContext context, DBUtil dbUtil) {
		this.context = context;
		this.dbUtil = dbUtil;
		this.demandTable = PPSTempTable.getInstance(DemandTempTable.class,
				this.context).getRealTableName();
		this.supplyTable = PPSTempTable.getInstance(SupplyTempTable.class,
				this.context).getRealTableName();
	}

	public void adjustDemand(String levelCode, DemandType demandType) {

		Map<String, SQLParameter> mapSql = new HashMap<String, SQLParameter>();
		// 需求及供给日期调整
		this.dateMapping4Demand = this.context.getWorkDateCache()
				.getDateMapping4Demand();

		// 更新需求明细中需求日期小于当前日期的记录
		SQLParameter para = new SQLParameter();
		para.addParam(this.context.getStartWorkDate());
		para.addParam(this.context.getStartWorkDate());

		MMSqlBuilder sql = new MMSqlBuilder();
		sql.update();
		sql.append(this.demandTable);
		sql.set();
		sql.append(DemandVO.DMATCHDATE + " = ");
		sql.l();
		sql.append(" case when " + DemandVO.DEMANDTIME + " < ? then ? else "
				+ DemandVO.DEMANDTIME + " end ");
		sql.r();

		// 运算号
		sql.where();
		if (demandType != null) {
			sql.append(DemandVO.FDEMANDTYPE, demandType.toInt());
			sql.and();
		}
		if (levelCode != null) {
			sql.append(DemandVO.LEVELCODE, levelCode);
		}

		mapSql.put(sql.toString(), para);

		// 更新需求日期不是工作日的匹配日期
		this.demanddateTempTable = this.createDemandMatchDateTemp();
		if (this.demanddateTempTable != null) {
			Map<String, SQLParameter> map = this.getDemandMatchDateUpdateSql();
			mapSql.putAll(map);
		}

		this.dbUtil.execBatchSql(mapSql);
	}

	public void adjustDS() {
		// 计划独立需求日期调整
		PIDDemandTimeManager pidAdjust = new PIDDemandTimeManager(this.context,
				this.dbUtil);
		pidAdjust.adjustDate();
		// 需求及供给日期调整
		Map<String, SQLParameter> mapSql = new HashMap<String, SQLParameter>();
		this.dateMapping4Demand = this.context.getWorkDateCache()
				.getDateMapping4Demand();
		this.dateMapping4Supply = this.context.getWorkDateCache()
				.getDateMapping4Supply();
		// 更新匹配日期小于当前日期的记录
		Map<String, SQLParameter> histSqlmap = this.UpdateMatchDate();
		mapSql.putAll(histSqlmap);
		this.dbUtil.execBatchSql(mapSql);
		mapSql.clear();
		this.supplydateTempTable = this.createSupplyMatchDateTemp();
		this.demanddateTempTable = this.createDemandMatchDateTemp();

		// 更新匹配日期不在工作日的记录
		if (this.supplydateTempTable != null) {
			Map<String, SQLParameter> map = this.getSupplyMatchDateUpdateSql();
			mapSql.putAll(map);
		}
		if (this.demanddateTempTable != null) {
			Map<String, SQLParameter> map = this.getDemandMatchDateUpdateSql();
			mapSql.putAll(map);
		}

		if (this.demanddateTempTable != null
				&& this.supplydateTempTable != null) {
			Map<String, SQLParameter> map = this.getPDemandMatchDateUpdateSql();
			mapSql.putAll(map);
		}

		this.dbUtil.execBatchSql(mapSql);

	}

	private Map<String, SQLParameter> UpdateMatchDate() {
		Map<String, SQLParameter> map = new HashMap<String, SQLParameter>();
		
	
		// ///////////////////***供给****////////////////////////////
		SQLParameter paraS = new SQLParameter();
		paraS.addParam(this.context.getStartWorkDate());
		paraS.addParam(this.context.getStartWorkDate());
		

		MMSqlBuilder sqlS = new MMSqlBuilder();
		sqlS.update();
		sqlS.append(this.supplyTable);
		sqlS.set();
		sqlS.append(SupplyVO.DMATCHDATE + " = ");
		sqlS.l();
	
			sqlS.append(" case when " + SupplyVO.DSUPPLYDATE + " < ? then ? else "
					+ SupplyVO.DSUPPLYDATE + " end ");	
			sqlS.r();

	
		

		
		
		map.put(sqlS.toString(), paraS);

		// ///////////////////***需求****////////////////////////////

		SQLParameter paraD = new SQLParameter();
		paraD.addParam(this.context.getStartWorkDate());
		paraD.addParam(this.context.getStartWorkDate());

		MMSqlBuilder sqlD = new MMSqlBuilder();
		sqlD.update();
		sqlD.append(this.demandTable);
		sqlD.set();
		sqlD.append(DemandVO.DMATCHDATE + " = ");
		sqlD.l();
		sqlD.append(" case when " + DemandVO.DEMANDTIME + " < ? then ? else "
				+ DemandVO.DEMANDTIME + " end ");
		sqlD.r();

		sqlD.where();
		sqlD.append(DemandVO.VDEMANDTYPECODE + "<>'" + BillTypeConst.PID + "'");

		map.put(sqlD.toString(), paraD);

		return map;

	}

	private Map<String, SQLParameter> getSupplyMatchDateUpdateSql() {
		Map<String, SQLParameter> map = new HashMap<String, SQLParameter>();
		MMSqlBuilder sql = new MMSqlBuilder();
		sql.update();
		sql.append(this.supplyTable);
		sql.set();
		sql.append(SupplyVO.DMATCHDATE + " = ");
		sql.l();
		sql.select();
		sql.append(this.supplydateTempTable + "." + this.MatchWorkDateFieldName);
		sql.from(this.supplydateTempTable);
		sql.where();
		sql.appendFieldEqual(this.supplyTable + "." + SupplyVO.DMATCHDATE,
				this.supplydateTempTable + "." + SupplyVO.DMATCHDATE);
		sql.r();
		sql.where();
		sql.exists();
		sql.l();
		sql.select();
		sql.append("1");
		sql.from(this.supplydateTempTable);
		sql.where();
		sql.appendFieldEqual(this.supplydateTempTable + "."
				+ SupplyVO.DMATCHDATE, this.supplyTable + "."
				+ SupplyVO.DMATCHDATE);
		sql.r();
		SQLParameter para = new SQLParameter();

		map.put(sql.toString(), para);
		return map;
	}

	private Map<String, SQLParameter> getDemandMatchDateUpdateSql() {
		Map<String, SQLParameter> map = new HashMap<String, SQLParameter>();
		MMSqlBuilder sql = new MMSqlBuilder();
		sql.update();
		sql.append(this.demandTable);
		sql.set();
		sql.append(DemandVO.DMATCHDATE + " = ");
		sql.l();
		sql.select();
		sql.append(this.demanddateTempTable + "." + this.MatchWorkDateFieldName);
		sql.from(this.demanddateTempTable);
		sql.where();
		sql.appendFieldEqual(this.demandTable + "." + DemandVO.DMATCHDATE,
				this.demanddateTempTable + "." + DemandVO.DMATCHDATE);
		sql.r();
		sql.where();
		sql.exists();
		sql.l();
		sql.select();
		sql.append("1");
		sql.from(this.demanddateTempTable);
		sql.where();
		sql.appendFieldEqual(this.demanddateTempTable + "."
				+ DemandVO.DMATCHDATE, this.demandTable + "."
				+ DemandVO.DMATCHDATE);
		sql.r();
		sql.and();
		// 计划独立需求这里不处理
		sql.append(this.demandTable + "." + DemandVO.VDEMANDTYPECODE + "<>'"
				+ BillTypeConst.PID + "'");

		SQLParameter para = new SQLParameter();
		map.put(sql.toString(), para);
		return map;
	}

	/**
	 * 更新父项需求表
	 * 
	 * @param demanddateTempTable
	 * @param supplydateTempTable
	 * @return
	 */
	private Map<String, SQLParameter> getPDemandMatchDateUpdateSql() {

		Map<String, SQLParameter> map = new HashMap<String, SQLParameter>();

		SQLParameter paraD = new SQLParameter();
		paraD.addParam(this.context.getCalculateCode());

		// 更新需求匹配日期
		MMSqlBuilder sqlD = new MMSqlBuilder();
		sqlD.update();
		sqlD.append(PDemandVO.TABLENAME);
		sqlD.set();
		sqlD.append(PDemandVO.DMATCHDATE + " = ");
		sqlD.l();
		sqlD.select();
		sqlD.append(this.demanddateTempTable + "."
				+ this.MatchWorkDateFieldName);
		sqlD.from(this.demanddateTempTable);
		sqlD.where();
		sqlD.appendFieldEqual(PDemandVO.TABLENAME + "." + PDemandVO.DMATCHDATE,
				this.demanddateTempTable + "." + PDemandVO.DMATCHDATE);
		sqlD.r();
		sqlD.where();
		sqlD.exists();
		sqlD.l();
		sqlD.select();
		sqlD.append("1");
		sqlD.from(this.demanddateTempTable);
		sqlD.where();
		sqlD.appendFieldEqual(this.demanddateTempTable + "."
				+ PDemandVO.DMATCHDATE, PDemandVO.TABLENAME + "."
				+ PDemandVO.DMATCHDATE);
		sqlD.append(" )");
		// 运算号
		sqlD.and();
		sqlD.appendFieldEqual(PDemandVO.COMPUTECODE, "?");
		map.put(sqlD.toString(), paraD);

		SQLParameter paraS = new SQLParameter();
		paraS.addParam(this.context.getCalculateCode());
		// 更新供给匹配日期
		MMSqlBuilder sqlS = new MMSqlBuilder();
		sqlS.update();
		sqlS.append(PDemandVO.TABLENAME);
		sqlS.set();
		sqlS.append(PDemandVO.DSUPPLYMATCHDATE + " = ");
		sqlS.l();
		sqlS.select();
		sqlS.append(this.supplydateTempTable + "."
				+ this.MatchWorkDateFieldName);
		sqlS.from(this.supplydateTempTable);
		sqlS.where();
		sqlS.appendFieldEqual(PDemandVO.TABLENAME + "."
				+ PDemandVO.DSUPPLYMATCHDATE, this.supplydateTempTable + "."
				+ PDemandVO.DMATCHDATE);
		sqlS.r();
		sqlS.where();
		sqlS.exists();
		sqlS.l();
		sqlS.select();
		sqlS.append("1");
		sqlS.from(this.supplydateTempTable);
		sqlS.where();
		sqlS.appendFieldEqual(this.supplydateTempTable + "."
				+ PDemandVO.DMATCHDATE, PDemandVO.TABLENAME + "."
				+ PDemandVO.DSUPPLYMATCHDATE);
		sqlS.append(" )");
		// 运算号
		sqlS.and();
		sqlS.appendFieldEqual(PDemandVO.COMPUTECODE, "?");
		map.put(sqlS.toString(), paraS);

		return map;
	}

	private String createSupplyMatchDateTemp() {
		TempTable tt = new TempTable();
		String tablename = "temp_mm_mpsmrp_wdsupply";
		String[] columns = new String[] { SupplyVO.DMATCHDATE,
				this.MatchWorkDateFieldName };
		String[] columnTypes = new String[] { "char(19)", "char(19)" };
		JavaType[] types = new JavaType[] { JavaType.String, JavaType.String };

		List<List<Object>> data = new ArrayList<List<Object>>();
		if (this.dateMapping4Supply.size() > 0) {
			for (UFDate key : this.dateMapping4Supply.keySet()) {
				List<Object> list = new ArrayList<Object>();
				list.add(key);
				list.add(this.dateMapping4Supply.get(key));
				data.add(list);
			}
		}
		if (data.size() > 0) {
			String tempTable = tt.getTempTable(tablename, columns, columnTypes,
					types, data);
			return tempTable;
		}
		return null;
	}

	private String createDemandMatchDateTemp() {
		TempTable tt = new TempTable();
		String tablename = "temp_mm_mpsmrp_wddemand";
		String[] columns = new String[] { DemandVO.DMATCHDATE,
				this.MatchWorkDateFieldName };
		String[] columnTypes = new String[] { "char(19)", "char(19)" };
		JavaType[] types = new JavaType[] { JavaType.String, JavaType.String };

		List<List<Object>> data = new ArrayList<List<Object>>();
		if (this.dateMapping4Demand.size() > 0) {
			for (UFDate key : this.dateMapping4Demand.keySet()) {
				List<Object> list = new ArrayList<Object>();
				list.add(key);
				list.add(this.dateMapping4Demand.get(key));
				data.add(list);
			}
		}
		if (data.size() > 0) {
			String tempTable = tt.getTempTable(tablename, columns, columnTypes,
					types, data);
			return tempTable;
		}
		return null;
	}

}
