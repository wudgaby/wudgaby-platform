package com.wudgaby.platform.message.api.enmus;

import com.wudgaby.platform.core.enums.ICode;
import lombok.Getter;

/**
 * @ClassName : SmsTmplEnum
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 9:41
 * @Desc :   邮件模板
 */
@Getter
public enum MailTmplEnum implements ICode<Integer> {
    /*** 注册模板.两个参数. 验证码, 过期时间(分钟) */
    REGISTER_TMPL(1, "注册模板", "注册", "register.ftl"),
    /*** 修改密码模板.两个参数. 验证码, 过期时间(分钟) */
    UPDATE_PWD_TMPL(2, "修改密码模板", "修改密码", "updatePwd.ftl"),
    ;

    private Integer tmplId;
    private String desc;
    private String subject;
    private String tmplFileName;

    MailTmplEnum(Integer tmplId, String desc, String subject, String tmplFileName){
        this.tmplId = tmplId;
        this.desc = desc;
        this.subject = subject;
        this.tmplFileName = tmplFileName;
    }

    @Override
    public Integer getCode() {
        return tmplId;
    }

    @Override
    public String getName() {
        return name();
    }
}
