/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.ui.mmpac.wr.service;

/*    */
/*    */import nc.ui.mmpac.wr.dialog.WrProdInDlgEx;
/*    */
import nc.ui.mmpac.wr.serviceproxy.WrBusinessServiceProxy;
/*    */
import nc.util.mmf.framework.base.MMValueCheck;
/*    */
import nc.vo.mmpac.wr.consts.WrptLangConst;
/*    */
import nc.vo.mmpac.wr.entity.AggWrVO;
/*    */
import nc.vo.pub.BusinessException;
/*    */
import nc.vo.pub.lang.UFBoolean;
/*    */
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/*    */
/*    */public class WrProdInUIServiceEx extends WrProdInUIService
/*    */{
	/*    */private WrProdInDlgEx wrProdInDlg;

	/*    */
	/*    */public WrProdInDlgEx getWrProdInDlgEx()
	/*    */{
		/* 19 */return this.wrProdInDlg;
		/*    */}

	/*    */
	/*    */public void setWrProdInDlgEx(WrProdInDlgEx wrProdInDlg) {
		/* 23 */this.wrProdInDlg = wrProdInDlg;

			//*****************20251223 yezhian 禁用表表字段点击排序*****
			this.wrProdInDlg.getBillForm().getBodyTable().setSortEnabled(false);
			//----------------------------*20251223 yezhian 禁用表表字段点击排序------------
	}
	/*    */
	/*    */public Object process(Object obj)
	/*    */{
		/* 30 */if (MMValueCheck.isEmpty(obj)) {
			/* 31 */ExceptionUtils.wrappBusinessException(WrptLangConst
					.getProdinHIT_NOPRODINDATAS());
			/*    */}
		/*    */
		/* 34 */WrBusinessServiceProxy wrBusinessServiceProxy = new WrBusinessServiceProxy();
		/* 35 */AggWrVO[] aggWrVOs = null;
		/*    */try
		/*    */{
			/* 40 */aggWrVOs = wrBusinessServiceProxy
					.prodInFilter((AggWrVO[]) obj);
			/*    */}
		/*    */catch (BusinessException e) {
			/* 43 */ExceptionUtils.wrappException(e);
			/*    */}
		/*    */
		/* 46 */if (MMValueCheck.isEmpty(aggWrVOs)) {
			/* 47 */ExceptionUtils.wrappBusinessException(WrptLangConst
					.getProdinHIT_NOPRODINDATAS());
			/*    */}
		/*    */
		/* 51 */getWrProdInDlgEx().setInitDatas(aggWrVOs);
		/* 52 */getWrProdInDlgEx().setEnabled(UFBoolean.TRUE.booleanValue());
		/*    */
		/* 54 */if (1 == getWrProdInDlgEx().showModal()) {
			/* 55 */return Boolean.valueOf(true);
			/*    */}
		/*    */
		/* 58 */return Boolean.valueOf(false);
		/*    */}
	/*    */
}