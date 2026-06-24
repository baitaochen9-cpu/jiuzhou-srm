package nccloud.web.ct.saledaily.action;


import java.util.ArrayList;
import java.util.List;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleChangeVO;
import nc.vo.ct.saledaily.entity.CtSaleExecVO;
import nc.vo.ct.saledaily.entity.CtSaleExpVO;
import nc.vo.ct.saledaily.entity.CtSaleMemoraVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ct.saledaily.entity.CtSaleTermVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.saledaily.entity.RecvPlanVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillCombinServer;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.commons.lang.StringUtils;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;
import nccloud.pubitf.scmpub.commit.service.IBatchRunScriptService;
import nccloud.web.ct.saledaily.utils.SaleDailyCompareUtil;
import nccloud.web.scmpub.pub.operator.SCMExtBillCardOperator;

/**
 * @description 销售合同变更
 * @author wangshrc
 * @date 2019年1月23日 下午4:57:37
 * @version ncc1.0
 */
public class SaleDailyCardModifySaveAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		SCMExtBillCardOperator operator = SaleDailyCompareUtil.getBillCardOperator();
		AggCtSaleVO vo = (AggCtSaleVO) operator.toBill(request);
		AggCtSaleVO origvo = (AggCtSaleVO) operator.toBill(request);
		this.checkChgReason(vo);
		AggCtSaleVO[] bills = new AggCtSaleVO[] { vo };
		appendPseudoColumn(bills);
		CloudPFlowContext context = new CloudPFlowContext();
		context.setTrantype(vo.getParentVO().getVtrantypecode());
		context.setBillType(CTBillType.SaleDaily.getCode());
		context.setBillVos(bills);
		context.setActionName("MODIFY");
		SCMScriptResultDTO dto = ServiceLocator.find(IBatchRunScriptService.class).runBacth(context, AggCtSaleVO.class);
		if (dto.getSucessVOs() == null) {			
			return null;
		}
		AggCtSaleVO singleVo = (AggCtSaleVO) dto.getSucessVOs()[0];
		//应该把返回的数据更新到前台，add by lichaoah
		ClientBillCombinServer<AggCtSaleVO> util = new ClientBillCombinServer<AggCtSaleVO>();
		
		//合并差异对比前要去掉被删除的表体行
		processBody(bills);
		util.combine(bills,new AggCtSaleVO[] { singleVo});
//		ExtBillCard billcard = operator.toCard(singleVo);
		ExtBillCard billcard = SaleDailyCompareUtil.operator(operator, bills[0],origvo);
		return billcard;
	}

	private void checkChgReason(AggCtSaleVO vo) {
		CtSaleChangeVO chgVO = this.getLastVersionVO(vo.getCtSaleChangeVO());
		if (chgVO != null && StringUtils.isEmpty(chgVO.getVchgreason())) {
			ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
					"04020003-0336", null, new String[] { vo.getParentVO().getVbillcode() }));// 变更原因不可为空:单据号为{0}
		} else {
			CtSaleVO ctHeadVo = vo.getParentVO();
			/**
			 * 港华需求： 原有变更保存是直接版本号+1，生成生效态新版本合同 这里根据需求，先判断合同状态是否为”生效“
			 * 如果是，走原来的逻辑，版本号+1，后台save再处理单据状态原来设置为生效态的逻辑，改为自由态
			 * 如果不是，vo不做处理，对应场景是自由态新版本合同变更保存（修改）
			 */
			if (CtFlowEnum.VALIDATE.toIntValue() == ctHeadVo.getFstatusflag().intValue()) {
				chgVO.setPk_group(ctHeadVo.getPk_group());
				chgVO.setPk_org(ctHeadVo.getPk_org());
				chgVO.setPk_org_v(ctHeadVo.getPk_org_v());
				chgVO.setPk_ct_sale(ctHeadVo.getPk_ct_sale());
				chgVO.setVchgdate(new UFDate(SessionContext.getInstance().getClientInfo().getBizDateTime()));
				chgVO.setVchgpsn(SessionContext.getInstance().getClientInfo().getUserid());
			}
		}
	}

	private CtSaleChangeVO getLastVersionVO(CtSaleChangeVO[] CtSaleChangeVOs) {
		UFDouble version = UFDouble.ONE_DBL;
		CtSaleChangeVO resultVO = CtSaleChangeVOs[0];
		for (CtSaleChangeVO chgVO : CtSaleChangeVOs) {
			if (chgVO == null) {
				continue;
			}
			if (MathTool.compareTo(chgVO.getVchangecode(), version) > 0) {
				version = chgVO.getVchangecode();
				resultVO = chgVO;
			}
		}
		return resultVO;
	}
	/**
	 * 增加伪列
	 * @param bills
	 */
	private void appendPseudoColumn(AggCtSaleVO[] bills) {
		if (bills != null && bills.length > 0) {
			for (AggCtSaleVO bill : bills) {
					IBillMeta billMeta = bill.getMetaData();
					IVOMeta[] childMetas = billMeta.getChildren();
				    for (IVOMeta childMeta : childMetas) {
				    	ISuperVO[] clientVOs = bill.getChildren(childMeta);
				    	if(clientVOs!=null) {				    		
				    		for (int i = 0; i < clientVOs.length; i++) {
				    			clientVOs[i].setAttributeValue("pseudocolumn", i);
				    		}
				    	}
				    }				
			}
		}
	}

	private void processBody(AggCtSaleVO[] vos) {
		CtSaleBVO[] ctSaleBVO = vos[0].getCtSaleBVO();
		vos[0].setCtSaleBVO(removeDelBody(ctSaleBVO));
		
		CtSaleChangeVO[] ctSaleChangeVO = vos[0].getCtSaleChangeVO();
		vos[0].setCtSaleChangeVO(removeDelBody(ctSaleChangeVO));
		
		CtSaleExecVO[] ctSaleExecVO = vos[0].getCtSaleExecVO();
		vos[0].setCtSaleExecVO(removeDelBody(ctSaleExecVO));
		
		CtSaleTermVO[] ctSaleTermVO = vos[0].getCtSaleTermVO();
		vos[0].setCtSaleTermVO(removeDelBody(ctSaleTermVO));
		
		CtSaleExpVO[] ctSaleExpVO = vos[0].getCtSaleExpVO();
		vos[0].setCtSaleExpVO(removeDelBody(ctSaleExpVO));
		
		CtSaleMemoraVO[] ctSaleMemoraVO = vos[0].getCtSaleMemoraVO();
		vos[0].setCtSaleMemoraVO(removeDelBody(ctSaleMemoraVO));
		
		RecvPlanVO[] ctRecvPlanVO = vos[0].getCtRecvPlanVO();
		vos[0].setCtRecvPlanVO(ctRecvPlanVO);
		
		CtSalePayTermVO[] ctSalePayTermVO = vos[0].getCtSalePayTermVO();
		vos[0].setCtSalePayTermVO(ctSalePayTermVO);

	}

	@SuppressWarnings("unchecked")
	private SuperVO[] removeDelBody(SuperVO[] vos) {
		List<SuperVO> realbody = new ArrayList<SuperVO>();
		for (SuperVO vo : vos) {
			if (vo.getStatus() != 3) {
				realbody.add(vo);
			}
		}
		return realbody.toArray(new SuperVO[] {});
	}
}
