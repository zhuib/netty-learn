package netty.dubborpc.provider;

import netty.dubborpc.publicinterface.HelloService;

/**
 * Date: 2021/6/16 12:27
 */
public class HelloServiceImpl implements HelloService {

//    private int count;
    private static int count;
    // 当有消费方调用方法时，就返回一个结果
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息 = " + msg);

        // 根据msg，返回不同的结果
        if (msg != null) {
            return "你好客户端，我已接收到消息 【" + msg +"] 第" + (++count) +"次";
        }else {
            return "你好客户端，我已接收到消息";
        }
    }
}
