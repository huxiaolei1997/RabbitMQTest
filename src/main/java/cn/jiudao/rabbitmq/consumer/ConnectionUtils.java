package cn.jiudao.rabbitmq.consumer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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
	private static String HOST;
	private static Integer PORT;
	private static String VHOST;
	private static String USERNAME;
	private static String PASSWORD;
	// 断开连接时重新连接的时间间隔（单位 s ）
	private static Long RETRYINTERVAL;
	private static Connection connection;

	static {
		InputStream inputStream = ConnectionUtils.class.getResourceAsStream("/rabbitmq.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			HOST = properties.getProperty("rabbitmq.host");
			PORT = Integer.valueOf(properties.getProperty("rabbitmq.port"));
			VHOST = properties.getProperty("rabbitmq.vhost");
			USERNAME = properties.getProperty("rabbitmq.username");
			PASSWORD = properties.getProperty("rabbitmq.password");
			RETRYINTERVAL = Long.valueOf(properties.getProperty("rabbitmq.retryInterval"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Long getRETRYINTERVAL() {
		return RETRYINTERVAL;
	}

    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务地址
        factory.setHost(HOST);
        // 端口
        factory.setPort(PORT);
        // 设置账号信息，用户名、密码、vhost
        factory.setVirtualHost(VHOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        // 设置 rabbitmq 断开连接时 不自动重新建立连接
        factory.setAutomaticRecoveryEnabled(false);
		//factory.setConnectionTimeout(30000);

        // 获取连接
		connection = factory.newConnection();
		connection.addShutdownListener(new Consumer());
        System.out.println("获取RabbitMQ连接成功！");
        return connection;
    }
}
