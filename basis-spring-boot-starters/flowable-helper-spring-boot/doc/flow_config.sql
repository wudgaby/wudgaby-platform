/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : flowable-demo

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 28/12/2020 20:35:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flow_config
-- ----------------------------
DROP TABLE IF EXISTS `flow_config`;
CREATE TABLE `flow_config`  (
  `id` bigint(20) NOT NULL DEFAULT 0,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程名称',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程分组类',
  `def_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义key',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `auto_repeat` tinyint(1) NULL DEFAULT NULL COMMENT '自动去重',
  `auto_approve` tinyint(1) NULL DEFAULT NULL COMMENT '发起人自动审批通过',
  `comment_hint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提示信息',
  `version` int(255) NULL DEFAULT NULL COMMENT '版本',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_optimized` tinyint(1) NULL DEFAULT 0 COMMENT '流程优化',
  `def_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义id',
  `is_reject_to_initiator` tinyint(1) NULL DEFAULT NULL COMMENT '是否回退到发起人',
  `is_nobody_skip` tinyint(1) DEFAULT NULL COMMENT '没人审批跳过',
  `default_approver` varchar(255) DEFAULT NULL COMMENT '没人审批时 指定审批人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_config
-- ----------------------------

-- ----------------------------
-- Table structure for flow_config_procinst
-- ----------------------------
DROP TABLE IF EXISTS `flow_config_procinst`;
CREATE TABLE `flow_config_procinst`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程名称',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程分组类',
  `def_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义key',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `auto_repeat` tinyint(1) NULL DEFAULT NULL COMMENT '自动去重',
  `auto_approve` tinyint(1) NULL DEFAULT NULL COMMENT '发起人自动审批通过',
  `comment_hint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提示信息',
  `version` int(255) NULL DEFAULT NULL COMMENT '版本',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `optimized` tinyint(1) NULL DEFAULT 0 COMMENT '流程优化',
  `def_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_config_procinst
-- ----------------------------
INSERT INTO `flow_config_procinst` VALUES (1341661915061628929, '警情审批', 'group1', 'demoDefKey', '我的爱', 1, 1, 'kkk', NULL, NULL, 1, NULL);

-- ----------------------------
-- Table structure for flow_node
-- ----------------------------
DROP TABLE IF EXISTS `flow_node`;
CREATE TABLE `flow_node`  (
  `id` bigint(20) NOT NULL,
  `flow_id` bigint(20) NULL DEFAULT NULL COMMENT '流程配置id',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点名称',
  `node_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点key',
  `node_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点类型',
  `assign_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分配id列表',
  `assign_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分配名称列表',
  `apportion_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人类型',
  `sign_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '多人审批时类型',
  `level` int(255) NULL DEFAULT NULL COMMENT '等级,LEAD类型时必填',
  `expr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表达式,EXPR类型时必填',
  `allow_optional` tinyint(1) NULL DEFAULT NULL COMMENT '允许选择其他抄送人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_node
-- ----------------------------

-- ----------------------------
-- Table structure for flow_node_procinst
-- ----------------------------
DROP TABLE IF EXISTS `flow_node_procinst`;
CREATE TABLE `flow_node_procinst`  (
  `id` bigint(20) NOT NULL,
  `flow_id` bigint(20) NULL DEFAULT NULL COMMENT '流程配置id',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点名称',
  `node_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点key',
  `node_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点类型',
  `assign_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分配id列表',
  `assign_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分配名称列表',
  `apportion_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人类型',
  `sign_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '多人审批时类型',
  `level` int(255) NULL DEFAULT NULL COMMENT '等级,LEAD类型时必填',
  `expr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表达式,EXPR类型时必填',
  `allow_optional` tinyint(1) NULL DEFAULT NULL COMMENT '允许选择其他抄送人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_node_procinst
-- ----------------------------
INSERT INTO `flow_node_procinst` VALUES (1341661915204235266, 1341661915061628929, '审批人', '', 'APPROVAL', '1', 'sp1', 'USER', 'OR_SIGN', 0, '', 1, NULL);
INSERT INTO `flow_node_procinst` VALUES (1341661915216818178, 1341661915061628929, '审批人', '', 'APPROVAL', '1,2,3', 'sp1,sp2,sp3', 'USER', 'OR_SIGN', 0, '', 1, NULL);
INSERT INTO `flow_node_procinst` VALUES (1341661915216818179, 1341661915061628929, '抄送', '', 'CC', '4,5', 'cc4,cc5', 'USER', 'OR_SIGN', 0, '', 1, NULL);
INSERT INTO `flow_node_procinst` VALUES (1341661915225206785, 1341661915061628929, '直接领导', '', 'APPROVAL', '', '', 'LEAD', 'OR_SIGN', 1, '', 1, NULL);
INSERT INTO `flow_node_procinst` VALUES (1341661915225206786, 1341661915061628929, '审批人', '', 'APPROVAL', '1,2', 'sp1,sp2', 'USER', 'AND_SIGN', 0, '', 1, NULL);
INSERT INTO `flow_node_procinst` VALUES (1341661915233595394, 1341661915061628929, '财务审批', '', 'APPROVAL', '1', 'sp1', 'ROLE', 'OR_SIGN', 0, '', 1, NULL);

-- ----------------------------
-- Table structure for oa_dept
-- ----------------------------
DROP TABLE IF EXISTS `oa_dept`;
CREATE TABLE `oa_dept`  (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父级部门ID',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `dept_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门描述',
  `dept_leader` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门负责人ID',
  `dept_state` tinyint(2) NULL DEFAULT 0 COMMENT '0:正常  11：屏蔽',
  `dept_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门深度',
  `is_parent` tinyint(1) NULL DEFAULT 0 COMMENT '是否是父部门',
  `add_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '添加该部门用户id',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '添加时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oa_dept
-- ----------------------------

-- ----------------------------
-- Table structure for oa_dept_user
-- ----------------------------
DROP TABLE IF EXISTS `oa_dept_user`;
CREATE TABLE `oa_dept_user`  (
  `dept_id` int(11) NULL DEFAULT NULL,
  `user_id` int(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oa_dept_user
-- ----------------------------

-- ----------------------------
-- Table structure for oa_role
-- ----------------------------
DROP TABLE IF EXISTS `oa_role`;
CREATE TABLE `oa_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色Code',
  `role_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `add_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '添加人用户ID',
  `with_system` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是系统预定 0：不是，1不是',
  `role_state` tinyint(2) NOT NULL DEFAULT 0 COMMENT '0：正常  10：删除 11：屏蔽',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '添加时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oa_role
-- ----------------------------
INSERT INTO `oa_role` VALUES (1, '开发', 'devp', NULL, '1', 0, 0, NULL, NULL);
INSERT INTO `oa_role` VALUES (2, '人事', 'renshi', NULL, '1', 0, 0, NULL, NULL);

-- ----------------------------
-- Table structure for oa_user
-- ----------------------------
DROP TABLE IF EXISTS `oa_user`;
CREATE TABLE `oa_user`  (
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
  `police_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '警号',
  `user_dept` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户部门',
  `user_tel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `add_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户添加id',
  `user_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户Email',
  `user_sexy` int(11) NOT NULL DEFAULT 0 COMMENT '0：男 1：女',
  `user_age` int(11) NOT NULL DEFAULT 0 COMMENT '用户年龄',
  `user_portrait` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像URL',
  `user_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户地址',
  `user_province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_area` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_street` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_room` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '用户可用状态 0：正常  1：不可用',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '用户添加时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oa_user
-- ----------------------------
INSERT INTO `oa_user` VALUES ('1', 'test1', NULL, 'test1', NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `oa_user` VALUES ('2', 'test2', NULL, 'test2', NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `oa_user` VALUES ('3', 'test3', NULL, 'test3', NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `oa_user` VALUES ('4', 'test4', NULL, 'test4', NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `oa_user` VALUES ('5', 'test5', NULL, 'test5', NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 0);

-- ----------------------------
-- Table structure for oa_user_role
-- ----------------------------
DROP TABLE IF EXISTS `oa_user_role`;
CREATE TABLE `oa_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oa_user_role
-- ----------------------------
INSERT INTO `oa_user_role` VALUES (1, '4', 1);
INSERT INTO `oa_user_role` VALUES (2, '5', 1);

SET FOREIGN_KEY_CHECKS = 1;
