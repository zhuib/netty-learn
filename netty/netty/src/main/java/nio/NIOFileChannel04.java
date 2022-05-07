package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Author: zhuib
 * Date: 2020/10/11 22:00
 * Describe:
 */
public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception{

        // 创建相关的流
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhuib\\Desktop\\课表.png");
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\zhuib\\Desktop\\课表02.png");

        // 获取各个流对应的fileChannel

        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        // 使用transferForm 完成拷贝
        destCh.transferFrom(sourceCh, 0, sourceCh.size());

        // 关闭相关通道和流
        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
}
