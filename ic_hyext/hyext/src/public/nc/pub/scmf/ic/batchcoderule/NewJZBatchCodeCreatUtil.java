/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.pub.scmf.ic.batchcoderule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.ic.m4b.returnin_org;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import net.sf.json.JSONObject;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

public class NewJZBatchCodeCreatUtil {

	static BaseDAO dao = null;

	public static String NextBatchCode(NewJZBatchCodeVO queryBatchSerial)
			throws BusinessException {
		String coderule = queryBatchSerial.getCoderule(); // 当前流水表中的数据

		Map<String, String> parseRule = parseRule(coderule);

		String coverage = queryBatchSerial.getCoverage();// 流水依据

		String next_num = next_num(queryBatchSerial.getNum()); // 尝试获取新的流水号
		UFBoolean bool = checkNumLength(next_num,
				parseRule.get(NewJZBatchCodeVO.NUM));

		if (bool == UFBoolean.FALSE) {// 不如果不符合长度标准，检查流水依据是否进行变更
			queryBatchSerial.setIsmax(UFBoolean.TRUE);
			if (coverage.equals(NewJZBatchCodeVO.YEARS)) { // 如果流水依据是年，无法直接晋升，如果要自然日期到达新年度
				Boolean yearUpdate = checkYearUpdate(queryBatchSerial,
						parseRule);// 年度更新检查
				if (!yearUpdate) { // 没有更新时，抛出异常
					throw new BusinessException(
							"流水已到达极限,无法实现自动分配批次号，请尝试手动录入批次号！");
				}// 如果此时延期日期达到下一年时，把流水设置成初始化
				else {
					init_years(queryBatchSerial,
							parseRule.get(NewJZBatchCodeVO.YEARS));// 初始化年
				}

			} else if (coverage.equals(NewJZBatchCodeVO.MONTHS)) { // 流水已达最大值，且流水依据为月时：像年一样检查是否年月有更新，如果没有更新刚抛出一场，否则直接先变更年月有且对流水进行归零
				Boolean yearUpdate = checkYearUpdate(queryBatchSerial,
						parseRule);// 年度更新检查
				Boolean mothUpdate = checkmonthUpdate(queryBatchSerial,
						parseRule);
				if (!yearUpdate && !mothUpdate) { // 没有更新时，抛出异常
					throw new BusinessException(
							"流水已到达极限,无法实现自动分配批次号，请尝试手动录入批次号！");
				}// 如果此时延期日期达到下一年时，把流水设置成初始化
				else {
					init_years(queryBatchSerial,
							parseRule.get(NewJZBatchCodeVO.YEARS));// 初始化年
					init_months(queryBatchSerial,
							parseRule.get(NewJZBatchCodeVO.MONTHS));// 初始化月
				}
			}

			else if (coverage.equals(NewJZBatchCodeVO.ATZ)) { // 如果是ATZ元素，可自动晋升，如果后期ATZ达到上线，可直接在实体中进行范围扩充
				// 确认当前值的下标位置，进行下标加1

				next_atz(queryBatchSerial);

			}

			// 处理好流水依据后，处理流水，需要流水初始化
			init_num(queryBatchSerial, parseRule.get(NewJZBatchCodeVO.NUM));// 初始化流水
		} else { // 流水符合条件 只需要处理年，不用管ATZ元素

			queryBatchSerial.setNum(next_num);//流水递增

			String string_y = parseRule.get(NewJZBatchCodeVO.YEARS); //获取年度规则
			String string_m = parseRule.get(NewJZBatchCodeVO.MONTHS);//获取月份规则
			if (null != string_y && !"".equals(string_y)) {
				Boolean checkYearUpdate = checkYearUpdate(queryBatchSerial,
						parseRule);// 年度更新检查
			
				if (!checkYearUpdate) {
					init_years(queryBatchSerial,
							parseRule.get(NewJZBatchCodeVO.YEARS));// 初始化年
					init_months(queryBatchSerial, parseRule.get(NewJZBatchCodeVO.MONTHS));//初始化月度
					//且要检查年度的月度是否不流水的依据，如果是则需要初始化流水
					if(coverage.equals(NewJZBatchCodeVO.MONTHS) || coverage.equals(NewJZBatchCodeVO.YEARS)){ 
						init_num(queryBatchSerial,
								parseRule.get(NewJZBatchCodeVO.NUM));// 初始化流水
					} 

				}

			}
			
			if(null != string_m && !"".equals(string_m)){//检查月份元素是否有配置
				Boolean checkMonthUpdate = checkmonthUpdate(queryBatchSerial, parseRule);// 月份更新检查
				if(!checkMonthUpdate){
					init_years(queryBatchSerial,
							parseRule.get(NewJZBatchCodeVO.YEARS));// 初始化年
					init_months(queryBatchSerial, parseRule.get(NewJZBatchCodeVO.MONTHS));//初始化月度
					//且要检查月份是否为流水的依据，如果是则需要初始流水
					if(coverage.equals(NewJZBatchCodeVO.MONTHS)){
						init_num(queryBatchSerial,
								parseRule.get(NewJZBatchCodeVO.NUM));// 初始化流水
					}
				}
			}

		}

		// 数值修正
		correctionsVal(queryBatchSerial);
		// 处理批次组装

		String newBatchCode = assemble(queryBatchSerial, parseRule);

		return newBatchCode;
	}

	/**
	 * 检查月是否有更新，如果有更新，那返回true
	 * 
	 * @param queryBatchSerial
	 * @param parseRule
	 * @return
	 */
	private static Boolean checkmonthUpdate(NewJZBatchCodeVO queryBatchSerial,
			Map<String, String> parseRule) {
		String string = parseRule.get(NewJZBatchCodeVO.MONTHS);
		String[] split = string.split(",");
		String y = AppContext.getInstance().getBusiDate().getMonth() + "";
		Integer length = (y.length() - new Integer(split[0])) < 0 ? 0 : (y.length() - new Integer(split[0]));
		String substring = y.substring(length, y.length());
		substring = NewJZBatchCodeCreatUtil.isCompensator(substring,
				new Integer(split[0]), split[1]);
		return queryBatchSerial.getMonths().equals(substring);
	}

	/**
	 * 检查年是否有更新，如果有更新，那返回true
	 * 
	 * @param queryBatchSerial
	 * @param parseRule
	 * @return
	 */
	private static Boolean checkYearUpdate(NewJZBatchCodeVO queryBatchSerial,
			Map<String, String> parseRule) {
		String string = parseRule.get(NewJZBatchCodeVO.YEARS);
		String[] split = string.split(",");
		String y = AppContext.getInstance().getBusiDate().getYear() + "";
		Integer length = (y.length() - new Integer(split[0]))<0 ? 0 :(y.length() - new Integer(split[0]));
		String substring = y.substring(length, y.length());
		substring = NewJZBatchCodeCreatUtil.isCompensator(substring,
				new Integer(split[0]), split[1]);

		return queryBatchSerial.getYears().equals(substring);
	}

	/**
	 * 全值修正
	 * @param queryBatchSerial 批次组装库表数据
	 * @throws BusinessException
	 */
	private static void correctionsVal(NewJZBatchCodeVO queryBatchSerial)
			throws BusinessException {
		String coderule = queryBatchSerial.getCoderule();
		Map<String, String[]> parseRuleForList = parseRuleForList(coderule);
		for (String key : parseRuleForList.keySet()) {
			String attributevalue = (String) queryBatchSerial
					.getAttributeValue(key);
			if (attributevalue == null || attributevalue.isEmpty()) {
				throw new BusinessException("请检查批次号元素 【" + key
						+ "】是否正确配置，当前获取值为空，导致无法正常生成批次号！");
			}
			String compensator = NewJZBatchCodeCreatUtil.isCompensator(
					attributevalue, new Integer(parseRuleForList.get(key)[0]),
					parseRuleForList.get(key)[1]);
			queryBatchSerial.setAttributeValue(key, compensator.trim());
		}

	}

	/**
	 * * zhian.ye 20210414 校验批次号是否为可用批次号
	 * 
	 * @param nextCode
	 * @return
	 * @throws BusinessException
	 */
	public static boolean checkCode(String nextCode) throws BusinessException {
		String sql = " select 1 from ( "
				+ " select vbatchcode,dr from scm_batchcode " + " UNION "
				+ " select vprodbatchcode,dr  vbatchcode from pd_pb "
				+ " ) where  nvl(dr,0)=0 and vbatchcode ='" + nextCode + "' ";
		Object[] executeQuery = (Object[]) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class)
				.executeQuery(sql, new ArrayProcessor());
		if (null != executeQuery && executeQuery.length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 格式转换
	 * @param coderule
	 * @return
	 * @throws BusinessException
	 */
	private static Map<String, String[]> parseRuleForList(String coderule)
			throws BusinessException {
		Map<String, String> parseRule = parseRule(coderule);
		Map<String, String[]> parseRuleForList = new HashMap<String, String[]>();
		for (String key : parseRule.keySet()) {
			parseRuleForList.put(key, parseRule.get(key).split(","));
		}
		return parseRuleForList;
		// TODO Auto-generated method stub

	}

	/**
	 *  处理批次组装
	 * @param queryBatchSerial 组装库表数据
	 * @param parseRule 生成规则
	 * @return
	 */
	public static String assemble(NewJZBatchCodeVO queryBatchSerial,
			Map<String, String> parseRule) {
		StringBuffer buf = new StringBuffer();
		for (String key : parseRule.keySet()) {
			buf.append(queryBatchSerial.getAttributeValue(key));
		}
		return buf.toString();
	}

	/**
	 * 流水起始方法
	 * @param queryBatchSerial
	 * @param string
	 */
	private static void init_num(NewJZBatchCodeVO queryBatchSerial,
			String string) {
		String[] split = string.split(",");
		Integer valuelength = new Integer(split[0]);// 字段长度
		String compensator = split[1];// 进行补位字符
		queryBatchSerial.setNum(null);
		queryBatchSerial.setNum(valuelength, compensator);

	}

	/**
	 * 年度起始方法
	 * @param queryBatchSerial
	 * @param string
	 */
	private static void init_years(NewJZBatchCodeVO queryBatchSerial,
			String string) {
		String[] split = string.split(",");
		Integer valuelength = new Integer(split[0]);// 字段长度
		String compensator = split[1];// 进行补位字符
		// String setYears =
		queryBatchSerial.setYears(valuelength, compensator);
		// queryBatchSerial.setYears(setYears);

	}

	/**
	 * 月份起始方法
	 * @param queryBatchSerial
	 * @param string
	 */
	private static void init_months(NewJZBatchCodeVO queryBatchSerial,
			String string) {
		String[] split = string.split(",");
		Integer valuelength = new Integer(split[0]);// 字段长度
		String compensator = split[1];// 进行补位字符
		// String setYears =
		queryBatchSerial.setMonths(valuelength, compensator);

	}

	/**
	 * 判断流水是否符合长度标准
	 * 
	 * @param next_num
	 *            新的流水号
	 * @param string
	 *            字段规则
	 * @return 是否符合长度标准
	 */
	private static UFBoolean checkNumLength(String next_num, String string) {
		String[] split = string.split(",");
		Integer lengthForRule = new Integer(split[0]);
		if (next_num.length() <= lengthForRule) {
			return UFBoolean.TRUE;
		}
		return UFBoolean.FALSE;
	}

	/**
	 * * zhian.ye 20210414 批次号流水表同步
	 * 
	 * @param creatBatchcode
	 * @throws DAOException
	 */
	public static void batchcodeDB(NewJZBatchCodeVO creatBatchcode)
			throws DAOException {
		// TODO Auto-generated method stub
		if (null == creatBatchcode) {
			return;
		}
		creatBatchcode.setTs(AppContext.getInstance().getServerTime());
		int status = creatBatchcode.getStatus();
		if (status == VOStatus.UPDATED) {
			getDAO().updateVO(creatBatchcode);
		} else if (status == VOStatus.NEW) {
			getDAO().insertVO(creatBatchcode);
		}
	}
	
	/**
	 * 流水递增
	 * @param num
	 * @return
	 */
	private static String next_num(String num) {
		// 处理流水增加
		int num_int = new Integer(num);
		num_int = num_int + 1;
		String toStr = ((Integer) num_int).toString();
		return toStr;

	}

	/**
	 * * zhian.ye 20210414
	 * 
	 * @param pk_org
	 *            组织
	 * @param pk_material
	 *            物料号
	 * @param coderule
	 *            批次规则
	 * @return 批次流水记录
	 * @throws BusinessException
	 */
	public static NewJZBatchCodeVO queryBatchSerial(String pk_org,
			String pk_material, String coderule) throws BusinessException {

		String wheresql = "select * from jz_vbatchnumber where pk_org='"
				+ pk_org + "' and material='" + pk_material
				+ "' and coderule = '" + coderule + "'";
		List<NewJZBatchCodeVO> batchlist = null;

		batchlist = (List<NewJZBatchCodeVO>) getDAO().executeQuery(wheresql,
				new BeanListProcessor(NewJZBatchCodeVO.class));

		if (batchlist == null || batchlist.size() == 0) {
			return null;
		}

		return batchlist.get(0);
	}

	/**
	 * 
	 * @param forMap
	 *            当前值
	 * @param parseRule
	 *            规则范围
	 * @param split
	 *            当前字段规则
	 * @param coverage
	 *            归零依据
	 * @return
	 * @throws BusinessException
	 */
	private static void next_atz(NewJZBatchCodeVO queryBatchSerial)
			throws BusinessException {
		String[] strlist = NewJZBatchCodeVO.strList;
		// 1\确认下下标位置
		// 2、在原下标下做晋升 如果到最后一位，也不可晋升
		int printArrayIndex = printArrayIndex(strlist,
				queryBatchSerial.getAtz());

		if (printArrayIndex == -2 || printArrayIndex == strlist.length - 1)
			throw new BusinessException("流水已到达极限,无法实现自动分配批次号，请尝试手动录入批次号！");

		queryBatchSerial.setAtz(strlist[printArrayIndex + 1]);
		// queryBatchSerial.setNum(valuelength, compensator)

	}

	private static int printArrayIndex(String[] strlist, String obj) {
		int i = -2;
		if (null == strlist || null == obj) { // 如果列表和元素都任一一个为空时，都我返回超出范围
			return i;
		}
		for (i = 0; i < strlist.length; i++) { // 在列表内
			if (obj.equals(strlist[i])) {
				return i;
			}
		}
		return -2; // 不在列表内

	}

	/**
	 * 依据指定规则对长度进行修正
	 * 
	 * @param val
	 *            当前值
	 * @param valuelength
	 *            规则长度
	 * @param compensator
	 *            补偿字符
	 * @return 修正后的值
	 */
	public static String isCompensator(String val, Integer valuelength,
			String compensator) {
		char[] array = new char[valuelength];
		char[] charArray = val.toCharArray();
		int i = array.length - charArray.length;
		for (int j = charArray.length - 1; j >= 0; j--) {// 从右到左排列元素
			array[j + i] = charArray[j];
		}
		boolean iscompensator = false;
		if (compensator != null && !"".equals(compensator))
			iscompensator = true;
		for (int j = 0; j < i && iscompensator; j++) { // 正向补位
			array[j] = compensator.toCharArray()[0];
		}
		String string = Arrays.toString(array);
		string = string.replace(",", "");
		string = string.replace("[", "");
		string = string.replace("]", "");
		string = string.replace(" ", "");
		return string;
	}

	/**
	 * 规则解析
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static Map<String, String> parseRule(String codeRule)
			throws BusinessException {

		if (codeRule == null || "".equals(codeRule)) {
			return null;
		}
		JSONObject obj = JSONObject.fromObject(codeRule);

		return obj;

	}

	/**
	 * * zhian.ye 20210414
	 * 
	 * @param pk_org
	 * @param attributeValue
	 * @return
	 * @throws BusinessException
	 */

	private NewJZBatchCodeVO queryBatchSerial(String pk_org, String pk_material)
			throws BusinessException {
		String wheresql = "select * from jz_vbatchnumber where pk_org='"
				+ pk_org + "' and material='" + pk_material + "'";
		List<NewJZBatchCodeVO> batchlist = null;

		batchlist = (List<NewJZBatchCodeVO>) getDAO().executeQuery(wheresql,
				new BeanListProcessor(NewJZBatchCodeVO.class));

		if (batchlist == null || batchlist.size() == 0) {
			return null;
		}

		return batchlist.get(0);
	}

	/**
	 * zhian.ye 通过物料ID来获取编码
	 * 
	 * @return
	 */
	public static String getMaterialCodeByID(String pk_material) {
		VOQuery<MaterialVO> query = new VOQuery<>(MaterialVO.class);
		String wheresql = "and bd_material.pk_material = '" + pk_material + "'";
		MaterialVO[] query2 = query.query(wheresql, null);
		if (null == query2 || query2.length == 0) {
			return null;
		}
		// 有的话也应该只有一个；
		return query2[0].getCode();
	}

	/***
	 * zhian.ye20230204 通过物料来获取
	 */
	public static String getMaterialstockByID(String pk_material, String pk_org) {
		VOQuery<MaterialStockVO> query = new VOQuery<>(MaterialStockVO.class);
		String wheresql = "and bd_materialstock.pk_material = '" + pk_material
				+ "' and pk_org='" + pk_org + "'";
		MaterialStockVO[] query2 = query.query(wheresql, null);
		if (null == query2 || query2.length == 0) {
			return null;
		}
		// 有的话也应该只有一个；
		return query2[0].getDef1();
	}

	/**
	 * * zhian.ye 20210414
	 * 
	 * @return
	 */
	private static BaseDAO getDAO() {
		if (null == dao) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 检查物料是否批次号管理 zhian.ye 20220616
	 * 
	 * @param cmaterialid
	 *            物料ID
	 * @param pk_org
	 *            组织
	 */
	public static UFBoolean checkMaterialFoBatchcode(String cmaterialid,
			String pk_org) {
		// TODO Auto-generated method stub
		VOQuery<MaterialStockVO> query = new VOQuery<>(MaterialStockVO.class);
		String condition = " and pk_org ='" + pk_org + "' and pk_material = '"
				+ cmaterialid + "' and wholemanaflag = 'Y'";
		MaterialStockVO[] query2 = query.query(condition, null);
		if (query2 == null || query2.length < 0) {
			return UFBoolean.FALSE;
		} else {
			return UFBoolean.TRUE;
		}

	}

}
