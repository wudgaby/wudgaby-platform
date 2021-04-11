package com.wudgaby.logger.api;

import com.wudgaby.logger.api.vo.AccessLoggerInfo;

/**
 * @ClassName : AccessLoggerEventListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/4 12:20
 * @Desc :   访问日志监听器,实现此接口并注入到spring容器即可获取访问日志信息
 */
public interface AccessLoggerEventListener {
    /**
     * 当产生访问日志时,将调用此方法.注意,此方法内的操作应尽量设置为异步操作,否则可能影响请求性能
     */
    void onLogger(AccessLoggerInfo loggerInfo);

    default void onLogBefore(AccessLoggerInfo loggerInfo) { }
}
