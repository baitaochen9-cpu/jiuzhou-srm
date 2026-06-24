package nc.impl.to.m5x;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import nc.bs.arap.util.SqlUtils;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pu.pub.VOQryUtil;
import nc.itf.scmpub.reference.uap.org.CostRegionPubService;
import nc.itf.to.m5x.IFindPricePubService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.ia.pub.period.AccountPeriod;
import nc.vo.ia.pub.period.Calendar;
import nc.vo.ia.pub.util.IAScaleUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nccloud.base.exception.ExceptionUtils;

public class FindPricePubServiceImpl implements IFindPricePubService {

	private IUAPQueryBS qryservice;

	/**
	 * 
	 * @param map
	 *            Map<行号，Map<属性，值>>
	 * @return map<行号，单价> 逻辑，逐行取价，以免不同行维度有差异。
	 */
	@Override
	public Map<Integer, UFDouble> doFindPrice(
			Map<Integer, Map<String, String>> map) {
		try {
			Map<Integer, UFDouble> result = new HashMap<Integer, UFDouble>();
			// 获取时间
			long bizDateTime = InvocationInfoProxy.getInstance()
					.getBizDateTime();
			UFDate currDate = new UFDate(bizDateTime).asEnd();

			// 询订单价格
			this.findSaleOrderPrice(result, map, currDate);

			// 询结存价格
			this.findNabPrice(result, map, currDate);
			// 询物料价格
			this.findMaterialPrice(result, map, currDate);
			
			// 以上都没有的情况，检查库存流水，防止被卡特殊日期
			this.findMaterialGuaranteePrice(result, map, currDate);
			return result;
		} catch (Exception e) {
			ExceptionUtils.wrapBusinessException("询价失败" + e);
		}
		return null;
	}

	/**
	 * 检查流水里可能 存在的单价信息
	 * @param result
	 * @param map
	 * @param currDate
	 * @throws BusinessException 
	 */
	private void findMaterialGuaranteePrice(Map<Integer, UFDouble> result,
			Map<Integer, Map<String, String>> map, UFDate currDate) throws BusinessException {
		// TODO Auto-generated method stub
		//直接取物料ID关联主数据查流水，最近一条带单价的，
		if(null == result || result.size() == 0 ){
			return;
		}
		for(Integer key  : result.keySet()){
			if(null != result.get(key)){
				continue;
			}
			Map<String, String> map2 = map.get(key);
			String cprojectid = map2.get("cprojectid");
			String pk_material = map2.get("pk_material");
			StringBuffer sql = new StringBuffer() ;
			sql.append(" select ncostprice from ( " +
					"select ncostprice from ( " +
					" select case ic_flow.cbilltypecode  when '40'    then ncostprice "+
					"  when '45' then  (select nprice from  po_arriveorder_b where  pk_arriveorder_b  =  ic_flow.csourcebillbid ) else ncostprice "+
					"  end as ncostprice ,   ic_flow.cprojectid, "+
					"  bd_material.def7 ,ic_flow.cmaterialvid ,ic_flow.ts  "+
					" from ic_flow left join bd_material on ic_flow.cmaterialvid = bd_material.pk_material  "+
					" where ic_flow.cbilltypecode in ('40','45') and nvl(ic_flow.dr,0)=0 ");
			if(null != cprojectid){
				sql.append("  and ic_flow.cprojectid = '"+cprojectid+"' ") ;
			}
					
			sql.append(	"  and cmaterialvid in ( select pk_material from bd_material "+
					"  where def7 = (select def7 from bd_material where pk_material ='"+pk_material+"') "+
					"  )   order by ic_flow.ts desc  )" +
					"  where nvl(ncostprice,0) <> 0 ) " +
					"   where rownum =1" );
			Object executeQuery = this.getQryService().executeQuery(sql.toString(), new ColumnProcessor());
			if (null != executeQuery){
				result.put(key, new UFDouble(executeQuery.toString()));
			}
		}
		
	}

	private void findSaleOrderPrice(Map<Integer, UFDouble> result,
			Map<Integer, Map<String, String>> map, UFDate currDate)
			throws BusinessException {

		UFDate lastDate = new UFDate(currDate.toStdString().substring(0, 7)
				+ "-01").asBegin();
		Set<String> orgs = new HashSet<>();
		Set<String> material = new HashSet<>();
		for (Integer key : map.keySet()) {
			Map<String, String> map2 = map.get(key); // 行数据

			StringBuilder sql = new StringBuilder();

			// sql.append(" select b.pk_org , b.cmaterialvid pk_material,bd_material.def7,b.cprojectid,sum(b.norigmny)/sum(b.nnum) price from ic_purchasein_b b ");
			sql.append(this.getSelectSql(map2));
			sql.append(" from ic_purchasein_b b  ");
			sql.append(" left join ic_purchasein_h h on h.CGENERALHID = b.CGENERALHID ");
			sql.append(" left join bd_material on b.cmaterialvid = bd_material.pk_material ");
			sql.append(" where h.dr = 0 and b.dr = 0 and b.norigtaxmny is not null and b.nnum is not null ");
			sql.append(" and h.fbillflag = '3' and nvl(b.flargess,'N') = 'N' and nvl(h.freplenishflag,'N') = 'N' ");
			// sql.append(" and "+SqlUtils.getInStr("b.cmaterialvid",
			// material.toArray(new String[material.size()])));
			// sql.append(" and "+SqlUtils.getInStr("b.pk_org", orgs.toArray(new
			// String[orgs.size()])));
			sql.append(" and h.dbilldate>='" + lastDate.toString() + "' ");
			sql.append(" and h.dbilldate<='" + currDate.toString() + "' ");
			sql.append(" and  b.pk_org = '" + map2.get("pk_org") + "' ");// 设置组织查询条件
			sql.append(this.getMaterialCound(map2));// 设置物料查询条件**
													// 确认是否为主数据检索，或者物料ID检索
			// sql.append(" group by b.pk_org,b.cmaterialvid ");
			sql.append(this.getGroupSql(map2));// 动态分组
			sql.append(this.getOrderSql(map2));// 动态排序

			// 脚本查询返回物料、单价。
			Map<String, UFDouble> res = (Map<String, UFDouble>) this
					.getQryService().executeQuery(sql.toString(),
							new ResultSetProcessor() {
								@Override
								public Object handleResultSet(ResultSet rs)
										throws SQLException {
									Map<String, UFDouble> map = new HashMap<>();
									while (rs.next()) {
										String key = rs
												.getString("pk_material");
										String price = String.valueOf(rs
												.getObject("price"));
										map.put(key,
												StringUtils.isBlank(price) ? UFDouble.ZERO_DBL
														: new UFDouble(price)
																.setScale(
																		4,
																		UFDouble.ROUND_HALF_UP));
									}
									return map;
								}
							});

			result.put(key, res.get(map2.get("pk_material")));
		}

	}

	private String getOrderSql(Map<String, String> map2) {
		StringBuffer ordersql = new StringBuffer();
		ordersql.append(" order by b.pk_org,b.cmaterialvid ,bd_material.def7 ");
		String cprojectid = map2.get("cprojectid");
		if (null != cprojectid && !"".equals(cprojectid)) {
			ordersql.append(" ,b.cprojectid ");
		}
		return ordersql.toString();
	}

	/**
	 * 
	 * @param map2
	 * @return
	 */
	private Object getGroupSql(Map<String, String> map2) {
		StringBuffer groupsql = new StringBuffer();
		groupsql.append(" group by b.pk_org,b.cmaterialvid ,bd_material.def7 ");
		String cprojectid = map2.get("cprojectid");
		if (null != cprojectid && !"".equals(cprojectid)) {
			groupsql.append(" ,b.cprojectid ");
		}
		return groupsql.toString();
	}

	/**
	 * select 清单
	 * 
	 * @param map2
	 * @return
	 */
	private String getSelectSql(Map<String, String> map2) {
		StringBuffer selectSql = new StringBuffer();
		selectSql
				.append(" select b.pk_org , b.cmaterialvid pk_material,bd_material.def7 ");
		String cprojectid = map2.get("cprojectid");
		if (null != cprojectid && !"".equals(cprojectid)) {
			selectSql.append(" ,b.cprojectid ");
		}
		selectSql.append(" ,sum(b.norigmny)/sum(b.nnum) price  ");
		return selectSql.toString();
	}

	/**
	 * 
	 * @param map2
	 * @return
	 */
	private String getMaterialCound(Map<String, String> map2) {
		// TODO Auto-generated method stub
		String mdmcode = map2.get("mdmcode");
		String pk_material = map2.get("pk_material");
		StringBuffer sql = new StringBuffer();
		sql.append(" ");
		if (null != mdmcode && !"".equals(mdmcode)) {
			sql.append(" and bd_material.def7 = '" + mdmcode + "' ");
			// sql.append(" and bd_material.pk_org = '"+map2.get("cinstockorgid")+"'");
			// //无法锁定公司取从，采购时可能会有跨公司物料出现，
		} else {
			sql.append(" and b.cmaterialvid = '" + pk_material + "' ");
		}

		return sql.toString();
	}

	private void findNabPrice(Map<Integer, UFDouble> result,
			Map<Integer, Map<String, String>> map, UFDate currDate)
			throws BusinessException {
		if (map.isEmpty()) {
			return;
		}

		// List<String> keyList = new ArrayList<String>();
		for (Integer row : map.keySet()) {
			Map<String, String> rowdate = map.get(row);
			String pk_org = rowdate.get("pk_org");

			String pk_material = rowdate.get("pk_material");
			String mdmcode = rowdate.get("mdmcode");
			String pk_project = rowdate.get("cprojectid");
			// String pk_product = dimensions[3];
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT    ia_detailledger.pk_org, MAX(ia_detailledger.caccountperiod) AS max_period ,  ia_detailledger.nprice,  ");
			if (null != pk_project && !pk_project.isEmpty()) {
				sql.append(" ia_detailledger.cprojectid ,");
			}
			sql.append(" bd_material.def7 ");
			sql.append(" from ia_detailledger  left join bd_material on ia_detailledger.cinventoryid = bd_material.pk_material ");
			sql.append(" WHERE ia_detailledger.iauditsequence <> '-1' ");
			// sql.append(" and replace(ia_detailledger.cprojectid, '~', null) is not null ");
			sql.append(" and ia_detailledger.pk_org = (select pk_org from org_orgs where orgtype19='Y' and code = (select code from org_orgs where pk_org='"
					+ pk_org + "'))");
			// sql.append(" and ia_detailledger.cinventoryid = '" + pk_material
			// + "'");
			if (!StringUtils.isEmpty(pk_project)) {
				sql.append(" and ia_detailledger.cprojectid = '" + pk_project
						+ "'");
			}
			if (!StringUtils.isEmpty(mdmcode)) {
				sql.append(" and bd_material.def7 = '" + mdmcode + "'");
				sql.append(" and bd_material.pk_org = '"
						+ rowdate.get("cinstockorgid") + "'");
			} else {
				sql.append(" and bd_material.pk_material = '" + pk_material
						+ "'");
			}
			sql.append(" GROUP BY ia_detailledger.pk_org ,ia_detailledger.nprice ");
			if (null != pk_project && !pk_project.isEmpty()) {
				sql.append(" ,ia_detailledger.cprojectid");
			}
			sql.append(", bd_material.def7");
			Map<String, Object> res = (Map<String, Object>) this
					.getQryService().executeQuery(sql.toString(),
							new MapProcessor());
			if (res != null && res.size() > 0) {
				Object obj = res.get("nprice");
				UFDouble price = obj == null ? UFDouble.ZERO_DBL
						: new UFDouble(obj.toString()).setScale(4,
								UFDouble.ROUND_HALF_UP);
				if (null == result.get(row)) {
					result.put(row, price);
				}
			}
		}

		// for(String key : keyList){
		// if(map.containsKey(key)){
		// map.remove(key);
		// }
		// }
	}

	private void findMaterialPrice(Map<Integer, UFDouble> result,
			Map<Integer, Map<String, String>> map, UFDate currDate)
			throws BusinessException {
		if (map.isEmpty()) {
			return;
		}
		// String priod= currDate.toStdString().substring(0, 7);
		// IAScaleUtil scaleutil = IAScaleUtil.getScaleUtils();
		for (Integer row : map.keySet()) {
			Map<String, String> rowdata = map.get(row);
			String sql = "select planprice from bd_materialfi  where pk_material = '"
					+ rowdata.get("pk_material")
					+ "' and pk_org ='"
					+ rowdata.get("pk_org") + "'";
			Object price = this.getQryService().executeQuery(sql,
					new ColumnProcessor());
			if (price != null) {
				UFDouble planprice = new UFDouble(price.toString());
				if (null != result.get(row)) {
					result.put(row, planprice);
				}
			}
		}
	}

	private String getCostorgid(String pk_org) throws BusinessException {
		return (String) this.getQryService().executeQuery(
				"select  pk_costregion from org_costregion where pk_org= '"
						+ pk_org + "'", new ColumnProcessor());
	}

	private String getPK_book(String pk_org) {
		String[] costregionids = { pk_org };
		Map mapBook = CostRegionPubService
				.queryAccountingBookIDByCostRegionID(costregionids);
		return ((String) mapBook.get(pk_org));
	}

	private IUAPQueryBS getQryService() {
		if (this.qryservice == null) {
			this.qryservice = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return this.qryservice;
	}

	/**
	 * 通过单据信息，找来源单据单价 主要处理SO销售订单直接关联的补货处理
	 * 
	 * @throws BusinessException
	 */
	@Override
	public Map<Integer, UFDouble> doFindPrice_so(
			Map<Integer, Map<String, String>> map) throws BusinessException {
		// 创建返回
		Map<Integer, UFDouble> rst = new HashMap<Integer, UFDouble>();
		Map<String, UFDouble> buf = new HashMap<String, UFDouble>();// 创建一个缓存，处理相同物料；
		for (Integer row : map.keySet()) {
			Map<String, String> rowdata = map.get(row); // 取单行数据
			// String csrcid = rowdata.get("csrcid");//来源ID
			String csrcbid = rowdata.get("csrcbid");// 明细ID
			// String mdmcode = rowdata.get("mdmcode");
			String pk_material = rowdata.get("pk_material");
			if (null == csrcbid) { // 行ID为空，可能 为复制行，取价失败，先给空值
				rst.put(row, null);
			}
			if (buf.get(pk_material) != null) {// 如果这个物料已经找过有找过，不再查了，
				rst.put(row, buf.get(pk_material));
			} else {

				String sql = " select nprice  from so_saleorder_b where csaleorderbid ='"
						+ csrcbid + "' ";
				Object price = this.getQryService().executeQuery(sql,
						new ColumnProcessor());
				if (price != null) {
					UFDouble planprice = new UFDouble(price.toString());
					buf.put(pk_material, planprice);
					rst.put(row, planprice);
				}

			}

		}

		for (Integer r : rst.keySet()) {
			if (null == rst.get(r)) {
				rst.put(r, buf.get(map.get(r).get("pk_material")));
			}
		}

		return rst;
	}

	@Override
	public Map queryDbByCond(String id) throws BusinessException {
		// VOQryUtil<MarBasClassVO> query = new
		// VOQryUtil<>(MarBasClassVO.class);
		// MarBasClassVO[] qryByPKs = query.qryByPKs(new String[] { id });
		String sql = " select bd_material.pk_material ,bd_marbasclass.pk_marbasclass ,bd_marbasclass.def3 "
				+ " from bd_marbasclass  "
				+ " left join bd_material on bd_material.pk_marbasclass = bd_marbasclass.pk_marbasclass "
				+ " where pk_material ='" + id + "' ";

		Map ids = (Map) this.getQryService().executeQuery(sql,
				new MapProcessor());

		if (null == ids || ids.size() == 0) {
			return null;
		}
		// ids[0]

		// qryByPKs[]
		return ids;
	}

	/**
	 * 查询物料参考成本
	 * 
	 * @throws BusinessException
	 */
	@Override
	public Map<? extends Integer, ? extends UFDouble> doFindPrice_ck(
			Map<Integer, Map<String, String>> map_3) throws BusinessException {
			Map<Integer,UFDouble> rest = new HashMap<Integer,UFDouble>();
			if(null == map_3 || map_3.size() == 0){
				return rest;
			}
			for(Integer key : map_3.keySet()){
				Map<String, String> map = map_3.get(key);
				String sql =" select bd_materialfi.costprice "+
						" from bd_materialfi  where bd_materialfi.pk_material ='"+map.get("pk_material")+"' and bd_materialfi.pk_org ='"+map.get("pk_org")+"' ";
				
				Object executeQuery = this.getQryService().executeQuery(sql, new ColumnProcessor());
				if (null != executeQuery){
					rest.put(key, new UFDouble(executeQuery.toString()));
				}
//				rest.put(key, executeQuery);
			}
		return rest;
	}
}
