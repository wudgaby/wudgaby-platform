package com.wudgaby.platform.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * @ClassName : LocalDateTimeUtil
 * @Author :  wudgaby
 * @Version :  1.0
 * @Date : 2018/9/19/01919:49
 * @Desc :
 */
@UtilityClass
public class LocalDateTimeUtil {
    public static final String COMMON_DATE = "yyyy-MM-dd";
    public static final String COMMON_YEAR_MONTH = "yyyy-MM";
    public static final String COMMON_TIME = "HH:mm:ss";
    public static final String COMMON_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String COMMON_DATE_HH_MIN = "yyyy-MM-dd HH:mm";

    //获取当前时间的LocalDateTime对象
    //LocalDateTime.now();

    //根据年月日构建LocalDateTime
    //LocalDateTime.of();

    //比较日期先后
    //LocalDateTime.now().isBefore(),
    //LocalDateTime.now().isAfter(),

    /**
    　　* @Description: Date转换为LocalDateTime
        * @package com.wudgaby.util.util
    　　* @param [date]
    　　* @return java.time.LocalDateTime
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:56
    　　*/
    public static LocalDateTime convertDateToLDT(Date date) {
        if(date == null){
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
    　　* @Description: 
        * @package com.wudgaby.util.util
    　　* @param [time]
    　　* @return java.util.Date
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:55
    　　*/
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
    　　* @Description: 获取指定日期的毫秒
        * @package com.wudgaby.util.util
    　　* @param [time]
    　　* @return java.lang.Long
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:56
    　　*/
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
    　　* @Description: 获取指定日期的秒
        * @package com.wudgaby.util.util
    　　* @param [time]
    　　* @return java.lang.Long
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:56
    　　*/
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
    　　* @Description: 获取指定时间的指定格式
        * @package com.wudgaby.util.util
    　　* @param [time, pattern]
    　　* @return java.lang.String
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:56
    　　*/
    public static String formatTime(LocalDateTime time,String pattern) {
        if(time == null){
            return "";
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
    　　* @Description: 获取当前时间的指定格式
        * @package com.wudgaby.util.util
    　　* @param [pattern]
    　　* @return java.lang.String
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:56
    　　*/
    public static String formatNow(String pattern) {
        return  formatTime(LocalDateTime.now(), pattern);
    }

    /**
    　　* @Description: 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
        * @package com.wudgaby.util.util
    　　* @param [time, number, field]
    　　* @return java.time.LocalDateTime
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:56
    　　*/
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
    　　* @Description: 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
        * @package com.wudgaby.util.util
    　　* @param [time, number, field]
    　　* @return java.time.LocalDateTime
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:57
    　　*/
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field){
        return time.minus(number,field);
    }

    /**
    　　* @Description: 获取两个日期的差  field参数为ChronoUnit.*
        * @package com.wudgaby.util.util
    　　* @param [startTime, endTime, field]
    　　* @return long
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:57
    　　*/
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) return period.getYears();
        if (field == ChronoUnit.MONTHS) return period.getYears() * 12L + period.getMonths();
        return field.between(startTime, endTime);
    }

    /**
    　　* @Description: 获取一天的开始时间，2017,7,22 00:00
        * @package com.wudgaby.util.util
    　　* @param [time]
    　　* @return java.time.LocalDateTime
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:57
    　　*/
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
    　　* @Description: 获取一天的结束时间，2017,7,22 23:59:59.999999999
        * @package com.wudgaby.util.util
    　　* @param [time]
    　　* @return java.time.LocalDateTime
    　　* @throws
    　　* @author Administrator
    　　* @date 2018/9/19/019 19:57
    　　*/
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

}