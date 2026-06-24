package nc.bs.aop.loginfo;
/*    */
/*    */import nc.bs.aop.log.vo.KeyParam;
/*    */
import nc.md.model.IBusiOperation;
/*    */
/*    */public class DeleteOpProcessor extends AbstractOPProcessor
/*    */{
	/*    */public DeleteOpProcessor(KeyParam keyparam, IBusiOperation busiop,
			int optype)
	/*    */{
		/*  9 */super(keyparam, busiop, optype);
		/*    */}
	/*    */
	/*    */public boolean isNeedDetail()
	/*    */{
		/* 19 */return true;
		/*    */}
	/*    */
	/*    */public void doBeforeCommite()
	/*    */{
		/* 24 */createOldInfo();
		/*    */}
	/*    */
	/*    */public void doafterCommite()
	/*    */{
		/*    */}
	/*    */
}