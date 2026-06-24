/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package nc.ui.pu.m422x.action;

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
import nc.ui.pub.bill.BillItem;
import nc.ui.uif2.NCAction;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m422x.entity.StoreReqAppItemVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


public class DaoruAction extends NCAction {
	private static final long serialVersionUID = 4171051148975331842L;

	private nc.ui.pu.m422x.view.StoreReqBillForm editor;
	
//	private nc.ui.pu.m20.action.PraybillBodyAddLineAction addLineAction0;

	public nc.ui.pu.m422x.view.StoreReqBillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.pu.m422x.view.StoreReqBillForm editor) {
		this.editor = editor;
	}


	public DaoruAction() {
	    super.setBtnName("导入");
	    super.setCode("DaoruAction");
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub
		FeilPass filePas=new FeilPass();
		List<PraybillData> billDatas = filePas.FileOpen();
//		System.out.println(billDatas);
		
		String org_code=this.editor.getBillCardPanel().getHeadItem("pk_org").getValueObject().toString();
		PrayBillQueryMaterialSever lookup = NCLocator.getInstance().lookup(PrayBillQueryMaterialSever.class);
		String orgCode_v=lookup.orgQuery(org_code);
		
		Map unitMap=new HashMap<>();
		String[] codes=new String[billDatas.size()];
		for(int i=0;i<billDatas.size();i++){
			codes[i]=billDatas.get(i).getMaterialCode();
			unitMap.put(billDatas.get(i).getMaterialCode(), billDatas.get(i).getUnitId());
		}
		
		Map materialsMap = lookup.materialQuery(codes);
		
		unitMap=lookup.unitQuery(unitMap);
		
		Iterator iter = materialsMap.entrySet().iterator();
		List<StoreReqAppItemVO> list =  new ArrayList<StoreReqAppItemVO>();
		
		String returnMsg="";
		
		for(int i=0;i<billDatas.size();i++){
			String materialCode=billDatas.get(i).getMaterialCode();
			String unidId=billDatas.get(i).getUnitId();
			BigDecimal naskNum=billDatas.get(i).getNastNum();
			String materialId=(String)materialsMap.get(materialCode);
			String unitID=(String)unitMap.get(unidId);
			
			int rowNum=i+1;
			if(StringUtil.isEmpty(materialId)){
				returnMsg+=("第"+rowNum+"行 物料编码为："+materialCode+" 未在数据库中找到该物料信息、 ");
			}
			
//			StorereqItemVo item0=new StorereqItemVo();
			
			StoreReqAppItemVO billItem=new StoreReqAppItemVO();
			
//			PraybillItemVO billItem=new PraybillItemVO();
			billItem.setCrowno(rowNum+"");
			billItem.setPk_material(materialId);
			billItem.setPk_srcmaterial(materialId);
			billItem.setCastunitid(unitID);
			billItem.setNastnum(new UFDouble(naskNum));
			billItem.setNnum(new UFDouble(naskNum));
			billItem.setDreqdate(new UFDate());
			
			billItem.setPk_reqstoorg(org_code);
			billItem.setPk_reqstoorg_v(orgCode_v);
			billItem.setPk_nextbalanceorg(org_code);
			billItem.setPk_nextbalanceorg_v(orgCode_v);
			billItem.setCunitid(unitID);
			billItem.setNnum(new UFDouble(naskNum));
			
			list.add(billItem);
			
//			this.edi-tor.getBillCardPanel().setBodyValueAt(materialId, rowNum, "pk_material");
//			this.editor.getBillCardPanel().setBodyValueAt(unidId, rowNum, "castunitid");
//			this.editor.getBillCardPanel().setBodyValueAt(new UFDouble(naskNum), rowNum, "nastnum");
		}
		
		this.editor.getBillCardPanel().getBodyPanel().getTableModel().setBodyDataVO(list.toArray(new StoreReqAppItemVO[0]));
		
		if(StringUtil.isNotEmpty(returnMsg)){
//			MessageDialog.showHintDlg(editor.getModel().getContext().getEntranceUI(), "异常提示", returnMsg); 
			MessageDialog.showErrorDlg(editor.getModel().getContext().getEntranceUI(), "异常提示", returnMsg);
		}
	}
}