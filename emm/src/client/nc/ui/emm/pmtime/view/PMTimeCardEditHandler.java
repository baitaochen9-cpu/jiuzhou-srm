package nc.ui.emm.pmtime.view;

import nc.ui.am.editor.AMBillForm;
import nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler;
import nc.ui.emm.pmbase.utils.PMEventUtils;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.vo.emm.premaintain.PMHeadVO;
import nc.vo.emm.premaintain.PMStdJobVO;
import nc.vo.emm.premaintain.PMWorkObjVO;
import nc.vo.pub.BusinessException;

/**
 * 
 * <p>
 * 时间型预防性维护卡片事件管理器。
 * </p>
 * 
 * @author cuikai
 * @version 6.0
 */
public class PMTimeCardEditHandler extends WithRefInitCardEditEventHandler {

	@Override
	public boolean handleHeadBeforeEditEvent(AMBillForm billForm, BillItemEvent e) throws Exception {
		super.handleHeadBeforeEditEvent(billForm, e);
		// 调整下一工单到期日期只有是固定计划时且下一工单到期日期有值时可以编辑.
		if (e.getItem().getKey().equalsIgnoreCase(PMHeadVO.ADJUST_START_DATE)) {
			return PMEventUtils.beforeEditAdjustStartDate(billForm, e);
		}
		
		/*********************************20240611 bbt *****************/
		if(e.getItem().getKey().equalsIgnoreCase(PMHeadVO.PK_EQUIP)){
			return PMEventUtils.beforeEditHeadEquip(billForm, e);
		}
		/***************************************************************/
		
		return true;
	}

	@Override
	public void handleHeadAfterEditEvent(AMBillForm billForm, BillEditEvent e) throws BusinessException {		
		// 根据表头选取的设备编码，自动给设备名称，位置编码，位置名称等设备信息赋值
		if (e.getKey().equalsIgnoreCase(PMHeadVO.PK_EQUIP)) {
			PMEventUtils.afterEditHeadEquip(billForm);
		}
		// 如果表头操作了位置信息，而位置信息与设备带出来的位置不符，则清空设备信息
		else if (e.getKey().equalsIgnoreCase(PMHeadVO.PK_LOCATION)) {
			PMEventUtils.afterEditHeadLocation(billForm);
		}
		// 首次开始日期如果变化，1.重置计数器为0；2. 下一工单到期日期 ＝ 首次开始日期
		else if (e.getKey().equalsIgnoreCase(PMHeadVO.INITIAL_DATE)) {
			//yezhian 2020069
			PMEventUtils.afterEditHeadInitialDate(billForm, e);
		}
		// 当时间型预防性维护更改了生成周期后算法
		else if (e.getKey().equalsIgnoreCase(PMHeadVO.PERIODS) || e.getKey().equalsIgnoreCase(PMHeadVO.PERIODS_UNIT)) {
			PMEventUtils.afterEditHeadPeriods(billForm, e);
		}
		// 可变计划编辑的算法
		else if (e.getKey().equalsIgnoreCase(PMHeadVO.ALTER_FLAG)) {
			PMEventUtils.afterEditHeadAlterFlag(billForm, e);
		} else if (e.getKey().equalsIgnoreCase(PMHeadVO.ADJUST_START_DATE)
				|| e.getKey().equalsIgnoreCase(PMHeadVO.AHEAD_DAYS)) {
			PMEventUtils.setNextCreateDate(billForm);
		}
		/*
		 * 20200628 yezhian RayBow 界面编辑控制
		 */
		else if (e.getKey().equalsIgnoreCase(PMHeadVO.DEF1)){
			PMEventUtils.setNextendDate(billForm);
		}
		
	}

	@Override
	public void handleBodyAfterEditEvent(AMBillForm billForm, BillEditEvent e) throws Exception {
		// 作业方案的设备编辑后的事件
		if (e.getKey().equals(PMWorkObjVO.PK_EQUIP)) {
			PMEventUtils.afterEditBodyEquip(billForm, e);
		}
		// 作业方案的位置编辑后的事件
		else if (e.getKey().equals(PMWorkObjVO.PK_LOCATION)) {
			PMEventUtils.afterEditBodyLocation(billForm, e);
		}
		// 标准工作包编辑后的事件
		else if (e.getKey().equals(PMStdJobVO.PK_STD_JOB)) {
			PMEventUtils.afterEditBodyStdJob(billForm, e);
		}
	}
	
//	/**
//	 * 20240606 bbt 增加表头编辑前事件
//	 */
//	@Override
//	public boolean handleBodyBeforeEditEvent(AMBillForm billForm,
//			BillEditEvent e) throws Exception {
//		String key = e.getKey();
//		if("".equals(key)){
//			PMEventUtils.beforeEditHeadEquip(billForm, e);
//		}
//		return super.handleBodyBeforeEditEvent(billForm, e);
//	}
	}
