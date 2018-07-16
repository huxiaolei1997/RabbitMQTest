package cn.jiudao.rabbitmq.workqueues;

import cn.jiudao.rabbitmq.ConnectionUtils;
import cn.jiudao.rabbitmq.simple.RabbitMQConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQProducer
 *
 * version 1.0
 *
 * @create 2018-07-16 14:13
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQProducer {
    /**
     * 我们可以使用多个消费者同时处理多个消息
     * 让它们相互竞争，这样消费者就可以同时处理多条消息了
     * 而 work queues 就是这样的一种队列，可以轻易的通过增加消费者来处理积压的工作
     * 测试结果
     * 消费者 1 和 消费者 2 获取到的消息内容是不同的，同一个消息只能被一个消费者获取
     * 消费者 1 和 消费者 2 获取到的消息数量是一样的
     * 按道理说消费者 1 获取到的要比消费者 2 多
     * 这种方式叫做轮询分发 结果就是不管谁忙谁清闲，都不会给谁多一个任务或者少一个任务，任务总是
     * 你一个我一个的分
     */
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
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

//    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
//        RabbitMQProducer rabbitMQProducer = new RabbitMQProducer();
//        rabbitMQProducer.producer();
////        RabbitMQConsumerOne.consumer1();
////        RabbitMQConsumerTwo.consumer2();
//    }
}
