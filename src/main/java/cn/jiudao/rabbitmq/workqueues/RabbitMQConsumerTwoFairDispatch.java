package cn.jiudao.rabbitmq.workqueues;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQConsumerTwoFairDispatch
 *
 * version 1.0
 *
 * @create 2018-07-16 16:05
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQConsumerTwoFairDispatch {
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接以及 mq`通道
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.basicQos(1);
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 定义第一个消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("consumer 2 Received  '" + message + "'");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        boolean autoAck = false; // 关闭消息的自动应答
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

}
