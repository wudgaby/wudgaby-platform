package com.wudgaby.platform.netty.netty.coder;

import lombok.Data;

import java.io.Serializable;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/28 11:15
 * @Desc :   
 */
@Data
public class ChineseMessage implements Serializable {
    private long id;
    private String message;
}
