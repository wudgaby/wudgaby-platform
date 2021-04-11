package com.wudgaby.platform.sys.dto;

import com.wudgaby.platform.sys.enums.InMailType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName : InMailTypeStDTO
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/9/28 18:02
 * @Desc :   TODO
 */
@Data
@Accessors(chain = true)
@ApiModel("通知公告未读类型传输对象")
public class InMailTypeStDTO {
    @ApiModelProperty("类型")
    private InMailType inMailType;

    @ApiModelProperty("未读数")
    private Long unreadTotal;
}
