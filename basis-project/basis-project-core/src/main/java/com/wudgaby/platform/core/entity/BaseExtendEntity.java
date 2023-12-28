package com.wudgaby.platform.core.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/23 19:23
 * @Desc :
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BaseExtendEntity extends BaseEntity {
    @ApiModelProperty(value = "状态 1:正常, 0:异常")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "乐观锁")
    @JsonIgnore
    @Version
    private Integer version;

    @ApiModelProperty(value = "逻辑删除 0:存在 1:已删除")
    @JsonIgnore
    @TableLogic
    private Boolean deleted;
}
