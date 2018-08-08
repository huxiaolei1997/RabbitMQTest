package cn.jiudao.rabbitmq.consumer;

import cn.jiudao.rabbitmq.JdbcUtils;
import cn.jiudao.rabbitmq.consumer.ConnectionUtils;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Consumer
 *
 * version 1.0
 *
 * @create 2018-08-02 14:34
 *
 * @copyright huxiaolei1997@gmail.com
 */
@Component
public class Consumer implements ShutdownListener{
	// 交换器名
	private static final String EXCHANGE_NAME = "alert";
	// 队列名
	private static final String QUEUE_NAME = "telesignalling";

	private ScheduledExecutorService scheduledExecutorService;

	private Connection connection;

	private Channel channel;

	private JdbcUtils jdbcUtils;

	private Logger logger = LoggerFactory.getLogger(getClass());

    public void setJdbcUtils(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

	protected void run() {
		this.runConsumer();
	}

	@Override
	public void shutdownCompleted(ShutdownSignalException e) {
		System.out.println("rabbitmq 消息队列断开连接，60s 之后准备重新连接");
		reConnect();
	}

	private void reConnect() {
		System.out.println("调用 reConnect()");
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				ApplicationContext context = new ClassPathXmlApplicationContext("spring-rabbitmq.xml");
				Consumer consumer = (Consumer) context.getBean("consumer");
				consumer.runConsumer();
			}
		}, 6000);
	}


	private void runConsumer() {
		try {
			// 获取到连接以及通道
			connection = ConnectionUtils.getConnection();
			channel = connection.createChannel();
			// 声明队列
			channel.queueDeclare(QUEUE_NAME, true, false, false, null);

			// 绑定队列到交换机
			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

			// 设置同一时刻只发送 1 条消息给消费者
			channel.basicQos(1);
			final com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
				// 消息到达，触发这个方法
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					logger.info("Recv msg : " + message);
					System.out.println("Recv msg : " + message);
					channel.basicAck(envelope.getDeliveryTag(), false);
					try {
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
					/**
					 * 在这里对消息作进一步处理
					 */
				}
			};
			// 设置手动 确认消息
			boolean autoAck = false;
			channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		} catch (Exception e) {
			// 发生异常时，60s 之后重新连接 RabbitMQ 服务器
			reConnect();
		}
	}


	public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-rabbitmq.xml");
        Consumer consumer = (Consumer) context.getBean("consumer");
        consumer.run();
	}
}
