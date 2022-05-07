package netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2020/11/2 23:00
 * 自定义一个Handler 需要继承netty 规定好的某个HandlerAdapter（规范）
 *
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /*
    读取（这里可以读取客户端发送的消息）
    ChannelHandlerContext ctx 上下文对象，含有 管道pipeline 通道channel 地址
    Object msg 就是客户端发送的数据 默认是 Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        System.out.println("Server ctx : " + ctx);
//        Channel channel = ctx.channel();
//        System.out.println("channel : "+ channel);
//        ChannelPipeline pipeline = ctx.pipeline();
//        System.out.println("pipeline : "+ pipeline);

        // 比如这里执行一个非常耗时的业务  --> 异步执行 ---> 提交该channel 对应的 NIOEventLoop 的taskQueue 中
/*        Thread.sleep(10 * 1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~2", CharsetUtil.UTF_8));
        System.out.println("go on ...");*/

        // 解决方案一 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(5 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~2", CharsetUtil.UTF_8));


                }catch (Exception e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        });

        // 可以提交多个任务到 taskQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~3", CharsetUtil.UTF_8));

                }catch (Exception e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        });

        // 用户自定义定时任务 --> 该任务是提交到 scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~4", CharsetUtil.UTF_8));

                }catch (Exception e) {
                    System.out.println("发生异常" + e.getMessage());
                }

            }
        }, 5, TimeUnit.SECONDS);

        // 模仿消息推送
        System.out.println("go on ...");


        /*        System.out.println("Server ctx = " + ctx);
        // 将msg 转成一个ByteBuf
        // ByteBuf 是netty 提供的，不是 NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是：" + buf .toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());*/
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓存，并刷新
        // 一般将，需要对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~1", CharsetUtil.UTF_8));
    }

    // 处理异常 一般是需要关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
