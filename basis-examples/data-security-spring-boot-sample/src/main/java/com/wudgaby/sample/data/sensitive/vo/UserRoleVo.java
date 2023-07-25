package com.wudgaby.sample.data.sensitive.vo;

import com.wudgaby.sample.data.sensitive.entity.SysRole;
import com.wudgaby.sample.data.sensitive.entity.SysUser;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoBean;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 用户-角色关系表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@Accessors(chain = true)
@ApiModel(value="UserRoleVo")
@CryptoBean
public class UserRoleVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @CryptoField
    private SysUser user;

    @CryptoField
    private List<SysRole> roles;

}
