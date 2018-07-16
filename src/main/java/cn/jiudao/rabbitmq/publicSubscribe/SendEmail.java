package cn.jiudao.rabbitmq.publicSubscribe;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * SendEmail
 *
 * version 1.0
 *
 * @create 2018-07-16 16:52
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class SendEmail {
    /**
     * 模拟发送邮件
     */
    private final static String QUEUE_NAME = "queue_fanout_email";
    private final static String EXCHANGE_NAME = "exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取到连接以及通道
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        // 设置同一时刻只发送 1 条消息给消费者
        channel.basicQos(1);

        // 定义一个消费者
        final Consumer consumer = new DefaultConsumer(channel) {
          // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Recv msg : " + message);

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
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
