package cn.jiudao.rabbitmq.topic;

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
 * @create 2018-07-17 10:27
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQProducer {
    /**
     * Topic Exchange
     * 将路由键和某模式进行匹配
     * 此时队列需要绑定到一个模式上。符号 "#" 匹配一个或多个词，符号 "*" 匹配一个词。因此 "adult.#" 能够匹配到 "adult.irs.corporate"，
     * 但是 "adult.*" 只会匹配到 "adult.irs"
     *
     */
    private static final String EXCHANGE_NAME = "exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取到连接以及通道
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 消息内容
        String message = "id=1001";
        channel.basicPublish(EXCHANGE_NAME, "item.delete", null, message.getBytes());
        System.out.println(" [x] Send '" + message + "'");

        channel.close();
        connection.close();
    }
}
