package nio.nioSeletor;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Date: 2020/10/29 11:02
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{
        // 创建ServerSocketChannel - > ServerSocket

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个Selector对象
        Selector selector = Selector.open();

        // 绑定一个端口6666 在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把 serverSocketChannel 注册到selector 关心的事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {

            // 等待1秒，如果没有事件发生，返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            // 如果返回的 > 0 就获取到相关的selectionKey 集合
            // 如果返回的 > 0 表示已经获取到关注的事件
            // selector, selectedKey() 返回关注事件的集合
            //  通过selectionKey  反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历 Set<SelectionKey>， 使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                // 获取到 selectionKey
                SelectionKey key = keyIterator.next();
                // 根据key 对应的通道发生的事件做相应处理
                if (key.isAcceptable()) {  // 如果是OP_ACCEPT 有新的客户端连接
                    // 该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("接收客户端连接成功 生成一个 socketChannel   " + socketChannel.hashCode());
                    // 设置非阻塞
                    socketChannel.configureBlocking(false);
                    // 将 socketChannel 注册到selector， 关注事件为OP_READ, 同时给socketChannel 关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if (key.isReadable()) { // 发生OP_READ
                    // 通过key 反向获取到对应Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端" + new String(buffer.array()));
                }

                // 手动从集合中移除当前selectionKey 防止重复操作
                keyIterator.remove();
            }


        }
    }
}
