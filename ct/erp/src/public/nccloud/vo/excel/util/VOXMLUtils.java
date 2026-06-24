package nccloud.vo.excel.util;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import nc.bs.logging.Logger;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pfxx.exception.PfxxRuntimeException;
import nc.vo.pfxx.pub.SwapException;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Field;
import nccloud.vo.excel.scheme.IFieldDataType;
import nccloud.vo.excel.scheme.IRecordType;
import nccloud.vo.excel.scheme.Record;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by IntelliJ IDEA.
 * User: cch
 * Date: 2004-12-13
 * Time: 13:13:53
 * VO取值赋值基本操作类
 */
public class VOXMLUtils
{
	private static final SimpleDateFormat	SIMPLE_DATE_FORMAT	= new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

	/**
	 * 取得简单字段的值。
	 *
	 * @param curvo
	 * @param fieldname
	 * @return
	 */
	public static String getProperty(ValueObject curvo, String fieldname)
	{
		Object fieldvalue = getComplexProperty(curvo, fieldname);
		String strfieldvalue = fieldvalue == null ? null : fieldvalue.toString();
		return strfieldvalue;
	}

	/**
	 * 取得外部简单字段的值。
	 *
	 * @param curvo
	 * @param fieldname
	 * @return
	 */
	public static Object getExternalSimplePropertyValue(Object curvo, Field field) throws SwapException
	{
		Object exvalue = getComplexProperty(curvo, field.getName());
		if (exvalue == null)
		{
			String defaultValue = field.getDefaultValue();
			if (!StringUtil.isEmpty(defaultValue))
				exvalue = defaultValue;
		}
		//转换类型
		return valueOfExternalObj(field, exvalue);
	}
	

	/**
	 * 取得外部VO复杂字段的值,不需要进行类型转换,比如VO，VO数组等
	 *
	 * @param curvo
	 * @param fieldname
	 * @return
	 */
	public static Object getExternalComplexPropertyValue(Object curvo, String fieldName)
	{
		return getComplexProperty(curvo, fieldName);
	}

	/**
	 * 取得内部的值，将被赋给外部VO
	 * @param innervo
	 * @param fieldName
	 * @return
	 * @throws SwapException
	 */
	public static Object getInnerComplexPropertyValue(Object innervo, String matchtag) throws SwapException
	{
		return getComplexProperty(innervo, matchtag);
	}

	/**
	 * 取得内部的值，将被赋给外部VO
	 * @param innervo
	 * @param fieldName
	 * @return
	 * @throws SwapException
	 */
	public static Object getInnerSimplePropertyValue(Object innervo, Object externalvo, Field field) throws SwapException
	{
		Object exvalue = getComplexProperty(innervo, field.getMatchTag());
		//转换类型
		return valueOfInnerObj(field, exvalue, getExternalFieldClassName(externalvo, field.getName()));
	}

	/**
	 * 取得外部字段的类信息
	 * @param externalvo
	 * @param fieldName
	 */
	public static String getExternalFieldClassName(Object externalvo, String fieldName) throws SwapException
	{
		Class attrClass = getExternalFieldClass(externalvo, fieldName);
		return attrClass.getName();
	}

	/**
	 * 取得外部字段的类信息
	 * @param externalvo
	 * @param fieldName
	 */
	public static Class getExternalFieldClass(Object externalvo, String fieldName) throws SwapException
	{
		Class attrClass = null;
		try
		{
			PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(externalvo, fieldName);
			attrClass = pd.getPropertyType();
		}
		catch (Exception e)
		{
			throw new SwapException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0216")/*@res "无法取得外部VO的属性信息,属性:"*/+ fieldName);
		}
		return attrClass;
	}

	/**
	 * 将外部的类型转换为内部的类型
	 * @param value
	 * @return
	 */
	public static Object valueOfExternalObj(Field field, Object fieldvalue) throws SwapException
	{
		if (fieldvalue == null)
			return null;
		try
		{
			//数组或者list类型
			if (fieldvalue instanceof Object[])
			{
				Object[] objarr = (Object[]) fieldvalue;
				for (int i = 0; i < objarr.length; i++)
				{
					objarr[i] = valueOfExternalObj(field, objarr[i]);
				}
				return objarr;
			}
			else if (fieldvalue instanceof Collection)
			{
				List<Object> objcol = (List<Object>) fieldvalue;
				int size = objcol.size();
				for (int i = 0; i < size; i++)
				{
					objcol.set(i, valueOfExternalObj(field, objcol.get(i)));
				}
				return objcol;
			}
			else
			{
				if (field.getType().equals(IFieldDataType.STR_BOOL) || field.getType().equalsIgnoreCase(IFieldDataType.STR_UFBOOL))
				{
					if (fieldvalue instanceof UFBoolean)
						return fieldvalue;
					return Boolean.valueOf(fieldvalue.toString()) ? UFBoolean.TRUE : UFBoolean.FALSE;
				}
				else if (field.getType().equals(IFieldDataType.STR_INT))
				{
					if (fieldvalue instanceof Integer)
						return fieldvalue;
					return Integer.valueOf(fieldvalue.toString());
				}
				else if (field.getType().equals(IFieldDataType.STR_DOUBLE) || field.getType().equals(IFieldDataType.STR_UFDOUBLE))
				{
					if (fieldvalue instanceof UFDouble)
						return fieldvalue;
					else if (fieldvalue instanceof String)
						return new UFDouble((String) fieldvalue);
					else if (fieldvalue instanceof Double)
						return new UFDouble((Double) fieldvalue);
					else if (fieldvalue instanceof BigDecimal)
						return new UFDouble((BigDecimal) fieldvalue);
				}
				else if (field.getType().equals(IFieldDataType.STR_DATE) || field.getType().equalsIgnoreCase(IFieldDataType.STR_UFDATE))
				{
					if (fieldvalue instanceof UFDate)
						return fieldvalue;
					if (isAllowDate(fieldvalue.toString()))
						return UFDate.getDate(fieldvalue.toString());
					else
						throw new SwapException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0217")/*@res "日期格式不合法,日期字符串:"*/
								+ fieldvalue.toString());
				}
				else if (field.getType().equals(IFieldDataType.STR_UFLITERALDATE))
				{
					if (fieldvalue instanceof UFLiteralDate)
						return fieldvalue;
					if (isAllowDate(fieldvalue.toString()))
						return UFLiteralDate.getDate(fieldvalue.toString());
					else
						throw new SwapException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0217")/*@res "日期格式不合法,日期字符串:"*/
								+ fieldvalue.toString());
				}
				else if (field.getType().equals(IFieldDataType.STR_STRING))
				{
					return fieldvalue.toString();
				}
				else if (field.getType().equals(IFieldDataType.STR_TIME))
				{
					if (fieldvalue instanceof UFDateTime)
						return fieldvalue;
					return new UFDateTime(fieldvalue.toString());
				}
			}
		}
		catch (NumberFormatException e)
		{
			throw new SwapException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000527", null,
					new String[] { field.getName() })/*
														 * @res "字段" +
														 * field.getName() +
														 * "类型转换错误:"
														 */
					+ e.getMessage());
		}
		return fieldvalue;
	}

	/**
	 * 如果字符串能转换成日期返回true。
	 *
	 * @return boolean
	 * @param strDate
	 *            java.lang.String
	 */
	public static boolean isAllowDate(String strDate)
	{
		if (strDate == null || strDate.trim().length() == 0)
			return true;
		if (strDate.trim().length() != 10)
			return false;
		for (int i = 0; i < 10; i++)
		{
			char c = strDate.trim().charAt(i);
			if (i == 4 || i == 7)
			{
				if (c != '-')
					return false;
			}
			else if (c < '0' || c > '9')
				return false;
		}
		int year = Integer.parseInt(strDate.trim().substring(0, 4));
		int month = Integer.parseInt(strDate.trim().substring(5, 7));
		if (month < 1 || month > 12)
			return false;
		int day = Integer.parseInt(strDate.trim().substring(8, 10));
		int MONTH_LENGTH[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int LEAP_MONTH_LENGTH[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int daymax = UFDate.isLeapYear(year) ? LEAP_MONTH_LENGTH[month - 1] : MONTH_LENGTH[month - 1];
		if (day < 1 || day > daymax)
			return false;
		return true;
	}

	/**
	 * 将内部的类型转换为外部的类型
	 *
	 * @param value
	 * @return
	 */
	public static Object valueOfInnerObj(Field field, Object fieldvalue, String fullClassName) throws SwapException
	{
		if (fieldvalue == null)
			return null;
		try
		{
			//数组或者list类型
			if (fieldvalue instanceof Object[])
			{
				String elementClassName = fullClassName.substring(2, fullClassName.length() - 1);
				Object[] objarr = (Object[]) fieldvalue;
				for (int i = 0; i < objarr.length; i++)
				{
					objarr[i] = valueOfInnerObj(field, objarr[i], elementClassName);
				}
				return objarr;
			}
			else if (fieldvalue instanceof Collection)
			{
				throw new PfxxRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0218")/*@res "暂时不支持外部字段为collection类型!"*/);
			}
			else
			{
				String lsclassname = fullClassName.toLowerCase();
				if (field.getType().equals(IFieldDataType.STR_BOOL) || field.getType().equalsIgnoreCase(IFieldDataType.STR_UFBOOL))
				{
					if (lsclassname.indexOf("bool") >= 0)
					{
						if (fieldvalue instanceof UFBoolean)
							return ((UFBoolean) fieldvalue).booleanValue();
						else if (fieldvalue instanceof Boolean) //Boolean 或者boolean
							return ((Boolean) fieldvalue).booleanValue();
					}
				}
				else if (field.getType().equals(IFieldDataType.STR_INT))
				{
					if (lsclassname.indexOf("int") >= 0)
					{
						return fieldvalue;
					}
				}
				else if (field.getType().equals(IFieldDataType.STR_DOUBLE) || field.getType().equals(IFieldDataType.STR_UFDOUBLE))
				{
					if (lsclassname.indexOf("double") >= 0)
					{
						if (fieldvalue instanceof UFDouble)
							return ((UFDouble) fieldvalue).doubleValue();
						else if (fieldvalue instanceof BigDecimal)
							return ((BigDecimal) fieldvalue).doubleValue();
					}
					else if (lsclassname.indexOf("bigdecimal") >= 0)
					{
						if (fieldvalue instanceof UFDouble)
							return new BigDecimal(((UFDouble) fieldvalue).toString());
						else if (fieldvalue instanceof BigDecimal)
							return fieldvalue;
					}
				}
				else if (field.getType().equals(IFieldDataType.STR_DATE) || field.getType().equalsIgnoreCase(IFieldDataType.STR_UFDATE))
				{
					if (fieldvalue instanceof UFDate)
						return ((UFDate) fieldvalue).toDate();
				}
				else if (field.getType().equals(IFieldDataType.STR_UFLITERALDATE))
				{
					if (fieldvalue instanceof UFLiteralDate)
						return ((UFLiteralDate) fieldvalue).toDate();
				}
				else if (field.getType().equals(IFieldDataType.STR_STRING))
				{
					return fieldvalue.toString();
				}
				else if (field.getType().equals(IFieldDataType.STR_TIME))
				{
					if (fieldvalue instanceof UFDateTime)
					{
						try
						{
							return SIMPLE_DATE_FORMAT.parse(((UFDateTime) fieldvalue).toString());
						}
						catch (ParseException e)
						{
							Logger.error("", e);
						}
					}
				}
			}
		}
		catch (NumberFormatException e)
		{
			throw new SwapException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000527", null,
					new String[] { field.getName() })/*
														 * @res "字段" +
														 * field.getName() +
														 * "类型转换错误:"
														 */
					+ e.getMessage());
		}
		throw new SwapException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0219")/*@res "字段:"*/+ field.getName()
				+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0220")/*@res "类型转换错误!转换目标类:"*/+ fullClassName
				+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0221")/*@res ",原始类型："*/+ fieldvalue.getClass().getName());
	}

	/**
	 * 根据字段的前缀决定如何写入XML元素
	 * @param newEle
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void appendFieldToElement(Element parentEle, String fieldName, String fieldValue)
	{
		if (fieldName.startsWith("@"))
		{
			fieldName = fieldName.substring(fieldName.indexOf("@") + 1);
			parentEle.setAttribute(fieldName, fieldValue);
		}
		else if (fieldName.startsWith("$"))
		{
			parentEle.appendChild(parentEle.getOwnerDocument().createTextNode(fieldValue));
		}
		else
		{
			XMLUtil.addChildElement(parentEle, fieldName, fieldValue);
		}
	}

	/**
	 * 根据字段的前缀决定如何写入XML元素,并且在所新增的字段前增加注释，导出样本用
	 * @param newEle
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void appendFieldToElement(Element parentEle, String fieldName, String fieldValue, Field field)
	{
		if (fieldName.startsWith("@"))
		{
			fieldName = fieldName.substring(fieldName.indexOf("@") + 1);
			parentEle.setAttribute(fieldName, fieldValue);
			//增加属性注释
			Node commentNode = parentEle.getOwnerDocument().createComment(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0222")/*@res "属性:"*/+ field.getCommentDesc());
			parentEle.getParentNode().insertBefore(commentNode, parentEle);
		}
		else if (fieldName.startsWith("$"))
		{
			parentEle.appendChild(parentEle.getOwnerDocument().createTextNode(fieldValue));
			//增加文本值注释
			Node commentNode = parentEle.getOwnerDocument().createComment(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0223")/*@res "文本值:"*/+ field.getCommentDesc());
			parentEle.getParentNode().insertBefore(commentNode, parentEle);
		}
		else
		{
			Document doc = parentEle.getOwnerDocument();
			Element sub_element = XMLUtil.createLeafElement(doc, fieldName, fieldValue);
			parentEle.appendChild(sub_element);
			//增加文本值注释
			Node commentNode = sub_element.getOwnerDocument().createComment(field.getCommentDesc());
			sub_element.getParentNode().insertBefore(commentNode, sub_element);
		}
	}

	/**
	 * 取子元素的值
	 * @param parentEle
	 * @param fieldName
	 * @return
	 */
	public static String getFieldValueFromParentEle(Element parentEle, String fieldName)
	{
		String fieldValue = null;
		if (fieldName.startsWith("@"))
		{
			fieldName = fieldName.substring(fieldName.indexOf("@") + 1);
			fieldValue = parentEle.getAttribute(fieldName);
		}
		else
		{
			fieldValue = XMLUtil.getFirstChildElementText(parentEle, fieldName);
			// 为增加查询功能需要把属性也放置到翻译后的文档中
		}
		return fieldValue;
	}

	/**
	 * 取得复杂字段的值。
	 * @param curvo
	 * @param fieldname
	 * @return
	 */
	public static Object getComplexProperty(Object curvo, String fieldname)
	{
		Object fieldvalue = null;
		try
		{
			fieldname = filterFieldName(fieldname);
			if (curvo instanceof SuperVO)
			{
				SuperVO svo = (SuperVO) curvo;
				if (fieldname.equals(svo.getPKFieldName()))
				{
					fieldvalue = svo.getPrimaryKey();
				}
				else if (curvo instanceof CircularlyAccessibleValueObject)
				{
					//SCM的VO数据基本上都是CircularlyAccessibleValueObject，并且都没有get,set方法
					CircularlyAccessibleValueObject cirvo = (CircularlyAccessibleValueObject) curvo;
					fieldvalue = cirvo.getAttributeValue(fieldname);
				}
			}
			else if (curvo instanceof CircularlyAccessibleValueObject)
			{
				//SCM的VO数据基本上都是CircularlyAccessibleValueObject，并且都没有get,set方法
				CircularlyAccessibleValueObject cirvo = (CircularlyAccessibleValueObject) curvo;
				fieldvalue = cirvo.getAttributeValue(fieldname);
			}
			if (fieldvalue == null)
				fieldvalue = BeanHelper.getProperty(curvo, fieldname.toLowerCase());
		}
		catch (Exception e)
		{
			Logger.warn("VO中没有字段:" + fieldname + "，可能是衍生字段." + e.getMessage());
			fieldvalue = null;
		}
		return fieldvalue;
	}

	/**
	 * 设置简单字段的值。
	 * @param fieldname
	 * @param fieldvalue
	 * @param curvo
	 * @throws SwapException
	 */
	public static void setVOProperty(String fieldname, Object fieldvalue, Object curvo) throws SwapException
	{
		//过滤字段名
		fieldname = filterFieldName(fieldname);
		try
		{
			boolean bset = false;
			if (curvo instanceof SuperVO)
			{
				SuperVO svo = (SuperVO) curvo;
				if (fieldname.equals(svo.getPKFieldName()))
				{
					if (fieldvalue != null)
						svo.setPrimaryKey(fieldvalue.toString());
					else
						svo.setPrimaryKey(null);
					bset = true;
				}
			}
			else if (curvo instanceof CircularlyAccessibleValueObject)
			{
				//SCM的VO数据基本上都是CircularlyAccessibleValueObject，并且都没有get,set方法
				CircularlyAccessibleValueObject cirvo = (CircularlyAccessibleValueObject) curvo;
				// 有set方法优先设置到set方法中
				if (BeanHelper.getSetMethod(curvo, fieldname.toLowerCase()) != null)
				{
					BeanHelper.setProperty(curvo, fieldname.toLowerCase(), fieldvalue);
				}
				cirvo.setAttributeValue(fieldname, fieldvalue);
				if (cirvo.getAttributeValue(fieldname) != null)
				{
					bset = true;
				}
			}
			if (!bset)
			{
				if (BeanHelper.getSetMethod(curvo, fieldname.toLowerCase()) != null)
				{
					BeanHelper.setProperty(curvo, fieldname.toLowerCase(), fieldvalue);
				}
				if (curvo instanceof SuperVO){
					((SuperVO)curvo).setAttributeValue(fieldname,fieldvalue);
				}
			}
		}
		catch (NumberFormatException e)
		{
			Logger.warn("给VO字段:" + fieldname + "赋值时发现类型不匹配:" + e.getMessage());
		}
		catch (Exception e)
		{
			Logger.warn("给VO字段:" + fieldname + "赋值时出现异常:" + e.getMessage());
			Logger.error("给VO字段:" + fieldname + "赋值时出现异常:" + e.getMessage());
		}
	}

	/** 对字段名进行过滤     */
	private static String filterFieldName(String fieldName)
	{
		if (fieldName.startsWith("@"))
		{
			fieldName = fieldName.substring(1);
		}
		else if (fieldName.startsWith("$"))
		{
			fieldName = fieldName.substring(1);
		}
		return fieldName;
	}

	/**
	 * 从父元素element中取得field的NC名称指定子元素或者属性的值。
	 *
	 * @param field
	 * @param element
	 * @return 返回NC相应的数据类型值
	 */
	public static Object getFieldValue(Field field, Element element) throws SwapException
	{
		String matchFieldName = getFieldName(field);
		String fieldvalue = getFieldValueFromSourceDoc(element, matchFieldName);
		if (fieldvalue == null || fieldvalue.length() == 0)
			return null;
		return getFieldValue(fieldvalue, field);
	}

	/**
	 * 根据名称的差异按照不同方式取值(NC30约定格式)
	 * @param source
	 * @param fieldName
	 * @return
	 */
	private static String getFieldValueFromSourceDoc(Element source, String fieldName)
	{
		String fieldValue = null;
		if (fieldName.startsWith("@"))
		{
			fieldName = fieldName.substring(fieldName.indexOf("@") + 1);
			fieldValue = source.getAttribute(fieldName);
		}
		else if (fieldName.startsWith("$"))
		{
			fieldValue = XMLUtil.getChildText(source);
		}
		else
		{
			fieldValue = XMLUtil.getFirstChildElementText(source, fieldName);
		}
		if (fieldValue != null && fieldValue.trim().length() == 0)
			fieldValue = null;
		return fieldValue;
	}

	/**
	 * 将字符串形式的字段值转换为字段定义中规定类型的值。
	 * @param fieldvalue
	 * @param field
	 * @return
	 * @throws SwapException
	 */
	public static Object getFieldValue(String fieldvalue, Field field) throws SwapException
	{
		Object resobj = null;
		try
		{
			if (field.getType().equals(IFieldDataType.STR_BOOL) || field.getType().equalsIgnoreCase(IFieldDataType.STR_UFBOOL))
			{
				resobj = UFBoolean.valueOf(fieldvalue);
			}
			else if (field.getType().equals(IFieldDataType.STR_INT))
			{
				resobj = new Integer(fieldvalue.trim());
			}
			else if (field.getType().equals(IFieldDataType.STR_DOUBLE) || field.getType().equals(IFieldDataType.STR_UFDOUBLE))
			{
				resobj = new UFDouble(fieldvalue);
			}
			else if (field.getType().equals(IFieldDataType.STR_DATE) || field.getType().equalsIgnoreCase(IFieldDataType.STR_UFDATE))
			{
			
				resobj = new UFDate(fieldvalue);
				
			}
			else if (field.getType().equals(IFieldDataType.STR_STRING))
			{
				resobj = fieldvalue;
			}
			else if (field.getType().equals(IFieldDataType.STR_TIME))
			{
				resobj = new UFDateTime(fieldvalue.trim());
			}
			else if (field.getType().equals(IFieldDataType.STR_UFLITERALDATE))
			{
				resobj = UFLiteralDate.getDate(fieldvalue.trim());
			}	
			else
			{
				resobj = fieldvalue;
			}
		}
		catch (NumberFormatException e)
		{
			throw new SwapException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000527", null,
					new String[] { field.getName() })/*@res "字段" + field.getName() + "类型转换错误:"*/
					+ e.getMessage());
		}
		return resobj;
	}

	/**
	 * 从字段里取得字段名
	 *
	 * @param field
	 * @return
	 */
	public static String getFieldName(Field field) throws SwapException
	{
		String FieldName = field.getName();
		if (FieldName == null || FieldName.length() == 0)
			throw new SwapException("字段的Name不能为空!字段名:"/*@res "字段的MatchTag不能为空!字段名:"*/
					+ field.getName());
		return FieldName;
	}

	/**
	 * 根据名称获得类，区分数组类型。
	 * @param curclassname
	 * @return
	 * @throws SwapException
	 */
	public static Class getClassFromString(String curclassname) throws SwapException
	{
		Class curfieldclass = null;
		try
		{
			if (curclassname == null || curclassname.trim().length() == 0)
				return null;
			int kpos = curclassname.indexOf("[");
			if (kpos > 0)
			{
				String nestvoclass = curclassname.substring(0, kpos);
				curfieldclass = Class.forName(nestvoclass);
			}
			else
				curfieldclass = Class.forName(curclassname);
		}
		catch (ClassNotFoundException e)
		{
			throw new SwapException("Class not found:" + curclassname);
		}
		return curfieldclass;
	}

	/**
	 * 判断classname是否为数组。
	 * @return
	 */
	public static boolean isClassNameBeArray(String curclassname)
	{
		//取得类型,并且判断是数组还是简单元素
		if (curclassname == null || curclassname.trim().length() == 0)
			return false;
		int kpos = curclassname.indexOf("[");
		if (kpos >= 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 根据记录定义或者相应复杂字段定义，判断该记录是否描述数组类型。
	 * @param refRecord
	 * @param field
	 * @return
	 */
	public static boolean isFieldArray(Record refRecord, Field field)
	{
		String classname = refRecord.getClassname();
		return isClassNameBeArray(classname);
	}

	/**
	 * 取得某些类型的记录定义的非空NC系统标签。注：这种类型的记录定义的NC系统标签不能为空。
	 * @param refRecord
	 * @return
	 * @throws SwapException
	 */
	public static String getNotNullRecordMatchTag(Record refRecord) throws SwapException
	{
		String matchtag = refRecord.getMatchTag();
		if (matchtag == null || matchtag.length() == 0)
		{
			throw new SwapException("This Record Name in NC can not be null,Record:" + refRecord.getName());
		}
		return matchtag;
	}

	/**
	 * 取得某些类型（VO数组和集合）的记录定义的非空的NC系统的实体元素标签名。注：这种类型的记录定义的NC系统实体元素标签名不能为空。
	 * @param record
	 * @return
	 * @throws SwapException
	 */
	public static String getNotNullRecordEntityMatchedTag(Record record) throws SwapException
	{
		String entityMatchedTag = record.getEntityMatchedTag();
		if (entityMatchedTag != null && entityMatchedTag.length() != 0)
		{
			return entityMatchedTag;
		}
		// 以下为兼容NC50之前的单据定义
		entityMatchedTag = record.getMatchTag();
		if (entityMatchedTag != null && entityMatchedTag.length() != 0)
		{
			return entityMatchedTag;
		}
		throw new SwapException("Can not find Record's entityMatchedTag, which can not be null, Record:" + record.getName());
	}

	/**
	 * 取得复杂字段的数据类型,兼容V31,所以field也的传入
	 * @param refRecord
	 * @param field
	 * @return
	 */
	public static Class getComplexFieldClass(Record refRecord, Field field) throws SwapException
	{
		String classname = refRecord.getClassname();
		return getClassFromString(classname);
	}

	/**
	 * 从当前字段节点(元素或者是属性)中取得字段值。
	 * @param field
	 * @param fieldNode
	 * @return
	 * @throws SwapException
	 */
	public static Object getFieldValueFromFieldNode(Field field, Object fieldValue) throws SwapException
	{
		String fieldvalue = (String)fieldValue;
		if (fieldvalue == null || fieldvalue.length() == 0)
			return null;
		return getFieldValue(fieldvalue, field);
	}

	/**
	 * 从字段里取得外系统名称
	 * @param field
	 * @return
	 * @throws SwapException
	 */
	public static String getFieldExName(Field field) throws SwapException
	{
		String exFieldTagName = field.getName();
		if (exFieldTagName == null || exFieldTagName.length() == 0)
		{
			throw new SwapException("The Field Name in NC can not be null, Field:" + field.getDesc());
		}
		return exFieldTagName;
	}

	/**
	 * 取得某些类型的记录定义的非空外系统标签名。注：这种类型的记录定义的外系统标签不能为空。
	 * @param record
	 * @return
	 * @throws SwapException
	 */
	public static String getNotNullRecordExTag(Record record) throws SwapException
	{
		String exTag = record.getExTag();
		if (exTag == null || exTag.length() == 0)
		{
			throw new SwapException("This Record name in Exsystem can not be null, Record:" + record.getName());
		}
		return exTag;
	}

	/**
	 * 取得某些类型(VO数组和集合)的记录定义的非空的实体元素外系统标签名。注：因为在翻译转换这种类型记录时必须，所以这种类型的
	 * 记录定义的实体元素外系统标签名不能为空。
	 * @param record
	 * @return
	 * @throws SwapException
	 */
	public static String getNotNullRecordEntityExtag(Record record) throws SwapException
	{
		String entityExtag = record.getEntityExtag();
		if (entityExtag != null && entityExtag.length() != 0)
		{
			return entityExtag;
		}
		// 以下代码为兼容5.0以下版本单据记录定义
		entityExtag = record.getExTag();
		if (entityExtag != null && entityExtag.length() != 0)
		{
			return entityExtag;
		}
		throw new SwapException("Can not find Record's entityExtag, which can not be null, Record:" + record.getName());
	}

	/**
	 * 为当前节点创建属性节点。
	 * @param exTag
	 * @return
	 */
	public static Node createAttrNode(Element currEle, String tag)
	{
		Attr fieldAttr = currEle.getOwnerDocument().createAttribute(tag);
		currEle.setAttributeNode(fieldAttr);
		return fieldAttr;
	}

	/**
	 * 为当前节点创建子元素节点。如果only为true，并且已经存在相同名称的子元素，则返回已经存在的子元素。
	 * @param currEle 当前节点
	 * @param tag 子元素标签
	 * @param only 是否唯一
	 * @return 新建子元素节点
	 */
	public static Element createChildElement(Node currEle, String tag, boolean only)
	{
		Element lastEle = XMLUtil.getFirstChildElement(currEle, tag);
		if (lastEle != null && only)
		{
			return lastEle;
		}
		else
		{
			lastEle = currEle.getOwnerDocument().createElement(tag);
			currEle.appendChild(lastEle);
		}
		return lastEle;
	}

	/**
	 * 判断节点candiAncester是否是节点child的祖先节点，或者candiAncester与child是相同节点。
	 */
	public static boolean isAncesterOrSelfOfNode(Node candiAncester, Node child)
	{
		//寻找祖先的孩子
		Node nodeForParent = child;
		while (nodeForParent != null)
		{
			if (nodeForParent == candiAncester)
				return true;
			nodeForParent = nodeForParent.getParentNode();
		}
		return false;
	}

	/**
	 * 判断节点candiAncester是否是节点child的祖先节点。
	 */
	public static boolean isAncesterOfNode(Node candiAncester, Node child)
	{
		if (child == null)
		{
			return false;
		}
		Node nodeForParent = child.getParentNode();
		while (nodeForParent != null)
		{
			if (nodeForParent == candiAncester)
			{
				return true;
			}
			nodeForParent = nodeForParent.getParentNode();
		}
		return false;
	}
	

}