package netty.codec2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * Date: 2020/11/2 10:44
 */
public class NettyServer {

    public static void main(String[] args) throws Exception{
        // 创建 bossGroup 和 workerGroup
        // 创建两个线程组  bossGroup 和 workerGroup
        // bossGroup 只是处理连接请求，真正的和客户端业务处理 会交给 workerGroup 完成
        // 两个都是无限循环
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class)  // 使用NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
//                    .handler(null)  // handler 只作用于 bossGroup， childHandler 对应 workerGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() {  // 创建一个通道初始对象（匿名对象）
                //给pipeline 设置处理器
                @Override
                protected void initChannel(SocketChannel ch) throws Exception{

                    ChannelPipeline pipeline = ch.pipeline();
                    // 传入解码的对象
                    pipeline.addLast("decoder",new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
                    pipeline.addLast(new NettyServerHandler());
                }
            });  // 给我们的workerGroup 的 EventLoop 对应的管道设置处理器

            System.out.println(".....服务器 is ready....");

            // 默认是异步，这里设成同步
            // 绑定一个端口并且同步，生成了一个ChannelFuture 对象
            // 启动服务器（并绑定端口）
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // 由于 cf 是ChannelFuture类型 future-listen 即对future 进行监听
            // 给cf 注册监听器 监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                        if (cf.isSuccess()) {
                            System.out.println("绑定端口6668 成功");
                        } else {
                            System.out.println("绑定端口6668 失败");
                        }
                }
            });
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
