package nc.ui.scmf.ic.mbatchcode.action;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.cmp.utils.BillcodeGenerater;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.pu.m23.pubquery.IArrivePubQuery;
import nc.ui.jzqc.labelprint.action.CommonLabelPrintAction;
import nc.ui.pu.m23.action.SingletonLabelPrintAction;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.IMetaDataDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.scmf.ic.mbatchcode.dialog.LableReprintDlg;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.actions.RefreshSingleAction;
import nc.ui.uif2.editor.BatchBillTable;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.uif2.LoginContext;
import nc.vo.trade.checkrule.VOChecker;

/**
 * @author huotf
 * 标签重打 
 * 
 */
public class LableReprintAction extends SingletonLabelPrintAction {
	
	
	public void initPrintInfo() {
		
		setPreview(false);
		super.setCode("trayLabel");
		super.setBtnName("标签重打");
		setActioncode("trayLabel");
		setNodeKey("ot");
		setFuncode("H3010200401");
//		setTranstype("JZ01-Cxx-10");
		setActionname(getBtnName());
		putValue("ShortDescription", getActionname());
		putValue("AcceleratorKey", null);
	}
	// NCAction
	public LableReprintAction() {
		setCode("LableReprint");
		setBtnName("标签重打");
	}
	private BatchBillTableModel model;
	private BatchBillTable editor;
	
	private String pk;
	private String vdef2;
	private UFDate dproducedate;
	private UFDate dvalidate;
	private static final String FIXED_TEMPLATE_PK = "H3010200401";
	
	private CommonLabelPrintAction comAction = new CommonLabelPrintAction();;
	
	public BatchBillTableModel getModel() {
		return model;
	}
	public void setModel(BatchBillTableModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}
	public BatchBillTable getEditor() {
		return editor;
	}
	public void setEditor(BatchBillTable editor) {
		this.editor = editor;
	}
	
	@Override
	public void doAction(ActionEvent e) throws Exception {
		 List<Object> aggVO = this.editor.getModel().getRows();
//		BatchcodeVO aggVO = (BatchcodeVO) this.getForm().getValue();
//		Object  obj = model.getSelectedData();
		BatchcodeVO vo = (BatchcodeVO)aggVO.get(0);
		HashMap qryBatchVdef16 = qryBatchVdef16(vo.getPk_batchcode());
		Object object = qryBatchVdef16.get("vdef16");
		if(object == null){
			ExceptionUtils.wrappBusinessException("重打标记为N，请刷新后再试！");
		}
//		if(obj instanceof BatchcodeVO){
//			vo = (BatchcodeVO)obj;
//		}
		pk = vo.getPk_batchcode();
		dproducedate = vo.getDproducedate();
		dvalidate = vo.getDvalidate();
		LableReprintDlg trayDlg = new LableReprintDlg(this.editor.getModel()
				.getContext().getEntranceUI(), new BatchcodeVO[] { vo });
		int idok = trayDlg.showModal();

		if (UIDialog.ID_OK == idok) {
			BatchcodeVO[] srcvos = trayDlg.getBvos();
			VORowNoUtils.setVOsRowNoByRule(srcvos, "crowno");
			vdef2 = srcvos[0].getVdef2();
			List<AggLabelPrintHVO> list = changeLabelPrintHVO(srcvos[0], srcvos);
			// 保存标签数据
			Object o = savechangeAggLabelPrintHVO(list);
			 // 更新标签重打标记为N
			vo.setVdef16(null);
			vo.setAttributeValue("dr", 0);
			List<BatchcodeVO> list2 = new ArrayList();
			list2.add(vo);
			doApprove(list2);
			
			// 调用标签打印
			doLabelRePrint((AggLabelPrintHVO[]) o);
			ShowStatusBarMsgUtil.showStatusBarMsg("标签打印完成！", this.editor.getModel()
					.getContext());
		}
	}
	
	
	/**
     * 标签重打（固定模板）
     */
    public void doLabelRePrint(AggLabelPrintHVO[] vo) {
        try {
            // 2. 打印入口
            PrintEntry printEntry = new PrintEntry(this.editor.getModel()
    				.getContext().getEntranceUI());

            LoginContext ctx = this.editor.getModel().getContext();
            String bq = "H3010200404";
    		if("合格标签".equals(vdef2)){
    			bq = "H3010200404";
    		}else if("不合格标签".equals(vdef2)){
    			bq = "H3010200408";
    		}else if("过期标签".equals(vdef2)){
    			bq = "H3010200405";
    		}else if("分装标签".equals(vdef2)){
    			bq = "H3010200406";
    		}
    		
            // 3. 直接设置固定模板（关键！不再弹选择框）
            printEntry.setTemplateID(ctx.getPk_group(), bq,
    				ctx.getPk_loginUser(), null, "ot");

            // 4. 批量打印开始
//            printEntry.beginBatchPrint();

            // 5. 设置数据源：标签档案
            IMetaDataDataSource[] defaultDataSource = getDefaultMetaDataSource(vo);
            if (!VOChecker.isEmpty(defaultDataSource)) {
    			for (IMetaDataDataSource dataSourceItem : defaultDataSource) {
    				printEntry.setDataSource(dataSourceItem);
    				printEntry.setAdjustable(isAdjustable());
    			}
    		} else {
    			return;
    		}
//            printEntry.setDataSource(new Object[] { labelVO });

            // 6. 直接预览（正式用可改成 print()）
            printEntry.preview();

            printEntry.endBatchPrint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private IMetaDataDataSource[] getDefaultMetaDataSource(Object[] datas) {
		IMetaDataDataSource[] defaultDataSource = null;
		if (!VOChecker.isEmpty(datas)) {
			if (true) {
				defaultDataSource = new MetaDataSource[datas.length];
				for (int i = 0; i < defaultDataSource.length; i++) {
					defaultDataSource[i] = new MetaDataSource(
							new Object[] { datas[i] });
				}
			} else {
				defaultDataSource = new MetaDataSource[] { new MetaDataSource(
						datas) };
			}
		}
		return defaultDataSource;
	}
    
    /**
	 * 查询批次vdef16
	 * @param material
	 * @return
	 * @throws BusinessException
	 */
	public HashMap qryBatchVdef16(String batch)
			throws BusinessException {
		String sql = " select vdef16  from scm_batchcode where pk_batchcode='"
				+ batch + "'  and dr=0";
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
		 * 查询物料
		 * @param material
		 * @return
		 * @throws BusinessException
		 */
		public HashMap qryMaterial(String material)
				throws BusinessException {
			String sql = " select code,name,materialspec,pk_measdoc  from bd_material where pk_material='"
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
	protected List<AggLabelPrintHVO> changeLabelPrintHVO(BatchcodeVO totalvo,
			BatchcodeVO[] bvos) throws ValidationException, BusinessException {
		if (bvos.length == 0 || bvos[0] == null) {
			ExceptionUtils.wrappBusinessException("没有要打印的数据！");
		}
		List<AggLabelPrintHVO> list = new ArrayList<>();
		int size = bvos.length;
		// 默认值
		String pk_group = "0001V110000000000FH0";
		String pk_org = "0001V110000000012E56";
		
		String vdef2 = totalvo.getVdef2();
		String cmaterialoid = totalvo.getCmaterialoid();
		HashMap qryMaterial = qryMaterial(cmaterialoid);
		String transtype = "JZ01-Cxx-25";
		if("合格标签".equals(vdef2)){
			transtype = "JZ01-Cxx-25";
		}else if("不合格标签".equals(vdef2)){
			transtype = "JZ01-Cxx-45";
		}else if("过期标签".equals(vdef2)){
			transtype = "JZ01-Cxx-30";
		}else if("分装标签".equals(vdef2)){
			transtype = "JZ01-Cxx-35";
		}
		int i = 10;
		for (BatchcodeVO bvo : bvos) {
			AggLabelPrintHVO aggvo = new AggLabelPrintHVO();
			LabelPrintHVO hvo = new LabelPrintHVO();
			String vdef1 = bvo.getVdef1();// 数量
			String vdef4 = bvo.getVdef4();// 总数量
			String vdef5 = bvo.getVdef5();// 包装规格
			// 设置主组织默认值
			hvo.setAttributeValue("pk_group", pk_group);
			hvo.setAttributeValue("pk_org", pk_org);
			hvo.setAttributeValue("pkorg", pk_org);
			// 设置单据状态、单据业务日期默认值
			hvo.setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
			hvo.setAttributeValue("billdate",
					new UFDate(System.currentTimeMillis()));
			hvo.setAttributeValue("billtype", "JZ01");
			// TODO 待定
			hvo.setAttributeValue("transtype", transtype);
			hvo.setAttributeValue("transtypepk", PfServiceScmUtil
					.getTrantypeidByCode(new String[] { transtype })
					.get(transtype));

			hvo.setAttributeValue("creationtime",
					new UFDateTime(System.currentTimeMillis()));
			hvo.setAttributeValue("creator", this.editor.getModel().getContext()
					.getPk_loginUser());
			hvo.setAttributeValue("maketime",
					new UFDateTime(System.currentTimeMillis()));
			hvo.setAttributeValue("billmaker", this.editor.getModel().getContext()
					.getPk_loginUser());

			hvo.setAttributeValue("iprintcount", 0);// 打印次数
			hvo.setSrcbilltype("pc");// 标签类型
			hvo.setBlabelstatus(UFBoolean.TRUE);// 标签状态
			hvo.setBprintstatus(UFBoolean.TRUE);// 可打印状态
			hvo.setSrcbillid(totalvo.getPk_batchcode());// 批次档案id
			hvo.setSrcbillrowid(bvo.getPk_batchcode());// 批次档案子表id
			hvo.setPk_material(bvo.getCmaterialoid());// 物料主键
			hvo.setPk_srcmaterial(bvo.getCmaterialoid());// 物料版本主键
			hvo.setNum_b(new UFDouble(vdef1));// 重量
			hvo.setCastunitid(String.valueOf(qryMaterial.get("pk_measdoc")));// 包装单位
			hvo.setCunitid(String.valueOf(qryMaterial.get("pk_measdoc")));// 计量单位
			hvo.setVbatchcode(bvo.getVbatchcode());// 批次号
			hvo.setPk_batchcode(pk);
			hvo.setBc_vvendbatchcode(bvo.getVbatchcode());// 供应商批次号
			hvo.setAmount(new UFDouble(vdef4));// 总包装数量
			hvo.setSerial_number(i / 10);// 标签序号
			i = i +10;
			hvo.setSerial_total(size);
			hvo.setProduceno(bvo.getVbatchcode());
			hvo.setCouterpackspec(vdef5);// 包装规格
			hvo.setNum(new UFDouble(vdef4));// 批次总数量
			hvo.setDproducedate(dproducedate);// 生产日期
			hvo.setEnddate(dvalidate);// 复测日期
			aggvo.setParentVO(hvo);
			list.add(aggvo);
		}
		return list;
	}
	
	/**
	 * 保存标签
	 * @param list
	 * @return
	 */
	protected Object savechangeAggLabelPrintHVO(List<AggLabelPrintHVO> list) {
		Object o = PfServiceScmUtil.processBatch("SAVEBASE", "JZ01",
				list.toArray(new AggLabelPrintHVO[list.size()]), null, null);
		return o;
	}
	/**
	 * 更新批次档案
	 * @param list
	 * @return
	 * @throws BusinessException
	 */
	private void doApprove(List<BatchcodeVO> list) throws BusinessException{
		IVOPersistence ivoPersistence=(IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class);
		ivoPersistence.updateVOList(list);
	}
	
	 @Override
	protected boolean isActionEnable() {
		 List<Object> aggVO1 = this.editor.getModel().getRows();
		 if(null == aggVO1 || aggVO1.size() == 0){
			 return false;
		 }
		 BatchcodeVO aggVO2 =(BatchcodeVO)aggVO1.get(0);
		 if(null == aggVO2){
			 return false;
		 }else{
				String vdef16 = aggVO2.getVdef16();
				 if("Y".equals(vdef16)){
					 return true;
				 }else {
					 return false;
				 }
			}
	}
}



