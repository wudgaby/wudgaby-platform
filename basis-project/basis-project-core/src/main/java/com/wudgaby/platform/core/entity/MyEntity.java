package com.wudgaby.platform.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName : MyEntity
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/23 19:23
 * @Desc :
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MyEntity extends BaseEntity {
    @ApiModelProperty(value = "状态 1:正常, 0:异常")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "乐观锁")
    @JSONField(serialize = false)
    @Version
    private Integer version;

    @ApiModelProperty(value = "逻辑删除 0:存在 1:已删除")
    @JSONField(serialize = false)
    @TableLogic
    private Boolean deleted;
}
