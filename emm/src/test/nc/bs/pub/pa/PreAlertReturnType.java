package nc.bs.pub.pa;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 表示预警插件的返回值PreAlertObject中所承载的返回对象的类型
 * 
 * @author yanke1
 *
 */
public enum PreAlertReturnType {
	
	RETURNNOTHING(0), 	
	
	@Deprecated
	RETURNOBJECT(1), 	 
	@Deprecated
	RETURNFORMATMSG(2), 
	@Deprecated
	RETURNMESSAGE(3), 
	
	RETURNDATASOURCE(4), 
	RETURNMULTILANGTEXT(5),
	RETURN_MULTIPLE_RECEIVER_OBJECT(6);
	
	private int value = -1;
	private PreAlertReturnType(int value) {
		this.value = value;
	}

	public String toString() {
		switch (this) {
		case RETURNMESSAGE:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("prealertres", "PreAlertReturnType-000000")/*插件返回字符串*/;
		case RETURNFORMATMSG:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("prealertres", "PreAlertReturnType-000001")/*插件返回格式化字符串*/;
		case RETURNOBJECT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("prealertres", "PreAlertReturnType-000002")/*插件返回对象*/;
		case RETURNDATASOURCE:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("prealertres", "PreAlertReturnType-000003")/*消息模板数据源*/;
		case RETURNNOTHING:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("prealertres", "PreAlertReturnType-000004")/*插件返回空值*/;
		case RETURNMULTILANGTEXT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("prealertres", "PreAlertReturnType-000005")/*多语文本*/;
		case RETURN_MULTIPLE_RECEIVER_OBJECT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("prealertres", "PreAlertReturnType-0000")/*多接收者返回值*/;
		default:
			throw new RuntimeException();
		}
	}

	public int toInt() {
		return value;
	}

	public static PreAlertReturnType getObject(int i) {
		switch (i) {
		case 0:
			return RETURNNOTHING; 
		case 1:
			return RETURNOBJECT; 
		case 2:
			return RETURNFORMATMSG; 
		case 3:
			return RETURNMESSAGE; 
		case 4:
			return RETURNDATASOURCE; 
		case 5:
			return RETURNMULTILANGTEXT;
		case 6:
			return RETURN_MULTIPLE_RECEIVER_OBJECT;
		default:
			throw new IllegalArgumentException("no type matches int: " + i);
		}
	}
}
