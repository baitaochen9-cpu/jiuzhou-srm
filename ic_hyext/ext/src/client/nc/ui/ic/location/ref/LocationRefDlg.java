package nc.ui.ic.location.ref;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.pubitf.sn.doc.param.SnDocQueryParam;
import nc.ui.ic.location.handler.LocationRefInitListener;
import nc.ui.ic.location.view.ICLocationEnum;
import nc.ui.ic.material.query.InvInfoUIQuery;
import nc.ui.ic.pub.env.ICUIContext;
import nc.ui.ic.pub.util.ShowUtil;
import nc.ui.ic.pub.view.ActionPanel;
import nc.ui.ic.pub.view.ICBaseDialog;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.textfield.UITextType;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.style.Style;
import nc.vo.ic.location.ICLocationVO;
import nc.vo.ic.material.query.InvInfoQuery;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandSNVO;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.onhand.pub.OnhandVOTools;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.DimMatchedObj;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.sncode.SnCodeUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletHeadVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.sn.doc.entity.SerialNoVO;

/**
 * <p>
 * <b>单品参照对话框：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author yangb
 * @time 2010-4-9 下午03:01:52
 */
public class LocationRefDlg extends ICBaseDialog implements ActionListener {

  private static final String ROW_SEL = "selected";

  private static final long serialVersionUID = 1L;

  private static final String SNFROM = "vserialcodefrom";

  private static final String SNTO = "vserialcodeto";

  private static final String VINPUTBARCODE = "vinputbarcode";

  private BillCardPanel barcodePanel;

  private ICUIContext context;

  private BillCardPanel dataPanel;
  
  private UIPanel pnl_page;
  
  private UIButton btnNextPage = null;
  
  private UILabel lb_pageInfo = null;
  
  private UILabel lb_pagecountInfo = null;
  
  private UIButton btnPreviousPage = null;
  
  private UITextField pagecountTF = null;
  
  private int currentPage = 0;
  
  private int defaultPageCount = 100;

  private OnhandSNViewVO param;

  private ICLocationEnum locationEnum;
  

//  private Map<String, OnhandSNViewVO> resultVOsMap;
  
  private MapList<String, OnhandSNViewVO> pageData =
	      new MapList<String, OnhandSNViewVO>();
  
  // 选择的序列号
  private MapList<String, OnhandSNViewVO> selVOMap = new MapList<String, OnhandSNViewVO>();

  //是否是表体序列号
  private boolean bBodySerial;
  
  private boolean isMaterialOut;
  
  public boolean isMaterialOut() {
	return isMaterialOut;
}

public void setMaterialOut(boolean isMaterialOut) {
	this.isMaterialOut = isMaterialOut;
}

private ISerialCodeDialogSortListner sortrule;
  
  private ISerialCodeDialogInitListener initListener = new LocationRefInitListener();
  
  public LocationRefDlg(
      Container parent, ICUIContext context, ICLocationEnum locationEnum) {
    super(parent);
    this.context = context;
    this.locationEnum = locationEnum;
    this.initUI();
    if (this.context == null) {
      return;
    }
  }

  public void clearSelectedRows() {
    for (int row = 0; row < this.getDataPanel().getRowCount(); row++) {
      this.getDataPanel().setBodyValueAt(UFBoolean.FALSE, row,
          LocationRefDlg.ROW_SEL);
    }
    this.selVOMap.toMap().clear();
  }

//  public OnhandSNViewVO[] getSelectedVOs() {
//    List<OnhandSNViewVO> l = new ArrayList<OnhandSNViewVO>();
//    UFBoolean bsel = null;
//    OnhandSNViewVO vo = null;
//	  String key="";
//	  if (SysInitGroupQuery.isSNEnabled()) {
//		  key=ICPubMetaNameConst.PK_SERIALCODE;
//	  }else{
//		  key=OnhandSNVO.VSNCODE;
//	  }
//    for (int row = 0; row < this.getDataPanel().getRowCount(); row++) {
//      bsel =
//          ValueUtils.getUFBoolean(this.getDataPanel().getBodyValueAt(row,
//              LocationRefDlg.ROW_SEL));
//      if (ValueCheckUtil.isTrue(bsel)) {
//        vo =
//            this.resultVOsMap.get(this.getDataPanel().getBodyValueAt(row,key));
//        if (vo != null) {
//          l.add(vo);
//          continue;
//        }
//        vo =
//            this.resultVOsMap.get(this.getDataPanel().getBodyValueAt(row,
//                OnhandSNVO.VBARCODE));
//        if (vo != null) {
//          l.add(vo);
//        }
//      }
//    }
//    if (l.size() <= 0) {
//      return null;
//    }
//    return l.toArray(new OnhandSNViewVO[l.size()]);
//  }
  public OnhandSNViewVO[] getSelectedVOs() {
	  if (ValueCheckUtil.isNullORZeroLength(this.selVOMap.toMap())) {
		  return null;
	  }
	  
	  List<OnhandSNViewVO> result = new ArrayList<OnhandSNViewVO>();
	    for (Map.Entry<String, List<OnhandSNViewVO>> entry : this.selVOMap
	            .entrySet()) {
	    	result.addAll(entry.getValue());
	        }
	  return CollectionUtils.listToArray(result);
  }

  public void setSelectedVOsCache() {
	UFBoolean bsel = null;
	OnhandSNViewVO vo = null;
	String key = SysInitGroupQuery.isSNEnabled() ? ICPubMetaNameConst.PK_SERIALCODE : OnhandSNVO.VSNCODE;
	this.selVOMap.remove(String.valueOf(this.currentPage));
	List<OnhandSNViewVO> showData = this.pageData.get(String.valueOf(this.currentPage));
	Map<String, OnhandSNViewVO> pagedata = this.hashVObykey(key, showData);
	
	for (int row = 0; row < this.getDataPanel().getRowCount(); row++) {
		bsel = ValueUtils.getUFBoolean(this.getDataPanel().getBodyValueAt(
				row, LocationRefDlg.ROW_SEL));
		if (ValueCheckUtil.isTrue(bsel)) {
			vo = pagedata.get(this.getDataPanel().getBodyValueAt(
					row, key));
			if (vo != null) {
				this.selVOMap.put(String.valueOf(this.currentPage), vo);
			}
//			vo = this.resultVOsMap.get(this.getDataPanel().getBodyValueAt(
//					row, OnhandSNVO.VBARCODE));
//			if (vo != null) {
//				this.selVOMap.put((String)this.getDataPanel().getBodyValueAt(
//						row, key), vo);
//			}
		}
	}
  }

private Map<String, OnhandSNViewVO> hashVObykey(String key, List<OnhandSNViewVO> showData) {
	if (ValueCheckUtil.isNullORZeroLength(showData)) {
		return new HashMap<String, OnhandSNViewVO>();
	}
	Map<String, OnhandSNViewVO> resultmap = new HashMap<String, OnhandSNViewVO>();
	for (OnhandSNViewVO snvo : showData) {
		resultmap.put((String)snvo.getAttributeValue(key),snvo);
	}
	return resultmap;
}
  
  

  /**
   * 父类方法重写
   * 
   * @see nc.ui.ic.pub.view.ICBaseDialog#initUI()
   */
  @Override
  public void initUI() {
    this.setModal(true);
    this.setName("LocationRef");
    this.setSize(600, 450);
    this.setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4008001_0", "04008001-0088")/*@res "单品参照"*/);
    super.initUI();
  }

  @Override
  protected UIPanel getUIActionPanel() {
    if (this.invUICmdPane == null) {
      ActionPanel actionPane = (ActionPanel) super.getUIActionPanel();
     // actionPane.addAction(new SplitAction(this));
     // actionPane.addAction(new WeightDiffOutAction(this));
      //actionPane.addAction(new WeightDifInAction(this));
      actionPane.initUI();
    }
    return super.getUIActionPanel();
  }
  public void loadData() {
    try {
    	
    	this.currentPage = 1;
    	this.selVOMap.toMap().clear();
    	this.pageData.toMap().clear();
    	this.setLbPageInfo();
      OnhandSNViewVO[] datavos = this.queryVOs(this.currentPage);
      this.showData(datavos);
    }
    catch (Exception ex) {
      Throwable et = ExceptionUtils.unmarsh(ex);
      ShowUtil.showErrorDlg(this, et.getMessage());
    }
  }

  private void fillCsnqualitylevelid (OnhandSNViewVO[] datavos) {
	  if (!SysInitGroupQuery.isSNEnabled() || ValueCheckUtil.isNullORZeroLength(datavos)) {
		  return;
	  }
	  List<SnDocQueryParam> paramlist = new ArrayList<SnDocQueryParam>();
	  for (OnhandSNViewVO onhandsn : datavos) {
		  SnDocQueryParam param = new SnDocQueryParam();
		  param.setCmaterialvid(onhandsn.getCmaterialvid());
		  param.setSncode(onhandsn.getVsncode());
		  param.setPk_sn(onhandsn.getPk_serialcode());
		  paramlist.add(param);
	  }
	  Map<String, SerialNoVO> snmap = SnCodeUtil.querySerailnoMap(paramlist);
	  if (ValueCheckUtil.isNullORZeroLength(snmap)) {
		  return;
	  }
	  for (OnhandSNViewVO onhandsn : datavos) {
		  String key = StringUtil.mergeTowString(onhandsn.getCmaterialvid(), onhandsn.getVsncode());
		  SerialNoVO snvo = snmap.get(key);
		  if (snvo == null) {
			  continue;
		  }
		  onhandsn.setAttributeValue(ICPubMetaNameConst.CSNQUALITYLEVELID, snvo.getCqualitylevelid());
	  }
  }
  
  public void setParam(OnhandSNViewVO param) {
    this.param = param;
  }

  private void clearUI() {
    this.getDataPanel().getBillData().clearViewData();
    this.getBarCodePanel().getBillData().clearViewData();
  }

//  private OnhandSNViewVO[] filterShowData(OnhandSNViewVO[] snvos) {
//    if (ValueCheckUtil.isNullORZeroLength(snvos)) {
//      return snvos;
//    }
//    List<OnhandSNViewVO> lvos = new ArrayList<OnhandSNViewVO>();
//    for (OnhandSNViewVO vo : snvos) {
//      if (NCBaseTypeUtils.isLEZero(vo.getNonhandnum())) {
//        continue;
//      }
//      lvos.add(vo);
//    }
//    if (lvos.size() <= 0) {
//      return null;
//    }
//    return lvos.toArray(new OnhandSNViewVO[lvos.size()]);
//  }

  /**
   * 条码数据的面板
   */
  private BillCardPanel getBarCodePanel() {
    if (this.barcodePanel == null) {
      BillTempletVO newTempletVO = new BillTempletVO();

      BillTempletHeadVO headVO = new BillTempletHeadVO();
      headVO.setBillTempletCaption("");
      newTempletVO.setParentVO(headVO);
      List<BillTempletBodyVO> lbodyVO = new ArrayList<BillTempletBodyVO>();

      BillTempletBodyVO bi =
          this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4008001_0", "04008001-0089")/*@res "已选数量："*/,
              ICLocationVO.NNUM, 0, false, IBillItem.INTEGER, 1, 20, false);

      lbodyVO.add(bi);

      bi =
          this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4008001_0", "04008001-0090")/*@res "请输入条形码："*/,
              LocationRefDlg.VINPUTBARCODE, 1, false, IBillItem.STRING, 1, 40,
              true);
      lbodyVO.add(bi);

      newTempletVO.setChildrenVO(lbodyVO.toArray(new BillTempletBodyVO[lbodyVO
          .size()]));

      this.barcodePanel = new BillCardPanel();
      this.barcodePanel.setBillData(new BillData(newTempletVO));
      this.barcodePanel.setName("BarCodeInfo");
      this.barcodePanel.addNew();
      this.barcodePanel.getBillData().setEnabled(true);

      this.barcodePanel.addEditListener(new BillEditListener() {
        @Override
        public void afterEdit(BillEditEvent e) {
          LocationRefDlg.this.afterBarCodePanelEdit(e);
        }

        @Override
        public void bodyRowChange(BillEditEvent e) {
          // 目前不需要实现
          e.getKey();
        }
      });
    }

    return this.barcodePanel;
  }

  public void afterInputEnd(KeyEvent e) {
    e.getSource();
  }

  /**
   *
   */
  private BillTempletBodyVO getBillTempletBodyVO(String name, String key,
      int showorder, boolean isbody, int itype, int iwidth, int inputlen,
      boolean bedit) {
    BillTempletBodyVO bi = new BillTempletBodyVO();
    if (isbody) {
      bi.setPos(Integer.valueOf(IBillItem.BODY));
    }
    else {
      bi.setPos(Integer.valueOf(IBillItem.HEAD));
    }
    bi.setDefaultshowname(name);
    bi.setDatatype(Integer.valueOf(itype));
    bi.setInputlength(Integer.valueOf(inputlen));
    bi.setEditflag(bedit ? Boolean.TRUE : Boolean.FALSE);
    bi.setItemkey(key);
    bi.setWidth(Integer.valueOf(iwidth));
    bi.setShoworder(Integer.valueOf(showorder));
    bi.setCardflag(Boolean.TRUE);
    bi.setListflag(Boolean.FALSE);
    return bi;
  }
  
  private UIPanel getDataPanelWithPageButton () {
	  UIPanel maindatapanel = new UIPanel();

	  maindatapanel.setName("LocDataPanel");
	  maindatapanel.setLayout(new BorderLayout());
	  maindatapanel.add(this.getDataPanel(), BorderLayout.CENTER);
	  maindatapanel.add(this.getPagePanel(), BorderLayout.SOUTH);
	  return maindatapanel;
  }
  
  /**
   * 分页按钮
   * 
   * @return
   */
  private UIPanel getPagePanel () {
	  if (this.pnl_page == null) {
			pnl_page = new UIPanel();
			pnl_page.setName("pnl_page");

			pnl_page.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 8));
			//pnl_page.setBorder(BorderFactory.createLineBorder(Color.BLUE));

			pnl_page.add(getLbPageCountInfo(), getLbPageCountInfo().getName());
			pnl_page.add(getPageNumText(), getPageNumText().getName());
			pnl_page.add(getLbPageInfo(), getLbPageInfo().getName());
			pnl_page.add(getBtnPreviousPage(), getBtnPreviousPage().getName());
			pnl_page.add(getBtnNextPage(), getBtnNextPage().getName());
			pnl_page.setBackground(Style.getDlgBgColor());
			pnl_page.setPreferredSize(new java.awt.Dimension(350, 30));
		}
	  return this.pnl_page;
  }
  
  private UITextField getPageNumText() {
	  if (pagecountTF == null) {
		  pagecountTF = new UITextField();
		  pagecountTF.setName("pagesize");
		  pagecountTF.setTextType(UITextType.TextInt);
		  pagecountTF.setText(String.valueOf(defaultPageCount));
		  pagecountTF.setOpaque(false);
		  pagecountTF.addActionListener(this);
	  }
	  return pagecountTF;
  }
  
  private UIButton getBtnNextPage() {

		if (btnNextPage == null) {

			String name = "btnNextPage";

			String text = NCLangRes.getInstance().getStrByID("ref",
					"RefButtonPanelFactory-000002")/*下一页*/;
			btnNextPage = new UIButton(); 
			btnNextPage.setText(text);
			btnNextPage.setName(name);
			btnNextPage.setToolTipText(NCLangRes.getInstance().getStrByID(
					"ref", "RefButtonPanelFactory-000003")/*下一页数据*/);
			// btnNextPage.setPreferredSize(new Dimension(57, 20));
			//btnNextPage.setIconTextGap(-48);
			btnNextPage.setIcon(Style.getImage("参照.下一页"));/*-=notranslate=-*/
			btnNextPage.addActionListener(this);
			setBtnSize(btnNextPage);

		}

		return btnNextPage;
	}
  
	private void setBtnSize(UIButton button) {
		int width = getScreenWidth(button.getText());
		button.setPreferredSize(new Dimension(width, 20));
	}

	private int getScreenWidth(String text) {
		int defaultLen = 48;
		int textLen = defaultLen;

		if (text != null) {

			textLen = text.getBytes().length * 10;
		}

		return textLen >= defaultLen ? textLen : defaultLen;
	}
	
	private nc.ui.pub.beans.UIButton getBtnPreviousPage() {

	if (btnPreviousPage == null) {

		String name = "btnPreviousPage";

		String text = NCLangRes.getInstance().getStrByID("ref",
				"RefButtonPanelFactory-000004")/*上一页*/;
		btnPreviousPage = new UIButton(); 
		btnPreviousPage.setText(text);
		btnPreviousPage.setName(name);
		btnPreviousPage.setToolTipText(NCLangRes.getInstance().getStrByID(
				"ref", "RefButtonPanelFactory-000005")/*上一页数据*/);
		btnPreviousPage.setIcon(Style.getImage("参照.上一页"));/*-=notranslate=-*/
		btnPreviousPage.addActionListener(this);
		setBtnSize(btnPreviousPage);

	}

	return btnPreviousPage;
  }
	
	/**
	 * @return 返回 lbLocate。
	 */
  private nc.ui.pub.beans.UILabel getLbPageCountInfo() {
		if (lb_pagecountInfo == null) {
	
			lb_pagecountInfo = new nc.ui.pub.beans.UILabel() {
				@Override
				public void setText(String text) {
					// TODO Auto-generated method stub
					super.setText(text);
					setPreferredSize(new java.awt.Dimension(
							getScreenWidth(text) - 20, 20));
				}
			};
			lb_pagecountInfo.setName("lb_pagecountInfo");
			String text =
					NCLangRes.getInstance().getStrByID("4008001_0",
							"04008001-0907", "", null);
			lb_pagecountInfo.setText(text);
	
		}
		return lb_pagecountInfo;
	}
	
  
	/**
	 * @return 返回 lbLocate。
	 */
  private nc.ui.pub.beans.UILabel getLbPageInfo() {
		if (lb_pageInfo == null) {
	
			lb_pageInfo = new nc.ui.pub.beans.UILabel() {
				@Override
				public void setText(String text) {
					// TODO Auto-generated method stub
					super.setText(text);
					setPreferredSize(new java.awt.Dimension(
							getScreenWidth(text) - 20, 20));
				}
			};
			lb_pageInfo.setName("lb_pageInfo");
			String text =
					NCLangRes.getInstance().getStrByID("ref",
							"UFRefGridTreeCommDataUI-000000", "", new String[] {
							String.valueOf("1")
					});
			lb_pageInfo.setText(text);
	
		}
		return lb_pageInfo;
	}
	
	  private void setLbPageInfo () {
			String text =
					NCLangRes.getInstance().getStrByID("ref",
							"UFRefGridTreeCommDataUI-000000", "", new String[] {
							String.valueOf(this.currentPage)
					});
		  this.getLbPageInfo().setText(text);
		  this.getPageNumText().setText(String.valueOf(this.defaultPageCount));
	  }
	  
	  private void setBtnPageEnabled (int count) {
		  if (this.currentPage == 1) {
			  this.getBtnPreviousPage().setEnabled(false);
		  }else {
			  this.getBtnPreviousPage().setEnabled(true);
		  }
		  if (count < defaultPageCount) {
			  this.getBtnNextPage().setEnabled(false);
		  } else if (count == defaultPageCount) {
			  this.getBtnNextPage().setEnabled(this.isNextPageEnabled());
		  } else {
			  this.getBtnNextPage().setEnabled(true);
		  }
	  }

	private boolean isNextPageEnabled() {
		if (this.pageData.containsKey(String.valueOf(this.currentPage + 1))) {
			return true;
		}
		OnhandSNViewVO[] result = null;
		try {
			result = this.queryVOs(this.currentPage + 1); 
			if (ValueCheckUtil.isNullORZeroLength(result)) {
				return false;
			}
			// 缓存起来，防止下次查询
		  pageData.remove(String.valueOf(this.currentPage+1));
		  pageData.putAll(String.valueOf(this.currentPage+1), Arrays.asList(result));
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return true;
	}

  /**
   * 显示参照数据的面板，包括序列号设置区，在表头
   */
  private BillCardPanel getDataPanel() {
    if (this.dataPanel == null) {
      BillTempletVO newTempletVO = new BillTempletVO();

      BillTempletHeadVO headVO = new BillTempletHeadVO();
      headVO.setPrimaryKey("jiluxuliehaojiemian2");
      headVO.setTs(new UFDateTime("2015-04-28 12:00:00"));
      headVO.setBillTempletCaption("");
      headVO.setDividerProportion("0.1");
      newTempletVO.setParentVO(headVO);
      List<BillTempletBodyVO> lbodyVO = new ArrayList<BillTempletBodyVO>();

      BillTempletBodyVO bi =
          this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4008001_0", "04008001-0091")/*@res "序列号："*/,
              LocationRefDlg.SNFROM, 0, false, IBillItem.STRING, 1, 50, true);
      lbodyVO.add(bi);

      bi =
          this.getBillTempletBodyVO("--", LocationRefDlg.SNTO, 1, false,
              IBillItem.STRING, 1, 50, true);
      lbodyVO.add(bi);

      bi =
          this.getBillTempletBodyVO("", LocationRefDlg.ROW_SEL, 0, true,
              IBillItem.BOOLEAN, 50, 5, true);
      lbodyVO.add(bi);
      int barsnwidth = 180;
      if (!ICLocationEnum.Serial.equals(this.locationEnum)) {
        barsnwidth = 140;
        bi =
            this.getBillTempletBodyVO(
                nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
                    "UC000-0003830")/*@res "货位"*/, OnhandDimVO.CLOCATIONID, 3,
                true, IBillItem.STRING, barsnwidth, 50, false);
        bi.setLoadformula(" getColValue(bd_rack,name,pk_rack,"
            + OnhandDimVO.CLOCATIONID + ") ");
        lbodyVO.add(bi);
      }
      bi =
          this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("common", "UC000-0001819")/*@res "序列号"*/,
              OnhandSNVO.VSNCODE, 1, true, IBillItem.STRING, barsnwidth, 50,
              false);
      lbodyVO.add(bi);

      bi =
          this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4008001_0", "04008001-0092")/*@res "条码"*/,
              OnhandSNVO.VBARCODE, 2, true, IBillItem.STRING, barsnwidth, 50,
              false);
      lbodyVO.add(bi);
      
      bi =
          this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("40080818", "1400808180001")/*@res "主数量"*/,
              OnhandSNVO.NONHANDNUM, 4, true, IBillItem.DECIMAL, barsnwidth, 50,
              false);
      lbodyVO.add(bi);
      
      bi =
          this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("40080818", "1400808180014")/*@res "质量等级"*/,
              ICPubMetaNameConst.CSNQUALITYLEVELID, 5, true, IBillItem.STRING, barsnwidth, 50,
              false);
      bi.setLoadformula(" getColValue(scm_qualitylevel_b,cqualitylvname,pk_qualitylv_b,"
              + ICPubMetaNameConst.CSNQUALITYLEVELID + ") ");
      lbodyVO.add(bi);

//      bi =
//          this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
//              .getStrByID("4008001_0", "04008001-0757")/*@res "次条码"*/,
//              OnhandSNVO.VBARCODESUB, 2, true, IBillItem.STRING, barsnwidth,
//              50, false);
//      lbodyVO.add(bi);
      
      bi =
              this.getBillTempletBodyVO(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
                  .getStrByID("4008001_0", "04008001-0862")/*@res "条码主键"*/,
                  ICPubMetaNameConst.PK_SERIALCODE, 2, true, IBillItem.STRING, barsnwidth,
                  50, false);
      bi.setShowflag(false);
      lbodyVO.add(bi);

      newTempletVO.setChildrenVO(lbodyVO.toArray(new BillTempletBodyVO[lbodyVO
          .size()]));

      this.dataPanel = new BillCardPanel();
      this.dataPanel.setBillData(new BillData(newTempletVO));
      dataPanel.setDividerProportion(0.1);
      this.dataPanel.setName("locRef");
      this.dataPanel.getBodyPanel().setBBodyMenuShow(false);
      this.dataPanel.getBodyPanel().setRowNOShow(false);
      this.dataPanel.addNew();
      this.dataPanel.getBillData().setEnabled(true);
      this.dataPanel.setBodyMenuShow(false);
      this.dataPanel.getBillModel().setEnabled(true);
      this.dataPanel.getBodyItem(LocationRefDlg.ROW_SEL).setEdit(true);
      this.dataPanel.addEditListener(new BillEditListener() {
        @Override
        public void afterEdit(BillEditEvent e) {
          LocationRefDlg.this.afterDataPanelEdit(e);
        }

        @Override
        public void bodyRowChange(BillEditEvent e) {
          // 目前不需要实现
        }
      });
    }
    return this.dataPanel;
  }

  /**
   * @return param
   */
  private OnhandSNViewVO getParam() {
    return this.param;
  }

  private OnhandSNViewVO[] queryVOs(int pagenum) throws BusinessException {
	    OnhandSNViewVO queryParam = this.getParam();
	    // 根据getParam从模板获得各个参数的值 然后转换成查询流水账的参数
	    // 单据表头仓库为空时，如果查询结存，之后用户编辑仓库后，会清空所有单品信息，因此干脆不查。 wangxhi
	    if (queryParam == null
	        || StringUtil.isSEmptyOrNull(queryParam.getCwarehouseid())) {
	      // throw new BusinessException("查询参数为空");
	      return null;
	    }	    
	    OnhandSNViewVO[] snvos = null;
	    InvInfoQuery invq = InvInfoUIQuery.getInstance().getInvInfoQuery();

	    try {
	      OnhandVOTools.getRealOnhandDim(invq, new OnhandDimVO[] {
	        queryParam.getOnhandDimVO()
	      });
	      
	      OnhandSNViewVO[] onhandSNViewVO=this.queryOhandSNs(invq, queryParam, pagenum);
	      //材料出库单过滤PDA占用的
//		  onhandSNViewVO = filterByPad(onhandSNViewVO, queryParam);
	      return onhandSNViewVO;
	    }
	    catch (Exception e) {
	      ExceptionUtils.marsh(e);
	    }
	    return snvos;
	  }

  /**
   * 查询序列号结存
   * 
   * @param invq
   * @param queryParam
   * @return
   */
  private OnhandSNViewVO[] queryOhandSNs(InvInfoQuery invq,
      OnhandSNViewVO queryParam,int pagenum) {
    OnhandSNViewVO[] snvos = null;
    IOnhandQry server = NCLocator.getInstance().lookup(IOnhandQry.class);
//    InvCalBodyVO invvo =
//        invq.getInvCalBodyVO(queryParam.getPk_org(),
//            queryParam.getCmaterialvid());
    try {
//      if (!ValueCheckUtil.isTrue(invvo.getIsprimarybarcode())) {
//        if (!StringUtil.isSEmptyOrNull(queryParam.getVsncode())) {
//          return this.queryBySN(server, queryParam);
//        }
//        return this.queryByDim(server, queryParam);
//      }
//
//      if (!ValueCheckUtil.isTrue(invvo.getIssecondarybarcode())) {
//        if (!StringUtil.isSEmptyOrNull(queryParam.getVbarcode())) {
//          return this.queryByBarcode(server, queryParam, false);
//        }
//        if (!StringUtil.isSEmptyOrNull(queryParam.getVsncode())) {
//          return this.queryBySN(server, queryParam);
//        }
//
//        return this.queryByDim(server, queryParam);
//      }
//      if (!StringUtil.isSEmptyOrNull(queryParam.getVbarcodesub())) {
//        return this.queryByBarcode(server, queryParam, true);
//      }
//      if (!StringUtil.isSEmptyOrNull(queryParam.getVbarcode())) {
//        return this.queryByBarcode(server, queryParam, false);
//      }
//
//      if (!StringUtil.isSEmptyOrNull(queryParam.getVsncode())) {
//        return this.queryBySN(server, queryParam);
//      }
    	snvos =this.queryByDim(server, queryParam, pagenum);
    	// 查询时过滤
//    	OnhandSNViewVO[] showdatas = this.filterShowData(snvos);
        if (ValueCheckUtil.isNullORZeroLength(snvos)) {
          return null;
        }
        this.fillCsnqualitylevelid(snvos);
        
      return snvos;

    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    return snvos;
  }

//  /**
//   * 根据序列号查询序列号结存
//   * 
//   * @param server
//   * @param queryParam
//   * @return
//   * @throws BusinessException
//   */
//  private OnhandSNViewVO[] queryBySN(IOnhandQry server,
//      OnhandSNViewVO queryParam) throws BusinessException {
//    OnhandSelectDim dim = new nc.vo.ic.onhand.pub.OnhandSelectDim();
//    dim.addSelectFields(OnhandDimVO.getDimContentFields());
//
//    return server.queryOnhandSNBySNCode(dim, new String[] {
//      queryParam.getPk_serialcode()
//    });
//  }
//
//  /**
//   * 根据条码查询序列号结存
//   * 
//   * @param server
//   * @param queryParam
//   * @param bysecbar
//   * @return
//   * @throws BusinessException
//   */
//  private OnhandSNViewVO[] queryByBarcode(IOnhandQry server,
//      OnhandSNViewVO queryParam, boolean bysecbar) throws BusinessException {
//    OnhandSelectDim dim = new nc.vo.ic.onhand.pub.OnhandSelectDim();
//    dim.addSelectFields(OnhandDimVO.getDimContentFields());
//    return server.queryOnhandSNByBarCode(dim, new String[] {
//      queryParam.getVbarcode()
//    }, new String[] {
//      queryParam.getVbarcodesub()
//    }, bysecbar);
//  }

  /**
   * 根据存量维度查询序列号结存，不含条码序列号条件
   * 
   * @param server
   * @param queryParam
   * @return
   * @throws BusinessException
   */
  private OnhandSNViewVO[] queryByDim(IOnhandQry server,
      OnhandSNViewVO queryParam, int pagenum) throws BusinessException {
	    OnhandQryCond cond = new OnhandQryCond();
	    cond.setCurrentPage(pagenum);
	    cond.setPagerows(defaultPageCount);
	    cond.addAllSelectFields();
	    String vfree9=queryParam.getVfree9();
	    UFBoolean b=UFBoolean.FALSE;
//	    if("Y".equals(vfree9)){
//	    	queryParam.setVfree9(null);
//	    	b=UFBoolean.TRUE;
//	    }
	    
	    DimMatchedObj<OnhandDimVO> handkey =
	        new DimMatchedObj<OnhandDimVO>(queryParam.getOnhandDimVO(),
	            OnhandDimVO.getDimContentFields());
	    cond.addFilterDimConditon(
	        handkey.getDimFields(),
	        VOEntityUtil.getVOValues(queryParam.getOnhandDimVO(),
	            handkey.getDimFields()));
	   
//	    TODO
	    OnhandSNViewVO[] onhandSNViewVO=server.queryOnhandSNOnlyForSNRef(cond);
	    //材料出库单过滤PDA占用的
	    if(isMaterialOut){
//	    	queryParam.setVfree9("Y");
	    	 onhandSNViewVO = filterByPad(onhandSNViewVO, queryParam);
	    }
	   
	    return onhandSNViewVO;
	  }
  /**
   * liyf
   * 过滤PDA占用的
   * @return
 * @throws BusinessException 
   */
  private OnhandSNViewVO [] filterByPad( OnhandSNViewVO[] onhandSNViewVO,OnhandSNViewVO queryParam) throws BusinessException{
	  //  材料出选序列号 过滤序列号档案有标识的
//		  String vfree9=queryParam.getVfree9();//用来判断是否材料出库单
//		  if("Y".equals(vfree9)){
		  	  queryParam.setVfree9(null);
		  	  String cmaterialoid =queryParam.getCmaterialoid();
		  	  String pk_org=queryParam.getPk_org();
		  	  String sql= " select vcode   from sn_serialno a where a.cmaterialoid='"+cmaterialoid +"' and  pk_org='"+pk_org+"' and vdef2='Y' and dr=0";
		  	  IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  	  List<Map<String,Object>> list = (List<Map<String,Object>> )iuap.executeQuery(sql, new MapListProcessor());
		  	  if(list==null||list.size()==0){
		  		  return null;
		  	  }
		  	  Set<String>set=new HashSet<String>(); 
		  	  for(int i=0;i<list.size();i++){
		  		  String vcode=list.get(i).get("vcode").toString();
		    		set.add(vcode);
		  	  }
		  	  List<OnhandSNViewVO>list2=new ArrayList<OnhandSNViewVO>();
		  	  for(int i=0;i<onhandSNViewVO.length;i++){
		  		  String vhashcode=onhandSNViewVO[i].getVsncode();
		  		  if(set.contains(vhashcode)){
		  			  list2.add(onhandSNViewVO[i]);
		  		  }
		  		 
		  	  }
		  	  
		  	  return list2.toArray(new  OnhandSNViewVO[list2.size()] );
		    
  }

  private void setVOToMap(OnhandSNViewVO[] vos) {
  pageData.remove(String.valueOf(this.currentPage));
  pageData.putAll(String.valueOf(this.currentPage), vos == null ? new ArrayList<OnhandSNViewVO>() : Arrays.asList(vos));
//    if (this.resultVOsMap == null) {
//      this.resultVOsMap = new HashMap<String, OnhandSNViewVO>();
//    }
//    this.resultVOsMap.clear();
//    if (ValueCheckUtil.isNullORZeroLength(vos)) {
//      return;
//    }
//    for (OnhandSNViewVO vo : vos) {
//      if (NCBaseTypeUtils.isNullOrZero(vo.getNonhandnum())) {
//        continue;
//      }
//      this.resultVOsMap.put(vo.getVbarcode(), vo);
//      if (SysInitGroupQuery.isSNEnabled()) {
//    	  this.resultVOsMap.put(vo.getPk_serialcode(), vo);  
//      }else{
//    	  this.resultVOsMap.put(vo.getVsncode(), vo);  
//      }
//    }
  }

  private void showData(OnhandSNViewVO[] snvos) {
    this.clearUI();
//    if (this.resultVOsMap != null) {
//      this.resultVOsMap.clear();
//    }
    // 按照设置排序
    if (this.getSortrule() != null) {
    	snvos = this.getSortrule().sortByRule(snvos);
    }
    this.setVOToMap(snvos);
	this.setBtnPageEnabled(snvos == null ? 0 : snvos.length);
    this.getDataPanel().getBillModel().setBodyDataVO(snvos);
    this.getDataPanel().getBillModel().execLoadFormula();
    // add by wangceb start
    // 增加初始化事件处理，供调用方进行处理
	if (this.getInitListener() != null) {
		String key = SysInitGroupQuery.isSNEnabled() ? ICPubMetaNameConst.PK_SERIALCODE : OnhandSNVO.VSNCODE;
		Map<String, OnhandSNViewVO> pagedata = this.hashVObykey(key, this.selVOMap.get(String.valueOf(this.currentPage)));
		
		this.getInitListener().handleBodyInitEvent(
				this.getDataPanel(),
				this.getBarCodePanel(),
				pagedata);
	}
    // add by wangce end
  }

  /**
   * 条码面板编辑后事件
   */
  protected void afterBarCodePanelEdit(BillEditEvent e) {
    if (e.getKey().equals(LocationRefDlg.VINPUTBARCODE)) {
      // 获得输入文本
      String text = e.getValue().toString();
      if (text == null || text.trim().length() == 0) {
        return;
      }
      text = text.trim();
      this.selectMatch(text, text, OnhandSNVO.VBARCODE);
    }

  }

  /**
   * 根据输入的序列号或条码范围自动选择匹配的表体行
   * 
   * @param codeFrom 起始序列号或条码
   * @param codeTo 终止序列号或条码
   * @param keyCode 要匹配的字段key：条码/序列号
   */
  private void selectMatch(String codeFrom, String codeTo, String keyCode) {
    boolean bsel = false;
    int selCount = 0;
    for (int row = 0; row < this.getDataPanel().getRowCount(); row++) {
      this.getDataPanel().setBodyValueAt(UFBoolean.FALSE, row,
          LocationRefDlg.ROW_SEL);
      String code = (String) this.getDataPanel().getBodyValueAt(row, keyCode);
      if (StringUtil.isSEmptyOrNull(code)) {
        continue;
      }

      bsel = false;
      if (StringUtil.isSEmptyOrNull(codeFrom)) {
        if (!StringUtil.isSEmptyOrNull(codeTo) && code.compareTo(codeTo) == 0) {
          bsel = true;
        }
      }
      else if (StringUtil.isSEmptyOrNull(codeTo)) {
        if (!StringUtil.isSEmptyOrNull(codeFrom)
            && code.compareTo(codeFrom) == 0) {
          bsel = true;
        }
      }
      else {
        if (code.compareTo(codeFrom) >= 0 && code.compareTo(codeTo) <= 0) {
          bsel = true;
        }
      }
      if (bsel) {
        this.getDataPanel().setBodyValueAt(UFBoolean.TRUE, row,
            LocationRefDlg.ROW_SEL);
        selCount++;
      }

    }
    Object oldNum =
        this.getBarCodePanel().getHeadItem(ICLocationVO.NNUM).getValueObject();
    if (oldNum == null && selCount == 0) {
      return;
    }
    this.getBarCodePanel().getHeadItem(ICLocationVO.NNUM)
        .setValue(Integer.valueOf(selCount));
  }

  /**
   * 数据面板编辑后事件
   */
  protected void afterDataPanelEdit(BillEditEvent e) {
    if (e.getKey().equals(LocationRefDlg.SNFROM)
        || e.getKey().equals(LocationRefDlg.SNTO)) {
      this.afterSNCodeEdit();
    }
    else if (e.getKey().equals(LocationRefDlg.ROW_SEL)) {
      this.afterCheckBoxSelectChange();
    }
  }

  /**
   * 序列号编辑事件
   */
  protected void afterSNCodeEdit() {
    String snfrom =
        (String) this.getDataPanel().getHeadItem(LocationRefDlg.SNFROM)
            .getValueObject();
    String snto =
        (String) this.getDataPanel().getHeadItem(LocationRefDlg.SNTO)
            .getValueObject();
    this.selectMatch(snfrom, snto, OnhandSNVO.VSNCODE);
  }

  /**
   * 选择框改变事件
   */
  protected void afterCheckBoxSelectChange() {
    int selCount = 0;
    for (int row = 0; row < this.getDataPanel().getRowCount(); row++) {
      Boolean bsel =
          (Boolean) this.getDataPanel().getBodyValueAt(row,
              LocationRefDlg.ROW_SEL);

      if (ValueCheckUtil.isTrue(bsel)) {
        selCount++;
      }

    }
    Object oldNum =
        this.getBarCodePanel().getHeadItem(ICLocationVO.NNUM).getValueObject();
    if (oldNum == null && selCount == 0) {
      return;
    }

    int n = this.getSumNum(false);
    
    this.getBarCodePanel().getHeadItem(ICLocationVO.NNUM)
        .setValue(Integer.valueOf(selCount) + n);
  }

  @Override
  protected UIPanel getUIPanelMain() {
    if (this.ivjUIPanelMain == null) {

      this.ivjUIPanelMain = new UIPanel();
      this.ivjUIPanelMain.setName("UIDialogContentPane");
      this.ivjUIPanelMain.setLayout(new BorderLayout());
      this.ivjUIPanelMain.add(this.getDataPanelWithPageButton(), BorderLayout.CENTER);
      this.ivjUIPanelMain.add(this.getBarCodePanel(), BorderLayout.SOUTH);

    }
    return this.ivjUIPanelMain;
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.ic.pub.view.ICBaseDialog#onCancel()
   */
  @Override
  protected void onCancel() {
    this.clearSelectedRows();
    super.onCancel();
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.ic.pub.view.ICBaseDialog#onOk()
   */
  @Override
  protected void onOk() {
	  this.setSelectedVOsCache();
    OnhandSNViewVO[] vos = this.getSelectedVOs();
    if (ValueCheckUtil.isNullORZeroLength(vos)) {
//      ShowUtil.showErrorDlg(this, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
//          .getStrByID("4008001_0", "04008001-0093")/*@res "没有选择的数据"*/);
	  super.onOk();
      return;
    }
    if(bBodySerial&&vos.length>1){
        ShowUtil.showErrorDlg(this, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
                .getStrByID("4008001_0", "04008001-0861")/*@res "表体序列号不能多选，请修改！"*/);
            return;
    }
    super.onOk();
  }
  
  public boolean isbBodySerial() {
	return bBodySerial;
  }

  public void setbBodySerial(boolean bBodySerial) {
	this.bBodySerial = bBodySerial;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		OnhandSNViewVO[] datavos = null;
		try {
			if (e.getSource().equals(this.getPageNumText())) {
				String text = this.getPageNumText().getText();
				if (Integer.parseInt(text) <= 0 || Integer.parseInt(text) > 5000) {
				      MessageDialog
			          .showErrorDlg(
			              null,
			              nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
			                  "4008020_0", "04008020-0000")/*@res "错误"*/,
			              nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"4008001_0", "04008001-0908")/* @res "每页显示行数范围大于0小于5000！" */);
				      this.getPageNumText().setText(String.valueOf(this.defaultPageCount));
				      return;
				}
				this.defaultPageCount = Integer.parseInt(text);
				this.loadData();
				return;
			}
			
			this.setSelectedVOsCache();
			if (e.getSource().equals(this.getBtnNextPage())) {
				this.currentPage++;

			} else if (e.getSource().equals(this.getBtnPreviousPage())) {
				this.currentPage--;
			}
			if (this.pageData.containsKey(String.valueOf(this.currentPage))) {
				datavos = CollectionUtils.listToArray(this.pageData.get(String.valueOf(this.currentPage)));
			} else {
				datavos = this.queryVOs(this.currentPage);
			}
			// 显示数据
			this.setLbPageInfo();
			this.showData(datavos);
			this.getBarCodePanel().getHeadItem(ICLocationVO.NNUM).setValue(this.getSumNum());
		} catch (BusinessException e1) {
			Throwable et = ExceptionUtils.unmarsh(e1);
			ShowUtil.showErrorDlg(this, et.getMessage());
		}
	}

	private int getSumNum () {
		return this.getSumNum(true);
	}
	
	private int getSumNum(boolean includeCurrent) {
		int n = 0;
		for (Map.Entry<String, List<OnhandSNViewVO>> entry : this.selVOMap
		        .entrySet()) {
			if (!includeCurrent && Integer.valueOf(entry.getKey()) == this.currentPage) {
				continue;
			}
			n += entry.getValue().size();
		    }
		return n;
	}

}
