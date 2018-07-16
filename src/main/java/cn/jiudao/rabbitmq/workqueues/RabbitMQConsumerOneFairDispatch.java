package cn.jiudao.rabbitmq.workqueues;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQConsumerOneFairDispatch
 *
 * version 1.0
 *
 * @create 2018-07-16 16:05
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQConsumerOneFairDispatch {
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接以及 mq`通道
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 保证一次只分发一个
        channel.basicQos(1);
        // 定义第一个消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("consumer 1 Received  '" + message + "'");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        /**
         * autoAck = false;
         * 关闭消息的自动应答，如果此时当前消费者被 kill 了，那么当前的消息应该被分发到另外一个消费者去处理，
         * 为了确保消息不会丢失，我们应该关闭消息的自动应答，等到消费者发送一个消息应答之后，告诉 RabbitMQ 这个消息已经被处理完毕了，
         * 可以从内存中删除了
         *
         * autoAck = true;
         * 消息发送给消费者之后，就会从内存中删除，如果这个时候，我们杀死当前的处理该消息的消费者，那么我们会丢失当前正在处理的消息，
         * 也就是丢失这个已经分发但尚未处理的消息
         */
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
