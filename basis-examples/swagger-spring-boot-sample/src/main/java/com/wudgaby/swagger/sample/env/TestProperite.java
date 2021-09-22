package com.wudgaby.swagger.sample.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "vail")
public class TestProperite {

    @NotBlank(message = "不能为空")
    private String name;
    @Min(value = 18, message = "需要成年人")
    private Integer age;
}
