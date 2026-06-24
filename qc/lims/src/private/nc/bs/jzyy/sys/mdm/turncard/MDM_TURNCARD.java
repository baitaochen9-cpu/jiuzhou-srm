package nc.bs.jzyy.sys.mdm.turncard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pub.smart.SmartServiceImpl;
import nc.itf.ic.m4460.IStateAdjustMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.ui.pub.print.IDataSource;
import nc.vo.arap.uforeport.SqlBuffer;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.material.query.InvInfoQuery;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
import nc.vo.ic.onhand.entity.OnhandSNVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.onhand.pub.OnhandVOTools;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nccloud.api.jzyy.JZYYResultMessageUtil;

import com.alibaba.fastjson.JSONObject;
//import org.jfree.util.Log;

/**
 * 
 * @ClassName: MDM_TURNCARD
 * @Description:TODO(翻牌放行)
 * @author: 云峰网络 411072655 
 * 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */
public class MDM_TURNCARD extends AbstracAdapter4Ext{

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// TODO Auto-generated method stub
		JSONObject rqJosn = (JSONObject) billvo;
		//检索数据
		try {
			 //2021-11-27 槽车放行
			String flowid= rqJosn.getJSONObject("data").getString("id");
			if(flowid.startsWith("PU1")||flowid.startsWith("PU3")){
				String sql="select nnum,nastnum, pk_arriveorder_b from po_arriveorder_b where dr=0 and vbdef12='"+flowid+"'";
				Map<String, Object> arrorder_b = (Map<String, Object>) getDao().executeQuery(sql, new MapProcessor());

				if(arrorder_b!=null && arrorder_b.size() >0){			

	//					--2021-12-10 根据放行结果判断是否允许入库	
						String cqualitylevel=rqJosn.getJSONObject("data").getString("cqualitylevel");
	//					--溶媒回收 LIMS放行质量等级只允许A Or R  @hew 2021-12-13
						Object nnum = arrorder_b.get("nnum");
						Object nastnum = arrorder_b.get("nastnum");
						Object pk_arriveorder_b = arrorder_b.get("pk_arriveorder_b");
						if(flowid.startsWith("PU3")){
							if(cqualitylevel.equals("A")){
								sql="update po_arriveorder_bb set nnum="+nnum+",nastnum="+nastnum+",bcanstore='Y' where dr=0 and pk_arriveorder_b='"+pk_arriveorder_b+"'";
								getDao().executeUpdate(sql);
								queryRespData(rqJosn);
							}else if (flowid.startsWith("PU3") && cqualitylevel.equals("R")){
								sql="update po_arriveorder_bb set  bcanstore='N' where dr=0 and" +
										 " pk_arriveorder_b ='"+pk_arriveorder_b+"'";
								getDao().executeUpdate(sql);
								queryRespData(rqJosn);
							}else  {
								throw new BusinessException("槽车放行收只允许质量等级: A或者R !");
							}
						}else {
							queryRespData(rqJosn);
						}
					}else{
					throw new BusinessException("未匹配到需要放行的到货单");
				}
			}else if(flowid.startsWith("WR5")){//生产槽车放行

				/*回写完工报告
				 * @hew
				 * 2021-12-07
				 * */
			
//				String sql="select cmoid from mm_mo where dr=0 and vdef11='"+flowid+"'";
				
				String sql="select pk_wr_product from mm_wr_product where dr=0 and vbdef12='"+flowid+"'";
				Map<String, Object> arrorder_b = (Map<String, Object>) getDao().executeQuery(sql, new MapProcessor());
				if(arrorder_b!=null && arrorder_b.size() >0){
					
					Object pk_wr_product = arrorder_b.get("pk_wr_product");
//					/*获取质量等级PK*/
					String pk_qualitylv_b=this.getQualitylv(
													rqJosn.getJSONObject("data").getString("cqualitylevel"));
					String cqualitylevel =rqJosn.getJSONObject("data").getString("cqualitylevel");
					
//					--  bbisempass 根据检验结果 是否紧急放行  bbisempass 
					if(cqualitylevel.equals("A")){
						sql="update mm_wr_product set nbchecknum=nbsldchecknum," +
							"nbcheckastnum=nbsldcheckastnum, bbisempass='Y' where dr=0 and pk_wr_product ='"+pk_wr_product+"'";
						getDao().executeUpdate(sql);
					}else if(cqualitylevel.equals("R")){
						sql="update mm_wr_product set " +" bbisempass='N' where dr=0 and pk_wr_product ='"+pk_wr_product+"'";
						getDao().executeUpdate(sql);
					}else{
						throw new BusinessException("溶媒回收收只允许放行质量等级: A或者R !");
						
					}
					
//					-- vbdef13 质量等级
					sql="update mm_wr_product set vbdef13='"+pk_qualitylv_b+"' where " +
									"dr=0 and pk_wr_product ='"+pk_wr_product+"'";
					getDao().executeUpdate(sql);
					
//					-- DINVALIDDATE_148 完工报告复测日期
					String dvalidate =rqJosn.getJSONObject("data").getString("dvalidate");
					sql ="update mm_wr_product_148 set  DINVALIDDATE_148 = '"+dvalidate+"' " +
								"where  pk_wr_product = '"+pk_wr_product+"' and dr = 0;";
					
					getDao().executeUpdate(sql);

				}else{
					throw new BusinessException("未匹配到需要放行的生产订单");
				}

			}else{
				 queryRespData(rqJosn);
			}
			
		} catch (Exception e) {
			return JZYYResultMessageUtil.getFailedRsultJson( "翻牌失败"+e.getMessage());
		}
	
		//将结果返回			
		return JZYYResultMessageUtil.getSuccessRsultJson("创建成功",rqJosn);
	}
	
	/**
	 * 
	 * @return
	 * @throws BusinessException 
	 */
	private List<Map<String, Object>> queryRespData(JSONObject rqJosn) throws BusinessException {
		
//		物料编码	material_code
//		物料名称	material_name
//		批次号	vbatchcode
//		库存维度	pk_onhanddim
//		失效/复测日期	dvalidate
//		放行主数量	num
//		库存状态	cstate
//		质量等级	cqualitylevel
//		放行人	usercode
//		所属公司编码	org_code
//		所属公司	org_name
//		所属集团	group
//		预留1	def1
//		预留2	def2
//		预留3	def3
//		预留4	def4
//		预留5	def5
		String material_code = rqJosn.getJSONObject("data").getString("material_code");
		String vbatchcode = rqJosn.getJSONObject("data").getString("vbatchcode");
		String dvalidate = rqJosn.getJSONObject("data").getString("dvalidate");
//		String cstate = rqJosn.getJSONObject("data").getString("cstate");
		String cqualitylevel = rqJosn.getJSONObject("data").getString("cqualitylevel");
		String org_code = rqJosn.getJSONObject("data").getString("org_code");
		String group = rqJosn.getJSONObject("data").getString("group");
		String  pk_group=getPk_group(group);
		String[]  pk_orgs=new String[]{getPk_financeorg(org_code)};
		String usercode = rqJosn.getJSONObject("data").getString("usercode");
		String pk_user=getPk_user(usercode);
		String cstate=getCState(cqualitylevel);	
		StoreStateVO targetStateVO = getStoreStateByCode( cstate,pk_group);
//	现存量的质量等级
		String[] srcStates=new String[]{"null"};
//		是否封存
		UFBoolean includebseal =  UFBoolean.FALSE	;

		BatchcodeVO[] batchcodes = getExpiredBatchcodeVO(includebseal, new UFDate(dvalidate),vbatchcode,material_code);
		if (batchcodes == null || batchcodes.length <= 0) {
			throw new BusinessException("未检索到批次信息:物料="+material_code+"批次="+vbatchcode);
		}
		/*
		 * 执行完成后反查批次号， 将对应批次号的质量等级全改回传的状态
		 */
		this.executeTaskAfutToBatchcode(batchcodes, cqualitylevel);
		
		String sql = getOnhandWhereSql(batchcodes, pk_group, pk_orgs, srcStates);
		OnhandVO[] onhands = getOnhandVOs(sql);
		//检验完成才允许入库的,可能还没库存信息
		if (onhands == null || onhands.length <= 0) {
			return null;
		}		
		StateAdjustVO[] stateAdjustVOs = processOnhandVO(onhands);
		String[] pk_onhanddims = null;
		if (stateAdjustVOs != null && stateAdjustVOs.length > 0) {
			pk_onhanddims = doStateAdjust(stateAdjustVOs, targetStateVO,pk_user);
		}
		return null;
	}
	

	/**
	 * 根据条件获取物料结存记录
	 * 
	 * @throws BusinessException
	 * @throws nc.vo.pub.BusinessException 
	 */
	private OnhandVO[] getOnhandVOs(String where) throws BusinessException, nc.vo.pub.BusinessException {
		// 现存量查询接口
		IOnhandQry onbandquery = NCLocator.getInstance().lookup(
				IOnhandQry.class);
		// 通过查询条件，返回现存量
		OnhandQryCond cond = new OnhandQryCond();
		cond.setISSum(true);
		cond.addSelectFields(CollectionUtils.combineArrs(
				OnhandDimVO.getDimContentFields(),
				new String[] { OnhandDimVO.PK_ONHANDDIM }));// 加入现存量维度及现存量维度pk
		cond.setWhere(where.toString());
		OnhandVO[] onhands = onbandquery.queryOnhand(cond);/* yezhian */
		return onhands;
	}


	/**
	 * 
	 * @param onhanddims
	 *            维度主键
	 * @param zldj
	 *            调整后的质量等级
	 * @return
	 * @throws BusinessException
	 */
	private void executeTaskAfutToBatchcode(BatchcodeVO[] batchcodes,
			String zldj) throws BusinessException {
		// 设置批次号内的质量等级
		// BaseDAO dao = getDAO();
		 List<BatchcodeVO> list = new ArrayList<BatchcodeVO>();

		SmartServiceImpl serve = new SmartServiceImpl();
		BatchOperateVO batchOperate = null;
//		String sql ="select  pk_qualitylv_b from scm_qualitylevel_b where bqualified  = 'Y' and cqualitylvcode ='"+zldj+"' ";		
//		Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
//		if(obj == null){
//			throw new BusinessException("错误的质量等级(NC未匹配到):"+zldj);
//		}
//		--- 获取质量等级 
		Object obj =this.getQualitylv(zldj);
		
		String pk_qualitylv_b=(obj==null?null:obj.toString());
		for (BatchcodeVO batchcode : batchcodes) {
			String cqualitylevelid = batchcode.getCqualitylevelid();
			batchcode.setCqualitylevelid(pk_qualitylv_b);
			batchcode.setStatus(VOStatus.UPDATED);
			batchcode.setBinqc(UFBoolean.FALSE);
			batchcode.setVnote("LIMS放行");
			//batchcode.setTs(new UFDateTime());
			list.add(batchcode);
		}
		batchOperate = new BatchOperateVO();
		batchOperate.setUpdObjs(list.toArray(new BatchcodeVO[0] ));
		serve.batchSave(batchOperate);
	}


	
	private String getCState(String cqualitylevel) throws BusinessException {
		 String sql="select  b.vcode   from scm_qualitylevel_b h ,ic_storestate b  where  h.  pk_stockstate=b.pk_storestate  and h.cqualitylvcode ='"+cqualitylevel+"' and h.dr=0";
		 
		 
//		 "select  pk_financeorg    from org_stockorg where code ='"+code  +"' and dr=0";			
	Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
	
	 String cuserid=(obj==null?null:obj.toString());
	 return cuserid;
	}

	private String getPk_group(String code  ) throws BusinessException {
		 String sql="select  pk_group    from org_group where code ='"+code  +"' and dr=0";			
			Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
			
			 String pk_group=(obj==null?null:obj.toString());
			 return pk_group;
	}
	
	
	private String getPk_financeorg(String code  ) throws BusinessException {
		 String sql="select  pk_financeorg    from org_stockorg where code ='"+code  +"' and dr=0";			
			Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
			
			 String pk_financeorg=(obj==null?null:obj.toString());
			 return pk_financeorg;
	}
	
	
	private String getPk_user(String code  ) throws BusinessException {
		 String sql="select  h.cuserid  from sm_user h   where h.user_code='"+code+"' and dr=0";
		 Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		 String cuserid=(obj==null?null:obj.toString());
		 return cuserid;
	}

	protected PreAlertObject getReturnObject(String[] pk_onhanddims)
			throws BusinessException {
		PreAlertObject resultObject = new PreAlertObject();
		AutoAdjustForExpiredBatchDataSource datasource = new AutoAdjustForExpiredBatchDataSource();
		resultObject.setReturnObj(datasource);
		resultObject.setReturnType(PreAlertReturnType.RETURNDATASOURCE);
		if (pk_onhanddims == null || pk_onhanddims.length <= 0) {
			return resultObject;
		}
		IOnhandQry onhandquery = NCLocator.getInstance().lookup(
				IOnhandQry.class);
		OnhandDimVO[] onhandDims = onhandquery.queryOnhandDim(pk_onhanddims);
		if (onhandDims == null || onhandDims.length <= 0) {
			return resultObject;
		}
		Set<String> pk_materials = new HashSet<>();
		Set<String> pk_batchcodes = new HashSet<>();
		Map<String, OnhandDimVO> onhandMap = new HashMap<>();
		for (int i = 0; i < onhandDims.length; i++) {
			pk_materials.add(onhandDims[i].getCmaterialvid());
			pk_batchcodes.add(onhandDims[i].getPk_batchcode());
			onhandMap.put(onhandDims[i].getPk_onhanddim(), onhandDims[i]);
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
		for (Entry<String, OnhandDimVO> entry : onhandEntrys) {
			String pk_material = entry.getValue().getCmaterialvid();
			String pk_batchcode = entry.getValue().getPk_batchcode();
			materialpks.add(pk_material);
			materialcodes.add(materialMap.get(pk_material).getCode());
			materialnames.add(materialMap.get(pk_material).getName());
			batchcodepks.add(pk_batchcode);
			batchcodes.add(batchcodeMap.get(pk_batchcode).getVbatchcode());
			expireddates.add(batchcodeMap.get(pk_batchcode).getDvalidate()
					.toStdString());
		}
		datasource.setMaterialCode(materialcodes.toArray(new String[0]));
		datasource.setMaterialName(materialnames.toArray(new String[0]));
		datasource.setBatchcode(batchcodes.toArray(new String[0]));
		datasource.setPKMaterialCode(materialpks.toArray(new String[0]));
		datasource.setPKBatchcode(batchcodepks.toArray(new String[0]));
		datasource.setExpiredDate(expireddates.toArray(new String[0]));

		return resultObject;
	}

	public class AutoAdjustForExpiredBatchDataSource implements IDataSource {

		private static final long serialVersionUID = 8543547808997920885L;

		private static final String PK_MATERIAL = "pk_material";
		private static final String MATERIALCODE = "materialcode";
		private static final String MATERIALNAME = "materialname";
		private static final String PK_BATCHCODE = "pk_batchcode";
		private static final String BATCHCODE = "batchcode";
		private static final String EXPIREDDATE = "expireddate";

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

		public void setExpiredDate(String[] values) {
			this.dataMap.put(EXPIREDDATE, values);
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
					PK_BATCHCODE, BATCHCODE, EXPIREDDATE };
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
	
	/**
	 * 进行库存状态调整
	 * 
	 * @param bills
	 *            库存状态调整记录
	 * @param targetStateVO
	 *            目标库存状态
	 * @return String[] 已调整的存量维度pk
	 * @throws BusinessException
	 */
	private String[] doStateAdjust(StateAdjustVO[] bills,
			StoreStateVO targetStateVO,String pk_user) throws BusinessException {
		if (bills == null || bills.length <= 0) {
			return null;
		}
		for (StateAdjustVO stateAdjust : bills) {
			setDefaultValue(stateAdjust, targetStateVO,pk_user);
		}
		Set<String> onhanddims = new HashSet<>();
		IStateAdjustMaintain stateAdjustService = NCLocator.getInstance()
				.lookup(IStateAdjustMaintain.class);
		for (StateAdjustVO stateAdjust : bills) {
			try {
				stateAdjustService
						.stateAdjust(new StateAdjustVO[] { stateAdjust });
				if (onhanddims.contains(stateAdjust.getPk_onhanddim())) {
					onhanddims.add(stateAdjust.getPk_onhanddim());
				}
			} catch (Exception e) {
				continue;
			}
		}
		return onhanddims.toArray(new String[0]);
	}
	/**
	 * 设置库存状态调整记录的默认值
	 * 
	 * @param stateAdjustVO
	 *            库存状态调整记录
	 * @param targetStateVO
	 *            调整后库存状态
	 * @throws BusinessException
	 */
	public void setDefaultValue(StateAdjustVO stateAdjustVO,
			StoreStateVO targetStateVO,String pk_user) throws BusinessException {
		// 库存现存量
		UFDouble nnum = stateAdjustVO.getNnum() != null ? stateAdjustVO
				.getNnum() : UFDouble.ZERO_DBL;
		UFDouble nassistnum = stateAdjustVO.getNassistnum() != null ? stateAdjustVO
				.getNassistnum() : UFDouble.ZERO_DBL;
		UFDouble nlocknum = stateAdjustVO.getNlocknum() != null ? stateAdjustVO
				.getNlocknum() : UFDouble.ZERO_DBL;
		UFDouble nlockassistnum = stateAdjustVO.getNlockassistnum() != null ? stateAdjustVO
				.getNlockassistnum() : UFDouble.ZERO_DBL;
		// 设置调整数量现存量
		stateAdjustVO.setNadjustassistnum(nassistnum.sub(nlockassistnum));
		stateAdjustVO.setNadjustnum(nnum.sub(nlocknum));
		// 设置调整人
		stateAdjustVO.setBillmaker(pk_user);
		// 设置调整状态
		stateAdjustVO.setCadjuststateid(targetStateVO.getPk_storestate());
		// 设置调整时间
		stateAdjustVO.setCreationtime(AppContext.getInstance().getServerTime());
	}

	/**
	 * 方法功能描述：根据编码,查询待检库存状态vo
	 * 
	 * @throws BusinessException
	 * 
	 */
	private StoreStateVO getStoreStateByCode(String code, String pk_group)
			throws BusinessException {
		VOQuery<StoreStateVO> query = new VOQuery<>(StoreStateVO.class);
		StringBuilder sql = new StringBuilder();
		sql.append("  and " + StoreStateVO.PK_GROUP + " = '" + pk_group + "'");
		sql.append(" and " + StoreStateVO.VCODE + " = '" + code + "'");
		StoreStateVO[] states = query.query(sql.toString(), null);
		if (states == null || states.length <= 0) {
			throw new BusinessException("无法查询到编码为【" + code + "】的库存状态！！！");
		}
		return states[0];
	}

	
	/**
	 * 处理查询出来的物料结存记录
	 * 
	 * @param onhandvos
	 * @throws BusinessException
	 */
	private StateAdjustVO[] processOnhandVO(OnhandVO[] onhandvos)
			throws BusinessException {
		// 根据单品信息过滤掉现存量记录，V60暂不支持库存状态调整
		OnhandVO[] rets = this.filterOnHandBySN(onhandvos);
		StateAdjustVO[] bills = this.translateResult(rets);
		return bills;
	}
	
	
	/**
	 * 将现存量记录转化为库存状态调整单
	 * 
	 * @param resultVOs
	 * @return
	 * @throws BusinessException
	 */
	private StateAdjustVO[] translateResult(OnhandVO[] resultVOs)
			throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(resultVOs)) {
			return null;
		}
		Map<String, InvBasVO> map = this.getInvBasMap(resultVOs);
		if (ValueCheckUtil.isNullORZeroLength(map)) {
			return null;
		}
		Map<String, UFDateTime> tsMap = this.getOnhandnumTS(resultVOs);
		if (ValueCheckUtil.isNullORZeroLength(tsMap)) {
			return null;
		}
		InvBasVO inv = null;
		List<StateAdjustVO> billList = new ArrayList<StateAdjustVO>();
		for (OnhandVO onhandVO : resultVOs) {
			inv = map.get(onhandVO.getCmaterialvid());
			// 物料管理属性：库存状态管理；
			if (inv == null || !inv.getFix1().booleanValue()) {
				continue;
			}
			// 结存主数量-冻结数量>0；预留主数量，冻结数量满足至少有一个为空或零
			if (tsMap.get(onhandVO.getPk_onhanddim()) == null
					|| !canStateAdjust(onhandVO)) {
				continue;
			}

			StateAdjustVO vo = new StateAdjustVO();
			vo.setPk_onhanddim(onhandVO.getPk_onhanddim());
			vo.setPk_group(onhandVO.getPk_group());
			vo.setPk_org(onhandVO.getPk_org());// 库存组织，方便生成单据号
			vo.setNnum(onhandVO.getNonhandnum());// 结存主数量
			vo.setNassistnum(onhandVO.getNonhandastnum());// 结存数量
			vo.setNgrossnum(onhandVO.getNgrossnum());// 毛重
			vo.setNrsnum(onhandVO.getNrsnum());// 预留主数量
			vo.setNlocknum(onhandVO.getNlocknum());// 冻结主数量
			vo.setNlockassistnum(onhandVO.getNlockastnum());// 冻结数量
			vo.setOnhandnumts(tsMap.get(onhandVO.getPk_onhanddim()));// 现存量时间戳
			vo.setVchangerate(onhandVO.getVchangerate());// 换算率；

			for (int i = 1; i < 11; i++) {
				vo.setAttributeValue("vfree" + i,
						onhandVO.getAttributeValue("vfree" + i));
			}

			vo.setCunitid(inv.getPk_measdoc());// 主单位，方便处理精度
			vo.setCastunitid(onhandVO.getCastunitid());// 单位，方便处理精度
			billList.add(vo);
		}
		return billList.toArray(new StateAdjustVO[0]);
	}
	
	
	/**
	 * 物料是否库存状态管理，现存量换算率处理
	 * 
	 * @param resultVOs
	 * @return
	 */
	private Map<String, InvBasVO> getInvBasMap(OnhandVO[] resultVOs) {
		String[] cmaterialvids = VOEntityUtil.getVOsValues(resultVOs,
				ICPubMetaNameConst.CMATERIALVID, String.class);
		InvInfoQuery invInfoQuery =new InvInfoQuery();
		InvBasVO[] invvos = invInfoQuery.getInvBasVO(cmaterialvids);
		if (ValueCheckUtil.isNullORZeroLength(invvos)) {
			return null;
		}
		// 现存量换算率处理
		OnhandVOTools.fillOnhandVChangerate(resultVOs, invInfoQuery);

		Map<String, InvBasVO> map = new HashMap<String, InvBasVO>();
		for (InvBasVO vo : invvos) {
			map.put(vo.getPk_material(), vo);
		}
		return map;
	}

	/**
	 * 现存量的ts
	 * 
	 * @param pk_dims
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, UFDateTime> getOnhandnumTS(OnhandVO[] resultVOs)
			throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(resultVOs)) {
			return null;
		}

		String[] pk_dims = VOEntityUtil.getVOsNotRepeatValue(resultVOs,
				OnhandDimVO.PK_ONHANDDIM);

		// 现存量查询接口
		IOnhandQry onbandquery = NCLocator.getInstance().lookup(
				IOnhandQry.class);
		OnhandNumVO[] numvos = onbandquery.queryOnhandNumByDim(pk_dims);
		if (ValueCheckUtil.isNullORZeroLength(numvos)) {
			return null;
		}
		Map<String, UFDateTime> map = new HashMap<String, UFDateTime>();
		for (OnhandNumVO vo : numvos) {
			map.put(vo.getPk_onhanddim(), vo.getTs());
		}
		return map;
	}

	/**
	 * 现存量数量方面是否符合状态调整
	 * 
	 * @param onhandVO
	 * @return
	 */
	private boolean canStateAdjust(OnhandVO onhandVO) {
		UFDouble nlocknum = onhandVO.getNlocknum() == null ? UFDouble.ZERO_DBL
				: onhandVO.getNlocknum();
		UFDouble nonhandnum = onhandVO.getNonhandnum() == null ? UFDouble.ZERO_DBL
				: onhandVO.getNonhandnum();
		// ------
		if (onhandVO.getAttributeValue("vbatchcode").equals("CNT302")) {
//			Log.debug(onhandVO.getAttributeValue("vbatchcode"));
		}
		if (nonhandnum.compareTo(nlocknum) <= 0) {
			return false;
		}
		if (NCBaseTypeUtils.isGtZero(onhandVO.getNlocknum())
				&& NCBaseTypeUtils.isGtZero(onhandVO.getNrsnum())) {
			return false;
		}
		if (NCBaseTypeUtils.isNullOrZero(onhandVO.getNonhandnum())
				&& NCBaseTypeUtils.isNullOrZero(onhandVO.getNonhandastnum())
				&& NCBaseTypeUtils.isNullOrZero(onhandVO.getNgrossnum())) {
			return false;
		}
		return true;
	}
	private OnhandVO[] filterOnHandBySN(OnhandVO[] resultVOs)
			throws BusinessException, DAOException {
		String[] pk_dims = VOEntityUtil.getVOsNotRepeatValue(resultVOs,
				OnhandDimVO.PK_ONHANDDIM);
		OnhandSNVO[] vos = NCLocator.getInstance().lookup(IOnhandQry.class)
				.queryOnhandSNVOByDimPK(pk_dims);
		if (ValueCheckUtil.isNullORZeroLength(vos)) {
			return resultVOs;
		}
		Set<String> dims = VOEntityUtil.getVOsValueSet(vos,
				OnhandDimVO.PK_ONHANDDIM);
		List<OnhandVO> ret = new ArrayList<OnhandVO>();
		for (OnhandVO vo : resultVOs) {
			String cmaterialvid = vo.getCmaterialvid();

			String sql = " select 1 from bd_materialstock where pk_material = '"
					+ cmaterialvid
					+ "' and pk_org = '"
					+ vo.getPk_org()
					+ "'"
					+ " and serialmanaflag = 'Y'";
//			BaseDAO dao=new BaseDAO();
			Object executeQuery = getDao().executeQuery(sql,
					new ArrayProcessor());
			if (!dims.contains(vo.getPk_onhanddim()) && executeQuery != null) {
				continue;
			}
			ret.add(vo);

		}
		return CollectionUtils.listToArray(ret);
	}


	
	public String getOnhandWhereSql(BatchcodeVO[] batchcodes, String pk_group,
			String[] pk_orgs, String[] includeStates) {
		SqlBuffer sql = new SqlBuffer();
		sql.append(" 1=1 ");
		if (pk_orgs != null && pk_orgs.length > 0) {
			sql.append(" and ");
			sql.append(ICPubMetaNameConst.PK_ORG, pk_orgs);
		}
		if (batchcodes == null || batchcodes.length <= 0) {
			return sql.toString();
		}
		List<String> pk_batchcodes = new ArrayList<>();
		for (int i = 0; i < batchcodes.length; i++) {
			pk_batchcodes.add(batchcodes[i].getPk_batchcode());
		}
		if (pk_batchcodes != null && pk_batchcodes.size() > 0) {
			sql.append(" and ");
			sql.append(ICPubMetaNameConst.PK_BATCHCODE,
					pk_batchcodes.toArray(new String[0]));
		}
//		TODO
//		if (includeStates != null && includeStates.length > 0) {
//			sql.append(" and ");
//			sql.append(ICPubMetaNameConst.CSTATEID, includeStates);
//		}
		// sql.append(" and vbatchcode ='CNT302' ");
		return sql.toString();
	}

	/**
	 * 获取效期失效的批次号档案
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private BatchcodeVO[] getExpiredBatchcodeVO(UFBoolean includebseal,
			UFDate criticalDate,String vbatchcode,String material_code ) throws BusinessException {
//		UFDate criticalDate = BSContext.getInstance().getDate()
//				.getDateBefore(daysBefore);
		StringBuilder sql = new StringBuilder();
		sql.append(" and exists (");
		sql.append("    select 1 from bd_material_v material  ");
		sql.append(" 	where material.pk_material = cmaterialoid ");
		sql.append("    and material.code ='"+material_code+"' ");
		sql.append(" 	and isnull(material.dr, 0) = 0 ");
		sql.append(" 	and isnull(material.enablestate, 2) = 2 ");
		sql.append(" )");
/*		sql.append(" and substr(dvalidate, 0, 10) < '").append(criticalDate)
				.append("'");*/
		sql.append(" and dvalidate is not null");
		
		sql.append(" and  vbatchcode='"+vbatchcode+"'");
		
		
//	是否封存
		if (!includebseal.booleanValue()) {
			sql.append(" and nvl(bseal,'N') = 'N'   ");
		}

		VOQuery<BatchcodeVO> query = new VOQuery<>(BatchcodeVO.class);
		BatchcodeVO[] batchcodes = query.query(sql.toString(), null);
		
		for( int i=0;i<batchcodes.length;i++){
			if(!criticalDate.equals(batchcodes[i].getDvalidate().toString())){
				//LIms 调整失效日期  进行更新
			/*	String sql2= "  update scm_batchcode set dvalidate='"+criticalDate.toString()+"' where pk_batchcode='"+batchcodes[i].getPk_batchcode()+"'";

		BaseDAO  dao=new BaseDAO();
		dao.executeUpdate(sql2);*/
				
				batchcodes[i].setDvalidate(criticalDate);
			}
		}
		
		return batchcodes;
	}
	
	
	
	/*
	 *获取质量等级主键 
	 *@return
	 *@throw  BusinessException
	 * hw 2021-12-07
	 * */
	private String getQualitylv(String zldj) throws BusinessException {
//		String sql ="select  pk_qualitylv_b from scm_qualitylevel_b where bqualified  = 'Y' and cqualitylvcode ='"+zldj+"' ";	
		
//		--允许放行为任意质量等级  @Hw  2021-12-10 
		String sql ="select  pk_qualitylv_b from scm_qualitylevel_b where  cqualitylvcode ='"+zldj+"' ";	
		Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		if(obj == null){
			throw new BusinessException("错误的质量等级(NC未匹配到):"+zldj);
		}
		String pk_qualitylv_b=(obj==null?null:obj.toString());
		return pk_qualitylv_b;
	}
	
	
	/*
	 * 根据质量等级判断能否入库
	 * @return UFboolen
	 * hw 2021-2-10
	 * */
	private UFBoolean excuteCheckstoreState(String cqualitylevel) throws BusinessException {
		if(this.getCState(cqualitylevel).equals("hege")){
			return UFBoolean.TRUE;
		}
		return UFBoolean.FALSE;
	}
	
	
}
	

