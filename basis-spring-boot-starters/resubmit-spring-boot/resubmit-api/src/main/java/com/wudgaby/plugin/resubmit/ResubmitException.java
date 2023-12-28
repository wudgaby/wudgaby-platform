package com.wudgaby.plugin.resubmit;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2021/3/14 0:30
 * @Desc :
 */
public class ResubmitException extends RuntimeException {
    public ResubmitException(){
        super("重复提交");
    }

    public ResubmitException(String message){
        super(message);
    }
}
