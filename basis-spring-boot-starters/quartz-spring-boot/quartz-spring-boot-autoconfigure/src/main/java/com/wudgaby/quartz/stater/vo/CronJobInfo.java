package com.wudgaby.quartz.stater.vo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * @author wudgaby
 * @version V1.0
 * @ClassName: CronJobInfo
 * @Description: TODO
 * @date 2018/9/20 09:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CronJobInfo extends JobTriggerBaseInfo {
    private String cronExpression;

    public CronJobInfo(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                         Class jobClass, Map<String, Object> paramMap){
        super(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, paramMap);
    }
}