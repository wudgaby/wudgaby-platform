package com.wudgaby.platform.core.result;

import com.google.common.collect.Maps;
import com.wudgaby.platform.core.constant.SystemConstant;
import com.wudgaby.platform.core.result.enums.ApiResultCode;
import com.wudgaby.platform.core.result.enums.SystemResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName : ApiResult
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/26/026 22:11
 * @Desc :
 */
@Data
@NoArgsConstructor
@ApiModel("统一接口返回")
public class ApiResult<T> implements Result {
    public static final Object EMPTY = new Object();

    @ApiModelProperty(value = "状态码", required = true)
    private Integer code;

    @ApiModelProperty(value = "消息", required = true)
    private String message;

    @ApiModelProperty(value = "数据体", required = true)
    private T data;

    @ApiModelProperty(value = "是否成功", required = true)
    private Boolean success;

    @ApiModelProperty(value = "附加数据")
    private Map<String, Object> extra;

    @ApiModelProperty(value = "响应时间")
    private Long timestamp;

    @ApiModelProperty(value = "请求标识")
    private String requestId;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "http状态码")
    private Integer httpStatus;

    /*@ApiModelProperty(value = "数据总数")
    private Long total;*/

    protected ApiResult(Integer code, String message){
        this(code, message, null);
    }

    private ApiResult(@Nonnull ApiResultCode apiResultCode){
        this(apiResultCode.getCode(), apiResultCode.getReason());
    }

    private ApiResult(@Nonnull ApiResultCode apiResultCode, T data){
        this(apiResultCode.getCode(), apiResultCode.getReason(), data);
    }

    private ApiResult(Integer code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = (code != null && code.equals(SystemResultCode.SUCCESS.getCode()));
        this.timestamp = System.currentTimeMillis();
        this.requestId = MDC.get(SystemConstant.MDC_TRACE_ID);
    }

    public static <T> ApiResult<T> success(){
        return new ApiResult<>(SystemResultCode.SUCCESS);
    }

    public static <T> ApiResult<T> success(String message){
        return new ApiResult<>(SystemResultCode.SUCCESS.getCode(), message);
    }
    public static <T> ApiResult<T> success(T data){
        return new ApiResult<>(SystemResultCode.SUCCESS.getCode(), SystemResultCode.SUCCESS.getReason(), data);
    }

    public static <T> ApiResult<T> success(String message, T data){
        return new ApiResult<>(SystemResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> ApiResult<T> failure(){
        return new ApiResult<>(SystemResultCode.FAILURE);
    }

    public static <T> ApiResult<T> failure(String message){
        return new ApiResult<>(SystemResultCode.FAILURE.getCode(), message);
    }

    public static <T> ApiResult<T> failure(ApiResultCode apiResultCode){
        return new ApiResult<>(apiResultCode);
    }

    public ApiResult<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    public ApiResult<T> message(String message){
        this.setMessage(message);
        return this;
    }

    public ApiResult<T> data(T data){
        this.setData(data);
        return this;
    }

    public ApiResult<T> requestId(String requestId){
        this.setRequestId(requestId);
        return this;
    }

    public ApiResult<T> path(String path){
        this.setPath(path);
        return this;
    }

    public ApiResult<T> httpStatus(Integer httpStatus){
        this.setHttpStatus(httpStatus);
        return this;
    }

    /**
     * 判断返回是否为成功
     *
     * @param apiResult
     * @return 是否成功
     */
    public static boolean isSuccess(@Nonnull ApiResult<?> apiResult) {
        return Optional.of(apiResult).map(ApiResult::getSuccess).get();
    }

    /**
     * 判断返回是否失败
     *
     * @param apiResult
     * @return 是否成功
     */
    public static boolean isFailure(@Nonnull ApiResult<?> apiResult) {
        return !isSuccess(apiResult);
    }

    public ApiResult<T> put(String key, Object value) {
        if (this.extra == null) {
            this.extra = Maps.newHashMap();
        }
        this.extra.put(key, value);
        return this;
    }

    public ApiResult<T> remove(String key) {
        if (this.extra == null) {
            return this;
        }
        this.extra.remove(key);
        return this;
    }

}
