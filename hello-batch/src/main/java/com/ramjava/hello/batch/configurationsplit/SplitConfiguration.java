package com.ramjava.hello.batch.configurationsplit;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

//@Configuration
public class SplitConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private Tasklet tasklet() {
        return new CountingTasklet();
    }
    @Bean
    public Flow splitFlow1() {
        return new FlowBuilder<Flow>("splitFlow1")
                .start(stepBuilderFactory.get("splitStep1")
                        .tasklet(tasklet()).build())
                .build();
    }

    // 2 steps in sequence
    @Bean
    public Flow splitFlow2() {
        return new FlowBuilder<Flow>("splitFlow1")
                .start(stepBuilderFactory.get("splitStep2")
                        .tasklet(tasklet()).build())
                .next(stepBuilderFactory.get("splitStep3")
                        .tasklet(tasklet()).build())
                .build();
    }

    // Run 2 steps in parallel and in separate threads
    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(splitFlow1())
                .split(new SimpleAsyncTaskExecutor()).add(splitFlow2())
                .end().build();
    }

    public static class CountingTasklet implements Tasklet {

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            System.out.println(String.format("%s has been executed on thread %s", chunkContext.getStepContext()
                    .getStepName(), Thread.currentThread().getName()));
            return RepeatStatus.FINISHED;
        }
    }

}
