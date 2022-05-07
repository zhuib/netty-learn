package netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.dubborpc.publicinterface.HelloService;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 2021/6/16 13:25
 */
public class NettyClient {

    // 创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler client;
    private int count = 0;

    // 使用代理模式，获取一个代理对象
    public Object getBean(Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[] {serviceClass},((proxy, method, args) -> { // 这部分代码，客户端每调用一个hello，就会进入到该代码
            System.out.println("(proxy, method, args) 进入...." + (++count));
            if (client == null) {
                initClient();
            }
            // 设置要发给服务端的信息
            // providerName: 协议头
//            args[0] 客户端调用 api hello() 方法的参数
            client.setPara(providerName + args[0]);
            // 把client 提交到 线程池中去执行 ，并得到结果
            Object o = executor.submit(client).get();
            System.out.println("client 提交到------"+o);
            return o;
        }));
    }

    // 初始化客户端
    private static void initClient() {
        client = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(client);
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8000).sync();
//            channelFuture.channel().closeFuture().sync();
        }catch (Exception e) {
            e.printStackTrace();
        }/*finally {
            group.shutdownGracefully();
        }*/
    }
}
