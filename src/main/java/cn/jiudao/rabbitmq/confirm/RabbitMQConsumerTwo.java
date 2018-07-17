package cn.jiudao.rabbitmq.confirm;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQConsumerTwo
 *
 * version 1.0
 *
 * @create 2018-07-17 11:14
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQConsumerTwo {
    private static final String QUEUE_NAME = "work_queue_confirm";
    private static final String EXCHANGE_NAME = "exchange_direct_confirm";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "update");
//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "delete");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "delete");
        // 同一时刻只发一条消息给消费者
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [2] Recv msg :'" + message + "'");
                } finally {
                    System.out.println("[2] done");
                    // 手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
