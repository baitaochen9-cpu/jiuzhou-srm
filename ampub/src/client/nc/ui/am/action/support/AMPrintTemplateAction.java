package nc.ui.am.action.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.IActionCode;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.itf.bd.printcheck.IPrintLog;
import nc.itf.bd.printset.IPrintLogService;
import nc.itf.ewm.prv.IWorkOrderService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.ui.am.print.AMUserDefinedDatasSource;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.print.PrintException;
import nc.ui.pub.print.output.IDataSourceOrganizer;
import nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.actions.ActionInitializer;
import nc.vo.bd.printcheck.PrintResultVO;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.print.PrintTempletmanageHeaderVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uapbd.printnumber.PrintLogVO;
import nc.vo.uapbd.printnumber.PrintNumberVO;
import nc.vo.util.AuditInfoUtil;

/**
 * 模板打印
 * 
 * @author mxh
 */
public class AMPrintTemplateAction extends AMPrintTempPrinviewAction {

	private static final long serialVersionUID = 1L;

	public AMPrintTemplateAction() {
		ActionInitializer.initializeAction(this, IActionCode.PRINT);
	}

	@Override
	protected void doFunction(PrintEntry entry) {
//		entry.print();
		String code=entry.getTemplateID();
		PrintTempletmanageHeaderVO[] headerVO=entry.getAllTemplates();
		String typecode=headerVO[0].getVnodecode();
		if("4560016004".equals(typecode)){
			nc.ui.am.print.AMUserDefinedDatasSource data= (AMUserDefinedDatasSource) entry.getDataSource();
			
			Object[] agg= data.getMDObjects();
			nc.vo.ewm.workorder.AggWorkOrderVO aggvo=(AggWorkOrderVO) agg[0];
			String pk=aggvo.getParentVO().getPk_wo();
			
			int vdef1=aggvo.getParentVO().getDef1()==null? Integer.valueOf("0") :Integer.valueOf(aggvo.getParentVO().getDef1());
		
			
 			

				
			try {
				IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				
//				//			控制类型  提醒 、强控、不控制
						String sql = 
								"select  a.printlimit,a.billtypename,a.controltype\n" +
										"from pub_printlimit a\n" + 
										"where a.billtypename='工单'";
				Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
				int controltype=Integer.valueOf(map.get("controltype").toString());
				int printlimit=Integer.valueOf(map.get("printlimit").toString());
				UFBoolean booolean=UFBoolean.FALSE;

				if(vdef1>=printlimit){
					booolean=UFBoolean.TRUE;
				}
				if(booolean.booleanValue()){
//					0=提示，1=不控制，2=严格控制，
					if(controltype==0){
						MessageDialog.showHintDlg(null, "提示", "打印次数超标,请注意");
					}else  if( controltype==2){
						  
						  MessageDialog.showErrorDlg(null, "错误", "打印次数超标,请注意");
					}
					
				}
				
				 PrintNumberVOContrl(aggvo,controltype,printlimit) ;
				aggvo.getParentVO().setDef1(String.valueOf(vdef1+1));
				aggvo.getParentVO().setStatus(VOStatus.UPDATED);
				IWorkOrderService iWorkOrderService = (IWorkOrderService)NCLocator.getInstance().lookup(IWorkOrderService.class.getName());
				iWorkOrderService.updateWorkOrder(aggvo);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
//			System.out.println("111");
		}
		entry.print();
//		nc.vo.ewm.workorder.AggWorkOrderVO aggvo=entry.
		
//		System.out.println("111");
	}
	
	
	 private IMDPersistenceQueryService getMDQueryService() {
		 /* 478 */     return MDPersistenceService.lookupPersistenceQueryService();
		 /*     */   }
	
	
	/**
	 * 根据“节点是否提供打印参数”，选择检查方式
	 * 
	 * @return
	 * @throws BusinessException
	 * @throws PrintException
	 */
	private void PrintNumberVOContrl(AggWorkOrderVO aggvo,int controltype,int printlimit) throws BusinessException,
			PrintException {
//		List<PrintNumberVO> paras = getPrintParas();
//		IPrintLog service = NCLocator.getInstance().lookup(IPrintLog.class);
//		service.updatePrintNumAndLog(remindvos);
//		String sql=" select pk_printnumber  from pub_printnumber where  billid='"+aggvo.getParentVO().getPk_wo() +"'";
//		Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		
		
		/* 534 */     String con = " 1=1 and billid='"+aggvo.getParentVO().getPk_wo()+"'";
		
		 Collection col = getMDQueryService().queryBillOfVOByCond(PrintNumberVO.class, con, true);
		 if(col==null||col.size()==0){
			 PrintNumberVO vo=new PrintNumberVO();
			 
			 vo.setAlternum(0);
			 vo.setBillcode(aggvo.getParentVO().getBill_code());
//			 vo.setBilldata(aggvo.getParentVO().getBillmaketime().getDate() );
//			 0=提示，1=不控制，2=严格控制，
			 vo.setBilldata(aggvo.getParentVO().getBillmaketime());
			 vo.setBillid(aggvo.getParentVO().getPk_wo());
			 vo.setControltype(controltype);
			 vo.setCreator(AuditInfoUtil.getCurrentUser());
			 vo.setCreationtime(new UFDateTime(System.currentTimeMillis()));
			 vo.setModifier(AuditInfoUtil.getCurrentUser());
			 vo.setModifiedtime(new UFDateTime(System.currentTimeMillis()));
			 vo.setOperator(AuditInfoUtil.getCurrentUser());
			 vo.setPk_org(aggvo.getParentVO().getPk_org());
			 vo.setPk_billtype("1001Z900000000002215");
			 vo.setPrintlimit(printlimit);
			 vo.setPrintnumber(0);
			 vo.setPrintnumber(0);
//				BaseDAO dao=new BaseDAO();
//				dao.insertVO(vo);
				HYPubBO_Client client =new HYPubBO_Client();
				client.insert(vo);
//			  alternum  申请次数  alternum int  Integer      
//			  2   billcode  单据编号  billcode varchar(50)  String      
//			  3   billdata  单据日期  billdata char(19)  UFDateTime      
//			  4   billid  单据主键  billid varchar(50)  String      
//			  5   controltype  控制方式  controltype int  控制方式   0=提示，1=不控制，2=严格控制，    
//			  6   creationtime  创建时间  creationtime char(19)  UFDateTime      
//			  7   creator  创建人  creator varchar(20)  用户  用户    
//			  8   modifiedtime  最后修改时间  modifiedtime char(19)  UFDateTime      
//			  9   modifier  最后修改人  modifier varchar(20)  用户  用户    
//			  10   operator  制单人  operator varchar(20)  用户  用户    
//			  11   pk_billtype  单据类型  pk_billtype varchar(20)  单据类型  影响因素单据类型    
//			  12   pk_org  业务单元  pk_org varchar(20)  组织  组织类型分类的组织单元    
//			  15   pk_printnumber  打印次数主键  pk_printnumber char(20)  UFID      
//			  16   printlimit  限制次数  printlimit int  Integer      
//			  17   printnumber  已打印次数  printnumber int  Integer      
//			  18   printorder  打印顺序  
			 col = getMDQueryService().queryBillOfVOByCond(PrintNumberVO.class, con, true);
//			 vo.s
		 }
		String userr=InvocationInfoProxy.getInstance().getUserId();
		PrintNumberVO[] printvos = (PrintNumberVO[])col.toArray(new PrintNumberVO[0]);
		
		  for (int i = 0; i < printvos.length; ++i) {
			  /* 545 */       PrintLogVO logvo = new PrintLogVO();
			  /* 546 */       logvo.setStatus(2);
			  /* 547 */       logvo.setOperator(AuditInfoUtil.getCurrentUser());
			  /* 548 */       logvo.setPrintdata(new UFDateTime(System.currentTimeMillis()));
			  /* 549 */       int printorder = (printvos[i].getPrintorder() == null) ? 0 : printvos[i].getPrintorder().intValue();
			  /* 550 */       logvo.setPrintnumber(Integer.valueOf(printorder + 1));
			  /* 551 */       printvos[i].setPrintnumber(Integer.valueOf(printvos[i].getPrintnumber().intValue() + 1));
			  /* 552 */       printvos[i].setPrintorder(Integer.valueOf(printorder + 1));
			  /* 553 */       printvos[i].setStatus(1);
			  /* 554 */       printvos[i].setPk_printlog(new PrintLogVO[] { logvo });
			  /*     */     }
			  /*     */ 
			  /* 557 */     IPrintLogService service = (IPrintLogService)NCLocator.getInstance().lookup(IPrintLogService.class);
			  /* 558 */     service.update(printvos);

	}

}
