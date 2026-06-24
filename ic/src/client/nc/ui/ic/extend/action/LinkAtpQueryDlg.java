package nc.ui.ic.extend.action;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator;
import nc.ui.uif2.userdefitem.QueryParam;
import nc.ui.uif2.userdefitem.UserDefItemContainer;
import nc.vo.bc.mapp.pub.BarcodeRCKVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uif2.LoginContext;

/**
 * 出入库 单--联查条码 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class LinkAtpQueryDlg extends UIDialog implements ActionListener,
		KeyListener,BillEditListener  {

	private String bid = null;
	private JPanel contentPanel = null;
	private BillCardPanel billCardPanel = null;
	private LoginContext context = null;
	
	/**
	 * 
	 * @param container
	 * @param pk_group
	 * @param pk_org
	 * @param pk_pickm
	 */
	public LinkAtpQueryDlg(Container container, String bid,LoginContext context) {
		super(container);
		this.bid = bid;
		this.context = context;
		// 初始化窗口
		initialize();
		// 初始化监听
		initListener();
		// 初始化数据
		initData();
	}

	/**
	 * 初始化
	 */
	public void initialize() {
		setTitle("条码联查");
		setSize(new Dimension(getScreamWidth()/2, getScreamHeight()/2));
		setContentPane(getContentPanel());
		setLocation(getScreamWidth() / 4,
				getScreamHeight() / 4);
	}

	/**
	 * 初始化监听
	 */
	public void initListener() {
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		BarcodeRCKVO[] plVOs = getVOs();
		getBillCardPanel().getBillModel().setBodyDataVO(plVOs);
		getBillCardPanel().getBillModel().loadLoadRelationItemValue();
		if(plVOs != null){
			for(int i = 0; i < plVOs.length; i ++){
				getBillCardPanel().getBillModel().setRowState(i, BillModel.NORMAL);
			}
		}
	}
	
	private BarcodeRCKVO[] getVOs(){
		BarcodeRCKVO[] vos = null;
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select * " +
				" from qilibc_barcodeflow m " +
				"where  isnull(m.dr,0)=0 and pk_item='"+bid+"' order by m.vbarcode";
		try {
			ArrayList<BarcodeRCKVO> list = (ArrayList<BarcodeRCKVO>) query.executeQuery(sql, new BeanListProcessor(BarcodeRCKVO.class));
			if(list!=null && list.size()>0){
				vos = list.toArray(new BarcodeRCKVO[0]);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException("查询失败："+e.getMessage());
		}
		return vos;
	}

	/**
	 * 主界面
	 * 
	 * @return
	 */
	public JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setPreferredSize(new Dimension(getScreamWidth()/2, getScreamHeight()/2));
//			contentPanel.add(getLabel());
			// 加载卡片模板
			contentPanel.add(getBillCardPanel());
//			// 加载按钮框
//			contentPanel.add(getButtonPanel());
		}
		return contentPanel;
	}
//
//	/**
//	 * 添加标签
//	 * @return
//	 */
//    private JLabel getLabel(){
//        if(label1==null){
//            label1 = new JLabel();
//            label1.setPreferredSize(new Dimension(1000, 30));
//            label1.setToolTipText("JLabel");
//            label1.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 3));
//
//        }
//        return label1;
//    }
	/**
	 * 数据Panel
	 * 
	 * @return
	 */
	public BillCardPanel getBillCardPanel() {
		if (billCardPanel == null) {
			String pk_group = WorkbenchEnvironment.getInstance().getGroupVO()
					.getPk_group();
			billCardPanel = new BillCardPanel();
			billCardPanel.setPreferredSize(new Dimension(getScreamWidth()/2, getScreamHeight()/2-5));
			String billtype = "barFlow";
			BillData billData = new BillData(billCardPanel.getDefaultTemplet(
					billtype, null, null, pk_group));
			billCardPanel.setBillData(billData);
			billCardPanel.addEditListener(this);
			getMarAsst().prepareBillData(billCardPanel.getBillData());
		}
		return billCardPanel;
	}
	
	private MarAsstPreparator getMarAsst(){
		MarAsstPreparator marAsst = new MarAsstPreparator();
		marAsst.setContainer(getUserDefContainer());
		marAsst.setPrefix("vfree");
		return marAsst;
	}
	
	private UserDefItemContainer getUserDefContainer(){
		UserDefItemContainer container = new UserDefItemContainer();
		container.setContext(context);
		List<QueryParam> params = new ArrayList<QueryParam>();
		QueryParam param1 = new QueryParam();
		param1.setRulecode("materialassistant");
		params.add(param1);
		container.setParams(params);
		return container;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * 屏幕的宽度。
	 * 
	 * @return
	 */
	private int getScreamWidth() {
		JApplet jApp = ClientEnvironment.getInstance().getDesktopApplet();
		int width = 0;
		if (jApp != null) {
			width = jApp.getWidth();
		}
		if(width == 0){
			width = 1000;
		}
		return width;
	}

	/**
	 * 屏幕的高度
	 * 
	 * @return
	 */
	private int getScreamHeight() {
		JApplet jApp = ClientEnvironment.getInstance().getDesktopApplet();
		int height = 0;
		if (jApp != null) {
			height = jApp.getHeight();
		}
		if(height == 0){
			height = 1000;
		}
		return height;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		
	}

	@Override
	public void bodyRowChange(BillEditEvent e) {
		
	}
}
