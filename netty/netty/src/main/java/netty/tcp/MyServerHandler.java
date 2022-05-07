package netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Date: 2021/6/15 16:50
 */
public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        // 转成字符串
        String message = new String(buffer, Charset.forName("utf-8"));

        System.out.println("服务器接收到的数据 = " + message);
        System.out.println("服务器接收到消息量 = " + (++this.count));

        // 回送随机id
        ByteBuf responseByteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString(), Charset.forName("utf-8"));
        ctx.writeAndFlush(responseByteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
