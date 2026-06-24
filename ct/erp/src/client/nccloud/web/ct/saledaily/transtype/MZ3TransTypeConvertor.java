package nccloud.web.ct.saledaily.transtype;

import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.pub.billtype.BilltypeVO;
import nccloud.framework.web.convert.pattern.FormConvertor;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.form.Form;

/**
 * @description 销售合同交易类型面板数据转换
 * @author wangceb
 * @date 创建时间：2019-2-19 上午9:36:03
 * @version ncc1.0
 **/
public class MZ3TransTypeConvertor {
	
	private static final String EXTEND = "extendAttribute";

	/**
	 * 
	 * json转VO(平台调用)
	 * 
	 * @param json
	 * @param transTypeVO
	 * @return
	 *
	 */
	public Object convertToExtendVO(String json, BilltypeVO transTypeVO) {
		Form form = JsonFactory.create().fromJson(json, Form.class);
		FormConvertor convertor = new FormConvertor();
		BusinessSetVO vo = convertor.fromForm(BusinessSetVO.class, form);
		return vo;
	}

	/**
	 * 
	 * VO转换成Form
	 * 
	 * @param transTypeVO
	 * @return
	 *
	 */
	public Form convertToForm(BusinessSetVO transTypeVO) {
		FormConvertor convertor = new FormConvertor();
		return convertor.toForm(EXTEND, transTypeVO);
	}
}
