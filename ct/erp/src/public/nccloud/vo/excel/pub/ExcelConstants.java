package nccloud.vo.excel.pub;

import nc.bs.framework.common.RuntimeEnv;

public class ExcelConstants {
	
	public static String ElementLevelSplitChar = "|";
	public static String PositionBetweenSplitChar = ";";

	// 一个文档一个大事务,相对速度要快，但是对于凭证这样得单据，并发时可能出现凭证号重复
	public static int DEALRULE_ALL_OR_NONE = 0;
	// 每个文档独立处理，正确的导入，错误的无法导入
	public static int DEALRULE_EACH_ALONE = 1;

	public static final String YES = "yes";
	public static final String NO = "no";

	public static final String Y = "Y";
	public static final String N = "N";

	public static final String JMS_DUAL_MESSAGE_SEND_Q = "dualMessageSendQ";
	public static final String JMS_URL = "jmsURL";
	public static final String JMS_RECEIVE_Q = "receiveQ";
	public static final String JMS_SEND_Q = "sendQ";
	public static final String JMS_EXSYSTEM = "exsystem";

	/****************************************************************
	 * 配置文件XML节点常量 *
	 ****************************************************************/
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String CLASSNAME = "classname";

	public static final String MODULENAME = "modulename";
	public static final String BILLTYPE = "billtype";
	public static final String PK_ORG = "pk_org";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_BUSINESSUNIT = "pk_businessUnit";
	public static final String GROUPNO = "groupno";
	public static final String ROOTTAG = "roottag";
	public static final String ISEXCHANGE = "isexchange";
	public static final String REPLACE = "replace";
	public static final String FILENAME = "filename";
	public static final String ACCOUNT = "account";
	public static final String RECEIVER = "receiver";
	public static final String SENDER = "sender";
	public static final String COMPRESS = "compress";
	public static final String OPERATOR = "operator";
	public static final String LANGCODE = "langcode";
	public static final String GROUPCODE = "groupcode";
	public static final String BUSINESSUNITCODE = "businessunitcode";
	public static final String ORGCODE = "orgcode";
	public static final String ORGTYPE = "orgtype";
	public static final String IMPORTPROCESS = "importprocess";

	// BillDefination定义文件常量
	public static final String XMLCONFIG_BD_UFINTERFACE = "ufinterface";
	public static final String XMLCONFIG_BD_CRYPT_CLASSNAME = "cryptClassname";
	public static final String XMLCONFIG_BD_DOCIDNAME = "docidname";
	public static final String XMLCONFIG_BD_ROOTTAG = "roottag";

	public static final String XMLCONFIG_BD_RECORD = "record";
	public static final String XMLCONFIG_BD_METADATAID = "metadataid";
	public static final String XMLCONFIG_BD_TABLENAME = "tablename";
	public static final String XMLCONFIG_BD_TABLETAG = "tabletag";
	public static final String XMLCONFIG_BD_STABLETAG = "stabletag";
	public static final String XMLCONFIG_BD_ENTRYTAG = "entrytag";
	public static final String XMLCONFIG_BD_TABLETYPE = "tabletype";
	public static final String XMLCONFIG_BD_RECORDTYPE = "recordtype";
	public static final String XMLCONFIG_BD_INNERCLASSNAME = "innerclassname";
	public static final String XMLCONFIG_BD_INNERRECORDTYPE = "innerrecordtype";
	public static final String XMLCONFIG_BD_ENTITYTAG = "entitytag";
	public static final String XMLCONFIG_BD_SENTITYTAG = "sentitytag";
	public static final String XMLCONFIG_BD_ENTITYPOSITION = "entityposition";
	public static final String XMLCONFIG_BD_DISPLAYNAME = "displayName";

	public static final String XMLCONFIG_BD_VARS = "vars";
	public static final String XMLCONFIG_BD_VAR = "var";
	public static final String XMLCONFIG_BD_VARKEY = "varkey";
	public static final String XMLCONFIG_BD_VARVALUE = "varvalue";

	public static final String XMLCONFIG_BD_FIELD = "field";
	public static final String XMLCONFIG_BD_DESC = "desc";
	public static final String XMLCONFIG_BD_TYPE = "type";
	public static final String XMLCONFIG_BD_NULLALLOWED = "nullallowed";
	public static final String XMLCONFIG_BD_DEFAULTVALUE = "defaultvalue";
	public static final String XMLCONFIG_BD_MAXLENGTH = "maxLength";
	public static final String XMLCONFIG_BD_MATCHTAG = "matchtag";
	public static final String XMLCONFIG_BD_BASDOC = "basdoc";
	public static final String XMLCONFIG_BD_RULE = "rule";
	public static final String XMLCONFIG_BD_REFITEM = "refitem";
	public static final String XMLCONFIG_BD_POSITION = "position";
	public static final String XMLCONFIG_BD_FORMULAIN = "formulain";
	public static final String XMLCONFIG_BD_FORMULAOUT = "formulaout";
	public static final String XMLCONFIG_BD_NEEDEXPORT = "needexport";
	public static final String XMLCONFIG_BD_EXCLASSNAME = "exclassname";
	public static final String XMLCONFIG_BD_VARIABLE = "variable";
	public static final String XMLCONFIG_BD_ORGPK = "orgpk";
	public static final String XMLCONFIG_BD_TRANSLATOR = "translator";
	public static final String XMLCONFIG_BD_VARIABLENAME = "variablename";
	public static final String XMLCONFIG_BD_NEEDEXPANDREFINFO = "needexpandrefinfo";

	// BusinessProcessor定义文件常量
	public static final String XMLCONFIG_BP_BUSINESSPROCESSORS = "businessprocessors";
	public static final String XMLCONFIG_BP_BUSINESSPROCESSOR = "businessprocessor";
	public static final String XMLCONFIG_BP_LOCKLEVEL = "locklevel";
	public static final String XMLCONFIG_BP_METADATAID = "metadataid";
	public static final String XMLCONFIG_BP_BASECLASSNAME = "baseclassname";
	public static final String AAMLEVEL="level";

	/****************************************************************
	 * <手动数据加载节点>-客户端参数 *
	 ****************************************************************/
	public static final int PFXX_CLIENT_MANUAL_LOAD_COMPLETE = 0;
	public static final int PFXX_CLIENT_MANUAL_LOAD_HAS_NO_DESTINATION_URL = -1;
	public static final int PFXX_CLIENT_MANUAL_LOAD_HAS_NO_LOGON_ACCOUNT = -2;
	public static final int PFXX_CLIENT_MANUAL_LOAD_UNKNOWN_DETINATION_HOST = -3;
	public static final int PFXX_CLIENT_MANUAL_LOAD_UNKNOWN_ERROR = -4;
	public static final String PFXX_CLIENT_RECEIPT_FILE_NAME_FORMAT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
			.getStrByID("pfxx", "UPPpfxx-000127")/* @res "BkMsg_数据文件名_YYYYMMDDHHMMSS" */;
	public static final int PFXX_CLIENT_PGS_BAR_INCREMENT = 1;
	public static final int PFXX_CLIENT_PGS_BAR_MIN = 0;
	public static final int PFXX_CLIENT_PGS_BAR_INITIAL = 0;

	/****************************************************************
	 * 自动发送功能-配置参数 *
	 ****************************************************************/
	public static final int SEND_FILE_SUCCEED = 1;
	public static final int CONFIG_ERROR = -1;
	public static final int FILE_LOAD_ERROR = -2;
	public static final int FILE_CONTENT_ERROR = -3;
	public static final int FILE_POST_ERROR = -4;
	public static final int OTHER_ERROR = -5;
	public static final int HEAD = 0;
	public static final int BODY = 1;

	/****************************************************************
	 * 配置文件导出路径 *
	 ****************************************************************/
	// 校验文件路径
	public static final String EXPORT_BILLDEFINEPATH = "/pfxx/billdefine/";
	// 业务插件的注册文件路径
	public static final String EXPORT_BUSINESS_PROCESSORS_PATH = "/pfxx/businessprocessor/";
	// 辅助信息注册文件路径
	public static final String EXPORT_AUXIREGISTER_PATH = "/pfxx/auxiregister/";
	// 多语言文件资源
	public static final String EXPORT_MULTILANGRES_CN = "/resources/lang/simpchn/pfxx/";
	public static final String EXPORT_MULTILANGRES_EN = "/resources/lang/english/pfxx/";
	public static final String EXPORT_MULTILANGRES_TD = "/resources/lang/tradchn/pfxx/";

	/****************************************************************
	 * <配置文件存放路径>-服务端参数 *
	 ****************************************************************/
	// NCHOME
	public static final String NC_HOME = RuntimeEnv.getInstance().getProperty(RuntimeEnv.SERVER_LOCATION_PROPERTY);

	// 配置文件存放根路径
	public static final String CONFIG_PATH = NC_HOME + "/excel/";

	// 配置文件存放根路径
	public static final String CONFIG_PATHS = NC_HOME + "/resources/excel/";
	// 辅助信息注册文件路径
	public static final String AUXIREGISTER_PATH = CONFIG_PATHS + "auxiregister/";
	public static final String BUSINESS_PROCESSORS = "businessprocessor";
	// 业务插件的注册文件路径
	public static final String BUSINESS_PROCESSORS_PATH = CONFIG_PATHS +BUSINESS_PROCESSORS+ "/";
	// 单据定义文件的存放路径
	public static final String BILLDEFINE_PATH = CONFIG_PATHS + "billdefine/";
	// 单据定义文件的存放路径
		public static final String BILLDEFINE_PATHS = CONFIG_PATHS + "billdefine/";
	// 标准单据定义文件的存放路径
	public static final String STDBILLDEFINE_PATH = BILLDEFINE_PATH + "standard/";
	// 接收到的单据保存路径
	public static final String RECEIVEDBILLS_PATH = CONFIG_PATHS + "receivedbills/";
	// 导出单据存根
	public static final String EXPORTBILLS_PATH = CONFIG_PATHS + "exportbills/";
	// 系统配置文件
	public static final String SYSTEMCONFIGINFO = CONFIG_PATHS + "globalset.xml";
	// 外系统发送的文档的存放路径
	public static final String INDOC_PATH = CONFIG_PATHS + "pfxxtemp/indocs/";
	// 翻译后文档的存放路径
	public static final String TRANSLATEDDOC_PATH = CONFIG_PATHS + "pfxxtemp/translated/";
	// 接收失败文档的存放路径
	public static final String FAILEDDOC_PATH = CONFIG_PATHS + "pfxxtemp/failure/";
	// 回执文件备份路径
	public static final String RESPONSE_PATH = CONFIG_PATHS + "pfxxtemp/responses/";
	// 记录输入流以便校对
	public static final String INPUTSTREAM_PATH = CONFIG_PATHS + "pfxxtemp/inputStream/";
	// 后台临时工作目录
	public static final String WORK_TEMP_PATH = CONFIG_PATHS + "pfxxtemp/_work_temp_/";
	///////////////////////////////////////////////////////////////////////////////////
	//excel配置文件根路径
	public static final String EXCEL_PATH = NC_HOME+"/excel/";
	
	//excel模板导出文件的存放路径
	public static final String EXCEL_TEMPLATE_PATH = EXCEL_PATH + "excelbilltemplate/";
	
	//后台文件临时存放路径
	public static final String EXCEL_TEMP_PATH = CONFIG_PATHS+"exceltemp/_work_temp_";
	//后台文件临时存放路径
	public static final String EXCEL_TEMP_PATHS = CONFIG_PATHS+"exceltemp/_work_temp_";
	//后台文件临时存放路径
	public static final String EXCEL_EXPORT_TEMP_PATHS = CONFIG_PATHS+"exceltemp/_export_temp_";
	
	

}
