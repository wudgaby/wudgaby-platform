package com.wudgaby.logger.api.event;

import com.wudgaby.logger.api.vo.AccessLoggerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/4 12:17
 * @Desc :   
 */
@AllArgsConstructor
@Data
public class AccessLoggerBeforeEvent {
    private AccessLoggerInfo accessLogInfoVo;
}
