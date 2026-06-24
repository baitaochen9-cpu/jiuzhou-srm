package nc.bs.qc.supplierqualitystatus.ace.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Log;
import nc.bs.trade.business.HYPubBO;
import nc.impl.am.db.processor.BeanListMapProcessor;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.bd.supplier.suppliergradesys.SupplierGradeVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class AceSupplierqualitystatusCheckBP {

	public void checkStatus(Map<String, String> pk_material_vs,
			String pk_group, String pk_org, String pk_supplier,
			String pk_billtype, String supgradestatus) {

		try {
			
			boolean bparam = getParam(pk_org, "YF621");
			if (!bparam) {
				return;
			}
			
			if ("采购".equals(pk_billtype)) {
				bparam = getParam(pk_org, "YF622");
				if (bparam) {
					return;
				}
			}
			
			BaseDAO dao = new BaseDAO();
			Map<String, List<SupplierGradeVO>> map = getSupplierGrade(dao);
			// 维持状态 不允许采购
			String strWhere = " pk_supplier = '" + pk_supplier + "'  and pk_org ='"+ pk_org+"'";
			List<SupStockVO> list = (List<SupStockVO>) dao.retrieveByClause(
					SupStockVO.class, strWhere, new String[] { "supgrade" });
			if (list != null && list.size() > 0)
				checkQualiStatus(supgradestatus, pk_billtype, list.get(0)
						.getSupgrade(), map, "供应商的质量状态不允许" + pk_billtype + "！");

			SqlBuilder builder = new SqlBuilder();
			builder.append("select * from qc_supplierquality where nvl(dr,0)=0 ");
			builder.append(" and pk_group", pk_group);
			builder.append(" and pk_org", pk_org);
			builder.append(" and pk_supplier", pk_supplier);
			List<SupplierqualityHVO> volist = (List<SupplierqualityHVO>) dao
					.executeQuery(builder.toString(), new BeanListProcessor(
							SupplierqualityHVO.class));
			if (volist == null || volist.size() == 0)
				volist = new ArrayList<>();
			Map<String, List<SupplierqualityHVO>> retMap = new HashMap<String, List<SupplierqualityHVO>>();
			for (SupplierqualityHVO vo : volist) {
				String key = vo.getPk_material() + "&" + vo.getPk_vendor();
				List<SupplierqualityHVO> slist = null;
				if (retMap.containsKey(key)) {
					slist = retMap.get(key);
				} else {
					slist = new ArrayList<SupplierqualityHVO>();
				}
				slist.add(vo);
				retMap.put(key, slist);
			}

			if (pk_material_vs == null || pk_material_vs.size() == 0)
				return;
			Map<String, MaterialVO> materialVos = new HashMap<String, MaterialVO>();

			IMaterialPubService materialService = AMProxy
					.lookup(IMaterialPubService.class);

			String[] fields = new String[] { MaterialVO.PK_MARBASCLASS,
					MaterialVO.PK_MATERIAL, // 物料版本id
					MaterialVO.PK_SOURCE, MaterialVO.CODE, MaterialVO.DEF1 // 物料编码
			};

			Set<String> materSet = pk_material_vs.keySet();
			materialVos = materialService.queryMaterialBaseInfoByPks(
					materSet.toArray(new String[0]), fields);

			HYPubBO bo = new HYPubBO();

			for (String pk_material : materSet) {

				MaterialVO vo = materialVos.get(pk_material);
				if (vo == null)
					throw new BusinessException("物料信息出错，请检查！");
				String str = (String) bo.findColValue(
						"bd_materialpu",
						"def1",
						" nvl(dr,0) = 0 and pk_material = '"
								+ vo.getPk_material() + "' and pk_org ='"
								+ pk_org + "'");
				if ("Y".equalsIgnoreCase(str)) {
					if (retMap == null || retMap.size() == 0)
						throw new BusinessException("生产厂商物料状态未设置，请检查！");
					String key = pk_material + "&"
							+ pk_material_vs.get(pk_material);
					List<SupplierqualityHVO> list1 = retMap.get(key);
					if (list1 == null || list1.size() == 0)
						throw new BusinessException("生产厂商物料状态未设置，请检查！");
					//
					for (SupplierqualityHVO vo1 : list1) {
						checkQualiStatus(supgradestatus, pk_billtype,
								vo1.getPk_grade_info(), map,
								"物料" + vo.getCode() + "的生产商状态不允许" + pk_billtype
										+ "！");
					}

				}
			}
		} catch (BusinessException e) {
			// 日志异常
			Log.getInstance(this.getClass()).error(e);
			ExceptionUtils.wrappException(e);
		}
	}

	private boolean getParam(String pk_org, String paramCode) {
		boolean param = false;
		if (!StringUtil.isEmpty(paramCode)) {
			try {
				param = SysinitAccessor.getInstance()
						.getParaBoolean(pk_org, paramCode).booleanValue();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return param;
	}

	private Map<String, List<SupplierGradeVO>> getSupplierGrade(BaseDAO dao)
			throws DAOException {
		SqlBuilder builder = new SqlBuilder();
		builder.append("select * from bd_supplier_grade where nvl(dr,0)=0 ");

		Map<String, List<SupplierGradeVO>> retMap = (Map<String, List<SupplierGradeVO>>) dao
				.executeQuery(builder.toString(), new BeanListMapProcessor<>(
						"pk_suppliergrade", SupplierGradeVO.class));
		return retMap;
	}

	private void checkQualiStatus(String supgradestatus, String pk_billtype,
			String supgrade, Map<String, List<SupplierGradeVO>> map, String msg)
			throws BusinessException {

		if (map == null || map.size() == 0)
			return;

		if (StringUtil.isEmpty(supgrade))
			return;
		for (Map.Entry<String, List<SupplierGradeVO>> entry : map.entrySet()) {
			List<SupplierGradeVO> list = entry.getValue();
			if (list == null || list.size() == 0)
				continue;

			for (SupplierGradeVO vo : list) {
				if (supgrade.equals(vo.getPk_grade_info())) {
					// if (supgradestatus.equals(vo.getSuppliergrade())) {
					if (getIsControl(pk_billtype, vo)) {
						throw new BusinessException(msg);
					}
					// }
				}
			}
		}
	}

	private boolean getIsControl(String billtype, SupplierGradeVO vo) {
		boolean flag = false;
		if ("收货".equals(billtype)) {
			if ("Y".equals(vo.getDef1())) {
				flag = true;
			}
		} else if ("入库".equals(billtype)) {
			if ("Y".equals(vo.getDef2())) {
				flag = true;
			}
		} else if ("采购".equals(billtype)) {
			if ("Y".equals(vo.getDef3())) {
				flag = true;
			}
		} else {
			flag = true;
		}
		return flag;
	}
}
