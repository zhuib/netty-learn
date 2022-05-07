package netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Date: 2021/6/14 15:22
 */
public class GroupChatServer {

    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            // 编码器
                            pipeline.addLast("encoder",new StringEncoder());

                            // 业务处理器 handler
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });

            System.out.println("netty 服务器启动....");
            // 绑定端口
            ChannelFuture channelFuture = b.bind(port).sync();
            // 关闭 channel 监听关闭
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(8000).run();
    }
}
