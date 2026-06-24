package nc.bs.ic.m45.insert;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.general.insert.IInsertBP;
import nc.bs.ic.general.insert.InsertBPTemplate;
import nc.bs.ic.general.rule.after.AtpAfterUpdate;
import nc.bs.ic.general.rule.before.CtplcustomeridCheck;
import nc.bs.ic.m45.base.BPPlugInPoint;
import nc.bs.ic.m45.base.rule.FillCostOrgRule;
import nc.bs.ic.m45.base.rule.PurchaseBillCheckRule;
import nc.bs.ic.m45.base.rule.PurchaseInAssetWarehouseCheck;
import nc.bs.ic.m45.base.rule.PurchaseInVOScaleCheckRule;
import nc.bs.ic.m45.base.rule.PurchaseinRetMarginProcess;
import nc.bs.ic.m45.base.rule.ReturnSnIsExistInEquipcardCheck;
import nc.bs.ic.m45.base.rule.VmiCheckRule;
import nc.bs.ic.m45.insert.rule.InsReWritePIM;
import nc.bs.ic.m45.insert.rule.InsertCheckCanInRule;
import nc.bs.ic.m45.insert.rule.InsertRewritePORule;
import nc.bs.ic.m45.insert.rule.M45SaveAndRewriteOrderPayPlan;
import nc.bs.ic.m45.irule.FillCstateidForm45Rule;
import nc.bs.ic.m45.irule.InsReWriteBctchCodeRule;
import nc.bs.ic.m45.update.rule.PurchInQualiStatuskRule;
import nc.bs.ic.pub.base.ICAroundProcesser;
import nc.bs.ic.pub.base.IInsertRuleProvider;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.ic.hd.HdCommonConstant;
import nc.itf.ic.hd.IQueryBatchCodeForm45;
import nc.itf.uap.busibean.ISysInitQry;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 采购入库单后台新增 BP
 * 
 * @author songhy
 */
public class InsertBP implements IInsertBP<PurchaseInVO>,
		IInsertRuleProvider<PurchaseInVO> {

	@Override
	public void addAfterRule(PurchaseInVO[] vos,
			AroundProcesser<PurchaseInVO> processor) {

		/**
		 * 放在现存量更新规则（OnhandAfterUpdate）之后，因为现存量更新时会调用预留接口，预留接口会回写采购订单，
		 * 如果入库数量=订单数量，订单会自动入库关闭，入库关闭会解除预留，导致后续操作错误
		 */
		((ICAroundProcesser<PurchaseInVO>) processor).addAfterRuleAt(
				new InsertRewritePORule(), AtpAfterUpdate.class);
		processor.addAfterRule(new InsReWritePIM());
		// add by yechd5 at 2017-07-03 入库单保存回写采购订单付款计划
    		processor.addAfterRule(new M45SaveAndRewriteOrderPayPlan());
		// processor.addAfterRule(new InsertRewritePORule());
		//更新批次号档案信息--xyp  华东医药
		processor.addAfterRule(new InsReWriteBctchCodeRule());
		processor.addAfterRule(new nc.bs.ic.general.sign.rule.SpaceAttributeCheck<PurchaseInVO>());
	}

	@Override
	public void addBeforeRule(PurchaseInVO[] vos,
			AroundProcesser<PurchaseInVO> processor) {

		// 检查是否可以生成入库单
		processor.addBeforeRule(new InsertCheckCanInRule());
		processor.addBeforeRule(new FillCostOrgRule());
		processor.addBeforeRule(new PurchaseBillCheckRule());
		processor.addBeforeRule(new PurchaseInAssetWarehouseCheck());
		processor.addBeforeRule(new CtplcustomeridCheck<PurchaseInVO>());
		processor.addBeforeRule(new VmiCheckRule());
		processor.addBeforeRule(new ReturnSnIsExistInEquipcardCheck());
		// 红蓝字入库单倒挤，跟订单倒挤
		processor.addBeforeRule(new PurchaseinRetMarginProcess());
		processor.addBeforeRule(new PurchaseInVOScaleCheckRule());
		// 华东医药库存状态保存更新
		processor.addBeforeRule(new FillCstateidForm45Rule());
		//2021-06-29 liyf 校验供应商和生产者质量状态
	    processor.addBeforeRule(new PurchInQualiStatuskRule());

	}

	@Override
	public PurchaseInVO[] insert(PurchaseInVO[] bills) {
		// 应该在“物理自由辅助属性检查”规则执行前补充供应商
		this.fillCvendorid(bills);
		this.setBatchCodeInFirst(bills);
		this.setCstateidFor45(bills);//采购入库默认库存状态赋值--华东医药--xyp
		InsertBPTemplate<PurchaseInVO> insertBP = new InsertBPTemplate<PurchaseInVO>(
				BPPlugInPoint.InsertBP, this);
		return insertBP.insert(bills);
	}
	
	
	/**
	   * 在执行前规则之前，先把批次号set
	   * add by Yong
	   * @param bills
	   */
	  public void setBatchCodeInFirst(PurchaseInVO[] bills){
		  //保存之前根据质量代码自动带出批次号  add by Yong 
		    IQueryBatchCodeForm45 iQueryBatchCodeForm45=NCLocator.getInstance().lookup(IQueryBatchCodeForm45.class);
//			for(PurchaseInVO vo:bills){
//				try {
//					if (iQueryBatchCodeForm45.getSCM_HD001(vo)!=null && "Y".equals(iQueryBatchCodeForm45.getSCM_HD001(vo))) {
//						iQueryBatchCodeForm45.setBathCode(vo);
//					}
//				} 
//				catch (BusinessException e) {
//					ExceptionUtils.wrappException(e);
//				}
//			}
	  }

	/**
	 * 补表体的供应商，供应商已经挪到表头，表体的供应商要跟表头供应商一样
	 * 
	 * @param vos
	 */
	private void fillCvendorid(PurchaseInVO[] vos) {
		String cvendor = null;
		for (PurchaseInVO vo : vos) {

			// 取表头供应商
			cvendor = vo.getHead().getCvendorid();
			if (StringUtil.isSEmptyOrNull(cvendor)) {
				continue;
			}

			// 为表体供应商赋值
			for (PurchaseInBodyVO body : vo.getBodys()) {
				body.setCvendorid(cvendor);
			}
		}
	}

	public void setCstateidFor45(PurchaseInVO[] vos) {
		/**
		 * 表体赋值
		 */

		ISysInitQry operator = (ISysInitQry) NCLocator.getInstance().lookup(
				ISysInitQry.class.getName());
		String pk_org = null;
		try {
			for (PurchaseInVO purchaseInVO : vos) {
				// 取表头供应商
				pk_org = purchaseInVO.getHead().getPk_org();
				if (StringUtil.isSEmptyOrNull(pk_org)) {
					continue;
				}
				// 为表体供应商赋值
				for (PurchaseInBodyVO body : purchaseInVO.getBodys()) {
					String material = body.getCmaterialvid();
					String Cstateid = body.getCstateid();
					if (Cstateid != null && !"".equals(Cstateid)) {
						continue;
					}
					if (StringUtil.isSEmptyOrNull(material)) {
						continue;
					}
					if (body.getCstateid() == null
							|| "".equals(body.getCstateid())) {
						// 库存组织
						int row = queryMaterialKCStatus(material);
						String pk_as = null;
						if (row != 0) {
							// 根据参数查询库存状态
							pk_as = operator.getParaString(pk_org,
									HdCommonConstant.PROIC001);
//							if (pk_as == null || "".equals(pk_as)) {
////								ExceptionUtils
////										.wrappBusinessException("当前组织无法获取 PROIC001 参数");
//								continue;
//							}
							body.setCstateid(pk_as);
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

	public int queryMaterialKCStatus(String material) throws BusinessException {
		List<Object[]> retlist = null;
		try {
			BaseDAO dao = new BaseDAO();
			String sql = "select bd_marasstframe.fix1 from bd_material\n"
					+ "left join bd_marasstframe on bd_material.pk_marasstframe=bd_marasstframe.pk_marasstframe\n"
					+ "where bd_material.pk_material='"
					+ material
					+ "' and bd_marasstframe.fix1='Y' and bd_material.dr=0 and bd_marasstframe.dr=0";
			retlist = (ArrayList<Object[]>) dao.executeQuery(sql,
					new ArrayListProcessor());
		} catch (Exception e) {
			return 0;
		}
		return retlist.size();
	}

}
