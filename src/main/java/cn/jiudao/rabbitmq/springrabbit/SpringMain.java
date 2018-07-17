package cn.jiudao.rabbitmq.springrabbit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringMain
 *
 * version 1.0
 *
 * @create 2018-07-17 16:36
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class SpringMain {
    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springconfig/rabbit-context.xml");

        // RabbitMQ 模板
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.convertAndSend("myQueue", "Hello");
        //AmqpTemplate amqpTemplate = applicationContext.getBean(AmqpTemplate.class);
        //amqpTemplate.convertAndSend("myQueue", "hello");
        //String foo = (String) amqpTemplate.receiveAndConvert("myQueue");
        // 发送消息
        //rabbitTemplate.convertAndSend("myQueue", "Hello, This is a message send by spring");

        //Thread.sleep(1000);
        //String foo = (String) rabbitTemplate.receiveAndConvert("myQueue");
        //System.out.println(foo);
        applicationContext.close();
    }
}
