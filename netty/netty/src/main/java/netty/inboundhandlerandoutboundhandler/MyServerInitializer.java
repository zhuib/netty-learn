package netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Date: 2021/6/15 23:50
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 两个编解码器是独立工作
        // 入站的handler 进行解码
//        pipeline.addLast(new MyByteToLongDecoder());  // 解码器
        pipeline.addLast(new MyByteToLongDecoder02());  // 解码器
        // 出站的handler 进行解码
        pipeline.addLast(new MyLongToByteEncoder());  // 编码器

        pipeline.addLast(new MyServerHandler());
    }
}
