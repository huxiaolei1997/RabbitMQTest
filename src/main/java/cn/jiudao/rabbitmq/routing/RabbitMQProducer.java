package cn.jiudao.rabbitmq.routing;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQProducer
 *
 * version 1.0
 *
 * @create 2018-07-16 17:17
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQProducer {
    private static final String EXCHANGE_NAME = "exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 消息内容
       String message = "id=1001的商品删除了";
       channel.basicPublish(EXCHANGE_NAME, "delete", null ,message.getBytes());
       System.out.println(" Send '" + message + "'");
       message = "id=1001的商品更新了";
       channel.basicPublish(EXCHANGE_NAME, "update", null ,message.getBytes());
       System.out.println(" Send '" + message + "'");
//        System.out.println(" Send '" + message + "'");
       channel.close();
       connection.close();
    }
}
