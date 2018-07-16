package cn.jiudao.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ConnectionUtils
 *
 * version 1.0
 *
 * @create 2018-07-16 10:17
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class ConnectionUtils {
    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务地址
        factory.setHost("127.0.0.1");
        // 端口
        factory.setPort(5672);
        // 设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("/vhost_hxl");
        factory.setUsername("hxl");
        factory.setPassword("hxl2580");

        // 获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
