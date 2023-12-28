package com.wudgaby.platform.netty.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/28 11:14
 * @Desc :   https://www.cnblogs.com/throwable/p/11619080.html
 */
// 解码器
public class ChineseMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        long id = in.readLong();
        int length = in.readInt();
        CharSequence charSequence = in.readCharSequence(length, StandardCharsets.UTF_8);
        ChineseMessage message = new ChineseMessage();
        message.setId(id);
        message.setMessage(charSequence.toString());
        out.add(message);
    }
}