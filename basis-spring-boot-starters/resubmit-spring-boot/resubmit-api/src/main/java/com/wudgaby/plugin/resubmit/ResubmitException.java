package com.wudgaby.plugin.resubmit;

/**
 * @ClassName : ResubmitException
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2021/3/14 0:30
 * @Desc :
 */
public class ResubmitException extends RuntimeException {
    public ResubmitException(){
        super("重复提交");
    }
}
