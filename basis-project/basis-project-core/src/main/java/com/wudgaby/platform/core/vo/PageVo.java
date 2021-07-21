package com.wudgaby.platform.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName : PageVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/3 11:46
 * @Desc :   
 */
@Data
@Accessors(chain = true)
@ApiModel("分页返回对象")
public class PageVo<T> {
    @ApiModelProperty(value = "当前页数")
    private long pageNum;

    @ApiModelProperty(value = "总页数")
    private long totalPage;

    @ApiModelProperty(value = "数据总数")
    private long totalData;

    @ApiModelProperty(value = "每页行数")
    private long pageCount;

    @ApiModelProperty(value = "数据")
    private List<T> dataList;
}
