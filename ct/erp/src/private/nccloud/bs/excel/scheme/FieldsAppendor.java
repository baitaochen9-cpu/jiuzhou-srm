/*
 * Created on 2006-4-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nccloud.bs.excel.scheme;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;

import nc.vo.pfxx.util.PfxxUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Field;
import nccloud.vo.excel.scheme.IFieldDataType;
import nccloud.vo.excel.scheme.IRecordType;
import nccloud.vo.excel.scheme.Record;
import nccloud.vo.excel.util.VOXMLUtils;

/**
 * 记录表定义的字段填充器。
 * 
 * @author ljian
 */
public class FieldsAppendor {

	/** 类名 */
	private String classname = null;

	private BillDefination billDefination;
	/** 记录元素 */
	private Record record = null;

	/** Constructor */
	public FieldsAppendor(BillDefination billDefination, Record record, String classname) {
		this.billDefination = billDefination;
		this.record = record;
		this.classname = classname;
	}

	public void appendRecordFields() throws BusinessException {

		// 得到指定VO类的所有成员变量的属性描述
		PropertyDescriptor[] properties = PropertyUtils
				.getPropertyDescriptors(VOXMLUtils.getClassFromString(classname));

		// 属性描述数组过滤，将没有getter/setter方法以及超类的属性描述去掉
		properties = filterProperties(classname, properties);

		if (properties != null) {
			for (PropertyDescriptor prop : properties) {
				// 从属性描述中获得该属性的类名和类对象等信息
				Class fieldclass = prop.getPropertyType();
				String childClassName = fieldclass.getName();

				// 数组情形
				if (fieldclass.isArray()) {
					// 取得数组元素的类对象和类名
					fieldclass = fieldclass.getComponentType();
					childClassName = fieldclass.getName();
					// 该字段是VO数组
					if (ValueObject.class.isAssignableFrom(fieldclass)) {
						dealVOArrayField(prop, childClassName);
					}
					// 该字段是简单数组，暂不考虑其余情形，如数组元素是数组或集合
					else {
						// dealSimpleArrayField(prop, classname);
					}
				}
				// 该字段是容器类型
				else if (Collection.class.isAssignableFrom(fieldclass)) {
					dealCollectionField(prop, childClassName);
				}
				// 该字段是VO复杂字段
				else if (ValueObject.class.isAssignableFrom(fieldclass)) {
					dealValueObjectField(prop, childClassName);
				}
				// 普通字段
				else {
					// 根据某个字段的属性描述填充一个字段对象
					Field field = fillFieldWithProperty(prop);

					// 根据field实例生成字段元素，并挂接到表定义元素下
					record.getFields().add(field);
				}
			}
		}
	}

	/** VO数组类型的复杂字段，在父记录定义中写一个字段定义，并新生成一个记录定义来描述这个复杂字段 */
	private void dealVOArrayField(PropertyDescriptor prop, String classname) throws BusinessException {

		// 在原记录元素中写入一个站位字段
		Field field = fillFieldWithProperty(prop);
		field.setType(IFieldDataType.STR_REFERENCE); // 设置站位字段的类型
		field.setRefItem(prop.getName());
		record.getFields().add(field);

		// 在新产生的校验文档中生成该复杂字段对应VO的record记录元素
		Record newRecord = new Record();
		newRecord.setName(field.getName());
		newRecord.setDisplayName(field.getName());
		newRecord.setType(IRecordType.EMBED);
		newRecord.setRecordtype(IRecordType.DATA_TYPE_VOS);
		newRecord.setClassname(classname + "[]");
		newRecord.setEntityExtag("item");
		newRecord.setEntityMatchedTag("item");
		newRecord.setBillDefination(billDefination);
		billDefination.getRecords().add(newRecord);

		// 递归调用，根据VO类名往记录里面填字段
		FieldsAppendor appendor = new FieldsAppendor(billDefination, newRecord, classname);
		appendor.appendRecordFields();
	}

	/** 简单数组类型的复杂字段，在父记录定义中写一个字段定义，并新生成一个记录定义来描述这个复杂字段 */
	// private void dealSimpleArrayField(PropertyDescriptor prop, String classname) {
	//
	// // 在原记录元素中写入一个站位字段
	// Field field = SchemeCreateUtils.fillFieldWithProperty(prop, true);
	// field.setType("ref(item)");
	// String element = field.getName() + "Element"; // 数组特殊，需要设置位置属性
	// field.setPosition(element);
	//
	// // 在新产生的校验文档中生成该复杂字段对应的record记录元素
	// RecordInfo recordInfo = new RecordInfo();
	// recordInfo.setRecordname(field.getName());
	// recordInfo.setClassname(classname + "[]");
	// Element embedRecordEle = SchemeCreateUtils.createSimpleArrayEmbedRecord(newDoc, recordInfo);
	//
	// // 默认向该记录元素下添加数组元素类型的字段
	// Field fieldDef = new Field();
	// fieldDef.setName(element);
	// fieldDef.setDesc("");
	// fieldDef.setMatchTag(element);
	// fieldDef.setType(classname);
	// fieldDef.setMaxLength(30);
	// fieldDef.setNullAllowed(true);
	// SchemeCreateUtils.addSimpleField(newDoc, embedRecordEle, fieldDef);
	// }

	/** 集合类型复杂字段，在父记录定义中写一个字段定义，并新生成一个记录定义来描述这个复杂字段，但不往这个新记录中追加字段 */
	private void dealCollectionField(PropertyDescriptor prop, String classname) {

		// 在原记录元素中写入一个占位字段
		Field field = fillFieldWithProperty(prop);
		field.setType(IFieldDataType.STR_REFERENCE);
		field.setRefItem(prop.getName());
		field.setPosition(field.getName() + "Item"); // Collection类型特殊，需要设置位置属性
		record.getFields().add(field);

		// 在新产生的校验文档中生成该容器字段对应的record记录元素，这是个只有属性的空元素
		// 这个方法还会创建一个容器内部元素对应的实体记录
		// 由于事先不能确认这个实体类型，所以默认是一个字符串简单类型
		Record newRecord = new Record();
		newRecord.setName(field.getName());
		newRecord.setDisplayName(field.getName());
		newRecord.setType(IRecordType.EMBED);
		newRecord.setRecordtype(IRecordType.DATA_TYPE_COLLECTION);
		newRecord.setClassname(classname);
		newRecord.setInnerRecordtype(IRecordType.DATA_TYPE_BASIC);
		newRecord.setBillDefination(billDefination);
		billDefination.getRecords().add(newRecord);

		// 默认加一个字段
		Field fieldDef = new Field();
		fieldDef.setName("innerfield");
		fieldDef.setDesc("innerfield");
		fieldDef.setMatchTag("innerfield");
		fieldDef.setType(IFieldDataType.STR_STRING);
		fieldDef.setMaxLength(30);
		fieldDef.setNullAllowed(true);
		newRecord.getFields().add(fieldDef);
	}

	/** VO复杂字段，在父记录定义中写一个字段定义，并新生成一个记录定义来描述这个复杂字段 */
	private void dealValueObjectField(PropertyDescriptor prop, String classname) throws BusinessException {
		// 在原记录元素中写入一个站位字段
		Field field = fillFieldWithProperty(prop);
		field.setType(IFieldDataType.STR_REFERENCE);
		field.setRefItem(prop.getName());
		field.setPosition("");
		record.getFields().add(field);

		// 在新产生的校验文档中生成该复杂字段对应VO的record记录元素
		Record newRecord = new Record();
		newRecord.setName(field.getName());
		newRecord.setDisplayName(field.getName());
		newRecord.setType(IRecordType.EMBED);
		newRecord.setRecordtype(IRecordType.DATA_TYPE_VO);
		newRecord.setClassname(classname);
		newRecord.setBillDefination(billDefination);
		billDefination.getRecords().add(newRecord);

		FieldsAppendor appendor = new FieldsAppendor(billDefination, newRecord, classname);
		appendor.appendRecordFields();

	}

	/**
	 * 过滤掉没有getter/setter方法的属性，对于从超类继承的属性，即使它有getter/setter方法，也需要删掉
	 * 
	 * @param properties
	 * @return
	 */
	protected PropertyDescriptor[] filterProperties(String voclassname, PropertyDescriptor[] properties) {
		ArrayList<PropertyDescriptor> props = new ArrayList<PropertyDescriptor>();
		PropertyDescriptor[] propArray = null;
		for (PropertyDescriptor prop : properties) {
			// 过滤条件：存在读/写方法且不是在超类中声明的属性
			if (prop.getReadMethod() != null && prop.getWriteMethod() != null) {
				//2011-05-12 modify by zhaoxub 显示非抽象超类的属性
//				if ((prop.getReadMethod().getDeclaringClass().getName().equals(voclassname))
//						&& (prop.getWriteMethod().getDeclaringClass().getName().equals(voclassname))) {
//					if (!prop.getName().equals("dr") && !prop.getName().equals("ts"))
//						props.add(prop);
//				}
				if (Modifier.ABSTRACT != prop.getReadMethod().getDeclaringClass().getModifiers()) {
					if ((prop.getReadMethod().getDeclaringClass().getName().equals(prop.getWriteMethod()
							.getDeclaringClass().getName()))) {
						if (!prop.getName().equals("dr") && !prop.getName().equals("ts"))
							props.add(prop);
					}
				}

			}
		}
		if (props.size() > 0) {
			propArray = new PropertyDescriptor[props.size()];
			props.toArray(propArray);
			return propArray;
		}
		return null;
	}

	private Field fillFieldWithProperty(PropertyDescriptor prop) {
		Field field = new Field();
		field.setName(prop.getName());
		field.setDesc(prop.getShortDescription());
		field.setType(PfxxUtils.getDatatype(prop.getPropertyType().getName()));
		field.setNullAllowed(true);
		field.setMaxLength(64);
		field.setMatchTag(prop.getName());
		field.setNeedexport(true);
		field.setRecord(record);
		return field;
	}

}
