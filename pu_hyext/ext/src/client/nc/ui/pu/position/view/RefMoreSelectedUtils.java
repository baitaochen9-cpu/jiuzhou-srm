package nc.ui.pu.position.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.view.BatchBillTable;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 列表多选工具类
 * 
 * @since 6.0
 * @version 2011-3-9 上午11:50:36
 * @author jinjya
 */
@SuppressWarnings("restriction")
public class RefMoreSelectedUtils {
  private BillListPanel billListPanel;

  private BatchBillTable editor;

  public RefMoreSelectedUtils(BillListPanel billListPanel) {
    this.billListPanel = billListPanel;
  }

  /**
   * 参照多选处理 - 主子表
   * 
   * @param currentRow
   * @param key
   * @param isAddNullLine
   *          为true则新增行，为false则复制行
   * @return void
   */
  public int[] refMoreSelected(int currentRow, String key, boolean isAddLine) {
    UIRefPane ref =
        (UIRefPane) this.billListPanel.getBodyItem(key).getComponent();
    String[] refPKs = ref.getRefPKs();

    if ((refPKs == null) || (refPKs.length <= 1)) {
      return new int[] {
        currentRow
      };
    }

    int oldSelectedRow =
        this.billListPanel.getBodyTable().getSelectedRow();

    this.insertLine(currentRow, refPKs.length - 1, isAddLine);

    List<Integer> addRowIndexes =
        this.getAddRowIndex(currentRow, refPKs.length);

    for (int i = 0; i < addRowIndexes.size(); i++) {
      this.updateRow(refPKs[i], addRowIndexes.get(i).intValue(), key);
    }

    int newSelectedRow =
        oldSelectedRow <= currentRow ? oldSelectedRow : oldSelectedRow
            + refPKs.length;
    this.billListPanel.getBodyTable().getSelectionModel()
        .setSelectionInterval(newSelectedRow, newSelectedRow);

    return this.toArray(addRowIndexes);
  }

  /**
   * 参照多选处理 - 单表 （需要注入editor，新增行时editor要注入addLineAction/insLineAction，
   * 复制行时editor要注入copyLineAction/pasteLineAction）
   * 
   * @param currentRow
   * @param key
   * @param isAddLine
   *          为true则新增行，为false则复制行
   * @return void
   */
  public void refMoreSelectedForBillTable(int currentRow, String key,
      boolean isAddLine) {
    UIRefPane ref =
        (UIRefPane) this.billListPanel.getBodyItem(key).getComponent();
    String[] refPKs = ref.getRefPKs();
    if ((refPKs == null) || (refPKs.length <= 1)) {
      return;
    }
    try {
      int oldSelectedRow =
          this.billListPanel.getHeadTable().getSelectedRow();

      BatchBillTableModel model =
          this.insertLineForBillTable(currentRow, refPKs.length - 1, isAddLine);

      List<Integer> addRowIndexes =
          this.getAddRowIndex(currentRow, refPKs.length);

      for (int i = 0; i < addRowIndexes.size(); i++) {
        this.updateRowForBillTable(model, refPKs[i], addRowIndexes.get(i)
            .intValue(), key);
      }

      int newSelectedRow =
          oldSelectedRow <= currentRow ? oldSelectedRow : oldSelectedRow
              + refPKs.length;
      model.setSelectedIndex(newSelectedRow);
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
  }

  private List<Integer> getAddRowIndex(int currentRow, int length) {
    List<Integer> indexes = new ArrayList<Integer>();
    for (int i = 0; i < length; i++) {
      indexes.add(Integer.valueOf(currentRow + i));
    }
    return indexes;
  }

  private void insertLine(int baseRow, int rowNum, boolean isAddLine) {
    if (isAddLine) {
   this.billListPanel.getBodyBillModel().addLine(rowNum);
      }      
  }

  private BatchBillTableModel insertLineForBillTable(int baseRow, int rowNum,
      boolean isAddLine) {
    BatchBillTableModel model = null;
    Action action = null;
    if (isAddLine) {
      int count =
          this.billListPanel.getHeadTable().getRowCount() - 1;
      if (baseRow == count) {
        action = this.editor.getAddLineAction();
        model = this.editor.getAddLineAction().getModel();
      }
      else {
        action = this.editor.getInsLineAction();
        model = this.editor.getInsLineAction().getModel();
      }
      model.setSelectedIndex(baseRow);
    }
    else {
      model = this.editor.getCopyLineAction().getModel();
      model.setSelectedIndex(baseRow);
      this.editor.getCopyLineAction().actionPerformed(null);
      action = this.editor.getPasteLineAction();
    }

    for (int i = 0; i < rowNum; i++) {
      action.actionPerformed(null);
    }
    return model;
  }

  private int[] toArray(List<Integer> rowList) {
    int[] rows = new int[rowList.size()];
    int length = rows.length;
    for (int i = 0; i < length; i++) {
      rows[i] = rowList.get(i).intValue();
    }
    return rows;
  }

  private void updateRow(String newRefPK, int currentRow, String key) {
    this.billListPanel.getBodyBillModel().setValueAt(newRefPK, currentRow, key);
    this.billListPanel.getBodyBillModel()
        .loadEditRelationItemValue(currentRow, key);
  }

  private void updateRowForBillTable(BatchBillTableModel model,
      String newRefPK, int currentRow, String key) {
    this.billListPanel.getHeadBillModel().setValueAt(newRefPK,currentRow, key);
    this.billListPanel.getHeadBillModel().setValueAt(newRefPK, currentRow, key
        + IBillItem.ID_SUFFIX);
    this.billListPanel.getHeadBillModel().loadLoadRelationItemValue(currentRow,
        key + IBillItem.ID_SUFFIX);

    Object obj = this.editor.getEditingLineVO(currentRow);
    model.updateLine(currentRow, obj);
  }


}
