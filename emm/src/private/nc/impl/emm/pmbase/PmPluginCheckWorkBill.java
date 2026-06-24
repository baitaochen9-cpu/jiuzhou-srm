package nc.impl.emm.pmbase;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import nc.pub.jz.util.RaybowIcMassageUtil;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.ewm.workorder.WorkOrderHeadVO;
import nc.bs.dao.BaseDAO;
import nc.cmp.bill.util.SQLUtil;
import nc.impl.am.db.QueryUtil;
import nc.impl.obm.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;

public class PmPluginCheckWorkBill implements Runnable {

	public PMBillVO[] pmbillvos;
	public String userid;
	public List<PMBillVO> queryBillOfVOByCond;

	public List<PMBillVO> getQueryBillOfVOByCond() {
		return queryBillOfVOByCond;
	}

	public void setQueryBillOfVOByCond(List<PMBillVO> queryBillOfVOByCond) {
		this.queryBillOfVOByCond = queryBillOfVOByCond;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public PMBillVO[] getPmbillvos() {
		return pmbillvos;
	}

	public void setPmbillvos(PMBillVO[] pmbillvos) {
		this.pmbillvos = pmbillvos;
	}

	public PmPluginCheckWorkBill(PMBillVO[] pms, String string) {
		this.setPmbillvos(pms);
		this.setUserid(string);
	}
	
	public PmPluginCheckWorkBill(PMBillVO[] pms,List<PMBillVO> queryBillOfVOByCond , String string) {
		this.setPmbillvos(pms);
		this.setQueryBillOfVOByCond(queryBillOfVOByCond);
		this.setUserid(string);
		
	}
	

	@SuppressWarnings({ "static-access", "static-access" })
	@Override
	public void run() {	
		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuffer msgBuff = new StringBuffer();
		// 如果查询结果为空或者 与执行结果数量一致时 直接返回，
		if(queryBillOfVOByCond.size()!= 0  || queryBillOfVOByCond.size() == pmbillvos.length){
			return ;
		} 
		
		if (null == getPmbillvos() || getPmbillvos().length == 0) {
			msgBuff.append("PM后台任务已完成，但未接收到执行完成后的PM，请到预防性维护中检查!");
		}
		List<String> pklist = new ArrayList<String>();

		StringBuffer whereSql = new StringBuffer();
		if (null != getPmbillvos()) {

			for (PMBillVO pm : getPmbillvos()) {
				String pk_pre_bill = pm.getParentVO().getPk_pre_bill();
				pklist.add(pk_pre_bill);
			}
			whereSql.append(nc.vo.pf.pub.util.SQLUtil.buildSqlForIn("pk_wo",
					pklist.toArray(new String[0])));
			whereSql.append("' and dr=0 ");
			
			VOQuery<WorkOrderHeadVO> query = new VOQuery<WorkOrderHeadVO>(WorkOrderHeadVO.class);
			WorkOrderHeadVO[] query2 = query.query(pklist.toArray(new String[0]));

			if (null == query2 || query2.length == 0) {
				msgBuff.append("异常：后台任务有检查到PM，但工单未能正常执行，请检查服务器日志！");
			}
			RaybowIcMassageUtil rimu = new RaybowIcMassageUtil();
			if (null != msgBuff && msgBuff.length() != 0) {
				try {
					rimu.sedMassage("0001V1100000000038N7", getUserid(),
							"预防性维护异常", msgBuff.toString(), "~", "~");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

}
