package com.wudgaby.platform.sso.core.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/2/27 0:03
 * @Desc :
 */
@Data
@Accessors(chain = true)
public class SsoTokenVo implements Serializable {

    @ApiModelProperty(value = "用户令牌")
    @JsonProperty(value = "access_token")
    private String accessToken;

    @ApiModelProperty(value = "目标url")
    @JsonProperty(value = "target_url")
    private String targetUrl;
}
