package netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * Date: 2021/6/15 16:41
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端发送10 条数据 "哪里都去不了，只能敲代码" 编号
        for (int i = 0; i < 5; i++) {
           String mes = "哪里都去不了，只能敲代码";
            byte[] content = mes.getBytes(Charset.forName("utf-8"));
            int length = content.length;

            // 创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("客户端接收到消息如下");
        System.out.println("长度 = " + len);
        System.out.println("内容 = " + new String(content, Charset.forName("utf-8")));

        System.out.println("客户端接收到消息数量 = " + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
