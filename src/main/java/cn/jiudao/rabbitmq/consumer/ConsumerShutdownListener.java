package cn.jiudao.rabbitmq.consumer;

import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ConsumerShutdownListener
 *
 * version 1.0
 *
 * @create 2018-08-02 17:00
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class ConsumerShutdownListener implements ShutdownListener {
	@Autowired
	private Consumer consumer;

	@Override
	public void shutdownCompleted(ShutdownSignalException e) {

	}
}
