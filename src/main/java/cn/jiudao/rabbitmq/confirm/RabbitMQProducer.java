package cn.jiudao.rabbitmq.confirm;

import cn.jiudao.rabbitmq.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
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
     *
     * RabbitMQ 为我们提供了解决此问题的两种方式
     * 1. 通过 AMQP 事务机制实现，这也是 AMQP 协议层面提供的解决方案
     * 2. 通过将 channel 设置成 confirm 模式来实现
     *
     * 事务机制
     *
     * RabbitMQ 中与事务机制有关的方法有三个： txSelect(), txCommit() 以及 txRollback(), txSelect() 用于将当前 channel 设置
     * 成 transaction 模式，txCommit 用于提交事务，txCommit 提交事务成功了，则消息一定到达 broker 了，如果在 txCommit 执行之前 broker
     *  异常崩溃或者由于其他原因抛出异常，这个时候我们便可以捕获异常通过 txRollback 回滚事务了
     *
     * 但是这种方式很耗时，而且降低了 RabbitMQ 的消息吞吐量，这里我们采用 confirm 模式来实现
     *
     * confirm 模式简介
     *
     * 生成者将信道设置为 confirm 模式，一旦信道进入 confirm 模式，所有在该信道上面发布的信息都会被指派一个唯一的 ID(从 1 开始)
     * ，一旦消息被投递到所有匹配的队列之后，broker 就会发送一个确认给生产者（包含消息的唯一ID），这就使得生产者知道消息已经正确到达目的
     * 队列了，如果消息和队列是可持久化的，那么确认消息会将消息写入磁盘后发出，broker 回传给生产者的确认消息中 deliver-tag 域中包含了确认
     * 消息的序列号，此外 broker 也可以设置 basic.ack 的 multiple 域，表示到这个序列号之前的所有消息都已经得到了处理
     *
     * confirm 模式最大的好处就是他是异步的，一旦发布一条消息，生产者应用程序就可以在等信道返回确认的同时继续发送下一条消息，当消息
     * 最终得到确认后，生产者应用便可以通过回调方法来处理该确认消息，如果 RabbitMQ 因为自身内部错误导致消息丢失，就会发送一条 nack 消息，
     * 生产者应用程序同样可以在回调方法中处理该 nack 消息。
     *
     * confirm 三种模式
     *
     * 普通 confirm 模式 每发送一条消息后，调用 waitForConfirms() 方法，等待服务器端 confirm
     * 批量 confirm 模式 每发送一批消息后，调用 waitForConfirms() 方法，等待服务器端 confirm
     * 异步 confirm 模式 提供一个回调方法，服务端 confirm 了一条或者多条消息后 Client 会回调这个方法
     *
     * 默认采用批量回复模式，并不是每一条消息都要进行回复
     *
     * 注意：
     * transaction 和 confirm 这两种模式是不能共存的
     *
     */
    private static final String EXCHANGE_NAME = "exchange_direct_confirm";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String message = "id=1001的商品删除了";

        // 消息内容
        try {
            // 生产者通过调用 channel 的 confirmSelect 方法将 channel 设置为 confirm 模式
            channel.confirmSelect();

            // 普通 confirm 模式
            //for (int i = 0; i < 10; i++) {
            //    channel.basicPublish(EXCHANGE_NAME, "delete", null ,message.getBytes());
            //    if (channel.waitForConfirms()) {
            //        System.out.println("send msg :" + message + " success!");
            //    } else {
            //        System.out.println("send msg :" + message + " failed!");
            //    }
            //}

            // 批量 confirm 模式
            //for (int i = 0; i < 10; i++) {
            //    channel.basicPublish(EXCHANGE_NAME, "delete", null ,message.getBytes());
            //}
            //if (channel.waitForConfirms()) {
            //    System.out.println("send msg :" + message + " success!");
            //} else {
            //    System.out.println("send msg :" + message + " failed!");
            //}

            // 异步模式
            //channel.basicPublish(EXCHANGE_NAME, "delete", null ,message.getBytes());
            // confirmSet 是我们自己维护的一个消息集合，保存了（未被消费者处理）消息的编号
            final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

            channel.addConfirmListener(new ConfirmListener() {
                // 每回调一次 handleAck() 方法，confirmSet 集合就相应删除一条（multiple=false）或多条（multiple=true）
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    /**
                     * deliveryTag 表示该编号之前（包括该编号）的所有消息都已经收到了
                     */
                    if (multiple) {
                        System.out.println("--multiple true");
                        confirmSet.headSet(deliveryTag + 1).clear();
                    } else {
                        System.out.println("--multiple false");
                        confirmSet.remove(deliveryTag);
                    }
                }

                // 如果 RabbitMQ 因为自身内部错误导致消息丢失，就会发送一条 nack 消息，
                // 生产者应用程序同样可以在该回调方法（handleNack）中处理该 nack 消息。
                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("Nack, SeqNo:" + deliveryTag + ", multiple:" + multiple);
                    if (multiple) {
                        confirmSet.headSet(deliveryTag + 1).clear();
                    } else {
                        confirmSet.remove(deliveryTag);
                    }
                }
            });

            while (true) {
                long nextSeqNo = channel.getNextPublishSeqNo();
                System.out.println("nextSeqNo = " + nextSeqNo);
                channel.basicPublish(EXCHANGE_NAME, "delete", null ,message.getBytes());
                confirmSet.add(nextSeqNo);
                if (nextSeqNo == 10000) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        channel.close();
        connection.close();
    }
}
