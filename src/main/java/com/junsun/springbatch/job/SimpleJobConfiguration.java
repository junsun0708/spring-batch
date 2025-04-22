package com.junsun.springbatch.job;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@Slf4j 
//@RequiredArgsConstructor
//@Configuration
//@EnableBatchProcessing
public class SimpleJobConfiguration {
/* 4->5 버전업으로 deprecated됨 대부분의 예제가 안돌아가서 새로 만듬. 아래에 추가
	private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .build();
    }
    
    @Bean
    public Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    */
//@Autowired
//private JobRepository jobRepository;
//
//@Bean
//public Tasklet myTasklet() {
//   return new MyTasklet();
//}
//
//@Bean
//public Job simpleJob(Step step) {
//	return new JobBuilder("simpleJob", this.jobRepository).start(step).build();
//}
//
//@Bean
//public Step simpleStep1(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
//	return new StepBuilder("simpleStep1", jobRepository).tasklet(myTasklet, transactionManager).build();
//}
	
//    @Bean
//    public Tasklet myTasklet() {
//       return new MyTasklet();
//    }
//
//    @Bean
//    public Step myStep(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("myStep", jobRepository)
//                .tasklet(myTasklet, transactionManager) // or .chunk(chunkSize, transactionManager)
//                .build();
//    }
}
