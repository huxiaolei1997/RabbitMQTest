package cn.jiudao.rabbitmq.springrabbit;

/**
 * SpringConsumer
 *
 * version 1.0
 *
 * @create 2018-07-17 16:28
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class SpringConsumer {
    public void listen(String springConsumer) {
        System.out.println("消费者收到了一条消息：" + springConsumer);
    }
}
