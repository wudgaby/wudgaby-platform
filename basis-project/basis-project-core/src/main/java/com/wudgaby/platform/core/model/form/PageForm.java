package com.wudgaby.platform.core.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @ClassName : PageForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/22 14:45
 * @Desc :
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("分页表单")
public class PageForm extends BaseParam{
    /**默认每页行数*/
    private static final long DEFAULT_PAGE_SIZE = 20L;

    @ApiModelProperty(value = "页数", example = "1")
    @Min(value = 1, message = "最小页数1")
    @Max(value = 1000, message = "最大页数1000")
    private Long pageNum = 1L;

    @ApiModelProperty(value = "每页行数", example = "20")
    @Min(value = 1, message = "最小行数1")
    @Max(value = 9999, message = "最大行数9999")
    private Long pageSize = DEFAULT_PAGE_SIZE;

    @ApiModelProperty("排序参数")
    private List<SortItem> sortItems;

    @Data
    @ApiModel("排序对象")
    public static class SortItem {
        @ApiModelProperty("排序字段")
        private String item;

        @ApiModelProperty("是否正序排列，默认 true")
        private Boolean asc = true;
    }

    @ApiModelProperty(hidden = true)
    public Long getOffset() {
        return pageNum > 0 ? (pageNum - 1) * pageSize : 0;
    }
}
