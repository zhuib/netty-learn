package proxy.statiz;

/**
 * Date: 2021/6/17 14:02
 *
 * 这种代理方式需要代理对象和目标对象实现一样的接口。
 *
 * 优点：可以在不修改目标对象的前提下扩展目标对象的功能。
 *
 * 缺点：
 *
 * 冗余。由于代理对象要实现与目标对象一致的接口，会产生过多的代理类。
 * 不易维护。一旦接口增加方法，目标对象与代理对象都要进行修改。
 * 举例：保存用户功能的静态代理实现
 */

public class UserDaoProxy implements IUserDao{

    private IUserDao target;
    public UserDaoProxy(IUserDao target) {
        this.target = target;
    }

    @Override
    public void save() {
        System.out.println("开启事务");//扩展了额外功能
        target.save();
        System.out.println("提交事务");
    }

    public static void main(String[] args) {
        //目标对象
        IUserDao target = new UserDao();
        //代理对象
        UserDaoProxy proxy = new UserDaoProxy(target);
        proxy.save();
    }
}