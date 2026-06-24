package nc.ui.scmf.ic.mbatchcode.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.ic.onhand.OnhandResService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.ObjectUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 
 * @author htf 20260330
 *
 */
public class LableReprintDlg extends UIDialog implements BillEditListener2,
BillEditListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private UIButton btnCancle;
	private UIButton btnOk;
	
	private UIButton addline;
	private UIButton delline;
	private boolean isCheckPass;
	private boolean isCloseByBtnOK;
	private UIPanel m_pnlButton;
	private BillCardPanel pnlCardpanel;
	// 我的 1001AA100000001BMRIX  之前：1001AA100000000BXAOC
	private String billTempletID = "1001AA100000001BMRIX";
	private ArriveItemVO[] bvos;
	
	private BatchcodeVO[] batvos;
	
    private UIRefPane ref;
    // 业务数据
    private double xcl;  // 当前现存量2
    // 40010815-1
    
    private BatchcodeVO getvo = new BatchcodeVO();
    private BatchcodeVO[] bacthvos;
	
	public LableReprintDlg(Container parent, BatchcodeVO[] bvos) {
	super(parent);
	initialize(bvos);
	}
	
	public void actionPerformed(ActionEvent e) {
	if (e.getSource() == btnOk) {
		super.setResult(1);
		onSplitOK();
	} else if (e.getSource() == btnCancle) {
		super.setResult(2);
		dispose();
	} else if (e.getSource() == addline) {
		getBillCardPanel().getBodyPanel().getTableModel()
				.copyLine(new int[] { 0 });
		getBillCardPanel().pasteLineToTail();
		calncanreplnumBacth();
	} else if (e.getSource() == delline) {
		getBillCardPanel().delLine();
		calncanreplnumBacth();
	}
	}
	
	public boolean beforeEdit(BillEditEvent e) {
	pnlCardpanel.stopEditing();
	
	if ((e.getPos() == 1) && ("values".equals(e.getKey()))) {
	
	}
	
	return true;
	}
	
	public boolean isCloseByBtnOK() {
	return isCloseByBtnOK;
	}
	
	private BillCardPanel getBillCardPanel() {
	if (pnlCardpanel == null) {
		pnlCardpanel = new BillCardPanel();
		pnlCardpanel.loadTemplet(billTempletID);
		pnlCardpanel.setSize(getContentPane().size());
		pnlCardpanel.setDividerProportion(0.2);
		pnlCardpanel.getBodyPanel().getTable().setSelectionMode(0);
		pnlCardpanel.addBodyEditListener2(this);
		pnlCardpanel.addEditListener(this);
		pnlCardpanel.setBodyMenuShow(false);
	}
	return pnlCardpanel;
	}
	
	// 标签类型选项
    private final String[] labelTypes = {"合格标签", "拒绝标签", "过期标签", "封装标签"};
    
	/**
     * 创建顶部信息面板（包含新增的字段）
     */
    private UIPanel createTopInfoPanel() {
        return new UIPanel(new GridBagLayout());
    }
    
	private UIPanel getUIPanelButton() {
	if (m_pnlButton == null) {
		
		
		btnOk = new UIButton("保存");
		btnOk.setSize(60, 22);
		btnOk.addActionListener(this);
		btnCancle = new UIButton(NCLangRes.getInstance().getStrByID(
				"pubapp_0", "0pubapp-0020"));
		btnCancle.setSize(btnOk.getSize());
		btnCancle.addActionListener(this);
	
		addline = new UIButton("增行");
		addline.setSize(60, 22);
		addline.addActionListener(this);
	
		delline = new UIButton("删行");
		delline.setSize(60, 22);
		delline.addActionListener(this);
		m_pnlButton = new UIPanel();
		m_pnlButton.setLayout(new FlowLayout(1));
		m_pnlButton.add(addline);
		m_pnlButton.add(delline);
		m_pnlButton.add(btnOk);
		m_pnlButton.add(btnCancle);
		
		
	
	}
	return m_pnlButton;
	}
	
	private void initialize(BatchcodeVO[] bvos) {
	try {
		setSize(1000, 500);
		setTitle("标签重打");
		getContentPane().setLayout(new BorderLayout());
		// 我的
		// 创建顶部信息面板
		getContentPane().add(createTopInfoPanel(), BorderLayout.NORTH);
		getContentPane().add(getBillCardPanel(), "Center");
	
		getContentPane().add(getUIPanelButton(), "South");
		
		// 我的 cmaterialoid
		BatchcodeVO vo = bvos[0];
		getvo = vo;
		// 1. 查询物料名称和编码，赋值表头
		HashMap qryMaterial = qryMaterial(vo.getCmaterialoid());
		BillItem code = pnlCardpanel.getHeadItem("cmaterialoid.code");
        if(code != null){
        	code.setValue(qryMaterial.get("code"));
        	code.setEdit(false);
		}
        BillItem name = pnlCardpanel.getHeadItem("cmaterialoid.name");
        if(name != null){
        	name.setValue(qryMaterial.get("name"));
        	name.setEdit(false);
		}
        // 2. 批次号
        BillItem vbatchcode = pnlCardpanel.getHeadItem("vbatchcode");
		Object value = vbatchcode.getValueObject();
		if(vbatchcode != null){
			vbatchcode.setValue(vo.getVbatchcode());
			vbatchcode.setEdit(false);
		}
		// 3. 现存量，查
		xcl = getStockQty(vo.getCmaterialoid(),vo.getVbatchcode());
        BillItem xcl2 = pnlCardpanel.getHeadItem("xcl");
        if(xcl2 != null){
        	xcl2.setValue(xcl);
        	xcl2.setEdit(false);
		}
     // 4.标准包装规格
        BillItem bzbzgg = pnlCardpanel.getHeadItem("bzbzgg");
        // 药物科技是默认组织 28 
        ref = (UIRefPane)bzbzgg.getComponent();
        ref.setPk_org("0001V110000000012E56");
        
		// 表体数量
//		getBillCardPanel().getBillModel().setBodyDataVO(bvos);
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().getBillModel().loadLoadRelationItemValue();
	} catch (Exception ivjExc) {
		handleException(ivjExc);
	}
	}
	
	
	/**
	 * 查询物料
	 * @param material
	 * @return
	 * @throws BusinessException
	 */
	public HashMap qryMaterial(String material)
			throws BusinessException {
		String sql = " select code,name  from bd_material where pk_material='"
				+ material + "'  and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	
		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs
				.executeQuery(sql, new MapProcessor());
	
		if (hashMap2 != null && hashMap2.size() > 0) {
			return hashMap2;
		}
		return new HashMap<String, Object>();
	
	}

	 /**
     * 获取现存量（需要根据实际业务实现）
	 * @throws BusinessException 
     */
    private double getStockQty(String pk_material ,String vbatchcode) throws BusinessException {
        // TODO: 查询物料在所有仓库的现存量
        // 可以通过调用库存服务或查询库存台账表实现
    	OnhandDimVO onhandDimVO = new OnhandDimVO();
		onhandDimVO.setCmaterialvid(pk_material);
		onhandDimVO.setVbatchcode(vbatchcode);
		double sum = 0.0;
		OnhandVO[] onhandVOs = NCLocator.getInstance().lookup(OnhandResService.class).queryOnhandVOByDims(new OnhandDimVO[]{onhandDimVO});
		if(onhandVOs == null || onhandVOs.length == 0){
			return sum; 
		}
		for(OnhandVO hand : onhandVOs){
			sum = sum + hand.getNonhandnum().doubleValue();
		}
        return sum;  
		// TODO 暂时给个固定值
//		 return 100;  
    }
	private void onSplitOK() {
	try {
		BatchcodeVO[] vos = (BatchcodeVO[]) pnlCardpanel.getBillModel()
				.getBodyValueVOs(BatchcodeVO.class.getName());
		setBvos(vos);
		isCloseByBtnOK = true;
		dispose();
	} catch (Exception e1) {
		e1.printStackTrace();
		handleException(e1);
	}
	
	}
	
	public BatchcodeVO[] getBvos() {
	return batvos;
	}
	
	public void setBvos(BatchcodeVO[] bvos) {
	this.batvos = bvos;
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
	
	// ncanarrivenum 单托数量
	// ncanreplnum 已分配数量
	// nastnum 数量
	try {
		String key = e.getKey();
		Object value = e.getValue();
		if (e.getPos() == BillItem.HEAD) {
			if("bzbzgg".equals(key)){// 标准包装规格
				String bzbzgg = getBillCardPanel().getHeadItem("bzbzgg").getValue();
				HashMap strjz = qryJZ(bzbzgg);
				BillItem jz = getBillCardPanel().getHeadItem("jz");
				// 选择标准包装规格后带出净重
				if(jz != null){
		        	jz.setValue(strjz.get("def2"));
		        	jz.setEdit(false);
				}
				// 根据现存量-已分配，再除以净重，计算生成的表体行数
				UFDouble ufxcl = new UFDouble(xcl);
				String yfpstr = getBillCardPanel().getHeadItem("yfpsl").getValue();
				String bqlx = getBillCardPanel().getHeadItem("bqlx").getValue();
				UFDouble ufyfp = new UFDouble(yfpstr);
				UFDouble ufjz = new UFDouble(String.valueOf(strjz.get("def2")));
				
				
				List<BatchcodeVO> list = new ArrayList<>();
				UFDouble ntempnum = SafeCompute.sub(ufxcl, ufyfp);
				if (ntempnum.compareTo(UFDouble.ZERO_DBL) > 0) {
					// 取到现有行
					BatchcodeVO[] getlist = (BatchcodeVO[])getBillCardPanel().getBillModel().getBodyValueVOs(BatchcodeVO.class.getName());
					if(getlist != null && getlist.length>0){
						for(BatchcodeVO vo:getlist){
							vo.setVdef2(bqlx);
							vo.setVdef3("0001V110000000012E56");
							vo.setVdef4(String.valueOf(xcl));
							vo.setVdef5(bzbzgg);
							list.add(vo);
						}
						
					}
					// 加上重新计算的行
					bacthvos = calBacthVOs(getvo, ntempnum, ufjz);
					for (BatchcodeVO bvo : bacthvos) {
						bvo.setVdef2(bqlx);
						bvo.setVdef3("0001V110000000012E56");
						bvo.setVdef4(String.valueOf(xcl));
						bvo.setVdef5(bzbzgg);
						list.add(bvo);
					}
				}
				
				// 生成表体行后，计算已分配数量
				getBillCardPanel().getBillModel().setBodyDataVO(
						list.toArray(new BatchcodeVO[list.size()]));
				getBillCardPanel().getBillModel().execLoadFormula();
				getBillCardPanel().getBillModel()
						.loadLoadRelationItemValue();
				calncanreplnumBacth();
			}
			
		} else if (e.getPos() == BillItem.BODY) {
			if ("nastnum".equals(key)) {// 数量
				calncanreplnum();
			}
		}
	
	} catch (Exception e1) {
		handleException(e1);
	}
	
	}
	/**
	 * 根据包装规格查净重
	 * @param bzbzgg
	 * @return
	 * @throws BusinessException 
	 */
	private HashMap qryJZ(String bzbzgg) throws BusinessException {
		// TODO Auto-generated method stub
		String sql = " select def2 from bd_defdoc where pk_defdoc='"
				+ bzbzgg + "'  and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	
		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs
				.executeQuery(sql, new MapProcessor());
	
		if (hashMap2 != null && hashMap2.size() > 0) {
			return hashMap2;
		}
		return new HashMap<String, Object>();
	}

	/**
	 * 重新计算：已分配数量
	 */
	private void calncanreplnumBacth() {
		BatchcodeVO[] getlist = (BatchcodeVO[])getBillCardPanel().getBillModel().getBodyValueVOs(BatchcodeVO.class.getName());
		UFDouble total = UFDouble.ZERO_DBL;
		for (BatchcodeVO vo: getlist) {
			String vdef1 = vo.getVdef1();
			UFDouble nastnum = new UFDouble(vdef1);
			total = SafeCompute.add(total, nastnum);
		}
		getBillCardPanel().getHeadItem("yfpsl").setValue(total);// 已分配数量
		
		}
	
	private void calncanreplnum() {
	int rowcount = getBillCardPanel().getBillModel().getRowCount();
	UFDouble total = UFDouble.ZERO_DBL;
	for (int i = 0; i < rowcount; i++) {
		UFDouble nastnum = (UFDouble) getBillCardPanel().getBillModel()
				.getValueAt(i, "nastnum");
		total = SafeCompute.add(total, nastnum);
	}
	getBillCardPanel().getHeadItem("ncanreplnum").setValue(total);// 已分配数量
	
	}
	
	private void handleException(Exception exception) {
	try {
		ExceptionUtils.wrappException(exception);
	} catch (Exception e) {
		Logger.warn(e.getMessage(), e);
		MessageDialog.showErrorDlg(this, null, e.getMessage());
	}
	}
	
	@Override
	public void bodyRowChange(BillEditEvent e) {
	
	}
	
	/**
	 * 计算批次VO
	 * @param hvo
	 * @param ntempnum
	 * @param ncanarrivenum
	 * @return
	 * @throws Exception
	 */
	private BatchcodeVO[] calBacthVOs(BatchcodeVO hvo,
			UFDouble ntempnum, UFDouble ncanarrivenum) throws Exception {
		
		BigDecimal[] bigs = getNnpiece(ntempnum, ncanarrivenum);
		
		int size = bigs[0].intValue();
		List<BatchcodeVO> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			BatchcodeVO sbvo = getBacthVO(hvo, bigs[2]);
			list.add(sbvo);
		}
		
		if (bigs[1].compareTo(BigDecimal.ZERO) != 0) {
			BatchcodeVO bvo1 = getBacthVO(hvo, bigs[1]);
			list.add(bvo1);
		}
		
		return list.toArray(new BatchcodeVO[list.size()]);
		}
	
	private ArriveItemVO[] calArriveItemVOs(ArriveItemVO hvo,
		UFDouble ntempnum, UFDouble ncanarrivenum) throws Exception {
	
	hvo.setCrowno(null);
	BigDecimal[] bigs = getNnpiece(ntempnum, ncanarrivenum);
	
	int size = bigs[0].intValue();
	List<ArriveItemVO> list = new ArrayList<>();
	for (int i = 0; i < size; i++) {
		ArriveItemVO sbvo = getArriveItemVO(hvo, bigs[2]);
		list.add(sbvo);
	}
	
	if (bigs[1].compareTo(BigDecimal.ZERO) != 0) {
		ArriveItemVO bvo1 = getArriveItemVO(hvo, bigs[1]);
		list.add(bvo1);
	}
	
	return list.toArray(new ArriveItemVO[list.size()]);
	}
	
	/**
	 * 获取批次VO
	 * @param hvo
	 * @param npiece
	 * @return
	 * @throws Exception
	 */
	private BatchcodeVO getBacthVO(BatchcodeVO hvo, BigDecimal npiece)
			throws Exception {
		BatchcodeVO sbvo = (BatchcodeVO) ObjectUtils.serializableClone(hvo);
		sbvo.setVdef1(String.valueOf(npiece));
		return sbvo;
		}
	
	private ArriveItemVO getArriveItemVO(ArriveItemVO hvo, BigDecimal npiece)
		throws Exception {
	ArriveItemVO sbvo = (ArriveItemVO) ObjectUtils.serializableClone(hvo);
	sbvo.setNastnum(new UFDouble(npiece));
	return sbvo;
	}
	
	// 商 和 余数
	private BigDecimal[] getNnpiece(UFDouble ntempnum, UFDouble ncanarrivenum)
		throws BusinessException {
	
	BigDecimal bweight = new BigDecimal(ncanarrivenum.doubleValue());
	BigDecimal bgrosswt = ntempnum.toBigDecimal();
	BigDecimal[] bigs = bgrosswt.divideAndRemainder(bweight);
	
	List<BigDecimal> list = new ArrayList<>();
	for (BigDecimal big : bigs) {
		list.add(big);
	}
	list.add(bweight);
	
	return list.toArray(new BigDecimal[list.size()]);
	}
}