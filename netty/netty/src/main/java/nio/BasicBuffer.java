package nio;

import java.nio.IntBuffer;

/**
 * Author: zhuib
 * Date: 2020/10/11 8:45
 * Describe:
 */
public class BasicBuffer {
    public static void main(String[] args) {

        // 创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

//        向buffer存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 将buffer转换，读写切换（即在写与读之间进行转换）
        intBuffer.flip();

        // 获取数据
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
