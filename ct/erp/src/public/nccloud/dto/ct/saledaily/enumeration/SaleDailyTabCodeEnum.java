package nccloud.dto.ct.saledaily.enumeration;

import nccloud.base.type.AbstractStringEnum;

/**
 * @description 销售合同枚举
 * @author wangshrc
 * @date 2019年3月5日 上午9:47:10
 * @version ncc1.0
 */
public class SaleDailyTabCodeEnum extends AbstractStringEnum {
	private static final long serialVersionUID = 1184502885301685675L;

	/**
	 * 全部
	 */
	public static final SaleDailyTabCodeEnum All = new SaleDailyTabCodeEnum(
			"all", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0013")/*@res "全部"*/);

	/**
	 * 全部枚举值
	 */
	public static String[] AllEnums = { SaleDailyTabCodeEnum.All.value() };

	private SaleDailyTabCodeEnum(String value, String label) {
		super(value, label);
	}
}