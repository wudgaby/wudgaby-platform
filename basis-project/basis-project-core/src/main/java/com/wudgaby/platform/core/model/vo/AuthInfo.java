package com.wudgaby.platform.core.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/4 15:21
 * @Desc :
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "认证信息")
public class AuthInfo implements Serializable {
    @ApiModelProperty(value = "访问令牌")
    private String accessToken;
    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    @ApiModelProperty(value = "访问令牌过期时间")
    private long accessTokenExpire;
    @ApiModelProperty(value = "刷新令牌过期时间")
    private long refreshTokenExpire;

    @ApiModelProperty(value = "角色名")
    private String authority;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    @ApiModelProperty(value = "用户角色")
    private Integer userRole;

    @ApiModelProperty(value = "所属机构")
    private String orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "单点登录获取的token")
    private String ssoToken;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "内部用户的工号")
    private String jobNum;

    @ApiModelProperty(value = "用户属于内部机构还是外部机构")
    private Integer userBelongto;
}
