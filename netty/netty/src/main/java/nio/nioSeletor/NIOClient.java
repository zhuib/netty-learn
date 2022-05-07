package nio.nioSeletor;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Date: 2020/10/29 13:24
 */
public class NIOClient {
    public static void main(String[] args) throws Exception{

        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 提供服务器端的 IP 和 端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {

            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作...");
            }
        }
        // 如果连接成功，就发送数据
        String str = "hello aron";
//        wrap是根据字符串的字节长度，创建大小
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 向channel发送数据
        socketChannel.write(buffer);

        System.in.read();

    }
}
