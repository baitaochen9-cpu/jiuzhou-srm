/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ package nc.ui.ewm.workorder.action;
/*     */ 
/*     */ import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import nc.ui.am.action.info.ActionInfoInitalizer;
import nc.ui.am.action.taskbatch.DataProcess;
import nc.ui.am.action.taskbatch.NetFlowMode;
import nc.ui.am.action.taskself.IBillOperateService;
import nc.ui.am.action.taskself.TaskSelfAction;
import nc.ui.am.model.BillManageModel;
import nc.ui.ewm.workorder.dlg.WoStatusDialog;
import nc.ui.ewm.workorder.model.UpdateStatusService;
import nc.ui.ewm.workorder.view.WorkOrderViewUtil;
import nc.ui.uif2.UIState;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WOHisVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

import nc.ui.pub.beans.MessageDialog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AlterStatusAction extends TaskSelfAction
/*     */ {
/*     */   public AlterStatusAction()
/*     */   {
/*  33 */     ActionInfoInitalizer.initializeAction(this, "AlterStatus");
/*     */   }
/*     */ 
/*     */   public void doAction(ActionEvent e)
/*     */     throws Exception
/*     */   {
/*  39 */     BillManageModel billManageModel = getModel();
/*  40 */     if (billManageModel == null)
/*  41 */       return;
/*  42 */     AggWorkOrderVO[] billVOs = (AggWorkOrderVO[])(AggWorkOrderVO[])DataProcess.getSelectedViewData(billManageModel);
/*     */ 
/*  44 */     if (ArrayUtils.isEmpty(billVOs)) {
/*  45 */       return;
/*     */     }
/*     */ 
/*  48 */     if (!(isSameStatusType(billVOs))) {
/*  49 */       showErrorMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0077"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */       return;
/*     */     }
/*     */ 
/*  57 */     validate(billVOs);
/*     */ 
/*     */ 
/*  60 */     AggWorkOrderVO billVO = (AggWorkOrderVO)billManageModel.getSelectedData();
/*     */ 
/*  62 */     String currStatusTypeid = billVO.getParentVO().getPk_wo_status();
/*     */ 
/*     */ 
/*  65 */     Integer[] statustype = getUsableStatusType(billVO);
/*     */ 
/*  67 */     WoStatusDialog dlg = new WoStatusDialog(getModel().getContext().getEntranceUI(), statustype, currStatusTypeid, getModel().getContext());
/*     */ 
/*     */ 		
/*  70 */     if (1 == dlg.showModal()) {
/*  71 */       Map m_para = dlg.getRetMap();
/*  72 */       WOHisVO paraVO = new WOHisVO();
/*  73 */       paraVO.setPk_wostatus((String)m_para.get("Pk_wo_status"));
/*  74 */       paraVO.setMemo((String)m_para.get("Memo"));
/*  75 */       paraVO.setWo_statustype(new Integer((String)m_para.get("Status_type")));
/*     */ 		
				/*********************************20230802 bbt ***********/
				String currOrg = billVO.getParentVO().getPk_org();	
				Map<String,Boolean> showWindow = new HashMap<>();
				
				//苏州工厂要求在工单状态从“进行中”被调整时进行校验
				//表头报告页签的实际结束时间为空，则弹窗提示“存在未出库的计划物料”，message非error
//				if (currOrg.equals("0001V11000000000374G") 
//						&& billVO.getParent().getAttributeValue("pk_wo_status").equals("0001V110000000002YN9")){
//					//actu_end_time  实际结束时间 
//						
//					if(billVO.getParent().getAttributeValue("actu_end_time") != null 
//							|| billVO.getParent().getAttributeValue("actu_end_time") != ""){
//						//String actulEndTime = billVO.getParent().getAttributeValue("actu_end_time").toString();
//						showWindow.put("2YN9", true);	
//					}
//				}
				
				//苏州工厂要求在工单状态被调整为“完成”时进行校验
				//有计划物料但其对应的实际物料无记录，则弹窗提示“存在未出库的计划物料”，message非error						
				if(currOrg.equals("0001V11000000000374G") 
						&& paraVO.getPk_wostatus().equals("0001V110000000002YNA")){
					SuperVO[] sv_Plan = billVO.getChildrenVO();
					SuperVO[] sv_Actul = billVO.getChildrenVO("WOActualInvVO");
					//计划物料为空，无法通过比对判断，故直接退出
					if(sv_Plan == null){
						
					}
					//计划物料不为空
					else if(sv_Plan != null){
						//实际物料为空，直接触发弹窗退出
						if(sv_Actul == null){
							showWindow.put("2YNA", true);
						}
						//实际物料非空，分别求实际物料的各物料出库数量和，与计划物料数量做比对
						else if(sv_Actul != null){								
							//得到计划物料数组后，将物料及主数量的信息组装到map中，再做相同物料的数量合并
							Map<String,UFDouble> map = new HashMap<>();
							for(SuperVO item:sv_Plan){
								String plan_materil = item.getAttributeValue("pk_material_v").toString();
								UFDouble plan_nnum = new UFDouble(item.getAttributeValue("nnum").toString());
								//判断键是否已存在
								if(!map.containsKey(plan_materil)){
									map.put(plan_materil, plan_nnum);
								}
								else if(map.containsKey(plan_materil)){
									UFDouble new_plan_nnum = map.get(plan_materil).add(plan_nnum);
									map.remove(plan_materil);
									map.put(plan_materil, new_plan_nnum);
								}	
							}							
							for(String key : map.keySet()){
								double sumThisMater = 0.0;
								for(int j = 0 ; j < sv_Actul.length;j++){
									//通过map中存的第一个元素比对实际物料的各行
									if(key.equals(sv_Actul[j].getAttributeValue("pk_material_v"))){									
										UFDouble ufd1 = new UFDouble(sv_Actul[j].getAttributeValue("nnum").toString());
										sumThisMater += ufd1.toDouble();
									}
								}
								//此条计划物料是否完全出库
								if(map.get(key).toDouble() == sumThisMater){
									
								}
								else{
									showWindow.put("2YNA", true);
									break;
								}													
							}							
						}
					}						
				}
				//弹窗信息统一处理
				if(showWindow.size() > 1){
					MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),"请注意", "请在确认工单完成后及时将工单状态更新为完成；\n且存在未出库的计划物料");
				}
				else if(showWindow.size() == 1){
					if(showWindow.get("2YNA") != null){
						MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),"请注意", "存在未出库的计划物料"); 
					}
					else if(showWindow.get("2YN9") != null){
						MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),"请注意", "请在确认工单完成后及时将工单状态更新为完成");
					}
				}				
				/********************************************************/

/*  77 */       setUserObject(paraVO);
/*     */ 
/*     */ 
/*  80 */       Object retObj = procOperation(billVOs, e);
/*     */ 
/*  82 */       getModel().directlyUpdate(retObj);
/*     */ 
/*  84 */       getModel().toDataStatus();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AggregatedValueObject[] filterBillVOByValidate(AggregatedValueObject[] billVOs, ActionEvent e)
/*     */   {
/*  92 */     return billVOs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer[] getUsableStatusType(AggWorkOrderVO AggBillVo)
/*     */   {
/* 103 */     Integer statusType = AggBillVo.getParentVO().getWo_statustype();
/* 104 */     Integer[] statusTypes = WorkOrderViewUtil.getInstance().getStatusTypeTransTO(statusType);
/*     */ 
/* 106 */     Integer[] newStatusTypes = new Integer[statusTypes.length + 1];
/* 107 */     System.arraycopy(statusTypes, 0, newStatusTypes, 0, statusTypes.length);
/* 108 */     newStatusTypes[statusTypes.length] = statusType;
/* 109 */     return newStatusTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isSameStatusType(AggWorkOrderVO[] billVOs)
/*     */   {
/* 123 */     Integer statusType = billVOs[0].getParentVO().getWo_statustype();
/* 124 */     for (int i = 1; i < billVOs.length; ++i) {
/* 125 */       Integer statusType2 = billVOs[i].getParentVO().getWo_statustype();
/* 126 */       if (!(statusType.equals(statusType2))) {
/* 127 */         return false;
/*     */       }
/*     */     }
/* 130 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   protected boolean isActionEnable()
/*     */   {
/* 136 */     return ((getModel().getUiState() == UIState.NOT_EDIT) && (getModel().getSelectedData() != null));
/*     */   }
/*     */ 
/*     */ 
/*     */   public IBillOperateService getBillOperateService()
/*     */   {
/* 142 */     return new UpdateStatusService();
/*     */   }
/*     */ 
/*     */   protected NetFlowMode getNetFlowMode()
/*     */   {
/* 147 */     return NetFlowMode.NOTREAT;
/*     */   }
/*     */ 
/*     */   public String getActionCode()
/*     */   {
/* 152 */     return null;
/*     */   }
/*     */ }
