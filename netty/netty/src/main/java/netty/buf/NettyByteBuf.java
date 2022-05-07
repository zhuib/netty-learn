package netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Date: 2021/6/14 14:39
 * 1 创建一个ByteBuf 该对象包含一个数组arr 是一个byte[10]
 * 2 在netty的buffer中，不需要使用flip 进行读写切换
 *          底层维护了 readerIndex 和 writerIndex
 * 3 通过readerIndex 和 writerIndex 和capacity ，将buffer分成三个区域
 *  0 ---> readerIndex 已经读取的区域
 *  readerIndex --> writerIndex  可读的区域
 *  writerIndex --> capacity 可写区域
 *
 *
 */
public class NettyByteBuf {

    public static void main(String[] args) {
        // 创建一个ByteBuf 该对象包含一个数组arr 是一个byte[10]
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity = " + buffer.capacity());

        // 不会引起 readerIndex 变化
//        for (int i = 0; i < buffer.capacity(); i++) {
//            buffer.getByte(i);
//        }

//        引起 readerIndex 变化
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.readByte();
        }

        System.out.println("执行完毕~~");
    }
}
