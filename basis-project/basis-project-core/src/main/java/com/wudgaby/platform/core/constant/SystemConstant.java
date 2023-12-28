package com.wudgaby.platform.core.constant;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/8/22 14:58
 * @Desc :   系统级别共享常量
 */
public interface SystemConstant {
    String SUCCESS = "success";
    String FAILURE = "failure";

    String YES = "yes";
    String NO = "no";

    String SYSTEM = "SYSTEM";


    /**
     * log MDC
     */
    String MDC_TRACE_ID = "X-TRACE-ID";
    /**
     *
     */
    String MDC_REQ_PATH = "X-REQ-PATH";
    /**
     * 前端请求头 - 用于链路跟踪
     */
    String HEADER_X_REQUEST_ID = "X-REQUEST-ID";
    /**
     * 响应头 - 用于链路跟踪
     */
    String HEADER_X_RESP_ID = "X-RESP-ID";
}
