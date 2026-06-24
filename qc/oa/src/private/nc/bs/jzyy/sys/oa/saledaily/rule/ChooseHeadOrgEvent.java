package nc.bs.jzyy.sys.oa.saledaily.rule;

import nc.bs.pf.pub.PfDataCache;
import nc.impl.pubapp.env.BSContext;
import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.bd.currency.CurrencyRate;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.scmpub.reference.uap.para.SysParaInitQuery;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleChangeVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.scmpub.res.para.NCPara;

import com.google.gdata.util.common.base.StringUtil;

/**
 * 选择表头组织设置相关联的数据
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 上午11:32:28   
 * @version NCC1909
 */
public class ChooseHeadOrgEvent extends CtFieldEvent{

	
	private AggCtSaleVO[] aggVOs = null;
	
	public ChooseHeadOrgEvent(AggCtSaleVO[] aggVOs){
		this.aggVOs = aggVOs;
	}
	@Override
	public void process() {
		for(AggCtSaleVO aggvo : aggVOs){
			//1、设置表头集团组织
			this.fillBase(aggvo);
			//2、根据组织设置联动信息
			this.LinkView(aggvo);
			//3、设置其他信息
			this.setOtherView(aggvo);
			//4、根据默认财务组织，设置报税国  发货国信息
			this.setTaxCountry(aggvo);
			//5 变更版本信息
			this.setCahngeVOInfo(aggvo);
		}
		
	}
	
	/**
	 * 填充基本信息  组织集团 公司 交易类型
	 * @param aggvo
	 */
	private void fillBase(AggCtSaleVO aggvo) {
		String pkGroup = AppContext.getInstance().getPkGroup();
		UFDate busiDate = AppContext.getInstance().getBusiDate();
		CtSaleVO parentVO = aggvo.getParentVO();
		String pk_org = parentVO.getPk_org();
		String pk_org_v = null;
		if (!ValueUtil.isEmpty(pk_org)) {
	        pk_org_v = OrgUnitPubService.getOrgVid(pk_org);
	    }
		parentVO.setAttributeValue(CtAbstractVO.PK_GROUP, pkGroup);
		parentVO.setAttributeValue(CtAbstractVO.PK_ORG_V, pk_org_v);
		parentVO.setAttributeValue(CtAbstractVO.CBILLTYPECODE, CTBillType.SaleDaily.getCode());
		parentVO.setSubscribedate(busiDate);
		
		//设置交易类型
		BilltypeVO trantypeVO = PfDataCache.getBillTypeInfo(pkGroup, parentVO.getVtrantypecode());
		parentVO.setAttributeValue(CtSaleVO.CTRANTYPEID, trantypeVO.getPk_billtypeid());
		
	}
	
	/**
	 * 处理关联项  币种 汇率
	 * @param aggvo
	 */
	private void LinkView(AggCtSaleVO aggvo) {
		//设置币种及汇率
		CtSaleVO parentVO = aggvo.getParentVO();
		String pk_org = parentVO.getPk_org();
		UFDate busiDate = parentVO.getSubscribedate();
		// 组织本位币
		String pk_currtype = OrgUnitPubService.queryOrgCurrByPk(pk_org);
		if(parentVO.getCorigcurrencyid() == null){
			parentVO.setAttributeValue(CtAbstractVO.CORIGCURRENCYID, pk_currtype);
		}
		if(parentVO.getCcurrencyid() == null){
			parentVO.setAttributeValue(CtAbstractVO.CCURRENCYID, pk_currtype);
		}
		if(parentVO.getNexchangerate() == null){
			parentVO.setAttributeValue(CtAbstractVO.NEXCHANGERATE, UFDouble.ONE_DBL);
		}
		String origcurrency = parentVO.getCorigcurrencyid();
		 //集团本币汇率 没有币种或者日期不再后续处理
		if (!StringUtil.isEmptyOrWhitespace(pk_currtype) && 
				!StringUtil.isEmptyOrWhitespace(origcurrency) && busiDate != null) {
		    // 集团本币汇率
		    UFDouble groupChangeRate = null;
		    BSContext ctx = new BSContext();
		    // 如果不是 "基于原币计算" "基于组织本位币计算" 那么就是不启用，
		    // groupChangeRate = UFDouble.ZERO_DBL
		    String nc001 = SysParaInitQuery.getParaString(ctx.getGroupID(), "NC001");
		    if (NCPara.NC001_CALCULATEBYORIGCURRTYPE.getName().equals(nc001)) {
		      groupChangeRate =
		          CurrencyRate.getGroupLocalCurrencyBuyRate(origcurrency, busiDate);
		    }
		    else if (NCPara.NC001_CALCULATEBYCURRTYPE.getName().equals(nc001)) {
		      groupChangeRate =
		          CurrencyRate.getGroupLocalCurrencyBuyRate(pk_currtype, busiDate);
		    }
		    if (groupChangeRate != null) {
		    	parentVO.setAttributeValue(CtAbstractVO.NGROUPEXCHGRATE, groupChangeRate);
		    }
	    }
		//全局本币汇率 没有币种或者日期不再后续处理
		if (!StringUtil.isEmptyOrWhitespace(pk_currtype) && 
				!StringUtil.isEmptyOrWhitespace(origcurrency) && busiDate != null) {
			UFDouble globalChangeRate = null;
		    // 如果不是 "基于原币计算" "基于组织本位币计算" 那么就是不启用，
		    String nc002 = SysParaInitQuery.getParaString(IOrgConst.GLOBEORG, "NC002");
		    if (NCPara.NC002_CALCULATEBYORIGCURRTYPE.getName().equals(nc002)) {
		      globalChangeRate =
		          CurrencyRate.getGlobalLocalCurrencyBuyRate(origcurrency, busiDate);
		    }
		    else if (NCPara.NC002_CALCULATEBYCURRTYPE.getName().equals(nc002)) {
		      globalChangeRate =
		          CurrencyRate.getGlobalLocalCurrencyBuyRate(pk_currtype, busiDate);
		    }
		    if (globalChangeRate != null) {
		    	parentVO.setAttributeValue(CtAbstractVO.NGLOBALEXCHGRATE, globalChangeRate);
		    }
	    }
	}
	
	private void setOtherView(AggCtSaleVO aggvo) {
		CtSaleVO parentVO = aggvo.getParentVO();
		String pk_group = parentVO.getPk_group();
		String pk_org = parentVO.getPk_org();
		String pk_org_v = parentVO.getPk_org_v();
		//设置表体信息
		Integer rowNo = 10;
		for(CtSaleBVO bvo : aggvo.getCtSaleBVO()){
			bvo.setAttributeValue(CtAbstractBVO.PK_GROUP, pk_group);
			bvo.setAttributeValue(CtAbstractBVO.PK_ORG, pk_org);
			bvo.setAttributeValue(CtAbstractBVO.PK_ORG_V, pk_org_v);
			bvo.setAttributeValue(CtAbstractBVO.CROWNO, rowNo);
			rowNo = rowNo + 10;
			boolean blen = false;
			blen = OrgUnitPubService.isTypeOf(pk_org, IOrgConst.FINANCEORGTYPE);
		    if (blen) {
		    	  bvo.setAttributeValue(CtAbstractBVO.PK_FINANCEORG, pk_org);
		    	  bvo.setAttributeValue(CtAbstractBVO.PK_FINANCEORG_V, pk_org_v);
		          String taxcountry = CTVatUtil.getTaxCountry(pk_org);
		          bvo.setAttributeValue(CTVatNameConst.CTAXCOUNTRYID, taxcountry);
		    }
		    else {
		    	  OrgVO vo = OrgUnitPubService.getOrg(pk_org);
		          String pk_corp = vo.getPk_corp();
		          String pk_corp_v = OrgUnitPubService.getOrgVid(pk_corp);
		          bvo.setAttributeValue(CtAbstractBVO.PK_FINANCEORG, pk_corp);
		          bvo.setAttributeValue(CtAbstractBVO.PK_FINANCEORG_V, pk_corp_v);
		          String taxcountry = CTVatUtil.getTaxCountry(pk_corp);
		          bvo.setAttributeValue(CTVatNameConst.CTAXCOUNTRYID, taxcountry);
		    }
		}
		UFDate busiDate = AppContext.getInstance().getBusiDate();
		// 设置版本信息
		parentVO.setAttributeValue(CtAbstractVO.VERSION, UFDouble.ONE_DBL);
        // 设置打印次数为0
		parentVO.setAttributeValue(CtAbstractVO.IPRINTCOUNT, Integer.valueOf(0));
        // 设置制单时间
		parentVO.setAttributeValue(CtAbstractVO.DBILLDATE, busiDate);
		//签订时间
		parentVO.setAttributeValue(CtAbstractVO.SUBSCRIBEDATE, busiDate);
		//状态
		parentVO.setAttributeValue(CtAbstractVO.FSTATUSFLAG, CtFlowEnum.Free.value());
	}
	
	/**
	 * 根据默认财务组织，设置报税国
	 * @param aggvo
	 */
	private void setTaxCountry(AggCtSaleVO aggvo) {
		String pk_org = aggvo.getParentVO().getPk_org();
		String pk_customer = aggvo.getParentVO().getPk_customer();
		String sendcountry = CTVatUtil.getSaleSendcountry(pk_org);
		for(CtSaleBVO bvo : aggvo.getCtSaleBVO()){
		      String taxcountry = CTVatUtil.getTaxCountry(bvo.getPk_financeorg());
		      bvo.setAttributeValue(CTVatNameConst.CTAXCOUNTRYID, taxcountry);
		      bvo.setAttributeValue(CTVatNameConst.CSENDCOUNTRYID, sendcountry);
		      
		      String rececountry = CTVatUtil.getSaleRececountry(pk_customer);
		      bvo.setAttributeValue(CTVatNameConst.CRECECOUNTRYID, rececountry);
		      BuySellFlagEnum buysell =
		              CTVatUtil.getSaleBuySellFlag(rececountry, taxcountry);
	          if (null != buysell) {
	        	  bvo.setAttributeValue(CTVatNameConst.FBUYSELLFLAG, buysell.value());
	          }
	          Boolean triatrade =
	              CTVatUtil.getSaleTriatradeFlag(sendcountry, taxcountry, buysell);
	          bvo.setAttributeValue(CTVatNameConst.BTRIATRADEFLAG, triatrade);
		}
	}

	/**
	 * 设置默认变更信息
	 * @param aggvo
	 */
	private void setCahngeVOInfo(AggCtSaleVO aggvo) {
		CtSaleChangeVO changeVO = new CtSaleChangeVO();
		changeVO.setPk_group(aggvo.getParentVO().getPk_group());
		changeVO.setPk_org(aggvo.getParentVO().getPk_org());
		changeVO.setPk_org_v(aggvo.getParentVO().getPk_org_v());
		changeVO.setVchangecode(new UFDouble(1.0));
		changeVO.setVmemo("初始版本");
		aggvo.setCtSaleChangeVO(new CtSaleChangeVO[]{changeVO});
	}

	

}
