/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */
package nc.ui.pu.m23.editor.card.beforeedit;
/*     */import java.util.Map;
import nc.pubitf.para.SysInitQuery;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.AstUnit;
import nc.ui.pu.m23.editor.card.beforeedit.body.Bc_cstateid;
import nc.ui.pu.m23.editor.card.beforeedit.body.Ventor;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.BatchCode;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.ChangeRate;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.Material;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.NeverEditBodyItem;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.NumHandler;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.PresentFlag;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.Project;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.Rack;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.ReceiveStore;
/*     */
import nc.ui.pu.m23.editor.card.beforeedit.body.Reporter;
/*     */
import nc.ui.pu.pub.editor.card.beforeedit.Casscustid;
/*     */
import nc.ui.pu.pub.editor.card.beforeedit.ProjectTaskId;
/*     */
import nc.ui.pu.pub.editor.card.handler.AbstractCardBodyBeforeEditEventHandler;
/*     */
import nc.ui.pu.pub.editor.card.listener.ICardBodyBeforeEditEventListener;
import nc.ui.pub.para.SysInitBO_Client;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/*     */
import nc.vo.uif2.LoginContext;
/*     */
/*     */public class CardBodyBeforeEditEventHandler
		extends
			AbstractCardBodyBeforeEditEventHandler
/*     */{
	/*     */private LoginContext context;
	/*     */
	/*     */public LoginContext getContext()
	/*     */{
		/*  43 */return this.context;
		/*     */}
	/*     */	
	/*     */public void registerEventListener(
			Map<String, ICardBodyBeforeEditEventListener> listenerMap)
	/*     */{
		/*  51 */listenerMap.put("pk_material", new Material());
			
		listenerMap.put("cproductorid", new Ventor());
		listenerMap.put("bc_cstateid", new Bc_cstateid());
		
		/*     */
		/*  53 */listenerMap.put("castunitid", new AstUnit());
		/*     */
		/*  55 */listenerMap.put("creporterid", new Reporter());
		/*     */
		/*  57 */NumHandler numHandler = new NumHandler();
		/*  58 */listenerMap.put("nnum", numHandler);
		/*  59 */listenerMap.put("nastnum", numHandler);
		/*     */
		/*  61 */listenerMap.put("vchangerate", new ChangeRate());
		/*     */
		/*  63 */listenerMap.put("vbatchcode", new BatchCode());
		/*     */
		/*  65 */listenerMap.put("bpresent", new PresentFlag());
		/*     */
		/*  67 */listenerMap.put("pk_receivestore", new ReceiveStore());
		/*     */
		/*  69 */listenerMap.put("cprojectid", new Project());
		/*     */
		/*  71 */listenerMap.put("pk_rack", new Rack());
		/*     */
		/*  74 */NeverEditBodyItem neverEditItem = new NeverEditBodyItem();
		/*     */
		/*  76 */listenerMap.put("bfixedrate", neverEditItem);
		/*     */
		/*  78 */listenerMap.put("bbackreforder", neverEditItem);
		/*     */
		/*  81 */listenerMap.put("fproductclass", neverEditItem);
		/*  82 */listenerMap.put("bpresentsource", neverEditItem);
		/*  83 */listenerMap.put("npresentastnum", neverEditItem);
		/*  84 */listenerMap.put("npresentnum", neverEditItem);
		/*  85 */listenerMap.put("ntaxrate", neverEditItem);
		/*  86 */listenerMap.put("pk_reqstoorg", neverEditItem);
		/*  87 */listenerMap.put("pk_reqstore", neverEditItem);
		/*  88 */listenerMap.put("pk_apfinanceorg", neverEditItem);
		/*  89 */listenerMap.put("pk_apfinanceorg_v", neverEditItem);
		/*  90 */listenerMap.put("pk_psfinanceorg", neverEditItem);
		/*  91 */listenerMap.put("pk_psfinanceorg_v", neverEditItem);
		/**************20220905yezhian 失效日期增加参数控制可调   begin**********************************/
		UFBoolean paraBoolean = UFBoolean.FALSE;
				try {
					 paraBoolean = SysInitQuery.getParaBoolean(this.getContext().getPk_org(), "POJZ01");
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					ExceptionUtils.wrappBusinessException(e.getMessage());
				}
				if(paraBoolean == UFBoolean.FALSE)
					/*************************end**************/
			
		/*  92 */listenerMap.put("dinvaliddate", neverEditItem);
		/*  93 */listenerMap.put("nexchangerate", neverEditItem);
		/*     */
		/*  95 */listenerMap.put("cffileid", neverEditItem);
		/*     */
		/*  98 */listenerMap.put("cprojecttaskid", new ProjectTaskId());
		/*     */
		/* 100 */listenerMap.put("casscustid", new Casscustid());
		/*     */}
	/*     */
	/*     */public void setContext(LoginContext context) {
		/* 104 */this.context = context;
		/*     */}
	/*     */
}