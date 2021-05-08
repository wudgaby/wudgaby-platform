package com.wudgaby.platform.websocket.coder;

import com.alibaba.fastjson.JSON;
import com.wudgaby.platform.websocket.vo.WsMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @ClassName : MessageEncode
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/27 1:26
 * @Desc :   TODO
 */
public class MessageEncode implements Encoder.Text<WsMessage> {

    @Override
    public void destroy() {

    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public String encode(WsMessage msg) throws EncodeException {
        return JSON.toJSONString(msg);
    }
}