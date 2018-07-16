package cn.jiudao.rabbitmq.publicSubscribe;

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
 * @create 2018-07-16 16:43
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQProducer {
    private static final String EXCHANGE_NAME = "exchange_fanout";

    /**
     * 之前的 simple 队列和 work 对列，都是一个消息只能被一个消费者处理，如果此时我想一个消息被多个消费者
     * 处理呢，这时我们就需要用到消息中的 发布订阅 模型
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        // 声明交换机 转发器
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");// fanout 分裂

        // 消息内容
        String message = "Hello PB";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" Send '" + message + "'");

        channel.close();
        connection.close();
    }
}
