package nc.pubimpl.ic.statusAdjust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.ic.onhand.OnhandResService;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.exception.DbException;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.ui.pub.print.IDataSource;
import nc.vo.bd.material.MaterialVO;
import nc.vo.fi.pub.SqlUtils;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.m4460.util.ScmSendBuziMsgPara;
import nc.vo.ic.m4460.util.StateAdjustOnhandUtil1;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

import org.jfree.util.Log;

/**
 * 物料状态变更提醒标签打印
 */
public class AutoSendMsgForLabelPrint implements IBackgroundWorkPlugin {

	@Override
	public PreAlertObject executeTask(BgWorkingContext context)
			throws BusinessException {
		Log.debug("AutoSendMsgForLabelPrint后台任务执行开始");
		// 参数处理
		// String pk_group = context.getGroupId();
		String[] pk_orgs = context.getPk_orgs();
		// HashMap<String, Object> params = context.getKeyMap();

		StateAdjustVO[] vos = getStateAdjustVO(pk_orgs);

		if (ValueCheckUtil.isNullORZeroLength(vos)) {
			return getReturnObject(null);
		}
		List<String> pk_onhanddims = new ArrayList<String>();
		for (StateAdjustVO stateAdjust : vos) {
			if (!pk_onhanddims.contains(stateAdjust.getPk_onhanddim())) {
				pk_onhanddims.add(stateAdjust.getPk_onhanddim());
			}
		}

		String[] onhanddims = pk_onhanddims.toArray(new String[0]);
		sendMsg(vos, onhanddims);
		return getReturnObject(onhanddims);

	}
	private JdbcSession session = null;
	private StateAdjustVO[] getStateAdjustVO(String[] pk_orgs)
			throws BusinessException {
		SqlBuilder sql = new SqlBuilder();
		session = this.getSession(InvocationInfoProxy.getInstance()
                .getUserDataSource());
		
		String wherepart = SqlUtils.getInStr("pk_org", pk_orgs, false);
		VOQuery<StateAdjustVO> query = new VOQuery<>(StateAdjustVO.class);
		
		//把获取到的vos.CSTATEADJUSTID和数据表ic_stateadjust_old.CSTATEADJUSTID做差集
//		sql.append("select ic_stateadjust.CSTATEADJUSTID from ic_stateadjust inner join ic_stateadjust_old on ic_stateadjust.CSTATEADJUSTID = ic_stateadjust_old.CSTATEADJUSTID");
		
		StateAdjustVO[] vos = (StateAdjustVO[]) query
				.query(//在下面vos的子查询条件中加入剔除ic_stateadjust_old数据
						" AND ic_stateadjust.cstateadjustid not IN (SELECT ic_stateadjust.CSTATEADJUSTID FROM ic_stateadjust INNER JOIN ic_stateadjust_old ON ic_stateadjust.CSTATEADJUSTID = ic_stateadjust_old.CSTATEADJUSTID) "
						+ " and nvl(vdef1,'N') ='Y' and pk_onhanddim_adj in ( select pk_onhanddim from ic_onhanddim  where "
						+ wherepart  + ")", null);
//		sql.reset();
		if (vos == null || vos.length == 0)
			return null;		
		List<String> pk_onhanddims = new ArrayList<String>();
		
		//把获取到的vos.CSTATEADJUSTID存入到数据表ic_stateadjust_old里		
		for (StateAdjustVO stateAdjust : vos) {
			
			sql.append("insert into ic_stateadjust_old (CSTATEADJUSTID) values( ");            
            sql.append("'" + stateAdjust.getCstateadjustid() + "'");
            sql.append(") \n");
            try {
				session.addBatch(sql.toString());
				sql.reset();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (!pk_onhanddims.contains(stateAdjust.getPk_onhanddim_adj())) {
				pk_onhanddims.add(stateAdjust.getPk_onhanddim_adj());
			}
		}
		try {
			session.executeBatch();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 		IOnhandQry onhandquery = NCLocator.getInstance().lookup(
				IOnhandQry.class);
		OnhandDimVO[] onhandDims = onhandquery.queryOnhandDim(pk_onhanddims
				.toArray(new String[pk_onhanddims.size()]));
		OnhandResService onhandquery1 = NCLocator.getInstance().lookup(
				OnhandResService.class);
		OnhandVO[] onhandvo = onhandquery1.queryOnhandVOByDims(onhandDims);

		List<StateAdjustVO> billList = new StateAdjustOnhandUtil1()
				.onhand2StateAdjust(onhandvo, false, null);
		return billList.toArray(new StateAdjustVO[billList.size()]);
	}
	
	private JdbcSession getSession(String userDataSource) {
        if (session == null) {
                try {
                        session = new JdbcSession(userDataSource);
                        session.getConnection().setAutoCommit(false);
                        session.setAddTimeStamp(true);
                } catch (Exception e) {
                }
        }
        return session;

}

	private PreAlertObject getNothingObject() {
		PreAlertObject resultObject = new PreAlertObject();
		resultObject.setReturnObj("没有需要执行的任务");
		resultObject.setReturnType(PreAlertReturnType.RETURNNOTHING);
		return resultObject;
	}

	protected PreAlertObject getReturnObject(String[] pk_onhanddims)
			throws BusinessException {
		PreAlertObject resultObject = new PreAlertObject();
		AutoAdjustForExpiredBatchDataSource datasource = new AutoAdjustForExpiredBatchDataSource();
		resultObject.setReturnObj(datasource);
		resultObject.setReturnType(PreAlertReturnType.RETURNDATASOURCE);
		if (pk_onhanddims == null || pk_onhanddims.length <= 0) {
			return getNothingObject();
		}

		Map<String, OnhandDimVO> onhandMap = getOnhandDimVO(pk_onhanddims);
		if (onhandMap == null || onhandMap.size() <= 0) {
			return getNothingObject();
		}
		Set<String> pk_materials = new HashSet<>();
		Set<String> pk_batchcodes = new HashSet<>();
		for (OnhandDimVO onhandvo : onhandMap.values()) {
			pk_materials.add(onhandvo.getCmaterialvid());
			pk_batchcodes.add(onhandvo.getPk_batchcode());
		}

		IMaterialPubService service = NCLocator.getInstance().lookup(
				IMaterialPubService.class);
		Map<String, MaterialVO> materialMap = service
				.queryMaterialBaseInfoByPks(
						pk_materials.toArray(new String[0]), new String[] {
								MaterialVO.CODE, MaterialVO.NAME });
		VOQuery<BatchcodeVO> query = new VOQuery<>(BatchcodeVO.class);
		BatchcodeVO[] batchcodeVOs = pk_batchcodes == null
				|| pk_batchcodes.size() <= 0 ? null : query.query(pk_batchcodes
				.toArray(new String[0]));
		Map<String, BatchcodeVO> batchcodeMap = new HashMap<>();
		int len = batchcodeVOs == null ? 0 : batchcodeVOs.length;
		for (int i = 0; i < len; i++) {
			batchcodeMap
					.put(batchcodeVOs[i].getPk_batchcode(), batchcodeVOs[i]);
		}
		Set<Entry<String, OnhandDimVO>> onhandEntrys = onhandMap.entrySet();
		List<String> materialpks = new ArrayList<>();
		List<String> materialcodes = new ArrayList<>();
		List<String> materialnames = new ArrayList<>();
		List<String> batchcodepks = new ArrayList<>();
		List<String> batchcodes = new ArrayList<>();
		List<String> expireddates = new ArrayList<>();
		List<String> onhanddinps = new ArrayList<>();
		List<String> storenames = new ArrayList<>();
		List<String> locationnames = new ArrayList<>();
		for (Entry<String, OnhandDimVO> entry : onhandEntrys) {
			String pk_material = entry.getValue().getCmaterialvid();
			String pk_batchcode = entry.getValue().getPk_batchcode();
			materialpks.add(pk_material);
			materialcodes.add(materialMap.get(pk_material).getCode());
			materialnames.add(materialMap.get(pk_material).getName());
			batchcodepks.add(pk_batchcode);
			batchcodes.add(batchcodeMap.get(pk_batchcode).getVbatchcode());
			if(batchcodeMap.get(pk_batchcode).getDvalidate()!= null){
				expireddates.add(batchcodeMap.get(pk_batchcode).getDvalidate()
						.toStdString());
			}
			onhanddinps.add(entry.getValue().getPk_onhanddim());
			storenames.add(entry.getValue().getCwarehouseid());
			locationnames.add(entry.getValue().getClocationid());
		}
		datasource.setMaterialCode(materialcodes.toArray(new String[0]));
		datasource.setMaterialName(materialnames.toArray(new String[0]));
		datasource.setBatchcode(batchcodes.toArray(new String[0]));
		datasource.setPKMaterialCode(materialpks.toArray(new String[0]));
		datasource.setPKBatchcode(batchcodepks.toArray(new String[0]));
		datasource.setPk_onhanddim(onhanddinps.toArray(new String[0]));
		datasource.setStroeName(storenames.toArray(new String[0]));
		datasource.setLocationName(locationnames.toArray(new String[0]));

		return resultObject;
	}

	public class AutoAdjustForExpiredBatchDataSource implements IDataSource {

		private static final long serialVersionUID = 8543547808997920885L;

		private static final String PK_ONHANDDIM = "pk_onhanddim";
		private static final String PK_MATERIAL = "pk_material";
		private static final String MATERIALCODE = "materialcode";
		private static final String MATERIALNAME = "materialname";
		private static final String PK_BATCHCODE = "pk_batchcode";
		private static final String BATCHCODE = "batchcode";
		private static final String STORENAME = "stroename";
		private static final String LOCATIONNAME = "locationname";

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

		public void setPKBatchcode(String[] values) {
			this.dataMap.put(PK_BATCHCODE, values);
		}

		public void setBatchcode(String[] values) {
			this.dataMap.put(BATCHCODE, values);
		}

		public void setPk_onhanddim(String[] values) {
			this.dataMap.put(PK_ONHANDDIM, values);
		}

		public void setStroeName(String[] values) {
			this.dataMap.put(STORENAME, values);
		}

		public void setLocationName(String[] values) {
			this.dataMap.put(LOCATIONNAME, values);
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
			return new String[] { PK_MATERIAL, MATERIALCODE, MATERIALNAME,
					PK_BATCHCODE, BATCHCODE, PK_ONHANDDIM, STORENAME,
					LOCATIONNAME };
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

	private void sendMsg(StateAdjustVO[] vos, String[] pk_onhanddims)
			throws BusinessException {
		BuziMsgSender sender = new BuziMsgSender();

		Map<String, OnhandDimVO> onhandMap = getOnhandDimVO(pk_onhanddims);
		if (onhandMap == null || onhandMap.size() <= 0) {
			return;
		}
		Map<String, List<StateAdjustVO>> map = stateAdjustVOGroupByOrg(vos,
				onhandMap);

		if (map == null || map.size() <= 0) {
			return;
		}
		for (List<StateAdjustVO> list : map.values()) {
			for (StateAdjustVO vo : list) {
				ScmSendBuziMsgPara param = new ScmSendBuziMsgPara();
				param.setMsgrescode("46111");
				param.setBillType(vo.getCadjustbilltype());
				param.setMsgSourceType("labelprint");
				// param.setMsgSourceType("notice");
				param.setContentType("text");
				param.setPk_detail(vo.getPk_onhanddim());
				param.setApproverField("approver");
				param.setCtrantypeidField("ctrantypeid");
				param.setPk_groupField("pk_group");
				param.setPk_materialField("pk_material");
				param.setPk_orgField("pk_org");
				param.setVtrantypecodeField("vtrantypecode");
				sender.send(vo,
						new String[] { onhandMap.get(vo.getPk_onhanddim())
								.getPk_org() }, param);
			}
		}
	}

	private Map<String, OnhandDimVO> getOnhandDimVO(String[] pk_onhanddims)
			throws BusinessException {
		Map<String, OnhandDimVO> onhandMap = new HashMap<>();
		IOnhandQry onhandquery = NCLocator.getInstance().lookup(
				IOnhandQry.class);
		OnhandDimVO[] onhandDims = onhandquery.queryOnhandDim(pk_onhanddims);
		if (onhandDims == null || onhandDims.length <= 0) {
			return onhandMap;
		}
		Set<String> pk_materials = new HashSet<>();
		Set<String> pk_batchcodes = new HashSet<>();

		for (int i = 0; i < onhandDims.length; i++) {
			pk_materials.add(onhandDims[i].getCmaterialvid());
			pk_batchcodes.add(onhandDims[i].getPk_batchcode());
			onhandMap.put(onhandDims[i].getPk_onhanddim(), onhandDims[i]);
		}
		return onhandMap;
	}

	private Map<String, List<StateAdjustVO>> stateAdjustVOGroupByOrg(
			StateAdjustVO[] vos, Map<String, OnhandDimVO> onhandMap) {
		List<StateAdjustVO> newBillVOs = null;

		Map<String, List<StateAdjustVO>> map = new HashMap<>();

		for (StateAdjustVO itemVO : vos) {
			String pk_purchaseorg = onhandMap.get(itemVO.getPk_onhanddim())
					.getPk_org();
			if (map.containsKey(pk_purchaseorg)) {
				newBillVOs = map.get(pk_purchaseorg);
			} else {
				newBillVOs = new ArrayList<>();
			}
			newBillVOs.add(itemVO);
			map.put(pk_purchaseorg, newBillVOs);
		}

		return map;
	}

}
