DROP TABLE IF EXISTS `base_action`;
CREATE TABLE `base_action`
(
    `action_id`   bigint(20) NOT NULL COMMENT '资源ID',
    `action_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源编码',
    `action_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源名称',
    `action_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
    `menu_id`     bigint(20) DEFAULT NULL COMMENT '资源父节点',
    `priority`    int(10) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
    `status`      tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
    `create_time` datetime                      NOT NULL,
    `update_time` datetime                      DEFAULT NULL,
    `is_persist`  tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
    `service_id`  varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务名称',
    PRIMARY KEY (`action_id`),
    UNIQUE KEY `action_code` (`action_code`) USING BTREE,
    UNIQUE KEY `action_id` (`action_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-功能操作';

DROP TABLE IF EXISTS `base_api`;
CREATE TABLE `base_api`
(
    `api_id`         bigint(20) NOT NULL COMMENT '接口ID',
    `api_code`       varchar(255) COLLATE utf8_bin NOT NULL COMMENT '接口编码',
    `api_name`       varchar(100) COLLATE utf8_bin NOT NULL COMMENT '接口名称',
    `api_category`   varchar(20) COLLATE utf8_bin  DEFAULT 'default' COMMENT '接口分类:default-默认分类',
    `api_desc`       varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
    `request_method` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求方式',
    `content_type`   varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '响应类型',
    `service_id`     varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务ID',
    `path`           varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
    `priority`       bigint(20) NOT NULL DEFAULT '0' COMMENT '优先级',
    `status`         tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
    `create_time`    datetime                      NOT NULL,
    `update_time`    datetime                      DEFAULT NULL,
    `is_persist`     tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
    `is_auth`        tinyint(3) NOT NULL DEFAULT '1' COMMENT '是否需要认证: 0-无认证 1-身份认证 默认:1',
    `is_open`        tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否公开: 0-内部的 1-公开的',
    `class_name`     varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '类名',
    `method_name`    varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '方法名',
    PRIMARY KEY (`api_id`),
    UNIQUE KEY `api_code` (`api_code`),
    UNIQUE KEY `api_id` (`api_id`),
    KEY              `service_id` (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-API接口';

DROP TABLE IF EXISTS `base_authority`;
CREATE TABLE `base_authority`
(
    `authority_id` bigint(20) NOT NULL,
    `authority`    varchar(255) NOT NULL COMMENT '权限标识',
    `menu_id`      bigint(20) DEFAULT NULL COMMENT '菜单资源ID',
    `api_id`       bigint(20) DEFAULT NULL COMMENT 'API资源ID',
    `action_id`    bigint(20) DEFAULT NULL,
    `status`       tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态',
    `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`authority_id`),
    KEY            `menu_id` (`menu_id`),
    KEY            `api_id` (`api_id`),
    KEY            `action_id` (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-菜单权限、操作权限、API权限';

DROP TABLE IF EXISTS `base_authority_action`;
CREATE TABLE `base_authority_action`
(
    `action_id`    bigint(20) NOT NULL COMMENT '操作ID',
    `authority_id` bigint(20) NOT NULL COMMENT 'API',
    `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
    KEY            `action_id` (`action_id`) USING BTREE,
    KEY            `authority_id` (`authority_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统权限-功能操作关联表';


DROP TABLE IF EXISTS `base_authority_role`;
CREATE TABLE `base_authority_role`
(
    `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
    `role_id`      bigint(20) NOT NULL COMMENT '角色ID',
    `expire_time`  datetime DEFAULT NULL COMMENT '过期时间:null表示长期',
    `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
    KEY            `authority_id` (`authority_id`) USING BTREE,
    KEY            `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-角色关联';


DROP TABLE IF EXISTS `base_authority_user`;
CREATE TABLE `base_authority_user`
(
    `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
    `user_id`      bigint(20) NOT NULL COMMENT '用户ID',
    `expire_time`  datetime DEFAULT NULL COMMENT '过期时间',
    `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
    KEY            `authority_id` (`authority_id`) USING BTREE,
    KEY            `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-用户关联';

DROP TABLE IF EXISTS `base_authority_app`;
CREATE TABLE `base_authority_app`
(
    `authority_id` bigint(50) NOT NULL COMMENT '权限ID',
    `app_id`       varchar(100) NOT NULL COMMENT '应用ID',
    `expire_time`  datetime DEFAULT NULL COMMENT '过期时间:null表示长期',
    `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
    KEY            `authority_id` (`authority_id`) USING BTREE,
    KEY            `app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-应用关联';

DROP TABLE IF EXISTS `base_app`;
CREATE TABLE `base_app`
(
    `app_id`       varchar(50)  NOT NULL COMMENT '客户端ID',
    `api_key`      varchar(255) DEFAULT NULL COMMENT 'API访问key',
    `secret_key`   varchar(255) NOT NULL COMMENT 'API访问密钥',
    `app_name`     varchar(255) NOT NULL COMMENT 'app名称',
    `app_name_en`  varchar(255) NOT NULL COMMENT 'app英文名称',
    `app_icon`     varchar(255) NOT NULL COMMENT '应用图标',
    `app_type`     varchar(50)  NOT NULL COMMENT 'app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用',
    `app_desc`     varchar(255) DEFAULT NULL COMMENT 'app描述',
    `app_os`       varchar(25)  DEFAULT NULL COMMENT '移动应用操作系统:ios-苹果 android-安卓',
    `website`      varchar(255) NOT NULL COMMENT '官网地址',
    `developer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '开发者ID:默认为0',
    `create_time`  datetime     NOT NULL COMMENT '创建时间',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新时间',
    `status`       tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
    `is_persist`   tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
    PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统应用-基础信息';


DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu` (
     `menu_id` bigint(20) NOT NULL COMMENT '菜单Id',
     `parent_id` bigint(20) DEFAULT NULL COMMENT '父级菜单',
     `menu_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单编码',
     `menu_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
     `menu_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
     `scheme` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '路径前缀',
     `path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
     `icon` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '菜单标题',
     `target` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '_self' COMMENT '打开方式:_self窗口内,_blank新窗口',
     `priority` bigint(20) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
     `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
     `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
     `service_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务名',
     PRIMARY KEY (`menu_id`),
     UNIQUE KEY `menu_code` (`menu_code`),
     KEY `service_id` (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-菜单信息';