package com.wudgaby.platform.netty.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/28 11:14
 * @Desc :
 */
public class ChineseMessageEncoder extends MessageToByteEncoder<ChineseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ChineseMessage target, ByteBuf out) throws Exception {
        out.writeLong(target.getId());
        String message = target.getMessage();
        int writerIndex = out.writerIndex();
        out.writeInt(0);
        int length = ByteBufUtil.writeUtf8(out, message);
        out.setInt(writerIndex, length);
    }
}