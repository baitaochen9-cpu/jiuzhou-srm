package nc.ui.bd.material.baseinfo.editor;

import nc.bs.framework.common.NCLocator;
import nc.individuation.property.itf.IPropertyService;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.vo.pub.BusinessException;

public class MaterialBaseInfoEditorForjiuzhou extends MaterialBaseInfoEditor {

	@Override
	public void initUI() {
		// TODO Auto-generated method stub
		super.initUI();
		initRefModel_Def1();
	}

	private void initRefModel_Def1() {
		// TODO Auto-generated method stub
		 @SuppressWarnings("restriction")
		BillItem headItem = getBillCardPanel().getHeadItem("def1");
		 @SuppressWarnings("restriction")
		UIRefPane refPane = (UIRefPane)headItem.getComponent();
		 @SuppressWarnings("restriction")
		String pk_group = this.getModel().getContext().getPk_group();
		 @SuppressWarnings("restriction")
		String pk_loginUser = this.getModel().getContext().getPk_loginUser();
		 IPropertyService lookup = NCLocator.getInstance().lookup(IPropertyService.class);
		 String biz_org = pk_group;
		 try {
			 biz_org = lookup.queryDefaultDBizOrg(pk_loginUser, pk_group, "org_df_biz");
		} catch (BusinessException e) {
			// 这里异常不处理，如果发生异常时，保留原集团ID
			e.printStackTrace();
		}
		 refPane.getRefModel().setWherePart(" enablestate = 2 ) and ( ( pk_org = '"+biz_org+"' or pk_org ='"+pk_group+"' ) and pk_defdoclist = '1001V11000000000AGM2' ) order by code --	 ");

	}
}
