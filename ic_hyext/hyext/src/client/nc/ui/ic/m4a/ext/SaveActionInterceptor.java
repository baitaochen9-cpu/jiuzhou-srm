/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.ic.m4a.ext;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

import nc.bs.dbcache.DBCacheFacadeFactory;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.general.view.ICBizView;
import nc.ui.ic.pub.model.ICBizEditorModel;
import nc.ui.medpub.editor.card.afteredit.body.BillPropUtil;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.vo.ic.m4a.entity.GeneralInBodyVO;
import nc.vo.ic.m4a.entity.GeneralInVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
/**
 * 
 * Insert into sm_paramregister (DR,PARAMNAME,PARAMVALUE,PARENTID,PK_PARAM,TS)
 *  values (0,'PluginBeanConfigFilePath_03','nc/ui/ic/m4a/ext/generalin_ext.xml','1001Z810000000000SJU','1001B61000000003BHEB','2020-10-16 07:22:58');
 * @author hoofi
 *
 */
@SuppressWarnings("restriction")
public class SaveActionInterceptor implements ActionInterceptor {
	private ICBizEditorModel editorModel;
	
    
	@Override
	public boolean beforeDoAction(Action action, ActionEvent e) {
		ICBizView view = (ICBizView) this.getEditorModel().getICBizView();
		if (view == null) {
			return false;
		}
		GeneralInVO bill = (GeneralInVO) this.getEditorModel().getICBizView().getValue();
		
		GeneralInBodyVO[] bodys = bill.getBodys();
		
		Map<String,BatchcodeVO> map=new HashMap<String,BatchcodeVO>();
		StringBuilder s=new StringBuilder();
		for(GeneralInBodyVO body:bodys){
			
			if(StringUtil.isEmpty( body.getVbatchcode())){
				continue;
			}
			
		
			BatchcodeVO bcodevo=getBatchCode(body.getCmaterialvid(), body.getVbatchcode());
			if(bcodevo==null){
				continue;
			}
			// 这里要找物料库存页签下的 保质期管理。然后就没时间是否开户保质期，进巨顶要不要做检查，
			String sql =" select qualitymanflag  bd_materialstock where pk_org='"+bill.getHead().getPk_org()+"' and pk_material='"+body.getCmaterialvid()+"'";
			IUAPQueryBS qry =NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String qualitymanflag ="N";
			try {
				qualitymanflag =(String)qry.executeQuery(sql, new ColumnProcessor());
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			if(qualitymanflag == null){
				qualitymanflag ="N";
			}
			if("N".equalsIgnoreCase(qualitymanflag)){
				continue;
			}
			String msg=checkdata(body,bcodevo);
			if(!StringUtil.isEmpty(msg)){
				s.append(msg);
				map.put(body.getCmaterialvid()+ body.getVbatchcode(),bcodevo);
			}
			
		}
		if(s.length()==0){
			return true;
		}
		
		s.append("\n是否继续保存？");
		
		int ret=MessageDialog.showYesNoDlg(getEditorModel().getICBizView(), null, s.toString());
		if(ret!=nc.ui.pub.beans.UIDialog.ID_YES){
			return false;
		}
		//选择是 根据批次档案替换
		for(GeneralInBodyVO body:bodys){
			
			if(StringUtil.isEmpty( body.getVbatchcode())){
				continue;
			}
			BatchcodeVO bcodevo=map.get(body.getCmaterialvid()+ body.getVbatchcode());
			if(bcodevo==null){
				continue;
			}
			//重新设置到界面
			
			body.setAttributeValue("dproducedate_148", bcodevo.getDproducedate());
			body.setAttributeValue("dvalidate", bcodevo.getDvalidate());
			body.setAttributeValue("recheckdate_148", bcodevo.getAttributeValue("recheckdate_148"));
			body.setAttributeValue("cstateid", bcodevo.getCqualitylevelid());
		}
		BillCardPanel card=getEditorModel().getICBizView().getBillCardPanel();
		BillModel bm= card.getBillModel();
		
		String[] str=new String[] {
				    "cmaterialvid", "cmaterialvid.ivaliduntilunit_148", 
			       "cmaterialvid.iexpirydate_148",   "cmaterialvid.iexpiryunit_148", 
			      "vinvaliddate_148", "dproducedate_148", "dproducedate", 
			     "dvalidate", 
			    "", "pk_org" };
		for(int i=0;i<bm.getRowCount();i++){
			String cmaterialvid=(String)bm.getValueAt(i, "cmaterialvid_ID");
			String vbatchcode=(String)bm.getValueAt(i, "vbatchcode");
			if(StringUtil.isEmpty(vbatchcode)){
				continue;
			}
			BatchcodeVO bcodevo=map.get(cmaterialvid+vbatchcode);
			if(bcodevo==null){
				continue;
			}
			//
			bm.setValueAt(bcodevo.getDproducedate(), i, "dproducedate_148");
			BillEditEvent be=new BillEditEvent(bm.getItemByKey("dproducedate_148"),bcodevo.getDproducedate(),"dproducedate_148", i, 1);
			
			CardBodyAfterEditEvent editevent=new CardBodyAfterEditEvent(card,be,null);
			try {
				BillPropUtil.afterEditProduceDate(card, str, editevent);
			} catch (Exception e1) {
			
				e1.printStackTrace();
				return false;
			}
			 
			bm.setValueAt(bcodevo.getDproducedate(), i, "dproducedate");
			bm.setValueAt(bcodevo.getDvalidate(), i, "dvalidate");
			bm.setValueAt(bcodevo.getAttributeValue("recheckdate_148"), i, "recheckdate_148");
			bm.setValueAt(bcodevo.getAttributeValue("cqualitylevelid"), i, "cstateid");
			
		}
		
	
		return true;
	}
	//生产日期，失效日期，质量等级/库存状态
	//精确到日期 不考虑时间
	private String checkdata(GeneralInBodyVO body,BatchcodeVO bcodevo){
		StringBuilder s=new StringBuilder();
		UFDate dproducedate_148=(UFDate) body.getAttributeValue("dproducedate_148");
		if(!bcodevo.getDproducedate().toStdString().equals(dproducedate_148.toStdString())){
			s.append("批次生产日期").append(bcodevo.getDproducedate().toStdString());
			s.append("\n单据生产日期").append(dproducedate_148.toStdString());
		}
		
		//String vinvaliddate_148=(String) body.getAttributeValue("vinvaliddate_148");//失效日期
		UFDate billd=body.getDvalidate();
		UFDate dvalidate=bcodevo.getDvalidate();
		if(billd !=null){
			if(dvalidate!=null && !dvalidate.toStdString().equals(billd.toStdString())){
				s.append("\n批次失效日期").append(dvalidate.toStdString());
				s.append("\n单据失效日期").append(billd.toStdString());
			}
		}
		
		
		UFDate recheckdate_148=(UFDate) body.getAttributeValue("recheckdate_148");//复检日期
		UFDate pcrecheckdate_148=(UFDate) bcodevo.getAttributeValue("recheckdate_148");//复检日期
		if(pcrecheckdate_148==null && recheckdate_148==null){
		}else if(pcrecheckdate_148==null && recheckdate_148!=null){
			s.append("\n原批次复检日期为空，单据复检日期").append(recheckdate_148.toStdString());
			
		}else if(!pcrecheckdate_148.toStdString().equals(recheckdate_148.toStdString())){
			s.append("\n原批次复检日期").append(pcrecheckdate_148.toStdString());
			s.append("\n单据复检日期").append(recheckdate_148.toStdString());
		}
		
		String cstateid=(String) body.getAttributeValue("cstateid");	//库存状态
		
		
		String cqualitylevelid=(String) bcodevo.getAttributeValue("cqualitylevelid");	//质量等级
		
		
		if(cqualitylevelid==null){
			
		}else if(!cqualitylevelid.equals(cstateid)){
			//等级不一致 0001Z8100IC040080209
//			IGeneralAccessor acc=GeneralAccessorFactory.getAccessor("0001Z8100IC040080209");
//			IBDData bdvo= acc.getDocByPk(cstateid);
			
			SQLParameter sp =new SQLParameter();
			sp.addParam(cqualitylevelid);
			
			String name=(String)DBCacheFacadeFactory.getDBCacheFacade().runDynamicQuery("select vname from ic_storestate where pk_storestate=?",sp, new ColumnProcessor());
			sp.clearParams();
			sp.addParam(cstateid);
			
			String billname=(String)DBCacheFacadeFactory.getDBCacheFacade().runDynamicQuery("select vname from ic_storestate where pk_storestate=?",sp, new ColumnProcessor());
			sp.clearParams();
			s.append("\n原批次质量等级").append(name);
			
			s.append("\n单据库存状态").append(billname);
			
		}
		
		
		return s.toString();
			
	}
	
	
	private BatchcodeVO getBatchCode(String pk_material,String vbatchcode){
		String batchsql = "select * from scm_batchcode where isnull(dr,0)=0 and cmaterialvid=? and vbatchcode=? order by version desc ";
		SQLParameter sp =new SQLParameter();
		sp.addParam(pk_material);
		sp.addParam(vbatchcode);
		
		IUAPQueryBS bs =NCLocator.getInstance().lookup(IUAPQueryBS.class);
		BatchcodeVO batchcodevo=null;
		try {
			batchcodevo = (BatchcodeVO)bs.executeQuery(batchsql, sp,new BeanProcessor(BatchcodeVO.class));
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return batchcodevo;
		
	}

	@Override
	public void afterDoActionSuccessed(Action action, ActionEvent e) {
	
	}

	@Override
	public boolean afterDoActionFailed(Action action, ActionEvent e,
			Throwable ex) {
		
		return true;
	}

	public ICBizEditorModel getEditorModel() {
		return editorModel;
	}

	public void setEditorModel(ICBizEditorModel editorModel) {
		this.editorModel = editorModel;
	}


}
