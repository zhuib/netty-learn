package nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Author: zhuib
 * Date: 2020/10/11 21:28
 * Describe:
 */
public class NIOFileChannel {
    public static void main(String[] args) throws Exception{
        String str = "hello, zhuib";
        // 创建一个输出流 -》channel
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");

        // 通过 fileOutputStream 获取 对应的 fileChannel
        // 这个 fileChannel 真实 类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将str 放入 byteBuffer
        byteBuffer.put(str.getBytes());

       // 对 byteBuffer 进行flip
        byteBuffer.flip();

        // 将byteBuffer 数据写入到 fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();


    }
}
