package nc.ui.so.m4331.billui.action.extend.sourceref;

import java.awt.Container;

import nc.bs.framework.codesync.client.NCClassLoader;
import nc.ui.pub.pf.BillSourceVar;
import nc.ui.pubapp.billref.src.RefContext;
import nc.ui.pubapp.billref.src.RefInfo;
import nc.ui.pubapp.billref.src.view.SourceRefDlg;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.so.salepacklist.SalePackListHVO;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class BillSource4331RefDlgFor4345 extends SourceRefDlg {

	private static final long serialVersionUID = 1L;
	private static final String REF_BILLUI_BEAN_PATH = "nc/ui/so/m4331/billui/action/extend/sourceref/billref.xml";
	private static final String REF_BILLINFO_BEAN_PATH = "nc/ui/so/m4331/billui/action/extend/sourceref/refinfo_4331for4345.xml";

	public BillSource4331RefDlgFor4345(Container parent, BillSourceVar bsVar) {
		super(parent, bsVar);
		setResizable(true);
	}

	public String getRefBillInfoBeanPath() {
		String srcBillType = getBillSourceVar().getBillType();
		String currBillType = getBillSourceVar().getCurrBillOrTranstype();
		if (StringUtils.isEmpty(srcBillType)
				|| StringUtils.isEmpty(currBillType))
			ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0053"));
		return BillSource4331RefDlgFor4345.REF_BILLINFO_BEAN_PATH;
	}

	protected void createFactory() {

		this.factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(
				(BeanDefinitionRegistry) this.factory);
		((AbstractBeanFactory) this.factory).setBeanClassLoader(NCClassLoader
				.getNCClassLoader());

		Resource resource = new ClassPathResource(
				BillSource4331RefDlgFor4345.REF_BILLUI_BEAN_PATH,
				NCClassLoader.getNCClassLoader());
		try {
			reader.loadBeanDefinitions(resource);
			if (!StringUtil.isEmptyWithTrim(this.getRefBillInfoBeanPath())) {

				Resource pluginResource = new ClassPathResource(
						this.getRefBillInfoBeanPath(),
						NCClassLoader.getNCClassLoader());
				try {
					reader.loadBeanDefinitions(pluginResource);
				} catch (BeanDefinitionStoreException ex) {
					ExceptionUtils.wrappException(ex);
					// Logger.error(ex.getMessage(), ex);
				}
			}
			this.initContext();
		} catch (Exception e) {
			this.handleException(e);
		}
	}

	private void initContext() {
		this.getRefContext().setRefDialog(this);
		this.getRefContext().setRefInfo(
				(RefInfo) this.factory.getBean(RefContext.BEANNAME_REFINFO));
		this.getRefContext().getRefInfo()
				.setBillSrcVar(this.getBillSourceVar());
	}

	public AggregatedValueObject[] getRetVos() {
		AggregatedValueObject[] retvos = getRefBill().getSelectVOs();
		return retvos;
	}

	@Override
	public void closeOK() {
		if (checkItemNotNull()) {
			super.closeOK();
		}
	}

	private boolean checkItemNotNull() {
		try {
			AggregatedValueObject[] retvos = getRefBill().getSelectVOs();

			for (AggregatedValueObject retvo : retvos) {
				AggSalePackListHVO aggvo = (AggSalePackListHVO) retvo;
				SalePackListHVO hvo = aggvo.getParentVO();

				SalePackListBVO[] bodyVOs = (SalePackListBVO[]) aggvo
						.getChildrenVO();
				if (bodyVOs == null || bodyVOs.length == 0) {
					throw new BusinessException("行号：" + hvo.getCode()
							+ "，表体不能为空");
				}

				int i = 1;
				for (SalePackListBVO bvo : bodyVOs) {
					if (StringUtil.isEmpty(bvo.getSpec())) {
						throw new BusinessException("行号：" + i + "，包装规格不能为空");
					}

					if (StringUtil.isEmpty(bvo.getPalleno())) {
						throw new BusinessException("行号：" + i + "，托盘号不能为空");
					}
					if (StringUtil.isEmpty(bvo.getSpec_t())) {
						throw new BusinessException("行号：" + i + "，托盘规格不能为空");
					}
					i++;
				}
				//
				boolean ischecknum = checkNum(bodyVOs[0].getSpec());
				if (ischecknum) {
					String def1 = hvo.getDef1();
					UFDouble nusenum = new UFDouble(def1);
					UFDouble nnum = hvo.getNgrosswt();
					if (nusenum.compareTo(nnum) != 0) {
						throw new BusinessException("已分配数量不等于总数量！");
					}
				}
				
			}
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			handleException(e1);
		}
		return false;
	}
	
	private boolean checkNum(String spec) {
		try {
			DefdocVO defvo = getDefdocVO(spec);
			if (defvo != null && "Y".equalsIgnoreCase(defvo.getDef3())) {
				return true;
			}
		} catch (Exception ivjExc) {
			handleException(ivjExc);
		}
		return false;
	}

	// 包装规格
	private DefdocVO getDefdocVO(String value) throws UifException {
		DefdocVO bvo = (DefdocVO) HYPubBO_Client.queryByPrimaryKey(
				DefdocVO.class, value);
		return bvo;
	}

}
