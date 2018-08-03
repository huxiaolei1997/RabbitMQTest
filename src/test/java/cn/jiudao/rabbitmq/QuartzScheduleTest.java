package cn.jiudao.rabbitmq;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuartzScheduleTest {

    @Test
    public void runSchedule() {
        QuartzSchedule quartzSchedule = new QuartzSchedule();
        quartzSchedule.runSchedule();
    }
}