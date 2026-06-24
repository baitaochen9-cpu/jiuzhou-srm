package nc.itf.fa.info;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ProcessorUtils;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.DataTypeConstant;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

import com.ufida.dataset.IContext;

/**
 * 瑞博苏州固定资产折旧预测报表查询
 * 
 * @author zhian.ye
 * 
 */
public class DepreciationForecastImp implements DepreciationForecastItf {
	/**
	 * 按月计算折旧
	 * 
	 * @param context
	 *            起码需要两个时间参数【开始时间】【结束时间】
	 * @return 返回查询的月份有及对应折旧
	 * @throws DAOException
	 */
	BaseDAO dao = null;
	private final int PK_ORG = 0;
	private final int PK_GROUP = 1;
	private final int PK_DEPT = 2;
	private final int YEAR = 3;
	private final int MONTH = 4;
	private final int MONEY = 5;
	private final int ACCUSEP = 6;
	private final int PK_CATEGORY = 7;
	private final String[] fields = { "pk_org", "pk_group", "pk_dept", "year",
			"month", "money", "accudep", "pk_category" };


	public DataSet forecastForMonth(IContext context,
			nc.itf.fa.info.MyParam paramt) throws BusinessException {
		/*
		 * 方案： 取全部部门进行统计，从数据库取在用卡片进行查询使用部门，做为基础维度
		 * 逐一部门进行where过滤，以确保查询及数据处理效率，小心内存溢出，主要
		 * 查询出来的卡片逐一累计相加至部门上，不要去计较单个卡片，数据量过大
		 * 
		 * 字段： 集团/组织/科目/使用部门/时间轴/折旧金额 报表设置时使用交叉表实现X拓展
		 * 
		 * 月度： 年度：
		 */
		String pk_org = paramt.getPk_org();
		String begin_data = paramt.getBegin_data();
		String end_data = paramt.getEnd_data();

		DataSet ds = new DataSet();/* 数据集 */
		Field[] monthFiels = this.getMonthFiels();
		MetaData md = new MetaData(monthFiels);/* 元数据 */
		ds.setMetaData(md);
		List<Object[]> list = new ArrayList<Object[]>();// 这里添加数据

		Map<String, List<Map<String, Object>>> depts = getUsedept(pk_org);/* 部门分组数据 */

		if (null != depts && depts.size() > 0) {
			for (String dept : depts.keySet()) {
				List<Map<String, Object>> deptcardlist = depts.get(dept);/* 单部门参与计算的卡片集 */
				if (null == deptcardlist || deptcardlist.size() == 0) {/* 如果没有内容就直接返回计算下一个部门 */
					continue;
				}
				Object[] thisDeptCount = new Object[fields.length]; /*
																	 * 部门行数据，
																	 * 每取到一个最少有一行数据
																	 * ，看计算周期，
																	 * 每个周期都应该创建一行数据
																	 */

				Map<String, Object[]> deptSum = deptSum(deptcardlist, end_data,
						begin_data, thisDeptCount);
				for (String key : deptSum.keySet()) {
					String substring = begin_data.substring(0, 7);
					String substring2 = end_data.substring(0, 7);
					int compareTo = substring.compareTo(key.substring(0, 7));
					int compareTo2 = substring2.compareTo(key.substring(0, 7));
					if (compareTo > 0 || compareTo2 < 0) {
						continue;
					}
					Object[] objects = deptSum.get(key);
					list.add(objects);
				}

			}
		}
		ds.setDatas(list.toArray(new Object[0][0]));
		return ds;

	}

	/**
	 * 统计部门折旧总和
	 * 
	 * @param deptcardlist
	 *            （自用方法，此参数不做空非空判断，以在上层方法处理）
	 * @param begin_data
	 * @param thisDeptCount
	 * @param end_data
	 *            统计结束日间 （此参数必不能空） 开始时间自己算 * 1/先找出计算次数，【参数】结束日期月份 - 【数据】开始折旧日期月
	 *            = 计算次数 2/判断开始【数据】当前已折旧与【参数】开始日期月份差额做为轮空
	 *            目标：返回维数组，从当前折旧基到目村折旧期全部数据做为统计依据，
	 * @return Map<String,Map<String,Object>> key 期间标识 value 卡片信息及当期这就信息
	 * @throws BusinessException
	 */
	private Map<String, Object[]> deptSum(
			List<Map<String, Object>> deptcardlist, String end_date,
			String begin_data, Object[] thisDeptCount) throws BusinessException {

		if (null == end_date || "".equals(end_date)) {
			throw new BusinessException("结束日期不能为空，否则没有边界，无法计算！");
		}
		Integer end = new Integer(end_date.substring(0, 4)) * 12
				+ new Integer(end_date.substring(5, 7));/* 参数结束日期 */
		Integer begin = new Integer(begin_data.substring(0, 4)) * 12
				+ new Integer(begin_data.substring(5, 7));/* 参数开始日期 */
		Map<String, Object[]> rsult = new HashMap<String, Object[]>();

		for (Map<String, Object> map : deptcardlist) {
			String dep_start_date = (String) map.get("dep_start_date");/* 折旧开始时间 */
			Integer usedmonth = (Integer) map.get("usedmonth");/* 已计提月 */
			Integer used = new Integer(dep_start_date.substring(0, 4)) * 12
					+ new Integer(dep_start_date.substring(5, 7)) + usedmonth;/* 已计提截止期间 */
			UFDouble accudep = new UFDouble( map.get("accudep").toString());/* 累计折旧 */

			UFDouble predevaluate = new UFDouble(
					 map.get("predevaluate").toString());/* 减值准备 */
			UFDouble salvage = new UFDouble(map.get("salvage").toString());/* 净残值 */
			UFDouble localoriginvalue = new UFDouble(
					 map.get("localoriginvalue").toString());/* 原值 */
			UFDouble servicemonth = new UFDouble(
					(Integer) map.get("servicemonth"));/* 使用年限 */
			String pk_category = (String) map.get("pk_category");/* 资产类别 */
			UFDouble month = new UFDouble((String) map.get(fields[MONTH]));
			UFDouble year = new UFDouble((String) map.get(fields[YEAR]));
			Object[] data = null;

			while (end - used + 1 >= 0
					&& servicemonth.sub(usedmonth).doubleValue() != 0
					&& localoriginvalue.sub(accudep).sub(salvage)
							.sub(predevaluate).doubleValue() != 0) {
				UFDouble div = null;
				try {
					div = (localoriginvalue.sub(accudep).sub(salvage)
							.sub(predevaluate))
							.div(servicemonth.sub(usedmonth));/* 默认：平均年限法二 */

				} catch (Exception e) {
					System.err.println("123");
				}
				month = month.add(1).setScale(0, 0);
				if (month.intValue() > 12) { /* 如果月分加和大于12，则设置成1，年份上加1 */
					month = UFDouble.ONE_DBL.setScale(0, 0);
					year = year.add(1).setScale(0, 0);
				}
				String strkey = year.intValue()
						+ "-"
						+ (month.toString().length() == 1 ? "0"
								+ month.toString() : month.toString()) + "/"
						+ pk_category;
				if (null == rsult.get(strkey)) {
					data = new Object[fields.length];
					initialize_data(data, map);
					rsult.put(strkey, data);
				}
				data = rsult.get(strkey);
				data[MONTH] = year.intValue()
						+ "-"
						+ (month.toString().length() == 1 ? "0"
								+ month.toString() : month.toString());
				data[YEAR] = year;
				data[MONEY] = new UFDouble(
						(data[MONEY] == null ? UFDouble.ZERO_DBL : data[MONEY])
								.toString()).add(div);

				used++;

				usedmonth++;/* 已计提月进1 */
				accudep = accudep.add(div);/* 累计折旧进行累加 */
				data[ACCUSEP] = new UFDouble(
						(data[ACCUSEP] == null ? UFDouble.ZERO_DBL
								: data[ACCUSEP]).toString()).add(accudep);
				rsult.put(strkey, data);
			}

		}

		return rsult;

	}

	/**
	 * /*初始化数据
	 * 
	 * @param data
	 * @param map
	 */
	private void initialize_data(Object[] data, Map<String, Object> map) {
		data[PK_ORG] = map.get(fields[PK_ORG]);
		data[PK_GROUP] = map.get(fields[PK_GROUP]);
		data[PK_DEPT] = map.get(fields[PK_DEPT]);
		data[MONEY] = UFDouble.ZERO_DBL;
		data[PK_CATEGORY] = map.get(fields[PK_CATEGORY]);

	}

	/**
	 * 获取部门数据（关联卡片使用部门）
	 * 
	 * @throws BusinessException
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Map<String, Object>>> getUsedept(String pk_org)
			throws BusinessException {
		String sql = "select forcardhistory.pk_cardhistory " /*资产类别，年，月*/
                      +" ,fa_cardhistory.pk_category "
      +" ,fa_cardhistory.period  month " 
      +" ,fa_cardhistory.accyear year " 
     +"  ,forcardhistory.pk_card " /*卡片ID*/
      +" , forcardhistory.pk_usedept " /*使用部门*/
      +"  ,fa_deptscale.pk_dept" /*费用 部门*/
     +"  , forcardhistory.pk_org " /*组织*/
      +" ,forcardhistory.pk_group " /*集团*/
     +"  ,fa_card.asset_code " /*资产编码 */
     +"  ,fa_card.asset_name " /*资产名称 */
     +"  ,fa_card.card_code " /*卡片编号*/
    +"  ,forcardhistory.localoriginvalue " /*原值*/
     +" ,forcardhistory.accudep " /*累计折旧*/
     +" ,forcardhistory.predevaluate " /*减值准备*/
     +" ,forcardhistory.dep_start_date " /*开始折旧时间*/
     +" ,forcardhistory.usedmonth  "  /*已计提月份*/
     +" ,forcardhistory.servicemonth  "  /*折旧期数*/
     +" ,forcardhistory.pk_depmethod " /*折旧方法*/
     +" ,forcardhistory.salvage " /* 净残值 */
     +" ,forcardhistory.salvagerate " /* 净残值 */
     +"  from forcardhistory " 
      +" left join fa_card on fa_card.pk_card = forcardhistory.pk_card " 
      +" left join fa_cardhistory on fa_cardhistory.pk_cardhistory = forcardhistory.pk_cardhistory " +
      " left join fa_deptscale on forcardhistory.pk_usedept = fa_deptscale.link_key and nvl(fa_deptscale.dr,0)=0 ";

		Map<String, List<Map<String, Object>>> executeQuery = null;
		try {
			executeQuery = (Map<String, List<Map<String, Object>>>) getDAO()
					.executeQuery(sql, new ResultSetProcessor() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						
						
						
						
						
						
						@Override
						public Map<String, List<Map<String, Object>>> handleResultSet(
								ResultSet rs) throws SQLException {
							Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
							while (rs.next()) {
								Map<String, Object> rowData = ProcessorUtils
										.toMap(rs);/* 单行数据 */
								List<Map<String, Object>> rowDataList = result
										.get(rowData.get("pk_dept"));
								if (null == rowDataList
										|| rowDataList.size() == 0) {
									rowDataList = new ArrayList<Map<String, Object>>();
									result.put((String) rowData.get("pk_dept"),
											rowDataList);
								}
								rowDataList.add(rowData);
							}
							return result;
						}

					});
		} catch (DAOException e) {
			throw new BusinessException(e);
		}
		return executeQuery;
	}

	/**
	 * 提供报表所需元数据
	 * 
	 * @return
	 */
	private Field[] getMonthFiels() {
		List<Field> fieldlist = new ArrayList<Field>();
		for (String name : this.fields) {
			Field field = new Field();
			if (name.equals("money")) {
				field.setDataType(DataTypeConstant.DOUBLE);
			} else {
				field.setDataType(DataTypeConstant.STRING);
			}
			field.setFldname(name);
			fieldlist.add(field);
		}
		return fieldlist.toArray(new Field[0]);

	}

	private BaseDAO getDAO() {
		if (null == dao) {
			dao = new BaseDAO();
		}
		return dao;

	}
}
