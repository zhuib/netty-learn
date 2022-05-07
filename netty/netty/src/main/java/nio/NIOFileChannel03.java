package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Author: zhuib
 * Date: 2020/10/11 21:44
 * Describe:
 */
public class NIOFileChannel03 {

    public static void main(String[] args) throws Exception{

        FileInputStream fileInputStream = new FileInputStream("netty\\1.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("netty\\2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        while (true) {

//            这里有一个重要的操作， 一定不要忘了
            /**
             * public final Buffer clear() {
             *         position = 0;
             *         limit = capacity;
             *         mark = -1;
             *         return this;
             *     }
             */
            byteBuffer.clear(); // 清空buffer 即重置buffer，不然这里的position 和limit的值是一样的 导致读到的长度是 0
            int read = fileChannel01.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) { // 表示读完
                break;
            }

            // 将buffer 中的数据写入到fileChannel02 ---》 2.txt
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }

        // 关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();


    }
}
