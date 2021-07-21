package com.wudgaby.quartz.stater.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

/**
 * @author wudgaby
 * @version V1.0
 * @ClassName: SimpleJobInfo
 * @description: 
 * @date 2018/9/20 09:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SimpleJobInfo extends JobTriggerBaseInfo{
    /**
     * 任务开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 重复次数 小于0 一直重复
     */
    private int repeatCount;
    /**
     * 重复间隔 单位秒
     */
    private int intervalInSeconds;

    public SimpleJobInfo(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                       Class jobClass, Map<String, Object> paramMap){
        super(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, paramMap);
    }
}
