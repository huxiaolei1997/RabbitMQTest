package cn.jiudao.rabbitmq;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * HelloJob
 *
 * version 1.0
 *
 * @create 2018-08-01 13:46
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class HelloJob implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.out.println(this.getClass().getName());
	}
}
