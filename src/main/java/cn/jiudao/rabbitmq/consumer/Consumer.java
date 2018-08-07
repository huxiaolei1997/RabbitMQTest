//package cn.jiudao.rabbitmq.consumer;
//
//import cn.jiudao.rabbitmq.JdbcUtils;
//import com.rabbitmq.client.*;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
///**
// * Consumer
// *
// * version 1.0
// *
// * @create 2018-08-02 14:34
// *
// * @copyright huxiaolei1997@gmail.com
// */
//@Transactional
//public class Consumer implements ShutdownListener {
//	private static final String EXCHANGE_NAME = "alert";
//	// 队列名
//	private static final String QUEUE_NAME = "telesignalling";
//
//	//private ScheduledExecutorService scheduledExecutorService;
//	private Connection connection;
//	private Channel channel;
//
//	private JdbcUtils JdbcUtils;
//
//    public void setJdbcUtils(JdbcUtils jdbcUtils) {
//        this.JdbcUtils = jdbcUtils;
//    }
//
//	public void run() {
//		try {
//			// 获取到连接以及通道
//			connection = ConnectionUtils.getConnection();
//			channel = connection.createChannel();
//			// 声明队列
//			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//
//			// 绑定队列到交换机
//			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
//
//			// 设置同一时刻只发送 1 条消息给消费者
//			channel.basicQos(1);
//			// 定义一个消费者
//			final com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
//				// 消息到达，触发这个方法
//				@Override
//				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
////					String message = new String(body, "UTF-8");
////					System.out.println("Recv msg : " + message + "channel Number : " + channel.getChannelNumber());
////                    //JdbcUtils.getConnection();
////                    // 增加数据
////                    //List<Object> paramsInsert = new ArrayList<Object>();
////                    //paramsInsert.add(message);
////                    //paramsInsert.add(23);
////                    try {
//////                        boolean resultInsert = JdbcUtils.updateByPreparedStatement("insert into message(message) values(?)", paramsInsert);
//////                        JdbcUtils.releaseConn();
////                        Thread.sleep(5000);
////					} catch (InterruptedException e1) {
////						e1.printStackTrace();
////					}
////                    channel.basicAck(envelope.getDeliveryTag(), false);
//					String message = new String(body, "UTF-8");
//					System.out.println("Recv msg : " + message + "channel Number : " + channel.getChannelNumber());
//					channel.basicAck(envelope.getDeliveryTag(), false);
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//						//reConnect();
//					}
//				}
//			};
//			boolean autoAck = false;
//			channel.basicConsume(QUEUE_NAME, autoAck, consumer);
//            //System.out.println("数据已被插入到数据中");
//		} catch (Exception e) {
//			System.out.println("消息队列发生了一个异常，" + e.getMessage());
//			reConnect();
//		}
//	}
//
//	@Override
//	public void shutdownCompleted(ShutdownSignalException e) {
//        //throw new RuntimeException("消息队列断开连接");
//		System.out.println("rabbitmq 消息队列断开连接，60s 之后准备重新连接");
//		reConnect();
//	}
//
//	private void reConnect() {
//		System.out.println("调用 reConnect()");
////		try {
////			// 等待60s 之后开始重新连接（这里指的是60s 之后开始连接并不是到了60s之后马上开始连接，具体还是要看java虚拟机的调度）
////			Thread.sleep(ConnectionUtils.getRETRYINTERVAL()  * 1000);
////		} catch (InterruptedException e1) {
////			e1.printStackTrace();
////		}
////		new Timer().schedule(new TimerTask() {
////			@Override
////			public void run() {
////				Consumer.this.run();
////			}
////		}, 60);
//		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//		// 延迟 60s 执行
//		scheduledExecutorService.schedule(() -> {
//            ApplicationContext context = new ClassPathXmlApplicationContext("spring-rabbitmq.xml");
//            Consumer consumer = (Consumer) context.getBean("consumer");
//            consumer.run();
//			//Consumer.this.run();
//        }, ConnectionUtils.getRETRYINTERVAL() / 10, TimeUnit.SECONDS);
//	}
//
//	public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring-rabbitmq.xml");
//        Consumer consumer = (Consumer) context.getBean("consumer");
//        consumer.run();
//	}
//}
package cn.jiudao.rabbitmq.consumer;

import cn.jiudao.rabbitmq.JdbcUtils;
import cn.jiudao.rabbitmq.consumer.ConnectionUtils;
import com.rabbitmq.client.*;
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
	private static final String EXCHANGE_NAME = "alert";
	// 队列名
	private static final String QUEUE_NAME = "telesignalling";

	//private ScheduledExecutorService scheduledExecutorService;
	private Connection connection;

	private Channel channel;
	private JdbcUtils jdbcUtils;

    public void setJdbcUtils(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }
	protected void run() {
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
			//String[] message2 = new String[1];
			// 定义一个消费者
			final com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
				// 消息到达，触发这个方法
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					//message2[0] = message;
					System.out.println("Recv msg : " + message + "channel Number : " + channel.getChannelNumber());
					channel.basicAck(envelope.getDeliveryTag(), false);
					try {
						//Thread.sleep(3000);
                        // 此种写法具有更好的可读性，甚至某些时候运行速度相较于 sleep() 更快。
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
						//reConnect();
					}
                    jdbcUtils.getConnection();
                    //增加数据
                    List<Object> paramsInsert = new ArrayList<Object>();
                    paramsInsert.add(message);
                    try {
                        boolean resultInsert = jdbcUtils.updateByPreparedStatement("insert into message(message) values(?)", paramsInsert);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    jdbcUtils.releaseConn();
				}
			};
			// 设置手动 确认消息
			boolean autoAck = false;
			channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		} catch (Exception e) {
			reConnect();
		}
	}

	@Override
	public void shutdownCompleted(ShutdownSignalException e) {
    	//throw new RuntimeException("消息队列断开连接");
		System.out.println("rabbitmq 消息队列断开连接，60s 之后准备重新连接");
		reConnect();
	}

	private void reConnect() {
		System.out.println("调用 reConnect()");
//		try {
//			// 等待60s 之后开始重新连接（这里指的是60s 之后开始连接并不是到了60s之后马上开始连接，具体还是要看java虚拟机的调度）
//			Thread.sleep(ConnectionUtils.getRETRYINTERVAL()  * 1000);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				Consumer.this.run();
//			}
//		}, 60);
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		// 延迟 60s 执行
		scheduledExecutorService.schedule(() -> {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring-rabbitmq.xml");
            Consumer consumer = (Consumer) context.getBean("consumer");
            consumer.run();
        }, ConnectionUtils.getRETRYINTERVAL() / 10, TimeUnit.SECONDS);
	}

	public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-rabbitmq.xml");
        Consumer consumer = (Consumer) context.getBean("consumer");
        consumer.run();
	}
}
