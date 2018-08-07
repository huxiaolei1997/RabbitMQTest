package cn.jiudao.rabbitmq.consumer;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TestExecutor
 *
 * version 1.0
 *
 * @create 2018-08-07 17:20
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class TestExecutor {
    private ExecutorService executorService;
    private TestExecutor testExecutor;
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        // 延迟 5s 执行
        long startTime = System.currentTimeMillis();
        scheduledExecutorService.schedule(() -> {
            System.out.println("test");
        }, 5, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();
        System.out.println("total time = " + (endTime - startTime));
        // 该方法调用之后不允许往线程池里面添加新的线程，此时线程池的状态将会变为 SHUTDOWN 状态，所有在调用 shutdown() 方法之前提交的任务都会执行
        // 一旦线程池结束执行所有的任务，线程池才会真正的关闭
        scheduledExecutorService.shutdown();
    }

    private void run() {
        testExecutor = new TestExecutor();
        System.out.println(testExecutor.hashCode());
    }
}
