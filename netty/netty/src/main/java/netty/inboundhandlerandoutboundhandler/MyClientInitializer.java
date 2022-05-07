package netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Date: 2021/6/16 0:07
 * SocketChannel 是实现的类型
 *
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 加入一个出站的handler 对数据进行一个编码
        pipeline.addLast(new MyLongToByteEncoder());

//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder02());
        // 加入一个自定义的handler ，处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
