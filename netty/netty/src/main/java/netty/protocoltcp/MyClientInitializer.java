package netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Date: 2021/6/15 16:39
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageEncoder());  // 编码器
        pipeline.addLast(new MyMessageDecoder());  /// 解码器
        pipeline.addLast(new MyClientHandler());
    }
}
