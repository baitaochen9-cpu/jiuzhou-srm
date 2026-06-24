package nccloud.vo.excel.scheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nccloud.vo.excel.pub.ExcelConstants;

import nc.vo.jcom.lang.StringUtil;

/**
 * 路径单元
 * @author cch (cch@ufida.com.cn)
 * 2006-7-6-11:17:57
 * 
 */
public class NodePath implements Serializable, Cloneable
{
	private String nodePathString = null;
	private List steps = null;

	/**
	 * @param pathUnitString
	 */
	public NodePath(String pathUnitString)
	{
		super();
		this.nodePathString = pathUnitString;
        if(StringUtil.isEmpty(pathUnitString))
        	return;
        	
        String[] levelstrs = pathUnitString.split(String.valueOf(ExcelConstants.PositionBetweenSplitChar));
        for (int i = 0; i < levelstrs.length; i++) {
            getSteps().add(levelstrs[i]);    
        }
	}
	
	/**
	 * 取得最末层次,包含条件如 a|b|c[condtion], 则会返回c[conditon]
	 * @return
	 */
	public String getEndLevel()
	{
		return (String)getSteps().get(getSteps().size()-1);
	}
	
	/**
	 * 取得最末层次,不包含条件如 a|b|c[condtion], 则会返回c
	 * @return
	 */
	public String getEndLevelWithoutCon()
	{
		String endlevel = getEndLevel();
		if(!StringUtil.isEmpty(endlevel))
		{
			int npos = endlevel.indexOf("[");
			if (npos>=0) 
				return endlevel.substring(0, npos);
			else
				return endlevel;
		}
		return null;
	}
	

	/**
	 * 取得路径中的所有层次,list中依次存放各层次的字符串
	 * @return Returns the levels.
	 */
	public List getSteps()
	{
		if(steps == null)
			steps = new ArrayList();
		return steps;
	}

	/**
	 * @param steps The levels to set.
	 */
	public void setSteps(List steps)
	{
		this.steps = steps;
	}

	/**
	 * @return Returns the pathUnitString.
	 */
	public String getNodePathString()
	{
		return nodePathString;
	}

	/**
	 * @param pathUnitString The pathUnitString to set.
	 */
	public void setNodePathString(String pathUnitString)
	{
		this.nodePathString = pathUnitString;
	}
	
	
}
