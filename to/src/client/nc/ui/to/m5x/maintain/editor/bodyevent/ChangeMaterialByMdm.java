package nc.ui.to.m5x.maintain.editor.bodyevent;
import nc.bs.framework.common.NCLocator;
import nc.itf.material.mdm.SendMdmItf;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class ChangeMaterialByMdm {
	public void afterEdit(CardBodyAfterEditEvent e){

		String key = e.getKey();
		SendMdmItf itf = NCLocator.getInstance().lookup(SendMdmItf.class);
		if("cinventoryvid".equals(key)){
			String dest_org = (String) e.getBillCardPanel().getHeadItem("cinstockorgid").getValueObject();
			int rowCount = e.getBillCardPanel().getBillModel().getRowCount(); //总行数据
			StringBuffer erromsg = new StringBuffer();
			for(int i = 0 ;i< rowCount; i++){
				String cinventoryvid = (String) e.getBillCardPanel().getBodyValueAt(i, "cinventoryvid");
				if(null == cinventoryvid){
					continue;
				}
				String materialChangeByOrg = "";
				try {
					materialChangeByOrg = itf.materialChangeByOrg(cinventoryvid, dest_org);
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					 ExceptionUtils.wrappBusinessException(e1.getMessage());
				}
				if(null == materialChangeByOrg){
					String crowno = (String) e.getBillCardPanel().getBodyValueAt(i, "crowno");
					erromsg.append("第"+crowno+"行物料未匹配到对方公司物料，请检查双方物料是否正常维护主数据！\n");
//					ExceptionUtils.wrappBusinessException("第"+crowno+"行物料未匹配到对方公司物料，请检查双方物料是否正常维护主数据！");
				}else{
					
					e.getBillCardPanel().setBodyValueAt(materialChangeByOrg, i, "vbdef1 ");
				}
			}
			if(erromsg.length() > 0){
				ExceptionUtils.wrappBusinessException(erromsg.toString());
			}
			
			
		}
	
	}
}
