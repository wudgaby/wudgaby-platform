package com.wudgaby.sample.data.sensitive.vo;

import com.wudgaby.starter.data.security.crypt.annotation.CryptoBean;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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
@ApiModel(value="UserForm")
@CryptoBean
public class UserForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @CryptoField
    private From1 from1;

    @CryptoField
    private From2 from2;

    @CryptoBean
    @Data
    public class From1{
        @ApiModelProperty(value = "账号")
        private String account;

        @ApiModelProperty(value = "密码")
        private String password;

        @CryptoField
        @ApiModelProperty(value = "用户名")
        private String userName;
    }

    @CryptoBean
    @Data
    public class From2{
        @CryptoField
        @ApiModelProperty(value = "电话")
        private String phone;

        @CryptoField
        @ApiModelProperty(value = "邮箱")
        private String email;

        @ApiModelProperty(value = "性别")
        private String sex;
    }

}


