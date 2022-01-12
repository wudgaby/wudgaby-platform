CREATE TABLE `default_schema` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) DEFAULT '' COMMENT '创建者',
  `update_by` varchar(50) DEFAULT '' COMMENT '修改者',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0:正常, -1:异常',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除 0:存在 1:删除',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `tenant_id` varchar(50) DEFAULT '' COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;