package com.wudgaby.platform.message.api.enmus;

import com.wudgaby.platform.core.enums.ICode;
import lombok.Getter;

/**
 * @ClassName : SmsTmplEnum
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 9:41
 * @Desc :   短信模板
 */
@Getter
public enum SmsTmplEnum implements ICode<Integer> {
    /** 注册模板 一个参数. 验证码 */
    REGISTER_TMPL(247298, "注册模板."),
    /** 修改密码模板,一个参数 验证码 */
    UPDATE_PWD_TMPL(250245, "修改密码模板.");

    private Integer tmplId;
    private String desc;

    SmsTmplEnum(Integer tmplId, String desc){
        this.tmplId = tmplId;
        this.desc = desc;
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
