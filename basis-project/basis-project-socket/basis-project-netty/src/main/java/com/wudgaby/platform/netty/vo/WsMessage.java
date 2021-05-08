package com.wudgaby.platform.netty.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName : WebMessage
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/26 19:12
 * @Desc :   TODO
 */
@Data
public class WsMessage implements Serializable {

    public enum MsgType {
        NOTICE,
        MSG,
        ;
    }

    private int cmd;
    private String name;
    private String msg;
    private int onlineNum;
    /**
     * 1:公告, 2: 消息
     */
    private MsgType type;

    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date date;
}