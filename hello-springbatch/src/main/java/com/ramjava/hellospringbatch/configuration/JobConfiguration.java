package com.ramjava.hellospringbatch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /*
    *  A Step represents an independent state of processing that makes up a job.
    *  There's 2 types of Steps, 1. Tasklet (An interface with a single method called execute that runs
    *  within the scope the transaction) 2. Chunk-based processing (Item-based process items individually.
    *  The 3 main components are ItemReader, ItemWriter and ItemProcessor)
    * */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution
                            , ChunkContext chunkContext) throws Exception {
                        System.out.println("Hello !!!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    /* A Job defines a flow that processing will through states. It could have many Steps
    * */
    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(step1())
                .build();
    }
}
