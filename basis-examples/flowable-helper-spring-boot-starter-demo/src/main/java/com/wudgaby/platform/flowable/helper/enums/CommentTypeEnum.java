package com.wudgaby.platform.flowable.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : bruce.liu
 * @title: : CommentTypeEnum
 * @projectName : flowable
 * @description: 审批意见的类型
 * @date : 2019/11/2412:53
 */
@Getter
@AllArgsConstructor
public enum CommentTypeEnum {
    /**
     * 名称
     */
    SP("审批"),
    BH("驳回"),
    CH("撤回"),
    TH("退回"),

    ZC("暂存"),
    QS("签收"),
    QXQS("取消签收"),
    WP("委派"),
    ZH("知会"),
    ZY("转阅"),
    YY("已阅"),
    ZB("转办"),
    QJQ("前加签"),
    HJQ("后加签"),
    XTZX("系统执行"),
    TJ("提交"),
    CXTJ("重新提交"),
    SPJS("审批结束"),
    LCZZ("流程终止"),
    SQ("授权"),
    CFTG("重复跳过"),
    XT("协同"),
    PS("评审"),

    TJ_CASE("录入警情"),
    TJ_EXAMINE("提交审批"),

    SP_PASS("审批通过"),
    SP_REJECT("审批不通过"),
    SP_ABSTAIN("审批弃权"),

    CX("撤销"),
    ZZ("终止"),
    NOBODY_SKIP("无人处理"),
    ;


    private String name;

    /**
     * 通过type获取Msg
     *
     * @param type
     * @return
     * @Description:
     */
    public static String getEnumMsgByType(String type) {
        for (CommentTypeEnum e : CommentTypeEnum.values()) {
            if (e.toString().equals(type)) {
                return e.name;
            }
        }
        return "";
    }
}
