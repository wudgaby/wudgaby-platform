package com.wudgaby.sign.supoort;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import org.springframework.util.Assert;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;

/**
 * @ClassName : NettyServletInputStream
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/17 0:54
 * @Desc :   TODO
 */
public class NettyServletInputStream extends ServletInputStream {

    private final ByteBufInputStream in;
    private final ByteBuf buffer;

    /**
     * Create a ServletInputStream based on the byteBuff. The byteBuf passed in is wrapped using {@link Unpooled#wrappedBuffer(ByteBuf)} }.
     *
     * @param byteBuf a readable ByteBuf
     */
    public NettyServletInputStream(ByteBuf byteBuf) {
        Assert.isTrue(byteBuf.isReadable(), "BytBuf is not readable!");
        buffer = Unpooled.wrappedBuffer(byteBuf);
        in = new ByteBufInputStream(buffer);
    }

    @Override
    public int read() throws IOException {
        return this.in.read();
    }

    @Override
    public int read(byte[] buf) throws IOException {
        return this.in.read(buf);
    }

    @Override
    public int read(byte[] buf, int offset, int len) throws IOException {
        return this.in.read(buf, offset, len);
    }

    @Override
    public void close() throws IOException {
        buffer.release();
    }

    @Override
    public boolean isFinished() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public boolean isReady() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}
