package netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Date: 2020/11/3 23:49
 */
public class TestServer {

    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                           .channel(NioServerSocketChannel.class)
                            .childHandler(new TestServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8888)).sync();

            System.out.println("start.....");
            channelFuture.channel().closeFuture().sync();


        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
