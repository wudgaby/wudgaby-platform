/*
MySQL Backup
Database: caseoa
Backup Time: 2020-02-29 15:06:47
*/

DROP TABLE IF EXISTS `ACT_ID_MEMBERSHIP`;
DROP TABLE IF EXISTS `ACT_ID_GROUP`;
DROP TABLE IF EXISTS `ACT_ID_USER`;

DROP VIEW IF EXISTS `ACT_ID_MEMBERSHIP`;
DROP VIEW IF EXISTS `ACT_ID_GROUP`;
DROP VIEW IF EXISTS `ACT_ID_USER`;

CREATE SQL SECURITY DEFINER VIEW `ACT_ID_GROUP` AS (select `r`.`role_id` AS `ID_`,NULL AS `REV_`,`r`.`role_name` AS `NAME_`,`r`.`role_desc` AS `TYPE_` from `oa_role` `r`);
CREATE SQL SECURITY DEFINER VIEW `ACT_ID_MEMBERSHIP` AS (select `ur`.`user_id` AS `USER_ID_`,`ur`.`role_id` AS `GROUP_ID_` from `oa_user_role` `ur`);
CREATE SQL SECURITY DEFINER VIEW `ACT_ID_USER` AS (select `u`.`user_id` AS `ID_`,NULL AS `REV_`,`u`.`username` AS `FIRST_`,NULL AS `LAST_`,`u`.`user_email` AS `EMAIL_`,`u`.`password` AS `PWD_`,NULL AS `PICTURE_ID_` from `oa_user` `u`);

DROP VIEW IF EXISTS `view_examine_task`;
DROP VIEW IF EXISTS `view_oa_task`;
CREATE SQL SECURITY DEFINER VIEW `view_examine_task` AS select `task`.`ID_` AS `taskId`,`task`.`NAME_` AS `taskName`,`task`.`START_TIME_` AS `START_TIME_`,`task`.`END_TIME_` AS `task_end_time`,`task`.`ASSIGNEE_` AS `ASSIGNEE_`,`proc`.`PROC_DEF_ID_` AS `PROC_DEF_ID_`,`proc`.`PROC_INST_ID_` AS `PROC_INST_ID_`,`proc`.`START_USER_ID_` AS `START_USER_ID_`,`tuser2`.`FIRST_` AS `starterName`,`proc`.`END_TIME_` AS `proc_end_time`,`ident`.`TYPE_` AS `TYPE_`,`ident`.`USER_ID_` AS `USER_ID_`,`ident`.`GROUP_ID_` AS `GROUP_ID_`,`info`.`examine_id` AS `examine_id`,`info`.`case_no` AS `case_no`,`info`.`case_name` AS `case_name`,`info`.`loss_money` AS `loss_money`,`info`.`emergency_degree` AS `emergency_degree`,`info`.`apply_type` AS `apply_type`,`info`.`operator_name` AS `operator_name`,`info`.`bank_name` AS `bank_name`,`info`.`apply_user_id` AS `apply_user_id`,`info`.`police_no` AS `police_no`,`info`.`dispose_type` AS `dispose_type`,`info`.`query_req` AS `query_req`,`info`.`remark` AS `remark`,`info`.`apply_department` AS `apply_department`,`info`.`apply_time` AS `apply_time`,`info`.`examine_approve_status` AS `examine_approve_status`,`info`.`document_number` AS `document_number`,`info`.`document_name` AS `document_name`,`info`.`document_path` AS `document_path`,`info`.`feedback_state` AS `feedback_state`,`info`.`feedback_content` AS `feedback_content`,`info`.`upload_time` AS `upload_time`,`info`.`proc_inst_id` AS `proc_inst_id`,`info`.`handle_state` AS `handle_state`,`tuser1`.`FIRST_` AS `approverName`,`tuser1`.`ID_` AS `approverId` from (((((`ACT_HI_TASKINST` `task` left join `ACT_HI_PROCINST` `proc` on((`proc`.`PROC_INST_ID_` = `task`.`PROC_INST_ID_`))) left join `ACT_HI_IDENTITYLINK` `ident` on((`ident`.`TASK_ID_` = `task`.`ID_`))) left join `ACT_ID_USER` `tuser1` on((convert(`task`.`ASSIGNEE_` using utf8mb4) = `tuser1`.`ID_`))) left join `ACT_ID_USER` `tuser2` on((convert(`proc`.`START_USER_ID_` using utf8mb4) = `tuser2`.`ID_`))) join `oa_examine` `info` on((`info`.`proc_inst_id` = convert(`proc`.`PROC_INST_ID_` using utf8mb4))));
CREATE SQL SECURITY DEFINER VIEW `view_oa_task` AS select `task`.`ID_` AS `taskId`,`task`.`NAME_` AS `taskName`,`task`.`START_TIME_` AS `START_TIME_`,`task`.`END_TIME_` AS `task_end_time`,`task`.`ASSIGNEE_` AS `ASSIGNEE_`,`proc`.`PROC_DEF_ID_` AS `PROC_DEF_ID_`,`proc`.`PROC_INST_ID_` AS `PROC_INST_ID_`,`proc`.`START_USER_ID_` AS `START_USER_ID_`,`tuser2`.`FIRST_` AS `starterName`,`proc`.`END_TIME_` AS `proc_end_time`,`ident`.`TYPE_` AS `TYPE_`,`ident`.`USER_ID_` AS `USER_ID_`,`ident`.`GROUP_ID_` AS `GROUP_ID_`,`info`.`warn_id` AS `warn_id`,`info`.`warn_no` AS `warn_no`,`info`.`warn_name` AS `warn_name`,`info`.`industry_entry_user` AS `industry_entry_user`,`info`.`create_time` AS `create_time`,`info`.`examine_state` AS `examine_state`,`info`.`proc_inst_id` AS `proc_inst_id`,`tuser1`.`FIRST_` AS `approverName`,`tuser1`.`ID_` AS `approverId` from (((((`ACT_HI_TASKINST` `task` left join `ACT_HI_PROCINST` `proc` on((`proc`.`PROC_INST_ID_` = `task`.`PROC_INST_ID_`))) left join `ACT_HI_IDENTITYLINK` `ident` on((`ident`.`TASK_ID_` = `task`.`ID_`))) left join `ACT_ID_USER` `tuser1` on((convert(`task`.`ASSIGNEE_` using utf8mb4) = `tuser1`.`ID_`))) left join `ACT_ID_USER` `tuser2` on((convert(`proc`.`START_USER_ID_` using utf8mb4) = `tuser2`.`ID_`))) join `oa_warn_case_info` `info` on((`info`.`proc_inst_id` = convert(`proc`.`PROC_INST_ID_` using utf8mb4))));
