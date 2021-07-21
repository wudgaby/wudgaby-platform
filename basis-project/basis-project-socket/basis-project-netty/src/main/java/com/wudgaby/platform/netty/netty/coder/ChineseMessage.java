package com.wudgaby.platform.netty.netty.coder;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName : ChineseMessage
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/28 11:15
 * @Desc :   
 */
@Data
public class ChineseMessage implements Serializable {
    private long id;
    private String message;
}
