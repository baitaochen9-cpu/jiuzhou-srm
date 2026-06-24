/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.vo.ct.rule;
/*    */
/*    */import nc.bs.businessevent.BusinessEvent;
/*    */
import nc.bs.businessevent.EventDispatcher;
/*    */
import nc.impl.pubapp.pattern.rule.IRule;
/*    */
import nc.vo.ct.pub.CTMDValue;
/*    */
import nc.vo.ct.purdaily.entity.AggCtPuVO;
/*    */
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/*    */
/*    */public class PurdailyEventRule
/*    */implements IRule<AggCtPuVO>
/*    */{
	/*    */private String type;
	/*    */
	/*    */public PurdailyEventRule(String type)
	/*    */{
		/* 24 */this.type = type;
		/*    */}
	/*    */
	/*    */public void process(AggCtPuVO[] vos)
	/*    */{
		/*    */try {
			/* 30 */String sourceid = CTMDValue.PURDAILY.value();
			/* 31 */EventDispatcher.fireEvent(new BusinessEvent(sourceid,
					this.type, vos));
			/*    */}
		/*    */catch (Exception e) {
			/* 34 */ExceptionUtils.wrappException(e);
			/*    */}
		/*    */}
	/*    */
}