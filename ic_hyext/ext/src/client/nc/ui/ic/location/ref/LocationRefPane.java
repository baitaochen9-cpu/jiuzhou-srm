package nc.ui.ic.location.ref;

import java.awt.Container;
import java.awt.event.FocusEvent;

import nc.ui.ic.location.view.ICLocationEnum;
import nc.ui.ic.pub.env.ICUIContext;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.ic.onhand.entity.OnhandSNVO;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.ic.pub.util.ValueCheckUtil;

/**
 * <p>
 * <b>单品参照：</b>
 * <ul>
 * <li>数据来自单品结存
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author yangb
 * @time 2010-8-28 上午12:24:13
 */
public class LocationRefPane extends UIRefPane {

  private static final long serialVersionUID = 1L;

  private ICUIContext context;

  // 是否打开参照对话框选择了数据
  private boolean isClicked = false;

  private LocationRefType locRefType;

  private OnhandSNViewVO param;

  private LocationRefDlg refDlg;

  private ICLocationEnum locationEnum;
  
  //是否是表体序列号
  private boolean bBodySerial;
//  是否材料出
  private boolean isMaterialOut;
  
  private ISerialCodeDialogSortListner sortrule;
  
  private ISerialCodeDialogInitListener initListener;
  
  

  public boolean isMaterialOut() {
	return isMaterialOut;
}

public void setMaterialOut(boolean isMaterialOut) {
	this.isMaterialOut = isMaterialOut;
}

public boolean isbBodySerial() {
	return bBodySerial;
}

public void setbBodySerial(boolean bBodySerial) {
	this.bBodySerial = bBodySerial;
}

/**
   * SerailRefPane 的构造子
   * 
   * @param parent
   */
  public LocationRefPane(
      Container parent, ICUIContext con, LocationRefType type,
      ICLocationEnum locationEnum) {
    super(parent);
    this.locRefType = type;
    this.context = con;
    this.locationEnum = locationEnum;
    this.init();
  }

  public void clearResultVOs() {
    this.getLocationRefDlg().clearSelectedRows();
  }

  /**
   * @return param
   */
  public OnhandSNViewVO getParam() {
    return this.param;
  }

  public OnhandSNViewVO[] getResultVOs() {
    return this.getLocationRefDlg().getSelectedVOs();
  }

  /**
   * @return isClicked
   */
  public boolean isClicked() {
    return this.isClicked;
  }

  @Override
  public void onButtonClicked() {

    this.isClicked = false;
    if (this.getParam() == null) {
      return;
    }
    if(isMaterialOut){
    	 this.getLocationRefDlg().setMaterialOut(isMaterialOut);
    }
    
    this.getLocationRefDlg().setbBodySerial(bBodySerial);
    this.getLocationRefDlg().setParam(this.getParam());
    this.getLocationRefDlg().setSortrule(this.getSortrule());
    this.getLocationRefDlg().setInitListener(this.getInitListener());
    this.getLocationRefDlg().loadData();
    if (this.getLocationRefDlg().showModal() == UIDialog.ID_OK) {
      OnhandSNViewVO[] refVOs = this.getLocationRefDlg().getSelectedVOs();
      if (ValueCheckUtil.isNullORZeroLength(refVOs)) {
		this.getUITextField().setText(null);
        return;
      }
      String code = null;
      if (this.locRefType == LocationRefType.Serial) {
        code = refVOs[0].getVsncode(); 
      }
      else {
        code = refVOs[0].getVbarcode();
      }
      this.getUITextField().setText(code);
      this.isClicked = true;
    }
    else {
      this.isClicked = false;
    }
    this.getUITextField().setRequestFocusEnabled(true);
    this.getUITextField().grabFocus();
  }

  /**
   * @param param
   *          要设置的 param
   */
  public void setParam(OnhandSNViewVO param) {
    this.param = param;
  }

  private LocationRefDlg getLocationRefDlg() {
    if (this.refDlg == null) {
      this.refDlg = new LocationRefDlg(this, this.context, this.locationEnum);
    }
    return this.refDlg;
  }

  private void init() {
    if (this.locRefType == LocationRefType.Location) {
      this.setReturnCode(false);
      this.getUITextField().setEditable(false);
    }
    else {
      this.setReturnCode(true);
      this.getUITextField().setEditable(true);
      this.getUITextField().setMaxLength(
          new OnhandSNVO().getMetaData().getAttribute(OnhandSNVO.VSNCODE)
              .getColumn().getLength());
    }
    return;
  }

  /**
   * 
   */
  @Override
  protected void processFocusGained(FocusEvent e1) {
    super.processFocusGained(e1);
    this.isClicked = false;
  }
  
  public ISerialCodeDialogSortListner getSortrule() {
	return sortrule;
  }

  public void setSortrule(ISerialCodeDialogSortListner sortrule) {
	this.sortrule = sortrule;
  }

  public ISerialCodeDialogInitListener getInitListener() {
	return initListener;
  }

  public void setInitListener(ISerialCodeDialogInitListener initListener) {
	this.initListener = initListener;
  }
}
