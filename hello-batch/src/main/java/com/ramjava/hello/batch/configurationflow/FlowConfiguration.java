package com.ramjava.hello.batch.configurationflow;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FlowConfiguration {

    // Flows are made up of steps
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step stepFlow1() {
        return stepBuilderFactory.get("stepFlow1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step Flow 1 from inside flow foo");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step stepFlow2() {
        return stepBuilderFactory.get("stepFlow1")
                .tasklet((contribution, chunkContext) ->{
                    System.out.println("Step Flow 2 from inside flow foo");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Flow foo() {
        // FlowBuilder is used to build a flow
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("foo");
        // Encapsulates step 1 and step 2
        flowFlowBuilder.start(stepFlow1())
                .next(stepFlow2())
                .end();
        return flowFlowBuilder.build();
    }
}
