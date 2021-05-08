package com.wudgaby.platform.flowable.helper.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("增加抄送人")
public class AddCcForm {
    @ApiModelProperty("节点名称")
    private String nodeKey;
    @ApiModelProperty("抄送人")
    private List<String> ccList;
}
