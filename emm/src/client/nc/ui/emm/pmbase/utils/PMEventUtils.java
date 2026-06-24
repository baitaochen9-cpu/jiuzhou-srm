/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.emm.pmbase.utils;

import java.util.ArrayList;

import java.util.List;

import javax.swing.SwingUtilities;

import nc.bs.framework.common.NCLocator;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.am.editor.AMBillForm;
import nc.ui.am.ref.util.RefMultiSelectedUtils;
import nc.ui.am.util.BillCardPanelUtils;
import nc.ui.am.util.RownoClientUtils;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.IBillItem;
import nc.ui.uif2.UIState;
import nc.vo.aim.equip.EquipHeadVO;
import nc.vo.am.common.BizContext;
import nc.vo.am.constant.CommonKeyConst;
import nc.vo.am.measurepoint.RefModelConstantVO;
import nc.vo.cmp.util.SqlBuilder;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.emm.premaintain.PMConst;
import nc.vo.emm.premaintain.PMHeadVO;
import nc.vo.emm.premaintain.PMResultVO;
import nc.vo.emm.premaintain.PMWorkObjVO;
import nc.vo.emm.premaintain.utils.PMArithTools;
import nc.vo.emm.premaintain.utils.PMCommonUtils;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * <p>
 * 预防性维护UI事件处理工具类。
 * </p>
 * 
 * @author cuikai
 * @version 6.0
 */
public class PMEventUtils {

	/**
	 * 
	 * <p>
	 * 根据绩效页签的数据设定维护对象是否可以编辑。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 * @return
	 */
	public static void pmEquipEdit(AMBillForm billForm, BillEditEvent e) {
		pmEquipLocEdit(billForm, e, PMHeadVO.PK_LOCATION);
	}

	/**
	 * 
	 * <p>
	 * 根据绩效页签的数据设定维护对象是否可以编辑。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 * @return
	 */
	public static void pmEquipLocEdit(AMBillForm billForm, BillEditEvent e,
			String clearField) {
		// 从界面上取得数据
		PMBillVO vo = (PMBillVO) billForm.getBillCardPanel().getBillData()
				.getBillObjectByMetaData();
		PMResultVO[] resultVOs = (PMResultVO[]) vo
				.getTableVO(PMConst.PM_TAB_RESULT);
		List<PMResultVO> newResultList = new ArrayList<PMResultVO>();
		if (resultVOs != null) {
			for (int i = 0; i < resultVOs.length; i++) {
				PMResultVO resultVO = resultVOs[i];
				if (resultVO.getPk_measure_point_b() != null) {
					newResultList.add(resultVO);
				}
			}
		}
		// 根据绩效页签中的数据来判断当前的预防性维护对象是否可以编辑,如果有数据,则对象不可以修改
		if (newResultList.size() > 0) {
			if (billForm.getModel().getUiState() == UIState.EDIT
					|| billForm.getModel().getUiState() == UIState.ADD) {
				if (UIDialog.ID_OK == billForm
						.showOkCancelMessage(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("pmbase_0",
										"04550002-0034")/*
														 * @res
														 * "更改维修对象将会清空绩效项目，请确认是否更改维修对象?"
														 */)) {
					// 删除绩效频率页签所有行
					BillCardPanelUtils.deleteAllyRows(
							billForm.getBillCardPanel(),
							new String[] { PMConst.PM_TAB_RESULT });
					BillCardPanelUtils.setHeadItemValue(
							billForm.getBillCardPanel(), clearField, null);
					billForm.getBillCardPanel().getBillData()
							.loadEditHeadRelation(clearField);
					return;
				} else {
					// 参照类型取出的是字符串的数组，注意：不能直接塞到界面上
					((UIRefPane) billForm.getBillCardPanel()
							.getHeadItem(e.getKey()).getComponent())
							.setPKs((String[]) e.getOldValue());
					billForm.getBillCardPanel().getBillData()
							.loadEditHeadRelation(e.getKey());
					return;

				}
			}
		} else {
			BillCardPanelUtils.setHeadItemValue(billForm.getBillCardPanel(),
					clearField, null);
			billForm.getBillCardPanel().getBillData()
					.loadEditHeadRelation(clearField);
		}
	}

	/**
	 * 
	 * <p>
	 * 根据绩效页签的数据设定维护对象是否可以编辑。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 * @return
	 */
	public static void pmLocationEdit(AMBillForm billForm, BillEditEvent e) {
		pmEquipLocEdit(billForm, e, PMHeadVO.PK_EQUIP);
	}

	/*************************************
	 * sql查询 bbt 2024/06/06
	 * 
	 * 只有在查询唯一内容的时候才允许调用，即单个位置安装单个设备，或者查询一些固定值的时候
	 * @throws BusinessException
	 **********************************************/
	private static String useQuery(String columnName, String tableName,
			String conditionColumn, String conditionnValue)
			throws BusinessException {
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		SqlBuilder sql = new SqlBuilder();
		sql.append("select " + columnName + " from " + tableName + " where "
				+ conditionColumn + " = '" + conditionnValue + "';");
		Object[] obj_result = (Object[]) query.executeQuery(sql.toString(),
				new ArrayProcessor());
		List list_result = new ArrayList();
		if(null != obj_result && obj_result.length >= 0){
			for(Object o : obj_result){
				list_result.add(o.toString());
			}
		}
		
		try {
			//防止没查到值时，list_result里面是空值
			return list_result.get(0).toString();
		} catch (Exception e) {
			throw new BusinessException("未查询到值: " + sql);
		}
	}

	/********************************************************************************************************/

	/**
	 * 20240606 bbt 单位置多设备，做界面数据筛选
	 * @param billForm
	 * @param e
	 * @return 
	 * @throws BusinessException 
	 */
	public static boolean beforeEditHeadEquip(AMBillForm billForm, BillItemEvent e) throws BusinessException {
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		String EMM05 = nc.cmp.bill.util.SysInit.getParaString(billForm.getPk_org(), "EMM05");
		//输入设备填充位置 或 双向填充时
		if ("E0502".equals(EMM05) || "E0503".equals(EMM05)) {
			if (null != billCardPanel.getHeadItem("pk_location").getValueObject()) {
				BillItem headItem = billForm.getBillCardPanel().getHeadItem("pk_equip");
				UIRefPane component = (UIRefPane) headItem.getComponent();
				AbstractRefModel refModel = component.getRefModel();
				refModel.addWherePart("and pam_equip.pk_location = '" + billCardPanel.getHeadItem("pk_location").getValue() + "'");	
				return true;
			}
			//保持返回值为true，否则编辑前事件会使得设备字段不可编辑
		}return true;
	
	}
	
	
	/**
	 * <p>
	 * 表头设备变化后的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @throws BusinessException
	 */
	public static void afterEditHeadEquip(AMBillForm billForm) throws BusinessException  {		
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		
		/***************通过参数控制PM工单资产和位置的互相填充***bbt 2024/06/06**********************************/
		String EMM05 = nc.cmp.bill.util.SysInit.getParaString(billForm.getPk_org(), "EMM05");
		
		//输入设备填充位置 或 双向填充时
		if ("E0502".equals(EMM05) || "E0503".equals(EMM05)) {
			// 当设备编码非空时
			if (null != billCardPanel.getHeadItem("pk_equip").getValueObject()) {
				// 组装sql,获取当前选定设备对应资产卡片中的位置
				String useQuery = useQuery("pk_location", "pam_equip","pk_equip", billCardPanel.getHeadItem("pk_equip").getValueObject().toString());
				// 设置单据表头位置主键
				BillCardPanelUtils.setHeadItemValue(billCardPanel,PMHeadVO.PK_LOCATION, useQuery);
			} else {
				// 设备编码清空时自动清掉可能存在的旧位置信息（不删也可，下次重新选择设备编码也会刷新）
				BillCardPanelUtils.setHeadItemValue(billCardPanel,PMHeadVO.PK_LOCATION, null);
			}
		}
		
		/***********************************************************************************************************/
		billCardPanel.getBillData().loadEditHeadRelation(PMHeadVO.PK_LOCATION);
		
		if(billCardPanel.getHeadItem(PMHeadVO.PK_EQUIP) != null){		    
		    // 设备修改后，判断专业字段是否为空，若不为空，则保持原有值不变，若为空，则带出设备相关的专业值
		    String oldSpeciality = BillCardPanelUtils.getHeadItemValue(billCardPanel, PMHeadVO.PK_SPECIALTY);
		    if (oldSpeciality == null || "~".equals(oldSpeciality)) {
		    	UIRefPane equip = (UIRefPane) billCardPanel.getHeadItem(PMHeadVO.PK_EQUIP).getComponent();
		    	String newSpeciality = (String) equip.getRefModel().
		    			getValue(EquipHeadVO.TABLE_NAME+"."+EquipHeadVO.PK_SPECIALTY);
		    	billCardPanel.setHeadItem(PMHeadVO.PK_SPECIALTY, newSpeciality);	
		    }
		}
	}

	/**
	 * <p>
	 * 表头位置编辑后的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @throws BusinessException
	 */
	public static void afterEditHeadLocation(AMBillForm billForm)
			throws BusinessException {
		/********************* bbt 2024/06/06 ***************************************************************/
		BillCardPanel billCardPanel = billForm.getBillCardPanel();

		String EMM05 = nc.cmp.bill.util.SysInit.getParaString(billForm.getPk_org(), "EMM05");
		// 输入位置填充设备 或 双向填充
		if ("E0501".equals(EMM05) || "E0503".equals(EMM05)) {
			//界面位置字段已经有值
			if (null != billCardPanel.getHeadItem("pk_location").getValueObject()) {
				//先判断目标位置的安装限制，是否安装多个设备
				String install_limit  = useQuery("install_limit","pam_location","pk_location",billCardPanel.getHeadItem("pk_location").getValueObject().toString());
				//只安装一个设备，才进行自动填充
				if("1".equals(install_limit)){
					// 组装sql,获取当前选定设备对应位置的资产卡片
					String useQuery = useQuery("pk_equip", "pam_equip","pk_location", billCardPanel.getHeadItem("pk_location").getValueObject().toString());
					// 设置单据表头设备主键
					BillCardPanelUtils.setHeadItemValue(billCardPanel,PMHeadVO.PK_EQUIP, useQuery);
				}
			} else {
				// 位置编码清空时自动清掉可能存在的旧设备信息（不删也可，下次重新选择设备编码也会刷新）
				BillCardPanelUtils.setHeadItemValue(billCardPanel,PMHeadVO.PK_EQUIP, null);
			}
		}
		

		/*******************************************************************************************************/
		billForm.getBillCardPanel().getBillData().loadEditHeadRelation(PMHeadVO.PK_EQUIP);
	}

	/**
	 * 
	 * <p>
	 * 标准工作包编辑后事件。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 */
	public static void afterEditBodyStdJob(AMBillForm billForm, BillEditEvent e)
			throws Exception {
		RefMultiSelectedUtils.multiSelectedWithRowNum(billForm, e);
	}

	/**
	 * 
	 * <p>
	 * 更改了生成周期后的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 */
	public static void afterEditHeadPeriods(AMBillForm billForm, BillEditEvent e) {
		setNextStartDate(billForm);
	}

	/**
	 * 
	 * <p>
	 * 可变计划编辑的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 */
	public static void afterEditHeadAlterFlag(AMBillForm billForm,
			BillEditEvent e) {
		setNextStartDate(billForm);
	}

	/**
	 * 设置下一工单目标开始日期
	 * 
	 * @param billForm
	 */
	private static void setNextStartDate(AMBillForm billForm) {
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		// 特种设备预防性维护：按实际完成时间计算
		if (isSpecialEquipmentPM(billCardPanel)) {
			setSpecialPMNextStartDate(billCardPanel);
			setNextCreateDate(billForm);
			return;
		}
		Integer periods = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.PERIODS);
		Integer periodsUnit = BillCardPanelUtils.getHeadItemValue(
				billCardPanel, PMHeadVO.PERIODS_UNIT);
		UFDate preStartDate = BillCardPanelUtils.getHeadItemValue(
				billCardPanel, PMHeadVO.PRE_START_DATE);
		UFDate initialDate = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.INITIAL_DATE);
		Object oValue = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.ALTER_FLAG);
		UFBoolean alterFlag = UFBoolean.FALSE;
		if (oValue != null && oValue.toString().trim().length() > 0) {
			alterFlag = UFBoolean.valueOf(oValue.toString());
		}
		UFDate preEndDate = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.PRE_END_DATE);
		UFDate nextStartDate = PMArithTools.getNextTargStartDate(periods,
				periodsUnit, preStartDate, preEndDate, initialDate, alterFlag);
		BillCardPanelUtils.setHeadItemValue(billCardPanel,
				PMHeadVO.NEXT_START_DATE, nextStartDate);

		setNextCreateDate(billForm);
	}

	/**
	 * 设置下一工单生成日期
	 * 
	 * @param billForm
	 */
	public static void setNextCreateDate(AMBillForm billForm) {
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		UFDate adjustDate = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.ADJUST_START_DATE);
		Integer aheadDays = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.AHEAD_DAYS);
		UFDate nextStartDate = BillCardPanelUtils.getHeadItemValue(
				billCardPanel, PMHeadVO.NEXT_START_DATE);
		UFDate createDate = PMArithTools.getNextCreateWODate(adjustDate,
				nextStartDate, aheadDays);
		BillCardPanelUtils.setHeadItemValue(billCardPanel,
				PMHeadVO.NEXT_CREATE_DATE, createDate);
		// 特种设备预防性维护：DEF2 = nextStartDate（不加允差）
		if (isSpecialEquipmentPM(billCardPanel)) {
			BillCardPanelUtils.setHeadItemValue(billCardPanel, PMHeadVO.DEF2,
					nextStartDate);
		} else if (BillCardPanelUtils.getHeadItemValue(billCardPanel, PMHeadVO.DEF1) != null
				&& new Integer(BillCardPanelUtils.getHeadItemValue(
						billCardPanel, PMHeadVO.DEF1).toString()) != 0) {
			BillCardPanelUtils.setHeadItemValue(billCardPanel, PMHeadVO.DEF2,
					PMCommonUtils.getDateByFre(new Integer(BillCardPanelUtils
							.getHeadItemValue(billCardPanel, PMHeadVO.DEF1)
							.toString()), 0, nextStartDate));
		} else {

			BillCardPanelUtils.setHeadItemValue(billCardPanel, PMHeadVO.DEF2,
					nextStartDate);
		}
	}

	/**
	 * <p>
	 * 首次开始日期编辑后的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 */
	public static void afterEditHeadInitialDate(AMBillForm billForm,
			BillEditEvent e) {
		final BillCardPanel billCardPanel = billForm.getBillCardPanel();
		final Object oldValue = e.getOldValue();
		// 校验开始日期 应该大于当前日期
		if (e.getValue() != null) {
			UFDate new_initial_date = (UFDate) e.getValue();
			if (BizContext.getInstance().getBizDate()
					.afterDate(new_initial_date)) {
				billForm.showErrorMessage(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pmbase_0", "04550002-0035")/*
																				 * @
																				 * res
																				 * "开始日期应该大于等于当前日期"
																				 */);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						billCardPanel.getHeadItem(PMHeadVO.INITIAL_DATE)
								.setValue(oldValue);
					}
				});
				return;
			}
		}

		BillCardPanelUtils.setHeadItemValue(billCardPanel, PMHeadVO.COUNTER, 0);
		BillCardPanelUtils.setHeadItemValue(billCardPanel,
				PMHeadVO.NEXT_START_DATE, BillCardPanelUtils.getHeadItemValue(
						billCardPanel, PMHeadVO.INITIAL_DATE));
		// 清空调整信息
		BillCardPanelUtils.setHeadItemValue(billCardPanel,
				PMHeadVO.PK_ADJUSTER, null);
		BillCardPanelUtils.setHeadItemValue(billCardPanel,
				PMHeadVO.ADJUST_DATE, null);
		BillCardPanelUtils.setHeadItemValue(billCardPanel,
				PMHeadVO.ADJUST_START_DATE, null);
		BillCardPanelUtils.setHeadItemValue(billCardPanel,
				PMHeadVO.PERSIST_FLAG, UFBoolean.FALSE);
		setNextCreateDate(billForm);
	}

	/**
	 * <p>
	 * 调整下一工单到期日期只有是固定计划时且下一工单到期日期有值时可以编辑。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @return
	 */
	public static boolean beforeEditAdjustStartDate(AMBillForm billForm,
			BillItemEvent e) {
		Boolean alterFlag = BillCardPanelUtils.getHeadItemValue(
				billForm.getBillCardPanel(), PMHeadVO.ALTER_FLAG);
		UFDate nextStartDate = BillCardPanelUtils.getHeadItemValue(
				billForm.getBillCardPanel(), PMHeadVO.NEXT_START_DATE);
		if (!alterFlag.booleanValue() && nextStartDate != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * <p>
	 * 提前量小于等与生成频率。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 */
	public static void afterEditBodyAheadNum(AMBillForm billForm,
			BillEditEvent e) {
		int row = e.getRow();
		// 取得选中的数据
		PMResultVO resultVO = (PMResultVO) billForm.getBillCardPanel()
				.getBillModel(PMConst.PM_TAB_RESULT)
				.getBodyValueRowVO(row, PMResultVO.class.getName());
		if (resultVO.getAhead_num() != null
				&& resultVO.getAhead_num().doubleValue() < 0) {
			billForm.showErrorMessage(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pmbase_0", "04550002-0036")/*
																			 * @res
																			 * "提前量不能为负数"
																			 */);
			billForm.getBillCardPanel().setBodyValueAt(e.getOldValue(), row,
					PMResultVO.AHEAD_NUM);
			return;
		}
		if (resultVO.getGen_rate() != null && resultVO.getAhead_num() != null) {
			if (resultVO.getAhead_num().compareTo(resultVO.getGen_rate()) >= 0) {
				billForm.showErrorMessage(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pmbase_0", "04550002-0037")/*
																				 * @
																				 * res
																				 * "提前量必须小于生成频率"
																				 */);
				billForm.getBillCardPanel().setBodyValueAt(e.getOldValue(),
						row, PMResultVO.AHEAD_NUM);
				return;
			}
		}
	}

	/**
	 * 
	 * <p>
	 * 更改起始读数的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 */
	public static void afterEditBodyInitialNum(AMBillForm billForm,
			BillEditEvent e) {
		// 1. 重置计数器为0
		billForm.getBillCardPanel().getHeadItem(PMHeadVO.COUNTER).setValue(0);
		int row = e.getRow();
		// 取得选中的数据
		PMResultVO resultVO = (PMResultVO) billForm.getBillCardPanel()
				.getBillModel(PMConst.PM_TAB_RESULT)
				.getBodyValueRowVO(row, PMResultVO.class.getName());
		// 2. 下一工单读数 ＝ 新的起始读数＋生成频率
		if (resultVO.getInitial_num() != null && resultVO.getGen_rate() != null) {
			UFDouble next_num = resultVO.getInitial_num().add(
					resultVO.getGen_rate());
			billForm.getBillCardPanel().setBodyValueAt(next_num, row,
					PMResultVO.NEXT_NUM, PMConst.PM_TAB_RESULT);
		} else {
			UFDouble next_num = null;
			billForm.getBillCardPanel().setBodyValueAt(next_num, row,
					PMResultVO.NEXT_NUM, PMConst.PM_TAB_RESULT);
		}
		// 清空调整标记，调整人和调整时间
		billForm.getBillCardPanel().setBodyValueAt(UFBoolean.FALSE, row,
				PMResultVO.PERSIST_FLAG, PMConst.PM_TAB_RESULT);
		billForm.getBillCardPanel().setBodyValueAt(null, row,
				PMResultVO.PK_ADJUSTER, PMConst.PM_TAB_RESULT);
		billForm.getBillCardPanel().setBodyValueAt(null, row,
				PMResultVO.ADJUST_DATE, PMConst.PM_TAB_RESULT);
	}

	/**
	 * 
	 * <p>
	 * 当绩效型预防性维护更改了“可变计划”字段后的算法。。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 */
	public static void afterEditBodyAlterFlag(AMBillForm billForm,
			BillEditEvent e) {
		int row = e.getRow();
		// 取得选中的数据
		PMResultVO resultVO = (PMResultVO) billForm.getBillCardPanel()
				.getBillModel(PMConst.PM_TAB_RESULT)
				.getBodyValueRowVO(row, PMResultVO.class.getName());
		UFDouble nextNum = PMArithTools.getNextStartNum(resultVO);
		billForm.getBillCardPanel().setBodyValueAt(nextNum, row,
				PMResultVO.NEXT_NUM, PMConst.PM_TAB_RESULT);

	}

	/**
	 * 
	 * <p>
	 * 当绩效型预防性维护更改了生成频率后的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 */
	public static void afterEditBodyGenRate(AMBillForm billForm, BillEditEvent e) {
		int row = e.getRow();
		// 取得选中的数据
		PMResultVO resultVO = (PMResultVO) billForm.getBillCardPanel()
				.getBillModel(PMConst.PM_TAB_RESULT)
				.getBodyValueRowVO(row, PMResultVO.class.getName());
		if (resultVO.getGen_rate() != null && resultVO.getAhead_num() != null) {
			if (resultVO.getAhead_num().compareTo(resultVO.getGen_rate()) > 0) {
				billForm.showErrorMessage(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pmbase_0", "04550002-0037")/*
																				 * @
																				 * res
																				 * "提前量必须小于等与生成频率"
																				 */);
				billForm.getBillCardPanel().setBodyValueAt(e.getOldValue(),
						row, PMResultVO.GEN_RATE);
				return;
			}
		}
		UFDouble nextNum = PMArithTools.getNextStartNum(resultVO);
		billForm.getBillCardPanel().setBodyValueAt(nextNum, row,
				PMResultVO.NEXT_NUM, PMConst.PM_TAB_RESULT);
	}

	/**
	 * <p>
	 * 作业对象设备变化后的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 */
	public static void afterEditBodyEquip(AMBillForm billForm, BillEditEvent e)
			throws Exception {
		billForm.getBillCardPanel().setBodyValueAt(null, e.getRow(),
				PMWorkObjVO.PK_LOCATION, PMConst.PM_TAB_WORKOBJ);
		billForm.getBillCardPanel().getBillModel(PMConst.PM_TAB_WORKOBJ)
				.loadEditRelationItemValue(e.getRow(), PMWorkObjVO.PK_LOCATION);
		RefMultiSelectedUtils.multiSelectedWithRowNum(billForm, e);
	}

	/**
	 * <p>
	 * 作业对象位置编辑后的算法。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 */
	public static void afterEditBodyLocation(AMBillForm billForm,
			BillEditEvent e) throws Exception {
		billForm.getBillCardPanel().setBodyValueAt(null, e.getRow(),
				PMWorkObjVO.PK_EQUIP, PMConst.PM_TAB_WORKOBJ);
		billForm.getBillCardPanel().getBillModel(PMConst.PM_TAB_WORKOBJ)
				.loadEditRelationItemValue(e.getRow(), PMWorkObjVO.PK_EQUIP);
		RefMultiSelectedUtils.multiSelectedWithRowNum(billForm, e);
	}

	/**
	 * 
	 * <p>
	 * 测量点参照多选处理。
	 * </p>
	 * 
	 * @author cuikai
	 * @version 6.0
	 * @param billForm
	 * @param e
	 */
	public static void afterEditBodyMeasurePoint(AMBillForm billForm,
			BillEditEvent e) {
		// 测量点参照多选处理
		BillCardPanel card = billForm.getBillCardPanel();
		// 选中行数
		int selectRow = card.getBillTable().getSelectedRow();
		// 取得测量点表体的参照
		UIRefPane refPointPane = (UIRefPane) card.getBodyItem(
				PMConst.PM_TAB_RESULT, PMResultVO.PK_MEASURE_POINT_B)
				.getComponent();
		// 从测点参照中取得测量点表头主键
		String[] pk_measure_points = objs2Strs(refPointPane.getRefModel()
				.getValues(RefModelConstantVO.PK_measure_point));
		String[] pk_measure_point_bs = refPointPane.getRefPKs();
		String[] codes = refPointPane.getRefModel().getRefCodeValues();
		if (pk_measure_point_bs != null) {
			for (int i = 0; i < pk_measure_point_bs.length; i++) {
				String pk_measure_point = pk_measure_points[i];
				String pk_measure_point_b = pk_measure_point_bs[i];
				if (pk_measure_point != null) {
					// 将测点表头主键设置到界面模型上，注意要更新模型的id列，因为引用编辑关联项做准备
					card.setBodyValueAt(pk_measure_point_b, selectRow + i,
							PMResultVO.PK_MEASURE_POINT_B + IBillItem.ID_SUFFIX);
					card.setBodyValueAt(codes[i], selectRow + i,
							PMResultVO.PK_MEASURE_POINT_B);
					card.setBodyValueAt(pk_measure_point, selectRow + i,
							PMResultVO.PK_MEASURE_POINT);
					card.getBillModel().loadEditRelationItemValue(
							selectRow + i, PMResultVO.PK_MEASURE_POINT_B);
					card.getBillModel().execEditFormulaByKey(selectRow + i,
							PMResultVO.PK_MEASURE_POINT_B);
				} else {
					card.setBodyValueAt(null, selectRow + i,
							PMResultVO.PK_MEASURE_POINT);
				}
				billForm.getBillCardPanel().addLine(PMConst.PM_TAB_RESULT);
			}
		} else {
			card.setBodyValueAt(null, selectRow, PMResultVO.PK_MEASURE_POINT_B
					+ IBillItem.ID_SUFFIX);
			card.setBodyValueAt(null, selectRow, PMResultVO.PK_MEASURE_POINT_B);
		}
		RownoClientUtils.whenAddLine(billForm.getBillCardPanel(),
				PMConst.PM_TAB_RESULT, CommonKeyConst.rowno, billForm
						.getBillCardPanel().getRowCount());
	}

	private static String[] objs2Strs(Object[] oDatas) {
		if (oDatas == null)
			return null;
		String[] sDatas = new String[oDatas.length];
		for (int i = 0; i < oDatas.length; i++) {
			if (oDatas[i] == null)
				sDatas[i] = null;
			else
				sDatas[i] = oDatas[i].toString().trim();
		}
		return sDatas;
	}

	/**
	 * 判断是否为特种设备预防性维护（交易类型4B72-Cxx-02）
	 */
	private static boolean isSpecialEquipmentPM(BillCardPanel billCardPanel) {
		Object transiType = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.TRANSI_TYPE);
		if (transiType == null) {
			return false;
		}
		return "4B72-Cxx-02".equals(transiType.toString().trim());
	}

	/**
	 * 特种设备预防性维护：按实际完成时间计算下一工单目标日期
	 */
	private static void setSpecialPMNextStartDate(BillCardPanel billCardPanel) {
		Integer counter = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.COUNTER);
		// 首次生成（无历史工单）：保持开始日期不变，不加周期，允许立即生成工单
		if (counter == null || counter == 0) {
			return;
		}
		Integer periods = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.PERIODS);
		Integer periodsUnit = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.PERIODS_UNIT);
		UFDate baseDate = BillCardPanelUtils.getHeadItemValue(billCardPanel,
				PMHeadVO.PRE_END_DATE);
		if (baseDate == null) {
			baseDate = BillCardPanelUtils.getHeadItemValue(billCardPanel,
					PMHeadVO.INITIAL_DATE);
		}
		UFDate nextStartDate = null;
		if (periods != null && periodsUnit != null && baseDate != null) {
			nextStartDate = PMCommonUtils.getDateByFre(periods, periodsUnit,
					baseDate);
		}
		BillCardPanelUtils.setHeadItemValue(billCardPanel,
				PMHeadVO.NEXT_START_DATE, nextStartDate);
	}

	/**
	 * 202200628 yezhian RayBow 控件内容更新，def1 TO def2
	 * 
	 * @param billForm
	 */
	public static void setNextendDate(AMBillForm billForm) {
		// TODO Auto-generated method stub
		BillCardPanel panel = billForm.getBillCardPanel();
		Integer def1 = panel.getHeadItem(PMHeadVO.DEF1).getValueObject() != null ? new Integer(
				panel.getHeadItem(PMHeadVO.DEF1).getValueObject().toString())
				: 0;

		if (panel.getHeadItem(PMHeadVO.PERIODS) == null) {
			billForm.showErrorMessage("请先维护周工单生成周期！");
		}
		if (panel.getHeadItem(PMHeadVO.NEXT_START_DATE) == null) {
			billForm.showErrorMessage("必须先确认下次工单开始时间！");
		}
		panel.setHeadItem(
				PMHeadVO.DEF2,
				PMCommonUtils.getDateByFre(
						def1,
						0,
						((UFDate) panel.getHeadItem(PMHeadVO.NEXT_START_DATE)
								.getValueObject())).toString());
	}
}