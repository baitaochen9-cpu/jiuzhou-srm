package nccloud.pubimpl.ct.purdaily.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.ct.purdaily.IPuContrl;
import nc.itf.scmpub.reference.uap.org.PurchaseOrgForScopeService;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtScopeVo;
import nc.vo.ct.uitl.ArrayUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ml.MultiLangContext;
import nc.vo.org.PurchaseOrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.pubitf.ct.purdaily.service.IControlScope;

/**
 * @description 合同控制范围
 * @author xiahui
 * @date 创建时间：2019-3-7 下午2:01:32
 * @version ncc1.0
 **/
public class ControlScopeImpl implements IControlScope {

	@Override
	public Map<String, Object> getCtrlScopeList(AggCtPuVO bill) throws BusinessException {
		// 合同控制范围
		CtScopeVo[] scopeVos = NCLocator.getInstance().lookup(IPuContrl.class)
				.queryCtScope(bill.getParentVO().getPk_ct_pu());
		this.checkCtScopeVo(scopeVos);

		boolean isDisabled = !this.isCtrlScopeEditable(bill);
		return this.initCtCtrlScope(scopeVos, isDisabled);
	}

	@Override
	public void updateCtrlScope(String id, String[] sCtrlScopeNew) throws BusinessException {
		// 远程调用
		IPuContrl service = NCLocator.getInstance().lookup(IPuContrl.class);
		CtScopeVo[] vos = service.queryCtScope(id);

		String[] sCtrlScopeOld = new String[vos.length];
		for (int i = 0; i < vos.length; i++) {
			sCtrlScopeOld[i] = vos[i].getPk_scopeorg();
		}
		// 合同控制范围更新
		service.updateCtCtrlScope(id, sCtrlScopeNew, sCtrlScopeOld, vos);
	}

	@Override
	public Map<String, Object> getBatchCtrlScopeList(AggCtPuVO[] bills) throws BusinessException {
		List<String> pk_ct_pus = new ArrayList<String>(); // 可修改的控制范围主键
		for (AggCtPuVO bill : bills) {
			if (this.isBatchCtrlScopeEditable(bill)) {
				pk_ct_pus.add(bill.getParentVO().getPk_ct_pu());
			}
		}
		if (pk_ct_pus.size() <= 0) {
			ExceptionUtils.wrappBusinessException(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0053")); // 请选择需要查看或修改合同控制范围的合同!
		}

		// 查询合同控制范围
		Map<String, List<String>> vosmap = NCLocator.getInstance().lookup(IPuContrl.class)
				.queryCtsScope(pk_ct_pus.toArray(new String[pk_ct_pus.size()]));
		// 除合同删除外 其他都可查询出控制范围
		if (vosmap == null || vosmap.isEmpty()) {
			ExceptionUtils.wrappBusinessException(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0340")); // 合同可能已经被删除，请刷新合同
		}

		return this.initCtBatchCtrlScope(pk_ct_pus);
	}

	@Override
	public void addBatchCtrlScope(String[] pks, String[] sCtrlScopeNew) throws BusinessException {
		Map<String, List<String>> ctscopemap = NCLocator.getInstance().lookup(IPuContrl.class).queryCtsScope(pks); // 查询合同控制范围

		List<CtScopeVo> insertscopevos = new ArrayList<CtScopeVo>(); // 需插入的合同控制范围
		for (String pk : pks) {// 批选合同
			List<String> ctscopelist = ctscopemap.get(pk);// 旧合同范围
			for (String scopeorg : sCtrlScopeNew) {// 新合同范围
				// 加入新增组织
				if (!ctscopelist.contains(scopeorg)) {
					CtScopeVo vo = new CtScopeVo();
					vo.setPk_ct_pu(pk);
					vo.setPk_scopeorg(scopeorg);
					insertscopevos.add(vo);
				}
			}
		}

		// 插入新增合同控制范围
		if (insertscopevos.size() > 0) {
			NCLocator.getInstance().lookup(IPuContrl.class)
					.insertCtCtrlScope(insertscopevos.toArray(new CtScopeVo[insertscopevos.size()]));
		}
	}

	@Override
	public void deleteBatchCtrlScope(String[] pks, String[] sCtrlScopeNew, Map<String, String> puorgmap)
			throws BusinessException {
		// 查询合同控制范围
		Map<String, List<String>> ctscopemap = NCLocator.getInstance().lookup(IPuContrl.class).queryCtsScope(pks);
		// 需删除的合同控制范围
		Map<String, List<String>> delscopemap = new HashMap<String, List<String>>();
		// 待删除的合同PK
		List<String> delctpupks = new ArrayList<String>();
		for (String pk : pks) {// 批选合同
			List<String> ctscopelist = ctscopemap.get(pk);// 旧合同范围
			List<String> delscopelist = new ArrayList<String>();
			for (String scopeorg : sCtrlScopeNew) {// 列表要删除的合同范围
				// 如果原范围中存在要删除的合同，则添加到待删除合同范围内
				if (ctscopelist.contains(scopeorg) && !puorgmap.get(pk).isEmpty()
						&& !puorgmap.get(pk).equals(scopeorg)) {
					delscopelist.add(scopeorg);
				}
			}
			if (delscopelist.size() > 0) {
				delscopemap.put(pk, delscopelist);
				delctpupks.add(pk);
			}
		}

		// 删除合同控制范围
		if (delscopemap.size() > 0) {
			NCLocator.getInstance().lookup(IPuContrl.class).deleteCtCtrlScopes(delscopemap,
					delctpupks.toArray(new String[delctpupks.size()]));
		}
	}

	/**
	 * 合同控制校验
	 * 
	 * @param scopeVos
	 */
	private void checkCtScopeVo(CtScopeVo[] scopeVos) {
		// 除合同删除外 其他都可查询出控制范围
		if (ArrayUtil.isEmpty(scopeVos)) {
			ExceptionUtils.wrappBusinessException(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0400")); // 合同或合同控制范围可能已经被删除,无法做控制范围调整操作
		}
	}

	/**
	 * 批量控制范围是否可编辑
	 * 
	 * @param bill
	 * @return
	 */
	private boolean isBatchCtrlScopeEditable(AggCtPuVO bill) {
		// 总括订单不能修改合同控制范围 && 只有在自由状态，生效，审核状态 控制范围才可修改
		return !bill.getParentVO().getBbracketOrder().booleanValue() && this.isCtrlScopeEditable(bill);
	}

	/**
	 * 只有在自由状态，生效，审核状态 控制范围才可修改
	 * 
	 * @param bill
	 * @return
	 */
	private boolean isCtrlScopeEditable(AggCtPuVO bill) {
		Integer fstatusflag = bill.getParentVO().getFstatusflag();
		if (CtFlowEnum.Free.value().equals(fstatusflag) || CtFlowEnum.VALIDATE.value().equals(fstatusflag)
				|| CtFlowEnum.APPROVE.value().equals(fstatusflag) || CtFlowEnum.APPROVING.value().equals(fstatusflag)) {
			return true;
		}
		return false;
	}

	/**
	 * 初始化批量控制范围界面数据
	 * 
	 * @return
	 */
	private Map<String, Object> initCtBatchCtrlScope(List<String> pk_ct_pus) {
		Map<String, Object> retMap = this.initCtCtrlScope(null, false);
		retMap.put("pks", pk_ct_pus);
		return retMap;
	}

	/**
	 * 初始化控制范围界面数据
	 * 
	 * @param scopeVos 合同控制范围
	 * @param disabled 是否不可编辑
	 * @return
	 */
	private Map<String, Object> initCtCtrlScope(CtScopeVo[] scopeVos, boolean isDisabled) {
		Map<String, Object> retMap = new HashMap<>();
		List<String> dataKeys = new ArrayList<String>(); // 所有关联组织的key
		List<String> dataSource = new ArrayList<String>(); // 所有关联组织的名称
		List<Integer> targetKeys = new ArrayList<Integer>(); // 已合同控制的Index

		HashMap<String, String> mapAssCorpsKeyPk = this.initAssociatedOrg(AppContext.getInstance().getPkGroup());
		for (Map.Entry<String, String> entry : mapAssCorpsKeyPk.entrySet()) {
			dataKeys.add(entry.getKey());
			dataSource.add(entry.getValue());
		}

		if (scopeVos != null) {
			for (CtScopeVo scopeVo : scopeVos) {
				String corpsValue = mapAssCorpsKeyPk.get(scopeVo.getPk_scopeorg());
				targetKeys.add(dataSource.indexOf(corpsValue));
			}
		}

		retMap.put("dataSource", dataSource);
		retMap.put("dataKeys", dataKeys);
		retMap.put("targetKeys", targetKeys);
		retMap.put("disabled", isDisabled);
		return retMap;
	}

	/**
	 * 根据登录集团获取 用户相关联组织
	 * 
	 * @param sLogingroup
	 * @return
	 */
	private HashMap<String, String> initAssociatedOrg(String sLogingroup) {
		HashMap<String, String> mapAssCorpsKeyPk = new HashMap<String, String>();
		// 查询集团下的采购组织
		PurchaseOrgVO[] vos = PurchaseOrgForScopeService.queryAllPurchaseOrgVOSByGroup(sLogingroup);
		if (vos != null) {
			String nameField = this.getNameX();
			for (int i = 0; i < vos.length; i++) {
				Object nameValue = vos[i].getAttributeValue(nameField);
				// 如果当前语种没有值，就取主语种的
				if (ValueUtil.isEmpty(nameValue)) {
					nameValue = vos[i].getAttributeValue(PurchaseOrgVO.NAME);
				}
				if (null == nameValue) {
					nameValue = "";
				}
				String strAssCorpsName = vos[i].getCode() + " " + nameValue;
				// 保存所有关联组织的 [主键-编码和名称] 对应
				mapAssCorpsKeyPk.put(vos[i].getPk_purchaseorg(), strAssCorpsName);
			}
		}
		return mapAssCorpsKeyPk;
	}

	private String getNameX() {
		// 当前语言index
		Integer icul = MultiLangContext.getInstance().getCurrentLangSeq();
		String nameX = PurchaseOrgVO.NAME;
		if (icul.intValue() > 1) {
			nameX = nameX + icul;
		}
		return nameX;
	}
}
