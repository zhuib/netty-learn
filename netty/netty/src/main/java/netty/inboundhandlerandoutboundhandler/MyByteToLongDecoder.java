package netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Date: 2021/6/15 23:52
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     *
     * decoder 会根据接收的数据，被调用多次，知道确定没有新的元素被添加到list
     * 或者是 ByteBuf 没有更多的可读字节为止
     * 如果list out 不为空，就会将list 的内容传递给下一个 channelInBoundHandler 处理，该处理器的方法也会被调用多次
     *
     * @param ctx 上下文对象
     * @param in  入站 的 ByteBuf
     * @param out  list 集合，将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("MyByteToLongDecoder decode 被调用");
        // long 需要 8个字节，需要判断有 8个字节，才能读取一个 long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
