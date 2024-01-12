package com.wudgaby.sample.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wudgaby.starter.tenant.db.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户表
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
@Accessors(chain = true)
public class SysUser extends TenantEntity implements Serializable {
    /**
     * 状态 0:正常, 1:停用
     */
    private Integer status;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 逻辑删除 0:存在 1:删除
     */
    private Integer deleted;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String sex;

    /**
     * 头像
     */
    private String avatar;
}