package com.wudgaby.platform.core.result.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/26/026 22:05
 * @Desc :
 */
@Getter
@AllArgsConstructor
public enum SystemResultCode implements ApiResultCode {
    /**
     * 成功/失败
     */
    SUCCESS(1, "成功"),
    FAILURE(0, "失败"),

    /**
     * httpStatus 200
     * 70000 - 79999
     * 不用提示
     */

     /** httpStatus 200
     * 业务错误 10000 - 39999
      */
     BIZ_FAILURE(10000, "失败"),


    /** HttpStatus 400 客户端错误：40000 - 49999 */
    PARAM_IS_INVALID(40000, "无效参数"),
    EXISTED(40001, "数据已存在"),
    NO_ACCESS(40003, "无权限"),
    NOT_FOUND(40004, "资源不存在，请检查请求地址"),
    METHOD_NOT_ALLOWED(40005, "不支持该方法"),
    MISSING_REQ_DATA(40006, "缺少请求数据."),
    UNSUPPORTED_MEDIA_TYPE(40007, "不支持的ContentType"),
    NOT_READABLE(40008, "请求不可读错误"),
    REPEATED_REQUEST(40009, "请勿重复提交"),

    TOKEN_IS_INVALID(41000, "单点登录无效Token"),
    TOKEN_IS_DISABLED(41001, "单点登录Token过期"),

    /** HttpStatus 500 系统错误：50000 - 50999 */
    SYSTEM_INNER_ERROR(50000, "服务器出现异常，请联系管理员"),
    SYSTEM_UNUSUAL_ERROR(50001, "服务异常."),

    /** HttpStatus 500 网关: 51000 - 51999 */
    GATEWAY_SYSTEM_INNER_ERROR(51000, "网关内部异常"),
    GATEWAY_ERROR(51001, "网关异常"),
    GATEWAY_CONNECT_TIME_OUT(51002, "网关连接超时"),
    GATEWAY_NOT_FOUND_SERVICE(51003, "未找到服务"),
    GATEWAY_HYSTRIX_FALL_BACK(51004, "网关降级"),

    /** 接口错误：60000 - 69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),
    ;

    private final int code;
    private final String reason;

    @Override
    public String toString() {
        return code + " : " + reason;
    }
}
