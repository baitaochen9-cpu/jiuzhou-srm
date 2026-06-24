package nccloud.web.ct.saledaily.transtype;

import nc.itf.ct.business.BusinessTypeService;
import nc.vo.ct.business.entity.BusinessSetVO;
import nccloud.framework.core.env.Locator;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.translate.Translator;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.config.ITempletResource;
import nccloud.framework.web.ui.config.PageTemplet;
import nccloud.framework.web.ui.config.TempletQueryPara;
import nccloud.framework.web.ui.pattern.form.Form;
import nccloud.web.ct.pub.entry.SimpleQueryConditon;

/**
 * @description 销售合同交易类型面板查询
 * @author wangceb
 * @date 创建时间：2019-2-19 下午3:31:38
 * @version ncc1.0
 **/
public class MZ3QueryTransTypeAction implements ICommonAction {
	
	private static final String CTRANTYPEID = "pk_billtypeid";
	
	private static final String TEMPLATEID = "templateid";

	@Override
	public Object doAction(IRequest request) {
		try {
			String str = request.read();
			IJson json = JsonFactory.create();
			SimpleQueryConditon condition = json.fromJson(str, SimpleQueryConditon.class);
			String ctrantypeid = (String) condition.getConditonByKey(CTRANTYPEID);
			String templateid = (String) condition.getConditonByKey(TEMPLATEID);

			BusinessSetVO[] returnVos = BusinessTypeService
					.queryBusinesstypeExtProp(" and ctrantypeid = '" + ctrantypeid + "' and dr = 0 ");

			if (returnVos == null || returnVos.length == 0) {
				return null;
			}
			MZ3TransTypeConvertor convertor = new MZ3TransTypeConvertor();
			Form form = convertor.convertToForm(returnVos[0]);
			Translator translator = new Translator(this.queryTemplet(templateid));
			translator.translate(form);
			return form;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	private PageTemplet queryTemplet(String templateid) {
		ITempletResource resource = Locator.find(ITempletResource.class);
		TempletQueryPara para = new TempletQueryPara();
		para.setTemplateid(templateid);
		return resource.query(para);
	}

}
