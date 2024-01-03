package com.wudgaby.sample.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wudgaby.platform.core.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 配置表
 * @TableName sys_config
 */
@TableName(value ="sys_config")
@Data
@Accessors(chain = true)
public class SysConfig extends BaseEntity implements Serializable {
    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置键名
     */
    private String configKey;

    /**
     * 配置键值
     */
    private String configValue;

    /**
     * 是否系统级别 1:是,0否
     */
    private Integer isSys;

    /**
     * 描述
     */
    private String remark;
}