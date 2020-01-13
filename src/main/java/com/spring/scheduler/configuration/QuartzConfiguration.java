package com.spring.scheduler.configuration;

import com.spring.scheduler.job.AnotherJob;
import com.springernature.scheduler.job.SimpleJob;
import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfiguration {

    Logger logger = LoggerFactory.getLogger(getClass());

    String SIMPLE_JOB_CRON_EXPRESSION = "0 0/1 * 1/1 * ? *";

    String ANOTHER_JOB_CRON_EXPRESSION = "0 0/2 * 1/1 * ? *";


    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        logger.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler() {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        logger.debug("Setting the Scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setTriggers(simpleJobTrigger().getObject(),
                                     anotherJobTrigger().getObject());

        return schedulerFactory;
    }

    @Bean
    public JobDetailFactoryBean simpleJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(SimpleJob.class);
        jobDetailFactory.setName("simpleJobDetail");
        jobDetailFactory.setDescription("Invoke simple Job ...");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }


    @Bean
    public CronTriggerFactoryBean simpleJobTrigger() {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(simpleJobDetail().getObject());
        trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        trigger.setCronExpression(SIMPLE_JOB_CRON_EXPRESSION);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean anotherJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(AnotherJob.class);
        jobDetailFactory.setName("anotherJobDetail");
        jobDetailFactory.setDescription("Invoke another Job ...");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }


    @Bean
    public CronTriggerFactoryBean anotherJobTrigger() {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(anotherJobDetail().getObject());
        trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        trigger.setCronExpression(ANOTHER_JOB_CRON_EXPRESSION);
        return trigger;
    }

}
