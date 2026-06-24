package nc.ui.to.m5x.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.mysql.fabric.xmlrpc.Client;

import nc.bs.framework.common.NCLocator;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoQueryService;
import nc.itf.fi.pub.SysInit;
import nc.itf.to.m5x.IFindPricePubService;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.to.m5x.maintain.view.TransOrderEditor;
import nc.ui.to.m5x.pub.NumPriceMnyUtil;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.cmp.util.StringUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;

@SuppressWarnings("restriction")
public class InquaryAction extends NCAction{
	
	private static final long serialVersionUID = 1466378756729891216L;
	
	private TransOrderEditor editor;
	private AbstractAppModel model;
	private IFindPricePubService sev;
	
	public InquaryAction() {
		this.setBtnName("自动取价");
		this.setCode("inquaryAction");
	}
	
	private IFindPricePubService getSeverse(){
		if(null == sev){
			sev = NCLocator.getInstance().lookup(IFindPricePubService.class);
			
		}
		return sev;
	}

	/**
	 * pk_org 单据所属公司
	 * cinventoryvid 物料ID
	 * vbnote 备注，
	 * 考虑到采购订单目前从SRM进来的物料是分子公司的物料而非本部的物料，到货时转换成本部物料，
	 * 为避免由于物料ID变更导致的取价失败问题，
	 * 如果是产品，销售调拨，那么需要确认订单价
	 */
	@Override
	public void doAction(ActionEvent e) throws Exception {
		BillCardPanel cardPanel = this.getEditor().getBillCardPanel();
		// 依据组织物料获取价格
		// 存储维度
		Map<Integer , Map<String,String>> map_1 = new HashMap<Integer, Map<String,String>>();//1	销售价  1001V1100000001GY4EA
		Map<Integer , Map<String,String>> map_2 = new HashMap<Integer, Map<String,String>>();//2	最新价  1001V1100000001GY4EC
		Map<Integer , Map<String,String>> map_3 = new HashMap<Integer, Map<String,String>>();//3	参考价  1001V1100000001GY4ED
		Map<Integer, String> priceSourceMap = new HashMap<Integer, String>();
		
		// 组织
		String pk_org = cardPanel.getHeadItem("pk_org").getValue();//组织
		String cinstockorgid  = cardPanel.getHeadItem("cinstockorgid").getValue();//调入组织
		String csrcmodulecode = cardPanel.getHeadItem("csrcmodulecode").getValue();//来源模块
		
		UFBoolean paraBoolean = SysInit.getParaBoolean(pk_org, "TO26");
	
		int size = cardPanel.getRowCount();
		for(int index = 0; index < size; index++){	
			Map<String,String> row_date = new HashMap<String,String>();
			row_date.put("pk_org",pk_org);
			String pk_material = (String) cardPanel.getBodyValueAt(index, "cinventoryvid"); //物料
			if(StringUtils.isEmpty(pk_material)){//物料号为空时，直接忽略
				continue;
			}
			row_date.put("pk_material", pk_material);
			MaterialVO[] queryDataByPks = this.getMaterialQuery().queryDataByPks(new String[]{pk_material}); //物料获取 带主键查的，必有值，不再判空
			String mdmcode = queryDataByPks[0].getDef7();//主数据编码
			row_date.put("mdmcode", mdmcode);
	
			String pk_project = (String) cardPanel.getBodyValueAt(index, "cprojectid");//项目
			row_date.put("cprojectid", pk_project);
			
//			String cinstockorgid  = (String) cardPanel.getBodyValueAt(index, "cinstockorgid");//项目
			row_date.put("cinstockorgid", cinstockorgid);
			
			String csrcbid = (String) cardPanel.getBodyValueAt(index, "csrcbid");//来源单据明细
			row_date.put("csrcbid" , csrcbid);
			
			String csrcid  = (String) cardPanel.getBodyValueAt(index, "csrcid");//来源单据ID
			row_date.put("csrcid" , csrcbid);
			
			//获取物料分类信息，
			//获取物料分类的取从模式
			Map ids  =  getPk_marbasclass(pk_material);
			row_date.putAll(ids);
			String marbas_def3 = (String) ids.get("def3");//取价选项
			if(null == marbas_def3  ){  //如果没有选择取从方式，按原逻辑取最新结存从，或者取订单从，订单价一般不会再取了，应该是异常数据
				if( null == csrcmodulecode){//自制单单据
					map_2.put(index, row_date); //最新价
					priceSourceMap.put(index, "-取最新价");
				}else if("SO".equals(csrcmodulecode)){ //销售端来的
					map_1.put(index, row_date); //取销售价
					priceSourceMap.put(index, "-取销售价");
				}
			} else if(marbas_def3.equals("1001V1100000001GY4EA")){
				map_1.put(index, row_date); //取销售价
				priceSourceMap.put(index, "-取销售价");
			}else if(marbas_def3.equals("1001V1100000001GY4EC")){
				map_2.put(index, row_date);//最新价
				priceSourceMap.put(index, "-取最新价");
			}else if(marbas_def3.equals("1001V1100000001GY4ED") && paraBoolean.booleanValue() ){ //组织参数必须开启
				map_3.put(index, row_date);//参考价
				priceSourceMap.put(index, "-取参考价");
			}
			
//			map_3.put(index, row_date);
		}
		
		
		
		Map<Integer, UFDouble> res = new HashMap<Integer,UFDouble>() ;
//		StringBuffer masg = new StringBuffer();
		
		
		
		
		if("SO".equals(csrcmodulecode)){ //SO 下只考虑参考成本 和 销售订单取从
			res.putAll(getSeverse().doFindPrice_so(map_1)); //取销售单价格
			if(null != res && res.size() > 0){
				String paraString = SysInit.getParaString(pk_org, "TO24"); //折扣率
				if (StringUtils.isEmpty(paraString)){
					ExceptionUtils.wrappBusinessException("内部交易折扣参数未设置，请检查组织级参数【TO24】设置。");
				}
				UFDouble discount = new UFDouble(paraString); //折扣率
				for(Integer key :  res.keySet()){
					res.put(key, res.get(key).multiply(discount.div(100)));
				}

				
			}
			res.putAll(getSeverse().doFindPrice_ck(map_3));
			
		}else{
			//其它单据只考虑最新结存和参考价
			res.putAll(getSeverse().doFindPrice(map_2));
			res.putAll(getSeverse().doFindPrice_ck(map_3));
		}
		
		
			
		if(res != null && res.size() > 0){
			for(Integer key : res.keySet()){
				cardPanel.setBodyValueAt(res.get(key), Integer.valueOf(key), "nqtorignetprice");  // 单价赋值
				String sourceLabel = priceSourceMap.get(key);
				if (sourceLabel != null) {
					String existNote = (String) cardPanel.getBodyValueAt(key, "vbnote");
					String newNote = (existNote == null ? "" : existNote) + sourceLabel;
					cardPanel.setBodyValueAt(newNote, key, "vbnote");
				}
				UFDouble value = res.get(key);
				CardPanelValueUtils util = new CardPanelValueUtils(cardPanel);
				if (value != null && MathTool.compareTo(value, UFDouble.ZERO_DBL) <= 0) {
					String message = NCLangRes.getInstance().getStrByID("4009011_0", "04009011-0371")/*"其值不能小于0"*/;
				    util.setBodyValue(null, key, "nqtorignetprice");
				    ExceptionUtils.wrappBusinessException(message);
				}
				// 1、 调用“数量单价金额公共算法”
			    NumPriceMnyUtil editUtil = new NumPriceMnyUtil(cardPanel);
			    editUtil.calculateNumPriceMny(Integer.valueOf(key), "nqtorignetprice");
			}
		}else{
			ShowStatusBarMsgUtil.showStatusBarMsg("未从价格库中获取到任何价格信息！\n如果调拨的物料人需要销售的产品，请通过发货安排节点下的补货自动生成调拨单以关联销售单价。",this.getModel().getContext());
			return;
		}
		ShowStatusBarMsgUtil.showStatusBarMsg("取价完成", this.getModel().getContext());
	}

//	private String getMarbasclass_def3(String pk_marbasclass) {
//		
//		return null;
//	}

	private Map getPk_marbasclass(String pk_material) throws BusinessException {
		Map queryDbByCond = getSeverse().queryDbByCond(pk_material);
		return queryDbByCond;
	}

	public TransOrderEditor getEditor() {
		return editor;
	}

	public void setEditor(TransOrderEditor editor) {
		this.editor = editor;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		this.model.addAppEventListener(this);
	}
	
	private IMaterialBaseInfoQueryService query;
	private IMaterialBaseInfoQueryService getMaterialQuery(){
		if(null  == query ){
			query = NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
		}
		return query;
	}
	
	

}
