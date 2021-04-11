package com.wudgaby.quartz.stater.quartz;

import com.wudgaby.quartz.stater.vo.CronJobInfo;
import com.wudgaby.quartz.stater.vo.JobTriggerBaseInfo;
import com.wudgaby.quartz.stater.vo.SimpleJobInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName : QuartzJobManager
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/26/026 23:18
 * @Desc : quartz管理类
 */
@Slf4j
@Service
public class QuartzJobManager {
    @Resource private Scheduler scheduled;

    /**
    　　* @Description: 创建任务
    　　* @param [jobTriggerBaseInfo]
    　　* @return java.util.Date
    　　* @throws
    　　* @author
    　　* @date 2018/9/20 12:00
    　　*/
    public Date createJob(JobTriggerBaseInfo jobTriggerBaseInfo){
        try{
            if(jobTriggerBaseInfo instanceof SimpleJobInfo){
                return addSimpleJob((SimpleJobInfo)jobTriggerBaseInfo);
            }else if(jobTriggerBaseInfo instanceof CronJobInfo){
                return addCronJob((CronJobInfo)jobTriggerBaseInfo);
            }
        }catch(SchedulerException se){
            log.error("创建任务失败", se);
        }
        return null;
    }

    /**
    　　* @Description: 修改任务
    　　* @param [jobTriggerBaseInfo]
    　　* @return void
    　　* @throws
    　　* @author
    　　* @date 2018/9/20 11:59
    　　*/
    public void modifyJob(JobTriggerBaseInfo jobTriggerBaseInfo){
        try{
            if(jobTriggerBaseInfo instanceof SimpleJobInfo){
                modifySimpleJob((SimpleJobInfo)jobTriggerBaseInfo);
            }else if(jobTriggerBaseInfo instanceof CronJobInfo){
                modifyCronJob((CronJobInfo)jobTriggerBaseInfo);
            }
        }catch(SchedulerException se){
            log.error("修改任务失败", se);
        }
    }

    /**
    　　* @Description: 修改任务时间
    　　* @param [jobTriggerBaseInfo]
    　　* @return java.util.Date
    　　* @throws
    　　* @author
    　　* @date 2018/9/20 12:01
    　　*/
    public Date modifyJobTime(JobTriggerBaseInfo jobTriggerBaseInfo){
        try{
            if(jobTriggerBaseInfo instanceof SimpleJobInfo){
                return modifySimpleJobTrigger((SimpleJobInfo)jobTriggerBaseInfo);
            }else if(jobTriggerBaseInfo instanceof CronJobInfo){
                return modifyCronJobTrigger((CronJobInfo)jobTriggerBaseInfo);
            }
        }catch(SchedulerException se){
            log.error("修改任务时间失败", se);
        }
        return null;
    }

    /**
     　　* @Description: 移除一个任务，以及任务的所有trigger
     　　* @param [jobTriggerBaseInfo]
     　　* @return boolean
     　　* @throws
     　　* @author
     　　* @date 2018/9/20 11:54
     　　*/
    public boolean delJob(JobTriggerBaseInfo jobTriggerBaseInfo) throws SchedulerException {
        if(jobTriggerBaseInfo == null || StringUtils.isBlank(jobTriggerBaseInfo.getJobName())){
            log.error("trigger name is empty");
            return false;
        }
        log.info("删除任务. {}", jobTriggerBaseInfo);
        return scheduled.deleteJob(JobKey.jobKey(jobTriggerBaseInfo.getJobName(), jobTriggerBaseInfo.getJobGroupName()));
    }

    /**
     　　* @Description: 删除一个任务的的trigger
     　　* @param [jobTriggerBaseInfo]
     　　* @return boolean
     　　* @throws
     　　* @author
     　　* @date 2018/9/20 11:55
     　　*/
    public boolean delTrigger(JobTriggerBaseInfo jobTriggerBaseInfo) throws SchedulerException {
        if(jobTriggerBaseInfo == null || StringUtils.isBlank(jobTriggerBaseInfo.getTriggerName())){
            log.error("trigger name is empty");
            return false;
        }
        log.info("删除触发器. {}", jobTriggerBaseInfo);
        return scheduled.unscheduleJob(TriggerKey.triggerKey(jobTriggerBaseInfo.getTriggerName(), jobTriggerBaseInfo.getTriggerGroupName()));
    }

    /**
     　　* @Description: 启动所有任务
     　　* @param []
     　　* @return void
     　　* @throws
     　　* @author
     　　* @date 2018/9/20 12:06
     　　*/
    public void startJobs() throws SchedulerException {
        scheduled.start();
    }

    /**
     　　* @Description: 停止所有任务
     　　* @param [waitForJobsToComplete] false：不等待执行完成便结束；true：等待任务执行完才结束
     　　* @return void
     　　* @throws
     　　* @author
     　　* @date 2018/9/20 12:05
     　　*/
    public void shutdownJobs(boolean waitForJobsToComplete) throws SchedulerException {
        scheduled.shutdown(waitForJobsToComplete);
    }

    /**
     　　* @Description: 重新开始
     　　* @param [jobTriggerBaseInfo]
     　　* @return void
     　　* @throws
     　　* @author
     　　* @date 2018/9/20 13:41
     　　*/
    public void resumeJob(JobTriggerBaseInfo jobTriggerBaseInfo) throws SchedulerException {
        scheduled.resumeJob(JobKey.jobKey(jobTriggerBaseInfo.getJobName(), jobTriggerBaseInfo.getJobGroupName()));
    }

    /**
     　　* @Description: 重新开始
     　　* @param [jobTriggerBaseInfo]
     　　* @return void
     　　* @throws
     　　* @author
     　　* @date 2018/9/20 13:41
     　　*/
    public void resumeTrigger(JobTriggerBaseInfo jobTriggerBaseInfo) throws SchedulerException {
        scheduled.resumeTrigger(TriggerKey.triggerKey(jobTriggerBaseInfo.getTriggerName(), jobTriggerBaseInfo.getJobGroupName()));
    }

    /**
    　　* @Description: 添加simplejob
    　　* @param [simpleJobInfo]
    　　* @return java.util.Date
    　　* @throws
    　　* @author
    　　* @date 2018/9/20 12:01
    　　*/
    private Date addSimpleJob(SimpleJobInfo simpleJobInfo) throws SchedulerException {
        log.info("创建SimpleJob. {}", simpleJobInfo);
        //任务 自定义参数. jobclass可以获取到
        JobDataMap jobDataMap = new JobDataMap();
        simpleJobInfo.getJobMap().forEach((k, v)->{
            jobDataMap.put(k,v);
        });

        //任务描述 任务名，任务组，任务执行类
        JobDetail jobDetail = JobBuilder.newJob(simpleJobInfo.getJobClass())
                .withIdentity(simpleJobInfo.getJobName(), simpleJobInfo.getJobGroupName())
                .setJobData(jobDataMap)
                .build();

        //制定执行计划
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        //重复间隔
        if(simpleJobInfo.getIntervalInSeconds() > 0){
            scheduleBuilder.withIntervalInSeconds(simpleJobInfo.getIntervalInSeconds());
        }
        //重复指定次数
        if(simpleJobInfo.getRepeatCount() > 0){
            scheduleBuilder.withRepeatCount(simpleJobInfo.getRepeatCount());
        }
        //一直重复
        if(simpleJobInfo.getRepeatCount() < 0){
            scheduleBuilder.repeatForever();
        }

        //触发器构建
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(simpleJobInfo.getTriggerName(), simpleJobInfo.getTriggerGroupName())
                .startAt(simpleJobInfo.getStartTime())
                .endAt(simpleJobInfo.getEndTime())
                .withSchedule(scheduleBuilder)
                .build();
        return scheduled.scheduleJob(jobDetail, trigger);
    }

    /**
    　　* @Description: 修改simplejob
    　　* @param [simpleJobInfo]
    　　* @return void
    　　* @throws
    　　* @author
    　　* @date 2018/9/20 12:01
    　　*/
    private void modifySimpleJob(SimpleJobInfo simpleJobInfo) throws SchedulerException {
        log.info("创建SimpleJob. {}", simpleJobInfo);
        //任务 自定义参数. jobclass可以获取到
        JobDataMap jobDataMap = new JobDataMap();
        simpleJobInfo.getJobMap().forEach((k, v)->{
            jobDataMap.put(k,v);
        });

        //任务描述 任务名，任务组，任务执行类
        JobDetail jobDetail = JobBuilder.newJob(simpleJobInfo.getJobClass())
                .withIdentity(simpleJobInfo.getJobName(), simpleJobInfo.getJobGroupName())
                .setJobData(jobDataMap)
                .build();

        scheduled.addJob(jobDetail, true);
    }

    /**
    　　* @Description: 添加cronjob
    　　* @param [cronJobInfo]
    　　* @return java.util.Date
    　　* @throws
    　　* @author
    　　* @date 2018/9/20 12:02
    　　*/
    private Date addCronJob(CronJobInfo cronJobInfo) throws SchedulerException {
        log.info("创建CronJob. {}", cronJobInfo);
        //任务名，任务组，任务执行类
        JobDetail jobDetail = JobBuilder.newJob(cronJobInfo.getJobClass())
                .withIdentity(cronJobInfo.getJobName(), cronJobInfo.getJobGroupName())
                .build();

        //触发器构建
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(cronJobInfo.getTriggerName(), cronJobInfo.getTriggerGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronJobInfo.getCronExpression()))
                .build();
        return scheduled.scheduleJob(jobDetail, trigger);
    }

    /**
     　　* @Description: 修改cronjob
     　　* @param [cronJobInfo]
     　　* @return void
     　　* @throws
     　　* @author
     　　* @date 2018/9/20 12:01
     　　*/
    private void modifyCronJob(CronJobInfo cronJobInfo) throws SchedulerException {
        log.info("创建CronJob. {}", cronJobInfo);
        //任务名，任务组，任务执行类
        JobDetail jobDetail = JobBuilder.newJob(cronJobInfo.getJobClass())
                .withIdentity(cronJobInfo.getJobName(), cronJobInfo.getJobGroupName())
                .build();

        scheduled.addJob(jobDetail, true);
    }

    /**
    　　* @Description: 重置job触发时间
    　　* @param [simpleJobInfo]
    　　* @return java.util.Date
    　　* @throws
    　　* @author
    　　* @date 2018/9/20 11:51
    　　*/
    private Date modifySimpleJobTrigger(SimpleJobInfo simpleJobInfo) throws SchedulerException {
        log.info("重置SimpleJob. {}", simpleJobInfo);
        //制定执行计划
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        //重复间隔
        if(simpleJobInfo.getIntervalInSeconds() > 0){
            scheduleBuilder.withIntervalInSeconds(simpleJobInfo.getIntervalInSeconds());
        }
        //重复指定次数
        if(simpleJobInfo.getRepeatCount() > 0){
            scheduleBuilder.withRepeatCount(simpleJobInfo.getRepeatCount());
        }
        //一直重复
        if(simpleJobInfo.getRepeatCount() < 0){
            scheduleBuilder.repeatForever();
        }

        //触发器构建
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(simpleJobInfo.getTriggerName(), simpleJobInfo.getTriggerGroupName())
                .startAt(simpleJobInfo.getStartTime())
                .endAt(simpleJobInfo.getEndTime())
                .withSchedule(scheduleBuilder)
                .build();
        return scheduled.rescheduleJob(TriggerKey.triggerKey(simpleJobInfo.getTriggerName(), simpleJobInfo.getTriggerGroupName()), trigger);
    }
    
    /**
    　　* @Description: TODO
    　　* @param [cronJobInfo]
    　　* @return java.util.Date
    　　* @throws 
    　　* @author 
    　　* @date 2018/9/20 11:55 
    　　*/
    private Date modifyCronJobTrigger(CronJobInfo cronJobInfo) throws SchedulerException {
        log.info("重置CronJob. {}", cronJobInfo);

        //触发器构建
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(cronJobInfo.getTriggerName(), cronJobInfo.getTriggerGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronJobInfo.getCronExpression()))
                .build();
        return scheduled.rescheduleJob(TriggerKey.triggerKey(cronJobInfo.getTriggerName(), cronJobInfo.getTriggerGroupName()), trigger);
    }
}