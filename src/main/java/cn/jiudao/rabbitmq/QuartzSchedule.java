package cn.jiudao.rabbitmq;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * QuartzSchedule
 *
 * version 1.0
 *
 * @create 2018-08-01 13:38
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class QuartzSchedule {
	public void runSchedule() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			scheduler.start();
			// define the job and tie it to our HelloJob class
			JobDetail job = newJob(HelloJob.class)
				.withIdentity("job1", "group1")
				.build();

			// Trigger the job to run now, and then repeat every 40 seconds
			Trigger trigger = newTrigger()
				.withIdentity("trigger1", "group1")
				.startNow()
				.withSchedule(simpleSchedule()
					.withIntervalInSeconds(5)
					.repeatForever())
				.build();

			// Tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(job, trigger);
			Thread.sleep(20000);
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
