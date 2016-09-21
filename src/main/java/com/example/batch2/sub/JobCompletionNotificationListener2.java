package com.example.batch2.sub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobCompletionNotificationListener2 extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener2.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("処理終了");
    }
}
