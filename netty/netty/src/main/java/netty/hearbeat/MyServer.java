package netty.hearbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/6/11 19:58
 */
public class MyServer {
    public static void main(String[] args) throws Exception{
        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //8个NioEventLoop
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))  // 给bossGroup 添加一个日志记录处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            /**
                             * long readerIdleTime : 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                             * long writerIdleTime ： 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                             * long allIdleTime ： 表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                             *
                             *  当 IdleStateEvent 触发后，就会传递给管道的下一个 handler 去处理，通过调用（触发）下一个handler 的userEventTriggered，
                             *  在该方法中去处理 IdleStateEvent 读空闲，写空闲，读写空闲
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            // 启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(8000).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
