package netty.dubborpc.consumer;

import netty.dubborpc.netty.NettyClient;
import netty.dubborpc.publicinterface.HelloService;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/6/16 13:45
 */
public class ClientBootstrap {
    //定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) {
        // 创建一个消费者
        NettyClient consumer = new NettyClient();

        // 创建代理对象
        HelloService service = (HelloService) consumer.getBean(HelloService.class, providerName);

        for (;;){
        try{
            TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
            // 通过代理对象调用服务提供者的方法（服务）
        String res = service.hello("你好 dubbo ~");
        System.out.println("调用的结果 res = " + res);
        }
    }
}
