package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wudgaby.platform.core.entity.BaseEntity;
import com.wudgaby.platform.sys.enums.InMailType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站内信
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysInMail对象", description="站内信")
public class SysInMail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型")
    private InMailType inMailType;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "发送人id")
    private Long senderId;

    @ApiModelProperty(value = "是否全员通知 0:否, 1:是")
    @TableField("is_send_all")
    private Boolean sendAll;

    /*@ApiModelProperty(value = "逻辑删除 0:存在 1:删除")
    private Boolean deleted;*/


}
