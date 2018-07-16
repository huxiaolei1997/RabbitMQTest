package cn.jiudao.rabbitmq.workqueues;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQConsumerOne
 *
 * version 1.0
 *
 * @create 2018-07-16 14:25
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQConsumerTwo {

    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接以及 mq`通道
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

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
                }
            }
        };
        boolean autoAck = true; // 消息的确认模式设置为自动应答
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

//    private static void doWork(String task) throws InterruptedException {
//        Thread.sleep(2000);
//    }

}
