package com.wudgaby.platform.sys.form;

import com.wudgaby.platform.core.model.form.DateRangeForm;
import com.wudgaby.platform.sys.enums.InMailType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("通知查询表单")
public class InMailQueryForm extends DateRangeForm {
    @ApiModelProperty("关键词")
    private String keyword;
    @ApiModelProperty("类型")
    private InMailType inMailType;
    @ApiModelProperty("发布人ids")
    private List<Long> issueIds;

    @ApiModelProperty(hidden = true)
    private Long userId;
}
