package com.wudgaby.platform.twofactorauth.sample.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : User
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/9 23:09
 * @Desc :
 */
@ApiModel
@Data
public class User {
    private String account;
    private String password;
    @ApiModelProperty(hidden = true)
    private String secretKey;
    @ApiModelProperty(hidden = true)
    private Long googleCode;
}
