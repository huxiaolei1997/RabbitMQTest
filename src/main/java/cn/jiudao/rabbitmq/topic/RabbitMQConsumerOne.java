package cn.jiudao.rabbitmq.topic;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQConsumerOne
 *
 * version 1.0
 *
 * @create 2018-07-17 10:32
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQConsumerOne {

    private static final String QUEUE_NAME = "queue_topic_1";
    private static final String EXCHANGE_NAME = "exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "item.#");

        // 同一时刻RabbitMQ服务器只会发送一条消息给消费者
        channel.basicQos(1);

        // 定义队列的消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [1] Recv msg:" + message);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [1] done");
                    // 手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        boolean autoAck = false; // 关闭消息的自动应答，改为手动应答
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
