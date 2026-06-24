package nc.ui.ic.inandoutui.linkquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.funcnode.ui.FuncletInitData;
import nc.md.persist.framework.MDPersistenceService;
import nc.sfbase.client.ClientToolKit;
import nc.ui.ic.general.model.ICGenBizModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener.IInitDataProcessor;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.components.IAutoShowUpComponent;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.param.ICMutiPKsLinkQueryParam;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * 联查多PK单据 联查存储key为 89
 * 
 * @since 6.0
 * @version 2011-12-12 上午10:40:18
 * @author jinjya
 */
public class ICGenMutiPkLinkQuery implements IInitDataProcessor {

	private IAutoShowUpComponent autoShowUpComponent;

	private ICGenBizModel model;

	private String voClass;

	public IAutoShowUpComponent getAutoShowUpComponent() {
		return this.autoShowUpComponent;
	}

	public ICGenBizModel getModel() {
		return this.model;
	}

	public String getVoClass() {
		return this.voClass;
	}

	@Override
	public void process(FuncletInitData data) {
		if (data == null
				|| !(data.getInitData() instanceof ICMutiPKsLinkQueryParam)) {
			return;
		}

		ICMutiPKsLinkQueryParam funInitData = (ICMutiPKsLinkQueryParam) data
				.getInitData();
		if (ValueCheckUtil.isNullORZeroLength(funInitData.getBillVOs())
				&& ValueCheckUtil.isNullORZeroLength(funInitData.getBillids())) {
			MessageDialog
					.showErrorDlg(
							ClientToolKit.getApplet(),
							NCLangRes.getInstance().getStrByID("4008001_0",
									"04008001-0073")/* 错误 */,
							NCLangRes.getInstance().getStrByID("4008001_0",
									"04008001-0690")/* 联查参数无效！ */);
			return;
		}
		if (!ValueCheckUtil.isNullORZeroLength(funInitData.getBillVOs())) {
			this.model.initModel(funInitData.getBillVOs());
			return;
		}
		try {
			Class<?> voClasss = Class.forName(this.voClass);
			if (SuperVO.class.isAssignableFrom(voClasss)
					|| AggregatedValueObject.class.isAssignableFrom(voClasss)) {
				Collection bills = MDPersistenceService
						.lookupPersistenceQueryService().queryBillOfVOByPKs(
								voClasss, funInitData.getBillids(), false);
				if (bills == null || bills.isEmpty()) {
					return;
				}

				List<Object> objs = new ArrayList<Object>();
				// 过滤掉dr=1的单据
				for (Object obj : bills.toArray()) {
					if (!(obj instanceof AbstractBill)) {
						objs.add(obj);
						continue;
					}
					AbstractBill bill = (AbstractBill) obj;
					if ((Integer) bill.getParentVO().getAttributeValue("dr") == 1) {
						continue;
					}
					/*** 2021 03 22 yezhian 修复材料出库单序列号单位为空问题 **********/
					if (obj instanceof nc.vo.ic.m4d.entity.MaterialOutVO) {
						ICBillBodyVO[] childrenVO = ((nc.vo.ic.m4d.entity.MaterialOutVO) obj)
								.getChildrenVO();
						for (ICBillBodyVO bvo : childrenVO) {
							SuperVO[] queryByCondition = HYPubBO_Client
									.queryByCondition(
											nc.vo.bd.material.stock.MaterialStockVO.class,
											"  pk_material = '"
													+ bvo.getCmaterialvid()
													+ "' and pk_org = '"
													+ bill.getParent()
															.getAttributeValue(
																	"pk_org")
													+ "'");
							if(queryByCondition == null || queryByCondition.length == 0){
								continue;
							}
							UFBoolean serialmanaflag = (UFBoolean) queryByCondition[0].getAttributeValue("serialmanaflag");
							String sernumunit = (String) queryByCondition[0].getAttributeValue("sernumunit");
							if(serialmanaflag == UFBoolean.TRUE  ){
								bvo.setCSnunitid(sernumunit);/* 序列号单位应取基本物料库存信息下的序列号单位 */
								
							}
						}
					}
					/************************************/
					objs.add(obj);
				}
				if (objs == null || objs.isEmpty()) {
					return;
				}
				this.model.initModel(objs.toArray());

			}
		} catch (BusinessException e1) {
			ExceptionUtils.wrappException(e1);
		} catch (ClassNotFoundException e1) {
			ExceptionUtils.wrappException(e1);
		}

		// 显示界面
		if (null != this.getAutoShowUpComponent()) {
			this.getAutoShowUpComponent().showMeUp();
		}

	}

	public void setAutoShowUpComponent(IAutoShowUpComponent autoShowUpComponent) {
		this.autoShowUpComponent = autoShowUpComponent;
	}

	public void setModel(ICGenBizModel model) {
		this.model = model;
	}

	public void setVoClass(String voClass) {
		this.voClass = voClass;
	}

}
