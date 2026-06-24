package nc.ui.uif2;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;

import nc.bs.busilog.vo.BusinessLogESVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.funcnode.ui.action.INCAction;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.md.data.access.NCObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.ui.uif2.gmplog.GmpLogProcessor;
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
import nc.vo.pub.VOStatus;
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
	//电子签名信息
	private String pk;
	
	private String signVnote;
	
	//电子签名信息


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
		//电子签名校验
		boolean ischeck = beforeDoAction(e);
		if (!ischeck)
			return;
		beforeDoAction();
		try {
			if (interceptor == null || interceptor.beforeDoAction(this, e)) {
				try {
					//获取当前界面数据，用来后续比较
					Object cilentBill = null;
					//当前数据库保存的数据
					NCObject oldncobj =null;
					if(getPk() !=null){
						cilentBill =  getSuperVO(e);
						GmpLogProcessor process = new GmpLogProcessor();
						oldncobj = process.qyrNCObj(cilentBill);
						//如果是供应商-集团 保存前VO ，单独处理一下企业地址
						if("10140SUG".equalsIgnoreCase(context.getNodeCode())){
//							process.dealCorpAddress(cilentBill);
						}
					}
					
					
					doAction(e); 
			
					if (interceptor != null)
						interceptor.afterDoActionSuccessed(this, e);
					//如果增加了电子签名记录
					if(getPk() !=null){
						try {
							//只针对物料档案更新业务操作日志 更新 sm_busilog_default_es
							if (context != null && "10140MAG".equals(context.getNodeCode())) {
								SuperVO clientHeadVo = null;
								if(cilentBill!=null){
									if (cilentBill instanceof AggregatedValueObject) {
										AggregatedValueObject aggvo = (AggregatedValueObject) cilentBill;
										clientHeadVo = (SuperVO) aggvo.getParentVO();
									} else if (cilentBill instanceof SuperVO) {
										clientHeadVo = (SuperVO) cilentBill;
									}
								}						
								updateLog(clientHeadVo);
							}
							
	//						更新本次记录的电子签名的单据PK
							updatePk(e);
						//20230710 liyf 记录审计日志
							setGmpLog(e,cilentBill,oldncobj);
						} catch (Exception e1) {

							Logger.error("记录审计日志异常:-->>"+e1.getMessage());
												}
					}
					
				} catch (Exception ex) {
					Logger.error(ex);
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

	/**
	 * 记录GPM审计日志
	 * @param ae
	 * @throws BusinessException
	 */
	public void setGmpLog(ActionEvent ae,Object cilentBill,NCObject oldncobj) throws Exception {
		// TODO Auto-generated method stub
		if(ae == null)
			return ;
		LoginContext context = null;
		AbstractUIAppModel model = null;
		Method[] ms = this.getClass().getMethods();
		for (Method m : ms) {
			Class<?> clazz = m.getReturnType();
			if (AbstractUIAppModel.class.isAssignableFrom(clazz)) {
				try {
					model = (AbstractUIAppModel) m.invoke(this, null);
					if (model == null)
						return ;
					LoginContext ctx = model.getContext();
					if (ctx == null)
						break;
					if (context == null)
						context = ctx;
					ShowStatusBarMsgUtil.showStatusBarMsg("", ctx);
				} catch (Exception e) {
					Logger.debug(e.getMessage());
				} 
				break;
			}
		}
		
		Object afcilentBill = getSuperVO(ae);////保存后界面VO
		
		GmpLogProcessor process = new GmpLogProcessor();
		//
		HashMap<String ,Object> otherParams = new HashMap<String ,Object>();
		otherParams.put("btnname", getBtnName());
		otherParams.put("signpk", getPk());
		otherParams.put("signvnote",signVnote);
		otherParams.put("bfsavevo",cilentBill);//保存前界面VO
		otherParams.put("bfncobj",oldncobj);//保存前界面VO的NCOBJECT
		otherParams.put("afsavevo",afcilentBill);//保存后界面VO
		process.setGmpLog(context,model,otherParams);
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
					AbstractUIAppModel model = (AbstractUIAppModel) m.invoke(
							this, null);
					if (model == null)
						return;
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
					model = (AbstractUIAppModel) m.invoke(this, null);
					if (model == null)
						return true;
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
		if(ae == null)
			return true;
		boolean ischeck = checkElectronicSignature(context,
				ae.getActionCommand(), model);
		return ischeck;
	}
	LoginContext context = null;
	public Object  getSuperVO(ActionEvent ae) {
		AbstractUIAppModel amodel = null;
	
		Method[] ms = this.getClass().getMethods();
		
		UIState uistate = UIState.EDIT;
		for (Method m : ms) {
			Class<?> clazz = m.getReturnType();
			if (AbstractUIAppModel.class.isAssignableFrom(clazz)) {
				try {
					AbstractUIAppModel model = (AbstractUIAppModel) m.invoke(
							this, null);
					if (model == null)
						return null;
					amodel = model;
					
					uistate = model.getUiState();
					LoginContext ctx = model.getContext();
					if (ctx == null)
						break;
					if (context == null)
						context = ctx;
				} catch (Exception e) {
					Logger.debug(e.getMessage());
				} 
				break;
			}
			
			
		}

		if (amodel == null || ae == null ||StringUtil.isEmpty(ae.getActionCommand()))
			return null;

		Object o = amodel.getSelectedData();
		//如果是新增状态,model.getSelectedData(); 获取到的是空或者之前的界面数据
		if(uistate == UIState.ADD){
			for (Method m : ms) {
				Class<?> clazz = m.getReturnType();
				if (nc.ui.uif2.editor.IEditor.class.isAssignableFrom(clazz)) {
					try {
						nc.ui.uif2.editor.IEditor editor = (nc.ui.uif2.editor.IEditor) m.invoke(
								this, null);
						o = editor.getValue();
					} catch (Exception e) {
						Logger.debug(e.getMessage());
					} 
					break;
				}
				
				
			}
			
		}
		
		
		
		return o;
	}
	/**
	 * 更新本次记录的电子签名的单据PK
	 * @param ae
	 */
	public void updatePk(ActionEvent ae) {
		AbstractUIAppModel amodel = null;
		Method[] ms = this.getClass().getMethods();
		for (Method m : ms) {
			Class<?> clazz = m.getReturnType();
			if (AbstractUIAppModel.class.isAssignableFrom(clazz)) {
				try {
					AbstractUIAppModel model = (AbstractUIAppModel) m.invoke(
							this, null);
					if (model == null)
						return;
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

		if (amodel == null || ae == null || StringUtil.isEmpty(ae.getActionCommand()))
			return;

		Object o = amodel.getSelectedData();
		String billid = null;
		String def1 = null;
		SuperVO vo1 = null;
		if (o instanceof AggregatedValueObject) {
			AggregatedValueObject aggvo = (AggregatedValueObject) o;
			try {
				billid = aggvo.getParentVO().getPrimaryKey();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		} else if (o instanceof SuperVO) {
			vo1 = (SuperVO) o;
			if (vo1 instanceof SupFinanceVO) {
				billid = ((SupFinanceVO) vo1).getPk_supplier();
			} else if (vo1 instanceof SupStockVO) {
				billid = ((SupStockVO) vo1).getPk_supplier();
			} else if (vo1 instanceof MaterialFiVO) {
				billid = ((MaterialFiVO) vo1).getPk_material();
				def1 = ((MaterialFiVO) vo1).getPk_materialfi();
			} else if (vo1 instanceof MaterialPuVO) {
				billid = ((MaterialPuVO) vo1).getPk_material();
				def1 = ((MaterialPuVO) vo1).getPk_materialpu();
			} else if (vo1 instanceof MaterialSaleVO) {
				billid = ((MaterialSaleVO) vo1).getPk_material();
				def1 = ((MaterialSaleVO) vo1).getPk_materialsale();
			} else if (vo1 instanceof MaterialStockVO) {
				billid = ((MaterialStockVO) vo1).getPk_material();
				def1 = ((MaterialStockVO) vo1).getPk_materialstock();
			} else if (vo1 instanceof MaterialPlanVO) {
				billid = ((MaterialPlanVO) vo1).getPk_material();
				def1 = ((MaterialPlanVO) vo1).getPk_materialplan();
			} else if (vo1 instanceof MaterialProdVO) {
				billid = ((MaterialProdVO) vo1).getPk_material();
				def1 = ((MaterialProdVO) vo1).getPk_materialprod();
			} else if (vo1 instanceof MaterialCostVO) {
				billid = ((MaterialCostVO) vo1).getPk_material();
				def1 = ((MaterialCostVO) vo1).getPk_materialcost();
			} else if (vo1 instanceof MaterialPFCCVO) {
				billid = ((MaterialPFCCVO) vo1).getPk_material();
				def1 = ((MaterialPFCCVO) vo1).getPk_mateprofcost();
			} else if (vo1 instanceof MaterialPfcVO) {
				billid = ((MaterialPfcVO) vo1).getPk_material();
				def1 = ((MaterialPfcVO) vo1).getPk_materialpfc();
			} else {
				billid = vo1.getPrimaryKey();
			}
		}
		updatebillId(ae.getActionCommand(), billid, def1);
	}

	// 生成日志文件
	private void updateLog(SuperVO oldVO) {
		try {
			if (!StringUtil.isEmpty(getPk()) && oldVO != null) {

				BusiLogSave save = new BusiLogSave();
				SuperVO vo1 = HYPubBO_Client.queryByPrimaryKey(
						oldVO.getClass(), oldVO.getPrimaryKey());
				List<BusinessLogESVO> list = save.writeModefyBusiLog("", vo1,
						oldVO);

				for (BusinessLogESVO svo : list) {
					svo.setStatus(VOStatus.NEW);
					svo.setPk_hises(getPk());
					svo.setTablename("sm_busilog_default_es");
				}
				HYPubBO_Client.insertAry(list.toArray(new BusinessLogESVO[list
						.size()]));
			}
		} catch (UifException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void updatebillId(String cmd, String billid) {
		updatebillId(cmd, billid, null);
	}

	public void updatebillId(String cmd, String billid, String def1) {
		ElectronicsignatureHisVO vo = null;
		try {
			// if(!cmd.contains("保存")){
			// return;
			// }
			if (!StringUtil.isEmpty(getPk())) {
				vo = (ElectronicsignatureHisVO) HYPubBO_Client
						.queryByPrimaryKey(ElectronicsignatureHisVO.class,
								getPk());
				if (vo != null && !StringUtil.isEmpty(billid)) {
					vo.setBillid(billid);
				}
				if (!StringUtil.isEmpty(def1)) {
					vo.setDef1(def1);
				}
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

	public boolean checkElectronicSignature(LoginContext context, String cmd,
			AbstractUIAppModel model) {
		if (context == null)
			return true;
		String nodecode = context.getNodeCode();
		Log.getInstance(this.getClass()).info("功能节点号：" + nodecode);
		//40081009 库存状态调整 
		if("40081009".equals(context.getNodeCode())){
			return true;
		}
		List<ElectronicSignatureVO> list = getListElect();
		if (list == null || list.size() == 0)
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
			Object o = model.getSelectedData();
			String billid = null;
			SuperVO vo1 = null;
			if (o instanceof AggregatedValueObject) {
				AggregatedValueObject aggvo = (AggregatedValueObject) o;
				billid = aggvo.getParentVO().getPrimaryKey();
			} else if (o instanceof SuperVO) {
				vo1 = (SuperVO) o;
				billid = vo1.getPrimaryKey();
			}
			dialog = new AccreditLoginDialog(context.getEntranceUI(), context,
					cmd, billid,true);

			UserVO vo = (UserVO) HYPubBO_Client.queryByPrimaryKey(UserVO.class,
					context.getPk_loginUser());
			dialog.getTfUser().setText(vo.getUser_code());
			dialog.getAction().setText(cmd);
			if (dialog.showModal() == UIDialog.ID_OK) {
				//设置电子签名日志的pk
				setPk(dialog.getPk());
				this.signVnote = dialog.getVmemo().getText();
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
			list1 = (List<ElectronicSignatureVO>) service.executeQuery(strb
					.toString(), new BeanListProcessor(
					ElectronicSignatureVO.class));
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


	public String getSignVnote() {
		return signVnote;
	}

	public void setSignVnote(String signVnote) {
		this.signVnote = signVnote;
	}

}
