package netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * Date: 2021/6/16 13:07
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;  // 上下文
    private String result;  // 返回结果
    private String para;  // 客户端调用方法时，传入的参数

    // 与服务器的连接创建后，就会被调用，这个方法是第一个被调用 （1）
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyClientHandler  channelActive 被调用");
       context = ctx;  // 在其他方法会使用到ctx
    }

    // 接收服务器的数据后，调用方法 (4)
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("NettyClientHandler.channelRead 被调用");
        result = msg.toString();
        System.out.println("result = " + result);
        notify();  // 唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }


    // 被代理对象调用，发送数据给服务器，  --> wait --> 等待被唤醒(channelRead) --> 返回结果 (3) -> (5)
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("NettyClientHandler call01 被调用");
        context.writeAndFlush(para);
        // 进行 wait
        wait(); // 等待channelRead 方法获取到服务器的结果后，唤醒
        System.out.println("NettyClientHandler call02 被调用");
        return result; // 服务方返回结果
    }

    // (2)
    public void setPara(String para) {
        System.out.println("NettyClientHandler setPara 被调用");
        this.para = para;
    }
}
