package nccloud.vo.excel.scheme;

/**
 * Created by IntelliJ IDEA.
 * User: cch
 * Date: 2004-12-6
 * Time: 14:47:25
 * To change this template use File | Settings | File Templates.
 */
public interface IFieldDataType {

	public static final String STR_STRING = "String";
	public static final String STR_UFID = "UFID";
	public static final String STR_INT = "Integer";
	public static final String STR_UFDOUBLE = "UFDouble";
	public static final String STR_UFBOOL = "UFBoolean";
	public static final String STR_UFDATE = "UFDate";
	public static final String STR_UFDATETIME = "UFDateTime";
	public static final String STR_UFTIME = "UFTime";
	public static final String STR_UFLITERALDATE = "UFLiteralDate";
	public static final String STR_BIGDECIMAL = "BigDecimal";
	public static final String STR_CUSTOM = "CUSTOM";

	public static final String STR_BOOL = "Boolean";
	public static final String STR_DOUBLE = "Double";
	public static final String STR_DATE = "Date";
	public static final String STR_TIME = "Time";
	
	public static final String STR_MULTILANGTEXT="MULTILANGTEXT";

	public static final String STR_INVALID = "InvalidType";
	public static final String STR_REFERENCE = "ref(item)";
}
