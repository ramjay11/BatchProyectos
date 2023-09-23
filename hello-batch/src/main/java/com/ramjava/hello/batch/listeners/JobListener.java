package com.ramjava.hello.batch.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class JobListener implements JobExecutionListener {
    private JavaMailSender mailSender;

    public JobListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        SimpleMailMessage mailMessage =
                getSimpleMailMessage(String.format("%s is starting", jobName),
                        String.format("Per your request, we are informing you that %s is starting",
                                jobName));
        mailSender.send(mailMessage);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        SimpleMailMessage mailMessage =
                getSimpleMailMessage(String.format("%s has completed", jobName),
                        String.format("Per your request, we are informing you that %s has completed",
                                jobName));
        mailSender.send(mailMessage);
    }

    private SimpleMailMessage getSimpleMailMessage(String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("ramjayromorosa@yahoo.com");
        mail.setSubject(subject);
        mail.setText(text);
        return mail;
    }
}
