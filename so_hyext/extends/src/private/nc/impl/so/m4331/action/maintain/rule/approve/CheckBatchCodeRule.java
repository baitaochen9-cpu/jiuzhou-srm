package nc.impl.so.m4331.action.maintain.rule.approve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;

import org.apache.commons.lang3.StringUtils;

/**
 * 批次号校验规则
 * @author yinsen.zhang
 * @since 2022年5月1日
 */
public class CheckBatchCodeRule implements IRule<DeliveryVO>{

	@Override
	public void process(DeliveryVO[] deliveryVOs) {
		for (DeliveryVO deliveryVO : deliveryVOs) {
			// 没有审批的物料批次
//			ArrayList<String> unApproveBatch = new ArrayList<>();
			LinkedHashSet<String> unApproveBatch = new LinkedHashSet<>();
			
			DeliveryHVO head = deliveryVO.getParentVO();
			String pk_org = head.getPk_org();
			// 获取组织参数是否控制批次号
			String enableFlag = this.getSysInitParamByOrg("YF22003", pk_org);
			String YF2200 = this.getSysInitParamByOrg("YF2200*", pk_org);
			//如果交易类型 == 参数YF2200业务参数所选的交易类型，则直接跳过
			if(null != YF2200 && head.getCtrantypeid().equals(YF2200)){
				continue;
			}
			// 如果为空或者N跳过当前控制
			if (StringUtils.isBlank(enableFlag) || StringUtils.equals("N", enableFlag)) {
				continue;
			}
			DeliveryBVO[] bodys = deliveryVO.getChildrenVO();
			for (DeliveryBVO body : bodys) {
				String cmaterialid = body.getCmaterialid();
				// 批次号
				String pk_batchcode = body.getPk_batchcode();
				String vbatchcode = body.getVbatchcode();
//				String checkBatchCodeSql = "select vdef5 from scm_batchcode where dr = 0 " +
//						"and pk_batchcode = '"+ pk_batchcode +"'";
				String checkBatchCodeSql = "select b.vdef5, d.name vdef7 from scm_batchcode b " +
						"left join bd_defdoc d on b.vdef7 = d.pk_defdoc where b.dr = 0 " +
						"and b.pk_batchcode = '"+ pk_batchcode +"'";
//				String batchCodeRes = (String) this.querySqlRsColumn(checkBatchCodeSql);
				
				Map<String, String> checkBatchCodeResMap = this.querySqlRsMap(checkBatchCodeSql);
				
				if(null == checkBatchCodeResMap || checkBatchCodeResMap.size() == 0) {
					ExceptionUtils.wrappBusinessException(" 请检查批次号审核信息或处理意见！ 当下未查询到相关数据。");
				}
				String vdef5 = checkBatchCodeResMap.get("vdef5");
				String vdef7 = checkBatchCodeResMap.get("vdef7");
				
				if (StringUtils.isBlank(vdef5) || StringUtils.equals("N", vdef5)) {
					unApproveBatch.add(vbatchcode);
//					ExceptionUtils.wrappBusinessException(vbatchcode + "批次物料不是审核态！");
				}
				if (!StringUtils.equals("放行", vdef7)) {
					unApproveBatch.add(vbatchcode);
				}
				// 【原材料投料批次报表 数据依赖】 处理
				String queryMaterialSql = "select code from bd_material where dr = 0 " +
						"and pk_material = '" + cmaterialid + "'";
				String materialCode = (String) this.querySqlRsColumn(queryMaterialSql);
//				String queryYLSql = "select ylbatchpk,ylvbatchcode " + 
//									"  from v_yf_trcc " + 
//									" start with vbatchcode = '" + vbatchcode + "'" + 
//									"        and material_code = '" + materialCode + "' " + 
//									"        and fbproducttype = '主产品' " + 
//									" connect by prior ylvbatchcode = vbatchcode";

				String queryYLSql = "   select vbatchcode ylvbatchcode,cbatchid ylbatchpk,cmaterialid ylcmaterialid from mm_mo " +
									"	  where dr = 0 and vbatchcode in ( "+
									"      select distinct vbatchcode from v_yf_trcc "+
									"      start with vbatchcode = '" + vbatchcode + "'  "+
									"      and material_code = '" + materialCode + "'  "+
									"      connect by prior ylvbatchcode = vbatchcode "+
									"  ) group by vbatchcode,cbatchid,cmaterialid";
				List<Map<String, Object>> ylBatchCode = this.querySqlRsListMap(queryYLSql);
				for (Map<String, Object> batchItem : ylBatchCode) {

					String ylbatchpk = (String) batchItem.get("ylbatchpk");
					String ylvbatchcode = (String) batchItem.get("ylvbatchcode");
					String ylcmaterialid = (String) batchItem.get("ylcmaterialid");
					
					/********************************2024.07.09 bbt **********************************************************/
					/*对于重复批次号的情况，原逻辑只获取批次号可能无法准确匹配唯一数据，新逻辑通过物料id+批次号保证获取的数据唯一*/
					String queryMaterialClsAndDefSql = "select mc.code,b.vbatchcode,b.vdef5,d.name vdef7 from bd_material m " +
							"						left join bd_marbasclass mc on mc.pk_marbasclass = m.pk_marbasclass" +
							"						left join scm_batchcode b on b.cmaterialoid = m.pk_material" + 
							"						left join bd_defdoc d on b.vdef7 = d.pk_defdoc where b.dr = 0 " +
							" 						and m.dr = 0 and b.vbatchcode = '" + ylvbatchcode + "'" + 
							"						and b.cmaterialoid = '" + ylcmaterialid + "';";
					Map<String, String> batchCodeByCodeResMap = this.querySqlRsMap(queryMaterialClsAndDefSql);
					//物料分类编码
					String materialClsCode = batchCodeByCodeResMap.get("code");
					//批次档案的是否审批
					String fvdef5 = batchCodeByCodeResMap.get("vdef5");
					//批次档案的放行意见
					String fvdef7 = batchCodeByCodeResMap.get("vdef7");
					
					/*************************************************************************************************/
					
//					String queryMaterialClsSql = "select mc.code from bd_material m " +
//							"						left join bd_marbasclass mc on mc.pk_marbasclass = m.pk_marbasclass" +
//							"						left join scm_batchcode b on b.cmaterialoid = m.pk_material" + 
////							" 						where m.dr = 0 and b.pk_batchcode = '" + ylbatchpk + "'";
//							" 						where m.dr = 0 and b.vbatchcode = '" + ylvbatchcode + "'" + 
//							"						and b.cmaterialoid = '" + ylcmaterialid + "';";
//					
//					String materialClsCode = (String) this.querySqlRsColumn(queryMaterialClsSql);
					//物料分类不是中间体跳过
					if (!StringUtils.equals("280301", materialClsCode)) {
						continue;
					}
//					if (StringUtils.isBlank(ylbatchpk)) {
					if (StringUtils.isBlank(ylvbatchcode)) {
						continue;
					}
//					// 批次号是否可以加个主键 怕有重复的
////					String checkBatchCodeByCodeSql = "select vdef5 from scm_batchcode where dr = 0 " +
////							"and pk_batchcode = '"+ ylbatchpk +"'";
//					String checkBatchCodeByCodeSql = "select b.vdef5, d.name vdef7 from scm_batchcode b " +
//							"left join bd_defdoc d on b.vdef7 = d.pk_defdoc where b.dr = 0 " +
////							"and b.pk_batchcode = '"+ ylbatchpk +"'";
//							"and b.vbatchcode = '"+ ylvbatchcode +"'";
////					String batchCodeByCodeRes = (String) this.querySqlRsColumn(checkBatchCodeByCodeSql);
//
//					Map<String, String> batchCodeByCodeResMap = this.querySqlRsMap(checkBatchCodeByCodeSql);
//					String fvdef5 = batchCodeByCodeResMap.get("vdef5");
//					String fvdef7 = batchCodeByCodeResMap.get("vdef7");
					if (StringUtils.isBlank(fvdef5) || StringUtils.equals("N", fvdef5)) {
//						unApproveBatch.add(vbatchcode);
						unApproveBatch.add(ylvbatchcode);
//						ExceptionUtils.wrappBusinessException(batchItem + "批次物料不是审核态！");
					}
					// 取消校验放行
//					if (!StringUtils.equals("放行", fvdef7)) {
//						unApproveBatch.add(ylvbatchcode);
//					}
				}
			}
			if (unApproveBatch.size() > 0) {
				ExceptionUtils.wrappBusinessException("以下批次物料不是审核态或处理意见不是放行：" + unApproveBatch.toString() + "！");
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
	
	
	@SuppressWarnings("rawtypes")
	private List querySqlRsColumnList(String sql) {
		try {
			IUAPQueryBS queryBs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List result = (List) queryBs.executeQuery(sql, new ColumnListProcessor());
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
	
	/**
	 * 查询列表Map
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String,String> querySqlRsMap(String sql) {
		try {
			IUAPQueryBS queryBs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Map<String,String> result = (Map<String,String>) queryBs.executeQuery(sql, new MapProcessor());
			return result;
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("语句执行异常：" + e.getMessage());
		}
		return new HashMap<>();
	}
}
