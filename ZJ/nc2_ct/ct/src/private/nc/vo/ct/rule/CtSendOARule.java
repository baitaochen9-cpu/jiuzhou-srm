/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package nc.vo.ct.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class CtSendOARule
implements IRule<AggCtPuVO>
{
	private String type;
	
	public CtSendOARule(String type)
	{
		/* 24 */this.type = type;
		}
	
	public void process(AggCtPuVO[] vos)
	{
		
			
			for (int i = 0; i < vos.length; ++i) {
				ISysDispatcher iIplatFormEntry = (ISysDispatcher)NCLocator.getInstance().lookup(ISysDispatcher.class.getName());
				 try {
					 iIplatFormEntry.dispatch(vos[i], "OA_CT", null);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					ExceptionUtils.wrappException(e);
				}
				}
			}

}