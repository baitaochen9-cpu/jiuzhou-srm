/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package nc.ui.pu.m20.action;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.coremedia.iso.boxes.CompositionTimeToSample.Entry;


import nc.bs.framework.common.NCLocator;
import nc.bs.ia.pub.VOQryUtil;
import nc.pubitf.pu.m21.pub.PrayBillQueryMaterialSever;
import nc.cmp.tools.StringUtil;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.NCAction;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m21.entity.Stud;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;


public class CopyOfPraybillApproveAction extends NCAction {
	private static final long serialVersionUID = 4171051148975331842L;

	private nc.ui.pu.m20.view.PraybillBillForm editor;
	
	private nc.ui.pu.m20.action.PraybillBodyAddLineAction addLineAction0;

	public nc.ui.pu.m20.view.PraybillBillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.pu.m20.view.PraybillBillForm editor) {
		this.editor = editor;
	}

	public nc.ui.pu.m20.action.PraybillBodyAddLineAction getAddLineAction0() {
		return addLineAction0;
	}

	public void setAddLineAction0(
			nc.ui.pu.m20.action.PraybillBodyAddLineAction addLineAction0) {
		this.addLineAction0 = addLineAction0;
	}

	public CopyOfPraybillApproveAction() {
		  
//	    super();
	    super.setBtnName("导入");
//	    action.setBtnName(info.getName());
	    /* 26 */     super.setCode("CopyOfPraybillApproveAction");
//	    SCMActionInitializer.initializeAction(this, SCMActionCode.SCM_VERIFY);
	  }
	
	@SuppressWarnings("restriction")
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub
		FeilPass filePas=new FeilPass();
		List<PraybillData> billDatas = filePas.FileOpen();
		System.out.println(billDatas);
		
//		Iterator<Integer> iterator=dataMap.keySet().iterator();
//        while (dataMap.keySet().iterator().hasNext()){
//            Integer key=iterator.next();
////            System.out.println(key);
//            PraybillData billData=(PraybillData)dataMap.get(key);
//            System.out.println("code:"+billData.getCode());
//            codes.add(billData.getCode());
//        }
//		CircularlyAccessibleValueObject bodyRowVO=
//		addLineAction0.setCardPanel(editor.getBillCardPanel());
//		
//		addLineAction0.setModel(editor.getModel());
//		
//		addLineAction0.doAction();addLineAction0.doAction();addLineAction0.doAction();
		
		PrayBillQueryMaterialSever lookup = NCLocator.getInstance().lookup(PrayBillQueryMaterialSever.class);
		
		String[] codes=new String[billDatas.size()];
		for(int i=0;i<billDatas.size();i++){
			codes[i]=billDatas.get(i).getMaterialCode();
		}
		
		Map materialsMap = lookup.materialQuery(codes);
		Iterator iter = materialsMap.entrySet().iterator();
		List<PraybillItemVO> list =  new ArrayList<PraybillItemVO>();
		
		String returnMsg="";
		
		for(int i=0;i<billDatas.size();i++){
			String materialCode=billDatas.get(i).getMaterialCode();
			String unidId=billDatas.get(i).getUnitId();
			BigDecimal naskNum=billDatas.get(i).getNastNum();
			String materialId=(String)materialsMap.get(materialCode);
			
			int rowNum=i+1;
			if(StringUtil.isEmpty(materialId)){
				returnMsg+=("第"+rowNum+"行 物料编码为："+materialCode+" 未在数据库中找到该物料信息、 ");
			}
			
			PraybillItemVO billItem=new PraybillItemVO();
			billItem.setCrowno(rowNum+"");
			billItem.setPk_material(materialId);
			billItem.setCastunitid(unidId);
			billItem.setNastnum(new UFDouble(naskNum));
			list.add(billItem);
		}
		
		this.editor.getBillCardPanel().getBodyPanel().getTableModel().setBodyDataVO(list.toArray(new PraybillItemVO[0]));
		
		if(StringUtil.isNotEmpty(returnMsg)){
			MessageDialog.showHintDlg(editor.getModel().getContext().getEntranceUI(), "异常提示", returnMsg); 
		}
	}
}