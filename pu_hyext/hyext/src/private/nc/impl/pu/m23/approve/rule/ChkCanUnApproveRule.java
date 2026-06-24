package nc.impl.pu.m23.approve.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.pu.reference.ic.M45PUServices;
import nc.itf.pu.reference.ic.M47PUServices;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.pub.util.ApproveFlowUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.qc.c001.entity.ApplyVO;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.scmpub.res.billtype.IBillType;
import nc.vo.scmpub.res.billtype.POBillType;
import nc.vo.scmpub.res.billtype.SCBillType;

/**
 * 
 * @description <ul>
 *              <li>检查是否可弃审
 *              <li>检查是否存在生成过补货订单的到货单
 *              <li>生成下游单据的不能弃审(包括已生成资产卡片)
 *              <li>已报检的不能弃审
 *              </ul>
 * @scene
 * 到货单取消审批
 * @param 无
 * 
 * @since 6.3
 * @version 2010-1-19 上午09:34:01
 * @author hanbin
 */

public class ChkCanUnApproveRule implements IRule<ArriveVO> {

	@Override
	public void process(ArriveVO[] voArray) {
		for (ArriveVO aggVO : voArray) {
			// 检查是否可弃审
			this.chkCanUnApprove(aggVO);
		}
		// 检查是否生成过采购入
		this.checkHasPurchaseIn(voArray);
		// 检查是否生成过委外入
		this.checkHasSubcontIn(voArray);
	}

	private void checkHasPurchaseIn(ArriveVO[] vos) {
		List<String> hidLst = this.getHIDSourceFrom(vos, POBillType.Order);
		if (hidLst.size() == 0) {
			return;
		}
		Map<String, UFBoolean> hidMap = M45PUServices.getMapBysrcHid(hidLst
				.toArray(new String[hidLst.size()]));
		if (null == hidMap || hidMap.size() == 0) {
			return;
		}
		for (UFBoolean value : hidMap.values()) {
			if (UFBoolean.TRUE.equals(value)) {
				this.throwHasStoreInExp();
			}
		}
	}

	private void checkHasSubcontIn(ArriveVO[] vos) {
		List<String> hidLst = this.getHIDSourceFrom(vos, SCBillType.Order);
		if (hidLst.size() == 0) {
			return;
		}
		if (M47PUServices.hasFromSource(hidLst)) {
			this.throwHasStoreInExp();
		}
	}

	/**
	 * 方法功能描述：检查是否可弃审
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param aggVO
	 *            <p>
	 * @since 6.0
	 * @author hanbin
	 * @time 2010-1-19 上午10:22:02
	 */
	private void chkCanUnApprove(ArriveVO aggVO) {
		if (!ApproveFlowUtil.isCanUnApprove(aggVO)) {
			String message = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("4004040_0", "04004040-0122")/*
															 * @res
															 * "到货单的当前状态不允许取消审核！"
															 */;
			ExceptionUtils.wrappBusinessException(message);
		}

		
		ArriveItemVO[] itemVOArray = aggVO.getBVO();
		for (int i = 0; i < itemVOArray.length; i++) {
			// 生成下游单据的不能弃审(包括已生成资产卡片)
			if (MathTool.nvl(itemVOArray[i].getNaccumstorenum()).doubleValue() > 0) {
				this.throwHasStoreInExp();
			}
			if (itemVOArray[i].getBfaflag().booleanValue()) {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("4004040_0",
										"04004040-0124")/*
														 * @res "已生成设备卡片，不允许弃审！"
														 */);
			}
			// 检查是否存在生成过补货订单的到货单
			if (MathTool.nvl(itemVOArray[i].getNaccumreplnum()).doubleValue() > 0) {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("4004040_0",
										"04004040-0125")/*
														 * @res "已生成补货单，不允许弃审！"
														 */);
			}
			
      //bg-NCM-zhangkjb-NC2015051100102-2015-05-15-通版
      /*
       * add by wandl 合维护开发部补丁
       * 参照原到货单生成退货单，会把累计校验数量带过来，
       * 需求建议可以在退货单不校验累计保健数量。
       */
			 
		      
			try {
				canclebj(aggVO);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ExceptionUtils
				.wrappBusinessException(e.getMessage());
			}
//      if(aggVO.getHVO().getBisback() == null ||!aggVO.getHVO().getBisback().booleanValue()){
//				// 已报检的不能弃审
//				if (MathTool.nvl(itemVOArray[i].getNaccumchecknum())
//						.doubleValue() > 0) {
//					ExceptionUtils
//							.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
//									.getNCLangRes().getStrByID("4004040_0",
//											"04004040-0126")/*
//															 * @res
//															 * "已报检的到货单，不允许弃审！"
//															 */);
//				}
//      }
      //ed-NCM-zhangkjb-NC2015051100102-2015-05-15-通版
			
			// 已紧急放行的到货单
			if (MathTool.nvl(itemVOArray[i].getNaccumletgonum()).doubleValue() > 0) {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("4004040_0",
										"04004040-0127")/*
														 * @res
														 * "已紧急放行的到货单，不允许弃审！"
														 */);
			}
			// 生成转固单
			if (UFBoolean.TRUE.equals(itemVOArray[i].getBtransasset())) {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("4004040_0",
										"04004040-0128")/*
														 * @res "已生成转固单，不允许弃审！"
														 */);
			}
			// 全部退货
			if (itemVOArray[i].getNaccumbacknum() != null
					&& !UFDouble.ZERO_DBL.equals(itemVOArray[i]
							.getNaccumbacknum())) {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("4004040_0",
										"04004040-0192")/*
														 * @res
														 * "已基于该到货单生成退货单，不允许弃审！"
														 */);
			}
		}
	}
	
	public  void canclebj(ArriveVO vos) throws BusinessException{
		 
	    	  String sql=
	        		  "select b.csourceid,b.pk_applybill" + "\n" +
	        				  "        from qc_applybill_s b" + "\n" + 
	        				  "       where b.pk_applybill in (select a.pk_applybill" + "\n" + 
	        				  "                                  from qc_applybill_s a" + "\n" + 
	        				  "                                 where a.csourceid = '"+vos.getHVO().getPk_arriveorder()+"'" + "\n" + 
	        				  "                                   and a.dr = 0)" + "\n" + 
	        				  "         and b.dr = 0" + "\n" + 
	        				  "       group by b.csourceid,b.pk_applybill";
	      	IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
	    			IUAPQueryBS.class.getName());
	      	 List<Map<String,String>> list = (List) qryBS.executeQuery(sql, null,
	    				new MapListProcessor());
	      	 if(list==null||list.size()==0){
	      		 return;
	      	 }else{
	      		 if(list.size()>1){
	      			throw new BusinessException("报检单含有其他到货单，不能反审,请手工删除");
	      		 }	
	      		 String pk_applybill=list.get(0).get("pk_applybill").toString();
	      		 BillQuery<ApplyVO> billquery = new BillQuery(ApplyVO.class);
	      		 ApplyVO[] aggvo=billquery.query(new String[] {pk_applybill});
	      		 
//	      		 检查是否含有质检报告
	      		 String sql2=
	      				"select  a.pk_reportbill" + "\n" +
	      						"from  qc_reportbill a,qc_reportbill_b b" + "\n" + 
	      						"where a.pk_reportbill =b.pk_reportbill" + "\n" + 
	      						"and b.csourceid='"+pk_applybill+"'" + "\n" + 
	      						"and a.dr=0";
	      		 
	      	 
	          	 List<Map<String,String>> list2 = (List) qryBS.executeQuery(sql2, null,
	        				new MapListProcessor());
	          	IPFBusiAction service = NCLocator.getInstance().lookup(IPFBusiAction.class);
	          	 if(list2!=null&&list2.size()>0){
	          		 String pk=list2.get(0).get("pk_reportbill").toString();
	          		 BillQuery<ReportVO> billquery2 = new BillQuery(ReportVO.class);
	          		 ReportVO[] revo=billquery2.query(new String[] {pk});
	          		 for(int j=0;j<revo.length;j++){
	          			ReportVO vo=revo[j];
	          			ReportHeaderVO  hvo=vo.getHVO();
	          			if( hvo.getModifiedtime()!=null&&hvo.getModifiedtime().compareTo(hvo.getCreationtime())>0){
	          				throw new BusinessException("质检报告已修改，不允许删除");
	          			}
	          			 service.processBatch("DISCARD", "C003",revo, null, null, null);		
	          		 }
	          	 }	
				//保存
	          	 BillQuery<ApplyVO> billquery2 = new BillQuery(ApplyVO.class);
	          	ApplyVO[] aggvo3=billquery2.query(new String[] {pk_applybill});
	          	
//	          	aggvo3[0].getHVO().setTs(new UFDateTime(System.currentTimeMillis()));
//	          	aggvo3[0].getB1VO()[0].setTs(new UFDateTime(System.currentTimeMillis()));
	          	
	      		ApplyVO[] aggvo21 = (ApplyVO[]) service.processBatch("UNAPPROVE", "C001",aggvo3, null, null, null);	
	      		 BillQuery<ApplyVO> billquery3 = new BillQuery(ApplyVO.class);
	      		ApplyVO[] aggvo2=billquery3.query(new String[] {pk_applybill});
	      		ApplyVO[] aggvo22 = (ApplyVO[]) service.processBatch("DISCARD", "C001",aggvo21, null, null, null);	
	      	 }
	         
	      
	      
	}

	private List<String> getHIDSourceFrom(ArriveVO[] vos, IBillType bt) {
		List<String> hidLst = new ArrayList<String>();
		for (ArriveVO vo : vos) {
			for (ArriveItemVO item : vo.getBVO()) {
				if (bt.getCode().equals(item.getCsourcetypecode())) {
					hidLst.add(item.getPk_arriveorder());
					break;
				}
			}
		}
		return hidLst;
	}

	private void throwHasStoreInExp() {
		ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
				.getNCLangRes().getStrByID("4004040_0", "04004040-0123"));/*
																		 * @res
																		 * "已生成入库单，不允许弃审！"
																		 */
	}

}
