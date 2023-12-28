package com.wudgaby.platform.core.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wudgaby.platform.core.result.enums.SystemResultCode;
import com.wudgaby.platform.core.vo.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/26/026 22:11
 * @Desc :   
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel("分页统一接口返回")
public class ApiPageResult<T> extends ApiResult {

    @ApiModelProperty(value = "当前页数")
    private long pageNum;

    @ApiModelProperty(value = "总页数")
    private long totalPage;

    @ApiModelProperty(value = "数据总数")
    private long totalData;

    @ApiModelProperty(value = "每页行数")
    private long pageCount;

    public ApiPageResult(long pageNum, long pageCount) {
        super(SystemResultCode.SUCCESS.getCode(), SystemResultCode.SUCCESS.getReason());
        this.pageNum = pageNum;
        this.pageCount = pageCount;
    }

    public ApiPageResult(long pageNum, long pageCount, long totalCount) {
        this(pageNum, pageCount);
        this.totalData = totalCount;
        if(pageCount > 0){
            this.totalPage = (totalCount + pageCount - 1) / pageCount;
        }
    }

    /**
     *
     * @param pageNum  当前页数
     * @param pageCount 每页行数
     * @param totalCount 数据总数
     * @param data 数据列表
     * @param <T>
     * @return
     */
    public static <T> ApiPageResult<T> success(long pageNum, long pageCount, long totalCount, T data){
        ApiPageResult<T> apiPageResult = new ApiPageResult<>(pageNum, pageCount, totalCount);
        apiPageResult.data(data);
        apiPageResult.setCode(SystemResultCode.SUCCESS.getCode());
        apiPageResult.setMessage(SystemResultCode.SUCCESS.getReason());
        return apiPageResult;
    }

    public static <T> ApiPageResult<T> success(PageVo<T> pageVo){
        ApiPageResult<T> apiPageResult = new ApiPageResult<>(pageVo.getPageNum(), pageVo.getPageSize(), pageVo.getTotal());
        apiPageResult.data(pageVo.getDataList());
        if(pageVo.getTotalPage() != 0){
            apiPageResult.setTotalPage(pageVo.getTotalPage());
        }
        apiPageResult.setCode(SystemResultCode.SUCCESS.getCode());
        apiPageResult.setMessage(SystemResultCode.SUCCESS.getReason());
        return apiPageResult;
    }

    public static <T> ApiPageResult<T> success(IPage<T> page){
        ApiPageResult<T> apiPageResult = new ApiPageResult<>(page.getCurrent(), page.getSize(), page.getTotal());
        apiPageResult.data(page.getRecords());
        if(page.getPages() != 0){
            apiPageResult.setTotalPage(page.getPages());
        }
        apiPageResult.setCode(SystemResultCode.SUCCESS.getCode());
        apiPageResult.setMessage(SystemResultCode.SUCCESS.getReason());
        return apiPageResult;
    }
}
