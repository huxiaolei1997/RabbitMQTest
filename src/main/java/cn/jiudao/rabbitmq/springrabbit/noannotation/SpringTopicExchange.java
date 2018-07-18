package cn.jiudao.rabbitmq.springrabbit.noannotation;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringTopicExchange
 *
 * version 1.0
 *
 * @create 2018-07-18 13:04
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class SpringTopicExchange {
    /**
     * topic 模式，根据指定的表达式来匹配并发送消息到匹配的队列
     */
    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springconfig/rabbitmq-topic-exchange.xml");

        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);

        //rabbitTemplate.convertAndSend("topicExchange", "item.spring.update", "id=1001的商品被更新了");

        rabbitTemplate.convertAndSend("topicExchange", "item.spring.delete", "id=1001的商品被删除了");
        Thread.sleep(1000);
        applicationContext.close();
    }
}
