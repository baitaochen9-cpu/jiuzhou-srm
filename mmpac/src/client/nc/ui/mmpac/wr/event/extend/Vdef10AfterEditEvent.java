package nc.ui.mmpac.wr.event.extend;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.sql.SqlUtils;
//import nc.impl.ic.util.DateCalUtil;
import nc.itf.bd.material.med.extend.IMaterialMantainExt;
import nc.ui.mmpac.wr.handler.WrBaseHandler;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.ftpub.util.DateCalUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class Vdef10AfterEditEvent {
	 public void afterEdit(CardHeadTailAfterEditEvent e)
	   {
	     IMaterialMantainExt service = (IMaterialMantainExt)NCLocator.getInstance().lookup(IMaterialMantainExt.class);
	 
	     Map hashMap = new HashMap();
	     UFDate dbilldate = (UFDate)e.getBillCardPanel().getHeadItem("vdef10").getValueObject();
	     e.getBillCardPanel().setHeadItem("dbilldate", dbilldate);
	     
	     String pk_org = (String)e.getBillCardPanel().getHeadItem("pk_org").getValueObject();
	     int rowCount = e.getBillCardPanel().getRowCount();
	     Set set = new HashSet();
	     for (int row = 0; row < rowCount; ++row) {
	       String cbmainmaterialvid = (String)e.getBillCardPanel().getBodyValueAt(row, "cbmainmaterialvid");
	       if (cbmainmaterialvid == null) continue; if (cbmainmaterialvid.length() == 0) { //主产品信息
	         continue;
	       }
	       set.add(cbmainmaterialvid);
	     }
	 
	     if (set.size() == 0) {
	       return;
	     }
	     StringBuilder sb = new StringBuilder();
	     sb.append(" and ").append("pk_org").append("= '").append(pk_org).append("' and ");
	     sb.append("pk_material").append(" in ").append(SqlUtils.inSql((String[])set.toArray(new String[set.size()]))).append(" ");
	     MaterialStockVO[] materialStockVOs = null;
	     try {
	       materialStockVOs = service.queryMaterialStockVOByCondition(sb.toString(), null);
	     }
	     catch (BusinessException e1) {
	       ExceptionUtils.wrappException(e1);
	     }
	 
	     if ((materialStockVOs == null) || (materialStockVOs.length == 0)) {
	       return;
	     }
	 
	     for (MaterialStockVO materialStockVO : materialStockVOs)
	       if ((materialStockVO != null) && (materialStockVO.getQualitymanflag() != null)) {
	         if (!(materialStockVO.getQualitymanflag().booleanValue())) { //是否保质期管理
	           continue;
	         }
	         hashMap.put(materialStockVO.getPk_material(), materialStockVO);
	       }
	     if ((hashMap == null) || (hashMap.size() == 0)) {
	       return;
	     }
	     for (int row = 0; row < rowCount; ++row) {
	       String cbmainmaterialvid = (String)e.getBillCardPanel().getBodyValueAt(row, "cbmainmaterialvid");
	       if (cbmainmaterialvid == null) continue; if (cbmainmaterialvid.length() == 0) {
	         continue;
	       }
	       if (hashMap.containsKey(cbmainmaterialvid)) {
	         MaterialStockVO stockvo = (MaterialStockVO)hashMap.get(cbmainmaterialvid);
	         UFDate date = DateCalUtil.calDvalidate(dbilldate, stockvo.getQualitynum(), stockvo.getQualityunit());
	 
	         e.getBillCardPanel().setBodyValueAt(date, row, "dvalidate_148");
	         e.getBillCardPanel().setBodyValueAt(date, row, "dinvaliddate_148");
	       }
	     }
	   }
}
