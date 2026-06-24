/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.bs.gmp.release.main.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.bd.material.stock.IMaterialStockQueryService;
import nc.itf.gmp.release.main.IReleaaseTurnCardService;
import nc.itf.gmp.release.main.ReleaseNoticeVO;
import nc.itf.gmp.release.main.ReleaseNoticeVO.MessageType;
import nc.itf.ic.m4z.IFreezeThaw;
import nc.itf.ic.onhand.IMedOnhandDim;
import nc.itf.qc.c003.main.IHdTurnService;
import nc.itf.qc.c003.maintain.IReportMaintain;
import nc.itf.qc.c003.print.IHdReportBillService;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.ic.m4z.qc.QueryFreezeThawUtils;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.pubitf.pu.m23.pubquery.IArrivePubQuery;
import nc.pubitf.qc.c001.pub.IApplyPubQuery;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.vo.bd.material.MaterialConvertVO;
import nc.vo.bd.material.marasstframe.MarAsstFrameVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.bd.stordoc.StordocVO;
import nc.vo.gmp.release.AggMedReleaseVO_148;
import nc.vo.gmp.release.MedReleaseHVO_148;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.m4c.entity.SaleOutBodyVO;
import nc.vo.ic.m4c.entity.SaleOutVO;
import nc.vo.ic.m4i.entity.GeneralOutHeadVO;
import nc.vo.ic.m4i.entity.GeneralOutVO;
import nc.vo.ic.m4z.entity.FreezeThawVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
import nc.vo.ic.onhand.entity.OnhandSNVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.mmpac.wr.entity.WrQualityVO;
import nc.vo.mmsfc.operationrep.entity.OprepItemVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.calculator.formula.NumConvertRateFormula;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.qc.c001.entity.ApplyHeaderVO;
import nc.vo.qc.c001.entity.ApplySourceItemVO;
import nc.vo.qc.c001.entity.ApplyVO;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c007.entity.SamplingHeadVO;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

import org.apache.commons.lang.StringUtils;

public class ReleaseTurnCardServiceImpl implements IReleaaseTurnCardService {
	
	private final Set<String> acceptedTypeCodes = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("4C","23","4Z","55A4")));
	
	private Map<String, MaterialStockVO> materialStockMap = new HashMap<>();
	
	private Map<String, MaterialConvertVO> materialconMap = new HashMap<>(); //物料计量单位，一次查询，方便后续使用
	
	private Map<String, MarAsstFrameVO> materialAsstFrameMap = new HashMap<>();
	
	private Map<String, StoreStateVO> storeStateMap = new HashMap<>();
	
	private Map<String, StordocVO> stordocMap = new HashMap<>();

	@Override
	public ReleaseNoticeVO beforeDoTurnReportVOAction(AggMedReleaseVO_148 releaseAggvo) throws BusinessException {
		MedReleaseHVO_148 releaseHVO = (MedReleaseHVO_148) releaseAggvo.getParent();
		String pk_material = releaseHVO.getCmaterialvid();
		//单据检查
		Integer fstatusflag = releaseHVO.getFstatusflag();
        //检查审批态
		if(fstatusflag == null || fstatusflag != 1){
			return ReleaseNoticeVO.create(false, "放行单未审批通过，不允许翻牌！");
		}
		//查询是否已经翻牌
		if (releaseHVO.getIsturn() != null && releaseHVO.getIsturn().booleanValue()) {
			return ReleaseNoticeVO.create(false, "已经翻牌，不能重复翻牌！");
		}
		Integer ipassway = releaseHVO.getIpassway();
		if(ipassway == null){
			return ReleaseNoticeVO.create(false, "放行处理意见不能为空！");
		}
		if(ipassway > 5 || ipassway < 1){
			return ReleaseNoticeVO.create(false, "放行处理意见不能识别！");
		}
		if(ipassway == 4){
			return ReleaseNoticeVO.create(false, "放行处理意见是返工！！！");
		}
		MarAsstFrameVO marassframe = getMaterialMarAsst(pk_material);
		if (marassframe != null && !marassframe.getFix1().booleanValue()) {
			return ReleaseNoticeVO.create(false, "物料未启动库存状态，不能翻牌！");
		}
		String reportid = releaseHVO.getVsrcid();
  		//质检报告检查
		if (!"C003".equals(releaseHVO.getVsrctype()) || StringUtils.isBlank(reportid)) {
			return ReleaseNoticeVO.create(false, "质检报告未找到");
		}
  		//查询质检报告
		ReportVO reportVO = NCLocator.getInstance().lookup(IReportMaintain.class)
				.querySingleBillByPkAndBatchInfo(reportid); 
		if(reportVO == null){
			return ReleaseNoticeVO.create(false, "质检报告未找到");
		}
		IHdReportBillService billService = NCLocator.getInstance().lookup(IHdReportBillService.class);
  		//是否是默认方案
		if(!billService.isDefaultstdReportVO(reportVO)){
			return ReleaseNoticeVO.create(false, "不是默认方案");
		}
		// 获取表体VO
		ReportItemVO[] reportItemVOs = reportVO.getBVO();
		if(reportItemVOs==null || reportItemVOs.length<=0){
			return ReleaseNoticeVO.create(false, "来源报检单表体为空，不能翻牌！");
		}
		String applyid = reportItemVOs[0].getCsourceid();//来源报检单pk
		if (applyid == null || "".equals(applyid)) {
			return ReleaseNoticeVO.create(false, "来源报检单为空，不能翻牌！");
		}
		// 批量查询报检单<表头主键, 对应的VO>
		IApplyPubQuery applyPubQuery = NCLocator.getInstance().lookup(IApplyPubQuery.class);
		Map<String, ApplyVO> map = applyPubQuery.queryApplyVOByHid(new String[]{applyid});
		List<ApplyVO> list = new ArrayList<ApplyVO>(map.values());
		ApplyVO[] applyVOs = (ApplyVO[])list.toArray(new ApplyVO[0]);
		if(applyVOs == null || applyVOs.length <= 0){
			return ReleaseNoticeVO.create(false, "来源报检单数据为空，不能翻牌！");
		}
		
		//判断报检单的来源
		ApplyVO applyVO= applyVOs[0];
		ApplyHeaderVO applyHeaderVO= applyVO.getHVO();
		ApplySourceItemVO asvo = applyVO.getB2VO()[0];
		String csourcetypecode = asvo.getCsourcetypecode();// 报检单上游单据来源
		String csourcebid = asvo.getCsourcebid();
		if(csourcetypecode == null || "".equals(csourcetypecode)){
			return ReleaseNoticeVO.create(false, "来源报检单来源为空，不能翻牌！");
		}
		if(!acceptedTypeCodes.contains(csourcetypecode)){
			return ReleaseNoticeVO.create(false, "来源报检单来源需为到货单、红色销售出库、库存存量、完工报告，否则不能翻牌！");
		}
		// 获取表头VO
		ReportHeaderVO reportHeaderVO = reportVO.getHVO();
		//判断是否是默认方案
		if(!reportHeaderVO.getPk_chkstd_v().equals(applyHeaderVO.getPk_defaultstd_v())){
			return ReleaseNoticeVO.create(false, "质检报告方案与报检单默认方案不一致，不能翻牌！");
		}
		MaterialStockVO stockInfo = getMaterialStockInfo(releaseHVO.getPk_org(), releaseHVO.getCmaterialvid());
		if (stockInfo == null) {
			return ReleaseNoticeVO.create(false, "未找到库存信息!!!");
		}
		if (csourcetypecode.startsWith("23")) {
			//  物料勾选根据检验结果入库
			if (stockInfo.getStockbycheck().booleanValue()) {
				return ReleaseNoticeVO.create(false, "物料勾选了根据检验结果入库, 无需翻牌!");
			}
			ArriveItemVO arriveItemVO = NCLocator.getInstance().lookup(IArrivePubQuery.class).queryItemVOByBid(csourcebid);
			if (arriveItemVO == null) {
				return ReleaseNoticeVO.create(false, "到货单表体未找到!");
			}
			//2020-11-23 云峰网络 wangnw 1.签字校验：放行单翻牌的时候，需要校验该批次的入库单是否签字。如果没有签字，则对应的放行单不能翻牌，并给出提示：“入库单签字后才能翻牌！”
			IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String sql = " select a.fbillflag" + "\n" +
							" from  ic_purchasein_h a,ic_purchasein_b b" + "\n" + 
							" where a.cgeneralhid=b.cgeneralhid and a.dr=0 and b.dr=0 " + "\n" + 
							" and a.fbillflag!=3" + "\n" + 
							" and b.csourcebillbid ='"+arriveItemVO.getPk_arriveorder_b()+"'";
			Map map2 = (Map)iuap.executeQuery(sql, new MapProcessor());
			if (map2!=null&&map2.size()>0 ) {
				return ReleaseNoticeVO.create(false, "入库单签字后才能翻牌!");
			}
			
			// 到货单表体累计入库数量>0
			UFDouble naccumstorenum = arriveItemVO.getNaccumstorenum();
			if (naccumstorenum == null || naccumstorenum.doubleValue() <= 0) {
				return ReleaseNoticeVO.create(false, "放行单不满足翻牌条件 : 入库数量需大于零!");
			}
		} else if (csourcetypecode.startsWith("55D2")) {
			//工序完工报告
			if (stockInfo.getStockbycheck().booleanValue()) {
				return ReleaseNoticeVO.create(false, "物料勾选了根据检验结果入库, 无需翻牌!");
			}
			OprepItemVO[] vos = new VOQuery<>(OprepItemVO.class).query(new String[] { csourcebid });
			if (vos == null || vos.length == 0) {
				return ReleaseNoticeVO.create(false, "完工报告表体未找到！");
			}
		} else if (csourcetypecode.startsWith("55A4")) {
			//生产报告
			WrQualityVO[] qualityVOs = new VOQuery<>(WrQualityVO.class).query(" and pk_wr_product_q='" + csourcebid + "' ", "");
			if (qualityVOs == null || qualityVOs.length == 0) {
				return ReleaseNoticeVO.create(false, "生产报告表体未找到！");
			}
			UFDouble nginastnum = UFDouble.ZERO_DBL;// 实际入库数量（含报废）
			UFDouble ngaldrmkastnum = UFDouble.ZERO_DBL;// 已返工数量
			UFDouble nghandoverastnum = UFDouble.ZERO_DBL;// 已转移数量
			for (WrQualityVO vo : qualityVOs) {
				UFDouble t1 = vo.getNginastnum();
				UFDouble t2 = vo.getNgaldrmkastnum();
				UFDouble t3 = vo.getNghandoverastnum();
				nginastnum = nginastnum.add(t1 == null ? UFDouble.ZERO_DBL : t1);
				ngaldrmkastnum = ngaldrmkastnum.add(t2 == null ? UFDouble.ZERO_DBL : t2);
				nghandoverastnum = nghandoverastnum.add(t3 == null ? UFDouble.ZERO_DBL : t3);
			}
			UFDouble sum = nginastnum.add(ngaldrmkastnum).add(nghandoverastnum);
			if (sum.doubleValue() <= 0) {
				return ReleaseNoticeVO.create(false, "放行单不满足翻牌条件! \n质量信息：入库数量+返工数量+转移数量+报废数量之和需大于零!");
			}
		}
		// 放行单 处理意见 ，放行 1，退货2，报废3，返工4，放行冻结5
		// 到货单(23) 1，2，3 
		// 销售出库(4C) 1，2 
		// 库存(4Z) 1，2，3  
		// 完工报告(55A4) 1，2，3，5 
		// 工序报告没有翻牌
		switch (ipassway) {
		case 1:
		case 5:
			return passWayForInstore(reportVO, applyVO, releaseAggvo);
		case 2:
		case 3:
			return passWayForOuttrashin(reportVO, applyVO, releaseAggvo);
		default:
			return ReleaseNoticeVO.create(false, "放行处理意见是返工！！！");
		}
	}
	
	public ReleaseNoticeVO passWayForOuttrashin(ReportVO reportVO, ApplyVO applyVO, AggMedReleaseVO_148 releaseVO) throws BusinessException {
		String csourcetype = applyVO.getB2VO()[0].getCsourcetypecode();
		if(!acceptedTypeCodes.contains(csourcetype)){
			return ReleaseNoticeVO.create(false, "未知类型报检，不支持放行翻牌！");
		}
		MedReleaseHVO_148 releaseHVO_148 = (MedReleaseHVO_148)releaseVO.getParent();
		// 库存组织
		String pk_org = reportVO.getHVO().getPk_org();
		// 集团
		String pk_group = reportVO.getHVO().getPk_group();
		// 物料
		String pk_material = reportVO.getHVO().getPk_material();
		MaterialStockVO stockvo = getMaterialStockInfo(pk_org, pk_material);
		if(stockvo == null ){
			throw new BusinessException("未找到库存信息!");
		}
		ReleaseNoticeVO releaseNotice = null;
		StoreStateVO unPassState = getStockStateByName("不合格", pk_group);
		Set<String> unableStoreStatePks = new HashSet<>();
		if (csourcetype.startsWith("23")) {
			releaseNotice = releaseForDefault(reportVO, applyVO, releaseVO, false);
		} else if (csourcetype.startsWith("4C")) {
			unableStoreStatePks.add(unPassState.getPk_storestate());
			releaseNotice = releaseFor4C(reportVO, applyVO, releaseVO, unableStoreStatePks);
		}else if (csourcetype.startsWith("4Z")) {
			unableStoreStatePks.add(unPassState.getPk_storestate());
			releaseNotice = releaseFor4Z(reportVO, applyVO, releaseVO, unableStoreStatePks);
		} else if (csourcetype.startsWith("55A4")) {
		    releaseNotice = releaseForDefault(reportVO, applyVO, releaseVO, false);
		} else {
			releaseNotice = releaseForDefault(reportVO, applyVO, releaseVO, false);//此处还需处理：库存组织+物料+仓库 +批次+匹配库存状态为‘待检’|‘待确定’|‘合格’的对应物料批次表现存量;
		}
		StateAdjustVO[] adjustVOs = releaseNotice.getResults();
		if (adjustVOs != null && adjustVOs.length > 0) {
			for (StateAdjustVO vo : adjustVOs) {
				defaultValue(vo, reportVO.getHVO(), releaseHVO_148, csourcetype);
				vo.setCadjuststateid(unPassState.getPk_storestate());
			}
		}
		//设置展示信息
		releaseNotice.setReportVO(reportVO);
		releaseNotice.setReleaseHVO(releaseHVO_148);
		releaseNotice.setCsourcetypecode(csourcetype);
		return releaseNotice;
	}
	
	/**
	 * 放行方式入库
	 *
	 * @param applyHeaderVO 
	 * @throws BusinessException 
	 * @date 2014-10-20
	 */
	public ReleaseNoticeVO passWayForInstore(ReportVO reportVO, ApplyVO applyVO, AggMedReleaseVO_148 releaseVO) throws BusinessException {
		String csourcetype = applyVO.getB2VO()[0].getCsourcetypecode();
		if(!acceptedTypeCodes.contains(csourcetype)){
			return ReleaseNoticeVO.create(false, "未知类型报检，不支持放行翻牌！");
		}
		MedReleaseHVO_148 releaseHVO_148 = (MedReleaseHVO_148)releaseVO.getParent();
		// 库存组织
		String pk_org = reportVO.getHVO().getPk_org();
		// 集团
		String pk_group = reportVO.getHVO().getPk_group();
		// 物料
		String pk_material = reportVO.getHVO().getPk_material();
		MaterialStockVO stockvo = getMaterialStockInfo(pk_org, pk_material);
		if(stockvo == null ){
			throw new BusinessException("未找到库存信息!");
		}
		ReleaseNoticeVO releaseNotice = null;
		StoreStateVO passState = getStockStateByName("合格", pk_group);
		Set<String> unableStoreStatePks = new HashSet<>();
		unableStoreStatePks.clear();
		if (csourcetype.startsWith("23")) {
			releaseNotice = releaseForDefault(reportVO, applyVO, releaseVO, true);
		} else if (csourcetype.startsWith("4C")) {
			unableStoreStatePks.add(passState.getPk_storestate());
			releaseNotice = releaseFor4C(reportVO, applyVO, releaseVO, unableStoreStatePks);
		}else if (csourcetype.startsWith("4Z")) {
			unableStoreStatePks.add(passState.getPk_storestate());
			releaseNotice = releaseFor4Z(reportVO, applyVO, releaseVO, unableStoreStatePks);
		} else if (csourcetype.startsWith("55A4")) {
			releaseNotice = releaseForDefault(reportVO, applyVO, releaseVO, true);
		} else {
			return releaseNotice = releaseForDefault(reportVO, applyVO, releaseVO, true);//此处还需处理：库存组织+物料+仓库 +批次+匹配库存状态为‘待检’|‘待确定’|‘不合格’的对应物料批次表现存量;
		}
		StateAdjustVO[] adjustVOs = releaseNotice.getResults();
		if (adjustVOs != null && adjustVOs.length > 0) {
			for (StateAdjustVO vo : adjustVOs) {
				defaultValue(vo, reportVO.getHVO(), releaseHVO_148, csourcetype);
				vo.setCadjuststateid(passState.getPk_storestate());
			}
		}
		//设置展示信息
		releaseNotice.setReportVO(reportVO);
		releaseNotice.setReleaseHVO(releaseHVO_148);
		releaseNotice.setCsourcetypecode(csourcetype);
		return releaseNotice;
	}
	
	/**
	 * 到货报检(先入库后报检)/完工报检，此时翻牌不考虑具体维度，只按照库存组织+物料+批次进行整批翻牌
	 * @param reportVO
	 * @param applyVO
	 * @param releaseVO
	 * @param unableStoreStatePks
	 * @return
	 * @throws BusinessException
	 */
	private ReleaseNoticeVO releaseForDefault(ReportVO reportVO, ApplyVO applyVO, AggMedReleaseVO_148 releaseVO, boolean passFlag) throws BusinessException {
		MedReleaseHVO_148 releaseHVO_148 = (MedReleaseHVO_148)releaseVO.getParent();
		// 批次号
		String vbatchcode = getRealVbatchcode(releaseHVO_148.getPk_batchcode());
		// 库存组织
		String pk_org = reportVO.getHVO().getPk_org();
		// 集团
		String pk_group = reportVO.getHVO().getPk_group();
		// 物料
		String pk_material = reportVO.getHVO().getPk_material();
		
		
		
		String[] states = passFlag ? new String[] {"待确定", "待检", "不合格"} : new String[] {"待确定", "待检", "合格"};
		
		IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//2021-03-21 云峰网络  wangnw 2. 批次号存量校验：放行单放行时，以批次号进行存量维度检索，过滤掉不合格状态/报废状态物料存量，其它状态存量进行统一翻牌。
//		【YF604放行翻牌查询算法方案】
		String yf604 = SysinitAccessor.getInstance().getParaString(pk_org, "YF604");

		Set<String> ableStateids = new HashSet<>();
		for (String state : states) {
			ableStateids.add(getStockStateByName(state, pk_group).getPk_storestate());
		}
		StringBuilder whereSql = new StringBuilder();
		whereSql.append(" pk_org = '" + pk_org + "' ");
		whereSql.append(" and vbatchcode = '" + vbatchcode + "' ");
		whereSql.append(" and cmaterialvid = '" + pk_material + "' ");
		if("批次号".equals(yf604)){
//			1. 修改翻盘按钮中翻牌展示信息的查询算法，根据参数【YF604放行翻牌查询算法方案】判断需要执行的查询算法，参数值为空或者维度，则保持系统当前算法，如果参数是批次号则新增算法，只根据批次号去检索过滤掉不合格状态/报废状态后的所有存量

//			根据批次号去检索过滤掉不合格状态/报废状态后的所有存量  没有报废状态 
			String[] states2 =  new String[] { "报废", "不合格"};
			Set<String> ableStateids2= new HashSet<>();
			for (String state : states) {
				ableStateids2.add(getStockStateByName(state, pk_group).getPk_storestate());
			}		
			String  pk=getStockStateByName("不合格", pk_group).getPk_storestate();
			whereSql.append(" and cstateid not in ('" + StringUtils.join(ableStateids2.toArray(new String[0]), "', '") + "') ");
		}else{
			whereSql.append(" and cstateid in ('" + StringUtils.join(ableStateids.toArray(new String[0]), "', '") + "') ");
		}
		
		
		whereSql.append(" and pk_group = '" + pk_group + "' ");
		ReleaseNoticeVO notice = getStateAdjustVO(whereSql.toString());
		if (!notice.isSuccess()) {
			return notice;
		}
		List<StateAdjustVO> stateAdjustVOs = notice.getObj();
		if(stateAdjustVOs == null || stateAdjustVOs.size() <= 0){
			return ReleaseNoticeVO.create(false,"到货单入库库存调整单获取失败！！！");
		}
		notice.setResults(stateAdjustVOs.toArray(new StateAdjustVO[0]));
		return notice;
	}
	
	private ReleaseNoticeVO releaseFor4C(ReportVO reportVO, ApplyVO applyVO, AggMedReleaseVO_148 releaseVO, Set<String> unableStoreStatePks) throws BusinessException {
		ApplySourceItemVO[] asvos = applyVO.getB2VO();
		if (asvos == null || asvos.length <= 0) {
			return ReleaseNoticeVO.create(false, "无法查询到来源报检单的报检点明细！！!");
		}
		String csourceid = asvos[0].getCsourceid();
		Set<String> csourrcebidSet = new HashSet<>();
		for (int i = 0; i < asvos.length; i++) {
			String csourcebid = asvos[i].getCsourcebid();
			csourrcebidSet.add(csourcebid);
		}
		MedReleaseHVO_148 releaseHeader = (MedReleaseHVO_148)releaseVO.getParentVO();
		String pk_material = releaseHeader.getCmaterialvid();
		String pk_group = releaseHeader.getPk_group();
		BillQuery<SaleOutVO> query = new BillQuery<SaleOutVO>(SaleOutVO.class);
		SaleOutVO[] saleOutVOs = query.query(new String[]{ csourceid });
		if (saleOutVOs == null || saleOutVOs.length <= 0) {
			return ReleaseNoticeVO.create(false, "销售出库单未找到!");
		}
		SaleOutVO saleOutVO = saleOutVOs[0];
		SaleOutBodyVO[] saleOutBodyVOs = saleOutVO.getBodys();
		if (saleOutBodyVOs == null || saleOutBodyVOs.length <= 0) {
			return ReleaseNoticeVO.create(false, "销售出库表体未找到!");
		}
		List<SaleOutBodyVO> newSaleOutBodys = new ArrayList<>();
		for (SaleOutBodyVO body : saleOutBodyVOs) {
			if (csourrcebidSet.contains(body.getCgeneralbid())) {
				newSaleOutBodys.add(body);
				csourrcebidSet.remove(body.getCgeneralbid());
			}
		}
		if (csourrcebidSet.size() > 0) {
			return ReleaseNoticeVO.create(false, "销售出库表体未找到!");
		}
		saleOutVO.setChildren(SaleOutBodyVO.class, newSaleOutBodys.toArray(new SaleOutBodyVO[0]));
		IMedOnhandDim service = NCLocator.getInstance().lookup(IMedOnhandDim.class);
		OnhandVO[] onhandVOs = service.getOnhandDimByICBillBody(saleOutVO);
		if (onhandVOs == null || onhandVOs.length <= 0) {
			return ReleaseNoticeVO.create(false,"无法找到对应销售出库单据的现存量维度数据，请检查！！！");
		}
		Set<String> pk_onhanddims = new HashSet<>();
		// 退货对应实际入库主辅数量
		Map<String, UFDouble> dimInstoreNum = new HashMap<>();
		Map<String, UFDouble> dimInstoreAstNum = new HashMap<>();
		for (OnhandVO vo : onhandVOs) {
			pk_onhanddims.add(vo.getPk_onhanddim());
			UFDouble instoreNum = dimInstoreNum.containsKey(vo.getPk_onhanddim()) ? 
					dimInstoreNum.get(vo.getPk_onhanddim()) : UFDouble.ZERO_DBL;
			UFDouble instoreAstNum = dimInstoreAstNum.containsKey(vo.getPk_onhanddim()) ? 
			        dimInstoreAstNum.get(vo.getPk_onhanddim()) : UFDouble.ZERO_DBL;
			dimInstoreNum.put(vo.getPk_onhanddim(), instoreNum.add(vo.getNonhandnum()));
			dimInstoreAstNum.put(vo.getPk_onhanddim(), instoreAstNum.add(vo.getNonhandastnum()));
		}
		ReleaseNoticeVO notice = getStateAdjustVO(pk_onhanddims.toArray(new String[0]));
		if (!notice.isSuccess()) {
			return notice;
		}
		List<StateAdjustVO> stateAdjustVOs = notice.getObj();
		if (stateAdjustVOs == null || stateAdjustVOs.size() <= 0) {
			return ReleaseNoticeVO.create(false, "销售出库库存调整失败！！！");
		}
		GeneralOutVO[] samplingOuts = getSamplingOutByApplyBill(applyVO);
		Map<String, OnhandVO> outOnhandMap = new HashMap<>();
		if (samplingOuts != null && samplingOuts.length > 0) {
			List<OnhandVO> outOnhandVOs = service.getOnhandDimByICBill(samplingOuts);
			if (outOnhandVOs != null && outOnhandVOs.size() > 0) {
				for (OnhandVO onhandVO : outOnhandVOs) {
					outOnhandMap.put(onhandVO.getPk_onhanddim(), onhandVO);
				}
			}
		}
		ScaleUtils scale = new ScaleUtils(pk_group);
		List<StateAdjustVO> newStateAdjustVOs = new ArrayList<>();
		for (StateAdjustVO stateAdjustVO : stateAdjustVOs) {
			// 库存状态此时系现存量库存状态，不符合条件的库存状态数据不进行调整
			if (unableStoreStatePks.contains(stateAdjustVO.getCstateadjustid())) {
				continue;
			}
			String pk_onhanddim = stateAdjustVO.getPk_onhanddim();
			OnhandVO outOnhand = outOnhandMap.get(pk_onhanddim);
			UFDouble samplingNum = outOnhand == null ? UFDouble.ZERO_DBL : outOnhand.getNonhandnum();
			UFDouble samplingAstNum = outOnhand == null ? UFDouble.ZERO_DBL : outOnhand.getNonhandastnum();
			samplingNum = samplingNum == null ? UFDouble.ZERO_DBL : samplingNum;
			samplingAstNum = samplingAstNum == null ? UFDouble.ZERO_DBL : samplingAstNum;
			UFDouble instoreNum = dimInstoreNum.containsKey(pk_onhanddim) ? 
					dimInstoreNum.get(pk_onhanddim) : UFDouble.ZERO_DBL;
			UFDouble instoreAstNum = dimInstoreAstNum.containsKey(pk_onhanddim) ? 
			        dimInstoreAstNum.get(pk_onhanddim) : UFDouble.ZERO_DBL;
			UFDouble checkNum = instoreNum.add(samplingNum); // 取样数量是由取样出库获得，数量为负，所以此处使用加法即可
			UFDouble checkAstNum = instoreAstNum.add(samplingAstNum); // 取样数量是由取样出库获得，数量为负，所以此处使用加法即可
			UFDouble lockNum = stateAdjustVO.getNlocknum() == null ? UFDouble.ZERO_DBL : stateAdjustVO.getNlocknum();
			UFDouble lockAstNum = stateAdjustVO.getNlockassistnum() == null ? UFDouble.ZERO_DBL : stateAdjustVO.getNlockassistnum();
			UFDouble onhandNum = stateAdjustVO.getNnum() == null ? UFDouble.ZERO_DBL : stateAdjustVO.getNnum();
			UFDouble onhandAstNum = stateAdjustVO.getNassistnum() == null ? UFDouble.ZERO_DBL : stateAdjustVO.getNassistnum();
			// 可调整现存量 = 现存量 - 冻结数量
			UFDouble usableOnhandNum = onhandNum.sub(lockNum);
			UFDouble usableOnhandAstNum = onhandAstNum.sub(lockAstNum);
			UFDouble adjustNum = UFDouble.ZERO_DBL;
			UFDouble adjustAstNum = UFDouble.ZERO_DBL;
			String vchangerate = "1/1";
			//调整数量 = 检验数量 > 可调整现存量 ？可调整现存量 ： 检验数量
			if (checkAstNum.compareTo(usableOnhandAstNum) > 0) {
			    adjustAstNum = usableOnhandAstNum;
			    adjustNum = usableOnhandNum;
			    vchangerate = NumConvertRateFormula.calculateConvertRate(usableOnhandNum, usableOnhandAstNum, scale);
			} else {
			    adjustAstNum = checkAstNum;
			    String cunitid = stateAdjustVO.getCunitid();
			    vchangerate = NumConvertRateFormula.calculateConvertRate(checkNum, checkAstNum, scale);
			    adjustNum = NumConvertRateFormula.calculateMainNum(adjustAstNum, vchangerate, cunitid, scale);
			}
			stateAdjustVO.setNadjustnum(adjustNum);
			stateAdjustVO.setNadjustassistnum(adjustAstNum);
			stateAdjustVO.setVchangerate(vchangerate);
			newStateAdjustVOs.add(stateAdjustVO);
		}
		notice.setResults(newStateAdjustVOs.toArray(new StateAdjustVO[0]));
		return notice;
	}
	
	private ReleaseNoticeVO releaseFor4Z(ReportVO reportVO, ApplyVO applyVO, AggMedReleaseVO_148 releaseVO, Set<String> unableStoreStatePks) throws BusinessException {
		ApplyHeaderVO applyHeader = applyVO.getHVO();
		ApplySourceItemVO[] asvos = applyVO.getB2VO();
		MedReleaseHVO_148 releaseHeader = (MedReleaseHVO_148)releaseVO.getParentVO();
		String pk_material = releaseHeader.getCmaterialvid();
		String pk_org = releaseHeader.getPk_org();
		String pk_group = releaseHeader.getPk_group();
		MaterialStockVO materialStock = getMaterialStockInfo(pk_org, pk_material);
		UFBoolean iskckj_148 = ValueUtils.getUFBoolean(materialStock.getAttributeValue("iskckj_148")); //库存检验取样扣减库存
		Map<String, UFDouble> samplingDimAstNumMap = new HashMap<>();
		// 取样扣减库存需要查询取样单对应的其他出库
		if (iskckj_148.booleanValue()) {
		    GeneralOutVO[] samplingOuts = getSamplingOutByApplyBill(applyVO);
		    samplingDimAstNumMap = getSamplingAstNumFromOnhandDim(samplingOuts);
		}
		Map<String, UFDouble> dimCheckAstNum = new HashMap<>(); // 现存量维度实际检验数量
		for (int i = 0; i < asvos.length; i++) {
			ApplySourceItemVO sourceItem = asvos[i];
			String pk_onhanddim = (String)sourceItem.getAttributeValue("pk_onhanddim_148");
			UFDouble applyAstNum = sourceItem.getNastnum() == null ? UFDouble.ZERO_DBL : sourceItem.getNastnum();
	        UFDouble samplingAstNum = samplingDimAstNumMap.containsKey(pk_onhanddim) 
	                ? samplingDimAstNumMap.get(pk_onhanddim) : UFDouble.ZERO_DBL;
			// 如果取样扣减库存，实际检验数量 = 报检数量 - 取样数量(此数量为负，所以下面用加法)（ps:虽然取样审批的时候，有其他出库才反写此字段，这里还是进行下判断吧）
	        UFDouble checkAstNum = applyAstNum.add(iskckj_148.booleanValue() ? samplingAstNum : UFDouble.ZERO_DBL);
	        dimCheckAstNum.put(pk_onhanddim, checkAstNum);
		}
		//查询报检单关联的冻结记录，看是否还有剩余冻结
		FreezeThawVO[] freezeThawVOs = QueryFreezeThawUtils.queryFreezesForC001(applyHeader.getVbillcode(), null);
		Map<String, UFDouble> dimFreezeAstNum = new HashMap<>(); //维度报检冻结，剩余冻结数量
		Map<String, UFDouble> dimFreezeNum = new HashMap<>(); //维度报检冻结，剩余冻结数量
		if (freezeThawVOs != null && freezeThawVOs.length > 0) {
			for (FreezeThawVO freeze : freezeThawVOs) {
				UFDouble freezeNum = UFDouble.ZERO_DBL;
				UFDouble freezeAstNum = UFDouble.ZERO_DBL;
				if (!dimFreezeAstNum.containsKey(freeze.getPk_onhanddim())) {
				    freezeAstNum = freeze.getNfrzastnum() == null ? UFDouble.ZERO_DBL : freeze.getNfrzastnum();
				    freezeNum = freeze.getNfrznum() == null ? UFDouble.ZERO_DBL : freeze.getNfrznum();
				} else {
				    freezeAstNum = dimFreezeAstNum.get(freeze.getPk_onhanddim()).add(freeze.getNfrzastnum());
				    freezeNum = dimFreezeNum.get(freeze.getPk_onhanddim()).add(freeze.getNfrznum());
				}
				dimFreezeAstNum.put(freeze.getPk_onhanddim(), freezeAstNum);
				dimFreezeNum.put(freeze.getPk_onhanddim(), freezeNum);
			}
		}
		ReleaseNoticeVO notice = getStateAdjustVO(dimCheckAstNum.keySet().toArray(new String[0]));//库存报检记录上记录的现存量维度

		List<StateAdjustVO> stateAdjustVOs = null;        //解冻前的调整VO
		if (!notice.isSuccess()) {
			return notice;
		}
		stateAdjustVOs = notice.getObj();
		if(stateAdjustVOs == null || stateAdjustVOs.size() <= 0){
			return ReleaseNoticeVO.create(false,"库存检验冻结库存调整单获取失败");
		}
		ScaleUtils scale = new ScaleUtils(pk_group);
		List<StateAdjustVO> newAdjustVOs = new ArrayList<>();
		for (StateAdjustVO stateAdjustVO : stateAdjustVOs) {
			// 库存状态此时系现存量库存状态，不符合条件的库存状态数据不进行调整
			if (unableStoreStatePks.contains(stateAdjustVO.getCstateadjustid())) {
				continue;
			}
			String pk_onhanddim = stateAdjustVO.getPk_onhanddim();
			UFDouble lockNum = stateAdjustVO.getNlocknum() == null ? UFDouble.ZERO_DBL : stateAdjustVO.getNlocknum();
			UFDouble lockAstNum = stateAdjustVO.getNlockassistnum() == null ? UFDouble.ZERO_DBL : stateAdjustVO.getNlockassistnum();
			UFDouble onhandNum = stateAdjustVO.getNnum() == null ? UFDouble.ZERO_DBL : stateAdjustVO.getNnum();
			UFDouble onhandAstNum = stateAdjustVO.getNassistnum() == null ? UFDouble.ZERO_DBL : stateAdjustVO.getNassistnum();
			UFDouble checkAstNum = dimCheckAstNum.get(pk_onhanddim);
			UFDouble freezeAstNum = dimFreezeAstNum.containsKey(pk_onhanddim) 
			        ? dimFreezeAstNum.get(pk_onhanddim) : UFDouble.ZERO_DBL;
	        UFDouble freezeNum = dimFreezeNum.containsKey(pk_onhanddim) 
	                ? dimFreezeNum.get(pk_onhanddim) : UFDouble.ZERO_DBL;
			// 应调整现存量 = 现存量 - 冻结数量 + 报检剩余冻结数量，分主辅数量
			UFDouble usableOnhandAstNum = onhandAstNum.sub(lockAstNum).add(freezeAstNum);
			UFDouble usableOnhandNum = onhandNum.sub(lockNum).add(freezeNum);
			// 调整数量 = 检验数量 > 可调整现存量 ？可调整现存量 ： 检验数量
			UFDouble adjustAstNum = UFDouble.ZERO_DBL;
			UFDouble adjustNum = UFDouble.ZERO_DBL;
			String vchangerate = "1/1";
			if (checkAstNum.compareTo(usableOnhandAstNum) > 0) {
			    adjustAstNum = usableOnhandAstNum;
			    adjustNum = usableOnhandNum;
			    vchangerate = NumConvertRateFormula.calculateConvertRate(adjustNum, adjustAstNum, scale);
			} else {
			    // 取检验数量时，需要根据存量中的主辅数量计算换算率，再根据此换算率计算调整主数量
			    adjustAstNum = checkAstNum;
			    vchangerate = NumConvertRateFormula.calculateConvertRate(usableOnhandNum, usableOnhandAstNum, scale);
			    String cunitid = stateAdjustVO.getCunitid();
			    adjustNum = NumConvertRateFormula.calculateMainNum(adjustAstNum, vchangerate, cunitid, scale);
			}
			stateAdjustVO.setNadjustnum(adjustNum);
			stateAdjustVO.setNadjustassistnum(adjustAstNum);
			stateAdjustVO.setVchangerate(vchangerate);
			newAdjustVOs.add(stateAdjustVO);
		}
		notice.setResults(newAdjustVOs.toArray(new StateAdjustVO[0]));
		return notice;
	}

	@Override
	public ReleaseNoticeVO doTurnReportVOAction(ReportVO reportVO, StateAdjustVO[] stateAdjustVOs, 
			MedReleaseHVO_148 releaseHVO_148, String csourcetypecode) {
		Set<String> dimSet = new HashSet<>();
		if (stateAdjustVOs == null || stateAdjustVOs.length <= 0) {
			return ReleaseNoticeVO.create(true);
		} 
		for (StateAdjustVO stateAdjustVO : stateAdjustVOs) {
			dimSet.add(stateAdjustVO.getPk_onhanddim());
		}
		try {
			if (csourcetypecode.startsWith("4Z")) {
				unFreeze(reportVO);
			}
			Map<String, UFDateTime> tsMap = getOnhandnumTS(dimSet.toArray(new String[0]));
			for (StateAdjustVO stateAdjustVO : stateAdjustVOs) {
				stateAdjustVO.setOnhandnumts(tsMap.get(stateAdjustVO.getPk_onhanddim()));
			}
			IHdTurnService hdTurnService = NCLocator.getInstance().lookup(IHdTurnService.class);
			StateAdjustVO[] results = hdTurnService.turnReportVOAction(reportVO, stateAdjustVOs, releaseHVO_148);
			// 完工报告放行冻结
			if ("55A4".startsWith(csourcetypecode) && releaseHVO_148.getIpassway() == 5) {
				doFreeze(results, releaseHVO_148);
			}
		} catch (Exception e) {
			ReleaseNoticeVO.create(false, e.getMessage());
			ExceptionUtils.wrappException(e);
		}
		return ReleaseNoticeVO.create(true);
	}
	
	private void unFreeze(ReportVO reportVO) throws BusinessException {
		ReportHeaderVO reportHeader = reportVO.getHVO();
		String applyBillCode = reportHeader.getVapplybillcode();
		FreezeThawVO[] freezeThawVOs = QueryFreezeThawUtils.queryFreezesForC001(applyBillCode, null);
		List<FreezeThawVO> thawList = new ArrayList<>();
		if (freezeThawVOs == null || freezeThawVOs.length <= 0) {
			return;
		}
		for (int i = 0; i < freezeThawVOs.length; i++) {
			FreezeThawVO vo = freezeThawVOs[i];
			if (vo.getNfrznum() == null || UFDouble.ZERO_DBL.compareTo(vo.getNfrznum()) > -1) {
				continue;
			}
			vo.setNdefrzastnum(vo.getNfrzastnum());
			vo.setNdefrzgrsnum(vo.getNfrzgrsnum());
			vo.setNdefrznum(vo.getNfrznum());
			thawList.add(vo);
		}
		IFreezeThaw thawService = NCLocator.getInstance().lookup(IFreezeThaw.class);
		thawService.thaw(thawList.toArray(new FreezeThawVO[0]));
	}
	
	/**
	 * 完工报告放行冻结
	 * @param results
	 * @param item
	 * @param parent
	 * @param freezeThaw
	 * @throws BusinessException
	 */
	public void doFreeze(StateAdjustVO[] results, MedReleaseHVO_148 releaseHVO_148)throws BusinessException {
		IFreezeThaw freezeThaw = NCLocator.getInstance().lookup(IFreezeThaw.class);
		for (StateAdjustVO sa : results) {
			UFDouble nnum = sa.getNnum() == null ? UFDouble.ZERO_DBL : sa.getNnum();
			UFDouble nlocknum = sa.getNlocknum();
			if (nlocknum == null) {
				nlocknum = UFDouble.ZERO_DBL;
			}
			if (nnum.doubleValue() <= nlocknum.doubleValue()) {
				continue;
			}
			FreezeThawVO freezevo = new FreezeThawVO();
			freezevo.setPk_onhanddim(sa.getPk_onhanddim_adj());
			freezevo.setCcorrespondrowno(null);
			freezevo.setCunitid(sa.getCunitid());
			freezevo.setCastunitid(sa.getCastunitid());
			freezevo.setVchangerate(sa.getVchangerate());
			freezevo.setPk_group(sa.getPk_group());
			freezevo.setPk_org(sa.getPk_org());
			freezevo.setPk_org_v(releaseHVO_148.getPk_org_v());
			freezevo.setCfreezerid(InvocationInfoProxy.getInstance().getUserId());
			freezevo.setDfreezedate(AppContext.getInstance().getServerTime().getDate());
			freezevo.setNnum(nnum.sub(nlocknum));
			freezevo.setNassistnum(sa.getNassistnum());
			freezevo.setNgrossnum(sa.getNgrossnum());
			freezevo.setNfrznum(nlocknum);
			freezevo.setNfrzastnum(sa.getNassistnum());
			freezevo.setNfrzgrsnum(sa.getNgrossnum());
			freezevo.setAttributeValue("vsrctype_148",releaseHVO_148.getVbilltypecode());
			freezevo.setAttributeValue("vsrcid_148", releaseHVO_148.getPrimaryKey());
			freezevo.setAttributeValue("vsrcbid_148", null);
			freezevo.setAttributeValue("vsrccode_148", releaseHVO_148.getVbillcode());
			// Logger.info("翻牌-55A4-冻结-");
			freezeThaw.freeze(new FreezeThawVO[] { freezevo });
		}
	}
	
	private MarAsstFrameVO getMaterialMarAsst(String pk_material) throws BusinessException {
		MarAsstFrameVO marAsstFrameVO = materialAsstFrameMap.get(pk_material);
		if (marAsstFrameVO == null) {
			BaseDAO service = new BaseDAO();
			StringBuilder whereSql = new StringBuilder();
			whereSql.append(" pk_marasstframe in (");
			whereSql.append(" select bd_material.pk_marasstframe from bd_material bd_material ");
			whereSql.append(" where bd_material.pk_material = '" + pk_material + "'");
			whereSql.append(" ) ");
			whereSql.append(" and isnull(dr, 0) = 0");
			@SuppressWarnings("unchecked")
			Collection<MarAsstFrameVO> marAsstFrameVOs = service.retrieveByClause(MarAsstFrameVO.class, whereSql.toString());
			if (marAsstFrameVOs == null || marAsstFrameVOs.size() <= 0) {
				throw new BusinessException("无法查询到物料的启用的辅助属性结构!");
			}
			marAsstFrameVO = marAsstFrameVOs.toArray(new MarAsstFrameVO[0])[0];
			materialAsstFrameMap.put(pk_material, marAsstFrameVO);
		}
		return marAsstFrameVO;
	}
	
	
	
	/**
	 * 获取物料库存信息
	 * @param parent
	 * @return
	 * @throws BusinessException
	 */
	private MaterialStockVO getMaterialStockInfo(String pk_org, String pk_material) throws BusinessException{
		MaterialStockVO stockInfo = materialStockMap.get(pk_org + "-" + pk_material);
		if (stockInfo != null) {
			return stockInfo;
		}
		IMaterialStockQueryService service = NCLocator.getInstance().lookup(IMaterialStockQueryService.class);
		MaterialStockVO[] stockVOs = service.queryMaterialStockVOs(new String[]{ pk_org }, pk_material);
		if(stockVOs == null || stockVOs.length == 0){
			throw new BusinessException("未找到库存信息!");
		}
		materialStockMap.put(pk_org + "-" + pk_material, stockVOs[0]);
		return stockVOs[0];
	}
	
	/**
	 * 获取真实的批次号
	 * @param pk_batchcode
	 * @return
	 * @throws BusinessException 
	 */
	public String getRealVbatchcode(String pk_batchcode) throws BusinessException{
		if(StringUtils.isBlank(pk_batchcode)){
			throw new BusinessException("无法在放行单中获取批次号信息！");
		}
		BatchcodeVO[] batches = NCLocator.getInstance().lookup(IBatchcodePubService.class).queryBatchcodesByPks(new String[]{pk_batchcode});
		if(batches == null || batches.length == 0 ){
			throw new BusinessException("无法查询到批次号信息！可能已经被删除了。");
		}
		String vbatchcode = batches[0].getVbatchcode();
		if (vbatchcode == null || "".equals(vbatchcode)) {
			throw new BusinessException("无法获取批次号信息！");
		}
		return vbatchcode;
	}
	
	private MaterialConvertVO getMaterialConvert(String pk_material) throws BusinessException {
		MaterialConvertVO convert = materialconMap.get(pk_material);
		if (convert != null ) {
			return convert;
		}
		VOQuery<MaterialConvertVO> service = new VOQuery<>(MaterialConvertVO.class);
		StringBuilder whereSql = new StringBuilder();
		whereSql.append(" and " + MaterialConvertVO.PK_MATERIAL + " = '" + pk_material + "'");
		whereSql.append(" and " + MaterialConvertVO.ISSTOCKMEASDOC + " = 'Y'");
		MaterialConvertVO[] converts = service.query(whereSql.toString(), null);
		if (converts == null || converts.length <= 0) {
			throw new BusinessException("查询不到物料的辅计量管理信息！！！");
		}
		convert = converts[0];
		materialconMap.put(pk_material, converts[0]);
		return convert;
	}
	
	/**
	 * 方法功能描述：根据现存量维度表现存量,匹配不到，返回;
	 * @throws BusinessException 
	 */
	public ReleaseNoticeVO getStateAdjustVO(String[] pk_onhanddims) throws BusinessException {
		String whereSql = "  pk_onhanddim in ('" + StringUtils.join(pk_onhanddims, "', '") +  "') ";
		return getStateAdjustVO(whereSql);
	}
	
	/**
	 * 根据条件获取库存状态调整的数据
	 * @throws BusinessException 
	 */
	private ReleaseNoticeVO getStateAdjustVO(String where) throws BusinessException {
		// 现存量查询接口
		IOnhandQry onbandquery = NCLocator.getInstance().lookup(IOnhandQry.class);
		// 通过查询条件，返回现存量
		OnhandQryCond cond = new OnhandQryCond();
		cond.setISSum(true);
		cond.addSelectFields(CollectionUtils.combineArrs(
				OnhandDimVO.getDimContentFields(),
				new String[] { OnhandDimVO.PK_ONHANDDIM }));// 加入现存量维度及现存量维度pk


		cond.setWhere(where.toString());
		return doResult(onbandquery.queryOnhand(cond));// 处理查询出来的物料结存记录
	}
	
	/**
	 * 处理查询出来的物料结存记录，展现在界面上，字段编辑性处理
	 * 
	 * @param resultVOs
	 * @throws BusinessException
	 */
	private ReleaseNoticeVO doResult(OnhandVO[] resultVOs)throws BusinessException {
		// 根据单品信息过滤掉现存量记录，V60暂不支持库存状态调整
//		OnhandVO[] rets = this.filterOnHandBySN(resultVOs);
	    // 此处暂定不再进行单品过滤
		List<StateAdjustVO> billList = this.translateResult(resultVOs);
		if (ValueCheckUtil.isNullORZeroLength(billList)) {
			return ReleaseNoticeVO.create(false,
					 "没有符合条件的物料结存记录 ，请核对现存量同一批次对应库存状态(如入库生产为待检、库存检验为合格)的物料是否存在"
					,MessageType.DIALOG);
		}
		return ReleaseNoticeVO.create(true).setObject(billList);
	}
	
	private OnhandVO[] filterOnHandBySN(OnhandVO[] resultVOs) throws BusinessException {
		String[] pk_dims = VOEntityUtil.getVOsNotRepeatValue(resultVOs,OnhandDimVO.PK_ONHANDDIM);
		OnhandSNVO[] vos = NCLocator.getInstance().lookup(IOnhandQry.class).queryOnhandSNVOByDimPK(pk_dims);
		if (ValueCheckUtil.isNullORZeroLength(vos))
			return resultVOs;
		Set<String> dims = VOEntityUtil.getVOsValueSet(vos,OnhandDimVO.PK_ONHANDDIM);
		List<OnhandVO> ret = new ArrayList<OnhandVO>();
		for (OnhandVO vo : resultVOs) {
			if (dims.contains(vo.getPk_onhanddim()))
				continue;
			ret.add(vo);

		}
		return CollectionUtils.listToArray(ret);
	}
	
	public void defaultValue(StateAdjustVO vo, ReportHeaderVO reportHeaderVO, MedReleaseHVO_148 releaseHVO_148, String csourcetypecode) {		
		// 设置调整人
		vo.setBillmaker(AppContext.getInstance().getPkUser());
		// 设置调整时间
		vo.setCreationtime(AppContext.getInstance().getServerTime());
		// 设置调整单号
		vo.setAttributeValue("reportvbillcode_148", reportHeaderVO.getVbillcode());
		// 设置调整单号pk
		vo.setAttributeValue("pk_reportbill_148", reportHeaderVO.getPk_reportbill());
		vo.setCadjustbillcode(releaseHVO_148.getVbillcode());
		vo.setCadjusttranstype(releaseHVO_148.getVtrantypecode());
		vo.setCadjustbillid(releaseHVO_148.getPrimaryKey());
		vo.setCadjustbilltype(csourcetypecode);
		vo.setAttributeValue("releaseno_148", releaseHVO_148.getVbillcode());
		vo.setAttributeValue("releaseid_148", releaseHVO_148.getPrimaryKey());
	}
	
	
	/**
	 * 将现存量记录转化为库存状态调整单
	 * 
	 * @param resultVOs
	 * @return
	 * @throws BusinessException
	 */
	private List<StateAdjustVO> translateResult(OnhandVO[] resultVOs) throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(resultVOs)) {
			return null;
		}
		List<StateAdjustVO> billList = new ArrayList<StateAdjustVO>();
		for (OnhandVO onhandVO : resultVOs) {
			String pk_material = onhandVO.getCmaterialvid();
			MarAsstFrameVO marassframe = getMaterialMarAsst(pk_material);
			MaterialConvertVO convertVO = getMaterialConvert(pk_material);
			if (marassframe == null) {
				throw new BusinessException("无法查询到物料的启用的辅助属性结构!");
			}
			if (convertVO == null) {
				throw new BusinessException("查询不到物料的辅计量管理信息！！！");
			}
			// 物料管理属性：库存状态管理；
			if (!marassframe.getFix1().booleanValue()) {
				continue;
			}
			// 结存主数量-冻结数量>0, 预留主数量, 冻结数量, 满足至少有一个为空或零
			if (!canStateAdjust(onhandVO)) {
				continue;
			}
			IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			//2021-03-21 云峰网络  wangnw 3.	库存状态校验：同时，QC在检验放行时，校验该物料是否冻结状态，如果库存为检验/冻结状态，翻牌时，系统提醒：“此物料库存状态为冻结！”
//			【YF605-放行翻牌是否校验冻结批次】
			String yf605 = SysinitAccessor.getInstance().getParaString(onhandVO.getPk_org(), "YF605");
			if("Y".equals(yf605)){
				if(onhandVO.getNlocknum() != null && onhandVO.getNlocknum().compareTo(UFDouble.ZERO_DBL) > 0){
					String pk_store=onhandVO.getCwarehouseid();
					String sql2 = " select a.name    as name  " + "\n" +
									" from  bd_stordoc a " + "\n" + 
									" where a.pk_stordoc  ='"+pk_store+"' ";
					Map map2 = (Map)iuap.executeQuery(sql2, new MapProcessor());
					String name= map2.get("name")==null?"":map2.get("name").toString() ;
					String bat=onhandVO.getVbatchcode();
					throw new BusinessException("仓库"+name+" 下的批次"+bat+" 存在冻结，请检查");
				}
				if (onhandVO.getNlocknum() != null && UFDouble.ZERO_DBL.compareTo(onhandVO.getNlocknum()) > 0) {
					throw new BusinessException("现存量错误，冻结数量不能小于0！！！");
				}
				
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
			vo.setOnhandnumts(onhandVO.getTs());// 现存量时间戳
			vo.setVchangerate(onhandVO.getVchangerate());// 换算率；
			vo.setCadjuststateid(onhandVO.getCstateid());
			// 有多少调整多少
			UFDouble onhandNum = onhandVO.getNonhandnum() == null ? UFDouble.ZERO_DBL : onhandVO.getNonhandnum();
			UFDouble onhandAstNum = onhandVO.getNonhandastnum() == null ? UFDouble.ZERO_DBL : onhandVO.getNonhandastnum();
			UFDouble nlockNum = onhandVO.getNlocknum() == null ? UFDouble.ZERO_DBL : onhandVO.getNlocknum();
			UFDouble nlockAstNum = onhandVO.getNlockastnum() == null ? UFDouble.ZERO_DBL : onhandVO.getNlockastnum();
			vo.setNadjustnum(onhandNum.sub(nlockNum));
			vo.setNadjustassistnum(onhandAstNum.sub(nlockAstNum));

			for (int i = 1; i < 11; i++) {
				vo.setAttributeValue("vfree" + i, onhandVO.getAttributeValue("vfree" + i));
			}
			vo.setCunitid(convertVO.getPk_measdoc());// 主单位，方便处理精度
			vo.setCastunitid(onhandVO.getCastunitid());// 单位，方便处理精度
			billList.add(vo);
		}
		return billList;
	}
	
	/**
	 * 现存量数量方面是否符合状态调整
	 * 
	 * @param onhandVO
	 * @return
	 */
	private boolean canStateAdjust(OnhandVO onhandVO) {
		if (NCBaseTypeUtils.isGt(onhandVO.getNlocknum(), onhandVO.getNonhandnum())) {
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
	
	/**
	 * 通过状态名字查询库存状态
	 *
	 */
	public StoreStateVO getStockStateByName(String name, String pk_group)throws BusinessException {
		StoreStateVO storeStateVO = storeStateMap.get(name);
		if (storeStateVO == null) {
			VOQuery<StoreStateVO> service = new VOQuery<>(StoreStateVO.class);
			StringBuilder whereSql = new StringBuilder();
			whereSql.append(" and vname = '" + name + "'");
			whereSql.append(" and pk_group = '" + pk_group + "'");
			whereSql.append(" and sealflag in ('Y', 'N')");
			StoreStateVO[] storeStateVOs = service.query(whereSql.toString(), null);
			if (storeStateVOs == null || storeStateVOs.length <= 0) {
				throw new BusinessException("查询不到集团下名为【" + name + "】的库存状态！！！");
			}
			storeStateMap.put(pk_group + "-" + name, storeStateVOs[0]);
			storeStateVO = storeStateVOs[0];
		}
		return storeStateVO;
	}
	
	private Map<String, UFDateTime> getOnhandnumTS(String[] pk_onhanddims) throws BusinessException {
		// 现存量查询接口
		IOnhandQry onbandquery = NCLocator.getInstance().lookup(IOnhandQry.class);
		OnhandNumVO[] numvos = onbandquery.queryOnhandNumByDim(pk_onhanddims);
		if (ValueCheckUtil.isNullORZeroLength(numvos))
			return null;
		Map<String, UFDateTime> map = new HashMap<String, UFDateTime>();
		for (OnhandNumVO vo : numvos) {
			map.put(vo.getPk_onhanddim(), vo.getTs());
		}
		return map;
	}
	
	private StordocVO getStordocInfo(String pk_stordoc) throws BusinessException {
		StordocVO stordocVO = stordocMap.get(pk_stordoc);
		if (stordocVO != null) {
			return stordocVO;
		}
		VOQuery<StordocVO> service = new VOQuery<>(StordocVO.class);
		StordocVO[] stordocVOs = service.query(new String[]{ pk_stordoc });
		if (stordocVOs == null || stordocVOs.length <= 0) {
			throw new BusinessException("无法查询到仓库信息！！！");
		}
		stordocVO = stordocVOs[0];
		stordocMap.put(stordocVO.getPk_stordoc(), stordocVOs[0]);
		return stordocVO;
	}
	
	private GeneralOutVO[] getSamplingOutByApplyBill(ApplyVO applyVO) {
		VOQuery<SamplingHeadVO> samplingQuery = new VOQuery<>(SamplingHeadVO.class);
		StringBuilder samplingSQl = new StringBuilder();
		samplingSQl.append(" and " + SamplingHeadVO.VSOURCECODE + " = '" + applyVO.getHVO().getVbillcode() + "' ");
		samplingSQl.append(" and " + SamplingHeadVO.CSOURCEID + " = '" + applyVO.getHVO().getPk_applybill() + "' ");
		SamplingHeadVO[] samplingHeaders = samplingQuery.query(samplingSQl.toString(), null);
		if (samplingHeaders == null || samplingHeaders.length <= 0) {
			return null;
		}
		Set<String> pk_samplings = new HashSet<>();
		for (SamplingHeadVO header : samplingHeaders) {
			pk_samplings.add(header.getPk_samplebill());
		}
		VOQuery<GeneralOutHeadVO> outHeadQuery = new VOQuery<>(GeneralOutHeadVO.class);
		StringBuilder whereSql = new StringBuilder();
		whereSql.append(" and " + MetaNameConst.CGENERALHID + " in ( ");
		whereSql.append(" select b." + MetaNameConst.CGENERALHID);
		whereSql.append(" from ic_generalout_b b ");
		whereSql.append(" where b.csourcebillhid in ('");
		whereSql.append(StringUtils.join(pk_samplings.toArray(new String[0]), "', '"));
		whereSql.append("')");
		whereSql.append(" and isnull(b.dr, 0) = 0");
		whereSql.append(" ) ");
		GeneralOutHeadVO[] heads = outHeadQuery
				.query(whereSql.toString(), null);
		if (heads == null || heads.length <= 0) {
			return null;
		}
		List<String> cgeneralhids = new ArrayList<>();
		for (GeneralOutHeadVO head : heads) {
			cgeneralhids.add(head.getCgeneralhid());
		}
		BillQuery<GeneralOutVO> query = new BillQuery<GeneralOutVO>(GeneralOutVO.class);
		GeneralOutVO[] goutVOs = query.query(cgeneralhids.toArray(new String[0]));
		return goutVOs;
	}
	
	/**
	 * 获取出库单据对应的存量维度的[主数量, 辅数量]
	 * @param generalOutVOs
	 * @return
	 * @throws BusinessException
	 */
    private Map<String, UFDouble> getSamplingAstNumFromOnhandDim(GeneralOutVO[] generalOutVOs) throws BusinessException {
        Map<String, UFDouble> onhandAstNumMap = new HashMap<>();
        if (generalOutVOs == null || generalOutVOs.length <= 0) {
            return onhandAstNumMap;
        }
        IMedOnhandDim service = NCLocator.getInstance().lookup(IMedOnhandDim.class);
        List<OnhandVO> onhandVOs = service.getOnhandDimByICBill(generalOutVOs);
        if (onhandVOs == null || onhandVOs.size() <= 0) {
            return onhandAstNumMap;
        }
        for (OnhandVO onhandVO : onhandVOs) {
            UFDouble nastNum = onhandAstNumMap.containsKey(onhandVO.getPk_onhanddim()) 
                    ? onhandAstNumMap.get(onhandVO.getPk_onhanddim()) : UFDouble.ZERO_DBL;
            nastNum = nastNum.add(onhandVO.getNonhandastnum());
            onhandAstNumMap.put(onhandVO.getPk_onhanddim(), nastNum);
        }
        return onhandAstNumMap;
    }

}
