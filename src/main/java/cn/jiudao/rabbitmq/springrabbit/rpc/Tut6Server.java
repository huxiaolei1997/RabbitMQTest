package cn.jiudao.rabbitmq.springrabbit.rpc;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Tut6Server
 *
 * version 1.0
 *
 * @create 2018-07-19 17:03
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class Tut6Server {

    @RabbitListener(queues = "tut.rpc.requests")
    public int fibonacci(int n) {
        System.out.println(" [x]  Received  requests for " + n);
        int result = fib(n);
        System.out.println(" [.] Returned " + result);
        return result;
    }

    public int fib(int n) {
        return n == 0 ? 0 : n == 1 ? 1 : (fib(n - 1 + fib(n - 2)));
    }
}
