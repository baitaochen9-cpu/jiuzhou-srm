package nc.ui.to.m5x.event.extend;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.material.mdm.SendMdmItf;
import nc.ui.medpub.editor.card.afteredit.body.BatchCode;
import nc.ui.medpub.editor.card.afteredit.body.MedLotNo;
import nc.ui.medpub.editor.card.handler.AbstractCardBodyAfterEditEventHandler;
import nc.ui.medpub.editor.card.listener.ICardBodyAfterEditEventListener;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.springframework.core.annotation.Order;

public class CardBodyAfterEditHandler extends
		AbstractCardBodyAfterEditEventHandler {
	private MedLotNo medLotNo = null;
	private BatchCode batchCode = null;

	public void registerEventListener(
			Map<String, ICardBodyAfterEditEventListener> listenerMap) {
		listenerMap.put("vlotno_148", getMedLotNo());
		listenerMap.put("vbatchcode", getBatchCode());
	}

	public MedLotNo getMedLotNo() {
		return this.medLotNo;
	}

	public void setMedLotNo(MedLotNo medLotNo) {
		this.medLotNo = medLotNo;
	}

	public BatchCode getBatchCode() {
		return this.batchCode;
	}

	public void setBatchCode(BatchCode batchCode) {
		this.batchCode = batchCode;
	}

//	@Order
//	public void handleAppEvent(CardBodyAfterEditEvent e) {
//		String key = e.getKey();
//		SendMdmItf itf = NCLocator.getInstance().lookup(SendMdmItf.class);
//		if("cinventoryvid".equals(key)){
//			String dest_org = (String) e.getBillCardPanel().getHeadItem("cinstockorgid").getValueObject();
//			int rowCount = e.getBillCardPanel().getBillModel().getRowCount(); //总行数据
//			StringBuffer erromsg = new StringBuffer();
//			for(int i = 0 ;i< rowCount; i++){
//				String cinventoryvid = (String) e.getBillCardPanel().getBodyValueAt(i, "cinventoryvid");
//				if(null == cinventoryvid){
//					continue;
//				}
//				String materialChangeByOrg = "";
//				try {
//					materialChangeByOrg = itf.materialChangeByOrg(cinventoryvid, dest_org);
//				} catch (BusinessException e1) {
//					// TODO Auto-generated catch block
//					 ExceptionUtils.wrappBusinessException(e1.getMessage());
//				}
//				if(null == materialChangeByOrg){
//					String crowno = (String) e.getBillCardPanel().getBodyValueAt(i, "crowno");
//					erromsg.append("第"+crowno+"行物料未匹配到对方公司物料，请检查双方物料是否正常维护主数据！\n");
////					ExceptionUtils.wrappBusinessException("第"+crowno+"行物料未匹配到对方公司物料，请检查双方物料是否正常维护主数据！");
//				}else{
//					
//					e.getBillCardPanel().setBodyValueAt(materialChangeByOrg, i, "vbdef1 ");
//				}
//			}
//			if(erromsg.length() > 0){
//				ExceptionUtils.wrappBusinessException(erromsg.toString());
//			}
//			
//			
//		}
//	}
}
