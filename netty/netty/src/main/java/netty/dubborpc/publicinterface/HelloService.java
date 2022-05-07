package netty.dubborpc.publicinterface;

/**
 * Date: 2021/6/16 12:25
 *
 * 接口，是服务提供方和消费都需要
 */
public interface HelloService {

    String hello(String msg);
}
