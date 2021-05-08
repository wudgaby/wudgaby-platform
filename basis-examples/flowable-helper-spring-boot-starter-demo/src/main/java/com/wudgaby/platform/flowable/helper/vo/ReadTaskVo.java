package com.wudgaby.platform.flowable.helper.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName : ReadTaskVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/28 17:36
 * @Desc :   TODO
 */
@Data
public class ReadTaskVo {
    @ApiModelProperty(value = "任务列表id", required = true)
    @NotEmpty(message = "任务列表不能为空")
    private List<String> taskIds;
}
