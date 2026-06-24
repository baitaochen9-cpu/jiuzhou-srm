package nc.ewm.print.add;

import nc.bs.ewm.printapply.ace.bp.AcePrintapplyInsertBP;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.vo.ewm.printapply.Printapply;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WorkOrderHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class PrintAddService {
	protected String sysList(AggWorkOrderVO AggWorkOrderVO)
			throws Exception {
		//处理传入的工单，生成打印申请单
		return assmbleBill(AggWorkOrderVO);
		
	}

	private String assmbleBill(AggWorkOrderVO work) throws Exception {
		AggPrintapply printvo = new AggPrintapply();
		Printapply lhvo = new Printapply();
		WorkOrderHeadVO whvo = work.getParentVO();
		//集团
		String group = whvo.getPk_group();
		lhvo.setPk_group(group);
		//组织
		lhvo.setPk_org(whvo.getPk_org());
		lhvo.setPkorg(whvo.getPk_org());
		lhvo.setPk_org_v(whvo.getPk_org_v());
		//创建人创建时间
		lhvo.setCreator(whvo.getCreator());
		lhvo.setCreationtime(whvo.getCreationtime());
		lhvo.setMaketime(new UFDateTime(System.currentTimeMillis()));
		lhvo.setBilldate(new UFDate(System.currentTimeMillis()));
		//业务类型，制单人
		lhvo.setBusitype(whvo.getBusi_type());
		lhvo.setBillmaker(whvo.getBillmaker());
		//来源单据类型
		lhvo.setSrcbilltype(whvo.getBill_type());
		lhvo.setPk_wo(whvo.getPrimaryKey());
		//id
		lhvo.setSrcbillid(whvo.getPrimaryKey());
		//工单pks
		//打印原因
		lhvo.setDef1("工单重新打印");
		//交易类型
		lhvo.setTranstype("CDSQ");
		lhvo.setBilltype("CDSQ");
		lhvo.setTranstypepk("0001ZZ1000000004N5XK");
		lhvo.setApprovestatus(-1);
		lhvo.setStatus(VOStatus.NEW);
		printvo.setParentVO(lhvo);
		return saveBase(printvo);
	}
	
	private String saveBase(AggPrintapply res) throws Exception {
		try {
			//新增
			AggPrintapply[] hvo = 
					new AcePrintapplyInsertBP().insert(new AggPrintapply[] {res});
			return hvo[0].getParentVO().billno;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("打印申请单保存执行失败:" + e.getMessage());
		}
	}


}
