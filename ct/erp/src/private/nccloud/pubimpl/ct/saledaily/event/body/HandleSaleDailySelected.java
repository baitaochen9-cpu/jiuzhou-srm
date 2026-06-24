package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.vo.ct.entity.CtAbstractExpVO;
import nc.vo.ct.entity.CtAbstractTermVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.util.CollectionUtils;
import nccloud.dto.scmpub.pub.context.BillCardBodyChangedRow;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.context.TableAfterEditEvent;
import nccloud.dto.scmpub.pub.utils.SCMIndexUtil;
import nccloud.dto.scmpub.pub.utils.SCMMultiSelectConst;
import nccloud.dto.scmpub.pub.utils.SCMRowNoUtils;

public class HandleSaleDailySelected {
	/**
	 * 
	 * 处理多选场景（new）
	 * 
	 * @param billvo
	 * @param event
	 * @param userObj
	 * @return
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int[] handleSelected(AggregatedValueObject billvo, BillCardBodyEditEvent event, Map userObj) {
		String key = event.getChangeKey();
		List<String> scm_indexs = (List<String>) userObj.get(SCMMultiSelectConst.SCM_INDEXS);
		if (scm_indexs == null) {
			int[] rows = this.handleMultSelected(billvo, event);
			if (event.getChangrows() != null && event.getChangrows().length > 1) {
				userObj.put(SCMMultiSelectConst.SCM_INDEXS, rows);
			}
			Integer originIndex = SCMIndexUtil.getOriginIndex(userObj);
			String[] allRownos = SCMIndexUtil.getAllRowNos(userObj);
			// 行号处理 合同条款、合同费用多选
			if (key.equals(CtAbstractTermVO.VTERMCODE)) {
				SCMRowNoUtils.setMultiSelectRowNo(((AggCtSaleVO) billvo).getCtSaleTermVO(), rows, originIndex,
						allRownos);
			} else if (key.equals(CtAbstractExpVO.VEXPCODE)) {
				SCMRowNoUtils.setMultiSelectRowNo(((AggCtSaleVO) billvo).getCtSaleExpVO(), rows, originIndex,
						allRownos);
			} else {
				SCMRowNoUtils.setMultiSelectRowNo(billvo.getChildrenVO(), rows, originIndex, allRownos);
			}
			return rows;
		} else {
			int[] array = new int[scm_indexs.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = Integer.parseInt(scm_indexs.get(i));
			}
			return array;
		}
	}

	/**
	 * 多选处理
	 * 
	 * @param billvo 聚合VO
	 * @param event
	 * @return int[] 需要单独处理编辑事件的行索引数组
	 */
	public int[] handleMultSelected(AggregatedValueObject billvo, BillCardBodyEditEvent event) {
		String key = event.getChangeKey();
		// 当前第几行
		int currentIndex = event.getRow();
		// 编辑行的行数
		int editRowCount = 0;
		// 起始编辑行数，默认是当前行，如果当前行已有物料，并且包含在已选物料中，则起始位置是当前行的下一行
		int startIndex = currentIndex;
		CircularlyAccessibleValueObject[] bodys = null;
		if (key.equals(CtAbstractTermVO.VTERMCODE)) {
			bodys = ((AggCtSaleVO)billvo).getCtSaleTermVO();
		}else if(key.equals(CtAbstractExpVO.VEXPCODE)) {
			bodys = ((AggCtSaleVO)billvo).getCtSaleExpVO();
		}
		// 将编辑行之前的行复制到新表体行中
		List<CircularlyAccessibleValueObject> newBodys = new ArrayList<CircularlyAccessibleValueObject>();
		for (int i = 0; i < currentIndex; i++) {
			newBodys.add(bodys[i]);
		}
		BillCardBodyChangedRow[] changedRows = event.getChangrows();
		if (changedRows == null || changedRows.length == 0) {
			return null;
		}
		List<String> newValues = new ArrayList<String>();
		for (BillCardBodyChangedRow changedRow : changedRows) {
			String newValue = (String) changedRow.getNewvalue();
			// 如果新值为空，则不用处理这一行
			newValues.add(newValue);
		}
		// 旧值只在第一行数据上可能有
		String oldValue = (String) changedRows[0].getOldvalue();
		// 如果新值集合中存在旧值，则只把不包含在新值中的旧值增行，当前行不变，起始编辑行为当前行的下一行
		if (newValues.contains(oldValue)) {
			// 新值集合中存在旧值，也要把当前行设为编辑行
			// startIndex++;
			editRowCount++;
			newBodys.add(bodys[currentIndex]);
			for (String newValue : newValues) {
				if (null != newValue && (!newValue.equals(oldValue))) {
					editRowCount++;
					addNewBodyvo(newBodys, key, newValue, bodys[0]);
				} else {
					bodys[currentIndex].setAttributeValue(key, newValue);
				}
			}
		} else {
			// 如果新值中不存在旧值,则把新值的第一个元素设到当前行上,余下的增行处理
			for (int i = 0; i < newValues.size(); i++) {
				if (i == 0) {
					bodys[currentIndex].setAttributeValue(key, newValues.get(i));
					newBodys.add(bodys[currentIndex]);
				} else {
					addNewBodyvo(newBodys, key, newValues.get(i), bodys[0]);
				}
				editRowCount++;
			}
		}
		// 将编辑行之后的行复制到新表体行中
		for (int i = currentIndex + 1; i < bodys.length; i++) {
			newBodys.add(bodys[i]);
		}
		billvo.setChildrenVO(CollectionUtils.listToArray(newBodys));
		int[] editedIndexes = new int[editRowCount];
		for (int i = 0; i < editRowCount; i++) {
			editedIndexes[i] = startIndex + i;
		}
		return editedIndexes;
	}

	/**
	 * 处理表格多选场景
	 * 
	 * @param billvo
	 * @param event
	 * @return
	 */
	public int[] handleMultSelected(AggregatedValueObject billvo, TableAfterEditEvent event) {
		String key = event.getChangeKey();
		// 当前第几行
		int currentIndex = event.getRow();
		// 编辑行的行数
		int editRowCount = 0;
		// 起始编辑行数，默认是当前行，如果当前行已有物料，并且包含在已选物料中，则起始位置是当前行的下一行
		int startIndex = currentIndex;

		CircularlyAccessibleValueObject[] bodys = billvo.getChildrenVO();
		// 将编辑行之前的行复制到新表体行中
		List<CircularlyAccessibleValueObject> newBodys = new ArrayList<CircularlyAccessibleValueObject>();
		for (int i = 0; i < currentIndex; i++) {
			newBodys.add(bodys[i]);
		}
		BillCardBodyChangedRow[] changedRows = event.getChangedRows();
		if (changedRows == null || changedRows.length == 0) {
			return null;
		}
		List<String> newValues = new ArrayList<String>();
		for (BillCardBodyChangedRow changedRow : changedRows) {
			String newValue = (String) changedRow.getNewvalue();
			// 如果新值为空，则不用处理这一行
			newValues.add(newValue);
		}
		// 旧值只在第一行数据上可能有
		String oldValue = (String) changedRows[0].getOldvalue();
		// 如果新值集合中存在旧值，则只把不包含在新值中的旧值增行，当前行不变，起始编辑行为当前行的下一行
		if (newValues.contains(oldValue)) {
			// 新值集合中存在旧值，也要把当前行设为编辑行
			// startIndex++;
			editRowCount++;
			newBodys.add(bodys[currentIndex]);
			for (String newValue : newValues) {
				if (null != newValue && (!newValue.equals(oldValue))) {
					editRowCount++;
					addNewBodyvo(newBodys, key, newValue, bodys[0]);
				} else {
					bodys[currentIndex].setAttributeValue(key, newValue);
				}
			}
		} else {
			// 如果新值中不存在旧值,则把新值的第一个元素设到当前行上,余下的增行处理
			for (int i = 0; i < newValues.size(); i++) {
				if (i == 0) {
					bodys[currentIndex].setAttributeValue(key, newValues.get(i));
					newBodys.add(bodys[currentIndex]);
				} else {
					addNewBodyvo(newBodys, key, newValues.get(i), bodys[0]);
				}
				editRowCount++;
			}
		}
		// 将编辑行之后的行复制到新表体行中
		for (int i = currentIndex + 1; i < bodys.length; i++) {
			newBodys.add(bodys[i]);
		}
		billvo.setChildrenVO(CollectionUtils.listToArray(newBodys));
		int[] editedIndexes = new int[editRowCount];
		for (int i = 0; i < editRowCount; i++) {
			editedIndexes[i] = startIndex + i;
		}
		return editedIndexes;
	}
	/**
	 * 处理新增行，将新增行状态设为新增
	 * 
	 * @param newBodys
	 * @param cmaterialvid
	 * @param bodyvo
	 */
	private void addNewBodyvo(List<CircularlyAccessibleValueObject> newBodys,
			String key, String cmaterialvid, CircularlyAccessibleValueObject bodyvo) {
		CircularlyAccessibleValueObject tempBody = getNewInstance(bodyvo);
		tempBody.setAttributeValue(key, cmaterialvid);
		tempBody.setStatus(VOStatus.NEW);
		newBodys.add(tempBody);
	}

	private CircularlyAccessibleValueObject getNewInstance(
			CircularlyAccessibleValueObject body) {
		try {
			return body.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			ExceptionUtils.wrappException(e);
		}
		return body;
	}
}
