package netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Date: 2021/6/16 0:18
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip=" +ctx.channel().remoteAddress());
        System.out.println("收到服务器消息 = " + msg);
    }

    // 重写 channelActive 发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);

        /**分析  Unpooled.copiedBuffer() 方式不进行 encoder 处理
         * 1. "abcdabcdabcdabcd" 是16 个字节
         * 2. 该处理器的前一个handler 是 MyLongToByteEncoder
         * 3. MyLongToByteEncoder 的父类 MessageToByteEncoder
         * 4 父类 MessageToByteEncoder 的部分源码 分析如下
         *  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
         *         ByteBuf buf = null;
         *         try {
         *             if (acceptOutboundMessage(msg)) { // 判断当前msg 是不是应该被处理的类型，如果是就处理，不是就跳过 encode
         *                 @SuppressWarnings("unchecked")
         *                 I cast = (I) msg;
         *                 buf = allocateBuffer(ctx, cast, preferDirect);
         *                 try {
         *                     encode(ctx, cast, buf);
         *                 } finally {
         *                     ReferenceCountUtil.release(cast);
         *                 }
         *
         *                 if (buf.isReadable()) {
         *                     ctx.write(buf, promise);
         *                 } else {
         *                     buf.release();
         *                     ctx.write(Unpooled.EMPTY_BUFFER, promise);
         *                 }
         *                 buf = null;
         *             } else {
         *                 ctx.write(msg, promise);
         *             }
         *         }
         *
         * 因此我们重写 Encoder 是要注意传入的数据类型和处理的数据类型一致
         * 由于我们编码的是long类型而 这里使用 Unpooled.copiedBuffer() 是字节数组
         */
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
