package nccloud.web.scmpub.pub.operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nccloud.framework.core.env.Locator;
import nccloud.framework.core.json.IJson;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.translate.Translator;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.processor.template.ExtBillCardConvertProcessor;
import nccloud.framework.web.ui.config.Area;
import nccloud.framework.web.ui.config.ITempletResource;
import nccloud.framework.web.ui.config.PageTemplet;
import nccloud.framework.web.ui.config.TempletQueryPara;
import nccloud.framework.web.ui.meta.AreaRelation;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardFormulaHandler;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.web.scmpub.pub.utils.formula.SCMExtRelationFomulaUtil;

/**
 * 
 * <p>
 * Description: 供应链ExtBillCard转换类，增加执行编辑公式，关联项处理
 * </p>
 * 
 * @author CongKe
 * @date 2018年12月12日 下午1:20:21
 * @version ncc1.0
 */
public class SCMExtBillCardOperator {

	private PageTemplet templet;

	private String templetid;

	private String pagecode;

	private Map<String, Object> userObj = new HashMap<String, Object>();

	private ExtBillCard extBillCard;
	
	/**
	 * 构造函数
	 */
	public SCMExtBillCardOperator() {
		super();
	}

	/**
	 * 构造函数
	 */
	public SCMExtBillCardOperator(PageTemplet templet) {
		this(templet.getOid(), templet.getPagecode());
		this.templet = templet;
	}

	/**
	 * 构造函数
	 */
	public SCMExtBillCardOperator(PageTemplet templet, ExtBillCard extCard) {
		this(templet.getOid(), templet.getPagecode(), extCard);
		this.templet = templet;
	}

	public SCMExtBillCardOperator(String templetid, String pagecode,
			ExtBillCard extBillCard) {
		this.templetid = templetid;
		this.pagecode = pagecode;
		this.extBillCard = extBillCard;
	}

	public SCMExtBillCardOperator(String templetid, String pagecode) {
		this(templetid, pagecode, null);
	}
	
	public SCMExtBillCardOperator(String pagecode) {
		this(null, pagecode, null);
	}

	/**
	 * 
	 * 编辑态Card转换
	 * 
	 * @param vo
	 * @return
	 * 
	 */
	public ExtBillCard toEditExtCard(Object vo, String moduleId, String editKey) {
		// 判断模板是否存在
		if (this.templet == null) {
			this.loadTemplet();
		}
		Map<String, AreaRelation> relations = this.templet.getRelations();
		List<String> extcodes = new ArrayList<String>();
		for (String bodycode : relations.keySet()) {
			for (Area area : this.templet.getAllAreas()) {
				if (bodycode.equals(area.getRelationcode())
						&& !bodycode.equals(area.getCode())) {
					extcodes.add(area.getCode());
				}
			}
		}
		// 转换
		ExtBillCardConvertProcessor processor = new ExtBillCardConvertProcessor(
				extcodes, this.templet);
		ExtBillCard extBillCard = processor.convertByTempletid(templetid, pagecode,
				vo);
		// 执行编辑公式(表体多选场景下,才会执行)
		SCMExtRelationFomulaUtil fomulaUtil = new SCMExtRelationFomulaUtil(
				this.templet);
		fomulaUtil.handleMultiSelectRelationAndFomula(extBillCard, moduleId,
				editKey, this.userObj);
		// 显示公式计算
		ExtBillCardFormulaHandler handler = new ExtBillCardFormulaHandler(
				extBillCard, this.templet);
		handler.handleLoadFormula();
		handler.handleBodyLoadFormula();
		// 翻译
		Translator translator = new Translator(this.templet);
		translator.translate(extBillCard);
		return extBillCard;
	}

	/**
	 * 
	 * 加载模板
	 * 
	 */
	private void loadTemplet() {
		// 模板查询
		ITempletResource resourc = Locator.find(ITempletResource.class);
		TempletQueryPara para = new TempletQueryPara();
		para.setPagecode(this.pagecode);
		para.setTemplateid(this.templetid);
		this.templet = resourc.query(para);
	}

	/**
	 * 
	 * 设置自定义对象
	 * 
	 * @param userObj
	 *
	 */
	public void setUserObj(Map<String, Object> userObj) {
		this.userObj = userObj;
	}
	
  /**
   * 转vo
   * 
   * @param request
   * @return T
   */
  public <T> T toBill(IRequest request) {
    // 接收并转化参数，json转化为ExtBillCard
    String str = request.read();
    IJson json = JsonFactory.create();
    ExtBillCard card = json.fromJson(str, ExtBillCard.class);
    this.pagecode = card.getPageid();
    this.templetid = card.getTempletid();
    this.extBillCard = card;
    // ExtBillCard转化为vo
    ExtBillCardConvertProcessor processor = new ExtBillCardConvertProcessor();
    T t = processor.fromExtBillCard(card);
    return t;
  }
	
  
  /**
   * 
   * 转换成ExtBillCard
   * @param vo
   * @return
   *
   */
  public ExtBillCard toCard(Object vo) {
  	ExtBillCard card = this.toNoTransCard(vo);
  	this.translate(card);
  	return card;
  }
	
	/**
	 * 
	 * 转换成不翻译的ExtBillCard
	 * @param vo
	 * @return
	 *
	 */
	public ExtBillCard toNoTransCard(Object vo) {
		// 判断模板是否存在
		if (this.templet == null) {
			this.loadTemplet();
		}
	  Map<String, AreaRelation> relations = this.templet.getRelations();
	  List<String> extcodes = new ArrayList<String>();
	  for (String bodycode : relations.keySet()) {
	    for (Area area : this.templet.getAllAreas()) {
	      if (bodycode.equals(area.getRelationcode())
	          && !bodycode.equals(area.getCode())) {
	        extcodes.add(area.getCode());
	      }
	    }
	  }
	  // vo转ExtBillCard
	  ExtBillCardConvertProcessor processor =
	  		new ExtBillCardConvertProcessor(extcodes, this.templet);
	  ExtBillCard retcard =
	      processor.convertByTempletid(this.templetid, this.pagecode, vo);
	  // 显示公式计算
	  ExtBillCardFormulaHandler handler =
	      new ExtBillCardFormulaHandler(retcard, this.templet);
	  handler.handleLoadFormula();
	  handler.handleBodyLoadFormula();
	  return retcard;
	}
	
	/**
	 * 
	 * 单独执行翻译操作
	 * 
	 * @param billCard
	 * 
	 */
	public void translate(ExtBillCard billCard) {
		// 判断模板是否存在
		if (this.templet == null) {
			this.loadTemplet();
		}
		// 翻译
		Translator translator = new Translator(this.templet);
		translator.translate(billCard);
	}
	
	/**
	 * 
	 * 获取原始Card
	 * @return
	 *
	 */
	public ExtBillCard getOriginalCard() {
		return this.extBillCard;
	}
	
	
}
