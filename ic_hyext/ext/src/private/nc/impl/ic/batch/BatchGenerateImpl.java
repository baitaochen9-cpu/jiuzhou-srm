package nc.impl.ic.batch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.businessevent.EventDispatcher;
import nc.bs.businessevent.IEventType;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.impl.pubapp.pattern.data.vo.VODelete;
import nc.itf.ic.batch.IBatchGenerate;
import nc.itf.scmf.ic.mbatchcode.IBatchcodeMaintainService;
import nc.pubitf.ic.flowaccount.FlowAccountQuery;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.ic.batch.AbstractBillItemAdapter;
import nc.vo.ic.batch.util.BatchGenerateUtils;
import nc.vo.ic.batchcode.AbstractBatchFieldMap;
import nc.vo.ic.batchcode.BatchSynchronizer;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.DiffVOComparator;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.ic.businessevent.ICCommonEvent;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.scmf.pub.util.BusinessEventType;

/**
 * 库存单据保存时更新批次档案
 * 
 * @author chennn 2010-3-10 下午04:25:20
 * @since 6.0
 */
public class BatchGenerateImpl implements IBatchGenerate {

	@Override
	public List<BatchcodeVO> insertBatchWithoutSource(
			List<AbstractBillItemAdapter> bodys, AbstractBatchFieldMap fieldsMap)
			throws BusinessException {
		try {
			if ((bodys == null) || (bodys.size() <= 0)) {
				return null;
			}
			List<BatchcodeVO> newBatchcode = new ArrayList<BatchcodeVO>();
			for (AbstractBillItemAdapter body : bodys) {
				if (StringUtil.isSEmptyOrNull(body.getPk_batchcode())
						&& !StringUtil.isSEmptyOrNull(body.getVbatchcode())) {
					BatchSynchronizer syn = new BatchSynchronizer(fieldsMap);
					BatchcodeVO vo = new BatchcodeVO();
					syn.synBilltoBatchVO(body, vo);
					newBatchcode.add(vo);
				}
			}
			if (newBatchcode.size() == 0) {
				return null;
			}
			List<String> uniqueKeys = new ArrayList<String>();
			List<BatchcodeVO> uniqueBatch = new ArrayList<BatchcodeVO>();
			for (int i = 0; i < newBatchcode.size(); i++) {
				Object[] key = VOEntityUtil.getVOValues(newBatchcode.get(i),
						new String[] { ICPubMetaNameConst.CMATERIALVID,
								ICPubMetaNameConst.VBATCHCODE });
				String tempkey = StringUtil.mergeString(key, null, null);
				if (!uniqueKeys.contains(tempkey)) {
					uniqueKeys.add(tempkey);
					uniqueBatch.add(newBatchcode.get(i));
				}

			}
			BatchcodeVO[] retVOs = this.addBatch(uniqueBatch
					.toArray(new BatchcodeVO[0]));
			return Arrays.asList(retVOs);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
			return null;
		}

	}

	@Override
	public void updateBatch(List<BatchcodeVO> originBatch,
			List<AbstractBillItemAdapter> bodys,
			List<AbstractBillItemAdapter> originBodys,
			AbstractBatchFieldMap fieldsMap) throws BusinessException {
		try {
			BatchcodeVO[] finalVO = this.getFinalVO(bodys, fieldsMap);
			BatchcodeVO[] originVO = originBatch.toArray(new BatchcodeVO[originBatch.size()]);
			
			// 20250712 九洲药业，批次号检查，如果变更内容包含批次生产日期，抛出异常--------------------------
//			bodys.get(0).getStatus();
			checkDproducedate(finalVO,originVO);
			//-------------------------------------------------------------------------------end
			DiffVOComparator<BatchcodeVO> diff = new DiffVOComparator<BatchcodeVO>( finalVO, originVO, new String[] { BatchcodeVO.CMATERIALVID, BatchcodeVO.VBATCHCODE });

			this.processNewBatch( CollectionUtils.combineArrs(diff.getUnchangeVOs(), diff.getNewVOs()), bodys);
			this.processUpdateBatch( CollectionUtils.combineArrs(diff.getUpdateVOs(), diff.getNewVOs()), bodys);
			this.processDeleteBatch(diff.getDeleteVOs(), originBodys);
			return;
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}

	}
	/**
	 * 检查生产日期变更
	 * @param finalVO
	 * @param originVO
	 * @author  jiuzhou yezhian 20250712
	 * @throws BusinessException 
	 */
	private void checkDproducedate(BatchcodeVO[] finalVO, BatchcodeVO[] originVO) throws BusinessException {
		// TODO Auto-generated method stub
		//如果参数为空，直接返回
		if(null == finalVO || finalVO.length == 0 || null == originVO || originVO.length == 0){
			return ;
		}
		Map<String,UFDate> map_Dproducedate = new HashMap<String,UFDate>();
		Map<String,UFDate> map_dvalidate  = new HashMap<String,UFDate>();
		
		for(BatchcodeVO flvo : finalVO){
			String mergeString = StringUtil .mergeString(
					VOEntityUtil.getVOValues(flvo, new String[] {
							BatchcodeVO.CMATERIALVID,
							BatchcodeVO.VBATCHCODE }), null, null);
			map_Dproducedate.put(mergeString, flvo.getDproducedate());
			map_dvalidate.put(mergeString, flvo.getDvalidate());//失效日期
		}
		

		for(BatchcodeVO origvo : originVO){
			String mergeString = StringUtil .mergeString(
					VOEntityUtil.getVOValues(origvo, new String[] {
							BatchcodeVO.CMATERIALVID,
							BatchcodeVO.VBATCHCODE }), null, null);
			UFDate dproducedate = origvo.getDproducedate();
			UFDate dvalidate = origvo.getDvalidate();//失效日期
			
			UFDate ufDate = map_Dproducedate.get(mergeString);
			UFDate ufDate2 = map_dvalidate.get(mergeString);
			if( null != ufDate && (null != dproducedate.asEnd() && !(dproducedate.asEnd()).equals(ufDate.asEnd())) ){
				throw new BusinessException("批次号生产日期异常：【"+origvo.getVbatchcode()+"】批次生产日期 【"+ufDate.asEnd()+"】与档案中【"+dproducedate.asEnd()+"】不一致， 请清除该批次号信息后重新选择批次号！ ");
			}if(null != ufDate2 && (null != dvalidate.asEnd() && !(dvalidate.asEnd()).equals(ufDate2.asEnd())) ){
				throw new BusinessException("批次号失效日期异常：【"+origvo.getVbatchcode()+"】批次失效日期 【"+ufDate2.asEnd()+"】与档案中【"+dvalidate.asEnd()+"】不一致， 请清除该批次号信息后重新选择批次号！ ");
			}
		}
	}

	@Override
	public void updateBatch(List<AbstractBillItemAdapter> bodys,
			AbstractBatchFieldMap fieldsMap) throws BusinessException {
		try {
			BatchcodeVO[] finalVO = this.getFinalVO(bodys, fieldsMap);
			this.processUpdateBatch(finalVO, bodys);
			return;
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
	}

	private BatchcodeVO[] addBatch(BatchcodeVO[] batchcodes) throws Exception {
		if ((batchcodes == null) || (batchcodes.length <= 0)) {
			return null;
		}
		IBatchcodeMaintainService maintainService = NCLocator.getInstance()
				.lookup(IBatchcodeMaintainService.class);
		BatchOperateVO operateVO = new BatchOperateVO();
		operateVO.setAddObjs(batchcodes);
		maintainService.batchSave(operateVO);
		return (BatchcodeVO[]) operateVO.getAddObjs();
	}

	private BatchcodeVO[] clearBatchSourceInfo(BatchcodeVO[] batchcodes)
			throws BusinessException {
		if ((batchcodes == null) || (batchcodes.length <= 0)) {
			return null;
		}
		for (BatchcodeVO vo : batchcodes) {
			for (String key : BatchcodeVO.sourcefields) {
				vo.setAttributeValue(key, null);
			}
		}
		BatchcodeVO[] updVOs = NCLocator.getInstance()
				.lookup(IBatchcodePubService.class)
				.updateBatchByBill(batchcodes, BatchcodeVO.sourcefields);
		return updVOs;

	}

	private void deleteBatch(BatchcodeVO[] batchcodes) {
		if ((batchcodes == null) || (batchcodes.length <= 0)) {
			return;
		}
		new VODelete<BatchcodeVO>().delete(batchcodes);
		this.fireDelEvent(batchcodes);
	}

	/**
	 * 删除后派发事件
	 * 
	 * @param delVOs
	 */
	private void fireDelEvent(BatchcodeVO[] delVOs) {
		// 来源ID
		String sourceID = BusinessEventType.getSourceIDByBillVO(delVOs);
		try {
			EventDispatcher.fireEvent(new ICCommonEvent(sourceID,
					IEventType.TYPE_DELETE_AFTER, delVOs, null));
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrappException(e);
		}
	}

	private BatchcodeVO[] getFinalVO(List<AbstractBillItemAdapter> itemsInfo,
			AbstractBatchFieldMap fields) throws BusinessException {
		if ((itemsInfo == null) || (itemsInfo.size() == 0)) {
			return null;
		}
		List<BatchcodeVO> retList = new ArrayList<BatchcodeVO>();
		BatchSynchronizer syn = new BatchSynchronizer(fields);
		for (AbstractBillItemAdapter item : itemsInfo) {
			if (StringUtil.isSEmptyOrNull(item.getPk_batchcode())
					&& StringUtil.isSEmptyOrNull(item.getVbatchcode())) {
				continue;
			}
			BatchcodeVO batch = new BatchcodeVO();
			syn.synBilltoBatchVO(item, batch);
			if (StringUtil.isSEmptyOrNull(batch.getPk_batchcode())) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4008028_0", "04008028-0028")/* @res "单据数据错误" */);
			}
			retList.add(batch);
		}
		BatchGenerateUtils util = new BatchGenerateUtils();
		util.fillBatchVO(retList);// 补全批次相关信息
		return retList.toArray(new BatchcodeVO[retList.size()]);
	}

	private int getItemIndex(BatchcodeVO vo, List<AbstractBillItemAdapter> items) {
		if ((vo == null) || (items == null) || (items.size() == 0)) {
			return -1;
		}
		for (int i = 0; i < items.size(); i++) {
			if (!StringUtil.isStringEqual(vo.getCmaterialvid(), items.get(i)
					.getCmaterialvid())
					|| !StringUtil.isStringEqual(vo.getVbatchcode(),
							items.get(i).getVbatchcode())) {
				continue;
			}
			return i;
		}
		return -1;
	}

	private BatchcodeVO[] processDeleteBatch(BatchcodeVO[] delBatch,
			List<AbstractBillItemAdapter> originBodys) throws BusinessException {
		if ((delBatch == null) || (delBatch.length == 0)) {
			return null;
		}
		List<BatchcodeVO> batchtoHandle = new ArrayList<BatchcodeVO>();
		for (BatchcodeVO vo : delBatch) {
			int index = this.getItemIndex(vo, originBodys);
			if (index < 0) {
				continue;
			}
			AbstractBillItemAdapter body = originBodys.get(index);
			if (!StringUtil
					.isStringEqual(body.getBid(), vo.getCsourcebillbid())) {
				continue;
			}
			batchtoHandle.add(vo);
		}
		String tableName = originBodys.get(0).getTableName();
		List<BatchcodeVO> referedBatch = new ArrayList<BatchcodeVO>();
		List<BatchcodeVO> unreferedBatch = new ArrayList<BatchcodeVO>();
		this.splitBatchVOsByRefered(tableName, batchtoHandle, referedBatch,
				unreferedBatch);
		this.deleteBatch(unreferedBatch.toArray(new BatchcodeVO[unreferedBatch
				.size()]));
		BatchcodeVO[] batches = this.clearBatchSourceInfo(referedBatch
				.toArray(new BatchcodeVO[referedBatch.size()]));
		return batches;
	}

	private BatchcodeVO[] processNewBatch(BatchcodeVO[] batchVOs,
			List<AbstractBillItemAdapter> bodys) throws Exception {
		if ((batchVOs == null) || (batchVOs.length == 0)) {
			return null;
		}
		Map<String, String> newBatchMap = new ICBSContext().getNewBatchMap();
		// Map<String,String> newBatchMap=null;
		List<BatchcodeVO> newBatches = new ArrayList<BatchcodeVO>();
		for (BatchcodeVO vo : batchVOs) {
			String key = StringUtil
					.mergeString(
							VOEntityUtil.getVOValues(vo, new String[] {
									BatchcodeVO.CMATERIALVID,
									BatchcodeVO.VBATCHCODE }), null, null);
			if (newBatchMap.get(key) == null) {
				continue;
			}
			this.setBatchSourceInfo(vo, bodys);
			newBatches.add(vo);
		}
		if (newBatches.size() <= 0) {
			return null;
		}
		// 填充批次的创建信息
		return NCLocator
				.getInstance()
				.lookup(IBatchcodePubService.class)
				.updateBatchByBill(
						newBatches.toArray(new BatchcodeVO[newBatches.size()]),
						BatchcodeVO.sourcefields);
	}

	/**
	 * 方法功能描述：
	 * <p>
	 * 处理更新的批次 <b>参数说明</b>
	 * 
	 * @param batchtoHandle
	 * @param bodys
	 *            表体
	 * @return
	 * @throws Exception
	 *             <p>
	 * @since 6.0
	 * @author chennn
	 * @time 2010-9-7 下午07:04:40
	 */

	private BatchcodeVO[] processUpdateBatch(BatchcodeVO[] batchtoHandle,
			List<AbstractBillItemAdapter> bodys) throws Exception {
		if ((batchtoHandle == null) || (batchtoHandle.length == 0)) {
			return null;
		}
		Map<String, String> newBatchMap = new ICBSContext().getNewBatchMap();
		// Map<String,String> newBatchMap=null;
		List<BatchcodeVO> updList = new ArrayList<BatchcodeVO>();
		for (BatchcodeVO vo : batchtoHandle) {
			String key = StringUtil
					.mergeString(
							VOEntityUtil.getVOValues(vo, new String[] {
									BatchcodeVO.CMATERIALVID,
									BatchcodeVO.VBATCHCODE }), null, null);
			if (null != newBatchMap && newBatchMap.get(key) != null) {
				continue;
			}
			updList.add(vo);
		}
		List<BatchcodeVO> changedBatch = new BatchGenerateUtils()
				.fetchChangedBatch(updList);
		if ((changedBatch == null) || (changedBatch.size() == 0)) {
			return new BatchcodeVO[0];
		}
		BatchcodeVO[] updateBatch = this.updateBatch(changedBatch
				.toArray(new BatchcodeVO[0]));
		return updateBatch;
	}

	private void setBatchSourceInfo(BatchcodeVO batch,
			AbstractBillItemAdapter body) {

		batch.setVsourcebillcode(body.getVbillcode());
		batch.setCsourcetype(body.getBillType());
		batch.setVsourcerowno(body.getCrowNo());
		batch.setCsourcebillbid(body.getBid());
		batch.setCsourcebillhid(body.getHid());
	}

	private void setBatchSourceInfo(BatchcodeVO batch,
			List<AbstractBillItemAdapter> bodys) {
		int index = this.getItemIndex(batch, bodys);
		if (index < 0) {
			return;
		}
		AbstractBillItemAdapter body = bodys.get(index);
		this.setBatchSourceInfo(batch, body);
	}

	// private void setBatchtoBill(BatchcodeVO[] batchcodes,
	// AbstractBillItemAdapter[] bodys,AbstractBatchFieldMap fieldsMap) {
	// Map<String, AbstractBillItemAdapter> billMap = new HashMap<String,
	// AbstractBillItemAdapter>();
	// for (AbstractBillItemAdapter body :bodys) {
	// if (StringUtil.isSEmptyOrNull(body.getVbatchcode()))
	// continue;
	// String key = body.getCmaterialvid() + body.getVbatchcode();
	// billMap.put(key, body);
	// }
	// for (BatchcodeVO vo : batchcodes) {
	// String batchKey = vo.getCmaterialvid() + vo.getVbatchcode();
	// AbstractBillItemAdapter body = billMap.get(batchKey);
	// BatchSynchronizer syn = new BatchSynchronizer(fieldsMap);
	// syn.synBatchVOtoBill(vo, body);
	// }
	//
	// }

	/**
	 * 方法功能描述：
	 * <p>
	 * 根据流水账是否引用批次，将批次分为两个集合 <b>参数说明</b>
	 * 
	 * @param tableName
	 * @param batchtoHandle
	 * @param referedBatches
	 * @param unreferedBatches
	 *            <p>
	 * @since 6.0
	 * @author chennn
	 * @time 2010-9-7 下午07:05:32
	 */
	private void splitBatchVOsByRefered(String tableName,
			List<BatchcodeVO> batchtoHandle, List<BatchcodeVO> referedBatches,
			List<BatchcodeVO> unreferedBatches) {
		String[] pk_batchcodes = VOEntityUtil.getVOsValues(
				batchtoHandle.toArray(new BatchcodeVO[batchtoHandle.size()]),
				BatchcodeVO.PK_BATCHCODE, String.class);
		if ((pk_batchcodes == null) || (pk_batchcodes.length == 0)) {
			return;
		}
		Set<String> refered_pks = NCLocator.getInstance()
				.lookup(FlowAccountQuery.class).queryBatchRef(pk_batchcodes);
		for (BatchcodeVO vo : batchtoHandle) {
			if ((refered_pks != null)
					&& refered_pks.contains(vo.getPk_batchcode())) {
				referedBatches.add(vo);
			} else {
				unreferedBatches.add(vo);
			}
		}
	}

	private BatchcodeVO[] updateBatch(BatchcodeVO[] batchcodes)
			throws BusinessException {
		if ((batchcodes == null) || (batchcodes.length <= 0)) {
			return null;
		}
		// 针对更新的批次档案进行部分字段（BatchcodeVO.fieldsChanged）的更新
		BatchcodeVO[] updVOs = NCLocator.getInstance()
				.lookup(IBatchcodePubService.class)
				.updateBatchByBill(batchcodes, BatchcodeVO.fieldsFoUpdate);
		return updVOs;
	}

}
