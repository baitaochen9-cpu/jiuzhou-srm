package nc.bs.qc.supplierqualityapply.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.itf.qc.ISupplierqualityapplyMaintain;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.ui.pub.print.IDataSource;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

import org.jfree.util.Log;

/**
 * 物料状态变更
 */
public class AutoCreateSupplierQualityApplyPlugin implements
		IBackgroundWorkPlugin {

	// 失活控制周期(年)
	private static final String YEARNUM = "yearnum";
	// 周期单位
	private static final String UNIT = "periods_unit";
	// 物料
	private static final String MATERIAL = "material";
	// 变更后状态
	private static final String AFTERSTATUS = "after_qualitylevel";

	@Override
	public PreAlertObject executeTask(BgWorkingContext context)
			throws BusinessException {
		Log.debug("AutoSendMsgForLabelPrint后台任务执行开始");
		// 参数处理
		// String pk_group = context.getGroupId();
		String[] pk_orgs = context.getPk_orgs();
		HashMap<String, Object> params = context.getKeyMap();
		Object yearnum1 = params.get(YEARNUM);
		int yearnum = yearnum1 == null ? 0 : ValueUtils.getInt(yearnum1);
		if (yearnum <= 0) {
			throw new BusinessException("后台任务参数错误：【失活控制周期】不可小于0！！！");
		}

		Object periodUnit = params.get(UNIT);
		int unit = periodUnit == null ? 0 : ValueUtils.getInt(periodUnit);
		if (unit < 0) {
			throw new BusinessException("后台任务参数错误：【周期单位】不可小于0！！！");
		}

		Object material = params.get(MATERIAL);
		// if (material == null) {
		// throw new BusinessException("后台任务参数错误：物料不能空！！！");
		// }else{

		String[] pk_materials = null;
		if (material != null) {
			pk_materials = ((String) material).split(",");
		}

		Object afterstatus = params.get(AFTERSTATUS);
		if (afterstatus == null) {
			throw new BusinessException("后台任务参数错误：质量状态不能空！！！");
		}
		String after_qualitylevel = (String) afterstatus;

		AggSupplierQualityApplyVO[] bills = null;
		try {
			ISupplierqualityapplyMaintain applyMaintain = AMProxy
					.lookup(ISupplierqualityapplyMaintain.class);
			bills = applyMaintain.createApply(pk_orgs, yearnum,unit, pk_materials,
					after_qualitylevel);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}

		if (bills == null || bills.length == 0) {
			return getNothingObject();
		} else {
			return getReturnObject(bills);
		}

	}

	private PreAlertObject getNothingObject() {
		PreAlertObject resultObject = new PreAlertObject();
		resultObject.setReturnObj("没有需要执行的任务");
		resultObject.setReturnType(PreAlertReturnType.RETURNNOTHING);
		return resultObject;
	}

	protected PreAlertObject getReturnObject(AggSupplierQualityApplyVO[] bills)
			throws BusinessException {
		PreAlertObject resultObject = new PreAlertObject();
		SupplierQualityApplyVODataSource datasource = new SupplierQualityApplyVODataSource();
		resultObject.setReturnObj(datasource);
		resultObject.setReturnType(PreAlertReturnType.RETURNDATASOURCE);

		Set<String> pk_materials = new HashSet<>();
		Set<String> pk_suppliers = new HashSet<>();
		for (int i = 0; i < bills.length; i++) {
			pk_materials.add(bills[i].getParentVO().getPk_material());
			pk_suppliers.add(bills[i].getParentVO().getPk_supplier());
		}
		IMaterialPubService service = NCLocator.getInstance().lookup(
				IMaterialPubService.class);
		Map<String, MaterialVO> materialMap = service
				.queryMaterialBaseInfoByPks(
						pk_materials.toArray(new String[0]), new String[] {
								MaterialVO.CODE, MaterialVO.NAME });

		List<String> materialpks = new ArrayList<>();
		List<String> materialcodes = new ArrayList<>();
		List<String> materialnames = new ArrayList<>();
		for (int i = 0; i < bills.length; i++) {
			String pk_material = bills[i].getParentVO().getPk_material();
			String pk_supplier = bills[i].getParentVO().getPk_supplier();
			materialpks.add(pk_material);
			materialcodes.add(materialMap.get(pk_material).getCode());
			materialnames.add(materialMap.get(pk_material).getName());
		}
		datasource.setMaterialCode(materialcodes.toArray(new String[0]));
		datasource.setMaterialName(materialnames.toArray(new String[0]));
		datasource.setPKMaterialCode(materialpks.toArray(new String[0]));

		return resultObject;
	}

	public class SupplierQualityApplyVODataSource implements IDataSource {

		private static final long serialVersionUID = 8543547808997920885L;

		private static final String PK_MATERIAL = "pk_material";
		private static final String MATERIALCODE = "materialcode";
		private static final String MATERIALNAME = "materialname";

		private HashMap<String, String[]> dataMap = new HashMap<>();

		public void setPKMaterialCode(String[] values) {
			this.dataMap.put(PK_MATERIAL, values);
		}

		public void setMaterialCode(String[] values) {
			this.dataMap.put(MATERIALCODE, values);
		}

		public void setMaterialName(String[] values) {
			this.dataMap.put(MATERIALNAME, values);
		}

		@Override
		public String[] getItemValuesByExpress(String itemExpress) {
			return this.dataMap.get(itemExpress);
		}

		@Override
		public boolean isNumber(String itemExpress) {
			return false;
		}

		@Override
		public String[] getDependentItemExpressByExpress(String itemExpress) {
			return null;
		}

		@Override
		public String[] getAllDataItemExpress() {
			return new String[] { PK_MATERIAL, MATERIALCODE, MATERIALNAME };
		}

		@Override
		public String[] getAllDataItemNames() {
			return null;
		}

		@Override
		public String getModuleName() {
			return null;
		}

	}

}
