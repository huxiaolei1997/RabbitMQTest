package cn.jiudao.rabbitmq.simple;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * RabbitMQConsumer
 * <p>
 * version 1.0
 *
 * @create 2018-07-13 13:40
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQConsumer {

    private final static String QUEUE_NAME = "QUEUE_simple";

    // 新方法获取队列中的消息
    public void newRecv(Channel channel) throws IOException {
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" new Received '" + message + "'");
            }
        };
        // 监听队列
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }

    // 旧方法获取队列中的消息
    public void oldRecv(Channel channel) throws IOException, TimeoutException, InterruptedException {
        //定义队列消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //监听队列
        channel.basicConsume(QUEUE_NAME,true,queueingConsumer);
        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("old Recv msg:"+msg);
        }

    }

    public static void main(String[] args) {
        try {
            RabbitMQConsumer rabbitMQConsumer = new RabbitMQConsumer();
            // 获取一个连接
            Connection connection = ConnectionUtils.getConnection();
            Channel channel = connection.createChannel();
            // 旧方法获取队列中的消息
            //rabbitMQConsumer.oldRecv(channel);
            // 新方法获取消息队列中的消息
            rabbitMQConsumer.newRecv(channel);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
