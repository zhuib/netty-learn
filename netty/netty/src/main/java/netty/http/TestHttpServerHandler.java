package netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * Date: 2020/11/3 23:49
 * HttpObject 客户端和服务端的相互通讯的数据被封装成 HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    // 读取客户数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断 msg 是不是 httpRequest 请求
        if (msg instanceof HttpRequest) {

            // 每个连接的都会创建自己的pipeline 即每个连接都是独立的
            System.out.println("pipeline  " + ctx.pipeline().hashCode() + "\t    TestHttpServerHandler  " + this.hashCode());
            System.out.println("");
            System.out.println("msg 类型 = " +  msg.getClass());
            System.out.println("客户端地址 = " + ctx.channel().remoteAddress());

            // 获取到
            HttpRequest httpRequest = (HttpRequest) msg;
            // 获取Uri ,过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico， 不做响应" + uri.getPath());
                return;
            }

            // 回复信息给浏览器 [http 协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器",CharsetUtil.UTF_8);

            // 构造一个http的响应 即httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8"); // utf-8 解决乱码
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的response 返回
            ctx.writeAndFlush(response);
        }
    }
}
