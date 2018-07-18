package cn.jiudao.rabbitmq.springrabbit.noannotation;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringDirectExchange
 *
 * version 1.0
 *
 * @create 2018-07-18 10:05
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class SpringDirectExchange {
    public static void main(String[] args) throws InterruptedException {
        /**
         * direct 模式，需要一个 routingKey 来匹配队列
         */
        AbstractApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:springconfig/rabbitmq-direct-exchange.xml");
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.convertAndSend("directExchange","item.spring.update", "direct exchange");
        Thread.sleep(1000);
        applicationContext.close();
    }
}
