package com.wudgaby.logger.api.event;

import com.wudgaby.logger.api.vo.AccessLoggerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName : AccessLoggerAfterEvent
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/4 12:17
 * @Desc :   TODO
 */
@Data
@AllArgsConstructor
public class AccessLoggerAfterEvent {
    private AccessLoggerInfo accessLogInfoVo;
}
