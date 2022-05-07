package netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Date: 2020/11/3 23:50
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        // 向管道加入处理器

        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个netty 提供的httpServerCodec codec -> [ encoder - decoder]
        // HttpServerCodec
        // 1. HttpServerCodec 是netty 提供的处理http的 编 - 解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        // 增加一个定义的handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());

        System.out.println("OK");
    }
}
