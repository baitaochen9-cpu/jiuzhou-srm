package nc.ui.pu.position.editor;


import nc.ui.pu.position.view.RefMoreSelectedUtils;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.view.handler.DefaultBillListEventHandler;
import nc.ui.scmpub.ref.FilterMaterialBaseClassRefUtils;
import nc.ui.scmpub.ref.FilterMaterialRefUtils;
import nc.ui.scmpub.ref.FilterMaterialoidRefUtils;
import nc.ui.scmpub.ref.FilterPsndocRefUtils;
import nc.vo.pu.position.entity.PositionHeaderVO;
import nc.vo.pu.position.entity.PositionItemVO;
import nc.vo.pu.pub.enumeration.PuNodeCode;
import nc.vo.uif2.LoginContext;
import nc.ui.pubapp.uif2app.view.BatchBillTable;

/**
 * 岗位设置编辑事件类
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>表头编辑后事件
 * <li>表体编辑前事件处理
 * </ul>
 * <p>
 * 
 * @version v60
 * @since v60
 * @author GGR
 * @time 2009-12-19 上午10:44:13
 */

public class PositionEventHandler extends DefaultBillListEventHandler {

  private LoginContext loginContext = null;
  // private String type;
  
  
  private BatchBillTable batchBillTable;

  @Override
  public void afterBodyEdit(BillListPanel listPanel, BillEditEvent e) {
    this.setclass(listPanel, e);
  }

  @Override
  public void afterHeadEdit(BillListPanel listPanel, BillEditEvent e) {
    // 方法未使用
  }

  @Override
  public boolean beforeBodyEdit(BillListPanel listPanel, BillEditEvent e) {
    this.setRefMode(listPanel, e);
    return true;
  }

  @Override
  public boolean beforeHeadEdit(BillListPanel listPanel, BillEditEvent e) {

    if (e.getKey().equals(PositionHeaderVO.CEMPLOYEEID)) {
      // 过滤人员
      if (PuNodeCode.n40010515.code().equals(this.loginContext.getNodeCode())) {
        FilterPsndocRefUtils filter =
            FilterPsndocRefUtils
                .createFilterPsndocRefUtilsOfIC((UIRefPane) listPanel
                    .getHeadItem(PositionHeaderVO.CEMPLOYEEID).getComponent());
        filter.filterItemRefByOrg(this.getContext().getPk_org());
      }
      else {
        FilterPsndocRefUtils filter =
            FilterPsndocRefUtils
                .createFilterPsndocRefUtilsOfPU((UIRefPane) listPanel
                    .getHeadItem(PositionHeaderVO.CEMPLOYEEID).getComponent());
        filter.filterItemRefByOrg(this.getContext().getPk_org());
      }
    }
    return true;
  }

  public LoginContext getContext() {
    return this.loginContext;
  }

  public void setContext(LoginContext loginContext) {
    this.loginContext = loginContext;
  }

  // private String getType() {
  // if (null == this.type) {
  // this.type = PUSysParamUtil.getPO85(this.getContext().getPk_group());
  // }
  //
  // return this.type;
  // }

  private void clearMaterialInfo(BillListPanel listPanel, BillEditEvent e) {
    listPanel.getBodyBillModel().setValueAt(null, e.getRow(),
        PositionItemVO.PK_MATERIAL);
    listPanel.getBodyBillModel().setValueAt(null, e.getRow(),
        PositionItemVO.PK_SRCMATERIAL);
    listPanel.getBodyBillModel().setValueAt(null, e.getRow(),
        PositionItemVO.MATERIAL_CODE);
    listPanel.getBodyBillModel().setValueAt(null, e.getRow(),
        PositionItemVO.PK_SRCMATERIAL + ".name");
  }

  @SuppressWarnings("restriction")
private void setclass(BillListPanel listPanel, BillEditEvent e) {  
	  
	  /*20230105 yezhian 重构编辑处理  begin*/
    // 物料修改后设置物料分类
    	if (e.getKey().equals(PositionItemVO.PK_SRCMATERIAL)) {
    		//专项：物料多选后处理
       	 RefMoreSelectedUtils utils = new RefMoreSelectedUtils(listPanel);    	
    	    int  editrow = e.getRow();
    	    utils.refMoreSelected(editrow, e.getKey(), true); 
//      nc.ui.pub.bill.BillModel billModel = listPanel.getBodyBillModel();
      // 采购分类时 清空基本分类，同时设置采购分类
//      billModel.setValueAt(null, e.getRow(), PositionItemVO.PK_MARBASCLASS);
//      if (PuNodeCode.n40010520.code().equals(this.getContext().getNodeCode())) {
//        int col = billModel.getBodyColByKey(PositionItemVO.PK_MARPUCLASS);
//        billModel.setValueAt(null, e.getRow(), col);
//
//        billModel.loadLoadRelationItemValue(e.getRow(),
//            PositionItemVO.PK_MARPUCLASS);
//
//      }

      // 刷新行
//      billModel.loadEditRelationItemValue(e.getRow(),
//          PositionItemVO.PK_MARBASCLASS);
//
//      billModel.loadEditRelationItemValue(e.getRow(),
//          PositionItemVO.PK_MARPUCLASS);
    }

    // 物料分类修改后 ，清空物料
    if (e.getKey().equals(PositionItemVO.PK_MARBASCLASS)) {
//      this.clearMaterialInfo(listPanel, e);
    	 RefMoreSelectedUtils utils = new RefMoreSelectedUtils(listPanel);    	
 	    int  editrow = e.getRow();
 	    utils.refMoreSelected(editrow, e.getKey(), true); 

    }
    /*20230105 yezhian 重构编辑处理  end*/

    // 物料采购分类修改后 ，清空物料
    if (e.getKey().equals(PositionItemVO.PK_MARPUCLASS)) {
      this.clearMaterialInfo(listPanel, e);
    }
  }

  private void setRefMode(BillListPanel listPanel, BillEditEvent e) {
    // 物料基本分类
    if (e.getKey().equals(PositionItemVO.PK_MARBASCLASS)) {

    	
      FilterMaterialBaseClassRefUtils filter =
          new FilterMaterialBaseClassRefUtils((UIRefPane) listPanel
              .getBodyItem(PositionItemVO.PK_MARBASCLASS).getComponent());
      /*20230105 yezhian 设置物料分类多选 begin*/
      UIRefPane refpn =  (UIRefPane) listPanel
      .getBodyItem(PositionItemVO.PK_MARBASCLASS).getComponent();
      	refpn.setMultiSelectedEnabled(true);
      	/*20230105 yezhian 设置物料分类多选 end*/
//    		  listPanel.getBodyItem(PositionItemVO.PK_MARBASCLASS).setComponent(refpn);
      filter.filterItemRefByOrg(this.getContext().getPk_org());
      
    }

    // 物料采购分类
    if (e.getKey().equals(PositionItemVO.PK_MARPUCLASS)) {
      UIRefPane pane =
          (UIRefPane) listPanel.getBodyItem(PositionItemVO.PK_MARPUCLASS)
              .getComponent();
      if (pane != null && pane.getRefModel() != null) {
        pane.getRefModel().setPk_org(this.getContext().getPk_org());
      }
    }

    // 物料
    if (e.getKey().equals(PositionItemVO.PK_MATERIAL)) {
      FilterMaterialRefUtils filter =
          new FilterMaterialRefUtils((UIRefPane) listPanel.getBodyItem(
              PositionItemVO.PK_MATERIAL).getComponent());
      filter.filterItemRefByOrg(this.getContext().getPk_org());
    }
    // 物料oid
     FilterMaterialoidRefUtils filter =
          new FilterMaterialoidRefUtils((UIRefPane) listPanel.getBodyItem(
              PositionItemVO.PK_SRCMATERIAL).getComponent());
      filter.filterItemRefByOrg(this.getContext().getPk_org());
      //专项：支持物料多选 20170914  by fangmj7 
      filter.setMultiSelectedEnabled(true);
    }

public BatchBillTable getBatchBillTable() {
	return batchBillTable;
}

public void setBatchBillTable(BatchBillTable batchBillTable) {
	this.batchBillTable = batchBillTable;
}
  }


