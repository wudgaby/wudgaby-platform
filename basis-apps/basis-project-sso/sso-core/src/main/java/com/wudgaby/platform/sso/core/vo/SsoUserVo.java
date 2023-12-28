package com.wudgaby.platform.sso.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/3 9:57
 * @Desc :
 */
@Data
@Accessors(chain = true)
public class SsoUserVo implements Serializable {
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "用户姓名")
    private String username;

    @ApiModelProperty(value = "警号")
    private String policeNo;

    @ApiModelProperty(value = "用户部门")
    private Integer userDept;

    @ApiModelProperty(value = "用户手机")
    private String userTel;

    @ApiModelProperty(value = "用户Email")
    private String userEmail;

    @ApiModelProperty(value = "0：男 1：女")
    private Integer userSexy;

    @ApiModelProperty(value = "用户年龄")
    private Integer userAge;

    @ApiModelProperty(value = "用户头像URL")
    private String userPortrait;

    @ApiModelProperty(value = "用户地址")
    private String userAddr;

    @ApiModelProperty(value = "用户所在省")
    private String userProvince;

    @ApiModelProperty(value = "所在城市")
    private String userCity;

    @ApiModelProperty(value = "所在区域")
    private String userArea;

    @ApiModelProperty(value = "所在街道")
    private String userStreet;

    @ApiModelProperty(value = "房间号")
    private String userRoom;

    @ApiModelProperty(value = "角色")
    private List<String> roleList;

    private String token;
    private String version;
    private long expireMin;
    private long expireFreshTime;
}
