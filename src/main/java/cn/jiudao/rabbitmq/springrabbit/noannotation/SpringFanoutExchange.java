package cn.jiudao.rabbitmq.springrabbit.noannotation;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringFanoutExchange
 *
 * version 1.0
 *
 * @create 2018-07-18 10:05
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class SpringFanoutExchange {
    public static void main(String[] args) throws InterruptedException {
        /**
         * fanout 模式，不用匹配 routingKey，RabbitMQ 把消息发送到绑定到该交换机的所有队列上
         */
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springconfig/rabbitmq-fanout-exchange.xml");

        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);

        rabbitTemplate.convertAndSend( "fanout exchange");

        Thread.sleep(1000);
        applicationContext.close();
    }
}
