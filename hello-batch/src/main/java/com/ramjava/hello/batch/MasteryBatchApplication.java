package com.ramjava.hello.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class MasteryBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasteryBatchApplication.class, args);
	}

}
