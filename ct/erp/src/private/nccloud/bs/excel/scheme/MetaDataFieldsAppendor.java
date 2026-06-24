package nccloud.bs.excel.scheme;

import java.util.List;

import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.impl.Attribute;
import nc.md.model.type.ICollectionType;
import nc.md.model.type.IEnumType;
import nc.md.model.type.IType;
import nc.md.util.MDUtil;
import nc.vo.pub.BusinessException;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Field;
import nccloud.vo.excel.scheme.IFieldDataType;
import nccloud.vo.excel.scheme.IRecordType;
import nccloud.vo.excel.scheme.Record;

/**
 * 记录表定义的字段填充器。
 *
 * @author zhaoxub
 */
public class MetaDataFieldsAppendor {

	private BillDefination billDefination;
	/** 记录元素 */
	private Record record = null;

	private IBean metaDataBean = null;

	public MetaDataFieldsAppendor(BillDefination billDefination, Record record, IBean metaDataBean) {
		this.billDefination = billDefination;
		this.record = record;
		this.metaDataBean = metaDataBean;
	}

	public void appendRecordFields() throws BusinessException {

		if (metaDataBean != null) {
			record.setMeteDataID(metaDataBean.getID());
			record.setDisplayName(metaDataBean.getDisplayName());
			List<IAttribute> attributes = metaDataBean.getAttributesOfModel();
			for (IAttribute attribute : attributes) {

				IType dataType = attribute.getDataType();
				// 取得属性的值
				// 基础类型,枚举类型,引用类型
				if (MDUtil.isSimpleType(dataType)) {
					// 根据某个字段的属性描述填充一个字段对象
					Field field = fillFieldWithProperty(attribute);

					// 根据field实例生成字段元素，并挂接到表定义元素下
					record.getFields().add(field);
				}
				// Bean类型
				else if (MDUtil.isMDBean(dataType)) {
					dealBeanField(attribute, record);
				}
				// 集合类型
				else if (MDUtil.isCollectionType(dataType)) {
					ICollectionType collectionType = (ICollectionType) dataType;
					// 取得集合中的具体类型
					IType elementType = collectionType.getElementType();
					if (collectionType.getCollectionStyle() == IType.STYLE_ARRAY) {
						if (MDUtil.isSimpleType(elementType)) {
							throw new UnsupportedOperationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx","0pfxx0100")/*@res "暂时不支持简单类型的数组。"*/);
						} else if (MDUtil.isMDBean(elementType)) {
							dealBeanArrayField(attribute, record);
						}
					} else if (collectionType.getCollectionStyle() == IType.STYLE_LIST) {
						if (MDUtil.isSimpleType(elementType)) {
							throw new UnsupportedOperationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx","0pfxx0101")/*@res "暂时不支持简单元素的集合。"*/);
						} else if (MDUtil.isMDBean(elementType)) {
							dealBeanCollectionField(attribute, record);
						}
					}
				}

			}
		}
	}

	/** VO复杂字段，在父记录定义中写一个字段定义，并新生成一个记录定义来描述这个复杂字段 */
	private void dealBeanField(IAttribute attribute, Record record) throws BusinessException {
		// 在原记录元素中写入一个站位字段
		Field field = fillFieldWithProperty(attribute);
		field.setType(IFieldDataType.STR_REFERENCE);
		field.setRefItem(attribute.getAssociation().getEndBean().getID());
		field.setPosition("");
		record.getFields().add(field);

		// 在新产生的校验文档中生成该复杂字段对应VO的record记录元素
		Record newRecord = new Record();
		newRecord.setName(field.getName());
		newRecord.setType(IRecordType.EMBED);
		newRecord.setRecordtype(IRecordType.DATA_TYPE_VO);
		newRecord.setClassname(attribute.getAssociation().getEndBean().getFullClassName());
		newRecord.setBillDefination(billDefination);
		billDefination.getRecords().add(newRecord);

		MetaDataFieldsAppendor appendor = new MetaDataFieldsAppendor(billDefination, newRecord, attribute
				.getAssociation().getEndBean());
		appendor.appendRecordFields();
	}

	/** VO数组类型的复杂字段，在父记录定义中写一个字段定义，并新生成一个记录定义来描述这个复杂字段 */
	private void dealBeanArrayField(IAttribute attribute, Record record) throws BusinessException {

		// 在原记录元素中写入一个站位字段
		Field field = fillFieldWithProperty(attribute);
		field.setType(IFieldDataType.STR_REFERENCE); // 设置站位字段的类型
		field.setRefItem(attribute.getAssociation().getEndBean().getID());
		record.getFields().add(field);

		// 在新产生的校验文档中生成该复杂字段对应VO的record记录元素
		Record newRecord = new Record();
		newRecord.setName(field.getName());
		newRecord.setType(IRecordType.EMBED);
		newRecord.setRecordtype(IRecordType.DATA_TYPE_VOS);
		newRecord.setClassname(attribute.getAssociation().getEndBean().getFullClassName() + "[]");
		newRecord.setEntityExtag("item");
		newRecord.setEntityMatchedTag("item");
		newRecord.setBillDefination(billDefination);
		billDefination.getRecords().add(newRecord);

		// 递归调用，根据VO类名往记录里面填字段
		MetaDataFieldsAppendor appendor = new MetaDataFieldsAppendor(billDefination, newRecord, attribute
				.getAssociation().getEndBean());
		appendor.appendRecordFields();
	}

	// /** 简单数组类型的复杂字段，在父记录定义中写一个字段定义，并新生成一个记录定义来描述这个复杂字段 */
	// private void dealSimpleArrayField(IAttribute attribute, Document ownerDocument) {
	//
	// // 在原记录元素中写入一个站位字段
	// Field field = fillFieldWithProperty(attribute);
	// field.setType("ref(item)");
	// String element = field.getName() + "Element"; // 数组特殊，需要设置位置属性
	// field.setPosition(element);
	// SchemeCreateUtils.addComplexField(ownerDocument, recordEle, field);
	//
	// // 在新产生的校验文档中生成该复杂字段对应的record记录元素
	// Element embedRecordEle = SchemeCreateUtils.createSimpleArrayEmbedRecord(ownerDocument, field.getName(),
	// field.getClassname() + "[]");
	//
	// // 默认向该记录元素下添加数组元素类型的字段
	// Field fieldDef = new Field();
	// fieldDef.setName(element);
	// fieldDef.setDesc("");
	// fieldDef.setMatchTag(element);
	// fieldDef.setType(field.getClassname());
	// fieldDef.setMaxLength(30);
	// fieldDef.setNullAllowed(true);
	// SchemeCreateUtils.addSimpleField(ownerDocument, embedRecordEle, fieldDef);
	// }

	/** 集合类型复杂字段，在父记录定义中写一个字段定义，并新生成一个记录定义来描述这个复杂字段，但不往这个新记录中追加字段 */
	private void dealBeanCollectionField(IAttribute attribute, Record record) {

		// 在原记录元素中写入一个占位字段
		Field field = fillFieldWithProperty(attribute);
		field.setType(IFieldDataType.STR_REFERENCE);
		field.setRefItem(attribute.getAssociation().getEndBean().getID());
		field.setPosition(field.getName() + "Item"); // Collection类型特殊，需要设置位置属性
		record.getFields().add(field);

		// 在新产生的校验文档中生成该容器字段对应的record记录元素，这是个只有属性的空元素
		// 这个方法还会创建一个容器内部元素对应的实体记录
		// 由于事先不能确认这个实体类型，所以默认是一个字符串简单类型
		Record newRecord = new Record();
		newRecord.setName(field.getName());
		newRecord.setType(IRecordType.EMBED);
		newRecord.setRecordtype(IRecordType.DATA_TYPE_COLLECTION);
		newRecord.setClassname(attribute.getAssociation().getEndBean().getFullClassName());
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

	private Field fillFieldWithProperty(IAttribute attribute) {
		Field field = new Field();
		field.setName(attribute.getName());
		field.setDesc(attribute.getDisplayName());
		if (IType.REF == attribute.getDataType().getTypeType()) {
			field.setType(IFieldDataType.STR_STRING);
			field.setBaseDocMetaDataID(((Attribute) attribute).getDataTypeID());
		} else if (IType.ENUM == attribute.getDataType().getTypeType()) {
			field.setType(((IEnumType) attribute.getDataType()).getReturnType().getTagString());
			field.setEnumMetaDataID(attribute.getDataType().getID());
		} else if (IType.MULTILANGUAGE == attribute.getDataType().getTypeType()) {
			field.setType(IFieldDataType.STR_STRING);
		} else {
			field.setType(attribute.getDataType().getName());
		}
		field.setNullAllowed(true);
		field.setMaxLength(attribute.getLength());
		field.setMatchTag(attribute.getName());
		field.setNeedexport(true);
		field.setDefaultValue(attribute.getDefaultValue());
		field.setRecord(record);
		return field;
	}
}