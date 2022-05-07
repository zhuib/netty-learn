package netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Date: 2021/6/15 17:22
 */
public class MyMessageDecoder extends ReplayingDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decode 被调用");

        // 需将得到的二进制字节码 -- > MessageProtocol 数据包（对象）
        int length = in.readInt();

        byte[] content = new byte[length];
        in.readBytes(content);

        // 封装成 MessageProtocol 对象，放入 out 传递下一个 handler 业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);

        out.add(messageProtocol);

    }
}
