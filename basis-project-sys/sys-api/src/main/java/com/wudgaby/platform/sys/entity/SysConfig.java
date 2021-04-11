package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 配置表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysConfig对象", description="配置表")
public class SysConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置名称", required = true)
    private String configName;

    @ApiModelProperty(value = "配置键名", required = true)
    private String configKey;

    @ApiModelProperty(value = "配置键值", required = true)
    private String configValue;

    @ApiModelProperty(value = "是否系统级别 1:是,0否", required = true)
    @TableField("is_sys")
    private Boolean sys;

    @ApiModelProperty(value = "描述")
    private String remark;
}
