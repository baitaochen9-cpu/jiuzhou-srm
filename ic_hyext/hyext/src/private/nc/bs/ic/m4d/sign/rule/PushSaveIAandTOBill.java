/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.bs.ic.m4d.sign.rule;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.m4d.Bill4DFinanceProcess;
import nc.bs.ic.pub.base.ICRule;
import nc.bs.trade.business.HYPubBO;
import nc.impl.am.db.DBAccessUtil;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.corg.ICostRegionQryService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.util.mmf.framework.db.SqlInUtil;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.material.MaterialVO;
import nc.vo.corg.CostRegionVO;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WOActualInvVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 
 * @description 材料出库单签字推式生成存货核算的单据
 * @scene 材料出库单签字
 * @param 无
 * @version 2010-6-22 上午09:16:08
 * @since 6.0
 * @author chennn
 */
public class PushSaveIAandTOBill extends ICRule<MaterialOutVO> {

	@Override
	public void process(MaterialOutVO[] vos) {
		try {
			new Bill4DFinanceProcess().processFinance(vos);
			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());

			for (int i = 0; i < vos.length; i++) {

				List<String> list = new ArrayList<>();
				for (MaterialOutBodyVO vo : vos[i].getBodys()) {
					list.add(vo.getCmaterialvid());
				}
				IMaterialPubService materialService = AMProxy
						.lookup(IMaterialPubService.class);
				String[] fields = new String[] { MaterialVO.PK_MATERIAL,
						MaterialVO.PK_MARBASCLASS, MaterialVO.PK_SOURCE,
						MaterialVO.CODE };
				Map<String, MaterialVO> voMap = materialService
						.queryMaterialBaseInfoByPks(
								list.toArray(new String[list.size()]), fields);
				for (int j = 0; j < vos[i].getBodys().length; j++) {
					MaterialOutBodyVO bvo = vos[i].getBodys()[j];
					if(StringUtil.isEmpty(bvo.getCworkordercode())){
						continue;
					}
					//工单主键,如果是非自制的有工单主键,就不需要再反写
					String cworkorderhid = bvo.getCworkorderhid();
					if(StringUtils.isNotEmpty(cworkorderhid) ){
						continue;
					}
					
					String sql = " select pk_wo from ewm_wo a where a.bill_code='"
							+ bvo.getCworkordercode()
							+ "' and dr=0 and pk_org ='"
							+ vos[i].getHead().getPk_org() + "'";

					Map map = (Map) iuap.executeQuery(sql, new MapProcessor());

					if (map != null && map.size() > 0) {
						String pk_wo = map.get("pk_wo").toString();
						BillQuery<AggWorkOrderVO> billquery = new BillQuery(
								AggWorkOrderVO.class);

						AggWorkOrderVO[] aggWorkOrderVO = billquery
								.query(new String[] { pk_wo });
						WOActualInvVO[] vo2 = (WOActualInvVO[]) aggWorkOrderVO[0]
								.getChildren(WOActualInvVO.class);
						WOActualInvVO vo = new WOActualInvVO();
						vo.setCother_billid(pk_wo);
						vo.setCastunitid(bvo.getCastunitid());
						vo.setInout_date(vos[i].getHead().getDbilldate());
						vo.setBill_date(vos[i].getHead().getDbilldate());
						vo.setNassistnum(bvo.getNassistnum());
						vo.setNnum(bvo.getNnum());
						vo.setPrice(bvo.getNcostprice());
						vo.setPrice_org(bvo.getNcostprice());
						vo.setMoney(SafeCompute.multiply(vo.getNnum(),
								vo.getPrice()));
						vo.setMoney_org(SafeCompute.multiply(vo.getNnum(),
								vo.getPrice_org()));
						vo.setPk_group(bvo.getPk_group());
						vo.setPk_org(bvo.getPk_org());
						vo.setPk_org_v(bvo.getPk_org_v());
						vo.setPk_material(bvo.getCmaterialoid());
						vo.setPk_material_v(bvo.getCmaterialoid());
						vo.setPk_measdoc(bvo.getCunitid());
						vo.setPk_stockorg(bvo.getPk_org());
						vo.setPk_stockorg_v(bvo.getPk_org_v());
						vo.setPk_stordoc(bvo.getCbodywarehouseid());
						vo.setSrc_bill_code(vos[i].getHead().getVbillcode());
						vo.setSrc_pk_bill(vos[i].getHead().getCgeneralhid());
						vo.setSrc_bill_type("4D");
						vo.setSrc_pk_bill_b(bvo.getCgeneralbid());
						// vo.setPk_stordoc(newPk_stordoc)
						vo.setStatus(VOStatus.NEW);
						vo.setVchangerate(bvo.getVchangerate());
						vo.setPk_wo(pk_wo);
						vo.setRowno(String.valueOf(vo2.length + 1));
						BaseDAO dao = new BaseDAO();
						dao.insertVO(vo);
						writeBackActualInvCost(vo, voMap);
					}
				}
			}

		} catch (BusinessException ex) {
			ExceptionUtils.wrappException(ex);
		}

	}

	// add by 2021-05-26 更新實際成本和服務成本
	private void writeBackActualInvCost(WOActualInvVO retActualVO,
			Map<String, MaterialVO> voMap) throws BusinessException {
		// 回写物料的服务费用和材料费用 UpRewriteWorkOrder
		// ??
		// 先区分存货是服务还是材料，根据存货分类的编码09默认服务，其他为材料

		HYPubBO bo = new HYPubBO();
		// 服务类存货取材料出库单的单价
		// 材料类型的存货取最新成本价
		Map<String, UFDouble> materialMap = new HashMap<String, UFDouble>();
		Map<String, UFDouble> serviceMap = new HashMap<String, UFDouble>();
		MaterialVO vo1 = voMap.get(retActualVO.getPk_material());
		if (vo1 == null)
			throw new BusinessException("物料信息出错，请检查！");
		String str = (String) bo.findColValue(
				"bd_marbasclass",
				"code ",
				" nvl(dr,0) = 0 and pk_marbasclass = '"
						+ vo1.getPk_marbasclass() + "'");
		if (str.startsWith("09")) {// 服務類

			UFDouble price = getMaterialPrice(retActualVO.getPk_material(),
					retActualVO.getSrc_pk_bill_b());
			if (retActualVO.getPrice() != null) {
				price = retActualVO.getPrice();
			}
			calMny(serviceMap, price, retActualVO);
			updateNmny(new WOActualInvVO[] { retActualVO });
			updateHeadNmny1(serviceMap, "def4");// 实际fuwu成本
		} else {
			UFDouble price = getCostPrice(retActualVO.getPk_material(),
					retActualVO.getPk_stockorg(), retActualVO.getPk_group(),retActualVO.getPk_stordoc());
			// price = UFDouble.ZERO_DBL;
			if (UFDouble.ZERO_DBL.equals(price) || price == null) {
				price = (UFDouble) bo.findColValue("bd_materialfi",
						"planprice  ", " nvl(dr,0) = 0 and pk_material  = '"
								+ vo1.getPk_material() + "'");

			}
			if (UFDouble.ZERO_DBL.equals(price) || price == null) {
				price = retActualVO.getPrice();
			}
			calMny(materialMap, price, retActualVO);
			updateNmny(new WOActualInvVO[] { retActualVO });
			updateHeadNmny(materialMap, "ac_mtr_mny_org");// 实际物料成本
		}
	}

	private void updateNmny(WOActualInvVO[] retActualVOs)
			throws BusinessException {
		String sql = " update ewm_wo_actual_inv set money=?,money_org=?,price=?,price_org=? where pk_wo_actual_inv=? and dr=0 ";
		List<SQLParameter> sqlparaList = new ArrayList();
		for (int i = 0; i < retActualVOs.length; i++) {
			SQLParameter sqlpara = new SQLParameter();
			sqlpara.addParam(retActualVOs[i].getMoney());
			sqlpara.addParam(retActualVOs[i].getMoney_org());
			sqlpara.addParam(retActualVOs[i].getPrice());
			sqlpara.addParam(retActualVOs[i].getPrice_org());
			sqlpara.addParam(retActualVOs[i].getPk_wo_actual_inv());
			sqlparaList.add(sqlpara);
		}
		if (sqlparaList.size() > 0) {
			try {
				new DBAccessUtil().batchUpdate(sql,
						(SQLParameter[]) sqlparaList
								.toArray(new SQLParameter[sqlparaList.size()]));
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
		}
	}

	private void updateHeadNmny1(Map<String, UFDouble> serviceMap,
			String itemkey) throws BusinessException {
		String sql = " update ewm_wo set  " + itemkey
				+ " = to_number(replace(" + itemkey + ",'~',0 )) + ? where pk_wo= ? and dr=0 ";
		String sql1 = " update ewm_wo set ac_ttl_mny_org = ac_ttl_mny_org + ? where pk_wo=? and dr=0 ";
		List<SQLParameter> sqlparaList = new ArrayList();
		for (Map.Entry<String, UFDouble> entry : serviceMap.entrySet()) {
			SQLParameter sqlpara = new SQLParameter();
			sqlpara.addParam(entry.getValue());
			sqlpara.addParam(entry.getKey());
			sqlparaList.add(sqlpara);
		}
		if (sqlparaList.size() > 0) {
			try {
				new DBAccessUtil().batchUpdate(sql,
						(SQLParameter[]) sqlparaList
								.toArray(new SQLParameter[sqlparaList.size()]));
				new DBAccessUtil().batchUpdate(sql1,
						(SQLParameter[]) sqlparaList
								.toArray(new SQLParameter[sqlparaList.size()]));
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
		}
	}

	private void updateHeadNmny(Map<String, UFDouble> serviceMap, String itemkey)
			throws BusinessException {
		String sql = " update ewm_wo set  " + itemkey + " = to_number("
				+ itemkey + ") + ? where pk_wo=? and dr=0 ";
		String sql1 = " update ewm_wo set ac_ttl_mny_org = ac_ttl_mny_org + ? where pk_wo=? and dr=0 ";
		List<SQLParameter> sqlparaList = new ArrayList();
		for (Map.Entry<String, UFDouble> entry : serviceMap.entrySet()) {
			SQLParameter sqlpara = new SQLParameter();
			sqlpara.addParam(entry.getValue());
			sqlpara.addParam(entry.getKey());
			sqlparaList.add(sqlpara);
		}
		if (sqlparaList.size() > 0) {
			try {
				new DBAccessUtil().batchUpdate(sql,
						(SQLParameter[]) sqlparaList
								.toArray(new SQLParameter[sqlparaList.size()]));
				new DBAccessUtil().batchUpdate(sql1,
						(SQLParameter[]) sqlparaList
								.toArray(new SQLParameter[sqlparaList.size()]));
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
		}
	}

	private void calMny(Map<String, UFDouble> materialMap, UFDouble price,
			WOActualInvVO vo) {
		UFDouble mny = UFDouble.ZERO_DBL;
		mny = SafeCompute.multiply(price, vo.getNnum());
		vo.setMoney(mny);
		vo.setMoney_org(mny);
		vo.setPrice(price);
		vo.setPrice_org(price);
		if (materialMap.containsKey(vo.getPk_wo())) {
			mny = SafeCompute
					.add(vo.getMoney(), materialMap.get(vo.getPk_wo()));
		}
		materialMap.put(vo.getPk_wo(), mny);
	}

	private UFDouble getCostPrice(String pk_material, String pk_org,
			String pk_group,String pk_stordoc) throws BusinessException {
		// String sql =
		// " select b.nabprice from  ia_monthnab b where b.cinventoryid ='"+pk_material+"' and b.pk_org ='"+pk_org+"' and nvl(b.dr,0) = 0 order by ts desc;";
		// List<UFDouble> list =(List<UFDouble>) new
		// DBAccessUtil().executeQuery(sql, new ColumnListProcessor());
		// UFDouble price = UFDouble.ZERO_DBL;
		// if(list != null && list.size()>0){
		// price = list.get(0);
		// }
		String[] cmaterialoids = new String[] { pk_material };
		Map<String, String> qryParam = new HashMap<String, String>();
		qryParam.put("pk_org", pk_org);
		qryParam.put("pk_group", pk_group);
		qryParam.put("pk_stordoc", pk_stordoc);
		List<Map> list = dealMonthnab(qryParam, cmaterialoids);
		if (list.isEmpty() || list.size() == 0) {
			return UFDouble.ZERO_DBL;
		} else {
			UFDouble price = UFDouble.ZERO_DBL;
			Object o = list.get(0).get("nprice");
			if (o instanceof BigDecimal) {
				price = new UFDouble(((BigDecimal) o));
			} else {
				price = (UFDouble) o;
			}
			return price;
		}
	}

	/**
	 * 根据指定物料(可以为空，为空则查询所有的物料) 查询最新月结存信息 查询最新结存月的信息
	 * 
	 * @param qryParam
	 * @param cmaterialoids
	 * @return
	 * @throws BusinessException
	 */
	public List<Map> dealMonthnab(Map qryParam, String[] cmaterialoids)
			throws BusinessException {

		StringBuffer sql = new StringBuffer();
		sql.append("select caccountperiod as dbilldate, cinventoryid as  cmaterialoid,");
		sql.append(" nabnum, nabprice as nprice , nabmny");
		sql.append(" from  ia_monthnab h ");
		sql.append(" where nvl(h.dr, 0) = 0");
		// 最大月份的会计期间
		sql.append(" and caccountperiod =( ");
		sql.append(" select max(caccountperiod)  from ia_monthnab");
		sql.append(" where nvl(dr, 0) = 0 ");
		sql.append(" and pk_group = '" + qryParam.get("pk_group") + "'");
		// 库存组织---成本域
		sql.append(" and pk_org = " +"'" + getCostOrg(qryParam.get("pk_org").toString(), qryParam.get("pk_org").toString(),qryParam.get("pk_stordoc").toString()) + "'");//修改
		// 本月之前的期间--当前业务场景应该不需要，如果需要，请注意会计月的转换
		sql.append(" )");
		// 过滤物料
		if (cmaterialoids != null && cmaterialoids.length > 0) {
			sql.append(" and cinventoryid " + getMaterialsIn(cmaterialoids));
		}
		sql.append(" and h.pk_group = '" + qryParam.get("pk_group") + "'");
		// 成本域
		sql.append(" and pk_org = " +"'" + getCostOrg(qryParam.get("pk_org").toString(), qryParam.get("pk_org").toString() , qryParam.get("pk_stordoc").toString()) + "'");//修改

		sql.append(" and abs(nabprice)>0");

		List<Map> list = (List<Map>) new DBAccessUtil().executeQuery(
				sql.toString(), new MapListProcessor());
		return list;
	}

	/**
	* 根据库存组织查询成本域
	* 
	* @param pk_stockorg
	* @return
	 * @throws BusinessException 
	*/
	String getCostOrg(String pk_org,String pk_stockorg,String pk_stordoc) throws BusinessException {
		ICostRegionQryService quer = NCLocator.getInstance().lookup(ICostRegionQryService.class);
		CostRegionVO[] query = quer.queryCostRegionVOsByClause( " nvl(dr,0)=0 and pk_org='"+pk_stockorg+"' and layertype = 1 " +
				" and pk_costregion in (select pk_costregion from org_cr_stockorg where pk_stockorg = '" + pk_stordoc +"' and nvl(dr,0)=0)");
		CostRegionVO[] query2 = quer.queryCostRegionVOsByClause(" nvl(dr,0)=0 and pk_org='"+pk_stockorg+"' and layertype = 2  " +
				" and pk_costregion in (select pk_costregion from org_cr_stockstore where pk_storage = '" + pk_stordoc + "' and nvl(dr,0)=0)");
		List<String> keys  = new ArrayList<String>();
		//成本域对应层级为 库存组织+仓库
		 if (query2 !=null){
			 if(  query2.length > 1){
				 throw new BusinessException("检查出仓库参与多个成本域设置，无法进行计划发料！ ");
			 }
				 return  query2[0].getPk_costregion();		 
		 }
		 else if (null != query ){//成本域对应层级为 库存组织,且有多个库存组织
			 if(query.length > 1){	
				 throw new BusinessException("检查出成本域对应多个库存组织，无法进行计划发料！ ");
			 }
			 return  query[0].getPk_costregion();	
			 
		 }
		 CostRegionVO[] queryCostRegionVOsByClause = quer.queryCostRegionVOsByClause( "nvl(dr,0)=0  and pk_org='" + pk_stockorg + "'");
		 if(null!= queryCostRegionVOsByClause && queryCostRegionVOsByClause.length > 0){
			 
			 String pk_costregion = queryCostRegionVOsByClause[0].getPk_costregion();
			 return pk_costregion;
		 }
		return pk_stordoc;
	}

	public UFDouble getUFDoubleNullASZero(UFDouble value) {
		if (value == null || "".equals(value)) {
			return UFDouble.ZERO_DBL;
		}
		return value;
	}

	String getMaterialsIn(String[] cmaterialoids) throws BusinessException {

		SqlInUtil util = new SqlInUtil(cmaterialoids);
		return util.getInSql();
	}
	
	private UFDouble getMaterialPrice(String pk_material, String csourcebid)
			throws DAOException {
		String sql = " select pb.nprice from ic_material_b b join ic_purchasein_b pb on b.csourcebillbid = pb.cgeneralbid   where b.cgeneralbid = '"
				+ csourcebid + "' and nvl(b.dr, 0 ) = 0 ";
		BigDecimal price = (BigDecimal) new DBAccessUtil().executeQuery(sql,
				new ColumnProcessor());
		UFDouble uprice = UFDouble.ZERO_DBL;
		if(price != null){
			 uprice = new UFDouble(price.doubleValue());
		}
		return uprice;
	}

}
