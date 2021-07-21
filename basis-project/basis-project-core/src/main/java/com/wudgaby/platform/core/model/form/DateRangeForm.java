package com.wudgaby.platform.core.model.form;

import cn.hutool.core.date.DatePattern;
import com.wudgaby.platform.utils.LocalDateTimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName : DateRangeForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/22 18:06
 * @Desc :   
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("时间范围表单")
public class DateRangeForm extends PageForm{
    @ApiModelProperty(value = "开始时间(yyyy-MM-dd HH:mm:ss)")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date beginDate;

    @ApiModelProperty(value = "结束时间(yyyy-MM-dd HH:mm:ss)")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date endDate;

    public void endDateProcess(){
        if(endDate != null){
            LocalDateTime processed = LocalDateTimeUtil.convertDateToLDT(endDate)
                    .withHour(23).withMinute(59).withSecond(59);
            endDate = LocalDateTimeUtil.convertLDTToDate(processed);
        }
    }
}
