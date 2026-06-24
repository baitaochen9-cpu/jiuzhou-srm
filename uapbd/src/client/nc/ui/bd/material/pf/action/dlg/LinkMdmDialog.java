package nc.ui.bd.material.pf.action.dlg;
 
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.AbstractFunclet;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoService;
import nc.itf.bd.material.pf.IMaterialPfService;
import nc.itf.material.mdm.SendMdmItf;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.pf.MaterialPfVO;
import nc.vo.material.mdm.SendMdmPropetys;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

import org.java.plugin.boot.ErrorDialog;


 
/**
 * 
 * @ClassName: SplitLineDialog
 * @Description: TODO(拆行对话框)
 * @author Administrator
 * @version 1.0
 */
public class LinkMdmDialog extends UIDialog implements BillEditListener,
		BillEditListener2, ListSelectionListener {
	
	private JPanel uiContentPane;

	private JPanel btnUIPanel;
	private BillCardPanel billPanel;
	private JPanel DimPanel;
	SendMdmItf lookup ;
	MaterialVO materialvo ;
	MaterialPfVO pfvo ;	
	String Pk_org;
	SendMdmItf mdmitf ;

	
	public SendMdmItf getMdmitf() {
		if(null ==  mdmitf ){
			mdmitf  = NCLocator.getInstance().lookup(SendMdmItf.class);
		}
		return mdmitf;
	}

	public String getPk_org() {
		return Pk_org;
	}

	public void setPk_org(String pk_org) {
		Pk_org = pk_org;
	}

	
	/**
	 * 
	 * @param parent
	 * @param material
	 * @param pfvo 物料申请单，如果是物料档案则传空
	 * @param pk_org
	 * @throws BusinessException
	 */
	public LinkMdmDialog(AbstractFunclet  parent ,MaterialVO material,MaterialPfVO pfvo,String pk_org) throws BusinessException {
		// TODO Auto-generated constructor stub
		super(parent);
		setMaterialvo(material);
		setPk_org(pk_org);
		setPfvo(pfvo);
		this.initialize();
		initData(material);
		
		
	}
	
	/**
	 * 界面初始化 zhian.ye
	 */
	private void initialize() {
		this.setName("mpsSplitDialog");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("数据展示");
		int w = 1200;
		int h = 600;
		this.setBounds(300, 200, w, h);
		this.setResizable(true);
		// 设置模板
		this.setContentPane(this.getUIContentPane());
		
	}
	
	/**
	 * 界面布局总装
	 * @return
	 */
	private Container getUIContentPane() {
		if (null == this.uiContentPane) {
			this.uiContentPane = new JPanel();
			this.uiContentPane.setName("UIDialogContentPane");
			this.uiContentPane.setLayout(new BorderLayout());
			this.getUIContentPane().add(this.getBillPanel(), "Center");//"Center"
			this.getUIContentPane().add(this.getBtnUIPanel(), "South");
			this.getUIContentPane().add(this.getQueryPanel(),"West");
		}
		return this.uiContentPane;
	}
	
	/**
	 * 查询面板
	 * @return
	 */
	public JPanel queryPanel;
	private Component getQueryPanel() {
		if(null == this.queryPanel){
			queryPanel= new JPanel();
			this.queryPanel.setName("queryMDM");
			this.queryPanel.setPreferredSize(new Dimension(110, 150));
			this.queryPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			this.queryPanel.setLayout(new GridLayout(2,1));
			this.queryPanel.add(getDimPanel());
			this.queryPanel.add(getContPanel());
			
		}
		return queryPanel;
	}
	
	private TextField textField;
	private TextField textField1;
	private TextField textField2;
	private TextField textField3;
	private TextField textField4;
	private TextField textField5;
	
	
	/**
	 * 条件查询
	 * @return
	 */
	public JPanel contPanel;
	private Component getContPanel() {
		if(null == this.contPanel){
			contPanel= new JPanel();
			this.contPanel.setName("contPanel");
			this.contPanel.setPreferredSize(new Dimension(110, 130));
			this.contPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			this.contPanel.setLayout(new GridLayout(13,1));
			
			
//			new Label("ABC"));
//			frame.add(new TextField());
			this.contPanel.add(new Label("原始物料编码："));
			textField5 = new TextField();
			textField5.setName("code");
			this.contPanel.add(textField5);
			
			this.contPanel.add(new Label("物料名称："));
			textField = new TextField();
			textField.setName("name");
			this.contPanel.add(textField);
			
			this.contPanel.add(new Label("助记码："));
			textField1 = new TextField();
			textField.setName("materialmnecode");
			this.contPanel.add(textField1);
			
			this.contPanel.add(new Label("主数据编码："));
			textField2 = new TextField();
			textField2.setName("mdm_code");
			this.contPanel.add(textField2);
			
			this.contPanel.add(new Label("规格："));
			textField3 = new TextField();
			textField3.setName("spec");
			this.contPanel.add(textField3);
			
			this.contPanel.add(new Label("型号："));
			textField4 = new TextField();
			textField4.setName("model");
			this.contPanel.add(textField4);
			
			
			this.contPanel.add(blurryQuery());
			
		}
		return contPanel;
	}

	
	public Checkbox getMaterialname() {
		return materialname;
	}

	public void setmMaterialname(Checkbox materialname) {
		this.materialname = materialname;
	}

	public Checkbox getMaterialtype() {
		return materialtype;
	}

	public void setMaterialtype(Checkbox materialtype) {
		this.materialtype = materialtype;
	}

	public Checkbox getMaterialspec() {
		return materialspec;
	}

	public void setMaterialspec(Checkbox materialspec) {
		this.materialspec = materialspec;
	}

	public Checkbox getMaterialmnecode() {
		return materialmnecode;
	}

	public void setMaterialmnecode(Checkbox materialmnecode) {
		this.materialmnecode = materialmnecode;
	}

	public Checkbox getDef19() {
		return def19;
	}

	public void setDef19(Checkbox def19) {
		this.def19 = def19;
	}

	public Checkbox getBrand() {
		return brand;
	}

	public void setBrand(Checkbox brand) {
		this.brand = brand;
	}

	public Checkbox getGroupMarbascode_name() {
		return groupMarbascode_name;
	}

	public void setGroupMarbascode_name(Checkbox groupMarbascode_name) {
		this.groupMarbascode_name = groupMarbascode_name;
	}
	private Checkbox materialname;
	private Checkbox materialtype;
	private Checkbox materialspec;
	private Checkbox materialmnecode;
	private Checkbox def19;
	private Checkbox brand;
	private Checkbox groupMarbascode_name;
	
	/**
	 * 维度查询
	 * @return
	 */
	private Component getDimPanel() {
		if (null == this.DimPanel) {
			this.DimPanel = new JPanel();
			this.DimPanel.setName("MdmDimPane");
			DimPanel.setPreferredSize(new Dimension(110, 100));
			this.DimPanel.setLayout(new GridLayout(8,1));
			this.DimPanel.setBorder(BorderFactory.createLineBorder(Color.black));
//			 String[] options = {"选项1", "选项2", "选项3"};
//			JComboBox<String> comboBox = new JComboBox<>(options);
			setmMaterialname(new Checkbox("物料名称"));
			setMaterialtype(new Checkbox("型号"));
			setMaterialspec(new Checkbox("规格"));
			setMaterialmnecode(new Checkbox("助记码"));
			setDef19(new Checkbox("材质"));
			setBrand(new Checkbox("生产厂家"));
			setGroupMarbascode_name(new Checkbox("集团统一分类"));
			
			DimPanel.add( getMaterialname()  );
			DimPanel.add( getMaterialtype()  );
			DimPanel.add( getMaterialspec()  );
			DimPanel.add( getMaterialmnecode()  );
			DimPanel.add( getDef19()  );
			DimPanel.add( getBrand()  );
			DimPanel.add( getGroupMarbascode_name() );
			this.DimPanel.add(this.queryDimensionBut()); //增加主数据匹配设置
		}			
		return this.DimPanel;
	}

	/**
	 * 单据模板加载
	 * @return
	 */
	private BillCardPanel getBillPanel() {
		if (this.billPanel == null) {
			this.billPanel = new BillCardPanel();
			this.billPanel.setName("linkmdm");
			this.billPanel.loadTemplet("1001ZZ100000000Y0MN4");//0001ZZ1000000003SXVB
			this.billPanel.addEditListener(this);
			this.billPanel.addBillEditListenerHeadTail(this);
			this.billPanel.getBillTable().setSelectionMode(
					javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.billPanel.getBodyPanel().getTable().getSelectionModel().addListSelectionListener(this);
//			this.billPanel.getBodyPanel().getTable().setEnabled(false);
			this.billPanel.getBodyPanel().getTableModel().setEnabledAllItems(false);
			this.billPanel.setTatolRowShow(false);
			this.billPanel.setBodyMenuShow(false);
		}
		return this.billPanel;
	}
	
	/**
	 * 按钮模板加载
	 * @return
	 */
	public JPanel getBtnUIPanel() {
		if (this.btnUIPanel == null) {
			this.btnUIPanel = new JPanel();
//			btnUIPanel.add(comp, constraints)
			this.btnUIPanel.setName("BtnUIPanel");
			
			//*begin*****20250512****应用性调整，增加物料分类全量数据查询
			this.btnUIPanel.add(this.queyMdmByClassBut());
			//**end***
			this.btnUIPanel.add(this.getSendMdmBut());/*升级到主数据*/
			this.btnUIPanel.add(this.getLinkMdm());/*关联到主数据*/
			this.btnUIPanel.add(this.getCancel());/*取消*/
		}
		
		return this.btnUIPanel;
	}
	
	
	
	




@SuppressWarnings("restriction")
/**
 * 数据初始化
 * @param data
 */
	public void initData(MaterialVO data) throws BusinessException{
//		 pfvo = (MaterialPfVO) data.getParentVO();
		if (null == materialvo){
			materialvo = data;
			
		}
		if(null == data){
			return	;
		}
		//  表头数据加载
		this.setHaedData();
		//表体数据加载 ， 表体数据来源于主数据主表，创建NC接口通过后台服务器访问主数据服务器
		  List<Map<String,String>> bodydata = this.getMdmMaterialData(data);
		  this.setBodyData( bodydata);
		
	}

/**
 *表体数据加载
 * @param bodydata
 */
public void setBodyData(List<Map<String, String>> bodydata) {
	// TODO Auto-generated method stub
	this.billPanel.getBodyPanel().getTableModel().clearBodyData();// 界面清除
	if(null == bodydata || bodydata.size() == 0 ){	
		return;
	  }

	  	this.billPanel.getBodyPanel().addLine(bodydata.size()); //增行
	  	
		  for(int i = 0 ; i < bodydata.size(); i++)
			  for(String bodyitem :SendMdmPropetys.bodyItems){		  
				  this.billPanel.setBodyValueAt(bodydata.get(i).get(bodyitem), i, bodyitem);//页签编码	mdmmaterial
				  
			  }
	 
}

/**
 * 加载窗口表头数据
 * @param 
 */
	private void setHaedData() {
		
		for(String headItem : SendMdmPropetys.headItems){
			if(this.billPanel.getHeadItem(headItem) == null) continue;
			this.billPanel.setHeadItem(headItem, materialvo.getAttributeValue(headItem));
			this.billPanel.getHeadItem(headItem).setEdit(false);
		}
		
		//物料集团分类赋值，取物料基本分类档案关联def1关联自定义档案【GM002】  需要开启物料基本分类自定义项   ---pk_marbasclass
		String pk_marbasclass = (String) materialvo.getAttributeValue("pk_marbasclass");//组织级别物料基本分类，这个是在物料申请时必须 做必填的
		String queryDocNameByID = materialvo.getDef14();//集团物料分类优先取物料自定义项14 如果没有再取物料基本分类
		if(materialvo.getDef14() == null){
			try {
				queryDocNameByID =   getSenMdmSevecs().queryDocNameByID(pk_marbasclass, " bd_marbasclass left join bd_defdoc on bd_marbasclass.def1 = bd_defdoc.pk_defdoc ", "bd_marbasclass.pk_marbasclass", "bd_defdoc.pk_defdoc");
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			materialvo.setDef14(queryDocNameByID);
		}
		
		if(null == queryDocNameByID){
			this.billPanel.getHeadItem(SendMdmPropetys.MDMCLASS).setEdit(true);
		}else{
			this.billPanel.setHeadItem(SendMdmPropetys.MDMCLASS, queryDocNameByID); //这个模板取消参照，直接显示名称，然后禁止操作
			this.billPanel.getHeadItem(SendMdmPropetys.MDMCLASS).setEdit(false);
			
		}
		
		String mdmcode = (String) materialvo.getAttributeValue("def7"); //主数据编码，按原定义的自定义7，不做改变，后续也按这个字段进行存储
		this.billPanel.setHeadItem(SendMdmPropetys.MDMCODE,mdmcode);
		this.billPanel.getHeadItem(SendMdmPropetys.MDMCODE).setEdit(false);
}

	/**
	 * 通过实体获取表体数据
	 * @return 调用接获取主数据主表格式化数据
	 */
	private List<Map<String, String>> getMdmMaterialData(MaterialVO mateiral) {
		/*
		 * 组装查询条件
		 */
		Map<String,List<String>>  conds = SendMdmPropetys.getCondsByVO(mateiral);
		SendMdmItf lookup = getSenMdmSevecs();
		List<Map<String, String>> queryMdmPrimary = null;
		try {
			 queryMdmPrimary = lookup.queryMdmPrimary(conds);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			ErrorDialog.showError(this, "异常", "表休加载失败，无法获取到主数据内容！\n "+e.getMessage());
		}
		return queryMdmPrimary;
	}
	
	
	/**
	 * 数据持久化调用
	 * @return
	 */
	private SendMdmItf getSenMdmSevecs(){
		if(null == lookup ){
			lookup = NCLocator.getInstance().lookup(nc.itf.material.mdm.SendMdmItf.class);
		}
		return lookup;
	}
	
	
	public static List<Component> findAllComponents(Container container) {
        List<Component> componentList = new ArrayList<>();
        Component[] components = container.getComponents();
        for (Component component : components) {
            componentList.add(component);
            if (component instanceof Container) {
                componentList.addAll(findAllComponents((Container) component));
            }
        }
        return componentList;
    }
	
	/**
	 * 查询维度设置
	 * @return
	 */
	private JButton blurryQuery() {
		// TODO Auto-generated method stub
		JButton sendMdm = new JButton("模糊搜索");
		sendMdm.setName("模糊搜索");
		sendMdm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				new MdmDimensionDlg().show();
				String textField_code = textField5.getText();
				String textField_name = textField.getText();
				String textField_materialmnecode = textField1.getText();
				String textField_mdm_code = textField2.getText();
				String textField_spec = textField3.getText();
				String textField_model = textField4.getText();
				
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("code", textField_code);
				hashMap.put("name", textField_name);
				hashMap.put("materialmnecode", textField_materialmnecode);
				hashMap.put("mdm_code", textField_mdm_code);
				hashMap.put("spec", textField_spec);
				hashMap.put("model", textField_model);
				
				
				String GroupMarbascode = (String) getBillPanel().getHeadItem(SendMdmPropetys.MDMCLASS).getValueObject();//集团统一分类
				Map<String,List<String>> conds = 
						SendMdmPropetys.getCondsByVO(2,
								getMaterialvo(),
								!"".equals(textField_code),
								!"".equals(textField_name),
								!"".equals(textField_model),
								!"".equals(textField_spec),
								!"".equals(textField_materialmnecode),
								false,
								false,
								!"".equals(textField_mdm_code),
								GroupMarbascode,hashMap
								);
				List<Map<String, String>> queryMdmPrimary = null;
				try {
					queryMdmPrimary = getSenMdmSevecs().queryMdmPrimary(conds,"unity");
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					MessageDialog.showErrorDlg(getBillPanel(), "错误", "主数据内容加载时遇到问题，联系技术人员。");
				}
				setBodyData(queryMdmPrimary);
			}
		});
		return sendMdm;
	}

	/**
	 * 查询维度设置
	 * @return
	 */
	private JButton queryDimensionBut() {
		// TODO Auto-generated method stub
		JButton sendMdm = new JButton("维度匹配");
		sendMdm.setName("维度匹配");
		sendMdm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				new MdmDimensionDlg().show();
				String GroupMarbascode = (String) getBillPanel().getHeadItem(SendMdmPropetys.MDMCLASS).getValueObject();//集团统一分类
				if(null == GroupMarbascode){
					MessageDialog.showErrorDlg(getBillPanel(), "错误", "字段【集团统一分类】不能为空，请输入后再试！");
					return;
				}
				Map<String,List<String>> conds = 
						SendMdmPropetys.getCondsByVO(3
								,getMaterialvo(),
								false,
								getMaterialname().getState(),
								getMaterialtype().getState(),
								getMaterialspec().getState(),
								getMaterialmnecode().getState(),
								getDef19().getState(),
								getBrand().getState(),
								getGroupMarbascode_name().getState(),
								GroupMarbascode,null
								);		
				List<Map<String, String>> queryMdmPrimary = null;
				try {
					queryMdmPrimary = getSenMdmSevecs().queryMdmPrimary(conds,"unity");
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					MessageDialog.showErrorDlg(getBillPanel(), "错误", "主数据内容加载时遇到问题，联系技术人员。");
				}
				setBodyData(queryMdmPrimary);
			}
		});
		return sendMdm;
	}
	/**
	 * 分类数据查询
	 * @return
	 */
	private JButton queyMdmByClassBut() {
		JButton sendMdm = new JButton("分类数据查询");
		sendMdm.setName("分类数据查询");
		sendMdm.addActionListener(new ActionListener(){@Override
		public void actionPerformed(ActionEvent e) {
			//调用查询接口，查询条件重新定义，以物料分类进行查询，将全部数据进行展示，
			String value = (String) getBillPanel().getHeadItem(SendMdmPropetys.MDMCLASS).getValueObject();//集团统一分类
			if(null == value){
				MessageDialog.showErrorDlg(getBillPanel(), "错误", "字段【集团统一分类】不能为空，请输入后再试！");
				return;
			}
			
			String queryDocNameByID = "";
			try {
				queryDocNameByID = getSenMdmSevecs().queryDocNameByID(value, "bd_defdoc", "pk_defdoc", "name");
			} catch (DAOException e2) {
				// TODO Auto-generated catch block
				MessageDialog.showErrorDlg(getBillPanel(), "错误", "DAO物料分类查询异常！"+e2.getMessage());
				return;
			}
			
			List<String> list = new ArrayList<String>();
			list.add("and");
			list.add("like");
			list.add("'%"+queryDocNameByID+"%'");
			
			Map<String,List<String>> conds = new HashMap<String,List<String>>();
			conds.put("groupMarbascode#name", list);
			
			List<Map<String, String>> queryMdmPrimary = null;
			try {
				queryMdmPrimary = getSenMdmSevecs().queryMdmPrimary(conds);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				MessageDialog.showErrorDlg(getBillPanel(), "错误", "主数据内容加载时遇到问题，联系技术人员。");
			}
			setBodyData(queryMdmPrimary);
		}});
		return sendMdm;
	}
	
	/**
	 * 升级到主数据   
	 * @return
	 */
	private JButton getSendMdmBut(){
		JButton sendMdm = new JButton("升级到主数据");
		sendMdm.setName("升级到主数据");
		
		sendMdm.addActionListener(new ActionListener(){

			@SuppressWarnings("restriction")
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				nc.bs.logging.Logger.debug("升级到主数据》》");
				// 接口调用后台，31服务器调用主数据服务，主数据主表新增接口。
				//检查界面数据
				//非空检查
				for(String key :SendMdmPropetys.handNotNullList.keySet()){
					
					String value = (String) getBillPanel().getHeadItem(key).getValueObject();
					if(null == value){
						MessageDialog.showErrorDlg(getBillPanel(), "错误", "字段【"+SendMdmPropetys.handNotNullList.get(key)+"】不能为空，请输入后再试！");
						return;
					}
					if("mdmclass".equals(key)){
						materialvo.setDef14(value);
					}
				}
				
				//升级到主数据动作说明原主数据下没有相关信息，物料统一分类应由界面选择的值 为准，如果直接挂在物料分类下面无法修正分子公司不规范分类体系；所以，物料申请单及物料档案应有一个字段 来存储统一分类的数据。
				try {
					
					MaterialVO[] materials = new MaterialVO[]{materialvo};
					Map<String, Map<String, String>> insetMdmMaterialHost = getSenMdmSevecs().insetMdmMaterialHost(materials,getPk_org());
					Map<String, String> map = insetMdmMaterialHost.get(materialvo.getPk_material());
					//重新加载数据
					 materialvo.setDef7(map.get(SendMdmPropetys.MDMCODE));
					 materialvo.setDef14(map.get(SendMdmPropetys.MDMCLASS));
					
					//1先处理表头的MDMcode
					setHaedData();
					
					//2，重新查表体的集合
//					 List<Map<String,String>> bodydata = getMdmMaterialData(getMaterialvo());
					 initData(getMaterialvo());
//					 setBodyData( bodydata);
					 
					 //如果是申请节点，更新申请单数据
					if(null != pfvo){
						pfvo.setMaterial(materialvo);
						IMaterialPfService sv = NCLocator.getInstance().lookup(IMaterialPfService.class);
						sv.updateMaterialPfVO(pfvo);
					}
//					System.out.println();
				} catch (BusinessException e1) {
					MessageDialog.showErrorDlg(getBillPanel(), "失败", e1.getMessage());
				}
			}

			
		});
		return sendMdm;
	}
	
	/**
	 * 关联到主数据
	 * @return
	 */
	private JButton getLinkMdm(){
		JButton linkMdm = new JButton("关联到主数据");
		
		linkMdm.addActionListener(new ActionListener(){
			@SuppressWarnings("restriction")
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				// 数据检查，MDMcode是空的时候才可以执行，否则说明已经进行过关联，已经关联过的数据只能去做主数据修改，
				//修改数据不直接通过这个动作进行处理，1、加一个按钮 
				String mdmcode = (String) getBillPanel().getHeadItem(SendMdmPropetys.MDMCODE).getValueObject();
				String pk_measdoc = (String) getBillPanel().getHeadItem("pk_measdoc").getValueObject();
				if(null != mdmcode){
//					MessageDialog.showErrorDlg(getBillPanel(), "错误", "已经关联的数据，不能进行再次关联，如需调整请联系管理员！");
					int showHintDlg = MessageDialog.showOkCancelDlg(getBillPanel(), "重要", "此已经关联被主数据信息，是否确认切换关联数据？");
					if (2 == showHintDlg) 
					 return;
				}
				
				int selectedIndex = getBillPanel().getBodyPanel().getTable().getSelectedRow(); 
				 mdmcode = (String) getBillPanel().getBodyValueAt(selectedIndex, "mdm_code");
				String mdmclass = (String) getBillPanel().getBodyValueAt(selectedIndex, "groupMarbascode_@code");//
				String postunit = (String) getBillPanel().getBodyValueAt(selectedIndex, "postunit");//计量单位编码
				String postunit_name = (String) getBillPanel().getBodyValueAt(selectedIndex, "postunit_name");//计量单位名称
				
				try {
					checkUnit(postunit,postunit_name,pk_measdoc);
				} catch (BusinessException e3) {
					int showHintDlg = MessageDialog.showOkCancelDlg(getBillPanel(), "重要", "选定的主数据物料计量单位与当前物料不匹配，确认是否关联？\n（单位一致可能导致后续物料交换时出现问题）");
					if (2 == showHintDlg) 
					 return;
				}//检查计量单位的一致性
				
				try {
					mdmclass = getMdmitf().
					queryDocNameByID(mdmclass, " (select * from bd_defdoc where pk_defdoclist =(select pk_defdoclist from bd_defdoclist where code = 'GM002') and nvl(dr,0)=0 ) ", 
							"code", "pk_defdoc");
				} catch (DAOException e2) {
					// TODO Auto-generated catch block
					MessageDialog.showErrorDlg(getBillPanel(), "错误", "界面主数据分类错误【"+mdmclass+"】，请检查。");
				}
				if(null != mdmcode & null != mdmclass){
					getMaterialvo().setDef7(mdmcode);
					getMaterialvo().setDef14( mdmclass	);
				}else {
					MessageDialog.showErrorDlg(getBillPanel(), "错误", "未选择有效主数据信息，请重新选择。");
					 return;
				}
				try {
					
//					String pk_org = null == getPfvo() ? getOrg(getMaterialvo()) : getPfvo().getPk_org();
					UFBoolean insetMdmMaterialAssist = getSenMdmSevecs().insetMdmMaterialAssist(new MaterialVO[]{getMaterialvo()} ,getPk_org());
					//2、进入物料界面直接修改标编码【def7】的值 
					if( insetMdmMaterialAssist.booleanValue()){ 
						if( null != getPfvo()){//如果PFvo不为空时，为物料申请单，成功后对物料申请进行更新
							IMaterialPfService updatesev = NCLocator.getInstance().lookup(IMaterialPfService.class);
							updatesev.updateMaterialPfVO(getPfvo());
						}else{ //如果PF为空时，说明是物料档案，只对物料信息进行更新
							IMaterialBaseInfoService updateservice = NCLocator.getInstance().lookup(IMaterialBaseInfoService.class);
							updateservice.updateMaterial(getMaterialvo());
						}
					}
					setHaedData();
					MessageDialog.showHintDlg(getBillPanel(), "成功", "数据已经成功关联到主数据！");
				} catch (BusinessException e1) {
					MessageDialog.showHintDlg(getBillPanel(), "错误", e1.getMessage());
					 return; //执行中断
				}
			}

			private void checkUnit(String postunit, String postunit_name,String pk_measdoc) throws BusinessException {
			
					String queryDocNameByID = getMdmitf().queryDocNameByID(pk_measdoc, "bd_measdoc", "pk_measdoc", "name");
					if(null == queryDocNameByID || null == postunit_name || !postunit_name.equals(queryDocNameByID)){
					throw new BusinessException(" 计量单位不到致，请修改计量单位后再关联！");
					}
			
				
			}

		
			
		});
		return linkMdm;
	}

	
	
	/**
	 * 取消按钮
	 * @return
	 */
	private JButton getCancel(){
		JButton cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();//窗口关闭
			}
		});
		return cancel;
	}
	
	


	@Override
	public boolean beforeEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		if("mdmclass".equals(arg0.getKey())){

			materialvo.setDef14((String)billPanel.getHeadItem("mdmclass").getValueObject());
		}
		System.out.println();
	}

	/**
	 * 窗口表体行切换事件
	 */
	@Override
	public void bodyRowChange(BillEditEvent arg0) {
//		System.out.println("//将选择的主数据行上的主数据编码复制到表头！并禁用表头MDMcode的编辑！");
//		int selectedIndex = getBillPanel().getBodyPanel().getTable().getSelectedRow();
//		String mdm_code = (String) getBillPanel().getBodyValueAt(selectedIndex, "mdm_code");
//		String mdmclass = (String) getBillPanel().getBodyValueAt(selectedIndex, "groupMarbascode_@code");
//		//选择的主数据信息，主数据编码以及主数据分类不应该为空才对
//			if(null != pfvo){
//				((MaterialVO)pfvo.getMaterial()).setDef7(mdm_code);
//				((MaterialVO)pfvo.getMaterial()).setDef14(mdmclass);
//			}else {
//				materialvo.setDef7(mdm_code);
//				materialvo.setDef14(mdmclass);
//			}
//			getBillPanel().setHeadItem(SendMdmPropetys.MDMCODE, mdm_code);//界面还是不要更新的好，最终同步时还是由物料实体进行同步，这里展示只有物料本身已经被关联的数据才做显示，便可以锁定表头全部字段的编辑
		
	}
	
	/**
	 * 表体值切换事件
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int selectedIndex = getBillPanel().getBodyTabbedPane().getSelectedIndex(); 
		String mdm_code = (String) getBillPanel().getBodyValueAt(selectedIndex, "mdm_code");
		String mdmclass = (String) getBillPanel().getBodyValueAt(selectedIndex, "groupMarbascode_@code");
		//选择的主数据信息，主数据编码以及主数据分类不应该为空才对
			if(null != pfvo){
				((MaterialVO)pfvo.getMaterial()).setDef7(mdm_code);
				((MaterialVO)pfvo.getMaterial()).setDef14(mdmclass);
			}else {
				materialvo.setDef7(mdm_code);
				materialvo.setDef14(mdmclass);
			}
		
	}

	 
	

	
//	public String getPk_org() {
//		return Pk_org;
//	}
//
//	public void setPk_org(String pk_org) {
//		Pk_org = pk_org;
//	}
	public MaterialPfVO getPfvo() {
		return pfvo;
	}

	public void setPfvo(MaterialPfVO pfvo) {
		this.pfvo = pfvo;
	}
	
	public MaterialVO getMaterialvo() {
		return materialvo;
	}

	public void setMaterialvo(MaterialVO materialvo) {
		this.materialvo = materialvo;
	}

 
}