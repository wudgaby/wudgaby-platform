package com.wudgaby.platform.core.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName : BaseEntity
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/18 16:03
 * @Desc :   TODO
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("令牌信息")
public class TokenBean {
    @ApiModelProperty("访问令牌")
    private String accessToken;
    @ApiModelProperty("刷新令牌")
    private String refreshToken;

    @ApiModelProperty("过期时间")
    private long expire;

    public TokenBean(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
