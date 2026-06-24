/*
 * Created on 2004-10-25
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nccloud.vo.excel.scheme;

/**
 * @author CCH
 * 单据体内含类型代码
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IRecordType 
{
    //结构类型 
	public static final String HEAD = "head"; //主表
	public static final String BODY = "body"; //子表
	public static final String EMBED = "embed"; //内嵌结构
	public static final String INCLUDE = "include";//包含结构
	
	//数据类型
	public static String DATA_TYPE_BASIC = "basic"; //基本类型
    public static String DATA_TYPE_ARRAY = "array"; //基本类型数组
    public static String DATA_TYPE_VO = "vo";       //VO
    public static String DATA_TYPE_VOS = "vos";     //VOS数组
    public static String DATA_TYPE_COLLECTION = "collection"; //集合
    public static String DATA_TYPE_CUSTOM = "custom"; //自定义类型
}
