package cn.jiudao.rabbitmq.workqueues;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQProducerFairDispatch
 *
 * version 1.0
 *
 * @create 2018-07-16 15:59
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQProducerFairDispatch {
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        // 指定一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        int prefetchCount = 1;

        // 在消费者发送确认信号之前，消息队列不发送下一个消息过来，消费者一次只处理一个消息
        // prefetchCount 限制一次只发送一条消息给同一个消费者
        channel.basicQos(prefetchCount);

        for (int i = 0; i < 50; i++) {
            // 消息内容
            String message = "这是第 " + i + " 消息";
            channel.basicPublish("",  QUEUE_NAME, null, message.getBytes());
            System.out.println("当前发送的是第 " + i + " 消息");
            Thread.sleep(i * 10);
        }
        channel.close();
        connection.close();
    }

}
