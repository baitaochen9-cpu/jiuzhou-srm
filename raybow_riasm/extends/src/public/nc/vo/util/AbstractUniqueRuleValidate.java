/**
 * 
 */
package nc.vo.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.uif2.BusinessExceptionAdapter;
import nc.bs.uif2.validation.UniqueValidationFailure;
import nc.bs.uif2.validation.ValidationFailure;
import nc.bs.uif2.validation.Validator;
import nc.itf.bd.config.uniquerule.IBDUniqueruleQryService;
import nc.itf.bd.config.uniquerule.UniqueRuleConst;
import nc.itf.bd.userdefitem.IUserdefitemQryService;
import nc.itf.org.IOrgConstBasic;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.DASFacade;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.IColumn;
import nc.md.model.MetaDataException;
import nc.md.model.type.IEnumType;
import nc.md.model.type.IType;
import nc.md.model.type.impl.RefType;
import nc.mddb.model.impl.MultiColumn;
import nc.vo.bd.config.BDUniqueruleItemVO;
import nc.vo.bd.config.BDUniqueruleVO;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.meta.VO2BDObjectAdatpter;
import nc.vo.bd.userdefrule.UserdefItemUtil;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.LanguageVO;
import nc.vo.ml.MultiLangContext;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pub.lang.UFBoolean;

/**
 * 档案唯一性规则校验类
 * @author guoting
 * 
 * @update lkp 重写唯一性校验逻辑
 */
public abstract class AbstractUniqueRuleValidate implements Validator, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5881439153846626149L;
	
	protected IBean bean = null; 
	
	protected boolean checknullFlag = false; //默认false，唯一性规则中的空值字段不参与校验

	protected boolean needReturnUniqueInfo = false;
	
	public AbstractUniqueRuleValidate() {
		super();
	}

	public AbstractUniqueRuleValidate(IBean bean) {
		super();
		this.bean = bean;
	}
	
	public AbstractUniqueRuleValidate(IBean bean, boolean checknullFlag) {
		super();
		this.bean = bean;
		this.checknullFlag = checknullFlag;
	}
	
	/**
	 * 查询对象是否满足其配置的唯一性规则
	 * 
	 * @param object 
	 * @return
	 */
	public ValidationFailure validate(Object objects) {
		
		if(objects == null || (objects.getClass().isArray() && ((Object[])objects).length == 0))
			return null;
		
		if (objects.getClass().isArray() && ((Object[])objects).length > 1) 
		{
			Object[] objs = (Object[])objects;
			SuperVO[] vos = new SuperVO[objs.length];
			for(int i = 0;i < objs.length; i++)
				vos[i] = (SuperVO)objs[i];
			
			return validateMultiVO(vos);
			
		} else {
			
			//进行单对象的唯一性规则校验
			SuperVO vo = null;
			if (objects.getClass().isArray() && ((Object[])objects).length == 1)
			{
				vo = (SuperVO) ((Object[])objects)[0];
			} else {
				vo = (SuperVO) objects;
			} 
	
			return validateSingleVO(vo);
		} 
		
	}  
	
	private ValidationFailure validateSingleVO(SuperVO vo)
	{
		try {
			if(this.bean == null) {
				bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(vo.getClass().getName());
				
			}
			if(this.bean == null) 
			{
				throw new RuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("uffactory_hyeaa", "AbstractUniqueRuleValidate-000001", null, new String[]{this.getClass().getName()})/*利用{0}进行唯一性校验，必须具有元数据模型！*/);
			}
			
			Collection<BDUniqueruleVO> rules = getUniqueRules(bean, new SuperVO[] {vo});
			if(rules == null || rules.size() == 0)
				return null;

			String uniquescope = getUniqueScopeWhere(vo,bean);
			List<BDUniqueruleVO> rulevoList = getBreakUniqueRule(vo, rules, uniquescope, null);
			String errorcode = getErrorCode(rulevoList);
			String info = getDetailErrorMsg(rulevoList, bean, vo);
			if(StringUtil.isEmptyWithTrim(info))
				return null;
			
			LinkedHashMap<Integer, String> detailMsgMap = new LinkedHashMap<Integer, String>();
			detailMsgMap.put(System.identityHashCode(vo), info);
			
			return createFailure(info, errorcode, detailMsgMap);
			
		} catch (MetaDataException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessExceptionAdapter(e);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e); 
			throw new BusinessExceptionAdapter(e);
		}
	}
 
	/**
	 * 获取违反的唯一性规则
	 * 
	 * @param vo
	 * @param rules
	 * @param uniquescope
	 * @param toBeValidatePKList 待校验的vo主键集合
	 * @return
	 * @throws BusinessException
	 */
	protected List<BDUniqueruleVO> getBreakUniqueRule(SuperVO vo, Collection<BDUniqueruleVO> rules, String uniquescope, ArrayList<String> toBeValidatePKList)
			throws BusinessException {
		if (checknullFlag) {
			List<BDUniqueruleVO> rulevoList = getService().getBreakUniqueRuleByVOWithNullCheck(vo, bean, rules, uniquescope, toBeValidatePKList);
			return rulevoList;
		} else {
			List<BDUniqueruleVO> rulevoList = getService().getBreakUniqueRuleByVO(vo, bean, rules, uniquescope, toBeValidatePKList);
			return rulevoList;
		}
	}
	
//	protected String getErrorMsg(String detailErrorMsg)
//	{
//		return "保存失败！下列字段值已存在，不允许重复，请检查：\n" + detailErrorMsg;
//	}
	
	protected String getHintMsg()
	{
//		return "保存失败！下列字段值已存在，不允许重复，请检查：\n";
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("uffactory_hyeaa", "AbstractUniqueRuleValidate-000000")/*下列字段值已存在，不允许重复，请检查：\n*/;
	}
	
//	protected String getErrorMsgSelfBreakRule(String errorMsg)
//	{
//		return "保存失败！保存数据本身存在如下重复的记录，请检查：\n" + errorMsg;
//	}
	
	protected abstract String getUniqueScopeWhere(SuperVO vo,IBean bean) throws BusinessException;
	
	protected abstract Collection<BDUniqueruleVO> getUniqueRules(IBean bean, SuperVO[] vos) throws BusinessException;
	
	private ValidationFailure validateMultiVO(SuperVO[] vos)
	{
		try {  
			if(this.bean == null) {
				bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(vos[0].getClass().getName());
			}
			if(this.bean == null)
			{
				throw new RuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("uffactory_hyeaa", "AbstractUniqueRuleValidate-000001", null, new String[]{this.getClass().getName()})/*利用{0}进行唯一性校验，必须具有元数据模型！*/);
			}
			
			Collection<BDUniqueruleVO> rules = getUniqueRules(bean, vos);
			if(rules == null || rules.size() == 0)
				return null;

			//验证自身数据是否违反唯一性教研规则
			ValidationFailure vf = validateDataSelf(vos, rules);
			if(vf != null)
				return vf;
			
			Map<SuperVO, List<BDUniqueruleVO>> dataVO_breakRuleList_map = new LinkedHashMap<SuperVO, List<BDUniqueruleVO>>();
			
			//获取所有待校验数据的pk集合
			ArrayList<String> toBeValidatePKList = new ArrayList<String>();
			for(SuperVO vo : vos) {
				toBeValidatePKList.add(vo.getPrimaryKey());
			}
			
			for(SuperVO vo : vos)
			{
				String uniquescope = getUniqueScopeWhere(vo, bean);
				List<BDUniqueruleVO> rulevoList = getBreakUniqueRule(vo, rules, uniquescope, toBeValidatePKList);
				if(rulevoList != null && !rulevoList.isEmpty())
					dataVO_breakRuleList_map.put(vo, rulevoList);
			}
			
			if(dataVO_breakRuleList_map.isEmpty()) 
				return null;
			
			String errorInfo = "";
			LinkedHashMap<Integer, String> detailMsgMap = new LinkedHashMap<Integer, String>();
			for(Iterator<SuperVO> keyIter = dataVO_breakRuleList_map.keySet().iterator(); keyIter.hasNext();)
			{
				SuperVO vo = keyIter.next();
				String info = getDetailErrorMsg(dataVO_breakRuleList_map.get(vo), bean, vo);
				if(StringUtil.isEmptyWithTrim(info))
					continue;
				
				detailMsgMap.put(System.identityHashCode(vo), info);
				errorInfo += info; 
			}
			
			if(!StringUtil.isEmptyWithTrim(errorInfo)) {
				
				List<BDUniqueruleVO> allBreakRuleList = new ArrayList<BDUniqueruleVO>();
				for(List<BDUniqueruleVO> breakRuleList : dataVO_breakRuleList_map.values()){
					allBreakRuleList.addAll(breakRuleList);
				}
				return createFailure(errorInfo, getErrorCode(allBreakRuleList), detailMsgMap);
			}
			
		} catch (MetaDataException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessExceptionAdapter(e);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessExceptionAdapter(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private ValidationFailure validateDataSelf(SuperVO[] vos, Collection<BDUniqueruleVO> rules) {
		
		Map<BDUniqueruleVO, List<SuperVO>> map = getBreakRuleofVosSelf(vos, rules, bean);
		if(map == null || map.isEmpty())
			return null;
		
		Map<SuperVO, List<BDUniqueruleVO>> voMap = new LinkedHashMap<SuperVO, List<BDUniqueruleVO>>();
		for(Iterator<BDUniqueruleVO> iter = map.keySet().iterator(); iter.hasNext();)
		{
			BDUniqueruleVO uniquevo = iter.next();
			List<SuperVO> list = map.get(uniquevo);
			for(SuperVO vo : list)
			{
				if(voMap.containsKey(vo))
				{
					List<BDUniqueruleVO> listx = voMap.get(vo);
					if(!listx.contains(uniquevo))
						listx.add(uniquevo);
				}else {
					List<BDUniqueruleVO> listx = new ArrayList<BDUniqueruleVO>();
					listx.add(uniquevo);
					voMap.put(vo, listx);
				}
			}
		}
		
		String errorMsg = "";
		LinkedHashMap<Integer, String> errorMsgMap = new LinkedHashMap<Integer, String>();
		for(Iterator<SuperVO> iter = voMap.keySet().iterator(); iter.hasNext();)
		{
			SuperVO vo = iter.next();
			String info = getDetailErrorMsg(voMap.get(vo), bean, vo);
			if(StringUtil.isEmptyWithTrim(info))
				continue;
			errorMsg += info;
			errorMsgMap.put(System.identityHashCode(vo), info);
		}
			
		//取出违反唯一性约束的规则数组的异常号
		List<BDUniqueruleVO> ruleList = new ArrayList<BDUniqueruleVO>(map.keySet());
		String errorcode = getErrorCode(ruleList);
		return createFailure(errorMsg, errorcode, errorMsgMap);
		
	}
	
	protected Map<BDUniqueruleVO, List<SuperVO>> getBreakRuleofVosSelf(SuperVO[] vos, Collection<BDUniqueruleVO> rules, IBean bean)
	{
		Map<BDUniqueruleVO, List<SuperVO>> result = new HashMap<BDUniqueruleVO, List<SuperVO>>();
		for(BDUniqueruleVO rule : rules)
		{
			boolean eachRuleCheckNullFlag = rule.getIschecknull() != null && UFBoolean.TRUE.equals(rule.getIschecknull());
			List<SuperVO> list = new ArrayList<SuperVO>();
			Map<String, SuperVO> map = new HashMap<String, SuperVO>();
			for(SuperVO vo : vos)
			{
				BDUniqueruleItemVO[] items = rule.getRuleitems();
				if(items == null || items.length == 0)
					throw new RuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("uffactory_hyeaa", "AbstractUniqueRuleValidate-000002")/*唯一性规则设置错误，唯一性规则必须设置唯一性项！*/);
				
				String validateKeyValueStr = "";//待校验重复的值串
				try {
					 //每条数据在校验规则字段值是否重复时需要加上唯一性范围
					String uniqueScope = getUniqueScopeWhere(vo, bean);
					if (!StringUtil.isEmpty(uniqueScope)) {
						validateKeyValueStr += uniqueScope;
					}
					
				} catch (BusinessException e) {
					throw new BusinessExceptionAdapter(e);
				}
				boolean hasNullValue = false;
				boolean hasMultiColumn = false;
				ArrayList<IColumn> multicolumns = new ArrayList<IColumn>();
				for(BDUniqueruleItemVO item : items) {
					String attriId = item.getMdcolumnid();
					IAttribute attr = bean.getAttributeByID(attriId);
					IColumn column = attr.getColumn();
					
					if(!checknullFlag && !eachRuleCheckNullFlag && UniqueConditionUtil.isValueNull(column, attr, vo))
					{
						hasNullValue = true;
						break;
					} 
					if (column instanceof MultiColumn) {
						//多语类型得成分多个语种分开比较
						hasMultiColumn = true;
						multicolumns.add(column);
					} else {
						Object value = vo.getAttributeValue(bean.getAttributeByID(item.getMdcolumnid()).getName());
						validateKeyValueStr += value;
					}
				}
				
				//如果不校验空值字段，且存在唯一性规则字段值为空，则不进行校验
				if (!checknullFlag && !eachRuleCheckNullFlag && hasNullValue)
					continue; 
				 
				ArrayList<String> itemkeyList = new ArrayList<String>();
				if (!hasMultiColumn) {
					if(!validateSelfCaseSensitive()) {
						validateKeyValueStr = validateKeyValueStr.toUpperCase();
					}
					itemkeyList.add(validateKeyValueStr);
				} else if (hasMultiColumn && multicolumns != null && multicolumns.size() > 0) {
					//循环多语字段，放入多个语种的值串
					int envLangCount = MultiLangContext.getInstance().getEnableLangVOs().length;
					for (int i = 0; i < envLangCount; i++) {
						StringBuffer withMultiColumnValuestr = new StringBuffer();
						withMultiColumnValuestr.append(validateKeyValueStr);
						for (IColumn multicolumn : multicolumns) {
							List<IColumn> columns = ((MultiColumn)multicolumn).getColumns();
							//多语字段值为空，则不放入比较
							if (vo.getAttributeValue(columns.get(i).getName()) == null)
								continue;
							withMultiColumnValuestr.append(columns.get(i).getName() + "=" + vo.getAttributeValue(columns.get(i).getName()));
						}
						if (withMultiColumnValuestr.length() > validateKeyValueStr.length()) {
							if(!validateSelfCaseSensitive()) {
								itemkeyList.add(withMultiColumnValuestr.toString().toUpperCase());
							} else {
								itemkeyList.add(withMultiColumnValuestr.toString());
							}
						}
					}
				}
				
				//对多个值串放入map进行唯一比较，有重复的放入list
				if (itemkeyList != null && itemkeyList.size() > 0) {
					for (Iterator iterator = itemkeyList.iterator(); iterator.hasNext();) {
						String eachItemKey = (String) iterator.next();
						if(map.containsKey(eachItemKey) && !list.contains(map.get(eachItemKey))) {
							list.add(map.get(eachItemKey));
							list.add(vo);
						}else if(map.containsKey(eachItemKey)) {
							list.add(vo);
						}else {
							map.put(eachItemKey, vo);
						}
					}
				}
				
			} 
			
			if(!list.isEmpty())
			{
				result.put(rule, list);
			}
		}
		if(result.isEmpty())
			return null;
		else
			return result;
	}
	
	protected boolean validateSelfCaseSensitive()
	{
		return true;
	}
	
	protected String getErrorCode(List<BDUniqueruleVO> ruleList) {
		if(ruleList == null || ruleList.isEmpty())
			return null;
		String errorcode = UniqueRuleConst.OTHERBREAKUNIQUE;
		//优先返回32001编码重复异常
		for(BDUniqueruleVO rulevo : ruleList) {
			if (rulevo.getErrorcode().equals(UniqueRuleConst.CODEBREAKUNIQUE)) {
				return rulevo.getErrorcode();
			} else if (rulevo.getErrorcode().equals(UniqueRuleConst.NAMEBREAKUNIQUE)) {
				errorcode = rulevo.getErrorcode();
			}
		}
		return errorcode;
	}
	
	protected String getDetailErrorMsg(List<BDUniqueruleVO> ruleList, IBean bean, SuperVO vo)
	{
		if(ruleList == null || ruleList.isEmpty())
			return null;
		String msg = "";
		for(BDUniqueruleVO rulevo : ruleList)
		{
			boolean eachRuleCheckNullFlag = rulevo.getIschecknull() != null && UFBoolean.TRUE.equals(rulevo.getIschecknull());
			BDUniqueruleItemVO[] items = rulevo.getRuleitems();
			String ruleInfo = "";
			String valueInfo = "";
			for(BDUniqueruleItemVO item : items)
			{
				String attriId = item.getMdcolumnid();
				IAttribute attr = bean.getAttributeByID(attriId);
				IColumn column = attr.getColumn();
				
				Object value = vo.getAttributeValue(attr.getName());
				boolean isValueNullFlag = UniqueConditionUtil.isValueNull(column, attr, vo);
				
				if(!checknullFlag && !eachRuleCheckNullFlag && isValueNullFlag)
					continue;
				
				if(!StringUtil.isEmptyWithTrim(ruleInfo))
				{
					ruleInfo += "+";
					valueInfo += "+";
				}
				
				
				if (attr.getDataType().getTypeType() == IType.TYPE_CUSTOM) {
					//用户自定义属性，需要查询用户自定义的属性显示名称，显示当前语种对应文本
					Map<Integer, MultiLangText> userdefMap = getUserDefUniqueruleItemNameMap(bean.getID(), getPK_OrgByVO(vo));
					int propIndex = 0;
					try {
						propIndex = UserdefItemUtil.getUserdefColumnIndex(attr.getName());
					} catch (BusinessException e) {
						throw new BusinessExceptionAdapter(e);
					}
					MultiLangText showname = userdefMap.get(propIndex);
					ruleInfo += showname.toString();
				} else if(attr != null){
						ruleInfo += attr.getDisplayName();					
					
				}
//				valueInfo += value; 
				
				//空值参与唯一性规则
				if ((checknullFlag || eachRuleCheckNullFlag) && isValueNullFlag) {
					valueInfo += NCLangResOnserver.getInstance().getStrByID("org", "OrgRelationMultiplicityUniqueRuleValidate-000005")/*空*/;
				} else {
					valueInfo += getValue(bean, vo, attr, value);
				}
			}

			if(!StringUtil.isEmptyWithTrim(msg))
				msg += ", ";
			msg += (ruleInfo + ": " + valueInfo);
		}
		
		if(StringUtil.isEmptyWithTrim(msg))
			return null;
		return "[" + msg + "]\n";
	}
	
	//获取数据提取对应用户自定义属性时需传入的组织值（全局或集团）
	private static String getPK_OrgByVO(SuperVO obj) {
		// 得到实体的所属组织、所属集团的字段值
		IBDObject bdo = new VO2BDObjectAdatpter(obj);
		String pk_org = (String) bdo.getPk_org();
		String pk_group = (String) bdo.getPk_group();
		
		if (IOrgConstBasic.GLOBEORG.equals(pk_org)) {
			return IOrgConstBasic.GLOBEORG;
		} 
		return pk_group;
	}
		
	private Map<Integer, MultiLangText> getUserDefUniqueruleItemNameMap(String mdclassid, String pk_org) {
		Map<Integer, MultiLangText> defItemMap = new HashMap<Integer, MultiLangText>();
		UserdefitemVO[] defitemVOs = getUserDefitemVOs(mdclassid, pk_org);
		if (!ArrayUtils.isEmpty(defitemVOs)) {
			for (int i = 0; i < defitemVOs.length; i++) {
				MultiLangText multiLangText = new MultiLangText();
				multiLangText.setText(defitemVOs[i].getShowname());
				multiLangText.setText2(defitemVOs[i].getShowname2());
				multiLangText.setText3(defitemVOs[i].getShowname3());
				multiLangText.setText4(defitemVOs[i].getShowname4());
				multiLangText.setText5(defitemVOs[i].getShowname5());
				multiLangText.setText6(defitemVOs[i].getShowname6());
				defItemMap.put(defitemVOs[i].getPropindex(), multiLangText);
			}
		}
		return defItemMap;
	}
	
	private Map<Integer, UserdefitemVO> getUserDefUniqueruleItemMap(String mdclassid, String pk_org) {
		Map<Integer, UserdefitemVO> defItemMap = new HashMap<Integer, UserdefitemVO>();
		UserdefitemVO[] defitemVOs = getUserDefitemVOs(mdclassid, pk_org);
		if (!ArrayUtils.isEmpty(defitemVOs)) {
			for (int i = 0; i < defitemVOs.length; i++) {
				defItemMap.put(defitemVOs[i].getPropindex(), defitemVOs[i]);
			}
		}
		return defItemMap;
	}
	
	private UserdefitemVO[] getUserDefitemVOs(String mdclassid,String pk_org){
		IUserdefitemQryService service = NCLocator.getInstance().lookup(
				IUserdefitemQryService.class);
		UserdefitemVO[] defitemVOs = null;
		try {
			defitemVOs = service.qeuryUserdefitemVOsByMDClassID(mdclassid,
					pk_org);
		} catch (BusinessException e) {
			Logger.error(e);
			throw new BusinessExceptionAdapter(e);
		}
		return defitemVOs;
	}
	
	private ValidationFailure createFailure(String errorInfo, String errorcode, LinkedHashMap<Integer, String> detailErrorMap)
	{//lkp将提示信息和具体错误信息进行分离，便于异常后期格式化处理
		UniqueValidationFailure vf = null;
		vf = new UniqueValidationFailure();
		vf.setHintMsg(getHintMsg());
		vf.setDetailMsg(errorInfo);
		vf.setErrorcode(errorcode);
		vf.setErrorMsgMap(detailErrorMap);
		return vf;
	}
	
//	private ValidationFailure createFailure(String errorInfo, String errorcode)
//	{
//		return createFailure(errorInfo, errorcode, null);
//	}
	
	protected Object getValue(IBean bean , SuperVO vo, IAttribute attr, Object value)
	{
		//多语属性，按多个语种在当前环境配置的语种顺序组装值信息
		if (attr.getColumn() instanceof MultiColumn) {
			//当前环境支持的语种个数
			LanguageVO[] enlangvos = MultiLangContext.getInstance().getEnableLangVOs();

			List<IColumn> columns = ((MultiColumn)attr.getColumn()).getColumns();
			if (columns != null && columns.size() > 0) {
				StringBuffer valuestr = new StringBuffer(); 
				int i = 1;
				//最多能支持6个语种，但一般环境不会配那么多个语种，需要判断只对支持语种进行校验
				for (IColumn iColumn : columns) {
					if (i > enlangvos.length)
						break;
					//字段数据后加上（语种缩写）用于区分不同语种
					if (vo.getAttributeValue(iColumn.getName()) == null) {
						valuestr.append(" | " + NCLangResOnserver.getInstance().getStrByID("org", "OrgRelationMultiplicityUniqueRuleValidate-000005")/*空*/ + "(" + enlangvos[i-1].getLocallang() + ")");
					} else {
						valuestr.append(" | " + vo.getAttributeValue(iColumn.getName()) + "(" + enlangvos[i-1].getLocallang() + ")");
					}
					i++;
						
				}
				return valuestr.substring(3);
			}
		}

		IType type  = attr.getDataType();
		
		if(type instanceof IEnumType)
			return ((IEnumType)type).getConstEnum(value).getName();

		IBean beanx = null;
		if(!(type instanceof RefType)){
			if (attr.getDataType().getTypeType() == IType.TYPE_CUSTOM) {
				//用户自定义属性，需要查询用户自定义的属性类型
				int propIndex = 0;
				try {
					propIndex = UserdefItemUtil.getUserdefColumnIndex(attr.getName());
					UserdefitemVO item = getUserDefUniqueruleItemMap(bean.getID(), getPK_OrgByVO(vo)).get(propIndex);
					if(item != null){
						beanx = MDBaseQueryFacade.getInstance().getBeanByID(item.getClassid());
					}
				} catch (BusinessException e) {
					throw new BusinessExceptionAdapter(e);
				}
			} 
		}
		else{
			RefType refType = (RefType) type;
			beanx = refType.getRefType();
		}
			
		value = getNameValue(beanx,value);
		return value;
	}
	
//	private ValidationFailure getValidationFailure(BDUniqueruleVO... rules)
//	{
//		String msg = "";
//		for(BDUniqueruleVO vo : rules)
//			msg += vo.getRulecontent() + ";";
//		vf.setMessage("必须满足以下唯一性规则：" + msg.substring(0, msg.length() - 1));
//		return vf;
//	}

	protected IBDUniqueruleQryService getService() {
		return NCLocator.getInstance().lookup(IBDUniqueruleQryService.class);
	}
	
	private Object getNameValue(IBean beanx,Object value){
		if((beanx!= null && beanx instanceof IBusinessEntity)){
			IBusinessEntity entity = (IBusinessEntity) beanx;
			if(entity.isImplementBizInterface(IBDObject.class.getName())){
				Map<String, String> map = entity.getBizInterfaceMapInfo(IBDObject.class.getName());
				String nameAttrPath = map.get("name"); 
				if(nameAttrPath != null) {
					Map<String, Object> valueMap = DASFacade.getAttributeValues(beanx, (String)value, new String[] {nameAttrPath});
					Object nameValue =  valueMap.get(nameAttrPath);
					if(nameValue != null)
						return nameValue;
				}
			}
		}
		return value;
	}
	
}