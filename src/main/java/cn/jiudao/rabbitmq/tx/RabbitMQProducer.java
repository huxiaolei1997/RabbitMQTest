package cn.jiudao.rabbitmq.tx;

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
 * @create 2018-07-17 11:14
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class RabbitMQProducer {
    /**
     * 在 RabbitMQ 中我们可以通过持久化来解决因为服务器异常而导致丢失的问题
     * 除此之外，我们还会遇到另一个问题：生产者将消息发送出去以后，消息到底有没有正确到达 RabbitMQ 服务器呢？如果没有得到处理
     * ，我们是不知道的，（即 RabbitMQ 服务器不会反馈任何消息给生产者），也就是默认的情况下是不知道消息有没有正确到达
     * RabbitMQ 为我们提供了解决此问题的两种方式
     * 1. 通过 AMQP 事务机制实现，这也是 AMQP 协议层面提供的解决方案
     * 2. 通过将 channel 设置成 confirm 模式来实现
     *
     * 事务机制
     * RabbitMQ 中与事务机制有关的方法有三个： txSelect(), txCommit() 以及 txRollback(), txSelect() 用于将当前 channel 设置
     * 成 transaction 模式，txCommit 用于提交事务，txCommit 提交事务成功了，则消息一定到达 broker 了，如果在 txCommit 执行之前 broker
     *  异常崩溃或者由于其他原因抛出异常，这个时候我们便可以捕获异常通过 txRollback 回滚事务了
     *
     *  但是这种方式很耗时，而且降低了 RabbitMQ 的消息吞吐量
     *
     */
    private static final String EXCHANGE_NAME = "exchange_direct_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String message = "id=1001的商品删除了";

        // 消息内容
        try {
            // 开启事务
            channel.txSelect();
            channel.basicPublish(EXCHANGE_NAME, "delete", null ,message.getBytes());
            //int i = 1 / 0;
            System.out.println(" Send '" + message + "'");
            //message = "id=1001的商品更新了";
            //channel.basicPublish(EXCHANGE_NAME, "update", null ,message.getBytes());
            channel.txCommit();
            //System.out.println(" Send '" + message + "'");
        } catch (Exception e) {
            // 回滚数据
            channel.txRollback();
            System.out.println("msg rollback");
        }
//        System.out.println(" Send '" + message + "'");
        channel.close();
        connection.close();
    }
}
