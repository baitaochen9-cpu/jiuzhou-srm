/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.ui.bd.ref.model;
/*    */
/*    */import java.util.Vector;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.sec.esapi.NCESAPI;
/*    */
import nc.ui.bd.ref.AbstractRefModel;
/*    */
import nc.vo.bd.supplier.suppliergradesys.SupplierGradeSysVO;
/*    */
import nc.vo.ml.AbstractNCLangRes;
/*    */
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pubapp.AppContext;
/*    */
/*    */public class SupplierGradeSysRefModel extends AbstractRefModel
/*    */{
	/*    */public SupplierGradeSysRefModel()
	/*    */{
		/* 25 */reset();
		/*    */}
	/*    */
	/*    */public void reset()
	/*    */{
		/* 32 */setFieldCode(new String[]{"code", "name"});
		/*    */
		/* 35 */setFieldName(new String[]{
				NCLangRes4VoTransl.getNCLangRes().getStrByID("10140sgrade",
						"2sgrade-000009"),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("10140sgrade",
						"2sgrade-000002")});
		/*    */
		/* 42 */setHiddenFieldCode(new String[]{"pk_suppliergrade"});
		/*    */
		/* 44 */setPkFieldCode("pk_suppliergrade");
		/*    */
		/* 47 */setRefCodeField("code");
		/*    */
		/* 50 */setRefNameField("name");
		/*    */
		/* 53 */setRefTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"10140sgrade", "2sgrade-000019"));
		/*    */
		/* 57 */setAddEnableStateWherePart(true);
		/*    */
		/* 60 */setTableName(SupplierGradeSysVO.getDefaultTableName());
		/*    */
		/* 62 */resetFieldName();
		/*    */}
	/*    */
	/*    */protected String getEnvWherePart()
	/*    */{
		/* 67 */return " ENABLESTATE = '2'";
//		/* 67 */return "pk_org='" + NCESAPI.clientSqlEncode(getPk_org())
//				+ "' and ENABLESTATE = '2'";
		/*    */}
	/*    */
}