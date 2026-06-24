package nc.ui.bc.ytmprint.action;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.uap.lock.PKLock;
import nc.impl.pubapp.bd.material.assistant.MarAssistantCheckUtils;
import nc.impl.pubapp.bd.material.assistant.MarAsstValidationService;
import nc.itf.bd.material.stock.IMaterialStockQueryService;
import nc.itf.qilibc.ITmprintMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.md.data.access.DASFacade;
import nc.md.data.access.NCObject;
import nc.pubitf.para.SysInitQuery;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pubapp.uif2app.actions.AbstractTemplatePrintAction;
import nc.ui.qilibc.tmprint.handler.BodyAfterEditHandler;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.pub.lang.RuleRes;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.qilibc.barcodeconfig.AggBarcodeconfigVO;
import nc.vo.qilibc.barcodeconfig.BarcodeconfigBVO;
import nc.vo.qilibc.barcodeconfig.BarcodeconfigVO;
import nc.vo.qilibc.streamconfig.StreamconfigVO;
import nc.vo.qilibc.tmprintupdate.TmprintupdateVO;
import nc.vo.tmprint.tmprint.TmprintVO;
import nc.vo.to.pub.entity.SimpleAggregatedValueObject;
import nc.vo.uif2.LoginContext;
/**
 * 物料条码打印  打印按钮
 * @author LY
 *
 */
public class TmPrintAction extends AbstractTemplatePrintAction{
    private BatchBillTableModel model;
    MyPrintProcessor listener = null;
	public TmPrintAction() {
		super();
		setBtnName("打印预览");
//		String str = NCLangRes.getInstance().getStrByID("common", "40H1-BUTTON0003")/*打印*/;
//		setBtnName(str);
		setCode("printsaction");
	}
	
	@Override
	protected Object[] getBillVO() {
		BillCardPanel cardPanel = getEditor().getBillCardPanel();
		BillModel billModel = cardPanel.getBillModel();
		int rowCount = cardPanel.getRowCount();
		List<TmprintVO> printdatas = new ArrayList<TmprintVO>();
		List<Integer> selectrow=new ArrayList<Integer>();
		for(int i = 0; i < rowCount; i++){
			if(billModel.getRowState(i)==4){
				selectrow.add(i);
			}
		}
	    for (int i = 0; i < selectrow.size(); i++) {
    		TmprintVO vo = (TmprintVO) billModel.getBodyValueRowVO(selectrow.get(i), TmprintVO.class.getName());
    		//根据打印份数进行拆行---- mod by ly 2018-10-29-----start-----
	    	//int num = vo.getPrintnum();
			//for(int j = 0; j < num; j++){
		    	//printdatas.add((TmprintVO)vo.clone());
			//}
			//根据打印份数进行拆行---- mod by ly 2018-10-29-----end-----
    		vo.setPrinter(AppContext.getInstance().getPkUser());
    		billModel.setValueAt(AppContext.getInstance().getPkUser(), selectrow.get(i), "printer");
    		billModel.setBodyRowVO(vo, selectrow.get(i));
			billModel.loadLoadRelationItemValue();
			printdatas.add(vo);
        }
        AggregatedValueObject vos[] = new AggregatedValueObject[1];
        SimpleAggregatedValueObject billVO3 = new SimpleAggregatedValueObject();
        billVO3.setChildrenVO(printdatas.toArray(new TmprintVO[printdatas.size()]));
        billVO3.setParentVO(null);
        vos[0] = billVO3;
        return vos;
	}
	@Override
	public void doAction(ActionEvent arg0) throws Exception {
		try{
			setPreview(true);
			setBtnName("打印预览");
//			String str = NCLangRes.getInstance().getStrByID("common", "40H1-BUTTON0003")/*打印*/;
//			setBtnName(str);
			setCode("printsaction");
			BillCardPanel cardPanel = getEditor().getBillCardPanel();
			BillModel billModel = cardPanel.getBillModel();
			cardPanel.stopEditing();
			int rowCount = cardPanel.getRowCount();
			List<TmprintupdateVO> printsVO = new ArrayList<TmprintupdateVO>();
			List<TmprintVO> qcprintsVO = new ArrayList<TmprintVO>();//存储期初打印表体行数据
			List<Integer> selectrow=new ArrayList<Integer>();
			for(int i = 0; i < rowCount; i++){
				if(billModel.getRowState(i)==4){
					selectrow.add(i);
				}
			}
		    if (selectrow == null || selectrow.size() == 0) {
		      // 未选中数据，不允许执行此操作
//				throw new BusinessException("未选中数据，不允许执行此操作！");
				throw new BusinessException(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0059")/*未选中数据，不允许执行此操作！*/);
		    }
		  //判断表体选中行必输项  start--------------------------------------
			BillItem[] items = cardPanel.getBillModel().getBodyItems();
			//存非空字段名值
			ArrayList<BillItem> notNullItems = new ArrayList<BillItem>();
			for(int i = 0; i < items.length; i++){
				if(items[i].isNull()){
					notNullItems.add(items[i]);
				}
			}
			//错误信息
			StringBuffer message = new StringBuffer();
			if(notNullItems != null && notNullItems.size() > 0 ){
				for(int i = 0; i < selectrow.size(); i ++){
					int nowrow = selectrow.get(i) + 1;
					StringBuffer newmessage = new StringBuffer();
					Boolean flag = false;
					for (BillItem billItem : notNullItems) {
						Object value = billModel.getValueAt(selectrow.get(i), billItem.getKey());
						if(value == null){
							newmessage.append("、["+billItem.getName()+"]");
							billModel.cellShowWarning(selectrow.get(i),  billItem.getKey());
							flag = true;
						}
					}
					if(flag){
//						message.append("第"+nowrow+"行：");
						message.append(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0060",null,new String[]{
								nowrow+""
						})/*第{0}行：*/);
						message.append(newmessage.substring(1));
					}
				}
			}
			if(message != null && message.length() > 0){
//				throw new BusinessException("下列字段不能为空：\n" +message+"\n请修改后再打印！");
				throw new BusinessException(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0061",null,new String[]{
						message+""
				})/*下列字段不能为空：\n{0}\n请修改后再打印！*/);
			}
			//判断表体选中行必输项  end--------------------------------------
			//打印后更新条码配置标识
			List<TmprintVO> prints = new ArrayList<TmprintVO>();
			Set<String> pk_materialSet = new HashSet<String>();
		    for (int i = 0; i < selectrow.size(); i++) {
		    	billModel.setValueAt(AppContext.getInstance().getServerTime(), selectrow.get(i), "printtime");
		    	billModel.setValueAt(AppContext.getInstance().getPkUser(), selectrow.get(i), "printer");
		    	TmprintVO vo = (TmprintVO) billModel.getBodyValueRowVO(selectrow.get(i), TmprintVO.class.getName());
		    	String pk_config = vo.getPk_config();
		    	//如果未找到规则，则获取一次
		    	if(pk_config == null || "".equals(pk_config)){
		    		new BodyAfterEditHandler().upBarConfig(billModel, selectrow.get(i), vo.getPk_material(), vo.getPk_org(), null);
		    		vo = (TmprintVO) billModel.getBodyValueRowVO(selectrow.get(i), TmprintVO.class.getName());
		    	}
		    	if(vo.getPk_material()!=null && !"".equals(vo.getPk_material())){
		    		pk_materialSet.add(vo.getPk_material());
		    	}
		    	vo.setCrowno((selectrow.get(i)+1)+"");
		    	prints.add(vo);
	       }
		    billModel.loadLoadRelationItemValue();
//			InvInfoQuery invQuery = InvInfoUIQuery.getInstance().getInvInfoQuery();
//			Map<String, InvBasVO> invBasVOs = invQuery.getInvBasVOs(pk_materialSet.toArray(new String[0]));
//		   chekVfree(prints,invBasVOs);
			//更新之前锁定表
			boolean islock = false;
			islock = PKLock.getInstance().acquireBatchLock(
					new String[]{"qilibc_tmprint"}, AppContext.getInstance().getPkUser(), "");
			if(!islock){
//				throw new BusinessException("正在同步操作，请稍后再试");
				throw new BusinessException(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0062")/*正在同步操作，请稍后再试*/);
			}
			IMaterialStockQueryService query = NCLocator.getInstance().lookup(
					IMaterialStockQueryService.class);
			//存放流水号配置Map  key:组织主键
			HashMap<String,StreamconfigVO> streamMap = new HashMap<String,StreamconfigVO>();
			//存放条码配置Map   key:条码配置主键
			HashMap<String,AggBarcodeconfigVO> configMap = new HashMap<String,AggBarcodeconfigVO>();
			//存放是否流水号         key:条码配置主键
			HashMap<String,Boolean> isLshMap = new HashMap<String,Boolean>();
			//存放是否序列号         key:条码配置主键
			HashMap<String,Boolean> isSerialMap = new HashMap<String,Boolean>();
			for(TmprintVO printVO1:prints){
		    	String pk_configs = printVO1.getPk_config();
		    	String pk_group1 = printVO1.getPk_group();
		    	String pk_org1 = printVO1.getPk_org();
				String pk_material1 = printVO1.getPk_material();
				String crowno1 = printVO1.getCrowno();
			    //条码配置项
				AggBarcodeconfigVO configVO = null;
				if(configMap.containsKey(pk_configs)){
					configVO = configMap.get(pk_configs);
				}else{
					configVO = (AggBarcodeconfigVO)HYPubBO_Client.queryBillVOByPrimaryKey(new String[]{AggBarcodeconfigVO.class.getName(),BarcodeconfigVO.class.getName(),BarcodeconfigBVO.class.getName()}, pk_configs);
					if(configVO == null){
//						showBarMsg("请先在“条码配置”界面配置条码规则后重新打印！");
						showBarMsg(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0063")/*请先在“条码配置”界面配置条码规则后重新打印！*/);
						return;
					}
					configMap.put(pk_configs, configVO);
					//是否根据流水号规则
					Boolean isLsh = false;
					Boolean isSerial = false;
					BarcodeconfigBVO[] configBVOs = (BarcodeconfigBVO[]) configVO.getChildrenVO();
					for(int j = 0; j < configBVOs.length; j++){
						if("lsh".equals(configBVOs[j].getItemcode())){
							isLsh = true;
						}
						if("serialcode".equals(configBVOs[j].getItemcode())){
							isSerial = true;
						}
					}
					isLshMap.put(pk_configs, isLsh);
					isSerialMap.put(pk_configs, isSerial);
				}
				//判断批次管理物料是否录入批次
				MaterialStockVO[] stockvos = query.queryMaterialStockVOs(new String[] { pk_org1  },
						pk_material1);
				if(stockvos !=null && stockvos.length>0){
					MaterialStockVO stockvo = stockvos[0];
					
					//是否批次管理、保质期管理
					if(stockvo!=null && stockvo.getWholemanaflag()!=null&&stockvo.getWholemanaflag().booleanValue()){
						if(printVO1.getVbatchcode()==null || "".equals(printVO1.getVbatchcode())){
							throw new Exception(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0064",null,new String[]{
									crowno1
							})/*第{0}行为批次管理物料，请录入批次后重新打印！*/);
						}
					}else{
						printVO1.setVbatchcode(null);
					}
//					if(stockvo!=null && stockvo.getQualitymanflag()!=null&&stockvo.getQualitymanflag().booleanValue()){
//						if(printVO1.getDproducedate()==null || "".equals(printVO1.getDproducedate())){
//							throw new Exception(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0121",null,new String[]{
//									crowno1
//							})/*第{0}行为保质期管理物料，请录入生产日期后重新打印！*/);
//						}
//					}

					//序列号管理
					if(stockvo!=null && stockvo.getSerialmanaflag()!=null&&stockvo.getSerialmanaflag().booleanValue()){
						if(!(isSerialMap.get(pk_configs))){
							throw new Exception(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0065",null,new String[]{
									crowno1
							})/*第{0}行为序列号管理物料，必须分配序列号条码规则！*/);
						}
						if(printVO1.getSerialcode()==null || "".equals(printVO1.getSerialcode())){
							throw new Exception(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0066",null,new String[]{
									crowno1
							})/*第{0}行为序列号管理物料，请点击生成序列号！*/);
						}
						if(printVO1.getNassistnum()==null || "".equals(printVO1.getNassistnum()) || !printVO1.getNassistnum().equals(UFDouble.ONE_DBL)){
							throw new Exception(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0067",null,new String[]{
									crowno1
							})/*第{0}行为序列号管理物料，数量必须为1!*/);
						}
					}
				}else{
					throw new Exception(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0068",null,new String[]{
							crowno1
					})/*请确认第{0}行物料是否已分配库存组织！*/);
				}
				
				//生成流水号
				if(isLshMap.get(pk_configs)){
					if(!streamMap.containsKey(pk_group1)){
						StreamconfigVO[] configVOs = (StreamconfigVO[]) HYPubBO_Client
								.queryByCondition(StreamconfigVO.class, "dr=0 and pk_group='"
										+ pk_group1 + "'");
						if (configVOs != null && configVOs.length > 0) {
							streamMap.put(pk_group1, configVOs[0]);
						}else{
//							throw new Exception("第"+crowno+"行对应的集团未定义流水号，请先定义流水号！");
							throw new Exception(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0069",null,new String[]{
									crowno1
							})/*第{0}行对应的集团未定义流水号，请先定义流水号！*/);
						}
					}
				}
				//添加补打原因校验
				String lsh = printVO1.getLsh();
				String maxlsh = null;
				if(lsh != null && !"".equals(lsh)){
//					if(MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), "提示","第"+crowno+"行，流水号值已存在，是否覆盖？")==nc.ui.pub.beans.UIDialog.ID_NO){
//					if(MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0044")/*提示*/,
//							NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0070",null,new String[]{
//									crowno1
//							})/*第{0}行，流水号值已存在，是否覆盖？*/)==nc.ui.pub.beans.UIDialog.ID_YES){
//						ITmprintMaintain  itmprintmaintain =NCLocator.getInstance().lookup(ITmprintMaintain.class);
//						maxlsh=itmprintmaintain.getLsh(printVO1, streamMap.get(pk_group1));
//						billModel.setValueAt(maxlsh, Integer.valueOf(printVO1.getCrowno())-1, "lsh");
//						printVO1.setLsh(maxlsh);
//
//					}else{
						ITmprintMaintain  itmprintmaintain =NCLocator.getInstance().lookup(ITmprintMaintain.class);
						maxlsh=itmprintmaintain.getMaxLsh(printVO1, streamMap.get(pk_group1));
						//【业务参数设置-集团】中增加一个参数编码为【TM001】类型为【字符】的参数，用来写上成品的物料分类编码，如果是多个物料分类，用【,】隔开
//							String cpflcode = SysInitQuery.getParaString(AppContext.getInstance().getPkGroup(), "TM001");
						String cpflcode = SysInitQuery.getParaString(printVO1.getPk_org(), "TM001");
						if (cpflcode==null) {
							MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(), NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0044"), "未获取到【TM001】参数设置，请先配置参数！");
							return;
						}
						String[] cpflcodes = cpflcode.split(",");
						String vbatchcode = printVO1.getVbatchcode();
						String sqlwhere = "lsh='"+printVO1.getLsh()+"' and pk_material ='"+printVO1.getPk_material()+"' and dr=0  ";
						if (vbatchcode != null && !"".equals(vbatchcode)) {
							sqlwhere = "lsh='"+printVO1.getLsh()+"' and pk_material ='"+printVO1.getPk_material()+"' and dr=0  and vbatchcode ='"+vbatchcode+"'";
						}
						Object lsh2 =HYPubBO_Client.findColValue("qilibc_tmprintupdate", "lsh", sqlwhere);
						for(String matclasscode : cpflcodes){
							if(matclasscode.equals(getCpmap().get(pk_material1)) && (printVO1.getVdef15() == null || "".equals(printVO1.getVdef15())) && (null!=lsh2 &&  !lsh2.equals(""))){
								MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(), NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0044"), "*第【"+crowno1+"】行，请填写补打原因！");
								return;
							}
						}
//					}
				}
			}
			for(TmprintVO printVO:prints){
				printVO.setPrinter(AppContext.getInstance().getPkUser());
				billModel.setValueAt(AppContext.getInstance().getPkUser(), Integer.valueOf(printVO.getCrowno())-1, "printer");
		    	String pk_config = printVO.getPk_config();
		    	String pk_group = printVO.getPk_group();
			    //条码配置项
				AggBarcodeconfigVO configVO = null;
				if(configMap.containsKey(pk_config)){
					configVO = configMap.get(pk_config);
				}else{
					configVO = (AggBarcodeconfigVO)HYPubBO_Client.queryBillVOByPrimaryKey(new String[]{AggBarcodeconfigVO.class.getName(),BarcodeconfigVO.class.getName(),BarcodeconfigBVO.class.getName()}, pk_config);
				}
//				//生成流水号
				String flg = "";//1代表新流水,2代表旧流水
					flg =genStream(billModel, printVO,streamMap.get(pk_group));
				//生成条码
				genBarcode(billModel, printVO,configVO);
				//生成标签管理
				TmprintupdateVO  tmprintupdatevo= new TmprintupdateVO();
				String [] AttributeName=printVO.getAttributeNames();
				//只需要流水码的数据和特征码的数据
				if(!"".equals(printVO.getAttributeValue("lsh")) && printVO.getAttributeValue("lsh") != null){
					for(String clumname :AttributeName){
						tmprintupdatevo.setAttributeValue(clumname, printVO.getAttributeValue(clumname));
					}
					tmprintupdatevo.setDr(0);
					tmprintupdatevo.setTs(null);
					tmprintupdatevo.setPk_tmprint(null);
					printsVO.add(tmprintupdatevo);
				}
				
		    }
			//根据打印条码生成期初条码流水和包装单
//			if(printsVO!=null && printsVO.size()>0){
//				ITmprintMaintain save = NCLocator.getInstance().lookup(ITmprintMaintain.class);
//				save.savetmupdate(printsVO);
//			}
			 listener = new MyPrintProcessor(printsVO,billModel,selectrow);
		    super.doAction(arg0);
		} catch (Exception e) {
			showBarMsg(e.getMessage());
		} finally{
			//释放表
			PKLock.getInstance().releaseBatchLock(
					new String[]{"qilibc_tmprint"}, AppContext.getInstance().getPkUser(), "");
		}
	}
	private nc.ui.pub.print.PrintEntry printEntry;
	@Override
	protected PrintEntry getPrintEntry() {
//		if (null == this.printEntry) {
		this.printEntry = super.getPrintEntry();
		this.printEntry.setPrintListener(listener);
//		}
		return this.printEntry;
	}
	
	private Map<String,String> cpmap = null;
	
	private Map<String,String> getCpmap() throws BusinessException{
		if(cpmap==null){
			cpmap = new HashMap<String,String>();
			Map<String,String> wlflmap = new HashMap<String,String>();
			MaterialVO[] matvos = (MaterialVO[]) HYPubBO_Client.queryAll(MaterialVO.class);
			MarBasClassVO[] marclassvos = (MarBasClassVO[]) HYPubBO_Client.queryAll(MarBasClassVO.class);
			
			if(marclassvos!=null&&marclassvos.length>0){
				for(MarBasClassVO marclassvo : marclassvos){
					wlflmap.put(marclassvo.getPk_marbasclass(), marclassvo.getCode());
				}
			}
			if(matvos!=null&&matvos.length>0&&!wlflmap.isEmpty()){
				for(MaterialVO matvo : matvos){
					cpmap.put(matvo.getPk_material(), wlflmap.get(matvo.getPk_marbasclass()));
				}
			}else{
				cpmap=null;
			}
		}
		return cpmap;
	}
//	@Override
//	public String getNodekey() {
//		BillCardPanel cardPanel = getEditor().getBillCardPanel();
//		BillModel billModel = cardPanel.getBillModel();		
//		int rowCount = cardPanel.getRowCount();
//		//判断表体选中行必输项  end--------------------------------------
//		List<Integer> selectrow=new ArrayList<Integer>();
//		for(int i = 0; i < rowCount; i++){
//			if(billModel.getRowState(i)==4){
//				selectrow.add(i);
//			}
//		}
//		//打印后更新条码配置标识
//		Object wlflcode = billModel.getValueObjectAt(selectrow.get(0), "pk_material.pk_marbasclass.code");
//		if(null!=wlflcode && !"".equals(wlflcode) && wlflcode.toString().startsWith("05")){
//			return "yl";
//		}else if(null!=wlflcode && !"".equals(wlflcode) && wlflcode.toString().startsWith("06")){
//			return "cp";
//		}else{
//			return "";
//		}
//
//		
//	}
	
		
	
	/**
	 * 生成流水号
	 * 
	 */
	private String genStream(BillModel billModel,TmprintVO printVO,StreamconfigVO streamVO) throws 
			BusinessException {
		//检查行是否已有流水号
		String lsh = printVO.getLsh();
		String crowno = printVO.getCrowno();
		String vbatchcode = printVO.getVbatchcode();
		if(lsh != null && !"".equals(lsh)){
			String sqlwhere = "lsh='"+printVO.getLsh()+"' and pk_material ='"+printVO.getPk_material()+"' and dr=0  ";
			if (vbatchcode != null && !"".equals(vbatchcode)) {
				sqlwhere = "lsh='"+printVO.getLsh()+"' and pk_material ='"+printVO.getPk_material()+"' and dr=0  and vbatchcode ='"+vbatchcode+"'";
			}
			Object lsh2 =HYPubBO_Client.findColValue("qilibc_tmprintupdate", "lsh", sqlwhere);
				if(null!=lsh2 &&  !lsh2.equals("")){
					Object oversion =HYPubBO_Client.findColValue("qilibc_tmprintupdate", "max(version)", sqlwhere);
					String lshflg = "2";
					int  version= Integer.parseInt(printVO.getVersion())+1;
					if (null != oversion && !"".equals(oversion)) {
						version= Integer.parseInt(oversion+"")+1;
					}
					billModel.setValueAt(String.format("%04d", version), Integer.valueOf(printVO.getCrowno())-1, "version");
					printVO.setVersion(String.format("%04d", version));
					return lshflg;
				}else{
					billModel.setValueAt(String.format("%04d", 1), Integer.valueOf(printVO.getCrowno())-1, "version");
					printVO.setVersion(String.format("%04d", 1));
					return "1";
				}
//			}
		}
		//条码流水组成年月日部分
		String nyr = "";
		String sqlwhere = " 1=1 ";
		String streamValue = streamVO.getValue(); 
		String[] fields = streamValue.split(";");
		for (String field : fields) {
			String date = printVO.getPrinttime().toStdString();
			if("year".equals(field)){
				nyr = date.substring(0, 4);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 4)+"%'";
			}else if("month".equals(field)){
				nyr = date.substring(0, 4) + date.toString().substring(5, 7);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 7)+"%'";
			}else if("day".equals(field)){
				nyr = date.substring(0, 4) + date.toString().substring(5, 7) + date.toString().substring(8, 10);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 10)+"%'";
			}else if("producedate".equals(field) && printVO.getDproducedate()!=null){
				sqlwhere = sqlwhere +" and dproducedate like '"+printVO.getDproducedate().toStdString().substring(0, 10)+"%'";
			}else if("batchcode".equals(field) && printVO.getVbatchcode()!=null){
				sqlwhere = sqlwhere +" and vbatchcode like '"+printVO.getVbatchcode()+"%'";
			}else{
				Object value = printVO.getAttributeValue(field);
				if(value == null || "".equals(value)){
					//sqlwhere = sqlwhere +" and isnull("+field+",'~')='~'";
				}else{
					sqlwhere = sqlwhere +" and "+field+"='"+printVO.getAttributeValue(field)+"'";
				}
			}
		}
		
		//add by cp 存放界面上的最大流水号
		TmprintVO[] printVOs = (TmprintVO[]) billModel.getBodyValueVOs(TmprintVO.class.getName());
		Integer jmMaxls = 0;
		if(printVOs !=null && printVOs.length>0){
			for (int i = 0; i < printVOs.length; i++) {
				TmprintVO tmprintVO = printVOs[i];
				
				if(tmprintVO.getLsh() == null || "".equals(tmprintVO.getLsh())){
					continue;
				}
				boolean isxt = true;
				for (String field : fields) {
					String date = printVO.getPrinttime().toStdString();
					if("year".equals(field)){
						if(tmprintVO.getPrinttime()!=null && tmprintVO.getPrinttime().toString().startsWith(date.substring(0, 4))){
							
						}else{
							isxt = false;
							break;
						}
					}else if("month".equals(field)){
						if(tmprintVO.getPrinttime()!=null && tmprintVO.getPrinttime().toString().startsWith(date.substring(0, 7))){
							
						}else{
							isxt = false;
							break;
						}
					}else if("day".equals(field)){
						if(tmprintVO.getPrinttime()!=null && tmprintVO.getPrinttime().toString().startsWith(date.substring(0, 10))){
							
						}else{
							isxt = false;
							break;
						}
					}else if("producedate".equals(field) && printVO.getDproducedate()!=null){
						if(tmprintVO.getDproducedate()!=null && tmprintVO.getDproducedate().toString().startsWith(printVO.getDproducedate().toStdString().substring(0, 10))){
							
						}else{
							isxt = false;
							break;
						}
					}else if("batchcode".equals(field) && printVO.getVbatchcode()!=null){
						if(tmprintVO.getVbatchcode()!=null && tmprintVO.getVbatchcode().equals(printVO.getVbatchcode())){
							
						}else{
							isxt = false;
							break;
						}
					}else{
						Object value = printVO.getAttributeValue(field);
						if(value == null || "".equals(value)){
							//sqlwhere = sqlwhere +" and isnull("+field+",'~')='~'";
						}else{
							if(tmprintVO.getAttributeValue(field)!=null && tmprintVO.getAttributeValue(field).equals(printVO.getAttributeValue(field))){
								
							}else{
								isxt = false;
								break;
							}
						}
					}
				}
				
				if(isxt){
					Integer lsh1 = Integer.valueOf(tmprintVO.getLsh());
					if(lsh1>jmMaxls){
						jmMaxls = lsh1;
					}
				}
			}
		}
		
//	    if(printVO.getVfree1()!=null){
//			sqlwhere = sqlwhere +" and vfree1 = '"+printVO.getVfree1()+"'";
//		}
		//流水长度
		Integer strlen = streamVO.getStrlenth();
		//流水号最大值9999
		String max99 = "";
		for(int j = 0; j < strlen; j++){
			max99 = "9" + max99;
		}

		String maxstr = null;
		//过滤掉流水号为空的数据
		sqlwhere +=" and isnull(lsh,'~')!='~' ";
		TmprintVO[] qryVos = (TmprintVO[]) HYPubBO_Client.queryByCondition(TmprintVO.class, sqlwhere);
		if(qryVos != null && qryVos.length>0){
			Integer maxlsh = 0;
			TmprintVO maxtmprintVO = null;
			for (TmprintVO tmprintVO : qryVos) {
				Integer lsh1 = Integer.valueOf(tmprintVO.getLsh());
				if(lsh1>maxlsh){
					maxlsh = lsh1;
					maxtmprintVO = tmprintVO;
				}
			}
			if(maxlsh<jmMaxls){
				maxlsh = jmMaxls;
			}
			if(max99.equals(maxlsh.toString())){
//				throw new BusinessException("流水号已达最大值"+max99+"，请在“流水号规则”配置界面修改流水号位数后重新打印！");
				throw new BusinessException(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0071",null,new String[]{
						max99+""
				})/*流水号已达最大值{0}，请在“流水号规则”配置界面修改流水号位数后重新打印！*/);
			}
			maxlsh=maxlsh+1;
			maxstr = Integer.toString(maxlsh);
			if(maxtmprintVO!=null){
				maxtmprintVO.setLsh(maxstr);
//				HYPubBO_Client.update(maxtmprintVO);
			}
			lsh = nyr+maxstr;
			billModel.setValueAt(lsh, Integer.valueOf(printVO.getCrowno())-1, "lsh");
			printVO.setLsh(lsh);
		}else{
			if(jmMaxls>0){
				maxstr=(jmMaxls+1)+"";
			}else{
				maxstr = "1";
			}
//			maxstr = "1";
			printVO.setLsh(maxstr);
//			HYPubBO_Client.insert(printVO);
			lsh = nyr+maxstr;
			billModel.setValueAt(lsh, Integer.valueOf(printVO.getCrowno())-1, "lsh");
			printVO.setLsh(lsh);
		}
		billModel.setValueAt("0001", Integer.valueOf(printVO.getCrowno())-1, "version");//版本默认为1.0
		printVO.setVersion("0001");
		return "1";
	}

	/**
	 * 生成条码
	 * @param billModel
	 * @param rowCount
	 * @param marker 分隔符
	 * @throws BusinessException
	 * @throws UifException
	 */
	private void genBarcode(BillModel billModel, TmprintVO printVO,AggBarcodeconfigVO configVO)
			throws BusinessException {
		String crowno = printVO.getCrowno();
		int rowno = Integer.valueOf(crowno)-1;
		String marker = configVO.getParentVO().getMarker();
		String configcode = configVO.getParentVO().getConfigcode();
		BarcodeconfigBVO[] configBVOs = (BarcodeconfigBVO[]) configVO.getChildrenVO();
		String[] fields = new String[configBVOs.length];
		for(BarcodeconfigBVO bvo:configBVOs){
			fields[bvo.getSerial()-1] = bvo.getMdcode();
		}
		Object object = "";
		String value = "";
		StringBuffer barcode = new StringBuffer();
		for (String field : fields) {
			object = DASFacade.getAttributeValue(NCObject.newInstance(printVO),field);
			value = object == null?"":object+"";
			if(object instanceof UFDouble){
				DecimalFormat decimalFormat = new DecimalFormat("#.########");// 格式化设置
				value = decimalFormat.format(((UFDouble)object).doubleValue());
			}
			barcode.append(value);
			if(barcode.length() > 0){
				barcode.append(marker);
			}
		}
		barcode.append("[@]"+ configcode);
		billModel.setValueAt(barcode.toString(), rowno, "barcode");
		printVO.setBarcode(barcode.toString());
	}
	
	protected boolean isActionEnable(){
       return true;
    }

	@Override
	protected LoginContext getContext() {
		return getModel().getContext();
	}
	public BatchBillTableModel getModel()
    {
        return model;
    }

    public void setModel(BatchBillTableModel model)
    {
        this.model = model;
        this.model.addAppEventListener(this);
    }
	private IUAPQueryBS query = null;
	private IUAPQueryBS getQry() {
		if(query == null)
			query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		return query;
	}
	/**
	 * 自由项校验
	 * @param prints
	 * @return
	 * @throws Exception
	 */
	private void chekVfree(List<TmprintVO> prints,Map<String, InvBasVO> invBasVOs) throws Exception{
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		UserdefitemVO[] userdefitemVOs = MarAssistantCheckUtils.getFixedAssistUserDefitem(pk_group);
		
		List<String> errVendorRows = new ArrayList<String>();// 供应商
		List<String> errProductRows = new ArrayList<String>();// 生产厂商
		List<String> errProjectRows = new ArrayList<String>();// 项目
		List<String> errStateRows = new ArrayList<String>();// 库存状态
		List<String> errCustRows = new ArrayList<String>();// 客户
		List<String> errCffileRows = new ArrayList<String>();// 特征码

		for(int i = 0; i < prints.size(); i ++){
			TmprintVO vo = prints.get(i);
			String prefix = "vfree";
			  MarAssistantCheckUtils.check(new TmprintVO[]{vo}, prefix, userdefitemVOs);
			  // 物料字段有值时，进行物料对应辅助属性的非空校验
			  new MarAsstValidationService().validate("pk_material", prefix,
							new TmprintVO[]{vo}, userdefitemVOs);
			  String cmaterialvid = vo.getPk_material();
				if (this.isVendorChosen(cmaterialvid, invBasVOs)
						&& StringUtil.isSEmptyOrNull(vo.getCvendorid())) {
					errVendorRows.add(vo.getCrowno());
				}
				if (this.isProductorChosen(cmaterialvid, invBasVOs)
						&& StringUtil.isSEmptyOrNull(vo.getCproductorid())) {
					errProductRows.add(vo.getCrowno());
				}
				if (this.isProjectChosen(cmaterialvid, invBasVOs)
						&& StringUtil.isSEmptyOrNull(vo.getCprojectid())) {
					errProjectRows.add(vo.getCrowno());
				}
				if (this.isCustomerChosen(cmaterialvid, invBasVOs)
						&& StringUtil.isSEmptyOrNull(vo.getCasscustid())) {
					errCustRows.add(vo.getCrowno());
				}
				if (this.isInvStateManage(cmaterialvid, invBasVOs)
						&& StringUtil.isSEmptyOrNull(vo.getCstateid())) {
					errStateRows.add(vo.getCrowno());
				}
				if (this.isCffileChosen(cmaterialvid, invBasVOs)
						&& StringUtil.isSEmptyOrNull(vo.getCffileid())) {
					errCffileRows.add(vo.getCrowno());
				}
		}
		
		showErrByRows(errVendorRows, 
				RuleRes.getVendorNullErr());
		showErrByRows(errProductRows, 
				RuleRes.getProductorNullErr());
		showErrByRows(errProjectRows, 
				RuleRes.getProjectNullErr());
		showErrByRows(errStateRows, 
				RuleRes.getStateNullErr());
		showErrByRows(errCustRows, 
				RuleRes.getCustNullErr());
		showErrByRows(errCffileRows, 
				RuleRes.getCffileNullErr());
	}
	
	  /**
	   * 方法功能描述：
	   * <p>
	   * 根据行号进行出错提示 <b>参数说明</b>
	   * 增加来源备料计划表体行ID列表
	   * 
	   * @param crows
	   * @param srcBids
	   * @param errMsg
	   * @throws BusinessException <p>
	   */
	  public void showErrByRows(List<String> crows,
	      String errMsg) throws BusinessException {
	    if (crows == null || crows.size() == 0) {
	      return;
	    }
	    StringBuilder errs = new StringBuilder();
	    errs.append(errMsg).append("\n");
	    for (String row : crows) {
	      errs.append("行号").append(": ").append(row).append("\n");
	    }
	    throw new BusinessException(errs.toString());
	  }
	
	/**
	 * 
	 * 方法功能描述：
	 * <p>
	 * 物料是否勾选客户辅助属性 <b>参数说明</b>
	 * 
	 * @param cmateiralvid
	 * @param invMap
	 * @return <p>
	 * @since 6.0
	 * @author chennn
	 * @time 2011-12-8 上午10:58:09
	 */
	private boolean isCustomerChosen(String cmateiralvid,
			Map<String, InvBasVO> invMap) {
		UFBoolean fix5 = invMap.get(cmateiralvid).getFix5();
		return fix5 == null ? false : fix5.booleanValue();
	}

	/***
	 * 方法功能描述：
	 * <p>
	 * 物料是否库存状态管理 <b>参数说明</b>
	 * 
	 * @param cmaterialvid
	 */
	private boolean isInvStateManage(String cmaterialvid,
			Map<String, InvBasVO> invMap) {
		UFBoolean fix1 = invMap.get(cmaterialvid).getFix1();
		return fix1 == null ? false : fix1.booleanValue();

	}

	/**
	 * 方法功能描述：
	 * <p>
	 * 物料是否勾选生产厂商辅助属性 <b>参数说明</b>
	 * 
	 * @param cmaterialvid
	 * @return <p>
	 * @since 6.0
	 * @author chennn
	 * @time 2010-6-1 下午02:24:11
	 */
	private boolean isProductorChosen(String cmaterialvid,
			Map<String, InvBasVO> invMap) {
		UFBoolean fix4 = invMap.get(cmaterialvid).getFix4();
		return fix4 == null ? false : fix4.booleanValue();
	}

	/**
	 * 方法功能描述：
	 * <p>
	 * 物料是否勾选项目辅助属性 <b>参数说明</b>
	 * 
	 * @param cmaterialvid
	 */
	private boolean isProjectChosen(String cmaterialvid,
			Map<String, InvBasVO> invMap) {
		UFBoolean fix2 = invMap.get(cmaterialvid).getFix2();
		return fix2 == null ? false : fix2.booleanValue();
	}

	/**
	 * 方法功能描述：
	 * <p>
	 * 物料是否勾选供应商辅助属性 <b>参数说明</b>
	 * 
	 * @param cmaterialvid
	 */
	private boolean isVendorChosen(String cmaterialvid,
			Map<String, InvBasVO> invMap) {
		UFBoolean fix3 = invMap.get(cmaterialvid).getFix3();
		return fix3 == null ? false : fix3.booleanValue();
	}
	
	/*
	 * 物料是否勾选特征码辅助属性
	 */
	private boolean isCffileChosen(String cmaterialvid,
			Map<String, InvBasVO> invMap) {
		UFBoolean fix100 = invMap.get(cmaterialvid).getFix100();
		return fix100 == null ? false : fix100.booleanValue();
	}

	/**
	 * 显示提示信息
	 * @param msg
	 */
	private void showBarMsg(String msg){
		ShowStatusBarMsgUtil.showErrorMsgWithClear(NCLangRes.getInstance().getStrByID("common", "40H1-MESSAGE0044")/*提示*/, msg,getModel().getContext());
	}
}
