package nc.bs.pub.pa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 *<b> 预警/后台任务插件的返回值</b>
 * 
 * 
 * 所有实现IPreAlertPlugin及IBackgroundWorkPlugin的类 在其execute()方法中请返回一个此类的实例
 * 该返回值将用来构建预警/后台任务的消息
 * 
 * 
 * 设置返回值的步骤： 1. 调用setTitle()方法设置预警/后台任务消息标题 注意：如果采用消息模板方式，可以不用设置消息标题 2.
 * 调用setReturnType()方法设置返回值类型 3. 调用setReturnObj()方法设置返回值 4.1
 * 若在消息模板分类中为该预警/后台任务类型关联了业务实体 请调用setBusiObj()方法设置业务实体对象 4.2
 * 若在消息模板分类中为该预警/后台任务类型中设置了自定义变量 请调用setBusiCalculater()方法设置变量对应的值
 * 
 * @author yanke1
 */
public class PreAlertObject
{
	// 默认预警消息标题，即消息中心看到的“默认主题”，如果预警条目中未设置消息标题，则取该属性值
	@Deprecated
	// 推荐采用消息模板的方式实现预警消息的构造。消息模板方式无需设置标题
	private String msgTitle;

	// 返回类型
	// 返回值,根据返回值类型(PreAlertReturnType)的不同,要求返回值对应如下：
	// 1 PreAlertReturnType.RETURNMESSAGE——>String (固定字符串类型)
	// 2 PreAlertReturnType.RETURNOBJECT——>Object (对象类型)
	// 3 PreAlertReturnType.RETURNFORMATMSG——>IAlertMessage (格式化消息，可转换成打印模板数据源)
	// 4 PreAlertReturnType.RETURNDATASOURCE——>IDataSource (输出模板数据源)
	// 5 PreAlertREturnType.RETURNNOTHING->null
	// 6 PreAlertReturnType.RETURNMULTILANGTEXT->MultiLangText
	private PreAlertReturnType returnType;
	private Object returnObj;
	private Object busiObj;
	
	private IBusiCalculater busiCalculater;
	
	/**
	 * key-自定义接收人编码 value-编码对应的用户主键集合 
	 * 该Map的数据须由PreAlertObject的创建者调用addUserPks进行填充
	 */
	private Map<String, Set<String>> codeUserPkSetMap = new HashMap<String, Set<String>>();

	public IBusiCalculater getBusiCalculater()
	{
		return busiCalculater;
	}

	/**
	 * 业务组设置自定义变量计算器(考虑到多语问题)
	 * 
	 * @param busiCalculater
	 */
	public void setBusiCalculater(IBusiCalculater busiCalculater)
	{
		this.busiCalculater = busiCalculater;
	}

	public PreAlertReturnType getReturnType()
	{
		return returnType;
	}

	/**
	 * 设置预警ReturnObj的类型 该ReturnObj用来构造预警消息的附件
	 * 若预警执行后不需要返回消息，则此处设为第5项PreAlertReturnType.RETURNNOTHING 1
	 * PreAlertReturnType.RETURNMESSAGE——>String (固定字符串类型) 2
	 * PreAlertReturnType.RETURNOBJECT——>Object (对象类型) 3
	 * PreAlertReturnType.RETURNFORMATMSG——>IAlertMessage (格式化消息，可转换成打印模板数据源) 4
	 * PreAlertReturnType.RETURNDATASOURCE——>IDataSource (输出模板数据源) 5
	 * PreAlertReturnType.RETURNNOTHING->null 6
	 * PreAlertReturnType.RETURNMULTILANGTEXT->MultiLangText
	 * 
	 * @param returnType
	 */
	public void setReturnType(PreAlertReturnType returnType)
	{
		this.returnType = returnType;
	}

	public Object getReturnObj()
	{
		return returnObj;
	}

	public Object getBusiObj()
	{
		return busiObj;
	}

	/**
	 * 设置业务实体对象
	 * <p>
	 * 若在消息模板分类中为该预警类型关联了业务实体并且在预警条目中使用了消息模板
	 * <p>
	 * 则此处应当设置业务实体对象
	 * <p>
	 * 该对象将用来解析消息模板中配置的元数据
	 * 
	 * @param busiObj
	 */
	public void setBusiObj(Object busiObj)
	{
		this.busiObj = busiObj;
	}

	/**
	 * 设置预警ReturnObj
	 * <p>
	 * 该ReturnObj用来构造预警消息的附件
	 * <p>
	 * 调用此方法前请调用setReturnType设置此ReturnObj的类型
	 * 
	 * @param returnObj
	 */
	public void setReturnObj(Object returnObj)
	{
		this.returnObj = returnObj;
	}

	@Deprecated
	public String getMsgTitle()
	{
		return msgTitle;
	}

	/**
	 * 设置预警消息的标题
	 * <p>
	 * 若该预警条目使用了消息模板并在消息模板中配置了标题
	 * <p>
	 * 则此处标题将不起作用
	 * 
	 * @param title
	 * @deprecated
	 */
	public void setMsgTitle(String title)
	{
		this.msgTitle = title;
	}
	
	/**
	 * <b>自定义接收人编码及其对应用户主键集合容器Map 添加数据</b>
	 * 
	 * @param code
	 * 				自定义接收人编码
	 * @param userPkSet
	 * 				编码对应的用户主键集合
	 */
	public void addUserPks(String code, Set<String> userPkSet)
	{
		if(CollectionUtils.isEmpty(userPkSet))
			return;
		codeUserPkSetMap.put(code, userPkSet);
	}
	
	/**
	 * <b>传入自定义接收人编码数组 获取其对应的用户主键集合</b>
	 * 
	 * @param code
	 * 				自定义接收人编码
	 * @return
	 * 				用户主键集合
	 */
	public Set<String> getUserPkSetByCode(String code) 
	{
		if(codeUserPkSetMap.isEmpty() || code == null)
			return new HashSet<String>();
		
		Set<String> set = codeUserPkSetMap.get(code);
		if (!CollectionUtils.isEmpty(set))
			return set;
			
		return new HashSet<String>();
	}
}
