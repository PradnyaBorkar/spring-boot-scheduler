package com.springernature.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class SimpleJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("I am executing SimpleJob...");
    }
}
