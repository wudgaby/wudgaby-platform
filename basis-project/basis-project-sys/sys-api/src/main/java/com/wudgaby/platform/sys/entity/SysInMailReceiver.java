package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站内信-接收关系
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysInMailReceiver对象", description="站内信-接收关系")
public class SysInMailReceiver extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "站内信id")
    private Long imId;

    @ApiModelProperty(value = "接收人id")
    private Long receiverId;

    @ApiModelProperty(value = "接收人")
    private String receiverName;

    @ApiModelProperty(value = "状态 0:未读,1:已读")
    private Integer status;


}
