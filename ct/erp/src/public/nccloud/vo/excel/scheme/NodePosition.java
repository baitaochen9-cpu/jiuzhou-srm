package nccloud.vo.excel.scheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.vo.jcom.lang.StringUtil;

/**
 * 节点位置(position+extag)定义
 * @author cch (cch@ufida.com.cn)
 * 2006-7-6-11:13:29
 * 
 */
public class NodePosition implements Cloneable,Serializable
{
	/** 路径字符串定义 */
	private String positionString = null;
	/** position中包含的多条路径*/
	private List nodePaths = null;

	/**
	 * @param positionString
	 */
	public NodePosition(String positionString)
	{
		super();
		this.positionString = positionString;
		if(StringUtil.isEmpty(positionString))
			return;
		//拆分路径单元
		String[] units = positionString.split(";");
		for (int i = 0; i < units.length; i++)
		{
			getNodePaths().add(new NodePath(units[i]));
		}
	}

	/**
	 * @return Returns the pathString.
	 */
	public String getPositionString()
	{
		return positionString;
	}

	/**
	 * @param positionString The pathString to set.
	 */
	public void setPositionString(String positionString)
	{
		this.positionString = positionString;
	}

	/**
	 * @return Returns the pathUnit.
	 */
	public List getNodePaths()
	{
		if(nodePaths == null)
			nodePaths = new ArrayList();
		return nodePaths;
	}

	/**
	 * @param pathUnit The pathUnit to set.
	 */
	public void setNodePaths(List pathUnit)
	{
		this.nodePaths = pathUnit;
	}
}
