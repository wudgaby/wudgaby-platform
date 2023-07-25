package com.wudgaby.sample.data.sensitive.entity;

import com.wudgaby.platform.core.entity.BaseEntity;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoBean;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import com.wudgaby.starter.data.security.sensitive.annotation.Sensitive;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;
import com.wudgaby.starter.data.security.sensitive.desensitize.SensitiveType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysUser对象", description="用户表")
@CryptoBean
@Sensitive
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态 0:正常, 1:停用")
    private Integer status;

    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除 0:存在 1:删除")
    private Boolean deleted;

    @ApiModelProperty(value = "账号")
    @SensitiveField
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "密码盐")
    private String salt;

    @SensitiveField
    @CryptoField
    @ApiModelProperty(value = "用户名")
    private String userName;

    @SensitiveField(SensitiveType.MOBILE_PHONE)
    @CryptoField
    @ApiModelProperty(value = "电话")
    private String phone;

    @SensitiveField(SensitiveType.EMAIL)
    @CryptoField
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "头像")
    private String avatar;


}
