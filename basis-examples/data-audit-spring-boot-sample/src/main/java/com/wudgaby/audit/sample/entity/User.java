package com.wudgaby.audit.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.javers.core.metamodel.annotation.PropertyName;

import java.math.BigDecimal;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 17:05
 * @desc :
 */
@Data
public class User {
    private Long id;
    private String username;
    // 该注解可以指定数据审计输出内容
    @PropertyName("手机号码")
    private String mobile;
    @PropertyName("邮箱")
    private String email;
    private BigDecimal wallet;
    private BigDecimal amount;
}
