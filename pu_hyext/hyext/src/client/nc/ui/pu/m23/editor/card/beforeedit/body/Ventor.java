package nc.ui.pu.m23.editor.card.beforeedit.body;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.bd.ref.model.DefdocGridRefModel;
import nc.ui.pu.pub.editor.card.listener.ICardBodyBeforeEditEventListener;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class Ventor implements ICardBodyBeforeEditEventListener {
	@SuppressWarnings("restriction")
	public void beforeEdit(CardBodyBeforeEditEvent e) {
		BillCardPanel card = e.getBillCardPanel();

		String pk_group = card.getHeadItem("pk_group").getValue();//集团
		String pk_org = card.getHeadItem("pk_org").getValue();//组织
		String pk_supplier = card.getHeadItem("pk_supplier").getValue();//供应商
//		int selectedIndex = card.getBodyTabbedPane().getSelectedIndex();
		String pk_material = (String) card.getBodyValueAt(e.getRow(), "pk_material");
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select DISTINCT qc_supplierquality.Pk_vendor from qc_supplierquality  ");
		sql.append(" left join bd_supplier_grade_sys on qc_supplierquality.PK_SUPPLIERGRADE = bd_supplier_grade_sys. pk_suppliergrade  ");
		sql.append(" and bd_supplier_grade_sys.pk_org = qc_supplierquality.pk_org and nvl(bd_supplier_grade_sys.dr,0)=0 ");
		sql.append(" left join bd_supplier_grade on bd_supplier_grade_sys.pk_suppliergrade  = bd_supplier_grade.pk_suppliergrade and nvl(bd_supplier_grade.dr,0)=0 ");
		sql.append(" where nvl(qc_supplierquality.dr,0)=0 "); 
		sql.append("      and nvl(bd_supplier_grade.def1,'N') <> 'Y' ");
		sql.append("       and qc_supplierquality.pk_org ='"+pk_org+"' ");
		sql.append("        and qc_supplierquality.PK_SUPPLIER ='"+pk_supplier+"' ");
		sql.append("       and qc_supplierquality.pk_group='"+pk_group+"' ");
		sql.append("       and qc_supplierquality.pk_material ='"+pk_material+"' ");
		
		try{
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<String> list = (List<String>) query.executeQuery(sql.toString(), new ColumnListProcessor("Pk_vendor") );
		
		
//		SqlBuilder builder = new SqlBuilder();
//		builder.append("select y.*,e.suppliergrade code from qc_supplierquality y ");
//		builder.append(" join bd_supplier_grade e on y.pk_grade_info = e.pk_grade_info ");
//		builder.append(" where nvl(y.dr,0)=0 ");
//		builder.append(" and y.pk_group", pk_group);
//		builder.append(" and y.pk_org", pk_org);
//		builder.append(" and y.pk_supplier", pk_supplier);
//		
		
//		try {
//			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
//			List<SupplierqualityHVO> volist = (List<SupplierqualityHVO>) query
//					.executeQuery(builder.toString(), new BeanListProcessor(
//							SupplierqualityHVO.class));
//			if (volist == null || volist.size() == 0)
//				volist = new ArrayList<>();
//			Map<String,List<String>> retMap = new HashMap<String, List<String>>();
//			for (SupplierqualityHVO vo : volist) {
//				String key = vo.getPk_material();
//				List<String> slist = null;
//				if (retMap.containsKey(key)) {
//					slist = retMap.get(key);
//				} else {
//					slist = new ArrayList<String>();
//				}
//				if(StringUtil.isEmpty(vo.getCode())){
//					continue;
//				}
//				if("待批准".equals(vo.getCode().trim()) || "已批准".equals(vo.getCode().trim()) || "认证".equals(vo.getCode().trim()) || "批准".equals(vo.getCode().trim())){
//					slist.add(vo.getPk_vendor());
//					retMap.put(key, slist);
//				}
//			}
//			String material = (String) card.getBodyValueAt(e.getRow(),
//					"pk_material");
//			List<String> list = retMap.get(material);
		
			UIRefPane refPane = (UIRefPane) card.getBodyItem("cproductorid")
					.getComponent();
			DefdocGridRefModel refmodel = (DefdocGridRefModel)refPane.getRefModel();
			refmodel.setPk_org(pk_org);
			if(list != null && list.size()>0){
				String newWherePart = SQLUtil.buildSqlForIn("pk_defdoc", list.toArray(new String[list.size()]));
				refmodel.addWherePart(" and " +newWherePart);
			}else{
				refmodel.addWherePart(" and 1=1 ");
			}
			
		} catch (BusinessException e1) {
			e1.printStackTrace();
		}
		e.setReturnValue(Boolean.TRUE);
	}

}