package com.wudgaby.common.schedule;

import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName : SystemInfoSchedule
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/25 11:32
 * @Desc :   TODO
 */
@Slf4j
@Component
public class SystemInfoSchedule {

    @Scheduled(cron = "0 0/30 * * * ?")
    public void launchScheduled(){
        log.info("Runtime Info. \n------------------------------------\n{}------------------------------------", SystemUtil.getRuntimeInfo());
    }
}
