/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.uif2;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.AbstractAction;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.funcnode.ui.action.INCAction;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.material.cost.MaterialCostVO;
import nc.vo.bd.material.fi.MaterialFiVO;
import nc.vo.bd.material.pfc.MaterialPfcVO;
import nc.vo.bd.material.pfcc.MaterialPFCCVO;
import nc.vo.bd.material.plan.MaterialPlanVO;
import nc.vo.bd.material.prod.MaterialProdVO;
import nc.vo.bd.material.pu.MaterialPuVO;
import nc.vo.bd.material.sale.MaterialSaleVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.bd.supplier.finance.SupFinanceVO;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.riasm.electronicsignature.ElectronicSignatureVO;
import nc.vo.riasm.electronicsignature.IconstEnum;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;
import nc.vo.sm.UserVO;
import nc.vo.uif2.LoginContext;

public abstract class NCAction extends AbstractAction implements
		AppEventListener {

	private static final long serialVersionUID = 2471962024773715385L;

	protected ActionInterceptor interceptor;

	protected IExceptionHandler exceptionHandler;

	protected INCActionStatusJudge ncActionStatusJudge = null;

	public static final String TOOLBAR_SHOWNAME_KEY = "TOOLBAR_SHOWNAME_KEY";

	List<ElectronicSignatureVO> listElect;
	private String pk;
	public NCAction() {

		setShowNameInToolbar(false);
	}

	public String getBtnName() {
		return (String) getValue(NAME);
	}

	public void setBtnName(String btnName) {
		putValue(NAME, btnName);
	}

	public void setBtnResdir(String resdir) {
		putValue(INCAction.NAMERESDIR, resdir);
	}

	public void setBtnResid(String resid) {
		putValue(INCAction.NAMERESID, resid);
	}

	public void setCode(String code) {
		putValue(INCAction.CODE, code);
	}

	public void handleEvent(AppEvent event) {

		updateStatus();
	}

	public void updateStatus() {

		boolean isEnable = isActionEnable();
		setEnabled(getNcActionStatusJudge() == null ? isEnable
				: getNcActionStatusJudge().isActionEnable(this, isEnable));
	}

	protected boolean isActionEnable() {
		return true;
	}

	public void setShowNameInToolbar(boolean isShow) {
		putValue(NCAction.TOOLBAR_SHOWNAME_KEY, isShow ? Boolean.TRUE
				: Boolean.FALSE);
	}

	public void actionPerformed(ActionEvent e) {
		Logger.debug("Entering " + getClass().toString() + ".actionPerformed");
		boolean ischeck = beforeDoAction(e);
		if (!ischeck)
			return;
		beforeDoAction();
		try {
			if (interceptor == null || interceptor.beforeDoAction(this, e)) {
				try {
					doAction(e);
					if (interceptor != null)
						interceptor.afterDoActionSuccessed(this, e);
					//2021 zhenghw 更新日志
					updatePk(e);
				} catch (Exception ex) {
					if (interceptor == null
							|| interceptor.afterDoActionFailed(this, e, ex)) {
						if (getExceptionHandler() != null) {
							processExceptionHandler(ex);
						} else if (ex instanceof RuntimeException) {
							throw (RuntimeException) ex;
						}

						throw new RuntimeException(ex);
					}
				}
			}
		} finally {
			Logger.debug("Leaving " + getClass().toString()
					+ ".actionPerformed");
		}

	}

	protected void processExceptionHandler(Exception ex) {

		// exceptionHandler.handlerExeption(ex);
		new ExceptionHandlerUtil().processErrorMsg4SpecialAction(this,
				getExceptionHandler(), ex);
	}

	protected void beforeDoAction() {
		LoginContext context = null;
		Method[] ms = this.getClass().getMethods();
		for (Method m : ms) {
			Class<?> clazz = m.getReturnType();
			if (AbstractUIAppModel.class.isAssignableFrom(clazz)) {
				try {
					AbstractUIAppModel model = null;
					if(m !=null)
					 model = (AbstractUIAppModel) m.invoke(this, null);
					if (model == null)
						continue ;
					LoginContext ctx = model.getContext();
					if (ctx == null)
						break;
					if (context == null)
						context = ctx;
					ShowStatusBarMsgUtil.showStatusBarMsg("", ctx);
				} catch (IllegalArgumentException e) {
					Logger.debug(e.getMessage());
				} catch (IllegalAccessException e) {
					Logger.debug(e.getMessage());
				} catch (InvocationTargetException e) {
					Logger.debug(e.getMessage());
				}
				break;
			}
		}
	}

	public boolean beforeDoAction(ActionEvent ae) {
		LoginContext context = null;
		AbstractUIAppModel model = null;
		Method[] ms = this.getClass().getMethods();
		for (Method m : ms) {
			Class<?> clazz = m.getReturnType();
			if (AbstractUIAppModel.class.isAssignableFrom(clazz)) {
				try {
					if(m !=null)
					 model = (AbstractUIAppModel) m.invoke(this, null);
					if (model == null)
						continue ;
					LoginContext ctx = model.getContext();
					if (ctx == null)
						break;
					if (context == null)
						context = ctx;
					ShowStatusBarMsgUtil.showStatusBarMsg("", ctx);
				} catch (IllegalArgumentException e) {
					Logger.debug(e.getMessage());
				} catch (IllegalAccessException e) {
					Logger.debug(e.getMessage());
				} catch (InvocationTargetException e) {
					Logger.debug(e.getMessage());
				}
				break;
			}
		}
		if (ae == null)
			return true;
		boolean ischeck = checkElectronicSignature(context, ae.getActionCommand(),model);
		return ischeck;
	}

	public void updatePk(ActionEvent ae){
		
		if(ae == null)
			return;
		AbstractUIAppModel amodel = null;
		Method[] ms = this.getClass().getMethods();
		for (Method m : ms) {
			Class<?> clazz = m.getReturnType();
			if (AbstractUIAppModel.class.isAssignableFrom(clazz)) {
				try {
					AbstractUIAppModel model = null;
					if(m !=null)
					 model = (AbstractUIAppModel) m.invoke(this, null);
					if (model == null)
						continue ;
					amodel = model;
					break;
				} catch (IllegalArgumentException e) {
					Logger.debug(e.getMessage());
				} catch (IllegalAccessException e) {
					Logger.debug(e.getMessage());
				} catch (InvocationTargetException e) {
					Logger.debug(e.getMessage());
				}
				break;
			}
		}
		
		if(amodel == null || StringUtil.isEmpty(ae.getActionCommand()))
			return;
		
		Object  o = amodel.getSelectedData();
		String billid = null;
		if(o instanceof AggregatedValueObject){
			AggregatedValueObject aggvo = (AggregatedValueObject)o;
			try {
				billid = aggvo.getParentVO().getPrimaryKey();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}else if(o instanceof  SuperVO){
			SuperVO vo1 = (SuperVO)o;
			if(vo1 instanceof SupFinanceVO){
				billid = ((SupFinanceVO)vo1).getPk_supplier();
			}else if(vo1 instanceof SupStockVO){
				billid = ((SupStockVO)vo1).getPk_supplier();
			}else if(vo1 instanceof MaterialFiVO){
				billid = ((MaterialFiVO)vo1).getPk_material();
			}else if(vo1 instanceof MaterialPuVO){
				billid = ((MaterialPuVO)vo1).getPk_material();
			}else if(vo1 instanceof MaterialSaleVO){
				billid = ((MaterialSaleVO)vo1).getPk_material();
			}else if(vo1 instanceof MaterialStockVO){
				billid = ((MaterialStockVO)vo1).getPk_material();
			}else if(vo1 instanceof MaterialPlanVO){
				billid = ((MaterialPlanVO)vo1).getPk_material();
			}else if(vo1 instanceof MaterialProdVO){
				billid = ((MaterialProdVO)vo1).getPk_material();
			}else if(vo1 instanceof MaterialCostVO){
				billid = ((MaterialCostVO)vo1).getPk_material();
			}else if(vo1 instanceof MaterialPFCCVO){
				billid = ((MaterialPFCCVO)vo1).getPk_material();
			}else if(vo1 instanceof MaterialPfcVO){
				billid = ((MaterialPfcVO)vo1).getPk_material();
			}else{
				billid  = vo1.getPrimaryKey();
			}
		}
		updatebillId(ae.getActionCommand(),billid);
	}
	public void updatebillId(String cmd,String billid ){
		if(StringUtils.isEmpty(billid)){
			return ;
		}
		ElectronicsignatureHisVO vo = null;
		try {
//			if(!cmd.contains("保存")){
//				return;
//			}
			vo = (ElectronicsignatureHisVO)HYPubBO_Client.queryByPrimaryKey(ElectronicsignatureHisVO.class, getPk());
			if(vo != null){
				vo.setBillid(billid);
				HYPubBO_Client.update(vo);
			}
		} catch (UifException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public boolean checkElectronicSignature(LoginContext context,
			String cmd,AbstractUIAppModel model ) {
		if (context == null)
			return true;
		String nodecode = context.getNodeCode();
		Log.getInstance(this.getClass()).info("功能节点号：" + nodecode);
		List<ElectronicSignatureVO> list = getListElect();
		if(list == null || list.size() == 0)
			return true;
		ElectronicSignatureVO bill = null;
		for (ElectronicSignatureVO vo : list) {
			if (nodecode.equals(vo.getPk_parent())) {
				bill = vo;
				break;
			}
		}
		boolean ischeck = IconstEnum.isCheckElectronicSignature(bill, cmd);
		if (!ischeck)
			return true;
		// 校验录入的签名
		AccreditLoginDialog dialog = null;
		try {
			Object  o = model.getSelectedData();
			String billid = null;
			if(o instanceof AggregatedValueObject){
				AggregatedValueObject aggvo = (AggregatedValueObject)o;
				billid = aggvo.getParentVO().getPrimaryKey();
			}else if(o instanceof  SuperVO){
				SuperVO vo1 = (SuperVO)o;
				billid  = vo1.getPrimaryKey();
			}
			dialog = new AccreditLoginDialog(
				context.getEntranceUI(), context, cmd,billid);
		
			UserVO vo = (UserVO) HYPubBO_Client.queryByPrimaryKey(UserVO.class,
					context.getPk_loginUser());
			dialog.getTfUser().setText(vo.getUser_code());
			dialog.getAction().setText(cmd);
			if (dialog.showModal() == UIDialog.ID_OK) {
				setPk(dialog.getPk());
				// String password = dialog.get;
				// this.loginName(context, usercode, password);
				return true;
			} else {
				return false;
			}
		} catch (UifException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}

	public abstract void doAction(ActionEvent e) throws Exception;

	public ActionInterceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(ActionInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(IExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public INCActionStatusJudge getNcActionStatusJudge() {
		return ncActionStatusJudge;
	}

	public void setNcActionStatusJudge(INCActionStatusJudge ncActionStatusJudge) {
		this.ncActionStatusJudge = ncActionStatusJudge;
	}

	private List<ElectronicSignatureVO> getElectronicSignatureVO() {

		List<ElectronicSignatureVO> list1 = null;
		try {
			IUAPQueryBS service = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			StringBuffer strb = new StringBuffer();
			strb.append(" select k.*from riasm_electronicsignature  k where nvl(k.dr,0) = 0 ");
			list1= (List<ElectronicSignatureVO>)service
					.executeQuery(strb.toString(),
							new BeanListProcessor(ElectronicSignatureVO.class));
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return list1;
	}

	public List<ElectronicSignatureVO> getListElect() {
		if (listElect == null || listElect.size() == 0)
			setListElect(getElectronicSignatureVO());
		return listElect;
	}

	public void setListElect(List<ElectronicSignatureVO> listElect) {
		this.listElect = listElect;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	
}
