package cn.jiudao.rabbitmq.simple;

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
 * @create 2018-07-13 11:21
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQProducer {

    private static final String QUEUE_NAME = "QUEUE_simple";

    public  void producer() throws IOException, TimeoutException {
        // 获取连接
       Connection connection = ConnectionUtils.getConnection();

       // 从连接中创建通道
       Channel channel = connection.createChannel();

       // 创建队列（声明），因为我们要往队列里发送消息，这是为了知道往哪个队列里发送消息
        boolean durable = false;
        boolean exclusive = false;
        boolean autoDelete = false;
        channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, null);

        // 消息内容
        String msg = "Hello Simple QUEUE";

        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        System.out.println("--------- send msg : " + msg);

        channel.close();
        connection.close();
    }

    public static void main(String[] args) {
        try {
            RabbitMQProducer rabbitMQProducer = new RabbitMQProducer();
            rabbitMQProducer.producer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
