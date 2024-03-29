package com.wudgaby.platform.websocket.coder;

import cn.hutool.json.JSONUtil;
import com.wudgaby.platform.websocket.vo.WsMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/27 1:26
 * @Desc :
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
        return JSONUtil.toJsonStr(msg);
    }
}