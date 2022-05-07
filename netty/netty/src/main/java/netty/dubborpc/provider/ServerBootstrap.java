package netty.dubborpc.provider;

import netty.dubborpc.netty.NettyServer;

/**
 * Date: 2021/6/16 12:33
 *
 * ServerBootstrap 会启动一个服务提供者，就是NettyServer
 */
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",8000);
    }
}
