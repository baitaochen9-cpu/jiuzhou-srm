package nc.impl.so.m4331.action.maintain.rule.approve;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 质量市场校验规则
 * @author yinsen.zhang
 * @since 2022年5月1日
 */
public class CheckQualityMarketRule implements IRule<DeliveryVO>{

	@Override
	public void process(DeliveryVO[] deliveryVOs) {
		for (DeliveryVO deliveryVO : deliveryVOs) {
			DeliveryHVO head = deliveryVO.getParentVO();
			// 组织
			String pk_org = head.getPk_org();
			// 获取组织参数是否控制质量市场
			String enableFlag = this.getSysInitParamByOrg("YF22004", pk_org);
			
			String YF2200 = this.getSysInitParamByOrg("YF2200*", pk_org);
			//如果交易类型 == 参数YF2200业务参数所选的交易类型，则直接跳过
			if(null != YF2200 && head.getCtrantypeid().equals(YF2200)){
				continue;
			}
			// 如果为空或者N跳过当前控制
			if (StringUtils.isBlank(enableFlag) || StringUtils.equals("N", enableFlag)) {
				continue;
			}
			// 质量市场
			String vdef2 = head.getVdef2();
			// 质量市场为空跳过校验
			if (StringUtils.isBlank(vdef2)) {
				continue;
			}
			// 获取自定义项名称
			String queryQualityMarketSql = "select name from bd_defdoc where dr = 0 and " +
											"pk_defdoc = '" + vdef2 + "'";
			String qualityMarketName = (String) this.querySqlRsColumn(queryQualityMarketSql);
			//替换\n
			qualityMarketName = qualityMarketName.replaceAll("\n", "");
			DeliveryBVO[] bodys = deliveryVO.getChildrenVO();
//			// 批次原料不存在数据
//			ArrayList<String> batchNotFindList = new ArrayList<>();
			// 违法质量市场的批次
			Set<String> vbatchcodes = new HashSet<>();
			for (DeliveryBVO body : bodys) {
				// 批次号
//				String pk_batchcode = body.getPk_batchcode();
				String vbatchcode = body.getVbatchcode();
				String cmaterialid = body.getCmaterialid();
				// 生产厂商 、 原材料
//				String queryBomSql = "select bomb.cproductorid,bomb.cmaterialid " +
//									"	  from mm_wr_product wrp " +
//									"	  left join mm_mo mo " +
//									"	    on mo.cmoid = wrp.vbsrcrowid " +
//									"	   and mo.dr = 0 " +
//									"	  left join bd_bom_b bomb " +
//									"	    on cbomid = mo.cbomversionid " +
//									"	   and bomb.dr = 0 " +
//									"	 where wrp.dr = 0 " +
//									"	   and wrp.vbbatchid = '" + pk_batchcode + "'";

				String queryMaterialSql = "select code from bd_material where dr = 0 " +
						"and pk_material = '" + cmaterialid + "'";
				String materialCode = (String) this.querySqlRsColumn(queryMaterialSql);
				String queryBomSql = "select ylcmaterialvid,pk_supplier,vquality_market,ylvbatchcode,ylbatchpk " + 
									"  from v_yf_trcc " + 
									" start with vbatchcode = '" + vbatchcode + "'" + 
									"        and material_code = '" + materialCode + "' " + 
									" connect by prior ylvbatchcode = vbatchcode";
				List<Map<String, Object>> bomInfos = this.querySqlRsListMap(queryBomSql);
				for (Map<String, Object> bomInfo : bomInfos) {
//					// 生产厂商
					String pk_vendor = (String) bomInfo.get("vquality_market");
					String ylvbatchcode = (String) bomInfo.get("ylvbatchcode");
					String pk_supplier = (String) bomInfo.get("pk_supplier");
					String ylcmaterialvid = (String) bomInfo.get("ylcmaterialvid");
					
					// 校验是否有到货单，有到货单校验，无到货单跳过校验
					String arriveBatchCode = (String) this.querySqlRsColumn("select vbatchcode from po_arriveorder_b where vbatchcode = '" + ylvbatchcode + "'");
					if (StringUtils.isBlank(arriveBatchCode)) {
						continue;
					}
					// 校验物料分类是否是 原料  包装及其他   2801 2804
					String marbasclassCode = (String) this.querySqlRsColumn("select c.code from bd_marbasclass c " +
							"left join bd_material m on m.pk_marbasclass = c.pk_marbasclass " +
							"where m.dr = 0 and c.dr = 0 and m.pk_material = '" + ylcmaterialvid + "'");
					if (!StringUtils.startsWithAny(marbasclassCode, "2801", "2804")) {
						continue;
					}
					
					// 物料编码
					String queryPoMaterialSql = "select code from bd_material where dr = 0 " +
							"and pk_material = '" + ylcmaterialvid + "'";
					String materialPoCode = (String) this.querySqlRsColumn(queryPoMaterialSql);

					// 供应商名称
					String queryPoSupplierSql = "select name from bd_supplier where dr = 0 " +
							"and pk_supplier = '" + pk_supplier + "'";
					String supplierName = (String) this.querySqlRsColumn(queryPoSupplierSql);

					// 生产厂商名称
					String queryPoVendorSql = "select name from bd_defdoc where dr = 0 " +
							"and pk_defdoc = '" + pk_vendor + "'";
					String vendorName = (String) this.querySqlRsColumn(queryPoVendorSql);
					
					String queryMarketQualitySql = "select def1 " + 
												"	  from qc_supplierquality " + 
												"	 where nvl(dr,0) = 0 " + 
												"	   and pk_supplier = '" + pk_supplier + "' " + 
												"	   and pk_material = '" + ylcmaterialvid + "' " + 
												"	   and pk_vendor = '" + pk_vendor + "' ";
					String vquality_market = (String)this.querySqlRsColumn(queryMarketQualitySql);
					if (StringUtils.isBlank(vquality_market)) {
						// 为空不校验
//						batchNotFindList.add(supplierName + "-" + vendorName + "-" + materialPoCode + "(" + ylvbatchcode + ")");
						continue;
					}
					String[] vquality_markets = vquality_market.split("&");
					// 校验质量市场
//					if (!this.equalsAny(qualityMarketName, vquality_markets)) {
					if (this.equalsAny(qualityMarketName, vquality_markets)) {
						vbatchcodes.add(supplierName + "-" + vendorName + "-" + materialPoCode + "(" + ylvbatchcode + ")");
					}
				}
			}
//			if (batchNotFindList.size() > 0) {
//				ExceptionUtils.wrappBusinessException("批次号" + batchNotFindList + "原材料生产厂商的质量市场未找到对应关系！");
//			}
			//查询 组织-生产厂商-原材料-质量市场  如果查询不到信息则校验失败 违反质量市场
			if (vbatchcodes.size() > 0) {
				ExceptionUtils.wrappBusinessException("批次号" + vbatchcodes + "原材料生产厂商的质量市场违反不可销售的质量市场！");
			}
		}
	}
	
	/**
	 * 获取业务参数
	 * @param paramCode
	 * @param pk_org
	 * @return
	 * @throws BusinessException 
	 */
	private String getSysInitParamByOrg(String paramCode, String pk_org) {
		String querySql = "select value from pub_sysinit where dr = 0 and initcode = '"+ 
				paramCode + "' and pk_org = '" + pk_org + "'";
		Object querySqlRsColumn = this.querySqlRsColumn(querySql);
		return (String) querySqlRsColumn;
	}
	
	/**
	 * 查询第一项
	 * @param sql
	 * @return
	 */
	private Object querySqlRsColumn(String sql) {
		try {
			IUAPQueryBS queryBs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Object result = queryBs.executeQuery(sql, new ColumnProcessor());
			return result;
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("语句执行异常：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 查询列表Map
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> querySqlRsListMap(String sql) {
		try {
			IUAPQueryBS queryBs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<Map<String,Object>> result = (List<Map<String,Object>>) queryBs.executeQuery(sql, new MapListProcessor());
			return result;
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("语句执行异常：" + e.getMessage());
		}
		return new ArrayList<>();
	}
	
	private boolean equalsAny(String string, String[] searchStr) {
		if (ArrayUtils.isNotEmpty(searchStr)) {
			for (String itemStr : searchStr) {
				if (StringUtils.equals(string, itemStr)) {
					return true;
				}
			}
		}
		return false;
	}
}
