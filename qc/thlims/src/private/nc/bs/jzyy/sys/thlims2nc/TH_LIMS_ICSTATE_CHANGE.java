package nc.bs.jzyy.sys.thlims2nc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.jzyy.sys.thlims.LimsIpcVO;
import nc.bs.logging.Log;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.trade.business.HYPubBO;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pub.smart.SmartServiceImpl;
import nc.itf.ic.m4460.IStateAdjustMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.ui.pub.print.IDataSource;
import nc.vo.arap.uforeport.SqlBuffer;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.material.query.InvInfoQuery;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
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
import nc.vo.sm.UserVO;
import nccloud.api.jzyy.JZYYResultMessageUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName: MDM_TURNCARD
 * @Description:TODO(翻牌放行)
 * @author: 云峰网络 411072655 
 * 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */

/*
 * {
	"functype": "TH_LIMS_ICSTATE_CHANGE",
	"data": {
		"id": "rq123123",
		"org_code": "23",
		"material_code": "804700",
		"vbatchcode": "XC0022",
		"pk_onhanddim": "",
		"dvalidate": "",
		"num": "1",
		"cstate": "buhege",
		"usercode": "yf01",
		"group": "G",
		"def1": "",
		"def2": "",
		"def3": "",
		"def4": "",
		"def5": ""
	}
}
 * */
public class TH_LIMS_ICSTATE_CHANGE extends AbstracAdapter4Ext{
	
	
	private HYPubBO pubBO;
	private HYPubBO getPubBO(){
		if(null==pubBO){
			pubBO=new HYPubBO();
		}
		return pubBO;
	}
	String flowid = null;

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		JSONObject rqJosn = (JSONObject) billvo;
		/*
		 * 2023年3月16日 默认操作人 均为LIMS 用户
		 * */
		rqJosn.getJSONObject("data").put("usercode", "LIMS");
		//检索数据
		try {
			
			flowid= rqJosn.getJSONObject("data").getString("id");
			if(flowid!=null && flowid.startsWith("IPC28")){
				this.IPC(rqJosn);
			}else{
				dealRespData(rqJosn);
			}
		} catch (Exception e) {
			return JZYYResultMessageUtil.getFailedRsultJson( "创建失败 "+e.getMessage());
		}
	
		//将结果返回			
		return JZYYResultMessageUtil.getSuccessRsultJson("创建成功",rqJosn);
	}
	
	/*
	 * IPC处理 
	 * 2022年12月2日
	 * */
	private void IPC(JSONObject rqJosn) throws BusinessException{
		JSONObject jsonObject = rqJosn.getJSONObject("data");
		if(!jsonObject.containsKey("def2") || StringUtils.isEmpty(jsonObject.getString("def2"))){
			throw new BusinessException("IPC业务 def2 参数不能为空!");
		}
		if(!jsonObject.containsKey("cstate") || StringUtils.isEmpty(jsonObject.getString("cstate"))){
			throw new BusinessException("IPC业务 cstate 参数不能为空!");
		}
		
		//根据def2 查询IPC记录表
		LimsIpcVO[] ipcVOs=(LimsIpcVO[])this.getPubBO().queryByCondition(LimsIpcVO.class, "ID='"+jsonObject.getString("def2")+"' and dr=0");
		if(ArrayUtils.isEmpty(ipcVOs)){
			throw new BusinessException("为查询到 "+jsonObject.getString("def2")+" 对应的IPC请验记录!");
		}
		//hege buhege 转为汉字
		String cstate=jsonObject.getString("cstate");
		if("buhege".equals(jsonObject.getString("cstate"))){
			cstate="不合格";
		}else if("M".equals(jsonObject.getString("cstate"))){
			cstate="合格";
		}
		//放行人编码转名称
		String psname=jsonObject.getString("usercode");
		UserVO[] userVOs=(UserVO[])this.getPubBO().queryByCondition(UserVO.class, "user_code='"+jsonObject.getString("usercode")+"' and enablestate=2");
		if(null!=userVOs && userVOs.length>0){
			psname=userVOs[0].getUser_name();
		}
		String upd_sql="UPDATE TH_LIMS_IPC set Qc_final='"+cstate+"',Qc_passs_psn='"+psname+"',Qc_passs_time='"+new UFDateTime().toString()+"' WHERE ID='"+jsonObject.getString("def2")+"'";
		/*ipcVOs[0].setStatus(VOStatus.UPDATED);
		ipcVOs[0].setQc_final(jsonObject.getString("cstate"));
		ipcVOs[0].setQc_passs_psn(jsonObject.getString("usercode"));
		ipcVOs[0].setQc_passs_time(new UFDateTime().toString());*/
		Log.getInstance("IPC结果回传").equals("UPD SQL："+upd_sql);
		int executeUpdate = getDao().executeUpdate(upd_sql);
		Log.getInstance("IPC结果回传").error("IPC结果回传:"+rqJosn.toJSONString()+" ,更新结果："+executeUpdate);
	}
	   
	/**
	 * 
	 * @return
	 * @throws BusinessException 
	 */
	private List<Map<String, Object>> dealRespData(JSONObject rqJosn) throws BusinessException {
		
//		物料编码	material_code
//		物料名称	material_name
//		批次号	vbatchcode
//		库存维度	pk_onhanddim
//		失效/复测日期dvalidate
//		放行主数量	num
//		库存状态	 cstate
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
	
		String cstate = rqJosn.getJSONObject("data").getString("cstate");
		//String cqualitylevel = rqJosn.getJSONObject("data").getString("cqualitylevel");
		String org_code = rqJosn.getJSONObject("data").getString("org_code");
		String group = rqJosn.getJSONObject("data").getString("group");
		String  pk_group=getPk_group(group);
		String[]  pk_orgs=new String[]{getPk_financeorg(org_code)};
		String usercode = rqJosn.getJSONObject("data").getString("usercode");
		String pk_user=getPk_user(usercode);
		//String cstate=getCState(cqualitylevel);	
		
		//是否封存
		UFBoolean includebseal =  UFBoolean.FALSE;
		//查询批次档案信息
		BatchcodeVO[] batchcodes = getBatchcodeVO(includebseal, new UFDate("2022-10-9"),vbatchcode,material_code);
		if (batchcodes == null || batchcodes.length <= 0) {
			throw new BusinessException("未检索到批次信息:物料="+material_code+"批次="+vbatchcode);
		}
		
		
		/*
		 * 执行完成后反查批次号， 将对应批次号的质量等级全改回传的状态
		 */
		this.executeTaskAfutToBatchcode(batchcodes,cstate);
		
		
		//现存量的质量等级
		StoreStateVO targetStateVO = getStoreStateByCode(cstate,pk_group,pk_orgs[0]);
		String[] srcStates=new String[]{"null"};
		String sql = getOnhandWhereSql(batchcodes, pk_group, pk_orgs, srcStates);
		OnhandVO[] onhands = getOnhandVOs(sql);

		//检验完成才允许入库的,可能还没库存信息
		if (onhands == null || onhands.length <= 0) {
			/*
			 * 2023年1月9日  根据批次号vdef15 是否为VAR 
			 * 如果没有现存量 则直接返回
			 * */
			if(StringUtils.isNotEmpty(batchcodes[0].getVdef15()) 
					&& "VAR".equals(batchcodes[0].getVdef15())){
				return null;
			}
				
			throw new BusinessException("未检索到对应的库存信息");
		}		
		StateAdjustVO[] stateAdjustVOs = processOnhandVO(onhands);
		String[] pk_onhanddims = null;
		if (stateAdjustVOs != null && stateAdjustVOs.length > 0) {
			pk_onhanddims = doStateAdjust(stateAdjustVOs, targetStateVO,pk_user);
		}
		return null;
	}
	
	/**
	 * 判断是否没有现存量
	 * @return
	 */
	public boolean isNotOnhandNum(List<Map<String,Object>> respData ){
		if(respData==null || respData.size()==0){
			return true;
		}
		
		
		//判断num是否等于0
		Map<String, Object> map = respData.get(0);
		if(null!=map.get("num") && Double.valueOf(map.get("num").toString())==0){
			return true;
		}
		return false;
	}
	

	/**
	 * 根据条件获取物料结存记录
	 * 
	 * @throws BusinessException
	 * @throws nc.vo.pub.BusinessException 
	 */
	private OnhandVO[] getOnhandVOs(String where) throws BusinessException, nc.vo.pub.BusinessException {
		//(ic_onhanddim.pk_org = '0001V110000000012E56' AND T1.code = 'R8104' AND ic_onhanddim.vbatchcode = 'R810400117-1')and ic_onhanddim.pk_onhanddim in (select dim.pk_onhanddim from ic_onhanddim dim left outer join bd_stordoc bd_stordoc on dim.cwarehouseid = bd_stordoc.pk_stordoc where bd_stordoc.gubflag = 'N' )
		// 1=1  and  pk_org in ( '0001V11000000000374G')  and  pk_batchcode in ( '1001V1100000000D2JUU') 
		// 现存量查询接口
		IOnhandQry onbandquery = NCLocator.getInstance().lookup(IOnhandQry.class);
		// 通过查询条件，返回现存量
		OnhandQryCond cond = new OnhandQryCond();
		cond.setISSum(true);
		cond.addSelectFields(CollectionUtils.combineArrs(OnhandDimVO.getDimContentFields(),new String[] { OnhandDimVO.PK_ONHANDDIM }));// 加入现存量维度及现存量维度pk
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
			String cstate) throws BusinessException {
		 List<BatchcodeVO> list = new ArrayList<BatchcodeVO>();
		 
		UFDate dvalidate = new UFDate();
		UFDateTime systime = new UFDateTime();

		SmartServiceImpl serve = new SmartServiceImpl();
		BatchOperateVO batchOperate = null;
	
		//失效/复测日期
		/*
		 * 
		 * 批次号中增加的批号报检次数为判断依据，
		 * 如果报检次数Vdef14 =1时，为初次检验，此时失效日期为生产日期加保质期减1
		 * 如果报检次数Vdef14 >1时，该物料号为复测批次，此时失效复测日期为放行日期加定期复检周期减1；
		 * 放行日期：LIMS回传调用库存状态变更的时候回传的时系统日期
		 * */
		MaterialStockVO[] materialStockVOs=(MaterialStockVO[])this.getPubBO().queryByCondition(MaterialStockVO.class, "pk_material='"+batchcodes[0].getCmaterialoid()+"' and dr=0");
		//保质期
		int qualitynum=0;
		//定期检验周期
		int cyclecheck=0;
		if(null==materialStockVOs || materialStockVOs.length==0){
			throw new BusinessException("未检索到物料库存信息");
		}else{
			if(null!=materialStockVOs[0].getQualitynum()){
				qualitynum=materialStockVOs[0].getQualitynum();
			}
			if(null!=materialStockVOs[0].getCyclecheck()){
				cyclecheck=materialStockVOs[0].getCyclecheck();
			}
		}
		
		for (BatchcodeVO batchcode : batchcodes) {
			//String cqualitylevelid = batchcode.getCqualitylevelid();
			//batchcode.setCqualitylevelid(pk_qualitylv_b);
			batchcode.setStatus(VOStatus.UPDATED);
			//在检状态
			batchcode.setBinqc(UFBoolean.FALSE);
			//备注
			batchcode.setVnote("LIMS调整");
			//tchecktime  上次检验日期  2023年2月28日 增加
			batchcode.setTchecktime(systime);
			//如此传入cstate 是合格 才重算复检更新  否则不做任何处理2023年3月2日
			if(flowid!=null && flowid.startsWith("KC28")){
				if("M".equals(cstate)){
					//报检次数
					int times = 0;
					if(StringUtils.isNotEmpty(batchcode.getVdef4())){
						 times=Integer.parseInt(batchcode.getVdef4());
					}
					//失效复测日期为放行日期加定期复检周期cyclecheck 减1 
					UFDate dateBefore = dvalidate.getDateAfter(cyclecheck).getDateBefore(1);
					batchcode.setDvalidate(dateBefore);
//					if(times>1){
//						//失效复测日期为放行日期加定期复检周期cyclecheck 减1 
//						UFDate dateBefore = dvalidate.getDateAfter(cyclecheck).getDateBefore(1);
//						batchcode.setDvalidate(dateBefore);
//					}else{
//						//生产日期加保质期减1
//						UFDate dateBefore = batchcode.getDproducedate().getDateAfter(qualitynum).getDateBefore(1);
//						batchcode.setDvalidate(dateBefore);
//					}
				
				}
			}

		
			list.add(batchcode);
		}
		batchOperate = new BatchOperateVO();
		batchOperate.setUpdObjs(list.toArray(new BatchcodeVO[0] ));
		serve.batchSave(batchOperate);
	}


	
	private String getCState(String cqualitylevel) throws BusinessException {
		//质量等级 (scm_qualitylevel_b) 
		 String sql="select  b.vcode   from scm_qualitylevel_b h ,ic_storestate b  where  h.  pk_stockstate=b.pk_storestate  and h.cqualitylvcode ='"+cqualitylevel+"' and h.dr=0";
//		 "select  pk_financeorg    from org_stockorg where code ='"+code  +"' and dr=0";			
	Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
	
	 String vcode=(obj==null?null:obj.toString());
	 return vcode;
	}

	private String getPk_group(String code  ) throws BusinessException {
		 String sql="select  pk_group  from org_group where code ='"+code  +"' and dr=0";			
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
		IStateAdjustMaintain stateAdjustService = NCLocator.getInstance().lookup(IStateAdjustMaintain.class);
		for (StateAdjustVO stateAdjust : bills) {
			try {
				stateAdjustService.stateAdjust(new StateAdjustVO[] { stateAdjust });
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
	private StoreStateVO getStoreStateByCode(String code, String pk_group,String pk_org)
			throws BusinessException {
		VOQuery<StoreStateVO> query = new VOQuery<>(StoreStateVO.class);
		StringBuilder sql = new StringBuilder();
		sql.append("  and " + StoreStateVO.PK_GROUP + " = '" + pk_group + "'");
		sql.append(" and " + StoreStateVO.VCODE + " = '" + code + "'");
		sql.append(" and " + StoreStateVO.PK_ORG + " = '" + pk_org + "'");

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
		StateAdjustVO[] bills = this.translateResult(onhandvos);
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
			/* 
			 * edit by cc 此处校验会导致无法生成状态调整单
			 * 结存主数量-冻结数量>0；预留主数量，冻结数量满足至少有一个为空或零
			if (tsMap.get(onhandVO.getPk_onhanddim()) == null
					|| !canStateAdjust(onhandVO)) {
				continue;
			}*/

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
		// 排除 现存量维度为0的记录 2023年2月22日
		sql.append(" and nonhandnum > 0");
		return sql.toString();
	}

	/**
	 * 获取批次号档案
	 * @return
	 * @throws BusinessException
	 */
	private BatchcodeVO[] getBatchcodeVO(UFBoolean includebseal,
			UFDate criticalDate,String vbatchcode,String material_code ) throws BusinessException {
		//UFDate criticalDate = BSContext.getInstance().getDate().getDateBefore(daysBefore);
		StringBuilder sql = new StringBuilder();
		sql.append(" and exists (");
		sql.append("    select 1 from bd_material_v material  ");
		sql.append(" 	where material.pk_material = cmaterialoid ");
		sql.append("    and material.code ='"+material_code+"' ");
		sql.append(" 	and isnull(material.dr, 0) = 0 ");
		sql.append(" 	and isnull(material.enablestate, 2) = 2 ");
		sql.append(" )");
		/*sql.append(" and substr(dvalidate, 0, 10) < '").append(criticalDate).append("'");*/
		sql.append(" and dvalidate is not null");
		sql.append(" and  vbatchcode='"+vbatchcode+"'");
		
		//	是否封存
		if (!includebseal.booleanValue()) {
			sql.append(" and nvl(bseal,'N') = 'N'   ");
		}
		
		VOQuery<BatchcodeVO> query = new VOQuery<>(BatchcodeVO.class);
		BatchcodeVO[] batchcodes = query.query(sql.toString(), null);

		return batchcodes;
	
	}
	
	
	
	/*
	 *获取质量等级主键 
	 *@return
	 *@throw  BusinessException
	 * hw 2021-12-07
	 * */
	private String getQualitylv(String cqualitylvcode) throws BusinessException {
//		String sql ="select  pk_qualitylv_b from scm_qualitylevel_b where bqualified  = 'Y' and cqualitylvcode ='"+zldj+"' ";	
		
//		--允许放行为任意质量等级  @Hw  2021-12-10 
		String sql ="select  pk_qualitylv_b from scm_qualitylevel_b where  cqualitylvcode ='"+cqualitylvcode+"' ";	
		Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		if(obj == null){
			throw new BusinessException("错误的质量等级(NC未匹配到):"+cqualitylvcode);
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
		if(this.getCState(cqualitylevel).equals("M")){
			return UFBoolean.TRUE;
		}
		return UFBoolean.FALSE;
	}
}
	

