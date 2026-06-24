/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */
package nc.impl.pu.m23.approve.action;
/*     */import nc.bs.pu.m23.fa.rule.CanStoreNumRule;
/*     */
import nc.bs.pu.m23.plugin.ArriveActionPlugInPoint;
/*     */
import nc.bs.pub.compiler.AbstractCompiler2;
/*     */
import nc.bs.scmpub.pf.PfParameterUtil;
/*     */
import nc.impl.pu.m23.approve.rule.ApproveAfterEventRule;
import nc.impl.pu.m23.approve.rule.M23ApproveAndCreateZJ;
/*     */
import nc.impl.pu.m23.approve.rule.ApproveAfterUpdateBatchcodeRule;
/*     */
import nc.impl.pu.m23.approve.rule.ApproveBeforeEventRule;
/*     */
import nc.impl.pu.m23.approve.rule.ChkCanApproveRule;
/*     */
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
/*     */
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
/*     */
import nc.vo.ml.AbstractNCLangRes;
/*     */
import nc.vo.ml.NCLangRes4VoTransl;
/*     */
import nc.vo.pu.m23.entity.ArriveVO;
/*     */
import nc.vo.pu.m23.rule.ArriveATPUpdateRule;
/*     */
import nc.vo.pu.pub.enumeration.PuBusiLogActionCode;
/*     */
import nc.vo.pu.pub.enumeration.PuBusiLogPathCode;
/*     */
import nc.vo.pu.pub.rule.busilog.WriteOperateLogRule;
/*     */
import nc.vo.pub.compiler.PfParameterVO;
/*     */
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/*     */

/*     */public class ArriveApproveAction
/*     */{
	/*     */public ArriveVO[] approveArrive(ArriveVO[] voArray,
			AbstractCompiler2 script)
	/*     */{
		/*  48 */PfParameterUtil util =
		/*  49 */new PfParameterUtil((script == null) ? null :
		/*  50 */script.getPfParameterVO(), voArray);
		/*  51 */ArriveVO[] originBills = (ArriveVO[]) util.getOrginBills();
		/*     */
		/*  54 */AroundProcesser processer =
		/*  55 */new AroundProcesser(
		/*  56 */ArriveActionPlugInPoint.ArriveApproveAction);
		/*  57 */addBeforeRule(processer);
		/*  58 */addAfterRule(processer);
		/*  59 */addATPRule(processer);
		/*     */
		/*  61 */new ArriveATPUpdateRule(true).process(voArray);
		/*  62 */processer.before(voArray);
		/*     */
		/*  64 */if (script != null) {
			/*     */try {
				/*  66 */script.procFlowBacth(script.getPfParameterVO());
				/*     */}
			/*     */catch (Exception e) {
				/*  69 */ExceptionUtils.wrappException(e);
				/*     */}
			/*     */}
		/*  72 */if ((script == null)
				|| (script.getPfParameterVO().m_preValueVos == null)) {
			/*  73 */String msg =
			/*  74 */NCLangRes4VoTransl.getNCLangRes().getStrByID("4004040_0",
			/*  75 */"04004040-0115");
			/*  76 */ExceptionUtils.wrappBusinessException(msg);
			/*  77 */return null;
			/*     */}
		/*  79 */BillUpdate update = new BillUpdate();
		/*  80 */ArriveVO[] returnVos = (ArriveVO[]) update.update(voArray,
				originBills);
		/*     */
		/*  82 */processer.after(returnVos);
		/*     */
		/*  84 */return returnVos;
		/*     */}
	/*     */
	/*     */private void addAfterRule(AroundProcesser<ArriveVO> processer)
	/*     */{
		/*  89 */processer.addAfterRule(
		/*  91 */new WriteOperateLogRule(
				PuBusiLogPathCode.puarrivalApprovePath.getCode(),
				/*  91 */PuBusiLogActionCode.approve.getCode()));
		/*     */
		/*  93 */processer.addAfterRule(new ApproveAfterEventRule());
		/*     */
		/*  95 */processer.addAfterRule(new ApproveAfterUpdateBatchcodeRule());
		processer.addAfterRule(new M23ApproveAndCreateZJ());
		/*     */}
	/*     */
	/*     */private void addATPRule(AroundProcesser<ArriveVO> processer)
	/*     */{
		/* 101 */processer.addAfterRule(new ArriveATPUpdateRule(false));
		/*     */}
	/*     */
	/*     */private void addBeforeRule(AroundProcesser<ArriveVO> processer)
	/*     */{
		/* 106 */processer.addBeforeRule(new ChkCanApproveRule());
		/*     */
		/* 110 */processer.addBeforeRule(new ApproveBeforeEventRule());
		/*     */
		/* 112 */processer.addBeforeRule(new CanStoreNumRule());
		/*     */}
	/*     */
}