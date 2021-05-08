package com.wudgaby.quartz.stater.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * @author wudgaby
 * @version V1.0
 * @ClassName: JobTriggerBaseInfo
 * @Description: TODO
 * @date 2018/9/20 09:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobTriggerBaseInfo {
    protected String jobName;
    protected String jobGroupName;
    protected String triggerName;
    protected String triggerGroupName;
    protected Class jobClass;
    protected Map<String, Object> jobMap;
}