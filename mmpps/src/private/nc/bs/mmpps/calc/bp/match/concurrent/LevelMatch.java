package nc.bs.mmpps.calc.bp.match.concurrent;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.execute.Executor;
import nc.bs.framework.execute.ThreadFactoryManager;
import nc.bs.mmpps.calc.bp.BPManager;
import nc.bs.mmpps.calc.bp.CalcDeleteBP;
import nc.bs.mmpps.calc.bp.CalculateResult;
import nc.bs.mmpps.calc.bp.batch.IMatchBatch;
import nc.bs.mmpps.calc.bp.grossdemand.DemandCounteract;
import nc.bs.mmpps.calc.bp.lotreg.DSByLotRegUpdateBP;
import nc.bs.mmpps.calc.bp.match.lrp.LrpMOIDTempTableCreator;
import nc.bs.mmpps.calc.bp.match.lrp.LrpPickDemandPicker;
import nc.bs.mmpps.calc.bp.match.lrp.LrpUpdatePDemand;
import nc.bs.mmpps.calc.bp.material.MaterialInsertBP;
import nc.bs.mmpps.calc.bp.material.MaterialQueryBP;
import nc.bs.mmpps.calc.bp.realsubs.IRealSubsQuery;
import nc.bs.mmpps.calc.bp.result.CalculateResultManager;
import nc.bs.mmpps.calc.bp.syslog.SysLogManager;
import nc.bs.mmpps.calc.bp.utils.PloReWriteUtil;
import nc.vo.mmpps.calc.entity.MrpParam;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapSet;

/**
 * 层匹配类
 * 
 * @since 6.5
 * @version 2012-8-10 下午01:49:07
 * @author guojunf
 */

public final class LevelMatch {

	private CalculateContext calulateContext;

	private MrpParam param;

	/**
	 * 批量策略
	 */
	private IMatchBatch batchStrategy;

	/**
	 * 物料范围查询类
	 */
	private MaterialQueryBP materialQrybp;

	/**
	 * 当前层的物料临时表
	 */
	private String mCurrTempTable;

	/**
	 * 完成数量
	 */
	private int completeCount = 0;

	/**
	 * 需补货物料
	 */
	private Set<String> poMaterials = new HashSet<String>();
	private int lowlevelCode;
private CalculateResult calcResult;
	public int getCompleteCount() {
		return this.completeCount;
	}

	public Set<String> getPoMaterials() {
		return this.poMaterials;
	}

	/**
	 * 低层码
	 */

public LevelMatch(CalculateContext calulateContext, int lowlevelCode,	IMatchBatch batchStrategy, MaterialQueryBP materialQrybp) 
	
	{
	
	
		this.calulateContext = calulateContext;
		this.param = calulateContext.getParam();
		this.lowlevelCode = lowlevelCode;
		this.batchStrategy = batchStrategy;
		this.materialQrybp = materialQrybp;
		this.calcResult = CalculateResultManager.getCalcResult(calulateContext);
	}

	/**
	 * 匹配
	 */
	public void doMatch() {
		// 匹配前处理
		this.doBeforeMatch();
		// 执行多线程匹配/单线程匹配
		if (this.param.getMultiThreadMatch().equalsIgnoreCase(	UFBoolean.TRUE.toString())) {
			
			this.doMatchMultThread();
		} else {
			this.doMatchNoExThread();
		}

		// 匹配后处理
		this.doAfterMatch();
	}

	/**
	 * 匹配前处理
	 */
	private void doBeforeMatch() {
		MaterialInsertBP bp = new MaterialInsertBP(this.calulateContext);
		// 当前层的物料临时表
		this.mCurrTempTable = bp.loadCurrLevelTempTable(this.lowlevelCode);

		if (this.calulateContext.getBLotReg()) {
			// 加载数据前处理
			LrpPickDemandPicker lrpPickDemandPicker = new LrpPickDemandPicker(	this.calulateContext, this.lowlevelCode,	this.mCurrTempTable);
			lrpPickDemandPicker.doFetch();
		}

		// 加载数据
		this.loadData();
		
//		if (this.calulateContext.getBLotReg()) {
			// 加载数据后处理
//			DSByLotRegUpdateBP lotRegUpdateDS = new DSByLotRegUpdateBP(
//					this.calulateContext, this.lowlevelCode,
//					this.mCurrTempTable);
//			lotRegUpdateDS.doUpdate();
//		}
	}

	/**
	 * 匹配后处理
	 */
	private void doAfterMatch() {

		if (this.calulateContext.getBLotReg()) {
			LrpMOIDTempTableCreator lrpMOIDTTCreator = new LrpMOIDTempTableCreator(	this.calulateContext, this.mCurrTempTable);
			lrpMOIDTTCreator.doCreate();

			LrpUpdatePDemand lrpPDemandUpdate = new LrpUpdatePDemand(		this.calulateContext, this.lowlevelCode);
			lrpPDemandUpdate.doUpdate();
		}

	}

	/**
	 * 不开启新线程
	 */
	private void doMatchNoExThread() {
		String calculateCode = this.calulateContext.getCalculateCode();
		String logMessage = "<<=======Single Thread========>>";
		SysLogManager.createNormalMessage(calculateCode, logMessage);
		// 设置批量大小
		this.batchStrategy.setBatchSize(this.param.getBatchSize());
		int batchIndex = 0;
		// 循环获取每一批数据
		while (this.batchStrategy.hasNextBatch()) {
			batchIndex++;
			// 获得下一批数据
			this.batchStrategy.nextBatch();
			logMessage = "Level:" + this.lowlevelCode + ",BatchIndex:"
					+ batchIndex;
			SysLogManager.createStartMessage(calculateCode, logMessage);
			BatchMatch batch = new BatchMatch(this.calulateContext,	this.lowlevelCode, this.batchStrategy.getBatchFilter(),	this.calcResult);
			batch.doMatch();
			this.completeCount = this.completeCount + batch.getCompleteCount();
			this.poMaterials.addAll(batch.getPoMaterials());
			this.clearBatchEnd();
			SysLogManager.createEndMessage(calculateCode, logMessage);
		}
	}

	private void clearBatchEnd() {
		String calculateCode = this.calulateContext.getCalculateCode();
		String logMessage = "clearBatchEnd()";
		SysLogManager.createStartMessage(calculateCode, logMessage);
		PloReWriteUtil.resetPloReWriteCache(this.calulateContext);
		SysLogManager.createEndMessage(calculateCode, logMessage);
	}

	/**
	 * 多线程匹配
	 */
	private void doMatchMultThread() {

		String calculateCode = this.calulateContext.getCalculateCode();
		String logMessage = "<<=======Multi Thread========>>";
		;
		SysLogManager.createNormalMessage(calculateCode, logMessage);

		int batchSize = this.param.getBatchSize();
		// 设置批量大小
		this.batchStrategy.setBatchSize(batchSize);
		SysLogManager.createNormalMessage(calculateCode, "Param Batch Size:"	+ batchSize);

		// 内批大小
		int innerBatchSize;

		// 记录执行信息
		TaskResult result = new TaskResult();

		// 获取InvocationInfo信息，传给子线程，否则子线程取不到相关信息
		InvocationInfo info = this.getInfo();
		int batchIndex = 0;
		// 循环获取每一批数据
		while (this.batchStrategy.hasNextBatch()) {
			// 获得下一批数据
			// 返回本批实际的数量
			int realnum = this.batchStrategy.nextBatch();
			SysLogManager.createNormalMessage(calculateCode, "Real Batch Size:"
					+ realnum);
			if (this.param.getBatchLeastLimit() == 0) {
				innerBatchSize = batchSize / this.param.getSynThreadCount();
			} else {
				if (realnum <= this.param.getBatchLeastLimit()) {
					innerBatchSize = this.param.getBatchLeastLimit();
				} else {
					innerBatchSize = realnum / this.param.getSynThreadCount()		+ realnum % this.param.getSynThreadCount();
				}
			}

			this.batchStrategy.setInnerBatchSize(innerBatchSize);
			SysLogManager.createNormalMessage(calculateCode,	"Inner Batch Size:" + innerBatchSize);

			batchIndex++;
			logMessage = "Level:" + this.lowlevelCode + ",BatchIndex:"	+ batchIndex;
			SysLogManager.createStartMessage(calculateCode, logMessage);

			// 阻塞队列
			LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
			// 线程池
			ThreadPoolExecutor pool = new ThreadPoolExecutor(	this.param.getSynThreadCount(),	this.param.getSynThreadCount(), 10, TimeUnit.DAYS,	taskQueue, ThreadFactoryManager.newThreadFactory());
			int i = 1;
			while (this.batchStrategy.hasNextInnerBatch()) {

				this.batchStrategy.nextInnerBatch();

				BatchMatch batch = new BatchMatch(this.calulateContext,	this.lowlevelCode,	this.batchStrategy.getInnerBatchFilter(),	CalculateResultManager	.getCalcResult(this.calulateContext));
				pool.execute(new Executor(new MatchTask(batchIndex,	calculateCode, i++, batch, info, result)));
			}

			// 不接受新的入队信息
			pool.shutdown();
			try {
				// 等待所有的任务执行完毕或者超时(超时时间：1天)
				pool.awaitTermination(1, TimeUnit.DAYS);
		
	} 
catch (InterruptedException e) {	}
			this.clearBatchEnd();
			SysLogManager.createEndMessage(calculateCode, logMessage);
		}
		if (result.getErrBatches().size() > 0) {
			// 如果分批线程中有错误，则删除所有当前运算号的数据
			CalcDeleteBP delbp = new CalcDeleteBP(this.calulateContext);
			delbp.delRecWhenBatchFailed(this.calulateContext.getCalculateCode());

	ExceptionUtils	.wrappException(result.getErrBatches().get(0).getEx());
		} else {
			this.completeCount = result.getCompletedMtrlCount();
			this.poMaterials.addAll(result.getPoMaterials());
		}
	}

	private InvocationInfo getInfo() {
		InvocationInfo info = new InvocationInfo();
		InvocationInfoProxy infoProxy = InvocationInfoProxy.getInstance();
		info.setUserId(infoProxy.getUserId());
		info.setUserDataSource(infoProxy.getUserDataSource());
		info.setGroupId(infoProxy.getGroupId());
		info.setLangCode(infoProxy.getLangCode());
		info.setLogLevel(infoProxy.getLogLevel());
		info.setBizCenterCode(infoProxy.getBizCenterCode());
		info.setCallId(infoProxy.getCallId());
		info.setGroupNumber(infoProxy.getGroupNumber());
		info.setHyCode(infoProxy.getHyCode());
		info.setSysid(infoProxy.getSysid());
		return info;
	}

	/**
	 * 加载数据 <br>
	 * <ul>
	 * <li>1.加载当前层物料范围id</li>
	 * <li>1.初始物料-需求数、物料-供应数 映射</li>
	 * </ul>
	 */
	private void loadData() {
		// 加载当前层物料范围id
		String[] cppsmaterialids = this.materialQrybp	.getCppmaterialids(this.mCurrTempTable);

		// 查询具有替代关系的物料
		IRealSubsQuery realSubsQry = new BPManager(this.calulateContext)	.getRealSubsQueryBP();	

		MapSet<String, String> data = realSubsQry				.queryReplRelation(this.mCurrTempTable);

		// 计划独立需求冲销
		DemandCounteract dct = new DemandCounteract(this.calulateContext,	this.mCurrTempTable);
		dct.doUpdate();

		// 初始化批量数据(初始化"物料-需求数"、"物料-供应数" 映射)
		this.batchStrategy.batchInit(this.lowlevelCode, cppsmaterialids, data);

	}

}
