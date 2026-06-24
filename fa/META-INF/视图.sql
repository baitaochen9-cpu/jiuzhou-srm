create or REPLACE view forcardhistory as
select 
a.pk_group,a.pk_card, a.pk_cardhistory, a.pk_org, a.localoriginvalue  /*本币原值*/ , a.accudep  /*累计折旧*/ , a.predevaluate  /*减值准备*/, a.salvagerate  /*净残值率*/ , a.salvage  /*净残值*/ ,
a.servicemonth  /*折旧期数*/ , a.usedmonth  /*已计提月份*/, a.deprate  /*月折旧率*/, a.depamount  /*月折旧额*/, a.depamount_pre, a.originvalue_cal, a.accudep_cal, a.servicemonth_cal, a.usedmonth_cal, 
a.allworkloan, a.monthworkloan, a.depunit, a.pk_depmethod, a.herit_flag, a.newasset_flag, a.asset_state, a.asset_state_old, a.localoriginvalue_old, 
a.accudep_old, a.predevaluate_old, a.salvagerate_old, a.salvage_old, a.servicemonth_old, a.usedmonth_old, a.deprate_old, a.depamount_old, a.originvalue_cal_old,
a.accudep_cal_old, a.servicemonth_cal_old, a.usedmonth_cal_old, a.allworkloan_old, a.accuworkloan_old, a.pk_depmethod_old, a.herit_flag_old, a.localorigin_count,
a.curryeardep, a.curryeardep_new, a.pk_usedept pk_usedept/*部门*/, a.pk_mandept, a.paydept_flag, a.dep_start_date_old, a.dep_start_date, a.pk_costcenter, 0 localoriginvalue_ac,
0 accudep_ac, 0 salvage_ac, 0 salvagerate_ac, a.servicemonth servicemonth_ac, 0 usedmonth_ac, a.allworkloan allworkloan_ac, 0 accuworkloan_ac, 0 predevaluate_ac, 
0 yeardep, a.pk_depmethod pk_depmethod_ac, b.depattribute, c.depattr, d.dep_flag, 'N' istrace, 'N' iseffect, 'N' iseffect_next, 'N' if_dep, gatheritem1, gatheritem2,
gatheritem3, gatheritem4, gatheritem5, gatheritem6 from ( 
select fa_cardhistory.pk_group pk_group, fa_cardhistory.pk_card,fa_cardhistory.pk_org, --fa_cardhistory.period,fa_cardhistory.accyear,
max ( case when laststate_flag = 'Y' then pk_cardhistory else null end ) pk_cardhistory,
max ( case when laststate_flag = 'N' then localoriginvalue else null end ) localoriginvalue_old,
max ( case when laststate_flag = 'Y' then localoriginvalue else null end ) localoriginvalue, 
max ( case when laststate_flag = 'N' then accudep else null end ) accudep_old, 
max ( case when laststate_flag = 'Y' then accudep else null end ) accudep,
max ( case when laststate_flag = 'N' then predevaluate else null end ) predevaluate_old,
max ( case when laststate_flag = 'Y' then predevaluate else null end ) predevaluate, 
max ( case when laststate_flag = 'N' then salvagerate else null end ) salvagerate_old,
max ( case when laststate_flag = 'Y' then salvagerate else null end ) salvagerate,
max ( case when laststate_flag = 'N' then salvage else null end ) salvage_old, 
max ( case when laststate_flag = 'Y' then salvage else null end ) salvage,
max ( case when laststate_flag = 'N' then servicemonth else null end ) servicemonth_old,
max ( case when laststate_flag = 'Y' then servicemonth else null end ) servicemonth, 
max ( case when laststate_flag = 'N' then usedmonth else null end ) usedmonth_old,
max ( case when laststate_flag = 'Y' then usedmonth else null end ) usedmonth, 
max ( case when laststate_flag = 'N' then deprate else null end ) deprate_old,
max ( case when laststate_flag = 'Y' then deprate else null end ) deprate, 
max ( case when laststate_flag = 'N' then depamount else null end ) depamount_old, 
max ( case when laststate_flag = 'Y' then depamount else null end ) depamount,
max ( case when laststate_flag = 'Y' then depamount else null end ) depamount_pre, 
max ( case when laststate_flag = 'N' then originvalue_cal else null end ) originvalue_cal_old, 
max ( case when laststate_flag = 'Y' then originvalue_cal else null end ) originvalue_cal, 
max ( case when laststate_flag = 'N' then accudep_cal else null end ) accudep_cal_old, 
max ( case when laststate_flag = 'Y' then accudep_cal else null end ) accudep_cal,
max ( case when laststate_flag = 'N' then servicemonth_cal else null end ) servicemonth_cal_old, 
max ( case when laststate_flag = 'Y' then servicemonth_cal else null end ) servicemonth_cal,
max ( case when laststate_flag = 'N' then usedmonth_cal else null end ) usedmonth_cal_old,
max ( case when laststate_flag = 'Y' then usedmonth_cal else null end ) usedmonth_cal,
max ( case when laststate_flag = 'N' then allworkloan else null end ) allworkloan_old, 
max ( case when laststate_flag = 'Y' then allworkloan else null end ) allworkloan, 
max ( case when laststate_flag = 'Y' then monthworkloan else null end ) monthworkloan, 
max ( case when laststate_flag = 'Y' then depunit else null end ) depunit, 
max ( case when laststate_flag = 'N' then pk_category else null end ) pk_category_old, 
max ( case when laststate_flag = 'Y' then pk_category else null end ) pk_category,
max ( case when laststate_flag = 'N' then pk_usingstatus else null end ) pk_usingstatus_old, 
max ( case when laststate_flag = 'Y' then pk_usingstatus else null end ) pk_usingstatus, 
max ( case when laststate_flag = 'N' then pk_depmethod else null end ) pk_depmethod_old,
max ( case when laststate_flag = 'Y' then pk_depmethod else null end ) pk_depmethod, 
max ( case when laststate_flag = 'Y' then newasset_flag else null end ) newasset_flag,
max ( case when laststate_flag = 'N' then asset_state else null end ) asset_state_old, 
max ( case when laststate_flag = 'Y' then asset_state else null end ) asset_state,
max ( case when laststate_flag = 'N' then herit_flag else null end ) herit_flag_old, 
max ( case when laststate_flag = 'Y' then herit_flag else null end ) herit_flag,
max ( case when laststate_flag = 'N' then accuworkloan else null end ) accuworkloan_old, 
max ( case when laststate_flag = 'N' then localorigin_count else null end ) localorigin_count,
max ( case when laststate_flag = 'N' then curryeardep else null end ) curryeardep, 
max ( case when laststate_flag = 'Y' then curryeardep else null end ) curryeardep_new, 
max ( case when laststate_flag = 'Y' then paydept_flag else null end ) paydept_flag, 
max ( case when laststate_flag = 'N' then dep_start_date else null end ) dep_start_date_old,
max ( case when laststate_flag = 'Y' then dep_start_date else null end ) dep_start_date,
max ( case when laststate_flag = 'N' then pk_costcenter else null end ) pk_costcenter_old, 
max ( case when laststate_flag = 'Y' then pk_costcenter else null end ) pk_costcenter, 	
max ( case when laststate_flag = 'N' then pk_usedept else null end ) pk_usedept, 
max ( case when laststate_flag = 'N' then pk_mandept else null end ) pk_mandept, 
max ( case when laststate_flag = 'N' then pk_category else null end ) gatheritem1, 
null gatheritem2, null gatheritem3, null gatheritem4, null gatheritem5, null gatheritem6 
from fa_cardhistory where fa_cardhistory.dr = 0 and fa_cardhistory.localoriginvalue - fa_cardhistory.accudep - fa_cardhistory.salvage - fa_cardhistory.predevaluate > 0 
group by fa_cardhistory.pk_card , fa_cardhistory.pk_org ,fa_cardhistory.pk_group
) a 
left outer join temp_fa_depcategory b on a.pk_category_old = b.pk_category 
left outer join fa_category c on a.pk_category_old = c.pk_category 
left outer join fa_usingstatus d on a.pk_usingstatus = d.pk_usingstatus 
where a.asset_state_old <> 'reduce' 
and
a.pk_depmethod in ( '0001V110000000002YNQ', '0001V110000000002YNR', '0001V110000000002YNS', '0001V110000000002YNT', '0001V110000000002YNU', '0001V110000000002YNV', '0001V110000000002YNW', '0001V110000000002YNY', '0001V110000000002YNZ' )

;