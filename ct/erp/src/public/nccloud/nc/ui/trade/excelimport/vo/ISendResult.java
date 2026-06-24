package nccloud.nc.ui.trade.excelimport.vo;

public interface ISendResult
{

	//正常处理完毕，没有错误
	public final static String NO_ERROR_DEAL_SUCCEED = "1";
	
	/*
	 * 第一类：环境及未知错误：外部环境、网络错误、未知异常 (需要检查外部环境、网络设置、端口等等)
	 */
	
	//-10000 Servlet处理异常
	public final static String ERR_ENV_SERVLET = "-10000";
	//-10001 数据库链接错误
	public final static String ERR_ENV_DBEXCEPTION = "-10001";
	//-10002 数据库查询错误，SQL错误
	public final static String ERR_ENV_DBQUERY_EXCEPTION = "-10002";
	
	
	/*
	 * 第二类：实施配置错误：信息交换平台配置错误(总体参数、外部系统、帐套、辅助信息、校验文件、对照表等等配置错误)
	 */
	public final static String ERR_CONFIG_EXCEPTION = "-20000";
	
	//-21000整体配置
	
	//-21001 没有设置默认帐套
	public final static String ERR_CONFIG_DEFAULTACCOUNT_NONE = "-21001";
	//-21002 初始化数据源错误
	public final static String ERR_CONFIG_DATASOURCE_INIT = "-21002";
	
	
	//-21101 无法找到相应的校验文件
	public final static String ERR_CONFIG_SCHEME_NONE = "-21101";
	
	//-21102 无法找到单据配置文件
	public final static String ERR_CONFIG_BILLINFO_NONE = "-21102";
	
	//-21103 无法找到插件注册文件
	public final static String ERR_CONFIG_BUSIREG_NONE = "-21103";
	
	//-21104 无法找到SENDURL文件
	public final static String ERR_CONFIG_SENDURL_NONE = "-21104";
	
	//-21105 无法找到辅助信息注册文件
	public final static String ERR_CONFIG_AUXIINFO_NONE = "-21105";
	
	//-21106 文件超过最大单篇传输上限
	public final static String ERR_CONFIG_LIMIT_SIZE = "-21106";
	
	
	//-22000与单据相关配置
	public final static String ERR_CONFIG_BILL_EXCEPTION = "-22000";
	

	//-22001 无法找到对应的注册插件
	public final static String ERR_CONFIG_BILL_BUSIREG_NONE = "-22001";
	//-22002 无法找到插件类、实例化业务插件出现错误
	public final static String ERR_CONFIG_BILL_BUSIPLUGIN_INIT = "-22002";
	
	
	//-22021 档案只可以导入到集团，不能导入到公司
	public final static String ERR_CONFIG_BILL_GROUP_ONLY = "-22021";
	
	//-22022 档案只可以导入到公司，不能导入到集团
	public final static String ERR_CONFIG_BILL_CORP_ONLY = "-22022";
	
	
	
	//-22030 待分配公司编码不正确!非法公司编码
	public final static String ERR_CONFIG_ASSIGN_CORP = "-22030";
	

	//-22100 XML转换为VO时出现错误
	public final static String ERR_CONFIG_XMLTOVO = "-22100";
	
	//
	
	/*
	 * 第三类：用户数据错误，主要包括一下两种:
	 */
	 //3.1 导入数据时出现基本校验错误，这些错误由信息交换平台校验发现（重复导入、格式不对、信息不全、字段类型错误、无法翻译基础数据等等）
	
	 //31000 单据重复导入错误
	 public final static String ERR_PFXX_ADD_BILL_AREADYEXIST = "-31000";
	 //31001 删除不存在的单据错误
	 public final static String ERR_PFXX_DELETE_BILL_NOTEXIST = "-31001";
	 //31002 更新不存在的单据错误
	 public final static String ERR_PFXX_UPDATE_BILL_NOTEXIST = "-31002";
	 //31003 所导入的XML文件格式不正确
	 public final static String ERR_PFXX_BILL_FORMAT_INVALID = "-31003";
	 //31004 单据本身没有错误，但是由于配置为大事务，受其他单据影响而无法导入
	 public final static String ERR_PFXX_TRANSACTION_EFFECTED = "-31004";
	 //31005 XML文件头格式不正确
	 public final static String ERR_PFXX_DOCPARA_INVALID = "-31005";
	 //31006 XML文件里根据头标签找不到单据头
	 public final static String ERR_PFXX_DOC_NOHEAD = "-31006";
	 //31007 XML文件里根据体标签找不到单据体
	 public final static String ERR_PFXX_DOC_NOBODY = "-31007";
	 
	 
	 //-31100 无效操作类型
	 public final static String ERR_PFXX_PROCESS_INVALID = "-31100";
	 //-31101 无效的接收公司
	 public final static String ERR_PFXX_RECEIVER_INVALID = "-31101";
	 //-31102 无效的发送方	 
	 public final static String ERR_PFXX_SENDER_INVALID = "-31102";
	 //-31113 无效的单据类型
	 public final static String ERR_PFXX_BILLTYPE_INVALID = "-31103";
	 //-31114 根据帐套无法找到数据源
	 public final static String ERR_PFXX_ACCOUNT_INVALID = "-31114";
	 //-31115 根据公司无法取得默认主体帐簿
	 public final static String ERR_PFXX_CORP_GLORGBOOK = "-31115";
	 //-31116 外部系统已经停用，不能导入
	 public final static String ERR_PFXX_EXSYSTEM_STOP = "-31116";
	 //-31117 无效的主体帐簿
	 public final static String ERR_PFXX_GLORGBOOK_INVALID = "-31117";
	 //-31118 无效的科目方案
	 public final static String ERR_PFXX_ACCSUJBSCHEME_INVALID = "-31118";
	 //-31119 无效的url格式 应为<公司编码@主体账簿编码>格式
	 public final static String ERR_PFXX_URL_INVALID = "-31119";
	 
	 
	 //31200 字段内容错误－不能为空，类型错误，无法翻译等
	 public final static String ERR_PFXX_BILL_FIELD_INVALID = "-31200";
	 
	 //31201 发送方地址不合法
	 public final static String ERR_PFXX_SENDERADDRESS_INVALID = "-31201";
	 
     //31201 记录内容错误－没有为特定表记录类型定义特定属性等
	 public final static String ERR_PFXX_BILL_RECORD_INVALID= "-31201";
	 
     //31202 单据转换翻译错误
	 public final static String ERR_PFXX_CONVERT_TRANSLATE_ERROR = "-31202";
	 
     //31203 单据导入环境初始化异常
	 public final static String ERR_PFXX_ENVIRONMENT_ERROR = "-31203";
	 
     //31204 基础档案资源列表里没有名为[{0}]的档案，请在自定义档案里添加！
	 public final static String ERR_PFXX_FIELD_NOBASDOC = "-31204";
	 
     //31205 往公司发送时，pk_corp=0001不合法
	 public final static String ERR_PFXX_FIELD_GROUP_WHILECORP = "-31205";
	 
     //31206 根据基本档案[{0}]无法翻译[{1}]字段,待翻译值:{2},翻译方式:{3}.
	 public final static String ERR_PFXX_FIELD_TRAN_ACCORDBASDOC = "-31206";
     
     // 31207 组织字段不一致 
     public final static String ERR_PFXX_ORG_FIELD_INCOMPATIABLE = "-31207";
	 
	 //3.2 所导入的数据出现业务错误，这些错误与实际的单据业务相关，由业务插件发现。
	 
	 
	 //32000 业务插件处理错误
	 public final static String ERR_BUSI_BILL_EXCEPTION = "-32000";
	 //32001 单据编码重复错误
	 public final static String ERR_BUSI_BILL_CODE_AREADYEXIST = "-32001";
	 //32002 名称重复错误
	 public final static String ERR_BUSI_BILL_NAME_AREADYEXIST = "-32002";
	 //32003 没有单据却调用UPDATE操作或者有单据却调用ADD操作
	 public final static String ERR_BUSI_BILL_OPER_CONFICT= "-32003";
	 //更多业务插件错误得在实际业务插件中抽取

	 
	//其他未知错误
	public final static String ERR_UNKOWN_EXCEPTION = "-40000";


}
