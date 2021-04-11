/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : wudgaby-sys

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 13/04/2020 15:45:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_region`;
CREATE TABLE `sys_region`  (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
     `pid` int(11) NULL DEFAULT NULL COMMENT '父id',
     `shortname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称',
     `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
     `merger_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '全称',
     `level` tinyint(4) NULL DEFAULT NULL COMMENT '层级 0 1 2 省市区县',
     `pinyin` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音',
     `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '长途区号',
     `zip_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
     `first` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首字母',
     `lng` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经度',
     `lat` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纬度',
     `merge_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合并id',
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `idx_mid`(`merge_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3750 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;
-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `create_time` datetime NOT NULL COMMENT '创建时间',
      `update_time` datetime DEFAULT NULL COMMENT '修改时间',
      `create_by` varchar(50) DEFAULT '' COMMENT '创建者',
      `update_by` varchar(50) DEFAULT '' COMMENT '修改者',
      `config_name` varchar(100) DEFAULT NULL COMMENT '配置名称',
      `config_key` varchar(100) DEFAULT NULL COMMENT '配置键名',
      `config_value` varchar(100) DEFAULT NULL COMMENT '配置键值',
      `is_sys` tinyint(1) DEFAULT NULL COMMENT '是否系统级别 1:是,0否',
      `remark` varchar(2000) DEFAULT NULL COMMENT '描述',
      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='配置表';

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `parent_id` bigint(20) NOT NULL COMMENT '父id 0:根节点',
  `parent_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '父级ids ,分隔',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT '层级',
  `dept_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `dept_leader` bigint(20) NULL DEFAULT NULL COMMENT '负责人id',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0:正常, 1:停用',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0:存在 1:删除',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `org_id` bigint(20) NOT NULL COMMENT '所属公司',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
 `id` bigint(20) unsigned NOT NULL COMMENT '主键',
 `create_time` datetime NOT NULL COMMENT '创建时间',
 `update_time` datetime DEFAULT NULL COMMENT '修改时间',
 `create_by` varchar(50) DEFAULT '' COMMENT '创建者',
 `update_by` varchar(50) DEFAULT '' COMMENT '修改者',
 `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0:正常, 1:停用',
 `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁',
 `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除 0:存在 1:删除',
 `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
 `dict_type` varchar(50) NOT NULL COMMENT '字典类型',
 `dict_name` varchar(50) NOT NULL COMMENT '字典项名称',
 `dict_value` varchar(50) NOT NULL COMMENT '字典项值',
 `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父id 0:根节点',
 `parent_ids` varchar(100) NOT NULL DEFAULT '0' COMMENT '父级ids ,分隔',
 `remark` varchar(255) DEFAULT NULL COMMENT '备注',
 `is_sys` tinyint(1) DEFAULT '0' COMMENT '是否系统',
 PRIMARY KEY (`id`) USING BTREE,
 KEY `idx_type_val` (`dict_type`,`dict_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典表';
-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
 `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
 `name` varchar(50) NOT NULL COMMENT '字典类型名称',
 `type` varchar(50) DEFAULT NULL COMMENT '字典类型',
 `create_time` datetime NOT NULL COMMENT '创建时间',
 `update_time` datetime DEFAULT NULL COMMENT '修改时间',
 `create_by` varchar(50) DEFAULT '' COMMENT '创建者',
 `update_by` varchar(50) DEFAULT '' COMMENT '修改者',
 `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0:正常, 1:停用',
 `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁',
 `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除 0:存在 1:删除',
 `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
 `remark` varchar(255) DEFAULT NULL COMMENT '备注',
 `is_sys` tinyint(1) DEFAULT '0' COMMENT '是否系统',
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE KEY `uni_type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典类型表';
-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `file_bucket` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件仓库',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `file_suffix` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件后缀',
  `file_path` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
  `file_md5` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'md5',
  `file_unique_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件唯一标识',
  `file_size` bigint(20) NOT NULL DEFAULT 0 COMMENT '文件大小kb',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_in_mail
-- ----------------------------
DROP TABLE IF EXISTS `sys_in_mail`;
CREATE TABLE `sys_in_mail`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `in_mail_type` varchar(20) NOT NULL COMMENT '类型',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `sender_id` bigint(20) NOT NULL COMMENT '发送人id',
  `is_send_all` tinyint(1) NOT NULL COMMENT '是否全员通知 0:否, 1:是',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0:存在 1:删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '站内信' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_in_mail_file`;
CREATE TABLE `sys_in_mail_file` (
   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
   `im_id` bigint(20) NOT NULL COMMENT '站内信id',
   `file_name` varchar(100) NOT NULL COMMENT '文件名',
   `file_url` varchar(500) NOT NULL COMMENT '文件地址',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
   `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
   `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='站内信附件';

DROP TABLE IF EXISTS `sys_in_mail_receiver`;
CREATE TABLE `sys_in_mail_receiver` (
   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
   `im_id` bigint(20) NOT NULL COMMENT '站内信id',
   `receiver_id` bigint(20) NOT NULL COMMENT '接收人id',
   `receiver_name` varchar(50) NOT NULL COMMENT '接收人',
   `status` tinyint(4) NOT NULL COMMENT '状态 0:未读,1:已读',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
   `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
   `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='站内信-接收关系';
-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `update_time` datetime DEFAULT NULL COMMENT '修改时间',
   `create_by` varchar(50) DEFAULT '' COMMENT '创建者',
   `update_by` varchar(50) DEFAULT '' COMMENT '修改者',
   `user_id` bigint(20) NOT NULL COMMENT '操作人id',
   `user_name` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '操作人名称',
   `action` varchar(100) NOT NULL COMMENT '操作名称',
   `desc` varchar(1000) NOT NULL COMMENT '描述',
   `user_agent` varchar(1000) CHARACTER SET utf8mb4 NOT NULL COMMENT '客户端user-agent',
   `req_url` varchar(500) CHARACTER SET utf8mb4 NOT NULL COMMENT '访问地址',
   `req_method` varchar(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '访问方法',
   `req_param` varchar(1000) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '请求参数',
   `client_ip` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '请求ip',
   `server_addr` varchar(50) NOT NULL COMMENT '服务器ip',
   `exception_info` longtext CHARACTER SET utf8mb4 COMMENT '异常信息',
   `execute_time` int(11) NOT NULL COMMENT '执行时间ms',
   `type` varchar(50) NOT NULL COMMENT '日志类型SYSTEM',
   `os` varchar(50) NOT NULL COMMENT '系统名称',
   `browser` varchar(50) NOT NULL COMMENT '浏览器名称',
   `is_succeed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否成功0:成功 1:失败',
   `response` text COMMENT '返回消息',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='访问日志表';

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0:正常, 1:停用',
  `stick` tinyint(1) NOT NULL DEFAULT 0 COMMENT '置顶0:不置顶,1:置顶',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `full_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构全称',
  `abbr_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构简称',
  `org_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构代号',
  `area` int(11) NOT NULL DEFAULT 0 COMMENT '所属区域',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0:正常, 1:停用',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0:存在 1:删除',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `parent_id` bigint(20) NOT NULL COMMENT '父id 0:根节点',
  `parent_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '父级ids ,分隔',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT '层级',
  `leader` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人电话',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人邮箱',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人地址',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `position_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
  `position_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
  `position_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '岗位类型.高层,中层,基层,其他',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0:正常, 1:停用',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0:存在 1:删除',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `org_id` bigint(20) NOT NULL COMMENT '所属公司',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `parent_id` bigint(20) NOT NULL COMMENT '父id 0:根节点',
  `parent_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '父级ids ,分隔',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT '层级',
  `res_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源名称',
  `res_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源url',
  `res_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源类型. CATALOG,MENU,BUTTON',
  `res_method` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求方法',
  `target` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'CURRENT' COMMENT '打开方式. BLANK(新窗口),CURRENT(当前窗口)',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0:正常, 1:停用',
  `perm_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限标识',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端图标',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端组件',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端样式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `role_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0:正常, 1:停用',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0:存在 1:删除',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `is_sys` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否系统级别 1:是,0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `resource_id` bigint(20) NOT NULL COMMENT '资源id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_role_res`(`role_id`, `resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色-资源关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0:正常, 1:停用',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0:存在 1:删除',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '租户名',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '租户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0:正常, 1:停用',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0:存在 1:删除',
  `account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `salt` varchar(255) DEFAULT NULL COMMENT '密码盐',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `sex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '性别',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_user_dept`(`user_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-部门关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_org`;
CREATE TABLE `sys_user_org`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `org_id` bigint(20) NOT NULL COMMENT '公司id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_user_org`(`user_id`, `org_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户-机构关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_position`;
CREATE TABLE `sys_user_post`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `position_id` bigint(20) NOT NULL COMMENT '职位id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_user_post`(`user_id`, `position_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-职位关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_user_role`(`role_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户-角色关系表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_user_res`;
CREATE TABLE `sys_user_res`  (
 `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
 `user_id` bigint(20) NOT NULL COMMENT '用户id',
 `res_id` bigint(20) NOT NULL COMMENT '资源id',
 `create_time` datetime(0) NOT NULL COMMENT '创建时间',
 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
 `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
 `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改者',
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE INDEX `uni_user_res`(`res_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户-资源关系表' ROW_FORMAT = Dynamic;


SET FOREIGN_KEY_CHECKS = 1;
