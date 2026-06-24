package nc.bs.ic.m4d.cancelsign.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.m4d.Bill4DFinanceProcess;
import nc.bs.ic.pub.base.ICRule;
import nc.bs.trade.business.HYPubBO;
import nc.impl.am.db.DBAccessUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.material.MaterialVO;
import nc.vo.ewm.workorder.WOActualInvVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 
 * @description 材料出库单取消签字删除存货核算和内部交易的单据
 * @scene 材料出库单取消签字
 * @param 无
 * 
 * @version 2010-6-22 下午02:36:43
 * @since 6.0
 * @author chennn
 */
public class PushDeleteIAandTOBills extends ICRule<MaterialOutVO> {

	@Override
	public void process(MaterialOutVO[] vos) {
		try {
			new Bill4DFinanceProcess().unProcessFinance(vos);
			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			BaseDAO dao = new BaseDAO();
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
					//如果没有工单号,则跳过
					if (StringUtil.isEmpty(bvo.getCworkordercode())) {
						continue;
					}
					//工单主键,如果是非自制的有工单主键,就不需要反写
					String cworkorderhid = bvo.getCworkorderhid();
					if(StringUtils.isNotEmpty(cworkorderhid) ){
						continue;
					}
					String sql = " select * from ewm_wo_actual_inv where src_pk_bill_b = '"
							+ bvo.getCgeneralbid() + "' and nvl(dr,0) = 0";
					List<WOActualInvVO> vlist = (List<WOActualInvVO>) iuap
							.executeQuery(sql, new BeanListProcessor(
									WOActualInvVO.class));
					dao.deleteVOArray(vlist.toArray(new WOActualInvVO[vlist
							.size()]));

					for (WOActualInvVO retActualVO : vlist) {
						writeBackActualInvCost(retActualVO, voMap);
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
			serviceMap.put(retActualVO.getPk_wo(), retActualVO.getMoney());
			updateHeadNmny1(serviceMap, "def4");// 实际fuwu成本
		} else {
			materialMap.put(retActualVO.getPk_wo(), retActualVO.getMoney());
			updateHeadNmny(materialMap, "ac_mtr_mny_org");// 实际物料成本
		}
	}

	private void updateHeadNmny1(Map<String, UFDouble> serviceMap,
			String itemkey) throws BusinessException {
		String sql = " update ewm_wo set  " + itemkey
				+ " = to_number(replace(" + itemkey + ",'~',0 )) - ? where pk_wo= ? and dr=0 ";
		String sql1 = " update ewm_wo set ac_ttl_mny_org = ac_ttl_mny_org - ? where pk_wo=? and dr=0 ";
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
				+ itemkey + ") - ? where pk_wo=? and dr=0 ";
		String sql1 = " update ewm_wo set ac_ttl_mny_org = ac_ttl_mny_org - ? where pk_wo=? and dr=0 ";
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

}
