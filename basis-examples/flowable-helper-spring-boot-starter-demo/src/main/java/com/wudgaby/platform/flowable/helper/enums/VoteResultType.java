package com.wudgaby.platform.flowable.helper.enums;

/**
 * @ClassName : VoteResultType
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/19 15:13
 * @Desc :
 */
public enum VoteResultType {
    /**
     * 一票通过
     */
    ONE_VOTE_PASS,
    /**
     * 一票否决
     */
    ONE_VOTE_VETO,
    /**
     * 比例通过
     */
    PERCENT,
    /**
     * 所有通过
     */
    ALL_PASS,
    /**
     * 或签 一名负责人通过或拒绝即可
     */
    OR_SIGN,
    ;

}
