package com.wudgaby.platform.websocket.coder;

import cn.hutool.json.JSONUtil;
import com.wudgaby.platform.websocket.vo.WsMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/27 1:25
 * @Desc :
 */
public class MessageDecode implements Decoder.Text<WsMessage> {

    @Override
    public void destroy() {

    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public WsMessage decode(String msg) throws DecodeException {
        return JSONUtil.toBean(msg, WsMessage.class);
    }

    @Override
    public boolean willDecode(String msg) {
        return true;
    }

}