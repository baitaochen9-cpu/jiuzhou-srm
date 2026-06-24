/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*      */package nc.vo.ewm.workorder;
/*      */
/*      */import nc.vo.am.common.AMSuperVO;
/*      */
import nc.vo.am.constant.IWorkOrderFields;
/*      */
import nc.vo.pub.lang.UFBoolean;
/*      */
import nc.vo.pub.lang.UFDate;
/*      */
import nc.vo.pub.lang.UFDateTime;
/*      */
import nc.vo.pub.lang.UFDouble;
/*      */
/*      */public class WorkOrderHeadVO extends AMSuperVO
/*      */implements IWorkOrderFields
/*      */{
	/*      */public static final String PLAN_S_MATERIAL_MNY = "plan_s_material_mny";
	/*      */public static final String PLAN_S_LABOUR_MNY = "plan_s_labour_mny";
	/*      */public static final String PLAN_S_TOOL_MNY = "plan_s_tool_mny";
	/*      */public static final String PLAN_S_OTHER_MNY = "plan_s_other_mny";
	/*      */public static final String PLAN_S_TOTAL_MNY = "plan_s_total_mny";
	/*      */public static final String ACTUAL_S_MATERIAL_MNY = "actual_s_material_mny";
	/*      */public static final String ACTUAL_S_LABOUR_MNY = "actual_s_labour_mny";
	/*      */public static final String ACTUAL_S_TOOL_MNY = "actual_s_tool_mny";
	/*      */public static final String ACTUAL_S_OTHER_MNY = "actual_s_other_mny";
	/*      */public static final String ACTUAL_S_TOTAL_MNY = "actual_s_total_mny";
	/*      */public static final String CHECK_PLAN_COST = "check_plan_cost";
	/*      */private transient String pk_wo;
	/*      */private transient String busi_type;
	/*      */private transient String bill_type;
	/*      */private transient String transi_type;
	/*      */private transient String pk_transitype;
	/*      */private transient Integer bill_status;
	/*      */private transient String pk_group;
	/*      */private transient String pk_org;
	/*      */private transient String bill_code;
	/*      */private transient String wo_content;
	/*      */private transient Integer wo_statustype;
	/*      */private transient String pk_wo_status;
	/*      */private transient UFDateTime status_time;
	/*      */private transient UFBoolean status_follow;
	/*      */private transient String pk_worktype;
	/*      */private transient String pk_equip;
	/*      */private transient String pk_location;
	/*      */private transient String pk_equip_capital;
	/*      */private transient String pk_ownerorg;
	/*      */private transient String pk_mandept;
	/*      */private transient String pk_manager;
	/*      */private transient String pk_usedunit;
	/*      */private transient String pk_usedept;
	/*      */private transient String pk_usedorg;
	/*      */private transient String pk_user;
	/*   90 */private transient UFBoolean warrant_flag = UFBoolean.FALSE;
	/*      */private transient String pk_warcontract;
	/*      */private transient String pk_warcontract_b;
	/*      */private transient String pk_parent_wo;
	/*      */private transient String pk_failure_type;
	/*      */private transient String pk_failure_symptom;
	/*      */private transient String pk_failure_detect;
	/*      */private transient String failure_detail;
	/*      */private transient UFDateTime failure_time;
	/*      */private transient String pk_reportedby;
	/*      */private transient UFDateTime report_time;
	/*      */private transient String pk_executor;
	/*      */private transient String pk_director;
	/*      */private transient String pk_psn_group;
	/*      */private transient String pk_wo_dept;
	/*      */private transient String pk_wo_dept_v;
	/*      */private transient String pk_specialty;
	/*      */private transient String pk_workcenter;
	/*      */private transient String pk_priority;
	/*      */private transient String pri_clause;
	/*      */private transient String pk_repair_plan_b;
	/*      */private transient String pk_repair_plan;
	/*      */private transient UFDateTime targ_start_time;
	/*      */private transient UFDateTime targ_end_time;
	/*      */private transient UFDateTime plan_start_time;
	/*      */private transient UFDateTime plan_end_time;
	/*      */private transient UFDateTime actu_start_time;
	/*      */private transient UFDateTime actu_end_time;
	/*      */private transient UFDateTime plan_sthalt_time;
	/*      */private transient UFDateTime plan_endhalt_time;
	/*      */private transient UFDateTime actu_sthalt_time;
	/*      */private transient UFDateTime actu_endhalt_time;
	/*      */private transient String workload;
	/*      */private transient UFBoolean haslatter_flag;
	/*      */private transient String pk_std_job;
	/*      */private transient String pk_safety_job;
	/*      */private transient UFBoolean requirepermit;
	/*      */private transient String pk_inspection_road;
	/*      */private transient UFBoolean consign_flag;
	/*      */private transient String consign_reason;
	/*      */private transient String pk_provider;
	/*      */private transient String pk_mtcon;
	/*      */private transient String pk_project;
	/*      */private transient String pk_projecttask;
	/*      */private transient String failure_influence;
	/*      */private transient String pk_failure_reason;
	/*      */private transient String failure_reason_name;
	/*      */private transient String pk_service_step;
	/*      */private transient String failure_reason_desc;
	/*      */private transient String service_step_desc;
	/*      */private transient String task_progress;
	/*      */private transient String task_summary;
	/*      */private transient String left_problem;
	/*      */private transient String innercode;
	/*      */private transient String src_bill_type;
	/*      */private transient String src_transi_type;
	/*      */private transient String src_pk_transitype;
	/*      */private transient String src_rowno;
	/*      */private transient String src_pk_bill;
	/*      */private transient String src_bill_code;
	/*      */private transient String src_pk_bill_b;
	/*      */private transient UFDateTime src_head_ts;
	/*      */private transient UFDateTime src_body_ts;
	/*      */private transient String memo;
	/*      */private transient String creator;
	/*      */private transient UFDateTime creationtime;
	/*      */private transient String billmaker;
	/*      */private transient UFDateTime billmaketime;
	/*      */private transient String auditor;
	/*      */private transient UFDateTime audittime;
	/*      */private transient String check_opinion;
	/*      */private transient String modifier;
	/*      */private transient UFDateTime modifiedtime;
	/*      */private transient String def1;
	/*      */private transient String def2;
	/*      */private transient String def3;
	/*      */private transient String def4;
	/*      */private transient String def5;
	/*      */private transient String def6;
	/*      */private transient String def7;
	/*      */private transient String def8;
	/*      */private transient String def9;
	/*      */private transient String def10;
	/*      */private transient String def11;
	/*      */private transient String def12;
	/*      */private transient String def13;
	/*      */private transient String def14;
	/*      */private transient String def15;
	/*      */private transient String def16;
	/*      */private transient String def17;
	/*      */private transient String def18;
	/*      */private transient String def19;
	/*      */private transient String def20;
	/*      */private transient String def21;
	/*      */private transient String def22;
	/*      */private transient String def23;
	/*      */private transient String def24;
	/*      */private transient String def25;
	/*      */private transient String def26;
	/*      */private transient String def27;
	/*      */private transient String def28;
	/*      */private transient String def29;
	/*      */private transient String def30;
	/*      */private transient UFBoolean gen_failure_flag;
	/*      */private transient UFBoolean failure_flag;
	/*      */private transient UFBoolean haschild_flag;
	/*  273 */private transient UFDouble pl_mtr_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  275 */private transient UFDouble pl_lbr_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  277 */private transient UFDouble pl_tol_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  279 */private transient UFDouble pl_oth_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  281 */private transient UFDouble pl_ttl_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  283 */private transient UFDouble ac_mtr_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  285 */private transient UFDouble ac_lbr_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  287 */private transient UFDouble ac_tol_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  289 */private transient UFDouble ac_oth_mny_org = UFDouble.ZERO_DBL;
	/*      */
	/*  291 */private transient UFDouble ac_ttl_mny_org = UFDouble.ZERO_DBL;
	/*      */private transient UFDate capitalize_date;
	/*      */private transient String pk_ancestor_wo;
	/*      */private transient String pk_currtype;
	/*      */private transient String pk_org_v;
	/*      */private transient String pk_fiorg_ap;
	/*      */private transient String pk_fiorg_ap_v;
	/*      */private transient String pk_profitcen_ap;
	/*      */private transient String pk_profitcen_ap_v;
	/*      */private transient String pk_chkfactor;
	/*      */private transient String pk_fiorg_armt;
	/*      */private transient String pk_fiorg_armt_v;
	/*      */private transient String pk_profitcen_armt;
	/*      */private transient String pk_profitcen_armt_v;
	/*      */private transient String pk_chkfactor_armt;
	/*      */private transient String pk_fiorg_apmt;
	/*      */private transient String pk_fiorg_apmt_v;
	/*      */private transient String pk_profitcen_apmt;
	/*      */private transient String pk_profitcen_apmt_v;
	/*      */private transient String pk_chkfactor_apmt;
	/*      */private transient String pk_fiorg_ic;
	/*      */private transient String pk_fiorg_ic_v;
	/*      */private transient String opinion;
	/*  337 */private transient UFDouble pl_mtr_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  339 */private transient UFDouble pl_lbr_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  341 */private transient UFDouble pl_tol_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  343 */private transient UFDouble pl_oth_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  345 */private transient UFDouble pl_ttl_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  347 */private transient UFDouble ac_mtr_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  349 */private transient UFDouble ac_lbr_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  351 */private transient UFDouble ac_tol_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  353 */private transient UFDouble ac_oth_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  355 */private transient UFDouble ac_ttl_mny_group = UFDouble.ZERO_DBL;
	/*      */
	/*  357 */private transient UFDouble pl_mtr_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  359 */private transient UFDouble pl_lbr_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  361 */private transient UFDouble pl_tol_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  363 */private transient UFDouble pl_oth_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  365 */private transient UFDouble pl_ttl_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  367 */private transient UFDouble ac_mtr_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  369 */private transient UFDouble ac_lbr_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  371 */private transient UFDouble ac_tol_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  373 */private transient UFDouble ac_oth_mny_global = UFDouble.ZERO_DBL;
	/*      */
	/*  375 */private transient UFDouble ac_ttl_mny_global = UFDouble.ZERO_DBL;
	/*      */private transient String pk_customer;
	/*      */private transient String pk_supplier;
	/*      */private transient UFBoolean receivable_flag;
	/*      */private transient UFBoolean payable_flag;
	/*      */private transient WOChildrenVO[] wo_children;
	/*  387 */private transient Integer dr = Integer.valueOf(0);
	/*      */private transient UFDateTime ts;
	/*      */private transient UFBoolean payout_flag;
	/*      */private transient String pk_fiorg;
	/*      */private transient String coordinate;
	/*      */private transient String coordinate_desc;
	/*      */public static final String DEF1 = "def1";
	/*      */public static final String DEF2 = "def2";
	/*      */public static final String DEF3 = "def3";
	/*      */public static final String DEF4 = "def4";
	/*      */public static final String DEF5 = "def5";
	/*      */public static final String DEF6 = "def6";
	/*      */public static final String DEF7 = "def7";
	/*      */public static final String DEF8 = "def8";
	/*      */public static final String DEF9 = "def9";
	/*      */public static final String DEF10 = "def10";
	/*      */public static final String DEF11 = "def11";
	/*      */public static final String DEF12 = "def12";
	/*      */public static final String DEF13 = "def13";
	/*      */public static final String DEF14 = "def14";
	/*      */public static final String DEF15 = "def15";
	/*      */public static final String DEF16 = "def16";
	/*      */public static final String DEF17 = "def17";
	/*      */public static final String DEF18 = "def18";
	/*      */public static final String DEF19 = "def19";
	/*      */public static final String DEF20 = "def20";
	/*      */public static final String DEF21 = "def21";
	/*      */public static final String DEF22 = "def22";
	/*      */public static final String DEF23 = "def23";
	/*      */public static final String DEF24 = "def24";
	/*      */public static final String DEF25 = "def25";
	/*      */public static final String DEF26 = "def26";
	/*      */public static final String DEF27 = "def27";
	/*      */public static final String DEF28 = "def28";
	/*      */public static final String DEF29 = "def29";
	/*      */public static final String DEF30 = "def30";
	/*  431 */private UFBoolean bICRETBill = null;
	/*      */
	/*      */public String getPk_wo()
	/*      */{
		/*  439 */return this.pk_wo;
		/*      */}
	/*      */
	/*      */public void setPk_wo(String newPk_wo)
	/*      */{
		/*  447 */this.pk_wo = newPk_wo;
		/*      */}
	/*      */
	/*      */public String getBusi_type()
	/*      */{
		/*  455 */return this.busi_type;
		/*      */}
	/*      */
	/*      */public void setBusi_type(String newBusi_type)
	/*      */{
		/*  463 */this.busi_type = newBusi_type;
		/*      */}
	/*      */
	/*      */public String getBill_type()
	/*      */{
		/*  471 */return this.bill_type;
		/*      */}
	/*      */
	/*      */public void setBill_type(String newBill_type)
	/*      */{
		/*  479 */this.bill_type = newBill_type;
		/*      */}
	/*      */
	/*      */public String getTransi_type()
	/*      */{
		/*  487 */return this.transi_type;
		/*      */}
	/*      */
	/*      */public void setTransi_type(String newTransi_type)
	/*      */{
		/*  495 */this.transi_type = newTransi_type;
		/*      */}
	/*      */
	/*      */public Integer getBill_status()
	/*      */{
		/*  503 */return this.bill_status;
		/*      */}
	/*      */
	/*      */public void setBill_status(Integer newBill_status)
	/*      */{
		/*  511 */this.bill_status = newBill_status;
		/*      */}
	/*      */
	/*      */public String getPk_group()
	/*      */{
		/*  519 */return this.pk_group;
		/*      */}
	/*      */
	/*      */public void setPk_group(String newPk_group)
	/*      */{
		/*  527 */this.pk_group = newPk_group;
		/*      */}
	/*      */
	/*      */public String getPk_org()
	/*      */{
		/*  535 */return this.pk_org;
		/*      */}
	/*      */
	/*      */public void setPk_org(String newPk_org)
	/*      */{
		/*  543 */this.pk_org = newPk_org;
		/*      */}
	/*      */
	/*      */public String getBill_code()
	/*      */{
		/*  551 */return this.bill_code;
		/*      */}
	/*      */
	/*      */public void setBill_code(String newBill_code)
	/*      */{
		/*  559 */this.bill_code = newBill_code;
		/*      */}
	/*      */
	/*      */public String getWo_content()
	/*      */{
		/*  567 */return this.wo_content;
		/*      */}
	/*      */
	/*      */public void setWo_content(String newWo_content)
	/*      */{
		/*  575 */this.wo_content = newWo_content;
		/*      */}
	/*      */
	/*      */public Integer getWo_statustype()
	/*      */{
		/*  583 */return this.wo_statustype;
		/*      */}
	/*      */
	/*      */public void setWo_statustype(Integer newWo_statustype)
	/*      */{
		/*  591 */this.wo_statustype = newWo_statustype;
		/*      */}
	/*      */
	/*      */public String getPk_wo_status()
	/*      */{
		/*  599 */return this.pk_wo_status;
		/*      */}
	/*      */
	/*      */public void setPk_wo_status(String newPk_wo_status)
	/*      */{
		/*  607 */this.pk_wo_status = newPk_wo_status;
		/*      */}
	/*      */
	/*      */public UFDateTime getStatus_time()
	/*      */{
		/*  615 */return this.status_time;
		/*      */}
	/*      */
	/*      */public void setStatus_time(UFDateTime newStatus_time)
	/*      */{
		/*  623 */this.status_time = newStatus_time;
		/*      */}
	/*      */
	/*      */public String getPk_worktype()
	/*      */{
		/*  631 */return this.pk_worktype;
		/*      */}
	/*      */
	/*      */public void setPk_worktype(String newPk_worktype)
	/*      */{
		/*  639 */this.pk_worktype = newPk_worktype;
		/*      */}
	/*      */
	/*      */public String getPk_equip()
	/*      */{
		/*  647 */return this.pk_equip;
		/*      */}
	/*      */
	/*      */public void setPk_equip(String newPk_equip)
	/*      */{
		/*  655 */this.pk_equip = newPk_equip;
		/*      */}
	/*      */
	/*      */public String getPk_location()
	/*      */{
		/*  663 */return this.pk_location;
		/*      */}
	/*      */
	/*      */public void setPk_location(String newPk_location)
	/*      */{
		/*  671 */this.pk_location = newPk_location;
		/*      */}
	/*      */
	/*      */public String getPk_ownerorg()
	/*      */{
		/*  679 */return this.pk_ownerorg;
		/*      */}
	/*      */
	/*      */public void setPk_ownerorg(String newPk_ownerorg)
	/*      */{
		/*  687 */this.pk_ownerorg = newPk_ownerorg;
		/*      */}
	/*      */
	/*      */public String getPk_mandept()
	/*      */{
		/*  695 */return this.pk_mandept;
		/*      */}
	/*      */
	/*      */public void setPk_mandept(String newPk_mandept)
	/*      */{
		/*  703 */this.pk_mandept = newPk_mandept;
		/*      */}
	/*      */
	/*      */public String getPk_manager()
	/*      */{
		/*  711 */return this.pk_manager;
		/*      */}
	/*      */
	/*      */public void setPk_manager(String newPk_manager)
	/*      */{
		/*  719 */this.pk_manager = newPk_manager;
		/*      */}
	/*      */
	/*      */public String getPk_usedunit()
	/*      */{
		/*  727 */return this.pk_usedunit;
		/*      */}
	/*      */
	/*      */public void setPk_usedunit(String newPk_usedunit)
	/*      */{
		/*  735 */this.pk_usedunit = newPk_usedunit;
		/*      */}
	/*      */
	/*      */public String getPk_usedept()
	/*      */{
		/*  743 */return this.pk_usedept;
		/*      */}
	/*      */
	/*      */public void setPk_usedept(String newPk_usedept)
	/*      */{
		/*  751 */this.pk_usedept = newPk_usedept;
		/*      */}
	/*      */
	/*      */public String getPk_usedorg()
	/*      */{
		/*  759 */return this.pk_usedorg;
		/*      */}
	/*      */
	/*      */public void setPk_usedorg(String newPk_usedorg)
	/*      */{
		/*  767 */this.pk_usedorg = newPk_usedorg;
		/*      */}
	/*      */
	/*      */public String getPk_user()
	/*      */{
		/*  775 */return this.pk_user;
		/*      */}
	/*      */
	/*      */public void setPk_user(String newPk_user)
	/*      */{
		/*  783 */this.pk_user = newPk_user;
		/*      */}
	/*      */
	/*      */public String getPk_parent_wo()
	/*      */{
		/*  791 */return this.pk_parent_wo;
		/*      */}
	/*      */
	/*      */public void setPk_parent_wo(String newPk_parent_wo)
	/*      */{
		/*  799 */this.pk_parent_wo = newPk_parent_wo;
		/*      */}
	/*      */
	/*      */public String getPk_failure_type()
	/*      */{
		/*  807 */return this.pk_failure_type;
		/*      */}
	/*      */
	/*      */public void setPk_failure_type(String newPk_failure_type)
	/*      */{
		/*  815 */this.pk_failure_type = newPk_failure_type;
		/*      */}
	/*      */
	/*      */public String getPk_failure_symptom()
	/*      */{
		/*  823 */return this.pk_failure_symptom;
		/*      */}
	/*      */
	/*      */public void setPk_failure_symptom(String newPk_failure_symptom)
	/*      */{
		/*  831 */this.pk_failure_symptom = newPk_failure_symptom;
		/*      */}
	/*      */
	/*      */public String getPk_failure_detect()
	/*      */{
		/*  839 */return this.pk_failure_detect;
		/*      */}
	/*      */
	/*      */public void setPk_failure_detect(String newPk_failure_detect)
	/*      */{
		/*  847 */this.pk_failure_detect = newPk_failure_detect;
		/*      */}
	/*      */
	/*      */public String getFailure_detail()
	/*      */{
		/*  855 */return this.failure_detail;
		/*      */}
	/*      */
	/*      */public void setFailure_detail(String newFailure_detail)
	/*      */{
		/*  863 */this.failure_detail = newFailure_detail;
		/*      */}
	/*      */
	/*      */public UFDateTime getFailure_time()
	/*      */{
		/*  871 */return this.failure_time;
		/*      */}
	/*      */
	/*      */public void setFailure_time(UFDateTime newFailure_time)
	/*      */{
		/*  879 */this.failure_time = newFailure_time;
		/*      */}
	/*      */
	/*      */public String getPk_reportedby()
	/*      */{
		/*  887 */return this.pk_reportedby;
		/*      */}
	/*      */
	/*      */public void setPk_reportedby(String newPk_reportedby)
	/*      */{
		/*  895 */this.pk_reportedby = newPk_reportedby;
		/*      */}
	/*      */
	/*      */public UFDateTime getReport_time()
	/*      */{
		/*  903 */return this.report_time;
		/*      */}
	/*      */
	/*      */public void setReport_time(UFDateTime newReport_time)
	/*      */{
		/*  911 */this.report_time = newReport_time;
		/*      */}
	/*      */
	/*      */public String getPk_executor()
	/*      */{
		/*  919 */return this.pk_executor;
		/*      */}
	/*      */
	/*      */public void setPk_executor(String newPk_executor)
	/*      */{
		/*  927 */this.pk_executor = newPk_executor;
		/*      */}
	/*      */
	/*      */public String getPk_director()
	/*      */{
		/*  935 */return this.pk_director;
		/*      */}
	/*      */
	/*      */public void setPk_director(String newPk_director)
	/*      */{
		/*  943 */this.pk_director = newPk_director;
		/*      */}
	/*      */
	/*      */public String getPk_psn_group()
	/*      */{
		/*  951 */return this.pk_psn_group;
		/*      */}
	/*      */
	/*      */public void setPk_psn_group(String newPk_psn_group)
	/*      */{
		/*  959 */this.pk_psn_group = newPk_psn_group;
		/*      */}
	/*      */
	/*      */public String getPk_wo_dept()
	/*      */{
		/*  967 */return this.pk_wo_dept;
		/*      */}
	/*      */
	/*      */public void setPk_wo_dept(String newPk_wo_dept)
	/*      */{
		/*  975 */this.pk_wo_dept = newPk_wo_dept;
		/*      */}
	/*      */
	/*      */public String getPk_specialty()
	/*      */{
		/*  983 */return this.pk_specialty;
		/*      */}
	/*      */
	/*      */public void setPk_specialty(String newPk_specialty)
	/*      */{
		/*  991 */this.pk_specialty = newPk_specialty;
		/*      */}
	/*      */
	/*      */public String getPk_workcenter()
	/*      */{
		/*  999 */return this.pk_workcenter;
		/*      */}
	/*      */
	/*      */public void setPk_workcenter(String newPk_workcenter)
	/*      */{
		/* 1007 */this.pk_workcenter = newPk_workcenter;
		/*      */}
	/*      */
	/*      */public String getPk_priority()
	/*      */{
		/* 1015 */return this.pk_priority;
		/*      */}
	/*      */
	/*      */public void setPk_priority(String newPk_priority)
	/*      */{
		/* 1023 */this.pk_priority = newPk_priority;
		/*      */}
	/*      */
	/*      */public String getPri_clause()
	/*      */{
		/* 1031 */return this.pri_clause;
		/*      */}
	/*      */
	/*      */public void setPri_clause(String newPri_clause)
	/*      */{
		/* 1039 */this.pri_clause = newPri_clause;
		/*      */}
	/*      */
	/*      */public String getPk_repair_plan_b()
	/*      */{
		/* 1047 */return this.pk_repair_plan_b;
		/*      */}
	/*      */
	/*      */public void setPk_repair_plan_b(String newPk_repair_plan_b)
	/*      */{
		/* 1055 */this.pk_repair_plan_b = newPk_repair_plan_b;
		/*      */}
	/*      */
	/*      */public String getPk_repair_plan()
	/*      */{
		/* 1063 */return this.pk_repair_plan;
		/*      */}
	/*      */
	/*      */public void setPk_repair_plan(String newPk_repair_plan)
	/*      */{
		/* 1071 */this.pk_repair_plan = newPk_repair_plan;
		/*      */}
	/*      */
	/*      */public UFDateTime getTarg_start_time()
	/*      */{
		/* 1079 */return this.targ_start_time;
		/*      */}
	/*      */
	/*      */public void setTarg_start_time(UFDateTime newTarg_start_time)
	/*      */{
		/* 1087 */this.targ_start_time = newTarg_start_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getTarg_end_time()
	/*      */{
		/* 1095 */return this.targ_end_time;
		/*      */}
	/*      */
	/*      */public void setTarg_end_time(UFDateTime newTarg_end_time)
	/*      */{
		/* 1103 */this.targ_end_time = newTarg_end_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getPlan_start_time()
	/*      */{
		/* 1111 */return this.plan_start_time;
		/*      */}
	/*      */
	/*      */public void setPlan_start_time(UFDateTime newPlan_start_time)
	/*      */{
		/* 1119 */this.plan_start_time = newPlan_start_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getPlan_end_time()
	/*      */{
		/* 1127 */return this.plan_end_time;
		/*      */}
	/*      */
	/*      */public void setPlan_end_time(UFDateTime newPlan_end_time)
	/*      */{
		/* 1135 */this.plan_end_time = newPlan_end_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getActu_start_time()
	/*      */{
		/* 1143 */return this.actu_start_time;
		/*      */}
	/*      */
	/*      */public void setActu_start_time(UFDateTime newActu_start_time)
	/*      */{
		/* 1151 */this.actu_start_time = newActu_start_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getActu_end_time()
	/*      */{
		/* 1159 */return this.actu_end_time;
		/*      */}
	/*      */
	/*      */public void setActu_end_time(UFDateTime newActu_end_time)
	/*      */{
		/* 1167 */this.actu_end_time = newActu_end_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getPlan_sthalt_time()
	/*      */{
		/* 1175 */return this.plan_sthalt_time;
		/*      */}
	/*      */
	/*      */public void setPlan_sthalt_time(UFDateTime newPlan_sthalt_time)
	/*      */{
		/* 1183 */this.plan_sthalt_time = newPlan_sthalt_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getPlan_endhalt_time()
	/*      */{
		/* 1191 */return this.plan_endhalt_time;
		/*      */}
	/*      */
	/*      */public void setPlan_endhalt_time(UFDateTime newPlan_endhalt_time)
	/*      */{
		/* 1199 */this.plan_endhalt_time = newPlan_endhalt_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getActu_sthalt_time()
	/*      */{
		/* 1207 */return this.actu_sthalt_time;
		/*      */}
	/*      */
	/*      */public void setActu_sthalt_time(UFDateTime newActu_sthalt_time)
	/*      */{
		/* 1215 */this.actu_sthalt_time = newActu_sthalt_time;
		/*      */}
	/*      */
	/*      */public UFDateTime getActu_endhalt_time()
	/*      */{
		/* 1223 */return this.actu_endhalt_time;
		/*      */}
	/*      */
	/*      */public void setActu_endhalt_time(UFDateTime newActu_endhalt_time)
	/*      */{
		/* 1231 */this.actu_endhalt_time = newActu_endhalt_time;
		/*      */}
	/*      */
	/*      */public String getWorkload()
	/*      */{
		/* 1239 */return this.workload;
		/*      */}
	/*      */
	/*      */public void setWorkload(String newWorkload)
	/*      */{
		/* 1247 */this.workload = newWorkload;
		/*      */}
	/*      */
	/*      */public UFBoolean getHaslatter_flag()
	/*      */{
		/* 1255 */return this.haslatter_flag;
		/*      */}
	/*      */
	/*      */public void setHaslatter_flag(UFBoolean newHaslatter_flag)
	/*      */{
		/* 1263 */this.haslatter_flag = newHaslatter_flag;
		/*      */}
	/*      */
	/*      */public String getPk_std_job()
	/*      */{
		/* 1271 */return this.pk_std_job;
		/*      */}
	/*      */
	/*      */public void setPk_std_job(String newPk_std_job)
	/*      */{
		/* 1279 */this.pk_std_job = newPk_std_job;
		/*      */}
	/*      */
	/*      */public UFBoolean getRequirePermit()
	/*      */{
		/* 1287 */return this.requirepermit;
		/*      */}
	/*      */
	/*      */public void setRequirePermit(UFBoolean newRequirePermit)
	/*      */{
		/* 1295 */this.requirepermit = newRequirePermit;
		/*      */}
	/*      */
	/*      */public String getPk_safety_job()
	/*      */{
		/* 1303 */return this.pk_safety_job;
		/*      */}
	/*      */
	/*      */public void setPk_safety_job(String newPk_safety_job)
	/*      */{
		/* 1311 */this.pk_safety_job = newPk_safety_job;
		/*      */}
	/*      */
	/*      */public String getPk_inspection_road()
	/*      */{
		/* 1320 */return this.pk_inspection_road;
		/*      */}
	/*      */
	/*      */public void setPk_inspection_road(String newPk_inspection_road)
	/*      */{
		/* 1328 */this.pk_inspection_road = newPk_inspection_road;
		/*      */}
	/*      */
	/*      */public UFBoolean getConsign_flag()
	/*      */{
		/* 1336 */return this.consign_flag == null ? UFBoolean.FALSE : this.consign_flag;
		/*      */}
	/*      */
	/*      */public void setConsign_flag(UFBoolean newConsign_flag)
	/*      */{
		/* 1344 */this.consign_flag = newConsign_flag;
		/*      */}
	/*      */
	/*      */public String getConsign_reason()
	/*      */{
		/* 1352 */return this.consign_reason;
		/*      */}
	/*      */
	/*      */public void setConsign_reason(String newConsign_reason)
	/*      */{
		/* 1360 */this.consign_reason = newConsign_reason;
		/*      */}
	/*      */
	/*      */public String getPk_provider()
	/*      */{
		/* 1368 */return this.pk_provider;
		/*      */}
	/*      */
	/*      */public void setPk_provider(String newPk_provider)
	/*      */{
		/* 1376 */this.pk_provider = newPk_provider;
		/*      */}
	/*      */
	/*      */public UFBoolean getWarrant_flag()
	/*      */{
		/* 1383 */return this.warrant_flag;
		/*      */}
	/*      */
	/*      */public void setWarrant_flag(UFBoolean warrant_flag)
	/*      */{
		/* 1389 */this.warrant_flag = warrant_flag;
		/*      */}
	/*      */
	/*      */public String getPk_warcontract()
	/*      */{
		/* 1395 */return this.pk_warcontract;
		/*      */}
	/*      */
	/*      */public void setPk_warcontract(String pk_warcontract)
	/*      */{
		/* 1401 */this.pk_warcontract = pk_warcontract;
		/*      */}
	/*      */
	/*      */public String getPk_warcontract_b()
	/*      */{
		/* 1407 */return this.pk_warcontract_b;
		/*      */}
	/*      */
	/*      */public void setPk_warcontract_b(String pk_warcontract_b)
	/*      */{
		/* 1413 */this.pk_warcontract_b = pk_warcontract_b;
		/*      */}
	/*      */
	/*      */public String getPk_mtcon()
	/*      */{
		/* 1419 */return this.pk_mtcon;
		/*      */}
	/*      */
	/*      */public void setPk_mtcon(String pk_mtcon)
	/*      */{
		/* 1425 */this.pk_mtcon = pk_mtcon;
		/*      */}
	/*      */
	/*      */public String getPk_project()
	/*      */{
		/* 1433 */return this.pk_project;
		/*      */}
	/*      */
	/*      */public void setPk_project(String newPk_project)
	/*      */{
		/* 1441 */this.pk_project = newPk_project;
		/*      */}
	/*      */
	/*      */public String getPk_projecttask()
	/*      */{
		/* 1449 */return this.pk_projecttask;
		/*      */}
	/*      */
	/*      */public void setPk_projecttask(String newPk_projecttask)
	/*      */{
		/* 1457 */this.pk_projecttask = newPk_projecttask;
		/*      */}
	/*      */
	/*      */public String getFailure_influence()
	/*      */{
		/* 1465 */return this.failure_influence;
		/*      */}
	/*      */
	/*      */public void setFailure_influence(String newFailure_influence)
	/*      */{
		/* 1473 */this.failure_influence = newFailure_influence;
		/*      */}
	/*      */
	/*      */public String getPk_failure_reason()
	/*      */{
		/* 1481 */return this.pk_failure_reason;
		/*      */}
	/*      */
	/*      */public void setPk_failure_reason(String newPk_failure_reason)
	/*      */{
		/* 1489 */this.pk_failure_reason = newPk_failure_reason;
		/*      */}
	/*      */
	/*      */public String getFailure_reason_name()
	/*      */{
		/* 1497 */return this.failure_reason_name;
		/*      */}
	/*      */
	/*      */public void setFailure_reason_name(String newFailure_reason_name)
	/*      */{
		/* 1505 */this.failure_reason_name = newFailure_reason_name;
		/*      */}
	/*      */
	/*      */public String getPk_service_step()
	/*      */{
		/* 1513 */return this.pk_service_step;
		/*      */}
	/*      */
	/*      */public void setPk_service_step(String newPk_service_step)
	/*      */{
		/* 1521 */this.pk_service_step = newPk_service_step;
		/*      */}
	/*      */
	/*      */public String getFailure_reason_desc()
	/*      */{
		/* 1529 */return this.failure_reason_desc;
		/*      */}
	/*      */
	/*      */public void setFailure_reason_desc(String newFailure_reason_desc)
	/*      */{
		/* 1537 */this.failure_reason_desc = newFailure_reason_desc;
		/*      */}
	/*      */
	/*      */public String getService_step_desc()
	/*      */{
		/* 1545 */return this.service_step_desc;
		/*      */}
	/*      */
	/*      */public void setService_step_desc(String newService_step_desc)
	/*      */{
		/* 1553 */this.service_step_desc = newService_step_desc;
		/*      */}
	/*      */
	/*      */public String getTask_progress()
	/*      */{
		/* 1561 */return this.task_progress;
		/*      */}
	/*      */
	/*      */public void setTask_progress(String newTask_progress)
	/*      */{
		/* 1569 */this.task_progress = newTask_progress;
		/*      */}
	/*      */
	/*      */public String getTask_summary()
	/*      */{
		/* 1577 */return this.task_summary;
		/*      */}
	/*      */
	/*      */public void setTask_summary(String newTask_summary)
	/*      */{
		/* 1585 */this.task_summary = newTask_summary;
		/*      */}
	/*      */
	/*      */public String getLeft_problem()
	/*      */{
		/* 1593 */return this.left_problem;
		/*      */}
	/*      */
	/*      */public String getPk_wo_dept_v() {
		/* 1597 */return this.pk_wo_dept_v;
	}
	/*      */
	/*      */public void setPk_wo_dept_v(String pk_wo_dept_v) {
		/* 1600 */this.pk_wo_dept_v = pk_wo_dept_v;
		/*      */}
	/*      */
	/*      */public void setLeft_problem(String newLeft_problem)
	/*      */{
		/* 1608 */this.left_problem = newLeft_problem;
		/*      */}
	/*      */
	/*      */public String getInnercode()
	/*      */{
		/* 1616 */return this.innercode;
		/*      */}
	/*      */
	/*      */public void setInnercode(String newInnercode)
	/*      */{
		/* 1624 */this.innercode = newInnercode;
		/*      */}
	/*      */
	/*      */public String getSrc_bill_type()
	/*      */{
		/* 1632 */return this.src_bill_type;
		/*      */}
	/*      */
	/*      */public void setSrc_bill_type(String newSrc_bill_type)
	/*      */{
		/* 1640 */this.src_bill_type = newSrc_bill_type;
		/*      */}
	/*      */
	/*      */public String getSrc_transi_type()
	/*      */{
		/* 1648 */return this.src_transi_type;
		/*      */}
	/*      */
	/*      */public void setSrc_transi_type(String newSrc_transi_type)
	/*      */{
		/* 1656 */this.src_transi_type = newSrc_transi_type;
		/*      */}
	/*      */
	/*      */public String getSrc_rowno()
	/*      */{
		/* 1664 */return this.src_rowno;
		/*      */}
	/*      */
	/*      */public void setSrc_rowno(String newSrc_rowno)
	/*      */{
		/* 1672 */this.src_rowno = newSrc_rowno;
		/*      */}
	/*      */
	/*      */public String getSrc_pk_bill()
	/*      */{
		/* 1680 */return this.src_pk_bill;
		/*      */}
	/*      */
	/*      */public void setSrc_pk_bill(String newSrc_pk_bill)
	/*      */{
		/* 1688 */this.src_pk_bill = newSrc_pk_bill;
		/*      */}
	/*      */
	/*      */public String getSrc_bill_code()
	/*      */{
		/* 1696 */return this.src_bill_code;
		/*      */}
	/*      */
	/*      */public void setSrc_bill_code(String newSrc_bill_code)
	/*      */{
		/* 1704 */this.src_bill_code = newSrc_bill_code;
		/*      */}
	/*      */
	/*      */public String getSrc_pk_bill_b()
	/*      */{
		/* 1712 */return this.src_pk_bill_b;
		/*      */}
	/*      */
	/*      */public void setSrc_pk_bill_b(String newSrc_pk_bill_b)
	/*      */{
		/* 1720 */this.src_pk_bill_b = newSrc_pk_bill_b;
		/*      */}
	/*      */
	/*      */public UFDateTime getSrc_head_ts() {
		/* 1724 */return this.src_head_ts;
	}
	/*      */
	/*      */public void setSrc_head_ts(UFDateTime src_head_ts) {
		/* 1727 */this.src_head_ts = src_head_ts;
		/*      */}
	/*      */
	/*      */public String getMemo()
	/*      */{
		/* 1735 */return this.memo;
		/*      */}
	/*      */
	/*      */public void setMemo(String newMemo)
	/*      */{
		/* 1743 */this.memo = newMemo;
		/*      */}
	/*      */
	/*      */public String getCreator()
	/*      */{
		/* 1751 */return this.creator;
		/*      */}
	/*      */
	/*      */public void setCreator(String newCreator)
	/*      */{
		/* 1759 */this.creator = newCreator;
		/*      */}
	/*      */
	/*      */public UFDateTime getCreationtime()
	/*      */{
		/* 1767 */return this.creationtime;
		/*      */}
	/*      */
	/*      */public void setCreationtime(UFDateTime newCreationtime)
	/*      */{
		/* 1775 */this.creationtime = newCreationtime;
		/*      */}
	/*      */
	/*      */public String getAuditor()
	/*      */{
		/* 1783 */return this.auditor;
		/*      */}
	/*      */
	/*      */public void setAuditor(String newAuditor)
	/*      */{
		/* 1791 */this.auditor = newAuditor;
		/*      */}
	/*      */
	/*      */public UFDateTime getAudittime()
	/*      */{
		/* 1799 */return this.audittime;
		/*      */}
	/*      */
	/*      */public void setAudittime(UFDateTime newAudittime)
	/*      */{
		/* 1807 */this.audittime = newAudittime;
		/*      */}
	/*      */
	/*      */public String getCheck_opinion()
	/*      */{
		/* 1815 */return this.check_opinion;
		/*      */}
	/*      */
	/*      */public void setCheck_opinion(String newCheck_opinion)
	/*      */{
		/* 1823 */this.check_opinion = newCheck_opinion;
		/*      */}
	/*      */
	/*      */public String getModifier()
	/*      */{
		/* 1831 */return this.modifier;
		/*      */}
	/*      */
	/*      */public void setModifier(String newModifier)
	/*      */{
		/* 1839 */this.modifier = newModifier;
		/*      */}
	/*      */
	/*      */public UFDateTime getModifiedtime()
	/*      */{
		/* 1847 */return this.modifiedtime;
		/*      */}
	/*      */
	/*      */public void setModifiedtime(UFDateTime newModifiedtime)
	/*      */{
		/* 1855 */this.modifiedtime = newModifiedtime;
		/*      */}
	/*      */
	/*      */public String getDef1()
	/*      */{
		/* 1863 */return this.def1;
		/*      */}
	/*      */
	/*      */public void setDef1(String newDef1)
	/*      */{
		/* 1871 */this.def1 = newDef1;
		/*      */}
	/*      */
	/*      */public String getDef2()
	/*      */{
		/* 1879 */return this.def2;
		/*      */}
	/*      */
	/*      */public void setDef2(String newDef2)
	/*      */{
		/* 1887 */this.def2 = newDef2;
		/*      */}
	/*      */
	/*      */public String getDef3()
	/*      */{
		/* 1895 */return this.def3;
		/*      */}
	/*      */
	/*      */public void setDef3(String newDef3)
	/*      */{
		/* 1903 */this.def3 = newDef3;
		/*      */}
	/*      */
	/*      */public String getDef4()
	/*      */{
		/* 1911 */return this.def4;
		/*      */}
	/*      */
	/*      */public void setDef4(String newDef4)
	/*      */{
		/* 1919 */this.def4 = newDef4;
		/*      */}
	/*      */
	/*      */public String getDef5()
	/*      */{
		/* 1927 */return this.def5;
		/*      */}
	/*      */
	/*      */public void setDef5(String newDef5)
	/*      */{
		/* 1935 */this.def5 = newDef5;
		/*      */}
	/*      */
	/*      */public String getDef6()
	/*      */{
		/* 1943 */return this.def6;
		/*      */}
	/*      */
	/*      */public void setDef6(String newDef6)
	/*      */{
		/* 1951 */this.def6 = newDef6;
		/*      */}
	/*      */
	/*      */public String getDef7()
	/*      */{
		/* 1959 */return this.def7;
		/*      */}
	/*      */
	/*      */public void setDef7(String newDef7)
	/*      */{
		/* 1967 */this.def7 = newDef7;
		/*      */}
	/*      */
	/*      */public String getDef8()
	/*      */{
		/* 1975 */return this.def8;
		/*      */}
	/*      */
	/*      */public void setDef8(String newDef8)
	/*      */{
		/* 1983 */this.def8 = newDef8;
		/*      */}
	/*      */
	/*      */public String getDef9()
	/*      */{
		/* 1991 */return this.def9;
		/*      */}
	/*      */
	/*      */public void setDef9(String newDef9)
	/*      */{
		/* 1999 */this.def9 = newDef9;
		/*      */}
	/*      */
	/*      */public String getDef10()
	/*      */{
		/* 2007 */return this.def10;
		/*      */}
	/*      */
	/*      */public void setDef10(String newDef10)
	/*      */{
		/* 2015 */this.def10 = newDef10;
		/*      */}
	/*      */
	/*      */public String getDef11()
	/*      */{
		/* 2023 */return this.def11;
		/*      */}
	/*      */
	/*      */public void setDef11(String newDef11)
	/*      */{
		/* 2031 */this.def11 = newDef11;
		/*      */}
	/*      */
	/*      */public String getDef12()
	/*      */{
		/* 2039 */return this.def12;
		/*      */}
	/*      */
	/*      */public void setDef12(String newDef12)
	/*      */{
		/* 2047 */this.def12 = newDef12;
		/*      */}
	/*      */
	/*      */public String getDef13()
	/*      */{
		/* 2055 */return this.def13;
		/*      */}
	/*      */
	/*      */public void setDef13(String newDef13)
	/*      */{
		/* 2063 */this.def13 = newDef13;
		/*      */}
	/*      */
	/*      */public String getDef14()
	/*      */{
		/* 2071 */return this.def14;
		/*      */}
	/*      */
	/*      */public void setDef14(String newDef14)
	/*      */{
		/* 2079 */this.def14 = newDef14;
		/*      */}
	/*      */
	/*      */public String getDef15()
	/*      */{
		/* 2087 */return this.def15;
		/*      */}
	/*      */
	/*      */public void setDef15(String newDef15)
	/*      */{
		/* 2095 */this.def15 = newDef15;
		/*      */}
	/*      */
	/*      */public String getDef16()
	/*      */{
		/* 2103 */return this.def16;
		/*      */}
	/*      */
	/*      */public void setDef16(String newDef16)
	/*      */{
		/* 2111 */this.def16 = newDef16;
		/*      */}
	/*      */
	/*      */public String getDef17()
	/*      */{
		/* 2119 */return this.def17;
		/*      */}
	/*      */
	/*      */public void setDef17(String newDef17)
	/*      */{
		/* 2127 */this.def17 = newDef17;
		/*      */}
	/*      */
	/*      */public String getDef18()
	/*      */{
		/* 2135 */return this.def18;
		/*      */}
	/*      */
	/*      */public void setDef18(String newDef18)
	/*      */{
		/* 2143 */this.def18 = newDef18;
		/*      */}
	/*      */
	/*      */public String getDef19()
	/*      */{
		/* 2151 */return this.def19;
		/*      */}
	/*      */
	/*      */public void setDef19(String newDef19)
	/*      */{
		/* 2159 */this.def19 = newDef19;
		/*      */}
	/*      */
	/*      */public String getDef20()
	/*      */{
		/* 2167 */return this.def20;
		/*      */}
	/*      */
	/*      */public void setDef20(String newDef20)
	/*      */{
		/* 2175 */this.def20 = newDef20;
		/*      */}
	/*      */
	/*      */public String getDef21()
	/*      */{
		/* 2183 */return this.def21;
		/*      */}
	/*      */
	/*      */public void setDef21(String newDef21)
	/*      */{
		/* 2191 */this.def21 = newDef21;
		/*      */}
	/*      */
	/*      */public String getDef22()
	/*      */{
		/* 2199 */return this.def22;
		/*      */}
	/*      */
	/*      */public void setDef22(String newDef22)
	/*      */{
		/* 2207 */this.def22 = newDef22;
		/*      */}
	/*      */
	/*      */public String getDef23()
	/*      */{
		/* 2215 */return this.def23;
		/*      */}
	/*      */
	/*      */public void setDef23(String newDef23)
	/*      */{
		/* 2223 */this.def23 = newDef23;
		/*      */}
	/*      */
	/*      */public String getDef24()
	/*      */{
		/* 2231 */return this.def24;
		/*      */}
	/*      */
	/*      */public void setDef24(String newDef24)
	/*      */{
		/* 2239 */this.def24 = newDef24;
		/*      */}
	/*      */
	/*      */public String getDef25()
	/*      */{
		/* 2247 */return this.def25;
		/*      */}
	/*      */
	/*      */public void setDef25(String newDef25)
	/*      */{
		/* 2255 */this.def25 = newDef25;
		/*      */}
	/*      */
	/*      */public String getDef26()
	/*      */{
		/* 2263 */return this.def26;
		/*      */}
	/*      */
	/*      */public void setDef26(String newDef26)
	/*      */{
		/* 2271 */this.def26 = newDef26;
		/*      */}
	/*      */
	/*      */public String getDef27()
	/*      */{
		/* 2279 */return this.def27;
		/*      */}
	/*      */
	/*      */public void setDef27(String newDef27)
	/*      */{
		/* 2287 */this.def27 = newDef27;
		/*      */}
	/*      */
	/*      */public String getDef28()
	/*      */{
		/* 2295 */return this.def28;
		/*      */}
	/*      */
	/*      */public void setDef28(String newDef28)
	/*      */{
		/* 2303 */this.def28 = newDef28;
		/*      */}
	/*      */
	/*      */public String getDef29()
	/*      */{
		/* 2311 */return this.def29;
		/*      */}
	/*      */
	/*      */public void setDef29(String newDef29)
	/*      */{
		/* 2319 */this.def29 = newDef29;
		/*      */}
	/*      */
	/*      */public String getDef30()
	/*      */{
		/* 2327 */return this.def30;
		/*      */}
	/*      */
	/*      */public void setDef30(String newDef30)
	/*      */{
		/* 2335 */this.def30 = newDef30;
		/*      */}
	/*      */
	/*      */public UFBoolean getGen_failure_flag()
	/*      */{
		/* 2343 */return this.gen_failure_flag;
		/*      */}
	/*      */
	/*      */public void setGen_failure_flag(UFBoolean newGen_failure_flag)
	/*      */{
		/* 2351 */this.gen_failure_flag = newGen_failure_flag;
		/*      */}
	/*      */
	/*      */public UFBoolean getFailure_flag()
	/*      */{
		/* 2359 */return this.failure_flag;
		/*      */}
	/*      */
	/*      */public void setFailure_flag(UFBoolean newFailure_flag)
	/*      */{
		/* 2367 */this.failure_flag = newFailure_flag;
		/*      */}
	/*      */
	/*      */public UFBoolean getHaschild_flag()
	/*      */{
		/* 2375 */return this.haschild_flag;
		/*      */}
	/*      */
	/*      */public void setHaschild_flag(UFBoolean newHaschild_flag)
	/*      */{
		/* 2383 */this.haschild_flag = newHaschild_flag;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_mtr_mny_org()
	/*      */{
		/* 2391 */return this.pl_mtr_mny_org;
		/*      */}
	/*      */
	/*      */public void setPl_mtr_mny_org(UFDouble newPl_mtr_mny_org)
	/*      */{
		/* 2399 */this.pl_mtr_mny_org = newPl_mtr_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_lbr_mny_org()
	/*      */{
		/* 2407 */return this.pl_lbr_mny_org;
		/*      */}
	/*      */
	/*      */public void setPl_lbr_mny_org(UFDouble newPl_lbr_mny_org)
	/*      */{
		/* 2415 */this.pl_lbr_mny_org = newPl_lbr_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_tol_mny_org()
	/*      */{
		/* 2423 */return this.pl_tol_mny_org;
		/*      */}
	/*      */
	/*      */public void setPl_tol_mny_org(UFDouble newPl_tol_mny_org)
	/*      */{
		/* 2431 */this.pl_tol_mny_org = newPl_tol_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_oth_mny_org()
	/*      */{
		/* 2439 */return this.pl_oth_mny_org;
		/*      */}
	/*      */
	/*      */public void setPl_oth_mny_org(UFDouble newPl_oth_mny_org)
	/*      */{
		/* 2447 */this.pl_oth_mny_org = newPl_oth_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_ttl_mny_org()
	/*      */{
		/* 2455 */return this.pl_ttl_mny_org;
		/*      */}
	/*      */
	/*      */public void setPl_ttl_mny_org(UFDouble newPl_ttl_mny_org)
	/*      */{
		/* 2463 */this.pl_ttl_mny_org = newPl_ttl_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_mtr_mny_org()
	/*      */{
		/* 2471 */return this.ac_mtr_mny_org;
		/*      */}
	/*      */
	/*      */public void setAc_mtr_mny_org(UFDouble newAc_mtr_mny_org)
	/*      */{
		/* 2479 */this.ac_mtr_mny_org = newAc_mtr_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_lbr_mny_org()
	/*      */{
		/* 2487 */return this.ac_lbr_mny_org;
		/*      */}
	/*      */
	/*      */public void setAc_lbr_mny_org(UFDouble newAc_lbr_mny_org)
	/*      */{
		/* 2495 */this.ac_lbr_mny_org = newAc_lbr_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_tol_mny_org()
	/*      */{
		/* 2503 */return this.ac_tol_mny_org;
		/*      */}
	/*      */
	/*      */public void setAc_tol_mny_org(UFDouble newAc_tol_mny_org)
	/*      */{
		/* 2511 */this.ac_tol_mny_org = newAc_tol_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_oth_mny_org()
	/*      */{
		/* 2519 */return this.ac_oth_mny_org;
		/*      */}
	/*      */
	/*      */public void setAc_oth_mny_org(UFDouble newAc_oth_mny_org)
	/*      */{
		/* 2527 */this.ac_oth_mny_org = newAc_oth_mny_org;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_ttl_mny_org()
	/*      */{
		/* 2535 */return this.ac_ttl_mny_org;
		/*      */}
	/*      */
	/*      */public void setAc_ttl_mny_org(UFDouble newAc_ttl_mny_org)
	/*      */{
		/* 2543 */this.ac_ttl_mny_org = newAc_ttl_mny_org;
		/*      */}
	/*      */
	/*      */public UFDate getCapitalize_date()
	/*      */{
		/* 2551 */return this.capitalize_date;
		/*      */}
	/*      */
	/*      */public void setCapitalize_date(UFDate newCapitalize_date)
	/*      */{
		/* 2559 */this.capitalize_date = newCapitalize_date;
		/*      */}
	/*      */
	/*      */public String getPk_ancestor_wo()
	/*      */{
		/* 2567 */return this.pk_ancestor_wo;
		/*      */}
	/*      */
	/*      */public void setPk_ancestor_wo(String newPk_ancestor_wo)
	/*      */{
		/* 2575 */this.pk_ancestor_wo = newPk_ancestor_wo;
		/*      */}
	/*      */
	/*      */public String getPk_currtype()
	/*      */{
		/* 2583 */return this.pk_currtype;
		/*      */}
	/*      */
	/*      */public void setPk_currtype(String newPk_currtype)
	/*      */{
		/* 2591 */this.pk_currtype = newPk_currtype;
		/*      */}
	/*      */
	/*      */public String getPk_org_v()
	/*      */{
		/* 2599 */return this.pk_org_v;
		/*      */}
	/*      */
	/*      */public void setPk_org_v(String newPk_org_v)
	/*      */{
		/* 2607 */this.pk_org_v = newPk_org_v;
		/*      */}
	/*      */
	/*      */public String getPk_fiorg_ap()
	/*      */{
		/* 2615 */return this.pk_fiorg_ap;
		/*      */}
	/*      */
	/*      */public void setPk_fiorg_ap(String newPk_fiorg_ap)
	/*      */{
		/* 2623 */this.pk_fiorg_ap = newPk_fiorg_ap;
		/*      */}
	/*      */
	/*      */public String getCoordinate()
	/*      */{
		/* 2631 */return this.coordinate;
		/*      */}
	/*      */
	/*      */public void setCoordinate(String newCoordinate)
	/*      */{
		/* 2639 */this.coordinate = newCoordinate;
		/*      */}
	/*      */
	/*      */public String getCoordinate_desc()
	/*      */{
		/* 2647 */return this.coordinate_desc;
		/*      */}
	/*      */
	/*      */public void setCoordinate_desc(String newCoordinate_desc)
	/*      */{
		/* 2655 */this.coordinate_desc = newCoordinate_desc;
		/*      */}
	/*      */
	/*      */public String getPk_fiorg_ap_v()
	/*      */{
		/* 2663 */return this.pk_fiorg_ap_v;
		/*      */}
	/*      */
	/*      */public void setPk_fiorg_ap_v(String newPk_fiorg_ap_v)
	/*      */{
		/* 2671 */this.pk_fiorg_ap_v = newPk_fiorg_ap_v;
		/*      */}
	/*      */
	/*      */public String getPk_profitcen_ap()
	/*      */{
		/* 2679 */return this.pk_profitcen_ap;
		/*      */}
	/*      */
	/*      */public void setPk_profitcen_ap(String newPk_profitcen_ap)
	/*      */{
		/* 2687 */this.pk_profitcen_ap = newPk_profitcen_ap;
		/*      */}
	/*      */
	/*      */public String getPk_chkfactor()
	/*      */{
		/* 2695 */return this.pk_chkfactor;
		/*      */}
	/*      */
	/*      */public void setPk_chkfactor(String newPk_chkfactor)
	/*      */{
		/* 2703 */this.pk_chkfactor = newPk_chkfactor;
		/*      */}
	/*      */
	/*      */public String getPk_fiorg_armt()
	/*      */{
		/* 2711 */return this.pk_fiorg_armt;
		/*      */}
	/*      */
	/*      */public void setPk_fiorg_armt(String newPk_fiorg_armt)
	/*      */{
		/* 2719 */this.pk_fiorg_armt = newPk_fiorg_armt;
		/*      */}
	/*      */
	/*      */public String getPk_fiorg_armt_v()
	/*      */{
		/* 2727 */return this.pk_fiorg_armt_v;
		/*      */}
	/*      */
	/*      */public void setPk_fiorg_armt_v(String newPk_fiorg_armt_v)
	/*      */{
		/* 2735 */this.pk_fiorg_armt_v = newPk_fiorg_armt_v;
		/*      */}
	/*      */
	/*      */public String getPk_profitcen_armt()
	/*      */{
		/* 2743 */return this.pk_profitcen_armt;
		/*      */}
	/*      */
	/*      */public void setPk_profitcen_armt(String newPk_profitcen_armt)
	/*      */{
		/* 2751 */this.pk_profitcen_armt = newPk_profitcen_armt;
		/*      */}
	/*      */
	/*      */public String getPk_chkfactor_armt()
	/*      */{
		/* 2759 */return this.pk_chkfactor_armt;
		/*      */}
	/*      */
	/*      */public void setPk_chkfactor_armt(String newPk_chkfactor_armt)
	/*      */{
		/* 2767 */this.pk_chkfactor_armt = newPk_chkfactor_armt;
		/*      */}
	/*      */
	/*      */public String getPk_fiorg_apmt()
	/*      */{
		/* 2775 */return this.pk_fiorg_apmt;
		/*      */}
	/*      */
	/*      */public void setPk_fiorg_apmt(String newPk_fiorg_apmt)
	/*      */{
		/* 2783 */this.pk_fiorg_apmt = newPk_fiorg_apmt;
		/*      */}
	/*      */
	/*      */public String getPk_fiorg_apmt_v()
	/*      */{
		/* 2791 */return this.pk_fiorg_apmt_v;
		/*      */}
	/*      */
	/*      */public void setPk_fiorg_apmt_v(String newPk_fiorg_apmt_v)
	/*      */{
		/* 2799 */this.pk_fiorg_apmt_v = newPk_fiorg_apmt_v;
		/*      */}
	/*      */
	/*      */public String getPk_profitcen_apmt()
	/*      */{
		/* 2807 */return this.pk_profitcen_apmt;
		/*      */}
	/*      */
	/*      */public void setPk_profitcen_apmt(String newPk_profitcen_apmt)
	/*      */{
		/* 2815 */this.pk_profitcen_apmt = newPk_profitcen_apmt;
		/*      */}
	/*      */
	/*      */public String getPk_chkfactor_apmt()
	/*      */{
		/* 2823 */return this.pk_chkfactor_apmt;
		/*      */}
	/*      */
	/*      */public void setPk_chkfactor_apmt(String newPk_chkfactor_apmt)
	/*      */{
		/* 2831 */this.pk_chkfactor_apmt = newPk_chkfactor_apmt;
		/*      */}
	/*      */
	/*      */public String getPk_fiorg_ic()
	/*      */{
		/* 2839 */return this.pk_fiorg_ic;
		/*      */}
	/*      */
	/*      */public void setPk_fiorg_ic(String newPk_fiorg_ic)
	/*      */{
		/* 2847 */this.pk_fiorg_ic = newPk_fiorg_ic;
		/*      */}
	/*      */
	/*      */public String getPk_fiorg_ic_v()
	/*      */{
		/* 2855 */return this.pk_fiorg_ic_v;
		/*      */}
	/*      */
	/*      */public void setPk_fiorg_ic_v(String newPk_fiorg_ic_v)
	/*      */{
		/* 2863 */this.pk_fiorg_ic_v = newPk_fiorg_ic_v;
		/*      */}
	/*      */
	/*      */public String getOpinion()
	/*      */{
		/* 2871 */return this.opinion;
		/*      */}
	/*      */
	/*      */public void setOpinion(String newOpinion)
	/*      */{
		/* 2879 */this.opinion = newOpinion;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_mtr_mny_group()
	/*      */{
		/* 2887 */return this.pl_mtr_mny_group;
		/*      */}
	/*      */
	/*      */public void setPl_mtr_mny_group(UFDouble newPl_mtr_mny_group)
	/*      */{
		/* 2895 */this.pl_mtr_mny_group = newPl_mtr_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_lbr_mny_group()
	/*      */{
		/* 2903 */return this.pl_lbr_mny_group;
		/*      */}
	/*      */
	/*      */public void setPl_lbr_mny_group(UFDouble newPl_lbr_mny_group)
	/*      */{
		/* 2911 */this.pl_lbr_mny_group = newPl_lbr_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_tol_mny_group()
	/*      */{
		/* 2919 */return this.pl_tol_mny_group;
		/*      */}
	/*      */
	/*      */public void setPl_tol_mny_group(UFDouble newPl_tol_mny_group)
	/*      */{
		/* 2927 */this.pl_tol_mny_group = newPl_tol_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_oth_mny_group()
	/*      */{
		/* 2935 */return this.pl_oth_mny_group;
		/*      */}
	/*      */
	/*      */public void setPl_oth_mny_group(UFDouble newPl_oth_mny_group)
	/*      */{
		/* 2943 */this.pl_oth_mny_group = newPl_oth_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_ttl_mny_group()
	/*      */{
		/* 2951 */return this.pl_ttl_mny_group;
		/*      */}
	/*      */
	/*      */public void setPl_ttl_mny_group(UFDouble newPl_ttl_mny_group)
	/*      */{
		/* 2959 */this.pl_ttl_mny_group = newPl_ttl_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_mtr_mny_group()
	/*      */{
		/* 2967 */return this.ac_mtr_mny_group;
		/*      */}
	/*      */
	/*      */public void setAc_mtr_mny_group(UFDouble newAc_mtr_mny_group)
	/*      */{
		/* 2975 */this.ac_mtr_mny_group = newAc_mtr_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_lbr_mny_group()
	/*      */{
		/* 2983 */return this.ac_lbr_mny_group;
		/*      */}
	/*      */
	/*      */public void setAc_lbr_mny_group(UFDouble newAc_lbr_mny_group)
	/*      */{
		/* 2991 */this.ac_lbr_mny_group = newAc_lbr_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_tol_mny_group()
	/*      */{
		/* 2999 */return this.ac_tol_mny_group;
		/*      */}
	/*      */
	/*      */public void setAc_tol_mny_group(UFDouble newAc_tol_mny_group)
	/*      */{
		/* 3007 */this.ac_tol_mny_group = newAc_tol_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_oth_mny_group()
	/*      */{
		/* 3015 */return this.ac_oth_mny_group;
		/*      */}
	/*      */
	/*      */public void setAc_oth_mny_group(UFDouble newAc_oth_mny_group)
	/*      */{
		/* 3023 */this.ac_oth_mny_group = newAc_oth_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_ttl_mny_group()
	/*      */{
		/* 3031 */return this.ac_ttl_mny_group;
		/*      */}
	/*      */
	/*      */public void setAc_ttl_mny_group(UFDouble newAc_ttl_mny_group)
	/*      */{
		/* 3039 */this.ac_ttl_mny_group = newAc_ttl_mny_group;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_mtr_mny_global()
	/*      */{
		/* 3047 */return this.pl_mtr_mny_global;
		/*      */}
	/*      */
	/*      */public void setPl_mtr_mny_global(UFDouble newPl_mtr_mny_global)
	/*      */{
		/* 3055 */this.pl_mtr_mny_global = newPl_mtr_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_lbr_mny_global()
	/*      */{
		/* 3063 */return this.pl_lbr_mny_global;
		/*      */}
	/*      */
	/*      */public void setPl_lbr_mny_global(UFDouble newPl_lbr_mny_global)
	/*      */{
		/* 3071 */this.pl_lbr_mny_global = newPl_lbr_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_tol_mny_global()
	/*      */{
		/* 3079 */return this.pl_tol_mny_global;
		/*      */}
	/*      */
	/*      */public void setPl_tol_mny_global(UFDouble newPl_tol_mny_global)
	/*      */{
		/* 3087 */this.pl_tol_mny_global = newPl_tol_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_oth_mny_global()
	/*      */{
		/* 3095 */return this.pl_oth_mny_global;
		/*      */}
	/*      */
	/*      */public void setPl_oth_mny_global(UFDouble newPl_oth_mny_global)
	/*      */{
		/* 3103 */this.pl_oth_mny_global = newPl_oth_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getPl_ttl_mny_global()
	/*      */{
		/* 3111 */return this.pl_ttl_mny_global;
		/*      */}
	/*      */
	/*      */public void setPl_ttl_mny_global(UFDouble newPl_ttl_mny_global)
	/*      */{
		/* 3119 */this.pl_ttl_mny_global = newPl_ttl_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_mtr_mny_global()
	/*      */{
		/* 3127 */return this.ac_mtr_mny_global;
		/*      */}
	/*      */
	/*      */public void setAc_mtr_mny_global(UFDouble newAc_mtr_mny_global)
	/*      */{
		/* 3135 */this.ac_mtr_mny_global = newAc_mtr_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_lbr_mny_global()
	/*      */{
		/* 3143 */return this.ac_lbr_mny_global;
		/*      */}
	/*      */
	/*      */public void setAc_lbr_mny_global(UFDouble newAc_lbr_mny_global)
	/*      */{
		/* 3151 */this.ac_lbr_mny_global = newAc_lbr_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_tol_mny_global()
	/*      */{
		/* 3159 */return this.ac_tol_mny_global;
		/*      */}
	/*      */
	/*      */public void setAc_tol_mny_global(UFDouble newAc_tol_mny_global)
	/*      */{
		/* 3167 */this.ac_tol_mny_global = newAc_tol_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_oth_mny_global()
	/*      */{
		/* 3175 */return this.ac_oth_mny_global;
		/*      */}
	/*      */
	/*      */public void setAc_oth_mny_global(UFDouble newAc_oth_mny_global)
	/*      */{
		/* 3183 */this.ac_oth_mny_global = newAc_oth_mny_global;
		/*      */}
	/*      */
	/*      */public UFDouble getAc_ttl_mny_global()
	/*      */{
		/* 3191 */return this.ac_ttl_mny_global;
		/*      */}
	/*      */
	/*      */public void setAc_ttl_mny_global(UFDouble newAc_ttl_mny_global)
	/*      */{
		/* 3199 */this.ac_ttl_mny_global = newAc_ttl_mny_global;
		/*      */}
	/*      */
	/*      */public String getPk_customer()
	/*      */{
		/* 3207 */return this.pk_customer;
		/*      */}
	/*      */
	/*      */public void setPk_customer(String newPk_customer)
	/*      */{
		/* 3215 */this.pk_customer = newPk_customer;
		/*      */}
	/*      */
	/*      */public String getPk_supplier()
	/*      */{
		/* 3223 */return this.pk_supplier;
		/*      */}
	/*      */
	/*      */public void setPk_supplier(String newPk_supplier)
	/*      */{
		/* 3231 */this.pk_supplier = newPk_supplier;
		/*      */}
	/*      */
	/*      */public UFBoolean getReceivable_flag()
	/*      */{
		/* 3239 */return this.receivable_flag;
		/*      */}
	/*      */
	/*      */public void setReceivable_flag(UFBoolean newReceivable_flag)
	/*      */{
		/* 3247 */this.receivable_flag = newReceivable_flag;
		/*      */}
	/*      */
	/*      */public UFBoolean getPayable_flag()
	/*      */{
		/* 3255 */return this.payable_flag;
		/*      */}
	/*      */
	/*      */public void setPayable_flag(UFBoolean newPayable_flag)
	/*      */{
		/* 3263 */this.payable_flag = newPayable_flag;
		/*      */}
	/*      */
	/*      */public String getPk_transitype() {
		/* 3267 */return this.pk_transitype;
	}
	/*      */
	/*      */public void setPk_transitype(String pk_transitype) {
		/* 3270 */this.pk_transitype = pk_transitype;
	}
	/*      */
	/*      */public String getSrc_pk_transitype() {
		/* 3273 */return this.src_pk_transitype;
	}
	/*      */
	/*      */public void setSrc_pk_transitype(String src_pk_transitype) {
		/* 3276 */this.src_pk_transitype = src_pk_transitype;
		/*      */}
	/*      */
	/*      */public Integer getDr()
	/*      */{
		/* 3284 */return this.dr;
		/*      */}
	/*      */
	/*      */public void setDr(Integer newDr)
	/*      */{
		/* 3292 */this.dr = newDr;
		/*      */}
	/*      */
	/*      */public UFDateTime getTs()
	/*      */{
		/* 3300 */return this.ts;
		/*      */}
	/*      */
	/*      */public void setTs(UFDateTime newTs)
	/*      */{
		/* 3308 */this.ts = newTs;
		/*      */}
	/*      */
	/*      */public String getParentPKFieldName()
	/*      */{
		/* 3319 */return null;
		/*      */}
	/*      */
	/*      */public String getPKFieldName()
	/*      */{
		/* 3330 */return "pk_wo";
		/*      */}
	/*      */
	/*      */public String getTableName()
	/*      */{
		/* 3341 */return "ewm_wo";
		/*      */}
	/*      */
	/*      */public static String getDefaultTableName()
	/*      */{
		/* 3350 */return "ewm_wo";
		/*      */}
	/*      */
	/*      */public WOChildrenVO[] getWo_children()
	/*      */{
		/* 3367 */return this.wo_children;
		/*      */}
	/*      */
	/*      */public void setWo_children(WOChildrenVO[] newWo_children)
	/*      */{
		/* 3375 */this.wo_children = newWo_children;
		/*      */}
	/*      */
	/*      */public UFBoolean getPayout_flag()
	/*      */{
		/* 3383 */return this.payout_flag;
		/*      */}
	/*      */
	/*      */public void setPayout_flag(UFBoolean newPayout_flag)
	/*      */{
		/* 3391 */this.payout_flag = newPayout_flag;
	}
	/*      */
	/*      */public String getPk_fiorg() {
		/* 3394 */return this.pk_fiorg;
	}
	/*      */
	/*      */public void setPk_fiorg(String pk_fiorg) {
		/* 3397 */this.pk_fiorg = pk_fiorg;
	}
	/*      */
	/*      */public String getPk_profitcen_armt_v() {
		/* 3400 */return this.pk_profitcen_armt_v;
	}
	/*      */
	/*      */public void setPk_profitcen_armt_v(String pk_profitcen_armt_v) {
		/* 3403 */this.pk_profitcen_armt_v = pk_profitcen_armt_v;
	}
	/*      */
	/*      */public String getPk_profitcen_apmt_v() {
		/* 3406 */return this.pk_profitcen_apmt_v;
	}
	/*      */
	/*      */public void setPk_profitcen_apmt_v(String pk_profitcen_apmt_v) {
		/* 3409 */this.pk_profitcen_apmt_v = pk_profitcen_apmt_v;
	}
	/*      */
	/*      */public String getPk_profitcen_ap_v() {
		/* 3412 */return this.pk_profitcen_ap_v;
	}
	/*      */
	/*      */public void setPk_profitcen_ap_v(String pk_profitcen_ap_v) {
		/* 3415 */this.pk_profitcen_ap_v = pk_profitcen_ap_v;
	}
	/*      */
	/*      */public UFBoolean getBICRETBill() {
		/* 3418 */return this.bICRETBill;
	}
	/*      */
	/*      */public void setBICRETBill(UFBoolean bill) {
		/* 3421 */this.bICRETBill = bill;
	}
	/*      */
	/*      */public String getBillmaker() {
		/* 3424 */return this.billmaker;
	}
	/*      */
	/*      */public void setBillmaker(String billmaker) {
		/* 3427 */this.billmaker = billmaker;
	}
	/*      */
	/*      */public UFDateTime getBillmaketime() {
		/* 3430 */return this.billmaketime;
	}
	/*      */
	/*      */public void setBillmaketime(UFDateTime billmaketime) {
		/* 3433 */this.billmaketime = billmaketime;
	}
	/*      */
	/*      */public UFDateTime getSrc_body_ts() {
		/* 3436 */return this.src_body_ts;
	}
	/*      */
	/*      */public void setSrc_body_ts(UFDateTime src_body_ts) {
		/* 3439 */this.src_body_ts = src_body_ts;
	}
	/*      */
	/*      */public UFBoolean getStatus_follow() {
		/* 3442 */return this.status_follow;
	}
	/*      */
	/*      */public void setStatus_follow(UFBoolean status_follow) {
		/* 3445 */this.status_follow = status_follow;
	}
	/*      */
	/*      */public String getPk_equip_capital() {
		/* 3448 */return this.pk_equip_capital;
	}
	/*      */
	/*      */public void setPk_equip_capital(String pk_equip_capital) {
		/* 3451 */this.pk_equip_capital = pk_equip_capital;
		/*      */}
	/*      */
}